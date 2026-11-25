#!/usr/bin/env python3
"""Offline inventory of raw worldgen resources; never loads a game or edits inputs."""
import argparse
import hashlib
import io
import json
import re
import tomllib
import zipfile
from collections import Counter, defaultdict
from pathlib import Path

RESOURCE = re.compile(r"^data/([^/]+)/(worldgen/(?:structure_set|structure)|tags/worldgen/(?:structure|biome)|neoforge/biome_modifier)/(.+)\.json$")
TEMPLATE = re.compile(r"^data/([^/]+)/structures?/(.+)\.nbt$")
FEATURE = re.compile(r"^data/([^/]+)/worldgen/(configured_feature|placed_feature)/(.+)\.json$")
MAX_JSON = 8 * 1024 * 1024
MAX_NESTED = 256 * 1024 * 1024

def digest(path, algorithm):
    h = hashlib.new(algorithm)
    with path.open("rb") as f:
        for block in iter(lambda: f.read(1024 * 1024), b""):
            h.update(block)
    return h.hexdigest()

def read_pins(root, side, pin_paths=None):
    requested = set(pin_paths) if pin_paths is not None else None
    found = set()
    index = tomllib.loads((root / "index.toml").read_text(encoding="utf-8"))
    result = {}
    for row in index["files"]:
        name = row["file"]
        if not (name.startswith("mods/") and name.endswith(".pw.toml")):
            continue
        if requested is not None and name not in requested:
            continue
        found.add(name)
        path = root / name
        if digest(path, index["hash-format"]) != row["hash"].lower():
            raise ValueError(f"Index hash mismatch: {name}")
        pin = tomllib.loads(path.read_text(encoding="utf-8"))
        if pin.get("side", "both") not in ("both", side):
            continue
        filename = pin["filename"]
        if filename in result or Path(filename).name != filename:
            raise ValueError(f"Duplicate or unsafe pin filename: {filename}")
        result[filename] = {"pin": name, "name": pin["name"], "download": pin["download"]}
    if requested is not None and requested != found:
        raise ValueError(f"Unknown requested pins: {sorted(requested - found)}")
    return result

