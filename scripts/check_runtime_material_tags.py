#!/usr/bin/env python3
"""Check real Almost Unified tags from a disposable, hash-pinned modded-server boot.

This cannot prove GUI results, crafting-tool remainders, actual chunk terrain,
or Neo Vitae's dungeon/meteor rewards; those require gameplay tests.
"""
from __future__ import annotations

import json
import sys
from pathlib import Path

root = Path(__file__).resolve().parents[1]
tags_path = Path(sys.argv[1])
assert tags_path.is_file(), f"Missing live tags: {tags_path}"
tags = {}
for line in tags_path.read_text(encoding="utf-8").splitlines():
    if line.startswith("#") or " => " not in line:
        continue
    key, values = line.split(" => ", 1)
    tags[key.strip()] = {x.strip() for x in values.split(", ") if x.strip()}

silver_ores = {
    "occultism:silver_ore", "occultism:silver_ore_deepslate",
    "embers:silver_ore", "embers:deepslate_silver_ore",
    "eidolon_repraised:silver_ore", "eidolon_repraised:deep_silver_ore",
}
lead_ores = {
    "embers:lead_ore", "embers:deepslate_lead_ore",
    "eidolon_repraised:lead_ore", "eidolon_repraised:deep_lead_ore",
}
assert silver_ores <= tags.get("c:ores/silver", set()), (
    "Missing silver source(s) in live c:ores/silver: "
    + repr(silver_ores - tags.get("c:ores/silver", set()))
)
assert lead_ores <= tags.get("c:ores/lead", set()), (
    "Missing lead source(s): " + repr(lead_ores - tags.get("c:ores/lead", set()))
)
for tag, items in {
    "c:ingots/silver": {
        "occultism:silver_ingot", "embers:silver_ingot",
        "eidolon_repraised:silver_ingot"},
    "c:raw_materials/lead": {"embers:raw_lead", "eidolon_repraised:raw_lead"},
    "c:dusts/iron": {"mahoutsukai:powdered_iron", "occultism:iron_dust",
                     "neovitae:iron_dust", "primalmagick:iron_grit"},
    "c:dusts/gold": {"mahoutsukai:powdered_gold", "occultism:gold_dust",
                     "neovitae:gold_dust", "primalmagick:gold_grit"},
    "c:dusts/copper": {"occultism:copper_dust", "neovitae:copper_dust",
                       "primalmagick:copper_grit"},
    "c:dusts/silver": {"occultism:silver_dust", "neovitae:silver_dust"},
    "c:dusts/lead": {"neovitae:lead_dust"},
}.items():
    missing = items - tags.get(tag, set())
    assert not missing, f"Tag {tag} missing {sorted(missing)}"

cfg = json.loads((root / "config/almostunified/unification/materials.json").read_text())
assert {"c:gems/emerald", "c:gems/quartz",
        "c:storage_blocks/emerald", "c:storage_blocks/quartz"} <= set(cfg["ignored_tags"])
for special in ["goety:magic_emerald", "goety:awakened_emerald_block",
                "extrabotany:.*_quartz", "botania:(blaze|dark|elf|lavender|mana|red|sunny)_quartz"]:
    assert special in cfg["ignored_items"]
assert "deeperdarker:(gloomslate|sculk_stone)_.*_ore" in cfg["ignored_items"]
assert "pastel:blackslag_.*_ore" in cfg["ignored_items"]

print("PASS: LIVE 6-way silver/4-way lead IDs and metal dusts including Neo Vitae silver/lead")
print("PASS: magic emerald/quartz and unique dimension ores excluded by configuration")
print("NOT A GUI, RECIPE, ORE WORLDGEN, OR CUSTOM LOOT MECHANICS TEST")
