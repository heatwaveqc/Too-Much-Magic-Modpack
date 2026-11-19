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
    def test_removed_theurgy(self):
        r=copy.deepcopy(R)
        r["approved_entries"]["theurgy"]=r["approved_entries"].pop("farming")
        with self.assertRaises(ValueError):m.compile_candidate(C,A,r)
    def test_locked_occultism_rewards(self):
        expected={"minecraft:skeleton_skull","minecraft:wither_skeleton_skull","occultism:spirit_attuned_gem","occultism:afrit_essence","occultism:marid_essence"}
        occ=next(f for f in C["schools"] if f["id"]=="occultism")
        self.assertEqual(set(occ["reward_item_candidates"]),expected)
        r=copy.deepcopy(R)
        r["registered_items"]=sorted(expected | {"minecraft:gold_ingot","occultism:chalk_gold"})
        r["effective_emc"]={item:1 for item in r["registered_items"]}
        r["approved_reward_items"]=sorted(expected | {"occultism:chalk_gold"})
        r["approved_entries"]={"occultism":{"rewards":[{"item":item,"count":1} for item in sorted(expected)],"objectives":[{"item":"minecraft:gold_ingot","count":4}]}}
        files=m.compile_candidate(C,A,r)
        pool=json.loads(files["config/bountiful/bounty_pools/tmm_occultism_rews.json"])
        self.assertEqual({e["content"] for e in pool["content"].values()},expected)
        r["approved_entries"]["occultism"]["rewards"].append({"item":"occultism:chalk_gold","count":1})
        with self.assertRaisesRegex(ValueError,"owner-locked"):m.compile_candidate(C,A,r)
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

    def test_native_reputation_and_pair_exclusions(self):
        r=copy.deepcopy(R)
        r["balance_profile"]="native_v1"
        r["registered_items"]+=["minecraft:golden_carrot","minecraft:carrot"]
        r["effective_emc"].update({"minecraft:golden_carrot":100,"minecraft:carrot":16})
        r["approved_reward_items"].append("minecraft:golden_carrot")
        r["approved_entries"]={"farming":{"rewards":[{"item":"minecraft:bread","min":1,"max":2,"rarity":"COMMON"},{"item":"minecraft:golden_carrot","min":1,"max":1,"rarity":"UNCOMMON"}],"objectives":[{"item":"minecraft:wheat","min":1,"max":180},{"item":"minecraft:carrot","min":1,"max":64}]}}
        files=m.compile_candidate(C,A,r)
        cfg=json.loads(files["config/bountiful/bountiful.json"])["bounty"]
        self.assertEqual(cfg["fillerDifficultyModifierPercent"],75)
        rews=json.loads(files["config/bountiful/bounty_pools/tmm_farming_rews.json"])["content"]
        objs=json.loads(files["config/bountiful/bounty_pools/tmm_farming_objs.json"])["content"]
        self.assertEqual(rews["item_minecraft_bread"]["repRequired"],-30)
        self.assertEqual(rews["item_minecraft_golden_carrot"]["repRequired"],0)
        self.assertIn({"type":"item","content":"minecraft:wheat"},rews["item_minecraft_golden_carrot"]["forbids"])
        for rew in rews.values():
            for obj in objs.values():
                if {"type":obj["type"],"content":obj["content"]} in rew.get("forbids",[]):continue
                for count in range(rew["amount"]["min"],rew["amount"]["max"]+1):
                    for rep in range(rew["repRequired"],31):
                        import math
                        target=rew["unitWorth"]*count*1.75*(1-rep/75)
                        n=min(obj["amount"]["max"],max(obj["amount"]["min"],math.ceil(target/obj["unitWorth"])))
                        self.assertGreaterEqual(n*r["effective_emc"][obj["content"]],count*r["effective_emc"][rew["content"]])
                        self.assertGreaterEqual(n*obj["unitWorth"]+1e-8,target)
    def test_native_rejects_no_feasible_objective(self):
        r=copy.deepcopy(R)
        r["balance_profile"]="native_v1"
        r["approved_entries"]["farming"]["reward"]["rarity"]="EPIC"
        with self.assertRaisesRegex(ValueError,"no feasible objective"):m.compile_candidate(C,A,r)
    def test_native_premium_only_has_no_rep_deadlock(self):
        r=copy.deepcopy(R)
        r["balance_profile"]="native_v1"
        r["registered_items"]=["astralsorcery:constellation_paper","minecraft:gold_ingot"]
        r["effective_emc"]={"astralsorcery:constellation_paper":512,"minecraft:gold_ingot":144}
        r["approved_reward_items"]=["astralsorcery:constellation_paper"]
        r["approved_entries"]={"astral_sorcery":{"rewards":[{"item":"astralsorcery:constellation_paper","count":1,"rarity":"EPIC"}],"objectives":[{"item":"minecraft:gold_ingot","min":1,"max":128}]}}
        files=m.compile_candidate(C,A,r)
        rew=next(iter(json.loads(files["config/bountiful/bounty_pools/tmm_astral_sorcery_rews.json"])["content"].values()))
        self.assertEqual(rew["repRequired"],-30)
        self.assertEqual(rew["unitWorth"],256)
        r["approved_entries"]["astral_sorcery"]["rewards"][0]["rarity"]="COMMON"
        with self.assertRaisesRegex(ValueError,"premium"):m.compile_candidate(C,A,r)

    def test_iron_all_ink_tiers_in_balance_draft(self):
        family=next(f for f in C["schools"] if f["id"]=="irons_spells")
        payouts={row["item"]:row for row in family["balance_reward_draft"]}
        for tier in ("common","uncommon","rare","epic","legendary"):
            item="irons_spellbooks:"+tier+"_ink"
            self.assertIn(item,family["reward_item_candidates"])
            self.assertEqual(payouts[item]["rarity"],tier.upper())
        self.assertIn("irons_spellbooks:arcane_essence",payouts)

    def test_board_worldgen_disabled(self):
        files=m.compile_candidate(C,A,R)
        cfg=json.loads(files["config/bountiful/bountiful.json"])
        self.assertEqual(cfg["board"]["villageGenFrequency"],0)
        self.assertEqual(C["board_distribution"]["inn"]["starting_decree"],"tmm_hunting")
    def test_gateway_criteria_native_and_group_safe(self):
        r=copy.deepcopy(R)
        r["balance_profile"]="native_v1"
        r["registered_items"]=["minecraft:bone","minecraft:arrow","minecraft:emerald"]
        r["effective_emc"]={"minecraft:bone":32,"minecraft:arrow":4,"minecraft:emerald":512}
        r["approved_reward_items"]=["minecraft:arrow","minecraft:emerald"]
        gates=["tmm:mob_ars_wilden_hunter","tmm:mob_irons_pyromancer"]
        r["registered_finite_gateways"]=gates
        r["approved_gateway_ids"]=gates
        r["approved_gateway_effort_emc"]={gates[0]:512,gates[1]:2048}
        r["approved_entries"]={"hunting":{"rewards":[{"item":"minecraft:arrow","count":16,"rarity":"COMMON"},{"item":"minecraft:emerald","count":1,"rarity":"RARE"}],"objectives":[{"type":"gateway","gateway":g,"min":1,"max":1} for g in gates]+[{"item":"minecraft:bone","min":4,"max":128}]}}
        files=m.compile_candidate(C,A,r)
        obs=json.loads(files["config/bountiful/bounty_pools/tmm_hunting_objs.json"])["content"]
        gate_entries=[e for e in obs.values() if e["type"]=="criteria"]
        self.assertEqual({e["conditions"]["gateway"] for e in gate_entries},set(gates))
        self.assertTrue(all(e["content"]=="gateways:finish_gateway" and e["amount"]=={"min":1,"max":1} for e in gate_entries))
        rewards=json.loads(files["config/bountiful/bounty_pools/tmm_hunting_rews.json"])["content"]
        self.assertIn({"type":"criteria","content":"gateways:finish_gateway"},rewards["item_minecraft_emerald"]["forbids"])
        self.assertNotIn({"type":"criteria","content":"gateways:finish_gateway"},rewards["item_minecraft_arrow"].get("forbids",[]))
        r["approved_gateway_ids"]=[]
        with self.assertRaisesRegex(ValueError,"unreviewed finite gateway"):m.compile_candidate(C,A,r)

if __name__=="__main__":unittest.main()
