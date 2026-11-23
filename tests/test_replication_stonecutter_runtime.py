import importlib.util
import json
import tempfile
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SPEC = importlib.util.spec_from_file_location(
    "stonecutter_runtime", ROOT / "scripts" / "build_replication_stonecutter_runtime.py"
)
MODULE = importlib.util.module_from_spec(SPEC)
assert SPEC.loader is not None
SPEC.loader.exec_module(MODULE)
CATALOG = ROOT / "docs/research/replication-stonecutter-catalog-2026-11-23"


class ReplicationStonecutterRuntimeTest(unittest.TestCase):
    def test_frozen_catalog_produces_all_five_resources(self):
        items, rows, summary = MODULE.load_catalog(CATALOG)
        self.assertEqual(len(items), 1720)
        self.assertEqual(len(rows), len(items))
        self.assertEqual(summary["stonecutting_outputs_with_explicit_anchor_count"], 4)
        self.assertIn("minecraft:cut_copper", items)
        self.assertIn("minecraft:stone_stairs", items)
        files = MODULE.runtime_files(items)
        self.assertEqual(len(files), 5)
        for tag in MODULE.NATIVE_TAGS:
            namespace, name = tag.split(":", 1)
            self.assertEqual(
                files[f"{namespace}/tags/item/{name}.json"]["values"],
                ["#tmm:replication/stonecutter_outputs"],
            )
        anchor = files["replication/recipe/matter_values/c/tags/stones.json"]
        self.assertEqual(anchor["input"], {
            "type": "neoforge:difference",
            "base": {"tag": "c:stones"},
            "subtracted": {"tag": "tmm:replication/stonecutter_outputs"},
        })
        self.assertEqual(anchor["matter"], [{"type": "replication:earth", "amount": 2.0}])

    def test_generated_datapack_files_match_catalog(self):
        files = MODULE.runtime_files(MODULE.load_catalog(CATALOG)[0])
        for path, expected in files.items():
            self.assertEqual(json.loads((ROOT / "kubejs/data" / path).read_text()), expected, path)

    def test_new_explicit_anchor_fails_closed(self):
        with tempfile.TemporaryDirectory() as directory:
            source = Path(directory) / "catalog"
            source.mkdir()
            for name in ("summary.json", "proposed-native-tag-values.json", "stonecutter-outputs.csv"):
                (source / name).write_bytes((CATALOG / name).read_bytes())
            csv_file = source / "stonecutter-outputs.csv"
            contents = csv_file.read_text()
            self.assertIn("c:stones", contents)
            csv_file.write_text(contents.replace("c:stones", "c:unexpected_anchor", 1))
            output = Path(directory) / "out"
            with self.assertRaisesRegex(ValueError, "Uncovered explicit matter anchor"):
                MODULE.write_runtime(source, output)
            self.assertFalse(output.exists())

    def test_unsorted_catalog_fails_closed(self):
        with tempfile.TemporaryDirectory() as directory:
            source = Path(directory)
            for name in ("summary.json", "proposed-native-tag-values.json", "stonecutter-outputs.csv"):
                (source / name).write_bytes((CATALOG / name).read_bytes())
            file = source / "proposed-native-tag-values.json"
            data = json.loads(file.read_text())
            data["tags"][MODULE.NATIVE_TAGS[0]].reverse()
            file.write_text(json.dumps(data))
            with self.assertRaisesRegex(ValueError, "sorted"):
                MODULE.load_catalog(source)


if __name__ == "__main__":
    unittest.main()
