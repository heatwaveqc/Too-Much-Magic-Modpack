> **Decision update, 2026-11-14:** Tyz's Skills 6.4.2 is the selected
> leveled backend. Default **five consecutive 99-rank stages**, with
> per-rank effect and price multiplied by 10 at each stage; sixth is
> optional only where the underlying attribute genuinely supports it.
> See [tier policy](MONK-TYZS-TIERED-ADVANCEMENT.md).
> This catalog remains source-candidate based, not yet a compiled UI.

# TMM Monk skill catalog v1 — backend and placement agnostic

**State: proposed skill roster for review, not gameplay or a final balance
decision.** 2026-11-14. Source authority:
[Full machine-readable skill inventory](../compat/monk/profiles/skill-catalog-v1.json),
[98 proposed Apotheosis adaptations from 99 upstream definitions](../compat/monk/profiles/skill-affix-techniques-v1.json),
and [original Monk 25-ability crosswalk](../compat/monk/profiles/original-monk-abilities-1.4.json).

**Strong design boundaries:**

- The Monk remains unarmed/unarmored, allows Curios, and excludes dragon,
  dracolich, lich and abomination forms.
- **No Monk Creative Flight skill.** Draconic Evolution flight modules in
  a permitted Curio remain the external provider. Momentum Gliding is
  innate and unlocked after the Ender Dragon.
- **Harvest tier** has finitely many actual effective tier unlocks, capped
  by the best obtainable tool tier and block requirements in the pack.
  **Harvest speed** is separately repeatable at increasing cost. A high
  speed must NEVER permit invalid drops or bypass a tool requirement.
- Numeric talents get either **repeatable** ranks until their actual
  effective/native limit (no arbitrary Monk rank cap) or **bounded** ranks
  when the effect genuinely stops improving. No wasted paid ranks.
- Unique technique unlocks are **once**. Multi-stage practices have
  **staged** ranks, with the number defined by actual useful modes.
- Exactly **98 Apotheosis techniques** from 99 pinned definitions: **Unbound excluded entirely**,
  scale only through **distinct source-supported effect** tiers. Identical
  rarity values collapse; 32 affixes have such duplicates. Source affixes
  with no rarity scale are single-unlock. A separate, continuously
  improvable numeric stat may also contribute to the same effect;
  prevent accidental double application. Rarity rank means deterministic
  upper source-tier effect, not a random item rarity roll.
- **Owner-selected backend: Tyz's Skills 6.4.2** for leveled traits,
  with each 99-rank skill followed by tenfold higher-value/cost skill
  tiers when a stat meaningfully supports more investment. The Monk
  native faction tree retains single-use powers and permissions;
  Pufferfish retains nonoverlapping one-time shared choices.
  **Exact skill/world-tier placement remains undecided**, beyond the
  post-Dragon momentum glide. No Tyz runtime integration has been built.
- Additional mana/attunement and summoning talents are **conditional**
  on verified player-specific API hooks in the exact pinned mods. No
  imaginary attributes should be awarded just to hit a count.

## Catalog totals

| Record group | Count |
| --- | ---: |
| Core, registered-stat, and cross-mod character skills | **172** |
| Apotheosis individual affix techniques | **98** |
| **Total preliminary skill records** | **270** (before unification) |
| Registered candidate attributes accounted for | 144 (prior 142 + 2 NeoForge affix-derived) |
| Attribute candidates intentionally not selected | 3 |
| Original Monk ability factories explicitly mapped | 25/25 |

The 270 candidate records are **not** 270 unrelated purchasable upgrades: some numerical traits
underpin several affix techniques, and one harvest-speed skill deliberately
combines three overlapping registry stats. The catalog gives each
progression purchase one owner in the eventual implementation.

### Level semantics

| Model | Allowed levels |
| --- | --- |
| **repeatable** | No arbitrary number of ranks; escalating cost and native/effective max guard |
| **bounded** | Finite useful ranks only; upper bound is effect-specific, confirmed from runtime |
| **once** | Exactly 1 |
| **staged** | Exact count of useful modes: Breath Meditation 2; harvest tiers from final pack data |
| **affix distinct effect ranks** | From 1–5 nonredundant source-effect milestones; absent rarity values means 1 |

## Core and mod-specific skill inventory

### Body and defensive disciplines (16)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **Absorption Capacity** | Repeatable | `minecraft:generic.max_absorption` |
| **Blast Rooting** | Bounded | `minecraft:generic.explosion_knockback_resistance` |
| **Body Toughness** | Repeatable | `minecraft:generic.armor_toughness` |
| **Deep Breathing** | Bounded | `minecraft:generic.oxygen_bonus` |
| **Evasion** | Bounded | `apothic_attributes:dodge_chance` |
| **Explosion Resilience** | Bounded | Incoming explosion damage mitigation; different from blast knockback resistance. |
| **Feather Fall** | Bounded | `minecraft:generic.fall_damage_multiplier` |
| **Fire Resilience** | Bounded | Passive mitigation of fire damage independent from native burning duration. |
| **Flame Endurance** | Bounded | `minecraft:generic.burning_time` |
| **Ghost Health** | Repeatable | `apothic_attributes:ghost_health` |
| **Healing Receptivity** | Repeatable | `apothic_attributes:healing_received` |
| **Lava Survival** | 1 | Special brief protection when in lava; not unconditional hazard immunity. |
| **Natural Armor** | Repeatable | `minecraft:generic.armor` |
| **Overheal** | Repeatable | `apothic_attributes:overheal` |
| **Stability** | Bounded | `minecraft:generic.knockback_resistance` |
| **Vitality** | Repeatable | `minecraft:generic.max_health` |

