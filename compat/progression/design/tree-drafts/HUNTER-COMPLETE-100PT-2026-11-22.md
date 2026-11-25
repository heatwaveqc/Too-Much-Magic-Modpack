# Hunter — complete 100-point, 57-option monster-specialist skill tree

**Too Much Magic · November 22, 2026 · DESIGN ONLY / NOT INSTALLED / ALL NEW NUMERICS REVIEW CANDIDATES**

Branch `feature/hunter-100pt-complete-tree-20261122` is stacked on Bare Zerker PR #553 → Gilded #547 → Forest #536 → Cave #530 → Lich #526 → Werewolf #520 → Vampire #518 → Sea #514 → original catalog #495. The only changes are this design specification and two first-pass ledger files. No runtime Path, Java handler, source-recipes, creature tag, loot table, KubeJS, package, CI or Prism build is modified.

## Hunter identity and what remains FREE

Hunter is a **professional monster hunter** who can earn a living through ordinary hunting Decrees, wield selected weapons well, maintain field equipment, track and identify quarry, and specialize deeply against exactly **one chosen prey family**. It should outperform a generalist in its chosen specific encounter, not receive blanket anti-every-boss or infinite monster-loot benefits.

- **Trial of Venom:** preserve the real server-checked enrollment procedure: consume actual approved Poison potion while under Regeneration after any required verified prior hostile kill; source `enrollment_trigger` already enforces the approved potion IDs and trial event. It is a free Path initiation, not a bought node, and must be reconciled with Newbie graduation and mutually exclusive human disciplines.
- **Favored Prey licenses:** all **14 existing live `specialty_*` nodes** remain zero-cost, one-at-a-time choices in `tyr_progression:hunter_favored_enemy`. Each selected free license gates only its own 30 points. No category automatically has an exclusive boss status, no new 15th Fae choice is smuggled into live data, and all taxonomy exclusions must be source-verified.
- **Hunter XP:** actual verified hostile kill XP is source-owned. The historical native `maxHP/4` (and favored prey ×2) was built for a *different* XP curve and **MUST NOT** be copied numerically into the 1,000-base-MXP-per-point unified default. Instead implement appropriate single verified kill receipt, source-calibrated MXP reward and optional favored bonus according to owner mode #475, with no double credit. Global Core and its earned wallet remain independent of Path. Newbie earns no MXP. In configurable hybrid/separate modes, Path XP is routed as explicitly defined.
- **Mutagenic Brews / Witcher-like mechanics:** preserve the standalone already-designed Hunter bio-mutagen recipe and 10-hour progress tracking to permanent permitted potion mutations, non-Hunter Wither punishment, nonstacking effects and hunger consumption cost. These are free native Path/consumable features, **not a 15th shared skill, extra active button or paid second potion system**. Current live data uses `target_duration_ticks:720000`; exact source effects and removal semantics are separate integration QA holds.
- **Hunting Decree economy:** approved issue #523 makes the *ordinary* `tmm_hunting` Bountiful Decree **coins-only and available to non-Hunters**. Hunter can be an effective earner by actually hunting, but the tree must never claim the general decree or forge a Hunter-only coin wallet. Existing general decree implementation remains owned by Bountiful/Quest Shop workstreams, not Hunter tree code.
- **No additional active buttons:** Quarry Mark happens as a consequence of real successful attacks; Tracking is a bounded, private passive highlight; Monster Lore observes an actually visible entity. Gear repair, projectile source, evasion and prey mastery use verified player-owned actions. No unsourced potion buff UI or invented damage category.

## Purchase invariant

| Component | Displayed paid skills | Ranks available to ONE Hunter |
|---|---:|---:|
| Shared Hunter Training | 13 | 62 |
| Choose one: Equipment Care OR Fortified Training | 2 | 8 |
| Choose one of 14 favored prey trees (3 nodes each) | 42 | 30 |
| **Visible catalog / one legal build** | **57** | **100** |

A single Hunter purchases exactly **17 node identities** when everything in that character's legal selections is maxed (13 shared + 1 style + 3 favored-prey), at exactly one advancement point per rank. The 57 displayed paid skill identities represent all alternative selections, not 57 simultaneously attainable skills. The full visible-option rank sum is 498, not a player budget. Original 14 **free** license nodes are separate and must not cost a point.

## Shared Hunter Training — 13 skills / 62 one-point ranks

Source-verified where appropriate: the current `dev/progression` Hunter JSON exposes +0.1 knockback resistance per Rooted rank, +1 armor per Monster Resilience rank, +0.5 per relevant direct weapon damage rank, +1 Silver damage per rank, and a source `fast_hands` coefficient of +0.2 per rank. The new detailed timing/attribution and tracking proposals below require testing; do not assume the source `fast_hands` +0.2 implies 20% shorter time.

