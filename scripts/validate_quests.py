#!/usr/bin/env python3
"""Static validation for TMM FTB Quests, including reward and objective contracts."""
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


def scan_snbt(text: str, filename: str) -> None:
    """Basic balanced-string/brace scan. Game runtime is still authoritative."""
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
            ensure(bool(stack) and stack.pop() == pairs[char], f"Malformed delimiters in {filename}")
    ensure(not in_string and not stack, f"Unclosed quote/delimiter in {filename}")


def quest_blocks(text: str, filename: str) -> list[str]:
    """Extract top-level quest compounds from the quests array, ignoring nested NBT."""
    m = re.search(r'\bquests:\s*\[', text)
    ensure(m is not None, f"Missing quests list in {filename}")
    i = m.end()
    nesting = 1
    braces = 0
    quoted = False
    escaped = False
    current = None
    out: list[str] = []
    while i < len(text):
        ch = text[i]
        if quoted:
            if escaped:
                escaped = False
            elif ch == "\\":
                escaped = True
            elif ch == '"':
                quoted = False
            i += 1
            continue
        if ch == '"':
            quoted = True
        elif ch == "[":
            nesting += 1
        elif ch == "]":
            nesting -= 1
            if nesting == 0:
                ensure(braces == 0, f"Unfinished quest compound in {filename}")
                return out
        elif ch == "{":
            if nesting == 1 and braces == 0:
                current = i
            braces += 1
        elif ch == "}":
            braces -= 1
            ensure(braces >= 0, f"Unbalanced quest compounds in {filename}")
            if nesting == 1 and braces == 0 and current is not None:
                out.append(text[current:i + 1])
                current = None
        i += 1
    raise ValueError(f"Unterminated quests array: {filename}")


