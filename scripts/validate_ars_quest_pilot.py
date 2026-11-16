#!/usr/bin/env python3
"""Check the pilot's coverage, detection, dependencies, rewards and pinned evidence.

--jars validates against the exact downloaded distribution artifacts. This is
source validation; scripts/write_ars_quest_probe.py adds a live-registry probe.
"""
from __future__ import annotations

import argparse
from collections import Counter
import hashlib
import json
from pathlib import Path
import tomllib
import zipfile

from validate_quests import ensure, quest_blocks

ROOT = Path(__file__).resolve().parents[1]


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--jars", type=Path)
    args = parser.parse_args()
    pilot = json.loads((ROOT / "manifest/ars_quest_pilot.json").read_text())
    quests = pilot["quests"]
    by_key = {q["key"]: q for q in quests}
    ensure(len(quests) == len(by_key) == 49, "Expected 49 distinct Ars quests")
    ensure([q["number"] for q in quests] == list(range(49)), "Stable Ars allocation order changed")
    pins = {p.stem.removesuffix(".pw"): tomllib.loads(p.read_text())
            for p in (ROOT / "mods").glob("ars*.pw.toml")}
    coverage = Counter(q["mod"] for q in quests)
    ensure(set(coverage) == set(pins), "Pilot and installed Ars roster differ")
    ensure(all(coverage[mod] >= 2 for mod in pins), "Every installed add-on needs at least two quests")
    ensure([q["key"] for q in quests if q["criterion"]["type"] == "checkmark"] == ["orientation"],
           "Only initial orientation may use a manual task")
    ensure(bool(by_key["orientation"].get("manual_rationale")), "Undocumented manual exception")
    ensure(by_key["orientation"]["reward"] == {"xp": 5}, "Orientation must not pay a filler item")
    positions = {tuple(q["position"]) for q in quests}
    ensure(len(positions) == len(quests), "Overlapping Ars layout")
    for key in by_key:
        visited = set()
        cursor = key
        while cursor:
            ensure(cursor in by_key, f"Missing prerequisite {cursor}")
            ensure(cursor not in visited, f"Cyclic dependency from {key}")
            visited.add(cursor)
            cursor = by_key[cursor]["depends"]
    for key, advance in {
        "novice": "ars_nouveau:novice_spell_book",
        "mage": "ars_nouveau:apprentice_spell_book",
        "archmage": "ars_nouveau:archmage_spell_book",
        "chimera": "tmm:ars_pilot/chimera",
        "ans_transcribe": "ars_n_spells:transcribe_spell",
        "ans_bind": "ars_n_spells:bind_spell",
        "ans_cast": "ars_n_spells:first_cross_cast",
    }.items():
        ensure(by_key[key]["criterion"] == {"type": "advancement", "id": advance},
               f"Lost persistent action/tier detection for {key}")
    boss = json.loads((ROOT / "kubejs/data/tmm/advancement/ars_pilot/chimera.json").read_text())
    ensure(boss == {"criteria": {"kill_chimera": {
        "trigger": "minecraft:player_killed_entity",
        "conditions": {"entity": {"type": "ars_nouveau:wilden_boss"}}
    }}, "requirements": [["kill_chimera"]]}, "Chimera must require an actual credited kill, with no independent payout")

    author = json.loads((ROOT / "manifest/quest_content.json").read_text())
    resolved = json.loads((ROOT / "manifest/quest_content_resolved.json").read_text())
    author_ars = [q for q in author["quests"] if q["chapter"] == "Ars Nouveau"]
    ensure(len(author_ars) == len(quests), "Pilot absent from canonical authoring manifest")
    for q, row in zip(quests, author_ars, strict=True):
        ensure(all(q[k] == row[k] for k in ("title", "description", "criterion", "reward", "optional")),
               f"Canonical source drift: {q['key']}")
        reward = q["reward"]
        ensure(reward["xp"] > 0, f"No direct XP: {q['key']}")
        ensure(q["key"] == "orientation" or reward.get("count", 0) > 0, f"No useful payout: {q['key']}")
        forbidden = {"minecraft:experience_bottle", "minecraft:paper", "minecraft:cookie", "minecraft:compass", "minecraft:bowl"}
        ensure(reward.get("item") not in forbidden, f"Filler/forbidden payout: {q['key']}")
        ensure(not any(k in reward for k in ("coin", "choice")), "Pilot uses fixed physical milestone rewards")
        for source in q["evidence"]:
            pin = tomllib.loads((ROOT / source["pin"]).read_text())
            ensure(source["jar"] == pin["filename"] and source["hash"] == pin["download"]["hash"]
                   and source["hash_format"] == pin["download"]["hash-format"], f"Stale pin evidence: {q['key']}")

    text = (ROOT / "config/ftbquests/quests/chapters/ars_nouveau.snbt").read_text()
    blocks = quest_blocks(text, "Ars Nouveau")
    ensure(len(blocks) == len(quests), "Missing generated quests")
    for q, block in zip(quests, blocks, strict=True):
        ensure("can_repeat: true" not in block and "auto_claim:" not in block, f"Automatic/repeated claim: {q['key']}")
        ensure(block.count("team_reward: false") == (1 if q["key"] == "orientation" else 2),
               f"Pilot rewards must be claimed once per player: {q['key']}")
        ensure("consume_items: true" not in block, "Equipment/product tasks must not consume the player's work")

    # Beyond the whole-book validator's item-task graph, inspect the native
    # advancement predicates so changing a task to an advancement cannot hide a gift loop.
    targets = {}
    for q in quests:
        task = q["criterion"]
        if task["type"] == "item":
            targets.setdefault(task["id"], set()).add(q["key"])
    if args.jars:
        jars = {}
        for mod, pin in pins.items():
            path = args.jars / pin["filename"]
            ensure(path.is_file(), f"Missing pinned artifact {path}")
            ensure(hashlib.new(pin["download"]["hash-format"], path.read_bytes()).hexdigest() == pin["download"]["hash"],
                   f"Unverified artifact {path}")
            jars[mod] = zipfile.ZipFile(path)
        for q in quests:
            source = q["evidence"][0]
            jar = jars[q["mod"]]
            for asset in [source.get("asset"), source.get("advancement"),
                          *source.get("classes", []), *source.get("recipes", [])]:
                if asset:
                    ensure(asset in jar.namelist(), f"Missing pinned evidence {asset}")
            if source.get("advancement"):
                data = json.loads(jar.read(source["advancement"]))
                for criterion in data["criteria"].values():
                    if criterion["trigger"] == "minecraft:inventory_changed":
                        for predicate in criterion["conditions"]["items"]:
                            items = predicate.get("items", [])
                            for item in [items] if isinstance(items, str) else items:
                                targets.setdefault(item, set()).add(q["key"])
        print("PASS: evidence files and native advancement predicates match all eight exact pinned Ars JARs")
    for q in author["quests"]:
        reward = q["reward"]
        # The preexisting personal starter choice deliberately offers the
        # Novice book; this documented exception is not a milestone payout.
        for item in [reward.get("item")]:
            ensure(item not in targets, f"Gift {q['title']} would trigger Ars objective(s) {targets.get(item)}")
    repeats = Counter(q["reward"].get("item") for q in author["quests"] if q["reward"].get("item"))
    pilot_repeats = {q["reward"]["item"]: repeats[q["reward"]["item"]]
                     for q in quests if q["reward"].get("item") and repeats[q["reward"]["item"]] > 1}
    print("EDITORIAL REVIEW: repeated pilot payout IDs across the book: " + json.dumps(pilot_repeats, sort_keys=True))
    ensure(len([q for q in resolved["quests"] if q["chapter"] == "Ars Nouveau"]) == 49,
           "Stable ID ledger incomplete")
    print("PASS: 49 Ars quests, 7/7 add-ons, one justified orientation, acyclic branches, personal fixed rewards")
    print("NOT A CLIENT TEST: FTB rendering, real action credit, team claims and gameplay require a disposable world")


if __name__ == "__main__":
    main()
