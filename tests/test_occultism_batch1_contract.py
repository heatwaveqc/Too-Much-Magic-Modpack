"""Type A source-only checks for the four recovered #338 recipe families."""
import json
import hashlib
import tomllib
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
DATA = ROOT / "kubejs/data"
ENTRIES = DATA / "occultism/modonomicon/books/dictionary_of_spirits/entries"
TIERS = {"foliot": ("mysterious_scrap_metal", "cracked"),
         "djinni": ("timeworn_fabric", "chipped"),
         "afrit": ("luminous_crystal_shard", "flawed"),
         "marid": ("arcane_sands", "normal")}
PURITIES = ("cracked", "chipped", "flawed", "normal", "flawless", "perfect")

def load(path):
    return json.loads(path.read_text(encoding="utf-8"))

class Batch1Contract(unittest.TestCase):
    def test_binding_recipes_and_pages(self):
        book = load(ENTRIES / "getting_started/books_of_binding.json")
        ids = {p.get("recipe_id_1") for p in book["pages"]}
        for tier, (material, purity) in TIERS.items():
            for suffix in ("", "_from_empty"):
                name = f"book_of_binding_{tier}{suffix}"
                recipe = load(DATA / f"occultism/recipe/crafting/{name}.json")
                self.assertEqual(recipe["result"], {"id": f"occultism:book_of_binding_{tier}", "count": 1})
                self.assertEqual(recipe["key"]["a"], {"type": "neoforge:compound", "children": [
                    {"item": f"apotheosis:{material}"}, {"type": "apotheosis:gem", "purity": purity}]})
                self.assertEqual("".join(recipe["pattern"]).count("a"), 1)
                self.assertIn(f"occultism:crafting/{name}", ids)
    def test_spirit_fire(self):
        book = load(ENTRIES / "getting_started/spirit_fire.json")
        ids = {p.get("recipe_id_1") for p in book["pages"]}
        for purity in PURITIES:
            name = f"apotheosis_gem_{purity}"
            recipe = load(DATA / f"tmm/recipe/spirit_fire/{name}.json")
            self.assertEqual(recipe, {"type": "occultism:spirit_fire", "ingredient": {
                "type": "apotheosis:gem", "purity": purity}, "result": {"id": "apotheosis:gem_dust", "count": 1}})
            self.assertIn(f"tmm:spirit_fire/{name}", ids)
    def test_rituals(self):
        miner = load(DATA / "occultism/recipe/ritual/misc_miner_ancient_eldritch.json")
        self.assertEqual(miner["activation_item"], {"item": "occultism:mining_dim_core"})
        self.assertEqual(miner["ritual_type"], "occultism:craft_miner_spirit")
        self.assertEqual(miner["entity_to_sacrifice"]["tag"], "occultism:humans")
        self.assertEqual(miner["result"], {"id": "occultism:miner_ancient_eldritch", "count": 1,
                                          "components": {"minecraft:unbreakable": {}}})
        self.assertEqual(sorted(i["item"] for i in miner["ingredients"]), sorted([
            "occultism:miner_marid_master", "forbidden_arcanus:eternal_stella", "deeperdarker:heart_of_the_deep",
            "cataclysm:abyssal_egg", "alexscaves:pure_darkness", "apotheosis:godforged_pearl",
            "cataclysm:cursium_ingot", "deeperdarker:soul_crystal"]))
        life = load(DATA / "tmm/recipe/ritual/perpetual_life.json")
        self.assertEqual(life["activation_item"], {"item": "minecraft:nether_star"})
        self.assertEqual(life["result"], {"id": "minecraft:spawner", "count": 1})
        self.assertEqual(life["ritual_type"], "occultism:craft")
        self.assertNotIn("entity_to_sacrifice", life)
        self.assertEqual(sorted(i["item"] for i in life["ingredients"]), sorted(
            ["occultism:iesnium_ingot"]*2 + ["malum:soul_stained_steel_ingot"]*4 +
            ["neovitae:ingot_hellforged"]*2 + ["goety:cursed_ingot"]*2 +
            ["occultism:soul_gem", "minecraft:echo_shard"]))
        for recipe in (miner, life):
            self.assertEqual(recipe["pentacle_id"], "occultism:contact_eldritch_spirit")
            self.assertEqual(recipe["duration"], 780)
        book = load(ENTRIES / "crafting_rituals/tmm_perpetual_life.json")
        self.assertEqual(book["condition"]["entry_id"], "pentacles/contact_eldritch_spirit")
        self.assertIn("tmm:ritual/perpetual_life", {p.get("recipe_id_1") for p in book["pages"]})
    def test_perpetual_life_neo_vitae_provider_id(self):
        # Independent provider declaration inventory, upstream NVItems.java
        # @2744123bb6e4f72c602178b23384be41956eef23 (HELLFORGED_INGOT).
        # Thanksgiving QA #708 also verified the installed native recipe output.
        inventory = (ROOT / "docs/mod-capabilities/inventories/neovitae-registries.md").read_text(encoding="utf-8")
        provider_ids = [row.split("|")[2].strip().strip("`")
                        for row in inventory.splitlines()
                        if row.startswith("| `HELLFORGED_INGOT` |")]
        self.assertEqual(provider_ids, ["neovitae:ingot_hellforged"])
        recipe = load(DATA / "tmm/recipe/ritual/perpetual_life.json")
        self.assertEqual([i["item"] for i in recipe["ingredients"]
                          if i["item"].startswith("neovitae:")], provider_ids * 2)
        self.assertEqual(len(recipe["ingredients"]), 12)
        self.assertEqual(recipe["ritual_dummy"], {"id": "minecraft:spawner", "count": 1})

    def test_localized_pages(self):
        lang = load(ROOT / "kubejs/assets/tmm/lang/en_us.json")
        for name in ("getting_started/books_of_binding", "getting_started/spirit_fire",
                     "crafting_rituals/craft_ancient_miner", "crafting_rituals/tmm_perpetual_life"):
            entry = load(ENTRIES / f"{name}.json")
            for node in [entry, *entry["pages"]]:
                for v in node.values():
                    if isinstance(v, str) and v.startswith("book.tmm.occultism_batch1."):
                        self.assertTrue(lang.get(v), v)

    def test_packwiz_includes_every_changed_runtime_file(self):
        raw = (ROOT / "index.toml").read_bytes()
        pack = tomllib.loads((ROOT / "pack.toml").read_text())
        self.assertEqual(pack["index"]["hash"], hashlib.sha256(raw).hexdigest())
        records = {x["file"]: x for x in tomllib.loads(raw.decode())["files"]}
        for path in ["kubejs/assets/tmm/lang/en_us.json","kubejs/data/occultism/modonomicon/books/dictionary_of_spirits/entries/crafting_rituals/craft_ancient_miner.json","kubejs/data/occultism/modonomicon/books/dictionary_of_spirits/entries/crafting_rituals/tmm_perpetual_life.json","kubejs/data/occultism/modonomicon/books/dictionary_of_spirits/entries/getting_started/books_of_binding.json","kubejs/data/occultism/modonomicon/books/dictionary_of_spirits/entries/getting_started/spirit_fire.json","kubejs/data/occultism/recipe/crafting/book_of_binding_afrit.json","kubejs/data/occultism/recipe/crafting/book_of_binding_afrit_from_empty.json","kubejs/data/occultism/recipe/crafting/book_of_binding_djinni.json","kubejs/data/occultism/recipe/crafting/book_of_binding_djinni_from_empty.json","kubejs/data/occultism/recipe/crafting/book_of_binding_foliot.json","kubejs/data/occultism/recipe/crafting/book_of_binding_foliot_from_empty.json","kubejs/data/occultism/recipe/crafting/book_of_binding_marid.json","kubejs/data/occultism/recipe/crafting/book_of_binding_marid_from_empty.json","kubejs/data/occultism/recipe/ritual/misc_miner_ancient_eldritch.json","kubejs/data/tmm/recipe/ritual/perpetual_life.json","kubejs/data/tmm/recipe/spirit_fire/apotheosis_gem_chipped.json","kubejs/data/tmm/recipe/spirit_fire/apotheosis_gem_cracked.json","kubejs/data/tmm/recipe/spirit_fire/apotheosis_gem_flawed.json","kubejs/data/tmm/recipe/spirit_fire/apotheosis_gem_flawless.json","kubejs/data/tmm/recipe/spirit_fire/apotheosis_gem_normal.json","kubejs/data/tmm/recipe/spirit_fire/apotheosis_gem_perfect.json"]:
            with self.subTest(path=path):
                self.assertEqual(records[path]["hash"], hashlib.sha256((ROOT / path).read_bytes()).hexdigest())

if __name__ == "__main__":
    unittest.main()