def main() -> None:
    chapters_spec = json.loads((ROOT / "manifest" / "quest_chapters.json").read_text(encoding="utf-8"))
    authored = json.loads((ROOT / "manifest" / "quest_content.json").read_text(encoding="utf-8"))
    resolved = json.loads((ROOT / "manifest" / "quest_content_resolved.json").read_text(encoding="utf-8"))
    groups = chapters_spec["groups"]
    chapters = chapters_spec["chapters"]
    ensure({g["name"]: g["count"] for g in groups} == EXPECTED, "Wrong group counts")
    ensure(len(groups) == 5 and len(chapters) == 45, "Expected five groups and 45 chapters")
    ensure(len(authored["quests"]) == len(resolved["quests"]) == 46, "Expected 46 authored intro quests")
    ensure([q["title"] for q in authored["quests"]] == [q["title"] for q in resolved["quests"]], "Authored/resolved quest order differs")
    names = {c["name"] for c in chapters}
    ensure({"Getting Started", "Tips & Tricks", "Food & Fishing"} <= names, "Intro chapter missing")

    grptext = (QUESTS / "chapter_groups.snbt").read_text(encoding="utf-8")
    scan_snbt(grptext, "chapter_groups")
    ensure(re.findall(r'\bid: "([0-9A-F]{16})"', grptext) == [g["id"] for g in groups], "Group order mismatch")
    grouploc = (QUESTS / "lang" / "en_us" / "chapter_group.snbt").read_text(encoding="utf-8")
    chapterloc = (QUESTS / "lang" / "en_us" / "chapter.snbt").read_text(encoding="utf-8")
    scan_snbt(grouploc, "group labels")
    scan_snbt(chapterloc, "chapter labels")
    for g in groups:
        ensure(f'chapter_group.{g["id"]}.title: {json.dumps(g["name"])}' in grouploc, f"Missing group label {g['name']}")
    wanted = {c["filename"] for c in chapters}
    existing = {x.name for x in (QUESTS / "chapters").glob("*.snbt")}
    ensure(wanted == existing, f"Wrong chapter files: extra={existing-wanted} missing={wanted-existing}")

    global_ids: set[str] = {g["id"] for g in groups}
    all_blocks: dict[str, str] = {}
    item_tasks: dict[str, str] = {}
    advancement_tasks: dict[str, str] = {}
    reward_items: list[tuple[str, str]] = []
    for c in chapters:
        path = QUESTS / "chapters" / c["filename"]
        text = path.read_text(encoding="utf-8")
        scan_snbt(text, c["filename"])
        ensure(f'group: "{c["group_id"]}"' in text, f"Wrong group on {path.name}")
        ensure(f'id: "{c["chapter_id"]}"' in text, f"Wrong chapter ID on {path.name}")
        ensure(f'filename: "{path.stem}"' in text, f"Wrong filename on {path.name}")
        ensure(f'order_index: {c["order_index"]}' in text, f"Wrong chapter index on {path.name}")
        ensure(f'title: {json.dumps(c["name"])}' in text, f"Wrong chapter title on {path.name}")
        ensure(f'chapter.{c["chapter_id"]}.title: {json.dumps(c["name"])}' in chapterloc, f"Missing locale for {path.name}")
        ensure("re-avaritia" not in text and "extended-crafting" not in text, f"Excluded mod referenced: {path.name}")
        for ident in re.findall(r'\bid: "([0-9A-F]{16})"', text):
            ensure(ident not in global_ids, f"Duplicate global chapter/quest/task/reward ID: {ident} in {path.name}")
            global_ids.add(ident)
        blocks = quest_blocks(text, path.name)
        ensure(blocks, f"Empty quest chapter: {path.name}")
        expected = 1 if c["category"] != "Introduction" else {"Getting Started": 11, "Tips & Tricks": 15, "Food & Fishing": 20}[c["name"]]
        ensure(len(blocks) == expected, f"Wrong quest count in {path.name}: got {len(blocks)} expected {expected}")
        for block in blocks:
            head = re.match(r'\{\s*id: "([0-9A-F]{16})"', block)
            ensure(head is not None, f"Quest has no primary ID in {path.name}")
            qid = head.group(1)
            ensure(qid not in all_blocks, f"Duplicate quest ID: {qid}")
            all_blocks[qid] = block
            reward_start = block.find("rewards: [")
            task_start = block.find("tasks: [")
            ensure(0 <= reward_start < task_start, f"Missing ordered rewards/tasks on {qid}")
            rewards = block[reward_start:task_start]
            tasks = block[task_start:]
            xp = re.search(r'type: "xp", xp: (\d+)', rewards)
            itm = re.search(r'type: "item", item: \{ id: "([^"]+)", count: 1 \}, count: (\d+)', rewards)
            ensure(xp is not None and int(xp.group(1)) > 0, f"Missing positive XP on {qid}")
            ensure(itm is not None and int(itm.group(2)) > 0, f"Missing item reward on {qid}")
            ensure(rewards.count('type: "xp"') == 1 and rewards.count('type: "item"') == 1, f"Unexpected rewards on {qid}")
            reward_items.append((qid, itm.group(1)))
            for item_id in re.findall(r'item: \{ id: "([^"]+)", count: 1 \}', tasks):
                ensure(item_id not in item_tasks, f"Accidental repeated item criterion {item_id} in {qid} and {item_tasks.get(item_id)}")
                item_tasks[item_id] = qid
            for advance in re.findall(r'advancement: "([^"]+)"', tasks):
                ensure(advance not in advancement_tasks, f"Accidental repeated advancement criterion {advance}")
                advancement_tasks[advance] = qid
            if c["category"] != "Introduction":
                ensure(qid == c["overview_quest_id"], f"Unexpected nonintro quest in {path.name}")
                ensure("optional: true" in block and 'type: "checkmark"' in tasks, f"Unapproved placeholder objective in {path.name}")
            if qid == c["overview_quest_id"]:
                ensure(f'id: "{c["overview_task_id"]}"' in tasks, f"Changed stable overview task ID {path.name}")

    ensure(len(all_blocks) == 88, f"Expected 88 quests, found {len(all_blocks)}")
    for q in resolved["quests"]:
        qid = q["quest_id"]
        ensure(qid in all_blocks, f"Authored quest missing {q['title']}")
        block = all_blocks[qid]
        for key in ("task_id", "xp_reward_id", "item_reward_id"):
            ensure(f'id: "{q[key]}"' in block, f"Lost stable {key} in {q['title']}")
        ensure(f'xp: {q["reward"]["xp"]}' in block, f"Wrong XP in {q['title']}")
        ensure(f'id: "{q["reward"]["item"]}"' in block, f"Wrong reward item in {q['title']}")
        if q.get("depends"):
            match = next((x for x in resolved["quests"] if x["chapter"] == q["chapter"] and x["title"] == q["depends"]), None)
            ensure(match is not None and f'dependencies: ["{match["quest_id"]}"]' in block, f"Broken prerequisite {q['title']}")
        if q["criterion"]["type"] == "item":
            ensure(q["criterion"]["id"] in item_tasks and item_tasks[q["criterion"]["id"]] == qid, f"Wrong task for {q['title']}")
        if q["criterion"]["type"] == "advancement":
            ensure(advancement_tasks.get(q["criterion"]["id"]) == qid, f"Wrong advancement for {q['title']}")
        if q["criterion"]["type"] == "checkmark":
            ensure('type: "checkmark"' in block, f"Missing manual checkmark {q['title']}")

    shortcuts = [(qid, item_id, item_tasks[item_id]) for qid, item_id in reward_items if item_id in item_tasks]
    ensure(not shortcuts, f"Quest item rewards could complete another item task: {shortcuts}")
    ensure(advancement_tasks == {"tmm:first_log": "544D4D1100000001"}, "Unexpected advancement objective overlap")
    ensure(len(item_tasks) == 28, f"Unexpected unique item-task count: {len(item_tasks)}")
    fn = (ROOT / "kubejs/data/tmm/function/rewards/first_log.mcfunction").read_text(encoding="utf-8")
    ensure(fn.count("give @s ars_elemental:curio_bag 1") == 1, "First-log item issued wrong number of times")
    a = json.loads((ROOT / "kubejs/data/tmm/advancement/first_log.json").read_text(encoding="utf-8"))
    ensure(a.get("rewards", {}).get("function") == "tmm:rewards/first_log", "First-log advancement reward changed")
    ars = json.loads((ROOT / "kubejs/data/ars_elemental/tags/item/blacklist_bag_item.json").read_text(encoding="utf-8"))
    ensure("#tmm:portable_bags" in ars["values"], "Ars bag blacklist missing")
    for path in QUESTS.rglob("*.snbt"):
        scan_snbt(path.read_text(encoding="utf-8"), str(path))
    print(f"PASS: five groups, 45 chapters, {len(all_blocks)} quests; every quest awards XP + item")
    print("PASS: no duplicate item/advancement objectives, no item reward completing another item-task criterion")
    print("NOT A GAME TEST: item registries, FTB GUI, reward claims, teams and server progression remain unverified")


if __name__ == "__main__":
    main()
