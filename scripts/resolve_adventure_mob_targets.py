#!/usr/bin/env python3
"""Read-only exact-version metadata resolver for approved B19 adventure/mob batch.

Network access only to official Modrinth API/CDN and exact CurseForge CDN file.
Report valid candidate pins and mandatory dependency edges for review. Does not
mutate checkout, auto-upgrade existing packwiz dependencies, boot or package.
"""
from __future__ import annotations
import argparse, hashlib, io, json, re, tomllib, urllib.parse, urllib.request, zipfile
from pathlib import Path

ROOT=Path(__file__).resolve().parents[1]
SPEC=ROOT/"manifest/next-adventure-mob-targets.json"
SPEC_DATA=json.loads(SPEC.read_text())
MC, LOADER = "1.21.1", "neoforge"
API="https://api.modrinth.com/v2"
QUERY=urllib.parse.urlencode({"game_versions":json.dumps([MC]),"loaders":json.dumps([LOADER])})
CACHE={}
def fetch(url):
    if url not in CACHE:
        request=urllib.request.Request(url,headers={"User-Agent":"heatwaveqc/Too-Much-Magic-Modpack QA metadata resolver"})
        with urllib.request.urlopen(request,timeout=45) as res:
            CACHE[url]=res.read()
    return CACHE[url]
def api(path): return json.loads(fetch(API+path))
def project(slug): return api("/project/"+urllib.parse.quote(slug,safe=""))
def versions(pid):
    return api("/project/"+pid+"/version?"+QUERY)
def compatible(v):
    return MC in v["game_versions"] and LOADER in v["loaders"] and v["version_type"]=="release"
def choose(pid, wanted=None, candidates=None):
    all_versions=versions(pid) if candidates is None else candidates
    good=[v for v in all_versions if compatible(v) and v["project_id"]==pid]
    if wanted:
        exact=[v for v in good if wanted in (v["id"],v["version_number"])]
        if len(exact)!=1:
            raise ValueError(f"{pid}: exact requested version {wanted!r} unavailable, candidates: {[v['version_number'] for v in good[:8]]}")
        return exact[0]
    if not good: raise ValueError(f"{pid}: no approved stable release for 1.21.1 NeoForge")
    return max(good,key=lambda v:(v["date_published"],v["id"]))
def primary(v):
    files=[f for f in v["files"] if f["primary"] and f["filename"].endswith(".jar") and not f["filename"].endswith(("-sources.jar","-javadoc.jar"))]
    if len(files)!=1: raise ValueError("Missing unique primary runtime JAR: "+v["id"])
    f=files[0]
    if not re.fullmatch(r"[0-9a-f]{128}",f["hashes"]["sha512"]) or not f["url"].startswith("https://cdn.modrinth.com/data/"):
        raise ValueError("Untrusted URL/hash: "+v["id"])
    return f
def mrpin(name,side,v):
    f=primary(v); q=json.dumps
    return f'name = {q(name)}\nfilename = {q(f["filename"])}\nside = {q(side)}\n\n[download]\nurl = {q(f["url"])}\nhash-format = "sha512"\nhash = {q(f["hashes"]["sha512"])}\n\n[update]\n[update.modrinth]\nmod-id = {q(v["project_id"])}\nversion = {q(v["id"])}\n'
