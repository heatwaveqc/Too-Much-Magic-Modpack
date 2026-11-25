#!/usr/bin/env python3
"""Audit selected built first-party mod metadata against hash-pinned provider JARs.

Source-only CI gate: no Minecraft launch, pack assembly, or JAR redistribution.
Maven version-range *semantics* are evaluated by the companion Java checker,
using org.apache.maven.artifact.versioning.VersionRange, not Python sorting.
"""
from __future__ import annotations

import argparse
import hashlib
import json
from pathlib import Path
import re
import tomllib
import zipfile

ROOT = Path(__file__).resolve().parents[1]

# Explicit native mod ID -> selected Packwiz slug mapping. A new external
# dependency must be reviewed and registered rather than silently skipped.
PROVIDERS = {
    "projecte": "projecte",
    "apotheosis": "apotheosis",
    "malum": "malum",
    "botania": "botania-revived",
    "evilcraft": "evilcraft",
    "apothic_spawners": "apothic-spawners",
    "ars_nouveau": "ars-nouveau",
    "draconicevolution": "draconic-evolution",
    "neovitae": "neovitae",
    "theurgy": "theurgy",
    "forbidden_arcanus": "forbidden-arcanus",
    "embers": "embers-re-ignited",
    "goety": "goety",
    "primalmagick": "primal-magick",
    "astralsorcery": "astral-sorcery",
    "pastel": "pastel",
    "irons_spellbooks": "irons-spells-n-spellbooks",
    "projectexpansion": "project-expansion",
    "projecte_energy": "projecte-energy-converters",
    "jade": "jade",
    "titanium": "titanium",
}
VALID_VERSION = re.compile(r"^[0-9A-Za-z][0-9A-Za-z.+_-]*$")
ID = re.compile(r"^[a-z][a-z0-9_]*$")
MANIFEST_VERSION = "$" + "{file.jarVersion}"


class ClosureError(ValueError):
    pass


def digest(path: Path, algorithm: str) -> str:
    if algorithm not in ("sha1", "sha256", "sha512"):
        raise ClosureError(f"Unsupported pinned hash algorithm: {algorithm}")
    with path.open("rb") as stream:
        return hashlib.file_digest(stream, algorithm).hexdigest()


def manifest_main(raw: bytes) -> dict[str, str]:
    """Read only the main JAR manifest section, including continued values."""
    result: dict[str, str] = {}
    previous = None
    for line in raw.decode("utf-8").replace("\r\n", "\n").split("\n"):
        if not line:
            break
        if line.startswith(" "):
            if previous is None:
                raise ClosureError("Invalid JAR manifest continuation")
            result[previous] += line[1:]
        else:
            if ": " not in line:
                raise ClosureError("Malformed JAR manifest header")
            previous, value = line.split(": ", 1)
            result[previous] = value
    return result


def jar_metadata(path: Path) -> dict:
    if not path.is_file() or not zipfile.is_zipfile(path):
        raise ClosureError(f"Missing or invalid JAR: {path}")
    with zipfile.ZipFile(path) as z:
        names = z.namelist()
        locations = [p for p in ("META-INF/neoforge.mods.toml", "META-INF/mods.toml")
                     if p in names]
        if len(locations) != 1 or names.count(locations[0]) != 1:
            raise ClosureError(f"Missing or duplicate NeoForge mod metadata: {path.name}")
        try:
            data = tomllib.loads(z.read(locations[0]).decode("utf-8"))
        except (tomllib.TOMLDecodeError, UnicodeError) as exc:
            raise ClosureError(f"Unparseable mod metadata: {path.name}") from exc
        mods = data.get("mods", [])
        if not isinstance(mods, list) or not mods:
            raise ClosureError(f"JAR declares no native mod IDs: {path.name}")
        if any(m.get("version") == MANIFEST_VERSION for m in mods):
            if names.count("META-INF/MANIFEST.MF") != 1:
                raise ClosureError(f"JAR version needs unambiguous manifest: {path.name}")
            version = manifest_main(z.read("META-INF/MANIFEST.MF")).get("Implementation-Version")
        else:
            version = None
        ids = set()
        for mod in mods:
            mod_id, value = mod.get("modId"), mod.get("version")
            if not isinstance(mod_id, str) or not ID.fullmatch(mod_id) or mod_id in ids:
                raise ClosureError(f"Invalid/duplicate native mod ID in {path.name}: {mod_id}")
            ids.add(mod_id)
            if value == MANIFEST_VERSION:
                value = version
            if not isinstance(value, str) or not VALID_VERSION.fullmatch(value):
                raise ClosureError(f"Unresolved or invalid native version for {mod_id} in {path.name}: {value}")
            mod["_effective_version"] = value
        return data


def pinned_paths(root: Path) -> dict[str, Path]:
    pack = tomllib.loads((root / "pack.toml").read_text(encoding="utf-8"))
    index_file = root / pack["index"]["file"]
    if (pack["index"]["file"] != "index.toml"
            or pack["index"]["hash-format"] != "sha256"
            or digest(index_file, "sha256") != pack["index"]["hash"]):
        raise ClosureError("Packwiz root index hash mismatch")
    index = tomllib.loads(index_file.read_text(encoding="utf-8"))
    if index.get("hash-format") != "sha256":
        raise ClosureError("Unsupported Packwiz metafile index hash format")
    paths = {}
    for row in index["files"]:
        path = row["file"]
        if not path.startswith("mods/") or not path.endswith(".pw.toml"):
            continue
        slug = Path(path).name.removesuffix(".pw.toml")
        if slug in paths or not row.get("metafile"):
            raise ClosureError(f"Duplicate/non-metafile selected mod: {slug}")
        metadata = root / path
        if digest(metadata, "sha256") != row["hash"]:
            raise ClosureError(f"Indexed mod pin has changed: {path}")
        paths[slug] = metadata
    return paths


