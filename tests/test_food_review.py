"""Food census safety contract; offline fixture and real source tags only."""
import csv
import gzip
import json
import subprocess
import sys
import tempfile
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "scripts"))
from build_food_review import build

class FoodReviewTests(unittest.TestCase):
    def setUp(self):
        self.items = [
            {"id":"minecraft:stone","tags":"c:stones"},
            {"id":"dragonsurvival:charred_meat","tags":"tmm:food/charred"},
            {"id":"pamhc2crops:tomatoitem","tags":"c:foods/tomato;c:crops/tomato"},
            {"id":"example:stew","tags":""},
            {"id":"example:unknown","tags":""},
        ]
        self.recipes = [
            {"id":"example:stew","type":"farmersdelight:cooking","output":{"id":"example:stew"},"ingredients":[{"truncated":True}]},
            {"id":"example:char","type":"minecraft:smelting","output":{"id":"dragonsurvival:charred_meat"},"ingredients":[]},
        ]
        self.seed = {"items":{"dragonsurvival:charred_meat":{"traits":["charred"]},"example:missing":{"traits":["fruit"]}},
                     "frozen_census":{"item_count":5,"recipe_count":2,"source_commit":"fixture"}}
    def test_classification_not_authorization(self):
        rows, missing = build(self.items,self.recipes,self.seed)
        self.assertEqual(missing,["example:missing"])
        m = {r["id"]:r for r in rows}
        self.assertEqual(m["dragonsurvival:charred_meat"]["traits"],["charred"])
        self.assertEqual(m["pamhc2crops:tomatoitem"]["review_status"],"candidate_review")
        self.assertEqual(m["example:stew"]["culinary_recipe_type_evidence"],["farmersdelight:cooking"])
        self.assertEqual(m["minecraft:stone"]["review_status"],"no_food_evidence_not_classified")
        self.assertEqual(m["example:unknown"]["review_status"],"no_food_evidence_not_classified")
        self.assertTrue(all(r["consumption"]=="unknown" and r["consumer_policy"]=="unresolved" for r in rows))
    def test_deterministic(self):
        self.assertEqual(build(self.items,self.recipes,self.seed),build(self.items[::-1],self.recipes[::-1],self.seed))
    def test_duplicate(self):
        with self.assertRaises(ValueError):
            build(self.items+[self.items[0]],self.recipes,self.seed)
    def test_item_error(self):
        with self.assertRaises(ValueError):
            build(self.items+[{"id":"example:bad","error":"read failed"}],self.recipes,self.seed)
    def test_recipe_error(self):
        with self.assertRaises(ValueError):
            build(self.items,self.recipes+[{"id":"example:bad","error":"read failed"}],self.seed)
    def cli(self, path, capture):
        with (path/"items.csv").open("w",newline="") as f:
            writer=csv.DictWriter(f,fieldnames=["id","tags"])
            writer.writeheader()
            writer.writerows(self.items)
        with gzip.open(path/"capture.gz","wt") as f:
            json.dump(capture,f)
        (path/"seed.json").write_text(json.dumps(self.seed))
        return subprocess.run([sys.executable,str(ROOT/"scripts/build_food_review.py"),"--items",str(path/"items.csv"),"--capture",str(path/"capture.gz"),"--seed",str(path/"seed.json"),"--output",str(path/"out")],capture_output=True,text=True)
    def test_cli_end_to_end(self):
        with tempfile.TemporaryDirectory() as d:
            p=Path(d)
            result=self.cli(p,{"items":self.items,"recipes":self.recipes,"errors":[]})
            self.assertEqual(result.returncode,0,result.stderr)
            summary=json.loads((p/"out/summary.json").read_text())
            self.assertEqual(summary["registry_items"],5)
            self.assertEqual(summary["recipe_slots_truncated"],1)
            self.assertFalse(summary["food_properties_captured"])
            self.assertEqual(len((p/"out/food-review.jsonl").read_text().splitlines()),5)
    def test_cli_mismatch(self):
        with tempfile.TemporaryDirectory() as d:
            p=Path(d)
            result=self.cli(p,{"items":self.items[:-1],"recipes":self.recipes,"errors":[]})
            self.assertNotEqual(result.returncode,0)
            self.assertIn("CSV/capture item registry mismatch",result.stderr)
            self.assertFalse((p/"out").exists())
    def test_cli_capture_errors(self):
        with tempfile.TemporaryDirectory() as d:
            result=self.cli(Path(d),{"items":self.items,"recipes":self.recipes,"errors":["bad"]})
            self.assertNotEqual(result.returncode,0)
            self.assertIn("capture has read errors",result.stderr)
    def test_cli_size_guard(self):
        with tempfile.TemporaryDirectory() as d:
            result=self.cli(Path(d),{"items":self.items,"recipes":[],"errors":[]})
            self.assertNotEqual(result.returncode,0)
            self.assertIn("unexpected census size",result.stderr)
    def test_charred_source_contract(self):
        def read(path): return json.loads((ROOT/path).read_text())
        tag=read("kubejs/data/tmm/tags/item/food/charred.json")
        expected={"dragonsurvival:charred_"+kind for kind in ("meat","seafood","vegetable","mushroom")}
        self.assertFalse(tag["replace"])
        self.assertEqual(len(tag["values"]),4)
        self.assertEqual({v["id"] for v in tag["values"]},expected)
        self.assertTrue(all(v["required"] is False for v in tag["values"]))
        cfb=read("kubejs/data/cookingforblockheads/tags/item/foods.json")
        self.assertFalse(cfb["replace"])
        self.assertIn({"id":"#tmm:food/charred","required":False},cfb["values"])
        seed=read("docs/food/food-registry-seed-v1.json")
        self.assertEqual({k for k,v in seed["items"].items() if "charred" in v["traits"]},expected)

if __name__ == "__main__":
    unittest.main()
