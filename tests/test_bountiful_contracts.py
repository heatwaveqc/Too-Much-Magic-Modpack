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

if __name__=="__main__":unittest.main()
