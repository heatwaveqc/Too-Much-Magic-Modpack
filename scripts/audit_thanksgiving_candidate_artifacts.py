#!/usr/bin/env python3
"""Targeted publisher artifact evidence; no assembly, Minecraft launch or complete roster download."""
from pathlib import Path
import hashlib, json, sys, tomllib, zipfile, io
from concurrent.futures import ThreadPoolExecutor
sys.path.insert(0, str(Path(__file__).resolve().parent))
from verify_approved_recovery_artifacts import download
from verify_first_party_dependency_closure import manifest_main
ROOT=Path(__file__).resolve().parents[1]
STAGE=ROOT/"integration/lootr-addons/pins"
FILES={"sodium":STAGE/"sodium-audit-only.pw.toml","lootr-liaison":STAGE/"lootr-liaison.pw.toml","twilight-lootr":STAGE/"twilight-lootr.pw.toml","lootr":ROOT/"mods/lootr.pw.toml","twilight-forest":ROOT/"mods/twilight-forest.pw.toml"}
def inspect(data, label, depth=0):
    if depth>4: raise ValueError("nested jar depth limit")
    out=[]
    with zipfile.ZipFile(io.BytesIO(data)) as z:
        bad=z.testzip()
        if bad: raise ValueError(f"bad ZIP {bad}")
        names=z.namelist()
        for name in ("META-INF/neoforge.mods.toml","META-INF/mods.toml"):
            if name not in names: continue
            meta=tomllib.loads(z.read(name).decode("utf-8-sig"))
            mods=meta.get("mods",[])
            if "META-INF/MANIFEST.MF" in names:
                mf=manifest_main(z.read("META-INF/MANIFEST.MF"))
                for mod in mods:
                    if mod.get("version")=="${file.jarVersion}": mod["effective_version"]=mf.get("Implementation-Version")
            out.append({"jar":label,"metadata":name,"mods":mods,"dependencies":meta.get("dependencies",{}),"loaderVersion":meta.get("loaderVersion")})
        if "fabric.mod.json" in names:
            meta=json.loads(z.read("fabric.mod.json"))
            out.append({"jar":label,"metadata":"fabric.mod.json","id":meta.get("id"),"version":meta.get("version"),"depends":meta.get("depends"),"breaks":meta.get("breaks")})
        for name in names:
            if name.lower().endswith(".jar") and name.startswith("META-INF/"):
                if z.getinfo(name).file_size>105*1024*1024: raise ValueError("nested JAR too large")
                out.extend(inspect(z.read(name),label+"!"+name,depth+1))
    return out
def audit(pair):
    slug,path=pair
    row={"slug":slug}
    try:
        pin=tomllib.loads(path.read_text())
        data,url=download(pin)
        lock=pin["download"]; got=hashlib.new(lock["hash-format"],data).hexdigest()
        row.update({"source":url,"filename":pin["filename"],"expected":lock["hash"],"actual":got,"sha256":hashlib.sha256(data).hexdigest(),"hash_matches":got==lock["hash"],"metadata":inspect(data,pin["filename"])})
        if not row["hash_matches"]: raise ValueError("publisher bytes differ from staged hash")
        if not row["metadata"]: raise ValueError("no mod metadata")
    except Exception as e: row["error"]=str(e)
    return row
with ThreadPoolExecutor(max_workers=5) as pool:
    rows=list(pool.map(audit,FILES.items()))
report={"artifacts":rows,"limitations":"Only these five explicit artifacts audited. No complete client roster cache; Sodium reverse dependency audit remains open. Native version ranges and required dependency closure require review; successful download is not gameplay acceptance."}
Path("thanksgiving-candidate-artifacts.json").write_text(json.dumps(report,indent=2,default=str)+"\n")
print(json.dumps(report,indent=2,default=str))
sys.exit(1 if any("error" in r for r in rows) else 0)
