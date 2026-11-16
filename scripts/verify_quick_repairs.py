#!/usr/bin/env python3
"""Source-only regression guard, no Minecraft boot or world mutation."""
import hashlib
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
def read(p): return (ROOT / p).read_text(encoding="utf-8")

def main():
    rec = json.loads(read("compat/progression/src/main/resources/data/tmm_progression/recipe/bio_mutagen.json"))
    assert rec["type"] == "minecraft:crafting_shapeless" and len(rec["ingredients"]) == 9
    assert all(isinstance(v, dict) and set(v) == {"item"} for v in rec["ingredients"])
    ids = [v["item"] for v in rec["ingredients"]]
    assert ids.count("minecraft:moss_block") == 4
    assert ids.count("minecraft:rotten_flesh") == 4
    assert ids.count("minecraft:spider_eye") == 1
    intro = read("config/ftbquests/quests/chapters/intro.snbt")
    assert intro.count('id: "enigmaticlegacyplus:unwitnessed_amulet"') == 1
    assert intro.count('id: "enigmaticlegacyplus:cursed_ring"') == 1
    assert intro.count('id: "ars_elemental:curio_bag"') == 2 # icon + reward
    assert not (ROOT / "kubejs/data/tmm/function/rewards/first_log.mcfunction").exists()
    log_quest = intro.split('id: "544D4D1100000001"', 1)[1].split("\n\t\t}", 1)[0]
    assert 'id: "544D701000200003"' in log_quest
    assert 'id: "minecraft:torch"' not in log_quest
    a = json.loads(read("kubejs/data/tmm/advancement/first_log.json"))
    assert "rewards" not in a and a["criteria"]["first_log"]["conditions"]["items"][0]["items"] == "#minecraft:logs"
    authored = json.loads(read("manifest/quest_content.json"))["quests"]
    resolved = json.loads(read("manifest/quest_content_resolved.json"))["quests"]
    for records in (authored, resolved):
        welcome = next(v for v in records if v["chapter"] == "Getting Started" and v["title"] == "Welcome to Too Much Magic")
        assert welcome["reward"]["item"] == "enigmaticlegacyplus:unwitnessed_amulet"
    for rec in authored:
        if rec["title"] == "Punch a Tree" and rec["chapter"] == "Getting Started":
            assert rec["reward"] == {"xp": 10, "item": "ars_elemental:curio_bag", "count": 1}
            assert "claim this quest" in rec["description"]
    dragon = read("defaultconfigs/dragonsurvival-server.toml")
    for s in ("start_with_dragon_choice = false", "allow_dragon_choice_from_inventory = false",
              "transform_altar = true", "no_humans_allowed = false"):
        assert s in dragon, s
    enig = read("defaultconfigs/enigmaticlegacyplus-server.toml")
    assert "[sevenCurses]" in enig and "giveStarterGear = false" in enig
    assert "feature/traders-vengence" in json.loads(read("manifest/integration-sources.json"))["deferred_pending_runtime"]
    index, pack = read("index.toml"), read("pack.toml")
    assert f'hash = "{hashlib.sha256(index.encode()).hexdigest()}"' in pack
    files = ("config/ftbquests/quests/chapters/intro.snbt",
             "config/ftbquests/quests/lang/en_us.snbt",
             "kubejs/data/tmm/advancement/first_log.json",
             "defaultconfigs/dragonsurvival-server.toml",
             "defaultconfigs/enigmaticlegacyplus-server.toml")
    for p in files:
        digest = hashlib.sha256(read(p).encode()).hexdigest()
        assert f'file = "{p}"\\nhash = "{digest}"'.replace("\\n", "\n") in index, p
    print("PASS quick fixes: recipe shape, reward IDs, no auto-gifts, configs, trader registry, packwiz chain")

if __name__ == "__main__":
    main()
