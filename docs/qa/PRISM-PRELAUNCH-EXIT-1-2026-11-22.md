# November 22 U/E launch failure: Windows pre-launch process regression

Status: root cause traced; **no corrective code or instance changes applied**. Owner requested diagnosis and publication of findings, not an ad-hoc bypass or another game launch. No Minecraft client/server, world, smoke or packaging run was started during this investigation.

## Finding

The new PowerShell pre-launch verifier treats an unset native-process exit code as a failed Packwiz installation. Prism selects the Windows GUI Java launcher, `javaw.exe`, and passes it as `$INST_JAVA`. The wrapper invokes it with PowerShell's call operator and checks `$LASTEXITCODE` without explicitly waiting for and obtaining the child process's exit code. In the observed Windows invocation, `$LASTEXITCODE` is null, so `$LASTEXITCODE -ne 0` is true. The wrapper throws `Packwiz installer failed ()` and itself exits 1 before Minecraft starts.

This is a launcher-wrapper regression, not evidence of a mod incompatibility. It does not establish that either pack would successfully enter the game after the wrapper is corrected.

## Introduction and why earlier builds did not hit it

Introducing commit: [`90d8e8e3927c2261470d346836b137fa3b97e22c`](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/commit/90d8e8e3927c2261470d346836b137fa3b97e22c), **2026-11-19 14:18:21 America/Phoenix**, `qa: enforce target, installed server and client inventory; audit exact missing candidate artifacts`.

That commit added `scripts/tmm_qa_install_verify.ps1` and changed the Prism builder's launch chain:

```text
Before:
Prism -> "$INST_JAVA" -jar packwiz-installer-bootstrap.jar -s client <frozen pack URL>

After:
Prism -> powershell.exe ... tmm-qa-install-verify.ps1 -JavaPath "$INST_JAVA" ...
      -> & $JavaPath -jar packwiz-installer-bootstrap.jar -s client $PackUrl
      -> if ($LASTEXITCODE -ne 0) { throw "Packwiz installer failed ($LASTEXITCODE)" }
```

Previously Prism managed the Java process directly. The new wrapper introduced a second process-management boundary and assumed PowerShell would supply a usable exit code for that same executable. The earlier installed November 18 source `f7aeb56a0f7b9c5957f922f5a397037f25207c76` still contains the direct Java pre-launch command. Mod-version changes are not needed to explain this transition.

The introducing commit is an ancestor of **both** failed frozen package sources, verified with `git merge-base --is-ancestor`. The installed verifier files are byte-identical to each other (SHA-256 `3ac6063029b1e36e474376d82b76c6c497d8797e7aea505e79ebf6ef6358f289`). The defective invocation and check remain present in both.

## Evidence from the actual installed packages

| Package | Frozen Packwiz / integration commit | Installed inventory result |
| --- | --- | --- |
| Full Integration UNSTABLE (U) | `f34a369d037bee0bab32069f50f9a558f4cd5e85` | 226 expected / 226 actual JARs; no missing or wrong-hash expected JARs |
| Four-Mod Update EXPERIMENTAL (E) | `69e1fd20f0c012f9ad4b1cc0095199a6fe4719ee` | 226 expected / 226 actual JARs; no missing or wrong-hash expected JARs |

Each inventory was checked read-only against its own installed `tmm-qa-expected-mods.json`, using that row's SHA-1/SHA-256/SHA-512 algorithm. Equal expected/actual counts plus every expected file's presence/hash establish the directory inventory for this check; this is not mod-loading acceptance. Java selected by Prism is Microsoft OpenJDK **21.0.7**, 64-bit.

The retained **U** Prism console directly showed the pre-launch invocation with `javaw.exe`, followed by this sanitized error:

```text
Packwiz installer failed ()
tmm-qa-install-verify.ps1:17
Process exited with code 1.
Pre-Launch command failed with code 1.
```

Neither instance had a Minecraft `logs/latest.log` or crash report at investigation time. This is consistent with failure before the actual game process starts. The owner's exit-1 report covers both packages; the retained console was directly inspected for U only. Attribution to E is supported by its same selected Java executable, byte-identical verifier, same defective call/check and independently verified inventory; a second E console was not captured. Do not describe a second console capture as performed.