| Skill | Ranks | Candidate effect | Exact source/handler hold |
|---|---:|---|---|
| **Rooted** (`tyr_progression:rooted`) | 5 | +0.10 knockback resistance per rank (+0.50 full), only while genuinely Hunter; use source attribute, no armor or knockback immunity granted. | Source `minecraft:knockback_resistance` 0.1/rank and total attribute cap |
| **Monster Resilience** (`tyr_progression:monster_resilience`) | 5 | +1 armor point per rank (+5 full), player-owned Hunter effect; distinct from separately selected Fortified Training. | Source `minecraft:armor` 1/rank, stacking against Fortified |
| **Sword Specialist** (`tyr_progression:sword_specialist`) | 5 | +0.5 damage per rank (+2.5 full) on genuine direct sword melee; the actual weapon and player must be the attack source. | Real mainhand swords tag and `delivery:melee` |
| **Axe Specialist** (`tyr_progression:axe_specialist`) | 5 | +0.5 damage per rank (+2.5 full) on genuine direct axe melee; no bonus to mining or throwable axes. | Real axe tag and direct hit identity |
| **Scythe Specialist** (`tyr_progression:scythe_specialist`) | 5 | +0.5 damage per rank (+2.5 full) only with eligible scythe item on direct melee; no arbitrary AoE or extra mob drops. | Current scythe weapon tag IDs and direct-hit scope |
| **Bow Specialist** (`tyr_progression:bow_specialist`) | 5 | +0.5 per rank (+2.5 full) for arrows truly fired from a bow by this Hunter, using shot-time source weapon rather than current hand on hit. | Player-shot bow arrows/UUID snapshots and direct damage handler |
| **Crossbow Specialist** (`tyr_progression:crossbow_specialist`) | 5 | +0.5 per rank (+2.5 full) for actual crossbow arrows; firework rockets or other ammunition require explicit provider verification. | Player-fired crossbow arrow origin; exclude fireworks by default |
| **Fast Hands Bow** (`tyr_progression:fast_hands_bow`) | 5 | +0.20 source `fast_hands` rate units per rank (+1.0 units full) for real bow draw; exact draw-time conversion and power floor require pinned engine audit. | Verify 0.20 source multiplier semantics, bow power and real draw ticks |
| **Fast Hands Crossbow** (`tyr_progression:fast_hands_crossbow`) | 5 | +0.20 source `fast_hands` rate units per rank (+1.0 units full) for real crossbow reload; must respect Quick Charge and native minimum charge. | Verify 0.20 source reload math, Quick Charge and live reloading |
| **Silver Hunter** (`tyr_progression:silver_hunter`) | 5 | +1 direct melee damage per rank (+5 full) with a genuine tagged silver weapon against tagged lycanthrope creatures; never global silver damage. | Canonical silver-weapon tags and JAWR lycan taxonomy |
| **Monster Lore** (`tyr_progression:hunter_monster_lore`) | 4 | Passively show verified target taxonomy and source-known weakness at 8 + 4 blocks per rank (24 full) when target is genuinely seen, without spoiling hidden bosses. | Safe server/client taxonomy HUD, verified creature weaknesses |
| **Quarry Mark** (`tyr_progression:hunter_quarry_mark`) | 4 | After one successful direct Hunter attack, passively remember exactly one living hostile target for 4 + 2 seconds per rank (12 seconds full), replacing earlier quarry; no new key. | Source hit receipt, one target UUID, expiry/death cleanup |
| **Hunter Tracking** (`tyr_progression:hunter_hunter_tracking`) | 4 | For favored prey or the one real marked quarry, privately outline at most 1 + rank legal targets within 8 + 4×rank blocks (up to five, 24 blocks); source-check every 40 ticks and never reveal all players. | Client-only private target outline, range, visibility and safe entity cap |

## Training Style — choose ONE of two 8-rank lanes

These are **mutually exclusive** from the first purchased rank. A normal respec and complete point reconciliation are required before swapping. Do not silently let a Hunter buy eight ranks from both and count 16/100. Both target a sustainable adventuring career, not Hunter-only equipment items.

| Choice | Ranks | Candidate effect | Source/handler hold |
|---|---:|---|---|
| **Equipment Care** (`tyr_progression:equipment_care`) | 8 | Repair exactly one real point of worn armor or active mainhand equipment durability every (220 - 20×rank) ticks, 200 at rank1 to 60 at rank8; no offhand, inventory or broken-item resurrection. | Existing handler `slot: all` needs safe armor+mainhand scope, no duplicate repair on same gear |
| **Fortified Training** (`tyr_progression:fortified_training`) | 8 | Gain +0.5 armor points per rank (+4 at rank8) as a player-owned attribute, distinct from Monster Resilience, with no permanent armor item. | Existing 3-rank armor source reprice; stacking cap with common armor |

