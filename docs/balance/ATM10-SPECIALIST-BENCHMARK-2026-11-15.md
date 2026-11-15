# ATM10 Specialist Power Benchmark — v0.3 (2026-11-15)

**Status:** SOURCE-VERIFIED BASELINES AND **CONSTRAINED LOADOUT MAXIMA**; NOT YET A CERTIFIED ALL-MOD ENTIRE-PACK MAXIMUM. This is a design/audit document, not an active balance patch.

**Owner-approved comparison rule:** A TMM character with the best legal native spellbook/armor, maximum compatible enchantments, gems, affixes, appropriate accessories and maximum *non-Monk* specialist skill investment should perform approximately as well as a similarly optimized ATM10 Unobtainium caster. Once each TMM specialist maximum is established, Monk's compatible, unarmored performance target is **85% of the TMM specialist's measured final performance**, not 85% of an arbitrary technical attribute cap. Suggested acceptance band for the TMM-versus-ATM10 performance metric is +/-5%; this band is a proposed test tolerance, not a current observation.

## Reference snapshot and confidence

- ATM10 reference: AllTheMods/ATM-10 main, commit **d3fef516ed2c3610f49981f5f924d7205d8536ef** (2026-11-14). This is a **repository snapshot**, not a frozen public release-pack ZIP or a verified live game.
- Iron's 1.21 upstream reference: iron431/irons-spells-n-spellbooks branch 1.21, commit **bc3671984544f10264bcab6828d4ef6f2afddee2**.
- ATM wizard addon reference: iron431/all-the-wizard-armor branch 1.21, commit **12a8151484f3e489bb6a3ae277197af602a232fd**.
- Ars Nouveau upstream reference: baileyholl/Ars-Nouveau main, commit **4d323fba9200c3293a4f4848cf1e0fe70beb7098**.
- ATM arcanist addon reference: Jarva/All-The-Arcanist-Gear main, commit **0c15d90b18b54f6ea11fe9c92636145f9e090e96**.
- Apotheosis 1.21 reference: Shadows-of-Fire/Apotheosis branch 1.21, commit **84e27e7031c7e2ab2f2e8328daf491c88203fcbc**.
- TMM local reference is test/full-integration (as of this research). Its manifest pins Iron's 3.16.3, Ars Nouveau 5.13.5, Apothic Attributes 2.11.0, Apotheosis 8.9.0, AttributeFix 21.1.3, and Ars 'n Spells 3.3.5. Do not substitute upstream branch HEAD for byte-for-byte verification of any pinned JAR.

## Benchmark rules and exclusions

1. All equipment has to fit a **real loadout**: four armor slots, the actual spellbook/Curios slot(s), a legal hand/offhand selection, and the real socket count and enchantment compatibility. No stacking of mutually exclusive armor, weapons, upgrades, affixes, gems, or two spellbooks in one slot.
2. Optimize **one performance axis at a time**, then record tradeoffs. Max mana, maximum damage, lowest cooldown, maximum healing and maximum defense can require different layouts. Do not present their individually maximal statistics as one simultaneously equipped character.
3. Passive/permanent gear and skills form the steady-state benchmark. Temporary potions, active spells, food, transformations, ritual effects and environmental buffs belong in named **burst** or **sustained-encounter** subprofiles with identical conditions on both packs.
4. Show stat base, flat additions, multiplied-base additions, multiplied-total modifiers, clamps, and game-specific nonlinear soft caps separately. Minecraft 1.21's add_multiplied_total multiplies **each** factor; it does not simply sum their percentages.
5. Compare final behavior (damage/sec, casts/sec, mana/sec, effective HP, etc.), not only the UI's attribute percentages. Apply the eventual Monk 85% to the comparable **observable metric**.
6. Unverified eligibility = **unknown**, not zero and not assumed maximum. Do not grant magical effects from an armor affix/gem unless the source actually lists that attribute, or a verified compat datapack adds it.
7. ATM10's tiered Ars and Iron's magic resources are **separate source-native profiles**. TMM's Ars 'n Spells 3.3.5 can unify pools; its active mode must be confirmed before comparing TMM resource capacity. Never add two unified pools as though independent.
8. AttributeFix expands bounds; it is not an earnings source. Check for clipping at every end-of-build attribute and for hidden hard or soft caps inside game logic.

