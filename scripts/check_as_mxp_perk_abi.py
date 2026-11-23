#!/usr/bin/env python3
"""#474: exact pinned Astral 2.0.3.59 SHA-1 plus declared native ABI.

Type-A ONLY: no modded Minecraft boot. For manual use:
  python3 scripts/check_as_mxp_perk_abi.py --jar /path/to/verified.jar
Default invocation downloads the exact CurseForge file 9281073 CDN artifact,
then verifies the Packwiz SHA-1 before inspecting bytecode with Java 21 javap.
"""
from __future__ import annotations

import argparse
import hashlib
import re
import subprocess
import sys
import tempfile
import tomllib
from pathlib import Path
from urllib.request import Request, urlopen
from zipfile import ZipFile

ROOT = Path(__file__).resolve().parents[1]
PIN = ROOT / "mods" / "astral-sorcery.pw.toml"
PUBLISHER_URLS = (
    "https://mediafilez.forgecdn.net/files/9281/073/AstralSorcery-2.0.3.59.jar",
    "https://edge.forgecdn.net/files/9281/073/AstralSorcery-2.0.3.59.jar",
    "https://www.cursemaven.com/curse/maven/astral-sorcery-241721/9281073/astral-sorcery-241721-9281073.jar",
)
MAX_DOWNLOAD = 80 * 1024 * 1024
METHODS = {
    "hellfirepvp.astralsorcery.common.research.ResearchHelper": [
        "public static boolean givePerkPointToken(net.minecraft.server.level.ServerPlayer, net.minecraft.resources.ResourceLocation);",
        "public static boolean removePerkPointToken(net.minecraft.server.level.ServerPlayer, net.minecraft.resources.ResourceLocation);",
    ],
    "hellfirepvp.astralsorcery.common.research.ResearchManager": [
        "getProgress(net.minecraft.world.entity.player.Player, net.neoforged.fml.LogicalSide);",
    ],
    "hellfirepvp.astralsorcery.common.research.PlayerProgress": [
        "public boolean isValid();",
        "public java.util.Optional<hellfirepvp.astralsorcery.common.constellation.BaseConstellation> getAttunedConstellation();",
        "public hellfirepvp.astralsorcery.common.research.PlayerPerkData getPerkData();",
    ],
    "hellfirepvp.astralsorcery.common.research.PlayerPerkData": [
        "public java.util.Set<net.minecraft.resources.ResourceLocation> getPointTokens();",
    ],
}


def verify_sha1(jar: Path, expected: str) -> None:
    digest = hashlib.sha1()
    with jar.open("rb") as stream:
        for chunk in iter(lambda: stream.read(1024 * 1024), b""):
            digest.update(chunk)
    if digest.hexdigest() != expected:
        raise ValueError("Astral JAR does not match frozen pack SHA1 " + expected)


def download(path: Path, expected: str) -> None:
    failures = []
    for url in PUBLISHER_URLS:
        try:
            request = Request(url, headers={"User-Agent": "Mozilla/5.0 (TMM pinned source ABI verifier)"})
            total = 0
            with urlopen(request, timeout=45) as response, path.open("wb") as output:
                for data in iter(lambda: response.read(1024 * 1024), b""):
                    total += len(data)
                    if total > MAX_DOWNLOAD:
                        raise ValueError("Astral download exceeded 80 MiB")
                    output.write(data)
            verify_sha1(path, expected)
            print("PASS exact publisher bytes from " + url.split("/")[2], flush=True)
            return
        except Exception as bad:
            failures.append(url.split("/")[2] + ": " + str(bad))
            path.unlink(missing_ok=True)
    raise ValueError("All pinned publisher mirrors failed: " + " | ".join(failures))


def inspect(jar: Path) -> None:
    with ZipFile(jar) as archive:
        names = set(archive.namelist())
        for clazz in METHODS:
            target = clazz.replace(".", "/") + ".class"
            if target not in names:
                raise ValueError("Missing native class " + clazz)
        meta = next((p for p in ("META-INF/neoforge.mods.toml", "META-INF/mods.toml")
                     if p in names), None)
        if meta is None or 'modId="astralsorcery"' not in archive.read(meta).decode("utf-8"):
            raise ValueError("Native Astral modId cannot be confirmed")
    for clazz, expected in METHODS.items():
        result = subprocess.run(
            ["javap", "-p", "-s", "-classpath", str(jar), clazz],
            capture_output=True, text=True, timeout=35, check=False)
        if result.returncode:
            raise ValueError("javap cannot open " + clazz + ": " + result.stderr[-350:])
        normal = re.sub(r"\s+", " ", result.stdout)
        for signature in expected:
            if re.sub(r"\s+", " ", signature) not in normal:
                raise ValueError("Mismatch in pinned " + clazz + ": " + signature)
    print("PASS exact SHA1 / native Astral token grant, remove, attunement and token set ABI")
    print("Type A only. Runtime client sync, respec and wipe still require authorized B/C QA.")


def main() -> int:
    args = argparse.ArgumentParser()
    args.add_argument("--jar", type=Path, help="Check an already available local JAR")
    opt = args.parse_args()
    pin = tomllib.loads(PIN.read_text(encoding="utf-8"))
    if pin["download"]["hash-format"] != "sha1" or pin["filename"] != "AstralSorcery-2.0.3.59.jar":
        raise ValueError("Exact pack Astral pin changed; refresh this ABI contract first")
    expected = pin["download"]["hash"]
    if not re.fullmatch(r"[0-9a-f]{40}", expected):
        raise ValueError("Malformed Astral pinned SHA1")
    try:
        if opt.jar:
            verify_sha1(opt.jar, expected)
            inspect(opt.jar)
        else:
            with tempfile.TemporaryDirectory(prefix="astral-mxp-pin-") as temp:
                jar = Path(temp) / pin["filename"]
                download(jar, expected)
                inspect(jar)
    except Exception as exc:
        print("FAIL pinned Astral perk API: " + str(exc), file=sys.stderr)
        return 1
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
