#!/usr/bin/env python3
"""Resolve ATM10's Inventory Tweaks — ReFoxed, while retaining B6 rollback."""
import json
import pathlib
import subprocess
import tomllib
import re

ROOT=pathlib.Path(__file__).resolve().parents[1]
MANIFEST=ROOT/"manifest/mods.json"
CF_PROJECT=976858
CF_FILE=6407807
CANON="inventory-tweaks-refoxed"
EXACT="invtweaks-1.21.1-1.3.2.jar"

def run(*args):
    p=subprocess.run(args,cwd=ROOT,text=True,stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,timeout=180)
    if p.returncode:raise RuntimeError("Command "+repr(args)+" failed:\n"+p.stdout[-1600:])
    return p.stdout

def main():
    m=json.loads(MANIFEST.read_text())
    before={mod["id"] for mod in m["mods"] if mod["enabled_in_pack"]}
    if len(before)!=125:
        raise RuntimeError("B6 baseline changed unexpectedly: "+str(len(before)))
    assert CANON not in before
    assert any(mod["id"]==CANON and mod["desired"] for mod in m["mods"])
    assert any(mod["id"]=="mouse-tweaks" and mod["enabled_in_pack"] for mod in m["mods"])
    run("packwiz","--yes","curseforge","add","--addon-id",str(CF_PROJECT),"--file-id",str(CF_FILE))
    files=list((ROOT/"mods").glob("*.pw.toml"))
    matches=[]
    for path in files:
        data=tomllib.loads(path.read_text())
        if data.get("update",{}).get("curseforge",{}).get("project-id")==CF_PROJECT:
            matches.append((path,data))
    if len(matches)!=1:raise RuntimeError("ReFoxed project ID absent or ambiguous after resolve")
    path,data=matches[0]
    # CurseForge describes this as usable on both sides, but the sorter is
    # actually a client UI feature; never load it on the dedicated server.
    # packwiz does not expose a side override on curseforge add.
    if data.get("side")=="both":
        contents=path.read_text()
        if contents.count('side = "both"')!=1:
            raise RuntimeError("Cannot safely override side metadata")
        path.write_text(contents.replace('side = "both"','side = "client"',1))
        data=tomllib.loads(path.read_text())
    if data.get("filename")!=EXACT:
        raise RuntimeError("Incompatible ReFoxed JAR selected: "+str(data.get("filename")))
    if data.get("side")!="client":
        raise RuntimeError("ReFoxed must be client-only; reviewed manifest side: "+str(data.get("side")))
    dl=data.get("download",{})
    if dl.get("hash-format") not in ("sha1","sha256","sha512") or not dl.get("hash"):
        raise RuntimeError("ReFoxed lacks pinned integrity hash")
    check_file_id=data["update"]["curseforge"].get("file-id")
    if check_file_id!=CF_FILE:raise RuntimeError("Unexpected CurseForge file ID "+str(check_file_id))
    assert "mouse-tweaks.pw.toml" in [p.name for p in files]
    if len(files)!=126:
        raise RuntimeError("Expected 126 exact mod metadata pins, got "+str(len(files)))
    mod=next(q for q in m["mods"] if q["id"]==CANON)
    mod["enabled_in_pack"]=True
    mod["version"]=EXACT[:-4]
    mod["packwiz_slug"]=path.name.removesuffix(".pw.toml")
    mod["dependencies_verified"]=True
    mod["note"]=(
     "Pinned in B9: stable NeoForge 1.21.1 "+EXACT+
     "; CurseForge project "+str(CF_PROJECT)+", file "+str(check_file_id)+
     ". Exact sha256/sha1 pinned by packwiz. Client-only; ATM10 config enables all sorting and auto-refill. "
     "User must verify middle-click chest sorting in Prism; Mouse Tweaks retained.")
    next(x for x in m["batches"] if x["id"]=="B9")["status"]="installed_client_test_pending"
    m["note"]="B7: 126 metadata pins including Inventory Tweaks ReFoxed; B6 125-mod local client-good baseline retained in prior branch; no other QoL candidates yet approved."
    MANIFEST.write_text(json.dumps(m,indent=2)+"\n")
    p=ROOT/"pack.toml"
    s=p.read_text()
    s=re.sub(r'(?m)^version = ".*"$',
        'version = "0.7.0-atm10-inventory-refoxed-client-candidate"',s,count=1)
    p.write_text(s)
    run("packwiz","refresh")
    print(run("python3","scripts/validate.py"),flush=True)
    assert len([x for x in m["mods"] if x.get("enabled_in_pack")])==126
    info={
      "baseline_pinned":125,"new_active":"inventory-tweaks-refoxed",
      "total_pinned":126,
      "new_side":"client",
      "expected_filename":EXACT,
      "curseforge_project":CF_PROJECT,
      "curseforge_file":check_file_id,
      "packwiz_slug":mod["packwiz_slug"],
      "metadata_hash_format":dl["hash-format"],
      "metadata_hash":dl["hash"],
      "mouse_tweaks_preserved":True,
      "client_sorting_unverified":True,
      "no_other_qol_mods_added":True
    }
    (ROOT/"docs/B7-RESOLUTION.json").write_text(json.dumps(info,indent=2)+"\n")
    print("B7 REFOXED RESOLVED "+json.dumps(info),flush=True)

if __name__=="__main__":main()
