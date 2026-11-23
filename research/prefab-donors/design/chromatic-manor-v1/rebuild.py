#!/usr/bin/env python3
"""Restore exact source and check structure geometry independent of gzip metadata."""
import gzip, hashlib, json, pathlib, subprocess, sys

p = pathlib.Path(__file__).resolve().parent
src = gzip.decompress((p / "build_manor.py.gz").read_bytes())
source_sha256 = "5b92122a1cb6ad25a9a4ad4bb67f2b4faf865e70b77fcd6375f1411b82fe942f"
assert hashlib.sha256(src).hexdigest() == source_sha256, "Source hash mismatch"
f = p / "build_manor.py"
f.write_bytes(src)
subprocess.run([sys.executable, str(f)], check=True)
nbt = p / "prefab-extension" / "chromatic_manor.nbt"
compressed = nbt.read_bytes()
raw = gzip.decompress(compressed)
assert raw.startswith(b"\x0a\x00\x00"), "Not a vanilla root Compound NBT"
audit = json.loads((p / "audit.json").read_text(encoding="utf-8"))
reference = json.loads((p / "static-audit.json").read_text(encoding="utf-8"))
for key in ("size", "ground_anchor_y", "nonair",
            "explicit_air", "palette_states", "rooms"):
    assert audit.get(key) == reference.get(key), f"Geometry mismatch: {key}"
assert audit["size"][0] * audit["size"][1] * audit["size"][2] == reference["bbox_volume"], "Volume mismatch"
compressed_sha256 = hashlib.sha256(compressed).hexdigest()
raw_sha256 = hashlib.sha256(raw).hexdigest()
if compressed_sha256 != reference["nbt_sha256"]:
    print("gzip output differs from 2026-11-18 compressed reference; "
          "exact generator SHA256 and geometry checked. "
          f"compressed_sha256={compressed_sha256}; raw_sha256={raw_sha256}")
print(f"Chromatic Manor V1 static QA geometry verified: {nbt} raw_sha256={raw_sha256}")
