"""Bountiful shop and zero-EMC document guard; read-only static tests."""
import hashlib, json, pathlib, re, unittest
ROOT=pathlib.Path(__file__).resolve().parents[1]
class TestBountifulDistribution(unittest.TestCase):
    def test_shop(self):
        cat=json.loads((ROOT/"manifest/quest_shop_catalog.json").read_text())
        listed=[(c["id"],x) for c in cat["categories"] for x in c["entries"] if x[0]=="bountiful:bountyboard"]
        self.assertEqual(listed,[("supplies",["bountiful:bountyboard",1,5])])
        entries=json.loads((ROOT/"kubejs/data/tmm/questshop/shop_entries/supplies.json").read_text())
        self.assertEqual([x for x in entries if x["item"]=="bountiful:bountyboard"],[{"item":"bountiful:bountyboard","amount":1,"cost":5,"category":"tmm:supplies"}])
    def test_emc_document_exclusions(self):
        contents=json.loads((ROOT/"config/ProjectE/custom_emc.json").read_text())
        self.assertEqual({x["id"]:x["emc"] for x in contents["entries"]},{"bountiful:decree":0,"bountiful:bounty":0})
        self.assertNotIn("bountiful:bountyboard",{x["id"] for x in contents["entries"]})
        pol=json.loads((ROOT/"manifest/emc-coverage-policy.json").read_text())
        self.assertEqual(set(pol["non_transmutable_document_exceptions"]),{"bountiful:decree","bountiful:bounty"})
    def test_packwiz_hashes(self):
        ix=(ROOT/"index.toml").read_bytes()
        pack=(ROOT/"pack.toml").read_text()
        expected=re.search(r'hash = "([0-9a-f]{64})"',pack).group(1)
        self.assertEqual(hashlib.sha256(ix).hexdigest(),expected)
        data=ix.decode()
        for name in ("config/ProjectE/custom_emc.json","kubejs/data/tmm/questshop/shop_entries/supplies.json"):
            rows=[s for s in data.split("[[files]]") if f'file = "{name}"' in s]
            self.assertEqual(len(rows),1)
            h=re.search(r'hash = "([0-9a-f]{64})"',rows[0]).group(1)
            self.assertEqual(hashlib.sha256((ROOT/name).read_bytes()).hexdigest(),h)
if __name__=="__main__":unittest.main()
