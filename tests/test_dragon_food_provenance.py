"""Offline regression coverage for the #444 source-only Dragon food model."""
import copy
import importlib.util
import json
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
IMPL = ROOT / "scripts/dragon_food_provenance.py"
SEED = ROOT / "docs/qa/candidates/dragon-food-source-pilot-v1.json"
SPEC = importlib.util.spec_from_file_location("dragon_food_provenance", IMPL)
module = importlib.util.module_from_spec(SPEC)
import sys
sys.modules[SPEC.name] = module
SPEC.loader.exec_module(module)


def fixture():
    return json.loads(SEED.read_text(encoding="utf-8"))


def edible(data, item):
    data["edibles"].append({"item": item, "evidence": "Type A fixture with explicit source provenance"})


def recipe(data, item, ingredients, preparation=None, kind="minecraft:crafting_shapeless"):
    ident = f"fixture:recipe_{len(data['recipes'])}"
    row = {
        "id": ident, "type": kind, "output": item,
        "ingredients": ingredients, "evidence": f"Regression fixture {ident}",
    }
    if preparation is not None:
        row["preparation"] = preparation
    data["recipes"].append(row)


class DragonFoodModelTests(unittest.TestCase):
    def test_pilot_honors_three_families_and_flagged_unresolved_item(self):
        result = module.FoodModel(fixture()).report()
        by_id = {r["item"]: r for r in result["records"]}
        self.assertEqual(result["scope"], "source_pilot_not_full_registry_census")
        self.assertEqual(result["count"], 9)
        self.assertEqual(result["unresolved"], 1)
        self.assertEqual(by_id["minecraft:suspicious_stew"]["status"], "unresolved")
        self.assertEqual(
            by_id["minecraft:cooked_beef"]["variants"][0]["components"],
            {"terrestrial_animal": 1},
        )
        self.assertEqual(by_id["minecraft:cooked_beef"]["variants"][0]["preparation"], "cooked")
        self.assertEqual(
            by_id["aquaculture:sushi"]["variants"][0]["components"],
            {"marine_animal": 1, "plant_fungus": 1},
        )
        self.assertEqual(by_id["aquaculture:sushi"]["variants"][0]["marine_plant_units"], 1)
        self.assertTrue(by_id["minecraft:seagrass"]["dragon_only_edible"])
        self.assertFalse(result["runtime_map_generated"])
        self.assertFalse(result["numeric_food_values_generated"])

    def test_nonthermal_raw_grinding_keeps_terrestrial_source(self):
        data = fixture()
        edible(data, "fixture:ground_beef")
        recipe(data, "fixture:ground_beef", [{"item": "minecraft:beef"}],
               preparation="minimally_processed")
        result = module.FoodModel(data).report()
        entry = next(r for r in result["records"] if r["item"] == "fixture:ground_beef")
        self.assertEqual(entry["status"], "resolved")
        self.assertEqual(entry["variants"][0]["preparation"], "minimally_processed")
        self.assertEqual(entry["variants"][0]["components"], {"terrestrial_animal": 1})

    def test_multiple_cooking_stages_are_distinct_and_auditable(self):
        data = fixture()
        edible(data, "fixture:roasted")
        edible(data, "fixture:charred")
        recipe(data, "fixture:roasted", [{"item": "minecraft:beef"}],
               kind="minecraft:smoking")
        recipe(data, "fixture:charred", [{"item": "fixture:roasted"}],
               preparation="charred")
        report = module.FoodModel(data).report()
        by_id = {r["item"]: r for r in report["records"]}
        self.assertEqual(by_id["fixture:roasted"]["variants"][0]["preparation"], "cooked")
        self.assertEqual(by_id["fixture:charred"]["variants"][0]["preparation"], "charred")
        self.assertEqual(by_id["fixture:charred"]["variants"][0]["components"],
                         {"terrestrial_animal": 1})

    def test_only_explicit_reusable_tool_is_excluded(self):
        data = fixture()
        tool = "fixture:pan"
        data["reusable_tools"].append(tool)
        edible(data, "fixture:meal")
        recipe(data, "fixture:meal", [
            {"item": tool},
            {"item": "minecraft:carrot", "count": 2},
            {"item": "minecraft:beef"},
        ], preparation="cooked")
        report = module.FoodModel(data).report()
        entry = next(r for r in report["records"] if r["item"] == "fixture:meal")
        self.assertEqual(entry["variants"][0]["components"],
                         {"plant_fungus": 2, "terrestrial_animal": 1})
        self.assertEqual(entry["status"], "resolved")
        # Without an actual reuse declaration the same item is unknown, not
        # silently mistaken for a reusable tool based on its name.
        data["reusable_tools"].clear()
        other = module.FoodModel(data).report()
        entry = next(r for r in other["records"] if r["item"] == "fixture:meal")
        self.assertEqual(entry["status"], "unresolved")

    def test_tag_and_recipe_alternatives_remain_separate(self):
        data = fixture()
        edible(data, "fixture:stock")
        data["tags"]["fixture:broth_sources"] = ["minecraft:carrot", "minecraft:beef"]
        recipe(data, "fixture:stock", [{"tag": "fixture:broth_sources"}],
               preparation="cooked")
        entry = next(r for r in module.FoodModel(data).report()["records"]
                     if r["item"] == "fixture:stock")
        comps = [v["components"] for v in entry["variants"]]
        self.assertIn({"plant_fungus": 1}, comps)
        self.assertIn({"terrestrial_animal": 1}, comps)
        self.assertTrue(entry["multiple_family_outcomes"])
        self.assertTrue(entry["multiple_possible_provenances"])

    def test_nested_any_of_counts_preserve_alternative_material_quantity(self):
        data = fixture()
        edible(data, "fixture:bulk")
        recipe(data, "fixture:bulk", [
            {"any_of": [
                {"item": "minecraft:beef"},
                {"item": "minecraft:carrot"},
            ], "count": 3}
        ], preparation="minimally_processed")
        entry = next(r for r in module.FoodModel(data).report()["records"]
                     if r["item"] == "fixture:bulk")
        self.assertEqual(entry["status"], "resolved")
        self.assertEqual(len(entry["variants"]), 2)
        self.assertIn({"terrestrial_animal": 3},
                      [v["components"] for v in entry["variants"]])
        self.assertIn({"plant_fungus": 3},
                      [v["components"] for v in entry["variants"]])

    def test_partly_unresolved_tag_never_looks_fully_resolved(self):
        data = fixture()
        edible(data, "fixture:partial_broth")
        data["tags"]["fixture:broth_options"] = [
            "minecraft:carrot", "fixture:unknown_stock_source"
        ]
        recipe(data, "fixture:partial_broth", [
            {"tag": "fixture:broth_options"}
        ], "cooked")
        result = module.FoodModel(data).report()
        entry = next(r for r in result["records"] if r["item"] == "fixture:partial_broth")
        self.assertEqual(entry["status"], "partial")
        self.assertEqual(entry["variants"][0]["components"], {"plant_fungus": 1})
        self.assertIn("unresolved_recipe_ingredient",
                      {x["reason"] for x in entry["issues"]})

    def test_two_recipe_sources_for_one_output_do_not_collapse(self):
        data = fixture()
        edible(data, "fixture:food")
        recipe(data, "fixture:food", [{"item": "minecraft:beef"}], "raw")
        recipe(data, "fixture:food", [{"item": "minecraft:carrot"}], "raw")
        entry = next(r for r in module.FoodModel(data).report()["records"]
                     if r["item"] == "fixture:food")
        self.assertEqual(len(entry["variants"]), 2)
        self.assertEqual(entry["status"], "resolved")
        self.assertTrue(entry["multiple_family_outcomes"])

    def test_missing_tag_or_unknown_item_fails_closed_with_queue(self):
        data = fixture()
        edible(data, "fixture:unknown")
        recipe(data, "fixture:unknown", [{"tag": "c:foods/not_censused"}], "raw")
        report = module.FoodModel(data).report()
        entry = next(r for r in report["records"] if r["item"] == "fixture:unknown")
        self.assertEqual(entry["status"], "unresolved")
        self.assertEqual(entry["variants"], [])
        reasons = {issue["reason"] for issue in report["issues"]}
        self.assertIn("unresolved_tag", reasons)
        self.assertIn("incomplete_recipe_inputs", reasons)

    def test_cycle_is_reported_without_inventing_a_food(self):
        data = fixture()
        edible(data, "fixture:a")
        recipe(data, "fixture:a", [{"item": "fixture:b"}], "raw")
        recipe(data, "fixture:b", [{"item": "fixture:a"}], "raw")
        result = module.FoodModel(data).report()
        record = next(r for r in result["records"] if r["item"] == "fixture:a")
        self.assertEqual(record["status"], "unresolved")
        self.assertIn("recipe_cycle", {issue["reason"] for issue in result["issues"]})

    def test_untyped_crafting_does_not_imply_uncooked_or_cooked(self):
        data = fixture()
        edible(data, "fixture:unknown_prep")
        recipe(data, "fixture:unknown_prep", [{"item": "minecraft:beef"}])
        result = module.FoodModel(data).report()
        item = next(r for r in result["records"] if r["item"] == "fixture:unknown_prep")
        self.assertEqual(item["status"], "unresolved")
        self.assertIn("unclassified_preparation",
                      {i["reason"] for i in result["issues"]})

    def test_large_alternative_set_fails_closed_not_truncated(self):
        data = fixture()
        edible(data, "fixture:too_many")
        data["tags"]["fixture:large"] = [
            f"fixture:ingredient{i}" for i in range(module.MAX_VARIANTS + 1)
        ]
        for item in data["tags"]["fixture:large"]:
            data["roots"].append({
                "item": item, "family": "plant_fungus", "preparation": "raw",
                "evidence": "Synthetic fixture root"
            })
        recipe(data, "fixture:too_many", [{"tag": "fixture:large"}], "raw")
        result = module.FoodModel(data).report()
        entry = next(r for r in result["records"] if r["item"] == "fixture:too_many")
        self.assertEqual(entry["status"], "unresolved")
        self.assertIn("variant_limit_exceeded", {i["reason"] for i in result["issues"]})

    def test_malformed_inputs_and_duplicate_registry_entries_are_rejected(self):
        data = fixture()
        data["edibles"].append(copy.deepcopy(data["edibles"][0]))
        with self.assertRaises(ValueError):
            module.FoodModel(data)
        data = fixture()
        data["recipes"][0]["ingredients"] = [{"tag": "c:a", "item": "minecraft:beef"}]
        with self.assertRaises(ValueError):
            module.FoodModel(data)

    def test_output_is_stable_across_repeated_runs(self):
        data = fixture()
        a = module.FoodModel(data).report()
        b = module.FoodModel(copy.deepcopy(data)).report()
        self.assertEqual(json.dumps(a, sort_keys=True), json.dumps(b, sort_keys=True))
        self.assertRegex(a["input_sha256"], r"^[0-9a-f]{64}$")


if __name__ == "__main__":
    unittest.main()
