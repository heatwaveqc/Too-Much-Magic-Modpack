import json
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DATA = ROOT / "compat/progression/src/main/resources/data/tyr_progression/tyr_progression"
SOURCES = DATA / "food_sources"
ROUTES = DATA / "food_recipe_routes"
RECEIPTS = ROOT / "compat/progression/design/food-provenance/source-receipts-v1.json"


def load_index(root):
    records = {}
    for path in sorted(root.rglob("*.json")):
        payload = json.loads(path.read_text(encoding="utf-8"))
        ns = path.relative_to(root).parts[0]
        suffix = path.relative_to(root / ns).as_posix().removesuffix(".json")
        key = f"{ns}:{suffix}"
        if payload["item"] != key:
            raise ValueError(f"{path}: item ID does not match resource location")
        if key in records:
            raise ValueError(f"Duplicate evidence {key}")
        records[key] = payload
    return records


class FoodRecipeRouteFilesTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.sources = load_index(SOURCES)
        cls.routes = load_index(ROUTES)

    def test_source_coverage_and_real_seagrass_without_global_food_overrides(self):
        self.assertGreaterEqual(len(self.sources), 162)
        self.assertEqual(self.sources["minecraft:seagrass"]["source_units"], 1)
        self.assertEqual(self.sources["minecraft:seagrass"]["inferred_tags"],
                         ["#tmm:foods/raw", "#tmm:foods/sea_plants"])
        self.assertNotIn("nutrition", self.sources["minecraft:seagrass"])
        self.assertNotIn("saturation", self.sources["minecraft:seagrass"])

    def test_all_complete_routes_have_audited_inputs_and_bounded_yields(self):
        self.assertGreaterEqual(len(self.routes), 7)
        for item, payload in self.routes.items():
            self.assertEqual(payload["schema"], 1)
            self.assertTrue(payload["complete"], item)
            self.assertIn(payload["identity"], ("ordinary", "unfavored_all"))
            self.assertTrue(payload["routes"], item)
            receipts = set()
            for route in payload["routes"]:
                self.assertNotIn(route["receipt"], receipts, item)
                receipts.add(route["receipt"])
                self.assertIn(route["method"], ("nonthermal", "heat", "char"))
                self.assertGreaterEqual(route["result_count"], 1)
                self.assertLessEqual(route["result_count"], 64)
                eff = route.get("efficiency", 1)
                self.assertGreaterEqual(eff, 1)
                self.assertLessEqual(eff, 4)
                if route["method"] != "nonthermal":
                    self.assertEqual(eff, 1)
                self.assertTrue(route["ingredients"], item)
                food_inputs = 0
                for input in route["ingredients"]:
                    self.assertGreaterEqual(input["count"], 1)
                    self.assertLessEqual(input["count"], 64)
                    role = input.get("role", "food")
                    self.assertIn(role, ("food", "neutral"))
                    if role == "food":
                        food_inputs += 1
                        self.assertIn(input["item"], self.sources.keys() | self.routes.keys(),
                                      f"{item}: missing source record for {input['item']}")
                self.assertGreater(food_inputs, 0, item)

    def test_no_recipe_cycle_in_checked_in_files(self):
        def visit(item, path):
            self.assertNotIn(item, path, "Food recipe cycle: " + repr(path + (item,)))
            route = self.routes.get(item)
            if route:
                for option in route["routes"]:
                    for ingredient in option["ingredients"]:
                        if ingredient.get("role", "food") == "food":
                            visit(ingredient["item"], path + (item,))
        for item in self.routes:
            visit(item, ())

    def test_pams_juice_two_oranges_and_non_nutritive_juicer(self):
        juice = self.routes["pamhc2foodextended:orangejuiceitem"]
        self.assertEqual(juice["routes"][0]["receipt"], "pamhc2foodextended:orangejuiceitem")
        self.assertEqual(juice["routes"][0]["result_count"], 1)
        self.assertEqual(juice["routes"][0]["method"], "nonthermal")
        self.assertEqual(juice["routes"][0]["ingredients"], [
            {"item": "pamhc2trees:orangeitem", "count": 2},
            {"item": "pamhc2foodcore:juiceritem", "count": 1, "role": "neutral"},
        ])

    def test_aquaculture_sushi_is_real_compatible_route_not_an_unresolved_hold(self):
        sushi = self.routes["aquaculture:sushi"]
        self.assertEqual(sushi["routes"][0]["receipt"], "aquaculture:sushi")
        self.assertEqual(sushi["routes"][0]["result_count"], 1)
        self.assertEqual(
            [(a["item"], a["count"]) for a in sushi["routes"][0]["ingredients"]],
            [("aquaculture:fish_fillet_raw", 1), ("minecraft:seagrass", 1)],
        )
        holds = {v["item"] for v in json.loads(RECEIPTS.read_text())["explicit_holds"]}
        self.assertNotIn("aquaculture:sushi", holds)
        self.assertNotIn("pamhc2foodextended:orangejuiceitem", holds)
        self.assertIn("pamhc2foodextended:orangejuiceitem", self.routes)
        self.assertIn("minecraft:suspicious_stew", holds)


if __name__ == "__main__":
    unittest.main()
