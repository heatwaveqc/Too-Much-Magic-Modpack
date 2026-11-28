#!/usr/bin/env python3
"""First authorized, isolated, original Infinity Cave control. No pack assembly or repair."""
import argparse
from collections import Counter, defaultdict
import gzip
import hashlib
import json
from pathlib import Path
import shutil
import sys
import tomllib
import urllib.error
import zipfile

TOOLS = "990c97ce355aac191419354809ab013efddb10f0"
FILE = "infinity_cave-0.7-NeoForge-1.21.1.jar"
URL = "https://cdn.modrinth.com/data/DEq97NAA/versions/6XJT5mi4/" + FILE
SHA512 = "e96cb77cf8fb3ff1fa6af7bd08a57e5cad017be3917b91b3ecec781d5886f7c137d84a3e4f3bb90d2d7500e9ff2d678663dea3060fa803e024a439a26a4125a3"
INNER = (-8, -8, 8, 8)
BUFFER = 4

def decode(palette, data, size, min_bits):
    if not palette:
        raise ValueError("empty palette")
    if len(palette) == 1:
        if data:
            raise ValueError("unexpected single-entry storage")
        return [palette[0]] * size
    bits = max(min_bits, (len(palette)-1).bit_length())
    per = 64 // bits
    if data is None or len(data) != (size+per-1)//per:
        raise ValueError("invalid non-spanning palette storage length")
    mask = (1 << bits)-1
    out = []
    for i in range(size):
        index = (data[i//per] >> ((i%per)*bits)) & mask
        if index >= len(palette):
            raise ValueError("invalid palette index")
        out.append(palette[index])
    return out

def category(name):
    if name in ("minecraft:air","minecraft:cave_air","minecraft:void_air"):
        return "air"
    if name in ("minecraft:lava","minecraft:water","minecraft:bedrock"):
        return name.split(":")[1]
    return "other"

def section_counts(section):
    states = section["block_states"]
    blocks = decode([x["Name"] for x in states["palette"]], states.get("data"),4096,4)
    biome = section["biomes"]
    biomes = decode(biome["palette"],biome.get("data"),64,1)
    y0=16*section["Y"]
    layers={y0+y:dict(Counter(category(n) for n in blocks[y*256:(y+1)*256])) for y in range(16)}
    return layers, Counter(biomes)

def inspect_jar(path):
    if hashlib.sha512(path.read_bytes()).hexdigest()!=SHA512:
        raise ValueError("original publisher SHA512 mismatch")
    with zipfile.ZipFile(path) as z:
        if z.testzip() is not None:
            raise ValueError("JAR CRC failed")
        meta=tomllib.loads(z.read("META-INF/neoforge.mods.toml").decode())
        mods=meta["mods"]
        deps=[d for d in meta["dependencies"]["infinity_cave"] if d["modId"]=="minecraft"]
        if len(mods)!=1 or mods[0]["modId"]!="infinity_cave" or mods[0]["version"]!="0.7" or len(deps)!=1 or deps[0]["versionRange"]!="[1.21,1.21.1)":
            raise ValueError("unexpected native identity/range")
        return {"mod":mods[0],"dependencies":meta["dependencies"],"sha512":SHA512}

def census_blocks(region, reader):
    # Reuse bounded Anvil reading and retain long arrays which the structure-only
    # parser intentionally discards. Do not modify frozen tooling on disk.
    base=reader.NBT
    class PaletteNBT(base):
        def value(self,t,depth=0):
            if t==12:
                n=self.integer()
                raw=self.read(n*8)
                return [int.from_bytes(raw[i:i+8],"big") for i in range(0,len(raw),8)]
            return super().value(t,depth)
    reader.NBT=PaletteNBT
    errors=[]; seen=set(); layers=defaultdict(Counter); biomes=defaultdict(Counter); sections=Counter()
    try:
        for path in sorted(region.glob("r.*.*.mca")):
            for x,z,chunk in reader.read_region(path,INNER,errors):
                if chunk.get("Status") not in ("minecraft:full","full"): continue
                if (x,z) in seen:
                    errors.append({"chunk":[x,z],"error":"duplicate full chunk"}); continue
                seen.add((x,z))
                for s in chunk.get("sections",[]):
                    sy=s.get("Y")
                    if not isinstance(sy,int) or not -8<=sy<=19 or "block_states" not in s or "biomes" not in s: continue
                    try:
                        values,bio=section_counts(s)
                        sections[sy]+=1
                        biomes[sy].update(bio)
                        for y,counts in values.items(): layers[y].update(counts)
                    except (KeyError,ValueError,TypeError) as e:
                        errors.append({"chunk":[x,z],"section":sy,"error":str(e)})
    finally:
        reader.NBT=base
    expected=(INNER[2]-INNER[0])*(INNER[3]-INNER[1])
    missing={str(y):expected-sections[y] for y in range(-8,20) if sections[y]!=expected}
    return {"full_chunks":len(seen),"expected_chunks":expected,"sections_by_y":dict(sections),
            "missing_or_duplicate_sections":missing,"blocks_by_y":dict(layers),"biome_quarts_by_section":dict(biomes),
            "errors":errors,"complete":len(seen)==expected and not missing and not errors,
            "limitations":["Other includes solid and non-fluid non-air blocks; not collision solidity.",
                           "Absent sections are unknown, not assumed air; counts are saved blocks, not predicted aquifer coverage."]}

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--tools",required=True,type=Path)
    p.add_argument("--work",required=True,type=Path)
    p.add_argument("--authorized",action="store_true")
    a=p.parse_args()
    if not a.authorized: p.error("explicit first-control authorization required")
    tools=a.tools.resolve();sys.path.insert(0,str(tools/"scripts"))
    import run_worldgen_pilot as pilot
    import audit_worldgen_regions as reader
    if pilot.read_output(["git","rev-parse","HEAD"],tools)!=TOOLS: raise ValueError("wrong reviewed tooling")
    work=a.work.resolve();work.mkdir(parents=True,exist_ok=False)
    evidence=work/"evidence";evidence.mkdir()
    server=work/"server";server.mkdir()
    mods=server/"mods";mods.mkdir()
    pilot.progress("download original Infinity Cave and audit dependencies only")
    pilot.download(URL,mods/FILE)
    jar=inspect_jar(mods/FILE)
    (evidence/"original-metadata.json").write_text(json.dumps(jar,indent=2))
    pilot.download(pilot.PREGEN_URL,mods/pilot.PREGEN,pilot.PREGEN_SHA)
    try:
        pilot.download(pilot.CARBON_URL,mods/pilot.CARBON,pilot.CARBON_SHA)
    except urllib.error.HTTPError as e:
        if e.code not in (403,404): raise
        pilot.download(pilot.CARBON_URL.replace("edge.forgecdn.net","mediafilez.forgecdn.net"),mods/pilot.CARBON,pilot.CARBON_SHA)
    if {f.name for f in mods.iterdir()}!={FILE,pilot.PREGEN,pilot.CARBON}: raise ValueError("unexpected roster")
    installer=server/"installer.jar"
    installer_sha=pilot.download(f"https://maven.neoforged.net/releases/net/neoforged/neoforge/{pilot.LOADER}/neoforge-{pilot.LOADER}-installer.jar",installer)
    pilot.run(["java","-jar","installer.jar","--installServer"],server,900)
    installer.unlink()
    (server/"eula.txt").write_text("eula=true\n")
    (server/"user_jvm_args.txt").write_text("-Xms2G\n-Xmx6G\n")
    (server/"server.properties").write_text(
        f"server-ip=127.0.0.1\nserver-port=25565\nlevel-name=world\nlevel-seed={pilot.SEED}\n"
        "level-type=minecraft:normal\nonline-mode=true\nwhite-list=true\nenforce-whitelist=true\n"
        "enable-rcon=false\nenable-query=false\nview-distance=6\nsimulation-distance=4\n"
        "max-tick-time=-1\nspawn-protection=0\ngenerate-structures=true\n")
    pilot.INNER=INNER;pilot.BUFFER=BUFFER;pilot.RADIUS=12
    pilot.START="pregen start gen radius tmm_audit SQUARE 0 0 12 minecraft:overworld NORMAL_GEN"
    (evidence/"provenance.json").write_text(json.dumps({"mode":"original-infinity-cave-control","minecraft":"1.21.1",
        "loader":pilot.LOADER,"tools":TOOLS,"seed":pilot.SEED,"bounds":INNER,"buffer":BUFFER,
        "generation_command":pilot.START,"installer_sha256":installer_sha,
        "mods_sha256":{f.name:hashlib.sha256(f.read_bytes()).hexdigest() for f in mods.iterdir()},
        "metadata_repaired":False},indent=2))
    # If the original artifact is rejected, generate raises with server.log and
    # execution.json preserved. Never silently repair, retry or advance the matrix.
    result=pilot.generate(server,evidence,900,False)
    summary=census_blocks(server/"world/region",reader)
    (evidence/"block-census.json").write_text(json.dumps(summary,indent=2,sort_keys=True))
    print("CAVE_CONTROL "+json.dumps({k:v for k,v in summary.items() if k not in ("blocks_by_y","biome_quarts_by_section","errors")}),flush=True)
    level=reader.NBT(gzip.decompress((server/"world/level.dat").read_bytes())).root()["Data"]
    gen=level.get("WorldGenSettings",{}).get("dimensions",{}).get("minecraft:overworld",{})
    (evidence/"effective-overworld.json").write_text(json.dumps({"generator":gen,"datapacks":level.get("DataPacks")},indent=2))
    return int(bool(result) or not summary["complete"])

if __name__=="__main__": raise SystemExit(main())
