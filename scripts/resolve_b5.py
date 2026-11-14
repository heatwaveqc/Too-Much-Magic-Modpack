#!/usr/bin/env python3
"""Resolve user-approved B5 projects against exact upstream identities.

CurseForge projects use *verified official project IDs*. Modrinth candidates
need an EXACT normalized project title match or an explicitly whitelisted alias,
plus a NeoForge 1.21.1 version. Fuzzy search alone never authorizes install.

Script records non-publication/unavailable candidates rather than silently
substituting alternate mods or booting with banned dependencies.
"""
from __future__ import annotations
import datetime
import json
import pathlib
import re
import subprocess
import sys
import time
import tomllib
import urllib.parse
import urllib.request
import urllib.error

ROOT=pathlib.Path(__file__).resolve().parents[1]
MANIFEST=ROOT/"manifest/mods.json"
RESULT=ROOT/"docs/B5-RESOLUTION.json"

# Canonical id / CurseForge immutable PROJECT ID / expected project-title.
# Published official pages explicitly identify all these 12 project IDs.
CURSEFORGE=[
 ("eidolon-repraised",870250,"eidolon repraised"),
 ("extra-botany-revived",1764698,"extra botany revived"),
 ("feywild",484377,"feywild"),
 ("primal-magick",545271,"primal magick"),
 ("psi",241665,"psi"),
 ("scriptor-magicae",817436,"scriptor magicae"),
 ("summoning-rituals",671040,"summoning rituals"),
 ("pastel",1264556,"pastel"),
 ("enchanted-witchcraft",560363,"enchanted witchcraft"),
 ("aspect-alchemy",1523956,"aspect alchemy"),
 ("vampirism",233029,"vampirism become a vampire"),
 ("werewolves",417851,"werewolves become a beast"),
]
# Exact titles/aliases must be tied to an approved named project.
# Search results that share words but not full identity are never installed.
MODRINTH=[
 ("goety",["goety - the dark arts","goety"]),
 ("malum",["malum"]),
 ("hexerei",["hexerei"]),
 ("forbidden-arcanus",["forbidden & arcanus","forbidden and arcanus"]),
 ("embers-re-ignited",["embers re-ignited"]),
 ("totemic",["totemic"]),
 ("reliquary-reincarnations",["reliquary reincarnations","reliquary"]),
 ("enigmatic-legacy-plus",["enigmatic legacy+","enigmatic legacy plus"]),
 ("hexcast-neoforged",["hexcast neoforged"]),
 ("ars-elemental",["ars elemental"]),
 ("ars-ocultas",["ars ocultas"]),
 ("ars-additions",["ars additions"]),
 ("ars-controle",["ars controle"]),
 ("ars-unification",["ars unification"]),
 ("ars-elemancy",["ars elemancy"]),
 ("ars-n-spells",["ars 'n spells","ars n spells"]),
 ("goety-cataclysm",["goety cataclysm"]),
 ("gaze-malum",["gaze","gaze (malum addon)"]),
 ("psi-tweaks",["psi: tweaks and additions","psi tweaks and additions"]),
]
# The canonical mod manifest controls these hard bans; these patterns guard
# against dependencies introducing their libraries behind our backs.
BANNED_IDS={"create","sophisticatedstorage","sophisticatedbackpacks","sophisticatedcore",
"appliedenergistics2","refinedstorage","ftbultimine","bloodmagic",
"elementalcraft","naturesaura","forcecraft","mysticalagriculture",
"mysticalagradditions","spellengine","rpgseries"}
# Do not add a "broadly helpful" storage or logistics dependency.
def norm(s): return re.sub(r"[^a-z0-9]+","",s.casefold())
def run(*args):
    p=subprocess.run(args,cwd=ROOT,stdout=subprocess.PIPE,stderr=subprocess.STDOUT,text=True,timeout=200)
    return p.returncode,p.stdout
def api(path,query=None):
    base="https://api.modrinth.com/v2/"+path
    if query: base+="?"+urllib.parse.urlencode(query)
    for retry in range(4):
        try:
            req=urllib.request.Request(base,headers={"User-Agent":"TooMuchMagic-Roster-B5/0.1"})
            with urllib.request.urlopen(req,timeout=45) as res:return json.load(res)
        except urllib.error.HTTPError as ex:
            if ex.code not in {429,500,502,503,504} or retry==3:raise
            time.sleep(2+retry*2)
    raise RuntimeError("Unreachable Modrinth search")
