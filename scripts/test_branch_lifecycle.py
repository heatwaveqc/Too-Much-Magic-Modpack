"""Offline safety and decision tests for branch lifecycle maintenance."""
import datetime as dt
import hashlib
import json
import os
from pathlib import Path
import subprocess
import tempfile
import unittest
import urllib.error
from unittest.mock import patch

import branch_lifecycle as lifecycle

A = "a" * 40
B = "b" * 40
PATH = "maintenance/branch-retirement/example.json"
ENTRY = {"name": "feature/example", "sha": A, "reason": "verified preserved",
         "evidence": ["https://github.com/" + lifecycle.REPO + "/pull/1"]}


def manifest(*entries):
    return json.dumps({"schema": 1, "batch": "example", "branches": list(entries)}).encode()


class LifecycleTests(unittest.TestCase):
    def test_budget_boundaries(self):
        self.assertEqual([lifecycle.branch_budget(n) for n in (0, 20, 21, 30, 31)],
                         ["HEALTHY", "HEALTHY", "REVIEW", "REVIEW", "MAINTENANCE"])

    def test_valid_manifest(self):
        self.assertEqual(lifecycle.validate_manifest(PATH, manifest(ENTRY)), [ENTRY])

    def test_invalid_manifests(self):
        bad = [dict(ENTRY, name=x) for x in lifecycle.PERMANENT |
               {"feature/../unsafe", "feature//unsafe", "feature/test.lock", "-unsafe"}]
        bad += [dict(ENTRY, sha="short"), dict(ENTRY, evidence=[]),
                dict(ENTRY, reason=" "), dict(ENTRY, sha=B.upper()),
                dict(ENTRY, archive_history="yes")]
        for row in bad:
            with self.subTest(row=row), self.assertRaises(ValueError):
                lifecycle.validate_manifest(PATH, manifest(row))
        for raw in (manifest(), manifest(ENTRY, ENTRY)):
            with self.assertRaises(ValueError):
                lifecycle.validate_manifest(PATH, raw)
        with self.assertRaises(ValueError):
            lifecycle.validate_manifest("../example.json", manifest(ENTRY))

    def test_hold_reasons(self):
        name = ENTRY["name"]
        branches = {name: {"commit": {"sha": A}, "protected": False}}
        self.assertIsNone(lifecycle.hold_reason(ENTRY, branches, set(), set()))
        self.assertEqual(lifecycle.hold_reason(ENTRY, {}, set(), set()), "already absent")
        self.assertEqual(lifecycle.hold_reason(ENTRY, branches, {name}, set()), "open PR head/base")
        self.assertEqual(lifecycle.hold_reason(ENTRY, branches, set(), {name}), "integration source dependency")
        branches[name]["commit"]["sha"] = B
        self.assertEqual(lifecycle.hold_reason(ENTRY, branches, set(), set()), "SHA changed")
        branches[name]["protected"] = True
        self.assertEqual(lifecycle.hold_reason(ENTRY, branches, set(), set()), "protected/permanent")

    def test_local_pr_refs_include_stacked_bases(self):
        def ref(name, repo):
            return {"ref": name, "repo": {"full_name": repo}}
        prs = [{"head": ref("feature/a", lifecycle.REPO), "base": ref("feature/b", lifecycle.REPO)},
               {"head": ref("fork-a", "other/repo"), "base": ref("main", lifecycle.REPO)}]
        self.assertEqual(lifecycle.local_pr_refs(prs), {"feature/a", "feature/b", "main"})

    def test_dry_run_never_pushes_and_execute_requires_authorization(self):
        raw = manifest(ENTRY)
        digest = hashlib.sha256(raw).hexdigest()
        state = ({ENTRY["name"]: {"commit": {"sha": A}}}, set(), set())
        with patch.object(Path, "read_bytes", return_value=raw), \
             patch.object(lifecycle, "snapshot", return_value=state), \
             patch.object(lifecycle, "report") as report, \
             patch.object(subprocess, "run") as run, \
             patch.dict(os.environ, {}, clear=True):
            lifecycle.retire(PATH, digest, False)
            self.assertIn("READY", " ".join(report.call_args.args[0]))
            run.assert_not_called()
            with self.assertRaises(ValueError):
                lifecycle.retire(PATH, digest, True)
            with self.assertRaises(ValueError):
                lifecycle.retire(PATH, "wrong", False)
            run.assert_not_called()

    def test_git_delete_lease_on_local_bare_repository(self):
        # Exercise actual Git ref transaction, with no network/real repository.
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            remote, work = root / "remote.git", root / "work"
            def git(*args, cwd=root, check=True):
                return subprocess.run(["git", *args], cwd=cwd, check=check,
                                      capture_output=True, text=True)
            git("init", "--bare", str(remote))
            git("init", str(work))
            git("config", "user.email", "test@example.invalid", cwd=work)
            git("config", "user.name", "Test", cwd=work)
            git("commit", "--allow-empty", "-m", "initial", cwd=work)
            git("remote", "add", "origin", str(remote), cwd=work)
            ref = "refs/heads/feature/fixture"
            first = git("rev-parse", "HEAD", cwd=work).stdout.strip()
            git("push", "origin", "HEAD:" + ref, cwd=work)
            git("commit", "--allow-empty", "-m", "advanced", cwd=work)
            second = git("rev-parse", "HEAD", cwd=work).stdout.strip()
            git("push", "origin", "HEAD:" + ref, cwd=work)
            stale = git("push", "--force-with-lease=" + ref + ":" + first, "origin", ":" + ref,
                        cwd=work, check=False)
            self.assertNotEqual(stale.returncode, 0)
            self.assertEqual(git("rev-parse", ref, cwd=remote).stdout.strip(), second)
            git("push", "--force-with-lease=" + ref + ":" + second, "origin", ":" + ref, cwd=work)
            self.assertNotEqual(git("show-ref", "--verify", ref, cwd=remote, check=False).returncode, 0)


