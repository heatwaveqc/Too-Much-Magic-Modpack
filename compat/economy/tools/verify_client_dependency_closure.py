#!/usr/bin/env python3
"""Fail-closed Economy/Jade client dependency closure against a pinned provider JAR.

Compare the *compiled* first-party NeoForge metadata to the provider's effective
loader-visible version, not the filename or a human-facing release number.
This audit handles exact Maven [version] ranges only; do not approximate open
Maven version ranges using Python lexical version comparisons.

No Minecraft launch or provider execution.
"""
from __future__ import annotations

import argparse
import hashlib
from pathlib import Path
import re
import tomllib
import zipfile

ROOT = Path(__file__).resolve().parents[3]
PROVIDER = "jade"
CONSUMER = "tmm_economy"
MIXIN_TARGET = "snownee/jade/api/view/EnergyView.class"
EXACT = re.compile(r"^\[([0-9A-Za-z][0-9A-Za-z.+_-]*)\]$")
MANIFEST_VERSION_REF = "$" + "{file.jarVersion}"


class DependencyClosureError(ValueError):
    pass


def manifest_attrs(raw: bytes) -> dict[str, str]:
    """Read the main JAR manifest section, including continuation lines."""
    lines = raw.decode("utf-8", errors="strict").replace("\r\n", "\n").split("\n")
    attrs: dict[str, str] = {}
    current = None
    for line in lines:
        if not line:
            break
        if line.startswith(" "):
            if current is None:
                raise DependencyClosureError("Malformed JAR manifest continuation")
            attrs[current] += line[1:]
            continue
        if ": " not in line:
            raise DependencyClosureError("Malformed JAR manifest header")
        key, val = line.split(": ", 1)
        attrs[key] = val
        current = key
    return attrs


def actual_mod_version(provider: Path, *, check_mixin_target: bool = True) -> str:
    if not provider.is_file() or not zipfile.is_zipfile(provider):
        raise DependencyClosureError("Missing or non-JAR Jade provider")
    with zipfile.ZipFile(provider) as jar:
        try:
            meta = tomllib.loads(jar.read("META-INF/neoforge.mods.toml").decode("utf-8"))
        except (KeyError, tomllib.TOMLDecodeError, UnicodeError) as exc:
            raise DependencyClosureError("Unparseable Jade provider mod metadata") from exc
        versions = [mod.get("version") for mod in meta.get("mods", [])
                    if mod.get("modId") == PROVIDER]
        if len(versions) != 1 or not isinstance(versions[0], str):
            raise DependencyClosureError("Missing or duplicated Jade native version declaration")
        version = versions[0]
        if version == MANIFEST_VERSION_REF:
            try:
                version = manifest_attrs(jar.read("META-INF/MANIFEST.MF")).get("Implementation-Version")
            except (KeyError, UnicodeError) as exc:
                raise DependencyClosureError("Jade declares file.jarVersion without JAR manifest") from exc
        if not isinstance(version, str) or not re.fullmatch(r"[0-9A-Za-z][0-9A-Za-z.+_-]*", version):
            raise DependencyClosureError("Jade effective mod version is missing or unsafe")
        if check_mixin_target and MIXIN_TARGET not in jar.namelist():
            raise DependencyClosureError("Jade is missing Economy's client EnergyView target class")
        return version


def verify_pinned_bytes(provider: Path, pin_path: Path) -> None:
    pin = tomllib.loads(pin_path.read_text(encoding="utf-8"))
    if pin.get("filename") != provider.name or pin.get("side") not in ("both", "client"):
        raise DependencyClosureError("Jade JAR does not match the selected Packwiz filename/side")
    digest = pin.get("download", {})
    algo, value = digest.get("hash-format"), digest.get("hash")
    if algo not in ("sha1", "sha256", "sha512") or not isinstance(value, str):
        raise DependencyClosureError("Unsupported Packwiz Jade digest")
    with provider.open("rb") as stream:
        actual = hashlib.file_digest(stream, algo).hexdigest()
    if actual != value.lower():
        raise DependencyClosureError("Jade provider differs from hash-verified pinned bytes")


def check_client_optional_present(
        dependencies: dict, provider_id: str, version: str | None
) -> str:
    """Exactly one client-optional dependency; if installed, match its exact bound.

    Complex Maven ranges with commas/open bounds are intentionally unsupported
    by this *narrow* audit. We do not claim general Maven ordering semantics.
    """
    rows = [x for x in dependencies.get(CONSUMER, []) if x.get("modId") == provider_id]
    if len(rows) != 1:
        raise DependencyClosureError(f"Expected exactly one {provider_id} dependency entry")
    row = rows[0]
    if (row.get("type"), row.get("side"), row.get("ordering")) != ("optional", "CLIENT", "AFTER"):
        raise DependencyClosureError(f"{provider_id} dependency lost optional/client/AFTER semantics")
    spec = row.get("versionRange")
    bound = EXACT.fullmatch(spec) if isinstance(spec, str) else None
    if not bound:
        raise DependencyClosureError(f"{provider_id} requires reviewed exact Maven [version] range")
    if version is None:
        return "optional-absent"
    if bound.group(1) != version:
        raise DependencyClosureError(
            f"{provider_id} optional but installed: Economy requires {spec}, effective provider version [{version}]"
        )
    return "matched"


def verify(module_jar: Path, provider_jar: Path | None, pin_path: Path | None = None) -> str:
    if not module_jar.is_file() or not zipfile.is_zipfile(module_jar):
        raise DependencyClosureError("Missing or non-JAR first-party Economy artifact")
    with zipfile.ZipFile(module_jar) as jar:
        try:
            meta = tomllib.loads(jar.read("META-INF/neoforge.mods.toml").decode("utf-8"))
        except (KeyError, tomllib.TOMLDecodeError, UnicodeError) as exc:
            raise DependencyClosureError("Unparseable compiled Economy metadata") from exc
        if [mod.get("modId") for mod in meta.get("mods", [])] != [CONSUMER]:
            raise DependencyClosureError("Wrong packaged first-party Economy mod ID")
    if provider_jar is None:
        if pin_path is not None:
            raise DependencyClosureError("Pinned Jade is selected but the provider JAR is missing")
        version = None
    else:
        if pin_path is not None:
            verify_pinned_bytes(provider_jar, pin_path)
        version = actual_mod_version(provider_jar)
    return check_client_optional_present(meta.get("dependencies", {}), PROVIDER, version)


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--module-jar", type=Path, required=True)
    parser.add_argument("--provider-jar", type=Path, required=True)
    parser.add_argument("--pin", type=Path, default=ROOT / "mods/jade.pw.toml")
    args = parser.parse_args()
    result = verify(args.module_jar, args.provider_jar, args.pin)
    if result != "matched":
        raise DependencyClosureError("Selected Jade is present but closure is not confirmed")
    print("PASS: compiled Economy optional CLIENT Jade range matches effective, hash-verified "
          "Implementation-Version from the exact pinned provider JAR")


if __name__ == "__main__":
    main()
