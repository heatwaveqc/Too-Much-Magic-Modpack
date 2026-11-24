# Cave Dragon — complete first-pass 100-point skill tree

**Too Much Magic · 2026-11-22 · DESIGN ONLY; NOT LOADED, NOT GAME-TESTED**

**Source chain:** this design is on `feature/cave-dragon-100pt-complete-tree-20261122`, stacked on Lich PR #526 → Werewolf #520 → Vampire #518 → Sea Dragon #514 → parent blueprint #495. The JSON and companion MD are design ledgers, **not runtime Path JSON**. No Java, KubeJS, packs, integration, recipes, client or Prism files changed.

## Player identity and free native authority

**Cave Dragon is TMM's supreme magma-and-stone excavating caster**, distinctive from Sea Dragon's aquatic mobility/Luck and Forest Dragon's vegetation/animal support. It excels underground and in the Nether: premium personal mana and spell power; earned native firebreath, fireball and protective magic; a Cave-pickaxe claw system; stonebound durability and selective personally cast demon/infernal spells. It remains seriously vulnerable to water, rain and snow.

- The only permitted species gate is **Dragon Survival's authoritative CAVE form**. Source retains its age/size/health progression, innate lava/fire immunity, lava vision, natural claws/teeth, genuine mining harvest tier, native skill prerequisites, native fire breath/ball, Sturdy Skin, flight and Flight Spin, controls and mana/cooldown. These are **free** and must never be separately sold as new abilities. Some are conditional on native skill learning.
- The authoritative source developer wiki says the Cave claw-slot specialty is a **pickaxe**, native claw tools have held-tool precedence, and 1.21.1 abilities are datapack-generated. The wiki's old named spell list is an **audit candidate**, not evidence the exact 2.0.74 binary registers an identical ID or behavior.
- TMM owner-locked food redesign issue #444 **supersedes** the old Cave charred/coal species diet: Cave prefers **raw terrestrial meat and terrestrial monster meat**, default preferred raw source 6 nutrition / 2.0 saturation modifier; cooked is fractionally worse, charred worse again, and special mineral/magical foods are handled separately. None of these food rules is altered or monetized by this tree; breath-related food interactions must respect the new food handler's stages rather than resurrect old charred preference.
- **No new active ability buttons**. Every rank modifies real player casts/native actions, provides conditional passives, or responds to legitimate in-game events. No standalone mana bar, new XP wallet, additional harvest permissions, creative flight, re-granted fire immunity, infinite heat protection for allies, unrestricted ore tracking or generic Ultimine.
- A legitimate Dracolich Cave Dragon may hold **one Cave Dragon tree + one shared Lich tree**; it cannot hold an extra dragon species or the other native Lich. Universal Core remains a separate player-owned progression. All entered/advanced/mastery gates must use actual provider states; no made-up source level milestones.

## Exact purchase budget

| Branch | Skills | One-point ranks |
|---|---:|---:|
| Draconic Body & Personal Sorcery | 5 | 20 |
| Pyroclastic Combat | 6 | 23 |
| Stone Scales & Hearth | 5 | 19 |
| Excavation & Depth | 5 | 22 |
| Infernal Sorcery | 4 | 16 |
| **Total** | **25** | **100** |

Every rank has price **1**, including late talents. Candidate numerics are new, bounded balance assumptions; the old 17-skill allocation and historical extreme Tyz figures are reference/provenance only, not caps or costs.

## Purchased skills and candidate effects

**Activation rule:** if a referenced native skill/attribute/source hook does not exist for the pinned 1.21.1 binary, hold that paid skill back until a source-backed substitute is reviewed. Do not sell an inert rank merely to fill the point count.

### Draconic Body & Personal Sorcery — 20 points

