#!/usr/bin/env python3
"""Targeted OFFLINE/remote audit of approved, currently unindexed Packwiz candidates.

This is NOT integration assembly: no existing pack JARs are downloaded, no
Minecraft processes or Prism archives are produced. External downloads happen
only with --verify-official, for the explicit candidate list below.
"""
from __future__ import annotations

import argparse
import hashlib
import json
from pathlib import Path
import re
import sys
import tomllib
from urllib.error import URLError, HTTPError
from urllib.parse import quote
from urllib.request import Request, urlopen
from zipfile import ZipFile, BadZipFile
from io import BytesIO

ROOT = Path(__file__).resolve().parents[1]
CANDIDATES = [
    "aquamirae", "fragmentum-neoforge", "mowzies-mobs", "sons-of-sins",
    "the-graveyard", "villagerstradingplus", "aura-cascade-reimagined",
    "lets-do-herbalbrews", "aquaculture-delight", "born-in-chaos",
    "dungeon-crawl", "just-enough-professions", "ldlib2",
    "lets-do-brewery", "lets-do-farm-and-charm",
    "prefab-custom-addon", "yungs-better-end-island",
    "lets-do-foundation", "ars-conflux",
]
# Informational: the exact publisher binaries remain the source of truth.
KNOWN_BASELINE_DEPS = {
    "minecraft", "neoforge", "forge", "javafml", "neoforge_fml",
    "geckolib", "patchouli", "architectury", "ars_nouveau",
    "jei", "prefab", "yungsapi", "yungs_api", "curios",
    "framework", "moonlight", "blueprint", "balm", "cloth_config",
    "ftbchunks", "farmersdelight", "aquaculture", "modonomicon",
}
MAX_ONE_FILE = 105 * 1024 * 1024
TIMEOUT = 85


def source_urls(pin: dict) -> list[str]:
    dl = pin["download"]
    if dl.get("url"):
        return [dl["url"]]
    cf = pin.get("update", {}).get("curseforge")
    if not cf:
        raise ValueError("No official direct URL or CurseForge release ID")
    n = int(cf["file-id"])
    fname = quote(pin["filename"], safe="._-")
    return [
        f"https://mediafilez.forgecdn.net/files/{n // 1000}/{n % 1000:03d}/{fname}",
        f"https://media.forgecdn.net/files/{n // 1000}/{n % 1000:03d}/{fname}",
    ]


def download(pin: dict) -> tuple[bytes, str]:
    errors = []
    for url in source_urls(pin):
        try:
            req = Request(url, headers={"User-Agent": "TMM-Approval-Audit/1.0"})
            with urlopen(req, timeout=TIMEOUT) as response:
                if int(response.headers.get("Content-Length", "0")) > MAX_ONE_FILE:
                    raise ValueError("Artifact exceeds per-file audit limit")
                data = response.read(MAX_ONE_FILE + 1)
            if len(data) > MAX_ONE_FILE:
                raise ValueError("Artifact exceeds per-file audit limit")
            if len(data) < 100:
                raise ValueError("Unexpectedly small JAR")
            return data, url
        except (URLError, HTTPError, TimeoutError, ValueError, OSError) as exc:
            errors.append(f"{url}: {exc}")

    # Official alternate publisher CDN, not a third-party mirror.
    # Preserve the original candidate hash comparison after download.
    if pin.get("name") == "[Let's Do] HerbalBrews":
        try:
            req = Request("https://api.modrinth.com/v2/project/lets-do-herbalbrews/version",
                          headers={"User-Agent": "TMM-Approval-Audit/1.0"})
            with urlopen(req, timeout=TIMEOUT) as response:
                versions = json.load(response)
            releases = [v for v in versions if "1.21.1" in v.get("game_versions", [])
                        and "neoforge" in v.get("loaders", [])
                        and "1.1.4" in v.get("version_number", "")]
            for v in releases:
                for file in v.get("files", []):
                    if file.get("filename") == pin["filename"] and file.get("url", "").startswith("https://cdn.modrinth.com/"):
                        url = file["url"]
                        with urlopen(Request(url, headers={"User-Agent": "TMM-Approval-Audit/1.0"}),
                                     timeout=TIMEOUT) as response:
                            data = response.read(MAX_ONE_FILE + 1)
                        if len(data) > MAX_ONE_FILE or len(data) < 100:
                            raise ValueError("Bad official Modrinth file size")
                        # Verify Modrinth's own published exact SHA too.
                        remote_sha = file.get("hashes", {}).get("sha1")
                        if remote_sha and hashlib.sha1(data).hexdigest() != remote_sha:
                            raise ValueError("Modrinth official SHA1 mismatched downloaded bytes")
                        return data, url
            errors.append("No exact official 1.1.4 NeoForge Modrinth binary matched expected filename")
        except (URLError, HTTPError, TimeoutError, ValueError, OSError, KeyError) as exc:
            errors.append(f"Official Modrinth fallback: {exc}")

    raise RuntimeError("Publisher download failed: " + " ; ".join(errors))


