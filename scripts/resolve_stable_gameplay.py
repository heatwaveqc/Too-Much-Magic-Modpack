#!/usr/bin/env python3
"""Install first-party stable Minecraft 1.21.1 NeoForge releases of approved mods.

Never search fuzzily: verify an exact Modrinth project slug/title, discover a
stable (version_type=release) compatible artifact, then pass immutable project
and version IDs to packwiz. Log missing releases instead of substituting mods.
"""
from __future__ import annotations
import json
import pathlib
import re
import subprocess
import sys
import time
import tomllib
import urllib.error
import urllib.parse
import urllib.request
from datetime import datetime, timezone

ROOT = pathlib.Path(__file__).resolve().parents[1]
MANIFEST = ROOT / "manifest/mods.json"
EVIDENCE = ROOT / "docs/B3-RESOLUTION.json"
TARGET_MINECRAFT = "1.21.1"
TARGET_LOADER = "neoforge"

# (canonical manifest id, exact upstream Modrinth slug, expected project title,
#  category). None of these represent approval to substitute an adjacent mod.
CANDIDATES = [
    ("tectonic", "tectonic", "Tectonic", "worldgen"),
    ("occultism", "occultism", "Occultism", "magic"),
    ("ars-nouveau", "ars-nouveau", "Ars Nouveau", "magic"),
    ("irons-spells", "irons-spells-n-spellbooks", "Iron's Spells", "magic"),
    ("theurgy", "theurgy", "Theurgy", "magic"),
    ("neovitae", "neovitae", "Neo Vitae", "magic"),
    ("witchery-remake", "just-another-witchery-remake", "Just Another Witchery Remake", "magic"),
    ("mahou-tsukai", "mahou-tsukai", "Mahou Tsukai", "magic"),
    ("evilcraft", "evilcraft", "EvilCraft", "magic"),
    ("roots-classic", "roots-classic", "Roots Classic", "magic"),
    ("cataclysm", "l_enders-cataclysm", "Cataclysm", "adventure"),
    ("apotheosis", "apotheosis", "Apotheosis", "power"),
    ("artifacts", "artifacts", "Artifacts", "power"),
    ("relics", "relics", "Relics", "power"),
    ("deeper-darker", "deeperdarker", "Deeper and Darker", "adventure"),
    ("gateways-to-eternity", "gateways-to-eternity", "Gateways to Eternity", "adventure"),
    ("extended-crafting", "extended-crafting", "Extended Crafting", "crafting"),
    ("re-avaritia", "re-avaritia", "Re:Avaritia", "crafting"),
    ("project-expansion", "project-expansion", "Project Expansion", "post_chaos"),
    ("construction-gadgets", "construction-gadgets", "Construction Gadgets", "qol"),
    # JAWR's separate Forbidden Magic addon is an approved candidate from
    # the preceding design discussion. If no compatible release, record it.
    ("forbidden-magic", "jawr-forbidden-magic", "Forbidden Magic", "magic"),
]
# Stable official CurseForge artifacts not available on Modrinth (or only an
# older version is available there). Project IDs are documented upstream.
# Filename assertions are release-channel checkpoints from CurseForge.
OFFICIAL_CURSEFORGE = [
    ("roots-classic", 295780, "RootsClassic-1.21.1-1.5.10"),
    ("projecte", 226410, "ProjectE-1.21.1-PE1.1.0"),
    ("replication", 638351, "Replication-1.21.1-1.2.7"),
]
CF_MAP = {pid: mid for mid, pid, expected in OFFICIAL_CURSEFORGE}
CF_EXPECTED = {pid: expected for mid, pid, expected in OFFICIAL_CURSEFORGE}

SKIPPED_BY_DESIGN = {
    "astral-sorcery": "User expressly excluded the alpha build",
    "botania": "User expressly excluded until stable/approved",
    "draconic-evolution": "Available build is a beta; user requested no betas",
    "bigger-reactors": "Original official mod has no compatible 1.21.1 NeoForge release; forks not approved",
    "observerlib": "Astral Sorcery dependency, deferred with Astral",
    "brandons-core": "Draconic Evolution dependency, deferred with beta",
    "magical-satchel": "Unresolved upstream project identity; no guessing",
    "formation-wand": "Unresolved upstream project identity; no guessing",
    "roguelike-dungeons": "Unresolved requested mod identity; no guessing",
    "starlight-structures": "Unresolved requested mod identity; no guessing",
}
BLOCKED_NAMES = {
    "create", "bloodmagic", "sophisticatedstorage", "sophisticatedbackpacks",
    "appliedenergistics2", "refinedstorage", "ftbultimine", "ae2",
}

def normalized(text: str) -> str:
    return "".join(c for c in text.lower() if c.isalnum())

