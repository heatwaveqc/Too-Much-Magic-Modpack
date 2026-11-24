#!/usr/bin/env python3
"""Verify the frozen inputs for issue #330's direct-flight ownership audit.

This is deliberately a static check.  It does not download mods, assemble the
pack, or start Minecraft.
"""

from __future__ import annotations

import json
from pathlib import Path
import tomllib


ROOT = Path(__file__).resolve().parents[1]
AUDIT = ROOT / "manifest" / "flight-provider-direct-ownership.json"
ARTIFACTS = ROOT / "docs" / "qa" / "surveys-2026-11-18" / "installed-artifacts.json"


def load_toml(path: Path) -> dict:
    with path.open("rb") as handle:
        return tomllib.load(handle)


def main() -> None:
    audit = json.loads(AUDIT.read_text(encoding="utf-8"))
    assert audit["schema"] == 1
    assert audit["issue"] == 330
    assert len(audit["baseline_commit"]) == 40

    artifacts = {
        row["file"]: row["sha256"]
        for row in json.loads(ARTIFACTS.read_text(encoding="utf-8"))
    }
    index = (ROOT / "index.toml").read_text(encoding="utf-8")

    for provider in audit["providers"]:
        pin_path = ROOT / "mods" / f"{provider['id']}.pw.toml"
        pin = load_toml(pin_path)
        assert pin["filename"] == provider["file"], provider["id"]
        assert pin["download"]["hash-format"] == "sha1", provider["id"]
        assert pin["download"]["hash"] == provider["packwiz_sha1"], provider["id"]
        assert artifacts[provider["file"]] == provider["observed_sha256"], provider["id"]
        assert f'mods/{provider["id"]}.pw.toml' in index, provider["id"]
        assert len(provider["source_commit"]) == 40, provider["id"]

    print(
        "PASS: 2 indexed provider pins, Packwiz SHA-1 values, frozen observed "
        "SHA-256 values, and 40-hex source commits match the issue #330 audit"
    )
    print("STATIC ONLY: no JAR bytecode comparison, pack assembly, or Minecraft run")


if __name__ == "__main__":
    main()
