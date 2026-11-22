#!/usr/bin/env python3
"""Apply exactly four inspected update pins *after* pristine integration assembly.

The regular assembler intentionally only runs on a clean, current
test/full-integration checkout. Never weaken or bypass that contract. In an
ephemeral owner-authorized QA workflow, first assemble that exact baseline in
a separate checkout, then overlay only the four independently verified pins
from the pinned experimental commit. No Git pushes or public artifacts.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import os
import re
import subprocess
import tomllib
from pathlib import Path

SLUG_FILES = {
    "neovitae": "neovitae-1.21.1-1.1.54.jar",
    "evilcraft": "evilcraft-1.21.1-neoforge-1.2.102.jar",
    "goety": "goety-3.2.3.jar",
    "ars-elemental": "ars_elemental-1.21.1-0.7.11.3.jar",
}
SHA = re.compile(r"^[0-9a-f]{40}$")


def git(root: Path, *args: str) -> str:
    return subprocess.check_output(["git", "-C", str(root), *args], text=True).strip()


def content_at_head(root: Path, path: str) -> str:
    return git(root, "show", "HEAD:" + path)


def packwiz_update(index: str, slug: str, digest: str) -> str:
    pattern = (r'(?m)(^file = "mods/' + re.escape(slug) +
               r'\\.pw\\.toml"\\n(?:hash-format = "[^"]+"\\n)?hash = ")[0-9a-f]{64}(")')
    # Use explicit string replacement instead of trusting a loose global hash
    # substitution. Each pack file must have one and only one indexed pin.
    result, count = re.subn(pattern, lambda m: m[1] + digest + m[2], index)
    if count != 1:
        raise ValueError(f"Expected one pin entry for {slug}, found {count}")
    return result


def build_plan(trial: Path, assembled: Path, candidate_sha: str) -> dict[str, str]:
    if not SHA.fullmatch(candidate_sha):
        raise ValueError("Candidate SHA must be full 40-hex lowercase")
    if os.getenv("TMM_UPDATE_CANDIDATE_SHA") != candidate_sha:
        raise ValueError("CI candidate SHA environment mismatch")
    if os.getenv("GITHUB_ACTIONS") != "true" or os.getenv("GITHUB_EVENT_NAME") != "issue_comment":
        raise ValueError("Overlay only allowed during owner-only CI issue_comment")
    if git(trial, "rev-parse", "HEAD") != candidate_sha:
        raise ValueError("Candidate commit changed")
    if git(trial, "branch", "--show-current"):
        raise ValueError("Candidate checkout must be immutable/detached HEAD")
    if git(assembled, "branch", "--show-current") != "test/full-integration":
        raise ValueError("Assembler checkout must be real test/full-integration branch")
    if git(assembled, "rev-parse", "HEAD") != git(assembled, "rev-parse", "refs/remotes/origin/test/full-integration"):
        raise ValueError("Assembly baseline advanced, reject stale run")
    # The source assembler must have been run and saved its output before any overlay.
    lock_path = assembled / "manifest/integration-lock.json"
    if not lock_path.is_file():
        raise ValueError("Missing successful assembled integration lock")
    lock = json.loads(lock_path.read_text(encoding="utf-8"))
    if lock.get("channel") != "INTEGRATION-NOT-STABLE":
        raise ValueError("Not an assembled integration snapshot")

    original = {x["id"]: x for x in json.loads(content_at_head(trial, "manifest/mods.json"))["mods"]}
    updated = {x["id"]: x for x in json.loads((trial / "manifest/mods.json").read_text(encoding="utf-8"))["mods"]}
    assembly_manifest = json.loads((assembled / "manifest/mods.json").read_text(encoding="utf-8"))
    present = {x["id"]: x for x in assembly_manifest["mods"]}
    plan = {}
    index = (assembled / "index.toml").read_text(encoding="utf-8")
    pack = (assembled / "pack.toml").read_text(encoding="utf-8")

    for slug, expected_file in SLUG_FILES.items():
        pin_path = f"mods/{slug}.pw.toml"
        original_pin = content_at_head(trial, pin_path)
        assembled_pin = (assembled / pin_path).read_text(encoding="utf-8")
        if original_pin != assembled_pin or present[slug] != original[slug]:
            raise ValueError(f"Baseline changed {slug}; refuse to overwrite a newer integration change")
        if not original[slug].get("enabled_in_pack") or not updated[slug].get("enabled_in_pack"):
            raise ValueError(f"{slug} was disabled")
        new_pin = (trial / pin_path).read_text(encoding="utf-8")
        meta = tomllib.loads(new_pin)
        if (meta.get("filename") != expected_file or meta.get("side") != "both"
            or meta.get("download", {}).get("hash-format") != "sha512"
            or not re.fullmatch("[a-f0-9]{128}", meta["download"]["hash"])
            or updated[slug]["version"] == original[slug]["version"]):
            raise ValueError(f"Missing exact verified updated metadata for {slug}")
        plan[pin_path] = new_pin
        index = packwiz_update(index, slug, hashlib.sha256(new_pin.encode()).hexdigest())
        # Do not clobber other pack mods or newly assembled additions.
        for position, item in enumerate(assembly_manifest["mods"]):
            if item["id"] == slug:
                assembly_manifest["mods"][position] = updated[slug]
                break
        else:
            raise ValueError(f"Missing active assembly mod: {slug}")
    plan["manifest/mods.json"] = json.dumps(assembly_manifest, indent=2, ensure_ascii=False) + "\n"
    plan["index.toml"] = index
    pattern = r'(?m)(^\\[index\\]\\nfile = "index.toml"\\nhash-format = "sha256"\\nhash = ")[0-9a-f]{64}(")'
    digest = hashlib.sha256(index.encode()).hexdigest()
    pack, count = re.subn(pattern, lambda m: m[1] + digest + m[2], pack)
    if count != 1:
        raise ValueError("Unexpected Packwiz index header")
    plan["pack.toml"] = pack
    lock["pack_sha256"] = hashlib.sha256(pack.encode()).hexdigest()
    lock["update_trial"] = {
        "candidate_sha": candidate_sha,
        "base_sha": git(assembled, "rev-parse", "HEAD"),
        "four_mod_ids": sorted(SLUG_FILES),
        "client_tested": False,
        "smoke_tested": False,
    }
    plan["manifest/integration-lock.json"] = json.dumps(lock, indent=2) + "\n"
    return plan


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--trial", type=Path, required=True)
    parser.add_argument("--assembled", type=Path, required=True)
    parser.add_argument("--candidate-sha", required=True)
    args = parser.parse_args()
    trial, assembled = args.trial.resolve(), args.assembled.resolve()
    if trial == assembled or not trial.is_dir() or not assembled.is_dir():
        raise ValueError("Must use separate existing candidate and assembly checkouts")
    plan = build_plan(trial, assembled, args.candidate_sha)
    for path, contents in plan.items():
        target = assembled / path
        if not target.is_file():
            raise ValueError(f"Missing expected assembly file: {path}")
        target.write_text(contents, encoding="utf-8", newline="\n")
    print(json.dumps({"result": "FOUR MODS OVERLAID AFTER ASSEMBLY; SERVER NOT TESTED",
                      "source_sha": args.candidate_sha,
                      "assembly_sha": git(assembled, "rev-parse", "HEAD"),
                      "changed_paths": sorted(plan)}, indent=2))


if __name__ == "__main__":
    main()
