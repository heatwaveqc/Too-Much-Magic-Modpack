"""Source-only contract: Angler's Hat gated materials (#321)."""
import json
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
P = ROOT / "kubejs/data/reliquified_artifacts/tags/item/anglers_hat_valuables.json"

class AnglerHatGatedTreasures(unittest.TestCase):
    def test_approved_loot_members_without_replacing_prior(self):
        row = json.loads(P.read_text(encoding="utf-8"))
        self.assertIs(row["replace"], False)
        self.assertEqual(row["values"][0], {"id": "#c:raw_materials", "required": False})
        self.assertIn({"id": "dragonsurvival:elder_dragon_dust", "required": False}, row["values"])
        self.assertEqual(row["values"][-2:], [
            "draconicevolution:draconium_dust", "tmm:eldritch_logic_core"])
        self.assertEqual(len(row["values"]), 9)
        self.assertEqual(len({json.dumps(v, sort_keys=True) for v in row["values"]}), 9)

    def test_electric_core_item_id_resolves_to_existing_registration(self):
        source = (ROOT / "kubejs/startup_scripts/eldritch_logic_core.js").read_text(encoding="utf-8")
        miner = json.loads((ROOT / "kubejs/data/tmm/recipe/miner/eldritch_logic_core.json").read_text(encoding="utf-8"))
        self.assertIn("event.create('tmm:eldritch_logic_core')", source)
        self.assertEqual(miner["result"]["stack"]["id"], "tmm:eldritch_logic_core")

    def test_packwiz_installs_this_tag(self):
        s = (ROOT / "index.toml").read_text(encoding="utf-8")
        self.assertIn('file = "kubejs/data/reliquified_artifacts/tags/item/anglers_hat_valuables.json"', s)

if __name__ == "__main__":
    unittest.main()
