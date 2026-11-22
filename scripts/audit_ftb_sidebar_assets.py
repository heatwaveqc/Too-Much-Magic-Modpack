#!/usr/bin/env python3
"""Read-only, pinned-JAR FTB sidebar icon audit (#507); never launches Minecraft.

Source-only mode verifies the Packwiz lock and searches pack-owned asset
overrides. --mods-dir additionally verifies exact installed FTB JAR hashes and
examines sidebar JSON and direct texture paths across installed client mods.
Symbolic icon names and renderer/shader failures require real client QA.
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
)
DIRECT_TEXTURE = re.compile(r"^([a-z0-9_.-]+):textures/([a-z0-9_./-]+[.]png)$")
SIDEBAR_FILE = re.compile(r"^assets/([a-z0-9_.-]+)/sidebar_buttons/.+[.]json$")
ASSET_FILE = re.compile(r"^assets/[a-z0-9_.-]+/.+")
MAX_SIDEBAR_JSON_BYTES = 512 * 1024


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
        if file_hash(root / rel, entry["hash-format"]) != entry["hash"]:
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
    """Inspect an already read resource index; reusable by synthetic tests."""
    providers: dict[str, list[str]] = {}
    definitions = []
    missing_direct = []
    symbolic = []
    malformed = []
    for provider, resources in sorted(archives.items()):
        for name in resources:
            if ASSET_FILE.fullmatch(name):
                providers.setdefault(name, []).append(provider)
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
            unresolved = []
            for icon in icons:
                match = DIRECT_TEXTURE.fullmatch(icon)
                if match:
                    resource = f"assets/{match.group(1)}/textures/{match.group(2)}"
                    direct.append(resource)
                    if resource not in providers:
                        missing_direct.append({"provider": provider, "path": name, "missing": resource})
                else:
                    # e.g. ftblibrary:icons/... is a renderer-specific symbolic
                    # icon, not a literal PNG; do not label it as a missing file.
                    unresolved.append(icon)
            if unresolved:
                symbolic.append({"provider": provider, "path": name, "icons": unresolved})
            definitions.append({"provider": provider, "path": name, "direct_textures": direct, "symbolic_count": len(unresolved)})
    duplicates = [
        {"path": name, "providers": owners}
        for name, owners in sorted(providers.items())
        if len(owners) > 1 and ("sidebar_buttons/" in name or "/textures/icons/" in name)
    ]
    return {
        "sidebar_definitions": definitions,
        "missing_direct_textures": missing_direct,
        "symbolic_icons_needing_runtime_resolution": symbolic,
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
                    if SIDEBAR_FILE.fullmatch(name):
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
                "Symbolic FTB icon IDs are not literal file paths and are not declared broken by this audit.",
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
        if args.mods_dir and (report["missing_direct_textures"] or report["malformed_definitions"]):
            return 1
        return 0
    except (OSError, ValueError, KeyError, TypeError, tomllib.TOMLDecodeError) as exc:
        print(f"FTB SIDEBAR AUDIT FAIL: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
