# Forest Dragon — complete 100-point skill tree

**Too Much Magic · November 22, 2026 · DESIGN ONLY / NOT INSTALLED / NUMERIC BALANCE FOR REVIEW**

Stacked branch: `feature/forest-dragon-100pt-complete-tree-20261122` → Cave PR #530 → Lich #526 → Werewolf #520 → Vampire #518 → Sea Dragon #514 → base #495. Only the standalone design MD and shared design JSON/MD are changed. No runtime JSON, Java, KubeJS, mods, Packwiz, quests, source integrations, or playable QA.

## Role and native contract

The Forest Dragon is a **living woodland protector**: poisonous native attacks, daylight-fed regeneration, bounded plant and animal cultivation and personal Nature/Fae support magic. It should feel meaningfully different from Sea Dragon's luck/water specialization, Cave Dragon's fire and excavation, and Werewolf's combat sustain.

**Free provider powers, not purchased:** Dragon Survival Forest species, native growth/body/wing/Flight Spin, axe-claw harvest tier, natural fall and thorn resistance, native poisonous Forest Breath, native Sun Breath plant behavior, source spike, source native Haste/Hunter/Light in the Dark, mana regeneration, original cooldowns, and darkness Stress/accelerated hunger. The 1.21.1 skill system is datapack-defined; historical developer wiki names are clues, not verified binary IDs in pinned v2.0.74.

**TMM diet #444 is separate and supersedes old source diet:** Forest has a strict plant-based preferred category (fruits, vegetables, grains, nuts, fungi and plant meals). Raw preferred fruit/vegetable whole ingredient has 6 nutrition / 2.0 saturation modifier. Default raw/cooked/charred nutrition and saturation fractions 1.0/0.5/0.25, with mixed dishes configurable and magical exception foods maintained. No food handlers or passive nutrition buffs in purchased tree.

**Progression integration:** One purchased rank costs exactly 1 advancement point, 100 total. Shared Core skills are independently owned; Newbie MXP prohibition remains. A legal Forest Dracolich has exactly one Forest tree and one shared Lich tree, not a second dragon species or second Lich. All purchased skills are passives, enhancements to legitimate native skills, or reactions to player-performed game actions—not new active buttons or a fresh mana resource.

## Six-branch mastery allocation

| Branch | Skills | One-point ranks |
|---|---:|---:|
| Draconic Body & Personal Sorcery | 5 | 20 |
| Verdant Vitality | 4 | 19 |
| Thorn & Root | 5 | 18 |
| Fertility & Bounty | 5 | 19 |
| Fae Affinity | 4 | 14 |
| Light & Shadow | 3 | 10 |
| **Total** | **26** | **100** |

All 20 previously drafted skill IDs are retained and rebalanced; six new concepts distribute useful ranks rather than increasing rank price or cloning old Tyz tier costs. All numeric values below are candidacy targets requiring source and balance review.

## Full purchased tree

### Draconic Body & Personal Sorcery — 20 points

| Skill | Ranks | Proposed effect | Stage & prerequisite | Source or handler HOLD |
|---|---:|---|---|---|
| **Draconic Reservoir** (`tyr_progression:forest_dragon_draconic_reservoir`) | 6 | +10% per rank (max +60%) eligible personal spell-resource capacity, including native Dragon Survival mana only if source modifier supports it; no machine, grid, or world storage. | entry | Personal mana/cap adapter and Core stacking |
| **Draconic Potency** (`tyr_progression:forest_dragon_draconic_potency`) | 5 | +4% per rank (max +20%) personally cast verified spell potency, additive with other magic modifiers; exclude world rituals and passive machines. | entry | Real personal cast IDs, source spell output and stacking |
| **Draconic Scales** (`tyr_progression:forest_dragon_draconic_scales`) | 3 | +1 natural armor per rank (max +3) only in verified Forest Dragon form; normal native body armor and fall/thorn defenses remain innate. | entry | Forest shape/armor attribute cleanup |
| **Draconic Claws** (`tyr_progression:forest_dragon_draconic_claws`) | 3 | +1 native physical claw/bite melee damage per rank (max +3), excluding held swords, breath and poison ticks. | entry | Forest native melee/held-tool identity |
| **Draconic Talons** (`tyr_progression:forest_dragon_draconic_talons`) | 3 | +15% legal Forest axe-claw chop speed per rank (max +45%), only on wood/plant blocks already harvestable by native claws; no treecapitator/ore tier bypass. | entry | Native axe claw, claims, harvest tier, tool precedence |

