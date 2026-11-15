#!/usr/bin/env python3
"""Normalize the 1.21.1 FTB Quests book: translations, descriptions and readable layouts.

FTB Quests 2101.x reads user-visible quest text from:
  config/ftbquests/quests/lang/en_us.snbt
NOT the 'title'/'description' fields embedded in chapter data.
This script is intentionally idempotent and never changes any quest, task or reward ID.
"""
from __future__ import annotations

import hashlib
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
QUESTDIR = ROOT / "config/ftbquests/quests"
SPEC = json.loads((ROOT / "manifest/quest_chapters.json").read_text(encoding="utf-8"))
AUTHOR = json.loads((ROOT / "manifest/quest_content.json").read_text(encoding="utf-8"))
RESOLVED = json.loads((ROOT / "manifest/quest_content_resolved.json").read_text(encoding="utf-8"))
BY_AUTHOR = {(q["chapter"], q["title"]): q for q in AUTHOR["quests"]}
BY_ID = {q["quest_id"]: q for q in RESOLVED["quests"]}
BLOCK = re.compile(r"\n\t\t\{\n.*?\n\t\t\}", flags=re.S)
INLINE_TITLE = re.compile(r'\n\t\t\ttitle: "(?:\\.|[^"\\])*"')
ROOT_TITLE = re.compile(r'\n\ttitle: "(?:\\.|[^"\\])*"')
ROOT_SUBTITLE = re.compile(r'\n\tsubtitle: "(?:\\.|[^"\\])*"')


def q(s: str) -> str:
    return json.dumps(s, ensure_ascii=False)


