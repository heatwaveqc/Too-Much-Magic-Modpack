"""Read-only canonical food review census. Does not generate active game tags."""
import argparse
import collections
import csv
import gzip
import json
from pathlib import Path

def build(items, recipes, seed):
    by_id = {}
    for row in items:
        item = row["id"]
        if item in by_id or row.get("error"):
            raise ValueError(f"duplicate or failed item row: {item}")
        by_id[item] = row
    observed = collections.defaultdict(set)
    for recipe in recipes:
        if recipe.get("error"):
            raise ValueError(f"failed recipe: {recipe.get('id')}")
        typ = recipe.get("type", "")
        out = recipe.get("output", {}).get("id")
        if out in by_id:
            observed[out].add(typ)
    seeds = seed["items"]
    rows = []
    for item, row in sorted(by_id.items()):
        tags = sorted(t for t in row.get("tags", "").split(";") if t)
        food_tags = [t for t in tags if t.startswith(("c:foods", "c:drinks", "c:crops", "c:seeds", "cookingforblockheads:"))]
        types = sorted(observed[item])
        culinary_types = [t for t in types if t in ("farmersdelight:cooking", "farmersdelight:cutting", "brewinandchewin:fermenting") or t.startswith("cocktailsdelight:")]
        known = seeds.get(item)
        evidence = bool(known or food_tags or culinary_types)
        rows.append({"id":item,"name":row.get("name",""),"review_status":"seed_review" if known else "candidate_review" if evidence else "no_food_evidence_not_classified","traits":known["traits"] if known else [],"seed":known,"food_related_tags":food_tags,"native_tags":tags,"recipe_output_types":types,"culinary_recipe_type_evidence":culinary_types,"consumption":"unknown","consumer_policy":"unresolved"})
    return rows, sorted(set(seeds)-set(by_id))

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--items",type=Path,required=True)
    p.add_argument("--capture",type=Path,required=True)
    p.add_argument("--seed",type=Path,default=Path("docs/food/food-registry-seed-v1.json"))
    p.add_argument("--output",type=Path,required=True)
    args=p.parse_args()
    seed=json.loads(args.seed.read_text(encoding="utf-8"))
    with args.items.open(encoding="utf-8",newline="") as f:
        items=list(csv.DictReader(f))
    with gzip.open(args.capture,"rt",encoding="utf-8") as f:
        capture=json.load(f)
    ids=[r["id"] for r in capture["items"]]
    if len(ids)!=len(set(ids)) or set(ids)!={r["id"] for r in items} or len(items)!=len(ids):
        raise ValueError("CSV/capture item registry mismatch")
    if any(r.get("error") for r in capture["items"]) or capture.get("errors"):
        raise ValueError("capture has read errors")
    if len(ids)!=seed["frozen_census"]["item_count"] or len(capture["recipes"])!=seed["frozen_census"]["recipe_count"]:
        raise ValueError("unexpected census size; update provenance explicitly for a new census")
    rows,missing=build(items,capture["recipes"],seed)
    args.output.mkdir(parents=True,exist_ok=True)
    with (args.output/"food-review.jsonl").open("w",encoding="utf-8") as f:
        for row in rows: f.write(json.dumps(row,ensure_ascii=True,sort_keys=True)+"\n")
    counts=dict(collections.Counter(r["review_status"] for r in rows))
    summary={"registry_items":len(rows),"counts":counts,"missing_seed_ids":missing,"food_properties_captured":False,"source_commit":seed["frozen_census"]["source_commit"],"recipe_slots_truncated":sum(bool(i.get("truncated")) for r in capture["recipes"] for i in r.get("ingredients",[]))}
    (args.output/"summary.json").write_text(json.dumps(summary,indent=2,sort_keys=True)+"\n",encoding="utf-8")
    print(json.dumps(summary,sort_keys=True))

if __name__=="__main__":
    main()
