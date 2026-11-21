#!/usr/bin/env python3
"""Static verification for issue #404's two inactive pin candidates.

This does not download mods, assemble the pack, or start Minecraft.
"""

from pathlib import Path
import re
import tomllib


ROOT = Path(__file__).resolve().parents[1]

EXPECTED = {
    "aquamirae-nouveau.pw.toml": {
        "name": "Aquamirae & Nouveau",
        "filename": "aquamirae-nouveau-0.1.3-mc1.21.1-neoforge.jar",
        "sha1": "932c37b798a04d13ca231d141d86590153e99fa3",
        "project": 1767657,
        "file": 9470029,
    },
    "ars-conflux.pw.toml": {
        "name": "Ars Conflux",
        "filename": "ars-conflux-0.1.56-mc1.21.1-neoforge.jar",
        "sha1": "97f3c31c624df19209db648e4fe6dd2e67e08d80",
        "project": 1737308,
        "file": 9505585,
    },
}


def load_toml(path: Path) -> dict:
    with path.open("rb") as handle:
        return tomllib.load(handle)


for pin_name, expected in EXPECTED.items():
    pin = load_toml(ROOT / "mods" / pin_name)
    assert pin["name"] == expected["name"]
    assert pin["filename"] == expected["filename"]
    assert pin["side"] == "both"
    assert pin["download"] == {
        "hash-format": "sha1",
        "hash": expected["sha1"],
        "mode": "metadata:curseforge",
    }
    assert pin["update"]["curseforge"] == {
        "file-id": expected["file"],
        "project-id": expected["project"],
    }
    assert not (ROOT / expected["filename"]).exists(), "third-party JAR must not be committed"

index = load_toml(ROOT / "index.toml")
indexed = {entry["file"] for entry in index["files"]}
for pin_name in EXPECTED:
    assert f"mods/{pin_name}" not in indexed, (
        "candidate must stay inactive until the integration writer reconciles shared files"
    )

pack = load_toml(ROOT / "pack.toml")
assert pack["versions"]["minecraft"] == "1.21.1"
neoforge = tuple(map(int, pack["versions"]["neoforge"].split(".")))
assert (21, 1, 250) <= neoforge < (21, 2), "both candidates require NeoForge [21.1.250,21.2)"

ars = load_toml(ROOT / "mods" / "ars-nouveau.pw.toml")
match = re.fullmatch(r"ars_nouveau-1\.21\.1-(\d+)\.(\d+)\.(\d+)\.jar", ars["filename"])
assert match, "cannot establish the installed Ars Nouveau version"
ars_version = tuple(map(int, match.groups()))
assert (5, 13, 1) <= ars_version < (6, 0, 0), "candidates require Ars Nouveau [5.13.1,6)"

aquamirae_pin = ROOT / "mods" / "aquamirae.pw.toml"
if aquamirae_pin.exists():
    aquamirae = load_toml(aquamirae_pin)
    assert aquamirae["filename"] == "aquamirae-neoforge-1.21.1-7.2.13.jar"
    dependency_state = "Aquamirae 7.2.13 candidate present"
else:
    dependency_state = "Aquamirae 7.2.13 remains on draft #399"

print(
    "PASS: exact Aquamirae & Nouveau 0.1.3 and Ars Conflux 0.1.56 candidates; "
    f"Ars Nouveau {'.'.join(match.groups())}, NeoForge {pack['versions']['neoforge']}; "
    f"{dependency_state}; candidates remain unindexed"
)