### Unarmed martial arts (19)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **Armor Penetration** | Bounded | `apothic_attributes:armor_pierce` |
| **Armor Sundering** | Bounded | `apothic_attributes:armor_shred` |
| **Boss Shield Contact** | 1 | Ensure a legal ranged/unarmed Monk attack can interact with Draconic Guardian shields and crystals by normal boss rules. |
| **Catch Arrows** | 1 | Intercept and optionally recover arrows while unarmed; must define projectile eligibility. |
| **Combat Tempo** | Bounded | `minecraft:generic.attack_speed` |
| **Critical Impact** | Repeatable | `apothic_attributes:crit_damage` |
| **Critical Precision** | Bounded | `apothic_attributes:crit_chance` |
| **Flame Fist** | Repeatable | `apothic_attributes:fire_damage` |
| **Frost Fist** | Repeatable | `apothic_attributes:cold_damage` |
| **Giant Slaying** | Bounded | `apothic_attributes:current_hp_damage` |
| **Guarding Palm** | 1 | Innate defensive counter/reflect capability without conventional shield. |
| **Life Drain** | Bounded | `apothic_attributes:life_steal` |
| **Martial Reach** | Repeatable | `minecraft:player.entity_interaction_range` |
| **Palm Impact** | Repeatable | `minecraft:generic.attack_knockback` |
| **Protection Penetration** | Bounded | `apothic_attributes:prot_pierce` |
| **Protection Sundering** | Bounded | `apothic_attributes:prot_shred` |
| **Sweeping Strikes** | Repeatable | `minecraft:player.sweeping_damage_ratio` |
| **Unarmed Strike** | Repeatable | `minecraft:generic.attack_damage` |
| **Withering Stare** | 1 | Sustained gaze damages/Withers eligible targets; preserve original Monk's distinctive stare. |

### Legal innate projectiles (5)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **Spirit Projectile** | 1 | Legal Monk ranged attack for combat and boss crystal interactions; other projectile upgrades apply. |
| **Spirit Projectile Damage** | Repeatable | `apothic_attributes:projectile_damage` |
| **Spirit-Arrow Damage** | Repeatable | `apothic_attributes:arrow_damage` |
| **Spirit-Arrow Draw Speed** | Bounded | `apothic_attributes:draw_speed` |
| **Spirit-Arrow Velocity** | Repeatable | `apothic_attributes:arrow_velocity` |

### Mobility and traversal (15)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **Blink** | 1 | Short-distance safe teleportation with reliable collision, cooldown, line of sight. |
| **Body Scale** | Bounded | `minecraft:generic.scale` |
| **Breath Meditation** | Staged (2) | Two meaningful stages: slower air depletion then full underwater breathing. |
| **Flight Control Speed (External)** | Repeatable | `minecraft:generic.flying_speed` |
| **Gravity Mastery** | Bounded | `minecraft:generic.gravity` |
| **Step Assist** | Bounded | `minecraft:generic.step_height` |
| **Leap Strength** | Repeatable | `minecraft:generic.jump_strength` |
| **Momentum Gliding** | 1 | `apothic_attributes:elytra_flight` |
| **Safe Fall Distance** | Repeatable | `minecraft:generic.safe_fall_distance` |
| **Silent Movement** | Bounded | `minecraft:player.sneaking_speed` |
| **Swift Steps** | Repeatable | `minecraft:generic.movement_speed` |
| **Swim Speed** | Repeatable | `neoforge:swim_speed` |
| **Terrain Mastery** | Bounded | `minecraft:generic.movement_efficiency` |
| **Water Adaptation** | Bounded | `minecraft:generic.water_movement_efficiency` |
| **Water Walking** | 1 | Walk on water surface without boots or permanent potion. |

### Barehand harvesting and gathering (8)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **Aquatic Mining Speed** | Repeatable | `minecraft:player.submerged_mining_speed` |
| **Barehand Harvest Tier** | Staged (runtime_pack_tool_tiers) | Permits valid drops at progressively higher real tool tiers; no extra ranks after the highest legitimate pack tier. |
| **Barehand Mining Speed** | Repeatable | `minecraft:player.block_break_speed`, `minecraft:player.mining_efficiency`, `apothic_attributes:mining_speed` |
| **Delicate Hands** | 1 | Wider Silk Touch analogue for valid hand-harvestable blocks, independent from Glass Touch. |
| **Fortunate Hands** | Bounded | Fortune-like drop bonus up to the actually achievable equipment enchantment bound, respecting recipes. |
| **Gentle Shearing** | 1 | Legal empty-hand shearing/plant gathering without carrying shears. |
| **Glass Touch** | 1 | Harvest ordinary glass like Silk Touch when using compliant empty hands; original Monk registered extra. |
| **Looting Palm** | Bounded | Looting-like bonus from qualifying unarmed or personally credited spells. |

