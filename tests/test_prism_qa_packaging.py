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
        for base in ("tmm_progression-",):
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
                commit, str(addons[0]),
            ], check=True)
            # The builder intentionally stages a DIRECTORY at this .zip-named
            # upload path. GitHub Actions wraps its contents in one ZIP. Previous
            # tests tried opening the staging directory as a ZIP and incorrectly
            # failed on Windows after we eliminated the nested ZIP.
            archive = folder / "Too-Much-Magic-Full-Integration-Prism.zip"
            assert archive.is_dir(), "GitHub upload must receive an instance directory"
            contents = sorted(p.relative_to(archive).as_posix()
                              for p in archive.rglob("*") if p.is_file())
            assert "instance.cfg" in contents and "mmc-pack.json" in contents
            assert not any(n.lower().endswith((".zip", ".mrpack"))
                           for n in contents), "No nested modpack archive allowed"
            github_download = folder / "github-artifact-download.zip"
            with zipfile.ZipFile(github_download, "w", zipfile.ZIP_DEFLATED) as out:
                for name in contents:
                    out.write(archive / name, name)
            # Assert the precise single-ZIP import shape players receive.
            with zipfile.ZipFile(github_download) as z:
                assert sorted(z.namelist()) == contents
                assert "instance.cfg" in z.namelist()
                assert "mmc-pack.json" in z.namelist()
                expected = {
                    "minecraft/options.txt",
                    "minecraft/tmm-qa-preferences.ps1",
                    "minecraft/IMPORT-OLD-TMM-PREFERENCES.cmd",
                }
                assert expected.issubset(z.namelist()), "QA settings missing from Prism import"
                opts = z.read("minecraft/options.txt").decode()
                assert opts.replace("\r\n", "\n").startswith("version:3955\n")
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
                assert sum(x.endswith(".jar") for x in z.namelist()) == 2
                assert not any(p.startswith("minecraft/saves/") for p in z.namelist())
                assert not any(p.startswith("minecraft/config/") for p in z.namelist())
            print("PASS: direct-import single GitHub ZIP root, QA-only seeded options, local sync hooks, immutable packwiz, one standalone first-party JAR and no personal data or configs")
        finally:
            if existing is None:
                target.unlink(missing_ok=True)
            else:
                target.write_bytes(existing)

if __name__ == "__main__":
    main()
