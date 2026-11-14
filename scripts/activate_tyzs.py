#!/usr/bin/env python3
"""Activate the SINGLE Tyz 6.4.2 primary tree in a disposable integration checkout.

Exact upstream release ID, game version, loader, and SHA-512 are checked before
building an immutable packwiz snapshot. This never downloads or commits JARs.
Not a substitute for an in-game test or the future MXP dispatcher.
"""
from __future__ import annotations

import json
import re
import subprocess
import sys
import urllib.parse
import urllib.request
from pathlib import Path

MODRINTH_PROJECT = "4rKNK5O2"
MODRINTH_VERSION = "OUmvFAPs"  # Tyz 6.4.2, MC 1.21.1 NeoForge. Never "latest".
EXPECTED_VERSION = "6.4.2"
EXPECTED_SHA512 = "070abb0665dee3a442b57a9957eed206adf4b510e66ce648ba3e68098fa1cd4e570af01950df0be3163eaf0469d84b79e1afc8d3c44924399f074584084b55f3"
RELEASE_URL = f"https://api.modrinth.com/v2/version/{MODRINTH_VERSION}"
DEFAULT_CATEGORIES = {"abilities", "combat", "misc", "nature"}
SKILL_IDS = {"tmm_magic_reservoir_1", "tmm_magic_wellspring_1",
             "tmm_magic_thrift_1", "tmm_magic_potency_1"}

def source_metadata() -> tuple[str, str, str]:
    req = urllib.request.Request(RELEASE_URL, headers={"User-Agent": "TooMuchMagic-QA/1.0"})
    with urllib.request.urlopen(req, timeout=25) as response:
        metadata = json.load(response)
    if (metadata.get("id") != MODRINTH_VERSION or
            metadata.get("project_id") != MODRINTH_PROJECT or
            metadata.get("version_number") != EXPECTED_VERSION or
            "1.21.1" not in metadata.get("game_versions", []) or
            "neoforge" not in metadata.get("loaders", [])):
        raise ValueError("Tyz release identity / loader / MC version mismatch")
    jars = [f for f in metadata.get("files", []) if f.get("primary") and
            str(f.get("filename", "")).lower().endswith(".jar")]
    if len(jars) != 1:
        raise ValueError("Tyz 6.4.2 must identify exactly one primary JAR")
    jar = jars[0]
    filename = jar["filename"]
    url = jar["url"]
    parts = urllib.parse.urlsplit(url)
    if (EXPECTED_VERSION not in filename or "1.21.1" not in filename or
            parts.scheme != "https" or parts.netloc != "cdn.modrinth.com" or
            not parts.path.startswith(f"/data/{MODRINTH_PROJECT}/versions/{MODRINTH_VERSION}/") or
            parts.username or parts.password):
        raise ValueError("Unrecognized primary Tyz JAR URL/filename; refuse to pin")
    sha512 = jar.get("hashes", {}).get("sha512")
    if not isinstance(sha512, str) or not re.fullmatch(r"[0-9a-fA-F]{128}", sha512):
        raise ValueError("Upstream Tyz release has no valid SHA-512")
    if sha512.lower() != EXPECTED_SHA512:
        raise ValueError("Tyz provider SHA-512 changed: fail closed instead of silently updating")
    return filename, url, sha512.lower()

def write_packwiz(root: Path, filename: str, url: str, sha512: str) -> None:
    old = root / "mods/puffish-skills.pw.toml"
    if not old.is_file():
        raise ValueError("Expected original Pufferfish Skills pin missing; inspect migration first")
    if (root / "mods/tyzs-skills.pw.toml").exists():
        raise ValueError("Tyz already pinned; refuse to overwrite without review")
    if (root / "config/puffish_skills").exists():
        raise ValueError("Pufferfish category/skill config still present; migrate before removal")
    old.unlink()
    # JSON escapes TOML strings correctly for this restricted source format.
    toml = (
        'name = "Tyz\'s Skills"\n'
        f'filename = {json.dumps(filename)}\n'
        'side = "both"\n\n'
        '[download]\n'
        f'url = {json.dumps(url)}\n'
        'hash-format = "sha512"\n'
        f'hash = "{sha512}"\n\n'
        '[update]\n[update.modrinth]\n'
        f'mod-id = "{MODRINTH_PROJECT}"\n'
        f'version = "{MODRINTH_VERSION}"\n'
    )
    (root / "mods/tyzs-skills.pw.toml").write_text(toml, encoding="utf-8")

def update_manifest(manifest: dict) -> None:
    by_id = {m["id"]: m for m in manifest["mods"]}
    if "puffish-skills" not in by_id:
        raise ValueError("Pufferfish Skills manifest entry missing")
    old = by_id["puffish-skills"]
    old["desired"] = False
    old["enabled_in_pack"] = False
    old["note"] = ("Removed from the rolling Tyz 6.4.2 integration channel; "
                   "historical B6/B8 rollback remains immutable. "
                   "Pufferfish Attributes is a separate dependency and is retained pending audit.")
    if "tyzs-skills" in by_id:
        new = by_id["tyzs-skills"]
        if new.get("version") != "Tyz's skills 6.4.2 - 1.21.1":
            raise ValueError("Tyz manifest version conflict")
    else:
        new = {
            "id": "tyzs-skills", "name": "Tyz's Skills", "category": "skills",
            "desired": True, "version": "Tyz's skills 6.4.2 - 1.21.1",
            "release_status": "verified_release",
            "source": "https://modrinth.com/mod/tyzs-skills/version/6.4.2",
            "enabled_in_pack": False, "dependencies_verified": False,
            "candidate_file_id": 8118598, "curseforge_project_id": 990190,
            "packwiz_slug": "tyzs-skills"
        }
        manifest["mods"].append(new)
    new["desired"] = True
    new["enabled_in_pack"] = True
    new["note"] = ("First-wave integration: four 99-rank Core Magic skills, one shared Tyz "
                   "SP wallet, all Tyz native activity XP disabled. Until the canonical "
                   "MXP dispatcher is runtime-integrated, XP is available via explicit "
                   "operator test grants only. Core Magic and vanilla Tyz categories "
                   "are NOT a second Monk tree.")
    # Do not casually remove this independent library, which may have other dependents.
    if "puffish-attributes" not in by_id or not by_id["puffish-attributes"]["enabled_in_pack"]:
        raise ValueError("Pufferfish Attributes dependency safety requires an explicit audit")

def activate(root: Path, manifest: dict) -> None:
    filename, url, sha512 = source_metadata()
    stage = root / "compat/magic/stage_preview.py"
    subprocess.run([sys.executable, str(stage), "--check"], check=True, cwd=root)
    subprocess.run([sys.executable, str(stage), "--install", "--pack-root", str(root)],
                   check=True, cwd=root)
    cfg = root / "config/tyzs_skills/custom/data/categories.json"
    cats = json.loads(cfg.read_text(encoding="utf-8"))
    if not DEFAULT_CATEGORIES.issubset(cats) or "tmm_magic_core" not in cats:
        raise ValueError("Tyz category override lost its default tabs")
    skills = root / "config/tyzs_skills/custom/skills"
    if {x.stem for x in skills.glob("tmm_*.json")} != SKILL_IDS:
        raise ValueError("First-wave Tyz skill set incomplete")
    write_packwiz(root, filename, url, sha512)
    update_manifest(manifest)
    print(f"PINNED Tyz {EXPECTED_VERSION} as SHA-512 {sha512}; removed Pufferfish Skills.")
    print("MXP dispatcher remains PENDING; native Tyz activity XP disabled.")
