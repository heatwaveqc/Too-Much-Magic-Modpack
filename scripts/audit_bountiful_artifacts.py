#!/usr/bin/env python3
"""Targeted two-artifact audit. No Minecraft boot, pack assembly or runtime writes."""
import hashlib
import io
import json
import re
import sys
import tomllib
import urllib.parse
import urllib.request
import zipfile
from pathlib import Path

CANDIDATES = {"bountiful": "swTb6NU9", "kambrik": "rCSmCZpX"}

def get(url, limit):
    with urllib.request.urlopen(urllib.request.Request(url, headers={"User-Agent": "TMM-Bountiful-Audit/1.0"}), timeout=60) as response:
        data = response.read(limit + 1)
    if len(data) > limit:
        raise ValueError("response exceeds audit limit")
    return data

def require(condition, message):
    if not condition:
        raise ValueError(message)

def audit(slug, version_id):
    meta = json.loads(get("https://api.modrinth.com/v2/version/" + version_id, 2_000_000))
    project = json.loads(get("https://api.modrinth.com/v2/project/" + meta["project_id"], 2_000_000))
    require(project["slug"] == slug and meta["id"] == version_id, "project/version identity mismatch")
    require(meta["version_number"] == "8.0.0-beta.2", "unexpected version")
    require("1.21.1" in meta["game_versions"] and "neoforge" in meta["loaders"], "wrong game or loader")
    files = [f for f in meta["files"] if f["primary"] and f["filename"].endswith(".jar") and not f["filename"].endswith("-sources.jar")]
    require(len(files) == 1, "expected one primary binary")
    f = files[0]
    parsed = urllib.parse.urlparse(f["url"])
    require(parsed.scheme == "https" and parsed.netloc == "cdn.modrinth.com" and parsed.path.startswith("/data/" + meta["project_id"] + "/versions/" + version_id + "/"), "invalid CDN path")
    expected = f["hashes"]["sha512"]
    require(re.fullmatch("[0-9a-f]{128}", expected), "invalid SHA512")
    binary = get(f["url"], 80_000_000)
    require(hashlib.sha512(binary).hexdigest() == expected, "provider/file hash mismatch")
    require(hashlib.sha1(binary).hexdigest() == f["hashes"]["sha1"], "SHA1 mismatch")
    with zipfile.ZipFile(io.BytesIO(binary)) as jar:
        mod = tomllib.loads(jar.read("META-INF/neoforge.mods.toml").decode())
        require(any(m["modId"] == slug for m in mod["mods"]), "wrong JAR mod ID")
        defaults = {}
        for name in sorted(jar.namelist()):
            if re.fullmatch(r"data/[^/]+/(bounty_pools|bounty_decrees)/.+\.json", name):
                obj = json.loads(jar.read(name))
                defaults[name] = obj
        recipes = sorted(n for n in jar.namelist() if re.search(r"/(?:recipe|recipes|loot_table|worldgen)/", n) and n.endswith(".json"))
        embedded = [n for n in jar.namelist() if n.startswith("META-INF/jarjar/") and n.endswith(".json")]
        result = {"slug":slug,"version_id":version_id,"project_id":meta["project_id"],"version":meta["version_number"],"channel":meta["version_type"],"file":f["filename"],"url":f["url"],"hashes":f["hashes"],"license":project["license"],"client_side":project["client_side"],"server_side":project["server_side"],"provider_dependencies":meta["dependencies"],"jar_metadata":mod,"bundled_data":defaults,"recipe_worldgen_paths":recipes,"embedded_metadata":{n:json.loads(jar.read(n)) for n in embedded}}
        if slug == "bountiful":
            require(defaults, "no bundled bounty resources found")
        return result

def main():
    result = {k:audit(k,v) for k,v in CANDIDATES.items()}
    print(json.dumps(result, indent=2, sort_keys=True))
    return 0

if __name__ == "__main__":
    sys.exit(main())
