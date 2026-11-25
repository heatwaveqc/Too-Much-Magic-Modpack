"""Exact uploaded Pastel 1.1.5.5 JSON evidence for #711; no game boot."""
import hashlib
import json
from pathlib import Path
import tomllib
import unittest

ROOT = Path(__file__).resolve().parents[1]
PREFIX = "kubejs/data/pastel/recipe/"
BOTANIA = "mod_integration/botania/"
HEX = "mod_integration/hexcasting/fusion_shrine/charged_amethyst_from_amethyst_shard_fusion"
DYES = "black blue brown cyan gray green light_blue light_gray lime magenta orange pink purple red white yellow".split()
COLORS = "cyan magenta yellow white black".split()
PEDESTALS = [BOTANIA + "pedestal/floral_fertilizer"] + [
    BOTANIA + "pedestal/" + suffix
    for dye in DYES for suffix in
    (f"glimmering_flowers/{dye}_glimmering_flower", f"mushrooms/{dye}_mushroom")
]
LOTUS = {
    BOTANIA + "potion_workshop_reacting/blackest_lotus": "pastel:blackest_lotus",
    BOTANIA + "potion_workshop_reacting/least_black_lotus": "pastel:least_black_lotus",
    BOTANIA + "primordial_fire_burning/least_black_lotus": "pastel:least_black_lotus",
    BOTANIA + "spirit_instiller/blackest_lotus": "pastel:blackest_lotus",
}
NAMES = PEDESTALS + list(LOTUS) + [HEX]
# Independently computed from exact SHA1 53a582520b78aebd56c9be1fd041472047dca3d3
# uploaded JAR, all 38 original recipe objects (integer-valued floats normalized).
ORIGINAL = "433784f5cf84e0cede17a67a68ee057b5cb4ac1c06680a13e7f998d988ee750e"
FIXED = "17c4d461b7cc7661e26cca794b970864a4c2136c1bee3dd243a655b31b416bab"


def normalized(v):
    if isinstance(v, float) and v.is_integer():
        return int(v)
    if isinstance(v, dict):
        return {k: normalized(x) for k, x in v.items()}
    if isinstance(v, list):
        return [normalized(x) for x in v]
    return v


def digest(v):
    return hashlib.sha256(json.dumps(normalized(v), sort_keys=True,
                                     separators=(",", ":"), ensure_ascii=False).encode()).hexdigest()


class PastelRecipeCompat(unittest.TestCase):
    def test_exact_native_recipe_payloads_except_documented_repairs(self):
        actual = {n: json.loads((ROOT / (PREFIX + n + ".json")).read_text(encoding="utf-8"))
                  for n in NAMES}
        self.assertEqual(len(actual), 38)
        self.assertEqual(digest(actual), FIXED)
        for n in PEDESTALS:
            v = actual[n]
            self.assertEqual(v.pop("colors"), {"pastel:" + c: 0 for c in COLORS})
            v.update({c: 0 for c in COLORS})
        self.assertEqual(actual[HEX]["fluid"], {"fluid": "minecraft:water"})
        actual[HEX]["fluid"] = "minecraft:water"
        for n, item in LOTUS.items():
            self.assertEqual(actual[n]["neoforge:conditions"].pop(),
                             {"type": "neoforge:item_exists", "item": item})
        self.assertEqual(digest(actual), ORIGINAL)

    def test_exact_overrides_are_indexed(self):
        raw = (ROOT / "index.toml").read_bytes()
        pack = tomllib.loads((ROOT / "pack.toml").read_text(encoding="utf-8"))
        self.assertEqual(pack["index"]["hash"], hashlib.sha256(raw).hexdigest())
        entries = tomllib.loads(raw.decode())["files"]
        for n in NAMES:
            path = PREFIX + n + ".json"
            rows = [r for r in entries if r["file"] == path]
            self.assertEqual(len(rows), 1)
            self.assertEqual(rows[0]["hash"], hashlib.sha256((ROOT / path).read_bytes()).hexdigest())


if __name__ == "__main__":
    unittest.main()
