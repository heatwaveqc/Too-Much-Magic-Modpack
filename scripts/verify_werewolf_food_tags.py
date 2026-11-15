#!/usr/bin/env python3
"""Static guardrail for the Relics/Werewolves missing-food-tag regression.

This validates authored datapack JSON. It does NOT prove KubeJS datapack
precedence or live food consumption; check a fresh combined-server log and
then test a disposable world with a real Werewolves player.
"""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TAGS = ROOT / "kubejs/data/werewolves/tags/item"


def entries(filename):
    data = json.loads((TAGS / f"{filename}.json").read_text(encoding="utf-8"))
    assert isinstance(data.get("replace"), bool), f"{filename}: explicit replace required"
    assert isinstance(data.get("values"), list) and data["values"], f"{filename}: no values"
    return data


def has(values, id, required):
    return any(isinstance(v, dict) and v.get("id") == id
               and v.get("required") is required for v in values)


def main():
    base = entries("werewolf_food")
    assert base["replace"] is True, "Must override broken Relics additive contribution"
    originals = {
        "#werewolves:raw_food", "#werewolves:cooked_fish", "#minecraft:meat",
        "werewolves:wolf_berries", "minecraft:sweet_berries", "minecraft:glow_berries",
    }
    assert originals.issubset(set(v for v in base["values"] if isinstance(v, str))), (
        "Original native Werewolves diet lost"
    )
    assert "relics:infinity_ham" not in base["values"], "Unconditional Relics reference reintroduced"
    assert has(base["values"], "relics:infinity_ham", False), "Relics item must be optional"
    assert has(base["values"], "#werewolves:cooked_food", False), (
        "Modded cooked meat must reach the main werewolf diet"
    )
    expected = {
        "raw_fish": "#c:foods/raw_fish",
        "cooked_fish": "#c:foods/cooked_fish",
        "raw_food": "#c:foods/raw_meat",
        "cooked_food": "#c:foods/cooked_meat",
    }
    for filename, tag in expected.items():
        data = entries(filename)
        assert data["replace"] is False, f"{filename}: do not erase native categories"
        assert has(data["values"], tag, False), f"{filename}: missing optional {tag}"
    print("PASS: native Werewolves foods preserved; Relics infinity ham optional;")
    print("      modern raw/cooked meat and fish tags bridged without replacing category tags")
    print("NOT TESTED: live tag loading, optional Relics item, actual nutrition, factions/UI")


if __name__ == "__main__":
    main()
