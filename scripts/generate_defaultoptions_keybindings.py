#!/usr/bin/env python3
"""Create Default Options' exhaustive mod-only, unbound initial key defaults.

Uses two actual /defaultoptions saveKeys exports. Does NOT change options.txt.
No client runtime here; never assume a source mod's key ID or scrape defaults.
"""
from __future__ import annotations
import argparse
from pathlib import Path
import re

LINE = re.compile(r"key_([^:\n]+):([^:\n]+)(?::([^\n]*))?$")
MIN_VANILLA = {"key.attack", "key.use", "key.forward", "key.back", "key.jump", "key.inventory"}

def read_keys(path: Path) -> dict[str, str]:
    if not path.is_file():
        raise ValueError(f"Missing export {path}")
    entries = {}
    for num, raw in enumerate(path.read_text(encoding="utf-8-sig").splitlines(), 1):
        if not raw.strip():
            continue
        match = LINE.fullmatch(raw)
        if not match:
            raise ValueError(f"Invalid export entry {path}:{num}: {raw!r}")
        key = match.group(1)
        if key in entries:
            raise ValueError(f"Duplicate mapping {key} in {path}")
        entries[key] = raw
    if not entries:
        raise ValueError(f"Empty key export: {path}")
    return entries

def build(loaded: dict[str, str], vanilla: dict[str, str]) -> str:
    missing = MIN_VANILLA - vanilla.keys()
    if missing:
        raise ValueError(f"Vanilla reference is missing required bindings: {sorted(missing)}")
    absent = vanilla.keys() - loaded.keys()
    if absent:
        raise ValueError(f"Full modded export is missing vanilla controls: {sorted(absent)}")
    modded = loaded.keys() - vanilla.keys()
    if not modded:
        raise ValueError("No modded bindings found; refuse empty output")
    return "".join(f"key_{key}:key.keyboard.unknown:\n" for key in sorted(modded))

def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--loaded", type=Path, required=True)
    parser.add_argument("--vanilla", type=Path, required=True)
    parser.add_argument("--output", type=Path, default=Path("config/defaultoptions/keybindings.txt"))
    mode = parser.add_mutually_exclusive_group()
    mode.add_argument("--write", action="store_true", help="Create output after reviewing actual loaded exports")
    mode.add_argument("--check", action="store_true", help="Require output exactly matches current loaded exports")
    args = parser.parse_args()
    loaded, vanilla = read_keys(args.loaded), read_keys(args.vanilla)
    output = build(loaded, vanilla)
    total = len(loaded) - len(vanilla)
    print(f"Validated {len(vanilla)} vanilla IDs and {total} modded IDs; vanilla will be untouched.")
    if args.check:
        if not args.output.is_file() or args.output.read_text(encoding="utf-8") != output:
            raise SystemExit(f"FAIL: {args.output} absent/stale; regenerate and index in packwiz")
        print(f"PASS: {args.output} covers every exported modded binding")
    elif args.write:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(output, encoding="utf-8")
        print(f"Wrote {args.output}; update packwiz index.toml and pack.toml SHA256 hashes.")
    else:
        print("PREVIEW only (no file changed). Use --write after verifying the exports.")

if __name__ == "__main__":
    main()
