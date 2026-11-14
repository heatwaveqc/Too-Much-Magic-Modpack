#!/usr/bin/env python3
"""Replace previously installed JEI beta with exact 1.21.1 NeoForge release.

Uses Modrinth's immutable project/version metadata and verified artifact hash.
"""
from __future__ import annotations
import json
from pathlib import Path
import sys
import tomllib
from resolve_stable_gameplay import ROOT, matching_project, release_version, run

def main():
    p=matching_project("jei","Just Enough Items")
    v=release_version(p["id"])
    if not v or v.get("version_type")!="release":
        raise RuntimeError("Missing stable 1.21.1 NeoForge JEI")
    options=[f for f in v.get("files",[]) if f.get("filename","").endswith(".jar")
             and "neoforge" in f.get("filename","").lower() and
             not f.get("filename","").endswith("-sources.jar")]
    if not options:
        raise RuntimeError("No matching NeoForge binary in exact JEI release metadata")
    f=next((f for f in options if f.get("primary")),options[0])
    hashes=f["hashes"]
    if "sha512" not in hashes:
        raise RuntimeError("JEI release missing SHA-512")
    if not f["url"].startswith("https://cdn.modrinth.com/"):
        raise RuntimeError("Unapproved JEI artifact host")
    meta=ROOT/"mods/jei.pw.toml"
    prev=tomllib.loads(meta.read_text())
    if prev.get("update",{}).get("modrinth",{}).get("mod-id")!=p["id"]:
        raise RuntimeError("Installed JEI upstream ID mismatch")
    # TOML string literals are valid JSON quoted strings.
    q=lambda s:json.dumps(s,ensure_ascii=False)
    content=(
        "name = "+q(p["title"])+"\n"+
        "filename = "+q(f["filename"])+"\n"+
        "side = "+q("both")+"\n\n"+
        "[download]\nurl = "+q(f["url"])+"\n"+
        'hash-format = "sha512"\n'+
        "hash = "+q(hashes["sha512"])+"\n\n"+
        "[update]\n[update.modrinth]\n"+
        "mod-id = "+q(p["id"])+"\n"+
        "version = "+q(v["id"])+"\n"
    )
    meta.write_text(content)
    mpath=ROOT/"manifest/mods.json"
    manifest=json.loads(mpath.read_text())
    mod=next(x for x in manifest["mods"] if x["id"]=="jei")
    mod["version"]=f["filename"].removesuffix(".jar")
    mod["release_status"]="verified_release"
    mod["note"]="Pinned stable upstream release instead of the B2 preexisting beta, to honor no-beta gameplay policy."
    mpath.write_text(json.dumps(manifest,indent=2)+"\n")
    evidence=ROOT/"docs/B3-RESOLUTION.json"
    data=json.loads(evidence.read_text())
    for item in data.get("metafiles",[]):
        if item["id"]=="jei":
            item["filename"]=f["filename"]
            item["version_id"]=v["id"]
    data["jei_stable_replacement"]={
        "old_filename":prev["filename"],"new_filename":f["filename"],
        "version_id":v["id"],"version_type":v["version_type"],
        "sha512":hashes["sha512"]
    }
    evidence.write_text(json.dumps(data,indent=2)+"\n")
    rc,output=run("packwiz","refresh")
    print(output,flush=True)
    if rc: raise RuntimeError("packwiz refresh failed")
    rc,output=run(sys.executable,"scripts/validate.py")
    print(output,flush=True)
    if rc: raise RuntimeError("Pack validation failed")
    print("JEI release pinned: "+v["version_number"]+" "+v["id"],flush=True)

if __name__=="__main__":main()
