#!/usr/bin/env python3
"""Resolve three owner-approved publisher files only; no runtime or lock changes."""
import hashlib, io, json, re, sys, tomllib, urllib.parse, urllib.request, zipfile
from pathlib import Path

OUT = Path(sys.argv[1])
OUT.mkdir(parents=True, exist_ok=True)
API = "https://api.modrinth.com/v2"
MC = "1.21.1"
def get(url):
    req = urllib.request.Request(url, headers={"User-Agent": "heatwaveqc/TMM exact pin audit"})
    with urllib.request.urlopen(req, timeout=45) as r:
        return r.read(64_000_001)
def api(path):
    return json.loads(get(API + path))
def verify(data, slug):
    if not 0 < len(data) <= 64_000_000:
        raise ValueError("unexpected binary size")
    with zipfile.ZipFile(io.BytesIO(data)) as z:
        if z.testzip() is not None:
            raise ValueError("invalid archive CRC")
        meta = tomllib.loads(z.read("META-INF/neoforge.mods.toml").decode())
        if not meta.get("mods"):
            raise ValueError("missing mod declarations")
        return {"mods": meta["mods"], "dependencies": meta.get("dependencies", {})}
def quote(s):
    return json.dumps(s, ensure_ascii=False)
def modrinth(slug, name, project, wanted, vid=None):
    if vid:
        v = api("/version/" + vid)
    else:
        query = urllib.parse.urlencode({"game_versions": json.dumps([MC]), "loaders": json.dumps(["neoforge"])})
        candidates = [x for x in api("/project/" + project + "/version?" + query)
                      if x["version_number"] == wanted and x["version_type"] == "release"
                      and MC in x["game_versions"] and "neoforge" in x["loaders"]]
        if len(candidates) != 1:
            raise ValueError(f"{slug}: expected one exact release, got {len(candidates)}")
        v = candidates[0]
    if not (v["project_id"] == project and v["version_number"] == wanted and v["version_type"] == "release"
            and MC in v["game_versions"] and "neoforge" in v["loaders"]):
        raise ValueError("unexpected release metadata")
    files = [f for f in v["files"] if f.get("primary") and f["filename"].endswith(".jar")
             and not f["filename"].endswith(("-sources.jar", "-javadoc.jar"))]
    if len(files) != 1:
        raise ValueError("ambiguous primary runtime file")
    f = files[0]
    url = f["url"]
    parsed = urllib.parse.urlparse(url)
    if parsed.scheme != "https" or parsed.hostname != "cdn.modrinth.com" or not parsed.path.startswith(f"/data/{project}/versions/{v['id']}/"):
        raise ValueError("unexpected provider download path")
    sha = f["hashes"]["sha512"]
    if not re.fullmatch("[0-9a-f]{128}", sha):
        raise ValueError("missing provider SHA512")
    data = get(url)
    if hashlib.sha512(data).hexdigest() != sha:
        raise ValueError("publisher hash mismatch")
    metadata = verify(data, slug)
    pin = f'name = {quote(name)}\nfilename = {quote(f["filename"])}\nside = "both"\n\n[download]\nurl = {quote(url)}\nhash-format = "sha512"\nhash = {quote(sha)}\n\n[update]\n[update.modrinth]\nmod-id = {quote(project)}\nversion = {quote(v["id"])}\n'
    return pin, {"slug": slug, "version": wanted, "id": v["id"], "filename": f["filename"], "url": url, "sha512": sha, "provider_dependencies": v["dependencies"], **metadata}
def bakery():
    slug = "lets-do-bakery"
    name = "[Let's Do] Bakery - Farm&Charm Compat"
    filename = "letsdo-bakery-neoforge-2.1.12.jar"
    errors = []
    for host in ("mediafilez.forgecdn.net", "edge.forgecdn.net"):
        url = f"https://{host}/files/9747/401/{filename}"
        try:
            data = get(url)
            metadata = verify(data, slug)
            if not any(m["modId"] == "bakery" for m in metadata["mods"]):
                raise ValueError("incorrect native mod ID")
            sha = hashlib.sha1(data).hexdigest()
            pin = f'name = {quote(name)}\nfilename = {quote(filename)}\nside = "both"\n\n[download]\nhash-format = "sha1"\nhash = {quote(sha)}\nmode = "metadata:curseforge"\n\n[update]\n[update.curseforge]\nfile-id = 9747401\nproject-id = 1038130\n'
            return pin, {"slug": slug, "version": "2.1.12", "filename": filename, "url": url, "sha1": sha, **metadata}
        except Exception as e:
            errors.append(f"{host}: {e}")
    raise ValueError("; ".join(errors))
report = {"resolved": [], "blocked": []}
for slug, fn in (
    ("lets-do-bakery", bakery),
    ("oaks-delight", lambda: modrinth("oaks-delight", "Oaks Delight", "aWAAHxYu", "1.0.9", "DagHrUgg")),
    ("sweet-cakes", lambda: modrinth("sweet-cakes", "Sweet Cakes", "M0IszrLu", "1.1.1")),
):
    try:
        pin, row = fn()
        (OUT / (slug + ".pw.toml")).write_text(pin)
        report["resolved"].append(row)
        print("PIN", slug, json.dumps({"pin": pin, "evidence": row}, default=str), flush=True)
    except Exception as e:
        report["blocked"].append({"slug": slug, "error": str(e)})
        print("BLOCKED", slug, str(e), flush=True)
(OUT / "resolution.json").write_text(json.dumps(report, indent=2, default=str) + "\n")
if report["blocked"]:
    sys.exit(1)
