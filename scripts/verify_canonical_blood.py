#!/usr/bin/env python3
"""Static checks for the first, no-Java canonical blood compatibility slice.

This checks authored JSON only. It is NOT proof that mod recipe serializers or
Minecraft fluid capabilities accept these files in a running 1.21.1 world.
"""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DATA = ROOT / "kubejs" / "data"
CANON = "evilcraft:blood"
AMOUNT = 250


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


def check_transfer(path: str, recipe_type: str, source: str, destination: str,
                   fluid_is_input: bool = False) -> None:
    r = read(path)
    assert r["type"] == recipe_type, path
    assert r["fluid"] == {"id": CANON, "amount": AMOUNT}, path
    assert r["input"] == {"item": source}, path
    key = "output" if recipe_type.startswith("hexerei:") else "result"
    assert r[key] == {"id": destination, "count": 1}, path
    # In these recipe types, the same JSON field may describe input-to-tank
    # or tank-to-bottle; actual transaction semantics require client tests.


def main() -> None:
    check_tag("neovitae/tags/fluid/essentia_vitae.json", {
        CANON, "irons_spellbooks:blood", "hexerei:blood_fluid",
        "hexerei:blood_flowing",
    })
    check_tag("c/tags/fluid/blood.json", {
        "irons_spellbooks:blood", "hexerei:blood_fluid",
        "hexerei:blood_flowing", "neovitae:essentia_vitae_source",
        "neovitae:essentia_vitae_flowing",
    })
    check_transfer(
        "irons_spellbooks/recipe/alchemist_cauldron/fill_blood_vial.json",
        "irons_spellbooks:alchemist_cauldron_fill",
        "irons_spellbooks:blood_vial", "minecraft:glass_bottle",
    )
    check_transfer(
        "irons_spellbooks/recipe/alchemist_cauldron/empty_blood_vial.json",
        "irons_spellbooks:alchemist_cauldron_empty",
        "minecraft:glass_bottle", "irons_spellbooks:blood_vial",
    )
    check_transfer(
        "tmm/recipe/hexerei/evilcraft_blood_to_hex_bottle.json",
        "hexerei:cauldron_emptying",
        "minecraft:glass_bottle", "hexerei:blood_bottle",
    )
    check_transfer(
        "tmm/recipe/hexerei/iron_vial_to_evilcraft.json",
        "hexerei:cauldron_filling",
        "irons_spellbooks:blood_vial", "minecraft:glass_bottle",
    )
    check_transfer(
        "tmm/recipe/irons_spellbooks/hex_bottle_to_evilcraft.json",
        "irons_spellbooks:alchemist_cauldron_fill",
        "hexerei:blood_bottle", "minecraft:glass_bottle",
    )
    print("PASS: seven authored JSONs satisfy fluid-ID and 250-mB contracts")
    print("NOT RUNTIME VALIDATED: inspect JEI/cauldrons/altar in disposable MC world")


if __name__ == "__main__":
    main()