def load_pin(path: Path, provider_id: str, jar_dir: Path) -> tuple[Path, str]:
    pin = tomllib.loads(path.read_text(encoding="utf-8"))
    name = pin.get("filename")
    if (not isinstance(name, str) or name != Path(name).name
            or not name.lower().endswith(".jar")):
        raise ClosureError(f"Unsafe pinned JAR filename for {provider_id}: {name}")
    side = pin.get("side", "both")
    if side not in ("both", "client", "server"):
        raise ClosureError(f"Invalid selected provider side for {provider_id}: {side}")
    jar = jar_dir / name
    if not jar.is_file():
        raise ClosureError(f"Selected {provider_id} is missing from verified compile providers: {name}")
    lock = pin["download"]
    if digest(jar, lock["hash-format"]) != lock["hash"].lower():
        raise ClosureError(f"Wrong pinned bytes for {provider_id}: {name}")
    return jar, side


def load_selected_modules(root: Path) -> dict[str, dict]:
    selected = json.loads((root / "manifest/first-party-runtime.json").read_text(encoding="utf-8"))
    if selected.get("schema") != 1 or not selected.get("modules"):
        raise ClosureError("Invalid selected first-party manifest")
    result = {}
    for row in selected["modules"]:
        mod_id = row["mod_id"]
        directory = root / row["directory"]
        if not ID.fullmatch(mod_id) or mod_id in result or not directory.is_relative_to(root / "compat"):
            raise ClosureError(f"Invalid/duplicate selected first-party module: {mod_id}")
        props = dict(line.split("=", 1) for line in (directory / "gradle.properties").read_text().splitlines()
                     if "=" in line and not line.startswith("#"))
        if props.get("mod_id") != mod_id:
            raise ClosureError(f"Gradle ID diverges from selected first-party module: {mod_id}")
        jar = directory / "build/libs" / (mod_id + "-" + props["mod_version"] + ".jar")
        data = jar_metadata(jar)
        mods = data["mods"]
        if len(mods) != 1 or mods[0]["modId"] != mod_id:
            raise ClosureError(f"Wrong compiled first-party mod identity: {mod_id}")
        result[mod_id] = data
    return result


def collect(root: Path, provider_dir: Path) -> list[tuple[str, str, str, str, str, str]]:
    """Return (consumer, side, dependency, type, range, native_version) pairs.

    Unknown external IDs are hard failures, even when optional. This protects
    against accidental omission of a newly added optional-but-pinned provider.
    """
    pins = pinned_paths(root)
    modules = load_selected_modules(root)
    pack = tomllib.loads((root / "pack.toml").read_text(encoding="utf-8"))
    builtin = {"minecraft": pack["versions"]["minecraft"],
               "neoforge": pack["versions"]["neoforge"]}
    cache: dict[str, tuple[str, str]] = {}
    pairs = []
    for consumer, metadata in modules.items():
        for row in metadata.get("dependencies", {}).get(consumer, []):
            dep = row.get("modId")
            kind = row.get("type", "required")
            side = row.get("side", "BOTH")
            spec = row.get("versionRange")
            if (not isinstance(dep, str) or not ID.fullmatch(dep)
                    or kind not in ("required", "optional")
                    or side not in ("BOTH", "CLIENT", "SERVER")
                    or not isinstance(spec, str) or not spec
                    or "\t" in spec or "\n" in spec):
                raise ClosureError(f"Unsupported dependency declaration: {consumer} -> {dep}")
            if dep in builtin:
                provider_version, provider_side = builtin[dep], "both"
            elif dep in modules:
                provider_version = next(m["_effective_version"] for m in modules[dep]["mods"]
                                        if m["modId"] == dep)
                provider_side = "both"
            else:
                if dep not in PROVIDERS:
                    raise ClosureError(f"Unmapped first-party dependency {consumer} -> {dep}: review pin ownership")
                slug = PROVIDERS[dep]
                if slug not in pins:
                    if kind == "required":
                        raise ClosureError(f"Required provider is not selected in Packwiz: {consumer} -> {dep}")
                    continue
                if dep not in cache:
                    jar, provider_side = load_pin(pins[slug], dep, provider_dir)
                    matches = [m["_effective_version"] for m in jar_metadata(jar)["mods"]
                               if m["modId"] == dep]
                    if len(matches) != 1:
                        raise ClosureError(f"Pinned {slug} does not supply required native mod ID {dep}")
                    cache[dep] = (matches[0], provider_side)
                provider_version, provider_side = cache[dep]
            for check_side in (("client", "server") if side == "BOTH" else (side.lower(),)):
                if provider_side != "both" and provider_side != check_side:
                    if kind == "required":
                        raise ClosureError(
                            f"Required {dep} is not installed on {check_side} for {consumer}")
                    continue
                pairs.append((consumer, check_side, dep, kind, spec, provider_version))
    if not pairs:
        raise ClosureError("No selected first-party dependency constraints were audited")
    return sorted(pairs)


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--root", type=Path, default=ROOT)
    parser.add_argument("--providers-dir", type=Path, required=True,
                        help="Already downloaded, hash-verified provider JAR directory")
    parser.add_argument("--pairs-out", type=Path, required=True)
    args = parser.parse_args()
    pairs = collect(args.root.resolve(), args.providers_dir.resolve())
    lines = ["\t".join(p) for p in pairs]
    args.pairs_out.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"PASS: prepared {len(pairs)} native first-party dependency checks "
          "with verified Packwiz source and provider JAR hashes; "
          "Maven version evaluation is required next.")


if __name__ == "__main__":
    main()
