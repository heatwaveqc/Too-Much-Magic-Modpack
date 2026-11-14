#!/usr/bin/env python3
"""Explicitly pin Iron's Spells' two upstream-required missing libraries.

The only beta exception is KosmX playerAnimator 2.0.x for Minecraft 1.21.1,
required by an otherwise stable Iron's Spells build. Never auto-substitute.
"""
from __future__ import annotations
import json
from pathlib import Path
import sys
import tomllib
from resolve_stable_gameplay import ROOT, api, matching_project, release_version, run

MANIFEST = ROOT / "manifest/mods.json"
EXCEPTIONS = ROOT / "docs/B3-DEPENDENCY-EXCEPTIONS.json"

def add(mod_id: str, slug: str, expected_name: str, stable_only: bool):
    project = matching_project(slug, expected_name)
    if stable_only:
        v = release_version(project["id"])
    else:
        versions = api("project/" + project["id"] + "/version",
            {"loaders": json.dumps(["neoforge"]), "game_versions": json.dumps(["1.21.1"])})
        valid = [v for v in versions if
            "neoforge" in v.get("loaders",[]) and "1.21.1" in v.get("game_versions",[])
            and v.get("version_type") == "beta" and
            v.get("version_number","").startswith("2.0.")
            and any(f.get("filename","").lower().endswith(".jar") for f in v.get("files",[]))]
        valid.sort(key=lambda x:x.get("date_published",""),reverse=True)
        v = valid[0] if valid else None
    if not v:
        raise RuntimeError("No upstream compatible "+("release" if stable_only else "necessary beta") + " for " + slug)
    rc, out = run("packwiz","--yes","modrinth","add",
                  "--project-id",project["id"],"--version-id",v["id"])
    if rc:
        raise RuntimeError("packwiz dependency installation failed: " + out[-2200:])
    print("PIN REQUIRED: " + mod_id + " " + v["version_number"] + " " + v["version_type"],flush=True)
    return {"id":mod_id,"project_id":project["id"],"version_id":v["id"],
            "version_number":v["version_number"],"version_type":v["version_type"]}

def main():
    deps = [
        add("irons-lib","irons-lib","Iron's Lib",True),
        add("playeranimator","playeranimator","playerAnimator",False),
    ]
    packwiz_files={p.name.removesuffix(".pw.toml"):p for p in (ROOT/"mods").glob("*.pw.toml")}
    manifest=json.loads(MANIFEST.read_text())
    by_id={m["id"]:m for m in manifest["mods"]}
    for dep in deps:
        matching=[]
        for slug,path in packwiz_files.items():
            meta=tomllib.loads(path.read_text())
            up=meta.get("update",{}).get("modrinth",{})
            if up.get("mod-id")==dep["project_id"] and up.get("version")==dep["version_id"]:
                matching.append((slug,meta))
        if len(matching)!=1:
            raise RuntimeError("Missing/duplicate expected dependency metadata for " + dep["id"])
        slug,data=matching[0]
        m=by_id.get(dep["id"])
        if not m:
            m={"id":dep["id"],"name":data.get("name",dep["id"]),"category":"dependency",
               "desired":True,"version":None,"release_status":"unverified",
               "source":None,"enabled_in_pack":False,"dependencies_verified":False}
            by_id[dep["id"]]=m
            manifest["mods"].append(m)
        m["packwiz_slug"]=slug
        m["version"]=data["filename"].removesuffix(".jar")
        m["enabled_in_pack"]=True
        m["release_status"]="verified_release" if dep["version_type"]=="release" else "experimental_release"
        m["source"]="https://modrinth.com/mod/"+dep["project_id"]
        m["note"]=("Required runtime dependency of stable Iron's Spells 3.16.3. " +
            ("Release channel." if dep["version_type"]=="release" else
             "Only compatible 2.0.x files are beta-tagged; explicit limited exception for boot prototype."))
    manifest["mods"].sort(key=lambda m:m["id"])
    batch=next(x for x in manifest["batches"] if x["id"]=="B5")
    batch["members"]=sorted(set(batch["members"])|{d["id"] for d in deps})
    batch["status"]="repaired_dependencies_reboot_pending"
    MANIFEST.write_text(json.dumps(manifest,indent=2)+"\n")
    EXCEPTIONS.write_text(json.dumps({
        "reason":"NeoForge server error: irons_spellbooks requires irons_lib [1.21.1-2,1.21.1-3), playeranimator >=2.0.1+1.21.1",
        "exception_policy":"Allow beta-tagged prerequisite only; no new beta/alpha gameplay mods installed",
        "dependencies":deps
    },indent=2)+"\n")
    for args in [("packwiz","refresh"),(sys.executable,"scripts/validate.py")]:
        rc,out=run(*args)
        print(out,flush=True)
        if rc: raise SystemExit("validation/refresh failed: "+" ".join(args))

if __name__=="__main__":
    main()
