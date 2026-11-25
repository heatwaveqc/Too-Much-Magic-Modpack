"""Source contract for #713's scoped ExMI 1.0.3 configuration, no client boot."""
import hashlib
import json
from pathlib import Path
import tomllib
import unittest

ROOT = Path(__file__).resolve().parents[1]
CONFIG = "config/exmi/enable_config.json"


class ExmiPluginGuards(unittest.TestCase):
    def test_only_two_broken_integrations_disabled(self):
        # Upstream v1.0.3+1.21.1 ExMIEnableConfig and RPlugin classes:
        # enabledIntegrations uses target mod IDs, missing entries default true.
        config = json.loads((ROOT / CONFIG).read_text(encoding="utf-8"))
        self.assertEqual(config, {"enabledIntegrations": {
            "rechiseled": False, "reliquary": False,
        }})

    def test_current_viewers_and_providers_remain_pinned(self):
        manifest = json.loads((ROOT / "manifest/mods.json").read_text(encoding="utf-8"))
        active = {m["id"] for m in manifest["mods"] if m["enabled_in_pack"]}
        self.assertTrue({"emi", "emisemble", "extra-mod-integrations",
                         "rechiseled", "reliquary-reincarnations"} <= active)
        pin = tomllib.loads((ROOT / "mods/extra-mod-integrations.pw.toml").read_text(encoding="utf-8"))
        self.assertEqual(pin["filename"], "extra-mod-integrations-all-neoforge-1.0.3+1.21.1.jar")
        self.assertEqual(pin["side"], "client")
        self.assertEqual(pin["update"]["modrinth"]["version"], "bxgGyUjY")

    def test_config_is_indexed_with_current_hash(self):
        raw = (ROOT / "index.toml").read_bytes()
        pack = tomllib.loads((ROOT / "pack.toml").read_text(encoding="utf-8"))
        self.assertEqual(pack["index"]["hash"], hashlib.sha256(raw).hexdigest())
        rows = [r for r in tomllib.loads(raw.decode("utf-8"))["files"]
                if r["file"] == CONFIG]
        self.assertEqual(len(rows), 1)
        self.assertEqual(rows[0]["hash"], hashlib.sha256((ROOT / CONFIG).read_bytes()).hexdigest())


if __name__ == "__main__":
    unittest.main()
