#!/usr/bin/env python3
"""Offline, read-only Weeping Angels native-teleport policy check (#387 / PR #392).

With no census: verify authored native config, report that strict dimension QA is BLOCKED.
With a witnessed complete loaded-level census: fail if *any* actual loaded level
is not covered by the owner-approved six destinations or the native blacklist.
This never launches Minecraft, patches the native selector or produces a QA PASS.
"""
from __future__ import annotations

import argparse
import json
import re
import sys
import tomllib
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ALLOWED = frozenset({
    "minecraft:overworld",
    "aether:the_aether",
    "the_bumblezone:the_bumblezone",
    "twilightforest:twilight_forest",
    "theaurorian:the_aurorian",
    "eternal_starlight:starlight",
})
# These are source-identified exclusions only, NOT the complete registry.
CONFIRMED_EXCLUSIONS = frozenset({
    "minecraft:the_nether",
    "minecraft:the_end",
    "deeperdarker:otherside",
    "wardentools:abyssdim",
    "voidscape:void",
    "pastel:deeper_down",
    "neovitae:dungeon",
})
IDENTIFIER = re.compile(r"^[a-z0-9_.-]+:[a-z0-9_./-]+$")
DIMENSION_RESOURCE = re.compile(r"^data/([a-z0-9_.-]+)/dimension/([a-z0-9_./-]+)\.json$")
SHA = re.compile(r"^[a-f0-9]{40}$")


class AuditError(ValueError):
    pass


def demand(value: bool, message: str) -> None:
    if not value:
        raise AuditError(message)


def ids(value: object, where: str) -> set[str]:
    demand(isinstance(value, list), f"{where}: must be a JSON/TOML list")
    result: set[str] = set()
    for entry in value:
        demand(isinstance(entry, str) and IDENTIFIER.fullmatch(entry) is not None,
               f"{where}: invalid dimension ID {entry!r}")
        demand(entry not in result, f"{where}: duplicate dimension ID {entry}")
        result.add(entry)
    return result


def source_check(root: Path = ROOT) -> set[str]:
    common = tomllib.loads((root / "config/weeping_angels-common.toml").read_text(encoding="utf-8"))
    spawns = tomllib.loads((root / "config/weeping-angels-spawns.toml").read_text(encoding="utf-8"))
    modifier = json.loads((root / "kubejs/data/weeping_angels/neoforge/biome_modifier/angel_spawns.json")
                          .read_text(encoding="utf-8"))
    behaviour, teleport = common["behaviour"], common["teleporting"]
    demand(behaviour.get("angel_theft") is False, "Permanent Angel item theft disable was changed")
    demand(behaviour.get("block_breaking") is True,
           "Approved unclaimed-block interaction QA candidate was changed")
    demand(teleport.get("interdimensional_teleporting") is True,
           "Native cross-dimensional teleportation was disabled")
    demand(type(teleport.get("teleport_range")) is int and teleport["teleport_range"] == 400,
           "Approved native displacement is not 400")
    demand(type(teleport.get("teleport_chance")) is int and teleport["teleport_chance"] == 50,
           "Approved native teleport chance is not 50")
    banned = ids(teleport.get("banned_dimensions"), "[teleporting].banned_dimensions")
    demand(not (ALLOWED & banned), "One or more approved destinations are blacklisted: "
           + ", ".join(sorted(ALLOWED & banned)))
    demand(CONFIRMED_EXCLUSIONS <= banned, "Source-confirmed excluded dimensions missing: "
           + ", ".join(sorted(CONFIRMED_EXCLUSIONS - banned)))
    demand(spawns.get("snow_angels", {}).get("snow_angels") is False,
           "Random Snow Angel feature must remain disabled")
    demand(modifier == {"type": "neoforge:none"},
           "Biome modifier must replace the native angel_spawns entry with neoforge:none")
    return banned


