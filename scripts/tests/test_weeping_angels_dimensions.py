"""Source-only unit tests. Run: python -m unittest discover -s scripts/tests -p test_weeping_angels_dimensions.py"""
from __future__ import annotations

import sys
import tempfile
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import audit_weeping_angels_dimensions as audit


class DimensionPolicyTests(unittest.TestCase):
    def test_source_config(self):
        banned = audit.source_check()
        self.assertTrue(set(audit.CONFIRMED_EXCLUSIONS) <= banned)
        self.assertIn("botania:skybox", banned)  # known resource, disabled in observed build
        self.assertFalse(banned & audit.ALLOWED)

    def test_live_census_is_covered_exactly(self):
        witness = Path(__file__).resolve().parents[2] / "docs/dimensions/weeping-angels-live-census-c56fb18f.json"
        loaded, sha = audit.read_census(witness)
        self.assertEqual(sha, "c56fb18ffc3b9f12f27317e7fb37f15c32576a8c")
        self.assertEqual(len(loaded), 19)
        banned = audit.source_check()
        self.assertEqual(len(loaded - audit.ALLOWED), 13)
        self.assertEqual(banned - (loaded - audit.ALLOWED), {"botania:skybox"})
        audit.check_loaded(loaded, banned)

    def test_current_unlisted_world_fails_closed(self):
        loaded, _ = audit.read_census(Path(__file__).resolve().parents[2] / "docs/dimensions/weeping-angels-live-census-c56fb18f.json")
        banned = audit.source_check() - {"witchery:mirror_world"}
        with self.assertRaisesRegex(audit.AuditError, "UNSAFE"):
            audit.check_loaded(loaded, banned)

    def test_exact_six_plus_blacklist_is_accepted(self):
        audit.check_loaded(set(audit.ALLOWED | audit.CONFIRMED_EXCLUSIONS),
                           set(audit.CONFIRMED_EXCLUSIONS))

    def test_unlisted_jawr_or_any_future_dimension_is_rejected(self):
        loaded = set(audit.ALLOWED | audit.CONFIRMED_EXCLUSIONS) | {"witchery:unexpected_realm"}
        with self.assertRaisesRegex(audit.AuditError, "UNSAFE"):
            audit.check_loaded(loaded, set(audit.CONFIRMED_EXCLUSIONS))

    def test_missing_approved_realm_is_rejected(self):
        loaded = set(audit.ALLOWED | audit.CONFIRMED_EXCLUSIONS) - {"aether:the_aether"}
        with self.assertRaisesRegex(audit.AuditError, "destination missing"):
            audit.check_loaded(loaded, set(audit.CONFIRMED_EXCLUSIONS))

    def test_invalid_or_duplicate_census_is_rejected(self):
        with self.assertRaises(audit.AuditError):
            audit.ids(["minecraft:overworld", "minecraft:overworld"], "census")
        with self.assertRaises(audit.AuditError):
            audit.ids(["minecraft:overworld", "BAD REALM"], "census")

    def test_static_jar_level_scan_does_not_claim_runtime_presence(self):
        import zipfile
        with tempfile.TemporaryDirectory() as d:
            path = Path(d)
            with zipfile.ZipFile(path / "fixture.jar", "w") as zf:
                zf.writestr("data/witchery/dimension/spirit_world.json", "{}")
                zf.writestr("data/witchery/dimension_type/spirit_world.json", "{}")
            self.assertEqual(audit.scan_jars(path), {"witchery:spirit_world"})


if __name__ == "__main__":
    unittest.main()
