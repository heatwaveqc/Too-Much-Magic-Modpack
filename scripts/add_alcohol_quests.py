#!/usr/bin/env python3
"""Author Alcohol is Magic as a real FTB Quests chapter in the pinned TMM book.

Run from the repo after B13 has been merged. The chapter is a complete
instructional map for the three existing drink mods, not another mod install.
Existing quest IDs and reward sources are preserved, and all additions
pass the existing uniqueness/reward policies.
"""
from __future__ import annotations

import json
import re
import subprocess
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SPEC_PATH = ROOT / "manifest/quest_chapters.json"
CONTENT_PATH = ROOT / "manifest/quest_content.json"
RESOLVED_PATH = ROOT / "manifest/quest_content_resolved.json"
REBUILD = ROOT / "scripts/rebuild_quest_ui.py"
VALIDATE = ROOT / "scripts/validate_quests.py"
CHAPTER = ROOT / "config/ftbquests/quests/chapters/alcohol_is_magic.snbt"
ARCH = ROOT / "docs/QUEST-ARCHITECTURE.md"
TITLE = "Alcohol is Magic"
CHAPTER_ID = "A1C0000000000000"
GROUP_ID = "544D101000000000"

# Criterion types are honest: an item task verifies possession, not successful
# fermentation. Manual checkmarks instruct the player to verify operation.
# Optional side branches must never block the main path.
# (title, description, criterion item or None, depends title or None,
#  optional, x, y, item reward, quantity, XP, icon)
QUESTS = [
    (
        "Welcome to the Magic of Alcohol",
        "This chapter teaches three DIFFERENT installed drink systems: Brewin' and Chewin' ferments liquids and foods in a Keg; Cocktails Delight adds spirits and mixed drinks, some conditional on other food mods; Beachparty mixes tropical cocktails at a Palm Bar, with a Mini Fridge for ice. Food & Fishing covers farms and meals, while Advanced Progression covers the vanilla Brewing Stand. No separate brewmaster book is required. Read the branches, then check this orientation quest. Task: manual acknowledgment, not a recipe check.",
        None, None, True, 0, 0, "minecraft:glass_bottle", 4, 5, "brewinandchewin:keg",
    ),
    (
        "Read the Brewer's In-Game Reference",
        "Brewin' and Chewin' has NO physical guidebook. Open Advancements (L by default, or your custom keybinding), find the Brewin' and Chewin' tab marked with a beer icon, and browse its Keg progression. For a Keg item use JEI recipe lookup (R) and usages (U); search 'Keg', 'Fermenting' and 'Keg Pouring'. Cocktails Delight recipes are in JEI, including optional integrations; Beachparty uses JEI's Palm Bar Mixing and Mini Fridge Freezing categories. Task: manual check after you open the references; this does not prove brewing.",
        None, "Welcome to the Magic of Alcohol", False, 3, 0, "minecraft:paper", 2, 10, "minecraft:book",
    ),
    (
        "Build a Keg",
        "The Keg is Brewin' and Chewin's main workstation. In a crafting table put three planks across the bottom, iron ingots at top-left/top-right and center-left/center-right, a plank at top-middle, and a honeycomb at center-middle (recipe pattern i-p-i / i-h-i / p-p-p). Any vanilla plank-tag wood qualifies. Place the Keg in a safe workroom and open its inventory. JEI: brewinandchewin:keg recipe. Task: acquire a Keg; this detects possession, not placement.",
        "brewinandchewin:keg", "Read the Brewer's In-Game Reference", False, 6, 0, "minecraft:honeycomb", 2, 20, "brewinandchewin:keg",
    ),
    (
        "Make Your Serving Tankards",
        "Craft Tankards from planks and iron nuggets: top row plank/empty/plank, middle iron nugget/empty/iron nugget, bottom three planks; one craft yields four Tankards. Place empties in the Keg's finished-fluid serving slot, or use the correct container directly on the Keg, to take brewed drinks. The output preview tells you which container fits. JEI: brewinandchewin:tankard. Task: acquire a Tankard (the recipe produces four).",
        "brewinandchewin:tankard", "Build a Keg", False, 9, 0, "minecraft:glass_bottle", 4, 15, "brewinandchewin:tankard",
    ),
    (
        "Prime the Keg and Read Its Temperature",
        "Open the placed Keg. The top four slots hold ingredients; the separate container slot fills the base-fluid tank with water, honey or milk as the JEI Fermenting recipe requires. A Keg stores up to 1000 mB (one bucket). Temperature comes from the environment/nearby blocks and can determine whether the recipe progresses. Match the recipe's full fluid amount, ingredients and temperature; monitor its progress before serving into the indicated container. Task: manually confirm you saw a fermentation progress indicator. Holding a Keg alone does not count.",
        None, "Make Your Serving Tankards", False, 12, 0, "minecraft:bucket", 1, 20, "minecraft:water_bucket",
    ),
    (
        "First Brew: Beer",
        "In JEI, select Brewin' and Chewin's Beer Fermenting recipe. One 1000 mB water batch uses wheat dough via the c:foods/dough/wheat tag (NOT just raw wheat), wheat seeds and a brown mushroom; leave the unused fourth ingredient slot empty. Use Farmer's Delight / JEI to craft an accepted dough ingredient. Fill the Keg, adjust temperature if JEI requires it, watch fermentation finish, then fill Tankards. Task: manual check after you actually pour Beer; the quest does not falsely infer brewing from item possession.",
        None, "Prime the Keg and Read Its Temperature", False, 15, 0, "minecraft:glass_bottle", 4, 30, "brewinandchewin:beer_tankard",
    ),
    (
        "A Honey Mead Batch",
        "For a second recipe route, select Mead in JEI's Fermenting recipes: use 1000 mB honey as the base fluid plus wheat, wheat seeds and sweet berries (the fourth ingredient slot is empty). Honey is a FLUID input rather than a sugar substitute; obtain it from supported containers. Confirm the recipe's temperature and pour into its required serving container. Task: manual completion after preparing and serving Mead. This is an optional fermentation specialization, not a gate for other drinks.",
        None, "Prime the Keg and Read Its Temperature", True, 15, 4, "minecraft:honey_bottle", 2, 25, "minecraft:honey_bottle",
    ),
    (
        "A Keg Makes Food, Too",
        "A brewery needn't produce only alcohol. The same Keg ferments cheese and fudge. JEI: Fermenting -> Cocoa Fudge uses 250 mB milk, sugar, and two cocoa beans, and requires temperature 2 in the upstream 1.21.1 recipe. Cheeses have their own serving requirements; when the fluid/output indicator requests Honeycomb, use it rather than a Tankard. See JEI's individual recipes and Farmer's Delight Cutting Board usages for slicing prepared cheese. Task: manual check after successfully producing ONE edible fermented food; optional.",
        None, "Prime the Keg and Read Its Temperature", True, 15, 8, "minecraft:cocoa_beans", 4, 25, "minecraft:milk_bucket",
    ),
    (
        "Transfer a Full Keg",
        "A Keg may be carried with liquid still inside, and its capacity is 1000 mB. Use this to move a prepared batch from a brewing shed to a bar. First inspect the stored fluid before breaking or moving it, then place the Keg and confirm its contents remain. Do not assume ingredients or finished servings survive all pickup methods; test safely with an ordinary batch. Task: manual confirmation of successful fluid transport. Optional utility trick.",
        None, "Prime the Keg and Read Its Temperature", True, 18, 4, "minecraft:barrel", 1, 20, "minecraft:barrel",
    ),
    (
        "Cocktails Delight: Learn the Two Workflows",
        "Cocktails Delight EXTENDS the Keg with new fermented spirits and uses ordinary crafting recipes for many mixed cocktails. In JEI, find the Fermenting and Keg Pouring categories for spirits, then press U on the obtained drink item to see its crafting-table cocktail uses. Many drinks work with vanilla and Farmer's Delight; others are conditional on external crop/item tags. Do not assume every advertised drink exists in this pack. Task: manual check after inspecting at least one fermentation and one mixed-drink recipe; optional branch.",
        None, "Read the Brewer's In-Game Reference", True, 3, 6, "minecraft:glass_bottle", 4, 10, "minecraft:glass_bottle",
    ),
    (
        "Ferment White Rum",
        "Cocktails Delight's White Rum is brewed in the Brewin' and Chewin' Keg, NOT at Beachparty's Palm Bar. Upstream Fermenting recipe: 1000 mB water, THREE sugar cane and ONE sugar; fermentation time 8400 ticks and required temperature value 4. Check JEI for which conditions achieve that heat in this version. Pour the finished rum using Keg Pouring into the serving item indicated by JEI. Task: acquire cocktailsdelight:rum; an item check does not confirm HOW the drink was brewed. Optional if you prefer the tropical route.",
        "cocktailsdelight:rum", "Cocktails Delight: Learn the Two Workflows", True, 6, 6, "minecraft:sugar", 4, 35, "cocktailsdelight:rum",
    ),
    (
        "Mix a Drink and Check Its Side Effects",
        "A spirit is usually an INPUT to a cocktail, not the finished mixed drink. Press U on the White Rum or another Cocktails Delight ingredient in JEI and select a recipe available with this pack's crops, bottle count and crafting table. Craft a mixed drink and inspect its tooltip before drinking. Many alcoholic drinks trigger Brewin' and Chewin's Tipsy/Intoxication; some Cocktails Delight recipes grant combat or XP effects. Avoid stacking effects during XP/skill testing until compatibility has been audited. Task: manual completion after mixing one available drink; optional.",
        None, "Ferment White Rum", True, 9, 6, "minecraft:cookie", 2, 25, "minecraft:potion",
    ),
    (
        "The Piña Colada Ingredient Puzzle",
        "Yes, there IS a Piña Colada item in Cocktails Delight, but its recipe is CONDITIONAL. In JEI search cocktailsdelight:pina_colada. The upstream recipe is shapeless: TWO items under c:foods/pineapple_item, ONE item under c:foods/coconut, ONE White Rum and FOUR glass bottles; it yields four cocktails. It is registered only when BOTH food tags are nonempty. Beachparty coconuts and Pam's crops must actually populate those common tags; do not assume they do. If JEI lacks the recipe, record the missing-tag incompatibility instead of chasing an impossible craft. Task: manual check after verifying the available recipe or identifying the tag gap; optional, NEVER blocks the chapter.",
        None, "Mix a Drink and Check Its Side Effects", True, 12, 6, "minecraft:flower_pot", 2, 15, "minecraft:glass_bottle",
    ),
    (
        "Find Coconuts and Palm Wood",
        "Beachparty's tropical work starts on a coast with naturally generated palms. Gather Palm Logs and coconuts hanging beneath foliage; turn logs into Palm Planks. Coconuts can be opened for drink ingredients and can help produce palm sprouts on suitable sand. The beach biome and worldgen are a discovery route, not a hard quest lock: bring home saplings if you want a base elsewhere. Use JEI on Palm Planks and Coconut Open. Task: manual check when you have examined a palm and obtained its useful materials.",
        None, "Read the Brewer's In-Game Reference", True, 3, 12, "minecraft:sand", 8, 15, "beachparty:coconut_open",
    ),
    (
        "Construct a Palm Bar",
        "Beachparty's Palm Bar is a separate cocktail mixer; it does NOT ferment your Keg's spirits. Craft it using SEVEN Palm Planks arranged ### / ##(empty) / ##(empty) in a crafting grid; the recipe yields three Palm Bars. Place one in your bungalow, open it, and inspect JEI's Palm Bar Mixing recipes. Task: acquire beachparty:palm_bar, which detects possession rather than an actual successful cocktail.",
        "beachparty:palm_bar", "Find Coconuts and Palm Wood", False, 6, 12, "minecraft:glass_bottle", 4, 25, "beachparty:palm_bar",
    ),
    (
        "Build a Mini Fridge for Ice",
        "For Beachparty cocktails, the Mini Fridge makes ice without requiring a frozen biome. Craft a Mini Fridge from FIVE iron ingots and ONE redstone (pattern III / IRI), then put a WATER potion bottle in it: the Mini Fridge Freezing recipe produces Ice after 1200 ticks (about a minute). Verify the JEI Freezing recipe and collect the Ice for Palm Bar Mixing. Task: acquire beachparty:mini_fridge; you must separately test that freezing works.",
        "beachparty:mini_fridge", "Construct a Palm Bar", False, 9, 12, "minecraft:glass_bottle", 4, 25, "beachparty:mini_fridge",
    ),
    (
        "Serve a Coconut Cocktail",
        "At the Palm Bar, mix ONE Open Coconut (beachparty:coconut_open), ONE block of Ice and ONE Honey Bottle. JEI category: Palm Bar Mixing -> Coconut Cocktail. This makes beachparty:coconut_cocktail, a refreshing tropical drink with a temporary Strength effect. It is DISTINCT from Cocktails Delight's conditional Piña Colada, and does NOT need rum or a Keg. Task: acquire the Coconut Cocktail; possession doesn't verify drinking it.",
        "beachparty:coconut_cocktail", "Build a Mini Fridge for Ice", False, 12, 12, "minecraft:honeycomb", 3, 30, "beachparty:coconut_cocktail",
    ),
    (
        "Place, Sip, and Compare Effects",
        "Beachparty cocktails are placeable drinks. Set a Coconut Cocktail on a table/counter and interact with it to sip servings; the upstream wiki describes three placed servings (30 seconds each), compared with consuming it directly. Other Palm Bar flavors have their own effects: Sweet Berries (Absorption), Cocoa (Regeneration), Pumpkin (Fire Resistance), Honey (Haste), Melon (Luck). Check the effects and durations in this pinned client. Task: manual check after placing a drink and taking a serving. Optional: do not require alcohol consumption to finish the book.",
        None, "Serve a Coconut Cocktail", True, 15, 12, "minecraft:sea_pickle", 2, 15, "beachparty:coconut_cocktail",
    ),
    (
        "Open the Beach Bungalow",
        "Final open-ended build: set up a Palm Bar, Mini Fridge and a stocked Coconut Cocktail table at a coastal bungalow. Optionally include a Keg, Tankards, cheese and a Cocktails Delight mixing area; provide a lounge chair, fishing spot and a safe storage barrel. Aquaculture 2 supplies fishing, Farmer's Delight and Pam's provide food, and Let's Do Furniture supplies extra décor. You do NOT need to craft all 40+ Cocktails Delight recipes or obtain conditional missing ingredients. Task: manual check after your functioning beach bar is assembled; chapter complete!",
        None, "Serve a Coconut Cocktail", True, 18, 12, "minecraft:lantern", 2, 45, "beachparty:palm_bar",
    ),
]

