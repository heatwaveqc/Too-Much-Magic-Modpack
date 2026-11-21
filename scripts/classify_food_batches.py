"""Offline first three food batches. Classification evidence only; no game tag writes."""
import argparse
import collections
import gzip
import json
from pathlib import Path

BATCHES = {
    "agriculture": {"fruit","berry","vegetable","fungus","grain","nut","legume","aquatic_plant","sugar"},
    "animal_products": {"raw_meat","cooked_meat","raw_fish","cooked_fish","raw_seafood","cooked_seafood","egg","cooked_egg","milk","dairy","cheese","butter","terrestrial_meat","seafood"},
    "intermediates": {"flour","dough","pasta","salt","oil","stock","condiment","culinary_intermediate"}
}
FOOD_NAMESPACES = {"pamhc2crops","pamhc2trees","pamhc2foodcore","pamhc2foodextended","farmersdelight","aquaculture","brewinandchewin","cocktailsdelight","beachparty"}
GENERIC = {"stock","soup","prepared_meal","placed_food","utensil"}
SEED_CANDIDATES = {"fruit_candidate":"fruit","vegetable_candidate":"vegetable","raw_meat_candidate":"raw_meat","raw_fish_candidate":"raw_fish"}

def classify(row, rules):
    item = row["id"]
    tags = set(row.get("native_tags", []))
    evidence = collections.defaultdict(set)
    for trait in row.get("traits", []):
        evidence[SEED_CANDIDATES.get(trait, trait)].add("seed:"+trait)
    for tag in tags:
        for trait in rules["tag_traits"].get(tag, []):
            evidence[trait].add("native_tag:"+tag)
    for trait in rules["exact_hints"].get(item, []):
        evidence[trait].add("exact_id_hint")
    # No transitive recipe evidence: an identical stock stays identical, always.
    # Generic mixed/stock or utensil identities do not auto-qualify biological diets.
    seed_traits = set(row.get("traits", []))
    seed_or_hint = bool(seed_traits or rules["exact_hints"].get(item))
    planting = any(t == "c:seeds" or t.startswith("c:seeds/") or t == "c:crops/seeds" for t in tags)
    candidate = row["review_status"]!="no_food_evidence_not_classified" or bool(evidence)
    traits=set(evidence)
    batch_traits=traits & set().union(*BATCHES.values())
    conflicts=[]
    if "utensil" in traits:
        conflicts.append("utensil_not_ordinary_ingredient")
    if planting and not seed_or_hint:
        conflicts.append("planting_material_not_food_proof")
    if traits & {"stock","soup","prepared_meal","placed_food"} and batch_traits - {"stock","culinary_intermediate"}:
        conflicts.append("mixed_or_generic_no_diet_inference")
    if {"raw_meat","cooked_meat"} <= traits or {"raw_fish","cooked_fish"} <= traits:
        conflicts.append("raw_cooked_conflicting_evidence")
    if traits & {"magical_nourishment","reusable_food","hazardous","native_special","gilded_food","charred","blood_candidate"} and batch_traits:
        conflicts.append("special_consumer_review")
    if any(t.startswith("seed:") and t.endswith("_candidate") for v in evidence.values() for t in v) and not any(t.startswith("native_tag:") for v in evidence.values() for t in v):
        conflicts.append("historical_candidate_only")
    batches=sorted(k for k,v in BATCHES.items() if v & batch_traits)
    status="no_batch_evidence"
    if batches:
        status="targeted_review" if conflicts else "batch_provisional"
    elif candidate:
        status="later_batch_or_nonfood_review"
    return {"id":item,"name":row.get("name",""),"status":status,"batches":batches,"traits":sorted(traits),"evidence":{k:sorted(v) for k,v in sorted(evidence.items())},"review_flags":sorted(conflicts),"consumer_policy":"unresolved","consumption":"unknown","prior_status":row["review_status"]}

