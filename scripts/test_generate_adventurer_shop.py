import json
import unittest
from fractions import Fraction
from generate_adventurer_shop import ROOT, POLICY, quote, generate

P = json.loads(POLICY.read_text())
T = json.loads((ROOT / "config/qshop/shops/tmm.json").read_text())

def row(*tags, stack=64, food=False, spawn=False):
    return {"tags": ["tmm:shop/" + t for t in tags],
            "max_stack": stack, "food": food, "spawn_egg": spawn}

class ShopTest(unittest.TestCase):
    def test_exact_bounds(self):
        for value in (1, 2, 9, 18, 64, 144, 8192, 16384, 1000000000000):
            for maximum in (1, 16, 64):
                for side in ("BUY", "SELL"):
                    q = quote(value, maximum, side, P)
                    if q:
                        n, price, efficiency = q
                        self.assertLessEqual(n, maximum)
                        self.assertGreaterEqual(price, 1)
                        self.assertLessEqual(price, P["max_price"])
                        self.assertTrue(Fraction(75,100) <= efficiency <= Fraction(85,100))
        self.assertEqual(quote(6400, 1, "BUY", P)[:2], (1,125))
        self.assertEqual(quote(6400, 1, "SELL", P)[:2], (1,80))
        self.assertIsNone(quote(1, 1, "SELL", P))
        self.assertIsNone(quote(0, 64, "BUY", P))

    def test_catalogue(self):
        facts = {
            "minecraft:iron_ore": row("buy/ores","eligible/overworld"),
            "minecraft:iron_ingot": row("sell/ingots","sell/gems"),
            "minecraft:nether_quartz_ore": row("buy/ores","eligible/nether"),
            "example:unreviewed_ore": row("buy/ores"),
            "example:blocked_ore": row("buy/ores","eligible/overworld","excluded"),
            "example:egg": row("buy/raw_materials","eligible/overworld",spawn=True),
            "example:seed": row("buy/raw_materials","eligible/overworld"),
            "minecraft:bread": row("buy/food","eligible/overworld",food=True),
            "example:other_food": row(food=True),
            "apotheosis:gem": row("sell/gems",stack=1),
            "artifacts:thing": row("eligible/overworld",stack=1),
            "minecraft:diamond": row("sell/gems"),
        }
        values = {key: 6400 for key in facts if key != "minecraft:diamond"}
        shop, audit = generate(facts, values, P, T)
        self.assertEqual((shop,audit), generate(dict(reversed(list(facts.items()))), values, P,T))
        by = {r["item"]: r for r in audit}
        self.assertEqual(by["example:unreviewed_ore"]["status"],"acquisition_review")
        self.assertEqual(by["example:blocked_ore"]["status"],"excluded")
        self.assertEqual(by["example:egg"]["status"],"excluded")
        self.assertEqual(by["example:seed"]["status"],"market_only")
        self.assertEqual(by["example:other_food"]["status"],"acquisition_review")
        self.assertEqual(by["apotheosis:gem"]["status"],"component_review")
        self.assertEqual(by["artifacts:thing"]["status"],"component_review")
        self.assertEqual(by["minecraft:diamond"]["status"],"missing_emc")
        self.assertEqual(by["minecraft:iron_ingot"]["category"],"ingots")
        self.assertEqual(by["minecraft:nether_quartz_ore"]["status"],"stage_bridge_pending")
        entries = [e for t in shop["tabs"] for e in t["entries"]]
        self.assertEqual(len({e["uuid"] for e in entries}),len(entries))
        gated = next(e for e in entries if e["item"]["item"] == "minecraft:nether_quartz_ore")
        self.assertEqual(gated["requiredStages"],["tmm_shop_nether"])
        self.assertEqual(len([e for e in entries if e["item"]["item"] == "tmm:coin"]),2)

if __name__ == "__main__":
    unittest.main()
