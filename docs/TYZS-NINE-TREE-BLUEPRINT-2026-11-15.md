# Tyz Skills — nine-tree final-direction blueprint (2026-11-15)

> **Status: CONCEPT / REVIEW.** This is an *identity-first predecessor concept*, not active Tyz JSON or tested gameplay. **The new [all-nine-tree strength and caps pass](TYZS-STRENGTH-AND-NINE-TREE-FIRST-PASS-2026-11-15.md) supersedes prototype rank guesses and supplies explicit tier/cap/point assignments.** Do not stage it automatically in a client build. It replaces the **six-category conceptual layout** (one Core + five form categories) with **nine new custom categories**, while preserving the separate four existing Tyz default categories only **until all 32 built-in skills have been inventoried, reassigned and independently validated**. A separate machine-readable companion file enumerates candidate IDs.

## 1. Scope and canonical progression rules

The nine custom categories are **Core Skills; Cave Dragon; Sea Dragon; Forest Dragon; Vampire; Werewolf; Lich; Hunter; Monk**. These are one shared Tyz XP/SP wallet, not nine currencies. The three Dragon categories replace the old single planned `tmm_dragon` category; the Hunter category is new.

- **XP source:** only cross-mod magic-practice MXP dispatcher, not vanilla XP or Tyz's native activity XP. Every effective **1,000 MXP** = one Tyz level = one SP. Ordinary awards divide by `1.2^P` where `P` is lifetime points earned from level progression; first rituals / first bosses and authorized fixed repeats obey the existing exceptions. Respec does not rewind `P`.
- **Native ownership (new target 2026-11-15):** JAWR alone owns vampire/lycanthrope physiology; Dragon Survival owns dragons; Forbidden Magic/JAWR and Goety own supported lich states; standalone Hunter and Monk mods own their independent native permissions. TeamLapen Vampirism and Werewolves are slated for retirement after standalone migrations, and must not be used as future tree providers. Tyz boosts eligible skills after actual native milestones. Tyz ranks enhance or add carefully bounded effects *after* the native permission has been earned, never simulate acquisition.
- **Eligibility:** server-authoritative form snapshots from Transformation Compat, not merely a tag or visible tab. Hunter is a standalone class whose cross-form compatibility is undecided, not automatically a Vampirism-exclusive faction; dragon is one of three; Lich applies to one lichdom, Archlich, and a valid Dracolich. Monk requires its real native faction and equipment discipline; cross-faction coexistence is not assumed.
- **Allowed hybrids:** valid Dracolich gets one dragon tree + the one shared Lich tree. Archlich gets one shared Lich tree, not two duplicate purchases. Abomination remains excluded from combined-tree design until base hybrid physiology is live-tested; a hypothetical allowed future Abomination may reference its separately valid components only after arbitration. No invalid hybrid can acquire ranks by merely buying Tyz nodes.
- **Ranks on cure:** purchased ranks persist as dormant investments; incompatible-form effects stop immediately. No automatic refunds or deletions. A player can respec through validated Tyz refund rules. Any native change that invalidates actual Hunter eligibility immediately suspends Hunter effects, without assuming a forced faction exclusivity rule.
- **Tyz limitations:** normal custom categories are global, not per-player hidden. Keep all old default category definitions in the full replacement category JSON; **4 default + 9 custom = 13 temporary categories**, reducing to **9 total** after the stock-skill migration and safe default-category retirement. Category visibility is not permission. A later client mask might hide unfamiliar tabs, but purchase/refund enforcement belongs to the server.
- **Purchase integrity:** enforce native state, node-specific stage, and outside milestones with the server's Tyz `PurchasePre` and `RefundPre` gates, plus an authorized direct-grant reconciliation path. Tyz bulk-buy checks a pre-event once: put external gates at **node boundaries**, not mid-node rank thresholds. One rank in a predecessor is sufficient as a native node prerequisite; advanced stages additionally require real native milestones.
- **Power ceiling — OWNER OVERRIDE (2026-11-15):** **Monk is the overall combat apex. At full Monk progression, a player must be able to solo the genuine Chaos Guardian without worn equipment or weapons**, using legitimate encounter mechanics. This supersedes the earlier generic ban on surpassing Draconic/Gem equipment in *gearless combat*. Other classes may be nearly as good in **one or two designated specialties**, and should be **significantly worse in most other combat/adventuring areas**; narrow powers such as Dragon spell capacity, Hunter quarry combat, Werewolf sustain and Sea Dragon Luck can still exceed Monk on their individual measurement. No creative-mode invulnerability, cheese/phase skips, free immunity, infinite energy or protected mining bypass.

