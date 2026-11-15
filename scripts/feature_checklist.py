#!/usr/bin/env python3
"""Validate, render, summarize and explicitly update the TMM feature ledger.

The ledger is intentionally independent of gameplay/packwiz. Passing its own
checks is NOT evidence a feature works in Minecraft. Only an owner-reported,
source-linked in-world result may become player_pass.
"""
from __future__ import annotations

import argparse
import collections
import json
import pathlib
import re
import sys

ROOT = pathlib.Path(__file__).resolve().parents[1]
REGISTRY = ROOT / "manifest/feature-checklist.json"
REPORT = ROOT / "docs/FEATURE-CHECKLIST.md"
STATES = {
    "planned", "in_progress", "source_ready", "server_smoked",
    "player_partial", "player_pass", "blocked", "deferred"
}
DISPLAY = {
    "planned": "Planned",
    "in_progress": "In progress",
    "source_ready": "Source ready; gameplay unverified",
    "server_smoked": "Server-booted; mechanics unverified",
    "player_partial": "Player test partial",
    "player_pass": "Player-tested PASS",
    "blocked": "Blocked",
    "deferred": "Deferred",
}

def load() -> dict:
    data = json.loads(REGISTRY.read_text(encoding="utf-8"))
    if data.get("schema_version") != 1:
        raise ValueError("Unknown ledger schema")
    features = data.get("features")
    if not isinstance(features, list) or not features:
        raise ValueError("Missing feature list")
    ids = set()
    for idx, row in enumerate(features, 1):
        for key in ("id", "area", "title", "state", "acceptance", "source", "evidence"):
            if key not in row or not isinstance(row[key], str):
                raise ValueError(f"Feature {idx} requires string {key}")
        if row["id"] in ids or not re.fullmatch(r"[a-z][a-z0-9_]*\.[a-z][a-z0-9_]*", row["id"]):
            raise ValueError("Duplicate or invalid feature ID: " + row["id"])
        ids.add(row["id"])
        if row["state"] not in STATES:
            raise ValueError("Unknown state for " + row["id"])
        if not all(row[k].strip() for k in ("area", "title", "acceptance")):
            raise ValueError("Empty feature title/area/acceptance: " + row["id"])
        if row["state"] == "player_pass" and not row["evidence"].strip():
            raise ValueError("Player PASS requires evidence: " + row["id"])
        if row["state"] == "player_pass" and not any(
            x in row["evidence"].lower()
            for x in ("https://", "docs/", "tests/")
        ):
            raise ValueError("Player PASS evidence needs a reviewable path or URL: " + row["id"])
    return data

def link_to(path: str) -> str:
    if not path:
        return ""
    if path.startswith("https://"):
        return path
    return "../" + path

