#!/usr/bin/env python3
"""Write compact, deterministic, hash-checked inventory shards; preserve decisions."""
import argparse
import gzip
import hashlib
import json
from pathlib import Path

def write_catalog(scan, root):
    root.mkdir(parents=True,exist_ok=True)
    index_path=root/'inventory.json'
    previous=json.loads(index_path.read_text()) if index_path.exists() else {}
    old={r['key']:r for r in previous.get('assets',[])}
    # Compressed JSON is registry data, not an upstream asset/archive cache.
    groups={k:scan[k] for k in ['assets','overlays','special_resources','contracts']}
    files={}
    for kind,rows in groups.items():
        compact=[]
        for row in rows:
            if kind=='assets':
                row={k:row[k] for k in ['key','sources','category']}
                row['sources']=[{k:v for k,v in s.items() if k!='archive_sha256'} for s in row['sources']]
            compact.append(row)
        chunks=[]
        def emit(part):
            raw=(json.dumps(part,separators=(',',':'),ensure_ascii=False)+'\n').encode()
            data=gzip.compress(raw,mtime=0)
            if len(data)>200000 and len(part)>1:
                mid=len(part)//2;emit(part[:mid]);emit(part[mid:]);return
            name=f'catalog/{kind}-{len(chunks):04d}.json.gz'
            target=root/name;target.parent.mkdir(exist_ok=True)
            target.write_bytes(data)
            chunks.append({'path':name,'rows':len(part),'sha256':hashlib.sha256(data).hexdigest()})
        for start in range(0,len(compact),8192):emit(compact[start:start+8192])
        files[kind]=chunks
    decisions=[]
    for row in scan['assets']:
        prior=old.get(row['key'])
        if prior and prior.get('status')!='pending':
            if prior['sources']!=row['sources']:
                raise ValueError('Source changed under existing decision: '+row['key'])
            decisions.append(prior)
    index={k:scan[k] for k in ['schema_version','complete','sources','unresolved','integration_sha','first_party_projection']}
    index.update(archive_path_scan_complete=True,counts={k:len(v) for k,v in groups.items()},
                 catalog=files,assets=decisions,
                 note='Catalog shards are immutable source facts. assets holds reviewed/draft decisions merged by key. Whole-runtime completeness remains false.')
    index_path.write_text(json.dumps(index,indent=2)+'\n')
    referenced={item['path'] for group in files.values() for item in group}
    for stale in (root/'catalog').glob('*.json.gz'):
        if stale.relative_to(root).as_posix() not in referenced:stale.unlink()
    return index

def load_registry(path):
    inv=json.loads(path.read_text())
    if not inv.get('catalog'):return inv
    rows=[]
    sources={s['id']:s for s in inv['sources']}
    for item in inv['catalog']['assets']:
        target=(path.parent/item['path']).resolve()
        if not target.is_relative_to(path.parent.resolve()):raise ValueError('Unsafe catalog path')
        data=target.read_bytes()
        if hashlib.sha256(data).hexdigest()!=item['sha256']:raise ValueError('Catalog hash mismatch: '+item['path'])
        part=json.loads(gzip.decompress(data))
        if len(part)!=item['rows']:raise ValueError('Catalog row count mismatch')
        for row in part:
            for s in row['sources']:s['archive_sha256']=sources[s['source_id']]['sha256']
            row.update(status='pending',target_dimensions=None,output_sha256=None)
        rows.extend(part)
    decisions={r['key']:r for r in inv['assets']}
    known={r['key'] for r in rows}
    if not decisions.keys()<=known:raise ValueError('Decision without inventoried source')
    inv['assets']=[decisions.get(r['key'],r) for r in rows]
    if inv.get('classification_index'):
        classes=load_classifications(path.parent/inv['classification_index'],inv)
        for row in inv['assets']:
            row.update(classes[row['key']])
            if row['production_scope']=='excluded-approved':
                row['status']='excluded-approved'
    return inv

def load_classifications(path,inv=None):
    index=json.loads(path.read_text());result={}
    if inv is not None:
        signature=hashlib.sha256(json.dumps(sorted((s['id'],s['sha256']) for s in inv['sources'])).encode()).hexdigest()
        if signature!=index['source_catalog_signature']:raise ValueError('Classification source catalog changed')
    evidence_ids=set()
    for group in ['artifacts','evidence','missing_references']:
        count=0
        for item in index.get(group,[]):
            target=(path.parent/item['path']).resolve()
            if not target.is_relative_to(path.parent.resolve()):raise ValueError('Unsafe classification path')
            data=target.read_bytes()
            if hashlib.sha256(data).hexdigest()!=item['sha256']:raise ValueError('Classification hash mismatch')
            rows=json.loads(gzip.decompress(data))
            if len(rows)!=item['rows']:raise ValueError('Classification row count mismatch')
            count+=len(rows)
            if group=='artifacts':
                for row in rows:
                    if row['key'] in result:raise ValueError('Duplicate classification key')
                    result[row['key']]=row
            elif group=='evidence':
                for row in rows:
                    if row['id'] in evidence_ids:raise ValueError('Duplicate classification evidence ID')
                    evidence_ids.add(row['id'])
        if group=='artifacts' and count!=index['total']:raise ValueError('Classification total mismatch')
    if inv is not None and set(result)!={r['key'] for r in inv['assets']}:raise ValueError('Classification key coverage mismatch')
    if any(not set(row['evidence'])<=evidence_ids for row in result.values()):raise ValueError('Missing classification evidence record')
    return result

def verify_catalog(path):
    """Check every evidence shard, including the non-asset contract collections."""
    inv=json.loads(path.read_text())
    for kind,items in inv.get('catalog',{}).items():
        count=0
        for item in items:
            target=(path.parent/item['path']).resolve()
            if not target.is_relative_to(path.parent.resolve()):raise ValueError('Unsafe catalog path')
            data=target.read_bytes()
            if hashlib.sha256(data).hexdigest()!=item['sha256']:raise ValueError('Catalog hash mismatch: '+item['path'])
            rows=json.loads(gzip.decompress(data))
            if len(rows)!=item['rows']:raise ValueError('Catalog row count mismatch')
            count+=len(rows)
        if count!=inv['counts'][kind]:raise ValueError('Catalog total mismatch: '+kind)

if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--scan',type=Path,required=True);p.add_argument('--registry',type=Path,required=True);a=p.parse_args()
    index=write_catalog(json.loads(a.scan.read_text()),a.registry)
    print(json.dumps(index['counts'],sort_keys=True))
