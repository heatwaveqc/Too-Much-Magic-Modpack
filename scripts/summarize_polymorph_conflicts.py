#!/usr/bin/env python3
"""Parse Polymorph's own on-server /polymorph conflicts diagnostic.

Does not attempt to claim that Polymorph catches every custom-mod crafting
station or conditional recipe. The original log remains the authority.
"""
import argparse
import collections
import itertools
import json
import os
import re
from pathlib import Path

ap = argparse.ArgumentParser()
ap.add_argument("--report", type=Path, required=True)
ap.add_argument("--server-log", type=Path, required=True)
ap.add_argument("--output-json", type=Path, required=True)
ap.add_argument("--output-md", type=Path, required=True)
args = ap.parse_args()
log = args.server_log.read_text(encoding="utf-8", errors="replace")
groups = []
types = {}
skipped = collections.defaultdict(list)
current_type = None
scanning_skipped = False
if args.report.is_file():
    for line in args.report.read_text(encoding="utf-8", errors="replace").splitlines():
        m = re.search(r"^([a-z0-9_.-]+:[a-z0-9_./-]+) recipe conflicts \((\d+)\)", line.strip())
        if m:
            current_type = m.group(1)
            types[current_type] = int(m.group(2))
            scanning_skipped = False
            continue
        if line.strip() == "Skipped special recipes:":
            scanning_skipped = True
            continue
        if scanning_skipped and re.fullmatch(r"[a-z0-9_.-]+:[a-z0-9_./-]+", line.strip()):
            skipped[current_type].append(line.strip())
            continue
        m = re.match(r"^\s*(\d+):\s*(.+)", line)
        if m and current_type:
            ids = [x.strip() for x in m.group(2).split(",") if x.strip()]
            groups.append({"recipe_type": current_type, "group_number": int(m.group(1)),
                           "recipe_ids": ids, "namespaces": sorted({x.split(":")[0] for x in ids})})
    assert sum(types.values()) == len(groups), (
        f"Polymorph report says {sum(types.values())} groups but parser found {len(groups)}"
    )
else:
    assert re.search(r"Found 0 potential conflicts", log), (
        "No Polymorph report and no confirmed zero-conflict console response"
    )

namespace_counts = collections.Counter(
    ns for g in groups for ns in g["namespaces"]
)
crossmod_counts = collections.Counter()
for g in groups:
    for pair in itertools.combinations(g["namespaces"], 2):
        crossmod_counts[" + ".join(pair)] += 1
out = {
    "auditor": "Polymorph /polymorph conflicts on live recipe manager",
    "groups_total": len(groups),
    "recipe_types": types,
    "groups": groups,
    "skipped_special_recipes": dict(skipped),
    "namespaces_by_conflict_group": dict(namespace_counts.most_common()),
    "cross_mod_pairs_by_group": dict(crossmod_counts.most_common()),
    "limitations": [
        "Polymorph reports potential conflicts, not proof that player crafting fails",
        "Special/custom recipes skipped by Polymorph require independent review",
        "Custom recipe managers and magical crafting stations are outside built-in scan",
        "The scanner's grouping is not a mathematically exhaustive pairwise conflict set",
    ],
}
args.output_json.parent.mkdir(parents=True, exist_ok=True)
args.output_json.write_text(json.dumps(out, indent=2) + "\n", encoding="utf-8")
lines = ["# Polymorph conflict audit — actual loaded modded-server recipes", "",
         f"**Potential conflicting groups: {len(groups)}**", "",
         "This is the native Polymorph scanner (crafting, smelting, blasting, smoking, smithing). "
         "Special recipes and independent magical stations may not be represented.", "",
         "## Breakdown", "", "| Recipe type | Conflict groups |", "|---|---:|"]
for typ, count in types.items():
    lines.append(f"| \`{typ}\` | {count} |")
lines += ["", "## Most involved namespaces", "",
          "| Namespace | Conflict groups containing it |", "|---|---:|"]
for ns, n in namespace_counts.most_common(20):
    lines.append(f"| \`{ns}\` | {n} |")
lines += ["", "## Cross-mod combinations", "", "| Pair | Groups |", "|---|---:|"]
for pair, n in crossmod_counts.most_common(30):
    lines.append(f"| {pair} | {n} |")
lines += ["", "## Actual conflicts", ""]
for g in groups[:120]:
    lines.append(f"- **{g['recipe_type']} group {g['group_number']}**: " +
                 ", ".join(f"\`{i}\`" for i in g["recipe_ids"]))
if len(groups) > 120:
    lines.append(f"- ... {len(groups) - 120} more groups in attached JSON / Polymorph log")
lines += ["", "## Special recipes skipped by Polymorph", ""]
for typ, ids in skipped.items():
    lines.append(f"- {typ}: {len(ids)} skipped (see JSON for IDs)")
if not skipped:
    lines.append("None reported.")
lines += ["", "**Interpretation:** A healthy end state is zero unintended collisions; "
         "not all scanner positives necessarily require changes. "
         "Keep Polymorph installed as a fallback.", ""]
md = "\n".join(lines)
args.output_md.write_text(md, encoding="utf-8")
summary_path = os.environ.get("GITHUB_STEP_SUMMARY")
if summary_path:
    with open(summary_path, "a", encoding="utf-8") as fh:
        fh.write(md)
print(f"Polymorph native audit: {len(groups)} potential conflict groups.")
print("Types:", types)
print("Top mod pairs:", crossmod_counts.most_common(15))
