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
from urllib.request import Request, urlopen
import sys
import tomllib
from zipfile import ZipFile

from verify_approved_recovery_artifacts import download, jar_metadata
from food_provenance_census import ingredient_tags, resource_type, result_item, recipe_outputs

ROOT = Path(__file__).resolve().parents[1]
CANDIDATES = {
    "huaxia-delight": (1685797, 8829017, "huaxia-delight-1.21.1-1.2.0.jar", "huaxiadelight"),
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
                outputs = recipe_outputs(raw)
                multi = outputs if (isinstance(raw.get("result"), list)
                                    or isinstance(raw.get("results"), list)) else []
                single = outputs[0] if len(outputs) == 1 and not multi else None
                if not outputs:
                    issues.append(info.filename + ": unsupported or ambiguous recipe result")
                ingredients = raw.get("ingredients", raw.get("ingredient", []))
                recipes.append({
                    "recipe": rid, "type": raw.get("type"),
                    "result": single["item"] if single else None,
                    "result_count": single["count"] if single else None,
                    "multi_results": multi,
                    "outputs": outputs,
                    "ingredient_inputs": ingredients,
                    "ingredient_tags": sorted(ingredient_tags(ingredients)),
                    "tool": raw.get("tool"),
                    "station": raw.get("station"),
                    "container": raw.get("container"),
                    "native_conditions": raw.get("neoforge:conditions", []),
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
    outcomes = sorted({output["item"] for r in recipes for output in r["outputs"]})
    return {
        "resource_counts": dict(sorted(counts.items())),
        "recipe_count": len(recipes),
        "cutting_recipe_count": sum("cutting" in str(r["type"]) for r in recipes),
        "recipe_ids_with_single_result": sum(r["result"] is not None for r in recipes),
        "candidate_output_ids_not_confirmed_edible": outcomes,
        "recipes": recipes, "item_tags": tags, "parse_issues": issues[:100],
        "total_parse_issues": len(issues),
    }

def download_checked(slug, pin):
    try:
        return download(pin)
    except RuntimeError:
        if slug != "occult-cooking":
            raise
    # Exact same-file publisher fallback: the author also distributes Occult
    # Cooking on Modrinth. The ordinary Packwiz digest is still mandatory.
    uri = "https://api.modrinth.com/v2/project/occult-cooking/version"
    with urlopen(Request(uri, headers={"User-Agent": "TMM-Food-Source-Census/1.0"}),
                 timeout=40) as response:
        versions = json.load(response)
    for version in versions:
        if (version.get("version_number") != "1.0.1" or
                "1.21.1" not in version.get("game_versions", []) or
                "neoforge" not in version.get("loaders", [])):
            continue
        for file in version.get("files", []):
            if (file.get("filename") != pin["filename"] or
                    not file.get("url", "").startswith("https://cdn.modrinth.com/")):
                continue
            with urlopen(Request(file["url"], headers={
                    "User-Agent": "TMM-Food-Source-Census/1.0"}), timeout=40) as response:
                data = response.read(20 * 1024 * 1024 + 1)
            if len(data) > 20 * 1024 * 1024:
                raise ValueError("Modrinth publisher file exceeds 20MiB limit")
            if file.get("hashes", {}).get("sha1") != hashlib.sha1(data).hexdigest():
                raise ValueError("Official Modrinth file SHA1 does not match")
            return data, file["url"]
    raise RuntimeError("Occult Cooking 1.0.1 exact-name official Modrinth fallback not found")


def audit_one(slug, local_file=None):
    pin, expected_hash, modid = selected_pin(slug)
    if local_file is not None:
        local_file = Path(local_file)
        if local_file.name != pin["filename"]:
            raise ValueError("Local JAR filename differs from approved exact release")
        if local_file.stat().st_size > 105 * 1024 * 1024:
            raise ValueError("Local JAR exceeds audit size bound")
        data, url = local_file.read_bytes(), "user-supplied:" + local_file.name
        provenance = "user_uploaded_source_exact_sha_no_publisher_hash"
    else:
        data, url = download_checked(slug, pin)
        provenance = "publisher_download"
    fmt = pin["download"].get("hash-format")
    matches = (hashlib.new(fmt, data).hexdigest().lower() == expected_hash.lower()
               if expected_hash else None)
    meta = jar_metadata(data)
    found = set(meta.get("mod_ids") or [])
    id_matches = modid is None or modid in found
    evidence = audit_resources(data)
    valid = matches is not False and id_matches and bool(found)
    status = ("verified_user_upload" if provenance.startswith("user_uploaded") and valid
              else "verified" if valid else "held")
    return {
        "status": status, "slug": slug, "source": url,
        "provenance": provenance,
        "publisher_checksum_independently_matched": (
            matches if expected_hash else None),
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
    parser.add_argument("--local-jar", action="append", default=[], metavar="SLUG=PATH",
                        help="Audit a supplied exact-name JAR offline instead of downloading it")
    args = parser.parse_args()
    local = {}
    for pair in args.local_jar:
        slug, sep, name = pair.partition("=")
        if not sep or slug in local or slug not in CANDIDATES and slug not in PRIORITY:
            parser.error("Expected unique approved SLUG=PATH for --local-jar")
        local[slug] = Path(name)
    slugs = ((list(CANDIDATES) if args.scope != "pinned" else []) +
             (list(PRIORITY) if args.scope != "candidates" else []))
    report = {"schema": 1, "scope": "source_only_publisher_jar_census",
              "requested_scope": args.scope, "runtime_map_generated": False,
              "food_coverage_complete": False, "minecraft_booted": False,
              "releases": [], "failures": []}
    for slug in slugs:
        try:
            row = audit_one(slug, local.get(slug))
            report["releases"].append(row)
            print("SOURCE", slug, row["status"], row["filename"], row["sha1"],
                  "recipes", row["source_evidence"]["recipe_count"],
                  "tags", len(row["source_evidence"]["item_tags"]),
                  "native_mods", row["metadata"].get("mod_ids"),
                  "required_deps", row["metadata"].get("required_dependencies"), flush=True)
            if row["status"] not in ("verified", "verified_user_upload"):
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
        "verified": sum(row["status"] in ("verified", "verified_user_upload")
                        for row in report["releases"]),
        "native_recipe_sources": sum(row["source_evidence"]["recipe_count"]
                                     for row in report["releases"]),
        "recipe_output_candidate_ids": len(output_ids),
        "failures": report["failures"],
    }, sort_keys=True), flush=True)
    return 0  # Audit completed; unresolved source holds remain explicit in the evidence

if __name__ == "__main__":
    sys.exit(main())
