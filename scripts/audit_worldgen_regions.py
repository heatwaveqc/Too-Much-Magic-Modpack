#!/usr/bin/env python3
"""Read-only Anvil structure start census for a stopped Minecraft 1.21.1 world."""
import argparse
import gzip
import io
import json
import math
import re
import struct
import zlib
from collections import Counter
from pathlib import Path

MAX_NBT = 64 * 1024 * 1024
REGION = re.compile(r"r\.(-?\d+)\.(-?\d+)\.mca$")
SIZES = {1: 1, 2: 2, 3: 4, 4: 8, 5: 4, 6: 8}

class NBT:
    def __init__(self, data):
        self.f = io.BytesIO(data)
    def read(self, n):
        if not 0 <= n <= MAX_NBT:
            raise ValueError("invalid NBT length")
        out = self.f.read(n)
        if len(out) != n:
            raise ValueError("truncated NBT")
        return out
    def integer(self, n=4):
        return int.from_bytes(self.read(n), "big", signed=True)
    def string(self):
        return self.read(int.from_bytes(self.read(2), "big")).decode("utf-8", errors="replace")
    def value(self, t, depth=0):
        if depth > 80:
            raise ValueError("NBT recursion limit")
        if t in (1, 2, 3, 4):
            return self.integer(SIZES[t])
        if t in (5, 6):
            return struct.unpack(">f" if t == 5 else ">d", self.read(SIZES[t]))[0]
        if t == 8:
            return self.string()
        if t in (7, 11, 12):
            n = self.integer()
            raw = self.read(n * {7: 1, 11: 4, 12: 8}[t])
            if t == 11 and n <= 64:
                return list(struct.unpack(">" + str(n) + "i", raw))
            return None
        if t == 9:
            child = self.integer(1)
            n = self.integer()
            if not 0 <= n <= 2_000_000 or (child == 0 and n):
                raise ValueError("invalid NBT list")
            return [self.value(child, depth + 1) for _ in range(n)]
        if t == 10:
            obj = {}
            while (child := self.integer(1)) != 0:
                key = self.string()
                obj[key] = self.value(child, depth + 1)
            return obj
        raise ValueError(f"unsupported NBT type {t}")
    def root(self):
        if self.integer(1) != 10:
            raise ValueError("root is not compound")
        self.string()
        return self.value(10)

def decompress(payload, mode):
    if mode in (1, 2):
        dec = zlib.decompressobj(31 if mode == 1 else 15)
        out = dec.decompress(payload, MAX_NBT + 1)
        if len(out) > MAX_NBT or not dec.eof:
            raise ValueError("oversized or incomplete compressed NBT")
        return out
    if mode == 3 and len(payload) <= MAX_NBT:
        return payload
    raise ValueError(f"unsupported compression {mode}")

def read_region(path, bounds, errors):
    match = REGION.fullmatch(path.name)
    if not match:
        return
    rx, rz = map(int, match.groups())
    xmin, zmin, xmax, zmax = bounds
    with path.open("rb") as f:
        header = f.read(8192)
        if len(header) != 8192:
            errors.append({"file": path.name, "error": "truncated header"})
            return
        for i in range(1024):
            x, z = rx * 32 + i % 32, rz * 32 + i // 32
            if not (xmin <= x < xmax and zmin <= z < zmax):
                continue
            entry = header[4*i:4*i+4]
            off, sectors = int.from_bytes(entry[:3], "big"), entry[3]
            if off == 0 and sectors == 0:
                continue
            try:
                if off < 2 or sectors == 0:
                    raise ValueError("invalid sector location")
                f.seek(off * 4096)
                length = int.from_bytes(f.read(4), "big")
                if not 1 <= length <= sectors * 4096 - 4:
                    raise ValueError("invalid chunk record length")
                mode = f.read(1)[0]
                if mode & 128:
                    external = path.parent / f"c.{x}.{z}.mcc"
                    if external.stat().st_size > MAX_NBT:
                        raise ValueError("oversized external chunk")
                    payload = external.read_bytes()
                else:
                    payload = f.read(length - 1)
                    if len(payload) != length - 1:
                        raise ValueError("truncated chunk record")
                chunk = NBT(decompress(payload, mode & 127)).root()
                if chunk.get("xPos") != x or chunk.get("zPos") != z:
                    raise ValueError("chunk coordinate mismatch")
                yield x, z, chunk
            except (ValueError, OSError, IndexError, struct.error, zlib.error) as e:
                errors.append({"file": path.name, "chunk": [x,z], "error": str(e)})

