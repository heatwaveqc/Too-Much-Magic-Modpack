#!/usr/bin/env python3
"""Generate or validate the narrow, non-production Bountiful QA overlay."""
import argparse
import json
import tomllib
from pathlib import Path
from compile_bountiful_contracts import compile_candidate

ROOT = Path(__file__).resolve().parents[1]
BASE = ROOT / "compat/bountiful/live-pilot"

def build():
    review=json.loads((ROOT/"manifest/bountiful-live-pilot-review.json").read_text())
    catalog=json.loads((ROOT/"manifest/bountiful-contract-candidates.json").read_text())
    pin=tomllib.loads((ROOT/"mods/bountiful.pw.toml").read_text())
    assert pin["download"]["hash-format"]=="sha512"
    assert review["audit_hash"]==pin["download"]["hash"]
    assert set(review["approved_entries"])=={"hunting","culinary","fishing","ars_nouveau"}
    for key in review["approved_gateway_ids"]:
        path=ROOT/"kubejs/data/tmm/gateways"/(key.split(":")[1]+".json")
        gateway=json.loads(path.read_text())
        assert gateway["type"]=="gateways:normal" and len(gateway["waves"])==3, key
    result=compile_candidate(catalog,{"bountiful":{"hashes":{"sha512":review["audit_hash"]}}},review)
    assert len(result)==18, sorted(result)
    return result

def main():
    parser=argparse.ArgumentParser()
    parser.add_argument("--verify",action="store_true")
    parser.add_argument("--write",action="store_true")
    args=parser.parse_args()
    if args.verify==args.write: parser.error("select exactly one mode")
    result=build()
    for relative,body in result.items():
        file=BASE/relative
        if args.write:
            file.parent.mkdir(parents=True,exist_ok=True)
            file.write_text(body)
        else:
            assert file.is_file(),f"missing overlay {relative}"
            assert json.loads(file.read_text())==json.loads(body),f"stale overlay {relative}"
    present={str(x.relative_to(BASE)) for x in BASE.rglob("*.json")}
    assert present==set(result),f"overlay file set differs: {sorted(present^set(result))}"
    print(f"Verified {len(result)} first-live Bountiful pilot configs. No boot.")

if __name__=="__main__":
    main()
