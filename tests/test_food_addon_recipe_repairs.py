"""Type A source contracts for #715; no native recipe codec or game boot."""
import hashlib
import json
from pathlib import Path
import tomllib
import unittest

ROOT = Path(__file__).resolve().parents[1]
PREFIX = "kubejs/data/"
SOY = "cocktailsdelight/recipe/pouring/soymilk_"
SMOKER = "dungeonsdelight/recipe/integration/aether/smoker/ae_cooked_marbled_meat_from_smoking.json"
PIE = "endersdelight/recipe/chorus_pie_slice.json"
PATHS = [SOY + "bottle.json", SOY + "bucket.json", SMOKER, PIE]


def load(path):
    return json.loads((ROOT / PREFIX / path).read_text(encoding="utf-8"))


class FoodAddonRecipeRepairs(unittest.TestCase):
    def test_optional_soymilk_guard_and_original_pouring_contract(self):
        # Cocktails Delight source @14480c8aa5101e73fbeda4386fb07a6a1b10b6f5.
        for kind, amount in (("bottle", 250), ("bucket", 1000)):
            with self.subTest(kind=kind):
                self.assertEqual(load(SOY + kind + ".json"), {
                    "neoforge:conditions": [{"type": "neoforge:mod_loaded", "modid": "vegandelight"}],
                    "type": "brewinandchewin:keg_pouring",
                    "fluid": {"amount": amount, "id": "vegandelight:soymilk"},
                    "output": {"count": 1, "id": "vegandelight:soymilk_" + kind},
                    "unit": "millibuckets",
                })

    def test_aether_smoking_preserves_ingredients_time_xp_and_yield(self):
        # Installed recipe decoder evidence in #708.
        self.assertEqual(load(SMOKER), {
            "neoforge:conditions": [{"type": "neoforge:mod_loaded", "modid": "aether"}],
            "type": "minecraft:smoking", "category": "food",
            "cookingtime": 100, "experience": 0.1,
            "ingredient": {"item": "dungeonsdelight:marbled_meat"},
            "result": {"id": "dungeonsdelight:cooked_marbled_meat", "count": 1},
        })

    def test_chorus_pie_matches_native_fd_cutting_shape(self):
        # EndersDelight issue 41, native FarmersDelight apple pie @dd8b9b0bfec916a2da88a6d64e3a37f4a02c3259.
        self.assertEqual(load(PIE), {
            "type": "farmersdelight:cutting",
            "ingredients": [{"item": "endersdelight:chorus_pie"}],
            "tool": [{"type": "farmersdelight:item_ability", "action": "knife_dig"},
                     {"tag": "c:tools/knife"}],
            "result": [{"item": {"count": 4, "id": "endersdelight:chorus_pie_slice"}}],
        })

    def test_packwiz_hash_chain(self):
        raw = (ROOT / "index.toml").read_bytes()
        pack = tomllib.loads((ROOT / "pack.toml").read_text(encoding="utf-8"))
        self.assertEqual(pack["index"]["hash"], hashlib.sha256(raw).hexdigest())
        records = tomllib.loads(raw.decode("utf-8"))["files"]
        for path in PATHS:
            full = PREFIX + path
            matches = [row for row in records if row["file"] == full]
            self.assertEqual(len(matches), 1)
            self.assertEqual(matches[0]["hash"], hashlib.sha256((ROOT / full).read_bytes()).hexdigest())


if __name__ == "__main__":
    unittest.main()
