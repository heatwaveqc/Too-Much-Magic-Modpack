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

    assert len(ids) == 168
    assert len(rehomes) == len(set(rehomes)) == 32
    assert len([t for t in data["trees"] if t.get("capstone")]) == 8
    assert all(all_entries[sid]["strength"] == "Average" and all_entries[sid]["level_cap"] == 5 for sid in CORE_IDS)
    assert all(all_entries[sid]["sp_per_rank"] == 1 for sid in CORE_IDS)
    # Canonical owner ceiling: Monk overall-combat apex, Sea Dragon absurd Luck niche.
    hierarchy = data["balancing"]["overall_endgame_power_hierarchy"]
    assert "Monk" in hierarchy["primary_benchmark"]
    assert "appropriately progressed specialist" in hierarchy["primary_benchmark"]
    gear = data["balancing"]["gear_normalized_balance_contract"]
    assert gear["status"] == "DESIGN_REQUIREMENT_NOT_MEASURED"
    assert "Monk" in gear["question"]
    assert "LEGAL CURIOS ALLOWED" in gear["primary_monk_profile"]["worn_equipment"]
    assert "no Curios" in data["balancing"]["chaos_guardian_monk_acceptance"]["desired_result"]
    assert "Curios are ALLOWED" in data["balancing"]["chaos_guardian_monk_acceptance"]["curios_interpretation"]
    assert "enchantments" in gear["comparison_specialist_profile"]["enhancements"]
    assert "Apotheosis" in gear["comparison_specialist_profile"]["enhancements"]
    assert "Curios" in gear["comparison_specialist_profile"]["gear"]
    assert any("Dragon mana" in condition for condition in gear["success_conditions"])
    assert any("Monk can" in condition for condition in gear["success_conditions"])
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

    # Late noncombat Monk normalization is measured against COMPLETE geared builds.
    parity = json.loads((ROOT / "docs/tyzs/monk-noncombat-parity-v1.json").read_text(encoding="utf-8"))
    assert parity["status"] == "DESIGN_TARGET_ONLY_NOT_MEASURED"
    assert parity["ratios"] == {"min": 0.8, "default": 0.85, "max": 0.9, "sea_dragon_luck": 0.8}
    assert len(parity["benchmarks"]) == 10
    luck = next(row for row in parity["benchmarks"] if row["id"] == "luck")
    assert luck["specialist_skill"] == "tmm_dragon_sea_abyssal_fortune"
    assert luck["monk_skills"] == ["tmm_monk_inner_fortune"]
    assert luck["target_ratio"] == 0.8
    assert parity["luck_example"]["monk_cap_50_curve"][-1]["monk_index"] == 80
    assert data["balancing"]["noncombat_parity_2026_11_15"]["ratio"]["default"] == 0.85
    for sid, strength, cap in (
        ("tmm_monk_inner_fortune", "Extreme", 50),
        ("tmm_monk_inner_reservoir", "Extreme", 25),
        ("tmm_monk_worldly_cultivation", "Strong", 25),
    ):
        skill = all_entries[sid]
        assert skill["tree"] == "tmm_monk"
        assert (skill["strength"], skill["level_cap"]) == (strength, cap)
    aff = json.loads((ROOT / "docs/tyzs/magic-affinity-nature-blood-other-v1.json").read_text(encoding="utf-8"))
    assert aff["status"] == "DESIGN_ONLY_NOT_RUNTIME_TAGS"
    assert aff["categories"] == ["Nature", "Blood", "Other"]
    names = [mod["id"] for mod in aff["mods"]]
    assert len(names) == len(set(names)) == 40
    tags = {mod["id"]: mod["primary"] for mod in aff["mods"]}
    assert all(category in aff["categories"] for category in tags.values())
    assert tags["neovitae"] == tags["evilcraft"] == "Blood"
    assert all(tags[x] == "Nature" for x in
               ("botania-revived", "roots-classic", "feywild", "extra-botany-revived", "totemic"))
    assert all(tags[x] == "Other" for x in
               ("goety", "occultism", "malum", "forbidden-magic", "irons-spells"))
    overrides = {(o["parent"], o["id"]): o for o in aff["contract"]["mixed_overrides"]}
    assert overrides[("irons-spells", "irons_spellbooks:blood")]["category"] == "Blood"
    assert overrides[("irons-spells", "irons_spellbooks:nature")]["category"] == "Nature"
    manifest = json.loads((ROOT / "manifest/mods.json").read_text(encoding="utf-8"))
    installed_ids = {mod["id"] for mod in manifest["mods"]}
    assert set(names) <= installed_ids, f"Affinity registry missing manifest IDs: {set(names)-installed_ids}"

    gear_audit = json.loads((ROOT / "docs/tyzs/gear-enchant-affix-curios-ceiling-audit-v1.json").read_text(encoding="utf-8"))
    assert gear_audit["status"] == "SOURCE_AUDIT_PRELIMINARY_NOT_RUNTIME_VERIFIED"
    assert gear_audit["apotheosis"]["source_definition_count"] == 99
    assert gear_audit["apotheosis"]["gem_catalog"]["count"] == 23
    assert gear_audit["apothic_custom_enchantments"]["count"] == 20
    assert gear_audit["manifest_screen"]["count"] == 164
    assert gear_audit["manifest_screen"]["enabled_count"] == 155
    assert gear_audit["curios_policy"]["standard_monk_allowed"]
    assert gear_audit["apotheosis"]["overcharge"]["sigil_of_supremacy"]["level"] == 1.5
    assert gear_audit["apotheosis"]["overcharge"]["sigil_of_malice"]["level"] == 2.0
    assert len(gear_audit["apotheosis"]["source_affix_groups"]) >= 20
    assert sum(g["count"] for g in gear_audit["apotheosis"]["source_affix_groups"].values()) == 99
    assert "MONK" in data["balancing"]["overall_endgame_power_hierarchy"]["monk_benchmark_loadout"]
    assert data["balancing"]["equipment_ceiling_2026_11_15"]["source_affixes"] == 99

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
