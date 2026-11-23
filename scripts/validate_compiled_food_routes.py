#!/usr/bin/env python3
"""Source-only consistency checks for compiled food recipe candidate artifacts."""
from __future__ import annotations
import argparse
import json
from pathlib import Path
import sys

from compile_crafted_food_survey import DATA, ROUTES, SOURCES, TAGS, identifier, read_records


def validate(repo: Path, output: Path):
    native = repo / "compat/progression/src/main/resources"
    roots = read_records(native / SOURCES)
    legacy = read_records(native / ROUTES)
    added_roots = read_records(output / SOURCES)
    routes = read_records(output / ROUTES)
    if set(roots) & set(added_roots) or set(legacy) & set(routes):
        raise ValueError("Generated food evidence would overwrite a reviewed record")
    known_roots = set(roots) | set(added_roots)
    known_routes = set(legacy) | set(routes)
    known = known_roots | known_routes
    for item, entry in routes.items():
        if entry.get("schema") != 1 or entry.get("complete") is not True:
            raise ValueError(f"Unverified route schema: {item}")
        if entry.get("identity") not in ("ordinary", "unfavored_all"):
            raise ValueError(f"Unknown final dish identity: {item}")
        if any(k not in ("schema", "item", "identity", "complete", "routes")
               for k in entry):
            raise ValueError(f"Unrecognized route property: {item}")
        candidates = entry.get("routes")
        if not isinstance(candidates, list) or not 1 <= len(candidates) <= 64:
            raise ValueError(f"Route count outside supported bound: {item}")
        fingerprints = set()
        for route in candidates:
            receipt = route.get("receipt")
            method = route.get("method")
            count = route.get("result_count")
            efficiency = route.get("efficiency", 1)
            inputs = route.get("ingredients")
            if (not identifier(receipt) or method not in ("heat", "nonthermal", "char")
                    or type(count) is not int or not 1 <= count <= 64
                    or type(efficiency) not in (int, float)
                    or not 1 <= efficiency <= 4
                    or method != "nonthermal" and efficiency != 1
                    or not isinstance(inputs, list) or not 1 <= len(inputs) <= 32):
                raise ValueError(f"Malformed route: {item}, {receipt}")
            digest = (receipt, method, count, efficiency,
                      tuple((part.get("item"), part.get("count")) for part in inputs))
            if digest in fingerprints:
                raise ValueError(f"Duplicate concrete route: {item}, {receipt}")
            fingerprints.add(digest)
            for part in inputs:
                ingredient = part.get("item")
                amount = part.get("count")
                if (not identifier(ingredient) or type(amount) is not int
                        or not 1 <= amount <= 64 or ingredient not in known):
                    raise ValueError(f"Unresolved nutritive input: {item}, {ingredient}")
                if "role" in part or set(part) != {"item", "count"}:
                    raise ValueError(f"Unexpected material component: {item}")
    visited = set()
    def visit(item, path):
        if item in path:
            raise ValueError("Food recipe loop: " + " -> ".join(path + (item,)))
        if item in visited:
            return
        entry = routes.get(item) or legacy.get(item)
        if entry:
            for route in entry["routes"]:
                for ingredient in route["ingredients"]:
                    if ingredient.get("role", "food") == "food":
                        visit(ingredient["item"], path + (item,))
        visited.add(item)
    for item in routes:
        visit(item, ())
    for item, source in added_roots.items():
        if (source.get("schema") != 1 or source.get("source_units") != 1
                or not source.get("inferred_tags")
                or any(x in source for x in ("nutrition", "saturation", "food_component"))):
            raise ValueError(f"Generated material would change native nutrition: {item}")
    report = json.loads((output / "crafted-food-survey-report.json").read_text())
    candidates = set(report["generated_ids"])
    supports = set(report["support_ids"])
    if (report["generated_candidate_recipe_outputs"] != len(candidates)
            or report["generated_support_recipe_outputs"] != len(supports)
            or report["emitted_recipe_route_records"] != len(routes)
            or report["generated_material_sources"] != len(added_roots)
            or candidates & supports
            or candidates | supports != set(routes)):
        raise ValueError("Summary report disagrees with generated route/dependency scope")
    for path in (output / TAGS).glob("*.json"):
        value = json.loads(path.read_text())
        entries = value["values"]
        if (value.get("replace") is not False or entries != sorted(set(entries))
                or set(entries) - candidates):
            raise ValueError(f"Invalid or ungrounded crafted candidate tag {path}")
    if set(report["excluded_output_ids"]) & set(routes):
        raise ValueError("Generated a known seed/container/vanilla-owned output")
    print(f"Validated {len(candidates)} food-tagged candidate routes, "
          f"{len(supports)} dependency routes, "
          f"{len(added_roots)} derived material sources and "
          f"{len(list((output / TAGS).glob('*.json')))} generated category tags")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--repo", type=Path, required=True)
    parser.add_argument("--out", type=Path, required=True)
    args = parser.parse_args()
    validate(args.repo, args.out)


if __name__ == "__main__":
    main()
