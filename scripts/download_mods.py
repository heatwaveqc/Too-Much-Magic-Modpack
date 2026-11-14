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
from urllib.parse import quote, urlparse

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
        urls = [url] if url else []
        if not urls and info.get("mode")=="metadata:curseforge":
            cf=mod.get("update",{}).get("curseforge",{})
            file_id=cf.get("file-id")
            if not isinstance(file_id,int):
                raise ValueError(f"CurseForge metadata missing file ID for {filename}")
            remote_path=f"/files/{file_id//1000}/{file_id%1000:03d}/{quote(filename)}"
            urls=[host+remote_path for host in ("https://mediafilez.forgecdn.net","https://edge.forgecdn.net")]
        if not urls:
            raise ValueError(f"Missing approved download metadata for {filename}")
        algorithm = info["hash-format"]
        if algorithm not in {"sha512", "sha256", "sha1"}:
            raise ValueError(f"Unsupported hash algorithm {algorithm}")
        target = out_dir / filename
        obtained = None
        last_error = None
        for url in urls:
            parsed=urlparse(url)
            if parsed.scheme != "https" or parsed.hostname not in {"cdn.modrinth.com","edge.forgecdn.net","mediafilez.forgecdn.net"}:
                raise ValueError(f"Unreviewed artifact origin for {filename}: {parsed.hostname}")
            for attempt in range(2):
                try:
                    req=urllib.request.Request(url,headers={"User-Agent":"TooMuchMagic-CI/0.2"})
                    with urllib.request.urlopen(req,timeout=90) as response:
                        data=response.read()
                    if getattr(hashlib,algorithm)(data).hexdigest().lower()!=info["hash"].lower():
                        raise ValueError(f"Hash mismatch for {filename}; will not install")
                    obtained=data
                    break
                except (OSError,TimeoutError) as exc:
                    last_error=exc
                    time.sleep(1+attempt)
            if obtained is not None:
                break
        if obtained is None:
            raise ValueError(f"Unable to retrieve hash-verified {filename}: {last_error}")
        target.write_bytes(obtained)
        data=obtained
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
