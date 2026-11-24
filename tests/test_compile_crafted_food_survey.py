import importlib.util
import json
from pathlib import Path
import tempfile
import unittest

ROOT = Path(__file__).resolve().parents[1]
spec = importlib.util.spec_from_file_location(
    "compiler", ROOT / "scripts/compile_crafted_food_survey.py")
m = importlib.util.module_from_spec(spec)
spec.loader.exec_module(m)


def source(item, family="#c:foods/fruit", units=1):
    return {"schema": 1, "item": item, "source_units": units,
            "provenance": "test:source",
            "inferred_tags": [family, "#tmm:foods/raw"]}


def recipe(output, receipt, ingredients, station="minecraft:crafting_shapeless"):
    return {"recipe": receipt, "type": station,
            "ingredient_inputs": ingredients,
            "outputs": [{"item": output, "count": 1}]}


def audit(recipes, tags=(), tag_outputs=True):
    # Most unit cases exercise deliberately food-tagged synthetic results.
    # To test strict gating, pass tag_outputs=False explicitly.
    targets = sorted({
        value["item"] for recipe in recipes for value in recipe["outputs"]
    }) if tag_outputs else []
    tags = list(tags) + ([{"tag": "c:foods", "values": targets}] if targets else [])
    return {"releases": [{"slug": "test", "native_versions": {"test": "1"},
                          "source_evidence": {"recipes": recipes, "item_tags": tags}}]}