def search_project(names):
    # Search is ONLY discovery, followed by strict title match and project ID pin.
    matches={}
    for name in names[:2]:
        facets=json.dumps([["project_type:mod"],["categories:neoforge"],["versions:1.21.1"]])
        res=api("search",{"query":name,"limit":100,"facets":facets})
        for hit in res.get("hits",[]):
            if norm(hit.get("title","")) in {norm(x) for x in names}:
                matches[hit["project_id"]]=hit
        if matches:break
    if len(matches)!=1:
        raise LookupError("No uniquely identified exact Modrinth project title: "+
                          str([(x.get("title"),pid) for pid,x in matches.items()]))
    pid,hit=next(iter(matches.items()))
    details=api("project/"+pid)
    if norm(details.get("title","")) not in {norm(x) for x in names}:
        raise LookupError("Search/project title discrepancy")
    if details.get("project_type")!="mod":raise LookupError("Not a mod")
    return pid,details
def select_version(pid):
    versions=api("project/"+pid+"/version",{
         "loaders":json.dumps(["neoforge"]),
         "game_versions":json.dumps(["1.21.1"])})
    versions=[x for x in versions if x.get("version_type") in {"release","beta","alpha"}
         and "1.21.1" in x.get("game_versions",[])
         and "neoforge" in x.get("loaders",[])
         and any(y.get("filename","").endswith(".jar") and "sources" not in y.get("filename","").lower()
                 for y in x.get("files",[]))]
    versions.sort(key=lambda x:( {"release":2,"beta":1,"alpha":0}[x["version_type"]],
                                x.get("date_published","")),reverse=True)
    return versions[0] if versions else None

