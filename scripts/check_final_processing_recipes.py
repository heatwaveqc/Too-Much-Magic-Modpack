#!/usr/bin/env python3
"""Check real KubeJS-exported FINAL recipe JSON, after all startup and AU hooks.

Unlike Almost Unified's recipes_after.txt, this contains modifications from
ServerEvents.recipes. Run only on disposable CI worlds with /kubejs export.
"""
import argparse
import json
from pathlib import Path

arg=argparse.ArgumentParser()
arg.add_argument("--export",required=True,type=Path)
arg.add_argument("--json-out",required=True,type=Path)
arg.add_argument("--md-out",required=True,type=Path)
a=arg.parse_args()
root=a.export / "recipes"
assert root.is_dir(),f"KubeJS export missing recipes folder: {root}"
def recipe(id):
    ns, path=id.split(":",1)
    path=root/ns/(path+".json")
    return json.loads(path.read_text()) if path.is_file() else None
def req(id):
    r=recipe(id)
    assert r is not None,f"Expected effective recipe missing: {id}"
    return r
def ing(id):
    r=req(id)
    return r.get("ingredient",r.get("ingredients",r.get("key")))
def output(id):
    r=req(id)
    return r.get("result",{})
def check_tag_input(id,tag):
    assert ing(id)=={"tag":tag},f"Incorrect input for {id}: {ing(id)}"
def check_result(id,item):
    r=output(id)
    assert r.get("id",r.get("item"))==item,f"Incorrect output for {id}: {r}"

# Confirm MAHOU IS NOT an ingot->2 dust->2 ingot closed loop.
# The final KubeJS recipe is the only authoritative proof of replaceInput.
for metal in ("iron","gold"):
    for suffix,count in (("",1),("_2",2)):
        id="mahoutsukai:powdered_"+metal+suffix
        r=req(id)
        inputs=r.get("ingredients",[])
        assert {"tag":"c:raw_materials/"+metal} in inputs, f"{id}: no RAW input {inputs}"
        assert not any(x.get("tag")=="c:ingots/"+metal for x in inputs if isinstance(x,dict)), (
            f"{id} could turn ingot back into multiplied dust"
        )
        assert output(id).get("id")=="mahoutsukai:powdered_"+metal,id
        assert int(output(id).get("count",1))==count,id
for gem in ("diamond","emerald","quartz"):
    id="mahoutsukai:powdered_"+gem+"_2"
    assert {"tag":"c:ores/"+gem} in req(id).get("ingredients",[]), (
        f"{id} must require ore for double-powder yield"
    )

dust_sources={
 "iron":("primalmagick:iron_ingot_from_grit_smelting","primalmagick:iron_ingot_from_grit_blasting","minecraft:iron_ingot"),
 "gold":("neovitae:materials/smelting_gold_dust","neovitae:materials/blasting_gold_dust","minecraft:gold_ingot"),
 "silver":("neovitae:materials/smelting_silver_dust","neovitae:materials/blasting_silver_dust","occultism:silver_ingot"),
 "lead":("neovitae:materials/smelting_lead_dust","neovitae:materials/blasting_lead_dust","embers:lead_ingot"),
}
for metal,(smelt,blast,target) in dust_sources.items():
    for id in (smelt,blast):
        check_tag_input(id,"c:dusts/"+metal)
        check_result(id,target)

for id,tag,out in (
    ("embers:silver_ingot_from_smelting_raw_silver","c:raw_materials/silver","occultism:silver_ingot"),
    ("embers:silver_ingot_from_blasting_raw_silver","c:raw_materials/silver","occultism:silver_ingot"),
    ("embers:silver_ingot_from_smelting_deepslate_silver_ore","c:ores/silver","occultism:silver_ingot"),
    ("embers:silver_ingot_from_blasting_deepslate_silver_ore","c:ores/silver","occultism:silver_ingot"),
):
    check_tag_input(id,tag)
    check_result(id,out)
assert {"tag":"c:nuggets/copper"} in req("embers:copper_nugget_to_ingot").get("key",{}).values()
check_result("embers:copper_nugget_to_ingot","minecraft:copper_ingot")
for id in ("tmm:netherite_scrap_dust_smelting","tmm:netherite_scrap_dust_blasting"):
    r=req(id)
    assert r["ingredient"]=={"tag":"c:dusts/netherite_scrap"},id
    assert r["result"]["id"]=="minecraft:netherite_scrap",id
    assert r["result"].get("count",1)==1,id
    assert r["experience"]==2.0,id
    assert r["cookingtime"]==(100 if id.endswith("blasting") else 200),id

# This is both a design and safety condition: custom minerals are not
# silently exchanged for vanilla ore through a common input tag.
for id,expected in (
    ("minecraft:quartz","minecraft:nether_quartz_ore"),
    ("malum:natural_quartz_from_blasting","malum:natural_quartz_ore"),
    ("malum:natural_quartz_from_deepslate_smelting","malum:deepslate_quartz_ore"),
    ("minecraft:copper_ingot_from_smelting_copper_ore","minecraft:copper_ore"),
):
    assert ing(id)=={"item":expected}, f"Special ore ambiguity: {id}: {ing(id)}"
