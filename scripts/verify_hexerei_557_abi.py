#!/usr/bin/env python3
"""Offline class/descriptor audit after downloading the exact Packwiz-pinned JAR.

Read-only quick Type A check. Never launch Minecraft, write to packwiz or
commit downloaded mod binaries. This verifies class presence and JVM ABI,
not runtime Mixin injection success.
"""

import hashlib
import re
import subprocess
import sys
import tempfile
import tomllib
import urllib.request
from pathlib import Path
from zipfile import ZipFile

ROOT = Path(__file__).resolve().parents[1]
PIN = ROOT / "mods" / "hexerei.pw.toml"
CLASS = "net.joefoxe.hexerei.tileentity.MixingCauldronTile"
CLASS_FILE = CLASS.replace(".", "/") + ".class"
EXPECTED = {
    "fill": "(Lnet/neoforged/neoforge/fluids/FluidStack;"
            "Lnet/neoforged/neoforge/fluids/capability/IFluidHandler$FluidAction;)I",
    "interactWithFluid": "(Lnet/neoforged/neoforge/fluids/capability/IFluidHandlerItem;)Z",
}


def method_section(text: str, name: str) -> str:
    # javap -p -s / -p -c prints member declarations at two-space indent
    decl = re.compile(
        r"(?m)^  (?:public|private|protected)[^\n]*\b"
        + re.escape(name) + r"\([^\n]*\);\s*$"
    )
    found = decl.search(text)
    if not found:
        raise AssertionError(f"Missing pinned Hexerei method: {name}")
    next_member = re.search(
        r"(?m)^  (?:public|private|protected)[^\n]*[;{]\s*$",
        text[found.end():]
    )
    return text[found.end(): found.end() + next_member.start()] if next_member else text[found.end():]


def main():
    data = tomllib.loads(PIN.read_text(encoding="utf-8"))
    assert data["filename"] == "hexerei-0.5.0.3.jar", (
        "Hexerei pin changed: review mixin targets for the new version before applying #557"
    )
    pin = data["download"]
    assert pin["hash-format"] == "sha512", "Expected hash-verified pinned Hexerei binary"
    with tempfile.TemporaryDirectory(prefix="tmm-hexerei-abi-") as tmp:
        artifact = Path(tmp) / data["filename"]
        req = urllib.request.Request(
            pin["url"], headers={"User-Agent": "Too-Much-Magic source ABI check"}
        )
        with urllib.request.urlopen(req, timeout=90) as response:
            with artifact.open("wb") as out:
                while chunk := response.read(1024 * 1024):
                    out.write(chunk)
        digest = hashlib.sha512(artifact.read_bytes()).hexdigest()
        assert digest == pin["hash"], "Pinned Hexerei JAR SHA-512 mismatch"
        with ZipFile(artifact) as archive:
            assert CLASS_FILE in archive.namelist(), f"Missing {CLASS_FILE}"

        def javap(*flags: str) -> str:
            return subprocess.run(
                ["javap", "-classpath", str(artifact), "-p", *flags, CLASS],
                capture_output=True, text=True, timeout=40, check=True
            ).stdout

        descriptors = javap("-s")
        for method, expected in EXPECTED.items():
            section = method_section(descriptors, method)
            assert f"descriptor: {expected}" in section, (
                f"Hexerei {method} descriptor drift: expected {expected}, got {section[:300]}"
            )
        assert re.search(
            r"(?m)^  private .*\bfluidStack;\s*\n\s*descriptor: Lnet/neoforged/neoforge/fluids/FluidStack;",
            descriptors
        ), "Internal field changed; inspect native item direct assignment"

        code = javap("-c")
        direct = method_section(code, "interactWithFluid")
        assert re.search(r"putfield\s+#[0-9]+\s+// Field fluidStack:", direct), (
            "Native item flow changed; direct-bypass mixin must be re-evaluated"
        )
        print("PASS pinned Hexerei 0.5.0.3 SHA-512, fill/held-item JVM ABI, native direct tank write")


if __name__ == "__main__":
    try:
        main()
    except Exception as exc:
        print(f"FAIL Hexerei #557 pinned-JAR ABI audit: {exc}", file=sys.stderr)
        raise SystemExit(1) from exc
