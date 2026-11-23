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

    def test_packaged_atlas_source_resolves_map_and_tint(self):
        fixture = {
            "ftb-library.jar": {
                "assets/minecraft/atlases/blocks.json": json.dumps({"sources": [
                    {"type": "directory", "source": "icons", "prefix": "icons/"}
                ]}).encode(),
                "assets/ftblibrary/textures/icons/map.png": b"PNG stored separately",
            },
            "ftb-chunks.jar": {
                "assets/ftbchunks/sidebar_buttons/claim_chunks.json": json.dumps({
                    "icon": "ftblibrary:icons/map; tint=#FF80FFFF",
                    "click": ["ftbchunks:open_claim_gui"],
                }).encode(),
            },
        }
        report = audit_sidebar_resources(fixture)
        evidence = report["atlas_icon_evidence"]
        self.assertEqual(len(evidence), 1)
        self.assertEqual(evidence[0]["status"], "candidate_png_and_packaged_atlas_source_present")
        self.assertEqual(evidence[0]["candidate_sources"][0]["path"],
                         "assets/ftblibrary/textures/icons/map.png")
        self.assertFalse(report["missing_direct_textures"])

    def test_existing_texture_without_supplied_atlas_does_not_fail(self):
        fixture = {"ftb-quests.jar": {
            "assets/ftbquests/sidebar_buttons/quests.json": b'{"icon": "ftbquests:item/book"}',
            "assets/ftbquests/textures/item/book.png": b"PNG",
        }}
        report = audit_sidebar_resources(fixture)
        self.assertEqual(report["atlas_icon_evidence"][0]["status"],
                         "candidate_png_present_but_atlas_source_not_in_scanned_jars")
        self.assertEqual(report["missing_direct_textures"], [])

    def test_bad_atlas_is_reported(self):
        fixture = {"ftb-library.jar": {
            "assets/minecraft/atlases/blocks.json": b"{ malformed",
        }}
        report = audit_sidebar_resources(fixture)
        self.assertEqual(len(report["malformed_block_atlases"]), 1)

    def test_direct_texture_with_icon_properties(self):
        fixture = {"teams.jar": {
            "assets/ftbteams/sidebar_buttons/my_team.json":
                b'{"icon":"ftbteams:textures/teams.png; tint=#FFFFFFFF"}',
            "assets/ftbteams/textures/teams.png": b"PNG",
        }}
        report = audit_sidebar_resources(fixture)
        self.assertFalse(report["missing_direct_textures"])
        self.assertEqual(report["sidebar_definitions"][0]["direct_textures"],
                         ["assets/ftbteams/textures/teams.png"])

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