## Verified ATM10 Unobtainium gear baselines (before affixes, gems, enchants, Curios, upgrade orbs, skill points)

| Axis | ATM10 Iron's Unobtainium wizard | ATM10 Ars Unobtainium arcanist | Evidence grade |
| --- | --- | --- | --- |
| Four-piece armor | 40 armor, 24 toughness | 40 armor, 24 toughness | Source-defined |
| Base maximum mana with book | 100 + 4x450 + 300 = **2,200** | 100 + 4x450 + 6x50 + 15G = **2,200 + 15G** | Formula, with Ars glyph count G |
| Generic magic attack | **1.4^4 = 3.8416x** Iron's SPELL_POWER | **+48 flat** Ars SPELL_DAMAGE_BONUS (12 per piece) | Source-defined operations |
| Mana regen modifier | **1.15^4 = 1.74900625x** Iron's MANA_REGEN | **+48 flat** Ars MANA_REGEN_BONUS (12 per piece) | Source-defined operations |
| Unupgraded actual regen | At base 2% max mana/sec, 2,200 x .02 x 1.74900625 = **76.956275 mana/sec** | (base 5 + tier 6 + .33G glyph + 48 armor) = **59 + .33G mana/sec** | Assumes default regen config |
| Cast cooldown | Unobtainium spellbook's +.50 multiplied-total on base 1 -> **50% shorter cooldown** | Not the same system; no universal cooldown factor established | Source-defined |
| Cast time | Unobtainium spellbook's +.35 multiplied-total on base 1 -> **35% shorter noncontinuous cast** | No equivalent global speed stat established | Source-defined |
| Book capacity | **15 Iron's spell slots** | **20 Ars glyph positions per spell** (10 base +10 bonus) | Source-defined |
| Additional book feature | Spellbook contributes +300 mana | Tier-six Ars casting book; **1.3x damage on its marked target entity tag** only; NOT a universal 30% damage buff | Source-defined |

**Important correction to prior discussions:** Iron's 4 x +40% armor spell power is **3.8416x**, not 2.6x. Four x +15% regen is **1.74900625x**, not 1.6x. Ars book tier is 6, not 3; the prior 3,325/84.05 examples undercount this tier. The Ars book's 1.3x damage applies only to entities in the addon's bonus-damage tag and is conditional.

Iron's formulas (assuming ServerConfigs.MANA_REGEN_MULTIPLIER=1): regen/s = max_mana x MANA_REGEN_attribute x 0.02. Noncontinuous cast/cooldown multiplier = 2 - softCapFormula(attribute), where softCapFormula(x) = x if x <= 1.5, otherwise 2 - 0.25/(x-1). Other spell mechanics, attack source multipliers and integer-tick rounding must still be applied.

## ATM10 maximum configured Ars enchantments — verified source values

ATM10's config/apotheosis/enchantments.cfg sets both ars_nouveau:mana_boost and ars_nouveau:mana_regen **Max Level=7**. The Ars enchant definitions give +25 max mana/level and +2 regen stat/level respectively, with four normal armor slots.

| Component | Four equipped pieces, each at VII |
| --- | --- |
| Mana Boost | **+700 max mana** |
| Mana Regeneration | **+56 mana/sec** (under source-default regen logic) |

Thus an ATM10 tier-six Unobtainium arcanist with both VII enchantments and **without threads, amulets or other buffs** has a documented calculation of:
- **Max mana = 2,900 + 15G**, where G is the **actual earned glyph bonus count**.
- **Mana regen = 115 + 0.33G per second**.
- Example **G=85**: 4,175 mana and 143.05 mana/sec. **G=85 is illustrative, not an audited ATM10 maximum.**
- Max glyphs and all addon glyph sources must be enumerated and the active ATM10 Ars server config verified before these expressions can be finalized.

