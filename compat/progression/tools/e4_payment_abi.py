"""Read-only local ABI discovery for the three remaining E4 optional mods.
Usage: python compat/progression/tools/e4_payment_abi.py --mods PATH_TO_INSTALLED_MODS
Requires JDK 21 javap; no downloads, game launch, decompilation artifacts or pack edits.
Prints hash-matched class names, descriptors and relevant JVM invocation lines only.
"""
import argparse
import hashlib
import pathlib
import re
import subprocess
import sys
import zipfile

TARGETS = {
    "mahou": (
        "mahoutsukai-1.21.1-v1.36.31.jar",
        "4022fb3cee32b28df02994cc4c5b40069651583516f6abd04775789616739aad",
        ("drainMana", "chargeCircuit", "getStoredMana", "getCurrentMana", "getMana", "updateManaCounter"),
        ("PlayerManaManager", "dataattachments/mahou/Mahou", "AttunedGem", "ManaCircuit"),
        ("drainMana", "chargeCircuit"),
    ),
    "jawr": (
        "witchery-0.5.39+mc1.21.1.jar",
        "81557ceb1b878d2aa8df5fcbcc468b82479d1e38d38097873a6ffb225324e256",
        ("decreaseBlood", "useBlood", "increaseBlood", "tickBloodRegen", "BloodPoolAttachment", "getBloodPool"),
        ("BloodPoolHandler", "BloodPoolAttachment", "VampireAbility", "WerewolfAbility"),
        ("decreaseBlood", "useBlood"),
    ),
    "pastel": (
        "Pastel-1.1.5.5.jar",
        "33122d9a6bdeeb025d1a2a1210d035104da23c213750d13b3d10af55716a443a",
        ("Ink", "ink", "tryDrainEnergy", "drainEnergy", "extractEnergy", "consumeEnergy"),
        ("ink", "palette", "Ink", "Palette"),
        ("tryDrainEnergy", "drainEnergy", "extractInk", "consumeInk", "drainInk", "removeInk", "withdrawInk", "InkPowered"),
    ),
}

def scan(jar, target, javap):
    filename, expected, terms, name_terms, caller_terms = TARGETS[target]
    if not jar.is_file():
        raise FileNotFoundError(str(jar))
    with jar.open("rb") as stream:
        actual = hashlib.file_digest(stream, "sha256").hexdigest()
    if actual != expected:
        raise ValueError(f"{filename}: expected {expected}, got {actual}; refusing mismatched ABI")
    print(f"\n## {target} {filename} sha256={actual}", flush=True)
    with zipfile.ZipFile(jar) as z:
        classes = []
        for entry in sorted(z.namelist()):
            if not entry.endswith(".class") or entry.startswith("META-INF/") or entry == "module-info.class":
                continue
            name = entry[:-6]
            raw = z.read(entry)
            if any(t.lower() in name.lower() for t in name_terms) or any(t.encode() in raw for t in caller_terms):
                classes.append(name.replace("/", "."))
    print(f"Candidate classes: {len(classes)}", flush=True)
    for clazz in classes:
        print(f"CANDIDATE {clazz}")
    # javap on bounded batches avoids per-class startup overhead. No extracted bytecode saved.
    for i in range(0, len(classes), 24):
        proc = subprocess.run([javap, "-classpath", str(jar), "-p", "-s", "-c", *classes[i:i+24]],
                              capture_output=True, text=True, timeout=90, check=False)
        if proc.returncode:
            raise RuntimeError(f"javap failed: {proc.stderr[:800]}")
        owner = method = descriptor = ""
        for line in proc.stdout.splitlines():
            if line.startswith("Compiled from"):
                owner = method = descriptor = ""
            if line and not line[0].isspace() and (" class " in line or " interface " in line or " enum " in line):
                owner = line.strip()
            elif line.startswith("  ") and not line.startswith("    ") and line.strip().endswith(";"):
                method, descriptor = line.strip(), ""
            elif line.strip().startswith("descriptor:"):
                descriptor = line.strip()
                if any(term.lower() in method.lower() for term in terms):
                    print(f"DECL {owner} | {method} | {descriptor}")
            elif re.search(r"\b(?:invokevirtual|invokestatic|invokeinterface|invokespecial)\b", line):
                if any(term.lower() in line.lower() for term in terms):
                    print(f"CALL {owner} | {method} | {descriptor} | {line.strip()}")
    print(f"END {target}", flush=True)

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--mods", type=pathlib.Path, required=True)
    parser.add_argument("--provider", choices=TARGETS, action="append")
    parser.add_argument("--javap", default="javap")
    args = parser.parse_args()
    failed = False
    for provider in args.provider or TARGETS:
        try:
            scan(args.mods / TARGETS[provider][0], provider, args.javap)
        except (OSError, ValueError, RuntimeError, subprocess.TimeoutExpired, zipfile.BadZipFile) as exc:
            failed = True
            print(f"BLOCKED {provider}: {exc}", file=sys.stderr)
    return int(failed)

if __name__ == "__main__":
    raise SystemExit(main())
