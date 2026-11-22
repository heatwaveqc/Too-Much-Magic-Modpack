#!/usr/bin/env python3
"""Targeted no-game-boot audit of possible embedded Foundation in existing Let's Do JARs.

Download exactly the named pre-existing publisher JARs and compare installed
Mod IDs/nested JARs against the separately verified Foundation candidate.
"""
from __future__ import annotations
import json
from io import BytesIO
from pathlib import Path
import tomllib
from zipfile import ZipFile

from verify_approved_recovery_artifacts import download, jar_metadata

ROOT = Path(__file__).resolve().parents[1]
PINS = ("lets-do-furniture", "lets-do-beachparty", "lets-do-foundation")

def main() -> int:
    results = []
    native_ids = {}
    for slug in PINS:
        pin = tomllib.loads((ROOT / "mods" / (slug + ".pw.toml")).read_text(encoding="utf-8"))
        data, url = download(pin)
        from hashlib import new
        algorithm = pin["download"]["hash-format"]
        real = new(algorithm, data).hexdigest()
        if real != pin["download"]["hash"].lower():
            raise ValueError(f"{slug}: publisher digest mismatch")
        with ZipFile(BytesIO(data)) as jar:
            names = jar.namelist()
            nested_jars = [name for name in names if name.endswith(".jar")]
            foundation_files = [name for name in names
                                if name.lower().startswith(("dev/letsshare/foundation/", "net/satisfy/foundation/",
                                                             "com/letsdo/foundation/", "com/letsshare/foundation/"))
                                or "/foundation/" in name.lower()]
        info = jar_metadata(data)
        native_ids[slug] = info.get("mod_ids", [])
        results.append({"slug": slug, "publisher": url, "bytes": len(data),
                        "mod_ids": native_ids[slug],
                        "nested_jars": nested_jars[:30],
                        "possible_embedded_foundation_entries": foundation_files[:30],
                        "embedded_foundation_entry_count": len(foundation_files),
                        "required_deps": info.get("required_dependencies", [])})
    seen = {}
    duplicated = []
    for slug, mods in native_ids.items():
        for mod in mods:
            if mod in seen:
                duplicated.append({"mod_id": mod, "in": [seen[mod], slug]})
            seen[mod] = slug
    print(json.dumps({"results": results, "duplicate_native_mod_ids": duplicated}, indent=2))
    if duplicated:
        raise ValueError("Duplicate native mod IDs across Let’s Do files")
    if any(x["nested_jars"] for x in results):
        raise ValueError("Nested JARs found: inspect duplicate dependencies before merge")
    if any(x["possible_embedded_foundation_entries"] for x in results if x["slug"] != "lets-do-foundation"):
        raise ValueError("Possible embedded foundation code: needs manual ABI coexistence review")
    return 0

if __name__ == "__main__":
    raise SystemExit(main())
