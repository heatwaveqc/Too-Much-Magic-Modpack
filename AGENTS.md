# Too Much Magic — agent contract

This file governs every automated contributor and pack change.


## Pre-stable save compatibility (owner decision, 2026-11-18)
No persistent user worlds exist as of this decision. Until the first **stable release**, disposable QA worlds and player data may be invalidated/reset by intentional schema, ID, registry or engine changes. Do not spend work on legacy save conversion, dual schemas, Tyz rank import, or backward-compatibility bridges solely to preserve pre-stable saves. Current saves still need correct persistence across normal logout, death and server restart on a single build. Never silently delete user saves; explicitly label reset requirements. At first stable release establish versioned schema, migration and compatibility obligations. This policy does not waive source provenance, integration, testing or safe runtime boundaries.

## Progression architecture and branch ownership (owner decisions, 2026-11-16 and 2026-11-18)

**Locked target (2026-11-18):** consolidate MXP and selectively reused MIT-licensed Tyz functionality into `tyr_progression`; one built-in MXP/SP economy plus an extensible namespaced registry of JSON-defined independent XP systems (no fixed slot limit), tree-level default and per-skill currency overrides, XP source ownership independent of trees, ten declarative pack trees including one shared Lich tree, no bespoke composite trees. Preserve in-scope player-effect expressiveness and add enhanced night/lava vision. All first-party mods target MIT with upstream license/provenance preserved. See [unified engine target](compat/progression/design/UNIFIED-PROGRESSION-ENGINE-TARGET.md). Current source/modules remain until reviewed runtime replacement and QA; do not remove dependencies or trigger builds on the strength of this design alone.

**Player-only ownership (owner decision, 2026-11-18):** Progression supports innate/automatic/form-granted abilities and player-owned skill tree unlocks/ranks only. Research, item/resource costs, rituals, timed prerequisites and permanent installation-themed unlocks are flavors of acquisition for the same player-owned nodes, not an independent augment inventory or ownership type. A learned ability may be toggled inactive without changing ownership; configured skill-tree respec remains distinct from detachable traits. No equipment-owned effects, item affixes, gem/perk sockets, transferable/collectible ability tokens, uninstall/extraction/refund systems for removable installed traits, or ability trading/PvP theft. Equipment/item conditions for player-owned effects remain allowed; do not alter other mods' native equipment systems. Reference mods are donors of useful player-effect algorithms and acquisition/graph mechanics only, not full ownership parity. This supersedes earlier broad parity targets.

**One coded Progression engine; configuration-only paths.** All executable functionality for Monk, Hunter, Gilded and future disciplines belongs to `compat/progression` / `tyr_progression`: Java, APIs, registration, event handlers, mixins, effects, networking, adapters and executable tests. Path workstreams author **only declarative JSON/datapacks, skill-tree/config JSON (legacy Tyz format until migration), tags/data assets, localization, docs and non-executable fixtures**. They do not own Java modules/classes, KubeJS/JS/Python gameplay code, or path-specific runtime implementations. Prefer generic data-driven handlers; a feature required by only one path is still implemented and maintained by Progression, not that path's branch. Historical path-specific Java is legacy migration work, not permission for new code ownership.

**Mandatory branch flow:** `feature/progression-*` for any engine/API/handler/schema code and `feature/monk-*`, `feature/hunter-*`, `feature/gilded-*` (and analogous paths) for configuration-only work. All branch from the coherent shared `dev/progression` baseline and merge there by PR, with a single designated Progression integration writer. Class/feature agents never push directly to `dev/progression` or `test/full-integration`. If a class needs a new engine capability, split it into a separate Progression PR; the same chat may create both and stack the config PR on the core PR temporarily. Promote reviewed, coherent Progression commits to `test/full-integration` via its one integration writer, with immutable source provenance and the existing owner-authorized QA gates. `main` remains the accepted bootstrap/policy base.

**Recovery and concurrency:** before creating branches, discover existing Issues, PRs, branches and integrated code; reuse existing workstream identity, preserve exact commit/dependency/edited-path/test/next-step handoffs in the PR, and do not start duplicate implementations. One writer per shared branch; feature agents own only isolated PR branches. Synchronize feature branches from `dev/progression` and reconcile relevant newer `test/full-integration` changes back into `dev/progression` through review. Never blindly reapply old whole-directory snapshots, force-rebase shared branches, or delete branches still referenced by frozen sources. Classify existing legacy branches before migrating them.

