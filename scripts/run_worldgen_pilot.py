#!/usr/bin/env python3
"""Explicitly authorized, single-seed, isolated audit-only server reconstruction."""
import argparse
import hashlib
import json
import os
import re
import shutil
import subprocess
import sys
import time
import urllib.request
from pathlib import Path

from audit_worldgen_regions import census
from audit_worldgen_resources import inventory

BASELINE = "721ea058294f838b9d7f145af427f5db228d248d"
LOADER = "21.1.265"
PREGEN = "Chunk-Pregenerator-Neoforge-1.21-4.5.4.jar"
PREGEN_URL = "https://mediafilez.forgecdn.net/files/8748/909/" + PREGEN
PREGEN_SHA = "2acd1f3b50472d49e7af69582ed718ac93272752e50cb636a97fcb04b498aa51"
CARBON = "CarbonConfig-Neoforge-1.21.1-2.0.3.jar"
CARBON_URL = "https://mediafilez.forgecdn.net/files/9531/034/" + CARBON
SEED = 424242
INNER = (-32,-32,32,32)
BUFFER = 24
RADIUS = 56
START = "pregen start gen radius tmm_audit SQUARE 0 0 56 minecraft:overworld NORMAL_GEN"
STOP = "pregen stop tmm_audit"

def run(cmd, cwd, timeout=1200):
    subprocess.run(cmd, cwd=cwd, check=True, timeout=timeout)

def read_output(cmd, cwd):
    return subprocess.check_output(cmd, cwd=cwd, text=True, timeout=120).strip()

def download(url, dest, sha=None):
    req=urllib.request.Request(url, headers={"User-Agent":"TMM-worldgen-audit/1"})
    with urllib.request.urlopen(req, timeout=120) as res, dest.open("xb") as out:
        shutil.copyfileobj(res,out)
    actual=hashlib.sha256(dest.read_bytes()).hexdigest()
    if sha and sha!=actual:
        raise ValueError("audit dependency digest mismatch: "+dest.name)
    return actual

def stage(pack, work, carbon_sha):
    if read_output(["git","rev-parse","HEAD"],pack)!=BASELINE:
        raise ValueError("not the frozen Thanksgiving snapshot")
    if read_output(["git","status","--porcelain","--untracked-files=no"],pack):
        raise ValueError("frozen checkout has tracked modifications")
    work.mkdir(parents=True,exist_ok=False)
    evidence=work/"evidence"
    evidence.mkdir()
    server=work/"server"
    server.mkdir()
    run([sys.executable,"scripts/first_party_runtime.py","build"],pack)
    paths=read_output([sys.executable,"scripts/first_party_runtime.py","paths"],pack)
    jars=[Path(p) if Path(p).is_absolute() else pack/p for p in paths.split(":") if p]
    if len(jars)!=4 or any(not p.is_file() for p in jars):
        raise ValueError("expected four exact first-party modules")
    run([sys.executable,str(pack/"scripts/download_mods.py"),str(server/"mods")],pack)
    for jar in jars:
        shutil.copy2(jar, server/"mods"/jar.name)
    checks=[sys.executable,"scripts/verify_qa_inventory.py","--strict-target",
            "--mods-dir",str(server/"mods"),"--side","server"]
    for jar in jars:
        checks+=["--extra",str(jar)]
    run(checks,pack)
    for dirname in ("config","defaultconfigs","kubejs"):
        if (pack/dirname).is_dir():
            shutil.copytree(pack/dirname,server/dirname)
    if (pack/"research/prefab-donors/stage_all_houses_qa.py").is_file():
        (server/".tmm-disposable-qa").write_text("DISPOSABLE QA ONLY\n")
        run([sys.executable,str(pack/"research/prefab-donors/stage_all_houses_qa.py"),
             "--stage-qa-game-dir",str(server),"--target","server"],pack)
        if len(list((server/"prefab-extension/server-cache").glob("*.nbt")))!=18:
            raise ValueError("missing approved prefab templates")
    report=inventory(pack,server/"mods",data_roots=[server/"kubejs"])
    report["snapshot_label"]=BASELINE
    (evidence/"raw-inventory.json").write_text(json.dumps(report,indent=2,sort_keys=True),encoding="utf-8")
    # Raw resource parsing issues do not falsify the runtime pin verification.
    # Retain them explicitly; runtime stage stops for any actual pin failure.
    if any(p["status"]!="verified" for p in report["pins"]):
        raise ValueError("raw scanner pin verification failed")
    dep=server/"mods"/PREGEN
    sha=download(PREGEN_URL,dep,PREGEN_SHA)
    carbon_actual=download(CARBON_URL,server/"mods"/CARBON,carbon_sha)
    installer=server/"installer.jar"
    installer_sha=download(f"https://maven.neoforged.net/releases/net/neoforged/neoforge/{LOADER}/neoforge-{LOADER}-installer.jar",installer)
    run(["java","-jar","installer.jar","--installServer"],server,900)
    (server/"eula.txt").write_text("eula=true\n")
    (server/"user_jvm_args.txt").write_text("-Xms2G\n-Xmx6G\n")
    (server/"server.properties").write_text(
        f"server-ip=127.0.0.1\nserver-port=25565\nlevel-name=world\nlevel-seed={SEED}\n"
        "level-type=minecraft:normal\nonline-mode=true\nwhite-list=true\nenforce-whitelist=true\n"
        "enable-rcon=false\nenable-query=false\nview-distance=6\nsimulation-distance=4\n"
        "max-tick-time=-1\nspawn-protection=0\ngenerate-structures=true\n")
    provenance={"baseline":BASELINE,"loader":LOADER,"seed":SEED,"bounds_chunks":INNER,
                "buffer_chunks":BUFFER,"radius_chunks":RADIUS,"pregen_file":PREGEN,
                "pregen_sha256":sha,"carbon_file":CARBON,"carbon_sha256":carbon_actual,"installer_sha256":installer_sha,
                "generation_command":START,"first_party_sha256":{
                    p.name:hashlib.sha256(p.read_bytes()).hexdigest() for p in jars},
                "limitations":["Audit-only reconstructed snapshot, not release or installed-world acceptance.",
                               "World preset/actual datapacks and completion must be verified from runtime evidence."]}
    (evidence/"provenance.json").write_text(json.dumps(provenance,indent=2))
    return server,evidence

