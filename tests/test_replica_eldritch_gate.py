"""Frozen source-level safety checks for the approved Replica Ore gate (#448)."""
from __future__ import annotations

import json
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
BIOME = ROOT / "kubejs/data/replication/neoforge/biome_modifier/replica_ore.json"
MINER = ROOT / "kubejs/data/tmm/recipe/miner/replica_ore_eldritch.json"
INDEX = ROOT / "index.toml"

class ReplicaEldritchGateSourceTest(unittest.TestCase):
    def test_native_biome_modifier_is_disabled_by_same_id(self):
        self.assertEqual(json.loads(BIOME.read_text(encoding="utf-8")), {"type": "neoforge:none"})

    def test_eldritch_miner_still_produces_real_ore(self):
        recipe = json.loads(MINER.read_text(encoding="utf-8"))
        self.assertEqual(recipe["type"], "occultism:miner")
        self.assertEqual(recipe["ingredient"], {"tag": "occultism:miners/eldritch"})
        self.assertEqual(recipe["result"]["type"], "occultism:weighted_item")
        self.assertEqual(recipe["result"]["stack"], {
            "count": 1, "id": "replication:deepslate_replica_ore"})
        self.assertEqual(recipe["result"]["weight"], 90)

    def test_both_resources_are_in_actual_packwiz_index(self):
        text = INDEX.read_text(encoding="utf-8")
        for path in (BIOME, MINER):
            self.assertIn(f'file = "{path.relative_to(ROOT).as_posix()}"', text)

if __name__ == "__main__":
    unittest.main()
