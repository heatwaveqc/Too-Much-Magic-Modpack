# Ars Nouveau pinned armor, threads and Reactive activation

Exact `ars_nouveau-1.21.1-5.13.5.jar`, SHA-1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; direct JSON/CFR0.152. Evidence: AlterationTable/Tile, ArmorUpgradeRecipe, AnimatedMagicArmor, ArmorPerkHolder/StackPerkHolder/IPerkHolder, APIRegistry/PerkRegistry/PerkUtil, nineteen functional perks, PerkEvents/EventHandler/PerkLivingEntity, ReactiveEvents/ReactiveCasterData/PacketReactiveSpell. [Parent](ars-nouveau.md), [Apparatus and enchantment acquisition](ars-nouveau-pinned-crafting.md), [personal casting/mana](ars-nouveau-pinned-casting.md). No armor/repair/death/Reactive client acceptance.

## Choose armor and upgrade its thread capacity

Native Notebook **Items/Equipment → Armor → armor / alteration_table / named thread**, including PerkDiagramEntry diagrams for each armor family. Getting Started → Magical Crafting cross-links armor. JEI Enchanting Apparatus shows exact equipment/thread ingredients; Notebook armor upgrade pages use first_armor_upgrade and second_armor_upgrade. These are optional equipment branches alongside spellbook progression; armor families are choices, not a compulsory Iron→Gold→Diamond tier ladder.

Use Magebloom→four Magebloom Fiber (shapeless). For each family, enchant matching vanilla piece with **four Magebloom Fiber on separate pedestals**, sourceCost0, keepNbtOfReagent=true: Iron→Arcanist, Gold→Sorcerer, Diamond→Battlemage. Four pieces each (Hood/Robes/Leggings/Boots), twelve recipes in crafting companion. Reagent components are copied and damage reset by ordinary apparatus assembly; final effective enchant/attribute state still needs acceptance.

Fresh ArmorPerkHolder internal tier0; two native Apparatus upgrade recipes require exactly the preceding tier and preserve the armor item:

| Recipe / transition | Consumed pedestal ingredients | Source paid at apparatus start |
| --- | --- | ---: |
| first_armor_upgrade: internal0→1 | Two #c:rods/blaze | 2500 |
| second_armor_upgrade: internal1→2 | Two #c:ender_pearls and one Chorus Fruit | 5000 |

No direct0→2, and **book tier, armor upgrade tier and thread slot value are separate quantities**. Native diagrams describe slots; holder sorts them **descending by slot value** before assignment. First installed thread takes highest available slot. Each piece's native layout below is shown in that effective order, not the raw registration list order.

| Piece | Internal tier0 | Internal tier1 | Internal tier2 |
| --- | --- | --- | --- |
| battlemage boots | 1 | 1,1 | 2,1,1 |
| battlemage hood | 1 | 1,1 | 2,1,1 |
| battlemage leggings | 1 | 2,1 | 3,1,1 |
| battlemage robes | 1 | 2,1 | 3,1,1 |
| arcanist hood | 1 | 2,1 | 3,1,1 |
| arcanist boots | 1 | 2,1 | 2,2,1 |
| arcanist leggings | 1 | 3,1 | 3,2,1 |
| arcanist robes | 1 | 3,1 | 3,2,1 |
| sorcerer boots | 1 | 2,1 | 3,2,1 |
| sorcerer robes | 2 | 3,2 | 3,2,2 |
| sorcerer leggings | 2 | 3,2 | 3,2,2 |
| sorcerer hood | 1 | 2,1 | 3,2,1 |

Every equipped AnimatedMagicArmor piece adds30×(internalTier+1) Max Mana and(internalTier+1) Mana Regen attribute value, plus its thread modifiers and material armor. Thus thread-free armor can still benefit casting; final attribute/reserve/gear composition is in mana calculation, not one fixed universal total.

## Alteration Table: place, install, recover

