#!/usr/bin/env python3
"""Read-only audit of the exact installed energy converter; never execute mod code."""
import hashlib
import pathlib
import subprocess
import sys
import zipfile

EXPECTED = "22e6a476508b9d9d9fa35cbfdecf5677ec5c623c"

def main():
    if len(sys.argv) != 2:
        raise SystemExit("usage: audit-op-converter.py /path/to/projecte-energy-0.1.4.jar")
    jar = pathlib.Path(sys.argv[1]).resolve(strict=True)
    digest = hashlib.sha1(jar.read_bytes()).hexdigest()
    if digest != EXPECTED:
        raise SystemExit(f"wrong pinned jar SHA1: {digest}")
    print("Pinned SHA1 verified:", digest, flush=True)
    with zipfile.ZipFile(jar) as z:
        classes = []
        for name in z.namelist():
            if not name.endswith(".class") or name.startswith("META-INF/"):
                continue
            data = z.read(name)
            if any(key in data for key in (b"emcPerEnergy", b"energyPerEmc", b"basicEmcPerTick")):
                classes.append(name.removesuffix(".class").replace("/", "."))
        print("Ratio-related classes:", *classes, sep="\n", flush=True)
        for name in z.namelist():
            if name.startswith("assets/") and name.endswith("/lang/en_us.json"):
                print("LANG:", name, flush=True)
                print(z.read(name).decode("utf-8"), flush=True)
    if not classes:
        raise SystemExit("No ratio symbols found; inspect jar layout manually")
    for name in sorted(set(classes)):
        print("\nJAVAP:", name, flush=True)
        subprocess.run(["javap", "-classpath", str(jar), "-p", "-c", "-s", name], check=True)

if __name__ == "__main__":
    main()
