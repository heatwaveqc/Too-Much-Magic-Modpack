#!/usr/bin/env python3
"""Deterministic, fail-closed food-family / recipe-provenance survey for TMM #444.

Consumes an explicitly sourced registry-and-recipe inventory. Does NOT infer
food identity from an item's name, silently resolve unknown tags or fabricate
Dragon Survival nutrition, effects or live pack data-map entries.
"""
from __future__ import annotations

import argparse
from collections import defaultdict
from dataclasses import dataclass
import hashlib
import itertools
import json
from pathlib import Path
import sys
from typing import Any

FAMILIES = frozenset({"plant_fungus", "terrestrial_animal", "marine_animal", "neutral"})
PREPARATIONS = frozenset({
    "raw", "minimally_processed", "cooked", "heavily_cooked", "charred",
})
THERMAL_RECIPES = frozenset({
    "minecraft:smelting", "minecraft:smoking", "minecraft:campfire_cooking",
    "minecraft:blasting",
})
MAX_VARIANTS = 64


@dataclass(frozen=True)
class Variant:
    # Sorted family/count pairs retain mixed-dish ratios for later owner tuning.
    components: tuple[tuple[str, int], ...]
    preparation: str
    sources: tuple[str, ...]
    # Exact marine-plant origin is separately tracked; the primary three
    # biological families are not redefined by an item's display name.
    marine_plant_units: int = 0

    def json(self) -> dict[str, Any]:
        return {
            "components": dict(self.components),
            "preparation": self.preparation,
            "marine_plant_units": self.marine_plant_units,
            "sources": list(self.sources),
        }