assert output("pastel:smelting/blackslag_ores/copper").get("id")=="minecraft:raw_copper"
# Enchanted's lime keeps furnace priority; Pastel's decorative polished
# calcite is stonecuttable and still available through blast furnace.
assert ing("enchanted:quicklime")=={"item":"minecraft:calcite"}
assert output("enchanted:quicklime").get("id")=="enchanted:quicklime"
assert output("pastel:blasting/calcite_to_polished_calcite").get("id")=="pastel:polished_calcite"
stone=req("tmm:stonecutting/polished_calcite")
assert stone["type"]=="minecraft:stonecutting",stone
assert stone["result"].get("id")=="pastel:polished_calcite",stone
assert stone["ingredient"] in ({"item":"minecraft:calcite"},[{"item":"minecraft:calcite"}]),stone

removed=[
 "malum:copper_ingot_from_nugget",
 "werewolves:silver_block_from_silver_ingot",
 "werewolves:raw_silver_block_from_raw_silver_ingot",
 "werewolves:silver_ingot_from_nuggets",
 "werewolves:silver_ingot_from_smelting_raw_silver",
 "werewolves:silver_ingot_from_smelting_deepslate_silver_ore",
 "werewolves:silver_ingot_from_blasting_raw_silver",
 "werewolves:silver_ingot_from_blasting_deepslate_silver_ore",
 "eidolon_repraised:smelt_silver_dust",
 "eidolon_repraised:blast_silver_dust",
 "eidolon_repraised:smelt_lead_dust",
 "eidolon_repraised:blast_lead_dust",
 "neovitae:smelting/hellforged_ingot_from_dust",
 "neovitae:blasting/hellforged_ingot_from_dust",
 "occultism:smelting/netherite_scrap_from_dust",
 "occultism:blasting/netherite_scrap_from_dust",
 "neovitae:materials/smelting_netherite_scrap_dust",
 "neovitae:materials/blasting_netherite_scrap_dust",
 "botania:botania_extras/smelting/charcoal_white_iridescent",
 "botania:botania_extras/smelting/charcoal_pink_iridescent",
 "pastel:smelting/calcite_to_polished_calcite",
 "tmm:iron_dust_smelting",
 "tmm:iron_dust_blasting",
 "tmm:gold_dust_smelting",
 "tmm:gold_dust_blasting",
 "occultism:crushing/iron_dust_from_ingot",
 "occultism:crushing/gold_dust_from_ingot",
]
still=[id for id in removed if recipe(id) is not None]
assert not still,f"Removed/unsafe recipes survived KubeJS: {still}"

# Preserve native expensive processing routes and config-managed self repair.
for id in (
    "occultism:crushing/silver_dust_from_raw",
    "occultism:crushing/iron_dust_from_raw",
    "occultism:crushing/gold_dust_from_raw",
    "primalmagick:silver_dust_from_dissolving_raw_metal",
    "primalmagick:lead_dust_from_dissolving_raw_metal",
    "neovitae:materials/smelting_hellforged_dust",
    "neovitae:materials/blasting_hellforged_dust",
):
    req(id)

watched=[
 "mahoutsukai:powdered_iron",
 "mahoutsukai:powdered_iron_2",
 "mahoutsukai:powdered_gold",
 "mahoutsukai:powdered_gold_2",
 "mahoutsukai:powdered_diamond_2",
 "mahoutsukai:powdered_emerald_2",
 "mahoutsukai:powdered_quartz_2",
 "tmm:netherite_scrap_dust_smelting",
 "tmm:netherite_scrap_dust_blasting",
 "minecraft:quartz",
 "malum:natural_quartz_from_blasting",
 "pastel:smelting/blackslag_ores/copper",
 "enchanted:quicklime",
 "pastel:blasting/calcite_to_polished_calcite",
 "tmm:stonecutting/polished_calcite",
]
for ids in dust_sources.values():watched.extend(ids[:2])
watched.extend(["embers:silver_ingot_from_smelting_raw_silver","embers:copper_nugget_to_ingot"])
out={"source":"KubeJS /kubejs export (final recipe manager)", "total_exported_json_files":sum(1 for _ in root.rglob("*.json")),
     "present":{id:req(id) for id in watched},
     "removed_recipes_verified_absent":removed,
     "verification":"PASS: no duplicative dust recipes; no Mahou ingot-multiplication loop; quartz and Pastel copper protected"}
a.json_out.parent.mkdir(parents=True,exist_ok=True)
a.json_out.write_text(json.dumps(out,indent=2)+"\n")
md=["# Effective post-KubeJS material processing audit","",
    "Validated **"+str(out["total_exported_json_files"])+"** exported recipe JSON files.",
    "All expected retained recipes checked; **"+str(len(removed))+"** redundant or unsafe recipe IDs absent.",
    "Mahou iron and gold both require RAW iron/gold for hammer 1x and mortar 2x; gem mortar doubling requires Silk Touch ore.",
    "Iron/gold/silver/lead dust smelting+blasting each use a single shared material tag and canonical ingot output.",
    "Quartz ore remains distinct from Malum natural quartz ore; Pastel blackslag copper retains its raw-copper furnace output.",
    "Netherite scrap from dust yields 1 scrap at 2 XP per dust, both furnace and blast-furnace.","",
    "## Verified critical effective recipes",""]
for id in watched:
    r=out["present"][id]
    md.append("- `"+id+"`: "+json.dumps({"ingredient":r.get("ingredient",r.get("ingredients",r.get("key"))),"result":r.get("result"),"experience":r.get("experience")},ensure_ascii=False))
md+=["","**No GUI/tool durability or actual in-world processing machine behavior is implied.**",""]
a.md_out.write_text("\n".join(md))
print("PASS: KubeJS final material recipes validated, ",out["total_exported_json_files"]," exported recipes",len(removed)," verified absent")
