#!/usr/bin/env python3
"""Offline regression for QA-only options packaging.

Uses dummy first-party JARs and bootstrap only; never distributes them.
Actual downloader/server/client testing remains in the full workflow.
"""
from __future__ import annotations
import json
import pathlib
import subprocess
import sys
import tempfile
import zipfile

ROOT = pathlib.Path(__file__).resolve().parents[1]
BUILDER = ROOT / "scripts/build_full_integration_prism.py"

def main() -> None:
    # Do not let personal files migrate into packwiz or a normal client builder.
    index = (ROOT / "index.toml").read_text(encoding="utf-8")
    for name in ("options.txt", "tmm-qa-preferences.ps1", "IMPORT-OLD-TMM-PREFERENCES.cmd"):
        assert name not in index, "Personal QA preferences must not be packwiz indexed"
    with tempfile.TemporaryDirectory(prefix="tmm-personal-qa-") as d:
        folder = pathlib.Path(d)
        bootstrap = folder / "bootstrap.jar"
        bootstrap.write_bytes(b"PK" + b"\x00" * (98989 - 2))
        addons = []
        for base in ("tmm_monk-", "tmm_transformations-"):
            jar = folder / (base + "0.1.0-dev.jar")
            with zipfile.ZipFile(jar, "w") as z:
                z.writestr("META-INF/MANIFEST.MF", "Manifest-Version: 1.0\n")
            addons.append(jar)
        target = ROOT / "manifest/integration-lock.json"
        existing = target.read_bytes() if target.exists() else None
        try:
            target.write_text(json.dumps({
                "schema": 1, "channel": "INTEGRATION-NOT-STABLE",
                "workstreams": [], "pending_runtime_work": [],
            }), encoding="utf-8")
            commit = "a" * 40
            subprocess.run([
                sys.executable, str(BUILDER), str(bootstrap), str(folder),
                commit, str(addons[0]), str(addons[1]),
            ], check=True)
            archive = folder / "Too-Much-Magic-Full-Integration-Prism.zip"
            with zipfile.ZipFile(archive) as z:
                expected = {
                    "minecraft/options.txt",
                    "minecraft/tmm-qa-preferences.ps1",
                    "minecraft/IMPORT-OLD-TMM-PREFERENCES.cmd",
                }
                assert expected.issubset(z.namelist()), "QA settings missing from Prism import"
                opts = z.read("minecraft/options.txt").decode()
                assert opts.startswith("version:3955\n")
                assert "guiScale:3" in opts
                assert "soundCategory_master:0.55" in opts
                assert not any(l.startswith("key_") for l in opts.splitlines())
                cfg = z.read("instance.cfg").decode()
                assert "PreLoadCommand=powershell.exe " in cfg
                assert "PostExitCommand=powershell.exe " in cfg
                assert 'PreLaunchCommand="$INST_JAVA" -jar packwiz-installer-bootstrap.jar' in cfg
                assert "raw.githubusercontent.com" in cfg and commit in cfg
                meta = json.loads(z.read("BUILD-MANIFEST.json"))
                assert meta["personal_settings"]["public_release"] is False
                assert meta["dev_only_preferences"] if "dev_only_preferences" in meta else True
                assert sum(x.endswith(".jar") for x in z.namelist()) == 3
                assert not any(p.startswith("minecraft/saves/") for p in z.namelist())
                assert not any(p.startswith("minecraft/config/") for p in z.namelist())
            print("PASS: QA-only seeded options, local sync hooks, immutable packwiz, two first-party JARs and no personal data or configs")
        finally:
            if existing is None:
                target.unlink(missing_ok=True)
            else:
                target.write_bytes(existing)

if __name__ == "__main__":
    main()
