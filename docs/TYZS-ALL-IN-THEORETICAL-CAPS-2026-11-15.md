# All-in theoretical noncombat cap audit — current TMM selections

> **15 November 2026 · Source-level bounds audit / not full-loadout optimized or game tested.** Target: **Minecraft 1.21.1 NeoForge**, test manifest of **164 selections, 155 enabled**, actual client/server pins vary across mods and remain subject to installed pack verification. The [all-142-attribute machine ledger](tyzs/theoretical-attribute-hard-caps-v1.json) contains numeric defaults, minimums and hard registration maxima; the [Curios slot/item candidate audit](tyzs/curios-all-in-eligibility-v1.json) and [gear/enchant/affix ceiling audit](TYZS-ENCHANTING-APOTHEOSIS-CURIOS-STACK-AUDIT-2026-11-15.md) identify the source interactions. **This is not a proof that a legitimate character can reach each registry maximum.**

## 1. The governing balance constraint

**All comparable capped noncombat traits**, including Sea Dragon Luck, target:

\[\boxed{M_{\max}=0.85\;X_{\mathrm{best\,legal\,specialist}}}\]

Here **X** is the greatest **attainable** final number after investing **every permitted specialist skill rank** and independently optimizing all legal permanent equipment, enchanted/Iesnium items, Mythic affixes (including Supremacy and legal Malice), sockets, Perfect gems, and full compatible Curios. The Monk's **M** includes its own full native/Core/Monk skill rank investment and compatible Curios, without conventional armor/weapons. A shared Curio is included ONCE on each build. Only the maximum actually reachable under item-type, slot, unique, and enchantment-exclusivity constraints qualifies.

This intentionally demands a *theoretically optimal* specialist, not a representative endgame outfit; the next modeling stage is an explicit constrained item/slot optimizer. **The registered hard bound is merely an upper limit.** E.g. raw Minecraft Luck caps at **1,024**, but the specialist might only be able to build X=350 without violating legal item combinations. In that hypothetical case capped Monk Luck is **297.5**, *not* automatically 870.4. (350 is an illustration, not a measured source max.)

The old Sea Luck 80% exception and prior practical-gearing basis are **superseded** by this 85% owner decision. The strict Chaos Guardian solo without **any worn equipment including Curios** is a separate combat acceptance test; ordinary Monk players can use Curios. Do not apply the scalar 0.85 formula to Boolean powers, damage immunity, combat regeneration, and incomparable blood/lich physiological permissions.

## 2. Hard registration limits we can actually support

Of 142 previously inventoried source candidates, **135 attribute bound triples** are visible in Java source/vanilla 1.21.1 data, and **seven Neo Vitae maxima** are additionally documented in its public API reference but their exact pinned code/minima remain unverified.

| Attribute | Registered hard maximum | Interpretation / trap |
|---|---:|---|
| `minecraft:generic.luck` | **1024** | True Luck, not Fortune/Looting |
| `minecraft:generic.armor` | **30** | Effective mitigation separately caps |
| `minecraft:generic.armor_toughness` | **20** | Effective mitigation separately caps |
| `minecraft:generic.max_health` | **1024** | Attribute engine clamp is not a buildable full-loadout estimate |
| `minecraft:generic.movement_speed` | **1024** | Attribute engine clamp is not a buildable full-loadout estimate |
| `minecraft:player.block_break_speed` | **1024** | Mining speed stacking and tool tier restrictions matter |
| `minecraft:player.mining_efficiency` | **1024** | Mining speed stacking and tool tier restrictions matter |
| `minecraft:generic.attack_damage` | **2048** | Attribute engine clamp is not a buildable full-loadout estimate |
| `apothic_attributes:cooldown_reduction` | **0.95** | 95% CDR upper limit, not unlimited spell speed |
| `apothic_attributes:crit_chance` | **10** | Attribute engine clamp is not a buildable full-loadout estimate |
| `apothic_attributes:dodge_chance` | **1** | Potential hit-avoidance saturation; actual event gating matters |
| `apothic_attributes:mining_speed` | **10** | Mining speed stacking and tool tier restrictions matter |
| `irons_spellbooks:max_mana` | **1000000** | Only this mod's personal mana resource |
| `irons_spellbooks:spell_power` | **100** | Attribute engine clamp is not a buildable full-loadout estimate |
| `irons_spellbooks:mana_regen` | **100** | Attribute engine clamp is not a buildable full-loadout estimate |
| `ars_nouveau:ars_nouveau.perk.max_mana` | **10000** | Only this mod's personal mana resource |
| `ars_nouveau:ars_nouveau.perk.spell_damage` | **10000** | Attribute engine clamp is not a buildable full-loadout estimate |
| `goety:spell_potency` | **2048** | Attribute engine clamp is not a buildable full-loadout estimate |
| `malum:geas_limit` | **8** | Attribute engine clamp is not a buildable full-loadout estimate |
| `eidolon_repraised:magic_power` | **10** | Attribute engine clamp is not a buildable full-loadout estimate |
| `forbidden_arcanus:generic.magic_protection` | **1** | Attribute engine clamp is not a buildable full-loadout estimate |
| `neovitae:blood_shield` | **10** | API reports max 10, but effective shield damage reduction has a lower cap and EV cost |

