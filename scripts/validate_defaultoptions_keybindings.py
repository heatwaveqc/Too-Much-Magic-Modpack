#!/usr/bin/env python3
"""Static guard for Default Options' observed-key baseline (NOT live roster certification)."""
from __future__ import annotations

import hashlib
import json
import re
from pathlib import Path

from generate_defaultoptions_keybindings import JEI_NATIVE_DEFAULTS

ROOT = Path(__file__).resolve().parents[1]
SNAPSHOT = ROOT / "docs/qa/keybindings/modded-unbound-2aacf348.txt"
CONFIG = ROOT / "config/defaultoptions/keybindings.txt"
VANILLA = ROOT / "docs/qa/keybindings/vanilla-1.21.1-ids.json"
SNAPSHOT_SHA256 = "2af3b812811c7461c7e5fd772068d1f8f4f53261986da9420a584926e94ede11"
RENAMED_MOD_KEYS = {
    "key.tmm_progression.armor": "key.tyr_progression.armor",
    "key.tmm_progression.blink": "key.tyr_progression.blink",
    "key.tmm_progression.glide": "key.tyr_progression.glide",
    "key.tmm_progression.ordeal": "key.tyr_progression.ordeal",
    "key.tmm_progression.treasure": "key.tyr_progression.treasure",
}
LINE = re.compile(r"key_([^:\n]+):key\.keyboard\.unknown:$")


def parse_unbound(path: Path) -> set[str]:
    names = set()
    for number, line in enumerate(path.read_text(encoding="utf-8").splitlines(), 1):
        if not line:
            continue
        match = LINE.fullmatch(line)
        if not match:
            raise ValueError(f"Non-unbound/invalid Default Options mapping {path}:{number}: {line!r}")
        name = match.group(1)
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
    current = parse_unbound(CONFIG)
    if not set(RENAMED_MOD_KEYS) <= original:
        raise ValueError("Observed key snapshot does not contain the expected old Progression key IDs")
    expected = ((original - set(RENAMED_MOD_KEYS)) | set(RENAMED_MOD_KEYS.values())) - JEI_NATIVE_DEFAULTS
    if missing := expected - current:
        raise ValueError(f"Packaged Default Options dropped observed mod keys: {sorted(missing)}")
    if retained := current.intersection(JEI_NATIVE_DEFAULTS):
        raise ValueError(f"JEI native-default exceptions were re-unbound: {sorted(retained)}")
    if unexpected := current - expected:
        raise ValueError(f"Unapproved extra Default Options unbindings: {sorted(unexpected)}")
    ref = json.loads(VANILLA.read_text(encoding="utf-8"))
    if ref.get("minecraft") != "1.21.1" or len(ref.get("ids", [])) != 34:
        raise ValueError("Vanilla 1.21.1 reference is missing or wrong")
    if vanilla_overlap := current.intersection(ref["ids"]):
        raise ValueError(f"Default Options must NOT alter vanilla keys: {sorted(vanilla_overlap)}")
    print(f"PASS: Default Options packages {len(current)} unbound mod-key defaults, excluding {len(JEI_NATIVE_DEFAULTS)} JEI native defaults; no vanilla bindings")
    print("LIMITATION: observed options.txt inventory is not a final all-mods registered-key export")


if __name__ == "__main__":
    validate()
