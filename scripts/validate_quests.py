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
    ensure(len(authored["quests"]) == len(resolved["quests"]) == 61, "Expected 61 authored quests")
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
    item_tasks: dict[str, list[tuple[str, int]]] = {}
    advancement_tasks: dict[str, str] = {}
    reward_items: list[tuple[str, str, int]] = []
    coin_quests: set[str] = set()
    intentional_sale_ids = {"minecraft:copper_ingot", "minecraft:copper_block", "minecraft:emerald", "minecraft:emerald_block", "minecraft:diamond", "minecraft:diamond_block"}
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
        expected = {"Getting Started": 11, "Tips & Tricks": 16, "Food & Fishing": 22, "Basic Progression": 13}.get(c["name"], 1)
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
            coins = re.search(r'type: "questshop:coins", amount: (\d+)', rewards)
            ensure((itm is not None and int(itm.group(2)) > 0) or (coins is not None and int(coins.group(1)) > 0), f"Missing fixed item or justified coin reward on {qid}")
            ensure(rewards.count('type: "xp"') == 1 and rewards.count('type: "item"') + rewards.count('type: "questshop:coins"') == 1, f"Unexpected rewards on {qid}")
            if itm is not None:
                reward_items.append((qid, itm.group(1), int(itm.group(2))))
            else:
                coin_quests.add(qid)
                ensure('team: true' in rewards, f"Quest Shop coins must use team-shared one-claim rewards on {qid}")
                ensure("consume_items: true" in tasks, f"Sale input must be consumed on {qid}")
                ensure("can_repeat: true" not in block or "repeat_cooldown: 2" in block, f"Sale cooldown missing on {qid}")
            for item_id, required in re.findall(r'item: \{ id: "([^"]+)", count: 1 \}(?:, count: (\d+)L)?', tasks):
                count = int(required) if required else 1
                existing = item_tasks.get(item_id, [])
                if existing:
                    ensure(item_id in intentional_sale_ids, f"Accidental repeated item criterion {item_id} in {qid} and {existing}")
                    ensure(qid in coin_quests and all(x[0] in coin_quests for x in existing), f"Repeated criterion {item_id} must be limited to deliberate sales")
                    ensure(all(x[1] != count for x in existing), f"Identical submission quantities for {item_id}")
                item_tasks.setdefault(item_id, []).append((qid, count))
            for advance in re.findall(r'advancement: "([^"]+)"', tasks):
                ensure(advance not in advancement_tasks, f"Accidental repeated advancement criterion {advance}")
                advancement_tasks[advance] = qid
            if c["name"] not in {"Getting Started", "Tips & Tricks", "Food & Fishing", "Basic Progression"}:
                ensure(qid == c["overview_quest_id"], f"Unexpected nonintro quest in {path.name}")
                ensure("optional: true" in block and 'type: "checkmark"' in tasks, f"Unapproved placeholder objective in {path.name}")
            if qid == c["overview_quest_id"]:
                ensure(f'id: "{c["overview_task_id"]}"' in tasks, f"Changed stable overview task ID {path.name}")

    ensure(len(all_blocks) == 103, f"Expected 103 quests, found {len(all_blocks)}")
    for q in resolved["quests"]:
        qid = q["quest_id"]
        ensure(qid in all_blocks, f"Authored quest missing {q['title']}")
        block = all_blocks[qid]
        for key in ("task_id", "xp_reward_id", "item_reward_id"):
            ensure(f'id: "{q[key]}"' in block, f"Lost stable {key} in {q['title']}")
        ensure(f'xp: {q["reward"]["xp"]}' in block, f"Wrong XP in {q['title']}")
        if "item" in q["reward"]:
            ensure(f'id: "{q["reward"]["item"]}"' in block, f"Wrong item reward in {q['title']}")
        else:
            ensure(qid in coin_quests and f'amount: {q["reward"]["coin"]}' in block, f"Wrong coin reward in {q['title']}")
        if q.get("depends"):
            match = next((x for x in resolved["quests"] if x["chapter"] == q["chapter"] and x["title"] == q["depends"]), None)
            ensure(match is not None and f'dependencies: ["{match["quest_id"]}"]' in block, f"Broken prerequisite {q['title']}")
        if q["criterion"]["type"] == "item":
            ensure(q["criterion"]["id"] in item_tasks and (qid, q["criterion"].get("count", 1)) in item_tasks[q["criterion"]["id"]], f"Wrong task for {q['title']}")
            if q.get("consume_items"):
                ensure("consume_items: true" in block, f"Unconsumed sale input in {q['title']}")
            if q.get("repeatable"):
                ensure("can_repeat: true" in block, f"Missing repeatability in {q['title']}")
        if q["criterion"]["type"] == "advancement":
            ensure(advancement_tasks.get(q["criterion"]["id"]) == qid, f"Wrong advancement for {q['title']}")
        if q["criterion"]["type"] == "checkmark":
            ensure('type: "checkmark"' in block, f"Missing manual checkmark {q['title']}")

    shortcuts = [(qid, item_id, payout_count, task_id, task_count)
                 for qid, item_id, payout_count in reward_items
                 for task_id, task_count in item_tasks.get(item_id, [])
                 if qid != task_id and payout_count >= task_count]
    ensure(not shortcuts, f"Quest item rewards alone could complete another item task: {shortcuts}")
    ensure(advancement_tasks == {"tmm:first_log": "544D4D1100000001"}, "Unexpected advancement objective overlap")
    ensure(len(item_tasks) == 36, f"Unexpected unique item-task count: {len(item_tasks)}")
    ensure(len(coin_quests) == 12, f"Expected exactly 12 intentional resource sales, found {len(coin_quests)}")
    for commodity in intentional_sale_ids:
        ensure(len(item_tasks.get(commodity, [])) == 2, f"Commodity {commodity} must appear in exactly two documented tiers")
    shop = json.loads((ROOT / "manifest" / "quest_shop_catalog.json").read_text(encoding="utf-8"))
    ensure(len(shop["categories"]) == 5, "Shop must have five planned categories")
    for category in shop["categories"]:
        cat = category["id"]
        catfile = ROOT / "kubejs" / "data" / "tmm" / "questshop" / "shop_categories" / (cat + ".json")
        entriesfile = ROOT / "kubejs" / "data" / "tmm" / "questshop" / "shop_entries" / (cat + ".json")
        ensure(catfile.is_file() and entriesfile.is_file(), f"Missing shop datapack files for {cat}")
        parsed = json.loads(entriesfile.read_text(encoding="utf-8"))
        ensure(len(parsed) == len(category["entries"]), f"Shop item count mismatch for {cat}")
        for product in parsed:
            ensure(product["category"] == "tmm:" + cat, f"Wrong namespaced shop category in {cat}")
            ensure(product["amount"] > 0 and product["cost"] > 0, "Invalid shop quantity/price")
            ensure(not product["item"].startswith(("projecte:", "projectexpansion:", "reavaritia:", "extendedcrafting:")), f"Shop progression bypass item {product['item']}")
    fn = (ROOT / "kubejs/data/tmm/function/rewards/first_log.mcfunction").read_text(encoding="utf-8")
    ensure(fn.count("give @s ars_elemental:curio_bag 1") == 1, "First-log item issued wrong number of times")
    a = json.loads((ROOT / "kubejs/data/tmm/advancement/first_log.json").read_text(encoding="utf-8"))
    ensure(a.get("rewards", {}).get("function") == "tmm:rewards/first_log", "First-log advancement reward changed")
    ars = json.loads((ROOT / "kubejs/data/ars_elemental/tags/item/blacklist_bag_item.json").read_text(encoding="utf-8"))
    ensure("#tmm:portable_bags" in ars["values"], "Ars bag blacklist missing")
    for path in QUESTS.rglob("*.snbt"):
        scan_snbt(path.read_text(encoding="utf-8"), str(path))
    print(f"PASS: five groups, 45 chapters, {len(all_blocks)} quests; all give XP and a fixed item or justified coin")
    print("PASS: shared item criteria restricted to intentional copper/emerald/diamond sales; no item reward completes a task")
    print("NOT A GAME TEST: item registries, FTB GUI, reward claims, teams and server progression remain unverified")


if __name__ == "__main__":
    main()
