import json
import pathlib
import sys
import unittest

ROOT=pathlib.Path(__file__).resolve().parents[1]
sys.path.insert(0,str(ROOT/"scripts"))
import build_bountiful_live_pilot as pilot

class TestPilot(unittest.TestCase):
    def setUp(self):
        self.output={p:json.loads(s) for p,s in pilot.build().items()}
        self.review=json.loads((ROOT/"manifest/bountiful-live-pilot-review.json").read_text())
    def test_four_pools(self):
        self.assertEqual(set(self.review["approved_entries"]),{"hunting","culinary","fishing","ars_nouveau"})
        self.assertEqual(len(self.output),18)
    def test_no_village_boards_or_default_pools(self):
        cfg=self.output["config/bountiful/bountiful.json"]
        self.assertEqual(cfg["board"]["villageGenFrequency"],0)
        self.assertEqual(cfg["general"]["dataPathsToExclude"],["bounty_pools/.+","bounty_decrees/.+"])
        self.assertFalse(cfg["bounty"]["allowDecreeMixing"])
        for k in self.review["approved_entries"]:
            decree=self.output[f"config/bountiful/bounty_decrees/tmm_{k}.json"]
            self.assertFalse(decree["canWanderBuy"])
            self.assertEqual(decree["objectives"],[f"tmm_{k}_objs"])
    def test_gateway_full_completion_only(self):
        items=self.output["config/bountiful/bounty_pools/tmm_hunting_objs.json"]["content"]
        gates=[v for v in items.values() if v["type"]=="criteria"]
        self.assertEqual({v["conditions"]["gateway"] for v in gates},set(self.review["approved_gateway_ids"]))
        self.assertTrue(all(x["content"]=="gateways:finish_gateway" and x["amount"]=={"min":1,"max":1} for x in gates))
    def test_no_quest_or_decree_emc(self):
        self.assertEqual(self.review["qa_scope"],"disposable_live_pilot_only_not_final_economy_or_prod_release")
        self.assertFalse({"bountiful:bounty","bountiful:decree"}&set(self.review["effective_emc"]))
        for k in self.review["approved_entries"]:
            items=self.output[f"config/bountiful/bounty_pools/tmm_{k}_rews.json"]["content"]
            self.assertTrue(all(i["unitWorth"]>0 for i in items.values()))
    def test_ars_custom_rewards_and_effort_bands(self):
        rewards=self.output["config/bountiful/bounty_pools/tmm_ars_nouveau_rews.json"]["content"]
        self.assertEqual({v["content"] for v in rewards.values()},
            {"ars_nouveau:source_gem","ars_nouveau:air_essence"})
        self.assertEqual({v["rarity"] for v in rewards.values()},{"COMMON","UNCOMMON"})
        self.assertEqual(set(self.review["unpriced_reward_items"]),
            {"ars_nouveau:source_gem","ars_nouveau:air_essence"})
        objectives=self.output["config/bountiful/bounty_pools/tmm_ars_nouveau_objs.json"]["content"]
        self.assertIn("item_minecraft_lapis_lazuli",objectives)
        self.assertTrue(any(v["type"]=="criteria" and
            v["conditions"]=={"gateway":"tmm:mob_ars_wilden_hunter"} for v in objectives.values()))
        recipe=self.output["kubejs/data/tmm/recipe/bountiful/tmm_ars_nouveau.json"]
        self.assertEqual(recipe["ingredients"],
            [{"item":"minecraft:paper"},{"item":"ars_nouveau:source_gem"}])
        self.assertEqual(recipe["result"]["components"]["bountiful:decree_data"],
            {"ids":["tmm_ars_nouveau"],"rank":1})
    def test_paper_recipes(self):
        for k in self.review["approved_entries"]:
            recipe=self.output[f"kubejs/data/tmm/recipe/bountiful/tmm_{k}.json"]
            self.assertEqual(recipe["result"]["components"]["bountiful:decree_data"],{"ids":[f"tmm_{k}"],"rank":1})
            self.assertEqual(recipe["ingredients"][0],{"item":"minecraft:paper"})
    def test_committed_overlay_equals_compiler(self):
        for path,content in self.output.items():
            self.assertEqual(json.loads((ROOT/"compat/bountiful/live-pilot"/path).read_text()),content)

if __name__=="__main__":unittest.main()
