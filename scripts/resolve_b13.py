#!/usr/bin/env python3
"""B13: resolve ONLY owner-approved beach/decoration/beverage mods and their three reviewed libs.

This script is intended for the b13 GitHub Actions runner, where packwiz has
Internet access. It deliberately stops on unexpected transitive dependencies
instead of silently admitting extra gameplay mods.
"""
from __future__ import annotations
import json
import re
import subprocess
import tomllib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MANIFEST = ROOT / "manifest/mods.json"
BASE_PINS = 147

# The exact file IDs are from official CurseForge and Modrinth 1.21.1
# *NeoForge* release pages, independently selected rather than "latest".
# (manifest id, hosting source, project id, version/file id, expected side).
PIN = [
    ("supermartijn642-core-lib", "modrinth", "rOUBggPv", "MQOzQxLt", "both"),
    ("supermartijn642-config-lib", "modrinth", "LN9BxssP", "qKL9jM75", "both"),
    ("fusion-connected-textures", "modrinth", "p19vrgc2", "AYDaLPK8", "client"),
    ("crystalix", "curseforge", "1187033", "9251153", "both"),
    ("lets-do-beachparty", "modrinth", "GyKzAh3l", "HN4rzKRl", "both"),
    ("rechiseled", "modrinth", "B0g2vT6l", "S5FnErRw", "both"),
    ("brewin-and-chewin", "modrinth", "hIu9KJTT", "MbcR48Ou", "both"),
    ("lets-do-furniture", "modrinth", "3JQDJrYW", "FxbGZtS6", "both"),
    ("cocktails-delight", "curseforge", "1198178", "9482953", "both"),
]

def run(*args: str) -> str:
    p = subprocess.run(args, cwd=ROOT, text=True, stdout=subprocess.PIPE,
                       stderr=subprocess.STDOUT, timeout=360)
    print("COMMAND", repr(args), "=>", p.returncode, flush=True)
    print(p.stdout[-2200:], flush=True)
    if p.returncode:
        raise RuntimeError("Command failed: " + repr(args) + "\n" + p.stdout[-5000:])
    return p.stdout

def find_pin(obj: dict) -> str | None:
    cf = obj.get("update", {}).get("curseforge", {})
    mr = obj.get("update", {}).get("modrinth", {})
    for key, platform, project, version, _side in PIN:
        if platform == "curseforge" and str(cf.get("project-id")) == project and str(cf.get("file-id")) == version:
            return key
        if platform == "modrinth" and mr.get("mod-id") == project and mr.get("version") == version:
            return key
    return None

