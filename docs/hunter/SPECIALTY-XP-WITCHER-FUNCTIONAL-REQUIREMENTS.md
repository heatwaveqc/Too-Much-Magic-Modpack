# Hunter — selectable quarry, kill/potion XP and Witcher-parity design audit

**Date:** 2026-11-15. **Status:** live source-code branch / draft PR; not a finished gameplay module and not part of the installed pack. **Depends on:** shared progression chassis PR #127, Trial of Venom PR #136, starter skill PR #141. **Taxonomy authority:** research PR #97, `CREATURE-TAXONOMY.md`.

## Implemented in this pilot

### Quarry specialization

**One free selection at Hunter rank 1**, saved in the existing persistent Hunter skill ledger. A single choice group prevents concurrent selection of a second family. A Hunter retains normal kill XP for any eligible creature; the chosen quarry earns twice the normal amount. The choice can currently be changed by a full Hunter respec; a distinct costed specialization-switch ritual may replace that later.

Available initial families: **Undead, Illager, Dragon, Void, Piglin, Sculk, Aquatic, Construct, Beast, Arthropod, Plant, Elemental, Ooze, Demon, Fae, Aberration**. No Prehistoric (dinosaurs are Beast); no Mage Slayer, Boss, Endborn/Netherborn, Spirit, Explosive, or dimension-as-species tree. Goety Cult, Wilden, Hobgoblin, etc. remain candidates pending owner review.

- Each choice is a one-rank, zero-point `hunter_specialty` marker pointing at `#tmm_progression:<family>`.
- Example commands: `/tmmprogress specialty list` and `/tmmprogress specialty dragon` (no Hunter GUI yet).
- A multi-family creature, e.g. Ender Dragon tagged both Dragon and Void, receives at most **one** 2× multiplier from the chosen category; specialty bonuses do not stack with themselves.
- The initial tags intentionally cover only established vanilla lineages and a few verified optional tags (e.g. Goety Enderlings, Witchery demons, DE Chaos Guardian). Empty or partial Construct/Fae/Aberration tags need the pinned creature-registry crosswalk before shipping. Tags represent lineage, **not** XP legality or faction friendliness.
- Existing Undead/Raider **pilot combat skills remain**; this does not yet rewrite every ability into the canonical 16-family taxonomy.

### HP-derived kill XP

For a qualified directly credited Hunter kill:

```
base_xp = max(1, round(victim.maximum_health / 4))
kill_xp = base_xp * (chosen_specialty_matches_victim ? 2 : 1)
```

The implementation rounds **after** the specialty multiplier, equivalent for whole-number HP divisible by 4; for fractional HP it preserves the intended ratio before rounding. The absolute safety limit is 1,000,000 XP per death event, mainly to prevent extreme edited-health mobs/overflow rather than to flatten ordinary bosses. Server `LivingDeathEvent` supplies trusted victim max health; no player-supplied XP value. The old fixed 5-XP Undead and 6-XP Raider *activity rewards* were removed, so they do not double-award. One-time 20-kill study milestones remain +100 XP/+2 points.

| Monster maximum HP | Normal Hunter XP | Chosen specialty XP |
| ---: | ---: | ---: |
| 20 | 5 | 10 |
| 40 | 10 | 20 |
| 200 | 50 | 100 |
| 500 | 125 | 250 |
| 1,000 | 250 | 500 |

**Eligibility P0 caveat:** only non-player living creatures registered in hostile `MobCategory.MONSTER` or an explicit `hunt_eligible` tag count. Tamed vanilla animals and `hunt_excluded` types are excluded. A new server-side `registerKillGuard` seam lets optional adapters reject summoned/owned/ritual/artificial prey; **these provider-specific adapters are not yet installed**. A process-local UUID deduper handles duplicate death events but cannot reliably detect farmed spawner kills or persistence across server restarts. The accompanying `recordFiltered` gate ensures Hunter milestones are not counted for rejected victims, while other progression paths still see their own signals. Do not call this completely anti-farm yet. Spell/summon ownership attribution likewise remains an integration task.

### Potion consumption XP

**Designed to reward practical alchemy and experimentation, rather than unlimited bottled-water grinding.** A completed drink of an explicitly listed base potion variant grants Hunter XP. Data-defined brewing-difficulty score distinguishes 41 approved vanilla variants; modded potions can join only after adding verified exact registry IDs and difficulty ratings.

```
potency = 1 + 0.5 * min(highest_effect_amplifier, 10)
duration_factor = 1 + min(1.5, sqrt(longest_effect_duration_ticks / 3600) / 2)
potion_xp = round(2 * brewing_difficulty * potency * duration_factor)
if first time drinking this registered variant: potion_xp *= 2  [before rounding]
```

