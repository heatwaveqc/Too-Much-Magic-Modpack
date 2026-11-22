#!/usr/bin/env python3
"""Prepare a FOUR-MOD *isolated* source candidate. No game boot or publishing.

Run only on experiment/four-mod-update-smoke-20261121, with a clean worktree.
Default mode resolves and verifies the four exact JARs without writing.
--apply updates only four Packwiz mod pins, manifest versions, index and pack hash.
Network/metadata/hash failures are fatal; no fallback versions or guessed hashes.
"""
from __future__ import annotations

import argparse
import hashlib
import io
import json
import re
import subprocess
import sys
import tomllib
import urllib.parse
import urllib.request
import zipfile
from pathlib import Path

BRANCH = "experiment/four-mod-update-smoke-20261121"
BASE_MODS = {
    "neovitae": ("rvaW4C93", "neovitae-1.21.1-1.1.54.jar", "Neo Vitae", "neovitae-1.21.1-1.1.54"),
    "evilcraft": ("3ANq2btA", "evilcraft-1.21.1-neoforge-1.2.102.jar", "EvilCraft", "evilcraft-1.21.1-neoforge-1.2.102"),
    "goety": ("4ZVIxU8x", "goety-3.2.3.jar", "Goety - The Dark Arts", "goety-3.2.3"),
}
ARS = {
    "file_id": 9716535,
    "project_id": 561470,
    "filename": "ars_elemental-1.21.1-0.7.11.3.jar",
    "version": "ars_elemental-1.21.1-0.7.11.3",
}
MAX_JAR_BYTES = 40 * 1024 * 1024

def fetch(url: str, *, jar: bool = False) -> bytes:
    req = urllib.request.Request(url, headers={"User-Agent": "TooMuchMagic-UpdateTrial/1.0"})
    with urllib.request.urlopen(req, timeout=45) as resp:
        data = resp.read(MAX_JAR_BYTES + 1 if jar else 2_000_001)
        if jar and len(data) > MAX_JAR_BYTES:
            raise RuntimeError("Oversized candidate binary")
        if not jar and len(data) > 2_000_000:
            raise RuntimeError("Oversized metadata")
        return data

def modrinth_candidate(project: str, filename: str) -> dict:
    query = urllib.parse.urlencode({
        "loaders": json.dumps(["neoforge"]),
        "game_versions": json.dumps(["1.21.1"]),
    })
    versions = json.loads(fetch(f"https://api.modrinth.com/v2/project/{project}/version?{query}"))
    hits = [
        (release, artifact)
        for release in versions
        if release.get("project_id") == project
        and release.get("version_type") == "release"
        and "neoforge" in release.get("loaders", [])
        and "1.21.1" in release.get("game_versions", [])
        for artifact in release.get("files", [])
        if artifact.get("filename", "").lower() == filename.lower()
        and artifact.get("primary", False)
    ]
    if len(hits) != 1:
        raise RuntimeError(f"{filename}: expected one official stable NeoForge 1.21.1 artifact, got {len(hits)}")
    release, artifact = hits[0]
    sha = artifact.get("hashes", {}).get("sha512", "")
    url = artifact.get("url", "")
    parsed = urllib.parse.urlparse(url)
    if parsed.scheme != "https" or parsed.hostname != "cdn.modrinth.com":
        raise RuntimeError(f"{filename}: download URL not on official Modrinth CDN")
    if not parsed.path.startswith(f"/data/{project}/versions/{release['id']}/"):
        raise RuntimeError(f"{filename}: CDN project/release path mismatch")
    if not re.fullmatch(r"[0-9a-fA-F]{128}", sha):
        raise RuntimeError(f"{filename}: provider supplied no valid SHA-512")
    jar = fetch(url, jar=True)
    actual = hashlib.sha512(jar).hexdigest()
    if actual != sha.lower():
        raise RuntimeError(f"{filename}: downloaded bytes differ from provider SHA-512")
    verify_jar(jar, filename)
    return {"filename": filename, "url": url, "sha512": actual, "provider": "modrinth",
            "project": project, "file": release["id"], "channel": "release"}

def verify_jar(data: bytes, expected: str) -> None:
    if not zipfile.is_zipfile(io.BytesIO(data)):
        raise RuntimeError(f"{expected}: expected a valid JAR")
    with zipfile.ZipFile(io.BytesIO(data)) as jar:
        if not any(name in jar.namelist() for name in
                   ("META-INF/neoforge.mods.toml", "META-INF/mods.toml")):
            raise RuntimeError(f"{expected}: no NeoForge/Forge mod metadata in archive")

def ars_candidate() -> dict:
    # Exact project/file ID and filename verified from the official CurseForge file page.
    # This is the only BETA candidate. Construct the documented ForgeCDN file-ID path.
    file_id = ARS["file_id"]
    filename = ARS["filename"]
    url = f"https://mediafilez.forgecdn.net/files/{file_id // 1000}/{file_id % 1000:03d}/{filename}"
    jar = fetch(url, jar=True)
    verify_jar(jar, filename)
    return {"filename": filename, "url": url,
            "sha512": hashlib.sha512(jar).hexdigest(), "provider": "curseforge",
            "project": ARS["project_id"], "file": file_id, "channel": "beta"}

