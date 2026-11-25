#!/usr/bin/env python3
"""Reconcile audit-only placement events against completed region census."""
import argparse
import json
from collections import defaultdict
from pathlib import Path

def tracked(rid):
    return rid!="astralsorcery:focal_point" and rid.split(":")[0] not in {"alexscaves","betterdungeons","bettermineshafts","pastel"}

def report(census, lines):
    if not census["complete"] or census["outer_full_chunks"]!=census["outer_expected_chunks"]:
        raise ValueError("incomplete region coverage")
    expected={(s["id"],*s["start_chunk"]) for s in census["starts"] if s["in_measured_area"] and tracked(s["id"])}
    totals=defaultdict(lambda:{"scopes":0,"accepted_writes":0,"unique_positions":0,"non_air_positions":0,
        "at_or_above_base_height":0,"max_rise":0,"non_air_columns":0,"cross_chunk_writes":0})
    seen=set()
    for line in lines:
        fields=line.rstrip("\n").split("\t")
        if len(fields)!=12: raise ValueError("invalid placement row")
        rid=fields[0]
        x,z,tx,tz,accepted,unique,nonair,above,rise,cols,cross=map(int,fields[1:])
        key=(rid,x,z)
        scope=(*key,tx,tz)
        if key not in expected or scope in seen:
            raise ValueError("unexpected or duplicate placement scope: "+str(scope))
        seen.add(scope)
        if not 0<=above<=nonair<=unique<=accepted or not 0<=cols<=min(nonair,256) or min(rise,cross)<0:
            raise ValueError("invalid placement counters")
        row=totals[key]
        row["scopes"]+=1
        for k,v in zip(("accepted_writes","unique_positions","non_air_positions","at_or_above_base_height","non_air_columns","cross_chunk_writes"),
                       (accepted,unique,nonair,above,cols,cross)):
            row[k]+=v
        row["max_rise"]=max(row["max_rise"],rise)
    missing=sorted(expected-set(totals))
    grouped=defaultdict(lambda:{"starts":0,"non_air_positions":0,"at_or_above_base_height":0,"max_rise":0,"cross_chunk_writes":0})
    rows=[]
    for key,row in sorted(totals.items()):
        rid,x,z=key
        rows.append(dict(row,id=rid,start_chunk=[x,z]))
        g=grouped[rid]
        g["starts"]+=1
        for k in ("non_air_positions","at_or_above_base_height","cross_chunk_writes"):
            g[k]+=row[k]
        g["max_rise"]=max(g["max_rise"],row["max_rise"])
    cross=sum(r["cross_chunk_writes"] for r in rows)
    return {"schema":1,"expected_starts":len(expected),"observed_starts":len(rows),"missing_starts":missing,
            "scopes":len(seen),"cross_chunk_writes":cross,"complete_start_reconciliation":not missing,
            "by_id":dict(grouped),"starts":rows,"limitations":[
            "Successful WorldGenRegion.setBlock calls inside StructureStart.placeInChunk only; direct writes and features outside this hook are not attributed.",
            "Last successful non-air write per position per start/target chunk, not final saved state; repeated writes deduplicated within scope.",
            "Positions written by different starts can overlap and are not globally deduplicated.",
            "Cross-chunk writes counted separately, omitted from unique-position counts.",
            "Base WORLD_SURFACE_WG generator height is a noise terrain estimate including water, not measured pre-structure terrain or visible silhouette.",
            "Intentional focal points and selected subsurface namespaces excluded from placement instrumentation; raw census preserved."]}

def main():
    p=argparse.ArgumentParser()
    p.add_argument("--census",required=True,type=Path)
    p.add_argument("--placements",required=True,type=Path)
    p.add_argument("--output",required=True,type=Path)
    a=p.parse_args()
    with a.placements.open() as f:
        result=report(json.loads(a.census.read_text()),f)
    with a.output.open("x") as f:json.dump(result,f,indent=2)
    print("PLACEMENT "+json.dumps({k:v for k,v in result.items() if k!="starts"}),flush=True)
    return int(not result["complete_start_reconciliation"])

if __name__=="__main__":raise SystemExit(main())