### Contemplation, animals and meditation (4)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **Animal Friendship** | 1 | Tame/friendly interactions with animals while empty-handed; never trivialize rare boss mobs. |
| **Creeper Kiss** | 1 | Pacify or harmlessly greet a creeper while empty-handed. |
| **Hunger Discipline** | Bounded | Reduce food exhaustion while keeping consumption and unique-food discovery meaningful. |
| **Purifying Meditation** | 1 | Active bounded cooldown cleanse of harmful potion effects; not global permanent immunity. |

### Utility and general knowledge (4)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **Block Reach** | Repeatable | `minecraft:player.block_interaction_range` |
| **Experience Attunement** | Repeatable | `apothic_attributes:experience_gained` |
| **Fortune** | Repeatable | `minecraft:generic.luck` |
| **General Cooldown Mastery** | Bounded | `apothic_attributes:cooldown_reduction` |

### Mastery advancement (2)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **Focused Advancement** | Bounded | Additional skill points per completed Monk faction rank, within supported Vampirism accounting; no duplication exploits. |
| **Monastic Mastery Learning** | Repeatable | Improves earned Monk Mastery XP with escalating costs and anti-exploit checks; distinct from vanilla XP. |

### Iron's Spells 'n Spellbooks (26)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **irons spellbooks — Blood Magic Resist** | Bounded | `irons_spellbooks:blood_magic_resist` |
| **irons spellbooks — Blood Spell Power** | Repeatable | `irons_spellbooks:blood_spell_power` |
| **irons spellbooks — Cast Time Reduction** | Bounded | `irons_spellbooks:cast_time_reduction` |
| **irons spellbooks — Casting Movespeed** | Repeatable | `irons_spellbooks:casting_movespeed` |
| **irons spellbooks — Cooldown Reduction** | Bounded | `irons_spellbooks:cooldown_reduction` |
| **irons spellbooks — Eldritch Magic Resist** | Bounded | `irons_spellbooks:eldritch_magic_resist` |
| **irons spellbooks — Eldritch Spell Power** | Repeatable | `irons_spellbooks:eldritch_spell_power` |
| **irons spellbooks — Ender Magic Resist** | Bounded | `irons_spellbooks:ender_magic_resist` |
| **irons spellbooks — Ender Spell Power** | Repeatable | `irons_spellbooks:ender_spell_power` |
| **irons spellbooks — Evocation Magic Resist** | Bounded | `irons_spellbooks:evocation_magic_resist` |
| **irons spellbooks — Evocation Spell Power** | Repeatable | `irons_spellbooks:evocation_spell_power` |
| **irons spellbooks — Fire Magic Resist** | Bounded | `irons_spellbooks:fire_magic_resist` |
| **irons spellbooks — Fire Spell Power** | Repeatable | `irons_spellbooks:fire_spell_power` |
| **irons spellbooks — Holy Magic Resist** | Bounded | `irons_spellbooks:holy_magic_resist` |
| **irons spellbooks — Holy Spell Power** | Repeatable | `irons_spellbooks:holy_spell_power` |
| **irons spellbooks — Ice Magic Resist** | Bounded | `irons_spellbooks:ice_magic_resist` |
| **irons spellbooks — Ice Spell Power** | Repeatable | `irons_spellbooks:ice_spell_power` |
| **irons spellbooks — Lightning Magic Resist** | Bounded | `irons_spellbooks:lightning_magic_resist` |
| **irons spellbooks — Lightning Spell Power** | Repeatable | `irons_spellbooks:lightning_spell_power` |
| **irons spellbooks — Mana Regen** | Repeatable | `irons_spellbooks:mana_regen` |
| **irons spellbooks — Max Mana** | Repeatable | `irons_spellbooks:max_mana` |
| **irons spellbooks — Nature Magic Resist** | Bounded | `irons_spellbooks:nature_magic_resist` |
| **irons spellbooks — Nature Spell Power** | Repeatable | `irons_spellbooks:nature_spell_power` |
| **irons spellbooks — Spell Power** | Repeatable | `irons_spellbooks:spell_power` |
| **irons spellbooks — Spell Resist** | Bounded | `irons_spellbooks:spell_resist` |
| **irons spellbooks — Summon Damage** | Repeatable | `irons_spellbooks:summon_damage` |

### Ars Nouveau (9)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **Ars Featherfall** | 1 | `ars_nouveau:ars_nouveau.perk.feather` |
| **Ars Magical Weight** | Bounded | `ars_nouveau:ars_nouveau.perk.weight` |
| **Ars Mana Capacity** | Repeatable | `ars_nouveau:ars_nouveau.perk.max_mana` |
| **Ars Mana Regeneration** | Repeatable | `ars_nouveau:ars_nouveau.perk.mana_regen` |
| **Ars Saturation** | Bounded | `ars_nouveau:ars_nouveau.perk.saturation` |
| **Ars Spell Damage** | Repeatable | `ars_nouveau:ars_nouveau.perk.spell_damage` |
| **Ars Warding** | Repeatable | `ars_nouveau:ars_nouveau.perk.warding` |
| **Drygmy Proficiency** | Repeatable | `ars_nouveau:ars_nouveau.perk.drygmy` |
| **Wixie Proficiency** | Repeatable | `ars_nouveau:ars_nouveau.perk.wixie` |

