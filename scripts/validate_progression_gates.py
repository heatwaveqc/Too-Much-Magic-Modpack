#!/usr/bin/env python3
"""Validate the owner-approved native ProjectE ritual and flower gates, not gameplay."""
from pathlib import Path
import json

ROOT = Path(__file__).resolve().parents[1]
def read(path):
    return json.loads((ROOT / path).read_text(encoding="utf-8"))

def main():
    data = read("manifest/progression-gates.json")
    assert data["decision_status"] == "owner_approved_ritual_and_recipe_ingredient_gates"
    assert data["runtime_enforcement"] == "NATIVE_RITUAL_RECIPE_SOURCE_STAGED_CLIENT_UNTESTED"
    assert data["do_not_allow_quest_only_gating"] and data["trade_and_shared_boss_loot_allowed"]
    stages = {s["id"]: s for s in data["stages"]}
    stone, flower = stages["basic_projecte"], stages["advanced_emc_and_expansion"]
    assert stone["personal_kill_tracking"] is False and not stone["gameplay_tested"] and not flower["gameplay_tested"]
    for key in ("philosophers_stone", "philosophers_stone_alt"):
        recipe = read(f"kubejs/data/projecte/recipe/{key}.json")
        assert recipe["neoforge:conditions"] == [{"type": "neoforge:false"}]
        assert recipe["result"]["id"] == "projecte:philosophers_stone"
    mk1_gates = {
        "collector_mk1": {
            "pattern": ["GPG", "GDG", "GFG"],
            "keys": {
                "D": {"tag": "c:storage_blocks/diamond"},
                "F": {"item": "minecraft:furnace"},
                "G": {"item": "minecraft:glowstone"},
                "P": {"item": "projecte:philosophers_stone"},
            },
        },
        "relay_mk1": {
            "pattern": ["OPO", "ODO", "OOO"],
            "keys": {
                "D": {"tag": "c:storage_blocks/diamond"},
                "O": {"tag": "c:obsidians/normal"},
                "P": {"item": "projecte:philosophers_stone"},
            },
        },
    }
    for name, expected in mk1_gates.items():
        recipe = read(f"kubejs/data/projecte/recipe/{name}.json")
        assert recipe["type"] == "minecraft:crafting_shaped" and recipe["category"] == "misc"
        assert recipe["pattern"] == expected["pattern"] and recipe["key"] == expected["keys"]
        assert sum(row.count("P") for row in recipe["pattern"]) == 1
        assert recipe["pattern"][0][1] == "P"
        assert recipe["result"] == {"count": 1, "id": f"projecte:{name}"}
    ritual = read("kubejs/data/tmm/recipe/ritual/equivalent_exchange.json")
    assert ritual["type"] == "occultism:ritual" and ritual["ritual_type"] == "occultism:craft"
    assert ritual["activation_item"] == {"item": stone["activation_item"]} and ritual["duration"] == 600
    assert ritual["pentacle_id"] == stone["pentacle"] and ritual["result"] == {"count": 1, "id": stone["target_item"]}
    assert len(ritual["ingredients"]) == stone["offering_slots"] == 12
    tags = [i["tag"] for i in ritual["ingredients"]]
    assert len(set(tags)) == 12
    offerings = []
    for tag in tags:
        assert tag.startswith("tmm:ritual_of_equivalent_exchange/")
        value = read("kubejs/data/tmm/tags/item/" + tag.split(":",1)[1] + ".json")
        assert value["replace"] is True and value["values"]
        assert all(isinstance(i,str) and not i.startswith("#") for i in value["values"])
        offerings.extend(value["values"])
    assert len(set(offerings)) == len(offerings) == stone["distinct_alternatives"] == 40
    assert len(flower["tiers_direct"]) == 14 and flower["tiers_direct"][0] == "red" and flower["tiers_direct"][-1] == "final"
    for tier in flower["tiers_direct"] + ["red_upgrade"]:
        recipe = read(f"kubejs/data/projectexpansion/recipe/power_flower/{tier}.json")
        assert recipe["key"]["S"] == {"item": "draconicevolution:chaos_shard"}
        assert sum(row.count("S") for row in recipe["pattern"]) == 1
        assert recipe["result"]["id"] == "projectexpansion:" + ("red" if tier=="red_upgrade" else tier) + "_power_flower"
        if tier == "red_upgrade":
            assert recipe["key"]["P"] == {"item": "projectexpansion:dark_power_flower"}
    registry = read("manifest/integration-sources.json")
    names = [p for stream in registry["streams"] for p in stream["paths"]]
    obsolete = ["kubejs/server_scripts/projecte_boss_stone_and_powerflowers.js", "kubejs/data/tmm/tags/item/philosophers_stone_boss_trophies.json", "scripts/verify_projecte_boss_recipes.cjs"]
    assert all(p not in names and not (ROOT / p).exists() for p in obsolete)
    for p in ["manifest/emc-coverage-policy.json", "scripts/audit_emc_coverage.py"]:
        assert p in names and (ROOT / p).exists()
    features = {f["id"]:f["state"] for f in read("manifest/feature-checklist.json")["features"]}
    assert all(features.get(name) != "player_pass" for name in ("end.projecte_start", "end.projecte_high"))
    print("PASS: source-only native ritual, both grid suppressions, Stone-gated MK1 Collector/Relay, 12 closed tags/40 alternatives, 14 direct flowers + Red upgrade, no obsolete script.")
    print("PENDING: actual loaded recipes, acquisition, transmutation and Matter Upgrader bypasses.")

if __name__ == "__main__":
    main()
