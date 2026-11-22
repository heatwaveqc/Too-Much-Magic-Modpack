#!/usr/bin/env python3
"""Read-only, fail-closed food provenance census for Too Much Magic (#444).

Default mode audits the *indexed packwiz roster*, checked-in datapacks,
KubeJS script inventory, existing Dragon Survival diet selectors and the
nine-item source pilot. Supplying locally acquired, SHA-verified pinned JARs
adds exact recipe/tag/loot resource evidence. A separate same-build registry
export is required before reporting an authoritative edible-item denominator.

This script does not download or copy JARs, boot Minecraft, assign nutrition,
replace recipes or write a live food/diet data map.
"""
from __future__ import annotations

import argparse
from collections import Counter, defaultdict
from hashlib import new as new_hash
import json
from pathlib import Path
import re
import tomllib
from typing import Any
from zipfile import BadZipFile, ZipFile

PRIORITY_PINS = (
    "dragon-survival", "farmers-delight", "aquaculture",
    "aquaculture-delight", "pams-harvestcraft-2-food-core",
    "pams-harvestcraft-2-food-extended", "pams-harvestcraft-2-crops",
    "pams-harvestcraft-2-trees", "my-nethers-delight",
    "nether-depths-upgrade", "brewin-and-chewin", "cocktails-delight",
    "occult-cooking", "lets-do-brewery", "lets-do-herbalbrews",
)
ITEM_ID = re.compile(r"^[a-z0-9_.-]+:[a-z0-9_./-]+$")
MAX_JSON_BYTES = 1024 * 1024


def read_json(path: Path) -> Any:
    return json.loads(path.read_text(encoding="utf-8"))


def pinned_mods(root: Path) -> tuple[list[dict[str, Any]], list[str]]:
    index = tomllib.loads((root / "index.toml").read_text(encoding="utf-8"))
    rows = []
    warnings: list[str] = []
    for entry in index["files"]:
        name = entry["file"]
        if not (name.startswith("mods/") and name.endswith(".pw.toml")
                and entry.get("metafile") is True):
            continue
        path = root / name
        if not path.is_file():
            warnings.append("missing indexed mod metadata: " + name)
            continue
        content = path.read_bytes()
        actual = new_hash(index["hash-format"], content).hexdigest()
        if actual != entry["hash"]:
            warnings.append("indexed metadata digest mismatch: " + name)
        meta = tomllib.loads(content.decode("utf-8"))
        download = meta.get("download", {})
        fmt, digest = download.get("hash-format"), download.get("hash")
        if fmt not in ("sha1", "sha256", "sha512") or not digest:
            warnings.append("missing supported pinned JAR digest: " + name)
        rows.append({
            "pin": Path(name).stem.removesuffix(".pw"),
            "packwiz_metafile": name,
            "filename": meta.get("filename"),
            "download_hash_format": fmt,
            "download_hash": digest,
            "provider": "direct" if "url" in download else download.get("mode", "unknown"),
            "side": meta.get("side"),
        })
    return sorted(rows, key=lambda r: r["pin"]), sorted(set(warnings))


def is_id(value: Any) -> bool:
    return isinstance(value, str) and bool(ITEM_ID.fullmatch(value))


def selector_kind(value: Any) -> str:
    if isinstance(value, str) and value.startswith("#") and is_id(value[1:]):
        return "tag"
    if is_id(value):
        return "literal"
    return "expression_or_unverified"


def legacy_diet(root: Path) -> dict[str, Any]:
    path = root / "kubejs/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json"
    if not path.is_file():
        return {"found": False, "unique_literal_items": [],
                "unverified_selectors": [], "species": {}}
    data = read_json(path)
    species: dict[str, Any] = {}
    literals: set[str] = set()
    unexpected: set[str] = set()
    for form, mapping in sorted(data.get("values", {}).items()):
        entries = mapping.get("neoforge:value", [])
        kinds = Counter()
        for entry in entries:
            value = entry.get("items")
            kind = selector_kind(value)
            kinds[kind] += 1
            if kind == "literal":
                literals.add(value)
            elif kind == "expression_or_unverified":
                unexpected.add(str(value))
        species[form] = {"selectors": len(entries),
                         "literal_selectors": kinds["literal"],
                         "tag_selectors": kinds["tag"],
                         "expression_or_unverified_selectors":
                             kinds["expression_or_unverified"]}
    return {
        "found": True,
        "path": str(path.relative_to(root)),
        "species": species,
        "selector_total": sum(row["selectors"] for row in species.values()),
        "unique_literal_items": sorted(literals),
        "unverified_selectors": sorted(unexpected),
        "warning": "Legacy species permissions, not a current edible registry or new nutrition authority",
    }


