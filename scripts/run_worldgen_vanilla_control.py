#!/usr/bin/env python3
"""One authorized vanilla-worldgen control using the proven pinned audit observer."""
import argparse
import hashlib
import json
import shutil
import sys
import urllib.error
from pathlib import Path

TOOLS = "990c97ce355aac191419354809ab013efddb10f0"

def occupancy(lines):
    occupied=set()
    for line in lines:
        row=line.rstrip("\n").split("\t")
        if len(row)!=12: raise ValueError("invalid placement row")
        x,z=int(row[3]),int(row[4])
        if int(row[8])>0: occupied.add((x,z))
    inner={(x,z) for x in range(-32,32) for z in range(-32,32)}
    n=len(inner & occupied)
    return {"measured_chunks":len(inner),"occupied":n,"empty":len(inner)-n,
            "empty_fraction":(len(inner)-n)/len(inner),
            "nearby":{str(d):sum(any((x-a)**2+(z-b)**2<=d*d for a,b in occupied)
                        for x,z in inner) for d in (4,8,12,16,24)},
            "limitations":["Same start-filtered observer and base-height proxy as TMM; not final saved blocks or line of sight.",
                           "Starts outside the inner region and cross-target writes are excluded."]}

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--tools",required=True,type=Path)
    p.add_argument("--work",required=True,type=Path)
    p.add_argument("--authorized",action="store_true")
    a=p.parse_args()
    if not a.authorized: p.error("explicit vanilla control execution approval required")
    tools=a.tools.resolve()
    sys.path.insert(0,str(tools/"scripts"))
    import run_worldgen_pilot as pilot
    if pilot.read_output(["git","rev-parse","HEAD"],tools)!=TOOLS:
        raise ValueError("incorrect observer source")
    work=a.work.resolve()
    work.mkdir(parents=True,exist_ok=False)
    server=work/"server"; server.mkdir()
    evidence=work/"evidence"; evidence.mkdir()
    mods=server/"mods"; mods.mkdir()
    pilot.progress("compile same audit-only observer")
    module=tools/"research/worldgen-audit-instrumentation"
    pilot.run(["gradle","--no-daemon","clean","build"],module)
    jar=module/"build/libs/tmm_worldgen_audit-0.1.0.jar"
    shutil.copy2(jar,mods/jar.name)
    pilot.progress("fetch exact pregenerator and Carbon audit dependencies only")
    pilot.download(pilot.PREGEN_URL,mods/pilot.PREGEN,pilot.PREGEN_SHA)
    try:
        pilot.download(pilot.CARBON_URL,mods/pilot.CARBON,pilot.CARBON_SHA)
    except urllib.error.HTTPError as e:
        if e.code not in (403,404): raise
        pilot.download(pilot.CARBON_URL.replace("edge.forgecdn.net","mediafilez.forgecdn.net"),mods/pilot.CARBON,pilot.CARBON_SHA)
    expected={jar.name,pilot.PREGEN,pilot.CARBON}
    if {f.name for f in mods.iterdir()}!=expected: raise ValueError("unexpected control mods")
    pilot.progress("install exact NeoForge server; no TMM or Tectonic")
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
    if {f.name for f in mods.iterdir()}!=expected: raise ValueError("control mod roster changed")
    (evidence/"provenance.json").write_text(json.dumps({
        "mode":"vanilla-worldgen-control","minecraft":"1.21.1","loader":pilot.LOADER,"tools":TOOLS,
        "seed":pilot.SEED,"bounds_chunks":pilot.INNER,"buffer_chunks":pilot.BUFFER,
        "generation_command":pilot.START,"installer_sha256":installer_sha,
        "mods_sha256":{f.name:hashlib.sha256(f.read_bytes()).hexdigest() for f in mods.iterdir()},
        "limitations":["NeoForge and three audit-only jars, not a literal unmodded server binary.",
                       "No TMM configs, datapacks, Tectonic, or gameplay mods copied."]},indent=2))
    # Same proven generation and reconciliation implementation. The TMM-only registry
    # command is harmlessly unavailable in this control; vanilla registry starts are read from NBT.
    result=pilot.generate(server,evidence,1800,True)
    if result: return result
    census=json.loads((evidence/"region-census.json").read_text())
    if any(not rid.startswith("minecraft:") for rid in census["counts_by_id"]):
        raise ValueError("non-vanilla structure ID in control")
    with (evidence/"placements.tsv").open() as f:
        summary=occupancy(f)
    (evidence/"occupancy.json").write_text(json.dumps(summary,indent=2))
    print("CONTROL_OCCUPANCY "+json.dumps(summary),flush=True)
    return 0

if __name__=="__main__":raise SystemExit(main())
