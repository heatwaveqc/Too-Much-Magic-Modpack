#!/usr/bin/env python3
"""Build the isolated Theurgy FTB Quests candidate; never overwrite integrated quests.

Usage:
  python scripts/build_theurgy_quest_pilot.py --check
  python scripts/build_theurgy_quest_pilot.py --write

The integration writer must merge the manifest/locale/FTB chapter into canonical
sources and update packwiz; this script intentionally does not do that.
"""
from __future__ import annotations

import argparse
import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SPEC = ROOT / "manifest/theurgy_quest_pilot.json"
OUT = ROOT / "docs/quest-pilots/theurgy/generated"
ASSETS = [
    ("title.png", 6.0, -12.5, 13.5, 3.3),
    ("vessels.png", 14.0, -6.8, 8.0, 2.4),
    ("niter.png", 18.0, 12.0, 11.5, 2.5),
    ("reformation.png", 30.0, 12.0, 8.0, 2.8),
    ("wires.png", 25.0, -10.0, 11.2, 2.4),
]

PREFIX = {"quest": "544D3A150000", "task": "544D4A150000",
          "xp": "544D6A150000", "item": "544D7A150000"}
EXISTING = {"quest": "544D303001500001", "task": "544D403001500001",
            "xp": "544D903001500001", "item": "544DA03001500002"}


def q(text: str) -> str:
    return json.dumps(text, ensure_ascii=False)


def id_for(kind: str, key: str) -> str:
    if key == "TH-00":
        return EXISTING[kind]
    return PREFIX[kind] + f"{int(key[3:]):04X}"


def num(value: float) -> str:
    return f"{float(value):.1f}d"



def _proper_cross(a, b, c, d) -> bool:
    """Count genuine line intersections, not endpoints shared by a prerequisite."""
    def orient(p, q, r):
        return (q[0] - p[0]) * (r[1] - p[1]) - (q[1] - p[1]) * (r[0] - p[0])
    p, q = orient(a, b, c), orient(a, b, d)
    r, t = orient(c, d, a), orient(c, d, b)
    return p * q < -1e-7 and r * t < -1e-7


def _line_to_point_distance(a, b, p) -> float:
    dx, dy = b[0] - a[0], b[1] - a[1]
    d2 = dx * dx + dy * dy
    t = max(0, min(1, ((p[0] - a[0]) * dx + (p[1] - a[1]) * dy) / d2))
    return ((a[0] + t * dx - p[0]) ** 2 +
            (a[1] + t * dy - p[1]) ** 2) ** 0.5


def _line_hits_panel(a, b, panel) -> bool:
    """Liang–Barsky segment clipping: positioned art must avoid connector strokes."""
    _, x, y, width, height = panel
    xmin, xmax = x - width / 2, x + width / 2
    ymin, ymax = y - height / 2, y + height / 2
    dx, dy = b[0] - a[0], b[1] - a[1]
    lo, hi = 0.0, 1.0
    for p, qv in ((-dx, a[0] - xmin), (dx, xmax - a[0]),
                  (-dy, a[1] - ymin), (dy, ymax - a[1])):
        if abs(p) < 1e-10:
            if qv < 0:
                return False
            continue
        ratio = qv / p
        if p < 0:
            lo = max(lo, ratio)
        else:
            hi = min(hi, ratio)
        if lo > hi:
            return False
    return True


