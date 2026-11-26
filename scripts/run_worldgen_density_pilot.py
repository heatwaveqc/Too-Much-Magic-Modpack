#!/usr/bin/env python3
"""One authorized paired TMM experiment: frozen server + exact reviewed density overlay."""
import argparse
import copy
import hashlib
import json
import shutil
import sys
import tomllib
from pathlib import Path

from run_worldgen_vanilla_control import occupancy

TOOLS="990c97ce355aac191419354809ab013efddb10f0"
DENSITY="5beae36105a61e5671d5a9e12989bfa0a8ae2904"
BASELINE="721ea058294f838b9d7f145af427f5db228d248d"
MANIFEST="research/worldgen-surface-density/first-pass.json"
PROTECTED={"astralsorcery:focal_point_structures","botania:exploration_mushroom"}

def digest(p):
    return hashlib.sha256(p.read_bytes()).hexdigest()

def plan(frozen,overlay):
    manifest=json.loads((overlay/MANIFEST).read_text())
    if manifest["source_snapshot"]!=BASELINE or manifest["multiplier"]!=0.5 or set(manifest["protected"])!=PROTECTED:
        raise ValueError("unexpected density manifest")
    old={r["file"]:r["hash"] for r in tomllib.loads((frozen/"index.toml").read_text())["files"]}
    new={r["file"]:r["hash"] for r in tomllib.loads((overlay/"index.toml").read_text())["files"]}
    for pin,sha in manifest["pins"].items():
        if old.get(pin)!=sha or new.get(pin)!=sha or digest(frozen/pin)!=sha or digest(overlay/pin)!=sha:
            raise ValueError("frozen density pin mismatch: "+pin)
    rows={}
    for e in manifest["entries"]:
        rid=e["id"]
        ns,name=rid.split(":")
        import re
        if not re.fullmatch(r"[a-z0-9_.-]+:[a-z0-9_/-]+",rid) or rid in PROTECTED or ns in {"minecraft","alexscaves","pastel","dungeoncrawl","betterdungeons","bettermineshafts"}:
            raise ValueError("unexpected structure ID: "+rid)
        if e["pin"] not in manifest["pins"] or any(s["structure"] in {"astralsorcery:focal_point","botania:exploration_mushroom"} for s in e["original"]["structures"]):
            raise ValueError("protected or unpinned structure member")
        path=f"kubejs/data/{ns}/worldgen/structure_set/{name}.json"
        if path in rows or (frozen/path).exists() or path in old:
            raise ValueError("duplicate or existing baseline override: "+path)
        if new.get(path)!=digest(overlay/path):
            raise ValueError("density file hash mismatch")
        expected=copy.deepcopy(e["original"])
        placement=expected["placement"]
        if placement["type"]!="minecraft:random_spread":
            raise ValueError("unsupported placement codec")
        placement["frequency"]=placement.get("frequency",1)*0.5
        if json.loads((overlay/path).read_text())!=expected:
            raise ValueError("not the approved frequency-only override: "+path)
        rows[path]=new[path]
    if len(rows)!=69 or len(manifest["pins"])!=13:
        raise ValueError("unexpected approved overlay size")
    return rows

def install(server,overlay,rows):
    # Never write into the frozen checkout or an existing player world.
    for path,sha in sorted(rows.items()):
        dest=server/path
        if dest.exists(): raise ValueError("unexpected staged override: "+path)
        dest.parent.mkdir(parents=True,exist_ok=True)
        shutil.copy2(overlay/path,dest)
        if digest(dest)!=sha: raise ValueError("staged override digest mismatch")
    return {"count":len(rows),"sha256":rows}

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--tools",required=True,type=Path)
    p.add_argument("--pack-root",required=True,type=Path)
    p.add_argument("--density-root",required=True,type=Path)
    p.add_argument("--work",required=True,type=Path)
    p.add_argument("--authorized",action="store_true")
    p.add_argument("--check-only",action="store_true")
    a=p.parse_args()
    if not a.authorized and not a.check_only: p.error("explicit density pilot execution approval required")
    tools=a.tools.resolve()
    sys.path.insert(0,str(tools/"scripts"))
    import run_worldgen_pilot as pilot
    frozen=a.pack_root.resolve(); overlay=a.density_root.resolve()
    for root,sha in ((tools,TOOLS),(frozen,BASELINE),(overlay,DENSITY)):
        if pilot.read_output(["git","rev-parse","HEAD"],root)!=sha:
            raise ValueError("incorrect exact input checkout")
        if pilot.read_output(["git","status","--porcelain","--untracked-files=no"],root):
            raise ValueError("dirty exact input checkout")
    rows=plan(frozen,overlay)
    print("DENSITY_PLAN "+json.dumps({"count":len(rows),"overlay_sha":DENSITY}),flush=True)
    if a.check_only: return 0
    server,evidence=pilot.stage(frozen,a.work.resolve(),pilot.CARBON_SHA,True)
    pilot.progress("install only approved density overrides into disposable server")
    staged=install(server,overlay,rows)
    provenance=json.loads((evidence/"provenance.json").read_text())
    provenance.update(mode="tmm-density-half",density_source=DENSITY,density_overlay=staged)
    (evidence/"provenance.json").write_text(json.dumps(provenance,indent=2))
    (evidence/"density-overlay.json").write_text(json.dumps(staged,indent=2))
    result=pilot.generate(server,evidence,1800,True)
    if result: return result
    with (evidence/"placements.tsv").open() as f:
        summary=occupancy(f)
    (evidence/"occupancy.json").write_text(json.dumps(summary,indent=2))
    print("DENSITY_OCCUPANCY "+json.dumps(summary),flush=True)
    return 0

if __name__=="__main__":raise SystemExit(main())
