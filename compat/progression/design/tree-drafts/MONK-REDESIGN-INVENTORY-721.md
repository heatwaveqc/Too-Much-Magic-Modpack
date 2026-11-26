# Monk redesign inventory — #721 — first research checkpoint

Date: 2026-11-25. Writer: Monk design research chat. Status: needs-research; not a gameplay configuration or balance approval. Source baseline dev/progression `4a027c5f0e78af0cc08bb004e1bbb4ca3688a42a`; active path blob `fbccf9f4acc23dc02d57bb4871b2bcc5eb238c25`. Cross-check integration baseline e090df93d500784a050fb7cab2b2c153fb4fcc03 had the same path blob. Minecraft 1.21.1 NeoForge; exact loader/Java/mappings to be recorded alongside pinned JAR survey. No runtime files edited.

## Governing design

Challenge entitlements outside purchased-point budget, one-time qualitative grants; full fall and fire/lava immunity. Classic approximately 100–120 points, final number pending inventory; match fully enchanted vanilla armor, tools, potions and original Monk uniqueness plus basic magic. Unlimited is a finite mastery tier, not infinite ranks; final purchased rank 1001 is transcendence; pre-capstone Classic + Unlimited = 1000. One point each rank, at most two skills per stat (one per phase). Prefer flat additive units; probabilities and ratios use additive percentage points. Every rank meaningful (10% guideline; reference baseline TBD). No arbitrary equipment-era ceilings on Unlimited. Configurable Classic stop / Dragon transition; exact mode and challenge requirements owner decision. Keep strict barehand Wither gate for gliding. Passive entitlements follow existing HUD-invisible policy #532. No new engine behavior in path PR.

## Evidence sources

- Historical 25-ability catalog: [archived parity design](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/main/docs/archive/pr-100-monk-classic/docs/monk/STANDALONE-PRE-TYZS-CLASSIC-TARGET.md), its JSON manifest and original source references (do not copy upstream code/assets).
- [Current path JSON](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/4a027c5f0e78af0cc08bb004e1bbb4ca3688a42a/compat/progression/src/main/resources/data/tyr_progression/tyr_progression/paths/monk.json); local design/monk-original-21-gate-inventory-v1.json and monk-classic-coverage-v1.json; historical design numbers are NOT new owner-approved targets.
- Runtime paths relative to compat/progression/src/main/java/io/github/heatwaveqc/tyr/progression: EffectRuntime, ClassAbilities, HarvestAbilities, MechanicalEffects, UtilityEffects, NativeLootBonuses, BedrockRest, VirtualElytra; source observed in prior pass, not in-game acceptance.
- Core path JSON and #475 for shared magical stat effects; #611 Otherworld; #609/#610 food and elixir; #615 strict Wither; #717 passive QA; #271 existing gameplay acceptance. Do not duplicate repairs.
- Pinned pack files at baseline: Apotheosis 8.9.0, Apothic Attributes 2.11.0, Apothic Enchanting 1.6.4, Mahou Tsukai 1.36.31 (all 1.21.1). Verify actual installation/config separately.
- docs/mod-capabilities/mods/mahou-tsukai.md records native 5,000,000 global limit, innate tier list, Caliburn -> Morgan inheritance and upgrade costs. This is NOT attainable equipped damage evidence.

## R1 — original 25 capabilities (all mapped)

| Historical ID | Destination | Notes |
|---|---|---|
| armor | Classic armor | combine natural armor/poise |
| armor_toughness | Classic toughness | combine natural toughness/iron body |
| blindness | Challenge trial | temporary condition; night vision reward proposed |
| blink | Challenge | safe targeted blink |
| catch_arrows | Challenge | frontal arrows, hands empty |
| kiss_creeper | Challenge | pacification vs durable pet ownership clarify |
| explosion_resistance | Classic defense | independent blast mitigation |
| feather_fall | Challenge | full fall immunity, supersedes partial reduction and safe-fall ranks |
| fly | Challenge | Momentum Glide, strict Wither gate; historical Dragon gate superseded |
| glass_touch | Challenge | narrow early glass harvest, preserve until universal Silk Touch |
| potion_immunity | Challenge | ordinary harmful effects, explicit boss/system exceptions TBD |
| health | Classic health | combine vitality |
| hunger | Classic exhaustion | quantitative |
| jump | Classic leap | jump only; immunity makes fall-distance bonus redundant |
| mining | Challenge + Classic speed | tier entitlement separate from mining speed |
| fire_resistance | Challenge | full fire immunity replaces partial effect |
| lava_protection | Challenge | full lava immunity in same grant; no contextual partial effect |
| swift | Classic movement | preserve sprint distinction without redundant skills |
| step_up | Challenge | fixed step assist, crouch escape |
| strength | Classic damage | one stat owner |
| tame_animals | Challenge | calm/breed; actual taming semantics separate |
| water_walking | Challenge | crouch/dive escape |
| water_breathing_partial | Challenge | consolidate with full breathing (intermediate optional free stage TBD) |
| water_breathing_full | Challenge | unlimited air |
| withering_stare | Challenge | existing undead gaze; quantitative scaling only if approved |