## Favored Prey — choose ONE of 14, 30 one-point ranks

**Eligibility:** each branch requires its unchanged original `specialty_*` free choice. An unselected branch is not purchasable, cannot grant benefits, and cannot hold ranks for simultaneous future activation. Refund/clear ranks and ownership on a legal specialty switch. Every specialization uses exactly **12 offense + 8 defense + 10 specialty mastery**, and every rank costs **1**. The specialty's prey entity classifier is an exact source list, not a naming heuristic or world biome guess.

**Damage/cap design:** standard new taxonomy candidates are +3% per offensive rank (+36%) against genuinely tagged quarry, and -2% per defensive rank (-16%) for legitimate prey-sourced attacks. The older Undead and Raider slayer/evasion skills retain their original source-targeted weapon and evasion identities instead, with new numeric balancing candidates. All modifiers are calculated additively with other purchased effects where possible, not a recursive multiplication chain. Mastery nodes have a narrower genuine quarry interaction, not a new combat hotkey. All numbers are source/power-benchmark candidates, especially regarding real Chaos Guardian and specialist Monk ceilings.

| Prey selection | Free unchanged license | Offense / Defense / Mastery | Spend |
|---|---|---|---:|
| **Undead specialty** | `tyr_progression:specialty_undead` | Undead Slayer / Undead Evasion / Ranged Quarry | 12 + 8 + 10 = 30 |
| **Raider specialty** | `tyr_progression:specialty_illager` | Raider Slayer / Raider Evasion / Raidbreaker | 12 + 8 + 10 = 30 |
| **Drakebane specialty** | `tyr_progression:specialty_dragon` | Drakebane / Scaled Hunter / Dragonbane Tactics | 12 + 8 + 10 = 30 |
| **Void specialty** | `tyr_progression:specialty_void` | Void Slayer / Voidwalker Guard / Rift Tracker | 12 + 8 + 10 = 30 |
| **Piglin specialty** | `tyr_progression:specialty_piglin` | Piglin Slayer / Bastion Guard / Hoglin Harrier | 12 + 8 + 10 = 30 |
| **Sculk specialty** | `tyr_progression:specialty_sculk` | Sculk Slayer / Deepdark Vigilance / Sonic Defense | 12 + 8 + 10 = 30 |
| **Aquatic specialty** | `tyr_progression:specialty_aquatic` | Aquatic Slayer / Deepwater Guard / Harpoon Hunter | 12 + 8 + 10 = 30 |
| **Construct specialty** | `tyr_progression:specialty_construct` | Construct Slayer / Construct Plating / Golem Breaker | 12 + 8 + 10 = 30 |
| **Beast specialty** | `tyr_progression:specialty_beast` | Beast Slayer / Beast Evasion / Apex Pursuit | 12 + 8 + 10 = 30 |
| **Arthropod specialty** | `tyr_progression:specialty_arthropod` | Arthropod Slayer / Venom Guard / Webbreaker | 12 + 8 + 10 = 30 |
| **Plant specialty** | `tyr_progression:specialty_plant` | Plant Slayer / Spore Guard / Creeper Ward | 12 + 8 + 10 = 30 |
| **Elemental specialty** | `tyr_progression:specialty_elemental` | Elemental Slayer / Primordial Guard / Elemental Analysis | 12 + 8 + 10 = 30 |
| **Ooze specialty** | `tyr_progression:specialty_ooze` | Ooze Slayer / Slime Resilience / Viscous Tactics | 12 + 8 + 10 = 30 |
| **Demon specialty** | `tyr_progression:specialty_demon` | Demon Slayer / Infernal Guard / Exorcist | 12 + 8 + 10 = 30 |

### Undead specialty — undead (30 points)

**FREE choice license:** `tyr_progression:specialty_undead`; at most ONE of fourteen active. Category-specific source hold: Source undead tag and old undead_slayer, evasion_undead, ranged_quarry numerical reprice.

| Purchased skill | Ranks | Candidate effect | Milestone/required license |
|---|---:|---|---|
| **Undead Slayer** (`tyr_progression:undead_slayer`) | 12 | Actual sword melee against verified undead: +0.75 physical damage/rank (+9 full), preserving the source sword-restricted Slayer role. | entry; tyr_progression:specialty_undead; free undead choice |
| **Undead Evasion** (`tyr_progression:evasion_undead`) | 8 | +2 percentage points/rank (+16 pp full) to evade real undead-origin direct hits; not world/environment damage. | adept; tyr_progression:undead_slayer; free undead choice |
| **Ranged Quarry** (`tyr_progression:ranged_quarry`) | 10 | Actual Hunter-fired bow/crossbow arrow hit against undead: +0.30 damage/rank (+3 full), preserving the source Ranged Quarry arrow-delivery role. | mastery; tyr_progression:evasion_undead; free undead choice |