Configurable in Hunter JSON: base multiplier `2`, discover multiplier `2`, cooldown `12,000 ticks` (10 real minutes at 20 TPS), brewing-difficulty map, kill divisor and specialty multiplier. Repeated uses of the *same exact potion registry ID* earn XP only when at least `max(12,000 ticks, longest_effect_duration_ticks)` have elapsed since that player's previous credited use. This is persisted in per-player `PLAYER_MODES` using the Overworld game-time clock, across death/relog/server restart. The initial discovery payout is allowed immediately upon a successful first drink.

- Valid: original, completed, normal `minecraft:potion` with a curated base potion and at least one effect. Base data rather than player-controlled descriptions supplies the attributes.
- Invalid: water/awkward/mundane, splash/lingering, interrupted drinking, custom-effect spoofed stacks, empty effects, unknown registry IDs, creative/spectator, non-Hunter players.
- Difficulty is an intentionally **curated cost/complexity rating** rather than a live brewing-stand ingredients-cost oracle. Automatically determining actual recipes would require inspecting the pinned vanilla/modded `PotionBrewing` registry/recipe providers and full ingredient costs. Such a bridge can be added after the recipe audit; numeric difficulty is tuneable by datapack.
- This pilot does **not** implement alchemy Toxicity, oils, decoctions or brewing mastery bonuses. Potion XP measures drinking, not who originally crafted it.

**Preliminary implication:** On the current 28,000-XP curve, a 20-HP specialty mob gives 10 XP, while a 500-HP specialty boss gives 250. This still represents 2,800 ordinary specialty kills or 112 such boss kills if no potion/milestone XP is earned. Those are **not** final progression targets. No cap/rank curve has been changed.

## Fictional Witcher gameplay capability target

The mod should reproduce the **functionality**, not require the Witcher world, named characters, a new dimension or an external class mod. Hunter should be standalone with optional adapters to Iron's Spells, Ars Nouveau, Witchery, Hexerei, Neo Vitae, Occultism, Apotheosis and creature mods. The Witcher style is a versatile Hunter build, not a forced separate profession replacing every other Hunter choice.

### 1. Mutated hunter physiology and survival

**High priority.** Permanent Poison resistance after the Trial of Venom, reduced harmful-effect duration, disease/curse resistances where safe, enhanced senses, night vision, healing/endurance, reflexes, acrobatics, precise dodges, deflections, and speed. Some framework buffs (Rooted, Evasion, Fast Hands and existing melee bonuses) are already piloted. **Missing:** real persistent Poison immunity, timed-duration reduction, distinct Witcher toxicity resistance, species transformation veto adapters, responsive dodge and parry. The enrollment ritual currently clears the Poison acquired that instant but is not permanent poison immunity.

### 2. The hunt: senses, bestiary, signs of life and contracts

**High priority.** A toggleable Hunter Senses mode: track trails from real entities (prints/blood/sound/scent in a bounded region, with age and dimension), highlight inspectable evidence, detect nearby threats, show recent quarry direction without omniscient wall-hacks, and record findings in a persistent bestiary. Contracts combine clue, preparation, verified kill and trophy/turn-in, with rarity, repeatability and sensible XP. Optional medallion can detect magic/monsters, with positive feedback and no requirement to own the item. Existing generic advancement/milestone support is only a starting point; neither visual tracking nor contracts exists yet.

### 3. Combat & materials

**High priority.** Steel/silver weapon effectiveness by **creature susceptibility and material** (not universal silver bonus); sword combos, quick vs heavy attacks, timed parry, dodge roll/evade, counterattack, crossbow tools, blade coating and monster-weakness study. Hunter's general sword/axe/scythe/bow/crossbow specialists already exist as starter passives. Multi-category interaction and Apotheosis affix/enchants must be audited for additive/excessive stacking.

### 4. Five core Signs, optional lore variants

**High priority but independently budgeted.** Five low-ceiling, short-cast active powers, each with an optional improved/alternate mode and a shared Stamina/Focus meter distinct from normal spellbook mana:

| Sign | Functional equivalent | Standalone implementation target |
| --- | --- | --- |
| Aard | Short-range kinetic shove/stagger | Server-validated cone knockback, break only whitelisted weak objects |
| Igni | Flame burst/ignite | Bounded cone ignition, claims and fire-spread safe |
| Quen | Personal ward | Timed damage absorb, no indefinite invulnerability |
| Yrden | Warding/snare circle | Ground AoE that slows and can reveal intangible/wraith-tagged foes |
| Axii | Brief charm/mental influence | Non-boss PvE stagger/de-aggro; optionally temporary hostility redirection, no forced player mind control |

