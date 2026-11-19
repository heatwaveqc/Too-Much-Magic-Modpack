#!/usr/bin/env python3
"""Build and verify the explicitly selected first-party modules, never arbitrary JARs."""
from __future__ import annotations
import argparse
import hashlib
import json
import os
from pathlib import Path
import re
import subprocess
import tomllib
import tempfile
import sys
import zipfile

ROOT = Path(__file__).resolve().parents[1]
MANIFEST = ROOT / "manifest/first-party-runtime.json"

def selected(root=ROOT):
    data = json.loads((root / "manifest/first-party-runtime.json").read_text())
    if data.get("schema") != 1 or not data.get("modules"):
        raise ValueError("Missing runtime module selection")
    ids, dirs, result = set(), set(), []
    for row in data["modules"]:
        d, mod = row["directory"], row["mod_id"]
        if not re.fullmatch(r"compat/[a-z0-9-]+", d) or not re.fullmatch(r"[a-z][a-z0-9_]+", mod):
            raise ValueError("Unsafe module path or mod ID")
        if mod in ids or d in dirs or mod == "tmm_monk":
            raise ValueError("Duplicate or retired module")
        ids.add(mod); dirs.add(d)
        directory = root / d
        props = dict(line.split("=", 1) for line in (directory / "gradle.properties").read_text().splitlines()
                     if "=" in line and not line.startswith("#"))
        if props.get("mod_id") != mod or not re.fullmatch(r"[a-zA-Z0-9._-]+", props.get("mod_version", "")):
            raise ValueError("Unexpected module metadata: " + d)
        jar = directory / "build/libs" / (mod + "-" + props["mod_version"] + ".jar")
        result.append((directory, mod, jar))
    return result

def validate_jar(jar, mod):
    if not jar.is_file():
        raise ValueError("Missing selected runtime JAR: " + str(jar))
    with zipfile.ZipFile(jar) as z:
        metadata = tomllib.loads(z.read("META-INF/neoforge.mods.toml").decode())
        if [m["modId"] for m in metadata.get("mods", [])] != [mod]:
            raise ValueError("Wrong or duplicate declared mod IDs in " + jar.name)

def expected_license(directory):
    return "GPL-3.0-only" if directory.name == "traders-vengence" else "MIT"

CALIBERARMS_MANIFEST = "META-INF/traders_vengence/caliberarms/provenance.json"
CALIBERARMS_JAR_SHA256 = "88068f10a62af5af35b2e8256137cb5160ea5eb2cc8f42644a0fa42722931f90"

def caliberarms_asset_paths(directory):
    """The donor model, audio and separate MIT/CC0 notices must ship together."""
    root = directory / "src/main/resources"
    manifest = json.loads((root / CALIBERARMS_MANIFEST).read_text(encoding="utf-8"))
    expected_paths = {
        "META-INF/traders_vengence/caliberarms/LICENSE.txt",
        "META-INF/traders_vengence/caliberarms/CREDITS.md",
        "assets/traders_vengence/models/item/npc_pistol.json",
        "assets/traders_vengence/textures/item/gun/m1911.png",
        "assets/traders_vengence/sounds.json",
        *(f"assets/traders_vengence/sounds/guns/m1911/fire_{i}.ogg" for i in (1, 2)),
        *(f"assets/traders_vengence/sounds/reload/pistol/{action}_{i}.ogg"
          for action, count in (("mag_in", 2), ("mag_out", 2), ("slide", 4))
          for i in range(1, count + 1)),
    }
    if manifest.get("jar_sha256") != CALIBERARMS_JAR_SHA256 or set(manifest.get("files", {})) != expected_paths:
        raise ValueError("Unreviewed Caliber Arms donor manifest")
    for path, expected_hash in manifest["files"].items():
        if hashlib.sha256((root / path).read_bytes()).hexdigest() != expected_hash:
            raise ValueError("Caliber Arms source asset hash mismatch: " + path)
    return sorted(expected_paths | {CALIBERARMS_MANIFEST})

def validate_caliberarms_assets(directory, z):
    root = directory / "src/main/resources"
    for name in caliberarms_asset_paths(directory):
        if z.namelist().count(name) != 1 or z.read(name) != (root / name).read_bytes():
            raise ValueError("Missing, duplicate or inconsistent Caliber Arms resource: " + name)

MINIGUN_AUDIO_BLOBS = {
    "assets/traders_vengence/sounds/minigun/minigun.ogg": "20b0041019891539b9df3473df7ab1c40ca93ec1",
    "assets/traders_vengence/sounds/minigun/minigun_stop.ogg": "b81e9252251729ff50736258e07974b25afaf019",
    "assets/traders_vengence/sounds/minigun/minigun_spinup.ogg": "aebb1dd5ffc561193f57e4ccfa0b07933659b3ed",
}

def validate_minigun_audio(directory, z):
    for name, expected in MINIGUN_AUDIO_BLOBS.items():
        data = (directory / "src/main/resources" / name).read_bytes()
        blob = hashlib.sha1(f"blob {len(data)}".encode() + bytes(1) + data).hexdigest()
        if blob != expected or z.namelist().count(name) != 1 or z.read(name) != data:
            raise ValueError("Missing or modified audited minigun recording: " + name)
    if any(name.endswith("/hud_init.ogg") for name in z.namelist()):
        raise ValueError("Uncleared third-party HUD audio must not be packaged")

