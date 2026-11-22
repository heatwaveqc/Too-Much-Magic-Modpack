#!/usr/bin/env python3
"""Static validation for TMM FTB Quests, including reward and objective contracts."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
QUESTS = ROOT / "config" / "ftbquests" / "quests"
EXPECTED = {"Introduction": 4, "Progression": 5, "Magic": 25, "Exploration & Combat": 7, "Endgame": 3}
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
    authored_by_key = {(q["chapter"], q["title"]): q for q in authored["quests"]}
    resolved_by_id = {q["quest_id"]: q for q in resolved["quests"]}
    groups = chapters_spec["groups"]
    chapters = chapters_spec["chapters"]
    ensure({g["name"]: g["count"] for g in groups} == EXPECTED, "Wrong group counts")
    ensure(len(groups) == 5 and len(chapters) == 44, "Expected five groups and 44 chapters")
    from build_theurgy_quest_pilot import check_spec as check_theurgy_spec, render as render_theurgy
    pilot = json.loads((ROOT / "manifest/theurgy_quest_pilot.json").read_text(encoding="utf-8"))
    check_theurgy_spec(pilot)
    expected_theurgy = render_theurgy(pilot)["theurgy.snbt"]
    actual_theurgy = (QUESTS / "chapters" / "theurgy.snbt").read_text(encoding="utf-8")
    ensure(actual_theurgy == expected_theurgy, "Theurgy live chapter differs from reviewed pilot source")
    art_dir = ROOT / "kubejs/assets/tmm/textures/questpics/theurgy"
    if art_dir.exists():
        expected_art = {"title.png", "vessels.png", "niter.png", "reformation.png", "wires.png"}
        actual_art = {f.name for f in art_dir.iterdir() if f.is_file()}
        ensure(actual_art == expected_art, "Incomplete Theurgy client quest art")
        ensure(all((art_dir / name).read_bytes().startswith(b"\x89PNG\r\n\x1a\n") for name in expected_art), "Invalid Theurgy PNG")

    ensure(len(authored["quests"]) == len(resolved["quests"]) == 221, "Expected 221 authored quests including 20 Theurgy lessons")
    ensure([q["title"] for q in authored["quests"]] == [q["title"] for q in resolved["quests"]], "Authored/resolved quest order differs")
    names = {c["name"] for c in chapters}
    ensure({"Getting Started", "Tips & Tricks", "Food & Fishing"} <= names, "Intro chapter missing")

    grptext = (QUESTS / "chapter_groups.snbt").read_text(encoding="utf-8")
    scan_snbt(grptext, "chapter_groups")
    ensure(re.findall(r'\bid: "([0-9A-F]{16})"', grptext) == [g["id"] for g in groups], "Group order mismatch")
    language_path = QUESTS / "lang" / "en_us.snbt"
    ensure(language_path.is_file(), "FTB Quests 2101.x requires lang/en_us.snbt")
    ensure(not (QUESTS / "lang" / "en_us").exists(), "Legacy split lang/en_us directory is invalid")
    lang_text = language_path.read_text(encoding="utf-8")
    scan_snbt(lang_text, str(language_path))
    ensure("undefined" not in lang_text, "Broken English quest translations")
    grouploc = lang_text
    chapterloc = lang_text
    translated_titles = re.findall(r'quest\.([0-9A-F]{16})\.title: ', lang_text)
    translated_desc = re.findall(r'quest\.([0-9A-F]{16})\.quest_desc: \[', lang_text)
    ensure(len(translated_titles) == len(translated_desc) == 259,
           f"Expected 240 localized quests, got {len(translated_titles)} titles and {len(translated_desc)} descriptions")
    lang_ids = set(translated_titles)
    ensure(len(lang_ids) == 259 and lang_ids == set(translated_desc),
           "Missing/duplicate quest locale keys in lang/en_us.snbt")
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
        ensure("undefined" not in text, f"Unrendered quest descriptions in {path.name}")
        ensure(not re.search(r'(?m)^\t{3}(?:title|description):', text),
               f"Quest title/description wrongly embedded in data instead of English locale: {path.name}")
        ensure(not re.search(r'(?m)^\t(?:title|subtitle):', text),
               f"Chapter title/subtitle must be localized: {path.name}")
        ensure(f'group: "{c["group_id"]}"' in text, f"Wrong group on {path.name}")
        ensure(f'id: "{c["chapter_id"]}"' in text, f"Wrong chapter ID on {path.name}")
        ensure(f'filename: "{path.stem}"' in text, f"Wrong filename on {path.name}")
        ensure(f'order_index: {c["order_index"]}' in text, f"Wrong chapter index on {path.name}")
        # Chapter title lives in lang/en_us.snbt, not in the chapter data.
        ensure(f'chapter.{c["chapter_id"]}.title: {json.dumps(c["name"], ensure_ascii=False)}' in chapterloc,
               f"Wrong English chapter name for {path.name}")
        ensure(f'chapter.{c["chapter_id"]}.chapter_subtitle: [' in chapterloc,
               f"Missing localized chapter subtitle for {path.name}")
        ensure("re-avaritia" not in text and "extended-crafting" not in text, f"Excluded mod referenced: {path.name}")
        for ident in re.findall(r'\bid: "([0-9A-F]{16})"', text):
            ensure(ident not in global_ids, f"Duplicate global chapter/quest/task/reward ID: {ident} in {path.name}")
            global_ids.add(ident)
        blocks = quest_blocks(text, path.name)
        ensure(blocks, f"Empty quest chapter: {path.name}")
        expected = {"Getting Started": 20, "Tips & Tricks": 20, "Food & Fishing": 22, "Alcohol is Magic": 19, "Ars Nouveau": 49,
                    "Basic Progression": 23, "Advanced Progression": 11, "Endgame Roadmap": 7,
                    "Skills & Factions": 18, "Theurgy": 20, "Artifacts": 3, "ProjectE": 2, "A Sufficiently Advanced Magic": 14}.get(c["name"], 1)
        ensure(len(blocks) == expected, f"Wrong quest count in {path.name}: got {len(blocks)} expected {expected}")
        coords = set()
        for block in blocks:
            coord = re.search(r'\bx: (-?[0-9.]+)d\s+y: (-?[0-9.]+)d', block)
            ensure(coord is not None, f"Missing quest coordinates in {path.name}")
            point = (float(coord.group(1)), float(coord.group(2)))
            ensure(point not in coords, f"Overlapping quest icons at {point} in {path.name}")
            coords.add(point)
            head = re.match(r'\{\s*id: "([0-9A-F]{16})"', block)
            ensure(head is not None, f"Quest has no primary ID in {path.name}")
            qid = head.group(1)
            ensure(qid in lang_ids, f"Quest missing a locale entry: {qid} in {path.name}")
            record = resolved_by_id.get(qid)
            expected_title = record["title"] if record is not None else "Chapter Overview"
            ensure(f'quest.{qid}.title: {json.dumps(expected_title, ensure_ascii=False)}' in lang_text,
                   f"Wrong English quest title: {qid} ({expected_title})")
            if record is not None:
                authored_record = authored_by_key.get((c["name"], expected_title))
                ensure(authored_record is not None, f"Missing canonical source text for {qid}")
                desc = authored_record["description"]
                ensure(isinstance(desc, str) and bool(desc.strip()), f"Empty authoring description for {qid}")
                wanted_desc = f'quest.{qid}.quest_desc: [{json.dumps(desc, ensure_ascii=False)}]'
                ensure(wanted_desc in lang_text, f"Quest description differs from its authoring source: {qid}")
            ensure(qid not in all_blocks, f"Duplicate quest ID: {qid}")
            all_blocks[qid] = block
            reward_start = block.find("rewards: [")
            task_start = block.find("tasks: [")
            ensure(0 <= reward_start < task_start, f"Missing ordered rewards/tasks on {qid}")
            rewards = block[reward_start:task_start]
            tasks = block[task_start:]
            xp = re.search(r'type: "xp", xp: (\d+)', rewards)
            item_matches = re.findall(r'type: "item", item: \{ id: "([^"]+)", count: 1 \}, count: (\d+)', rewards)
            itm = re.search(r'type: "item", item: \{ id: "([^"]+)", count: 1 \}, count: (\d+)', rewards)
            ensure(xp is not None and int(xp.group(1)) > 0, f"Missing positive XP on {qid}")
            # Q Shop's published 1.21.1 FTB reward still risks the void/boolean ABI mismatch.
            # FTB Quests 2101.1.38 expects boolean; use its native command reward
            # to invoke Q Shop's personal-wallet command until the native reward is verified.
            coins = re.search(r'type: "command", command: "qshop currency give \{p\} coins (\d+)", permission_level: 2, team_reward: false', rewards)
            ensure('type: "questshop:coins"' not in rewards and 'type: "qshop:money"' not in rewards, f"Unsafe native shop reward ABI on {qid}")
            ensure(re.search(r'\bteam: true\b', rewards) is None, f"Legacy team key on {qid}; expected team_reward")
            newbie_graduation = qid == "544D309000100001" and (
                'type: "command", command: "tmmnewbie admin graduate {p}", permission_level: 2, team_reward: false' in rewards
                and 'type: "checkmark"' in tasks and c["name"] == "Getting Started"
            )
            ensure((itm is not None and int(itm.group(2)) > 0) or (coins is not None and int(coins.group(1)) > 0)
                   or qid == "544D303000100001" or newbie_graduation,
                   f"Missing fixed item or approved command reward on {qid}")
            # Only the two existing Getting Started quests may carry an extra starter item.
            starter_rewards = {
                "544D301000100001": {("enigmaticlegacyplus:unwitnessed_amulet", "1"),
                                     ("enigmaticlegacyplus:cursed_ring", "1")},
            }
            expected_reward_count = 0 if qid == "544D303000100001" else (2 if qid in starter_rewards else 1)
            ensure(rewards.count('type: "xp"') == 1 and
                   rewards.count('type: "item"') + rewards.count('type: "command"') == expected_reward_count,
                   f"Unexpected rewards on {qid}")
            if qid in starter_rewards:
                ensure(len(item_matches) == 2 and set(item_matches) == starter_rewards[qid],
                       f"Wrong special starter rewards on {qid}")
            if itm is not None:
                reward_items.extend((qid, item_id, int(count)) for item_id, count in item_matches)
            elif coins is not None:
                coin_quests.add(qid)
                ensure(rewards.count("team_reward: true") == 1 and rewards.count("team_reward: false") == 1,
                       f"Sale XP remains team-shared but coin claims must be personal on {qid}")
                ensure("consume_items: true" in tasks, f"Sale input must be consumed on {qid}")
                ensure("can_repeat: true" not in block or "repeat_cooldown: 2" in block, f"Sale cooldown missing on {qid}")
            else:
                ensure((qid == "544D303000100001" and 'type: "checkmark"' in tasks)
                       or newbie_graduation, "Unapproved no-item quest reward")
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
            if c["name"] not in {"Getting Started", "Tips & Tricks", "Food & Fishing", "Alcohol is Magic", "Basic Progression",
                                 "Advanced Progression", "Endgame Roadmap", "Skills & Factions", "Artifacts", "ProjectE", "A Sufficiently Advanced Magic", "Ars Nouveau", "Theurgy"}:
                ensure(qid == c["overview_quest_id"], f"Unexpected nonintro quest in {path.name}")
                ensure("optional: true" in block and 'type: "checkmark"' in tasks, f"Unapproved placeholder objective in {path.name}")
            if qid == c["overview_quest_id"]:
                ensure(f'id: "{c["overview_task_id"]}"' in tasks, f"Changed stable overview task ID {path.name}")

    ensure(len(all_blocks) == 259, f"Expected 259 quests, found {len(all_blocks)}")
    for q in resolved["quests"]:
        qid = q["quest_id"]
        ensure(qid in all_blocks, f"Authored quest missing {q['title']}")
        block = all_blocks[qid]
        for key in ("task_id", "xp_reward_id") + (("item_reward_id",) if "item_reward_id" in q else ()):
            ensure(f'id: "{q[key]}"' in block, f"Lost stable {key} in {q['title']}")
        ensure(f'xp: {q["reward"]["xp"]}' in block, f"Wrong XP in {q['title']}")
        if "item" in q["reward"]:
            ensure(f'id: "{q["reward"]["item"]}"' in block, f"Wrong item reward in {q['title']}")
        elif "coin" in q["reward"]:
            ensure(qid in coin_quests and f'command: "qshop currency give {{p}} coins {q["reward"]["coin"]}"' in block, f"Wrong coin command reward in {q['title']}")
        elif "command" in q["reward"]:
            ensure(qid == "544D309000100001"
                   and q["reward"]["command"] == "tmmnewbie admin graduate {p}"
                   and 'team_reward: false' in block,
                   "Graduation command must be personal and admin-authorized")
        else:
            ensure(qid == "544D303000100001" and q["reward"] == {"xp": 5},
                   "Only Ars orientation is XP-only")
        if q.get("depends"):
            match = next((x for x in resolved["quests"] if x["chapter"] == q["chapter"] and x["title"] == q["depends"]), None)
            ensure(match is not None and f'dependencies: ["{match["quest_id"]}"]' in block, f"Broken prerequisite {q['title']}")
        if q["chapter"] == "Theurgy":
            predecessors = q.get("prerequisites", [])
            ids = []
            for predecessor in predecessors:
                match = next((x for x in resolved["quests"] if x["chapter"] == "Theurgy" and x["title"] == predecessor), None)
                ensure(match is not None, f"Theurgy unknown prerequisite: {predecessor}")
                ids.append(match["quest_id"])
            if ids:
                expected_deps = "dependencies: [" + ", ".join(json.dumps(x) for x in ids) + "]"
                ensure(expected_deps in block, f"Theurgy dropped/changed prerequisites: {q['title']}")
            else:
                ensure("dependencies:" not in block, "Unexpected Theurgy root quest dependency")

        if q["criterion"]["type"] == "item":
            ensure(q["criterion"]["id"] in item_tasks and (qid, q["criterion"].get("count", 1)) in item_tasks[q["criterion"]["id"]], f"Wrong task for {q['title']}")
            if q.get("consume_items"):
                ensure("consume_items: true" in block, f"Unconsumed sale input in {q['title']}")
            if q.get("repeatable"):
                ensure("can_repeat: true" in block, f"Missing repeatability in {q['title']}")
        if q["criterion"]["type"] == "item_tag":
            ensure(qid == "544D4D1100000001" and q["criterion"] == {"type": "item_tag", "id": "minecraft:logs", "filter": "ftbfiltersystem"},
                   f"Unexpected tag-filter quest: {q['title']}")
            native_filter = 'item: { components: { "ftbfiltersystem:filter": "ftbfiltersystem:item_tag(minecraft:logs)" }, count: 1, id: "ftbfiltersystem:smart_filter" }'
            ensure(native_filter in block and 'type: "item"' in block and "consume_items: true" not in block,
                   "Punch a Tree must detect a non-consuming native item-tag filter")
            ensure("advancement:" not in block, "Legacy first-log advancement task survived")
        if q["criterion"]["type"] == "advancement":
            ensure(advancement_tasks.get(q["criterion"]["id"]) == qid, f"Wrong advancement for {q['title']}")
        if q["criterion"]["type"] == "checkmark":
            ensure('type: "checkmark"' in block, f"Missing manual checkmark {q['title']}")

    shortcuts = [(qid, item_id, payout_count, task_id, task_count)
                 for qid, item_id, payout_count in reward_items
                 for task_id, task_count in item_tasks.get(item_id, [])
                 if qid != task_id and payout_count >= task_count]
    ensure(not shortcuts, f"Quest item rewards alone could complete another item task: {shortcuts}")
    expected_advancement_tasks = {q["criterion"]["id"]: q["quest_id"] for q in resolved["quests"] if q["criterion"]["type"] == "advancement"}
    ensure(advancement_tasks == expected_advancement_tasks, "Unexpected advancement objective overlap")
    ensure(len(item_tasks) == 115, f"Unexpected unique item-task count: {len(item_tasks)}")
    ensure([q["quest_id"] for q in resolved["quests"] if q["criterion"]["type"] == "item_tag"] == ["544D4D1100000001"],
           "Only Punch a Tree may use the native tag filter")
    progression = json.loads((ROOT / "manifest" / "progression_quest_content.json").read_text(encoding="utf-8"))
    planned = {"Basic Progression": 10, "Advanced Progression": 10, "Endgame Roadmap": 6,
               "Skills & Factions": 17, "A Sufficiently Advanced Magic": 13}
    ensure(len(progression["quests"]) == 56 and progression["counts"] == planned,
           "Full Progression source count or chapter distribution drift")
    ensure(all(x["criterion"]["type"] == "checkmark" for x in progression["quests"] if x["chapter"] == "Endgame Roadmap"),
           "Roadmap must not duplicate boss/material progression tasks")
    tech_items = {x["criterion"].get("id") for x in progression["quests"]
                  if x["chapter"] == "A Sufficiently Advanced Magic"}
    for core in ("draconicevolution:energy_core",
                 "draconicevolution:energy_pylon", "draconicevolution:reactor_core",
                 "draconicevolution:reactor_stabilizer", "draconicevolution:reactor_injector"):
        ensure(core in tech_items, f"Tech chapter lost verified milestone {core}")
    ensure(not any(x["reward"].get("item") == "draconicevolution:reactor_core" for x in progression["quests"]),
           "Never grant the Draconic Reactor Core as a quest reward")
    ensure(len(coin_quests) == 12, f"Expected exactly 12 intentional resource sales, found {len(coin_quests)}")
    for commodity in intentional_sale_ids:
        ensure(len(item_tasks.get(commodity, [])) == 2, f"Commodity {commodity} must appear in exactly two documented tiers")
    shop = json.loads((ROOT / "manifest/quest_shop_catalog.json").read_text(encoding="utf-8"))
    qshop = json.loads((ROOT / "config/qshop/shops/tmm.json").read_text(encoding="utf-8"))
    currencies = json.loads((ROOT / "config/qshop/currencies.json").read_text(encoding="utf-8"))
    ensure(shop["currency"] == "coins" and shop["team_shared"] is False, "Q Shop wallet must be personal")
    ensure([x["id"] for x in currencies["currencies"]] == ["coins"], "No second shop currency")
    ensure(qshop["id"] == "tmm" and qshop["currency"] == "coins", "Wrong Q Shop id/currency")
    ensure(len(qshop["tabs"]) == len(shop["categories"]) == 6, "Expected six matching Q Shop tabs")
    offers = []
    for category, tab in zip(shop["categories"], qshop["tabs"]):
        ensure(tab["uuid"] == "tmm-" + category["id"] and tab["name"] == category["display"], "Wrong Q Shop tab")
        rows = []
        for entry in tab["entries"]:
            ensure(entry["type"] == "BUY" and entry["currency"] == "coins", "Unexpected shop trade")
            rows.append([entry["item"]["item"], entry["item"]["count"], entry["price"]])
        ensure(rows == category["entries"], f"Shop drift in {category['id']}")
        offers.extend(rows)
    ensure(len(offers) == 48 and len({item for item, _, _ in offers}) == 48, "48 original offers must survive")
    ensure(not any(i.startswith(("projecte:", "projectexpansion:", "reavaritia:", "extendedcrafting:"))
                   for i, _, _ in offers), "Shop progression bypass")
    ensure(not (ROOT / "mods/quest-shop.pw.toml").exists() and (ROOT / "mods/q-shop.pw.toml").exists(),
           "Old shop pin or missing Q Shop")
    ensure(not (ROOT / "config/questshop-common.toml").exists()
           and not (ROOT / "kubejs/data/tmm/questshop").exists(), "Old shop config remains")
    ensure(not (ROOT / "kubejs/data/tmm/function/rewards/first_log.mcfunction").exists(),
           "Obsolete empty first-log reward function must not be shipped")
    pouch = all_blocks["544D4D1100000001"]
    ensure(pouch.count('id: "ars_elemental:curio_bag"') == 2 and 'id: "544D701000200003"' in pouch,
           "Punch a Tree must grant the original once-claimable Curio Bag reward")
    ensure('id: "minecraft:torch"' not in pouch, "Punch a Tree must not award torches")
    ensure(not (ROOT / "kubejs/data/tmm/advancement/first_log.json").exists(),
           "Obsolete first-log advancement must be absent")
    ensure("tmm:first_log" not in "\n".join(all_blocks.values()),
           "No FTB quest may reference the removed hidden advancement")
    pin = (ROOT / "mods/ftb-filter-system.pw.toml").read_text(encoding="utf-8")
    ensure('filename = "ftb-filter-system-neoforge-21.1.4.jar"' in pin
           and 'hash = "a48e54edeacc59aaba29a1b627799c052112ee0b"' in pin
           and "file-id = 7429011" in pin and "project-id = 943925" in pin,
           "Missing FTB Filter System 1.21.1 NeoForge pin")
    ars = json.loads((ROOT / "kubejs/data/ars_elemental/tags/item/blacklist_bag_item.json").read_text(encoding="utf-8"))
    ensure("#tmm:portable_bags" in ars["values"], "Ars bag blacklist missing")
    # FTB 2101.1.38 BaseQuestFile.readID(StringTag) uses signed Long.parseLong(hex, 16).
    # Out-of-range IDs are silently replaced at runtime, breaking translations and links.
    for path in QUESTS.rglob("*.snbt"):
        raw = path.read_text(encoding="utf-8")
        scan_snbt(raw, str(path))
        for ident in re.findall(r'(?<![0-9A-Za-z_])[0-9A-F]{16}(?![0-9A-Za-z_])', raw):
            ensure(1 < int(ident, 16) <= (1 << 63) - 1,
                   f"FTB signed-long ID out of range or reserved: {ident} in {path}")
    chapter_titles = re.findall(r'(?m)^\s*chapter\.([0-9A-F]{16})\.title:', lang_text)
    ensure(len(chapter_titles) == len(set(chapter_titles)) == len(chapters)
           and set(chapter_titles) == {c["chapter_id"] for c in chapters},
           "Chapter translation IDs must match all runtime chapters exactly")
    print("PASS: Theurgy 20-node/5-panel layout and stable dependency/reward IDs validated")
    print(f"PASS: five groups, 44 chapters, {len(all_blocks)} quests; all give XP; only Ars orientation is XP-only; others grant fixed items or justified coin")
    print("PASS: shared item criteria restricted to intentional copper/emerald/diamond sales; no item reward completes a task")
    print("NOT A GAME TEST: item registries, FTB GUI, reward claims, teams and server progression remain unverified")


if __name__ == "__main__":
    main()