def pilot(root: Path) -> dict[str, Any]:
    path = root / "docs/qa/candidates/dragon-food-source-pilot-v1.json"
    if not path.is_file():
        return {"found": False, "items": [], "count": 0}
    data = read_json(path)
    if data.get("schema") != 1 or data.get("scope") != "pilot":
        raise ValueError("Unexpected provenance pilot schema")
    items = sorted(e["item"] for e in data["edibles"])
    if len(items) != len(set(items)) or not all(is_id(i) for i in items):
        raise ValueError("Bad or duplicated source pilot items")
    return {"found": True, "path": str(path.relative_to(root)), "items": items,
            "count": len(items),
            "warning": "Pilot entries are not an authoritative installed edible-item census"}


def resource_type(name: str) -> tuple[str, str] | None:
    # Resources: data/<namespace>/{recipe|recipes|tags/item|tags/items|loot_table|loot_tables}/...json.
    parts = name.split("/")
    if len(parts) < 4 or parts[0] != "data" or not parts[-1].endswith(".json"):
        return None
    ns, folder = parts[1], parts[2]
    rel = "/".join(parts[3:])[:-5]
    if not re.fullmatch(r"[a-z0-9_.-]+", ns):
        return None
    if folder in ("recipe", "recipes"):
        return "recipe", f"{ns}:{rel}"
    if folder in ("loot_table", "loot_tables"):
        return "loot_table", f"{ns}:{rel}"
    if folder == "tags" and len(parts) >= 5 and parts[3] in ("item", "items"):
        return "item_tag", f"{ns}:{'/'.join(parts[4:])[:-5]}"
    return None


def result_item(data: dict[str, Any]) -> tuple[str | None, int | None]:
    # Known Minecraft shaped/shapeless, furnace and compatible mod recipe
    # results. Unknown custom recipe schema stays an unresolved source.
    output = data.get("result")
    if isinstance(output, str):
        return (output, 1) if is_id(output) else (None, None)
    if isinstance(output, dict):
        name = output.get("id", output.get("item"))
        count = output.get("count", 1)
        if is_id(name) and type(count) is int and 0 < count <= 64:
            return name, count
    return None, None


def ingredient_tags(value: Any) -> set[str]:
    found: set[str] = set()
    if isinstance(value, list):
        for v in value:
            found.update(ingredient_tags(v))
    elif isinstance(value, dict):
        tag = value.get("tag")
        if is_id(tag):
            found.add(tag)
        for k, v in value.items():
            if k not in ("tag",):
                found.update(ingredient_tags(v))
    return found


def scan_resource(content: bytes, name: str, origin: str,
                  rows: dict[str, list[dict[str, Any]]],
                  issues: list[str]) -> None:
    identity = resource_type(name)
    if not identity:
        return
    kind, resource_id = identity
    if len(content) > MAX_JSON_BYTES:
        issues.append(f"oversized resource: {origin}/{name}")
        return
    try:
        data = json.loads(content.decode("utf-8"))
        if not isinstance(data, dict):
            raise ValueError("expected a JSON object")
    except (UnicodeError, ValueError) as exc:
        issues.append(f"unparseable {kind}: {origin}/{name} ({type(exc).__name__})")
        return
    row: dict[str, Any] = {"id": resource_id, "origin": origin, "path": name}
    if kind == "recipe":
        output, count = result_item(data)
        row.update({"recipe_type": data.get("type"), "result_item": output,
                    "result_count": count,
                    "ingredient_tag_refs": sorted(ingredient_tags(
                        data.get("ingredients", data.get("ingredient", []))))})
        if output is None:
            issues.append(f"unresolved recipe output/schema: {origin}/{name}")
    elif kind == "item_tag":
        row.update({"replace": data.get("replace", False),
                    "values_count": len(data.get("values", []))
                        if isinstance(data.get("values"), list) else None})
    rows[kind].append(row)


