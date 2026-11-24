#!/usr/bin/env python3
"""Pinned Type-A bytecode signature audit for #450; no game boot.

Downloads only the two exact signed Packwiz provider URLs into a temporary
directory, verifies complete SHA-512, then uses Java 21 javap to assert the
native JAWR blood API and candidate sausage provider binary evidence.
"""
from __future__ import annotations

import hashlib
import json
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
        "umpaz.brewinandchewin.common.registry.BnCItems": [
            "public",
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
            needles = ("Aging", "Cask", "Wine", "Distill", "Ferment",
                       "META-INF/jarjar/", "META-INF/jars/")
            matches = [name for name in all_entries if any(needle in name for needle in needles)]
            raise ValueError(
                jar.name + ": missing class " + clazz
                + "; related ZIP entries (" + str(len(matches)) + "): "
                + repr(matches[:200])
                + "; archive starts: " + repr(all_entries[:20])
            )
    result = subprocess.run(["javap", "-p", "-s", "-classpath", str(jar), clazz],
                            capture_output=True, text=True, timeout=35, check=False)
    if result.returncode:
        raise ValueError(jar.name + ": javap failed for " + clazz + ": " + result.stderr[-900:])
    return result.stdout



# Exact integration roster pins observed 2026-11-24. The feature branch
# does not own mod pins; integrator must reconcile these at promotion.
SAUSAGE_PROVIDERS = {
    "pamhc2foodextended:sausageitem": (6221521, "pamhc2foodextended-NEOFORGE-1.21.1-1.0.0.jar", "0cf7f668fdedef93446f33215484913991185102"),
    "brewery:sausage": (9150302, "letsdo-brewery-neoforge-2.1.13.jar", "20e30e1a182fdfbfff3df91f43e068a16e04798e"),
    "mynethersdelight:roasted_sausage": (8854319, "MyNethersDelight-1.21.1-1.10.4.1.jar", "9c54439d65e77c7b4b711831138f81f3a849ff35"),
}


def check_sausage_candidates(directory: Path) -> None:
    from urllib.parse import quote
    tag = ROOT / "compat/progression/src/main/resources/data/c/tags/item/foods/sausages.json"
    data = json.loads(tag.read_text(encoding="utf-8"))
    expected = [{"id": item, "required": False} for item in SAUSAGE_PROVIDERS]
    if data != {"replace": False, "values": expected}:
        raise ValueError("Cooked sausage tag changed: reconcile exact provider evidence")
    for item, (file_id, filename, sha1) in SAUSAGE_PROVIDERS.items():
        namespace, name = item.split(":")
        url = f"https://mediafilez.forgecdn.net/files/{file_id // 1000}/{file_id % 1000:03d}/{quote(filename)}"
        jar = directory / filename
        digest = hashlib.sha1()
        count = 0
        with urlopen(url, timeout=60) as response, jar.open("wb") as output:
            for part in iter(lambda: response.read(1024 * 1024), b""):
                count += len(part)
                if count > MAX_BYTES:
                    raise ValueError(f"{item}: download exceeded maximum bytes")
                digest.update(part)
                output.write(part)
        if digest.hexdigest() != sha1:
            raise ValueError(f"{item}: publisher bytes do not match frozen Packwiz SHA-1")
        with ZipFile(jar) as archive:
            names = archive.namelist()
            models = [p for p in names if p == f"assets/{namespace}/models/item/{name}.json"]
            recipes = []
            for p in names:
                if not p.startswith(f"data/{namespace}/") or "/recipe" not in p or not p.endswith(".json"):
                    continue
                raw = archive.read(p)
                if item.encode() in raw:
                    recipes.append(p)
                    print(f"REVIEW {item} recipe {p}: {raw.decode('utf-8')[:3000]}", flush=True)
            if not models and not recipes:
                raise ValueError(f"{item}: no exact model or recipe reference found in pinned JAR")
            print(f"PASS {item}: exact SHA-1 + model/recipe evidence; review cooking and FOOD properties separately", flush=True)


def main() -> int:
    try:
        with tempfile.TemporaryDirectory(prefix="tmm-cuisine-abi-") as tmp:
            check_sausage_candidates(Path(tmp))
            for mod_id, path in PINS.items():
                jar = Path(tmp) / (mod_id + ".jar")
                download(ROOT / path, jar)
                for clazz, expected in SIGNATURES[mod_id].items():
                    text = class_text(jar, clazz)
                    if mod_id == "witchery" and clazz.endswith(".VampireEvents"):
                        for line in text.splitlines():
                            if "isSolidFood(" in line:
                                print("PINNED JAWR vampire predicate: " + line.strip(), flush=True)
                    if mod_id == "brewinandchewin" and clazz.endswith(".BnCItems"):
                        present = "COOKED_SAUSAGE" in text
                        print("BNc 4.5.0 native cooked sausage present: " + str(present), flush=True)
                        if present:
                            print("WARNING: native sausage newly available; review #450 recipe admission", flush=True)
                        with ZipFile(jar) as archive:
                            names = archive.namelist()
                            has_cask = any(path.endswith("/AgingCaskBlockEntity.class") for path in names)
                            has_wine = any(path.endswith("/WineItem.class") for path in names)
                            print("BNc native Aging Cask: " + str(has_cask)
                                  + "; WineItem: " + str(has_wine), flush=True)
                    for token in expected:
                        if token not in text:
                            raise ValueError(mod_id + ": " + clazz + " missing " + token)
                print("PASS " + mod_id + ": SHA-512 + native bytecode signatures", flush=True)
    except (OSError, ValueError, KeyError, subprocess.TimeoutExpired) as exc:
        print("FAIL #450 pinned JAWR/food ABI: " + str(exc), file=sys.stderr)
        return 1
    print("TYPE A native JAWR blood + BnC item registry verified. Bloody Sausage candidate binary evidence collected; food properties and runtime tag review remain. Wine deferred; no gameplay PASS.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