## Minimal reproduction without Minecraft or Packwiz

Only the already-installed JDK's version query was invoked, from PowerShell; this did not boot the game, run Packwiz, download mods or touch a save:

```powershell
$global:LASTEXITCODE = $null
& '<selected-JDK>/bin/javaw.exe' -version
'exit-code set: ' + ($null -ne $LASTEXITCODE)
'existing guard rejects: ' + ($LASTEXITCODE -ne 0)

& '<same-JDK>/bin/java.exe' -version
'console Java exit-code: ' + $LASTEXITCODE
```

Observed: `exit-code set: False`, `existing guard rejects: True`, and `console Java exit-code: 0`. This demonstrates the Windows invocation/exit-code assumption, not a completed Packwiz run. The initial suggestion to substitute `java.exe` preceded the history investigation; it was **not applied** and is not the accepted permanent fix here.

## Why existing checks missed it

- `tests/test_prism_qa_packaging.py` was updated to assert that the PowerShell hook and verifier/inventory assets are packaged. It does not execute the Windows Java child-process chain or verify synchronization/exit-code behavior.
- `tests/test_qa_inventory.py` tests Python inventory/hash/side logic. It does not exercise the PowerShell wrapper.
- The inspected full-integration workflow's job runs on `ubuntu-latest`. Server smoke and package-content checks do not establish Windows graphical launch-hook behavior.

The missing regression boundary is therefore **Windows launcher -> PowerShell wrapper -> Java child completion and exit-status propagation**, not merely mod-roster validation.

## Work-slice handoff

### Implementation-ready / engineering follow-through

- [x] **Document root cause and provenance — implemented/source-ready.** Owner explicitly requested repository publication. This report links the introducing commit, both frozen sources, local inventory evidence and the no-game reproduction. No gameplay/launch PASS is claimed.
- [ ] **Packaging owner: correct process completion and exit-status handling — engineering follow-up, not implemented.** Preserve frozen-source checks, installer execution and complete missing/extra/hash inventory enforcement. Obtain a real completed child-process exit code; distinguish success, nonzero exit and process-start failure. Do not silence errors, default null to success, or remove the inventory guard. Exact implementation should be reviewed on its owning branch before rollout; this documentation request did not authorize editing either installed instance or dispatching a replacement build.
- [ ] **Add isolated Windows regression coverage — ready for engineering implementation alongside the fix.** Exercise a controlled child process with success, nonzero exit, delayed completion and start failure, plus executable/instance paths containing spaces. Cover both supplied `java.exe` and `javaw.exe` paths or an explicit supported normalization policy. Verify that inventory checks cannot run before installer completion and cannot be skipped after a failure. These are process/fixture tests, not a full modded boot.

### Explicit design blockers

None for the diagnosed defect: installer completion, accurate exit-code handling and fail-closed inventory enforcement are already the intended behavior. Selecting the implementation is engineering work, not a new mod/balance decision. No workaround is approved by this report.

### In-game/client testing queue

- [ ] **C / Client Launch Smoke — awaiting corrected helper inclusion or explicit local patch approval.** On an explicitly authorized disposable U launch, verifier waits for Packwiz, validates all expected JARs, and Prism reaches the actual Minecraft loader. Record exact helper/build identity and any subsequent independent mod-load error. Status: failed / follow-up needed for the currently diagnosed U package; corrected launch NOT RUN.
- [ ] **C / Client Launch Smoke — awaiting corrected helper inclusion or explicit local patch approval.** Repeat separately for E; do not infer its actual client result from U or from unchanged mods. Status: owner-reported failed / follow-up needed; corrected launch NOT RUN.
- [ ] **B/C gameplay acceptance — blocked by client launch.** Resume the corresponding frozen QA checklist only after a real client launch succeeds. No world creation, scripted B tests or gameplay C checks were performed in this investigation.

Related package/test handoffs: PR #476 (U/E differential QA) and PR #461 (four-mod experiment). Investigation is read-only, QA execution type A plus inspection of existing client-launch failure evidence. No fix, bypass, packaging/workflow change, instance modification or new launch is included.
