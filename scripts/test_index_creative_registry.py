#!/usr/bin/env python3
"""Unit tests for the source-only TMM creative registry census."""
import csv
import json
import sys
import tempfile
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from index_creative_registry import candidate_diff, inventory, read_metadata


class CreativeCensusTests(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.addCleanup(self.tmp.cleanup)
        base = Path(self.tmp.name)
        self.items = base / "items.csv"
        self.meta = base / "capture.json"
        self.capture = {
            "source": "qa/new-install/items.csv",
            "capture_date": "2026-11-23",
            "installed_build_sha": "a" * 40,
            "source_installed_jar_count": 251,
        }
        self.meta.write_text(json.dumps(self.capture), encoding="utf-8")
        with self.items.open("w", newline="", encoding="utf-8") as handle:
            w = csv.DictWriter(handle, fieldnames=["id", "name", "emc", "tags"])
            w.writeheader()
            w.writerows([
                {"id": "mod:creative_gizmo", "name": "Creative Gizmo", "emc": "0", "tags": ""},
                {"id": "mod:eternal_brick", "name": "Eternal Brick", "emc": "5", "tags": ""},
                {"id": "neovitae:blood_battery", "name": "Blood Battery", "emc": "0", "tags": ""},
                {"id": "botania:mana_tablet", "name": "Mana Tablet", "emc": "0", "tags": ""},
            ])

    def test_custom_capture_rejects_implicit_historical_metadata(self):
        with self.assertRaisesRegex(ValueError, "requires --metadata"):
            read_metadata(self.items, None)

    def test_custom_capture_preserves_exact_provenance(self):
        metadata = read_metadata(self.items, self.meta)
        result = inventory(self.items, metadata)
        self.assertEqual(result["capture_date"], "2026-11-23")
        self.assertEqual(result["installed_build_sha"], "a" * 40)
        self.assertEqual(result["source_installed_jar_count"], 251)
        self.assertEqual(result["registered_items_seen"], 4)
        self.assertEqual(result["counts"]["explicit_creative_or_cheat"], 1)
        self.assertEqual(result["counts"]["name_only_review"], 1)
        self.assertEqual(result["counts"]["source_confirmed_creative_candidates"], 1)
        self.assertTrue(result["source_confirmed_creative_candidates"][0]["registered_in_capture"])
        self.assertEqual(result["known_component_only_variants"][0]["base_item_id"], "botania:mana_tablet")

    def test_bad_metadata_refused(self):
        bad = dict(self.capture, installed_build_sha="not-a-sha")
        self.meta.write_text(json.dumps(bad), encoding="utf-8")
        with self.assertRaisesRegex(ValueError, "40 lowercase hex"):
            read_metadata(self.items, self.meta)

    def test_duplicate_registry_ids_refused(self):
        with self.items.open("a", encoding="utf-8") as handle:
            handle.write("mod:creative_gizmo,Again,0,\n")
        with self.assertRaisesRegex(ValueError, "Duplicate"):
            inventory(self.items, self.capture)

    def test_comparison_reports_differences_without_acquisition_claims(self):
        current = inventory(self.items, self.capture)
        earlier = {
            "source": "old/items.csv",
            "installed_build_sha": "b" * 40,
            "registered_items_seen": 2,
            "explicit_creative_or_cheat": [{"id": "mod:old_creative"}],
            "name_only_review": [],
        }
        delta = candidate_diff(current, earlier)
        self.assertEqual(delta["registered_item_delta"], 2)
        self.assertEqual(delta["candidates"]["explicit_creative_or_cheat"]["removed"], ["mod:old_creative"])
        self.assertEqual(delta["candidates"]["explicit_creative_or_cheat"]["added"], ["mod:creative_gizmo"])
        self.assertTrue(delta["comparison_only"])


if __name__ == "__main__":
    unittest.main()
