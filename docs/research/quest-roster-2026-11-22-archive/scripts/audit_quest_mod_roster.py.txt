#!/usr/bin/env python3
"""Type-A audit: enumerate every Packwiz-indexed mod and its quest-coverage class.

Reads source only. Does NOT assemble the pack, download mod JARs or start Minecraft.
Usage: python scripts/audit_quest_mod_roster.py --check | --write
"""
from __future__ import annotations

import argparse
import hashlib
import json
import re
import sys
import tomllib
from collections import Counter
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OUTPUT = Path("manifest/quest_mod_roster_current.json")

# Libraries, framework, optimization and server-admin mods do NOT need quests.
# These are source-catalog classifications, not hard-coded JAR identity claims.
NO_QUEST_CATEGORIES = {
    "dependency", "library", "performance",
    "server_utility", "infrastructure", "compatibility",
}
# Player-facing convenience systems deserve a Tip, not necessarily a whole chapter.
TIP_CATEGORIES = {"qol", "utility"}
EXPLORATION_CATEGORIES = {"worldgen"}
# A specific mod's player-facing nature can override its catalog-wide category.
CONTENT_OVERRIDES = {"bountiful": "chapter_or_section"}
# These appear as QoL in the historical roster but are technical, cosmetic-only,
# admin-only or performance helpers; they impose no quest-content obligation.
NO_QUEST_SLUGS = {
    "attributefix", "clumps", "colorful-hearts", "default-options",
    "ftb-essentials", "get-it-together-drops", "lootjs", "more-darkness",
}


def require(condition: bool, message: str) -> None:
    if not condition:
        raise ValueError(message)


def load_json(path: Path) -> dict:
    return json.loads(path.read_text(encoding="utf-8"))


def load_toml(path: Path) -> dict:
    return tomllib.loads(path.read_text(encoding="utf-8"))


def policy_for(slug: str, category: str) -> str:
    if slug in NO_QUEST_SLUGS:
        return "technical_inventory_only"
    if slug in CONTENT_OVERRIDES:
        return CONTENT_OVERRIDES[slug]
    if category in NO_QUEST_CATEGORIES:
        return "technical_inventory_only"
    if category in TIP_CATEGORIES:
        return "tips_or_section"
    if category in EXPLORATION_CATEGORIES:
        return "exploration_section_or_chapter"
    return "chapter_or_section"


