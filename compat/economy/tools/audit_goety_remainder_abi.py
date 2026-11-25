#!/usr/bin/env python3
"""Verify the exact PE/Goety binaries for the narrow mapper-only #718 fix."""
import hashlib
import json
from pathlib import Path
import re
import subprocess
import sys
import tomllib
import zipfile

ROOT = Path(__file__).resolve().parents[3]

def checked_jar(directory, slug):
    pin = tomllib.loads((ROOT / "mods" / (slug + ".pw.toml")).read_text(encoding="utf-8"))
    jar = directory / pin["filename"]
    assert hashlib.new(pin["download"]["hash-format"], jar.read_bytes()).hexdigest() == pin["download"]["hash"], slug
    return jar

def javap(jar, symbol):
    return subprocess.run(["javap", "-c", "-p", "-classpath", str(jar), symbol],
                          check=True, capture_output=True, text=True, timeout=45).stdout

def verify(directory):
    pe = checked_jar(directory, "projecte")
    goety = checked_jar(directory, "goety")
    mapper = javap(pe, "moze_intel.projecte.emc.mappers.recipe.BaseRecipeTypeMapper")
    target = re.search(r"(?ms)^  private boolean addIngredient\(.*?\n(?=  (?:public|private|protected) |\Z)", mapper)
    assert target, "missing addIngredient ABI"
    body = target.group()
    assert body.count("Item.hasCraftingRemainingItem:") == 1, "redirect target changed"
    assert "Item.getCraftingRemainingItem:" in body and "NSSItem.createItem:" in body
    nss = javap(pe, "moze_intel.projecte.api.nss.NSSItem")
    factory = re.search(r"(?ms)^  public static .*?NSSItem createItem\(net\.minecraft\.world\.item\.ItemStack\);\n(.*?)(?=^  (?:public|private|protected) |\Z)", nss)
    assert factory, "missing ItemStack factory"
    factory_body = factory.group(1)
    assert re.search(r"ItemStack\.isEmpty:\(\)Z.*?ifeq.*?class java/lang/IllegalArgumentException.*?athrow", factory_body, re.S), "empty-stack rejection changed"
    assert "Can't make NSSItem with empty stack" in factory_body.replace("\\'", "'"), "empty-stack rejection message changed"
    roots = javap(goety, "com.Polarice3.Goety.common.items.magic.FullSpentTotem")
    souls = javap(goety, "com.Polarice3.Goety.common.items.magic.TotemOfSouls")
    assert "extends com.Polarice3.Goety.common.items.magic.TotemOfSouls" in roots
    assert "getCraftingRemainingItem(" in roots and "ItemStack.EMPTY" in roots
    assert "hasCraftingRemainingItem(" in souls
    assert re.search(r"hasCraftingRemainingItem\([^\n]*\);\s+Code:\s+0: iconst_1\s+1: ireturn", souls)
    with zipfile.ZipFile(goety) as z:
        tag = json.loads(z.read("data/goety/tags/item/totems.json"))
        assert "goety:totem_of_roots" in tag["values"]
        for name in ("savage_tooth_craft", "empty_focus_craft"):
            recipe = json.loads(z.read("data/goety/recipe/" + name + ".json"))
            assert any(v.get("tag") == "goety:totems" for v in recipe["key"].values())
    print("PASS: exact ProjectE and Goety hash, mapping call-site and native recipe/remainder ABI; no game boot")

if __name__ == "__main__":
    verify(Path(sys.argv[1]))