A possible sixth lore Sign, **Heliotrope**, can remain an optional defensive technique; not required for baseline Witcher 3 functionality. Reusing Iron's/Ars spell resources is an **optional compatibility implementation**, but standalone signs must have a local mechanic even if those spell mods are not installed. Shared stamina/cooldowns/cast priorities need server authority and modded claims checks.

### 5. Alchemy as a real Hunter branch

**High priority.** Toxicity meter with thresholds and decay; tolerance, potion duration/potency, antidotes and cleansing; specialized decoctions using rare monster components, temporary body/skill buffs and high persistent toxicity; blade oils per quarry family with limited hits/duration; anti-monster bombs and traps; potion formula knowledge and brewing expertise. Drinking XP is a start only. Evaluate using Hexerei/Neo Vitae existing consumables, not cloning dozens of decorative bottles. Do **not** equate higher XP reward with safe potion use; high-impact decoctions should remain a deliberate risk/reward choice. Consider meditation/replenishment as configurable convenience, not duplicate-item generation on a modded survival server.

### 6. Mutagens, schools and loadout identity

**Medium priority.** Harvest rare verified boss parts / mutagens (watch farm dupe), unlock enhancement slots and permanent or long-duration adaptations. A secondary optional School training choice (e.g. fast/defensive/signs/alchemy/bleed) could sit **beside** the creature specialty, not replace it. This offers Witcher-like styles without forcing every Hunter into one fiction-specific template. Trophies, medallion, armor and two-weapon swaps should be equipment/curio optional with clear stat limits.

### 7. Narrative/gameplay fit without forced lore

**Medium priority.** Monster contract boards, clues, potion prep, hunts, rewards, knowledge unlocks, inspectable journal and optional NPC clients. The system should not require Geralt, Witcher schools by trademark, the Continent dimension, or artificial faction dialogue. Some monsters ought to be pacifiable or handled with research rather than always slain; do not award hunting XP for harvesting passive villagers or owned creatures.

## Capability matrix and build order

| Pillar | Current code | What remains |
| --- | --- | --- |
| Trial/enrollment | Potion+advancement+Regen implemented | Poison physiology and form veto |
| One creature specialty | 16 data choices/choice lock/XP bonus | Full pinned species crosswalk; GUI |
| General combat | Evasion, Rooted, weapon specialist, Fast Hands pilots | dodge, parry, hybrid combos, modded projectiles |
| Hunt XP | max HP / 4, chosen 2× | claim/kill owner guard integrations and boss tests |
| Potion XP | effect-derived XP, curated brewing difficulty, persistent cooldown | modded potion registry, recipe-cost auditing |
| Witcher Senses | none | tracking signals, evidence, player UI, journal |
| Signs | none | active resource, 5 signs, safety/claims |
| Alchemy/toxicity | drinking XP only | toxicity, decoctions, oils, bombs, antidotes |
| Contracts/bestiary | native milestones only | evidence, contracts, unique trophy credit |
| Schools/mutagens | none | equipment/capability/reward design |

**Suggested order:** (A) finish Hunter physiology and provenance; (B) enrich taxonomy and actual available prey; (C) implement Senses + bestiary; (D) implement toxicity/oils/decoctions and signs as independent modules; (E) add contracts/mutagens/schools; (F) profile XP sources and total skill-point spend against 16 specialty paths before approving rank count or cap.

**Cap decision deferred as requested:** the active path is still 20 ranks/28,000 XP, and its 24-point ceiling is insufficient even for some existing 10-rank pilot skills. Adding full Witcher parity would increase the number of purchasable *options* dramatically; level cap and points must be derived from desired complete builds and real encounter frequency, not set by counting all skills as though a player should buy every branch.

### References (mechanics, not game assets)

- [Official Witcher Wiki — witchers and equipment](https://witcher-games.fandom.com/wiki/Witcher)
- [Witcher Wiki — The Witcher 3: Guide to Monster Hunting](https://witcher.fandom.com/wiki/The_Witcher_3:_Wild_Hunt_-_Guide_to_Monster_Hunting)
- [Witcher Wiki — Witcher Senses](https://witcher.fandom.com/wiki/Witcher_Senses)
- [Official Witcher Wiki — Signs](https://witcher-games.fandom.com/wiki/Sign_effects)
- [Witcher Wiki — Potions](https://witcher.fandom.com/wiki/The_Witcher_3_potions)
- [Witcher Wiki — Potion Toxicity](https://witcher.fandom.com/wiki/The_Witcher_3_potions)
