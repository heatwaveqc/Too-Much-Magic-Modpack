# Occultism pinned Battlefield and entity-capture devices

Exact local `occultism-1.21.1-neoforge-1.227.0.jar`, SHA-512 `063d775420e1efa916d1ed94f3f5bfc3b00b976b1de6fc333eda9886ea3665aa38254407a18469ed0a334bcac67b204392c9bf197fc10916ce36a9e29abd5ef0`. [Parent](occultism.md), [all rituals](occultism-pinned-rituals.md), [crafting/guide](occultism-pinned-crafting-guide.md). This source audit does not assert loaded tag membership, successful capture, protected boss farm permission or runtime acceptance.

## Acquisition and role

Battlefield ritual `ritual/craft_dimensional_battlefield`: craft_afrit pentacle, bound Afrit Book activation, duration counter300; four compound Otherstone-or-Otherrock ingredients, Netherite Ingot tag, Iesnium Block tag, Spirit Attuned Crystal and Afrit Essence. Dictionary guide crosswalk records binding/crafting entry and exact recipe ID. Place the block, open its dedicated menu, supply entity-data soul item, attribute-modifier weapon and Soul Value fuel; keep redstone unpowered. Actual loot generation is virtual/native loot-table and LivingDropsEvent evaluation, not repeated visible mob spawning/killing. A ritual item task proves block acquisition, not loot operation.

Soul Gem and Fragile Soul Gem acquisition are separate rituals in the complete ritual appendix. `ritual/misc_trinity_gem` is late contact_eldritch_spirit counter780: Soul Gem activation; Afrit/Marid/Cruelty Essence, Echo/Dragonyst/Witherite Dust tags and three Iesnium Dust, plus human-tag sacrifice. It yields Trinity Gem with rolls-per-operation3. Do not gift this as an early material/quest reward. Fragile Gem has fail-chance0.8 for Battlefield evaluation; ordinary Soul Gem no failure component by default.

## Capture/release contracts

`SoulGemItem` capture is main hand only, target must be alive LivingEntity, server-side, never Player, empty ENTITY_DATA. Fragile/Soul/Trinity each checks **its own** deny tag when that exact item is used. It serializes the target with ID and full saved data into the item, then discards original; there is no owner check in this capture method. This does not mean every modded/protected entity is capture-safe. Bundled fragile_soul_gem_deny_list and soul_gem_deny_list include Iesnium Golem, optional #c:capturing_not_supported and #c:bosses. The inspected JAR has no trinity_gem_deny_list JSON; contributions from the effective pack may add it, so an empty native definition is not a global absence claim.

Release main-hand useOn removes ENTITY_DATA **before** validating entity type/creation. It clears Pos, loads entity, places at clicked position or adjacent face when collision exists, rotates toward player+180 and calls unchecked addFreshEntity. Fragile Gem removes itself from main hand after this call; ordinary gem remains empty. Invalid type, creation failure or rejected add has no payload rollback in this method. Test only disposable captures before trusting valuable pets/workers.

MagicLampItem capture is narrower: owned SpiritEntity with present job, main hand, alive server-side and empty lamp. It saves entity data plus job/name, discards worker. Release clears payload before validation/add, resets displayed job/name afterward, no success check/refund. Filled lamp every20ticks has1/100 random chance to send owner a spirit message, and hurts sleeping Player by1 in that branch. It is not a generic arbitrary mob container or unconditional safe storage.

## Battlefield operation and economy

Soul slot accepts any stack with ENTITY_DATA; weapon accepts ATTRIBUTE_MODIFIERS; fuel accepts SOUL_VALUE. These component gates do not validate every payload as a usable living entity in the insertion method. Output has25slots. Server tick pauses on redstone signal or missing soul/weapon; EvilCraft Unusing pauses weapon near damage max−6. Cached entity/loot rebuilds on item object changes. It creates a living entity from payload **type** for simulation; does not load every captured entity property into that simulated instance. Captured attributes may separately influence health budget.

