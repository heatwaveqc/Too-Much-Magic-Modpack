"""Bountiful shop and zero-EMC document guard; read-only static tests."""
import hashlib, json, pathlib, re, unittest
ROOT=pathlib.Path(__file__).resolve().parents[1]
class TestBountifulDistribution(unittest.TestCase):
    def test_indexed_live_pilot(self):
        lock=json.loads((ROOT/"manifest/bountiful-live-pilot-integration.json").read_text())
        self.assertEqual(lock["count"],14)
        self.assertEqual(len(lock["files"]),14)
        self.assertEqual(lock["source_pr"],366)
        for name,digest in lock["files"].items():
            self.assertEqual(hashlib.sha256((ROOT/name).read_bytes()).hexdigest(),digest,name)
        cfg=json.loads((ROOT/"config/bountiful/bountiful.json").read_text())
        self.assertEqual(cfg["board"]["villageGenFrequency"],0)
        self.assertEqual(cfg["general"]["dataPathsToExclude"],["bounty_pools/.+","bounty_decrees/.+"])
        self.assertFalse(cfg["bounty"]["allowDecreeMixing"])
        for family in ("hunting","culinary","fishing"):
            name=f"tmm_{family}"
            decree=json.loads((ROOT/f"config/bountiful/bounty_decrees/{name}.json").read_text())
            self.assertEqual(decree["objectives"],[f"{name}_objs"])
            self.assertEqual(decree["rewards"],[f"{name}_rews"])
            self.assertFalse(decree["canWanderBuy"])
            recipe=json.loads((ROOT/f"kubejs/data/tmm/recipe/bountiful/{name}.json").read_text())
            self.assertEqual(recipe["result"]["components"]["bountiful:decree_data"],{"ids":[name],"rank":1})
            self.assertEqual(recipe["ingredients"][0],{"item":"minecraft:paper"})
        pool=json.loads((ROOT/"config/bountiful/bounty_pools/tmm_hunting_objs.json").read_text())["content"]
        gates=[entry for entry in pool.values() if entry["type"]=="criteria"]
        self.assertEqual({x["conditions"]["gateway"] for x in gates},{"tmm:mob_ars_wilden_hunter","tmm:mob_irons_cultist"})
        self.assertTrue(all(x["content"]=="gateways:finish_gateway" and x["amount"]=={"min":1,"max":1} for x in gates))
    def test_shop(self):
        cat=json.loads((ROOT/"manifest/quest_shop_catalog.json").read_text())
        listed=[(c["id"],x) for c in cat["categories"] for x in c["entries"] if x[0]=="bountiful:bountyboard"]
        self.assertEqual(listed,[("supplies",["bountiful:bountyboard",1,5])])
        shop=json.loads((ROOT/"config/qshop/shops/tmm.json").read_text())
        self.assertEqual(shop["id"],"tmm")
        self.assertEqual(shop["currency"],"coins")
        supplies=next(tab for tab in shop["tabs"] if tab["uuid"]=="tmm-supplies")
        entries=[x for x in supplies["entries"] if x["item"]["item"]=="bountiful:bountyboard"]
        self.assertEqual(len(entries),1)
        self.assertEqual(entries[0]["item"]["count"],1)
        self.assertEqual(entries[0]["price"],5)
        self.assertEqual(entries[0]["currency"],"coins")
    def test_emc_document_exclusions(self):
        contents=json.loads((ROOT/"config/ProjectE/custom_emc.json").read_text())
        self.assertEqual({x["id"]:x["emc"] for x in contents["entries"]},{"bountiful:decree":0,"bountiful:bounty":0})
        self.assertNotIn("bountiful:bountyboard",{x["id"] for x in contents["entries"]})
        self.assertEqual(json.loads((ROOT/"config/bountiful/bountiful.json").read_text())["board"]["villageGenFrequency"],0)
        pol=json.loads((ROOT/"manifest/emc-coverage-policy.json").read_text())
        self.assertEqual(set(pol["non_transmutable_document_exceptions"]),{"bountiful:decree","bountiful:bounty"})
    def test_packwiz_hashes(self):
        ix=(ROOT/"index.toml").read_bytes()
        pack=(ROOT/"pack.toml").read_text()
        expected=re.search(r'hash = "([0-9a-f]{64})"',pack).group(1)
        self.assertEqual(hashlib.sha256(ix).hexdigest(),expected)
        data=ix.decode()
        for name in ("config/ProjectE/custom_emc.json","config/bountiful/bountiful.json","config/qshop/shops/tmm.json",*json.loads((ROOT/"manifest/bountiful-live-pilot-integration.json").read_text())["files"]):
            rows=[s for s in data.split("[[files]]") if f'file = "{name}"' in s]
            self.assertEqual(len(rows),1)
            h=re.search(r'hash = "([0-9a-f]{64})"',rows[0]).group(1)
            self.assertEqual(hashlib.sha256((ROOT/name).read_bytes()).hexdigest(),h)
if __name__=="__main__":unittest.main()