### Goety (27)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **goety — Abyss Discount** | Bounded | `goety:abyss_discount` |
| **goety — Abyss Potency** | Repeatable | `goety:abyss_potency` |
| **goety — Casting Speed** | Bounded | `goety:casting_speed` |
| **goety — Cooldown Discount** | Bounded | `goety:cooldown_discount` |
| **goety — Frost Discount** | Bounded | `goety:frost_discount` |
| **goety — Frost Potency** | Repeatable | `goety:frost_potency` |
| **goety — Geomancy Discount** | Bounded | `goety:geomancy_discount` |
| **goety — Geomancy Potency** | Repeatable | `goety:geomancy_potency` |
| **goety — Necromancy Discount** | Bounded | `goety:necromancy_discount` |
| **goety — Necromancy Potency** | Repeatable | `goety:necromancy_potency` |
| **goety — Nether Discount** | Bounded | `goety:nether_discount` |
| **goety — Nether Potency** | Repeatable | `goety:nether_potency` |
| **goety — Soul Discount** | Bounded | `goety:soul_discount` |
| **goety — Spell Burning** | Repeatable | `goety:spell_burning` |
| **goety — Spell Duration** | Repeatable | `goety:spell_duration` |
| **goety — Spell Potency** | Repeatable | `goety:spell_potency` |
| **goety — Spell Radius** | Repeatable | `goety:spell_radius` |
| **goety — Spell Range** | Repeatable | `goety:spell_range` |
| **goety — Spell Velocity** | Repeatable | `goety:spell_velocity` |
| **goety — Storm Discount** | Bounded | `goety:storm_discount` |
| **goety — Storm Potency** | Repeatable | `goety:storm_potency` |
| **goety — Void Discount** | Bounded | `goety:void_discount` |
| **goety — Void Potency** | Repeatable | `goety:void_potency` |
| **goety — Wild Discount** | Bounded | `goety:wild_discount` |
| **goety — Wild Potency** | Repeatable | `goety:wild_potency` |
| **goety — Wind Discount** | Bounded | `goety:wind_discount` |
| **goety — Wind Potency** | Repeatable | `goety:wind_potency` |

### Malum (15)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **malum — Arcane Resonance** | Repeatable | `malum:arcane_resonance` |
| **malum — Charge Capacity** | Repeatable | `malum:charge_capacity` |
| **malum — Charge Duration** | Repeatable | `malum:charge_duration` |
| **malum — Charge Recovery Rate** | Repeatable | `malum:charge_recovery_rate` |
| **malum — Geas Limit** | Bounded | `malum:geas_limit` |
| **malum — Malignant Aegis Capacity** | Repeatable | `malum:malignant_aegis_capacity` |
| **malum — Malignant Aegis Recovery Gain** | Repeatable | `malum:malignant_aegis_recovery_gain` |
| **malum — Malignant Aegis Recovery Rate** | Repeatable | `malum:malignant_aegis_recovery_rate` |
| **malum — Malignant Conversion** | Bounded | `malum:malignant_conversion` |
| **malum — Soul Ward Capacity** | Repeatable | `malum:soul_ward_capacity` |
| **malum — Soul Ward Integrity** | Repeatable | `malum:soul_ward_integrity` |
| **malum — Soul Ward Recovery Gain** | Repeatable | `malum:soul_ward_recovery_gain` |
| **malum — Soul Ward Recovery Rate** | Repeatable | `malum:soul_ward_recovery_rate` |
| **malum — Spirit Spoils** | Repeatable | `malum:spirit_spoils` |
| **Spirit Harvest (Unarmed Adaptation)** | Repeatable | `malum:scythe_proficiency` |

### Eidolon: Repraised (3)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **Eidolon Persistent Soul Hearts** | Repeatable | `eidolon_repraised:persistent_soul_hearts` |
| **eidolon repraised — Chanting Speed** | Repeatable | `eidolon_repraised:chanting_speed` |
| **eidolon repraised — Magic Power** | Repeatable | `eidolon_repraised:magic_power` |

### Forbidden & Arcanus (2)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **Aureal Regeneration** | Repeatable | `forbidden_arcanus:generic.aureal_regeneration` |
| **Magic Protection** | Repeatable | `forbidden_arcanus:generic.magic_protection` |

