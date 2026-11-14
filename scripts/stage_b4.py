#!/usr/bin/env python3
"""Stage exactly approved CurseForge files into the 1.21.1 NeoForge pack.

No fuzzy mod search; stages are applied one at a time by ci_b4.py.
Downloaded jars stay on disposable CI runners, never in Git.
"""
from __future__ import annotations
import json
import pathlib
import re
import subprocess
import sys
import tomllib

ROOT=pathlib.Path(__file__).resolve().parents[1]
# (manifest ID, exact CurseForge project ID, exact file ID, expected jar, category,
#  release classification)
STAGES={
 "reactor":[("bigger-reactors-rework",1760630,9448110,"biggerreactorsrework-1.21.1-0.1.6.jar","technology","verified_release")],
 "astral":[("observerlib",316833,8726128,"observerlib-1.10.3.jar","dependency","experimental_release"),
           ("astral-sorcery",241721,9281073,"AstralSorcery-2.0.3.59.jar","magic","experimental_release")],
 "botania":[("botania-revived",1725641,9577651,"Botania-Revived-1.21.1-2.0.2-NeoForge.jar","magic","verified_release")],
 "draconic":[("brandons-core",231382,7130990,"BrandonsCore-1.21.1-3.2.1.309.jar","dependency","verified_release"),
             ("draconic-evolution",223565,8797042,"Draconic-Evolution-1.21.1-3.1.4.633.jar","technology","experimental_release")]
}
APPROVED={pid:(mid,fileid,filename,category,status) for entries in STAGES.values() for mid,pid,fileid,filename,category,status in entries}
BANNED_NAMES=("sophisticatedstorage","sophisticatedbackpacks","sophisticatedcore","refinedstorage","appliedenergistics",
              "ftbultimine","bloodmagic","create","meekanism","mekanism")
BANNED_PROJECTS={618298}  # Sophisticated Core (not needed by Botania Revived)

def run(*args):
    p=subprocess.run(args,text=True,cwd=ROOT,stdout=subprocess.PIPE,stderr=subprocess.STDOUT,timeout=180)
    return p.returncode,p.stdout

