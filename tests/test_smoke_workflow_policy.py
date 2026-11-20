"""Regression tests for the #260 B14 documentation-PR game-boot incident."""
from __future__ import annotations

import sys
import tempfile
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "scripts"))
from audit_smoke_workflows import audit, scan_workflow  # noqa: E402

HEADER = """name: Sample
on:
  push:
  pull_request:
  workflow_dispatch:
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
"""


class SmokeWorkflowPolicyTest(unittest.TestCase):
    def test_unguarded_b14_smoke_fails(self):
        source = HEADER + "      - name: boot\n        run: bash scripts/smoke_server.sh\n"
        self.assertTrue(scan_workflow(source, "b14-cc-eldritch-iesnium.yml"))

    def test_dispatch_without_flag_fails(self):
        source = HEADER + """      - name: boot
        if: github.event_name == 'workflow_dispatch'
        run: bash scripts/smoke_server.sh
"""
        self.assertTrue(scan_workflow(source, "b14-cc-eldritch-iesnium.yml"))

    def test_flag_without_dispatch_fails(self):
        source = HEADER + """      - name: boot
        if: inputs.full_smoke == true
        run: bash scripts/smoke_server.sh
"""
        self.assertTrue(scan_workflow(source, "b14-cc-eldritch-iesnium.yml"))

    def test_or_bypass_fails(self):
        source = HEADER + """      - name: boot
        if: github.event_name == 'workflow_dispatch' || inputs.full_smoke == true
        run: bash scripts/smoke_server.sh
"""
        self.assertTrue(scan_workflow(source, "b14-cc-eldritch-iesnium.yml"))

    def test_manually_authorized_b14_step(self):
        source = HEADER + """      - name: source check
        run: python3 scripts/validate.py
      - name: boot
        if: github.event_name == 'workflow_dispatch' && inputs.full_smoke == true
        run: bash scripts/smoke_server.sh
"""
        self.assertEqual(scan_workflow(source, "b14-cc-eldritch-iesnium.yml"), [])

    def test_manual_job_gate_suffices_for_prism(self):
        source = """name: Sample
on:
  workflow_dispatch:
jobs:
  package:
    if: github.event_name == 'workflow_dispatch' && inputs.authorized_build == true
    runs-on: ubuntu-latest
    steps:
      - run: python3 scripts/build_b6_prism.py --test
"""
        self.assertEqual(scan_workflow(source, "b6-prism-download.yml"), [])

    def test_inline_run_with_unguarded_prism_fails(self):
        source = HEADER + "      - run: python3 scripts/build_b6_prism.py --test\\n"
        self.assertTrue(scan_workflow(source, "b6-prism-download.yml"))

    def test_source_compile_is_not_client_packaging(self):
        source = HEADER + """      - name: compile only
        run: python3 -m py_compile scripts/build_b6_prism.py
"""
        self.assertEqual(scan_workflow(source, "b12-qol-relics.yml"), [])

    def test_guard_on_other_step_does_not_protect_boot(self):
        source = HEADER + """      - name: check
        if: github.event_name == 'workflow_dispatch' && inputs.full_smoke == true
        run: python3 scripts/validate.py
      - name: boot
        run: bash scripts/smoke_server.sh
"""
        self.assertTrue(scan_workflow(source, "b14-cc-eldritch-iesnium.yml"))

    def test_missing_b14_fails_closed(self):
        with tempfile.TemporaryDirectory() as temp:
            path = Path(temp) / ".github/workflows"
            path.mkdir(parents=True)
            (path / "b12-demo.yml").write_text(HEADER, "utf-8")
            self.assertTrue(audit(Path(temp))[1])

    def test_repository_current_workflows(self):
        checked, errors = audit(ROOT)
        self.assertGreaterEqual(checked, 14)
        self.assertEqual(errors, [])


if __name__ == "__main__":
    unittest.main()