### Raider specialty — illager (30 points)

**FREE choice license:** `tyr_progression:specialty_illager`; at most ONE of fourteen active. Category-specific source hold: Illager taxonomy, raider direct hit and evoker projectile/fangs actual source; source IDs raider_slayer/evasion_raiders.

| Purchased skill | Ranks | Candidate effect | Milestone/required license |
|---|---:|---|---|
| **Raider Slayer** (`tyr_progression:raider_slayer`) | 12 | Actual direct axe melee against genuine Illagers/raiders: +0.75 damage/rank (+9 full), preserving old Raider Slayer axe restriction. | entry; tyr_progression:specialty_illager; free illager choice |
| **Raider Evasion** (`tyr_progression:evasion_raiders`) | 8 | +2 percentage points/rank (+16 pp full) evasion against raider-sourced incoming direct hit. | adept; tyr_progression:raider_slayer; free illager choice |
| **Raidbreaker** (`tyr_progression:hunter_illager_mastery`) | 10 | -2% damage/rank (-20% full) from genuine Illager raid-projectile, evoker-fangs or Ravager-source attacks, excluding generic village raids. | mastery; tyr_progression:evasion_raiders; free illager choice |

### Drakebane specialty — dragon (30 points)

**FREE choice license:** `tyr_progression:specialty_dragon`; at most ONE of fourteen active. Category-specific source hold: Pinned dragons vs Chaos Guardian, Ender Dragon and transformed player PvP rules; native breath state.

| Purchased skill | Ranks | Candidate effect | Milestone/required license |
|---|---:|---|---|
| **Drakebane** (`tyr_progression:hunter_dragon_slayer`) | 12 | +3% legitimate Hunter direct/shot damage per rank (+36% full) to source-verified Dragon prey, never all bosses. | entry; tyr_progression:specialty_dragon; free dragon choice |
| **Scaled Hunter** (`tyr_progression:hunter_dragon_defense`) | 8 | -2% damage/rank (-16% full) from actual Dragon-sourced breath, claw or melee hits, not ordinary fire. | adept; tyr_progression:hunter_dragon_slayer; free dragon choice |
| **Dragonbane Tactics** (`tyr_progression:hunter_dragon_mastery`) | 10 | When confirmed Dragon prey is performing its own verified breath, +1.5% legitimate Hunter direct/arrow damage per rank (+15% full) during its 4-second actual breath window. | mastery; tyr_progression:hunter_dragon_defense; free dragon choice |

### Void specialty — void (30 points)

**FREE choice license:** `tyr_progression:specialty_void`; at most ONE of fourteen active. Category-specific source hold: Tagged Enderman/shulker/void entities, teleports and Dragon/Void taxonomy overlap, no player teleport shortcut.

| Purchased skill | Ranks | Candidate effect | Milestone/required license |
|---|---:|---|---|
| **Void Slayer** (`tyr_progression:hunter_void_slayer`) | 12 | +3% eligible physical/shot damage/rank (+36% full) to tagged Void creatures; no free buff merely for being in End. | entry; tyr_progression:specialty_void; free void choice |
| **Voidwalker Guard** (`tyr_progression:hunter_void_defense`) | 8 | -2% direct damage/rank (-16%) from actual Void-creature attack; NEVER prevents void-fall/kill-plane. | adept; tyr_progression:hunter_void_slayer; free void choice |
| **Rift Tracker** (`tyr_progression:hunter_void_mastery`) | 10 | On a genuine mark hit against Void prey that teleports, retain that same quarry's identity +2s/rank (+20s) within 24 blocks; no cross-dimension/unloaded x-ray. | mastery; tyr_progression:hunter_void_defense; free void choice |

### Piglin specialty — piglin (30 points)

**FREE choice license:** `tyr_progression:specialty_piglin`; at most ONE of fourteen active. Category-specific source hold: Piglin/Hoglin/Zoglin tags, human Gilded kinship and Beast overlap, ownership/friendly rules.

| Purchased skill | Ranks | Candidate effect | Milestone/required license |
|---|---:|---|---|
| **Piglin Slayer** (`tyr_progression:hunter_piglin_slayer`) | 12 | +3% actual physical/shot damage/rank (+36%) against valid hostile Piglin/Hoglin/Zoglin quarry, not retaliation or kinship manipulation. | entry; tyr_progression:specialty_piglin; free piglin choice |
| **Bastion Guard** (`tyr_progression:hunter_piglin_defense`) | 8 | -2% direct damage/rank (-16%) from true Piglin/Hoglin/Zoglin entity attacks, no AI anger alteration. | adept; tyr_progression:hunter_piglin_slayer; free piglin choice |
| **Hoglin Harrier** (`tyr_progression:hunter_piglin_mastery`) | 10 | +1.5% direct melee damage/rank (+15%) against specifically verified Hoglin/Zoglin quarry with sword/axe, not against all Piglins. | mastery; tyr_progression:hunter_piglin_defense; free piglin choice |

