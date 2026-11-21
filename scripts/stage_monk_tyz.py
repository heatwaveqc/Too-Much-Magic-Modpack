#!/usr/bin/env python3
"""Safely add Monk's 18 IMMUTABLE Tyz skills to an already-working Tyz 6.4.2 pack.

This stages config into packwiz *source* only; it never changes mod pins, Tyz
XP/SP, default categories, player data, world saves, or published artifacts.
The calling assembler refreshes packwiz once after all source modifications.
"""
from __future__ import annotations

import argparse
import json
import subprocess
import sys
import tomllib
from pathlib import Path

SOURCE_ROOT = Path(__file__).resolve().parents[1]
OVERLAY = SOURCE_ROOT / "compat/magic/tyzs-test-overlay/config/tyzs_skills/custom"
PREVIEW = SOURCE_ROOT / "compat/magic/stage_preview.py"
MONK_IDS = {
    "tmm_monk_empty_hand_force", "tmm_monk_flowing_counter",
    "tmm_monk_unarmed_precision", "tmm_monk_natural_poise",
    "tmm_monk_hunger_discipline", "tmm_monk_fire_blast_tempering",
    "tmm_monk_body_speed", "tmm_monk_balanced_landing",
    "tmm_monk_glide_control", "tmm_monk_barehand_efficiency",
    "tmm_monk_fortunate_hands", "tmm_monk_looting_palm",
    "tmm_monk_monastic_learning", "tmm_monk_focused_advancement",
    "tmm_monk_inner_fortune", "tmm_monk_inner_reservoir",
    "tmm_monk_worldly_cultivation", "tmm_monk_capstone",
}


def stage(root: Path) -> None:
    root = root.resolve()
    if not (root / "index.toml").is_file():
        raise ValueError("Requires a real packwiz source tree, not an installed game or save")
    pinned = root / "mods/tyzs-skills.pw.toml"
    if not pinned.is_file():
        raise ValueError("Existing Tyz pack pin missing; use first-wave activation instead")
    t = tomllib.loads(pinned.read_text(encoding="utf-8"))
    if t.get("update", {}).get("modrinth") != {
        "mod-id": "4rKNK5O2", "version": "OUmvFAPs"
    } or t.get("download", {}).get("hash-format") != "sha512":
        raise ValueError("Unexpected Tyz version/pin: refuse to patch incompatible skill format")
    subprocess.run([sys.executable, str(PREVIEW), "--check"],
                   cwd=SOURCE_ROOT, check=True)

    source_skills = OVERLAY / "skills"
    destination_skills = root / "config/tyzs_skills/custom/skills"
    source_ids = {p.stem for p in source_skills.glob("tmm_monk_*.json")}
    if source_ids != MONK_IDS:
        raise ValueError(f"18-skill Monk source mismatch: {source_ids ^ MONK_IDS}")
    # Validate all files before writing anything.
    content = {}
    for skill_id in sorted(MONK_IDS):
        item = json.loads((source_skills / (skill_id + ".json")).read_text())
        if not item.get("active") or item.get("id") != skill_id                 or item.get("category") != "tmm_monk" or item.get("type") != "IMMUTABLE":
            raise ValueError(f"Invalid Tyz Monk skill: {skill_id}")
        content[skill_id] = json.dumps(item, indent=2) + "\n"

    current_path = root / "config/tyzs_skills/custom/data/categories.json"
    if not current_path.exists():
        raise ValueError("Existing Tyz category registry missing; do not replace its four defaults")
    current = json.loads(current_path.read_text())
    required = {"abilities", "combat", "misc", "nature", "tmm_magic_core"}
    if not required.issubset(current):
        raise ValueError("The existing Tyz category set is incomplete or already overwritten")
    intended = json.loads((OVERLAY / "data/categories.json").read_text())
    existing = current.get("tmm_monk")
    if existing is not None and existing.get("displayName") != "Monk":
        raise ValueError("Another mod already owns the tmm_monk category")
    current["tmm_monk"] = intended["tmm_monk"]

    destination_skills.mkdir(parents=True, exist_ok=True)
    current_path.write_text(json.dumps(current, indent=2) + "\n")
    changed = 0
    for skill_id, data in content.items():
        dest = destination_skills / (skill_id + ".json")
        if not dest.exists() or dest.read_text() != data:
            dest.write_text(data, encoding="utf-8")
            changed += 1
    print(f"PASS: Staged Monk Tyz 6.4.2 category and {len(content)} skills; {changed} written.")
    print("Single Tyz wallet and four Core preview skills were left unchanged.")
    print("Run packwiz refresh and compile the SAME branch's tyr_progression JAR.")


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--pack-root", type=Path, required=True)
    stage(parser.parse_args().pack_root)
