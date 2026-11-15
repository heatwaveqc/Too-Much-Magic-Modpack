#!/usr/bin/env python3
"""Read-only inventory of the pinned Mahou Catalyst Exchange native code.

The old KubeJS compatibility hooks run for every dropped item each tick.
Use this source-verified disassembly to design an injection scoped to the
Catalyst Exchange's own processing method. Never ship a global entity-tick
bridge as a shortcut or rewrite Mahou's native mana economy.
"""
from __future__ import annotations
import hashlib
import json
import re
import subprocess
import sys
import zipfile
from pathlib import Path

MAHOU_SHA512 = "0c4f3bc823d681ad9cb00895d348d2b414201c4d397ca3aa6427b45b8d2b369e0bae8c1f354a9541e4205dadb58c49203d3570e2433d772c3eef1227bc8bfa5a"
EXPECTED = "mahoutsukai-1.21.1-v1.36.31.jar"
HINTS = ("CatalystExchangeMahoujinTileEntity", "PowderedCatalyst")
RELEVANT = re.compile(r"(checkForItemClass|ifItemFoundAbove|tick|ItemEntity|PowderedCatalyst|instanceof|spawn|mana|shrink|setCount|getItem|checkFor)", re.I)

def main(jar: Path) -> None:
    assert jar.is_file() and jar.name == EXPECTED, "Use exact pinned Mahou 1.36.31 NeoForge JAR"
    assert hashlib.sha512(jar.read_bytes()).hexdigest() == MAHOU_SHA512, "Pinned Mahou JAR hash changed"
    with zipfile.ZipFile(jar) as z:
        classes = [name[:-6].replace("/", ".") for name in z.namelist() if name.endswith(".class")]
    targets = [name for name in classes if any(t.lower() in name.lower() for t in HINTS)
               and "$" not in name]
    assert any("CatalystExchangeMahoujinTileEntity" in name for name in targets), "Expected Catalyst Exchange bytecode missing"
    print("MAHOU_NATIVE_AUDIT classes=" + json.dumps(targets[:35]))
    for name in targets[:8]:
        p = subprocess.run(["javap", "-p", "-c", "-classpath", str(jar), name],
                           capture_output=True, text=True, check=True, timeout=25)
        lines = p.stdout.splitlines()
        # Leave method declarations + a surrounding window for the known
        # circle scanner and hard-coded PowderedCatalyst eligibility.
        keep = set(range(min(25, len(lines))))
        for i, line in enumerate(lines):
            if RELEVANT.search(line):
                keep.update(range(max(0, i - 4), min(len(lines), i + 7)))
        out = "\n".join(f"{i+1:04d} {lines[i]}" for i in sorted(keep))
        print("MAHOU_NATIVE_AUDIT_BEGIN " + name)
        print(out[:38000])
        print("MAHOU_NATIVE_AUDIT_END " + name)
    print("PASS: exact pinned native Mahou Catalyst Exchange disassembly inventoried; NO MODIFICATION")

if __name__ == "__main__":
    if len(sys.argv) != 2:
        raise SystemExit("Usage: audit_mahou_catalyst_bytecode.py /path/to/mahoutsukai-1.21.1-v1.36.31.jar")
    main(Path(sys.argv[1]))
