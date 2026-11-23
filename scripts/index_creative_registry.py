#!/usr/bin/env python3
"""Offline creative candidate census from an exact installed item registry.

Default-stack names are candidate signals, not recipe or acquisition evidence.
Source-confirmed items and component-only variants are tracked separately.
New captures must carry explicit metadata; no Minecraft boot or JAR downloads.
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
HISTORICAL_CAPTURE = {
    "source": "docs/qa/surveys-2026-11-18/items.csv",
    "capture_date": "2026-11-18",
    "installed_build_sha": "f7aeb56a0f7b9c5957f922f5a397037f25207c76",
    "source_installed_jar_count": 209,
}
EXPLICIT = re.compile(r"creative|ceremony_cheat", re.I)
REVIEW = re.compile(
    r"infinite|infinity|unlimited|everlasting|eternal|endless|"
    r"bottomless|infrangible|unbreakable|inexhaustible", re.I
)

# Source-backed semantics that name/ID heuristics miss. Not an acquisition ban.
SOURCE_CONFIRMED = [
    {
        "id": "neovitae:blood_battery",
        "classification": "creative_class",
        "function": "Refills FE on redstone signal without EV/Anima/Spiritus debit",
        "evidence": (
            "Neo Vitae 1.1.54 BloodBatteryBlock Creative Only tooltip and "
            "BloodBatteryBlockEntity; issue #596"
        ),
        "verified_runtime_in_capture": False,
    }
]
# Alternate stack states cannot be discovered from default-stack item IDs.
COMPONENT_VARIANTS = [
    {
        "base_item_id": "botania:mana_tablet",
        "variant": "Botania Revived Creative Mana Tablet",
        "identifier": "native custom data creative=1",
        "evidence": "Botania-Revived 2.0.2 CreativeCraftingModule.creativeTablet()",
        "verified_runtime_in_capture": False,
    }
]
FIELDS = ("source", "capture_date", "installed_build_sha", "source_installed_jar_count")


def read_metadata(input_path: Path, metadata_path: Path | None) -> dict:
    """Require explicit provenance whenever the input is not the frozen capture."""
    if metadata_path is None:
        if input_path.resolve() != DEFAULT_INPUT.resolve():
            raise ValueError("Custom --input requires --metadata with capture provenance")
        return HISTORICAL_CAPTURE.copy()
    data = json.loads(metadata_path.read_text(encoding="utf-8"))
    if not isinstance(data, dict) or any(key not in data for key in FIELDS):
        raise ValueError("Metadata needs source, capture_date, installed_build_sha and source_installed_jar_count")
    meta = {key: data[key] for key in FIELDS}
    if any(not isinstance(meta[key], str) or not meta[key].strip() for key in FIELDS[:3]):
        raise ValueError("Capture source, date and build SHA must be non-empty strings")
    if not re.fullmatch(r"[0-9a-f]{40}", meta["installed_build_sha"]):
        raise ValueError("Installed build SHA must be 40 lowercase hex characters")
    count = meta["source_installed_jar_count"]
    if not isinstance(count, int) or isinstance(count, bool) or count < 1:
        raise ValueError("Installed JAR count must be a positive integer")
    return meta


def inventory(input_path: Path, metadata: dict) -> dict:
    explicit: list[dict[str, str]] = []
    review: list[dict[str, str]] = []
    seen: set[str] = set()
    total = 0
    with input_path.open(encoding="utf-8", newline="") as handle:
        reader = csv.DictReader(handle)
        if not {"id", "name", "emc", "tags"}.issubset(reader.fieldnames or []):
            raise ValueError("Wrong item survey schema")
        for row in reader:
            total += 1
            item_id, name = row["id"], row["name"]
            if not item_id or ":" not in item_id:
                raise ValueError(f"Bad registry row {total}: {item_id!r}")
            if item_id in seen:
                raise ValueError(f"Duplicate captured registry ID: {item_id}")
            seen.add(item_id)
            # Legitimate default stacks can have blank translated names.
            candidate = {"id": item_id, "name": name, "default_stack_emc": row["emc"]}
            item_path = item_id.split(":", 1)[-1]
            if EXPLICIT.search(item_path) or EXPLICIT.search(name):
                explicit.append(candidate)
            elif REVIEW.search(item_path) or REVIEW.search(name):
                review.append(candidate)

    return {
        "schema_version": 2,
        **metadata,
        "method": "registered default-stack item ID and display name pattern match; NOT a recipe or creative-tab census",
        "registered_items_seen": total,
        "counts": {
            "explicit_creative_or_cheat": len(explicit),
            "name_only_review": len(review),
            "source_confirmed_creative_candidates": len(SOURCE_CONFIRMED),
        },
        "explicit_creative_or_cheat": sorted(explicit, key=lambda x: x["id"]),
        "name_only_review": sorted(review, key=lambda x: x["id"]),
        "source_confirmed_creative_candidates": [
            {**candidate, "registered_in_capture": candidate["id"] in seen}
            for candidate in SOURCE_CONFIRMED
        ],
        "known_component_only_variants": COMPONENT_VARIANTS,
        "limitations": [
            "Historical installed build, not the live next-build/manifest pin roster",
            "A name is evidence of a candidate, never proof of creative functionality",
            "Creative-mode tab membership is not a disallowed creative-tier tag",
            "Default-stack item exports do not enumerate creative NBT/component variants",
            "Resource acquisition, EMC learning, recipes and loot are not checked",
            "Source-confirmed creative function does not itself prove survival acquisition",
        ],
    }


def candidate_diff(current: dict, earlier: dict) -> dict:
    """Compare candidate IDs only; never claim additions prove new survival access."""
    categories = {
        "explicit_creative_or_cheat": "id",
        "name_only_review": "id",
        "source_confirmed_creative_candidates": "id",
        "known_component_only_variants": "base_item_id",
    }
    changes = {}
    for group, key in categories.items():
        before = {row[key] for row in earlier.get(group, [])}
        after = {row[key] for row in current.get(group, [])}
        changes[group] = {"added": sorted(after - before), "removed": sorted(before - after)}
    return {
        "comparison_only": True,
        "old_source": earlier.get("source"),
        "new_source": current["source"],
        "old_build_sha": earlier.get("installed_build_sha"),
        "new_build_sha": current["installed_build_sha"],
        "registered_item_delta": current["registered_items_seen"] - earlier.get("registered_items_seen", 0),
        "candidates": changes,
        "caution": "Registry differences alone do not establish acquisition, EMC or gameplay policy.",
    }


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--input", type=Path, default=DEFAULT_INPUT)
    parser.add_argument("--output", type=Path, default=DEFAULT_OUTPUT)
    parser.add_argument("--metadata", type=Path,
                        help="capture metadata JSON (required for custom --input)")
    parser.add_argument("--compare", type=Path,
                        help="previous snapshot JSON for a source-only candidate diff")
    parser.add_argument("--diff-output", type=Path,
                        help="save comparison JSON (requires --compare)")
    parser.add_argument("--check", action="store_true",
                        help="compare regenerated snapshot to existing output")
    args = parser.parse_args()
    if args.diff_output and not args.compare:
        parser.error("--diff-output requires --compare")
    try:
        metadata = read_metadata(args.input, args.metadata)
    except (ValueError, OSError, json.JSONDecodeError) as exc:
        parser.error(str(exc))
    result = inventory(args.input, metadata)
    if args.check:
        observed = json.loads(args.output.read_text(encoding="utf-8"))
        if observed != result:
            raise SystemExit("FAIL: creative candidate index differs from captured registry")
    else:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(json.dumps(result, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    if args.compare:
        old = json.loads(args.compare.read_text(encoding="utf-8"))
        diff = candidate_diff(result, old)
        if args.diff_output:
            args.diff_output.parent.mkdir(parents=True, exist_ok=True)
            args.diff_output.write_text(json.dumps(diff, indent=2) + "\n", encoding="utf-8")
        print(json.dumps(diff, indent=2))
    print(
        f"PASS {result['registered_items_seen']} registry IDs; "
        f"{result['counts']['explicit_creative_or_cheat']} explicit; "
        f"{result['counts']['name_only_review']} name-only; "
        f"{len(SOURCE_CONFIRMED)} source-backed; "
        "1 known component-only variant; no recipe or gameplay claims"
    )


if __name__ == "__main__":
    main()
