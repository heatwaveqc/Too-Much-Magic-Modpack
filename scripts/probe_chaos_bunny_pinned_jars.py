#!/usr/bin/env python3
"""Read-only SHA-512-verified *official* Gateways / Apotheosis 1.21.1 release JAR probe.

Runs in a disposable GitHub Actions working dir. It NEVER adds, uploads,
rehosts, or packages third-party bytes. No Minecraft launch or full build.
"""
from __future__ import annotations
import hashlib
import json
from pathlib import Path
import sys
import tomllib
import zipfile

ROOT = Path(__file__).resolve().parents[1]
NAMES = ("gateways-to-eternity", "apotheosis")

def require(ok: bool, problem: str) -> None:
    if not ok:
        raise ValueError(problem)

def read_pin(slug: str) -> dict:
    p = tomllib.loads((ROOT / "mods" / f"{slug}.pw.toml").read_text())
    require(p["download"]["hash-format"] == "sha512", slug + ": unverified pin")
    require(p["download"]["url"].startswith("https://cdn.modrinth.com/"), "wrong publisher host")
    return p

def check(slug: str, path: Path) -> tuple[list[str], dict]:
    pin = read_pin(slug)
    require(path.is_file(), f"missing publisher download for {slug}: {path}")
    require(hashlib.sha512(path.read_bytes()).hexdigest() == pin["download"]["hash"],
            f"{slug} publisher sha512 mismatch")
    with zipfile.ZipFile(path) as z:
        require(z.testzip() is None, slug + ": JAR CRC failure")
        files = z.namelist()
        meta = [x for x in files if x.endswith("META-INF/neoforge.mods.toml")]
        require(meta, slug + ": not a native NeoForge mod")
        embedded = z.read(meta[0]).decode()
        report = {"slug": slug, "publisher_file": pin["filename"],
                  "bytes": path.stat().st_size, "sha512": pin["download"]["hash"],
                  "classes": len([x for x in files if x.endswith(".class")]),
                  "neo_metadata_present": True}
        return files, report

def main(gateways_jar: str, apotheosis_jar: str) -> None:
    gateways = Path(gateways_jar)
    apotheosis = Path(apotheosis_jar)
    gfiles, g = check(NAMES[0], gateways)
    afiles, a = check(NAMES[1], apotheosis)

    needed_classes = (
        "dev/shadowsoffire/apotheosis/compat/gateways/EliteWaveEntity.class",
        "dev/shadowsoffire/apotheosis/compat/gateways/AffixWaveModifier.class",
        "dev/shadowsoffire/apotheosis/mobs/types/Elite.class",
        "dev/shadowsoffire/apotheosis/mobs/util/BossStats.class",
        "dev/shadowsoffire/apotheosis/mobs/util/AffixData.class",
    )
    require(all(s in afiles for s in needed_classes),
            "Exact pinned Apotheosis jar lacks expected Gateway elite/affix bridge")
    needed_g = (
        "dev/shadowsoffire/gateways/gate/StandardWaveEntity.class",
        "dev/shadowsoffire/gateways/gate/WaveModifier.class",
    )
    require(all(s in gfiles for s in needed_g),
            "Exact pinned Gateways jar lacks expected vanilla NBT/attribute entrypoint")
    with zipfile.ZipFile(apotheosis) as z:
        matches = [x for x in afiles if x.startswith("data/apotheosis/gateways/") and x.endswith(".json")]
        require(matches, "No built-in Apotheosis Gateways data found")
        discovered = []
        bunny = []
        for path in matches:
            try:
                data = json.loads(z.read(path))
            except Exception:
                continue
            raw = json.dumps(data, sort_keys=True)
            if "minecraft:rabbit" in raw and "RabbitType" in raw:
                bunny.append(path)
            if "apotheosis:elite" in raw or "apotheosis:affix" in raw:
                discovered.append(path)
        require(discovered, "No actual Apotheosis/Vanilla Gateway affixes/elites found")
        require(bunny, "No exact pinned source of Apotheosis Killer Bunny Gateway")
        a["gateway_data_files"] = matches
        a["actual_affix_or_elite_gateways"] = discovered
        a["exact_killer_bunny_gateway_files"] = bunny
        a["elite_wrapper_class"] = True
        a["actual_gear_affix_class"] = True
    with zipfile.ZipFile(gateways) as z:
        g["example_gateways"] = [x for x in gfiles if x.startswith("data/gateways/gateways/") and x.endswith(".json")][:30]
    print(json.dumps({"status":"EXACT_PUBLISHER_JAR_SHA512_AND_ZIP_PASS_NO_GAME_BOOT",
                      "gateways":g, "apotheosis":a}, indent=2))
    print("NOTE: bytecode class presence and JSON source confirm available codec paths, not in-game initialization or trait balance.")

if __name__ == "__main__":
    if len(sys.argv)!=3:
        sys.exit("Usage: probe_chaos_bunny_pinned_jars.py EXACT_GATEWAYS.jar EXACT_APOTHEOSIS.jar")
    main(*sys.argv[1:])
