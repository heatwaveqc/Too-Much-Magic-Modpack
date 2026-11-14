#!/usr/bin/env python3
"""B8 strict CurseForge resolution for the selected ATM10 QoL features.

Pins an exact NeoForge Minecraft 1.21.1 roster, resolves libraries first,
preserves the B7 baseline without mutating it, and rejects excluded systems.
A successful metadata check is not a game or client test.
"""
from __future__ import annotations
import json
from pathlib import Path
import subprocess
import tomllib
import re
ROOT=Path(__file__).resolve().parents[1]
MANIFEST=ROOT/"manifest/mods.json"
PROJECTS=[
 ("balm",531761,"balm-neoforge-1.21.1-21.0.67.jar","both"),
 ("bookshelf",228525,"bookshelf-neoforge-1.21.1-21.1.81.jar","both"),
 ("akashic-tome",250577,"AkashicTome-1.8-30.jar","both"),
 ("crafting-tweaks",233071,"craftingtweaks-neoforge-1.21.1-21.1.11.jar","both"),
 ("simple-backups",583228,"SimpleBackups-1.21-4.0.30.jar","server"),
 ("keybind-bundles",1172594,"keybindbundles-1.5.0.jar","client"),
 ("enchantment-descriptions",250419,"enchdesc-neoforge-1.21.1-21.1.11.jar","client"),
 ("better-advancements",272515,"BetterAdvancements-NeoForge-1.21.1-0.4.3.21.jar","client"),
 ("oracle-index",1206582,"oracle_index-neoforge-1.4.0.jar","client"),
 ("more-overlays-updated",391382,"moreoverlays-1.24.2-mc1.21.1-neoforge.jar","client"),
 ("spark",361579,"spark-1.10.124-neoforge.jar","both"),
 ("natures-compass",252848,"NaturesCompass-1.21.1-3.4.0-neoforge.jar","both"),
 ("farming-for-blockheads",261924,"farmingforblockheads-neoforge-1.21.1-21.1.14.jar","both"),
]
PREFIX={pid:(id,name,side) for id,pid,name,side in PROJECTS}
BANNED=("waystones","explorerscompass","ftbultimine","sophisticatedstorage",
    "sophisticatedbackpacks","sophisticatedcore","appliedenergistics2",
    "refinedstorage","mysticalagriculture","createmod","aspectalchemy")
def norm(s):return re.sub(r"[^a-z0-9]","",s.lower())
def run(*args):
 p=subprocess.run(args,cwd=ROOT,text=True,stdout=subprocess.PIPE,
  stderr=subprocess.STDOUT,timeout=240)
 if p.returncode:
  raise RuntimeError("B8 command failed "+repr(args)+"\n"+p.stdout[-2400:])
 return p.stdout
