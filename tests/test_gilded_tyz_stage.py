import json
import runpy
import tempfile
import unittest
from pathlib import Path


class TestGildedTyzStage(unittest.TestCase):
    def load(self):
        return runpy.run_path(
            str(Path(__file__).resolve().parents[1] / "compat/gilded/stage_gilded_tyz.py"),
            run_name="gilded_stage_test",
        )

    def test_overlay_contracts_are_distinct(self):
        skills, category = self.load()["verify_source"]()
        self.assertEqual(set(skills), {"tmm_gilded_fortune_mastery", "tmm_gilded_looting_mastery"})
        self.assertEqual(list(category), ["tmm_gilded"])
        for name, skill in skills.items():
            self.assertEqual(skill["id"], name)
            self.assertEqual(skill["maximumLevel"], 7)
            self.assertEqual(skill["prices"], [1] * 7)
            self.assertEqual(skill["customValues"]["power"]["values"], list(range(4, 11)))

    def mock_pack(self, root):
        (root / "mods").mkdir()
        (root / "mods/tyzs-skills.pw.toml").write_text("name = 'Tyz'\n")
        categories = root / "config/tyzs_skills/custom/data/categories.json"
        categories.parent.mkdir(parents=True)
        original = {key: {"displayName": key, "order": rank}
                    for rank, key in enumerate(
                        ["abilities", "combat", "misc", "nature", "tmm_magic_core", "tmm_monk"],1)}
        categories.write_text(json.dumps(original))
        monk = root / "config/tyzs_skills/custom/skills/tmm_monk_inner_fortune.json"
        monk.parent.mkdir(parents=True)
        monk.write_text('{"id":"tmm_monk_inner_fortune"}\n')
        return categories, original, monk

    def test_staging_preserves_other_categories_and_skills_and_is_idempotent(self):
        stage = self.load()["stage"]
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            categories, original, monk = self.mock_pack(root)
            stage(root)
            after = json.loads(categories.read_text())
            self.assertEqual(len(after),7)
            for key,value in original.items():
                self.assertEqual(after[key],value)
            self.assertTrue(monk.is_file())
            for key in ("tmm_gilded_fortune_mastery","tmm_gilded_looting_mastery"):
                data = json.loads((monk.parent / f"{key}.json").read_text())
                self.assertEqual(data["id"],key)
            stage(root)

    def test_conflicting_looting_skill_aborts_before_any_write(self):
        stage = self.load()["stage"]
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            categories, original, monk = self.mock_pack(root)
            before = categories.read_text()
            existing_looting=monk.parent/"tmm_gilded_looting_mastery.json"
            existing_looting.write_text('{"id":"customized_by_owner"}\n')
            with self.assertRaisesRegex(ValueError,"Conflicting Tyz skill"):
                stage(root)
            self.assertEqual(categories.read_text(),before)
            self.assertFalse((monk.parent/"tmm_gilded_fortune_mastery.json").exists())

    def test_fail_if_tyz_not_present(self):
        with tempfile.TemporaryDirectory() as temp:
            with self.assertRaises(ValueError):
                self.load()["stage"](Path(temp))


if __name__ == "__main__":
    unittest.main()
