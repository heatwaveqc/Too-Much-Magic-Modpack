# Witchcraft family comparison — four independent capability evaluations

2026-11-25. Atlas #410, owner-requested batch evaluation. Not a combined family score: JAWR, Enchanted, Hexerei and EvilCraft are separate traditions with separate resources and native mechanics. Integration baseline a98eadfbd625260f8d2c984cd51844ee76f198af. Existing exact-release dossiers reread, current pins checked, no new JAR audit or game test.

## Provenance

- [JAWR 0.5.39](witchery-remake.md): active Packwiz SHA512 a3d50e65bb494b4d9b709163a295ddcecf53c498314df473d6de55633a4a5ebe810b9a0b5672f97f41786c03af01ef3ac91f0262216e074887e75d6afc35141d. Closed-source, prior exact-binary inspection; 314 recipes, 223 guide entries, 68 lesson nodes.
- [Enchanted: Witchcraft 4.2.7](enchanted-witchcraft.md): active SHA1 2380cb5a49c7eb86afa6f790a920d1872d5cb85e. Prior matched binary and [version-aligned source](https://github.com/Favouriteless/Enchanted/tree/55f37c7e15a2848be2cf25ff20232a1893795e75); 47 rites, 37 teaching slices.
- [Hexerei 0.5.0.3](hexerei.md): active SHA512 6ad9f14ad23e84e693c8a02fa210871f7f9f7abb34acb342d9c72aacaa822dc4d06d203d78803ab40dd5eb334344961586a3f223ae877c362f1788b7be1dad6a. Prior exact binary inspection, near-release source supplementary only (does not establish entangled storage); 54 instructional nodes.
- [EvilCraft 1.2.100 factual dossier](evilcraft.md): active pin now 1.2.102, SHA512 278ab7cadb9077400563e2a238efdf63aecc71dbcd61b81db7620d020f3ff5420bff522057c60ef57a47cc7efd3bf0f9cb16c69c31ec0b9d9aed0c8fed29a5d5. Verified [source delta 1.2.100→1.2.102](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.100...1.21.1-1.2.102), head ecc323924e482f603d53c918f0af39cc68a8e924: eight changed files, only functional Java change display stand recipe returning ItemStack.EMPTY instead of null, plus removed invalid broom guide appendices (witherer, hungerer, kamikaze, withershield), changelogs and version. No relevant domain change; prior 184-recipe audit remains useful, but actual new JAR not independently inspected and prior version counts remain historical. Do not count removed/invalid guide entries as features.

## Editorial ratings

Scale 0 absent, 1 some assistance, 2 significant assistance, 3 independent, 4 superior, 5 transformative relative to vanilla. Major 9–11 independent domains plus personal and base breadth; Substantial 4–8. No credit for external TMM hooks or other traditions. All ratings subjective, not gameplay performance benchmarks.

| Domain | JAWR | Enchanted | Hexerei | EvilCraft |
| --- | ---: | ---: | ---: | ---: |
| Combat | 5 | 2 | 3 | 4 |
| Protection | 5 | 5 | 4 | 3 |
| Survival | 5 | 3 | 4 | 4 |
| Gathering | 4 | 3 | 3 | 5 |
| Industry | 4 | 2 | 2 | 5 |
| Construction | 2 | 2 | 1 | 2 |
| Transportation | 5 | 4 | 4 | 4 |
| Logistics & storage | 2 | 1 | 5 | 2 |
| Life & agriculture | 5 | 4 | 5 | 2 |
| Death & recovery | 4 | 4 | 1 | 0 |
| Advancement | 5 | 3 | 3 | 4 |
| Total | 46 | 33 | 35 | 35 |
| Independent (>=3) | 9 | 7 | 8 | 7 |

## JAWR — 46/55, 9 independent, Major Magic

- Combat 5: personal infusions, 15 Symbology spells, remote taglock voodoo and curses, familiars, temporary spectral servants and two independent supernatural bloodline ladders. Not every spectral creature is a pet.
- Protection 5: protective poppets, curse protection/removal, armor, bloodline defensive abilities, Sunstone and Tarot. Survival 5: blood-based sustenance and healing, custom brews, sustained incense/censers, Drain Growth, Mend Flesh and transformed forms with distinct vulnerabilities.
- Gathering 4: six Blocks Below rituals extract existing iron/gold/copper/zinc/cinnabar/amber, Earth Bore and Hag tools, Koboldite labor and Vampire Children. No generic ore creation.
- Industry 4: Oven, distillery, wheel, ordered cauldron, custom potion and infusion economy, ritual conversion, nature-powered altar and recharge. More extensive than a single specialist workstation, but not a general automated factory.
- Construction 2: earth/erosion/frost and biome transformations, block break and brambles; no broad bulk placement tools demonstrated.
- Transport 5: broom, bat/wolf forms, Propulsion, personal Otherwhere infusion, bound Waystone rites and Spirit World travel with actual transfer limits.
- Logistics 2: Grassper single-item ritual holder and Hobgoblin Koboldite chest deposits, dedicated blood storage; no warehouse.
- Life 5: Mutandis, Extremis, Mutating Sprig, mushroom logs, breeding, familiars, coven, plants and supernatural servants.
- Death 4: consumable carried death poppet, Judgement conditional save, familiar resurrection and conditional vampire high-rank survival. No universal immortality/postmortem player grave retrieval; pack effective TMM transformation rules can change native outcomes.
- Advancement 5: two separate rank-10 bloodlines, four personal infusion options, symbology, 22 major Arcana, dreams/Spirit World, coven and material/ritual tiers. Do not require coexistence of bloodlines or count custom Tyr Progression bonuses.

## Enchanted — 33/55, 7 independent, Substantial Magic

- Combat 2: Sky Wrath, Voodoo and curses, Arthana, defensive rites; no comparable broad combat kit. Protection 5: nine protection poppet families, ordinary/sturdy/infused variants, persistent remote shelf, protection circles and curse management. Four-slot shelf uses global UUID-indexed SavedData; no explicit distance/loaded-chunk requirement in trigger path, but persistence and event ordering remain QA.
- Survival 3: breathing/disguise/protection, hunger poppets and repair, limited brew catalog. Gathering 3: Transpose Iron extracts actual ore, herb/tree outputs and Arthana drops.
- Industry 2: oven, kettle, distillery and wheel dedicated to magical material processing; Broiling performs actual item smelting but no broad manufacturing network. Construction 2: Forest rite, temporary physical protection barriers and mutations; no arbitrary building tools.
- Transport 4: flying broom and entity/location transposition, including dimension transition; not unlimited offline entity travel.
- Logistics 1: remote poppet protection and narrow transport, no broad storage. Life 4: herbology, weighted mutations, Fertility radius 25, Forest radius 25, familiar cat, plant and animal effects.
- Death 4: damage-specific fatal poppets, usually restore 1 HP; hunger restores food, void relocates into sky. Ordinary/sturdy/infused use counts 1/2/1, not infinite. Remote prepositioned protective stock, not universal death save. Familiar recall reconstruction has no proven health reset for dead cat, do not claim guaranteed resurrection.
- Advancement 3: altar upgrade, circle and material progression, persistent curses/familiar and upgraded poppets; no equivalent JAWR personal infusion or bloodline skill tree. Witch's Cauldron is unfinished, not a credited additional brewing system.

## Hexerei — 35/55, 8 independent, Substantial Magic

- Combat 3: Warhammer, damage candles, blood sigil and crow support, no general spell arsenal. Protection 4: witch armor, sage spawn suppression, broom protective attachments, candle buffs and familiars. Survival 4: healing/regeneration, slow falling, water breathing and other sustained candle effects, familiar aid.
- Gathering 3: crows harvest eligible crop/block tags, collect items and pickpocket generated loot; no player area mining or renewable ore machine. Industry 2: eight-input 2000mB Mixing Cauldron (whole-tank fluid conversions separate), drying, pestle and dipping; no broad refining/manufacturing.
- Construction 1: decorative palette, wax and minor environment assistance, not building tools. Do not credit Cutting Crystal/Fire Tablet without demonstrated survival acquisition.
- Transport 4: three broom tiers, replaceable brushes/modules, seat and whistle; owl transport is item logistics, not player teleport.
- Logistics 5: 1024-single-type jars, portable 36-slot Coffer, paired Entangled Coffer persistent shared UUID inventory (not copied items), crow collection/whitelist/perch handling, owl five-slot sealed parcels and registered named depot eight-stack inbound queue. Cross-dimensional delivery controller and chunk forcing in binary; actual restart/multiplayer/security/performance not validated.
- Life 5: reusable herbs, sage crop, growth candles, tamed breedable crows and owls, crop automation using separate harvesting and gathering birds.
- Death 1: Crow Ankh protects familiar, not player; assistance only in animal recovery. Advancement 3: branch-based material, moon, Nether/End and broom attachment upgrades, no permanent character tree. Earlier descriptive dossier calls Hexerei a major standalone system; current formal 9-domain threshold makes it Substantial, not contradictory to breadth or standalone playability.

## EvilCraft — 35/55, 7 independent, Substantial Magic

- Combat 4: Distortion/Destruction Maces, Vein Sword, Necromancer Staff zombies, grenades and paid effects. Protection 3: spirit-specific intercept, cleansing/extinguishing pendant, broom defensive modifiers; spectral glasses visibility only. Survival 4: Rejuvenated Flesh uses Blood rather than consuming itself, Invigorating/Primed Pendant, Blood Chest repairs and Purifier.
- Gathering 5: automatic blood collection from kills/stains, Spirit Furnace boxed-spirit mob drop production, Spirit Reanimator conditional spawn eggs. Wither→Nether Star native route is evidence of upstream power, not proof enabled by TMM gates. Unsupported entity reanimation may erase contents; no universal ore generation.
- Industry 5: specialized but transformative renewable Blood-based industrial loop, Promise machine upgrades tier0–3, Infuser, Spirit Furnace, Reanimator, Blood Chest and environmental processing; no universal ore refining. Construction 2: paid broom SMASH, biome/weather tools and terrain-capable mace, no broad placement system.
- Transport 4: modular Blood-fueled broom, paid Blood Pearl teleport and Effortless Ring. Logistics 2: substantial but fluid-specialist tanks up to 655,360,000mB combinable capacity, shared 4,000mB Entangled Chalice per ID, sided machine item I/O; no general inventory network. Do not upgrade general storage rating solely because of tank capacity.
- Life 2: Undead Trees, sapling production, mob/spawn-egg handling (stronger gathering than crop/livestock); no broad agriculture.
- Death 0: player fall-death stains and Vengeance Spirits harvest post-death resources; do not prevent/reverse player death or recover their inventory. Advancement 4: First/Second Age, four machine tiers, Promise speed/efficiency, spirit and modular broom gear, no player skill tree.

## Handoff

Four editorial evaluations complete as a single non-overlapping documentation PR; prior factual dossiers unchanged. EvilCraft version delta explicitly checked, no new binary audit or actual runtime validation. Source evidence, not approval of balance or quest changes. Native capability vs TMM-specific restrictions (blood unification, transformation, player death ordering, protected ore and boss loot, bag nesting) remains separate. Keep both Witchery variations distinct per owner decision. Later shared rating index reconciliation by designated writer; next queue Psi, Hexcasting and Scriptor Magicae.
