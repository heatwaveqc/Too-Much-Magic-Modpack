"""Offline regressions for the strictly candidate-only owner-locked food budget."""
from __future__ import annotations

from copy import deepcopy
import json
from pathlib import Path
import sys
import unittest

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "scripts"))
from dragon_food_budget import plan


class DragonFoodBudgetTest(unittest.TestCase):
    def setUp(self):
        self.inventory = json.loads((ROOT / "docs/qa/candidates/dragon-food-source-pilot-v1.json").read_text())
        self.policy = json.loads((ROOT / "docs/qa/candidates/dragon-food-approved-budget-pilot-v1.json").read_text())

    def add_root(self, item, family, habitat):
        self.inventory["edibles"].append({"item": item, "evidence": "test fixture source receipt"})
        self.inventory["roots"].append({
            "item": item, "family": family, "habitat": habitat,
            "preparation": "raw", "evidence": "test fixture native registry",
        })

    def add_recipe(self, ident, item, source, kind="farmersdelight:cutting", count=2):
        self.inventory["edibles"].append({"item": item, "evidence": "test fixture recipe output"})
        self.inventory["recipes"].append({
            "id": ident, "type": kind, "output": item, "result_count": count,
            "preparation": "minimally_processed", "ingredients": [{"item": source}],
            "evidence": "test fixture exact recipe and yield",
        })

    def test_pilot_exact_owner_raw_values_and_unresolved_queue(self):
        before = deepcopy(self.inventory)
        result = plan(self.inventory, self.policy)
        self.assertEqual(before, self.inventory)
        self.assertEqual(result["scope"], "numeric_candidate_not_runtime")
        self.assertFalse(result["runtime_map_generated"])
        self.assertFalse(result["global_food_components_changed"])
        self.assertEqual(result["held"], [])
        self.assertEqual(result["unresolved_provenance"], ["minecraft:suspicious_stew"])
        actual = {(v["species"], v["item"]):
                  (v["nutrition"], v["saturation_modifier"], v["theoretical_saturation"])
                  for v in result["values"]}
        self.assertEqual(actual, {
            ("cave_dragon", "minecraft:beef"): (6, 2.0, 24.0),
            ("sea_dragon", "aquaculture:fish_fillet_raw"): (6, 2.0, 24.0),
            ("forest_dragon", "minecraft:carrot"): (6, 2.0, 24.0),
        })

    def test_cutting_native_piece_ratios_no_per_piece_duplication(self):
        examples = [
            ("minecraft:cod", "farmersdelight:cod_slice", "marine_animal", "marine", "sea_dragon", 2, 1, 3),
            ("minecraft:salmon", "farmersdelight:salmon_slice", "marine_animal", "marine", "sea_dragon", 2, 1, 3),
            ("farmersdelight:cabbage", "farmersdelight:cabbage_leaf", "plant_fungus", "terrestrial", "forest_dragon", 2, 1, 3),
            ("minecraft:beef", "farmersdelight:minced_beef", "terrestrial_animal", "terrestrial", "cave_dragon", 3, 2, 4),
        ]
        for source, output, family, habitat, species, whole, piece, expected in examples:
            if source != "minecraft:beef":
                self.add_root(source, family, habitat)
                self.policy["raw"].append({"species": species, "item": source, "evidence": "fixture verified root"})
            recipe = "test:cut_" + output.split(":", 1)[1]
            self.add_recipe(recipe, output, source)
            self.policy["cutting"].append({
                "species": species, "item": output, "source": source, "recipe": recipe,
                "native_whole_nutrition": whole, "native_piece_nutrition": piece,
                "output_count": 2, "evidence": "fixture native-cut ratios",
            })
        result = plan(self.inventory, self.policy)
        self.assertFalse(result["held"])
        actual = {(v["species"], v["item"]): v["nutrition"] for v in result["values"]}
        for source, output, family, habitat, species, whole, piece, expected in examples:
            self.assertEqual(actual[(species, output)], expected)
        self.assertEqual(actual[("cave_dragon", "farmersdelight:minced_beef")] * 2, 8)

    def test_two_fruits_one_juice_conserves_both(self):
        self.add_root("test:orange", "plant_fungus", "terrestrial")
        self.inventory["edibles"].append({"item": "test:orange_juice", "evidence": "test juice fixture"})
        self.inventory["recipes"].append({
            "id": "test:orange_juice", "type": "minecraft:crafting_shapeless",
            "output": "test:orange_juice", "result_count": 1, "preparation": "minimally_processed",
            "ingredients": [{"item": "test:orange", "count": 2}],
            "evidence": "fixture single-output, no reusable tool counted",
        })
        self.policy["raw"].append({"species": "forest_dragon", "item": "test:orange", "evidence": "test root"})
        self.policy["combinations"].append({
            "species": "forest_dragon", "item": "test:orange_juice", "recipe": "test:orange_juice",
            "ingredients": [{"item": "test:orange", "count": 2}], "evidence": "fixture recipe",
        })
        report = plan(self.inventory, self.policy)
        self.assertFalse(report["held"])
        juice = next(x for x in report["values"] if x["item"] == "test:orange_juice")
        self.assertEqual((juice["nutrition"], juice["saturation_modifier"], juice["theoretical_saturation"]),
                         (12, 2.0, 48.0))

    def test_unknown_or_marine_plant_cannot_receive_automatic_bonus(self):
        self.policy["raw"].extend([
            {"species": "forest_dragon", "item": "minecraft:seagrass", "evidence": "habitat mismatch"},
            {"species": "sea_dragon", "item": "minecraft:seagrass", "evidence": "numeric sea plant hold"},
            {"species": "forest_dragon", "item": "minecraft:suspicious_stew", "evidence": "unresolved"},
            {"species": "cave_dragon", "item": "minecraft:carrot", "evidence": "off diet"},
        ])
        report = plan(self.inventory, self.policy)
        self.assertEqual(len(report["values"]), 3)
        self.assertEqual(len(report["held"]), 4)

    def test_heat_rejected_even_if_falsely_marked_minimal(self):
        self.add_recipe("test:cooked_cut", "test:bad_cut", "minecraft:beef", kind="minecraft:smelting")
        self.policy["cutting"].append({
            "species": "cave_dragon", "item": "test:bad_cut", "source": "minecraft:beef",
            "recipe": "test:cooked_cut", "native_whole_nutrition": 3,
            "native_piece_nutrition": 2, "output_count": 2, "evidence": "disallowed heat",
        })
        report = plan(self.inventory, self.policy)
        self.assertEqual(len(report["held"]), 1)
        self.assertNotIn("test:bad_cut", [x["item"] for x in report["values"]])

    def test_fractional_rounding_and_mismatched_yields_fail_closed(self):
        self.add_recipe("test:odd_cut", "test:odd", "minecraft:beef")
        self.policy["cutting"].extend([
            {"species": "cave_dragon", "item": "test:odd", "source": "minecraft:beef",
             "recipe": "test:odd_cut", "native_whole_nutrition": 4,
             "native_piece_nutrition": 1, "output_count": 2, "evidence": "fixture fractional"},
            {"species": "cave_dragon", "item": "test:wrong_yield", "source": "minecraft:beef",
             "recipe": "test:odd_cut", "native_whole_nutrition": 3,
             "native_piece_nutrition": 2, "output_count": 3, "evidence": "wrong output"},
        ])
        report = plan(self.inventory, self.policy)
        self.assertEqual(len(report["held"]), 2)
        self.assertNotIn("test:odd", [x["item"] for x in report["values"]])

    def test_reversible_recipe_does_not_amplify_canonical_raw(self):
        self.add_recipe("test:cabbage_reverse", "test:carrot_juice", "minecraft:carrot", count=1)
        self.policy["combinations"].append({
            "species": "forest_dragon", "item": "minecraft:carrot",
            "recipe": "test:cabbage_reverse", "ingredients": [{"item": "test:carrot_juice", "count": 1}],
            "evidence": "no new canonical carrot",
        })
        report = plan(self.inventory, self.policy)
        self.assertEqual(len(report["values"]), 3)
        self.assertEqual(len(report["held"]), 1)


if __name__ == "__main__":
    unittest.main()
