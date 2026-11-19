#!/usr/bin/env python3
"""Reproduce the pinned Reliquary literal-ID config from the live Capturing tag.
Does not launch Minecraft, alter the input config, or invent entity classifications."""
import argparse
import json
import re
import tomllib
from pathlib import Path

TAG = "apothic_spawners:blacklisted_from_spawners"
ID = re.compile(r"[a-z0-9_.-]+:[a-z0-9_/.-]+")
# Match the owner-staged native setting, including a multiline string-only array.
ARRAY = re.compile(r'(?m)^([ \t]*entityBlockList[ \t]*=[ \t]*)\[([^\]]*)\]')

def render(snapshot, source):
    if snapshot.get("schema") != 1 or snapshot.get("tag") != TAG:
        raise ValueError("Not a resolved TMM Capturing snapshot")
    blocked = snapshot.get("blocked_entity_ids")
    if not isinstance(blocked, list) or any(not isinstance(x, str) or not ID.fullmatch(x) for x in blocked):
        raise ValueError("Invalid blocked entity list")
    if len(set(blocked)) != len(blocked):
        raise ValueError("Duplicate blocked entity IDs")
    eligible = snapshot.get("native_eligible_pairs")
    if not isinstance(eligible, dict) or any(not ID.fullmatch(k) or not isinstance(v, str) or not ID.fullmatch(v) for k, v in eligible.items()):
        raise ValueError("Invalid native egg associations")
    if set(blocked) & eligible.keys() or len(set(eligible.values())) != len(eligible):
        raise ValueError("Conflicting eligibility snapshot")
    config = tomllib.loads(source)
    saved = config["items"]["mobCharm"]["entityBlockList"]
    if not isinstance(saved, list) or any(not isinstance(x, str) for x in saved):
        raise ValueError("Invalid original native list")
    # Preserve all unrelated values/comments; verify structural equivalence after editing.
    if len(list(ARRAY.finditer(source))) != 1:
        raise ValueError("Expected exactly one native entityBlockList assignment")
    new_ids = sorted(blocked)
    result = ARRAY.sub(lambda m: m.group(1) + json.dumps(new_ids), source, count=1)
    config["items"]["mobCharm"]["entityBlockList"] = new_ids
    if tomllib.loads(result) != config:
        raise ValueError("Unexpected change outside owner-approved list")
    return result

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--snapshot", type=Path, required=True, help="JSON after TMM_CAPTURING_SNAPSHOT in the authorized live map log")
    parser.add_argument("--config", type=Path, required=True)
    parser.add_argument("--out", type=Path, required=True)
    args = parser.parse_args()
    if args.config.resolve() == args.out.resolve() or args.snapshot.resolve() == args.out.resolve():
        parser.error("Write to a separate reviewable output, not the source")
    data = json.loads(args.snapshot.read_text(encoding="utf-8"))
    result = render(data, args.config.read_text(encoding="utf-8"))
    with args.out.open("x", encoding="utf-8", newline="\n") as output:
        output.write(result)
    print(f"Wrote {len(data['blocked_entity_ids'])} resolved Capturing IDs to {args.out}; review and install before the next authorized launch.")

if __name__ == "__main__":
    main()
