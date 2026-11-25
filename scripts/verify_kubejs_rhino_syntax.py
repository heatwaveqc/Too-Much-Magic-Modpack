#!/usr/bin/env python3
"""Check KubeJS source syntax using the *exact hash-pinned* Rhino parser, never JS execution.

Run locally using the installed Rhino JAR:
  python3 scripts/verify_kubejs_rhino_syntax.py --jar /path/to/rhino-2101.2.7-build.85.jar
Or permit only a pinned Modrinth download for source CI:
  python3 scripts/verify_kubejs_rhino_syntax.py --download
"""
from __future__ import annotations

import argparse
import hashlib
from pathlib import Path
import subprocess
import sys
import tempfile
import tomllib
from urllib.parse import urlparse
from urllib.request import urlopen

ROOT = Path(__file__).resolve().parents[1]
PIN = Path("mods/rhino.pw.toml")
JAVA_HELPER = Path("tests/ParseKubeJSSyntax.java")
FOLDERS = ("startup_scripts", "server_scripts", "client_scripts")
MAX_JAR_BYTES = 32 * 1024 * 1024


def pinned_rhino(root: Path) -> dict:
    pin = tomllib.loads((root / PIN).read_text(encoding="utf-8"))
    download = pin["download"]
    if pin["filename"] != "rhino-2101.2.7-build.85.jar":
        raise ValueError("unexpected Rhino version; revalidate parser and this pinned version contract")
    if download["hash-format"] != "sha512" or len(download["hash"]) != 128:
        raise ValueError("Rhino must have a full SHA-512 checksum")
    if not all(ch in "0123456789abcdef" for ch in download["hash"]):
        raise ValueError("malformed Rhino checksum")
    return pin


def assert_hash(jar: Path, expected: str) -> None:
    digest = hashlib.sha512()
    with jar.open("rb") as stream:
        for block in iter(lambda: stream.read(1024 * 1024), b""):
            digest.update(block)
    if digest.hexdigest() != expected:
        raise ValueError(f"Rhino JAR checksum mismatch: {jar.name}")


def download_pinned_jar(url: str, target: Path, expected: str) -> None:
    parsed = urlparse(url)
    if parsed.scheme != "https" or parsed.hostname != "cdn.modrinth.com":
        raise ValueError("Rhino download must be pinned to HTTPS Modrinth CDN")
    count = 0
    digest = hashlib.sha512()
    with urlopen(url, timeout=60) as response, target.open("wb") as output:
        while chunk := response.read(1024 * 1024):
            count += len(chunk)
            if count > MAX_JAR_BYTES:
                raise ValueError("Rhino download exceeded bounded size")
            digest.update(chunk)
            output.write(chunk)
    if digest.hexdigest() != expected:
        raise ValueError("downloaded Rhino JAR checksum differs from Packwiz pin")


def source_paths(root: Path) -> list[Path]:
    paths = []
    for directory in FOLDERS:
        scripts = root / "kubejs" / directory
        if scripts.is_dir():
            paths.extend(p for p in scripts.rglob("*.js") if p.is_file())
    return sorted(paths)


def run_parser(root: Path, jar: Path, java: str) -> int:
    sources = source_paths(root)
    if not sources:
        raise ValueError("No KubeJS sources found; failing closed")
    helper = root / JAVA_HELPER
    if not helper.is_file():
        raise ValueError("Missing Rhino parser source helper")
    command = [java, "-Xmx256m", "-cp", str(jar), str(helper), *(str(p) for p in sources)]
    print(f"Rhino 2101.2.7: syntax-only parsing {len(sources)} KubeJS scripts", flush=True)
    result = subprocess.run(command, cwd=root, check=False, timeout=120)
    if result.returncode != 0:
        print(f"Rhino syntax validation failed: exit {result.returncode}", file=sys.stderr)
    return result.returncode


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    origin = parser.add_mutually_exclusive_group(required=True)
    origin.add_argument("--jar", type=Path, help="Local pinned Rhino JAR")
    origin.add_argument("--download", action="store_true",
                        help="Download exact hash-pinned Rhino JAR to temporary directory")
    parser.add_argument("--root", type=Path, default=ROOT, help="Repository root")
    parser.add_argument("--java", default="java", help="Java 21 command")
    args = parser.parse_args()
    try:
        root = args.root.resolve()
        pin = pinned_rhino(root)
        expected = pin["download"]["hash"]
        if args.jar is not None:
            jar = args.jar.resolve()
            assert_hash(jar, expected)
            return run_parser(root, jar, args.java)
        with tempfile.TemporaryDirectory(prefix="tmm-rhino-parser-") as tmp:
            jar = Path(tmp) / pin["filename"]
            download_pinned_jar(pin["download"]["url"], jar, expected)
            return run_parser(root, jar, args.java)
    except (OSError, ValueError, subprocess.TimeoutExpired) as exc:
        print(f"PINNED RHINO SYNTAX CHECK FAIL: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
