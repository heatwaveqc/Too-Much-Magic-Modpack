"""Read-only direct-gold crafting review. Never generates or changes gameplay tags."""
import argparse
import gzip
import json
from pathlib import Path

GOLD = {"minecraft:gold_nugget", "minecraft:gold_ingot", "minecraft:gold_block"}

def audit(capture, explicit, food_candidates):
    items = {r["id"]:r for r in capture["items"]}
    results = {}
    for recipe in capture["recipes"]:
        if recipe.get("type") != "minecraft:crafting":
            continue
        output = recipe.get("output", {}).get("id")
        if output not in items or output == "minecraft:air":
            continue
        guaranteed = False
        possible = False
        for slot in recipe.get("ingredients", []):
            choices = {x["id"] for x in slot.get("choices", [])}
            possible |= bool(choices & GOLD)
            guaranteed |= bool(choices) and choices <= GOLD and not slot.get("truncated") and slot.get("totalAlternatives", len(choices)) == len(slot.get("choices", []))
        if not possible:
            continue
        row = results.setdefault(output, {"id":output,"name":items[output].get("name",""),"food_evidence":output in food_candidates,"recipe_evidence":[]})
        row["recipe_evidence"].append({"id":recipe["id"],"gold":"required" if guaranteed else "optional_or_truncated"})
    return {"explicit_present":sorted(explicit & items.keys()),"explicit_absent":sorted(explicit-items.keys()),"food_candidates":sorted([v for k,v in results.items() if v["food_evidence"]],key=lambda v:v["id"]),"other_gold_outputs":sorted([v for k,v in results.items() if not v["food_evidence"]],key=lambda v:v["id"]),"scope":"direct crafting only; custom/nonstandard recipes and nonvanilla gold equivalents require separate review; candidate evidence is not food confirmation"}

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--capture",type=Path,required=True)
    p.add_argument("--review",type=Path,required=True)
    p.add_argument("--output",type=Path,required=True)
    p.add_argument("--tag",type=Path,default=Path("kubejs/data/tmm/tags/item/food/gilded.json"))
    a=p.parse_args()
    with gzip.open(a.capture,"rt",encoding="utf-8") as f:
        capture=json.load(f)
    explicit={v["id"] if isinstance(v,dict) else v for v in json.loads(a.tag.read_text())["values"]}
    with a.review.open(encoding="utf-8") as f:
        food={r["id"] for r in map(json.loads,f) if r["review_status"]!="no_food_evidence_not_classified"}
    result=audit(capture,explicit,food)
    a.output.write_text(json.dumps(result,indent=2,sort_keys=True)+"\n")
    print(json.dumps({k:v for k,v in result.items() if k!="other_gold_outputs"},sort_keys=True))
    print("Other direct gold outputs for food-property review:",len(result["other_gold_outputs"]))
if __name__=="__main__":
    main()
