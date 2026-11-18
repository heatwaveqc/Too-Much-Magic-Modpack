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

def validate_jar_license(directory, jar):
    """Verify the release license and notices, not just an SPDX metadata label."""
    with zipfile.ZipFile(jar) as z:
        metadata = tomllib.loads(z.read("META-INF/neoforge.mods.toml").decode())
        if metadata.get("license") != "MIT":
            raise ValueError("First-party JAR must declare MIT: " + jar.name)
        for name in ("LICENSE", "THIRD_PARTY_NOTICES.md"):
            if z.namelist().count(name) != 1:
                raise ValueError("Missing or duplicate " + name + " in " + jar.name)
            expected = (directory / name).read_bytes()
            if not expected or z.read(name) != expected:
                raise ValueError("Missing or inconsistent notice " + name + " in " + jar.name)
        license_text = z.read("LICENSE").decode()
        for required in ("MIT License", "Copyright (c)", "Permission is hereby granted",
                         "The above copyright notice", 'THE SOFTWARE IS PROVIDED "AS IS"',
                         "SOFTWARE."):
            if required not in license_text:
                raise ValueError("Incomplete MIT license in " + jar.name)

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