Current additions: reserved fist/armor lock initiation, Treasure Hand mode, Bedrock Rest, Otherworld Sight/Iesnium, Elixir binding, vein/radial mining unlocks. Night vision is new. Owner decision (2026-11-25): non-harvesting challenges follow the original relative order; completing the previous challenge activates the next. Block-breaking challenges are removed from that sequence and form an independent, semi-dynamically generated harvest progression. When a challenge needs the very capability it awards, activate a hidden trial entitlement upon qualifying for that challenge, then record its permanent entitlement on completion. Keep the existing interaction-based proof where practical; do not replace successful creeper pacification or bedrock sleep with unrelated tasks merely to avoid circularity. See ordered challenge contract below. Historical original criteria in monk-original-21-gate-inventory-v1.json; do not reintroduce mandatory lethal trials without approval. Current 16 milestones include 9 fixed 1000 MXP rewards; audit exact reward economy (challenge spendable points prohibited; owner decision on XP itself); ordinary activity XP not changed.

## Ordered challenge contract — owner approved 2026-11-25

- The original non-harvest challenge progression is a visible ordered sequence, independent of purchased ranks and the separate harvesting path. Preserve the original relative order; place new non-harvest challenges after inventory review. Completing the preceding challenge is required before the next can progress; any additional phase/boss prerequisites are explicitly declared. Do not retroactively credit out-of-order actions unless separately approved. Exact sequence and criteria remain to be inventoried and approved.
- Lifecycle: locked → active/eligible (grant hidden trial access only where the qualifying action needs it) → complete (persist earned entitlement once). Trial access is neither a purchased rank nor a completion receipt. It must survive normal save/reload and cannot leak to an unrelated Path or prematurely unlock subsequent challenges. Preserve normal eligibility, server authority and protection checks. If a challenge needs no trial ability, do not grant the reward early.
- Verified source dependency: EffectRuntime.animalFriendship records animal_petted only after active animal_friendship calms/breeds; ClassAbilities.entityInteract records creeper_kissed only when creeper_peace is unlocked; BedrockRest.canStart requires bedrock_rest before successful sleep emits bedrock_rest_completed. Blindness Ordeal needs independently available trial mode and must not be cleared by Purification while active. Strict barehand Wither victory does not require gliding. Owner gameplay observation: five-stone challenge registered before correct-for-drops harvesting was available; preserve this distinction.
- For those three self-dependent challenges, prefer the existing real deed with hidden trial access: successful animal interaction, successful barehand creeper pacification, successful bedrock rest. Exact animal distinctness/counts and ordering still under design. On completion retain the earned capability; no duplicate rewards, rank cost or investment credit.
- HUD homage to original Monk: current challenge text in bottom-right, with progress/count when applicable (e.g. "Break stone barehanded — 3/5"). Only show active Monk's current challenge, use localized text and server-synchronized personal progress, advance on completion, avoid overlapping other HUD elements, clear when sequence complete or Path not active. HUD display details such as optional hiding and completion notification are proposals, not owner-approved requirements.
- Dynamic harvesting: independent material capability graph, not part of the original challenge chain. Discover native tool rules, expose separate progress, preserve prerequisite edges, and allow incomparable branches or explicit overrides. Do not give the next harvest eligibility as trial access: break-without-drops is the qualifying deed.
- Engine handoff: reusable ordered challenge prerequisites, separate trial/permanent entitlement evaluation and persistence, and current-challenge HUD/sync support belong in a separately scoped Progression engine PR if absent. Monk definitions and localization remain declarative. Inspect existing milestone prerequisite/metadata/sync mechanisms before proposing new handlers; this is not a code-ready claim.
- Acceptance (future A/B/C): no out-of-order progress, exactly one next challenge, hidden trial access only during qualified challenge, real interaction completes it and grants durable power, duplicate actions do not grant twice, no drop before harvest entitlement, correct progress after reconnect/death, path change cleanup, HUD localization/position/multiplayer isolation, no impact on rank budget. No runtime verification or build authorized.

