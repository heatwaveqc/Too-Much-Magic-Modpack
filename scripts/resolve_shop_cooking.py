#!/usr/bin/env python3
"""Resolve two explicitly approved B11 mods without silently adding dependencies."""
from __future__ import annotations
import json
import re
import subprocess
import tomllib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MANIFEST = ROOT / "manifest/mods.json"
BASE = 138

def run(*args: str) -> str:
    p = subprocess.run(args, cwd=ROOT, text=True,
                       stdout=subprocess.PIPE, stderr=subprocess.STDOUT, timeout=300)
    if p.returncode:
        raise RuntimeError("Command failed: " + repr(args) + "\n" + p.stdout[-4000:])
    return p.stdout

def main() -> None:
    m = json.loads(MANIFEST.read_text(encoding="utf-8"))
    byid = {e["id"]: e for e in m["mods"]}
    expected = {"quest-shop", "cooking-for-blockheads"}
    active = {e["id"] for e in m["mods"] if e["enabled_in_pack"]}
    if len(active) != BASE or expected & active:
        raise ValueError(f"Wrong B11 baseline: {len(active)} active")
    if not byid["balm"]["enabled_in_pack"]:
        raise ValueError("Cooking for Blockheads requires pre-existing Balm")
    start = {p.name for p in (ROOT / "mods").glob("*.pw.toml")}
    print(run("packwiz", "--yes", "curseforge", "add",
              "--addon-id", "1338320", "--file-id", "9046797"), flush=True)
    print(run("packwiz", "--yes", "modrinth", "add",
              "--project-id", "vJnhuDde", "--version-id", "MQCIy6VF"), flush=True)
    current = {p.name for p in (ROOT / "mods").glob("*.pw.toml")}
    added = sorted(current - start)
    if len(added) != 2:
        raise ValueError(f"Expected exactly TWO approved mod pins, got {added}; no other mods allowed")
    found = {}
    for name in added:
        path = ROOT / "mods" / name
        obj = tomllib.loads(path.read_text(encoding="utf-8"))
        cf = obj.get("update", {}).get("curseforge", {})
        mr = obj.get("update", {}).get("modrinth", {})
        if cf.get("project-id") == 1338320 and cf.get("file-id") == 9046797:
            key = "quest-shop"
            if obj.get("filename") != "questshop-1.4.0.jar":
                raise ValueError("Wrong Quest Shop JAR: " + repr(obj.get("filename")))
        elif mr.get("mod-id") == "vJnhuDde" and mr.get("version") == "MQCIy6VF":
            key = "cooking-for-blockheads"
            if "1.21.1" not in obj.get("filename", "") or "21.1.24" not in obj["filename"]:
                raise ValueError("Wrong Cooking for Blockheads file: " + repr(obj.get("filename")))
        else:
            raise ValueError("Unapproved dependency or wrong file: " + str(path) + " " + repr(obj))
        if obj.get("side") != "both":
            raise ValueError(f"B11 mods must be both-side: {key}={obj.get('side')}")
        d = obj.get("download", {})
        if d.get("hash-format") not in {"sha1", "sha256", "sha512"} or not d.get("hash"):
            raise ValueError(f"Missing verified hash for {key}")
        found[key] = name.removesuffix(".pw.toml")
        byid[key]["enabled_in_pack"] = True
        byid[key]["packwiz_slug"] = found[key]
        byid[key]["dependencies_verified"] = False
        print("B11 PIN " + json.dumps({"id":key,"filename":obj["filename"],"file":name,"side":obj["side"]}), flush=True)
    if set(found) != expected:
        raise ValueError("Did not pin both requested mods: " + repr(found))
    next(b for b in m["batches"] if b["id"] == "B11")["status"] = "installed_server_boot_pending"
    MANIFEST.write_text(json.dumps(m, indent=2) + "\n", encoding="utf-8")
    pack = ROOT / "pack.toml"
    text = pack.read_text(encoding="utf-8")
    text = re.sub(r'(?m)^version = ".*"$',
                  'version = "0.8.4-questshop-cooking-currency-candidate"', text, count=1)
    pack.write_text(text, encoding="utf-8")
    print(run("packwiz", "refresh"), flush=True)
    print(run("python3", "scripts/validate.py"), flush=True)
    print(run("python3", "scripts/validate_quests.py"), flush=True)
    print("B11 RESOLVED " + json.dumps({"new":added,"total_pins":BASE+2}), flush=True)

if __name__ == "__main__":
    main()