### Sculk specialty — sculk (30 points)

**FREE choice license:** `tyr_progression:specialty_sculk`; at most ONE of fourteen active. Category-specific source hold: Warden boom damage source ID, Deep Dark creature registry and protections, no hostile mob spoof.

| Purchased skill | Ranks | Candidate effect | Milestone/required license |
|---|---:|---|---|
| **Sculk Slayer** (`tyr_progression:hunter_sculk_slayer`) | 12 | +3% actual hit damage/rank (+36%) against valid Sculk-associated creatures including Warden only if in source taxonomy; not blocks. | entry; tyr_progression:specialty_sculk; free sculk choice |
| **Deepdark Vigilance** (`tyr_progression:hunter_sculk_defense`) | 8 | -2% received physical hit damage/rank (-16%) from confirmed Sculk creature source, not all sonic damage. | adept; tyr_progression:hunter_sculk_slayer; free sculk choice |
| **Sonic Defense** (`tyr_progression:hunter_sculk_mastery`) | 10 | -2% actual Warden-sourced sonic boom damage/rank (-20%), with actual attacker receipt; do not silence sensors or waive stealth. | mastery; tyr_progression:hunter_sculk_defense; free sculk choice |

### Aquatic specialty — aquatic (30 points)

**FREE choice license:** `tyr_progression:specialty_aquatic`; at most ONE of fourteen active. Category-specific source hold: Aquatic prey registry and underwater source checks, real trident/arrow source snapshot.

| Purchased skill | Ranks | Candidate effect | Milestone/required license |
|---|---:|---|---|
| **Aquatic Slayer** (`tyr_progression:hunter_aquatic_slayer`) | 12 | +3% legitimate damage/rank (+36%) against actual Aquatic species prey, not anything temporarily submerged. | entry; tyr_progression:specialty_aquatic; free aquatic choice |
| **Deepwater Guard** (`tyr_progression:hunter_aquatic_defense`) | 8 | -2% direct damage/rank (-16%) from actual aquatic creature while the Hunter is underwater; not drowning or all water damage. | adept; tyr_progression:hunter_aquatic_slayer; free aquatic choice |
| **Harpoon Hunter** (`tyr_progression:hunter_aquatic_mastery`) | 10 | +2% genuine trident/arrow damage/rank (+20%) against Aquatic prey while Hunter really submerged, with weapon provenance. | mastery; tyr_progression:hunter_aquatic_defense; free aquatic choice |

### Construct specialty — construct (30 points)

**FREE choice license:** `tyr_progression:specialty_construct`; at most ONE of fourteen active. Category-specific source hold: Actual source construct entity IDs, target armor math, tamed/owner golem exclusion.

| Purchased skill | Ranks | Candidate effect | Milestone/required license |
|---|---:|---|---|
| **Construct Slayer** (`tyr_progression:hunter_construct_slayer`) | 12 | +3% damage/rank (+36%) versus audited living construct mobs only; not machines, blocks or owned players' golems. | entry; tyr_progression:specialty_construct; free construct choice |
| **Construct Plating** (`tyr_progression:hunter_construct_defense`) | 8 | -2% true Construct-origin direct hit damage/rank (-16%), not blanket shield against all magical attacks. | adept; tyr_progression:hunter_construct_slayer; free construct choice |
| **Golem Breaker** (`tyr_progression:hunter_construct_mastery`) | 10 | Ignore 1.5% target armor *contribution* per rank (15% full) on legal physical hit of armored Construct, not invulnerable boss shields. | mastery; tyr_progression:hunter_construct_defense; free construct choice |

### Beast specialty — beast (30 points)

**FREE choice license:** `tyr_progression:specialty_beast`; at most ONE of fourteen active. Category-specific source hold: Source Beast taxonomy with pets/domestics protected and real movement/target UUID.

| Purchased skill | Ranks | Candidate effect | Milestone/required license |
|---|---:|---|---|
| **Beast Slayer** (`tyr_progression:hunter_beast_slayer`) | 12 | +3% legitimate damage/rank (+36%) against audited hostile wild beasts/dinosaurs, excluding owner pets and non-hostile livestock. | entry; tyr_progression:specialty_beast; free beast choice |
| **Beast Evasion** (`tyr_progression:hunter_beast_defense`) | 8 | +1.5 percentage points/rank (+12 pp) evasion against direct hostile wild Beast attacks. | adept; tyr_progression:hunter_beast_slayer; free beast choice |
| **Apex Pursuit** (`tyr_progression:hunter_beast_mastery`) | 10 | After real hit marking Beast quarry, +2% chase speed/rank (+20%) while grounded approaching same living target inside 24 blocks; no flight. | mastery; tyr_progression:hunter_beast_defense; free beast choice |

