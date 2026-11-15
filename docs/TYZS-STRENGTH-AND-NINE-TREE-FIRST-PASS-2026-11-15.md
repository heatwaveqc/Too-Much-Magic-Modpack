# Tyz — Strength tiers and complete nine-tree first pass (2026-11-15)

> **Status: DESIGN ONLY; not activated in the Tyz modpack.** The 165 entries and eight capstones below are first-pass investment budgets, not tested game balance or functional mods. The source-of-truth machine catalog is [nine-tree-strength-first-pass-v1.json](tyzs/nine-tree-strength-first-pass-v1.json). Reuses the [nine-tree identities](TYZS-NINE-TREE-BLUEPRINT-2026-11-15.md), [stock skill audit](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/feature/tyzs-primary-tree-runtime-migration/docs/TYZS-STOCK-SKILL-MIGRATION-2026-11-15.md) and [retired-mod ability triage](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/docs/TEAM-LAPEN-SKILL-TREE-TRIAGE-2026-11-15.md).

## 1. Reusable math and terminology

| Strength tier | Additive reference bonus / rank | 5 ranks | 10 ranks | 25 ranks | 50 ranks |
|---|---:|---:|---:|---:|---:|
| **Weak** | **+5%** | +25% | +50% | +125% | +250% |
| **Middling** | **+10%** | +50% | +100% | +250% | +500% |
| **Average** | **+20%** | +100% | +200% | +500% | +1000% |
| **Strong** | **+50%** | +250% | +500% | +1250% | +2500% |
| **Extreme** | **+100%** | +500% | +1000% | +2500% | +5000% |

**Standard rank caps are 5, 10, 25, 50**. An explicit one-off capstone has one rank; Monk's 40 source-bounded Apotheosis affix bonuses follow their actual finite rarity milestones (usually two to five), not fabricated 10/25/50 ranks.

**Reference model:** for a percentage-friendly effect, `bonus = pre-skill source-appropriate reference × (tier% / 100) × purchased rank`. It is **additive**, not compound interest: a 100-mana caster with Average 5 gains +100 mana (200 total), whereas Extreme 25 gains +2,500 mana (2,600 total). Never scale from the already skill-buffed result or multiply global/class/school talents recursively.

**Important existing runtime nuance:** the four installed Core Skills pilot IDs use five levels at 20/40/60/80/100% of their prior **fixed rank-99 ceiling**. That is a correct current design milestone, **not yet proof** that each mod receives literally +20% of its natural starting mana or spell damage at each rank. Cross-mod native reference values and real adapters must be reconciled before treating the percentage-normalized model as live code.

**Non-mana conversions:** a flat heart, armor rating, damage proc, cast cost, crop drop, hunger effect, harvest speed, Glowing radius, passive regeneration, block interaction, or item durability event needs an explicit physical/transaction reference. In particular, health/armor with zero baseline needs a fixed benchmark rather than multiplying zero; proc/resistance/damage mitigation/cost savings must use independent hard caps. A tier can be a **power-budget label**, rather than a literal percentage applied to a timer or a boolean unlock.

**SP model, provisional:** 1 SP per rank unless marked otherwise; Dragon generic body armor/claw/harvest ranks are commonly **2 SP per rank**, making them less efficient than specialized combat or excavation ranks despite being available. Strong/Extreme dragon casting uses 1 SP per rank. All trees share one exponentially diminishing MXP-funded Tyz SP wallet. Node-level form/milestone gates and tier prerequisites still apply; a cap of 50 does not mean early players can instantly purchase 50 ranks.

## 2. Nine tree totals and differentiated power budgets

| Tree | Proposed purchased skill entries (including original stock candidates) | Capstones | Primary tier investment |
|---|---:|---:|---|
| **Core Skills** | 27 | 0 | Average ×5 magic foundations; Weak/Middling broad utility |
| **Cave Dragon** | 17 | 1 | Extreme ×25 personal mana; Extreme ×10 spell potency; expensive physical ranks |
| **Sea Dragon** | 18 | 1 | Extreme ×25 mana; Extreme ×10 spell potency; **Extreme ×50 Luck**, expensive physical ranks |
| **Forest Dragon** | 20 | 1 | Extreme ×25 personal mana; Extreme ×10 spell potency; expensive physical ranks |
| **Vampire** | 17 | 1 | Average/Strong blood-conditional predation and defense |
| **Werewolf** | 18 | 1 | Strong health/HP regen; Strong/Extreme nature magic |
| **Lich** | 12 | 1 | Strong/Extreme personal blood magic with nature tradeoff |
| **Hunter** | 22 | 1 | Strong high-level combat; Weak/Middling equipment maintenance |
| **Monk** | 14 | 1 | Average/Strong unarmed body, mobility and hand tools |
| **Total** | **165** | **8** | **Design options, not 164 mandatory purchases** |

## 2A. Global endgame ceiling — Monk first (owner-approved guideline)

**Monk is the apex of complete personal combat capability.** A fully maxed, legitimate Monk skill path **must be capable of defeating the real Chaos Guardian solo, with no worn equipment or weapons**. This is the long-range endgame design benchmark—not a claim that the current Monk implementation can do it. At the target cap, the Monk must have enough independently earned damage, durability/mitigation, movement, uptime and tactical answers to participate in **the actual encounter**. The Guardian must remain dangerous and its legitimate combat phases, crystals/shields and other mechanics may **not** be switched off or skipped just to make the goal easy.

