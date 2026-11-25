#!/usr/bin/env python3
"""Bounded read-only exact-pin survey; deliberately not a full pack downloader."""
import argparse
import importlib.util
import json
import sys
import tomllib
from collections import Counter
from pathlib import Path

from audit_worldgen_resources import inventory, read_pins

TARGETS = ("primal-magick", "pastel", "irons-spells-n-spellbooks", "ars-nouveau",
           "goety", "hexerei", "the-graveyard", "dungeon-crawl",
           "l_enders-cataclysm", "mowzies-mobs", "aquamirae", "yungs-extras",
           "botania-revived", "extra-botany-revived")

def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--pack-root", type=Path, required=True)
    p.add_argument("--work", type=Path, required=True)
    p.add_argument("--snapshot", required=True)
    a = p.parse_args()
    a.work.mkdir(parents=True, exist_ok=False)
    mods = a.work / "mods"
    mods.mkdir()
    paths = ["mods/" + slug + ".pw.toml" for slug in TARGETS]
    pins = read_pins(a.pack_root, "server", paths)
    if len(pins) != len(paths):
        raise ValueError("Targeted server pin coverage mismatch")
    spec = importlib.util.spec_from_file_location("frozen_downloader", a.pack_root / "scripts/download_mods.py")
    downloader = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(downloader)
    failures = []
    for filename, pin in sorted(pins.items()):
        try:
            mod = tomllib.loads((a.pack_root / pin["pin"]).read_text(encoding="utf-8"))
            (mods / filename).write_bytes(downloader.fetch_verified(mod))
            print("FETCHED " + filename, flush=True)
        except Exception as e:
            failures.append({"filename": filename, "error": str(e)})
            print("UNAVAILABLE " + filename + ": " + str(e), flush=True)
    report = inventory(a.pack_root, mods, pin_paths=paths)
    report["snapshot_label"] = a.snapshot
    report["download_failures"] = failures
    (a.work / "inventory.json").write_text(json.dumps(report, indent=2, sort_keys=True), encoding="utf-8")
    for pin in report["pins"]:
        src = "mods/" + pin["filename"]
        rows = [x for x in report["resources"] if x["source"] == src or x["source"].startswith(src + "!")]
        kinds = Counter(x["kind"] for x in rows)
        templates = sum(x["source"] == src or x["source"].startswith(src + "!") for x in report["templates"])
        print("WORLDGEN_MOD " + json.dumps({"name": pin["name"], "file": pin["filename"],
              "status": pin["status"], "structures": kinds["worldgen/structure"],
              "sets": kinds["worldgen/structure_set"], "templates": templates}), flush=True)
        for row in rows:
            if row["kind"] == "worldgen/structure":
                d = row.get("data", {})
                print("WORLDGEN_STRUCTURE " + json.dumps({"mod": pin["name"], "id": row["id"], "type": d.get("type"), "biomes": d.get("biomes"), "step": d.get("step"), "start_height": d.get("start_height"), "project_start_to_heightmap": d.get("project_start_to_heightmap")}), flush=True)
            if row["kind"] == "worldgen/structure_set":
                print("WORLDGEN_SET " + json.dumps({"mod": pin["name"], "id": row["id"],
                      "placement": row.get("placement"), "structures": row.get("structures"),
                      "attempts_per_million_blocks_before_filters": row.get("attempts_per_million_blocks_before_filters")}), flush=True)
    for error in report["errors"]:
        print("WORLDGEN_ERROR " + json.dumps(error), flush=True)
    print("WORLDGEN_TOTAL " + json.dumps(report["counts"]), flush=True)
    return int(bool(report["errors"] or failures))

if __name__ == "__main__":
    raise SystemExit(main())
