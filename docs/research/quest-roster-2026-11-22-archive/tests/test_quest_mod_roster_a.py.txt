"""Type-A source-only fixtures for the Packwiz quest mod roster audit."""
from __future__ import annotations

import hashlib
import json
import sys
import tempfile
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "scripts"))
from audit_quest_mod_roster import build_inventory, policy_for  # noqa: E402


class QuestModRosterATest(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.addCleanup(self.tmp.cleanup)
        self.root = Path(self.tmp.name)
        (self.root / "mods").mkdir()
        (self.root / "manifest").mkdir()
        self.entries = {}
        self.roster = [
            {"id": "aether", "packwiz_slug": "aether",
             "name": "The Aether", "category": "adventure", "enabled_in_pack": True},
            {"id": "example-lib", "name": "Example Library",
             "category": "dependency", "enabled_in_pack": True},
            {"id": "bountiful", "name": "Bountiful",
             "category": "qol", "enabled_in_pack": True},
            {"id": "unselected-test", "name": "Unselected Test",
             "category": "magic", "enabled_in_pack": False,
             "release_status": "experimental_release"},
        ]
        for slug in ["aether", "example-lib", "bountiful"]:
            raw = (
                f'name = "{slug}"\n'
                f'filename = "{slug}-1.0.jar"\n'
                'side = "both"\n'
                '[download]\n'
                'url = "https://example.invalid/example.jar"\n'
                'hash-format = "sha1"\n'
                'hash = "' + "0" * 40 + '"\n'
            ).encode()
            (self.root / f"mods/{slug}.pw.toml").write_bytes(raw)
            self.entries[slug] = hashlib.sha256(raw).hexdigest()
        self.write_baseline()

    def write_baseline(self):
        rows = ['hash-format = "sha256"\n']
        for slug, digest in sorted(self.entries.items()):
            rows += [
                "[[files]]",
                f'file = "mods/{slug}.pw.toml"',
                f'hash = "{digest}"',
                "metafile = true",
                "",
            ]
        index = "\n".join(rows).encode()
        (self.root / "index.toml").write_bytes(index)
        digest = hashlib.sha256(index).hexdigest()
        (self.root / "pack.toml").write_text(
            '[index]\nfile = "index.toml"\n'
            'hash-format = "sha256"\n'
            f'hash = "{digest}"\n'
            '[versions]\nminecraft = "1.21.1"\nneoforge = "21.1.265"\n'
        )
        (self.root / "manifest/mods.json").write_text(json.dumps({
            "date": "2026-11-15", "mods": self.roster,
        }))
        (self.root / "manifest/quest_chapters.json").write_text(json.dumps({
            "chapters": [{"name": "The Aether", "mod_ids": ["aether"]}],
        }))

    def test_full_selected_index_and_unindexed_separation(self):
        report = build_inventory(self.root)
        self.assertEqual(report["counts"]["indexed_third_party_mods"], 3)
        self.assertEqual(report["counts"]["not_indexed_catalog_entries"], 1)
        self.assertEqual([m["slug"] for m in report["mods"]],
                         ["aether", "bountiful", "example-lib"])
        aether = next(m for m in report["mods"] if m["slug"] == "aether")
        self.assertEqual(aether["existing_chapters"], ["The Aether"])
        self.assertEqual(aether["coverage"], "mapped")
        bountiful = next(m for m in report["mods"] if m["slug"] == "bountiful")
        self.assertEqual(bountiful["quest_policy"], "chapter_or_section")
        library = next(m for m in report["mods"] if m["slug"] == "example-lib")
        self.assertEqual(library["coverage"], "not_required")
        self.assertEqual(build_inventory(self.root), report)

    def test_technical_qol_exemptions(self):
        self.assertEqual(policy_for("clumps", "qol"),
                         "technical_inventory_only")
        self.assertEqual(policy_for("attributefix", "qol"),
                         "technical_inventory_only")
        self.assertEqual(policy_for("ftb-essentials", "qol"),
                         "technical_inventory_only")
        self.assertEqual(policy_for("comforts", "qol"), "tips_or_section")
        self.assertEqual(policy_for("bountiful", "qol"), "chapter_or_section")

    def test_roster_index_drift_rejected(self):
        self.roster[0]["enabled_in_pack"] = False
        self.write_baseline()
        with self.assertRaisesRegex(ValueError, "enablement mismatch"):
            build_inventory(self.root)

    def test_mismatched_index_file_hash_rejected(self):
        path = self.root / "mods/aether.pw.toml"
        path.write_bytes(path.read_bytes() + b"\n")
        with self.assertRaisesRegex(ValueError, "Index SHA-256 mismatch"):
            build_inventory(self.root)

    def test_mismatched_pack_index_hash_rejected(self):
        path = self.root / "index.toml"
        path.write_bytes(path.read_bytes() + b"\n")
        with self.assertRaisesRegex(ValueError, "pack.toml index hash"):
            build_inventory(self.root)

    def test_invalid_download_digest_rejected(self):
        path = self.root / "mods/aether.pw.toml"
        path.write_text(path.read_text().replace("0" * 40, "0" * 38))
        self.entries["aether"] = hashlib.sha256(path.read_bytes()).hexdigest()
        self.write_baseline()
        with self.assertRaisesRegex(ValueError, "Invalid exact mod download hash"):
            build_inventory(self.root)


if __name__ == "__main__":
    unittest.main()
