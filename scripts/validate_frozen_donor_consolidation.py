#!/usr/bin/env python3
"""Source checks only; no Minecraft boot or artifact generation."""
import json
import tomllib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
def read(path):
    return (ROOT / path).read_text(encoding="utf-8")
def data(path):
    return json.loads(read(path))
def main():
    provenance = data("docs/qa/frozen-donor-reference/intake.json")
    current = {s["name"]: s for s in data("manifest/integration-sources.json")["streams"]}
    index = {x["file"] for x in tomllib.loads(read("index.toml"))["files"]}
    for old in provenance["streams"]:
        stream = current[old["stream"]]
        assert stream["integrated_into_baseline"] and stream["branch"] == "test/full-integration", old
        assert stream["source_commit"] == old["source_commit"], old
    assert not any(x.startswith("integration/") for x in index)
    for n in ("roots_terra_client.js", "roots_staff_first_pickup.js", "roots_terra_server.js", "mahou_neo_vitae_catalyst_exchange.js"):
        assert not any(x.endswith("/" + n) for x in index), n
    assert not any(x.startswith("kubejs/data/werewolves/") for x in index)
    locale = data("kubejs/assets/tmm/lang/en_us.json")
    assert set(("curios.identifier.bundle", "tmm.caerbannog", "tmm.chaos_bunny")) <= locale.keys()
    shop = data("config/qshop/shops/tmm.json")
    ore_tab = next(t for t in shop["tabs"] if t["uuid"] == "tmm-ores")
    ores = [entry["item"]["item"] for entry in ore_tab["entries"]]
    assert len(ores) == 18 and all(item.startswith("minecraft:") for item in ores)
    assert "minecraft:ancient_debris" not in ores
    assert len(shop["tabs"]) == 6
    assert sum(len(tab["entries"]) for tab in shop["tabs"]) == 48
    kit = read("kubejs/data/tmm/function/rewards/starter_kit.mcfunction")
    assert "give @s akashictome:tome 1" in kit and "give @s minecraft:cooked_beef 2" in kit
    assert "improvableskills book unlock @s true" in kit
    assert "kubejs/data/tmm/advancement/first_log.json" not in index
    assert "kubejs/data/tmm/function/rewards/first_log.mcfunction" not in index
    assert read("config/ftbessentials.snbt") == read("defaultconfigs/ftbessentials-server.snbt")
    for mid in ("almostunified", "ftb-essentials"):
        assert "mods/" + mid + ".pw.toml" in index
    assert not any("hexcast-media-pilot" in x for x in index)
    print("PASS: 12 baseline-owned frozen streams, safe runtime source boundaries, starter and ore contracts")
if __name__ == "__main__":
    main()