### Verdant Vitality — 19 points

| Skill | Ranks | Proposed effect | Stage & prerequisite | Source or handler HOLD |
|---|---:|---|---|---|
| **Green Recovery** (`tyr_progression:forest_dragon_green_recovery`) | 5 | +0.15 HP/s per rank (max +0.75) while grounded in natural living vegetation during daylight, after eight seconds without hostile damage. | entry | HP regen, true plants, light and last-hit predicate |
| **Living Bark** (`tyr_progression:forest_dragon_living_bark`) | 5 | +0.5 armor toughness per rank (max +2.5) near verified natural trees in Forest form; distinct from Draconic Scales armor. | entry, after draconic_scales | Natural vegetation and armor-toughness cap |
| **Elder Grove** (`tyr_progression:forest_dragon_elder_grove`) | 4 | After a successfully paid personal Nature spell, grant self Regeneration I for one second per rank (max four seconds), 20-second player cooldown; no ability key. | mastery, after living_bark | Nature caster debit receipt and cooldown |
| **Sunfed Resilience** (`tyr_progression:forest_dragon_sunfed_resilience`) | 5 | -2% ordinary physical hit damage per rank (max -10%) only while grounded under actual direct daylight in Forest Dragon form; not in artificial light or other dimensions. | advanced, after green_recovery | Real sky light, weather/day, vulnerability exclusions |

### Thorn & Root — 18 points

| Skill | Ranks | Proposed effect | Stage & prerequisite | Source or handler HOLD |
|---|---:|---|---|---|
| **Bramble Claw** (`tyr_progression:forest_dragon_bramble_claw`) | 4 | Legitimate native Forest natural claw hits inflict Slowness I for 0.5 second per rank (max two seconds), with 40-tick cooldown per hostile target. | entry, after draconic_claws | Native claw hit vs held item, status attribution |
| **Entangling Breath** (`tyr_progression:forest_dragon_entangling_breath`) | 4 | Source-verified already-unlocked Forest Breath applies Slowness I for 0.5 second per rank (max two seconds) to hostile targets; native poison behavior stays unchanged. | advanced, after bramble_claw | Pinned native Forest Breath cast/effect identifiers |
| **Thornstorm** (`tyr_progression:forest_dragon_thornstorm`) | 3 | A native Forest Spike projectile direct hostile hit may slow at most one additional nearby hostile within 2.5 blocks for 0.5 second per rank (max 1.5 seconds), 15-second caster cooldown; no new projectile or block changes. | mastery, after entangling_breath | True spike projectile/owner and bounded AoE |
| **Venomous Strike** (`tyr_progression:forest_dragon_venomous_strike`) | 4 | +5 percentage-point chance per rank (max 20%) for real natural claw melee to apply Poison I for three seconds to legal hostile targets; no bleed-through from poison ticks or allies. | entry | Original Tyz venomous_attack source handler and source attack identity |
| **Forest Breath Mastery** (`tyr_progression:forest_dragon_forest_breath_mastery`) | 3 | +4% per rank (max +12%) real source-identified Forest Breath poison damage, after native breath has been learned; not every poison effect or Sun Breath. | advanced, after venomous_strike | Native breath caster/poison ticks and direct source attribution |

### Fertility & Bounty — 19 points

