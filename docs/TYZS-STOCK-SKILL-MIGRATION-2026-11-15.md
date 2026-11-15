# Tyz 6.4.2 stock-skill inventory and nine-tree migration ledger

**Status:** exact-version source audit; **zero stock skills removed**. Based on Tyzbrand/TyzsSkills 1.21.1 `mc1.21.1-(main)` source, reporting `mod_version=6.4.2` in `gradle.properties`. Raw values, price arrays, matching original ID and Java behavior classes are available in [JSON snapshot](tyzs-stock-skills-6.4.2.json). This describes *factory presets*, not runtime user-specific override files or successful in-world mechanics.

## Summary

| Existing category | Skill count | Target |
|---|---:|---|
| Abilities | 9 | Mostly universal Core, with Monk/Dragon overlap audited |
| Combat | 12 | Mostly Core; predation to Vampire, fury to Werewolf, hunter combat specials |
| Miscellaneous | 9 | Core survival, crafting, exploration and economy; risky loops gated |
| Nature | 2 | Universal Core farming/husbandry; Forest Dragon adds specialties |
| **Total** | **32** | **All original IDs inventoried and provisionally mapped** |

**Important architecture finding:** Tyz's loader accepts custom JSON overrides by the **same original skill ID**, and its `SkillManager.registerSkill` attaches immutable Java skill behavior by that ID. We should **move stock skill IDs to new categories instead of renaming or reimplementing them** whenever an existing effect is acceptable. Alter a stock immutable skill's `category` in an override under `config/tyzs_skills/custom/skills/`, with matching `id`, type, values, prices and config. Test source behavior after category move, including reload, saved ranks and refund. Tyz's generated `default` directory is wiped/recreated on boot: **never edit it**.

**Do not remove the four stock categories yet.** With the current Core Magic pilot we have five visible tabs. Once all skills are mapped and migrated, the nine custom trees replace stock `abilities/combat/misc/nature`; under the current nine-tree blueprint, this is nine total tabs rather than thirteen. Until then, removing a category could strand visible stock skills; the global category list is an overriding *whole file*. No new category-only config or active stock overrides are installed by this audit.

## All 32 original skills and recommended destinations

The **Effect** column is the English tooltip from the original 6.4.2 localization; exact rank arrays are in the machine snapshot. Costs and caps are the upstream presets, *not* approved pack balancing. Destinations are proposals; no skills have moved.

