"""Source-only guards for the six Botania 2.0.2 creative crafting routes (#596)."""
import json
import tempfile
import unittest
from pathlib import Path
from scripts.verify_botania_creative_bans import ROOT, BASE, EXPECTED, audit


class CreativeBanTest(unittest.TestCase):
    def test_repository_six_routes(self):
        self.assertEqual(6, len(EXPECTED))
        self.assertEqual([], audit(ROOT))
        for suffix in EXPECTED:
            self.assertTrue((ROOT / BASE / (suffix + ".json")).is_file())

    def test_native_infrangible_platform_unmodified(self):
        self.assertFalse((ROOT / BASE / "terra_plate/infrangible_platform.json").exists())

    def test_creative_component_variants_do_not_hide_under_normal_tablet_id(self):
        for suffix, value in (("terra_plate/creative_mana_tablet", 1),
                              ("creative_mana_tablet_extreme", True)):
            recipe = json.loads((ROOT / BASE / (suffix + ".json")).read_text(encoding="utf-8"))
            payload = recipe["result"]["components"]["minecraft:custom_data"]
            self.assertIs(type(payload["creative"]), type(value))
            self.assertEqual(value, payload["creative"])

    def test_condition_failure_detected(self):
        with tempfile.TemporaryDirectory() as folder:
            tmp = Path(folder)
            (tmp / "mods").mkdir()
            (tmp / "mods/botania-revived.pw.toml").write_bytes(
                (ROOT / "mods/botania-revived.pw.toml").read_bytes())
            for suffix in EXPECTED:
                path = tmp / BASE / (suffix + ".json")
                path.parent.mkdir(parents=True, exist_ok=True)
                path.write_bytes((ROOT / BASE / (suffix + ".json")).read_bytes())
            self.assertEqual([], audit(tmp))
            bad = tmp / BASE / "terra_plate/creative_mana_tablet.json"
            obj = json.loads(bad.read_text(encoding="utf-8"))
            obj["neoforge:conditions"] = []
            bad.write_text(json.dumps(obj), encoding="utf-8")
            self.assertTrue(any("conditions" in msg for msg in audit(tmp)))


if __name__ == "__main__":
    unittest.main()
