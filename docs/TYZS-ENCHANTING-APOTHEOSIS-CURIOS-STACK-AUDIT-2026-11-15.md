# Full gear ceiling audit — Apotheosis 8.9, Apothic Enchanting, Occultism Iesnium Anvil, and Curios

> **2026-11-15 · HARD SOURCE AUDIT / NOT A PLAYTEST.** TMM 1.21.1 NeoForge test pack. Pin-accurate published source checks and 164-row manifest screen; player-effective outcome and third-party Enchantment/Curios runtime registries **not yet dumped**. Machine-readable [gear-ceiling source ledger](tyzs/gear-enchant-affix-curios-ceiling-audit-v1.json) records all 99 affix IDs, 23 gem candidates, 20 added Apothic Enchanting enchantments, the 142 preliminary attribute candidates and all 164 manifest entries, with explicit source-status flags.

## 1. The core conclusion: gear power is not a single additive number

A legally optimized item can combine the **item/material/set baseline**, **one or more enchantments**, **an Iesnium +1 applied at the correct part of enchantment processing**, **rolled Apotheosis rarity + affixes**, **affix strength-overcharging**, **up to a few gem sockets**, **gem-injected attributes and enchantment levels**, and **item-specific powers**. When equipped, those can combine with **Curios, mod-specific gear attributes, form/native abilities, potions, charms, enchantment protection cap and purchased Tyz skills**.

- **Add-value** bonuses (e.g. flat Luck) add to their matching attribute in their legal slot context. **Add-multiplied-base** and **add-multiplied-total** do different things and must use the loaded engine's attribute ordering, not a naïve sum of tooltip percentages.
- **Enchantments** have separate stored level, Apothic configured max, Iesnium-upgraded level, and *effective* level affected by affix and gem events. **A bigger effective enchantment level is not always better:** Mending and one-time movement effects may not scale, and damage reduction is bounded by actual mechanics.
- **Events** such as extra loot rolls, proc healing, chance-to-dodge, durability prevention, radial mining, replacement drops and mob effects cannot be equated to raw attributes or proc'd twice from both the item and a replicated Monk skill.

## 2. Pins and direct evidence

