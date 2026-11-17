#!/usr/bin/env python3
"""Metadata-only resolver for the approved BetterEnd: New Dawn B16 pin set.

No NeoForge launch, full modpack assembly, Prism packaging or committed JARs.
Use only on an isolated, reviewed integration PR branch.
"""
from __future__ import annotations

import hashlib
import json
import re
import tomllib
import urllib.parse
import urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MINECRAFT = "1.21.1"
LOADER = "neoforge"
LIBRARIES = (
    ("bclib-new-dawn", "BCLib: New Dawn", "QkXw8Fsl", "21.0.26"),
    ("wunderlib-new-dawn", "WunderLib: New Dawn", "5db3GZzg", "21.0.10"),
    ("worldweaver-new-dawn", "WorldWeaver: New Dawn", "TrReu8cF", "21.0.25"),
)
OLD_RECIPE = "kubejs/data/tmm/recipe/spirit_fire/iesnium_from_end_draconium.json"
NEW_RECIPE = "kubejs/data/tmm/recipe/spirit_fire/iesnium_from_thallasium.json"
RECIPE = {
    "type": "occultism:spirit_fire",
    "ingredient": {"item": "betterend:thallasium_ore"},
    "result": {"count": 1, "id": "occultism:iesnium_ore"},
}


def get_json(url: str):
    req = urllib.request.Request(url, headers={"User-Agent": "TMM-Source-Pinning/2026-11-16"})
    with urllib.request.urlopen(req, timeout=35) as response:
        return json.load(response)


def modrinth_version(version_id: str, expected_version: str) -> dict:
    version = get_json("https://api.modrinth.com/v2/version/" + version_id)
    if MINECRAFT not in version.get("game_versions", []):
        raise RuntimeError(version_id + ": not published for Minecraft 1.21.1")
    if LOADER not in [str(x).lower() for x in version.get("loaders", [])]:
        raise RuntimeError(version_id + ": not a NeoForge version")
    if not str(version["version_number"]).startswith(expected_version):
        raise RuntimeError(version_id + ": unexpected version " + version["version_number"])
    jars = [f for f in version["files"] if f["filename"].endswith(".jar")]
    if len(jars) != 1:
        jars = [f for f in jars if f.get("primary")]
    if len(jars) != 1:
        raise RuntimeError(version_id + ": ambiguous upstream JAR variants")
    jar = jars[0]
    hash_value = jar.get("hashes", {}).get("sha512")
    if not isinstance(hash_value, str) or not re.fullmatch(r"[0-9a-f]{128}", hash_value):
        raise RuntimeError(version_id + ": no upstream SHA-512")
    if not jar["url"].startswith("https://cdn.modrinth.com/"):
        raise RuntimeError(version_id + ": non-Modrinth provider URL")
    return {
        "url": jar["url"], "filename": jar["filename"],
        "sha512": hash_value, "project_id": version["project_id"],
        "version_id": version_id, "version": version["version_number"],
        "provider": "modrinth"
    }


def betterend_exact_curseforge() -> dict:
    """Hash one exact approved CurseForge JAR; do not ship it or fetch a mod roster."""
    filename = "BetterEnd-21.0.39.jar"
    file_id = 9306604
    prefix = f"{file_id // 1000}/{file_id % 1000:03d}"
    urls = [
        f"https://mediafilez.forgecdn.net/files/{prefix}/{filename}",
        f"https://edge.forgecdn.net/files/{prefix}/{filename}",
    ]
    errors = []
    for url in urls:
        try:
            req = urllib.request.Request(url, headers={"User-Agent": "TMM-Source-Pinning/2026-11-16"})
            with urllib.request.urlopen(req, timeout=90) as response:
                digest = hashlib.sha512()
                total = 0
                head = b""
                while True:
                    chunk = response.read(1024 * 1024)
                    if not chunk:
                        break
                    if not head:
                        head = chunk[:4]
                    digest.update(chunk)
                    total += len(chunk)
                if head != b"PK\x03\x04" or not 10_000_000 < total < 200_000_000:
                    raise RuntimeError("unexpected JAR file header/length: " + str(total))
            return {
                "url": url, "filename": filename, "sha512": digest.hexdigest(),
                "project_id": "1422294", "file_id": str(file_id),
                "version": "21.0.39", "provider": "curseforge",
            }
        except Exception as exc:
            errors.append(type(exc).__name__ + ": " + str(exc)[:120])
    raise RuntimeError("Approved BetterEnd CurseForge file 9306604 unavailable: " + "; ".join(errors))


def safe_toml(s: str) -> str:
    return json.dumps(s, ensure_ascii=True)


def pin(mod_id: str, name: str, data: dict) -> tuple[str, str]:
    parts = [
        "name = " + safe_toml(name),
        "filename = " + safe_toml(data["filename"]),
        'side = "both"',
        "",
        "[download]",
        "url = " + safe_toml(data["url"]),
        'hash-format = "sha512"',
        "hash = " + safe_toml(data["sha512"]),
        "",
        "[update]",
    ]
    if data["provider"] == "modrinth":
        parts += ["[update.modrinth]", "mod-id = " + safe_toml(data["project_id"]),
                  "version = " + safe_toml(data["version_id"])]
    else:
        parts += ["[update.curseforge]", "project-id = " + data["project_id"],
                  "file-id = " + data["file_id"]]
    path = "mods/" + mod_id + ".pw.toml"
    content = "\n".join(parts) + "\n"
    tomllib.loads(content)
    (ROOT / path).write_text(content, encoding="utf-8")
    return path, content


