# Gaze (A Malum Addon) 1.1.7.1 — separately scoped Atlas dossier

**Status:** EXACT-PINNED BINARY / RECIPE / REGISTRY SOURCE-SUBSTANTIAL. Owner-provided Gaze and Malum release JARs were verified against both live Packwiz SHA-512 pins and directly parsed/disassembled: **all 24 Gaze-authored recipes, 26 additional real Rite type registrations plus 26 effect classes, eight Runes, six base weapons, five Curios, Encyclopedia UI open path, Enchantment Workbench and remote generic pouch semantics.** Game acceptance and quantitative effect cross-mod behavior still require separate evidence. This is the native Malum 1.8.2 Atlas's installed addon, not an interchangeable part of base Malum's 26 Rites or 37 Geases. The TMM-selected `mods/gaze-a-malum-addon.pw.toml` pins `gaze-1.1.7.1.jar`, Modrinth `NlvaJ5WE` version `od4ltbRo`, SHA-512 `7e9ecdcc996f376e280bf46f5c0cfd328fb6f1228b1dac469390031458e7a81ad56acff03d9bb6b57b8209586798ab443be1e91fa8fe56297a9e7c9451db03e2`. [Publisher's CurseForge features](https://www.curseforge.com/minecraft/mc-mods/gaze-a-malum-addon) and [exact Modrinth release](https://modrinth.com/mod/gaze-a-malum-addon/version/1.1.7.1) confirm Minecraft 1.21.1 / NeoForge and required Malum 1.8.2 plus Lodestone; Iron's Spells is optional. There is no published official source tree. This Atlas continuation **directly examined user-supplied pinned JAR binaries** and discovered additional registry/recipe content not extracted by Progression PR #375. See the [complete exact-JAR census](../inventories/gaze-1.1.7.1-verified-jar-census.md) and [earlier focused ABI record, corrected](../inventories/gaze-1.1.7.1-pinned-abi-evidence.md). No FTB task or Minecraft gameplay PASS is implied.

## Exact pinned Gaze content — recipe/registry verified; behavior acceptance still outstanding

| Addon family | Publisher-visible count | Native vs addon, evidence and missing mechanics |
|---|---:|---|
| Separate progression codex | 1 | `gaze:encyclopedia_unveiled` is made from `malum:encyclopedia_esoterica` plus 1 Umbral Spirit. Right-click calls `GazeProgressionScreen.getScreen().openCodexViaTransition()`. No separate XP-tree inferred; live UI/entry visibility still untested. |
| Added Geases | **2 exact recipe-backed effects (JAR verified in PR #375)** | `gaze:domain_of_swords` and `gaze:pact_of_encroaching`, from `malum:soul_binding/domain_of_swords` and `malum:soul_binding/pact_of_encroaching_malice`. Distinct from the 37 native Geases. Exact named ingredients and Spirit counts are now independently recovered from PR #375's hash-pinned CI log; effect thresholds, balance, gameplay and non-Geas crafting remain incomplete. |
| Added Rites | **26 additional actual type registrations** | `GazeRiteRegistry` adds 26 distinct `malum:gaze_*` / `malum:corrupt_gaze_*` names through the **shared Malum `RITE_TYPES.register`** API scanned by `getRite`. Exact Spirit patterns documented; native Malum 26 + added Gaze 26 = 52 intended entries. Previous PR #375 non-registration assumption was wrong. |
| Weapons | **6 normal + 2 special Astral Splinter variants** | Exact base `gaze:` registrations: veil_dagger, splintered_world, soul_rapier, spirit_saber, swift_spiral, seidhr. Six ordinary infusion recipes all parsed. Extra `astral_splintered_world` variants in code without independent static recipe; do not treat as ordinary craft branches. |
| Runes | **8 exact Rune items/recipes** | All 8 Runeworking JSONs each consume `malum:umbral_spirit` ×1 as `secondaryInput`; primary item IDs/counts and matching `#curios:rune` tag verified. Mimir/Svalinn/Hodr/Nott are localized names, not registry suffixes. Fafnir base +0.1 Malignant Conversion static modifier identified; client set bonus still needs QA. |
| Curios | **5 exact recipe-backed equipables** | Sloth Brooch (`#curios:brooch`), Idle Meditation and Mage Ethics rings (`#curios:ring`), Arcane Charge Necklace (`#curios:necklace`), Replica Dharmachakra (`#curios:head`). Item IDs, recipes and tags parsed; equipped effects need quantitative review. |
| Pouch | **1 owner-approved native remote pouch** | `gaze:spirit_channel_pouch` infuses `malum:soulwoven_pouch`, inherits generic 512-weight storage (NOT Spirit-only), shift-use binds a location, and ordinary use remotely drops contents in the same loaded dimension (200-tick cooldown). **Explicitly allowed as a native magic-mod feature**; #16 portable bag nesting ban still applies. |
| Anima Bestiary | **JAR-bundled guide entry and MobSpiritPage class** | Confirmed `GazeProgressionScreen`/localization and `MobSpiritPage` class; actual collected mob spirit data, runtime rendering and player unlocks remain client-only acceptance questions. |

## Confirmed exact Gaze Soul Binding paths and named features

The [hash-pinned audit log's complete two Geas recipes](../inventories/gaze-1.1.7.1-pinned-abi-evidence.md) enable a safe late optional quest-source node (ML-43): Domain of Swords requires one `gaze:spirit_saber`, 16 Hallowed Gold, 4 Mimicry Relays, 8 Mnemonic Fragments, and Umbral×1/Arcane×64/Aerial×64/Infernal×64; Encroaching Malice requires 12 Void Salts, 6 Living Flesh, 6 Imitation Hearts, 6 Malignant Pewter, and Umbral×1/Arcane×60/Eldritch×60/Wicked×60. Soul Binding attaches a Geas through the Brazier, not a crafted take-home effect item. **These are late Umbral-gated branches**, not an early forced Gaze tutorial.

[Exact-Gaze JAR census](../inventories/gaze-1.1.7.1-verified-jar-census.md) verifies an **Enchantment Workbench** infusion recipe and its central-book/enchanted-pedestal extraction code, six combat weapon recipes, all eight Rune recipes and 26 distinct additional registered Rite builder patterns. An official release note also describes Corrupted Greater Wicked spawner activation, pending safe client operation review. [1.1.7.1 notes](https://www.curseforge.com/minecraft/mc-mods/gaze-a-malum-addon/files/7261638) name Fafnir's full-Malignant-set bonus. These descriptions are not full item or recipe proof. [Malum native pouch/Berserker policy review](../inventories/malum-1.8.2-berserker-and-pouch-policy.md) verifies all three storage types and records the owner's **2026-11-23 decision** allowing personal storage within magic mods.

## Source-evidence 29-axis boundary

These status rows deliberately do not infer absent features or transfer Malum base mechanics into Gaze without confirmation. `possible` is not a positive capability claim.

| Axis | Gaze 1.1.7.1 verified boundary |
|---|---|
| `gear` | **6 six base weapons + 8 Rune items + 5 Curios** directly registered/parsed; combat effectiveness and special variants require runtime checks. |
| `movement` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `ore_processing` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `storage` | `gaze:spirit_channel_pouch` is **general-purpose inherited Soulwoven storage**, with dump location/dimension components and same-dimension remote item ejection; 512 inherited weighted units. |
| `crafting` | **24 exact native serialized recipes:** 14 Spirit Infusion, 8 Rune Workbench, 2 Soul Binding; item + Spirit costs all parsed; one workbench and one guide crafted by Umbral infusion. |
| `logistics` | Spirit-Channel Pouch binds a location/dimension and drops items at a loaded remote position. **Allowed** because this is an incidental function of an approved magic addon, not a dedicated generic logistics mod/network; nested portable bags remain disallowed by #16. |
| `buffs` | two Geas effects, eight named Rune modifiers and five Curios have class/recipe evidence; check precise event and attribute values. |
| `external_compatibility` | declared: Malum 1.8 and Lodestone 1.8.2 required, Iron's optional; all loaded interactions untested. |
| `mob_farming` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `ore_generation` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `permanent_power` | two exact recipe-backed Geases verified. `SwordDomainGeas` class intercepts outgoing damage and declares projectile firing/sword attribute behavior; `EncroachingGeas` class declares armor, toughness, healing and damage conversions. Effect magnitudes and assembled-client persistence still unverified. |
| `general_generation` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `direct_damage` | six registered base weapon items and `gaze:domain_of_swords` projectile dispatch class confirmed; actual numerical damage values require source-depth or game checks. |
| `block_breaking` | Gaze Lesser Earthen Rite class calls server `destroyBlock`; patterns registered, but area/cadence not yet game-verified. |
| `permanent_pets` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `temporary_pets` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `defense` | Replica Dharmachakra text lists adaptive damage reduction up to 30%; Rune Svalinn/Arcana Shield classes exist; no blanket numerical gameplay PASS. |
| `healing_recovery` | Rune Eir/Idle Meditation Ring native texts describe Soul Ward recovery; specific rate/conditions need deeper bytecode or game proof. |
| `debuff_control` | Rune Ymir weighs enemies down; Gymir pulls targets; Wicked Rite and Blighted rune apply effects; exact target filters need review. |
| `energy` | typed Malum Spirit items drive all 24 recipes; no Gaze-generated FE battery or new universal personal energy store evidenced. |
| `automation_control` | **26 additional Rite type registrations** and `FusionAltarBlockEntity` with 200-tick craft; activation and cadence are distinct QA questions. |
| `agriculture` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `detection_information` | `EncyclopediaGazeItem` right-click opens the dedicated Gaze progression screen; `MobSpiritPage`/Anima Bestiary entry present; rune Mimir can reveal invisible entities. |
| `equipment_augmentation` | eight verified `#curios:rune` items, five other verified Curios tags, seven Better Combat weapon-attribute JSON paths; class-level bonuses partially verified. |
| `world_manipulation` | 26 named Gaze Rite type IDs with exact builder Spirit sequences; examples include registered Earthen block destruction/conversion and special Corrupted Wicked entity spawning, awaiting real-world QA. |
| `world_content` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `death_respawn` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `trade_economy` | unknown: no pinned Gaze source class/recipe or observed runtime action supporting a distinct addon operation. |
| `progression_tiers` | Encyclopedia Esoterica → Unveiled via 1 Umbral Spirit; late Umbral-staged source chapters for 8 Runes, two Geases, workbench, pouch. Codex visibility not presumed to be enforced mechanical gate. |

## Honest player quest placeholder map

The native Malum quest backbone remains unchanged. The [Gaze lesson map](malum-quest-map.md) now has direct source evidence for the Encyclopedia progression UI, workbench, 26 unique Rites, eight Runes, six weapons and two Soul Bindings; no fake FTB task IDs. The pouch is an **allowed native Gaze teaching topic**, not an automatic free starter/quest reward.

**Integration decision (owner clarified 2026-11-23):** All three native `malum:soulwoven_pouch`, `malum:ravenous_pouch` and `gaze:spirit_channel_pouch` items are allowed in ordinary survival progression, including general-item storage and the Gaze pouch's bound-position remote drop. The rejected category is **mods installed only for generic backpacks/logistics**, not item features from selected magic mods. No suppression of existing pouch recipes or special owner exception required. **Separately, #16 still requires no bag inside any other bag across mods**, including these three; no gameplay or FTB code is implemented by this document.

**Remaining to close as quest-source ready:** complete high-priority quantitative effect summaries (particularly Gaze Rite consequences, Geas balance and weapon behaviors), TMM-loaded override/EMC parity, focused client behavior and progression/FTB task attribution for the approved pouch, actual client codex and workbench behavior, and truthful FTB event attribution in future Quest implementation. The **exact pinned-JAR recipes/registries, core item identities, storage mechanics and book-opening path are now substantively closed**; no need to repeat the prior publisher-only survey. Full game acceptance and pack config integration are separate QA, not evidence that these binary registrations are missing.
