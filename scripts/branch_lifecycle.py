#!/usr/bin/env python3
"""TMM frozen-donor helpers and historical exact-ref retirement."""
import argparse
import base64
import collections
import datetime as dt
import hashlib
import json
import os
from pathlib import Path
import re
import subprocess
import urllib.parse
import urllib.request

REPO = "heatwaveqc/Too-Much-Magic-Modpack"
OWNER_ID = "22598785"
PERMANENT = {"main", "test/full-integration", "dev/progression", "dev/art"}
SHA = re.compile(r"[0-9a-f]{40}")
BATCH = re.compile(r"maintenance/branch-retirement/[A-Za-z0-9][A-Za-z0-9_-]*\.json")
NAME = re.compile(r"(audit|build|design|dev|docs|feature|fix|integration|policy|research|stage|test|maintenance|change)/[A-Za-z0-9][A-Za-z0-9._/-]*")
NOW = dt.datetime.now(dt.timezone.utc)


def api(path):
    req = urllib.request.Request(
        "https://api.github.com/repos/" + REPO + path,
        headers={"Accept": "application/vnd.github+json",
                 "Authorization": "Bearer " + os.environ["GITHUB_TOKEN"],
                 "X-GitHub-Api-Version": "2022-11-28",
                 "User-Agent": "TMM-branch-lifecycle"})
    with urllib.request.urlopen(req, timeout=40) as response:
        return json.load(response)


def pages(path):
    rows = []
    for i in range(1, 101):
        part = api(path + ("&" if "?" in path else "?") + f"per_page=100&page={i}")
        if not isinstance(part, list):
            raise ValueError("expected array: " + path)
        rows.extend(part)
        if len(part) < 100:
            return rows
    raise ValueError("pagination limit reached: " + path)


def source_manifest():
    raw = api("/contents/manifest/integration-sources.json?ref=test%2Ffull-integration")
    if raw["encoding"] != "base64":
        raise ValueError("unsupported manifest encoding")
    doc = json.loads(base64.b64decode(raw["content"]))
    rows = doc["streams"]
    if not isinstance(rows, list):
        raise ValueError("invalid streams")
    for row in rows:
        if not isinstance(row.get("branch"), str) or not row["branch"]:
            raise ValueError("missing stream ref")
    return rows


def local_pr_refs(prs):
    # Even an external PR targeting our branch is a reason to keep that base.
    refs = set()
    for p in prs:
        for side in ("head", "base"):
            ref = p[side]
            repo = ref.get("repo")
            if repo is None or repo.get("full_name") == REPO:
                refs.add(ref["ref"])
    return refs


def hours(stamp):
    return (NOW - dt.datetime.fromisoformat(stamp.replace("Z", "+00:00"))).total_seconds() / 3600


def report(lines):
    body = "\n".join(str(line) for line in lines) + "\n"
    print(body, end="")
    if os.environ.get("GITHUB_STEP_SUMMARY"):
        with open(os.environ["GITHUB_STEP_SUMMARY"], "a", encoding="utf-8") as out:
            out.write(body)


def validate_manifest(path, raw):
    if not BATCH.fullmatch(path):
        raise ValueError("invalid batch path")
    data = json.loads(raw)
    if data.get("schema") != 1 or data.get("batch") != Path(path).stem:
        raise ValueError("schema or batch ID mismatch")
    entries = data["branches"]
    if not isinstance(entries, list) or not entries:
        raise ValueError("empty/nonlist batch")
    names = set()
    for row in entries:
        name, sha = row["name"], row["sha"]
        if (not NAME.fullmatch(name) or name in PERMANENT or name in names or
            ".." in name or "//" in name or name.endswith(("/", ".")) or
            any(part.startswith(".") or part.endswith(".lock") for part in name.split("/")) or
            not SHA.fullmatch(sha) or
            not isinstance(row.get("reason"), str) or not row["reason"].strip() or
            not isinstance(row.get("evidence"), list) or not row["evidence"] or
            any(not isinstance(link, str) or not link.startswith("https://github.com/" + REPO + "/") for link in row["evidence"])):
            raise ValueError("invalid or duplicate candidate: " + str(name))
        names.add(name)
    return entries


def hold_reason(entry, branches, pr_refs, donor_refs):
    name = entry["name"]
    b = branches.get(name)
    if b is None:
        return "already absent"
    if b.get("protected") or name in PERMANENT:
        return "protected/permanent"
    if name in pr_refs:
        return "open PR head/base"
    if name in donor_refs:
        return "integration source dependency"
    if b["commit"]["sha"] != entry["sha"]:
        return "SHA changed"
    return None


def snapshot():
    return ({b["name"]: b for b in pages("/branches")},
            local_pr_refs(pages("/pulls?state=open")),
            {r["branch"] for r in source_manifest()})


def retire(path, digest, execute):
    raw = Path(path).read_bytes() if BATCH.fullmatch(path) else b""
    actual_digest = hashlib.sha256(raw).hexdigest()
    if digest != actual_digest:
        raise ValueError("manifest SHA-256 mismatch")
    entries = validate_manifest(path, raw)
    if execute:
        expected = {"GITHUB_REPOSITORY": REPO, "GITHUB_REF": "refs/heads/main",
                    "GITHUB_EVENT_NAME": "workflow_dispatch", "GITHUB_ACTOR_ID": OWNER_ID,
                    "GITHUB_RUN_ATTEMPT": "1", "RETIRE_AUTHORIZED": "true"}
        if any(os.environ.get(k) != v for k, v in expected.items()):
            raise ValueError("retirement requires a fresh owner-authorized main dispatch")
        # Ensure the exact reviewed bytes are tracked at the dispatched commit.
        committed = subprocess.check_output(["git", "show", "HEAD:" + path])
        if committed != raw:
            raise ValueError("manifest differs from checked-out main commit")
    state = snapshot()
    lines = [f"## Retirement batch {Path(path).stem} — {'EXECUTE' if execute else 'DRY RUN'}",
             f"SHA-256: {actual_digest}", "", "| Branch | Result | Detail |", "|---|---|---|"]
    errors = 0
    for entry in entries:
        name, sha = entry["name"], entry["sha"]
        # Refresh the entire live dependency snapshot immediately before each delete.
        if execute:
            state = snapshot()
        reason = hold_reason(entry, *state)
        if reason:
            status = "SKIP"
        elif not execute:
            status, reason = "READY", "dry run only"
        else:
            ref = "refs/heads/" + name
            result = subprocess.run(["git", "push", "--force-with-lease=" + ref + ":" + sha,
                                     "origin", ":" + ref], capture_output=True, text=True)
            if result.returncode != 0:
                status, reason = "ERROR", "lease or remote rejection; inspect action log"
                print(result.stderr)
                errors += 1
            else:
                status, reason = "DELETED", sha
        lines.append(f"| {name} | {status} | {reason} |")
    report(lines)
    if errors:
        raise SystemExit(1)


def main():
    p = argparse.ArgumentParser()
    sub = p.add_subparsers(dest="command", required=True)
    r = sub.add_parser("retire")
    r.add_argument("--manifest", required=True)
    r.add_argument("--digest", required=True)
    r.add_argument("--execute", action="store_true")
    args = p.parse_args()
    retire(args.manifest, args.digest, args.execute)


if __name__ == "__main__":
    main()