def main():
    m=json.loads(MANIFEST.read_text())
    byid={x["id"]:x for x in m["mods"]}
    excluded={norm(x["id"]) for x in m["excluded"]}|BANNED_IDS
    requested=set(x[0] for x in CURSEFORGE+MODRINTH)
    assert requested<=set(byid), sorted(requested-set(byid))
    assert all(byid[x].get("desired") and not byid[x].get("enabled_in_pack") for x in requested)
    report=[]
    # First install explicitly identified CurseForge mods; --yes chooses compatible
    # 1.21.1 NeoForge release, not newest on another platform.
    for mid,pid,title in CURSEFORGE:
        rc,out=run("packwiz","--yes","curseforge","add","--addon-id",str(pid))
        info={"id":mid,"provider":"curseforge","project_id":pid,"status":"resolved" if rc==0 else "blocked"}
        if rc:info["reason"]=out[-1600:]
        report.append(info)
        print("CURSEFORGE",json.dumps(info),flush=True)
    for mid,names in MODRINTH:
        try:
            pid,project=search_project(names)
            version=select_version(pid)
            if not version:
                raise LookupError("No compatible Minecraft 1.21.1 NeoForge mod version")
            rc,out=run("packwiz","--yes","modrinth","add",
                        "--project-id",pid,"--version-id",version["id"])
            if rc:raise RuntimeError("packwiz refused exact version: "+out[-1200:])
            result={"id":mid,"provider":"modrinth","project_id":pid,
              "version_id":version["id"],"version_type":version["version_type"],
              "version_number":version["version_number"],"status":"resolved"}
            report.append(result)
            print("MODRINTH",json.dumps(result),flush=True)
        except Exception as ex:
            info={"id":mid,"provider":"modrinth","status":"blocked","reason":str(ex)[:1400]}
            report.append(info);print("BLOCKED",json.dumps(info),flush=True)
    rc,out=run("packwiz","refresh")
    if rc:raise RuntimeError("packwiz refresh failed: "+out[-1600:])
    cfmap={pid:mid for mid,pid,_ in CURSEFORGE}
    mrmap={x["project_id"]:x["id"] for x in report if x.get("provider")=="modrinth" and x["status"]=="resolved"}
    known_slugs={x.get("packwiz_slug",x["id"]):x["id"] for x in m["mods"]}
    active_before={x["id"] for x in m["mods"] if x.get("enabled_in_pack")}
    installed=set()
    pins=[]
    for p in sorted((ROOT/"mods").glob("*.pw.toml")):
        slug=p.name.removesuffix(".pw.toml")
        data=tomllib.loads(p.read_text())
        cf=data.get("update",{}).get("curseforge",{})
        mr=data.get("update",{}).get("modrinth",{})
        mid=cfmap.get(cf.get("project-id")) or mrmap.get(mr.get("mod-id")) or known_slugs.get(slug) or slug
        if norm(slug) in excluded or norm(data.get("name","")) in excluded or norm(mid) in excluded:
            raise RuntimeError("FORBIDDEN MOD SELECTED: "+str([mid,slug,data.get("name")]))
        if any(x in norm(data.get("name","")) for x in {"sophisticatedcore","sophisticatedstorage",
                                                        "sophisticatedbackpacks","spellengine"}):
            raise RuntimeError("Excluded framework auto-resolved: "+str(data.get("name")))
        d=data.get("download",{})
        if not d.get("hash") or d.get("hash-format") not in {"sha1","sha256","sha512"}:
            raise RuntimeError("No immutable artifact hash for "+slug)
        if not d.get("url") and d.get("mode")!="metadata:curseforge":
            raise RuntimeError("Unresolvable download mode "+slug)
        if mid in installed:raise RuntimeError("Duplicate mod identity "+mid)
        installed.add(mid)
        entry=byid.get(mid)
        if entry is None:
            entry={"id":mid,"name":data.get("name",slug),"category":"dependency","desired":True,
                   "version":None,"release_status":"unverified","source":None,
                   "enabled_in_pack":False,"dependencies_verified":False}
            m["mods"].append(entry);byid[mid]=entry
        entry["packwiz_slug"]=slug
        entry["version"]=data["filename"].removesuffix(".jar")
        entry["enabled_in_pack"]=True
        r=next((x for x in report if x["id"]==mid and x["status"]=="resolved"),None)
        if r:
            entry["release_status"]="experimental_release" if r.get("version_type") in {"beta","alpha"} else "verified_release"
            entry["source"]=("https://modrinth.com/mod/"+r["project_id"]) if r["provider"]=="modrinth" else (
                "https://www.curseforge.com/minecraft/mc-mods/"+str(r["project_id"]))
            entry["note"]=("User-approved in PR #6. B5 pinned and server test pending. "+
                           ("Modrinth upstream release type: "+r["version_type"] if r.get("version_type") else
                            "CurseForge project ID pinned; verify file maturity in test report"))
        pins.append({"id":mid,"slug":slug,"file":data["filename"],
                     "side":data.get("side","both"),"curseforge_project":cf.get("project-id"),
                     "curseforge_file":cf.get("file-id"),"modrinth_project":mr.get("mod-id"),
                     "modrinth_version":mr.get("version")})
    if not active_before.issubset(installed):
        raise RuntimeError("Existing approved baseline mods accidentally removed: "+str(sorted(active_before-installed)))
    for x in report:
        if x["status"]=="resolved" and x["id"] not in installed:
            x["status"]="blocked";x["reason"]="Provider returned OK, but exact approved project not found in packwiz manifest"
    added=sorted(installed-active_before)
    m["mods"].sort(key=lambda x:x["id"])
    b=next(x for x in m["batches"] if x["id"]=="B7")
    b["members"]=sorted(set(b["members"])|set(added))
    b["status"]="metadata_resolved_boot_pending"
    m["note"]="B5 expanded approved roster loaded from PR6. Exact provider-resolution evidence docs/B5-RESOLUTION.json. ProjectE Chaos Guardian gate remains unimplemented."
    MANIFEST.write_text(json.dumps(m,indent=2)+"\n")
    RESULT.write_text(json.dumps({
        "baseline_mods":len(active_before),"total_pins":len(installed),
        "added_mods":added,"approved_candidates":report,"exact_pins":pins,
        "generated_at":datetime.datetime.now(datetime.timezone.utc).isoformat(),
        "unresolved":[x for x in report if x["status"]!="resolved"],
        "warnings":["Modrinth alpha/beta newly approved mods may be included for boot only",
        "CurseForge project-ID selection may include optional features; verify side-effects",
        "No custom KubeJS progression/gates installed; ProjectE and Expansion ungated"]
    },indent=2)+"\n")
    rc,out=run("packwiz","refresh")
    if rc:raise RuntimeError("packwiz refresh metadata failed: "+out[-2000:])
    rc,out=run(sys.executable,"scripts/validate.py")
    print(out,flush=True)
    if rc:raise RuntimeError("Packwiz/manifest validation failed")
    print("B5 RESULT",json.dumps({"before":len(active_before),"now":len(installed),"added":added,
        "unresolved":[x["id"] for x in report if x["status"]!="resolved"]}),flush=True)
if __name__=="__main__":
    main()
