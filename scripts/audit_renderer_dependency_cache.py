#!/usr/bin/env python3
"""Offline renderer dependency audit; takes already cached exact JARs, never downloads."""
import argparse, hashlib, io, json, tomllib, zipfile
from pathlib import Path
ROOT=Path(__file__).resolve().parents[1]
def digest(path,kind):
    with path.open("rb") as f: return hashlib.file_digest(f,kind).hexdigest()
def audit_jar(data,label,depth=0):
    if depth>4: raise ValueError("nested archive depth limit")
    out=[]; refs=[]
    with zipfile.ZipFile(io.BytesIO(data)) as z:
        for path in ("META-INF/neoforge.mods.toml","META-INF/mods.toml"):
            if path not in z.namelist(): continue
            meta=tomllib.loads(z.read(path).decode("utf-8-sig"))
            for owner,deps in meta.get("dependencies",{}).items():
                for dep in deps:
                    if dep.get("modId","").lower() in ("embeddium","sodium"):
                        out.append({"jar":label,"owner":owner,**dep})
        if "fabric.mod.json" in z.namelist():
            meta=json.loads(z.read("fabric.mod.json"))
            for kind in ("depends","recommends","suggests","conflicts","breaks"):
                for target,version in (meta.get(kind) or {}).items():
                    if target.lower() in ("embeddium","sodium"): out.append({"jar":label,"owner":meta.get("id"),"modId":target,"type":kind,"versionRange":version})
        for name in z.namelist():
            if name.lower().endswith(".jar") and name.startswith("META-INF/"):
                if z.getinfo(name).file_size>105*1024*1024: raise ValueError("nested archive too large")
                a,b=audit_jar(z.read(name),label+"!"+name,depth+1);out.extend(a);refs.extend(b)
            elif name.endswith((".json",".class")) and ("mixin" in name.lower() or "compat" in name.lower()):
                if z.getinfo(name).file_size>4*1024*1024: continue
                body=z.read(name).lower()
                if b"embeddium" in body: refs.append({"jar":label,"member":name})
    return out,refs
def main():
    p=argparse.ArgumentParser()
    p.add_argument("--jar-dir",type=Path,required=True,action="append",help="Existing cache/installed mods directory; may repeat")
    p.add_argument("--first-party-dir",type=Path,required=True,help="Exact current compiled first-party JAR directory")
    p.add_argument("--json-out",type=Path,default=Path("renderer-dependency-audit.json"))
    a=p.parse_args(); index=tomllib.loads((ROOT/"index.toml").read_text())
    scanned=[]; missing=[]; declarations=[]; refs=[]; errors=[]; first_party=[]
    for row in index["files"]:
        path=row["file"]
        if not (path.startswith("mods/") and path.endswith(".pw.toml")): continue
        pin=tomllib.loads((ROOT/path).read_text()); side=pin.get("side","both")
        if side=="server": continue
        file=pin["filename"]; lock=pin["download"]
        candidates=[d/file for d in a.jar_dir if (d/file).is_file()]
        jar=next((j for j in candidates if digest(j,lock["hash-format"])==lock["hash"].lower()),None)
        if jar is None: missing.append({"pin":path,"filename":file,"reason":"missing exact hash-matching JAR"}); continue
        try:
            d,r=audit_jar(jar.read_bytes(),file); declarations.extend(d); refs.extend(r); scanned.append({"pin":path,"filename":file,"hash":lock["hash"]})
        except Exception as e: errors.append({"filename":file,"error":str(e)})
    manifest=json.loads((ROOT/"manifest/first-party-runtime.json").read_text())
    for row in manifest["modules"]:
        mod=row["mod_id"]; props=dict(line.split("=",1) for line in (ROOT/row["directory"]/"gradle.properties").read_text().splitlines() if "=" in line and not line.startswith("#"))
        filename=mod+"-"+props["mod_version"]+".jar"; jar=a.first_party_dir/filename
        if not jar.is_file(): missing.append({"first_party":mod,"filename":filename});continue
        try:
            d,r=audit_jar(jar.read_bytes(),filename);declarations.extend(d);refs.extend(r);first_party.append({"filename":filename,"sha256":digest(jar,"sha256"),"provenance":"caller must confirm compiled from exact candidate"})
        except Exception as e: errors.append({"filename":filename,"error":str(e)})
    blockers=[d for d in declarations if d.get("modId","").lower()=="embeddium" and (d.get("type") in ("required","depends") or d.get("mandatory") is True)]
    blockers += [d for d in declarations if d.get("modId","").lower()=="sodium" and d.get("type") in ("incompatible","conflicts","breaks")]
    result={"scanned":scanned,"first_party":first_party,"missing":missing,"errors":errors,"renderer_declarations":declarations,"embeddium_mixin_and_compat_references":refs,"hard_blockers":blockers,"review_required":"Review optional declarations, mixin references, first-party build provenance and Sodium native version ranges before approval; this report is not runtime acceptance."}
    a.json_out.write_text(json.dumps(result,indent=2)+"\n");print(json.dumps({"scanned":len(scanned),"first_party":len(first_party),"missing":len(missing),"errors":len(errors),"hard_blockers":blockers,"manual_reference_review":len(refs)},indent=2))
    return 1 if missing or errors or blockers else 0
if __name__=="__main__": raise SystemExit(main())
