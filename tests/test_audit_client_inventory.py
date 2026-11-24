"""Offline fixtures for optional client-side installed-JAR QA audit.

No game launch, no network, no third-party JAR fetches.
"""
from __future__ import annotations

import hashlib
import json
from pathlib import Path
import sys
import tempfile
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "scripts"))
from audit_client_inventory import audit, read_manifest, InventoryError


class ClientInventoryAuditTests(unittest.TestCase):
    def setUp(self):
        temp = tempfile.TemporaryDirectory(prefix="tmm-client-audit-")
        self.addCleanup(temp.cleanup)
        self.dir = Path(temp.name)
        self.mods = self.dir / "mods"
        self.mods.mkdir()
        self.pack = self.dir / "tmm-qa-expected-mods.json"
        self.jar = self.mods / "example.jar"
        self.jar.write_bytes(b"client fixture")
        self.rows = [{
            "mod_id": "fixture",
            "filename": "example.jar",
            "side": "both",
            "algorithm": "sha256",
            "hash": hashlib.sha256(self.jar.read_bytes()).hexdigest()
        }]
        self.commit = "a" * 40
        self.save()

    def save(self):
        self.pack.write_text(json.dumps({
            "schema": 1, "source_commit": self.commit, "mods": self.rows
        }), encoding="utf-8")

    def test_exact_audit_succeeds(self):
        self.assertEqual(audit(self.dir), (self.commit, 1))

    def test_missing_or_extra_jar(self):
        self.jar.unlink()
        with self.assertRaisesRegex(InventoryError, "missing="):
            audit(self.dir)
        self.jar.write_bytes(b"client fixture")
        (self.mods / "unreviewed.jar").write_bytes(b"no")
        with self.assertRaisesRegex(InventoryError, "unexpected="):
            audit(self.dir)

    def test_modified_pinned_bytes(self):
        self.jar.write_bytes(b"altered")
        with self.assertRaisesRegex(InventoryError, "hash mismatch"):
            audit(self.dir)

    def test_unverified_or_invalid_manifest(self):
        self.pack.unlink()
        with self.assertRaisesRegex(InventoryError, "No packaged"):
            audit(self.dir)
        self.save()
        self.rows[0]["hash"] = "bad"
        self.save()
        with self.assertRaisesRegex(InventoryError, "pinned hash"):
            audit(self.dir)

    def test_path_traversal_and_duplicate_case_fails(self):
        self.rows[0]["filename"] = "../escape.jar"
        self.save()
        with self.assertRaisesRegex(InventoryError, "Unsafe"):
            read_manifest(self.pack)
        self.rows[0]["filename"] = "example.jar"
        self.rows.append(dict(self.rows[0], filename="EXAMPLE.JAR"))
        self.save()
        with self.assertRaisesRegex(InventoryError, "duplicate"):
            read_manifest(self.pack)

    def test_missing_commit_and_wrong_side_fails(self):
        self.commit = "not a SHA"
        self.save()
        with self.assertRaisesRegex(InventoryError, "immutable source"):
            audit(self.dir)
        self.commit = "a" * 40
        self.rows[0]["side"] = "server"
        self.save()
        with self.assertRaisesRegex(InventoryError, "client side"):
            audit(self.dir)

    def test_case_insensitive_windows_filename(self):
        self.jar.rename(self.mods / "EXAMPLE.JAR")
        self.assertEqual(audit(self.dir), (self.commit, 1))

    def test_sha512_pin(self):
        self.rows[0]["algorithm"] = "sha512"
        self.rows[0]["hash"] = hashlib.sha512(self.jar.read_bytes()).hexdigest()
        self.save()
        self.assertEqual(audit(self.dir), (self.commit, 1))


if __name__ == "__main__":
    unittest.main()
