#!/usr/bin/env python3
"""Validate the source-declared complete starter-guide manifest and Akashic stack components."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
PREFIX = "give @s akashictome:tome[akashictome:tool_content=["
SUFFIX = "]] 1"

def render_stack(book: dict) -> str:
    components = {"akashictome:defined_mod": book["mod"], **book.get("components", {})}
    fields = ",".join(f"{json.dumps(k)}:{json.dumps(v)}" for k, v in components.items())
    return f'{{id:{json.dumps(book["item"])},count:1,components:{{{fields}}}}}'

def validate_starter_tome() -> None:
    manifest = json.loads((ROOT / "manifest/starter_guidebooks.json").read_text("utf-8"))
    assert manifest["schema"] == 1
    books = manifest["books"]
    assert len(books) >= 11, "Do not regress to a partial four-book starter set"
    assert len({b["mod"] for b in books}) == len(books), "Duplicate selector key"
    assert len({(b["item"], tuple(sorted(b.get("components", {}).items()))) for b in books}) == len(books), "Duplicate exact book"
    for b in books:
        assert re.fullmatch(r"[a-z0-9_.-]+:[a-z0-9_./-]+", b["item"]), b
        assert re.fullmatch(r"[a-z0-9_.-]+", b["mod"]), b
        assert b["name"] and b["source"]
        assert "akashictome:defined_mod" not in b.get("components", {})
        assert all(isinstance(v, str) for v in b.get("components", {}).values())
    by_mod = {b["mod"]: b for b in books}
    assert by_mod["occultism"]["item"] == "occultism:dictionary_of_spirits"
    assert by_mod["rootsclassic"]["item"] == "rootsclassic:runic_tablet"
    assert by_mod["theurgy"]["item"] == "modonomicon:modonomicon"
    assert by_mod["theurgy"]["components"] == {"modonomicon:book_id": "theurgy:the_hermetica"}
    assert by_mod["goety"]["item"] == "patchouli:guide_book"
    assert by_mod["goety"]["components"] == {"patchouli:book": "goety:black_book"}
    assert manifest["pending_review"], "Keep unfinished all-roster audit visible"

    text = (ROOT / "kubejs/data/tmm/function/rewards/starter_kit.mcfunction").read_text("utf-8")
    lines = [s.strip() for s in text.splitlines() if s.strip() and not s.lstrip().startswith("#")]
    assert len(lines) == 3, "Exactly three starter commands"
    expected = PREFIX + ",".join(render_stack(b) for b in books) + SUFFIX
    assert lines[0] == expected, "Tome command must match the manifest and component payload exactly"
    assert lines[1] == "give @s minecraft:cooked_beef 2"
    assert lines[2] == "improvableskills book unlock @s true"
    advancement = json.loads((ROOT / "kubejs/data/tmm/advancement/starter_kit.json").read_text("utf-8"))
    assert advancement["criteria"] == {"first_tick": {"trigger": "minecraft:tick"}}
    assert advancement["rewards"] == {"function": "tmm:rewards/starter_kit"}
    config = (ROOT / "defaultconfigs/ars_nouveau-server.toml").read_text("utf-8")
    assert re.search(r"(?m)^\[general\]$", config)
    assert re.search(r"(?m)^\s*spawnBook\s*=\s*false\s*$", config)
    print(f"PASS: {len(books)} source-verified starter references, including Occultism and Roots Runic Tablet; once-only grant and steaks unchanged")
    print("NOT RUNTIME-PROVEN: complete pinned JAR survey, duplicate native gifts, client parsing and actual book selection")

if __name__ == "__main__":
    validate_starter_tome()
