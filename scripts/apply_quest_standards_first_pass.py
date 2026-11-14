#!/usr/bin/env python3
"""B14: migrate existing quests, preserve IDs, add a native personal starter choice."""
from __future__ import annotations
import json
import re
import subprocess
import sys
from pathlib import Path

ROOT=Path(__file__).resolve().parents[1]
AUTH=ROOT/"manifest/quest_content.json"
RES=ROOT/"manifest/quest_content_resolved.json"
GEN=ROOT/"scripts/rebuild_quest_ui.py"
VAL=ROOT/"scripts/validate_quests.py"
CHAPTERS=ROOT/"config/ftbquests/quests/chapters"
STARTER_ID="544D501001300000"
RENAMES={
 "Iron Is Only the Beginning":("Prepare a Field Kit","minecraft:barrel","Make a barrel for sorting the ingredients, tools and supplies you carry between your first magical projects. This is a practical storage milestone, not a requirement to specialize in any particular school."),
 "Find an Everyday Magical Shortcut":("Build a Research Lectern","minecraft:lectern","Craft a lectern for your early magical library. This is the ordinary Minecraft lectern; Scriptor Magicae has a separate Casting Lectern for fuel-free smelting. Check JEI before confusing the two."),
 "Take Your Magic Into the World":("Scout with a Spyglass","minecraft:spyglass","Use copper and amethyst to build a spyglass for scouting structures and terrain. This is an optional exploration aid; you can still prefer base magic, rituals or farming."),
 "The Rest of the Pack Is Yours":("Chart Your Next Journey","minecraft:filled_map","Use an empty map to create a filled map of your next destination. The quest book provides independent paths through every magic school; this optional task concludes onboarding without locking your choices.")
}
BOTTLES={
 ("Getting Started","Iron Is Only the Beginning"):"minecraft:bread",
 ("Basic Progression","Learn to Recognize Lapis"):"minecraft:glowstone_dust",
 ("Basic Progression","Gather Ender Pearls"):"minecraft:blaze_powder",
 ("Advanced Progression","Bring an Anvil"):"minecraft:iron_ingot",
 ("Endgame Roadmap","Roadmap: What Unlocks Afterwards"):"minecraft:compass",
 ("Skills & Advancement","Read the Two Skill Systems"):"minecraft:golden_carrot",
 ("Skills & Advancement","Plan for Practical Attribute Caps"):"minecraft:clock",
 ("Exceptional Power: Reactors","Activate the Draconic Power Bank"):"minecraft:comparator"
}
OPTIONS=[
 "ars_nouveau:novice_spell_book",
 "irons_spellbooks:copper_spell_book",
 "minecraft:fishing_rod",
 "botania:lexicon",
 "occultism:dictionary_of_spirits",
 "minecraft:compass"
]
def one(s,old,new,label):
    n=s.count(old)
    if n!=1: raise RuntimeError(f"{label}: found {n} matches")
    return s.replace(old,new,1)
def write_json(path,obj):
    path.write_text(json.dumps(obj,ensure_ascii=False,indent=2)+"\n",encoding="utf-8")
