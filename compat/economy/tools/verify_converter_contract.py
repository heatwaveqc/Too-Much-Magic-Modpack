#!/usr/bin/env python3
"""Offline / Type-A assertions for the owner-locked EMC converters (#322).

Checks source recipe ingredient identity and native rates. This is NOT a claim
that a Minecraft recipe has loaded, that its item is registered, or that the
Project Expansion/ProjectE account bridge works in a game.
"""
from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[3]
ECONOMY = ROOT / "compat/economy"
RECIPES = ECONOMY / "src/main/resources/data/tmm_economy/recipe"
CONVERTER_JAVA = ECONOMY / "src/main/java/io/github/heatwaveqc/tmm/economy/converter"

CONTRACTS = {
    "emc_catalyst": {
        "ingredients": {"botania:conjuration_catalyst", "projectexpansion:basic_emc_link"},
        "mods": {"botania", "projectexpansion"},
        "source": "ManaConverters.java",
        "rate": r"AccountRefill\.tick\(server,\s*owner,.*?\b20,\s*1\s*\)",
    },
    "emc_source_jar": {
        "ingredients": {"ars_nouveau:source_jar", "projectexpansion:basic_emc_link"},
        "mods": {"ars_nouveau", "projectexpansion"},
        "source": "SourceConverters.java",
        "rate": r"AccountRefill\.tick\(server,\s*owner,.*?\b4,\s*3\s*\)",
    },
}


def validate_recipe(recipe: object, *, name: str) -> None:
    """Raise ValueError on a recipe that violates owner-locked ingredients/gates."""
    contract = CONTRACTS[name]
    if not isinstance(recipe, dict):
        raise ValueError(f"{name}: not a JSON object")
    if recipe.get("type") != "minecraft:crafting_shapeless":
        raise ValueError(f"{name}: expected ordinary shapeless crafting, not a custom gate")
    ingredients = recipe.get("ingredients")
    if not isinstance(ingredients, list) or len(ingredients) != 2:
        raise ValueError(f"{name}: expected precisely two consumed ingredients")
    if any(not isinstance(i, dict) or set(i) != {"item"} or
           not isinstance(i["item"], str) for i in ingredients):
        raise ValueError(f"{name}: wildcard/tag/alternative/complex ingredients forbidden")
    names = [i["item"] for i in ingredients]
    if len(set(names)) != 2 or set(names) != contract["ingredients"]:
        raise ValueError(f"{name}: wrong ingredients: {names}")
    if recipe.get("result") != {"id": f"tmm_economy:{name}", "count": 1}:
        raise ValueError(f"{name}: incorrect output or count")
    conditions = recipe.get("neoforge:conditions")
    if not isinstance(conditions, list) or len(conditions) != 2:
        raise ValueError(f"{name}: must gate exactly the two required optional mods")
    if any(not isinstance(c, dict) or set(c) != {"type", "modid"}
           or c["type"] != "neoforge:mod_loaded"
           or not isinstance(c["modid"], str) for c in conditions):
        raise ValueError(f"{name}: unexpected recipe conditions")
    if set(c["modid"] for c in conditions) != contract["mods"]:
        raise ValueError(f"{name}: incorrect optional-mod prerequisites")
    if set(recipe) != {"neoforge:conditions", "type", "ingredients", "result"}:
        raise ValueError(f"{name}: additional recipe gates/side effects not approved")


def verify() -> None:
    actual_files = {p.name for p in RECIPES.glob("*.json")}
    expected_files = {f"{name}.json" for name in CONTRACTS}
    if actual_files != expected_files:
        raise ValueError(f"Unexpected converter recipe set: {actual_files ^ expected_files}")
    for name, contract in CONTRACTS.items():
        recipe = json.loads((RECIPES / f"{name}.json").read_text(encoding="utf-8"))
        validate_recipe(recipe, name=name)
        java = (CONVERTER_JAVA / contract["source"]).read_text(encoding="utf-8")
        if not re.search(contract["rate"], java, flags=re.DOTALL):
            raise ValueError(f"{name}: native account refill rate may have drifted")
        print(f"PASS {name}: exact 2-input recipe and native EMC rate", flush=True)

    # Native Project Expansion 1.0.6 uses online-Player-scoped knowledge access:
    # its Util.getKnowledgeProvider(UUID) calls Util.getPlayer(UUID) and
    # returns null for an offline owner. Do not manufacture a shadow EMC wallet.
    account = (CONVERTER_JAVA / "AccountRefill.java").read_text(encoding="utf-8")
    if not re.search(r"getPlayer\(owner\)", account) or        "if (player == null) return;" not in account or        "getCapability(PECapabilities.KNOWLEDGE_CAPABILITY)" not in account:
        raise ValueError("Owner-scoped, fail-closed native-account access changed; audit Project Expansion first")
    if not re.search(r"if \(account == null\) return;", account):
        raise ValueError("Missing native knowledge-provider fail-closed guard")
    print("PASS AccountRefill: owner-scoped knowledge provider; no synthetic offline wallet")
    print("TYPE A only: no Minecraft, JAR/bytecode, game/server boot or owner-authorized QA performed")


if __name__ == "__main__":
    try:
        verify()
    except (ValueError, OSError, json.JSONDecodeError) as exc:
        print(f"FAIL {exc}", file=sys.stderr)
        sys.exit(1)
