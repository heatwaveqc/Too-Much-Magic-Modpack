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
    def test_three_pools(self):
        self.assertEqual(set(self.review["approved_entries"]),{"hunting","culinary","fishing"})
        self.assertEqual(len(self.output),14)
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
            self.assertTrue(all(i["unitWorth"]>0 and i["content"].startswith("minecraft:") for i in items.values()))
    def test_paper_recipes(self):
        for k in self.review["approved_entries"]:
            recipe=self.output[f"kubejs/data/tmm/recipe/bountiful/tmm_{k}.json"]
            self.assertEqual(recipe["result"]["components"]["bountiful:decree_data"],{"ids":[f"tmm_{k}"],"rank":1})
            self.assertEqual(recipe["ingredients"][0],{"item":"minecraft:paper"})
    def test_committed_overlay_equals_compiler(self):
        for path,content in self.output.items():
            self.assertEqual(json.loads((ROOT/"compat/bountiful/live-pilot"/path).read_text()),content)

if __name__=="__main__":unittest.main()