Craft table shaped from Scribes Table center plus four Magebloom Fiber in cross (` x /xyx/ x `). Placement creates HEAD one block forward from placed part and OTHER above the placed part: reserve a two-block-long, two-block-high work area. One crafted table places its parts; no three separate table-item cost. Consult named Notebook page for stand/worktop orientation.

1. Main-hand use **HEAD/armor stand** with compatible armor. Held item must yield StackPerkHolder; empty stand required. Tile extracts installed perks into perkList, clears armor's perk list, copies armor into station, consumes one survival armor. This is actual station inventory, not merely displaying worn armor.
2. Craft Blank Thread: six Magebloom Fiber and three Gold Nuggets, shaped `xxx/yyy/xxx`. Refine a chosen thread via its Apparatus recipe below. Main-hand use **worktop/foot** with thread to add to next effective slot; OTHER forwards interaction to part below. Checks maximum3 and current piece slot count; minimum slot value must fit. Successful add splits one thread (method has no creative exemption).
3. Add highest-priority/high-minimum thread first because slots descend. Gliding and Undying need slot3; most other native threads default minimum1 and scale from actual assigned slot. Empty-hand worktop removes **first** thread, returning to player or loose drop if inventory cannot accept it. It is not selecting any arbitrary socket by clicking an individual model.
4. Empty-hand stand returns armor with queued threads applied; add to player inventory or loose drop if no space. Removing armor clears station stack/list. There is no Source or XP payment in these table methods.40tick newPerkTimer is animation state, not proof of a separate timed sewing debit.
5. Breaking/teardown drops station armor plus separated perk items. Stored armor was stripped of perks; don't count each loose thread as a duplicated still-equipped perk. Station saves armor/list/timer, but actual multipart break/reload/filled-inventory acceptance must be checked.

**Perk-state preservation boundary:** removeArmorStack builds a new perkTags map, adding getInitTag only when old holder lacks that perk's tag; then replaces map. An existing stateful tag can therefore be omitted on table removal, including Undying's isActive. Do not promise existing charge/state survives a remove/reinsert cycle. This is a recovery limitation to test, not a claimed reset exploit. Inspect actual state/ability and delivered items independently.

## Nineteen functional threads

APIRegistry registers19 functional perks plus EmptyPerk(blank_thread). Bonded/Toughness classes also exist but are not in this native registration list; do not invent normal obtainable branches from unused class names. All19 thread recipe resources are listed below. Attribute operations retain native types; duplicate thread effects/gear attributes are not guaranteed additive just because items can be inserted.