**Procedure:** [Branch coordination and configuration-owned Progression paths](docs/BRANCH-AND-PROGRESSION-WORKFLOW.md). This policy governs future work; it does not assert the legacy Monk/Hunter code has already been refactored or that a source merge constitutes gameplay validation.

## Next-test QA handoff for Codex

For the current test build, start with [docs/qa/README.md](docs/qa/README.md), follow the [Codex-ready three-tier checklist](docs/qa/NEXT-TEST-CODEX-CHECKLIST.md), and record results in [the current QA status ledger](docs/qa/CURRENT-TEST-STATUS.md). Re-read the exact integration SHA and source manifest before each test; planned and source-ready features are not necessarily installed. Reuse [the historical test ledger](docs/TESTING.md) and [canonical acceptance register](manifest/feature-checklist.json), and do not mark in-world PASS based on static/compile evidence.

The **full smoke authorization rule below remains in force**. Uploading or following a checklist does not authorize a full server boot, a full-smoke workflow invocation, an automatic push-triggered smoke, or a Prism build. Run quick source/static/unit checks by default; request explicit authorization for any full smoke.

## Integration ownership, frozen sources, and merge lifecycle (owner decision, 2026-11-16)

**One integration writer; many feature agents.** Feature agents work on isolated PR branches. They may propose source selections, but must not silently replace `test/full-integration` shared runtime/quest/config trees, `manifest/integration-sources.json`, `manifest/mods.json`, or packwiz `index.toml` / `pack.toml`. The integration writer reconciles these with current HEAD, explicit path ownership and an immutable donor revision. Re-fetch HEAD and review concurrent edits before every write.

**Merged is not automatically integrated.** Every external donor stream in `manifest/integration-sources.json` must carry an immutable full 40-hex `source_commit`. Do not use a moving `origin/feature/*` head to stage a candidate. When a PR is merged, mark its relevant stream `integrated_into_baseline: true`, switch `branch` to `test/full-integration`, and retain `merged_from`, `merged_pr`, and `merged_commit` for provenance. Verify every prior reference before deleting the feature branch. GitHub's auto-delete-on-merge may remain enabled; temporary branch names are not durable build dependencies.

**Do not replay stale directories.** An already-merged baseline path must not be restored from an old branch/commit. A new donor that differs from a tracked baseline file requires a specific `approved_overrides` path/pattern and meaningful explanation; otherwise assembly must fail before any writes. Never silently replace the latest starter quests, Monk/Hunter Java, Tyz overlays, config or packwiz metadata with older feature snapshots.

**Mandatory lightweight merge validation.** Never use `[skip ci]` or equivalent on changes to integration sources, workflows, build scripts, packwiz, runtime code, or shared data. Before merging, run/obtain Repository CI, Module CI where applicable, and the read-only Integration Preflight: `python scripts/validate_integration_sources.py`. Run it on a clean canonical-LF checkout (see `.gitattributes`); check source references, duplicate path owners, overwrite warnings and indexed hashes. `scripts/assemble_integration.py` may run **only after explicit authorization for assembly**, and only after strict preflight. Do not invoke a self-merge of an advancing integration branch.

**Draft discovery is not test inclusion.** Routine preflight reports unknown WIP branches without failing unrelated quick CI. When a chat or PR calls a feature ready for live testing, register and reconcile it under the mandatory inclusion rule below; do not silently defer it. A comprehensive test cannot be called complete with unaccounted ready items.

**Test vocabulary:** use [the canonical eight levels](docs/qa/TEST-LEVELS-AND-TERMINOLOGY.md): Repository CI, Module CI, Integration Preflight (all automatic/read-only), Integration Assembly (manual), Server Boot Smoke (owner-authorized), Prism QA Package (owner-authorized), Client Launch Smoke, and Gameplay Acceptance. "Quick CI" means the first three only. Package creation is not a client PASS. Never report an unrun level as successful.

**Branch protection:** GitHub repository settings must require the lightweight `Verify bootstrap / metadata` check for `test/full-integration` PRs and block direct writes, with no server smoke or Prism build as a required check. The settings are not implemented by this text alone; use [the admin setup checklist](docs/qa/BRANCH-PROTECTION-SETUP.md).

