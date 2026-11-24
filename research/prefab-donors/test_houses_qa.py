#!/usr/bin/env python3
"""Source-only tests for the disposable custom housing preview installer."""
import importlib.util
import tempfile
import unittest
from pathlib import Path

HERE = Path(__file__).resolve().parent
spec = importlib.util.spec_from_file_location("tmm_housing_qa", HERE / "stage_all_houses_qa.py")
module = importlib.util.module_from_spec(spec)
spec.loader.exec_module(module)


class HouseQATests(unittest.TestCase):
    def test_catalog_has_eighteen_unique_non_granted_houses(self):
        obj, houses = module.load_catalog()
        self.assertEqual(len(houses), 18)
        self.assertEqual(obj["native_generic"]["styles_count"], 15)
        self.assertTrue(all(h["grant"] is False for h in houses))
        self.assertTrue(all(h["addon_required"] is True for h in houses))
        self.assertEqual(len({h["filename"] for h in houses}), 18)
        self.assertEqual(len({h["id"] for h in houses}), 18)

    def test_rejects_unmarked_game_directory(self):
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp)
            source = root / "test.nbt"
            source.write_bytes(b"test")
            previews = [{"filename": "test.nbt", "_nbt_path": source}]
            with self.assertRaises(ValueError):
                module.stage_qa(previews, root, "server")
            self.assertFalse((root / "prefab-extension").exists())

    def test_idempotent_and_refuses_to_overwrite_blueprints(self):
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp)
            (root / module.DISPOSABLE_MARKER).write_text("DISPOSABLE QA ONLY\n")
            source = root / "test.nbt"
            source.write_bytes(b"source-bytes")
            previews = [{"filename": "test.nbt", "_nbt_path": source}]
            module.stage_qa(previews, root, "singleplayer")
            for sub in ("prefab-extension", "prefab-extension/server-cache"):
                self.assertEqual((root / sub / "test.nbt").read_bytes(), b"source-bytes")
            module.stage_qa(previews, root, "singleplayer")
            (root / "prefab-extension" / "test.nbt").write_bytes(b"another-build")
            with self.assertRaises(FileExistsError):
                module.stage_qa(previews, root, "singleplayer")

    def test_rejects_unsafe_file_names(self):
        for name in ("../bad.nbt", "/tmp/bad.nbt", "bad.zip", "bad\\name.nbt"):
            with self.subTest(name=name), self.assertRaises(ValueError):
                module.require_safe_filename(name, ".nbt")


if __name__ == "__main__":
    unittest.main()