## Apotheosis legal-loadout considerations

ATM10 includes Apotheosis. At the examined Apotheosis version, **Mythic** rarity rolls four stat affixes, two basic-effect affixes, one ability affix and 1-3 sockets at 95% probability, or 4 sockets on the rare branch (5%). **Four sockets is a possible roll, not a universal default.** Enchantment caps/compatibility and reforging restrictions remain separate constraints.

Examples of **source-supported local modifiers**, not cumulative maxima:
- **Blessed** Mythic armor stat affix: up to **+8 max health** on a legal helmet/chestplate/leggings/boots.
- **Stalwart** Mythic armor stat affix: up to **+0.35 knockback resistance** per eligible piece, subject to native cap and stacking/eligibility.
- **Adamantine** Mythic armor stat affix: up to **+0.40 add_multiplied_base armor** for chestplate or leggings (do not confuse with multiplying an individual item's displayed armor).
- **Spiritual** Mythic armor affix: up to **+0.40 add_multiplied_base healing received** on chestplate or leggings.
- **Guardian** Perfect gem: up to **+8 flat armor** when socketed in its valid chestplate/leggings class.
- **Royalty** Perfect gem: **+15% add_multiplied_total** to its explicitly listed attributes in a **helmet**, flagged **unique**. Its explicit list has core combat/utility stats and Apothic attributes, **not Ars or Iron's spell power/mana**.

The examined base Apotheosis affix and gem JSON do not establish a magic-specific mana/spell-power gain for these mage sets. **Check effective ATM10 data pack registry and all integration mods** before asserting there are none. Each item's effective LootCategory, gem class, affix eligibility, unique-gem restrictions and number of sockets must be probed. Neither Iron's nor Ars spellbook is assumed to accept armor gems simply because armor does.

**Iron's upgrade orb tradeoff:** Upstream Mana Upgrade Orb gives +50 mana each. A community ATM10 guide describes up to three per eligible armor/book item, but exact item acceptance and 3-per-item cap require runtime verification for this specific gear. The often quoted **3,050 mana** scenario is 2,200 + (5x3x50) + Ring of Mana (+100), *if all five items and the ring are concurrently legal*. With the unmodified +15%-per-piece regen, that scenario is **106.68938125 mana/sec**. It is a **conditional example**, not a certified maximum. Power/cooldown upgrade orbs compete for their allowable upgrade slots: do not spend the same slots twice.

## Required optimization profiles and measurable axes

The machine-readable companion is at manifest/atm10_specialist_benchmark.json. The profiles below are the required search and measurement matrix. "Pending" means an exact legal ATM10 optimum is not yet established.

| Profile | Performance metrics | Quantitative state |
| --- | --- | --- |
| Ars damage caster | spell damage versus unarmored and high-armor targets, glyph amplification, cast throughput, spell cost, 20-glyph access | +48 flat spell-power armor verified; optimized damage pending |
| Ars capacity/sustain | total mana, absolute mana/sec, 60-second sustainable mana expenditure, reserved mana and costs | 2,900+15G and 115+.33G with VII enchantments verified conditionally |
| Iron's generic damage caster | generic SPELL_POWER, school power, actual spell damage and 60-second DPS | 3.8416x armor-only generic power verified; school/upgrade orbs pending |
| Iron's rapid caster | effective cooldown ticks, cast ticks, casts/min, minimum interval and mana throughput | 50% cooldown and 35% cast-time book-only verified; optimized pending |
| Iron's mana/sustain | maximum mana, percentage and absolute mana regeneration, costly cast sustainability | 2,200 and 76.956275/s armor+book verified; orb and Curio setup pending |
| Hybrid Ars+Iron's caster | mana primary mode, cross-cast resource debit, scaling and cooldown parity | TMM bridge configuration and double-count guards pending |
| Melee weapon specialist | damage/hit, attack speed, DPS by armor class, crit chance/damage, armor pierce/shred, life steal | Endgame weapon+affix/gem search pending |
| Ranged specialist | arrow/projectile damage, draw speed, reload time, projectile velocity, armor penetration, ranged DPS | Endgame bow/crossbow+affix/gem search pending |
| Physical defender | maximum health, armor, toughness, actual mitigation, incoming damage/sec, effective HP, regeneration | Unobtainium mage armor baseline 40/24; general defensive optimum pending |
| Evasion/mobility specialist | dodge chance, walk/sprint speed, jump, knockback resistance, fall mitigation, step height | Source candidate affixes verified; optimum pending |
| Unarmed Monk comparison | unarmed DPS, armor-independent mitigation, effective HP, dodge, speed, utility, spell throughput | **TMM reference** only; derive from TMM specialist outputs, not direct ATM item stats |
| Hunter / favored-target fighter | favored and unfavored target DPS, evasion, sustain, potion preparation and weapon specialization | Matched encounter profiles pending; no invented ATM "Hunter" stat |
| Miner/utility specialist | mining rate, harvest tier, interaction reach, movement utility and gear durability | Pending; never substitute raw Minecraft XP for custom path XP |

### Standard comparison encounters (test design, not claims about ATM10 mobs)

- **Sustained caster:** same spell, spell level, dummy target, range and hit frequency; log at 0-10s and 0-60s, with mana at start/end and actual cooldown/cast ticks.
- **Burst caster:** maximum legal one-hit combo and maximum 10-second effective damage, accounting for cooldowns and mana.
- **Physical melee/ranged:** compare unarmored and heavily armored nonboss dummies; report damage/hit, attacks/sec, actual hits, armor/penetration and crit distribution.
- **Defense:** compare repeated physical, ranged, magic and armor-bypassing hits at two fixed incoming hit strengths; report damage taken and effective hit points, not armor points alone.
- **Mobility:** block-distance per second, burst dash/blink distance, fall survivability, movement under active casting.
- **Resource sustainability:** mana spent and regenerated per 60 seconds under an attainable repeated-cast rotation.
- **Ability parity:** binary/qualitative powers such as flight, wall mobility, projectile catching, passive defenses, form abilities, harvest tier and immunities need separate test cases and cannot be compressed into a percentage.

## Specialist-then-Monk calibration contract

For each capability C and test context T:

**ATM_reference[C,T] = maximum measured legal ATM10 endgame build** (armor/book + legal enchantments + legal affixes/gems + legal accessories; active buffs explicitly named).

**TMM_specialist[C,T] = maximum measured legal TMM endgame build** (best native gear + legal enhancements + maximal relevant non-Monk skills).

Target **TMM_specialist performance ~= ATM_reference performance (within +/-5% initial tolerance)**. The required specialist skill bonuses are the difference between equipment-contributed performance and the ATM target, using the *real nonlinear game mechanics*; percentage arithmetic alone is insufficient.

Then target **TMM_Monk[C,T] ~= 0.85 x TMM_specialist[C,T]** for the comparable measurable capabilities, subject to deliberate exception for nonnumeric abilities and Monk's armor restrictions. Where an action has a nonzero baseline (e.g. spell power x1), compare the final action outcome, not 85% of the *bonus component*. A 0.85 throughput target does NOT automatically imply a 0.85 reduction percentage.

No balance values for Monk or other classes should be committed to runtime JSON from this v0.1 document.

## Open validation gates before declaring any "full optimized maximum"

1. Freeze an ATM10 downloadable pack/version and extract the **effective datapack registry** of enchantments, affixes, gems, loot category overrides, magic power integrations and Curios slot capacities (the upstream source alone is not the runtime pack).
2. Record the **actual** Unobtainium spellbook/armor item attributes in that ATM10 client. Confirm both mage lines can receive Mythic affixes and which items are socket-eligible. Spellbook socket/affix eligibility is **not verified**.
3. Enumerate every legal maximum enchantment on each piece; verify whether Ars Mana Boost VII / Regen VII stack across all four pieces as source definitions imply.
4. Enumerate gem classes, uniqueness, effective socket limit and mutually exclusive affixes, and solve the per-attribute integer loadout optimization; don't combine incompatible maxima.
5. Measure Ars glyph count G at full registry unlock; verify tier-six, 20 glyph slots, capacity/mana-regen threads, equipment modifier-ID stacking and any backend limit.
6. Verify Iron's actual maximum orb counts and mana rings, power/cooldown/cast-time stacking and nonlinear soft caps with the pinned version.
7. Probe physical Unobtainium/alternative endgame gear (including pack-adjusted Silent Gear) and record defensible offensive, defensive and mobility maxima.
8. Validate TMM's exact armor/caster item categories, enchanting rules and Ars 'n Spells bridge mode; formulate its specialist skill deficit in separate data from the ATM benchmark.
9. Only after each profile has a runtime-verified reference, publish "certified" maxima and derive specialist skill JSON; then derive Monk from TMM actual maxima.

## Primary source links

- [ATM10 enchant caps](https://github.com/AllTheMods/ATM-10/blob/d3fef516ed2c3610f49981f5f924d7205d8536ef/config/apotheosis/enchantments.cfg)
- [ATM10 category override list](https://github.com/AllTheMods/ATM-10/blob/d3fef516ed2c3610f49981f5f924d7205d8536ef/kubejs/data/apotheosis/data_maps/item/loot_category_overrides.json)
- [Unobtainium wizard config](https://github.com/iron431/all-the-wizard-armor/blob/12a8151484f3e489bb6a3ae277197af602a232fd/src/main/java/io/redspace/allthewizardgear/ServerConfig.java)
- [Wizard item modifier implementation](https://github.com/iron431/all-the-wizard-armor/blob/12a8151484f3e489bb6a3ae277197af602a232fd/src/main/java/io/redspace/allthewizardgear/item/WizardArmorItem.java)
- [Unobtainium spellbook registry](https://github.com/iron431/all-the-wizard-armor/blob/12a8151484f3e489bb6a3ae277197af602a232fd/src/main/java/io/redspace/allthewizardgear/registry/ItemRegistry.java)
- [Arcanist stats and tier-six book](https://github.com/Jarva/All-The-Arcanist-Gear/blob/0c15d90b18b54f6ea11fe9c92636145f9e090e96/src/main/java/com/github/jarva/allthearcanistgear/setup/config/ServerConfig.java)
- [Arcanist tier and threads](https://github.com/Jarva/All-The-Arcanist-Gear/blob/0c15d90b18b54f6ea11fe9c92636145f9e090e96/src/main/java/com/github/jarva/allthearcanistgear/setup/registry/ArsNouveauRegistry.java)
- [Ars mana calculations](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/api/util/ManaUtil.java)
- [Ars mana enchantments](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/generated/resources/data/ars_nouveau/enchantment/mana_boost.json)
- [Iron's attribute definitions](https://github.com/iron431/irons-spells-n-spellbooks/blob/bc3671984544f10264bcab6828d4ef6f2afddee2/src/main/java/io/redspace/ironsspellbooks/api/registry/AttributeRegistry.java)
- [Iron's regen/cooldowns](https://github.com/iron431/irons-spells-n-spellbooks/blob/bc3671984544f10264bcab6828d4ef6f2afddee2/src/main/java/io/redspace/ironsspellbooks/capabilities/magic/MagicManager.java)
- [Apotheosis Mythic socket rule](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/rarities/mythic.json)
- [Apotheosis Royalty gem (helmet only)](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/gems/overworld/royalty.json)


## v0.2 — computed constrained maxima (source-derived; runtime confirmation pending)

These are **actual numeric solutions within explicitly defined item pools**, and are now recorded in \`manifest/atm10_specialist_benchmark.json\` under \`constrained_optimized_loadouts\`. All globally optimized \`optimized_atm10_max\` fields remain null until full-pack equipment and buffs are checked. This avoids falsely certifying a partial item search as the pack's maximum.

### Iron's: maximum mana and recovery in the examined caster slots

Source evidence: \`ArcaneAnvilMenu\` checks \`getTotalUpgrades() < ServerConfigs.MAX_UPGRADES\`; \`ServerConfigs\` defines \`maxUpgrades=3\`; \`Utils.canBeUpgraded\` accepts \`SpellBook\`, \`ArmorItem\`, and \`CastingItem\`; a Graybeard Staff extends \`CastingItem\`; \`UpgradeOrbTypeRegistry\` defines the Mana Orb as **+50 max mana**. Iron's 1.21 Curios JSON registers **2 ring**, **1 necklace**, **1 spellbook** slots. Iron's own item registry defines **Ring of Mana +100**, **Amethyst Resonance Charm +15% regen, add_multiplied_base**, and the \`StaffTier.GRAYBEARD\` grants **+25% regen, add_multiplied_base** and **+10% generic SPELL_POWER, add_multiplied_base**.

- Five-slot-only maximum mana (four armor + one book, 15 Mana orbs): **2,950 mana**, with **103.19136875 mana/sec** at default regen and no other accessories.
- Expanded standard caster-slot **mana** configuration: four Unobtainium armor pieces + Unobtainium book + Graybeard Staff in the main hand. All six are upgraded with three Mana Orbs each (18 total), with two Rings of Mana and an Amethyst Resonance Charm: **100 + 4×450 + 300 + 18×50 + 2×100 = 3,300 maximum mana**.
- The above standard-slot **recovery** result: **3,300 × .02 × (1.15)^4 × (1 + .25 + .15) = 161.6081775 mana/sec**. +25% staff and +15% necklace bonuses are add_multiplied_base, hence combined as +40% before the four independently multiplied armor bonuses.
- The regular two-ring capacity is **source-backed** in Iron's 1.21 datapack (\`data/irons_spellbooks/curios/slots/ring.json\`), but the effective ATM10 runtime may have *additional slots* from accessories/other mods; the above explicitly includes only the standard two.
- The absolute TMM and ATM10 maximum might also use magic items in the offhand, alternative ring/Curios modifiers, spell effects or abilities. Do not label 3,300/161.61 as the final pack-wide cap.

### Iron's: single-school damage and fast casting, distinct legal loadouts

- **Single-school power**: four Unobtainium mage armor pieces yield \`1.4^4 = 3.8416×\` generic power; a Graybeard Staff adds +10% \`add_multiplied_base\`, yielding **4.22576×** generic power. If all 18 allowable upgrades on the four armor pieces, Curios book and mainhand staff are the **+5% to one school** orbs, the school attribute becomes \`1 + 18×.05 = 1.90×\`. The combined single-school spell-power multiplier is **4.22576 × 1.90 = 8.028944×** before spell level and target resistances. This is not a global multiplier to every school; it spends all 18 orb allocations on one school.
- **Cooldown**: Unobtainium spellbook +50% multiplied-total cooldown attribute, Artificer Staff +10% multiplied-base cooldown, two Cooldown Rings +15% multiplied-base each, and 18 cooldown orbs +5% multiplied-base each: \`raw = (1 + .10 + 2×.15 + 18×.05)×1.50 = 3.45\`. Iron's uses \`durationFraction = 2 - (2 - 0.25/(raw - 1))\` for raw >1.5, producing **0.1020408163× original duration = 89.79591837% reduction**. Actual cooldown rounds to integral ticks per spell; casts also consume mana. Cooldown ring slots cannot be mana/cast-time rings at the same time.
- **Cast time**: Unobtainium book +35% multiplied-total cast-time attribute, Artificer Staff +10% multiplied-base, two Cast Time Rings +15% multiplied-base each: \`raw = (1 + .10 + 2×.15)×1.35 = 1.89\`. For noncontinuous casts the effective duration fraction is **0.2808988764× = 71.91011236% cast-time reduction**. The registry contains no ordinary \`cast_time\` upgrade orb; filling orb slots with cooldown or mana orbs does not boost this stat. Continuous casts use a different calculation.

**These cooldown and casting loadouts are separate optimizations**, and the spellbook itself contributes its +300 max mana and 15 spell slots in all cases. Neither reduction should be treated as permanently applicable to every spell type without checking spell event hooks and tick rounding.

### Apotheosis: Mythic health-specialist test case

This is an instructive **extreme survivability build**, not a generic mage benchmark and not a proof of global maximum. Apotheosis 1.21 Mythic has a **rare four-socket outcome**. From its datapack gems and affix definitions:

- Each of four Mythic armor pieces: Blessed stat affix at maximum **+8 Max Health** (total +32). Base player health assumed 20.
- Unobtainium chestplate's four sockets: four **Perfect Warlord** gems, each **+35% add_multiplied_base max health**; \`unique\` is absent/false. Total multiplied-base +140%.
- Unobtainium leggings' four sockets: four **Perfect Brawlers** gems, each **+12 Max Health**; \`unique\` is absent/false. Total +48.
- Helmet: one **Perfect Royalty** gem, **+15% add_multiplied_total max health**; unique means once **per item**, not per entire loadout.
- Boots: one **Perfect Tyrannical** gem, **+40% add_multiplied_total max health**, with its associated negative life-steal tradeoff.
- Armor-only result: \`(20 + 32 + 48) × (1 + 4×.35) × 1.15 × 1.4\` = **386.4 maximum health** (193.2 normal hearts), before all additional accessories/other mod effects.
- If additionally carrying an eligible four-socket Mythic shield with four **Perfect Brawlers** gems, each +30% multiplied-total max health, the resulting health is **386.4 × (1.3)^4 = 1,103.59704 maximum health** (~551.8 hearts) **conditional on the four socketed gem modifiers applying separately at runtime**.
- This offhand shield configuration is incompatible with an offhand staff, focus or dual-wield comparison. Neither the helmet nor boots must use all four sockets for this specific example; the four-socket assumptions on chest, leggings, and shield are the important ones.

**Gem uniqueness is per ITEM**: Apotheosis's \`Gem.canApplyTo\` checks the gem list of the specific socketed stack, and its guide calls Unique a one-of-a-kind-per-item restriction. Four identical non-unique gems on one item are allowed by the gem uniqueness rule, though actual modifier ID stacking and full-pack overrides still need testing. The shield, armor and book's **effective LootCategory** must also be checked in-game; the v0.2 health calculation is a well-supported **conditional equipment scenario**, not a certified observed stat.

The very high defense values underscore why Monk parity must be tied to *actual effective health / sustained incoming damage under realistic enemy tests* rather than naively giving Monk 85% of the largest theoretical health attribute from an ultra-narrow glass-cannon HP build.

### Sources added for v0.2

- [Iron's source: Arcane Anvil upgrades](https://github.com/iron431/irons-spells-n-spellbooks/blob/bc3671984544f10264bcab6828d4ef6f2afddee2/src/main/java/io/redspace/ironsspellbooks/gui/arcane_anvil/ArcaneAnvilMenu.java)
- [Iron's maximum upgrades and default configuration](https://github.com/iron431/irons-spells-n-spellbooks/blob/bc3671984544f10264bcab6828d4ef6f2afddee2/src/main/java/io/redspace/ironsspellbooks/config/ServerConfigs.java)
- [Iron's upgrade orb values](https://github.com/iron431/irons-spells-n-spellbooks/blob/bc3671984544f10264bcab6828d4ef6f2afddee2/src/main/java/io/redspace/ironsspellbooks/registries/UpgradeOrbTypeRegistry.java)
- [Iron's registered rings and necklaces](https://github.com/iron431/irons-spells-n-spellbooks/blob/bc3671984544f10264bcab6828d4ef6f2afddee2/src/main/java/io/redspace/ironsspellbooks/registries/ItemRegistry.java)
- [Iron's StaffTier stats](https://github.com/iron431/irons-spells-n-spellbooks/blob/bc3671984544f10264bcab6828d4ef6f2afddee2/src/main/java/io/redspace/ironsspellbooks/item/weapons/StaffTier.java)
- [Iron's ring slot count 2](https://github.com/iron431/irons-spells-n-spellbooks/blob/bc3671984544f10264bcab6828d4ef6f2afddee2/src/main/resources/data/irons_spellbooks/curios/slots/ring.json)
- [Apotheosis Warlord gem](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/gems/core/warlord.json)
- [Apotheosis Brawlers gem](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/gems/core/brawlers.json)
- [Apotheosis Royalty gem](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/gems/overworld/royalty.json)
- [Apotheosis Tyrannical gem](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/gems/core/tyrannical.json)
- [Apotheosis Blessed affix](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/affixes/armor/attribute/blessed.json)
- [Apotheosis uniqueness rule](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/main/java/dev/shadowsoffire/apotheosis/socket/gem/Gem.java)


## v0.3 — additional constrained defensive maxima (not simultaneous)

Using only the Unobtainium armor base, source-defined Mythic affixes, and the listed Perfect gems, these are source-computable **scenario peaks** (pending in-client item category/roll/stack verification). Same 40 armor / 24 armor-toughness Unobtainium mage armor reference, but each row allocates its sockets differently. Minecraft AttributeFix in ATM10 makes the ordinary vanilla armor/toughness 30/20 bounds unsuitable as assumed clamping ceilings for the examined default-enabled attributes.

| Optimization | Four-piece armor only | Four-piece armor + offhand shield |
|---|---:|---:|
| **Armor points** | **182.16** | **381.524** |
| **Armor toughness** | **81.0** | **145.8** |
| **Max health** (from v0.2) | **386.4** | **1,103.59704** |

**Armor loadout, no shield:** 40 base + four Mythic Ironforged (+8 each) + one Perfect Guardian (+8) on each of chestplate and leggings = 88 additive armor; multiply base by 1.8 from two Mythic Adamantine (+40% each), then 1.15 from one Perfect Royalty helmet gem: \`88 × 1.8 × 1.15 = 182.16\`.

**Armor loadout with shield:** add shield Mythic Ironforged (+30% multiplied base), Royalty Perfect shield (+50% multiplied base), and Guardian Perfect shield (+45% multiplied total): \`88 × (1+.4+.4+.3+.5) × 1.15 × 1.45 = 381.524\`.

**Toughness loadout, no shield:** 24 base + four Mythic Steel Touched (+6 each) + Perfect Tyrannical gems (+6 each chest and leggings) = 60 additive toughness; multiply by 1.35 from one Perfect Samurai leggings gem: \`60 × 1.35 = 81\`.

**Toughness loadout with shield:** additionally shield Mythic Steel Touched (+30% multiplied base) and a Perfect Tyrannical shield gem (+50% multiplied base): \`60 × (1+.3+.5) × 1.35 = 145.8\`.

Each set is a **different** specialized optimization. The 381.524 armor, 145.8 toughness and 1,103.6 HP values **cannot be assigned as simultaneous** without solving the intersecting gem-slot allocation problem. Armor+toughness values are not direct effective damage reduction under Apothic Attributes; physical and bypass-hit tests are still required. Other item effects, potions, Curios, different base gear and alternate enchantments could make a larger entire-pack maximum.

Source files: [Ironforged armor affix](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/affixes/armor/attribute/ironforged.json), [Adamantine affix](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/affixes/armor/attribute/adamantine.json), [Steel Touched affix](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/affixes/armor/attribute/steel_touched.json), [Guardian gem](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/gems/core/guardian.json), [Samurai gem](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/gems/core/samurai.json), [Tyrannical gem](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/gems/core/tyrannical.json), [Royalty gem](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/gems/overworld/royalty.json), [Shield ironforged affix](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/affixes/shield/attribute/ironforged.json), [Shield steel touched affix](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/affixes/shield/attribute/steel_touched.json).
