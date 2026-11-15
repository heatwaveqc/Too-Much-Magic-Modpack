# Noncombat Monk parity and Nature / Blood / Other magic affinity audit

> **Design working pass, 2026-11-15.** No live Tyz skill JSON or Loot/Forge tag registrations modified. This pass adds calibrated **late Monk target skills** in the main [strength catalog](tyzs/nine-tree-strength-first-pass-v1.json) and a **magic affinity rule ledger**. See [machine-readable parity contract](tyzs/monk-noncombat-parity-v1.json) and [40-system affinity registry](tyzs/magic-affinity-nature-blood-other-v1.json).

## A. New cross-tree noncombat guideline

The capped fully progressed **Monk without conventional armor/weapons, but wearing legal Curios** should have **85% of the theoretically highest legally attainable final noncombat capability** of a player with a comparably invested, fully optimized **relevant specialist tree plus all legally worn/held gear**. **85% applies to every comparable noncombat specialty, including Sea Dragon Luck**; this supersedes the earlier 80% exception. This is a fully optimized *build* comparison: include enchantments, Apotheosis affixes, reforges, gems, Curios, armor and legitimate enchanted tools where relevant.

**Specialist X is not a Tyz skill tooltip percentage.** It is the authoritative final measured result, after actual engine attribute calculations and all appropriate gear. For compatible effects, the intended Monk skill final addition is `max(0, desired_ratio × specialist_full_geared_output − current_Monk_plus_Core_output)`. If the skill has `N` ranks, a first linear slope is that amount divided by `N`. Round/clamp/discretize using actual event rules rather than assuming all outputs are floats. A 50-level skill uses milestone gating even though players can purchase individual ranks.

**Important exceptions:** This is for *comparable noncombat outcomes*, not universal access to blood physiology, Lich phylacteries, Dragon bodies, Fae allegiance or transformed-only actions. Damage, health, damage resistance, combat regeneration, boss and PvP scaling remain governed by the separate gear-normalized combat benchmark. Do not multiply vanilla Luck by a percentage of zero and claim a change; actual Luck bonus requires a measured nonzero unit and supported consumer events.

### Luck example: exact 0.85× theoretical optimum

Let **X = a fully optimized Sea Dragon's final actual Luck**, including **all** Sea Dragon talents, armor/Curios, enchanted and Apotheosis-modified gear. Capped **Monk + legal Curios Luck = 0.85X**, not Monk intrinsic 0.85X plus Curios again. Illustrative values (not actual Minecraft Luck caps):

| Sea final Luck X | Maxed Monk target 0.85X |
|---:|---:|
| 50 | 42.5 |
| 100 | 85 |
| 200 | 170 |

