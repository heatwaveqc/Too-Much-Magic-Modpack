#!/usr/bin/env python3
"""B5 dependency repair #2: Caelus 7.0.1, Paucal 0.7, Ars Elemancy 1.18.3.

Ars 'n Spells 3.3.5 requires Ars Elemancy >=1.18.3; initial Modrinth
release was stale 1.17, so replace that artifact *without duplicate mod ID*.
The two additional libraries are required by HexCast and Enigmatic Legacy+.
"""
from __future__ import annotations
import json
from pathlib import Path
import subprocess
import sys
import tomllib

ROOT=Path(__file__).resolve().parents[1]
MANIFEST=ROOT/"manifest/mods.json"
EVIDENCE=ROOT/"docs/B5-RESOLUTION.json"
# Exact CurseForge project identities verified on official pages.
ADDITIONS=[
    ("caelus",308989,"caelus-neoforge-7.0.1+1.21.1"),
    ("paucal",597824,"paucal-0.7.0+neoforge-1.21.1"),
    ("ars-elemancy",1153666,"ars_elemancy-1.21.1-1.18.3")
]

def run(*args):
    p=subprocess.run(args,cwd=ROOT,text=True,stdout=subprocess.PIPE,
                     stderr=subprocess.STDOUT,timeout=180)
    return p.returncode,p.stdout

def main():
    original=json.loads(MANIFEST.read_text())
    byid={x["id"]:x for x in original["mods"]}
    before={x["id"] for x in original["mods"] if x.get("enabled_in_pack")}
    # Prevent double-loading Ars Elemancy via Modrinth and CurseForge.
    old=ROOT/"mods/ars-elemancy.pw.toml"
    if not old.exists():raise RuntimeError("Old Ars Elemancy metadata missing")
    om=tomllib.loads(old.read_text())
    if om.get("update",{}).get("modrinth",{}).get("mod-id")!="mR4yp7HM":
        raise RuntimeError("Unexpected old Ars Elemancy project")
    old.unlink()
    rc,out=run("packwiz","refresh")
    if rc:raise RuntimeError("Unable to remove stale 1.17 from index: "+out[-1400:])
    for mid,pid,expected in ADDITIONS:
        rc,out=run("packwiz","--yes","curseforge","add","--addon-id",str(pid))
        print("REPAIR "+mid+": "+("OK" if rc==0 else "FAILED"),flush=True)
        if rc:
            print(out[-1300:],flush=True)
            raise RuntimeError("CurseForge pin failed: "+mid)
    rc,out=run("packwiz","refresh")
    if rc:raise RuntimeError("packwiz refresh failed: "+out[-1300:])
    pid_map={pid:(mid,expect) for mid,pid,expect in ADDITIONS}
    old_ids={x.get("packwiz_slug",x["id"]):x["id"] for x in original["mods"]}
    observed=set()
    extras=[]
    items=[]
    for p in sorted((ROOT/"mods").glob("*.pw.toml")):
        slug=p.name.removesuffix(".pw.toml")
        data=tomllib.loads(p.read_text())
        cf=data.get("update",{}).get("curseforge",{})
        mid=pid_map[cf["project-id"]][0] if cf.get("project-id") in pid_map else old_ids.get(slug,slug)
        if mid in observed:raise RuntimeError("Duplicate project "+mid)
        observed.add(mid)
        dl=data.get("download",{})
        if dl.get("hash-format") not in {"sha1","sha256","sha512"} or not dl.get("hash"):
            raise RuntimeError("Unhashed package "+mid)
        if mid not in byid:
            row={"id":mid,"name":data.get("name",mid),"category":"dependency",
                "desired":True,"version":None,"release_status":"unverified",
                "source":None,"enabled_in_pack":False,"dependencies_verified":False}
            original["mods"].append(row);byid[mid]=row
        row=byid[mid]
        row["packwiz_slug"]=slug
        row["version"]=data["filename"].removesuffix(".jar")
        row["enabled_in_pack"]=True
        if cf.get("project-id") in pid_map:
            expected=pid_map[cf["project-id"]][1]
            if expected.lower() not in data["filename"].lower():
                raise RuntimeError("Incorrect CurseForge artifact for "+mid+": "+data["filename"])
            row["release_status"]="experimental_release" if mid=="paucal" else "verified_release"
            row["source"]=f"https://www.curseforge.com/minecraft/mc-mods/{cf['project-id']}"
            row["note"]="Pinned actual required NeoForge 1.21.1 version. "+(
                "Required alpha-tagged library of HexCast; test-only candidate." if mid=="paucal"
                else "Required >=1.18.3 to satisfy Ars 'n Spells 3.3.5." if mid=="ars-elemancy"
                else "Required by HexCast and Enigmatic Legacy+.")
            extras.append({"id":mid,"curseforge_project":cf["project-id"],
                           "file_id":cf.get("file-id"),"filename":data["filename"]})
        items.append({"id":mid,"slug":slug,"file":data["filename"],
                      "side":data.get("side","both"),
                      "curseforge_project":cf.get("project-id"),
                      "curseforge_file":cf.get("file-id"),
                      "modrinth_project":data.get("update",{}).get("modrinth",{}).get("mod-id")})
    if not before.issubset(observed):
        raise RuntimeError("Lost previously installed mod(s): "+str(sorted(before-observed)))
    original["mods"].sort(key=lambda x:x["id"])
    b=next(x for x in original["batches"] if x["id"]=="B7")
    b["members"]=sorted(set(b["members"])|set(x["id"] for x in extras))
    b["status"]="libraries_and_version_repaired_boot_pending"
    MANIFEST.write_text(json.dumps(original,indent=2)+"\n")
    evidence=json.loads(EVIDENCE.read_text())
    evidence["total_pins"]=len(observed)
    evidence["added_mods"]=sorted(set(evidence["added_mods"])|{x["id"] for x in extras})
    evidence["required_dependency_fixes"]=evidence.get("required_dependency_fixes",[])+extras
    evidence["exact_pins"]=items
    EVIDENCE.write_text(json.dumps(evidence,indent=2)+"\n")
    rc,out=run("packwiz","refresh")
    if rc:raise RuntimeError("Final packwiz refresh failed: "+out[-1200:])
    rc,out=run(sys.executable,"scripts/validate.py")
    print(out,flush=True)
    if rc:raise RuntimeError("Pack validation failed")
    print("VERSION REPAIR COMPLETE",json.dumps({"total":len(observed),"repaired":extras}),flush=True)

if __name__=="__main__":main()
