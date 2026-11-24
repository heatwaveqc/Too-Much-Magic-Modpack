"""No-network regressions for the exact food publisher JAR census (#444)."""
from io import BytesIO
import hashlib
import tempfile
import json
from pathlib import Path
import sys
import unittest
from zipfile import ZipFile

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "scripts"))
from audit_approved_food_jars import CANDIDATES, PRIORITY, audit_resources, audit_one, selected_pin

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
        self.assertEqual(receipt["known_food_sources"]["verified_jar_count"], 20)
        self.assertEqual(receipt["known_food_sources"]["publisher_verified_jars"], 19)
        self.assertEqual(receipt["known_food_sources"]["owner_uploaded_source_jars"], 1)
        self.assertEqual(receipt["known_food_sources"]["checked_native_recipe_json_files"], 2819)
        for row in receipt["candidates"]:
            staged = ROOT / "integration/food-candidates/pins" / (row["slug"] + ".pw.toml")
            self.assertTrue(staged.is_file(), row["slug"])
            parsed = tomllib.loads(staged.read_text(encoding="utf-8"))
            self.assertEqual(parsed["filename"], row["filename"])
            self.assertEqual(parsed["download"]["hash"], row["sha1"])
            self.assertEqual(parsed["download"]["hash-format"], "sha1")
            self.assertEqual(parsed["update"]["curseforge"]["file-id"], row["file_id"])
            self.assertEqual(parsed["update"]["curseforge"]["project-id"], row["project_id"])
            self.assertEqual(parsed["side"], "both")
        index = (ROOT / "index.toml").read_text(encoding="utf-8")
        huaxia = next(r for r in receipt["candidates"] if r["slug"] == "huaxia-delight")
        self.assertEqual(huaxia["native_mod_id"], "huaxiadelight")
        self.assertEqual(huaxia["provenance"],
                         "user_uploaded_jar_sha_computed_not_independent_publisher_digest")
        self.assertEqual(huaxia["sha1"], "8c4eb34973a6f2ca5d0aac29e1bdff065bfc6919")
        for row in receipt["candidates"]:
            self.assertNotIn('file = "mods/' + row["slug"] + '.pw.toml"', index)

    def test_huaxia_style_nested_cutting_and_conditional_receipts(self):
        file = self.jar({
            "data/huaxiadelight/recipe/cutting/chopped_scallion.json": {
                "type": "farmersdelight:cutting",
                "ingredients": [{"tag": "huaxiadelight:scallion"}],
                "tool": {"item": "huaxiadelight:slicing_knife"},
                "result": [{"item": {"id": "huaxiadelight:chopped_scallion", "count": 4}}],
                "neoforge:conditions": [{"type": "neoforge:not",
                                        "value": {"type": "neoforge:tag_empty",
                                                  "tag": "huaxiadelight:scallion"}}]
            }
        })
        r = audit_resources(file)
        self.assertEqual(r["recipe_count"], 1)
        self.assertEqual(r["cutting_recipe_count"], 1)
        self.assertEqual(r["candidate_output_ids_not_confirmed_edible"],
                         ["huaxiadelight:chopped_scallion"])
        recipe = r["recipes"][0]
        self.assertEqual(recipe["multi_results"], [
            {"item": "huaxiadelight:chopped_scallion", "count": 4}])
        self.assertEqual(recipe["ingredient_tags"], ["huaxiadelight:scallion"])
        self.assertEqual(recipe["tool"], {"item": "huaxiadelight:slicing_knife"})
        self.assertEqual(len(recipe["native_conditions"]), 1)

    def test_local_user_upload_keeps_distinct_evidence_from_publisher(self):
        with tempfile.TemporaryDirectory() as base:
            path = Path(base) / "huaxia-delight-1.21.1-1.2.0.jar"
            meta = """modLoader="javafml"
loaderVersion="[4,)"
license="All Rights Reserved"
[[mods]]
modId="huaxiadelight"
version="1.2.0"
"""
            with ZipFile(path, "w") as archive:
                archive.writestr("META-INF/neoforge.mods.toml", meta)
                archive.writestr("data/huaxiadelight/recipe/cutting/sample.json",
                                json.dumps({"type": "farmersdelight:cutting",
                                            "result": [{"item": {
                                                "id": "huaxiadelight:chopped_scallion",
                                                "count": 4}}]}))
            result = audit_one("huaxia-delight", path)
            self.assertEqual(result["status"], "verified_user_upload")
            self.assertEqual(result["metadata"]["mod_ids"], ["huaxiadelight"])
            self.assertIsNone(result["publisher_checksum_independently_matched"])
            self.assertEqual(result["source_evidence"]["cutting_recipe_count"], 1)
            self.assertEqual(result["sha1"], hashlib.sha1(path.read_bytes()).hexdigest())
            self.assertTrue(result["native_version_matches_approved"])
            with ZipFile(path, "w") as archive:
                archive.writestr("META-INF/neoforge.mods.toml",
                                meta.replace('version="1.2.0"', 'version="1.2.1"'))
            mismatched = audit_one("huaxia-delight", path)
            self.assertEqual(mismatched["status"], "held")
            self.assertFalse(mismatched["native_version_matches_approved"])

    def test_uploaded_huaxia_native_food_source_list_is_not_nutrition_map(self):
        d = json.loads((ROOT / "docs/qa/candidates/huaxia-native-food-source-v1.json")
                       .read_text(encoding="utf-8"))
        self.assertFalse(d["verified_in_game"])
        self.assertFalse(d["runtime_data_generated"])
        native = d["source_native_food_items"]
        regen = d["source_native_regeneration_effect_items"]
        bowl = d["native_returns_bowl_items"]
        nonfood = d["source_non_food_direct_ingredients"]
        self.assertEqual((len(native), len(regen), len(bowl), len(nonfood)),
                         (51, 29, 22, 11))
        self.assertEqual(len(set(native)), len(native))
        self.assertTrue(set(regen).issubset(native))
        self.assertTrue(set(bowl).issubset(native))
        self.assertTrue(set(native).isdisjoint(nonfood))
        self.assertEqual(d["provenance"]["sha1"],
                         "8c4eb34973a6f2ca5d0aac29e1bdff065bfc6919")
        self.assertNotIn("nutrition_values", d)
        receipt = json.loads((ROOT / "integration/food-candidates/approved-food-candidates.json")
                             .read_text(encoding="utf-8"))
        uploaded = next(x for x in receipt["candidates"] if x["slug"] == "huaxia-delight")
        self.assertEqual(uploaded["native_food_component_registration_count"], 51)
        self.assertEqual(uploaded["native_regeneration_food_items"], 29)

    def test_invalid_zip_rejected(self):
        with self.assertRaises(Exception):
            audit_resources(b"not a jar")

if __name__ == "__main__":
    unittest.main()
