#!/usr/bin/env python3
"""Reproduce the full static art scan from frozen inputs, without a Minecraft build."""
import argparse
from collections import Counter
import hashlib
import json
from pathlib import Path
import subprocess
import zipfile
from inventory import audit_archives

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument('--lock',type=Path,required=True)
    p.add_argument('--cache',type=Path,required=True)
    p.add_argument('--out',type=Path,required=True)
    args=p.parse_args()
    lock=json.loads(args.lock.read_text());ref=lock['integration_sha']
    pins={x['id']:x for x in lock['archives']};specs=[];blocked=[]
    for sid,pin in sorted(pins.items()):
        archive=args.cache/pin['filename']
        if not archive.exists():blocked.append(sid+': missing archive');continue
        data=archive.read_bytes()
        if hashlib.new(pin['provider_hash_algorithm'],data).hexdigest()!=pin['provider_hash']:
            raise ValueError('Changed/unverified source: '+sid)
        specs.append(f'{sid}={archive}')
    def git(*commands):return subprocess.check_output(['git',*commands])
    paths=git('ls-tree','-r','--name-only',ref).decode().splitlines()
    entries=[(x,'assets/'+x.split('/assets/',1)[1]) for x in paths
             if '/src/main/resources/assets/' in x or x.startswith('kubejs/assets/')]
    projection=args.cache/'first-party-source-resources.zip'
    with zipfile.ZipFile(projection,'w',zipfile.ZIP_DEFLATED) as z:
        for path,name in entries:
            info=zipfile.ZipInfo(name,(1980,1,1,0,0,0))
            z.writestr(info,git('show',f'{ref}:{path}'))
    specs.append(f'first-party-source-resources={projection}')
    result=audit_archives(specs,pins.keys())
    for source in result['sources']:
        if source['id'] not in pins:continue
        source['filename']=pins[source['id']]['filename']
        with zipfile.ZipFile(args.cache/source['filename']) as z:
            counts=Counter(i.filename for i in z.infolist())
            duplicate=[{'path':i.filename,'zip_header_offset':i.header_offset,
                        'sha256':hashlib.sha256(z.read(i)).hexdigest()}
                       for i in z.infolist() if counts[i.filename]>1]
            if duplicate:source['duplicate_members']=duplicate
    result['integration_sha']=ref
    result['first_party_projection']={'integration_sha':ref,
        'kind':'source-resource projection, NOT built runtime JAR','source_paths':[x for x,n in entries]}
    result['unresolved']+=blocked+[
        '12 desired but unpinned future roster entries have no approved exact archive in this runtime lock.',
        'Built first-party JARs and loader-injected/generated client resources require runtime closure; source resources inspected only.']
    args.out.write_text(json.dumps(result,separators=(',',':'))+'\n')
    print(json.dumps({k:len(result[k]) for k in ['sources','assets','overlays','special_resources','contracts','unresolved']}))

if __name__=='__main__':main()
