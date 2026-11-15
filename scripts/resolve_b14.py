#!/usr/bin/env python3
"""Hash-pin only approved Cobblegen Galore (0.2.9) against current test pack.

Explicit provider file ID, no automatic dependency upgrade or speculative
cross-mod material/ore recipes. Download happens through upstream publisher.
"""
from __future__ import annotations
import json
import subprocess
import tomllib
import re
from pathlib import Path

ROOT=Path(__file__).resolve().parents[1]
MODS=ROOT/"mods"
ROSTER=ROOT/"manifest/mods.json"
MOD_ID="cobblegen-galore"
CF_PROJECT=1108467
CF_FILE=8523192
VERSION="cobblegengalore-1.21.1-0.2.9.jar"

def command(*args: str) -> None:
    p=subprocess.run(args, cwd=ROOT, text=True, stdout=subprocess.PIPE,
                     stderr=subprocess.STDOUT, timeout=360)
    print(args,"return",p.returncode, "\n",p.stdout[-3000:],flush=True)
    if p.returncode: raise RuntimeError(f"Command failed: {args}")

def main() -> None:
    m=json.loads(ROSTER.read_text(encoding="utf-8"))
    active=[x for x in m["mods"] if x["enabled_in_pack"]]
    original={path.name:path.read_bytes() for path in MODS.glob("*.pw.toml")}
    assert len(active)==len(original)==156,(len(active),len(original))
    candidate=next(x for x in m["mods"] if x["id"]==MOD_ID)
    assert candidate["desired"] and not candidate["enabled_in_pack"]
    assert candidate["version"]==VERSION
    assert m["batches"][-1]["id"]=="B14"
    command("packwiz","--yes","curseforge","add","--addon-id",str(CF_PROJECT),"--file-id",str(CF_FILE))
    all_pins={p.name:p for p in MODS.glob("*.pw.toml")}
    added=set(all_pins)-set(original)
    assert len(added)==1,f"One addon expected, got {added}"
    assert all(all_pins[key].read_bytes()==old for key,old in original.items()), (
        "Packwiz silently modified existing mods; reject upgrade"
    )
    name=next(iter(added))
    pin=tomllib.loads(all_pins[name].read_text(encoding="utf-8"))
    cf=pin.get("update",{}).get("curseforge",{})
    assert (cf.get("project-id"),cf.get("file-id"))==(CF_PROJECT,CF_FILE),(cf, pin)
    assert pin["filename"]==VERSION,pin["filename"]
    assert pin.get("side","both")=="both","Galore must run on client and server"
    assert pin.get("download",{}).get("hash"),"No upstream hash"
    assert pin.get("download",{}).get("hash-format") in ("sha1","sha256","sha512")
    candidate["enabled_in_pack"]=True
    candidate["dependencies_verified"]=True
    candidate["packwiz_slug"]=name.removesuffix(".pw.toml")
    candidate["note"]=candidate["note"].replace(
        "Pending hash-pinned packwiz + dedicated-server smoke.",
        "Exact CF file pinned with published hash; dedicated-server/client verification pending.")
    m["batches"][-1]["status"]="hash_pinned_full_smoke_pending"
    m["note"]+="\nB14 pinned exactly one Galore 0.2.9 release, no other mod files changed."
    ROSTER.write_text(json.dumps(m,indent=2)+"\n",encoding="utf-8")
    p=ROOT/"pack.toml"
    s=p.read_text(encoding="utf-8")
    s,n=re.subn(r'(?m)^version = ".*"$','version = "0.9.2-b14-cobblegen-stone-inventory-qa"',s,count=1)
    assert n==1
    p.write_text(s,encoding="utf-8")
    command("packwiz","refresh")
    command("python3","scripts/validate.py")
    print("B14 PASS pin",name,pin["filename"],pin["download"]["hash-format"],"active",len(active)+1,flush=True)

if __name__=="__main__":
    main()
