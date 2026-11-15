#!/usr/bin/env python3
"""Static validation of the nine-tree *design* catalog (not in-game balance)."""
from __future__ import annotations

import json
from collections import Counter
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
FILE = ROOT / "docs/tyzs/nine-tree-strength-first-pass-v1.json"
TIERS = {"Weak": 5, "Middling": 10, "Average": 20, "Strong": 50, "Extreme": 100}
CAPS = {5, 10, 25, 50}
CATEGORIES = {
    "tmm_magic_core", "tmm_dragon_cave", "tmm_dragon_sea",
    "tmm_dragon_forest", "tmm_vampire", "tmm_werewolf",
    "tmm_lich", "tmm_hunter", "tmm_monk",
}
CORE_IDS = {
    "tmm_magic_reservoir_1", "tmm_magic_wellspring_1",
    "tmm_magic_thrift_1", "tmm_magic_potency_1",
}
DRAGON_IDS = {x for x in CATEGORIES if x.startswith("tmm_dragon_")}


def verify() -> None:
    data = json.loads(FILE.read_text(encoding="utf-8"))
    assert data["status"] == "DESIGN_ONLY_NOT_ACTIVE"
    assert data["balancing"]["strength_categories"] == TIERS
    assert data["balancing"]["standard_level_caps"] == sorted(CAPS)
    assert len(data["trees"]) == 9
    assert {t["id"] for t in data["trees"]} == CATEGORIES

    ids: set[str] = set()
    rehomes: list[str] = []
    all_entries: dict[str, dict] = {}
    for tree in data["trees"]:
        for group in tree["groups"]:
            for skill in group["skills"]:
                sid = skill["id"]
                assert sid not in ids, f"Duplicate ID: {sid}"
                ids.add(sid)
                all_entries[sid] = {"tree": tree["id"], **skill}
                assert skill["strength"] in TIERS
                assert skill["percent_per_rank"] == TIERS[skill["strength"]]
                assert skill["level_cap"] in CAPS
                assert skill["sp_per_rank"] in (1, 2)
                assert skill["max_sp"] == skill["level_cap"] * skill["sp_per_rank"]
                assert skill["nominal_cumulative_percent"] == skill["percent_per_rank"] * skill["level_cap"]
                assert skill["runtime_status"] == "DESIGN_ONLY"
                if skill["origin"] == "builtin_same_id_rehome_plan":
                    rehomes.append(sid)
                    assert skill["target_category"] == tree["id"]
                    assert skill["native_max_levels"] >= 1

        if tree["id"] in DRAGON_IDS:
            shared = next(g for g in tree["groups"] if g["name"].startswith("Universal Dragon"))
            shared_items = {s["label"]: s for s in shared["skills"]}
            assert len(shared_items) == 5
            assert shared_items["Draconic Reservoir"]["strength"] == "Extreme"
            assert shared_items["Draconic Reservoir"]["level_cap"] == 25
            assert shared_items["Draconic Potency"]["strength"] == "Extreme"
            assert shared_items["Draconic Potency"]["level_cap"] == 10
            assert all(
                shared_items[name]["sp_per_rank"] == 2
                for name in ("Draconic Scales", "Draconic Claws", "Draconic Talons")
            )
            assert not any("mana_regen" in s["id"] for group in tree["groups"] for s in group["skills"])
        else:
            assert tree["id"] == "tmm_magic_core" or tree["capstone"]["level_cap"] == 1

    assert len(ids) == 165
    assert len(rehomes) == len(set(rehomes)) == 32
    assert len([t for t in data["trees"] if t.get("capstone")]) == 8
    assert all(all_entries[sid]["strength"] == "Average" and all_entries[sid]["level_cap"] == 5 for sid in CORE_IDS)
    assert all(all_entries[sid]["sp_per_rank"] == 1 for sid in CORE_IDS)
    # Canonical owner ceiling: Monk overall-combat apex, Sea Dragon absurd Luck niche.
    hierarchy = data["balancing"]["overall_endgame_power_hierarchy"]
    assert "Monk" in hierarchy["primary_benchmark"]
    boss = data["balancing"]["chaos_guardian_monk_acceptance"]
    assert boss["status"] == "DESIGN_ONLY_NEEDS_MECHANICS_AUDIT_AND_PLAYTEST"
    assert "Chaos Guardian" in boss["desired_result"]
    assert "no weapons" in boss["desired_result"]
    sea_luck = all_entries["tmm_dragon_sea_abyssal_fortune"]
    assert sea_luck["tree"] == "tmm_dragon_sea"
    assert (sea_luck["strength"], sea_luck["level_cap"], sea_luck["percent_per_rank"]) == ("Extreme", 50, 100)
    monk = next(t for t in data["trees"] if t["id"] == "tmm_monk")
    assert "Chaos Guardian" in monk["capstone"]["benchmark"]
    assert monk["capstone"]["benchmark_status"] == "DESIGN_REQUIREMENT_NOT_YET_TESTED"
    assert "no worn equipment" in monk["capstone"]["benchmark"]
    assert "no weapons" in monk["capstone"]["benchmark"]

    assert "tmm_monk_spirit_bolt_mastery" not in ids
    assert "tmm_monk_unarmed_precision" in ids
    assert all_entries["green_thumb"]["tree"] == "tmm_dragon_forest"
    assert all_entries["shepherd_s_blessing"]["tree"] == "tmm_dragon_forest"
    assert all_entries["critical_hit"]["tree"] == "tmm_hunter"
    assert all_entries["resilience"]["tree"] == "tmm_hunter"
    for t in data["trees"]:
        for group in t["groups"]:
            for skill in group["skills"]:
                for prerequisite in skill.get("prerequisites", []):
                    assert prerequisite in ids, f"{skill['id']}: missing {prerequisite}"

    print(f"PASS (DESIGN ONLY): {len(data['trees'])} trees, {len(ids)} rankable entries, "
          f"{len(rehomes)} same-ID stock candidates, 8 capstones.")


if __name__ == "__main__":
    verify()
