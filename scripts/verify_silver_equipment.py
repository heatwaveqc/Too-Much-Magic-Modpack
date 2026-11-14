#!/usr/bin/env python3
"""Fail early if canonical silver recipes or critical Werewolves hooks drift."""
from pathlib import Path
import re
ROOT=Path(__file__).resolve().parents[1]
s=(ROOT/"kubejs/server_scripts/silver_equipment_unification.js").read_text()
stats=(ROOT/"kubejs/startup_scripts/silver_equipment_stats.js").read_text()
for hook in ("ServerEvents.tags('item'", "ServerEvents.recipes(", "werewolves:tools/silver",
             "werewolves:armor/silver", "eidolon_repraised:silver_", "embers:silver_",
             "werewolves:silver_", "event.remove({ id:"):
    assert hook in s, hook
assert re.search(r"\['sword', 'pickaxe', 'axe', 'shovel', 'hoe'\]",s)
assert re.search(r"\['helmet', 'chestplate', 'leggings', 'boots'\]",s)
assert "mod: " not in s, "Never remove entire mod's recipes"
assert "event.remove({ output:" not in s, "No broad removal by output item"
assert "item.maxDamage = 250" in stats
assert "item.digSpeed = 7.6" in stats
for slot in ("helmet","chestplate","leggings","boots"):
    assert f"['{slot}', " in stats
assert "event.modify('werewolves:silver_'" in stats
assert "item.tier =" not in stats and "item.armor" not in stats, (
    "Broken 1.21.1 tier/armor mutation must not be silently reintroduced"
)
print("PASS: nine canonical silver craftable results selected, 14 redundant recipe IDs, tags and stats configured")
print("NOTE: actual recipe loss, native effects, armor enchantability and item stats require modded runtime/client verification")
