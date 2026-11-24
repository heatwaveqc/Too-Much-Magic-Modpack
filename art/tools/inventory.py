#!/usr/bin/env python3
"""Inventory verified local archives; never extract images or mark art complete."""
import argparse
import hashlib
import json
import re
import struct
import zipfile
import io
from pathlib import Path

ASSET = re.compile(r"^assets/[a-z0-9_.-]+/textures/[a-z0-9_./-]+\.png$")

def sha(data):
    return hashlib.sha256(data).hexdigest()

def file_sha(path):
    digest = hashlib.sha256()
    with open(path, "rb") as stream:
        for chunk in iter(lambda: stream.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()

def dimensions(data):
    if len(data) < 33 or data[:8] != b"\x89PNG\r\n\x1a\n" or data[12:16] != b"IHDR":
        raise ValueError("invalid PNG header")
    w, h = struct.unpack(">II", data[16:24])
    if not w or not h:
        raise ValueError("zero-sized PNG")
    return [w, h]

def inventory(specs):
    sources, records, issues, ids = [], {}, [], set()
    for spec in sorted(specs):
        source_id, sep, filename = spec.partition("=")
        if not sep or not source_id or source_id in ids:
            raise ValueError("each --source must have unique id=path")
        ids.add(source_id)
        p = Path(filename)
        try:
            archive_hash = file_sha(p)
            with zipfile.ZipFile(p) as z:
                names = z.namelist()
                sources.append({"id": source_id, "filename": p.name, "sha256": archive_hash,
                                "provider_hash_verified": False, "scan": "top-level PNGs only"})
                if len(names) != len(set(names)):
                    issues.append(f"{source_id}: duplicate archive member names")
                name_set = set(names)
                nested = sum(n.endswith(".jar") for n in names)
                other = sum("/textures/" in n and not n.endswith((".png", ".mcmeta", "/")) for n in names)
                if nested: issues.append(f"{source_id}: {nested} nested JAR(s) need separate audit")
                if other: issues.append(f"{source_id}: {other} non-PNG texture paths need review")
                for name in sorted(name_set):
                    if not ASSET.fullmatch(name):
                        if "/textures/" in name and name.endswith(".png"):
                            issues.append(f"{source_id}: noncanonical/overlay path {name}")
                        continue
                    if ".." in name.split("/"):
                        issues.append(f"{source_id}: unsafe member {name}")
                        continue
                    try:
                        data = z.read(name)
                        dims = dimensions(data)
                    except (ValueError, zipfile.BadZipFile, RuntimeError) as exc:
                        issues.append(f"{source_id}: {name}: {exc}")
                        continue
                    meta = name + ".mcmeta"
                    entry = {"source_id": source_id, "archive_sha256": archive_hash,
                             "asset_sha256": sha(data), "dimensions": dims,
                             "metadata_sha256": sha(z.read(meta)) if meta in name_set else None}
                    row = records.setdefault(name, {"key": name, "sources": [], "category": "unclassified",
                                                   "target_dimensions": None, "status": "pending",
                                                   "style_version": None, "batch": None,
                                                   "output_sha256": None, "provenance": None,
                                                   "review": None, "game_test": None, "exception": None})
                    row["sources"].append(entry)
        except (OSError, zipfile.BadZipFile) as exc:
            issues.append(f"{source_id}: archive unavailable/invalid: {exc}")
    for key, row in records.items():
        if len(row["sources"]) > 1: issues.append(f"{key}: source precedence unresolved")
    return {"schema_version": 1, "complete": False, "sources": sources,
            "assets": [records[k] for k in sorted(records)],
            "unresolved": sorted(set(issues + ["Verify provider locks, roster closure, overlays, generated assets and renderer contracts."]))}


def audit_archives(specs, verified_ids=()):
    """Recursive archive closure plus overlays and renderer evidence, without extraction.

    Runtime-generated resources still require a client audit. Nested archives retain
    their own hashes and parent identity; overlay keys never masquerade as base keys.
    """
    result={'schema_version':2,'complete':False,'sources':[],'assets':[],
            'overlays':[],'special_resources':[],'contracts':[],'unresolved':[]}
    records={}
    def scan(data, source_id, parent=None, depth=0):
        digest=sha(data)
        with zipfile.ZipFile(io.BytesIO(data)) as z:
            names=z.namelist()
            source={'id':source_id,'filename':source_id.split('!')[-1],
                    'sha256':digest,'provider_hash_verified':source_id.split('!')[0] in verified_ids,
                    'parent':parent,'scan':'recursive PNG + special resources + JSON contracts',
                    'png_count':0,'nested_archives':[]}
            result['sources'].append(source)
            if len(names)!=len(set(names)):
                result['unresolved'].append(source_id+': duplicate ZIP members')
            for name in sorted(set(names)):
                if name.endswith('/'):
                    continue
                if '..' in name.split('/') or name.startswith('/'):
                    result['unresolved'].append(source_id+': unsafe member '+name)
                    continue
                if name.endswith('.jar'):
                    source['nested_archives'].append(name)
                    if depth>=8 or z.getinfo(name).file_size>256*1024*1024:
                        result['unresolved'].append(source_id+': nested archive limit '+name)
                    else:
                        scan(z.read(name),source_id+'!'+name,source_id,depth+1)
                    continue
                if name.endswith('.png'):
                    raw=z.read(name)
                    try: dims=dimensions(raw)
                    except ValueError as exc:
                        result['unresolved'].append(source_id+': '+name+': '+str(exc)); continue
                    source['png_count']+=1
                    evidence={'source_id':source_id,'archive_sha256':digest,
                              'asset_sha256':sha(raw),'dimensions':dims,
                              'metadata_sha256':sha(z.read(name+'.mcmeta')) if name+'.mcmeta' in names else None}
                    if ASSET.fullmatch(name):
                        category=name.split('/')[3]
                        row=records.setdefault(name,{'key':name,'sources':[],'category':category,
                            'target_dimensions':None,'status':'pending','style_version':None,
                            'batch':None,'output_sha256':None,'provenance':None,'review':None,
                            'game_test':None,'exception':None})
                        row['sources'].append(evidence)
                    elif '/assets/' in name and '/textures/' in name:
                        result['overlays'].append({'path':name,**evidence,'status':'pending-overlay-contract'})
                    else:
                        result['special_resources'].append({'path':name,**evidence,'status':'pending-applicability'})
                elif name.endswith(('.json','.mcmeta')) and (name.startswith('assets/') or '/assets/' in name or name=='pack.mcmeta'):
                    # Models, atlases, animation and GUI nine-slice metadata are technical
                    # contracts, not copied artwork. Record only necessary JSON evidence.
                    if not any(t in name for t in ['/models/','/atlases/','.png.mcmeta','pack.mcmeta','/particles/','/font/']):
                        continue
                    raw=z.read(name)
                    try: obj=json.loads(raw)
                    except (ValueError,UnicodeDecodeError):
                        result['unresolved'].append(source_id+': invalid JSON '+name); continue
                    contract={'source_id':source_id,'path':name,'sha256':sha(raw)}
                    if name.endswith('.mcmeta') or '/atlases/' in name:
                        contract['contract']=obj
                    elif '/models/' in name and isinstance(obj,dict):
                        contract['parent']=obj.get('parent')
                        contract['textures']=obj.get('textures',{})
                        contract['has_explicit_uv']=any('uv' in face for el in obj.get('elements',[]) for face in el.get('faces',{}).values())
                        contract['element_count']=len(obj.get('elements',[]))
                    elif '/particles/' in name:
                        contract['textures']=obj.get('textures',[]) if isinstance(obj,dict) else []
                    result['contracts'].append(contract)
                elif '/textures/' in name and not name.endswith('.mcmeta'):
                    result['special_resources'].append({'source_id':source_id,'path':name,
                        'sha256':sha(z.read(name)),'status':'non-PNG-applicability-review'})
    for spec in sorted(specs):
        source_id,sep,filename=spec.partition('=')
        if not sep or not source_id or any(s['id']==source_id for s in result['sources']):
            raise ValueError('each --source must have unique id=path')
        try: scan(Path(filename).read_bytes(),source_id)
        except (OSError,ValueError,zipfile.BadZipFile) as exc:
            result['unresolved'].append(source_id+': '+str(exc))
    for key,row in sorted(records.items()):
        if len(row['sources'])>1:
            result['unresolved'].append(key+': source precedence unresolved')
        result['assets'].append(row)
    result['unresolved']+=['Runtime-generated resources and hardcoded renderers require separate client/source audit.',
        'Overlay activation, special-resource applicability and duplicate precedence require review.']
    return result

def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--source", action="append", required=True, metavar="ID=PATH")
    p.add_argument("--out", required=True, type=Path)
    args = p.parse_args()
    result = inventory(args.source)
    if args.out.exists():
        p.error("refusing overwrite: choose a scratch output and reconcile intentionally")
    args.out.parent.mkdir(parents=True, exist_ok=True)
    args.out.write_text(json.dumps(result, indent=2) + "\n", encoding="utf-8")
    print(f"{len(result['sources'])} archives; {len(result['assets'])} asset paths; {len(result['unresolved'])} unresolved checks. Coverage incomplete.")

if __name__ == "__main__":
    main()
