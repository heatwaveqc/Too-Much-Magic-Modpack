#!/usr/bin/env python3
"""Validate an actual TMM Economy same-invocation ProjectE snapshot and report missing EMC.

Unlike a source-only candidate census, a runtime snapshot has items.json,
emc.json, status.json, and metadata.json emitted together by the admin command.
The operator-provided integration SHA is a *label*, not cryptographic attestation;
verify the assembled manifest and mod hashes separately.
"""
from __future__ import annotations

import argparse
import hashlib
import json
from pathlib import Path

from audit_emc_coverage import analyze, emc_values, items, ROOT


def validate(snapshot: Path, expected_sha: str | None = None) -> dict:
    if not snapshot.is_dir() or snapshot.name.startswith("incomplete-"):
        raise ValueError("Use a completed EMC audit snapshot directory")
    paths = {name: snapshot / f"{name}.json" for name in
             ("items", "emc", "status", "metadata")}
    if any(not p.is_file() for p in paths.values()):
        raise ValueError("Incomplete same-invocation snapshot: items/emc/status/metadata required")

    registry = items(paths["items"])
    mapped = emc_values(paths["emc"])
    status = json.loads(paths["status"].read_text(encoding="utf-8"))
    meta = json.loads(paths["metadata"].read_text(encoding="utf-8"))
    if status.get("schema_version") != 1 or meta.get("schema_version") != 1:
        raise ValueError("Unsupported snapshot schema")
    source_sha = meta.get("source_sha_label")
    if not isinstance(source_sha, str) or len(source_sha) != 40 or any(
            c not in "0123456789abcdef" for c in source_sha):
        raise ValueError("Missing/invalid operator-provided integration SHA label")
    if expected_sha is not None and source_sha != expected_sha.lower():
        raise ValueError("Snapshot source SHA differs from expected integration SHA")

    missing = sorted(registry - mapped.keys())
    zero = sorted(key for key, val in mapped.items() if val == 0)
    positive = sum(1 for val in mapped.values() if val > 0)
    for field, computed in (
        ("registry_items", len(registry)),
        ("mapped_positive", positive),
        ("mapped_zero", len(zero)),
        ("unmapped", len(missing)),
    ):
        if status.get(field) != computed or meta.get(field) != computed:
            raise ValueError(f"{field}: registry/EMC/status/metadata disagree")
    if status.get("unmapped_ids") != missing or status.get("mapped_zero_ids") != zero:
        raise ValueError("Snapshot status IDs do not match actual registry and EMC values")
    if not set(mapped).issubset(registry):
        raise ValueError("EMC map contains registry IDs missing from this export")

    policy = json.loads((ROOT / "manifest/emc-coverage-policy.json").read_text(encoding="utf-8"))
    exclusions = set(policy["explicitly_tiny_exceptions"])
    exclusions.update(policy.get("non_transmutable_document_exceptions", []))
    report = analyze(registry, mapped, exclusions)
    report["source_sha_label"] = source_sha
    report["source_sha_verified"] = False
    report["projecte_map_generation"] = meta.get("projecte_map_generation")
    report["captured_utc"] = meta.get("captured_utc")
    report["unmapped_count"] = len(missing)
    report["mapped_zero_count"] = len(zero)
    report["unmapped_ids"] = missing
    report["mapped_zero_ids"] = zero
    report["snapshot_file_sha256"] = {name: hashlib.sha256(
        path.read_bytes()).hexdigest() for name, path in sorted(paths.items())}
    return report


def main() -> int:
    p = argparse.ArgumentParser()
    p.add_argument("snapshot", type=Path,
                   help="Complete world/tmm-economy/emc-audits/source-*/ directory")
    p.add_argument("--expected-source-sha",
                   help="40-hex commit SHA from the immutable integrated pack manifest")
    p.add_argument("--output", type=Path, help="Write JSON report to this path")
    p.add_argument("--enforce", action="store_true",
                   help="Fail if any non-excepted registered ID remains zero/absent")
    args = p.parse_args()

    report = validate(args.snapshot, args.expected_source_sha)
    if args.output:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(json.dumps(report, indent=2) + "\n", encoding="utf-8")
    print(json.dumps({key:report[key] for key in (
        "registry_items", "positive_items", "missing_count",
        "unmapped_count", "mapped_zero_count", "complete")}, indent=2))
    print("Operator-source-SHA label:", report["source_sha_label"],
          "(verify against immutable build manifest; not auto-attested)")
    if report["missing_ids"]:
        print("First missing IDs:", ", ".join(report["missing_ids"][:30]))
    return 1 if args.enforce and not report["complete"] else 0


if __name__ == "__main__":
    raise SystemExit(main())
