"""#712 source contracts: upstream costs, scoped provider IDs, pinned codec shape."""
import hashlib
import json
from pathlib import Path
import tomllib
import unittest

ROOT = Path(__file__).resolve().parents[1]
PREFIX = "kubejs/data/malum/recipe/malum/spirit_repair/"
# Source: Malum 1.8.2-declared 03b743a37f3eeb0cc7f4364f0730e1f135f78408.
# Provider IDs: Botania Revived 5f803227, Hexerei a91395c + exact .3 Atlas,
# Occultism native input lists. Keep narrow intended regex membership.
EXPECTED = json.loads(r'''{
  "botania/elementium": [
    1,
    {
      "item": "botania:elementium_ingot",
      "count": 1
    },
    [
      {
        "type": "malum:arcane",
        "count": 24
      }
    ],
    [
      "botania:elementium_helmet",
      "botania:elementium_chestplate",
      "botania:elementium_leggings",
      "botania:elementium_boots",
      "botania:elementium_pickaxe",
      "botania:elementium_shovel",
      "botania:elementium_axe",
      "botania:elementium_hoe",
      "botania:elementium_sword",
      "botania:elementium_shears",
      "botania:thunder_sword"
    ]
  ],
  "botania/manasteel": [
    1,
    {
      "item": "botania:manasteel_ingot",
      "count": 2
    },
    [
      {
        "type": "malum:arcane",
        "count": 16
      }
    ],
    [
      "botania:manasteel_helmet",
      "botania:manasteel_chestplate",
      "botania:manasteel_leggings",
      "botania:manasteel_boots",
      "botania:manasteel_pick",
      "botania:manasteel_shovel",
      "botania:manasteel_axe",
      "botania:manasteel_hoe",
      "botania:manasteel_sword",
      "botania:manasteel_shears",
      "botania:ender_dagger"
    ]
  ],
  "botania/terrasteel": [
    1,
    {
      "item": "botania:terrasteel_ingot",
      "count": 1
    },
    [
      {
        "type": "malum:arcane",
        "count": 32
      }
    ],
    [
      "botania:terrasteel_helmet",
      "botania:terrasteel_chestplate",
      "botania:terrasteel_leggings",
      "botania:terrasteel_boots",
      "botania:terra_sword",
      "botania:terra_pick",
      "botania:terra_axe"
    ]
  ],
  "hexerei/broom": [
    1,
    {
      "count": 4,
      "item": "hexerei:mandrake_root"
    },
    [
      {
        "type": "malum:arcane",
        "count": 16
      },
      {
        "type": "malum:aerial",
        "count": 16
      },
      {
        "type": "malum:eldritch",
        "count": 16
      }
    ],
    [
      "hexerei:herb_enhanced_broom_brush"
    ]
  ],
  "hexerei/witch_armor": [
    0.5,
    {
      "count": 4,
      "item": "hexerei:infused_fabric"
    },
    [
      {
        "type": "malum:arcane",
        "count": 8
      },
      {
        "type": "malum:wicked",
        "count": 8
      },
      {
        "type": "malum:eldritch",
        "count": 8
      }
    ],
    [
      "hexerei:witch_helmet",
      "hexerei:witch_chestplate",
      "hexerei:witch_boots"
    ]
  ],
  "occultism/gold_chalk": [
    1,
    {
      "count": 1,
      "tag": "c:dusts/gold"
    },
    [
      {
        "type": "malum:arcane",
        "count": 8
      }
    ],
    [
      "occultism:chalk_gold"
    ]
  ],
  "occultism/purple_chalk": [
    1,
    {
      "count": 2,
      "tag": "c:dusts/obsidian"
    },
    [
      {
        "type": "malum:arcane",
        "count": 8
      }
    ],
    [
      "occultism:chalk_purple"
    ]
  ],
  "occultism/red_chalk": [
    1,
    {
      "count": 1,
      "item": "occultism:afrit_essence"
    },
    [
      {
        "type": "malum:arcane",
        "count": 8
      }
    ],
    [
      "occultism:chalk_red"
    ]
  ],
  "occultism/white_chalk": [
    1,
    {
      "count": 1,
      "item": "occultism:burnt_otherstone"
    },
    [
      {
        "type": "malum:arcane",
        "count": 8
      }
    ],
    [
      "occultism:chalk_white"
    ]
  ]
}''')


class MalumSpiritRepairCompat(unittest.TestCase):
    def test_native_codec_fields_costs_and_targets(self):
        for name, (percentage, material, spirits, items) in EXPECTED.items():
            with self.subTest(recipe=name):
                recipe = json.loads((ROOT / (PREFIX + name + ".json")).read_text(encoding="utf-8"))
                provider = name.split("/")[0]
                self.assertEqual(recipe, {
                    "neoforge:conditions": [{"type": "neoforge:mod_loaded", "modid": provider}],
                    "type": "malum:spirit_repair", "durabilityPercentage": percentage,
                    "repairMaterial": material, "spirits": spirits, "validItems": items,
                })
                self.assertTrue(items)
                self.assertEqual(len(items), len(set(items)))
                self.assertTrue(all(item.startswith(provider + ":") for item in items))
                self.assertTrue(all(spirit["type"].startswith("malum:") for spirit in spirits))
        self.assertEqual(len(EXPECTED), 9)

    def test_packwiz_hashes(self):
        raw = (ROOT / "index.toml").read_bytes()
        pack = tomllib.loads((ROOT / "pack.toml").read_text(encoding="utf-8"))
        self.assertEqual(pack["index"]["hash"], hashlib.sha256(raw).hexdigest())
        records = {r["file"]: r for r in tomllib.loads(raw.decode("utf-8"))["files"]}
        for name in EXPECTED:
            path = PREFIX + name + ".json"
            self.assertEqual(records[path]["hash"], hashlib.sha256((ROOT / path).read_bytes()).hexdigest())


if __name__ == "__main__":
    unittest.main()
