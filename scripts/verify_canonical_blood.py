#!/usr/bin/env python3
"""Static checks for the zero-Java, recipe-boundary canonical blood compatibility.

NOT proof that these recipes load or execute successfully in the pinned
Minecraft 1.21.1 NeoForge pack; run client/world tests before promotion.
"""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DATA = ROOT / "kubejs" / "data"
EVILCRAFT = "evilcraft:blood"
HEXE = "hexerei:blood_fluid"
IRON = "irons_spellbooks:blood_vial"
GLASS = "minecraft:glass_bottle"


def read(path: str) -> dict:
    p = DATA / path
    obj = json.loads(p.read_text(encoding="utf-8"))
    assert isinstance(obj, dict), p
    return obj


def check_tag(path: str, mandatory: set[str]) -> None:
    tag = read(path)
    assert tag.get("replace") is False, path
    assert isinstance(tag.get("values"), list), path
    assert mandatory.issubset(set(tag["values"])), path
    assert len(tag["values"]) == len(set(tag["values"])), path


def item(item_id: str) -> dict:
    return {"item": item_id}


def result(item_id: str) -> dict:
    return {"id": item_id, "count": 1}


def check_transfer(path: str, kind: str, item_in: str, item_out: str,
                   fluid_id: str, amount: int) -> None:
    recipe = read(path)
    assert recipe["type"] == kind, path
    assert recipe["input"] == item(item_in), path
    assert recipe["fluid"] == {"id": fluid_id, "amount": amount}, path
    output_key = "output" if kind.startswith("hexerei:") else "result"
    assert recipe[output_key] == result(item_out), path


def main() -> None:
    check_tag("neovitae/tags/fluid/essentia_vitae.json", {
        EVILCRAFT, "irons_spellbooks:blood", HEXE, "hexerei:blood_flowing",
    })
    check_tag("c/tags/fluid/blood.json", {
        "irons_spellbooks:blood", HEXE, "hexerei:blood_flowing",
        "neovitae:essentia_vitae_source", "neovitae:essentia_vitae_flowing",
    })
    # The native altar is recipe-driven for buckets; there is no reason to
    # patch the capability just to change the player's item output.
    altar = read("neovitae/recipe/ara_vitae/bucket_life.json")
    assert altar == {
        "type": "neovitae:ara_vitae_recipe",
        "bloodNeeded": 1000,
        "craftSpeed": 5,
        "drainSpeed": 0,
        "input": item("minecraft:bucket"),
        "minTier": 0,
        "output": result("evilcraft:bucket_blood"),
    }, "Neo Vitae bucket ritual must consume exactly 1,000 EV"

    check_transfer("irons_spellbooks/recipe/alchemist_cauldron/fill_blood_vial.json",
                   "irons_spellbooks:alchemist_cauldron_fill",
                   IRON, GLASS, EVILCRAFT, 250)
    check_transfer("irons_spellbooks/recipe/alchemist_cauldron/empty_blood_vial.json",
                   "irons_spellbooks:alchemist_cauldron_empty",
                   GLASS, IRON, EVILCRAFT, 250)

    # Hexerei retains HEXE as its native cauldron fluid. Recipe interaction
    # takes priority over generic FluidUtil bucket handling in MixingCauldron.
    check_transfer("tmm/recipe/hexerei/hex_blood_to_evilcraft_bucket.json",
                   "hexerei:cauldron_emptying",
                   "minecraft:bucket", "evilcraft:bucket_blood", HEXE, 1000)
    check_transfer("tmm/recipe/hexerei/evilcraft_bucket_to_hex_blood.json",
                   "hexerei:cauldron_filling",
                   "evilcraft:bucket_blood", "minecraft:bucket", HEXE, 1000)
    check_transfer("tmm/recipe/hexerei/iron_vial_to_hex_blood.json",
                   "hexerei:cauldron_filling",
                   IRON, GLASS, HEXE, 250)

    # Legacy Hex bottle stays an item with unique Hexerei use/effects; the
    # native 250-mB HEXE bottle->cauldron recipe is deliberately not replaced.
    check_transfer("tmm/recipe/hexerei/evilcraft_blood_to_hex_bottle.json",
                   "hexerei:cauldron_emptying",
                   GLASS, "hexerei:blood_bottle", EVILCRAFT, 250)
    check_transfer("tmm/recipe/irons_spellbooks/hex_bottle_to_evilcraft.json",
                   "irons_spellbooks:alchemist_cauldron_fill",
                   "hexerei:blood_bottle", GLASS, EVILCRAFT, 250)
    assert not (DATA / "tmm/recipe/hexerei/iron_vial_to_evilcraft.json").exists(), (
        "Conflicting Iron-vial recipes must not compete over the same input"
    )
    assert not (DATA / "hexerei/recipe/cauldron_emptying/blood_to_bottle.json").exists(), (
        "Native Hexerei blood bottle output must remain intact"
    )
    print("PASS: eleven authored JSONs meet canonical bucket/fluid/vial boundaries")
    print("NOT RUNTIME VALIDATED: test recipe loading, right-click buckets, pipes, and bottles in a disposable world")


if __name__ == "__main__":
    main()