For a strict initial benchmark, **no armor, Curios or other worn gear, no melee/ranged weapons, and no assistance from another player**. The Monk must not *require* powers earned from another specialist tree or a pre-damaged Guardian. Define exact treatment of food, potions, held casting implements, and other **non-worn** utility in a separate encounter acceptance contract; this guideline itself does **not** assume those are banned. Do not resurrect the rejected native Spirit Bolt to solve range: use legal Monk movement/contact and/or verified existing magic mechanics, while respecting the chosen item constraints.

The *other eight trees* are deliberately asymmetric: each may **approach** Monk in **one or two** emphasized combat capabilities, but should be **markedly inferior in most remaining combat/adventuring capabilities** at comparable progression. Their highest specialties can still **outperform Monk on that narrow metric** (Dragon spell power and capacity, Hunter against specified quarry with equipment, Werewolf health regeneration, Sea Dragon Luck). Thus Monk is the **best overall solo combatant**, not the best miner, best caster, best hunter against every tagged mob, or luckiest character.

| Tree | Allowed top-end specialty, potentially near / above Monk | Deliberate shortfalls versus maxed Monk |
|---|---|---|
| **Core Skills** | General usability across many source mods | No class-level boss-ready combat, mobility or defense package |
| **Cave Dragon** | Huge mana/spell damage; demonic magic | Much less independent gearless combat breadth, resilience and technique |
| **Sea Dragon** | Huge mana/spell damage; aquatic magic; **absurd Luck** | General terrestrial boss adaptability and full gearless combat package |
| **Forest Dragon** | Huge mana; fae magic, bonemeal, crops/breeding | Overall martial versatility, independent boss survival and mobility |
| **Vampire** | Blood-funded predation and stealth | Reduced flexibility outside blood, night and feeding constraints |
| **Werewolf** | Maximum hearts, HP regeneration, melee resilience | Fewer solutions to range, special encounter phases and general utility |
| **Lich** | Blood/necromancy magic and undead command | Nature-magic cost, physical versatility and self-sufficient no-gear offense |
| **Hunter** | Weapon combat and survival **against undead/demons/fae** | Reliance on equipment and quarry matchups; few broad magic improvements |
| **Monk** | **Broadest full-combat capability**: unarmed offense, defense, movement, endurance, mechanics | Gearless Chaos Guardian solo is the explicit acceptance target; no general superiority in all niche economic or magical statistics |

**Balance precedence:** When the earlier first-pass skill numbers conflict with this Monk apex, rebalance the *other class's aggregate package* first; do not flatten every class's signature one- or two-domain specialty, nor make Monk win purely by infinitely stacking armor/damage immunity. A difficult skill-based fight, not invulnerability, is the target. The Monk's win condition must remain feasible without equipped weapons or armor.

Stock skill entries have a **† symbol** in the detailed tables. These are *destination and target-tier proposals*, **not yet reassigned or rebalanced**. Tyz's 32 originals remain in the old four default categories until native handler, save/rank migration, actual effects, and form use-time gating are independently proven. Do not turn this conceptual grid into active JSON.

## 3. Core Skills — `tmm_magic_core`

**Identity:** Universal Core Skills and source-backed stock rehomes. **Current intended authority:** universal. 

### Universal magic fundamentals

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Reservoir I | Average **+20%** | **5** | 1 | Personal maximum mana/resources. Initial four runtime preview nodes preserve their original absolute rank-5 ceilings until per-mod base normalization is source-verified. |
| Wellspring I | Average **+20%** | **5** | 1 | Personal magical regeneration; dragon forms may purchase this UNIVERSAL option, but have no dragon-exclusive magic regen nodes. |
| Thrift I | Average **+20%** | **5** | 1 | Discount personal magic payments; original resource-specific hard ceiling and minimum positive payment apply. |
| Potency I | Average **+20%** | **5** | 1 | Base personal spell effect strength; source-specific spells and damage tags. Existing four preview attributes stay exact until normalized. |
| Quickening | Weak **+5%** | **10** | 1 | Personal spell casting speed, not industrial throughput. |
| Projection | Middling **+10%** | **10** | 1 | Valid spell target range, not free global AoE. |
| Persistence | Weak **+5%** | **25** | 1 | Duration of supported temporary effects and personally owned summons. |
| Conjuration | Average **+20%** | **10** | 1 | Bounded strength of personally owned summoned allies, not free summoned mob count. |
| Warding | Middling **+10%** | **10** | 1 | Source-verified magical damage resistance, hard mitigation cap. |

