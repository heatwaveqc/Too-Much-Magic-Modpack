# Build channels and integrated testing

> **Living feature acceptance ledger (2026-11-14):** [112-item full feature checklist](FEATURE-CHECKLIST.md), with machine-readable [registry](../manifest/feature-checklist.json) and evidence-bounded player/test statuses. Update feature results using `scripts/feature_checklist.py`; a PASS on the server or a generated quest must never auto-check the relevant gameplay box. Proposed ProjectE gate transition: [two-stage design](PROJECTE-TIERED-GATING-PROPOSAL.md).

**Effective 2026-11-14.** This is the default standard for new test builds.

**Canonical test names:** [Repository CI, Module CI, Integration Preflight, Integration Assembly, Server Boot Smoke, Prism QA Package, Client Launch Smoke and Gameplay Acceptance](qa/TEST-LEVELS-AND-TERMINOLOGY.md). "Quick CI" means the first three; assembly, server boot and packaging have separate proof and explicit manual authorization. A Prism ZIP existing does **not** prove client launch.

## Progression subsystem branch pipeline (2026-11-16)

**Progression is one code-owning module; Monk, Hunter, Gilded, etc. are configuration-only paths.** The shared Progression engine owns **all** Java/API/mixin/event/network/effect-handler/executable-test changes. A class branch may only change its own declarative path/skill/Tyz JSON, datapack tags/assets, localization, docs and non-executable fixtures. Class-specific runtime code, including KubeJS scripts, is not an acceptable shortcut; an engine change belongs in a separate `feature/progression-*` PR.

Use `dev/progression` as the coherent rolling subsystem branch. Engine PRs and config-only class PRs branch from it and target it; the designated Progression writer reviews their combinations. A missing engine capability may be developed in parallel and a config PR temporarily stacked on its PR. Promote coherent, frozen `dev/progression` commits to `test/full-integration` by a reviewed PR under the existing one-writer, SHA provenance, and quick CI rules. Keep `dev/progression` synchronized with relevant current integration changes, but never blindly restore old paths or overwrite the latest baseline. **Promotion does not itself authorize Assembly, Server Boot Smoke or Prism QA Package.**

This is the required branch/ownership procedure: [Branch coordination and configuration-owned Progression paths](BRANCH-AND-PROGRESSION-WORKFLOW.md). Current historical PR stacks may not comply and must be inventoried/migrated individually rather than automatically rebased, merged or deleted.

## Channels

- **Integration (UNSTABLE):** all classified, active testable implementation streams in a single Prism instance. The canonical branch is test/full-integration. Updates may fail or break gameplay.
- **Candidate (frozen):** a green full integration snapshot pinned to its exact Git commit and ZIP digest for owner testing. Never a floating branch reference.
- **Stable:** a deliberately accepted, immutable tagged release after both appropriate CI and in-world user tests. Never automatically promoted.

**Historic evidence:** B6 (ecddc4342c439a2b973ad16acff112b7d0a4bb60) was reported to launch client and generate a world; B8 and B12 had dedicated-server evidence, not complete client-and-mechanics acceptance. No balanced survival release is certified stable. Keep old working client snapshots and artifacts for rollback. The main branch is the accepted policy/bootstrap base, not the union of unverified changes.

## Requirements

1. **Live-test-ready inclusion is mandatory to attempt:** any chat/PR/branch handoff reporting a feature ready for live testing enters the next comprehensive unstable integration scope, even if unmerged or previously marked `deferred_pending_runtime`. Inventory and reconcile all reported-ready features using [the inclusion register](qa/LIVE-TEST-INCLUSION-REGISTER.md). A prior narrow test's deferral is not an exclusion. All active implementation branches must also be present in `manifest/integration-sources.json` or correctly classified; a new unclassified branch is a hard failure.
2. Each workstream declares an owning source branch and exact paths. Import only those paths, not every old file from older PR stacks. The combined quest/vampire QA branch provides the initial base and its latest fixes have priority.
3. A stacked source may supersede its ancestors: silver-equipment carries conventional material and Polymorph audit work; the no-Pufferfish Monk design branch carries the whole Monk scaffold. Do not duplicate skills or older implementations.
4. Additions to the manifest are reconciled by exact mod ID and version. Fail on conflicting IDs. Only config, KubeJS and pinned mod metadata enter the packwiz instance; separate first-party source projects are built as JARs.
5. Record source commits, actual module hashes, mod count, known pending work, and exact built commit in every ZIP. No third-party mods are redistributed.
6. **Default, automatic quick preflight:** run static packwiz, quest, material and equipment validation, plus first-party module compilation/unit tests; no complete mod downloads, NeoForge server boot, world generation, immutable server-tested tags or Prism QA ZIP. **Full smoke only after an explicit owner instruction for that run:** boot the full hash-pinned dedicated server with **both** custom modules actually copied into mods, and only then produce an immutable QA ZIP for a separately requested client test in a disposable Prism world.
7. A failing or conflicting source **blocks publication**, leaves the last known-good build alone, and saves logs. A server-ready line is not a successful gameplay test.
8. For candidates and stable builds, collect client launch, disposable-world generation, important recipes, coin claims, vampire dual acquisition and blood, Monk registration, compatibility conflicts, and progression safety gates. Record pass/fail and outstanding issues in the test ledger. Never auto-upgrade existing worlds.
9. Do not claim planned gameplay is live. Tyz 6.4.2's MXP-only XP architecture, universal magic and form trees, replacing Pufferfish Skills, and actual Monk powers are still pending until installed and validated.

