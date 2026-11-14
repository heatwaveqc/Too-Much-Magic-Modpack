#!/usr/bin/env python3
"""Require the approved More Overlays client mod to be directly downloadable."""
from pathlib import Path
import hashlib
import json
import re
import tomllib

root = Path(__file__).resolve().parents[1]
metadata = tomllib.loads((root / "mods/more-overlays-updated.pw.toml").read_text())
roster = json.loads((root / "manifest/mods.json").read_text())
pack = tomllib.loads((root / "pack.toml").read_text())
index = (root / "index.toml").read_bytes()
launcher = (root / "scripts/build_quest_ui_prism.py").read_text()
assert metadata["filename"] == "moreoverlays-1.24.2-mc1.21.1-neoforge.jar"
assert metadata["side"] == "client"
assert metadata["update"]["modrinth"] == {
    "mod-id": "Thy5Pqut", "version": "Kq8xaqKi"}
assert metadata["download"]["url"].startswith(
    "https://cdn.modrinth.com/data/Thy5Pqut/versions/Kq8xaqKi/")
assert metadata["download"]["hash-format"] == "sha512"
assert re.fullmatch("[0-9a-f]{128}", metadata["download"]["hash"])
assert pack["index"]["hash"] == hashlib.sha256(index).hexdigest()
assert 'file = "mods/more-overlays-updated.pw.toml"' in index.decode()
assert any(x["id"] == "more-overlays-updated" and x["enabled_in_pack"]
           for x in roster["mods"])
assert ' -s client {uri}' in launcher, "Prism must install client-side mods"
print("PASS: exact More Overlays Modrinth pin, packwiz index and client-side bootstrap")
print("NOT A CLIENT TEST: check successful download and rendering in Prism")