### Retained Tyz 6.4.2 source skills (proposed rehomes)

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Health Boost † | Weak **+5%** | **25** | 1 | Increases maximum health by {value} half hearts |
| Oxygen Boost † | Middling **+10%** | **10** | 1 | Increases underwater breathing time by {value}% |
| Block Reach † | Weak **+5%** | **5** | 1 | Increases the range at which the player can place and destroy by {value} blocks |
| Attack Damage † | Weak **+5%** | **10** | 1 | Increases melee damage by {value} |
| Sneaking Speed † | Weak **+5%** | **50** | 1 | Increases movement speed while sneaking by {value}% |
| speed_boost † | Weak **+5%** | **10** | 1 | NO ENGLISH STRING FOUND |
| Step Height † | Weak **+5%** | **5** | 1 | Allows stepping up blocks up to {value} blocks high without jumping |
| Swim Speed † | Weak **+5%** | **25** | 1 | Increases swim speed by {value}% |
| Excavation † | Weak **+5%** | **50** | 1 | Increases mining speed by {value}% |
| Swift Learning † | Weak **+5%** | **25** | 1 | Increases Skill XP gain by {value}% |
| Metabolism † | Weak **+5%** | **10** | 1 | Increases food value (nutrition & saturation) by {nutrition_bonus}% |
| Wisdom † | Middling **+10%** | **10** | 1 | Increases Vanilla XP gain by {bonus_percentage}% |
| Stealth † | Weak **+5%** | **25** | 1 | Reduces the detection range of hostile mobs by {range_reduction}% |
| Magnet † | Weak **+5%** | **5** | 1 | Attracts items within a {block_radius} block radius |
| Spare Parts † | Weak **+5%** | **5** | 1 | Grants a {refund_chance}% chance for each ingredient to be recovered when crafting. Up to {max_materials} item(s) per craft |
| Twist Of Fate † | Weak **+5%** | **10** | 1 | Increases the quantity of items found in structure loot chests by {loot_multiplier}% |
| Safekept † | Weak **+5%** | **5** | 1 | Safeguards the first {saved_slots} slot(s) of your inventory upon death |
| Haggler † | Weak **+5%** | **5** | 1 | Holding an Emerald attracts villagers within a {block_radius} block radius.  They will follow you with a {villager_speed}% speed bonus |

**Core note:** initial 4 five-rank skills remain live-source pilots; all other new and legacy rows are conceptual. A stock Tyz immutable handler must keep its ID and type until actual replacement code exists.

## 4. Cave Dragon — `tmm_dragon_cave`

**Identity:** Supreme high-capacity/high-potency dragon caster with armor, claws and harvest at premium SP prices; special emphasis on demonic magic.. **Current intended authority:** Dragon Survival verified species and real growth gates. 

### Universal Dragon Physiology & Sorcery (species-local)

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Draconic Reservoir | Extreme **+100%** | **25** | 1 | Massive bonus to personally usable maximum magic resource capacity; never machine/world storage. |
| Draconic Potency | Extreme **+100%** | **10** | 1 | Very high general personal spell potency per SP; additive by same reference with typed affinity, not multiplicative recursion. |
| Draconic Scales | Middling **+10%** | **25** | 2 | Species natural armor; deliberately inefficient per SP versus specialized martial defense. |
| Draconic Claws | Middling **+10%** | **25** | 2 | Real dragon form physical strike boost; not global magic damage. |
| Draconic Talons | Weak **+5%** | **50** | 2 | Improve legal dragon claw harvest rate, not tool-tier bypass. |

### Claw & Fang

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Stone Claws | Middling **+10%** | **25** | 2 | Scale unarmed/claw damage only in dragon body; no weapon-wide bonus. |
| Crushing Bite | Average **+20%** | **10** | 2 | Bounded armor/knockback pressure on confirmed bite hits. |
| Seismic Pounce | Strong **+50%** | **5** | 2 | Cooldown-limited landing shockwave against entities; terrain damage off by default. |

### Stone Scales

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Stonehide | Middling **+10%** | **25** | 2 | Incremental natural armor; respect equipment stacking caps. |
| Earthbound Poise | Average **+20%** | **10** | 2 | Reduce knockback and fall damage while grounded. |
| Mountain Bastion | Strong **+50%** | **5** | 2 | Short defensive stance with a resource cost and meaningful cooldown, not permanent immunity. |

### Excavation

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Tunneler | Weak **+5%** | **50** | 2 | Improve legal claw-mining speed; preserve vanilla/tool harvest checks. |
| Vein Sense | Weak **+5%** | **10** | 2 | Short-range, timed ore awareness without unrestricted x-ray or chunk scans. |
| Deep Excavation | Average **+20%** | **5** | 2 | Controlled multi-block excavation; count actual blocks, honor claims and server limits. |

### Infernal Attunement

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Infernal Resonance | Strong **+50%** | **25** | 1 | Bonus to source-verified demonic/infernal personal magic, beyond shared dragon arcane power but not world rituals. |
| Hellbound Command | Strong **+50%** | **10** | 1 | Strengthen eligible player-owned demonic servants and source-verified infernal spell interactions. |
| Infernal Dominion | Extreme **+100%** | **5** | 1 | Late specialization in demon-affiliated spell potency; strong, narrowly classified and gated. |

**Capstone: Heart of the Mountain** — one purchase, **5 SP provisional**, treated as **Extreme power-budget** but **not** a literal unlimited +100% permanent stat buff. Requires verified final native milestone, a different-lane prerequisite and intended cooldown/resource limits. After late native dragon progression, briefly combine armor and excavation benefits; no Chaos Guardian-tier gear replacement.

**Cave integration:** infernal/demonic spells and owned demonic summons only; no effect on all Goety infrastructure. All Dragon-only mana **regeneration** is forbidden.

## 5. Sea Dragon — `tmm_dragon_sea`

**Identity:** High-capacity/high-potency dragon caster of tides, cold, storms and deep-sea magic; aquatic mobility and defenses with costly generic combat/harvest nodes.. **Current intended authority:** Dragon Survival verified species and real growth gates. 

