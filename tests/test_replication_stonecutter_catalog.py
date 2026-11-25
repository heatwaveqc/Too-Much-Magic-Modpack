import importlib.util
import unittest
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
SPEC = importlib.util.spec_from_file_location(
    "stonecutter_catalog", ROOT / "scripts" / "build_replication_stonecutter_catalog.py"
)
MODULE = importlib.util.module_from_spec(SPEC)
assert SPEC.loader is not None
SPEC.loader.exec_module(MODULE)


class ReplicationStonecutterCatalogTest(unittest.TestCase):
    def test_effective_outputs_and_anchor_overlap(self):
        capture = {
            "schema": 1,
            "items": [
                {"id": "minecraft:cut_copper", "tags": ["c:storage_blocks/copper"]},
                {"id": "minecraft:stone_stairs", "tags": ["minecraft:stairs"]},
            ],
            "recipes": [
                {
                    "id": "minecraft:cut_copper",
                    "type": "minecraft:stonecutting",
                    "output": {"id": "minecraft:cut_copper", "count": 1},
                },
                {
                    "id": "minecraft:stone_stairs_from_stone_stonecutting",
                    "type": "minecraft:stonecutting",
                    "output": {"id": "minecraft:stone_stairs", "count": 1},
                },
                {
                    "id": "minecraft:stone_stairs",
                    "type": "minecraft:crafting_shaped",
                    "output": {"id": "minecraft:stone_stairs", "count": 4},
                },
                {
                    "id": "replication:matter_values/minecraft/items/cut_copper",
                    "type": "replication:matter_value",
                    "output": {"id": "minecraft:air", "count": 0},
                },
                {
                    "id": "replication:matter_values/minecraft/tags/stairs",
                    "type": "replication:matter_value",
                    "output": {"id": "minecraft:air", "count": 0},
                },
            ],
        }
        rows, summary = MODULE.build_catalog(capture)
        self.assertEqual([row["item_id"] for row in rows], ["minecraft:cut_copper", "minecraft:stone_stairs"])
        self.assertTrue(rows[0]["explicit_item_anchor"])
        self.assertEqual(rows[1]["explicit_tag_anchors"], ["minecraft:stairs"])
        self.assertTrue(rows[1]["has_other_observed_recipe"])
        self.assertEqual(summary["stonecutting_outputs_with_explicit_anchor_count"], 2)

    def test_invalid_capture_fails_closed(self):
        with self.assertRaises(ValueError):
            MODULE.build_catalog({"recipes": []})

    def test_frozen_live_capture_regression(self):
        capture = MODULE.load_capture(
            ROOT / "docs" / "qa" / "surveys-2026-11-18" / "runtime-capture.json.gz"
        )
        rows, summary = MODULE.build_catalog(capture)
        self.assertEqual(summary["observed_recipe_count"], 24857)
        self.assertEqual(summary["observed_stonecutting_recipe_count"], 2503)
        self.assertEqual(summary["unique_stonecutting_output_count"], 1720)
        self.assertEqual(summary["stonecutting_outputs_with_explicit_anchor_count"], 4)
        self.assertEqual(len({row["item_id"] for row in rows}), len(rows))


if __name__ == "__main__":
    unittest.main()
