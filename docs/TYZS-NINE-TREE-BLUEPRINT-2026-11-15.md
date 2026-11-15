# Tyz Skills — nine-tree final-direction blueprint (2026-11-15)

> **Status: CONCEPT / REVIEW.** This is a proposed complete *catalog and dependency design*, not active Tyz JSON, tested attributes, native skills, or approved numerical balance. Do not stage it automatically in a client build. It replaces the **six-category conceptual layout** (one Core + five form categories) with **nine new custom categories**, while preserving the separate four existing Tyz default categories until explicitly changed. A separate machine-readable companion file enumerates candidate IDs.

## 1. Scope and canonical progression rules

The nine custom categories are **Core Magic; Cave Dragon; Sea Dragon; Forest Dragon; Vampire; Werewolf; Lich; Hunter; Monk**. These are one shared Tyz XP/SP wallet, not nine currencies. The three Dragon categories replace the old single planned `tmm_dragon` category; the Hunter category is new.

- **XP source:** only cross-mod magic-practice MXP dispatcher, not vanilla XP or Tyz's native activity XP. Every effective **1,000 MXP** = one Tyz level = one SP. Ordinary awards divide by `1.2^P` where `P` is lifetime points earned from level progression; first rituals / first bosses and authorized fixed repeats obey the existing exceptions. Respec does not rewind `P`.
- **Native ownership:** Vampirism/Werewolves, Dragon Survival, Forbidden Magic/JAWR, Goety, and the custom Monk faction retain real transformations, form abilities, native advancement, feeding, death and cures. Tyz ranks enhance or add carefully bounded effects *after* the native permission has been earned, never simulate acquisition.
- **Eligibility:** server-authoritative form snapshots from Transformation Compat, not merely a tag or visible tab. Hunter is exclusive; dragon is one of three; Lich applies to one lichdom, Archlich, and a valid Dracolich. Monk requires its real native faction and equipment discipline; cross-faction coexistence is not assumed.
- **Allowed hybrids:** valid Dracolich gets one dragon tree + the one shared Lich tree. Archlich gets one shared Lich tree, not two duplicate purchases. Abomination remains excluded from combined-tree design until base hybrid physiology is live-tested; a hypothetical allowed future Abomination may reference its separately valid components only after arbitration. No invalid hybrid can acquire ranks by merely buying Tyz nodes.
- **Ranks on cure:** purchased ranks persist as dormant investments; incompatible-form effects stop immediately. No automatic refunds or deletions. A player can respec through validated Tyz refund rules. Voluntary Hunter replacement explicitly suspends Hunter effects.
- **Tyz limitations:** normal custom categories are global, not per-player hidden. Keep all old default category definitions in the full replacement category JSON; **4 default + 9 custom = 13 total categories** unless old defaults are separately retired. Category visibility is not permission. A later client mask might hide unfamiliar tabs, but purchase/refund enforcement belongs to the server.
- **Purchase integrity:** enforce native state, node-specific stage, and outside milestones with the server's Tyz `PurchasePre` and `RefundPre` gates, plus an authorized direct-grant reconciliation path. Tyz bulk-buy checks a pre-event once: put external gates at **node boundaries**, not mid-node rank thresholds. One rank in a predecessor is sufficient as a native node prerequisite; advanced stages additionally require real native milestones.
- **Power ceiling:** talents improve forms without replacing Chaos Guardian, Draconic Evolution, ProjectE or Gem Armor. No creative flight, global creature pacification, immortality for free, protected-block mining bypass, infinite energy, or unlimited combat evasion.

