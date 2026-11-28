#!/usr/bin/env python3
"""Generate an auditable Q Shop candidate from a same-invocation #47 export.
Never install or overwrite a live shop, and never manufacture EMC estimates.
"""
from __future__ import annotations

import argparse
import hashlib
import json
from fractions import Fraction
from pathlib import Path
from uuid import UUID, uuid5

from audit_emc_coverage import items, emc_values
from audit_emc_snapshot import validate

ROOT = Path(__file__).resolve().parents[1]
POLICY = ROOT / "manifest/adventurer-shop-policy.json"
NICE_COUNTS = (1, 2, 4, 8, 16, 32, 64)
NICE_PRICE_STEPS = (100, 50, 25, 10, 5, 2, 1)
UUID_NS = UUID("bf5bb0e1-0429-4a67-9630-4a14c928e6b2")
LABELS = {
    "ores": ("Ores", "minecraft:iron_ore"),
    "raw_materials": ("Raw Materials", "minecraft:amethyst_shard"),
    "food": ("Food & Drink", "minecraft:bread"),
    "artifacts": ("Artifacts", "minecraft:emerald"),
    "relics": ("Relics", "minecraft:ender_pearl"),
    "iron_gear": ("Iron Gear", "minecraft:iron_sword"),
    "ingots": ("Sell Ingots", "minecraft:iron_ingot"),
    "gems": ("Sell Gems", "minecraft:diamond"),
    "animal_products": ("Sell Animal Products", "minecraft:cooked_beef"),
    "plant_products": ("Sell Plant Products", "minecraft:wheat"),
}