def get_starts(chunk, x, z, errors):
    # Modern chunk format: starts only, never References.
    structures = chunk.get("structures", {})
    if not isinstance(structures, dict):
        raise ValueError("invalid structures compound")
    for key, start in structures.get("starts", {}).items():
        if not isinstance(start, dict):
            errors.append({"chunk":[x,z], "id":key, "error":"invalid start record"})
            continue
        rid = start.get("id")
        if rid == "INVALID" or rid == "minecraft:invalid":
            continue
        if not isinstance(rid, str) or ":" not in rid or not start.get("Children"):
            errors.append({"chunk":[x,z], "id":key, "error":"unrecognized/empty start"})
            continue
        if (start.get("ChunkX"), start.get("ChunkZ")) != (x, z):
            errors.append({"chunk":[x,z], "id":rid, "error":"start coordinate mismatch"})
            continue
        pieces = []
        for piece in start["Children"]:
            bb = piece.get("BB") if isinstance(piece, dict) else None
            if isinstance(bb, list) and len(bb) == 6 and all(isinstance(n,int) for n in bb) and all(bb[i] <= bb[i+3] for i in range(3)):
                pieces.append(bb)
        box = ([min(b[i] for b in pieces) for i in range(3)] +
               [max(b[i] for b in pieces) for i in range(3,6)]) if pieces else None
        yield {"id":rid, "registry_key":key, "start_chunk":[x,z], "bbox":box,
               "pieces":len(start["Children"]), "pieces_with_bbox":len(pieces)}

def census(region_dir, inner, buffer=0):
    x0,z0,x1,z1 = inner
    if x1 <= x0 or z1 <= z0 or buffer < 0:
        raise ValueError("invalid bounds")
    if not region_dir.is_dir():
        raise ValueError("region directory missing")
    outer = (x0-buffer,z0-buffer,x1+buffer,z1+buffer)
    full, inner_full, seen, errors, starts = set(), set(), set(), [], []
    statuses = Counter()
    def inside(x,z,b):
        return b[0] <= x < b[2] and b[1] <= z < b[3]
    for path in sorted(region_dir.glob("r.*.*.mca")):
        for x,z,chunk in read_region(path,outer,errors):
            if (x,z) in seen:
                errors.append({"chunk":[x,z],"error":"duplicate chunk"})
                continue
            seen.add((x,z))
            status = chunk.get("Status")
            if inside(x,z,inner):
                statuses[str(status)] += 1
            if status not in ("minecraft:full","full"):
                continue
            full.add((x,z))
            if inside(x,z,inner):
                inner_full.add((x,z))
            try:
                for start in get_starts(chunk,x,z,errors):
                    start["in_measured_area"] = inside(x,z,inner)
                    starts.append(start)
            except (ValueError, AttributeError, TypeError) as e:
                errors.append({"chunk":[x,z],"error":str(e)})
    expected=(x1-x0)*(z1-z0)
    outer_expected=(outer[2]-outer[0])*(outer[3]-outer[1])
    counts=Counter(s["id"] for s in starts if s["in_measured_area"])
    namespaces=Counter()
    for rid,n in counts.items():
        namespaces[rid.split(":",1)[0]] += n
    complete = len(inner_full)==expected and not errors
    area=len(inner_full)*256
    # Proximity is a horizontal start-chunk distance, not surface visibility.
    nearby={}
    if buffer >= 24 and len(full)==outer_expected and not errors:
        coords=[(s["start_chunk"][0],s["start_chunk"][1]) for s in starts]
        sample=[(x,z) for z in range(z0,z1,8) for x in range(x0,x1,8)]
        for radius in (8,12,16,24):
            values=[sum((a-x)**2+(b-z)**2 <= radius**2 for a,b in coords) for x,z in sample]
            nearby[str(radius)]={"samples":len(values),"mean":sum(values)/len(values),
                                 "max":max(values),"fraction_zero":values.count(0)/len(values)}
    return {"schema":1,"bounds_chunks":list(inner),"buffer_chunks":buffer,
            "full_chunks":len(inner_full),"expected_chunks":expected,
            "outer_full_chunks":len(full),"outer_expected_chunks":outer_expected,
            "coverage":len(inner_full)/expected,"complete":complete,
            "area_full_chunks_blocks2":area,"chunk_status_counts":dict(statuses),
            "starts":starts,"counts_by_id":dict(counts),"counts_by_namespace":dict(namespaces),
            "starts_per_million_blocks2":{k:v*1_000_000/area for k,v in namespaces.items()} if complete else None,
            "nearby_start_proxy":nearby,"errors":errors,
            "limitations":["Start chunk counts, not total physical footprints or visible structures.",
                           "Namespaces are not necessarily mod ownership.",
                           "Features and custom placements absent from structure starts are not counted.",
                           "No biome or surface visibility classification yet; bounding boxes may be incomplete.",
                           "Incomplete/corrupt coverage suppresses density estimates."]}

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--region-dir",required=True,type=Path)
    p.add_argument("--bounds",required=True,nargs=4,type=int,metavar=("MIN_X","MIN_Z","MAX_X","MAX_Z"))
    p.add_argument("--buffer",type=int,default=0)
    p.add_argument("--output",required=True,type=Path)
    a=p.parse_args()
    report=census(a.region_dir,a.bounds,a.buffer)
    a.output.parent.mkdir(parents=True,exist_ok=True)
    with a.output.open("x",encoding="utf-8") as f:
        json.dump(report,f,indent=2,sort_keys=True)
        f.write("\n")
    print("REGION_CENSUS "+json.dumps({k:v for k,v in report.items() if k not in ("starts","errors")}))
    return int(not report["complete"] or report["outer_full_chunks"] != report["outer_expected_chunks"])

if __name__=="__main__":
    raise SystemExit(main())