## Active skills crosswalk (generated from exact JSON)

72 skills; 805 ranks; 996 paid points. All active IDs accounted for once below. Categories are preliminary destinations, not new ranks. Mixed effects require decomposition; no silent loss of behavior (e.g. Paragon armor and Elemental Tempering blast portion).

### Challenge entitlement (provisional mapping)

| Active ID | Ranks | Total paid points | Configured effects (raw units) |
|---|---:|---:|---|
| reserved_fist | 1 | 0 | reserved_fist |
| armor_lock | 1 | 0 | armor_lock |
| hand_harvest | 3 | 0 | harvest_tier; #minecraft:mineable/pickaxe; tier 3 / harvest_tier; #minecraft:mineable/axe; tier 3 / harvest_tier; #minecraft:mineable/shovel; tier 3 |
| fire_discipline | 1 | 0 | damage_multiplier; #minecraft:is_fire; 0.5/rank |
| feather_fall | 1 | 0 | damage_multiplier; minecraft:fall; 0.8/rank |
| lava_protection | 1 | 0 | lava_protection |
| animal_friendship | 1 | 0 | animal_friendship; * |
| catch_arrows | 1 | 0 | catch_arrows |
| blink | 1 | 0 | blink; {"range":"16","cooldown_ticks":"40"} |
| breath_meditation | 2 | 0 | water_breathing |
| purification | 1 | 0 | purification |
| creeper_kiss | 1 | 0 | creeper_peace |
| treasure_hand | 1 | 0 | treasure_hand |
| glass_touch | 1 | 0 | glass_touch |
| withering_stare | 1 | 0 | withering_stare; #tyr_progression:undead; {"range":"20"} |
| step_assist | 1 | 0 | attribute; minecraft:step_height; 0.5/rank; add_value; {"requires_not_sneaking":"true"} |
| water_walking | 1 | 0 | water_walk |
| momentum_glide | 1 | 0 | momentum_glide; {"requires_milestone":"tyr_progression:trial_wither_victory"} |
| optional_blindness_ordeal | 1 | 0 | blindness_ordeal; {"completion_path":"tyr_progression:monk","completion_milestone":"tyr_progression:blind_ordeal_victory","kill_event":"tyr_progression:blind_hostile_kill"} |
| bedrock_rest | 1 | 0 | bedrock_rest |
| otherworld_sight | 1 | 1 | otherworld_sight; tier 2 |
| iesnium_palm | 1 | 1 | otherworld_harvest; occultism:iesnium_ore_natural; tier 2 |
| elixir_of_life | 1 | 1 | personal_elixir; #tyr_progression:elixir_candidates; {"fixed_mxp":"10","saturation":"6","effects":"minecraft:regeneration,1,100;minecraft:absorption,0,2400","restricted_effects":"#tyr_progression:elixir_restricted_effects","protected_effects":"#tyr_progression:elixir_protected_effects","reset_on_path_reset":"true","stack_meditative":"false"} |

### Mixed: free unlock plus paid quantitative extension

| Active ID | Ranks | Total paid points | Configured effects (raw units) |
|---|---:|---:|---|
| resonant_palm | 5 | 5 | vein_mining; #c:ores; 3/rank; {"max_blocks":"15","sneak_only":"false"} |
| shattering_palm | 3 | 3 | radial_mining; #tyr_progression:radial_mineable; 1/rank; {"max_radius":"3","hardness_multiplier":"3","sneak_only":"false"} |

### Fist damage (consolidate)

| Active ID | Ranks | Total paid points | Configured effects (raw units) |
|---|---:|---:|---|
| iron_palm | 8 | 0 | melee_bonus; 2/rank |
| empty_hand_force | 50 | 50 | attribute; minecraft:attack_damage; 0.175/rank; add_value |
| paragon_force | 10 | 20 | attribute; minecraft:attack_damage; 0.02/rank; add_multiplied_base |
| empty_hand_paragon | 1 | 5 | attribute; minecraft:attack_damage; 5/rank; add_value / attribute; minecraft:armor; 3/rank; add_value |

