# Enigmatic Legacy+ 1.1.2 — Mod Capability Atlas

Exact pinned Minecraft 1.21.1 NeoForge release, static JAR survey 2026-11-25. Owner-requested full 29-axis profile plus separate 11-domain evaluation. Tracker #410. No gameplay build, runtime registry, code, quest, pin or balance modifications.

## 1. Provenance

[Packwiz](../../../mods/enigmaticlegacy-plus.pw.toml), [Modrinth version](https://modrinth.com/mod/oIjj1Fan/version/ELK5gx91). Drive JAR enigmaticlegacyplus-1.21.1-1.1.2.jar 7,254,195 bytes, SHA-512 9f334bc1c22756d0d1d5e785223217409dcacf05a8ff3998f6d18281f45487df5f5a405d41204908e49591464f690d085724a7d8b00318ad2811448a2057b0f1 matches pin; CRC all 2147 archive entries passed. 546 classes, 127 recipes (65 shaped, 33 cursed shaped, 12 spellstone, 7 shapeless, 4 blasting, 3 smelting, 2 shapeless_no_remain, 1 annihilating), 187 advancements (126 recipe/61 other), 49 loot tables, 42 mod-owned loot modifiers, 9 enchantments, 4 worldgen JSONs, 12 English Patchouli entry JSONs plus 2 categories. Native registry prefix enigmaticlegacyplus; code namespace auviotre.enigmatic.legacy. Requires NeoForge >=21.1.224, Minecraft 1.21.1, Curios >=1.21.1-9.5.0, Patchouli >=1.21-88, Caelus >=1.21.1-7.0.0. Local javap -p -c static disassembly (Java 21), data JSON and localization inspection; not execution of mod code. Tooltips describe intended behavior, not all numeric implementation independently verified. Exact server configs separate.

## 2. Player experience and progression

Ordinary explorer: The Acknowledgment (book + lantern; TMM welcome reward), Iron/Quartz/Golden rings, scrolls, relics from Overworld/Nether/End chests; collect Spellcore and debris, reconstruct stones, choose elemental specialization and weapon resonance. Early Earth Heart/fragments (dungeon loot), Nether Infernal Cinder/Ichor Droplet, End Astral Dust/Starlight Particle and Etherium Ore (End Stone); blast raw Etherium to ingot (200 ticks, 0.8 XP, one output). Craft actual armor, sword, hammer, scythe, Etherium Core and advanced Starlight gear. Cosmic Heart uses four Astral Dust, Nether Star, two Blaze Powder, Heart of Sea and Eye of Ender; Cube recipe consumes six distinct basic spellstones, not all collected stones. This ordinary route does not require Seven Curses.

Optional challenge: equip Ring of the Seven Curses only after consent and explanation of permanent binding/soul loss; obtain cursed special loot; Twisted Heart and The Twist offensive line or Pure Heart and Sacred Crystal/The Bless defensive line. Track cursed-time ratio for Worthy status and Abyssal Hearts, then Abyssal relics. Redemption is a separate branch and changes curse access; do not require every player to switch or die. Advancement parent links describe teaching/achievement relationships, not necessarily hard recipe gates. Native all_spellstone advancement checks nine named basic stones; Cube consumes six distinct entries from ten-item tag, not the advancement itself. No forced all-spellstone grind.

## 3. Complete 29-axis navigation

| Axis | Support | Native capability and operating boundary |
| --- | --- | --- |
| gear | core | Iron/Golden/Quartz rings, scrolls and spellstones → Etherium Hammer/Scythe/Sword, four-piece invisible armor, Spellstone Sword, Infernal Spear/Shield, Execution Axe, Dragon Breath Bow, Majestic/Chaos Elytra, curse/blessing relics. No universal spellbook. |
| movement | core | Amulet movement modifiers, Angel's Blessing dash and fall immunity, Ocean Stone underwater mobility, Eye of Nebula target teleport, Recall/Wormhole Potions, Twisted Mirror, Heaven/Fabulous Scroll beacon/XP flight, Majestic/Chaos Elytra and Cube random travel. |
| ore_processing | present | Miner Ring uses actual blasting recipes on mined or nearby item drops; 256-point budget, replenishes naturally or by fuel. No general multiplication of raw ore identified; raw Etherium blasted to ingot. |
| storage | present, narrow | Ring of Ender remote access to vanilla Ender Chest; Antique Book Bag shared specialized book carrier; Enigmatic Amulet/Storage Crystal death contents. No expanded universal item warehouse. |
| crafting | core | 127 recipe records incl. 12 Spellstone Table, 33 cursed shaped, 1 annihilating, vanilla crafting/blasting/smelting; named material and taint gates. Table not general-purpose factory. |
| logistics | limited | Magnet Ring pickup, remote Ender Chest, specialized Antique Book Bag. No unattended item-routing network, filter pipes, machine transport or autonomous workers. |
| buffs | core | Spellstones with elemental advantages and vulnerabilities, book/scroll/charm buffs, potion branches, Forbidden Fruit hunger removal with healing penalty; active/passive conditions and Curio slots matter. |
| external_compatibility | present, bounded | NeoForge event and Curios integration, Patchouli and Caelus hard deps. MixinApothMenu/Screen, JEI, AppleSkin, Mystical Agriculture and Thirst paths present; presence not evidence of effective loaded integration. TMM death/XP and enchanting ownership separate. |
| mob_farming | limited | Cursed ring +Looting, XP, conditional special drops, offensive aura and weapon abilities. No autonomous spawner or virtual mob-loot generator established. |
| ore_generation | natural only | Etherium naturally replaces End Stone (vein size 3, 3 placements/chunk in feature). No renewable new ore generation mechanism. |
| permanent_power | core | Persistent SevenCursesBearing/RedemptionBearing, cursed vs uncursed play time, AbyssalHeartsGained, soul-loss count, Astral Fruit ring-slot augmentation, Forbidden Fruit lasting hunger state. Not TMM skill points. |
| general_generation | limited | Infinimeal reusable bonemeal, Starlight Bucket fluid utility, bonus loot, rare relic ingredients. No universal transmutation or duplicator. |
| direct_damage | core | Native enchanted weapons, ten spellstone resonance families with active skills, debuff/reflection effects, infernal/abyssal weapons, damage/attack-speed Curios. Exclude OP-only or unproven acquisition endpoints. |
| block_breaking | present | Etherium Hammer area mining, Etherium Scythe area breaking and enhanced harvesting, mining charm/Ring and Etheric Resonance. Netherite tool eligibility, no general quarry. |
| permanent_pets | supplemental | Animal Guidebook, Hunter Guidebook and Sanguinary Handbook modify existing pets/animal interactions. Ichor Sprite follows/heals an infected monster owner, not a demonstrated player-tamed pet; no general taming engine. |
| temporary_pets | limited | Illusion Lantern soul-fireballs, Spellstone Sword projectiles and temporary offensive effects are not persistent servants; no broad summon-army system established. |
| defense | core | Golem Heart armor/armorless bonuses and melee resistance, Etherium gear shield threshold, Etheric Resonance enhancement, elemental spellstone resistances and vulnerabilities, parry/deflect, Cube lethal intercept. |
| healing_recovery | core | Revival Leaf regeneration, potion/food alternatives, Redemption Ring regeneration, life steal, Totem of Malice and Cube lethal prevention. Forbidden Fruit imposes recovery drawback. |
| debuff_control | core | Frozen targets, Withering, Poison, Ichor Corrosion, Abyss Corruption, removal of effects, Illusion Lantern undead interactions, Spellstone Sword resonance effects. No all-target universal crowd control. |
| energy | present, typed | Spellstone cooldowns, Spellstone Sword SPELL_ENERGY / SPELL_LEVEL, Miner Ring MINER_POINT 0–256, XP-paid flight/weather/healing, item components and cursed play time. No shared magical mana/FE network. |
| automation_control | limited | Automatic ring processing, passive crop growth, area scythe harvesting and periodic effects. No general programmable block network. |
| agriculture | present | Infinimeal reusable growth tool (including several nonstandard crops), Revival Leaf accelerates nearby crop growth, Etheric Resonance Scythe enhances harvesting. No complete automated planting/breeding/transport farm. |
| detection_information | present | Explorer Scroll highlights nearby treasure, Mining Charm underground night vision, Soul Compass tracks recovery, Acknowledgment unlockable material pages and lore. Not an ore generator. |
| equipment_augmentation | core | Spellstone resonance and levels 0–5, Spellstone Tuner, nine enchantment definitions, Enchanter Pearl, enchantment/curse transposers, Forger Gem/Crystal, Ethereal Forging Charm, permanent Astral Fruit slot bonus. |
| world_manipulation | limited | Infinite bonemeal, area excavation and growth, Starlight Bucket, Ocean Stone weather, light blocks, Dimensional Anchor respawn. No general place/exchange/build wand. |
| world_content | present | Spellstone Hut underground structure (spacing 32, separation 11), Etherium ore feature, Ichor Sprite and Piglin Wanderer, chest loot additions and curse-boosted vanilla monsters/bosses. |
| death_respawn | core | Sixth Curse Soul Crystal loss (-10% max health per lost crystal; default max 9); recovery, Soul Compass, Enigmatic Amulet vessel, Storage Crystal, Dimensional Anchor, Eternal Binding, conditional lethal intercept. TMM disables Eldritch Amulet keepInventory. |
| trade_economy | limited | Loot and special drops, Piglin Wanderer, equipment repair economics; no independent player trade/currency network established. |
| progression_tiers | core | Parallel ordinary relic/exploration, 10 basic spellstones, Etherium/Core/Cube, five sword upgrades, Nether cursed materials, Twisted/Pure/Abyssal branches, six Redemption levels; do not force curse path or count unobtainable Creation Heart. |

## 4. Spellstone system: 12 actual recipe records

Table recipe: Spellcore + 2 Ender Pearls + Ichor Droplet + 3 Crying Obsidian. SpellstoneTableMenu exposes core/debris/stone/ingredient/result containers; SpellstoneTableRecipe declares seven ordered ingredients, debris_count and optional all_different flag. Player uses table GUI and recipe viewer; do not treat ingredients as unordered. Loot tables and data/loot_modifiers provide basic stones/cores/debris at matching vanilla sites (e.g. Golem Heart in mineshaft/dungeon; Ocean Stone shipwreck/buried treasure; Blazing Core ruined portal/fortress; Forgotten Ice igloo/ancient city ice box; Void Pearl End City). Table inputs below list central item and paired ingredient types; exact quantities/order in recipe JSON. Passive effects and vulnerabilities from native localization and class family; active payment/cooldowns depend on stone/config.

| Stone | Debris | Seven ingredient slots (center; paired materials) | Practical use |
| --- | ---: | --- | --- |
| Golem Heart | 6 | Iron Ring; obsidian, iron, redstone blocks | Armor, armorless alternative, melee resistance, knockback resistance; magic vulnerability. |
| Blazing Core | 6 | Magma Cream; magma blocks, blaze powder, basalt | Fire immunity, limited lava immunity, feedback burning, fast effect decay; aquatic vulnerability. |
| Ocean Stone | 6 | Heart of the Sea; dark prismarine, crystals, lapis blocks | Underwater breathing/vision/mobility, weather at XP cost; fire vulnerability. |
| Forgotten Ice | 6 | Iron Block; iron ingots, snow blocks, blue ice | Frost Walker, projectile/sonic resistance, freezing active/feedback; fire vulnerability. |
| Revival Leaf | 6 | Infinimeal; leaves, emeralds, seeds | Regeneration, crop acceleration, conditional flight near plants, heal others at XP cost; fire/projectile vulnerability. |
| Eye of Nebula | 7 | Eye of Ender; mod Ender Rod, chorus fruit, end stone bricks | Target teleport, magic offense/resistance, evasion; vulnerability in water. |
| Angel's Blessing | 8 | Quartz Ring; Starlight Particle, membranes, glowstone | Dash, fall/collision immunity, projectile deflection and acceleration; wither/void vulnerability. |
| Etherium Core | 9 | Earth Heart; mod Ender Rods and Etherium Blocks | Armor/toughness, shield threshold, retaliatory damage, Starlight Blessing. |
| Void Pearl | 10 | Void Stone; gold blocks, Etherium nuggets, Ender Pearls | Darkness aura, wither attacks, status immunity and conditional survival. |
| Illusion Lantern | 10 | Blaze Powder; soul soil, cyan glass, netherite scrap | Automatic soul flames and damage sharing, darkness affinity, plant destruction and light penalty. |
| Lost Engine | 13 | Lightning Rod; copper/redstone blocks and pistons | Mobility/toughness, 25% cooldown reduction tooltip, critical damage and fall/explosion immunity; lightning danger. |
| The Cube | 24 | Cosmic Heart; six distinct items from ten basic-stone tag | Mixed movement/mining/combat and immunities, damage ceiling, lethal intercept, random teleport. |

Cube materials tag contains Golem Heart, Blazing Core, Ocean Stone, Angel's Blessing, Eye of Nebula, Void Pearl, Forgotten Ice, Revival Leaf, Lost Engine, Illusion Lantern; requires six distinct choices. Creation Heart registered, tooltip describes flight/immortality/Wither-skull ability, but only referenced in Curios spellstone tag among bundled data JSONs: no native recipe or loot acquisition identified. Exclude it and OP Loot Generator/The Judgement/Quote Player from survival ratings, quest rewards and balance ceilings unless legitimate acquisition later proven. Spelltuner inventory right-click tunes eligible stone, cooldown/discovery passive; Spellstone Sword right-click with offhand stone sets resonance, accepts Spellcore via inventory stack interaction to raise SPELL_LEVEL by one up to 5 (consumes one each). Ten basic resonance styles include melee armor penetration, burning, water/projectile, frost, growth/healing, mobility/chain hook, soul fire, target swaps and parry/debuff removal. Exact per-style functions are in SpellstoneSword.java compiled methods; energy capacities differ (Blazing 100, Ocean 64, Ice 100, Revival 32, Angel 7+level); do not treat as interchangeable universal mana or imply simultaneous styles. GUI result slot consumption and exact active cooldown/charge formulas warrant targeted numeric crosswalk if needed for balance; ordinary teaching can use exact loaded recipe.

## 5. Personal equipment, industry and resource ledger

- EtheriumProperties compiled material: tier uses INCORRECT_FOR_NETHERITE_TOOL, durability 2794, speed 8, attack bonus 5, enchantability 24. Armor material helmet/chest/legs/boots 4/9/7/4 = 24 armor; toughness 4 each = 16, base material knockback resistance 0, enchantability 24. Actual items can add distinct modifiers. EtheriumProperties.hasShield is maxHealth × EtheriumShield attribute > current health; it is a low-health threshold, not an independently depleting energy tank. Armor and core supply shield-related modifiers; do not claim exact damage absorption cap without event-handler crosswalk. Armor invisible visually, not player invisibility. Etherium Hammer (5 ingots + 2 mod Ender Rods) area mining, Scythe (2 ingots + 2 rods) area breaking and Etheric Resonance enhanced harvest, Sword avoidance and enhancement parry. Honor actual tool tags and block/event restrictions.
- Miner Ring (4 copper, iron pick, coal, Iron Ring, blast furnace): MINER_POINT clamped 0–256; tick regenerates 1 per 80 entity ticks if below cap. Nearby blast processing subtracts 2 × number of converted inputs. Native right-click fuel recharge and mining-drop handling, not smelting all furnace recipes or free doubling. 128 inputs per full charge in that branch; four-second/point nominal recharge if ticking continuously. Smelt and blast recipes for Iron/Gold items included; distinguish conversion of dropped raw material from generating new ore.
- Infinimeal: Earth Heart + eight different plant inputs (cocoa, vine, warped fungus, apple, wheat, poppy, lily pad, Nether Wart); reusable bonemeal and extra target support in item code. Revival Leaf passive nearby crop growth. No general farming factory. Etherium feature size 3, count 3, uniform bottom-to-top, replaces End Stone; Spellstone Hut underground structure spacing 32/separation 11. These are worldgen definitions, not measured density.
- XP paid Heaven Scroll flight near active Beacon; Fabulous Scroll outside beacon, free within beacon; Ocean Stone weather and Revival Leaf active healing cost XP. No generic FE conversion. Travel Recall returns bed/anchor or Overworld fallback, Wormhole Potion records first holder and teleports to named player, Twisted Mirror reusable recall. Angel dash and Eye target teleport; Cube active random travel among three main dimensions. Dimensional Anchor uses charge/respawn fuel and dimension checks; not arbitrary safe cross-dimensional spawn. Native source semantics only, pack claims/teleport rules separate.
- Ring of Ender accesses vanilla Ender Chest; Magnet Ring collects nearby drops; Antique Book Bag specialized linked book carrier, stored books treated as in inventory; not arbitrary expanded storage. Storage Crystal is death recovery data, not an item-drive network. Avoid treating carry/storage as factory logistics.
- Forger Gem tooltip halves anvil XP and improves anvil durability/result; Forger Crystal further preserves anvil, permits unbreakable result with undamaged identical tool and certain lethal-protection behavior. Ethereal Forging Charm reduces durability costs and changes forging results; inspect anvil mixin before deriving maximum numerical stack or compatibility with Apotheosis.
- Enchantment JSONs: Ceaseless I (crossbow), Eternal Binding Curse I, Etheric Resonance I (Etherium tag), Nemesis Curse I (sharp weapon), Redemption Curse I (equippable), Sharpshooter V (crossbow), Slayer V (weapon), Sorrow Curse I (armor), Wrath V (trident). Nemesis localization reflects 35% melee damage; exact modded level cap not native max. Item/book enchant transfer, curse transfer, Enchanter Pearl and Tome of Divination: teach eligible item, real result, not arbitrary enchant duplication. Item attributes and enchantment JSON are separate from effective Apotheosis cap.
- Astral Fruit recipe 8 Astral Dust + apple; enchanted fruit's finishUsingItem calls unlockSpecialSlot(ring, player, item registry ID). Helper adds +1 permanent slot modifier only if this exact ID is absent. No repeatable infinite slots from identical fruit. Forbidden Fruit removes hunger at cost of reduced regeneration; optional lasting choice with Redemption Potion removal route (tooltip/compiled item). Native effects include Abyss Corruption, Blazing Might, Blood Chalice, Ichor Corrosion/Curse, Molten Heart, Pure Resistance, Starlight Blessing, Violence Curse. Ichor Sprite acquisition: IchorCurse.onDeath checks uncanceled death, living attacker with Ichor Curse, nonboss Monster whose target is attacker, native NATURAL spawn, Nether, not infected, line of sight. Marks monster infected, spawns sprites at monster eye position with that MONSTER as owner; not a player pet. Sacred Crystal from sprite loot. Idle effective-AI sprite with empty hand accepts one held item from player; PurifyItem needs live visible owner to start, increments timer, after >100 ticks sets BLESSED=true on copied held item and throws it back, clearing held slot. Not generalized enchantment copying. Piglin Wanderer loot includes Ichor and spear.

## 6. Seven Curses and Abyss — exact defaults and gating

CursedRing.onConfig: painMultiplier 200% (range 100–500), neutralAngerRange 24 blocks, armorDebuff 30%, monsterDamageDebuff 40%, eternalBurning true, soulCrystalsMode NEED_CURSE_RING, insomnia true, lootingBonus 1, fortuneBonus 1, experienceBonus 300 (configured percent bonus; exact event formula separate), enchantingBonus 10, special drops/Ender Ring/uniqueLegacy true, autoEquip false, ultraHardcore false, maxSoulCrystalLoss 9 (0–10), ForTheWorthy true, immediatelyCurseBoost false, giveStarterGear true. These are constructors' defaults not loaded TMM values. Ring Curios canUnequip and persistence enforce binding; EnigmaticHandler.isTheCursedOne also checks persisted SevenCursesBearing. Do not tell players that ordinary unequip removes curse. Twenty-eight named entity loot modifier entries plus chest additions and special loot hook provide conditional resources, e.g. Ghast→Ichor, Blaze→Cinder, Elder Guardian→Guardian Heart, Shulker→Astral Dust, Wither→Evil Essence path; eligible cursed/unique drop conditions must apply, not guaranteed on every kill. CursedShapedRecipe.matches checks taintable input stacks when result cursed/blessed, then shaped pattern; recipe matching and item usability differ. Do not equate simply having ordinary input heart or cursed item ID with eligibility.

Sixth Curse: SoulCrystal.getLostCrystals reads SixthCurseSoulLossCount. updateSoul map uses MAX_HEALTH ADD_MULTIPLIED_TOTAL modifier −0.1 × lost count. Default 9 losses means 10% of otherwise calculated max health remains (before clamp/interacting modifiers); 10 permitted by config is dangerous. Crystal owner-bound physical recovery restores loss count; guide describes permanent item, no automatic deletion by generic grave policy assumed. isAffectedBySoulLoss checks config mode and keepInventory; canDropSoulCrystal enforces configured count. Enigmatic Amulet tooltip creates extradimensional vessel storing items, XP and crystals; StorageCrystal actual store/retrieve handlers support death storage. Native Eldritch Amulet broad inventory retention distinct from Amulet vessel. Pack ownership below.

Worthy: EnigmaticHandler.getSufferingFraction = cursed ticks/(cursed+uncursed ticks), truncates both by 100 at large counters then rounds to 3 decimals HALF_UP; no cursed time →0, no uncursed time →1. isTheWorthyOne requires cursed state and fraction >= clamped abyssThreshold (native 0.995; allowed clamp .05–.999); Dimness Charm changes threshold to 2×threshold−1. It is a fraction, not a fixed number of hours. AbyssalHeart.Events records qualifying attacker UUID on actual post-damage to EnderDragon/AbyssalHeartBearer; MixinEnderDragon.onTickDeath at dragonDeathTime=200 creates permanent reward entity with acceptors, filters tracked attackers to players present in level and below AbyssalHeartsGained cap, increments acquisition counter before pickup. Native default cap 5 (config 1–10). No ordinary unlimited duplicate method established; compiled JAR references to AbyssalHeartBearer identify only Ender Dragon implementation, no other native bearer found; Wither mixin is Evil Essence instead. TMM Ender Dragon loot override must distinguish vanilla loot table removal from this mixin drop; do not assume either a bypass or suppression.

Twisted Heart recipe consumes Earth Heart and Ghast Tear, Eye of Ender, 2 Blaze Powder, 3 Redstone; Pure Heart substitutes Ichor and Glowstone in same pattern. The Twist uses 4 Evil Essence, 2 Netherite Ingots, Redstone, Twisted Heart, Acknowledgment; The Infinitum upgrades with Abyssal Heart, 2 Cosmic Hearts, 2 Evil Essence, 2 Netherite Ingots, Enchanter Pearl. The Bless uses Pure Heart/Sacred Crystals etc. Abyssal equipment branches: Eldritch Amulet, Desolation Ring, Dimness Charm, Chaos Elytra, Violence Scroll. Decision of Annihilation special recipe (AnnihilatingRecipe.matches) requires four abyssal-tag relics in corners, Evil Ingot top middle, tainted Abyssal Heart center, Twisted Heart bottom middle, empty middle-row sides. Worthy player's remaining-items handler returns corners based on gained hearts: >4 top-left, >3 top-right, >2 bottom-left, >1 bottom-right; do not claim unlimited first-heart access. Respect cursed time and relic ownership.

## 7. Redemption — six distinct levels

RedemptionRing.Helper.getPossibleLevel loops thresholds for both suffering percentage and total cursed ticks / 1200 (minutes at 20 TPS). 0-index arrays have final 200% sentinel, so resulting 1..6 thresholds:
| Level | Cursed-time fraction at least | Cursed minutes at least |
| ---: | ---: | ---: |
| 1 | 0% | 5 |
| 2 | 10% | 10 |
| 3 | 25% | 20 |
| 4 | 45% | 40 |
| 5 | 75% | 60 |
| 6 | 99% | 90 |
Native Holy Stone recipe (Cursed Stone, 3 Sacred Crystals, 2 Ichor, 2 Glowstone Dust, Pure Heart). CursedRing.Events.onDrop branch recognizes possession of Holy Stone and replaces equipped Cursed Ring with Redemption Ring, records level and REDEMPTION_THRESHOLD = uncursed ticks /16; hardcore direct use in BlessStone.use also replaces ring. Native separate Cursed Stone destruction branch requires Nether/lava death and location conditions; don't recommend a mandatory death or assert safe escape in TMM. Curios death-event ordering/Smart Keep may affect these interactions. Redemption ring canUseRelic level >4; damage multiplier 100 −25×clamp(level,0,5)/5 percent (default 75% damage at levels 5–6), outgoing bonus 20×(clamp(level,1,5)−1)/4 percent, +0.25×(level−1) regen modifier capped 1.25, loot/fortune bonus applies rounded half at levels 3–4 and full at >4. Default regenSpeed20 ticks, looting/fortune bonus 1. Native tier and rate formulas not a guarantee of cross-mod stacking. Holy Stone direct right-click branch specifically tests hardcore; ordinary survival redemption uses death handler, not simple right-click. Don't force this branch for players in quest book.

## 8. Instructional graph: research nodes, not actual FTB IDs

59 unique backward-only teaching nodes. Dependencies indicate editorial ordering rather than every ingredient acquisition; independent alternatives retain explicit actual recipe/use prerequisites in sections above. No automatic rewards, forced Seven Curses, forced death, or OP-only acquisition.

| ID | Prior nodes | Lesson | Action/boundary |
| --- | --- | --- | --- |
| E01 | — | The Acknowledgment | Craft book + lantern or use welcome reward; open material guide. |
| E02 | E01 | Earth Heart | Explore dungeons or assemble fragments; record source and unlock book. |
| E03 | E01 | Discover a spellstone | Acquire actual chest relic or reconstruct; equip, show tradeoff. |
| E04 | E03 | Spellcore and debris | Acquire chest components and identify their different roles. |
| E05 | E04 | Spellstone Table | Make table with Spellcore, Ichor Droplet, 2 Ender Pearls, 3 Crying Obsidian; optional wait until ingredient acquisition. |
| E06 | E05 | Reconstruct a stone | Choose 7-slot layout and correct debris count; confirm actual crafted item. |
| E07 | E06 | Spelltuner | Tune an eligible stone through inventory interaction. |
| E08 | E04 | Spellstone Sword | Craft base sword; resonate with offhand stone. |
| E09 | E08 | Upgrade resonance | Consume Spellcores via inventory stacking up to level 5; demonstrate ability. |
| E10 | E06 | Golem Heart | Test armorless vs armored benefits and magic vulnerability. |
| E11 | E06 | Blazing Core | Demonstrate fire protection and water-creature drawback. |
| E12 | E06 | Ocean Stone | Underwater breathing/swimming and XP-paid weather. |
| E13 | E06 | Forgotten Ice | Freeze target, use frost movement and identify fire vulnerability. |
| E14 | E02 | Infinimeal | Use renewable growth on eligible plant; distinguish from automation. |
| E15 | E06,E14 | Revival Leaf | Healing, growth and conditional movement near plants. |
| E16 | E06 | Eye of Nebula | Targeted teleport, magic resistance and water weakness. |
| E17 | E06 | Angel's Blessing | Air dash and fall protection. |
| E18 | E06 | Lost Engine | Movement and cooldown specialization; note lightning liability. |
| E19 | E01 | Nether exploration | Find Infernal Cinder and Ichor in loot; cursed drops alternative. |
| E20 | E01 | End exploration | Find Astral Dust, Starlight Particle, Etherium loot; identify natural ore. |
| E21 | E20 | Etherium smelting | Mine actual eligible ore, blast raw metal into ingot. |
| E22 | E21 | Etherium Hammer | Mine useful materials in area; toggled/sneak modes. |
| E23 | E21 | Etherium Scythe | Area harvesting; show enhanced behavior only after resonance. |
| E24 | E21 | Etherium armor | Acquire actual armor, examine threshold and set enhancement. |
| E25 | E21 | Etherium Sword | Damage avoidance and enhanced parry. |
| E26 | E06,E21 | Etherium Core | Reconstruct core; distinguish shield threshold from extra HP. |
| E27 | E06,E19,E21 | Void Pearl | Darkness-oriented relic and survival probability. |
| E28 | E06,E19 | Illusion Lantern | Soul flames and damage sharing; plant/light drawbacks. |
| E29 | E20 | Cosmic Heart | Combine Heart of Sea, Nether Star, Astral Dust etc. |
| E30 | E06,E29 | The Cube | Six distinct basic stones + Cosmic Heart + 24 debris; not all 10 consumed. |
| E31 | E01 | Ordinary utility rings | Craft iron/quartz/gold rings; choose specialization. |
| E32 | E31 | Miner Ring | Charge with fuel, process dropped blastable input. |
| E33 | E31 | Remote storage | Ring of Ender opens vanilla Ender Chest; magnet pickup optional. |
| E34 | E01 | Utility scrolls | Choose exploration, hunting, XP or survival scroll. |
| E35 | E34 | Heaven/Fabulous Scroll | Beacon and XP-limited flight; demonstrate payment. |
| E36 | E19 | Recall/Wormhole | Use eligible native travel consumable; consent for player target. |
| E37 | E20 | Astral Fruit | Acquire and consume for extra ring slot; don't presume unlimited slots. |
| E38 | E19 | Forbidden Fruit | Explain hunger removal and healing penalty; optional lasting choice, with Redemption Potion reversal. |
| E39 | E31 | Forging aid | Use Forger Gem and repair/forge; later crystal optional. |
| E40 | E21,E26 | Etheric Resonance | Apply native enchant where eligible and compare capability. |
| E41 | E01 | Seven Curses opt-in | Explicit informed choice, not a mandatory task for all players. Explain bound slot and soul loss. |
| E42 | E41 | Soul recovery | Explain owner-bound crystals, max HP modifier and compass; no mandatory death. |
| E43 | E41,E19 | Cursed materials | Obtain actual Ichor and Evil Essence via eligible loot/kill. |
| E44 | E42,E43,E02 | Twisted Heart | Cursed recipe and taint requirements, then The Twist. |
| E45 | E43,E02 | Pure Heart | Create alternative heart, identify Sacred Crystal/Ichor Sprite branch. |
| E46 | E45 | The Bless | Craft protective relic; distinguish relic access from redemption. |
| E47 | E44 | Worthy status | Track suffering fraction; reach native configured threshold if pursuing Abyss. |
| E48 | E47 | Abyssal Heart | Eligible boss participation; personal acquisition cap. |
| E49 | E48,E44 | The Infinitum | Abyssal Heart, Cosmic Heart and The Twist upgrade; gated use. |
| E50 | E48 | Abyssal relic choice | Eldritch Amulet, Burden of Desolation, Dimness Charm, Chaos Elytra or Violence Scroll; preserve TMM overrides. |
| E51 | E45,E46 | Holy Stone | Explain native redemption branch and level threshold; optional, not forced death. |
| E52 | E51 | Redemption Ring | Conditional transformation by native death-drop handler or hardcore Holy Stone use; inspect actual level and gate. |
| E53 | E45,E39 | Forger Crystal | Craft eligible upgrade; anvil identical-item unbreakability operation; note TMM compat. |
| E54 | E01 | Enigmatic Amulet recovery | Explain vessel/Storage Crystal and pack death interaction; no requirement to die. |
| E55 | E21 | Dimensional Anchor | Craft respawn-anchor upgrade, use charged allowed dimension; do not promise unrestricted placement. |
| E56 | E01 | Companion books | Animal protection and Hunter damage redirection, choose meaningful existing pet; optional Sanguinary Handbook. |
| E57 | E20 | Starlight equipment | Craft ingots and Starlight utility item; avoid claiming unlimited fluid automation. |
| E58 | E45,E43 | Ichor Sprite encounter | Use Ichor Curse against eligible naturally spawned Nether monster targeting the player; obtain Sacred Crystal or observe eligible idle sprite purifying an offered item; monster owner, not pet. |
| E59 | E48,E50 | Decision of Annihilation | Construct exact four-corner abyssal recipe with tainted heart, Evil Ingot and Twisted Heart; note conditional recipe remainders. |

## 9. TMM-specific separation

Baseline a98eadfbd625260f8d2c984cd51844ee76f198af. [defaultconfigs/enigmaticlegacyplus-server.toml](../../../defaultconfigs/enigmaticlegacyplus-server.toml) explicitly sets sevenCurses.giveStarterGear=false, autoEquip=false, abyssItems.eldritchAmulet.keepInventory=false. Starter reward moved to welcome quest; actual server config existing-world overrides and death event ordering not verified. [Existing enigmatic_legacy chapter](../../../config/ftbquests/quests/chapters/enigmatic_legacy.snbt) is one optional checkmark placeholder at baseline. Do not revise chapter here. Ring of Seven Curses is **not** Relics Ring of Seven Deadly Sins (separately owner-excluded). No changes to exclusions, corpse/XP ownership, mob drop tags, loot, recipes or class system. Monk balance handoff: Etherium native baseline 24 armor/16 toughness, armorless Golem Heart and shield threshold, cursed soul-loss multiplier, Redemption damage formula, spellstone resistances/deflection and permanent ring slots deserve targeted numerical audit if used as benchmark. No generic native QA gate.

## 10. Supplementary 11-domain classification

Owner-approved scale 0 absent, 1 some assistance, 2 significant assistance, 3 independent, 4 superior, 5 transformative relative to vanilla. Combat 5 (resonance weapon styles); Protection 5 (elemental resistance/weakness choices, threshold defense, armorless route); Survival 4 (regeneration, resistance, hunger transformation with cost); Gathering 4 (area mining/harvesting, loot bonuses, no autonomous resource generation); Industry 2 (blast-on-drop and repair, no broad production network); Construction 1 (area clearing/fluid utility, no general placement); Transportation 4 (flight, recall, target teleport and mobility); Logistics & storage 2 (Ender Chest access, magnet, specialized book bag; no network); Life & agriculture 2 (growth, harvesting and companion support, not broad autonomous farms); Death & recovery 5 (conditional actual soul loss, recovery and prevention radically alter gameplay; no credit for disabled Eldritch keepInventory or inaccessible Creation Heart); Advancement 5 (persistent curse time/Redemption, material and resonance progression). Total 39/55, seven independent and superior domains, four transformative. Substantial Magic, excellent magical adventurer, not a self-contained base lifestyle. Classification editorial, independent from full factual Atlas.

## 11. Evidence closure and followup

Full 29-axis static pass, recipe and loot census, core material/curse/Redemption gates, actual constructor and selected bytecode values, 12 table recipes and 59-node teaching graph. Native behavior presumed functional. Specific gaps for optional later numerical/source refinement: per-spellstone energy/cooldowns, full anvil/attribute stacking and remaining tool/loot conditions. Ichor Sprite ownership/purification, Astral Fruit repeat limit, Dragon bearer identity and Annihilating recipe traced in follow-up. Do not treat these as resolved or infer in-game behavior from tooltip; graph is representative, not exhaustive 127-recipe checklist. TMM actual death/first-dragon-drop and enchantment integrations separate. No runtime or CI full build authorization. Research deliverable status: source evidence ready for review, focused followup retained.