### Arthropod specialty — arthropod (30 points)

**FREE choice license:** `tyr_progression:specialty_arthropod`; at most ONE of fourteen active. Category-specific source hold: Actual spider poison/web provenance, source attacks and Bane of Arthropods stacking.

| Purchased skill | Ranks | Candidate effect | Milestone/required license |
|---|---:|---|---|
| **Arthropod Slayer** (`tyr_progression:hunter_arthropod_slayer`) | 12 | +3% legitimate damage/rank (+36%) to tagged arthropod targets; not a spider biome or web blocks. | entry; tyr_progression:specialty_arthropod; free arthropod choice |
| **Venom Guard** (`tyr_progression:hunter_arthropod_defense`) | 8 | -2% legitimate Arthropod-origin poison damage/duration OR direct hit damage/rank (-16%), choose single eligible source to prevent double apply. | adept; tyr_progression:hunter_arthropod_slayer; free arthropod choice |
| **Webbreaker** (`tyr_progression:hunter_arthropod_mastery`) | 10 | Reduce source-confirmed spider-web movement slowdown 6%/rank (60% full) while real hostile Arthropod prey has trapped player; no universal cobweb pass-through. | mastery; tyr_progression:hunter_arthropod_defense; free arthropod choice |

### Plant specialty — plant (30 points)

**FREE choice license:** `tyr_progression:specialty_plant`; at most ONE of fourteen active. Category-specific source hold: Creeper classified as Plant by TMM, explosion actor source; no crop loot.

| Purchased skill | Ranks | Candidate effect | Milestone/required license |
|---|---:|---|---|
| **Plant Slayer** (`tyr_progression:hunter_plant_slayer`) | 12 | +3% real player-origin physical/shot damage/rank (+36%) to living Plant prey, including Creeper only by audited TMM taxonomy. | entry; tyr_progression:specialty_plant; free plant choice |
| **Spore Guard** (`tyr_progression:hunter_plant_defense`) | 8 | -2% real Plant-creature-origin direct hit damage/rank (-16%); not all poison or any plant block. | adept; tyr_progression:hunter_plant_slayer; free plant choice |
| **Creeper Ward** (`tyr_progression:hunter_plant_mastery`) | 10 | -2% actual Creeper/charged Creeper-source explosion damage/rank (-20%); TNT, Ghasts and other explosions unchanged. | mastery; tyr_progression:hunter_plant_defense; free plant choice |

### Elemental specialty — elemental (30 points)

**FREE choice license:** `tyr_progression:specialty_elemental`; at most ONE of fourteen active. Category-specific source hold: Source elemental mob roster and factual per-element vulnerability IDs, no arbitrary element-name inference.

| Purchased skill | Ranks | Candidate effect | Milestone/required license |
|---|---:|---|---|
| **Elemental Slayer** (`tyr_progression:hunter_elemental_slayer`) | 12 | +3% real damage/rank (+36%) versus verified living elemental enemies, independent of current biome or spell animation color. | entry; tyr_progression:specialty_elemental; free elemental choice |
| **Primordial Guard** (`tyr_progression:hunter_elemental_defense`) | 8 | -2% direct attack damage/rank (-16%) from valid living Elemental target as true attacker, not all elemental magic in world. | adept; tyr_progression:hunter_elemental_slayer; free elemental choice |
| **Elemental Analysis** (`tyr_progression:hunter_elemental_mastery`) | 10 | If Hunter actually uses a documented elemental weakness against corresponding live Elemental quarry, +2% hit damage/rank (+20%); unknown weaknesses fail closed. | mastery; tyr_progression:hunter_elemental_defense; free elemental choice |

### Ooze specialty — ooze (30 points)

**FREE choice license:** `tyr_progression:specialty_ooze`; at most ONE of fourteen active. Category-specific source hold: Ooze/Slime child-split farming and Sapient Slime owned ally protections; source sticky debuff tag.

| Purchased skill | Ranks | Candidate effect | Milestone/required license |
|---|---:|---|---|
| **Ooze Slayer** (`tyr_progression:hunter_ooze_slayer`) | 12 | +3% real hit damage/rank (+36%) against true Slime/Ooze prey, not all liquids. | entry; tyr_progression:specialty_ooze; free ooze choice |
| **Slime Resilience** (`tyr_progression:hunter_ooze_defense`) | 8 | -2% authentic Ooze-origin direct damage/rank (-16%), not generic fall damage. | adept; tyr_progression:hunter_ooze_slayer; free ooze choice |
| **Viscous Tactics** (`tyr_progression:hunter_ooze_mastery`) | 10 | +2% melee hit damage/rank (+20%) to Ooze quarry while player has a debuff truly inflicted by that ooze, not arbitrary Slowness potion. | mastery; tyr_progression:hunter_ooze_defense; free ooze choice |