| Skill | Ranks | Proposed effect | Stage & prerequisite | Source or handler HOLD |
|---|---:|---|---|---|
| **Germination Touch** (`tyr_progression:forest_dragon_germination_touch`) | 4 | 5% per rank chance (max 20%) to refund exactly one bonemeal consumed in one successful, claim-allowed player crop-growth application; no free growth without real use. | entry | Confirmed item consumption/use and permissions |
| **Breeder's Blessing** (`tyr_progression:forest_dragon_breeders_blessing`) | 4 | After personally triggering a legitimate fed breeding pair, reduce both parents' normal subsequent breeding cooldown by 5% per rank (max 20%) subject to a floor; no extra child spawned. | advanced, after germination_touch | Two-parent breeding credit, no duplicate event, native cooldown floor |
| **Harvest Bounty** (`tyr_progression:forest_dragon_harvest_bounty`) | 4 | +5% chance per rank (max 20%) of exactly one extra ordinary edible fruit/berry for one legally harvested mature shrub/vine/fruit tree; excludes conventional field crops and hidden loot. | advanced, after germination_touch | Mature non-field fruit categories, single-use harvest receipts |
| **Green Thumb** (`tyr_progression:forest_dragon_green_thumb`) | 4 | +4 percentage-point chance per rank (max 16%) to duplicate normal edible produce from one personally harvested mature planted field crop, not shrub berries, rare loot or automation. | entry | Preserve historic Tyz green_thumb attribution; no Harvest Bounty overlap |
| **Shepherd’s Blessing** (`tyr_progression:forest_dragon_shepherds_blessing`) | 3 | +8% juvenile livestock growth rate per rank (max 24%) only for player-owned/cared-for legitimate juveniles within 12 blocks; bounded refresh, no instant maturity. | advanced, after breeders_blessing | Preserve historic shepherd_s_blessing attribution, owner and baby-age accounting |

### Fae Affinity — 14 points

| Skill | Ranks | Proposed effect | Stage & prerequisite | Source or handler HOLD |
|---|---:|---|---|---|
| **Fey Courtesy** (`tyr_progression:forest_dragon_fey_courtesy`) | 5 | +3% beneficial status duration per rank (max +15%) for personally cast, source-tagged Fae spells; no blanket mob friendliness or bonus to every Roots/Botania action. | entry | Exact Fae secondary tag and personal spell carrier |
| **Fey Conduit** (`tyr_progression:forest_dragon_fey_conduit`) | 4 | +4% per rank (max +16%) verified personal Fae spell healing or damage within six blocks of natural vegetation, additive with Core and Draconic Potency. | advanced, after fey_courtesy | Source Fae spell ID, vegetation and additive caps |
| **Grove Guardian** (`tyr_progression:forest_dragon_grove_guardian`) | 3 | After a paid personal Nature/Fae support spell, grant at most two nearby valid allies +2% physical hit mitigation per rank (max 6%) for five seconds, 25-second cooldown, no permanent aura. | mastery, after fey_conduit | Personal cast, allied player/owned companion tags and targeting |
| **Woodland Accord** (`tyr_progression:forest_dragon_woodland_accord`) | 2 | +5% max HP per rank (max +10%) to genuine player-owned, source-confirmed Fae summons only; none for wild fae, villagers or generic pets, and no ghost rank if provider lacks them. | advanced, after fey_courtesy | Actual eligible summoned fae provider and UUID ownership; HOLD if none |

### Light & Shadow — 10 points

| Skill | Ranks | Proposed effect | Stage & prerequisite | Source or handler HOLD |
|---|---:|---|---|---|
| **Gloam Endurance** (`tyr_progression:forest_dragon_gloam_endurance`) | 4 | Reduce only source-native darkness Stress *extra hunger drain* by 5% per rank (max 20%); never cancel Stress, ordinary hunger, or native Light in the Dark's identity. | entry | Native stress and extra hunger debit, earned Light in the Dark behavior |
| **Sunward Focus** (`tyr_progression:forest_dragon_sunward_focus`) | 3 | Reduce cooldown of already-earned, actual native Sun Breath by 4% per rank (max 12%) in direct daylight; do not expand growth/transmutation targets or create the breath ability. | advanced, after gloam_endurance | Pinned Sun Breath ability ID, genuine cooldown and daylight |
| **Lantern Keeper** (`tyr_progression:forest_dragon_lantern_keeper`) | 3 | +3% per rank (max +9%) personal source-verified Nature support-spell duration while sheltered in artificial block light level 11+, not a blanket magic buff or source Stress immunity. | mastery, after gloam_endurance | Separate block light/sky light and valid personal support spells |

## Cross-mod and Progression adapter boundaries

