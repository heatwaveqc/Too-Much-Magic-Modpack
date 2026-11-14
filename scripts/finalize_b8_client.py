#!/usr/bin/env python3
"""B8 client packaging correction: retain Simple Backups in integrated single-player servers.

No mods, exact project file/version/hash, or server-side JAR composition change.
"""
from pathlib import Path
import json
import subprocess
import tomllib

ROOT=Path(__file__).resolve().parents[1]
def run(*cmd):
 r=subprocess.run(cmd,cwd=ROOT,text=True,stdout=subprocess.PIPE,stderr=subprocess.STDOUT,timeout=200)
 if r.returncode:raise RuntimeError(" ".join(cmd)+" failed:\n"+r.stdout[-2000:])
 return r.stdout
def main():
 mf=ROOT/"manifest/mods.json"
 m=json.loads(mf.read_text())
 active=[x for x in m["mods"] if x["enabled_in_pack"]]
 if len(active)!=140:raise RuntimeError("Unexpected B8 pin count "+str(len(active)))
 if any(x["id"] in ("waystones","explorers-compass") and x["enabled_in_pack"] for x in m["mods"]):
  raise RuntimeError("Unapproved travel mod present")
 pkg=ROOT/"mods/simple-backups.pw.toml"
 tom=tomllib.loads(pkg.read_text())
 if tom["filename"]!="SimpleBackups-1.21-4.0.30.jar" or (
    tom["update"]["curseforge"]["project-id"]!=583228 or
    tom["update"]["curseforge"]["file-id"]!=7965880):
  raise RuntimeError("Unexpected backup artifact; refusing to change sides")
 if tom["side"] not in ("server","both"):
  raise RuntimeError("Unexpected backup package side")
 if tom["side"]=="server":
  txt=pkg.read_text()
  if txt.count('side = "server"')!=1:
   raise RuntimeError("Expected one server-side metadata entry")
  pkg.write_text(txt.replace('side = "server"','side = "both"',1))
 backup=next(x for x in m["mods"] if x["id"]=="simple-backups")
 backup["release_status"]="experimental_release"
 backup["note"]=(
  "Simple Backups 1.21-4.0.30 (CurseForge 583228/7965880) is BETA-TAGGED "
  "upstream, included in experimental client test; provider side changed "
  "server -> both so Prism singleplayer integrated server receives it. "
  "Not a proven disaster-recovery solution; test backup generation, retention, "
  "size cap, restore and offline backup before trusting valuable worlds.")
 m["note"]="B8 client test metadata: 140 exact pinned mods (127 dedicated-server/both-side, 13 client-only). Server smoke PASS; Simple Backups is installed on both client and server for singleplayer, and is explicitly beta. Market magic seed trades and GUI remain untested; Waystones excluded."
 mf.write_text(json.dumps(m,indent=2)+"\n")
 rpath=ROOT/"docs/B8-RESOLUTION.json"
 report=json.loads(rpath.read_text())
 for row in report["pins"]:
  if row["curseforge_project"]==583228:
   assert row["filename"]==tom["filename"]
   row["side"]="both"
 report["simple_backups_side"]="both"
 report["simple_backups_beta"]=True
 report["singleplayer_backup_eligible"]=True
 report["backup_restore_unverified"]=True
 rpath.write_text(json.dumps(report,indent=2)+"\n")
 path=ROOT/"docs/B8-TESTING.md"
 path.write_text(path.read_text()+"\n## Singleplayer backup delivery correction\n"
  "- Simple Backups 1.21-4.0.30 is upstream **BETA**; packwiz side updated from server-only "
  "to **both** so Prism installs it for the singleplayer integrated server.\n"
  "- No JAR version, hash or dedicated-server composition changed from the full passing B8 boot.\n"
  "- Backup scheduling, retention and disaster recovery are NOT acceptance-tested; "
  "retain independent world saves until restore is verified.\n"
  "- This metadata-only change requires refreshed packwiz index hash and a new immutable client ZIP.\n")
 run("packwiz","refresh")
 print(run("python3","scripts/validate.py"),flush=True)
 print("B8 CLIENT READY "+json.dumps({
  "pins":len(active),"simple_backups_side":"both",
  "beta":True,"repacked_index":True,"previous_server_boot":"PASS"}),flush=True)

if __name__=="__main__":main()
