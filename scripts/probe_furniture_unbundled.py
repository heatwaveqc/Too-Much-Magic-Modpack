#!/usr/bin/env python3
"""Official Modrinth probe for 1.21.1 NeoForge Furniture unbundled from Foundation.

Targeted read-only artifact verification. Does not install or start Minecraft.
"""
from __future__ import annotations
import hashlib
from io import BytesIO
import json
from urllib.request import Request, urlopen
from zipfile import ZipFile
from verify_approved_recovery_artifacts import jar_metadata, TIMEOUT, MAX_ONE_FILE

PROJECT_ID = "3JQDJrYW"
TARGET = "1.1.11"
GAME = "1.21.1"
LOADER = "neoforge"

def main() -> int:
    req = Request(f"https://api.modrinth.com/v2/project/{PROJECT_ID}/version",
                  headers={"User-Agent": "TooMuchMagic-Exact-Dependency-Audit/1.0"})
    with urlopen(req, timeout=TIMEOUT) as response:
        versions = json.load(response)
    matches = [v for v in versions if TARGET in v.get("version_number", "")
               and GAME in v.get("game_versions", []) and LOADER in v.get("loaders", [])]
    if len(matches) != 1:
        print(json.dumps({"matching_versions": [v.get("id") for v in matches]}))
        raise ValueError("Not exactly one suitable verified Minecraft 1.21.1 NeoForge Furniture release")
    v = matches[0]
    candidates = [f for f in v["files"] if f.get("primary") or len(v["files"]) == 1]
    if len(candidates) != 1 or not candidates[0]["url"].startswith("https://cdn.modrinth.com/"):
        raise ValueError("Ambiguous official Furniture artifact")
    f = candidates[0]
    with urlopen(Request(f["url"], headers={"User-Agent": "TooMuchMagic-Exact-Dependency-Audit/1.0"}),
                 timeout=TIMEOUT) as response:
        b = response.read(MAX_ONE_FILE + 1)
    if len(b) > MAX_ONE_FILE:
        raise ValueError("Oversized artifact")
    hashes = {"sha1": hashlib.sha1(b).hexdigest(), "sha512": hashlib.sha512(b).hexdigest()}
    for algorithm in ("sha1", "sha512"):
        if f.get("hashes", {}).get(algorithm) != hashes[algorithm]:
            raise ValueError("Official published hash mismatch: " + algorithm)
    with ZipFile(BytesIO(b)) as jar:
        bad = jar.testzip()
        if bad:
            raise ValueError("Corrupted jar: " + bad)
        nested = [x for x in jar.namelist() if x.endswith(".jar")]
    meta = jar_metadata(b)
    output = {
        "project_id": PROJECT_ID, "version_id": v["id"],
        "version_number": v["version_number"], "filename": f["filename"], "url": f["url"],
        "bytes": len(b), "digests": hashes, "native": meta, "nested_jars": nested,
        "published_game_versions": v["game_versions"], "published_loaders": v["loaders"],
    }
    print(json.dumps(output, indent=2))
    if nested or "furniture" not in meta.get("mod_ids", []):
        raise ValueError("Furniture not unbundled or native mod ID differs")
    return 0

if __name__ == "__main__":
    raise SystemExit(main())
