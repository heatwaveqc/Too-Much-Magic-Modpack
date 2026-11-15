#!/usr/bin/env python3
"""Read-only pinned-JAR audit for the Transformation Compat vampire pilot.

Downloads ONLY two unmodified, hash-pinned mod artifacts into an ephemeral temp
directory. Prints class/API and resource inventories, never uploads/distributes JARs.
Do not infer event semantics or player state from class names alone.
"""
from __future__ import annotations

import hashlib
import pathlib
import re
import subprocess
import tempfile
import tomllib
import urllib.parse
import urllib.request
import zipfile

ROOT = pathlib.Path(__file__).resolve().parents[1]
MOD_PINS = (
    "mods/just-another-witchery-remake.pw.toml",
    "mods/vampirism-become-a-vampire.pw.toml",
)
CLASS_TERM = re.compile(r"(vampir|blood|sanguin|sunlight|sun.?damage|immortal|death|reviv|resurrect|hunger|food|lycan|curs|batform|bat_form|bottle|goblet|wine.?glass|crucible|bone.?needle)", re.I)
METHOD_TERM = re.compile(r"(vampir|blood|sanguin|sun|immortal|death|reviv|respawn|hunger|feed|eat|drink|cure|curse|transform|faction|isVampire)", re.I)


def inspect_pin(pin: str) -> None:
    source = ROOT / pin
    data = tomllib.loads(source.read_text())
    info = data["download"]
    url = info.get("url")
    if not url and info.get("mode") == "metadata:curseforge":
        fid = data["update"]["curseforge"]["file-id"]
        filename = data["filename"]
        url = f"https://mediafilez.forgecdn.net/files/{fid//1000}/{fid%1000:03d}/{urllib.parse.quote(filename)}"
    if not url or urllib.parse.urlparse(url).hostname not in {"mediafilez.forgecdn.net", "cdn.modrinth.com", "edge.forgecdn.net"}:
        raise ValueError(f"Unapproved source for {pin}")
    print(f"===== {source.name} =====", flush=True)
    req = urllib.request.Request(url, headers={"User-Agent": "TMM-Vampire-Audit/1.0"})
    with urllib.request.urlopen(req, timeout=90) as response:
        payload = response.read()
    algorithm = info["hash-format"]
    assert algorithm in {"sha512", "sha256", "sha1"}
    actual = getattr(hashlib, algorithm)(payload).hexdigest()
    if actual.lower() != info["hash"].lower():
        raise ValueError(f"Hash mismatch: {pin}")
    print(f"HASH VERIFIED: {algorithm} {actual}, bytes={len(payload)}", flush=True)

    with tempfile.TemporaryDirectory(prefix="tmm-vampire-audit-") as tmp:
        jar = pathlib.Path(tmp) / data["filename"]
        jar.write_bytes(payload)
        with zipfile.ZipFile(jar) as z:
            classes = [n.removesuffix(".class").replace("/", ".") for n in z.namelist()
                       if n.endswith(".class") and not n.startswith("META-INF/")]
            matching = sorted(c for c in classes if CLASS_TERM.search(c))
            files = sorted(n for n in z.namelist() if
                           n.startswith(("data/", "assets/")) and
                           re.search(r"(vampir|blood|sun|curs|bat|reviv|resurrect|food)", n, re.I))
            print(f"TOTAL classes={len(classes)}, vampire/death/food-related={len(matching)}, related resources={len(files)}")
            print("MATCHING_CLASSES:")
            for name in matching[:260]:
                print(" CLASS", name)
            if len(matching) > 260:
                print(f" ... {len(matching)-260} more matching classes")
            print("MATCHING_RESOURCES:")
            for path in files[:180]:
                print(" RESOURCE", path)
            if len(files) > 180:
                print(f" ... {len(files)-180} more matching resources")
            print("ENTRYPOINT_CANDIDATES:")
            keys = re.compile(r"(VampireData|VampirePlayer|Vampire.*Attachment|VampirismAPI|Blood.*Data|Blood.*Component|Vampire.*Component|Vampire.*Event|Vampire.*Handler|Vampire.*Ability|Vampire.*Skill|Vampire.*Power|Vampire.*Quest|Vampire.*Mixin|PlayerFactionEvent|FactionPlayerHandler|Vampire.*Death|Vampire.*Config|Vampire.*Transformation)", re.I)
            candidates = [x for x in matching if keys.search(x.split(".")[-1]) and "$" not in x]
            for cls in candidates[:50]:
                proc = subprocess.run(["javap", "-p", "-classpath", str(jar), cls],
                                       capture_output=True, text=True, timeout=12)
                if proc.returncode == 0:
                    print(" JAVAP", cls)
                    for line in proc.stdout.splitlines():
                        if METHOD_TERM.search(line) or re.search(r"^public (class|interface|record|final|abstract)", line):
                            print("  ", line[:310])
                else:
                    print(" JAVAP_FAILED", cls, proc.stderr[:200])
            if len(candidates) > 50:
                print(f" ... {len(candidates)-50} more candidates")
            # Exact class signatures from the pinned binary for proposed adapters.
            deep = (
                "dev.sterner.witchery.feature.affliction.vampire.VampirePlayerAttachment",
                "dev.sterner.witchery.feature.affliction.vampire.VampirePlayerAttachment$Data",
                "dev.sterner.witchery.feature.affliction.vampire.VampireEvents",
                "dev.sterner.witchery.feature.affliction.vampire.VampireLeveling",
                "dev.sterner.witchery.feature.blood_pool.BloodPoolAttachment",
                "dev.sterner.witchery.feature.blood_pool.BloodPoolAttachment$Data",
                "dev.sterner.witchery.feature.blood_pool.BloodPoolHandler",
                "dev.sterner.witchery.content.ability.DrinkBloodAbility",
                "dev.sterner.witchery.core.api.event.VampireAfflictionEvent$LevelChanged",
                "dev.sterner.witchery.core.api.event.VampireAfflictionEvent$SunDamageApplied",
                "dev.sterner.witchery.core.api.event.VampireAfflictionEvent$BloodHealing",
                "de.teamlapen.vampirism.api.event.PlayerFactionEvent$FactionLevelChangePre",
                "de.teamlapen.vampirism.api.entity.player.vampire.IBloodStats",
            )
            for cls in deep:
                if cls not in classes:
                    continue
                proc = subprocess.run(["javap", "-p", "-classpath", str(jar), cls],
                                      capture_output=True, text=True, timeout=20)
                print("DEEP_SIGNATURE", cls, "exit", proc.returncode)
                for line in proc.stdout.splitlines()[:200]:
                    print(" SIG", line[:350])
            # Summarize critical bytecode paths, never ship or rewrite upstream JARs.
            bytecode = {
                "dev.sterner.witchery.feature.affliction.vampire.VampirePlayerAttachment":
                    ("getData(", "setData("),
                "dev.sterner.witchery.feature.affliction.vampire.VampireEvents":
                    ("handleBloodHunger(", "handleBloodHealing(", "handleSunExposure(", "vampireTick("),
                "dev.sterner.witchery.feature.blood_pool.BloodPoolHandler":
                    ("getBlood(", "setBlood(", "consumeBlood(", "addBlood("),
            }
            for cls, needles in bytecode.items():
                if cls not in classes:
                    continue
                proc = subprocess.run(["javap", "-c", "-p", "-classpath", str(jar), cls],
                                      capture_output=True, text=True, timeout=25)
                ls = proc.stdout.splitlines()
                print("BYTECODE", cls, "exit", proc.returncode)
                for i, line in enumerate(ls):
                    if any(n in line for n in needles) and line.startswith("  ") and not line.startswith("    "):
                        print(" METHOD", line[:350])
                        for follower in ls[i+1:i+100]:
                            if follower.startswith("  public") or follower.startswith("  private") or follower.startswith("  protected") or follower == "}":
                                break
                            print(" BC", follower[:280])

            # Canonical bottled-blood economy audit: preserve special vampire blood.
            # This is read-only inspection of the hash-pinned JAWR JAR. Never
            # normalize transformation-capable items without checking component
            # semantics and volume/transaction rules in this exact binary.
            bottle_classes = [name for name in classes if re.search(
                r"(BottleOfBlood|BloodBottle|WineGlass|GlassGoblet|BloodCrucible|BoneNeedle)",
                name.split(".")[-1], re.I) and "$" not in name]
            for cls in sorted(bottle_classes)[:35]:
                signature = subprocess.run(["javap", "-p", "-classpath", str(jar), cls],
                    capture_output=True, text=True, timeout=20)
                print("BOTTLE_CLASS", cls, "exit", signature.returncode, flush=True)
                for line in signature.stdout.splitlines()[:100]:
                    print(" BOTTLE_SIGNATURE", line[:320], flush=True)
                bytecode = subprocess.run(["javap", "-c", "-p", "-classpath", str(jar), cls],
                    capture_output=True, text=True, timeout=35)
                if bytecode.returncode != 0:
                    print(" BOTTLE_BYTECODE_FAILED", bytecode.stderr[:250], flush=True)
                    continue
                lines = bytecode.stdout.splitlines()
                # Show full bytecode only for small, directly relevant classes;
                # otherwise print selected methods without huge binary dumps.
                method_terms = re.compile(
                    r"(finishUsingItem|use\(|interact|fill|empty|consume|blood|vampir|isVampire|setData|getData|tooltip|appendHoverText|hurt)",
                    re.I)
                selected = 0
                for i, line in enumerate(lines):
                    if not (line.startswith(("  public ", "  private ", "  protected "))
                            and method_terms.search(line)):
                        continue
                    j = next((j for j in range(i+1, len(lines))
                        if lines[j].startswith(("  public ", "  private ", "  protected "))
                        or lines[j] == "}"), len(lines))
                    print(" BOTTLE_METHOD", cls, line.strip(), flush=True)
                    for instruction in lines[i+1:min(j, i+130)]:
                        print(" BOTTLE_BC", instruction[:250], flush=True)
                    selected += 1
                    if selected >= 12:
                        break

            # Transaction-specific disassembly for exact pinned versions. Inspect
            # acquisition, cure, feeding and weaknesses before enabling mixins.
            focus = {
                "dev.sterner.witchery.feature.affliction.vampire.VampireLeveling": (
                    "setLevel(", "increaseLevel(", "checkAndLevelUp("),
                "dev.sterner.witchery.feature.affliction.vampire.VampireEvents": (
                    "handleHurt(", "handleSunDamage(", "onItemUseFinish(", "onLogin(", "handleBloodHunger("),
                "dev.sterner.witchery.feature.blood_pool.BloodPoolHandler": (
                    "increaseBlood(", "decreaseBlood("),
                "dev.sterner.witchery.feature.affliction.vampire.VampirePlayerAttachment": (
                    "setData(", "getData("),
                "dev.sterner.witchery.content.ritual_event.RemoveCurseRitualEvent": (
                    "onFinish(", "start(", "complete(", "run(", "apply("),
                "dev.sterner.witchery.content.ability.DrinkBloodAbility": (
                    "transferBlood(", "vampireDrinkBloodAbility("),
                "dev.sterner.witchery.content.item.BottleOfBloodItem": (
                    "finishUsingItem(", "use("),
                "de.teamlapen.vampirism.entity.factions.FactionPlayerHandler": (
                    "setFactionAndLevel(", "leaveFaction("),
                "de.teamlapen.vampirism.entity.player.vampire.VampirePlayer": (
                    "drinkBlood(", "useBlood(", "removeBlood("),
            }
            for cls, needles in focus.items():
                if cls not in classes:
                    continue
                pr = subprocess.run(["javap", "-c", "-p", "-classpath", str(jar), cls],
                                    capture_output=True, text=True, timeout=45)
                ls = pr.stdout.splitlines()
                print("FOCUS_CLASS", cls, "exit", pr.returncode, flush=True)
                for i, line in enumerate(ls):
                    if line.startswith("  ") and not line.startswith("    ") and any(n in line for n in needles):
                        next_method = next((j for j in range(i+1, len(ls))
                            if ls[j].startswith(("  public ", "  private ", "  protected ", "  static "))
                            or ls[j] == "}"), len(ls))
                        excerpt = ls[i:min(next_method, i+190)]
                        print("FOCUS_METHOD", cls, line.strip(), flush=True)
                        for instr in excerpt[1:]:
                            print(" FOCUS", instr[:270], flush=True)
        jar.unlink()


if __name__ == "__main__":
    for entry in MOD_PINS:
        inspect_pin(entry)
