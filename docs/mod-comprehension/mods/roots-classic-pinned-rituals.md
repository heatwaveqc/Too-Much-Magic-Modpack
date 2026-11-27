# Roots Classic — pinned ritual inventory and lifecycle

Companion to [main profile](roots-classic.md). Exact `RootsClassic-1.21.1-1.5.10.jar`, SHA1 `7d02ec46e4d276ebf556cdd529a75108b4724efd`, matched to [pin](../../../mods/roots-classic.pw.toml). Inspected2026-11-16; no gameplay tests. Actual native inventory:46 ritual recipes,22 component recipes,12 registered ritual-effect families. This corrects earlier49/23 counts. All rows below are current singular `data/rootsclassic/recipe/` resources.

## Build and run a ritual

Obtain/open Runic Tablet (main profile); use **Rituals → Basic Ritual Mechanics**, named ritual page and JEI ritual category for the row's ingredients/incense. Put item inputs individually onto Altar. Put each required incense into a separate Brazier, ignite it and keep all participating braziers at altar Y within X/Z offsets−4..4. Extra burning incense can invalidate ordinary matching; make a dedicated ritual area. With empty main hand, sneak-right-click Altar. Observe started feedback and200-tick progress, nominal10seconds at20TPS. Ordinary empty-hand non-sneaking interaction removes an input only while progress0.

- Level0: no standing stones required.
- Level1: eight Mundane Standing Stones at X/Z offsets (±3,±3), (±3,0), (0,±3), same Y as Altar.
- Level2: keep level1 eight, plus Attuned Standing Stones at (±5,+1,0) and (0,+1,±5), relative XYZ. Upper stones are one block above altar Y, not on the same plane. Exact block equality is checked.

The altar first selects a recipe by ingredient match, then verifies its structure/incense. It caches incense stacks, sets progress200 and empties **all nearby Braziers returned by the layout scan**, setting them burning, not just a minimal used subset. Do not leave valuable unrelated unlit contents in that area. It retains altar inventory during progress; at completion invokes effect, then empties every altar slot regardless of effect success. Output spawn rejection does not automatically refund.

**Save/reload boundary:** Altar NBT includes inventory, incense and progress, plus ritual level/colors for client display. The inspected load path does not restore a server recipe ID/currentRitual. Server ticking requires progress>0 AND currentRitual non-null, while starting/removing items requires progress0. A reloaded in-progress altar can therefore be stuck until another recovery route. Breaking drops inventory in the removal path, but incense already cleared is not refunded there. Positive completion/reload/recovery tests remain unrun; no runtime fix is included.

## All46 native recipes

Each item/ingredient occurrence is one required unit in the recipe; duplicates are counted below. Crafting outputs are ItemStacks spawned above altar; summon output is an entity, **not the JEI spawn-egg display**. `effectConfig` is read by corresponding registered implementation.

