#!/usr/bin/env python3
"""Validate and optionally stage the four Tyz Core Magic skill prototypes.

This does not install Tyz, edit packwiz, or claim any in-game success.
Usage:
  python3 compat/magic/stage_preview.py --check
  python3 compat/magic/stage_preview.py --pack-root /path/to/pack --install
After installing into a *packwiz source tree*, run "packwiz refresh" and
validate the index. Use an ephemeral test pack/world, never a live save.
"""
from __future__ import annotations

import argparse
import json
import shutil
from pathlib import Path

ROOT = Path(__file__).resolve().parent
OVERLAY = ROOT / "tyzs-test-overlay"
CORE_IDS = {
    "tmm_magic_reservoir_1",
    "tmm_magic_wellspring_1",
    "tmm_magic_thrift_1",
    "tmm_magic_potency_1",
}
DEFAULT_CATEGORIES = {"abilities", "combat", "misc", "nature"}
VALID_OPERATIONS = {"ADD_VALUE", "ADD_MULTIPLIED_BASE", "ADD_MULTIPLIED_TOTAL"}


def verify() -> list[Path]:
    """Fail fast on malformed Tyz JSON/config before copying anything."""
    skill_dir = OVERLAY / "config/tyzs_skills/custom/skills"
    actual = {p.stem for p in skill_dir.glob("*.json")}
    if actual != CORE_IDS:
        raise ValueError(f"Expected {sorted(CORE_IDS)}, found {sorted(actual)}")
    targets = set()
    for name in sorted(CORE_IDS):
        item = json.loads((skill_dir / (name + ".json")).read_text(encoding="utf-8"))
        if item["id"] != name or item["type"] != "CUSTOM":
            raise ValueError(f"{name}: wrong ID or CUSTOM type")
        if item["category"] != "tmm_magic_core" or not item["active"]:
            raise ValueError(f"{name}: wrong category or inactive")
        levels = item["maximumLevel"]
        if levels != 5 or len(item["prices"]) != levels:
            raise ValueError(f"{name}: expected five ranks and five prices")
        if any(type(n) is not int or n <= 0 for n in item["prices"]):
            raise ValueError(f"{name}: invalid prices")
        if item["prices"] != [1, 1, 1, 1, 1]:
            raise ValueError(f"{name}: base five-rank nodes must cost exactly 1 SP each")
        if not item["modifiers"]:
            raise ValueError(f"{name}: no modifiers")
        for mod in item["modifiers"]:
            attr = mod["attribute"]
            if attr in targets:
                raise ValueError(f"Attribute {attr} is modified by two core skills")
            targets.add(attr)
            if mod["operation"] not in VALID_OPERATIONS:
                raise ValueError(f"{name}: invalid operation for {attr}")
            vals = mod["values"]
            if len(vals) != levels or any(not isinstance(n, (int, float)) for n in vals):
                raise ValueError(f"{name}: malformed values for {attr}")
            if vals[0] <= 0 or any(b <= a for a, b in zip(vals, vals[1:])):
                raise ValueError(f"{name}: nonincreasing upgrade values for {attr}")
    categories = json.loads(
        (OVERLAY / "config/tyzs_skills/custom/data/categories.json").read_text(encoding="utf-8")
    )
    if not (DEFAULT_CATEGORIES | {"tmm_magic_core"}).issubset(categories):
        raise ValueError("Category override would remove Tyz's existing categories")
    levels = json.loads(
        (OVERLAY / "config/tyzs_skills/custom/data/level-pool.json").read_text(encoding="utf-8")
    )
    if levels != {"-1": {"goal": 1000, "reward": 1}}:
        raise ValueError("MXP level pool must always cost 1000 and grant exactly 1 SP")
    for name in ("config/tyzs_skills-common.toml", "kubejs/server_scripts/tmm_magic_core_thrift.js"):
        if not (OVERLAY / name).is_file():
            raise ValueError(f"Missing {name}")
    return sorted(p for p in OVERLAY.rglob("*") if p.is_file())


def install(pack_root: Path, paths: list[Path], overwrite: bool) -> None:
    pack_root = pack_root.resolve()
    if not pack_root.is_dir():
        raise ValueError(f"Pack source folder missing: {pack_root}")
    if not (pack_root / "index.toml").is_file():
        raise ValueError("Use a packwiz source folder containing index.toml")

    planned = []
    for src in paths:
        relative = src.relative_to(OVERLAY)
        dest = pack_root / relative
        if dest.exists() and not overwrite and relative.as_posix() != (
            "config/tyzs_skills/custom/data/categories.json"
        ):
            raise FileExistsError(f"Refusing to overwrite existing file {dest}; use --overwrite")
        planned.append((src, dest, relative))

    # Preserve preexisting categories even when Tyz/other addons add new tabs.
    target_categories = pack_root / "config/tyzs_skills/custom/data/categories.json"
    if target_categories.exists():
        current = json.loads(target_categories.read_text(encoding="utf-8"))
        desired = json.loads(
            (OVERLAY / "config/tyzs_skills/custom/data/categories.json").read_text(encoding="utf-8")
        )
        current.update({k: v for k, v in desired.items() if k == "tmm_magic_core"})
        merged = json.dumps(current, indent=2) + "\n"
    else:
        merged = None

    for src, dest, relative in planned:
        dest.parent.mkdir(parents=True, exist_ok=True)
        if relative.as_posix() == "config/tyzs_skills/custom/data/categories.json" and merged:
            dest.write_text(merged, encoding="utf-8")
        else:
            shutil.copyfile(src, dest)
        print("staged:", relative.as_posix())
    print("Staged preview only. Run packwiz refresh, then manifest/index validation and boot tests.")


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--check", action="store_true", help="Validate preview files without copying")
    parser.add_argument("--install", action="store_true", help="Copy preview files into --pack-root")
    parser.add_argument("--pack-root", type=Path, help="Packwiz checkout path")
    parser.add_argument("--overwrite", action="store_true", help="Permit replacing previous TMM preview files")
    args = parser.parse_args()
    files = verify()
    print(f"PASS: {len(CORE_IDS)} skills, five ranks each, {len(files)} overlay files")
    if args.install:
        if args.pack_root is None:
            parser.error("--install requires --pack-root")
        install(args.pack_root, files, args.overwrite)
