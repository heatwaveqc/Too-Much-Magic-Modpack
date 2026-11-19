"""Complete a first-pass item-scoped food review using a frozen census.
No game writes, no recipe inheritance, no component edits or consumer permissions.
"""
import argparse
import collections
import json
import re
from pathlib import Path

MEAL_TRAITS={"prepared_meal","prepared_plant","ordinary_grain_meal","soup","bread","salad","pastry","dessert","sandwich","placed_food","grain_free_seafood_candidate"}
DRINK_TRAITS={"beverage","native_drink","tea","coffee","juice","alcohol_candidate","fermented"}
SPECIAL_TRAITS={"magical_nourishment","magical_nourishment_candidate","magic_meat","reusable_food","resource_cost_candidate","blood","blood_candidate","medicinal","component_sensitive","native_special","charred","gilded_food","hazardous","special_effect_candidate","special_consumable","special_consumable_candidate","mutagen","potion"}
CULINARY_NS={"pamhc2crops","pamhc2trees","pamhc2foodcore","pamhc2foodextended","farmersdelight","aquaculture","brewinandchewin","cocktailsdelight","beachparty"}
PRODUCERS={"pamhc2foodcore","pamhc2foodextended","brewinandchewin","cocktailsdelight","beachparty"}
TOOLS=re.compile(r"(?:^|_)(?:knife|sword|pickaxe|axe|shovel|hoe|helmet|chestplate|leggings|boots|rod|keg|tankard|mug|glass|cup|bottle|plate|bowl|basket|seed|seeds|sapling|sign|door|trapdoor|planks|slab|stairs|fence|chair|table|fridge|block)(?:_|$)")
MEAL_WORDS=re.compile(r"(?:salad|sandwich|burger|pizza|stew|soup|pie|cake|cookie|muffin|pancake|waffle|taco|burrito|dumpling|noodle|casserole|omelet|omelette|donut|doughnut|pudding|sushi|sundae|icecream|ice_cream|chowder|lasagna| risotto|risotto|meatball|kebab|skewer|wrapitem|toastitem)")
DRINK_WORDS=re.compile(r"(?:juice|smoothie|milkshake|lemonade|coffee|tea(?:item|$)|cocktail|soda|cider|wine|beer|mead|vodka|rum(?:item|$)|whiskey|whisky|brandy|liqueur|martini|margarita|mojito|colada|daiquiri|grog|stout|ale(?:item|$))")
ALCOHOL_WORDS=re.compile(r"(?:wine|beer|mead|vodka|rum(?:item|$)|whiskey|whisky|brandy|liqueur|martini|margarita|mojito|daiquiri|grog|stout|ale(?:item|$))")
def classify(row, earlier, rules):
    item=row["id"]
    ns,path=item.split(":",1)
    tags=set(row.get("native_tags",[]))
    evidence=collections.defaultdict(set)
    for t in row.get("traits",[]): evidence[t].add("seed")
    flags=set()
    if earlier:
        for t in earlier["traits"]: evidence[t].add("batch_1_3")
        flags.update(earlier["review_flags"])
    for tag in tags:
        for t in rules["tag_traits"].get(tag,[]): evidence[t].add("native_tag:"+tag)
    for t in rules["exact_hints"].get(item,[]): evidence[t].add("exact_id_hint")
    # Native generic food evidence is not necessarily a FOOD component.
    is_food_tag=any(t=="c:foods" or t.startswith("c:foods/") or t=="cookingforblockheads:foods" for t in tags)
    is_drink_tag=any(t=="c:drinks" or t.startswith("c:drinks/") for t in tags)
    types=set(row.get("recipe_output_types",[]))
    culinary_output=bool(row.get("culinary_recipe_type_evidence")) or any(t.startswith(("brewinandchewin:","cocktailsdelight:","beachparty:")) for t in types)
    has_known=bool(evidence)
    tool_tag=any(t=="c:tools" or t.startswith("c:tools/") or t=="cookingforblockheads:utensils" or t.startswith("minecraft:enchantable/") for t in tags)
    seed_tag=any(t=="c:seeds" or t.startswith("c:seeds/") or t=="c:crops/seeds" for t in tags)
    # Exact/tag evidence outranks lexical hints. A producer namespace alone is never food proof.
    # Ignore tooling name matches on positive native drink/food tags (e.g. honey bottle).
    path_tool=bool(TOOLS.search(path)) and not (is_food_tag or is_drink_tag or has_known)
    candidate=row["review_status"]!="no_food_evidence_not_classified" or has_known or is_food_tag or is_drink_tag or culinary_output
    lexical_scope=(ns in CULINARY_NS and not tool_tag and not path_tool and not seed_tag and "utensil" not in evidence)
    if not has_known and lexical_scope:
        if DRINK_WORDS.search(path):
            evidence["beverage_candidate"].add("culinary_namespace_name_hint")
            if ALCOHOL_WORDS.search(path): evidence["alcohol_candidate"].add("culinary_namespace_name_hint")
            candidate=True
        elif MEAL_WORDS.search(path):
            evidence["prepared_meal_candidate"].add("culinary_namespace_name_hint")
            candidate=True
    # Generic food tag no broad invented meal category; classify ordinary uncategorized food generically.
    if is_food_tag and not evidence: evidence["generic_food"].add("native_food_tag")
    if is_drink_tag and not evidence: evidence["beverage"].add("native_drink_tag")
    if culinary_output and not evidence:
        evidence["culinary_output_candidate"].add("recipe_type_output")
    # Pam food modules contain many untagged crafted dishes, but equipment is also crafted.
    if not evidence and lexical_scope and ns in PRODUCERS and "minecraft:crafting" in types:
        evidence["culinary_output_candidate"].add("producer_crafting_output")
        candidate=True
    if not evidence and candidate:
        if tool_tag or path_tool: evidence["nonfood_equipment_candidate"].add("equipment_evidence")
        elif seed_tag: evidence["planting_material_candidate"].add("seed_tag")
        else: evidence["unclassified_food_related_candidate"].add("prior_census_evidence")
    traits=set(evidence)
    if not traits:
        return {"id":item,"status":"no_food_evidence_not_classified","traits":[],"review_flags":[],"batches":[],"evidence":{},"consumption":"unknown","consumer_policy":"unresolved"}
    if tool_tag and (traits & (MEAL_TRAITS|DRINK_TRAITS|SPECIAL_TRAITS) or is_food_tag):
        flags.add("equipment_food_evidence_conflict")
    if traits & {"alcohol_candidate"}:
        flags.add("alcohol_native_effect_review")
    if traits & SPECIAL_TRAITS:
        flags.add("special_consumer_review")
    if traits & {"prepared_meal_candidate","beverage_candidate","culinary_output_candidate","unclassified_food_related_candidate"}:
        flags.add("generic_identity_review_if_consumer_needs")
    if traits & {"nonfood_equipment_candidate","nonfood_throwable","utensil","planting_material_candidate"}:
        flags.add("not_ordinary_edible_evidence")
    if "grain_free_seafood_candidate" in traits:
        flags.add("grain_free_recipe_review")
    batches=[]
    if earlier: batches+=earlier["batches"]
    if traits & (MEAL_TRAITS|{"prepared_meal_candidate"}): batches.append("prepared_meals")
    if traits & (DRINK_TRAITS|{"beverage_candidate"}): batches.append("beverages")
    if traits & SPECIAL_TRAITS: batches.append("supernatural_special")
    if flags and (flags-{"not_ordinary_edible_evidence","generic_identity_review_if_consumer_needs"}): status="targeted_review"
    elif traits & {"nonfood_equipment_candidate","nonfood_throwable","utensil","planting_material_candidate"}:
        status="nonfood_or_planting_review"
    elif flags: status="generic_candidate"
    else: status="provisional"
    return {"id":item,"status":status,"traits":sorted(traits),"review_flags":sorted(flags),"batches":sorted(set(batches)),"evidence":{k:sorted(v) for k,v in sorted(evidence.items())},"consumption":"unknown","consumer_policy":"unresolved"}

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--review",type=Path,required=True)
    p.add_argument("--earlier",type=Path,required=True)
    p.add_argument("--rules",type=Path,default=Path("docs/food/batch-4-6-rules-v1.json"))
    p.add_argument("--output",type=Path,required=True)
    a=p.parse_args()
    rules=json.loads(a.rules.read_text())
    with a.review.open(encoding="utf-8") as f: rows=list(map(json.loads,f))
    with a.earlier.open(encoding="utf-8") as f: old={r["id"]:r for r in map(json.loads,f) if r["batches"]}
    if len(rows)!=len({r["id"] for r in rows}): raise ValueError("duplicate input item")
    present={r["id"] for r in rows}
    if not set(old)<=present: raise ValueError("earlier overlay references absent ID")
    result=[classify(r,old.get(r["id"]),rules) for r in rows]
    active=[r for r in result if r["status"]!="no_food_evidence_not_classified"]
    # Original targeted review decisions must never disappear.
    for r in active:
        if r["id"] in old and not set(old[r["id"]]["review_flags"])<=set(r["review_flags"]):
            raise ValueError("lost first-pass review flag "+r["id"])
    summary={"registry_items":len(rows),"candidate_records":len(active),"status_counts":dict(collections.Counter(r["status"] for r in result)),
        "batch_counts":dict(collections.Counter(k for r in active for k in r["batches"])),
        "flag_counts":dict(collections.Counter(k for r in active for k in r["review_flags"])),
        "newly_surfaced":sum(r["status"]!="no_food_evidence_not_classified" and source["review_status"]=="no_food_evidence_not_classified" for r,source in zip(result,rows)),
        "prior_candidates_lost":sum(source["review_status"]!="no_food_evidence_not_classified" and r["status"]=="no_food_evidence_not_classified" for r,source in zip(result,rows)),
        "earlier_batch_entries_preserved":len(old),
        "exact_hints_missing":sorted(set(rules["exact_hints"])-present),
        "alcohol_candidates":sum("alcohol_candidate" in r["traits"] for r in active),
        "scope":"First-pass evidence only. Generic labels do not imply FOOD component, diet eligibility, or consumer authorization."}
    a.output.mkdir(parents=True,exist_ok=True)
    (a.output/"food-first-pass.jsonl").write_text("".join(json.dumps(r,sort_keys=True)+"\n" for r in result),encoding="utf-8")
    (a.output/"food-first-pass-summary.json").write_text(json.dumps(summary,indent=2,sort_keys=True)+"\n")
    print("FOOD_FIRST_SUMMARY "+json.dumps(summary,sort_keys=True))
    for r in active:
        print("FOOD_FIRST_ROW "+json.dumps([r["id"],r["status"],r["traits"],r["review_flags"],r["batches"]],separators=(",",":")))
if __name__=="__main__":
    main()
