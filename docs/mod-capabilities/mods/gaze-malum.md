# Gaze (A Malum Addon) 1.1.7.1 — separately scoped Atlas dossier

**Status:** SOURCE-/JAR-PARTIAL, **publisher roster verified only**, not actual source/recipe/runtime evidence. This is the native Malum 1.8.2 Atlas's installed addon, not an interchangeable part of base Malum's 26 Rites or 37 Geases. The TMM-selected `mods/gaze-a-malum-addon.pw.toml` pins `gaze-1.1.7.1.jar`, Modrinth `NlvaJ5WE` version `od4ltbRo`, SHA-512 `7e9ecdcc996f376e280bf46f5c0cfd328fb6f1228b1dac469390031458e7a81ad56acff03d9bb6b57b8209586798ab443be1e91fa8fe56297a9e7c9451db03e2`. [Publisher's CurseForge features](https://www.curseforge.com/minecraft/mc-mods/gaze-a-malum-addon) and [exact Modrinth release](https://modrinth.com/mod/gaze-a-malum-addon/version/1.1.7.1) confirm Minecraft 1.21.1 / NeoForge and required Malum 1.8.2 plus Lodestone; Iron's Spells is optional. There is no official downloadable public source code and the pinned JAR could not be read in this source-only session. No claimed successful JAR extraction, verification of registered IDs, FTB task implementation or gameplay PASS.

## Distinct reported content — treat counts as *published claims*, not parsed registries

| Addon family | Publisher-visible count | Native vs addon, evidence and missing mechanics |
|---|---:|---|
| Separate progression screen | 1 | Reported by author. Need access key/UI title, actual prerequisites, persistence, player-level gated unlocks. |
| Added Geases | 2 | **Not included** in the 37 base-native Malum Geas types; must inspect exact IDs/classes, Soul Brazier/Curios inheritance and balance. |
| Added Rites | A new set (count unspecified) | Not included in the 26 base rites; spirit pattern/base, cooldown, effects, trigger, source item costs unknown. |
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
| `crafting` | unverified: new weapons/runes likely have crafting but no pinned 1.1.7.1 recipe JSON inspected. |
| `logistics` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `buffs` | present (publisher roster): two added Geases and eight runes, effects/costs not established. |
| `external_compatibility` | declared: Malum 1.8 and Lodestone 1.8.2 required, Iron's optional; all loaded interactions untested. |
| `mob_farming` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `ore_generation` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `permanent_power` | possible: two Geases, but must verify persistent attachment/slots and effect values rather than assuming native semantics. |
| `general_generation` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `direct_damage` | possible: six advertised weapons, no verified attack behavior. |
| `block_breaking` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `permanent_pets` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `temporary_pets` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `defense` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `healing_recovery` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `debuff_control` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `energy` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `automation_control` | possible: new Rites, unknown cadence/inputs. |
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

**Required to close:** read the actual installed 1.1.7.1 JAR's `/data` recipes/registry/lang assets and source classes (if legally accessible), inspect exact two Geases, all added Rites/8 Runes/6 Weapons/5 Curios/pouch/Bestiary, and verify first-to-end player tutorial in isolated TMM. If a future source becomes public, pin its immutable commit rather than copying 1.9.0 features from base Malum. Until then, the **base Malum 1.8.2 dossier is source-substantial, but the complete Malum+Gaze family cannot be marked fully source-ready**.
