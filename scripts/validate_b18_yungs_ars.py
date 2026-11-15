#!/usr/bin/env python3
"""Static B18 source contracts; not a loaded-registry or gameplay test."""
from __future__ import annotations
import hashlib
import json
import subprocess
import sys
import tomllib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
newmods = {
    "yungs-better-mineshafts", "yungs-better-dungeons", "yungs-better-desert-temples",
    "yungs-better-jungle-temples", "yungs-better-ocean-monuments", "yungs-better-witch-huts",
    "yungs-bridges", "yungs-extras", "yungs-better-end-island",
}
tags = {
    "stronghold": "#betterstrongholds:better_strongholds",
    "nether_fortress": "#betterfortresses:better_fortresses",
    "desert_temple": "#betterdeserttemples:better_desert_temples",
    "jungle_temple": "#betterjungletemples:better_jungle_temples",
    "monument": "#betteroceanmonuments:better_ocean_monuments",
    "witch_hut": "betterwitchhuts:witch_hut",
}
def read(path):
    return (ROOT / path).read_text(encoding="utf-8")
def main():
    roster = json.loads(read("manifest/mods.json"))
    entries = {m["id"]:m for m in roster["mods"] if m["id"] in newmods}
    assert set(entries) == newmods
    assert all(m["desired"] and not m["enabled_in_pack"] for m in entries.values())
    assert any(b["id"] == "B18" and set(b["members"]) == newmods for b in roster["batches"])
    pol = json.loads(read("manifest/ars_structure_locator_policy.json"))
    assert "minecraft:stronghold" in pol["disabled_structures"]
    assert len(pol["address_palette_items"]) == 16 and pol["address_count"] == 4
    index = read("index.toml")
    pack = tomllib.loads(read("pack.toml"))
    assert hashlib.sha256(index.encode()).hexdigest() == pack["index"]["hash"]
    files = ["kubejs/server_scripts/tmm_structure_registry_export.js",
             "manifest/ars_structure_locator_policy.json",
             "kubejs/data/tmm/recipe/locate_structure/mineshaft.json"]
    for name, target in tags.items():
        path = f"kubejs/data/ars_additions/tags/worldgen/structure/{name}.json"
        files.append(path)
        data = json.loads(read(path))
        assert data == {"replace": True, "values": [{"id": target, "required": False}]}
        assert "minecraft:" not in str(data)
    m = json.loads(read("kubejs/data/tmm/recipe/locate_structure/mineshaft.json"))
    assert m["structure"] == {"tag": "bettermineshafts:better_mineshafts"}
    exporter = read(files[0])
    assert "ServerEvents.basicCommand" in exporter and "minecraft:worldgen/structure" in exporter
    for path in files:
        digest = hashlib.sha256(read(path).encode()).hexdigest()
        assert f'file = "{path}"\nhash = "{digest}"' in index, path
    result = subprocess.run([sys.executable,
        str(ROOT / "scripts/generate_ars_all_structures.py"), "--self-test"],
        capture_output=True, text=True)
    assert result.returncode == 0, result.stderr
    print("PASS: B18 planned mods, exact source tags without vanilla, mineshaft fix, generator self-test, packwiz hashes")
    print("PENDING: exact mod pins, runtime structure registry, generated per-structure recipes, client/server and worldgen QA")
if __name__ == "__main__":
    main()
