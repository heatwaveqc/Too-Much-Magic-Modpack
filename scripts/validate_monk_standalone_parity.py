#!/usr/bin/env python3
"""Validate the *design-only* standalone Monk pre-Tyz classic-parity contract.

Usage: python3 scripts/validate_monk_standalone_parity.py
This is a source-design inventory check; it does NOT prove a gameplay feature works.
"""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MANIFEST = ROOT / "docs/monk/standalone-pre-tyz-classic-parity-v1.json"
TARGET = ROOT / "docs/monk/STANDALONE-PRE-TYZS-CLASSIC-TARGET.md"
ORIGINAL_IDS = frozenset({
    "armor", "armor_toughness", "blindness", "blink", "catch_arrows",
    "kiss_creeper", "explosion_resistance", "feather_fall", "fly",
    "glass_touch", "potion_immunity", "health", "hunger", "jump",
    "mining", "fire_resistance", "lava_protection", "swift", "step_up",
    "strength", "tame_animals", "water_walking",
    "water_breathing_partial", "water_breathing_full", "withering_stare",
})
NON_DEFAULT = frozenset({
    "armor_toughness", "glass_touch", "health", "water_breathing_full",
})
UTILITY_IDS = frozenset({
    "tmm_monk:reserved_fist_slot", "tmm_monk:armor_lock",
    "tmm_monk:treasure_hand",
})


def validate() -> list[str]:
    errors: list[str] = []
    if not MANIFEST.is_file() or not TARGET.is_file():
        return ["Missing standalone parity JSON or Markdown target"]

    data = json.loads(MANIFEST.read_text(encoding="utf-8"))
    doc = TARGET.read_text(encoding="utf-8")
    rows = data.get("abilities", [])
    ids = [a.get("classic_id") for a in rows]
    if len(rows) != 25 or len(set(ids)) != 25:
        errors.append("Exactly 25 distinct classic ability rows required")
    if set(ids) != ORIGINAL_IDS:
        errors.append(f"Classic mismatch: missing={ORIGINAL_IDS-set(ids)}, extra={set(ids)-ORIGINAL_IDS}")
    if data.get("ability_count") != len(rows):
        errors.append("ability_count disagrees with actual rows")
    if data.get("state") != "DESIGN_TARGET_ONLY_NOT_RUNTIME_IMPLEMENTED":
        errors.append("Design target must not pretend to be active runtime")

    claims = data.get("claims", {})
    for flag in (
        "parity_all_in_one_character", "requires_vampirism", "requires_werewolves",
        "requires_tyz", "innate_creative_flight",
    ):
        expected = flag == "parity_all_in_one_character"
        if claims.get(flag) is not expected:
            errors.append(f"Incorrect pre-Tyz/parity invariant: {flag}")
    if claims.get("classic_registered_abilities") != 25:
        errors.append("Classic registered count must be 25")
    if set(claims.get("original_unused_by_default", [])) != NON_DEFAULT:
        errors.append("All four original registered-but-unused abilities must be accounted for")

    for row in rows:
        id_ = row.get("classic_id")
        if row.get("classic_default_config_enabled") != (id_ not in NON_DEFAULT):
            errors.append(f"{id_}: wrong original default status")
        for key in (
            "name", "classic_milestone_reference", "source_effect", "native_target",
            "implementation_contract", "acceptance_tests", "progression",
        ):
            if not isinstance(row.get(key), str) or not row[key].strip():
                errors.append(f"{id_}: missing implementation/acceptance field {key}")
        if row.get("status") != "design_only_not_installed":
            errors.append(f"{id_}: not a design-only entry")
        if id_ != "fly" and row.get("parity") == "intentional_non_equivalent_replacement_user_approved":
            errors.append(f"{id_}: only fly may have an explicit non-equivalent substitution")
        if f"\x60{id_}\x60" not in doc:
            errors.append(f"{id_}: not in human-readable inventory table")
    flight = next((row for row in rows if row.get("classic_id") == "fly"), {})
    if flight.get("parity") != "intentional_non_equivalent_replacement_user_approved":
        errors.append("Flight must be explicitly a non-equivalent native Momentum Glide replacement")

    utilities = {p.get("id"): p for p in data.get("native_free_utility_contracts", [])}
    if set(utilities) != UTILITY_IDS:
        errors.append("Expected fist, armor-lock and Treasure Hand free/baseline features")
    for id_, utility in utilities.items():
        if not utility.get("contract") or not utility.get("input"):
            errors.append(f"{id_}: missing utility behavior")
    if "hotbar key 1" not in utilities.get("tmm_monk:reserved_fist_slot", {}).get("input", ""):
        errors.append("Protected fist must be key 1, index zero")
    armor = utilities.get("tmm_monk:armor_lock", {}).get("contract", "")
    if not ("Life Mending" in armor and "20 server ticks" in armor):
        errors.append("Armor Lock needs binding wear, one-second cadence and Life Mending trap")
    treasure = utilities.get("tmm_monk:treasure_hand", {}).get("contract", "")
    if not ("Fortune" in treasure and "Silk Touch" in treasure and "no Tyz required" in treasure):
        errors.append("Treasure Hand baseline must be exclusive and independent from Tyz")

    if len(data.get("acceptance_gates", [])) < 10:
        errors.append("Need comprehensive standalone end-to-end exit gates")
    if "## 2. Complete registered-ability inventory" not in doc:
        errors.append("Human design missing full 25-row inventory")
    if "## 7. Suggested implementation slices" not in doc:
        errors.append("Human design missing handoff implementation sequence")
    if "No native Spirit Projectile" not in doc and "do not generate Spirit Projectile" not in doc:
        errors.append("Missing explicit no-projectile owner rule")
    return errors


if __name__ == "__main__":
    violations = validate()
    if violations:
        for issue in violations:
            print(f"FAIL: {issue}")
        raise SystemExit(1)
    print("PASS: 25 classic Monk abilities accounted for; four off-default cases,")
    print("      no-Tyz baselines, glide exception, utility contracts and exits validated.")
