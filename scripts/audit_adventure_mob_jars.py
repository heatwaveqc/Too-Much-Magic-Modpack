#!/usr/bin/env python3
"""Inspect exact frozen provider pins' actual jar mod IDs/dependency ranges.
No game, install, compilation or artifact redistribution. Only metadata logged.
"""
import concurrent.futures, hashlib, io, json, pathlib, tomllib, urllib.request, urllib.parse, zipfile
ROOT=pathlib.Path(__file__).resolve().parents[1]
LOCK=json.loads((ROOT/"manifest/next-adventure-mob-resolution.json").read_text())
HEADERS={"User-Agent":"heatwaveqc/Too-Much-Magic-Modpack pinned JAR metadata audit"}
def get(url):
    with urllib.request.urlopen(urllib.request.Request(url,headers=HEADERS),timeout=90) as h:return h.read()
def api(path):return json.loads(get("https://api.modrinth.com/v2"+path))
def inspect_jar(raw,depth=0):
    with zipfile.ZipFile(io.BytesIO(raw)) as z:
        name=next((p for p in ("META-INF/neoforge.mods.toml","META-INF/mods.toml") if p in z.namelist()),None)
        result={}
        if name:
            m=tomllib.loads(z.read(name).decode("utf-8"))
            result={"mods":[{"id":x["modId"],"version":x.get("version")} for x in m.get("mods",[])],"dependencies":{key:[{a:x.get(a) for a in ("modId","type","mandatory","versionRange","side")} for x in values if x.get("type") in ("required","incompatible") or x.get("mandatory") is True] for key,values in m.get("dependencies",{}).items()}}
            mf=z.read("META-INF/MANIFEST.MF").decode("utf-8",errors="replace") if "META-INF/MANIFEST.MF" in z.namelist() else ""
            result["manifest_version"]=next((s.partition(": ")[2] for s in mf.splitlines() if s.startswith("Implementation-Version: ")),None)
        elif "fabric.mod.json" in z.namelist():result={"fabric_metadata_only":True}
        if depth==0 and "META-INF/jarjar/metadata.json" in z.namelist():
            jarjar=json.loads(z.read("META-INF/jarjar/metadata.json"))
            result["jarjar"]=[{"identifier":item.get("identifier"),"version":item.get("version"),"metadata":inspect_jar(z.read(item["path"]),1)} for item in jarjar["jars"]]
        return result
def inspect(row):
    try:
        raw=get(row["url"]);sha=hashlib.sha512(raw).hexdigest()
        if row.get("sha512") and row["sha512"]!=sha:raise ValueError("Provider hash mismatch")
        result=inspect_jar(raw)
        if not result.get("mods"):raise ValueError("No native loader mod metadata")
        return {"id":row["id"],"side":row.get("side"),"version":row.get("version"),"filename":row["filename"],"sha512":sha,"url":row["url"],**result}
    except Exception as e:return {"id":row["id"],"error":str(e)}
def pinned_baseline(slug):
    p=tomllib.loads((ROOT/"mods"/(slug+".pw.toml")).read_text())
    url=p["download"].get("url")
    if not url and "curseforge" in p.get("update",{}):
        f=p["update"]["curseforge"]["file-id"]
        url=f"https://mediafilez.forgecdn.net/files/{f//1000}/{f%1000}/{urllib.parse.quote(p['filename'])}"
    if not url:raise ValueError("No direct official URL for baseline "+slug)
    row={"id":slug,"filename":p["filename"],"url":url,"side":p.get("side","both")}
    raw=get(url)
    algorithm=p["download"]["hash-format"]
    if hashlib.new(algorithm,raw).hexdigest()!=p["download"]["hash"]:raise ValueError("Baseline hash mismatch: "+slug)
    return {**row,"sha512":hashlib.sha512(raw).hexdigest(),**inspect_jar(raw)}
def proposal(dep):
    try:
        v=api("/version/"+dep["version_id"])
        f=next(x for x in v["files"] if x["primary"] and x["filename"].endswith(".jar"))
        return {**dep,"version":v["version_number"],"release_type":v["version_type"],"supported_loaders":v["loaders"],"game_versions":v["game_versions"],"project":v["project_id"],"filename":f["filename"],"url":f["url"],"sha512":f["hashes"]["sha512"],"dependencies":v["dependencies"]}
    except Exception as e:return {**dep,"error":str(e)}
def main():
    for id in ("ccKDOlHs","kkmrDlKT","YBz7DOs8"):
        try:
            p=api("/project/"+id)
            vs=api("/project/"+id+"/version?"+urllib.parse.urlencode({"game_versions":json.dumps(["1.21.1"]),"loaders":json.dumps(["neoforge"])}))
            print("DEPENDENCY_IDENTITY",json.dumps({"project":id,"slug":p["slug"],"title":p["title"],"versions":[{"id":v["id"],"v":v["version_number"],"channel":v["version_type"]} for v in vs[:8]]},separators=(",",":")),flush=True)
        except Exception as e:print("DEPENDENCY_IDENTITY_ERROR",id,str(e),flush=True)
    deps=[proposal(x) for x in LOCK["audit_only_dependencies"]]
    for row in deps: print("BETA_PROPOSAL_ONLY",json.dumps(row,separators=(",",":")),flush=True)
    allrows=LOCK["roots"]+LOCK["required_dependencies"]+[x for x in deps if "url" in x]
    with concurrent.futures.ThreadPoolExecutor(max_workers=4) as pool:
        for v in pool.map(inspect,allrows):
            print("JAR_META",json.dumps(v,separators=(",",":")),flush=True)
        for slug in ("geckolib","architectury-api","cloth-config","curios","yungs-api","jei"):
            try: print("BASELINE_META",json.dumps(pinned_baseline(slug),separators=(",",":")),flush=True)
            except Exception as e: print("BASELINE_ERROR",slug,str(e),flush=True)
if __name__=="__main__":main()
