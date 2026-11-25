#!/usr/bin/env python3
"""Reanalyze retained pilot geometry and exact-pin NBT templates without booting Minecraft."""
import argparse
import importlib.util
import json
import math
import re
import tomllib
import zipfile
from collections import Counter, defaultdict
from pathlib import Path
from statistics import mean, median

from audit_worldgen_regions import NBT, decompress, MAX_NBT
from audit_worldgen_resources import TEMPLATE, read_pins, digest, parse_resource

BASELINE = "721ea058294f838b9d7f145af427f5db228d248d"
AIR = {"minecraft:air", "minecraft:cave_air", "minecraft:void_air"}
NON_PLACING = AIR | {"minecraft:structure_void"}
PINS = ("botania-revived", "embers-re-ignited", "astral-sorcery", "primal-magick",
        "the-graveyard", "irons-spells-n-spellbooks", "evilcraft", "apotheosis",
        "dragon-survival", "mowzies-mobs", "goety", "hexerei")
POOL = re.compile(r"^data/([^/]+)/worldgen/template_pool/(.+)\.json$")
STRUCTURE = re.compile(r"^data/([^/]+)/worldgen/structure/(.+)\.json$")

def category(rid):
    if rid == "astralsorcery:focal_point":
        return "protected"
    if rid.split(":")[0] in {"alexscaves","betterdungeons","bettermineshafts","pastel"}:
        return "deprioritized_subsurface"
    if rid.startswith("botania:exploration_"):
        return "confirmed_surface"
    return "unclassified_review"

def box_metrics(bb):
    if not isinstance(bb,list) or len(bb)!=6 or not all(type(x) is int for x in bb):
        raise ValueError("missing/invalid bounding box")
    w,h,d=(bb[i+3]-bb[i]+1 for i in range(3))
    if min(w,h,d)<=0:
        raise ValueError("reversed bounding box")
    return {"width":w,"height":h,"depth":d,"footprint":w*d,"volume":w*h*d}

def summarize(rows):
    valid=[s for s in rows if s.get("geometry") is not None]
    return {"starts":len(rows),"complete_boxes":len(valid),
            "sum_footprint":sum(s["geometry"]["footprint"] for s in valid),
            "sum_volume":sum(s["geometry"]["volume"] for s in valid),
            "median_footprint":median(s["geometry"]["footprint"] for s in valid) if valid else None,
            "mean_height":mean(s["geometry"]["height"] for s in valid) if valid else None}

def geometry(census):
    if not census.get("complete") or census.get("full_chunks")!=4096 or census.get("outer_full_chunks")!=12544:
        raise ValueError("not complete fixed pilot census")
    if census.get("bounds_chunks")!=[-32,-32,32,32] or census.get("buffer_chunks")!=24:
        raise ValueError("unexpected pilot geometry")
    starts=[]
    for s in census["starts"]:
        row=dict(s, category=category(s["id"]), geometry=None)
        if s.get("pieces")==s.get("pieces_with_bbox") and s.get("pieces",0)>0:
            try:
                row["geometry"]=box_metrics(s["bbox"])
            except ValueError:
                pass
        starts.append(row)
    measured=[s for s in starts if s["in_measured_area"]]
    if Counter(s["id"] for s in measured)!=Counter(census["counts_by_id"]):
        raise ValueError("start counts inconsistent with census")
    def group(key):
        groups=defaultdict(list)
        for s in measured:
            groups[key(s)].append(s)
        return {k:summarize(v) for k,v in sorted(groups.items())}
    review=[s for s in measured if s["category"] in ("confirmed_surface","unclassified_review")]
    filtered=[s for s in starts if s["category"] in ("confirmed_surface","unclassified_review")]
    prox={}
    for radius in (8,12,16,24):
        vals=[sum((s["start_chunk"][0]-x)**2+(s["start_chunk"][1]-z)**2<=radius**2 for s in filtered)
              for z in range(-32,32,8) for x in range(-32,32,8)]
        prox[str(radius)]={"mean":mean(vals),"max":max(vals),"fraction_zero":vals.count(0)/len(vals)}
    return {"raw":summarize(measured),"review":summarize(review),"by_category":group(lambda s:s["category"]),
            "by_id":group(lambda s:s["id"]),
            "by_namespace_review":{k:summarize([s for s in review if s["id"].split(":")[0]==k])
                for k in sorted({s["id"].split(":")[0] for s in review})},
            "filtered_proximity":prox,"starts":starts,
            "limitations":["Bounding box envelopes, not occupied blocks; summed footprints overlap.",
                           "Remaining review starts include unclassified underground/aquatic structures.",
                           "Incomplete piece boxes excluded from geometry totals, not from start counts."]}

