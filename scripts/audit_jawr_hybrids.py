#!/usr/bin/env python3
"""Evidence-only JAWR vampire/lycanthrope audit of the exact pack-pinned 1.21.1 JAR.

Runs in CI, verifies the JAR's cryptographic pin, inspects class constants,
resources, method signatures, and selected javap bytecode. It never uploads
the third-party JAR, injects code, changes player data, or claims gameplay tests.
"""
from __future__ import annotations

import hashlib
import json
import pathlib
import re
import subprocess
import tempfile
import tomllib
import urllib.parse
import urllib.request
import zipfile

ROOT = pathlib.Path(__file__).resolve().parents[1]
PIN = ROOT / "mods" / "just-another-witchery-remake.pw.toml"
WOLF = re.compile(r"(werewolf|lycanthrop|wolfman|wolf_altar|wolfaltar|moon.?charm|moon.?stone)", re.I)
VAMP = re.compile(r"(vampir|bloodpool|blood_pool)", re.I)
HYBRID = re.compile(r"(hybrid|abominat|vampire.?werewolf|werewolf.?vampire|vampire.?lycan|lycan.?vampire)", re.I)
METHOD = re.compile(r"(vampir|lycan|werewolf|wolf|hybrid|abominat|cure|curse|remove|setLevel|setData|is[A-Z]|can[A-Z]|onHurt|hurt|onAttack|transform|onTick|tick|altar|infect|apply|afflict)", re.I)
BYTECODE = re.compile(r"(vampir|lycan|werewolf|wolf|hybrid|abominat|curse|blood|hunger|food|transform|attack|damage|hurt|level|isVampire|isWerewolf)", re.I)