## "Ready for a live test" = mandatory next-test inclusion attempt (owner clarification, 2026-11-16)

A contributor/chat saying **"ready for a live test"**, **"ready for next test build"**, or equivalent makes that feature **in scope for the next owner-authorized comprehensive unstable live-test build**. This applies to unmerged PRs, separate feature branches, KubeJS/config changes, mod pins, quests, and first-party JARs; it is not limited to changes already copied onto `test/full-integration`. **Another chat's narrower task, an old source-only label, or an entry in `deferred_pending_runtime` is NOT authorization to omit a ready feature.**

- **At the readiness declaration:** register the ready feature, source branch/PR, exact code/asset or pins, dependencies and acceptance test in [the live-test inclusion register](docs/qa/LIVE-TEST-INCLUSION-REGISTER.md). If an earlier chat omitted registration, the next QA assembler must recover the readiness claim from handoffs/PRs and register it; missing registration is not an exclusion.
- **Before the next comprehensive test:** independently inventory all readiness handoffs, compare them to all active feature/fix branches and `manifest/integration-sources.json`, and attempt to import/build/pin **every ready feature** in a unified disposable integration candidate. Reconcile conflicting branches/file owners; do not default to the old retirement-only source selection. Confirm inclusion in the *assembled runtime*, not merely a source branch or design document.
- **When a feature cannot be included:** record the attempted actions, precise blocker, evidence, next fix and impact in that register and the QA ledger. Only the owner's explicit feature-specific decision permits voluntary deferral. A blocked feature remains visible and is not silently declared "not ready." A partial test may proceed **only if the owner approves the narrower scope**, clearly listing omissions.
- **Before declaring an all-ready-features test build:** publish a per-feature INCLUDED/BLOCKED/OWNER-DEFERRED reconciliation matrix, including all previously `deferred_pending_runtime` runtime candidates. If ready items remain blocked or unaccounted for, do not call the build comprehensive.

This is a **scope/inclusion requirement**, not permission to run or trigger a full modded NeoForge smoke, client test, or build. The owner-authorization rules below still apply.

## Mod comprehension research

When asked to "work on the mod comprehension project", read [the entry point](MOD-COMPREHENSION.md), [Work instructions](docs/mod-comprehension/WORK-CHAT-INSTRUCTIONS.md) and [coverage index](docs/mod-comprehension/INDEX.md). Autonomously continue a substantive single-mod profile or cross-mod interaction report, with exact-version sources and honest verification status. Research only: no unsolicited quest/config/KubeJS/gameplay changes, no copied JARs/assets, and no guesses about runtime compatibility. Every substantial profile must ultimately map **full native progression and guidebook access** to a complete player walkthrough; read [QUEST-WALKTHROUGH-STANDARD.md](docs/QUEST-WALKTHROUGH-STANDARD.md). A mechanics survey with mandatory tier/how-to gaps is still researching.

## Hard baseline
- Minecraft 1.21.1, NeoForge 21.1.265 (provisional until launcher/installer verification), Java 21.
- Git tracks source, config, packwiz metadata and verification evidence. NEVER commit downloaded copyrighted mod JARs, caches, generated worlds, saves, logs, credentials, or EULA files.
- `manifest/mods.json` is the canonical decision roster; `pack.toml` / `index.toml` are the canonical **installed** packwiz lock. A listed mod is NOT active until a checked-in `mods/*.pw.toml` exists and the index references it.
- Keep excluded mods out; never silently replace a mod. For every unavailable mod, record a blocker and separately propose a substitute awaiting approval.
- Do not upgrade Java, Minecraft, loader or mods by guesswork or because the word "latest" appears on a page. Match all versions to MC 1.21.1 specifically.
- Preserve known gameplay decisions in docs/PROGRESSION.md and docs/DESIGN.md. Do not implement progression in the bootstrap stage.

## Unbreakable exclusions
No Create, original Blood Magic, Sophisticated Storage, generic backpacks, generic item logistics, Applied Energistics 2 (AE2), Refined Storage, FTB Ultimine. **No Re:Avaritia or Extended Crafting** (owner rejected competing Infinity-tier endgame after the earlier on-hold status was accidentally ignored). No generic FE transfer mod: Draconic Evolution supplies the high-end energy movement.
Never add the Ring of the Seven Deadly Sins to any normal acquisition route.