def layout() -> dict[str, dict[str, tuple[int, int]]]:
    result: dict[str, dict[str, tuple[int, int]]] = {}

    def put(ch: str, title: str, x: int, y: int) -> None:
        if title in result.setdefault(ch, {}):
            raise ValueError(f"Duplicate layout title {ch} / {title}")
        result[ch][title] = (x, y)

    intro = {
        "Welcome to Too Much Magic": (0, 0),
        "Punch a Tree": (0, 3),
        "Make a Crafting Table": (0, 6),
        "Make a Wooden Pickaxe": (0, 9),
        "Gather Stone": (0, 12),
        "Upgrade to Stone": (0, 15),
        "Find Your First Iron": (0, 18),
        "Iron Pickaxe": (0, 21),
        "A Place for Your Things": (4, 6),
        "Light the Furnace": (4, 12),
        "Begin a Library": (8, 6),
        "Build a Bookshelf": (8, 9),
        "Iron Is Only the Beginning": (4, 21),
        "Find an Everyday Magical Shortcut": (8, 21),
        "Try a First Spell or Ritual": (8, 24),
        "A Little Protection": (4, 24),
        "Take Your Magic Into the World": (4, 27),
        "The Rest of the Pack Is Yours": (8, 27),
    }
    for title, xy in intro.items():
        put("Getting Started", title, *xy)

    tips = [
        ("Tips & Tricks Index", 0, 0),
        ("Find Recipes with JEI", 0, 4),
        ("Identify Blocks with Jade", 3, 4),
        ("Sort Your Inventory", 6, 4),
        ("Use Crafting Tweaks", 9, 4),
        ("Search the Controls Screen", 12, 4),
        ("Gather Your Guidebooks", 0, 8),
        ("Curios and Portable Bags", 3, 8),
        ("Claim Your Base", 6, 8),
        ("Understand Lootr Chests", 9, 8),
        ("Plan for Death", 12, 8),
        ("Search Biomes", 0, 12),
        ("Resolve Recipe Conflicts", 3, 12),
        ("Understand Character Growth", 6, 12),
        ("Zoom and Overlay Controls", 9, 12),
        ("Use the Quest Shop", 12, 12),
        ("Smelt Without Fuel: Scriptor Lectern", 0, 17),
        ("Double Raw Ore: Occultism or Mahou?", 3, 17),
        ("Need Early Flight? Compare Brooms", 6, 17),
        ("Vengeance Pickaxe: Fortune with a Price", 9, 17),
    ]
    for title, x, y in tips:
        put("Tips & Tricks", title, x, y)

    food = [
        ("Food & Fishing Index", 0, 0),
        ("Start with Seeds", 0, 4),
        ("Prepare a Field", 3, 4),
        ("Bring in the Wheat", 6, 4),
        ("A Patch of Sugar Cane", 0, 8),
        ("Pam's HarvestCraft: Starfruit", 3, 8),
        ("Find the Seed Market", 6, 8),
        ("Raise a Chicken Dinner", 0, 12),
        ("Gather Eggs", 3, 12),
        ("Fresh Milk", 6, 12),
        ("Light a Campfire", 9, 12),
        ("Pam's HarvestCraft: Fried Chicken", 12, 12),
        ("Farmer's Delight: Cutting Board", 9, 4),
        ("Farmer's Delight: Cooking Pot", 12, 4),
        ("Cooking for Blockheads: Cooking Table", 15, 4),
        ("Cooking for Blockheads: Oven", 18, 4),
        ("Ready Your Fishing Rod", 0, 17),
        ("Catch a Cod", 3, 17),
        ("Catch a Salmon", 3, 21),
        ("Aquaculture: Iron Rod", 6, 17),
        ("Aquaculture: Gold Rod", 9, 17),
        ("Aquaculture: Neptunium Rod", 12, 17),
    ]
    for title, x, y in food:
        put("Food & Fishing", title, x, y)

    basic = [
        ("Chapter Overview", 0, 0),
        ("Choose a Magical Tradition", 3, 0),
        ("Secure Some Copper", 0, 4),
        ("Prospect for Gold", 3, 4),
        ("Learn to Recognize Lapis", 6, 4),
        ("Collect Amethyst", 9, 4),
        ("Bring a Water Bucket", 12, 4),
        ("Defend Yourself", 0, 8),
        ("Upgrade to Diamond Mining", 3, 8),
        ("Reach the Nether for Ingredients", 6, 8),
        ("Gather Ender Pearls", 9, 8),
    ]
    for title, x, y in basic:
        put("Basic Progression", title, x, y)
    suffix = ["I — First Trade", "II — Regular Buyer", "III — Packed Shipment", "IV — Industrial Exchange"]
    for row, commodity in enumerate(("Copper", "Emeralds", "Diamonds")):
        for col, tier in enumerate(suffix):
            put("Basic Progression", f"Sell {commodity} {tier}", 3 * col, 14 + row * 4)

    advanced = [
        "Chapter Overview", "Map Out Your Infrastructure", "Install an Enchanting Table",
        "Bring an Anvil", "Build a Brewing Stand", "Build a Portal Reserve",
        "Keep an Ender Chest", "Recover Ancient Debris Products", "Construct a Lodestone",
        "Understand Summoning Rituals", "Prepare a Protected Ritual Chamber",
    ]
    adv_coords = [
        (0, 0), (3, 0), (0, 4), (3, 4), (6, 4), (9, 4),
        (0, 8), (3, 8), (6, 8), (9, 8), (9, 12),
    ]
    for title, xy in zip(advanced, adv_coords, strict=True):
        put("Advanced Progression", title, *xy)

    road = [
        "Roadmap: Read Before the Endgame", "Roadmap: The End and Draconic Materials",
        "Roadmap: Cataclysm Boss Resources", "Roadmap: Occultism's Final Miner",
        "Roadmap: Chaos Guardian as the Gate", "Roadmap: What Unlocks Afterwards",
    ]
    put("Endgame Roadmap", "Chapter Overview", 0, 0)
    for i, title in enumerate(road):
        put("Endgame Roadmap", title, (i % 3) * 4, 4 + (i // 3) * 4)

    skills = [
        "Read the Two Skill Systems", "Open the Skill Interface", "Understand Attributes",
        "Experiment with Improvable Skills", "Inspect Cooldown and Casting Perks",
        "Plan for Practical Attribute Caps", "Choose How to Specialize",
    ]
    put("Skills & Advancement", "Chapter Overview", 0, 0)
    for i, title in enumerate(skills):
        put("Skills & Advancement", title, (i % 3) * 4, 4 + (i // 3) * 4)

    tech = "A Sufficiently Advanced Magic"
    put(tech, "Chapter Overview", 0, 0)
    put(tech, "Tech Overview: Energy Without a Tech Pack", 4, 0)
    energy = [
        "Craft a Draconic Generator", "Craft the Draconic Energy Core",
        "Prepare Energy Core Stabilizers", "Build Input and Output Pylons",
        "Activate the Draconic Power Bank", "Route FE with a Crystal",
    ]
    for title, xy in zip(energy, [(0, 4), (3, 4), (6, 4), (9, 4), (9, 8), (6, 8)], strict=True):
        put(tech, title, *xy)
    reactor = [
        "Regulate Power with a Flux Gate", "Acquire the Draconic Reactor Core",
        "Four Reactor Stabilizers", "Build the Reactor Energy Injector",
        "Plan Safe Reactor Containment", "Operate a Draconic Reactor Safely",
    ]
    for title, xy in zip(reactor, [(0, 14), (3, 14), (6, 14), (9, 14), (9, 18), (6, 18)], strict=True):
        put(tech, title, *xy)
    return result


SUBTITLE = {
    "Getting Started": "First tools and shelter, then a practical magic path of your choosing.",
    "Tips & Tricks": "Ore yield, fuel-free smelting, brooms, portable crafting, controls and storage.",
    "Food & Fishing": "Farming, fishing, food preparation and modular kitchens.",
    "Basic Progression": "Shared resources and tools, plus separate player commodity-selling contracts.",
    "Advanced Progression": "Enchanting, brewing, storage, ritual space and exploration supplies.",
    "Endgame Roadmap": "Read-only late-game map; actual boss objectives live in other chapters.",
    "Skills & Advancement": "Pufferfish skills, attributes and Improvable Skills progression.",
    "A Sufficiently Advanced Magic": "Magical FE sources, Draconic storage and endgame reactor.",
}


def find_array_end(s: str, start: int) -> int:
    depth = 0
    quote = False
    escaped = False
    for i in range(start, len(s)):
        char = s[i]
        if quote:
            if escaped:
                escaped = False
            elif char == "\\":
                escaped = True
            elif char == '"':
                quote = False
            continue
        if char == '"':
            quote = True
        elif char == "[":
            depth += 1
        elif char == "]":
            depth -= 1
            if depth == 0:
                return i
    raise ValueError("Unclosed description array")


def rewrite_quest_block(block: str, id_to_chapter: dict, positions: dict) -> tuple[str, str, tuple[int, int], bool]:
    m = re.search(r'\n\t\t\tid: "([0-9A-F]{16})"', block)
    if m is None:
        raise ValueError(f"Quest has no valid ID: {block[:120]!r}")
    qid = m.group(1)
    chapter = id_to_chapter[qid]
    ledger = BY_ID.get(qid)
    if ledger is not None:
        title = ledger["title"]
        item = BY_AUTHOR.get((chapter, title))
        if item is None or not isinstance(item.get("description"), str):
            raise ValueError(f"Missing authored quest description: {chapter} / {title}")
        description = [item["description"]]
    else:
        title = "Chapter Overview"
        description = [
            f"An introduction to {chapter} in Too Much Magic.",
            "This chapter's full questline is still being written. This optional overview does not unlock or gate any other chapter.",
        ]
    if any(not x or x.strip() == "undefined" for x in description):
        raise ValueError(f"Broken localized description: {chapter} / {title}")
    if ledger is not None and "coin" in ledger.get("reward", {}):
        expected = f'command: "hqs coins add {{p}} {ledger["reward"]["coin"]}", permission_level: 2, team_reward: true'
        if expected not in block or 'type: "questshop:coins"' in block:
            raise ValueError(f"Unsupported coin reward ABI on {chapter} / {title}: use native command reward")
    start = block.find("\n\t\t\tdescription:")
    if start >= 0:
        open_bracket = block.find("[", start)
        if open_bracket < 0:
            raise ValueError(f"Unbracketed description {qid}")
        end_bracket = find_array_end(block, open_bracket)
        block = block[:start] + block[end_bracket + 1:]
    block = INLINE_TITLE.sub("", block)
    if "\n\t\t\tdescription:" in block or "\n\t\t\ttitle:" in block:
        raise ValueError(f"Old inline translation survived for {qid}")
    if chapter in positions:
        if title not in positions[chapter]:
            raise ValueError(f"No tested layout entry for {chapter} / {title}")
        xy = positions[chapter][title]
    else:
        xy = (0, 0)
    block, nx = re.subn(r"\n\t\t\tx: -?[\d.]+d", f"\n\t\t\tx: {xy[0]:.1f}d", block, count=1)
    block, ny = re.subn(r"\n\t\t\ty: -?[\d.]+d", f"\n\t\t\ty: {xy[1]:.1f}d", block, count=1)
    if nx != 1 or ny != 1:
        raise ValueError(f"Missing FTB quest position for {qid}")
    if title in {"Chapter Overview", "Welcome to Too Much Magic", "The Rest of the Pack Is Yours"} and "\n\t\t\tsize:" not in block:
        block = block.replace("\n\t\t\ttasks:", "\n\t\t\tsize: 1.5d\n\t\t\ttasks:", 1)
    loc = f"\tquest.{qid}.title: {q(title)}\n\tquest.{qid}.quest_desc: {json.dumps(description, ensure_ascii=False)}"
    return block, loc, xy, ledger is not None


def update_packwiz(changed: dict[Path, str]) -> None:
    index_path = ROOT / "index.toml"
    index = index_path.read_text(encoding="utf-8")
    for path in sorted(changed):
        key = path.relative_to(ROOT).as_posix()
        value = hashlib.sha256(changed[path].encode("utf-8")).hexdigest()
        regex = re.compile(r'(file = "' + re.escape(key) + r'"\nhash = ")[0-9a-f]+(")')
        if regex.search(index):
            index = regex.sub(lambda m: m.group(1) + value + m.group(2), index, count=1)
        elif key.endswith("/lang/en_us.snbt"):
            index += f'\n[[files]]\nfile = "{key}"\nhash = "{value}"\n'
        else:
            raise ValueError(f"Packwiz omitted existing config file: {key}")
    index_path.write_text(index, encoding="utf-8")
    pack_path = ROOT / "pack.toml"
    pack = pack_path.read_text(encoding="utf-8")
    pack = re.sub(r'^version = ".*"$',
                  'version = "0.8.9-quest-vampire-coin-qa"',
                  pack, count=1, flags=re.M)
    new_hash = hashlib.sha256(index.encode("utf-8")).hexdigest()
    pack, n = re.subn(
        r'(\[index\]\nfile = "index.toml"\nhash-format = "sha256"\nhash = ")[0-9a-f]+(")',
        lambda m: m.group(1) + new_hash + m.group(2), pack, count=1)
    if n != 1:
        raise ValueError("pack.toml index pointer not found")
    pack_path.write_text(pack, encoding="utf-8")


def main() -> None:
    positions = layout()
    changes: dict[Path, str] = {}
    all_quest_translations: list[str] = []
    contents = []
    seen = set()
    locs = {}
    intro_count = 0
    for chapter in SPEC["chapters"]:
        chapter_path = QUESTDIR / "chapters" / chapter["filename"]
        original = chapter_path.read_text(encoding="utf-8")
        matches = list(BLOCK.finditer(original))
        if not matches:
            raise ValueError(f"No quest objects in {chapter['name']}")
        translations = []
        replacements = []
        occupied = {}
        for match in matches:
            old = match.group()
            m = re.search(r'\n\t\t\tid: "([0-9A-F]{16})"', old)
            if m is None or m.group(1) in seen:
                raise ValueError(f"Duplicate/broken quest ID in {chapter['name']}")
            qid = m.group(1)
            seen.add(qid)
            locs[qid] = chapter["name"]
            new, loc, xy, is_authored = rewrite_quest_block(
                old, {qid: chapter["name"]}, positions)
            key = tuple(xy)
            if key in occupied:
                raise ValueError(f"Overlapping quest layout in {chapter['name']}: {occupied[key]} and {qid}")
            occupied[key] = qid
            translations.append(loc)
            replacements.append((old, new))
            contents.append({"quest_id": qid, "chapter": chapter["name"],
                             "position": [xy[0], xy[1]], "authored": is_authored})
            intro_count += is_authored
        for old, new in replacements:
            if old not in original:
                raise ValueError(f"Cannot replace quest in {chapter['name']}")
            original = original.replace(old, new, 1)
        original = ROOT_TITLE.sub("", ROOT_SUBTITLE.sub("", original))
        if re.search(r'\n\t\t\t(?:description|title):|\n\t(?:title|subtitle):', original):
            raise ValueError(f"Old ignored title field left in {chapter_path.name}")
        if "undefined" in original:
            raise ValueError(f"Invalid undefined value left in {chapter_path.name}")
        changes[chapter_path] = original
        all_quest_translations.extend(translations)

    if len(seen) != 179 or intro_count != 137:
        raise ValueError(f"Expected 179 objects / 137 authored, got {len(seen)} / {intro_count}")
    chapter_translations = []
    group_translations = []
    for chapter in SPEC["chapters"]:
        chapter_translations.append(f'\tchapter.{chapter["chapter_id"]}.title: {q(chapter["name"])}')
        subtitle = SUBTITLE.get(chapter["name"], f"Explore {chapter['name']}; more quests are being authored.")
        chapter_translations.append(
            f'\tchapter.{chapter["chapter_id"]}.chapter_subtitle: {json.dumps([subtitle], ensure_ascii=False)}')
    for group in SPEC["groups"]:
        group_translations.append(f'\tchapter_group.{group["id"]}.title: {q(group["name"])}')
    changes[QUESTDIR / "lang/en_us.snbt"] = "{\n" + "\n".join(group_translations + chapter_translations + all_quest_translations) + "\n}\n"

    for path, content in changes.items():
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(content, encoding="utf-8")
    update_packwiz({path: value for path, value in changes.items() if path.is_relative_to(QUESTDIR)})

    ledger = {
        "schema_version": 1,
        "source": "scripts/rebuild_quest_ui.py",
        "source_of_titles": "manifest/quest_content.json and manifest/quest_content_resolved.json",
        "notes": "Fixed IDs and unified FTB 2101.x locale translations; manual stage-based layouts. Not a Prism client rendering test.",
        "quest_count": len(contents),
        "chapters": len(SPEC["chapters"]),
        "positions": contents,
    }
    (ROOT / "manifest/quest_layouts.json").write_text(
        json.dumps(ledger, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    print(f"PASS: generated one FTB Quests en_us.snbt for 45 chapters; {len(seen)} named quests, {intro_count} authored")
    print("PASS: quest, task and reward IDs preserved; inline title/description/undefined removed")
    print("PASS: new chapter layouts, five labeled groups, packwiz hash chain updated")


if __name__ == "__main__":
    main()
