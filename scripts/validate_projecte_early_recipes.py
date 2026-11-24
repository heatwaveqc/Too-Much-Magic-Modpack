#!/usr/bin/env python3
"""Type-A validator for owner-approved ProjectE early recipe gates (#44).

This validates the *staged datapack*, not the loaded Minecraft recipe manager.
It intentionally does not invent EMC values or claim that first-Stone knowledge
is already granted. Pass --emc-map with an actual same-build POST-MAPPING
ProjectE EMC export to additionally require all three dust values > 0.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DUSTS = ("low_covalence_dust", "medium_covalence_dust", "high_covalence_dust")
FALSE = [{"type": "neoforge:false"}]
RODS = (
    ("divining_rod_1", "neovitae:iron_dust", "minecraft:stick"),
    ("divining_rod_2", "neovitae:diamond_dust", "projecte:divining_rod_1"),
    ("divining_rod_3", "occultism:netherite_dust", "projecte:divining_rod_2"),
)


def read(root: Path, relative: str) -> dict:
    return json.loads((root / relative).read_text(encoding="utf-8"))


def recipe(root: Path, name: str) -> dict:
    return read(root, f"kubejs/data/projecte/recipe/{name}.json")


def assert_disabled(data: dict, name: str, result: str) -> None:
    assert data.get("neoforge:conditions") == FALSE, f"{name}: old recipe must be disabled"
    assert data["result"] == {"count": 40 if name in DUSTS else 1, "id": result}
    assert data["type"] in ("minecraft:crafting_shaped", "minecraft:crafting_shapeless")


def assert_shaped(data: dict, name: str, pattern: list[str], keys: dict) -> None:
    assert data["type"] == "minecraft:crafting_shaped", name
    assert data.get("neoforge:conditions") != FALSE, f"{name}: approved recipe disabled"
    assert data["pattern"] == pattern, name
    assert data["key"] == keys, f"{name}: incorrect ingredient(s)"
    assert data["result"] == {"count": 1, "id": f"projecte:{name}"}, name


def require_positive_dust_emc(values: dict) -> None:
    for name in DUSTS:
        key = f"projecte:{name}"
        value = values.get(key, values.get("item|" + key, 0))
        if isinstance(value, dict):
            value = value.get("emc", 0)
        assert type(value) is int and value > 0, (
            f"{key}: no positive same-build post-mapping EMC; "
            "DO NOT promote EMC-only dust until values and knowledge grant are verified"
        )


def validate(root: Path, emc_map: dict | None = None) -> None:
    for name in DUSTS:
        data = recipe(root, name)
        assert_disabled(data, name, f"projecte:{name}")
        assert data["type"] == "minecraft:crafting_shapeless"
    assert_disabled(recipe(root, "repair_talisman_alt"), "repair_talisman_alt",
                    "projecte:repair_talisman")

    talisman = recipe(root, "repair_talisman")
    assert talisman["type"] == "minecraft:crafting_shapeless"
    assert talisman.get("neoforge:conditions") != FALSE
    assert talisman["result"] == {"count": 1, "id": "projecte:repair_talisman"}
    assert talisman["ingredients"] == [
        {"item": "forbidden_arcanus:eternal_stella"},
        {"item": "occultism:iesnium_anvil"},
    ], "Repair Talisman must have exactly two named ingredients, no dust"

    for name, dust, center in RODS:
        assert_shaped(recipe(root, name), name, ["DDD", "DSD", "DDD"],
                      {"D": {"item": dust}, "S": {"item": center}})

    for name, bucket, sigil, letter in (
        ("evertide_amulet", "minecraft:water_bucket", "neovitae:sigil_water", "W"),
        ("volcanite_amulet", "minecraft:lava_bucket", "neovitae:sigil_lava", "L"),
    ):
        assert_shaped(recipe(root, name), name,
                      [f"{letter}S{letter}", "DDD", letter * 3],
                      {"D": {"item": "projecte:dark_matter"},
                       letter: {"item": bucket},
                       "S": {"item": sigil}})
        assert sum(row.count("D") for row in recipe(root, name)["pattern"]) == 3

    # MK1 Collector/Relay were integrated independently. Do not rewrite them.
    for name in ("collector_mk1", "relay_mk1"):
        data = recipe(root, name)
        assert data["pattern"][0][1] == "P", f"{name}: Stone gate lost"
        assert data["key"]["P"] == {"item": "projecte:philosophers_stone"}
        assert data["result"] == {"count": 1, "id": f"projecte:{name}"}

    # The TMM configured Neo Vitae material registry, not arbitrary source HEAD,
    # must support the dust IDs used by the first two Divining Rods.
    materials = read(root, "config/neovitae/materials.json")
    by_name = {row["name"]: row for row in materials}
    for name in ("iron", "diamond"):
        assert "dust" in by_name[name]["stages"], f"Neo Vitae {name} dust absent"

    if emc_map is not None:
        require_positive_dust_emc(emc_map)


def main() -> int:
    p = argparse.ArgumentParser()
    p.add_argument("--emc-map", type=Path, help="Same-build post-mapping EMC JSON; must be real, not guessed")
    args = p.parse_args()
    emc_map = None
    if args.emc_map:
        doc = json.loads(args.emc_map.read_text(encoding="utf-8"))
        emc_map = doc.get("values", doc.get("entries", doc))
        if not isinstance(emc_map, dict):
            raise ValueError("EMC map must be JSON object")
    validate(ROOT, emc_map)
    print("PASS: #44 ProjectE recipe-source contract; no Minecraft recipe load performed")
    if emc_map is None:
        print("PENDING: actual post-mapping positive Covalence Dust EMC, "
              "first-Stone knowledge grant and integrated Type C verification")
    else:
        print("PASS: all three Covalence Dust IDs have positive exported EMC (Type A)")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
