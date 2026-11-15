#!/usr/bin/env python3
"""Regression tests for the human-updated feature checklist contract."""
from __future__ import annotations

import importlib.util
import json
import pathlib
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[1]
spec = importlib.util.spec_from_file_location("feature_checklist", ROOT / "scripts/feature_checklist.py")
assert spec and spec.loader
module = importlib.util.module_from_spec(spec)
spec.loader.exec_module(module)

class FeatureChecklistTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.data = module.load()
        cls.by_id = {row["id"]: row for row in cls.data["features"]}

    def test_significant_feature_coverage(self):
        self.assertGreaterEqual(len(self.by_id), 100)
        self.assertGreaterEqual(len({x["area"] for x in self.by_id.values()}), 9)
        for prefix in ("build.", "quests.", "shop.", "magic.", "materials.",
                       "skills.", "form.", "monk.", "world.", "end.", "qol.", "food.", "release."):
            self.assertTrue(any(i.startswith(prefix) for i in self.by_id), prefix)

    def test_exact_approved_projecte_gate_proposal(self):
        for id in ("end.projecte_start", "end.projecte_high", "end.expansion", "end.emc_abuse", "end.emc_every_item", "end.projecte_trophies"):
            self.assertIn(id, self.by_id)
            if self.by_id[id]["state"] == "player_pass":
                self.assertTrue(self.by_id[id]["evidence"], "Live ProjectE gate must have linked in-world evidence")
        proposal = (ROOT / "docs/PROJECTE-TIERED-GATING-PROPOSAL.md").read_text(encoding="utf-8")
        for name in ("Wilden Chimera", "Foreman", "Chaos Shard", "botania:life_essence", "100%"):
            self.assertIn(name, proposal)

    def test_reviewable_player_passes(self):
        passed = [row for row in self.by_id.values() if row["state"] == "player_pass"]
        self.assertTrue(passed)
        for row in passed:
            self.assertTrue(row["evidence"].startswith(("docs/", "https://", "tests/")))

    def test_markdown_is_deterministically_generated(self):
        wanted = module.render(self.data)
        actual = (ROOT / "docs/FEATURE-CHECKLIST.md").read_text(encoding="utf-8")
        self.assertEqual(actual, wanted)

    def test_no_qa_pass_claim_for_mod_not_pinned(self):
        mods = json.loads((ROOT / "manifest/mods.json").read_text(encoding="utf-8"))
        selected = {m["id"]: m for m in mods["mods"]}
        self.assertTrue(selected["projecte"]["enabled_in_pack"])
        self.assertTrue(selected["project-expansion"]["enabled_in_pack"])
        # Proposed gate is not enforcement, even though both mods are already loaded.
        for id in ("end.projecte_start", "end.projecte_high"):
            if self.by_id[id]["state"] == "player_pass":
                self.assertTrue(self.by_id[id]["evidence"])
        if "comforts" in selected and not selected["comforts"]["enabled_in_pack"]:
            self.assertNotEqual(self.by_id["qol.comforts"]["state"], "player_pass")

if __name__ == "__main__":
    unittest.main()