def jar_metadata(data: bytes) -> dict:
    with ZipFile(BytesIO(data)) as jar:
        bad = jar.testzip()
        if bad:
            raise ValueError(f"Bad JAR ZIP member: {bad}")
        candidates = ["META-INF/neoforge.mods.toml", "META-INF/mods.toml"]
        meta_path = next((p for p in candidates if p in jar.namelist()), None)
        if meta_path is None:
            return {"jar_metadata": "no standard NeoForge TOML; needs manual check"}
        meta = tomllib.loads(jar.read(meta_path).decode("utf-8-sig"))
        modids = [m.get("modId") for m in meta.get("mods", [])]
        deps = []
        for mod_id, dependencies in meta.get("dependencies", {}).items():
            for d in dependencies:
                if d.get("type") == "required" or d.get("mandatory") is True:
                    deps.append({
                        "owner": mod_id,
                        "mod_id": d.get("modId"),
                        "range": d.get("versionRange"),
                        "side": d.get("side"),
                    })
        return {
            "jar_metadata": meta_path,
            "mod_ids": modids,
            "required_dependencies": deps,
            "license": meta.get("license"),
            "mod_loader": meta.get("modLoader"),
        }


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--verify-official", action="store_true")
    parser.add_argument("--json-out", default="")
    args = parser.parse_args()

    report = {"source_checks": [], "official_artifacts": [], "failures": []}
    selected = set(CANDIDATES)
    for slug in CANDIDATES:
        path = ROOT / "mods" / (slug + ".pw.toml")
        try:
            pin = tomllib.loads(path.read_text(encoding="utf-8"))
            fmt = pin["download"]["hash-format"]
            expect = pin["download"]["hash"].lower()
            if fmt not in {"sha1", "sha512"}:
                raise ValueError(f"Unsupported hash: {fmt}")
            if not re.fullmatch(r"[0-9a-f]{40}" if fmt == "sha1" else r"[0-9a-f]{128}", expect):
                raise ValueError("Malformed declared official hash")
            if pin["side"] not in {"both", "client", "server"}:
                raise ValueError("Invalid side")
            src = source_urls(pin)
            report["source_checks"].append({"slug": slug, "side": pin["side"], "hash_format": fmt, "source": src[0]})
            if not args.verify_official:
                continue

            data, url = download(pin)
            got = hashlib.new(fmt, data).hexdigest()
            entry = {"slug": slug, "source": url, "bytes": len(data), "hash_algorithm": fmt,
                     "expected": expect, "actual": got, "matches": got == expect}
            if got != expect:
                report["official_artifacts"].append(entry)
                raise ValueError(f"Official JAR hash mismatch expected={expect} actual={got}")
            entry.update(jar_metadata(data))
            report["official_artifacts"].append(entry)
        except Exception as exc:
            report["failures"].append({"slug": slug, "error": str(exc)})

    if args.verify_official:
        found_ids = {m for e in report["official_artifacts"] for m in e.get("mod_ids", []) if m}
        for e in report["official_artifacts"]:
            for dep in e.get("required_dependencies", []):
                mod = dep["mod_id"]
                if mod and mod not in KNOWN_BASELINE_DEPS and mod not in found_ids:
                    report["failures"].append({"slug": e["slug"], "error":
                        f"Unreconciled required native dependency {mod} {dep.get('range')}; inspect baseline and required JAR"})
    if args.json_out:
        Path(args.json_out).write_text(json.dumps(report, indent=2) + "\n", encoding="utf-8")
    print(json.dumps(report, indent=2))
    if report["failures"]:
        print(f"BLOCKED: {len(report['failures'])} candidate verification failure(s)", file=sys.stderr)
        return 1
    print("PASS: candidate metadata valid" + ("; official JAR hashes / metadata verified" if args.verify_official else "; publisher downloads NOT performed"))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