def build_inventory(root: Path, *, verify_metadata: bool = True) -> dict:
    pack = load_toml(root / "pack.toml")
    idx_text = (root / "index.toml").read_bytes()
    idx = tomllib.loads(idx_text.decode("utf-8"))
    roster = load_json(root / "manifest/mods.json")
    chapters = load_json(root / "manifest/quest_chapters.json")

    require(pack.get("index", {}).get("file") == "index.toml", "Wrong Packwiz index path")
    require(pack["index"]["hash-format"] == "sha256", "Unsupported Packwiz index digest")
    require(re.fullmatch(r"[0-9a-f]{64}", pack["index"]["hash"]) is not None,
            "Invalid pack.toml index hash")
    require(hashlib.sha256(idx_text).hexdigest() == pack["index"]["hash"],
            "pack.toml index hash disagrees with actual index.toml")
    require(idx.get("hash-format") == "sha256", "Unsupported metadata index hash")
    require(isinstance(idx.get("files"), list), "Index missing [[files]]")
    rows = idx["files"]
    all_paths = [row.get("file") for row in rows]
    require(all(isinstance(p, str) and p for p in all_paths), "Index contains blank file path")
    require(len(all_paths) == len(set(all_paths)), "Packwiz index duplicate file path")

    selected = [row for row in rows if row["file"].startswith("mods/")
                and row["file"].endswith(".pw.toml")]
    require(selected, "Packwiz contains no mod metadata")
    selected.sort(key=lambda row: row["file"])
    require(all(row.get("metafile") is True for row in selected),
            "Every selected mod must be a Packwiz metafile")
    require(all(re.fullmatch(r"[0-9a-f]{64}", row.get("hash", "")) for row in selected),
            "Missing/invalid mod metafile SHA-256 in Packwiz index")

    records = roster.get("mods", [])
    require(isinstance(records, list), "Malformed manifest/mods.json")
    by_slug: dict[str, dict] = {}
    for record in records:
        slug = record.get("packwiz_slug") or record.get("id")
        require(isinstance(slug, str) and slug and slug not in by_slug,
                f"Duplicate or blank catalog slug: {slug}")
        by_slug[slug] = record

    selected_slugs = {Path(row["file"]).name.removesuffix(".pw.toml") for row in selected}
    enabled_slugs = {slug for slug, record in by_slug.items()
                     if record.get("enabled_in_pack") is True}
    require(selected_slugs == enabled_slugs,
            "Packwiz/catalog enablement mismatch: "
            f"index-only={sorted(selected_slugs - enabled_slugs)} "
            f"roster-only={sorted(enabled_slugs - selected_slugs)}")

    chapters_by_mod: dict[str, set[str]] = {}
    for chapter in chapters["chapters"]:
        for modid in chapter.get("mod_ids", []):
            chapters_by_mod.setdefault(modid, set()).add(chapter["name"])

    result = []
    for row in selected:
        path = row["file"]
        slug = Path(path).name.removesuffix(".pw.toml")
        catalog = by_slug[slug]
        require(isinstance(catalog.get("name"), str) and catalog["name"],
                f"Missing catalog name: {slug}")
        category = catalog.get("category", "unclassified")
        require(isinstance(category, str) and category, f"Missing category: {slug}")
        if verify_metadata:
            metafile = root / path
            require(metafile.is_file(), f"Missing indexed mod metadata: {path}")
            raw = metafile.read_bytes()
            require(hashlib.sha256(raw).hexdigest() == row["hash"],
                    f"Index SHA-256 mismatch: {path}")
            obj = tomllib.loads(raw.decode("utf-8"))
            require(isinstance(obj.get("name"), str) and obj["name"].strip(),
                    f"Invalid Packwiz mod name: {path}")
            require(isinstance(obj.get("filename"), str)
                    and obj["filename"].endswith(".jar"),
                    f"Invalid mod JAR filename: {path}")
            require(obj.get("side", "both") in ("both", "client", "server"),
                    f"Invalid mod side: {path}")
            dl = obj.get("download", {})
            fmt = dl.get("hash-format")
            digest = dl.get("hash", "")
            require(fmt in ("sha1", "sha256", "sha512")
                    and re.fullmatch(r"[0-9a-f]+", digest) is not None
                    and len(digest) == {"sha1": 40, "sha256": 64, "sha512": 128}[fmt],
                    f"Invalid exact mod download hash: {path}")
            require(bool(dl.get("url") or dl.get("mode")),
                    f"Missing mod acquisition route: {path}")

        chapters_here = sorted(chapters_by_mod.get(slug, set()) |
                               chapters_by_mod.get(catalog.get("id"), set()))
        policy = policy_for(slug, category)
        coverage = ("not_required" if policy == "technical_inventory_only"
                    else "mapped" if chapters_here else "needs_coverage_review")
        result.append({
            "slug": slug,
            "name": catalog["name"],
            "packwiz_path": path,
            "packwiz_metadata_sha256": row["hash"],
            "source_category": category,
            "quest_policy": policy,
            "existing_chapters": chapters_here,
            "coverage": coverage,
        })

    pending = [
        {
            "slug": slug,
            "name": record.get("name", slug),
            "source_category": record.get("category", "unclassified"),
            "release_status": record.get("release_status", "unknown"),
            "packwiz_indexed": False,
        }
        for slug, record in sorted(by_slug.items())
        if slug not in selected_slugs
    ]
    policy_counts = dict(sorted(Counter(x["quest_policy"] for x in result).items()))
    coverage_counts = dict(sorted(Counter(x["coverage"] for x in result).items()))
    return {
        "schema_version": 1,
        "evidence_level": "A/source: Packwiz-indexed metadata, not installed JAR or client",
        "sources": {
            "packwiz_index": "index.toml",
            "packwiz_index_sha256": pack["index"]["hash"],
            "packwiz_target_minecraft": pack.get("versions", {}).get("minecraft"),
            "packwiz_target_neoforge": pack.get("versions", {}).get("neoforge"),
            "catalog": "manifest/mods.json",
            "catalog_date": roster.get("date"),
            "quest_chapters": "manifest/quest_chapters.json",
            "scope_freeze": "issue #390 (frozen eligibility universe, not an install roster)",
        },
        "counts": {
            "indexed_third_party_mods": len(result),
            "not_indexed_catalog_entries": len(pending),
            "policy": policy_counts,
            "coverage": coverage_counts,
        },
        "mods": result,
        "not_indexed_catalog_entries": pending,
    }


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    mode = parser.add_mutually_exclusive_group(required=True)
    mode.add_argument("--check", action="store_true")
    mode.add_argument("--write", action="store_true")
    args = parser.parse_args()
    try:
        inventory = build_inventory(ROOT)
        formatted = json.dumps(inventory, ensure_ascii=False, indent=2) + "\n"
        target = ROOT / OUTPUT
        if args.write:
            target.write_text(formatted, encoding="utf-8")
            print(f"WROTE {OUTPUT}: {inventory['counts']['indexed_third_party_mods']} indexed mods")
        else:
            require(target.is_file(), f"Missing generated snapshot {OUTPUT}")
            require(target.read_text(encoding="utf-8") == formatted,
                    f"Stale snapshot {OUTPUT}; rerun --write after source-roster change")
            print("PASS A-QMOD-01: all indexed metafiles, digest hashes and roster membership")
            print("PASS A-QMOD-02: deterministic full current mod list and technical-only classification")
            print("PASS A-QMOD-03: quest coverage queue is explicit; unindexed candidates separate")
            print(json.dumps(inventory["counts"], sort_keys=True))
        return 0
    except (ValueError, KeyError, OSError, tomllib.TOMLDecodeError) as exc:
        print(f"FAIL A-QMOD: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
