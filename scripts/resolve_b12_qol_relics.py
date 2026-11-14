#!/usr/bin/env python3
"""B12: resolve EXACT reviewed NeoForge releases, required libs, and packwiz hashes.

Approved NEW mods (seven): TrashSlot, TreeChop, Leaves Be Gone, Relics,
Reliquified Artifacts, ShatterLib/OctoLib and Puzzles Lib. Existing baseline
has 140 active pins. Relics already exists as desired-but-disabled.
No auto-added dependency, loader substitution, or third-party jars committed.
"""
from __future__ import annotations

import hashlib
import json
import pathlib
import re
import tomllib
import urllib.parse
import urllib.request

ROOT = pathlib.Path(__file__).resolve().parents[1]
MANIFEST = ROOT / "manifest/mods.json"
INDEX = ROOT / "index.toml"
PACK = ROOT / "pack.toml"

# Explicit official Modrinth projects and fixed release targets.
# Use a version number only where exact public Version ID was not discoverable;
# still require unique NeoForge/1.21.1 release and expected JAR filename.
PIN = {
    "trashslot": dict(project="vRYk0bv7", version_id="HY8Ybozd", version="21.1.11+neoforge-1.21.1", name="TrashSlot", side="both", tokens=["trash", "1.21.1"]),
    "treechop": dict(project="gHoB7SHO", version_id="iLXWmfax", version="0.19.3", name="HT's TreeChop", side="both", tokens=["treechop", "1.21.1", "neoforge", "0.19.3"]),
    "leaves-be-gone": dict(project="AVq17PqV", version_id="kAbmpvF3", version="v21.1.1-1.21.1-NeoForge", name="Leaves Be Gone", side="both", tokens=["leavesbegone", "21.1.1"]),
    "relics": dict(project="OCJRPujW", version_id="GIUnjk0v", version="0.12.5", name="Relics", side="both", tokens=["relics", "1.21.1", "0.12.5"]),
    "reliquified-artifacts": dict(project="GnU07giL", version="1.0.9", name="Reliquified Artifacts", side="both", tokens=["reliquified_artifacts", "1.21.1", "1.0.9"]),
    "octolib": dict(project="RH2KUdKJ", version="0.6.2", name="ShatterLib | OctoLib", side="both", tokens=["octolib", "neoforge", "0.6.2"]),
    "puzzles-lib": dict(project="QAGBst4M", version_id="lh44g7RC", version="21.1.60", name="Puzzles Lib", side="both", tokens=["puzzleslib", "21.1.60"]),
}
ALLOWED_PROJECTS = {
    # Already pinned base: Artifacts, Curios, Architectury (CurseForge
    # project rather than Modrinth), Balm, plus our new dependency set.
    "P0Mu4wcQ", "vvuO3ImH", "MBAkmtvl", "lhGA9TYQ",
    *[x["project"] for x in PIN.values()],
}


def ensure(ok: bool, message: str) -> None:
    if not ok:
        raise ValueError(message)


def fetch(path: str):
    req = urllib.request.Request(
        "https://api.modrinth.com/v2/" + path,
        headers={"User-Agent": "TooMuchMagic/1.21.1-B12 (modpack research)",
                 "Accept": "application/json"},
    )
    with urllib.request.urlopen(req, timeout=40) as resp:
        return json.load(resp)


def version_for(id: str, spec: dict) -> dict:
    if "version_id" in spec:
        result = fetch("version/" + spec["version_id"])
        ensure(result["version_number"] == spec["version"],
               f"Unreviewed version change for {id}: {result['version_number']}")
    else:
        params = urllib.parse.urlencode({
            "loaders": json.dumps(["neoforge"]),
            "game_versions": json.dumps(["1.21.1"]),
        })
        options = fetch(f"project/{spec['project']}/version?{params}")
        possibles = [
            x for x in options
            if x.get("version_number") == spec["version"]
            or x.get("version_number", "").startswith(spec["version"] + "+")
        ]
        ensure(len(possibles) == 1,
               f"Ambiguous {id} {spec['version']} NeoForge release: "
               f"{[(x['id'], x['version_number']) for x in possibles]}")
        result = possibles[0]
    ensure(result["project_id"] == spec["project"],
           f"{id} returned wrong Modrinth project")
    ensure("neoforge" in result["loaders"] and "1.21.1" in result["game_versions"],
           f"{id} release isn't specifically NeoForge 1.21.1")
    return result


def exact_file(id: str, spec: dict, version: dict) -> dict:
    matches = []
    for f in version.get("files", []):
        name = f["filename"].lower().replace("-", "").replace("_", "")
        if f["filename"].endswith(".jar") and all(
            t.lower().replace("-", "").replace("_", "") in name
            for t in spec["tokens"]
        ):
            matches.append(f)
    ensure(len(matches) == 1,
           f"{id} exact NeoForge .jar ambiguous: "
           f"{[x.get('filename') for x in version.get('files', [])]}")
    file = matches[0]
    ensure(re.fullmatch(r"[0-9a-f]{128}", file["hashes"].get("sha512", "")),
           f"Missing signed project SHA512 for {id}")
    host = urllib.parse.urlparse(file["url"])
    ensure(host.scheme == "https" and host.hostname == "cdn.modrinth.com",
           f"Untrusted distribution host for {id}")
    return file


