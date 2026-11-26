#!/usr/bin/env python3
"""Render an illustrated native-task chapter from authored JSON; no game boot."""
import hashlib
import json
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
SOURCE = "manifest/friendship_is_magic_quest_pilot.json"

def load():
    spec = json.loads((ROOT / SOURCE).read_text(encoding="utf-8"))
    ids = json.loads((ROOT / spec["id_map"]).read_text(encoding="utf-8"))
    catalog = json.loads((ROOT / "manifest/quest_chapters.json").read_text(encoding="utf-8"))
    meta = next(c for c in catalog["chapters"] if c.get("source_manifest") == SOURCE)
    return spec, ids, meta

def q(s):
    return json.dumps(s, ensure_ascii=False)

def num(n):
    return f"{float(n):.1f}d"

def render(spec, ids, meta):
    lines = ["{", "\tdefault_hide_dependency_lines: false", '\tdefault_quest_shape: "rsquare"',
             f'\tfilename: {q(spec["slug"])}', f'\tgroup: {q(meta["group_id"])}',
             f'\ticon: {{ id: {q(meta["icon"])} }}', f'\tid: {q(ids["chapter_id"])}', "\timages: ["]
    images = spec["presentation"]["images"]
    assert len(images) == 1, "one reviewed chapter background"
    for im in images:
        x, y = im["position"]
        lines += ["\t\t{", f'\t\t\tid: {q(ids["image_id"])}', f'\t\t\timage: {q(im["texture"])}',
                  f"\t\t\tx: {num(x)}", f"\t\t\ty: {num(y)}", f'\t\t\twidth: {num(im["width"])}',
                  f'\t\t\theight: {float(im["height"]):.5f}d', "\t\t\trotation: 0.0d",
                  "\t\t\tposition_locked: true", "\t\t}"]
    lines += ["\t]", f'\torder_index: {meta["order_index"]}', '\tprogression_mode: "flexible"',
              "\tquest_links: [ ]", "\tquests: ["]
    for i, node in enumerate(spec["quests"]):
        ident = ids["quests"][node["key"]]
        c, reward = node["criterion"], node["reward"]
        typ = c["type"]
        task = f'{{ id: {q(ident["task_id"])}, type: {q(typ)}'
        if typ == "item":
            assert c["consume"] is False
            task += f', item: {{ id: {q(c["id"])}, count: 1 }}, count: {c["count"]}L, consume_items: false'
        elif typ == "advancement":
            task += f', advancement: {q(c["id"])}'
        elif typ == "dimension":
            task += f', dimension: {q(c["id"])}'
        elif typ == "observation":
            task += f', observation_type: {q(c["observation_type"])}, to_observe: {q(c["to_observe"])}, timer: {c["timer"]}L'
        elif typ != "checkmark":
            raise ValueError(f"Unknown native task {typ}")
        task += " }"
        lines += ["\t\t{", f'\t\t\tid: {q(ident["quest_id"])}',
                  f'\t\t\ticon: {{ id: {q(node["icon"]["item"])} }}']
        if node["depends"]:
            deps = ", ".join(q(ids["quests"][k]["quest_id"]) for k in node["depends"])
            lines.append(f"\t\t\tdependencies: [{deps}]")
        if node["optional"]:
            lines.append("\t\t\toptional: true")
        lines += [f'\t\t\tsize: {num(node["presentation_size"])}', "\t\t\trewards: [",
                  f'\t\t\t\t{{ id: {q(ident["xp_reward_id"])}, type: "xp", xp: {reward["xp"]}, team_reward: false }}',
                  f'\t\t\t\t{{ id: {q(ident["item_reward_id"])}, type: "item", item: {{ id: {q(reward["item"])}, count: 1 }}, count: {reward["count"]}, team_reward: false }}',
                  "\t\t\t]", "\t\t\ttasks: [", "\t\t\t\t" + task, "\t\t\t]",
                  f'\t\t\tx: {num(node["position"][0])}', f'\t\t\ty: {num(node["position"][1])}',
                  "\t\t}" + ("," if i + 1 < len(spec["quests"]) else "")]
    return "\n".join(lines + ["\t]", "}", ""])

def check():
    spec, ids, meta = load()
    assert len(spec["quests"]) == 21
    keys = [n["key"] for n in spec["quests"]]
    assert len(set(keys)) == 21 and set(keys) == set(ids["quests"])
    all_ids = [ids["chapter_id"], ids["image_id"]] + [v for m in ids["quests"].values() for v in m.values()]
    assert len(set(all_ids)) == len(all_ids) == 86
    assert all(re.fullmatch("[0-7][0-9A-F]{15}", x) for x in all_ids)
    assert ids["chapter_id"] == meta["chapter_id"]
    assert spec["rules"]["no_graduation"] and not spec["rules"]["global_mxp"]
    assert sum(n["criterion"]["type"] == "checkmark" for n in spec["quests"]) == 10
    for i, n in enumerate(spec["quests"]):
        assert all(k in keys[:i] for k in n["depends"])
        assert n["reward"]["xp"] > 0 and n["reward"]["count"] > 0
        assert not n["reward"]["team_reward"] and not n["reward"]["autoclaim"]
        assert n["task_label"]
        if n["criterion"]["type"] == "advancement":
            ns, path = n["criterion"]["id"].split(":", 1)
            assert (ROOT / f"kubejs/data/{ns}/advancement/{path}.json").is_file()
        if n["key"] in ("UA17", "UA20"):
            assert n["criterion"]["type"] == "checkmark"
    im = spec["presentation"]["images"][0]
    data = (ROOT / im["source_path"]).read_bytes()
    assert data.startswith(b"\x89PNG\r\n\x1a\n") and hashlib.sha256(data).hexdigest() == im["sha256"]
    assert len(data) == im["bytes"]
    for n in spec["quests"]:
        x, y = n["position"]
        cx, cy = im["position"]
        assert abs(x-cx) + n["presentation_size"]/2 <= im["width"]/2
        assert abs(y-cy) + n["presentation_size"]/2 <= im["height"]/2
    expected = render(spec, ids, meta)
    actual = (ROOT / "config/ftbquests/quests/chapters" / meta["filename"]).read_text(encoding="utf-8")
    assert actual == expected, "Friendship live chapter differs from canonical authored source"
    print("PASS: Friendship 21 native-task lessons, 86 IDs, original art and personal reward/graph contract")

if __name__ == "__main__":
    check()