### Universal Dragon Physiology & Sorcery (species-local)

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Draconic Reservoir | Extreme **+100%** | **25** | 1 | Massive bonus to personally usable maximum magic resource capacity; never machine/world storage. |
| Draconic Potency | Extreme **+100%** | **10** | 1 | Very high general personal spell potency per SP; additive by same reference with typed affinity, not multiplicative recursion. |
| Draconic Scales | Middling **+10%** | **25** | 2 | Species natural armor; deliberately inefficient per SP versus specialized martial defense. |
| Draconic Claws | Middling **+10%** | **25** | 2 | Real dragon form physical strike boost; not global magic damage. |
| Draconic Talons | Weak **+5%** | **50** | 2 | Improve legal dragon claw harvest rate, not tool-tier bypass. |

### Tideshaping

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Current Rider | Average **+20%** | **25** | 2 | Improve movement control in water, including currents. |
| Tidal Strike | Strong **+50%** | **10** | 2 | Enhance wet-target combat and native aquatic attacks, not generic damage. |
| Undertow | Strong **+50%** | **5** | 2 | Cooldown-limited pull/slow applied to legal targets without moving world water blocks. |

### Abyssal Defense

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Pressure Adaptation | Middling **+10%** | **25** | 2 | Reduce underwater environmental penalties. |
| Brine Scales | Average **+20%** | **10** | 2 | Situational protection while submerged; no blanket armor bonus on land. |
| Deepwater Refuge | Strong **+50%** | **5** | 2 | Bounded underwater healing or defense with a meaningful resource/cooldown cost. |

### Swimmer's Grace

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Sleek Form | Weak **+5%** | **50** | 2 | Increase underwater speed, capped against sprint/exotic gear. |
| Slipstream | Middling **+10%** | **10** | 2 | Short burst dodge in water with collision safety. |
| Ocean Leap | Average **+20%** | **5** | 2 | Leap from water and control reentry without free flight. |

### Tidal & Abyssal Magic

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Tidal Attunement | Strong **+50%** | **25** | 1 | Enhance verified water, ocean and cold-themed personal spell damage/effects, not arbitrary wet status. |
| Stormcalling | Strong **+50%** | **10** | 1 | Enhance verified lightning/storm magic and aquatic control where supported; no unconditional world weather control. |
| Abyssal Sovereignty | Extreme **+100%** | **5** | 1 | High-level sea/abyssal spell focus, not an assumption that Occultism automatically counts as ocean magic. |

### Abyssal Fortune

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| **Abyssal Fortune** | **Extreme +100%** | **50** | 1 | Exceptional personal Luck strength; intentionally ridiculous at maximum investment. Real effects depend on Luck-aware game systems. |

**Luck interpretation:** Extreme ×50 is a **+5,000% design budget relative to an explicit nonzero Luck reference**, not +5,000% to the vanilla player's frequently zero Luck attribute, nor an unconditional ×51 multiplier to every loot roll. A provisional native Luck-unit conversion and its actual effect on loot tables, fishing and modded luck calculations must be verified before a working number is claimed. No duplicated fixed boss rewards, key fragments or progression-gated artifacts. This should be the **highest luck-oriented specialization in the nine-tree system**.

**Capstone: Tide Sovereign** — one purchase, **5 SP provisional**, treated as **Extreme power-budget** but **not** a literal unlimited +100% permanent stat buff. Requires verified final native milestone, a different-lane prerequisite and intended cooldown/resource limits. Late-game, time-limited aquatic combat and mobility surge; must be distinctly weaker on land.

**Sea integration:** tide, cold, storm and abyss are provisional source-tagged spell groups, **not** Nether/End/ocean-dimension classifications. No automatic weather manipulation or dragon-exclusive magic regen. Sea Dragon gains **Abyssal Fortune (Extreme ×50 Luck)** as a separate high-ceiling specialty; verify its real source-specific effect rather than inventing raw loot multiplication.

## 6. Forest Dragon — `tmm_dragon_forest`

**Identity:** High-capacity/high-potency fae dragon caster with ecological control, crop bounty, breeding and bonemeal effects; generic combat/harvest ranks cost a premium.. **Current intended authority:** Dragon Survival verified species and real growth gates. 

### Universal Dragon Physiology & Sorcery (species-local)

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Draconic Reservoir | Extreme **+100%** | **25** | 1 | Massive bonus to personally usable maximum magic resource capacity; never machine/world storage. |
| Draconic Potency | Extreme **+100%** | **10** | 1 | Very high general personal spell potency per SP; additive by same reference with typed affinity, not multiplicative recursion. |
| Draconic Scales | Middling **+10%** | **25** | 2 | Species natural armor; deliberately inefficient per SP versus specialized martial defense. |
| Draconic Claws | Middling **+10%** | **25** | 2 | Real dragon form physical strike boost; not global magic damage. |
| Draconic Talons | Weak **+5%** | **50** | 2 | Improve legal dragon claw harvest rate, not tool-tier bypass. |

### Verdant Vitality

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Green Recovery | Middling **+10%** | **25** | 2 | Small recovery benefit in living biomes, not in all dimensions. |
| Living Bark | Average **+20%** | **10** | 2 | Situational natural defense near vegetation, with stacking caps. |
| Elder Grove | Strong **+50%** | **5** | 2 | Time-limited regeneration/support pulse, costing dragon resource. |

### Thorn & Root

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Bramble Claw | Strong **+50%** | **25** | 1 | Add a bounded nature-themed status to confirmed claw attacks. |
| Entangling Breath | Strong **+50%** | **10** | 1 | Native breath enhancer applying short slows only where source breath supports it. |
| Thornstorm | Extreme **+100%** | **5** | 1 | Cooldown-limited small-area crowd control with no world block destruction. |

