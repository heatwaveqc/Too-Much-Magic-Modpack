import json
import runpy
import tempfile
import unittest
from pathlib import Path


class TestGildedTyzStage(unittest.TestCase):
    def load(self):
        module = runpy.run_path(
            str(Path(__file__).resolve().parents[1] / "compat/gilded/stage_gilded_tyz.py"),
            run_name="gilded_stage_test",
        )
        return module

    def test_overlay_contract(self):
        skill, patch = self.load()["verify_source"]()
        self.assertEqual(skill["maximumLevel"], 7)
        self.assertEqual(skill["customValues"]["power"]["values"], list(range(4, 11)))
        self.assertEqual(list(patch), ["tmm_gilded"])

    def test_staging_preserves_other_categories_and_skills(self):
        stage = self.load()["stage"]
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            (root / "mods").mkdir()
            (root / "mods/tyzs-skills.pw.toml").write_text("name = 'Tyz'\n")
            categories = root / "config/tyzs_skills/custom/data/categories.json"
            categories.parent.mkdir(parents=True)
            original = {k: {"displayName": k, "order": i} for i, k in enumerate(
                ["abilities", "combat", "misc", "nature", "tmm_magic_core", "tmm_monk"], 1)}
            categories.write_text(json.dumps(original))
            monk = root / "config/tyzs_skills/custom/skills/tmm_monk_inner_fortune.json"
            monk.parent.mkdir(parents=True)
            monk.write_text('{"id":"tmm_monk_inner_fortune"}\n')
            stage(root)
            after = json.loads(categories.read_text())
            self.assertEqual(len(after), 7)
            for key, value in original.items():
                self.assertEqual(after[key], value)
            self.assertTrue(monk.is_file())
            skill = json.loads((monk.parent/"tmm_gilded_fortune_mastery.json").read_text())
            self.assertEqual(skill["id"], "tmm_gilded_fortune_mastery")
            stage(root)  # idempotent

    def test_fail_if_tyz_not_present(self):
        stage = self.load()["stage"]
        with tempfile.TemporaryDirectory() as temp:
            with self.assertRaises(ValueError):
                stage(Path(temp))


if __name__ == "__main__":
    unittest.main()
