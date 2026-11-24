# Sea Dragon — complete first-pass 100-point skill tree

**Too Much Magic · 2026-11-22 · DESIGN ONLY / NO LIVE SKILLS INSTALLED**

Authoring source: `REMAINING-PATHS-100PT-FIRST-PASS.json` schema `tmm_remaining_path_tree_blueprint_v5`, on isolated `feature/sea-dragon-100pt-complete-tree-20261122`, stacked on design [PR #495](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/495). **This is a new balance pass**. Old Tyz/SP ranks and point allotments are provenance only; especially **old 50-point Abyssal Fortune is explicitly revoked**.

## Sea Dragon identity and what is free

The Sea Dragon should be an excellent **aquatic mover and water-based skirmisher**, a capable personal **tide/storm mage**, durable and recoverable in the ocean, and an extraordinary **Luck specialist**. Its major trade-off is being less effective outside water; the tree makes dry-land exploration viable without deleting the Dragon Survival hydration weakness.

**Native abilities are not purchases:** Sea Dragon species, growth/size, underwater breathing, any source-provided underwater vision, existing lightning/storm breath, Flight Spin/wings once earned, native claws and sand/gravel harvest, native mana regeneration or hydration behavior, diet and existing Dragon Survival ability selections. The purchased nodes below **improve verified native actions**, not grant those actions from nothing or create an extra active hotkey. Source-verify exact pinned 1.21.1 skill datapacks because the available abilities vary across provider versions.

**Resources & economy:** purchases use the same one-point-per-rank rule as every other Path. Core is global and bought separately, not replicated as a 'free second Core' through these species enhancements. A player with both Sea Dragon and legitimate Dracolich Lich is still charged separately for those distinct Path trees and may not double-register Sea Dragon skills.

## Exact mastery budget

| Branch | Paid nodes | 1-point ranks | Share |
|---|---:|---:|---:|
| **Dragon Body & Personal Sorcery** | 5 | 20 | 20% |
| **Oceanic Movement** | 4 | 18 | 18% |
| **Tideshaping Combat** | 4 | 18 | 18% |
| **Abyssal Survival** | 4 | 17 | 17% |
| **Tidal & Abyssal Magic** | 4 | 17 | 17% |
| **Abyssal Fortune** | 1 | 10 | 10% |
| **Full Sea Dragon mastery** | **22** | **100** | **100%** |

Every single rank costs **1 advancement point**, including every late skill. The full tree is achievable with **exactly 100 purchases**; there are no mutually exclusive Sea branches in this first pass. Unlike Hunter, this is one cohesive species build. Not all ranks need to unlock at the same native growth stage.

## Complete skill-by-skill draft

Numbers are **explicit balance candidates**, *not installed attribute magnitudes*. They describe expected effects **at rank 1 and full rank** so we can test useful output and avoid vaguely named no-op perks. A source adapter must check the actual authority for each condition; if the pinned version lacks an ability, revise the skill rather than mint fake power.

### Dragon Body & Personal Sorcery — 20 points

| Skill | Ranks / total SP | Effect when fully purchased (and per-rank increment) | Unlock gate |
|---|---:|---|---|
| **Draconic Reservoir** (`tyr_progression:sea_dragon_draconic_reservoir`) | **6** | +10% per rank to eligible personal magic resource maximums (+60% full); never to machinery/world tanks. | entry |
| **Draconic Potency** (`tyr_progression:sea_dragon_draconic_potency`) | **5** | +4% per rank eligible personally cast spell potency (+20% full); do not multiply by aquatic affinity recursively. | adept; after `draconic_reservoir` |
| **Draconic Scales** (`tyr_progression:sea_dragon_draconic_scales`) | **3** | +1 natural armor per rank (+3 total) while in verified Sea Dragon form; does not sell native scales again. | entry |
| **Draconic Claws** (`tyr_progression:sea_dragon_draconic_claws`) | **3** | +1 physical attack damage per rank (+3) on confirmed native dragon claw attacks only. | entry |
| **Draconic Talons** (`tyr_progression:sea_dragon_draconic_talons`) | **3** | +15% per rank claw mining speed (+45%) only for blocks native Sea Dragon can already harvest; no new tier permissions. | adept; after `draconic_claws` |

### Oceanic Movement — 18 points

| Skill | Ranks / total SP | Effect when fully purchased (and per-rank increment) | Unlock gate |
|---|---:|---|---|
| **Sleek Form** (`tyr_progression:sea_dragon_sleek_form`) | **5** | +8% per rank normal submerged swim speed (+40% full), within configured movement cap; distinct from water-flow handling. | entry |
| **Current Rider** (`tyr_progression:sea_dragon_current_rider`) | **5** | +6% per rank effective swimming movement against verified flowing water/current forces (+30% full), not a blanket speed multiplier. | entry |
| **Slipstream** (`tyr_progression:sea_dragon_slipstream`) | **4** | +2% dodge chance per rank (+8%) against real qualifying direct hits while actively swimming; no immunity, no independent dash action. | adept; after `sleek_form` |
| **Ocean Leap** (`tyr_progression:sea_dragon_ocean_leap`) | **4** | +0.25 block per rank (+1 block) legal water-exit jump height or native breach control; no extra flight, wall bypass or free creative movement. | mastery; after `current_rider` |

### Tideshaping Combat — 18 points

| Skill | Ranks / total SP | Effect when fully purchased (and per-rank increment) | Unlock gate |
|---|---:|---|---|
| **Tidal Strike** (`tyr_progression:sea_dragon_tidal_strike`) | **6** | +3% per rank confirmed melee damage (+18%) when Sea Dragon and prey are in water; no boost to unrelated ranged attacks. | entry; after `draconic_claws` |
| **Undertow** (`tyr_progression:sea_dragon_undertow`) | **4** | On confirmed native claw/bite hit while swimming, apply Slowness I for +0.5 s per rank (2 s full) to valid enemies, at most once per 40 ticks per target; no new cast button. | adept; after `tidal_strike` |
| **Storm Channel** (`tyr_progression:sea_dragon_storm_channel`) | **5** | +4% per rank (+20%) to verified *native* Sea Dragon lightning/breath damage when the relevant ability has been earned; no free weather control or invented storm attack. | adept; after `tidal_attunement` |
| **Wakebreaker** (`tyr_progression:sea_dragon_wakebreaker`) | **3** | +5% per rank native Flight Spin contact damage (+15%) *while submerged*, after native Spin is legitimately unlocked; no new active attack. | mastery; after `slipstream` |

### Abyssal Survival — 17 points

| Skill | Ranks / total SP | Effect when fully purchased (and per-rank increment) | Unlock gate |
|---|---:|---|---|
| **Brine Scales** (`tyr_progression:sea_dragon_brine_scales`) | **5** | Reduce remaining eligible direct physical damage by 3% per rank (15% full) while submerged; no invulnerability or extra damage outside water. | entry; after `draconic_scales` |
| **Deepwater Refuge** (`tyr_progression:sea_dragon_deepwater_refuge`) | **4** | Recover +0.2 HP/s per rank (+0.8 HP/s) while submerged after 8 s without hostile damage; never heals through lethal hit or duplicates totem effect. | adept; after `brine_scales` |
| **Dryland Endurance** (`tyr_progression:sea_dragon_dryland_endurance`) | **5** | Increase real native Sea Dragon safe dry-land hydration duration by 10% per rank (+50% full); dehydration still occurs and cannot be disabled. | entry |
| **Pressure Adaptation** (`tyr_progression:sea_dragon_pressure_adaptation`) | **3** | +20% per rank (+60%) underwater mining throughput where native water penalty exists; no fictitious vanilla 'pressure damage' reduction and no harvest-tier bypass. | adept; after `draconic_talons` |

### Tidal & Abyssal Magic — 17 points

| Skill | Ranks / total SP | Effect when fully purchased (and per-rank increment) | Unlock gate |
|---|---:|---|---|
| **Tidal Attunement** (`tyr_progression:sea_dragon_tidal_attunement`) | **5** | +4% per rank (+20%) to *verified* player-cast water/cold spell effects; spell school IDs must be traced from installed providers. | entry; after `draconic_reservoir` |
| **Stormcalling** (`tyr_progression:sea_dragon_stormcalling`) | **4** | +4% per rank (+16%) to *verified* player-cast lightning/storm spell effects; does not grant control of thunderstorms or any new cast button. | adept; after `tidal_attunement` |
| **Conduit Resonance** (`tyr_progression:sea_dragon_conduit_resonance`) | **4** | -3% real personal mana spend per rank (-12% full) only while the actual vanilla Conduit Power effect is active; MXP credit remains based on real, source-verified nominal cost. | adept; after `draconic_reservoir` |
| **Abyssal Sovereignty** (`tyr_progression:sea_dragon_abyssal_sovereignty`) | **4** | -5% per rank verified native Sea Dragon breath cooldown (-20% full) while submerged, capped by the source mod's minimum cooldown; no new native permissions. | mastery; after `stormcalling` |

### Abyssal Fortune — 10 points

| Skill | Ranks / total SP | Effect when fully purchased (and per-rank increment) | Unlock gate |
|---|---:|---|---|
| **Abyssal Fortune** (`tyr_progression:sea_dragon_abyssal_fortune`) | **10** | TEN ranks, ONE point each, for the ENTIRE extreme Sea Dragon Luck capability. Each rank grants 1/10 of the *newly calibrated* final native Luck target; legitimate Luck-sensitive vanilla/modded results only, no raw drop duplication. | adept |

## How each branch feels in play

**Dragon Body:** general baseline for a supernatural creature, but not so much unconditional armor/damage that the ocean focus becomes optional. Mana growth is **personal resource capacity**, not a generator or a second storage network. Claws and Talons are enhancements to native hits/harvest permissions; no late-game ore bypass.

**Oceanic Movement:** Sleek Form handles ordinary swimming through stationary water; Current Rider addresses *actual flow/current resistance*, not another redundant speed multiplier. Slipstream is a passive defense against verified direct attacks while swimming. Ocean Leap extends *existing* water-to-air control, never gives independent creative flight.

**Tideshaping Combat:** Tidal Strike rewards close combat in water. Undertow is an on-hit slow, not a new targeted pull spell or water-place action. Storm Channel and Wakebreaker improve native Storm Breath/lightning and native Flight Spin after those are legitimately unlocked. Unlike generic melee bonuses, these favor already-earned Sea Dragon activities.

**Abyssal Survival:** Brine Scales is submerged-only damage reduction, Deepwater Refuge is conditional out-of-combat regeneration, Dryland Endurance delays—but never eliminates—source-native desiccation, and Pressure Adaptation addresses *actual submerged mining throughput*, not nonexistent vanilla pressure damage. If the native underwater mining penalty is already absent for Sea Dragons, we must replace that skill rather than award an effect with no gameplay value.

**Tidal Magic:** Tidal Attunement and Stormcalling are **source-audited spell subtype enhancers**; do not infer spell school from the caster mod or attack color. Conduit Resonance needs real **Conduit Power** and preserves proper nominal-vs-actual mana expenditure for MXP. Abyssal Sovereignty shortens a **verified native Sea breath** cooldown only when submerged, subject to the source minimum. No Dragon-only magic regeneration node is sold.

**Abyssal Fortune:** one named skill with **ten ranks at one point each**, contributing one tenth of the complete intended extreme Luck benefit per rank. Old November 15 `50` was an obsolete *point allocation*, not an immutable numerical endpoint. Define `LuckTarget` only after auditing the genuinely legal geared/Luck source; the candidate value for rank `r` is `r × LuckTarget/10` as a *source-owned personal Luck contribution*, with sane attribute combination and cap handling. **Never** directly create bonus drops, duplicate protected loot, or manufacture boss keys. If a source doesn't respond to vanilla Luck, don't falsify the effect.

## Gate map

**Entry:** available after verified Sea Dragon initiation and enrollment; these include basic body, movement and defense; **Abyssal Fortune starts at the Adept stage**, after appropriate native Sea Dragon development, but has no unrelated paid-skill prerequisite. **Adept:** available after demonstrated source-native growth plus a relevant earlier rank in the branch; don't require a hidden second spendable XP currency. **Mastery:** reserved for an attained native late Dragon milestone and the listed prerequisite, never handed out for merely having lots of MXP. Gate identifiers are **to be resolved against actual Dragon Survival provider data**, not invented in this document.

Skill purchases must become **inactive** if the character is no longer eligible for Sea Dragon (subject to the approved Path compatibility matrix); ownership does not evaporate. A legitimate Sea Dragon Dracolich should retain its Sea Dragon tree while its Lich tree is independently governed. No species switching exploit to keep old species bonuses.

## Source reuse and implementation crosswalk

| Reusable capability | Candidate skills | Required verification or likely missing adapter |
|---|---|---|
| Generic conditional attributes | Draconic Scales, Sleek Form, Pressure Adaptation | Real Sea Dragon species state, submerged predicate, source attribute caps. `minecraft:submerged_mining_speed` if it actually influences the pinned Dragon claw handler |
| Generic `damage_multiplier`, `delayed_regeneration`, `evasion`, `status_on_hit` | Brine Scales, Deepwater Refuge, Slipstream, Undertow | Existing Progression effects, with source-backed real-water, recent-hit, weapon/source and appropriate-target conditions |
| Native personal magic attribute adapters | Draconic Reservoir, Draconic Potency, Tidal Attunement, Stormcalling, Conduit Resonance | Which pinned spells are actually water/cold/storm, effect of Core stacking, real verified resource debit and Conduit Power, nominal MXP credit unchanged |
| Dragon Survival native action hooks | Draconic Claws, Talons, Ocean Leap, Storm Channel, Wakebreaker, Abyssal Sovereignty | Pinned Dragon Survival native claw, mine, breaching, electric breath, spin contact and cooldown APIs/datapack descriptors. No invented ability when provider slot missing |
| Native hydration rules | Dryland Endurance | Confirm actual native wet/dry tracking, decrement timing and penalties; stretch existing grace only, never disable weakness or create a duplicate hydration bar |
| Vanilla Luck attribute + mod loot-source audit | Abyssal Fortune | Source-supported personal Luck carrier, actual affected rolls (fishing/loot/other) and legal geared endgame ceiling; no arbitrary raw Luck promise |

## Acceptance scenarios before gameplay promotion

1. Verify **22 bought skills** / **100 purchasable ranks**, all price 1 and no second Core purchase; skill prerequisites, rank persistence, respec and changing species do not create points.
2. Confirm Sea Dragon's native water breathing, sea vision, storm attack, native mana and growth continue to work **before** spending SP; purchased talents do not replicate native unlocks.
3. Compare swim speed against and across currents, dash/spin when legitimately unlocked, legal breach motion and collision. No unsupported creative flight or acceleration while walking on land.
4. Compare real melee, on-hit Slowness target limit, lightning/breath, Spin contact under wet/dry circumstances. Do not alter other species' melee or other players' storm spells.
5. Test submerged defense, regeneration after the precise post-hit wait, hydration while exploring Nether/End with legal sources of wetness, and pressure-mining on appropriate blocks without bypassing ore gates.
6. Test verified water/cold/storm spells, resource discounts while actual Conduit Power is present, native breath cooldown floor, and no duplicate ordinary MXP for nominal mana spends.
7. Audit true Luck **before and after each of 10 ranks**, with eligible fishing/loot outcomes over enough controlled trials; no guaranteed extra fish, no protected boss/drop duplication. Compare against actually legal Apotheosis/Curios builds and Monk Inner Fortune as separate design goals.
8. Tests are **NOT run** by this design-only PR: no code, compiled plugin, jar, server smoke, Prism package or gameplay acceptance. Ownership of any new generic effect handler remains in shared Progression engine.

## Historical number policy

Archived November 15 'Extreme ×50', 50 rank and 50 SP language is **reference provenance, not current allocation or magic numeric promise**. The current owner decision is *ten* one-point ranks for the complete extraordinary Luck ability. We explicitly did **not** preserve any former 50/50 split or simply spread 40 points proportionally across old nodes: all six branches were redesigned against the Sea Dragon fantasy and current 100-point mastery rule.

**Related:** [remaining Path first pass #495](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/495), [Progression XP economy #475](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/475), [Dragon/Supernatural Path design #415](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/415), [compatibility #452](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/452).
