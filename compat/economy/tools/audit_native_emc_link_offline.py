#!/usr/bin/env python3
"""Type A pinned-binary audit of Project Expansion's owner-account access.

Reads only the exact hash-verified installed JAR and javap bytecode. No mod
classes are executed, no game/server boot and no persistence modification.
"""
from __future__ import annotations

import argparse
import hashlib
import re
import subprocess
import sys
import tomllib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[3]
PIN = ROOT / "mods/project-expansion.pw.toml"
UTIL = "cool.furry.mc.neoforge.projectexpansion.util.Util"
LINK = "cool.furry.mc.neoforge.projectexpansion.block.entity.BlockEntityEMCLink"


def method_body(disassembly: str, method: str, param: str) -> str:
    expression = (r"(?m)^\s+public static .*\b" + re.escape(method) +
                  r"\(" + re.escape(param) + r"\);\s*$")
    found = re.search(expression, disassembly)
    if not found:
        raise ValueError(f"No public static {method}({param}) in exact JAR")
    following = disassembly[found.end():]
    marker = re.search(r"(?m)^\s{2,}(?:public|private|protected|static)\s", following)
    body = following[:marker.start()] if marker else following
    if "Code:" not in body:
        raise ValueError(f"{method}: no bytecode body")
    return body


def run_javap(jar: Path, symbol: str) -> str:
    result = subprocess.run(
        ["javap", "-p", "-c", "-classpath", str(jar), symbol],
        text=True, capture_output=True, check=False, timeout=45
    )
    if result.returncode:
        raise ValueError(f"Cannot audit {symbol}: {result.stderr[-600:]}")
    return result.stdout


def verify(jar: Path) -> None:
    pin = tomllib.loads(PIN.read_text(encoding="utf-8"))
    expected = pin["download"]
    if jar.name != pin["filename"] or expected["hash-format"] != "sha512":
        raise ValueError("Wrong Project Expansion JAR / pin metadata")
    actual = hashlib.sha512(jar.read_bytes()).hexdigest()
    if actual != expected["hash"]:
        raise ValueError("Pinned Project Expansion JAR failed SHA-512")
    util = run_javap(jar, UTIL)
    getter = method_body(util, "getKnowledgeProvider", "java.util.UUID")
    get_player = method_body(util, "getPlayer", "java.util.UUID")
    # Exact version 1.0.6 link uses a live ServerPlayer from PlayerList,
    # not a separately persisted offline IKnowledgeProvider.
    if not re.search(r"invokestatic\s+#\d+\s+// Method getPlayer:", getter):
        raise ValueError("getKnowledgeProvider(UUID) no longer resolves getPlayer(UUID)")
    if "aconst_null" not in getter or "areturn" not in getter:
        raise ValueError("getKnowledgeProvider(UUID) no longer has offline-null path")
    if not re.search(r"getCurrentServer:", get_player):
        raise ValueError("getPlayer(UUID) no longer queries current server")
    if not re.search(r"getPlayer:\(Ljava/util/UUID;\)Lnet/minecraft/server/level/ServerPlayer;", get_player):
        raise ValueError("getPlayer(UUID) no longer queries online PlayerList")
    link = run_javap(jar, LINK)
    if "getKnowledgeProvider:" not in link or "getPlayer:" not in link:
        raise ValueError("EMC Link account semantics changed; inspect exact 1.0.6 bytecode")
    print("PASS: pinned Project Expansion 1.0.6 account resolves live UUID -> ServerPlayer")
    print("PASS: provider cannot be resolved from saved UUID alone while owner offline")
    print("TYPE A bytecode audit only; game-world/account behavior still needs authorized B/C QA")


if __name__ == "__main__":
    try:
        parser = argparse.ArgumentParser()
        parser.add_argument("jar", type=Path)
        verify(parser.parse_args().jar)
    except (ValueError, OSError, subprocess.TimeoutExpired, tomllib.TOMLDecodeError) as exc:
        print(f"FAIL: {exc}", file=sys.stderr)
        sys.exit(1)
