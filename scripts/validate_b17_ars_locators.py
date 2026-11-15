#!/usr/bin/env python3
"""Static B17 Ars Additions locator-contract and packwiz hash checks (not a gameplay test)."""
import hashlib
import json
import tomllib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
EXPECTED = {
    "kubejs/data/tmm/recipe/locate_structure/mineshaft.json": ("tmm:mineshaft", "minecraft:rail", "minecraft:mineshaft"),
    "kubejs/data/tmm/recipe/locate_structure/shipwreck.json": ("tmm:shipwreck", "minecraft:nautilus_shell", "minecraft:shipwreck"),
}

def fail(condition, message):
    if not condition:
        raise AssertionError(message)

def read(path):
    return (ROOT / path).read_text(encoding="utf-8")

def main():
    index = read("index.toml")
    pack = tomllib.loads(read("pack.toml"))
    fail(pack["index"]["hash"] == hashlib.sha256(index.encode()).hexdigest(), "Invalid top-level packwiz index hash")
    m = json.loads(read("manifest/mods.json"))
    required = {"attributefix","clumps","extreme-sound-muffler","get-it-together-drops","lootjs","yungs-api","yungs-better-nether-fortresses","yungs-better-strongholds"}
    found = {item["id"]: item for item in m["mods"] if item["id"] in required}
    fail(set(found) == required, "B17 approved roster incomplete")
    fail(all(item["desired"] and not item["enabled_in_pack"] for item in found.values()), "Unpinned B17 mod incorrectly marked installed")
    fail(any(x["id"] == "explorers-compass" for x in m["excluded"]), "Missing Compass exclusion decision")
    for path, (rid, augment, tag) in EXPECTED.items():
        recipe = json.loads(read(path))
        fail(recipe == {
            "type": "ars_additions:locate_structure",
            "augments": [{"key": augment}],
            "id": rid,
            "structure": {"tag": tag},
        }, "Bad Locate Structure schema or cost: "+path)
        digest = hashlib.sha256(read(path).encode()).hexdigest()
        fail(f'file = "{path}"\nhash = "{digest}"' in index, "Missing/mismatched index entry: "+path)
    print("PASS: B17 approved planned roster + two native Ars Additions locator JSONs + packwiz hashes")
    print("NOT TESTED: upstream mod hashes/pins, runtime structure registries, YUNG compatibility, server/client, recipes")

if __name__ == "__main__":
    main()
