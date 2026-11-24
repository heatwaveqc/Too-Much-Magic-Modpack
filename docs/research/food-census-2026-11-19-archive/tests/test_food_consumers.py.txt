"""Offline consumer policy audit guards."""
import sys
import unittest
from pathlib import Path
sys.path.insert(0,str(Path(__file__).resolve().parents[1]/"scripts"))
from audit_food_consumers import assess,CHARRED,GOLDEN,SPECIES
def sample():
    ids=set(CHARRED|GOLDEN)|{"pastel:enchanted_star_candy","artifacts:eternal_steak","artifacts:everlasting_beef","evilcraft:flesh_rejuvenated","botania:infinite_fruit","witchery:bottle_of_blood","aquaculture:sushi","aquaculture:fish_fillet_raw"}
    records=[["witchery:bottle_of_blood","targeted_review",["blood","component_sensitive"],[],[]],["artifacts:eternal_steak","targeted_review",["reusable_food"],[],[]]]
    d={"values":{s:{"neoforge:value":[{"items":x} for x in CHARRED]+([{"items":"#c:foods/raw_fish"},{"items":"aquaculture:sushi"},{"items":"aquaculture:fish_fillet_raw"}] if s.endswith("sea_dragon") else []) + [{"items":"evilcraft:flesh_rejuvenated","retain_effects":True,"properties":{"nutrition":0,"saturation":0}},{"items":"botania:infinite_fruit","retain_effects":True,"properties":{"nutrition":0,"saturation":0}}]} for s in SPECIES}}
    return ids,records,d,{"values":[{"id":x} for x in GOLDEN]},{"values":[{"id":x} for x in CHARRED]},{"groups":[]}
class ConsumerAuditTests(unittest.TestCase):
    def test_component_blood_is_not_general_food(self):
        x=assess(*sample())
        self.assertIn("jawr_blood_component_variants",{r["code"] for r in x["issues"]})
        self.assertEqual(x["summary"]["runtime_changes"],0)
    def test_stale_magic_selector_detected_without_alias(self):
        args=list(sample())
        args[2]["values"]["dragonsurvival:cave_dragon"]["neoforge:value"].append({"items":"spectrum:enchanted_star_candy"})
        r=assess(*args)
        bad=next(x for x in r["issues"] if x["code"]=="dragon_stale_spectrum_selectors")
        self.assertEqual(bad["evidence"]["count"],1)
        self.assertTrue(bad["evidence"]["selectors"][0]["candidate_in_registry"])
    def test_regex_is_not_absent_item_id(self):
        args=list(sample())
        args[2]["values"]["dragonsurvival:forest_dragon"]["neoforge:value"].append({"items":"pastel:.*candy"})
        r=assess(*args)
        self.assertEqual(r["summary"]["dragon_installed_namespace_missing_selectors"],0)
        self.assertEqual(r["summary"]["dragon_regex_selectors"],1)
        self.assertIn("dragon_regex_selector_runtime_review",{i["code"] for i in r["issues"]})
    def test_optional_foreign_does_not_force_explicit_allow(self):
        args=list(sample())
        args[2]["values"]["dragonsurvival:forest_dragon"]["neoforge:value"].append({"items":"uninstalledmod:food"})
        r=assess(*args)
        self.assertEqual(r["summary"]["dragon_optional_foreign_selectors"],1)
    def test_charred_exact_excludes_charge(self):
        args=list(sample())
        args[4]["values"].append({"id":"dragonsurvival:charged_coal"})
        r=assess(*args)
        self.assertIn("charred_tag_drift",{x["code"] for x in r["issues"]})
    def test_no_per_recipe_diet_logic(self):
        a=sample()
        b=sample()
        b[1][1].append("ignored-recipe")
        self.assertEqual(assess(*a)["summary"],assess(*b)["summary"])
    def test_reusable_resource_zero_hold(self):
        args=list(sample())
        r=assess(*args)
        self.assertNotIn("magical_free_food_or_effect_loss",{x["code"] for x in r["issues"]})
        args[2]["values"]["dragonsurvival:forest_dragon"]["neoforge:value"].append({"items":"evilcraft:flesh_rejuvenated","retain_effects":False,"properties":{"nutrition":4,"saturation":4}})
        r=assess(*args)
        self.assertIn("magical_free_food_or_effect_loss",{x["code"] for x in r["issues"]})
    def test_gilded_tag_exact(self):
        args=list(sample())
        args[3]["values"].append({"id":"minecraft:golden_knife"})
        r=assess(*args)
        self.assertIn("gilded_tag_drift",{x["code"] for x in r["issues"]})
    def test_overlay_must_be_registry_backed(self):
        args=list(sample())
        args[1].append(["missing:fake","provisional",[],[],[]])
        with self.assertRaises(ValueError):assess(*args)
if __name__=="__main__":
    unittest.main()
