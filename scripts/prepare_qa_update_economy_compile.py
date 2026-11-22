#!/usr/bin/env python3
"""Rebind Economy's *exact* Gradle compile dependency filenames for isolated update QA.

Only ever runs in an owner-authorized issue-comment CI job, against an
already-assembled, verified four-mod overlay. This does not relax the
Economy build's exact-file check, and never changes shared Git source.
The classpath fetcher independently verifies each downloaded JAR digest
against the assembled Packwiz manifest.
"""
from __future__ import annotations

import argparse
import os
from pathlib import Path
import re
import subprocess
import tomllib

# Only JARs currently present in the Economy compile classpath. Ars Elemental
# is tested by the modded server but not an Economy compile-time dependency.
UPGRADES = {
    "evilcraft": ("evilcraft-1.21.1-neoforge-1.2.100.jar",
                   "evilcraft-1.21.1-neoforge-1.2.102.jar"),
    "neovitae": ("neovitae-1.21.1-1.1.53.jar",
                  "neovitae-1.21.1-1.1.54.jar"),
    "goety": ("goety-3.2.2.jar", "goety-3.2.3.jar"),
}


def checked_patch(gradle: str, verified: dict[str, tuple[str, str]]) -> str:
    """Replace only explicitly enumerated old filenames inside exactJars."""
    marker = "def exactJars = "
    next_marker = "\nif (modsDir.isPresent()) {"
    if gradle.count(marker) != 1 or gradle.count(next_marker) != 1:
        raise ValueError("Economy Gradle exactJars contract has changed")
    left, remainder = gradle.split(marker, 1)
    block, right = remainder.split(next_marker, 1)
    if not block.startswith("[") or not block.rstrip().endswith("]"):
        raise ValueError("Unexpected Gradle exact dependency list")
    if set(verified) != set(UPGRADES):
        raise ValueError("Missing or extra upgrade identities")
    for slug, (old, new) in sorted(verified.items()):
        if UPGRADES[slug] != (old, new):
            raise ValueError("Unreviewed compile dependency version: " + slug)
        old_literal, new_literal = "'" + old + "'", "'" + new + "'"
        if block.count(old_literal) != 1 or new_literal in block:
            raise ValueError("Economy compile pin is not the expected baseline: " + slug)
        block = block.replace(old_literal, new_literal)
    result = left + marker + block + next_marker + right
    if result == gradle:
        raise ValueError("No changed compile pin")
    return result


def git(root: Path, *args: str) -> str:
    return subprocess.check_output(["git", "-C", str(root), *args], text=True).strip()


def main() -> None:
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--assembly", type=Path, required=True)
    p.add_argument("--candidate-sha", required=True)
    args = p.parse_args()
    root = args.assembly.resolve()
    expected = args.candidate_sha
    if (os.environ.get("GITHUB_ACTIONS") != "true"
        or os.environ.get("GITHUB_EVENT_NAME") != "issue_comment"
        or not re.fullmatch("[0-9a-f]{40}", expected)
        or os.environ.get("TMM_UPDATE_CANDIDATE_SHA") != expected):
        raise RuntimeError("Not an owner-authorized exact-SHA QA job")
    if git(root, "branch", "--show-current") != "test/full-integration":
        raise RuntimeError("No clean integration assembly worktree")
    if git(root, "rev-parse", "HEAD") != git(root, "rev-parse", "refs/remotes/origin/test/full-integration"):
        raise RuntimeError("Integration source ref changed")
    lock = root / "manifest/integration-lock.json"
    if not lock.is_file() or ("update_trial" not in lock.read_text(encoding="utf-8")):
        raise RuntimeError("Four-mod isolated overlay is not present")

    verified = {}
    for slug, (old_name, new_name) in UPGRADES.items():
        pin_path = f"mods/{slug}.pw.toml"
        old_pin = tomllib.loads(git(root, "show", "HEAD:" + pin_path))
        new_pin = tomllib.loads((root / pin_path).read_text(encoding="utf-8"))
        if old_pin["filename"] != old_name or new_pin["filename"] != new_name:
            raise RuntimeError("Unexpected live or update pin: " + slug)
        if (new_pin["download"]["hash-format"] != "sha512"
                or not re.fullmatch("[0-9a-f]{128}", new_pin["download"]["hash"])):
            raise RuntimeError("Missing SHA-512 for updated compile JAR: " + slug)
        verified[slug] = (old_name, new_name)

    gradle_path = root / "compat/economy/build.gradle"
    original = gradle_path.read_text(encoding="utf-8")
    candidate = checked_patch(original, verified)
    gradle_path.write_text(candidate, encoding="utf-8", newline="\n")
    print("PASS: Economy exact compile dependency filenames updated only for "
          + ", ".join(sorted(verified)))
    print("All actual JARs still independently verified by first_party_runtime classpath fetcher; "
          "no jar aliases or automatic generic versions allowed.")


if __name__ == "__main__":
    main()
