# Gaze (A Malum Addon) 1.1.7.1 — separately scoped Atlas dossier

**Status:** PINNED-PARTIAL. Publisher content roster plus an exact-version **two-Geas/rite-effect ABI and recipe subset** recovered from a prior TMM audit; other Gaze systems still unverified. This is the native Malum 1.8.2 Atlas's installed addon, not an interchangeable part of base Malum's 26 Rites or 37 Geases. The TMM-selected `mods/gaze-a-malum-addon.pw.toml` pins `gaze-1.1.7.1.jar`, Modrinth `NlvaJ5WE` version `od4ltbRo`, SHA-512 `7e9ecdcc996f376e280bf46f5c0cfd328fb6f1228b1dac469390031458e7a81ad56acff03d9bb6b57b8209586798ab443be1e91fa8fe56297a9e7c9451db03e2`. [Publisher's CurseForge features](https://www.curseforge.com/minecraft/mc-mods/gaze-a-malum-addon) and [exact Modrinth release](https://modrinth.com/mod/gaze-a-malum-addon/version/1.1.7.1) confirm Minecraft 1.21.1 / NeoForge and required Malum 1.8.2 plus Lodestone; Iron's Spells is optional. There is no published official source code. This Atlas pass did not independently download or extract the JAR; however, **merged TMM Progression PR #375 already hash-verified and inspected the exact pinned binary**. See the [cross-workstream pinned evidence reconciliation](../inventories/gaze-1.1.7.1-pinned-abi-evidence.md) for the verified two Geas recipe/effect mappings and the 26 rite *effect-type* distinction. Neither the prior static audit nor this documentation is FTB quest implementation or gameplay PASS.

## Distinct reported content — treat counts as *published claims*, not parsed registries

| Addon family | Publisher-visible count | Native vs addon, evidence and missing mechanics |
|---|---:|---|
| Separate progression screen | 1 | Reported by author. Need access key/UI title, actual prerequisites, persistence, player-level gated unlocks. |
| Added Geases | **2 exact recipe-backed effects (JAR verified in PR #375)** | `gaze:domain_of_swords` and `gaze:pact_of_encroaching`, from `malum:soul_binding/domain_of_swords` and `malum:soul_binding/pact_of_encroaching_malice`. Distinct from the 37 native Geases. Effect behaviors, names of ingredients and balance still need a full addon audit. |
| Added Rites | **26 Gaze Rite effect types** found in pinned JAR audit | These are *not* 26 additional registered canonical Rites; native Rite lookup uses Malum's 26 registered IDs. Gaze-specific effect mapping, configuration, player-visible behavior and activation inputs still need inspection. |
| Weapons | 6 | Actual item IDs, crafting gates, damage, ammunition and Soul Ward/Scythe interaction unverified. |
| Runes | 8 | Separate from base 24 runeworking recipe paths. Release note mentions **Fafnir** interacting with full Malignant armor, but the magnitude/rule is not verified. |
| Curios | 5 | Slot types, ownership and in-pack equip/conflict semantics need item/Curios source or client inspection. |
| Pouch | 1 | **OWNER POLICY REVIEW**: no generic backpacks; exact auto-pickup, storage rules/size and recipe unknown, so no acquisition/reward approval. |
| Anima Bestiary | Reported UI/content | Change note describes Bestiary display repair; actual item/page/loot/knowledge logic not verified. |

## Source-evidence 29-axis boundary

These status rows deliberately do not infer absent features or transfer Malum base mechanics into Gaze without confirmation. `possible` is not a positive capability claim.

| Axis | Gaze 1.1.7.1 verified boundary |
|---|---|
| `gear` | present (publisher roster): six weapons, eight runes, five Curios; exact object stats/recipes unknown. |
| `movement` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `ore_processing` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `storage` | present (publisher roster): one new pouch; item restrictions/volume unknown and TMM generic bag policy HOLD. |
| `crafting` | partial: exact two `malum:soul_binding/*` Gaze Geas recipes and main/extra/Spirit **counts** independently documented by existing hash-pinned TMM audit; concrete ingredient IDs and all other addon crafting still unverified. |
| `logistics` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `buffs` | exact two Gaze Geas effect IDs verified by hash-pinned prior audit; eight additional runes remain a publisher roster count without inspected mechanics. |
| `external_compatibility` | declared: Malum 1.8 and Lodestone 1.8.2 required, Iron's optional; all loaded interactions untested. |
| `mob_farming` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `ore_generation` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `permanent_power` | two exact recipe-backed Gaze Geases verified in prior PR #375; persistency/limit/effect consequences in assembled client remain unverified. |
| `general_generation` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `direct_damage` | possible: six advertised weapons, no verified attack behavior. |
| `block_breaking` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `permanent_pets` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `temporary_pets` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `defense` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `healing_recovery` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `debuff_control` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `energy` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `automation_control` | pinned audit identified 26 Gaze Rite effect types, **not** 26 independent Rite type registrations; effect behavior and cadence remain unverified. |
| `agriculture` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `detection_information` | partial: new progression screen and Anima Bestiary in release notes; actual UI and data not inspected. |
| `equipment_augmentation` | present (publisher roster): eight runes and five Curios; no slot/modifier proof. |
| `world_manipulation` | possible: new Rites, unknown effects. |
| `world_content` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `death_respawn` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `trade_economy` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `progression_tiers` | present (publisher roster): new progression screen; actual unlock route unknown. |

## Honest player quest placeholder map

Parent native Malum guide branch should conclude with source-verified native operation and an **optional Gaze continuation**, not a forced Gaze task gate. [Research labels ML-42/43](malum-quest-map.md) intentionally stage (1) find the genuine progression screen once client loaded and (2) select one source-verified new rite, weapon, rune or Geas **after** exact item/recipe/activation information is recovered. Neither placeholder is a real FTB Quest task; don't generate fake IDs or say players can craft a given Fafnir rune until its source/version is checked.

**Integration decision:** Gaze's pouch joins native `malum:soulwoven_pouch`/`malum:ravenous_pouch` in the TMM generic-backpack policy audit. It must be excluded from survival progression or explicitly justified/limited before any quest, shop or loot grant. Actual source properties are unknown; do not silently apply the base Ravenous capacity to Gaze's new pouch.

**Required to close:** reuse or independently inspect the exact pinned 1.1.7.1 JAR's remaining `/data`, registry, language, UI and executable behavior evidence; reconcile both known Geases' actual ingredient identities and effects, the 26 Gaze Rite effect variants, eight Runes, six Weapons, five Curios, pouch/Bestiary, and first-to-end player tutorial in isolated TMM. If a future source becomes public, pin its immutable commit rather than copying 1.9.0 features from base Malum. Until then, the **base Malum 1.8.2 dossier is source-substantial, but the complete Malum+Gaze family cannot be marked fully source-ready**.
