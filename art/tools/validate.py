#!/usr/bin/env python3
"""Initial structural guard; does not certify visual, runtime, or inventory coverage."""
import argparse
import hashlib
import json
import struct
import zlib
from pathlib import Path
from inventory import ASSET, dimensions

APPROVED = {"approved", "validated", "game-tested"}
EXCEPTIONS = {"unchanged-approved", "excluded-approved"}
STATES = APPROVED | EXCEPTIONS | {"pending", "drafted", "reviewed", "blocked"}

def png_structure(data):
    dims = dimensions(data)
    p, chunks = 8, []
    while p + 12 <= len(data):
        n = struct.unpack(">I", data[p:p+4])[0]
        end = p + n + 12
        if end > len(data): raise ValueError("truncated chunk")
        kind = data[p+4:p+8]
        expected = struct.unpack(">I", data[end-4:end])[0]
        if zlib.crc32(data[p+4:end-4]) & 0xffffffff != expected:
            raise ValueError("PNG CRC mismatch")
        chunks.append(kind)
        p = end
        if kind == b"IEND": break
    if p != len(data) or not chunks or chunks[0] != b"IHDR" or chunks[-1] != b"IEND" or b"IDAT" not in chunks:
        raise ValueError("missing or malformed PNG chunks")
    return dims

def validate(inv, pack):
    errors, warnings = [], []
    rows = {}
    for row in inv.get("assets", []):
        key = row.get("key", "")
        if not ASSET.fullmatch(key) or ".." in key.split("/") or key in rows:
            errors.append(f"invalid/duplicate registry key: {key}")
            continue
        rows[key] = row
        state = row.get("status")
        if state not in STATES: errors.append(f"{key}: invalid status")
        if state in EXCEPTIONS and not row.get("exception"): errors.append(f"{key}: unexplained exception")
        if state in APPROVED:
            for field in ("target_dimensions", "output_sha256", "provenance", "review", "style_version", "sources"):
                if not row.get(field): errors.append(f"{key}: approved without {field}")
    try:
        if json.loads((pack / "pack.mcmeta").read_text(encoding="utf-8"))["pack"]["pack_format"] != 34:
            errors.append("unexpected resource-pack format")
    except (OSError, ValueError, KeyError): errors.append("invalid pack.mcmeta")
    present = set()
    for p in sorted((pack / "assets").rglob("*.png")):
        key = p.relative_to(pack).as_posix()
        present.add(key)
        row = rows.get(key)
        if row is None:
            errors.append(f"{key}: unregistered production PNG")
            continue
        try:
            data = p.read_bytes()
            dims = png_structure(data)
            digest = hashlib.sha256(data).hexdigest()
            if row.get("target_dimensions") != dims: errors.append(f"{key}: target dimensions mismatch/unset")
            if row.get("output_sha256") != digest: errors.append(f"{key}: output hash mismatch/unset")
            if any(s.get("asset_sha256") == digest for s in row.get("sources", [])):
                errors.append(f"{key}: byte-identical upstream texture")
        except (OSError, ValueError) as exc: errors.append(f"{key}: {exc}")
        if row.get("status") not in APPROVED: warnings.append(f"{key}: not yet approved")
    for key, row in rows.items():
        if row.get("status") in APPROVED and key not in present: errors.append(f"{key}: approved PNG missing")
        if row.get("status") in EXCEPTIONS and key in present: errors.append(f"{key}: exception has unexpected production PNG")
    for path in (pack / "assets").rglob("*.png.mcmeta"):
        if not path.with_suffix("").exists(): errors.append(f"{path}: orphan texture metadata")
        try: json.loads(path.read_text(encoding="utf-8"))
        except (OSError, ValueError): errors.append(f"{path}: malformed metadata")
    if not inv.get("complete") or not rows: warnings.append("Inventory incomplete: whole-pack coverage unknown")
    for w in inv.get("unresolved", []): warnings.append(w)
    return errors, warnings, {"registered": len(rows), "production_png": len(present),
        "approved_rows": sum(r.get("status") in APPROVED for r in rows.values()),
        "game_tested_rows": sum(r.get("status") == "game-tested" for r in rows.values())}

def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--inventory", required=True, type=Path)
    p.add_argument("--pack", required=True, type=Path)
    args = p.parse_args()
    errors, warnings, counts = validate(json.loads(args.inventory.read_text(encoding="utf-8")), args.pack)
    for e in errors: print("ERROR:", e)
    for w in warnings: print("WARN:", w)
    print(json.dumps(counts, sort_keys=True))
    print("Structural check only; visual review and client QA not performed.")
    raise SystemExit(bool(errors))

if __name__ == "__main__":
    main()
