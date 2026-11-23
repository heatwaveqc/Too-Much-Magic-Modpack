#!/usr/bin/env python3
"""Offline names/IDs census from an exact captured Minecraft item registry.

Classification is deliberately conservative: "creative" or an explicit cheat
label makes a *candidate*, not a confirmed mechanic or acquisition route.
Other names such as "eternal" enter a separate REVIEW list. An item named
"Creative" may still be a legitimate creative-only diagnostic; an unnamed
component/NBT variant will never be discoverable from default-stack CSV alone.

No Minecraft/game boot, jar downloading, or recipe scanning.
"""
from __future__ import annotations

import argparse
import csv
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DEFAULT_INPUT = ROOT / "docs/qa/surveys-2026-11-18/items.csv"
DEFAULT_OUTPUT = ROOT / "docs/qa/CREATIVE-ITEM-CENSUS-2026-11-18.json"
EXPLICIT = re.compile(r"creative|ceremony_cheat", re.I)
REVIEW = re.compile(
    r"infinite|infinity|unlimited|everlasting|eternal|endless|"
    r"bottomless|infrangible|unbreakable|inexhaustible", re.I
)

# Confirmed only as a source-defined alternate stack of a regular registry item.
# Other stack variants require an installed game, source audit, or saved instance.
COMPONENT_VARIANTS = [
    {
        "base_item_id": "botania:mana_tablet",
        "variant": "Botania Revived Creative Mana Tablet",
        "identifier": "native custom data creative=1",
        "evidence": "Botania-Revived 2.0.2 CreativeCraftingModule.creativeTablet()",
        "verified_runtime_in_capture": False,
    }
]


def inventory(input_path: Path) -> dict:
    explicit: list[dict[str, str]] = []
    review: list[dict[str, str]] = []
    total = 0
    with input_path.open(encoding="utf-8", newline="") as handle:
        reader = csv.DictReader(handle)
        if not {"id", "name", "emc", "tags"}.issubset(reader.fieldnames or []):
            raise ValueError("Wrong item survey schema")
        for row in reader:
            total += 1
            item_id, name = row["id"], row["name"]
            item_path = item_id.split(":", 1)[-1]
            if not item_id or ":" not in item_id or not name:
                raise ValueError(f"Bad captured registry row {total}: {item_id!r}")
            candidate = {"id": item_id, "name": name, "default_stack_emc": row["emc"]}
            if EXPLICIT.search(item_path) or EXPLICIT.search(name):
                explicit.append(candidate)
            elif REVIEW.search(item_path) or REVIEW.search(name):
                review.append(candidate)

    return {
        "schema_version": 1,
        "source": "docs/qa/surveys-2026-11-18/items.csv",
        "capture_date": "2026-11-18",
        "installed_build_sha": "f7aeb56a0f7b9c5957f922f5a397037f25207c76",
        "source_installed_jar_count": 209,
        "method": "registered default-stack item ID and display name pattern match; NOT a recipe or creative-tab census",
        "registered_items_seen": total,
        "counts": {"explicit_creative_or_cheat": len(explicit), "name_only_review": len(review)},
        "explicit_creative_or_cheat": sorted(explicit, key=lambda x: x["id"]),
        "name_only_review": sorted(review, key=lambda x: x["id"]),
        "known_component_only_variants": COMPONENT_VARIANTS,
        "limitations": [
            "Historical installed build, not the live next-build/manifest pin roster",
            "A name is evidence of a candidate, never proof of creative functionality",
            "Creative-mode tab membership is not a disallowed creative-tier tag",
            "Default-stack item exports do not enumerate creative NBT/component variants",
            "Resource acquisition, EMC learning, recipes and loot are not checked",
        ],
    }


def main() -> None:
    p = argparse.ArgumentParser()
    p.add_argument("--input", type=Path, default=DEFAULT_INPUT)
    p.add_argument("--output", type=Path, default=DEFAULT_OUTPUT)
    p.add_argument("--check", action="store_true",
                   help="verify existing JSON snapshot against registry without changing it")
    args = p.parse_args()
    result = inventory(args.input)
    if args.check:
        observed = json.loads(args.output.read_text(encoding="utf-8"))
        if observed != result:
            raise SystemExit("FAIL: creative candidate index differs from captured registry")
    else:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(json.dumps(result, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    print(
        f"PASS {result['registered_items_seen']} registry IDs; "
        f"{result['counts']['explicit_creative_or_cheat']} explicit; "
        f"{result['counts']['name_only_review']} name-only review; "
        "1 known component-only variant; no recipe or gameplay claims"
    )


if __name__ == "__main__":
    main()
