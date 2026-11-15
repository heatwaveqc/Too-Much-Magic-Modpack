#!/usr/bin/env python3
"""Check the *recipe-driven* ProjectE design contract, not gameplay completion.

The actual KubeJS JS and trophy tag live in a registered implementation
source branch. Do not require source files in a rolling integration checkout
until its assembler has imported them. A green validation isn't a live recipe.
"""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

def main() -> None:
    data = json.loads((ROOT / "manifest/progression-gates.json").read_text(encoding="utf-8"))
    if data.get("schema_version") != 1:
        raise ValueError("Unknown progression schema")
    if data.get("decision_status") != "owner_approved_recipe_ingredient_gates":
        raise ValueError("Old boss-kill entitlement model is obsolete")
    if data.get("runtime_enforcement") != "KUBEJS_RECIPE_SOURCE_STAGED_CLIENT_UNTESTED":
        raise ValueError("Do not claim in-world recipe enforcement before actual Prism acceptance")
    if not data.get("do_not_allow_quest_only_gating"):
        raise ValueError("Quest checkboxes must not be described as gameplay gates")
    if not data.get("trade_and_shared_boss_loot_allowed"):
        raise ValueError("The new design deliberately uses tradeable items, not defeat flags")
    stages = {x["id"]: x for x in data["stages"]}
    if len(stages) != len(data["stages"]) or set(stages) != {
       "basic_projecte", "advanced_emc_and_expansion"
    }:
        raise ValueError("Both staged recipe goals must exist")
    stone = stages["basic_projecte"]
    if stone["gate_logic"] != "ANY_ONE_OF_BOSS_ASSOCIATED_ITEMS":
        raise ValueError("Stone center ingredient accepts one of multiple bosses")
    if stone.get("personal_kill_tracking") is not False:
        raise ValueError("Old player defeat state must not be reintroduced")
    if stone["original_recipe_ids"] != [
       "projecte:philosophers_stone","projecte:philosophers_stone_alt"
    ]:
        raise ValueError("Both original recipes must be removed")
    if stone["center_tag"] != "tmm:philosophers_stone_boss_trophies":
        raise ValueError("Incorrect center tag")
    expected={"ars_nouveau:wilden_tribute","botania:life_essence","neovitae:mine_key"}
    items={x["item"] for x in stone["accepted_center_items"]}
    if items != expected or len(stone["accepted_center_items"]) != 3:
        raise ValueError("Wrong boss trophies: actual Gaia spirit is botania:life_essence")
    foreman = next(x for x in stone["accepted_center_items"] if x["item"]=="neovitae:mine_key")
    if foreman["exclusive_boss_drop"]:
        raise ValueError("Mine Key also drops from Mines trial spawners; document this")
    flower = stages["advanced_emc_and_expansion"]
    if flower["gate_logic"] != "CHAOS_SHARD_POWER_FLOWER_RECIPE":
        raise ValueError("Wrong MK3 gate type")
    if flower["ingredient"]!="draconicevolution:chaos_shard":
        raise ValueError("Wrong chaos shard ID")
    if flower["specific_first_threshold"]!="projectexpansion:red_power_flower":
        raise ValueError("MK3 is Red, not Magenta or Dark")
    if not flower.get("require_shard_in_direct_recipes_mk3_plus") or not flower.get("require_shard_in_mk3_upgrade"):
        raise ValueError("Both direct and upgrade paths require Chaos Shard")
    if flower["tiers_direct"][0]!="red" or flower["tiers_direct"][-1]!="final":
        raise ValueError("All red-through-final direct recipe bypasses must be covered")
    if stone.get("gameplay_tested") or flower.get("gameplay_tested"):
        raise ValueError("Machine-readable design cannot claim in-world acceptance without observed evidence")
    if not data.get("universal_emc_goal",{}).get("require_positive_emc_except_cheap_exceptions"):
        raise ValueError("Universal EMC coverage goal missing")
    registry=json.loads((ROOT/"manifest/integration-sources.json").read_text(encoding="utf-8"))
    stream=next((x for x in registry["streams"] if x["name"]=="projecte-boss-recipes-and-emc-audit"),None)
    if not stream:
        raise ValueError("Missing full integration projectE source stream")
    names=set(stream["paths"])
    for path in (
      "kubejs/server_scripts/projecte_boss_stone_and_powerflowers.js",
      "kubejs/data/tmm/tags/item/philosophers_stone_boss_trophies.json",
      "manifest/emc-coverage-policy.json",
      "scripts/audit_emc_coverage.py",
    ):
        if path not in names:
            raise ValueError("Missing integrated source path: "+path)
    features=json.loads((ROOT/"manifest/feature-checklist.json").read_text(encoding="utf-8"))
    states={f["id"]:f["state"] for f in features["features"]}
    if states.get("end.projecte_start")=="player_pass" or states.get("end.projecte_high")=="player_pass":
        raise ValueError("ProjectE has not passed real-world crafting acceptance")
    print("PASS: item-center Stone, both recipe IDs, MK3+ Chaos flower paths and all-registered-item EMC audit are specified")
    print("NOT CLIENT VERIFIED: actual recipe reload, JEI, Matter Upgrader and EMC/transmutation bypasses remain QA")

if __name__=="__main__":
    try:
        main()
    except (KeyError, ValueError, OSError, json.JSONDecodeError) as exc:
        raise SystemExit("PROJECTE PROGRESSION CONTRACT INVALID: "+str(exc))