| Behavior | Shared engine / data candidate | Hard proof before charging points |
|---|---|---|
| Native eligibility and growth | Progression read-only native Dragon Survival observation and purchase gating | Authoritative Forest species, actual earned native action, native 2.0.74 skill ID and growth stage; missing/unknown fail closed |
| Body/poison/breath | Conditional attributes, damage/status effects, sourced spell/stat events | Distinguish axe-claw from held weapons, real Forest Breath poison from all poison ticks, source Spike and Sun Breath behavior; avoid double procs |
| Light/darkness | Environment predicate, resource delta interceptor, delayed regeneration | Actual Stress-induced *extra* hunger vs ordinary hunger, daylight vs artificial light, source Light in the Dark immunity and no duplicate dark suppression |
| Fertility & drops | Player-used bonemeal receipt, mature crop/berry tags, breeding timer, owned juvenile update | Field crop vs berry exclusivity; protections/claims; actual successful action; cooldowns and server CPU limits; no auto farming or arbitrary spawn |
| Fae & Nature | Source-verified personal magic carrier, typed Nature/Fae secondary tags, owner-scoped status and summons | Confirm source spell IDs and whether any legitimate player-owned fae summons exist; otherwise withhold Woodland Accord and other unsupported nodes |
| Dracolich | Central form compatibility and one Lich tree | No double death save/Dragon species, correct effect activation/deactivation after cure or switch |

**Path ownership:** only non-executable design/config in `feature/forest-*`. All Java handlers, adapters, event hooks, source-tag logic, executable tests and schema code belong to shared `compat/progression` on `feature/progression-*`. Do not sell zero-effect nodes or add separate Forest Dragon runtime module.

## Regression and QA handoff

1. Validate 26 distinct nodes and 100 one-SP rank entries with resolved prerequisites. All other draft trees unchanged; Hunter's 44 extra alternative specialty options do not add to any single player's 100-point mastery.
2. Reject human, Sea and Cave as Forest buyers; source-missing/unknown must fail closed. Validate legitimate Forest Dracolich single species plus single Lich tree.
3. Verify pinned **Dragon Survival v2.0.74** Forest Breath, Sun Breath, Spike, axe-claw, Light in the Dark and native Haste/Hunter data; no purchase grants or substitutes for native skill unlocks.
4. Validate poison vs slow targets and projectile origin: no poison-on-poison recursive triggers, no friendly/PvP misuse, no terrain changing from paid ranks.
5. Test ordinary daylight, shadow, deep cave, artificial lanterns, rain, different dimensions and actual source darkness Stress. Gloam at full four ranks reduces only extra Stress hunger by 20%; darkness remains dangerous.
6. Check natural leaf/wood axe harvest throughput vs held tools, claim rules, planted-block farming, native harvest tiers, no vein/treecapitator/ore bypass.
7. Check Green Thumb field crop vs Harvest Bounty mature fruit/shrub disjoint tags and duplicate loot protection in vanilla, Pam's, Farmer's Delight and LootJS.
8. Successful real bonemeal refund only once, never on canceled use or unaffected target; ensure lossless item consumption audit.
9. Bred pair requires food and both mature animals; parent cooldown cannot hit zero; Shepherd's blessing affects eligible young only, with bounded owner/range refresh and no growth-from-nothing loops.
10. Source Fae and Nature personal spells only; exclude passive devices, machine generation and global mod classification; test party allies and legitimate owned summon identities before enabling related purchases.
11. Food regression belongs to #444: Forest strict plant preference, normal carrot and true fruit 6/2.0 raw, cooking fractions, fruit/juice provenance, ordinary players unaffected; skills must not change food tags.
12. World persistence, respec, shared Core, cure/switch, species-exclusive effects and absence of second death-save; no duplicate refunds or invisible active powers.
13. Design/JSON checks are not Module CI, Integration Preflight, server boot, Prism package, or gameplay acceptance; those require later authorized integration.

## Provenance and remaining decisions

- Source wiki for Forest axe claws, poison breath, darkness Stress and protective abilities: https://github-wiki-see.page/m/DragonSurvivalTeam/DragonSurvival/wiki/4.-Dragon-Species
- Dragon Survival features and 1.21.1 datapack ability warning: https://github-wiki-see.page/m/DragonSurvivalTeam/DragonSurvival/wiki/5.-Dragon-features
- Dragon Survival 1.21.1 Global Update release notes for Forest/Sun Breath, source support effects and spike: https://www.curseforge.com/minecraft/mc-mods/dragons-survival/files/6270247
- TMM diet decision: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/444. Historical stock Tyz rehomes and role requirements: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/88
- Source holds before activation: exact native event and IDs in installed 2.0.74, source Stress depletion math, mature field-crop vs shrub loot disjointness, feasible fae-owned minion providers, legal breeding events and real source spell attribution. Numeric per-rank potency and environmental conditions are proposed, not owner-final.