For a **hypothetical normalized index of 100** (75 from the Sea's 50 ranks, 15 from armor/tools and 10 from its legal Curios), assume Monk's legal Curios contribute 10. Its **intrinsic** 50-rank Inner Fortune needs 75 additional units to reach 85 total. This is an illustration, not measured Minecraft Luck:

| Monk Inner Fortune rank | Monk innate addition | Monk including Curios | Sea *skill-only* index at same rank |
|---:|---:|---:|---:|
| 0 | 0.0 | 10.0 | 0.0 |
| 1 | 1.5 | 11.5 | 1.5 |
| 5 | 7.5 | 17.5 | 7.5 |
| 10 | 15.0 | 25.0 | 15.0 |
| 25 | 37.5 | 47.5 | 37.5 |
| 50 | 75.0 | 85.0 | 75.0 |

The 100-index example **must not be coded as a real +100 Luck attribute**. It illustrates reverse-calibration, and holding Sea's finished loadout at 100 for the reference column is intentional. Real values require a verified source pin and a buildable fully optimized Luck gear kit. **Luck, Fortune and Looting are different mechanics**; only actual Luck-aware loot sources should change via Luck.

### Proposed Monk late rank-level candidates

These are **additional conceptual entries**; they do not replace any native Monk license, equip toggle, harvest tier or Apotheosis rarity.

| Skill | Tier / strength label | Rank cap | Monk target at cap | Implementation requirement |
|---|---|---:|---|---|
| **Inner Fortune** (`tmm_monk_inner_fortune`) | Extreme +100% / rank as **budget** | 50 | **85%** of optimized Sea Dragon Luck **including Monk Curios** | True equipment-free personal Luck strength, aiming at rank cap for exactly 85% of the optimized, fully geared Sea Dragon Luck attribute, after actual source effects and gear bonuses have been counted. |
| **Inner Reservoir** (`tmm_monk_inner_reservoir`) | Extreme +100% / rank as **budget** | 25 | **85%** of optimized Dragon personal max mana **including Monk Curios** | Personal magical resource reserve, targeting ~85% of geared full Dragon maximum capacity in each comparable mana-based spell system, not equivalent spell damage or mana regeneration. |
| **Worldly Cultivation** (`tmm_monk_worldly_cultivation`) | Strong +50% / rank as **budget** | 25 | **85%** of optimized Forest Dragon personally performed farming **including Monk Curios** | Personally controlled crop growth, legal breeding and harvesting efficiency, calibrated to ~85% of fully optimized Forest Dragon outcome for the same source action; reinforces native Monk Gardening rather than installing automated farm machinery. |

The tier and cap are *proposed investment shapes*. **The 85% parity rule overrides the raw nominal multiplier** wherever gear, preexisting Core Skills ranks, zero baseline, or nonlinear events make it wrong. The existing Monk Body Speed, Barehand Efficiency, Fortunate Hands, and Looting Palm must also be checked against properly enchanted specialist movement, mining, farming tools and weapons before their numeric ranks can be considered calibrated.

### First individual metric comparisons

| Noncombat benchmark | Primary fully geared specialist | Monk response | Cap target | Source/mechanics work still needed |
|---|---|---|---|---|
| **luck** | `tmm_dragon_sea` | `tmm_monk_inner_fortune` | **85%** | Fortune, Looting and generic loot amount are NOT the Luck attribute |
| **personal mana capacity** | `tmm_dragon_cave|tmm_dragon_sea|tmm_dragon_forest` | `tmm_magic_reservoir_1` + `tmm_monk_inner_reservoir` | **85%** | Does not multiply one shared Ars/Iron pool twice; industrial mana tanks excluded |
| **legal block break throughput** | `tmm_dragon_cave` | `tmm_monk_barehand_efficiency` | **85%** | Cannot compare a Monk who lacks the required native harvest-tier unlock against a tool that can mine the block |
| **crop yield and growing** | `tmm_dragon_forest` | `tmm_monk_worldly_cultivation` + `tmm_monk_fortunate_hands` | **85%** | Never multiply the same loot event twice or count machines/automatic farms |
| **animal husbandry** | `tmm_dragon_forest` | `tmm_monk_worldly_cultivation` | **85%** | Does not create extra animals from a single parent; maturation vs breeding separate measures |
| **underwater movement** | `tmm_dragon_sea` | `tmm_monk_body_speed` + `tmm_monk_glide_control` | **85%** | Do not treat flying, swimming and teleporting as the same measurement |
| **general ground mobility** | `tmm_hunter|tmm_werewolf|tmm_dragon_forest` | `tmm_monk_body_speed` + `tmm_monk_balanced_landing` | **85%** | No free creative flight; respect collision and traversability |
| **fortune and looting** | `tmm_dragon_forest|tmm_hunter` | `tmm_monk_fortunate_hands` + `tmm_monk_looting_palm` | **85%** | Separate from generic Luck and exclude static boss/advancement drops |

**Not directly comparable:** personally usable blood magic resource and personal spell potency. Blood/soul biology is not a universal skill; spell potency is combat-adjacent and is independently gear-normalized. The native discipline and one-wallet MXP progression remain unchanged.

**Economic caution:** At the current `1.2^P` lifetime-SP MXP divisor, taking every 25/50-rank Monk endpoint to its cap is potentially unreachable in ordinary gameplay. The numerical cap is an endgame target, not proof the XP economy can attain it. Before ship, model maximum practical SP per character and account for actual form-stage gates, achievements and source XP bonuses.


**Equipment-source correction:** The [full equipment and enchanting ceiling audit](TYZS-ENCHANTING-APOTHEOSIS-CURIOS-STACK-AUDIT-2026-11-15.md) confirms Curios are legal on both sides, an exact Occultism 1.227.0 Iesnium +1, 99 affix definitions, gem enchantment modifiers, and **Sigil overcharges above normal Mythic affix values**. So 0.85×Sea's fully geared Luck is a comparison of BOTH *final* outcomes with legal Curios, not a raw skill rank multiplier and not intrinsic Monk 0.85×Sea followed by extra Curios. The prior no-worn-gear Chaos Guardian trial is a stricter, separate test.\n
**Owner clarification — capped noncombat, 2026-11-15:** The capped Monk target is now **exactly 85% of the theoretical highest LEGALLY ATTAINABLE fully specialized and fully equipped build**, including Luck (superseding the former 80% Sea Dragon exception). This is not 85% of a representative build and not 85% of the registered attribute hard ceiling unless an all-in legal build actually reaches that ceiling. Both Monk and specialist include real eligible Curios only once. See [source-registered bounds audit](tyzs/theoretical-attribute-hard-caps-v1.json) and [Curios item/slot audit](tyzs/curios-all-in-eligibility-v1.json); banned Seven Deadly Sins Ring excluded from all legal builds.

## B. Nature / Blood / Other — definitions and ownership

- **Nature:** legitimately player-attributed nature, druidic, plants, animals and fae spell effects. Werewolf Nature bonus and Lich Nature penalty apply only to eligible personal results; machines, natural world growth and unrelated automated producers do not receive buffs or debuffs.
- **Blood:** magic that actually spends or derives power from blood, life-force/EV personal resources, or a source-registered Blood spell school. Lich Blood specialization applies here, not automatically to generic souls or every red particle effect.
- **Other:** arcane, celestial, soul/necromancy, demon/occult, storm/elemental, technoalchemy and cross-mod framework outputs that have no specific verified Nature/Blood classification. Separate **fae, demonic and aquatic/ice/storm** secondary tags still serve Forest/Cave/Sea Dragon specialties without falsely changing the primary category.

**Classification priority:** exact **spell-school ID → source-verified action/effect tag → validated parent-mod theme for eligible personal actions → held/Other**. Source-defined spell schools beat mod-wide themes. A recipe, altar, enchanting book, mob spawning action or item that contains blood/flowers/herbs does not by itself become a Blood/Nature spell. No blanket event-multiplication, machine buffs or repeated XP from cross-mod adapter bridges.

### Major magic roster — 40 audited projects and integrations

Primary tags are a convenient orientation. Mixed-use mods require the effect-level overrides below; this list is not a claim of exact pinned spell/action ID testing.

#### Nature (5 projects)

| Mod or bridge | How we categorize its effects | Exclusions / verification |
|---|---|---|
| [Botania Revived](https://www.curseforge.com/minecraft/mc-mods/botania-revived) | **Nature** · nature-themed floral mana and devices | Exclude mana generating flowers, spreaders, pools, runic altars, automated producers, Gaia Guardian reward drops. |
| [Extra Botany Revived](https://www.curseforge.com/minecraft/mc-mods/extra-botany-revived) | **Nature** · Botania addon floral/elf mana effects | Do not assume every addon armor stat or item action is a nature spell; confirm event and owner. |
| [Roots Classic](https://www.curseforge.com/minecraft/mc-mods/roots-classic) | **Nature** · druidic natural-ingredient powders and wands | This is ROOTS CLASSIC, not Roots 4; avoid source/mana APIs from the other project. |
| [Feywild](https://www.curseforge.com/minecraft/mc-mods/feywild) | **Nature** · fey magic, seasons and fae summons | Fey creature identity alone does not convert every mob attack, quest, or automatic activity into player-cast Nature magic. |
| [Totemic / Fenn's Totemic](https://www.curseforge.com/minecraft/mc-mods/fenns-totemic) | **Nature** · ritual/totemic ceremonial natural magic | Check exact pinned Fenn's fork mechanics; do not buff passive world totems/machines blindly. |

#### Blood (2 projects)

| Mod or bridge | How we categorize its effects | Exclusions / verification |
|---|---|---|
| [Neo Vitae](https://modrinth.com/mod/neovitae) | **Blood** · blood/life/EV/sigils; Spiritus hybrid | The Ara Vitae altar, EV pipes, tanks and passive automation remain excluded from personal spell-power buffs. |
| [EvilCraft](https://evilcraft.rubensworks.net/) | **Blood** · blood-powered rituals and devices; vengeance spirits | Blood Infuser, Spirit Furnace, Purifier and other machine throughput are not player spell-casts; vengeance spirit techniques require subtyping. |

#### Other (33 projects)

| Mod or bridge | How we categorize its effects | Exclusions / verification |
|---|---|---|
| [Iron's Spells 'n Spellbooks](https://github.com/iron431/irons-spells-n-spellbooks/blob/1.21/src/main/java/io/redspace/ironsspellbooks/api/registry/SchoolRegistry.java) | **Other** · multi-school spells; explicit BLOOD and NATURE schools | `irons_spellbooks:blood` -> Blood, `irons_spellbooks:nature` -> Nature, all other registered schools -> Other. School ID rather than spell display name. |
| [Just Another Witchery Remake (JAWR)](https://www.curseforge.com/minecraft/mc-mods/just-another-witchery-remake) | **Other** · witchcraft mix: blood/vampirism, herbalism and rites | Source-check blood/vampire actions -> Blood; confirmed plant/herb/growth personally cast spells -> Nature; other curses, sigils and generic ritual magic -> Other. |
| [Enchanted: Witchcraft](https://www.curseforge.com/minecraft/mc-mods/enchanted-witchcraft) | **Other** · herbal witchcraft, circle rites, brewing and poppets | Herbology, grow/herb effects may be Nature; curses/poppetry remain Other unless explicit blood mechanics. Brewing ingredients alone are not enough. |
| [Hexerei](https://www.curseforge.com/minecraft/mc-mods/hexerei) | **Other** · herbal cauldron witchcraft and artifacts | Herbal content is a Nature candidate only when a source-verified effect actually expresses nature magic; cauldron crafting not automatically a spell. |
| [Primal Magick](https://www.curseforge.com/minecraft/mc-mods/primal-magick) | **Other** · multiple primal sources; elemental research and spells | Use actual source/aspect identity; flora/life/earth themed spells may be Nature when verified, generic elemental/arcane effects Other. |
| [Ars Nouveau](https://www.curseforge.com/minecraft/mc-mods/ars-nouveau) | **Other** · programmable multiglyph personal spells and source automation | Nature-tagged growth/plant outcomes only if explicitly classified; spells are not wholesale Nature because Archwood exists. No machine automation buff. |
| [Ars Elemental](https://www.curseforge.com/minecraft/mc-mods/ars-elemental) | **Other** · four elemental schools and foci | Elemental is not automatically Nature; verify actual school/element tag per spell; water/ice/storm are Other with Sea Dragon sub-affinity. |
| [Astral Sorcery](https://www.curseforge.com/minecraft/mc-mods/astral-sorcery) | **Other** · starlight and constellations | Cosmic/starlight magic is not automatically Nature even if nighttime/outdoors; multiblock starlight altar is excluded from personal spell-power buffs. |
| [Eidolon: Repraised](https://www.curseforge.com/minecraft/mc-mods/eidolon-repraised) | **Other** · soul shards, necromancy, theurgy, ritual and artifice | Sacrifice, undead and soul shards do not automatically mean Blood. Require explicit blood-as-resource action for any Blood override. |
| [Goety: The Dark Arts](https://www.curseforge.com/minecraft/mc-mods/goety) | **Other** · soul-energy magic, minions, curses and necromancy | Soul energy is not blood. Cave Dragon demonic affinity is a separate secondary tag, not the Blood primary category. |
| [Goety Cataclysm](https://www.curseforge.com/minecraft/mc-mods/goety-cataclysm) | **Other** · Goety addon focused on Cataclysm servants and focus effects | Do not buff all Cataclysm bosses just because a Goety add-on references them. |
| [Occultism](https://modrinth.com/mod/occultism) | **Other** · demonic/otherworld spirit summoning and rituals | Blood-like sacrifices do not by themselves make spells Blood. Cave Dragon demonic affinity can key off a separate tagged source. |
| [Malum](https://www.curseforge.com/minecraft/mc-mods/malum) | **Other** · spirit arcana, soul and totem magic | Spirit and blight are not Blood; Runewood trees are not sufficient to make the whole mod Nature. |
| [Gaze (Malum addon)](https://www.curseforge.com/minecraft/mc-mods/gaze-a-malum-addon) | **Other** · Malum spirit/spell addon | Inherit Malum source context; no assumed blood tag. |
| [JAWR: Forbidden Magic](https://www.curseforge.com/minecraft/mc-mods/jawr-forbidden-magic) | **Other** · soul magic, lichdom and phylactery | Soul magic and lichdom are not automatically Blood; some explicit lifeblood effects require a per-action secondary Blood classification. |
| [Forbidden & Arcanus](https://www.curseforge.com/minecraft/mc-mods/forbidden-arcanus) | **Other** · Aureal, magical artifacts and occult elements | Do not map souls/occult aesthetics to Blood without actual blood-powered effects. |
| [HexCast NEOFORGED](https://www.curseforge.com/minecraft/mc-mods/hexcasting) | **Other** · programmable amethyst/media hex spells | Pattern outcome might be Nature-like; never classify solely by the port's media/amethyst cost. |
| [Psi](https://www.curseforge.com/minecraft/mc-mods/psi) | **Other** · programmable spellcasting using Psi CAD | A construct that makes crops grow isn't necessarily tagged nature until a verified spell effect is classified. |
| [Psi: Tweaks and Additions](https://www.curseforge.com/minecraft/mc-mods/psitweaks) | **Other** · Psi extended components/automation | Exclude industrial Psionic Generator and energy conversion loops from nature/blood personal magic. |
| [Mahou Tsukai](https://modrinth.com/project/mahou-tsukai) | **Other** · special effects, spell circles and personal mana | Some spells have sacrifice/HP themes; require actual blood power debit to tag Blood; do not tag all circles. |
| [Pastel](https://www.curseforge.com/minecraft/mc-mods/pastel) | **Other** · color-resonance magic and research | Color and natural energy aesthetics are not enough to classify as Nature. |
| [Scriptor Magicae](https://www.curseforge.com/minecraft/mc-mods/scriptor-magicae) | **Other** · language-built programmable spells | Default Other until explicit typed Nature/Blood action, not based on ingredient words. |
| [Theurgy](https://www.curseforge.com/minecraft/mc-mods/theurgy) | **Other** · sulfur/salt/mercury alchemy and matter transmutation | Machinery, sulfur processing and item duplication are not player Blood/Nature spells. |
| [Embers Re-Ignited](https://www.curseforge.com/minecraft/mc-mods/embers-rekindled) | **Other** · ember-fueled technoalchemy and production | Exclude production throughput unless separate crafting trait explicitly authorized. |
| [Ars Additions](https://www.curseforge.com/minecraft/mc-mods/ars-additions) | **Other** · Ars Nouveau addon | Classify by final effective Ars glyph/cast, not addon package. |
| [Ars Controle](https://www.curseforge.com/minecraft/mc-mods/ars-controle) | **Other** · Ars Nouveau addon/controller tools | Controller/item mechanics by concrete event; do not award bonus twice. |
| [Ars Elemancy](https://www.curseforge.com/minecraft/mc-mods/ars-elemancy) | **Other** · Ars Nouveau elemental glyph addon | Elemental school defaults Other; classify confirmed nature glyph per source. |
| [Ars Ocultas](https://www.curseforge.com/minecraft/mc-mods/ars-ocultas) | **Other** · Ars Nouveau/Occultism ritual bridge | Jarred mobs and sacrificial ritual components do not by themselves make the result Blood; tag effect based on resource/owner. |
| [Ars Unification](https://www.curseforge.com/minecraft/mc-mods/ars-unification) | **Other** · Ars cross-system integration | Bridge must not count the same spell twice. |
| [Ars 'n Spells](https://www.curseforge.com/minecraft/mc-mods/ars-n-spells) | **Other** · Ars Nouveau and Iron's spells interoperability | Use Iron Blood/Nature registered school for Iron spells; classify Ars glyph spells by verified context. Cross-cast must only get one affinity. |
| [Summoning Rituals](https://www.curseforge.com/minecraft/mc-mods/summoning-rituals) | **Other** · data-driven ritual framework, not a school | No automatic Nature or Blood from recipes requiring herbs, skulls, blood drops, or animals; classify actual effect and consumed power source. |
| [ProjectE](https://www.curseforge.com/minecraft/mc-mods/projecte) | **Other** · transmutation/EMC economy, not personal cast school | No class affinity buff to EMC machines or progression-gated items. |
| [Draconic Evolution](https://www.curseforge.com/minecraft/mc-mods/draconic-evolution) | **Other** · FE power systems, gear and Chaos Guardian | Tech gear matters to Monk-versus-equipped-specialist balance; not a personal Nature/Blood magic school. |

### Mixed-system rules that must be coded individually

| Real or provisional source key | Category | Precision / verification status |
|---|---|---|
| `irons_spellbooks:blood` | **Blood** | official registered SchoolRegistry source; all committed player Blood-school spell casts |
| `irons_spellbooks:nature` | **Nature** | official registered SchoolRegistry source; all committed player Nature-school spell casts |
| `witchery:vampire_blood_consumption` | **Blood** | RULE_CANDIDATE_EXACT_ACTION_IDS_PENDING; only actual blood-consumption / blood-powered effects, not becoming vampire itself |
| `witchery:herbal_growth` | **Nature** | RULE_CANDIDATE_EXACT_ACTION_IDS_PENDING; verified personal herbal/growth effect only |
| `ars_nouveau:nature_growth` | **Nature** | RULE_CANDIDATE_EXACT_GLYPH_CHAIN_IDS_PENDING; explicit growth/plant personal spell only |
| `enchanted:herbology` | **Nature** | RULE_CANDIDATE_EXACT_EFFECT_IDS_PENDING; source-verified personal nature effect only |
| `hexerei:herbal_magic` | **Nature** | RULE_CANDIDATE_EXACT_EFFECT_IDS_PENDING; source-verified herbal magic effect only |
| `primalmagick:verdant_source` | **Nature** | RULE_CANDIDATE_EXACT_SOURCE_IDS_PENDING; verified nature/life source spell only |

The two Iron school keys **`irons_spellbooks:blood` and `irons_spellbooks:nature` are source-verified registered categories**. The other *named rule candidates* in the table are descriptive placeholders, **not** proven in-game registry IDs. Before implementation, read the pinned 1.21.1 mod JAR/source and replace placeholders with actual events, schools or item IDs.

### Impact on existing trees

| Tree | Effect of this audit |
|---|---|
| **Lich** | Buff **Blood** personal spells (Neo Vitae sigils/verified actions, EvilCraft blood-powered personal effects, Iron Blood school, eligible JAWR vampire blood actions if mechanically compatible) while weakening **Nature** personal spells including Botania, Roots Classic, Feywild, Iron Nature school and verified botanical/witchery actions. Do not punish all Iron spells or boost all necromancy under the Blood label. |
| **Werewolf** | Nature bonus only to legitimate active Nature magic; JAWR is sole physiology source. Its natural regeneration remains **health**, not mana. |
| **Forest Dragon** | Fae secondary tags (Feywild/verified fae spells or controlled fae) and own agriculture; do not treat all woodland blocks or every Ars spell as fae. |
| **Cave Dragon** | Demonic secondary tags from legitimate Goety/Occultism and other named demon effects; demon magic is usually **Other**, not Blood. |
| **Sea Dragon** | Aquatic/ice/storm/abyssal secondary tags from source-verified casts; by default **Other**, not Nature simply because water occurs in nature. Sea **Luck** remains distinct from loot duplication. |
| **Monk / Core Skills** | Appropriate universal personal-source scaling, and gearless 85% of fully geared specialist noncombat measured at cap; no auto-grant of species-specific ritual/biology. |

### Immediate source / gameplay acceptance gates

1. **Iron's registered schools:** bind source spell-school IDs `blood` and `nature`, and ensure crosscasts through Ars 'n Spells receive **one** final categorization and one payout.
2. **JAWR + Forbidden Magic:** exact pinned sources for blood-fed actions versus soul necromancy; no automatic Blood label for soul, lich or phylactery. Lich nature penalty can only hit legitimate personally activated Nature effects, never world mana producers.
3. **Neo Vitae / EvilCraft:** separate handheld active magic from industrial altar/EV/blood item processing and passive storage. Source-specific player attribution and single-debit accounting.
4. **Botania / Roots Classic / Feywild:** confirm player item/cast/ritual action hooks; no auto nerfing passive flowers/automation for lich players.
5. **Other mixed mods:** verify per-action or school-type source IDs for Ars, Enchanted, Hexerei, Primal Magick, and programmables. Fae/demon/sea subtypes must be orthogonal to Nature/Blood/Other.
6. **Measured noncombat cap:** build actual Sea Dragon full Luck equipment configuration; measure raw Luck and its effects; compute Monk target **0.85X** and per-rank curve. Similarly measure Dragon max mana, Cave legal mining, Forest growth/yield, and movement. Test all at comparable endgame progression.

**State of implementation:** this is a taxonomy, first individual rank pass and measurement contract, **not** a 1.21.1 NeoForge compiled skill mod or a playable rebalanced class tree. The 32 original Tyz stock skills remain unharmed and unmoved.
