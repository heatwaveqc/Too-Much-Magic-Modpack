#!/usr/bin/env python3
"""Build an isolated Prism client ZIP with only our own bridge JAR.

All third-party magic JARs are still fetched by the existing trusted packwiz
bootstrap from their pinned hosts, NOT shipped in this ZIP.
"""
from __future__ import annotations
import hashlib
import pathlib
import sys
import tempfile
import zipfile

from build_b12_prism import main as build_b12

def main(bootstrap: str, output_dir: str, snapshot: str, our_jar: str) -> None:
    jar = pathlib.Path(our_jar)
    if not jar.is_file() or jar.suffix != ".jar" or not jar.name.startswith("tmm_transformations-"):
        raise RuntimeError("Not a recognized first-party bridge JAR")
    data = jar.read_bytes()
    if not data.startswith(b"PK"):
        raise RuntimeError("Bridge JAR is not a valid archive")
    out = pathlib.Path(output_dir)
    out.mkdir(parents=True, exist_ok=True)
    target = out / "Too-Much-Magic-Quest-Vampire-QA-Prism.zip"
    with tempfile.TemporaryDirectory(prefix="tmm-vampire-prism-") as folder:
        build_b12(bootstrap, folder, snapshot)
        source = pathlib.Path(folder) / "Too-Much-Magic-B12-QoL-Relics-Prism.zip"
        with zipfile.ZipFile(source) as src, zipfile.ZipFile(target, "w", zipfile.ZIP_DEFLATED) as dest:
            for name in src.namelist():
                value = src.read(name)
                if name == "instance.cfg":
                    value = value.replace(
                        b"Too Much Magic - B12 QoL and Relics Local QA",
                        b"Too Much Magic - Quest and Vampire QA - Test Worlds Only")
                dest.writestr(name, value)
            dest.writestr("minecraft/mods/" + jar.name, data)
            dest.writestr("VAMPIRE-CHASSIS-TEST.txt",
                "TOO MUCH MAGIC - FIXED QUESTS + EXPERIMENTAL VAMPIRE CHASSIS\n"
                "Do NOT use an existing save. Custom JAR is not in the packwiz lock.\n"
                "Both mod vampire states must be tested with experiment flags.\n"
                "All vampire features are OFF by default. Enable only after\n"
                "reading docs/VAMPIRE-CHASSIS-V1-TEST-PLAN.md in the GitHub repo.\n"
                "Use /tmmtransform vampire for read-only live metrics.\n"
                "Native acquisition and cure paths need in-world verification.\n"
                f"Snapshot: {snapshot}\n"
                f"Our bridge SHA256: {hashlib.sha256(data).hexdigest()}\n")
    with zipfile.ZipFile(target) as z:
        names = z.namelist()
        assert sum(n.startswith("minecraft/mods/") and n.endswith(".jar") for n in names) == 1
        assert z.read("minecraft/mods/" + jar.name) == data
        assert not any("optifine" in n.lower() or "third-party" in n.lower() for n in names)
    print("PASS: built isolated Prism vampire pilot", target,
          "bytes", target.stat().st_size,
          "sha256", hashlib.sha256(target.read_bytes()).hexdigest(), flush=True)

if __name__ == "__main__":
    if len(sys.argv) != 5:
        sys.exit("Usage: build_vampire_prism.py BOOTSTRAP_JAR OUTPUT_DIR PACKWIZ_SNAPSHOT_SHA OUR_BRIDGE_JAR")
    main(*sys.argv[1:])
