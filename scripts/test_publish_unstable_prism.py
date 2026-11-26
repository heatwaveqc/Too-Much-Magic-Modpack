#!/usr/bin/env python3
"""Offline publication trigger tests; no GitHub writes or Minecraft."""
import unittest
from unittest.mock import patch
import publish_unstable_prism as pub

class WaitForSourceTest(unittest.TestCase):
    def run_data(self, status="completed", conclusion="success", **changes):
        data = dict(id=123, status=status, conclusion=conclusion, event="workflow_dispatch",
                    head_branch="main", path=".github/workflows/full-integration-launcher.yml",
                    display_title="QA Prism abc")
        data.update(changes)
        return data

    @patch.object(pub.time, "sleep")
    @patch.object(pub, "api")
    def test_waits_for_terminal_success(self, api, sleep):
        api.side_effect = [self.run_data("in_progress", None), self.run_data()]
        pub.wait_for_source("123", attempts=2, delay=1)
        self.assertEqual(api.call_count, 2)
        sleep.assert_called_once_with(1)

    @patch.object(pub, "api")
    def test_terminal_failure_refused(self, api):
        api.return_value = self.run_data(conclusion="failure")
        with self.assertRaisesRegex(RuntimeError, "did not succeed"):
            pub.wait_for_source("123")

    @patch.object(pub, "api")
    def test_smoke_and_wrong_branch_refused(self, api):
        for change in (dict(display_title="QA Smoke abc"), dict(head_branch="other"),
                       dict(event="push"), dict(id=124)):
            api.return_value = self.run_data(**change)
            with self.assertRaisesRegex(RuntimeError, "canonical Prism launcher"):
                pub.wait_for_source("123")

    @patch.object(pub, "api")
    def test_timeout_refused(self, api):
        api.return_value = self.run_data("in_progress", None)
        with self.assertRaisesRegex(RuntimeError, "deadline"):
            pub.wait_for_source("123", attempts=1)

    @patch.object(pub, "api")
    def test_invalid_run_refused_without_api(self, api):
        with self.assertRaisesRegex(RuntimeError, "Invalid source"):
            pub.wait_for_source("123;bad")
        api.assert_not_called()

if __name__ == "__main__":
    unittest.main()
