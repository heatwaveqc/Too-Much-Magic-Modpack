# Test ledger

> **Current/next-test handoff:** [QA hub](qa/README.md) · [Codex-executable three-tier checklist](qa/NEXT-TEST-CODEX-CHECKLIST.md) · [current test status and evidence](qa/CURRENT-TEST-STATUS.md). This file is the historical test log; the QA status document is the active handoff, and `manifest/feature-checklist.json` remains the feature-level authority.

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

## Questbook navigation scaffold candidate (2026-11-14)

- 5 category groups, 45 chapter SNBT files, 45 optional no-reward orientation checkmarks, plus a first-log advancement/pouch quest. Stable IDs, grouped localization, packwiz indexing and offline structural validator authored in `feature/ftb-quests-45-chapter-scaffold`.
- **Integration not yet certified:** dedicated-server FTB Quests file parsing, Prism client GUI, per-player first-log advancement reward and storage behavior require explicit tests. CI runner's general server boot does not prove all 45 chapters appeared in the client.
- Corrected base has 138 mod pins (Re:Avaritia/Extended Crafting removed); historic 140-mod B8 run is not a verification of this composition.

## Intro quest content and reward-policy candidate (2026-11-14)

- Authored 11 Getting Started, 15 Tips & Tricks and 20 Food & Fishing quest tasks; 42 other chapter overview checkmarks now award XP and paper. **All 88 quests have XP and item rewards.** First-log FTB claim has XP+torches; the pouch still comes automatically from the persistent per-player advancement.
- Item completion criteria and advancement criteria are unique across authored quests. The 46 authored quest rewards never match any authored item-task item ID. All modded task item IDs need a live item-registry/JEI check before release.
- Static validator and server smoke test: require actual CI results on this branch. No client GUI, claim behavior or team test yet.

## Quest Shop and Cooking for Blockheads integration (2026-11-14)

- Approved B11 exactly two mods, Quest Shop 1.4.0 (CurseForge file 9046797) and Cooking for Blockheads 21.1.24 for NeoForge 1.21.1 (Modrinth version MQCIy6VF). Existing Balm is the cooking dependency.
- 103 FTB quest entries, with 12 deliberate coin-only sale tasks, must be validated in the **client** after server smoke. Sales use `consume_items: true`; 3 final tiers `can_repeat: true`, with one team credit per consumed shipment. Interactions with claiming and XP reset need in-world tests.
- Shop economy is provisional: data-driven catalog, monster coin drops disabled via `questshop-common.toml`, source-specified team-shared balance, exact artifact registry IDs and persistent shop categories/GUI still need validation.
- Production blocking: no world back-compat claim, no market seed bypass, no ProjectE/Chaos Guardian leak, no bag nesting. Record the CI run ID after the resolved 140-mod test completes.

## B11 final resolved pinned server evidence — 2026-11-14

- **PASS** [GitHub Actions run 43610383608](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43610383608), workflow *Resolve Quest Shop and Cooking for Blockheads* (resolved source commit `8d3b61d2689aaf1b1dc9d8055a5487148f653447`, metadata bot commit `04af77f4db967c567c7349ef45672f7c5e12346d`).
- Both requested exact NeoForge 1.21.1 jars pinned with reviewed provider hashes; **140 mod pins**, **127 dedicated-server jars downloaded/verified**, 13 client-only pins. **45 FTB Quests chapter files copied** into disposable server. Combined NeoForge server startup reached **Done** and exited **0**. Packwiz hash-chain validation PASS; quest static validator PASS (**45 chapters / 103 FTB quest objects / XP+meaningful item or explicitly allowed coin**).
- **Still untested in a client:** Quest Shop menu, category rendering and sale prices, category unlocking, coin reward actual claiming, team-sharing/double-claim behavior, consumption and repeat reset, Cooking for Blockheads blocks/recipes, artifact registry IDs, and real progression gating. Dedicated-server startup is not proof these operate correctly.
- Original B11 notes saying pending pin/smoke are superseded by this entry. PR remains draft until client acceptance. Do not import into an existing valued world without backups.

## Complete Progression quest skeleton, including technology line — 2026-11-14

- Feature branch `feature/progression-quests-full-tech`; authored **65 additional quests**: Basic 10, Advanced 10, Endgame Roadmap 6, Skills & Advancement 7, Exceptional Power: Reactors 32. Including 12 existing commodity sales and 5 Progression overviews, Progression has **82 quest objects**, and the whole book now has **168 quest objects in 45 chapters** (126 substantive authoring-ledger quests, 42 original overviews).
- All new objectives award XP **and a fixed item**. No new coin rewards (only the previously accepted 12 consumed commodity contracts). Checkmarks for actual reactor/battery operation are manually claimed; they do not verify machine state.
- Fixed legacy bad text in **all twelve commodity sale descriptions**, the Quest Shop tip, and both Cooking for Blockheads entries. Automated validator now rejects literal `description: [undefined]`.
- Source-backed modded item-ID references are in [POWER-TECH-QUEST-GUIDE.md](POWER-TECH-QUEST-GUIDE.md). **Still need live item registry and JEI/recipe inspection** on the exact installed 140-mod version and a client rendering test. No custom Pufferfish tree, source-to-power bridge or mining gate is asserted to be implemented.
- Quest structural validation, packwiz hash check, server start CI and outcomes are tracked in the corresponding branch Actions workflow; do not classify the feature as integrated solely on validation.

## FTB Quest UI localization and layout repair — 2026-11-14

