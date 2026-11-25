"""Offline negative/regression tests for exact Economy converter recipe contract."""
import copy
import importlib.util
import unittest
from pathlib import Path

MODULE = Path(__file__).with_name("verify_converter_contract.py")
SPEC = importlib.util.spec_from_file_location("verify_converter_contract", MODULE)
validator = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(validator)


def recipe(name):
    contract = validator.CONTRACTS[name]
    return {
        "neoforge:conditions": [
            {"type": "neoforge:mod_loaded", "modid": mod}
            for mod in sorted(contract["mods"])
        ],
        "type": "minecraft:crafting_shapeless",
        "ingredients": [{"item": item} for item in sorted(contract["ingredients"])],
        "result": {"id": f"tmm_economy:{name}", "count": 1},
    }


class ConverterRecipeTests(unittest.TestCase):
    def test_both_locked_recipes_valid(self):
        for name in validator.CONTRACTS:
            with self.subTest(name=name):
                validator.validate_recipe(recipe(name), name=name)

    def test_reject_obsolete_alchemy_catalyst_and_dark_matter(self):
        modifications = [
            ("emc_catalyst", "botania:conjuration_catalyst", "botania:alchemy_catalyst"),
            ("emc_catalyst", "projectexpansion:basic_emc_link", "projecte:dark_matter"),
            ("emc_source_jar", "projectexpansion:basic_emc_link", "projecte:dark_matter"),
        ]
        for name, original, invalid in modifications:
            with self.subTest(name=name, invalid=invalid):
                obj = recipe(name)
                for entry in obj["ingredients"]:
                    if entry["item"] == original:
                        entry["item"] = invalid
                with self.assertRaises(ValueError):
                    validator.validate_recipe(obj, name=name)

    def test_reject_alternative_tags_and_extra_gated_items(self):
        for name in validator.CONTRACTS:
            with self.subTest(name=name):
                obj = recipe(name)
                obj["ingredients"][0] = {"tag": "c:storage_links"}
                with self.assertRaises(ValueError):
                    validator.validate_recipe(obj, name=name)
                obj = recipe(name)
                obj["ingredients"].append({"item": "projecte:philosophers_stone"})
                with self.assertRaises(ValueError):
                    validator.validate_recipe(obj, name=name)

    def test_reject_nonconsuming_or_wrong_result_and_count(self):
        obj = recipe("emc_catalyst")
        obj["type"] = "minecraft:crafting_shaped"
        with self.assertRaises(ValueError):
            validator.validate_recipe(obj, name="emc_catalyst")
        obj = recipe("emc_catalyst")
        obj["result"]["count"] = 2
        with self.assertRaises(ValueError):
            validator.validate_recipe(obj, name="emc_catalyst")

    def test_reject_missing_or_broadened_mod_gates(self):
        obj = recipe("emc_source_jar")
        obj["neoforge:conditions"] = []
        with self.assertRaises(ValueError):
            validator.validate_recipe(obj, name="emc_source_jar")
        obj = recipe("emc_source_jar")
        obj["neoforge:conditions"].append({
            "type": "neoforge:mod_loaded", "modid": "projecte"
        })
        with self.assertRaises(ValueError):
            validator.validate_recipe(obj, name="emc_source_jar")

    def test_actual_source_and_account_guards(self):
        validator.verify()


if __name__ == "__main__":
    unittest.main()
