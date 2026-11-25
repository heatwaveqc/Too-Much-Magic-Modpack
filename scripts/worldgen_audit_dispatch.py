#!/usr/bin/env python3
"""Owner-only, one-shot worldgen pilot dispatcher; no server boot in this process."""
import datetime as dt
import json
import os
import re
import sys

import qa_dispatch_bridge as qa

ISSUE = 746
TOOLS = "9f10f668457c4596b26bb9124e847ca11e993bdf"
LAUNCHER = ".github/workflows/worldgen-audit-pilot.yml"
REQUIRED = {"Worldgen audit fixtures (offline)", "Verify bootstrap",
            "Python regression suite (NO GAME BOOT)", "Branch lifecycle report"}
# Reuse the canonical live-comment validator for this distinct, fixed console.
qa.ISSUE = ISSUE

def validate(event):
    issue, comment, repo = event.get("issue", {}), event.get("comment", {}), event.get("repository", {})
    if (event.get("action") != "created" or repo.get("full_name") != qa.REPO
        or repo.get("owner", {}).get("id") != qa.OWNER_ID
        or issue.get("number") != ISSUE or issue.get("state") != "open"
        or issue.get("pull_request") is not None or comment.get("author_association") != "OWNER"
        or not qa.owner(comment.get("user", {})) or not qa.owner(event.get("sender", {}))
        or not isinstance(comment.get("id"), int)):
        raise qa.Refused("Untrusted worldgen command")
    match = re.fullmatch(r"/tmm worldgen ([0-9a-f]{40}) ([0-9a-f]{32})", comment.get("body", "").strip())
    if not match or match[1] != TOOLS:
        raise qa.Refused("Expected exact reviewed worldgen tooling SHA and a fresh nonce")
    return match[2]

def main_head(api):
    return api("git/ref/heads/main")["object"]["sha"]

def check_ci(api):
    result = api(qa.query("actions/runs", head_sha=TOOLS, event="pull_request", per_page=100))
    runs = result["workflow_runs"]
    if result.get("total_count", len(runs)) > len(runs):
        raise qa.Refused("Truncated CI inventory")
    for name in REQUIRED:
        matches = [r for r in runs if r.get("name") == name and r.get("head_sha") == TOOLS]
        if not matches:
            raise qa.Refused("Missing exact tooling CI: " + name)
        run = max(matches, key=lambda x: (x["run_number"], x.get("run_attempt", 1), x["id"]))
        if run.get("status") != "completed" or run.get("conclusion") != "success":
            raise qa.Refused("Tooling check not green: " + name)

def check_active(api):
    for status in qa.ACTIVE:
        result = api(qa.query("actions/runs", event="workflow_dispatch", status=status, per_page=100))
        runs = result["workflow_runs"]
        if result.get("total_count", len(runs)) > len(runs):
            raise qa.Refused("Truncated active-run inventory")
        for run in runs:
            if qa.workflow_path(run) in (qa.LAUNCHER, qa.INTEGRATION, LAUNCHER):
                raise qa.Refused("Conflicting full run: " + str(run["id"]))

def execute(api, event, now, launcher_sha):
    nonce = validate(event)
    if not re.fullmatch(r"[0-9a-f]{40}", launcher_sha):
        raise qa.Refused("Invalid launcher SHA")
    qa.live_comment(api, event, now)
    if main_head(api) != launcher_sha:
        raise qa.Refused("Main advanced since authorization event")
    check_ci(api)
    check_active(api)
    if main_head(api) != launcher_sha:
        raise qa.Refused("Main advanced during checks")
    # Atomic create; no tag deletion or reuse, even on an ambiguous dispatch.
    api("git/refs", "POST", {"ref": "refs/tags/worldgen-request/" + nonce, "sha": launcher_sha})
    if main_head(api) != launcher_sha:
        raise qa.Refused("Main advanced after reservation; nonce consumed")
    check_active(api)
    api("actions/workflows/worldgen-audit-pilot.yml/dispatches", "POST",
        {"ref": "main", "inputs": {"authorized_build": "true",
         "expected_launcher_sha": launcher_sha, "request_nonce": nonce}})
    return nonce

def main():
    if os.environ.get("GITHUB_RUN_ATTEMPT") != "1":
        return 1
    event = json.load(open(os.environ["GITHUB_EVENT_PATH"], encoding="utf-8"))
    api = qa.API(os.environ["GH_TOKEN"])
    url = f"https://github.com/{qa.REPO}/actions/runs/{os.environ['GITHUB_RUN_ID']}"
    try:
        nonce = execute(api, event, dt.datetime.now(dt.timezone.utc), os.environ["GITHUB_SHA"])
        qa.report(api, f"Worldgen pilot DISPATCH-ACCEPTED: exact tools {TOOLS}, nonce {nonce}. "
                   f"Not a successful generation result. [Bridge evidence]({url}).")
        return 0
    except (qa.Refused, OSError, KeyError, ValueError) as e:
        print("REFUSED:", str(e), file=sys.stderr)
        try:
            validate(event)
            qa.report(api, f"Worldgen pilot refused or dispatch uncertain. No automatic retry. [Evidence]({url}).")
        except Exception:
            pass
        return 1

if __name__ == "__main__":
    raise SystemExit(main())
