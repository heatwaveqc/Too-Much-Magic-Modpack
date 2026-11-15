#!/usr/bin/env python3
"""Build/check B19 Gateways to Eternity native JSON from the verified manual mob cohort.

Inputs: manifest/gateway_required_mobs.json. Writes to a precisely scoped,
generator-owned family of kubejs/data/tmm/gateways/mob_*.json and
kubejs/data/tmm/recipe/gateways/mob_*.json files.
After --write, update packwiz index.toml and pack.toml before playing.
"""
from __future__ import annotations

import argparse
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MANIFEST = ROOT / "manifest/gateway_required_mobs.json"
ID = re.compile(r"^[a-z0-9_.-]+:[a-z0-9_./-]+$")


def render(manifest: dict) -> dict[str, str]:
    assert manifest["gateway_mod"] == "GatewaysToEternity-1.21.1-5.1.0"
    targets = manifest["targets"]
    assert targets
    assert len({x["entity"] for x in targets}) == len(targets), "duplicate entity"
    files: dict[str, str] = {}
    lang: dict[str, str] = {}
    recipes: set[str] = set()
    for t in targets:
        entity = t["entity"]
        assert ID.fullmatch(entity), entity
        assert t["family"] in ("ars", "irons"), entity
        assert t["tier"] in ("common", "elite"), entity
        assert t.get("kind", "combat") in ("combat", "friendly_reward"), entity
        if t.get("kind") == "friendly_reward":
            assert ID.fullmatch(t["encounter_entity"]), entity
            assert t["family"] == "ars", entity
        assert ID.fullmatch(t["catalyst"]) and t["catalyst"].startswith("minecraft:"), entity
        rid = f"mob_{t['family']}_{entity.split(':', 1)[1]}"
        assert rid not in recipes, rid
        recipes.add(rid)
        gateway = f"tmm:{rid}"
        elite = t["tier"] == "elite"
        n = [1, 1, 1] if elite else [1, 2, 3]
        friendly = t.get("kind") == "friendly_reward"
        opponent = t["encounter_entity"] if friendly else entity
        rewards = ([{"type": "gateways:summon", "entity": {
            "entity": entity, "count": 1, "finalize_spawn": True}}] if friendly else [])
        rewards.append({"type": "gateways:experience", "experience": 100 if elite else 40, "orb_size": 20})
        definition = {
            "type": "gateways:normal",
            "size": "medium" if elite else "small",
            "color": "#5AA6D8" if t["family"] == "ars" else "#9D63C9",
            "rules": {
                "spawn_range": 8, "leash_range": 32, "spacing": 48,
                "remove_mobs_on_failure": True, "requires_nearby_player": True},
            "waves": [
                {"entities": [{"entity": opponent, "count": count, "finalize_spawn": True}],
                 "max_wave_time": 1400 if elite else 1000,
                 "setup_time": 100 if i == 0 else 160}
                for i, count in enumerate(n)],
            "rewards": rewards,
            "failures": []}
        recipe = {
            "type": "gateways:gate_recipe", "group": "tmm_progression_mobs",
            "pattern": [" C ", "SMS", " E "],
            "key": {
                "C": {"item": t["catalyst"]},
                "S": {"item": "minecraft:iron_ingot"},
                "M": {"item": manifest["recipe"]["magic_token"][t["family"]]},
                "E": {"item": "minecraft:ender_pearl"}},
            "result": {"id": "gateways:gate_pearl"},
            "gateway": gateway}
        files[f"kubejs/data/tmm/gateways/{rid}.json"] = json.dumps(definition, indent=2) + "\n"
        files[f"kubejs/data/tmm/recipe/gateways/{rid}.json"] = json.dumps(recipe, indent=2) + "\n"
        lang[gateway.replace(":", ".")] = t["display_name"] + " Encounter"
    files["kubejs/assets/tmm/lang/en_us.json"] = json.dumps(lang, indent=2) + "\n"
    return files


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--write", action="store_true")
    args = ap.parse_args()
    expected = render(json.loads(MANIFEST.read_text(encoding="utf-8")))
    existing = [str(p.relative_to(ROOT)) for prefix in (
        "kubejs/data/tmm/gateways", "kubejs/data/tmm/recipe/gateways")
        for p in (ROOT / prefix).glob("mob_*.json")]
    if args.write:
        for old in existing:
            if old not in expected:
                (ROOT / old).unlink()
        for path, content in expected.items():
            p = ROOT / path
            p.parent.mkdir(parents=True, exist_ok=True)
            p.write_text(content, encoding="utf-8")
        print(f"WROTE {len(expected)} files; run packwiz refresh and runtime QA")
    else:
        assert set(existing) == {p for p in expected if p.endswith(".json") and "/lang/" not in p}, "generated JSON files mismatch catalog"
        for path, content in expected.items():
            assert (ROOT / path).read_text(encoding="utf-8") == content, f"out of date: {path}"
        print(f"PASS: {len(expected)} generated gateway/recipe/lang files match manifest; no runtime claims")


if __name__ == "__main__":
    main()
