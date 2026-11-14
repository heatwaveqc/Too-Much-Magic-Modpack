#!/usr/bin/env python3
"""B4 transactional staging: pin -> hash-verified fresh server boot -> commit.

Each passing stage is preserved. Failed stages are rolled back while their
CI log and failure record remain, so other experimental mods can be tested.
"""
from __future__ import annotations
import datetime
import json
import os
from pathlib import Path
import re
import subprocess
import sys
import tempfile
from stage_b4 import ROOT, STAGES

BRANCH="build/b4-reactors-experimental"
ORDER=("reactor","astral","botania","draconic")
EVIDENCE=ROOT/"docs/B4-RESOLUTION.json"
REPORT=ROOT/"docs/B4-TESTING.md"
STARTER=ROOT/"scripts/smoke_server.sh"

def cmd(*args,env=None,timeout=720):
    result=subprocess.run(args,cwd=ROOT,env=env,stdout=subprocess.PIPE,
                          stderr=subprocess.STDOUT,text=True,timeout=timeout)
    return result.returncode,result.stdout

def errors(log):
    hit=[]
    pattern=re.compile(r'(?i)failure message|currently, .*not installed|failed to start|modloadingexception|missing (?:dependency|required)|caused by:|exception in thread|\berror\b.*(?:mod|registry|mixin)|^\s*\- mod ')
    for raw in log.splitlines():
        if pattern.search(raw):
            v=raw.strip()
            if len(v)>260:v=v[:260]
            if v not in hit:hit.append(v)
    return hit[-24:]

def write(results):
    data={
        "minecraft":"1.21.1","neoforge":"21.1.265","java":21,
        "baseline_branch":"build/b3-stable-magic-tectonic",
        "baseline_mods":64,
        "staged_targets":{stage:[{"manifest_id":m,"curseforge_project":pid,
                              "curseforge_file":fid,"expected_filename":filename,
                              "release_classification":cls}
                             for m,pid,fid,filename,category,cls in STAGES[stage]]
                          for stage in ORDER},
        "results":results,
        "not_tested":["graphical client", "shader/sky rendering", "real player world entry",
                      "Chaos Guardian / ProjectE gating", "gameplay feature balance"]
    }
    EVIDENCE.write_text(json.dumps(data,indent=2)+"\n",encoding="utf8")
    summary=[
      "# B4: BiggerReactors Rework + Astral Sorcery + Botania Revived + Draconic Evolution",
      "",
      "Minecraft 1.21.1 / NeoForge 21.1.265 / Java 21. Experiments are tested in order",
      "with a fresh disposable dedicated server for each stage. Every server JAR is",
      "downloaded and hash-verified from packwiz metadata.",
      "",
      "Pre-existing B3 stable baseline: 64 mods; 57 server JARs; full dedicated-server PASS.",
      "",
      "| Stage | Exact mod(s) | Result |",
      "|---|---|---|"
    ]
    for r in results:
        summary.append(f"| {r['stage']} | {', '.join(r['requested'])} | **{r['status']}** |")
    summary.extend(["","## Detailed results"])
    for r in results:
        summary.extend(["",f"### {r['stage']}: {r['status']}",
                        f"- CI run: {os.getenv('GITHUB_SERVER_URL','https://github.com')}/{os.getenv('GITHUB_REPOSITORY','heatwaveqc/Too-Much-Magic-Modpack')}/actions/runs/{os.getenv('GITHUB_RUN_ID','local')}",
                        f"- Packwiz added: {', '.join(r.get('resolved',[])) or 'none'}",
                        f"- Observed boot: {r.get('detail','no test')}"])
        for s in r.get("errors",[]):
            summary.append(f"- Log: `{s.replace(chr(96),chr(39))}`")
    summary.extend([
        "","## Constraints and untested items",
        "- **Reactor selection:** BiggerReactors Rework by vindocel, not Bigger Reactors Revived. Different registry namespace `biggerreactorsrework` and not backward-compatible with original worlds.",
        "- **Botania selection:** unofficial Botania Revived port, not original Vazkii Botania; no Sophisticated Storage/Backpacks/Core dependency allowed. Revived's optional Sophisticated integration is not proof those packages are installed.",
        "- **Astral Sorcery** is officially alpha-classified for NeoForge 1.21.1. ObserverLib is likewise alpha.",
        "- **Draconic Evolution** is beta-classified; Brandon's Core is stable.",
        "- **KubeJS progression restrictions remain unimplemented.** ProjectE and Project Expansion remain installed for testing, not production-ready survival.",
        "- **No graphical client or fresh-player-world test was performed.** Dedicated-server readiness proves only server boot.",
        "- Failed stages are rolled back from the working pack; their failed attempts remain in this report and CI artifacts.",
        ""
    ])
    REPORT.write_text("\n".join(summary),encoding="utf8")

