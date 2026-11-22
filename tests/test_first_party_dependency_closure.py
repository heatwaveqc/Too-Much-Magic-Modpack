"""Offline first-party dependency-closure fixtures; no JAR downloads or game launch."""
import hashlib
import json
from pathlib import Path
import sys
import tempfile
import unittest
import zipfile

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "scripts"))
import verify_first_party_dependency_closure as check


class FirstPartyClosureTest(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.addCleanup(self.tmp.cleanup)
        self.root = Path(self.tmp.name)
        self.providers = self.root / "verified-mods"
        self.providers.mkdir()
        (self.root / "mods").mkdir()
        (self.root / "manifest").mkdir()
        self.econ = self.root / "compat/economy"
        (self.econ / "build/libs").mkdir(parents=True)
        (self.econ / "gradle.properties").write_text(
            "mod_id=tmm_economy\nmod_version=0.1.0-dev\n")
        (self.root / "manifest/first-party-runtime.json").write_text(
            json.dumps({"schema": 1, "modules": [
                {"directory": "compat/economy", "mod_id": "tmm_economy"}]}))
        self.jade = self.providers / "Jade-1.21.1-NeoForge-15.10.6.jar"
        self.irons = self.providers / "irons_spellbooks-1.21.1-3.16.3.jar"
        self.provider(self.jade, "jade", "${file.jarVersion}", "15.10.6+neoforge")
        self.provider(self.irons, "irons_spellbooks", "1.21.1-3.16.3")
        self.economy()
        self.pins = {"jade": self.jade, "irons-spells-n-spellbooks": self.irons}
        self.relock()

    def provider(self, path, mod_id, version, manifest=None):
        with zipfile.ZipFile(path, "w") as z:
            z.writestr("META-INF/neoforge.mods.toml",
                       f'[[mods]]\nmodId="{mod_id}"\nversion="{version}"\n')
            if manifest is not None:
                z.writestr("META-INF/MANIFEST.MF",
                           f"Manifest-Version: 1.0\r\nImplementation-Version: {manifest}\r\n\r\n")

    def economy(self, extra="", jade_range="[15.10.6+neoforge]"):
        rows = [
            ("minecraft", "required", "[1.21.1]", "BOTH"),
            ("neoforge", "required", "[21.1.265,)", "BOTH"),
            ("jade", "optional", jade_range, "CLIENT"),
            ("irons_spellbooks", "required", "[1.21.1-3.16.3]", "BOTH"),
        ]
        data = '[[mods]]\nmodId="tmm_economy"\nversion="0.1.0-dev"\n'
        for mod_id, kind, vrange, side in rows:
            data += (f'[[dependencies.tmm_economy]]\nmodId="{mod_id}"\n'
                     f'type="{kind}"\nversionRange="{vrange}"\nside="{side}"\n')
        data += extra
        with zipfile.ZipFile(self.econ / "build/libs/tmm_economy-0.1.0-dev.jar", "w") as z:
            z.writestr("META-INF/neoforge.mods.toml", data)

    def relock(self):
        lines = ['hash-format = "sha256"']
        for slug, jar in sorted(self.pins.items()):
            path = self.root / "mods" / (slug + ".pw.toml")
            raw = (f'filename = "{jar.name}"\nside = "both"\n'
                   f'[download]\nhash-format = "sha512"\n'
                   f'hash = "{hashlib.sha512(jar.read_bytes()).hexdigest()}"\n').encode()
            path.write_bytes(raw)
            lines.extend(["[[files]]", f'file = "mods/{slug}.pw.toml"',
                          "metafile = true",
                          f'hash = "{hashlib.sha256(raw).hexdigest()}"'])
        data = ("\n".join(lines) + "\n").encode()
        (self.root / "index.toml").write_bytes(data)
        (self.root / "pack.toml").write_text(
            '[index]\nfile = "index.toml"\nhash-format = "sha256"\n'
            f'hash = "{hashlib.sha256(data).hexdigest()}"\n'
            '[versions]\nminecraft = "1.21.1"\nneoforge = "21.1.265"\n')

    def test_qualified_version_and_side(self):
        pairs = check.collect(self.root, self.providers)
        self.assertEqual(len(pairs), 7)
        self.assertIn(("tmm_economy", "client", "jade", "optional",
                       "[15.10.6+neoforge]", "15.10.6+neoforge"), pairs)
        self.assertFalse(any(x[1] == "server" and x[2] == "jade" for x in pairs))

    def test_previous_unqualified_bug_is_exposed_to_maven_checker(self):
        self.economy(jade_range="[15.10.6]")
        self.assertIn(("tmm_economy", "client", "jade", "optional",
                       "[15.10.6]", "15.10.6+neoforge"),
                      check.collect(self.root, self.providers))

    def test_selected_optional_is_not_ignored_when_jar_missing(self):
        self.jade.unlink()
        with self.assertRaisesRegex(check.ClosureError, "missing"):
            check.collect(self.root, self.providers)

    def test_absent_optional_only_when_unselected(self):
        self.pins.pop("jade")
        self.relock()
        self.assertFalse(any(row[2] == "jade"
                             for row in check.collect(self.root, self.providers)))

    def test_required_missing(self):
        self.pins.pop("irons-spells-n-spellbooks")
        self.relock()
        with self.assertRaisesRegex(check.ClosureError, "Required provider"):
            check.collect(self.root, self.providers)

    def test_new_unknown_dependency_requires_mapping(self):
        self.economy(extra='[[dependencies.tmm_economy]]\n'
                     'modId="new_unknown"\ntype="optional"\n'
                     'versionRange="[1]"\nside="CLIENT"\n')
        with self.assertRaisesRegex(check.ClosureError, "Unmapped"):
            check.collect(self.root, self.providers)

    def test_wrong_provider_bytes_fail(self):
        self.jade.write_bytes(self.jade.read_bytes() + b"modified")
        with self.assertRaisesRegex(check.ClosureError, "Wrong pinned bytes"):
            check.collect(self.root, self.providers)

    def test_wrong_index_hash_fail(self):
        (self.root / "index.toml").write_text('hash-format = "sha256"\n')
        with self.assertRaisesRegex(check.ClosureError, "root index"):
            check.collect(self.root, self.providers)

    def test_wrong_native_mod_id_fail(self):
        self.provider(self.jade, "wrong_mod", "15.10.6+neoforge")
        self.relock()
        with self.assertRaisesRegex(check.ClosureError, "does not supply"):
            check.collect(self.root, self.providers)

    def test_unresolved_manifest_version_fail(self):
        self.provider(self.jade, "jade", "${file.jarVersion}")
        self.relock()
        with self.assertRaisesRegex(check.ClosureError, "manifest"):
            check.collect(self.root, self.providers)

    def test_missing_first_party_jar_fail(self):
        (self.econ / "build/libs/tmm_economy-0.1.0-dev.jar").unlink()
        with self.assertRaisesRegex(check.ClosureError, "Missing"):
            check.collect(self.root, self.providers)


if __name__ == "__main__":
    unittest.main()
