#!/usr/bin/env python3
"""Fail closed if the next QA restores a retired TeamLapen dependency."""
import json, tomllib
from pathlib import Path
ROOT = Path(__file__).resolve().parents[1]
REQUIRED = {
    "dragon-survival": ("both", "9539454"),
    "comforts": ("both", "3kpPjcTc"),
    "colorful-hearts": ("client", "us0gze50"),
    "tyzs-skills": ("both", "OUmvFAPs"),
}
def run(root=ROOT):
    mods = {m["id"]:m for m in json.loads((root/"manifest/mods.json").read_text())["mods"]}
    index = tomllib.loads((root/"index.toml").read_text())
    paths = {x["file"] for x in index["files"]}
    for mid,(side,version) in REQUIRED.items():
        name=f"mods/{mid}.pw.toml"
        assert name in paths and mods[mid]["enabled_in_pack"], mid
        entry=tomllib.loads((root/name).read_text())
        assert entry["side"]==side,mid
        d=entry.get("update",{}).get("curseforge",{}) | entry.get("update",{}).get("modrinth",{})
        assert str(d.get("file-id"))==version or d.get("version")==version,mid
    assert "mods/puffish-skills.pw.toml" not in paths
    assert mods["puffish-skills"]["enabled_in_pack"] is False
    assert mods["puffish-attributes"]["enabled_in_pack"] is True
    assert mods["improvable-skills"]["enabled_in_pack"] is True
    assert mods["geckolib"]["enabled_in_pack"] is True
    assert "config/tyzs_skills/custom/skills/tmm_magic_potency_1.json" in paths
    for mid in ("vampirism", "werewolves", "vampires-delight"):
        assert mid not in mods and f"mods/{mid}.pw.toml" not in paths, mid
    assert "defaultconfigs/vampirism-server.toml" not in paths
    assert "defaultconfigs/tmm_transformations-server.toml" not in paths
    assert "config/tmm_transformations-server.toml" not in paths
    assert "kubejs/server_scripts/dragon_diets.js" in paths
    assert "kubejs/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json" in paths
    print("PASS next-QA: new roster, Tyz and standalone class; TeamLapen absent")
if __name__=="__main__": run()