## Live-test readiness intake and no silent deferrals (owner decision, 2026-11-16)

Before **every** comprehensive live-test candidate, the integrator must read development-chat handoffs and open feature PRs, not just the preselected source-map streams. "Ready for a live test," "ready for next test build," and comparable statements require a documented **attempt to include** that feature. Record each in [docs/qa/LIVE-TEST-INCLUSION-REGISTER.md](qa/LIVE-TEST-INCLUSION-REGISTER.md), including owner, source SHA, runtime destinations/pins, and actual status. A PR not merged to `test/full-integration` is still in scope if its work was reported ready.

`deferred_pending_runtime` only explains an earlier snapshot. **It cannot satisfy the next-test inclusion obligation by itself.** Attempt pin resolution, module builds, data/quest merges and conflict repair; inspect the final assembled files/JARs so imported source is not silently overwritten or removed. If compatibility, dependency, missing code, or conflicting behavior blocks inclusion, record the specific failed attempt and evidence. **Only an explicit, feature-specific owner decision** authorizes voluntary deferral.

Do not call a build a comprehensive or all-workstreams live test until every reported-ready feature is **INCLUDED**, **BLOCKED with evidence**, or **OWNER-DEFERRED** in the preflight inclusion matrix. If anything is BLOCKED, present the omission and obtain owner approval before issuing a narrower *partial* live-test candidate. Preserve the full-smoke authorization rules in the following section. A chat asking to prepare this register or update guidance is not authorizing a Minecraft server boot.

## Routine: lightweight checks by default, full smoke by explicit request only

The **Full Integration** workflow defaults to quick CI on tracked integration-branch pushes and ordinary manual/reusable invocations. It **does not assemble or restore donor sources on the automatic path**: it reads frozen source refs, validates metadata/resources, checks potential overrides and draft branches, and compiles/tests first-party modules already present on the baseline. **A successful quick run does not establish that external donor work is assembled, start Minecraft, establish mod interoperability, freeze a tested snapshot, or publish a Prism QA ZIP.** Report `QUICK CI PASS; ASSEMBLY AND SERVER NOT TESTED`.

`Integration Assembly` is a distinct, explicitly authorized manual path: check `assemble_only=true` to stage sources and run quick tests **without** a server boot or Prism QA ZIP. The full `full_smoke=true` authorization implies assembly followed by the named Server Boot Smoke and (only if server boot passed) the Prism QA Package. Neither option may be auto-triggered.

The owner must explicitly request a **particular full dedicated-server smoke run** before an agent is allowed to start one. For an authorized run, dispatch `.github/workflows/full-integration.yml` with the **`full_smoke` boolean checked/true** (or pass the same true value from an explicitly owner-authorized reusable launcher). This unlocks the full server boot, immutable integration tag, and **Too-Much-Magic-Full-Integration-Prism-UNSTABLE** artifact. The checkbox defaults to false. Do not use an unguarded old feature workflow or direct `scripts/smoke_server.sh` to evade this restriction. Do not automatically retry failures. An already-running overnight test approved on 2026-11-15 is not canceled by this future-run rule.

For the resulting full-test artifact, use a fresh disposable world, report against the exact SHA in BUILD-MANIFEST.json, and preserve the old Prism profile. Client/world playtesting still requires its own evidence.

When a contributor creates a new feature branch, record it as WIP or ready; a WIP is reported by the read-only preflight but does not block unrelated quick checks. Once declared ready, register the source paths, exact frozen commit and owner in manifest/integration-sources.json or document the evidence-based blocker; historical deferrals are not waivers. After a feature merges, point its stream at the baseline rather than the deleted PR branch, and never restore an older donor directory over newer baseline files. Do not silently mix competing skill engines, revert newer quest localization, or relabel a Pufferfish-era snapshot as Tyz-enabled.

## Corrected test limitation

The previous quest/vampire workflow passed SMOKE_EXTRA_MOD_JAR to scripts/smoke_server.sh, but that script ignored the variable. That old server PASS therefore does not establish that the custom vampire JAR loaded. The full integration runner explicitly validates every first-party JAR's presence and byte-identical copy into the server mods directory before runtime smoke.

## Promotion

Only an **owner-authorized full-smoke-passing integrated CI run** with a produced QA ZIP may be **frozen as Candidate** by preserving its commit SHA and exact ZIP checksum. A quick green CI run is not a candidate. **Stable** promotion must be a separate explicit, human-approved tagging operation with relevant in-world test evidence. Neither stable nor candidate refs should automatically move when an experimental branch changes.

## Tester-only Prism preferences

The full-integration **Windows QA ZIP** seeds a minimal 1.21.1 `options.txt` and uses local Prism pre-load/post-exit scripts to carry keybindings, GUI/chat scale and audio volumes across separate imported test instances. Its persistent profile is kept outside each instance under `%LOCALAPPDATA%\\TooMuchMagic\\TestPreferences`; no personal data is sent to GitHub. The old-instance capture helper is optional. **Neither these defaults nor the hooks are allowed in a public/stable distribution.** Release candidates intended for other players must omit these files and launcher commands; see [the test-preferences contract](PRISM-TEST-PREFERENCES.md). The personal QA sync is only a testing convenience, not a replacement for fresh-world regression tests or pack config review.
