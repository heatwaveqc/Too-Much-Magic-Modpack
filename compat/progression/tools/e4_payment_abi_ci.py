"""Static evidence only. Download public hash-pinned JARs to disposable CI temp,
print only recipient/signature and targeted call-site evidence. No JAR upload."""
from pathlib import Path
import hashlib
import subprocess
import tempfile
import urllib.request
import zipfile
import re

PINS = {
    "mahou": ("https://cdn.modrinth.com/data/7jOxn6qJ/versions/GrYWbBFj/mahoutsukai-1.21.1-v1.36.31.jar",
        "4022fb3cee32b28df02994cc4c5b40069651583516f6abd04775789616739aad"),
    "jawr": ("https://cdn.modrinth.com/data/LCO8KZvC/versions/TWcJbsjg/witchery-0.5.39%2Bmc1.21.1.jar",
        "81557ceb1b878d2aa8df5fcbcc468b82479d1e38d38097873a6ffb225324e256"),
}
NATIVE = {
    "mahou": ["stepsword.mahoutsukai.mana.PlayerManaManager",
              "stepsword.mahoutsukai.dataattachments.mahou.Mahou",
              "stepsword.mahoutsukai.item.attunedgems.AttunedGem"],
    "jawr": ["dev.sterner.witchery.feature.blood_pool.BloodPoolHandler",
             "dev.sterner.witchery.feature.affliction.vampire.VampireEvents",
             "dev.sterner.witchery.feature.blood_pool.BloodPoolAttachment"],
}
MATCH = re.compile(r"drainMana|chargeCircuit|updateManaCounter|getMana|setMana|decreaseBlood|handleBloodHunger|handleBloodHealing|getBloodPool|setData|tickBloodRegen", re.I)

def inspect(jar, key):
    with zipfile.ZipFile(jar) as z:
        classes = set(z.namelist())
    for name in NATIVE[key]:
        if name.replace(".", "/") + ".class" not in classes:
            print("MISSING_CLASS", name, flush=True)
            continue
        args = ["javap", "-classpath", str(jar), "-p", "-s", "-c", name]
        out = subprocess.run(args, capture_output=True, text=True, check=True, timeout=45).stdout
        print("CLASS", name, flush=True)
        method = ""
        relevant = False
        for line in out.splitlines():
            if line.startswith("  ") and not line.startswith("    ") and line.rstrip().endswith(";"):
                method = line.strip()
                relevant = bool(MATCH.search(method))
                print("DECL", method)
            elif line.strip().startswith("descriptor:"):
                print("DESC", line.strip())
            elif relevant and ("// Method" in line or "// InterfaceMethod" in line or "// Field" in line):
                print("CALL", method, "|", line.strip())
        print("END", name, flush=True)

with tempfile.TemporaryDirectory() as tmp:
    for provider, (url, expected) in PINS.items():
        request = urllib.request.Request(url, headers={"User-Agent": "TMM-payment-static-audit/1.0"})
        with urllib.request.urlopen(request, timeout=90) as response:
            data = response.read()
        digest = hashlib.sha256(data).hexdigest()
        if digest != expected:
            raise ValueError(f"{provider} installed hash mismatch {digest}")
        path = Path(tmp) / (provider + ".jar")
        path.write_bytes(data)
        print("VERIFIED", provider, digest, flush=True)
        inspect(path, provider)