### Neo Vitae (8)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **Neo Vitae Ritual Mastery** | Bounded | Improve legitimate ritual/altar efficiency only if player-linked and mechanically meaningful; not an arbitrary altar EV cheat. |
| **neovitae — Blood Shield** | Repeatable | `neovitae:blood_shield` |
| **neovitae — Blood Siphon** | Repeatable | `neovitae:blood_siphon` |
| **neovitae — Bonus Sacrifice** | Repeatable | `neovitae:bonus_sacrifice` |
| **neovitae — Bonus Self Sacrifice** | Repeatable | `neovitae:bonus_self_sacrifice` |
| **neovitae — Bonus Spiritus** | Repeatable | `neovitae:bonus_spiritus` |
| **neovitae — Self Sacrifice Multiplier** | Repeatable | `neovitae:player.self_sacrifice_multiplier` |
| **neovitae — Sigil Cost Reduction** | Bounded | `neovitae:sigil_cost_reduction` |

### Mahou Tsukai (3)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **Mahou Capacity** | Repeatable | Increase the player's personal maximum Mahou, subject to installed Mahou Tsukai cap; pack-wide max configuration is not a Monk-only privilege. |
| **Mahou Cultivation** | Repeatable | Increase legal personal Mahou recovery where pinned Mahou provides an explicit controllable hook. |
| **Mahou Efficiency** | Bounded | Reduce Mahou cost where the pinned implementation actually provides a spend-side hook; never produce negative or zero resource cost by accident. |

### Primal Magick (2)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **Primal Attunement (per school)** | Repeatable | For each genuine registered Primal Magick school, improve attunement through its own data system; enumerate schools from pinned registry, not guesses. |
| **Primal Spell Proficiency** | Repeatable | Increase valid Primal Magick spell capability through verified player attachment/API; must not change attunement by accident. |

### Occultism (2)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **Familiar Bond** | Repeatable | Improve owned familiars only where Occultism supports meaningful per-player strength/benefits. |
| **Summoner Mastery** | Repeatable | Improve controllable spirit summon effectiveness without bypassing summoning rituals or item costs. |

### Astral Sorcery (1)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **Astral Attunement** | Repeatable | Improve Astral Sorcery's supported character attunement mechanism without breaking celestial ritual gates. |

### Roots Classic (1)

| Skill | Level model | Function or source |
| --- | --- | --- |
| **Roots Terra Mastery** | Repeatable | Improve legal Terra capacity/regeneration or staff potency using verified Roots Classic player/staff hooks; preserve staff imprint rules. |

## 98 proposed Apotheosis innate techniques (one deliberate omission from 99 upstream)

Apotheosis affix effects are gear-based upstream. Each **proposed** TMM
technique below needs an independently implemented unarmed, defensive,
harvest, magic-projectile, or counterattack adapter. The level cap comes
from the original affix's *distinct value tiers*, not an arbitrary
Monk cap; **32 of the remaining 98** have repeated source values across rarities
and those redundant tiers are not paid ranks. The source can have several rarities with **identical** values; those
  are collapsed before purchases. The source may have further quality scaling within a rarity;
the adapter should provide a deterministic tier effect no stronger
than the legitimate original top roll for that tier.

**Deliberate exclusion:** Apotheosis **Unbound** is backed by
`neoforge:creative_flight`; it has **no TMM Monk technique whatsoever**.
Draconic Curio gear independently supplies that ability. **Winged** cannot
unlock gliding before the Dragon gate.

