#!/usr/bin/env python3
"""Unit tests for differential A-level QA; no game process or network."""
import hashlib
import importlib.util
import tempfile
import tomllib
import unittest
from pathlib import Path
from unittest.mock import patch

SCRIPT = Path(__file__).resolve().parents[1] / "scripts" / "qa_compare_channels.py"
spec = importlib.util.spec_from_file_location("qa_compare_channels", SCRIPT)
qa = importlib.util.module_from_spec(spec)
import sys
sys.modules[spec.name] = qa
spec.loader.exec_module(qa)


class OfflineDualBuildTests(unittest.TestCase):
    def test_differential_watch_ignores_unrelated_files(self):
        with tempfile.TemporaryDirectory() as tmp:
            a = Path(tmp) / "a"
            b = Path(tmp) / "b"
            for path in (a, b):
                (path / "config").mkdir(parents=True)
                (path / "config" / "thing.txt").write_text("same")
                (path / "docs.txt").write_text("old")
            checks = (
                qa.Check("X", "tracked", ("py", "scripts/validate.py"), ("config/**/*",)),
                qa.Check("Y", "docs", ("py", "scripts/validate.py"), ("docs.txt",)),
            )
            self.assertEqual(qa.select_repeats(a, b, checks), {"X": False, "Y": False})
            (b / "docs.txt").write_text("new")
            self.assertEqual(qa.select_repeats(a, b, checks), {"X": False, "Y": True})
            self.assertEqual(qa.select_repeats(a, b, checks, True), {"X": True, "Y": True})

    def test_source_content_not_mtime_controls_rerun(self):
        with tempfile.TemporaryDirectory() as tmp:
            a, b = Path(tmp) / "a", Path(tmp) / "b"
            a.mkdir()
            b.mkdir()
            (a / "x.txt").write_text("same")
            (b / "x.txt").write_text("same")
            self.assertEqual(qa.fingerprint(a, ("x.txt",)), qa.fingerprint(b, ("x.txt",)))
            (b / "x.txt").write_text("different")
            self.assertNotEqual(qa.fingerprint(a, ("x.txt",)), qa.fingerprint(b, ("x.txt",)))

    def make_pack(self, root):
        (root / "mods").mkdir()
        pin = 'name = "Neo Vitae"\nfilename = "neovitae-1.21.1-1.1.54.jar"\nside = "both"\n' \
              '[download]\nhash-format = "sha512"\nhash = "' + "a" * 128 + '"\n'
        (root / "mods" / "neovitae.pw.toml").write_text(pin)
        pin_hash = hashlib.sha256((root / "mods" / "neovitae.pw.toml").read_bytes()).hexdigest()
        index = 'hash-format = "sha256"\n[[files]]\nfile = "mods/neovitae.pw.toml"\n' \
                f'hash = "{pin_hash}"\nmetafile = true\n'
        (root / "index.toml").write_text(index)
        index_hash = hashlib.sha256((root / "index.toml").read_bytes()).hexdigest()
        (root / "pack.toml").write_text(
            '[index]\nfile = "index.toml"\nhash-format = "sha256"\n'
            f'hash = "{index_hash}"\n[versions]\nminecraft = "1.21.1"\nneoforge = "21.1.265"\n'
        )

    def test_inventory_hash_chain_fails_closed(self):
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp)
            self.make_pack(root)
            pins = qa.inventory(root)
            self.assertEqual(pins["neovitae"]["filename"], "neovitae-1.21.1-1.1.54.jar")
            with (root / "mods" / "neovitae.pw.toml").open("a") as output:
                output.write("\n# tampered\n")
            with self.assertRaisesRegex(ValueError, "stale mod pin"):
                qa.inventory(root)

    def test_index_tamper_fails_before_pin_read(self):
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp)
            self.make_pack(root)
            with (root / "index.toml").open("a") as output:
                output.write("# wrong hash\n")
            with self.assertRaisesRegex(ValueError, "index hash mismatch"):
                qa.inventory(root)

    def test_execution_allowlist_is_offline_python_only(self):
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp)
            (root / "test.py").write_text("print('offline fixture')\n")
            allowed = qa.Check("X", "fixture", ("py", "test.py"), ("test.py",))
            passed = qa.execute(root, allowed, 10)
            self.assertEqual(passed["status"], "PASS")
            self.assertIn("offline fixture", passed["output_tail"])
            refused = qa.Check("Y", "cannot boot", ("runServer",), ())
            with self.assertRaisesRegex(ValueError, "Only explicitly registered Python"):
                qa.execute(root, refused, 10)

    def test_all_builtin_checks_are_a_level_only(self):
        self.assertEqual(len({check.id for check in qa.CHECKS}), len(qa.CHECKS))
        for check in qa.CHECKS:
            self.assertEqual(check.command[0], "py")
            self.assertTrue(check.command[1].startswith(("scripts/", "tests/")))
            self.assertFalse(any(token in ("runServer", "smoke_server.sh",
                                           "assemble_integration.py", "build_full_integration_prism.py")
                                 for token in check.command))
        self.assertEqual(set(qa.FOUR_UPDATES), {"ars-elemental", "evilcraft", "goety", "neovitae"})


if __name__ == "__main__":
    unittest.main()
