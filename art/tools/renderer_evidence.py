#!/usr/bin/env python3
"""Extract technical JSON and class-literal evidence; never export upstream pixels."""
import argparse
import hashlib
import io
import json
from pathlib import Path
import struct
import zipfile

def class_info(raw):
    """Minimal JVM constant-pool reader: names, inheritance and literal strings."""
    if raw[:4]!=b'\xca\xfe\xba\xbe':raise ValueError('Not a JVM class')
    count=struct.unpack_from('>H',raw,8)[0];pool=[None]*count;p=10;i=1
    while i<count:
        tag=raw[p];p+=1
        if tag==1:
            n=struct.unpack_from('>H',raw,p)[0];p+=2
            pool[i]=(tag,raw[p:p+n].decode('utf-8',errors='replace'));p+=n
        elif tag in (7,8,16,19,20):pool[i]=(tag,struct.unpack_from('>H',raw,p)[0]);p+=2
        elif tag in (3,4):p+=4
        elif tag in (5,6):p+=8;i+=1
        elif tag in (9,10,11,12,17,18):p+=4
        elif tag==15:p+=3
        else:raise ValueError('Unknown JVM constant tag '+str(tag))
        i+=1
    def cls(index):return pool[pool[index][1]][1] if index else None
    _,this,parent,n=struct.unpack_from('>HHHH',raw,p)
    interfaces=[cls(struct.unpack_from('>H',raw,p+8+2*j)[0]) for j in range(n)]
    strings=[pool[x[1]][1] for x in pool if x and x[0]==8]
    strings=[s for s in strings if '/' in s and len(s)<300]
    referenced=[cls(i) for i,x in enumerate(pool) if x and x[0]==7]
    return {'name':cls(this),'parent':cls(parent),'interfaces':interfaces,
            'referenced_classes':referenced,'texture_literals':strings}

def collect(lock,cache,mappings=None):
    output={'models':[],'fonts':[],'blockstates':[],'metadata':[],'classes':[],'errors':[]}
    rename={}
    if mappings:
        for line in mappings.read_text().splitlines():
            if line and not line[0].isspace() and ' -> ' in line and line.endswith(':'):
                named,obf=line[:-1].split(' -> ')
                rename[obf.replace('.','/')]=named.replace('.','/')
    def scan(raw,sid,depth=0):
        if depth>8:raise ValueError('Nested depth exceeded')
        with zipfile.ZipFile(io.BytesIO(raw)) as z:
            names=set(z.namelist())
            for name in sorted(names):
                if name.endswith('.jar'):
                    scan(z.read(name),sid+'!'+name,depth+1);continue
                if not name.endswith(('.json','.mcmeta','.class')):continue
                if name.endswith('.class'):
                    data=z.read(name)
                    try:c=class_info(data)
                    except (ValueError,IndexError,struct.error) as e:
                        output['errors'].append({'source_id':sid,'path':name,'error':str(e)});continue
                    c['name']=rename.get(c['name'],c['name'])
                    c['parent']=rename.get(c['parent'],c['parent'])
                    c['interfaces']=[rename.get(x,x) for x in c['interfaces']]
                    c['referenced_classes']=[rename.get(x,x) for x in c['referenced_classes']]
                    c.update(source_id=sid,path=name,sha256=hashlib.sha256(data).hexdigest())
                    output['classes'].append(c);continue
                kind=next((kind for token,kind in [('/models/','models'),('/font/','fonts'),('/blockstates/','blockstates'),('.png.mcmeta','metadata')] if token in name),None)
                if not kind or not name.startswith('assets/'):continue
                data=z.read(name)
                try:obj=json.loads(data)
                except (UnicodeDecodeError,ValueError):continue
                e={'source_id':sid,'path':name,'sha256':hashlib.sha256(data).hexdigest()}
                if kind=='models':
                    e.update(parent=obj.get('parent'),textures=obj.get('textures',{}),
                        element_count=len(obj.get('elements',[])),loader=obj.get('loader'),
                        render_type=obj.get('render_type'),
                        faces=[{'texture':f.get('texture'),'tintindex':f.get('tintindex')}
                               for element in obj.get('elements',[]) for f in element.get('faces',{}).values()])
                else:e['contract']=obj
                output[kind].append(e)
    for pin in lock['archives']:
        p=cache/pin['filename']
        raw=p.read_bytes()
        if hashlib.new(pin['provider_hash_algorithm'],raw).hexdigest()!=pin['provider_hash']:
            raise ValueError('Unverified archive '+pin['id'])
        scan(raw,pin['id'])
    return output

if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument('--lock',type=Path,required=True);p.add_argument('--cache',type=Path,required=True)
    p.add_argument('--mappings',type=Path);p.add_argument('--out',type=Path,required=True)
    a=p.parse_args();result=collect(json.loads(a.lock.read_text()),a.cache,a.mappings)
    a.out.write_text(json.dumps(result,separators=(',',':'))+'\n')
    print(json.dumps({k:len(v) for k,v in result.items()}))