## No unattended full builds or Prism packages (owner decision, 2026-11-16)

- **All automatic full NeoForge smoke tests, world boots, full pack builds, and Prism ZIP/package publication are prohibited** on push, pull request, or schedule. Do not restore legacy push-triggered smoke workflows when copying or rebasing a branch. The former `main` scheduled full-integration launcher is now dispatch-only.
- Automatic CI may keep quick static checks, compilation, and isolated tests **without** a full server boot or complete Prism/release artifact. A manual build/smoke needs **fresh owner approval for that run** plus the opt-in `full_smoke: true`; packaging-only tasks need an equivalent opt-in `authorized_build: true`.
- Include a job-level approval gate, not just a human instruction. Condition example: `if: ${{ github.event_name == 'workflow_dispatch' && inputs.full_smoke == true }}`. If joining the gate to branch matching, parenthesize every `||` clause first; otherwise some alternatives bypass authorization.
- Older workflows have been corrected on their active source branches. Before introducing a new feature branch or promoting a historical workflow, audit its `.github/workflows` for copied auto-smoke or auto-packaging steps. **Do not indirectly trigger an unguarded old workflow by pushing first.**

## Full smoke test authorization (owner decision, 2026-11-15)
- **Routine agent development and CI must use lightweight validation by default:** source/manifest/hash/quest/config checks, syntax and schema checks, Java compilation, and unit tests. Report exactly what ran. A quick green check is not a server/client PASS.
- **Never start, schedule, automatically trigger, retry, or indirectly launch a full modded NeoForge server smoke test without an explicit owner instruction for that run.** This includes `scripts/smoke_server.sh` with the full mod roster, GitHub Actions workflow dispatch/call, or pushing into a branch with an automatic full-smoke workflow. A request to implement, merge, research, prepare a test plan, check readiness, or create a test build does **not** itself authorize a full server smoke. Ask for authorization when needed.
- The active `.github/workflows/full-integration.yml` performs quick checks on push and by default on dispatch/call. Its `full_smoke: true` input is allowed **only following explicit owner approval** for that particular full server boot and Prism QA artifact. Agents must not set this flag on their own or bypass the gate by another workflow, direct script execution, or workflow edits.
- For old feature/legacy workflows still wired to boot servers on push, do not push or trigger those paths until their auto-smoke behavior is disabled or the owner expressly authorizes the run. Prefer isolated compile/unit workflows for branch development.
- Only an owner-authorized full run may claim `server-smoked`, create an immutable server-tested integration tag, and publish a full-smoke-certified Prism QA ZIP. Never promote `candidate` or `stable` based only on the quick path.
- **Do not cancel, interrupt, or restart the already-running 2026-11-15 overnight test build.** This policy governs subsequent runs.

## Build protocol
1. Read all docs and the machine manifest. Prefer upstream official release metadata over aggregators.
2. For every proposed dependency, record project URL, exact file/version ID, MC version, loader, side, and dependencies/conflicts; check licensing/distribution.
3. Add appropriate **broad, logically grouped batches** with packwiz, check resulting lock and index hashes, then run static checks and compile/unit tests. A combined dedicated-server boot is reserved for an explicitly owner-authorized full smoke run. Investigate validation failures first, narrowing to smaller groups only if needed. Client smoke tests remain a separate, explicitly requested requirement. A successful manifest validation alone is NOT a boot.
4. When explicitly authorized to run a full smoke, test dedicated-server startup from a clean directory and separately test client launch in a graphics-capable environment if requested. Record commands, logs, platform and exact outcome. Never report an unperformed server or client boot as passed.
5. Commit each verified step. Do not treat untested features, KubeJS scripts, or loot removal as functional merely because JSON/JS parses.
6. Never mutate a server world to test economy or recipes. Test with disposable worlds and backup before migrations.
7. Record blockers in docs/COMPATIBILITY.md and run details in docs/TESTING.md; keep a changelog in commits/PR.
8. Prefer source-backed recipe and loot changes with item IDs confirmed from a live registry (via JEI/commands/data dump). No fabricated item IDs.