def render(data: dict) -> str:
    features = data["features"]
    byarea = collections.OrderedDict()
    for row in features:
        byarea.setdefault(row["area"], []).append(row)
    totals = collections.Counter(x["state"] for x in features)
    out = [
        "# Too Much Magic — full feature & test checklist",
        "",
        "> Generated from [the machine-readable registry](../manifest/feature-checklist.json) by ",
        "> `python3 scripts/feature_checklist.py render`. Do not edit this Markdown by hand.",
        "",
        f"**{len(features)} acceptance items** across **{len(byarea)} tracks**. "
        f"**{totals['player_pass']}** have bounded owner-reported player PASS evidence; "
        f"**{totals['player_partial']}** have partial player checks; "
        f"**{totals['server_smoked']}** are server-smoked only. "
        "This is not a stable release.",
        "",
        "## Reading this checklist",
        "",
        "- **Checked** means owner-reported player-tested PASS for this specific, bounded criterion—not full release sign-off.",
        "- **Source ready** means code or configuration exists, not necessarily installed.",
        "- **Server-smoked** means a component or pack boots; no player mechanic is implied.",
        "- **Player partial** means at least one scenario was seen working, but the full acceptance criterion is still open.",
        "- **Planned/blocked/deferred** are neither implemented nor tested.",
        "- Evidence is retained even if later integration breaks a feature; update the state and evidence for new regressions.",
        "- Every new feature, boss gate, cross-mod bridge or acceptance failure should get a stable ID here.",
        "",
        "## Snapshot",
        "",
        "| State | Items |",
        "| --- | ---: |",
    ]
    for state in (
        "player_pass", "player_partial", "server_smoked", "source_ready",
        "in_progress", "planned", "blocked", "deferred"
    ):
        out.append(f"| {DISPLAY[state]} | {totals[state]} |")
    out.append("")
    out.extend(["## Master checklist", ""])
    for category, rows in byarea.items():
        out.extend([f"### {category} ({len(rows)})", ""])
        for row in rows:
            checked = "x" if row["state"] == "player_pass" else " "
            src = f" [source]({link_to(row['source'])})" if row["source"] else ""
            evidence = f" [evidence]({link_to(row['evidence'])})" if row["evidence"] else ""
            title = row["title"].replace("|", "/")
            test = row["acceptance"].replace("\n", " ").replace("|", "/")
            out.append(
                f"- [{checked}] **{title}** (`{row['id']}`) "
                f"— *{DISPLAY[row['state']]}*. {test}.{src}{evidence}"
            )
        out.append("")
    out.extend([
        "## When to check a box",
        "",
        "1. Record the **specific build SHA or artifact** and a client/real-world outcome in the evidence source.",
        "2. Run the concrete acceptance test and meaningful negative/bypass cases (including relog and multiplayer where relevant).",
        "3. Promote **only that row** to `player_pass`; keep adjacent incomplete features open.",
        "4. Do not auto-mark gameplay PASS from a Gradle test, JSON validator, dedicated-server ready line or a generated quest.",
        "",
        "Update safely: `python3 scripts/feature_checklist.py mark <id> <state> --evidence <path-or-url>`",
        "followed by `python3 scripts/feature_checklist.py render`. "
        "Use `python3 scripts/feature_checklist.py render --check` in CI.",
        "",
        "## Gate policy",
        "",
        "The earlier ProjectE-after-Chaos design is under revision. "
        "See [the tiered ProjectE gate proposal](PROJECTE-TIERED-GATING-PROPOSAL.md): "
        "initial transmutation after either Gaia Guardian I **or** Neo Vitae Foreman; "
        "extreme EMC generation and Project Expansion after Chaos Guardian. "
        "This is an explicit **proposal**, not an implemented server-side gate. "
        "Never use a quest checkbox as the only enforcement.",
        "",
    ])
    return "\n".join(out)

def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("command", choices=["validate", "summary", "render", "mark"])
    parser.add_argument("id", nargs="?")
    parser.add_argument("state", nargs="?")
    parser.add_argument("--evidence", default=None)
    parser.add_argument("--check", action="store_true")
    args = parser.parse_args()
    data = load()
    if args.command == "mark":
        if not args.id or args.state not in STATES:
            parser.error("mark needs <id> <valid_state>")
        matching = [x for x in data["features"] if x["id"] == args.id]
        if len(matching) != 1:
            raise ValueError("Unknown feature ID: " + args.id)
        row = matching[0]
        if args.state in ("player_partial", "player_pass") and not (
            args.evidence or row["evidence"]
        ):
            parser.error("Player-tested states need --evidence PATH/URL")
        row["state"] = args.state
        if args.evidence is not None:
            row["evidence"] = args.evidence
        REGISTRY.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
        data = load()
        print(f"Updated {args.id} -> {args.state}; now run render")
    elif args.command == "render":
        output = render(data)
        if args.check:
            if not REPORT.is_file() or REPORT.read_text(encoding="utf-8") != output:
                raise ValueError("Generated checklist is stale; run feature_checklist.py render")
            print("PASS: feature checklist matches registry")
        else:
            REPORT.write_text(output, encoding="utf-8")
            print(f"Rendered {len(data['features'])} feature acceptance items into {REPORT}")
    elif args.command == "summary":
        counts = collections.Counter(x["state"] for x in data["features"])
        print(json.dumps({
            "feature_count": len(data["features"]),
            "categories": len(set(x["area"] for x in data["features"])),
            "status": {key: counts[key] for key in sorted(STATES)},
            "player_pass_not_stable": True,
        }, indent=2))
    else:
        print(f"PASS: {len(data['features'])} unique feature entries and honest states")

if __name__ == "__main__":
    try:
        main()
    except (ValueError, OSError, json.JSONDecodeError) as exc:
        sys.exit("FEATURE CHECKLIST INVALID: " + str(exc))