def main() -> None:
    config = tomllib.loads(PIN.read_text(encoding="utf-8"))
    source = config["download"]
    url = source.get("url")
    assert url and urllib.parse.urlparse(url).hostname == "cdn.modrinth.com", "Only pinned Modrinth JAR permitted"
    assert source["hash-format"] in {"sha256", "sha512"}
    payload = urllib.request.urlopen(
        urllib.request.Request(url, headers={"User-Agent": "TMM-JAWR-Hybrid-Audit/1.0"}), timeout=100
    ).read()
    digest = hashlib.new(source["hash-format"], payload).hexdigest()
    if digest.lower() != source["hash"].lower():
        raise ValueError("Pinned JAWR hash mismatch; refusing bytecode audit")
    print("PIN_VERIFIED", config["filename"], source["hash-format"], digest, len(payload), flush=True)

    with tempfile.TemporaryDirectory(prefix="tmm-jawr-hybrid-") as tmp:
        jar = pathlib.Path(tmp) / "witchery.jar"
        jar.write_bytes(payload)
        with zipfile.ZipFile(jar) as z:
            names = z.namelist()
            classes = sorted(n.removesuffix(".class").replace("/", ".") for n in names
                             if n.endswith(".class") and n.startswith("dev/sterner/witchery/"))
            wolf = [c for c in classes if WOLF.search(c)]
            vampire = [c for c in classes if VAMP.search(c)]
            hybrid = [c for c in classes if HYBRID.search(c)]
            print("TOTAL_CLASSES", len(classes), "WOLF_CLASSES", len(wolf), "VAMP_CLASSES", len(vampire), "HYBRID_CLASSES", len(hybrid))
            for label, group, limit in (("WOLF_CLASS", wolf, 120), ("HYBRID_CLASS", hybrid, 80)):
                for cls in group[:limit]:
                    print(label, cls)
                if len(group) > limit:
                    print(label, "OMITTED", len(group) - limit)

            # Constant-pool references do not prove execution, but reveal actual
            # coexistence checks/cross-affliction call sites worth disassembling.
            cross = []
            explicit = []
            for cls in classes:
                raw = z.read(cls.replace(".", "/") + ".class")
                low = raw.lower()
                v = b"vampireplayerattachment" in low or b"vampireleveling" in low
                w = (b"werewolfplayerattachment" in low or
                     b"lycanthropyplayerattachment" in low or
                     b"werewolfleveling" in low or b"lycanthropyleveling" in low)
                if v and w:
                    cross.append(cls)
                if any(marker in low for marker in (
                        b"allowvampirewerewolfhybrids", b"allow_vampire_werewolf",
                        b"vampirewerewolfhybrid", b"abomination")):
                    explicit.append(cls)
            print("CROSS_ATTACHMENT_REFERENCE_COUNT", len(cross))
            for cls in cross[:100]:
                print("CROSS_ATTACHMENT_CLASS", cls)
            print("EXPLICIT_HYBRID_MARKER_COUNT", len(explicit))
            for cls in explicit[:100]:
                print("EXPLICIT_HYBRID_MARKER_CLASS", cls)

            # Inspect guides, configurable features and cure/altar recipes.
            resources = sorted(n for n in names if (n.startswith(("data/witchery/", "assets/witchery/"))
                           and (WOLF.search(n) or HYBRID.search(n) or
                                ("remove_curse" in n.lower()) or ("cure" in n.lower()))))
            print("RESOURCE_COUNT", len(resources))
            for path in resources[:140]:
                print("RESOURCE", path)
                if path.endswith(".json") and len(z.read(path)) < 6000 and any(k in path.lower()
                        for k in ("modonomicon", "ritual", "advancement")):
                    value = z.read(path).decode("utf-8", errors="replace")
                    print("RESOURCE_EXCERPT", path, " ".join(value.split())[:1300])
            lang = "assets/witchery/lang/en_us.json"
            if lang in names:
                data = json.loads(z.read(lang))
                entries = [(k, str(v)) for k, v in data.items()
                           if WOLF.search(k + " " + str(v)) or HYBRID.search(k + " " + str(v))]
                print("LANG_KEY_COUNT", len(entries))
                for key, value in sorted(entries)[:145]:
                    print("LANG_KEY", key, "=", value[:155])
            # Target all core wolf implementation classes and a small set of
            # cross-provider classes; avoid dumping every decompiled method.
            chosen = [c for c in wolf if "$" not in c and any(k in c.lower() for k in (
                "affliction", "level", "event", "altar", "ability", "attachment",
                "effect", "entity", "item"))]
            chosen += [c for c in cross + explicit if "$" not in c]
            chosen += [c for c in vampire if "$" not in c and c.endswith((
                "VampireEvents", "VampireLeveling", "VampirePlayerAttachment"))]
            chosen += [c for c in classes if "$" not in c and any(k in c.lower() for k in (
                "removecurseritualevent", "witcheryconfig", "lycanthropyconfig", "werewolfconfig"))]
            chosen = list(dict.fromkeys(chosen))[:65]
            print("DISASSEMBLY_CLASS_COUNT", len(chosen))
            for cls in chosen:
                proc = subprocess.run(
                    ["javap", "-c", "-p", "-classpath", str(jar), cls],
                    text=True, capture_output=True, timeout=35
                )
                print("JAVAP_CLASS", cls, "EXIT", proc.returncode)
                if proc.returncode:
                    print("JAVAP_ERROR", proc.stderr[:220].replace("\n", " "))
                    continue
                lines = proc.stdout.splitlines()
                print("JAVAP_LINES", cls, len(lines))
                heads = [i for i, line in enumerate(lines)
                         if line.startswith(("  public ", "  private ", "  protected ", "  static ", "  final ")) and
                         "(" in line and line.rstrip().endswith(";")]
                printed = 0
                for index, begin in enumerate(heads):
                    end = heads[index + 1] if index + 1 < len(heads) else len(lines)
                    header = lines[begin]
                    body = lines[begin + 1:end]
                    interesting = bool(METHOD.search(header)) or any(
                        HYBRID.search(line) or
                        ("VampirePlayerAttachment" in line and
                         WOLF.search("\n".join(body))) or
                        ("LycanthropyPlayerAttachment" in line and
                         VAMP.search("\n".join(body)))
                        for line in body)
                    if not interesting:
                        continue
                    print("METHOD", cls, header.strip()[:245])
                    hits = [i for i, line in enumerate(body) if BYTECODE.search(line)]
                    chosen_indices = set()
                    for hit in hits[:18]:
                        chosen_indices.update(range(max(0, hit - 2), min(len(body), hit + 3)))
                    # Always show an initial instruction sample, even if no text
                    # match, so an absent branch is distinguishable from no audit.
                    chosen_indices.update(range(min(8, len(body))))
                    for i in sorted(chosen_indices)[:90]:
                        print(" BC", body[i][:245])
                    printed += 1
                    if printed >= 22:
                        print("METHOD_CAP", cls, len(heads))
                        break
            print("AUDIT_COMPLETE (bytecode/static only; no gameplay or live hybrid test)", flush=True)


if __name__ == "__main__":
    main()
