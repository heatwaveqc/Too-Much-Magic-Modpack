#!/usr/bin/env python3
"""Fail-closed FTB Quest item-ID verification against the SAME BUILD's live item registry.

prepare: scan staged quest chapters and generate a temporary, CI-only KubeJS probe.
verify: require the exact probe result from the real dedicated-server smoke log.
This does not use guessed IDs, downloaded JAR source, or an old registry snapshot.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import re
import sys
import tempfile
from pathlib import Path

# FTB Quests' current item fields are "icon: { id: ... }" and
# "item: { id: ... }"; some generated variants use a direct ID string.
FIELD = re.compile(r"\b(icon|item)\s*:\s*")
ITEM = re.compile(r'\b(icon|item)\s*:\s*(?:\{\s*id\s*:\s*)?"([^"]+)"')
RESOURCE_ID = re.compile(r"^[a-z0-9_.-]+:[a-z0-9_./-]+$")
MISSING = re.compile(r"TMM_QUEST_ITEM_MISSING=([a-z0-9_.:/-]+)")
SUMMARY = re.compile(
    r"TMM_QUEST_ITEM_SUMMARY sha256=([0-9a-f]{64}) items=(\d+) missing=(\d+)"
)


def collect(chapters_dir: Path) -> dict[str, list[str]]:
    chapters = sorted(chapters_dir.glob("*.snbt"))
    if not chapters:
        raise ValueError(f"No generated FTB Quest chapters at {chapters_dir}")
    locations: dict[str, list[str]] = {}
    for path in chapters:
        source = path.read_text(encoding="utf-8")
        for field in FIELD.finditer(source):
            match = ITEM.match(source, field.start())
            line = source.count("\n", 0, field.start()) + 1
            if match is None:
                raise ValueError(
                    f"Unsupported FTB Quest {field.group(1)} reference at {path}:{line}; "
                    "extend the scanner instead of silently skipping it"
                )
            item_id = match.group(2)
            if not RESOURCE_ID.fullmatch(item_id) or item_id == "minecraft:air":
                raise ValueError(f"Invalid item registry ID {item_id!r} at {path}:{line}")
            locations.setdefault(item_id, []).append(
                f"{path.name}:{line}:{field.group(1)}"
            )
    if not locations:
        raise ValueError("No quest item tasks, rewards or icons found")
    return locations


def digest(ids: list[str]) -> str:
    return hashlib.sha256(("\n".join(ids) + "\n").encode("utf-8")).hexdigest()


def make_probe(ids: list[str], sha: str) -> str:
    # Packaged only into the disposable CI smoke server, never the player pack.
    return (
        "// GENERATED QA-ONLY file; do not add to the pack or a launcher ZIP.\n"
        "// Exact staged FTB Quest task/reward/icon item references.\n"
        "const TMM_QA_QUEST_ITEM_IDS = " + json.dumps(ids, separators=(",", ":")) + ";\n"
        "ServerEvents.loaded(event => {\n"
        "  const Registries = Java.loadClass('net.minecraft.core.registries.BuiltInRegistries');\n"
        "  const ResourceLocation = Java.loadClass('net.minecraft.resources.ResourceLocation');\n"
        "  let missing = 0;\n"
        "  for (const id of TMM_QA_QUEST_ITEM_IDS) {\n"
        "    if (!Registries.ITEM.containsKey(ResourceLocation.parse(id))) {\n"
        "      missing++;\n"
        "      console.error('TMM_QUEST_ITEM_MISSING=' + id);\n"
        "    }\n"
        "  }\n"
        "  console.info('TMM_QUEST_ITEM_SUMMARY sha256=" + sha + " items=' + "
        "TMM_QA_QUEST_ITEM_IDS.length + ' missing=' + missing);\n"
        "});\n"
    )


def prepare(chapters_dir: Path, manifest_path: Path, probe_path: Path) -> dict:
    locations = collect(chapters_dir)
    ids = sorted(locations)
    sha = digest(ids)
    manifest = {"schema": 1, "sha256": sha, "items": ids, "references": locations}
    manifest_path.parent.mkdir(parents=True, exist_ok=True)
    probe_path.parent.mkdir(parents=True, exist_ok=True)
    manifest_path.write_text(
        json.dumps(manifest, indent=2, sort_keys=True) + "\n", encoding="utf-8"
    )
    probe_path.write_text(make_probe(ids, sha), encoding="utf-8")
    print(
        f"PREPARED: {len(ids)} distinct quest item IDs, "
        f"{sum(map(len, locations.values()))} task/reward/icon references; digest={sha}"
    )
    return manifest


def verify(manifest: dict, log: str) -> None:
    ids = manifest["items"]
    if manifest.get("schema") != 1 or ids != sorted(set(ids)):
        raise ValueError("Invalid, noncanonical quest registry manifest")
    if not ids or manifest["sha256"] != digest(ids):
        raise ValueError("Missing/incorrect quest registry manifest checksum")
    summaries = SUMMARY.findall(log)
    if len(summaries) != 1:
        raise ValueError(
            f"Expected exactly one live item registry summary in the server log; "
            f"got {len(summaries)}. A clean server boot alone is NOT a pass."
        )
    sha, total, count = summaries[0]
    if sha != manifest["sha256"] or int(total) != len(ids):
        raise ValueError("Server checked a different quest source or incomplete item list")
    misses = MISSING.findall(log)
    if len(misses) != int(count) or len(set(misses)) != len(misses):
        raise ValueError("Server item registry error lines do not match summary")
    unknown = set(misses) - set(ids)
    if unknown:
        raise ValueError(f"Unrecognized registry failures: {sorted(unknown)}")
    if misses:
        details = []
        for item in misses:
            refs = ", ".join(manifest["references"].get(item, []))
            details.append(f"  {item}: {refs}")
        raise ValueError(
            "Quest items absent from the exact build's live item registry:\n"
            + "\n".join(details)
        )
    print(f"PASS: {len(ids)} quest item IDs exist in the loaded item registry")


def selftest() -> None:
    with tempfile.TemporaryDirectory() as tmp:
        root = Path(tmp)
        chapter_dir = root / "chapters"
        chapter_dir.mkdir()
        (chapter_dir / "example.snbt").write_text(
            '{ icon: { id: "minecraft:book" }, quests: [{ tasks: ['
            '{ type: "item", item: { id: "example:gem", count: 1 } }] }] }\n',
            encoding="utf-8",
        )
        manifest_path = root / "manifest.json"
        probe_path = root / "probe.js"
        m = prepare(chapter_dir, manifest_path, probe_path)
        assert m["items"] == ["example:gem", "minecraft:book"]
        assert "ServerEvents.loaded" in probe_path.read_text()
        ok = (
            f"TMM_QUEST_ITEM_SUMMARY sha256={m['sha256']} "
            "items=2 missing=0\n"
        )
        verify(m, ok)
        for bad in (
            "",
            ok.replace("items=2", "items=1"),
            ok.replace("missing=0", "missing=1"),
            "TMM_QUEST_ITEM_MISSING=example:gem\n"
            + ok.replace("missing=0", "missing=1"),
        ):
            try:
                verify(m, bad)
            except ValueError:
                continue
            raise AssertionError(f"Invalid registry result passed: {bad!r}")
        (chapter_dir / "example.snbt").write_text(
            '{ icon: { texture: "missing_format" } }', encoding="utf-8"
        )
        try:
            collect(chapter_dir)
        except ValueError:
            pass
        else:
            raise AssertionError("Unknown icon format was silently skipped")
    print("PASS: quest registry gate fail-closed unit tests")


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    sub = parser.add_subparsers(dest="action", required=True)
    p = sub.add_parser("prepare")
    p.add_argument("--chapters", type=Path, default=Path("config/ftbquests/quests/chapters"))
    p.add_argument("--manifest", type=Path, required=True)
    p.add_argument("--probe", type=Path, required=True)
    v = sub.add_parser("verify")
    v.add_argument("--manifest", type=Path, required=True)
    v.add_argument("--log", type=Path, required=True)
    sub.add_parser("selftest")
    args = parser.parse_args()
    if args.action == "prepare":
        prepare(args.chapters, args.manifest, args.probe)
    elif args.action == "verify":
        verify(
            json.loads(args.manifest.read_text(encoding="utf-8")),
            args.log.read_text(encoding="utf-8", errors="replace"),
        )
    else:
        selftest()


if __name__ == "__main__":
    try:
        main()
    except (ValueError, OSError, KeyError, TypeError, AssertionError) as exc:
        print(f"QUEST REGISTRY GATE FAILED: {exc}", file=sys.stderr)
        sys.exit(1)
