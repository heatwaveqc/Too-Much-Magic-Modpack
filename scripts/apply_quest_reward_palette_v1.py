#!/usr/bin/env python3
"""One-time, idempotent TMM quest reward cleanup. Preserve ALL IDs and goals."""
from __future__ import annotations
import json
import re
from pathlib import Path
R=Path(__file__).resolve().parents[1]
A=R/"manifest/quest_content.json"
D=R/"manifest/quest_content_resolved.json"
P=R/"manifest/progression_quest_content.json"
C=R/"manifest/quest_chapters.json"
V=R/"scripts/validate_quests.py"
# Chapter, title, expected old vanilla item, new vanilla item, new quantity.
CHANGES=[
("Getting Started","Welcome to Too Much Magic","paper","glow_berries",4),
("Getting Started","Make a Crafting Table","bowl","rabbit_stew",1),
("Tips & Tricks","Tips & Tricks Index","paper","sea_pickle",2),
("Tips & Tricks","Find Recipes with JEI","paper","brush",1),
("Tips & Tricks","Use Crafting Tweaks","cookie","shears",1),
("Tips & Tricks","Search the Controls Screen","paper","glow_ink_sac",2),
("Tips & Tricks","Gather Your Guidebooks","paper","ink_sac",4),
("Tips & Tricks","Zoom and Overlay Controls","cookie","sea_pickle",2),
("Food & Fishing","Food & Fishing Index","paper","pumpkin_pie",1),
("Food & Fishing","Bring in the Wheat","cookie","rabbit_stew",1),
("Food & Fishing","Gather Eggs","cookie","pumpkin_pie",2),
("Advanced Progression","Map Out Your Infrastructure","compass","scaffolding",16),
("Advanced Progression","Construct a Lodestone","compass","glow_item_frame",1),
("Endgame Roadmap","Roadmap: Occultism's Final Miner","paper","ladder",12),
("Endgame Roadmap","Roadmap: Chaos Guardian as the Gate","compass","firework_rocket",4),
("Endgame Roadmap","Roadmap: What Unlocks Afterwards","compass","phantom_membrane",2),
("Skills & Advancement","Open the Skill Interface","paper","honey_bottle",1),
("Getting Started","Chart Your Next Journey","compass","oak_boat",1),
("Food & Fishing","Farmer's Delight: Cooking Pot","baked_potato","bowl",1),
]
# Until replaced by substantive chapters, remove paper rewards from 42
# intentionally temporary overview pages. Each receives a tiny thematic gift.
SCAFFOLD=[
("Basic Progression","glow_berries",4),("Advanced Progression","scaffolding",8),
("Endgame Roadmap","torch",8),("Skills & Advancement","honey_bottle",1),
("Exceptional Power: Reactors","firework_rocket",2),
("Ars Nouveau","glow_berries",4),("Astral Sorcery","glow_ink_sac",2),
("Botania Revived","bone_meal",4),("Eidolon: Repraised","candle",2),
("Enchanted: Witchcraft","honey_bottle",1),("EvilCraft","glow_berries",4),
("Forbidden & Arcanus","amethyst_shard",2),("Goety","bone",4),
("Hex Casting","ink_sac",2),("Hexerei","honey_bottle",1),
("Iron's Spells 'n Spellbooks","lapis_lazuli",2),("Mahou Tsukai","glow_ink_sac",2),
("Malum","soul_torch",2),("Neo Vitae","glass_bottle",2),
("Occultism","candle",2),("Pastel","amethyst_shard",2),
("Primal Magick","glow_berries",4),("Psi","slime_ball",2),
("Roots Classic","bone_meal",4),("Scriptor Magicae","ink_sac",2),
("Theurgy","glass_bottle",2),("Totemic","leather",2),
("Just Another Witchery Remake","honey_bottle",1),("Embers Re-Ignited","charcoal",4),
("Reliquary Reincarnations","gold_nugget",4),
("L_Ender's Cataclysm","firework_rocket",2),
("Deeper and Darker","glow_berries",4),
("Gateways to Eternity","slime_ball",2),("Feywild","sea_pickle",2),
("Apotheosis","lapis_lazuli",2),("Artifacts","leather",2),
("Enigmatic Legacy+","glow_ink_sac",2),("Vampirism","milk_bucket",1),
("Werewolves","bone",4),("Draconic Evolution","firework_rocket",2),
("ProjectE","slime_ball",2),("Replication","amethyst_shard",2),
]
def read(path):return json.loads(path.read_text(encoding="utf-8"))
def write(path,obj):path.write_text(json.dumps(obj,indent=2,ensure_ascii=False)+"\n",encoding="utf-8")
def main():
    a=read(A);resolved=read(D);p=read(P);spec=read(C)
    by={(q["chapter"],q["title"]):q for q in resolved["quests"]}
    assert len(CHANGES)==19 and len(SCAFFOLD)==42 and len(by)==138
    if all(q["reward"].get("item") not in
           ("minecraft:paper","minecraft:cookie","minecraft:compass","minecraft:bowl")
           for q in a["quests"] if q["title"]!="Farmer's Delight: Cooking Pot"):
        print("Already applied to authored quests; no-op")
        return
    edits={}
    for chapter,title,old,new,count in CHANGES:
        key=(chapter,title)
        rec=by[key]
        au=next(x for x in a["quests"] if (x["chapter"],x["title"])==key)
        for q in (rec,au):
            assert q["reward"]["item"]=="minecraft:"+old,(key,q["reward"])
            q["reward"]["item"]="minecraft:"+new
            q["reward"]["count"]=count
        legacy=next((x for x in p["quests"] if (x["chapter"],x["title"])==key),None)
        if legacy:
            assert legacy["reward"]["item"] in ("minecraft:"+old,"minecraft:experience_bottle"),(key,legacy["reward"])
            legacy["reward"]["item"]="minecraft:"+new
            legacy["reward"]["count"]=count
        edits[rec["quest_id"]]=("minecraft:"+old,"minecraft:"+new,count)
    owned={q["quest_id"] for q in resolved["quests"]}
    scaffold=dict((name,("minecraft:"+item,n)) for name,item,n in SCAFFOLD)
    assert len(scaffold)==42
    for chapter in spec["chapters"]:
        qid=chapter["overview_quest_id"]
        if qid in owned:continue
        new,n=scaffold[chapter["name"]]
        edits[qid]=("minecraft:paper",new,n)
    assert len(edits)==61,len(edits)
    changes=0
    outer=re.compile(r"\n\t\t\{\n.*?\n\t\t\}",re.S)
    item=re.compile(r'type: "item", item: \{ id: "([^"]+)", count: 1 \}, count: (\d+)')
    for chapter in spec["chapters"]:
        file=R/"config/ftbquests/quests/chapters"/chapter["filename"]
        s=file.read_text(encoding="utf-8")
        def repl(m):
            nonlocal changes
            b=m.group()
            found=re.search(r'\n\t\t\tid: "([A-F0-9]{16})"',b)
            if not found or found.group(1) not in edits:return b
            qid=found.group(1)
            old,new,count=edits[qid]
            st=b.index("rewards: [")
            en=b.index("tasks: [")
            sub=b[st:en]
            oldmatch=item.search(sub)
            assert oldmatch is not None and oldmatch.group(1)==old,(chapter["name"],qid,oldmatch and oldmatch.group(1),old)
            newer=item.sub(f'type: "item", item: {{ id: "{new}", count: 1 }}, count: {count}',sub,count=1)
            assert newer!=sub
            changes+=1
            return b[:st]+newer+b[en:]
        updated=outer.sub(repl,s)
        if updated!=s:file.write_text(updated,encoding="utf-8")
    assert changes==61,changes
    val=V.read_text(encoding="utf-8")
    pot=by[("Food & Fishing","Farmer's Delight: Cooking Pot")]["quest_id"]
    anchor='                    reward_items.append((qid, itm.group(1), int(itm.group(2))))'
    assert val.count(anchor)==1
    val=val.replace(anchor,'''                    ensure(itm.group(1) not in {"minecraft:paper", "minecraft:cookie", "minecraft:compass"},
                           f"Generic filler reward on {qid}: {itm.group(1)}")
                    ensure(itm.group(1) != "minecraft:bowl" or
                           (qid == "'''+pot+'''" and int(itm.group(2)) == 1),
                           f"Only Farmer's Delight Cooking Pot may reward one empty bowl: {qid}")
'''+anchor)
    V.write_text(val,encoding="utf-8")
    write(A,a);write(D,resolved);write(P,p)
    print(f"PASS: {changes} quest item gifts revised; no changed quest/task/reward IDs")
if __name__=="__main__":
    main()
