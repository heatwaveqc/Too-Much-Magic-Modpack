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
No Create, original Blood Magic, Sophisticated Storage, generic backpacks, generic item logistics, Applied Energistics 2 (AE2), Refined Storage, FTB Ultimine. **No Re:Avaritia or Extended Crafting** (owner rejected competing Infinity-tier endgame after the earlier on-hold status was accidentally ignored). No generic FE transfer mod: Draconic Evolution supplies the high-end energy movement.
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
