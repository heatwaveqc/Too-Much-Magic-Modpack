#!/usr/bin/env python3
"""Exact current source contract for premium traders and the two rabbit Gateways.

SOURCE-ONLY verification. Never confuse file integrity / syntax with a successful
Minecraft 1.21.1 NeoForge + KubeJS + Gateways + Apotheosis launch.
"""
from __future__ import annotations
import hashlib
import json
from pathlib import Path
import tomllib

ROOT=Path(__file__).resolve().parents[1]
WATCHED=(
    "kubejs/data/tmm/gateways/caerbannog.json",
    "kubejs/data/tmm/gateways/chaos_bunny.json",
    "kubejs/data/tmm/recipe/caerbannog.json",
    "kubejs/data/tmm/loot_table/entities/empty.json",
    "kubejs/server_scripts/caerbannog_boss.js",
    "kubejs/startup_scripts/caerbannog_trophy.js",
    "kubejs/server_scripts/tmm_wandering_trader_magic_stock.js",
    "manifest/wandering_trader_magic_stock.json",
)

def text(path:str)->str:
    return (ROOT/path).read_text(encoding="utf-8")

def data(path:str):
    return json.loads(text(path))

def index_contract()->None:
    index=tomllib.loads(text("index.toml"))
    pack=tomllib.loads(text("pack.toml"))
    blob=text("index.toml").encode()
    assert hashlib.sha256(blob).hexdigest()==pack["index"]["hash"], "Stale Packwiz root index"
    files={row["file"]:row for row in index["files"]}
    for path in WATCHED:
        if path.startswith("manifest/"):
            continue  # Source-only policy manifest; not an in-game file
        assert path in files, "Missing integrated runtime source: "+path
        expected=hashlib.sha256((ROOT/path).read_bytes()).hexdigest()
        assert files[path]["hash"]==expected, "Packwiz hash mismatch: "+path

def trader_contract()->dict:
    m=data("manifest/wandering_trader_magic_stock.json")
    roles={x["slot"] for x in m["guaranteed_stock"]}
    assert roles=={"apotheosis_equipment","ars_glyph_tier_1","ars_glyph_tier_2",
                   "ars_glyph_tier_3","irons_ink_uncommon_plus",
                   "random_relic_or_artifact"}, roles
    assert len(m["guaranteed_stock"])==6
    assert len(m["premium_stock_choices"])==12
    assert m["premium_selection_count"]==4
    assert len({x["id"] for x in m["premium_stock_choices"]})==12
    assert all(1 <= row["price_emeralds"]<=128 for row in m["premium_stock_choices"])
    assert m["preserve_existing_trades"]
    ink=next(x for x in m["guaranteed_stock"] if x["slot"]=="irons_ink_uncommon_plus")
    assert ink["weights"]=={"uncommon":55,"rare":30,"epic":12,"legendary":3}
    forbidden="relics:ring_of_the_seven_deadly_sins"
    assert forbidden in m["exclusions"]["forbidden_trader_item_ids"]
    script=text("kubejs/server_scripts/tmm_wandering_trader_magic_stock.js")
    required=(
        "tmmRelicArtifactStockVersion","tmmGuaranteedMagicStockVersion",
        "tmmRotatingMagicStockVersion","tmmPurgeForbiddenTraderOffers",
        "tmmRandomRelicOrArtifact","tmmPremiumRotation(trader, 4)",
        "TMM_LootController.createRandomLootItem","TMM_Affixes.hasAffixes",
        "getConfigTier().value","witchery:drop_of_luck","hexerei:replacer_satchel",
        forbidden,
    )
    assert all(token in script for token in required), "Runtime premium contract incomplete"
    return {"guaranteed_offers":6,"distinct_rotating_premium":4,
            "premium_pool":12,"forbidden_relic_offer_filtered":True,
            "actual_trader_gui_and_purchases":"NOT RUN"}

