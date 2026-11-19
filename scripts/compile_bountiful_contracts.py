#!/usr/bin/env python3
"""Compile an isolated Bountiful source candidate from reviewed inputs.
No runtime files are installed by this script. See docs/BOUNTIFUL-CONTRACTS.md.
"""
import argparse
import json
import math
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ID = re.compile(r"[a-z0-9_]+")
ITEM = re.compile(r"[a-z0-9_.-]+:[a-z0-9_./-]+")

def positive(value, label):
    if type(value) is not int or not 0 < value <= 2**31 - 1:
        raise ValueError("invalid positive integer: " + label)
    return value

def native_balance_family(family, spec, review, profile):
    """Build reward-first pools with explicit pair exclusions and bounded native clamping.
    The proof only covers the inspected board reputation range and exact item IDs.
    """
    tiers = profile["tiers"]
    rank = {name: i for i, name in enumerate(("COMMON", "UNCOMMON", "RARE", "EPIC", "LEGENDARY"))}
    scale = positive(profile["unit_scale"], "worth scale")
    percent = profile["filler_difficulty_percent"]
    if type(percent) is not int or not 0 <= percent <= 100:
        raise ValueError("invalid filler difficulty")
    if (profile["normal_board_reputation_min"], profile["normal_board_reputation_max"]) != (-30, 30):
        raise ValueError("unverified board reputation range")
    low_factor = (1 + percent / 100) * 0.6
    if low_factor < 1:
        raise ValueError("reputation permits positive material exchange")
    prices = review["effective_emc"]
    registered = set(review["registered_items"])
    def range_of(row, cap):
        lo = positive(row.get("min", row.get("count")), "minimum count")
        hi = positive(row.get("max", row.get("count")), "maximum count")
        if not lo <= hi <= cap:
            raise ValueError("invalid contract quantity range")
        return lo, hi
    def make_entry(kind, ident, lo, hi, worth):
        return {"type":kind,"content":ident,"amount":{"min":lo,"max":hi},"unitWorth":worth}
    def key_for(kind, ident):
        return kind+"_"+re.sub(r"[^a-z0-9_]","_",ident)
    rewards=spec.get("rewards",[spec["reward"]] if "reward" in spec else [])
    if not rewards:
        raise ValueError("empty reward pool")
    rews = {}
    seen = set()
    for row in rewards:
        ident = row["item"]
        if not ITEM.fullmatch(ident) or ident not in registered or ident not in review["approved_reward_items"] or ident in seen:
            raise ValueError("invalid or unapproved reward: "+ident)
        if family.get("reward_allowlist_locked") and ident not in family["reward_item_candidates"]:
            raise ValueError("reward outside owner-locked decree roster: "+ident)
        lo,hi=range_of(row,64)
        tier=row["rarity"]
        if tier not in rank:
            raise ValueError("unknown native rarity")
        if ident in family.get("premium_reward_candidates",[]) and rank[tier] < rank["EPIC"]:
            raise ValueError("premium payout requires epic or legendary rarity")
        emc=positive(prices[ident],ident)
        worth=max(emc,positive(tiers[tier]["floor_emc"],tier)/lo)/scale
        e=make_entry("item",ident,lo,hi,worth)
        e["rarity"]=tier
        e["repRequired"]=tiers[tier]["rep"]
        key=key_for("item",ident)
        if key in rews:
            raise ValueError("duplicate reward ID")
        rews[key]=e
        seen.add(ident)
    # A pure premium decree must still make contracts even at low rep; its
    # high effort floor supplies the difficulty. Rep gates apply only above
    # the lowest actual available rarity.
    lowest=min(rank[x["rarity"]] for x in rews.values())
    for e in rews.values():
        if rank[e["rarity"]]==lowest:
            e["repRequired"]=-30
    objs={}
    for row in spec["objectives"]:
        kind=row.get("type","item")
        if kind=="item":
            ident=row["item"]
            if not ITEM.fullmatch(ident) or ident not in registered or ident not in family["items"]:
                raise ValueError("invalid or unapproved delivery: "+ident)
            emc=positive(prices[ident],ident)
            cap=profile["objective_caps"]["delivery"]
        elif kind=="entity":
            ident=row["entity"]
            if (not ITEM.fullmatch(ident) or ident=="minecraft:player" or ident not in family.get("kill_candidates",[])
                    or ident not in review.get("registered_entities",[]) or ident not in review.get("approved_kill_entities",[])):
                raise ValueError("unreviewed entity objective: "+ident)
            emc=positive(review["approved_kill_emc_budget"][ident],ident)
            cap=profile["objective_caps"]["kill"]
        else:
            raise ValueError("unsupported objective type")
        lo,hi=range_of(row,cap)
        key=key_for(kind,ident)
        if key in objs:
            raise ValueError("duplicate objective ID")
        e=make_entry(kind,ident,lo,hi,emc/scale)
        if kind=="entity":
            e["timeMult"]=3.0
        objs[key]=e
    if not objs:
        raise ValueError("empty objective pool")
    used=set()
    for reward in rews.values():
        high_factor=(1 + percent / 100)*(1-reward["repRequired"]/75)
        need=reward["amount"]["max"]*reward["unitWorth"]*high_factor
        allowed=[]
        forbidden=[]
        for key,obj in objs.items():
            # Native picks any valid filler within variance or nearest fallback,
            # then ceil and clamp. Eliminate all pairs that cannot cover the
            # maximum obligation at worst eligible rep; thus no emergency partial filler.
            if (obj["content"]==reward["content"] or
                obj["amount"]["max"]*obj["unitWorth"]+1e-9<need):
                forbidden.append({"type":obj["type"],"content":obj["content"]})
            else:
                allowed.append(key)
        if not allowed:
            raise ValueError("no feasible objective for reward: "+reward["content"])
        used.update(allowed)
        if forbidden:
            reward["forbids"]=forbidden
    return {k:v for k,v in objs.items() if k in used},rews