| Apotheosis technique | Source affix | Distinct source-effect milestones |
| --- | --- | --- |
| **Adamantine [armor]** | `apotheosis:armor/attribute/adamantine` | R → M (2) |
| **Aquatic [armor]** | `apotheosis:armor/attribute/aquatic` | C → R → M (3) |
| **Blessed [armor]** | `apotheosis:armor/attribute/blessed` | C → U → R → E → M (5) |
| **Elastic [armor]** | `apotheosis:armor/attribute/elastic` | C → R → M (3) |
| **Fireproof [armor]** | `apotheosis:armor/attribute/fireproof` | U → R → E → M (4) |
| **Fortunate [armor]** | `apotheosis:armor/attribute/fortunate` | C → R → E → M (4) |
| **Gravitational [armor]** | `apotheosis:armor/attribute/gravitational` | C → U → R → E → M (5) |
| **Ironforged [armor]** | `apotheosis:armor/attribute/ironforged` | C → R → E → M (4) |
| **Oxygenated [armor]** | `apotheosis:armor/attribute/oxygenated` | U → R → E → M (4) |
| **Spiritual [armor]** | `apotheosis:armor/attribute/spiritual` | R → E → M (3) |
| **Stalwart [armor]** | `apotheosis:armor/attribute/stalwart` | U → E → M (3) |
| **Steel Touched [armor]** | `apotheosis:armor/attribute/steel_touched` | R → E → M (3) |
| **Timekeeping [armor]** | `apotheosis:armor/attribute/timekeeping` | R → E → M (3) |
| **Windswept [armor]** | `apotheosis:armor/attribute/windswept` | C → U → R → E → M (5) |
| **Winged [armor]** | `apotheosis:armor/attribute/winged` | E (1) |
| **Blast Forged [armor]** | `apotheosis:armor/dmg_reduction/blast_forged` | C → U → R → E → M (5) |
| **Blockading [armor]** | `apotheosis:armor/dmg_reduction/blockading` | C → R → M (3) |
| **Deflective [armor]** | `apotheosis:armor/dmg_reduction/deflective` | C → U → R → M (4) |
| **Feathery [armor]** | `apotheosis:armor/dmg_reduction/feathery` | C → U → R → M (4) |
| **Grounded [armor]** | `apotheosis:armor/dmg_reduction/grounded` | C → U → R → M (4) |
| **Runed [armor]** | `apotheosis:armor/dmg_reduction/runed` | C → R → M (3) |
| **Blinding [armor]** | `apotheosis:armor/mob_effect/blinding` | U → R → M (3) |
| **Bolstering [armor]** | `apotheosis:armor/mob_effect/bolstering` | U → R → E → M (4) |
| **Bursting [armor]** | `apotheosis:armor/mob_effect/bursting` | E → M (2) |
| **Nimble [armor]** | `apotheosis:armor/mob_effect/nimble` | U → R → E → M (4) |
| **Revitalizing [armor]** | `apotheosis:armor/mob_effect/revitalizing` | E → M (2) |
| **Steadfast [boots]** | `apotheosis:boots/ability/steadfast` | Single (1) |
| **Surefooted [boots]** | `apotheosis:boots/ability/surefooted` | Single (1) |
| **Unhurried [boots]** | `apotheosis:boots/ability/unhurried` | Single (1) |
| **Enlightened [breaker]** | `apotheosis:breaker/ability/enlightened` | R → E → M (3) |
| **Sandforming [breaker]** | `apotheosis:breaker/ability/sandforming` | Single (1) |
| **Stoneforming [breaker]** | `apotheosis:breaker/ability/stoneforming` | Single (1) |
| **Supermassive [breaker]** | `apotheosis:breaker/ability/supermassive` | M (1) |
| **Destructive [breaker]** | `apotheosis:breaker/attribute/destructive` | C → R → E → M (4) |
| **Experienced [breaker]** | `apotheosis:breaker/attribute/experienced` | C → R → E → M (4) |
| **Lengthy [breaker]** | `apotheosis:breaker/attribute/lengthy` | C → R → M (3) |
| **Submerged [breaker]** | `apotheosis:breaker/attribute/submerged` | C → U → R → E → M (5) |
| **Omnetic [breaker]** | `apotheosis:breaker/effect/omnetic` | R → E → M (3) |
| **Radial [breaker]** | `apotheosis:breaker/effect/radial` | U → R → E → M (4) |
| **Prosperous [breaker]** | `apotheosis:breaker/enchantment/prosperous` | U → R → E → M (4) |
| **Spelunkers [breaker]** | `apotheosis:breaker/mob_effect/spelunkers` | U → R → E → M (4) |
| **Swift [breaker]** | `apotheosis:breaker/mob_effect/swift` | U → R → E → M (4) |
| **Lucky [generic]** | `apotheosis:generic/attribute/lucky` | U → R → E → M (4) |
| **Telepathic [generic]** | `apotheosis:generic/telepathic` | Single (1) |
| **Berserking [melee]** | `apotheosis:melee/attribute/berserking` | C → R → E → M (4) |
| **Forceful [melee]** | `apotheosis:melee/attribute/forceful` | C → U → R → E → M (5) |
| **Giant Slaying [melee]** | `apotheosis:melee/attribute/giant_slaying` | E → M (2) |
| **Glacial [melee]** | `apotheosis:melee/attribute/glacial` | U → R → E → M (4) |
| **Graceful [melee]** | `apotheosis:melee/attribute/graceful` | C → U → R → E → M (5) |
| **Infernal [melee]** | `apotheosis:melee/attribute/infernal` | U → R → E → M (4) |
| **Intricate [melee]** | `apotheosis:melee/attribute/intricate` | C → R → E → M (4) |
| **Lacerating [melee]** | `apotheosis:melee/attribute/lacerating` | C → R → M (3) |
| **Lengthy [melee]** | `apotheosis:melee/attribute/lengthy` | C → R → M (3) |
| **Murderous [melee]** | `apotheosis:melee/attribute/murderous` | R → E → M (3) |
| **Piercing [melee]** | `apotheosis:melee/attribute/piercing` | C → R → E → M (4) |
| **Vampiric [melee]** | `apotheosis:melee/attribute/vampiric` | C → R → E → M (4) |
| **Violent [melee]** | `apotheosis:melee/attribute/violent` | C → R → E → M (4) |
| **Cleaving [melee]** | `apotheosis:melee/cleaving` | E → M (2) |
| **Executing [melee]** | `apotheosis:melee/executing` | E → M (2) |
| **Festive [melee]** | `apotheosis:melee/festive` | E → M (2) |
| **Bloodletting [melee]** | `apotheosis:melee/mob_effect/bloodletting` | U → R → M (3) |
| **Caustic [melee]** | `apotheosis:melee/mob_effect/caustic` | R → E → M (3) |
| **Elusive [melee]** | `apotheosis:melee/mob_effect/elusive` | U → R → E → M (4) |
| **Omniscient [melee]** | `apotheosis:melee/mob_effect/omniscient` | M (1) |
| **Sophisticated [melee]** | `apotheosis:melee/mob_effect/sophisticated` | U → R → E → M (4) |
| **Weakening [melee]** | `apotheosis:melee/mob_effect/weakening` | U → R → E → M (4) |
| **Thunderstruck [melee]** | `apotheosis:melee/thunderstruck` | E → M (2) |
| **Agile [ranged]** | `apotheosis:ranged/attribute/agile` | C → R → E → M (4) |
| **Elven [ranged]** | `apotheosis:ranged/attribute/elven` | C → U → R → E → M (5) |
| **Streamlined [ranged]** | `apotheosis:ranged/attribute/streamlined` | C → R → E → M (4) |
| **Windswept [ranged]** | `apotheosis:ranged/attribute/windswept` | C → U → E → M (4) |
| **Prosperous [ranged]** | `apotheosis:ranged/enchantment/prosperous` | E → M (2) |
| **Magical [ranged]** | `apotheosis:ranged/magical` | Single (1) |
| **Acidic [ranged]** | `apotheosis:ranged/mob_effect/acidic` | M (1) |
| **Blighted [ranged]** | `apotheosis:ranged/mob_effect/blighted` | E → M (2) |
| **Deathbound [ranged]** | `apotheosis:ranged/mob_effect/deathbound` | M (1) |
| **Ensnaring [ranged]** | `apotheosis:ranged/mob_effect/ensnaring` | U → R → E → M (4) |
| **Fleeting [ranged]** | `apotheosis:ranged/mob_effect/fleeting` | U → E → M (3) |
| **Grievous [ranged]** | `apotheosis:ranged/mob_effect/grievous` | U → R → E → M (4) |
| **Ivy Laced [ranged]** | `apotheosis:ranged/mob_effect/ivy_laced` | R → E → M (3) |
| **Shulkers [ranged]** | `apotheosis:ranged/mob_effect/shulkers` | E → M (2) |
| **Spectral [ranged]** | `apotheosis:ranged/spectral` | E → M (2) |
| **Gardening [shears]** | `apotheosis:shears/effect/gardening` | Single (1) |
| **Leafforming [shears]** | `apotheosis:shears/effect/leafforming` | Single (1) |
| **Ironforged [shield]** | `apotheosis:shield/attribute/ironforged` | C → R → E → M (4) |
| **Stalwart [shield]** | `apotheosis:shield/attribute/stalwart` | C → R → M (3) |
| **Steel Touched [shield]** | `apotheosis:shield/attribute/steel_touched` | R → M (2) |
| **Catalyzing [shield]** | `apotheosis:shield/catalyzing` | E → M (2) |
| **Devilish [shield]** | `apotheosis:shield/mob_effect/devilish` | U → R → E → M (4) |
| **Galvanizing [shield]** | `apotheosis:shield/mob_effect/galvanizing` | M (1) |
| **Reinforcing [shield]** | `apotheosis:shield/mob_effect/reinforcing` | R → E → M (3) |
| **Venomous [shield]** | `apotheosis:shield/mob_effect/venomous` | U → R → E → M (4) |
| **Withering [shield]** | `apotheosis:shield/mob_effect/withering` | E → M (2) |
| **Psychic [shield]** | `apotheosis:shield/psychic` | E → M (2) |
| **Retreating [shield]** | `apotheosis:shield/retreating` | Single (1) |
| **Nullifying [weapon]** | `apotheosis:weapon/attribute/nullifying` | R → E → M (3) |
| **Runebreaking [weapon]** | `apotheosis:weapon/attribute/runebreaking` | R → E → M (3) |
| **Shredding [weapon]** | `apotheosis:weapon/attribute/shredding` | C → R → E → M (4) |