| Purchased skill | Ranks | Candidate effect | Gate | Required source/handler proof |
|---|---:|---|---|---|
| **Draconic Reservoir** (`tyr_progression:cave_dragon_draconic_reservoir`) | 6 | +10% per rank (+60% full) to existing eligible personal spell-resource maximums, including Dragon Survival native mana only where source-safe. Excludes machines, Arca, phylacteries and nonplayer mana grids. | entry | Provider-specific personal capacity, Core stacking order and true Dragon Survival native mana cap must be source-checked |
| **Draconic Potency** (`tyr_progression:cave_dragon_draconic_potency`) | 5 | +4% per rank (+20% full) to verified personally cast magic output, additive in the same bonus bucket as Core, before typed fire/infernal bonuses; not passive machines, automatic summons or double spell events. | entry | Need source-accurate personal spell attribution and eligible potency mapping |
| **Draconic Scales** (`tyr_progression:cave_dragon_draconic_scales`) | 3 | +1 natural armor per rank (+3 full) in actual Cave Dragon form, subject to configured cap. This is additional purchased armor, not charging for native Cave scales, levels or fire immunity. | entry | Native Cave body check, modifier cleanup on species/cure and actual armor stacking |
| **Draconic Claws** (`tyr_progression:cave_dragon_draconic_claws`) | 3 | +1 physical attack damage per rank (+3 full) on verified Cave Dragon natural claw/bite melee only, respecting source claw weapon-slot precedence; not sword-held normal attacks or magic casts. | entry | Exact native Dragon Survival claw/bite combat and equipped claw-slot attribution |
| **Draconic Talons** (`tyr_progression:cave_dragon_draconic_talons`) | 3 | +15% per rank (+45% full) to legal native Cave pickaxe-claw block mining speed only. No mining-tier upgrade, Fortune, Silk, ore-gen permission or tool durability bypass. | entry | Real claw pickaxe slot, held-tool precedence, harvest-level and protected ore checks |

### Pyroclastic Combat — 23 points

| Purchased skill | Ranks | Candidate effect | Gate | Required source/handler proof |
|---|---:|---|---|---|
| **Nether Breath** (`tyr_progression:cave_dragon_nether_breath`) | 5 | +4% per rank (+20% full) damage from a verified already-learned native Cave Dragon flame breath. Does not unlock breath, increase terrain burning or permit casting in rain/water when source forbids it. | entry | Current 2.0.74 cave breath datapack ability ID, native damage attribution and terrain behavior |
| **Fireball Mastery** (`tyr_progression:cave_dragon_fireball_mastery`) | 4 | +5% per rank (+20% full) to confirmed native Cave Fireball direct entity impact damage after source unlock. Explosion terrain effects, ignition footprint and block destruction remain native only. | advanced after nether_breath | Current Cave fireball action/ID, direct vs splash attribution, griefing/claims policy |
| **Burning Wake** (`tyr_progression:cave_dragon_burning_wake`) | 4 | +0.5 second per rank (+2 seconds full) to the existing burn on legitimate hostile targets hit by native Cave fire abilities; does not add independent passive ignition or extend beyond configured source fire cap. | advanced after nether_breath | Source-cast fire duration hook; don't repeatedly reset long burns or tag friendly entities |
| **Stone Claws** (`tyr_progression:cave_dragon_stone_claws`) | 3 | +0.1 knockback strength per rank (+0.3 full) to confirmed direct natural Cave Dragon claw attacks against legal hostiles; separate from Draconic Claws' raw damage. Does not affect mined blocks or hand-held weapons. | entry after draconic_claws | Native natural attack recognition rather than all barehand hits |
| **Crushing Bite** (`tyr_progression:cave_dragon_crushing_bite`) | 4 | +3% per rank (+12% full) direct bite damage against verified armored hostile mobs; preserve vanilla source armor calculation, no armor-bypass tag and no bonus to ordinary unarmored targets. | advanced after stone_claws | Actual Cave bite event and target armor predicate; avoid duplicate attack damage path |
| **Seismic Pounce** (`tyr_progression:cave_dragon_seismic_pounce`) | 3 | On a genuine already-unlocked Dragon Survival Flight Spin contacting a hostile while landing on eligible stone, apply Slowness I for +0.5 second per rank (1.5 seconds full), 15-second player cooldown. No new pounce key or terrain shockwave. | mastery after crushing_bite | Native Flight Spin hit/landing attribution, PvP/friendly exclusions, terrain integrity; do not unlock Spin |