def header_coverage(region, bounds):
    x0,z0,x1,z1=bounds
    n=0
    for rz in range(z0//32,(z1-1)//32+1):
        for rx in range(x0//32,(x1-1)//32+1):
            p=region/f"r.{rx}.{rz}.mca"
            if not p.exists():
                continue
            with p.open("rb") as f:
                h=f.read(4096)
            if len(h)!=4096:
                continue
            for i in range(1024):
                x,z=rx*32+i%32,rz*32+i//32
                if x0<=x<x1 and z0<=z<z1 and h[4*i:4*i+4]!=b"\0"*4:
                    n+=1
    return n

def generate(server,evidence,seconds):
    log=evidence/"server.log"
    outer=tuple(v+delta for v,delta in zip(INNER,(-BUFFER,-BUFFER,BUFFER,BUFFER)))
    expected=(outer[2]-outer[0])*(outer[3]-outer[1])
    stage_state={"ready":False,"command_sent":False,"header_coverage":0,"reason":"startup","clean_exit":False}
    with log.open("wb") as output:
        proc=subprocess.Popen(["bash","./run.sh","nogui"],cwd=server,stdin=subprocess.PIPE,stdout=output,stderr=subprocess.STDOUT)
        def send(command):
            if proc.poll() is not None:
                raise RuntimeError("server exited")
            proc.stdin.write((command+"\n").encode())
            proc.stdin.flush()
        def tail():
            with log.open("rb") as f:
                f.seek(max(0,log.stat().st_size-256*1024))
                return f.read().decode(errors="replace")
        try:
            deadline=time.monotonic()+900
            while time.monotonic()<deadline:
                if proc.poll() is not None:
                    raise RuntimeError("server exited before ready")
                if re.search(r"Done \([0-9.,]+s\)!",tail()):
                    stage_state["ready"]=True
                    break
                time.sleep(2)
            if not stage_state["ready"]:
                raise TimeoutError("server startup exceeded 900 seconds")
            send("help pregen")
            send("datapack list enabled")
            send("seed")
            send("tmm_structure_registry")
            send(START)
            stage_state["command_sent"]=True
            deadline=time.monotonic()+seconds
            next_save=time.monotonic()+60
            while time.monotonic()<deadline:
                if proc.poll() is not None:
                    raise RuntimeError("server exited during generation")
                if shutil.disk_usage(server).free<3*1024**3:
                    stage_state["reason"]="disk guard"
                    break
                if time.monotonic()>=next_save:
                    send("save-all flush")
                    next_save=time.monotonic()+60
                stage_state["header_coverage"]=header_coverage(server/"world/region",outer)
                if stage_state["header_coverage"]==expected:
                    stage_state["reason"]="all expected headers present (full status unverified)"
                    break
                time.sleep(10)
            else:
                stage_state["reason"]="bounded generation time elapsed"
        finally:
            try:
                if proc.poll() is None:
                    if stage_state["command_sent"]:
                        send(STOP)
                    send("save-all flush")
                    send("stop")
                    proc.wait(timeout=180)
            except (OSError,subprocess.TimeoutExpired,RuntimeError):
                proc.kill()
                proc.wait(timeout=30)
            stage_state["clean_exit"]=proc.returncode==0
            (evidence/"execution.json").write_text(json.dumps(stage_state,indent=2))
    report=census(server/"world/region",INNER,BUFFER)
    (evidence/"region-census.json").write_text(json.dumps(report,indent=2,sort_keys=True))
    print("PILOT "+json.dumps({k:v for k,v in report.items() if k not in ("starts","errors")}),flush=True)
    return int(not stage_state["clean_exit"] or not report["complete"] or report["outer_full_chunks"]!=report["outer_expected_chunks"])

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument("--pack-root",type=Path,required=True)
    p.add_argument("--work",type=Path,required=True)
    p.add_argument("--carbon-sha256",required=True)
    p.add_argument("--authorized",action="store_true")
    p.add_argument("--generation-seconds",type=int,default=1500)
    a=p.parse_args()
    if not a.authorized:
        p.error("explicit audit execution approval required")
    if not re.fullmatch("[0-9a-f]{64}",a.carbon_sha256):
        p.error("reviewed Carbon Config SHA-256 required")
    if not 60<=a.generation_seconds<=1800:
        p.error("generation must be bounded to 60–1800 seconds")
    server,evidence=stage(a.pack_root.resolve(),a.work.resolve(),a.pregen_sha256)
    return generate(server,evidence,a.generation_seconds)

if __name__=="__main__":
    raise SystemExit(main())
