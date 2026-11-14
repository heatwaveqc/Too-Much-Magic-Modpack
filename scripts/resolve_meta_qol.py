#!/usr/bin/env python3
"""Resolve the entire agreed meta/QoL batch with packwiz and record every failed candidate.

This intentionally does not touch any magic-system or progression mod.
"""
import json
import pathlib
import subprocess
import tomllib
from datetime import datetime, timezone

ROOT=pathlib.Path(__file__).resolve().parents[1]
MANIFEST=ROOT/"manifest/mods.json"
OUT=ROOT/"docs/META-QOL-RESOLUTION.json"
# Names resolve against Modrinth with packwiz's Minecraft/NeoForge filtering.
# We include both Curios API and Corail Tombstone because "coralis" could refer to either.
REQUESTED=[
    ("ftb-quests","ftb-quests","infrastructure"),
    ("ftb-chunks","ftb-chunks","infrastructure"),
    ("jei","jei","qol"),
    ("jade","jade","qol"),
    ("appleskin","appleskin","qol"),
    ("mouse-tweaks","mouse-tweaks","qol"),
    ("controlling","controlling","qol"),
    ("just-zoom","just-zoom","qol"),
    ("curios","curios-api","dependency"),
    ("modonomicon","modonomicon","dependency"),
    ("patchouli","patchouli","dependency"),
    ("geckolib","geckolib","dependency"),
    ("architectury-api","architectury-api","dependency"),
    ("corail-tombstone","corail-tombstone","qol"),
    ("polymorph","polymorph","qol"),
    ("lootr","lootr","qol"),
    ("ferrite-core","ferritecore","performance"),
    ("embeddium","embeddium","performance"),
    ("entityculling","entity-culling","performance"),
    ("immediatelyfast","immediatelyfast","performance"),
]
EXACT_MAP={
    "curios":"curios-api","ferrite-core":"ferritecore","entityculling":"entity-culling",
    "ftb-quests":"ftb-quests","ftb-chunks":"ftb-chunks",
    "ftb-library":"ftb-library","ftb-teams":"ftb-teams",
    "better-advanced-tooltips":"better-advanced-tooltips",
}
def mod_id(name): return EXACT_MAP.get(name,name)
def run(*cmd):
    p=subprocess.run(cmd,cwd=ROOT,text=True,stdout=subprocess.PIPE,stderr=subprocess.STDOUT,timeout=150)
    return p.returncode,p.stdout

def main():
    d=json.loads(MANIFEST.read_text(encoding="utf8"))
    by_id={m["id"]:m for m in d["mods"]}
    failures=[]
    for slug,mid,category in REQUESTED:
        rc,out=run("packwiz","--yes","modrinth","add",slug)
        print(f"RESOLVE {slug}: {'OK' if rc==0 else 'BLOCKED'}",flush=True)
        print(out[-2500:],flush=True)
        if rc:
            failures.append({"slug":slug,"intended_id":mid,"error":out[-1200:]})
            if mid in by_id:
                by_id[mid]["note"]="Modrinth resolution attempted; see docs/META-QOL-RESOLUTION.json"
            continue
    rc,out=run("packwiz","refresh")
    if rc: raise SystemExit("packwiz refresh failed: "+out[-3000:])
    check=set(d["excluded"][i]["id"] for i in range(len(d["excluded"])))
    installed=[]
    for meta in sorted((ROOT/"mods").glob("*.pw.toml")):
        slug=meta.name.removesuffix(".pw.toml")
        mid=mod_id(slug)
        if mid in check or slug in check: raise SystemExit("EXCLUDED MOD FOUND: "+slug)
        val=tomllib.loads(meta.read_text(encoding="utf8"))
        download=val["download"]
        if download.get("hash-format") not in {"sha512","sha256","sha1"} or not download.get("hash"):
            raise SystemExit("Unpinned artifact: "+slug)
        if download.get("url","").startswith("http://"): raise SystemExit("Insecure download: "+slug)
        category=next((c for s,m,c in REQUESTED if s==slug or m==mid),"dependency")
        m=by_id.get(mid)
        if m is None:
            m={"id":mid,"name":val["name"],"category":category,"desired":True,"version":None,
               "release_status":"verified_release","source":None,"enabled_in_pack":False,"dependencies_verified":False}
            d["mods"].append(m);by_id[mid]=m
        m["version"]=val["filename"].removesuffix(".jar")
        m["release_status"]="verified_release"
        m["enabled_in_pack"]=True
        m["dependencies_verified"]=False
        if val.get("update",{}).get("modrinth",{}).get("mod-id"):
            m["source"]="https://modrinth.com/mod/"+val["update"]["modrinth"]["mod-id"]
        installed.append({"id":mid,"name":val["name"],"file":val["filename"],"side":val.get("side","both"),
                          "metafile":str(meta.relative_to(ROOT)),"hash_format":download.get("hash-format"),
                          "modrinth_id":val.get("update",{}).get("modrinth",{}).get("mod-id")})
    d["mods"].sort(key=lambda m:m["id"])
    batch=next(b for b in d["batches"] if b["id"]=="B1")
    batch["members"]=sorted(set(batch["members"])|{m["id"] for m in d["mods"] if m["enabled_in_pack"]})
    batch["status"]="meta_qol_candidate_modded_server_unverified"
    d["note"]="Full infrastructure + QoL batch pinned; smoke test outcome recorded independently in docs/META-QOL-TESTING.md"
    MANIFEST.write_text(json.dumps(d,indent=2)+"\n",encoding="utf8")
    OUT.write_text(json.dumps({"generated_at_utc":datetime.now(timezone.utc).isoformat(),"requested_slugs":[x[0] for x in REQUESTED],
                               "resolved_mods":installed,"failed_modrinth_resolutions":failures},indent=2)+"\n",encoding="utf8")
    print(f"RESOLUTION SUMMARY: {len(installed)} exact packwiz mod pins; {len(failures)} unresolved requested slugs",flush=True)
    # Refresh after manifest writes; source docs and manifest are .packwizignored.
    rc,out=run("packwiz","refresh")
    if rc: raise SystemExit("packwiz second refresh failed: "+out[-3000:])
    rc,out=run("python3","scripts/validate.py")
    print(out,flush=True)
    if rc: raise SystemExit("Pack validation failed")

if __name__=="__main__":main()
