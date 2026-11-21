# Ars Nouveau addon family — version-pinned capability crosswalk (first-pass Atlas)

**Scope:** eight TMM **selected/enabled-in-manifest** modules, namely Ars Nouveau **5.13.5** plus **seven** addons. Research uses the complete existing [mod comprehension Ars family](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/docs/mod-comprehension/mods/ars-nouveau.md) and source-aligned primary repositories; modules appear in `test/full-integration/manifest/mods.json`. This is an **inventory of candidate capabilities**, not proof of every addon API functioning in the assembled game. No KubeJS or native addon files changed. The 49-item existing Ars pilot manifest is in `test/full-integration/manifest/ars_quest_pilot.json`; see [quest gap matrix](../mods/ars-nouveau-quest-map.md).

## Parent Ars 5.13.5 baseline

The **base mod** provides tiered personalized spells, Source production and machine crafting, four major spirit-like workers (Starbuncle/Whirlisprig/Drygmy/Wixie), natural familiars, mob containers, rituals, a boss and a material ecosystem. Its [82 core glyph recipes](ars-nouveau-glyph-recipes.md), [24 tablet recipes + Brazier](ars-nouveau-ritual-tablets.md), and [selected critical acquisition recipes](ars-nouveau-core-infrastructure-recipes.md) are separately indexed by exact source JSON. Source and player mana are **not the same energy**, and neither is FE; **27/55/160 vanilla XP for learning a glyph is not spellcasting mana or Tyz MXP**.

## Ars Additions — `ars_additions` 21.3.0

