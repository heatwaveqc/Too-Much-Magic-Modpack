#!/usr/bin/env python3
"""Read-only exact binary/dependency survey for owner-approved Creature Feature #419.

Strictly verifies selected Modrinth releases, downloads *only* their official
JARs to memory for SHA-512 / NeoForge metadata inspection, and prints proposed
Packwiz TOML. No checkout edits, pack assembly, server boot or release action.
"""
from __future__ import annotations

import argparse
import hashlib
import io
import json
import re
import tomllib
import urllib.parse
import urllib.request
import zipfile
from pathlib import Path

API = "https://api.modrinth.com/v2"
GAME = "1.21.1"
LOADER = "neoforge"
RELEASES = {
    "creature-feature": ("Creature Feature", "7nEkcq0F", "WSnoKdtT", "1.3.0"),
    "blueprint": ("Blueprint", "VsM5EDoI", "5JOCYuQM", "8.1.0"),
}
USER_AGENT = {"User-Agent": "heatwaveqc/Too-Much-Magic-Modpack-CreatureFeature-intake/1.0"}
ROOT = Path(__file__).resolve().parents[1]


def require(condition: bool, message: str) -> None:
    if not condition:
        raise ValueError(message)


def download(url: str, *, max_bytes: int) -> bytes:
    request = urllib.request.Request(url, headers=USER_AGENT)
    with urllib.request.urlopen(request, timeout=70) as response:
        require(response.status == 200, f"Provider returned HTTP {response.status}: {url}")
        chunks = bytearray()
        while True:
            block = response.read(min(1 << 20, max_bytes + 1 - len(chunks)))
            if not block:
                break
            chunks.extend(block)
            require(len(chunks) <= max_bytes, f"Unexpectedly large download: {url}")
        return bytes(chunks)


def official_file(version: dict, slug: str) -> dict:
    name, project_id, version_id, version_number = RELEASES[slug]
    require(version.get("id") == version_id and version.get("project_id") == project_id,
            f"{slug}: unexpected provider identity")
    require(version.get("version_number") == version_number and version.get("version_type") == "release",
            f"{slug}: unexpected release/version")
    require(GAME in version.get("game_versions", []) and LOADER in version.get("loaders", []),
            f"{slug}: wrong game/loader")
    files = [
        f for f in version.get("files", [])
        if f.get("primary") and f.get("filename", "").endswith(".jar")
        and not f.get("filename", "").endswith(("-sources.jar", "-javadoc.jar"))
    ]
    require(len(files) == 1, f"{slug}: expected one primary runtime jar, got {len(files)}")
    f = files[0]
    filename = f.get("filename", "")
    url = f.get("url", "")
    parsed = urllib.parse.urlparse(url)
    expected_prefix = f"/data/{project_id}/versions/{version_id}/"
    require(bool(filename) and "/" not in filename and "\\\\" not in filename,
            f"{slug}: invalid basename")
    require(parsed.scheme == "https" and parsed.hostname == "cdn.modrinth.com"
            and parsed.path == expected_prefix + urllib.parse.quote(filename, safe="")
            and not parsed.username and not parsed.password and not parsed.query and not parsed.fragment,
            f"{slug}: untrusted official URL")
    sha512 = f.get("hashes", {}).get("sha512", "")
    require(bool(re.fullmatch("[0-9a-f]{128}", sha512)), f"{slug}: missing SHA-512")
    return {"id": slug, "name": name, "project": project_id, "version_id": version_id,
            "version": version_number, "filename": filename, "url": url, "sha512": sha512,
            "dependencies": version.get("dependencies", []), "side": "both"}


def scan_jar(data: bytes, slug: str) -> dict:
    require(100_000 < len(data) < 40_000_000, f"{slug}: invalid JAR size")
    with zipfile.ZipFile(io.BytesIO(data)) as jar:
        files = set(jar.namelist())
        meta = next((f for f in ("META-INF/neoforge.mods.toml", "META-INF/mods.toml") if f in files), None)
        require(meta is not None, f"{slug}: missing NeoForge mod metadata")
        mod_info = tomllib.loads(jar.read(meta).decode("utf-8"))
        mod_ids = [m.get("modId") for m in mod_info.get("mods", [])]
        require(mod_ids and all(isinstance(mid, str) for mid in mod_ids), f"{slug}: no mod identities")
        if slug == "creature-feature":
            require("creaturefeature" in mod_ids, f"{slug}: unexpected runtime mod ID(s) {mod_ids}")
        if slug == "blueprint":
            require("blueprint" in mod_ids, f"{slug}: unexpected runtime mod ID(s) {mod_ids}")
        mandatory = []
        for mod, deps in mod_info.get("dependencies", {}).items():
            for dep in deps:
                if dep.get("type", "required") == "required" or dep.get("mandatory") is True:
                    mandatory.append({"declared_by": mod, "mod_id": dep.get("modId"),
                                      "version_range": dep.get("versionRange", ""),
                                      "side": dep.get("side", "BOTH")})
        jarjar = sorted(f for f in files if f.startswith("META-INF/jarjar/") and f.endswith(".jar"))
        return {"mod_ids": mod_ids, "mandatory": mandatory, "embedded_jars": jarjar,
                "metadata_path": meta}


def toml(row: dict) -> str:
    quote = json.dumps
    return (f"name = {quote(row['name'])}\nfilename = {quote(row['filename'])}\nside = \"both\"\n\n"
            f"[download]\nurl = {quote(row['url'])}\nhash-format = \"sha512\"\nhash = {quote(row['sha512'])}\n\n"
            f"[update]\n[update.modrinth]\nmod-id = {quote(row['project'])}\n"
            f"version = {quote(row['version_id'])}\n")


def report() -> dict:
    entries = []
    for slug in RELEASES:
        version_id = RELEASES[slug][2]
        version = json.loads(download(f"{API}/version/{version_id}", max_bytes=500_000))
        row = official_file(version, slug)
        payload = download(row["url"], max_bytes=40_000_000)
        require(hashlib.sha512(payload).hexdigest() == row["sha512"],
                f"{slug}: provider JAR hash mismatch")
        row["jar"] = scan_jar(payload, slug)
        entries.append(row)
    return {"schema": 1, "issue": 419, "source": "official Modrinth exact version API and CDN",
            "verified": entries}


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--output", type=Path, help="Optional OUTPUT DIRECTORY for proposed pins and audit JSON")
    args = parser.parse_args()
    result = report()
    for row in result["verified"]:
        print("CREATURE_PIN", row["id"], json.dumps({
            "filename": row["filename"], "url": row["url"], "sha512": row["sha512"],
            "project": row["project"], "version_id": row["version_id"],
            "dependencies": row["dependencies"], "jar": row["jar"]}, separators=(",", ":")), flush=True)
        print("CREATURE_TOML", row["id"], json.dumps(toml(row)), flush=True)
    if args.output:
        args.output.mkdir(parents=True, exist_ok=True)
        for row in result["verified"]:
            (args.output / (row["id"] + ".pw.toml")).write_text(toml(row), encoding="utf-8")
        (args.output / "creature-feature-audit.json").write_text(
            json.dumps(result, indent=2) + "\n", encoding="utf-8")
    print("CREATURE_AUDIT 2 official runtime JARs verified; other required dependencies need reconciliation")


if __name__ == "__main__":
    main()