| Component (installed test pin) | Direct source consulted | Confirmed |
|---|---|---|
| **Apotheosis 8.9.0** | [Exact-version upstream commit `84e27e3`](https://github.com/Shadows-of-Fire/Apotheosis/tree/84e27e7031c7e2ab2f2e8328daf491c88203fcbc) and [99-affix Monk inventory](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/fix/monk-tree-data-registry-2026-11-15/compat/monk/profiles/affix-inventory-apotheosis-8.9.0.json) | 99 data-defined affixes, five defined loot rarities, gem classes, source-defined Mythic sockets and affix overcharging |
| **Apothic Enchanting 1.6.4** | [Version-aligned 1.21 source](https://github.com/Shadows-of-Fire/Apothic-Enchanting/tree/1.21) (`gradle.properties` declares 1.6.4) | Eterna/Quanta/Arcana, **100 table-Eterna target**, source-added 20 enchantment definitions, 1.6.1+ enchantability-generated bonus level, per-enchantment max logic |
| **Occultism 1.227.0** | [Pinned release `990303e`](https://github.com/klikli-dev/occultism/blob/990303e/src/main/java/com/klikli_dev/occultism/client/gui/IesniumAnvilMenu.java) | Iesnium anvil checks **Apothic's maximum + 1** when Apothic is loaded, rather than vanilla max +1; normal item applicability and enchantment exclusivity still checked. XP discount exists |
| **Apothic Attributes 2.11.0** | [TMM pinned source attribute registry](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/fix/monk-tree-data-registry-2026-11-15/compat/monk/profiles/attributes-apothic-2.11.0.json) | 22 Apothic attributes alongside vanilla and other magic attributes; some are capped/boolean rather than arbitrary positive scalars |
| **Curios API 9.5.1+1.21.1** | TMM pack pin + [original Monk gear parity](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/fix/monk-tree-data-registry-2026-11-15/docs/MONK-GEAR-PARITY.md) | **Monks ARE allowed to equip Curios.** The slot count, usable bauble inventory, and affix/socket item eligibility are item/source-specific and require live validation |

**Critical distinction:** 100 Eterna is **enchanting-table power**, **not** level 100 on Sharpness/Fortune/Protection. The per-enchantment maximum depends on its own 1.21.1 definition, Apothic's 1.6.4 cap computation and any pack datapacks. The Enchantment Library improves combination/retention; it is **not** an unlimited enchant-level bypass.

### Iesnium anvil + one level

At the exact pinned 1.227.0 source, equal levels combine to +1 **only while the result is at most Apothic's configured level+1** (using Apothic integration when loaded). Applying an already over-level book, combining incompatible enchantments and combining an affix-boosted *effective* enchantment differ. **Enchantability's random +1** in Apothic 1.6.4 is a separate enchant-roll mechanism; no source proof that it and Iesnium always produce +2. Do not hardcode it as a constant. Source: [IesniumAnvilMenu.java](https://github.com/klikli-dev/occultism/blob/990303e/src/main/java/com/klikli_dev/occultism/client/gui/IesniumAnvilMenu.java), [Apothic source](https://github.com/Shadows-of-Fire/Apothic-Enchanting/blob/1.21/src/main/java/dev/shadowsoffire/apothic_enchanting/ApothicEnchanting.java).

## 3. Apotheosis affixes: all 99 source IDs covered

Apotheosis's source dataset has **99 affix definitions**, spanning armor, boots, tools, weapon, ranged, shields and shears. The standalone Monk historical source pass reviewed **98 technique analogues** (all but **Unbound/creative flight**). Only **40 bounded finite affix source candidates** are in the newer primary Monk staging shortlist. **99, 98, and 40 are different scopes; never count them as 99 simultaneous equipment upgrades or 98 mandatory Monk purchases.**

| Affix group | Count | Example capabilities |
|---|---:|---|
| `armor/attribute` | 16 | `adamantine`, `aquatic`, `blessed`, `elastic`, … |
| `armor/dmg_reduction` | 6 | `blast_forged`, `blockading`, `deflective`, `feathery`, … |
| `armor/mob_effect` | 5 | `blinding`, `bolstering`, `bursting`, `nimble`, … |
| `boots/ability` | 3 | `steadfast`, `surefooted`, `unhurried` |
| `breaker/ability` | 4 | `enlightened`, `sandforming`, `stoneforming`, `supermassive` |
| `breaker/attribute` | 4 | `destructive`, `experienced`, `lengthy`, `submerged` |
| `breaker/effect` | 2 | `omnetic`, `radial` |
| `breaker/enchantment` | 1 | `prosperous` |
| `breaker/mob_effect` | 2 | `spelunkers`, `swift` |
| `generic/attribute` | 1 | `lucky` |
| `generic` | 1 | `telepathic` |
| `melee/attribute` | 13 | `berserking`, `forceful`, `giant_slaying`, `glacial`, … |
| `melee` | 4 | `cleaving`, `executing`, `festive`, `thunderstruck` |
| `melee/mob_effect` | 6 | `bloodletting`, `caustic`, `elusive`, `omniscient`, … |
| `ranged/attribute` | 4 | `agile`, `elven`, `streamlined`, `windswept` |
| `ranged/enchantment` | 1 | `prosperous` |
| `ranged` | 2 | `magical`, `spectral` |
| `ranged/mob_effect` | 8 | `acidic`, `blighted`, `deathbound`, `ensnaring`, … |
| `shears/effect` | 2 | `gardening`, `leafforming` |
| `shield/attribute` | 3 | `ironforged`, `stalwart`, `steel_touched` |
| `shield` | 3 | `catalyzing`, `psychic`, `retreating` |
| `shield/mob_effect` | 5 | `devilish`, `galvanizing`, `reinforcing`, `venomous`, … |
| `weapon/attribute` | 3 | `nullifying`, `runebreaking`, `shredding` |
| **Total** | **99** | Source IDs, rarities and applicable item categories in JSON |

### Affix overcharging can exceed a perfect Mythic roll

A **standard max-roll affix has level 1.0**, but at this version [AffixHelper](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/main/java/dev/shadowsoffire/apotheosis/affix/AffixHelper.java) documents:

- **Sigil of Supremacy**: upgrades **every existing affix** on an item to effective **1.5**. This is a late, exceptional power source (e.g., the Endless Gateway wave-100 reward design).
- **Sigil of Malice**: upgrades **one affix** to effective **2.0** (the `Affix.MAX_LEVEL`), **usually resetting another**. Exact source chance of no reset on the lone remaining upgrade is **3.3%**, not a default guaranteed bypass.

The actual effect at 1.5 and 2.0 comes from each affix's **StepFunction** or other value producer. It is unsafe to equate level 1.5 to exactly +50% of every final tooltip; some bounded effects may clamp, some may extrapolate, and rarities/booleans may behave differently. **A four-socket max-roll Mythic item is not the highest possible affix-powered item.**

**Source-calculated overcharge values for Luck:** I checked [Placebo 9.9.4 StepFunction](https://github.com/Shadows-of-Fire/Placebo/blob/1.21/src/main/java/dev/shadowsoffire/placebo/util/StepFunction.java), which the pinned Apothic/Apotheosis affixes call. Its `get(level)` method **extrapolates beyond level 1.0**, using stepped increments. In the Mythic JSON, this yields:

| Affix | Normal level 1.0 | Supremacy level 1.5 | Malice level 2.0 |
|---|---:|---:|---:|
| Lucky (generic) | **+6 Luck** | **+7.5 Luck** | **+9 Luck** |
| Fortunate (armor) | **+5 Luck** | **+6 Luck** | **+7 Luck** |

Thus four ideal Mythic armor pieces with both Luck affixes raised by Supremacy and four Perfect Splendors apiece yield the **source-component subtotal** `4×(7.5+6+4×5) = 134 raw Luck`. That's above the **124** standard-affix subtotal and still excludes Royalty multipliers, held gear and Curios. Malice can raise one affix farther but normally resets another; it is **not** permission to place all affixes at 2.0. The data values are exact for this source combination, but actually rolling/owning four such items with all permitted affixes still requires gameplay verification.


### Sockets and gems

- [Mythic item rarity data](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/rarities/mythic.json) prescribes **four stat affixes, two basic-effect affixes, one ability**, and **1–3 sockets with 95% chance or four sockets with 5% chance**. It also has an independent durability/unbreakability roll. It does not guarantee the exact affixes you want.
- The normal [Sigil of Socketing recipe](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/recipe/sigil_add_sockets.json) specifies `max_sockets:2`. A 4-socket Mythic roll comes from the loot rarity, **not** repeatedly applying the basic sigil. The `SocketHelper` internal 16 cap is **not** a recipe-based survival guarantee of 16 sockets.
- The pinned source defines **23 gem types** across five groups; **21** are potentially reachable through current installed dimensions, while the two Twilight-specific gems should be considered inaccessible without proving their dimension/resource source exists. `unique:true` prevents duplicate copies of that **same gem type on one item**, not across the entire character. Splendor is **not unique**.

| Gem group | Types | Notable fully cut / Perfect values |
|---|---:|---|
| Core | 13 | Splendor +5 **Luck per armor socket**; Lightning +70% total movement speed on legs/boots; Warlord +35% base health chest |
| Overworld | 3 | Earth **+4** Sharpness/Protection/Fortune on suitable *already enchanted* items; Royalty **+15% total** to explicit vanilla/Apothic stats on helmet, including Luck |
| Nether | 3 | Inferno +4 Efficiency on mining tools; Blood Lord resource healing and Molten Breach combat/utility effects |
| End | 2 | Endersurge **+2 to existing enchantments** through `mode:global`; Mage Slayer defense |
| Twilight | 2 | Normally inaccessible in this pack without Twilight world/recipe; don't assume their availability |
| **Total** | **23** | Exact IDs and per-role results in machine-readable gem register |

**Royalty is not +15% to all possible modded attributes.** Its pinned list enumerates specific **vanilla and Apothic Attributes**, including Luck and crit/movement/armor, but **does not include Iron's mana, Iron's spell power or Ars spell damage**. Source: [Royalty gem JSON](https://github.com/Shadows-of-Fire/Apotheosis/blob/84e27e7031c7e2ab2f2e8328daf491c88203fcbc/src/generated/resources/data/apotheosis/gems/overworld/royalty.json).

## 4. The Luck ceiling is already far higher than our old balance assumptions

For ordinary **un-overcharged** Mythic affixes in source:

| Ingredient | Source-defined high-end value |
|---|---:|
| Generic **Lucky** affix (legal on armor, shields, certain tools/weapons) | **+6 raw Luck** per Mythic item |
| Armor-only **Fortunate** affix | **+5 raw Luck** per Mythic armor item |
| **Perfect Splendor** (not unique) | **+5 Luck** per legal armor socket |
| **Perfect Royalty** (unique per item, helmet only) | **+15% to total Luck** (alongside listed other attributes) |

Under the deliberately extreme assumption of four Mythic armor pieces, each with **both** perfect Lucky and Fortunate and **four** Perfect Splendors, a source-derived subtotal is:

`4 × (6 + 5 + 4×5) = 124 raw Luck`

This **is not a verified attainable full-build maximum**. It assumes allowed simultaneous affix rolls, four-socket pieces (the 5% branch), non-unique gem repetition, normal affix level 1.0 and no other sources. If one Splendor on the helmet is replaced with Perfect Royalty and its multiplier applies after all add-value Luck contributions, the illustrative subtotal changes to `(124−5)×1.15 = 136.85`. Extra legal held-item/Curios/temporary buffs, Supremacy/Malice, other enchanting mechanics and item restrictions may raise or reduce that further.

**Game-state matters**: `minecraft:generic.luck` is normally zero before modifiers, so a literal “Extreme +100% to current Luck” gives zero. We must define *additive Luck units* for Sea Dragon and Monk, then test where **Luck actually influences outcomes**. **Fortune, Looting, Luck of the Sea, extra-loot-roll Scavenger and actual raw Luck are different systems**. This is why the prior Sea-Luck X and Monk 0.8X values remain **unresolved**.

## 5. A single item may accumulate large effective enchant levels

For an enchantment with **Apothic configured max `B`**, these are **source-component thought experiments**, not proven effective in-game totals:

| Enchantment | Potential components on a legally compatible item | Pre-overcharge candidate |
|---|---|---|
| **Fortune** | Iesnium +1, Mythic Prosperous +4, Perfect Earth +4, Perfect Endersurge global +2 | **B + 11** |
| **Sharpness** | Iesnium +1, Perfect Earth +4, Perfect Endersurge global +2 | **B + 7** |
| **Efficiency** | Iesnium +1, Perfect Inferno +4, Perfect Endersurge global +2 | **B + 7** |
| **Protection** | Iesnium +1, Perfect Earth +4, Perfect Endersurge global +2 | **B + 7** |

### Projected numeric levels under the pinned default Apothic power formula

I went beyond the category lists and evaluated the **actual 1.6.4** source's default cap procedure. Apothic's `getDefaultMaxLevel` advances an enchantment while its minimum enchant power stays **at or below 200**. Above vanilla max level, the `DefaultMinPowerFunction` adds `slope × floor((level − vanilla_max)^1.6)` to the enchantment's vanilla linear min-cost. From the **Minecraft 1.21.1 definitions** this gives:

| Enchantment | Vanilla max | Projected default Apothic max B | Iesnium B+1 | Hypothetical effective with supported gems/affixes |
|---|---:|---:|---:|---:|
| Fortune | III | **VIII** | **IX** | **XIX** (B+11) |
| Efficiency | V | **IX** | **X** | **XVI** (B+7) |
| Sharpness | V | **IX** | **X** | **XVI** (B+7) |
| Protection | IV | **VIII** | **IX** | **XV** (B+7) |

The final column is a **conditional paper build**, not a claim the running server allows those effective values or provides the corresponding multiplied output.

**Why high effective levels matter, if actually permitted:** Minecraft 1.21.1's level-based enchantment effects mean **Efficiency XVI** would offer `16² + 1 = 257` additional mining-efficiency attribute points on a correctly held tool (compared with **26** for vanilla Efficiency V), while **Sharpness XVI** would offer `1 + 0.5×15 = 8.5` bonus attack damage (versus **3** at Sharpness V). However **Protection XV** would already contribute 15 protection points **per valid armor piece**, and the game's combined enchantment-protection cap prevents treating four such pieces as 60 independently usable protection points. **Fortune XIX** changes specific loot-table rolls, not the player's generic Luck and not every fixed reward. These are *conditional impact calculations* from [Efficiency](https://mcasset.cloud/1.21.1/data/minecraft/enchantment/efficiency.json), [Sharpness](https://mcasset.cloud/1.21.1/data/minecraft/enchantment/sharpness.json), [Protection](https://mcasset.cloud/1.21.1/data/minecraft/enchantment/protection.json), and [Fortune](https://mcasset.cloud/1.21.1/data/minecraft/enchantment/fortune.json) definitions, not demonstrated levels in this modpack.
 It assumes no custom enchant-info/data-map or hard enchant-level caps, that the item accepts both gems/affixes and their effects stack, and that Iesnium can produce the stored level before the effective buffs. Sigils of Supremacy/Malice might raise affix-based Fortune even higher. The actual enchant powers, min/next power and formulas are in the machine audit.

Sources: [Apothic maximum-level function](https://github.com/Shadows-of-Fire/Apothic-Enchanting/blob/1.21/src/main/java/dev/shadowsoffire/apothic_enchanting/ApothicEnchanting.java), [power evaluator](https://github.com/Shadows-of-Fire/Apothic-Enchanting/blob/1.21/src/main/java/dev/shadowsoffire/apothic_enchanting/PowerFunction.java), Minecraft 1.21.1 [Fortune](https://mcasset.cloud/1.21.1/data/minecraft/enchantment/fortune.json), [Efficiency](https://mcasset.cloud/1.21.1/data/minecraft/enchantment/efficiency.json), [Sharpness](https://mcasset.cloud/1.21.1/data/minecraft/enchantment/sharpness.json), [Protection](https://mcasset.cloud/1.21.1/data/minecraft/enchantment/protection.json).

All these require the enchantment to be present when its bonuses are `existing`-mode; the two unique gems may coexist with one another if their types are distinct and valid for that item. The gem and affix modifiers may apply as **effective-level events**, not stored NBT, and strict enchant level caps can block further power. **Prosperous may be stronger after Supremacy/Malice**; therefore B+11 is *not* the universal absolute Fortune ceiling.

A higher Protection display must still respect effective damage/EPF caps; Efficiency and mining speed combine nonlinearly; Miner's Fervor is **exclusive with Efficiency**; and overwrought Fortune/Scavenger/Apotheosis loot cannot grant duplicated fixed boss rewards or bypass pack gates by design. Validate actual tool output and 1000+ relevant legal blocks/kills instead of extrapolating purely from tooltip numbers.

## 6. Apothic Enchanting adds 20 separate enchantments

Exact source-defined candidates (the mod's own `max_level`; **effective Apothic configured caps may differ**):

| Enchantment | Source max level | Material balance impact |
|---|---:|---|
| `berserkers_fury` | 3 | burst HP costs and potion effects |
| `boon_of_the_earth` | 5 | bonus block loot rolls |
| `chainsaw` | 1 | multi-block cutting |
| `chromatic` | 1 | shear variant |
| `crescendo_of_bolts` | 5 | extra projectiles |
| `endless_quiver` | 1 | ammo conservation, incompatible Infinity |
| `growth_serum` | 1 | animal growth |
| `icy_thorns` | 3 | slow retaliation, exclusive with Thorns |
| `infusion` | 1 | infusion ingredient, no direct innate scale |
| `knowledge_of_the_ages` | 3 | drops to XP, conflicts with gear loot economy |
| `life_mending` | 3 | health paid repair, mutually exclusive Mending |
| `miners_fervor` | 5 | mining boost, mutually exclusive Efficiency |
| `natures_blessing` | 3 | bonemeal crop effect |
| `rebounding` | 3 | reflective knockback |
| `reflective_defenses` | 5 | projectile/attack reflect |
| `scavenger` | 3 | extra loot roll |
| `shield_bash` | 4 | attack effects |
| `stable_footing` | 1 | movement control |
| `tempting` | 1 | attract animals |
| `worker_exploitation` | 1 | special shearing output |

Apothic Enchanting also overrides **vanilla Protection and Sharpness definitions** in its generated data and can modify caps for most enchantments. This **20-name list does not exhaust Minecraft vanilla enchantments or third-party spell/weapon enchantments**, and even a valid named enchant may fail to apply to a given modded item if its enchantment tag/slot does not match. The exact **loaded enchantment registry** and level cap/data-map need a client/server dump.

## 7. Every major equipment contributor in the pack

The linked [machine ledger](tyzs/gear-enchant-affix-curios-ceiling-audit-v1.json) screens **all 164 selected manifest entries (155 enabled in the current test snapshot)**. That screening found **five core stack systems, 41 direct gear/stat candidates, 13 cross-mod candidates, 48 infrastructure libraries, 46 with no identified direct gear modifier, two legacy retiring mods, nine inactive selections**. This is an exhaustive **manifest screen**, **not** proof every enchanted item/data-driven registry was individually verified.

| Role | Notable modpack providers | What to count |
|---|---|---|
| **Core affix/enchant/socket** | Apotheosis, Apothic Enchanting, Apothic Attributes, Occultism | 99 affixes, 23 gems, all enchanting/proc metadata, Iesnium +1, world-tier and affix overcharge |
| **Spell attributes and worn mage gear** | Iron's Spells, Ars Nouveau, Goety, Malum, Eidolon, Neo Vitae, Forbidden & Arcanus | Exact personal max mana, school power, general spell power, casting speed, damage, discounts and armor/trinket gear |
| **Curios/baubles/relics** | Curios, Artifacts, Relics, Reliquified Artifacts, Reliquary, Enigmatic Legacy+, Botania Revived / Extra Botany, JAWR, Draconic Evolution | Slot count and exclusivity, item effects, relic XP/levels, equip requirements, duplication risks, actual armor restrictions |
| **Alternative pinnacle gear** | ProjectE **Gem Armor**, Draconic modular gear and possible body Curio, Cataclysm boss weapons, enchanted occult tools, Neo Vitae Sentient Armor, magic gear from other mods | Real legality and tiers; **not all are Apotheosis-affix or socket compatible** |
| **Other enchanting / artifact candidates** | Aquaculture, Forbidden & Arcanus, Feywild, Astral Sorcery, Enchanted: Witchcraft, Hexerei, Mahou Tsukai, Iron's and addon bridges | Source item tags, extra enchantment IDs, conditional set buffs, recipe/source gates and stack-compatible slots |
| **Runtime scripting and mechanics** | KubeJS, Curios, Apotheosis, shared compatibility code and FTB Quests rewards | Datapacks can change enchant effects and item tags, and rewards can bypass normal rarity acquisition; audit loaded pack, not just upstream docs |

### Mana / spell-power caveat

The existing Monk candidate inventory has **142 source-level player attribute candidates**: 30 vanilla, 22 Apothic, 26 Iron's, 9 Ars, 15 Malum, 27 Goety, 4 Eidolon, 2 Forbidden & Arcanus, 7 Neo Vitae. These include some mob/item-only candidates and miss unknown addons. **The default 99 Apotheosis affixes do not amount to a universal bonus to every one of those 142 attributes.** For example, **Iron's max mana and school-specific spell power** are distinct stats from **Ars max mana and spell damage**, and Royalty is explicitly restricted to a chosen list.

A fully geared Dragon's magic lead needs a **per-spell-source equipment build**, not multiplication of every generic affix as though it were spell power. A fully optimized Monk must be compared to that Dragon after **both characters' actual usable Curios and spellcasting attributes** are included.

## 8. Explicit correction: Monks are Curios-compatible

Monks are barred from **ordinary worn armor and conventional weapons** in the intended class mode, but **Curios remain allowed**—including the candidate Draconic Evolution body-slot chestpiece if its exact 1.21.1 integration works. Curios is not automatically an Apothic affix loot category. Do **not** assume a Curio can take 4 gem sockets, Lucky/Fortunate affixes or all armor enchantments unless the item is actually classified as supported.

Use **four distinct validation profiles**, not the old oversimplified naked-vs-equipped picture:

1. **Ordinary maxed Monk build:** Monk intrinsic abilities **+ best legal Curios**; no conventional armor or weapon.
2. **Ordinary maxed specialist build:** specialist intrinsic abilities **+ full level-appropriate enchanted/Affixed armor and tools/weapons + best legal Curios**.
3. **Strict naked Chaos Guardian benchmark:** full Monk skills **without any worn equipment, including Curios**, and without weapons. This **preserves the previous explicit challenge requirement** as a separate, stricter test; it does *not* imply Curios are generally prohibited.
4. **Equipped optimized versus theoretical extreme:** compare representative endgame gear (realistically obtainable) against four-socket, perfect-max-roll, Supremacy/rare-Malice edge cases. Do **not** set normal Monk skill costs to match a one-in-a-million theoretical combination.

### Revised noncombat ratio definition

For a comparable property `Y`:

`Y_Monk(intrinsic + legal Curios) ≈ 0.80–0.90 × Y_Specialist(skills + legal optimized conventional equipment + Curios)`

Use **80%** for generic Luck (Sea Dragon X → Monk 0.8X) and **85%** for other truly comparable noncombat outputs, after actual effect stacking. **Inner Fortune**, **Inner Reservoir** and **Worldly Cultivation** need their rank curves **recomputed after shared Curios effects** are measured. Do not make Monk's intrinsic Luck equal to 0.8×Sea's *entire* raw stat **and then add Monk Curios on top**; that would overshoot the proposed balance target.

## 9. Required next test measurements, with acceptance notes

1. **Loaded registries**: dump enchant IDs/caps (including vanilla/other mods), max loot/combine/strict level cap, player attribute instances and caps, Curios slot types and each candidate item's legal loot category/enchantability.
2. **Enchant +1**: maximal Eterna100, world tier, enchanting Enchantability luck, two equal max enchanted books → Iesnium combination; compare Vanilla anvil, Iesnium, library and final tool.
3. **True Luck**: compare Mythic three-socket practical vs rare four-socket with Lucky and Fortunate at level1, then Supremacy level1.5, Malice level2 where legal; insert Perfect Splendor and Royalty, record raw Luck and event frequency; add legal Curios on **both** builds.
4. **Fortune and mining**: combine effective Fortune with Prosperous, Earth and Endersurge; Efficiency vs Miner's Fervor; block-harvest tiers and loot correctness, real break speed and anti-duplication.
5. **Combat/defense**: item enchant level after affix/gems, max effective Protection, armor/armor toughness, overcharged damage reduction, dodge/crit/lifesteal, capped cooldown and item durability. Test damaging the real Chaos Guardian across its legal phases.
6. **Spellcasting**: separate 26 Iron, nine Ars and other source attributes; record mana before buffs, with Curios, equipped armor/weapon and enchant+affix+gem stages. Ensure no duplicate cross-mod payment/bonus.
7. **Curios**: compare Monk without Curios, Monk with full legitimately supported Curios, and geared specialist with Curios. Confirm Draconic body-slot modules and power source without assuming an armor slot.
8. **Acquisition**: normal endgame versus theoretical perfect rarity/4 sockets, Reforging/Augmenting, Sigils of Supremacy and Malice. Establish a realistic gearing tier to use for numerical balancing.
9. **Regression**: repeated equips/unequips, dimension changes, source hybrid forms/cures, death/tombstone, storage, enchant item copies, client/server attribute sync and duplicate bonus sources.

**No client/server QA or packed JAR registry extraction was completed in this pass.** Structural source research and an inventory screen do **not** certify an exact full equipment ceiling. The follow-on should produce a pinned runtime inventory rather than prematurely freezing Luck, mana or Monk stat numbers.
