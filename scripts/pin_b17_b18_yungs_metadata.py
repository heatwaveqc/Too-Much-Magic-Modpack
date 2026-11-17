#!/usr/bin/env python3
"""One-shot *metadata-only* 1.21.1 NeoForge resolver for approved B17/B18 mods.

No Minecraft boot, modpack assembly, world generation or JAR redistribution.
Runs on an isolated, reviewed integration PR branch only.
"""
from __future__ import annotations

import hashlib
import json
import re
import tomllib
import urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SPECS = (
    # mod ID, exact source name, approved Modrinth version ID, expected release family, packwiz side
    ("lootjs", "LootJS", "5AZDyUSn", "3.7.0", "both"),
    ("clumps", "Clumps", "jo7lDoK4", "19.0.0.1", "both"),
    ("yungs-better-strongholds", "YUNG's Better Strongholds", "8U0dIfSM", "5.1.3", "both"),
    ("yungs-better-nether-fortresses", "YUNG's Better Nether Fortresses", "iopJiJQp", "3.1.5", "both"),
    ("yungs-api", "YUNG's API", "2prKITKh", "5.1.9", "both"),
    ("extreme-sound-muffler", "Extreme Sound Muffler", "nUCCmquN", "3.57", "client"),
    ("get-it-together-drops", "Get It Together, Drops!", "K4TUzlwa", "1.4", "both"),
    ("yungs-better-mineshafts", "YUNG's Better Mineshafts", "Go3nbneL", "5.1.1", "both"),
    ("yungs-better-dungeons", "YUNG's Better Dungeons", "ZWDbFefV", "5.1.0", "both"),
    ("yungs-better-desert-temples", "YUNG's Better Desert Temples", "GQ9iNWkI", "4.1.5", "both"),
    ("yungs-better-jungle-temples", "YUNG's Better Jungle Temples", "rimnNoke", "3.1.0", "both"),
    ("yungs-better-ocean-monuments", "YUNG's Better Ocean Monuments", "NWit9vEK", "4.1.1", "both"),
    ("yungs-better-witch-huts", "YUNG's Better Witch Huts", "AvedwcIe", "4.1.1", "both"),
    ("yungs-bridges", "YUNG's Bridges", "yA2nuSdw", "5.1.0", "both"),
    ("yungs-extras", "YUNG's Extras", "uE6pFqck", "5.1.0", "both"),
)
PACK_DATA = (
    "kubejs/data/ars_additions/tags/worldgen/structure/desert_temple.json",
    "kubejs/data/ars_additions/tags/worldgen/structure/jungle_temple.json",
    "kubejs/data/ars_additions/tags/worldgen/structure/monument.json",
    "kubejs/data/ars_additions/tags/worldgen/structure/nether_fortress.json",
    "kubejs/data/ars_additions/tags/worldgen/structure/stronghold.json",
    "kubejs/data/ars_additions/tags/worldgen/structure/witch_hut.json",
    "kubejs/data/tmm/recipe/locate_structure/mineshaft.json",
    "kubejs/data/tmm/recipe/locate_structure/shipwreck.json",
    "kubejs/server_scripts/tmm_structure_registry_export.js",
)
B17 = ("attributefix", "clumps", "extreme-sound-muffler", "get-it-together-drops",
       "lootjs", "yungs-api", "yungs-better-nether-fortresses", "yungs-better-strongholds")
B18 = ("yungs-better-mineshafts", "yungs-better-dungeons", "yungs-better-desert-temples",
       "yungs-better-jungle-temples", "yungs-better-ocean-monuments",
       "yungs-better-witch-huts", "yungs-bridges", "yungs-extras",
       "yungs-better-end-island")
HELD_ID = "yungs-better-end-island"
HELD_VERSION_ID = "I52NZ1qK"


