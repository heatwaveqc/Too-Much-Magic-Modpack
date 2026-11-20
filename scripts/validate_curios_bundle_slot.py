#!/usr/bin/env python3
"""Source-only regression guard for the custom Curios bundle slot (#309).

Curios 9.5.1+1.21.1 upstream ships
assets/curios/textures/slot/empty_curio_slot.png and its own
data/curios/curios/slots/curio.json references
"curios:slot/empty_curio_slot" (singular). The old plural icon was absent.
Do not treat a matching source name as client texture/atlas acceptance.
"""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SLOT_PATH = "kubejs/data/tmm/curios/slots/bundle.json"
PLAYER_PATH = "kubejs/data/tmm/curios/entities/player.json"
TAG_PATH = "kubejs/data/curios/tags/item/bundle.json"
PIN_PATH = "mods/curios.pw.toml"

# Curios' built-in icon; deliberately not the broken "empty_curios_slot".
CURIO_ICON = "curios:slot/empty_curio_slot"
BAG_IDS = {"ars_elemental:curio_bag", "ars_elemental:caster_bag"}


def validate(slot: dict, players: dict, item_tag: dict, pin: str) -> None:
    expected = {
        "size": 1,
        "operation": "SET",
        "order": 50,
        "icon": CURIO_ICON,
        "validators": ["curios:tag"],
    }
    if slot != expected:
        raise ValueError(
            f"Curios bundle slot changed or icon invalid: expected {expected}, got {slot}"
        )
    if "minecraft:player" not in players.get("entities", []):
        raise ValueError("Bundle Curios player entity assignment missing")
    if "bundle" not in players.get("slots", []):
        raise ValueError("Bundle Curios slot ID renamed or missing")
    if not BAG_IDS.issubset(set(item_tag.get("values", []))):
        raise ValueError("Ars Curio and Caster bags must remain equipable in bundle slot")
    if 'filename = "curios-neoforge-9.5.1+1.21.1.jar"' not in pin:
        raise ValueError("Curios pin changed: reverify the actual built-in icon/atlas")


def main(root: Path = ROOT) -> None:
    def read_json(path: str) -> dict:
        return json.loads((root / path).read_text(encoding="utf-8"))

    validate(
        read_json(SLOT_PATH),
        read_json(PLAYER_PATH),
        read_json(TAG_PATH),
        (root / PIN_PATH).read_text(encoding="utf-8"),
    )
    print("PASS: Curios 9.5.1 built-in singular icon, bundle slot identity, player assignment and bag tag")
    print("SOURCE ONLY: client atlas, empty/occupied UI and equipping still require Prism QA")


if __name__ == "__main__":
    main()