| Original category | Skill (original ID) | Ranks / total SP | Upstream described effect | Exact stock level 1 → max parameters | Proposed new tree | Review action |
|---|---|---:|---|---|---|---|
| abilities | **Health Boost** (`health_boost`) | 10 / 92 | Increases maximum health by {value} half hearts | generic.max_health: 2→20 | `tmm_magic_core` | Universal vitality; preserve |
| abilities | **Oxygen Boost** (`oxygen_boost`) | 4 / 14 | Increases underwater breathing time by {value}% | generic.oxygen_bonus: 25→100 | `tmm_magic_core` | Universal breath training; preserve |
| abilities | **Block Reach** (`block_reach`) | 4 / 30 | Increases the range at which the player can place and destroy by {value} blocks | player.block_interaction_range: 1→4 | `tmm_magic_core` | Universal manipulation reach; preserve |
| abilities | **Attack Damage** (`attack_damage`) | 3 / 25 | Increases melee damage by {value} | generic.attack_damage: 1→3 | `tmm_magic_core` | Universal combat training; preserve |
| abilities | **Sneaking Speed** (`sneak_speed`) | 5 / 14 | Increases movement speed while sneaking by {value}% | player.sneaking_speed: 20→100 | `tmm_magic_core` | Universal footwork/stealth; preserve |
| abilities | **speed_boost** (`speed_boost`) | 5 / 21 | NO ENGLISH STRING FOUND | generic.movement_speed: 15→75 | `tmm_magic_core` | Universal mobility; preserve |
| abilities | **Step Height** (`step_height`) | 3 / 7 | Allows stepping up blocks up to {value} blocks high without jumping | generic.step_height: 0.5→1.5 | `tmm_magic_core` | Universal traversal, delay/condition |
| abilities | **Swim Speed** (`swim_speed`) | 5 / 21 | Increases swim speed by {value}% | swim_speed: 15→75 | `tmm_magic_core` | Universal swimming; preserve |
| abilities | **Excavation** (`excavation`) | 10 / 40 | Increases mining speed by {value}% | player.block_break_speed: 20→200 | `tmm_magic_core` | Universal mining efficiency; preserve |
| combat | **Venomous Strike** (`venomous_attack`) | 5 / 21 | Adds a {success_probability}% chance to poison targets on hit | success_probability: 3→40 | `tmm_dragon_forest` | Forest Dragon thorn/venom strikes; preserve with gate |
| combat | **Critical Strike** (`critical_hit`) | 5 / 21 | Adds a {success_probability}% chance to deal a critical hit (double damage) | success_probability: 5→25 | `tmm_magic_core` | Universal critical strikes; preserve |
| combat | **Deflection** (`damage_deal`) | 5 / 21 | Adds a {success_probability}% chance to negate and reflect incoming melee damage | success_probability: 3→20 | `tmm_magic_core` | Universal Deflection; preserve with rebalance |
| combat | **Bloodlust** (`bloodlust`) | 4 / 43 | Killing a hostile mob regenerates {health_percentage}% of its max health | health_percentage: 3→15 | `tmm_vampire` | Vampire predatory recovery; preserve with rework |
| combat | **Resistance** (`resistance`) | 6 / 99 | Reduces all incoming damage by {damage_resistance}% | damage_resistance: 7→45 | `tmm_magic_core` | Universal fortitude; major rebalance |
| combat | **Backstab** (`backstab`) | 5 / 21 | Increases damage dealt from behind by {damage_buff}% | damage_buff: 5→25 | `tmm_hunter` | Hunter ambush + optional Vampire variant |
| combat | **Rage** (`rage`) | 5 / 21 | Increases damage by {damage_buff}% when health is below {health_threshold}% | damage_buff: 10→50; health_threshold: 10→30 | `tmm_werewolf` | Werewolf fury; preserve with wolf-form gate |
| combat | **Adrenaline** (`adrenaline`) | 3 / 12 | Grants Absorption and Speed III for {effect_duration}s when health falls below {health_threshold}% | health_threshold: 10→30; effect_duration: 3→8 | `tmm_magic_core` | Universal emergency survival; preserve with cooldown review |
| combat | **Resilience** (`resilience`) | 9 / 72 | Reduces the duration of negative status effects by {effect_time_reduction}% | effect_time_reduction: 15→95 | `tmm_magic_core` | Universal condition resilience; rebalance |
| combat | **Jinxed** (`jinxed`) | 3 / 25 | Adds a {success_probability}% chance to scramble the target's hotbar on hit | success_probability: 3→7 | `tmm_hunter` | Hunter disruption / PvP-only talent; hold |
| combat | **True Strike** (`true_strike`) | 3 / 45 | Adds a {success_probability}% chance to deal true damage on hit | success_probability: 3→9 | `tmm_hunter` | Hunter supernatural piercing strike; hold for damage audit |
| combat | **Sunder Armor** (`sunder_armor`) | 5 / 46 | Adds a {success_probability}% chance for attacks to deal {damage_percentage}% extra durability damage to armor | success_probability: 3→15; damage_percentage: 20→100 | `tmm_hunter` | Hunter armor breaking; hold for PvP/boss policy |
| misc | **Swift Learning** (`swift_learn`) | 4 / 62 | Increases Skill XP gain by {value}% | skill_xp_multiplier: 15→60 | `tmm_magic_core` | Study/Cultivation specialization; redesign |
| misc | **Metabolism** (`nutrition`) | 5 / 21 | Increases food value (nutrition & saturation) by {nutrition_bonus}% | nutrition_bonus: 5→25 | `tmm_magic_core` | Universal food metabolism; preserve |
| misc | **Wisdom** (`experience_boost`) | 5 / 46 | Increases Vanilla XP gain by {bonus_percentage}% | bonus_percentage: 20→100 | `tmm_magic_core` | Universal vanilla XP wisdom; preserve |
| misc | **Stealth** (`stealth`) | 5 / 46 | Reduces the detection range of hostile mobs by {range_reduction}% | range_reduction: 10→50 | `tmm_magic_core` | Universal enemy-detection avoidance; preserve |
| misc | **Magnet** (`magnet`) | 4 / 30 | Attracts items within a {block_radius} block radius | block_radius: 3→10 | `tmm_magic_core` | Universal item attraction; preserve |
| misc | **Spare Parts** (`spare_parts`) | 5 / 67 | Grants a {refund_chance}% chance for each ingredient to be recovered when crafting. Up to {max_materials} item(s) per craft | refund_chance: 4→20; max_materials: 1→3 | `tmm_magic_core` | Universal manual crafting thrift; hold audit |
| misc | **Twist Of Fate** (`twist_of_fate`) | 5 / 46 | Increases the quantity of items found in structure loot chests by {loot_multiplier}% | loot_multiplier: 10→50 | `tmm_magic_core` | Universal explorer's fortune; hold loot audit |
| misc | **Safekept** (`keepsake`) | 8 / 121 | Safeguards the first {saved_slots} slot(s) of your inventory upon death | saved_slots: 1→8 | `tmm_magic_core` | Universal death inventory insurance; adapt |
| misc | **Haggler** (`haggler`) | 4 / 14 | Holding an Emerald attracts villagers within a {block_radius} block radius.  They will follow you with a {villager_speed}% speed bonus | block_radius: 10→25; villager_speed: 0→10 | `tmm_magic_core` | Universal villager trading utility; preserve |
| nature | **Green Thumb** (`green_thumb`) | 5 / 14 | Adds a {success_probability}% chance to double drops when harvesting crops and plants | success_probability: 15→75 | `tmm_magic_core` | Universal agriculture; preserve |
| nature | **Shepherd’s Blessing** (`shepherd_s_blessing`) | 15 / 79 | Baby animals grow {growth_speed}% faster | growth_speed: 50→750 | `tmm_magic_core` | Universal husbandry; preserve |