def template_metrics(raw):
    if len(raw)>MAX_NBT:
        raise ValueError("template exceeds size guard")
    data=NBT(decompress(raw,1) if raw.startswith(b"\x1f\x8b") else raw).root()
    size=data["size"]
    if len(size)!=3 or not all(type(x) is int and x>0 for x in size):
        raise ValueError("invalid template size")
    palettes=data.get("palettes") or [data["palette"]]
    blocks=data["blocks"]
    seen=set()
    counts=Counter()
    for b in blocks:
        pos=b["pos"]
        state=b["state"]
        if len(pos)!=3 or any(type(x) is not int or not 0<=x<size[i] for i,x in enumerate(pos)):
            raise ValueError("template position out of bounds")
        if tuple(pos) in seen or type(state) is not int or state<0:
            raise ValueError("duplicate position or invalid state")
        seen.add(tuple(pos))
        counts[state]+=1
    variants=[]
    for palette in palettes:
        if any(i>=len(palette) for i in counts):
            raise ValueError("palette index out of bounds")
        names=Counter()
        for i,n in counts.items():
            names[palette[i]["Name"]]+=n
        variants.append({"non_air_non_void":sum(v for k,v in names.items() if k not in NON_PLACING),
                         "jigsaw_or_structure_markers":sum(names[k] for k in ("minecraft:jigsaw","minecraft:structure_block")),
                         "explicit_air":sum(names[k] for k in AIR),"structure_void":names["minecraft:structure_void"]})
    return {"size":size,"volume":math.prod(size),"listed_positions":len(blocks),"variants":variants,
            "min_non_air":min(v["non_air_non_void"] for v in variants),
            "max_non_air":max(v["non_air_non_void"] for v in variants)}

def locations(e):
    if isinstance(e,dict):
        if isinstance(e.get("location"),str):
            yield e["location"] if ":" in e["location"] else "minecraft:"+e["location"]
        for child in e.get("elements",[]):
            yield from locations(child)

