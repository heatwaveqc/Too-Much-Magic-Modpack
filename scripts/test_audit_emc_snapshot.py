#!/usr/bin/env python3
"""Fixture tests for #47 runtime snapshot consistency (not a game export)."""
from __future__ import annotations

import json
from pathlib import Path
from tempfile import TemporaryDirectory
import unittest

from audit_emc_snapshot import validate

SHA = "a" * 40


class EmcSnapshotTests(unittest.TestCase):
    def setUp(self):
        self.temp = TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.folder = Path(self.temp.name) / ("source-" + SHA[:12] + "-1")
        self.folder.mkdir()
        self.registry = [
            "bountiful:bounty", "minecraft:glass_pane", "minecraft:stick",
            "modx:missing", "modx:zero"
        ]
        self.mapped = {"minecraft:stick": {"emc": 4}, "modx:zero": {"emc": 0}}
        self.status = {
            "schema_version": 1, "registry_items": 5,
            "mapped_positive": 1, "mapped_zero": 1, "unmapped": 3,
            "unmapped_ids": ["bountiful:bounty", "minecraft:glass_pane", "modx:missing"],
            "mapped_zero_ids": ["modx:zero"]
        }
        self.meta = {
            "schema_version": 1, "source_sha_label": SHA,
            "source_sha_verified": False, "projecte_map_generation": 7,
            "captured_utc": "2026-11-23T00:00:00Z",
            "registry_items": 5, "mapped_positive": 1,
            "mapped_zero": 1, "unmapped": 3
        }
        self.write()

    def write(self):
        for name, obj in (
            ("items", {"items": self.registry}),
            ("emc", {"entries": self.mapped}),
            ("status", self.status), ("metadata", self.meta)
        ):
            (self.folder / f"{name}.json").write_text(
                json.dumps(obj, indent=2) + "\n", encoding="utf-8")

    def test_reports_missing_and_zero_separately_and_respects_approved_exceptions(self):
        report = validate(self.folder, SHA)
        self.assertEqual(["modx:missing", "modx:zero"], report["missing_ids"])
        self.assertEqual(["modx:missing", "minecraft:glass_pane", "bountiful:bounty"],
                         sorted(set(self.registry) - set(self.mapped)))
        self.assertEqual(1, report["unmapped_count"] - 2)  # two approved exceptions are unmapped
        self.assertEqual(1, report["mapped_zero_count"])
        self.assertEqual(report["approved_exceptions_in_registry"], 2)
        self.assertFalse(report["complete"])
        self.assertEqual(len(report["snapshot_file_sha256"]), 4)

    def test_source_sha_mismatch_is_rejected(self):
        with self.assertRaisesRegex(ValueError, "differs"):
            validate(self.folder, "b" * 40)

    def test_detects_missing_or_tampered_metadata(self):
        self.meta["unmapped"] = 0
        self.write()
        with self.assertRaisesRegex(ValueError, "unmapped"):
            validate(self.folder)

    def test_detects_missing_emc_id_in_status(self):
        self.status["mapped_zero_ids"] = []
        self.write()
        with self.assertRaisesRegex(ValueError, "status IDs"):
            validate(self.folder)

    def test_rejects_partial_exports(self):
        (self.folder / "emc.json").unlink()
        with self.assertRaisesRegex(ValueError, "Incomplete"):
            validate(self.folder)

    def test_rejects_incomplete_staging_directory(self):
        incomplete = self.folder.parent / "incomplete-abc"
        self.folder.rename(incomplete)
        with self.assertRaisesRegex(ValueError, "completed"):
            validate(incomplete)


if __name__ == "__main__":
    unittest.main()
