# Too Much Magic — agent contract

This file governs every automated contributor and pack change.

## Hard baseline
- Minecraft 1.21.1, NeoForge 21.1.265 (provisional until launcher/installer verification), Java 21.
- Git tracks source, config, packwiz metadata and verification evidence. NEVER commit downloaded copyrighted mod JARs, caches, generated worlds, saves, logs, credentials, or EULA files.
- `manifest/mods.json` is the canonical decision roster; `pack.toml` / `index.toml` are the canonical **installed** packwiz lock. A listed mod is NOT active until a checked-in `mods/*.pw.toml` exists and the index references it.
- Keep excluded mods out; never silently replace a mod. For every unavailable mod, record a blocker and separately propose a substitute awaiting approval.
- Do not upgrade Java, Minecraft, loader or mods by guesswork or because the word "latest" appears on a page. Match all versions to MC 1.21.1 specifically.
- Preserve known gameplay decisions in docs/PROGRESSION.md and docs/DESIGN.md. Do not implement progression in the bootstrap stage.

## Unbreakable exclusions
No Create, original Blood Magic, Sophisticated Storage, generic backpacks, generic item logistics, Applied Energistics 2 (AE2), Refined Storage, FTB Ultimine. No generic FE transfer mod: Draconic Evolution supplies the high-end energy movement.
Never add the Ring of the Seven Deadly Sins to any normal acquisition route.

## Build protocol
1. Read all docs and the machine manifest. Prefer upstream official release metadata over aggregators.
2. For every proposed dependency, record project URL, exact file/version ID, MC version, loader, side, and dependencies/conflicts; check licensing/distribution.
3. Add appropriate **broad, logically grouped batches** with packwiz, check resulting lock and index hashes, then perform a combined dedicated-server boot. Investigate actual failures first, narrowing to smaller groups only if needed. Client smoke tests remain a separate requirement. A successful manifest validation alone is NOT a boot.
4. Test dedicated-server startup from a clean server directory; test client launch in a graphics-capable environment. Record full commands, logs, platform and the result. Do not label tests you could not execute as passed.
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
