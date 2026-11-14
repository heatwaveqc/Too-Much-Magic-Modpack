#!/usr/bin/env python3
"""Replace More Overlays' CurseForge metadata with the IDENTICAL official Modrinth JAR.

Only change the provider if the exact 1.21.1 NeoForge JAR has the same SHA1
as the previously approved CurseForge file. Do not substitute similar builds.
"""
from __future__ import annotations

import hashlib
import json
import pathlib
import re
import tomllib
import urllib.request

ROOT = pathlib.Path(__file__).resolve().parents[1]
META = ROOT / "mods/more-overlays-updated.pw.toml"
INDEX = ROOT / "index.toml"
PACK = ROOT / "pack.toml"
MANIFEST = ROOT / "manifest/mods.json"

PROJECT = "Thy5Pqut"
VERSION = "Kq8xaqKi"
FILENAME = "moreoverlays-1.24.2-mc1.21.1-neoforge.jar"
EXISTING_SHA1 = "21e8bd61c5e146a9574266015d3f64621c2d9e18"


def ensure(ok: bool, message: str) -> None:
    if not ok:
        raise ValueError(message)


def main() -> None:
    req = urllib.request.Request(
        f"https://api.modrinth.com/v2/version/{VERSION}",
        headers={"User-Agent": "TooMuchMagicModpack/1.0 (client hash verification)",
                 "Accept": "application/json"},
    )
    with urllib.request.urlopen(req, timeout=25) as resp:
        ver = json.load(resp)
    ensure(ver.get("id") == VERSION and ver.get("project_id") == PROJECT,
           "Modrinth returned wrong project or version")
    ensure("1.21.1" in ver.get("game_versions", []),
           "Modrinth version lacks Minecraft 1.21.1 support")
    ensure("neoforge" in [x.lower() for x in ver.get("loaders", [])],
           "Modrinth version does not support NeoForge")
    matching = [x for x in ver.get("files", []) if x.get("filename") == FILENAME]
    ensure(len(matching) == 1, f"Wrong/ambiguous Modrinth file: {matching}")
    file = matching[0]
    ensure(file.get("hashes", {}).get("sha1") == EXISTING_SHA1,
           "Modrinth binary differs from approved CurseForge SHA1; STOP, review manually")
    sha512 = file.get("hashes", {}).get("sha512", "")
    ensure(bool(re.fullmatch("[0-9a-f]{128}", sha512)), "Missing Modrinth SHA512 pin")
    url = file.get("url", "")
    ensure(url.startswith(f"https://cdn.modrinth.com/data/{PROJECT}/versions/{VERSION}/"),
           "Official Modrinth file URL unexpected")
    old = tomllib.loads(META.read_text(encoding="utf-8"))
    ensure(old.get("side") == "client" and old.get("filename") == FILENAME,
           "More Overlays file / client-only setting has drifted")
    if old.get("download", {}).get("hash-format") == "sha512":
        ensure(old["download"]["hash"] == sha512 and old["download"]["url"] == url,
               "Already-pinned Modrinth metadata differs from official version")
        print("PASS: exact Modrinth pin already active")
    else:
        ensure(old.get("download", {}).get("hash") == EXISTING_SHA1,
               "Existing CurseForge SHA1 changed; refuse silent re-pin")
        ensure(old.get("update", {}).get("curseforge", {}).get("file-id") == 6981252,
               "Existing CurseForge file version changed")
        new = (
            f'name = "More Overlays Updated"\n'
            f'filename = "{FILENAME}"\n'
            f'side = "client"\n\n'
            f'[download]\nurl = "{url}"\n'
            f'hash-format = "sha512"\nhash = "{sha512}"\n\n'
            f'[update]\n[update.modrinth]\n'
            f'mod-id = "{PROJECT}"\nversion = "{VERSION}"\n'
        )
        META.write_text(new, encoding="utf-8")
        index = INDEX.read_text(encoding="utf-8")
        pat = re.compile(r'(file = "mods/more-overlays-updated\.pw\.toml"\nhash = ")[0-9a-f]+(")')
        ensure(bool(pat.search(index)), "More Overlays absent from packwiz index")
        new_hash = hashlib.sha256(new.encode("utf-8")).hexdigest()
        index = pat.sub(lambda x: x.group(1) + new_hash + x.group(2), index, count=1)
        INDEX.write_text(index, encoding="utf-8")
        pack = PACK.read_text(encoding="utf-8")
        ptr = re.compile(r'(\[index\]\nfile = "index.toml"\nhash-format = "sha256"\nhash = ")[0-9a-f]+(")')
        ensure(bool(ptr.search(pack)), "Missing packwiz index pointer")
        pack = ptr.sub(
            lambda x: x.group(1) + hashlib.sha256(index.encode()).hexdigest() + x.group(2),
            pack, count=1,
        )
        PACK.write_text(pack, encoding="utf-8")
        print("PASS: switched More Overlays download to identical official Modrinth JAR")
    roster = json.loads(MANIFEST.read_text(encoding="utf-8"))
    match = [x for x in roster["mods"] if x.get("id") == "more-overlays-updated"]
    ensure(len(match) == 1 and match[0]["enabled_in_pack"], "Approved active mod missing")
    match[0]["note"] = (
        "Owner-approved 1.21.1 NeoForge client QoL 1.24.2. "
        "Identical SHA1 to CurseForge file 6981252, directly downloaded via "
        "official Modrinth version Kq8xaqKi to avoid manual Prism installs. "
        "F7 light-level, F9 chunk and JEI item overlays."
    )
    MANIFEST.write_text(json.dumps(roster, indent=2) + "\n", encoding="utf-8")
    print(f"PASS: {FILENAME}, SHA1={EXISTING_SHA1}, SHA512={sha512}")
    print("GAME CLIENT UNTESTED: confirm Prism downloaded the client-only mod.")


if __name__ == "__main__":
    main()