class CompilerTest(unittest.TestCase):
    def test_alternate_tag_members_stay_distinct_for_worst_diet(self):
        roots = {"test:fish": source("test:fish", "#c:foods/raw_fish"),
                 "test:rice": source("test:rice", "#tmm:foods/grains")}
        sushi = recipe("test:sushi", "test:recipe/sushi",
                       [{"tag": "c:tool_cuttingboard"}, {"tag": "c:filling"}])
        files, report = m.process(audit([sushi], [
            {"tag": "c:filling", "values": ["test:fish", "test:rice"]}]), roots, {})
        value = files[str(m.ROUTES / "test/sushi.json")]
        self.assertEqual(len(value["routes"]), 2)
        self.assertEqual({r["receipt"] for r in value["routes"]}, {"test:recipe/sushi"})
        self.assertNotIn("nbt", json.dumps(value))
        self.assertEqual(report["generated_candidate_recipe_outputs"], 1)

    def test_two_oranges_one_juice_and_neutral_tool(self):
        juice = recipe("test:juice", "test:juice",
                       [{"tag": "c:tool_juicer"}, {"tag": "c:orange"}, {"tag": "c:orange"}])
        data, _ = m.process(audit([juice], [
            {"tag": "c:orange", "values": ["test:orange"]}]),
            {"test:orange": source("test:orange")}, {})
        self.assertEqual(data[str(m.ROUTES / "test/juice.json")]["routes"][0]["ingredients"],
                         [{"item": "test:orange", "count": 2}])
        self.assertEqual(data[str(m.ROUTES / "test/juice.json")]["routes"][0]["method"], "nonthermal")

    def test_hot_tool_and_meatball_finished_identity(self):
        r = recipe("test:fishmeatballsitem", "test:meatball",
                   [{"tag": "c:tool_saucepan"}, {"item": "test:fish"}])
        data, _ = m.process(audit([r]), {
            "test:fish": source("test:fish", "#c:foods/raw_fish")}, {})
        v = data[str(m.ROUTES / "test/fishmeatballsitem.json")]
        self.assertEqual(v["identity"], "unfavored_all")
        self.assertEqual(v["routes"][0]["method"], "heat")

    def test_real_food_source_holds_name_missing_ingredients(self):
        # The tag identifies the finished edible candidate; the reason names
        # exactly which underlying source IDs we must classify next.
        meal = recipe("test:meal", "test:meal", [
            {"item": "test:fish"}, {"item": "test:missing_sauce"},
            {"item": "test:missing_sauce"}])
        generated, report = m.process(audit([meal]), {
            "test:fish": source("test:fish", "#c:foods/raw_fish")}, {})
        self.assertNotIn(str(m.ROUTES / "test/meal.json"), generated)
        self.assertEqual(
            report["unresolved_source_inputs_by_tagged_food"]["test:meal"],
            ["test:missing_sauce"])
        self.assertEqual(
            report["missing_source_input_occurrences"]["test:missing_sauce"], 1)
        self.assertEqual(report["untagged_recipe_outputs_by_namespace"], {})

    def test_untagged_output_count_does_not_bloat_food_hold_ledger(self):
        raw = audit([
            recipe("test:edible", "test:edible", [{"item": "test:fruit"}]),
            recipe("another:box", "another:box", [{"item": "test:fruit"}]),
        ], [{"tag": "c:foods", "values": ["test:edible"]}], tag_outputs=False)
        _, report = m.process(raw, {"test:fruit": source("test:fruit")}, {})
        self.assertEqual(report["generated_ids"], ["test:edible"])
        self.assertEqual(report["untagged_recipe_outputs_by_namespace"],
                         {"another": 1})
        self.assertEqual(report["held_output_ids"], 0)

    def test_incomplete_alternative_holds_the_whole_finished_item(self):
        roots = {"test:fish": source("test:fish", "#c:foods/raw_fish")}
        routes = [recipe("test:meal", "test:good", [{"item": "test:fish"}]),
                  recipe("test:meal", "test:bad", [{"item": "test:unknown"}])]
        data, summary = m.process(audit(routes), roots, {})
        self.assertNotIn(str(m.ROUTES / "test/meal.json"), data)
        self.assertEqual(summary["held_outputs"]["test:meal"], "unresolved_source_input")

    def test_neoforge_compound_difference_tag_cycle_and_unknown_codec(self):
        index = m.TagIndex([{"source_evidence": {"item_tags": [
            {"tag": "c:all", "values": ["test:fish", "test:rice"]}]}}])
        self.assertEqual(index.ingredient({
            "type": "neoforge:difference", "base": {"tag": "c:all"},
            "subtracted": {"item": "test:rice"}}), ("test:fish",))
        self.assertEqual(index.ingredient({
            "type": "neoforge:compound",
            "children": [{"item": "test:fish"}, {"item": "test:rice"}]}),
            ("test:fish", "test:rice"))
        with self.assertRaises(m.Hold):
            index.ingredient({"type": "unverified:codec", "values": []})
        cycle = m.TagIndex([{"source_evidence": {"item_tags": [
            {"tag": "c:a", "values": ["#c:b"]},
            {"tag": "c:b", "values": ["#c:a"]}]}}])
        with self.assertRaisesRegex(m.Hold, "tag_cycle"):
            cycle.expand("c:a")

    def test_signature_compression_only_for_equal_verified_sources(self):
        tags = [{"tag": "c:berries", "values": ["test:orange", "test:apple"]}]
        r = recipe("test:juice", "test:juice", [{"tag": "c:berries"}])
        roots = {"test:orange": source("test:orange"),
                 "test:apple": source("test:apple")}
        data, _ = m.process(audit([r], tags), roots, {})
        self.assertEqual(len(data[str(m.ROUTES / "test/juice.json")]["routes"]), 1)
        roots["test:apple"]["source_units"] = 0.5
        data, _ = m.process(audit([r], tags), roots, {})
        self.assertEqual(len(data[str(m.ROUTES / "test/juice.json")]["routes"]), 2)

    def test_malformed_native_tag_is_held_without_poisoning_unrelated_food(self):
        raw = audit([
            recipe("test:bad_soup", "test:bad", [{"tag": "c:bad"}]),
            recipe("test:good_soup", "test:good", [{"item": "test:fish"}]),
        ], [{"tag": "c:bad", "values": ["test:fish", "minecraft:INVALID"]}])
        files, report = m.process(raw, {
            "test:fish": source("test:fish", "#c:foods/raw_fish")}, {})
        self.assertEqual(report["held_outputs"]["test:bad_soup"],
                         "malformed_native_tag:c:bad")
        self.assertIn(str(m.ROUTES / "test/good_soup.json"), files)

    def test_only_food_tagged_final_outputs_emit_recipes_and_helper_provenance(self):
        puree = recipe("test:fruit_puree", "test:puree",
                       [{"item": "test:fruit"}])
        juice = recipe("test:juice", "test:juice",
                       [{"item": "test:fruit_puree"}])
        decoration = recipe("test:decoration", "test:decoration",
                            [{"item": "test:fruit"}])
        tags = [{"tag": "c:foods", "values": ["test:juice"]}]
        generated, report = m.process(audit([puree, juice, decoration],
                                            tags, tag_outputs=False),
                                      {"test:fruit": source("test:fruit")}, {})
        self.assertEqual(report["generated_candidate_recipe_outputs"], 1)
        self.assertEqual(report["generated_support_recipe_outputs"], 1)
        self.assertEqual(report["emitted_recipe_route_records"], 2)
        self.assertEqual(report["skipped_untagged_recipe_outputs"], 2)
        self.assertEqual(report["generated_ids"], ["test:juice"])
        self.assertEqual(report["support_ids"], ["test:fruit_puree"])
        self.assertNotIn(str(m.ROUTES / "test/decoration.json"), generated)
        self.assertIn(str(m.ROUTES / "test/fruit_puree.json"), generated)
        self.assertIn(str(m.ROUTES / "test/juice.json"), generated)
        self.assertEqual(
            generated[str(m.TAGS / "crafted_candidate.json")]["values"],
            ["test:juice"])

    def test_untagged_foodlike_name_does_not_create_food_evidence(self):
        meal = recipe("test:fancy_soup", "test:recipe_soup",
                      [{"item": "test:fruit"}])
        generated, report = m.process(audit([meal], tag_outputs=False),
                                      {"test:fruit": source("test:fruit")}, {})
        self.assertNotIn(str(m.ROUTES / "test/fancy_soup.json"), generated)
        self.assertEqual(report["generated_candidate_recipe_outputs"], 0)
        self.assertEqual(report["held_output_ids"], 0)

    def test_seed_and_container_outputs_are_not_automatically_food(self):
        seed = recipe("test:seed", "test:grow_seed", [{"item": "test:fruit"}])
        bottle = recipe("minecraft:glass_bottle", "test:bottle",
                        [{"item": "test:fruit"}])
        meal = recipe("test:fruit_juice", "test:fruit_juice",
                      [{"item": "test:fruit"}])
        tags = [
            {"tag": "minecraft:villager_plantable_seeds", "values": ["test:seed"]},
            {"tag": "c:juices", "values": ["test:fruit_juice"]},
        ]
        generated, summary = m.process(audit([seed, bottle, meal], tags),
                                       {"test:fruit": source("test:fruit")}, {})
        self.assertNotIn(str(m.ROUTES / "test/seed.json"), generated)
        self.assertNotIn(str(m.ROUTES / "minecraft/glass_bottle.json"), generated)
        self.assertIn(str(m.ROUTES / "test/fruit_juice.json"), generated)
        self.assertEqual(summary["native_food_tagged_candidates"], 1)
        self.assertEqual(summary["excluded_known_nonfood_or_vanilla_outputs"], 2)

    def test_cold_recipe_with_cooked_name_is_held_not_assumed_raw(self):
        r = recipe("test:grilledskewers", "test:grilled",
                   [{"item": "test:carrot"}])
        generated, report = m.process(audit([r]), {
            "test:carrot": source("test:carrot", "#c:foods/vegetable")}, {})
        self.assertNotIn(str(m.ROUTES / "test/grilledskewers.json"), generated)
        self.assertEqual(report["held_outputs"]["test:grilledskewers"],
                         "ambiguous_thermal_prep")

    def test_material_tags_generate_source_records_not_native_food_components(self):
        tags = m.TagIndex([{"source_evidence": {"item_tags": [
            {"tag": "c:crops/rice", "values": ["test:rice"]},
            {"tag": "c:mushrooms", "values": ["test:fungus"]}]}}])
        out = m.material_sources(tags, {}, {"test", "minecraft"})
        self.assertEqual(out["test:rice"]["inferred_tags"],
                         ["#tmm:foods/grains", "#tmm:foods/raw"])
        self.assertIn("minecraft:kelp", out)
        self.assertNotIn("nutrition", out["test:rice"])
        self.assertNotIn("edible", out["test:rice"])

    def test_pin_check_rejects_wrong_hash(self):
        with tempfile.TemporaryDirectory() as dirname:
            directory = Path(dirname) / "mods"
            directory.mkdir()
            (directory / "test.pw.toml").write_text(
                'filename = "test.jar"\n[download]\nhash-format = "sha1"\nhash = "' +
                "b" * 40 + '"\n')
            checked = {"slug": "test", "filename": "test.jar", "sha1": "a" * 40,
                       "provenance": "publisher_download", "status": "verified",
                       "existing_indexed_hash_matches": True}
            value = {"schema": 1, "requested_scope": "pinned",
                     "failures": [], "minecraft_booted": False,
                     "runtime_map_generated": False, "releases": [checked] * 15}
            with self.assertRaisesRegex(ValueError, "Outdated"):
                m.verify_pins(value, Path(dirname))

    def test_unsupported_shaped_recipe_is_not_assumed_resolved(self):
        raw = recipe("test:dish", "test:shaped", [], "minecraft:crafting_shaped")
        _, report = m.process(audit([raw]), {}, {})
        self.assertEqual(report["held_outputs"]["test:dish"],
                         "unsupported_processing_method")

    def test_deterministic_result(self):
        data = audit([recipe("test:soup", "test:soup", [{"item": "test:fish"}])])
        roots = {"test:fish": source("test:fish", "#c:foods/raw_fish")}
        self.assertEqual(m.process(data, roots, {}), m.process(data, roots, {}))

if __name__ == "__main__":
    unittest.main()
