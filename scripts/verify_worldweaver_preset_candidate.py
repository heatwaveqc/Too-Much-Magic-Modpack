#!/usr/bin/env python3
"""Verify issue #304's exact pins and uninstalled config candidate.

This is a source-only check. It does not resolve registries, create a world, or
establish that Tectonic and BetterEnd generate together at runtime.
"""

from pathlib import Path
import json
import tomllib


ROOT = Path(__file__).resolve().parents[1]
CANDIDATE = ROOT / "docs" / "qa" / "candidates" / "worldweaver-304"

EXPECTED_PINS = {
    "worldweaver-new-dawn.pw.toml": (
        "worldweaver-21.0.25.jar",
        "23926e6f737f9d73797ea7e5dfac2e6611a6cecdb768f236465c05e5750108100b6dbeb911e5f55b987f81a30f786e30a5910904dec6751e5c56bc8bf78eb2a7",
    ),
    "betterend-new-dawn.pw.toml": (
        "BetterEnd-21.0.39.jar",
        "41cbcc3e104269ac9330747018a4e6a130647d4df598d59307f33520764f22146dfafdfcd3971cefb344d610e71f832f345c01370658374a553568af53a32257",
    ),
    "tectonic.pw.toml": (
        "tectonic-3.0.28-neoforge-21.1.jar",
        "e07b9cb54f078b7bf74c925c5cebd6643f0b4c210b8f94474e68659b69468ece7d61432f70eafe7abaa1dfa798e4638aad297987c121dc127588c2623b7f7a81",
    ),
    "bclib-new-dawn.pw.toml": (
        "bclib-21.0.26.jar",
        "0724010228a724c77b021676310e96e6d428a5df1f66890ee11b74ea1c2592bdaf9be250bcdbd0eb8bf4a1d4d0c5bddde06fca5ab5d6d700be19d0c86fe67bf2",
    ),
    "wunderlib-new-dawn.pw.toml": (
        "wunderlib-21.0.10.jar",
        "8785b16968ed82ead0fc3f6f42ca3dd7fa94b94a79eeb97a61e57b258bf8c0e6775002a561cf28a1840216bd06f3b86a31da420d4ccc2d37c14abe22058bdf96",
    ),
}


def load_json(path: Path) -> dict:
    return json.loads(path.read_text(encoding="utf-8"))


for pin_name, (filename, sha512) in EXPECTED_PINS.items():
    with (ROOT / "mods" / pin_name).open("rb") as handle:
        pin = tomllib.load(handle)
    assert pin["filename"] == filename
    assert pin["side"] == "both"
    assert pin["download"]["hash-format"] == "sha512"
    assert pin["download"]["hash"] == sha512

client = load_json(CANDIDATE / "client.json")
assert client == {
    "internal": {"did_present_welcome_screen": True},
    "general": {"force_betterx_world_type": True},
}
main = load_json(CANDIDATE / "main.json")
assert main == {"server": {"force_default_world_preset": True}}

runtime_client = load_json(ROOT / "config" / "wover" / "client.json")
assert runtime_client["internal"]["did_present_welcome_screen"] is True
runtime_selected = runtime_client["general"]["force_betterx_world_type"] is True
assert runtime_selected, "The active QA preset must select the source-reviewed BetterX composition"
runtime_main = load_json(ROOT / "config" / "wover" / "main.json")
assert runtime_main == main, "Active server world preset must match reviewed candidate"

with (ROOT / "index.toml").open("rb") as handle:
    index = tomllib.load(handle)
indexed = {row["file"] for row in index["files"]}
assert not any(path.startswith("docs/qa/candidates/worldweaver-304/") for path in indexed)
assert {"config/wover/client.json", "config/wover/main.json"} <= indexed, "Both active WorldWeaver configurations must be in Packwiz"
assert not any(ROOT.rglob("worldweaver-21.0.25.jar")), "third-party JAR must not be committed"

state = "runtime already selects BetterX" if runtime_selected else "candidate awaits integration"
print(
    "PASS: exact WorldWeaver/BetterEnd/Tectonic dependency pins and welcome/preset "
    f"candidate; {state}; SOURCE ONLY, no world-generation acceptance"
)