| Thread suffix (`ars_nouveau:thread_…`) | Exact ability / slot-value L boundary |
| --- | --- |
| starbuncle | Movement Speed modifier0.2L ADD_MULTIPLIED_TOTAL. |
| high_step | Step Height+L ADD_VALUE. |
| heights | Highest equipped matching slot value increases jump boost power0.1L; fall event subtractsL/0.1 from fall distance. Not measured blocks of jump height. |
| feather | FEATHER attribute+0.2L; native fall damage handling reduces amount by amount×attribute, then final damage clamp0. Other damage/enchantment rules separate. |
| depths | Air depletion mixin retains supply with probability0.33L, always when highest matchingL>=3; slot3 adds Swim Speed+2 ADD_VALUE. |
| magic_capacity | Max Mana modifier0.1L ADD_MULTIPLIED_TOTAL. |
| spellpower | Spell Damage Bonus+2L ADD_VALUE. |
| warding | WARDING+2L; subtracts warding from magic-tagged damage in native hurt handler. Not universal armor bypass immunity. |
| amethyst_golem | Knockback Resistance+0.15L ADD_VALUE. |
| drygmy | DRYGMY attribute+L ADD_VALUE. Two native looting mixins wrap enchanted-bonus chance and enchanted-count functions: max(spell damage source luck, ordinary enchantment level)+integer DRYGMY attribute. This affects those loot functions, not every drop or Henge budget; effective protected eligibility still separate. |
| wixie | WIXIE modifier0.15L ADD_MULTIPLIED_BASE; potion-added handler uses target for beneficial effects, living applier for others to scale duration. Distinct from familiar's separate20% duration hook. |
| whirlisprig | WHIRLIESPRIG modifier0.3/0.6/1.0 forL1/2/3+ ADD_MULTIPLIED_TOTAL. Food-finish handler multiplies current saturation by effective attribute; not simply adding30% to one meal's raw nutrition. Faction/diet compatibility unverified. |
| repairing | Every200ticks per damaged stack whose item calls attemptRepair, repairs min(damage, highest equipped RepairingL+baseArmorRepairRate(default1)), pays20personal mana if available. Base repair works even withL0 when called by magical armor tick. Slot3+ onAdded marks equipped armor UNBREAKING; onRemoved clears marker. Applies across relevant worn items, not one free repair pool. |
| life_drain | Post SpellDamage heals caster damage×0.2×highest equipped Life DrainL per dispatched matching perk instance. Dispatcher iterates equipped instances; duplicates are not safely summarized as one summed-level heal. |
| kindling | Eligible IDamageEffect against another entity applies remainingFireTicks=100L before effect resolve; uses canDamage check. Not independent periodic fire generation. |
| chilling | Eligible damage effect against other living entity adds1frozen tick and Freezing duration200L, amplifier1 forL<3 or2 forL>=3. Literal amplifier values supersede ambiguous prose naming. |
| immolation | On spell-cast event, if on fire extinguishes caster and grants Immolate100ticks/amplifier(highestL-1); if standing in FIRE-tag block removes that block and grants same. Actual fire-school bonuses come from effect handling. |
| gliding | Minimum slot3; equips native gliding capability path, not an item dropped Elytra or unrestricted permanent flight. EffectGlide.canGlide checks active Glide effect or highest Gliding slot>0; actual Caelus/client controls require acceptance. |
| undying | Minimum slot3; init isActive=true. Player death event with active tag sets health1, clears effects, grants Regeneration900/amplifier1, Absorption100/amplifier1 and Fire Resistance800/amplifier0, marks inactive, cancels death. No loose Totem consumed in this handler. SleepFinishedTimeEvent refreshes matching holders for **all players in that level**, not only sleeper. Missing tag isn't repaired by sleep method; table-state boundary above matters. |

PerkUtil.countForPerk returns **maximum matching slot value**, not sum across armor. Equipment-change handler warns about duplicates while evaluating onAdded/onRemoved; Alteration add method itself doesn't prohibit duplicates. Event dispatch and attribute modifier IDs can behave differently from countForPerk, so neither unlimited stacking nor a blanket one-copy hard ban is established. Teach distinct useful threads first.

## Exact thread acquisition

All rows are `data/ars_nouveau/recipe/<basename>.json`, type Enchanting Apparatus, Blank Thread reagent, result one named thread, sourceCost0. Pedestals consumed by apparatus rules; recipe options are optional, not19required quests. More advanced ingredients (Elytra/Totem) remain actual gates.