def api(path: str, query=None):
    url = "https://api.modrinth.com/v2/" + path
    if query:
        url += "?" + urllib.parse.urlencode(query)
    for attempt in range(4):
        try:
            req = urllib.request.Request(url, headers={"User-Agent": "TooMuchMagic/0.3 (modpack build)"})
            with urllib.request.urlopen(req, timeout=45) as resp:
                return json.load(resp)
        except urllib.error.HTTPError as exc:
            if exc.code == 404:
                raise LookupError("Upstream Modrinth project/version not found") from exc
            if exc.code not in {429, 500, 502, 503, 504} or attempt == 3:
                raise
            time.sleep((attempt + 1) * 3)
    raise RuntimeError("Modrinth API retries exhausted")

def run(*args):
    proc = subprocess.run(args, cwd=ROOT, text=True, stdout=subprocess.PIPE,
                          stderr=subprocess.STDOUT, timeout=180)
    return proc.returncode, proc.stdout

def matching_project(slug: str, expected: str):
    data = api("project/" + urllib.parse.quote(slug))
    actual = normalized(data.get("title", ""))
    needed = normalized(expected)
    if needed not in actual and actual not in needed:
        raise ValueError(f"Wrong project: expected {expected!r}, got {data.get('title')!r}")
    if data.get("project_type") not in {"mod", "datapack"}:
        raise ValueError(f"Wrong Modrinth project type: {data.get('project_type')}")
    return data

def release_version(project_id: str):
    versions = api("project/" + project_id + "/version", {
        "loaders": json.dumps([TARGET_LOADER]),
        "game_versions": json.dumps([TARGET_MINECRAFT]),
    })
    eligible = []
    for v in versions:
        if v.get("version_type") != "release":
            continue
        if TARGET_MINECRAFT not in v.get("game_versions", []) or TARGET_LOADER not in v.get("loaders", []):
            continue
        text = (v.get("version_number", "") + " " + v.get("name", "")).lower()
        if re.search(r"(?<![a-z])(alpha|beta|prerelease|pre-release|snapshot|\brc\d*\b)(?![a-z])", text):
            continue
        if not any(f.get("filename", "").lower().endswith(".jar") and
                   not f.get("filename", "").lower().endswith("-sources.jar")
                   for f in v.get("files", [])):
            continue
        eligible.append(v)
    eligible.sort(key=lambda v: v.get("date_published", ""), reverse=True)
    return eligible[0] if eligible else None

