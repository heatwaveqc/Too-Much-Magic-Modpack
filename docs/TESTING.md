# Test ledger

**2026-11-13 bootstrap environment**
- GitHub repository created by user and first README commit verified: **PASS**.
- Java 21 in local container (`openjdk 21.0.12.1`): **PASS**.
- Local container `packwiz` binary: **NOT INSTALLED**.
- Local container network DNS for Minecraft/Modrinth/GitHub: **BLOCKED** (`Could not resolve host`).
- Packwiz TOML hash chain, manifest exclusions and CLI read: **PASS** GitHub Actions run 43545677885, job 132296314018.
- NeoForge 21.1.265 installer download and clean, loader-only dedicated-server startup: **PASS** GitHub Actions run 43545677885, job 132296313700; reached `Done` with script exit 0. This is a clean loader only, **not a modded-server boot**.
- B1 actual modded dedicated-server startup: **PASS** GitHub Actions run 43546287284, job 132298268556 (four SHA-512 pinned mod JARs fetched and verified, NeoForge 21.1.265 reached `Done`, clean exit status 0). No larger magic mods installed.
- Client startup / menu / fresh world: **NOT RUN** (no graphics-capable launcher).
- KubeJS script registration and recipe event validation: **NOT TESTED**; KubeJS JAR present in the B1 successful modded server, but the scripts are deliberately inert placeholders.
- B1 dependency resolution and download: **PASS** for exactly KubeJS, Rhino, Better Advanced Tooltips, ModernFix; Modrinth artifacts SHA-512-verified in CI run 43546287284. Other entries not resolved.
- Meta/QoL **modded-server runtime compatibility demonstrated** for the 23 server-loaded JARs only; core magic and graphical client are untested.

## Required subsequent test matrix
1. **Static**: `python3 scripts/validate.py`; packwiz index and hashes; machine manifest uniqueness and restrictions.
2. **Loader baseline dedicated server**: fetch exact 21.1.265 NeoForge installer; install on Java 21 into disposable dir; accept EULA for CI only; launch `nogui`, capture exit code and `Done` log line, then terminate cleanly.
3. **Core mods server**: pin exact 1.21.1 NeoForge mod files+all required libraries. Download via supported packwiz installer, start server, assert no missing dependencies, registry errors or data pack issues. Save logs.
4. **Client**: on suitable workstation import packwiz pack, launch main menu, create disposable world, check mod screens, shader/sky render, KubeJS events and absence of crashes.
5. **Gameplay integration**: validate each gate in survival and via negative bypass tests; confirm future renewable routes; ensure banned items have no acquisition paths.

Never reclassify step 1 as steps 2–5.

## B1 integration result — 2026-11-13 (local America/Phoenix date)
- Run: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43546287284
- Metadata job: **PASS**, offline SHA-256 chain and actual packwiz CLI.
- Modded-server job: **PASS**; downloaded `better-advanced-tooltips-2101.1.0-build.5.jar`, `kubejs-neoforge-2101.7.2-build.381.jar`, `modernfix-neoforge-5.27.27+mc1.21.1.jar`, `rhino-2101.2.7-build.85.jar`; verified SHA-512 each, reached dedicated server `Done`, shut down with status 0.
- Client GUI/menu: **NOT RUN**. KubeJS actual recipe execution: **NOT TESTED**. Multiplayer connectivity and custom quest APIs: **NOT RUN**.
- Baseline and modded stages are distinct; nothing in this run proves future Occultism/Ars/Iron's/Neo Vitae compatibility.

## B2 metadata/QoL integration result — 2026-11-13
- Workflow: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43548230954
- Automated resolution: **PASS**, 30 exact packwiz artifact pins, 0 requested slug failures. FTB pulled from exact official CurseForge IDs. Fuzzy-name false positives detected and rejected.
- Hash verification and dedicated-server startup: **PASS**, all 23 server-side JARs verified using source hashes (Modrinth and CurseForge), server reached `Done`, clean stop exit status 0.
- Seven client-only mods: **NOT CLIENT BOOT-TESTED**; no graphical session attempted.
- Quest content, KubeJS script events (other than empty placeholders), user joins and chunk claiming: **NOT TESTED**.
- Full resolved filenames, side and project IDs: [META-QOL-RESOLUTION.json](META-QOL-RESOLUTION.json).