def modify():
    a=json.loads(AUTH.read_text(encoding="utf-8"))
    r=json.loads(RES.read_text(encoding="utf-8"))
    assert len(a["quests"])==len(r["quests"])==137
    ids={q["quest_id"]:(q["chapter"],q["title"]) for q in r["quests"]}
    assert len(ids)==137
    old_titles=set(RENAMES)
    for q in a["quests"]+r["quests"]:
        title=q["title"]
        oldkey=(q["chapter"],title)
        if q["reward"].get("item")=="minecraft:experience_bottle":
            assert oldkey in BOTTLES, oldkey
            q["reward"]["item"]=BOTTLES[oldkey]
        if q["chapter"]=="Getting Started" and title in RENAMES:
            new,item,description=RENAMES[title]
            q["title"]=new
            q["criterion"]={"type":"item","id":item,"count":1}
            if "description" in q: q["description"]=description
        if q.get("depends") in RENAMES:
            q["depends"]=RENAMES[q["depends"]][0]
    aid=next(i for i,q in enumerate(a["quests"]) if q["title"]=="Make a Crafting Table")
    rid=next(i for i,q in enumerate(r["quests"]) if q["title"]=="Make a Crafting Table")
    reward={"xp":20,"choice":OPTIONS,"claim_scope":"player"}
    desc="Choose one starter gift for a path you want to try: Ars Nouveau, Iron's Spells, fishing, Botania, Occultism or exploration. This one-time personal reward does not lock a class. You may pursue every other magic mod later. An empty spellbook may still need spells or scrolls before you can cast."
    author={
      "chapter":"Getting Started","title":"Choose Your First Path",
      "description":desc,"criterion":{"type":"checkmark"},
      "reward":reward,"depends":"Make a Crafting Table",
      "optional":True,"overview":False,"first_log":False
    }
    resolved={
      "chapter":"Getting Started","title":"Choose Your First Path",
      "quest_id":STARTER_ID,"task_id":"544D801001300000",
      "xp_reward_id":"544D601001300001",
      "item_reward_id":"544D701001300002",
      "criterion":{"type":"checkmark"},"reward":reward,
      "depends":"Make a Crafting Table","optional":True
    }
    a["quests"].insert(aid+1,author)
    r["quests"].insert(rid+1,resolved)
    a["rules"]["experience_bottle"]="Forbidden item quest reward: minecraft:experience_bottle, including choice options. XP points are permitted."
    a["rules"]["manual_checkmarks"]="Allow only genuinely unobservable actions or initial information/choice; older scaffolds need conversion."
    write_json(AUTH,a)
    write_json(RES,r)
    # Existing chapter source gets new detectable task types and bottle replacements.
    pattern=re.compile(r"\n\t\t\{\n.*?\n\t\t\}",re.S)
    for file in CHAPTERS.glob("*.snbt"):
        source=file.read_text(encoding="utf-8")
        def change(match):
            block=match.group(0)
            idmatch=re.search(r'\n\t\t\tid: "([A-F0-9]{16})"',block)
            if not idmatch: return block
            key=ids.get(idmatch.group(1))
            if key is None: return block
            if key[0]=="Getting Started" and key[1] in RENAMES:
                item=RENAMES[key[1]][1]
                block=one(block,'type: "checkmark"',f'type: "item", item: {{ id: "{item}", count: 1 }}',"intro criterion")
            if key in BOTTLES:
                block=one(block,'id: "minecraft:experience_bottle"',f'id: "{BOTTLES[key]}"',"bottle item")
            return block
        file.write_text(pattern.sub(change,source),encoding="utf-8")
    intro=CHAPTERS/"intro.snbt"
    source=intro.read_text(encoding="utf-8")
    option_lines=[]
    for n,item in enumerate(OPTIONS,1):
        option_lines.append(f'\t\t\t\t\t\t{{ id: "544D9010013000{n:02X}", type: "item", item: {{ id: "{item}", count: 1 }}, count: 1 }}')
    choice=(
       '\n\t\t{\n'
       '\t\t\tid: "544D501001300000"\n'
       '\t\t\ticon: { id: "minecraft:enchanted_book" }\n'
       '\t\t\tdependencies: ["544D501000300000"]\n'
       '\t\t\toptional: true\n'
       '\t\t\trewards: [\n'
       '\t\t\t\t{ id: "544D601001300001", type: "xp", xp: 20, team_reward: false }\n'
       '\t\t\t\t{\n'
       '\t\t\t\t\tid: "544D701001300002"\n'
       '\t\t\t\t\ttype: "choice"\n'
       '\t\t\t\t\tteam_reward: false\n'
       '\t\t\t\t\tauto_claim: "disabled"\n'
       '\t\t\t\t\ttable_data: {\n'
       '\t\t\t\t\t\tloot_size: 1\n'
       '\t\t\t\t\t\trewards: [\n'
       + "\n".join(option_lines) + '\n'
       '\t\t\t\t\t\t]\n'
       '\t\t\t\t\t}\n'
       '\t\t\t\t}\n'
       '\t\t\t]\n'
       '\t\t\ttasks: [\n'
       '\t\t\t\t{ id: "544D801001300000", type: "checkmark" }\n'
       '\t\t\t]\n'
       '\t\t\tx: 4.0d\n\t\t\ty: 9.0d\n'
       '\t\t}\n'
    )
    anchor='\n\t\t{\n\t\t\tid: "544D501000400000"'
    source=one(source,anchor,choice+anchor,"choice insertion")
    intro.write_text(source,encoding="utf-8")
    gen=GEN.read_text(encoding="utf-8")
    gen=one(gen,'        "Make a Crafting Table": (0, 6),','        "Make a Crafting Table": (0, 6),\n        "Choose Your First Path": (4, 9),',"generator choice position")
    for old,(new,_,__) in RENAMES.items():
        gen=one(gen,f'"{old}": (',f'"{new}": (',"generator existing position")
    gen=one(gen,'if len(seen) != 179 or intro_count != 137:', 'if len(seen) != 180 or intro_count != 138:',"generator counts")
    gen=one(gen,'Expected 179 objects / 137 authored','Expected 180 objects / 138 authored',"generator counts label")
    gen=one(gen,'0.8.10-quest-text-format-qa','0.8.12-quest-standard-qa',"generator version")
    GEN.write_text(gen,encoding="utf-8")
    val=VAL.read_text(encoding="utf-8")
    for old,new,name in [
      ('== 137, "Expected 137 authored quests','== 138, "Expected 138 authored quests',"auth counts"),
      ('"Getting Started": 18,','"Getting Started": 19,',"intro count"),
      ('len(all_blocks) == 179','len(all_blocks) == 180',"all count"),
      ('len(item_tasks) == 80','len(item_tasks) == 84',"task count")
    ]: val=one(val,old,new,name)
    val=one(val,'    coin_quests: set[str] = set()','    coin_quests: set[str] = set()\n    choice_quests: set[str] = set()',"choice state")
    begin=val.index('            xp = re.search(r\'type: "xp"')
    finish=val.index('            for item_id, required in re.findall(',begin)
    old=val[begin:finish]
    xp_line=old[:old.index('            itm = re.search(')]
    rest=old[old.index('            itm = re.search('):]
    choice_block='''            ensure('minecraft:experience_bottle' not in rewards, f"Forbidden bottle item reward on {qid}")
            if qid == "544D501001300000":
                ensure('type: "choice"' in rewards and rewards.count('type: "choice"') == 1,
                       "Missing native starter choice reward")
                ensure('table_data:' in rewards and 'loot_size: 1' in rewards,
                       "No starter inline reward table")
                ensure(rewards.count('type: "item"') == 6, "Expected six starter options")
                ensure(rewards.count('team_reward: false') >= 2 and 'auto_claim: "disabled"' in rewards,
                       "Starter choice must be personal and manual")
                for item in (
                    "ars_nouveau:novice_spell_book", "irons_spellbooks:copper_spell_book",
                    "minecraft:fishing_rod", "botania:lexicon",
                    "occultism:dictionary_of_spirits", "minecraft:compass"
                ):
                    ensure(rewards.count(f'id: "{item}"') == 1, f"Starter option missing {item}")
                choice_quests.add(qid)
            else:
'''
    val=val[:begin]+xp_line+choice_block+"\n".join("    "+line if line else line for line in rest.split("\n"))+val[finish:]
    old='''        if "item" in q["reward"]:
            ensure(f'id: "{q["reward"]["item"]}"' in block, f"Wrong item reward in {q['title']}")
        else:'''
    new='''        if "choice" in q["reward"]:
            ensure(qid in choice_quests and 'type: "choice"' in block,
                   f"Wrong native starter choice on {q['title']}")
        elif "item" in q["reward"]:
            ensure(f'id: "{q["reward"]["item"]}"' in block, f"Wrong item reward in {q['title']}")
        else:'''
    val=one(val,old,new,"validator choice reward")
    old='    ensure(len(coin_quests) == 12, f"Expected exactly 12 intentional resource sales, found {len(coin_quests)}")'
    new=old+'''
    ensure(choice_quests == {"544D501001300000"}, "Expected one personal starter choice")
    checkmarks={q["title"] for q in authored["quests"]
                if q["chapter"]=="Getting Started" and q["criterion"]["type"]=="checkmark"}
    ensure(checkmarks=={"Welcome to Too Much Magic", "Choose Your First Path",
                        "Try a First Spell or Ritual"},
           f"Invalid introductory checkmark exceptions: {checkmarks}")
    ensure(not any(q["reward"].get("item")=="minecraft:experience_bottle"
                   for q in authored["quests"]), "Forbidden authored XP bottle grant")'''
    val=one(val,old,new,"validation policy")
    VAL.write_text(val,encoding="utf-8")
    result=subprocess.run([sys.executable,str(GEN)],cwd=ROOT,check=True,text=True,capture_output=True)
    print(result.stdout)
    result=subprocess.run([sys.executable,str(VAL)],cwd=ROOT,check=True,text=True,capture_output=True)
    print(result.stdout)
    print("PASS: starter choice, 8 XP bottle replacements and 4 detectable onboarding goals")

if __name__=="__main__":
    r=json.loads(RES.read_text(encoding="utf-8"))
    if any(q["quest_id"]==STARTER_ID for q in r["quests"]):
        print("PASS: B14 migration already applied")
    else:
        modify()
