#!/usr/bin/env python3
"""Read-only bytecode inventory of the exact JAWR/Forbidden Magic QA jars.

No bundled JARs or decompiled code are committed. This is only an inspection
aid; it does not prove that consuming a particular food works in-game.
"""
from __future__ import annotations

import argparse
import re
import subprocess
import zipfile
from pathlib import Path

KEYWORDS = re.compile(r"(food|hunger|exhaust|eat|consum|lycan|werewolf|wolf|lich|"
                      r"vampir|blood|soul|undead|mixin|player)", re.I)
FOOD_WORDS = re.compile(
    r"(FoodData|FoodProperties|setFoodLevel|setSaturation|eat|hunger|exhaust|"
    r"Lycan|Werewolf|Lich|Food|Consume|LivingEntityUseItem|PlayerEvent)", re.I
)


def inspect(jar: Path, folder: Path, limit: int) -> str:
    if not jar.is_file():
        raise FileNotFoundError(f"Pinned JAWR JAR missing: {jar}")
    with zipfile.ZipFile(jar) as z:
        all_classes = [
            n[:-6].replace("/", ".") for n in z.namelist()
            if n.endswith(".class") and not n.startswith(("META-INF/", "module-info"))
        ]
        mixin_configs = sorted(n for n in z.namelist() if "mixin" in n.lower()
                               and n.endswith(".json"))
    matches = [c for c in all_classes if KEYWORDS.search(c)]
    def rank(c: str) -> tuple[int, str]:
        short = c.rsplit(".", 1)[-1].lower()
        priority = ("lycan", "werewolf", "lich", "hunger", "food", "eat",
                    "consume", "mixin", "player", "blood", "soul", "vampir")
        return (min((i for i, word in enumerate(priority) if word in short), default=99), c)
    matches.sort(key=rank)
    selected = [c for c in matches if "$" not in c][:limit]
    disassembly = []
    signals = []
    failed = []
    for cls in selected:
        try:
            cmd = ["javap", "-p", "-c", "-classpath", str(jar), cls]
            result = subprocess.run(cmd, capture_output=True, text=True,
                                    timeout=12, check=False)
            if result.returncode:
                failed.append(f"{cls}: {result.stderr[:200].strip()}")
                continue
            disassembly.append(f"//// CLASS {cls}\n{result.stdout}")
            found = [line.strip() for line in result.stdout.splitlines()
                     if FOOD_WORDS.search(line)]
            if found:
                signals.append((cls, found[:12]))
        except (OSError, subprocess.TimeoutExpired) as error:
            failed.append(f"{cls}: {error}")

    label = jar.stem.replace("+", "_")
    (folder / f"{label}.javap.txt").write_text("\n\n".join(disassembly), encoding="utf-8")
    lines = [
        f"## {jar.name}", "",
        f"- Compiled classes: {len(all_classes)}",
        f"- Keyword-matching classes: {len(matches)}",
        f"- Disassembled candidates: {len(disassembly)} of {len(selected)} requested",
        f"- Mixin config files: {', '.join(mixin_configs) if mixin_configs else '(none found)'}",
        "- This is an inspection inventory, NOT a gameplay result.", "",
        "### Candidates", "",
    ]
    lines += [f"- `{x}`" for x in selected]
    lines.extend(["", "### Bytecode clues", ""])
    for cls, found in signals:
        lines.append(f"**{cls}**")
        lines.extend(f"- `{line[:260]}`" for line in found)
    if failed:
        lines.extend(["", "### Decompilation failures", ""])
        lines.extend(f"- {line}" for line in failed)
    return "\n".join(lines)


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("jars", type=Path, nargs="+", help="exact pinned JAWR .jar files")
    parser.add_argument("--output", type=Path, required=True)
    parser.add_argument("--limit", type=int, default=80)
    args = parser.parse_args()
    if args.limit < 1 or args.limit > 250:
        parser.error("--limit must be between 1 and 250")
    args.output.mkdir(parents=True, exist_ok=True)
    parts = [
        "# JAWR / Forbidden Magic hunger and food bytecode inventory",
        "",
        "Inspect raw `.javap.txt` to distinguish actual food-consumption hooks from unrelated class names.",
        "Run disposable-world tests separately for vanilla, modded, lycan and lich states.",
        "",
    ]
    for jar in args.jars:
        parts.append(inspect(jar, args.output, args.limit))
        parts.append("")
    report = args.output / "jawr-food-bytecode-inventory.md"
    report.write_text("\n".join(parts), encoding="utf-8")
    print(f"Read-only JAWR food inventory: {report}")


if __name__ == "__main__":
    main()
