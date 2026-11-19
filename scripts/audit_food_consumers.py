"""Cross-consumer food audit against the frozen installed registry and source contracts.
Read-only. Candidate data must NEVER grant diet, cooking, XP, blood or magical permissions.
"""
import argparse
import collections
import gzip
import json
from pathlib import Path

MAGIC_BASE=("enchanted_star_candy","magnificent_star_candy","enchanted_golden_carrot")
SPECIES=("dragonsurvival:cave_dragon","dragonsurvival:forest_dragon","dragonsurvival:sea_dragon")
CHARRED={f"dragonsurvival:charred_{x}" for x in ("meat","seafood","vegetable","mushroom")}
GOLDEN={"minecraft:golden_apple","minecraft:enchanted_golden_apple","minecraft:golden_carrot","pastel:enchanted_golden_carrot"}
SPECIAL_COST={"evilcraft:flesh_rejuvenated","botania:infinite_fruit"}
SPECIAL_BLOOD="witchery:bottle_of_blood"

def names(values):
    return {x["id"] if isinstance(x,dict) else x for x in values}

def assess(registry, records, diet, gilded, charred, equivalence):
    """Return observed issues; no rule modifies a game asset."""
    issue=[]
    def add(code,priority,system,evidence,action):
        issue.append({"code":code,"priority":priority,"consumer":system,"evidence":evidence,"next_action":action,"runtime_changed":False})
    ids=set(registry)
    index={r[0]:r for r in records}
    if len(index)!=len(records) or not set(index)<=ids:
        raise ValueError("duplicated overlay ID or absent from frozen registry")
    species={s:{v["items"]:v for v in diet["values"][s]["neoforge:value"]} for s in SPECIES}
    ns={i.split(":")[0] for i in ids}
    stale=[]
    missing_native=[]
    optional_foreign=[]
    for s,m in species.items():
        for key in m:
            if key.startswith("#") or key in ids:continue
            if key.startswith("spectrum:") and "pastel" in ns:stale.append({"species":s,"selector":key,"possible_new_id":"pastel:"+key.split(":",1)[1],"candidate_in_registry":"pastel:"+key.split(":",1)[1] in ids})
            elif key.split(":")[0] in ns:missing_native.append([s,key])
            else:optional_foreign.append([s,key])
    if stale:
        add("dragon_stale_spectrum_selectors","high","dragon_diets",
            {"count":len(stale),"selectors":stale},
            "Reconcile exact Pastel item IDs with Dragon Survival map and pinned version; do not invent missing Magnificent candy or delete native exceptions. Run species consumption QA.")
    if missing_native:
        add("dragon_missing_ids_installed_namespaces","medium","dragon_diets",
            {"count":len(missing_native),"examples":missing_native[:25]},
            "Review authored selector IDs for enabled providers; historical name/registry differences are not grounds for bulk deletion.")
    # Missing optional-mod selectors are kept as an aggregate, not automatically 'bugs'.
    if optional_foreign:
        add("dragon_foreign_optional_selectors","info","dragon_diets",
            {"count":len(optional_foreign),"examples":optional_foreign[:12]},
            "Retain optional legacy coverage unless schema or data-map loading errors demonstrate harm; skip absent providers when evaluating runtime permission.")
    if any(x not in species["dragonsurvival:cave_dragon"] for x in CHARRED) or "#c:foods/raw_meat" in species["dragonsurvival:cave_dragon"]:
        add("cave_charred_diet_regression","high","dragon_diets",
            {"charred_present":sorted(CHARRED & species["dragonsurvival:cave_dragon"].keys()),"raw_meat_selector_present":"#c:foods/raw_meat" in species["dragonsurvival:cave_dragon"]},
            "Preserve charred-only Cave policy; do not grant raw food based on taxonomy.")
    if "aquaculture:sushi" in species["dragonsurvival:sea_dragon"] and "aquaculture:fish_fillet_raw" not in species["dragonsurvival:sea_dragon"]:
        add("sea_sushi_fillet_mismatch","medium","dragon_diets",
            "Sea permits sushi but not native raw fillet","Verify allowed species diet; avoid rice-based sushi and recipe inheritance.")
    # Explicit food tags are item-ID scoped and must not contain any ordinary blood variants.
    g=names(gilded["values"]);c=names(charred["values"])
    if g!=GOLDEN:
        add("gilded_tag_drift","high","gilded",{"present":sorted(g),"expected":sorted(GOLDEN)},"Retain explicitly approved 4 canonical golden foods; separate Nether foods.")
    absent=sorted(g-ids)
    if absent:
        add("gilded_missing_registered_ids","high","gilded",absent,"Require exact installed ID, never guessed alias.")
    if c!=CHARRED:
        add("charred_tag_drift","high","cookingforblockheads",{"present":sorted(c),"expected":sorted(CHARRED)},"Use four food outputs, not Dragon Survival's broader tag with coal/soup.")
    if SPECIAL_BLOOD in index and "component_sensitive" in index[SPECIAL_BLOOD][2]:
        add("jawr_blood_component_variants","high","jawr_vampire",
            {"item_id":SPECIAL_BLOOD,"same_item_multiple_marker_variants":True},
            "Retain source-authoritative JAWR CHICKEN_BLOOD/VAMPIRE_BLOOD marker presence checks. Do not use generic food tags to authorize special blood or alter blood pool.")
    for item in SPECIAL_COST:
        if item in ids:
            bad={s:species[s].get(item) for s in SPECIES if item in species[s] and (species[s][item].get("properties",{}).get("nutrition")!=0 or species[s][item].get("properties",{}).get("saturation")!=0 or species[s][item].get("retain_effects") is not True)}
            if bad:
                add("magical_free_food_or_effect_loss","high","magical_nourishment",{"item":item,"bad_entries":bad},"Maintain zero bonus for on-use resource-cost feeds and retain_effects; test original blood/mana cost, cooldown and reuse.")
            else:
                add("magical_resource_use_unverified","medium","magical_nourishment",{"item":item,"species":list(SPECIES)},"Static map passes; live test one-time native mana/blood cost and no second nutrition/effect application.")
    if "artifacts:eternal_steak" in index and "reusable_food" in index["artifacts:eternal_steak"][2]:
        add("everlasting_beef_versus_eternal_steak","medium","dragon_werewolf",
            {"cave_beef":"artifacts:everlasting_beef" in species["dragonsurvival:cave_dragon"],"eternal_steak_in_any_species":any("artifacts:eternal_steak" in m for m in species.values())},
            "Keep Everlasting Beef Cave-only exception separate from cooked Eternal Steak; preserve native reuse.")
    for g0 in equivalence["groups"]:
        if g0["id"] in ("milk","raw_fillet","wheat_dough","raw_pasta","rice","tomato","onion","cabbage"):
            if g0["id"] in ("milk","raw_fillet"):
                add("equivalence_"+g0["id"]+"_hold","medium","cooking",
                    {"group":g0["id"],"partial_slots":g0["crafting_slots_partial"],"missing_common_tag":g0["declared_not_in_native_tag"]},
                    "Do not substitute different containers/quantities or whole fish and fillets. Test recipe remainder, serving amounts and yield.")
            elif g0["crafting_slots_partial"]:
                add("equivalence_"+g0["id"]+"_gap","medium","cooking",
                    {"group":g0["id"],"partial_slots":g0["crafting_slots_partial"],"missing_common_tag":g0["declared_not_in_native_tag"]},
                    "Tag support helps tag-aware recipes only. Review direct-ID recipes and processing quantities; do not globally rewrite outputs.")
    alcohol=[r[0] for r in records if "alcohol_candidate" in r[2]]
    uncertain=[r[0] for r in records if "alcohol_content_uncertain" in r[2]]
    if alcohol or uncertain:
        add("alcohol_identity_and_effects","medium","alcohol",
            {"candidates":len(alcohol),"uncertain":len(uncertain),"uncertain_ids":sorted(uncertain)},
            "Inspect native use/effects and component-aware beverages before granting alcohol permission; fermentation alone is not alcohol. Keep keg/cutting actions manual.")
    for item in ("tmm_progression:mutagenic_brew","tmm_progression:bio_mutagen"):
        if item in index:
            add("mutagen_special_use","medium","progression_hunter",{"id":item,"traits":index[item][2]},
                "Only Progression owns transformation/mutation rewards; preserve native item-use, component, cooldown and side effects.")
    for item in ("pastel:magnificent_star_candy",):
        if item not in ids:
            add("magical_food_missing","medium","magical_nourishment",{"missing_id":item,"related_present":sorted(i for i in ids if i.startswith("pastel:") and "star_candy" in i)},
                "Review actual provider item list for intended Magnificent effect; do not alias ordinary/enchanted candy automatically.")
    # Provider state precedence cannot be inferred from a tagged item.
    add("forms_precedence_live_qa","medium","jawr_werewolf_vampire_lich_dracolich",
        {"source_only":True,"diet_attribution":False},
        "Test human, JAWR vampire/werewolf, both liches, dragon forms and hybrids with separate food and effect checks. No broad food-tag permission.")
    summary={"registry_count":len(ids),"review_items":len(records),"issues":len(issue),
        "by_priority":dict(collections.Counter(x["priority"] for x in issue)),
        "dragon_stale_ids":len(stale),"dragon_installed_namespace_missing_selectors":len(missing_native),"dragon_optional_foreign_selectors":len(optional_foreign),
        "alcohol_candidates":len(alcohol),"alcohol_uncertain":len(uncertain),
        "source_only":True,"runtime_changes":0}
    return {"schema_version":1,"status":"read_only_first_pass", "summary":summary,"issues":issue}

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--capture",type=Path,required=True)
    p.add_argument("--overlay",type=Path,default=Path("docs/food/first-pass-v1/index.json"))
    p.add_argument("--diet",type=Path,default=Path("kubejs/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json"))
    p.add_argument("--gilded",type=Path,default=Path("kubejs/data/tmm/tags/item/food/gilded.json"))
    p.add_argument("--charred",type=Path,default=Path("kubejs/data/tmm/tags/item/food/charred.json"))
    p.add_argument("--equivalence",type=Path,default=Path("docs/food/ingredient-equivalence-audit-v1.json"))
    p.add_argument("--output",type=Path,required=True)
    a=p.parse_args()
    with gzip.open(a.capture,"rt",encoding="utf-8") as f: capture=json.load(f)
    index=json.loads(a.overlay.read_text())
    records=[]
    for shard in index["shards"]:
        pp=a.overlay.parents[3]/shard["path"]
        chunk=[json.loads(x) for x in pp.read_text().splitlines() if x]
        if len(chunk)!=shard["count"]:raise ValueError("shard count mismatch "+str(pp))
        records.extend(chunk)
    report=assess({r["id"] for r in capture["items"]},records,json.loads(a.diet.read_text()),json.loads(a.gilded.read_text()),json.loads(a.charred.read_text()),json.loads(a.equivalence.read_text()))
    report["provenance"]={"frozen_census_commit":index["provenance"]["census_commit"],"first_pass_source_commit":index["provenance"]["source_commit"]}
    a.output.parent.mkdir(parents=True,exist_ok=True)
    a.output.write_text(json.dumps(report,indent=2,sort_keys=True)+"\n")
    print("FOOD_CONSUMER_SUMMARY "+json.dumps(report["summary"],sort_keys=True))
    for issue in report["issues"]:print("FOOD_CONSUMER_ISSUE "+json.dumps(issue,sort_keys=True))
if __name__=="__main__":
    main()
