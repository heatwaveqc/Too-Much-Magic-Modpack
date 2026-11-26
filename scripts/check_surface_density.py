#!/usr/bin/env python3
"""Verify the bounded frequency-only surface balance overlay; never boot Minecraft."""
import argparse
import copy
import hashlib
import json
import re
import tomllib
import zipfile
from collections import Counter, defaultdict
from pathlib import Path

from audit_worldgen_resources import parse_resource
from download_mods import fetch_verified

MANIFEST = "research/worldgen-surface-density/first-pass.json"
PROTECTED = {"astralsorcery:focal_point_structures", "botania:exploration_mushroom"}
FORBIDDEN_MEMBERS = {"astralsorcery:focal_point", "botania:exploration_mushroom"}
HELD_NAMESPACES = {"minecraft", "alexscaves", "betterdungeons", "bettermineshafts",
                   "dungeoncrawl", "pastel", "cataclysm", "mowziesmobs",
                   "betterend", "aether", "twilightforest", "the_bumblezone",
                   "theaurorian", "eternal_starlight", "voidscape"}
ID = re.compile(r"[a-z0-9_.-]+:[a-z0-9_./-]+")

def sha256(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()

def path_for(rid):
    if not isinstance(rid,str) or not ID.fullmatch(rid) or ".." in rid.split(":")[1].split("/"):
        raise ValueError("invalid resource id")
    ns,name=rid.split(":")
    return f"kubejs/data/{ns}/worldgen/structure_set/{name}.json"

def expected(original):
    result=copy.deepcopy(original)
    p=result["placement"]
    if p["type"]!="minecraft:random_spread":
        raise ValueError("nonstandard placement codec")
    chance=p.get("frequency",1)
    if type(chance) not in (float,int) or not 0<chance<=1:
        raise ValueError("bad original frequency")
    p["frequency"]=chance*0.5
    return result

def check(root, verify_archives=False):
    manifest=json.loads((root/MANIFEST).read_text())
    if manifest["schema"]!=1 or manifest["multiplier"]!=0.5 or set(manifest["protected"])!=PROTECTED:
        raise ValueError("unsupported policy")
    index=tomllib.loads((root/"index.toml").read_text())
    pack=tomllib.loads((root/"pack.toml").read_text())
    if index["hash-format"]!="sha256" or pack["index"]["hash"]!=sha256(root/"index.toml"):
        raise ValueError("pack index mismatch")
    indexed={e["file"]:e["hash"] for e in index["files"]}
    if len(indexed)!=len(index["files"]): raise ValueError("duplicate packwiz index path")
    pins=manifest["pins"]
    for pin,digest in pins.items():
        if indexed.get(pin)!=digest or sha256(root/pin)!=digest:
            raise ValueError("source pin changed; re-review exact upstream sets: "+pin)
    rows=manifest["entries"]
    if not rows: raise ValueError("empty policy")
    ids=set()
    by_pin=defaultdict(list)
    for row in rows:
        rid=row["id"]
        path=path_for(rid)
        if rid in ids or rid in PROTECTED or rid.split(":")[0] in HELD_NAMESPACES:
            raise ValueError("duplicate or protected set: "+rid)
        ids.add(rid)
        if row["pin"] not in pins: raise ValueError("missing source pin")
        by_pin[row["pin"]].append(row)
        if any(e["structure"] in FORBIDDEN_MEMBERS for e in row["original"]["structures"]):
            raise ValueError("protected structure member: "+rid)
        if indexed.get(path)!=sha256(root/path):
            raise ValueError("unindexed or modified override: "+rid)
        if json.loads((root/path).read_text())!=expected(row["original"]):
            raise ValueError("not a frequency-only half reduction: "+rid)
    if set(by_pin)!=set(pins): raise ValueError("unused source pins")
    for rid in PROTECTED:
        if (root/path_for(rid)).exists(): raise ValueError("protected set override exists: "+rid)
    if verify_archives:
        import io
        for pin,entries in sorted(by_pin.items()):
            mod=tomllib.loads((root/pin).read_text())
            # Canonical downloader verifies the exact recorded pin digest.
            binary=fetch_verified(mod)
            with zipfile.ZipFile(io.BytesIO(binary)) as z:
                for row in entries:
                    path=path_for(row["id"]).removeprefix("kubejs/")
                    if z.namelist().count(path)!=1:
                        raise ValueError("missing or duplicate upstream set: "+path)
                    source,mode=parse_resource(z.read(path))
                    if source!=row["original"]:
                        raise ValueError("audited original differs from verified JAR: "+row["id"])
            print(f"VERIFIED {pin}: {len(entries)} exact original sets",flush=True)
    print("SURFACE_DENSITY "+json.dumps({"sets":len(ids),"pins":len(by_pin),"archives_verified":verify_archives,
          "by_namespace":dict(sorted(Counter(r.split(":")[0] for r in ids).items()))}),flush=True)

if __name__=="__main__":
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--pack-root",type=Path,default=Path(__file__).resolve().parents[1])
    p.add_argument("--verify-archives",action="store_true")
    a=p.parse_args()
    check(a.pack_root,a.verify_archives)