| Thread recipe | Pedestal ingredients |
| --- | --- |
| `thread_amethyst_golem` | `3×#c:obsidians` |
| `thread_chilling` | `minecraft:blue_ice; 2×ars_nouveau:water_essence; minecraft:powder_snow_bucket` |
| `thread_depths` | `3×#minecraft:fishes; 2×ars_nouveau:water_essence; minecraft:pufferfish` |
| `thread_drygmy` | `3×ars_nouveau:drygmy_shard; 2×ars_nouveau:earth_essence; minecraft:rabbit_foot` |
| `thread_feather` | `4×#c:feathers; 2×ars_nouveau:abjuration_essence` |
| `thread_gliding` | `2×ars_nouveau:air_essence; minecraft:elytra` |
| `thread_heights` | `minecraft:spider_eye; 3×minecraft:rabbit_hide; 2×ars_nouveau:air_essence` |
| `thread_high_step` | `3×ars_nouveau:air_essence` |
| `thread_immolation` | `3×ars_nouveau:fire_essence` |
| `thread_kindling` | `minecraft:magma_cream; ars_nouveau:fire_essence; minecraft:fire_charge` |
| `thread_life_drain` | `ars_nouveau:mendosteen_pod; minecraft:sculk_catalyst; 2×ars_nouveau:abjuration_essence` |
| `thread_magic_capacity` | `3×ars_nouveau:sourceberry_bush; 3×ars_nouveau:magebloom` |
| `thread_repairing` | `minecraft:anvil; 2×ars_nouveau:manipulation_essence` |
| `thread_spellpower` | `ars_nouveau:fire_essence; ars_nouveau:magebloom; ars_nouveau:manipulation_essence; ars_nouveau:water_essence; ars_nouveau:abjuration_essence; ars_nouveau:air_essence; ars_nouveau:earth_essence; ars_nouveau:conjuration_essence` |
| `thread_starbuncle` | `3×ars_nouveau:starbuncle_shards; 3×minecraft:sugar; 2×ars_nouveau:manipulation_essence` |
| `thread_undying` | `minecraft:totem_of_undying; minecraft:phantom_membrane; 2×ars_nouveau:abjuration_essence` |
| `thread_warding` | `8×ars_nouveau:magebloom_fiber` |
| `thread_whirlisprig` | `3×ars_nouveau:whirlisprig_shards; 2×ars_nouveau:earth_essence; minecraft:golden_apple` |
| `thread_wixie` | `3×ars_nouveau:wixie_shards; 2×ars_nouveau:abjuration_essence; 2×#c:crops/nether_wart; minecraft:blaze_rod` |

## Reactive enchantment activation

Obtain initial Reactive through written Spell Parchment apparatus recipe, then sequential rankII/III/IV upgrades as in [crafting companion](ars-nouveau-pinned-crafting.md). ReactiveCasterData stores spell separately from normal caster component. Native ReactiveEvents:

- After living damage on server, rolls each worn armor stack. On player attack, left-click block, or client left-click air followed by server packet, rolls main-hand item. Those are **event attempts**, not necessarily confirmed damage dealt or block broken.
- Chance test is level×0.25>=Math.random(): nominal25/50/75/100% for ranks1..4 per checked stack/event; multiple armor pieces can each attempt. Requires nonnull Reactive component and nonempty spell. No explicit cooldown in these event methods.
- Calls ReactiveCasterData.castSpell with entity and MAIN_HAND. Normal player uses standard resolver/personal mana, nonplayer or FakePlayer uses EntitySpellResolver; generic chance doesn't bypass spell validity/mana or prove its effect succeeded. No new Source charge in ReactiveEvents itself. Armor trigger still uses MAIN_HAND context in this call; actual focus/tool interactions need testing.

Do not teach incoming damage as a free-mana trigger, every click as a cast, or worn Reactive as ordinary hand-casting ownership. Check successful effect and actual resource debit.

## Quest and acceptance handoff

Teach one chosen armor family → actual initial slot → Blank/chosen Thread → table stand/worktop distinction → returned equipped perk → tier1/tier2 upgrades when desired → higher-value slot choice. Separate Reactive side branch. Use item tasks as acquisition proxies; real table operation/effect/death prevention require confirmed native criterion or explicit manual observation. Reward XP and safe fiber/build supplies, not Elytra/Totem/advanced-charge shortcuts.

Positive/boundary: each family's native diagram,0→1→2 and rejected0→2, highest-slot-first insertion, minimum3thread on1 rejected, full/max3table and wrong item, empty-hand remove-first vs armor-return, existing stateful tag retention, multipart break/reload/full inventory, duplicate threads vs countForPerk, per-item20mana repair and base-only repair, dying once/spent tag/global sleep refresh, potion/food vs faction restrictions, Reactive rank chances/multiple armor/insufficient mana/invalid spell/attack vs confirmed effect. No gameplay acceptance asserted. Parent remains researching for other equipment/devices/Amethyst worker/addons/pilot.