### Stone Scales & Hearth — 19 points

| Purchased skill | Ranks | Candidate effect | Gate | Required source/handler proof |
|---|---:|---|---|---|
| **Stonehide** (`tyr_progression:cave_dragon_stonehide`) | 5 | +0.5 armor toughness per rank (+2.5 full) in real Cave Dragon form, under effective defensive caps. Unlike Draconic Scales, this does not raise ordinary armor points. | entry after draconic_scales | Actual armor-toughness stacking with native species and equipment; no free fire immunity resale |
| **Earthbound Poise** (`tyr_progression:cave_dragon_earthbound_poise`) | 4 | -5% per rank (-20% full) ordinary fall damage only when landing on a verified eligible stone block; no void protection, immunity or separate flight permission. | entry | Server-side landing block and native fall-damage chain, race with flight/spin |
| **Mountain Bastion** (`tyr_progression:cave_dragon_mountain_bastion`) | 4 | After taking one genuine hostile hit while grounded on natural stone, gain 3% per rank (12% full) reduction to subsequent eligible direct physical damage for 5 seconds, with 20-second cooldown. The triggering hit is not retroactively reduced. | advanced after stonehide | After-hit listener, grounded condition, cooldown persistence, damage ordering and native source weakness exemptions |
| **Hearthskin** (`tyr_progression:cave_dragon_hearthskin`) | 3 | +8% per rank (+24% full) duration to the actual earned native Cave Sturdy Skin protective effect when legitimately activated; leave source target selection, cooldown, cost and effect strength unchanged. | advanced after stonehide | Source-true 2.0.74 Sturdy Skin datapack/effect receipt and duration, not an invented buff or free cast |
| **Water Temperance** (`tyr_progression:cave_dragon_water_temperance`) | 3 | -4% per rank (-12% full) of explicitly source-tagged Cave Dragon water, rain and snow weakness damage; partial mitigation only, never weather immunity or extinguishing native penalties. | advanced after earthbound_poise | Tagged source weakness types (not generic fire/cold or every water damage event), maintain meaningful exposure weakness |

### Excavation & Depth — 22 points

| Purchased skill | Ranks | Candidate effect | Gate | Required source/handler proof |
|---|---:|---|---|---|
| **Tunneler** (`tyr_progression:cave_dragon_tunneler`) | 5 | +4% per rank (+20% full) movement speed while already benefiting from earned native Cave Athletics on stone and grounded below open sky; does not grant Cave Athletics or invent extra Speed when inactive. | entry | Real Cave Athletics source status, approved stone block set and movement cap |
| **Vein Sense** (`tyr_progression:cave_dragon_vein_sense`) | 4 | After actually mining an eligible ore block with legal Cave claws, briefly (3 seconds) reveal up to 2 additional connected identical ore blocks per rank within five blocks of the broken block. Never global x-ray/chunk scan or excluded gated ores. | advanced after tunneler | Server-side limited scan; whitelist only currently harvestable ores; explicit uranium, hidden progression and protected-claim exclusions |
| **Deep Excavation** (`tyr_progression:cave_dragon_deep_excavation`) | 4 | After breaking three real stone-like blocks consecutively with native legal Cave claws, gain +5% per rank (+20% full) claw mining speed for five seconds. No automated extra block breaks, vein miner or bypass of individual durability/hunger. | advanced after tunneler | Per-player legal block-break receipts; prevent placement loops, no FTB Ultimine analogue |
| **Clawkeeper** (`tyr_progression:cave_dragon_clawkeeper`) | 4 | 5% chance per rank (20% full) to prevent precisely one durability expenditure on the actual equipped native Cave pickaxe-claw tool on a successful legal break. Never repairs items or changes fake slot visuals. | advanced after deep_excavation | Native claw tool-slot durability hooks, Unbreaking interaction and rollback correctness |
| **Understone Sight** (`tyr_progression:cave_dragon_understone_sight`) | 5 | After three seconds in genuinely dark enclosed underground terrain, gain server-authorized enhanced night vision lasting +2 seconds per rank (10 seconds full), refreshed only while conditions hold. Distinct from free native lava vision. | mastery after vein_sense | Do not give permanent see-through-lava, avoid needless potion flicker and vanilla Spectator/creative overrides |

