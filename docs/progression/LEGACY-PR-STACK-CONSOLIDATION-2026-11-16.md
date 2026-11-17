# Legacy Monk/Hunter PR consolidation — 2026-11-16

**Status: source/provenance reconciliation only, not a Minecraft gameplay PASS.** This replaces the old eight-PR dependency chain with the existing long-lived `dev/progression` engine baseline and the designated single Progression owner. It does **not** introduce new Java, change existing Hunter/Monk JSON, alter the modpack manifest, or authorize a full build.

## Ownership and authoritative branches

- The **only coded Progression engine** is `compat/progression` / `tmm_progression`, owned on `dev/progression`. All Java, mixins, commands, registration, adapters, networking and executable tests live there, even when initially motivated by one class.
- Monk/Hunter/Gilded are **configuration/data-only paths**. Their JSON, datapack tags, localization, and non-executable fixtures may live under `compat/progression/src/main/resources/` but class branches must not own executable code. Any future handler requires a separate `feature/progression-*` PR; any path configuration uses `feature/monk-*`, `feature/hunter-*`, etc., both targeting `dev/progression`.
- `test/full-integration` already owns an integrated `compat/progression/` source stream (recorded as merged PR **#229**, merge commit `f468ccf8589d0a4cbb2990c86e782f8b794f70df` in `manifest/integration-sources.json`), with later targeted Fortune/Looting, GUI/TreeChop and admin-command reconciliations. Do **not** merge an old PR branch or copy its entire `compat/progression/` directory over either current branch.
- `dev/progression` has a slightly different documentation history from `test/full-integration`. Keep its unique capability-parity design and reconcile new integrated changes in the normal single-writer flow; never force-rebase the shared branch.

## Eight retired source streams: what their current successor owns

| Legacy PR | Scope worth retaining | Current source of truth | Outstanding acceptance/risk |
|---|---|---|---|
| [#127](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/127) | Shared skills/ranks, enrollment, Blink, arrow catch, fist/armor rules, UI network, harvest and runtime effects | `compat/progression` engine and its live JSON on `dev/progression` | Tombstone full-inventory/gravestone restoration and cursed Armor Lock; multiplayer, keybindings, stamina/effect and safe equipment behavior |
| [#136](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/136) | Hunter Trial of Venom potion/advancement enrollment with declarative JSON | `data/PotionEnrollmentTrigger.java`, enrollment code, `paths/hunter.json`, unit tests | Live poison/regeneration timing and cross-species enrollment exclusions |
| [#141](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/141) | Rooted, Evasion, weapon specialization, Fast Hands; scythe tag; economy critique | Shared handlers in `compat/progression`; Hunter JSON/tag; archived starter audit | Combat animations, modded scythe validity and XP/point economy |
| [#143](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/143) | Creature taxonomy/selection, max-HP-based kill MXP-equivalent native Hunter XP, potion XP, Witcher scope audit | `HunterExperience.java`, `HunterXpMath.java`, `HunterXpPolicy.java`, entity tags, `hunter.json`; archived requirements | Player-attributed mob kill and anti-farm safeguards, modded potion attribution and taxonomy data |
| [#148](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/148) | Bio-Mutagen and crafted brew, permanent positive-effect mutation progress, persistent tier/state and hunger upkeep | `HunterMutations.java`, `HunterBrewing.java`, `HunterItems.java`, `MutagenicBrewItem.java`, `HunterMutationState.java` and tests; archived audit | **Unresolved balance/security:** Turtle Master permanent Resistance without Slowness, and explicit cure/removal semantics; modded potions, saved-world and multiplayer gameplay |
| [#138](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/138) | Monk classic abilities, JSON parity and hand-harvest rules | `paths/monk.json`, `MonkPathParityCoverageTest.java`, existing `design/MONK-JSON-PARITY-PASS-2026-11-15.md` and `monk-classic-coverage-v1.json` | Native item loot legality, Curse of Binding/Glass Touch order, lava/fire overlap, special interaction QA |
| [#142](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/142) | Audit of 21 original gates, optional one-time cultivation XP, unarmed event signals | Shared `ProgressionEvents`, Monk JSON, tests and `design/MONK-CLASSIC-GATES-AND-XP-TRIAL-2026-11-15.md` | XP duplication/farming and trusted source events; missing bespoke classic trial detectors |
| [#144](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/144) | Rank-17 real Bedrock Rest, actual vanilla sleeping, once-only +230 native Monk XP | `BedrockRest.java`, `BedrockRestGeometry.java`, `BedrockRestTest.java`, same Monk JSON | Night/sleep votes, bed respawn point, other mods' sleep handling, server/client and multiplayer QA |

### Source verification performed

Read-only GitHub comparisons confirmed the live shared engine includes the distinctive legacy modules (PotionEnrollmentTrigger, Hunter XP policy/math, Hunter Mutations/brewing/state/items, inventory reservation, Bedrock Rest/geometry, resource/taxonomy tags, potion recipe, and corresponding JUnit tests). Several source files were **Git-blob-identical**, notably `PotionEnrollmentTrigger.java`, `HunterXpMath.java`, `HunterXpPolicy.java`, `HunterMutations.java`, `HunterBrewing.java`, `BedrockRest.java` and `paths/monk.json`. Other current files differ because of subsequent fixes; those must take precedence. The current Hunter JSON retains the old 30-skill set **plus** the later `silver_hunter` entry (31 total). Comparison of key JSON objects reported only that appended skill, not deletions/rebalances.

The integration manifest says `feature/hunter-mutagenic-brew-persistent-mutations-2026-11-15` and `feature/monk-bedrock-rest-2026-11-15` were transplanted into the integrated baseline; the exact engine-copy history is a **source provenance claim**, not evidence of in-world acceptance. Code presence and unit tests do not prove all effects work in live NeoForge. Old PRs should be closed as **superseded/consolidated** (not falsely marked merged); Git history and their exact head SHAs remain reviewable.

### Source-vs-config principle

The historical workstreams include shared Java edits inside their Monk/Hunter PR branches. **That does not grant future class agents code ownership.** Once older code has been relocated to the shared engine, create new follow-up engine changes only in Progression-owned PRs, and class skill-tree improvements only as JSON/datapack content. Avoid reinserting old `Vampirism` faction skills, duplicate `compat/monk` runtime, obsolete workflows, or ancient packwiz files.

## Historical research preserved with this reconciliation

Five documents were absent from current `dev/progression`; copied **verbatim** from immutable source heads under [`docs/archive/progression-pr-stack/`](../archive/progression-pr-stack/README.md) so they do not masquerade as current developer instructions:

- PR #127: capability audit and fist/armor QA checklist (head `efd1545c84fee15d4c24d8876795c48bffcbb713`).
- PR #141: starter skills and XP economy audit (head `f8037397fbeb28d0d72b927fe44677e1dcf3d2d8`).
- PR #143: Hunter specialty/kill/potion XP and Witcher functional requirements (head `8e681cee45f385429082e0ba20b4a07977b7b2f6`).
- PR #148: Bio-Mutagen, mutations, permanence and balance audit (head `daef2fb705beda1c1a523ceddcf29c60b9d03788`).

Existing up-to-date Monk design/XP audits and test cases remain at their current `compat/progression/design/` paths, not duplicated from the old stacked branches. Original PR discussions remain accessible after closure.

## Validation and next steps

This is **documentation-only**. Exact-blob verification and Markdown/source provenance checks are appropriate; it does not authorize or claim Integration Assembly, full modded server boot, Prism packaging, client smoke or gameplay acceptance.

The next Progression writer should handle genuine new engine capabilities as isolated reviewed PRs, then path JSON in separate class-only PRs; reconcile back into `test/full-integration` without replaying older directory snapshots. Outstanding runtime tasks above remain tracked even after old PR closure. For the upcoming comprehensive test, use the current [live-test inclusion register](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/docs/qa/LIVE-TEST-INCLUSION-REGISTER.md), current integration SHA, and owner authorization gates.
