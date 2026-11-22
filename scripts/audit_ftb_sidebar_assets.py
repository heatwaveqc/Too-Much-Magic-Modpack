#!/usr/bin/env python3
"""Read-only, pinned-JAR FTB sidebar icon audit (#507); never launches Minecraft.

Source-only mode verifies the Packwiz lock and searches pack-owned asset
overrides. --mods-dir additionally verifies exact installed FTB JAR hashes and
examines sidebar JSON and direct texture paths across installed client mods.
Inspects packaged atlas source declarations; renderer/shader failures still require client QA.
"""
from __future__ import annotations

import argparse
import hashlib
import json
from pathlib import Path
import re
import sys
import tomllib
import zipfile

ROOT = Path(__file__).resolve().parents[1]
FTB_PIN_SLUGS = (
    "ftb-library-forge",
    "ftb-quests-forge",
    "ftb-teams-forge",
    "ftb-chunks-forge",
    "ftb-essentials",
    "ftb-filter-system",
    "ftb-xmod-compat",
)
DIRECT_TEXTURE = re.compile(r"^([a-z0-9_.-]+):textures/([a-z0-9_./-]+[.]png)$")
SIDEBAR_FILE = re.compile(r"^assets/([a-z0-9_.-]+)/sidebar_buttons/.+[.]json$")
ASSET_FILE = re.compile(r"^assets/[a-z0-9_.-]+/.+")
MAX_SIDEBAR_JSON_BYTES = 512 * 1024
BLOCK_ATLAS_FILE = "assets/minecraft/atlases/blocks.json"
ATLAS_ID = re.compile(r"^([a-z0-9_.-]+):([a-z0-9_./-]+)$")


