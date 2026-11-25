#!/usr/bin/env python3
"""Validate historical Monk Tyz overlay and current native Tyr Progression contract.

Run from repository root: python3 -m unittest tests/test_monk_tyz_tree.py
No modded server/client/packwiz smoke is started by this check.
"""
import json
import pathlib
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
NATIVE = ROOT / "compat/progression/src/main"
MONK_PATH = NATIVE / "resources/data/tyr_progression/tyr_progression/paths/monk.json"
MXP = "tyr_progression:mxp"


class MonkTyzTreeTests(unittest.TestCase):
    def test_historical_overlay_skill_inventory(self):
        actual = {p.stem for p in SKILLS.glob("tmm_*.json")}
        self.assertEqual(CORE | MONK, actual)
        self.assertEqual(18, len(MONK))
        categories = json.loads((OVERLAY / "config/tyzs_skills/custom/data/categories.json").read_text())
        self.assertEqual("Monk", categories["tmm_monk"]["displayName"])
        self.assertEqual("Core Skills", categories["tmm_magic_core"]["displayName"])
        self.assertTrue({"abilities", "combat", "misc", "nature"}.issubset(categories))
        self.assertEqual({"-1": {"goal": 1000, "reward": 1}},
                         json.loads((OVERLAY / "config/tyzs_skills/custom/data/level-pool.json").read_text()))

    def test_native_monk_rank_storage_and_discipline_rules(self):
        # The staged Tyz overlay is historical; live purchases use native Path JSON.
        path = json.loads(MONK_PATH.read_text(encoding="utf-8"))
        skills = {skill["id"]: skill for skill in path["skills"]}
        self.assertEqual(len(skills), len(path["skills"]))
        milestones = {row["id"] for row in path["milestones"]}
        self.assertEqual(MXP, path["xp_channel"])
        for sid, skill in skills.items():
            with self.subTest(skill=sid):
                self.assertTrue(sid.startswith("tyr_progression:"))
                self.assertEqual(MXP, skill["level_system"])
                self.assertGreater(skill["max_rank"], 0)
                self.assertTrue(skill["effects"], "No purchasable no-op skill")
                self.assertTrue(set(skill.get("requires_skills", [])) <= skills.keys())
                self.assertTrue(set(skill.get("requires_milestones", [])) <= milestones)
                if "auto_ranks" in skill:
                    ranks = skill["auto_ranks"]
                    self.assertEqual(skill["max_rank"], len(ranks))
                    self.assertEqual(ranks, sorted(set(ranks)))
                    self.assertEqual(0, skill["base_cost"])
                    self.assertEqual(0, skill["step_cost"])
                else:
                    self.assertEqual(MXP, skill["xp_channel"])
                    self.assertEqual(skill["max_rank"], len(skill["rank_costs"]))
                    self.assertTrue(all(isinstance(price, int) and price > 0
                                        for price in skill["rank_costs"]))
        fist = skills["tyr_progression:empty_hand_force"]
        self.assertTrue(fist["effects"][0]["empty_hand"])
        self.assertTrue(fist["effects"][0]["unarmored"])
        for name in ("body_speed", "graceful_step", "barehand_efficiency"):
            self.assertTrue(skills["tyr_progression:" + name]["effects"][0]["unarmored"])
        self.assertTrue(skills["tyr_progression:barehand_efficiency"]["effects"][0]["empty_hand"])
        self.assertIn("tyr_progression:empty_hand_force",
                      skills["tyr_progression:flowing_counter"]["requires_skills"])
        self.assertIn("tyr_progression:barehand_efficiency",
                      skills["tyr_progression:fortunate_hands"]["requires_skills"])
        self.assertEqual(list(range(100, 951, 50)),
                         skills["tyr_progression:enlightenment"]["rank_invested_requirements"])

    def test_native_mxp_authority_and_retired_bridge_absence(self):
        # Reuse the active source-selection guard rather than require dead Java.
        import sys
        sys.path.insert(0, str(ROOT / "scripts"))
        from verify_unified_progression import verify_selection
        verify_selection(ROOT)
        path = json.loads(MONK_PATH.read_text(encoding="utf-8"))
        self.assertEqual(MXP, path["training_mxp"]["system"])
        self.assertEqual(MXP, path["mastery_stages"]["system"])
        self.assertTrue(all(row["system"] == MXP for row in path["unified_mxp_sources"]))
        ids = {skill["id"] for skill in path["skills"]}
        self.assertNotIn("tyr_progression:spirit_projectile", ids)
        self.assertNotIn("tyr_progression:creative_flight", ids)
        self.assertFalse(any(NATIVE.rglob("TyzMonkBridge.java")))

    def test_historical_overlay_contains_no_placeholder_affixes(self):
        ids = [p.stem for p in SKILLS.glob("*.json") if p.stem.startswith("tmm_monk_")]
        self.assertEqual(18,len(ids))
        self.assertFalse(any("affix" in id for id in ids))


if __name__ == "__main__":
    unittest.main()
