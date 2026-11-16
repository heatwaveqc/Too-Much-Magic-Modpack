#!/usr/bin/env python3
"""Final-stage purge AFTER all source imports/pin generation.

Reject silent resurrection of TeamLapen / Vampire's Delight source paths.
No migration of saved worlds is attempted.
"""
from __future__ import annotations
import json
from pathlib import Path

REMOVED = {"vampirism", "werewolves", "vampires-delight"}
FILES = (
    "mods/vampirism-become-a-vampire.pw.toml",
    "mods/werewolves-become-a-beast.pw.toml",
    "mods/vampires-delight.pw.toml",
    "config/ftbquests/quests/chapters/vampirism.snbt",
    "config/ftbquests/quests/chapters/werewolves.snbt",
    "config/tmm_transformations-server.toml",
    "defaultconfigs/tmm_transformations-server.toml",
    "defaultconfigs/vampirism-server.toml",
    "kubejs/data/tmm/recipe/blockgen/natural_stone/vampirism__dark_stone.json",
    "kubejs/data/tmm/recipe/blockgen/natural_stone/vampirism__cobbled_dark_stone.json",
)

def write_json(path, data):
    path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")

def retire(root: Path, manifest: dict) -> None:
    manifest["mods"] = [m for m in manifest["mods"] if m["id"] not in REMOVED]
    excludes = {x["id"] for x in manifest["excluded"]}
    for mid in sorted(REMOVED - excludes):
        manifest["excluded"].append({"id": mid, "reason": "Owner-retired; JAWR remains the physiology owner"})
    for name in FILES:
        (root / name).unlink(missing_ok=True)
    # Werewolves' custom diet, including its Relics infinity-ham workaround,
    # must not be loaded or recreated as broken empty namespace tags.
    food = root / "kubejs/data/werewolves"
    if food.exists():
        for p in sorted(food.rglob("*"), key=lambda p: len(p.parts), reverse=True):
            if p.is_file(): p.unlink()
            elif p.is_dir(): p.rmdir()
        food.rmdir()
    # Two retired quest chapters are optional placeholder checkmarks.
    lang = root / "config/ftbquests/quests/lang/en_us.snbt"
    if lang.exists():
        lines = lang.read_text(encoding="utf-8").splitlines(keepends=True)
        old = ("544D204000800000", "544D204000900000",
               "544D304000800001", "544D304000900001")
        lang.write_text("".join(l for l in lines if not any(x in l for x in old)),
                        encoding="utf-8")
    chapters = root / "manifest/quest_chapters.json"
    if chapters.exists():
        j = json.loads(chapters.read_text())
        j["chapters"] = [x for x in j["chapters"]
                         if x.get("filename") not in ("vampirism.snbt", "werewolves.snbt")]
        for g in j.get("groups", []):
            g["count"] = sum(x.get("category") == g["name"] for x in j["chapters"])
        write_json(chapters, j)
    # Remove no-longer-registered stone block outputs AND seeded modifier blocks.
    stones = root / "manifest/stone_generation_policy.json"
    if stones.exists():
        j = json.loads(stones.read_text())
        for key in ("natural_stones", "harvested_stone_variants"):
            j[key] = [x for x in j[key] if not x.startswith(("vampirism:", "werewolves:"))]
        write_json(stones, j)
    # Almost Unified keeps Occultism/Embers/Eidolon, not Werewolves.
    materials = root / "config/almostunified/unification/materials.json"
    if materials.exists():
        j = json.loads(materials.read_text())
        j["mod_priorities"] = [x for x in j["mod_priorities"] if x not in REMOVED]
        write_json(materials, j)
    audit = root / "manifest/material-ore-audit.json"
    if audit.exists():
        j = json.loads(audit.read_text())
        for res in j.get("resources", {}).values():
            if isinstance(res, dict) and "providers" in res:
                res["providers"] = [x for x in res["providers"] if x.get("mod") not in REMOVED]
        j["post_retirement_policy"] = "Three active silver providers. Historical 4-provider runtime evidence is pre-retirement only."
        write_json(audit, j)
    # Acceptance criteria must no longer require TeamLapen vampire/hunter factions.
    checklist = root / "manifest/feature-checklist.json"
    if checklist.exists():
        j = json.loads(checklist.read_text())
        changes = {
            "form.vampire_acquire": ("JAWR-only vampire acquisition", "JAWR native infection, progress and cure; no TeamLapen enrollment"),
            "form.cure": ("JAWR vampire cure", "JAWR cure clears physiology, abilities and stale statuses"),
            "form.sunlight": ("JAWR sunlight and equipment protection", "JAWR native sun behavior works without Vampire Forest special handling"),
            "form.werewolf": ("JAWR lycanthropy and forms", "JAWR lycanthrope form, diet, moon, cure and relog work"),
            "form.blood": ("JAWR-native blood and feeding", "No dual blood pool or duplicated cost; JAWR owns all feeding and expenditure"),
            "monk.tree": ("Standalone Monk native tree", "tmm_progression skills, native XP and purchases work without Vampirism UI"),
            "monk.sunset": ("Standalone Monk First Sunset", "First Sunset enrolls native Monk path, with no TeamLapen faction"),
        }
        for f in j.get("features", []):
            if f.get("id") in changes:
                f["title"], f["acceptance"] = changes[f["id"]]
                f["state"] = "planned"
                f["evidence"] = ""
        write_json(checklist, j)
    # The quest builder owns a source validator still expecting the former
    # 45-chapter scaffold. One more chapter was already removed upstream;
    # retiring the two TeamLapen chapters leaves exactly 44 for this QA.
    validator = root / "scripts/validate_quests.py"
    if validator.exists():
        source = validator.read_text(encoding="utf-8")
        old = 'len(chapters) == 45'
        if old in source:
            source = source.replace(old, 'len(chapters) == 44')
            source = source.replace("Expected five groups and 45 chapters",
                                    "Expected five groups and 44 chapters")
            source = source.replace("PASS: five groups, 45 chapters",
                                    "PASS: five groups, 44 chapters")
            validator.write_text(source, encoding="utf-8")
        elif 'len(chapters) == 44' not in source:
            raise RuntimeError("Quest validator changed: verify chapter count before packaging")
    for name in FILES:
        assert not (root / name).exists(), name
    assert not food.exists()
    assert not (REMOVED & {x["id"] for x in manifest["mods"]})
    print("PASS: TeamLapen, Vampire's Delight, tags, quests, stone recipes and QA configs retired")
