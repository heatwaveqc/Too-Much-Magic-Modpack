#!/usr/bin/env python3
"""Produce an immutable, traceable, disposable full-integration Prism instance."""
from __future__ import annotations
import hashlib
import json
import pathlib
import re
import sys
import zipfile

REPO = "heatwaveqc/Too-Much-Magic-Modpack"
OUTPUT = "Too-Much-Magic-Full-Integration-Prism.zip"

def main(bootstrap: str, output_dir: str, commit: str, monk_jar: str, bridge_jar: str) -> None:
    if not re.fullmatch(r"[0-9a-f]{40}", commit):
        raise RuntimeError("Packwiz source must be an immutable commit")
    root = pathlib.Path(__file__).resolve().parents[1]
    raw = pathlib.Path(bootstrap).read_bytes()
    if len(raw) != 98989 or not raw.startswith(b"PK"):
        raise RuntimeError("Unrecognized packwiz bootstrap; refuse to distribute")
    meta = json.loads((root / "manifest/integration-lock.json").read_text())
    if meta["channel"] != "INTEGRATION-NOT-STABLE":
        raise RuntimeError("Wrong build channel")
    # Personal options are shipped ONLY by the UNSTABLE QA client builder.
    qa_assets = {
        "minecraft/options.txt": root / "scripts/prism_qa_options.txt",
        "minecraft/tmm-qa-preferences.ps1": root / "scripts/tmm_qa_preferences.ps1",
        "minecraft/IMPORT-OLD-TMM-PREFERENCES.cmd": root / "scripts/IMPORT-OLD-TMM-PREFERENCES.cmd",
    }
    for arcname, source in qa_assets.items():
        if not source.is_file():
            raise RuntimeError("Missing QA-only asset: " + arcname)
    seed = qa_assets["minecraft/options.txt"].read_text(encoding="utf-8")
    if not seed.startswith("version:3955\n") or "guiScale:" not in seed or "soundCategory_master:" not in seed:
        raise RuntimeError("Wrong Minecraft 1.21.1 QA defaults")
    if any(line.startswith("key_") for line in seed.splitlines()):
        raise RuntimeError("Never invent the owner's keybindings")
    addons = [pathlib.Path(monk_jar), pathlib.Path(bridge_jar)]
    expected = ("tmm_monk-", "tmm_transformations-")
    for jar, prefix in zip(addons, expected):
        if not jar.is_file() or not jar.name.startswith(prefix) or jar.suffix != ".jar":
            raise RuntimeError("Missing expected first-party jar " + str(jar))
        with zipfile.ZipFile(jar) as z:
            if not any(n.startswith("META-INF/") for n in z.namelist()):
                raise RuntimeError("Invalid built module " + jar.name)
    pack = f"https://raw.githubusercontent.com/{REPO}/{commit}/pack.toml"
    addons_sha = {jar.name: hashlib.sha256(jar.read_bytes()).hexdigest() for jar in addons}
    meta["integration_commit"] = commit
    meta["custom_jar_sha256"] = addons_sha
    meta["client_tested"] = False
    meta["stable_promoted"] = False
    meta["personal_settings"] = {
        "type": "QA-only Windows local preferences sync",
        "minecraft": "1.21.1", "version": 3955,
        "public_release": False,
        "storage": "LOCALAPPDATA/TooMuchMagic/TestPreferences/options-1.21.1.txt",
    }
    guide = f"""TOO MUCH MAGIC — FULL INTEGRATION / EXPERIMENTAL
This is NOT STABLE and has NOT PASSED YOUR IN-WORLD ACCEPTANCE TESTS.
DO NOT OPEN EXISTING SAVES. Create a new disposable Prism instance and world.
Minecraft 1.21.1; NeoForge 21.1.265; Java 21; 8 GiB suggested.

Sources: {commit}
Pinned packwiz: {pack}
Code/source manifest and exact branch SHAs: BUILD-MANIFEST.json in this ZIP.

WINDOWS PERSONAL QA SETTINGS (NOT PART OF PUBLIC MODPACK):
- First import: GUI scale 3, master volume 55%, music 20%, weather
  55%, ambient 65%. We do not guess your personal keybindings.
- Adjust your settings once, then exit Minecraft normally.
  The post-exit hook saves controls, GUI/chat sizing and sound locally;
  future test imports restore them before launch automatically.
- To inherit an already-configured OLD Prism instance, in this NEW
  instance's minecraft folder run IMPORT-OLD-TMM-PREFERENCES.cmd,
  select the old instance's minecraft/options.txt, then launch.
- Your preferences never leave your computer. Worlds, server lists,
  mod configurations, resource packs and graphics remain separate.
Only the TWO first-party custom JARs are bundled. All other mods are fetched
by packwiz from their original hosts using checked hashes.

Suggested QA:
- All five FTB category labels, chapter titles, and 179 quest descriptions.
- Copper quest: submit 8, claim XP AND command coin; check /hqs coins and shop.
- Confirm TrashSlot, TreeChop, fast leaf decay, the NEW Eternal Steak,
  and More Overlays are available automatically.
- If FTB Essentials is pinned in BUILD-MANIFEST, verify non-OP /sethome,
  /home after reconnect, /delhome, /listhomes and /rtp safety/cooldown;
  allow one home and keep the test world disposable (RTP is not claim-aware).
- Test conventional dust, silver equipment and ore-doubling conflicts.
  Compare Polymorph choices. Do not use scarce/valuable materials.
- Roots first staff pickup/Terra pilot if enabled by its source conditions.
- /tmmtransform vampire read-only metrics before/after each infection path.
  The experiment flags are OFF by default; enable ONLY in disposable worlds.
- /tmmmonk attributes is a read-only diagnostic, NOT a complete Monk skill tree.
- Tyz/MXP conversion and Pufferfish removal are PENDING, not represented
  as gameplay features in this download. Do not credit them as implemented.
- Save logs/latest.log, Prism console, screenshots and feature results.

Stable/candidate promotion is a SEPARATE manual operation. No automatic
switch of a known-good build or world migration occurs.
"""
    cfg = f"""[General]
InstanceType=OneSix
name=Too Much Magic - Full Integration DEV - Test Worlds ONLY
OverrideCommands=true
PreLoadCommand=powershell.exe -NoProfile -ExecutionPolicy Bypass -File "$INST_MC_DIR/tmm-qa-preferences.ps1" -Mode apply -OptionsPath "$INST_MC_DIR/options.txt"
PreLaunchCommand="$INST_JAVA" -jar packwiz-installer-bootstrap.jar -s client {pack}
PostExitCommand=powershell.exe -NoProfile -ExecutionPolicy Bypass -File "$INST_MC_DIR/tmm-qa-preferences.ps1" -Mode save -OptionsPath "$INST_MC_DIR/options.txt"
OverrideMemory=true
MinMemAlloc=2048
MaxMemAlloc=8192
"""
    mmc = {"formatVersion": 1, "components": [
        {"uid": "net.minecraft", "version": "1.21.1", "important": True},
        {"uid": "net.neoforged", "version": "21.1.265"}]}
    target = pathlib.Path(output_dir) / OUTPUT
    target.parent.mkdir(parents=True, exist_ok=True)
    with zipfile.ZipFile(target, "w", zipfile.ZIP_DEFLATED) as z:
        z.writestr("instance.cfg", cfg)
        z.writestr("mmc-pack.json", json.dumps(mmc, indent=2) + "\n")
        z.writestr("minecraft/packwiz-installer-bootstrap.jar", raw)
        z.writestr("INSTALL-AND-TEST.txt", guide)
        z.writestr("BUILD-MANIFEST.json", json.dumps(meta, indent=2) + "\n")
        for arcname, source in qa_assets.items():
            z.write(source, arcname)
        for jar in addons:
            z.write(jar, "minecraft/mods/" + jar.name)
    with zipfile.ZipFile(target) as z:
        names = z.namelist()
        actual = [n for n in names if n.startswith("minecraft/mods/") and n.endswith(".jar")]
        if sorted(actual) != sorted("minecraft/mods/" + x.name for x in addons):
            raise RuntimeError("Wrong custom jars in Prism distribution")
        for jar in addons:
            if z.read("minecraft/mods/" + jar.name) != jar.read_bytes():
                raise RuntimeError("First-party jar changed during packaging")
        if pack.encode() not in z.read("instance.cfg"):
            raise RuntimeError("Installer is not commit locked")
        for arcname, source in qa_assets.items():
            if z.read(arcname) != source.read_bytes():
                raise RuntimeError("QA asset changed during packaging: " + arcname)
        conf = z.read("instance.cfg")
        if b"PreLoadCommand=powershell.exe" not in conf or b"PostExitCommand=powershell.exe" not in conf:
            raise RuntimeError("Personal QA sync hook missing")
    print(json.dumps({"status": "PACKAGED-NOT-STABLE",
        "path": str(target), "sha256": hashlib.sha256(target.read_bytes()).hexdigest(),
        "integration_commit": commit, "first_party_jars": addons_sha,
        "third_party_jars_bundled": 0, "dev_only_preferences": True}, indent=2))

if __name__ == "__main__":
    if len(sys.argv) != 6:
        sys.exit("Usage: build_full_integration_prism.py BOOTSTRAP OUTPUT_DIR COMMIT MONK_JAR TRANSFORM_JAR")
    main(*sys.argv[1:])