### Fertility & Bounty

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Germination Touch | Average **+20%** | **10** | 1 | Enhance actual bonemeal application efficiency/area/yield without free unbounded creation of growth. |
| Breeder's Blessing | Middling **+10%** | **10** | 1 | Improve legal animal breeding/cooldown or food efficiency via real pair interaction; never spawn animals without parents. |
| Harvest Bounty | Average **+20%** | **10** | 1 | Increase real crop harvest outcomes with explicit per-block credit and cooldown/caps; preserve claim and mature crop checks. |

### Fae Affinity

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Fey Courtesy | Strong **+50%** | **25** | 1 | Improve source-verified fae-aligned personal magic effects and fae relations; no universal mob pacification. |
| Fey Conduit | Strong **+50%** | **10** | 1 | Specialize fae-aligned spell potency and owned fae summons at higher benefit per SP. |
| Grove Guardian | Extreme **+100%** | **5** | 1 | Bounded fae-aligned personal magic and allied-support synergy without taking over world production. |

### Retained Tyz 6.4.2 source skills

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Venomous Strike † | Middling **+10%** | **10** | 1 | Adds a {success_probability}% chance to poison targets on hit |
| Green Thumb † | Average **+20%** | **10** | 1 | Adds a {success_probability}% chance to double drops when harvesting crops and plants |
| Shepherd’s Blessing † | Average **+20%** | **10** | 1 | Baby animals grow {growth_speed}% faster |

**Capstone: Elderwood Heart** — one purchase, **5 SP provisional**, treated as **Extreme power-budget** but **not** a literal unlimited +100% permanent stat buff. Requires verified final native milestone, a different-lane prerequisite and intended cooldown/resource limits. Late native progression grants a timed defensive/support state in forest biomes.

**Forest integration:** source-verified fae spells plus legitimate bonemeal, breeding and crop harvest events. Old `green_thumb` / `shepherd_s_blessing` move with same IDs, gated by Forest Dragon; no automatic doubled award. No dragon-exclusive mana regen.

## 7. Vampire — `tmm_vampire`

**Identity:** Blood economy, predatory stealth and undead survival. **Current intended authority:** JAWR verified vampire state. 

### Bloodcraft

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Efficient Feeding | Average **+20%** | **25** | 1 | Improve real blood gained from eligible feeding transactions; cannot create blood from empty actions. |
| Deep Reservoir | Strong **+50%** | **10** | 1 | Increase one authoritative usable blood capacity, not two independent HUD pools. |
| Crimson Renewal | Strong **+50%** | **5** | 1 | Trade a real measured blood debit for bounded emergency healing. |

### Nightstalker

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Nocturnal Vision | Weak **+5%** | **50** | 1 | Better low-light perception with native-sunlight penalties untouched. |
| Shadow Stalker | Average **+20%** | **10** | 1 | Short stealth advantage in darkness; avoid invisibility in full daylight. |
| Bat Mastery | Strong **+50%** | **5** | 1 | Improve native bat-form controls/efficiency after that native ability is acquired. |

### Predation

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Fang Training | Average **+20%** | **25** | 1 | Scale only verified vampire bite/melee attacks. |
| Measured Feast | Strong **+50%** | **10** | 1 | Limited post-kill blood recovery from eligible kills, with transaction deduplication. |
| Blood Duelist | Strong **+50%** | **5** | 1 | Focused single-target bonuses under low blood; prevent intentional zero-cost loops. |

### Undead Constitution

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Pale Resilience | Middling **+10%** | **25** | 1 | Bounded poison/disease-type mitigation where the source system allows it. |
| Sunshroud | Average **+20%** | **10** | 1 | Short resource-funded sunlight resistance; never erase core daylight tradeoffs. |
| Elder Presence | Strong **+50%** | **5** | 1 | Bounded defensive/control aura with cooldown, not full charm immunity. |

### Retained Vampire Skill Concepts (JAWR-owned)

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Blood Thrift | Weak **+5%** | **10** | 1 | Reduce only real JAWR blood expenditure/hunger cost on verified committed actions. |
| Predatory Finesse | Average **+20%** | **10** | 1 | Blood-funded vampire-specific melee attack speed, not spell casting speed. |
| Night Runner | Middling **+10%** | **25** | 1 | Improved vampire walking/running mobility in darkness; no universal free speed. |
| Crimson Surge | Strong **+50%** | **5** | 1 | Blood-paid high-stage predatory burst, supplementing Blood Regent without minions. |

### Retained Tyz 6.4.2 source skills

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Bloodlust † | Strong **+50%** | **10** | 1 | Killing a hostile mob regenerates {health_percentage}% of its max health |

**Capstone: Blood Regent** — one purchase, **5 SP provisional**, treated as **Extreme power-budget** but **not** a literal unlimited +100% permanent stat buff. Requires verified final native milestone, a different-lane prerequisite and intended cooldown/resource limits. Blood-funded high-tier predatory stance requiring advanced native vampire progression.

**Vampire integration:** JAWR is the **only future** vampire physiology and blood owner. The old Vampirism mod is a retirement dependency, not a second progression source; no minions, raids, blood projectile or lord faction. Every Blood Thrift/Feeding/Crimson Renewal purchase is tied to an actual JAWR debit or source event.

## 8. Werewolf — `tmm_werewolf`

**Identity:** Exceptional max hearts and physical health regeneration, feral melee, and strong nature magic synergy.. **Current intended authority:** JAWR verified lycanthrope state. 

