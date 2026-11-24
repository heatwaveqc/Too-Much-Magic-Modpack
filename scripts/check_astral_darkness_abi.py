#!/usr/bin/env python3
"""#264 exact released binary contract, static only; no game or pack boot."""
import argparse
import hashlib
import re
import subprocess
import tempfile
import tomllib
from pathlib import Path
from urllib.request import Request, urlopen
from zipfile import ZipFile

from check_as_mxp_perk_abi import download as download_astral

ROOT = Path(__file__).resolve().parents[1]
MD_HASH = "e08c8e75c249e2c74cc2f223bbd8c89b66270533d1835fc525e5223813009c6ccd406f5b81c0893fa315fdbf60ab2e379ff8fb0f22ffed84f5963776158773d1"
AS_HASH = "66f36655eae7cb404c9c16da6f33db31dff6ed9a"
MD_URL = "https://cdn.modrinth.com/data/ghnOnAJe/versions/6jywYHaX/more_darkness-neoforge-1.21.1-2.0.1%2B1.21.1.jar"
DARKNESS = "com.payangar.moredarkness.darkness."
RESEARCH = "hellfirepvp.astralsorcery.common.research."

def check(jar, algo, expected, classes):
    if hashlib.new(algo, jar.read_bytes()).hexdigest() != expected:
        raise ValueError("Pinned JAR hash mismatch: " + str(jar))
    for clazz, signatures in classes.items():
        out = subprocess.run(["javap", "-p", "-s", "-classpath", str(jar), clazz],
                             capture_output=True, text=True, check=True, timeout=35).stdout
        normal = re.sub(r"\s+", " ", out)
        for signature in signatures:
            if signature not in normal:
                raise ValueError("Pinned ABI changed: " + clazz + " " + signature)
        print("PASS", clazz)

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--astral", type=Path)
    parser.add_argument("--darkness", type=Path)
    args = parser.parse_args()
    for name, expected in [("astral-sorcery", AS_HASH), ("more-darkness", MD_HASH)]:
        pin = ROOT / "mods" / (name + ".pw.toml")
        # More Darkness is client-pack-owned, and may be absent from subsystem branches.
        if pin.exists() and tomllib.loads(pin.read_text())["download"]["hash"] != expected:
            raise ValueError("Pack pin changed: " + name)
    with tempfile.TemporaryDirectory(prefix="astral-darkness-abi-") as tmp:
        md = args.darkness or Path(tmp) / "more-darkness.jar"
        astral = args.astral or Path(tmp) / "astral.jar"
        if not args.darkness:
            with urlopen(Request(MD_URL, headers={"User-Agent":"TMM ABI verifier"}), timeout=45) as response:
                data = response.read(8 * 1024 * 1024 + 1)
            if len(data) > 8 * 1024 * 1024:
                raise ValueError("More Darkness download exceeds bound")
            md.write_bytes(data)
        if not args.astral:
            download_astral(astral, AS_HASH)
        check(md, "sha512", MD_HASH, {
            DARKNESS + "DarknessCalculator": ["public static int darken(int, int, int); descriptor: (III)I"],
            DARKNESS + "PerceptionEffects": [
                "public static void processFrame(net.minecraft.client.Minecraft, float); descriptor: (Lnet/minecraft/client/Minecraft;F)V",
                "public static void processDarkSight(net.minecraft.client.Minecraft, float); descriptor: (Lnet/minecraft/client/Minecraft;F)V"],
        })
        check(astral, "sha1", AS_HASH, {
            RESEARCH + "ResearchManager": ["public static " + RESEARCH + "PlayerProgress getProgress(net.minecraft.world.entity.player.Player, net.neoforged.fml.LogicalSide);"],
            RESEARCH + "PlayerProgress": ["public boolean isValid(); descriptor: ()Z", "public " + RESEARCH + "ResearchTier getTierReached();"],
            RESEARCH + "ResearchTier": ["public static final " + RESEARCH + "ResearchTier " + tier + ";" for tier in ("SHIMMER","ILLUMINATION","RESONANCE","LUMINANCE","RADIANCE")],
        })
        with ZipFile(md) as archive:
            metadata = tomllib.loads(archive.read("META-INF/neoforge.mods.toml").decode())
            if not any(mod["modId"] == "more_darkness" and mod["version"] == "2.0.1+1.21.1" for mod in metadata["mods"]):
                raise ValueError("More Darkness metadata mismatch")
            if not any(dep["modId"] == "yet_another_config_lib_v3" and dep["versionRange"] == "[3.6.2,)" for dep in metadata["dependencies"]["more_darkness"]):
                raise ValueError("YACL requirement changed")
        print("PASS exact pinned darkness and personal research ABI; no game boot")

if __name__ == "__main__":
    main()
