"""Frozen source contract for approved #384 recovered next-QA QoL intake (Type A only)."""
from pathlib import Path
import json
import tomllib
import unittest

ROOT = Path(__file__).resolve().parents[1]
PIN = ROOT / "mods/swing-through-grass.pw.toml"

class SwingThroughGrassIntake(unittest.TestCase):
    def test_exact_approved_provider_pin(self):
        item = tomllib.loads(PIN.read_text(encoding="utf-8"))
        self.assertEqual(item["name"], "Swing Through Grass")
        self.assertEqual(item["filename"], "swingthroughgrass-neoforge-1.21.1-1.1.0.jar")
        self.assertEqual(item["side"], "both")
        self.assertEqual(item["download"], {"hash-format": "sha1", "hash": "15f48ed3ab02b9e8763284279487a9d95b206337", "mode": "metadata:curseforge"})
        self.assertEqual(item["update"]["curseforge"], {"project-id": 1477279, "file-id": 9063127})

    def test_manifest_and_next_qa_match(self):
        mod = json.loads((ROOT / "manifest/mods.json").read_text(encoding="utf-8"))
        qa = json.loads((ROOT / "manifest/next-qa-target.json").read_text(encoding="utf-8"))
        rows = [row for row in mod["mods"] if row["id"] == "swing-through-grass"]
        self.assertEqual(len(rows), 1)
        self.assertTrue(rows[0]["enabled_in_pack"])
        self.assertEqual(rows[0]["packwiz_slug"], "swing-through-grass")
        self.assertIn("swing-through-grass", qa["required_mod_ids"])
        self.assertIn('file = "mods/swing-through-grass.pw.toml"',
                      (ROOT / "index.toml").read_text(encoding="utf-8"))

    def test_other_two_approved_qol_edits_not_replaced(self):
        src = (ROOT / "kubejs/startup_scripts/vanilla_item_qol.js").read_text(encoding="utf-8")
        self.assertIn("minecraft:blaze_rod", src)
        self.assertIn("setFireResistant()", src)
        self.assertIn("minecraft:mushroom_stew", src)
        self.assertIn("setMaxStackSize(16)", src)

if __name__ == "__main__":
    unittest.main()
