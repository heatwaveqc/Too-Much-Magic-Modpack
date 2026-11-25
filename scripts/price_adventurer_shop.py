#!/usr/bin/env python3
"""Offline pricing proposal, not an anti-exploit checker. Supply same-build mapped EMC."""
import argparse
import json
from fractions import Fraction
from pathlib import Path
from audit_emc_coverage import emc_values

ROOT = Path(__file__).resolve().parents[1]
NICE_COUNTS = (1, 2, 4, 6, 8, 12, 16, 24, 32, 48, 64)

def price(total, coin):
    lo = (4 * total + 5 * coin - 1) // (5 * coin)
    hi = total // coin
    if lo > hi or hi < 1:
        return None
    target = Fraction(9 * total, 10 * coin)
    for step in (100, 50, 25, 10, 5, 2, 1):
        lower = ((lo + step - 1) // step) * step
        upper = (hi // step) * step
        if lower > upper:
            continue
        p = min(upper, max(lower, int(target / step + Fraction(1, 2)) * step))
        if step == 1 or abs(p - target) <= Fraction(total, 20 * coin):
            return p

def quote(count, unit_emc, coin):
    for n in (count, *sorted(NICE_COUNTS, key=lambda n: (abs(n-count), n))):
        p = price(n * unit_emc, coin)
        if p is not None:
            return n, p

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--emc", required=True, type=Path)
    args = parser.parse_args()
    values = emc_values(args.emc)
    catalog = json.loads((ROOT / "manifest/quest_shop_catalog.json").read_text())
    coin = catalog["coin_exchange"]["emc"]
    report = {"coin_emc": coin, "priced": [], "unresolved": []}
    for category in catalog["categories"]:
        for item, count, old in category["entries"]:
            emc = values.get(item, 0)
            result = quote(count, emc, coin) if emc else None
            if result is None:
                report["unresolved"].append({"item": item, "count": count, "retained_price": old})
                continue
            n, p = result
            report["priced"].append({"item": item, "count": n, "price": p,
                "goods_emc": n * emc, "paid_emc": p * coin})
    print(json.dumps(report, indent=2))

if __name__ == "__main__":
    main()
