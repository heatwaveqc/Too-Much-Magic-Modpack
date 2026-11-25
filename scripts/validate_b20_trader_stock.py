#!/usr/bin/env python3
"""Static validation of B20 guaranteed wandering-trader stock source; not a Minecraft test."""
from __future__ import annotations
import hashlib
import json
import tomllib
from pathlib import Path

ROOT=Path(__file__).resolve().parents[1]
def read(path): return (ROOT / path).read_text(encoding="utf-8")
def main():
    p=json.loads(read("manifest/wandering_trader_magic_stock.json"))
    assert p["minecraft"]=="1.21.1" and len(p["guaranteed_stock"])==6
    assert all(x["min"]>=1 for x in p["guaranteed_stock"])
    assert set(x["slot"] for x in p["guaranteed_stock"]) == {
      "apotheosis_equipment", "ars_glyph_tier_1", "ars_glyph_tier_2", "ars_glyph_tier_3", "irons_ink_uncommon_plus", "random_relic_or_artifact"}
    ink = next(x for x in p["guaranteed_stock"] if x["slot"] == "irons_ink_uncommon_plus")
    assert sum(ink["weights"].values()) == 100
    assert p["preserve_existing_trades"] and not p["no_stock_from_other_magic_mods_yet"]
    assert p["premium_selection_count"] == 4
    premium = p["premium_stock_choices"]
    assert len(premium) == 12 and len({x["id"] for x in premium}) == 12
    assert next(x for x in premium if x["id"] == "occultism_iesnium_ingot")["item"] == "occultism:iesnium_ingot"
    assert all(1 <= x["price_emeralds"] <= 128 for x in premium)
    assert all(x["uses"] in (1,2) for x in premium)
    assert {x["id"] for x in premium}.issuperset({"witchery_drop_of_luck","hexerei_replacer_satchel"})
    assert p["collectible_versioned_marker"] == "tmmRelicArtifactStockVersion=1"
    denied = p["exclusions"]["forbidden_trader_item_ids"]
    assert "relics:ring_of_the_seven_deadly_sins" in denied
    assert "relics:ring_of_the_seven_deadly_sins" in p["exclusions"]["disallowed"]
    assert all(x["status"] == "suggest_not_active" or x["status"] == "already_provisional_in_rotation" for x in p["under_review"])
    assert all(x["approval"] == "suggested_not_approved" for x in p["next_candidates"])
    script_path="kubejs/server_scripts/tmm_wandering_trader_magic_stock.js"
    script=read(script_path)
    for token in ["EntityEvents.spawned('minecraft:wandering_trader'",
                  "getOffers()", "getPersistentData()", "tmmGuaranteedMagicStockVersion",
                  "TMM_LootController.createRandomLootItem", "TMM_Affixes.hasAffixes",
                  "getGlyphItemMap()", "getConfigTier().value", "tmmGlyphForTier(trader, 1)",
                  "tmmGlyphForTier(trader, 2)", "tmmGlyphForTier(trader, 3)",
                  "INK_UNCOMMON", "INK_RARE", "INK_EPIC", "INK_LEGENDARY",
                  "offers.add(required[i])", "tmmPremiumRotation(trader, 4)",
                  "tmmRotatingMagicStockVersion", "tmmRelicArtifactStockVersion",
                  "tmmRandomRelicOrArtifact(trader)", "tmmRandomRelicOrArtifact",
                  "TMM_ArtifactItem", "TMM_RelicItem", "isCosmetic()",
                  "TMM_ForbiddenTraderItemIds",
                  "relics:ring_of_the_seven_deadly_sins",
                  "if (tmmIsForbiddenTraderItem(item)) continue;",
                  "tmmIsForbiddenTraderItem(stack.getItem())",
                  "tmmPurgeForbiddenTraderOffers(trader)",
                  "witchery:drop_of_luck", "hexerei:replacer_satchel",
                  "setMana(tablet, ManaTablet.MAX_MANA)",
                  "getParts().toArray()", "getModifiers().toArray()",
                  "occultism:iesnium_ingot", "astralsorcery:constellation_paper",
                  "rootsclassic:runic_focus", "forbidden_arcanus:growing_edelwood",
                  "offers.add(chosen[i].offer)", "data.putInt("]:
        assert token in script, "Missing implementation contract: " + token
    assert "INK_COMMON.get()" not in script
    assert script.index("tmmPurgeForbiddenTraderOffers(trader);") < script.index("const data = trader.getPersistentData();", script.index("EntityEvents.spawned('minecraft:wandering_trader'"))
    # Regression guard: never let the configured disallowed ID disappear from
    # both the runtime hard-denylist and policy silently.
    assert script.count("relics:ring_of_the_seven_deadly_sins") >= 1
    index=read("index.toml")
    assert hashlib.sha256(index.encode()).hexdigest() == tomllib.loads(read("pack.toml"))["index"]["hash"]
    hashval=hashlib.sha256(script.encode()).hexdigest()
    assert f'file = "{script_path}"\nhash = "{hashval}"' in index
    print("PASS: B20 six guaranteed stock slots, twelve premium items, weighted >= uncommon ink, true affixed gear, configured glyph tiers and packwiz hashes")
    print("PENDING: NeoForge/KubeJS runtime API, actual server+client trader offers, Apotheosis rarity/affix generation and item GUI")

if __name__=="__main__":
    main()