**Cross-reference:** [original primary Tyz design PR #27](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/27), [Core Skills preview PR #33](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/33), [Monk live fixes and complete native skeleton PR #71](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/71), [MXP source rules](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/design/universal-mxp-progression-registry/docs/MAGIC-MXP-PROGRESSION.md), [transformation target PR #77](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/77).


### Updated canonical balance handoff

The **current first-pass strength/tier and caps source of truth** is [TYZS-STRENGTH-AND-NINE-TREE-FIRST-PASS-2026-11-15.md](TYZS-STRENGTH-AND-NINE-TREE-FIRST-PASS-2026-11-15.md), plus [machine-readable 164-entry budget](tyzs/nine-tree-strength-first-pass-v1.json). Every proposed family has a tier (5/10/20/50/100% per rank), cap (5/10/25/50 except one-off capstones), per-rank SP price, and notes for non-mana conversion. These values **supersede the prototype 10/5/3-rank node suggestions** in the older machine catalogue; none is yet live.



### Apex and specialist benchmark (owner decision, 2026-11-15)

The full benchmark and limitations are in [nine-tree strength and caps design](TYZS-STRENGTH-AND-NINE-TREE-FIRST-PASS-2026-11-15.md#2a-global-endgame-ceiling--monk-first). A **fully maxed Monk** (native+Tyz, legitimate late game) should be able to win an actual **solo Chaos Guardian** fight with **empty armor/Curios/worn gear slots and no weapons**. The fight must remain dangerous and source-accurate: hostile attacks, movement demands, crystal/shield or phase restrictions, and completion all matter. Test support, spellcasting implements and non-worn consumables are separately specified later; no one gets permission to bypass the Guardian, and no rejected Monk innate Spirit Bolt is reintroduced. This is a target, **not yet implemented or tested**.

**Differentiation rule:** Every other tree is allowed a near-Monk niche or two, but loses clearly in most overall combat capacities. Sea Dragon has a deliberate high-end **Abyssal Fortune** niche: **Extreme (+100% of a defined nonzero Luck benchmark per rank), 50 ranks**, to be wired only into source-supported luck systems. This is not a guaranteed global 50-fold loot multiplier or a boss drop gate bypass. Monk is best *overall*, not simultaneously the best raw magician, best crop farmer, luckiest character and best anti-undead weapon specialist.

## 2. Tier pattern and purchasing model

These are *design stages*, not promises of specific form levels; map stages to real installed-mod progressions before coding:

| Stage | Eligibility | Typical purchase shape |
|---|---|---|
| Entry | Own the actual valid form/faction; no arbitrary quest gate | Standard cap 5/10/25/50 depending on effect; usually 1 SP/rank, physical Dragon talents may cost 2 |
| Advanced | Relevant native mid-progression (or Monk rank/ability license) and previous node rank >=1 | Standard 5/10/25/50 caps; verified native milestones, bounded utility/control |
| Mastery | Real native high progression, verified achievement where relevant, and previous node rank >=1 | Usually 5 or 10 ranks for strong mastery, 1 rank only for justified one-off unlocks |
| Tree capstone | Verified final native stage plus appropriate late-game milestone | One 5 SP provisional purchase, calibrated as qualitative Extreme impact, not unlimited passive +100% |

Within a form specialization lane the explicit dependency is **Entry → Advanced → Mastery**, requiring one rank in the directly preceding node. A proposed capstone requires at least **one Mastery purchase and one Advanced purchase in a different lane**, plus the verified native end stage and a real late milestone. These are intended edge rules, not native Tyz runtime wiring yet. 

Strength-tiers now standardize **Weak 5%, Middling 10%, Average 20%, Strong 50%, Extreme 100% per rank**, additive to a source-defined base, and **5/10/25/50** standard rank caps. See detailed [new strength allocation](TYZS-STRENGTH-AND-NINE-TREE-FIRST-PASS-2026-11-15.md). There is **no universal rule that every skill has 99 ranks**. **New decision (2026-11-15): the four initial Core Skills base skills compress from 99 ranks to 5 ranks, each giving 20% of its former rank-99 maximum attribute benefit (20/40/60/80/100% of cap).** Exact prior ceilings are preserved, with integer rounding where applicable. Later magnitude-tier skills may be 99-rank specialty bands if independently approved. The Monk's source-backed native one-off permissions and finite affix rarities should never be inflated to 99 generic ranks. Every proposed stage is separately purchasable; no 99/99 prerequisite. Suggested costs and percentages are not approved balance and remain unencoded.

## 3. Identity-first class rules (revised 2026-11-15)

**This section takes precedence over earlier prototype nodes when they conflict.** The catalog is a *skill design working sketch*, not a locked exhaustive 96-node ship list. Preserve one Tyz wallet and nine final tabs; do not add a separate common Dragon tab. See the `class_identity_rules` section of [machine-readable catalog](tyzs/nine-tree-catalog-v1.json) for binding considerations and source-classification policies.

| Tree | Where its skill points should be most effective | Hard design guardrails |
|---|---|---|
| **Core Skills** | Broad utility, baseline magic, combat, survival, crafting and exploration; home for stock skill relocations | Same stable category ID `tmm_magic_core`, one MXP wallet; not a mage-only category |
| **Hunter** | **Best specialized combat upgrades**: weapon/armor durability preservation, crossbow ammo conservation, and late anti-undead/demon/fae damage and survivability | Very little bonus magic power; **Hunter Tracking** Glowing on nearby qualified supernatural mobs only at very high advancement |
| **All three Dragons** | **Huge maximum personal mana** and **highest spell-power gain per SP** | **No dragon-exclusive mana regeneration bonuses**; Core Skills Wellspring remains a universal option. Every dragon gets armor, physical damage and legal harvest-speed skills, but these cost more SP per unit than a specialist's |
| **Cave Dragon** | Infernal/demonic spell power and eligible demonic servants | No blanket buff to all Goety production or summoning; effect classification must be verified |
| **Forest Dragon** | Fae magic and eligible fae servants; bonemeal effects, animal breeding and extra crop yields | Combat/harvest efficiency outside ecology remains expensive; preserve protected claims and harvest deduplication |
| **Sea Dragon** | **Provisional** tide, ice, storm and abyssal personal magic; aquatic control | Exact supported spell/effect schools still need mod-level research. Storm affinity does not imply free weather control |
| **Lich** | Blood magic, source-verified Neo Vitae/EvilCraft uses, necromancy | Blood specialization **also weakens the lich's own Botania/Roots nature magic**, measured after relevant shared Core Skills modifiers. Neither world machinery nor other players are globally nerfed |
| **Werewolf** | **Lots of extra hearts**, strong **health regeneration**, close combat, and nature magic | Health regen is not mana regen; retain native form rules, wolf weaknesses and resource feeding |
| **Vampire / Monk** | Existing blood/predation or equipment-independent cultivation identities | Their existing native progression/physiology ownership remains unchanged in this pass |

**Power budget, not numbers:** Hunters should buy substantially more effective monster-slaying survivability/offense per SP than generalists, dragons should buy far more spell power/mana per SP than any other class, and Werewolves should buy far more sustained health/health regeneration. Dragon armor/claw/harvest upgrades are deliberately **less** cost-efficient than specialist equivalents. Precise ranks, prices, caps, and damage/regen formulae await comparative late-game balance work, and nothing in this section is live code.

**Damage classification:** `undead`, `demon`, and `fae` need source-verified entity types/tags for mods in this pack. One creature in multiple groups is counted **once** for Hunter Bane/Tracking. High-level Hunter Glowing must use bounded per-player server scans/cooldowns and should not inadvertently mark players or disable intended nonquarry invisibility. **Crossbow conservation** operates on the actual projectile debit; fireworks and special ammo stay excluded until specifically tested. Equipment durability reduction operates on real durability loss (not free repair). Avoid source-based exploits against protected boss gear and no duplicate vanilla projectile refund.

**Nature and blood classification:** Only legitimate player-attributed blood/nature effects receive Lich potency modifiers. Neo Vitae and EvilCraft are *candidate providers* requiring actual hook checks, not a claim that every block/ritual belongs to a blood spell school. Botania and Roots are the target nature spell families; mana flowers, unattended processors and other world infrastructure are not spellcasting and must not be changed. Lich nature penalty must not be hidden by buying universal Potency. Werewolf nature bonuses and Forest fae bonuses likewise affect source-classified personal magic only.

**Forest legacy transfers:** Two existing Tyz `IMMUTABLE` skills, **Green Thumb** and **Shepherd’s Blessing**, now belong in the Forest Dragon migration proposal instead of Core Skills. Their original IDs, values and Java behavior should be preserved pending player testing, with explicit form eligibility and use-time gating. Additional Forest bonemeal/animal-breeding skills remain conceptual; moving existing skills alone does not guarantee all requested mechanics.

## 3A. Proposed tree families and provisional node sequences

### Core Skills — `tmm_magic_core`

Always available. This is the **broad universal** tree, including eligible original Tyz stock skills after relocation, plus these nine magic-specific families. The initial four active skill **source** files use **5 ranks × 20% of previous ceiling**, not 99 ranks.

| Family | Universal magic component | Implementation stage |
|---|---|---|
| **Reservoir** | Max eligible personal resource capacity; not machine tanks | Five-rank base prototype; native attributes/adapters |
| **Wellspring** | Personal resource regeneration; never passive world generators | Five-rank base prototype; native attributes/adapters |
| **Thrift** | Real positive-cost reduction for personal casts, with floor and cross-mod dedupe | Five-rank base prototype; debit-event adapters |
| **Potency** | Scalable offensive/healing magic effect, damage-tag and owner aware | Five-rank base prototype; native attributes/context |
| **Quickening** | Personal cast/activation speed only | Future per-mod cast timing adapter |
| **Projection** | Valid spell-target range, not AoE radius | Future range/raycast adapter |
| **Persistence** | Temporary effects and eligible controlled summons duration | Future duration adapter |
| **Conjuration** | Strengthen personally owned summoned allies with bounded stats | Future owner-attribution adapter |
| **Warding** | Mitigate confirmed magical damage only | Future damage-tag listener |

**Possible future expansions:** Expansion/AoE (only after source-verified area hooks); Artifice (only after real crafter attribution); Ritual mastery (must not erase native minigames). Do not copy a stock skill into both Core Skills and a specialist category, or let regular Core Skills stat growth erase specialist efficiency advantages.

### Cave Dragon — `tmm_dragon_cave`

**Identity:** Supreme high-capacity/high-potency dragon caster with armor, claws and harvest at premium SP prices; special emphasis on demonic magic.. **Eligibility:** verified cave dragon state; no other dragon species.

| Lane | Entry (concept) | Advanced (concept) | Mastery (concept) |
|---|---|---|---|
| **Claw & Fang** | **Stone Claws:** Scale unarmed/claw damage only in dragon body; no weapon-wide bonus. | **Crushing Bite:** Bounded armor/knockback pressure on confirmed bite hits. | **Seismic Pounce:** Cooldown-limited landing shockwave against entities; terrain damage off by default. |
| **Stone Scales** | **Stonehide:** Incremental natural armor; respect equipment stacking caps. | **Earthbound Poise:** Reduce knockback and fall damage while grounded. | **Mountain Bastion:** Short defensive stance with a resource cost and meaningful cooldown, not permanent immunity. |
| **Excavation** | **Tunneler:** Improve legal claw-mining speed; preserve vanilla/tool harvest checks. | **Vein Sense:** Short-range, timed ore awareness without unrestricted x-ray or chunk scans. | **Deep Excavation:** Controlled multi-block excavation; count actual blocks, honor claims and server limits. |
| **Infernal Attunement** | **Infernal Resonance:** Bonus to source-verified demonic/infernal personal magic, beyond shared dragon arcane power but not world rituals. | **Hellbound Command:** Strengthen eligible player-owned demonic servants and source-verified infernal spell interactions. | **Infernal Dominion:** Late specialization in demon-affiliated spell potency; strong, narrowly classified and gated. |

**Capstone — Heart of the Mountain:** After late native dragon progression, briefly combine armor and excavation benefits; no Chaos Guardian-tier gear replacement.

**Implementation conditions:** No cross-species dragon purchases or free dragon growth. Mining benefits must not bypass protected ores, world claims or late-game materials. No Cave Dragon mana regeneration talents; high-cost dragon armor, damage and legal harvest enhancements accompany outsized mana/spell power.

**Shared Dragon base (must still be added to this species' real tree):** huge personal max mana, very efficient spell potency, expensive scales/armor, expensive claws/physical damage and expensive legal harvest-speed talents; **zero dragon-exclusive magical regeneration**. The current four-lane prototype does **not** exhaustively enumerate these shared fundamentals, so it is not yet a finished dragon tree.

### Sea Dragon — `tmm_dragon_sea`

**Identity:** High-capacity/high-potency dragon caster of tides, cold, storms and deep-sea magic; aquatic mobility, extremely high Luck and defenses with costly generic combat/harvest nodes.. **Eligibility:** verified sea dragon state; no other dragon species.

| Lane | Entry (concept) | Advanced (concept) | Mastery (concept) |
|---|---|---|---|
| **Tideshaping** | **Current Rider:** Improve movement control in water, including currents. | **Tidal Strike:** Enhance wet-target combat and native aquatic attacks, not generic damage. | **Undertow:** Cooldown-limited pull/slow applied to legal targets without moving world water blocks. |
| **Abyssal Defense** | **Pressure Adaptation:** Reduce underwater environmental penalties. | **Brine Scales:** Situational protection while submerged; no blanket armor bonus on land. | **Deepwater Refuge:** Bounded underwater healing or defense with a meaningful resource/cooldown cost. |
| **Swimmer's Grace** | **Sleek Form:** Increase underwater speed, capped against sprint/exotic gear. | **Slipstream:** Short burst dodge in water with collision safety. | **Ocean Leap:** Leap from water and control reentry without free flight. |
| **Tidal & Abyssal Magic** | **Tidal Attunement:** Enhance verified water, ocean and cold-themed personal spell damage/effects, not arbitrary wet status. | **Stormcalling:** Enhance verified lightning/storm magic and aquatic control where supported; no unconditional world weather control. | **Abyssal Sovereignty:** High-level sea/abyssal spell focus, not an assumption that Occultism automatically counts as ocean magic. |

**Capstone — Tide Sovereign:** Late-game, time-limited aquatic combat and mobility surge; must be distinctly weaker on land.

**Implementation conditions:** Retain Sea Dragon's existing sushi/magical-food compatibility fixes as separate diet logic. No free permanent underwater invulnerability or world fluid griefing. Sea specialization is provisional until actual installed spells can be classified as tide/ice/storm/abyssal; avoid invented element IDs. No Sea Dragon mana regeneration talents; preserve baseline armor/damage/harvest-speed modules at premium prices.

**Shared Dragon base (must still be added to this species' real tree):** huge personal max mana, very efficient spell potency, expensive scales/armor, expensive claws/physical damage and expensive legal harvest-speed talents; **zero dragon-exclusive magical regeneration**. The current four-lane prototype does **not** exhaustively enumerate these shared fundamentals, so it is not yet a finished dragon tree.

**Sea-only extra specialization:** **Abyssal Fortune**, Extreme ×50. Luck is a very high personal strength that must be mapped to actual source-supported loot/fishing/roll consumers, not used to duplicate protected boss drops or create loot from nothing.

### Forest Dragon — `tmm_dragon_forest`

**Identity:** High-capacity/high-potency fae dragon caster with ecological control, crop bounty, breeding and bonemeal effects; generic combat/harvest ranks cost a premium.. **Eligibility:** verified forest dragon state; no other dragon species.

| Lane | Entry (concept) | Advanced (concept) | Mastery (concept) |
|---|---|---|---|
| **Verdant Vitality** | **Green Recovery:** Small recovery benefit in living biomes, not in all dimensions. | **Living Bark:** Situational natural defense near vegetation, with stacking caps. | **Elder Grove:** Time-limited regeneration/support pulse, costing dragon resource. |
| **Thorn & Root** | **Bramble Claw:** Add a bounded nature-themed status to confirmed claw attacks. | **Entangling Breath:** Native breath enhancer applying short slows only where source breath supports it. | **Thornstorm:** Cooldown-limited small-area crowd control with no world block destruction. |
| **Fertility & Bounty** | **Germination Touch:** Enhance actual bonemeal application efficiency/area/yield without free unbounded creation of growth. | **Breeder's Blessing:** Improve legal animal breeding/cooldown or food efficiency via real pair interaction; never spawn animals without parents. | **Harvest Bounty:** Increase real crop harvest outcomes with explicit per-block credit and cooldown/caps; preserve claim and mature crop checks. |
| **Fae Affinity** | **Fey Courtesy:** Improve source-verified fae-aligned personal magic effects and fae relations; no universal mob pacification. | **Fey Conduit:** Specialize fae-aligned spell potency and owned fae summons at higher benefit per SP. | **Grove Guardian:** Bounded fae-aligned personal magic and allied-support synergy without taking over world production. |

**Capstone — Elderwood Heart:** Late native progression grants a timed defensive/support state in forest biomes.

**Implementation conditions:** Do not turn Forest Dragon neutrality into global creature pacification. Sourceberries and modded enchanted golden carrot diets are separate food tags, not a skill gate. Green Thumb and Shepherd's Blessing are existing Tyz IMMUTABLE skills; propose same-ID Forest Dragon relocation, with real species-state use checks and no double-harvest multipliers. No Forest Dragon mana regeneration talents; its Green Recovery/Elder Grove refer only to health recovery, never mana.

**Shared Dragon base (must still be added to this species' real tree):** huge personal max mana, very efficient spell potency, expensive scales/armor, expensive claws/physical damage and expensive legal harvest-speed talents; **zero dragon-exclusive magical regeneration**. The current four-lane prototype does **not** exhaustively enumerate these shared fundamentals, so it is not yet a finished dragon tree.

### Vampire — `tmm_vampire`

**Identity:** Blood economy, predatory stealth and undead survival. **Eligibility:** unified vampire state recognized by transformation bridge; retains both native provider progressions.

| Lane | Entry (concept) | Advanced (concept) | Mastery (concept) |
|---|---|---|---|
| **Bloodcraft** | **Efficient Feeding:** Improve real blood gained from eligible feeding transactions; cannot create blood from empty actions. | **Deep Reservoir:** Increase one authoritative usable blood capacity, not two independent HUD pools. | **Crimson Renewal:** Trade a real measured blood debit for bounded emergency healing. |
| **Nightstalker** | **Nocturnal Vision:** Better low-light perception with native-sunlight penalties untouched. | **Shadow Stalker:** Short stealth advantage in darkness; avoid invisibility in full daylight. | **Bat Mastery:** Improve native bat-form controls/efficiency after that native ability is acquired. |
| **Predation** | **Fang Training:** Scale only verified vampire bite/melee attacks. | **Measured Feast:** Limited post-kill blood recovery from eligible kills, with transaction deduplication. | **Blood Duelist:** Focused single-target bonuses under low blood; prevent intentional zero-cost loops. |
| **Undead Constitution** | **Pale Resilience:** Bounded poison/disease-type mitigation where the source system allows it. | **Sunshroud:** Short resource-funded sunlight resistance; never erase core daylight tradeoffs. | **Elder Presence:** Bounded defensive/control aura with cooldown, not full charm immunity. |

**Capstone — Blood Regent:** Blood-funded high-tier predatory stance requiring advanced native vampire progression.

**Implementation conditions:** JAWR is the single planned blood and vampire physiology owner; no TeamLapen dual-pool bridging or duplicate feeding rewards. Cure suspends effects without deleting invested ranks; JAWR blood and effect hooks still require exact-version testing.

### Werewolf — `tmm_werewolf`

**Identity:** Exceptional max hearts and physical health regeneration, feral melee, and strong nature magic synergy.. **Eligibility:** unified werewolf/lycanthrope state; exact physiology owner remains unselected.

| Lane | Entry (concept) | Advanced (concept) | Mastery (concept) |
|---|---|---|---|
| **Lupine Vitality** | **Feral Vigor:** Large additional maximum health / extra hearts, materially more per SP than universal vitality boosts. | **Regenerative Flesh:** Strong, bounded actual HP regeneration under verified werewolf physiology and legal feeding/hunger rules; not mana regen. | **Unbreakable Beast:** High-level extra HP and in-combat wound recovery with caps and meaningful werewolf vulnerabilities. |
| **Feral Might** | **Rending Claws:** Scale claw damage in valid wolf/beast form, respecting weapon modifiers. | **Savage Momentum:** Short stacking melee bonus after actual hits, capped and reset out of combat. | **Primal Fury:** Timed burst consuming native appropriate resource or applying fatigue. |
| **Moonrunner** | **Predator's Stride:** Bonus mobility in valid wolf form. | **Pouncing Leap:** Targeted controlled leap with landing safety. | **Lunar Chase:** Short chase burst under moon conditions with weaker daytime counterpart. |
| **Wild Communion** | **Wild Attunement:** Increase eligible nature magic potency (Botania/Roots personal effects) while werewolf state is valid. | **Rootsong:** Further increase player nature spell potency or healing, separate from ordinary combat regeneration. | **Grove Howl:** High-level synergy between wolf pack allies and personally cast nature magic, bounded to eligible owners/effects. |

**Capstone — Moon Apex:** Late-game feral ascendancy emphasizing massive health, sustained HP regeneration, melee ferocity and nature magic; retain native silver/moon weaknesses.

**Implementation conditions:** JAWR is the planned sole authority for lycanthrope hunger, forms and forced-moon mechanics; source adapters must be verified against the installed JAWR version. Natural form-change progression remains native, not purchased again in Tyz. Werewolf regen means HEALTH regeneration, not passive magic regeneration.

### Lich — `tmm_lich`

**Identity:** Necromantic and blood-magic specialist, explicitly exchanging personal nature-magic potency for amplified blood magic.. **Eligibility:** verified Goety or JAWR/Forbidden Magic lich state; also Archlich and valid Dracolich.

| Lane | Entry (concept) | Advanced (concept) | Mastery (concept) |
|---|---|---|---|
| **Soul Economy** | **Soul Conservation:** Lower eligible personal necromancy spell costs, not global ritual/machine cost. | **Soul Reserve:** Bounded native personal soul/Arca resource capacity where extensible. | **Soul Treasury:** Small high-level sustain on committed necromantic actions, no passive self-funding loop. |
| **Blood & Nature Opposition** | **Sanguine Attunement:** Improve verified blood-based personal magic (Neo Vitae/EvilCraft where actual hooks apply) while applying a meaningful reduction to this lich's personal Botania/Roots nature spell power. | **Crimson Channel:** Strengthen source-verified blood magic further with proportionate nature-magic potency tradeoff; not passive blood machine throughput. | **Verdant Severance:** High blood-magic specialization at a stronger self-inflicted Botania/Roots nature power penalty; no world-machine or other-player debuff. |
| **Undead Host** | **Bone Captain:** Modest bonus to personally owned undead servants. | **Grave Regiment:** Improved servant coordination/leash and bounded count efficiency. | **Death Host:** Late temporary enhancement to current owned minions with hard entity budget. |
| **Soul Preservation** | **Cold Persistence:** Limited tolerance of wither/undead hazards, preserving explicit weaknesses. | **Anchor Attunement:** Improve diagnostics or controlled anchor maintenance; never bypass native phylactery/Arca cost. | **Final Passage:** Minor post-rescue recovery *after* an authoritative successful resurrection; no extra death-save trigger. |

**Capstone — Sovereign of Ash:** High-tier necromantic command stance requiring advanced lich progression and stable anchor ownership.

**Implementation conditions:** Goety and Forbidden Magic advancement remain separate native tracks; this Tyz category is shared once. For Archlich: phylactery rescue first, Arca only on failed phylactery; never duplicate saves. For Dracolich, the same one Lich Tyz category may coexist with exactly one dragon species. Negative nature modifier is a tradeoff on the lich's own active personally cast nature magic; should apply meaningfully even with Core Skills Potency. It is not an enemy debuff or whole-mod nerf.

### Hunter — `tmm_hunter`

**Identity:** Anti-undead/demon/fae apex monster-slayer; best combat upgrades, robust survival and equipment conservation, virtually no spell-stat growth. **Eligibility:** verified Vampirism hunter faction; mutually exclusive with supernatural states.

| Lane | Entry (concept) | Advanced (concept) | Mastery (concept) |
|---|---|---|---|
| **Quarry Lore & Tracking** | **Monster Lore:** Recognize verified undead, demon and fae categories, including weaknesses without revealing unrelated bosses. | **Quarry Mark:** Mark one legitimate undead, demon or fae quarry for specialized hunter tactics. | **Hunter Tracking:** Very high-level proximity effect: apply short, refreshed Glowing to eligible undead, demons and fae; explicit radius/cooldown and no unrelated mobs. |
| **Arms & Ammunition** | **Weapon Tempering:** Reduce real durability loss from Hunter weapons without healing or duplicating equipment. | **Measured Bolt:** Chance to avoid consuming actual crossbow projectile ammo on successful fire; keep special ammo/fireworks excluded until source QA. | **Supernatural Bane:** Very high-level significant damage bonus against verified undead, demons and fae; source-tag and boss caps. |
| **Armor & Survival** | **Armored Longevity:** Reduce real durability damage to equipped armor; preserve equipment break and exclude repair exploits. | **Survivor's Grit:** Strong nonmagical survival upgrade for a prepared Hunter without mimicking regeneration/immortality. | **Dreadbane Defense:** Very high-level improved survivability specifically against verified undead, demons and fae. |
| **Monster-Slayer Tactics** | **Fieldcraft:** Prepare and maneuver under threat with weapon-based tactics, not generic magic damage. | **Consecrated Snare:** Place claim-safe anti-monster traps using appropriate hunter resources, without generating magical mana. | **Slayer's Counter:** High-level counterattack window against identified supernatural enemies; bosses require special approval. |

**Capstone — Master Slayer:** Hunter combat capstone: powerful damage and defense versus verified undead/demons/fae, Hunter Tracking synergy, weapon proficiency; no broad spell power.

**Implementation conditions:** Hunter purchases never require vampire, wolf, or lich powers. Voluntary hunter-replacement ceremony suspends these effects and retains historical ranks for later valid return. Stock Sunder Armor DAMAGES enemy armor; it is not the requested reduction in durability lost from the Hunter's own equipment. Glowing on nearby quarry is deliberately a high-level Hunt Tracking skill, not an always-on early-game radar.

### Monk — `tmm_monk`

**Identity:** Unarmed martial arts, unarmored body, motion and hand-harvest specialization. **Eligibility:** valid tmm_monk native faction plus equipment-discipline condition on use; tier licenses from native Monk tree.

| Lane | Entry (concept) | Advanced (concept) | Mastery (concept) |
|---|---|---|---|
| **Martial Body** | **Empty-Hand Force:** Repeatable empty-hand damage bonus beyond the native +1 Open Palm unlock; stacking cap. | **Flowing Counter:** Bounded counterattack after a verified native Guarding Palm/parry unlock. | **Empty-Hand Precision:** Bonus to real unarmed combat after native martial prerequisites; **no Monk innate spirit projectile**—Ars Nouveau and Iron's provide ranged spells. |
| **Conditioning** | **Natural Poise:** Scale unarmored protection beyond native Unarmored Poise +2; never scale equipped armor. | **Hunger Discipline:** Bounded food/exhaustion efficiency after native license; do not remove all hunger. | **Fire & Blast Tempering:** Limited environmental damage mitigation after both matching native licenses. |
| **Footwork** | **Body Speed:** Repeatable speed under no conventional gear discipline; native Fleet Footwork remains a one-off. | **Balanced Landing:** Fall/knockback control; native Step Assist remains a one-off in native tree. | **Glide Control:** Improve native momentum gliding only after rank-15 native unlock; never creative flight. |
| **Hands as Tools** | **Barehand Efficiency:** Repeatable hand-mining speed; native Stone/Iron/Diamond/Netherite Fist unlock legal harvest tiers. | **Fortunate Hands:** Bounded fortune-like bonus only after the one native Treasure Hand Fortune/Silk Touch mode license and legitimate drop event. | **Looting Palm:** Bounded unarmed looting after native rank-9 license; no creature-drop duplication. |

**Capstone — Empty-Hand Paragon:** The maxed Monk tree, including legitimate native abilities and any approved endgame capstone, must support a real **solo Chaos Guardian victory with no worn equipment or weapons**; the fight still requires skilled execution and honors its normal mechanics. Other class trees are not prerequisites.

**Implementation conditions:** Preserve the Monk native full skeleton (50 nodes, 6 pilot nodes) and 47 Monk-specific Tyz candidates as source inventories, NOT 47 new compulsory live skills. First Sunset and native mastery from barehand breaks, kills and first foods stay separate from MXP global awards. Step Assist after Fleet Footwork, not at rank one. Tool-free harvest tiers remain native permission unlocks.

## 4. Native ability versus Tyz purchase: no double purchase

| Native provider | Native tree or state remains responsible for | Tyz is responsible for |
|---|---|---|
| Dragon Survival | Cave/Sea/Forest species choice, growth, body and native breath | Large mana/spell power, expensive armor/claw/harvest ranks, species schools |
| **JAWR (only future vampire owner)** | Vampire form, blood/feeding, daylight effects, native rank and cures | JAWR-gated personal blood specialization and survival, no duplicate Lord/minion tree |
| **JAWR (only future lycanthrope owner)** | Wolf body/morph, hunger, moon/silver, native rank and cures | Strong hearts and HP regen, nature magic and selected retained player skill concepts |
| Forbidden Magic + Goety | Two lich progressions; phylactery/Arca precedence | Blood magic vs nature magic tradeoff, necromantic personal capacity/summons |
| **Standalone Hunter module** | Hunter eligibility, specialist target classification and native skills | Hunter combat investments, weapon/armor preservation, ammo saving, proximity supernatural tracking |
| **Standalone Monk module** | First Sunset; native fist slot, armor toggle, legal hand harvest, Treasure Hand Fortune/Silk mode | Form-conditional finite martial stats/Apotheosis affixes under the one Tyz wallet |

**Monk exception:** PR #71 already defines a **50-node conceptual Monk design** (the old six-node Vampirism-hosted pilot is a migration reference, not the standalone future backend). Its second skeleton contains **47 Monk-only Tyz candidates** (seven traits plus forty bounded Apotheosis affix conversions); those are research candidates, not extra compulsory skills. This document distills their gameplay into four Tyz lanes. Preserve existing native rank-one Open Palm (+1 barehand attack), Unarmored Poise (+2 armor), Stone Fist, and later Fleet Footwork→Step Assist. Native Monk mastery credit (legal barehand block breaks, actual spell-attributed breaks, combat, and first foods) remains independent of the Tyz MXP ledger. Avoid double-awarding the same event into the same wallet or repeating native unlocks in Tyz.

**Lich exception:** both native lichdoms remain earnable and stack into Archlich. One Lich Tyz category covers both (source-specific nodes may require the matching native provider). When two exist, phylactery saves first; Goety Arca is fallback only if phylactery cannot rescue. A Tyz rank cannot grant or reorder death saves.

## 5. Server adapters and verification statuses

No row in this blueprint should be mistaken for an already working mod hook. Implementations fall into four buckets:

1. **Native attribute mapping** (low risk after ID check): attack, movement, health/armor, confirmed spell attributes. Changes must be reverted when form/equipment conditions fail.
2. **Shared event adapter** (medium): damage kinds, status effects, real drops, real resource debit, bounded owner-attributed summons, actual world/biome conditions.
3. **Exact-provider adapter** (high): dragon breath/body, JAWR-only vampire feeding and lycanthrope physiology/shape, phylactery/Arca death events, standalone Monk special techniques and Apotheosis affixes.
4. **New activated skill** (highest): custom dash, scan, shockwave, traps, control, defensive stance; requires cooldown, cost, anti-griefing, particles/UI and multiplayer replication.

Do not implement high-risk abilities as permanent passive buffs just because Tyz JSON cannot express the activation. Confirm dedicated-server and client behavior against exact 1.21.1 pinned binaries.

### Mechanics that must never be silently overridden

- Native special diets and magical-food exceptions, finite food resource costs and JAWR-only vampire feeding.
- One canonical per-resource debit and one MXP award for Ars/Iron's shared mana; other costs normalized by original reference capacities.
- Quest/resource gates for endgame; no mining talent bypassing disabled ores or Dragon Survival harvest checks.
- Transform/cure, JAWR progression history, death-rescue arbitration, client HUD and body renderer.
- Claim protections, server performance ceilings, safe teleport destination and attribution of player-created summons.
- Silver/sunlight/other native vulnerabilities remain meaningful; a Tyz node is never a blanket no-drawback transformation.

## 6. Build sequence and acceptance cases

**Phase A — concept-to-config:** Audit all 32 original Tyz built-in skills and preserve their IDs/behavior handlers in safe same-ID overrides assigned to the nine trees. During transition retain four old categories and validate 13-tab pagination. **Only after every stock skill's new placement and behavior works**, remove the four old category IDs and validate 9-tab pagination; create inactive/skeleton form node IDs. Handle old planned `tmm_dragon` gracefully: no automatic one-to-three point duplication; if any actual players own generic dragon ranks, make an explicit migration/refund map and back up saves first.

**Phase B — narrow playable vertical slice:** Existing four Core Skills **five-rank** files + **one entry node per each of eight form trees**. Each must be visible, gated by real provider state, spend exactly one wallet, change an actually measured stat/effect in the correct context, and deactivate on cure/relog. Prioritize simple measurable effects: Cave Stonehide, Sea Sleek Form, Forest Green Recovery, Vampire Efficient Feeding *after JAWR blood hook verification*, Werewolf Feral Vigor *after JAWR shape authority*, Lich Bone Captain *after ownership validation*, Hunter Monster Lore *after standalone Hunter state validation*, and Monk Empty-Hand Force *after standalone Monk backend verification*.

**Phase C — multi-lane progression:** Add the shared high-value Dragon mana/potency packages and expensive physical baselines to each species; source-classified Cave/Forest/Sea affinity, Forest agriculture, Hunter late anti-supernatural combat and tracking, Lich blood/nature tradeoffs and Werewolf HP/health regen. Then add chosen mid/final native milestones, refunds, capstones and class-specific adapters. Never certify based only on clean JSON/compilation/server boot. Keep all new skills flagged as conceptual until individually implemented and client-verified.

**Phase D — cross-form and exploit QA:** Verify every allowable and disallowed combination, all acquisition orders, logout/respawn/dimension travel, native skill purchases, bulk buys, direct grants, refund predecessor chains, packet sync, time-of-day/environment, claims, anti-grief rules, pet attributions, hunger/blood, food exceptions and resource loops. Record separate acceptance per skill; a passing Tyz GUI does not make the effects work.

### Explicit decisions not yet locked

- Exact number of points desired in a typical playthrough: the 1.2^P MXP slowdown makes exhaustive maxing unrealistic; decide whether distinct builds are intentionally permanent or normal respec is expected.
- Standalone Hunter and Monk coexistence, including transformations: do **not** inherit TeamLapen exclusivity; await their verified native state APIs and the transformation compatibility policy.
- Which JAWR actions already cover the older retired-mod skill concepts, to avoid duplicate native player powers.
- Source-specific percentage reference units, boss-tier damage caps and real native milestone gates; first-pass percentages/caps now exist but are not gameplay-approved.
- Exact source-compatible order for retiring the four default Tyz categories after all 32 same-ID skills are safely rehomed and validated.


### Source inventory and removal gate (added 2026-11-15)

The full 32-skill default inventory and source-to-destination proposals are in [PR #85](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/85) ([read the migration ledger](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/feature/tyzs-stock-inventory-five-rank-core-2026-11-15/docs/TYZS-STOCK-SKILL-MIGRATION-2026-11-15.md)). The current four default categories are a transitional compatibility layer, **not** permanent expansion to 13 tabs. Do not delete defaults until all 32 original IDs have equivalent effects in the nine final categories, including explicit decisions for unsafe skill-learning/PvP/economy behavior. Native Tyz `IMMUTABLE` handlers attach by skill ID; same-ID JSON overrides let us rehome behavior without generic JSON approximations, subject to exact-version runtime tests.

## 7. What this commit does *not* do

This is a design-only catalog and machine-readable sketch. It does **not** create active Tyz skills, mutate the level pool, add custom mod code, mark transformations compatible, fix the Monk UI or vampire blood pool, alter a diet, add a new faction, or change a release/build manifest. The existing staged Core Skills and Monk implementation remain where they are. Content is intended for review and later mechanically checked conversion into runtime config.
