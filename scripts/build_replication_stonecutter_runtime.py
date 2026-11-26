#!/usr/bin/env python3
"""Generate Replication runtime exclusions from an audited RecipeManager capture.

Use build_replication_stonecutter_catalog.py first. Only the integration writer
promotes these files with updated Packwiz hashes and current-roster evidence.
"""

from __future__ import annotations

import argparse
import csv
import json
import re
from pathlib import Path

CANONICAL = "tmm:replication/stonecutter_outputs"
NATIVE_TAGS = (
    "replication:cant_be_scanned",
    "replication:cant_be_disintegrated",
    "replication:skip_calculation",
)
ITEM_ID = re.compile(r"^[a-z0-9_.-]+:[a-z0-9_./-]+$")


def load_catalog(directory: Path) -> tuple[list[str], list[dict[str, str]], dict]:
    proposed = json.loads((directory / "proposed-native-tag-values.json").read_text(encoding="utf-8"))
    summary = json.loads((directory / "summary.json").read_text(encoding="utf-8"))
    with (directory / "stonecutter-outputs.csv").open(newline="", encoding="utf-8") as handle:
        rows = list(csv.DictReader(handle))
    if proposed.get("schema") != 1 or summary.get("schema") != 1:
        raise ValueError("Unsupported Replication catalog schema")
    if set(proposed.get("tags", {})) != set(NATIVE_TAGS):
        raise ValueError("Expected the three native Replication tags")
    items = proposed["tags"][NATIVE_TAGS[0]]
    if not isinstance(items, list) or not items or items != sorted(set(items)):
        raise ValueError("Stonecutter outputs must be nonempty, sorted, unique")
    if any(not isinstance(item, str) or ITEM_ID.fullmatch(item) is None for item in items):
        raise ValueError("Invalid item ID in catalog")
    if any(proposed["tags"][name] != items for name in NATIVE_TAGS):
        raise ValueError("Native prevention tag candidates diverged")
    if [row["item_id"] for row in rows] != items:
        raise ValueError("CSV output IDs and tag candidates disagree")
    if summary.get("unique_stonecutting_output_count") != len(items):
        raise ValueError("Summary output count disagrees")
    if summary.get("observed_stonecutting_recipe_count", 0) < len(items):
        raise ValueError("Invalid stonecutting recipe count")

    # The original Replication 1.2.7 anchor c:stones gives 2 earth. A
    # NeoForge difference ingredient preserves raw/base stones' anchor, while
    # avoiding an explicit value for polished stonecutter outputs. Fail closed
    # if future captures show any other explicit item/tag matter anchor.
    overlap = []
    for row in rows:
        tags = [tag for tag in row["explicit_tag_anchors"].split(";") if tag]
        if row["explicit_item_anchor"] == "True" or any(tag != "c:stones" for tag in tags):
            raise ValueError(f"Uncovered explicit matter anchor on {row['item_id']}")
        if tags:
            overlap.append(row["item_id"])
    if overlap != summary.get("stonecutting_outputs_with_explicit_anchor"):
        raise ValueError("Anchor IDs disagree with the audited summary")
    if summary.get("stonecutting_outputs_with_explicit_anchor_count") != len(overlap):
        raise ValueError("Anchor count disagrees")
    return items, rows, summary


def runtime_files(items: list[str]) -> dict[str, dict]:
    """Produce five KubeJS datapack resources with no Packwiz side effects."""
    files = {
        "tmm/tags/item/replication/stonecutter_outputs.json": {
            "replace": False, "values": items,
        },
        "replication/recipe/matter_values/c/tags/stones.json": {
            "type": "replication:matter_value",
            "input": {
                "type": "neoforge:difference",
                "base": {"tag": "c:stones"},
                "subtracted": {"tag": CANONICAL},
            },
            "matter": [{"type": "replication:earth", "amount": 2.0}],
        },
    }
    for tag in NATIVE_TAGS:
        namespace, name = tag.split(":", 1)
        files[f"{namespace}/tags/item/{name}.json"] = {
            "replace": False, "values": [f"#{CANONICAL}"],
        }
    return files


def write_runtime(catalog_dir: Path, output_root: Path) -> list[Path]:
    items, _, summary = load_catalog(catalog_dir)
    written = []
    for name, document in runtime_files(items).items():
        target = output_root / name
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_text(json.dumps(document, indent=2) + "\n", encoding="utf-8")
        written.append(target)
    print(f"STAGED {len(items)} IDs, {len(written)} files from {summary['capture_sha256']}")
    return written


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--catalog-dir", type=Path, required=True)
    parser.add_argument("--output-root", type=Path, required=True)
    args = parser.parse_args()
    write_runtime(args.catalog_dir, args.output_root)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
