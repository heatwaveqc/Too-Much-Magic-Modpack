# Gaze (A Malum Addon) 1.1.7.1 — separately scoped Atlas dossier

**Status:** PINNED-PARTIAL. Exact 1.1.7.1 JAR-backed **two Soul Binding recipes with all item and Spirit quantities**, two Geas effect-class signatures, and 26 Rite effect-type registrations recovered from prior TMM audit logs; other Gaze equipment, progression UI and pouch mechanics still need verification. This is the native Malum 1.8.2 Atlas's installed addon, not an interchangeable part of base Malum's 26 Rites or 37 Geases. The TMM-selected `mods/gaze-a-malum-addon.pw.toml` pins `gaze-1.1.7.1.jar`, Modrinth `NlvaJ5WE` version `od4ltbRo`, SHA-512 `7e9ecdcc996f376e280bf46f5c0cfd328fb6f1228b1dac469390031458e7a81ad56acff03d9bb6b57b8209586798ab443be1e91fa8fe56297a9e7c9451db03e2`. [Publisher's CurseForge features](https://www.curseforge.com/minecraft/mc-mods/gaze-a-malum-addon) and [exact Modrinth release](https://modrinth.com/mod/gaze-a-malum-addon/version/1.1.7.1) confirm Minecraft 1.21.1 / NeoForge and required Malum 1.8.2 plus Lodestone; Iron's Spells is optional. There is no published official source code. This Atlas pass did not independently download or extract the JAR; however, **merged TMM Progression PR #375 already hash-verified and inspected the exact pinned binary**. See the [cross-workstream pinned evidence reconciliation](../inventories/gaze-1.1.7.1-pinned-abi-evidence.md) for the verified two Geas recipe/effect mappings and the 26 rite *effect-type* distinction. Neither the prior static audit nor this documentation is FTB quest implementation or gameplay PASS.

## Distinct reported content — treat counts as *published claims*, not parsed registries

| Addon family | Publisher-visible count | Native vs addon, evidence and missing mechanics |
|---|---:|---|
| Separate progression screen | 1 | Reported by author. Need access key/UI title, actual prerequisites, persistence, player-level gated unlocks. |
| Added Geases | **2 exact recipe-backed effects (JAR verified in PR #375)** | `gaze:domain_of_swords` and `gaze:pact_of_encroaching`, from `malum:soul_binding/domain_of_swords` and `malum:soul_binding/pact_of_encroaching_malice`. Distinct from the 37 native Geases. Exact named ingredients and Spirit counts are now independently recovered from PR #375's hash-pinned CI log; effect thresholds, balance, gameplay and non-Geas crafting remain incomplete. |
| Added Rites | **26 Gaze Rite effect types** found in pinned JAR audit | These are *not* 26 additional registered canonical Rites; native Rite lookup uses Malum's 26 registered IDs. Gaze-specific effect mapping, configuration, player-visible behavior and activation inputs still need inspection. |
| Weapons | 6 (publisher roster) | Publisher 1.1.7 notes name Spirit Saber, Seidhr, Veil's Edge, Splintered World; precise complete registry, weapon stats and crafting gates remain unverified. Spirit Saber is independently confirmed as `gaze:spirit_saber` in exact-JAR Geas recipe. |
| Runes | 8 | Separate from base 24 runeworking recipe paths. Release note mentions **Fafnir** interacting with full Malignant armor, but the magnitude/rule is not verified. |
| Curios | 5 | Slot types, ownership and in-pack equip/conflict semantics need item/Curios source or client inspection. |
| Pouch | 1 | Publisher names **Spirit-Channel Pouch**. Exact capacity, auto-pickup, filtering and recipes still unverified. Unlike generic native Soulwoven/Ravenous Pouches, it must not be presumed broad OR Spirit-only; review under TMM storage policy before granting. |
| Anima Bestiary | Reported UI/content | 1.1.7 publisher notes add a Bestiary entry; 1.1.7.1 reports empty/jitter display repairs. Actual mob records, unlock/kill tracking and player UI still unverified. |

## Confirmed exact Gaze Soul Binding paths and named features

The [hash-pinned audit log's complete two Geas recipes](../inventories/gaze-1.1.7.1-pinned-abi-evidence.md) enable a safe late optional quest-source node (ML-43): Domain of Swords requires one `gaze:spirit_saber`, 16 Hallowed Gold, 4 Mimicry Relays, 8 Mnemonic Fragments, and Umbral×1/Arcane×64/Aerial×64/Infernal×64; Encroaching Malice requires 12 Void Salts, 6 Living Flesh, 6 Imitation Hearts, 6 Malignant Pewter, and Umbral×1/Arcane×60/Eldritch×60/Wicked×60. Soul Binding attaches a Geas through the Brazier, not a crafted take-home effect item. **These are late Umbral-gated branches**, not an early forced Gaze tutorial.

[Publisher's Gaze 1.1.7 notes](https://www.curseforge.com/minecraft/mc-mods/gaze-a-malum-addon/files/7250833) also identify an **Enchantment Workbench** that transfers enchantments to books, Spirit Saber forms, and special Rite variants (including Corrupted Greater Wicked spawner activation), pending exact registry/functional audit. [1.1.7.1 notes](https://www.curseforge.com/minecraft/mc-mods/gaze-a-malum-addon/files/7261638) name Fafnir's full-Malignant-set bonus. These descriptions are not full item or recipe proof. [Malum native pouch/Berserker policy review](../inventories/malum-1.8.2-berserker-and-pouch-policy.md) keeps source-backed native behavior distinct from Gaze's unverified pouch.

## Source-evidence 29-axis boundary

These status rows deliberately do not infer absent features or transfer Malum base mechanics into Gaze without confirmation. `possible` is not a positive capability claim.

| Axis | Gaze 1.1.7.1 verified boundary |
|---|---|
| `gear` | present (publisher roster): six weapons, eight runes, five Curios; exact object stats/recipes unknown. |
| `movement` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `ore_processing` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `storage` | publisher named Spirit-Channel Pouch; unknown contents predicate/capacity. Separate native Malum broad Soulwoven/Ravenous Pouch source analysis recorded in the linked policy dossier. |
| `crafting` | partial: two exact `malum:soul_binding/*` Gaze Geas recipes with full named ingredient IDs and typed Spirit counts verified by existing hash-pinned CI audit; addon runeworking/infusion/workstation recipes still need exact inventory. |
| `logistics` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `buffs` | exact two Gaze Geas effect IDs verified by hash-pinned prior audit; eight additional runes remain a publisher roster count without inspected mechanics. |
| `external_compatibility` | declared: Malum 1.8 and Lodestone 1.8.2 required, Iron's optional; all loaded interactions untested. |
| `mob_farming` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `ore_generation` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `permanent_power` | two exact recipe-backed Geases verified. `SwordDomainGeas` class intercepts outgoing damage and declares projectile firing/sword attribute behavior; `EncroachingGeas` class declares armor, toughness, healing and damage conversions. Effect magnitudes and assembled-client persistence still unverified. |
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
