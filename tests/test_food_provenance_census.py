"""Offline tests for read-only food source census (#444). No Minecraft boot."""
from __future__ import annotations

import hashlib
import json
from pathlib import Path
import tempfile
import tomllib
import unittest
from zipfile import ZipFile

from scripts.food_provenance_census import (
    census, ingredient_tags, legacy_diet, pinned_mods, registry_export,
    resource_type, result_item, selector_kind,
)


class FoodCensusTests(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.addCleanup(self.tmp.cleanup)
        self.root = Path(self.tmp.name) / "pack"
        self.root.mkdir()
        (self.root / "mods").mkdir()
        (self.root / "kubejs/data/test/recipe").mkdir(parents=True)
        (self.root / "kubejs/data/test/tags/item").mkdir(parents=True)
        (self.root / "kubejs/server_scripts").mkdir(parents=True)
        (self.root / "kubejs/server_scripts/runtime_diets.js").write_text(
            "ServerEvents.recipes(event => event.replaceInput(...));\n", encoding="utf-8"
        )
        (self.root / "docs/qa/candidates").mkdir(parents=True)
        (self.root / "docs/qa/candidates/dragon-food-source-pilot-v1.json").write_text(
            json.dumps({"schema": 1, "scope": "pilot",
                        "edibles": [{"item": "test:berry", "evidence": "test"}]}),
            encoding="utf-8"
        )
        (self.root / "kubejs/data/test/recipe/berry_juice.json").write_text(
            json.dumps({"type": "minecraft:crafting_shapeless",
                        "ingredients": [{"item": "test:berry"},
                                        {"tag": "c:foods/fruit"}],
                        "result": {"id": "test:juice", "count": 2}}),
            encoding="utf-8"
        )
        (self.root / "kubejs/data/test/tags/item/berries.json").write_text(
            json.dumps({"values": ["test:berry"]}), encoding="utf-8"
        )
        old = (self.root / "kubejs/data/dragonsurvival/data_maps/dragonsurvival/dragon_species")
        old.mkdir(parents=True)
        (old / "diet_entries.json").write_text(json.dumps({
            "values": {
                "dragonsurvival:forest_dragon": {
                    "neoforge:value": [
                        {"items": "test:berry"},
                        {"items": "#c:foods/fruit"},
                        {"items": "test:.*berry.*"}
                    ]
                }
            }
        }), encoding="utf-8")
        self.jars = Path(self.tmp.name) / "jars"
        self.jars.mkdir()
        self.jar = self.jars / "testfood.jar"
        self.write_jar({
            "data/test/recipes/berry_syrup.json":
                json.dumps({"type": "minecraft:crafting_shapeless",
                            "ingredients": [{"tag": "c:foods/fruit"}],
                            "result": {"item": "test:syrup"}}),
            "data/test/tags/items/edible.json": json.dumps({"values": ["test:berry"]}),
            "data/test/loot_tables/chests/pantry.json": json.dumps({"pools": []}),
        })
        self.write_metadata()

    def write_jar(self, contents):
        with ZipFile(self.jar, "w") as archive:
            for name, value in contents.items():
                archive.writestr(name, value)

    def write_metadata(self, *, indexed_hash=True):
        jar_hash = hashlib.sha1(self.jar.read_bytes()).hexdigest()
        metadata = (
            'name = "Test Food"\nfilename = "testfood.jar"\nside = "both"\n'
            '[download]\nhash-format = "sha1"\n'
            f'hash = "{jar_hash}"\nmode = "metadata:curseforge"\n'
        )
        path = self.root / "mods/test-food.pw.toml"
        path.write_text(metadata, encoding="utf-8")
        digest = hashlib.sha256(path.read_bytes()).hexdigest() if indexed_hash else "0" * 64
        (self.root / "index.toml").write_text(
            'hash-format = "sha256"\n[[files]]\n'
            'file = "mods/test-food.pw.toml"\n'
            f'hash = "{digest}"\nmetafile = true\n', encoding="utf-8"
        )

    def test_readonly_packwiz_scan_never_infers_edible_denominator(self):
        report = census(self.root)
        self.assertEqual(report["scope"], "source_only_incomplete_food_provenance_census")
        self.assertEqual(report["packwiz"]["indexed_mod_pins"], 1)
        self.assertEqual(report["source_pilot"]["count"], 1)
        self.assertEqual(report["legacy_dragon_diet"]["selector_total"], 3)
        self.assertEqual(report["legacy_dragon_diet"]["species"]["dragonsurvival:forest_dragon"]["tag_selectors"], 1)
        self.assertEqual(report["legacy_dragon_diet"]["species"]["dragonsurvival:forest_dragon"]["expression_or_unverified_selectors"], 1)
        self.assertEqual(report["jar_audit"]["counts"], {"not_supplied": 1})
        self.assertIsNone(report["registry_export"]["authoritative_edible_denominator"])
        self.assertIn("test:juice", report["recipe_result_candidates"])
        self.assertIn("test:berry", report["candidate_item_ids_not_an_edible_denominator"])
        self.assertIn("kubejs/server_scripts/runtime_diets.js",
                      report["kubejs_server_scripts_not_statically_evaluated"])
        self.assertEqual(report["resource_counts"]["recipe"], 1)
        self.assertEqual(report["resource_counts"]["item_tag"], 1)

    def test_sha_matched_jar_adds_exact_recipe_tag_loot_evidence(self):
        report = census(self.root, self.jars)
        self.assertEqual(report["jar_audit"]["counts"], {"verified_and_scanned": 1})
        self.assertEqual(report["resource_counts"],
                         {"recipe": 2, "item_tag": 2, "loot_table": 1})
        syrup = next(r for r in report["resources"]["recipe"]
                     if r["result_item"] == "test:syrup")
        self.assertEqual(syrup["ingredient_tag_refs"], ["c:foods/fruit"])
        self.assertEqual(syrup["origin"], "pinned-jar:test-food")
        self.assertIsNone(report["registry_export"]["authoritative_edible_denominator"])

    def test_hash_mismatch_fails_closed_and_does_not_scan(self):
        self.write_jar({
            "data/test/recipes/injected.json": json.dumps({
                "type": "minecraft:crafting_shapeless",
                "result": {"item": "test:fake_food"}})
        })
        result = census(self.root, self.jars)
        self.assertEqual(result["jar_audit"]["counts"], {"hash_mismatch": 1})
        self.assertEqual(result["resource_counts"]["recipe"], 1)
        self.assertNotIn("test:fake_food", result["recipe_result_candidates"])

    def test_unparseable_recipe_outputs_remain_audited(self):
        self.write_jar({
            "data/test/recipes/custom.json": json.dumps({
                "type": "test:unknown_machine",
                "target": {"id": "test:magic_food"}})
        })
        self.write_metadata()
        result = census(self.root, self.jars)
        self.assertEqual(result["jar_audit"]["counts"], {"verified_and_scanned": 1})
        self.assertTrue(any("unresolved recipe output/schema" in s
                            for s in result["unresolved_queue"]))
        self.assertNotIn("test:magic_food", result["recipe_result_candidates"])

    def test_registry_export_needs_build_evidence_and_complete_flag(self):
        file = self.root / "registry.json"
        file.write_text(json.dumps({
            "schema": 1, "build_sha": "a" * 40, "complete": False,
            "items": [
                {"id": "test:berry", "has_food_component": True, "native_only_edible": False},
                {"id": "test:block", "has_food_component": False, "native_only_edible": False},
                {"id": "test:special_food", "has_food_component": False, "native_only_edible": True},
            ]}), encoding="utf-8")
        partial = registry_export(file)
        self.assertEqual(len(partial["observed_edible_items"]), 2)
        self.assertIsNone(partial["authoritative_edible_denominator"])
        data = json.loads(file.read_text(encoding="utf-8"))
        data["complete"] = True
        file.write_text(json.dumps(data), encoding="utf-8")
        final = registry_export(file)
        self.assertEqual(final["authoritative_edible_denominator"], 2)
        data["items"].append(data["items"][0])
        file.write_text(json.dumps(data), encoding="utf-8")
        with self.assertRaises(ValueError):
            registry_export(file)

    def test_metadata_index_mismatch_is_visible_not_repaired(self):
        self.write_metadata(indexed_hash=False)
        pins, warnings = pinned_mods(self.root)
        self.assertEqual(len(pins), 1)
        self.assertIn("indexed metadata digest mismatch: mods/test-food.pw.toml", warnings)
        self.assertNotEqual(pins[0]["download_hash"], "0" * 64)

    def test_resource_identity_helpers_and_tag_recursion(self):
        self.assertEqual(resource_type("data/test/recipes/pies/berry.json"),
                         ("recipe", "test:pies/berry"))
        self.assertEqual(resource_type("data/test/tags/item/fruits/berry.json"),
                         ("item_tag", "test:fruits/berry"))
        self.assertIsNone(resource_type("assets/test/lang/en_us.json"))
        self.assertEqual(selector_kind("#c:foods/fruit"), "tag")
        self.assertEqual(selector_kind("test:.*berry"), "expression_or_unverified")
        self.assertEqual(result_item({"result": {"id": "test:meal", "count": 4}}),
                         ("test:meal", 4))
        self.assertEqual(result_item({"result": {"id": "test:meal", "count": 0}}),
                         (None, None))
        self.assertEqual(ingredient_tags([
            {"item": "test:carrot"},
            {"any_of": [{"tag": "c:foods/fruit"}, {"tag": "c:foods/fish"}]}]),
            {"c:foods/fruit", "c:foods/fish"})


if __name__ == "__main__":
    unittest.main()
