# Voidscape 1.9.588 — Mod Capability Atlas

**Status: native quest-source survey complete, 2026-11-25.** Tracker [#410](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410). Supersedes the completion scope of [preliminary source evaluation](voidscape-evaluation.md), preserving its findings and rating unless corrected below. No runtime, balance, recipe or quest changes.

## 1. Evidence and onboarding

Drive-provided Voidscape-1.21.1-1.9.588.jar 2,219,583 bytes; sha512sum 704b7358c3b961a54b7ca3ac566d976faf026464188aaab979658a208d0c4210a47dd78816dad1558702e0cd9cf4821a71c96896bd673ba0d87e49d252db6759 matches [active Packwiz pin](../../../mods/voidscape.pw.toml), CF251730/8175491. zipfile.testzip clean; 1753 entries,418 classes;112 recipes (83 smithing_transform,18 shaped,5 shapeless,3 smelting,3 blasting),25 native nonrecipe advancements,42 loot tables,86 worldgen JSON records,12 namespace tag records, one damage type, dimension and dimension type. Important namespace: all 112 native recipes are data/minecraft/recipe/ rather than data/voidscape/recipe/. Metadata javafml>=1, NeoForge>=21, version1.21.1-1.9.588. Prior exact immutable [source 683cd570](https://github.com/Tamaized/Voidscape/tree/683cd570f051a58d1091737490194e4c47ca82b9) and preliminary survey supplement binary inspection of Insanity, ModAttributes, ModToolTiers, ModArmorMaterials, all five armor sets, four entities, portal activator/predicates, ConfigurablePortalShape, StrangePearl, Hatchery, VeryDrippyDripstone, ore generator, death handler. No dependency or loaded world assumed. Native advancement tree and JEI supply instruction; no dedicated guidebook dependency required. All item IDs prefix voidscape: unless vanilla noted. Recipe IDs prefix minecraft: despite their Voidscape outputs. Do not use older wiki recipes over exact JSON.

## 2. Dimension entry, environment and survival

Native dimension voidscape:void uses layered biome source: lower threshold Y32, antispires pockets; mid-layer null, overworld, nether, end and optional aether pockets; upper threshold Y160, thunderspires and thunder_forest; major biome void. Conditional Aether datapack exists. Eight packaged base biomes (antispires/end/nether/null/overworld/thunderspires/thunder_forest/void), distinct optional ninth aether. Native monster lists: Null Servant weight20 in Null; Voidling weight5 in Thunder Forest; Enderman20 in End; Wither Skeleton5, zombified piglin20, zoglin10, ghast25, blaze5 in Nether; spider5,zombie20,skeleton10,creeper5 in Overworld. Biome names do not mean actually entering vanilla dimensions. Void's Wrath and private Pawn encounter use native non-biome spawn routes; don't assume ordinary spawn-egg reproduction.

Initial entry: stand on bedrock in an eligible dimension, native canTeleport predicates and configurable dimension allow/deny; shouldTeleport checks every20 ticks and 1/8 RNG then advances progress in 20-tick steps, can decay while not eligible; reaches200 and transfers. Not fixed ten seconds; multiple rolls. Bring netherite pickaxe, food and retreat plan. Portal separate 200-tick charge. Frame predicate explicitly accepts Voidic Crystal Block or fragile variant; use Charred Bone on valid frame, native ConfigurablePortalShape tests rectangular portal geometry (width2–21, height3–21; avoid overclaiming untested specific orientation). Native portal route useful after first expedition. Death ejection is not required to exit and not a universal home command.

Compiled Insanity.tick: infusion and paranoia each clamp0–600. On non-spectator in Void and no decrement effect, infusion adds (1−voidic_infusion_res)/20 per tick, paranoia adds .9*(1−voidic_paranoia_res)/20 per tick. No armor: infusion600 in600s, paranoia600 in666.67s nominal at20TPS. Outside Void paranoia immediately0 and infusion decreases1/tick (max30s to clear). Defuser decrement takes priority over natural increase for that tick. Every20 ticks calculateEffects removes old infusion modifiers, f=infusion/600; maxHP total multiplier max(1−f,1/unmodifiedMaxHP); floor1 HP, current health clamped down, not healed on removal. Adds10f to voidic_dmg and voidic_res. Thus full infusion is both lethal vulnerability and native power increase. Percentage attributes visibility, infusion, infusion_res, paranoia_res clamp0–1; damage, arrow damage, resistance clamp0–2048 engine bounds, NOT attainable gear stats. Full Corrupt+ armor can halt ordinary paranoia accumulation; use partial protection or controlled native fruit if seeking Pawn, not compulsory permanent exposure. Max paranoia spawns target-bound Corrupted Pawn if no existing hunt; compiled custom death drops 1–4 Tendrils at target, resets target paranoia to0. Native paranoia visual and sound effects precede encounter; no generic summon item. Actual special entry/exit eligibility and cross-mod death ordering separate integration questions.

Renewable Voidic Crystal: native generator non-spectator in Void every30 ticks 20% chance selects random offset −32..31 in each axis; actual bedrock replacement, not a guaranteed ore every cycle. Loot1–3 crystals, transform-on-break returns bedrock. First gather crystals, template and gear. Ethereal Essence: qualified player kill of non-player non-IEthereal with target infusion>=200 in Void yields essence 2/3 chance (source and compiled death handler); use essence on bedrock in Void to convert ore, repeat extraction. Ethereal Spider Eggs remove bedrock or thunder nylium in Void only; not vanilla bedrock tool permission or universal building/claim bypass. Different blocks and drops: Titanite Ore→Titanite Chunk (or smelt/blast ore), Flesh Ore→Flesh Chunk, Strange Ore→Strange Pearl, Cracked Astralrock→Astral Essence. Silk Touch branch on Titanite/Flesh/Strange. Packaged tags require Voidic tool for Titanite Ore, Titanite tool for Flesh Ore, Ichor tool for Strange Ore/Cracked Astralrock. No actual block requiring Astral tier in native needs_astral_tool (empty); no dedicated Corrupt-only block in needs_corrupt_tool. These distinctions matter for semi-dynamic Monk harvest capabilities, not a new Monk balance instruction.

## 3. Actual gear progression and acquisition

Craft Voidic Template: eight Voidic Crystals around diamond; duplicate: eight crystals around template→two templates. Smith Netherite base item with template and one Voidic Crystal. Sword/axe/armor follow Voidic→Corrupt (Tendril)→Titanite (Titanite Shard)→Ichor (Ichor Crystal)→Astral (Astral Crystal) with one template and appropriate addition per step. Bow/crossbow base is corresponding vanilla equipment; check JEI. Crucial separate pickaxe route **Voidic→Titanite→Ichor→Astral**, no Corrupt pickaxe recipe; Titanite Hoe directly from Netherite Hoe, Astral Shovel directly from Netherite Shovel. Charred Warhammer head five Charred Bones+one Voidic Crystal, then head/netherite ingot/crystal vertically; 3x3 block break advancement hammer. Charred Quiver (4 leather,3 crystal,1 charred bone,1 tendril? exact shape LCL/CBC/LCT: 3 leather,4 crystals,1 bone,1 tendril) aids ranged use; exact ammo/activation details reference precision, don't claim unlimited arrows. Voidic Shield separate craft. 83 smithing transformations include fang upgrades on sword/axe/bow/crossbow at each of five tiers (20 recipes, voidscape:fang=true), elytra chestplate variants at Voidic/Titanite/Ichor/Astral, four Astral draconic variants (dragon egg addition, same item ID with voidscape:draconic=true); component-bearing upgrades aren't separate registry items. Preserve component through upgrading per native recipe behavior; don't guarantee all cross-mod enchantment/components survive every smithing override. Fang augment supplies native infusion application; no invented universal poison/immunity.

| Material | Tool uses | Mining speed | Tier attack bonus | Enchantability | Repair |
|---|---:|---:|---:|---:|---|
| Voidic Crystal |2538|9.5|5|17|Voidic Crystal|
| Charred |2538|9.5|5|17|Charred Bone|
| Corrupt |3041|10|6|19|Tendril|
| Titanite |3544|10.5|7|21|Titanite Shard|
| Ichor |4047|11|8|23|Ichor Crystal|
| Astral |4550|11.5|9|25|Astral Crystal|

Material attack bonus is not item total damage. Custom voidic weapon damage separate from physical; actual hit handler and target resistances matter, don't add all attribute engine maxima as DPS. Armor constructors and per-piece add-value attributes:
| Full set | Armor helmet/chest/legs/boots | Total armor | Total toughness | Knockback res | Infusion res | Paranoia res | Voidic res | Helmet visibility |
|---|---|---:|---:|---:|---:|---:|---:|---:|
| Voidic |3/8/6/3|20|16|.4|.20|0|4|0|
| Corrupt |4/9/7/4|24|20|.6|.40|1|8|.15|
| Titanite |5/10/8/5|28|24|.8|.60|1|12|.20|
| Ichor |6/11/9/6|32|28|1.0|.68|1|16|.25|
| Astral |7/12/10/7|36|32|1.2 raw|.80|1|20|.30|

Knockback total raw additive; vanilla effective attribute clamp not exceeded simply because raw sum>1. Armor raw points are not guaranteed percent reduction. Astral +5 maxHP each draconic upgrade, +20 total four actual eggs, no passive HP grant absent armor. These are gear-specific abilities for owner #721 scope only if otherwise inaccessible to Monk; do not raise baseline universally. No free armorless access. Ichor+ etc 100% paranoia resistance under ordinary native accumulation, not immunity to external direct paranoia additions. At full Astral default infusion .2/sec, 50min nominal to600.

## 4. Encounters and material gates

- Void's Wrath base40HP, armor20, physical attack3 and voidic damage3; charged explosion AI, Charred Bone table one roll0–1 +Looting uniform0–1 increment. Use bone to unlock portal and machine/warhammer crafts. No guaranteed bone each kill.
- Voidling base spider attributes plus speed.4, armor8, voidic damage3; Thunder Forest spawn. Loot one weighted pool eggs75 vs fang10; eggs1–3 +looting0–2 increment; fang0–1. Thus eggs/rare fang different outcomes, not both guaranteed. Ethereal Spider Eggs are consumables to remove specified native blocks, not companion hatchers.
- Corrupted Pawn base20HP, target-bound at paranoia600; native psychosis advancement kill in Void; loot table empty because custom death handler creates1–4 Tendrils and resets paranoia. This supplies Corrupt gear and machine core. No stationary world boss with ordinary fixed spawn location.
- Null Servant base50HP/armor10/physical attack3/voidic damage2, Null biome ordinary spawn. Right-click unaugmented eligible Servant with Titanite Chunk, Ichor or Astral Essence (distinct native item-used-on-null-servant trigger) to initiate respective challenge augment, not tame. Compiled setupAugmentStats adds +50/+150/+400 HP, +1/+4/+8 voidic damage, +1/+2/+4 voidic resistance for augment1/2/3. Base + augment nominal100/200/450HP before other gear/effects; weapon replacement/phase mechanics additional. Custom death loot is one shard/crystal corresponding to augment: Titanite Shard, Ichor Crystal, Astral Crystal. Empty ordinary loot table not evidence of no drops. Native phases implemented by TitaniteAugmentGoal/IchorAugmentGoal/AstralAugmentGoal; use encounter prompts and combat gear, don't fabricate exact attack timings. No independent permanent companion capability. 
- Ichor acquisition: smith Flesh Chunk + Ethereal Nether Fruit with Nether Star as smithing template (consumed by normal smithing), yields Ichor for Servant augment, not Ichor Crystal yet. Mine Titanite with Voidic pickaxe, kill augmented Servant, upgrade pickaxe; mine Flesh, craft Ichor, augment again, upgrade Ichor pickaxe. Strange Pearl from Strange Ore: throw at End Crystal (compiled onHitEntity type check) to spawn Astral Shards; not generic Ender Pearl teleport. Native Astral Shards/Cracked Astralrock route leads to Astral Essence then final Servant augment; cracked rock loot is Essence, not finished crystal. Player teaching should explicitly differentiate shards, essence and crystal. Prior source's generic boss wording was imprecise; no new guessed boss phases or rewards.

## 5. Tomes, food and native environmental utility

Exact four tome recipes: Ichor Tome = vanilla book + Ichor Crystal + Voidic Template (base for all others). Voidic, Titanite, Corrupt Tome respectively smith Ichor Tome with Voidic Crystal, Titanite Shard, Tendril and template; despite earlier material names, all require Ichor stage! 100 durability, use/release after >15 ticks, spend one durability, repair with corresponding material. Prior version-aligned compiled-compatible source:
| Tome | Effect | Cooldown |
|---|---|---:|
| Voidic | Aura30s, optional lingering potion contents |45s|
| Titanite | Fortified30s |45s|
| Corrupt | look-direction motion ×2.5, Slow Falling10s |5s|
| Ichor | Ichor Bolt projectile |10s|

Voidic Aura every20 ticks within inflated2 AABB; optional potion effects (ordinary duration quartered, instant applied, harmful self ignored); otherwise voidic damage. No general mana resource or spell composer. Spell durability and cooldown are typed operating resources. Ichor base/other variants each repair material; no creative flight. Corrupt Tome launch is motion, not long-distance teleport. End Crystal transformation unrelated to Guardian of DE.

Five native ethereal fruit types: Void +150 infusion, Null −150 infusion, Overworld −150 paranoia, Nether +150 paranoia, End Regeneration IV20s. Ethereal Fruit Salad18 nutrition, saturation modifier.5, always edible, random 11 outcomes including harmful and two none; no promise all benefits. Germinator can generate biome fruits. Titanite Hoe native bonemeal action and advancement; not general autonomous crop system. Thunder wood hyphae/plank/slab/stairs and flesh block compression ordinary JEI building materials. Very Drippy Dripstone shapeless pointed dripstone+Voidic Bucket. Compiled ticker every15 ticks, random boolean, if support above not air and target two above Mud outside ultrawarm, 1/4 roll converts Mud to Clay; otherwise fluid above, 1/4 roll invokes vanilla cauldron drip within11 blocks below with appropriate conditions. Nominal accelerated fluid/Clay utility, not free Voidic liquid generation. Astral Shovel/Charred Hammer 3x3 actions, block permission and native correct tool respected; no arbitrary schematic placement. 

## 6. Machines, fluid ledger and terminal systems

Machine Core = eight Tendrils around Redstone. Liquifier = four Tendrils +4 Charred Bones + core; Defuser =4 Voidic Crystals+4 Charred Bones+core. Other machines ordinary JEI, significant gate Hatchery = Dragon Head smithing template, Coop base, Astral Crystal addition. Liquid Voidic Crystal is typed fluid, not FE. Liquifier consumes1 crystal per80 ticks, produces250mB, capacity10,000 and pushes up to1000mB to each eligible adjacent handler every20 ticks bounded by acceptance. Prior exact release-aligned source machine handlers retained; Hatchery terminal state additionally checked compiled. Ordinary machines 10,000mB tanks. No general item network or universal arbitrary fluids. Redstone pause relevant machines, local adjacency and output acceptance matter. Use fluid interface/bucket, not imagined power requirement.
| Machine | Input/use | Result and operating boundary |
|---|---|---|
| Liquifier |1 crystal/80t|250mB, storage/neighbor export|
| Defuser |1mB starts100t|each active tick decreases nearby entities' infusion, inflated32 AABB|
| Infuser |1mB starts40t|adds250 infusion to eligible entities below200 in inflated6 AABB, idle retry80t|
| Collector |1mB/40t attempt|collects dropped items inflated6 AABB, inserts adjacent item handlers and retains unaccepted remainder, idle retry80t|
| Germinator |1mB/60t|fake-player bonemeal inflated4 AABB, may grow biome fruit in Void, idle retry120t|
| Coop |1mB/60–199t|one ordinary egg without chicken, adjacent output, blocked retry200t|
| Well |no finite water reagent|Integer.MAX_VALUE tank refills water, adjacent transfer|
| Hatchery |100,000mB accumulated|replaces itself with vanilla Dragon Egg, consumes machine, not breeding or mob spawner|

Hatchery minimum liquid corresponds to400 crystals (100000/250), one Liquifier nominal32000 ticks=26m40s to produce, excluding transfer/other consumers; can parallelize. This is a repeatable expensive Dragon Egg alternative and each hatch requires new Coop/Astral Crystal/Dragon Head template. Do not infer free eggs, permanent machine or fixed loaded time. Significant TMM gate interaction: Dragon Egg used in DE Chaotic Injector and Astral armor, potentially other gates; flag existing economy/owner review, no unauthorized disable. This reinforces Life4 and does not make Voidscape general factory infrastructure. Native machine advancements triggered by operation (liquifier, defuser, infuser, collector, germinator, coop, hatchery, well) can serve quest evidence after criterion verification, not possession proxies.

## 7. Death protection and TMM comparison

Compiled VoidDimensionDeathHandler registers highest-priority LivingDeathEvent: for eligible ServerPlayer in voidscape:void, restore10% current max health, remove milk-curable effects, reset fall distance, select spawn dimension or Overworld fallback, transfer via SpawnPointTeleporter and cancel when destination present. Actual death interception and inventory retention as consequence of canceled event, not ordinary grave, resurrection outside Void or guaranteed safe landing. If still severely infused current health may be low; allow recovery outside. Interaction with Dracolich/Archlich, Undying, Monk trial/keep inventory, death-based quest credit and spawn availability is cross-mod event ordering, not native survey blocker. Do not make death mandatory quest.

Live source pin and manifest verified adventure category, enabled; index has no Voidscape-specific config/datapack/quest path at queried snapshot, unlike dedicated magic mods. No claim loaded server config defaults or global scripts have no indirect effect. Lost Soul policy #343 includes Voidscape as eligible dimension; only source candidate from PR search, not assumed integrated. Owner's separate Hunter taxonomy, Newbie dimension restrictions, existing dimension gating and dragon egg progression require own workstream. No changes here. 

## 8. 29-axis navigation

| Axis | State | Concrete capability |
|---|---|---|
| gear | core | five armor families/six tool materials, shield, bow/crossbow, warhammer, fang/elytra/draconic upgrades |
| movement | present | Corrupt Tome boost and slow fall, elytra chestplate, portal |
| ore_processing | limited | smelt/blast Titanite/Flesh/Strange, crystal liquification, unique smithing |
| storage | limited | specialized machine tanks, no general item warehouse |
| crafting | core | 83 smithing transforms, tier template, core machines, Ichor/tome gates |
| logistics | limited | Collector and adjacent fluid/water export, no general pipe system |
| buffs | core | infusion offense/resistance, food, Fortified, aura and armor modifiers |
| external_compatibility | present | NeoForge fluid/item capabilities, optional Aether dimension pocket, vanilla smithing, potion/elytra integration |
| mob_farming | limited | repeatable encounter resource drops, no general spawner or grinder |
| ore_generation | present | renewable crystal ore from actual bedrock, Essence conversion, not general ore selection |
| permanent_power | limited | lasting gear and resources, no innate skill tree |
| general_generation | present | renewable crystal, machine eggs/water, clay, fruit; not arbitrary creation |
| direct_damage | core | voidic melee/ranged, fang and tome bolt/aura |
| block_breaking | core | unique harvest tiers, warhammer/shovel 3x3, spider egg native block removal |
| permanent_pets | absent | Null Servant augment is hostile challenge, no tame |
| temporary_pets | absent | no native ally summon established |
| defense | core | high armor/toughness, voidic resistance, paranoia/infusion mitigation |
| healing_recovery | present | End fruit Regeneration IV, Null fruit/Defuser, dimension death handler |
| debuff_control | present | infusion/paranoia management, potion aura and death cleanse |
| energy | core | typed Liquid Voidic Crystal, duration/cooldown/durability, player infestation/mental meters distinct |
| automation_control | present | redstone machine control, adjacent capability transfer and autonomous cycles |
| agriculture | core | Germinator, Well, Coop, Titanite Hoe, fruit, Hatchery alternate egg |
| detection_information | present | visibility attribute, meters, native advancement tree, biome/ore clues |
| equipment_augmentation | core | tier smithing, fang, elytra, draconic HP |
| world_manipulation | present | bedrock-to-crystal, restricted removal, mud-to-clay, 3x3 terrain |
| world_content | core | Void dimension,8 base biomes + conditional Aether, ore/encounter systems |
| death_respawn | limited | actual Void-only death ejection, not global revival |
| trade_economy | absent | no native shops or currency |
| progression_tiers | core | Netherite→Voidic→Corrupt/Titanite→Ichor→Astral, repeatable bosses and machines |

## 9. Proposed instructional quest map

All native advancement paths prefix voidscape:, actual item IDs same namespace, ordinary JEI recipes use minecraft: IDs. A=advancement, O=observable operation, I=inventory/craft proxy; no native advancement for every individual gear piece, gear and voidmancer aggregate criteria. Preserve personal progress; no mandated death, no forced Dragon Egg consumption before other gated paths. No quest implementation or reward amounts.

| ID | Player-facing action/prerequisite | Evidence |
|---|---|---|
| VS01 | Prepare Netherite pickaxe, food, eligible bedrock entry; wait native stochastic transfer | A root |
| VS02 | Explain infusion/paranoia and health trade-off; use UI | O |
| VS03 | Mine renewable crystal from bedrock, collect 1–3 | I |
| VS04 | Craft and duplicate template | I |
| VS05 | Smith Voidic pickaxe/weapon and armor; compare protection | I/O |
| VS06 | Qualifying infused mob kill→Ethereal Essence→convert bedrock | A essence |
| VS07 | Explore Void's Wrath encounter, obtain Charred Bone | I |
| VS08 | Crystal-block frame, activate Charred Bone, demonstrate return | A portal/O |
| VS09 | Craft Charred Warhammer and use 3x3 | A hammer |
| VS10 | Craft Quiver and test supported ammo behavior | I/O |
| VS11 | Thunder Forest Voidling hunt, collect eggs/fang | I |
| VS12 | Use Ethereal Spider Eggs on permitted block | A spidereggs |
| VS13 | Smith fang augment, inspect component | I/O |
| VS14 | Intentionally encounter Pawn via ordinary eligible paranoia, defeat it | A psychosis |
| VS15 | Tendrils→Corrupt equipment, complete set ordinary paranoia mitigation | I/O |
| VS16 | Mine Titanite Ore with Voidic pickaxe, process Chunk | I |
| VS17 | Locate Null Servant, use Titanite Chunk and defeat augmented encounter | A purified + shard |
| VS18 | Upgrade Titanite tools/armor, specifically Voidic→Titanite pickaxe | I |
| VS19 | Smith Titanite Hoe from Netherite, use bonemeal function | A hoe |
| VS20 | Mine Flesh Ore with Titanite, obtain Flesh Chunk | I |
| VS21 | Obtain Nether Ethereal Fruit, use Nether Star smithing template to craft Ichor | I |
| VS22 | Augment Null Servant with Ichor, defeat for Ichor Crystal | A ichor + crystal |
| VS23 | Upgrade Ichor pickaxe/gear | I |
| VS24 | Smith Ichor Tome from book and Crystal; cast bolt | I/O |
| VS25 | Convert separate Ichor Tome to Voidic; aura/potion option | I/O |
| VS26 | Titanite Tome Fortified | I/O |
| VS27 | Corrupt Tome leap and slow fall | I/O; A voidmancer all four |
| VS28 | Mine Strange Ore and throw Strange Pearl at End Crystal, collect Shards | A cracked |
| VS29 | Locate/mine Cracked Astralrock with Ichor tool, collect Essence | A astral |
| VS30 | Augment Null Servant with Astral Essence, defeat for Astral Crystal | A astral_crystal + crystal |
| VS31 | Astral armor/weapons/pickaxe, compare raw stats | I; A gear if all criteria |
| VS32 | Astral Shovel directly from Netherite and 3x3 | A terraform |
| VS33 | Optional elytra chestplate recipe, test flight under TMM policy | I/O |
| VS34 | Optional Astral dragon egg augment (one per piece), respect progression gate | I/O |
| VS35 | Tendrils/redstone→Machine Core, craft Liquifier | I |
| VS36 | Crystal→250mB liquid, route to neighbor | A liquid/O |
| VS37 | Fill native Voidic Bucket, craft Very Drippy Dripstone | A bucket/I |
| VS38 | Demonstrate accelerated mud/clay or cauldron drip | O |
| VS39 | Defuser reduces infusion in nearby area | A defused/O |
| VS40 | Infuser raises eligible target infusion, understand risk | A infuser/infused |
| VS41 | Collector and adjacent inventory, unaccepted remainder | A collector |
| VS42 | Germinator, bonemeal and biome plants | A germinated |
| VS43 | Eat appropriate fruit for intentional meter management, optional salad risk | O |
| VS44 | Coop egg output, redstone control | A coop |
| VS45 | Well generates water, adjacent consumer | A well |
| VS46 | Optional late Hatchery, Coop+Dragon Head+Astral Crystal, supply100000mB, collect Dragon Egg | A hatchery/I |
| VS47 | Explain Void-only death ejection, spawn and safe voluntary return | read/checkmark or O voluntary exit, no fatal trial |

All 25 native progression advancements mapped (root,portal,essence,gear,hammer,spidereggs,psychosis,purified,hoe,ichor,cracked,astral,astral_crystal,terraform,voidmancer,liquid,bucket,defused,infused,infuser,collector,germinated,coop,well,hatchery). Significant native quest-source coverage complete; finer combat phase cadence, exact quiver ammo mechanics, all minor loot/worldgen coefficients, effective cross-mod event ordering and TMM gate policy remain separately identified, no claim exhaustive optimal builds. No runtime or quest implementation. Final rating unchanged36/55,7 independent, Substantial; [final evaluation](voidscape-final-evaluation.md).