## Promotion gates
`candidate` = published version / unresolved deps; `installed` = hash-pinned in packwiz; `server-smoked` = fresh dedicated server reaches ready; `client-smoked` = GUI loads world; `integrated` = feature test with gates.
An empty NeoForge pack is a baseline candidate, not an integrated magic modpack.

## Git
Do feature work on a branch, present reviewable pull requests, preserve main as accepted base, and avoid force-push. Every red CI check requires a blocker report.

## Approved B3 beta exception
- Release-channel Iron's Spells 3.16.3 requires playerAnimator 2.0.x for Minecraft 1.21.1. Upstream marks this compatible prerequisite beta; the pinned 2.0.4 version is the **only approved active beta dependency**. Do not extrapolate this approval to beta gameplay mods, Astral Sorcery alpha, Draconic Evolution beta or Relics beta. JEI must remain on release channel (stable 19.64.0.455 in B3).
- ProjectE and Expansion are **boot-testing artifacts only**, until all post-Chaos Guardian acquisition and usage paths are actually gated. No survival release before enforcement.

## Approved B4 experimental additions (2026-11-13)
- User authorized testing: **BiggerReactors Rework 0.1.6** (fork, not original mod); **Astral Sorcery 2.0.3 alpha** plus **ObserverLib 1.10.3 alpha**; **Botania Revived 2.0.2 release** (unofficial port, *not* original Botania); **Draconic Evolution 3.1.4.633 beta** plus Brandon's Core 3.2.1.309 and CodeChicken Lib 4.6.1.529.
- These exact experimental versions and dependencies achieved dedicated-server startup in [run 43551344358](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43551344358). This is **not** permission to install arbitrary alpha/beta mods or to claim graphical compatibility.
- Botania Revived may contain **optional** Sophisticated integrations; the Sophisticated modules **must not be introduced as installed dependencies**, and Sophisticated Backpacks, Storage and Core are all absent from the B4 pack. Preserve the original hard exclusion.
- Reactor Rework uses its own registry namespace `biggerreactorsrework` and its battery/processor functionality needs a balance audit; do not treat old worlds as compatible with this port.
- No ProjectE or Replication gating is implemented just by adding B4. This branch remains a test build.

## Approved B5 roster and integration
- The 32 recent approved selections and 13 exclusions are documented in `docs/MAGIC-MOD-SURVEY-DECISIONS.md`; on B5 they are reconciled with the B4 71-mod pack and all compatible approved systems are pinned. **114 pinned mods/107 server JARs** reached a successful clean NeoForge dedicated-server boot in [run 43553444492](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43553444492).
- Missing transitive version requirements found during full integration: Malum needs Lodestone 1.8.2; HexCast and Enigmatic Legacy+ need Caelus; HexCast needs PAUCAL 0.7.0+; Ars 'n Spells 3.3.5 needs Ars Elemancy 1.18.3+; provider automatic dependency resolution is **not** enough by itself. Preserve these exact version relationships.
- PAUCAL 0.7.0 is alpha-tagged and Psi: Tweaks and Additions is beta-tagged in their compatible upstream files; their inclusion is for B5 testing, not approval of arbitrary experimental addons. In all cases retain exclusions from PR #6, the no-RPG-Series/Spell Engine constraint and the no-Create/storage/logistics rule.
- Server readiness is not client world entry or confirmed game mechanics. Never claim production survival readiness while ProjectE/Expansion remain ungated.

## B6 current test snapshot (2026-11-13 local)
- **125 pinned mods, 118 server JARs, 7 client-only**, with all hash-verified server artifacts successfully booting in [run 43554705678](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43554705678). This supersedes earlier B3/B4/B5 mod counts, not their historical test records.
- Pufferfish's Skills **0.19.2 beta** and Pufferfish's Attributes **0.8.3 stable** are enabled for the **user-approved experimental client test**. The custom cross-mod skill tree is **not yet implemented**; Pufferfish is just the infrastructure. Improvable Skills 3 **21.0.2** is a separate XP-purchased perk system, with HammerLib **21.0.17**.
- Eight food/harvest additions are enabled and server-smoked: Pam's HarvestCraft 2 Core/Crops/Trees/Extended, Farmer's Delight, Aquaculture 2, Harvest with Ease and Cobweb. Do not assume all crop right-click behaviors or recipe tags work until client/world tests.
- **Aspect Alchemy is explicitly rejected** after design review; it was removed from manifest and packwiz, and added to `excluded`. This overrides older survey/B5 references to its approval.
- B6 client launcher handoff is a Prism instance ZIP carrying **only MIT packwiz installer bootstrap**, no third-party mod JARs; packwiz downloads each mod from its original host. See `docs/B6-CLIENT-HANDOFF.md`.
- A headless server PASS is not a graphical client/world-generation PASS, and the pack is still not a survival-ready progression release.