class FoodModel:
    def __init__(self, data: dict[str, Any]) -> None:
        self.data = data
        self.issues: set[tuple[str, str, str]] = set()
        self._validate()
        self.roots = {r["item"]: r for r in data["roots"]}
        self.recipes: dict[str, list[dict[str, Any]]] = defaultdict(list)
        for recipe in data["recipes"]:
            self.recipes[recipe["output"]].append(recipe)
        self.tools = set(data["reusable_tools"])
        self.tags = data["tags"]
        self.memo: dict[str, tuple[Variant, ...]] = {}

    def issue(self, item: str, reason: str, detail: str) -> None:
        self.issues.add((item, reason, detail))

    def _validate(self) -> None:
        d = self.data
        if d.get("schema") != 1 or d.get("scope") != "pilot":
            raise ValueError("Only schema 1 explicitly incomplete pilot source inventories are supported")
        for key in ("edibles", "roots", "recipes", "tags", "reusable_tools"):
            if key not in d:
                raise ValueError(f"Missing source-inventory field {key}")
        if not isinstance(d["tags"], dict):
            raise ValueError("tags must be a mapping of exact tag IDs to exact item IDs")
        def unique(rows: list[dict[str, Any]], key: str) -> None:
            ids = [x.get(key) for x in rows]
            if any(not isinstance(x, str) or ":" not in x for x in ids) or len(ids) != len(set(ids)):
                raise ValueError(f"Invalid or duplicate {key} in source inventory")
        unique(d["edibles"], "item")
        unique(d["roots"], "item")
        unique(d["recipes"], "id")
        if len(d["reusable_tools"]) != len(set(d["reusable_tools"])):
            raise ValueError("Duplicate reusable tool")
        for row in d["edibles"]:
            if not row.get("evidence"):
                raise ValueError(f"Edible item {row['item']} has no registry/source evidence")
        for root in d["roots"]:
            if root.get("family") not in FAMILIES or root.get("preparation") not in PREPARATIONS:
                raise ValueError(f"Root {root['item']} has invalid biological family/preparation")
            if root.get("habitat", "unspecified") not in ("marine", "terrestrial", "unspecified"):
                raise ValueError(f"Root {root['item']} has invalid habitat")
            if not root.get("evidence"):
                raise ValueError(f"Root {root['item']} has no source evidence")
            if root.get("habitat") == "marine" and root["family"] == "neutral":
                raise ValueError(f"Neutral root {root['item']} has invalid marine habitat")
        for recipe in d["recipes"]:
            if not recipe.get("evidence") or not recipe.get("output") or not recipe.get("type"):
                raise ValueError("Every recipe needs exact source, output and type")
            if not isinstance(recipe.get("ingredients"), list) or not recipe["ingredients"]:
                raise ValueError(f"Recipe {recipe['id']} missing source-backed ingredients")
            prep = recipe.get("preparation")
            if prep is not None and prep not in PREPARATIONS:
                raise ValueError(f"Recipe {recipe['id']} has invalid preparation")
            for ingredient in recipe["ingredients"]:
                self._validate_ingredient(ingredient)
        for tag, members in d["tags"].items():
            if ":" not in tag or not isinstance(members, list) or not members:
                raise ValueError(f"Tag {tag} needs a nonempty sourced expansion")
            if any(not isinstance(m, str) or ":" not in m for m in members):
                raise ValueError(f"Invalid tag expansion {tag}")
        for tool in d["reusable_tools"]:
            if not isinstance(tool, str) or ":" not in tool:
                raise ValueError("Reusable tools require exact item IDs")

    @staticmethod
    def _validate_ingredient(ingredient: Any) -> None:
        if not isinstance(ingredient, dict):
            raise ValueError("Ingredient must be source-backed object")
        slots = set(ingredient) & {"item", "tag", "any_of"}
        if len(slots) != 1 or not set(ingredient) <= {"item", "tag", "any_of", "count"}:
            raise ValueError(f"Unsupported or ambiguous ingredient {ingredient}")
        count = ingredient.get("count", 1)
        if not isinstance(count, int) or isinstance(count, bool) or not 1 <= count <= 64:
            raise ValueError("Ingredient count must be a positive bounded integer")
        if "any_of" in ingredient:
            if not isinstance(ingredient["any_of"], list) or not ingredient["any_of"]:
                raise ValueError("Empty recipe alternative")
            for choice in ingredient["any_of"]:
                FoodModel._validate_ingredient(choice)
        else:
            key = "item" if "item" in ingredient else "tag"
            if not isinstance(ingredient[key], str) or ":" not in ingredient[key]:
                raise ValueError("Ingredients require exact registry or tag IDs")

    def _resolve_ingredient(
        self, owner: str, ingredient: dict[str, Any], path: tuple[str, ...]
    ) -> tuple[Variant, ...]:
        count = ingredient.get("count", 1)
        choices: list[str] = []
        if "item" in ingredient:
            choices = [ingredient["item"]]
        elif "tag" in ingredient:
            tag = ingredient["tag"]
            if tag not in self.tags:
                self.issue(owner, "unresolved_tag", tag)
                return ()
            choices = sorted(set(self.tags[tag]))
        elif "any_of" in ingredient:
            variants = []
            for alternate in ingredient["any_of"]:
                for base in self._resolve_ingredient(owner, alternate, path):
                    variants.append(Variant(
                        tuple((family, units * count) for family, units in base.components),
                        base.preparation, base.sources, base.marine_plant_units * count,
                    ))
            return self._limit(owner, variants, "ingredient_alternatives")
        variants = []
        for choice in choices:
            if choice in self.tools:
                variants.append(Variant((), "reusable_tool", (f"reusable:{choice}",)))
                continue
            found = self.resolve_item(choice, path)
            if not found:
                # A partially resolved tag must not make the other valid
                # alternatives look like a complete proven recipe.
                self.issue(owner, "unresolved_recipe_ingredient", choice)
            for base in found:
                variants.append(Variant(
                    tuple((family, units * count) for family, units in base.components),
                    base.preparation, base.sources, base.marine_plant_units * count,
                ))
        return self._limit(owner, variants, "ingredient_alternatives")

    def _limit(self, owner: str, variants: list[Variant], detail: str) -> tuple[Variant, ...]:
        if len(variants) > MAX_VARIANTS:
            self.issue(owner, "variant_limit_exceeded", detail)
            return ()
        return tuple(sorted(set(variants), key=lambda v: (
            v.preparation, v.components, v.marine_plant_units, v.sources,
        )))

    def resolve_item(self, item: str, path: tuple[str, ...] = ()) -> tuple[Variant, ...]:
        if item in path:
            self.issue(item, "recipe_cycle", " -> ".join(path + (item,)))
            return ()
        if item in self.memo:
            return self.memo[item]
        if len(path) > 32:
            self.issue(item, "recipe_depth_exceeded", " -> ".join(path))
            return ()
        variants: list[Variant] = []
        root = self.roots.get(item)
        if root:
            family = root["family"]
            marine_plant = 1 if family == "plant_fungus" and root.get("habitat") == "marine" else 0
            variants.append(Variant(((family, 1),), root["preparation"],
                                    (f"root:{item}",), marine_plant))
        for recipe in sorted(self.recipes.get(item, []), key=lambda r: r["id"]):
            prep = recipe.get("preparation")
            if prep is None and recipe["type"] in THERMAL_RECIPES:
                prep = "cooked"
            if prep is None:
                self.issue(item, "unclassified_preparation", recipe["id"])
                continue
            option_lists = [
                self._resolve_ingredient(item, ing, path + (item,))
                for ing in recipe["ingredients"]
            ]
            if any(not options for options in option_lists):
                self.issue(item, "incomplete_recipe_inputs", recipe["id"])
                continue
            total_options = 1
            for options in option_lists:
                total_options *= len(options)
            if total_options > MAX_VARIANTS:
                self.issue(item, "variant_limit_exceeded", recipe["id"])
                continue
            for combination in itertools.product(*option_lists):
                families: dict[str, int] = defaultdict(int)
                provenance = {f"recipe:{recipe['id']}"}
                marine = 0
                for source in combination:
                    for family, units in source.components:
                        families[family] += units
                    marine += source.marine_plant_units
                    provenance.update(source.sources)
                if not families:
                    self.issue(item, "no_food_materials", recipe["id"])
                    continue
                variants.append(Variant(tuple(sorted(families.items())),
                                        prep, tuple(sorted(provenance)), marine))
        result = self._limit(item, variants, "item_variants")
        if not result:
            self.issue(item, "no_verified_classification", "source registry/recipe evidence required")
        # Cache only a top-level, unambiguous path traversal. A cycle detected
        # inside one recursion branch must not poison an alternate acyclic path.
        if not path and not any(i[0] == item and i[1] == "recipe_cycle" for i in self.issues):
            self.memo[item] = result
        return result

    def report(self) -> dict[str, Any]:
        records = []
        for edible in sorted(self.data["edibles"], key=lambda r: r["item"]):
            item = edible["item"]
            variants = self.resolve_item(item)
            issues = sorted((reason, detail) for target, reason, detail in self.issues if target == item)
            compositions = {tuple(f for f, n in v.components if n > 0 and f != "neutral")
                            for v in variants}
            records.append({
                "item": item,
                "evidence": edible["evidence"],
                "dragon_only_edible": bool(edible.get("dragon_only_edible", False)),
                "status": "unresolved" if not variants else ("partial" if issues else "resolved"),
                "variants": [v.json() for v in variants],
                "multiple_possible_provenances": len(variants) > 1,
                "multiple_family_outcomes": len(compositions) > 1,
                "issues": [{"reason": reason, "detail": detail} for reason, detail in issues],
            })
        canonical = json.dumps(self.data, sort_keys=True, separators=(",", ":"), ensure_ascii=False)
        return {
            "schema": 1,
            "scope": "source_pilot_not_full_registry_census",
            "input_sha256": hashlib.sha256(canonical.encode("utf-8")).hexdigest(),
            "runtime_map_generated": False,
            "numeric_food_values_generated": False,
            "count": len(records),
            "resolved": sum(r["status"] == "resolved" for r in records),
            "partial": sum(r["status"] == "partial" for r in records),
            "unresolved": sum(r["status"] == "unresolved" for r in records),
            "records": records,
            "issues": [
                {"item": item, "reason": reason, "detail": detail}
                for item, reason, detail in sorted(self.issues)
            ],
        }


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--input", type=Path, required=True,
                        help="Versioned sourced inventory JSON (pilot only)")
    parser.add_argument("--output", type=Path, required=True,
                        help="Versioned deterministic JSON report (not a runtime data map)")
    args = parser.parse_args()
    model = FoodModel(json.loads(args.input.read_text(encoding="utf-8")))
    report = model.report()
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(report, indent=2, sort_keys=True, ensure_ascii=False) + "\n",
                           encoding="utf-8")
    print(f"Dragon food pilot: {report['resolved']} resolved, {report['partial']} partial, "
          f"{report['unresolved']} unresolved; {len(report['issues'])} source issues")
    print("SOURCE PILOT ONLY — no runtime diet map, food values, module build, or gameplay QA")


if __name__ == "__main__":
    try:
        main()
    except (ValueError, OSError, json.JSONDecodeError) as exc:
        print(f"FAILED: {exc}", file=sys.stderr)
        sys.exit(1)
