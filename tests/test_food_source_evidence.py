import copy
import importlib.util
import json
import tempfile
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SPEC = importlib.util.spec_from_file_location(
    "food_source_builder", ROOT / "scripts" / "build_food_source_evidence.py"
)
MODULE = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(MODULE)
MANIFEST = ROOT / "compat/progression/design/food-provenance/source-receipts-v1.json"
RESOURCES = ROOT / "compat/progression/src/main/resources"


class FoodSourceEvidenceTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.source = json.loads(MANIFEST.read_text())
        cls.generated = MODULE.build(cls.source)

    def test_real_records_are_shipped_and_match_generator(self):
        self.assertGreaterEqual(len(self.generated) - 3, 140)
        for name, expected in self.generated.items():
            installed = json.loads((RESOURCES / name).read_text())
            self.assertEqual(installed, expected, name)

    def test_native_family_and_preparation_classes(self):
        source = "data/tyr_progression/tyr_progression/food_sources/"
        def get(item):
            ns, name = item.split(":")
            return self.generated[f"{source}{ns}/{name}.json"]
        self.assertEqual(get("minecraft:beef")["inferred_tags"], ["#c:foods/raw_meat", "#tmm:foods/raw"])
        self.assertEqual(get("minecraft:carrot")["source_units"], 1)
        self.assertEqual(get("pamhc2trees:orangeitem")["inferred_tags"], ["#c:foods/fruit", "#tmm:foods/raw"])
        self.assertEqual(get("aquaculture:tuna")["inferred_tags"], ["#c:foods/raw_fish", "#tmm:foods/raw"])
        self.assertEqual(get("minecraft:cooked_beef")["inferred_tags"], ["#c:foods/meat", "#tmm:foods/cooked"])
        self.assertEqual(get("dragonsurvival:charred_meat")["inferred_tags"], ["#c:foods/meat", "#tmm:foods/charred"])

    def test_cutting_preserves_approved_source_ratios(self):
        prefix = "data/tyr_progression/tyr_progression/food_sources/farmersdelight/"
        expected = {"cod_slice": 0.5, "salmon_slice": 0.5,
                    "cabbage_leaf": 0.5, "minced_beef": 2 / 3}
        for item, units in expected.items():
            self.assertAlmostEqual(self.generated[f"{prefix}{item}.json"]["source_units"], units)

    def test_unindexed_huaxia_tag_values_are_optional(self):
        values = self.generated["data/tmm/tags/item/foods/raw.json"]["values"]
        huaxia = [v for v in values if isinstance(v, dict) and v["id"].startswith("huaxiadelight:")]
        self.assertEqual(len(huaxia), 7)
        self.assertTrue(all(v["required"] is False for v in huaxia))

    def test_unresolved_mixed_recipes_do_not_acquire_records(self):
        for held in self.source["explicit_holds"]:
            ns, path = held["item"].split(":")
            self.assertNotIn(
                f"data/tyr_progression/tyr_progression/food_sources/{ns}/{path}.json",
                self.generated,
            )

    def test_rejects_duplicate_or_invalid_receipts(self):
        bad = copy.deepcopy(self.source)
        bad["verified_direct"].append(copy.deepcopy(bad["verified_direct"][0]))
        with self.assertRaisesRegex(ValueError, "Conflicting source routes"):
            MODULE.build(bad)
        bad = copy.deepcopy(self.source)
        bad["verified_direct"][0]["source_units"] = -1
        with self.assertRaisesRegex(ValueError, "Invalid source unit"):
            MODULE.build(bad)
        bad = copy.deepcopy(self.source)
        bad["verified_direct"][0]["item"] = "bad id"
        with self.assertRaisesRegex(ValueError, "Invalid item/provenance"):
            MODULE.build(bad)

    def test_all_data_roundtrip_files(self):
        with tempfile.TemporaryDirectory() as temp:
            output = MODULE.write(MANIFEST, Path(temp))
            self.assertEqual(set(output), set(self.generated))
            for relative, document in output.items():
                self.assertEqual(json.loads((Path(temp) / relative).read_text()), document)


if __name__ == "__main__":
    unittest.main()