### Demon specialty — demon (30 points)

**FREE choice license:** `tyr_progression:specialty_demon`; at most ONE of fourteen active. Category-specific source hold: Goety/Occultism demon classification, summoned actor/owner UUID and player-friendly checks.

| Purchased skill | Ranks | Candidate effect | Milestone/required license |
|---|---:|---|---|
| **Demon Slayer** (`tyr_progression:hunter_demon_slayer`) | 12 | +3% true Hunter direct/shot damage/rank (+36%) to source-tagged Demon subtype creatures, not all Goety/Occultism or Nether mobs. | entry; tyr_progression:specialty_demon; free demon choice |
| **Infernal Guard** (`tyr_progression:hunter_demon_defense`) | 8 | -2% real Demon-creature-origin direct hit damage/rank (-16%), source-specific. | adept; tyr_progression:hunter_demon_slayer; free demon choice |
| **Exorcist** (`tyr_progression:hunter_demon_mastery`) | 10 | +2% direct damage/rank (+20%) to genuine HOSTILE summoned Demon quarry with source summon owner, not friendly owned servants or every undead. | mastery; tyr_progression:hunter_demon_defense; free demon choice |

## Critical source risks and owner review

**Missing Fae:** the earlier owner Hunter-category concept included Fae among examples. The current `dev/progression` Hunter source has fourteen categories—Undead, Illager, Dragon, Void, Piglin, Sculk, Aquatic, Construct, Beast, Arthropod, Plant, Elemental, Ooze, Demon—and **no Fae**. This design **deliberately preserves** those 14 existing choice IDs and no fictitious `specialty_fae`. Ask owner to decide whether to add Fae as a fifteenth only after reviewing actual Feywild/other source entity roster and future license/respec semantics. Do NOT label all witches/forest mobs Fae by appearance.

**Overlaps, ownership, and bosses:** Ender Dragon could be Dragon and Void; Hoglins could be Piglin and Beast; Warden could be Sculk and another taxonomy; Creeper could count as Plant; tamed wolves/golems may belong to owners. Exactly ONE free chosen prey group decides bonuses, and a given damage event gets a single eligible favored-category application. Boss is not a prey category; no automatic 'all bosses' classification, no bypass of Chaos Guardian encounter mechanics. Exact mapping is an owner/source-backed registry decision and must be tested against the pinned creature census from PR #97 (which counted 1,393 registered entity IDs, not 1,393 hostile targets).

**Bow/Crossbow origin:** already existing skill IDs have real bow/crossbow source restrictions. Validate original launcher, ammunition, shooter UUID, real projectile damage and equipped weapon at firing, not the current hand at damage or weapon model name; ensure no bonus to duplicate arrows, nearby explosive bolts or unowned projectile hits. Fast Hands bow draws and crossbow reloads are separate. Crossbow ammo conservation from older owner goals can be added through a separately verified generic debit handler, but is not silently invented or assumed to be one of the existing 57 IDs.

**Training reprice:** Source `equipment_care` currently uses `gear_durability` with `slot: all`, and its old 3-rank escalating purchase differs from new 8 one-point ranks. The proposed repair node must be restricted to actual worn armor and active mainhand equipment or explicitly approved alternative, not backpacks or inventory mass-repair. `fortified_training` must coexist with common armor and gear caps. The live Hunter's 17 paid skills historically cost 238 under its old escalating formula; these *draft* 100 ranks do **not** change runtime or automatically import old save points.

**XP economy:** one verified eligible kill receipt can award qualified activities without duplicate callback credit, while another independent source can also award legitimate MXP (e.g. a separately paid spell source) under #475. Legacy native maxHP/4 and favored doubled awards are **recalibration targets only**. 1,000 base MXP per first-level universal point is the anchor, not a claim one mob kill should be a fixed 1,000. Do not tie MXP ownership to a single Path, award it to Newbie, or turn Bountiful currency into MXP.

**Mutagenics:** source native mutation target is 720000 ticks/10 hours; source effect blacklist, per-effect amplifier, one active mutation and hunger drain require gameplay QA and owner removal/cure decisions. Mutagenic Brew is not a skill purchase or a means to bypass category exclusivity. No new active signs or spells added.

## Shared Progression implementation crosswalk

