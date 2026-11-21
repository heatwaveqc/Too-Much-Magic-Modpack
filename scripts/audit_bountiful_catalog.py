#!/usr/bin/env python3
"""Extract a bounded, historical item-census review for Bountiful, no game/pack operations."""
import csv
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ID = re.compile(r"^[a-z0-9_.-]+:[a-z0-9_./-]+$")
PATTERNS = {
    "mahoutsukai": r"powder|fae_essence",
    "rootsclassic": r"focus|herb|root|bark|petal|incense|dust",
    "scriptor": r"tome",
    "witchery": r"mutandis|drop_of_luck|luck|soul|blood",
    "witchery_forbidden_magic": r"soul|bottle",
    "malum": r"_spirit$|refined_soulstone",
    "goety": r"totem|soul.*(?:bottle|jar|energy)",
    "totemic": r"buffalo|cedar|eagle|feather|flute",
    "dragonsurvival": r"charred|dragon.*food",
    "vampiresdelight": r"blood|eternal",
}
def main():
    catalog = json.loads((ROOT/"manifest/bountiful-contract-candidates.json").read_text())
    families = catalog["general"]+catalog["schools"]+catalog["transformations"]
    selected = {row["item"] for family in families for row in family.get("balance_reward_draft", [])}
    selected.update(row["item"] for family in families for row in family.get("balance_objective_draft", []) if row["type"]=="item")
    selected.update(f["test_decree_recipe_reagent"] for f in families if "test_decree_recipe_reagent" in f)
    found = {}
    related = {}
    path = ROOT/"docs/qa/surveys-2026-11-18/items.csv"
    with path.open(encoding="utf-8-sig",newline="") as file:
        reader = csv.DictReader(file)
        print("CENSUS_COLUMNS",json.dumps(reader.fieldnames))
        id_field = None
        for row in reader:
            if id_field is None:
                candidates = [k for k,v in row.items() if isinstance(v,str) and ID.fullmatch(v)]
                id_field=next((k for k in candidates if k in ("id","registered_id","item_id","item","registry_id")),None)
                if id_field is None:
                    if len(candidates)!=1: raise ValueError("cannot identify item id column: "+str(candidates))
                    id_field=candidates[0]
            ident=row[id_field]
            if ident in selected or (":" in ident and ident.split(":",1)[0] in PATTERNS and re.search(PATTERNS[ident.split(":",1)[0]],ident.split(":",1)[1])):
                values={k:v for k,v in row.items() if k and isinstance(v,str) and ("emc" in k.lower() or k.lower() in ("name","display_name","description_id","item_name","translation_key"))}
                if ident in selected: found[ident]=values
                else: related[ident]=values
    print("CENSUS_MATCH_COUNTS",json.dumps({"selected":len(selected),"historically_registered":len(found),"not_observed":len(selected-found.keys()),"historically_zero_emc":sum(v.get("emc")=="0" for v in found.values())},sort_keys=True))
    print("CENSUS_MISSING",json.dumps(sorted(selected-found.keys())))
    for name in sorted(related): print("CENSUS_RELATED",json.dumps({"id":name,**related[name]},sort_keys=True))
    for name in sorted(found): print("CENSUS_SELECTED",json.dumps({"id":name,**found[name]},sort_keys=True))
    print("Historical capture only; not effective next-build EMC or proof of current runtime.")
if __name__=="__main__":
    main()