def main() -> None:
    m = json.loads(MANIFEST.read_text(encoding="utf-8"))
    mods = {e["id"]: e for e in m["mods"]}
    assert sum(e["enabled_in_pack"] for e in m["mods"]) == BASE_PINS, "B13 must start on 147-pin base"
    assert all(k in mods and mods[k]["desired"] and not mods[k]["enabled_in_pack"]
               for k, *_ in PIN), "B13 owner-approved pending roster mismatch"
    assert all(mods[k]["enabled_in_pack"] for k in
               ("ars-nouveau", "astral-sorcery", "farmers-delight", "architectury-api", "curios-api")), "Missing prior core"
    assert "1.3.4" in next(p.read_text() for p in (ROOT / "mods").glob("farmers-delight.pw.toml")), (
        "Brewin' and Chewin' 4.5 requires Farmer's Delight 1.3.2+"
    )
    architectury_original = (ROOT / "mods" / "architectury-api.pw.toml").read_text(encoding="utf-8")
    original = {p.name for p in (ROOT / "mods").glob("*.pw.toml")}
    assert len(original) == BASE_PINS, f"Expected {BASE_PINS} original pins, saw {len(original)}"
    # Pin known libraries FIRST so dependency auto-resolution cannot choose
    # a different version or a Fabric artifact.
    for key, platform, project, version, side in PIN:
        if platform == "curseforge":
            run("packwiz", "--yes", "curseforge", "add", "--addon-id", project, "--file-id", version)
        else:
            run("packwiz", "--yes", "modrinth", "add", "--project-id", project, "--version-id", version)

    # Modrinth/CurseForge dependency resolvers may overwrite existing pins:
    # Cocktails Delight auto-converts the same Brewin' 4.5.0 jar to a
    # CurseForge metafile, while Beachparty upgrades Architectury silently.
    # Restore our exact chosen Brewin provider and the known-good pinned
    # Architectury 13.0.11 after every auto-resolution step.
    run("packwiz", "--yes", "modrinth", "add",
        "--project-id", "hIu9KJTT", "--version-id", "MbcR48Ou")
    run("packwiz", "--yes", "curseforge", "add",
        "--addon-id", "419699", "--file-id", "8492726")
    assert (ROOT / "mods" / "architectury-api.pw.toml").read_text(encoding="utf-8") == architectury_original, (
        "Unexpected Architectury baseline modification"
    )

    added = {p.name for p in (ROOT / "mods").glob("*.pw.toml")} - original
    assert len(added) == len(PIN), (
        f"Expected only {len(PIN)} selected pins, got {len(added)}: {sorted(added)}. "
        "Unexpected auto-dependency or duplicate install; resolve explicitly."
    )
    resolved: dict[str, str] = {}
    for pathstr in sorted(added):
        p = ROOT / "mods" / pathstr
        obj = tomllib.loads(p.read_text(encoding="utf-8"))
        key = find_pin(obj)
        assert key is not None and key not in resolved, f"Unknown or duplicate approved pin {pathstr}: {obj}"
        target = next(row for row in PIN if row[0] == key)
        assert obj.get("download", {}).get("hash") and obj.get("download", {}).get("hash-format") in (
            "sha1", "sha256", "sha512"), f"Missing hash: {pathstr}"
        assert "sources" not in obj.get("filename", "").lower(), f"Accidental sources jar {pathstr}"
        # Upstream explicitly labels Fusion client-side. Force truthful packwiz side metadata.
        if obj.get("side", "both") != target[4]:
            if key != "fusion-connected-textures":
                raise ValueError(f"Wrong side for {key}: {obj.get('side')}")
            t = p.read_text(encoding="utf-8")
            t, n = re.subn(r'(?m)^side = "(client|server|both)"$', 'side = "client"', t, count=1)
            assert n == 1, "Could not set client-only Fusion"
            p.write_text(t, encoding="utf-8")
        resolved[key] = pathstr.removesuffix(".pw.toml")
        mods[key]["enabled_in_pack"] = True
        mods[key]["packwiz_slug"] = resolved[key]
        mods[key]["dependencies_verified"] = False
        print("B13 PIN", json.dumps({"id": key, "filename": obj["filename"],
                                      "side": target[4], "file": pathstr}), flush=True)
    assert set(resolved) == {row[0] for row in PIN}, "Missing one or more owner-approved selections"

    next(b for b in m["batches"] if b["id"] == "B13")["status"] = "hash_pinned_server_smoke_pending"
    m["note"] += "\nB13 resolved nine exact pins (six gameplay + three libraries); server/client tests still pending."
    MANIFEST.write_text(json.dumps(m, indent=2) + "\n", encoding="utf-8")
    pack = ROOT / "pack.toml"
    t = pack.read_text(encoding="utf-8")
    t = re.sub(r'(?m)^version = ".*"$',
               'version = "0.9.0-b13-beach-decor-qa"', t, count=1)
    pack.write_text(t, encoding="utf-8")
    run("packwiz", "refresh")
    run("python3", "scripts/validate.py")
    print("B13 RESOLVED", json.dumps({"new": sorted(added), "total": BASE_PINS + len(added)}), flush=True)

if __name__ == "__main__":
    main()