def compile_candidate(catalog, audit, review):
    if review.get("baseline") != catalog["baseline"]:
        raise ValueError("review baseline differs from catalog")
    if review.get("audit_hash") != audit["bountiful"]["hashes"]["sha512"]:
        raise ValueError("review is not bound to audited artifact")
    if review.get("settlement") not in {"approved_item_barter", "approved_thematic_materials"}:
        raise ValueError("currency/settlement decision absent; do not generate runtime pools")
    registered = set(review["registered_items"])
    prices = review["effective_emc"]
    approved = review["approved_entries"]
    if not approved:
        raise ValueError("no approved entries")
    families = {x["id"]:x for x in catalog["general"] + catalog["schools"]
                if not x.get("status", "").startswith("held_")}
    if set(approved) - set(families):
        raise ValueError("unknown or held decree")
    output = {}
    translations = {}
    use_native = review.get("balance_profile") == "native_v1"
    if review.get("balance_profile") not in (None, "native_v1"):
        raise ValueError("unknown balance profile")
    profile = catalog["balance_draft"] if use_native else None
    def dump(path, obj):
        output[path] = json.dumps(obj, indent=2, sort_keys=True) + "\n"
    # Built-in loader strips .json and interprets exclusion entries as regex.
    # Avoid its limited '*' character class; exclude all resource-backed pools
    # and decrees, including add-on paths. Our flat files load separately.
    dump("config/bountiful/bountiful.json", {"general":{"dataPathsToExclude":["bounty_pools/.+","bounty_decrees/.+"]},"bounty":{"allowDecreeMixing":False,"shouldHaveTimersAndExpire":True,"initialCountPreference":{"min":1,"max":1},"fillerCountPreference":{"min":1,"max":1},"reverseMatchingAlgorithm":False,"fillerDifficultyModifierPercent":profile["filler_difficulty_percent"] if use_native else 0}})
    def entry(item, count, worth):
        if not ITEM.fullmatch(item) or item not in registered:
            raise ValueError("unregistered item: " + item)
        return {"type":"item","content":item,"amount":{"min":count,"max":count},"unitWorth":worth}
    for key, spec in sorted(approved.items()):
        if not ID.fullmatch(key):
            raise ValueError("bad decree id")
        family = families[key]
        if use_native:
            objs, rews = native_balance_family(family, spec, review, profile)
        else:
            # Native generation chooses one objective and one reward. Validate
            # every possible cross-pair; no custom kill tracker or Hunter hook.
            rewards = spec.get("rewards", [spec["reward"]] if "reward" in spec else [])
            if not rewards:
                raise ValueError("empty reward pool")
            rews = {}
            reward_ids = set()
            reward_total = 0
            for reward in rewards:
                reward_id = reward["item"]
                reward_n = positive(reward["count"], "reward count")
                if reward_id not in review["approved_reward_items"] or reward_id in reward_ids:
                    raise ValueError("reward not explicitly allowlisted or duplicate")
                if family.get("reward_allowlist_locked") and reward_id not in family["reward_item_candidates"]:
                    raise ValueError("reward outside owner-locked decree roster: " + reward_id)
                reward_emc = positive(prices[reward_id], reward_id)
                if reward_n > 64:
                    raise ValueError("reward exceeds initial pilot stack cap")
                reward_total = max(reward_total, reward_n * reward_emc)
                reward_ids.add(reward_id)
                reward_key = reward_id.replace(":", "_").replace("/", "_").replace(".", "_").replace("-", "_")
                if reward_key in rews:
                    raise ValueError("duplicate reward ID")
                rews[reward_key] = entry(reward_id, reward_n, reward_emc)
            objs = {}
            for row in spec["objectives"]:
                n = positive(row["count"], "objective count")
                kind = row.get("type", "item")
                if kind == "entity":
                    entity = row["entity"]
                    if (not ITEM.fullmatch(entity) or entity == "minecraft:player"
                            or entity not in family.get("kill_candidates", [])
                            or entity not in review.get("registered_entities", [])
                            or entity not in review.get("approved_kill_entities", [])):
                        raise ValueError("unreviewed entity objective: " + entity)
                    if n > 256:
                        raise ValueError("kill count exceeds pilot cap")
                    worth = positive(review["approved_kill_emc_budget"][entity], entity)
                    if n * worth < reward_total:
                        raise ValueError("kill payout exceeds reviewed budget: " + entity)
                    name = "kill_" + entity.replace(":", "_").replace("/", "_").replace(".", "_").replace("-", "_")
                    obj = {"type":"entity","content":entity,"amount":{"min":n,"max":n},"unitWorth":worth}
                elif kind == "item":
                    item = row["item"]
                    if item not in family["items"] or item in reward_ids:
                        raise ValueError("objective outside candidate family or self-trade")
                    if n > 256:
                        raise ValueError("delivery exceeds pilot cap")
                    emc = positive(prices[item], item)
                    if n * emc < reward_total:
                        raise ValueError("positive EMC exchange: " + item)
                    name = item.replace(":", "_").replace("/", "_").replace(".", "_").replace("-", "_")
                    obj = entry(item,n,emc)
                else:
                    raise ValueError("unsupported objective type")
                if name in objs:
                    raise ValueError("duplicate objective ID")
                objs[name] = obj
            if not objs:
                raise ValueError("empty objective pool")
        stem = "tmm_" + key
        dump("config/bountiful/bounty_pools/"+stem+"_objs.json", {"replace":True,"content":objs})
        dump("config/bountiful/bounty_pools/"+stem+"_rews.json", {"replace":True,"content":rews})
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