- User client QA reported broken names/error-like formatting, poor quest arrangement, and Getting Started ending at iron. Diagnosed missing **per-chapter English `lang/en_us/chapters/*.snbt` files**, plus **65 literal `undefined` Progression descriptions** from old authoring. [Details](QUEST-CLIENT-QA-FIX.md).
- Generator `scripts/rebuild_quest_ui.py` regenerates 45 chapter-language files, 5 group names, chapter names/subtitles, 179 correct quest descriptions, and intentional branch coordinates; preserves all existing quest/task/reward IDs. In GitHub Actions run [43618497460](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43618497460), the generator, metadata check and quest static validator passed twice, including idempotence. **This is NOT client rendering verification.**
- Seven additional Getting Started quests (18 total) and four new Tips & Tricks guides (20 total), including Scriptor Inflame fuel-free smelting. Total book **179** entries. The shop remains unchanged.
- Fresh server smoke on this repaired commit and fresh Prism GUI test remain distinct; check workflow status before declaring them passed.

## B12 QoL and Reliquified Artifacts acceptance (2026-11-14)

- Starting condition: user's 140-mod Prism build had no TrashSlot, missing Relics+Reliquified upgrade mechanics on Eternal Steak, and no repeated-chop tree harvest or rapid leaf decay. These are *real omissions*, not a visual setting. Existing Artifacts and the unrelated Reliquary Reincarnations both remain unchanged.
- Seven B12 pins: TrashSlot; TreeChop; Leaves Be Gone; Relics; Reliquified Artifacts; ShatterLib/OctoLib; Puzzles Lib. **147 active mod pins after successful verified resolver**, with 7 added binaries. Relics was previously an inactive manifest candidate. No new power/tech progression gate and no quest/shop entry modified.
- [B12 exact resolver / modded server / Prism workflow](../.github/workflows/b12-qol-relics.yml) is the source of truth for pin/boot status. A CI server boot does not exercise **TrashSlot UI**, **tree drops**, **leaf decay**, **Relics XP/durability**, **vampirism compat**, or **the player's existing world**.
- Local Prism: import B12 test ZIP into a **new** instance with Java 21, create a **disposable new world**. In player inventory press **T**, discard one common dirt and recover it from TrashSlot. Check Delete/Shift+Delete only on junk, and keybind conflicts with KeyBind Bundles/other inventory mods.
- TreeChop: cut a vanilla oak repeatedly at base, note required chops, test a birch, a tall spruce, a jungle tree and a Pam's HarvestCraft fruit tree. **Sneak-break** one log and confirm only it falls. Check that player-placed wooden walls are not accidentally felled and that axe durability, block drops and relevant skill XP don't duplicate or disappear.
- Leaves Be Gone: after ordinary logging (not just TreeChop), confirm unsupported leaves decay in seconds with natural sapling/apple/stick drops. Test installed modded leaf blocks and Trees/Chop double handling. No false leaf-decay claim if it only triggers with a TreeChop action.
- Relics + Reliquified: inspect Mods list for `Relics`, `ShatterLib/OctoLib` and `Reliquified Artifacts`, then get **fresh Everlasting Beef or Eternal Steak**. Compare ability text/rank/quality, use-based XP, durability charges and charge restoration against the original cooldown version; test surviving a logout/login. Existing legacy items may differ; do not test only one old steak. Important: **Relics 0.12.5 and Reliquified 1.0.9 are beta releases**. Check replacement attributes and damage/health effects under Vampire/Werewolf transformations, item equip/Curios slots and possible death-handler interactions separately.
- Quest Shop has a buy entry for `artifacts:everlasting_beef`; after adding Reliquified, confirm purchased item is properly created with quality/progression data rather than an invalid NBT state. Its price remains **120 coins**. No item price or recipe changed in B12.

### B12 completed automated resolution + server smoke (2026-11-14)

- **PASS [GitHub Actions 43625296741](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43625296741)**. Seven exact 1.21.1 NeoForge releases, all pinned with official Modrinth SHA512 and accepted mandatory dependency projects; **147 total active mods**. Full fresh NeoForge server smoke completed successfully; the resolver committed exact pin metadata as commit `e6328f05b70b65df0e3427b6da27990b4739c371`. Server success does **not** test inventory UI/relativistic game behavior or old-world migration.
- **Prism test artifact:** [Too Much Magic — B12 QoL + Reliquified, 147 mods](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43625296741/artifacts/14126160245). Extract GitHub's outer ZIP, import the inner `Too-Much-Magic-B12-QoL-Relics-Prism.zip` via Prism → Add Instance → Import; select Java 21. All 147 mod downloads originate from approved providers with hashes. **Use a new disposable world**.
- **Server logs artifact:** [B12 test logs](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43625296741/artifacts/14125462813). If anything breaks in Prism, supply client `logs/latest.log` and whether it happens only when combining TreeChop/Leaves Be Gone or Reliquified/Vampirism.


## Automated client-build preflight — 2026-11-16

[Full A01–A18 results](qa/AUTOMATED-QA-2026-11-16.md), source/installed snapshot `2aacf34893bc06f8048a7fabae644b3b91a1d1d4`. Windows quick suite and both Gradle 9.2.1 builds executed: 43 progression and 23 transformation tests passed. All 161 installed mod hashes and both custom JAR hashes match manifest. Initial pack hash failure was checkout CRLF conversion; canonical Git bytes pass without weakening the index. Existing new-world logs reveal a Bio-Mutagen recipe codec failure and stale oven reference; real Brigadier fixture reproduces namespaced parser failure and admin XP is absent. Deferred B19 validator fails on Windows path spelling; separate normalized contents match. Full smoke not authorized/run; no new client launch/world mutations, real Prism build or B/C gameplay tests. See the current QA ledger for bounded statuses; no feature promotion.
