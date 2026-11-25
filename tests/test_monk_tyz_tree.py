#!/usr/bin/env python3
"""Validate Monk Tyz 6.4.2 JSON, native bridge, and release-stage contract.

Run from repository root: python3 -m unittest tests/test_monk_tyz_tree.py
No modded server/client/packwiz smoke is started by this check.
"""
import json
import pathlib
import re
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[1]
OVERLAY = ROOT / "compat/magic/tyzs-test-overlay"
SKILLS = OVERLAY / "config/tyzs_skills/custom/skills"
CORE = {
    "tmm_magic_reservoir_1", "tmm_magic_wellspring_1",
    "tmm_magic_thrift_1", "tmm_magic_potency_1",
}
LANES = {
    "martial": ["empty_hand_force", "flowing_counter", "unarmed_precision"],
    "conditioning": ["natural_poise", "hunger_discipline", "fire_blast_tempering"],
    "footwork": ["body_speed", "balanced_landing", "glide_control"],
    "harvest": ["barehand_efficiency", "fortunate_hands", "looting_palm"],
}
OTHER = [
    "monastic_learning", "focused_advancement", "inner_fortune",
    "inner_reservoir", "worldly_cultivation",
]
MONK = {"tmm_monk_" + name for values in LANES.values() for name in values}
MONK |= {"tmm_monk_" + name for name in OTHER}
MONK.add("tmm_monk_capstone")
BRIDGE = ROOT / "compat/progression/src/main/java/io/github/heatwaveqc/tyr/progression/runtime/TyzMonkBridge.java"


class MonkTyzTreeTests(unittest.TestCase):
    def test_every_skill_loads_in_exactly_one_existing_tyz_wallet(self):
        actual = {p.stem for p in SKILLS.glob("tmm_*.json")}
        self.assertEqual(CORE | MONK, actual)
        self.assertEqual(18, len(MONK))
        categories = json.loads((OVERLAY / "config/tyzs_skills/custom/data/categories.json").read_text())
        self.assertEqual("Monk", categories["tmm_monk"]["displayName"])
        self.assertEqual("Core Skills", categories["tmm_magic_core"]["displayName"])
        self.assertTrue({"abilities", "combat", "misc", "nature"}.issubset(categories))
        self.assertEqual({"-1": {"goal": 1000, "reward": 1}},
                         json.loads((OVERLAY / "config/tyzs_skills/custom/data/level-pool.json").read_text()))

    def test_monk_immutable_rank_storage_and_discipline_rules(self):
        bridge = BRIDGE.read_text()
        stage = {}
        for sid in sorted(MONK):
            item = json.loads((SKILLS / f"{sid}.json").read_text())
            self.assertEqual(sid, item["id"])
            self.assertTrue(item["active"])
            self.assertEqual("IMMUTABLE", item["type"])
            self.assertEqual("tmm_monk", item["category"])
            self.assertFalse(item["modifiers"], "No unrestricted attribute perks allowed")
            count = item["maximumLevel"]
            self.assertEqual(count, len(item["prices"]))
            self.assertTrue(all(isinstance(price, int) and price > 0 for price in item["prices"]))
            values = item["customValues"]["power"]["values"]
            self.assertEqual(count, len(values))
            self.assertGreater(values[-1], 0)
            self.assertTrue(all(a < b for a, b in zip(values, values[1:])))
            self.assertTrue(item["config"]["visible"])
            self.assertTrue(item["config"]["purchasable"])
            for parent in item["config"]["skillPrerequisites"]:
                self.assertIn(parent, MONK)
            self.assertIn(sid, bridge, f"{sid} lacks code-owned runtime or license")
            stage[sid] = item

        for lane in LANES.values():
            entry, advanced, mastery = ["tmm_monk_" + name for name in lane]
            self.assertEqual([], stage[entry]["config"]["skillPrerequisites"])
            self.assertEqual([entry], stage[advanced]["config"]["skillPrerequisites"])
            self.assertEqual([advanced], stage[mastery]["config"]["skillPrerequisites"])
        self.assertEqual(5,stage["tmm_monk_capstone"]["prices"][0])
        self.assertEqual(1,stage["tmm_monk_capstone"]["maximumLevel"])
        self.assertFalse(stage["tmm_monk_focused_advancement"]["config"]["refundable"])
        self.assertIn("SkillActionEvent$PurchasePre",bridge)
        self.assertIn("SkillActionEvent$RefundPre",bridge)
        self.assertIn("getSkillLevel",bridge)
        self.assertIn("FOCUS_PREFIX",bridge)
        self.assertIn("emptyCurios",bridge)

    def test_bridge_is_optional_and_global_mxp_is_unmodified(self):
        bridge=BRIDGE.read_text()
        self.assertIn('ModList.get().isLoaded("tyzs_skills")',bridge)
        self.assertNotIn("import com.tyzsskills",bridge)
        self.assertNotIn("minecraft:creative_flight",bridge)
        self.assertNotIn("spirit_projectile",bridge)
        native=(ROOT / "compat/progression/src/main/java/io/github/heatwaveqc/tyr/progression/core/ProgressionApi.java").read_text()
        self.assertIn("awardMonkTyzPointOnce", native)
        self.assertIn("bonusMonkXp",native)
        self.assertIn("ChannelEngine.grant(ChannelEngine.ownership(ledger,path,monk.complete(marker)),channel,0,1)", native)
        self.assertNotIn("TyzsSkillsAPI.sp().add",native)

    def test_pending_affixes_are_not_noop_purchases(self):
        ids = [p.stem for p in SKILLS.glob("*.json") if p.stem.startswith("tmm_monk_")]
        self.assertEqual(18,len(ids))
        self.assertFalse(any("affix" in id for id in ids))


if __name__ == "__main__":
    unittest.main()
