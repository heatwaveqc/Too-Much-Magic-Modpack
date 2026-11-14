#!/usr/bin/env python3
"""Validate source-audited unification candidate. NOT a NeoForge boot/registry dump."""
import json
from pathlib import Path
root = Path(__file__).resolve().parents[1]
p = root / "config/almostunified"
cfg = json.loads((p / "unification/materials.json").read_text())
place = json.loads((p / "placeholders.json").read_text())
startup = json.loads((p / "startup.json").read_text())
audit = json.loads((root / "manifest/material-ore-audit.json").read_text())
materials = set(place["material"])
for material in ("silver", "lead", "iron", "gold", "copper", "sulfur"):
    assert material in materials, material
assert not ({"uranium", "draconium", "iesnium", "replica", "replication"} & materials)
assert not ({"tin", "aluminum", "nickel", "platinum", "zinc"} & materials), "Unverified ores slipped into whitelist"
assert startup["world_gen_unification"] is True
assert cfg["loot_unification"] is True
mods = json.loads((root / "manifest/mods.json").read_text())["mods"]
assert any(m["id"] == "almostunified" and m["enabled_in_pack"] for m in mods)
priorities = cfg["mod_priorities"]
for mod in ("minecraft", "occultism", "embers", "eidolon_repraised", "werewolves", "mahoutsukai", "neovitae", "primalmagick"):
    assert mod in priorities, f"unification missing source provider: {mod}"
overrides = cfg["priority_overrides"]
for resource in ("silver", "lead"):
    a = audit["resources"][resource]
    preferred = a["preferred"]
    assert preferred in priorities
    assert overrides["c:ores/" + resource] == preferred
    assert overrides["c:ingots/" + resource] == preferred
    assert overrides["c:raw_materials/" + resource] == preferred
    for provider in a["providers"]:
        assert provider["mod"] in priorities
        assert provider["ore"]
        assert provider["ore"][0].startswith(provider["mod"] + ":")
assert len(audit["resources"]["silver"]["providers"]) >= 4
assert len(audit["resources"]["lead"]["providers"]) >= 2
for name in ("iron", "gold"):
    assert overrides[f"c:dusts/{name}"] == "mahoutsukai"
assert overrides["c:dusts/copper"] == "occultism"
assert overrides["c:dusts/sulfur"] == "neovitae"
for recipe in ("mahoutsukai:powdered_", "#c:raw_materials/", "tmm:iron_dust_smelting", "tmm:gold_dust_smelting"):
    assert recipe in (root / "kubejs/server_scripts/material_unification.js").read_text()
print("PASS: source-audited silver/lead providers, conventional whitelist, priorities, and special-ore guards")
print("NOT A BOOT, LIVE REGISTRY, CLIENT, RECIPE, OR WORLDGEN TEST")
