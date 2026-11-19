#!/usr/bin/env python3
"""Compile an isolated Bountiful source candidate from reviewed inputs.
No runtime files are installed by this script. See docs/BOUNTIFUL-CONTRACTS.md.
"""
import argparse
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ID = re.compile(r"[a-z0-9_]+")
ITEM = re.compile(r"[a-z0-9_.-]+:[a-z0-9_./-]+")

def positive(value, label):
    if type(value) is not int or not 0 < value <= 2**31 - 1:
        raise ValueError("invalid positive integer: " + label)
    return value

def compile_candidate(catalog, audit, review):
    if review.get("baseline") != catalog["baseline"]:
        raise ValueError("review baseline differs from catalog")
    if review.get("audit_hash") != audit["bountiful"]["hashes"]["sha512"]:
        raise ValueError("review is not bound to audited artifact")
    if review.get("settlement") != "approved_item_barter":
        raise ValueError("currency/settlement decision absent; do not generate runtime pools")
    registered = set(review["registered_items"])
    prices = review["effective_emc"]
    approved = review["approved_entries"]
    if not approved:
        raise ValueError("no approved entries")
    families = {x["id"]:x for x in catalog["general"] + catalog["schools"]}
    if set(approved) - set(families):
        raise ValueError("unknown or held decree")
    output = {}
    translations = {}
    def dump(path, obj):
        output[path] = json.dumps(obj, indent=2, sort_keys=True) + "\n"
    # Built-in loader strips .json and interprets exclusion entries as regex.
    # Avoid its limited '*' character class; exclude all resource-backed pools
    # and decrees, including add-on paths. Our flat files load separately.
    dump("config/bountiful/bountiful.json", {"general":{"dataPathsToExclude":["bounty_pools/.*","bounty_decrees/.*"]},"bounty":{"allowDecreeMixing":False,"shouldHaveTimersAndExpire":True,"initialCountPreference":{"min":1,"max":1},"fillerCountPreference":{"min":1,"max":1}}})
    def entry(item, count, worth):
        if not ITEM.fullmatch(item) or item not in registered:
            raise ValueError("unregistered item: " + item)
        return {"type":"item","content":item,"amount":{"min":count,"max":count},"unitWorth":worth}
    for key, spec in sorted(approved.items()):
        if not ID.fullmatch(key):
            raise ValueError("bad decree id")
        family = families[key]
        # One reward, one exact delivery. Each permitted input must cover the
        # actual EMC of the selected reward; even generator clamping cannot
        # create a positive material exchange. No implicit coin conversion.
        reward = spec["reward"]
        reward_id = reward["item"]
        reward_n = positive(reward["count"], "reward count")
        if reward_id not in review["approved_reward_items"]:
            raise ValueError("reward not explicitly allowlisted")
        reward_emc = positive(prices[reward_id], reward_id)
        if reward_n > 64:
            raise ValueError("reward exceeds initial pilot stack cap")
        reward_total = reward_n * reward_emc
        objs = {}
        for row in spec["objectives"]:
            item = row["item"]
            n = positive(row["count"], "objective count")
            if item not in family["items"] or item == reward_id:
                raise ValueError("objective outside candidate family or self-trade")
            if n > 256:
                raise ValueError("delivery exceeds pilot cap")
            emc = positive(prices[item], item)
            if n * emc < reward_total:
                raise ValueError("positive EMC exchange: " + item)
            name = item.replace(":", "_").replace("/", "_").replace(".", "_").replace("-", "_")
            if name in objs:
                raise ValueError("duplicate objective ID")
            objs[name] = entry(item,n,emc)
        if not objs:
            raise ValueError("empty objective pool")
        stem = "tmm_" + key
        dump("config/bountiful/bounty_pools/"+stem+"_objs.json", {"replace":True,"content":objs})
        dump("config/bountiful/bounty_pools/"+stem+"_rews.json", {"replace":True,"content":{"settlement":entry(reward_id,reward_n,reward_emc)}})
        dump("config/bountiful/bounty_decrees/"+stem+".json", {"replace":True,"objectives":[stem+"_objs"],"rewards":[stem+"_rews"],"canSpawn": key in {x["id"] for x in catalog["general"]},"canReveal": key in {x["id"] for x in catalog["general"]},"canWanderBuy":False,"linkedProfessions":[]})
        translations["bountiful.decree."+stem+".name"] = family["name"]
    dump("kubejs/assets/bountiful/lang/en_us.json", translations)
    return output

def main():
    p = argparse.ArgumentParser()
    p.add_argument("--audit",type=Path,required=True)
    p.add_argument("--review",type=Path,required=True)
    p.add_argument("--out",type=Path,required=True)
    a=p.parse_args()
    out=a.out.resolve()
    if out == ROOT or ROOT in out.parents:
        raise ValueError("output must be an isolated directory outside the checkout")
    if out.exists():
        raise ValueError("output must not exist; never merge stale configs")
    files=compile_candidate(json.loads((ROOT/"manifest/bountiful-contract-candidates.json").read_text()),json.loads(a.audit.read_text()),json.loads(a.review.read_text()))
    for name, contents in files.items():
        path=out/name
        path.parent.mkdir(parents=True,exist_ok=True)
        path.write_text(contents,encoding="utf-8")
    print(f"Wrote {len(files)} candidate files; NOT installed")

if __name__=="__main__":
    main()
