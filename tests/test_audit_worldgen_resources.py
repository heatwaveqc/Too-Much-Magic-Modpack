"""Synthetic, read-only worldgen inventory regressions; no game or download."""
import hashlib
import json
import sys
import tempfile
import unittest
import zipfile
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "scripts"))
from audit_worldgen_resources import inventory, parse_resource

class InventoryTests(unittest.TestCase):
    def test_comments_preserve_strings(self):
        raw = b'{// annotation\n"url":"https://test.invalid/a/*not-comment*/", /* comment */ "n": 7}'
        parsed, mode = parse_resource(raw)
        self.assertEqual(mode, 'comments-stripped')
        self.assertEqual(parsed, {'url':'https://test.invalid/a/*not-comment*/','n':7})
        with self.assertRaises(ValueError):
            parse_resource(b'{broken: 7}')

    def test_pins_placement_duplicates_and_missing(self):
        with tempfile.TemporaryDirectory() as d:
            root = Path(d)
            (root / "mods").mkdir()
            installed = root / "installed"
            installed.mkdir()
            jar = installed / "fixture.jar"
            name = "data/example/worldgen/structure_set/ruins.json"
            obj = {"placement": {"type": "minecraft:random_spread", "spacing": 32, "separation": 8},
                   "structures": [{"structure": "example:ruin", "weight": 1}]}
            with zipfile.ZipFile(jar, "w") as z:
                z.writestr(name, json.dumps(obj))
                z.writestr("data/example/worldgen/structure/ruin.json", '{"biomes":"#example:allowed"}')
                z.writestr("data/example/structure/ruin.nbt", b"fixture")
            sha = hashlib.sha256(jar.read_bytes()).hexdigest()
            pin = root / "mods/fixture.pw.toml"
            pin.write_text(f'name = "Fixture"\nfilename = "fixture.jar"\nside = "both"\n[download]\nhash-format = "sha256"\nhash = "{sha}"\n', encoding="utf-8")
            psha = hashlib.sha256(pin.read_bytes()).hexdigest()
            (root / "index.toml").write_text(f'hash-format = "sha256"\n[[files]]\nfile = "mods/fixture.pw.toml"\nhash = "{psha}"\n', encoding="utf-8")
            overlay = root / "overlay"
            (overlay / name).parent.mkdir(parents=True)
            (overlay / name).write_text(json.dumps(obj), encoding="utf-8")
            report = inventory(root, installed, data_roots=[overlay])
            self.assertEqual(report["counts"]["pins"], {"verified": 1})
            self.assertEqual(report["errors"], [])
            self.assertEqual(len(report["templates"]), 1)
            self.assertEqual(len(report["duplicate_definitions"]), 1)
            row = next(x for x in report["resources"] if x["kind"] == "worldgen/structure_set")
            self.assertAlmostEqual(row["attempts_per_million_blocks_before_filters"], 1_000_000 / 512**2)
            jar.unlink()
            self.assertEqual(inventory(root, installed)["counts"]["pins"], {"missing": 1})

if __name__ == "__main__":
    unittest.main()