def scan_repo_resources(root: Path, rows: dict[str, list[dict[str, Any]]],
                        issues: list[str]) -> int:
    source = root / "kubejs/data"
    count = 0
    if not source.exists():
        issues.append("missing checked-in kubejs/data source tree")
        return 0
    for path in sorted(source.rglob("*.json")):
        name = str(path.relative_to(root / "kubejs")).replace("\\", "/")
        if resource_type(name):
            count += 1
            scan_resource(path.read_bytes(), name, "checked-in:kubejs/data", rows, issues)
    return count


def scan_jars(pins: list[dict[str, Any]], jar_dir: Path | None,
              rows: dict[str, list[dict[str, Any]]],
              issues: list[str]) -> dict[str, Any]:
    counts = Counter()
    per_pin = {}
    for pin in pins:
        name = pin["filename"]
        status = "not_supplied" if jar_dir is None else "missing_file"
        scanned = Counter()
        if jar_dir is not None and isinstance(name, str):
            path = jar_dir / name
            if path.is_file():
                algorithm = pin["download_hash_format"]
                expected = pin["download_hash"]
                if algorithm not in ("sha1", "sha256", "sha512") or not expected:
                    status = "missing_pin_hash"
                else:
                    digest = new_hash(algorithm)
                    with path.open("rb") as handle:
                        for chunk in iter(lambda: handle.read(1024 * 1024), b""):
                            digest.update(chunk)
                    if digest.hexdigest().lower() != expected.lower():
                        status = "hash_mismatch"
                    else:
                        try:
                            with ZipFile(path) as jar:
                                for info in jar.infolist():
                                    if info.is_dir() or not resource_type(info.filename):
                                        continue
                                    if info.file_size > MAX_JSON_BYTES:
                                        issues.append(f"oversized resource: {pin['pin']}/{info.filename}")
                                        continue
                                    scan_resource(jar.read(info), info.filename,
                                        "pinned-jar:" + pin["pin"], rows, issues)
                                    scanned["resources"] += 1
                            status = "verified_and_scanned"
                        except BadZipFile:
                            status = "invalid_zip"
        counts[status] += 1
        if pin["pin"] in PRIORITY_PINS or status not in ("not_supplied", "missing_file"):
            per_pin[pin["pin"]] = {
                "status": status, "file": name,
                "resource_files_seen": scanned["resources"],
                "sha_pin": pin["download_hash"],
            }
    return {"counts": dict(sorted(counts.items())),
            "priority_and_examined_pins": dict(sorted(per_pin.items()))}


def registry_export(path: Path | None) -> dict[str, Any]:
    if path is None:
        return {"provided": False, "authoritative_edible_denominator": None,
                "reason": "A complete, same-build runtime registry and native-edible export was not provided"}
    data = read_json(path)
    if data.get("schema") != 1 or not re.fullmatch(r"[0-9a-f]{40}", data.get("build_sha", "")):
        raise ValueError("Registry export requires schema=1 and exact 40-hex build_sha")
    if type(data.get("complete")) is not bool or not isinstance(data.get("items"), list):
        raise ValueError("Registry export requires explicit complete boolean and items array")
    ids, edibles = set(), []
    for row in data["items"]:
        ident = row.get("id")
        if not is_id(ident) or ident in ids:
            raise ValueError("Duplicate or invalid registry item ID")
        ids.add(ident)
        if type(row.get("has_food_component")) is not bool or type(row.get("native_only_edible")) is not bool:
            raise ValueError("Every registry item must declare both edible flags")
        if row["has_food_component"] or row["native_only_edible"]:
            edibles.append(ident)
    return {"provided": True, "build_sha": data["build_sha"],
            "complete": data["complete"], "exported_items": len(ids),
            "observed_edible_items": sorted(edibles),
            "authoritative_edible_denominator": len(edibles) if data["complete"] else None}