## B7 QoL baseline
- User reported that the B6 125-mod Prism client launches and world generation works. Preserve B6 as the last user-tested client fallback, immutable snapshot `ecddc4342c439a2b973ad16acff112b7d0a4bb60`.
- B7 adds exactly one approved mod: **Inventory Tweaks — ReFoxed 1.21.1-1.3.2**, client-only, CF project 976858 file 6407807, SHA1 `76969e26520ab060d51515757db25282bed71b10`; Mouse Tweaks retained. B7 now has **126 packwiz pins** (118 server-side, 8 client-only).
- [Regression](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43558589888) booted original 118 server JARs cleanly; client-side ReFoxed GUI is still **untested**, requiring user middle-click check. [Prism import artifact](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43558589888/artifacts/14098959586) references exact verified snapshot `316d11a0626e5793ab7cc5d4b463891f80aa0fe6`.
- [Official ATM10 QoL comparison](docs/ATM10-QOL-AUDIT.md) records many recommendations; **none except ReFoxed is approved or installed**. Never auto-import ATM10's packs/storage/fast travel or Almost Unified resource rewrite without explicit owner decision.

## B8 expanded QoL and seed Market snapshot
- Current B8 **140 pinned mods**: 127 server-side/both, 13 client-only. [Full server boot passed](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43560055987) with all server JARs downloaded, hashes checked, NeoForge 21.1.265 / Java 21 reaching `Done`, exit 0. The updated Prism ZIP is [here](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43560531515/artifacts/14099663054), exact immutable commit `0c214d24793f2c9d912b4540e609bd04cb7b5e9a`.
- User approved **Nature's Compass** and all other QoL recommendations in the last concise shortlist (Akashic Tome, Crafting Tweaks, Simple Backups, KeyBind Bundles, Enchantment Descriptions, Better Advancements, Oracle Index, More Overlays Updated, spark); **Farming for Blockheads is the seed Market, separate from Farmer's Delight**. The required libs Balm, Bookshelf and Prickle are pinned.
- **Waystones explicitly rejected; Explorer's Compass unapproved and absent.** Do not infer permission for free structure teleportation, FTB Essentials, or previously excluded mods.
- **Simple Backups 1.21-4.0.30 is beta-tagged upstream**. It is pinned `side = "both"` to load in Prism singleplayer as well as on a dedicated server. Never claim backup/restore or retention tested; require actual disposable-world restore before relying on it.
- **Farming for Blockheads Market** can enable buying ordinary and potentially rare/magical seeds. Do not release as balanced survival until actual Market trade lists are audited and magical items gated or excluded; do not invent registry names.
- No B8 graphical client, market purchase behavior, Nature's Compass UI, Crafting Tweaks, wiki coverage, Pufferfish magic tree, progression KubeJS gates or authored quests have been tested/finished. B7 126-pin and B6 125-pin working client snapshots remain rollback.

## Owner-approved economy and kitchen additions (2026-11-14)
- Add **Quest Shop** (Holysweet/AquariusSidhe 1.4.0 for NeoForge 1.21.1) and **Cooking for Blockheads** (21.1.24 for NeoForge 1.21.1, with already-pinned Balm). This does not add another technology or sixth quest category.
- Every authored quest grants XP and **preferably a meaningful fixed item**; coin substitutes are rare, for quests where a sensible item is unavailable (notably intentional sales). Quest rewards may not auto-complete another quest except approved intro/self-resource exceptions. Repeat completion criteria only with explicit approval, e.g. the tiered copper/emerald/diamond sale contracts, and item consumption must be enabled.
- Quest Shop may remove rare-loot bad luck; it must not bypass Chaos Guardian, Cataclysm, Eldritch Ancient Miner or banned item acquisitions. Copper, emeralds and diamonds can eventually be converted to a **single team coin** per large repeatable item-consuming batch; later ProjectE infinite money is intentionally permissible.