def file_hash(path: Path, algorithm: str) -> str:
    digest = hashlib.new(algorithm)
    with path.open("rb") as stream:
        for chunk in iter(lambda: stream.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()


def pinned_ftb_files(root: Path) -> dict[str, dict]:
    """Fail closed on a stale Packwiz hash chain or missing exact FTB pin."""
    pack = tomllib.loads((root / "pack.toml").read_text(encoding="utf-8"))
    index_path = root / pack["index"]["file"]
    if file_hash(index_path, pack["index"]["hash-format"]) != pack["index"]["hash"]:
        raise ValueError("pack.toml/index.toml SHA mismatch")
    index = tomllib.loads(index_path.read_text(encoding="utf-8"))
    entries = {entry["file"]: entry for entry in index["files"]}
    out = {}
    for slug in FTB_PIN_SLUGS:
        rel = f"mods/{slug}.pw.toml"
        if rel not in entries or not entries[rel].get("metafile"):
            raise ValueError(f"FTB mod not indexed as Packwiz metadata: {rel}")
        entry = entries[rel]
        if file_hash(root / rel, entry.get("hash-format", index["hash-format"])) != entry["hash"]:
            raise ValueError(f"stale Packwiz FTB pin digest: {rel}")
        pin = tomllib.loads((root / rel).read_text(encoding="utf-8"))
        filename = pin["filename"]
        if "/" in filename or "\\" in filename or not filename.endswith(".jar"):
            raise ValueError(f"unsafe FTB filename: {filename}")
        if pin["download"]["hash-format"] not in ("sha1", "sha256", "sha512"):
            raise ValueError(f"unsupported FTB JAR checksum: {rel}")
        out[slug] = pin
    return out


def asset_overrides(root: Path) -> list[str]:
    """Report, but do not modify, pack-owned FTB assets."""
    base = root / "kubejs" / "assets"
    if not base.exists():
        return []
    return sorted(
        p.relative_to(base).as_posix()
        for p in base.rglob("*")
        if p.is_file() and p.relative_to(base).parts[0].startswith("ftb")
    )


def icon_tokens(data: object) -> list[str]:
    """Only the sidebar JSON's icon field, never click/URL/help text."""
    if not isinstance(data, dict):
        raise ValueError("sidebar JSON root is not an object")
    result = []
    def walk(value: object) -> None:
        if isinstance(value, str):
            result.append(value)
        elif isinstance(value, list):
            for child in value:
                walk(child)
        elif isinstance(value, dict):
            for child in value.values():
                walk(child)
    walk(data.get("icon", []))
    return result


def audit_sidebar_resources(archives: dict[str, dict[str, bytes]]) -> dict:
    """Inspect already-read resources. Atlas evidence != proof of live stitching."""
    providers: dict[str, list[str]] = {}
    atlas_sources = []
    bad_atlases = []
    definitions = []
    missing_direct = []
    symbolic = []
    atlas_evidence = []
    malformed = []
    for provider, resources in sorted(archives.items()):
        for name, raw in resources.items():
            if ASSET_FILE.fullmatch(name):
                providers.setdefault(name, []).append(provider)
            if name != BLOCK_ATLAS_FILE:
                continue
            try:
                parsed = json.loads(raw)
                if not isinstance(parsed, dict) or not isinstance(parsed.get("sources"), list):
                    raise ValueError("atlas must have a sources array")
                for entry in parsed["sources"]:
                    if isinstance(entry, dict) and entry.get("type") == "directory":
                        source, prefix = entry.get("source"), entry.get("prefix")
                        if isinstance(source, str) and isinstance(prefix, str):
                            atlas_sources.append({
                                "provider": provider, "source": source, "prefix": prefix,
                            })
            except (ValueError, UnicodeDecodeError) as exc:
                bad_atlases.append({"provider": provider, "reason": str(exc)})

    for provider, resources in sorted(archives.items()):
        for name, raw in sorted(resources.items()):
            if not SIDEBAR_FILE.fullmatch(name):
                continue
            if len(raw) > MAX_SIDEBAR_JSON_BYTES:
                malformed.append({"provider": provider, "path": name, "reason": "oversized JSON"})
                continue
            try:
                icons = icon_tokens(json.loads(raw))
            except (ValueError, UnicodeDecodeError) as exc:
                malformed.append({"provider": provider, "path": name, "reason": str(exc)})
                continue
            direct = []
            atlas_icons = []
            for icon in icons:
                # The compiled FTB Library Icon.getIcon(String) parser splits
                # semicolon properties before resolving PNG vs atlas sprite.
                base = icon.split(";", 1)[0].strip()
                match = DIRECT_TEXTURE.fullmatch(base)
                if match:
                    resource = f"assets/{match.group(1)}/textures/{match.group(2)}"
                    direct.append(resource)
                    if resource not in providers:
                        missing_direct.append({"provider": provider, "path": name, "missing": resource})
                    continue

                # Any remaining standard namespace:path ID is an AtlasSpriteIcon.
                # Sprite IDs may be registered by minecraft's own blocks atlas or
                # other installed packs, so absence from these JARs is not an
                # unconditional texture error.
                atlas_icons.append(icon)
                parsed_id = ATLAS_ID.fullmatch(base)
                if not parsed_id:
                    atlas_evidence.append({
                        "provider": provider, "path": name, "icon": base,
                        "status": "other_icon_expression_needs_runtime_resolution",
                    })
                    continue
                namespace, sprite = parsed_id.groups()
                candidates = []
                for source in atlas_sources:
                    prefix = source["prefix"]
                    if sprite.startswith(prefix):
                        candidate = (
                            f"assets/{namespace}/textures/{source['source']}/"
                            f"{sprite[len(prefix):]}.png"
                        )
                        if candidate in providers:
                            candidates.append({"path": candidate, "atlas_provider": source["provider"]})
                if candidates:
                    status = "candidate_png_and_packaged_atlas_source_present"
                else:
                    png = f"assets/{namespace}/textures/{sprite}.png"
                    status = (
                        "candidate_png_present_but_atlas_source_not_in_scanned_jars"
                        if png in providers
                        else "no_candidate_png_in_scanned_jars"
                    )
                atlas_evidence.append({
                    "provider": provider, "path": name, "icon": base,
                    "status": status, "candidate_sources": candidates,
                })
            if atlas_icons:
                symbolic.append({"provider": provider, "path": name, "icons": atlas_icons})
            definitions.append({
                "provider": provider, "path": name,
                "direct_textures": direct, "symbolic_count": len(atlas_icons),
            })
    duplicates = [
        {"path": name, "providers": owners}
        for name, owners in sorted(providers.items())
        if len(owners) > 1 and ("sidebar_buttons/" in name or "/textures/icons/" in name)
    ]
    return {
        "sidebar_definitions": definitions,
        "missing_direct_textures": missing_direct,
        "symbolic_icons_needing_runtime_resolution": symbolic,
        "atlas_icon_evidence": atlas_evidence,
        "packaged_block_atlas_directory_sources": atlas_sources,
        "malformed_block_atlases": bad_atlases,
        "ambiguous_asset_overrides": duplicates,
        "malformed_definitions": malformed,
    }


def scan_installed(mods_dir: Path, pins: dict[str, dict]) -> dict:
    if not mods_dir.is_dir():
        raise ValueError(f"not an installed mod directory: {mods_dir}")
    for slug, pin in pins.items():
        jar = mods_dir / pin["filename"]
        if not jar.is_file():
            raise ValueError(f"{slug}: required exact installed JAR missing: {jar.name}")
        fmt = pin["download"]["hash-format"]
        if file_hash(jar, fmt) != pin["download"]["hash"]:
            raise ValueError(f"{slug}: installed JAR checksum differs from Packwiz")
    archives: dict[str, dict[str, bytes]] = {}
    for jar in sorted(mods_dir.glob("*.jar")):
        resources = {}
        try:
            with zipfile.ZipFile(jar) as z:
                for name in z.namelist():
                    if not ASSET_FILE.fullmatch(name):
                        continue
                    if SIDEBAR_FILE.fullmatch(name) or name == BLOCK_ATLAS_FILE:
                        info = z.getinfo(name)
                        if info.file_size > MAX_SIDEBAR_JSON_BYTES:
                            resources[name] = b" " * (MAX_SIDEBAR_JSON_BYTES + 1)
                        else:
                            resources[name] = z.read(name)
                    else:
                        resources[name] = b""
        except (OSError, zipfile.BadZipFile) as exc:
            raise ValueError(f"bad installed JAR {jar.name}: {exc}") from exc
        archives[jar.name] = resources
    result = audit_sidebar_resources(archives)
    result["verified_ftb_pins"] = {slug: pin["filename"] for slug, pin in pins.items()}
    result["scanned_jars"] = len(archives)
    return result


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--pack-root", type=Path, default=ROOT)
    parser.add_argument("--mods-dir", type=Path, help="Optional installed mods/ directory; never downloads JARs")
    parser.add_argument("--output", type=Path, help="Optional local JSON report file")
    args = parser.parse_args()
    try:
        pins = pinned_ftb_files(args.pack_root)
        report = {
            "schema": 1,
            "issue": 507,
            "mode": "installed_jar_evidence" if args.mods_dir else "source_only",
            "ftb_pins": {slug: pin["filename"] for slug, pin in pins.items()},
            "pack_ftb_asset_overrides": asset_overrides(args.pack_root),
            "limitations": [
                "Source/ZIP analysis does not verify live icon renderer, atlas, font, shader, resource-pack priority or click behavior.",
                "Packaged atlas source + PNG presence does not prove final loaded/stiched Minecraft block atlas.",
                "Atlas sprite IDs from vanilla or other packs require full resource pack evaluation.",
                "A clean client GUI and personal-profile comparison remain required for #507.",
            ],
        }
        if args.mods_dir:
            report.update(scan_installed(args.mods_dir, pins))
        rendered = json.dumps(report, indent=2, sort_keys=True) + "\n"
        if args.output:
            args.output.parent.mkdir(parents=True, exist_ok=True)
            args.output.write_text(rendered, encoding="utf-8")
        print(rendered, end="")
        if args.mods_dir and (
            report["missing_direct_textures"] or report["malformed_definitions"]
            or report["malformed_block_atlases"]
        ):
            return 1
        return 0
    except (OSError, ValueError, KeyError, TypeError, tomllib.TOMLDecodeError) as exc:
        print(f"FTB SIDEBAR AUDIT FAIL: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
