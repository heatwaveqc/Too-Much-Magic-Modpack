#!/usr/bin/env python3
"""Assemble the disposable full-integration pack from explicitly owned source paths.

Does not blindly merge old feature branch manifests or overwrite newer quest fixes.
Run in a clean test/full-integration checkout with origin feature refs fetched.
"""
from __future__ import annotations
import hashlib
import json
import re
import subprocess
import sys
import tomllib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CONFIG = ROOT / "manifest/integration-sources.json"
RECORD = ROOT / "manifest/integration-lock.json"
TRACKED_PREFIXES = ("feature/", "fix/", "design/", "research/", "audit/")
PACK_DIRS = ("config", "defaultconfigs", "kubejs", "mods")

def git(*args: str, check: bool = True) -> str:
    cp = subprocess.run(["git", *args], cwd=ROOT, capture_output=True, text=True)
    if check and cp.returncode:
        raise RuntimeError("git " + " ".join(args) + "\n" + cp.stdout + cp.stderr)
    return cp.stdout.strip()

def blob(ref: str, path: str) -> str:
    return git("show", ref + ":" + path)

def sha256(path: Path) -> str:
    return hashlib.sha256(path.read_bytes()).hexdigest()

def refresh_packwiz_index() -> None:
    # A source-only first-party module must NEVER accidentally be shipped by packwiz.
    # We include only actual in-instance config, KubeJS and pinned metadata.
    old = tomllib.loads((ROOT / "index.toml").read_text(encoding="utf-8"))
    assert old["hash-format"] == "sha256"
    for entry in old["files"]:
        if not entry["file"].startswith(tuple(d + "/" for d in PACK_DIRS)):
            raise RuntimeError("Index contains unexpected directory: " + entry["file"])
    paths = []
    for dirname in PACK_DIRS:
        for path in (ROOT / dirname).rglob("*"):
            if not path.is_file() or path.name.startswith("."):
                continue
            if path.suffix.lower() in (".jar", ".zip", ".mrpack"):
                raise RuntimeError("Refusing to index binary distribution: " + str(path))
            paths.append(path.relative_to(ROOT).as_posix())
    entries = sorted(set(paths))
    parts = ['hash-format = "sha256"', ""]
    for name in entries:
        parts.extend(["[[files]]", 'file = "' + name + '"',
                      'hash = "' + sha256(ROOT / name) + '"'])
        if name.startswith("mods/"):
            if not name.endswith(".pw.toml"):
                raise RuntimeError("Invalid mod file: " + name)
            parts.append("metafile = true")
        parts.append("")
    (ROOT / "index.toml").write_text("\n".join(parts), encoding="utf-8")
    pack = ROOT / "pack.toml"
    content = pack.read_text(encoding="utf-8")
    content, count = re.subn(r'(?m)^version = "[^"]+"$', 'version = "0.9.0-dev-integration"', content)
    if count != 1:
        raise RuntimeError("Could not find exactly one pack version")
    content, count = re.subn(r'(?m)^hash = "[0-9a-f]{64}"$', 'hash = "' + sha256(ROOT / "index.toml") + '"', content)
    if count != 1:
        raise RuntimeError("Could not replace exactly one index hash")
    pack.write_text(content, encoding="utf-8")

def file_names(ref: str, selector: str) -> list[str]:
    return git("ls-tree", "-r", "--name-only", ref, "--", selector).splitlines()

