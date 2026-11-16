#!/usr/bin/env python3
"""Merge Gilded's TWO independent 7-level Tyz skills into an already staged Tyz pack.

Never replace the six existing Core Magic/Monk/vanilla Tyz categories or other skills.
Only explicitly authorized pack composition calls --install; --check is non-mutating.
"""
from __future__ import annotations

import argparse
import json
import os
import tempfile
from pathlib import Path

ROOT = Path(__file__).resolve().parent
OVERLAY = ROOT / "tyzs-overlay"
SKILLS = (
    "tmm_gilded_fortune_mastery",
    "tmm_gilded_looting_mastery",
)
CATEGORY_PATCH = OVERLAY / "category-patch.json"


def read_json(path: Path) -> dict:
    value = json.loads(path.read_text(encoding="utf-8"))
    if not isinstance(value, dict):
        raise ValueError(f"Expected object in {path}")
    return value


def verify_source() -> tuple[dict[str, dict], dict]:
    patch = read_json(CATEGORY_PATCH)
    if list(patch) != ["tmm_gilded"] or patch["tmm_gilded"].get("order") != 7:
        raise ValueError("Expected one Gilded category patch, not an entire Tyz replacement")
    skills = {}
    for key in SKILLS:
        file = OVERLAY / "config/tyzs_skills/custom/skills" / f"{key}.json"
        skill = read_json(file)
        if (skill.get("id") != key
                or skill.get("type") != "IMMUTABLE"
                or skill.get("category") != "tmm_gilded"
                or skill.get("active") is not True
                or skill.get("maximumLevel") != 7
                or skill.get("prices") != [1] * 7
                or skill.get("modifiers") != []):
            raise ValueError(f"{key}: Tyz schema/skill contract mismatch")
        if skill.get("customValues", {}).get("power", {}).get("values") != list(range(4, 11)):
            raise ValueError(f"{key}: expected seven displayed ranks, +4 through +10")
        if skill.get("config", {}).get("purchasable") is not True:
            raise ValueError(f"{key}: purchase disabled")
        skills[key] = skill
    return skills, patch


def atomic_text(path: Path, content: str) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    fd, temporary = tempfile.mkstemp(prefix="gilded-", suffix=".tmp", dir=path.parent)
    try:
        with os.fdopen(fd, "w", encoding="utf-8") as output:
            output.write(content)
        os.replace(temporary, path)
    finally:
        if os.path.exists(temporary):
            os.unlink(temporary)


def stage(pack_root: Path) -> None:
    skills, patch = verify_source()
    root = pack_root.resolve()
    if not (root / "mods/tyzs-skills.pw.toml").is_file():
        raise ValueError("Tyz 6.4.2 must be pinned before Gilded staging")
    categories_path = root / "config/tyzs_skills/custom/data/categories.json"
    if not categories_path.is_file():
        raise ValueError("Missing current Tyz categories: stage Core Magic and Monk first")
    categories = read_json(categories_path)
    if not {"abilities", "combat", "misc", "nature", "tmm_magic_core", "tmm_monk"} <= categories.keys():
        raise ValueError("Existing vanilla/Core Magic/Monk Tyz categories incomplete")
    for name, definition in patch.items():
        if name in categories and categories[name] != definition:
            raise ValueError(f"Conflicting Tyz category {name}: reconcile before staging")
        categories[name] = definition

    # Validate every destination BEFORE writing categories or either skill.
    # A conflict must not leave one new skill installed while its sibling is missing.
    destinations = {}
    for key, skill in skills.items():
        destination = root / "config/tyzs_skills/custom/skills" / f"{key}.json"
        if destination.is_file() and read_json(destination) != skill:
            raise ValueError(f"Conflicting Tyz skill {key}; preserve owner's customization")
        destinations[destination] = skill
    atomic_text(categories_path,json.dumps(categories,indent=2)+"\n")
    for path, skill in destinations.items():
        atomic_text(path,json.dumps(skill,indent=2)+"\n")
    print(f"Staged Gilded category and {len(SKILLS)} independent seven-rank Tyz skills in {root}")


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--check", action="store_true", help="Verify overlay source, no changes")
    parser.add_argument("--install", action="store_true", help="Merge into a separately staged Tyz pack")
    parser.add_argument("--pack-root", type=Path)
    args = parser.parse_args()
    verify_source()
    if args.install:
        if not args.pack_root:
            parser.error("--install requires --pack-root")
        stage(args.pack_root)
    elif not args.check:
        parser.error("Select --check or --install; never stage implicitly")
    else:
        print("Gilded Fortune and Looting Tyz overlay verified (no pack changed)")