### Lupine Vitality

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Feral Vigor | Strong **+50%** | **25** | 1 | Large additional maximum health / extra hearts, materially more per SP than universal vitality boosts. |
| Regenerative Flesh | Strong **+50%** | **10** | 1 | Strong, bounded actual HP regeneration under verified werewolf physiology and legal feeding/hunger rules; not mana regen. |
| Unbreakable Beast | Strong **+50%** | **5** | 1 | High-level extra HP and in-combat wound recovery with caps and meaningful werewolf vulnerabilities. |

### Feral Might

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Rending Claws | Strong **+50%** | **25** | 1 | Scale claw damage in valid wolf/beast form, respecting weapon modifiers. |
| Savage Momentum | Strong **+50%** | **10** | 1 | Short stacking melee bonus after actual hits, capped and reset out of combat. |
| Primal Fury | Extreme **+100%** | **5** | 1 | Timed burst consuming native appropriate resource or applying fatigue. |

### Moonrunner

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Predator's Stride | Average **+20%** | **25** | 1 | Bonus mobility in valid wolf form. |
| Pouncing Leap | Strong **+50%** | **10** | 1 | Targeted controlled leap with landing safety. |
| Lunar Chase | Strong **+50%** | **5** | 1 | Short chase burst under moon conditions with weaker daytime counterpart. |

### Wild Communion

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Wild Attunement | Strong **+50%** | **25** | 1 | Increase eligible nature magic potency (Botania/Roots personal effects) while werewolf state is valid. |
| Rootsong | Strong **+50%** | **10** | 1 | Further increase player nature spell potency or healing, separate from ordinary combat regeneration. |
| Grove Howl | Extreme **+100%** | **5** | 1 | High-level synergy between wolf pack allies and personally cast nature magic, bounded to eligible owners/effects. |

### Retained Lycan Skill Concepts (JAWR-owned)

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Bloodlust Recovery | Average **+20%** | **10** | 1 | Health restoration from actual player-credited hostile kill, with real cooldown. |
| Bite Techniques | Middling **+10%** | **10** | 1 | Select either limited bleeding or brief stun enhancement on confirmed wolf bites. |
| Feral Excavation | Middling **+10%** | **25** | 1 | Boost permitted claw mining/harvest throughput while in real JAWR wolf form. |
| Arrow Awareness | Average **+20%** | **10** | 1 | Limited moving-projectile evasion with shared evasion budget. |
| Tempered Against Silver | Weak **+5%** | **10** | 1 | Partial damage mitigation against source-verified silver damage without erasing weakness. |

### Retained Tyz 6.4.2 source skills

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Rage † | Strong **+50%** | **10** | 1 | Increases damage by {damage_buff}% when health is below {health_threshold}% |

**Capstone: Moon Apex** — one purchase, **5 SP provisional**, treated as **Extreme power-budget** but **not** a literal unlimited +100% permanent stat buff. Requires verified final native milestone, a different-lane prerequisite and intended cooldown/resource limits. Late-game feral ascendancy emphasizing massive health, sustained HP regeneration, melee ferocity and nature magic; retain native silver/moon weaknesses.

**Werewolf integration:** JAWR owns actual form/feeding/moon/silver physiology. Extra hearts use the **pre-Tyz base maximum health**; regeneration uses an explicit HP/s benchmark, not vanilla zero and not magic regen. Preserve retained Bloodlust Recovery, bites, claw excavation, projectile dodge, and partial silver weakness. Never import TeamLapen wolf-pet summoning.

## 9. Lich — `tmm_lich`

**Identity:** Necromantic and blood-magic specialist, explicitly exchanging personal nature-magic potency for amplified blood magic.. **Current intended authority:** JAWR Forbidden Magic / Goety verified lich state (Archlich and Dracolich share one Lich tree). 

### Soul Economy

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Soul Conservation | Strong **+50%** | **25** | 1 | Lower eligible personal necromancy spell costs, not global ritual/machine cost. |
| Soul Reserve | Average **+20%** | **10** | 1 | Bounded native personal soul/Arca resource capacity where extensible. |
| Soul Treasury | Average **+20%** | **5** | 1 | Small high-level sustain on committed necromantic actions, no passive self-funding loop. |

### Blood & Nature Opposition

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Sanguine Attunement | Strong **+50%** | **25** | 1 | Improve verified blood-based personal magic (Neo Vitae/EvilCraft where actual hooks apply) while applying a meaningful reduction to this lich's personal Botania/Roots nature spell power. |
| Crimson Channel | Strong **+50%** | **10** | 1 | Strengthen source-verified blood magic further with proportionate nature-magic potency tradeoff; not passive blood machine throughput. |
| Verdant Severance | Extreme **+100%** | **5** | 1 | High blood-magic specialization at a stronger self-inflicted Botania/Roots nature power penalty; no world-machine or other-player debuff. |

### Undead Host

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Bone Captain | Average **+20%** | **25** | 1 | Modest bonus to personally owned undead servants. |
| Grave Regiment | Strong **+50%** | **10** | 1 | Improved servant coordination/leash and bounded count efficiency. |
| Death Host | Strong **+50%** | **5** | 1 | Late temporary enhancement to current owned minions with hard entity budget. |

### Soul Preservation

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Cold Persistence | Weak **+5%** | **25** | 1 | Limited tolerance of wither/undead hazards, preserving explicit weaknesses. |
| Anchor Attunement | Middling **+10%** | **10** | 1 | Improve diagnostics or controlled anchor maintenance; never bypass native phylactery/Arca cost. |
| Final Passage | Average **+20%** | **5** | 1 | Minor post-rescue recovery *after* an authoritative successful resurrection; no extra death-save trigger. |

