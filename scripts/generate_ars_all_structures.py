#!/usr/bin/env python3
"""Generate an Ars Additions Locate Structure recipe for EVERY known active modded structure.

This compiler REQUIRES a verified registry dump for the exact integrated build.
It does not assume that a JAR filename, a configured feature, or a biome is a structure.
Source-only until registry snapshot, generated outputs, packwiz refresh and world tests.
"""
from __future__ import annotations

import argparse
import hashlib
import itertools
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
POLICY_PATH = ROOT / "manifest/ars_structure_locator_policy.json"
ID_RE = re.compile(r"^[a-z0-9_.-]+:[a-z0-9_./-]+$")


def registry_ids(path: Path) -> list[str]:
    data = json.loads(path.read_text(encoding="utf-8"))
    # Explicit contract: JSON array or {"structures": [registered_structure_id, ...]}.
    ids = data["structures"] if isinstance(data, dict) else data
    if not isinstance(ids, list) or not all(isinstance(x, str) and ID_RE.fullmatch(x) for x in ids):
        raise ValueError("Expected a list of exact registered structure IDs in namespace:path form")
    if len(ids) != len(set(ids)):
        raise ValueError("Duplicate IDs in structure registry")
    return sorted(ids)


def generate(ids: list[str], policy: dict) -> dict[str, dict]:
    denied = set(policy["disabled_structures"])
    namespaces = set(policy.get("excluded_namespaces", []))
    selected = [id_ for id_ in ids if id_.split(":", 1)[0] != "minecraft"
                and id_ not in denied and id_.split(":", 1)[0] not in namespaces]
    dyes = policy["address_palette_items"]
    count = int(policy["address_count"])
    tokens = list(itertools.combinations_with_replacement(dyes, count))
    # Cap at choose(16+4-1,4)=3876 targets; fail rather than silently miss anything.
    if len(selected) > len(tokens):
        raise ValueError(f"{len(selected)} targets exceed {len(tokens)} address combinations")
    occupied: set[int] = set()
    out: dict[str, dict] = {}
    for target in selected:
        start = int.from_bytes(hashlib.sha256(target.encode("utf-8")).digest()[:8], "big") % len(tokens)
        slot = next(((start + offset) % len(tokens) for offset in range(len(tokens))
                     if (start + offset) % len(tokens) not in occupied), None)
        if slot is None:
            raise AssertionError("No free deterministic address")
        occupied.add(slot)
        ns, path = target.split(":", 1)
        recipe_path = f"{policy['recipe_subdirectory']}/{ns}/{path}.json"
        augments = [{"key": x} for x in policy["address_marker_items"] + list(tokens[slot])]
        out[recipe_path] = {
            "type": "ars_additions:locate_structure",
            "id": f"tmm:locate_{ns}/{path}",
            "augments": augments,
            "structure": {"key": target},
        }
    return out


def run(registry: Path, write: bool) -> dict[str, dict]:
    policy = json.loads(POLICY_PATH.read_text(encoding="utf-8"))
    results = generate(registry_ids(registry), policy)
    base = ROOT / policy["recipe_subdirectory"]
    if write:
        base.mkdir(parents=True, exist_ok=True)
        # This directory is compiler-owned. Remove stale auto-recipes only.
        for old in base.rglob("*.json"):
            old.unlink()
        for path, value in sorted(results.items()):
            dest = ROOT / path
            dest.parent.mkdir(parents=True, exist_ok=True)
            dest.write_text(json.dumps(value, indent=2) + "\n", encoding="utf-8")
    return results


def self_test() -> None:
    policy = json.loads(POLICY_PATH.read_text(encoding="utf-8"))
    ids = ["minecraft:stronghold", "minecraft:fortress", "ars_additions:arcane_library",
           "ars_additions:nexus_tower", "test:disabled_structure"]
    case = dict(policy, disabled_structures=policy["disabled_structures"] + ["test:disabled_structure"])
    recipes = generate(ids, case)
    assert len(recipes) == 2
    assert {v["structure"]["key"] for v in recipes.values()} == {
        "ars_additions:arcane_library", "ars_additions:nexus_tower"}
    assert all(len(v["augments"]) == 6 for v in recipes.values())
    assert recipes == generate(list(reversed(ids)), case)
    assert len({tuple(x["key"] for x in v["augments"]) for v in recipes.values()}) == 2
    print("PASS: all active modded IDs, unique addresses, disabled IDs filtered, stable output")


def main() -> None:
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--registry", type=Path, help="Authoritative JSON export of loaded structure registry")
    ap.add_argument("--write", action="store_true", help="Replace compiler-owned recipe JSON files; packwiz refresh REQUIRED afterwards")
    ap.add_argument("--self-test", action="store_true")
    ns = ap.parse_args()
    if ns.self_test:
        self_test()
        return
    if ns.registry is None:
        ap.error("--registry is required for generation (no invented IDs)")
    recipes = run(ns.registry, ns.write)
    print(f"Generated {len(recipes)} targeted Locate Structure recipes" if ns.write
          else f"Dry-run: {len(recipes)} targeted Locate Structure recipes")
    if ns.write:
        print("NEXT: run packwiz refresh, add index and pin hashes, validate with a disposable server and client")


if __name__ == "__main__":
    main()