## B3 stable gameplay + Tectonic (2026-11-13 local)
- Initial 55-server-JAR boot was **BLOCKED** by missing Iron's Lib and playerAnimator in [run 43549472015](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43549472015). Both were added with exact immutable artifact metadata.
- Repaired full **57-JAR dedicated-server** boot was **PASS** in [run 43549822627](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43549822627), reached `Done`, graceful stop exit 0.
- **Final release-JEI full-server regression PASS** in [run 43550199289](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43550199289): replaced beta-tagged JEI 19.70.0.469 with stable 19.64.0.455, all 57 JARs hash-verified, reached `Done`, exit 0. 64 total mods; seven client-only.
- Beta exception: playerAnimator 2.0.4, mandatory prerequisite of release-channel Iron's Spells; no beta gameplay mods. No Astral Sorcery, Botania, Draconic Evolution or beta Relics.
- **Client menu, new-world entry, Tectonic worldgen, spellcasting, scripts, recipes, quest UI, server connection: NOT TESTED**. ProjectE/Expansion Chaos Guardian gates **NOT IMPLEMENTED**; the current pack is a startup test build, not a balanced survival release.
- [Detailed B3 evidence](B3-TESTING.md), [dependency exception](B3-DEPENDENCY-EXCEPTIONS.json), [exact release metadata](B3-RESOLUTION.json).

## B4 reactor and experimental magic candidate — 2026-11-13 local
- [Staged CI run 43551344358](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43551344358): **PASS all four stages**, each from a disposable new dedicated server. Sequence: Reactor Rework 0.1.6; Astral 2.0.3 alpha + ObserverLib 1.10.3 alpha; Botania Revived 2.0.2 release; Draconic Evolution 3.1.4.633 beta + Brandon's Core 3.2.1.309 + auto-required CodeChicken Lib 4.6.1.529.
- 71 distinct packwiz mod pins in final working configuration. Hash verification, NeoForge 21.1.265 `Done` ready line and graceful exit succeeded for each stage.
- Nonblocking recurring log messages: Project Expansion claims missing `accesstransformer.cfg`; a mixin referring to an absent Quark GoldToolsHaveFortuneModule emits warnings. Neither prevented server readiness, but both warrant investigation before a production release.
- Graphical client / shaders / Astral sky effects / player-world entry / Botania mechanics / reactor energy balances / Draconic Chaos Guardian **NOT TESTED**. Endgame gates remain unimplemented; the pack is not production-ready for survival.
- [Detailed stage evidence](B4-TESTING.md) and [pinned artifacts](B4-RESOLUTION.json).

## B5 expanded user-approved magic roster (2026-11-13 local)
- 32 user-approved survey selections reconciled with working B4 branch; **31 new approved systems** plus **12 required libraries** were added (Botania Revived was already installed): final total **114 mod pins**.
- [Attempt 1, run 43552770652](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43552770652): all 102 selected server JARs hash-verified, startup blocked by missing **Lodestone** required by Malum. Added Lodestone 1.8.2 plus Ars Elemental and Gaze from their verified upstream projects.
- [Attempt 2, run 43553107811](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43553107811): all 105 server JARs hash-verified; loader reported missing **Caelus API** and **PAUCAL** for HexCast/Enigmatic Legacy+ and incompatible **Ars Elemancy 1.17** for Ars 'n Spells 3.3.5.
- [Attempt 3, run 43553444492](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43553444492): upgraded Ars Elemancy to 1.18.3 and installed Caelus 7.0.1 and PAUCAL 0.7.0. **PASS** 107/107 JAR content hashes, NeoForge 21.1.265 reached `Done`, clean shutdown exit 0.
- One new alpha-tagged required library **PAUCAL 0.7.0** and one beta-tagged optional-power addon **Psi: Tweaks and Additions** are boot-test candidates, not validated gameplay mechanics. Previously approved B4 alphas/betas remain.
- No hard-excluded or unapproved maturity-test systems installed. No graphical client, Tectonic worldgen gameplay, multiplayer connection, FTB Quests, KubeJS gameplay scripts, creative/survival balance or Chaos Guardian/ProjectE progression restrictions were verified.
- [B5 test ledger](B5-TESTING.md), [exact resolution report](B5-RESOLUTION.json), [survey approvals](MAGIC-MOD-SURVEY-DECISIONS.md).

