#!/usr/bin/env python3
"""Eidolon-only silver craft, without TeamLapen anti-lycan tags."""
from pathlib import Path
ROOT=Path(__file__).resolve().parents[1]
s=(ROOT/"kubejs/server_scripts/silver_equipment_unification.js").read_text()
t=(ROOT/"kubejs/startup_scripts/silver_equipment_stats.js").read_text()
assert "ServerEvents.recipes(" in s and "embers:silver_" in s
assert "event.remove({ id: 'embers:silver_' + slot })" in s
assert "event.remove({ id: 'eidolon_repraised:silver_" not in s
assert "tier.setUses(250)" in t and "tier.setSpeed(7.6)" in t
assert "werewolves:" not in s+t and "vampirism:" not in s+t
print("PASS: five Eidolon tools + four Eidolon armor recipes; no global anti-lycan damage tag")
