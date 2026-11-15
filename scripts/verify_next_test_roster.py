#!/usr/bin/env python3
"""Fail-closed static gate for the NEXT QA download. Not an in-world gameplay test."""
from __future__ import annotations
import json
import tomllib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
REQUIRED = {
    "dragon-survival": ("both", "9539454"),
    "comforts": ("both", "3kpPjcTc"),
    "colorful-hearts": ("client", "us0gze50"),
    "vampires-delight": ("both", "oe9B4zXP"),
    "tyzs-skills": ("both", "OUmvFAPs")
}

def run(root: Path = ROOT) -> None:
    mods = {x["id"]: x for x in json.loads((root / "manifest/mods.json").read_text())["mods"]}
    index = tomllib.loads((root / "index.toml").read_text())
    indexed = {x["file"] for x in index["files"]}
    for mid, (side, file_id) in REQUIRED.items():
        name = f"mods/{mid}.pw.toml"
        assert name in indexed, f"Missing indexed mod {mid}"
        assert mods[mid]["enabled_in_pack"] is True, f"{mid} manifest not active"
        entry = tomllib.loads((root / name).read_text())
        assert entry["side"] == side, f"{mid} wrong side"
        downloaded = entry.get("download", {})
        assert downloaded["hash-format"] in {"sha1", "sha512"}, f"{mid} no valid checksum"
        assert len(downloaded["hash"]) in {40, 128}, f"{mid} truncated checksum"
        update = entry.get("update", {})
        ids = update.get("curseforge", {}) | update.get("modrinth", {})
        assert (str(ids.get("file-id")) == file_id or ids.get("version") == file_id), f"{mid} wrong exact release"
    vd = mods["vampires-delight"]
    assert vd["release_status"] == "experimental_release", "Vampire's Delight beta must remain experimental"
    assert vd["version"] == "0.1.12b", "Only owner-approved beta is allowed"
    assert mods["vampirism"]["enabled_in_pack"] is True
    assert mods["farmers-delight"]["enabled_in_pack"] is True
    assert "mods/puffish-skills.pw.toml" not in indexed
    assert mods["puffish-skills"]["enabled_in_pack"] is False
    assert mods["puffish-attributes"]["enabled_in_pack"] is True
    assert mods["geckolib"]["enabled_in_pack"] is True
    assert mods["improvable-skills"]["enabled_in_pack"] is True
    assert "config/tyzs_skills/custom/skills/tmm_magic_potency_1.json" in indexed
    assert "kubejs/server_scripts/dragon_diets.js" in indexed
    assert "kubejs/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json" in indexed
    vamp = root / "defaultconfigs/vampirism-server.toml"
    assert vamp.exists() and "fangInfection = false" in vamp.read_text(), "Missing fang infection override"
    assert "defaultconfigs/vampirism-server.toml" in indexed, "Vampire defaultconfig missing from packwiz"
    for p in [
        "kubejs/data/tmm/questshop/shop_categories/ores.json",
        "kubejs/data/tmm/questshop/shop_entries/ores.json",
        "config/ftbquests/quests/chapters/skills_advancement.snbt"
    ]:
        assert p in indexed, f"Quest/ore source missing from pack {p}"
    print("PASS: dragon+diet, Comforts, client Colorful Hearts, Vampire\'s Delight 0.1.12b beta,")
    print("      Monk/primary Tyz prerequisites and updated quest/ore sources present.")
    print("NOT PROVEN: client graphics, dragon foods/forms, vampirism cures, native Monk rank, MXP, claim semantics.")

if __name__ == "__main__":
    run()
