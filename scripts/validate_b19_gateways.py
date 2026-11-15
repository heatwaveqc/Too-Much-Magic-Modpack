#!/usr/bin/env python3
"""Validate B19 gateway content and packwiz hashes. Static only, not server boot."""
from __future__ import annotations
import hashlib
import json
import subprocess
import sys
import tomllib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
def read(p): return (ROOT / p).read_text(encoding="utf-8")
def main():
    source = json.loads(read("manifest/gateway_required_mobs.json"))
    assert source["minecraft"] == "1.21.1"
    assert len(source["targets"]) >= 14
    assert source["policy"]["no_extra_mob_loot"]
    forbidden = set(source["policy"]["native_boss_and_friendly_npc_exclusions"])
    assert all(t["entity"] not in forbidden for t in source["targets"])
    assert {t["entity"] for t in source["targets"] if t["family"] == "ars"} == {
        "ars_nouveau:wilden_hunter", "ars_nouveau:wilden_stalker", "ars_nouveau:wilden_guardian"}
    assert {"irons_spellbooks:necromancer", "irons_spellbooks:citadel_keeper"}.issubset({
        t["entity"] for t in source["targets"]})
    r = subprocess.run([sys.executable, str(ROOT / "scripts/build_progression_gateways.py")],
                       capture_output=True, text=True)
    assert r.returncode == 0, r.stderr + "\n" + r.stdout
    index = read("index.toml")
    pack = tomllib.loads(read("pack.toml"))
    assert hashlib.sha256(index.encode("utf-8")).hexdigest() == pack["index"]["hash"]
    for t in source["targets"]:
        rid = f"mob_{t['family']}_{t['entity'].split(':', 1)[1]}"
        gp = f"kubejs/data/tmm/gateways/{rid}.json"
        rp = f"kubejs/data/tmm/recipe/gateways/{rid}.json"
        gate = json.loads(read(gp))
        recipe = json.loads(read(rp))
        assert recipe["type"] == "gateways:gate_recipe"
        assert recipe["gateway"] == f"tmm:{rid}"
        assert recipe["result"] == {"id": "gateways:gate_pearl"}
        assert len(gate["waves"]) == 3
        assert sum(w["entities"][0]["count"] for w in gate["waves"]) == (3 if t["tier"] == "elite" else 6)
        assert all(w["entities"][0]["entity"] == t["entity"] for w in gate["waves"])
        assert not gate["failures"] and gate["rules"]["remove_mobs_on_failure"]
        assert all(not w.get("rewards") for w in gate["waves"]), "No bonus mob loot from waves"
        for path in (gp, rp):
            digest = hashlib.sha256(read(path).encode("utf-8")).hexdigest()
            assert f'file = "{path}"\nhash = "{digest}"' in index, path
    langpath = "kubejs/assets/tmm/lang/en_us.json"
    digest = hashlib.sha256(read(langpath).encode("utf-8")).hexdigest()
    assert f'file = "{langpath}"\nhash = "{digest}"' in index
    print(f"PASS: {len(source['targets'])} dedicated spawn Gateways, exact JSON schema shape, source match, correct packwiz hashes")
    print("PENDING: actual mob registry, Gate Pearl JEI, loot, mob AI, dedicated-server and client QA")

if __name__ == "__main__":
    main()