**Cross-reference:** [original primary Tyz design PR #27](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/27), [Core Magic preview PR #33](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/33), [Monk live fixes and complete native skeleton PR #71](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/71), [MXP source rules](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/design/universal-mxp-progression-registry/docs/MAGIC-MXP-PROGRESSION.md), [transformation target PR #77](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/77).

## 2. Tier pattern and purchasing model

These are *design stages*, not promises of specific form levels; map stages to real installed-mod progressions before coding:

| Stage | Eligibility | Typical purchase shape |
|---|---|---|
| Entry | Own the actual valid form/faction; no arbitrary quest gate | Repeatable, low-impact ranks, typically 3–10 ranks/node at 1 SP/rank |
| Advanced | Relevant native mid-progression (or Monk rank/ability license) and previous node rank >=1 | 3–5 ranks/node; utility / control / specialized combat |
| Mastery | Real native high progression, verified achievement where relevant, and previous node rank >=1 | 1–3 ranks/node or one-on-cooldown active; meaningful but bounded |
| Tree capstone | Verified final native stage plus appropriate late-game milestone | One 2–5 SP purchase, *provisional* price, test before acceptance |

There is **no universal rule that every skill has 99 ranks**. The four existing Core Magic preview traits already reserve 99 ranks and should be preserved/rebalanced rather than overwritten. The Monk's source-backed native one-off permissions and finite affix rarities should never be inflated to 99 generic ranks. Every proposed stage is separately purchasable; no 99/99 prerequisite. Suggested costs and percentages are not approved balance and remain unencoded.

## 3. Nine categories, complete proposed skill families

### Core Magic — `tmm_magic_core`

Always available. Keep the original 9 families; this is the cross-mod baseline, not a new exclusive mage class.

| Family | Effect contract | Current status |
|---|---|---|
| **Reservoir** | Max eligible personal resource capacity; not machine tanks | Existing 99-rank preview; native attributes/adapters |
| **Wellspring** | Personal resource regeneration; never passive world generators | Existing 99-rank preview; native attributes/adapters |
| **Thrift** | Real positive-cost reduction for personal casts, with floor and cross-mod dedupe | Existing 99-rank preview; debit-event adapters |
| **Potency** | Scalable offensive/healing magic effect, damage-tag and owner aware | Existing 99-rank preview; native attributes/context |
| **Quickening** | Personal cast/activation speed only | Future per-mod cast timing adapter |
| **Projection** | Valid spell-target range, not AoE radius | Future range/raycast adapter |
| **Persistence** | Temporary effects and eligible controlled summons duration | Future duration adapter |
| **Conjuration** | Strengthen personally owned summoned allies with bounded stats | Future owner-attribution adapter |
| **Warding** | Mitigate confirmed magical damage only | Future damage-tag listener |

Potential extensions: Expansion/AoE (only after source-verified area hooks); Artifice (only after real crafter attribution); Ritual mastery (must not erase native minigames). These are **not** launch commitments.

**Structure:** four foundation roots (Reservoir, Wellspring, Thrift, Potency), three spellcraft specialists (Quickening, Projection, Persistence), and two independent specialist roots (Conjuration, Warding). A family can have staged I→V successor nodes with one prior rank as prerequisite and verified milestones for late stages. Do not make a mage buy unrelated foundations to reach Warding. The existing four 99-rank preview skill files are the starting point, **not** an alternative separate wallet.

### Cave Dragon — `tmm_dragon_cave`

**Eligibility:** verified cave dragon state; no other dragon species. **Role:** Excavator, armored bruiser, and underground apex predator.

| Lane | Entry node | Advanced node | Mastery node |
|---|---|---|---|
| **Claw & Fang** | **Stone Claws:** Scale unarmed/claw damage only in dragon body; no weapon-wide bonus. | **Crushing Bite:** Bounded armor/knockback pressure on confirmed bite hits. | **Seismic Pounce:** Cooldown-limited landing shockwave against entities; terrain damage off by default. |
| **Stone Scales** | **Stonehide:** Incremental natural armor; respect equipment stacking caps. | **Earthbound Poise:** Reduce knockback and fall damage while grounded. | **Mountain Bastion:** Short defensive stance with a resource cost and meaningful cooldown, not permanent immunity. |
| **Excavation** | **Tunneler:** Improve legal claw-mining speed; preserve vanilla/tool harvest checks. | **Vein Sense:** Short-range, timed ore awareness without unrestricted x-ray or chunk scans. | **Deep Excavation:** Controlled multi-block excavation; count actual blocks, honor claims and server limits. |
| **Cavern Instinct** | **Gloom Eyes:** Improve dark navigation without permanent global spectral vision. | **Echo Sense:** Detect nearby moving creatures underground on a cooldown. | **Deep Sovereignty:** Situational breath or body-power enhancement underground; preserve native dragon-breath identity. |

**Capstone — Heart of the Mountain:** After late native dragon progression, briefly combine armor and excavation benefits; no Chaos Guardian-tier gear replacement.

**Special rules:** No cross-species dragon purchases or free dragon growth. Mining benefits must not bypass protected ores, world claims or late-game materials.

### Sea Dragon — `tmm_dragon_sea`

**Eligibility:** verified sea dragon state; no other dragon species. **Role:** High-mobility aquatic spellfighter and ocean explorer.

| Lane | Entry node | Advanced node | Mastery node |
|---|---|---|---|
| **Tideshaping** | **Current Rider:** Improve movement control in water, including currents. | **Tidal Strike:** Enhance wet-target combat and native aquatic attacks, not generic damage. | **Undertow:** Cooldown-limited pull/slow applied to legal targets without moving world water blocks. |
| **Abyssal Defense** | **Pressure Adaptation:** Reduce underwater environmental penalties. | **Brine Scales:** Situational protection while submerged; no blanket armor bonus on land. | **Deepwater Refuge:** Bounded underwater healing or defense with a meaningful resource/cooldown cost. |
| **Swimmer's Grace** | **Sleek Form:** Increase underwater speed, capped against sprint/exotic gear. | **Slipstream:** Short burst dodge in water with collision safety. | **Ocean Leap:** Leap from water and control reentry without free flight. |
| **Marine Communion** | **Ocean Forager:** Enhance valid fish/seafood gathering and food utility without infinite loot. | **Friendly Waters:** Improve interactions with eligible nonhostile aquatic mobs; no boss pacification. | **Abyssal Explorer:** Safe short-duration navigation/sensing in deep oceans. |

**Capstone — Tide Sovereign:** Late-game, time-limited aquatic combat and mobility surge; must be distinctly weaker on land.

**Special rules:** Retain Sea Dragon's existing sushi/magical-food compatibility fixes as separate diet logic. No free permanent underwater invulnerability or world fluid griefing.

### Forest Dragon — `tmm_dragon_forest`

**Eligibility:** verified forest dragon state; no other dragon species. **Role:** Nature guardian, mobile skirmisher and ecological support.

| Lane | Entry node | Advanced node | Mastery node |
|---|---|---|---|
| **Verdant Vitality** | **Green Recovery:** Small recovery benefit in living biomes, not in all dimensions. | **Living Bark:** Situational natural defense near vegetation, with stacking caps. | **Elder Grove:** Time-limited regeneration/support pulse, costing dragon resource. |
| **Thorn & Root** | **Bramble Claw:** Add a bounded nature-themed status to confirmed claw attacks. | **Entangling Breath:** Native breath enhancer applying short slows only where source breath supports it. | **Thornstorm:** Cooldown-limited small-area crowd control with no world block destruction. |
| **Canopy Mobility** | **Canopy Stride:** Improve movement through leaves/forests; no generic always-on sprint power. | **Rooted Landing:** Safer falls and precision landings, capped by equipment. | **Sylvan Bound:** Short leap between safe landing points; collision and claim checks. |
| **Forest Fellowship** | **Fey Courtesy:** Conditional neutrality with selected eligible fae mobs, excluding hostile bosses. | **Harvest Harmony:** Quality-of-life harvest/replant assistance without resource multiplication. | **Grove Guardian:** Bounded support bonus to nearby allies and pets in natural environments. |

**Capstone — Elderwood Heart:** Late native progression grants a timed defensive/support state in forest biomes.

**Special rules:** Do not turn Forest Dragon neutrality into global creature pacification. Sourceberries and modded enchanted golden carrot diets are separate food tags, not a skill gate.

### Vampire — `tmm_vampire`

**Eligibility:** unified vampire state recognized by transformation bridge; retains both native provider progressions. **Role:** Blood economy, predatory stealth and undead survival.

| Lane | Entry node | Advanced node | Mastery node |
|---|---|---|---|
| **Bloodcraft** | **Efficient Feeding:** Improve real blood gained from eligible feeding transactions; cannot create blood from empty actions. | **Deep Reservoir:** Increase one authoritative usable blood capacity, not two independent HUD pools. | **Crimson Renewal:** Trade a real measured blood debit for bounded emergency healing. |
| **Nightstalker** | **Nocturnal Vision:** Better low-light perception with native-sunlight penalties untouched. | **Shadow Stalker:** Short stealth advantage in darkness; avoid invisibility in full daylight. | **Bat Mastery:** Improve native bat-form controls/efficiency after that native ability is acquired. |
| **Predation** | **Fang Training:** Scale only verified vampire bite/melee attacks. | **Measured Feast:** Limited post-kill blood recovery from eligible kills, with transaction deduplication. | **Blood Duelist:** Focused single-target bonuses under low blood; prevent intentional zero-cost loops. |
| **Undead Constitution** | **Pale Resilience:** Bounded poison/disease-type mitigation where the source system allows it. | **Sunshroud:** Short resource-funded sunlight resistance; never erase core daylight tradeoffs. | **Elder Presence:** Bounded defensive/control aura with cooldown, not full charm immunity. |

**Capstone — Blood Regent:** Blood-funded high-tier predatory stance requiring advanced native vampire progression.

**Special rules:** One canonical blood owner; no dual-pool double gains or duplicate feeding rewards. Cure suspends effects without deleting invested ranks; JAWR blood HUD bug must be fixed independently.

### Werewolf — `tmm_werewolf`

**Eligibility:** unified werewolf/lycanthrope state; exact physiology owner remains unselected. **Role:** Hunter, sprinter, brawler and pack leader.

| Lane | Entry node | Advanced node | Mastery node |
|---|---|---|---|
| **Hunting Senses** | **Keen Scent:** Locate nearby tracked targets at limited range, not players through claims or walls without rule. | **Blood Trail:** Brief tracking of wounded mobs with privacy/multiplayer restrictions. | **Apex Tracker:** Improved tracking against eligible dangerous mobs, cooldown limited. |
| **Feral Might** | **Rending Claws:** Scale claw damage in valid wolf/beast form, respecting weapon modifiers. | **Savage Momentum:** Short stacking melee bonus after actual hits, capped and reset out of combat. | **Primal Fury:** Timed burst consuming native appropriate resource or applying fatigue. |
| **Moonrunner** | **Predator's Stride:** Bonus mobility in valid wolf form. | **Pouncing Leap:** Targeted controlled leap with landing safety. | **Lunar Chase:** Short chase burst under moon conditions with weaker daytime counterpart. |
| **Pack Instinct** | **Pack Sense:** Detect/coordinate eligible allied wolves and player-owned companions. | **Protect the Pack:** Bounded defense near allies, no pet invulnerability. | **War Howl:** Cooldown-limited temporary group buff; only one stacking instance per leader. |

**Capstone — Moon Apex:** Late-form transformation mastery for a short ferocious burst, preserving silver/other verified weaknesses.

**Special rules:** Do not commit to Werewolves vs JAWR hunger, model or forced-moon authority before the physiology audit. Natural form-change progression remains native, not purchased again in Tyz.

### Lich — `tmm_lich`

**Eligibility:** verified Goety or JAWR/Forbidden Magic lich state; also Archlich and valid Dracolich. **Role:** Soul economy, necromancy, controlled immortality.

| Lane | Entry node | Advanced node | Mastery node |
|---|---|---|---|
| **Soul Economy** | **Soul Conservation:** Lower eligible personal necromancy spell costs, not global ritual/machine cost. | **Soul Reserve:** Bounded native personal soul/Arca resource capacity where extensible. | **Soul Treasury:** Small high-level sustain on committed necromantic actions, no passive self-funding loop. |
| **Death Magic** | **Grave Touch:** Scalable effect on verified lich necrotic spell/melee attacks only. | **Lingering Curse:** Bounded duration of attributable lich debuffs, not a global potion multiplier. | **Dread Conduit:** Strengthen source-verified lich spell lines without unlocking forbidden research. |
| **Undead Host** | **Bone Captain:** Modest bonus to personally owned undead servants. | **Grave Regiment:** Improved servant coordination/leash and bounded count efficiency. | **Death Host:** Late temporary enhancement to current owned minions with hard entity budget. |
| **Soul Preservation** | **Cold Persistence:** Limited tolerance of wither/undead hazards, preserving explicit weaknesses. | **Anchor Attunement:** Improve diagnostics or controlled anchor maintenance; never bypass native phylactery/Arca cost. | **Final Passage:** Minor post-rescue recovery *after* an authoritative successful resurrection; no extra death-save trigger. |

**Capstone — Sovereign of Ash:** High-tier necromantic command stance requiring advanced lich progression and stable anchor ownership.

**Special rules:** Goety and Forbidden Magic advancement remain separate native tracks; this Tyz category is shared once. For Archlich: phylactery rescue first, Arca only on failed phylactery; never duplicate saves. For Dracolich, the same one Lich Tyz category may coexist with exactly one dragon species.

### Hunter — `tmm_hunter`

**Eligibility:** verified Vampirism hunter faction; mutually exclusive with supernatural states. **Role:** Prepared anti-supernatural specialist, scouting and counterplay.

| Lane | Entry node | Advanced node | Mastery node |
|---|---|---|---|
| **Investigation** | **Monster Lore:** Reveal eligible hostile supernatural type/weakness cues after actual observation. | **Quarry Mark:** Mark one tracked supernatural target, with limited range and duration. | **Master Pursuit:** Improved tracking of wounded or fleeing marked targets, respecting concealment rules. |
| **Weapons Training** | **Silver Discipline:** Bounded bonus using verified anti-supernatural material/weapon tags. | **Weak-Point Strike:** Conditional bonus after observing a marked target, not global crit multiplier. | **Slayer's Technique:** Cooldown-limited counterattack against specific supernatural telegraphs. |
| **Wards & Remedies** | **Steadfast Mind:** Limited resistance to eligible charm/fear/supernatural debuffs. | **Warding Kit:** Improve duration/efficacy of approved consumable anti-monster items, not free production. | **Hexbreaker:** Resource-cost/cooldown supernatural cleanse of eligible effects; not boss dispel. |
| **Ambush & Counter** | **Fieldcraft:** Short preparation-speed and stealth bonuses in natural environments. | **Consecrated Snare:** Craft/use a bounded anti-monster trap; ownership and claim-safe placement. | **Spell Intercept:** High-tier, timed interrupt window for verified hostile supernatural casts; bosses exempt unless explicitly whitelisted. |

**Capstone — Master Slayer:** Late hunter-only preparation stance combining marks and counterattacks against supernatural mobs.

**Special rules:** Hunter purchases never require vampire, wolf, or lich powers. Voluntary hunter-replacement ceremony suspends these effects and retains historical ranks for later valid return.

### Monk — `tmm_monk`

**Eligibility:** valid tmm_monk native faction plus equipment-discipline condition on use; tier licenses from native Monk tree. **Role:** Unarmed martial arts, unarmored body, motion and hand-harvest specialization.

| Lane | Entry node | Advanced node | Mastery node |
|---|---|---|---|
| **Martial Body** | **Empty-Hand Force:** Repeatable empty-hand damage bonus beyond the native +1 Open Palm unlock; stacking cap. | **Flowing Counter:** Bounded counterattack after a verified native Guarding Palm/parry unlock. | **Spirit Bolt Mastery:** Boost native spirit projectile after its native rank-6 license, not duplicate its unlock. |
| **Conditioning** | **Natural Poise:** Scale unarmored protection beyond native Unarmored Poise +2; never scale equipped armor. | **Hunger Discipline:** Bounded food/exhaustion efficiency after native license; do not remove all hunger. | **Fire & Blast Tempering:** Limited environmental damage mitigation after both matching native licenses. |
| **Footwork** | **Body Speed:** Repeatable speed under no conventional gear discipline; native Fleet Footwork remains a one-off. | **Balanced Landing:** Fall/knockback control; native Step Assist remains a one-off in native tree. | **Glide Control:** Improve native momentum gliding only after rank-15 native unlock; never creative flight. |
| **Hands as Tools** | **Barehand Efficiency:** Repeatable hand-mining speed; native Stone/Iron/Diamond/Netherite Fist unlock legal harvest tiers. | **Fortunate Hands:** Bounded fortune-like bonus only after native rank-13 license and legitimate drop event. | **Looting Palm:** Bounded unarmed looting after native rank-9 license; no creature-drop duplication. |

**Capstone — Empty-Hand Paragon:** After native rank 20 and late achievements, an equipment-restricted burst of unarmed prowess; avoid eclipsing Draconic/Gem armor.

**Special rules:** Preserve the Monk native full skeleton (50 nodes, 6 pilot nodes) and 47 Monk-specific Tyz candidates as source inventories, NOT 47 new compulsory live skills. First Sunset and native mastery from barehand breaks, kills and first foods stay separate from MXP global awards. Step Assist after Fleet Footwork, not at rank one. Tool-free harvest tiers remain native permission unlocks.


## 4. Native ability versus Tyz purchase: no double purchase

| Native provider | Native tree or state remains responsible for | Tyz is responsible for |
|---|---|---|
| Dragon Survival | Species choice, growth, actual scale/body, innate movement/breath, food policy | Bounded species-specific improvements to existing movement, physical, harvesting, combat and breath |
| Vampirism + JAWR | Both underlying vampire progression paths, unified blood/sun/feeding, form unlocks | Blood economy *against the one canonical pool*, physical/stealth modifiers, ability refinements |
| Werewolves + JAWR | Both native progressions, forced/voluntary shape and authoritative wolf physiology (not decided yet) | Beast-form-only strength/motion, senses, limited pack buffs |
| Forbidden Magic + Goety | Two distinct lich progressions; phylactery and Arca mechanics | Personal soul efficiency, undead summons, controlled offensive/status upgrades |
| Vampirism Hunter | Hunter profession/faction, native perks and equipment access | Anti-monster tactical specialization and gear-use enhancement; never supernatural physiology |
| Custom Monk + Vampirism faction host | First Sunset, native 20-rank plan, one-off Step Assist/Stone Fist/Open Palm/etc, native hand-harvest licenses | Repeatable conditional stats, hand-loot traits and any bounded affix-like bonuses after native licenses |

**Monk exception:** PR #71 already defines a **50-node conceptual native Monk tree**, with six live data nodes for client QA. Its second skeleton contains **47 Monk-only Tyz candidates** (seven traits plus forty bounded Apotheosis affix conversions); those are research candidates, not extra compulsory skills. This document distills their gameplay into four Tyz lanes. Preserve existing native rank-one Open Palm (+1 barehand attack), Unarmored Poise (+2 armor), Stone Fist, and later Fleet Footwork→Step Assist. Native Monk mastery credit (legal barehand block breaks, actual spell-attributed breaks, combat, and first foods) remains independent of the Tyz MXP ledger. Avoid double-awarding the same event into the same wallet or repeating native unlocks in Tyz.

**Lich exception:** both native lichdoms remain earnable and stack into Archlich. One Lich Tyz category covers both (source-specific nodes may require the matching native provider). When two exist, phylactery saves first; Goety Arca is fallback only if phylactery cannot rescue. A Tyz rank cannot grant or reorder death saves.

## 5. Server adapters and verification statuses

No row in this blueprint should be mistaken for an already working mod hook. Implementations fall into four buckets:

1. **Native attribute mapping** (low risk after ID check): attack, movement, health/armor, confirmed spell attributes. Changes must be reverted when form/equipment conditions fail.
2. **Shared event adapter** (medium): damage kinds, status effects, real drops, real resource debit, bounded owner-attributed summons, actual world/biome conditions.
3. **Exact-provider adapter** (high): dragon breath/body, Vampire dual blood pool, JAWR/Werewolves physiology and shape, phylactery/Arca death events, Monk special techniques and Apotheosis affixes.
4. **New activated skill** (highest): custom dash, scan, shockwave, traps, control, defensive stance; requires cooldown, cost, anti-griefing, particles/UI and multiplayer replication.

Do not implement high-risk abilities as permanent passive buffs just because Tyz JSON cannot express the activation. Confirm dedicated-server and client behavior against exact 1.21.1 pinned binaries.

### Mechanics that must never be silently overridden

- Native special diets and magical-food exceptions, finite food resource costs and double-source vampire feeding.
- One canonical per-resource debit and one MXP award for Ars/Iron's shared mana; other costs normalized by original reference capacities.
- Quest/resource gates for endgame; no mining talent bypassing disabled ores or Dragon Survival harvest checks.
- Transform/cure, dual progression history, death-rescue arbitration, client HUD and body renderer.
- Claim protections, server performance ceilings, safe teleport destination and attribution of player-created summons.
- Silver/sunlight/other native vulnerabilities remain meaningful; a Tyz node is never a blanket no-drawback transformation.

## 6. Build sequence and acceptance cases

**Phase A — concept-to-config:** Add nine categories, preserve four Tyz defaults, validate 13-tab pagination and localization; create inactive/skeleton node IDs. Handle old planned `tmm_dragon` gracefully: no automatic one-to-three point duplication; if any actual players own generic dragon ranks, make an explicit migration/refund map and back up saves first.

**Phase B — narrow playable vertical slice:** Existing four Core Magic rank files + **one entry node per each of eight form trees**. Each must be visible, gated by real provider state, spend exactly one wallet, change an actually measured stat/effect in the correct context, and deactivate on cure/relog. Prioritize simple measurable effects: Cave Stonehide, Sea Sleek Form, Forest Green Recovery, Vampire Efficient Feeding *after blood repair*, Werewolf Predator's Stride *after shape authority*, Lich Bone Captain *after ownership validation*, Hunter Monster Lore, and Monk Empty-Hand Force *after native tree purchase repair*.

**Phase C — multi-lane progression:** Add three lane tiers, chosen mid/final native milestones, refunds, capstones and class-specific adapters. Never certify based only on clean JSON/compilation/server boot. Keep all new skills flagged as conceptual until individually implemented and client-verified.

**Phase D — cross-form and exploit QA:** Verify every allowable and disallowed combination, all acquisition orders, logout/respawn/dimension travel, native skill purchases, bulk buys, direct grants, refund predecessor chains, packet sync, time-of-day/environment, claims, anti-grief rules, pet attributions, hunger/blood, food exceptions and resource loops. Record separate acceptance per skill; a passing Tyz GUI does not make the effects work.

### Explicit decisions not yet locked

- Exact number of points desired in a typical playthrough: the 1.2^P MXP slowdown makes exhaustive maxing unrealistic; decide whether distinct builds are intentionally permanent or normal respec is expected.
- Hunter-vs-Monk faction coexistence: current authoritative state/provider may not support it; design as independent categories but **never** allow combined use until transition policy approves.
- Which vampire and werewolf source powers should receive separate source-locked sub-nodes versus one shared equivalent.
- Specific native stage and boss gates, rank caps and individual percentages after source/version inventory and actual player timings.
- Whether to retain all four default Tyz categories unchanged indefinitely, or later curate them after no-overlap review.

## 7. What this commit does *not* do

This is a design-only catalog and machine-readable sketch. It does **not** create active Tyz skills, mutate the level pool, add custom mod code, mark transformations compatible, fix the Monk UI or vampire blood pool, alter a diet, add a new faction, or change a release/build manifest. The existing staged Core Magic and Monk implementation remain where they are. Content is intended for review and later mechanically checked conversion into runtime config.
