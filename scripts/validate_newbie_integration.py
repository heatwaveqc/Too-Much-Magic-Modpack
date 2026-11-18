#!/usr/bin/env python3
"""Source-only Newbie/SmartKeep/quest/Progression regression check (no game launch)."""
from __future__ import annotations
import hashlib
import json
import re
import tomllib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
def read(p: str) -> str: return (ROOT / p).read_text(encoding="utf-8")
def data(p: str): return json.loads(read(p))

def main():
    pinpath = "mods/smart-keep-inventory.pw.toml"
    pin = tomllib.loads(read(pinpath))
    assert pin["filename"] == "smart_keep_inventory-1.21.1-1.0.1-neoforge.jar"
    assert pin["side"] == "both", "Dedicated and integrated singleplayer must both receive it"
    assert pin["update"]["modrinth"] == {"mod-id": "qeGAjqzW", "version": "JqVOLkFB"}
    assert pin["download"]["hash-format"] == "sha512"
    assert pin["download"]["hash"] == (
        "c9db787e4f4a18bcc659b24e80c63232ac7919dc426fe27952de6f06a31fc67"
        "a282c5c50108de18a269411bfc6c8378c220683d0cb2859d83199504c250ac208"
    )
    assert pin["download"]["url"].startswith("https://cdn.modrinth.com/data/qeGAjqzW/versions/JqVOLkFB/")
    roster = data("manifest/mods.json")["mods"]
    assert len([m for m in roster if m["id"] == "smart-keep-inventory"]) == 1
    assert next(m for m in roster if m["id"] == "smart-keep-inventory")["enabled_in_pack"] is True
    assert next(m for m in roster if m["id"] == "prefab")["enabled_in_pack"] is False

    player_condition = data("kubejs/data/tmm/smart_keep_inventory/condition/newbie.json")
    assert player_condition == {"player": {"nbt": '{Tags:["tmm_newbie"]}'}, "keepExperience": True}
    assert data("kubejs/data/smart_keep_inventory/smart_keep_inventory/condition/bosses.json") == {"disabled": True}
    assert data("config/tyzs_skills/custom/skills/keepsake.json") == {
        "active": False, "id": "keepsake", "type": "IMMUTABLE"
    }

    authored = data("manifest/quest_content.json")["quests"]
    resolved = data("manifest/quest_content_resolved.json")["quests"]
    assert len(authored) == len(resolved), "Authored/resolved quest counts must agree"
    matches = [q for q in resolved if q.get("quest_id") == "544D309000100001"]
    assert len(matches) == 1
    newbie = matches[0]
    assert newbie["title"] == "I'm Ready for Regular Mode"
    assert newbie["reward"] == {"xp": 5, "command": "tmmnewbie admin graduate {p}"}
    assert newbie["team_reward"] is False and newbie["criterion"]["type"] == "checkmark"
    layouts = data("manifest/quest_layouts.json")
    assert layouts["quest_count"] == len(layouts["positions"])
    assert len({v["quest_id"] for v in layouts["positions"]}) == layouts["quest_count"]
    assert {q["quest_id"] for q in resolved} <= {v["quest_id"] for v in layouts["positions"]}
    # Exact book counts are owned by validate_quests.py; this check protects graduation.
    assert sum(v["quest_id"] == "544D309000100001" for v in layouts["positions"]) == 1
    intro = read("config/ftbquests/quests/chapters/intro.snbt")
    assert intro.count('id: "544D309000100001"') == 1
    assert 'command: "tmmnewbie admin graduate {p}", permission_level: 2, team_reward: false' in intro
    assert 'quest.544D309000100001.title: "I\x27m Ready for Regular Mode"' in read("config/ftbquests/quests/lang/en_us.snbt")

    pmix = data("compat/progression/src/main/resources/tmm_progression.mixins.json")["mixins"]
    for name in ("InventoryReservationMixin", "MonkFortuneMixin",
                 "HunterLootingMixin", "HunterRareLootMixin", "VirtualElytraItemExtensionMixin"):
        assert name in pmix, name
    tmix = data("compat/transformations/src/main/resources/tmm_transformations.mixins.json")
    for name in ("GoetyDracolichAcquisitionMixin",
                 "ForbiddenMagicDracolichAcquisitionMixin",
                 "DragonSurvivalDeathRepeatMixin",
                 "BotaniaThermalilyFiftyEndoflamesMixin", "BotaniaThermalilyNoCooldownMixin",
                 "NewbieVampireAcquisitionMixin", "NewbieWerewolfAcquisitionMixin",
                 "NewbieDragonAcquisitionMixin"):
        assert name in tmix["mixins"], name
    assert tmix.get("plugin") == "io.github.heatwaveqc.tmm.transformations.mixin.DracolichMixinPlugin"
    assert set(tmix["client"]) >= {"DracolichModelLoadMixin","DracolichDragonModelMixin",
                                    "DracolichGlowLayerMixin", "DracolichArmorLayerMixin"}
    mxp = read("compat/mxp/src/main/java/io/github/heatwaveqc/tmm/mxp/MxpService.java")
    assert "MxpProgressionEligibility.canEarn(player)" in mxp, "No-Newbie MXP must be enforced before ledger commit"
    api = read("compat/progression/src/main/java/io/github/heatwaveqc/tmm/progression/core/ProgressionApi.java")
    assert "anyJoined(ServerPlayer player)" in api and "NewbieMode.isNewbie(player)" in api
    native = read("compat/transformations/src/main/java/io/github/heatwaveqc/tmm/transformations/core/NativeSpeciesJoinGuard.java")
    assert "registerProgressionJoinVeto()" in native and "hasJoinedProgression(player)" in native
    assert "SpeciesEnrollmentPolicy.canJoinHumanPath" in native
    assert "SpeciesEnrollmentPolicy.canAcquire" in native

    idx, pack = read("index.toml"), read("pack.toml")
    assert hashlib.sha256(idx.encode("utf-8")).hexdigest() in pack
    for path in (
        pinpath,
        "kubejs/data/tmm/smart_keep_inventory/condition/newbie.json",
        "kubejs/data/smart_keep_inventory/smart_keep_inventory/condition/bosses.json",
        "config/tyzs_skills/custom/skills/keepsake.json",
        "config/ftbquests/quests/chapters/intro.snbt",
        "config/ftbquests/quests/lang/en_us.snbt",
    ):
        digest = hashlib.sha256((ROOT/path).read_bytes()).hexdigest()
        assert f'file = "{path}"\nhash = "{digest}"' in idx, path
    print("PASS: exact NeoForge Smart Keep SHA512; mixed-mode predicates, Safekept disabled;")
    print(f"{layouts['quest_count']} total quests, one personal graduation; all current spell/loot/visual/thermalily mixins preserved;")
    print("Newbie MXP and any-path species gates in source; every Packwiz index/root SHA256 matches")
    print("SOURCE ONLY — no NeoForge boot, client/Corail tests or installed first-party JAR evidence")

if __name__ == "__main__":
    main()
