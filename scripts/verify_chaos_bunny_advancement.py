#!/usr/bin/env python3
"""Type A only: tagged Chaos Bunny death -> one permanent per-player trophy.

The owner-approved new persistent-boss lifecycle is NOT satisfied until the
existing six-wave JSON is replaced by a five-wave ritual and the independent
spawn/teleport registry is implemented. This test proves only advancement
configuration, not actual 1.21.1 in-game kill attribution.
"""
import hashlib
import json
from pathlib import Path
import tomllib

ROOT = Path(__file__).resolve().parents[1]
ADV = "kubejs/data/tmm/advancement/chaos_bunny_slayer.json"
FN = "kubejs/data/tmm/function/rewards/chaos_bunny_trophy.mcfunction"

def main():
    adv = json.loads((ROOT / ADV).read_text())
    assert set(adv["criteria"]) == {"slay_chaos_bunny"}
    crit = adv["criteria"]["slay_chaos_bunny"]
    assert crit["trigger"] == "minecraft:player_killed_entity"
    assert crit["conditions"]["entity"] == {
        "type": "minecraft:rabbit",
        "nbt": "{Tags:['tmm_caerbannog_boss']}",
    }
    assert adv["requirements"] == [["slay_chaos_bunny"]]
    assert adv["rewards"] == {"function": "tmm:rewards/chaos_bunny_trophy"}
    assert adv["sends_telemetry_event"] is False
    fn = (ROOT / FN).read_text().splitlines()
    commands = [s.strip() for s in fn if s.strip() and not s.lstrip().startswith("#")]
    assert commands == ["give @s kubejs:chaos_bunny_trophy 1"], commands
    # Ordinary horde rabbits must never have the boss tag.
    gateway = json.loads((ROOT / "kubejs/data/tmm/gateways/chaos_bunny.json").read_text())
    for wave in gateway["waves"][:5]:
        for entity in wave["entities"]:
            assert "tmm_caerbannog_boss" not in entity["nbt"].get("Tags", [])
    index = tomllib.loads((ROOT / "index.toml").read_text())
    pack = tomllib.loads((ROOT / "pack.toml").read_text())
    assert hashlib.sha256((ROOT / "index.toml").read_bytes()).hexdigest() == pack["index"]["hash"]
    registered = {v["file"]:v for v in index["files"]}
    for name in (ADV,FN):
        assert name in registered, name + " not Packwiz indexed"
        assert registered[name]["hash"] == hashlib.sha256((ROOT / name).read_bytes()).hexdigest()
    print("PASS: exact tagged-kill per-player advancement, 1 function grant, no ordinary rabbit, Packwiz checks. No game boot.")

if __name__ == "__main__":
    main()
