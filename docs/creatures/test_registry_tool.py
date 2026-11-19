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
        self.assertEqual(21, len(self.tags))
        self.assertEqual(475, len(self.ids("hunt_eligible.json")))
        self.assertEqual(1219, len(self.ids("hunt_excluded.json")))
        self.assertTrue(self.ids("hunt_eligible.json").isdisjoint(self.ids("hunt_excluded.json")))
        self.assertEqual(69, len(set(self.data["entities"]) - self.ids("hunt_eligible.json") - self.ids("hunt_excluded.json")))
        self.assertEqual(58, len(self.ids("boss_generic_death_excluded.json")))
        self.assertEqual(56, len(self.ids("bosses.json")))
        self.assertEqual(20, len(self.ids("minibosses.json")))
        self.assertIn("minecraft:ender_dragon", self.ids("bosses.json") & self.ids("boss_generic_death_excluded.json"))
        self.assertIn("draconicevolution:draconic_guardian", self.ids("boss_generic_death_excluded.json"))

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

    def test_dragon_two_one_time_advancements_and_no_generic_payout(self):
        entry = self.data["entities"]["minecraft:ender_dragon"]
        self.assertEqual({
            "advancement_once:minecraft:end/kill_dragon:fixed_1000_mxp:zero_repeat",
            "advancement_once:minecraft:end/respawn_dragon:fixed_1000_mxp:zero_repeat",
        }, {r["rule"] for r in entry["instance_rules"] if r["consumer"] == "boss"})
        self.assertIn("minecraft:ender_dragon", self.ids("boss_generic_death_excluded.json"))
        self.assertIn("minecraft:ender_dragon", self.ids("hunt_eligible.json"))

    def test_special_native_receipts_do_not_promote_generic_deaths(self):
        rules = {"neovitae:daemonium_doloris":"native_isForeman_only:boss_neovitae_foreman","voidscape:null_servant":"native_getAugment_1_2_3:distinct_titanite_ichor_astral","twilightforest:knight_phantom":"native_postmortem_IT_IS_OVER_group_transition","the_bumblezone:cosmic_crystal_entity":"native_white_arena_success_reward_recipients","draconicevolution:draconic_guardian":"native_tickDeath_fight_manager_receipt:first_1000_fixed:repeat_100_fixed"}
        for entity_id, rule in rules.items():
            entry = self.data["entities"][entity_id]
            self.assertIn(entity_id, self.ids("boss_generic_death_excluded.json"))
            self.assertIn(rule, {r["rule"] for r in entry["instance_rules"] if r["consumer"] == "boss"})
        self.assertEqual("hold", self.data["entities"]["the_bumblezone:cosmic_crystal_entity"]["hunter"]["prey"])
        self.assertNotIn("the_bumblezone:cosmic_crystal_entity", self.ids("hunt_eligible.json"))

    def test_duplicate_keys_rejected(self):
        with self.assertRaises(ValueError):
            json.loads('{"a":1,"a":2}', object_pairs_hook=registry.no_duplicates)

if __name__ == "__main__":
    unittest.main()
