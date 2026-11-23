"""Source contract for #505 (does not claim an in-game recipe-registry PASS)."""
from __future__ import annotations

import json
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
RITUAL = "kubejs/data/tmm/recipe/ritual/equivalent_exchange.json"
GUIDE = ("kubejs/data/occultism/modonomicon/books/dictionary_of_spirits/"
         "entries/crafting_rituals/equivalent_exchange.json")
LANG = "kubejs/assets/tmm/lang/en_us.json"


def read(relative: str) -> dict:
    return json.loads((ROOT / relative).read_text(encoding="utf-8"))


class EquivalentExchangeSourceContract(unittest.TestCase):
    def test_exact_native_occultism_1227_ritual_fields_and_12_closed_tags(self):
        # The exact upstream Occultism 1.227.0 RitualRecipe.CODEC expects
        # ritual_type, ritual_dummy, pentacle_id, activation_item and ingredients;
        # JEI registers all OccultismRecipes.RITUAL_TYPE recipe holders.
        recipe = read(RITUAL)
        self.assertEqual(recipe["type"], "occultism:ritual")
        self.assertEqual(recipe["ritual_type"], "occultism:craft")
        self.assertEqual(recipe["activation_item"], {"item": "occultism:book_of_binding_bound_marid"})
        self.assertEqual(recipe["pentacle_id"], "occultism:craft_marid")
        self.assertEqual(recipe["duration"], 600)
        self.assertEqual(recipe["result"], {"id": "projecte:philosophers_stone", "count": 1})
        self.assertEqual(len(recipe["ingredients"]), 12)
        tags = [ingredient["tag"] for ingredient in recipe["ingredients"]]
        self.assertEqual(len(set(tags)), 12)
        offers = []
        for tag in tags:
            self.assertTrue(tag.startswith("tmm:ritual_of_equivalent_exchange/"))
            tag_data = read(f"kubejs/data/tmm/tags/item/{tag.split(':', 1)[1]}.json")
            self.assertEqual(tag_data["replace"], True)
            self.assertTrue(tag_data["values"])
            self.assertTrue(all(":" in item and not item.startswith("#")
                                for item in tag_data["values"]))
            offers += tag_data["values"]
        self.assertEqual(len(offers), len(set(offers)))
        self.assertEqual(len(offers), 40)

    def test_modonomicon_page_links_exact_runtime_recipe_and_marid_guide(self):
        book = read(GUIDE)
        self.assertEqual(book["category"], "crafting_rituals")
        self.assertEqual(book["condition"], {
            "type": "modonomicon:entry_read", "entry_id": "pentacles/craft_marid"})
        self.assertEqual(book["icon"], {"item": "projecte:philosophers_stone"})
        self.assertTrue(any(parent["entry"] == "crafting_rituals/craft_marid_miner"
                            for parent in book["parents"]))
        spotlight = [page for page in book["pages"]
                     if page["type"] == "modonomicon:spotlight"]
        self.assertEqual(len(spotlight), 1)
        self.assertEqual(spotlight[0]["item"], {"item": "projecte:philosophers_stone"})
        recipe_pages = [page for page in book["pages"]
                        if page["type"] == "occultism:ritual_recipe"]
        self.assertEqual(len(recipe_pages), 1)
        self.assertEqual(recipe_pages[0]["recipe_id_1"], "tmm:ritual/equivalent_exchange")
        self.assertFalse(recipe_pages[0].get("recipe_id_2"))

    def test_all_book_text_keys_locally_defined_without_overriding_occultism_lang(self):
        guide = read(GUIDE)
        lang = read(LANG)
        keys = [guide["name"], guide["description"]]
        for page in guide["pages"]:
            for field in ("text", "title"):
                v = page.get(field)
                if v and v.startswith("book.tmm."):
                    keys.append(v)
        for key in keys:
            self.assertIn(key, lang)
            self.assertTrue(lang[key].strip())
        # Additional guide localization is in TMM's lang namespace, so native
        # Occultism/Modonomicon translation resources are not replaced.
        self.assertFalse((ROOT / "kubejs/assets/occultism/lang/en_us.json").exists())

    def test_both_old_projecte_grid_recipes_remain_unavailable(self):
        for name in ("philosophers_stone", "philosophers_stone_alt"):
            row = read(f"kubejs/data/projecte/recipe/{name}.json")
            self.assertEqual(row["neoforge:conditions"], [{"type": "neoforge:false"}])
            self.assertEqual(row["result"]["id"], "projecte:philosophers_stone")


if __name__ == "__main__":
    unittest.main()
