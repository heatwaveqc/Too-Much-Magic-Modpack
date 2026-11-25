# Vampire — complete 100-point JAWR skill tree

**Too Much Magic · 2026-11-22 · DESIGN ONLY, NOT ACTIVE IN THE PACK**

Machine-readable skill source: `compat/progression/design/tree-drafts/REMAINING-PATHS-100PT-FIRST-PASS.json` (schema v6). Isolated authoring branch stacked on Sea Dragon design [#514](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/514). Numerical effects and gates are current **balance proposals**, not final gameplay-tested source contracts.

## Player experience and native ownership

Vampire should be a nocturnal hunter whose combat and spellcasting depend on managing real blood. Native JAWR controls the vampire state, Lilith initiation, bite/feeding, native levels, BAT unlock, existing hunger/sunlight penalties, cures, blood HUD and any native dark vision. **All of those are free native abilities**; skill ranks buy improvements to them, never substitute an extra blood meter, new transformation or a second XP wallet.

The authoritative blood pool is JAWR's native attachment. Verified native feeding transactions, including legitimate player donors and bottles, may receive a single bounded bonus. Donor blood must actually be debited; passive orb/pendant transfers, unfinished recipe items and self-bites do not qualify. Sunshroud is partial damage mitigation, not daylight immunity, and vampire forest exposure remains subject to native checks. Physical EvilCraft blood unification, Witchery food, and optional Vampire's Delight integration remain separately owned.

## Exact rank budget

| Branch | Paid skills | One-point ranks |
|---|---:|---:|
| **Bloodcraft** | 4 | 25 |
| **Predation** | 4 | 20 |
| **Nightstalker** | 4 | 20 |
| **Undead Constitution** | 4 | 20 |
| **Sanguine Arts** | 3 | 15 |
| **Total** | **19** | **100** |

**Every individual rank costs exactly one advancement point**, including high-stage talents. Full eligible mastery is 100 ranks. Core is independent and not bundled for free. Existing native vampire level progression also remains separate; its level milestones gate skills without consuming these same advancement points.

## Complete purchased-skill list

Effects are written as explicit candidate formulas to enable a later real handler/source audit. If a provider lacks a verified source ability, postpone or redesign that particular skill rather than selling a no-op.

### Bloodcraft — 25 points

| Skill | Ranks | Proposed effect (including full-rank value) | Unlock | Source hook status |
|---|---:|---|---|---|
| **Efficient Feeding** (`tyr_progression:vampire_efficient_feeding`) | **6** | +5% accepted native blood gain per rank (+30% max) from one verified JAWR feeding event, including recognized bottles, bites and valid player/nonplayer donors; no bonus on unrelated passive transfers or synthetic blood. | entry | `existing_source_hook` |
| **Deep Reservoir** (`tyr_progression:vampire_deep_reservoir`) | **6** | +8% native maximum blood capacity per rank (+48% full), computed from JAWR's level baseline instead of multiplying existing bonuses; require native vampire level at least 2. | adept after `efficient_feeding` | `existing_source_hook` |
| **Blood Thrift** (`tyr_progression:vampire_blood_thrift`) | **6** | -3% verified native personal self-blood expenditure per rank (-18% max), only hunger/healing/approved timed ability charges, never donor blood extraction or orb/altar transfers; positive costs pay at least one unit. | entry | `existing_source_hook` |
| **Crimson Renewal** (`tyr_progression:vampire_crimson_renewal`) | **7** | On a real completed feeding transaction that increases native blood, heal 0.5 HP per rank (+3.5 HP max), once per 20 seconds; no failed/full-pool, self-feeding or reentrant reward. | mastery after `efficient_feeding` | `generic_adapter_needed` |

### Predation — 20 points

| Skill | Ranks | Proposed effect (including full-rank value) | Unlock | Source hook status |
|---|---:|---|---|---|
| **Fang Training** (`tyr_progression:vampire_fang_training`) | **5** | Successful native bite marks the same hostile prey for five seconds; the next real melee hit on that victim gains +4% damage/rank (+20% max), then consumes the mark. Drinking bottles does not trigger this. | entry | `generic_native_bite_bridge_needed` |
| **Blood Duelist** (`tyr_progression:vampire_blood_duelist`) | **5** | +4% direct melee damage/rank (+20% max) against a real hostile whose current health is below half its maximum health; no self-damage or fake hostile target. | adept after `fang_training` | `generic_damage_predicate_needed` |
| **Predatory Precision** (`tyr_progression:vampire_predatory_precision`) | **5** | +2 percentage points real direct-melee critical chance per rank (+10 pp max), active only during genuine night/darkness. | adept after `blood_duelist` | `existing_effect_needs_light_gate` |
| **Blood Trail** (`tyr_progression:vampire_blood_trail`) | **5** | On a verified native bite against a hostile nonplayer, apply Glowing for 2 seconds/rank (10 seconds max). No global mob radar, no duplicate target marking and PvP exclusions configurable. | adept after `fang_training` | `generic_native_bite_bridge_needed` |

### Nightstalker — 20 points

| Skill | Ranks | Proposed effect (including full-rank value) | Unlock | Source hook status |
|---|---:|---|---|---|
| **Shadow Stalker** (`tyr_progression:vampire_shadow_stalker`) | **6** | Reduce eligible hostile mobs' initial acquisition distance by 5%/rank (-30% max) while crouching in genuine darkness; cease after combat aggro and do not mask boss targeting. | entry | `generic_mob_detection_adapter_needed` |
| **Night Runner** (`tyr_progression:vampire_night_runner`) | **5** | +3% grounded movement speed/rank (+15% max) during genuine darkness/night only; not constant daytime speed or free flight. | entry | `existing_attribute_needs_light_predicate` |
| **Bat Mastery** (`tyr_progression:vampire_bat_mastery`) | **5** | +20% duration/rank (+100% max) on already-unlocked native JAWR BAT shape, using existing native BAT_FORM_DURATION attribute; does not enable flying or independently change steering. | adept | `existing_source_hook` |
| **Night Veil** (`tyr_progression:vampire_night_veil`) | **4** | +2.5 percentage points projectile evasion/rank (+10 pp max) when crouching in genuine darkness; never negates area damage or sunlight. | mastery after `shadow_stalker` | `existing_evasion_needs_light_gate` |

### Undead Constitution — 20 points

| Skill | Ranks | Proposed effect (including full-rank value) | Unlock | Source hook status |
|---|---:|---|---|---|
| **Pale Resilience** (`tyr_progression:vampire_pale_resilience`) | **5** | Shorten newly applied finite harmful Wither, Weakness and Slowness by 5%/rank (-25% full), if JAWR vampire is actually susceptible; do not buy immunity it already has. | entry | `existing_exact_effect_handler` |
| **Sunshroud** (`tyr_progression:vampire_sunshroud`) | **5** | -8% native ordinary sunlight tick damage/rank (-40% max); not immunity, no suppression of native lethal-special sunlight check or vampire forest exposure, no independent blood drain discount. | adept after `pale_resilience` | `existing_source_hook` |
| **Bloodbound Vitality** (`tyr_progression:vampire_bloodbound_vitality`) | **5** | +2 maximum health/rank (+10 HP or 5 hearts) only when the authoritative native JAWR blood reservoir is ≥50% full; no free healing when buffs toggle. | entry | `existing_attribute_needs_blood_predicate` |
| **Elder Presence** (`tyr_progression:vampire_elder_presence`) | **5** | On true melee hit at night against ordinary hostile targets, apply Weakness I for 1 second/rank (5 seconds max), once per 40 ticks per target; no boss pacification or PvP aura. | mastery after `bloodbound_vitality` | `existing_status_handler_needs_night_gate` |

### Sanguine Arts — 15 points

| Skill | Ranks | Proposed effect (including full-rank value) | Unlock | Source hook status |
|---|---:|---|---|---|
| **Sanguine Attunement** (`tyr_progression:vampire_sanguine_attunement`) | **5** | +5% personally cast and actually blood-paid spell potency/rank (+25% full) for source-verified Neo Vitae/EvilCraft blood spell categories. Not all spells or machine/ritual throughput. | entry | `provider_catalog_audit_needed` |
| **Hematic Efficiency** (`tyr_progression:vampire_hematic_efficiency`) | **5** | -3% real personally paid blood spell costs/rank (-15% full); not JAWR hunger/heal, donors, stationary rituals or resources not consumed. Preserve MXP payment accounting. | adept after `sanguine_attunement` | `provider_cost_audit_needed` |
| **Midnight Invocation** (`tyr_progression:vampire_midnight_invocation`) | **5** | +4% speed/cooldown throughput per rank (+20% full) for eligible personally cast blood spells at night/in actual darkness. No invented active spell, free ritual acceleration or global spell cooldown change. | mastery after `hematic_efficiency` | `provider_speed_audit_needed` |

## How the five branches work together

**Bloodcraft:** Real feeding restores the authoritative JAWR blood pool. Efficient Feeding increases successful paid intake; Deep Reservoir enlarges the existing capacity without compounding on old boosts; Blood Thrift reduces verified native self-spending but not donor extraction; Crimson Renewal offers at most 3.5 HP after a completed feed, with a 20-second cooldown and no free loop from partial or failed consumption.

**Predation:** A successful native bite makes the next valid melee strike against that same victim more dangerous (Fang Training), while Blood Trail marks recently bitten hostiles. Blood Duelist improves execution of wounded enemies; Predatory Precision rewards direct combat in darkness. No added cast buttons or uncontrolled automatic blood harvest.

**Nightstalker:** Shadow Stalker modifies initial monster detection, not in-combat retaliation. Night Runner is grounded movement speed in real darkness/night. Bat Mastery lengthens the already-unlocked BAT duration using JAWR's verified native attribute; it never unlocks flight. Night Veil is a bounded projectile evasion chance while crouching in darkness.

**Undead Constitution:** Pale Resilience shortens only harmful statuses the character can actually receive; Sunshroud reduces ordinary native sunlight damage by 40% at rank five but cannot erase lethal sunlight cases; Bloodbound Vitality provides conditional maximum health while the real pool is at least half full; Elder Presence is a small single-target Weakness proc, not native mob alliance or boss mind control.

**Sanguine Arts:** Personal blood-spell potency, personally paid blood magic cost efficiency, and blood-spell cooldown/cast-speed improvements. Only actual source-identified, player-owned blood magic counts. Never infer every Neo Vitae or EvilCraft operation is a spell; do not accelerate machines or grant a second mana pool.

## Implementation crosswalk

| Candidate backing | Skills | Source-safe work remaining |
|---|---|---|
| **Existing JAWR E5 adapters:** `jawr_feeding_bonus`, `jawr_blood_capacity`, `jawr_blood_thrift`, `jawr_sun_resistance`, `jawr_bat_duration` | Efficient Feeding, Deep Reservoir, Blood Thrift, Sunshroud, Bat Mastery | Five verified generic source seams already exist in Progression; test loaded pinned JAWR runtime, limits, donor receipt correctness and HUD |
| Existing generic attributes and combat/status handlers | Blood Duelist, Predatory Precision, Night Runner, Night Veil, Pale Resilience, Bloodbound Vitality, Elder Presence | Add/verify exact blood-fraction, light, target health, projectile and vampire-state predicates. Do not assume arbitrary selectors are supported |
| Feeding/combat event combiners | Fang Training, Blood Trail, Crimson Renewal | Reuse verified native bite/feeding receipts, but needs common same-target follow-up hit, target Glowing and once-per-feed heal, with deduplication |
| Hostile initial detection control | Shadow Stalker | Implement reusable mob acquisition-distance guard, not total mob AI suppression or a special Vampire-only mixin |
| Personally paid blood-magic provider adapter | Sanguine Attunement, Hematic Efficiency, Midnight Invocation | Audit pinned Neo Vitae/EvilCraft personal casting IDs and resource/cooldown effects, rather than inventing blanket blood-school tags |

## Eligibility gates

Entry: native JAWR vampire level at least 1. Adept: **provisionally** level at least 4 (Deep Reservoir must also satisfy JAWR's ≥2 capacity rule). Mastery: **provisionally** level at least 8. These thresholds are balancing candidates, not a second native levelling system. Bat Mastery also requires actual unlocked BAT form; no rank unlock may grant native abilities prematurely.

If cured or otherwise invalid for Vampire, purchases become inactive without duplicating native state or silently erasing ownership. Cross-Path combinations obey the current Progression compatibility policy; don't restore retired TeamLapen Vampirism or Werewolves as a skill provider.

## Acceptance plan (not performed)

1. Verify 19 paid definitions, exactly 100 one-point ranks, source native eligibility and form-specific conditional effects; Core and MXP wallets remain independent.
2. Test real vampire feeding (mob bite, player donor, bottles including existing interop), donor debit, actual accepted gain, bonus clipping, full pool, partial pool, reentry, relog and source HUD; no double-bonus or drained-donor MXP.
3. Test actual native costs for Blood Thrift, native level 1/2+ Deep Reservoir capacity, vampire blood drain/high blood checks, and no additional blood meter. Separately retest known Lilith pool/HUD issue #58.
4. Test sunlight damage at both ordinary and native special-fatal cases, including vampire forest. Max-rank Sunshroud must not produce immunity. Confirm no duplicate sun/hunger penalization.
5. Test BAT unlock and actual source duration scaling, nighttime stealth aggro/retaliation, real projectile dodge, melee target/health thresholds and feed-followup healing/cooldowns.
6. Identify real blood-spell catalogs and actual personal debits, zero/no-op provider behavior, nominal MXP spent, no cost-free ritual or passive production loops.
7. Check cures, relogs, multiplayer isolation and Path state changes. Do not count source unit tests as an in-game PASS.

## Explicitly superseded historical concepts

Archived Tyz Vampire prices, excessively expensive Nocturnal Vision, passive blood awarded on generic kills, the independent Crimson Surge cast button and Bloodlust heal on every kill are **not mandatory skills**. Their older concepts were considered but not simply conserved. The new balance is built around JAWR feeding transactions and ordinary nighttime play, within a 100-point budget.

**No executable change:** This design PR edits only docs/JSON under `compat/progression/design/tree-drafts/`. It does not activate Path JSON, run an integrated smoke test, change mod pins, or build a Prism instance. Generic missing handlers remain Progression-owned on `feature/progression-*` branches, and Vampire-specific datapack work belongs on a separate `feature/vampire-*` source PR after owner balance review.

References: [E5 native adapter audit](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/dev/progression/compat/progression/design/BATCH-E5-TRANSFORMATIONS.md), [#468 single Progression engine](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/468), [#280 JAWR QA](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/280), [#495 remaining Paths](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/495).
