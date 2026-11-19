#!/usr/bin/env python3
"""Offline development-only registry reader; no runtime or repository writes."""
import argparse
import json
import re
from collections import Counter
from pathlib import Path

ROOT = Path(__file__).resolve().parent
ID = re.compile(r"^[a-z0-9_.-]+:[a-z0-9_./-]+$")
PREY = {"allow", "deny", "hold"}
CAPTURE = {"blacklist_target", "approved_misc_default_deny", "approved_vanilla_exception",
           "native_policy_pending", "review_hold"}

def no_duplicates(pairs):
    result = {}
    for key, value in pairs:
        if key in result:
            raise ValueError(f"duplicate key: {key}")
        result[key] = value
    return result

def read():
    data = json.loads((ROOT / "registry-v1.json").read_text(encoding="utf-8"),
                      object_pairs_hook=no_duplicates)
    if data["schema_version"] != 1 or data["activation"] != "not_runtime_active":
        raise ValueError("unsupported or activated registry")
    entities = data["entities"]
    if len(entities) != data["census"]["entity_count"]:
        raise ValueError("census count mismatch")
    for entity_id, entry in entities.items():
        if not ID.fullmatch(entity_id):
            raise ValueError(f"invalid entity ID: {entity_id}")
        if entry["hunter"]["prey"] not in PREY or entry["capture"]["disposition"] not in CAPTURE:
            raise ValueError(f"unsupported disposition: {entity_id}")
        for field in ("families", "other_categories"):
            items = entry["taxonomy"][field]
            if len(items) != len(set(items)):
                raise ValueError(f"duplicate taxonomy value: {entity_id}")
    return data

def hunter_tags(data):
    def tag(state):
        return {"replace": False, "values": [
            entity_id if entity_id.startswith("minecraft:") else {"id": entity_id, "required": False}
            for entity_id, entry in sorted(data["entities"].items())
            if entry["hunter"]["prey"] == state
        ]}
    return {"hunt_eligible.json": tag("allow"), "hunt_excluded.json": tag("deny")}

def family_tags(data):
    """Type identity, not prey authorization; preserve all working memberships."""
    families = sorted({f for e in data["entities"].values() for f in e["taxonomy"]["families"]})
    return {f.lower() + ".json": {"replace": False, "values": [
        entity_id if entity_id.startswith("minecraft:") else {"id": entity_id, "required": False}
        for entity_id, entry in sorted(data["entities"].items())
        if f in entry["taxonomy"]["families"]
    ]} for f in families}

def projections(data):
    return {**hunter_tags(data), **family_tags(data)}

def compare(data, directory):
    """Read-only drift check against integration donor tags; no generated writes."""
    mismatches = []
    for name, expected in projections(data).items():
        file = directory / name
        if not file.is_file():
            mismatches.append(name + ": missing")
            continue
        actual = json.loads(file.read_text(encoding="utf-8"), object_pairs_hook=no_duplicates)
        if actual != expected:
            mismatches.append(name + ": differs")
    return mismatches

def show(data, entity_id):
    entry = data["entities"][entity_id]
    return {**entry,
            "native_verification": entry.get("native_verification", data["defaults"]["native_verification"]),
            "resources": {**data["defaults"]["resources"], **entry.get("resources", {})},
            "capture": {**data["defaults"]["capture"], **entry["capture"]}}

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("command", choices=["validate", "hunter-tags", "family-tags", "all-tags", "compare-tags", "show", "holds"])
    parser.add_argument("entity_id", nargs="?")
    parser.add_argument("--tag-dir", type=Path)
    args = parser.parse_args()
    data = read()
    entities = data["entities"]
    if args.command == "validate":
        if args.entity_id:
            parser.error("validate takes no entity ID")
        result = {"entities": len(entities),
                  "hunter": dict(Counter(e["hunter"]["prey"] for e in entities.values())),
                  "capture": dict(Counter(e["capture"]["disposition"] for e in entities.values()))}
    elif args.command == "hunter-tags":
        result = hunter_tags(data)
    elif args.command == "family-tags":
        result = family_tags(data)
    elif args.command == "all-tags":
        result = projections(data)
    elif args.command == "compare-tags":
        if args.tag_dir is None:
            parser.error("compare-tags requires --tag-dir")
        mismatches = compare(data, args.tag_dir)
        print(json.dumps({"match": not mismatches, "mismatches": mismatches}, indent=2))
        return 1 if mismatches else 0
    elif args.command == "show":
        if args.entity_id not in entities:
            parser.error("show requires a known exact entity ID")
        result = show(data, args.entity_id)
    else:
        result = {i: e["capture"]["disposition"] for i, e in entities.items()
                  if e["hunter"]["prey"] == "hold"}
    print(json.dumps(result, indent=2, ensure_ascii=False))

if __name__ == "__main__":
    raise SystemExit(main())
