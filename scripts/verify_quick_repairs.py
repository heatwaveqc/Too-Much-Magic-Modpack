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
    assert "give @s" not in read("kubejs/data/tmm/function/rewards/first_log.mcfunction")
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
             "kubejs/data/tmm/function/rewards/first_log.mcfunction",
             "defaultconfigs/dragonsurvival-server.toml",
             "defaultconfigs/enigmaticlegacyplus-server.toml")
    for p in files:
        digest = hashlib.sha256(read(p).encode()).hexdigest()
        assert f'file = "{p}"\\nhash = "{digest}"'.replace("\\n", "\n") in index, p
    print("PASS quick fixes: recipe shape, reward IDs, no auto-gifts, configs, trader registry, packwiz chain")

if __name__ == "__main__":
    main()
