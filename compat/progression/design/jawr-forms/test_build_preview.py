#!/usr/bin/env python3
"""Unit tests for safe, inert form-tree source definitions (stdlib only)."""
import json
import tempfile
import unittest
from pathlib import Path
from build_preview import load, render_skill, categories, build

class FormTreesTests(unittest.TestCase):
    def setUp(self):
        self.catalog = load()
        self.skills = self.catalog["skills"]

    def test_counts_ids_and_stages(self):
        self.assertEqual(len(self.skills), 37)
        self.assertEqual(sum(s["form"] == "vampire" for s in self.skills), 16)
        self.assertEqual(sum(s["form"] == "werewolf" for s in self.skills), 21)
        self.assertEqual(len({s["id"] for s in self.skills}), 37)
        self.assertTrue(all(not s["active"] for s in self.skills))

    def test_json_overrides_match_tyz_immutable_schema(self):
        for spec in self.skills:
            data = render_skill(spec)
            self.assertEqual(data["type"], "IMMUTABLE")
            self.assertFalse(data["active"])
            self.assertEqual(len(data["prices"]), data["maximumLevel"])
            self.assertEqual(len(data["customValues"]["magnitude"]["values"]), data["maximumLevel"])
            self.assertEqual(data["config"]["skillPrerequisites"], spec["skillPrerequisites"])
            self.assertEqual(data["id"], spec["id"])

    def test_bite_choice_is_mutually_exclusive(self):
        nodes = {s["id"]: s for s in self.skills}
        self.assertIn("tmm_werewolf_stunning_bite", nodes["tmm_werewolf_bleeding_bite"]["incompatibleSkills"])
        self.assertIn("tmm_werewolf_bleeding_bite", nodes["tmm_werewolf_stunning_bite"]["incompatibleSkills"])

    def test_required_key_mechanics_retained(self):
        effects = {s["effect"] for s in self.skills}
        self.assertTrue({
            "jawr_kill_heal", "jawr_claw_mining_speed", "jawr_claw_harvest_tier",
            "jawr_bonus_max_health", "jawr_health_regen", "jawr_blood_debit",
            "jawr_bite_blood_gain", "jawr_silver_resistance", "jawr_bite_bleed",
            "jawr_projectile_avoidance"
        }.issubset(effects))

    def test_no_worldgen_raid_minion_skill(self):
        for skill in self.skills:
            self.assertFalse(any(term in skill["effect"] for term in
                                ("worldgen", "raid", "minion", "pet", "outpost", "lord")))

    def test_preserves_other_categories(self):
        base = {x: {"displayName": x} for x in
                ("abilities", "combat", "misc", "nature", "tmm_magic_core", "tmm_dragon_cave")}
        out = categories(base)
        self.assertEqual(out["tmm_dragon_cave"], base["tmm_dragon_cave"])
        self.assertEqual(len(out), 8)

    def test_staged_preview_is_inactive(self):
        base = {x: {"displayName": x} for x in
                ("abilities", "combat", "misc", "nature", "tmm_magic_core")}
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            fixture = root / "categories.json"
            fixture.write_text(json.dumps(base))
            self.assertEqual(build(root / "preview", fixture), 37)
            cfg = root / "preview/config/tyzs_skills/custom"
            data = json.loads((cfg / "skills/tmm_werewolf_bloodlust_recovery.json").read_text())
            self.assertFalse(data["active"])
            self.assertEqual(data["type"], "IMMUTABLE")
            self.assertEqual(len(list((cfg / "skills").glob("*.json"))), 37)

if __name__ == "__main__":
    unittest.main()