| Gameplay domain | Required generic engine adapter / proof |
|---|---|
| Core eligibility | Real Trial of Venom, life-stage/current Path, exclusive human disciplines; free 14 choices and owned rank refund/accounting |
| Weapon skills | Genuine held melee tool and source ID, arrow shooter/launch item snapshot, source-specific `damage_bonus`, modifier caps |
| Tracking | Source taxonomy readout, one marked target ID, bounded private outline; exact legal target class and safe range, no unbounded server scans |
| Equipment Care | Damageable equipped mainhand/armor slot selector, verified repair scheduler, does not write another player's item or fix broken gear |
| Mutagen and XP | Actual poison+regen consumption, persistent 10h mutations, successful kill owner, path-specific MXP mode/rate, anti-farm and independent same-event award categories |
| Prey taxonomy | Machine-readable exact entity tags, attack target and source, one selected free specialty, boss and tamed/owned exclusions, correct action stage |
| Bounty shop | Public Bountiful `tmm_hunting` coins only, shared Quest Shop team currency; outside Hunter skill-tree runtime ownership |

**One engine rule:** Hunter Path authors can produce data/configuration, tags, localization, non-executable design fixtures and docs. Any Java, mixins, API/handler changes and executable tests belong to shared `compat/progression` on `feature/progression-*`. Reconcile with `dev/progression` by one integration writer and preserve owner-authorized QA gates.

## Acceptance tests to run only after source/owner approval

1. Exactly 57 paid IDs, 14 unchanged zero-cost specialty choice IDs, 2 training alternatives, 13 shared. Each legal selected build has 62 + 8 + 30 = 100 one-point rank purchases and only 17 purchased node identities.
2. Ordinary Hunter Trial of Venom: real Poison potion consumed during actual Regeneration and required kill milestone; wrong potion, wrong effects, Newbie or incompatible form cannot bypass. Preserve original initiation.
3. Compare live source 31 Hunter skills and old 17 paid escalating-cost entries with candidate IDs/effects and explicit reprice; do not overwrite runtime or existing saves by installing this ledger.
4. Test all 14 free specialties with real tame/pet/hostile mobs and scanned registry IDs, one at a time, and one-of-two style selection; switching requires full single-branch point cleanup and purchase deactivation.
5. Rooted knockback, Monster Resilience +5 armor, all five actual weapon specializations and Silver Hunter only where source weapon/lycan tags match; clear all bonuses outside Hunter.
6. Bow vs crossbow draws/reloads, source projectile damage and arrows, offhand weapon, loaded fireworks, Quick Charge, modded bows/scythes and wrong item snapshot. Do not assume +0.2 fast_hands means zero reload.
7. Equipment Care modifies actual worn armor/mainhand and no inventory/offhand; Fortified +4 armor, armor caps with Monster Resilience and enchant/Apotheosis real-source stacking.
8. Monster Lore requires visible known quarry, Quarry Mark one actual hit/target, Tracking max five self-visible highlights within 24 blocks and safe bounded scanning; no reveals of players or protected/unloaded mobs.
9. Each 12/8/10 specialty actually improves its own prey attacks and/or native incoming attack condition, not all bosses or all mobs in the same biome. Test Dragon/Void, Piglin/Beast, Sculk/Warden, Creeper/Plant, Spider/Arthropod and Demon/Goety corner cases.
10. Undead/Illager source legacy melee/ranged and evasion IDs preserve specific weapon and actual hit identity, with rank rebalances needing balance review; no double special damage on overlapping tags.
11. Source first-accessible 1000 MXP point and growth=1.02 vs old Hunter maxHP/4 native curve; one source kill receipt, legitimate independent spell/activity awards additive, favored-prey multiplier calibrated rather than copying numbers.
12. Test General Bountiful Hunter Decree coin-only payouts for both enrolled Hunters and ordinary Normie/non-Hunters; Hunter tree has no special shop coin minting.
13. Test potion-mutagen recipe, ten hours of accumulation, non-Hunter Wither, hunger cost, blacklist, persistence, and any owner's cure/removal policy separately from purchased skill ranks.
14. PvP, multiplayer, respec, death/relog, refunds, stage gates and source-perfect tag classification. No extra Fae option until owner approves provider/source taxonomy.
15. Static design validation is **NOT** module CI, compiled engine acceptance, server boot or Prism QA; those are separately authorized.

## Evidence and open decisions

- Active Hunter source: `compat/progression/src/main/resources/data/tyr_progression/tyr_progression/paths/hunter.json` on `dev/progression` (31 skills, 14 free choices, current enrollment and mutation data). We did not edit this runtime file.
- Original design and source taxonomy: historical Hunter research PR #97 and original remaining trees #495; 14 categories audited against pinned entity registry, but current full tag maps need refresh after new mod intake.
- XP and currency: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/475.
- Public Bountiful hunting and Quest Shop economy: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/523.
- Respec and separate human disciplines: repository `AGENTS.md`, Hunter/Monk integration #271; policy before committing live data.
- Explicit owner/source holds: missing Fae category, balancing against the best specialized gear and Monk, actual archetype damage/defense bounds, weapon and projectile source implementation, new tracking HUD, exact creature taxonomy, Hunter kill-MXP calibration, mutagen cure/removal.