def check_layout(quests: list[dict]) -> None:
    """Type A: readable rendered graph geometry, never a claim of client QA."""
    coords = {n["key"]: tuple(n["position"]) for n in quests}
    edges = [(parent, node["key"]) for node in quests for parent in node["dependencies"]]
    assert all(coords[b][0] >= coords[a][0] for a, b in edges), "backward Theurgy dependency"
    for idx, (a, b) in enumerate(edges):
        start, end = coords[a], coords[b]
        length = _line_to_point_distance(start, end, start) + (
            (end[0] - start[0]) ** 2 + (end[1] - start[1]) ** 2) ** 0.5
        assert length <= 15.0, f"Long Theurgy dependency {a}->{b}: {length:.2f}"
        for c, point in coords.items():
            if c not in (a, b):
                assert _line_to_point_distance(start, end, point) >= 1.1, (
                    f"Dependency {a}->{b} runs through/too near node {c}")
        for asset in ASSETS:
            assert not _line_hits_panel(start, end, asset), (
                f"Decorative panel {asset[0]} covers dependency {a}->{b}")
    crossings = [(a, b, c, d) for i, (a, b) in enumerate(edges)
                 for c, d in edges[i + 1:] if len({a, b, c, d}) == 4
                 and _proper_cross(coords[a], coords[b], coords[c], coords[d])]
    assert len(crossings) <= 1, f"Excess Theurgy line crossings: {crossings}"
    for i, n in enumerate(quests):
        for other in quests[i + 1:]:
            px, py = n["position"]
            ox, oy = other["position"]
            assert (px - ox) ** 2 + (py - oy) ** 2 >= 1.7 ** 2, (
                f"Overlapping/too-close Theurgy nodes {n['key']} {other['key']}")


def check_spec(spec: dict) -> None:
    quests = spec["quests"]
    assert len(quests) == 20
    assert sum(x["lane"] == "main" for x in quests) == 9
    assert sum(x["lane"] == "optional" for x in quests) == 11
    keys = [x["key"] for x in quests]
    assert keys == [f"TH-{i:02d}" for i in range(20)]
    key_set = set(keys)
    ids: set[str] = set()
    positions: set[tuple] = set()
    item_tasks: set[str] = set()
    for node in quests:
        for dep in node["dependencies"]:
            assert dep in key_set and keys.index(dep) < keys.index(node["key"]), (
                node["key"], dep)
        position = tuple(node["position"])
        assert len(position) == 2 and position not in positions
        positions.add(position)
        assert node["icon"] and node["icon"] != "minecraft:enchanted_book"
        assert node["description"] and node["reward"]["xp"] > 0
        for kind in PREFIX:
            ident = id_for(kind, node["key"])
            assert re.fullmatch(r"[0-9A-F]{16}", ident)
            assert 1 < int(ident, 16) <= 2**63 - 1
            assert ident not in ids
            ids.add(ident)
        if node["criterion"]["type"] == "item":
            task_item = node["criterion"]["id"]
            assert task_item not in item_tasks
            item_tasks.add(task_item)
            assert task_item != node["reward"]["item"]
        else:
            assert node["criterion"]["type"] == "checkmark"
    assert not (item_tasks & {x["reward"]["item"] for x in quests})
    assert spec["existing_orientation"]["quest_id"] == id_for("quest", "TH-00")
    # Art is background-like composition, never a click-blocking overlay.
    for name, x, y, width, height in ASSETS:
        for node in quests:
            role = node["node_role"]
            size = (1.6 if role == "orientation" else
                    1.7 if role == "advanced_capstone" else
                    1.4 if role in ("first_capstone", "core_capstone") else 1.0)
            px, py = node["position"]
            collides = (abs(x - px) < (width + size) / 2 + 0.1 and
                        abs(y - py) < (height + size) / 2 + 0.1)
            assert not collides, f"{name} overlaps quest {node['key']}"
    check_layout(quests)