### Defense and recovery

| Active ID | Ranks | Total paid points | Configured effects (raw units) |
|---|---:|---:|---|
| natural_armor | 5 | 0 | attribute; minecraft:armor; 4/rank; add_value |
| natural_toughness | 2 | 0 | attribute; minecraft:armor_toughness; 2/rank |
| vitality | 1 | 0 | attribute; minecraft:max_health; 6/rank |
| natural_poise | 40 | 40 | attribute; minecraft:armor; 0.25/rank; add_value |
| iron_body | 40 | 40 | attribute; minecraft:armor_toughness; 0.25/rank; add_value |
| inner_vitality | 40 | 40 | attribute; minecraft:max_health; 0.5/rank; add_value |
| quick_recovery | 20 | 40 | delayed_regeneration; 0.1/rank; {"interval_ticks":"20","after_damage_ticks":"80"} |
| blast_discipline | 1 | 0 | damage_multiplier; #minecraft:is_explosion; 0.5/rank |
| elemental_tempering | 20 | 20 | damage_multiplier; #minecraft:is_fire; 0.00625/rank / damage_multiplier; #minecraft:is_explosion; 0.00625/rank |
| hunger_discipline | 1 | 0 | hunger_discipline |
| hunger_mastery | 20 | 20 | hunger_efficiency; 0.02/rank |

### Mobility

| Active ID | Ranks | Total paid points | Configured effects (raw units) |
|---|---:|---:|---|
| focus | 5 | 5 | attribute; minecraft:movement_speed; 0.05/rank; add_multiplied_base |
| monk_leap | 1 | 0 | monk_leap; {"factor":"1.2"} |
| swift_step | 2 | 0 | attribute; minecraft:movement_speed; 0.12/rank; add_multiplied_base / attribute; minecraft:movement_speed; 0.12/rank; add_multiplied_base; {"requires_sprinting":"true"} |
| body_speed | 30 | 30 | attribute; minecraft:movement_speed; 0.0025/rank; add_value |
| balanced_landing | 20 | 20 | fall_reduction; 0.5/rank |
| evasion | 20 | 20 | evasion; #tyr_progression:monk_training_hostiles; 0.005/rank |
| glide_control | 20 | 20 | glide_control_bonus; 0.005/rank; {"requires_milestone":"tyr_progression:trial_wither_victory"} |
| momentum_step | 20 | 20 | attribute; minecraft:movement_speed; 0.01/rank; add_multiplied_base; {"requires_sprinting":"true"} |
| graceful_step | 20 | 20 | attribute; minecraft:knockback_resistance; 0.015/rank; add_value |

### Combat modifiers

| Active ID | Ranks | Total paid points | Configured effects (raw units) |
|---|---:|---:|---|
| flowing_counter | 20 | 40 | counter_attack; #tyr_progression:monk_training_hostiles; 0.2/rank; {"max_damage":"4","cooldown_ticks":"40","chance":"1","delivery":"melee"} |
| empty_hand_precision | 20 | 40 | critical_hit; *; 0.005/rank; {"multiplier":"1.5","delivery":"melee"} |
| fist_tempo | 20 | 20 | attribute; minecraft:attack_speed; 0.005/rank; add_multiplied_base |
| pressure_points | 5 | 5 | status_on_hit; *; 0.01/rank; {"effect":"minecraft:slowness","duration_ticks":"40","amplifier":"0","chance":"1","delivery":"melee"} |
| piercing_palm | 20 | 20 | attribute; apothic_attributes:armor_shred; 0.01/rank; add_value |

### Gathering

| Active ID | Ranks | Total paid points | Configured effects (raw units) |
|---|---:|---:|---|
| hand_mining | 5 | 0 | break_speed; *; 2.4/rank |
| barehand_efficiency | 30 | 30 | break_speed; *; 0.05/rank |
| looting_palm | 20 | 20 | ordinary_drop_bonus; #tyr_progression:monk_training_hostiles; 0.01/rank |
| toolless_reach | 20 | 20 | attribute; minecraft:block_interaction_range; 0.075/rank; add_value |
| worldly_cultivation | 20 | 20 | crop_bonus; minecraft:wheat; 0.01/rank / crop_bonus; minecraft:carrots; 0.01/rank / crop_bonus; minecraft:potatoes; 0.01/rank / crop_bonus; minecraft:beetroots; 0.01/rank |
| fortunate_hands | 10 | 20 | virtual_fortune; #minecraft:mineable/pickaxe; 0.3/rank; {"max_total":"3","mode_key":"tyr_progression:treasure_mode","mode_value":"fortune"} |