def validate_jar_license(directory, jar):
    """Require the approved module-specific license and exact bundled notices."""
    expected_id = expected_license(directory)
    props = dict(line.split("=", 1) for line in (directory / "gradle.properties").read_text().splitlines()
                 if "=" in line and not line.startswith("#"))
    if props.get("mod_license") != expected_id:
        raise ValueError("Unexpected module license: " + str(directory))
    with zipfile.ZipFile(jar) as z:
        metadata = tomllib.loads(z.read("META-INF/neoforge.mods.toml").decode())
        if metadata.get("license") != expected_id:
            raise ValueError("First-party JAR must declare " + expected_id + ": " + jar.name)
        for name in ("LICENSE", "THIRD_PARTY_NOTICES.md"):
            if z.namelist().count(name) != 1:
                raise ValueError("Missing or duplicate " + name + " in " + jar.name)
            expected = (directory / name).read_bytes()
            if not expected or z.read(name) != expected:
                raise ValueError("Missing or inconsistent notice " + name + " in " + jar.name)
        license_text = z.read("LICENSE").decode()
        markers = (("MIT License", "Copyright (c)", "Permission is hereby granted",
                    "The above copyright notice", 'THE SOFTWARE IS PROVIDED "AS IS"', "SOFTWARE.")
                   if expected_id == "MIT" else
                   ("GNU GENERAL PUBLIC LICENSE", "Version 3", "TERMS AND CONDITIONS",
                    "Corresponding Source", "END OF TERMS AND CONDITIONS"))
        for required in markers:
            if required not in license_text:
                raise ValueError("Incomplete " + expected_id + " license in " + jar.name)
        if directory.name == "traders-vengence":
            validate_caliberarms_assets(directory, z)
            validate_minigun_audio(directory, z)

def verify_trader_irons_pin(jar, root=ROOT):
    """Fail before server boot if packaged Trader dependency disagrees with the exact Iron's pin.

    The native loader reports 1.21.1-3.16.3, NOT the marketing version 3.16.3.
    An intentional Iron's upgrade must review the native ABI and update the exact range.
    """
    pin = tomllib.loads((root / "mods/irons-spells-n-spellbooks.pw.toml").read_text())
    match = re.fullmatch(r"irons_spellbooks-(1[.]21[.]1-[0-9]+[.][0-9]+[.][0-9]+)[.]jar", pin["filename"])
    if not match:
        raise ValueError("Unreviewed Iron's pinned filename: " + pin["filename"])
    expected = "[" + match.group(1) + "]"
    with zipfile.ZipFile(jar) as z:
        metadata = tomllib.loads(z.read("META-INF/neoforge.mods.toml").decode())
    rows = [d for d in metadata.get("dependencies", {}).get("traders_vengence", [])
            if d.get("modId") == "irons_spellbooks"]
    if len(rows) != 1 or rows[0].get("versionRange") != expected or rows[0].get("type") != "required" or rows[0].get("ordering") != "AFTER" or rows[0].get("side") != "BOTH":
        raise ValueError("Trader's Vengence Iron's dependency must be required, BOTH, AFTER, exact native pinned range " + expected)
    print("PASS: compiled Trader's Vengence requires exact native Iron's version " + expected)

def verify_packaging_inputs(addons, root=ROOT):
    expected = {jar.name: (directory, mod) for directory, mod, jar in selected(root)}
    if len(addons) != len(expected) or {p.name for p in addons} != set(expected):
        raise ValueError("Missing, extra or duplicate first-party runtime JAR selection")
    for jar in addons:
        directory, mod = expected[jar.name]
        validate_jar(jar, mod)
        validate_jar_license(directory, jar)

def main():
    p = argparse.ArgumentParser()
    p.add_argument("action", choices=["build", "paths", "verify-smoke"])
    p.add_argument("--mods-dir", type=Path)
    args = p.parse_args()
    rows = selected()
    if args.action == "build":
        for directory, mod, jar in rows:
            if mod == "tmm_economy":
                with tempfile.TemporaryDirectory(prefix="tmm-economy-classpath-") as temp:
                    mods = Path(temp) / "verified-mods"
                    subprocess.run([sys.executable, str(directory / "tools/fetch_compile_classpath.py"), str(mods)], cwd=ROOT, check=True)
                    subprocess.run(["gradle", "--no-daemon", f"-PtmmCompileModsDir={mods}", "clean", "build"], cwd=directory, check=True)
            else:
                subprocess.run(["gradle", "--no-daemon", "clean", "build"], cwd=directory, check=True)
            validate_jar(jar, mod)
            validate_jar_license(directory, jar)
            if mod == "traders_vengence":
                verify_trader_irons_pin(jar)
    elif args.action == "paths":
        paths = [jar for _, _, jar in rows]
        verify_packaging_inputs(paths)
        print(os.pathsep.join(str(p) for p in paths))
    else:
        if args.mods_dir is None:
            p.error("--mods-dir required")
        expected = {jar.name for _, _, jar in rows}
        actual = {j.name for j in args.mods_dir.glob("*.jar") if j.name.startswith("tmm_") or j.name.startswith("traders_vengence-") or j.name in expected}
        if actual != expected:
            raise ValueError(f"Smoke custom JAR mismatch: missing={expected-actual}, extra={actual-expected}")
        for directory, mod, jar in rows:
            dest = args.mods_dir / jar.name
            validate_jar(dest, mod)
            validate_jar_license(directory, dest)
            if hashlib.sha256(jar.read_bytes()).digest() != hashlib.sha256(dest.read_bytes()).digest():
                raise ValueError("Smoke JAR bytes differ: " + jar.name)
        print("Verified exact selected runtime JARs: " + ", ".join(sorted(expected)))

if __name__ == "__main__":
    main()