def fetch(version_id: str, expected: str) -> dict:
    url = "https://api.modrinth.com/v2/version/" + version_id
    with urllib.request.urlopen(urllib.request.Request(
        url, headers={"User-Agent": "TMM-YUNG-Source-Pins/2026-11-16"}), timeout=40
    ) as response:
        data = json.load(response)
    if "1.21.1" not in data.get("game_versions", []):
        raise RuntimeError(version_id + ": not for Minecraft 1.21.1")
    if "neoforge" not in [str(s).lower() for s in data.get("loaders", [])]:
        raise RuntimeError(version_id + ": not a NeoForge file")
    number = str(data["version_number"])
    if expected not in number:
        raise RuntimeError(version_id + ": selected version drift " + number)
    files = [f for f in data["files"] if f["filename"].endswith(".jar")]
    if len(files) != 1:
        files = [f for f in files if f.get("primary")]
    if len(files) != 1:
        raise RuntimeError(version_id + ": ambiguous source files")
    f = files[0]
    sha = f.get("hashes", {}).get("sha512")
    if not isinstance(sha, str) or not re.fullmatch(r"[a-f0-9]{128}", sha):
        raise RuntimeError(version_id + ": missing SHA-512")
    if not f["url"].startswith("https://cdn.modrinth.com/"):
        raise RuntimeError(version_id + ": nonofficial download URL")
    return dict(project_id=data["project_id"], version_id=version_id,
                version=number, filename=f["filename"], url=f["url"],
                sha512=sha, dependencies=data.get("dependencies", []))


def write_pin(mid: str, name: str, side: str, file: dict) -> tuple[str, str]:
    path = "mods/" + mid + ".pw.toml"
    content = "\n".join((
        "name = " + json.dumps(name, ensure_ascii=True),
        "filename = " + json.dumps(file["filename"]),
        'side = "' + side + '"',
        "",
        "[download]",
        "url = " + json.dumps(file["url"]),
        'hash-format = "sha512"',
        "hash = " + json.dumps(file["sha512"]),
        "",
        "[update]",
        "[update.modrinth]",
        "mod-id = " + json.dumps(file["project_id"]),
        "version = " + json.dumps(file["version_id"]),
        "",
    ))
    tomllib.loads(content)
    if (ROOT / path).exists():
        raise RuntimeError(path + ": unexpected existing pin; reconcile manually")
    (ROOT / path).write_text(content, encoding="utf-8")
    return path, content


def update_roster(files: list[tuple], held: dict) -> None:
    path = ROOT / "manifest/mods.json"
    m = json.loads(path.read_text(encoding="utf-8"))
    if not any(x["id"] == "attributefix" and x["enabled_in_pack"] for x in m["mods"]):
        raise RuntimeError("Current AttributeFix pin missing; do not duplicate it")
    for mid, name, _, _, side, data in files:
        if any(x["id"] == mid for x in m["mods"]):
            raise RuntimeError(mid + ": existing manifest entry requires manual reconcile")
        m["mods"].append({
            "id": mid, "name": name, "category": "worldgen" if mid.startswith("yungs-") else "qol",
            "desired": True, "version": data["version"], "release_status": "verified_release",
            "source": data["url"], "enabled_in_pack": True, "dependencies_verified": True,
            "packwiz_slug": mid,
            "note": "SHA-512-pinned 1.21.1 NeoForge source/index for B17/B18. No worldgen, client, multiplayer or Ars ritual gameplay claim. Packwiz side: " + side,
        })
    if not any(x["id"] == HELD_ID for x in m["mods"]):
        m["mods"].append({
            "id": HELD_ID, "name": "YUNG's Better End Island",
            "category": "worldgen", "desired": True,
            "version": held["version"], "release_status": "verified_release",
            "source": "https://modrinth.com/mod/yungs-better-end-island/version/" + HELD_VERSION_ID,
            "enabled_in_pack": False, "dependencies_verified": False,
            "note": "Owner-approved conditional mod. Deliberately NOT pinned/installed pending verified BetterEnd: New Dawn worldgen/dragon/End portal compatibility in live B16+B18 QA.",
        })
    if not any(x["id"] == "explorers-compass" for x in m["excluded"]):
        m["excluded"].append({
            "id": "explorers-compass",
            "reason": "Owner declined; the existing native Ars Additions Locate Structure ritual is extended instead.",
        })
    for key, members, label in [
        ("B17-YUNG", B17, "YUNG/loot/performance approved components; distinct from Gilded B17"),
        ("B18", B18, "YUNG expansion with Better End Island held for End compatibility"),
    ]:
        if any(x["id"] == key for x in m["batches"]):
            raise RuntimeError("Existing batch collision: " + key)
        m["batches"].append({
            "id": key, "name": label, "members": list(members),
            "status": "metadata_pinned_awaiting_in_world_QA",
            "note": "All listed mods except conditional Better End Island have source SHA-512 and Packwiz pins. No full server or client test yet.",
        })
    path.write_text(json.dumps(m, indent=2) + "\n", encoding="utf-8")