### All 142 inventoried attributes (source values)

Each row is a **registered/default bound**. Values under “Max” are **not** gear-optimal values and should not be copied into the Monk stat configuration. Source URLs, candidate version notes and open player-instance questions are in the JSON.

#### Vanilla / Minecraft 1.21.1 (30)

| Attribute ID | Default | Minimum | Hard maximum | Evidence |
|---|---:|---:|---:|---|
| `minecraft:generic.armor` | 0 | 0 | 30 | Java/1.21.1 source |
| `minecraft:generic.armor_toughness` | 0 | 0 | 20 | Java/1.21.1 source |
| `minecraft:generic.attack_damage` | 2 | 0 | 2048 | Java/1.21.1 source |
| `minecraft:generic.attack_knockback` | 0 | 0 | 5 | Java/1.21.1 source |
| `minecraft:generic.attack_speed` | 4 | 0 | 1024 | Java/1.21.1 source |
| `minecraft:generic.burning_time` | 1 | 0 | 1024 | Java/1.21.1 source |
| `minecraft:generic.explosion_knockback_resistance` | 0 | 0 | 1 | Java/1.21.1 source |
| `minecraft:generic.fall_damage_multiplier` | 1 | 0 | 100 | Java/1.21.1 source |
| `minecraft:generic.flying_speed` | 0.4 | 0 | 1024 | Java/1.21.1 source |
| `minecraft:generic.follow_range` | 32 | 0 | 2048 | Java/1.21.1 source |
| `minecraft:generic.gravity` | 0.08 | -1 | 1 | Java/1.21.1 source |
| `minecraft:generic.jump_strength` | 0.41999998688697815 | 0 | 32 | Java/1.21.1 source |
| `minecraft:generic.knockback_resistance` | 0 | 0 | 1 | Java/1.21.1 source |
| `minecraft:generic.luck` | 0 | -1024 | 1024 | Java/1.21.1 source |
| `minecraft:generic.max_absorption` | 0 | 0 | 2048 | Java/1.21.1 source |
| `minecraft:generic.max_health` | 20 | 1 | 1024 | Java/1.21.1 source |
| `minecraft:generic.movement_efficiency` | 0 | 0 | 1 | Java/1.21.1 source |
| `minecraft:generic.movement_speed` | 0.7 | 0 | 1024 | Java/1.21.1 source |
| `minecraft:generic.oxygen_bonus` | 0 | 0 | 1024 | Java/1.21.1 source |
| `minecraft:generic.safe_fall_distance` | 3 | -1024 | 1024 | Java/1.21.1 source |
| `minecraft:generic.scale` | 1 | 0.0625 | 16 | Java/1.21.1 source |
| `minecraft:generic.step_height` | 0.6 | 0 | 10 | Java/1.21.1 source |
| `minecraft:generic.water_movement_efficiency` | 0 | 0 | 1 | Java/1.21.1 source |
| `minecraft:player.block_break_speed` | 1 | 0 | 1024 | Java/1.21.1 source |
| `minecraft:player.block_interaction_range` | 4.5 | 0 | 64 | Java/1.21.1 source |
| `minecraft:player.entity_interaction_range` | 3 | 0 | 64 | Java/1.21.1 source |
| `minecraft:player.mining_efficiency` | 0 | 0 | 1024 | Java/1.21.1 source |
| `minecraft:player.sneaking_speed` | 0.3 | 0 | 1 | Java/1.21.1 source |
| `minecraft:player.submerged_mining_speed` | 0.2 | 0 | 20 | Java/1.21.1 source |
| `minecraft:player.sweeping_damage_ratio` | 0 | 0 | 1 | Java/1.21.1 source |