def update_roster(selected: list[tuple[str, str, dict]]) -> None:
    path = ROOT / "manifest/mods.json"
    roster = json.loads(path.read_text(encoding="utf-8"))
    for mod_id, name, data in selected:
        existing = next((x for x in roster["mods"] if x.get("id") == mod_id), None)
        if existing is None:
            existing = {"id": mod_id, "name": name,
                        "category": "worldgen" if mod_id == "betterend-new-dawn" else "dependency",
                        "desired": True}
            roster["mods"].append(existing)
        existing.update({
            "version": data["version"],
            "release_status": "verified_release",
            "source": data["url"],
            "enabled_in_pack": True,
            "dependencies_verified": True,
            "packwiz_slug": mod_id,
            "note": "Hash-pinned 1.21.1 NeoForge source file for B16 with all three New Dawn libraries. Source/index only; End worldgen, Thallasium ore and client/server runtime NOT TESTED.",
        })
    path.write_text(json.dumps(roster, indent=2) + "\n", encoding="utf-8")


def update_index(replacements: dict[str, str], removals: set[str]) -> None:
    index_path = ROOT / "index.toml"
    pack_path = ROOT / "pack.toml"
    original = index_path.read_text(encoding="utf-8")
    sections = original.split("[[files]]\n")
    header, blocks = sections[0], ["[[files]]\n" + s for s in sections[1:]]
    found = set()
    kept = []
    for block in blocks:
        match = re.search(r'^file = "([^"]+)"$', block, flags=re.MULTILINE)
        if match is None:
            raise RuntimeError("malformed Packwiz index section")
        file = match.group(1)
        if file in removals or file in replacements:
            found.add(file)
            continue
        kept.append(block)
    for file, content in replacements.items():
        digest = hashlib.sha256(content.encode("utf-8")).hexdigest()
        if file not in found and (ROOT / file).exists() is False:
            raise RuntimeError("unwritten Packwiz file: " + file)
        block = '[[files]]\nfile = "' + file + '"\nhash = "' + digest + '"\n'
        if file.startswith("mods/"):
            block += "metafile = true\n"
        block += "\n"
        at = next((i for i, b in enumerate(kept)
                  if (m := re.search(r'^file = "([^"]+)"$', b, flags=re.MULTILINE))
                  and m.group(1).startswith(file.split("/")[0] + "/") and m.group(1) > file), len(kept))
        kept.insert(at, block)
    result = header + "".join(kept)
    old_hash = hashlib.sha256(original.encode("utf-8")).hexdigest()
    pack = pack_path.read_text(encoding="utf-8")
    if pack.count(old_hash) != 1:
        raise RuntimeError("Packwiz root index hash already stale")
    index_path.write_text(result, encoding="utf-8")
    pack_path.write_text(pack.replace(old_hash, hashlib.sha256(result.encode("utf-8")).hexdigest()), encoding="utf-8")


def main() -> None:
    selected = [("betterend-new-dawn", "BetterEnd: New Dawn", betterend_exact_curseforge())]
    for mod_id, name, version_id, expected in LIBRARIES:
        selected.append((mod_id, name, modrinth_version(version_id, expected)))
    files = dict(pin(mid, name, data) for mid, name, data in selected)
    p = ROOT / NEW_RECIPE
    if not (ROOT / OLD_RECIPE).is_file():
        raise RuntimeError("Expected original Iesnium source recipe missing")
    p.parent.mkdir(parents=True, exist_ok=True)
    recipe_content = json.dumps(RECIPE, indent=2) + "\n"
    p.write_text(recipe_content, encoding="utf-8")
    (ROOT / OLD_RECIPE).unlink()
    files[NEW_RECIPE] = recipe_content
    validator = ROOT / "scripts/validate_cc_iesnium_pilot.py"
    old_validator = validator.read_text(encoding="utf-8")
    if OLD_RECIPE not in old_validator or "draconicevolution:end_draconium_ore" not in old_validator:
        raise RuntimeError("Current CC Iesnium validator drift; review manually")
    new_validator = old_validator.replace(OLD_RECIPE, NEW_RECIPE).replace(
        "draconicevolution:end_draconium_ore", "betterend:thallasium_ore")
    validator.write_text(new_validator, encoding="utf-8")
    update_roster(selected)
    update_index(files, {OLD_RECIPE})
    print("PINNED B16: BetterEnd 21.0.39, three NeoForge New Dawn libraries, Thallasium Spirit Fire")
    for mid, _, data in selected:
        print(mid, data["version"], data["sha512"])
    print("NOT TESTED: NeoForge server, client, Thallasium ore harvest, End terrain")


if __name__ == "__main__":
    main()
