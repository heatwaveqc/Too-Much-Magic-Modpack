#!/usr/bin/env python3
"""Stage pinned 1.21.1 NeoForge JER + Alex's Caves Continued + CodxLib.

Opt-in only: python3 scripts/stage_next_qa_alex_jer.py --write
Runs on a 'stage/' branch, never touches or promotes test/full-integration.
Downloads metadata from official Modrinth API, never downloads/commits mod JARs.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import re
import subprocess
import tomllib
import urllib.parse
import urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
PACK_PATHS = (
    "kubejs/data/biggerreactorsrework/neoforge/biome_modifier/overworld_ores.json",
    "kubejs/server_scripts/toxic_caves_reactor_uranium.js",
)
SPECS = (
    {
        "id": "alexs-caves-continued",
        "project": "cO2CvXug",
        "version_id": "wSeCJMlI",
        "version": "1.1.2+neoforge-1.21.1",
        "side": "both",
        "category": "worldgen",
        "name": "Alex's Caves Continued",
        "modfile": "mods/alexs-caves-continued.pw.toml",
    },
    {
        "id": "codxlib",
        "project": "6oyMM4yX",
        "version_id": "FW5ZWCXm",
        "version": "1.5.1+neoforge-1.21.1",
        "side": "both",
        "category": "dependency",
        "name": "CodxLib",
        "modfile": "mods/codxlib.pw.toml",
    },
    {
        "id": "just-enough-resources-jer",
        "project": "uEfK2CXF",
        "version_id": "A2G7NFcD",
        "version": "1.6.0.57",
        "side": "client",
        "category": "qol",
        "name": "Just Enough Resources (JER)",
        "modfile": "mods/just-enough-resources-jer.pw.toml",
    },
)


def remote_version(version_id: str) -> dict:
    url = "https://api.modrinth.com/v2/version/" + urllib.parse.quote(version_id, safe="")
    request = urllib.request.Request(
        url, headers={"User-Agent": "Too-Much-Magic-Packwiz-Stage/1.0"}
    )
    with urllib.request.urlopen(request, timeout=35) as response:
        return json.load(response)


def verify_response(spec: dict, obj: dict) -> dict:
    assert obj["id"] == spec["version_id"], (spec["id"], "version ID changed")
    assert obj["project_id"] == spec["project"], (spec["id"], "project mismatch")
    assert "1.21.1" in obj["game_versions"], (spec["id"], "wrong MC version")
    assert "neoforge" in obj["loaders"], (spec["id"], "wrong loader")
    assert str(obj["version_number"]) == spec["version"], (spec["id"], "version drift")
    files = [f for f in obj["files"] if f.get("primary")] or obj["files"]
    if len(files) != 1:
        raise ValueError("Ambiguous primary download for " + spec["id"])
    file = files[0]
    if not file["filename"].endswith(".jar"):
        raise ValueError("Not a JAR: " + file["filename"])
    parsed = urllib.parse.urlparse(file["url"])
    if parsed.scheme != "https" or parsed.hostname != "cdn.modrinth.com":
        raise ValueError("Download not hosted by Modrinth CDN: " + file["url"])
    sha512 = file["hashes"]["sha512"]
    if not re.fullmatch("[a-f0-9]{128}", sha512):
        raise ValueError("Malformed SHA512: " + spec["id"])
    return file


def pin(spec: dict, file: dict) -> str:
    return (
        f'name = "{spec["name"]}"\n'
        f'filename = "{file["filename"]}"\n'
        f'side = "{spec["side"]}"\n\n'
        "[download]\n"
        f'url = "{file["url"]}"\n'
        'hash-format = "sha512"\n'
        f'hash = "{file["hashes"]["sha512"]}"\n\n'
        "[update]\n[update.modrinth]\n"
        f'mod-id = "{spec["project"]}"\n'
        f'version = "{spec["version_id"]}"\n'
    )


def packwiz_refresh() -> None:
    """Refresh indexed files only; do not import any other untracked pack files."""
    idx_path = ROOT / "index.toml"
    idx = tomllib.loads(idx_path.read_text(encoding="utf-8"))
    if idx.get("hash-format") != "sha256":
        raise ValueError("Unexpected packwiz index hash format")
    entries = {e["file"]: dict(e) for e in idx["files"]}
    for path in (*PACK_PATHS, *(spec["modfile"] for spec in SPECS)):
        file = ROOT / path
        if not file.is_file():
            raise ValueError("Missing staged pack file: " + path)
        item = {"file": path, "hash": hashlib.sha256(file.read_bytes()).hexdigest()}
        if path.startswith("mods/"):
            item["metafile"] = True
        entries[path] = item
    parts = ['hash-format = "sha256"', ""]
    for path in sorted(entries):
        entry = entries[path]
        parts.extend(["[[files]]", f'file = "{path}"', f'hash = "{entry["hash"]}"'])
        if entry.get("metafile"):
            parts.append("metafile = true")
        parts.append("")
    idx_path.write_text("\n".join(parts), encoding="utf-8")
    pack = ROOT / "pack.toml"
    original = pack.read_text(encoding="utf-8")
    replacement = hashlib.sha256(idx_path.read_bytes()).hexdigest()
    new, number = re.subn(
        r'(?m)^hash = "[a-f0-9]{64}"$',
        'hash = "' + replacement + '"',
        original
    )
    if number != 1:
        raise ValueError("Expected exactly one root index pointer")
    pack.write_text(new, encoding="utf-8")


def update_roster() -> None:
    path = ROOT / "manifest/mods.json"
    data = json.loads(path.read_text(encoding="utf-8"))
    existing = {x["id"]: x for x in data["mods"]}
    for spec in SPECS:
        if spec["id"] in existing and existing[spec["id"]]["enabled_in_pack"]:
            raise ValueError("Already installed; cannot silently overwrite " + spec["id"])
        existing[spec["id"]] = {
            "id": spec["id"], "name": spec["name"],
            "category": spec["category"], "desired": True,
            "version": spec["version"], "release_status": "verified_release",
            "source": f'https://modrinth.com/mod/{spec["project"]}/version/{spec["version_id"]}',
            "enabled_in_pack": True,
            "dependencies_verified": spec["id"] != "alexs-caves-continued",
            "packwiz_slug": spec["modfile"].removeprefix("mods/").removesuffix(".pw.toml"),
            "note": "Staged next-build candidate only; not in the rolling test/full-integration runtime. "
                    "Pin exact 1.21.1 NeoForge file. Requires client/worldgen QA.",
        }
    data["mods"] = sorted(existing.values(), key=lambda x: x["id"])
    path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--write", action="store_true", help="write pins to isolated staging branch")
    args = parser.parse_args()
    if not args.write:
        raise SystemExit("This script changes pins: rerun with --write on stage/ branch")
    current = subprocess.check_output(["git", "branch", "--show-current"], cwd=ROOT, text=True).strip()
    if not current.startswith("stage/"):
        raise ValueError("Refusing to write outside stage/ branch: " + current)
    versions = {}
    for spec in SPECS:
        metadata = remote_version(spec["version_id"])
        versions[spec["id"]] = metadata
        file = verify_response(spec, metadata)
        out = ROOT / spec["modfile"]
        out.parent.mkdir(parents=True, exist_ok=True)
        out.write_text(pin(spec, file), encoding="utf-8")
        print(f'PIN {spec["id"]}: {file["filename"]} SHA512 {file["hashes"]["sha512"][:16]}...')
    # Ensure Alex's Caves declares CodxLib, or stop to re-audit newer requirements.
    deps = versions["alexs-caves-continued"].get("dependencies", [])
    required = [d for d in deps if d.get("dependency_type") == "required"]
    if not any(d.get("project_id") == "6oyMM4yX" for d in required):
        raise ValueError("Alex's Caves CodxLib requirement changed; inspect upstream dependencies")
    update_roster()
    packwiz_refresh()
    print("PASS: exact official Modrinth SHA512 pins, gated recipe, packwiz hash chain staged.")
    print("NOT DEPLOYED: test/full-integration deliberately left unchanged.")


if __name__ == "__main__":
    main()