## Original 25 abilities: preservation decisions

| Original ability | TMM skill mapping or exception |
| --- | --- |
| `armor` | `tmm_monk:trait/minecraft/generic_armor` |
| `armor_toughness` | `tmm_monk:trait/minecraft/generic_armor_toughness` |
| `blindness` | Optional historical blindness trial, no positive purchased node |
| `blink` | `tmm_monk:mobility/blink` |
| `catch_arrows` | `tmm_monk:martial/arrow_catch` |
| `kiss_creeper` | `tmm_monk:martial/creeper_kiss` |
| `explosion_resistance` | `tmm_monk:contemplation/blast_resilience` |
| `feather_fall` | `tmm_monk:trait/minecraft/generic_fall_damage_multiplier` |
| `fly` | Draconic Curio flight only; no Monk Creative Flight |
| `glass_touch` | `tmm_monk:harvest/glass_touch` |
| `potion_immunity` | `tmm_monk:contemplation/cleansing` |
| `health` | `tmm_monk:trait/minecraft/generic_max_health` |
| `hunger` | `tmm_monk:contemplation/hunger_discipline` |
| `jump` | `tmm_monk:trait/minecraft/generic_jump_strength` |
| `mining` | `tmm_monk:harvest/barehand_tier`, `tmm_monk:harvest/barehand_speed` |
| `fire_resistance` | `tmm_monk:contemplation/fire_resilience` |
| `lava_protection` | `tmm_monk:contemplation/lava_survival` |
| `swift` | `tmm_monk:trait/minecraft/generic_movement_speed` |
| `step_up` | `tmm_monk:trait/minecraft/generic_step_height` |
| `strength` | `tmm_monk:trait/minecraft/generic_attack_damage` |
| `tame_animals` | `tmm_monk:contemplation/animal_friendship` |
| `water_walking` | `tmm_monk:mobility/water_walk` |
| `water_breathing_partial` | `tmm_monk:mobility/breath_meditation` |
| `water_breathing_full` | `tmm_monk:mobility/breath_meditation` |
| `withering_stare` | `tmm_monk:martial/withering_stare` |

