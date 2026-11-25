# Progression generic XP and Core pilot — source promotion and QA, 2026-11-23

## Immutable provenance and selection

- **Base**: `test/full-integration@c0e3fe3c522022298f795b7df8bee29200daedb4`.
- **Final coherent Progression donor**: `dev/progression@c1b0784bd756b7dde68d65febd574720cb1b036d`.
- Source merges were performed **in this exact order** (GitHub confirmed each current head and exact-head passing CI; stacked PRs automatically retargeted as prerequisites landed):
  - [#580](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/580) generic current-XP ledger, reviewed PR head `610a5999b986160e8f0b09292ac5f6ff45131fd6` → dev merge `6099146fe285488362538076b68ad08da1ff8a6d`.
  - [#588](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/588) live MXP accounting, reviewed PR head `aa3dbe05a56624d49652ea1f7be1d125bb251f90` → dev merge `2e94aeed1fc79d17890e105586d6d8e753ac0b4c`.
  - [#595](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/595) Newbie and Normie Paths, reviewed PR head `e1fb0c364f61e39de15a42000f07383c0fdfd260` → dev merge `3c2107ca7bd538bfdb6898b444e236b8c73fbc70`.
  - [#598](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/598) configured economies and purchases, reviewed PR head `8ada0bc73254a9284781dd1b59c2cdcc1836b379` → dev merge `fd4b716268b69b45209afc6dcc9ba5ad18446fb6`.
  - [#584](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/584) four Core magic skills, reviewed PR head `7a7775633bcb67155f2523c852225183e6475d57` → dev merge `c1b0784bd756b7dde68d65febd574720cb1b036d`.
- Final whole-subtree SHA comparison: `compat/progression/` has **530** donor files, **514** already on integration; **492 byte-identical**, **16 new**, **22 changed**, **0 deleted**. Promote these **38 exact donor Git blobs only**; do not merge whole `dev/progression`, revive retired MXP/Tyz modules or overwrite unrelated integration files.
- Preserve existing `tyr_progression` module identity and first-party selector; leave Food/Monk/Hunter-owned configs and already merged #525 screen fix, #508 blood bridge, #237 appraisals unchanged except when a reviewed new PR #598 file specifically updates the shared screen.

## Source behaviors included

1. #580: provider-independent per-system decimal XP ledger, derived level/points, dynamic growth, receipts, independent skill ownership and strict config parser. **Pure foundation alone did not activate runtime.**
2. #588: connect generic current-XP ledger into NeoForge player attachment, MXP earn/purchase and receipt deduction, real Path leave, operator removal and respec; compatible read-only legacy wallet projection. A Path exit forfeits old Path purchases under approved default rules, and Newbie remains barred from MXP earning.
3. #595: data-defined first-class Newbie and Normie baseline Paths with server reconciliation and Smart Keep compatibility projection; Core is universal, baseline switches idempotent, existing milestones retained.
4. #598: bundled `economy.json`, MXP baseline 1000 XP/point and growth 1.02; config-defined named XP economies, event-scoped receipts, buy-one-rank-per-point default, two-currency eligible purchases, Path transition policies, permanent achievement-derived free ranks, compatibility data, and wallet-aware client purchase UI.
5. #584: four active Core skill definitions (Reservoir, Wellspring, Thrift, Potency) at five one-point ranks each; 23 other Core candidates are **not** activated.

## Hard boundary / incomplete behavior

- **Not full #475**: historical native `experience_systems` sources are not all migrated; comprehensive verified block/kill/ritual accrual including #59 no-drop stone and spell mining still needs coding/review. Other magic/potion-provider modifiers and precedence need independent validation.
- **Not full #455**: full Core 27-node program is not live; FTB Quest reward consumer, third-party form admission and all multiplayer/provider compatibility are not finished. No automatic skill/graduation data migration of existing stable worlds is warranted, but pre-release worlds still need actual attachment persistence testing.
- #598's optional achievement ranks and arbitrary currencies are data/API-capable; do not claim every native Path, class or quest has its own custom configuration/receipt implementation.
- Source-level Gradle/JUnit pass does **not** prove NeoForge codecs, mixin application, native event hooks, loot/skill consumption, server/client packet sync or GUI visuals. No full integration assembly, Minecraft server smoke, Prism client/package or in-world tests authorized or launched.

## Type A source checks (PR and postmerge)

- Exact-head progress-core Java 21 compile and JUnit with all five slices **together**, shared JAWR/MXP, special boss and other pinned ABI source checks, Newbie cohort/source tests, metadata and first-party package regression.
- Read-only `scripts/validate_integration_sources.py` and full integration quick CI, including unchanged Packwiz metadata.
- Confirm 38 changed paths are all under `compat/progression/`, with two separate registry/documentation changes only; no mutations of `compat/mxp/`, `compat/transformations/`, food scripts, quest lists, Packwiz or other module binaries.

## Type B/C next owner-authorized comprehensive test queue

- Fresh Newbie: no MXP accrual or specialist spending, first-class baseline persisted, no premature or double graduation. Normie after personal quest: Core auto-joins, four skill nodes visible, 1000 current MXP buys first point, no phantom previous ranks.
- Arbitrary two-currency data-created skill: independent ledger/current balances and per-currency limits, combined effective rank exactly once, secure selection against client forgery, no duplicate effect application; old one-currency skills remain functional.
- MXP accumulation through genuinely committed native spell/ritual/blood-resource/boss sources; independent sources add; identical transaction receipts cannot double-pay. Growth factor 1.02 and configured 0.001..100 range, threshold crossing, achievement auto rank and irreversible one-time rewards.
- Save/relog, respawn, dimension change and multiplayer: Codec/attachment persistence of fractional current XP, bought rank receipts, achievement ledger and baseline ownership. Path specialist leave/join/respec/rejoin: current-XP rebasing and refund/forfeit policies, Core preservation, no skill purchases surviving actual Path loss.
- Client skill screen: #525 no-blur screen retained; per-currency purchase UI and threshold/point display correct; no ghost rank selection. Reload invalid economy/compatibility config safely and preserve last good data or fail closed per intended data loader behavior.
- All normal existing Monk, Hunter, dragon/blood forms and ProjectE/Jade appraisals should be regression checked once the assembled Progression JAR is in an authorized integrated client/server environment.

Record every B/C outcome with **exact built commit and JAR hash** in `docs/qa/CURRENT-TEST-STATUS.md`; do not mark as passed by source CI.
