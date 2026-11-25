#!/usr/bin/env python3
"""Post-assembly gate: detect any retired runtime residue before publication."""
from pathlib import Path
import json, tomllib
ROOT = Path(__file__).resolve().parents[1]
manifest = json.loads((ROOT/"manifest/mods.json").read_text())
mods = {m["id"] for m in manifest["mods"]}
assert not mods.intersection({"vampirism", "werewolves", "vampires-delight"})
index = tomllib.loads((ROOT/"index.toml").read_text())
paths = {e["file"] for e in index["files"]}
for path in paths:
    assert not path.startswith(("mods/vampirism-","mods/werewolves-",
        "mods/vampires-delight","kubejs/data/werewolves/",
        "config/tmm_transformations","defaultconfigs/tmm_transformations",
        "defaultconfigs/vampirism-server")), path
    assert not path.endswith(("chapters/vampirism.snbt","chapters/werewolves.snbt")), path
    assert "vampirism__" not in path, path
for path in ("kubejs/server_scripts/silver_equipment_unification.js",
             "kubejs/startup_scripts/silver_equipment_stats.js",
             "kubejs/server_scripts/material_processing_cleanup.js",
             "config/almostunified/unification/materials.json",
             "manifest/stone_generation_policy.json"):
    t = (ROOT/path).read_text()
    assert "werewolves:" not in t and "vampirism:" not in t, path
reg = json.loads((ROOT/"manifest/integration-sources.json").read_text())
streams = {x["name"] for x in reg["streams"]}
assert "standalone-monk-hunter-progression" in streams
assert "jawr-native-transformation-bridge" in streams
assert next(s for s in reg["streams"] if s["name"] == "jawr-native-transformation-bridge")["mode"] == "archived-source-test-only-not-runtime"
selected = json.loads((ROOT/"manifest/first-party-runtime.json").read_text())
assert "tmm_transformations" not in {m["mod_id"] for m in selected["modules"]}
assert not ({"vampire-bridge", "monk-faction", "werewolf-food-relics-compat"} & streams)
assert "kubejs/server_scripts/dragon_diets.js" in paths
assert "config/ftbquests/quests/chapters/just_another_witchery_remake.snbt" in paths
print("PASS: TeamLapen retired; unified Tyr Progression with internal native transformation bridge selected; retired standalone JAR absent")
