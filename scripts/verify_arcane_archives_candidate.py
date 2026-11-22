#!/usr/bin/env python3
"""Static verification for the selected Arcane Archives next-QA pin.

This does not download the artifact, assemble the pack, or start Minecraft.
"""

from pathlib import Path
import re
import tomllib
import json
import hashlib


ROOT = Path(__file__).resolve().parents[1]
PIN = ROOT / "mods" / "arcane-archives-reborn.pw.toml"
MODONOMICON_PIN = ROOT / "mods" / "modonomicon.pw.toml"


def load_toml(path: Path) -> dict:
    with path.open("rb") as handle:
        return tomllib.load(handle)


arcane = load_toml(PIN)
assert arcane["name"] == "Arcane Archives: Reborn"
assert arcane["filename"] == "ArcaneArchives-1.0.2+Neoforge-1.21.1.jar"
assert arcane["side"] == "both"
assert arcane["download"] == {
    "hash-format": "sha1",
    "hash": "082f5f560981c04c487b9c1f5e5ec9976f8ec2db",
    "mode": "metadata:curseforge",
}
assert arcane["update"]["curseforge"] == {
    "file-id": 8985321,
    "project-id": 1696190,
}

index = load_toml(ROOT / "index.toml")
indexed_paths = {entry["file"] for entry in index["files"]}
assert "mods/arcane-archives-reborn.pw.toml" in indexed_paths, "Approved Arcane Archives pin must be indexed"
assert next(row for row in index["files"] if row["file"] == "mods/arcane-archives-reborn.pw.toml")["hash"] == hashlib.sha256(PIN.read_bytes()).hexdigest()
manifest = json.loads((ROOT / "manifest/mods.json").read_text(encoding="utf-8"))
assert any(row["id"] == "arcane-archives-reborn" and row["enabled_in_pack"] for row in manifest["mods"])
required = json.loads((ROOT / "manifest/next-qa-target.json").read_text(encoding="utf-8"))["required_mod_ids"]
assert "arcane-archives-reborn" in required
assert not (ROOT / arcane["filename"]).exists(), "third-party JAR must not be committed"

modonomicon = load_toml(MODONOMICON_PIN)
match = re.fullmatch(
    r"modonomicon-1\.21\.1-neoforge-(\d+)\.(\d+)\.(\d+)\.jar",
    modonomicon["filename"],
)
assert match, "cannot establish the installed Modonomicon version"
assert tuple(map(int, match.groups())) >= (1, 120, 5), (
    "Arcane Archives requires Modonomicon 1.120.5 or newer"
)

pack = load_toml(ROOT / "pack.toml")
neoforge = tuple(map(int, pack["versions"]["neoforge"].split(".")))
assert neoforge >= (21, 1, 251), "Arcane Archives requires NeoForge 21.1.251 or newer"
assert pack["versions"]["minecraft"] == "1.21.1"

print(
    "PASS: exact Arcane Archives 1.0.2 candidate metadata; "
    f"Modonomicon {'.'.join(match.groups())} and NeoForge {pack['versions']['neoforge']} "
    "satisfy declared minimums; pin is indexed for next QA (not gameplay accepted)"
)
