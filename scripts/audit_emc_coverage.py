#!/usr/bin/env python3
"""Audit ProjectE EMC completeness against *actual exported* loaded item IDs.

This does not invent an inventory or assign guesses. Export the registry and
POST-MAPPING EMC values from the same installed full integration build first.
Accepted minimal JSON:
  items: ["minecraft:stick", ...] or {"items": [...]}
  emc: {"minecraft:stick": 4, ...} or {"entries": {"...": {"emc": 4}}}
If JSON keys use NSS 'item|minecraft:stick', normalize that prefix.
"""
from __future__ import annotations

import argparse
import collections
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
POLICY = ROOT / "manifest/emc-coverage-policy.json"

def item_id(key: str) -> str:
    if key.startswith("item|"):
        key = key[5:]
    return key

def items(path: Path) -> set[str]:
    data = json.loads(path.read_text(encoding="utf-8"))
    content = data if isinstance(data, list) else data.get("items")
    if not isinstance(content, list):
        raise ValueError("Items JSON must be an array or {items:[...]}")
    result = set()
    for value in content:
        if not isinstance(value, str) or value.count(":") != 1:
            raise ValueError("Invalid item registry ID: " + repr(value))
        if value in result:
            raise ValueError("Duplicate item registry ID: " + value)
        result.add(value)
    if not result:
        raise ValueError("Real registry list cannot be empty")
    return result

def emc_values(path: Path) -> dict[str, int]:
    data = json.loads(path.read_text(encoding="utf-8"))
    if not isinstance(data, dict):
        raise ValueError("EMC JSON must be a map")
    content = data.get("values", data.get("entries", data))
    if not isinstance(content, dict):
        raise ValueError("EMC table must be a map")
    result = {}
    for key, value in content.items():
        key = item_id(key)
        if not isinstance(key, str) or key.count(":") != 1:
            raise ValueError("Invalid EMC item ID: " + repr(key))
        if isinstance(value, dict):
            value = value.get("emc")
        if isinstance(value, bool) or not isinstance(value, int) or value < 0:
            raise ValueError("EMC must be a non-negative integer: " + key)
        if key in result and result[key] != value:
            raise ValueError("Conflicting EMC value for item: " + key)
        result[key] = value
    return result

def analyze(registry: set[str], emc: dict[str, int], exclusions: set[str]) -> dict:
    pending = sorted(id for id in registry - exclusions if emc.get(id, 0) == 0)
    tiny = sorted(registry & exclusions)
    by_mod: dict[str, dict] = {}
    for id in sorted(registry):
        mod = id.split(":")[0]
        if mod not in by_mod:
            by_mod[mod] = {"registered":0, "positive_emc":0, "missing":0, "excepted":0}
        data = by_mod[mod]
        data["registered"] += 1
        if id in exclusions:
            data["excepted"] += 1
        elif emc.get(id,0) > 0:
            data["positive_emc"] += 1
        else:
            data["missing"] += 1
    valid = len(registry - exclusions)
    return {
        "goal": "100 percent positive EMC for each actual registered item except explicit cheap exceptions",
        "registry_items": len(registry), "approved_exceptions_in_registry": len(tiny),
        "applicable_items": valid, "positive_items": valid - len(pending),
        "missing_count": len(pending),
        "complete": len(pending) == 0,
        "missing_ids": pending, "approved_exception_ids": tiny,
        "per_mod": by_mod,
        "unregistered_emc_keys": sorted(set(emc) - registry),
        "exception_policy": "No auto-priced items and no undeclared silent skips"
    }

def main() -> int:
    p=argparse.ArgumentParser()
    p.add_argument("--items",required=True,type=Path,help="Actual loaded item ID JSON export")
    p.add_argument("--emc",required=True,type=Path,help="Actual post-mapping item EMC export; not just the custom override file")
    p.add_argument("--output",type=Path,default=None)
    p.add_argument("--enforce",action="store_true",help="Fail CI if any non-excepted item has zero/missing EMC")
    a=p.parse_args()
    policy=json.loads(POLICY.read_text(encoding="utf-8"))
    if policy.get("schema_version") != 1 or "explicitly_tiny_exceptions" not in policy:
        raise ValueError("Unknown EMC coverage policy")
    exceptions=set(policy["explicitly_tiny_exceptions"])
    if len(exceptions)!=len(policy["explicitly_tiny_exceptions"]):
        raise ValueError("Duplicate cheap-value exceptions")
    report=analyze(items(a.items),emc_values(a.emc),exceptions)
    if a.output:
        a.output.parent.mkdir(parents=True,exist_ok=True)
        a.output.write_text(json.dumps(report,indent=2)+"\n",encoding="utf-8")
    print(json.dumps({key:report[key] for key in (
      "registry_items","approved_exceptions_in_registry","applicable_items",
      "positive_items","missing_count","complete"
    )},indent=2))
    if report["missing_count"]:
        print("FIRST MISSING:",", ".join(report["missing_ids"][:30]))
    if a.enforce and not report["complete"]:
        print("FAIL: no missing EMC allowed except the explicit cheap-value exceptions")
        return 1
    return 0

if __name__ == "__main__":
    raise SystemExit(main())