## B6 food + dual skill systems + Pufferfish — 2026-11-13 local
- [**Full dedicated-server PASS run 43554705678**](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43554705678): **125 exact packwiz pins** (118 server/both-side, 7 client-only), 118 server JARs downloaded and hash-verified, Minecraft 1.21.1 / NeoForge 21.1.265 / Java 21 reached `Done` and shut down gracefully exit 0.
- **12 newly pinned**: four Pam's HarvestCraft 2 modules, Farmer's Delight, Aquaculture 2, Harvest with Ease, Cobweb, Pufferfish Skills (0.19.2 beta) and Attributes (0.8.3 stable), Improvable Skills 3 (21.0.2), HammerLib (21.0.17). **Aspect Alchemy removed**, because subsequently excluded.
- [**Prism Client Test ZIP produced and verified**](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43555071188), artifact [14097951209](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43555071188/artifacts/14097951209). Format contains `mmc-pack.json`, `instance.cfg`, MIT installer bootstrap and help text, but **no third-party Minecraft mod JARs**. Prism import and graphical launch not yet performed.
- Remaining: client boot/menu/world entry, Pufferfish UI/config, Improvable Skills Book of Skills, Pam's crop harvest, fruit tree/fish worldgen, Farmer's Delight recipes, Tectonic, Astral graphics with Embeddium, KubeJS gates/quests and server multiplayer. **ProjectE/Expansion Chaos Guardian locks not implemented**.

## B7 sorting-only addition / same dedicated-server stack
[Run 43558589888](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43558589888): **PASS**, exact 125-to-126 mod metadata pin update (new ReFoxed client-only), 118 server-side JARs fetched and hash-verified, NeoForge 1.21.1 / 21.1.265 reached `Done` and exited 0. ReFoxed exact `invtweaks-1.21.1-1.3.2.jar`, CurseForge project 976858 file 6407807, SHA1 verified in packwiz; Mouse Tweaks retained. The mod is client-only and not in server JAR count, so **middle-click chest sorting remains a client acceptance test**. [B7 Prism ZIP](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43558589888/artifacts/14098959586), immutable install snapshot `316d11a0626e5793ab7cc5d4b463891f80aa0fe6`. [Audit of other ATM10 QoL mods](ATM10-QOL-AUDIT.md); no other QoL candidates added.

## B8 QoL, Nature's Compass and seed Market — 2026-11-13 local
- **Dedicated-server PASS:** [Action 43560055987](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43560055987) — 140 packwiz pins, 127 server-side JARs downloaded and hash-verified, Minecraft 1.21.1 / NeoForge 21.1.265 / Java 21 reached ready `Done` and quit exit 0.
- 14 additional exact mod JAR pins: 11 selected features plus Balm/Bookshelf/Prickle; old B7 baseline remains untouched.
- **Client delivery metadata corrected**: beta-tagged Simple Backups was moved from `side = "server"` to `side = "both"` for Prism singleplayer, without changing the JAR, version, hash or server composition. [Final Prism ZIP build](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43560531515) passed packwiz hash validation and ZIP structure checks. [Import artifact](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43560531515/artifacts/14099663054) pins immutable revision `0c214d24793f2c9d912b4540e609bd04cb7b5e9a`.
- **Not tested:** actual GUI/JEI interactions, Market seed/sapling trades (especially magical), crop and Pam's integration, compass biome search, inventory shortcuts, backup creation/retention/restore, profiler and overlay interactions. Use a disposable world. In particular, **do not trust Simple Backups as sole data protection** while beta and unverified.

## B8 corrective removal candidate (2026-11-14)

- **Scope:** remove Re:Avaritia and Extended Crafting as explicitly excluded by owner. Reduce 140 pinned mods to **138** (previous 127 server/both-side -> 125 server/both-side, 13 client-only unchanged). Two packwiz metafiles removed; manifest and SHA-256 hash chain updated.
- **Status when authored:** metadata validation and fresh combined NeoForge dedicated-server test **pending** on `design/five-category-quest-chapter-inventory`. Do not mistake the earlier 140-mod B8 test for verification of this changed candidate.
- **Client:** Prism launch, existing-world migration, all recipes and endgame compatibility still pending. Back up worlds; removing mods from a world that has their items/blocks can lose contents. Test on a disposable world first.


