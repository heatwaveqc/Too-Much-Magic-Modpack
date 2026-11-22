"""Synthetic tests for the offline FTB sidebar asset auditor (#507)."""
from __future__ import annotations

import json
from pathlib import Path
import sys
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "scripts"))
from audit_ftb_sidebar_assets import audit_sidebar_resources, icon_tokens, asset_overrides


class FtbSidebarAssetTests(unittest.TestCase):
    def test_real_png_and_unresolved_symbolic_icons_are_distinguished(self):
        fixture = {
            "ftb-library.jar": {
                "assets/ftblibrary/sidebar_buttons/toggle/day.json": json.dumps({
                    "icon": ["ftblibrary:textures/icons/toggle_day.png", "ftblibrary:icons/blue_button"],
                    "click": ["/ftblibrary day"],
                }).encode(),
                "assets/ftblibrary/textures/icons/toggle_day.png": b"",
            }
        }
        report = audit_sidebar_resources(fixture)
        self.assertEqual(len(report["sidebar_definitions"]), 1)
        self.assertFalse(report["missing_direct_textures"])
        self.assertEqual(report["symbolic_icons_needing_runtime_resolution"][0]["icons"],
                         ["ftblibrary:icons/blue_button"])

    def test_missing_literal_texture_is_reported(self):
        fixture = {"ftb.jar": {"assets/ftblibrary/sidebar_buttons/a.json":
                               b'{"icon":"ftblibrary:textures/icons/missing.png"}'}}
        report = audit_sidebar_resources(fixture)
        self.assertEqual(report["missing_direct_textures"][0]["missing"],
                         "assets/ftblibrary/textures/icons/missing.png")

    def test_bad_json_and_duplicate_assets_are_not_silent(self):
        fixture = {
            "a.jar": {
                "assets/ftblibrary/sidebar_buttons/broken.json": b"{broken",
                "assets/ftblibrary/textures/icons/icon.png": b"",
            },
            "b.jar": {"assets/ftblibrary/textures/icons/icon.png": b""},
        }
        report = audit_sidebar_resources(fixture)
        self.assertEqual(len(report["malformed_definitions"]), 1)
        self.assertEqual(len(report["ambiguous_asset_overrides"]), 1)

    def test_icon_field_only_and_nonobject_rejected(self):
        self.assertEqual(icon_tokens({"icon": ["a:b", {"alternatives": ["c:d"]}],
                                      "click": ["/danger"]}), ["a:b", "c:d"])
        with self.assertRaises(ValueError):
            icon_tokens(["a:b"])

    def test_pack_owned_ftb_asset_overrides(self):
        import tempfile
        with tempfile.TemporaryDirectory() as td:
            base = Path(td) / "kubejs" / "assets"
            (base / "ftblibrary" / "textures").mkdir(parents=True)
            (base / "ftblibrary" / "textures" / "foo.png").write_bytes(b"x")
            (base / "tmm").mkdir()
            (base / "tmm" / "bar.png").write_bytes(b"y")
            self.assertEqual(asset_overrides(Path(td)), ["ftblibrary/textures/foo.png"])


if __name__ == "__main__":
    unittest.main()
