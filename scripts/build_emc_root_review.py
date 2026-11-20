#!/usr/bin/env python3
"""Build a review queue from a same-build EMC census without inventing prices.

The input census is observational. A missing generic RecipeManager output is
not proof that an item is a true economic root, and a visible output is not
proof that ProjectE accounts for every ingredient or returned catalyst.
"""
from __future__ import annotations

import argparse
import csv
import hashlib
import json
from collections import defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DEFAULT_INPUT = ROOT / "docs/qa/surveys-2026-11-18/items.csv"
DEFAULT_SUMMARY = ROOT / "docs/qa/surveys-2026-11-18/summary.json"
DEFAULT_POLICY = ROOT / "manifest/emc-coverage-policy.json"
DEFAULT_OUTPUT = ROOT / "docs/qa/emc-root-review-2026-11-20"


def digest(path: Path) -> str:
    return hashlib.sha256(path.read_bytes()).hexdigest()


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--items", type=Path, default=DEFAULT_INPUT)
    parser.add_argument("--summary", type=Path, default=DEFAULT_SUMMARY)
    parser.add_argument("--policy", type=Path, default=DEFAULT_POLICY)
    parser.add_argument("--output", type=Path, default=DEFAULT_OUTPUT)
    parser.add_argument("--full-queue", action="store_true",
                        help="also write a large per-item derivative CSV; raw rows already live in items.csv")
    args = parser.parse_args()

    summary = json.loads(args.summary.read_text(encoding="utf-8"))
    policy = json.loads(args.policy.read_text(encoding="utf-8"))
    exceptions = set(policy["explicitly_tiny_exceptions"])
    args.output.mkdir(parents=True, exist_ok=True)

    per_namespace: dict[str, dict[str, int]] = defaultdict(
        lambda: {"registered": 0, "positive": 0, "approved_exception": 0,
                 "recipe_output_unpriced": 0, "root_or_special_candidate": 0,
                 "other_unpriced": 0}
    )
    review_rows = []
    with args.items.open(encoding="utf-8", newline="") as f:
        for row in csv.DictReader(f):
            item_id = row["id"]
            namespace = item_id.split(":", 1)[0]
            stats = per_namespace[namespace]
            stats["registered"] += 1
            value = int(row["emc"] or 0)
            if value > 0:
                stats["positive"] += 1
                continue
            if item_id in exceptions:
                stats["approved_exception"] += 1
                continue

            triage = row["triage"]
            if triage not in ("recipe_output_unpriced", "root_or_special_candidate"):
                stats["other_unpriced"] += 1
                triage = "unresolved_source_classification"
                interpretation = "Unpriced in capture; source category was not one of the two census queues."
            elif triage == "recipe_output_unpriced":
                stats["recipe_output_unpriced"] += 1
                interpretation = (
                    "At least one generic recipe output was observed; inspect complete ingredient, "
                    "count, catalyst, return, mapper, and alternate-route accounting."
                )
            else:
                stats["root_or_special_candidate"] += 1
                interpretation = (
                    "No generic RecipeManager output was observed; this is not proof of a true root. "
                    "Inspect loot, drops, world generation, custom producers, eligibility, and state."
                )

            review_rows.append({
                "registry_id": item_id,
                "display_name": row["name"],
                "namespace": namespace,
                "default_stack_emc": row["emc"],
                "numeric_precision": row["numeric_precision"],
                "stored_default_entry": row["has_stored_default_entry"],
                "observed_status": row["state"],
                "census_queue": triage,
                "tag_evidence": row["tags"],
                "generic_recipe_output_ids": row["recipe_outputs"],
                "static_loot_resource_refs": row["static_loot_references"],
                "interpretation_and_next_step": interpretation,
                "provider_pin_resolution": "not assigned per item; use frozen installed-artifact census",
                "acquisition_yield_cost_and_gate": "unknown until exact source and recipe/drop path review",
                "mapper_blacklist_or_component_attribution": "unknown unless captured in item-level evidence",
            })

    queue_path = args.output / "unpriced-review-queue.csv"
    if args.full_queue:
        fields = list(review_rows[0]) if review_rows else ["registry_id"]
        with queue_path.open("w", encoding="utf-8", newline="") as f:
            writer = csv.DictWriter(f, fieldnames=fields, lineterminator="\n")
            writer.writeheader()
            writer.writerows(sorted(review_rows, key=lambda r: (r["census_queue"], r["namespace"], r["registry_id"])))
    else:
        queue_path.unlink(missing_ok=True)

    summary_fields = ["namespace", "registered", "positive", "approved_exception",
                      "recipe_output_unpriced", "root_or_special_candidate", "other_unpriced"]
    with (args.output / "namespace-summary.csv").open("w", encoding="utf-8", newline="") as f:
        writer = csv.DictWriter(f, fieldnames=summary_fields, lineterminator="\n")
        writer.writeheader()
        for namespace, stats in sorted(per_namespace.items()):
            writer.writerow({"namespace": namespace, **stats})

    total = {
        "registered_items": sum(x["registered"] for x in per_namespace.values()),
        "positive_default_stack_emc": sum(x["positive"] for x in per_namespace.values()),
        "approved_exception_zero_default_emc": sum(x["approved_exception"] for x in per_namespace.values()),
        "unpriced_review_rows": len(review_rows),
        "recipe_output_unpriced": sum(x["recipe_output_unpriced"] for x in per_namespace.values()),
        "no_generic_recipe_output_observed": sum(x["root_or_special_candidate"] for x in per_namespace.values()),
        "other_unpriced_unclassified": sum(x["other_unpriced"] for x in per_namespace.values()),
    }
    output = {
        "schema_version": 1,
        "status": "STATIC_REVIEW_QUEUE_NOT_FINAL_ROOT_CLASSIFICATION_OR_PRICE_APPROVAL",
        "installed_build_sha": "f7aeb56a0f7b9c5957f922f5a397037f25207c76",
        "installed_baseline_sha": "a81723733b4437770f61f566afe583343ccccff9",
        "installed_mod_jar_count": 209,
        "source_capture_time": "2026-11-18T12:19:31-07:00",
        "capture_sha256": summary["capture_sha256"],
        "source_files": {
            str(args.items.relative_to(ROOT) if args.items.is_relative_to(ROOT) else args.items): digest(args.items),
            str(args.summary.relative_to(ROOT) if args.summary.is_relative_to(ROOT) else args.summary): digest(args.summary),
            str(args.policy.relative_to(ROOT) if args.policy.is_relative_to(ROOT) else args.policy): digest(args.policy),
        },
        "totals": total,
        "limitations": [
            "Census measures default-stack EMC and source references; it does not resolve every item component or alternate stack state.",
            "recipe_output_unpriced is a recipe investigation queue, not proof of a missing mapper or free recipe.",
            "root_or_special_candidate means no generic RecipeManager output was observed, not that the item is a true economic root.",
            "Static loot references do not prove conditional, enabled, reachable, renewable, or player-obtainable drops.",
            "Acquisition yields, resource costs, return containers, SCC/rootless cycles, mapper/blacklist attribution, and most provider-to-namespace joins remain unresolved.",
            "Eight measured default EMC values exceed JavaScript's exact integer range; use native long serialization before pricing work.",
            "The capture is the installed 2026-11-18 snapshot, not the moving 2026-11-20 integration head.",
        ],
    }
    (args.output / "summary.json").write_text(json.dumps(output, indent=2) + "\n", encoding="utf-8")
    print(json.dumps({"totals": total, "namespaces": len(per_namespace)}, indent=2))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