### Infernal Sorcery — 16 points

| Purchased skill | Ranks | Candidate effect | Gate | Required source/handler proof |
|---|---:|---|---|---|
| **Infernal Resonance** (`tyr_progression:cave_dragon_infernal_resonance`) | 5 | +4% per rank (+20% full) personally cast explicitly source-tagged demon/infernal magic potency (e.g. verified Goety or Occultism personal actions), additive with Draconic Potency and not a blanket bonus to all Goety/Occultism rituals. | entry after draconic_potency | Per-action infernal subclass/school and personal attribution, no classification by mod name only |
| **Hellbound Command** (`tyr_progression:cave_dragon_hellbound_command`) | 4 | +4% per rank (+16% full) direct damage from eligible player-owned demonic servants summoned by verified personal provider actions; not wild demons, every Goety undead, bosses or other players' summons. | advanced after infernal_resonance | Owned demon registry and genuine summoner UUID, no extra minion capacity or fake ownership |
| **Infernal Dominion** (`tyr_progression:cave_dragon_infernal_dominion`) | 3 | +5% per rank (+15% full) additional direct damage to eligible personally cast demonic/infernal spells when in the actual Nether or on source-verified hot stone, applied additively alongside Infernal Resonance. | mastery after hellbound_command | Exact provider damage source ID, Nether/hot-block predicate, no recursive burst inflation |
| **Furnace Focus** (`tyr_progression:cave_dragon_furnace_focus`) | 4 | -3% per rank (-12% full) real personally paid fire/infernal spell resource debit while on verified hot Nether/Cave terrain. Never discounts ritual/machine costs, native diet, dragon resurrection, or world aura generation. | advanced after infernal_resonance | Actual server-authoritative personal spell cost receipt, floor/cost caps and heat block tag; MXP award accounting unchanged |

## Implementation crosswalk

| Domain | Existing reusable capability candidate | Must verify or add in shared Progression engine |
|---|---|---|
| Native Cave probes & gates | Read-only Dragon Survival species/growth observation | Correct CAVE species and real earned abilities in 2.0.74; unknown provider fails closed; Core and Lich independent |
| Body defense & attacks | Generic attributes, conditional damage bonus, on-hit status | Distinguish native claw/bite vs equipped held weapon and projectile, survival caps, raw damage and correct stack order |
| Fire breath and ball | Source-identified cast/attack events | Exact cave fire action, direct vs splash, burn extension and source cooldown; water/rain cannot be bypassed; no added terrain grief |
| Ground & water weakness | Conditional speed/damage/defense effects | Legitimate stone/hot block tags, native Cave Athletics, full water/rain/snow source damage tagging, no weakness deletion |
| Excavation | Source-aware break_speed, vision and tool-slot adapters | Native pickaxe-claw slot vs held-tool override; allowed mining tiers, claim permissions, correct durability, no ore/uranium or gate bypass |
| Infernal magic | Source-scoped personal magic adapters, owner-scoped minion stats | Actual per-action demon/infernal secondary tags from Goety/Occultism, personal receipts, valid minion owner; not every soul, ritual or blood effect |
| Dracolich hybrid | Central Progression/native physiology and death controller | Native Dragon Survival anti-death before one eligible Lich backend; no purchasing extra resurrect or cross-species buff |

**Source ownership:** all executable handlers belong in `compat/progression` under a separate `feature/progression-*` PR. Cave Path workstream owns only declarative data, static fixtures and docs. A runnable tree must not be written until its hooks are verified; no external JARs committed.

## QA and design validation to run before activation