def check_dependencies(version: dict, installed_ids: set[str], id: str) -> None:
    for dep in version.get("dependencies", []):
        if dep.get("dependency_type") != "required":
            continue
        proj = dep.get("project_id")
        # Dependencies on a specific known version are resolved by its
        # associated project. Fetch if only version_id provided.
        if proj is None and dep.get("version_id"):
            proj = fetch("version/" + dep["version_id"])["project_id"]
        ensure(proj in installed_ids,
               f"{id} needs unknown mandatory project {proj}, version={dep.get('version_id')}")
    print(f"PASS required published dependency references: {id}")


def write_pin(id: str, spec: dict, v: dict, f: dict) -> str:
    filename = f["filename"]
    ensure("/" not in filename and "\\" not in filename, "Unsafe filename")
    body = (
        f'name = "{spec["name"]}"\n'
        f'filename = "{filename}"\n'
        f'side = "{spec["side"]}"\n\n'
        f'[download]\nurl = "{f["url"]}"\n'
        'hash-format = "sha512"\n'
        f'hash = "{f["hashes"]["sha512"]}"\n\n'
        '[update]\n[update.modrinth]\n'
        f'mod-id = "{spec["project"]}"\n'
        f'version = "{v["id"]}"\n'
    )
    name = f"mods/{id}.pw.toml"
    path = ROOT / name
    ensure(not path.exists(), f"Refusing to overwrite existing pinned {name}")
    path.write_text(body, encoding="utf-8")
    print(f"B12 PIN {id}: {filename} {v['id']} sha512={f['hashes']['sha512']}")
    return name


def main() -> None:
    m = json.loads(MANIFEST.read_text(encoding="utf-8"))
    by_id = {x["id"]: x for x in m["mods"]}
    ensure(set(PIN) == set(m["batches"][-1]["members"]), "B12 manifest membership mismatch")
    ensure(sum(x["enabled_in_pack"] for x in m["mods"]) == 140,
           "Wrong 140-mod tested baseline")
    ensure(all(not by_id[x]["enabled_in_pack"] for x in PIN),
           "Some B12 mods already active; prevent partial re-resolution")
    ensure(all(by_id[x]["enabled_in_pack"] for x in ("artifacts", "curios-api", "architectury-api", "balm")),
           "B12 baseline missing core dependencies")
    index = INDEX.read_text(encoding="utf-8")
    active_mods = [x for x in m["mods"] if x["enabled_in_pack"]]
    approved_projects = set(ALLOWED_PROJECTS)
    for entry in active_mods:
        meta = ROOT / "mods" / (entry.get("packwiz_slug", entry["id"]) + ".pw.toml")
        if not meta.is_file():
            continue
        approved = tomllib.loads(meta.read_text(encoding="utf-8"))
        project = approved.get("update", {}).get("modrinth", {}).get("mod-id")
        if project:
            approved_projects.add(project)
    prepared = {}
    for id, spec in PIN.items():
        v = version_for(id, spec)
        f = exact_file(id, spec, v)
        check_dependencies(v, approved_projects, id)
        prepared[id] = (v, f)
    # No mutation until every official provider file and dependency passes.
    entries = []
    for id, spec in PIN.items():
        v, f = prepared[id]
        key = write_pin(id, spec, v, f)
        digest = hashlib.sha256((ROOT / key).read_bytes()).hexdigest()
        entries.append(f'[[files]]\nfile = "{key}"\nhash = "{digest}"\nmetafile = true\n')
        by_id[id]["enabled_in_pack"] = True
        by_id[id]["packwiz_slug"] = id
        by_id[id]["dependencies_verified"] = True
        by_id[id]["release_status"] = "experimental_release" if id in ("relics", "reliquified-artifacts") else "verified_release"
        by_id[id]["version"] = v["version_number"]
    index = index.rstrip() + "\n\n" + "\n".join(entries)
    INDEX.write_text(index, encoding="utf-8")
    pack = PACK.read_text(encoding="utf-8")
    pack = re.sub(r'(?m)^version = ".*"$',
                  'version = "0.8.7-b12-qol-tree-reliquified-candidate"',
                  pack, count=1)
    pattern = re.compile(r'(\[index\]\nfile = "index.toml"\nhash-format = "sha256"\nhash = ")[0-9a-f]+(")')
    ensure(bool(pattern.search(pack)), "Invalid pack.toml index pointer")
    new_digest = hashlib.sha256(index.encode("utf-8")).hexdigest()
    pack = pattern.sub(lambda x: x.group(1) + new_digest + x.group(2), pack, count=1)
    PACK.write_text(pack, encoding="utf-8")
    m["batches"][-1]["status"] = "hash_pinned_combined_server_smoke_pending"
    m["note"] += (
        "\nB12 resolved: 7 exact official NeoForge 1.21.1 files "
        "(TrashSlot, TreeChop, Leaves Be Gone, Relics, Reliquified Artifacts, OctoLib, Puzzles Lib). "
        "147 total active pins; actual server/Prism runtime verification pending."
    )
    MANIFEST.write_text(json.dumps(m, indent=2) + "\n", encoding="utf-8")
    ensure(sum(x["enabled_in_pack"] for x in m["mods"]) == 147,
           "B12 pin count did not reach exactly 147")
    print("PASS B12: 147 exact active pins, 7 new provider JARs, no unreviewed dependencies")


if __name__ == "__main__":
    main()
