#!/usr/bin/env python3
"""Opt-in, read-only audit of an installed TMM Prism QA client's JAR inventory.

This does NOT install mods or run Minecraft. Invoke from a repository checkout:
    python scripts/audit_client_inventory.py "/path/to/Prism/instance/.minecraft"

The instance contains tmm-qa-expected-mods.json, bundled at package creation.
Build CI and the server smoke continue to perform their own required gates.
"""
from __future__ import annotations

import argparse
import hashlib
import json
from pathlib import Path
import re

ALGORITHMS = {"sha1": 40, "sha256": 64, "sha512": 128}
NAME = re.compile(r"[^/\\:\r\n]+\.jar", re.IGNORECASE)
SOURCE = re.compile(r"[0-9a-f]{40}")


class InventoryError(ValueError):
    """Bad manifest, missing/extra JAR or unexpected downloaded bytes."""


def read_manifest(path: Path) -> tuple[str, dict[str, dict]]:
    if not path.is_file():
        raise InventoryError(f"No packaged client QA manifest: {path}")
    try:
        data = json.loads(path.read_text(encoding="utf-8"))
    except (ValueError, UnicodeError) as exc:
        raise InventoryError("Invalid packaged client QA manifest JSON") from exc
    if not isinstance(data, dict) or data.get("schema") != 1:
        raise InventoryError("Unexpected client QA manifest schema")
    commit = data.get("source_commit")
    if not isinstance(commit, str) or SOURCE.fullmatch(commit) is None:
        raise InventoryError("Unverified immutable source commit in client QA manifest")
    rows = data.get("mods")
    if not isinstance(rows, list) or not rows:
        raise InventoryError("Empty or invalid expected client mod list")
    wanted = {}
    seen = set()
    for row in rows:
        if not isinstance(row, dict):
            raise InventoryError("Invalid client manifest mod row")
        filename = row.get("filename")
        algorithm, value = row.get("algorithm"), row.get("hash")
        side = row.get("side")
        if (not isinstance(filename, str) or NAME.fullmatch(filename) is None
                or filename in (".", "..") or filename.casefold() in seen):
            raise InventoryError(f"Unsafe or duplicate expected JAR: {filename}")
        if (algorithm not in ALGORITHMS or not isinstance(value, str)
                or re.fullmatch("[0-9a-f]{" + str(ALGORITHMS[algorithm]) + "}", value) is None
                or side not in ("both", "client")):
            raise InventoryError(f"Invalid pinned hash or client side: {filename}")
        seen.add(filename.casefold())
        wanted[filename] = row
    return commit, wanted


def audit(instance: Path) -> tuple[str, int]:
    instance = Path(instance)
    commit, wanted = read_manifest(instance / "tmm-qa-expected-mods.json")
    mods = instance / "mods"
    if not mods.is_dir():
        raise InventoryError(f"Missing installed mods directory: {mods}")
    actual = {}
    for path in mods.iterdir():
        if path.is_file() and path.suffix.casefold() == ".jar":
            key = path.name.casefold()
            if key in actual:
                raise InventoryError(f"Case-insensitive duplicate installed JAR: {path.name}")
            actual[key] = path
    selected = {name.casefold(): name for name in wanted}
    missing = sorted(wanted[selected[k]]["filename"] for k in selected.keys() - actual.keys())
    extra = sorted(actual[k].name for k in actual.keys() - selected.keys())
    if missing or extra:
        raise InventoryError(f"Client mod inventory mismatch: missing={missing} unexpected={extra}")
    for filename, row in wanted.items():
        path = actual[filename.casefold()]
        with path.open("rb") as stream:
            observed = hashlib.file_digest(stream, row["algorithm"]).hexdigest()
        if observed != row["hash"]:
            raise InventoryError(f"Client JAR hash mismatch: {filename}")
    return commit, len(wanted)


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("minecraft_dir", type=Path,
                        help="Minecraft game directory containing mods/ and tmm-qa-expected-mods.json")
    args = parser.parse_args()
    try:
        commit, count = audit(args.minecraft_dir)
    except (InventoryError, OSError) as exc:
        parser.exit(1, f"FAIL: {exc}\n")
    print(f"PASS: {count} exact client JARs match packaged SHA hashes "
          f"for immutable source {commit}; no downloads or game launch.")


if __name__ == "__main__":
    main()
