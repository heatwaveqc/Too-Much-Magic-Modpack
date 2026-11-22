#!/usr/bin/env python3
"""Read-only official-publisher SHA/metadata probe for three missing approved releases.

Does not mutate packs, install JARs, build, or run Minecraft.
"""
from __future__ import annotations
import hashlib
import json
import sys

from verify_approved_recovery_artifacts import download, jar_metadata

PROBES = {
    "lets-do-foundation": {
        "name": "[Let's Do] Foundation", "filename": "letsdo-foundation-neoforge-1.1.0.jar",
        "file_id": 9735873, "project_id": 1733606,
    },
    "ars-conflux": {
        "name": "Ars Conflux", "filename": "ars-conflux-0.1.56-mc1.21.1-neoforge.jar",
        "file_id": 9505585, "project_id": 1737308,
    },
    "aquamirae-nouveau": {
        "name": "Aquamirae & Nouveau", "filename": "aquamirae-nouveau-0.1.3-mc1.21.1-neoforge.jar",
        "file_id": 9470029, "project_id": 1767657,
    },
}

def main() -> int:
    out = {"official_probes": [], "errors": []}
    for slug, info in PROBES.items():
        try:
            pin = {
                "name": info["name"],
                "filename": info["filename"],
                "update": {"curseforge": {"file-id": info["file_id"], "project-id": info["project_id"]}},
                "download": {},
            }
            data, url = download(pin)
            entry = {"slug": slug, "file_id": info["file_id"],
                     "project_id": info["project_id"], "filename": info["filename"],
                     "source": url, "size": len(data),
                     "sha1": hashlib.sha1(data).hexdigest(),
                     "sha512": hashlib.sha512(data).hexdigest()}
            entry.update(jar_metadata(data))
            out["official_probes"].append(entry)
        except Exception as exc:
            out["errors"].append({"slug": slug, "error": str(exc)})
    print(json.dumps(out, indent=2))
    return 1 if out["errors"] else 0

if __name__ == "__main__":
    sys.exit(main())
