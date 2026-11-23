"""No-network regressions for the exact food publisher JAR census (#444)."""
from io import BytesIO
import json
from pathlib import Path
import sys
import unittest
from zipfile import ZipFile

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "scripts"))
from audit_approved_food_jars import CANDIDATES, PRIORITY, audit_resources, selected_pin

class FoodPublisherTests(unittest.TestCase):
    @staticmethod
    def jar(files):
        content = BytesIO()
        with ZipFile(content, "w") as archive:
            for path, data in files.items():
                archive.writestr(path, json.dumps(data))
        return content.getvalue()

    def test_exact_approved_file_id_filename_and_dependency(self):
        self.assertEqual(len(CANDIDATES), 5)
        pinned = selected_pin("dungeons-delight")[0]
        self.assertEqual(pinned["update"]["curseforge"]["file-id"], 9614702)
        # Published release is 1.5.3 but exact filename remains 1.5.2.
        self.assertEqual(pinned["filename"], "neoforge-dungeonsdelight-1.21.1-1.5.2.jar")
        self.assertEqual(selected_pin("runiclib")[0]["filename"],
                         "neoforge-runiclib-1.21.1-5.0.7.jar")
        self.assertTrue(set(CANDIDATES).isdisjoint(PRIORITY))

    def test_native_recipes_custom_cutting_and_real_tag_resources(self):
        bytes_ = self.jar({
            "data/example/recipe/berry_juice.json":
                {"type": "minecraft:crafting_shapeless",
                 "ingredients": [{"tag": "c:foods/fruit"}],
                 "result": {"id": "example:juice", "count": 2}},
            "data/example/recipe/cut_fish.json":
                {"type": "farmersdelight:cutting",
                 "ingredients": [{"item": "example:fish"}],
                 "results": [{"item": "example:fish_slice", "count": 3}]},
            "data/example/tags/item/raw_fish.json":
                {"values": ["example:fish"], "replace": False},
            "data/example/loot_table/chests/food.json":
                {"pools": []},
            "assets/example/lang/en_us.json":
                {"item.example.juice": "Juice"},
        })
        result = audit_resources(bytes_)
        self.assertEqual(result["resource_counts"],
                         {"item_tag": 1, "loot_table": 1, "recipe": 2})
        self.assertEqual(result["recipe_count"], 2)
        self.assertEqual(result["cutting_recipe_count"], 1)
        self.assertEqual(result["recipe_ids_with_single_result"], 1)
        self.assertEqual(result["candidate_output_ids_not_confirmed_edible"],
                         ["example:fish_slice", "example:juice"])
        self.assertEqual(next(r for r in result["recipes"] if r["recipe"] == "example:cut_fish")["multi_results"][0],
                         {"item": "example:fish_slice", "count": 3})
        self.assertEqual(next(r for r in result["recipes"] if r["recipe"] == "example:berry_juice")["ingredient_tags"],
                         ["c:foods/fruit"])

    def test_unrecognized_custom_recipe_stays_unresolved(self):
        file = self.jar({
            "data/test/recipes/unknown.json": {
                "type": "custom:recipe", "food": "test:magic_pie"},
        })
        r = audit_resources(file)
        self.assertEqual(r["recipe_count"], 1)
        self.assertEqual(r["recipe_ids_with_single_result"], 0)
        self.assertEqual(r["candidate_output_ids_not_confirmed_edible"], [])

    def test_staged_pins_match_publisher_receipts_not_live_pack(self):
        import tomllib
        receipt = json.loads((ROOT / "integration/food-candidates/approved-food-candidates.json")
                             .read_text(encoding="utf-8"))
        self.assertFalse(receipt["installed"])
        self.assertEqual(len(receipt["candidates"]), 5)
        self.assertEqual(receipt["known_food_sources"]["verified_jar_count"], 19)
        for row in receipt["candidates"]:
            staged = ROOT / "integration/food-candidates/pins" / (row["slug"] + ".pw.toml")
            if row["sha1"] is None:
                self.assertFalse(staged.exists(), "Do not stage a guessed Huaxia digest")
                continue
            self.assertTrue(staged.is_file(), row["slug"])
            parsed = tomllib.loads(staged.read_text(encoding="utf-8"))
            self.assertEqual(parsed["filename"], row["filename"])
            self.assertEqual(parsed["download"]["hash"], row["sha1"])
            self.assertEqual(parsed["download"]["hash-format"], "sha1")
            self.assertEqual(parsed["update"]["curseforge"]["file-id"], row["file_id"])
            self.assertEqual(parsed["update"]["curseforge"]["project-id"], row["project_id"])
            self.assertEqual(parsed["side"], "both")
        index = (ROOT / "index.toml").read_text(encoding="utf-8")
        for row in receipt["candidates"]:
            self.assertNotIn('file = "mods/' + row["slug"] + '.pw.toml"', index)

    def test_invalid_zip_rejected(self):
        with self.assertRaises(Exception):
            audit_resources(b"not a jar")

if __name__ == "__main__":
    unittest.main()
