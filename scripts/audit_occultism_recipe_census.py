#!/usr/bin/env python3
"""Read-only inspection of a *frozen, native runtime* recipe census gzip (#505).

Prints one exact recipe ID and bounded properties, never whole census/player data.
This is historical loaded-registry evidence, NOT a fresh game launch.
"""
from __future__ import annotations

import argparse
import gzip
import json
from pathlib import Path

ID = "tmm:ritual/equivalent_exchange"
MAX_BYTES = 32 * 1024 * 1024


def find_exact_recipe(data: object, target: str = ID) -> list[dict]:
    """Match an exact recipe ID in census data, not documentation substrings."""
    matches: list[dict] = []
    def walk(obj: object, at: str = "$") -> None:
        if isinstance(obj, dict):
            for key in ("id", "recipe_id", "recipeId", "location", "key"):
                if obj.get(key) == target:
                    matches.append({
                        "path": at, "id": target,
                        "type": obj.get("type", obj.get("recipe_type", obj.get("recipeType"))),
                        "result": obj.get("result", obj.get("output")),
                    })
                    break
            if target in obj:  # keyed recipe registry format
                nested = obj[target]
                matches.append({
                    "path": at + "[key]", "id": target,
                    "type": nested.get("type") if isinstance(nested, dict) else None,
                    "result": nested.get("result") if isinstance(nested, dict) else None,
                })
            for key, value in obj.items():
                if isinstance(value, (dict, list)):
                    walk(value, at + "." + str(key))
        elif isinstance(obj, list):
            for i, value in enumerate(obj):
                if isinstance(value, (dict, list)):
                    walk(value, at + "[" + str(i) + "]")
    walk(data)
    return matches


def read_census(path: Path) -> object:
    if path.stat().st_size > MAX_BYTES:
        raise ValueError("census file unexpectedly large")
    with gzip.open(path, "rb") as stream:
        blob = stream.read(MAX_BYTES + 1)
    if len(blob) > MAX_BYTES:
        raise ValueError("decompressed census exceeds limit")
    return json.loads(blob)


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("census", type=Path)
    args = parser.parse_args()
    obj = read_census(args.census)
    print("Census top-level keys:", sorted(obj.keys()) if isinstance(obj, dict) else type(obj).__name__)
    found = find_exact_recipe(obj)
    if found:
        print("HISTORICAL_REGISTRY_PRESENT:", json.dumps(found[:5], sort_keys=True)[:2500])
    else:
        print("HISTORICAL_REGISTRY_ID_NOT_FOUND:", ID)
        print("This is evidence from the old Experimental build only; check runtime load errors.")
    return 0  # observational result; missing historical recipe is not a source-test error


if __name__ == "__main__":
    raise SystemExit(main())
