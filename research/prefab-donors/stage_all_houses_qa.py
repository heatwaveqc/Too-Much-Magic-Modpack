#!/usr/bin/env python3
"""Rebuild all 18 approved Prefab Custom Addon previews for disposable QA only.

This is a read-only verifier by default. It never grants a free house or
packages a live survival instance. Python and Node.js standard libraries only.
"""
from __future__ import annotations

import argparse
import gzip
import hashlib
import json
import os
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path

HERE = Path(__file__).resolve().parent
CATALOG = HERE / "houses-qa-v1.json"
SOURCE_FOLDER = "design"
MAX_NBT_BYTES = 8 * 1024 * 1024
DISPOSABLE_MARKER = ".tmm-disposable-qa"


def require_safe_filename(value: str, suffix: str) -> str:
    if not isinstance(value, str) or not value.endswith(suffix):
        raise ValueError(f"Unsupported filename: {value!r}")
    if Path(value).name != value or value in {".", ".."} or "/" in value or "\\" in value:
        raise ValueError(f"Unsafe filename: {value!r}")
    return value


def load_catalog() -> tuple[dict, list[dict]]:
    obj = json.loads(CATALOG.read_text(encoding="utf-8"))
    if obj.get("schema") != 1 or obj.get("status") != "qa_preview_only":
        raise ValueError("Refusing non-QA catalog")
    homes = obj.get("custom_houses")
    if not isinstance(homes, list) or len(homes) != 18:
        raise ValueError("Exactly 18 custom homes required")
    ids, filenames = set(), set()
    for h in homes:
        name = require_safe_filename(h.get("filename"), ".nbt")
        source = h.get("source_dir")
        if not (isinstance(source, str) and source.endswith(("-v1", "-v2")) and Path(source).name == source and ".." not in source):
            raise ValueError(f"Unsafe source directory: {source!r}")
        generator = h.get("generator")
        if not (isinstance(generator, str) and Path(generator).name == generator
                and generator.endswith((".py", ".mjs"))):
            raise ValueError(f"Unsafe generator: {generator!r}")
        if h.get("grant") is not False or h.get("addon_required") is not True:
            raise ValueError(f"Unexpected entitlement in {h.get('id')}")
        if h["id"] in ids or name in filenames:
            raise ValueError("Duplicate home ID or NBT filename")
        ids.add(h["id"])
        filenames.add(name)
    if obj.get("native_generic", {}).get("grant") is not False:
        raise ValueError("Generic must not be granted by this tool")
    return obj, homes


def build_previews(homes: list[dict], work: Path) -> list[dict]:
    results = []
    for house in homes:
        design_dir = work / SOURCE_FOLDER / house["source_dir"]
        generator = design_dir / house["generator"]
        if not generator.is_file():
            raise FileNotFoundError(generator)
        cmd = ([sys.executable, str(generator)] if generator.suffix == ".py"
               else ["node", str(generator)])
        try:
            run = subprocess.run(cmd, cwd=design_dir, capture_output=True,
                                 text=True, check=True, timeout=120)
        except subprocess.CalledProcessError as exc:
            raise RuntimeError(
                f"{house['id']}: generator failed ({exc.returncode})\\n"
                + (exc.stdout or "")[-1800:] + (exc.stderr or "")[-1800:]
            ) from exc
        except subprocess.TimeoutExpired as exc:
            raise RuntimeError(f"{house['id']}: generator timed out") from exc

        # Apply the owner-approved Adventurer's Inn Bountiful office board
        # only AFTER the original hash-verified structure has been rebuilt.
        # This overlay checks the exact original raw NBT digest and fails
        # closed on source drift; no double application is allowed.
        if house["id"] == "adventurers_inn":
            overlay = design_dir / "add_bountiful_board.py"
            if not overlay.is_file():
                raise FileNotFoundError("Missing Bountiful Inn overlay")
            result = subprocess.run(
                [sys.executable, str(overlay)], cwd=design_dir,
                capture_output=True, text=True, check=True, timeout=120,
            )
            print(result.stdout.strip(), file=sys.stderr)

        nbt = design_dir / "prefab-extension" / house["filename"]
        if not nbt.is_file():
            raise FileNotFoundError(f"{house['id']}: expected output missing: {nbt}")
        data = nbt.read_bytes()
        if len(data) > MAX_NBT_BYTES or len(data) < 100:
            raise ValueError(f"{house['id']}: invalid NBT size {len(data)}")
        raw = gzip.decompress(data)
        if len(raw) < 100 or raw[:3] != b"\x0a\x00\x00":
            raise ValueError(f"{house['id']}: invalid vanilla structure NBT root")
        if b"minecraft:command_block" in raw or b"minecraft:structure_block" in raw:
            raise ValueError(f"{house['id']}: prohibited structure/command block")
        results.append({
            "id": house["id"],
            "filename": house["filename"],
            "source_dir": house["source_dir"],
            "compressed_bytes": len(data),
            "sha256": hashlib.sha256(data).hexdigest(),
            "raw_sha256": hashlib.sha256(raw).hexdigest(),
            "_nbt_path": nbt,
        })
        print(f"verified {house['id']}: {len(data)} bytes; "
              f"sha256 {results[-1]['sha256']}", file=sys.stderr)
    return results