**Capstone: Sovereign of Ash** — one purchase, **5 SP provisional**, treated as **Extreme power-budget** but **not** a literal unlimited +100% permanent stat buff. Requires verified final native milestone, a different-lane prerequisite and intended cooldown/resource limits. High-tier necromantic command stance requiring advanced lich progression and stable anchor ownership.

**Lich exchange formula:** owning the three blood-specialist tracks reduces only the lich player's **personal nature spell potency** (Botania/Roots), respectively **-2, -3, -5 percentage points per purchased blood-specialist rank**. Combined reduction maxes at **75%**, applied **after** positive Core effects so it remains a real weakness. EvilCraft/Neo Vitae buffs apply only to source-verified player blood effects, not multiblock automation or all items.

## 10. Hunter — `tmm_hunter`

**Identity:** Anti-undead/demon/fae apex monster-slayer; best combat upgrades, robust survival and equipment conservation, virtually no spell-stat growth. **Current intended authority:** standalone Hunter progression; cross-form exclusivity not assumed. 

### Quarry Lore & Tracking

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Monster Lore | Weak **+5%** | **25** | 1 | Recognize verified undead, demon and fae categories, including weaknesses without revealing unrelated bosses. |
| Quarry Mark | Middling **+10%** | **10** | 1 | Mark one legitimate undead, demon or fae quarry for specialized hunter tactics. |
| Hunter Tracking | Weak **+5%** | **5** | 1 | Very high-level proximity effect: apply short, refreshed Glowing to eligible undead, demons and fae; explicit radius/cooldown and no unrelated mobs. |

### Arms & Ammunition

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Weapon Tempering | Weak **+5%** | **25** | 1 | Reduce real durability loss from Hunter weapons without healing or duplicating equipment. |
| Measured Bolt | Middling **+10%** | **10** | 1 | Chance to avoid consuming actual crossbow projectile ammo on successful fire; keep special ammo/fireworks excluded until source QA. |
| Supernatural Bane | Strong **+50%** | **10** | 1 | Very high-level significant damage bonus against verified undead, demons and fae; source-tag and boss caps. |

### Armor & Survival

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Armored Longevity | Weak **+5%** | **25** | 1 | Reduce real durability damage to equipped armor; preserve equipment break and exclude repair exploits. |
| Survivor's Grit | Average **+20%** | **10** | 1 | Strong nonmagical survival upgrade for a prepared Hunter without mimicking regeneration/immortality. |
| Dreadbane Defense | Strong **+50%** | **10** | 1 | Very high-level improved survivability specifically against verified undead, demons and fae. |

### Monster-Slayer Tactics

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Fieldcraft | Average **+20%** | **25** | 1 | Prepare and maneuver under threat with weapon-based tactics, not generic magic damage. |
| Consecrated Snare | Strong **+50%** | **10** | 1 | Place claim-safe anti-monster traps using appropriate hunter resources, without generating magical mana. |
| Slayer's Counter | Strong **+50%** | **5** | 1 | High-level counterattack window against identified supernatural enemies; bosses require special approval. |

### Other Historic Combat Integration (not duplicates)

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Drakebane | Strong **+50%** | **10** | 1 | Optional specialization against verified dragons and draconic mobs, with dedicated Hunter monster-category rules rather than a boss/dimension label. |

### Retained Tyz 6.4.2 source skills

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Critical Strike † | Strong **+50%** | **10** | 1 | Adds a {success_probability}% chance to deal a critical hit (double damage) |
| Deflection † | Strong **+50%** | **5** | 1 | Adds a {success_probability}% chance to negate and reflect incoming melee damage |
| Resistance † | Strong **+50%** | **5** | 1 | Reduces all incoming damage by {damage_resistance}% |
| Backstab † | Strong **+50%** | **10** | 1 | Increases damage dealt from behind by {damage_buff}% |
| Adrenaline † | Strong **+50%** | **5** | 1 | Grants Absorption and Speed III for {effect_duration}s when health falls below {health_threshold}% |
| Resilience † | Strong **+50%** | **5** | 1 | Reduces the duration of negative status effects by {effect_time_reduction}% |
| Jinxed † | Weak **+5%** | **5** | 1 | Adds a {success_probability}% chance to scramble the target's hotbar on hit |
| True Strike † | Strong **+50%** | **5** | 1 | Adds a {success_probability}% chance to deal true damage on hit |
| Sunder Armor † | Average **+20%** | **10** | 1 | Adds a {success_probability}% chance for attacks to deal {damage_percentage}% extra durability damage to armor |

**Capstone: Master Slayer** — one purchase, **5 SP provisional**, treated as **Extreme power-budget** but **not** a literal unlimited +100% permanent stat buff. Requires verified final native milestone, a different-lane prerequisite and intended cooldown/resource limits. Hunter combat capstone: powerful damage and defense versus verified undead/demons/fae, Hunter Tracking synergy, weapon proficiency; no broad spell power. Preserve late quarry-specific anti-undead/demon/fae focus, not flat +100% vs all mobs.

**Hunter integration:** rank-10 Supernatural Bane and Dreadbane Defense require actual **undead/demon/fae** classification and a **very high native Hunter milestone**. The tracking skill grants bounded Glowing at late ranks; crossbow ammo conservation is a real prevented payment, not refund/dupe. Existing `sunder_armor` harms *enemy* gear and does not solve Hunter equipment preservation. Jinxed and True Strike remain held for PvP/boss-policy audits.