#### Apothic Attributes 2.11.0 (22)

| Attribute ID | Default | Minimum | Hard maximum | Evidence |
|---|---:|---:|---:|---|
| `apothic_attributes:armor_pierce` | 0 | 0 | 1000 | Java/1.21.1 source |
| `apothic_attributes:armor_shred` | 0 | 0 | 2 | Java/1.21.1 source |
| `apothic_attributes:arrow_damage` | 1 | 0 | 10 | Java/1.21.1 source |
| `apothic_attributes:arrow_velocity` | 1 | 0 | 10 | Java/1.21.1 source |
| `apothic_attributes:cold_damage` | 0 | 0 | 1000 | Java/1.21.1 source |
| `apothic_attributes:cooldown_reduction` | 0 | -10 | 0.95 | Java/1.21.1 source |
| `apothic_attributes:crit_chance` | 0.05 | 0 | 10 | Java/1.21.1 source |
| `apothic_attributes:crit_damage` | 1.5 | 1 | 100 | Java/1.21.1 source |
| `apothic_attributes:current_hp_damage` | 0 | 0 | 1 | Java/1.21.1 source |
| `apothic_attributes:dodge_chance` | 0 | 0 | 1 | Java/1.21.1 source |
| `apothic_attributes:draw_speed` | 1 | 0 | 4 | Java/1.21.1 source |
| `apothic_attributes:elytra_flight` | false | false | true | Java/1.21.1 source |
| `apothic_attributes:experience_gained` | 1 | 0 | 1000 | Java/1.21.1 source |
| `apothic_attributes:fire_damage` | 0 | 0 | 1000 | Java/1.21.1 source |
| `apothic_attributes:ghost_health` | 0 | 0 | 1000 | Java/1.21.1 source |
| `apothic_attributes:healing_received` | 1 | 0 | 1000 | Java/1.21.1 source |
| `apothic_attributes:life_steal` | 0 | 0 | 10 | Java/1.21.1 source |
| `apothic_attributes:mining_speed` | 1 | 0 | 10 | Java/1.21.1 source |
| `apothic_attributes:overheal` | 0 | 0 | 10 | Java/1.21.1 source |
| `apothic_attributes:projectile_damage` | 1 | 0 | 10 | Java/1.21.1 source |
| `apothic_attributes:prot_pierce` | 0 | 0 | 34 | Java/1.21.1 source |
| `apothic_attributes:prot_shred` | 0 | 0 | 0.9 | Java/1.21.1 source |

#### Iron's Spells 3.16.3 (26)

