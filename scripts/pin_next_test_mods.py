#!/usr/bin/env python3
"""Pin the approved next-QA additions: Dragon Survival, Comforts and Colorful Hearts.

Fixed MC/loader/version IDs; exact provider files only. No mod binaries are
stored in Git or redistributed with Prism. The integration smoke downloads the
selected originals and verifies the resulting packwiz hashes independently.
"""
from __future__ import annotations
import hashlib
import io
import json
import re
import tomllib
import urllib.parse
import urllib.request
import zipfile
from pathlib import Path

DRAGON_FILE_ID = 9539454
DRAGON_PROJECT_ID = 420799
DRAGON_FILE = "DragonSurvival-1.21.1-v2.0.74-09.11.2026-all.jar"
MODRINTH = (
    ("comforts", "SaCpeal4", "3kpPjcTc", "9.0.5+1.21.1", "both"),
    ("colorful-hearts", "wQfMUdZT", "us0gze50", "10.5.9", "client"),
)
USER_AGENT = {"User-Agent": "TooMuchMagic-NeoForge-QA/1.0"}
ALLOWED_CDN = {"mediafilez.forgecdn.net", "edge.forgecdn.net"}

def get(url: str, timeout: int = 75) -> bytes:
    request = urllib.request.Request(url, headers=USER_AGENT)
    with urllib.request.urlopen(request, timeout=timeout) as response:
        return response.read()

def toml_quote(v: str) -> str:
    return json.dumps(v, ensure_ascii=False)

def modrinth_pin(mod_id: str, project: str, release: str, expected: str, side: str) -> tuple[str, str]:
    metadata = json.loads(get(f"https://api.modrinth.com/v2/version/{release}", 25))
    assert metadata.get("id") == release, f"{mod_id} version ID mismatch"
    assert metadata.get("project_id") == project, f"{mod_id} project ID mismatch"
    assert metadata.get("version_number") == expected, f"{mod_id} version mismatch: {metadata.get('version_number')}"
    assert "1.21.1" in metadata.get("game_versions", []), f"{mod_id} wrong game version"
    assert "neoforge" in metadata.get("loaders", []), f"{mod_id} wrong loader"
    assert metadata.get("version_type") == "release", f"{mod_id} is not a release"
    candidates = [f for f in metadata.get("files", [])
                  if f.get("primary") and f.get("filename", "").endswith(".jar")
                  and not f.get("filename", "").endswith("-sources.jar")]
    if len(candidates) != 1:
        raise ValueError(f"{mod_id}: expected one primary binary JAR, got {len(candidates)}")
    f = candidates[0]
    filename, url, sha = f["filename"], f["url"], f.get("hashes", {}).get("sha512")
    parsed = urllib.parse.urlparse(url)
    if (parsed.scheme != "https" or parsed.hostname != "cdn.modrinth.com" or
            not parsed.path.startswith(f"/data/{project}/versions/{release}/") or
            parsed.username or parsed.password):
        raise ValueError(f"{mod_id}: unrecognized official download URL")
    if not isinstance(sha, str) or not re.fullmatch("[0-9a-f]{128}", sha.lower()):
        raise ValueError(f"{mod_id}: missing SHA-512")
    if "1.21.1" not in filename or "/" in filename or "\\" in filename:
        raise ValueError(f"{mod_id}: wrong JAR filename")
    # Retain the exact upstream URLs, SHA-512, and pinned version IDs.
    meta = (f"name = {toml_quote('Comforts' if mod_id == 'comforts' else 'Colorful Hearts')}\n"
            f"filename = {toml_quote(filename)}\n"
            f"side = {toml_quote(side)}\n\n"
            f"[download]\nurl = {toml_quote(url)}\n"
            f'hash-format = "sha512"\nhash = "{sha.lower()}"\n\n'
            f'[update]\n[update.modrinth]\nmod-id = "{project}"\nversion = "{release}"\n')
    required = [d for d in metadata.get("dependencies", []) if d.get("dependency_type") == "required"]
    print(f"PIN {mod_id}: release={release}, sha512={sha.lower()}, requirements={required}")
    return meta, filename

