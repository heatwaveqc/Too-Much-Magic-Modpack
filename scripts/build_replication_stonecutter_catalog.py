#!/usr/bin/env python3
"""Build a reviewable Replication exclusion catalog from a live recipe capture.

The input is the gzip JSON written by the existing TMM runtime survey.  This
tool does not launch Minecraft and does not guess recipe outputs from file
names.  It consumes RecipeManager observations, identifies every item emitted
by an effective stonecutting recipe, and records overlap with Replication's
explicit item/tag matter-value anchors.
"""

from __future__ import annotations

import argparse
import csv
import gzip
import hashlib
import json
from collections import defaultdict
from pathlib import Path
from typing import Any


STONECUTTING_TYPE = "minecraft:stonecutting"
MATTER_VALUE_TYPE = "replication:matter_value"
TAG_NAMES = (
    "replication:cant_be_scanned",
    "replication:cant_be_disintegrated",
    "replication:skip_calculation",
)


def load_capture(path: Path) -> dict[str, Any]:
    opener = gzip.open if path.suffix == ".gz" else open
    with opener(path, "rt", encoding="utf-8") as handle:
        data = json.load(handle)
    if not isinstance(data.get("recipes"), list) or not isinstance(data.get("items"), list):
        raise ValueError("capture must contain recipe and item arrays")
    return data


def anchor_target(recipe_id: str) -> tuple[str, str] | None:
    prefix = "replication:matter_values/"
    if not recipe_id.startswith(prefix):
        return None
    rest = recipe_id[len(prefix) :]
    if "/items/" in rest:
        namespace, path = rest.split("/items/", 1)
        return "item", f"{namespace}:{path}"
    if "/tags/" in rest:
        namespace, path = rest.split("/tags/", 1)
        return "tag", f"{namespace}:{path}"
    return None


def build_catalog(data: dict[str, Any]) -> tuple[list[dict[str, Any]], dict[str, Any]]:
    if not isinstance(data.get("recipes"), list) or not isinstance(data.get("items"), list):
        raise ValueError("capture must contain recipe and item arrays")
    stonecutting: dict[str, list[str]] = defaultdict(list)
    other_outputs: dict[str, list[str]] = defaultdict(list)
    matter_item_anchors: set[str] = set()
    matter_tag_anchors: set[str] = set()

    recipes = data["recipes"]
    for recipe in recipes:
        recipe_id = recipe.get("id")
        recipe_type = recipe.get("type")
        if not isinstance(recipe_id, str) or not isinstance(recipe_type, str):
            continue
        if recipe_type == MATTER_VALUE_TYPE:
            target = anchor_target(recipe_id)
            if target:
                (matter_item_anchors if target[0] == "item" else matter_tag_anchors).add(target[1])
        output = recipe.get("output")
        item_id = output.get("id") if isinstance(output, dict) else None
        count = output.get("count", 1) if isinstance(output, dict) else 0
        if not isinstance(item_id, str) or item_id == "minecraft:air" or not isinstance(count, int) or count <= 0:
            continue
        target = stonecutting if recipe_type == STONECUTTING_TYPE else other_outputs
        target[item_id].append(recipe_id)

    item_tags: dict[str, set[str]] = {}
    for item in data["items"]:
        item_id = item.get("id")
        tags = item.get("tags")
        if isinstance(item_id, str) and isinstance(tags, list):
            item_tags[item_id] = {tag for tag in tags if isinstance(tag, str)}

    rows: list[dict[str, Any]] = []
    anchored_items: set[str] = set()
    for item_id in sorted(stonecutting):
        direct = item_id in matter_item_anchors
        matched_tags = sorted(item_tags.get(item_id, set()) & matter_tag_anchors)
        anchored = direct or bool(matched_tags)
        if anchored:
            anchored_items.add(item_id)
        rows.append(
            {
                "item_id": item_id,
                "stonecutting_recipe_count": len(set(stonecutting[item_id])),
                "stonecutting_recipe_ids": sorted(set(stonecutting[item_id])),
                "has_other_observed_recipe": item_id in other_outputs,
                "other_recipe_count": len(set(other_outputs.get(item_id, []))),
                "explicit_item_anchor": direct,
                "explicit_tag_anchors": matched_tags,
            }
        )

    summary = {
        "schema": 1,
        "capture_schema": data.get("schema"),
        "observed_recipe_count": len(recipes),
        "observed_stonecutting_recipe_count": sum(
            1 for recipe in recipes if recipe.get("type") == STONECUTTING_TYPE
        ),
        "unique_stonecutting_output_count": len(rows),
        "outputs_with_other_observed_recipe_count": sum(row["has_other_observed_recipe"] for row in rows),
        "replication_matter_value_recipe_count": sum(
            1 for recipe in recipes if recipe.get("type") == MATTER_VALUE_TYPE
        ),
        "direct_item_anchor_count": len(matter_item_anchors),
        "tag_anchor_count": len(matter_tag_anchors),
        "stonecutting_outputs_with_explicit_anchor_count": len(anchored_items),
        "stonecutting_outputs_with_explicit_anchor": sorted(anchored_items),
        "proposed_native_tags": list(TAG_NAMES),
        "limits": [
            "The catalog is exact only for the captured RecipeManager snapshot.",
            "A newer mod roster or datapack set requires a fresh authorized capture and regeneration.",
            "The catalog does not prove Replication blocks printing of previously learned patterns.",
        ],
    }
    return rows, summary


def write_outputs(rows: list[dict[str, Any]], summary: dict[str, Any], output: Path) -> None:
    output.mkdir(parents=True, exist_ok=True)
    with (output / "stonecutter-outputs.csv").open("w", newline="", encoding="utf-8") as handle:
        writer = csv.DictWriter(
            handle,
            fieldnames=(
                "item_id",
                "stonecutting_recipe_count",
                "stonecutting_recipe_ids",
                "has_other_observed_recipe",
                "other_recipe_count",
                "explicit_item_anchor",
                "explicit_tag_anchors",
            ),
        )
        writer.writeheader()
        for row in rows:
            writer.writerow(
                {
                    **row,
                    "stonecutting_recipe_ids": ";".join(row["stonecutting_recipe_ids"]),
                    "explicit_tag_anchors": ";".join(row["explicit_tag_anchors"]),
                }
            )
    (output / "summary.json").write_text(json.dumps(summary, indent=2) + "\n", encoding="utf-8")
    proposed = {
        "schema": 1,
        "source": "effective RecipeManager stonecutting outputs",
        "tags": {tag: [row["item_id"] for row in rows] for tag in TAG_NAMES},
    }
    (output / "proposed-native-tag-values.json").write_text(
        json.dumps(proposed, indent=2) + "\n", encoding="utf-8"
    )


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--capture", required=True, type=Path)
    parser.add_argument("--output", required=True, type=Path)
    args = parser.parse_args()
    rows, summary = build_catalog(load_capture(args.capture))
    summary["capture_sha256"] = hashlib.sha256(args.capture.read_bytes()).hexdigest()
    summary["capture_path"] = args.capture.as_posix()
    write_outputs(rows, summary, args.output)
    print(
        f"cataloged {summary['unique_stonecutting_output_count']} outputs from "
        f"{summary['observed_stonecutting_recipe_count']} effective stonecutting recipes"
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
