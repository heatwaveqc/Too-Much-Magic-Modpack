# Build channels and integrated testing

> **Living feature acceptance ledger (2026-11-14):** [112-item full feature checklist](FEATURE-CHECKLIST.md), with machine-readable [registry](../manifest/feature-checklist.json) and evidence-bounded player/test statuses. Update feature results using `scripts/feature_checklist.py`; a PASS on the server or a generated quest must never auto-check the relevant gameplay box. Proposed ProjectE gate transition: [two-stage design](PROJECTE-TIERED-GATING-PROPOSAL.md).

**Effective 2026-11-14.** This is the default standard for new test builds.

## Channels

- **Integration (UNSTABLE):** all classified, active testable implementation streams in a single Prism instance. The canonical branch is test/full-integration. Updates may fail or break gameplay.
- **Candidate (frozen):** a green full integration snapshot pinned to its exact Git commit and ZIP digest for owner testing. Never a floating branch reference.
- **Stable:** a deliberately accepted, immutable tagged release after both appropriate CI and in-world user tests. Never automatically promoted.

**Historic evidence:** B6 (ecddc4342c439a2b973ad16acff112b7d0a4bb60) was reported to launch client and generate a world; B8 and B12 had dedicated-server evidence, not complete client-and-mechanics acceptance. No balanced survival release is certified stable. Keep old working client snapshots and artifacts for rollback. The main branch is the accepted policy/bootstrap base, not the union of unverified changes.

## Requirements

1. All active implementation branches must be present in manifest/integration-sources.json or explicitly classified as already in the baseline, superseded, or non-runtime. Any new unclassified branch is a hard failure, not a quiet omission.
2. Each workstream declares an owning source branch and exact paths. Import only those paths, not every old file from older PR stacks. The combined quest/vampire QA branch provides the initial base and its latest fixes have priority.
3. A stacked source may supersede its ancestors: silver-equipment carries conventional material and Polymorph audit work; the no-Pufferfish Monk design branch carries the whole Monk scaffold. Do not duplicate skills or older implementations.
4. Additions to the manifest are reconciled by exact mod ID and version. Fail on conflicting IDs. Only config, KubeJS and pinned mod metadata enter the packwiz instance; separate first-party source projects are built as JARs.
5. Record source commits, actual module hashes, mod count, known pending work, and exact built commit in every ZIP. No third-party mods are redistributed.
6. Run static packwiz, quest, material and equipment validation; compile and unit-test first-party modules; boot the full hash-pinned dedicated server with **both** custom modules actually copied into mods; then test the resulting client in a new disposable Prism world.
7. A failing or conflicting source **blocks publication**, leaves the last known-good build alone, and saves logs. A server-ready line is not a successful gameplay test.
8. For candidates and stable builds, collect client launch, disposable-world generation, important recipes, coin claims, vampire dual acquisition and blood, Monk registration, compatibility conflicts, and progression safety gates. Record pass/fail and outstanding issues in the test ledger. Never auto-upgrade existing worlds.
9. Do not claim planned gameplay is live. Tyz 6.4.2's MXP-only XP architecture, universal magic and form trees, replacing Pufferfish Skills, and actual Monk powers are still pending until installed and validated.

## Routine

Use GitHub Actions **Full Integration - Unstable Prism QA**, or its default-branch launcher, to incorporate the current tracked branch heads. After green CI the artifact **Too-Much-Magic-Full-Integration-Prism-UNSTABLE** is the single Prism download for that iteration. Use a fresh disposable world, report issues against the exact SHA in BUILD-MANIFEST.json, and preserve the old Prism profile.

When a contributor creates a new feature branch, register the source paths and owner in manifest/integration-sources.json or document why it is excluded. Do not silently mix competing skill engines, revert newer quest localization, or relabel a Pufferfish-era snapshot as Tyz-enabled.

## Corrected test limitation

The previous quest/vampire workflow passed SMOKE_EXTRA_MOD_JAR to scripts/smoke_server.sh, but that script ignored the variable. That old server PASS therefore does not establish that the custom vampire JAR loaded. The full integration runner explicitly validates every first-party JAR's presence and byte-identical copy into the server mods directory before runtime smoke.

## Promotion

A passing integrated CI result may be **frozen as Candidate** by preserving its commit SHA and exact ZIP checksum. **Stable** promotion must be a separate explicit, human-approved tagging operation with relevant in-world test evidence. Neither stable nor candidate refs should automatically move when an experimental branch changes.

## Tester-only Prism preferences

The full-integration **Windows QA ZIP** seeds a minimal 1.21.1 `options.txt` and uses local Prism pre-load/post-exit scripts to carry keybindings, GUI/chat scale and audio volumes across separate imported test instances. Its persistent profile is kept outside each instance under `%LOCALAPPDATA%\\TooMuchMagic\\TestPreferences`; no personal data is sent to GitHub. The old-instance capture helper is optional. **Neither these defaults nor the hooks are allowed in a public/stable distribution.** Release candidates intended for other players must omit these files and launcher commands; see [the test-preferences contract](PRISM-TEST-PREFERENCES.md). The personal QA sync is only a testing convenience, not a replacement for fresh-world regression tests or pack config review.