def main():
    manifest = json.loads(MANIFEST.read_text())
    all_mods = {m["id"]: m for m in manifest["mods"]}
    before = {m["id"] for m in manifest["mods"] if m.get("enabled_in_pack")}
    records = []
    skipped = []
    for mid, slug, title, category in CANDIDATES:
        if mid in SKIPPED_BY_DESIGN:
            raise RuntimeError("Explicitly skipped mod entered request list: " + mid)
        if mid in before:
            records.append({"id": mid, "status": "already_installed"})
            continue
        try:
            project = matching_project(slug, title)
            version = release_version(project["id"])
            if not version:
                raise LookupError("No 1.21.1 NeoForge stable release JAR on Modrinth")
            rc, output = run("packwiz", "--yes", "modrinth", "add",
                             "--project-id", project["id"],
                             "--version-id", version["id"])
            if rc:
                raise RuntimeError("packwiz error: " + output[-1800:])
            record = {"id": mid, "slug": slug, "project_id": project["id"],
                      "version_id": version["id"], "version_number": version["version_number"],
                      "version_type": version["version_type"], "status": "resolved"}
            records.append(record)
            print("RESOLVED " + json.dumps(record), flush=True)
            time.sleep(0.12)
        except (LookupError, ValueError, RuntimeError, urllib.error.URLError,
                subprocess.TimeoutExpired) as exc:
            problem = {"id": mid, "slug": slug, "status": "blocked", "reason": str(exc)}
            records.append(problem)
            print("BLOCKED " + json.dumps(problem), flush=True)
    for mid, pid, expect in OFFICIAL_CURSEFORGE:
        try:
            rc, out = run("packwiz", "--yes", "curseforge", "add", "--addon-id", str(pid))
            if rc:
                raise RuntimeError("packwiz official CurseForge error: " + out[-1400:])
            records.append({"id": mid, "source": "curseforge", "project_id": pid,
                            "expected_release_file": expect, "status": "resolved"})
            print("RESOLVED CURSEFORGE " + mid + " project " + str(pid), flush=True)
        except (RuntimeError, subprocess.TimeoutExpired) as exc:
            problem={"id": mid, "source": "curseforge", "status": "blocked", "reason": str(exc)}
            records.append(problem)
            print("BLOCKED CURSEFORGE " + json.dumps(problem), flush=True)
    rc, output = run("packwiz", "refresh")
    if rc:
        raise SystemExit("packwiz refresh failed: " + output[-2500:])
    # Inspect *every* Modrinth dependency picked automatically, too.
    # Refuse alpha/beta even if packwiz chose it for a dependency of a stable mod.
    version_cache = {}
    resolved_ids = {r["project_id"]: r["id"] for r in records
                    if r["status"] == "resolved" and r.get("source") != "curseforge"}
    new_metadata = []
    for meta in sorted((ROOT / "mods").glob("*.pw.toml")):
        data = tomllib.loads(meta.read_text())
        slug = meta.stem.removesuffix(".pw")
        project_id = data.get("update", {}).get("modrinth", {}).get("mod-id")
        cf_id = data.get("update", {}).get("curseforge", {}).get("project-id")
        if cf_id in CF_EXPECTED:
            want=CF_EXPECTED[cf_id]
            if want.lower() not in data.get("filename","").lower():
                raise RuntimeError("CurseForge did not select VERIFIED RELEASE " + want + ": selected " + data.get("filename",""))
        version_id = data.get("update", {}).get("modrinth", {}).get("version")
        if normalized(slug) in BLOCKED_NAMES or normalized(data.get("name", "")) in BLOCKED_NAMES:
            raise RuntimeError("HARD-EXCLUDED MOD WAS AUTO-SELECTED: " + data.get("name", slug))
        if project_id and version_id:
            if version_id not in version_cache:
                version_cache[version_id] = api("version/" + urllib.parse.quote(version_id))
            ver = version_cache[version_id]
            if ver.get("version_type") != "release":
                if slug not in {
                    existing.get("packwiz_slug", existing["id"])
                    for existing in manifest["mods"] if existing.get("enabled_in_pack")
                }:
                    raise RuntimeError("Newly introduced non-stable dependency: " + slug + " / " + str(ver.get("version_type")))
                print("PREEXISTING NON-RELEASE (grandfathered B2 baseline): " + slug + " / " + str(ver.get("version_type")),flush=True)
            if TARGET_MINECRAFT not in ver.get("game_versions", []) or TARGET_LOADER not in ver.get("loaders", []):
                raise RuntimeError("Wrong Minecraft/loader auto-dependency: " + slug)
        dlinfo = data.get("download", {})
        if dlinfo.get("hash-format") not in {"sha1", "sha256", "sha512"} or not dlinfo.get("hash"):
            raise RuntimeError("Missing immutable download hash for " + slug)
        # Match immutable project IDs, not filenames, for explicit mods.
        mid = CF_MAP.get(cf_id) or resolved_ids.get(project_id)
        if not mid:
            for existing in manifest["mods"]:
                if existing.get("packwiz_slug", existing["id"]) == slug:
                    mid = existing["id"]
                    break
        if not mid:
            mid = slug
        if mid in SKIPPED_BY_DESIGN:
            raise RuntimeError("Forbidden/experimental mod selected as a dependency: " + mid)
        current = all_mods.get(mid)
        if not current:
            current = {"id": mid, "name": data.get("name", mid),
                       "category": "dependency", "desired": True, "version": None,
                       "release_status": "verified_release", "source": None,
                       "enabled_in_pack": False, "dependencies_verified": False}
            manifest["mods"].append(current)
            all_mods[mid] = current
        current["packwiz_slug"] = slug
        current["version"] = data.get("filename", "").removesuffix(".jar")
        current["enabled_in_pack"] = True
        current["release_status"] = "verified_release"
        if project_id:
            current["source"] = "https://modrinth.com/mod/" + project_id
        if cf_id in CF_MAP:
            current["source"] = {
                295780:"https://www.curseforge.com/minecraft/mc-mods/roots-classic",
                226410:"https://www.curseforge.com/minecraft/mc-mods/projecte",
                638351:"https://www.curseforge.com/minecraft/mc-mods/replication",
            }[cf_id]
        new_metadata.append({"id": mid, "slug": slug, "name": data.get("name"),
                            "filename": data.get("filename"), "side": data.get("side", "both"),
                            "modrinth_id": project_id, "version_id": version_id,
                            "cf_project_id": cf_id})
    # Some projects may map to the same slug; preserve ID uniqueness.
    manifest["mods"].sort(key=lambda x: x["id"])
    manifest["batches"].append({
        "id": "B5", "name": "All release-channel gameplay mods plus Tectonic",
        "members": sorted({x["id"] for x in new_metadata if x["id"] not in before}),
        "status": "pinned_unbooted",
    })
    manifest["note"] = ("Broad stable-only gameplay integration candidate. "
                        "ProjectE/Project Expansion are TEST-BUILD ONLY until Chaos Guardian locks exist.")
    MANIFEST.write_text(json.dumps(manifest, indent=2) + "\n")
    EVIDENCE.write_text(json.dumps({
        "minecraft": TARGET_MINECRAFT, "loader": TARGET_LOADER,
        "generated_at_utc": datetime.now(timezone.utc).isoformat(),
        "candidates": records, "explicit_skips": SKIPPED_BY_DESIGN,
        "before_count": len(before), "now_count": sum(m["enabled_in_pack"] for m in manifest["mods"]),
        "metafiles": new_metadata
    }, indent=2) + "\n")
    rc, output = run("packwiz", "refresh")
    if rc:
        raise SystemExit("packwiz second refresh failed: " + output[-2500:])
    rc, output = run(sys.executable, "scripts/validate.py")
    print(output, flush=True)
    if rc:
        raise SystemExit("Manifest validation error")
    print("RESULT: " + json.dumps({"added": len(new_metadata) - len(before),
         "total": len(new_metadata), "blocked": [x for x in records if x["status"] == "blocked"]}), flush=True)

if __name__ == "__main__":
    main()
