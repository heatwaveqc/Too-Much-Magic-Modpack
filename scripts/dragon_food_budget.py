#!/usr/bin/env python3
"""Conservative source-only dragon nutrition budget planner for TMM #444.

Uses the reviewed recipe-provenance inventory. Emits *candidate* values only for
owner-locked raw species families, verified native cutting ratios, and verified
single-output nonthermal combinations. Never writes a live Dragon Survival map.
"""
from __future__ import annotations

import argparse
from collections import Counter
from fractions import Fraction
import json
from pathlib import Path
import sys
from typing import Any

from dragon_food_provenance import FoodModel, THERMAL_RECIPES

SPECIES = {
    "cave_dragon": "terrestrial_animal",
    "sea_dragon": "marine_animal",
    "forest_dragon": "plant_fungus",
}
BASE_NUTRITION = 6
BASE_SATURATION_MODIFIER = 2.0


def plan(inventory: dict[str, Any], policy: dict[str, Any]) -> dict[str, Any]:
    if policy.get("schema") != 1 or policy.get("scope") != "approved_numeric_pilot":
        raise ValueError("Require explicit schema-1 numeric pilot; no live or complete-map output")
    for key in ("raw", "cutting", "combinations"):
        if not isinstance(policy.get(key), list):
            raise ValueError(f"Missing policy list: {key}")

    model = FoodModel(inventory)
    report = model.report()
    edible = {r["item"]: r for r in report["records"]}
    recipes = {r["id"]: r for r in inventory["recipes"]}
    values: dict[tuple[str, str], dict[str, Any]] = {}
    held: list[dict[str, str]] = []

    def hold(kind: str, item: str, reason: str) -> None:
        held.append({"kind": kind, "item": item, "reason": reason})

    def approved_item(species: str, item: str, family: str | None = None) -> bool:
        row = edible.get(item)
        if species not in SPECIES or row is None or row["status"] != "resolved":
            return False
        if len(row["variants"]) != 1:
            return False
        variant = row["variants"][0]
        required = family or SPECIES[species]
        if variant["components"] != {required: 1}:
            return False
        if required == "plant_fungus" and variant["marine_plant_units"]:
            # Kelp / seagrass are edible, but their numeric bonus is unapproved.
            return False
        return variant["preparation"] == "raw"

    def add(kind: str, species: str, item: str, nutrition: int, evidence: str) -> None:
        key = (species, item)
        if key in values:
            hold(kind, item, "duplicate candidate value; no silent overwrite")
            return
        if not isinstance(nutrition, int) or nutrition <= 0:
            hold(kind, item, "nonpositive or fractional per-portion nutrition")
            return
        values[key] = {
            "species": species,
            "item": item,
            "nutrition": nutrition,
            "saturation_modifier": BASE_SATURATION_MODIFIER,
            "theoretical_saturation": 2 * nutrition * BASE_SATURATION_MODIFIER,
            "rule": kind,
            "evidence": evidence,
        }

    for rule in policy["raw"]:
        item, species = rule.get("item"), rule.get("species")
        evidence = rule.get("evidence")
        # Only a direct source-backed RAW root, never a name- or recipe-based guess.
        root = model.roots.get(item)
        if not evidence or not root or root["preparation"] != "raw" or not approved_item(species, item):
            hold("raw", str(item), "unverified direct preferred raw source or missing evidence")
            continue
        add("raw", species, item, BASE_NUTRITION, evidence)

    def valid_recipe(recipe_id: str, output: str, ingredients: Counter[str]) -> bool:
        recipe = recipes.get(recipe_id)
        item_report = edible.get(output)
        # An otherwise valid recipe is not authority to override an unregistered
        # output or one whose other recipes leave family/preparation unresolved.
        if (not recipe or recipe.get("output") != output or not recipe.get("evidence")
                or item_report is None or item_report["status"] != "resolved"
                or len(item_report["variants"]) != 1):
            return False
        if (recipe.get("preparation") not in ("raw", "minimally_processed")
                or recipe["type"] in THERMAL_RECIPES):
            return False
        actual: Counter[str] = Counter()
        for entry in recipe["ingredients"]:
            if "item" not in entry:
                return False  # Tag/alternate ingredients need explicit provenance review.
            if entry["item"] not in model.tools:
                actual[entry["item"]] += entry.get("count", 1)
        return actual == ingredients

    for rule in policy["cutting"]:
        item, source, species = rule.get("item"), rule.get("source"), rule.get("species")
        evidence = rule.get("evidence")
        recipe = recipes.get(rule.get("recipe"))
        whole, piece, count = (rule.get(k) for k in (
            "native_whole_nutrition", "native_piece_nutrition", "output_count"))
        if (not evidence or not recipe or not all(isinstance(v, int) and not isinstance(v, bool) and v > 0
                                                for v in (whole, piece, count))
                or recipe.get("result_count") != count
                or not valid_recipe(rule["recipe"], item, Counter({source: 1}))
                or (species, source) not in values or not approved_item(species, source)):
            hold("cutting", str(item), "unverified single-source nonthermal cut, ratio or yield")
            continue
        # Cannot infer portion identity from item names or round a fractional ratio.
        portion = Fraction(values[(species, source)]["nutrition"] * piece, whole)
        if portion.denominator != 1:
            hold("cutting", str(item), "fractional portion requires explicit owner rounding policy")
            continue
        add("cutting", species, item, portion.numerator, evidence)

    for rule in policy["combinations"]:
        item, species = rule.get("item"), rule.get("species")
        evidence = rule.get("evidence")
        recipe = recipes.get(rule.get("recipe"))
        consumed = rule.get("ingredients")
        if (not evidence or not recipe or not isinstance(consumed, list)
                or not consumed or recipe.get("result_count", 1) != 1):
            hold("combination", str(item), "single-output nonthermal recipe provenance required")
            continue
        actual: Counter[str] = Counter()
        for entry in consumed:
            if not isinstance(entry, dict) or not isinstance(entry.get("item"), str) or type(entry.get("count")) is not int or entry["count"] < 1:
                actual.clear()
                break
            actual[entry["item"]] += entry["count"]
        if not actual or not valid_recipe(rule["recipe"], item, actual):
            hold("combination", str(item), "input counts/recipe do not match reviewed nonthermal output")
            continue
        if not all((species, source) in values for source in actual):
            hold("combination", str(item), "all consumed food sources must have preferred values")
            continue
        nutrition = sum(values[(species, source)]["nutrition"] * count for source, count in actual.items())
        add("combination", species, item, nutrition, evidence)

    return {
        "schema": 1,
        "scope": "numeric_candidate_not_runtime",
        "input_sha256": report["input_sha256"],
        "runtime_map_generated": False,
        "global_food_components_changed": False,
        "values": [values[key] for key in sorted(values)],
        "held": sorted(held, key=lambda r: (r["kind"], r["item"], r["reason"])),
        "unresolved_provenance": [r["item"] for r in report["records"] if r["status"] != "resolved"],
    }


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--inventory", type=Path, required=True)
    parser.add_argument("--policy", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    result = plan(json.loads(args.inventory.read_text(encoding="utf-8")),
                  json.loads(args.policy.read_text(encoding="utf-8")))
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(result, indent=2, sort_keys=True) + "\n", encoding="utf-8")
    print(f"Source-only dragon food candidate: {len(result['values'])} values, "
          f"{len(result['held'])} held; no live diet-map or human food changes")


if __name__ == "__main__":
    try:
        main()
    except (ValueError, KeyError, TypeError, OSError, json.JSONDecodeError) as exc:
        print(f"FAILED: {exc}", file=sys.stderr)
        sys.exit(1)
