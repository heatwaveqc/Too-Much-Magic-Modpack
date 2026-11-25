"""Exact-source ink ritual and Dictionary of Spirits contracts — Type A only."""
import hashlib
import json
from pathlib import Path
import tomllib
import unittest

ROOT = Path(__file__).resolve().parents[1]
GRADES = {
    "common": ("foliot", 60, 4, ["occultism:purified_ink",
                                 "irons_spellbooks:arcane_essence", "minecraft:copper_ingot"]),
    "uncommon": ("foliot", 60, 2, ["irons_spellbooks:common_ink"] * 4),
    "rare": ("djinni", 150, 2, ["irons_spellbooks:uncommon_ink"] * 4),
    "epic": ("afrit", 300, 2, ["irons_spellbooks:rare_ink"] * 4),
    "legendary": ("marid", 510, 2, ["irons_spellbooks:epic_ink"] * 4),
}
BOOK = "kubejs/data/occultism/modonomicon/books/dictionary_of_spirits/entries/crafting_rituals/tmm_ink_rituals.json"
LANG = "kubejs/assets/tmm/lang/en_us.json"

class InkRitualBookContract(unittest.TestCase):
    def test_native_recipe_slots_pentacles_and_exact_yield(self):
        for grade, (tier, duration, count, ingredients) in GRADES.items():
            with self.subTest(grade=grade):
                recipe = json.loads((ROOT / f"kubejs/data/tmm/recipe/ritual/occultism_ink_{grade}.json").read_text())
                self.assertEqual(recipe["type"], "occultism:ritual")
                self.assertEqual(recipe["ritual_type"], "occultism:craft")
                self.assertEqual(recipe["pentacle_id"], f"occultism:craft_{tier}")
                self.assertEqual(recipe["activation_item"], {"item": f"occultism:book_of_binding_bound_{tier}"})
                self.assertEqual(recipe["duration"], duration)
                self.assertEqual([x["item"] for x in recipe["ingredients"]], ingredients)
                self.assertEqual(recipe["result"], {"id": f"irons_spellbooks:{grade}_ink", "count": count})
                self.assertEqual(recipe["ritual_dummy"], {"id": f"irons_spellbooks:{grade}_ink", "count": 1})

    def test_book_has_five_native_ritual_recipe_pages_and_accessible_parent(self):
        book = json.loads((ROOT / BOOK).read_text())
        self.assertEqual(book["category"], "crafting_rituals")
        self.assertEqual(book["condition"]["entry_id"], "pentacles/craft_foliot")
        self.assertEqual(book["parents"][0]["entry"], "crafting_rituals/overview")
        pages = [p for p in book["pages"] if p["type"] == "occultism:ritual_recipe"]
        self.assertEqual([p["recipe_id_1"] for p in pages],
                         [f"tmm:ritual/occultism_ink_{x}" for x in GRADES])
        self.assertTrue(all(p["title2"] == "" for p in pages))

    def test_all_book_strings_are_localized(self):
        book = json.loads((ROOT / BOOK).read_text())
        lang = json.loads((ROOT / LANG).read_text())
        keys = [book["name"], book["description"]]
        keys.extend(p[field] for p in book["pages"] for field in ("text", "title1")
                    if field in p and p[field])
        self.assertTrue(all(key in lang and lang[key] for key in keys))

    def test_packed_recipes_book_and_language_are_hash_locked(self):
        pack = tomllib.loads((ROOT / "pack.toml").read_text())
        raw = (ROOT / "index.toml").read_bytes()
        self.assertEqual(pack["index"]["hash"], hashlib.sha256(raw).hexdigest())
        files = {f["file"]: f for f in tomllib.loads(raw.decode())["files"]}
        expected = [LANG, BOOK] + [f"kubejs/data/tmm/recipe/ritual/occultism_ink_{g}.json" for g in GRADES]
        for path in expected:
            with self.subTest(path=path):
                self.assertIn(path, files)
                self.assertEqual(files[path]["hash"], hashlib.sha256((ROOT / path).read_bytes()).hexdigest())

if __name__ == "__main__":
    unittest.main()
