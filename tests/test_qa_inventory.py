import hashlib
import json
from pathlib import Path
import sys
import tempfile
import unittest

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "scripts"))
from verify_qa_inventory import inventory, target_report, for_side, verify_directory


class InventoryTest(unittest.TestCase):
    def test_missing_wrong_bytes_and_side(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            (root / "mods").mkdir()
            (root / "manifest").mkdir()
            rows, entries = [], []
            for mid, side in (("dragon", "both"), ("hearts", "client"), ("serveronly", "server")):
                rows.append({"id": mid, "desired": True, "enabled_in_pack": True})
                pin = root / "mods" / (mid + ".pw.toml")
                h = hashlib.sha256(mid.encode()).hexdigest()
                pin.write_text(f'filename = "{mid}.jar"\nside = "{side}"\n[download]\nhash-format = "sha256"\nhash = "{h}"\n')
                entries.append(f'[[files]]\nfile = "mods/{mid}.pw.toml"\nmetafile = true\nhash = "{hashlib.sha256(pin.read_bytes()).hexdigest()}"\n')
            index = root / "index.toml"
            index.write_text('hash-format = "sha256"\n' + "\n".join(entries))
            (root / "pack.toml").write_text('[index]\nfile = "index.toml"\nhash-format = "sha256"\nhash = "' + hashlib.sha256(index.read_bytes()).hexdigest() + '"\n')
            (root / "manifest/mods.json").write_text(json.dumps({"mods": rows + [{"id":"missing", "desired":True, "enabled_in_pack":False}]}))
            (root / "manifest/first-party-runtime.json").write_text('{"modules":[{"mod_id":"custom"}]}')
            target = {"schema":1, "required_mod_ids":["dragon","hearts","serveronly","missing"],"required_first_party_mod_ids":["custom"]}
            (root / "manifest/next-qa-target.json").write_text(json.dumps(target))
            self.assertEqual(target_report(root)["missing"], ["missing"])
            with self.assertRaises(ValueError):
                target_report(root, True)
            target["required_mod_ids"].remove("missing")
            (root / "manifest/next-qa-target.json").write_text(json.dumps(target))
            target_report(root, True)
            mods = root / "installed"
            mods.mkdir()
            extra = root / "custom.jar"
            extra.write_bytes(b"custom")
            server = for_side(inventory(root), "server", [extra])
            self.assertEqual({x["filename"] for x in server}, {"dragon.jar","serveronly.jar","custom.jar"})
            self.assertEqual({x["filename"] for x in for_side(inventory(root),"client")}, {"dragon.jar","hearts.jar"})
            for name in ("dragon","serveronly","custom"):
                (mods / (name + ".jar")).write_bytes(name.encode())
            verify_directory(mods, server)
            (mods / "dragon.jar").unlink()
            with self.assertRaises(ValueError):
                verify_directory(mods, server)
            (mods / "dragon.jar").write_bytes(b"incorrect")
            with self.assertRaises(ValueError):
                verify_directory(mods, server)
            (mods / "dragon.jar").write_bytes(b"dragon")
            (mods / "hearts.jar").write_bytes(b"hearts")
            with self.assertRaises(ValueError):
                verify_directory(mods, server)
            (root / "mods/dragon.pw.toml").unlink()
            with self.assertRaises(ValueError):
                inventory(root)


if __name__ == "__main__":
    unittest.main()