def rollback():
    rc,out=cmd("git","restore","--","pack.toml","index.toml","manifest/mods.json","mods")
    if rc:
        raise RuntimeError("Could not restore baseline after failed stage: "+out[-1200:])
    rc,out=cmd("git","clean","-f","--","mods")
    if rc:
        raise RuntimeError("Could not clean untracked stage pins: "+out[-1200:])

def milestone(stage,success):
    args=["git","add","docs/B4-TESTING.md","docs/B4-RESOLUTION.json"]
    if success:
        args+=["pack.toml","index.toml","manifest/mods.json","mods"]
    rc,out=cmd(*args)
    if rc: raise RuntimeError("Failed staging "+out[-2000:])
    rc,st=cmd("git","diff","--cached","--quiet")
    if rc==0:return
    rc,out=cmd("git","commit","-m",f"test(b4): {stage} "+("server boot PASS" if success else "blocked, preserve diagnostic and rollback"))
    if rc:raise RuntimeError("Commit failure: "+out[-2000:])
    rc,out=cmd("git","push","origin","HEAD:"+BRANCH)
    if rc:raise RuntimeError("Push failure: "+out[-2000:])
    print("COMMITTED "+stage+" "+out[-500:],flush=True)

def main():
    rc,out=cmd("git","config","user.name","github-actions[bot]")
    if rc:raise RuntimeError(out)
    rc,out=cmd("git","config","user.email","41898282+github-actions[bot]@users.noreply.github.com")
    if rc:raise RuntimeError(out)
    results=[]
    for stage in ORDER:
        requested=[r[0] for r in STAGES[stage]]
        print(f"=== B4 BEGIN {stage} : {', '.join(requested)} ===",flush=True)
        stage_data={"stage":stage,"requested":requested,"status":"NOT RUN","resolved":[]}
        try:
            rc,output=cmd(sys.executable,"scripts/stage_b4.py",stage,timeout=280)
            print(output[-4800:],flush=True)
            if rc:
                raise RuntimeError("Metadata pin/validation failed: "+output[-1600:])
            stage_data["resolved"]=requested
            work_dir=Path(os.environ.get("RUNNER_TEMP", tempfile.gettempdir()))/("tmm-b4-"+stage)
            env=os.environ.copy()
            env.update({"WORK_DIR":str(work_dir),"SMOKE_WITH_MODS":"1",
                        "SMOKE_TIMEOUT_SECONDS":"330"})
            rc,output=cmd("bash",str(STARTER),env=env,timeout=700)
            print(output[-3500:],flush=True)
            server_log=work_dir/"server.log"
            observed=server_log.read_text(errors="replace") if server_log.exists() else output
            ok=(rc==0 and "PASS: dedicated server reached Done" in output and
                re.search(r'Done \(',observed) is not None)
            stage_data["status"]="PASS" if ok else "FAIL"
            stage_data["detail"]=("Clean server readiness and stop; JAR hashes verified" if ok else
                f"Server did not reach clean ready state; smoke exit code {rc}")
            stage_data["errors"]=errors(observed)
            if not ok:
                print("B4 STAGE FAILED "+stage+" => reverting added files, retaining logs",flush=True)
                rollback()
        except Exception as exc:
            stage_data["status"]="ERROR"
            stage_data["detail"]=str(exc)[-1600:]
            print("B4 STAGE ERROR "+stage+": "+repr(exc),flush=True)
            rollback()
        results.append(stage_data)
        write(results)
        milestone(stage,stage_data["status"]=="PASS")
    print("B4 FINAL "+json.dumps([{k:v for k,v in x.items() if k!="errors"} for x in results]),flush=True)
    # Fail CI if any requested stage failed, even when the last working pack boots.
    if any(x["status"]!="PASS" for x in results):
        sys.exit(1)

if __name__=="__main__":
    main()