def main() -> None:
    cfg = json.loads(CONFIG.read_text(encoding="utf-8"))
    if cfg.get("schema") != 1 or cfg.get("channel") != "integration":
        raise RuntimeError("Unknown source manifest schema/channel")
    if git("branch", "--show-current") != "test/full-integration":
        raise RuntimeError("Run only on test/full-integration")
    if git("status", "--porcelain"):
        raise RuntimeError("Integration checkout is not clean")
    base_ref = "refs/remotes/origin/" + cfg["baseline"]
    git("rev-parse", "--verify", base_ref)
    # Integrate later baseline quest/economy corrections before importing owned paths.
    # A real merge conflict MUST stop the build, rather than clobbering the change.
    git("merge", "--no-edit", base_ref)
    sources = {s["branch"] for s in cfg["streams"]}
    covered = set(cfg["covered_by_baseline"]) | set(cfg["superseded_or_non_runtime"])
    known = sources | covered
    remote = git("for-each-ref", "--format=%(refname:short)", "refs/remotes/origin").splitlines()
    unexpected = sorted(name[7:] for name in remote
                        if name.startswith(tuple("origin/" + prefix for prefix in TRACKED_PREFIXES))
                        and name[7:] not in known)
    if unexpected:
        raise RuntimeError("New working branches require explicit integration classification: " + ", ".join(unexpected))
    imported: dict[str, str] = {}
    report = []
    manifest = json.loads((ROOT / "manifest/mods.json").read_text(encoding="utf-8"))
    items = {m["id"]: m for m in manifest["mods"]}
    for stream in cfg["streams"]:
        branch = stream["branch"]
        ref = "refs/remotes/origin/" + branch
        source_sha = git("rev-parse", "--verify", ref)
        selected = []
        for selector in stream["paths"] + stream.get("optional_paths", []):
            found = file_names(ref, selector)
            if not found and selector not in stream.get("optional_paths", []):
                raise RuntimeError("Source selection missing: " + branch + " " + selector)
            for path in found:
                if path in imported and imported[path] != branch:
                    raise RuntimeError("Workstream path overlap: " + path)
                if not path.startswith(("compat/", "integration/", "kubejs/", "config/", "defaultconfigs/", "mods/", "manifest/", "scripts/")):
                    raise RuntimeError("Unsafe imported source location: " + path)
                imported[path] = branch
                selected.append(path)
        selected = sorted(set(selected))
        # Never restore whole directories: removed baseline files must not be lost.
        for path in selected:
            git("restore", "--source=" + ref, "--staged", "--worktree", "--", path)
        for mod_id in stream.get("manifest_mod_ids", []) + stream.get("manifest_mod_ids_if_available", []):
            donor = json.loads(blob(ref, "manifest/mods.json"))
            found = [m for m in donor["mods"] if m["id"] == mod_id]
            optional = mod_id in stream.get("manifest_mod_ids_if_available", [])
            if not found and optional and not any(p.startswith("mods/") for p in selected):
                continue  # Feature source exists, but provider pin has not been resolved upstream.
            if len(found) != 1:
                raise RuntimeError("Missing/duplicate approved manifest entry " + mod_id)
            if optional and not any(p.startswith("mods/") for p in selected):
                raise RuntimeError("Approved runtime mod exists without explicitly imported pinned metadata: " + mod_id)
            item = found[0]
            if not item["enabled_in_pack"] and not item.get("desired", False):
                raise RuntimeError("Refusing an inactive, unapproved manifest entry: " + mod_id)
            if mod_id in items and items[mod_id] != item:
                raise RuntimeError("Conflicting version/metadata for " + mod_id)
            if mod_id not in items:
                manifest["mods"].append(item)
                items[mod_id] = item
        report.append({"name": stream["name"], "branch": branch, "sha": source_sha,
                       "mode": stream["mode"], "file_count": len(selected), "files": selected,
                       "note": stream["note"]})
    (ROOT / "manifest/mods.json").write_text(json.dumps(manifest, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    refresh_packwiz_index()
    mod_count = sum(m["enabled_in_pack"] for m in manifest["mods"])
    lock = {
        "schema": 1, "channel": "INTEGRATION-NOT-STABLE",
        "baseline": cfg["baseline"], "baseline_sha": git("rev-parse", base_ref),
        "workstreams": report, "covered_by_baseline": cfg["covered_by_baseline"],
        "superseded_or_non_runtime": cfg["superseded_or_non_runtime"],
        "pending_runtime_work": cfg["pending_runtime_work"],
        "pinned_mod_count": mod_count,
        "pack_sha256": sha256(ROOT / "pack.toml"),
        "client_tested": False, "stable_promoted": False
    }
    RECORD.write_text(json.dumps(lock, indent=2) + "\n", encoding="utf-8")
    print(json.dumps({"status": "ASSEMBLED; NOT GAMEPLAY-TESTED",
                      "workstreams": {s["name"]: s["sha"] for s in report},
                      "mods": mod_count, "lock": str(RECORD.relative_to(ROOT))}, indent=2))
if __name__ == "__main__":
    try:
        main()
    except (RuntimeError, ValueError, KeyError, OSError, subprocess.CalledProcessError) as exc:
        print("FULL INTEGRATION BLOCKED:", exc, file=sys.stderr)
        sys.exit(1)
