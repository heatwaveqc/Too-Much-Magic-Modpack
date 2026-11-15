#!/usr/bin/env python3
"""Check EXACT pinned Vampirism/Werewolves native action ABI used by the pilot.

No upstream jars are checked in, redistributed or added to build artifacts.
If a mod updates or removes the required actions, fail CI instead of claiming
an action test works. Does not perform client gameplay tests.
"""
from __future__ import annotations

import hashlib
import pathlib
import re
import subprocess
import tempfile
import tomllib
import urllib.parse
import urllib.request
import zipfile

ROOT = pathlib.Path(__file__).resolve().parents[1]
PINS = (
    ("mods/vampirism-become-a-vampire.pw.toml", (
        ("de.teamlapen.vampirism.entity.player.vampire.VampirePlayer", "get(net.minecraft.world.entity.player.Player)"),
        ("de.teamlapen.vampirism.entity.player.vampire.actions.VampireActions", "HISSING"),
        ("de.teamlapen.vampirism.entity.player.vampire.actions.HissingAction", "getCooldown"),
        ("de.teamlapen.vampirism.api.entity.player.actions.IAction", "onActivated"),
        ("de.teamlapen.vampirism.api.entity.player.actions.DefaultAction", "canUse"),
    )),
    ("mods/werewolves-become-a-beast.pw.toml", (
        ("de.teamlapen.werewolves.entities.player.werewolf.WerewolfPlayer", "get(net.minecraft.world.entity.player.Player)"),
        ("de.teamlapen.werewolves.core.ModActions", "HOWLING"),
        ("de.teamlapen.werewolves.entities.player.werewolf.actions.HowlingAction", "getCooldown"),
    )),
)

def inspect(pin: str, classes: tuple[tuple[str, str], ...]) -> None:
    config = tomllib.loads((ROOT / pin).read_text())
    info = config["download"]
    if info.get("mode") != "metadata:curseforge":
        raise ValueError("Expected version-pinned CurseForge source")
    fid = config["update"]["curseforge"]["file-id"]
    name = config["filename"]
    url = f"https://mediafilez.forgecdn.net/files/{fid // 1000}/{fid % 1000:03d}/{urllib.parse.quote(name)}"
    request = urllib.request.Request(url, headers={"User-Agent": "TMM-Abomination-ABI-Audit/1.0"})
    with urllib.request.urlopen(request, timeout=120) as response:
        jar_bytes = response.read()
    expected = info["hash"].lower()
    algorithm = info["hash-format"]
    if algorithm not in ("sha1", "sha256", "sha512"):
        raise ValueError("Unknown hash algorithm")
    actual = hashlib.new(algorithm, jar_bytes).hexdigest()
    if actual != expected:
        raise ValueError(f"Hash mismatch for {pin}")
    print("HASH_VERIFIED", name, algorithm, actual, "bytes", len(jar_bytes), flush=True)
    with tempfile.TemporaryDirectory(prefix="tmm-abomination-abi-") as directory:
        jar = pathlib.Path(directory) / name
        jar.write_bytes(jar_bytes)
        with zipfile.ZipFile(jar) as z:
            names = set(z.namelist())
            for cls, needle in classes:
                path = cls.replace(".", "/") + ".class"
                if path not in names:
                    raise ValueError(f"Missing class in {name}: {cls}")
                p = subprocess.run(("javap", "-p", "-classpath", str(jar), cls),
                                   capture_output=True, text=True, timeout=30)
                if p.returncode or needle not in p.stdout:
                    raise ValueError(f"Missing required method/field {needle} in {cls}: {p.stderr[:200]}")
                print("ABI_VERIFIED", cls, needle, flush=True)

if __name__ == "__main__":
    for path, targets in PINS:
        inspect(path, targets)
    print("TWO_ACTION_ABI_PASS: static JAR signatures; NOT an in-world hybrid test")
