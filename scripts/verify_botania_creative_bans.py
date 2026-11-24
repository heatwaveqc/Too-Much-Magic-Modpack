#!/usr/bin/env python3
"""#596 source-only Botania Revived creative recipe guard (not a full-pack proof).

Creative module stays ENABLED: preserve its legitimate infrangible platform.
Each forbidden recipe uses the exact upstream recipe ID, a NeoForge false
condition, AND a barrier-only fallback ingredient if condition loading regresses.
This checks the six known creative Pool/Tablet/Spark recipes only.
"""
from __future__ import annotations

import json
import sys
import tomllib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
PIN = ROOT / "mods/botania-revived.pw.toml"
BASE = "kubejs/data/botania/recipe/creative"
EXPECTED = {
    "terra_plate/creative_pool": ("botania:terra_plate", "botania:creative_pool", None),
    "terra_plate/creative_mana_tablet": ("botania:terra_plate", "botania:mana_tablet", 1),
    "terra_plate/creative_corporea_spark": ("botania:terra_plate", "botania:corporea_spark_creative", None),
    "creative_pool_extreme": ("avaritia:shaped_table", "botania:creative_pool", None),
    "creative_mana_tablet_extreme": ("avaritia:shaped_table", "botania:mana_tablet", True),
    "creative_corporea_spark_extreme": ("avaritia:shaped_table", "botania:corporea_spark_creative", None),
}
KEEP = "terra_plate/infrangible_platform"


def audit(root: Path = ROOT) -> list[str]:
    problems: list[str] = []
    pin = tomllib.loads((root / "mods/botania-revived.pw.toml").read_text(encoding="utf-8"))
    if (pin.get("filename") != "Botania-Revived-1.21.1-2.0.2-NeoForge.jar"
            or pin.get("download", {}).get("hash") != "b333e5ac4fd55b0e8822238ced0b25b193db9628"):
        problems.append("Botania source pin changed: review exact new native recipe IDs before reuse")

    for name, (kind, output, creative_marker) in EXPECTED.items():
        path = root / BASE / (name + ".json")
        if not path.is_file():
            problems.append(f"missing disabled native recipe: {path}")
            continue
        try:
            recipe = json.loads(path.read_text(encoding="utf-8"))
        except (ValueError, OSError) as e:
            problems.append(f"invalid recipe {name}: {e}")
            continue
        if recipe.get("neoforge:conditions") != [{"type": "neoforge:false"}]:
            problems.append(f"{name}: must fail NeoForge recipe conditions")
        if recipe.get("type") != kind:
            problems.append(f"{name}: native serializer changed")
        result = recipe.get("result", {})
        if result.get("id") != output or result.get("count") != 1:
            problems.append(f"{name}: unexpected result")
        if creative_marker is not None:
            value = result.get("components", {}).get("minecraft:custom_data", {}).get("creative")
            if type(value) is not type(creative_marker) or value != creative_marker:
                problems.append(f"{name}: creative stack component no longer explicitly guarded")
        if kind == "botania:terra_plate":
            if recipe.get("ingredients") != [{"item": "minecraft:barrier"}] or recipe.get("mana", 0) < 1:
                problems.append(f"{name}: missing uncraftable barrier-only fallback")
        elif (recipe.get("key") != {"X": {"item": "minecraft:barrier"}}
                or recipe.get("pattern") != ["XXXXXXXXX"] * 9
                or recipe.get("tier") != 4):
            problems.append(f"{name}: missing uncraftable 9x9 fallback")

    if (root / BASE / (KEEP + ".json")).exists():
        problems.append("legitimate infrangible platform recipe must remain native")
    return problems


if __name__ == "__main__":
    failures = audit(Path(sys.argv[1]) if len(sys.argv) > 1 else ROOT)
    if failures:
        print("\n".join(failures), file=sys.stderr)
        sys.exit(1)
    print("PASS: all six source-verified Botania creative outputs have disabled recipe IDs")
    print("NOTE: not a full creative-item loot/EMC/component acquisition audit; Packwiz indexing pending")
