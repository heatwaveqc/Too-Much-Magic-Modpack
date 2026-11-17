#!/usr/bin/env python3
"""Safely retire audited orphan refs; never merge or rewrite their contents.

Default: read-only. Requires the exact SHA allowlist, an unprotected branch,
no open PR head/base, and no active frozen integration source-stream reference.
"""
from __future__ import annotations

import argparse
import base64
import json
import os
import sys
import urllib.error
import urllib.parse
import urllib.request
from pathlib import Path

REPO = "heatwaveqc/Too-Much-Magic-Modpack"
API = "https://api.github.com/repos/" + REPO
PROTECTED = {"main", "test/full-integration", "dev/progression"}
REQUIRED_PREFIXES = (
    "build/", "change/", "design/", "docs/", "feature/",
    "fix/", "research/", "test/",
)


def api(path: str, method: str = "GET"):
    req = urllib.request.Request(
        API + path,
        method=method,
        headers={
            "Accept": "application/vnd.github+json",
            "Authorization": "Bearer " + os.environ["GITHUB_TOKEN"],
            "User-Agent": "Too-Much-Magic-branch-retirement/1.0",
            "X-GitHub-Api-Version": "2022-11-28",
        },
    )
    with urllib.request.urlopen(req, timeout=30) as response:
        raw = response.read()
        return json.loads(raw.decode("utf-8")) if raw else None


def pages(path: str):
    entries = []
    for page in range(1, 20):
        data = api(path + ("&" if "?" in path else "?") + f"per_page=100&page={page}")
        if not isinstance(data, list):
            raise RuntimeError(f"Expected GitHub array for {path}")
        entries.extend(data)
        if len(data) < 100:
            return entries
    raise RuntimeError(f"Pagination unexpectedly exceeded 1900 entries: {path}")


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--manifest", type=Path, required=True)
    args = parser.parse_args()
    execute = os.environ.get("EXECUTE", "").lower() == "true"

    # An actual delete must come from the carefully scoped workflow on main,
    # or a confirmed manual dispatch of that same workflow on main.
    if execute and (
        os.environ.get("GITHUB_REF") != "refs/heads/main"
        or os.environ.get("GITHUB_REPOSITORY") != REPO
        or os.environ.get("GITHUB_EVENT_NAME") not in {"push", "workflow_dispatch"}
    ):
        raise RuntimeError("Refusing deletes outside the authorized main-branch workflow")

    data = json.loads(args.manifest.read_text(encoding="utf-8"))
    candidates = data["branches"]
    names = [entry["name"] for entry in candidates]
    if len(names) != 46 or len(names) != len(set(names)):
        raise RuntimeError("The frozen 46-branch list must be complete and unique")
    if any(
        name in PROTECTED or not name.startswith(REQUIRED_PREFIXES)
        or entry["sha"] is None or len(entry["sha"]) != 40
        for name, entry in zip(names, candidates)
    ):
        raise RuntimeError("Unsafe candidate name or non-full source SHA")

    open_prs = pages("/pulls?state=open")
    active_pr_refs = {
        ref
        for pr in open_prs
        for ref in (pr["head"]["ref"], pr["base"]["ref"])
    }

    # Fail closed: if the source manifest cannot be inspected, delete NOTHING.
    manifest_ref = api(
        "/contents/manifest/integration-sources.json"
        "?ref=test%2Ffull-integration"
    )
    if manifest_ref.get("encoding") != "base64":
        raise RuntimeError("Unexpected active source-manifest encoding")
    sources = json.loads(base64.b64decode(manifest_ref["content"]))
    active_sources = {
        row["branch"]
        for row in sources["streams"]
        if isinstance(row.get("branch"), str)
    }

    current = {
        entry["name"]: entry
        for entry in pages("/branches")
    }

    results = []
    for entry in candidates:
        name, expected = entry["name"], entry["sha"]
        reason = None
        branch = current.get(name)
        if branch is None:
            reason = "already absent"
        elif branch.get("protected") or name in PROTECTED:
            reason = "protected"
        elif name in active_pr_refs:
            reason = "open PR references branch"
        elif name in active_sources:
            reason = "active frozen source-stream dependency"
        elif branch["commit"]["sha"] != expected:
            reason = "branch SHA changed since approval"
        else:
            # Recheck the exact ref immediately before the destructive action.
            ref_path = "/git/ref/heads/" + urllib.parse.quote(name, safe="/")
            actual = api(ref_path)
            if actual["object"]["sha"] != expected:
                reason = "branch SHA changed on final check"

        if reason:
            results.append((name, "SKIP", reason))
            print(f"SKIP {name}: {reason}", flush=True)
            continue
        if not execute:
            results.append((name, "READY", "dry run only"))
            print(f"READY {name}: exact SHA matches; dry run", flush=True)
            continue

        try:
            api("/git/refs/heads/" + urllib.parse.quote(name, safe="/"), "DELETE")
            results.append((name, "DELETED", expected))
            print(f"DELETED {name} @ {expected}", flush=True)
        except (urllib.error.HTTPError, urllib.error.URLError) as exc:
            results.append((name, "ERROR", str(exc)))
            print(f"ERROR {name}: {exc}", flush=True)

    counts = {
        state: sum(row[1] == state for row in results)
        for state in ("DELETED", "READY", "SKIP", "ERROR")
    }
    summary = (
        f"Branch retirement: execute={execute}; "
        + ", ".join(f"{k.lower()}={v}" for k, v in counts.items())
    )
    print(summary)
    output = os.environ.get("GITHUB_STEP_SUMMARY")
    if output:
        with open(output, "a", encoding="utf-8") as file:
            file.write("## Audited orphan-branch cleanup\n\n" + summary + "\n\n")
            file.write("| Branch | Result | Detail |\n|---|---|---|\n")
            for name, state, detail in results:
                file.write(f"| \`{name}\` | {state} | {detail} |\n")
    return 1 if counts["ERROR"] else 0


if __name__ == "__main__":
    sys.exit(main())