## 11. Monk — `tmm_monk`

**Identity:** Unarmed martial arts, unarmored body, motion and hand-harvest specialization. **Current intended authority:** standalone Monk faction + discipline. 

### Martial Body

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Empty-Hand Force | Average **+20%** | **25** | 1 | Repeatable empty-hand damage bonus beyond the native +1 Open Palm unlock; stacking cap. |
| Flowing Counter | Strong **+50%** | **10** | 1 | Bounded counterattack after a verified native Guarding Palm/parry unlock. |
| Empty-Hand Precision | Strong **+50%** | **5** | 1 | Strengthen genuine unarmed hit precision/critical strike after Monk Guarding Palm/Monk rank; no innate spirit projectile or spell projectile |

### Conditioning

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Natural Poise | Average **+20%** | **25** | 1 | Scale unarmored protection beyond native Unarmored Poise +2; never scale equipped armor. |
| Hunger Discipline | Weak **+5%** | **10** | 1 | Bounded food/exhaustion efficiency after native license; do not remove all hunger. |
| Fire & Blast Tempering | Average **+20%** | **5** | 1 | Limited environmental damage mitigation after both matching native licenses. |

### Footwork

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Body Speed | Average **+20%** | **25** | 1 | Repeatable speed under no conventional gear discipline; native Fleet Footwork remains a one-off. |
| Balanced Landing | Middling **+10%** | **10** | 1 | Fall/knockback control; native Step Assist remains a one-off in native tree. |
| Glide Control | Strong **+50%** | **5** | 1 | Improve native momentum gliding only after rank-15 native unlock; never creative flight. |

### Hands as Tools

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Barehand Efficiency | Average **+20%** | **25** | 1 | Repeatable hand-mining speed; native Stone/Iron/Diamond/Netherite Fist unlock legal harvest tiers. |
| Fortunate Hands | Average **+20%** | **10** | 1 | Bounded fortune-like bonus only after native rank-13 license and legitimate drop event. |
| Looting Palm | Strong **+50%** | **5** | 1 | Bounded unarmed looting after native rank-9 license; no creature-drop duplication. |

### Bounded Native-Licensed Tyz Enhancements

| Skill | Tier / rank bonus | Rank cap | SP per rank | What the ranks do |
|---|---|---:|---:|---|
| Monastic Mastery Learning | Weak **+5%** | **25** | 1 | Improve real Monk native practice/mastery gain after its native license; **not** global MXP or Tyz XP multiplier. |
| Focused Advancement | Weak **+5%** | **5** | 1 | One-time bounded Monk native skill-point training improvement only with owner-approved accounting. |

**Capstone: Empty-Hand Paragon** — one purchase, **5 SP provisional**, treated as **Extreme power-budget** but **not** a literal unlimited +100% permanent stat buff. Requires verified final native milestone, a different-lane prerequisite and intended cooldown/resource limits. **The entire maxed Monk tree (not merely this capstone) must permit a skilled player to solo the genuine Chaos Guardian without any worn equipment or weapons**; source-verified phases and meaningful danger remain required.

**Monk integration and endgame acceptance:** this is the **overall strongest personal combat path**, able when maxed to finish the real Chaos Guardian without worn gear or weapons. Other paths may match one or two specialties, but lack Monk's complete no-gear combat toolkit. Standalone Monk is the one-off native permission owner, not Vampirism. **No innate spirit projectile.** Ars Nouveau / Iron's supply ranged spells. First hotbar slot reserved for the fist, armor lock togglable default-on and non-purchasable, one Treasure Hand Fortune/Silk Touch mode toggle. There are separately **40 bounded Apotheosis affix-rarity** candidates retained in Monk's native source audit, not expanded to fake 25/50-level bonuses.

## Review and test order

1. **Approve the class power ratios and caps** before plugging in per-mod actual attributes. Extreme Dragon mana ×25 gives **+2,500%** by design and is a major endgame target; Extreme spell potency ×10 gives **+1,000%** and needs damage-school/boss caps. Strong Werewolf HP ×25 is **+1,250% of starting max HP**; test against real boss damage before acceptance. These are first-pass *targets*, not hard commitments.
2. Source-map each bonus to an installed attribute/event and an **unmodified reference baseline**. A tag named demon/fae or a name with "blood" is not proof of correct magic or entity classification.
3. Build narrow representative vertical tests: average five-level Core Reservoir, Extreme Dragon Reservoir/Potency, Strong late Hunter Bane + capped Glowing, Werewolf maximum hearts and health ticks, Forest Dragon crop/bonemeal, Lich one positive blood plus nature penalty, and Monk Fortune/Silk mode; then a dedicated no-worn-gear, no-weapons Chaos Guardian solo acceptance run after actual Monk abilities exist.
4. Preserve the 32 original Tyz skills with **identical IDs** until actual relocation parity and player data conversion are verified. The original four default categories retire last. A disabled or missing skill must not silently confiscate spent SP or continue firing after cure.
5. Measure balance against the modpack's gear ceiling, enemies (including high-level Cataclysm encounters and dragons), single-player and multiplayer stress, and the steeply increasing MXP cost per earned SP. Avoid unlimited immunity, infinite magic self-funding, arbitrary worldgen/pets or over-broad mob categories.

**Boundary:** this design does **not** install new form powers, disable any original mod, change the current five-rank Core Skills config, or prove any effect works in game.
