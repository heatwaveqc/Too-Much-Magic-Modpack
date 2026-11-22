#!/usr/bin/env python3
"""Isolated Packwiz-installer download acceptance, NOT a Minecraft/Prism build.

Exercise the SAME Packwiz bootstrap command as the Windows Prism QA prelaunch
hook, using a deliberately tiny one-metafile HTTP fixture. Publisher binary
bytes go only to ephemeral runner temp, never into the repository/artifacts.
"""
from __future__ import annotations
import argparse
import hashlib
import http.server
import json
import os
from pathlib import Path
import shutil
import subprocess
import tempfile
import threading
import tomllib
from urllib.request import urlopen

ROOT=Path(__file__).resolve().parents[1]
CASES={
    "cathobb": ("resourcepacks/cathobb-unified-64.pw.toml","resourcepacks"),
    "aquamirae-nouveau": ("mods/aquamirae-nouveau.pw.toml","mods"),
}
BOOTSTRAP="https://github.com/packwiz/packwiz-installer-bootstrap/releases/download/v0.0.3/packwiz-installer-bootstrap.jar"

def sha256(b:bytes)->str: return hashlib.sha256(b).hexdigest()

def run_case(which:str,side:str):
    rel,target_dir=CASES[which]
    source=ROOT/rel
    pin=tomllib.loads(source.read_text(encoding="utf-8"))
    with tempfile.TemporaryDirectory(prefix=f"tmm-isolated-{which}-") as base:
        path=Path(base)
        pack_root=path/"source"
        home=path/"installed"
        home.mkdir()
        target=(pack_root/rel)
        target.parent.mkdir(parents=True)
        shutil.copyfile(source,target)
        index=(f'hash-format = "sha256"\n\n[[files]]\nfile = "{rel}"\n'
               f'hash = "{sha256(target.read_bytes())}"\nmetafile = true\n')
        (pack_root/"index.toml").write_text(index,encoding="utf-8")
        pack=f'''name = "TMM approved publisher download TEST ONLY"
author = "TMM QA"
version = "metadata-probe"
pack-format = "packwiz:1.1.0"

[index]
file = "index.toml"
hash-format = "sha256"
hash = "{sha256(index.encode())}"

[versions]
minecraft = "1.21.1"
neoforge = "21.1.265"
'''
        (pack_root/"pack.toml").write_text(pack,encoding="utf-8")
        handler=lambda *a,**k:http.server.SimpleHTTPRequestHandler(*a,directory=str(pack_root),**k)
        server=http.server.ThreadingHTTPServer(("127.0.0.1",0),handler)
        thread=threading.Thread(target=server.serve_forever,daemon=True)
        thread.start()
        try:
            bootstrap=path/"packwiz-installer-bootstrap.jar"
            with urlopen(BOOTSTRAP,timeout=55) as remote,bootstrap.open("wb") as out:
                shutil.copyfileobj(remote,out)
            if bootstrap.stat().st_size!=98989:
                raise RuntimeError("Unexpected official Packwiz bootstrap binary size")
            url=f"http://127.0.0.1:{server.server_port}/pack.toml"
            cmd=["java","-jar",str(bootstrap),"-s",side,url]
            p=subprocess.run(cmd,cwd=home,stdin=subprocess.DEVNULL,
                             capture_output=True,text=True,timeout=660)
            print(json.dumps({"test_case":which,"side":side,"exit_code":p.returncode,
                 "stdout_tail":p.stdout[-3500:],"stderr_tail":p.stderr[-2000:]},indent=2))
            if p.returncode!=0:
                raise RuntimeError("Isolated Packwiz installer failed to fetch publisher file")
            downloaded=home/target_dir/pin["filename"]
            if side=="server" and pin["side"]=="client":
                if downloaded.exists():
                    raise RuntimeError("Client-only Cathobb resource pack incorrectly installed server-side")
                print("PASS: resource pack excluded from server-only install.")
                return
            if not downloaded.is_file():
                raise RuntimeError(f"Installer completed without target: {downloaded.name}")
            algo=pin["download"]["hash-format"]
            with downloaded.open("rb") as f:
                got=hashlib.file_digest(f,algo).hexdigest()
            if got!=pin["download"]["hash"]:
                raise RuntimeError("Downloaded publisher bytes differ from pinned exact digest")
            print(json.dumps({"status":"PASS","which":which,"side":side,
                "installed_filename":downloaded.name,
                "download_bytes":downloaded.stat().st_size,
                "sha_algorithm":algo,"sha":got,
                "binary_distributed":False},indent=2))
        finally:
            server.shutdown()
            server.server_close()
            thread.join(timeout=4)

if __name__=="__main__":
    p=argparse.ArgumentParser()
    p.add_argument("case",choices=sorted(CASES))
    p.add_argument("--side",choices=["client","server"],default="client")
    args=p.parse_args()
    run_case(args.case,args.side)
