#!/usr/bin/env python3
"""Owner-authorized packaging ONLY of the previously server-smoked immutable TMM control.

No server boot, assembly, source promotion or rolling unstable release mutation.
The fixed smoke/tag make a previously tested artifact reproducible despite moving HEAD.
"""
from __future__ import annotations

import datetime as dt
import json
import os
import re
import urllib.error
import urllib.request
from dataclasses import dataclass

REPO = "heatwaveqc/Too-Much-Magic-Modpack"
OWNER_ID = 22598785
ISSUE = 291
SMOKE_RUN = 44980284571
SMOKE_SOURCE = "2373d50732866e460dc9e9454f7187f9045e1cdc"
SNAPSHOT = "c78479f52f9592ca1f139a6b874d630422c34042"
TAG = "integration-qa/44980284571-1"
COMMAND = re.compile(r"/tmm-frozen-prism 44980284571 " + SNAPSHOT + r" ([0-9a-f]{32})\Z")

class Refused(ValueError):
    """A guard failed; no packaging should be started."""


@dataclass(frozen=True)
class Request:
    nonce: str


def parse(body: str) -> Request:
    if not isinstance(body, str):
        raise Refused("Missing command")
    match = COMMAND.fullmatch(body.strip())
    if not match:
        raise Refused("Expected exact frozen snapshot command with 32 lowercase nonce")
    return Request(match.group(1))


def is_owner(user: dict) -> bool:
    return user.get("login") == "heatwaveqc" and user.get("id") == OWNER_ID and user.get("type") == "User"


def verify_event(event: dict, now: dt.datetime, live: dict) -> Request:
    issue, comment = event.get("issue", {}), event.get("comment", {})
    repo = event.get("repository", {})
    if (event.get("action") != "created" or repo.get("full_name") != REPO
        or repo.get("owner", {}).get("id") != OWNER_ID
        or issue.get("number") != ISSUE or issue.get("state") != "open"
        or issue.get("pull_request") is not None or comment.get("author_association") != "OWNER"
        or not is_owner(comment.get("user", {})) or not is_owner(event.get("sender", {}))):
        raise Refused("Owner, source issue or event not verified")
    if (not isinstance(comment.get("id"), int) or live.get("id") != comment["id"]
        or live.get("body") != comment.get("body")
        or live.get("updated_at") != live.get("created_at")
        or live.get("author_association") != "OWNER"
        or live.get("issue_url") != f"https://api.github.com/repos/{REPO}/issues/{ISSUE}"
        or not is_owner(live.get("user", {}))):
        raise Refused("Edited or untrusted comment")
    created = dt.datetime.fromisoformat(live["created_at"].replace("Z", "+00:00"))
    if not dt.timedelta(0) <= now - created <= dt.timedelta(minutes=30):
        raise Refused("Approval comment expired")
    return parse(comment.get("body"))


def verify_prior_smoke(run: dict, jobs: list, ref: dict, tag: dict) -> None:
    if (run.get("id") != SMOKE_RUN or run.get("status") != "completed"
        or run.get("conclusion") != "success" or run.get("event") != "workflow_dispatch"
        or run.get("head_branch") != "main"
        or run.get("path", "").split("@")[0] != ".github/workflows/full-integration-launcher.yml"
        or run.get("run_attempt") != 1):
        raise Refused("Historical owner-authorized smoke run did not pass")
    relevant = [j for j in jobs if j.get("name", "").endswith("integration-preflight-and-test")
                and j.get("conclusion") == "success"]
    if len(relevant) != 1:
        raise Refused("No unique passing prior full-integration job")
    stages = {s["name"]: s.get("conclusion") for s in relevant[0].get("steps", [])}
    required = (
        "Integration Assembly — explicitly authorized, never on automatic push",
        "Compile and unit test all explicitly selected first-party modules",
        "Full dedicated-server smoke with selected first-party runtime modules",
        "Fail if standalone module is absent or retired modules leak",
        "Freeze immutable, server-tested integration snapshot without moving rolling branch",
    )
    if any(stages.get(step) != "success" for step in required):
        raise Refused("Previously authorized smoke lacks required passing stage")
    if stages.get("Produce commit-locked all-workstreams Prism client ZIP") != "skipped":
        raise Refused("Prior run is not the expected smoke-only control")
    if (ref.get("object", {}).get("type") != "tag"
        or tag.get("object", {}).get("type") != "commit"
        or tag.get("object", {}).get("sha") != SNAPSHOT
        or ref.get("ref") != "refs/tags/" + TAG):
        raise Refused("Frozen smoke tag does not reference expected immutable snapshot")


def api(token: str, path: str, *, method: str = "GET", data=None):
    if not token:
        raise Refused("No GitHub Actions token")
    url = "https://api.github.com/repos/" + REPO + "/" + path
    body = json.dumps(data).encode() if data is not None else None
    headers = {"Accept": "application/vnd.github+json",
               "Authorization": "Bearer " + token, "User-Agent": "TMM-Frozen-Control-Packager"}
    if body is not None:
        headers["Content-Type"] = "application/json"
    try:
        with urllib.request.urlopen(
            urllib.request.Request(url, data=body, headers=headers, method=method),
            timeout=30,
        ) as response:
            raw = response.read()
            return json.loads(raw) if raw else {}
    except urllib.error.HTTPError as exc:
        raise Refused(f"GitHub API {method} refused {path.split('?')[0]} (HTTP {exc.code})") from None


def authorize() -> None:
    if os.environ.get("GITHUB_RUN_ATTEMPT") != "1":
        raise Refused("Cannot rerun an owner-authorized package workflow")
    event = json.loads(open(os.environ["GITHUB_EVENT_PATH"], encoding="utf-8").read())
    comment_id = event.get("comment", {}).get("id")
    if not isinstance(comment_id, int):
        raise Refused("Missing comment ID")
    token = os.environ.get("GH_TOKEN", "")
    live = api(token, f"issues/comments/{comment_id}")
    request = verify_event(event, dt.datetime.now(dt.timezone.utc), live)
    run = api(token, f"actions/runs/{SMOKE_RUN}")
    jobs = api(token, f"actions/runs/{SMOKE_RUN}/jobs?per_page=100")
    if jobs.get("total_count", 0) > len(jobs.get("jobs", [])):
        raise Refused("Prior smoke jobs pagination incomplete")
    ref = api(token, "git/ref/tags/" + TAG)
    tag = api(token, "git/tags/" + ref["object"]["sha"])
    verify_prior_smoke(run, jobs.get("jobs", []), ref, tag)
    # The prior run's referenced workflow is frozen at the reviewed source.
    refs = run.get("referenced_workflows", [])
    if not any(x.get("sha") == SMOKE_SOURCE for x in refs):
        raise Refused("Prior smoke was not sourced from the expected approved commit")
    # Nonce receipt is create-only, atomic and never deleted or retried.
    api(token, "git/refs", method="POST",
        data={"ref": "refs/tags/qa-frozen-prism/" + request.nonce, "sha": SNAPSHOT})
    print(f"Owner package authorization accepted. Prior smoke {SMOKE_RUN} PASS; frozen source {SNAPSHOT}; nonce consumed.")
    with open(os.environ["GITHUB_OUTPUT"], "a", encoding="utf-8") as output:
        output.write(f"snapshot={SNAPSHOT}\n")
        output.write(f"smoke_run={SMOKE_RUN}\n")


if __name__ == "__main__":
    try:
        authorize()
    except (Refused, KeyError, OSError, TypeError, json.JSONDecodeError) as error:
        raise SystemExit(f"REFUSED (no Prism build): {error}")