class ConsoleTests(unittest.TestCase):
    digest = "c" * 64
    nonce = "d" * 32

    def setUp(self):
        self.env = {"GITHUB_REPOSITORY": lifecycle.REPO, "GITHUB_REF": "refs/heads/main",
                    "GITHUB_EVENT_NAME": "issue_comment", "GITHUB_ACTOR_ID": lifecycle.OWNER_ID,
                    "GITHUB_RUN_ATTEMPT": "1", "GITHUB_SHA": A, "GITHUB_EVENT_PATH": "event.json"}
        self.issue = {"number": lifecycle.RETIRE_CONSOLE, "state": "open"}
        stamp = dt.datetime.now(dt.timezone.utc).isoformat()
        self.comment = {"id": 10, "user": {"id": int(lifecycle.OWNER_ID)},
                        "author_association": "OWNER", "created_at": stamp, "updated_at": stamp,
                        "issue_url": "https://api.github.com/repos/" + lifecycle.REPO + "/issues/" + str(lifecycle.RETIRE_CONSOLE),
                        "body": f"/tmm retire {PATH} {self.digest} {self.nonce}"}
        self.event = {"action": "created", "repository": {"full_name": lifecycle.REPO, "default_branch": "main"},
                      "issue": self.issue, "sender": {"id": int(lifecycle.OWNER_ID)}, "comment": dict(self.comment)}
        self.responses = {f"/issues/{lifecycle.RETIRE_CONSOLE}": self.issue,
                          "/issues/comments/10": self.comment,
                          "/git/ref/heads/main": {"object": {"sha": A}}}

    def request(self):
        with patch.dict(os.environ, self.env, clear=True), \
             patch.object(Path, "read_bytes", return_value=json.dumps(self.event).encode()), \
             patch.object(lifecycle, "api", side_effect=lambda path: self.responses[path]), \
             patch.object(subprocess, "check_output", return_value=A):
            return lifecycle.console_request()

    def test_exact_commands(self):
        self.assertEqual(self.request(), (PATH, self.digest, self.nonce, A))
        self.assertEqual(lifecycle.parse_console(f"/tmm retire-check {PATH} {self.digest}"),
                         (PATH, self.digest, None))
        for bad in ("/tmm retire ../file.json " + self.digest + " " + self.nonce,
                    self.comment["body"] + "\n", self.comment["body"] + " extra",
                    self.comment["body"].replace(self.nonce, "short"),
                    self.comment["body"].replace(self.digest, self.digest.upper()),
                    self.comment["body"].replace("retire ", "retire-check "),
                    "/tmm retire-check " + PATH + " " + self.digest + " ; rm"):
            with self.subTest(bad=bad), self.assertRaises(ValueError):
                lifecycle.parse_console(bad)

    def test_reject_wrong_owner_issue_rerun_and_main(self):
        for key, value in (("GITHUB_ACTOR_ID", "5"), ("GITHUB_RUN_ATTEMPT", "2"),
                           ("GITHUB_REF", "refs/heads/feature/untrusted"),
                           ("GITHUB_REPOSITORY", "other/repo"), ("GITHUB_SHA", B)):
            original = self.env[key]
            self.env[key] = value
            with self.subTest(key=key), self.assertRaises(ValueError):
                self.request()
            self.env[key] = original
        self.event["issue"] = dict(self.issue, number=1)
        with self.assertRaises(ValueError):
            self.request()

    def test_reject_edited_deleted_stale_or_changed_commands(self):
        stamp = self.comment["updated_at"]
        self.comment["updated_at"] = "2000-01-01T00:00:00Z"
        with self.assertRaises(ValueError):
            self.request()
        self.comment["updated_at"] = stamp
        self.comment["body"] += " "
        with self.assertRaises(ValueError):
            self.request()
        self.comment["body"] = self.event["comment"]["body"]
        for record in (self.comment, self.event["comment"]):
            record["created_at"] = record["updated_at"] = "2000-01-01T00:00:00Z"
        with self.assertRaises(ValueError):
            self.request()
        del self.responses["/issues/comments/10"]
        with self.assertRaises(KeyError):
            self.request()

    def test_check_command_never_claims_nonce(self):
        with patch.object(lifecycle, "console_request", return_value=(PATH, self.digest, None, A)), \
             patch.object(lifecycle, "retire") as retire, patch.object(lifecycle, "claim_console_nonce") as claim:
            lifecycle.console()
            retire.assert_called_once_with(PATH, self.digest, False)
            claim.assert_not_called()

    def test_receipt_must_be_create_only_and_match(self):
        ref = "refs/tags/retirement-request/" + self.nonce
        with patch.object(lifecycle, "api", return_value={"ref": ref, "object": {"sha": A}}) as api, \
             patch.object(lifecycle, "report"):
            lifecycle.claim_console_nonce(self.nonce, A)
            api.assert_called_once_with("/git/refs", method="POST", data={"ref": ref, "sha": A})
        with patch.object(lifecycle, "api", side_effect=ValueError("already exists")):
            with self.assertRaises(ValueError):
                lifecycle.claim_console_nonce(self.nonce, A)

    def test_no_delete_on_wrong_manifest_or_failed_claim(self):
        raw = manifest(ENTRY)
        digest = hashlib.sha256(raw).hexdigest()
        with patch.dict(os.environ, self.env, clear=True), \
             patch.object(Path, "read_bytes", return_value=raw), \
             patch.object(subprocess, "check_output", return_value=raw), \
             patch.object(subprocess, "run") as run, \
             patch.object(lifecycle, "snapshot") as snapshot, \
             patch.object(lifecycle, "console_request", return_value=(PATH, self.digest, self.nonce, A)), \
             patch.object(lifecycle, "claim_console_nonce", side_effect=ValueError("duplicate nonce")) as claim:
            with self.assertRaises(ValueError):
                lifecycle.retire(PATH, digest, True)
            claim.assert_not_called()
            with patch.object(lifecycle, "console_request", return_value=(PATH, digest, self.nonce, A)):
                with self.assertRaises(ValueError):
                    lifecycle.retire(PATH, digest, True)
                claim.assert_called_once_with(self.nonce, A)
            snapshot.assert_not_called()
            run.assert_not_called()



