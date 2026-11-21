#!/usr/bin/env python3
"""Static cutover contract: the standalone Tyr runtime may not reactivate legacy engines.

Historical design, archived sources, and non-shipped `compat/mxp` stay outside
this scan intentionally. Integration-side pack/Packwiz checks belong to #376.
"""
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java"
PROGRESSION_MOD = JAVA / "io/github/heatwaveqc/tyr/progression/ProgressionMod.java"
OLD_PACKAGE = JAVA / "io/github/heatwaveqc/tmm/progression"
SCAN_DIRS = [
    JAVA, ROOT / "src/main/resources", ROOT / "src/main/templates",
    ROOT / "src/test/java", ROOT / "src/test/resources",
]
SCAN_EXTS = {".java", ".json", ".toml", ".properties", ".yml", ".yaml", ".mcmeta", ".cfg"}
FORBIDDEN = (
    "io.github.heatwaveqc.tmm.progression",
    "tmm_progression", "com.tyzsskills", "TyzMonkBridge",
    "FormSkillRegistration", "FormSkillRuntime", "tmmmxp",
)
LEGACY_LOAD_IDS = ("tmm_mxp", "tyzs_skills")
issues = []
scanned = 0
if OLD_PACKAGE.exists():
    issues.append(f"legacy Java package still exists: {OLD_PACKAGE.relative_to(ROOT)}")
if not PROGRESSION_MOD.is_file():
    issues.append("missing canonical Tyr Progression entrypoint")
else:
    main = PROGRESSION_MOD.read_text(encoding="utf-8")
    if '@Mod(ProgressionMod.MOD_ID)' not in main or 'MOD_ID = "tyr_progression"' not in main:
        issues.append("canonical entrypoint must register exactly tyr_progression")
    for legacy_id in LEGACY_LOAD_IDS:
        if f'ModList.get().isLoaded("{legacy_id}")' not in main:
            issues.append(f"missing fail-fast guard for {legacy_id}")

for root in SCAN_DIRS:
    if not root.is_dir():
        continue
    for path in root.rglob("*"):
        if not path.is_file() or path.suffix not in SCAN_EXTS:
            continue
        scanned += 1
        rel = path.relative_to(ROOT).as_posix()
        try:
            content = path.read_text(encoding="utf-8")
        except UnicodeError:
            issues.append(f"{rel}: not UTF-8 text")
            continue
        for line, text in enumerate(content.splitlines(), 1):
            for term in FORBIDDEN:
                if term in text:
                    issues.append(f"{rel}:{line}: legacy runtime identifier {term!r}")
            for legacy_id in LEGACY_LOAD_IDS:
                if legacy_id in text and not (
                    path == PROGRESSION_MOD and
                    f'ModList.get().isLoaded("{legacy_id}")' in text
                ):
                    issues.append(f"{rel}:{line}: obsolete provider {legacy_id!r} outside fail-fast guard")
gradle = (ROOT / "build.gradle").read_text(encoding="utf-8")
for term in ("TyzsSkills", "tyzsskills", "jitpack.io"):
    if term.lower() in gradle.lower():
        issues.append(f"build.gradle: retired compile dependency or repository {term}")
props = (ROOT / "gradle.properties").read_text(encoding="utf-8")
if "mod_id=tyr_progression" not in props or "mod_name=Tyr Progression" not in props:
    issues.append("gradle.properties: incorrect Tyr mod metadata")
if issues:
    print("FAIL: Tyr runtime cutover static guard")
    print("\n".join(f" - {issue}" for issue in issues))
    sys.exit(1)
print(f"PASS: scanned {scanned} active source/config files; no obsolete runtime identities or Tyz API imports")
