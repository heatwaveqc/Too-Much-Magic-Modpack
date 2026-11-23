#!/usr/bin/env python3
"""Exact publisher-JAR source census for approved TMM foods (#444/#439).

No pack assembly, binary redistribution, Minecraft boot or inferred edible registry.
Publisher downloads stay in memory and only JSON evidence is uploaded.
"""
from __future__ import annotations
import argparse
from collections import Counter
import hashlib
from io import BytesIO
import json
from pathlib import Path
import sys
import tomllib
from zipfile import ZipFile

from verify_approved_recovery_artifacts import download, jar_metadata
from food_provenance_census import ingredient_tags, resource_type, result_item

ROOT = Path(__file__).resolve().parents[1]
CANDIDATES = {
    "huaxia-delight": (1685797, 8829017, "huaxia-delight-1.21.1-1.2.0.jar", None),
    "crispy": (1206986, 6447126, "crispy-1.1.jar", "crispy"),
    # Publisher advertises release 1.5.3 but the exact hosted filename ends 1.5.2.
    "dungeons-delight": (1258923, 9614702, "neoforge-dungeonsdelight-1.21.1-1.5.2.jar", "dungeonsdelight"),
    "enders-delight": (827163, 8570814, "endersdelight-1.3.1.jar", "endersdelight"),
    "runiclib": (880879, 8188562, "neoforge-runiclib-1.21.1-5.0.7.jar", "runiclib"),
}
PRIORITY = (
    "farmers-delight", "aquaculture", "aquaculture-delight",
    "pams-harvestcraft-2-food-core", "pams-harvestcraft-2-food-extended",
    "pams-harvestcraft-2-crops", "pams-harvestcraft-2-trees",
    "my-nethers-delight", "nether-depths-upgrade",
    "dragon-survival", "brewin-and-chewin", "cocktails-delight",
    "occult-cooking", "lets-do-brewery", "lets-do-herbalbrews",
)

def selected_pin(slug):
    if slug in CANDIDATES:
        project, fid, filename, modid = CANDIDATES[slug]
        return ({"name": slug, "filename": filename, "side": "both",
                 "download": {}, "update": {"curseforge": {"file-id": fid, "project-id": project}}},
                None, modid)
    pin = tomllib.loads((ROOT / "mods" / (slug + ".pw.toml")).read_text(encoding="utf-8"))
    return pin, pin["download"]["hash"], None

def audit_resources(data):
    counts = Counter()
    recipes, tags, issues = [], [], []
    with ZipFile(BytesIO(data)) as jar:
        bad = jar.testzip()
        if bad:
            raise ValueError("ZIP CRC mismatch " + bad)
        for info in jar.infolist():
            identity = resource_type(info.filename)
            if not identity or info.is_dir():
                continue
            kind, rid = identity
            counts[kind] += 1
            if info.file_size > 1_048_576:
                issues.append(info.filename + ": JSON exceeds 1MiB")
                continue
            try:
                raw = json.loads(jar.read(info).decode("utf-8"))
                if not isinstance(raw, dict):
                    raise ValueError("expected JSON object")
            except (ValueError, UnicodeError) as exc:
                issues.append(info.filename + ": " + str(exc)[:80])
                continue
            if kind == "recipe":
                item, amount = result_item(raw)
                multi = []
                if isinstance(raw.get("results"), list):
                    for candidate in raw["results"]:
                        if isinstance(candidate, dict):
                            out, number = result_item({"result": candidate})
                            if out:
                                multi.append({"item": out, "count": number})
                recipes.append({
                    "recipe": rid, "type": raw.get("type"),
                    "result": item, "result_count": amount,
                    "multi_results": multi,
                    "ingredient_tags": sorted(ingredient_tags(raw.get(
                        "ingredients", raw.get("ingredient", [])))),
                    "path": info.filename,
                })
            elif kind == "item_tag":
                values = raw.get("values")
                if isinstance(values, list):
                    tags.append({"tag": rid, "values": values,
                                 "replace": raw.get("replace", False), "path": info.filename})
                else:
                    issues.append(info.filename + ": unresolved tag values")
    recipes.sort(key=lambda x: x["recipe"])
    tags.sort(key=lambda x: x["tag"])
    outcomes = sorted({s for r in recipes for s in
                       ([r["result"]] if r["result"] else []) +
                       [m["item"] for m in r["multi_results"]]})
    return {
        "resource_counts": dict(sorted(counts.items())),
        "recipe_count": len(recipes),
        "cutting_recipe_count": sum("cutting" in str(r["type"]) for r in recipes),
        "recipe_ids_with_single_result": sum(r["result"] is not None for r in recipes),
        "candidate_output_ids_not_confirmed_edible": outcomes,
        "recipes": recipes, "item_tags": tags, "parse_issues": issues[:100],
        "total_parse_issues": len(issues),
    }

