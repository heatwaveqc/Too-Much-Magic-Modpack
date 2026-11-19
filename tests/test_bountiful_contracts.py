import copy
import importlib.util
import json
import unittest
from pathlib import Path

ROOT=Path(__file__).resolve().parents[1]
spec=importlib.util.spec_from_file_location("bounty",ROOT/"scripts/compile_bountiful_contracts.py")
m=importlib.util.module_from_spec(spec)
spec.loader.exec_module(m)
C=json.loads((ROOT/"manifest/bountiful-contract-candidates.json").read_text())
A={"bountiful":{"hashes":{"sha512":"fixture"}}}
R={"baseline":C["baseline"],"audit_hash":"fixture","settlement":"approved_item_barter","registered_items":["minecraft:wheat","minecraft:bread"],"effective_emc":{"minecraft:wheat":2,"minecraft:bread":6},"approved_reward_items":["minecraft:bread"],"approved_entries":{"farming":{"reward":{"item":"minecraft:bread","count":1},"objectives":[{"item":"minecraft:wheat","count":4}]}}}

class TestBountifulCompiler(unittest.TestCase):
    def test_closed_single_delivery(self):
        files=m.compile_candidate(C,A,R)
        self.assertEqual(len(files),5)
        d=json.loads(files["config/bountiful/bounty_decrees/tmm_farming.json"])
        self.assertTrue(d["replace"])
        self.assertFalse(d["canWanderBuy"])
        p=json.loads(files["config/bountiful/bounty_pools/tmm_farming_objs.json"])
        self.assertEqual(next(iter(p["content"].values()))["amount"],{"min":4,"max":4})
        self.assertFalse(json.loads(files["config/bountiful/bountiful.json"])["bounty"]["allowDecreeMixing"])
    def test_no_positive_cycle(self):
        r=copy.deepcopy(R)
        r["approved_entries"]["farming"]["objectives"][0]["count"]=2
        with self.assertRaises(ValueError):m.compile_candidate(C,A,r)
    def test_no_unknown_registry(self):
        r=copy.deepcopy(R)
        r["registered_items"].remove("minecraft:wheat")
        with self.assertRaises(ValueError):m.compile_candidate(C,A,r)
    def test_no_held_decree(self):
        r=copy.deepcopy(R)
        r["approved_entries"]["vampire"]=r["approved_entries"].pop("farming")
        with self.assertRaises(ValueError):m.compile_candidate(C,A,r)
    def test_held_occultism_and_removed_theurgy(self):
        for key in ("occultism", "theurgy"):
            r=copy.deepcopy(R)
            r["approved_entries"][key]=r["approved_entries"].pop("farming")
            with self.assertRaises(ValueError):m.compile_candidate(C,A,r)
    def test_no_unapproved_settlement(self):
        r=copy.deepcopy(R)
        r["settlement"]="unknown"
        with self.assertRaises(ValueError):m.compile_candidate(C,A,r)
    def test_no_unapproved_reward(self):
        r=copy.deepcopy(R)
        r["approved_reward_items"]=[]
        with self.assertRaises(ValueError):m.compile_candidate(C,A,r)
    def test_no_wrong_artifact(self):
        r=copy.deepcopy(R)
        r["audit_hash"]="wrong"
        with self.assertRaises(ValueError):m.compile_candidate(C,A,r)

    def test_multiple_material_rewards(self):
        r=copy.deepcopy(R)
        r["settlement"]="approved_thematic_materials"
        r["registered_items"].append("minecraft:carrot")
        r["effective_emc"]["minecraft:carrot"]=4
        r["approved_reward_items"].append("minecraft:carrot")
        r["approved_entries"]["farming"]["rewards"]=[{"item":"minecraft:bread","count":1},{"item":"minecraft:carrot","count":2}]
        files=m.compile_candidate(C,A,r)
        p=json.loads(files["config/bountiful/bounty_pools/tmm_farming_rews.json"])
        self.assertEqual(len(p["content"]),2)
        r["approved_entries"]["farming"]["rewards"][1]["count"]=3
        with self.assertRaises(ValueError):m.compile_candidate(C,A,r)
    def test_native_kill_objective(self):
        r=copy.deepcopy(R)
        r["registered_entities"]=["minecraft:zombie"]
        r["approved_kill_entities"]=["minecraft:zombie"]
        r["approved_kill_emc_budget"]={"minecraft:zombie":3}
        r["approved_entries"]={"hunting":{"reward":{"item":"minecraft:bread","count":1},"objectives":[{"type":"entity","entity":"minecraft:zombie","count":2}]}}
        files=m.compile_candidate(C,A,r)
        p=json.loads(files["config/bountiful/bounty_pools/tmm_hunting_objs.json"])
        self.assertEqual(next(iter(p["content"].values()))["type"],"entity")
        r["approved_kill_entities"]=[]
        with self.assertRaises(ValueError):m.compile_candidate(C,A,r)

if __name__=="__main__":unittest.main()