def render(spec: dict) -> dict[str, str]:
    result = [
        "{",
        "\tdefault_hide_dependency_lines: false",
        '\tdefault_quest_shape: "rsquare"',
        '\tfilename: "theurgy"',
        f'\tgroup: {q(spec["group_id"])}',
        '\ticon: { id: "theurgy:alchemical_sulfur_sulfur" }',
        f'\tid: {q(spec["chapter_id"])}',
        "\timages: [",
    ]
    for n, (asset, x, y, width, height) in enumerate(ASSETS):
        result += [
            "\t\t{",
            f'\t\t\timage: {q("tmm:textures/questpics/theurgy/" + asset)}',
            f"\t\t\tx: {num(x)}",
            f"\t\t\ty: {num(y)}",
            f"\t\t\twidth: {num(width)}",
            f"\t\t\theight: {num(height)}",
            "\t\t\trotation: 0.0d",
            "\t\t}" + ("" if n == len(ASSETS) - 1 else ","),
        ]
    result += [
        "\t]", "\torder_index: 20", '\tprogression_mode: "flexible"',
        "\tquest_links: [ ]", "\tquests: [",
    ]
    translations: list[str] = []
    mapping: dict[str, dict] = {}
    quests = spec["quests"]
    for i, node in enumerate(quests):
        key = node["key"]
        xid = id_for("quest", key)
        mapping[key] = {"quest_id": id_for("quest", key),
                        "task_id": id_for("task", key),
                        "xp_reward_id": id_for("xp", key),
                        "item_reward_id": id_for("item", key)}
        lines = [
            "\t\t{",
            f"\t\t\tid: {q(xid)}",
            f'\t\t\ticon: {{ id: {q(node["icon"])} }}',
        ]
        if node["dependencies"]:
            deps = ", ".join(q(id_for("quest", dep)) for dep in node["dependencies"])
            lines += [f"\t\t\tdependencies: [{deps}]"]
        if node["lane"] == "optional" or key == "TH-00":
            lines += ["\t\t\toptional: true"]
        role = node["node_role"]
        if role in ("orientation", "core_capstone", "advanced_capstone", "first_capstone"):
            shape = "gear" if role == "orientation" else "octagon" if role == "advanced_capstone" else "hexagon"
            size = 1.6 if role == "orientation" else 1.7 if role == "advanced_capstone" else 1.4
            lines += [f"\t\t\tshape: {q(shape)}", f"\t\t\tsize: {num(size)}"]
        elif node["lane"] == "optional":
            lines += ['\t\t\tshape: "rsquare"']
        rewards = node["reward"]
        lines += [
            "\t\t\trewards: [",
            f'\t\t\t\t{{ id: {q(id_for("xp", key))}, type: "xp", xp: {rewards["xp"]}, team_reward: false }}',
            f'\t\t\t\t{{ id: {q(id_for("item", key))}, type: "item", item: {{ id: {q(rewards["item"])}, count: 1 }}, count: {rewards["count"]}, team_reward: false }}',
            "\t\t\t]",
            "\t\t\ttasks: [",
        ]
        criterion = node["criterion"]
        if criterion["type"] == "checkmark":
            lines += [f'\t\t\t\t{{ id: {q(id_for("task", key))}, type: "checkmark" }}']
        else:
            lines += [f'\t\t\t\t{{ id: {q(id_for("task", key))}, type: "item", item: {{ id: {q(criterion["id"])}, count: 1 }}, count: 1L, consume_items: false }}']
        lines += [
            "\t\t\t]",
            f'\t\t\tx: {num(node["position"][0])}',
            f'\t\t\ty: {num(node["position"][1])}',
            "\t\t}" + ("" if i == len(quests) - 1 else ","),
        ]
        result.extend(lines)
        translations += [
            f'\tquest.{xid}.title: {q(node["title"])}',
            f'\tquest.{xid}.quest_desc: [{q(node["description"])}]',
        ]
    result += ["\t]", "}"]
    return {
        "theurgy.snbt": "\n".join(result) + "\n",
        "lang_fragment.snbt": "{\n" + "\n".join(translations) + "\n}\n",
        "quest_id_map.json": json.dumps(mapping, indent=2, ensure_ascii=False) + "\n",
    }


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    mode = parser.add_mutually_exclusive_group(required=True)
    mode.add_argument("--check", action="store_true")
    mode.add_argument("--write", action="store_true")
    args = parser.parse_args()
    spec = json.loads(SPEC.read_text(encoding="utf-8"))
    check_spec(spec)
    outputs = render(spec)
    if args.write:
        OUT.mkdir(parents=True, exist_ok=True)
        for name, value in outputs.items():
            (OUT / name).write_text(value, encoding="utf-8")
        print("Generated isolated Theurgy candidate: 20 quests, 5 art references")
        return 0
    mismatches = [name for name, value in outputs.items()
                  if not (OUT / name).is_file() or (OUT / name).read_text(encoding="utf-8") != value]
    if mismatches:
        print("Theurgy generated candidate is stale: " + ", ".join(mismatches), file=sys.stderr)
        return 1
    print("PASS Theurgy: source graph, IDs, rewards and 3 reproducible preview files")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
