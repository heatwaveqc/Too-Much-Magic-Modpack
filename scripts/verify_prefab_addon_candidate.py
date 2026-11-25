#!/usr/bin/env python3
"""Verify the selected Prefab Custom Addon JAR from its official Modrinth CDN.

Read-only lightweight provider audit. No Minecraft boot, no persistent JAR,
no packwiz index write, no gameplay test.
"""
import hashlib
import io
import pathlib
import tomllib
import urllib.parse
import urllib.request
import zipfile

ROOT = pathlib.Path(__file__).resolve().parents[1]
META = ROOT / "mods" / "prefab-custom-addon.pw.toml"
LIMIT = 16 * 1024 * 1024


def main():
    item = tomllib.loads(META.read_text(encoding="utf-8"))
    download = item["download"]
    url = download["url"]
    parts = urllib.parse.urlparse(url)
    if parts.scheme != "https" or parts.hostname != "cdn.modrinth.com":
        raise ValueError("Refusing non-Modrinth artifact URL")
    if download["hash-format"] != "sha512":
        raise ValueError("Expected a pinned SHA512")
    expected = download["hash"]
    if len(expected) != 128:
        raise ValueError("Invalid SHA512 length")
    req = urllib.request.Request(url, headers={"User-Agent": "TMM-source-pin-audit/1.0"})
    with urllib.request.urlopen(req, timeout=45) as response:
        data = response.read(LIMIT + 1)
    if len(data) > LIMIT:
        raise ValueError("Oversize addon JAR")
    digest = hashlib.sha512(data).hexdigest()
    if digest != expected:
        raise ValueError(f"Official JAR SHA512 MISMATCH: actual={digest} expected={expected}")
    with zipfile.ZipFile(io.BytesIO(data)) as zf:
        names = set(zf.namelist())
        candidates = ["META-INF/neoforge.mods.toml", "META-INF/mods.toml"]
        meta = next((p for p in candidates if p in names), None)
        if meta is None:
            raise ValueError("Missing NeoForge/Forge mod metadata in downloaded JAR")
        native = tomllib.loads(zf.read(meta).decode("utf-8"))
    mods = native.get("mods", [])
    ids = [m.get("modId") for m in mods]
    if "prefab_custom_addon" not in ids:
        raise ValueError(f"Wrong mod in official JAR: {ids}")
    deps = native.get("dependencies", {}).get("prefab_custom_addon", [])
    print(f"PASS: official Prefab Custom Addon {item['filename']}")
    print(f"publisher_sha512={digest}")
    print(f"size={len(data)} bytes; native_mod_ids={ids}")
    print(f"native_dependency_entries={deps}")
    print("SCOPE: hash/dependency source check only; not indexed or boot-tested")


if __name__ == "__main__":
    main()