| Recipe suffix (`rootsclassic:`) | Level | Altar items | Burning incense | Effect / actual result |
| --- | ---: | --- | --- | --- |
| `ritual/banish_rain` | 1 | `minecraft:wheat` | `minecraft:vine`; `rootsclassic:old_root` | banish_rain |
| `ritual/cause_rain` | 1 | `minecraft:lily_pad` | `minecraft:vine` | cause_rain |
| `ritual/crafting/accelerator_standing_stone` | 2 | `rootsclassic:attuned_standing_stone`; `minecraft:glowstone_dust`; `minecraft:stone_bricks` | `rootsclassic:verdant_sprig`; `rootsclassic:infernal_bulb`; `minecraft:wheat`; `rootsclassic:jungle_bark` | crafting → 1× `rootsclassic:accelerator_standing_stone` |
| `ritual/crafting/aesthetic_standing_stone` | 2 | `rootsclassic:attuned_standing_stone`; `minecraft:glowstone_dust`; `minecraft:mossy_stone_bricks` | none | crafting → 1× `rootsclassic:aesthetic_standing_stone` |
| `ritual/crafting/charged_runic_focus` | 0 | `rootsclassic:runic_focus`; `minecraft:redstone`; `minecraft:glowstone_dust` | `rootsclassic:infernal_bulb`; `minecraft:wheat` | crafting → 1× `rootsclassic:charged_runic_focus` |
| `ritual/crafting/crystal_staff` | 2 | `minecraft:diamond_block`; `minecraft:stick`; `minecraft:blaze_powder` | `minecraft:coal_block`; `rootsclassic:acacia_bark`; `rootsclassic:verdant_sprig`; `rootsclassic:birch_bark` | crafting → 1× `rootsclassic:crystal_staff` |
| `ritual/crafting/engraved_crafting` | 2 | `minecraft:stone_sword`; `rootsclassic:runic_focus`; `minecraft:glowstone_dust` | `minecraft:golden_carrot`; `minecraft:golden_apple` | engraved_crafting → 1× `rootsclassic:engraved_blade` |
| `ritual/crafting/entangler_standing_stone` | 2 | `rootsclassic:attuned_standing_stone`; `minecraft:glowstone_dust`; `minecraft:cracked_stone_bricks` | `rootsclassic:verdant_sprig`; `rootsclassic:infernal_bulb`; `minecraft:wheat`; `rootsclassic:oak_bark` | crafting → 1× `rootsclassic:entangler_standing_stone` |
| `ritual/crafting/grower_standing_stone` | 2 | `rootsclassic:attuned_standing_stone`; `minecraft:glowstone_dust`; `minecraft:stone` | `rootsclassic:verdant_sprig`; `rootsclassic:infernal_bulb`; `minecraft:wheat`; `rootsclassic:birch_bark` | crafting → 1× `rootsclassic:grower_standing_stone` |
| `ritual/crafting/healer_standing_stone` | 2 | `rootsclassic:attuned_standing_stone`; `minecraft:redstone`; `minecraft:chiseled_stone_bricks` | `rootsclassic:verdant_sprig`; `rootsclassic:infernal_bulb`; `minecraft:wheat`; `minecraft:ghast_tear` | crafting → 1× `rootsclassic:healer_standing_stone` |
| `ritual/crafting/igniter_standing_stone` | 2 | `rootsclassic:attuned_standing_stone`; `minecraft:redstone`; `minecraft:cracked_stone_bricks` | `rootsclassic:verdant_sprig`; `rootsclassic:infernal_bulb`; `minecraft:wheat`; `rootsclassic:acacia_bark` | crafting → 1× `rootsclassic:igniter_standing_stone` |
| `ritual/crafting/living_axe` | 0 | `minecraft:wooden_axe`; `rootsclassic:verdant_sprig`; `minecraft:gold_nugget` | 2× `rootsclassic:oak_bark` | crafting → 1× `rootsclassic:living_axe` |
| `ritual/crafting/living_hoe` | 0 | `minecraft:wooden_hoe`; `rootsclassic:verdant_sprig`; `minecraft:gold_nugget` | 2× `rootsclassic:oak_bark` | crafting → 1× `rootsclassic:living_hoe` |
| `ritual/crafting/living_pickaxe` | 0 | `minecraft:wooden_pickaxe`; `rootsclassic:verdant_sprig`; `minecraft:gold_nugget` | 2× `rootsclassic:oak_bark` | crafting → 1× `rootsclassic:living_pickaxe` |
| `ritual/crafting/living_shovel` | 0 | `minecraft:wooden_shovel`; `rootsclassic:verdant_sprig`; `minecraft:gold_nugget` | 2× `rootsclassic:oak_bark` | crafting → 1× `rootsclassic:living_shovel` |
| `ritual/crafting/living_sword` | 0 | `minecraft:wooden_sword`; `rootsclassic:verdant_sprig`; `minecraft:gold_nugget` | 2× `rootsclassic:oak_bark` | crafting → 1× `rootsclassic:living_sword` |
| `ritual/crafting/repulsor_standing_stone` | 2 | `rootsclassic:attuned_standing_stone`; `minecraft:glowstone_dust`; `minecraft:chiseled_stone_bricks` | `rootsclassic:verdant_sprig`; `rootsclassic:infernal_bulb`; `minecraft:wheat`; `rootsclassic:spruce_bark` | crafting → 1× `rootsclassic:repulsor_standing_stone` |
| `ritual/crafting/runic_focus` | 2 | `rootsclassic:verdant_sprig`; `minecraft:diamond`; `minecraft:stone` | `rootsclassic:oak_bark`; `rootsclassic:spruce_bark`; `rootsclassic:acacia_bark`; `rootsclassic:jungle_bark` | crafting → 1× `rootsclassic:runic_focus` |
| `ritual/crafting/sylvan_boots` | 2 | `minecraft:diamond`; `minecraft:leather_boots`; `minecraft:vine` | 2× `rootsclassic:birch_bark`; `minecraft:golden_apple`; `minecraft:poppy` | crafting → 1× `rootsclassic:sylvan_boots` |
| `ritual/crafting/sylvan_hood` | 2 | `minecraft:diamond`; `minecraft:leather_helmet`; `minecraft:vine` | 2× `rootsclassic:birch_bark`; `minecraft:golden_apple`; `minecraft:poppy` | crafting → 1× `rootsclassic:sylvan_hood` |
| `ritual/crafting/sylvan_robe` | 2 | `minecraft:diamond`; `minecraft:leather_chestplate`; `minecraft:vine` | 2× `rootsclassic:birch_bark`; `minecraft:golden_apple`; `minecraft:poppy` | crafting → 1× `rootsclassic:sylvan_robe` |
| `ritual/crafting/sylvan_tunic` | 2 | `minecraft:diamond`; `minecraft:leather_leggings`; `minecraft:vine` | 2× `rootsclassic:birch_bark`; `minecraft:golden_apple`; `minecraft:poppy` | crafting → 1× `rootsclassic:sylvan_tunic` |
| `ritual/crafting/vacuum_standing_stone` | 2 | `rootsclassic:attuned_standing_stone`; `minecraft:glowstone_dust`; `minecraft:nether_bricks` | `rootsclassic:verdant_sprig`; `rootsclassic:infernal_bulb`; `minecraft:wheat`; `rootsclassic:dark_oak_bark` | crafting → 1× `rootsclassic:vacuum_standing_stone` |
| `ritual/crafting/wildwood_boots` | 2 | `minecraft:diamond`; `minecraft:iron_boots`; `minecraft:oak_planks` | 2× `rootsclassic:oak_bark`; `minecraft:wheat`; `minecraft:oak_sapling` | crafting → 1× `rootsclassic:wildwood_boots` |
| `ritual/crafting/wildwood_leggings` | 2 | `minecraft:diamond`; `minecraft:iron_leggings`; `minecraft:oak_planks` | 2× `rootsclassic:oak_bark`; `minecraft:wheat`; `minecraft:oak_sapling` | crafting → 1× `rootsclassic:wildwood_leggings` |
| `ritual/crafting/wildwood_mask` | 2 | `minecraft:diamond`; `minecraft:iron_helmet`; `minecraft:oak_planks` | 2× `rootsclassic:oak_bark`; `minecraft:wheat`; `minecraft:oak_sapling` | crafting → 1× `rootsclassic:wildwood_mask` |
| `ritual/crafting/wildwood_plate` | 2 | `minecraft:diamond`; `minecraft:iron_chestplate`; `minecraft:oak_planks` | 2× `rootsclassic:oak_bark`; `minecraft:wheat`; `minecraft:oak_sapling` | crafting → 1× `rootsclassic:wildwood_plate` |
| `ritual/flare` | 1 | `minecraft:flint`; `minecraft:coal`; `minecraft:charcoal` | `minecraft:gunpowder`; `rootsclassic:infernal_bulb` | flare |
| `ritual/grow` | 0 | `minecraft:redstone`; `minecraft:bone_meal`; `rootsclassic:verdant_sprig` | `minecraft:wheat`; `minecraft:beetroot`; `minecraft:potato`; `minecraft:carrot` | grow |
| `ritual/imbuer` | 1 | `rootsclassic:crystal_staff`; `rootsclassic:verdant_sprig` | none | imbuer |
| `ritual/life_drain` | 0 | `minecraft:blaze_powder`; `minecraft:bone` | `minecraft:wooden_sword`; `minecraft:wooden_axe`; `rootsclassic:dark_oak_bark`; `rootsclassic:birch_bark` | life_drain |
| `ritual/mass_breeding` | 2 | `minecraft:glowstone_dust`; `minecraft:bone` | `minecraft:carrot`; `minecraft:wheat`; `minecraft:wheat_seeds`; `minecraft:cod` | mass_breeding |
| `ritual/sacrifice` | 2 | `minecraft:flint`; `minecraft:iron_sword`; `minecraft:bone` | `minecraft:blaze_powder`; `rootsclassic:dark_oak_bark` | sacrifice |
| `ritual/summoning/cave_spider` | 2 | `minecraft:string`; `minecraft:fermented_spider_eye`; `minecraft:bone` | `minecraft:coal`; `minecraft:nether_wart` | summoning → one `minecraft:cave_spider` entity |
| `ritual/summoning/chicken` | 1 | `minecraft:chicken`; `minecraft:feather`; `minecraft:bone` | `minecraft:wheat_seeds`; `minecraft:nether_wart` | summoning → one `minecraft:chicken` entity |
| `ritual/summoning/cow` | 1 | `minecraft:beef`; `minecraft:leather`; `minecraft:bone` | `minecraft:wheat_seeds`; `minecraft:nether_wart` | summoning → one `minecraft:cow` entity |
| `ritual/summoning/creeper` | 2 | 2× `minecraft:gunpowder`; `minecraft:bone` | `minecraft:coal`; `minecraft:nether_wart` | summoning → one `minecraft:creeper` entity |
| `ritual/summoning/enderman` | 2 | 2× `minecraft:ender_pearl`; `minecraft:bone` | `minecraft:coal`; `minecraft:nether_wart` | summoning → one `minecraft:enderman` entity |
| `ritual/summoning/pig` | 1 | 2× `minecraft:porkchop`; `minecraft:bone` | `minecraft:wheat_seeds`; `minecraft:nether_wart` | summoning → one `minecraft:pig` entity |
| `ritual/summoning/rabbit` | 1 | `minecraft:rabbit`; `minecraft:rabbit_hide`; `minecraft:bone` | `minecraft:wheat_seeds`; `minecraft:nether_wart` | summoning → one `minecraft:rabbit` entity |
| `ritual/summoning/sheep` | 1 | `minecraft:mutton`; `#minecraft:wool`; `minecraft:bone` | `minecraft:wheat_seeds`; `minecraft:nether_wart` | summoning → one `minecraft:sheep` entity |
| `ritual/summoning/skeleton` | 2 | `minecraft:arrow`; 2× `minecraft:bone` | `minecraft:coal`; `minecraft:nether_wart` | summoning → one `minecraft:skeleton` entity |
| `ritual/summoning/slime` | 2 | 3× `minecraft:slime_ball` | `minecraft:coal`; `minecraft:nether_wart` | summoning → one `minecraft:slime` entity |
| `ritual/summoning/spider` | 2 | `minecraft:string`; `minecraft:spider_eye`; `minecraft:bone` | `minecraft:coal`; `minecraft:nether_wart` | summoning → one `minecraft:spider` entity |
| `ritual/summoning/zombie` | 2 | 2× `minecraft:rotten_flesh`; `minecraft:bone` | `minecraft:coal`; `minecraft:nether_wart` | summoning → one `minecraft:zombie` entity |
| `ritual/time_shift` | 1 | `minecraft:clock`; `minecraft:iron_ingot` | `minecraft:clock` | time_shift |

