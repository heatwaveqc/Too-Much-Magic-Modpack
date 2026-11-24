"""Type-A Theurgy v2 graph and generated FTB layout regression checks (no Minecraft boot)."""
from __future__ import annotations

import hashlib
import importlib.util
import json
from pathlib import Path
import re
import tomllib
import unittest

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "manifest/theurgy_quest_pilot.json"
GENERATED = ROOT / "docs/quest-pilots/theurgy/generated"
LIVE = ROOT / "config/ftbquests/quests/chapters/theurgy.snbt"

# Immutable first-pass dependency semantics. Dropping a transitively redundant
# line may simplify the graph, but must NEVER change effective prerequisites.
BASELINE = {
    "TH-00": [], "TH-01": ["TH-00"], "TH-02": ["TH-01"],
    "TH-03": ["TH-01"], "TH-04": ["TH-00"],
    "TH-05": ["TH-01", "TH-04"],
    "TH-06": ["TH-02", "TH-03", "TH-05"],
    "TH-07": ["TH-06"], "TH-08": ["TH-07"],
    "TH-09": ["TH-04", "TH-05"], "TH-10": ["TH-09", "TH-03"],
    "TH-11": ["TH-10"], "TH-12": ["TH-02", "TH-07"],
    "TH-13": ["TH-11", "TH-12"], "TH-14": ["TH-00"],
    "TH-15": ["TH-14"], "TH-16": ["TH-01", "TH-12"],
    "TH-17": ["TH-02", "TH-07"], "TH-18": ["TH-17", "TH-04"],
    "TH-19": ["TH-11", "TH-13"],
}


def ancestors(graph: dict[str, list[str]], key: str) -> set[str]:
    seen = set()
    stack = list(graph[key])
    while stack:
        parent = stack.pop()
        if parent not in seen:
            seen.add(parent)
            stack.extend(graph[parent])
    return seen


def pilot():
    spec = importlib.util.spec_from_file_location(
        "tmm_build_theurgy_pilot", ROOT / "scripts/build_theurgy_quest_pilot.py")
    if spec is None or spec.loader is None:
        raise RuntimeError("Theurgy generator not importable")
    module = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(module)
    return module


class TheurgyLayoutA(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.content = json.loads(SOURCE.read_text(encoding="utf-8"))
        cls.generator = pilot()

    def test_dependency_semantics_and_geometric_clearance(self):
        quests = self.content["quests"]
        current = {q["key"]: q["dependencies"] for q in quests}
        self.assertEqual(set(BASELINE), set(current))
        for key in BASELINE:
            self.assertEqual(
                ancestors(BASELINE, key), ancestors(current, key),
                f"Effective prerequisite regression at {key}")
        self.generator.check_spec(self.content)
        self.assertEqual(self.content["layout_review"]["after_crossings"], 1)

    def test_canonical_quest_sources_use_same_direct_arrows(self):
        expected = {q["title"]: q["dependencies"] for q in self.content["quests"]}
        key_title = {q["key"]: q["title"] for q in self.content["quests"]}
        for name in ("quest_content.json", "quest_content_resolved.json"):
            catalog = json.loads((ROOT / "manifest" / name).read_text(encoding="utf-8"))
            actual = {q["title"]: q.get("prerequisites", [])
                      for q in catalog["quests"] if q["chapter"] == "Theurgy"}
            mapped = {title: [key_title[k] for k in deps]
                      for title, deps in expected.items()}
            self.assertEqual(actual, mapped, f"Stale Theurgy direct edges in {name}")

    def test_stable_ids_and_reproducible_outputs(self):
        outputs = self.generator.render(self.content)
        self.assertEqual(outputs["theurgy.snbt"], LIVE.read_text(encoding="utf-8"))
        for name, data in outputs.items():
            self.assertEqual(data, (GENERATED / name).read_text(encoding="utf-8"))
        self.assertEqual(len(self.content["quests"]), 20)
        self.assertIn('544D303001500001', outputs["theurgy.snbt"])

    def test_derived_position_inventory(self):
        derived = json.loads((ROOT / "manifest/quest_layouts.json").read_text(encoding="utf-8"))
        keys = {q["key"] for q in self.content["quests"]}
        expected = {
            self.generator.id_for("quest", q["key"]): q["position"]
            for q in self.content["quests"]}
        actual = {r["quest_id"]: r["position"] for r in derived["positions"]
                  if r["chapter"] == "Theurgy"}
        self.assertEqual(len(actual), 20)
        self.assertEqual(actual, expected)

    def test_packwiz_index_and_pack_root_hash(self):
        index_raw = (ROOT / "index.toml").read_bytes()
        pack = tomllib.loads((ROOT / "pack.toml").read_text(encoding="utf-8"))
        self.assertEqual(pack["index"]["hash"], hashlib.sha256(index_raw).hexdigest())
        index = tomllib.loads(index_raw.decode("utf-8"))
        rows = [row for row in index["files"]
                if row["file"] == "config/ftbquests/quests/chapters/theurgy.snbt"]
        self.assertEqual(len(rows), 1)
        self.assertEqual(rows[0]["hash"], hashlib.sha256(LIVE.read_bytes()).hexdigest())


if __name__ == "__main__":
    unittest.main()
