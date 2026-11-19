import unittest
import tomllib
from generate_reliquary_capture_config import TAG, render

SOURCE = '[items.mobCharm]\nentityBlockList = ["minecraft:wither"]\ndurability = 80\n[other]\nflag = true\n'

class GeneratorTest(unittest.TestCase):
    def snapshot(self):
        return {"schema": 1, "tag": TAG, "blocked_entity_ids": ["other:boss", "minecraft:warden"],
                "native_eligible_pairs": {"minecraft:zombie": "minecraft:zombie_spawn_egg"}}
    def test_exact_parity_and_preservation(self):
        result = tomllib.loads(render(self.snapshot(), SOURCE))
        self.assertEqual(result["items"]["mobCharm"]["entityBlockList"], ["minecraft:warden", "other:boss"])
        self.assertEqual(result["items"]["mobCharm"]["durability"], 80)
        self.assertTrue(result["other"]["flag"])
    def test_reject_conflicting_entity(self):
        data = self.snapshot()
        data["blocked_entity_ids"].append("minecraft:zombie")
        with self.assertRaises(ValueError): render(data, SOURCE)
    def test_reject_duplicate_entity(self):
        data = self.snapshot()
        data["blocked_entity_ids"].append("other:boss")
        with self.assertRaises(ValueError): render(data, SOURCE)
    def test_reject_missing_tag(self):
        data = self.snapshot()
        data["tag"] = "c:bosses"
        with self.assertRaises(ValueError): render(data, SOURCE)
    def test_reject_missing_assignment(self):
        with self.assertRaises(KeyError): render(self.snapshot(), "[other]\nflag = true\n")

if __name__ == "__main__":
    unittest.main()