| Attribute ID | Default | Minimum | Hard maximum | Evidence |
|---|---:|---:|---:|---|
| `irons_spellbooks:blood_magic_resist` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:blood_spell_power` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:cast_time_reduction` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:casting_movespeed` | 1 | 0 | 100 | Java/1.21.1 source |
| `irons_spellbooks:cooldown_reduction` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:eldritch_magic_resist` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:eldritch_spell_power` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:ender_magic_resist` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:ender_spell_power` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:evocation_magic_resist` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:evocation_spell_power` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:fire_magic_resist` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:fire_spell_power` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:holy_magic_resist` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:holy_spell_power` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:ice_magic_resist` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:ice_spell_power` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:lightning_magic_resist` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:lightning_spell_power` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:mana_regen` | 1 | 0 | 100 | Java/1.21.1 source |
| `irons_spellbooks:max_mana` | 100 | 0 | 1000000 | Java/1.21.1 source |
| `irons_spellbooks:nature_magic_resist` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:nature_spell_power` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:spell_power` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:spell_resist` | 1 | -100 | 100 | Java/1.21.1 source |
| `irons_spellbooks:summon_damage` | 1 | -100 | 100 | Java/1.21.1 source |

#### Ars Nouveau (source-drift caveat) (9)

| Attribute ID | Default | Minimum | Hard maximum | Evidence |
|---|---:|---:|---:|---|
| `ars_nouveau:ars_nouveau.perk.drygmy` | 0 | 0 | 1024 | Java/1.21.1 source |
| `ars_nouveau:ars_nouveau.perk.feather` | 0 | 0 | 1 | Java/1.21.1 source |
| `ars_nouveau:ars_nouveau.perk.mana_regen` | 0 | 0 | 2000 | Java/1.21.1 source |
| `ars_nouveau:ars_nouveau.perk.max_mana` | 0 | 0 | 10000 | Java/1.21.1 source |
| `ars_nouveau:ars_nouveau.perk.saturation` | 1 | 0 | 10000 | Java/1.21.1 source |
| `ars_nouveau:ars_nouveau.perk.spell_damage` | 0 | 0 | 10000 | Java/1.21.1 source |
| `ars_nouveau:ars_nouveau.perk.warding` | 0 | 0 | 1024 | Java/1.21.1 source |
| `ars_nouveau:ars_nouveau.perk.weight` | 1 | 0 | 100 | Java/1.21.1 source |
| `ars_nouveau:ars_nouveau.perk.wixie` | 1 | 0 | 1024 | Java/1.21.1 source |

#### Goety 3.2.2 (27)

| Attribute ID | Default | Minimum | Hard maximum | Evidence |
|---|---:|---:|---:|---|
| `goety:abyss_discount` | 0 | -1 | 1 | Java/1.21.1 source |
| `goety:abyss_potency` | 0 | 0 | 2048 | Java/1.21.1 source |
| `goety:casting_speed` | 0 | -1 | 1 | Java/1.21.1 source |
| `goety:cooldown_discount` | 0 | -1 | 1 | Java/1.21.1 source |
| `goety:frost_discount` | 0 | -1 | 1 | Java/1.21.1 source |
| `goety:frost_potency` | 0 | 0 | 2048 | Java/1.21.1 source |
| `goety:geomancy_discount` | 0 | -1 | 1 | Java/1.21.1 source |
| `goety:geomancy_potency` | 0 | 0 | 2048 | Java/1.21.1 source |
| `goety:necromancy_discount` | 0 | -1 | 1 | Java/1.21.1 source |
| `goety:necromancy_potency` | 0 | 0 | 2048 | Java/1.21.1 source |
| `goety:nether_discount` | 0 | -1 | 1 | Java/1.21.1 source |
| `goety:nether_potency` | 0 | 0 | 2048 | Java/1.21.1 source |
| `goety:soul_discount` | 0 | -1 | 1 | Java/1.21.1 source |
| `goety:spell_burning` | 0 | 0 | 2048 | Java/1.21.1 source |
| `goety:spell_duration` | 0 | 0 | 2048 | Java/1.21.1 source |
| `goety:spell_potency` | 0 | 0 | 2048 | Java/1.21.1 source |
| `goety:spell_radius` | 0 | 0 | 2048 | Java/1.21.1 source |
| `goety:spell_range` | 0 | 0 | 2048 | Java/1.21.1 source |
| `goety:spell_velocity` | 0 | 0 | 2048 | Java/1.21.1 source |
| `goety:storm_discount` | 0 | -1 | 1 | Java/1.21.1 source |
| `goety:storm_potency` | 0 | 0 | 2048 | Java/1.21.1 source |
| `goety:void_discount` | 0 | -1 | 1 | Java/1.21.1 source |
| `goety:void_potency` | 0 | 0 | 2048 | Java/1.21.1 source |
| `goety:wild_discount` | 0 | -1 | 1 | Java/1.21.1 source |
| `goety:wild_potency` | 0 | 0 | 2048 | Java/1.21.1 source |
| `goety:wind_discount` | 0 | -1 | 1 | Java/1.21.1 source |
| `goety:wind_potency` | 0 | 0 | 2048 | Java/1.21.1 source |

#### Malum (source drift versus pin) (15)

| Attribute ID | Default | Minimum | Hard maximum | Evidence |
|---|---:|---:|---:|---|
| `malum:arcane_resonance` | 1 | 0 | 2048 | Java/1.21.1 source |
| `malum:charge_capacity` | 0 | 0 | 2048 | Java/1.21.1 source |
| `malum:charge_duration` | 0 | 0 | 2048 | Java/1.21.1 source |
| `malum:charge_recovery_rate` | 1 | 0 | 2048 | Java/1.21.1 source |
| `malum:geas_limit` | 2 | 0 | 8 | Java/1.21.1 source |
| `malum:malignant_aegis_capacity` | 0 | 0 | 2048 | Java/1.21.1 source |
| `malum:malignant_aegis_recovery_gain` | 1 | 0 | 2048 | Java/1.21.1 source |
| `malum:malignant_aegis_recovery_rate` | 1 | 0 | 2048 | Java/1.21.1 source |
| `malum:malignant_conversion` | 0 | 0 | 1 | Java/1.21.1 source |
| `malum:scythe_proficiency` | 1 | 0 | 2048 | Java/1.21.1 source |
| `malum:soul_ward_capacity` | 0 | 0 | 2048 | Java/1.21.1 source |
| `malum:soul_ward_integrity` | 1 | 0 | 2048 | Java/1.21.1 source |
| `malum:soul_ward_recovery_gain` | 1 | 0 | 2048 | Java/1.21.1 source |
| `malum:soul_ward_recovery_rate` | 1 | 0 | 2048 | Java/1.21.1 source |
| `malum:spirit_spoils` | 0 | 0 | 2048 | Java/1.21.1 source |

#### Eidolon Repraised (4)

| Attribute ID | Default | Minimum | Hard maximum | Evidence |
|---|---:|---:|---:|---|
| `eidolon_repraised:chanting_speed` | 1 | 0 | 100 | Java/1.21.1 source |
| `eidolon_repraised:magic_power` | 1 | 0 | 10 | Java/1.21.1 source |
| `eidolon_repraised:max_soul_hearts` | 60 | 0 | 2000 | Java/1.21.1 source |
| `eidolon_repraised:persistent_soul_hearts` | 0 | 0 | 2000 | Java/1.21.1 source |

#### Forbidden & Arcanus (2)

| Attribute ID | Default | Minimum | Hard maximum | Evidence |
|---|---:|---:|---:|---|
| `forbidden_arcanus:generic.aureal_regeneration` | 0 | 0 | 1.7976931348623157e308 | Java/1.21.1 source |
| `forbidden_arcanus:generic.magic_protection` | 0 | 0 | 1 | Java/1.21.1 source |

#### Neo Vitae (documentation-only maxima) (7)

| Attribute ID | Default | Minimum | Hard maximum | Evidence |
|---|---:|---:|---:|---|
| `neovitae:blood_shield` | 0 | unknown | 10 | API documentation only |
| `neovitae:blood_siphon` | 0 | unknown | 1024 | API documentation only |
| `neovitae:bonus_sacrifice` | 0 | unknown | 1000 | API documentation only |
| `neovitae:bonus_self_sacrifice` | 0 | unknown | 1000 | API documentation only |
| `neovitae:bonus_spiritus` | 0 | unknown | 1000 | API documentation only |
| `neovitae:player.self_sacrifice_multiplier` | 1 | unknown | 100 | API documentation only |
| `neovitae:sigil_cost_reduction` | 0 | unknown | 100 | API documentation only |

## 3. Exact source-derived equipment stack examples — not a final gear optimizer

In the exact pinned Apotheosis 8.9 / Placebo 9.9.4 source, Mythic Lucky+Fortunate at affix level 1.0 give **+6/+5 Luck**, and Supremacy level 1.5 gives **+7.5/+6**. Perfect Splendor gives **+5 raw Luck per legal armor socket**. Four Mythic 4-socket armor pieces carrying both affixes and four Perfect Splendors generate a paper armor subtotal of **+124 Luck standard** or **+134 with Supremacy**, before held gear, Royalty multiplier, Curios or specialist skill ranks. Actual double-affix co-roll/obtainment, Royalty replacement, Malice loss trades and maximum usable socket tier need validation.

Apothic default source power-200 cap formula plus Iesnium +1 and fully compatible gem/affix enchantment boosts suggest conditional effective **Fortune XIX, Efficiency XVI, Sharpness XVI, Protection XV** on appropriate equipment. Such paper levels are **not** guaranteed valid after loaded data-map/slot/effect checks. See the [full source breakdown](TYZS-ENCHANTING-APOTHEOSIS-CURIOS-STACK-AUDIT-2026-11-15.md). They are distinct from raw Luck and other stats.

## 4. Curios / Relics and the removed Seven Sins Ring

**Curios is allowed on ordinary Monk builds.** The item eligibility and final slot count, not a broad Curios category, define how much power a wearer gets. The current Relics 1.21.1 source line lists **eight Curios item-tag categories and 15 wearable entries**, including the forbidden Ring; this exact source line has advanced to **0.12.11** versus the TMM **0.12.5** pin. Verify the precise pinned JAR before claiming those items are all present in the current test build.

| Relics Curio category | Source tag entries |
|---|---|
| `back` | `relics:midnight_mantle`, `relics:leafy_mantle`, `relics:glitchy_mantle`, `relics:ghostly_mantle` |
| `belt` | `relics:kinetic_belt`, `relics:hunting_belt` |
| `charm` | `relics:experience_disperser` |
| `feet` | `relics:roller_skate`, `relics:springy_boot`, `relics:cut_glass_boot` |
| `hands` | None registered by Relics itself |
| `head` | `relics:piglin_mask`, `relics:chef_hat` |
| `necklace` | `relics:reflective_necklace`, `relics:jellyfish_necklace` |
| `ring` | `relics:ring_of_the_seven_deadly_sins` |

The **Ring of the Seven Deadly Sins** (`relics:ring_of_the_seven_deadly_sins`) is explicitly **ineligible for all-in balance builds**. **PR #130** on `test/full-integration` blocks Ring generation through Relics chest loot, and **PR #132** removes it from the default `curios:ring` item tag with KubeJS, blocking normal new equips. Existing/creative/command-supplied Rings are still possible; do not count them as legal pack equipment, and a runtime ban for pre-existing copies would require additional code or administrative cleanup.

Additional current Curios candidates include Artifacts, Relics add-ons, Enigmatic Legacy+, Draconic Evolution body modules, Botania baubles, Iron/Ars spellcasting accessories, Malum, Goety and Neo Vitae. **None** is automatically eligible for four Apotheosis gems just because it occupies a Curios slot; affix loot categories and gem class checks remain independent.

## 5. Status and the remaining calculation before numeric Monk skills

The next deliverable is a **gear-loadout optimizer**, separately for each noncombat property: build possible *legal* item sets, slot limits, enchantment exclusivity groups, gem uniqueness per item, affix stat/ability slots, overcharge reset tradeoffs, Curios, source-specific caps and all ranks. Retain the winning legal loadout (with item IDs and source provenance), compute **X**, then set final capped Monk attribute to **0.85X** and distribute its remaining purchased innate bonus over expensive late levels.

**Still blocked from exact X:** no actual assembled-client 1.21.1 enchantment registry and attribute/player instances, no guaranteed best-stat item source/Curios inventory for all 155 enabled mods, no confirmed Draconic body-curio legality, no exhaustive Relics item quality/upgrade cap snapshot from pinned 0.12.5, and no guaranteed legality of extraordinary four-socket double-affix gear. The old 142 source candidate inventory includes some potentially non-player attributes. The seven Neo Vitae values are documented maxima but not registered minima from its exact pinned JAR. No game/server test was run in this pass.

This is a **research-backed hard-cap ledger and item-eligibility audit**, not a claim the 168 proposed Tyz skills already match those caps.
