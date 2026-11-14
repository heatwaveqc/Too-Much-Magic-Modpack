#!/usr/bin/env python3
"""Runtime assertions on actual post-AU recipes + post-KubeJS Polymorph scan.

The AU debug file is produced before the final KubeJS modifications, so its
output is used only to prove retained tag coverage and special ore protection.
The Polymorph scan is the authority on recipe groups *after* KubeJS edits.
"""
import argparse
import json
from pathlib import Path

p=argparse.ArgumentParser()
p.add_argument("--polymorph", type=Path, required=True)
p.add_argument("--au-after", type=Path, required=True)
a=p.parse_args()
conf=json.loads(a.polymorph.read_text())
native={}
for line in a.au_after.read_text().splitlines()[1:]:
    if " => " not in line:
        continue
    id, data=line.split(" => ",1)
    try:
        native[id.strip()]=json.loads(data)
    except json.JSONDecodeError:
        continue

def require_input(id, tag):
    r=native.get(id)
    assert r is not None, f"AU recipe missing: {id}"
    ingredient=r.get("ingredient")
    assert ingredient=={"tag":tag}, f"{id} no longer covers all {tag}: {ingredient}"

for rid,tag in [
    ("primalmagick:iron_ingot_from_grit_smelting","c:dusts/iron"),
    ("primalmagick:iron_ingot_from_grit_blasting","c:dusts/iron"),
    ("neovitae:materials/smelting_gold_dust","c:dusts/gold"),
    ("neovitae:materials/blasting_gold_dust","c:dusts/gold"),
    ("neovitae:materials/smelting_silver_dust","c:dusts/silver"),
    ("neovitae:materials/blasting_silver_dust","c:dusts/silver"),
    ("neovitae:materials/smelting_lead_dust","c:dusts/lead"),
    ("neovitae:materials/blasting_lead_dust","c:dusts/lead"),
    ("embers:silver_ingot_from_smelting_raw_silver","c:raw_materials/silver"),
    ("embers:silver_ingot_from_blasting_raw_silver","c:raw_materials/silver"),
    ("embers:silver_ingot_from_smelting_deepslate_silver_ore","c:ores/silver"),
    ("embers:silver_ingot_from_blasting_deepslate_silver_ore","c:ores/silver"),
    ("embers:copper_nugget_to_ingot","c:nuggets/copper"),
]:
    if "nugget" in rid:
        r=native[rid]
        assert any(v=={"tag":tag} for v in r.get("key",{}).values()),rid
    else:
        require_input(rid,tag)

# The old AU substitution incorrectly turned *all* Malum and vanilla
# quartz ore furnace recipes into c:ores/quartz and gave two distinct outputs.
# Likewise it changed vanilla copper ore input to a tag that includes
# Pastel blackslag copper ore; Pastel intentionally returns RAW copper.
for rid,expected in [
    ("minecraft:quartz","minecraft:nether_quartz_ore"),
    ("minecraft:quartz_from_blasting","minecraft:nether_quartz_ore"),
    ("malum:natural_quartz_from_blasting","malum:natural_quartz_ore"),
    ("malum:natural_quartz_from_deepslate_smelting","malum:deepslate_quartz_ore"),
    ("minecraft:copper_ingot_from_smelting_copper_ore","minecraft:copper_ore"),
    ("minecraft:copper_ingot_from_blasting_copper_ore","minecraft:copper_ore"),
]:
    recipe=native.get(rid)
    assert recipe is not None,f"Protected source recipe missing: {rid}"
    ingredient=recipe.get("ingredient")
    if isinstance(ingredient,list):
        assert len(ingredient)==1
        ingredient=ingredient[0]
    assert ingredient=={"item":expected}, f"Unsafe AU recipe input for {rid}: {ingredient}"
assert native["pastel:smelting/blackslag_ores/copper"]["result"]["id"]=="minecraft:raw_copper", (
    "Pastel's special raw-copper result unexpectedly changed"
)

# No distinct-output material input collision or deliberate KubeJS/native
# duplicate. This is *not* a blanket zero-Polymorph-group requirement.
bad=[]
for group in conf["groups"]:
    ids=group["recipe_ids"]
    involved=" ".join(ids)
    if any(k in involved for k in (
        "tmm:iron_dust_", "tmm:gold_dust_", "tmm:netherite_scrap_dust_",
        "neovitae:materials/smelting_netherite_scrap_dust",
        "neovitae:materials/blasting_netherite_scrap_dust",
        "malum:copper_ingot_from_nugget",
        "werewolves:silver_block_from_silver_ingot",
        "werewolves:raw_silver_block_from_raw_silver_ingot",
        "werewolves:silver_ingot_from_nuggets",
        "werewolves:silver_ingot_from_smelting",
        "werewolves:silver_ingot_from_blasting",
        "eidolon_repraised:smelt_silver_dust",
        "eidolon_repraised:blast_silver_dust",
        "eidolon_repraised:smelt_lead_dust",
        "eidolon_repraised:blast_lead_dust",
        "neovitae:smelting/hellforged_ingot_from_dust",
        "neovitae:blasting/hellforged_ingot_from_dust",
        "botania:botania_extras/smelting/charcoal_white_iridescent",
    )):
        bad.append(group)
    if any(x in involved for x in ("malum:natural_quartz_from_","minecraft:quartz")) and (
        "malum:natural_quartz_from_" in involved or "primalmagick:quartz_from_" in involved
    ):
        bad.append(group)
    if "pastel:smelting/blackslag_ores/copper" in involved or "pastel:blasting/blackslag_ores/copper" in involved:
        bad.append(group)
assert not bad, "Unexpected loaded processing recipe conflicts remained: "+json.dumps(bad,indent=2)
print("PASS: AU retained all iron/gold/silver/lead dust, silver raw/ore, and copper-nugget tag coverage")
print("PASS: Malum quartz and Pastel blackslag copper preserve distinct original furnace inputs")
print("PASS: Polymorph found no targeted overlapping process recipes")
print("Remaining Polymorph groups (unresolved and harmless; classify separately):",conf["groups_total"])