## Deliberate exclusions from direct player-attribute purchases

| Source ID | Decision |
| --- | --- |
| `eidolon_repraised:max_soul_hearts` | Marked deprecated by pinned Eidolon source; persistent_soul_hearts is supported. Reassess only if live exporter proves a distinct player benefit. |
| `minecraft:generic.follow_range` | Mob AI targeting/follow range has no meaningful player effect in default gameplay; no wasted purchase. |
| `neoforge:creative_flight` | No Monk Creative Flight unlock; allow a Draconic Evolution Curio to supply permission. |

## Runtime and balancing follow-ups (not progression placement)

1. Load full pinned build and export `/tmmmonk attributes`, then
   reconcile the 144 candidate attribute references with real
   player-attached attributes and their actual min/max values.
2. For each bounded purchase, determine **effective** stops:
   chance at 100%, Apothic cooldown at 95%, discrete ticks for Attack
   Speed, nonnegative valid gravity, and safe scale/step limits.
   For repeatable purchases, ensure rising costs and never sell
   a rank that has zero practical effect.
3. Verify the 98 selected Apotheosis skill adapters preserve effects without
   hidden conventional weapons, armor, shield or tool slots.
   Prioritize `Winged`'s world-tier glide restriction and ensure `Unbound` is never registered.
4. Implement/test the real barehand harvest-tier adapter using the
   independently verified Dragon Survival harvesting reference,
   without giving the Monk dragon physiology. Dynamic highest tier
   must be computed against installed tool requirements and
   state-specific loot.
5. Verify non-native Mahou, Primal, Occultism, Roots, Astral and
   Neo Vitae progression APIs before confirming those proposed talents.
6. After owner review **lock the allow-level model and any remaining
   skill exclusions**, then pick Vampirism/Pufferfish/custom backend.
   **Only afterwards** assign each skill its stage, price and world-tier
   prerequisites.

## Evidence

- [TMM source scan](MONK-TRAIT-INVENTORY.md)
- [Original Monk 25-ability research](MONK-ORIGINAL-AUDIT.md)
- [World-tier boundaries](MONK-WORLD-TIERS.md)
- [Apotheosis 8.9.0 generated affix sources](https://github.com/Shadows-of-Fire/Apotheosis/tree/1.21/src/generated/resources/data/apotheosis/affixes)
- [Dragon Survival NeoForge 1.21.1 release candidate](https://www.curseforge.com/minecraft/mc-mods/dragons-survival/files/9539454)

## Tentative two-layer ownership + unified traits (2026-11-14)

This catalog is an **underlying candidate/source inventory**, not a
claim of 270 final unique purchases. One-time powers, including Step Assist
and Momentum Gliding, belong to the native Monk faction tree. Leveled
traits have **one native faction unlock/permission node** and then
**Pufferfish-based investment** where implementation supports it.
Do not automatically assume ordinary Pufferfish binary skill nodes are
repeatable. Group overlapping attributes under canonical shared Pufferfish
traits; the detailed first pass proposes **24 unified skills** spanning
43 registered source attributes, plus conditional Mahou, Roots, Primal
and Occultism adapters. The 172 core records are candidate effects and
some should merge into those shared traits, not turn into individual
Pufferfish purchases.

See [two-layer architecture smoke test](MONK-TWO-LAYER-SMOKE-TEST.md) and
[`skill-unification-proposal.json`](../compat/monk/profiles/skill-unification-proposal.json).
**Placement, category split, repeated-rank UI, upgrade costs and per-school
specialization remain subject to test and review.**

## Selected leveling backend: Tyz's Skills, 99-rank tier ladder

Previous tentative ownership labels naming Pufferfish as the repeatable
backend are superseded. The **one-time** skill nodes (Step Assist, Blink,
Catch Arrows, after-Dragon Momentum Glide) stay in the native Monk
faction tree. **Each leveled** Monk discipline has a native Monk
permission unlock; actual levels are purchased using **Tyz's Skills SP**
from its own action-based XP, **not vanilla XP**. Universal traits
remain universal: Monk's innate permission changes legitimate
applications, not the ownership of ordinary shared stat purchases.
Pufferfish remains for one-time general choices, not the same
numeric modifiers.

The five-tier ladder is only for **repeatable** stats with enough
native numerical headroom; other skills stop at their native
effective cap, and the 98 selected Apotheosis techniques remain
finite and rarity/effect-limited. The illustrative Strength tier
definitions are intentionally inactive because large damage bonuses
could hit Minecraft's effective attribute ceiling. The server
must enforce prior tier level 99 and refund highest tier first.

[Full backend rules and proof gates](MONK-TYZS-TIERED-ADVANCEMENT.md).