def audit_groups(capture, rules, rows):
    index={r["id"]:r for r in rows}
    out=[]
    for group in rules["equivalence_groups"]:
        declared=set(group["ids"])
        present=declared & index.keys()
        native={k for k,r in index.items() if group["tag"] in r.get("native_tags",[])}
        report={"id":group["id"],"tag":group["tag"],"status":group["status"],"present":sorted(present),"absent":sorted(declared-present),"declared_not_in_native_tag":sorted(present-native),"native_tag_members":sorted(native),"crafting_slots_partial":0,"crafting_slots_full":0,"truncated_slots_touching_group":0,"partial_recipe_samples":[]}
        samples=set()
        for recipe in capture["recipes"]:
            if recipe.get("type")!="minecraft:crafting":
                continue
            for slot in recipe.get("ingredients",[]):
                choices={v["id"] for v in slot.get("choices",[])}
                if not choices & present:
                    continue
                if slot.get("truncated"):
                    report["truncated_slots_touching_group"]+=1
                elif present <= choices:
                    report["crafting_slots_full"]+=1
                else:
                    report["crafting_slots_partial"]+=1
                    samples.add(recipe["id"])
        report["partial_recipe_samples"]=sorted(samples)[:5]
        out.append(report)
    return out

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--capture",type=Path,required=True)
    p.add_argument("--review",type=Path,required=True)
    p.add_argument("--rules",type=Path,default=Path("docs/food/batch-1-3-rules-v1.json"))
    p.add_argument("--output",type=Path,required=True)
    a=p.parse_args()
    rules=json.loads(a.rules.read_text())
    with a.review.open(encoding="utf-8") as f: rows=list(map(json.loads,f))
    with gzip.open(a.capture,"rt",encoding="utf-8") as f: capture=json.load(f)
    if len(rows)!=len({r["id"] for r in rows}) or {r["id"] for r in rows}!={r["id"] for r in capture["items"]}:
        raise ValueError("review/capture mismatch")
    classified=[classify(r,rules) for r in rows]
    active=[r for r in classified if r["status"]!="no_batch_evidence"]
    groups=audit_groups(capture,rules,rows)
    summary={"registry_items":len(rows),"status_counts":dict(collections.Counter(r["status"] for r in classified)),
        "provisional_by_batch":{k:sum(r["status"]=="batch_provisional" and k in r["batches"] for r in classified) for k in BATCHES},
        "review_by_batch":{k:sum(r["status"]=="targeted_review" and k in r["batches"] for r in classified) for k in BATCHES},
        "new_provisional_from_candidate":sum(r["status"]=="batch_provisional" and r["prior_status"]=="candidate_review" for r in classified),
        "newly_screened_no_prior_evidence":sum(r["status"]!="no_batch_evidence" and r["prior_status"]=="no_food_evidence_not_classified" for r in classified),
        "review_flags":dict(collections.Counter(t for r in classified if r["batches"] for t in r["review_flags"])),
        "exact_hints_missing":sorted(set(rules["exact_hints"])-{r["id"] for r in rows}),
        "pastel_enchanted_food_ids":sorted(r["id"] for r in rows if r["id"].startswith("pastel:") and ("golden_carrot" in r["id"] or "star_candy" in r["id"])),
        "scope":"candidate-only; no food-property proof or gameplay tag generation; batch counts overlap"}
    a.output.mkdir(parents=True,exist_ok=True)
    (a.output/"batch-1-3-review.jsonl").write_text("".join(json.dumps(r,sort_keys=True)+"\n" for r in active),encoding="utf-8")
    (a.output/"batch-1-3-summary.json").write_text(json.dumps(summary,indent=2,sort_keys=True)+"\n")
    (a.output/"ingredient-equivalence-audit.json").write_text(json.dumps(groups,indent=2,sort_keys=True)+"\n")
    print("FOOD_BATCH_SUMMARY "+json.dumps(summary,sort_keys=True))
    for g in groups:
        print("FOOD_EQUIVALENCE "+json.dumps({k:v for k,v in g.items() if k!="partial_recipe_samples"},sort_keys=True))
    # Compact reproducible overlay for maintaining exact-ID classifications in Git.
    for r in active:
        if r["batches"]:
            print("FOOD_BATCH_ROW "+json.dumps([r["id"],r["status"],r["traits"],r["review_flags"]],separators=(",",":")))

if __name__=="__main__":
    main()
