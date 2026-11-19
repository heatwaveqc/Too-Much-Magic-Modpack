#!/usr/bin/env python3
"""Fetch only the reviewed Economy compile classpath from Packwiz locks.

No mod execution, install, full pack resolution, version changes, world, or distribution.
"""
from concurrent.futures import ThreadPoolExecutor
import hashlib
from pathlib import Path
import subprocess
import sys
import tomllib
from urllib.parse import quote, urlparse
import zipfile

ROOT = Path(__file__).resolve().parents[3]
SLUGS = """
projecte apotheosis placebo botania-revived evilcraft ars-nouveau draconic-evolution
brandons-core neovitae malum lodestonelib theurgy forbidden-arcanus valhelsia-core
embers-re-ignited goety primal-magick astral-sorcery pastel irons-spells-n-spellbooks
irons-lib project-expansion projecte-energy-converters apothic-spawners
apothic-attributes apothic-enchanting relics artifacts reliquary-reincarnations
jade titanium replication observerlib cyclops-core curios geckolib patchouli jei
architectury-api resourceful-lib resourceful-config caelus playeranimator
codechicken-lib-1-8 cloth-config puzzles-lib smartbrainlib bookshelf
""".split()
HOSTS = {"cdn.modrinth.com", "edge.forgecdn.net", "mediafilez.forgecdn.net"}

def read_lock():
    pack = tomllib.loads((ROOT / "pack.toml").read_text(encoding="utf-8"))
    index = ROOT / "index.toml"
    raw = index.read_bytes()
    if pack["index"]["hash-format"] != "sha256" or hashlib.sha256(raw).hexdigest() != pack["index"]["hash"]:
        raise ValueError("Packwiz index is not hash-locked")
    doc = tomllib.loads(raw.decode("utf-8"))
    rows = {r["file"]: r for r in doc["files"]}
    pins = []
    for slug in SLUGS:
        path = f"mods/{slug}.pw.toml"
        row = rows[path]
        raw = (ROOT / path).read_bytes()
        if not row.get("metafile") or hashlib.sha256(raw).hexdigest() != row["hash"]:
            raise ValueError(f"Invalid locked metafile: {path}")
        pins.append(tomllib.loads(raw.decode("utf-8")))
    if len({p["filename"] for p in pins}) != len(pins):
        raise ValueError("Duplicate classpath filenames")
    return pins

def fetch(pin, dest):
    name = pin["filename"]
    if Path(name).name != name or not name.endswith(".jar"):
        raise ValueError(f"Unsafe JAR filename: {name}")
    target = dest / name
    download = pin["download"]
    alg = download["hash-format"]
    if alg not in {"sha1", "sha256", "sha512"}:
        raise ValueError(f"Unsupported digest: {alg}")
    url = download.get("url")
    urls = [url] if url else []
    if not urls and download.get("mode") == "metadata:curseforge":
        fid = int(pin["update"]["curseforge"]["file-id"])
        tail = f"/files/{fid//1000}/{fid%1000:03d}/{quote(name)}"
        urls = ["https://" + host + tail for host in ("mediafilez.forgecdn.net", "edge.forgecdn.net")]
    for url in urls:
        parsed = urlparse(url)
        if parsed.scheme != "https" or parsed.hostname not in HOSTS:
            raise ValueError(f"Unreviewed origin for {name}")
        result = subprocess.run(["curl", "--fail", "--location", "--retry", "1",
                                 "--proto", "=https", "--proto-redir", "=https",
                                 "--connect-timeout", "20", "--max-time", "120",
                                 "--silent", "--show-error", "--output", str(target), url])
        if result.returncode:
            target.unlink(missing_ok=True)
            continue
        with target.open("rb") as f:
            digest = hashlib.file_digest(f, alg).hexdigest()
        if digest.lower() != download["hash"].lower() or not zipfile.is_zipfile(target):
            target.unlink(missing_ok=True)
            raise ValueError(f"Hash or JAR validation failed: {name}")
        print(f"PASS exact compile pin {name}: {alg} {digest}", flush=True)
        return
    raise ValueError(f"Cannot obtain reviewed compile pin: {name}")

def main():
    if len(sys.argv) != 2:
        raise SystemExit("usage: fetch_compile_classpath.py <fresh temporary directory>")
    dest = Path(sys.argv[1]).resolve()
    dest.mkdir(parents=True, exist_ok=False)
    pins = read_lock()
    with ThreadPoolExecutor(max_workers=4) as pool:
        list(pool.map(lambda pin: fetch(pin, dest), pins))
    print(f"PASS: {len(pins)} hash-verified Economy compile dependencies; no game execution.")

if __name__ == "__main__":
    main()
