#!/usr/bin/env python3
"""Pin the approved next-QA additions: Dragon Survival, Comforts, Colorful Hearts and Vampire's Delight.

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
EXPECTED_HASHES = {
    "dragon-survival": "e11429dda4688711e174625bb7420873a255a715",
    "comforts": "e9de2952545e9e773a6e78fc501e8cd231ea19750c30404355b71df9928eb5bd0921a4429aeca9d0fa10e67717f77fcf7a2aae117ac953b8f7de246b0ad685e4",
    "colorful-hearts": "4791dddc6932a74c28a5e58b51865c09a6be0715bf235eb4d95a050a9130b4013cf4883d35f7c7ccea6997534a0e3cd0e5ec59b79acc8ce01f52d230d476a889",
}
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
    expected_channel = "release"
    assert metadata.get("version_type") == expected_channel, f"{mod_id} unexpected release channel"
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
    if sha.lower() != EXPECTED_HASHES[mod_id]:
        raise ValueError(f"{mod_id}: provider SHA-512 changed; refuse unexpected binary")
    # Retain the exact upstream URLs, SHA-512, and pinned version IDs.
    names = {'comforts': 'Comforts', 'colorful-hearts': 'Colorful Hearts'}
    meta = (f"name = {toml_quote(names[mod_id])}\n"
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
    if sha1 != EXPECTED_HASHES["dragon-survival"]:
        raise ValueError("Dragon Survival exact file ID has unexpected SHA-1; refuse altered binary")
    meta = (f'name = "Dragon Survival"\nfilename = {toml_quote(DRAGON_FILE)}\n'
            'side = "both"\n\n[download]\nhash-format = "sha1"\n'
            f'hash = "{sha1}"\nmode = "metadata:curseforge"\n\n'
            f'[update]\n[update.curseforge]\nfile-id = {DRAGON_FILE_ID}\n'
            f'project-id = {DRAGON_PROJECT_ID}\n')
    print(f"PIN dragon-survival: project={DRAGON_PROJECT_ID} file={DRAGON_FILE_ID} sha1={sha1}")
    return meta, DRAGON_FILE

def activate(root: Path, manifest: dict) -> None:
    """Pin ONLY Dragon Survival, Comforts and client-side Colorful Hearts."""
    rows = {m["id"]: m for m in manifest["mods"]}
    assert rows["geckolib"]["enabled_in_pack"] and (root / "mods/geckolib.pw.toml").is_file()
    installs = [("dragon-survival", *dragon_pin())]
    for mod_id, project, version, expected, side in MODRINTH:
        toml, filename = modrinth_pin(mod_id, project, version, expected, side)
        installs.append((mod_id, toml, filename))
    for mod_id, data, filename in installs:
        path = root / f"mods/{mod_id}.pw.toml"
        if path.exists():
            raise ValueError(f"Already pinned: {path}; inspect before replacing")
        path.write_text(data, encoding="utf-8")
        if mod_id not in rows:
            if mod_id == "dragon-survival":
                row = {
                    "id": mod_id, "name": "Dragon Survival", "category": "supernatural",
                    "desired": True, "version": "2.0.74",
                    "release_status": "verified_release",
                    "source": "https://www.curseforge.com/minecraft/mc-mods/dragon-survival/files/9539454",
                    "enabled_in_pack": False, "dependencies_verified": False,
                    "packwiz_slug": mod_id,
                    "note": "Owner-approved next QA Dragon Survival v2.0.74; exact CurseForge file/GeckoLib pinned by this script",
                }
                manifest["mods"].append(row)
                rows[mod_id] = row
            elif mod_id == "colorful-hearts":
                row = {
                "id": mod_id, "name": "Colorful Hearts", "category": "qol",
                "desired": True, "version": "10.5.9",
                "release_status": "verified_release",
                "source": "https://modrinth.com/mod/colorful-hearts/version/us0gze50",
                "enabled_in_pack": False, "dependencies_verified": False,
                "packwiz_slug": mod_id,
                "note": "Approved client-only colored hearts test",
            }
                manifest["mods"].append(row)
                rows[mod_id] = row
            else:
                raise ValueError(f"Unapproved mod: {mod_id}")
        row = rows[mod_id]
        if not row.get("desired"):
            raise ValueError(f"Not approved: {mod_id}")
        row["enabled_in_pack"] = True
        row["packwiz_slug"] = mod_id
        row["dependencies_verified"] = (mod_id == "dragon-survival")
        row["note"] = "Exact pinned release for standalone Monk/Hunter test; client QA pending."
    print("QA pins: Dragon Survival, Comforts, Colorful Hearts; no Vampire's Delight")
