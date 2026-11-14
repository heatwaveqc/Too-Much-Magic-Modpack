#!/usr/bin/env python3
"""Repair first B5 runtime dependency and exact-identity addon discovery.

This is a follow-up to docs/B5-RESOLUTION.json: preserve all earlier
approved, hash-pinned mods; add Lodestone for Malum and the identified Ars
Elemental and Gaze projects. No guesses, alpha substitutions or hard bans.
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
ADD=[
    {"id":"lodestone","provider":"modrinth","project":"bN3xUWdo",
     "version":"CohX6yP1","expected":"lodestone","dependency_for":"malum"},
    {"id":"ars-elemental","provider":"curseforge","project":561470,
     "expected":"ars_elemental","dependency_for":"ars-nouveau"},
    {"id":"gaze-malum","provider":"modrinth","project":"NlvaJ5WE",
     "version":"od4ltbRo","expected":"gaze","dependency_for":"malum"}
]

def run(*args):
    proc=subprocess.run(args,cwd=ROOT,text=True,stdout=subprocess.PIPE,
                        stderr=subprocess.STDOUT,timeout=180)
    return proc.returncode,proc.stdout

def main():
    m=json.loads(MANIFEST.read_text())
    index={x["id"]:x for x in m["mods"]}
    previous={x["id"] for x in m["mods"] if x.get("enabled_in_pack")}
    project_to_id={x["project"]:x["id"] for x in ADD}
    for entry in ADD:
        if entry["provider"]=="modrinth":
            cmd=["packwiz","--yes","modrinth","add","--project-id",entry["project"],
                "--version-id",entry["version"]]
        else:
            cmd=["packwiz","--yes","curseforge","add","--addon-id",str(entry["project"])]
        rc,out=run(*cmd)
        print("PIN "+entry["id"]+": "+("OK" if rc==0 else "FAILED"),flush=True)
        if rc:raise RuntimeError("packwiz refused "+entry["id"]+": "+out[-1800:])
    rc,out=run("packwiz","refresh")
    if rc:raise RuntimeError("packwiz refresh failed: "+out[-1800:])
    observed=set()
    new=[]
    for path in sorted((ROOT/"mods").glob("*.pw.toml")):
        slug=path.name.removesuffix(".pw.toml")
        data=tomllib.loads(path.read_text())
        cf=data.get("update",{}).get("curseforge",{})
        mr=data.get("update",{}).get("modrinth",{})
        project=cf.get("project-id") if cf.get("project-id") in project_to_id else mr.get("mod-id")
        mid=project_to_id.get(project)
        if not mid:
            mid=next((x["id"] for x in m["mods"] if x.get("packwiz_slug",x["id"])==slug),slug)
        if mid in observed:raise RuntimeError("Duplicate mod "+mid)
        observed.add(mid)
        if mid not in index:
            x={"id":mid,"name":data.get("name",mid),"category":"dependency","desired":True,
               "version":None,"release_status":"unverified","source":None,
               "enabled_in_pack":False,"dependencies_verified":False}
            m["mods"].append(x);index[mid]=x
        row=index[mid]
        row["packwiz_slug"]=slug
        row["enabled_in_pack"]=True
        row["version"]=data["filename"].removesuffix(".jar")
        matched=next((x for x in ADD if x["id"]==mid),None)
        if matched:
            if matched["expected"].lower() not in data["filename"].lower():
                raise RuntimeError("Unexpected project artifact for "+mid+": "+data["filename"])
            if matched["provider"]=="curseforge" and cf.get("project-id")!=matched["project"]:
                raise RuntimeError("CF project mismatch "+mid)
            if matched["provider"]=="modrinth" and (mr.get("mod-id")!=matched["project"]
                or mr.get("version")!=matched["version"]):
                raise RuntimeError("Immutable Modrinth version mismatch "+mid)
            row["release_status"]="verified_release"
            row["source"]=(f"https://modrinth.com/mod/{matched['project']}" if matched["provider"]=="modrinth" else
                           "https://www.curseforge.com/minecraft/mc-mods/ars-elemental")
            row["note"]="B5 verified release. "+("Missing prerequisite for Malum." if mid=="lodestone" else
                                      "Approved addon resolved from upstream exact project ID.")
            new.append(mid)
        if not data.get("download",{}).get("hash"):
            raise RuntimeError("Unhashed artifact "+mid)
    if not previous.issubset(observed):
        raise RuntimeError("Lost pinned baseline: "+str(sorted(previous-observed)))
    for a in ADD:
        if a["id"] not in observed:raise RuntimeError("Requested repair missing: "+a["id"])
    m["mods"].sort(key=lambda x:x["id"])
    b=next(x for x in m["batches"] if x["id"]=="B7")
    b["members"]=sorted(set(b["members"])|set(new))
    b["status"]="additional_prerequisites_resolved_boot_pending"
    MANIFEST.write_text(json.dumps(m,indent=2)+"\n")
    ev=json.loads(EVIDENCE.read_text())
    ev["total_pins"]=len(observed)
    ev["added_mods"]=sorted(set(ev["added_mods"])|set(new))
    ev["unresolved"]=[x for x in ev["unresolved"] if x["id"] not in new]
    for x in ev["approved_candidates"]:
        if x["id"] in new:
            x["status"]="resolved"
            x.pop("reason",None)
            z=next(y for y in ADD if y["id"]==x["id"])
            x["provider"]=z["provider"]
            x["project_id"]=z["project"]
            if z.get("version"):x["version_id"]=z["version"]
    ev["dependency_repairs"]=ev.get("dependency_repairs",[])+ADD
    ev["exact_pins"]=[]
    for path in sorted((ROOT/"mods").glob("*.pw.toml")):
        data=tomllib.loads(path.read_text())
        ev["exact_pins"].append({"slug":path.name.removesuffix(".pw.toml"),
          "file":data["filename"],"side":data.get("side","both"),
          "curseforge_project":data.get("update",{}).get("curseforge",{}).get("project-id"),
          "modrinth_project":data.get("update",{}).get("modrinth",{}).get("mod-id")})
    EVIDENCE.write_text(json.dumps(ev,indent=2)+"\n")
    rc,out=run("packwiz","refresh")
    if rc:raise RuntimeError("Second refresh failed: "+out[-1800:])
    rc,out=run(sys.executable,"scripts/validate.py")
    print(out,flush=True)
    if rc:raise RuntimeError("Manifest integrity failed")
    print("REPAIR COMPLETE",json.dumps({"new":new,"total":len(observed)}),flush=True)

if __name__=="__main__":main()
