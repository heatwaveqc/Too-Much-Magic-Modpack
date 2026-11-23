#!/usr/bin/env python3
"""Check four exact upstream CurseForge artifacts are directly Prism-downloadable.

Offline by default: verify Packwiz index/hash chain, host/file-ID/filename, retained
historical JAR SHA-1 and lack of metadata:curseforge mode. --download additionally
fetches those four originals from the same URLs Prism sees and checks each JAR's
actual bytes, SHA-1 and NeoForge/Forge metadata. No Minecraft launch or JAR commit.
"""
from __future__ import annotations
import argparse
import hashlib
import io
from pathlib import Path
import sys
import tomllib
import urllib.parse
import urllib.request
import zipfile

ROOT = Path(__file__).resolve().parents[1]
PINS = {
    "arcane-archives-reborn": (1696190, 8985321, "ArcaneArchives-1.0.2+Neoforge-1.21.1.jar", "082f5f560981c04c487b9c1f5e5ec9976f8ec2db"),
    "lava-fishing": (1035887, 7781402, "lavafishing-1.21.1-1.2.13-all.jar", "d3d7db677c6c4c669455de577bd258c791415df6"),
    "scarlet-springs-equestrian-decor-ssed": (1183801, 8100765, "ssedeco-1.21.1-1.2.1.jar", "37be09fb807ed5be6d1039ec3b98d729d78a8a10"),
    "unicorn-academy-bonded-magic-unofficial": (1741944, 9150781, "unicornacademy-1.0.0.jar", "b989b1dfec0d4c1360f52682adfa4f2196c38c50"),
}
MAX_JAR_BYTES = 32 * 1024 * 1024


def expected_url(file_id: int, filename: str) -> str:
    return (f"https://mediafilez.forgecdn.net/files/{file_id//1000}/{file_id%1000:03d}/"
            + urllib.parse.quote(filename, safe=""))


def check_offline(root: Path = ROOT) -> dict[str, tuple[str, str]]:
    pack = tomllib.loads((root / "pack.toml").read_text(encoding="utf-8"))
    index_bytes = (root / "index.toml").read_bytes()
    if hashlib.sha256(index_bytes).hexdigest() != pack["index"]["hash"]:
        raise ValueError("Packwiz pack.toml/index.toml hash mismatch")
    index = tomllib.loads(index_bytes.decode("utf-8"))
    files = {x["file"]: x for x in index["files"] if x.get("metafile")}
    out = {}
    for slug, (project, file_id, filename, expected_sha1) in PINS.items():
        rel = f"mods/{slug}.pw.toml"
        data = (root / rel).read_bytes()
        if rel not in files or hashlib.sha256(data).hexdigest() != files[rel]["hash"]:
            raise ValueError(f"{slug}: stale Packwiz SHA-256 index")
        pin = tomllib.loads(data.decode("utf-8"))
        url = expected_url(file_id, filename)
        if (pin["filename"] != filename or pin["side"] != "both" or
                pin["download"].get("url") != url or
                "mode" in pin["download"] or
                pin["download"].get("hash-format") != "sha1" or
                pin["download"].get("hash") != expected_sha1 or
                pin["update"]["curseforge"] != {"file-id": file_id, "project-id": project}):
            raise ValueError(f"{slug}: expected exact official immutable URL and original SHA-1")
        parsed = urllib.parse.urlsplit(url)
        if parsed.scheme != "https" or parsed.hostname != "mediafilez.forgecdn.net":
            raise ValueError(f"{slug}: untrusted download host")
        out[slug] = (url, expected_sha1)
        print(f"PASS offline {slug}: official direct URL, original SHA1, Packwiz SHA256 chain")
    return out


def check_downloads(checked: dict[str, tuple[str, str]]) -> None:
    for slug, (url, expected) in checked.items():
        request = urllib.request.Request(url, headers={"User-Agent": "TooMuchMagic-PrismQA/1.0"})
        with urllib.request.urlopen(request, timeout=90) as response:
            data = response.read(MAX_JAR_BYTES + 1)
            if len(data) > MAX_JAR_BYTES:
                raise ValueError(f"{slug}: unexpected oversized JAR")
            if response.geturl().startswith("http://"):
                raise ValueError(f"{slug}: unsafe HTTP redirect")
        actual = hashlib.sha1(data).hexdigest()
        if actual != expected:
            raise ValueError(f"{slug}: WRONG JAR SHA-1: {actual}")
        if not zipfile.is_zipfile(io.BytesIO(data)):
            raise ValueError(f"{slug}: downloaded bytes not a valid JAR")
        with zipfile.ZipFile(io.BytesIO(data)) as jar:
            if not ({"META-INF/neoforge.mods.toml", "META-INF/mods.toml"} & set(jar.namelist())):
                raise ValueError(f"{slug}: missing Minecraft mod metadata")
        print(f"PASS direct client URL {slug}: {len(data)} bytes; SHA1={actual}")


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--download", action="store_true", help="Resolve and SHA1-verify four pinned original upstream JARs")
    args = parser.parse_args()
    try:
        entries = check_offline()
        if args.download:
            check_downloads(entries)
        print(f"PASS: {len(entries)} former CurseForge-metadata installer blockers verified")
        return 0
    except (OSError, ValueError, KeyError, TypeError, zipfile.BadZipFile) as exc:
        print(f"QA FAIL: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
