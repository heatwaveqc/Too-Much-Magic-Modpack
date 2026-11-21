"""Regression checks for final first-pass food screen."""
import sys
import unittest
from pathlib import Path
sys.path.insert(0,str(Path(__file__).resolve().parents[1]/"scripts"))
from complete_food_first_pass import classify

RULES={"tag_traits":{"c:foods/soup":["prepared_meal","soup"],"brewinandchewin:wines":["beverage","alcohol_candidate"],"brewinandchewin:fermented_drinks":["beverage","fermented"]},"exact_hints":{"witchery:bottle_of_blood":["blood","component_sensitive","special_consumable"]}}
def row(id,tags=(),types=(),traits=(),state="no_food_evidence_not_classified"):
    return {"id":id,"native_tags":list(tags),"recipe_output_types":list(types),"traits":list(traits),"review_status":state}
class FirstPassTests(unittest.TestCase):
    def test_stock_no_inheritance(self):
        old={"traits":["stock"],"batches":["intermediates"],"review_flags":[]}
        a=row("pamhc2foodcore:stockitem",types=["minecraft:crafting"])
        a["recipe_inputs"]=["minecraft:beef"]
        b=classify(a,old,RULES)
        a["recipe_inputs"]=["minecraft:carrot"]
        self.assertEqual(b,classify(a,old,RULES))
        self.assertEqual(b["traits"],["stock"])
    def test_keg_food_not_alcohol(self):
        r=classify(row("brewinandchewin:cocoa_fudge",types=["brewinandchewin:fermenting"]),None,RULES)
        self.assertNotIn("alcohol_candidate",r["traits"])
    def test_wine_provisional(self):
        r=classify(row("brewinandchewin:wine",["brewinandchewin:wines"]),None,RULES)
        self.assertIn("alcohol_candidate",r["traits"])
        self.assertEqual(r["status"],"targeted_review")
        self.assertEqual(r["consumer_policy"],"unresolved")
    def test_tag_generic(self):
        r=classify(row("example:food",["c:foods"]),None,RULES)
        self.assertEqual(r["traits"],["generic_food"])
    def test_equipment_not_food(self):
        r=classify(row("farmersdelight:golden_knife",["c:tools/knife"],state="candidate_review"),None,RULES)
        self.assertEqual(r["status"],"nonfood_or_planting_review")
    def test_no_namespace_only_food(self):
        self.assertEqual(classify(row("pamhc2foodcore:pot"),None,RULES)["status"],"no_food_evidence_not_classified")
    def test_name_hint_not_authority(self):
        r=classify(row("pamhc2foodcore:applepieitem"),None,RULES)
        self.assertEqual(r["status"],"generic_candidate")
        self.assertEqual(r["consumption"],"unknown")
    def test_special_blood_component(self):
        r=classify(row("witchery:bottle_of_blood"),None,RULES)
        self.assertIn("component_sensitive",r["traits"])
        self.assertEqual(r["consumer_policy"],"unresolved")
    def test_prior_flags_retained(self):
        old={"traits":["raw_fish"],"batches":["animal_products"],"review_flags":["historical_candidate_only"]}
        r=classify(row("example:fish"),old,RULES)
        self.assertEqual(r["status"],"targeted_review")
        self.assertIn("historical_candidate_only",r["review_flags"])
    def test_untagged_unknown_not_edible(self):
        self.assertEqual(classify(row("example:apple_sword"),None,RULES)["status"],"no_food_evidence_not_classified")
    def test_false_positive_bale(self):
        r=classify(row("farmersdelight:rice_bale"),None,RULES)
        self.assertNotIn("alcohol_candidate",r["traits"])
    def test_rootbeer_not_inferred_alcohol(self):
        r=classify(row("pamhc2foodextended:rootbeerfloatitem"),None,RULES)
        self.assertNotIn("alcohol_candidate",r["traits"])
    def test_prior_candidate_not_lost(self):
        r=classify(row("example:unknown",state="candidate_review"),None,RULES)
        self.assertEqual(r["status"],"generic_candidate")
if __name__=="__main__":
    unittest.main()