1. Exactly 25 node IDs; 100 total one-point ranks; 100-point full eligible build; all rank arrays are `[1]` repeated `max_rank`; all prerequisites resolve; no change to the other nine budgets or Hunter mutual-exclusion calculations.
2. Actual Cave-only gate vs Forest/Sea/Human; Dragon Survival absent/unsupported fails closed; native growth/earned-fireball/sturdy skin/spin and Cave Athletics prerequisites cannot be forged by purchases.
3. Native fire immunity, lava vision, growth, climbing/digging permissions, firebreath and fireball stay **free**. Verify fire behavior in rain and while submerged; no paid perk circumvents the native forbidden cast condition.
4. Cave claw pickaxe slot and **hand-held tool precedence**, tool enchantments, durability, natural stone vs mod stones, correct break speed, harvest checks, claimed blocks, Silk/Fortune and uranium/other gated ores never become bypassable.
5. Ground defense and environmental checks: natural stone, basalt/blackstone, Nether/cave heat, solid and wet blocks, native Cave water/rain/snow damage; Water Temperance at full ranks remains only 12% mitigation.
6. Fire and melee: breath burn from same player vs splash, direct Fireball entity hit vs explosion, one true bite/claw contact, hostile-only Slowness for already-earned Flight Spin, no block modification or friendly/PvP double counting.
7. Infernal: eligible Goety/Occultism **personal spell receipts and owner-tagged demon servants**, no all-Goety/all-Occultism or all-Other bucket boost, no Soul Energy/Arca corruption and no passive ritual-machine acceleration.
8. Personal magic resource ceilings and Core stacking: actual Dragon Survival mana native cap, other personal pools, charged treats/Source of Magic, paid spells, MXP credit and no 2x accounting or artificial nominal cost awards.
9. Diet regression deferred to separately owned #444 food handler: Cave raw beef 6/2.0; native cutting ratios; cooked/charred penalties; fire breath cooking; source mineral exceptions; unchanged ordinary human nutrition. Purchased skills must not affect food ranks.
10. Legitimate Cave Dracolich holds one Cave tree and one shared Lich tree, no double native resurrection, no access to Sea/Forest purchases through species switching; effect removal and respec persist across reload and death.
11. Static/JSON validation and provider audits are **not** Module CI or in-game acceptance. Only the integration writer can assemble owner-authorized tests; stage actual gameplay after source-backed handlers exist.

## Archived names preserved and exceptions

All original 17 Cave named draft nodes are retained as distinct concepts and IDs, but their effect identities are no longer treated as historic caps. In particular, **Draconic Claws** = real raw melee damage while **Stone Claws** = bounded native natural attack knockback; **Draconic Scales** = armor while **Stonehide** = armor toughness; **Draconic Talons** = actual pickaxe-claw block break throughput while **Tunneler** = existing stone athletics movement and **Deep Excavation** = conditional sustained manual digging. This avoids buying three undifferentiated damage, defense or mining nodes for the sake of a 100-rank target.

## Source and unresolved owner-review items

- Dragon Survival developer wiki (read as version-aware background, not byte-verified code): https://github-wiki-see.page/m/DragonSurvivalTeam/DragonSurvival/wiki/5.-Dragon-features and https://github-wiki-see.page/m/DragonSurvivalTeam/DragonSurvival/wiki/4.-Dragon-Species. The 1.21.1 datapack ability registry must be inspected in the actual pinned **2.0.74** JAR before activating native fireball/skin/spin/claw interactions.
- Relevant TMM rules: owner diet issue #444 (current raw/cooked/charred 1/0.5/0.25 default, source-aware cooking), archived demonic subtype classification (Goety/Occultism per-personal action only) and source-locked ore gating (notably uranium). The raw-mob-meat taxonomies and thermal output need no duplicate Path implementation.
- **Major source holds:** exact existing Cave Sturdy Skin effect, real cave spin hit event, tool-slot durability hook, Cave Athletics strength stacking, uranium allowlist for Vein Sense, eligible infernal spell secondary IDs, and risk of dense underground speed bonus stacking. Gate or redesign any unsupported feature before it becomes purchasable.