Health budget starts at simulated max health (can read saved max-health base from payload attributes), multiplied by configured butcherLifeMultiplier; soulValue cost equals health unless soul itself has SOUL_VALUE, in which case0. Weapon sums attack-damage/speed modifier amounts (rather than universally evaluating every modifier operation), adds relevant Sharpness/Smite/Bane/Impaling bonuses and triples damage for Iesnium-knife-tag weapon against designated demon-healed types/boss tags. Derived work count is max(integer(health/damage),1); hit interval integer(40/attackSpeed). This is a simulated processing formula, not live entity DPS.

Each hit decrements work count and has configured butcherHurtChance to damage weapon1. Completion computes fuel luck component, spends fuel unless soul itself has SOUL_VALUE, with shrink `1 + soulValue/max(fuelValue,1)` (integer division). Fuel availability precheck requires fuelValue×count >= soulValue, so exact divisibility can still request one more unit than that ratio; shrink handles available count. Spending occurs before defeat's failure check, so Fragile Gem's80% failure does not refund work/fuel. No FE/fluid input in this operation.

Defeat uses soul ROLLS_PER_OPERATION default1; bosses force1, possessed types reduce rolls and may switch to base mob. It then **overwrites passed fuel luck with min(rolls,16)**, adds integer(luck²/100) rolls and a probabilistic fractional roll. Thus adding a luck-valued fuel does not straightforwardly increase this method's final luck as its parameter name might suggest. Soul consumption chance is optional CONSUME_CHANCE/luck; defaults0. The native Soul Gem can be a reusable template unless changed by components/config/hooks.

Loot lookup first tries `occultism:battlefield/<entity namespace>/<entity path>`, falls back to ordinary entity loot if empty. Evaluates loot with a cached FakePlayer, equipped weapon/tool, owner-like attack context and luck, then posts LivingDropsEvent for each roll; canceled events emit no output. Item insertion remainders into current output destination are **ignored**, including generated XP bottles, so full/rejecting output can lose loot after costs. Extractor directly below selects UP-face destination two blocks down when present, otherwise internal output; it does not make insertion transactional. Captured XP orbs within squared distance1 during listener window increment xpStored; >9 stored converts tenXP per Experience Bottle before adding current simulated XP reward. SaveAdditional serializes inventories, not a guaranteed persistent complete combat-progress/XP state; acceptance must verify reload behavior.

## Protected-output audit: distinct from miner tables

The earlier194-miner scan had no literal Dragon Egg/Nether Star outputs; **that result does not apply to Battlefield**. Two explicit native overrides:

| Simulated entity | Native override | Actual payload |
| --- | --- | --- |
| minecraft:wither | loot_table/battlefield/minecraft/wither.json | one Nether Star entry per evaluated loot-table roll |
| minecraft:ender_dragon | loot_table/battlefield/minecraft/ender_dragon.json | one weighted selection: Dragon Breath weight3, Dragon Egg weight1 |

Ordinary/Fragile Gem deny bosses through #c:bosses. Trinity uses separate deny check and costly late ritual; actual capture requires effective tag resolution and world event behavior. Do not describe either literal payload as an approved TMM farm or proof of first-boss-gate bypass. Record it for future progression review: acquisition, capture eligibility, virtual roll, output capacity and event hooks all matter. A source-supported candidate test uses disposable world, confirms deny ordinary gem, inspects Trinity effective tag, verifies one operation and real delivered quantity, and compares empty/full-output boundaries.

## Remaining device closure

All native device recipe IDs and guide parents are inventoried, but Stable/Entity Wormhole and satchel control paths still require source write-up before parent source-completion. These are distinct optional branches; no runtime items/configs/quests or adapter are implemented here. Full source scopes preserve implementation limitations rather than treating client tests as already passed.

Sources: `common/item/tool/{SoulGemItem,MagicLampItem}`, `registry/OccultismItems`, `common/blockentity/DimensionalBattlefieldBlockEntity` including nested inventories, exact ritual/tag/loot JSON; supporting storage/worker appendices for shared extractor behavior. CFR warnings about try/finally are treated cautiously; no exploit claim is inferred.
