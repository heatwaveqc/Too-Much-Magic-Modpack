#!/usr/bin/env python3
"""Produce a single-level GitHub artifact directly importable as a Prism instance."""
from __future__ import annotations
import hashlib
import json
import pathlib
import re
import sys
import zipfile
from first_party_runtime import verify_packaging_inputs
from verify_qa_inventory import inventory, for_side

REPO = "heatwaveqc/Too-Much-Magic-Modpack"
OUTPUT = "Too-Much-Magic-Full-Integration-Prism.zip"

def main(bootstrap: str, output_dir: str, commit: str, *runtime_jars: str) -> None:
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
    if seed.splitlines().count("onboardAccessibility:false") != 1 or any(line.startswith("narrator:") for line in seed.splitlines()):
        raise RuntimeError("Skip QA accessibility onboarding without forcing narrator preferences")
    if seed.splitlines().count("tutorialStep:none") != 1 or sum(line.startswith("tutorialStep:") for line in seed.splitlines()) != 1:
        raise RuntimeError("Skip vanilla tutorial toasts in fresh QA instances")
    if any(line.startswith("key_") for line in seed.splitlines()):
        raise RuntimeError("Never invent the owner's keybindings")
    addons = [pathlib.Path(p) for p in runtime_jars]
    verify_packaging_inputs(addons, root)
    pack = f"https://raw.githubusercontent.com/{REPO}/{commit}/pack.toml"
    addons_sha = {jar.name: hashlib.sha256(jar.read_bytes()).hexdigest() for jar in addons}
    expected = {"schema": 1, "source_commit": commit, "mods": for_side(inventory(root), "client", addons)}
    meta["expected_client_mod_count"] = len(expected["mods"])
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
This build is NOT STABLE and has NOT PASSED IN-WORLD ACCEPTANCE.
DO NOT OPEN EXISTING SAVES. Create a disposable Prism instance/world.
Minecraft 1.21.1, NeoForge 21.1.265, Java 21.

Source commit: {commit}
Pinned packwiz: {pack}
Exact source SHAs and first-party JAR hashes: BUILD-MANIFEST.json

NEXT TEST BUILD — STANDALONE MONK/HUNTER AND JAWR BRIDGE
- Retired: TeamLapen Vampirism, Werewolves and Vampire's Delight; no
  TeamLapen vampire blood, hunter faction or companion GUI.
- Included: tyr_progression with native Monk and Hunter class XP/skills.
  Monk classic trials, Bedrock Rest, armor lock, First Sunset.
  Hunter Trial of Venom, creature specialties, silver training,
  craftable Bio-Mutagen, Mutagenic Brew, permanent mutation pilot.
- Included: tmm_transformations with read-only JAWR vampire/werewolf
  form diagnostics, Goety/Forbidden Magic lich and Dragon Survival
  probes, Archlich/Dracolich event priority and cross-mod mob alliances.
- JAWR alone owns vampire/lycanthrope blood, hunger, progression, cures.
- Eidolon silver tools/armor; bonus silver-versus-lycanthrope is a
  native Hunter training skill, not an unconditional global modifier.
- No TeamLapen quests, village worldgen, stone outputs or food tags.

OPERATOR/PLAYER QA:
/tyrprogress list
/tyrprogress admin join tyr_progression:monk
/tyrprogress admin xp tyr_progression:monk 28000
/tyrprogress status tyr_progression:monk
/tyrprogress admin join tyr_progression:hunter
/tyrprogress admin xp tyr_progression:hunter 1000
/tyrprogress status tyr_progression:hunter
/tyrprogress mutations
/tmmtransform status
/tmmtransform vampire
/tmmtransform werewolf
/tmmtransform archlich
/tmmtransform dracolich

Run QA on disposable world(s): confirm all selected first-party JARs load; test Monk
First Sunset, bare-hand XP, Bedrock Rest, safety of armor lock and
inventory slot; Hunter potion enrollment, mutation brewing/cost,
specialty XP and Silver Hunter, persistence across reload/death;
JAWR vampire/werewolf forms, neutrality and resurrection priority.
Archlich death priority is experimental and modifies event ordering.
Client, multiplayer, third-party binary behavior, native death/cure
and mutation persistence are NOT proven by server smoke alone.

Other mods download by packwiz; selected first-party JARs bundled.
Packwiz installs/updates mods directly from the immutable hash-pinned pack.
Full client inventory verification is optional, not an executable launch gate.
From a TMM repository checkout after installing the Prism instance, run:
  py -3 scripts/audit_client_inventory.py "C:\\path\\to\\Prism instance\\.minecraft"
