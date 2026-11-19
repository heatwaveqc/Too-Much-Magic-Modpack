"""Source-only registry tests; no game, network, or runtime writes."""
import json
import tempfile
import unittest
from pathlib import Path
import registry_tool as registry

class RegistryTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.data = registry.read()
        cls.tags = registry.projections(cls.data)

    def ids(self, tag):
        return {v if isinstance(v, str) else v["id"] for v in self.tags[tag]["values"]}

    def test_complete_projection(self):
        self.assertEqual(1763, len(self.data["entities"]))
        self.assertEqual(19, len(self.tags))
        self.assertEqual(475, len(self.ids("hunt_eligible.json")))
        self.assertEqual(1219, len(self.ids("hunt_excluded.json")))
        self.assertTrue(self.ids("hunt_eligible.json").isdisjoint(self.ids("hunt_excluded.json")))
        self.assertEqual(69, len(set(self.data["entities"]) - self.ids("hunt_eligible.json") - self.ids("hunt_excluded.json")))
        self.assertEqual(56, len(self.ids("boss_generic_death_excluded.json")))

    def test_overlapping_membership_and_independent_policy(self):
        self.assertIn("minecraft:drowned", self.ids("undead.json") & self.ids("aquatic.json"))
        self.assertIn("minecraft:ender_dragon", self.ids("dragon.json") & self.ids("void.json"))
        self.assertIn("minecraft:vex", self.ids("illager.json") & self.ids("hunt_eligible.json"))
        self.assertIn("minecraft:cow", self.ids("beast.json") & self.ids("hunt_excluded.json"))
        self.assertIn("the_bumblezone:cosmic_crystal_entity", self.ids("boss_generic_death_excluded.json"))
        self.assertNotIn("the_bumblezone:cosmic_crystal_entity", self.ids("hunt_eligible.json"))
        self.assertNotIn("minecraft:wither", self.ids("boss_generic_death_excluded.json"))
        self.assertEqual("unreviewed", registry.show(self.data, "minecraft:vex")["resources"]["blood"])
        self.assertEqual("blacklist_target", registry.show(self.data, "minecraft:vex")["capture"]["disposition"])

    def test_strict_drift_check(self):
        with tempfile.TemporaryDirectory() as directory:
            directory = Path(directory)
            for name, tag in self.tags.items():
                (directory / name).write_text(json.dumps(tag), encoding="utf-8")
            self.assertEqual([], registry.compare(self.data, directory))
            (directory / "beast.json").write_text('{"replace":false,"values":[]}', encoding="utf-8")
            (directory / "fae.json").unlink()
            self.assertEqual({"beast.json: differs", "fae.json: missing"}, set(registry.compare(self.data, directory)))

    def test_duplicate_keys_rejected(self):
        with self.assertRaises(ValueError):
            json.loads('{"a":1,"a":2}', object_pairs_hook=registry.no_duplicates)

if __name__ == "__main__":
    unittest.main()