### Magic and spiritual

| Active ID | Ranks | Total paid points | Configured effects (raw units) |
|---|---:|---:|---|
| enlightenment | 18 | 18 | xp_growth_reduction; 0.001/rank |
| inner_reservoir | 30 | 60 | attribute; ars_nouveau:ars_nouveau.perk.max_mana; 5/rank; add_value / attribute; irons_spellbooks:max_mana; 5/rank; add_value / attribute; psi:total_psi; 50/rank; add_value |
| inner_wellspring | 30 | 60 | attribute; ars_nouveau:ars_nouveau.perk.mana_regen; 0.2/rank; add_value / attribute; irons_spellbooks:mana_regen; 0.1/rank; add_value / attribute; psi:regen; 1/rank; add_value / attribute; forbidden_arcanus:generic.aureal_regeneration; 0.1/rank; add_value |
| inner_thrift | 20 | 40 | attribute; goety:soul_discount; 0.003/rank; add_value / attribute; neovitae:sigil_cost_reduction; 40/rank; add_value |
| inner_potency | 20 | 40 | attribute; ars_nouveau:ars_nouveau.perk.spell_damage; 0.2/rank; add_value / attribute; irons_spellbooks:spell_power; 0.05/rank; add_value / attribute; goety:spell_potency; 2/rank; add_value / attribute; eidolon_repraised:magic_power; 0.05/rank; add_value |
| arcane_attunement | 10 | 20 | attribute; irons_spellbooks:cast_time_reduction; 0.005/rank; add_value / attribute; irons_spellbooks:cooldown_reduction; 0.005/rank; add_value |
| inner_fortune | 10 | 10 | attribute; minecraft:luck; 1/rank; add_value |
| spellward | 16 | 32 | damage_multiplier; minecraft:magic; 0.01/rank |
| alchemical_ward | 10 | 20 | damage_multiplier; minecraft:indirect_magic; 0.01/rank |
| withering_ward | 5 | 10 | damage_multiplier; minecraft:wither; 0.01/rank |
| dragonbreath_ward | 5 | 10 | damage_multiplier; minecraft:dragon_breath; 0.01/rank |
| meditative_elixir | 10 | 20 | beneficial_duration_bonus; *; 0.02/rank |

## R2 — candidate stat inventory (not allocation)

C = Classic candidate, U = Unlimited candidate, Q = challenge. Exact registry IDs for new rows and endpoints pending binary/registry audit.

| Family | Canonical stat or capability | Phase | Vanilla comparator / modded evidence target |
|---|---|---|---|
| combat | fist damage (HP/hit), attack speed (attacks/sec) | C/U | Netherite Sharpness V + Strength II; Morgan actual cap, enchants, affixes, gems |
| combat | critical chance and multiplier (separate stats) | U | Apothic Attributes |
| combat | armor shred, armor pierce (separate stats) | U | Apothic Attributes native units |
| combat | sweeping damage/radius, attack knockback, entity reach | C/U | Sweeping Edge III, Knockback II, native attribute limits |
| combat | retaliation damage, attack status chance | C/U or U | Thorns III; active counter, pressure points |
| combat | life steal, overheal, current HP damage, execute, fire/cold damage | U candidates | audit actual Apothic registry and affixes, decide distinct vs duplicate |
| defense | armor points, toughness, max HP, knockback resistance | C/U | full netherite 20 armor/12 toughness/0.4 knockback; health/absorption effects; god-tier gear |
| defense | general damage reduction, explosion and projectile reduction | C/U | Protection IV stack, Resistance, specialized protections (compare attainable sets rather than sum mutually exclusive enchants) |
| defense | fire/lava immunity, fall immunity, harmful effect cleansing | Q | owner-approved full immunity; no paid resistance to same source |
| defense | exact magic, indirect magic, wither, dragon breath damage reduction | C/U candidates | compare distinct damage types, avoid double protection assumptions |
| defense | regeneration HP/sec, exhaustion efficiency, healing received, evasion | C/U | regeneration potion/food, modded healing/dodge |
| movement | walk/sprint speed, jump strength, swim speed, submerged mining, soul terrain speed | C/U | Swiftness II, Jump Boost II, Depth Strider III, Aqua Affinity, Soul Speed III |
| movement | step height, water walking, underwater breathing, night vision, glide access, blink | Q | fixed capability; no repeat tier purchases |
| movement | glide propulsion, blink range/cooldown | U candidates | measurable extensions; flight safety and collision constraints |
| gathering | harvest capability | Q | dynamic tags/tool graph; independent of speed |
| gathering | mining speed, Fortune integer level, Looting integer level, block reach | C/U | Efficiency V/Haste II, Fortune III/Looting III, modded maxima |
| gathering | Silk Touch, glass touch, area/vein access | Q | mutually exclusive fortune/silk mode; glass precedence |
| gathering | vein block count, area radius, crop yield | U candidates | discrete meaningful increments, preserve protections |
| magic | capacity, regeneration, efficiency, potency, cast time, cooldown | C/U | per-provider raw units, not arbitrary universal percentages |
| magic | Otherworld sight, Iesnium access, bound Elixir | Q | challenge only, existing provider dependencies |
| spiritual | beneficial effect duration, Luck, Enlightenment | C/U or U | native durations/loot; growth factor baseline and invested point gates |
| spiritual | transcendence | final rank | owner choice after full inventory |

