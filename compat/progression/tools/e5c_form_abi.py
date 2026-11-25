"""E5c native form evidence only: hash-pinned original JARs, no startup or uploads."""
import hashlib
import pathlib
import re
import subprocess
import tempfile
import urllib.request
import zipfile

PINS = {
    "dragon": ((
        "https://mediafilez.forgecdn.net/files/9539/454/DragonSurvival-1.21.1-v2.0.74-09.11.2026-all.jar",
        "https://edge.forgecdn.net/files/9539/454/DragonSurvival-1.21.1-v2.0.74-09.11.2026-all.jar"),
        "sha1", "e11429dda4688711e174625bb7420873a255a715"),
    "jawr": (("https://cdn.modrinth.com/data/LCO8KZvC/versions/TWcJbsjg/witchery-0.5.39%2Bmc1.21.1.jar",),
        "sha256", "81557ceb1b878d2aa8df5fcbcc468b82479d1e38d38097873a6ffb225324e256"),
}
# Dragon release file/hash from the existing owner-approved pin_next_test_mods.py.
# JAWR identical to the prior E4 exact native payment audit.
DS_FIXED = ("DragonStateProvider", "DragonStateHandler", "ClawInventoryData",
            "ClawInventoryData$Slot", "ClawToolHandler", "ToolUtils", "DSDamageTypes",
            "DamageEffect", "ProjectileDamageEffect")
JAWR_FIXED = ("DrinkBloodAbility", "VampireEvents", "WerewolfEvents",
              "TransformationPlayerAttachment", "TransformationPlayerAttachment$Data")
METHOD = re.compile(r"bite|claw|paw|breath|attack|hurt|damage|harvest|swap|growth|getData\(|isDragon\(|speciesId\(|transformationType|transferBlood|wolf", re.I)
CALL = re.compile(r"hurt:|DamageSource|DamageTypes|damageSources:|swapStart:|swapFinish:|getDragonSword:|getDragonHarvestTool|canHarvestWithPaw|isCorrectToolForDrops|transferBlood:|getTransformationType:|getGrowth:", re.I)

def download(urls, algorithm, digest):
    errors = []
    for url in urls:
        try:
            req = urllib.request.Request(url, headers={"User-Agent": "TMM-static-form-audit/1.0"})
            with urllib.request.urlopen(req, timeout=90) as response:
                data = response.read(80_000_001)
            if len(data) > 80_000_000:
                raise ValueError("oversized artifact")
            if hashlib.new(algorithm, data).hexdigest() != digest:
                raise ValueError("hash mismatch; refusing artifact")
            return data
        except (OSError, ValueError) as exc:
            errors.append(str(exc))
    raise RuntimeError("No hash-matched artifact: " + " | ".join(errors))

def inspect(jar, key):
    with zipfile.ZipFile(jar) as z:
        classes = []
        for n in sorted(z.namelist()):
            if n.endswith(".json") and "/damage_type/" in n and "/tags/" not in n:
                print("DAMAGE_ID", n, flush=True)
            if key == "dragon" and n.endswith(".json") and "/dragon_ability/" in n and any(x in n.lower() for x in ("breath", "bite", "claw", "fang", "strike")):
                print("BREATH_DATA", n, z.read(n).decode("utf-8")[:7000].replace("\n", " "))
            if not n.endswith(".class") or n.startswith("META-INF/"):
                continue
            stem = n[:-6].rsplit("/", 1)[-1]
            raw = z.read(n)
            if key == "dragon":
                chosen = stem in DS_FIXED or (b"swapStart" in raw and b"ClawInventoryData" in raw) or ("breath" in stem.lower() and b"DamageSource" in raw)
            else:
                chosen = stem in JAWR_FIXED or ("ability" in n.lower() and any(t in n.lower() for t in ("vampire", "werewolf", "bite", "claw"))) or ("mixin" in n.lower() and (b"werewolf" in raw.lower() or b"TransformationHandler" in raw) and any(t in raw.lower() for t in (b"hurt", b"attack", b"harvest", b"destroy", b"dig", b"mining", b"correcttool")))
            if chosen:
                cls = n[:-6].replace("/", ".")
                classes.append(cls)
                print("CLASS_CANDIDATE", cls)
    if not classes:
        raise RuntimeError("no matching native classes")
    for clazz in classes:
        output = subprocess.run(["javap", "-classpath", str(jar), "-p", "-s", "-c", clazz],
                                capture_output=True, text=True, check=True, timeout=45).stdout
        print("CLASS", clazz, flush=True)
        method, relevant, declared = "", False, False
        for line in output.splitlines():
            if line.startswith("  ") and not line.startswith("    ") and line.rstrip().endswith(";"):
                method = line.strip()
                relevant = bool(METHOD.search(method))
                declared = True
                print("DECL", method)
            elif declared and line.strip().startswith("descriptor:"):
                print("DESC", line.strip())
            elif line.lstrip().split(":", 1)[0].isdigit() and (relevant and ("// Method" in line or "// InterfaceMethod" in line or "// Field" in line) or CALL.search(line)):
                print("BC", method, "|", line.strip())
        print("END", clazz, flush=True)

with tempfile.TemporaryDirectory() as directory:
    for key, (urls, algorithm, digest) in PINS.items():
        jar = pathlib.Path(directory) / (key + ".jar")
        jar.write_bytes(download(urls, algorithm, digest))
        print("VERIFIED", key, algorithm, digest, flush=True)
        inspect(jar, key)
