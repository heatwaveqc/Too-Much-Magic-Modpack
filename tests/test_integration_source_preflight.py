#!/usr/bin/env python3
"""Regression tests for read-only integration source-policy preflight."""
import json
import sys
import tempfile
import unittest
from pathlib import Path
from unittest.mock import patch

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "scripts"))
import validate_integration_sources as check  # noqa: E402

PIN = "a" * 40
HEAD = "b" * 40


class IntegrationPreflightTests(unittest.TestCase):
    def test_merged_source_uses_current_baseline_not_deleted_branch(self):
        stream = {"name": "monk", "branch": "test/full-integration",
                  "merged_from": "feature/deleted-after-merge",
                  "merged_commit": "f" * 40, "integrated_into_baseline": True}
        self.assertEqual(check.source_for(stream, HEAD), HEAD)

    def test_unfrozen_external_branch_is_rejected(self):
        with self.assertRaisesRegex(check.PreflightFailure, "immutable"):
            check.source_for({"name": "bad", "branch": "feature/moving"}, HEAD)

    def test_missing_pinned_object_is_rejected(self):
        stream = {"name": "missing", "branch": "feature/deleted",
                  "source_commit": PIN}
        with patch.object(check, "git", return_value=None):
            with self.assertRaisesRegex(check.PreflightFailure, "unavailable"):
                check.source_for(stream, HEAD)

    def test_override_requires_reason(self):
        stream = {"name": "s", "approved_overrides": {"compat/demo/old.java": "yes"}}
        with self.assertRaisesRegex(check.PreflightFailure, "meaningful"):
            check.approved_override(stream, "compat/demo/old.java")
        stream["approved_overrides"]["compat/demo/old.java"] = "Approved after source-level diff review"
        stream["expected_baseline_blobs"] = {"compat/demo/old.java": PIN}
        with patch.object(check, "git", return_value=PIN):
            self.assertTrue(check.approved_override(stream, "compat/demo/old.java"))
        with patch.object(check, "git", return_value=HEAD):
            with self.assertRaisesRegex(check.PreflightFailure, "baseline has changed"):
                check.approved_override(stream, "compat/demo/old.java")

    def test_drafts_reported_and_existing_overwrite_blocks_strict_assembly(self):
        cfg = {"schema": 1, "channel": "integration", "baseline": "test/full-integration",
               "streams": [{"name": "demo", "branch": "feature/demo",
                            "source_commit": PIN, "paths": ["compat/demo/"]}],
               "covered_by_baseline": {}, "superseded_or_non_runtime": {},
               "deferred_pending_runtime": {}}
        calls = []

        def fake_git(*args, allow_missing=False):
            calls.append(args)
            if args[:2] == ("rev-parse", "--verify") and args[2] == "HEAD":
                return HEAD
            if args == ("status", "--porcelain"):
                return ""
            if args[:2] == ("cat-file", "-e"):
                return ""
            if args[:3] == ("ls-tree", "-r", "--name-only"):
                return "compat/demo/example.java"
            if args[:2] == ("rev-parse", "--verify"):
                return "old-blob" if args[2].startswith("HEAD:") else "new-blob"
            if args[:2] == ("for-each-ref", "--format=%(refname:short)"):
                return "origin/feature/demo\norigin/feature/another-draft"
            raise AssertionError(f"Unexpected Git call {args}")

        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            (root / "manifest").mkdir()
            (root / "manifest/integration-sources.json").write_text(
                json.dumps(cfg), encoding="utf-8"
            )
            with patch.object(check, "ROOT", root), patch.object(check, "git", side_effect=fake_git):
                result = check.validate()
                self.assertEqual(result["unclassified_draft_branches"], ["feature/another-draft"])
                self.assertEqual(len(result["baseline_replacements_needing_review"]), 1)
                with self.assertRaisesRegex(check.PreflightFailure, "Unapproved baseline"):
                    check.validate(strict_overrides=True)
                with self.assertRaisesRegex(check.PreflightFailure, "Unreconciled active branches"):
                    check.validate(strict_readiness=True)
        for call in calls:
            self.assertNotIn(call[0], ("merge", "restore", "add", "commit", "reset"))
        self.assertNotIn(("git", "push"), calls)


if __name__ == "__main__":
    unittest.main()
