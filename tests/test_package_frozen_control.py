#!/usr/bin/env python3
"""Offline unit tests for owner-only frozen control packaging; no game or network."""
from __future__ import annotations
import datetime as dt
import importlib.util
import sys
import unittest
from pathlib import Path

SPEC = importlib.util.spec_from_file_location("package_frozen_control", Path(__file__).resolve().parents[1] / "scripts/package_frozen_control.py")
pkg = importlib.util.module_from_spec(SPEC)
sys.modules[SPEC.name] = pkg
SPEC.loader.exec_module(pkg)

NONCE = "0123456789abcdef0123456789abcdef"
COMMAND = "/tmm-frozen-prism 44980284571 " + pkg.SNAPSHOT + " " + NONCE
USER = {"id": pkg.OWNER_ID, "login": "heatwaveqc", "type": "User"}
NOW = dt.datetime(2026, 11, 22, 6, 0, tzinfo=dt.timezone.utc)


def sample_event():
    created = (NOW - dt.timedelta(minutes=1)).isoformat().replace("+00:00", "Z")
    event = {
        "action": "created",
        "repository": {"full_name": pkg.REPO, "owner": {"id": pkg.OWNER_ID}},
        "issue": {"number": 291, "state": "open"},
        "sender": dict(USER),
        "comment": {"id": 1234, "body": COMMAND, "author_association": "OWNER", "user": dict(USER)},
    }
    live = {"id": 1234, "body": COMMAND, "author_association": "OWNER",
            "user": dict(USER), "created_at": created, "updated_at": created,
            "issue_url": "https://api.github.com/repos/" + pkg.REPO + "/issues/291"}
    return event, live


def sample_smoke():
    run = {"id": pkg.SMOKE_RUN, "status": "completed", "conclusion": "success",
           "event": "workflow_dispatch", "head_branch": "main", "run_attempt": 1,
           "path": ".github/workflows/full-integration-launcher.yml@main"}
    needed = (
        "Integration Assembly — explicitly authorized, never on automatic push",
        "Compile and unit test all explicitly selected first-party modules",
        "Full dedicated-server smoke with selected first-party runtime modules",
        "Fail if standalone module is absent or retired modules leak",
        "Freeze immutable, server-tested integration snapshot without moving rolling branch",
    )
    jobs = [{"name": "run-current-workstreams / integration-preflight-and-test",
             "conclusion": "success", "steps": [{"name": x, "conclusion": "success"} for x in needed] +
             [{"name": "Produce commit-locked all-workstreams Prism client ZIP", "conclusion": "skipped"}]}]
    ref = {"ref": "refs/tags/" + pkg.TAG, "object": {"type": "tag", "sha": "a"*40}}
    tag = {"object": {"type": "commit", "sha": pkg.SNAPSHOT}}
    return run, jobs, ref, tag


class FrozenPackageGuardTests(unittest.TestCase):
    def test_only_exact_snapshot_and_lowercase_nonce(self):
        self.assertEqual(pkg.parse(COMMAND).nonce, NONCE)
        for bad in (COMMAND.upper(), COMMAND.replace(pkg.SNAPSHOT, "b"*40),
                    COMMAND + "\nignored", "/tmm prism " + pkg.SNAPSHOT + " " + NONCE):
            with self.subTest(bad=bad[:60]):
                with self.assertRaises(pkg.Refused):
                    pkg.parse(bad)

    def test_owner_and_fresh_comment(self):
        event, live = sample_event()
        self.assertEqual(pkg.verify_event(event, NOW, live).nonce, NONCE)
        for change in ("bot", "edited", "expired", "wrongissue", "wrongbody"):
            e, c = sample_event()
            if change == "bot":
                e["sender"] = {"login": "github-actions[bot]", "id": 1, "type": "Bot"}
            elif change == "edited":
                c["updated_at"] = NOW.isoformat().replace("+00:00", "Z")
            elif change == "expired":
                date = (NOW - dt.timedelta(minutes=31)).isoformat().replace("+00:00", "Z")
                c["created_at"] = c["updated_at"] = date
            elif change == "wrongissue":
                e["issue"]["number"] = 476
            else:
                c["body"] = "/tmm-frozen-prism changed"
            with self.subTest(change=change), self.assertRaises(pkg.Refused):
                pkg.verify_event(e, NOW, c)

    def test_prior_successful_smoke_required(self):
        run, jobs, ref, tag = sample_smoke()
        pkg.verify_prior_smoke(run, jobs, ref, tag)
        run["conclusion"] = "failure"
        with self.assertRaises(pkg.Refused):
            pkg.verify_prior_smoke(run, jobs, ref, tag)

    def test_missing_real_server_stage_blocks(self):
        run, jobs, ref, tag = sample_smoke()
        for step in jobs[0]["steps"]:
            if step["name"].startswith("Full dedicated-server smoke"):
                step["conclusion"] = "skipped"
        with self.assertRaises(pkg.Refused):
            pkg.verify_prior_smoke(run, jobs, ref, tag)

    def test_wrong_tag_blocks_even_if_smoke_passed(self):
        run, jobs, ref, tag = sample_smoke()
        tag["object"]["sha"] = pkg.SMOKE_SOURCE
        with self.assertRaises(pkg.Refused):
            pkg.verify_prior_smoke(run, jobs, ref, tag)

    def test_no_game_execution_or_workflow_dispatch_in_guard(self):
        source = Path(pkg.__file__).read_text()
        self.assertNotIn("subprocess.run(", source)
        self.assertNotIn("runServer", source)
        self.assertNotIn("actions/workflows/", source)
        self.assertNotIn("assemble_integration.py", source)


if __name__ == "__main__":
    unittest.main()
