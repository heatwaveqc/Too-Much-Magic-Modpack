#!/usr/bin/env python3
"""Compile the Ars pilot into the existing book without reallocating old IDs."""
from __future__ import annotations

import hashlib
import json
import re
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


def write(path, data):
    path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")


def main():
    pilot = json.loads((ROOT / "manifest/ars_quest_pilot.json").read_text())
    author_path = ROOT / "manifest/quest_content.json"
    resolved_path = ROOT / "manifest/quest_content_resolved.json"
    author = json.loads(author_path.read_text())
    resolved = json.loads(resolved_path.read_text())
    spec = json.loads((ROOT / "manifest/quest_chapters.json").read_text())
    chapter = next(c for c in spec["chapters"] if c["name"] == "Ars Nouveau")
    authored = []
    mapped = []
    blocks = []
    by_key = {q["key"]: q for q in pilot["quests"]}
    assert len(by_key) == len(pilot["quests"])
    for quest in pilot["quests"]:
        n = quest["number"]
        ids = {
            "quest_id": f"544DB13000{n:02X}0000",
            "task_id": f"544DB23000{n:02X}0000",
            "xp_reward_id": f"544DB33000{n:02X}0000",
        }
        if n == 0:
            ids.update(quest_id=chapter["overview_quest_id"],
                       task_id=chapter["overview_task_id"],
                       xp_reward_id="544D903000100001")
        if "item" in quest["reward"]:
            ids["item_reward_id"] = f"544DB43000{n:02X}0000"
        depends = by_key[quest["depends"]]["title"] if quest.get("depends") else None
        row = dict(chapter="Ars Nouveau", title=quest["title"],
                   description=quest["description"], criterion=quest["criterion"],
                   reward=quest["reward"], depends=depends, optional=quest["optional"])
        if quest.get("manual_rationale"):
            row["manual_rationale"] = quest["manual_rationale"]
        authored.append(row)
        mapped.append({k: v for k, v in row.items() if k not in {"description", "manual_rationale"}} | ids)
        lines = ["\t\t{", f'\t\t\tid: "{ids["quest_id"]}"',
                 f'\t\t\ticon: {{ id: "{quest["icon"]}" }}']
        if quest.get("depends"):
            parent = by_key[quest["depends"]]
            pid = chapter["overview_quest_id"] if parent["number"] == 0 else f'544DB13000{parent["number"]:02X}0000'
            lines.append(f'\t\t\tdependencies: ["{pid}"]')
        if quest["optional"]:
            lines.append("\t\t\toptional: true")
        lines += ["\t\t\trewards: [",
                  f'\t\t\t\t{{ id: "{ids["xp_reward_id"]}", type: "xp", xp: {quest["reward"]["xp"]}, team_reward: false }}']
        if "item" in quest["reward"]:
            lines.append(f'\t\t\t\t{{ id: "{ids["item_reward_id"]}", type: "item", item: {{ id: "{quest["reward"]["item"]}", count: 1 }}, count: {quest["reward"]["count"]}, team_reward: false }}')
        lines += ["\t\t\t]", "\t\t\ttasks: ["]
        task = quest["criterion"]
        if task["type"] == "item":
            payload = f'item: {{ id: "{task["id"]}", count: 1 }}, count: {task.get("count", 1)}L, consume_items: false'
        elif task["type"] == "advancement":
            payload = f'advancement: "{task["id"]}", criterion: ""'
        elif task["type"] == "checkmark":
            payload = ""
        else:
            raise ValueError(f"Unsupported task {task}")
        suffix = ", " + payload if payload else ""
        lines += [f'\t\t\t\t{{ id: "{ids["task_id"]}", type: "{task["type"]}"{suffix} }}',
                  "\t\t\t]", f'\t\t\tx: {quest["position"][0]:.1f}d',
                  f'\t\t\ty: {quest["position"][1]:.1f}d', "\t\t}"]
        blocks.append("\n".join(lines))
    for data, rows in ((author, authored), (resolved, mapped)):
        data["quests"] = [q for q in data["quests"] if q["chapter"] != "Ars Nouveau"] + rows
    author["schema_version"] = 2
    author["rules"]["each_quest_requires"] = "Positive XP; substantive objectives get useful thematic fixed items. Explicitly authored initial orientation may be XP-only."
    author["rules"]["ars_pilot"] = "manifest/ars_quest_pilot.json owns the authored Ars chapter. One initial orientation checkmark; all remaining tasks are items or advancements. No repeatable gifts or boss-resource rewards."
    write(author_path, author)
    write(resolved_path, resolved)
    path = ROOT / "config/ftbquests/quests/chapters/ars_nouveau.snbt"
    path.write_text('{\n\tdefault_hide_dependency_lines: false\n\tdefault_quest_shape: "rsquare"\n\tfilename: "ars_nouveau"\n'
                    f'\tgroup: "{chapter["group_id"]}"\n\ticon: {{ id: "ars_nouveau:novice_spell_book" }}\n\tid: "{chapter["chapter_id"]}"\n'
                    '\timages: [ ]\n\torder_index: 0\n\tprogression_mode: "flexible"\n\tquest_links: [ ]\n\tquests: [\n' +
                    "\n".join(blocks) + "\n\t]\n}\n", encoding="utf-8")
    advancement = ROOT / "kubejs/data/tmm/advancement/ars_pilot/chimera.json"
    advancement.parent.mkdir(parents=True, exist_ok=True)
    write(advancement, {"criteria": {"kill_chimera": {
        "trigger": "minecraft:player_killed_entity",
        "conditions": {"entity": {"type": "ars_nouveau:wilden_boss"}}
    }}, "requirements": [["kill_chimera"]]})
    # Register the new datapack file before rebuilding the full hash chain.
    index_path = ROOT / "index.toml"
    relative = advancement.relative_to(ROOT).as_posix()
    index = index_path.read_text()
    digest = hashlib.sha256(advancement.read_bytes()).hexdigest()
    if f'file = "{relative}"' not in index:
        index += f'\n[[files]]\nfile = "{relative}"\nhash = "{digest}"\n'
    else:
        index = re.sub(r'(file = "' + re.escape(relative) + r'"\nhash = ")[a-f0-9]+(")',
                       lambda m: m[1] + digest + m[2], index)
    index_path.write_text(index)
    subprocess.run([sys.executable, str(ROOT / "scripts/rebuild_quest_ui.py")], check=True, cwd=ROOT)
    print(f'PASS: compiled {len(blocks)} Ars quests, preserving the original overview and all other chapters')


if __name__ == "__main__":
    main()
