#!/usr/bin/env python3
"""Reproduce tag-first nutrition source evidence from reviewed source receipts.

No per-item nutrition or hunger tuning is stored. The pinned receipt ledger
supplies family, preparation and whole-input portion units. Actual food values
are computed by Progression's configurable FoodPolicyRules. An unresolved
recipe NEVER becomes a record through a guessed fallback.
"""
from __future__ import annotations

import argparse
import json
import math
import re
from pathlib import Path

ID = re.compile(r"^[a-z0-9_.-]+:[a-z0-9_./-]+$")
STAGES = {"raw", "cooked", "charred"}
FAMILY = {
    "fruit": "#c:foods/fruit",
    "vegetable": "#c:foods/vegetable",
    "meat": "#c:foods/meat",
    "fish": "#c:foods/fish",
}
RAW_FAMILY = {"meat": "#c:foods/raw_meat", "fish": "#c:foods/raw_fish"}
SOURCE_DIR = Path("data/tyr_progression/tyr_progression/food_sources")
TAG_DIR = Path("data/tmm/tags/item/foods")


def _record(item, family, stage, units, provenance, optional=False):
    if not all(isinstance(x, str) and ID.fullmatch(x) for x in (item, provenance)):
        raise ValueError(f"Invalid item/provenance: {item} {provenance}")
    if family not in FAMILY or stage not in STAGES:
        raise ValueError(f"Unknown family/stage for {item}")
    if type(units) not in (float, int) or not math.isfinite(units) or not 0 < units <= 64:
        raise ValueError(f"Invalid source unit amount for {item}")
    family_tag = RAW_FAMILY.get(family, FAMILY[family]) if stage == "raw" else FAMILY[family]
    stage_tag = f"#tmm:foods/{stage}"
    return {
        "schema": 1,
        "item": item,
        "provenance": provenance,
        "source_units": units,
        "inferred_tags": sorted((family_tag, stage_tag)),
    }


def build(manifest):
    if manifest.get("schema") != 1:
        raise ValueError("Unsupported source receipt version")
    entries = {}
    optional_ids = set()
    pins = {p["source"]: p for p in manifest["pinned_sources"]}
    if any(not re.fullmatch(r"[0-9a-f]{40}", p["sha1"]) for p in pins.values()):
        raise ValueError("Missing exact SHA1 receipt")

    def insert(e, optional=False):
        item = e["item"]
        if item in entries:
            raise ValueError(f"Conflicting source routes: {item}")
        entries[item] = _record(item, e["family"], e["stage"],
                                e["source_units"], e["provenance"], optional)
        if optional:
            optional_ids.add(item)

    for group in manifest["native_tag_roots"]:
        if group["source"] not in pins:
            raise ValueError("Unknown pinned native group source")
        if group["provenance"] not in ("c:fruits", "c:vegetables", "c:foods/raw_fish"):
            raise ValueError("Unexpected root tag cannot prove whole food source")
        stems = group["stems"]
        if not stems or stems != sorted(set(stems)):
            raise ValueError("Native root stems must be sorted and unique")
        for stem in stems:
            if not re.fullmatch("[a-z0-9_]+", stem):
                raise ValueError("Invalid root stem")
            insert(dict(item=f"{group['namespace']}:{stem}{group['suffix']}",
                        family=group["family"], stage=group["stage"],
                        source_units=1, provenance=group["provenance"]),
                   pins[group["source"]].get("not_indexed", False))

    for entry in manifest["verified_direct"]:
        insert(entry, entry.get("not_indexed", False))

    for entry in manifest["verified_processing"]:
        parent = entry["parent"]
        if parent not in entries:
            raise ValueError(f"Missing verified processing parent {parent}")
        if entry["provenance"] == parent:
            raise ValueError("Processing requires a real recipe receipt")
        units = entry["source_units"]
        if not 0 < units <= 64 or entry["count"] <= 0:
            raise ValueError("Invalid processing portion/count")
        # Relative source units are *not* necessarily 1 / output count:
        # native minced beef returns 2 x 2 hunger from 3 hunger beef.
        insert(entry)

    holds = manifest["explicit_holds"]
    held_ids = [e["item"] for e in holds]
    if len(held_ids) != len(set(held_ids)):
        raise ValueError("Duplicate holds")
    if set(held_ids) & set(entries):
        raise ValueError("An explicitly unresolved item was assigned a source value")

    files = {}
    stage_ids = {stage: [] for stage in STAGES}
    for item in sorted(entries):
        payload = entries[item]
        ns, path = item.split(":", 1)
        files[str(SOURCE_DIR / ns / (path + ".json"))] = payload
        stage = next(s for s in STAGES if f"#tmm:foods/{s}" in payload["inferred_tags"])
        stage_ids[stage].append({"id": item, "required": False} if item in optional_ids else item)
    for stage in sorted(STAGES):
        files[str(TAG_DIR / f"{stage}.json")] = {
            "replace": False, "values": stage_ids[stage],
        }
    return files


def write(manifest_path: Path, output_root: Path):
    manifest = json.loads(manifest_path.read_text(encoding="utf-8"))
    files = build(manifest)
    for relative, payload in files.items():
        target = output_root / relative
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_text(json.dumps(payload, indent=2) + "\n", encoding="utf-8")
    print(f"Generated {len(files) - 3} source records + 3 shared preparation tags")
    return files


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--receipts", type=Path, required=True)
    parser.add_argument("--output-root", type=Path, required=True)
    args = parser.parse_args()
    write(args.receipts, args.output_root)


if __name__ == "__main__":
    main()
