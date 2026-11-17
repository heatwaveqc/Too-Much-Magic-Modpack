#!/usr/bin/env python3
"""Static source contracts for TMM's Eldritch-gated CC: Tweaked and Spirit Fire pilot.

No jar registry/gameplay claims: run on a disposable Minecraft client/server next.
"""
from __future__ import annotations

import hashlib
import json
import re
import tomllib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
KEY = "tmm:eldritch_logic_core"
TARGETS = ("computer_normal", "computer_advanced", "pocket_computer_normal", "pocket_computer_advanced")


def require(ok: bool, message: str) -> None:
    if not ok:
        raise AssertionError(message)


def read(path: str) -> str:
    return (ROOT / path).read_text(encoding="utf-8")


def recipe(path: str) -> dict:
    return json.loads(read(path))


def main() -> None:
    pin_path = "mods/cc-tweaked.pw.toml"
    mod = tomllib.loads(read(pin_path))
    require(mod["filename"] == "cc-tweaked-1.21.1-forge-1.121.0.jar", "Wrong CC artifact")
    require(mod["side"] == "both", "CC must ship on both client/server")
    require(mod["download"]["hash-format"] == "sha1", "CC file hash algorithm drift")
    require(mod["download"]["hash"] == "946c83390aadfe0d8077198fcf22804cee206637", "CC published hash drift")
    require(mod["update"]["modrinth"] == {"mod-id": "gu7yAYhd", "version": "KNdTEfUA"}, "CC pinned version mismatch")
    require(mod["download"]["url"].endswith("/versions/KNdTEfUA/" + mod["filename"]), "CC URL mismatch")
    roster = json.loads(read("manifest/mods.json"))
    cc = [m for m in roster["mods"] if m["id"] == "cc-tweaked"]
    require(len(cc) == 1 and cc[0]["enabled_in_pack"], "CC must have one selected manifest entry")
    require(sum(bool(m["enabled_in_pack"]) for m in roster["mods"]) >= 156, "CC pack pin missing")

    startup = read("kubejs/startup_scripts/eldritch_logic_core.js")
    require("StartupEvents.registry('item'" in startup and KEY in startup, "Eldritch Logic Core not registered at startup")
    miner = recipe("kubejs/data/tmm/recipe/miner/eldritch_logic_core.json")
    require(miner["type"] == "occultism:miner", "Wrong miner type")
    require(miner["ingredient"] == {"tag": "occultism:miners/eldritch"}, "Only the Ancient Eldritch Miner may yield core")
    output = miner["result"]
    require(output["type"] == "occultism:weighted_item", "Expected native weighted item output")
    require(output["stack"] == {"count": 1, "id": KEY}, "Wrong Eldritch output")
    require(isinstance(output["weight"], int) and 0 < output["weight"] <= 500, "Unreasonable pilot relative weight")

    for name in TARGETS:
        path = "kubejs/data/computercraft/recipe/" + name + ".json"
        data = recipe(path)
        require(data["type"] == "minecraft:crafting_shaped", "Unexpected recipe type " + name)
        require(data["result"]["id"] == "computercraft:" + name, "Wrong CC output " + name)
        require(any(e == {"item": KEY} for e in data["key"].values()), "Missing core in " + name)
        require(len(data["pattern"]) == 3 and all(len(row) == 3 for row in data["pattern"]), "Wrong shape " + name)
        require("G" in data["key"] and data["key"]["G"] == {"tag": "c:glass_panes"}, "Lost CC glass " + name)
        if name.startswith("pocket_"):
            require(data["key"].get("A") == {"item": "minecraft:golden_apple"}, "Lost pocket apple " + name)

    fire = recipe("kubejs/data/tmm/recipe/spirit_fire/iesnium_from_thallasium.json")
    require(fire == {
        "type": "occultism:spirit_fire",
        "ingredient": {"item": "betterend:thallasium_ore"},
        "result": {"count": 1, "id": "occultism:iesnium_ore"}
    }, "Spirit Fire recipe drift or unsupported ore ID")

    index = read("index.toml")
    files = [pin_path, "kubejs/startup_scripts/eldritch_logic_core.js",
             "kubejs/data/tmm/recipe/miner/eldritch_logic_core.json",
             "kubejs/data/tmm/recipe/spirit_fire/iesnium_from_thallasium.json"]
    files += ["kubejs/data/computercraft/recipe/" + k + ".json" for k in TARGETS]
    for file in files:
        wanted = hashlib.sha256(read(file).encode("utf-8")).hexdigest()
        require('file = "' + file + '"' + chr(10) + 'hash = "' + wanted + '"' in index,
                "Missing/incorrect packwiz entry for " + file)
    pack = tomllib.loads(read("pack.toml"))
    require(pack["index"]["hash"] == hashlib.sha256(index.encode("utf-8")).hexdigest(), "Packwiz index digest broken")
    print("PASS: CC exact pin, Eldritch-only native mining, four gated computer recipes, and Iesnium Spirit Fire")
    print("PASS: all added pilot files indexed with correct SHA256 and top-level pack hash")
    print("NOT GAME TESTED: startup registry, loaded recipe IDs, JEI, mining output, DR reactor peripherals still need client/server acceptance")


if __name__ == "__main__":
    main()
