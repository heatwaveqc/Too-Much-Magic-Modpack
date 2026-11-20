#!/usr/bin/env python3
"""Read-only B-series smoke/Prism CI authorization audit (issue #260).

Parse executable workflow run steps; do not match comments, path filters or
py_compile references. Standard library only; no game boot, packages or writes.
"""
from __future__ import annotations

import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
LEGACY = re.compile(r"b[0-9]+(?:[-_].*)?\.ya?ml$")
JOB = re.compile(r"^  ([A-Za-z][\w-]*):\s*(?:#.*)?$")
STEP = re.compile(r"^      - (?:name|uses|run|if|id):")
JOB_IF = re.compile(r"^    if:\s*(.+?)\s*$")
STEP_IF = re.compile(r"^        if:\s*(.+?)\s*$")
RUN = re.compile(r"^        run:\s*(.*)$")
SMOKE = re.compile(r"\b(?:bash\s+)?scripts/smoke_server\.sh\b")
PRISM = re.compile(r"\bpython(?:3(?:\.\d+)?)?\s+scripts/(?:build_[\w-]*prism|finalize_[\w-]*client)\.py\b")
ASSEMBLE = re.compile(r"\bpython(?:3(?:\.\d+)?)?\s+scripts/assemble_integration\.py\b")
DISPATCH = re.compile(r"""github\.event_name\s*==\s*['"]workflow_dispatch['"]""")
AUTHORIZED = re.compile(r"\binputs\.(?:full_smoke|authorized_build)\s*==\s*true\b")


def protected(job_if: str, step_if: str) -> bool:
    # Job and step predicates are conjunctive; an OR within either may bypass.
    both = (job_if, step_if)
    if any("||" in text or "\nor " in text for text in both):
        return False
    text = " ".join(both)
    return bool(DISPATCH.search(text) and AUTHORIZED.search(text))


def scan_workflow(source: str, filename: str) -> list[str]:
    errors = []
    in_jobs = False
    job_name, job_if, step_name, step_if = "", "", "", ""
    step_run = []
    step_line = 0
    inspected = 0

    def finish_step() -> None:
        nonlocal inspected
        command = "\n".join(step_run)
        actions = [name for name, regex in (
            ("server smoke", SMOKE),
            ("Prism package", PRISM),
            ("integration assembly", ASSEMBLE),
        ) if regex.search(command)]
        if actions:
            inspected += 1
            if not protected(job_if, step_if):
                errors.append(
                    f"{filename}:{step_line}: {job_name}/{step_name or 'unnamed'} "
                    f"invokes {', '.join(actions)} without workflow_dispatch "
                    "AND full_smoke/authorized_build=true"
                )

    for i, line in enumerate(source.splitlines(), 1):
        if line == "jobs:":
            in_jobs = True
            continue
        if not in_jobs:
            continue
        if line and not line[0].isspace():
            finish_step()
            break
        match = JOB.match(line)
        if match:
            finish_step()
            job_name, job_if, step_name, step_if, step_run = match.group(1), "", "", "", []
            step_line = i
            continue
        if STEP.match(line):
            finish_step()
            step_label, inline = line.strip().split(":", 1)
            step_name, step_if, step_run = inline.strip(), "", []
            # YAML permits an entire step as '- run: command'. Audit that, too.
            if step_label == "- run":
                step_name = "inline-run"
                step_run.append(inline.strip())
            step_line = i
            continue
        match = JOB_IF.match(line)
        if match:
            job_if = match.group(1)
            continue
        match = STEP_IF.match(line)
        if match:
            step_if = match.group(1)
            continue
        match = RUN.match(line)
        if match:
            step_run.append(match.group(1))
            continue
        if step_run and (not line.strip() or len(line) - len(line.lstrip()) >= 10):
            step_run.append(line)
    else:
        finish_step()

    if filename == "b14-cc-eldritch-iesnium.yml" and inspected == 0:
        errors.append("B14 smoke command unexpectedly absent; review policy/renaming explicitly")
    return errors


def audit(root: Path = ROOT) -> tuple[int, list[str]]:
    directory = root / ".github/workflows"
    files = sorted(path for path in directory.iterdir()
                   if path.is_file() and (LEGACY.fullmatch(path.name) or path.name == "verify.yml"))
    if not any(p.name == "b14-cc-eldritch-iesnium.yml" for p in files):
        return 0, ["Missing B14 workflow: audit cannot establish current regression baseline"]
    errors = []
    for path in files:
        errors.extend(scan_workflow(path.read_text(encoding="utf-8"), path.name))
    return len(files), errors


def main() -> int:
    checked, errors = audit()
    for error in errors:
        print("FAIL:", error, file=sys.stderr)
    if errors:
        return 1
    print(f"PASS: {checked} B-series and bootstrap workflows checked; no unguarded game-boot or Prism-package steps")
    print("STATIC ONLY: no smoke/Prism/game launches")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