def write_json(path: Path, obj: object) -> None:
    path.write_text(json.dumps(obj, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")

def ident(prefix: str, n: int) -> str:
    return prefix + f"{n:012X}"

def replace_required(s: str, a: str, b: str, desc: str) -> str:
    assert a in s, f"Expected {desc} source contract was missing"
    return s.replace(a, b, 1)

def patch_tool_scripts() -> None:
    p = REBUILD
    s = p.read_text(encoding="utf-8")
    if 'put("Alcohol is Magic"' not in s:
        anchor = '    basic = ['
        rows = ["    alcohol = ["]
        for title, _, _, _, _, x, y, *_ in QUESTS:
            rows.append(f"        ({json.dumps(title, ensure_ascii=False)}, {x}, {y}),")
        rows += ['    ]', '    for title, x, y in alcohol:',
                 '        put("Alcohol is Magic", title, x, y)', '', '']
        s = replace_required(s, anchor, "\n".join(rows) + anchor, "alcohol layout")
        s = replace_required(s, '    "Food & Fishing": "Farming, fishing, food preparation and modular kitchens.",',
             '    "Food & Fishing": "Farming, fishing, food preparation and modular kitchens.",\n'
             '    "Alcohol is Magic": "Fermentation, cocktails, tropical bars and beverage effects.",',
             "chapter subtitle")
        s = replace_required(s, 'elif key.endswith("/lang/en_us.snbt"):',
             'elif key.endswith("/lang/en_us.snbt") or key == "config/ftbquests/quests/chapters/alcohol_is_magic.snbt":',
             "new chapter packwiz index")
        s = replace_required(s, 'version = "0.8.9-quest-vampire-coin-qa"',
             'version = "0.9.1-alcohol-is-magic-quests-qa"', "pack version")
        s = replace_required(s, "if len(seen) != 179 or intro_count != 137:",
             "if len(seen) != 198 or intro_count != 156:", "quest count gate")
        s = s.replace('Expected 179 objects / 137 authored', 'Expected 198 objects / 156 authored')
        s = s.replace("for 45 chapters;", "for 46 chapters;")
        p.write_text(s, encoding="utf-8")

    p = VALIDATE
    s = p.read_text(encoding="utf-8")
    if '"Alcohol is Magic": 19' not in s:
        changes = [
            ('"Introduction": 3,', '"Introduction": 4,'),
            ('len(groups) == 5 and len(chapters) == 45', 'len(groups) == 5 and len(chapters) == 46'),
            ('== 137, "Expected 137 authored quests', '== 156, "Expected 156 authored quests'),
            ('== 179,\n           f"Expected 179 localized quests', '== 198,\n           f"Expected 198 localized quests'),
            ('len(lang_ids) == 179', 'len(lang_ids) == 198'),
            ('"Food & Fishing": 22,', '"Food & Fishing": 22, "Alcohol is Magic": 19,'),
            ('"Getting Started", "Tips & Tricks", "Food & Fishing", "Basic Progression",',
             '"Getting Started", "Tips & Tricks", "Food & Fishing", "Alcohol is Magic", "Basic Progression",'),
            ('len(all_blocks) == 179, f"Expected 179 quests',
             'len(all_blocks) == 198, f"Expected 198 quests'),
            ('len(item_tasks) == 80, f"Unexpected unique item-task count',
             'len(item_tasks) == 86, f"Unexpected unique item-task count'),
            ('five groups, 45 chapters', 'five groups, 46 chapters'),
        ]
        for a, b in changes:
            s = replace_required(s, a, b, "validator patch: "+a[:48])
        p.write_text(s, encoding="utf-8")

def render_snbt(rows: list[dict]) -> str:
    head = [
        "{",
        '\tdefault_hide_dependency_lines: false',
        '\tdefault_quest_shape: "rsquare"',
        '\tfilename: "alcohol_is_magic"',
        f'\tgroup: "{GROUP_ID}"',
        '\ticon: { id: "brewinandchewin:keg" }',
        f'\tid: "{CHAPTER_ID}"',
        '\timages: [ ]',
        '\torder_index: 3',
        '\tprogression_mode: "flexible"',
        '\tquest_links: [ ]',
        '\tquests: [',
    ]
    for row in rows:
        i = row["number"]
        qid = row["quest_id"]
        reward = row["reward"]
        cr = row["criterion"]
        head.extend([
            "\t\t{",
            f'\t\t\tid: "{qid}"',
            f'\t\t\ticon: {{ id: "{row["icon"]}" }}',
        ])
        if row["depends"]:
            dep_id = next(z["quest_id"] for z in rows if z["title"] == row["depends"])
            head.append(f'\t\t\tdependencies: ["{dep_id}"]')
        if row["optional"]:
            head.append("\t\t\toptional: true")
        head.extend([
            "\t\t\trewards: [",
            f'\t\t\t\t{{ id: "{row["xp_reward_id"]}", type: "xp", xp: {reward["xp"]} }}',
            f'\t\t\t\t{{ id: "{row["item_reward_id"]}", type: "item", item: {{ id: "{reward["item"]}", count: 1 }}, count: {reward["count"]} }}',
            "\t\t\t]",
            "\t\t\ttasks: [",
        ])
        if cr["type"] == "item":
            head.append(f'\t\t\t\t{{ id: "{row["task_id"]}", type: "item", item: {{ id: "{cr["id"]}", count: 1 }} }}')
        else:
            head.append(f'\t\t\t\t{{ id: "{row["task_id"]}", type: "checkmark" }}')
        head.extend([
            "\t\t\t]",
            f'\t\t\tx: {row["position"][0]:.1f}d',
            f'\t\t\ty: {row["position"][1]:.1f}d',
            "\t\t}",
        ])
    head.extend(["\t]", "}", ""])
    return "\n".join(head)

def add_chapter() -> None:
    spec = json.loads(SPEC_PATH.read_text(encoding="utf-8"))
    authored = json.loads(CONTENT_PATH.read_text(encoding="utf-8"))
    resolved = json.loads(RESOLVED_PATH.read_text(encoding="utf-8"))
    if any(c["name"] == TITLE for c in spec["chapters"]):
        print("Chapter already in canonical spec; verifying/rebuilding idempotently.")
        assert CHAPTER.is_file()
        return
    assert len(spec["chapters"]) == 45
    assert len(authored["quests"]) == len(resolved["quests"]) == 137
    assert not CHAPTER.exists()
    assert len(QUESTS) == 19
    existing_tasks = {x["criterion"]["id"] for x in authored["quests"]
                      if x["criterion"]["type"] == "item"}
    new_tasks = {q[2] for q in QUESTS if q[2]}
    assert len(new_tasks) == 6 and not new_tasks.intersection(existing_tasks), (
        "Duplicate item criteria with existing quests"
    )
    existing_ids = set()
    for obj in resolved["quests"]:
        existing_ids |= {obj["quest_id"], obj["task_id"], obj["xp_reward_id"], obj["item_reward_id"]}
    candidate_reward_items = {q[7] for q in QUESTS}
    assert not candidate_reward_items.intersection(new_tasks | existing_tasks), (
        f"Reward shortcut: {candidate_reward_items.intersection(new_tasks | existing_tasks)}"
    )
    source_mods = json.loads((ROOT / "manifest/mods.json").read_text(encoding="utf-8"))
    enabled = {x["id"] for x in source_mods["mods"] if x["enabled_in_pack"]}
    assert {"brewin-and-chewin", "cocktails-delight", "lets-do-beachparty", "farmers-delight"} <= enabled

    group = next(g for g in spec["groups"] if g["name"] == "Introduction")
    assert group["count"] == 3
    group["count"] = 4
    new_chapter = {
        "category": "Introduction", "group_id": GROUP_ID, "chapter_id": CHAPTER_ID,
        "name": TITLE, "filename": "alcohol_is_magic.snbt", "order_index": 3,
        "icon": "brewinandchewin:keg",
        "mod_ids": ["brewin-and-chewin", "cocktails-delight", "lets-do-beachparty"],
        "overview_quest_id": ident("A1C1", 1), "overview_task_id": ident("A1C2", 1),
    }
    spot = next(i for i,c in enumerate(spec["chapters"]) if c["category"]=="Progression")
    spec["chapters"].insert(spot, new_chapter)
    rows=[]
    for n, (title, desc, criterion_item, depends, optional, x, y, reward_item, qty, xp, icon) in enumerate(QUESTS, 1):
        qid, tid, xpid, itemid = (ident(prefix, n) for prefix in ("A1C1", "A1C2", "A1C3", "A1C4"))
        assert {qid, tid, xpid, itemid}.isdisjoint(existing_ids)
        existing_ids.update((qid, tid, xpid, itemid))
        cr = {"type": "item", "id": criterion_item, "count": 1} if criterion_item else {"type": "checkmark"}
        reward = {"xp": xp, "item": reward_item, "count": qty}
        authored["quests"].append({
            "chapter": TITLE, "title": title, "description": desc,
            "criterion": cr, "reward": reward, "depends": depends,
            "optional": optional, "overview": n == 1, "first_log": False,
        })
        resolved["quests"].append({
            "chapter": TITLE, "title": title,
            "quest_id": qid, "task_id": tid,
            "xp_reward_id": xpid, "item_reward_id": itemid,
            "criterion": cr, "reward": reward, "depends": depends, "optional": optional,
        })
        rows.append({
            "number": n, "title": title, "description": desc,
            "quest_id": qid, "task_id": tid,
            "xp_reward_id": xpid, "item_reward_id": itemid,
            "criterion": cr, "reward": reward, "depends": depends,
            "optional": optional, "position": (x, y), "icon": icon,
        })
    assert len({r["position"] for r in rows}) == len(rows)
    write_json(SPEC_PATH, spec)
    write_json(CONTENT_PATH, authored)
    write_json(RESOLVED_PATH, resolved)
    CHAPTER.write_text(render_snbt(rows), encoding="utf-8")

    arch = ARCH.read_text(encoding="utf-8")
    arch += (
        "\n## New brewer chapter (2026-11-15)\n\n"
        "The owner explicitly requested **Alcohol is Magic** as a separate Introduction "
        "chapter following Food & Fishing. The five-group architecture remains, "
        "but Introduction now contains four chapters, for **46 chapters total**. "
        "A 19-quest how-to teaches Brewin' and Chewin', Cocktails Delight, and "
        "Let's Do Beachparty without duplicating Food & Fishing or the vanilla "
        "Brewing Stand track. All operation-only steps use honestly described "
        "manual checkmarks; item steps detect possession, not actual usage. "
        "The Piña Colada is optional and conditional on pineapple/coconut item tags.\n\n"
    )
    ARCH.write_text(arch, encoding="utf-8")

def main() -> None:
    add_chapter()
    patch_tool_scripts()
    subprocess.run(["python3", "scripts/rebuild_quest_ui.py"], cwd=ROOT, check=True)
    subprocess.run(["python3", "scripts/validate_quests.py"], cwd=ROOT, check=True)
    subprocess.run(["python3", "scripts/validate.py"], cwd=ROOT, check=True)
    print("PASS: Alcohol is Magic, 19 substantive quest entries, 46 chapters, 198 quests.")

if __name__ == "__main__":
    main()