Additional vanilla candidates requiring decision: Frost Walker (overlap with water walking), invisibility, fire aspect, smite/bane conditional damage (consolidate or omit), absorption, trident/bow equivalence (not automatically granted to fist), Swift Sneak, mining underwater/airborne penalties. Durability enchants Mending/Unbreaking irrelevant to intrinsic body. Distinguish loadouts that cannot coexist; compare attainable optimized builds, not impossible simultaneous enchant stacks.

### Existing magic attribute inventory

Monk and Core overlap on these supported configured identifiers; registry existence, units and limits must be checked against actual pinned JARs. Reservoir: ars_nouveau:ars_nouveau.perk.max_mana, irons_spellbooks:max_mana, psi:total_psi. Wellspring: ars_nouveau:ars_nouveau.perk.mana_regen, irons_spellbooks:mana_regen, psi:regen, forbidden_arcanus:generic.aureal_regeneration. Thrift: goety:soul_discount, neovitae:sigil_cost_reduction. Potency: ars_nouveau:ars_nouveau.perk.spell_damage, irons_spellbooks:spell_power, goety:spell_potency, eidolon_repraised:magic_power. Cast/cooldown: irons_spellbooks:cast_time_reduction, irons_spellbooks:cooldown_reduction. Audit absent attribute providers, Mahou personal mana and other nonattribute systems; external storage is not personal capacity. Keep Core globally separate but count its effects when benchmarking total attained power.

### Benchmark methodology / outstanding offline survey

For each stat record exact registry ID, additive vs native multiplicative unit, vanilla endpoint, installed-provider range/clamp, actual attainable optimized gear including sockets, mutually exclusive enchant/affix constraints, engine handler and observed output. Capture exact mod/version/hash and config provenance, distinguish native limit from attainable item. Record Morgan innate/global cap, possible enchant/affix/gem interactions, critical chance/damage, armor penetration, and damage-event caps; do not assume 5M cap attainable. Current source uses 1M damage clamp in EffectRuntime and native attributes can clamp or skip unregistered IDs; test and route extensions to generic engine. Current normal max fist estimate 33.7 before external modifiers (1 base +8.75 +5, multiplied by 1.2, then +16 event bonus); conditional; #717 means actual application not confirmed. Compare against fully charged equivalent attack, per-hit, DPS and mitigation separately. Research needed before numeric allocation.

## R3 — harvesting discovery feasibility (initial hypothesis)

### Owner clarification — independent material-based harvesting, 2026-11-25

