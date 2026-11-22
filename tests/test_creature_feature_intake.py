"""Offline contract tests for Creature Feature #419 intake (no network/game boot)."""
import hashlib
import importlib.util
import io
import json
import unittest
import zipfile
from pathlib import Path

FILE = Path(__file__).resolve().parents[1] / "scripts/resolve_creature_feature_130.py"
SPEC = importlib.util.spec_from_file_location("creature_feature_resolver", FILE)
resolver = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(resolver)


def version(slug):
    name, project, vid, number = resolver.RELEASES[slug]
    filename = "creaturefeature-1.3.0.jar" if slug == "creature-feature" else "blueprint-1.21.1-8.1.0.jar"
    return {
        "id": vid, "project_id": project, "version_number": number, "version_type": "release",
        "game_versions": ["1.21.1"], "loaders": ["neoforge"], "dependencies": [],
        "files": [{
            "primary": True, "filename": filename,
            "url": f"https://cdn.modrinth.com/data/{project}/versions/{vid}/{filename}",
            "hashes": {"sha512": "a" * 128},
        }]
    }


def jar(modid):
    content = f"""
modLoader = "javafml"
loaderVersion = "[4,)"
license = "MIT"
[[mods]]
modId = "{modid}"
version = "1.0"
[[dependencies.{modid}]]
modId = "blueprint"
type = "required"
versionRange = "[8.1.0,)"
side = "BOTH"
"""
    result = io.BytesIO()
    with zipfile.ZipFile(result, "w") as archive:
        archive.writestr("META-INF/neoforge.mods.toml", content)
        archive.writestr("test-padding.txt", "x" * 180_000)
        archive.writestr("META-INF/jarjar/embedded.jar", "embedded")
    return result.getvalue()


class ResolverTests(unittest.TestCase):
    def test_exact_candidate_file_and_toml(self):
        for slug in resolver.RELEASES:
            row = resolver.official_file(version(slug), slug)
            pin = resolver.toml(row)
            self.assertIn('hash-format = "sha512"', pin)
            self.assertIn('side = "both"', pin)
            self.assertIn(row["sha512"], pin)

    def test_reject_provider_id_mismatch_loader_and_changed_channel(self):
        for key, bad in (
            ("id", "unknown"),
            ("project_id", "malicious-project"),
            ("loaders", ["fabric"]),
            ("game_versions", ["1.20.1"]),
            ("version_type", "beta"),
            ("version_number", "1.3.1"),
        ):
            with self.subTest(key=key):
                obj = version("creature-feature")
                obj[key] = bad
                with self.assertRaises(ValueError):
                    resolver.official_file(obj, "creature-feature")

    def test_reject_unofficial_links_or_wrong_hashes(self):
        for fkey, bad in (
            ("url", "https://evil.invalid/fake.jar"),
            ("url", "http://cdn.modrinth.com/data/7nEkcq0F/versions/WSnoKdtT/creaturefeature-1.3.0.jar"),
            ("filename", "fake/creaturefeature.jar"),
            ("sha512", "0" * 127),
        ):
            with self.subTest(fkey=fkey):
                obj = version("creature-feature")
                target = obj["files"][0]
                if fkey == "sha512":
                    target["hashes"]["sha512"] = bad
                else:
                    target[fkey] = bad
                with self.assertRaises(ValueError):
                    resolver.official_file(obj, "creature-feature")

    def test_neoforge_metadata_declares_required_dependency_and_embedded_module(self):
        result = resolver.scan_jar(jar("creaturefeature"), "creature-feature")
        self.assertEqual(result["mod_ids"], ["creaturefeature"])
        self.assertEqual(result["mandatory"][0]["mod_id"], "blueprint")
        self.assertEqual(result["embedded_jars"], ["META-INF/jarjar/embedded.jar"])

    def test_reject_wrong_runtime_identity(self):
        with self.assertRaises(ValueError):
            resolver.scan_jar(jar("wrongmod"), "creature-feature")

    def test_dependency_closure_accepts_blueprint_and_rejects_new_required_modules(self):
        def row(slug):
            result = resolver.official_file(version(slug), slug)
            result["jar"] = {"mandatory": [
                {"mod_id": "neoforge"}, {"mod_id": "minecraft"}
            ]}
            return result
        creature = row("creature-feature")
        blueprint = row("blueprint")
        creature["dependencies"] = [
            {"project_id": "VsM5EDoI", "version_id": "5JOCYuQM", "dependency_type": "required"},
            {"project_id": "T9PomCSv", "version_id": None, "dependency_type": "optional"}
        ]
        resolver.verify_dependency_closure([creature, blueprint])
        creature["dependencies"].append({
            "project_id": "unknown", "version_id": "new", "dependency_type": "required"
        })
        with self.assertRaises(ValueError):
            resolver.verify_dependency_closure([creature, blueprint])

    def test_no_download_during_offline_import(self):
        self.assertEqual(sorted(resolver.RELEASES), ["blueprint", "creature-feature"])


if __name__ == "__main__":
    unittest.main()
