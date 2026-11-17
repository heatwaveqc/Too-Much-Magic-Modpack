#!/usr/bin/env python3
"""Owner-only QA dispatch; never evaluate event text as shell or code."""
from __future__ import annotations
import datetime as dt
import json
import os
import re
import sys
import urllib.error
import urllib.parse
import urllib.request
from dataclasses import dataclass

REPO = "heatwaveqc/Too-Much-Magic-Modpack"
OWNER = "heatwaveqc"
OWNER_ID = 22598785
ISSUE = 291
BRANCH = "test/full-integration"
LAUNCHER = ".github/workflows/full-integration-launcher.yml"
INTEGRATION = ".github/workflows/full-integration.yml"
BOOTSTRAP = ".github/workflows/verify.yml"
ACTIVE = ("queued", "in_progress", "waiting", "pending", "requested")
SHA = r"[0-9a-f]{40}"
NONCE = r"[0-9a-f]{32}"
class Refused(RuntimeError): pass

@dataclass(frozen=True)
class Request:
    mode: str
    sha: str
    nonce: str = ""

def parse(body: str) -> Request:
    if not isinstance(body, str): raise Refused("Non-text request")
    match = re.fullmatch(r"/tmm (smoke|prism) (" + SHA + r") (" + NONCE + r")", body.strip())
    if match: return Request(*match.groups())
    match = re.fullmatch(r"/tmm check (" + SHA + r")", body.strip())
    if match: return Request("check", match.group(1))
    raise Refused("Expected exactly one standalone /tmm check, smoke or prism command with complete lowercase SHA and nonce when required")

def owner(user: dict) -> bool:
    return user.get("login") == OWNER and user.get("id") == OWNER_ID and user.get("type") == "User"

def validate_event(event: dict) -> Request:
    issue = event.get("issue", {})
    comment = event.get("comment", {})
    repo = event.get("repository", {})
    if (event.get("action") != "created" or repo.get("full_name") != REPO
        or repo.get("owner", {}).get("id") != OWNER_ID
        or issue.get("number") != ISSUE or issue.get("state") != "open"
        or issue.get("pull_request") is not None or comment.get("author_association") != "OWNER"
        or not owner(comment.get("user", {})) or not owner(event.get("sender", {}))):
        raise Refused("Untrusted identity, issue, repository, or event")
    if not isinstance(comment.get("id"), int): raise Refused("Invalid comment ID")
    return parse(comment.get("body"))

class API:
    def __init__(self, token: str):
        if not token: raise Refused("Missing workflow token")
        self.token = token
    def __call__(self, path: str, method="GET", body=None):
        url = f"https://api.github.com/repos/{REPO}/{path}"
        headers = {"Authorization":"Bearer "+self.token, "Accept":"application/vnd.github+json",
                   "X-GitHub-Api-Version":"2022-11-28", "User-Agent":"TMM-QA-Dispatch-Bridge"}
        data = None if body is None else json.dumps(body).encode()
        if data is not None: headers["Content-Type"] = "application/json"
        try:
            with urllib.request.urlopen(urllib.request.Request(url, data=data, headers=headers, method=method), timeout=30) as res:
                raw = res.read()
                return json.loads(raw) if raw else {}
        except urllib.error.HTTPError as exc:
            # Do not echo arbitrary remote response content or credentials.
            raise Refused(f"GitHub API {method} returned HTTP {exc.code} for {path.split('?')[0]}") from None

def query(path, **fields):
    return path + "?" + urllib.parse.urlencode(fields)

def live_comment(api, event, now):
    c = event["comment"]
    current = api(f"issues/comments/{c['id']}")
    if (current.get("id") != c["id"] or current.get("body") != c["body"]
        or current.get("updated_at") != current.get("created_at")
        or current.get("author_association") != "OWNER" or not owner(current.get("user", {}))
        or current.get("issue_url") != f"https://api.github.com/repos/{REPO}/issues/{ISSUE}"):
        raise Refused("Comment was changed, removed or has incorrect provenance")
    timestamp = dt.datetime.fromisoformat(current["created_at"].replace("Z","+00:00"))
    if not dt.timedelta(0) <= now - timestamp <= dt.timedelta(minutes=30):
        raise Refused("Request expired; obtain fresh owner approval, no retry")
    issue = api(f"issues/{ISSUE}")
    if issue.get("state") != "open" or issue.get("pull_request") is not None:
        raise Refused("Command console is not an open ordinary issue")

def head(api):
    return api("git/ref/heads/" + BRANCH)["object"]["sha"]

def workflow_path(run):
    return run.get("path","").split("@",1)[0]

