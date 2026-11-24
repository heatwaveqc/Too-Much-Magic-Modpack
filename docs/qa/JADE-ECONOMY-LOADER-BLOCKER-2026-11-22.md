# Experimental client blocked by Economy's Jade version constraint

Status: **C / Client Launch Smoke FAILED at NeoForge dependency validation**. Owner opened the client and requested inspection/reporting. The investigator observed the existing screen, then ended screen access and reset the computer-use session. No clicks in Minecraft, new launch, world creation, JAR edits, dependency bypass or replacement build were performed.

## Observed build and blocking evidence

- Instance: Four-Mod Update EXPERIMENTAL, Minecraft 1.21.1 / NeoForge 21.1.265.
- Installed `BUILD-MANIFEST.json` integration commit: `c926c694cce89688c5075a38c1b0763f577f26e9`; baseline `51ff72e10fa158b4ba80d1d189ee023891f30d98`.
- Economy JAR: `tmm_economy-0.1.0-dev.jar`, SHA-256 `0de04b90e62b4a8493bf5d3402661cc0426bbd3aa9acb80df25e9560ec79095c`, matching its packaged BUILD-MANIFEST entry.
- Jade JAR: `Jade-1.21.1-NeoForge-15.10.6.jar`. Its mod metadata uses `${file.jarVersion}`; its JAR manifest declares `Implementation-Version: 15.10.6+neoforge`.

The existing screen showed one mod-loading error: Economy only supports Jade 15.10.6, while the installed version is 15.10.6+neoforge. The precise sanitized `latest.log` dependency line is:

```text
Mod ID: 'jade', Requested by: 'tmm_economy', Expected range: '[15.10.6,15.10.6]', Actual version: '15.10.6+neoforge'
```

The economy JAR's actual `META-INF/neoforge.mods.toml` contains:

```toml
[[dependencies.tmm_economy]]
modId="jade"
type="optional"
versionRange="[15.10.6]"
ordering="AFTER"
side="CLIENT"
```

The investigator's initial spoken reading of 15.10.2 was corrected after reading the actual log/JAR metadata; the verified required base version is **15.10.6**. No screenshot or full log is committed.

## Traced cause

Introducing source commit: [`4d75e99cb23ea83eb841bfc8df24d6a1e7fa5d42`](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/commit/4d75e99cb23ea83eb841bfc8df24d6a1e7fa5d42), **November 19, 2026, 11:21:27 America/Phoenix**, `Standardize Jade conventional power display to OP and survey exact pinned UI paths`.

It added the exact `[15.10.6]` client-side Jade constraint to `compat/economy/src/main/resources/META-INF/neoforge.mods.toml`. The commit is an ancestor of the installed source, verified with `git merge-base --is-ancestor`. Both the current source and packaged Economy metadata retain that constraint.

This is a mismatch between a hardcoded exact dependency declaration and the provider's **actual loader-visible qualified version**, not proof that Jade's API changed or that the downloaded JAR is corrupt. The filename/base release number alone is insufficient for an exact version declaration. `optional` permits absence; it does not make an installed incompatible version acceptable. The observed loader explicitly rejects this installed combination.

The same integration also supplies `JadeOpDisplayMixin`, targeting `snownee.jade.api.view.EnergyView.read(CompoundTag, String)` to change conventional FE/RF display units to OP. The mixin is in the client list of `tmm_economy.mixins.json`, whose `defaultRequire` is 1. Correcting dependency metadata therefore does not establish that this exact client integration subsequently loads or behaves correctly; the next stage still needs testing.

This is a **different, later launch stage** from the PowerShell pre-launch exit-code blocker documented in `PRISM-PRELAUNCH-EXIT-1-2026-11-22.md` / PR #488. The observed launch reached the NeoForge error UI. It does not prove that all pre-launch corrections are generally accepted or that U has the same new failure: only the running E instance was inspected for this report.

## Required follow-through and evidence limits

### Implementation-ready slices

- [x] **Root-cause report — implemented/source-ready.** Owner authorized publication; installed source/JAR metadata, manifest hash, dependency rejection and introducing commit documented. No runtime fix applied.
- [ ] **Economy/package owner: reconcile the Jade dependency with the actual pinned provider version.** Engineering follow-up, not implemented. Preserve intended narrow compatibility and verify the exact Jade client API before selecting an exact qualified version or a justified range. Do not remove the dependency check or arbitrarily widen it merely to pass the loader. Rebuild and reconcile first-party artifact provenance through the designated integration writer when separately authorized.
- [ ] **Add an offline dependency-closure regression check.** Ready engineering work: compare bundled first-party dependency constraints against selected provider JARs' effective metadata versions, including `${file.jarVersion}` expansion and qualifiers such as `+neoforge`. Include this exact optional-but-present/client-only rejection fixture. Filename strings, successful compilation, server-only smoke and matching file hashes do not cover this boundary. The check must use NeoForge-compatible version semantics or explicitly report unsupported comparisons.

### Explicit design blockers

None for the diagnosed metadata mismatch. The intended OP-only conventional-energy display behavior is already implemented in source. Exact compatibility validation and version-constraint selection are engineering investigation; no new balance or mod-inclusion decision is inferred.

### In-game test queue

- [ ] **C / E Client Launch Smoke — failed / follow-up needed.** After a separately authorized corrected artifact is included, confirm dependency validation passes and Economy's Jade client mixin applies without injection errors. Corrected launch NOT RUN.
- [ ] **C / conventional power display — awaiting corrected inclusion/build.** Inspect current and capacity values on a real FE/RF provider; expect OP label only, unchanged measured values and no relabeling of unrelated magical resources. NOT RUN.
- [ ] **C / U client — NOT RUN for this defect.** Inspect U separately if that package includes the same Economy/Jade metadata. Do not infer a U screenshot/result from E.

Related workstreams: #322 (Economy), #464 (valuation/Jade gating), #476 (U/E differential QA). This is reporting only: no generated worlds, raw logs, screenshots, mod JARs, private settings, game fixes, automated boot or package dispatch are included.