## Direct-move candidates and actual implementation limits

- **GENERIC attribute skills** use Tyz custom/default attribute modifiers: health, oxygen, reach, attacks, motion, excavation, vanilla XP multiplier. Move their category while retaining original modifier values and operation, then check interaction with each transformed form's native attributes.
- **IMMUTABLE skills** are not interchangeable custom-attribute JSON. Their **exact original skill ID** is registered with the original Java `SkillBehavior`; changing ID or changing skill type to CUSTOM can quietly erase the mechanic. Transfer by overriding the stock ID, retaining type `IMMUTABLE` and the full `customValues` array; verify actual behavior in a client world. `SkillManager` uses skill ID to bind the handler.
- **Critical, Deflection, Backstab, Bloodlust, Rage, etc.** require a native-state predicate at **effect application time** once placed in a form tree; purchase-only gating is not sufficient if a player cures/replaces a form.
- **Jinxed** is confirmed *player-vs-player only*: shuffles the target player's 9 hotbar slots. It has no practical PvE function and can be disruptive. Proposed Hunter placement is a *review placeholder*, not an automatic permission to enable.
- **True Strike** cancels an attack and reissues raw damage as indirect magic in the native code. It could interact with damage immunity and boss protections; test before assigning it a universal Hunter anti-boss bypass.
- **Spare Parts** can refund actual crafting ingredients and already blocks identical-all-four/all-nine recipes. Must audit magic crafting, duplication, ProjectE EMC value loops and any early access to post-Chaos materials.
- **Twist of Fate** multiplies *structure chest* loot; explicitly blacklist boss-critical, excluded and gated items until an acquisition audit passes.
- **Safekept/Keepsake** preserves inventory slots on actual death; coordinate with Corail Tombstone, equipment Curios and lich rescue to prevent double preservation or item duplication.
- **Swift Learning** currently buffs **Tyz skill XP gain** by up to 60%, but our MXP economy mandates one central award dispatcher and `1.2^P` normalization. **Do not copy the original as an unconditional MXP multiplier**. Its gameplay role needs a new owner-approved learning-oriented effect or deliberate retirement; all other stock skills remain eligible for direct reassignment pending tests.
- **Resistance** defaults to up to **45% all-damage reduction**; **Resilience** removes up to **95% negative-effect duration**. Their original power is documented, not automatically endorsed for our endgame.
- **Monk collision:** vanilla Step Height, Attack Damage, Swim/Oxygen can make native Monk unique unlocks redundant; stage/equipment/source locks required. Cave/Sea/Forest Dragon class enhancements must not double-grant identical native stats.