def verify_quick_ci(api, sha):
    result = api(query("actions/runs", branch=BRANCH, head_sha=sha, event="push", per_page=100))
    runs = result["workflow_runs"]
    evidence = {}
    for path in (BOOTSTRAP, INTEGRATION):
        found = [x for x in runs if workflow_path(x) == path and x.get("head_sha") == sha
                 and x.get("head_branch") == BRANCH and x.get("event") == "push"]
        if not found: raise Refused(f"No automatic quick CI for exact SHA: {path}")
        run = max(found, key=lambda x:(x["run_number"],x.get("run_attempt",1),x["id"]))
        if run.get("status") != "completed" or run.get("conclusion") != "success":
            raise Refused(f"Latest exact-SHA quick CI is not successful: {path}")
        jobs = api(query(f"actions/runs/{run['id']}/jobs",filter="latest",per_page=100))["jobs"]
        if path == BOOTSTRAP:
            if not any(j.get("name") == "metadata" and j.get("conclusion") == "success" for j in jobs):
                raise Refused("Required metadata job not green")
        else:
            job = next((j for j in jobs if j.get("name") == "integration-preflight-and-test" and j.get("conclusion") == "success"), None)
            if not job: raise Refused("Integration quick job not green")
            steps = {s["name"]:s.get("conclusion") for s in job.get("steps",[])}
            for name in ("Integration Preflight — frozen source refs, no writes or mod downloads",
                         "Compile and unit test all explicitly selected first-party modules",
                         "Offline regression for manifest-driven Prism packaging"):
                if steps.get(name) != "success": raise Refused("Missing quick-CI evidence: "+name)
            for name in ("Integration Assembly — explicitly authorized, never on automatic push",
                         "Full dedicated-server smoke with selected first-party runtime modules",
                         "Produce commit-locked all-workstreams Prism client ZIP"):
                if steps.get(name) != "skipped": raise Refused("Not a quick-only CI result: "+name)
        evidence[path] = run["id"]
    return evidence

def check_no_active(api):
    for status in ACTIVE:
        result = api(query("actions/runs",event="workflow_dispatch",status=status,per_page=100))
        if result.get("total_count",len(result["workflow_runs"])) > len(result["workflow_runs"]):
            raise Refused("Cannot establish active-run inventory from one page")
        for run in result["workflow_runs"]:
            if workflow_path(run) in (LAUNCHER, INTEGRATION):
                raise Refused(f"QA launcher already active (run {run['id']}); no concurrent dispatch")

def execute(api, event, now, audit=None):
    req = validate_event(event)
    live_comment(api,event,now)
    if head(api) != req.sha: raise Refused("Approved integration head is stale")
    evidence = verify_quick_ci(api,req.sha)
    check_no_active(api)
    if head(api) != req.sha: raise Refused("Integration advanced during validation")
    if req.mode == "check":
        return {"status":"CHECK-PASS","sha":req.sha,"quick_ci":evidence,"dispatched":False}
    # Atomically reserve one nonce in GitHub before dispatch. No automatic retry
    # or deletion of receipts even if dispatch fails or its response is lost.
    api("git/refs","POST",{"ref":"refs/tags/qa-request/"+req.nonce,"sha":req.sha})
    if audit is not None: audit("Reservation created (not a release/QA-tested tag): "+req.nonce)
    if head(api) != req.sha: raise Refused("Integration advanced after reservation; no dispatch, nonce consumed")
    check_no_active(api)
    result = api("actions/workflows/full-integration-launcher.yml/dispatches","POST",
                 {"ref":"main","inputs":{"full_smoke":"true",
                    "package_prism":"true" if req.mode=="prism" else "false",
                    "expected_integration_sha":req.sha,"qa_request_id":req.nonce}})
    return {"status":"DISPATCH-ACCEPTED","mode":req.mode,"sha":req.sha,
            "nonce":req.nonce,"quick_ci":evidence,"dispatched":True,"api_receipt":result}

def report(api, message):
    api(f"issues/{ISSUE}/comments","POST",{"body":message})

def main():
    event = json.load(open(os.environ["GITHUB_EVENT_PATH"],encoding="utf-8"))
    if os.environ.get("GITHUB_RUN_ATTEMPT") != "1":
        print("Refused: bridge rerun prohibited"); return 1
    api = API(os.environ["GH_TOKEN"])
    run = os.environ["GITHUB_RUN_ID"]
    link = f"https://github.com/{REPO}/actions/runs/{run}"
    comment = event.get("comment",{}).get("id")
    try:
        result = execute(api,event,dt.datetime.now(dt.timezone.utc),print)
        print(json.dumps(result,indent=2))
        report(api,f"QA bridge {result['status']}: integration `{result['sha']}`. "
                   + ("No build dispatched. " if not result["dispatched"] else f"Mode: **{result['mode']}**; nonce `{result['nonce']}`. Dispatch API accepted; verify downstream run before reporting completion. ")
                   + f"[Bridge audit]({link}); request comment #{comment}.")
        return 0
    except (Refused, OSError, KeyError, ValueError) as exc:
        # Trusted command only: avoid replying to unauthorized injected comments.
        print("REFUSED:",str(exc),file=sys.stderr)
        try:
            validate_event(event)
            report(api,f"QA bridge REFUSED or could not confirm dispatch for comment #{comment}. "
                       "Do not retry automatically; check audit and receipt first. "
                       f"[Audit]({link}).")
        except Exception: pass
        return 1

if __name__ == "__main__": raise SystemExit(main())