def gateway_contract()->dict:
    rabbit=data("kubejs/data/tmm/gateways/caerbannog.json")
    chaos=data("kubejs/data/tmm/gateways/chaos_bunny.json")
    assert len(rabbit["waves"])==1
    first=rabbit["waves"][0]["entities"]
    assert len(first)==1 and first[0]["entity"]=="minecraft:rabbit"
    assert first[0]["nbt"]["RabbitType"]==99 and first[0]["count"]==1
    rewards=rabbit["rewards"]
    assert sum(x["stack"]["count"] for x in rewards if x["type"]=="gateways:stack"
               and x["stack"]["id"]=="minecraft:rabbit_foot")==1
    assert sum(x["rolls"] for x in rewards if x["type"]=="gateways:entity_loot"
               and x["entity"]=="minecraft:rabbit")==12

    waves=chaos["waves"]
    assert len(waves)==6, "Five horde waves plus separate boss"
    counts=[sum(entity["count"] for entity in w["entities"]) for w in waves]
    assert counts==[5,10,20,40,80,1], counts
    assert sum(counts)==156
    assert all(len(w["entities"])==1 and w["entities"][0]["entity"]=="minecraft:rabbit"
               and w["entities"][0]["nbt"]["RabbitType"]==99 for w in waves)
    for w in waves:
        assert w["rewards"]==[], "No per-wave reward duplication"
        assert any(mod["type"]=="gateways:loot_table"
                   and mod["loot_table"]=="tmm:entities/empty"
                   for mod in w["modifiers"]), "Chaos wave native mob loot must be empty"
        assert "tmm_caerbannog_chaos" in w["entities"][0]["nbt"]["Tags"]
    assert "tmm_caerbannog_boss" in waves[-1]["entities"][0]["nbt"]["Tags"]
    bossmods={m.get("attribute"):m.get("value") for m in waves[-1]["modifiers"]
              if m["type"]=="gateways:attribute"}
    assert bossmods["minecraft:generic.max_health"]==997
    assert bossmods["minecraft:generic.attack_damage"]==92
    assert bossmods["minecraft:generic.armor"]==30
    assert bossmods["minecraft:generic.armor_toughness"]==20
    assert len(chaos["rewards"])==1
    assert chaos["rewards"][0]=={"type":"gateways:stack","stack":{
        "id":"kubejs:chaos_bunny_trophy","count":1}}
    assert chaos["failures"]==[]
    assert not (ROOT/"kubejs/data/tmm/recipe/chaos_bunny.json").exists(), (
        "Unfinished endgame Chaos Bunny may not have an unapproved survival recipe")
    recipe=data("kubejs/data/tmm/recipe/caerbannog.json")
    assert recipe["gateway"]=="tmm:caerbannog"
    assert "minecraft:rabbit_foot" not in json.dumps(recipe), (
        "Caerbannog entry recipe must NOT require its own rabbit foot drop")
    js=text("kubejs/server_scripts/caerbannog_boss.js")
    for token in ("TMM_BOSS_HIT_CHANCE = 0.10","EntityEvents.beforeHurt",
                  "EntityEvents.afterHurt","EntityEvents.drops",
                  "TMM_DamageTypes.GENERIC_KILL","tmm_caerbannog_boss"):
        assert token in js, "Boss code missing experimental guard: "+token
    startup=text("kubejs/startup_scripts/caerbannog_trophy.js")
    assert "chaos_bunny_trophy" in startup
    return {"ordinary_killer_bunny_waves":1,"guaranteed_foot":1,
            "extra_rabbit_loot_rolls":12,
            "chaos_horde_sizes":counts[:-1],"chaos_bosses":counts[-1],
            "chaos_total_rabbits":sum(counts),
            "per_wave_rewards":0,"completion_trophies":1,
            "survival_chaos_recipe":False,
            "god_tier_affixes":"NOT IMPLEMENTED",
            "tongue_visual_and_full_control_immunity":"NOT IMPLEMENTED",
            "real_waves_and_client_gameplay":"NOT RUN"}

def main()->None:
    index_contract()
    out={"status":"SOURCE_CONTRACT_PASS_NOT_GAMEPLAY",
         "premium_wandering_trader":trader_contract(),
         "caerbannog":gateway_contract()}
    print(json.dumps(out,indent=2))
    print("PENDING: live Gateways spawn waves/loot, full Chaos boss mechanics and actual trader GUI/purchases.")

if __name__=="__main__":
    main()
