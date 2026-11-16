#!/usr/bin/env python3
"""Hash-pin the owner-selected Smart Keep Inventory target into a disposable packwiz source.

Run ONLY during next integration preparation; this does not boot Minecraft or build Prism.
Requires packwiz, official CurseForge metadata, and the exact target file 7530253.
Do not run on live worlds or shared stable snapshots.
"""
from __future__ import annotations

import json
import pathlib
import re
import subprocess
import tomllib

ROOT = pathlib.Path(__file__).resolve().parents[1]
MODS = ROOT / "manifest/mods.json"
CURSEFORGE_PROJECT = 1424832
CURSEFORGE_FILE = 7530253
FILENAME = "smart_keep_inventory-1.21.1-1.0.1-neoforge.jar"

def run(*args: str) -> None:
    subprocess.run(args, cwd=ROOT, check=True, timeout=240)

def main() -> None:
    source = json.loads(MODS.read_text(encoding="utf-8"))
    candidates = [m for m in source["mods"] if m["id"] == "smart-keep-inventory"]
    if len(candidates) != 1 or not candidates[0]["desired"]:
        raise ValueError("Owner-approved Smart Keep Inventory target missing in manifest")
    if candidates[0]["enabled_in_pack"]:
        raise ValueError("Already marked enabled; verify existing official pin manually")
    if not (ROOT / "index.toml").exists():
        raise ValueError("Packwiz source tree is required")

    expected_predicates = {
        "kubejs/data/tmm/smart_keep_inventory/condition/newbie.json",
        "kubejs/data/smart_keep_inventory/smart_keep_inventory/condition/bosses.json",
    }
    if any(not (ROOT / p).exists() for p in expected_predicates):
        raise ValueError("Missing server-side Newbie and stock-boss predicate sources")
    if not any("tmm_newbie" in (ROOT / p).read_text() for p in expected_predicates):
        raise ValueError("Missing per-player Newbie tag predicate")

    run("packwiz", "--yes", "curseforge", "add",
        "--addon-id", str(CURSEFORGE_PROJECT), "--file-id", str(CURSEFORGE_FILE))
    pins = []
    for path in (ROOT / "mods").glob("*.pw.toml"):
        doc = tomllib.loads(path.read_text(encoding="utf-8"))
        update = doc.get("update", {}).get("curseforge", {})
        if update.get("project-id") == CURSEFORGE_PROJECT:
            pins.append((path, doc))
    if len(pins) != 1:
        raise ValueError("Expected exactly one official Smart Keep Inventory pin")
    pinfile, pin = pins[0]
    if pin.get("filename") != FILENAME or pin.get("side") not in ("both", "server"):
        raise ValueError("Unreviewed Smart Keep Inventory binary/side")
    if pin["update"]["curseforge"]["file-id"] != CURSEFORGE_FILE:
        raise ValueError("Wrong CurseForge file pin")
    integrity = pin.get("download", {})
    digest = integrity.get("hash", "")
    if integrity.get("hash-format") not in {"sha1", "sha256", "sha512"} or not re.fullmatch("[a-f0-9]+", digest):
        raise ValueError("Official hash metadata missing")

    candidates[0]["enabled_in_pack"] = True
    candidates[0]["dependencies_verified"] = True
    candidates[0]["packwiz_slug"] = pinfile.stem.removesuffix(".pw")
    candidates[0]["note"] += "\n2026-11-16 official hash pin resolved. Corail gameplay compatibility UNTESTED; do not mark player PASS."
    MODS.write_text(json.dumps(source, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    run("packwiz", "refresh")
    run("python3", "scripts/validate.py")
    print("STATIC PACKWIZ PIN VERIFIED — CORAIL GAMEPLAY STILL UNTESTED; NO SERVER SMOKE AUTHORIZED")

if __name__ == "__main__":
    main()