This read-only audit uses the packaged tmm-qa-expected-mods.json to verify the
installed client JAR set and hashes; it never starts Minecraft or Packwiz.
WINDOWS QA-ONLY PREFERENCES: first-instance display/sound defaults,
plus optional local options/keybind carryover.
Run IMPORT-OLD-TMM-PREFERENCES.cmd to select a previous options.txt.
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
        z.writestr("minecraft/tmm-qa-expected-mods.json", json.dumps(expected, indent=2) + "\n")
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
        if json.loads(z.read("minecraft/tmm-qa-expected-mods.json")) != expected:
            raise RuntimeError("Client inventory changed during packaging")
        conf = z.read("instance.cfg")
        expected_launch = f'PreLaunchCommand="$INST_JAVA" -jar packwiz-installer-bootstrap.jar -s client {pack}'
        if conf.decode("utf-8").splitlines().count(expected_launch) != 1:
            raise RuntimeError("Expected exactly one direct, commit-locked Packwiz prelaunch command")
        if b'tmm-qa-install-verify.ps1' in conf or b'PreLaunchCommand=powershell.exe' in conf:
            raise RuntimeError("Mandatory client PowerShell verifier unexpectedly enabled")
        if "minecraft/tmm-qa-install-verify.ps1" in names:
            raise RuntimeError("Legacy client installer/verifier must not be bundled")
        for arcname, source in qa_assets.items():
            if z.read(arcname) != source.read_bytes():
                raise RuntimeError("QA asset changed during packaging: " + arcname)
        conf = z.read("instance.cfg")
        if b"PreLoadCommand=powershell.exe" not in conf or b"PostExitCommand=powershell.exe" not in conf:
            raise RuntimeError("Personal QA sync hook missing")
    # GitHub actions/upload-artifact always wraps uploaded content in a ZIP.
    # Uploading this ZIP as a file makes a ZIP INSIDE a ZIP for every player.
    # Without changing the trusted workflow (which points to OUTPUT), stage
    # the *contents* as a directory named OUTPUT. The artifact ZIP will then
    # have instance.cfg and mmc-pack.json at its ROOT and can be imported
    # directly by Prism, without an extraction step.
    #
    # Keep producing/validating an ordinary Prism ZIP above, then extract our
    # own verified members for the artifact upload. The .zip suffix on the
    # staging DIRECTORY is only an internal workflow-path compatibility trick.
    # Players will download a normal, single-level GitHub ZIP.
    validated_archive_sha = hashlib.sha256(target.read_bytes()).hexdigest()
    staging = target.with_name(target.name + ".upload-contents")
    if staging.exists():
        raise RuntimeError("Upload staging already exists: " + str(staging))
    staging.mkdir()
    with zipfile.ZipFile(target) as z:
        for info in z.infolist():
            p = pathlib.PurePosixPath(info.filename)
            if p.is_absolute() or ".." in p.parts or info.is_dir():
                raise RuntimeError("Unexpected/unsafe Prism ZIP entry: " + info.filename)
            out = staging.joinpath(*p.parts)
            out.parent.mkdir(parents=True, exist_ok=True)
            out.write_bytes(z.read(info.filename))
    if not (staging / "instance.cfg").is_file() or not (staging / "mmc-pack.json").is_file():
        raise RuntimeError("Prism required files must be at artifact root")
    if any(staging.glob("*.zip")) or any(staging.glob("*.mrpack")):
        raise RuntimeError("Artifact must not contain a nested modpack archive")
    with zipfile.ZipFile(target) as z:
        for name in z.namelist():
            if (staging / name).read_bytes() != z.read(name):
                raise RuntimeError("Artifact staging content mismatch: " + name)
    target.unlink()
    staging.rename(target)
    if not target.is_dir():
        raise RuntimeError("GitHub upload must receive the Prism directory, not a ZIP file")
    print(json.dumps({"status": "PACKAGED-NOT-STABLE",
        "path": str(target), "upload_is_directory": True,
        "github_artifact_is_direct_prism_zip": True,
        "validated_source_zip_sha256": validated_archive_sha,
        "integration_commit": commit, "first_party_jars": addons_sha,
        "third_party_jars_bundled": 0, "dev_only_preferences": True}, indent=2))

if __name__ == "__main__":
    if len(sys.argv) < 5:
        sys.exit("Usage: build_full_integration_prism.py BOOTSTRAP OUTPUT_DIR COMMIT FIRST_PARTY_JAR [FIRST_PARTY_JAR ...]")
    main(*sys.argv[1:])
