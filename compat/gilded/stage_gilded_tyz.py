#!/usr/bin/env python3
"""Merge the Gilded Tyz skill into an already staged Tyz 6.4.2 pack.

Never overwrite the existing Core Magic/Monk categories or skill files.
This is a manual/explicit staging utility; no server smoke or pack build.
"""
from __future__ import annotations

import argparse
import json
import os
import tempfile
from pathlib import Path

ROOT = Path(__file__).resolve().parent
OVERLAY = ROOT / "tyzs-overlay"
SKILL = OVERLAY / "config/tyzs_skills/custom/skills/tmm_gilded_fortune_mastery.json"
CATEGORY_PATCH = OVERLAY / "category-patch.json"


def read_json(path: Path) -> dict:
    value = json.loads(path.read_text(encoding="utf-8"))
    if not isinstance(value, dict):
        raise ValueError(f"Expected object in {path}")
    return value


def verify_source() -> tuple[dict, dict]:
    skill, patch = read_json(SKILL), read_json(CATEGORY_PATCH)
    if skill.get("id") != "tmm_gilded_fortune_mastery" \
            or skill.get("type") != "IMMUTABLE" \
            or skill.get("category") != "tmm_gilded" \
            or skill.get("maximumLevel") != 7 \
            or skill.get("prices") != [1] * 7 \
            or skill.get("modifiers") != []:
        raise ValueError("Gilded Tyz skill contract mismatch")
    power = skill.get("customValues", {}).get("power", {}).get("values", [])
    if power != [4, 5, 6, 7, 8, 9, 10]:
        raise ValueError("Tyz Fortune mastery values must represent +4 to +10")
    if list(patch) != ["tmm_gilded"] or patch["tmm_gilded"].get("order") != 7:
        raise ValueError("Expected a single category overlay, not a full replacement")
    return skill, patch


def atomic_text(path: Path, content: str) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    fd, temporary = tempfile.mkstemp(prefix="gilded-", suffix=".tmp", dir=path.parent)
    try:
        with os.fdopen(fd, "w", encoding="utf-8") as out:
            out.write(content)
        os.replace(temporary, path)
    finally:
        if os.path.exists(temporary):
            os.unlink(temporary)


def stage(pack_root: Path) -> None:
    skill, patch = verify_source()
    pack_root = pack_root.resolve()
    tyz_pin = pack_root / "mods/tyzs-skills.pw.toml"
    categories = pack_root / "config/tyzs_skills/custom/data/categories.json"
    output = pack_root / "config/tyzs_skills/custom/skills/tmm_gilded_fortune_mastery.json"
    if not tyz_pin.is_file():
        raise ValueError("Tyz 6.4.2 must be pinned before Gilded Tyz staging")
    if not categories.is_file():
        raise ValueError("Missing current Tyz categories: stage the primary Tyz+Monk overlay first")
    existing = read_json(categories)
    if not {"abilities", "combat", "misc", "nature", "tmm_magic_core", "tmm_monk"} <= existing.keys():
        raise ValueError("Incomplete Tyz base or Monk categories: refuse replacement")
    for name, definition in patch.items():
        if name in existing and existing[name] != definition:
            raise ValueError(f"Conflicting category {name}: preserve owner changes")
        existing[name] = definition
    if output.is_file() and read_json(output) != skill:
        raise ValueError("Existing Gilded Fortune skill differs; reconcile before staging")
    atomic_text(categories, json.dumps(existing, indent=2) + "\n")
    atomic_text(output, json.dumps(skill, indent=2) + "\n")
    print(f"Staged tmm_gilded category and seven-rank Fortune skill in {pack_root}")


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--check", action="store_true", help="Verify overlay source only")
    parser.add_argument("--install", action="store_true", help="Merge into an already staged Tyz pack")
    parser.add_argument("--pack-root", type=Path, help="Pack checkout with pinned Tyz and categories")
    args = parser.parse_args()
    verify_source()
    if args.install:
        if not args.pack_root:
            parser.error("--install requires --pack-root")
        stage(args.pack_root)
    elif not args.check:
        parser.error("Select --check or --install; staging is never automatic")
    else:
        print("Gilded Tyz overlay verified (no pack changed)")