[Existing full profile](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/docs/mod-comprehension/mods/ars-additions.md) · [release-version source](https://github.com/Jarva/Ars-Additions/tree/91f102a90dc058cf40e4eac5a67a881e48b856b4) · declared against Ars 5.11.2 (TMM uses 5.13.5).

| Actual mechanism | Lower → higher capability | Concrete operation / cost / quest lesson | Atlas axes / caveats |
| --- | --- | --- | --- |
| **Advanced Dominion Wand** | Single target → multi-target bulk linking | Craft addon wand; select compatible endpoints and configure many Ars handlers | `logistics,automation_control`; intended endpoint types from addon profile; no free Source |
| **Warp Index; Stabilized Warp Index** | Remote Storage Lectern → cross-dimensional remote inventory | Bind to Storage Lectern, interact with loaded destination; stabilized route changes allowed dimension | `storage,movement,logistics`; loaded target remains requirement |
| **Ender Source Jar** | Local ordinary Jar → shared/linked Source pool | Pair addon jars and supply one with Source to draw elsewhere | `energy,storage,logistics`; storage network, **not Source generator** |
| **Mark, Recall, Unstable Reliquary** | Capture target reference → later remote targeting | Mark block/entity target; use Recall with stored reference, validate target eligibility | `movement,debuff_control,automation_control`; exact target type/limits require release-artifact audit |
| **Retaliate cast form** | Ordinary caster-selected target → last attacker | Spell form uses attacker from last 5 seconds | `direct_damage,defense`; timer and exclusions source README claim, not locally runtime tested |
| **Ruined Warp Portal / Nexus Tower** | Find portal ruins → travel network | Locate structures, recover Explorer's Warp Scroll, connect waypoint network | `world_content,movement`; placement frequency and portal gate need exact-pin metadata |
| **Codex Entries** | Tier-1 loot glyph teaching → claimed higher tier Codex | Obtain/craft/learn glyph from Codex; loot gating for upper tiers uncertain | `permanent_power,progression_tiers`; upper codices reported not ordinarily obtainable |
| **Arcane Permanent ritual** | Optional sustained / chunk-loader effect | Source README says disabled by default | `automation_control,energy`; do **not** advertise as enabled without pack configuration evidence |
| **Wixie Enchanting Apparatus** | Base Wixie cauldron → apparent Apparatus automation | Configure worker near Apparatus and valid ingredient inventories | `crafting,logistics,automation_control`; README only, unpiloted |

## Ars Controle — `ars_controle` 1.6.16

[Existing profile](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/docs/mod-comprehension/mods/ars-controle.md) · [1.6.16 source](https://github.com/Vonr/Ars-Controle/tree/14c5f4770a9ec265491fb9a7ae1a60a2123dfbc0) · compiled against Ars 5.13.1.

| Mechanism | Lowest practical setup → richer use | Inputs / outputs and operating limits | Axes |
| --- | --- | --- | --- |
| **Remote** | Manual Dominion Wand → crouch-use remote, select endpoint and single/multiple target | Select target/other endpoint, left-click air to clear; cross-dimension/invalid endpoint rejection | `logistics,automation_control` |
| **Scryer's Linkage** | Adjacent native handlers → remote capability bridging | First link target with Dominion Wand then Linkage; handles compatible item, **fluid**, FE, redstone and other channels (subject to endpoint) | `logistics,energy,storage`; **FE is transported, not inherently produced by Ars Source** |
| **Temporal Stability Sensor** | Comparator signal → throttled farm circuit | Read world load condition and use redstone cutoff; output signals load rather than fixing lag itself | `detection_information,automation_control` |
| **Portable Brazier Relay** | Fixed ritual → ritual effect follows player | Bind to loaded and supplied source ritual Brazier; some effects blacklisted | `movement,buffs,automation_control`; does not remove parent ritual supply cost |
| **Warp Scroll Holder** | Hand-thrown Warp Scroll → redstone/configured portal holder | Install in portal frame, insert written scroll; needs Source within **10 blocks Manhattan** per addon guide | `movement,world_manipulation,automation_control` |
| **Warping Spell Prism** | Local projectile → routed projectile at distant/cross-dimensional position | Configured target; CC:Tweaked may set block coordinates | `movement,logistics,automation_control,external_compatibility`; world/claim loading/safety |
| **Boolean/conditional/Precise Delay glyphs** | Single branch → NOT/OR/XOR/XNOR and height/random gated spell control | Scribe glyphs, sequence condition before effects; Random base chance 50%, delay uses Extend Time | `direct_damage,automation_control,debuff_control`; exact additional glyph recipes need index |

## Ars Elemental — `ars_elemental` 0.7.10.4

[Existing profile](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/docs/mod-comprehension/mods/ars-elemental.md) · [0.7.10.4 source](https://github.com/Alexthw46/Ars-Elemental/tree/2c433b6a4e6ff5c3b314b5f5442881e0ad2dd0c) · upstream source examples for some focus changes reference **0.7.11.0**, so lower confidence without version match.

| Branch | Lower → upper | Concrete carriers / resources / outputs | Coverage limits |
| --- | --- | --- | --- |
| **Fire/Water/Air/Earth foci** | Lesser focus → major focus, school-biased spell power/discount; sacrifices other schools | Equip chosen focus, build matching spell; test reported focus behavior only if TMM patch requires it | Not a fourth Ars spellbook tier; four optional school choices |
| **Necrotic/Anima focus** | School-based necromancy specialization | Distinct power theme and spell changes; must separate from TMM **Anima** in Neo Vitae (name collision, unrelated energy ledger) | Exact pin 0.7.10.4 rules not all enumerated |
| **Elemental armor** | Single elemental piece → set bonus / higher tier / Mark of Mastery | Four element armor sets with school bonus, damage mitigation and source-reported mana recovery on full set; upgrade and retained thread rules are important | Equipment rather than intrinsic permanent skill |
| **Urn of Endless Waters** | Source-consuming water supply → routing to supported consumers | Use Source to fill cauldrons or compatible destination such as Botania Apothecary | `general_generation,energy,external_compatibility`; Source→water, **not FE** |
| **Siren + Siren Shrine** | Befriend with sea pickle → shrine aquatic habitat + Source → repeat fishing loot | Nearby biodiversity and Source drive supported loot cycles | `permanent_pets,mob_farming,agriculture`; fishing loot must respect TMM gated items |
| **Flashjack/Flarecannon/Weald Walker** | Summon/befriend helper → patrol/turret synergy | Compatible target acquisition, brazier/Source/glyph actions as applicable | `permanent_pets,defense,automation_control`; exact tame/arming recipes not audited |
| **Elemental turret, Source relay and prism family** | School-specific turret → focus-discount and projectile manipulation | Upgrade existing Ars spell turret and Source relay/prism effects; some consume Source | `direct_damage,energy,logistics,automation_control` |
| **Biomes/structures and Vexing Caves** | Discover specialized plants/creatures → extra resources/boss-style environments | Native worldgen and addon resources | `world_content`; confirm what actual 0.7.10.4 release ships |

## Ars Elemancy — `ars_elemancy` 1.18.3

[Profile](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/docs/mod-comprehension/mods/ars-elemancy.md) · [1.18.3 source](https://github.com/Lyrellion/Ars-Elemancy/tree/dfb18286106aca1ca39a9b0053d64a1ef5041751)

This addon makes **six pairwise elemental specializations and one four-element capstone**. All seven source recipes use the Ars **Imbuement Chamber** with **3,000 Source**, a central reagent, and nonconsumed elemental Essence pedestal catalysts. Source claims are exact addon-source datagen, not client-confirmed.

| Output | Central reagent | Pedestal catalyst(s) | Wearable Focus/armor specialization |
| --- | --- | --- | --- |
| Cinder Essence | Manipulation Essence | Fire + Air | Cindermancer |
| Tempest Essence | Manipulation Essence | Water + Air | Tempestmancer |
| Silt Essence | Manipulation Essence | Earth + Air | Siltmancer |
| Mire Essence | Manipulation Essence | Water + Earth | Miremancer |
| Vapor Essence | Manipulation Essence | Water + Fire | Vapormancer |
| Lava Essence | Manipulation Essence | Fire + Earth | Lavamancer |
| Elemancer Shard | Source Gem | Air + Fire + Water + Earth | four-element Elemancer |

**Do not force every specialization as mandatory quests.** Teach making one dual essence, explain choice matrix, demonstrate a Focus/armor family, and add an optional four-element endcap.

## Ars 'n Spells — `ars_n_spells` 3.3.5

[Full mechanics/recipe profile](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/docs/mod-comprehension/mods/ars-n-spells.md) · [3.3.5 source](https://github.com/otectus/ars-n-spells/tree/f25c379d5e0521821ab6e80a9f485ae41d4506a0)

| Capability | Exact model and progression | Cost/limits and design relevance |
| --- | --- | --- |
| **Mana unification** | Modes **`iss_primary`, `ars_primary`, `hybrid`, `separate`, `disabled`** (server config) | Fresh addon default `iss_primary`; **effective TMM mode not established**. Must not double-count two mana pools; mode also controls UI. Source/FE separate |
| **Cross-school spell power** | Iron's school power scales Ars damage; Ars bonus contributes to Iron's; primary/max/average policy for mixed schools | Default scalar max 3.0, even when mana unification is disabled. Layer with TMM Progression effects without accidental doubling |
| **School practice & affinity** | Cast counters and recency-driven school focus | Fresh default 0.1%/cast to +25% after 250 casts; affinity decay off by default. This is a **separate persistent bonus**, not Tyz's MXP |
| **Resonance and cooldowns** | 95%-full mana threshold + 100-tick linger; optional shared cooldown categories; proxy cast cooldown | Category-wide cooldown off in default addon config; bound proxy defaults 40 ticks. Full/empty mana and refused casts matter |
| **Spell Loom** | Ars spell source + blank scroll → actual Iron's scroll carrier | Blank Scroll = Ars parchment + Source Gem; filled parchment consumed, Ars book/focus reusable. Source recipe has gold-over-lapis/book/obsidian material form |
| **Binding to Iron's spellbook** | Spellbook Binding Ars ritual **2,500 Source** (or native Iron's Inscription Table) | Scroll + Iron's book within 3 blocks of Brazier, clear unrelated drops. Cast via Iron's wheel. Eight registered proxy slots, default config cap -1 |
| **Generic Spell Transcription** | A *different* ritual, **2,000 Source**, Ars/Iron's spell source → blank item | Right-click target cast, 1.25 default cross-cast cost multiplier, not necessarily a wheel slot |
| **Spell Uninscription** | **500 Source** ritual removes inscription | Reusable carrier reset. Distinguish from deleting a learned glyph |
| **Mana Infusion** | **1,500 Source** ritual grants player mana | Fresh configurable grant 500, directed to chosen pool; addon has player award logic |
| **Mana Well** | **2,000 Source** ritual; source-defined near-player ongoing mana gain | Source default range 8 AABB, **2 mana/tick/player = 40/sec** at 20 TPS while active; ritual duration and total cost not established. May affect cap/balance significantly |

**Owner review / decision candidates:** lock the active TMM mana mode, school-power stacking, passive Mana Well throughput, and progression MXP interaction after inspecting live config and existing approved rules. These are **pack-owned cross-mod settings**, not reasons to regression-test unmodified native spells.

## Ars Ocultas — `ars_ocultas` 2.6.2

[Full evidence profile](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/docs/mod-comprehension/mods/ars-ocultas.md) · [2.6.2 source](https://github.com/dphaldes/Ars-Ocultas/tree/14aa0f4a05f8e85aca03b9bbe084e2bff36b007b)

| Mechanism | Input / cost | Output and notes | TMM impact |
| --- | --- | --- | --- |
| **Spirit Attuned Gem alternative** | Ars Apparatus central Source Gem + Conjuration/Manipulation/Occultism Demon's Dream Essences, **500 Source** | One Occultism `spirit_attuned_gem` | Alternate occultism material path, not a source of free gems |
| **Occultism spirits in Ars Mob Jars** | Bound Foliot, Djinni, Afrit, Marid in a filled Ars Mob Jar | Retain their Occultism jobs, filtered nearby pickups (7×7×7 scan), item handler, output to adjacent inventories, no normal jar decay per source | Very useful storage/process miniaturization; respect Occultism tier and source conditions |
| **Jarred Janitor / worker forwarding** | Spirit job plus nearby eligible item and adjacent inventory | Janitor moves held item; Crusher, Smelter, Crystallizer and Trader jobs can output adjacent | Input filter and overflow are **Occultism job** concerns, not generic free conversion |
| **Silver↔Gold transformation** | Ars Imbuement input metal + Manipulation Essence + Spirit Attuned Gem + direction reagent, **2,000 Source** per ingot | One ingot in other metal | Explicitly 1-to-1; source economics not universally profitable |
| **Occultism Tier-4 Storage Stabilizer** | Bound Marid book / appropriate pentacle and addon-specified ritual reagents | Access to `occultism:storage_stabilizer_tier4` | Must retain legitimate Occultism Marid tier; no skip merely from Ars Apparatus |
| **Sacrificial Altar** | Craft with Golden Sacrificial Bowl + four Occultism Silver, two Sourcestone, Drygmy Shard, Mob Jar and **5,000 Source** | Converts another **5,000 Source per pending Occultism bowl sacrifice/item-use flag**; source implementation doesn't consume jarred entity or matching altar item | **Owner design decision**: is reusable nonconsuming sacrifice/item fulfillment intentional? Keep separate from native mod regression testing; can bypass TMM-designed consumable gates |
| **Soul Gem ↔ Mob Jar bridge** | Empty/filled Occultism Soul Gem; config both directions enabled by default | Retains entity/job NBT for allowed targets; **source predicts potential data loss if placement rejects after removing stored entity data** | Specific code-order bug lead: do not promise safe denied transfer; escalate only if gated creatures/items depend on this path |

The metadata contains an apparently narrow Minecraft version range despite the same artifact appearing in an official ATM10 1.21.1 inventory. **Do not assert a TMM load failure** from the range alone.

## Ars Unification — `ars_unification` 1.2.23

[Profile](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/docs/mod-comprehension/mods/ars-unification.md) · [release source](https://github.com/Vonr/Ars-Unification/tree/1786bff672bcf6f6a9c6f571c9c140bbad4ab05c)

- **Installed recipe adapters:** Farmer's Delight **Cut** (Cutting Board) and Occultism **Crush** (Crusher Spirit). Spells learn via base Ars glyph paths; use JEI for matched recipe output. **Ars-native recipes win on default conflict**, with config ordering/regex exclusions. Supporting a recipe does not imply it is free or automatically duplicate-safe.
- **Absent from the currently inspected TMM roster:** Actually Additions, AE2, Ender IO, Immersive Engineering, Integrated Dynamics, Mekanism, Modern Industrialization, Oritech and Not Enough Glyphs press/flatten path. Do not invent quests for those adapters.
- Teach `Touch → Cut → Sensitive` as a representative Farmer's Delight operation, and an independent Occultism recipe whose **Crush glyph** uses a supported Spirit-Crusher conversion. The existing 49-node manifest only teaches the Farmer's Delight route. Check recipe tags/conflict priority **statically** against actual overrides before proposing economic changes.

## Review priority and scope

1. **Design first:** Ars 'n Spells effective mode/mana multipliers; Ars Ocultas nonconsuming sacrificial altar vs TMM Occultism gates; Drygmy output blacklist; Ars Additions chunk loading policy; Elemental Source→water and Siren loot if economy-sensitive.
2. **Implementation-ready (research artifacts, not runtime):** assemble optional how-to source nodes for unpiloted addon surfaces; cross-link all seven addon profiles, preserve chapter GUIDs, avoid duplicate 49-node quest implementation from #395.
3. **Only changed-code QA:** if TMM writes a custom source handler, recipe adapter, blacklist, XP/MXP bridge or quest detector, the narrow affected boundary needs testing. Do **not** add standalone native Tome-like verification of untouched addon behavior just because a user is taught how to operate it.

**Evidence confidence:** version-aligned primary code/README and existing deeper profile versus the installed pin; no client run, no first-party mod changes, no tested compatibility claims.
