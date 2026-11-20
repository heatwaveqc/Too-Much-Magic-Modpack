#!/usr/bin/env python3
"""Read-only 1.21.1 starter Tome data-component and no-duplicate-books checks."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
EXPECTED = {
    "ars_nouveau:worn_notebook": "ars_nouveau",
    "botania:lexicon": "botania",
    "occultism:dictionary_of_spirits": "occultism",
    "evilcraft:origins_of_darkness": "evilcraft",
}
# Matches the exact ItemStack.CODEC form used by pinned Akashic 1.8-30.
ENTRY = re.compile(
    r'\{id:"([^"]+)",count:1,components:\{"akashictome:defined_mod":"([^"]+)"\}\}'
)
PREFIX = "give @s akashictome:tome[akashictome:tool_content=["
SUFFIX = "]] 1"

def validate_starter_tome() -> None:
    function = (ROOT / "kubejs/data/tmm/function/rewards/starter_kit.mcfunction").read_text("utf-8")
    commands = [
        line.strip() for line in function.splitlines()
        if line.strip() and not line.lstrip().startswith("#")
    ]
    assert len(commands) == 3, "Starter should grant tome, two steaks and silent Improvable unlock only"
    tome, steaks, unlock = commands
    assert tome.startswith(PREFIX) and tome.endswith(SUFFIX), "Tome lacks native tool_content syntax"
    payload = tome[len(PREFIX):-len(SUFFIX)]
    pairs = ENTRY.findall(payload)
    assert pairs and ",".join(ENTRY.pattern and [
        f'{{id:"{item}",count:1,components:{{"akashictome:defined_mod":"{mod}"}}}}'
        for item, mod in pairs
    ]) == payload, "Malformed or extra nested ItemStack components"
    assert len(pairs) == len(EXPECTED), "Incorrect guidebook count"
    assert dict(pairs) == EXPECTED, "Missing/unverified guidebook or incorrect defined_mod mapping"
    assert len({x for x, _ in pairs}) == len(pairs), "Duplicate guidebook item"
    assert steaks == "give @s minecraft:cooked_beef 2", "Must preserve two ordinary steaks"
    assert unlock == "improvableskills book unlock @s true", "Must preserve silent skill-book unlock"
    advancement = json.loads((ROOT / "kubejs/data/tmm/advancement/starter_kit.json").read_text("utf-8"))
    assert advancement["criteria"] == {"first_tick": {"trigger": "minecraft:tick"}}
    assert advancement["rewards"] == {"function": "tmm:rewards/starter_kit"}
    config = (ROOT / "defaultconfigs/ars_nouveau-server.toml").read_text("utf-8")
    assert re.search(r"(?m)^\[general\]$", config)
    assert re.search(r"(?m)^\s*spawnBook\s*=\s*false\s*$", config)
    print(f"PASS: once-per-player starter Tome has {len(pairs)} native guides, steaks + skill unlock; Ars duplicate grant disabled for new worlds")
    print("SOURCE-ONLY: no installed JAR registry parse, gameplay, existing-world serverconfig migration or other native guidebook grant tested")

if __name__ == "__main__":
    validate_starter_tome()