def read_census(path: Path) -> tuple[set[str], str]:
    """Require evidence metadata: only a real loaded-level registry can authorize acceptance."""
    document = json.loads(path.read_text(encoding="utf-8"))
    demand(isinstance(document, dict) and document.get("schema") == 1,
           "Census must be a schema=1 JSON object")
    demand(document.get("source") == "runtime_loaded_level_registry",
           "Census source must be runtime_loaded_level_registry; static JAR scans are advisory")
    sha = document.get("integration_commit")
    demand(isinstance(sha, str) and SHA.fullmatch(sha) is not None,
           "Census must identify the exact 40-character integration commit")
    loaded = ids(document.get("dimensions"), "runtime dimensions")
    demand(bool(loaded), "Runtime census must not be empty")
    return loaded, sha


def check_loaded(loaded: set[str], banned: set[str]) -> None:
    missing_destinations = ALLOWED - loaded
    escaped = loaded - (ALLOWED | banned)
    demand(not missing_destinations, "Approved destination missing from loaded level registry: "
           + ", ".join(sorted(missing_destinations)))
    demand(not escaped, "UNSAFE: actual loaded dimension(s) eligible for Angel teleport: "
           + ", ".join(sorted(escaped)))
    # Extra blacklist entries may refer to dimensions absent in this snapshot.
    # This is safe: native blacklist does not imply the dimension must exist.


def scan_jars(folder: Path) -> set[str]:
    """Advisory: datapack JSONs do not establish which dynamic dimensions load."""
    demand(folder.is_dir(), f"JAR directory not found: {folder}")
    found: set[str] = set()
    jars = list(folder.glob("*.jar"))
    demand(bool(jars), f"No JARs found in {folder}")
    for jar in jars:
        with zipfile.ZipFile(jar) as zf:
            for name in zf.namelist():
                match = DIMENSION_RESOURCE.fullmatch(name)
                if match:
                    found.add(f"{match[1]}:{match[2]}")
    return found


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--census", type=Path, help="Complete loaded-level registry JSON for exact integration SHA")
    parser.add_argument("--require-census", action="store_true",
                        help="Fail closed if no runtime registry evidence was supplied")
    parser.add_argument("--jar-dir", type=Path, help="Optional read-only JAR dimension JSON survey (advisory)")
    parser.add_argument("--expect-integration-commit",
                        help="Exact frozen build SHA required for the supplied census; reject mismatched snapshots")
    options = parser.parse_args()
    try:
        banned = source_check()
        print(f"SOURCE PASS: native settings, no random spawns/theft, {len(banned)} excluded IDs")
        if options.jar_dir:
            discovered = scan_jars(options.jar_dir)
            print("JAR ADVISORY: data/*/dimension/*.json IDs: " + ", ".join(sorted(discovered)))
            print("JAR ADVISORY: dynamic or datapack dimensions may be absent; never substitute this for runtime census")
            unknown = discovered - (ALLOWED | banned)
            if unknown:
                print("JAR WARNING: candidate worlds outside native blacklist: " + ", ".join(sorted(unknown)))
        if not options.census:
            print("CENSUS BLOCKED: no loaded registry supplied; JAWR Spirit World and other dynamic levels unverified")
            return 2 if options.require_census else 0
        loaded, sha = read_census(options.census)
        if options.expect_integration_commit:
            demand(SHA.fullmatch(options.expect_integration_commit) is not None,
                   "Expected integration commit must be a full 40-character SHA")
            demand(sha == options.expect_integration_commit,
                   f"Census is for build {sha}, not expected {options.expect_integration_commit}")
        check_loaded(loaded, banned)
        print(f"POLICY PASS for supplied loaded-registry census at {sha}: "
              f"{len(ALLOWED)} permitted / {len(loaded) - len(ALLOWED)} excluded loaded dimensions")
        print("NOT GAMEPLAY QA: witness/validate census provenance, exact mod binary, damage, claims and traps separately")
        return 0
    except (AuditError, OSError, ValueError, KeyError, json.JSONDecodeError,
            tomllib.TOMLDecodeError, zipfile.BadZipFile) as exc:
        print(f"FAIL: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    sys.exit(main())