## Implementation families and consequential boundaries

| Registered family | Exact behavior and limits |
| --- | --- |
| crafting (24 recipes) | Parses configured ItemStack, creates one ItemEntity at altar+(0.5,1.5,0.5), ignores add boolean, clears input. Result count is row/config count. No tool NBT transfer implied. |
| summoning (12) | Creates configured entity, finalizes Mob with MOB_SUMMONED reason/current difficulty, positions above altar and clears input before unchecked add. No caster/tame-owner assignment in this method; hostile mob is not a servant. Invalid creation still faces altar's final cleanup. |
| engraved_crafting (1) | Crafts Engraved Blade with up to4 extra bark incense modifiers. Acacia→Spikes, Spruce→Forceful, Birch→Holy, Jungle→Aquatic, Dark Oak→Shadowstep; repeated barks increase levels. More than4 barks fails matcher; other recipe incense must still match after barks removed. |
| imbuer (1) | Recreates Crystal Staff and loads up to first4 Spell Powder incense entries into numbered spell slots, retaining powder's potency/efficiency/size data. At least one powder is required; recipe's non-powder incense list still matches. Existing staff's other state is not copied. |
| grow (1) | Server scans X/Z−17..17, Y−4..4; each BonemealableBlock independently gets1/12 chance to call performBonemeal. It does not use an item Bone Meal debit per target; altar ingredients are the cost. Eligibility is broad BonemealableBlock, not crop-only. Parent altar clears inputs. |
| life_drain (1) | Monster-class targets in box−22..+23 X/Z,−8..+9 Y get requested9 cactus damage; accumulates9 each regardless of hurt boolean. Every Player in same box heals total/(players×2.5). No owner/faction blacklist in this effect. Damage cancellation does not necessarily remove healing budget. |
| mass_breeding (1) | Animal-class targets in that same broad box call setInLove with nearest player within5 and write persistent InLove400. Does not verify species food or consume food per animal here. Actual breeding cooldown/age/mod effects require tests. |
| sacrifice (1) | Queries all LivingEntities in box−2..+3. For every non-Player entry, subtracts60 directly from **the first queried entity's health**, not the loop entry, then attempts one random plant/seed output. Thus first entry can be a Player despite loop skip. No requirement that each loop target actually dies before output. No owner/protection check is present in this direct-health method. Unrun damage/duplication boundary; do not teach as a safe animal-only ritual. |
| flare (1) | All LivingEntities in broad box ignite for random14..18 seconds; no player/pet exclusion. Inventory is cleared. The attempted Flint-and-Steel return branch has comparison damage≥damage−1, so it never reaches its return for normal integer damage. Native row uses Flint, not Flint-and-Steel. |
| cause_rain (1) | Clears inventory and sets current level raining true. No weather duration/thunder guarantee in this method. |
| banish_rain (1) | Clears inventory and sets current level raining false. No guarantee of permanent clear weather. |
| time_shift (1) | Requires Clock incense; accepts additional Clocks while normalizing matcher to one. Adds1000 time per Clock incense to **every server level's dayTime**. Clears inventory; not a per-player cosmetic clock. |