class ArchiveTests(unittest.TestCase):
    def entry(self):
        return dict(ENTRY, archive_history=True)

    def test_preservation_tag_create_only(self):
        ref = "refs/tags/archive/retired/" + A
        endpoint = "/git/ref/tags/archive/retired/" + A
        result = {"ref": ref, "object": {"sha": A, "type": "commit"}}
        missing = urllib.error.HTTPError("https://example.invalid", 404, "not found", {}, None)
        with patch.object(lifecycle, "api", side_effect=[missing, result, result]) as api, \
             patch.object(lifecycle, "report"):
            lifecycle.preserve_history(self.entry())
            self.assertEqual(api.call_args_list[1].kwargs,
                             {"method": "POST", "data": {"ref": ref, "sha": A}})
        with patch.object(lifecycle, "api", side_effect=[result, result]) as api, \
             patch.object(lifecycle, "report"):
            lifecycle.preserve_history(self.entry())
            self.assertEqual([x.args for x in api.call_args_list], [(endpoint,), (endpoint,)])
        with patch.object(lifecycle, "api", return_value={"ref": ref, "object": {"sha": B, "type": "commit"}}) as api:
            with self.assertRaises(ValueError):
                lifecycle.preserve_history(self.entry())
            api.assert_called_once_with(endpoint)

    def test_preserve_before_any_delete_and_fail_closed(self):
        row = self.entry()
        raw = manifest(row)
        digest = hashlib.sha256(raw).hexdigest()
        env = {"GITHUB_REPOSITORY": lifecycle.REPO, "GITHUB_REF": "refs/heads/main",
               "GITHUB_EVENT_NAME": "workflow_dispatch", "GITHUB_ACTOR_ID": lifecycle.OWNER_ID,
               "GITHUB_RUN_ATTEMPT": "1", "RETIRE_AUTHORIZED": "true"}
        state = ({row["name"]: {"commit": {"sha": A}}}, set(), set())
        with patch.dict(os.environ, env, clear=True), \
             patch.object(Path, "read_bytes", return_value=raw), \
             patch.object(subprocess, "check_output", return_value=raw), \
             patch.object(lifecycle, "snapshot", return_value=state), \
             patch.object(lifecycle, "preserve_history", side_effect=ValueError("cannot verify tag")) as archive, \
             patch.object(subprocess, "run") as run, patch.object(lifecycle, "report"):
            lifecycle.retire(PATH, digest, False)
            archive.assert_not_called()
            with self.assertRaises(ValueError):
                lifecycle.retire(PATH, digest, True)
            archive.assert_called_once_with(row)
            run.assert_not_called()

    def test_new_dependency_after_preservation_holds_branch(self):
        row = self.entry()
        raw = manifest(row)
        digest = hashlib.sha256(raw).hexdigest()
        env = {"GITHUB_REPOSITORY": lifecycle.REPO, "GITHUB_REF": "refs/heads/main",
               "GITHUB_EVENT_NAME": "workflow_dispatch", "GITHUB_ACTOR_ID": lifecycle.OWNER_ID,
               "GITHUB_RUN_ATTEMPT": "1", "RETIRE_AUTHORIZED": "true"}
        branches = {row["name"]: {"commit": {"sha": A}}}
        safe, held = (branches, set(), set()), (branches, {row["name"]}, set())
        with patch.dict(os.environ, env, clear=True), \
             patch.object(Path, "read_bytes", return_value=raw), \
             patch.object(subprocess, "check_output", return_value=raw), \
             patch.object(lifecycle, "snapshot", side_effect=[safe, safe, held]), \
             patch.object(lifecycle, "preserve_history") as archive, \
             patch.object(subprocess, "run") as run, patch.object(lifecycle, "report") as report:
            lifecycle.retire(PATH, digest, True)
            archive.assert_called_once_with(row)
            run.assert_not_called()
            self.assertIn("SKIP", " ".join(report.call_args.args[0]))


if __name__ == "__main__":
    unittest.main()
