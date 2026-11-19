"""Offline safety and decision tests for branch lifecycle maintenance."""
import hashlib
import json
import os
from pathlib import Path
import subprocess
import tempfile
import unittest
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
                dict(ENTRY, reason=" "), dict(ENTRY, sha=B.upper())]
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


if __name__ == "__main__":
    unittest.main()
