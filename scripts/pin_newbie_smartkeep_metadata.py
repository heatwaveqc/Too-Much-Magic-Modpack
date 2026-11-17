#!/usr/bin/env python3
"""One-shot exact hash pin of owner-selected Smart Keep Inventory 1.21.1 NeoForge.

Runs only on isolated PR; makes NO modpack assembly, server boot or Prism ZIP.
JAR is not downloaded, redistributed, committed, or executed.
"""
from __future__ import annotations

import hashlib
import json
import re
import tomllib
import urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
VERSION_ID = "JqVOLkFB"
PROJECT_ID = "qeGAjqzW"
EXPECTED_FILENAME = "smart_keep_inventory-1.21.1-1.0.1-neoforge.jar"
PINFILENAME = "mods/smart-keep-inventory.pw.toml"
KUBEJS = (
    "kubejs/data/smart_keep_inventory/smart_keep_inventory/condition/bosses.json",
    "kubejs/data/tmm/smart_keep_inventory/condition/newbie.json",
)

def pin_from_official_provider() -> str:
    request = urllib.request.Request(
        "https://api.modrinth.com/v2/version/" + VERSION_ID,
        headers={"User-Agent": "TMM-exact-1.21.1-SmartKeep-Pin/2026-11-16"},
    )
    with urllib.request.urlopen(request, timeout=45) as response:
        version = json.load(response)
    if version.get("project_id") != PROJECT_ID or VERSION_ID != version["id"]:
        raise RuntimeError("Unexpected Modrinth project/version ID")
    if "1.21.1" not in version.get("game_versions", []):
        raise RuntimeError("Version not for Minecraft 1.21.1")
    if "neoforge" not in [str(v).lower() for v in version.get("loaders", [])]:
        raise RuntimeError("Version is not NeoForge")
    if version["version_number"] != "1.21.1-1.0.1-neoforge":
        raise RuntimeError("Version number does not match approved 1.0.1")
    jars = [f for f in version["files"] if f["filename"] == EXPECTED_FILENAME]
    if len(jars) != 1:
        raise RuntimeError("Expected exactly one official 1.21.1 NeoForge jar")
    file = jars[0]
    digest = file.get("hashes", {}).get("sha512", "")
    if not re.fullmatch("[a-f0-9]{128}", digest):
        raise RuntimeError("Official SHA512 hash missing")
    if not file["url"].startswith("https://cdn.modrinth.com/data/" + PROJECT_ID + "/versions/"):
        raise RuntimeError("Unexpected or nonofficial download URL")
    required = [d for d in version.get("dependencies", [])
                if d.get("dependency_type") == "required"]
    if required:
        raise RuntimeError("New required transitive dependencies: " + repr(required))
    meta = (
        'name = "Smart Keep Inventory"\n'
        'filename = "' + EXPECTED_FILENAME + '"\n'
        'side = "both"\n\n'
        '[download]\n'
        'url = ' + json.dumps(file["url"]) + '\n'
        'hash-format = "sha512"\n'
        'hash = "' + digest + '"\n\n'
        '[update]\n[update.modrinth]\n'
        'mod-id = "' + PROJECT_ID + '"\n'
        'version = "' + VERSION_ID + '"\n'
    )
    tomllib.loads(meta)
    file_path = ROOT / PINFILENAME
    if file_path.exists():
        raise RuntimeError("Pin exists; do not overwrite a manually-reviewed file")
    file_path.write_text(meta, encoding="utf-8")
    return digest

def update_manifest() -> None:
    path = ROOT / "manifest/mods.json"
    roster = json.loads(path.read_text(encoding="utf-8"))
    existing = [m for m in roster["mods"] if m["id"] == "smart-keep-inventory"]
    if len(existing) != 1 or not existing[0]["desired"]:
        raise RuntimeError("Required owner-approved Smart Keep roster item missing/duplicate")
    item = existing[0]
    if item["enabled_in_pack"]:
        raise RuntimeError("Already marked installed; reject unreviewed overwrite")
    item["enabled_in_pack"] = True
    item["dependencies_verified"] = True
    item["packwiz_slug"] = "smart-keep-inventory"
    item["note"] += " Source/Packwiz metadata pinned to official SHA512 1.0.1 NeoForge; Corail grave, Curios and XP gameplay NOT TESTED."
    path.write_text(json.dumps(roster, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")

def update_index_and_root() -> None:
    index_path, pack_path = ROOT / "index.toml", ROOT / "pack.toml"
    original = index_path.read_text(encoding="utf-8")
    pieces = original.split("[[files]]\n")
    header, blocks = pieces[0], ["[[files]]\n" + p for p in pieces[1:]]
    registered = set()
    for block in blocks:
        m = re.search(r'^file = "([^"]+)"$', block, flags=re.MULTILINE)
        if not m:
            raise RuntimeError("Malformed existing index entry")
        registered.add(m.group(1))
    for path in (PINFILENAME, *KUBEJS):
        if path in registered:
            raise RuntimeError("Duplicate path in live Packwiz index: " + path)
        p = ROOT / path
        if not p.is_file():
            raise RuntimeError("Missing expected authored source " + path)
        content = p.read_bytes()
        digest = hashlib.sha256(content).hexdigest()
        block = f'[[files]]\nfile = "{path}"\nhash = "{digest}"\n'
        if path.endswith(".pw.toml"):
            block += "metafile = true\n"
        block += "\n"
        at = next((i for i, section in enumerate(blocks)
                   if (m := re.search(r'^file = "([^"]+)"$', section, flags=re.MULTILINE))
                   and m.group(1).startswith(path.split("/")[0] + "/")
                   and m.group(1) > path), len(blocks))
        blocks.insert(at, block)
    revised = (header + "".join(blocks)).rstrip() + "\n"
    old = hashlib.sha256(original.encode("utf-8")).hexdigest()
    pack = pack_path.read_text(encoding="utf-8")
    if pack.count(old) != 1:
        raise RuntimeError("Existing Packwiz index SHA256 stale before adding Smart Keep")
    index_path.write_text(revised, encoding="utf-8")
    pack_path.write_text(pack.replace(old, hashlib.sha256(revised.encode("utf-8")).hexdigest()), encoding="utf-8")

def main() -> None:
    for path in KUBEJS:
        json.loads((ROOT / path).read_text(encoding="utf-8"))
    official_sha = pin_from_official_provider()
    update_manifest()
    update_index_and_root()
    print("PASS: Smart Keep Inventory exact Modrinth 1.21.1 NeoForge 1.0.1 pinned")
    print("Official JAR SHA512:", official_sha)
    print("NO server/client boot, JAR redistribution or Corail gameplay acceptance")

if __name__ == "__main__":
    main()
