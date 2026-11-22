#!/usr/bin/env python3
"""Offline checks of the *packwiz metadata*, not a game boot or mod resolution."""
from __future__ import annotations

import hashlib
import json
import sys
import tomllib
from pathlib import Path, PurePosixPath

ROOT = Path(__file__).resolve().parents[1]


def check(ok: bool, explanation: str) -> None:
    if not ok:
        raise ValueError(explanation)


def sha256(path: Path) -> str:
    return hashlib.sha256(path.read_bytes()).hexdigest()


def main() -> None:
    pack = tomllib.loads((ROOT / "pack.toml").read_text(encoding="utf-8"))
    check(pack.get("pack-format") == "packwiz:1.1.0", "Unexpected packwiz format")
    versions = pack["versions"]
    check(versions.get("minecraft") == "1.21.1", "Wrong MC version")
    check(versions.get("neoforge") == "21.1.265", "Unexpected loader (review first)")
    check(pack["index"]["hash-format"] == "sha256", "Index must use sha256")
    index_path = ROOT / pack["index"]["file"]
    check(index_path.is_file(), "Missing index.toml")
    check(sha256(index_path) == pack["index"]["hash"], "Stale pack.toml index hash")
    index = tomllib.loads(index_path.read_text(encoding="utf-8"))
    check(index.get("hash-format") == "sha256", "Unexpected index hash algorithm")

    manifest = json.loads((ROOT / "manifest/mods.json").read_text(encoding="utf-8"))
    check(manifest["target"]["minecraft"] == "1.21.1", "Manifest MC mismatch")
    check(manifest["target"]["loader"] == "neoforge", "Manifest loader mismatch")
    check(manifest["target"]["loader_version"] == versions["neoforge"], "Loader mismatch")
    allowed = set(manifest["status_semantics"])
    mods = manifest["mods"]
    check(len({m["id"] for m in mods}) == len(mods), "Duplicate mod IDs")
    check(all(m["release_status"] in allowed for m in mods), "Unknown release state")
    excludes = {m["id"].lower() for m in manifest["excluded"]}
    check("create" in excludes and "ftb-ultimine" in excludes, "Missing hard exclusions")
    check({"extended-crafting", "re-avaritia"}.issubset(excludes), "Missing owner-locked ultimate crafting exclusions")
    check("Ring of the Seven Deadly Sins" in manifest["items_blacklist"], "Missing ring ban")
    check(not any(m["id"].lower() in excludes for m in mods), "Excluded mod included")

    indexed: set[str] = set()
    active: set[str] = set()
    for entry in index.get("files", []):
        name = entry["file"]
        p = PurePosixPath(name)
        check(not p.is_absolute() and ".." not in p.parts, f"Unsafe path {name}")
        check(name not in indexed, f"Duplicate index path {name}")
        indexed.add(name)
        target = (ROOT / name).resolve()
        check(target.is_relative_to(ROOT), f"Escaped pack root: {name}")
        check(target.is_file(), f"Missing indexed file {name}")
        check(entry["hash"] == sha256(target), f"Stale hash for {name}")
        if entry.get("metafile"):
            check(name.endswith(".pw.toml") and (name.startswith("mods/") or name.startswith("resourcepacks/")), f"Unexpected metafile: {name}")
            mod = tomllib.loads(target.read_text(encoding="utf-8"))
            check(mod.get("side", "both") in {"both", "client", "server"}, f"Invalid side: {name}")
            check(bool(mod.get("download", {}).get("hash")), f"Missing download hash {name}")
            if name.startswith("mods/"):
                active.add(Path(name).name.removesuffix(".pw.toml"))
            else:
                check(mod.get("side") == "client", f"Resource pack must be client-only: {name}")
                check(mod.get("filename", "").endswith(".zip"), f"Resource pack must be a ZIP: {name}")
    check(not active & excludes, "Excluded mod present in packwiz")
    catalog_active = {m.get("packwiz_slug", m["id"]) for m in mods if m["enabled_in_pack"]}
    check(len(catalog_active) == sum(m["enabled_in_pack"] for m in mods), "Duplicate packwiz slugs")
    check(active == catalog_active, f"Active mismatch: packwiz={sorted(active)} manifest={sorted(catalog_active)}")
    if "default-options" in active:
        from validate_defaultoptions_keybindings import validate as validate_default_keybindings
        validate_default_keybindings()
    if "curios" in active:
        from validate_curios_bundle_slot import main as validate_curios_bundle_slot
        validate_curios_bundle_slot()
    print(f"PASS: packwiz metadata + hashes; {len(indexed)} files, {len(active)} active mods; {len(mods)} desired entries")
    print("NOT A BOOT TEST: no downloadable mod, dependency, client or dedicated-server validation performed")


if __name__ == "__main__":
    try:
        main()
    except (ValueError, KeyError, OSError, json.JSONDecodeError, tomllib.TOMLDecodeError) as exc:
        print(f"FAIL: {exc}", file=sys.stderr)
        sys.exit(1)
