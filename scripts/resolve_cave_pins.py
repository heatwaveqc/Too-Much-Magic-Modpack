#!/usr/bin/env python3
"""Resolve only four approved cave artifacts and inspect exact Tectonic overlap.
No game boot, full roster download, repo writes or automatic pin activation.
"""
import hashlib, io, json, re, sys, tomllib, urllib.parse, urllib.request, zipfile
from pathlib import Path

OUT = Path(sys.argv[1])
OUT.mkdir(parents=True, exist_ok=True)
ROOT = Path(__file__).resolve().parents[1]
MAX = 96 * 1024 * 1024

def get(url):
    if urllib.parse.urlparse(url).hostname not in {"api.modrinth.com", "cdn.modrinth.com", "mediafilez.forgecdn.net", "edge.forgecdn.net"}:
        raise ValueError("unexpected publisher host")
    req = urllib.request.Request(url, headers={"User-Agent": "heatwaveqc/TMM cave exact pin audit"})
    with urllib.request.urlopen(req, timeout=45) as response:
        data = response.read(MAX + 1)
    if not 0 < len(data) <= MAX:
        raise ValueError("unexpected download size")
    return data

def inspect(data):
    with zipfile.ZipFile(io.BytesIO(data)) as jar:
        if jar.testzip() is not None:
            raise ValueError("bad archive CRC")
        meta = tomllib.loads(jar.read("META-INF/neoforge.mods.toml").decode("utf-8-sig"))
        if not meta.get("mods"):
            raise ValueError("missing native mod declaration")
        world = {}
        for n in jar.namelist():
            if re.search(r"(^|/)data/[^/]+/(dimension_type|dimension|worldgen/noise_settings|worldgen/density_function|worldgen/world_preset)/.+\.json$", n):
                raw = jar.read(n)
                world[n] = {"sha256": hashlib.sha256(raw).hexdigest()}
                if "/dimension_type/" in n or "/noise_settings/" in n or "/world_preset/" in n:
                    try:
                        d = json.loads(raw)
                        world[n]["min_y"] = d.get("min_y", d.get("noise", {}).get("min_y"))
                        world[n]["height"] = d.get("height", d.get("noise", {}).get("height"))
                    except (ValueError, AttributeError):
                        pass
        return {"mods": meta["mods"], "dependencies": meta.get("dependencies", {}), "worldgen": world}

def q(s):
    return json.dumps(s, ensure_ascii=False)

def cf(slug, name, filename, project, file):
    errors = []
    for host in ("mediafilez.forgecdn.net", "edge.forgecdn.net"):
        url = f"https://{host}/files/{file//1000}/{file%1000:03d}/{urllib.parse.quote(filename)}"
        try:
            data = get(url)
            evidence = inspect(data)
            digest = hashlib.sha1(data).hexdigest()
            pin = f'name = {q(name)}\nfilename = {q(filename)}\nside = "both"\n\n[download]\nhash-format = "sha1"\nhash = {q(digest)}\nmode = "metadata:curseforge"\n\n[update]\n[update.curseforge]\nfile-id = {file}\nproject-id = {project}\n'
            return pin, {"slug": slug, "url": url, "sha1": digest, "sha512": hashlib.sha512(data).hexdigest(), **evidence}
        except Exception as exc:
            errors.append(str(exc))
    raise ValueError("; ".join(errors))

def infinity(vid="6XJT5mi4", version="0.7"):
    v = json.loads(get("https://api.modrinth.com/v2/version/" + vid))
    if not (v["project_id"] == "DEq97NAA" and v["version_number"] == version and v["version_type"] == "beta" and "1.21.1" in v["game_versions"] and "neoforge" in v["loaders"]):
        raise ValueError("unexpected Infinity Cave release identity")
    files = [f for f in v["files"] if f.get("primary") and f["filename"].endswith(".jar")]
    if len(files) != 1:
        raise ValueError("ambiguous primary JAR")
    f = files[0]
    if not f["url"].startswith("https://cdn.modrinth.com/data/DEq97NAA/versions/" + vid + "/"):
        raise ValueError("unexpected publisher path")
    data = get(f["url"])
    digest = hashlib.sha512(data).hexdigest()
    if digest != f["hashes"]["sha512"]:
        raise ValueError("publisher hash mismatch")
    evidence = inspect(data)
    pin = f'name = "Infinity Cave"\nfilename = {q(f["filename"])}\nside = "both"\n\n[download]\nurl = {q(f["url"])}\nhash-format = "sha512"\nhash = {q(digest)}\n\n[update]\n[update.modrinth]\nmod-id = "DEq97NAA"\nversion = {q(vid)}\n'
    return pin, {"slug":"infinity-cave","url": f["url"],"sha512":digest,"provider_dependencies":v["dependencies"], **evidence}

report = {"resolved": [], "blocked": [], "worldgen_review_required": True}
for slug, fn in (
    ("infinity-cave", infinity),
    ("infinity-cave-0.61-fallback", lambda: infinity("JYmVFZJJ", "0.61")),
    ("stronger-mobs-below", lambda: cf("stronger-mobs-below","Stronger Mobs Below","strongermobsbelow-neoforge-1.21.1-1.0.0.jar",1293640,6874590)),
    ("enemy-echelons", lambda: cf("enemy-echelons","Enemy Echelons API","eechelonsapi-neoforge-1.21.1-1.0.0.jar",656060,6874524)),
    ("gottschcore", lambda: cf("gottschcore","GottschCore","gottschcore-neoforge-1.21.1-2.5.0.jar",272450,6863231)),
):
    try:
        pin, row = fn()
        (OUT / (slug + ".pw.toml")).write_text(pin, encoding="utf-8")
        row["slug"] = slug
        report["resolved"].append(row)
        print("PIN", slug, json.dumps({"pin":pin,"evidence":row},default=str), flush=True)
    except Exception as exc:
        report["blocked"].append({"slug":slug,"error":str(exc)})
        print("BLOCKED",slug, str(exc),flush=True)
try:
    t = tomllib.loads((ROOT / "mods/tectonic.pw.toml").read_text())["download"]
    data = get(t["url"])
    if hashlib.new(t["hash-format"], data).hexdigest() != t["hash"]:
        raise ValueError("Tectonic baseline hash mismatch")
    tw = inspect(data)["worldgen"]
    iw = next(row["worldgen"] for row in report["resolved"] if row["slug"] == "infinity-cave")
    def registry_paths(rows):
        mapped = {}
        for path, value in rows.items():
            key = path[path.index("data/"):]
            mapped.setdefault(key, []).append({"path":path, **value})
        return mapped
    a, b = registry_paths(iw), registry_paths(tw)
    report["tectonic_overlap"] = {k:{"infinity":a[k],"tectonic":b[k]} for k in sorted(a.keys() & b.keys())}
    report["tectonic_worldgen"] = tw
    print("WORLDGEN_OVERLAP", json.dumps(report["tectonic_overlap"]), flush=True)
except Exception as exc:
    report["blocked"].append({"slug":"tectonic-overlap","error":str(exc)})
(OUT / "resolution.json").write_text(json.dumps(report,indent=2,default=str)+"\n")
sys.exit(bool(report["blocked"]))