def main(stage):
    if stage not in STAGES:
        raise RuntimeError("Unknown B4 stage "+stage)
    manifest_path=ROOT/"manifest/mods.json"
    manifest=json.loads(manifest_path.read_text(encoding="utf8"))
    before={m.get("packwiz_slug",m["id"]) for m in manifest["mods"] if m.get("enabled_in_pack")}
    observed=[]
    for mid,pid,fileid,expected,category,status in STAGES[stage]:
        rc,out=run("packwiz","--yes","curseforge","add","--addon-id",str(pid),"--file-id",str(fileid))
        print(f"REQUEST {mid} CF {pid}/{fileid}: "+("OK" if rc==0 else "FAILED"),flush=True)
        if rc:
            print(out[-2200:],flush=True)
            raise RuntimeError("Exact CurseForge pin failed for "+mid)
    rc,out=run("packwiz","refresh")
    if rc:
        raise RuntimeError("packwiz refresh failed: "+out[-2200:])
    existing={m["id"]:m for m in manifest["mods"]}
    old_slug_to_id={m.get("packwiz_slug",m["id"]):m["id"] for m in manifest["mods"]}
    all_metas=[]
    seen=set()
    for meta in sorted((ROOT/"mods").glob("*.pw.toml")):
        slug=meta.name.removesuffix(".pw.toml")
        val=tomllib.loads(meta.read_text(encoding="utf8"))
        project=val.get("update",{}).get("curseforge",{})
        pid=project.get("project-id")
        fid=project.get("file-id")
        filename=val.get("filename","")
        if pid in BANNED_PROJECTS or any(b in "".join(c for c in (val.get("name","")+slug).lower() if c.isalnum()) for b in BANNED_NAMES):
            raise RuntimeError("Hard-excluded mod auto selected: "+str(val.get("name"))+"/"+str(pid))
        if not filename.endswith(".jar"):
            raise RuntimeError("Unexpected non-jar artifact for "+slug)
        info=val.get("download",{})
        if not info.get("hash") or info.get("hash-format") not in {"sha1","sha256","sha512"}:
            raise RuntimeError("Missing content hash for "+slug)
        if not info.get("url") and info.get("mode")!="metadata:curseforge":
            raise RuntimeError("Missing approved download source "+slug)
        if pid in APPROVED:
            mid,expected_file,expected_filename,category,status=APPROVED[pid]
            if fid!=expected_file or filename!=expected_filename:
                raise RuntimeError(f"Wrong CurseForge version for {mid}: project {pid}, file {fid}, jar {filename}")
        else:
            mid=old_slug_to_id.get(slug,slug)
            category="dependency"
            status="verified_release"
        if mid in seen:
            raise RuntimeError("Duplicate installed mod ID "+mid)
        seen.add(mid)
        m=existing.get(mid)
        if not m:
            m={"id":mid,"name":val.get("name",mid),"category":category,"desired":True,
               "version":None,"release_status":status,"source":None,
               "enabled_in_pack":False,"dependencies_verified":False}
            manifest["mods"].append(m)
            existing[mid]=m
        m["packwiz_slug"]=slug
        m["version"]=filename.removesuffix(".jar")
        m["enabled_in_pack"]=True
        if pid in APPROVED:
            m["release_status"]=status
            m["source"]=f"https://www.curseforge.com/minecraft/mc-mods/{pid}"
            m["note"]=("Approved BiggerReactors Rework 1.21.1 fork, namespace biggerreactorsrework; non-migrating worlds"
                       if mid=="bigger-reactors-rework" else
                       "Unofficial Botania Revived NeoForge port; not official Botania, no Sophisticated Storage dependencies allowed"
                       if mid=="botania-revived" else
                       "Explicit experimental testing candidate and exact pinned dependency"
                       if status=="experimental_release" else
                       "Required library pinned by exact CurseForge file ID")
        all_metas.append({"id":mid,"slug":slug,"filename":filename,"side":val.get("side","both"),
                          "cf_project_id":pid,"cf_file_id":fid,
                          "hash_format":info.get("hash-format")})
    new_slugs={m["slug"] for m in all_metas}
    if not before.issubset(new_slugs):
        raise RuntimeError("Packwiz removed baseline mod(s): "+str(sorted(before-new_slugs)))
    for mid,pid,fileid,filename,category,status in STAGES[stage]:
        if mid not in seen:
            raise RuntimeError("Requested mod not added: "+mid)
    manifest["mods"].sort(key=lambda m:m["id"])
    batch=next((x for x in manifest["batches"] if x["id"]=="B6"),None)
    if batch is None:
        batch={"id":"B6","name":"BiggerReactors Rework and experimental Astral/Botania/Draconic",
               "members":[],"status":"experiments_in_progress"}
        manifest["batches"].append(batch)
    batch["members"]=sorted(set(batch["members"]) | {x[0] for x in STAGES[stage]})
    batch["status"]="stage_"+stage+"_awaiting_boot"
    manifest["note"]="B4 experimental additions; trust only per-stage logged successful smoke tests. ProjectE and Expansion remain ungated."
    manifest_path.write_text(json.dumps(manifest,indent=2)+"\n",encoding="utf8")
    pack=ROOT/"pack.toml"
    src=pack.read_text()
    src=re.sub(r'(?m)^version = ".*"$','version = "0.4-'+stage+'-candidate"',src,count=1)
    pack.write_text(src)
    rc,out=run("packwiz","refresh")
    if rc:
        raise RuntimeError("packwiz final refresh failed: "+out[-2000:])
    rc,out=run(sys.executable,"scripts/validate.py")
    print(out,flush=True)
    if rc:
        raise RuntimeError("Manifest/index validation failed for "+stage)
    result={"stage":stage,"added":[x[0] for x in STAGES[stage]],"total_pins":len(all_metas),
            "newly_added_slugs":sorted(new_slugs-before),"mod_files":all_metas}
    print("STAGE RESULT "+json.dumps({k:v for k,v in result.items() if k!="mod_files"}),flush=True)
    return result

if __name__=="__main__":
    if len(sys.argv)!=2:
        sys.exit("Usage: stage_b4.py reactor|astral|botania|draconic")
    try:
        main(sys.argv[1])
    except Exception as e:
        print("STAGE FAILURE: "+repr(e),file=sys.stderr)
        raise
