#!/usr/bin/env python3
"""B13: pin FTB Essentials 2101.1.10, enable /sethome, /home and /rtp.

Exact CurseForge NeoForge 1.21.1 file: 8442866, project 410811.
Never silently upgrade, add a different provider, or modify the working modpack.
Run only on a disposable QA branch with packwiz CLI installed.
"""
from __future__ import annotations

import json
import pathlib
import re
import subprocess
import tomllib

ROOT = pathlib.Path(__file__).resolve().parents[1]
MANIFEST = ROOT / "manifest/mods.json"
PROJECT_ID = 410811
FILE_ID = 8442866
JAR = "ftb-essentials-neoforge-2101.1.10.jar"
MOD_ID = "ftb-essentials"
EXPECTED = 147

def ensure(ok: bool, label: str) -> None:
    if not ok:
        raise RuntimeError(label)

def cmd(*args: str) -> str:
    completed = subprocess.run(
        args, cwd=ROOT, text=True, capture_output=True, timeout=240,
        check=False
    )
    if completed.returncode:
        raise RuntimeError(f"{args!r} failed: {completed.stdout[-1000:]}\n{completed.stderr[-1500:]}")
    return completed.stdout

def main() -> None:
    manifest = json.loads(MANIFEST.read_text(encoding="utf-8"))
    current = [m for m in manifest["mods"] if m["enabled_in_pack"]]
    ensure(len(current) == EXPECTED, f"Expected {EXPECTED} prior mods; found {len(current)}")
    ensure(MOD_ID not in {m["id"] for m in manifest["mods"]}, "FTB Essentials already catalogued")
    ensure({"ftb-library", "architectury-api"}.issubset({m["id"] for m in current}) or
           {"ftb-library-forge", "architectury-api"}.issubset({m.get("packwiz_slug") for m in current}),
           "FTB Essentials requires existing FTB Library and Architectury")

    for location in ("config/ftbessentials.snbt", "defaultconfigs/ftbessentials-server.snbt"):
        data = (ROOT / location).read_text(encoding="utf-8")
        ensure("home:" in data and "rtp:" in data
               and data.count("enabled: true") == 2,
               f"Missing explicit sethome/rtp enablement: {location}")

    cmd("packwiz", "--yes", "curseforge", "add", "--addon-id", str(PROJECT_ID),
        "--file-id", str(FILE_ID))
    matches = []
    for path in (ROOT/"mods").glob("*.pw.toml"):
        data = tomllib.loads(path.read_text(encoding="utf-8"))
        if data.get("update", {}).get("curseforge", {}).get("project-id") == PROJECT_ID:
            matches.append((path, data))
    ensure(len(matches) == 1, "Official FTB Essentials project must resolve exactly once")
    path, pinned = matches[0]
    ensure(pinned["filename"] == JAR,
           "Unreviewed FTB Essentials binary: " + repr(pinned.get("filename")))
    ensure(pinned["side"] == "both", "FTB Essentials must load in client and server")
    ensure(pinned["update"]["curseforge"]["file-id"] == FILE_ID,
           "Unexpected CurseForge file ID")
    dl = pinned["download"]
    ensure(dl.get("mode") == "metadata:curseforge"
           and dl.get("hash-format") in {"sha1", "sha256", "sha512"}
           and re.fullmatch(r"[a-f0-9]+", dl.get("hash","")) is not None,
           "FTB Essentials must be pinned with official metadata and file integrity hash")
    ensure(len([* (ROOT/"mods").glob("*.pw.toml")]) == EXPECTED+1,
           "Packwiz added unwanted transitive mods")

    manifest["mods"].append({
        "id": MOD_ID,
        "name": "FTB Essentials (NeoForge)",
        "category": "qol",
        "desired": True,
        "version": "2101.1.10",
        "release_status": "verified_release",
        "source": "https://www.curseforge.com/minecraft/mc-mods/ftb-essentials/files/8442866",
        "enabled_in_pack": True,
        "dependencies_verified": True,
        "note": "B13: official FTB Essentials 2101.1.10 NeoForge 1.21.1 CurseForge 410811/8442866. Explicit home and rtp enablement in config and defaultconfigs. One home per player, 10s home cooldown, 600s RTP cooldown, 500-25000 blocks from spawn. Gameplay permissions and teleport behavior need fresh world QA.",
        "packwiz_slug": path.name.removesuffix(".pw.toml")
    })
    manifest["batches"].append({
        "id": "B13",
        "name": "FTB Essentials: player sethome/home and random teleport",
        "members": [MOD_ID],
        "status": "dedicated_server_smoke_pending_client_test_pending"
    })
    manifest["note"] += "\nB13: owner requested /sethome and /rtp available without OP; 2101.1.10 exact official NeoForge file pinned and server/client QA pending."
    MANIFEST.write_text(json.dumps(manifest, indent=2, ensure_ascii=False)+"\n", encoding="utf-8")
    pack_path = ROOT / "pack.toml"
    pack = pack_path.read_text(encoding="utf-8")
    pack, count = re.subn(r'(?m)^version = "[^"]+"$',
                          'version = "0.8.11-ftb-essentials-home-rtp-qa"', pack, count=1)
    ensure(count == 1, "Missing packwiz version field")
    pack_path.write_text(pack, encoding="utf-8")

    cmd("packwiz", "refresh")
    print(cmd("python3", "scripts/validate.py"))
    print(cmd("python3", "scripts/validate_quests.py"))
    print("PASS: FTBE 2101.1.10 exact CurseForge 410811/8442866, one added JAR; 148 pins")
    print("PASS: /sethome, /home, /rtp explicitly enabled in both client/server defaults")

if __name__ == "__main__":
    main()
