#!/usr/bin/env python3
"""Build a pinned-source, mod-jar-free Prism client QA import for TMM."""
from __future__ import annotations

import hashlib
import json
import pathlib
import re
import sys
import zipfile

REPO = "heatwaveqc/Too-Much-Magic-Modpack"
BOOT = "packwiz-installer-bootstrap.jar"
OUT = "Too-Much-Magic-More-Overlays-Auto-Prism.zip"


def main(bootstrap: str, target_dir: str, commit: str) -> None:
    if not re.fullmatch(r"[0-9a-f]{40}", commit):
        raise ValueError("Import source must be an immutable 40-digit commit SHA")
    uri = f"https://raw.githubusercontent.com/{REPO}/{commit}/pack.toml"
    jar = pathlib.Path(bootstrap).read_bytes()
    if len(jar) != 98989 or not jar.startswith(b"PK"):
        raise RuntimeError("Unexpected upstream packwiz installer bootstrap")
    out = pathlib.Path(target_dir) / OUT
    out.parent.mkdir(parents=True, exist_ok=True)
    cfg = f"""[General]
InstanceType=OneSix
name=Too Much Magic - Quest UI and More Overlays Auto Test
OverrideCommands=true
PreLaunchCommand="$INST_JAVA" -jar {BOOT} -s client {uri}
OverrideMemory=true
MinMemAlloc=2048
MaxMemAlloc=8192
"""
    mmc = {
        "formatVersion": 1,
        "components": [
            {"uid": "net.minecraft", "version": "1.21.1", "important": True},
            {"uid": "net.neoforged", "version": "21.1.265"},
        ],
    }
    guide = f"""TOO MUCH MAGIC — QUEST UI FIX LOCAL TEST
================================================
Current pack candidate: 140 hash-pinned mods (including More Overlays), 45 FTB chapters, 179 quests,
Quest Shop, Cooking for Blockheads, and full nuclear/Draconic power guide.

IMPORT INTO PRISM:
1. In Prism Launcher select Add Instance > Import and choose THIS ZIP.
   If GitHub provided an outer artifact ZIP, extract it first.
2. Select Java 21. Memory is set to 8 GiB maximum, adjust to your system.
3. Launch. The bootstrap downloads the exact pinned packwiz files and mod
   releases from their original hosting providers. No mod JARs are included.
4. More Overlays should install directly from its identical Modrinth-pinned
   release, without any manual CurseForge download. Some OTHER mods may
   still ask for manual provider downloads.
5. Create a NEW disposable test world. NEVER open a valued survival save.
6. Open FTB Quests. Confirm five top-level groups named Introduction,
   Progression, Magic, Exploration & Combat, Endgame. Verify all chapter,
   quest and group names, including 65 formerly undefined descriptions.
7. Check Getting Started now continues BEYOND Iron Pickaxe into practical
   magic, optional protection, exploration and chapter choice. Check that
   quest icons are spaced in clear branches and no icons overlap.
8. Read new Tips & Tricks entries: Scriptor Casting Lectern/Inflame free
   smelting (source-backed, chest targeting unverified), Occultism vs Mahou
   ore yield, EvilCraft vs Hexerei brooms and Vengeance Pickaxe.
9. Try JEI, Cooking for Blockheads, Quest Shop GUI and currency purchases,
   but keep gameplay and recipe behavior labeled as not yet tested.
10. Verify the 12 commodity sales only in a disposable world: item input
    is consumed, one team coin credited, repeatable trades are not abused.
11. Read Exceptional Power: Reactors; only test the dangerous Draconic
    Reactor in a throwaway creative world, far from valued infrastructure.
12. Confirm the first-log pouch is issued once per player and not duplicated.
13. Report error text, screenshots, Prism console and logs/latest.log.

MORE OVERLAYS PRESENCE CHECK:
- In Prism: Edit Instance -> Mods -> More Overlays Updated.
- In the instance minecraft/mods directory verify:
  moreoverlays-1.24.2-mc1.21.1-neoforge.jar
- F7 shows light-level overlay; F9 cycles chunk boundaries. Check Controls
  if another mod uses those keys.
- If missing, send the pre-launch packwiz installer log.

NOT FOR BALANCED SURVIVAL:
- ProjectE/Expansion's Chaos Guardian acquisition and usage gates are NOT
  implemented yet.
- Tech ore scarcity, magical Market seed trades, universal bag nesting,
  custom Pufferfish skills and transformation compatibility remain incomplete.
- A successful dedicated server smoke does not certify client GUI, machine
  behavior, item registries, quest rewards or reactor safety.

Immutable packwiz commit:
{commit}
Source pack.toml:
{uri}
Pack tree:
https://github.com/{REPO}/tree/{commit}

This artifact contains only Prism metadata and the upstream MIT-licensed
packwiz installer bootstrap; third-party Minecraft mod JARs are downloaded
by the installer and are NOT redistributed here.
"""
    with zipfile.ZipFile(out, "w", compression=zipfile.ZIP_DEFLATED) as z:
        z.writestr("instance.cfg", cfg)
        z.writestr("mmc-pack.json", json.dumps(mmc, indent=2) + "\n")
        z.writestr("minecraft/" + BOOT, jar)
        z.writestr("INSTALL-AND-TEST.txt", guide)
    with zipfile.ZipFile(out) as z:
        names = set(z.namelist())
        if names != {"instance.cfg", "mmc-pack.json", "minecraft/" + BOOT, "INSTALL-AND-TEST.txt"}:
            raise AssertionError("Unexpected files in Prism import")
        if jar != z.read("minecraft/" + BOOT):
            raise AssertionError("Installer changed in archive")
        if uri.encode() not in z.read("instance.cfg"):
            raise AssertionError("Import not locked to commit")
    print(json.dumps({
        "zip_path": str(out), "size": out.stat().st_size,
        "sha256": hashlib.sha256(out.read_bytes()).hexdigest(),
        "bootstrap_sha256": hashlib.sha256(jar).hexdigest(),
        "third_party_mod_jar_count": 0, "source_commit": commit,
        "source_pack": uri, "minecraft": "1.21.1", "neoforge": "21.1.265",
    }, indent=2))


if __name__ == "__main__":
    if len(sys.argv) != 4:
        sys.exit("Usage: build_quest_ui_prism.py BOOTSTRAP_JAR OUTPUT_DIR SNAPSHOT_SHA")
    main(*sys.argv[1:])
