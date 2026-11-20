#!/usr/bin/env python3
"""Static guard for Default Options' observed-key baseline (NOT live roster certification)."""
from __future__ import annotations

import hashlib
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SNAPSHOT = ROOT / "docs/qa/keybindings/modded-unbound-2aacf348.txt"
CONFIG = ROOT / "config/defaultoptions/keybindings.txt"
VANILLA = ROOT / "docs/qa/keybindings/vanilla-1.21.1-ids.json"
SNAPSHOT_SHA256 = "2af3b812811c7461c7e5fd772068d1f8f4f53261986da9420a584926e94ede11"
LINE = re.compile(r"key_([^:\n]+):(key\.keyboard\.unknown|key\.mouse\.middle):$")
SORT_KEY = "key.invtweaks_sort_either.desc"


def parse_unbound(path: Path, *, allow_sort_exception: bool = False) -> set[str]:
    names = set()
    for number, line in enumerate(path.read_text(encoding="utf-8").splitlines(), 1):
        if not line:
            continue
        match = LINE.fullmatch(line)
        if not match:
            raise ValueError(f"Non-unbound/invalid Default Options mapping {path}:{number}: {line!r}")
        name, binding = match.groups()
        expected = "key.mouse.middle" if allow_sort_exception and name == SORT_KEY else "key.keyboard.unknown"
        if binding != expected:
            raise ValueError(f"Unexpected binding {path}:{number}: {name} must be {expected}, got {binding}")
        if name in names:
            raise ValueError(f"Duplicate Default Options key mapping: {name}")
        names.add(name)
    if not names:
        raise ValueError(f"Unbound key mapping list is empty: {path}")
    return names


def validate() -> None:
    if hashlib.sha256(SNAPSHOT.read_bytes()).hexdigest() != SNAPSHOT_SHA256:
        raise ValueError("Observed 2aacf348 key snapshot has changed without review")
    original = parse_unbound(SNAPSHOT)
    if len(original) != 315:
        raise ValueError(f"Expected 315 unique observed modded controls, found {len(original)}")
    current = parse_unbound(CONFIG, allow_sort_exception=True)
    if SORT_KEY not in current:
        raise ValueError("Missing approved middle-click sort binding")
    if missing := original - current:
        raise ValueError(f"Packaged Default Options dropped observed mod keys: {sorted(missing)}")
    ref = json.loads(VANILLA.read_text(encoding="utf-8"))
    if ref.get("minecraft") != "1.21.1" or len(ref.get("ids", [])) != 34:
        raise ValueError("Vanilla 1.21.1 reference is missing or wrong")
    if vanilla_overlap := current.intersection(ref["ids"]):
        raise ValueError(f"Default Options must NOT alter vanilla keys: {sorted(vanilla_overlap)}")
    print(f"PASS: Default Options packages {len(current) - 1} unbound mod-key defaults and one contextual middle-click sort, no vanilla bindings, including all 315 observed IDs")
    print("LIMITATION: observed options.txt inventory is not a final all-mods registered-key export")


if __name__ == "__main__":
    validate()