Use actual tool material harvest capability rather than a numerical level. NeoForge 1.21.1 [tools documentation](https://github.com/neoforged/Documentation/blob/main/versioned_docs/version-1.21.1/items/tools.md) documents Tier#getIncorrectBlocksForDrops, incorrect_for_*_tool tags, and DataComponents.TOOL custom rules; the native correct-for-drops behavior is authoritative, not just a tag name. The existing engine has only three positive vanilla minimum-tier checks. At tag reload, survey representative registered standard tools, group equivalent harvest capabilities by tool family (pickaxe/axe/shovel/etc.), derive prerequisite edges from actual strict inclusion, and optionally use the material ore/storage block harvest requirement to suggest a predecessor (not an authoritative rule; some materials are crafted, dropped, or gated by bespoke mechanics). Config overrides for ambiguous or incomparable tiers, no eligible challenge blocks, special adapters, and dimension/phase locks. Proposed trial: after predecessor entitlement, break five blocks newly eligible at the candidate material capability while barehanded; count genuine removal even if no drops; award the fixed native harvesting equivalent, not speed/damage/durability or item-specific powers. Owner clarification: the five-log introductory trial grants fixed baseline barehand mining speed equivalent to wooden tools on their applicable blocks (no new correct-for-drops entitlement). Five barehand stone breaks then grant wooden-tier harvesting eligibility; stone is precisely a wooden-pickaxe target. Subsequent material challenges follow the discovered correct-for-drops capability graph: e.g. five stone-tier-required blocks (iron/copper ore) after wood unlock grant stone-tier eligibility, then five iron-tier-required blocks after stone unlock grant iron-tier eligibility. The one-off log speed grant is an explicit exception to qualitative-only challenge rewards, outside the purchased rank budget; Classic Hand Mining adds further speed from that baseline, not a duplicate grant. No automatic speed upgrades attached to subsequent tiers. Exact native speed handling by tool family and block state remains to be verified. Identical harvest capability (e.g. diamond/netherite where rules coincide) needs no duplicate challenge. Do not infer every item named X has an independent tier; use actual capability and respect tool family and custom context. Do not create a circular gate from a mod's own ore if it requires the tier being unlocked. Persist stable entitlement IDs, record migration and fail closed on invalid rules. Preserve vanilla no-drop behavior until award. Exact installed tool registry and API fixture audit still pending; no code or gameplay proof of generic discovery yet.


Source EffectRuntime.requiredTier currently tests NEEDS_DIAMOND_TOOL, NEEDS_IRON_TOOL, NEEDS_STONE_TOOL and returns 0 otherwise. HarvestAbilities and EffectRuntime compare rank against required tier; they do NOT dynamically discover modern NeoForge tool rules. Vanilla stone requires a wooden pickaxe despite not having a NEEDS_STONE_TOOL requirement. Distinguish mineable tool family and correct-for-drops behavior from minimum tier tag. In 1.21.1 tool rule components / incorrect-for-drops tags and tool-specific overrides require pinned API verification. Proposed capability fingerprints: evaluate representative tool stacks against eligible block states, collapse equal sets (diamond/netherite), derive subset edges, retain incomparable branches and require predecessor(s) per policy; no ordinal assumptions. Config overrides for mod-special tools, protected blocks or context-dependent checks; never grant special harvest mechanics simply because an item exists. Candidate challenge counts five genuine barehand destroys of qualifying next-tier blocks after predecessor unlocked, even when no loot drops; no cancelled/claimed/spoofed breaks. Stable capability IDs persisted across reload, explicit migration on mod/tag changes; bounded discovery at reload not per tick. Normal harvest eligibility, loot context, Fortune/Silk, XP credit and radial/vein harvest must agree. Source/JAR and offline fixtures needed; engine PR if required.

## Pending decisions and checks

- Owner: Classic exact budget; challenge predicates/phase boundaries; whether fixed challenge MXP remains; Classic stop policy, toggle reversibility and excess XP handling; capstone reward; benchmark loadouts and per-rank meaningful baseline. No numerical allocations ratified.
- Research next: pinned vanilla/NeoForge tool rules; full Apothic registered attributes, enchantment maxima and gem/affix combinations; pinned magical provider attributes; native cap and event semantics. Acquire installed JAR/config/registry exports through authorized QA/Codex if inaccessible.
- Future A evidence: crosswalk unique IDs, exact registries/units/maxima, harvest graph fixtures (stone, metal, obsidian, modded higher and incomparable tiers, identical tiers, no-drop). Future B/C: new player unlock/no premature drops, repeat/duplicate/reload, immunity by source and exception, challenge point neutrality, Classic stop/resume, 1001 budget and endgame loadout comparison. No runtime QA yet; no new build authorization. Existing #271/#717 obligations unchanged.
- This is an independent docs-only research boundary; no executable changes. No code-ready slices or new pending runtime acceptance until design is approved; canonical work ledger #431 links #721.
