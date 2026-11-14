#!/usr/bin/env python3
"""Static validation for the TMM FTB Quests scaffold, NOT a client or game test."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
QUESTS = ROOT / "config" / "ftbquests" / "quests"
EXPECTED = {"Introduction": 3, "Progression": 5, "Magic": 25, "Exploration & Combat": 9, "Endgame": 3}
ID_RE = re.compile(r"^[0-9A-F]{16}$")


def ensure(ok: bool, message: str) -> None:
    if not ok:
        raise ValueError(message)


def basic_snbt_scan(text: str, filename: str) -> None:
    """Check balanced strings/braces/brackets; runtime parser remains authoritative."""
    pairs = {"]": "[", "}": "{"}
    stack: list[str] = []
    in_string = False
    escaped = False
    for char in text:
        if in_string:
            if escaped:
                escaped = False
            elif char == "\\":
                escaped = True
            elif char == '"':
                in_string = False
            continue
        if char == '"':
            in_string = True
        elif char in "[{":
            stack.append(char)
        elif char in "]}":
            ensure(bool(stack) and stack.pop() == pairs[char], f"Malformed SNBT delimiters in {filename}")
    ensure(not in_string and not stack, f"Unclosed SNBT string/delimiter in {filename}")


def main() -> None:
    spec = json.loads((ROOT / "manifest" / "quest_chapters.json").read_text(encoding="utf-8"))
    groups = spec["groups"]
    chapters = spec["chapters"]
    ensure({g["name"]: g["count"] for g in groups} == EXPECTED, "Expected exactly 5 quest categories and 45 chapter counts")
    ensure(len(chapters) == 45, "Expected exactly 45 quest chapters")
    allids: set[str] = set()
    group_ids = {g["name"]: g["id"] for g in groups}
    for group in groups:
        ensure(bool(ID_RE.fullmatch(group["id"])), f"Invalid group ID: {group}")
        ensure(group["id"] not in allids, "Duplicate group ID")
        allids.add(group["id"])
    group_text = (QUESTS / "chapter_groups.snbt").read_text(encoding="utf-8")
    basic_snbt_scan(group_text, "chapter_groups.snbt")
    ensure(re.findall(r'\bid: "([0-9A-F]{16})"', group_text) == [g["id"] for g in groups], "Chapter group IDs/order differ")
    loc = (QUESTS / "lang" / "en_us" / "chapter_group.snbt").read_text(encoding="utf-8")
    chaploc = (QUESTS / "lang" / "en_us" / "chapter.snbt").read_text(encoding="utf-8")
    basic_snbt_scan(loc, "chapter_group locale")
    basic_snbt_scan(chaploc, "chapter locale")
    for group in groups:
        ensure(f'chapter_group.{group["id"]}.title: "{group["name"]}"' in loc, f"Missing English group label {group['name']}")
    filenames = [c["filename"] for c in chapters]
    ensure(len(set(filenames)) == 45, "Duplicate chapter filename")
    actual = {p.name for p in (QUESTS / "chapters").glob("*.snbt")}
    ensure(actual == set(filenames), f"Chapter files mismatch: missing={set(filenames)-actual} extras={actual-set(filenames)}")
    first_log = False
    for chapter in chapters:
        p = QUESTS / "chapters" / chapter["filename"]
        s = p.read_text(encoding="utf-8")
        basic_snbt_scan(s, str(p))
        ensure(chapter["group_id"] == group_ids[chapter["category"]], f"Wrong group for {p.name}")
        ensure(f'group: "{chapter["group_id"]}"' in s, f"Missing group reference: {p.name}")
        ensure(f'id: "{chapter["chapter_id"]}"' in s, f"Missing chapter ID: {p.name}")
        ensure(f'filename: "{p.stem}"' in s, f"Wrong chapter filename: {p.name}")
        ensure(f'order_index: {chapter["order_index"]}' in s, f"Wrong order: {p.name}")
        ensure(f'title: {json.dumps(chapter["name"])}' in s, f"Wrong chapter title: {p.name}")
        ensure(f'chapter.{chapter["chapter_id"]}.title: {json.dumps(chapter["name"])}' in chaploc, f"Missing localization {p.name}")
        ensure(f'id: "{chapter["overview_quest_id"]}"' in s and f'id: "{chapter["overview_task_id"]}"' in s, f"Missing overview IDs: {p.name}")
        ensure('type: "checkmark"' in s and "optional: true" in s, f"Missing optional read-me checkmark: {p.name}")
        ensure('rewards: [ ]' in s, f"Unexpected reward scaffold: {p.name}")
        ensure("re-avaritia" not in s and "extended-crafting" not in s, f"Forbidden removed mod in {p.name}")
        for field in ("chapter_id", "overview_quest_id", "overview_task_id"):
            ident = chapter[field]
            ensure(bool(ID_RE.fullmatch(ident)) and ident not in allids, f"Invalid/duplicate {field}={ident}")
            allids.add(ident)
        if chapter["name"] == "Getting Started":
            ensure(p.name == "intro.snbt", "Intro filename unexpectedly changed")
            for ident in ("544D4D1100000001", "544D4D1200000001"):
                ensure(ident not in allids and f'id: "{ident}"' in s, f"Missing/duplicate first-log ID: {ident}")
                allids.add(ident)
            ensure('advancement: "tmm:first_log"' in s, "First log achievement not connected")
            first_log = True
        else:
            ensure('type: "advancement"' not in s, f"Scaffold accidentally added advancement: {p.name}")
    ensure(first_log and len(allids) == 5 + (45 * 3) + 2, "Unexpected ID counts")
    ensure('function' in (ROOT / 'kubejs/data/tmm/advancement/first_log.json').read_text(), "Missing first-log function reward")
    fn = (ROOT / "kubejs/data/tmm/function/rewards/first_log.mcfunction").read_text(encoding="utf-8")
    ensure(fn.count("give @s ars_elemental:curio_bag 1") == 1, "Unexpected first-log reward")
    ars_blacklist = json.loads((ROOT / "kubejs/data/ars_elemental/tags/item/blacklist_bag_item.json").read_text(encoding="utf-8"))
    ensure("#tmm:portable_bags" in ars_blacklist["values"], "Ars Elemental nested bag blacklist missing")
    portable = json.loads((ROOT / "kubejs/data/tmm/tags/item/portable_bags.json").read_text(encoding="utf-8"))
    ensure("ars_elemental:curio_bag" in portable["values"], "Starter bag missing from blacklist")
    for p in QUESTS.rglob("*.snbt"):
        basic_snbt_scan(p.read_text(encoding="utf-8"), str(p))
    print("PASS: FTB Quests scaffold has 5 groups, 45 chapters, unique stable IDs, EN labels and first-log prototype")
    print("NOT A GAME TEST: chapter rendering, advancement rewards and multiplayer need runtime checks")


if __name__ == "__main__":
    main()