## Core Magic Tyz first-wave static validation (2026-11-14)

- **Branch:** `feature/tyzs-core-magic-first-wave`; **PR:** [#33](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/33), based on Monk no-Pufferfish design branch.
- **Static PASS:** [GitHub Actions Core Magic overlay validation](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43630243324) checked four JSON skills with 99 ranks and native modifier arrays, 1,000-XP global level pool, KubeJS JavaScript syntax, and TOML syntax; source connector separately parsed all four JSONs and verified 13 distinct attribute targets across seven mod namespaces.
- **Pack baseline validation PASS:** [Verify bootstrap branch run](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43630187440), for source/metadata structure only. It is **not** a native Tyz mod or game boot.
- **Not run:** packwiz staging, Tyz jar pin/dependency resolution, Forge/NeoForge server boot with Tyz and source event script, client launch, native attribute tests, rank purchases/refunds, paid casting/discount and shared mana dedup, external Monk gates, or MXP experience award dispatch. DO NOT label as gameplay-passed or next downloadable client build.
- **Next action:** integrate the overlay into a disposable full pack after Tyz 6.4.2 pin, retire Pufferfish only after dependency check, then test native attrs and cost hooks via admin Tyz XP grants while the full MXP dispatcher is being built. [Checklist](../compat/magic/README.md).


## MXP Java module initial build/test (2026-11-14)

- **Source**: draft [PR #40](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/40), feature/mxp-ledger-dispatcher, based on the Core Magic first-wave branch. Standalone Java module under compat/mxp is **not packwiz-installed** and **has not been launched in Minecraft**.
- **Compilation + arithmetic unit test PASS**: [GitHub Actions MXP ledger module](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43644249747). Covers fixed first rewards, ordinary divisor changes at award boundaries, resource normalization, high-level/fixed repeat exceptions, craft first-time divided behavior, transaction dedup, no-debit zero awards and fractional pending XP.
- **Source pilot**: Psi 110 preSpellCast/spellCast script verifies actual paid cost and then grants MXP through Java; it is deliberately outside packwiz and still lacks client/server runtime confirmation. Demo boss-rule script is inactive; no values silently activated.
- **Pending**: exact Tyz 6.4.2 ABI runtime, Java class-filter access in KubeJS, Source ADT signature in client/server, saved player data after reload/death, boss victory attribution, Ψ cost efficiency, and Ars/Iron's canonical payment settlement. No current distributed test-client build contains MXP.


## MXP four-mod spending adapter / first-boss regression (2026-11-14)

- **Source:** `feature/mxp-ars-irons-botania-neo-hooks`; committed real NeoForge mixins for Ars / Iron's / ANS, Botania Revived's player-item mana request handler, and Neo Vitae's Anima PostSyphon. The source contract is in [compat/mxp/SOURCE-HOOKS-2026-11-14.md](../compat/mxp/SOURCE-HOOKS-2026-11-14.md).
- **Boss rule:** every first registered victory = 1,000 fixed effective MXP; only repeat reward and fixed-repeat exception configurable. Regression tests check first payout at P=125 and 500,000 portable mana baseline and item whitelist.
- **Static validation:** Java module plus source-policy tests pass in [GitHub CI run 43661889592](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43661889592). Another run confirms the new implementation doc version; this is not a game launch.
- **Not tested:** optional mixins on *actual* Ars Nouveau 5.13.5, Iron's 3.16.3, Ars 'n Spells 3.3.5, Botania Revived 2.0.2, Neo Vitae 1.1.53, or Tyz 6.4.2 client/server. Also untested: Curios inventory snapshot, cross-cast dedup, mana-funded worn-item repair, automation and module-ABI compatibility. No Prism ZIP/test release has been created.
- **Neo baseline blocker:** `neovitae_ev_network_initial` defaults to 0 disabled because native Anima storage has no stable initial max; select the canonical equivalent pool before enabling in test config. Player's EV network PostSyphon includes automatic upkeep while online; confirm balance rule before survival deployment.