def census(root: Path, jar_dir: Path | None = None,
           registry: Path | None = None) -> dict[str, Any]:
    pins, metadata_warnings = pinned_mods(root)
    if not pins:
        raise ValueError("No installed Packwiz mod pins were found in index.toml")
    rows: dict[str, list[dict[str, Any]]] = defaultdict(list)
    issues = metadata_warnings[:]
    repo_resource_count = scan_repo_resources(root, rows, issues)
    jar_audit = scan_jars(pins, jar_dir, rows, issues)
    old = legacy_diet(root)
    seed = pilot(root)
    reg = registry_export(registry)
    for values in rows.values():
        values.sort(key=lambda r: (r["id"], r["origin"], r["path"]))

    # Not all recipe outputs are edible; the old DS map includes historical
    # mods and non-food treats. These are leads ONLY, never denominator inputs.
    outputs = sorted({r["result_item"] for r in rows["recipe"]
                      if r["result_item"] is not None})
    literal_ids = old["unique_literal_items"]
    candidates = sorted(set(outputs) | set(literal_ids) | set(seed["items"]))

    routes: dict[str, set[str]] = defaultdict(set)
    for row in rows["recipe"]:
        if row["result_item"] is not None:
            routes[row["result_item"]].add(row["origin"] + "/" + row["id"])
    multi_route_outputs = sorted(k for k, v in routes.items() if len(v) > 1)

    scripts = []
    server_scripts = root / "kubejs/server_scripts"
    if server_scripts.exists():
        scripts = [str(p.relative_to(root)).replace("\\", "/")
                   for p in sorted(server_scripts.rglob("*.js"))]

    priority = []
    all_pins = {p["pin"]: p for p in pins}
    for name in PRIORITY_PINS:
        p = all_pins.get(name)
        priority.append({"pin": name, "indexed": p is not None,
                         "status": jar_audit["priority_and_examined_pins"]
                            .get(name, {}).get("status", "not_indexed"),
                         "filename": p["filename"] if p else None})

    return {
        "schema": 1,
        "scope": "source_only_incomplete_food_provenance_census",
        "runtime_food_map_generated": False,
        "minecraft_booted": False,
        "packwiz": {
            "indexed_mod_pins": len(pins),
            "metadata_warnings": sorted(metadata_warnings),
        },
        "priority_sources": priority,
        "jar_audit": jar_audit,
        "checked_in_resource_files_seen": repo_resource_count,
        "resource_counts": {kind: len(rows[kind]) for kind in
                            ("recipe", "item_tag", "loot_table")},
        "resources": {kind: rows[kind] for kind in ("recipe", "item_tag", "loot_table")},
        "kubejs_server_scripts_not_statically_evaluated": scripts,
        "legacy_dragon_diet": old,
        "source_pilot": seed,
        "registry_export": reg,
        "recipe_result_candidates": outputs,
        "multiple_recipe_route_candidates": multi_route_outputs,
        "candidate_item_ids_not_an_edible_denominator": candidates,
        "unresolved_queue": sorted(set(issues)),
        "coverage_note": (
            "No authoritative edible registry count without an explicitly "
            "complete same-build export. No verified JAR recipe census "
            "without SHA-matched local pinned JARs. Legacy diet selectors, "
            "KubeJS scripts and recipe outputs are only source leads."
        ),
    }


def main() -> None:
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--repo", type=Path, default=Path("."))
    p.add_argument("--jars", type=Path,
                   help="Directory of separately obtained, exactly pinned JAR files (no download)")
    p.add_argument("--registry-export", type=Path,
                   help="Optional same-build exported item registry JSON, schema 1")
    p.add_argument("--output", type=Path, required=True)
    args = p.parse_args()
    report = census(args.repo.resolve(), args.jars, args.registry_export)
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(report, sort_keys=True, indent=2) + "\n",
                           encoding="utf-8")
    print("Source-only food census:", report["packwiz"]["indexed_mod_pins"],
          "indexed mods,", report["checked_in_resource_files_seen"],
          "checked-in recipe/tag/loot resources,",
          len(report["candidate_item_ids_not_an_edible_denominator"]),
          "candidate item IDs (NOT the edible registry count)")
    print("Registry denominator:",
          report["registry_export"]["authoritative_edible_denominator"])
    print("Pinned jar scan:", report["jar_audit"]["counts"],
          "| Minecraft/gameplay NOT run")


if __name__ == "__main__":
    main()
