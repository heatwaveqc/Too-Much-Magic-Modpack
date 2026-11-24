#!/usr/bin/env python3
"""Offline A-level QA for a frozen unstable checkout and an experimental checkout.

Never assembles a pack, starts Minecraft, builds Prism, or edits
either checkout. Optional first-party Gradle builds may fetch build dependencies.
Source-identical checks are reused, not counted as game passes.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import re
import subprocess
import sys
import tomllib
from dataclasses import dataclass
from pathlib import Path

SHA = re.compile(r"[0-9a-f]{40}\Z")


@dataclass(frozen=True)
class Check:
    id: str
    title: str
    command: tuple[str, ...]
    watch: tuple[str, ...]
    modules: bool = False


CHECKS = (
    Check("A01", "Packwiz integrity and repository validation", ("py", "scripts/validate.py"),
          ("pack.toml", "index.toml", "mods/*.pw.toml", "manifest/mods.json", "scripts/validate.py")),
    Check("A02", "Indexed QA roster and exact pin metadata", ("py", "scripts/verify_qa_inventory.py"),
          ("pack.toml", "index.toml", "mods/*.pw.toml", "manifest/mods.json",
           "manifest/next-qa-target.json", "manifest/first-party-runtime.json",
           "scripts/verify_qa_inventory.py")),
    Check("A03", "Read-only integration source preflight", ("py", "scripts/validate_integration_sources.py"),
          ("manifest/integration-sources.json", "scripts/validate_integration_sources.py",
           "manifest/first-party-runtime.json")),
    Check("A04", "Unified Progression contract", ("py", "scripts/verify_unified_progression.py"),
          ("compat/progression/**/*", "manifest/first-party-runtime.json",
           "mods/ars-elemental.pw.toml", "mods/goety.pw.toml",
           "scripts/verify_unified_progression.py")),
    Check("A05", "Quest content and localization", ("py", "scripts/validate_quests.py"),
          ("config/ftbquests/**/*", "kubejs/**/*", "manifest/quest*.json",
           "mods/ars-elemental.pw.toml", "scripts/validate_quests.py")),
    Check("A06", "Canonical blood / transformation sources", ("py", "scripts/verify_canonical_blood.py"),
          ("compat/transformations/**/*", "mods/evilcraft.pw.toml",
           "kubejs/**/*", "scripts/verify_canonical_blood.py")),
    Check("A07", "Materials, dust and recipe contracts", ("py", "scripts/verify_materials.py"),
          ("config/almostunified/**/*", "kubejs/**/*", "mods/neovitae.pw.toml",
           "mods/evilcraft.pw.toml", "scripts/verify_materials.py")),
    Check("A08", "Canonical silver equipment", ("py", "scripts/verify_silver_equipment.py"),
          ("compat/progression/**/*", "kubejs/**/*", "config/almostunified/**/*",
           "scripts/verify_silver_equipment.py")),
    Check("A09", "Newbie and MXP integration", ("py", "scripts/validate_newbie_integration.py"),
          ("compat/progression/**/*", "manifest/first-party-runtime.json",
           "mods/ars-elemental.pw.toml", "mods/goety.pw.toml",
           "scripts/validate_newbie_integration.py")),
    Check("A10", "Progression and ProjectE gates", ("py", "scripts/validate_progression_gates.py"),
          ("compat/progression/**/*", "kubejs/**/*", "manifest/progression-gates.json",
           "mods/goety.pw.toml", "scripts/validate_progression_gates.py")),
    Check("A11", "Dragon food data (not the redesign)", ("py", "scripts/verify_dragon_diets.py"),
          ("kubejs/**/*", "config/dragonsurvival/**/*", "scripts/verify_dragon_diets.py")),
    Check("A12", "Offline Prism packaging fixture", ("py", "tests/test_prism_qa_packaging.py"),
          ("scripts/build_full_integration_prism.py", "tests/test_prism_qa_packaging.py",
           "scripts/tmm_qa_preferences.ps1")),
    Check("A13", "First-party compile and unit tests", ("py", "scripts/first_party_runtime.py", "build"),
          ("compat/**/*", "manifest/first-party-runtime.json", "mods/neovitae.pw.toml",
           "mods/evilcraft.pw.toml", "mods/goety.pw.toml",
           "mods/ars-elemental.pw.toml", "scripts/first_party_runtime.py"), True),
)

FOUR_UPDATES = {
    "ars-elemental": "0.7.11.3",  # Beta, trial only.
    "evilcraft": "1.2.102",
    "goety": "3.2.3",
    "neovitae": "1.1.54",
}


def checkout_sha(root: Path) -> str:
    result = subprocess.run(["git", "-C", str(root), "rev-parse", "HEAD"],
                            text=True, capture_output=True, check=True)
    return result.stdout.strip()


def dirty(root: Path) -> bool:
    result = subprocess.run(["git", "-C", str(root), "status", "--porcelain"],
                            text=True, capture_output=True, check=True)
    return bool(result.stdout.strip())


def inventory(root: Path) -> dict[str, dict]:
    """Validate mod pin hashes and the Packwiz index hash; never download JARs."""
    pack = tomllib.loads((root / "pack.toml").read_text(encoding="utf-8"))
    index_file = root / "index.toml"
    if pack["index"]["file"] != "index.toml" or pack["index"]["hash-format"] != "sha256":
        raise ValueError(f"{root}: unexpected Packwiz index")
    if hashlib.sha256(index_file.read_bytes()).hexdigest() != pack["index"]["hash"]:
        raise ValueError(f"{root}: pack.toml index hash mismatch")
    if pack["versions"]["minecraft"] != "1.21.1" or pack["versions"]["neoforge"] != "21.1.265":
        raise ValueError(f"{root}: unexpected Minecraft/NeoForge target")
    index = tomllib.loads(index_file.read_text(encoding="utf-8"))
    if index["hash-format"] != "sha256":
        raise ValueError(f"{root}: unexpected index hash format")
    mods: dict[str, dict] = {}
    for row in index["files"]:
        rel = row["file"]
        if not (rel.startswith("mods/") and rel.endswith(".pw.toml")):
            continue
        if not row.get("metafile"):
            raise ValueError(f"{root}: mod pin not marked metafile: {rel}")
        path = root / rel
        if hashlib.sha256(path.read_bytes()).hexdigest() != row["hash"]:
            raise ValueError(f"{root}: stale mod pin hash: {rel}")
        pin = tomllib.loads(path.read_text(encoding="utf-8"))
        slug = Path(rel).name.removesuffix(".pw.toml")
        if slug in mods:
            raise ValueError(f"{root}: duplicate mod pin: {slug}")
        mods[slug] = {
            "filename": pin["filename"], "side": pin.get("side", "both"),
            "hash_format": pin["download"]["hash-format"],
            "hash": pin["download"]["hash"],
        }
    if not mods:
        raise ValueError(f"{root}: zero mod pins")
    return mods


def fingerprint(root: Path, patterns: tuple[str, ...]) -> str:
    """Hash watched source content, not mtimes; ignore generated/untracked build output."""
    found: dict[str, Path] = {}
    for pattern in patterns:
        for path in root.glob(pattern):
            if path.is_file():
                if path.is_symlink():
                    raise ValueError(f"Symlink in watched sources: {path}")
                found[path.relative_to(root).as_posix()] = path
    h = hashlib.sha256()
    for relative, path in sorted(found.items()):
        h.update(relative.encode("utf-8") + b"\0")
        with path.open("rb") as stream:
            while block := stream.read(1024 * 1024):
                h.update(block)
    return h.hexdigest()


def select_repeats(baseline: Path, experiment: Path, checks=CHECKS,
                   force_both: bool = False) -> dict[str, bool]:
    """True only when experimental inputs or the checker itself changed."""
    return {check.id: force_both or
            fingerprint(baseline, check.watch) != fingerprint(experiment, check.watch)
            for check in checks}


def execute(root: Path, check: Check, timeout: int) -> dict:
    if check.command[0] != "py":
        raise ValueError("Only explicitly registered Python offline checks are supported")
    command = [sys.executable, *check.command[1:]]
    if not (root / check.command[1]).is_file():
        return {"status": "BLOCKED", "command": command, "reason": "Checker missing"}
    try:
        completed = subprocess.run(command, cwd=root, capture_output=True, text=True,
                                   errors="replace", timeout=timeout, check=False)
        output = (completed.stdout + "\n" + completed.stderr)[-5000:]
        return {"status": "PASS" if completed.returncode == 0 else "FAIL",
                "exit_code": completed.returncode, "command": command, "output_tail": output}
    except subprocess.TimeoutExpired:
        return {"status": "BLOCKED", "command": command, "reason": "Timed out"}
    except OSError as exc:
        return {"status": "BLOCKED", "command": command, "reason": str(exc)}


def run(args) -> dict:
    baseline, experiment = args.unstable.resolve(), args.experimental.resolve()
    if baseline == experiment:
        raise ValueError("Use two separate source checkouts, not one directory")
    for name, sha in (("unstable", args.unstable_sha), ("experimental", args.experimental_sha)):
        if not SHA.fullmatch(sha):
            raise ValueError(f"{name}: expected SHA must be a full 40-character commit")
    identities = {"unstable": checkout_sha(baseline), "experimental": checkout_sha(experiment)}
    if identities != {"unstable": args.unstable_sha, "experimental": args.experimental_sha}:
        raise ValueError(f"Checkout SHA mismatch: {identities}")
    if args.run_a and (dirty(baseline) or dirty(experiment)):
        raise ValueError("Offline A execution requires both source checkouts clean")
    unstable_pins, experimental_pins = inventory(baseline), inventory(experiment)
    changed = {
        slug: {"unstable": unstable_pins.get(slug), "experimental": experimental_pins.get(slug)}
        for slug in sorted(set(unstable_pins) | set(experimental_pins))
        if unstable_pins.get(slug) != experimental_pins.get(slug)
    }
    trial = {
        slug: {"expected_version": version,
               "actual_filename": experimental_pins.get(slug, {}).get("filename"),
               "matches_expected": version in experimental_pins.get(slug, {}).get("filename", "")}
        for slug, version in FOUR_UPDATES.items()
    }
    if args.require_four_updates and (
        set(changed) != set(FOUR_UPDATES)
        or not all(item["matches_expected"] for item in trial.values())
    ):
        raise ValueError(f"Expected exactly four isolated trial pin changes: {sorted(changed)}")
    repeats = select_repeats(baseline, experiment, force_both=args.force_both)
    results = []
    for check in CHECKS:
        base = {"status": "NOT RUN"}
        exp = {"status": "NOT RUN"}
        if args.run_a and (args.build_modules or not check.modules):
            base = execute(baseline, check, args.timeout)
            exp = execute(experiment, check, args.timeout) if repeats[check.id] else {
                "status": "REUSED SOURCE CHECK",
                "source": "unstable",
                "source_result": base["status"],
                "note": "Same watched inputs; not a Minecraft or artifact test",
            }
        results.append({"id": check.id, "title": check.title,
                        "rerun_experimental": repeats[check.id],
                        "unstable": base, "experimental": exp})
    return {
        "schema": 1, "execution": "A offline only", "source_shas": identities,
        "mod_count": {"unstable": len(unstable_pins), "experimental": len(experimental_pins)},
        "mod_pin_diff": changed, "expected_four_update_versions": trial,
        "checks": results, "client_or_gameplay_verified": False,
        "full_server_smoke_run": False,
    }


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--unstable", type=Path, required=True, help="Immutable unstable source checkout")
    parser.add_argument("--unstable-sha", required=True, help="Full commit SHA of unstable checkout")
    parser.add_argument("--experimental", type=Path, required=True, help="Immutable experimental checkout")
    parser.add_argument("--experimental-sha", required=True, help="Full commit SHA of experimental checkout")
    parser.add_argument("--run-a", action="store_true", help="Run offline A checks (default: compare/plan only)")
    parser.add_argument("--build-modules", action="store_true",
                        help="Include Gradle first-party builds; only with --run-a")
    parser.add_argument("--force-both", action="store_true",
                        help="Rerun experimental checks despite identical watched inputs")
    parser.add_argument("--require-four-updates", action="store_true",
                        help="Fail if experimental pins differ by anything but the four approved trial mods")
    parser.add_argument("--timeout", type=int, default=300, help="Per-check timeout seconds")
    parser.add_argument("--report", type=Path, help="Write machine-readable A plan/results JSON")
    args = parser.parse_args()
    if args.timeout <= 0 or (args.build_modules and not args.run_a):
        parser.error("--timeout must be positive and --build-modules requires --run-a")
    try:
        result = run(args)
    except (ValueError, OSError, KeyError, subprocess.CalledProcessError, tomllib.TOMLDecodeError) as exc:
        print(f"QA BLOCKED: {exc}", file=sys.stderr)
        return 2
    for item in result["checks"]:
        print(f'{item["id"]} {item["title"]}: unstable={item["unstable"]["status"]}, '
              f'experimental={item["experimental"]["status"]}, '
              f'rerun_experimental={item["rerun_experimental"]}')
    print("Mod-pin differences:", ", ".join(result["mod_pin_diff"]) or "(none)")
    print("In-game B/C: NOT RUN; Prism/client and server status not inferred")
    if args.report:
        args.report.parent.mkdir(parents=True, exist_ok=True)
        args.report.write_text(json.dumps(result, indent=2) + "\n", encoding="utf-8")
    return 1 if any(
        case[channel]["status"] == "FAIL"
        for case in result["checks"] for channel in ("unstable", "experimental")
    ) else 0


if __name__ == "__main__":
    sys.exit(main())
