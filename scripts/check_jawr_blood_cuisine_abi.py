#!/usr/bin/env python3
"""Pinned Type-A bytecode signature audit for #450; no game boot.

Downloads only the two exact signed Packwiz provider URLs into a temporary
directory, verifies complete SHA-512, then uses Java 21 javap to assert the
specific optional mixin and reflection target methods are present.
"""
from __future__ import annotations

import hashlib
import re
import subprocess
import sys
import tempfile
import tomllib
from pathlib import Path
from urllib.parse import urlsplit
from urllib.request import urlopen
from zipfile import ZipFile

ROOT = Path(__file__).resolve().parents[1]
MAX_BYTES = 128 * 1024 * 1024
PINS = {
    "witchery": "mods/just-another-witchery-remake.pw.toml",
    "brewinandchewin": "mods/brewin-and-chewin.pw.toml",
}
SIGNATURES = {
    "witchery": {
        "dev.sterner.witchery.feature.blood_pool.BloodPoolHandler": [
            "increaseBlood(", "net.minecraft.world.entity.LivingEntity", "int",
        ],
        "dev.sterner.witchery.feature.affliction.vampire.VampireEvents": [
            "isSolidFood(", "net.minecraft.world.item.ItemStack",
        ],
        "dev.sterner.witchery.core.registry.WitcheryDataComponents": [
            "getCHICKEN_BLOOD(", "getVAMPIRE_BLOOD(", "INSTANCE",
        ],
    },
    "brewinandchewin": {
        "umpaz.brewinandchewin.common.block.entity.AgingCaskBlockEntity": [
            "lastInputs", "canAge(", "age(", "getContainerItem(",
            "canPlaceItem(", "net.minecraft.world.item.ItemStack",
        ],
        "umpaz.brewinandchewin.common.item.WineItem": [
            "finishUsingItem(", "appendHoverText(", "net.minecraft.world.item.ItemStack",
        ],
    },
}


def download(pin_path: Path, dest: Path) -> None:
    pin = tomllib.loads(pin_path.read_text(encoding="utf-8"))
    url = pin["download"]["url"]
    parsed = urlsplit(url)
    if parsed.scheme != "https" or parsed.hostname != "cdn.modrinth.com":
        raise ValueError(pin_path.name + ": expected official HTTPS Modrinth CDN")
    if pin["download"]["hash-format"] != "sha512":
        raise ValueError(pin_path.name + ": expected SHA-512")
    expected = pin["download"]["hash"]
    if not re.fullmatch(r"[0-9a-f]{128}", expected):
        raise ValueError(pin_path.name + ": malformed SHA-512")
    digest = hashlib.sha512()
    count = 0
    with urlopen(url, timeout=60) as response, dest.open("wb") as output:
        for part in iter(lambda: response.read(1024 * 1024), b""):
            count += len(part)
            if count > MAX_BYTES:
                raise ValueError(pin_path.name + ": download exceeded maximum bytes")
            digest.update(part)
            output.write(part)
    if digest.hexdigest() != expected:
        raise ValueError(pin_path.name + ": downloaded bytes do not match Packwiz pin")


def class_text(jar: Path, clazz: str) -> str:
    # Fail if the exact class does not exist in the pinned binary.
    entry = clazz.replace(".", "/") + ".class"
    with ZipFile(jar) as archive:
        all_entries = archive.namelist()
        if entry not in all_entries:
            needles = ("AgingCask", "WineItem", "BnCWine", "brewinandchewin",
                       "META-INF/jarjar/", "META-INF/jars/")
            matches = [name for name in all_entries if any(needle in name for needle in needles)]
            raise ValueError(
                jar.name + ": missing class " + clazz
                + "; related ZIP entries (" + str(len(matches)) + "): "
                + repr(matches[:75])
                + "; archive starts: " + repr(all_entries[:20])
            )
    result = subprocess.run(["javap", "-p", "-s", "-classpath", str(jar), clazz],
                            capture_output=True, text=True, timeout=35, check=False)
    if result.returncode:
        raise ValueError(jar.name + ": javap failed for " + clazz + ": " + result.stderr[-900:])
    return result.stdout


def main() -> int:
    try:
        with tempfile.TemporaryDirectory(prefix="tmm-cuisine-abi-") as tmp:
            for mod_id, path in PINS.items():
                jar = Path(tmp) / (mod_id + ".jar")
                download(ROOT / path, jar)
                for clazz, expected in SIGNATURES[mod_id].items():
                    text = class_text(jar, clazz)
                    for token in expected:
                        if token not in text:
                            raise ValueError(mod_id + ": " + clazz + " missing " + token)
                print("PASS " + mod_id + ": SHA-512 + native bytecode signatures", flush=True)
    except (OSError, ValueError, KeyError, subprocess.TimeoutExpired) as exc:
        print("FAIL #450 pinned JAWR/Cask ABI: " + str(exc), file=sys.stderr)
        return 1
    print("TYPE A ONLY: exact methods exist; does not prove optional mixins inject or gameplay works")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