def scan(pack, work):
    paths=["mods/"+p+".pw.toml" for p in PINS]
    pins=read_pins(pack,"server",paths)
    if len(pins)!=len(paths):
        raise ValueError("incomplete pin selection")
    spec=importlib.util.spec_from_file_location("frozen_downloader",pack/"scripts/download_mods.py")
    downloader=importlib.util.module_from_spec(spec)
    spec.loader.exec_module(downloader)
    mods=work/"mods"; mods.mkdir()
    templates=[]; pools=defaultdict(list); structures=defaultdict(list); errors=[]; verified=[]
    for filename,pin in sorted(pins.items()):
        print("SIZE_FETCH "+filename,flush=True)
        try:
            path=mods/filename
            path.write_bytes(downloader.fetch_verified(tomllib.loads((pack/pin["pin"]).read_text())))
            d=pin["download"]
            if digest(path,d["hash-format"])!=d["hash"].lower():
                raise ValueError("digest mismatch")
            verified.append(filename)
            with zipfile.ZipFile(path) as z:
                seen=set()
                for item in z.infolist():
                    m=TEMPLATE.fullmatch(item.filename)
                    p=POOL.fullmatch(item.filename)
                    s=STRUCTURE.fullmatch(item.filename)
                    if not (m or p or s):
                        continue
                    try:
                        if item.filename in seen or item.file_size>MAX_NBT:
                            raise ValueError("duplicate resource or exceeds size guard")
                        seen.add(item.filename)
                        if m:
                            templates.append(dict(template_metrics(z.read(item)),id=m[1]+":"+m[2],archive=filename))
                        elif p:
                            pools[p[1]+":"+p[2]].append(parse_resource(z.read(item))[0])
                        elif s:
                            structures[s[1]+":"+s[2]].append(parse_resource(z.read(item))[0])
                    except Exception as e:
                        errors.append({"archive":filename,"path":item.filename,"error":str(e)})
        except Exception as e:
            errors.append({"archive":filename,"error":str(e)})
    indexed=defaultdict(list)
    for t in templates:
        indexed[t["id"]].append(t)
    roots={}
    for rid,defs in structures.items():
        if len(defs)!=1 or not isinstance(defs[0].get("start_pool"),str):
            continue
        root=defs[0]["start_pool"]
        if ":" not in root: root="minecraft:"+root
        if len(pools[root])!=1:
            continue
        ids=sorted({loc for e in pools[root][0].get("elements",[]) for loc in locations(e.get("element",{}))})
        matched=[indexed[i][0] for i in ids if len(indexed[i])==1]
        roots[rid]={"root_pool":root,"template_ids":ids,"matched":len(matched),
                    "min_root_piece_non_air":min((t["min_non_air"] for t in matched),default=None),
                    "max_root_piece_non_air":max((t["max_non_air"] for t in matched),default=None),
                    "unresolved":[i for i in ids if len(indexed[i])!=1]}
    return {"verified":verified,"templates":templates,"root_pool_candidates":roots,"errors":errors,
            "limitations":["Only selected exact top-level mod JARs; nested archives/datapack overrides not included.",
                           "Non-air non-void source template positions, including markers; not actual placed blocks.",
                           "Root pool ranges are individual candidate templates, not full assembled structures or weighted averages.",
                           "Processors, procedural pieces, palette selection, terrain adaptation and overlaps are not reconstructed."]}

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--census",type=Path,required=True)
    p.add_argument("--pack-root",type=Path,required=True)
    p.add_argument("--work",type=Path,required=True)
    a=p.parse_args()
    a.work.mkdir(parents=True,exist_ok=False)
    g=geometry(json.loads(a.census.read_text()))
    (a.work/"geometry.json").write_text(json.dumps(g,indent=2,sort_keys=True))
    print("SIZE_GEOMETRY "+json.dumps({k:v for k,v in g.items() if k not in ("starts","by_id")}),flush=True)
    for rid,row in g["by_id"].items():
        if category(rid) in ("confirmed_surface","unclassified_review"):
            print("SIZE_STRUCTURE "+json.dumps(dict(row,id=rid,category=category(rid))),flush=True)
    t=scan(a.pack_root,a.work)
    (a.work/"templates.json").write_text(json.dumps(t,indent=2,sort_keys=True))
    for rid,row in t["root_pool_candidates"].items():
        if rid in g["by_id"] and category(rid) in ("confirmed_surface","unclassified_review"):
            print("SIZE_ROOT "+json.dumps(dict(row,id=rid)),flush=True)
    for row in t["templates"]:
        if row["id"].startswith("botania:"):
            print("SIZE_TEMPLATE "+json.dumps(row),flush=True)
    print("SIZE_TOTAL "+json.dumps({"verified":len(t["verified"]),"templates":len(t["templates"]),"errors":t["errors"]}),flush=True)
    return int(bool(t["errors"]) or len(t["verified"])!=len(PINS))

if __name__=="__main__":
    raise SystemExit(main())
