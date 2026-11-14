#!/usr/bin/env python3
"""Download all packwiz NeoForge server mods and verify remote jar hashes."""
from __future__ import annotations
import hashlib
import os
import pathlib
import sys
import time
import tomllib
import urllib.request
from urllib.parse import urlparse

ROOT = pathlib.Path(__file__).resolve().parents[1]


def download(out_dir: pathlib.Path) -> int:
    index = tomllib.loads((ROOT / "index.toml").read_text())
    out_dir.mkdir(parents=True, exist_ok=True)
    count = 0
    for item in index.get("files", []):
        if not item.get("metafile"):
            continue
        mod = tomllib.loads((ROOT / item["file"]).read_text())
        if mod.get("side", "both") == "client":
            continue
        filename = mod["filename"]
        if pathlib.PurePosixPath(filename).name != filename or not filename.endswith(".jar"):
            raise ValueError(f"Unsafe filename: {filename}")
        info = mod["download"]
        url = info.get("url")
        if not url or urlparse(url).scheme != "https":
            raise ValueError(f"Missing HTTPS download for {filename}")
        if urlparse(url).hostname not in {"cdn.modrinth.com", "edge.forgecdn.net", "mediafilez.forgecdn.net"}:
            raise ValueError(f"Unreviewed artifact origin for {filename}")
        algorithm = info["hash-format"]
        if algorithm not in {"sha512", "sha256", "sha1"}:
            raise ValueError(f"Unsupported hash algorithm {algorithm}")
        target = out_dir / filename
        for attempt in range(3):
            try:
                req = urllib.request.Request(url, headers={"User-Agent":"TooMuchMagic-CI/0.1"})
                with urllib.request.urlopen(req, timeout=90) as response:
                    data = response.read()
                if getattr(hashlib, algorithm)(data).hexdigest().lower() != info["hash"].lower():
                    raise ValueError(f"Hash mismatch: {filename}")
                target.write_bytes(data)
                break
            except (OSError, TimeoutError) as exc:
                if attempt == 2:
                    raise
                time.sleep(attempt + 2)
        print(f"PASS hash verified: {filename} ({len(data)} bytes)")
        count += 1
    if count == 0:
        raise ValueError("Refusing modded smoke with zero mods")
    print(f"PASS: fetched and hashed {count} server-side mod JARs")
    return count


if __name__ == "__main__":
    if len(sys.argv) != 2:
        sys.exit("Usage: download_mods.py <target-mods-dir>")
    download(pathlib.Path(sys.argv[1]))
