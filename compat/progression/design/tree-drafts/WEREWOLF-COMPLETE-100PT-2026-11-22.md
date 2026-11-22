# Werewolf — complete first-pass 100-point tree

**Too Much Magic · November 22, 2026 · DESIGN ONLY / NOT ACTIVE / NUMERIC BALANCE FOR REVIEW**

Machine-readable design ledger: `compat/progression/design/tree-drafts/REMAINING-PATHS-100PT-FIRST-PASS.json`, schema v7, on `feature/werewolf-100pt-complete-tree-20261122` stacked on [Vampire #518](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/518), which is stacked on [Sea Dragon #514](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/514) and parent [#495](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/495). No live Werewolf path exists on `dev/progression` yet; this draft enables no data, code, mod pin or gameplay.

## Identity and distinction from Vampire / Monk

The Werewolf is a physically dominant **health-regenerating bruiser** with powerful legal wolf-form claws, high mobility, full-moon peaks and legitimate nature-magic support. Vampire survives through blood harvesting, predatory stealth and sanguine spells; Werewolf instead sustains **actual HP** through correct hunger and form rules, closes distance in beast form, and enhances real Roots/Botania personal nature magic. It should be able to stay in combat longer than an unprepared generalist without becoming unkillable or gaining unrelated vampire blood powers.

**Free JAWR-native abilities are never purchased again:** lycanthrope affliction/rite via wolfsbane, authentic wolf and werewolf forms, JAWR native level progression, actual native claw/bite, full-moon forced changes (if source-enforced), native hunger/diet and vulnerabilities (including verifiable silver), and native transformations/cures. Progression only offers passive/contextual enhancements. Do not introduce TeamLapen Werewolves/Vampirism, a new Fury bar, a second resource wallet or a standalone active pounce/howl ability.

**Shape matters:** some proposed bonuses can be available for a verified lycan even in human-like form when explicitly stated (e.g. Feral Vigor), while most claws/mining/mobility/regen require a **real** JAWR `WOLF`/`WEREWOLF` source shape, not just an affliction flag. Each shape predicate and rank gate needs actual pinned JAWR 0.5.39 runtime testing; no binary/source shortcuts.

## Exact mastery budget

| Branch | Paid skills | One-point ranks |
|---|---:|---:|
| **Lupine Vitality** | 5 | 30 |
| **Feral Might** | 5 | 25 |
| **Moonrunner** | 5 | 20 |
| **Wild Communion** | 4 | 20 |
| **Moon Apex** | 1 | 5 |
| **Total** | **20** | **100** |

**Every rank costs one advancement point**. All twenty skills can be mastered for exactly 100 total points in this first-pass design. The five Moon Apex ranks are five separate one-point purchases, *not* one expensive capstone. Native JAWR rank gates and true moon phases are eligibility/activation checks rather than second spendable progression currencies.

## Every purchased skill and candidate effect

These numerical magnitudes are **new candidates for the current 100-point balance pass**, not inherited November 15 effect caps. Engineering ceilings or proof of generic handlers should not be confused with game balance approval.

### Lupine Vitality — 30 points

| Skill | Ranks / price | Candidate effect and full-rank result | Gate / dependency | Implementation class |
|---|---:|---|---|---|
| **Feral Vigor** (`tyr_progression:werewolf_feral_vigor`) | **8 × 1** | +3 max HP per rank (+24 HP = 12 hearts max) for legitimate JAWR lycanthropy; source form-state gating, avoid immediate free heal from attribute toggles. | entry | `generic_attribute_native_affliction_check` |
| **Regenerative Flesh** (`tyr_progression:werewolf_regenerative_flesh`) | **7 × 1** | Recover +0.15 HP/sec per rank (+1.05 HP/sec max) while genuinely fed and in an eligible native JAWR wolf/werewolf shape; 50% rate for the first 5s after taking real hostile damage, none while starving. HEALTH regeneration, not mana or wolf blood. | entry; prerequisite `feral_vigor` | `generic_regeneration_needs_native_shape_hunger_and_recent_hit` |
| **Unbreakable Beast** (`tyr_progression:werewolf_unbreakable_beast`) | **5 × 1** | Reduce remaining real non-silver physical damage by 2% per rank (10% max) when below 50% HP in wolf/werewolf form; never negate native silver/fire penalties or fatal damage. | mastery; prerequisite `regenerative_flesh` | `existing_damage_multiplier_needs_eligibility_and_silver_source_check` |
| **Bloodlust Recovery** (`tyr_progression:werewolf_bloodlust_recovery`) | **5 × 1** | After a genuine credited hostile kill while in wolf/werewolf form, heal 0.5 HP per rank (+2.5 HP max) with a 12-second cooldown; no credit from ownerless/farm/proxy deaths, natural entities without genuine hostile intent, or other player's kills. | adept; prerequisite `rending_claws` | `generic_heal_on_kill_native_shape_attribution` |
| **Tempered Against Silver** (`tyr_progression:werewolf_tempered_against_silver`) | **5 × 1** | Reduce remaining damage by 4% per rank (20% max) ONLY from provably source-identified silver weapons or hazards, leaving at least 80% of the original danger. If silver sources cannot be reliably identified, withhold rather than broadly reduce all melee. | mastery; prerequisite `unbreakable_beast` | `missing_pinned_silver_damage_identity_audit` |

### Feral Might — 25 points

| Skill | Ranks / price | Candidate effect and full-rank result | Gate / dependency | Implementation class |
|---|---:|---|---|---|
| **Rending Claws** (`tyr_progression:werewolf_rending_claws`) | **7 × 1** | Increase native legal empty-hand wolf/werewolf melee damage by 3% per rank (+21% max); use actual player_attack attribution, NOT arbitrary equipped weapons or wolf-type mobs. | entry | `generic_native_attack_proxy_existing_source_verify` |
| **Savage Momentum** (`tyr_progression:werewolf_savage_momentum`) | **5 × 1** | On successive actual qualifying wolf melee hits within 4s, gain a temporary +2% damage per stack per rank, at most 3 stacks (+30% full), expiring 4s after the last hit; no Fury economy or gain for misses. | adept; prerequisite `rending_claws` | `generic_hit_streak_native_attack_scope` |
| **Bite Techniques** (`tyr_progression:werewolf_bite_techniques`) | **5 × 1** | On a true native wolf bite with verified damage owner, apply Slowness I to eligible hostile prey for 1 second per rank (5s full), at most once per target every 40 ticks; no free stun, permanent infection or new bite button. | adept; prerequisite `rending_claws` | `native_bite_damage_identity_to_verify` |
| **Primal Fury** (`tyr_progression:werewolf_primal_fury`) | **4 × 1** | While in wolf/werewolf shape and below 35% current HP, increase legal direct melee damage by 5% per rank (+20% max); passive injury threshold, NOT a separate Fury resource or activated burst. | mastery; prerequisite `savage_momentum` | `generic_native_attack_health_gate` |
| **Predator's Focus** (`tyr_progression:werewolf_predators_focus`) | **4 × 1** | Against real hostiles below 50% max HP, gain +3% eligible native wolf melee damage per rank (+12% max); do not proc on nonhostiles, tamed wolves or friendly allies. | adept; prerequisite `bite_techniques` | `generic_target_health_native_attack_predicate` |

### Moonrunner — 20 points

| Skill | Ranks / price | Candidate effect and full-rank result | Gate / dependency | Implementation class |
|---|---:|---|---|---|
| **Predator's Stride** (`tyr_progression:werewolf_predators_stride`) | **5 × 1** | +4% grounded movement speed per rank (+20% full) while in actual JAWR WOLF/WEREWOLF form; not generic human walking or creative flight. | entry | `generic_conditional_attribute` |
| **Pouncing Leap** (`tyr_progression:werewolf_pouncing_leap`) | **4 × 1** | +0.25 block of genuine jump height per rank (+1 block full) during actual wolf movement, plus native fall handling; no new targeted leap hotkey or midair flight. | entry; prerequisite `predators_stride` | `generic_jump_bonus_native_form_shape_check` |
| **Lunar Chase** (`tyr_progression:werewolf_lunar_chase`) | **4 × 1** | +5% ground pursuit speed per rank (+20% full) when tracking a legitimate recently damaged hostile within 16 blocks under clear moonlit sky; no bonus indoors or in full daylight. Check exact lunar phase separately from simple night. | adept; prerequisite `predators_stride` | `generic_target_recent_damage_and_moon_predicates_missing` |
| **Feral Excavation** (`tyr_progression:werewolf_feral_excavation`) | **4 × 1** | +15% legal native empty-hand wolf claw break speed per rank (+60% full) only for blocks the claw can already harvest, no new tier, silk/fortune or ore-gate bypass. | entry | `generic_native_mining_existing_source_verify` |
| **Arrow Awareness** (`tyr_progression:werewolf_arrow_awareness`) | **3 × 1** | +2 percentage points evasion chance per rank (+6 pp max) against genuine moving projectiles while in wolf/werewolf shape, not area explosions or ownerless spells. | adept; prerequisite `predators_stride` | `existing_evasion_native_shape_predicate` |

### Wild Communion — 20 points

| Skill | Ranks / price | Candidate effect and full-rank result | Gate / dependency | Implementation class |
|---|---:|---|---|---|
| **Wild Attunement** (`tyr_progression:werewolf_wild_attunement`) | **6 × 1** | +5% per rank (+30% full) to verified personally cast nature spell potency from source-registered Roots/Botania/nature classes while JAWR werewolf-afflicted; not passive Botania flowers or generating machine throughput. | entry | `provider_specific_nature_spell_registry_audit` |
| **Rootsong** (`tyr_progression:werewolf_rootsong`) | **5 × 1** | +5% per rank (+25% full) to actual player-applied nature healing spell effects when spell provider and credited caster are verified. Do not count the intrinsic wolf HP regeneration twice. | adept; prerequisite `wild_attunement` | `nature_heal_caster_source_audit` |
| **Grove Howl** (`tyr_progression:werewolf_grove_howl`) | **4 × 1** | When the werewolf legitimately casts an eligible owned nature spell, its already-tamed wolves within 12 blocks receive +5% direct damage per rank (+20% full) for 10s, refreshed on real casts only; no taming, new summoning, permanent wolf ownership, or new howl button. | mastery; prerequisite `rootsong` | `pet_owner_nature_cast_adapter_needed` |
| **Verdant Resilience** (`tyr_progression:werewolf_verdant_resilience`) | **5 × 1** | Reduce remaining ordinary physical damage by 3% per rank (+15% full) in native wolf shape while grounded on grass, moss or leaves; not environmental/fire/silver reduction, and no global biome-wide magic aura. | adept; prerequisite `wild_attunement` | `ground_tag_and_damage_predicate_adapter` |

### Moon Apex — 5 points

| Skill | Ranks / price | Candidate effect and full-rank result | Gate / dependency | Implementation class |
|---|---:|---|---|---|
| **Moon Apex** (`tyr_progression:werewolf_moon_apex`) | **5 × 1** | Under actual full-moon conditions and in JAWR WOLF/WEREWOLF form, add +4% eligible claw damage AND +4% native personal nature-spell potency per rank (both +20% full); no activation button, permanent bonus, global weather override, extra transformation or silver immunity. | mastery; prerequisite `primal_fury`, `grove_howl` | `native_moon_phase_and_source_verified_attack_magic_adapters` |

## How the five branches play

**Lupine Vitality (30).** Feral Vigor gives up to 12 extra hearts from a truly lycan-afflicted player, above universal Core health upgrades. Regenerative Flesh recovers up to 1.05 HP/s while properly fed in native wolf/werewolf shape, reduced briefly after being hit rather than shut off. Unbreakable Beast improves genuine low-health durability; Bloodlust Recovery heals on a *credited* hostile kill with a cooldown; Tempered Against Silver partly reduces a genuine silver attack only if the exact item/damage source can be identified, and must never negate the silver weakness.

**Feral Might (25).** Rending Claws improves real empty-hand native wolf melee. Savage Momentum builds bonuses only on real, source-owned successive hits, max three stacks; Bite Techniques affects real source-attributed bites, not arbitrary punches. Primal Fury adds passive low-health aggression, **not** Bare Zerker Fury or a new resource bar. Predator's Focus rewards fighting already wounded real hostile targets, rather than manufacturing blood or loot.

**Moonrunner (20).** Predator's Stride improves grounded beast-form speed; Pouncing Leap increases the character's ordinary jump (no extra button). Lunar Chase requires actual moonlight, real pursuit and real target history, not a generic night speed buff. Feral Excavation uses Progression's audited `native_mining=jawr_wolf_claw` speed-only predicate; no native claw harvest tier, drops, ore gating or build permissions change. Arrow Awareness gives modest projectile evasion while in true wolf shape.

**Wild Communion (20).** Wild Attunement improves actual personal Roots/Botania or other explicitly designated nature spell categories and may stack with Core via a documented cap, but must not increase passive mana generators/flowers. Rootsong amplifies actual personally credited nature healing spells, separate from physical HP regeneration. Grove Howl passively buffs *owned* nearby tamed wolves on eligible, actually cast nature spells; it does not summon, tame, breed or force unrelated wolves to obey. Verdant Resilience offers conditional defense while standing on genuine natural plant blocks—no biome-only arbitrary buff or reduced fire/silver damage.

**Moon Apex (5).** A passive late branch that enhances legitimate wolf claw damage and personally cast nature magic while the source's real full-moon conditions are met, with **five one-point ranks**. This is not another transformation, active spell, pet cap increase or permanent night buff. It grants neither immunity nor extra Fury.

## Source-first implementation crosswalk

| Existing handler or potential shared engine extension | Affected skills | Required source verification |
|---|---|---|
| Generic conditional `attribute` (max health, speed), `delayed_regeneration`, `heal_on_kill` | Feral Vigor, Regenerative Flesh, Bloodlust Recovery, Predator's Stride | Source-backed lycanthropy vs `jawr_shape` gates, real hunger/recent hit, current HP clipping and kill credit. Old #94 native pilot is **archival**, not active free ability |
| Generic `damage_multiplier`, `damage_at_low_health`, `hit_streak`, target-health damage predicates | Unbreakable Beast, Rending Claws, Savage Momentum, Primal Fury, Predator's Focus | E5 `params.native_attack=jawr_wolf_melee` permits source-owned empty-hand vanilla player attack only; exact JAWR bite/weapon damage attribution still requires gameplay verification |
| Generic `break_speed` with `params.native_mining=jawr_wolf_claw` | Feral Excavation | Speed-only existing seam; verify pinned claw tool and actual loot/harvest scope, no tier/fortune bypass |
| Generic `jump_bonus`, `projectile_deflection`/evasion, `status_on_hit` | Pouncing Leap, Arrow Awareness, Bite Techniques | Verify source-native shape, real projectile hit stage and actual JAWR bite damage ID before data activation |
| Moon/target history predicates and bounded passive modifiers | Lunar Chase, Moon Apex | Real Minecraft moon phase/sky/night conditions, not any night or unconditionally under-roof buff, plus legitimate tracked hostile |
| Source-specific silver recognition | Tempered Against Silver | Verify actual silver *item or damage-type* identities across installed mods; if absent, flag as unsupported instead of substituting generic DR |
| Source-verified personal nature magic (Roots/Botania/other) | Wild Attunement, Rootsong, Grove Howl, Moon Apex | Enumerate actual player-cast spell systems, cost/potency/healing events, provider absence, rightful caster, personally owned wolves; never count passive machines/flower FE/mana production |
| Natural block tag predicate | Verdant Resilience | Grounded on actual grass/moss/leaves at a legal server block position; exclude artificial block aliases, fire and silver; avoid per-tick scans |

## Native level gates and purchases

**Entry:** verified JAWR werewolf/lycanthrope affliction at native level ≥1, plus actual shape for claw and movement effects. **Adept:** *provisional* native level ≥4 or matching true JAWR milestone. **Mastery:** *provisional* native level ≥8 or a verified high-tier JAWR milestone. **Moon Apex:** both Primal Fury and Grove Howl bought to their required prerequisite ranks; only its effect (not its purchase) needs genuine full moon. These gates are tuning proposals, not proof such levels and unlocks exist in every pinned build. Keep source features inactive on unknown provider state.

The owner-approved single MXP wallet stays independent of Path, and personal Core skills are purchased separately. On native cure or invalid combination, the bonuses become dormant rather than making JAWR's form or retained native levels disappear. Test respec/refunds as source receipts before activation.

## Acceptance before live implementation

1. Confirm **20 paid nodes, exactly 100 rank costs, all one point**, no stray legacy multi-SP capstone, exact native level and form eligibility, source state persistence/restore and no unauthorized attribute carryover.
2. Measure max hearts while human-afflicted vs wolf shape, native/current HP and healing clipping after toggling, Feral Vigor vs Core vitality, high/low hunger and mid-fight vs out-of-combat regeneration. It must be **health**, not mana.
3. Test `jawr_wolf_melee` with empty-handed native claw, true JAWR bite, an equipped weapon, other player damage, projectiles and wolf mobs, proving no double reward or generic creature buff. Test actual 3-stack hit streak and genuine low-health gates.
4. Verify eligible hostile kill healing with proper owner, timeout and cooldown, no livestock kill farm credits; actual wolf status target/duration, real wounded prey check and nonboss status rules.
5. Test moon phase (full moon vs ordinary moon and day), clear sky vs roof/Nether, real chase target, grounded jump, fall handling, projectile damage and no new keybind/creative flight.
6. Verify legal claw mining throughput with actual native wolf claw tool; no new ores, Silk/Fortune duplication, block XP bypass or wrong tool drops.
7. Test real silver item/damage source before implementing Tempered Against Silver. The base native vulnerability must remain dangerous even at full rank; unknown silver IDs are a **design hold**, not 'reduce all melee'.
8. Prove Roots/Botania source-owned personally cast nature spells and healing, caster attribution and owned wolves; avoid rewarding flowers, machinery, rituals with no personal cast, or non-owner mobs. If provider lacks relevant behavior, revise the skill rather than create ghost effects.
9. Retest actual JAWR Wolfsbane initiation, native wolf/WEREWOLF transitions, moon-forced changes, diet, cures, relog, respawn, cross-Path compatibility, pet ownership and multiplayer. Source CI does not count as in-world PASS.
10. **No actual gameplay work was performed by this design PR.** Config-only future `feature/werewolf-*` content PR and shared engine `feature/progression-*` generic adapters are separate from the draft. Respect single first-party Progression module design #468.

## Historical assumptions explicitly superseded

The November 15 Tyz role catalog had much larger rank totals and many proposed effects. Existing pilot #94 had 21 source skills, all disabled, and is retained as source research rather than assumed live. The old Rage skill and its severe redirection of health into damage are not mandatory; **Primal Fury is a passive conditional damage bonus, not a second Fury bar**. Prior rank counts, native level thresholds and effects are examples, not immutable owner decisions. Silver source and nature spell IDs must be proven before shipping.

**Related:** [Vampire draft #518](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/518), [Sea Dragon #514](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/514), [remaining Paths #495](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/495), [JAWR form work #280](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/280), [Progression single-JAR architecture #468](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/468), [E5 generic adapter source audit](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/dev/progression/compat/progression/design/BATCH-E5-TRANSFORMATIONS.md).
