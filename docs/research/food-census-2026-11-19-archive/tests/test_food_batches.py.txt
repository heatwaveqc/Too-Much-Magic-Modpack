"""First-three-batches classification and equivalence review safety."""
import sys
import unittest
from pathlib import Path
sys.path.insert(0,str(Path(__file__).resolve().parents[1]/"scripts"))
from classify_food_batches import classify,audit_groups

RULES={"tag_traits":{"c:foods/fruit":["fruit"],"c:foods/raw_fish":["raw_fish"],"c:foods/cooked_fish":["cooked_fish"],"c:foods/stock":["stock"],"c:foods/soup":["soup"],"cookingforblockheads:utensils":["utensil"]},"exact_hints":{},"equivalence_groups":[{"id":"test","tag":"c:foods/fruit","ids":["test:a","test:b"],"status":"review"}]}
def row(id="test:food",tags=(),traits=(),state="candidate_review"):
    return {"id":id,"native_tags":list(tags),"traits":list(traits),"review_status":state}
class BatchTests(unittest.TestCase):
    def test_native_not_permission(self):
        r=classify(row(tags=["c:foods/fruit"]),RULES)
        self.assertEqual(r["status"],"batch_provisional")
        self.assertEqual(r["consumer_policy"],"unresolved")
        self.assertEqual(r["consumption"],"unknown")
    def test_stock_does_not_inherit(self):
        a=row(tags=["c:foods/stock"])
        a["recipe_inputs"]=["minecraft:beef","minecraft:salmon"]
        self.assertEqual(classify(a,RULES)["traits"],["stock"])
        a["recipe_inputs"]=["minecraft:carrot"]
        self.assertEqual(classify(a,RULES)["traits"],["stock"])
    def test_mixed_hold(self):
        r=classify(row(tags=["c:foods/soup","c:foods/raw_fish"]),RULES)
        self.assertEqual(r["status"],"targeted_review")
    def test_seed_hold(self):
        self.assertEqual(classify(row(tags=["c:seeds/wheat","c:foods/fruit"]),RULES)["status"],"targeted_review")
    def test_raw_cooked_conflict(self):
        self.assertEqual(classify(row(tags=["c:foods/raw_fish","c:foods/cooked_fish"]),RULES)["status"],"targeted_review")
    def test_old_candidate_hold(self):
        self.assertEqual(classify(row(traits=["raw_fish_candidate"]),RULES)["status"],"targeted_review")
    def test_special_hold(self):
        self.assertEqual(classify(row(traits=["fruit","gilded_food"]),RULES)["status"],"targeted_review")
    def test_no_name_guess(self):
        self.assertEqual(classify(row(id="test:chicken_sword",state="no_food_evidence_not_classified"),RULES)["status"],"no_batch_evidence")
    def test_equivalence_slot_truncation_and_asymmetry(self):
        rows=[row("test:a",["c:foods/fruit"]),row("test:b")]
        recipes=[{"id":"test:one","type":"minecraft:crafting","ingredients":[{"choices":[{"id":"test:a"}]}]},
                 {"id":"test:both","type":"minecraft:crafting","ingredients":[{"choices":[{"id":"test:a"},{"id":"test:b"}]}]},
                 {"id":"test:trunc","type":"minecraft:crafting","ingredients":[{"choices":[{"id":"test:a"}],"truncated":True}]}]
        g=audit_groups({"recipes":recipes},RULES,rows)[0]
        self.assertEqual(g["declared_not_in_native_tag"],["test:b"])
        self.assertEqual((g["crafting_slots_partial"],g["crafting_slots_full"],g["truncated_slots_touching_group"]),(1,1,1))
if __name__=="__main__":
    unittest.main()