def audit_one(slug):
    pin, expected_hash, modid = selected_pin(slug)
    data, url = download(pin)
    fmt = pin["download"].get("hash-format")
    matches = (hashlib.new(fmt, data).hexdigest().lower() == expected_hash.lower()
               if expected_hash else None)
    meta = jar_metadata(data)
    found = set(meta.get("mod_ids") or [])
    id_matches = modid is None or modid in found
    evidence = audit_resources(data)
    status = ("verified" if (matches is not False and id_matches and
                           isinstance(meta.get("mod_ids"), list) and found) else "held")
    return {
        "status": status, "slug": slug, "source": url,
        "publisher_file_id": pin.get("update", {}).get("curseforge", {}).get("file-id"),
        "filename": pin["filename"], "size_bytes": len(data),
        "sha1": hashlib.sha1(data).hexdigest(),
        "sha512": hashlib.sha512(data).hexdigest(),
        "existing_indexed_hash_matches": matches,
        "native_expected_mod_id_matches": id_matches,
        "metadata": meta, "source_evidence": evidence,
    }

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--scope", choices=("candidates", "pinned", "all"), default="all")
    parser.add_argument("--json-out", required=True)
    args = parser.parse_args()
    slugs = ((list(CANDIDATES) if args.scope != "pinned" else []) +
             (list(PRIORITY) if args.scope != "candidates" else []))
    report = {"schema": 1, "scope": "source_only_publisher_jar_census",
              "requested_scope": args.scope, "runtime_map_generated": False,
              "food_coverage_complete": False, "minecraft_booted": False,
              "releases": [], "failures": []}
    for slug in slugs:
        try:
            row = audit_one(slug)
            report["releases"].append(row)
            print("SOURCE", slug, row["status"], row["filename"], row["sha1"],
                  "recipes", row["source_evidence"]["recipe_count"],
                  "tags", len(row["source_evidence"]["item_tags"]),
                  "native_mods", row["metadata"].get("mod_ids"), flush=True)
            if row["status"] != "verified":
                report["failures"].append({"slug": slug, "error": "pinned hash or native mod ID mismatch"})
        except Exception as exc:
            report["failures"].append({"slug": slug, "error": str(exc)[:1000]})
            print("SOURCE", slug, "UNAVAILABLE", str(exc)[:180], flush=True)
    Path(args.json_out).write_text(json.dumps(report, sort_keys=True, indent=2) + "\n",
                                   encoding="utf-8")
    output_ids = {o for row in report["releases"] for o in
                  row["source_evidence"]["candidate_output_ids_not_confirmed_edible"]}
    print("SUMMARY", json.dumps({
        "audited": len(report["releases"]),
        "verified": sum(row["status"] == "verified" for row in report["releases"]),
        "native_recipe_sources": sum(row["source_evidence"]["recipe_count"]
                                     for row in report["releases"]),
        "recipe_output_candidate_ids": len(output_ids),
        "failures": report["failures"],
    }, sort_keys=True), flush=True)
    return 1 if report["failures"] else 0

if __name__ == "__main__":
    sys.exit(main())