def update_index(files: dict[str, str]) -> None:
    idx = ROOT / "index.toml"
    pack = ROOT / "pack.toml"
    original = idx.read_text(encoding="utf-8")
    sections = original.split("[[files]]\n")
    header, blocks = sections[0], ["[[files]]\n" + x for x in sections[1:]]
    current = set()
    for block in blocks:
        match = re.search(r'^file = "([^"]+)"$', block, flags=re.MULTILINE)
        if match is None:
            raise RuntimeError("Malformed existing Packwiz index")
        current.add(match.group(1))
    for path, content in files.items():
        if path in current:
            raise RuntimeError(path + " is already indexed: review instead of overwriting")
        sha = hashlib.sha256(content.encode("utf-8")).hexdigest()
        new = '[[files]]\nfile = "' + path + '"\nhash = "' + sha + '"\n'
        if path.startswith("mods/"):
            new += "metafile = true\n"
        new += "\n"
        at = next((i for i, b in enumerate(blocks)
                   if (m := re.search(r'^file = "([^"]+)"$', b, flags=re.MULTILINE))
                   and m.group(1).startswith(path.split("/")[0] + "/") and m.group(1) > path), len(blocks))
        blocks.insert(at, new)
    result = (header + "".join(blocks)).rstrip() + "\n"
    oldhash = hashlib.sha256(original.encode()).hexdigest()
    ptext = pack.read_text(encoding="utf-8")
    if ptext.count(oldhash) != 1:
        raise RuntimeError("Packwiz root index SHA mismatch before changes")
    idx.write_text(result, encoding="utf-8")
    pack.write_text(ptext.replace(oldhash, hashlib.sha256(result.encode()).hexdigest()), encoding="utf-8")


def patch_validators() -> None:
    b17 = ROOT / "scripts/validate_b17_ars_locators.py"
    a = b17.read_text(encoding="utf-8")
    before = 'item["desired"] and not item["enabled_in_pack"]'
    if before not in a:
        raise RuntimeError("B17 validator drift")
    a = a.replace(before, 'item["desired"] and item["enabled_in_pack"]')
    a = a.replace("Unpinned B17 mod incorrectly marked installed", "B17 pinned mod missing from Packwiz")
    b17.write_text(a, encoding="utf-8")
    b18 = ROOT / "scripts/validate_b18_yungs_ars.py"
    a = b18.read_text(encoding="utf-8")
    before = 'assert all(m["desired"] and not m["enabled_in_pack"] for m in entries.values())'
    if before not in a:
        raise RuntimeError("B18 validator drift")
    a = a.replace(before, 'assert all(m["desired"] and (not m["enabled_in_pack"] if mid == "yungs-better-end-island" else m["enabled_in_pack"]) for mid, m in entries.items())')
    b18.write_text(a, encoding="utf-8")


def main() -> None:
    resolved = [(mid, name, vid, expected, side, fetch(vid, expected))
                for mid, name, vid, expected, side in SPECS]
    held = fetch(HELD_VERSION_ID, "3.1.2")  # resolve metadata, but DO NOT install this candidate
    selected_ids = {d["project_id"] for *_, d in resolved}
    for mid, _, _, _, _, data in resolved:
        required = [d["project_id"] for d in data["dependencies"]
                    if d.get("dependency_type") == "required" and d.get("project_id")]
        for dep in required:
            if dep not in selected_ids and mid.startswith("yungs-"):
                raise RuntimeError(mid + ": required YUNG dependency not in selected pins " + dep)
    paths = dict(write_pin(mid, name, side, data) for mid, name, _, _, side, data in resolved)
    for path in PACK_DATA:
        f = ROOT / path
        if not f.is_file():
            raise RuntimeError("Missing staged source: " + path)
        paths[path] = f.read_text(encoding="utf-8")
    update_roster(resolved, held)
    patch_validators()
    update_index(paths)
    print("PINNED: 15 new YUNG/loot/QoL mods + 9 selected Ars datapack/export files")
    for mid, _, _, _, side, data in resolved:
        print(mid, side, data["version"], data["sha512"])
    print("HOLD: YUNG Better End Island remains uninstalled until compatible End QA")
    print("NOT TESTED: generated structure registry, full modpack, client, worldgen, gameplay")


if __name__ == "__main__":
    main()