def pin_text(name: str, item: dict) -> str:
    q = lambda value: json.dumps(str(value), ensure_ascii=False)
    lines = [f"name = {q(name)}", f"filename = {q(item['filename'])}", 'side = "both"',
             "", "[download]", f"url = {q(item['url'])}", 'hash-format = "sha512"',
             f'hash = "{item["sha512"]}"', "", "[update]"]
    if item["provider"] == "modrinth":
        lines += ["[update.modrinth]", f"mod-id = {q(item['project'])}", f"version = {q(item['file'])}"]
    else:
        lines += ["[update.curseforge]", f"file-id = {item['file']}", f"project-id = {item['project']}"]
    return "\n".join(lines) + "\n"

def replace_index_hash(index: str, slug: str, digest: str) -> str:
    pattern = r'(?m)(^file = "mods/' + re.escape(slug) + r'\.pw\.toml"\n(?:hash-format = "[^"]+"\n)?hash = ")[0-9a-f]{64}(")'
    result, count = re.subn(pattern, lambda m: m[1] + digest + m[2], index)
    if count != 1:
        raise RuntimeError(f"{slug}: expected exactly one indexed Packwiz entry, got {count}")
    return result

def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--apply", action="store_true", help="Write checked/rehashed pins into this isolated branch")
    args = parser.parse_args()
    root = Path(__file__).resolve().parent.parent
    active = subprocess.check_output(["git", "branch", "--show-current"], cwd=root, text=True).strip()
    if active != BRANCH:
        raise RuntimeError(f"Unsafe checkout: expected branch {BRANCH}; got {active}")
    if subprocess.run(["git", "diff", "--quiet"], cwd=root, check=False).returncode:
        raise RuntimeError("Worktree is dirty; do not overwrite concurrent work")
    results = {}
    for slug, (project, filename, _, _) in BASE_MODS.items():
        results[slug] = modrinth_candidate(project, filename)
    results["ars-elemental"] = ars_candidate()
    print(json.dumps({"base":"test/full-integration@2373d50732866e460dc9e9454f7187f9045e1cdc",
                      "four_candidates":results}, indent=2))
    if not args.apply:
        print("Dry run only: no pins changed, no Minecraft smoke attempted.")
        return
    index_path = root / "index.toml"
    pack_path = root / "pack.toml"
    manifest_path = root / "manifest/mods.json"
    index = index_path.read_text(encoding="utf-8")
    manifest = json.loads(manifest_path.read_text(encoding="utf-8"))
    by_id = {entry["id"]: entry for entry in manifest["mods"]}
    for slug, candidate in results.items():
        if not by_id[slug].get("enabled_in_pack"):
            raise RuntimeError(f"{slug}: missing active roster entry")
        if slug == "ars-elemental":
            name, version = "Ars Elemental", ARS["version"]
            by_id[slug]["release_status"] = "experimental_release"
            by_id[slug]["note"] = (by_id[slug].get("note", "") + " Isolated trial of CurseForge "
                                   "beta 0.7.11.3, file 9716535; not approved for promotion.").strip()
        else:
            name, version = BASE_MODS[slug][2:]
        target = root / f"mods/{slug}.pw.toml"
        old = tomllib.loads(target.read_text(encoding="utf-8"))
        if old.get("side") != "both":
            raise RuntimeError(f"{slug}: unexpected existing side")
        by_id[slug]["version"] = version
        new_text = pin_text(name, candidate)
        index = replace_index_hash(index, slug, hashlib.sha256(new_text.encode()).hexdigest())
        results[slug]["prepared_pin"] = new_text
    pack = pack_path.read_text(encoding="utf-8")
    pattern = r'(?m)(^\[index\]\nfile = "index.toml"\nhash-format = "sha256"\nhash = ")[0-9a-f]{64}(")'
    pack_new, count = re.subn(pattern, lambda m: m[1] + hashlib.sha256(index.encode()).hexdigest() + m[2], pack)
    if count != 1:
        raise RuntimeError(f"Expected one pack index reference, got {count}")
    # All network and validation operations completed before the first write.
    for slug, candidate in results.items():
        (root / f"mods/{slug}.pw.toml").write_text(candidate["prepared_pin"], encoding="utf-8", newline="\n")
    manifest_path.write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + "\n", encoding="utf-8", newline="\n")
    index_path.write_text(index, encoding="utf-8", newline="\n")
    pack_path.write_text(pack_new, encoding="utf-8", newline="\n")
    print("Prepared four selected pins and complete SHA-256 Packwiz chain. RUN QUICK CI; NOT SMOKED.")

if __name__ == "__main__":
    try:
        main()
    except Exception as exc:
        print(f"ERROR: {exc}", file=sys.stderr)
        raise SystemExit(1)
