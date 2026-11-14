#!/usr/bin/env python3
"""Inspect pinned *downloaded* mod JARs for overlapping conventional equipment.

Source material: item model registry candidates + ordinary crafting outputs.
This is a family census, NOT proof of true item behavior, cross-mod recipes,
full registry membership, or runtime datapack-removal semantics. The native
Polymorph audit is required for actual recipe ambiguity.
"""
from __future__ import annotations
import argparse
import collections
import json
import os
from pathlib import Path
import re
import zipfile

parser = argparse.ArgumentParser()
parser.add_argument("jar_dir", type=Path)
parser.add_argument("--json-out", type=Path, required=True)
parser.add_argument("--md-out", type=Path, required=True)
args = parser.parse_args()
assert args.jar_dir.is_dir(), args.jar_dir

MATERIALS = {
    "silver", "lead", "copper", "iron", "gold", "golden", "tin",
    "bronze", "steel", "pewter", "electrum", "nickel", "aluminum",
    "platinum", "dawnstone", "netherite", "diamond", "obsidian",
}
TOOLS = ("sword", "axe", "pickaxe", "shovel", "hoe")
ARMOR = ("helmet", "chestplate", "leggings", "boots")
PATTERN = re.compile(
    r"^(" + "|".join(sorted(MATERIALS, key=len, reverse=True)) +
    r")_(sword|axe|pickaxe|shovel|hoe|helmet|chestplate|leggings|boots)$"
)
# Avoid mistaking arbitrary asset names for verified registered item IDs:
# model file presence is a *candidate* with recipe output validation when present.
models = collections.defaultdict(set)
recipes = collections.defaultdict(set)
invalid_json = 0
jars = list(sorted(args.jar_dir.glob("*.jar")))
assert len(jars) >= 90, f"expected full mod roster, found {len(jars)} jars"
for jar in jars:
    with zipfile.ZipFile(jar) as z:
        for name in z.namelist():
            if not name.endswith(".json"):
                continue
            parts = name.split("/")
            if len(parts) >= 5 and parts[0] == "assets" and parts[2:4] == ["models", "item"]:
                if len(parts) == 5:
                    mod, itemname = parts[1], parts[4][:-5]
                    matched = PATTERN.fullmatch(itemname)
                    if matched:
                        material, slot = matched.groups()
                        if material == "golden":
                            material = "gold"
                        models[(material, slot)].add(f"{mod}:{itemname}")
            if len(parts) < 4 or parts[0] != "data" or parts[2] not in ("recipe", "recipes"):
                continue
            try:
                raw = z.read(name)
                if len(raw) > 100000:
                    continue
                data = json.loads(raw)
            except (ValueError, UnicodeDecodeError, KeyError):
                invalid_json += 1
                continue
            if not isinstance(data, dict):
                continue
            typ = data.get("type", "")
            if typ not in ("minecraft:crafting_shaped", "minecraft:crafting_shapeless"):
                continue
            result = data.get("result")
            item = (result if isinstance(result, str) else
                    result.get("id", result.get("item")) if isinstance(result, dict) else None)
            if not isinstance(item, str) or ":" not in item:
                continue
            ns, itemname = item.split(":", 1)
            match = PATTERN.fullmatch(itemname)
            if match:
                material, slot = match.groups()
                if material == "golden":
                    material = "gold"
                recipe_id = f"{parts[1]}:{'/'.join(parts[3:])[:-5]}"
                recipes[(material, slot)].add((item, recipe_id))

families = []
for material, slot in sorted(set(models) | set(recipes)):
    items = sorted(models[(material,slot)] | {i for i, _ in recipes[(material,slot)]})
    providers = sorted(set(i.split(":")[0] for i in items))
    if len(providers) < 2:
        continue
    recipe_index = collections.defaultdict(list)
    for item, recipe_id in recipes[(material,slot)]:
        recipe_index[item].append(recipe_id)
    families.append({
        "material":material,"slot":slot,"providers":providers,
        "items": [{"id":item, "crafting_recipe_ids":sorted(recipe_index[item]),
                   "model_present":item in models[(material,slot)]} for item in items],
    })
by_material=collections.defaultdict(list)
for fam in families:
    by_material[fam["material"]].append(fam)
out={"source":"Original downloaded hash-pinned server mod JARs (not active KubeJS removals)",
     "jar_count":len(jars),
     "invalid_recipe_json":invalid_json,
     "scope":"Conventional <metal>_<slot> model names and crafting outputs; other naming conventions not covered",
     "multiple_provider_families":families,
     "materials":{k:len(v) for k,v in sorted(by_material.items())},
     "warnings":["Identical names do not establish identical effects or stats.",
                 "Runtime KubeJS recipe removals require separate Polymorph test.",
                 "Model assets are candidates, not guaranteed registered items."]}
args.json_out.parent.mkdir(parents=True,exist_ok=True)
args.json_out.write_text(json.dumps(out,indent=2)+"\n")
lines=["# Equipment material family census", "",
       f"Scanned **{len(jars)} pinned server-side JARs** and found "
       f"**{len(families)} cross-provider slot families** across "
       f"**{len(by_material)} materials**.", "",
       "Source is JAR model names and initial crafting JSON, **before KubeJS recipes**. "
       "A duplicate item family is not automatically a crafting conflict.", "",
       "| Material | Equipment slots with multiple providers |","|---|---:|"]
for material, entries in sorted(by_material.items()):
    lines.append(f"| {material} | {len(entries)} |")
lines += ["", "## Families and recipes", ""]
for fam in families:
    lines.append(f"### {fam['material']} {fam['slot']}")
    for item in fam["items"]:
        n=item["id"]
        recipe_ids=item["crafting_recipe_ids"]
        lines.append(f"- \`{n}\`: " +
                     (", ".join(f"\`{r}\`" for r in recipe_ids) if recipe_ids else
                      "No ordinary crafting recipe found; model candidate"))
    lines.append("")
lines += ["The full JSON includes the actual recipe IDs and model-candidate flags.", ""]
md="\n".join(lines)
args.md_out.write_text(md)
print("Equipment families by material:",dict(out["materials"]))
print(f"Found {len(families)} cross-provider families; scanned {len(jars)} jars.")
