#!/usr/bin/env python3
"""Deterministic OFF-by-default Tyz 6.4.2 preview for JAWR form skills.

This produces real Tyz 6.4.2 IMMUTABLE JSON *overrides*, but those are only
valid at runtime when an addon registers matching default SkillPrefabs.
The generator deliberately cannot set "active": true or modify packwiz.
"""
from __future__ import annotations
import argparse
import json
import re
from collections import Counter
from pathlib import Path

HERE = Path(__file__).resolve().parent
CATALOG = HERE / "catalog.json"
SKILL_ID = re.compile(r"^tmm_(vampire|werewolf)_[a-z0-9_]+$")
ALLOWED_GATES = {
    "jawr_form_present", "jawr_native_mid_milestone_verified",
    "jawr_native_late_milestone_verified",
    "jawr_native_end_milestone_and_late_boss"
}
ICON = {
    "tmm_vampire": "minecraft:textures/item/fermented_spider_eye.png",
    "tmm_werewolf": "minecraft:textures/item/bone.png",
}

def load() -> dict:
    obj = json.loads(CATALOG.read_text(encoding="utf-8"))
    assert obj["schemaVersion"] == 1
    skills = obj["skills"]
    assert obj["skillCount"] == len(skills)
    ids = [s["id"] for s in skills]
    assert len(ids) == len(set(ids))
    assert all(SKILL_ID.fullmatch(i) for i in ids)
    assert set(s["category"] for s in skills) == set(ICON)
    known = set(ids)
    groups = Counter(s["category"] for s in skills)
    assert groups["tmm_vampire"] == 16 and groups["tmm_werewolf"] == 21, groups
    for s in skills:
        assert s["category"] == f"tmm_{s['form']}"
        assert s["maximumLevel"] == len(s["prices"]) == len(s["values"])
        assert 1 <= s["maximumLevel"] <= 99
        assert all(type(n) is int and n > 0 for n in s["prices"])
        assert all(type(n) in (int, float) and n >= 0 for n in s["values"])
        assert s["externalGate"] in ALLOWED_GATES
        assert s["active"] is False, f"unsafe live flag: {s['id']}"
        for pre in s["skillPrerequisites"] + s.get("additionalServerPrerequisites", []):
            assert pre in known and pre != s["id"], (s["id"], pre)
        for excluded in s["incompatibleSkills"]:
            assert excluded in known and excluded != s["id"]
    # Prevent cycles and valid but unreachable prerequisites.
    graph = {s["id"]: s["skillPrerequisites"] + s.get("additionalServerPrerequisites", []) for s in skills}
    state = {}
    def visit(k):
        assert state.get(k) != 1, f"cyclic prerequisite: {k}"
        if state.get(k) == 2:
            return
        state[k] = 1
        for p in graph[k]:
            visit(p)
        state[k] = 2
    for name in graph:
        visit(name)
    return obj

def render_skill(s: dict) -> dict:
    # IMMUTABLE JSON must override an addon-registered default prefab.
    # A loose custom IMMUTABLE file is NOT loaded by Tyz 6.4.2 (unlike CUSTOM).
    return {
        "active": False,
        "id": s["id"],
        "maximumLevel": s["maximumLevel"],
        "prices": s["prices"],
        "type": "IMMUTABLE",
        "category": s["category"],
        "icon": ICON[s["category"]],
        "displayName": s["displayName"],
        "description": s["description"],
        "modifiers": [],
        "customValues": {
            "magnitude": {"values": [float(x) for x in s["values"]], "unit": s["unit"]}
        },
        "config": {
            "visible": True,
            "purchasable": True,
            "refundable": True,
            "skillPrerequisites": s["skillPrerequisites"],
            "incompatibleSkills": s["incompatibleSkills"]
        }
    }

def categories(base: dict) -> dict:
    mandatory = {"abilities", "combat", "misc", "nature", "tmm_magic_core"}
    missing = mandatory - set(base)
    if missing:
        raise ValueError(f"base Tyz categories missing: {sorted(missing)}")
    out = dict(base)
    # The final nine custom categories live in a separate design branch.
    # Always MERGE instead of overwriting other people's category edits.
    for key, name, icon, order in (
        ("tmm_vampire", "Vampire", ICON["tmm_vampire"], 7),
        ("tmm_werewolf", "Werewolf", ICON["tmm_werewolf"], 8),
    ):
        previous = out.get(key)
        value = {"displayName": name, "icon": icon, "order": order}
        if previous and previous != value:
            raise ValueError(f"category collision for {key}; manual reconciliation required")
        out[key] = value
    return out

def build(out: Path, base_categories: Path) -> int:
    skills = load()["skills"]
    cats = categories(json.loads(base_categories.read_text(encoding="utf-8")))
    skill_dir = out / "config/tyzs_skills/custom/skills"
    data_dir = out / "config/tyzs_skills/custom/data"
    skill_dir.mkdir(parents=True, exist_ok=True)
    data_dir.mkdir(parents=True, exist_ok=True)
    (data_dir / "categories.json").write_text(json.dumps(cats, indent=2) + "\n", encoding="utf-8")
    for spec in skills:
        path = skill_dir / (spec["id"] + ".json")
        if path.exists():
            raise FileExistsError(f"will not overwrite an existing skill: {path}")
        path.write_text(json.dumps(render_skill(spec), indent=2) + "\n", encoding="utf-8")
    return len(skills)

if __name__ == "__main__":
    ap = argparse.ArgumentParser()
    ap.add_argument("--check", action="store_true")
    ap.add_argument("--out", type=Path)
    ap.add_argument("--base-categories", type=Path)
    args = ap.parse_args()
    load()
    if args.check:
        print("PASS: 37 source definitions, valid prerequisites/tiers, no activated skills")
    if args.out:
        if args.base_categories is None:
            ap.error("--out requires --base-categories; must merge with existing Tyz categories")
        count = build(args.out, args.base_categories)
        print(f"Generated {count} INACTIVE Tyz form skills into {args.out}; NO gameplay enabled")
    elif not args.check:
        ap.error("Specify --check and/or --out")