def main():
    p=argparse.ArgumentParser();p.add_argument("--output",type=Path,required=True)
    args=p.parse_args();args.output.mkdir(parents=True,exist_ok=True)
    roster=json.loads((ROOT/"manifest/mods.json").read_text())
    excluded={r["id"].lower() for r in roster["excluded"]}|{"torchmaster"}
    baseline={r.get("packwiz_slug",r["id"]):r for r in roster["mods"] if r["enabled_in_pack"]}
    installed_projects={}
    for slug,row in baseline.items():
        pin=tomllib.loads((ROOT/"mods"/(slug+".pw.toml")).read_text())
        mr=pin.get("update",{}).get("modrinth")
        if mr: installed_projects[mr["mod-id"]]=(slug,mr["version"])
    report={"roots":[],"required_dependencies":[],"blockers":[],"existing_version_conflicts":[]}
    resolved={}; selected_versions={}; root_ids=set(); root_projects={}
    def add_row(target,project_data,v,root):
        pid=project_data["id"];slug=target["id"];side=target["side"];name=target["name"]
        if slug.lower() in excluded or project_data.get("slug","").lower() in excluded: raise ValueError("Excluded mod: "+slug)
        if project_data.get("project_type")!="mod": raise ValueError("Not a mod project: "+slug)
        if side=="both" and "unsupported" in (project_data.get("client_side"),project_data.get("server_side")): raise ValueError("Unexpected side metadata: "+slug)
        if side=="client" and project_data.get("client_side")=="unsupported": raise ValueError("Not a client mod: "+slug)
        f=primary(v)
        if pid in selected_versions and selected_versions[pid]!=v["id"]: raise ValueError("Dependency version collision: "+slug)
        selected_versions[pid]=v["id"]
        row={"id":slug,"name":name,"category":target["category"],"side":side,"project":pid,"version_id":v["id"],"version":v["version_number"],"filename":f["filename"],"url":f["url"],"sha512":f["hashes"]["sha512"],"dependencies":v["dependencies"],"project_categories":project_data.get("categories",[]),"source":f"https://modrinth.com/mod/{project_data['slug']}/version/{v['id']}"}
        (args.output/(slug+".pw.toml")).write_text(mrpin(name,side,v))
        resolved[pid]=row
        (report["roots"] if root else report["required_dependencies"]).append(row)
        return row
    for target in SPEC_DATA["modrinth"]:
        try:
            pr=project(target["slug"]);v=choose(pr["id"],target["version"]);root_ids.add(pr["id"]);root_projects[pr["id"]]=target
            add_row(target,pr,v,True)
        except Exception as e: report["blockers"].append({"root":target["id"],"error":str(e)})
    expanded=set()
    def requirements(pid):
        if pid in expanded:return
        expanded.add(pid)
        parent=resolved[pid]
        for dep in parent["dependencies"]:
            if dep["dependency_type"]=="incompatible":
                if dep.get("project_id") in installed_projects or dep.get("project_id") in resolved:
                    report["blockers"].append({"parent":parent["id"],"incompatible":dep})
                continue
            if dep["dependency_type"]!="required":continue
            try:
                version_id=dep.get("version_id");did=dep.get("project_id")
                if version_id and not did: did=api("/version/"+version_id)["project_id"]
                if not did: raise ValueError("Required dependency has no project ID")
                if did in installed_projects:
                    slug,current=installed_projects[did]
                    if version_id and current!=version_id: report["existing_version_conflicts"].append({"parent":parent["id"],"dependency":slug,"installed":current,"requested":version_id})
                    continue
                if did in resolved:
                    if version_id and selected_versions[did]!=version_id: raise ValueError("Required exact version conflicts with selected root/dependency: "+did)
                    requirements(did);continue
                dp=project(did);slug=dp["slug"]
                if slug in baseline:
                    report["existing_version_conflicts"].append({"parent":parent["id"],"dependency":slug,"reason":"existing cross-provider pin; reconcile, do not duplicate","requested":version_id})
                    continue
                if slug in excluded: raise ValueError("Required dependency excluded: "+slug)
                # Treat dependencies as proposals; don't auto-approve unrelated content.
                dv=choose(did,version_id)
                side="client" if dp.get("server_side")=="unsupported" else "server" if dp.get("client_side")=="unsupported" else "both"
                add_row({"id":slug,"name":dp["title"],"category":"library","side":side},dp,dv,False)
                requirements(did)
            except Exception as e:report["blockers"].append({"parent":parent["id"],"required":dep,"error":str(e)})
    for pid in list(resolved): requirements(pid)
    for row in SPEC_DATA["curseforge"]:
        # Exact official CF file only. No arbitrary alternate file or third-party mirror.
        try:
            n=row["file_id"];file=row["filename"]
            url=f"https://mediafilez.forgecdn.net/files/{n//1000}/{n%1000}/{urllib.parse.quote(file)}"
            jar=fetch(url);sha=hashlib.sha512(jar).hexdigest()
            with zipfile.ZipFile(io.BytesIO(jar)) as z:
                metadata=tomllib.loads(z.read("META-INF/neoforge.mods.toml").decode())
                ids=[m["modId"] for m in metadata["mods"]]
                if "twilightforest" not in ids:raise ValueError("Wrong official file metadata")
            q=json.dumps
            pin=f'name = {q(row["name"])}\nfilename = {q(file)}\nside = {q(row["side"])}\n\n[download]\nurl = {q(url)}\nhash-format = "sha512"\nhash = {q(sha)}\n\n[update]\n[update.curseforge]\nproject-id = {row["project_id"]}\nfile-id = {n}\n'
            (args.output/(row["id"]+".pw.toml")).write_text(pin)
            report["roots"].append({**row,"url":url,"sha512":sha,"mod_metadata_dependencies":metadata.get("dependencies",{}),"source":f"https://www.curseforge.com/minecraft/mc-mods/the-twilight-forest/files/{n}"})
        except Exception as e:report["blockers"].append({"root":row["id"],"error":str(e)})
    (args.output/"resolution.json").write_text(json.dumps(report,indent=2)+"\n")
    for key in report:
        for row in report[key]:print("RESOLUTION",key,json.dumps(row,separators=(",",":")),flush=True)
    print(f"Resolved roots: {len(report['roots'])}; required dependency candidates: {len(report['required_dependencies'])}; blockers: {len(report['blockers'])}; version conflicts: {len(report['existing_version_conflicts'])}",flush=True)
if __name__=="__main__":main()
