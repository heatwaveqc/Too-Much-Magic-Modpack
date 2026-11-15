#!/usr/bin/env python3
"""B16 stone recipes from the exact downloaded, hash-pinned server JARs."""
from __future__ import annotations
import argparse
import copy
import json
import re
import shutil
import tomllib
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
POLICY = ROOT / "manifest/stone_generation_policy.json"
INVENTORY = ROOT / "manifest/modded_stone_inventory.json"
GALORE = ROOT / "kubejs/data/tmm/recipe/blockgen/natural_stone"
CUTTING = ROOT / "kubejs/data/tmm/recipe/stonecutting/common_marble"
OVERRIDE = ROOT / "kubejs/data/primalmagick/worldgen/configured_feature/ore_marble_raw.json"
DOC = ROOT / "docs/NATURAL-STONE-GALORE-RECIPES.md"

def insist(ok, message):
    if not ok:
        raise ValueError(message)

def dump(obj):
    return json.dumps(obj, indent=2, ensure_ascii=False) + "\n"

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("jar_directory", type=Path)
    args = parser.parse_args()
    policy = json.loads(POLICY.read_text(encoding="utf-8"))
    inventory = json.loads(INVENTORY.read_text(encoding="utf-8"))
    rows = {row["id"]: row for row in inventory["blocks"]}
    natural = {x["id"] for x in rows.values() if x["class"] == "primary_candidate"
               and x["worldgen_json_evidence"]}
    accepted = set(policy["natural_stones"])
    excluded = set(policy["excluded_natural_candidates"])
    insist(policy["schema_version"] == 1 and policy["no_free_shale"] is True, "Bad policy schema")
    insist(accepted.isdisjoint(excluded), "Accepted and excluded stone overlap")
    insist(accepted | excluded == natural,
           "Unclassified or stale natural materials: " +
           repr(sorted(natural ^ (accepted | excluded))))
    new = policy["natural_stones"] + policy["harvested_stone_variants"] + \
          policy["vanilla_natural_stones_not_already_in_galore"]
    insist(len(new) == len(set(new)), "Duplicate generator output")
    insist(not(set(new) & excluded), "Excluded ore/host accidentally included")
    insist("astralsorcery:aquamarine_shale" not in new, "Free Shale not approved")
    insist(not any(x.endswith("_ore") or "_ore_" in x for x in new),
           "Ore-like output not allowed")
    for identifier in new + policy["marble_equivalence"]:
        insist(re.fullmatch(r"[a-z0-9_.-]+:[a-z0-9_./-]+", identifier),
               "Unsafe item ID " + identifier)
        if not identifier.startswith("minecraft:"):
            insist(identifier in rows, "Not present in pinned census: " + identifier)
    expected = {}
    for file in (ROOT/"mods").glob("*.pw.toml"):
        meta=tomllib.loads(file.read_text(encoding="utf-8"))
        if meta.get("side","both") != "client":
            insist(meta["filename"] not in expected, "Duplicate artifact name")
            expected[meta["filename"]] = file
    found = {p.name for p in args.jar_directory.glob("*.jar")}
    insist(found == set(expected), f"Exact pinned jars mismatch missing={sorted(set(expected)-found)} extra={sorted(found-set(expected))}")
    jars = {name:zipfile.ZipFile(args.jar_directory/name) for name in expected}
    try:
        blocks=set()
        native=set()
        for z in jars.values():
            for name in z.namelist():
                mat=re.fullmatch(r"assets/([^/]+)/blockstates/([^/]+)\.json",name)
                if mat:
                    blocks.add(mat.group(1)+":"+mat.group(2))
                if name.startswith("data/cobblegengalore/recipe/blockgen/") and name.endswith(".json"):
                    data=json.loads(z.read(name))
                    result=data.get("result",{}).get("id")
                    if result:
                        native.add(result)
        for item in new + policy["marble_equivalence"]:
            if not item.startswith("minecraft:"):
                insist(item in blocks, "Registered block assets missing from pinned jars: " + item)
        insist(not(set(new)&native), "Existing Galore recipe collision: " + repr(sorted(set(new)&native)))
        for item in accepted:
            insist(rows[item]["worldgen_json_refs"], "Worldgen evidence missing: "+item)
        path=policy["worldgen_unification"]["native_feature_resource"]
        exact=[(name,z) for name,z in jars.items() if path in z.namelist()]
        insist(len(exact)==1, "Expected exactly one pinned Primal Magick feature: " + str(len(exact)))
        original=json.loads(exact[0][1].read(path))
        assert original.get("type") == "minecraft:ore", "Wrong worldgen type"
        config=copy.deepcopy(original)
        targets=config["config"]["targets"]
        insist(len(targets)==1, "Manual review required for changed marble target count")
        old=targets[0]["state"]["Name"]
        insist(old in ("primalmagick:marble_raw","primalmagick:marble"),
               "Unexpected original Primal marble source: "+old)
        targets[0]["state"]["Name"]=policy["worldgen_unification"]["canonical_block"]
        check=copy.deepcopy(config)
        check["config"]["targets"][0]["state"]["Name"]=old
        insist(check==original,"Worldgen placement or geometry would be changed")
        for directory in (GALORE,CUTTING):
            if directory.exists():
                shutil.rmtree(directory)
            directory.mkdir(parents=True)
        for item in sorted(new):
            obj={
                "type":"cobblegengalore:blockgen",
                "result":{"id":item,"count":1},
                "left":{"Name":policy["base_inputs"]["left"]},
                "right":{"Name":policy["base_inputs"]["right"]},
                "modifier":{"Name":item},
                "speed":policy["base_inputs"]["speed"],
                "consumeLeft":False,"consumeRight":False,
            }
            (GALORE/(item.replace(":","__")+".json")).write_text(dump(obj),encoding="utf-8")
        marble=policy["marble_equivalence"]
        insist(len(marble)==len(set(marble)),"Duplicate marble family")
        for src in sorted(marble):
            for dst in sorted(marble):
                if src==dst:
                    continue
                obj={"type":"minecraft:stonecutting","ingredient":src,
                     "result":{"id":dst,"count":1}}
                name=src.replace(":","__")+"__to__"+dst.replace(":","__")
                (CUTTING/(name+".json")).write_text(dump(obj),encoding="utf-8")
        OVERRIDE.parent.mkdir(parents=True,exist_ok=True)
        OVERRIDE.write_text(dump(config),encoding="utf-8")
        loot={}
        for item in policy["natural_stones"]:
            ns,block=item.split(":",1)
            key="data/"+ns+"/loot_table/blocks/"+block+".json"
            matches=[(n,z) for n,z in jars.items() if key in z.namelist()]
            names=set()
            if matches:
                data=json.loads(matches[0][1].read(key))
                def walk(obj):
                    if isinstance(obj,dict):
                        if obj.get("type")=="minecraft:item" and isinstance(obj.get("name"),str):
                            names.add(obj["name"])
                        for v in obj.values(): walk(v)
                    elif isinstance(obj,list):
                        for v in obj: walk(v)
                walk(data)
            loot[item]=sorted(names)
        lines=[
            "# Natural stone Galore recipes and marble worldgen unification",
            "",
            "Generated from exact hash-pinned Minecraft 1.21.1 NeoForge server JARs.",
            f"Generation: {len(new)} Galore recipes, {len(marble)*(len(marble)-1)} marble stonecutter conversions, one Primal worldgen override.",
            "",
            "## Generator operation",
            "Place one of the listed stone blocks BELOW Cobblegen Galore. Water on one horizontal side, lava on the opposite side. One item per cycle at base speed (tier scales output), inserted directly into an inventory above. No water/lava consumption. Seed block remains in world.",
            "",
            "## Natural modded stones: all approved by source audit",
            "| Block ID | Pinned worldgen evidence | Native loot (static JAR data) |",
            "|---|---|---|",
        ]
        for item in policy["natural_stones"]:
            entry=rows[item]
            lines.append("| "+item+" | "+entry["worldgen_json_refs"][0]+" | "+
                         (", ".join(loot[item]) or "No direct loot file found")+" |")
        lines += ["","## Additional generator outputs","",
                  "Harvested cobbled variants: "+", ".join(policy["harvested_stone_variants"])+".",
                  "Vanilla natural stones not already native to Galore: "+", ".join(policy["vanilla_natural_stones_not_already_in_galore"])+".",
                  "Galore's own cobblestone, stone, basalt, granite, andesite, diorite, tuff, netherrack, obsidian and cobbled deepslate recipes are preserved.",
                  "","## Marble unification","",
                  "The original configured Primal Magick ore-marble feature, source "+exact[0][0]+", replaces "+old+" with astralsorcery:marble_raw. It preserves original target tag, count, size, placement, biomes and biome modifiers. This alters only new ordinary overworld terrain; existing chunks unchanged.",
                  "Pastel Basal Marble stays in its dimension because it is an intentionally different biome stone; Goety's dedicated structure palettes are unchanged. Only Astral and Primal Magick's identical ordinary Marble blocks have 1:1 bidirectional stonecutter conversions; Goety Marble preserves its Cursed Infuser/totem crafting path and Pastel Basal Marble stays dimension-specific.",
                  "","## Excluded stone-named worldgen materials","",
                  "| Block ID | Reason |","|---|---|"]
        for item,why in sorted(policy["excluded_natural_candidates"].items()):
            lines.append("| "+item+" | "+why+" |")
        lines += ["","## QA still required","",
                  "A server boot and static registry assets do not establish natural spawn in every dimension, actual block-entity Galore behavior, stonecutter GUI entries, or performance gains. Test in disposable client world: dimension terrain, new overworld marble, JEI, block harvest, tier scaling, output buffer and water/lava source preservation. Profile sustained farms with spark.",
                  "No Aquamarine Shale recipe exists: that resource remains in Astral's fluid-consuming Chalice system.",
                  ""]
        DOC.write_text("\n".join(lines),encoding="utf-8")
        print("PASS B16 generated",len(new),"Galore recipes,",
              len(marble)*(len(marble)-1),"stonecutters; checked",len(natural),
              "worldgen candidates in",len(jars),"exact JARs. Original Primal state:",old,flush=True)
    finally:
        for z in jars.values():
            z.close()

if __name__=="__main__":
    main()
