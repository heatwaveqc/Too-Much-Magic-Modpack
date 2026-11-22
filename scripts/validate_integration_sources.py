#!/usr/bin/env python3
"""Read-only integrity checks for the integration source registry.

No merge, restore, mod download, world launch, or artifact creation. Safe for CI.
Feature drafts are reported, not silently included or used to fail routine CI.
"""
from __future__ import annotations

import argparse
import fnmatch
import json
import re
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TRACKED_PREFIXES = ("feature/", "fix/", "design/", "audit/")
SHA = re.compile(r"^[0-9a-f]{40}$")
ALLOWED_ROOTS = ("compat/", "integration/", "kubejs/", "config/",
                 "defaultconfigs/", "mods/", "resourcepacks/", "manifest/", "scripts/")


class PreflightFailure(RuntimeError):
    pass


def git(*args: str, allow_missing: bool = False) -> str | None:
    result = subprocess.run(["git", *args], cwd=ROOT, text=True,
                            stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    if result.returncode:
        if allow_missing:
            return None
        raise PreflightFailure("git " + " ".join(args) + ": " + result.stderr.strip())
    return result.stdout.strip()


def paths_at(ref: str, selector: str) -> list[str]:
    return (git("ls-tree", "-r", "--name-only", ref, "--", selector) or "").splitlines()


def source_for(stream: dict, head: str) -> str:
    if stream.get("integrated_into_baseline"):
        if stream.get("branch") != "test/full-integration":
            raise PreflightFailure(stream["name"] + ": merged content must select the integration baseline")
        merge_commit = stream.get("merged_commit")
        if merge_commit and not SHA.fullmatch(merge_commit):
            raise PreflightFailure(stream["name"] + ": invalid provenance merge_commit")
        return head
    sha = stream.get("source_commit")
    if not isinstance(sha, str) or not SHA.fullmatch(sha):
        raise PreflightFailure(stream["name"] + ": external stream needs immutable 40-character source_commit")
    if git("cat-file", "-e", sha + "^{commit}", allow_missing=True) is None:
        raise PreflightFailure(stream["name"] + ": frozen source commit unavailable: " + sha)
    return sha


def approved_override(stream: dict, path: str) -> bool:
    overrides = stream.get("approved_overrides", {})
    if not isinstance(overrides, dict):
        raise PreflightFailure(stream["name"] + ": approved_overrides must map file patterns to reasons")
    for pattern, reason in overrides.items():
        if not isinstance(reason, str) or len(reason.strip()) < 15:
            raise PreflightFailure(stream["name"] + ": override must record a meaningful reason: " + pattern)
        if fnmatch.fnmatchcase(path, pattern):
            baseline_sha = stream.get("expected_baseline_blobs", {}).get(path)
            if not baseline_sha or not SHA.fullmatch(baseline_sha):
                raise PreflightFailure(stream["name"] + ": override requires expected_baseline_blobs for " + path)
            existing_sha = git("rev-parse", "--verify", "HEAD:" + path, allow_missing=True)
            if existing_sha != baseline_sha:
                raise PreflightFailure(stream["name"] + ": approved override baseline has changed: " + path)
            return True
    return False


def validate(strict_overrides: bool = False, strict_readiness: bool = False) -> dict:
    cfg = json.loads((ROOT / "manifest/integration-sources.json").read_text(encoding="utf-8"))
    if cfg.get("schema") != 1 or cfg.get("channel") != "integration":
        raise PreflightFailure("Wrong integration source schema/channel")
    if cfg.get("baseline") != "test/full-integration":
        raise PreflightFailure("Integration baseline must be test/full-integration")
    head = git("rev-parse", "--verify", "HEAD")
    if git("status", "--porcelain"):
        raise PreflightFailure("Preflight checkout is dirty; inspect Git EOL and generated files")
    streams = cfg.get("streams", [])
    if not isinstance(streams, list) or not streams:
        raise PreflightFailure("No integration streams")
    owners: dict[str, str] = {}
    collisions: list[str] = []
    report = []
    seen_names = set()
    for stream in streams:
        name, branch = stream.get("name"), stream.get("branch")
        if not isinstance(name, str) or not name or name in seen_names:
            raise PreflightFailure("Missing or repeated stream name: " + str(name))
        seen_names.add(name)
        if not isinstance(branch, str) or not branch:
            raise PreflightFailure(name + ": missing provenance branch")
        ref = source_for(stream, head)
        selected = []
        optional = set(stream.get("optional_paths", []))
        locale_paths = stream.get("merge_json_paths", [])
        for locale in locale_paths:
            if not isinstance(locale, str) or not locale.startswith("kubejs/assets/") or "/lang/" not in locale or not locale.endswith(".json"):
                raise PreflightFailure(name + ": unsafe merged localization path " + str(locale))
            if paths_at(ref, locale) != [locale]:
                raise PreflightFailure(name + ": pinned donor missing merged locale " + locale)
        for selector in stream.get("paths", []) + stream.get("optional_paths", []):
            if not isinstance(selector, str) or not selector.startswith(ALLOWED_ROOTS) or ".." in Path(selector).parts:
                raise PreflightFailure(name + ": unsafe source path " + str(selector))
            matched = paths_at(ref, selector)
            if not matched and selector not in optional:
                raise PreflightFailure(name + ": pinned snapshot is missing required source " + selector)
            for path in matched:
                if path in owners and owners[path] != name:
                    raise PreflightFailure(name + ": source ownership overlap for " + path +
                                           " (owned by " + owners[path] + ")")
                owners[path] = name
                selected.append(path)
                if stream.get("integrated_into_baseline"):
                    continue
                prior = git("rev-parse", "--verify", "HEAD:" + path, allow_missing=True)
                donor = git("rev-parse", "--verify", ref + ":" + path, allow_missing=True)
                if prior and donor and prior != donor and not approved_override(stream, path):
                    collisions.append(name + ": " + path)
        if not selected:
            raise PreflightFailure(name + ": no source files selected")
        report.append({"stream": name, "source_commit": ref, "files": len(selected),
                       "integrated": bool(stream.get("integrated_into_baseline"))})

    classified = ({s["branch"] for s in streams}
                  | set(cfg.get("covered_by_baseline", {}))
                  | set(cfg.get("superseded_or_non_runtime", {}))
                  | set(cfg.get("deferred_pending_runtime", {})))
    remote = git("for-each-ref", "--format=%(refname:short)", "refs/remotes/origin") or ""
    unknown = sorted(ref.removeprefix("origin/") for ref in remote.splitlines()
                     if ref.startswith(tuple("origin/" + p for p in TRACKED_PREFIXES))
                     and ref.removeprefix("origin/") not in classified)
    if strict_readiness and unknown:
        raise PreflightFailure("Unreconciled active branches: " + ", ".join(unknown))
    if strict_overrides and collisions:
        raise PreflightFailure("Unapproved baseline file replacements (review before assembly):\n"
                               + "\n".join(collisions[:100]))
    return {"status": "PREFLIGHT PASS (read-only)", "head": head,
            "streams": report, "baseline_replacements_needing_review": collisions,
            "unclassified_draft_branches": unknown,
            "note": "Drafts do not block routine CI. Before a comprehensive build reconcile every READY item."}


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--strict-overrides", action="store_true",
                        help="fail if any source would replace baseline content without a documented exception")
    parser.add_argument("--strict-readiness", action="store_true",
                        help="fail on any unclassified implementation branch (comprehensive owner-authorized run)")
    args = parser.parse_args()
    try:
        result = validate(args.strict_overrides, args.strict_readiness)
        print(json.dumps(result, indent=2))
    except (PreflightFailure, OSError, ValueError, KeyError, json.JSONDecodeError) as exc:
        print("INTEGRATION PREFLIGHT BLOCKED:", exc, file=sys.stderr)
        sys.exit(1)


if __name__ == "__main__":
    main()
