#!/usr/bin/env python3
"""Static acceptance gate for the active Tyz migration. NOT a client gameplay pass."""
from __future__ import annotations
import json
import tomllib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
IDS = {"tmm_magic_reservoir_1", "tmm_magic_wellspring_1",
       "tmm_magic_thrift_1", "tmm_magic_potency_1"}
MONK_IDS = {"tmm_monk_empty_hand_force", "tmm_monk_flowing_counter", "tmm_monk_unarmed_precision", "tmm_monk_natural_poise", "tmm_monk_hunger_discipline", "tmm_monk_fire_blast_tempering", "tmm_monk_body_speed", "tmm_monk_balanced_landing", "tmm_monk_glide_control", "tmm_monk_barehand_efficiency", "tmm_monk_fortunate_hands", "tmm_monk_looting_palm", "tmm_monk_monastic_learning", "tmm_monk_focused_advancement", "tmm_monk_inner_fortune", "tmm_monk_inner_reservoir", "tmm_monk_worldly_cultivation", "tmm_monk_capstone"}
REQUIRED_TABS = {"abilities", "combat", "misc", "nature", "tmm_magic_core", "tmm_monk"}
DISABLED_NUMERIC_XP = {"sleeping_gain", "fishing_gain", "crafting_gain",
                       "breeding_gain", "task_gain", "goal_gain",
                       "challenge_gain", "biome_gain", "dimension_gain"}
DISABLED_BOOLEAN_XP = {"eating_xp_earnings", "killing_xp_earnings",
                       "mining_xp_earnings", "earn_xp_in_creative"}

def verify(root: Path = ROOT) -> None:
    assert (root / "mods/tyzs-skills.pw.toml").is_file(), "Tyz missing from packwiz"
    assert not (root / "mods/puffish-skills.pw.toml").exists(), "Pufferfish Skills still installed"
    assert (root / "mods/puffish-attributes.pw.toml").is_file(), "Pufferfish Attributes removed without audit"
    mod = tomllib.loads((root / "mods/tyzs-skills.pw.toml").read_text(encoding="utf-8"))
    assert mod["side"] == "both"
    assert mod["update"]["modrinth"] == {"mod-id": "4rKNK5O2", "version": "OUmvFAPs"}
    assert len(mod["download"]["hash"]) == 128 and mod["download"]["hash-format"] == "sha512"
    manifest = json.loads((root / "manifest/mods.json").read_text(encoding="utf-8"))
    rows = {m["id"]: m for m in manifest["mods"]}
    assert rows["tyzs-skills"]["enabled_in_pack"] is True
    assert rows["puffish-skills"]["enabled_in_pack"] is False
    assert rows["puffish-attributes"]["enabled_in_pack"] is True
    cats = json.loads((root / "config/tyzs_skills/custom/data/categories.json").read_text())
    assert REQUIRED_TABS.issubset(cats), "Missing transitional Tyz or Core Skills tabs"
    assert cats["tmm_magic_core"]["displayName"] == "Core Skills"
    assert cats["tmm_monk"]["displayName"] == "Monk", "Wrong universal tab label"
    assert json.loads((root / "config/tyzs_skills/custom/data/level-pool.json").read_text()) == {
        "-1": {"goal": 1000, "reward": 1}
    }, "Wrong global MXP -> SP threshold"
    skill_dir = root / "config/tyzs_skills/custom/skills"
    assert {f.stem for f in skill_dir.glob("tmm_*.json")} == IDS | MONK_IDS, "Unexpected or missing first-wave skills"
    for sid in IDS:
        data = json.loads((skill_dir / f"{sid}.json").read_text())
        assert data["active"] is True and data["type"] == "CUSTOM"
        assert data["category"] == "tmm_magic_core" and data["maximumLevel"] == 5
        assert data["prices"] == [1, 1, 1, 1, 1]
        for modifier in data["modifiers"]:
            assert len(modifier["values"]) == 5, "Core modifier has an invalid rank curve"
    for mid in MONK_IDS:
        item = json.loads((skill_dir / f"{mid}.json").read_text())
        assert item["id"] == mid and item["active"] and item["type"] == "IMMUTABLE"
        assert item["category"] == "tmm_monk"
        assert len(item["prices"]) == item["maximumLevel"]
        assert len(item["customValues"]["power"]["values"]) == item["maximumLevel"]
        assert not item["modifiers"], "Monk conditional skills may not be unconditional attribute buffs"
    common = tomllib.loads((root / "config/tyzs_skills-common.toml").read_text(encoding="utf-8"))
    xp = common["xp_values"]
    assert all(xp[name] is False for name in DISABLED_BOOLEAN_XP), "Unwanted native activity XP"
    assert all(xp[name] == 0 for name in DISABLED_NUMERIC_XP), "Unwanted native activity XP"
    assert common["Limits"]["xp_daily"] == -1
    assert common["Limits"]["max_lvl"] == -1
    assert (root / "kubejs/server_scripts/tmm_magic_core_thrift.js").is_file()
    assert not (root / "config/puffish_skills").exists()
    print("PASS: Tyz 6.4.2 exclusive active Skills backend, 4x5 initial Core Magic plus 18 native-licensed Monk Tyz nodes,")
    print("      original Tyz tabs preserved, native XP disabled, independent Attributes retained.")
    print("NOT TESTED: client GUI, actual spell effects, and MXP award dispatcher.")

if __name__ == "__main__":
    verify()
