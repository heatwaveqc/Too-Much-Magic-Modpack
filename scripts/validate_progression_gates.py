#!/usr/bin/env python3
"""Preflight the *design contract*, never claim the ProjectE gate is active."""
from __future__ import annotations

import json
import pathlib
import sys

ROOT = pathlib.Path(__file__).resolve().parents[1]
REG = ROOT / "manifest/progression-gates.json"
FEATURES = ROOT / "manifest/feature-checklist.json"
MODS = ROOT / "manifest/mods.json"

def main() -> int:
    cfg = json.loads(REG.read_text(encoding="utf-8"))
    if cfg.get("schema_version") != 1:
        raise ValueError("Unsupported gate contract schema")
    stages = {row["id"]: row for row in cfg["stages"]}
    if len(stages) != len(cfg["stages"]) or set(stages) != {
        "basic_projecte", "advanced_emc_and_expansion"
    }:
        raise ValueError("Only the explicitly proposed two ProjectE tiers may be present")
    basic = stages["basic_projecte"]
    advanced = stages["advanced_emc_and_expansion"]
    if basic["gate_logic"] != "ANY_ONE_OF":
        raise ValueError("Early ProjectE is proposed as EITHER boss, not BOTH")
    if {x["name"] for x in basic["eligible_boss_events"]} != {
        "Gaia Guardian I", "Neo Vitae Foreman"
    }:
        raise ValueError("Missing explicit Gaia I or Neo Vitae Foreman alternative")
    if {x["name"] for x in advanced["eligible_boss_events"]} != {
        "Draconic Chaos Guardian"
    }:
        raise ValueError("Chaos must remain the proposed advanced EMC milestone")
    if not cfg.get("do_not_allow_quest_only_gating"):
        raise ValueError("Quests are not permission gates")
    if any(not stage.get("unlock") for stage in stages.values()):
        raise ValueError("A gate stage needs concrete unlock intent")
    mods = {m["id"]: m for m in json.loads(MODS.read_text(encoding="utf-8"))["mods"]}
    for id in ("projecte", "project-expansion", "neovitae", "botania-revived", "draconic-evolution"):
        if id not in mods or not mods[id]["enabled_in_pack"]:
            raise ValueError("ProjectE gate plan references an uninstalled source: " + id)
    features = {
        f["id"]: f for f in json.loads(FEATURES.read_text(encoding="utf-8"))["features"]
    }
    if cfg["runtime_enforcement"] != "NOT_IMPLEMENTED":
        # Do not trust a JSON switch as implementation proof.
        # The evidence path must be independently reviewed as actual server hooks.
        if not any(
            features[x]["state"] == "player_pass" and features[x]["evidence"]
            for x in ("end.projecte_start", "end.projecte_high", "end.expansion")
        ):
            raise ValueError("Gate claims runtime enforcement without owner in-world evidence")
        print("MANUAL REVIEW REQUIRED: claimed runtime gate; verify actual hooks, not only this file")
    else:
        for id in ("end.projecte_start", "end.projecte_high", "end.expansion"):
            if features[id]["state"] == "player_pass":
                raise ValueError("Cannot claim player PASS when boss-gate contract says NOT_IMPLEMENTED")
        print("PASS: proposed ProjectE stages and installed components consistent")
        print("NOT IMPLEMENTED: no executable ProjectE item, recipe, knowledge or machine-use gate")
        print("OPEN: registry IDs, true first-kill hooks, early item allowlist, live JEI and bypass regression")
    return 0

if __name__ == "__main__":
    try:
        sys.exit(main())
    except (KeyError, ValueError, OSError, json.JSONDecodeError) as exc:
        sys.exit("PROJECTE GATE PREFLIGHT FAILED: " + str(exc))
