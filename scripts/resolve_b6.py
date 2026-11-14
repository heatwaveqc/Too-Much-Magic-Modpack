#!/usr/bin/env python3
"""B6 pinned additions: Pam's 4, Farmer's Delight, Aquaculture, Harvest with Ease,
Cobweb, Pufferfish Skills & Attributes, Improvable Skills 3 and HammerLib.

Use exact CurseForge project IDs + packwiz 1.21.1 NeoForge compatibility
selection. Replace rejected Aspect Alchemy without leaving its jar in the pack.
The user explicitly approved testing beta Puffish Skills with this batch.
"""
from __future__ import annotations
import json
from pathlib import Path
import re
import subprocess
import sys
import tomllib
from datetime import datetime, timezone

ROOT=Path(__file__).resolve().parents[1]
MF=ROOT/"manifest/mods.json"
EVIDENCE=ROOT/"docs/B6-RESOLUTION.json"
# Official CurseForge project IDs, not fuzzy search results.
CANDIDATES=[
    ("pams-harvestcraft-2-food-core",372534,"pamhc2foodcore", "food"),
    ("pams-harvestcraft-2-crops",361385,"pamhc2crops","food"),
    ("pams-harvestcraft-2-trees",365460,"pamhc2trees","food"),
    ("pams-harvestcraft-2-food-extended",402231,"pamhc2foodextended","food"),
    ("farmers-delight",398521,"farmersdelight","food"),
    ("aquaculture",60028,"aquaculture","food"),
    ("cobweb",968456,"cobweb","dependency"),
    ("harvest-with-ease",602171,"harvest","qol"),
    ("puffish-attributes",991341,"puffish_attributes","dependency"),
    ("puffish-skills",835091,"puffish_skills","infrastructure"),
    ("hammerlib",247401,"hammerlib","dependency"),
    ("improvable-skills",252902,"improvable","skills"),
]
CF={pid:(mid,part,cat) for mid,pid,part,cat in CANDIDATES}
BLOCKED_TOKENS=("sophisticatedstorage","sophisticatedbackpacks","sophisticatedcore","appliedenergistics",
"refinedstorage","ftbultimine","bloodmagic","elementalcraft","naturesaura",
"forcecraft","mysticalagriculture","spellengine","aspectalchemy","create")
def norm(s:str):return re.sub(r"[^a-z0-9]","",s.lower())
def run(*args):
    p=subprocess.run(args,cwd=ROOT,text=True,stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,timeout=200)
    return p.returncode,p.stdout
