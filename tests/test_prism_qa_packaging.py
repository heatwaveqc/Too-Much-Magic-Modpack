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
    sys.path.insert(0, str(ROOT / 'scripts'))
    from first_party_runtime import selected, verify_packaging_inputs, verify_trader_irons_pin, validate_jar_license
    with tempfile.TemporaryDirectory(prefix="tmm-personal-qa-") as d:
        folder = pathlib.Path(d)
        bootstrap = folder / "bootstrap.jar"
        bootstrap.write_bytes(b"PK" + b"\x00" * (98989 - 2))
        addons = []
        for directory, mod, expected_path in selected():
            jar = folder / expected_path.name
            with zipfile.ZipFile(jar, "w") as z:
                z.writestr("META-INF/MANIFEST.MF", "Manifest-Version: 1.0\n")
                z.writestr("META-INF/neoforge.mods.toml", 'license = "MIT"\n[[mods]]\nmodId = "' + mod + '"\n')
                prefix = "META-INF/" if mod == "tmm_economy" else ""
                names = ("LICENSE", "THIRD_PARTY_NOTICES.md")
                if mod == "tmm_economy":
                    names += ("ASSET-CREDITS.md", "BOTANIA-ASSET-LICENSE.md")
                for name in names:
                    z.writestr(prefix + name, (directory / name).read_bytes())
            addons.append(jar)
        verify_packaging_inputs(addons)
        # Release license regression: accept exact bundled MIT files, reject
        # incorrect metadata, omitted files and mismatched notices.
        for directory, mod, expected_path in selected():
            j = folder / ("license-test-" + expected_path.name)
            for case in ("valid", "wrong_license", "missing_license", "wrong_notice"):
                with zipfile.ZipFile(j, "w") as z:
                    declared = "All Rights Reserved" if case == "wrong_license" else "MIT"
                    z.writestr("META-INF/neoforge.mods.toml", 'license = "' + declared + '"\n[[mods]]\nmodId = "' + mod + '"\n')
                    prefix = "META-INF/" if mod == "tmm_economy" else ""
                    if case != "missing_license":
                        z.writestr(prefix + "LICENSE", (directory / "LICENSE").read_bytes())
                    notice = b"incorrect" if case == "wrong_notice" else (directory / "THIRD_PARTY_NOTICES.md").read_bytes()
                    z.writestr(prefix + "THIRD_PARTY_NOTICES.md", notice)
                    if mod == "tmm_economy":
                        for name in ("ASSET-CREDITS.md", "BOTANIA-ASSET-LICENSE.md"):
                            z.writestr(prefix + name, (directory / name).read_bytes())
                try:
                    validate_jar_license(directory, j)
                except ValueError:
                    assert case != "valid", "Valid MIT license rejected"
                else:
                    assert case == "valid", "Invalid license accepted: " + case
        # Regression against the loader's actual native version, not its short
        # marketing version. Test the old failure, an exact pin and a future ABI.
        trader = folder / "trader-dependency-test.jar"
        for version, valid in (("[3.16.3,)", False), ("[1.21.1-3.16.3]", True), ("[1.21.1-3.16.4]", False)):
            with zipfile.ZipFile(trader, "w") as z:
                z.writestr("META-INF/neoforge.mods.toml", '[[mods]]\nmodId = "traders_vengence"\n'
                           '[[dependencies.traders_vengence]]\nmodId = "irons_spellbooks"\n'
                           'type = "required"\nordering = "AFTER"\nside = "BOTH"\n'
                           f'versionRange = "{version}"\n')
            try:
                verify_trader_irons_pin(trader)
            except ValueError:
                assert not valid, "Correct pinned native range rejected"
            else:
                assert valid, "Incorrect Iron's dependency accepted"
        for invalid in (addons[:-1], addons + [addons[0]]):
            try:
                verify_packaging_inputs(invalid)
            except ValueError:
                pass
            else:
                raise AssertionError('Missing or duplicate runtime JAR accepted')
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
                commit, *(str(jar) for jar in addons),
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
                    "minecraft/tmm-qa-install-verify.ps1",
                    "minecraft/tmm-qa-expected-mods.json",
                    "minecraft/tmm-qa-preferences.ps1",
                    "minecraft/IMPORT-OLD-TMM-PREFERENCES.cmd",
                }
                assert expected.issubset(z.namelist()), "QA settings missing from Prism import"
                opts = z.read("minecraft/options.txt").decode()
                assert opts.replace("\r\n", "\n").startswith("version:3955\n")
                assert opts.splitlines().count("onboardAccessibility:false") == 1
                assert [l for l in opts.splitlines() if l.startswith("tutorialStep:")] == ["tutorialStep:none"]
                assert not any(l.startswith("narrator:") for l in opts.splitlines()), "Never force narrator off"
                assert "guiScale:3" in opts
                assert "soundCategory_master:0.55" in opts
                assert not any(l.startswith("key_") for l in opts.splitlines())
                cfg = z.read("instance.cfg").decode()
                assert "PreLoadCommand=powershell.exe " in cfg
                assert "PostExitCommand=powershell.exe " in cfg
                assert 'PreLaunchCommand=powershell.exe ' in cfg and "tmm-qa-install-verify.ps1" in cfg
                from verify_qa_inventory import inventory, for_side
                expected_mods = json.loads(z.read("minecraft/tmm-qa-expected-mods.json"))
                assert expected_mods == {"schema": 1, "source_commit": commit, "mods": for_side(inventory(ROOT), "client", addons)}
                assert "raw.githubusercontent.com" in cfg and commit in cfg
                meta = json.loads(z.read("BUILD-MANIFEST.json"))
                assert meta["personal_settings"]["public_release"] is False
                assert meta["dev_only_preferences"] if "dev_only_preferences" in meta else True
                assert sum(x.endswith(".jar") for x in z.namelist()) == len(addons) + 1
                assert not any(p.startswith("minecraft/saves/") for p in z.namelist())
                assert not any(p.startswith("minecraft/config/") for p in z.namelist())
            print("PASS: direct-import single GitHub ZIP root, QA-only seeded options, local sync hooks, immutable packwiz, all selected first-party JARs and no personal data or configs")
        finally:
            if existing is None:
                target.unlink(missing_ok=True)
            else:
                target.write_bytes(existing)

if __name__ == "__main__":
    main()
