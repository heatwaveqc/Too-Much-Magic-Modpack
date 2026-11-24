#!/usr/bin/env python3
"""Independently approved QA target, indexed metadata and actual installed bytes.
No network, assembly, launch or package creation.
"""
import argparse
import hashlib
import json
import re
import tomllib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ALGORITHMS = {"sha1": 40, "sha256": 64, "sha512": 128}


def read_json(path):
    return json.loads(path.read_text(encoding="utf-8"))


def digest(path, algorithm):
    with path.open("rb") as stream:
        return hashlib.file_digest(stream, algorithm).hexdigest()


def inventory(root=ROOT):
    pack = tomllib.loads((root / "pack.toml").read_text(encoding="utf-8"))
    index_path = root / "index.toml"
    if pack["index"]["file"] != "index.toml" or pack["index"]["hash-format"] != "sha256" or digest(index_path, "sha256") != pack["index"]["hash"]:
        raise ValueError("Packwiz index hash mismatch")
    index = tomllib.loads(index_path.read_text(encoding="utf-8"))
    if index["hash-format"] != "sha256":
        raise ValueError("Unexpected index hash algorithm")
    rows = read_json(root / "manifest/mods.json")["mods"]
    enabled = {m.get("packwiz_slug", m["id"]): m["id"] for m in rows if m["enabled_in_pack"]}
    if len(enabled) != sum(m["enabled_in_pack"] for m in rows):
        raise ValueError("Duplicate enabled slug")
    seen, names, result = set(), set(), []
    for item in index["files"]:
        path = item["file"]
        if path.startswith("resourcepacks/"):
            # Client-only ZIP metafiles are downloaded by Packwiz but are not
            # NeoForge JARs or members of the manifest/mods.json roster.
            if not item.get("metafile") or not path.endswith(".pw.toml"):
                raise ValueError("Unexpected resource pack metadata: " + path)
            continue
        if not path.startswith("mods/") and not item.get("metafile"):
            continue
        match = re.fullmatch(r"mods/([a-zA-Z0-9_.-]+)\.pw\.toml", path)
        if not match or not item.get("metafile"):
            raise ValueError("Unexpected mod metadata path: " + path)
        slug = match[1]
        if slug in seen or slug not in enabled:
            raise ValueError("Duplicate or undeclared mod pin: " + slug)
        seen.add(slug)
        pin_path = root / path
        if not pin_path.is_file() or digest(pin_path, "sha256") != item["hash"]:
            raise ValueError("Missing or stale metadata: " + path)
        pin = tomllib.loads(pin_path.read_text(encoding="utf-8"))
        filename, side = pin["filename"], pin.get("side", "both")
        if not re.fullmatch(r"[^/\\:\r\n]+\.jar", filename, re.I) or filename.casefold() in names:
            raise ValueError("Unsafe or duplicate JAR filename: " + filename)
        names.add(filename.casefold())
        algorithm, hash_value = pin["download"]["hash-format"], pin["download"]["hash"].lower()
        if side not in {"both", "client", "server"} or algorithm not in ALGORITHMS or not re.fullmatch(r"[0-9a-f]{%d}" % ALGORITHMS[algorithm], hash_value):
            raise ValueError("Invalid side or hash: " + slug)
        result.append({"mod_id": enabled[slug], "filename": filename, "side": side,
                       "algorithm": algorithm, "hash": hash_value})
    if seen != set(enabled):
        raise ValueError("Enabled manifest mod missing indexed pin: " + ", ".join(sorted(set(enabled) - seen)))
    return sorted(result, key=lambda row: row["filename"].casefold())


def target_report(root=ROOT, strict=False):
    rows = inventory(root)
    target = read_json(root / "manifest/next-qa-target.json")
    required = target["required_mod_ids"]
    if target["schema"] != 1 or not required or len(required) != len(set(required)):
        raise ValueError("Invalid independently approved QA target")
    declared = {m["id"]: m for m in read_json(root / "manifest/mods.json")["mods"]}
    if any(mid not in declared or not declared[mid].get("desired") for mid in required):
        raise ValueError("Target includes undeclared/unapproved mod")
    active = {x["mod_id"] for x in rows}
    required_modules = set(target["required_first_party_mod_ids"])
    actual_modules = {m["mod_id"] for m in read_json(root / "manifest/first-party-runtime.json")["modules"]}
    report = {"target": len(required), "pinned": len(active), "missing": sorted(set(required) - active),
              "unreviewed_additions": sorted(active - set(required)),
              "missing_first_party": sorted(required_modules - actual_modules),
              "unreviewed_first_party": sorted(actual_modules - required_modules)}
    print(json.dumps(report, indent=2))
    if strict and any(report[k] for k in ("missing", "unreviewed_additions", "missing_first_party", "unreviewed_first_party")):
        raise ValueError("QA inclusion target incomplete; reconcile pins/dependencies or record explicit owner scope change")
    return report


def for_side(rows, side, extras=()):
    result = [dict(row) for row in rows if row["side"] in ("both", side)]
    for path in extras:
        path = Path(path)
        result.append({"mod_id": "first-party", "filename": path.name, "side": "both",
                       "algorithm": "sha256", "hash": digest(path, "sha256")})
    if len({x["filename"].casefold() for x in result}) != len(result):
        raise ValueError("Duplicate selected JAR filename")
    return sorted(result, key=lambda x: x["filename"].casefold())


def verify_directory(directory, rows):
    expected = {x["filename"]: x for x in rows}
    if len(expected) != len(rows) or not expected:
        raise ValueError("Empty or duplicate expected inventory")
    actual = {p.name: p for p in directory.iterdir() if p.is_file() and p.suffix.lower() == ".jar"}
    if set(actual) != set(expected):
        raise ValueError("Installed JAR inventory mismatch: missing=%s unexpected=%s" %
                         (sorted(set(expected) - set(actual)), sorted(set(actual) - set(expected))))
    for name, row in expected.items():
        if digest(actual[name], row["algorithm"]) != row["hash"]:
            raise ValueError("Installed JAR bytes differ: " + name)
    print("PASS: exact installed JAR inventory and hashes (%d)" % len(expected))


def main():
    p = argparse.ArgumentParser()
    p.add_argument("--root", type=Path, default=ROOT)
    p.add_argument("--strict-target", action="store_true")
    p.add_argument("--mods-dir", type=Path)
    p.add_argument("--side", choices=("server", "client"), default="server")
    p.add_argument("--extra", type=Path, action="append", default=[])
    args = p.parse_args()
    target_report(args.root, args.strict_target)
    if args.mods_dir is not None:
        verify_directory(args.mods_dir, for_side(inventory(args.root), args.side, args.extra))


if __name__ == "__main__":
    main()
