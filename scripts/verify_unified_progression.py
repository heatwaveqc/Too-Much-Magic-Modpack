#!/usr/bin/env python3
"""Fail closed on retired XP runtime selection and duplicate award ownership."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

def verify_selection(root=ROOT, manifest=None):
    manifest = manifest or json.loads((root / "manifest/mods.json").read_text(encoding="utf-8"))
    active = {row["id"] for row in manifest["mods"] if row["enabled_in_pack"]}
    assert not ({"tyzs-skills", "puffish-skills"} & active)
    assert "puffish-attributes" in active
    target = json.loads((root / "manifest/next-qa-target.json").read_text(encoding="utf-8"))
    selected = json.loads((root / "manifest/first-party-runtime.json").read_text(encoding="utf-8"))
    ids = {row["mod_id"] for row in selected["modules"]}
    assert ids == set(target["required_first_party_mod_ids"])
    assert "tmm_progression" in ids and "tmm_mxp" not in ids
    for path in ("mods/tyzs-skills.pw.toml", "mods/puffish-skills.pw.toml",
                 "mods/tmm-mxp.pw.toml", "config/tyzs_skills", "config/tyzs_skills-common.toml",
                 "config/tyzs_skills-client.toml", "kubejs/server_scripts/tmm_magic_core_thrift.js"):
        assert not (root / path).exists(), "Retired XP runtime residue: " + path
    base = root / "compat/progression/src/main"
    metadata = (base / "templates/META-INF/neoforge.mods.toml").read_text(encoding="utf-8")
    assert 'config="tmm_progression_mxp.mixins.json"' in metadata
    owner = base / "java/io/github/heatwaveqc/tmm/progression"
    api = (owner / "core/ProgressionApi.java").read_text(encoding="utf-8")
    gate = api.split("private static boolean awardMagic(", 1)[1].split("public static boolean awardExperience", 1)[0]
    assert 'NewbieMode.isNewbie(player)' in gate and 'isLoaded("tmm_mxp")' in gate
    assert "new io.github.heatwaveqc.tmm.progression.mxp.MxpMod" in (owner / "ProgressionMod.java").read_text(encoding="utf-8")
    assert 'isLoaded("tmm_mxp")' in (owner / "mxp/MxpMod.java").read_text(encoding="utf-8")
    mixins = set(json.loads((base / "resources/tmm_progression_mxp.mixins.json").read_text(encoding="utf-8"))["mixins"])
    assert {"MxpArsResolverMixin", "MxpIronsCastMixin", "ForgeRitualManagerMixin",
            "MalumTotemRitualMixin", "MalumGeasRitualMixin", "AstralPlayerAttunementMixin"} <= mixins
    print("PASS: Unified Progression MXP selected; Tyz and standalone MXP absent from runtime roster. Native gameplay untested.")

if __name__ == "__main__":
    verify_selection()
