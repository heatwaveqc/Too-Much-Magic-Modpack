#!/usr/bin/env python3
"""Static source contract for #306; not an installed-registry or client test."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "kubejs/data/tmm/function/rewards/starter_kit.mcfunction"
EXPECTED = {
    "occultism:dictionary_of_spirits": "occultism",
    "evilcraft:origins_of_darkness": "evilcraft",
    "theurgy:grimiore": "theurgy",
    "draconicevolution:info_tablet": "draconicevolution",
}

def test_source() -> None:
    lines = SOURCE.read_text(encoding="utf-8").splitlines()
    tome_lines = [line for line in lines if line.startswith("give @s akashictome:tome")]
    assert len(tome_lines) == 1, "Expected exactly one first-join Tome grant"
    prefix = "give @s akashictome:tome[akashictome:tool_content="
    suffix = "] 1"
    line = tome_lines[0]
    assert line.startswith(prefix) and line.endswith(suffix)
    contents = json.loads(line[len(prefix):-len(suffix)])
    assert len(contents) == len(EXPECTED)
    assert {book["id"] for book in contents} == set(EXPECTED)
    for book in contents:
        assert book["count"] == 1
        assert book["components"] == {"akashictome:defined_mod": EXPECTED[book["id"]]}
    assert lines.count("give @s minecraft:cooked_beef 2") == 1
    assert lines.count("improvableskills book unlock @s true") == 1
    assert not any(line.startswith("clear ") for line in lines), "No destructive inventory cleanup"

if __name__ == "__main__":
    test_source()
    print("PASS: populated Tome payload, four source candidates, and preserved starter grants")
