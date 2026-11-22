"""Offline regression for NeoForge's optional-but-present Jade dependency gate."""
import hashlib
import importlib.util
from pathlib import Path
import tempfile
import tomllib
import unittest
import zipfile

MODULE = Path(__file__).with_name("verify_client_dependency_closure.py")
SPEC = importlib.util.spec_from_file_location("verify_client_dependency_closure", MODULE)
check = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(check)


class ClientDependencyClosureTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory(prefix="tmm-client-dep-")
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.provider = self.root / "Jade-1.21.1-NeoForge-15.10.6.jar"
        self.economy = self.root / "tmm_economy-0.1.0-dev.jar"
        self.pin = self.root / "jade.pw.toml"
        self.make_provider()
        self.make_economy()
        self.make_pin()

    def make_provider(self, *, declared=None, effective="15.10.6+neoforge",
                      target=True, manifest=True, provider_id="jade"):
        if declared is None:
            declared = check.MANIFEST_VERSION_REF
        metadata = '\n'.join([
            'modLoader="javafml"', 'loaderVersion="[1,)"', 'license="MIT"',
            '[[mods]]', f'modId="{provider_id}"', f'version="{declared}"',
        ])
        with zipfile.ZipFile(self.provider, "w") as jar:
            jar.writestr("META-INF/neoforge.mods.toml", metadata)
            if manifest:
                jar.writestr("META-INF/MANIFEST.MF",
                             "Manifest-Version: 1.0\r\nImplementation-Version: "
                             + effective + "\r\n\r\n")
            if target:
                jar.writestr(check.MIXIN_TARGET, b"\xca\xfe\xba\xbe")
        self.make_pin()

    def make_economy(self, *, version="[15.10.6+neoforge]", side="CLIENT",
                     kind="optional", ordering="AFTER", duplicate=False):
        dep = '\n'.join([
            '[[dependencies.tmm_economy]]', 'modId="jade"',
            f'type="{kind}"', f'versionRange="{version}"',
            f'ordering="{ordering}"', f'side="{side}"',
        ])
        data = '\n'.join([
            'modLoader="javafml"', 'loaderVersion="[1,)"', 'license="MIT"',
            '[[mods]]', 'modId="tmm_economy"', 'version="0.1.0-dev"',
            dep,
        ])
        if duplicate:
            data += "\n" + dep
        with zipfile.ZipFile(self.economy, "w") as jar:
            jar.writestr("META-INF/neoforge.mods.toml", data)

    def make_pin(self):
        if not self.provider.exists():
            return
        digest = hashlib.sha512(self.provider.read_bytes()).hexdigest()
        self.pin.write_text('\n'.join([
            'filename = "Jade-1.21.1-NeoForge-15.10.6.jar"',
            'side = "both"', '[download]', 'hash-format = "sha512"',
            f'hash = "{digest}"',
        ]), encoding="utf-8")

    def test_native_qualified_version_from_jar_manifest_passes(self):
        self.assertEqual(check.verify(self.economy, self.provider, self.pin), "matched")
        self.assertEqual(check.actual_mod_version(self.provider), "15.10.6+neoforge")

    def test_previous_exact_unqualified_range_fails(self):
        self.make_economy(version="[15.10.6]")
        with self.assertRaisesRegex(check.DependencyClosureError, "optional but installed"):
            check.verify(self.economy, self.provider, self.pin)

    def test_another_loader_qualifier_fails(self):
        self.make_provider(effective="15.10.6+fabric")
        with self.assertRaises(check.DependencyClosureError):
            check.verify(self.economy, self.provider, self.pin)

    def test_wrong_provider_release_fails(self):
        self.make_provider(effective="15.10.7+neoforge")
        with self.assertRaises(check.DependencyClosureError):
            check.verify(self.economy, self.provider, self.pin)

    def test_optional_absent_only_when_not_selected(self):
        self.assertEqual(check.verify(self.economy, None), "optional-absent")
        with self.assertRaisesRegex(check.DependencyClosureError, "selected"):
            check.verify(self.economy, None, self.pin)

    def test_optional_present_incompatible_still_fails(self):
        self.make_economy(version="[15.10.6]")
        with self.assertRaises(check.DependencyClosureError):
            check.check_client_optional_present(
                {"tmm_economy": [{"modId": "jade", "type": "optional",
                                  "side": "CLIENT", "ordering": "AFTER",
                                  "versionRange": "[15.10.6]"}]},
                "jade", "15.10.6+neoforge"
            )

    def test_broadened_range_requires_explicit_maven_semantics_review(self):
        for version in ("[15.10.6,)", "[15.10.6,16)", "*", ""):
            with self.subTest(version=version):
                self.make_economy(version=version)
                with self.assertRaisesRegex(check.DependencyClosureError, "exact Maven"):
                    check.verify(self.economy, self.provider, self.pin)

    def test_client_dependency_contract_remains_strict(self):
        for changes in ({"kind": "required"}, {"side": "BOTH"},
                        {"side": "SERVER"}, {"ordering": "NONE"},
                        {"duplicate": True}):
            with self.subTest(changes=changes):
                self.make_economy(**changes)
                with self.assertRaises(check.DependencyClosureError):
                    check.verify(self.economy, self.provider, self.pin)

    def test_pinned_byte_hash_mismatch_fails(self):
        self.pin.write_text(self.pin.read_text().replace('hash = "', 'hash = "f'),
                            encoding="utf-8")
        with self.assertRaises(check.DependencyClosureError):
            check.verify(self.economy, self.provider, self.pin)

    def test_no_manifest_substitution_rejected(self):
        self.make_provider(manifest=False)
        with self.assertRaises(check.DependencyClosureError):
            check.verify(self.economy, self.provider, self.pin)

    def test_provider_mixin_target_missing_rejected(self):
        self.make_provider(target=False)
        with self.assertRaisesRegex(check.DependencyClosureError, "EnergyView"):
            check.verify(self.economy, self.provider, self.pin)

    def test_source_contract_keeps_exact_native_qualifier(self):
        source = (MODULE.parents[1] / "src/main/resources/META-INF/neoforge.mods.toml").read_text()
        meta = tomllib.loads(source)
        deps = meta["dependencies"]["tmm_economy"]
        self.assertEqual(check.check_client_optional_present(meta["dependencies"],
                          "jade", "15.10.6+neoforge"), "matched")
        self.assertEqual([x["versionRange"] for x in deps if x["modId"] == "jade"],
                         ["[15.10.6+neoforge]"])


if __name__ == "__main__":
    unittest.main()
