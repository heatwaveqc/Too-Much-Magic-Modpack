#!/usr/bin/env python3
"""Authorize one manual mod-update server smoke from an OWNER's exact GitHub comment.

This runs ONLY in the default-branch issue_comment workflow and performs no game
boot itself. All validation must succeed before the first GitHub write (receipt).
A receipt is an authorization/audit token, not evidence of a successful test.
"""
from __future__ import annotations

import datetime as dt
import json
import os
import re
import sys
import urllib.parse
import urllib.request

REPO = "heatwaveqc/Too-Much-Magic-Modpack"
OWNER_ID = 22598785
ISSUE = 291
SHA = r"[0-9a-f]{40}"
NONCE = r"[0-9a-f]{32}"
BRANCH = r"experiment/[a-z0-9][a-z0-9-]{4,75}"
COMMAND = re.compile(r"/tmm-update smoke (" + BRANCH + r") (" + SHA + r") (" + NONCE + r")\Z")
ACTIVE = ("queued", "in_progress", "waiting", "pending", "requested")
BLOCKED_WORKFLOWS = ("full-integration-launcher.yml", "full-integration.yml", "qa-update-smoke.yml")


class Refused(RuntimeError):
    pass


def parse(body: str) -> tuple[str, str, str]:
    if not isinstance(body, str) or "\n" in body or "\r" in body:
        raise Refused("Command must occupy exactly one line")
    match = COMMAND.fullmatch(body.strip())
    if not match:
        raise Refused("Expected /tmm-update smoke experiment/<branch> <40hex-sha> <32hex-nonce>")
    return match.groups()


def get(path: str, token: str, method: str = "GET", data: dict | None = None) -> dict:
    request = urllib.request.Request(
        "https://api.github.com/repos/" + REPO + "/" + path,
        method=method, headers={
            "Authorization": "Bearer " + token,
            "Accept": "application/vnd.github+json",
            "User-Agent": "TMM-Owner-Update-Smoke",
            "X-GitHub-Api-Version": "2022-11-28",
            **({"Content-Type": "application/json"} if data is not None else {}),
        }, data=None if data is None else json.dumps(data).encode("utf-8"),
    )
    try:
        with urllib.request.urlopen(request, timeout=30) as response:
            raw = response.read()
            return json.loads(raw) if raw else {}
    except urllib.error.HTTPError as exc:
        raise Refused(f"GitHub API request failed ({method}, status {exc.code}, resource {path.split('?')[0]})") from None


def owner(user: dict) -> bool:
    return (user.get("id") == OWNER_ID and user.get("login") == "heatwaveqc"
            and user.get("type") == "User")


def validate(event: dict, token: str, now: dt.datetime) -> tuple[str, str, str]:
    issue, comment = event.get("issue", {}), event.get("comment", {})
    repository = event.get("repository", {})
    if (event.get("action") != "created" or repository.get("full_name") != REPO
        or repository.get("owner", {}).get("id") != OWNER_ID or issue.get("number") != ISSUE
        or issue.get("state") != "open" or issue.get("pull_request") is not None
        or comment.get("author_association") != "OWNER" or not owner(comment.get("user", {}))
        or not owner(event.get("sender", {}))):
        raise Refused("Untrusted owner, issue or repository")
    branch, sha, nonce = parse(comment.get("body"))
    if not isinstance(comment.get("id"), int):
        raise Refused("Missing comment ID")
    live = get(f"issues/comments/{comment['id']}", token)
    if (live.get("body") != comment.get("body") or live.get("id") != comment["id"]
        or live.get("author_association") != "OWNER" or not owner(live.get("user", {}))
        or live.get("created_at") != live.get("updated_at")
        or live.get("issue_url") != f"https://api.github.com/repos/{REPO}/issues/{ISSUE}"):
        raise Refused("Edited or inconsistent owner comment")
    posted = dt.datetime.fromisoformat(live["created_at"].replace("Z", "+00:00"))
    if not (dt.timedelta(0) <= now - posted <= dt.timedelta(minutes=30)):
        raise Refused("Owner authorization comment expired")
    head = get("git/ref/heads/" + branch, token).get("object", {}).get("sha")
    if head != sha:
        raise Refused("Candidate branch HEAD differs from authorized immutable SHA")
    prs = get("pulls?" + urllib.parse.urlencode({"head": "heatwaveqc:" + branch, "state": "open", "per_page": 100}), token)
    if not isinstance(prs, list) or not any(
        p.get("head", {}).get("sha") == sha and p.get("base", {}).get("ref") == "test/full-integration"
        and p.get("head", {}).get("repo", {}).get("full_name") == REPO for p in prs
    ):
        raise Refused("Expected open candidate PR against test/full-integration at exact SHA")
    # A successful static/bootstrap run is a prerequisite, not a substitute for smoke.
    runs = get("actions/runs?" + urllib.parse.urlencode({
        "branch": branch, "head_sha": sha, "event": "push", "per_page": 100
    }), token).get("workflow_runs", [])
    verify = [r for r in runs if r.get("path", "").split("@", 1)[0].endswith("/verify.yml")
              and r.get("head_sha") == sha and r.get("head_branch") == branch]
    if not verify:
        raise Refused("No exact-candidate repository CI run")
    last = max(verify, key=lambda r: (r.get("run_number", 0), r.get("run_attempt", 1), r.get("id", 0)))
    if last.get("status") != "completed" or last.get("conclusion") != "success":
        raise Refused("Exact-candidate bootstrap CI not green")
    for status in ACTIVE:
        result = get("actions/runs?" + urllib.parse.urlencode(
            {"status": status, "per_page": 100}), token)
        runs = result.get("workflow_runs", [])
        if result.get("total_count", len(runs)) > len(runs):
            raise Refused("Cannot establish complete active-run inventory")
        for run in runs:
            if run.get("name") and (run.get("path", "").split("@", 1)[0].rsplit("/", 1)[-1]
                                    in BLOCKED_WORKFLOWS):
                raise Refused("Another full QA / update smoke is already active")
    if get("git/ref/heads/" + branch, token).get("object", {}).get("sha") != sha:
        raise Refused("Candidate HEAD advanced while checking")
    return branch, sha, nonce


def main() -> int:
    if os.getenv("GITHUB_RUN_ATTEMPT") != "1":
        raise Refused("Re-running update smoke command is prohibited")
    event = json.load(open(os.environ["GITHUB_EVENT_PATH"], encoding="utf-8"))
    token = os.environ["GH_TOKEN"]
    branch, sha, nonce = validate(event, token, dt.datetime.now(dt.timezone.utc))
    # Unique one-shot receipt, atomically create-only; never clean up or retry.
    get("git/refs", token, "POST", {"ref": "refs/tags/qa-update-request/" + nonce, "sha": sha})
    if get("git/ref/heads/" + branch, token).get("object", {}).get("sha") != sha:
        raise Refused("Candidate moved after receipt; authorization consumed, no smoke")
    output = os.environ["GITHUB_OUTPUT"]
    with open(output, "a", encoding="utf-8") as handle:
        for name, val in (("branch", branch), ("sha", sha), ("nonce", nonce)):
            handle.write(name + "=" + val + "\n")
    print(json.dumps({"status":"OWNER-AUTHORIZED", "branch":branch,
                      "candidate_sha":sha, "one_shot_nonce":nonce}))
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except (Refused, OSError, ValueError, KeyError) as exc:
        print("REFUSED:", str(exc), file=sys.stderr)
        raise SystemExit(1)