def stage_qa(results: list[dict], destination: Path, target: str) -> None:
    if not destination.is_dir() or not (destination / DISPOSABLE_MARKER).is_file():
        raise ValueError(
            f"Refusing unmarked/nonexistent QA directory: {destination}. "
            f"An owner must create {DISPOSABLE_MARKER} in a DISPOSABLE game directory."
        )
    if destination.is_symlink() or destination == Path("/"):
        raise ValueError("Refusing unsafe destination")
    roots = []
    if target in {"client", "singleplayer"}:
        roots.append(destination / "prefab-extension")
    if target in {"server", "singleplayer"}:
        roots.append(destination / "prefab-extension" / "server-cache")
    # All-or-nothing conflict preflight: never overwrite an existing blueprint.
    paths = [(r / entry["filename"], entry["_nbt_path"]) for r in roots for entry in results]
    for dest, src in paths:
        if dest.is_symlink() or (dest.exists() and dest.read_bytes() != src.read_bytes()):
            raise FileExistsError(f"Refusing to replace existing or symlinked NBT: {dest}")
    for dest, src in paths:
        dest.parent.mkdir(parents=True, exist_ok=True)
        if not dest.exists():
            temp_dest = dest.with_name(dest.name + ".tmm-temporary")
            try:
                with temp_dest.open("xb") as fh:
                    fh.write(src.read_bytes())
                os.replace(temp_dest, dest)
            finally:
                if temp_dest.exists():
                    temp_dest.unlink()
    print(f"Staged {len(results)} previews to {target} in disposable {destination}", file=sys.stderr)


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--verify-only", action="store_true",
                        help="Generate and check all previews without copying to a game directory")
    parser.add_argument("--stage-qa-game-dir", type=Path,
                        help="Explicitly stage into a pre-marked disposable QA instance")
    parser.add_argument("--target", choices=("client", "server", "singleplayer"),
                        help="Required only when staging: client root, server-cache, or both")
    args = parser.parse_args()
    if bool(args.stage_qa_game_dir) == bool(args.verify_only):
        parser.error("Select exactly one of --verify-only or --stage-qa-game-dir")
    if args.stage_qa_game_dir and not args.target:
        parser.error("--target is required for --stage-qa-game-dir")
    if args.verify_only and args.target:
        parser.error("--target is not meaningful in --verify-only mode")

    _, homes = load_catalog()
    with tempfile.TemporaryDirectory(prefix="tmm-prefab-qa-") as workdir:
        work = Path(workdir) / "research" / "prefab-donors"
        shutil.copytree(HERE, work, ignore=shutil.ignore_patterns(
            "prefab-extension", "__pycache__", "local-audit.json"))
        results = build_previews(homes, work)
        if args.stage_qa_game_dir:
            stage_qa(results, args.stage_qa_game_dir.resolve(), args.target)
        report = {
            "status": "STATIC_QA_PREVIEW_ONLY",
            "generated_houses": len(results),
            "native_generic_separate": True,
            "no_free_grants": True,
            "note": "Source/NBT validation only; not Minecraft startup or placement acceptance",
            "houses": [{k: v for k, v in result.items() if not k.startswith("_")}
                       for result in results],
        }
        print(json.dumps(report, indent=2))


if __name__ == "__main__":
    main()
