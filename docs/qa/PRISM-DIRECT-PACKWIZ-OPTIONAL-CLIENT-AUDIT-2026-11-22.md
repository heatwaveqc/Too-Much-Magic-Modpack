# Prism QA: direct Packwiz launch and optional client inventory audit

Approved 2026-11-22 by the owner for the **next** Too Much Magic test build, with **no build or smoke run authorized** by this change. Background: PR #488 (the Windows PowerShell prelaunch failure) and the subsequent Chrome dangerous-download report for the experimental ZIP.

## Change

- The Prism builder uses the original direct launcher mechanism, `"$INST_JAVA" -jar packwiz-installer-bootstrap.jar -s client <immutable SHA-pinned pack.toml URL>`, not the PowerShell wrapper.
- Do not package or call `tmm-qa-install-verify.ps1`. Leave the legacy source and its regression fixture in the repository for investigation of older frozen packages, not for new clients.
- Continue to package `tmm-qa-expected-mods.json` with the exact hash-pinned client JAR roster and first-party module JAR hashes, plus the frozen source SHA in `BUILD-MANIFEST.json`.
- The standalone `scripts/audit_client_inventory.py` reads that packaged JSON and hashes the *installed* `mods/*.jar` files on demand, without running Packwiz, PowerShell, Minecraft or modifying the instance. Run it from a TMM checkout against a local Prism game's `.minecraft` directory:

```powershell
py -3 scripts/audit_client_inventory.py "C:\\path\\to\\Prism instance\\.minecraft"
```

- Preserve the existing personal-options preference synchronization, its separate pre-load/post-exit hooks, first-party module hashes, packaged bootstrap verification, immutable source, and single-level Prism archive structure. These optional preference helpers remain an independent PowerShell component; this change does **not** claim to eliminate all executable content or resolve Chrome's classification.
- The server smoke continues to verify exact installed server JAR inventory and hashes. Packwiz continues handling client downloads from the frozen, hash-pinned index; the full client installation audit is deliberately optional.

## Regression / acceptance

- [x] **Source implemented:** direct Prism Packwiz prelaunch and fail-closed packaging assertion forbidding the old launcher wrapper, without relaxing package-time JAR validation.
- [x] **Source implemented:** optional read-only client audit, including manifest/hash validation and missing/extra/altered detection; no executable audit file bundled in client ZIP.
- [x] **Source implemented:** offline packaging fixture changed to require direct, immutable prelaunch and reject legacy bundled wrapper; separate no-network audit fixtures.
- [ ] **Type A:** verify current PR quick-CI passes (`Verify bootstrap`, offline Prism packaging fixtures, relevant Integration Preflight); fixture ZIP tests do not constitute a player download or a full build.
- [ ] **C / client acceptance, later owner-authorized:** with a new disposable Prism package, verify direct Packwiz installs client-only and both-side dependencies, starts Minecraft and does not invoke the old wrapper; verify manual inventory audit after normal launch; verify saved preferences remain available. No live game evidence claimed.
- [ ] **Security follow-up:** the Chrome dangerous-download designation is *not* proven caused by the removed verifier. Independently scan the final new ZIP, confirm GitHub artifact SHA-256/provenance and observe Chrome's classification before broad distribution.

## Workstream and ownership

This is QA framework/packaging source only, offered for designated integration-writer review into `test/full-integration`. No new mods, changed version pins, first-party Java gameplay, `manifest/mods.json`, `pack.toml` or `index.toml` changes. No design blockers. The next authorized build automatically picks up the updated builder **after reviewed integration**; previously frozen artifacts remain unchanged.
