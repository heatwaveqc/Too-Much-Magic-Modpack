#!/usr/bin/env python3
"""Fetch the frozen art-inspection inputs only; never install or launch anything."""
import argparse
from concurrent.futures import ThreadPoolExecutor, as_completed
from http.client import HTTPException
import hashlib
import json
from pathlib import Path
import urllib.request
from urllib.parse import urlparse
import zipfile

HOSTS = {'cdn.modrinth.com', 'mediafilez.forgecdn.net', 'edge.forgecdn.net', 'piston-data.mojang.com',
         'github.com', 'release-assets.githubusercontent.com'}

def acquire(pin, cache):
    row = dict(pin)
    name = pin['filename']
    if Path(name).name != name or pin['provider_hash_algorithm'] not in {'sha1','sha256','sha512'}:
        raise ValueError('Unsafe source lock')
    target = cache / name
    def verified(data):
        return hashlib.new(pin['provider_hash_algorithm'], data).hexdigest() == pin['provider_hash']
    data = target.read_bytes() if target.exists() else None
    failures = []
    if data is None or not verified(data):
        data = None
        for url in pin['urls']:
            try:
                if urlparse(url).scheme != 'https' or urlparse(url).hostname not in HOSTS:
                    raise ValueError('Unreviewed source host: ' + url)
                request = urllib.request.Request(url, headers={'User-Agent':'TooMuchMagic-ArtInventory/0.1'})
                with urllib.request.urlopen(request, timeout=35) as response:
                    if urlparse(response.url).hostname not in HOSTS:
                        raise ValueError('Unreviewed redirect host')
                    candidate = response.read()
                if not verified(candidate):
                    raise ValueError('Provider hash mismatch')
                data = candidate
                target.write_bytes(data)
                break
            except (OSError, ValueError, HTTPException) as exc:
                failures.append(str(exc))
    if data is None:
        row.update(status='blocked', errors=failures)
    elif not zipfile.is_zipfile(target):
        row.update(status='blocked', errors=['Not a ZIP/JAR archive'])
    else:
        row.update(status='verified', sha256=hashlib.sha256(data).hexdigest(), bytes=len(data))
    return row

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument('--lock',type=Path,required=True)
    p.add_argument('--cache',type=Path,required=True)
    p.add_argument('--report',type=Path,required=True)
    args=p.parse_args()
    args.cache.mkdir(parents=True,exist_ok=True)
    lock=json.loads(args.lock.read_text())
    rows=[]
    with ThreadPoolExecutor(max_workers=6) as pool:
        futures=[pool.submit(acquire,pin,args.cache) for pin in lock['archives']]
        for future in as_completed(futures):
            rows.append(future.result())
            report={'schema_version':1,'integration_sha':lock['integration_sha'],
                    'acquisition_complete':len(rows)==len(lock['archives']),
                    'archives':sorted(rows,key=lambda r:r['id'])}
            temporary=args.report.with_suffix('.tmp')
            temporary.write_text(json.dumps(report,indent=2)+'\n')
            temporary.replace(args.report)
    print(json.dumps({s:sum(r['status']==s for r in rows) for s in ['verified','blocked']}))

if __name__=='__main__': main()