These results are binary-derived, not measured rates. In particular life-drain rewards depend on requested damage rather than observed loss, and sacrifice indexing can affect unintended entities. Native summoned mobs and plant drops can affect pack economies; no rare-drop/claim/faction acceptance is established.

## Terra and staff closure

Exact `ComponentSpellsEvent.TICKS_PER_MANA_REGEN=5` and player tick handler use that field; declared common `ticksPerManaRegen=15` is not read by this native method. Default player attachment40 maximum; native arithmetic is one Terra per5 game ticks before clamp, nominal4/sec at20TPS. `StaffItem` active getters read `staffUsesBasic=15` and `staffUsesEfficiency=15`, not legacy65/32 fields. Release guard is `timeLeft<71988` for duration72000, corresponding to at least13 elapsed ticks, **not a required full second**. It subtracts one staff use before Terra affordability; actual effect costs component mana/(efficiency+1). Inventory tick deletes a staff with uses≤0. Effective config/event modifications remain untested.

## Quest and boundary handoff

- Teach Tablet → renewable roots/bark/sprigs → Mortar/Pestle powder → Imbuer/ordinary staff and Terra → Altar/Brazier level0 → eight-stone level1 → raised four-stone level2. Each named ritual above is an optional branch, not46 mandatory linear tiers.
- Choose an expendable level0 Living Tool recipe for first operation; output item is a possession proxy, not proof altar was operated. XP plus ordinary consumables is a safe reward direction.
- Teach powered stones, Crystal Staff imbue and Engraved Blade as separate advanced branches using their named Tablet pages. Full powered-stone/item/component implementation coverage is tracked separately; recipe possession does not prove correct activation.
- Test valid vs missing stone/extra incense,200-tick completion vs reload at100, inventory recovery and consumed incense; test spawn rejection/output storage separately. No automatic client/world run is authorized here.
- Sacrifice: isolated non-player first vs player first, several candidate entities; record exact health and outputs. Life Drain: damage-susceptible vs resistant/canceled Monster; observe heal budget. Flare: player/pet presence. Summons: verify owner/AI/drop behavior against pack factions.

Evidence: exact `ritual/RitualRegistry`, `ritual/RitualPillars`, all12 `ritual/rituals/*` implementations, `block/altar/AltarBlockEntity`, recipe JSON, config/event/StaffItem/ManaAttachment classes. Selected CFR interpretation; consequential bytecode can be reproduced with javap; no third-party classes/assets committed. Remaining item/component/aura branches keep main status researching, distinct from client acceptance.