def dragon_pin() -> tuple[str, str]:
    # Official CurseForge file ID 9539454, project 420799, release verified
    # for MC 1.21.1 NeoForge and requiring already installed GeckoLib (388172).
    # The original CDN payload is fetched ONLY to compute SHA-1, never checked in.
    folder = f"{DRAGON_FILE_ID // 1000}/{DRAGON_FILE_ID % 1000:03d}"
    path = f"/files/{folder}/{urllib.parse.quote(DRAGON_FILE)}"
    errors = []
    data = None
    for host in ("mediafilez.forgecdn.net", "edge.forgecdn.net"):
        if host not in ALLOWED_CDN:
            raise ValueError("Unapproved CurseForge download host")
        try:
            candidate = get("https://" + host + path)
            if len(candidate) < 1_000_000 or len(candidate) > 60_000_000:
                raise ValueError("Unexpected Dragon Survival JAR length")
            with zipfile.ZipFile(io.BytesIO(candidate)) as jar:
                names = set(jar.namelist())
                if "META-INF/neoforge.mods.toml" not in names and "META-INF/mods.toml" not in names:
                    raise ValueError("Dragon Survival missing NeoForge mod metadata")
                if not any("dragon" in n.lower() for n in names):
                    raise ValueError("Downloaded JAR is not Dragon Survival")
            data = candidate
            break
        except Exception as exc:
            errors.append(f"{host}: {type(exc).__name__} {exc}")
    if data is None:
        raise ValueError("Official Dragon Survival CDN retrieval failed: " + " | ".join(errors))
    sha1 = hashlib.sha1(data).hexdigest()
    meta = (f'name = "Dragon Survival"\nfilename = {toml_quote(DRAGON_FILE)}\n'
            'side = "both"\n\n[download]\nhash-format = "sha1"\n'
            f'hash = "{sha1}"\nmode = "metadata:curseforge"\n\n'
            f'[update]\n[update.curseforge]\nfile-id = {DRAGON_FILE_ID}\n'
            f'project-id = {DRAGON_PROJECT_ID}\n')
    print(f"PIN dragon-survival: project={DRAGON_PROJECT_ID} file={DRAGON_FILE_ID} sha1={sha1}")
    return meta, DRAGON_FILE

def activate(root: Path, manifest: dict) -> None:
    rows = {m["id"]: m for m in manifest["mods"]}
    assert "geckolib" in rows and rows["geckolib"]["enabled_in_pack"], "Required GeckoLib absent"
    assert (root / "mods/geckolib.pw.toml").is_file(), "Required GeckoLib not pinned"
    installs: list[tuple[str, str, str]] = []
    dragon_data, dragon_filename = dragon_pin()
    installs.append(("dragon-survival", dragon_data, dragon_filename))
    for mod_id, project, version, expected, side in MODRINTH:
        toml, filename = modrinth_pin(mod_id, project, version, expected, side)
        installs.append((mod_id, toml, filename))
    for mod_id, data, filename in installs:
        path = root / f"mods/{mod_id}.pw.toml"
        if path.exists():
            raise ValueError(f"{mod_id} already pinned; inspect source rather than replacing silently")
        path.write_text(data, encoding="utf-8")
        if mod_id not in rows:
            if mod_id != "colorful-hearts":
                raise ValueError(f"Missing preapproved manifest row for {mod_id}")
            row = {"id": mod_id, "name": "Colorful Hearts", "category": "qol",
                   "desired": True, "version": "10.5.9",
                   "release_status": "verified_release",
                   "source": "https://modrinth.com/mod/colorful-hearts/version/us0gze50",
                   "enabled_in_pack": False, "dependencies_verified": False,
                   "packwiz_slug": mod_id,
                   "note": "Owner-requested colored health layers instead of stacked hearts; client-only NeoForge 1.21.1 QA."}
            manifest["mods"].append(row)
            rows[mod_id] = row
        row = rows[mod_id]
        if not row.get("desired"):
            raise ValueError(f"{mod_id} not approved")
        row["enabled_in_pack"] = True
        row["packwiz_slug"] = mod_id
        row["dependencies_verified"] = mod_id == "dragon-survival"
        row["note"] = (
            "Exact version and remote binary hash resolved for combined next test. "
            "Run clean dedicated-server and client gameplay tests; NOT survival certified. "
            "Dragon requires existing GeckoLib; client-only hearts excluded from server."
        )
    print("NEXT QA PIN SET: Dragon Survival + Comforts + client-only Colorful Hearts")