## Five-rank Core Magic compression — user direction (2026-11-15)

The *existing four Core Magic CUSTOM skills* (separate from these 32 stock skills) are compressed from 99 ranks to **five purchasable levels**, with exactly 20%, 40%, 60%, 80%, 100% of their preexisting rank-99 **terminal effect**. Each costs **one SP** pending economy review; all four keep original skill IDs and same final values. For integer-only modifier values, use nearest valid whole-unit rounding at intermediate ranks. This is not uniformly a literal +20% stat per rank: Reservoir is flat mana/Psi, Wellspring is regeneration, Thrift is discount and Potency has mixed flat/ratio attributes. **The 100% refers to the original intended maximum of each attribute, not the resource itself.**

| Core family | Sample rank-5 ceiling | Levels & price |
|---|---|---|
| Reservoir | +99 Ars/Iron max mana; +4,950 Psi reserve | 5 × 1 SP |
| Wellspring | +2.475 Ars regen; +0.99 Iron's regen; +24.75 Psi regen | 5 × 1 SP |
| Thrift | +0.297 Goety soul discount stat; +29.7 Neo Vitae sigil reduction; maintain positive debit and source-specific stacking | 5 × 1 SP |
| Potency | +4.95 Ars spell damage; +0.99 Iron's power; +99 Goety potency | 5 × 1 SP |

The earlier Core Magic 99-rank *multi-magnitude future tiers* remain possible for much later specialist progression, but the **base node** is now five ranks. No bonus or XP currency is added; MXP source/exponential divisor and native progression permissions stay unchanged.

## Implementation and retirement order

1. **Completed here:** exact upstream 32-skill inventory, source effect/cost snapshot, proposed nine-tree destination, risk flags; Core Magic four-node 5-rank source config and static check updates.
2. **Not yet done:** review destination/role decisions, especially whether the base tree should be renamed **Core Skills** now that it may contain universal martial, survival, farming and exploration traits (keep ID `tmm_magic_core` for stable save data unless approved).
3. Prepare **same-ID skill overrides** with original effects in new categories; add use-time form gating for form-specific Java IMMUTABLE skills; save backup and define original rank/SP migrations, including options for existing users of old categories.
4. In a disposable client world verify migrated skill IDs/purchases/refunds, ranks, relogs, exclusions, special IMMUTABLE skill triggers, traits, and separate MXP rules. Document each pass. **Only after full coverage**, remove original category keys in the complete `categories.json` override; never remove default skill files or JAR data.
5. Inspect Tyz's separate Traits system before deciding any modpack-wide removal. The 32 standard skills are only the preset skill catalog, *not* a claim that Traits are already mapped.

## Primary references

- [Tyz 6.4.2 SkillPresets.java](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/server/skills/SkillPresets.java)
- [English UI effect descriptions](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/resources/assets/tyzs_skills/lang/en_us.json)
- [Override load logic](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/server/skills/SkillLoader.java) and [ID-to-behavior binding](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/server/skills/SkillManager.java)
- [FileManager: generated defaults plus custom overrides](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/server/active/FileManager.java)
- [existing nine-tree design](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/83)