def main():
 m=json.loads(MANIFEST.read_text())
 byid={v["id"]:v for v in m["mods"]}
 base={v["id"] for v in m["mods"] if v["enabled_in_pack"]}
 if len(base)!=126 or "inventory-tweaks-refoxed" not in base:
  raise RuntimeError("Previous B7 client-good fallback is not intact")
 for id,_,_,_ in PROJECTS:
  if id not in byid or byid[id]["enabled_in_pack"]:
   raise RuntimeError("Unexpected approved B8 roster "+id)
 for id,project,expected,side in PROJECTS:
  # Hard-pin the already-reviewed official MC1.21.1 version for Crafting Tweaks;
  # other files are validated against the ATM10 baseline below.
  args=["packwiz","--yes","curseforge","add","--addon-id",str(project)]
  if id=="crafting-tweaks":args+=["--file-id","8697050"]
  run(*args)
  files=[]
  for path in sorted((ROOT/"mods").glob("*.pw.toml")):
   obj=tomllib.loads(path.read_text())
   if obj.get("update",{}).get("curseforge",{}).get("project-id")==project:
    files.append((path,obj))
  if len(files)!=1:
   raise RuntimeError("B8 project ID mismatch / duplicate: "+str(project)+", files="+str(files))
  path,obj=files[0]
  if obj.get("filename")!=expected:
   raise RuntimeError("B8 selected unreviewed artifact for "+id+
     ": got "+repr(obj.get("filename"))+" expected "+repr(expected)+
     ". Resolve exact 1.21.1 CurseForge file ID; never guess or silently upgrade.")
  if obj.get("side")!=side:
   # packwiz defaults to provider side metadata. Force specific safe UI mods to client,
   # set Simple Backups to server-only, but do not ever force a server-only mod client.
   if side not in ("client","server") or obj.get("side") not in ("both",side):
    raise RuntimeError("Unsafe metadata side for "+id+": "+str(obj.get("side")))
   content=path.read_text()
   if content.count('side = "both"')!=1:raise RuntimeError("Cannot change side for "+id)
   path.write_text(content.replace('side = "both"',f'side = "{side}"',1))
   obj=tomllib.loads(path.read_text())
  d=obj.get("download",{})
  if d.get("hash-format") not in ("sha1","sha256","sha512") or not d.get("hash"):
   raise RuntimeError("Missing hash for "+id)
  item=byid[id]
  item.update({"enabled_in_pack":True,"packwiz_slug":path.name.removesuffix(".pw.toml"),
       "version":expected[:-4],"dependencies_verified":True})
  print("B8 SELECT "+json.dumps({"id":id,"filename":expected,"side":obj["side"],
       "file_id":obj["update"]["curseforge"].get("file-id")}),flush=True)
 run("packwiz","refresh")
 allmods=[]
 active={v["id"] for v in m["mods"] if v["enabled_in_pack"]}
 expected=len(base)+len(PROJECTS)
 for path in sorted((ROOT/"mods").glob("*.pw.toml")):
  obj=tomllib.loads(path.read_text())
  n=norm(path.name+" "+obj.get("name",""))
  if any(w in n for w in BANNED):
   raise RuntimeError("B8 has a banned mod: "+path.name)
  if obj.get("filename","").lower().endswith(".jar") is False:
   raise RuntimeError("Non-JAR mod file: "+path.name)
  allmods.append({"path":path.name,"filename":obj["filename"],"side":obj.get("side","both"),
       "curseforge_project":obj.get("update",{}).get("curseforge",{}).get("project-id"),
       "curseforge_file":obj.get("update",{}).get("curseforge",{}).get("file-id")})
 if len(allmods)!=expected or len(active)!=expected:
  raise RuntimeError("B8 pin count mismatch: "+str((len(allmods),len(active),expected)))
 if not base.issubset(active):raise RuntimeError("B7 mod missing after install")
 ids={x["id"] for x in m["mods"] if x["enabled_in_pack"]}
 if "waystones" in ids or "explorers-compass" in ids:
  raise RuntimeError("Disallowed travel mod enabled")
 m["mods"].sort(key=lambda x:x["id"])
 next(b for b in m["batches"] if b["id"]=="B10")["status"]="installed_server_boot_pending"
 m["note"]="B8 139 pinned mods (126 B7 + 13 new); no Waystones or Explorer's Compass; Farming for Blockheads Market pending purchase-list game audit; projectE/Chaos gate not implemented."
 MANIFEST.write_text(json.dumps(m,indent=2)+"\n")
 pack=ROOT/"pack.toml"
 pack.write_text(re.sub(r'(?m)^version = ".*"$',
   'version = "0.8.0-atm10-qol-natures-compass-market-candidate"',
   pack.read_text(),count=1))
 run("packwiz","refresh")
 print(run("python3","scripts/validate.py"),flush=True)
 report={"baseline":len(base),"new_mods":len(PROJECTS),"total":len(allmods),
  "server_side":sum(x["side"]!="client" for x in allmods),
  "client_only":sum(x["side"]=="client" for x in allmods),
  "approved_added":list(x[0] for x in PROJECTS),
  "pins":[x for x in allmods if x["curseforge_project"] in PREFIX],
  "market_magic_seed_whitelist_unverified":True,
  "client_gui_pending":True,"waystones_enabled":False,
  "earlier_known_good_client":"ecddc4342c439a2b973ad16acff112b7d0a4bb60"}
 (ROOT/"docs/B8-RESOLUTION.json").write_text(json.dumps(report,indent=2)+"\n")
 print("B8 RESOLVED "+json.dumps({"total":len(allmods),"server":report["server_side"],
  "client":report["client_only"]}),flush=True)
if __name__=="__main__":main()