def quote(unit, max_stack, direction, policy):
    """Exact rational bounds, then choose a readable price near the target.
    Prefer smaller sensible lots when several options are similarly readable.
    Never exceed the actual default stack size or provider integer limit.
    """
    if type(unit) is not int or unit <= 0 or type(max_stack) is not int or max_stack < 1:
        return None
    if direction not in ("BUY", "SELL"):
        raise ValueError("Invalid direction")
    coin = policy["coin_emc"]
    target = Fraction(policy["target_efficiency_pct"], 100)
    low = Fraction(policy["min_efficiency_pct"], 100)
    high = Fraction(policy["max_efficiency_pct"], 100)
    counts = sorted({n for n in (*NICE_COUNTS, max_stack) if n <= max_stack})
    best = None
    for n in counts:
        total = n * unit
        a, b, ideal = ((total / (high * coin), total / (low * coin),
                        total / (target * coin)) if direction == "BUY"
                       else (total * low / coin, total * high / coin, total * target / coin))
        lo = max(1, -(-a.numerator // a.denominator))
        hi = min(policy["max_price"], b.numerator // b.denominator)
        if lo > hi:
            continue
        for step in NICE_PRICE_STEPS:
            lower, upper = -(-lo // step) * step, hi // step * step
            if lower > upper:
                continue
            price = min(upper, max(lower, int(ideal / step + Fraction(1, 2)) * step))
            efficiency = (Fraction(total, price * coin) if direction == "BUY"
                          else Fraction(price * coin, total))
            # Reward target accuracy, round prices and small transaction size.
            # Prefer 1–64 coins; do not enlarge cheap batches just to find round hundreds.
            rank = (abs(efficiency-target) + Fraction(n-1, 6400)
                    + Fraction(len(str(price))-1, 1000) - Fraction(min(step, 10), 10000),
                    n, price)
            if best is None or rank < best[0]:
                best = (rank, n, price, efficiency)
    return best[1:] if best else None


def generate(facts, values, policy, template):
    entries = {}
    audit = []
    prefix = "tmm:shop/"
    blocked_namespaces = set(policy["component_sensitive_namespaces"])
    for item, row in sorted(facts.items()):
        tags = set(row["tags"])
        ns, path = item.split(":", 1)
        for direction, categories in policy["category_order"].items():
            matched = [cat for cat in categories
                       if prefix + direction.lower() + "/" + cat in tags]
            # Namespace candidates are audited, never turned into plain stacks.
            if direction == "BUY" and ns in ("artifacts", "relics", "reliquified_artifacts"):
                matched = matched or ["artifacts" if ns == "artifacts" else "relics"]
            if direction == "BUY" and row["food"] and not matched:
                matched = ["food"]
            if not matched:
                continue
            cat = matched[0]
            record = {"item": item, "direction": direction, "category": cat,
                      "matched_categories": matched, "unit_emc": values.get(item)}
            reason = None
            stages = []
            if prefix + "excluded" in tags or row["spawn_egg"]:
                reason = "excluded"
            elif direction == "BUY" and (prefix + "buy/excluded" in tags
                   or path.endswith(("_sapling", "_spawn_egg", "_seeds", "_seed"))
                   or "seeditem" in path):
                reason = "market_only"
            elif ns in blocked_namespaces:
                reason = "component_review"
            elif direction == "BUY":
                eligible = [d for d in policy["dimensions"] if prefix + "eligible/" + d in tags]
                if not eligible:
                    reason = "acquisition_review"
                else:
                    # Overworld takes precedence. Multiple non-overworld gates are
                    # ambiguous (OR is not native AND); hold for explicit provenance.
                    if "overworld" in eligible:
                        eligible = ["overworld"]
                    if len(eligible) != 1:
                        reason = "ambiguous_dimension"
                    else:
                        stage = policy["dimensions"][eligible[0]]
                        if stage:
                            stages.append(stage)
                        record["dimension"] = eligible[0]
            if reason is None:
                result = quote(values.get(item, 0), row["max_stack"], direction, policy)
                if result is None:
                    reason = "missing_emc" if not values.get(item) else "no_valid_lot"
            if reason:
                audit.append({**record, "status": reason})
                continue
            count, price, eff = result
            entry = {"uuid": "tmm-generated-" + str(uuid5(UUID_NS, direction + ":" + item)),
                     "type": direction, "item": {"item": item, "count": count},
                     "currency": "coins", "price": price}
            if stages:
                entry["requiredStages"] = stages
            entries.setdefault((direction, cat), []).append(entry)
            audit.append({**record, "status": "stage_bridge_pending" if stages else "quoted",
                          "count": count, "price": price,
                          "efficiency_pct": round(float(eff * 100), 4),
                          "uuid": entry["uuid"]})
    tabs = []
    for direction, categories in policy["category_order"].items():
        for cat in categories:
            group = entries.get((direction, cat), [])
            if group:
                name, icon = LABELS[cat]
                tabs.append({"uuid": "tmm-generated-" + direction.lower() + "-" + cat,
                             "name": name, "icon": icon, "entries": group})
    # Keep exactly native parity exchange; never reprice the currency.
    exchange = [tab for tab in template["tabs"]
                if tab["entries"] and all(e.get("item", {}).get("item") == "tmm:coin"
                                         for e in tab["entries"])]
    if len(exchange) != 1 or sorted(e["type"] for e in exchange[0]["entries"]) != ["BUY","SELL"]:
        raise ValueError("Expected exactly one two-way native coin exchange tab")
    if any(e["price"] != 1 or e["item"]["count"] != 1 or e["currency"] != "coins"
           for e in exchange[0]["entries"]):
        raise ValueError("Coin exchange is not 1:1")
    return {**template, "tabs": tabs + exchange}, audit


def load_snapshot(path, expected_sha):
    report = validate(path, expected_sha)
    fact_file = path / "shop-items.json"
    raw = fact_file.read_bytes()
    meta = json.loads((path / "metadata.json").read_text())
    if hashlib.sha256(raw).hexdigest() != meta.get("shop_items_sha256"):
        raise ValueError("Shop facts missing or digest mismatch; recapture augmented #47 export")
    payload = json.loads(raw)
    if (payload.get("schema_version") != 1
        or payload.get("source_sha_label") != report["source_sha_label"]
        or payload.get("projecte_map_generation") != report["projecte_map_generation"]):
        raise ValueError("Shop facts are not from this EMC snapshot")
    facts = payload["items"]
    if set(facts) != items(path / "items.json"):
        raise ValueError("Shop fact registry differs from EMC snapshot")
    for key, row in facts.items():
        if (type(row.get("max_stack")) is not int or row["max_stack"] < 1
            or type(row.get("food")) is not bool or type(row.get("spawn_egg")) is not bool
            or not isinstance(row.get("tags"), list)
            or any(not isinstance(t, str) or ":" not in t for t in row["tags"])):
            raise ValueError("Invalid shop facts: " + key)
    return facts, emc_values(path / "emc.json"), report


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("snapshot", type=Path)
    p.add_argument("--expected-source-sha", required=True)
    p.add_argument("--output-dir", required=True, type=Path, help="New directory outside active shop config")
    args = p.parse_args()
    facts, values, report = load_snapshot(args.snapshot, args.expected_source_sha)
    policy = json.loads(POLICY.read_text())
    template = json.loads((ROOT / "config/qshop/shops/tmm.json").read_text())
    candidate, audit = generate(facts, values, policy, template)
    # No overwrite and no live config filename: explicit review/cutover required.
    args.output_dir.mkdir(parents=True, exist_ok=False)
    (args.output_dir / "shop-candidate.json").write_text(json.dumps(candidate, indent=2) + "\n")
    counts = {}
    for row in audit:
        counts[row["status"]] = counts.get(row["status"], 0) + 1
    (args.output_dir / "shop-audit.json").write_text(json.dumps({
        "source_sha_label": report["source_sha_label"], "source_sha_verified": False,
        "projecte_map_generation": report["projecte_map_generation"], "counts": counts,
        "legacy_entries_not_migrated": [e["uuid"] for t in template["tabs"] for e in t["entries"]
                                         if e.get("item", {}).get("item") != "tmm:coin"],
        "offers": audit, "install_ready": False}, indent=2) + "\n")
    print(json.dumps(counts, indent=2))


if __name__ == "__main__":
    main()