def main():
    manifest=json.loads(MF.read_text())
    byid={m["id"]:m for m in manifest["mods"]}
    before={m["id"] for m in manifest["mods"] if m.get("enabled_in_pack")}
    if "aspect-alchemy" in byid or "aspect-alchemy" in before:
        raise RuntimeError("Rejected Aspect Alchemy remains in approved roster")
    if not any(m["id"]=="aspect-alchemy" for m in manifest["excluded"]):
        raise RuntimeError("Rejected Aspect Alchemy not explicitly excluded")
    badfile=ROOT/"mods/aspect-alchemy.pw.toml"
    if not badfile.exists():
        raise RuntimeError("Expected old Aspect Alchemy file for removal; verify baseline")
    badfile.unlink()
    rc,out=run("packwiz","refresh")
    if rc:raise RuntimeError("Unable to refresh after removing rejected mod: "+out[-1800:])
    report=[]
    for mid,pid,part,cat in CANDIDATES:
        if mid not in byid:raise RuntimeError("Missing approved mod "+mid)
        if byid[mid].get("enabled_in_pack"):raise RuntimeError("Unintended duplicate mod "+mid)
        rc,out=run("packwiz","--yes","curseforge","add","--addon-id",str(pid))
        r={"id":mid,"curseforge_project":pid,
           "status":"resolved" if rc==0 else "blocked"}
        if rc:r["reason"]=out[-1400:]
        report.append(r)
        print("B6 SELECT "+json.dumps(r),flush=True)
        if rc:raise RuntimeError("Requested approved mod could not be pinned: "+mid+": "+out[-1400:])
    rc,out=run("packwiz","refresh")
    if rc:raise RuntimeError("Packwiz refresh failed: "+out[-1400:])
    slugs={m.get("packwiz_slug",m["id"]):m["id"] for m in manifest["mods"]}
    pinned=[]
    seen=set()
    new=[]
    for path in sorted((ROOT/"mods").glob("*.pw.toml")):
        slug=path.name.removesuffix(".pw.toml")
        data=tomllib.loads(path.read_text())
        cf=data.get("update",{}).get("curseforge",{})
        mr=data.get("update",{}).get("modrinth",{})
        pid=cf.get("project-id")
        mid=CF[pid][0] if pid in CF else slugs.get(slug,slug)
        if mid in seen:raise RuntimeError("Duplicate installed canonical mod "+mid)
        seen.add(mid)
        raw=norm(data.get("name","")+slug+mid)
        if any(token in raw for token in BLOCKED_TOKENS):
            raise RuntimeError("Banned mod/framework was selected: "+data.get("name",slug))
        dl=data.get("download",{})
        if dl.get("hash-format") not in ("sha1","sha256","sha512") or not dl.get("hash"):
            raise RuntimeError("Unhashed JAR: "+slug)
        if not dl.get("url") and dl.get("mode")!="metadata:curseforge":
            raise RuntimeError("No provider download information: "+slug)
        if not data.get("filename","").endswith(".jar"):
            raise RuntimeError("Invalid non-JAR package: "+slug)
        if pid in CF:
            canon,expected,category=CF[pid]
            filename=data["filename"].lower()
            if norm(expected) not in norm(filename):
                raise RuntimeError("Wrong CurseForge project artifact for "+canon+": "+data["filename"])
            if "26." in filename or "1.21.11" in filename or "1.21.10" in filename or "1.20" in filename:
                raise RuntimeError("Wrong MC target in file: "+data["filename"])
            if canon=="puffish-skills" and "0.19.2" not in filename:
                raise RuntimeError("Unexpected Pufferfish Skills version, manual review required: "+data["filename"])
            if canon=="improvable-skills" and "21.0.2" not in filename:
                raise RuntimeError("Unexpected Improvable Skills version, manual review required: "+data["filename"])
        m=byid.get(mid)
        if m is None:
            m={"id":mid,"name":data.get("name",mid),"category":"dependency",
               "desired":True,"version":None,"release_status":"unverified",
               "source":None,"enabled_in_pack":False,"dependencies_verified":False}
            manifest["mods"].append(m);byid[mid]=m
        m["packwiz_slug"]=slug
        m["version"]=data["filename"].removesuffix(".jar")
        m["enabled_in_pack"]=True
        if pid in CF:
            m["release_status"]="experimental_release" if mid=="puffish-skills" else "verified_release"
            m["source"]="https://www.curseforge.com/minecraft/mc-mods/"+str(pid)
            m["note"]=("B6 user-approved boot-test candidate. "
                       + ("Pufferfish Skills release is beta-tagged, not custom skill tree content." if mid=="puffish-skills" else
                        "Improvable Skills XP-based upgrade system, distinct from Pufferfish; HammerLib required." if mid=="improvable-skills" else
                        "Food/farming or supporting framework; full gameplay interactions pending."))
            new.append(mid)
        pinned.append({"id":mid,"slug":slug,"file":data["filename"],
             "side":data.get("side","both"),"curseforge_project":pid,
             "curseforge_file":cf.get("file-id"),
             "modrinth_project":mr.get("mod-id"),
             "modrinth_version":mr.get("version"),
             "hash_format":dl.get("hash-format")})
    if not before.issubset(seen):
        raise RuntimeError("Previous B5 working mods unexpectedly removed: "+str(sorted(before-seen)))
    expected={m for m,_,_,_ in CANDIDATES}
    if not expected.issubset(seen):
        raise RuntimeError("Missing requested B6 mod(s): "+str(sorted(expected-seen)))
    manifest["mods"].sort(key=lambda m:m["id"])
    batch=next(b for b in manifest["batches"] if b["id"]=="B8")
    batch["members"]=sorted(set(batch["members"])|set(new))
    batch["status"]="resolved_server_boot_pending"
    manifest["note"]="B6: rejected Aspect Alchemy removed, Pufferfish Skills beta + Attributes, Improvable Skills 3, Pam's/Delight/Aquaculture/Harvest suite pinned. Test only; client/food compatibility/gating pending."
    MF.write_text(json.dumps(manifest,indent=2)+"\n")
    EVIDENCE.write_text(json.dumps({
        "generated_utc":datetime.now(timezone.utc).isoformat(),
        "target":{"minecraft":"1.21.1","loader":"neoforge","loader_version":"21.1.265","java":21},
        "baseline_installed":len(before),"removed":["aspect-alchemy"],
        "candidates":report,"newly_enabled":sorted(seen-before),
        "installed_mods":len(seen),
        "server_side_pins":sum(m["side"]!="client" for m in pinned),
        "client_only_pins":sum(m["side"]=="client" for m in pinned),
        "exact_pins":pinned,
        "notes":[
          "Pufferfish Skills 0.19.2 upstream beta; user approved testing it but no default tree or custom tree authored.",
          "Improvable Skills 21.0.2 is separate from Pufferfish; requires HammerLib.",
          "User-disapproved Aspect Alchemy deliberately removed and manifest excluded.",
          "Mods' CurseForge metadata was checked for expected project IDs and 1.21.1 variants; dynamic gameplay compatibility is not proven.",
          "Never redistribute copyrighted mod jars or export a mixed-source pack which embeds third-party jar files.",
          "No ProjectE progression locks have been implemented."]},indent=2)+"\n")
    pack=ROOT/"pack.toml"
    original=pack.read_text()
    pack.write_text(re.sub(r'(?m)^version = ".*"$',
        'version = "0.6.0-food-pufferfish-improvable-boot-candidate"',original,count=1))
    rc,out=run("packwiz","refresh")
    if rc:raise RuntimeError("Final packwiz refresh failed: "+out[-1400:])
    rc,out=run(sys.executable,"scripts/validate.py")
    print(out,flush=True)
    if rc:raise RuntimeError("B6 manifest hash validation failed")
    print("B6 RESOLVED "+json.dumps({"total":len(seen),"server":sum(m["side"]!="client" for m in pinned),
        "new":sorted(seen-before),"removed":["aspect-alchemy"]}),flush=True)

if __name__=="__main__":
    main()