def inventory(pack_root, mods_dir, side="server", extras=(), data_roots=(), pin_paths=None):
    pins = read_pins(pack_root, side, pin_paths)
    report = {"schema": 1, "side": side, "classification": "raw candidates, not effective registry",
              "pin_scope": sorted(pin_paths) if pin_paths is not None else "all-side-pins",
              "pins": [], "archives": [], "resources": [], "templates": [],
              "feature_candidates": [], "errors": [], "limitations": [
                  "Duplicate definitions are retained, not resolved by guessed pack precedence.",
                  "Runtime registrations, custom placement and configuration overrides require separate inspection.",
                  "Feature candidates and templates are not counts of actual structures. No biome/dimension or visibility inference.",
                  "Vanilla resources and first-party archives must be supplied explicitly or present in mods directory."
              ]}
    errors = report["errors"]

    def record(source, name, size, read):
        match = RESOURCE.fullmatch(name)
        if match:
            ns, kind, key = match.groups()
            row = {"source": source, "path": name, "kind": kind, "id": ns + ":" + key}
            try:
                if size > MAX_JSON:
                    raise ValueError("JSON exceeds size guard")
                body = json.loads(read())
                if not isinstance(body, dict):
                    raise ValueError("expected JSON object")
                row["data"] = body
                if kind == "worldgen/structure_set":
                    placement = body.get("placement", {})
                    row["placement"] = placement
                    row["structures"] = body.get("structures", [])
                    if isinstance(placement, dict) and placement.get("type") in ("minecraft:random_spread", "random_spread"):
                        spacing = placement.get("spacing")
                        if type(spacing) is int and spacing > 0:
                            row["attempts_per_million_blocks_before_filters"] = 1_000_000 / (16 * spacing) ** 2
            except (ValueError, TypeError, OSError, RuntimeError) as e:
                errors.append({"source": source, "path": name, "error": str(e)})
            report["resources"].append(row)
        elif TEMPLATE.fullmatch(name):
            ns, key = TEMPLATE.fullmatch(name).groups()
            report["templates"].append({"source": source, "path": name, "id": ns + ":" + key})
        elif FEATURE.fullmatch(name):
            ns, kind, key = FEATURE.fullmatch(name).groups()
            report["feature_candidates"].append({"source": source, "kind": kind, "id": ns + ":" + key, "path": name})

    def scan_zip(z, source, depth=0):
        seen = set()
        for entry in sorted(z.infolist(), key=lambda e: e.filename):
            name = entry.filename
            if name in seen:
                errors.append({"source": source, "path": name, "error": "duplicate archive member"})
            seen.add(name)
            if entry.is_dir():
                continue
            record(source, name, entry.file_size, lambda: z.read(entry))
            if name.startswith("META-INF/jarjar/") and name.endswith(".jar"):
                if depth >= 3 or entry.file_size > MAX_NESTED:
                    errors.append({"source": source, "path": name, "error": "nested archive exceeds guard"})
                    continue
                try:
                    with zipfile.ZipFile(io.BytesIO(z.read(entry))) as nested:
                        scan_zip(nested, source + "!" + name, depth + 1)
                except (zipfile.BadZipFile, OSError, RuntimeError) as e:
                    errors.append({"source": source, "path": name, "error": str(e)})

    if not mods_dir.is_dir():
        raise ValueError("mods directory does not exist")
    installed = {p.name: p for p in mods_dir.glob("*.jar") if p.is_file()}
    for filename, pin in sorted(pins.items()):
        path = installed.get(filename)
        row = {"filename": filename, "pin": pin["pin"], "name": pin["name"]}
        if path is None:
            row["status"] = "missing"
            errors.append({"source": filename, "error": "missing pinned archive"})
        else:
            d = pin["download"]
            try:
                row["status"] = "verified" if digest(path, d["hash-format"]) == d["hash"].lower() else "hash-mismatch"
            except (OSError, ValueError, KeyError) as e:
                row["status"] = "verification-error"
                errors.append({"source": filename, "error": str(e)})
            if row["status"] != "verified":
                errors.append({"source": filename, "error": row["status"]})
        report["pins"].append(row)

    archives = [(p, "mods/" + name, "pinned" if name in pins else "unindexed") for name, p in sorted(installed.items())]
    archives += [(p, "extra/" + str(i) + "/" + p.name, "explicit-extra") for i, p in enumerate(extras)]
    for path, source, origin in archives:
        try:
            report["archives"].append({"source": source, "origin": origin, "sha256": digest(path, "sha256")})
            with zipfile.ZipFile(path) as z:
                scan_zip(z, source)
        except (OSError, zipfile.BadZipFile, RuntimeError) as e:
            errors.append({"source": source, "error": str(e)})

    for i, root in enumerate(data_roots):
        if not root.is_dir():
            errors.append({"source": str(root), "error": "missing data root"})
            continue
        for p in sorted((root / "data").rglob("*")):
            if p.is_file():
                name = p.relative_to(root).as_posix()
                record("data-root/" + str(i), name, p.stat().st_size, p.read_bytes)
    grouped = defaultdict(list)
    for row in report["resources"]:
        grouped[(row["kind"], row["id"])].append(row["source"])
    report["duplicate_definitions"] = [
        {"kind": k, "id": rid, "sources": sources}
        for (k, rid), sources in sorted(grouped.items()) if len(sources) > 1
    ]
    report["counts"] = {
        "pins": dict(Counter(row["status"] for row in report["pins"])),
        "raw_resources": dict(Counter(row["kind"] for row in report["resources"])),
        "distinct_resource_ids": dict(Counter(k for k, rid in grouped)),
        "archives": len(report["archives"]), "errors": len(errors)
    }
    return report

def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--pack-root", required=True, type=Path)
    p.add_argument("--mods-dir", required=True, type=Path)
    p.add_argument("--side", choices=("client", "server"), default="server")
    p.add_argument("--extra-jar", action="append", type=Path, default=[])
    p.add_argument("--data-root", action="append", type=Path, default=[])
    p.add_argument("--pin", action="append", help="Explicit partial pin scope; default is all side-eligible pins")
    p.add_argument("--snapshot", required=True, help="Exact audited build SHA or other explicitly identified snapshot")
    p.add_argument("--output", required=True, type=Path)
    args = p.parse_args()
    report = inventory(args.pack_root, args.mods_dir, args.side, args.extra_jar, args.data_root, args.pin)
    report["snapshot_label"] = args.snapshot
    args.output.parent.mkdir(parents=True, exist_ok=True)
    with args.output.open("x", encoding="utf-8") as f:
        json.dump(report, f, ensure_ascii=False, indent=2, sort_keys=True)
        f.write("\n")
    print(json.dumps(report["counts"], sort_keys=True))
    return int(bool(report["errors"]))

if __name__ == "__main__":
    raise SystemExit(main())
