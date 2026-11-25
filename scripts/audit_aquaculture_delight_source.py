"""Verify official Modrinth API and exact download for #709; no game boot."""
import hashlib
import io
import json
from pathlib import Path
import tomllib
import urllib.request
import zipfile

ROOT = Path(__file__).resolve().parents[1]
EXPECTED = "111393c07a98f29cab1de0d91965cc1bc6d9d73f"
PROJECT = "U9GJqWrI"
VERSION = "VFpyJiVp"
FILENAME = "aquaculturedelight-1.2.0-neoforge-1.21.1.jar"
URL = "https://cdn.modrinth.com/data/U9GJqWrI/versions/VFpyJiVp/aquaculturedelight-1.2.0-neoforge-1.21.1.jar"

def read(url):
    req = urllib.request.Request(url, headers={"User-Agent": "heatwaveqc/Too-Much-Magic-Modpack (source audit #709)"})
    with urllib.request.urlopen(req, timeout=60) as response:
        return response.read()

pin = tomllib.loads((ROOT / "mods/aquaculture-delight.pw.toml").read_text(encoding="utf-8"))
assert pin["filename"] == FILENAME and pin["side"] == "both"
assert pin["download"] == {"url": URL, "hash-format": "sha1", "hash": EXPECTED}
assert pin["update"]["modrinth"] == {"mod-id": PROJECT, "version": VERSION}
version = json.loads(read("https://api.modrinth.com/v2/version/" + VERSION))
assert version["id"] == VERSION and version["project_id"] == PROJECT
assert version["version_number"] == "1.2.0" and "1.21.1" in version["game_versions"] and "neoforge" in version["loaders"]
files = [f for f in version["files"] if f["filename"] == FILENAME]
assert len(files) == 1 and files[0]["url"] == URL and files[0]["hashes"]["sha1"] == EXPECTED
jar = read(URL)
assert len(jar) == files[0]["size"] and hashlib.sha1(jar).hexdigest() == EXPECTED
assert hashlib.sha512(jar).hexdigest() == files[0]["hashes"]["sha512"]
with zipfile.ZipFile(io.BytesIO(jar)) as z:
    assert z.testzip() is None
    assert "META-INF/neoforge.mods.toml" in z.namelist()
    meta = tomllib.loads(z.read("META-INF/neoforge.mods.toml").decode("utf-8"))
    assert any(m["modId"] == "aquaculturedelight" for m in meta["mods"])
print("PASS: official Modrinth API, exact JAR SHA1/SHA512, ZIP integrity and NeoForge mod ID; identical original pin digest; no game boot")
