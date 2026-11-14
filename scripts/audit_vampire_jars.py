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
CLASS_TERM = re.compile(r"(vampir|blood|sanguin|sunlight|sun.?damage|immortal|death|reviv|resurrect|hunger|food|lycan|curs|batform|bat_form)", re.I)
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
            # Verify actual JAWR UI/localization registry keys and discover in-JEI names.
            # Names from original Witchery must never be assumed for this remake.
            if source.name.startswith("just-another-witchery"):
                import json
                for lang_name in ("assets/witchery/lang/en_us.json", "assets/witchery/lang/en_us.json5"):
                    if lang_name not in z.namelist():
                        continue
                    lang = json.loads(z.read(lang_name))
                    print("JAWR_LANGUAGE_FILE", lang_name, "TOTAL", len(lang), flush=True)
                    for key, val in sorted(lang.items()):
                        if any(x in (key + " " + str(val)).lower() for x in (
                            "vampir", "lilith", "goblet", "wine", "glass", "shadow",
                            "modonomicon", "observations", "book", "boline", "chalk",
                            "skull", "ritual", "sacrif", "vampire")):
                            print("JAWR_UI", key, "=", str(val)[:180], flush=True)
                for res in sorted(z.namelist()):
                    if res.startswith(("data/witchery/", "assets/witchery/", "data/modonomicon/")) and any(
                        x in res.lower() for x in (
                            "modonomicon", "book", "vampir", "lilith", "goblet",
                            "wine_glass", "sacrif", "circle", "ritual"
                        )):
                        print("JAWR_RELEVANT_RESOURCE", res, flush=True)
                for target in (
                    "data/witchery/modonomicon/books/guidebook/entries/vampirism/vamp_1.json",
                    "data/witchery/modonomicon/books/guidebook/categories/vampirism.json",
                    "data/witchery/modonomicon/books/guidebook/book.json",
                    "data/witchery/advancement/vampire/1.json",
                    "data/witchery/advancement/vampire/2.json",
                    "data/witchery/advancement/vampire/3.json",
                    "data/witchery/advancement/witchery/1.json",
                    "data/witchery/recipe/guidebook.json",
                    "data/witchery/recipe/wine_glass.json",
                ):
                    if target in z.namelist():
                        print("JAWR_EXACT_JSON", target, z.read(target).decode("utf-8")[:16000], flush=True)
                # Exact JAWR control names and default key IDs for 0.5.39.
                for key, val in sorted(lang.items()):
                    if any(x in key.lower() for x in ("key.", "keybind", "key_binding", "keybinding", "ability.", "category.witchery", "controls.")):
                        print("JAWR_CONTROL_LANG", key, "=", str(val)[:260], flush=True)
                for cls in sorted(classes):
                    if any(x in cls.lower() for x in ("keybind", "keybinding", "keymapping", "keymapping", "keyhandler", "abilityscreen", "abilityselection", "abilityhud", "abilityinput", "clientability")):
                        print("JAWR_CONTROL_CLASS", cls, flush=True)
                for cls in sorted(classes):
                    if not any(x in cls.lower() for x in ("keybind", "keybinding", "keymapping", "keyhandler")) or "$" in cls:
                        continue
                    proc = subprocess.run(["javap", "-c", "-p", "-classpath", str(jar), cls],
                        capture_output=True, text=True, timeout=25)
                    print("JAWR_CONTROL_BYTECODE", cls, "exit", proc.returncode, flush=True)
                    for ln in proc.stdout.splitlines()[:750]:
                        if any(x in ln.lower() for x in ("key.", "keybind", "keymapping", "glfw_key", "mouse", "ability", "open", "activate", "toggle", "shift", "mousebutton", "key_", "input", "scroll", "select")):
                            print(" JAWR_CONTROL_BC", ln[:350], flush=True)
                for cls in (
                    "dev.sterner.witchery.feature.ability.ClientAbilityHandler",
                    "dev.sterner.witchery.feature.ability.AbilityHudRenderer",
                    "dev.sterner.witchery.client.AbilitySelectionScreen",
                    "dev.sterner.witchery.core.registry.WitcheryKeyMappings",
                ):
                    proc = subprocess.run(["javap", "-c", "-p", "-classpath", str(jar), cls],
                        capture_output=True, text=True, timeout=25)
                    print("JAWR_INPUT_DISASM", cls, "exit", proc.returncode, flush=True)
                    for line in proc.stdout.splitlines()[:2200]:
                        print(" JAWR_INPUT", line[:310], flush=True)
                candidate_names = []
                for name in z.namelist():
                    if name.endswith(".class") and not name.startswith("META-INF/"):
                        raw = z.read(name)
                        if any(tag in raw for tag in (
                            b"WitcheryKeyMappings", b"ClientAbilityHandler",
                            b"AbilityScrollHandler", b"AbilityUseC2SPayload",
                            b"AbilityUsePayload", b"getUTILITY_BUTTON")):
                            candidate_names.append(name.removesuffix(".class").replace("/", "."))
                print("JAWR_INPUT_CALLER_CANDIDATES", sorted(candidate_names), flush=True)
                for cls in sorted(candidate_names):
                    if "$" in cls or cls.startswith("dev.sterner.witchery.feature.ability.AbilityHudRenderer"):
                        continue
                    proc = subprocess.run(["javap", "-c", "-p", "-classpath", str(jar), cls],
                        capture_output=True, text=True, timeout=25)
                    print("JAWR_INPUT_CALLER", cls, "exit", proc.returncode, flush=True)
                    for line in proc.stdout.splitlines()[:2200]:
                        print(" JAWR_CALLER_BC", line[:320], flush=True)
                control_callers = []
                for name in z.namelist():
                    if not name.endswith(".class") or name.startswith("META-INF/"): continue
                    raw = z.read(name)
                    if any(marker in raw for marker in (
                        b"clientRightClickAbility", b"handleScroll",
                        b"AbilityHandler", b"AbilityEventHandler", b"getUTILITY_BUTTON",
                        b"UseAbility", b"abilityIndex")):
                        control_callers.append(name.removesuffix(".class").replace("/", "."))
                print("JAWR_CONTROL_SECONDARY", sorted(control_callers), flush=True)
                for cls in sorted(control_callers):
                    if not ("mixin" in cls.lower() or "client" in cls.lower() or "Input" in cls or "Ability" in cls):
                        continue
                    if "$" in cls or cls.endswith(("AbilitySelectionScreen", "AbilityHudRenderer")):
                        continue
                    if cls in ("dev.sterner.witchery.feature.ability.AbilityHandler",
                               "dev.sterner.witchery.feature.ability.AbilityEventHandler",
                               "dev.sterner.witchery.feature.ability.ClientAbilityHandler",
                               "dev.sterner.witchery.feature.ability.AbilityScrollHandler"): continue
                    proc = subprocess.run(["javap", "-c", "-p", "-classpath", str(jar), cls],
                        capture_output=True,text=True,timeout=22)
                    if proc.returncode != 0: continue
                    lines=proc.stdout.splitlines()
                    hits=[i for i,line in enumerate(lines) if
                        any(key in line for key in ("AbilityHandler", "AbilityEventHandler",
                            "WitcheryKeyMappings", "MouseHandler", "getUTILITY_BUTTON",
                            "handleScroll", "clientRightClickAbility", "AbilityUseC2SPayload"))]
                    if hits:
                        print("JAWR_ACTIVATION_CALLER", cls, "hits",len(hits),flush=True)
                        for i in hits[:35]:
                            for ln in lines[max(0,i-13):min(len(lines),i+12)]:
                                print(" JAWR_ACTIVATION", ln[:320], flush=True)
                # Acquisition entrypoints: identify what triggers first-level advancement,
                # how the sacrificial circle is created, and ritual materials.
                for cls in sorted(classes):
                    if any(k in cls.split(".")[-1] for k in (
                        "Sacrificial", "Lilith", "Elle", "TornPage", "WineGlass",
                        "InfernalChalk", "Arthana", "Skull", "Candle", "VampireLeveling"
                    )):
                        print("JAWR_ACQUISITION_CLASS", cls, flush=True)
                for cls in sorted(classes):
                    if any(x in cls for x in (
                        "WitcheryItems", "WitcheryItemRegistry", "WitcheryItem",
                        "Modonomicon", "ModonomiconBook", "VampireLevelOneEntryProvider",
                        "VampireLevelTwoEntryProvider"
                    )):
                        print("JAWR_REGISTRY_OR_GUIDE_CLASS", cls, flush=True)
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
            if source.name.startswith("just-another-witchery"):
                audit_acquisition = [
                    c for c in classes if any(k in c.split(".")[-1] for k in
                    ("SacrificialBlock", "SacrificialBlockComponent", "SacrificialBlockEntity", "SacrificialCircleBlock", "LilithEntity", "ElleEntity",
                     "TornPageItem", "WineGlassItem", "InfernalChalkBlock",
                     "ArthanaItem", "VampireLeveling"))
                    and "$" not in c
                ]
                for cls in audit_acquisition:
                    proc = subprocess.run(
                        ["javap", "-c", "-p", "-classpath", str(jar), cls],
                        capture_output=True, text=True, timeout=30)
                    print("JAWR_ACQUISITION_BYTECODE", cls, "exit", proc.returncode, flush=True)
                    lines = proc.stdout.splitlines()
                    for i, line in enumerate(lines):
                        if line.startswith("  ") and not line.startswith("    ") and any(
                            x in line for x in ("use(", "useOn(", "interactLivingEntity(",
                                "onEntityInteract(", "onUseTick(", "onPlace(", "setLevel(",
                                "increaseLevel(", "interact(", "onPlayerInteract(", "hurtEnemy(",
                                "onEntityKill(", "tryCreate(", "create(", "handle(", "tick(",
                                "onInteract(", "onEntityDeath(", "placeBlock(", "getStateForPlacement(", "onRemove(", "setPlacedBy(", "neighborChanged(", "playerWillDestroy(", "onDestroyedByPlayer(")):
                            end = next((j for j in range(i+1, len(lines)) if lines[j].startswith(
                                ("  public ", "  private ", "  protected ", "  static ")
                            ) or lines[j] == "}"), len(lines))
                            print(" JAWR_METHOD", line.strip(), flush=True)
                            for instr in lines[i+1:min(i+210, end)]:
                                print(" JAWR_BC", instr[:300], flush=True)
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

            if source.name.startswith("just-another-witchery"):
                cls = "dev.sterner.witchery.content.item.TornPageItem"
                proc = subprocess.run(
                    ["javap", "-c", "-p", "-classpath", str(jar), cls],
                    capture_output=True, text=True, timeout=25)
                ls = proc.stdout.splitlines()
                for i, line in enumerate(ls):
                    if "static {};" in line or "advancementLocations" in line and line.startswith("  "):
                        print("TORN_PAGE_STATIC", line, flush=True)
                        for follower in ls[i+1:min(len(ls),i+190)]:
                            if follower == "}":
                                break
                            print(" TORN_PAGE_BC", follower[:330], flush=True)
            if source.name.startswith("just-another-witchery"):
                for cls in (
                    "dev.sterner.witchery.feature.affliction.vampire.VampireLeveling",
                    "dev.sterner.witchery.core.registry.WitcheryAbilities",
                    "dev.sterner.witchery.client.hud.QuestHudRenderer",
                ):
                    proc = subprocess.run(
                        ["javap", "-c", "-p", "-classpath", str(jar), cls],
                        capture_output=True, text=True, timeout=35)
                    print("JAWR_PROGRESSION_DISASM", cls, "exit", proc.returncode, flush=True)
                    lines = proc.stdout.splitlines()
                    if cls.endswith("QuestHudRenderer"):
                        for i,line in enumerate(lines):
                            if line.startswith("  public final") and "getVampireQuests" in line:
                                for snippet in lines[i:min(i+1250,len(lines))]:
                                    if any(k in snippet for k in ("String", "Integer.valueOf", "getLEVEL_REQUIREMENTS", "getAdvancement", "getLevel", "getFill", "getNight", "getHalf", "getSelected", "getProgress", "getAbility")):
                                        print(" JAWR_PROGRESSION_QUEST", snippet[:320], flush=True)
                    else:
                        for line in lines[:2500]:
                            print(" JAWR_PROGRESSION_BC", line[:350], flush=True)
                # Check known 1->2 and 2->3 advancement and guide book references.
                for candidate in sorted(z.namelist()):
                    if candidate.startswith("data/witchery/modonomicon/books/guidebook/entries/vampirism/") and candidate.endswith(".json"):
                        obj = __import__("json").loads(z.read(candidate))
                        if candidate.endswith(("vamp_1.json", "vamp_2.json", "vamp_3.json", "vamp_4.json")):
                            print("JAWR_PROGRESSION_BOOK", candidate, __import__("json").dumps(obj)[:4500], flush=True)
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
