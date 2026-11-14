#!/usr/bin/env python3
"""Static candidate checks. NOT a NeoForge boot or recipe test."""
import json
from pathlib import Path
root = Path(__file__).resolve().parents[1]
p = root / "config/almostunified"
cfg = json.loads((p / "unification/materials.json").read_text())
place = json.loads((p / "placeholders.json").read_text())
startup = json.loads((p / "startup.json").read_text())
materials = set(place["material"])
assert "silver" in materials and {"iron", "gold", "diamond", "emerald"} <= materials
assert not ({"uranium", "draconium", "iesnium", "replica", "replication"} & materials)
assert startup["server_only"] is False
assert startup["world_gen_unification"] is True
assert cfg["loot_unification"] is True
assert cfg["priority_overrides"]["c:dusts/iron"] == "mahoutsukai"
assert cfg["priority_overrides"]["c:dusts/gold"] == "mahoutsukai"
mods = json.loads((root / "manifest/mods.json").read_text())["mods"]
assert any(m["id"] == "almostunified" and m["enabled_in_pack"] for m in mods)
recipes = (root / "kubejs/server_scripts/material_unification.js").read_text()
for required in ("mahoutsukai:powdered_", "#c:raw_materials/", "tmm:iron_dust_smelting", "tmm:gold_dust_smelting", "occultism:crushing/iron_dust_from_ingot"):
    assert required in recipes
print("PASS: material whitelist, source tags and static safeguards")
print("NOT A BOOT/RECIPE TEST")
