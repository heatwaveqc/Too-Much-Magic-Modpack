#!/usr/bin/env python3
"""Offline Theurgy chapter and procedural PNG regression; no Minecraft/network."""
from __future__ import annotations

import json
import struct
import sys
import tempfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "scripts"))
from build_theurgy_quest_pilot import check_spec, render, ASSETS
from build_theurgy_quest_art import SIZE, draw


def main() -> None:
    spec = json.loads((ROOT / "manifest/theurgy_quest_pilot.json").read_text(encoding="utf-8"))
    check_spec(spec)
    outputs = render(spec)
    assert len(spec["quests"]) == 20
    assert len({q["key"] for q in spec["quests"]}) == 20
    for name in outputs:
        actual = (ROOT / "docs/quest-pilots/theurgy/generated" / name).read_text(encoding="utf-8")
        assert actual == outputs[name], f"Drifted staged Theurgy source: {name}"
        assert "undefined" not in actual, f"Non-serializable Theurgy content: {name}"
    chapter = (ROOT / "config/ftbquests/quests/chapters/theurgy.snbt").read_text(encoding="utf-8")
    assert chapter == outputs["theurgy.snbt"], "Canonical Theurgy differs from frozen authoring source"
    assert chapter.count('\n\t\t{\n\t\t\tid: "') == 20
    assert chapter.count("textures/questpics/theurgy/") == 5
    assert 'id: "544D303001500001"' in chapter
    assert 'id: "544D403001500001"' in chapter
    assert 'id: "544D903001500001"' in chapter
    assert 'id: "544DA03001500002"' in chapter
    eng = (ROOT / "config/ftbquests/quests/lang/en_us.snbt").read_text(encoding="utf-8")
    ids = json.loads((ROOT / "docs/quest-pilots/theurgy/generated/quest_id_map.json").read_text(encoding="utf-8"))
    for node in spec["quests"]:
        qid = ids[node["key"]]["quest_id"]
        assert f"quest.{qid}.title:" in eng
        assert f"quest.{qid}.quest_desc:" in eng
        assert node["description"] in eng, f"Missing Theurgy source prose: {node['key']}"
        assert isinstance(node["reward"]["count"], int) and node["reward"]["count"] > 0
        assert node["reward"]["item"].count(":") == 1
    assert not any("atm:textures" in text for text in outputs.values())
    assert {a[0] for a in ASSETS} == set(SIZE)
    with tempfile.TemporaryDirectory(prefix="tmm-theurgy-png-") as temp:
        for name, (width, height) in SIZE.items():
            output = Path(temp) / name
            draw(name, width, height).save(output)
            payload = output.read_bytes()
            assert payload.startswith(b"\x89PNG\r\n\x1a\n"), f"Invalid PNG signature: {name}"
            assert struct.unpack(">II", payload[16:24]) == (width, height)
            assert len(payload) > 150, f"Empty art: {name}"
            print(f"PASS art {name}: {width}x{height}, {len(payload)} bytes")
    print("PASS: 20 live Theurgy quests, signed stable IDs, original XP/rewards, 40 locale entries, five procedural images; offline only")


if __name__ == "__main__":
    main()
