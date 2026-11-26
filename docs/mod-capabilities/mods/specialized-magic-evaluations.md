# Specialized magic — three independent capability evaluations

2026-11-25. Atlas #410. Same 11-domain editorial rubric as previous batches, current Minecraft 1.21.1 NeoForge integration base a98eadfbd625260f8d2c984cd51844ee76f198af. Native capabilities with explicit known TMM boundaries, not inferred runtime acceptance.

## Evidence

- [Forbidden & Arcanus 2.6.1](forbidden-arcanus.md), active SHA512 012a1d5c779a0173a5c76ea79f429486753f040dfe59739bc45fd0817182d705a80110032f024757f0ac51a9bd10c546a11a730899aa9adbf7251a5040472724. Prior exact artifact and [aligned source](https://github.com/stal111/Forbidden-Arcanus/tree/f62af610d550e0d033f6c5cd166e40062638c44b).
- [Totemic 0.12.18](totemic.md), active SHA512 4b3c4a6c926b8bf203b5593c558264b096f870ecfe74e6b6dbeef850825c9814a3b9a2d9f03c3a91680042dd684574f0259280210b9173750c921ff733e94896. Prior exact artifact and targeted binary inspection. Extended Totems #749 not credited.
- [Reliquary 2.0.85.1599](reliquary-reincarnations.md), active SHA512 75e4034ffff03d85ac4647fef6802e8921034de55baa650c32b7826f6eba4406b65fedcf4a8875d1ae8a3b850395a49fd0ad40587d60ffa1a1c62bb3e48c2e37. Prior exact artifact and current TMM source overrides. Active pins all matched earlier dossiers; current tree confirms 19 custom Alkahestry seed paths, native recipe disable files, cap override config and raw iron recipe. No fresh binary disassembly or game test.

## Scores

Scale 0 absent/no obtainable capability confirmed, 1 some assistance, 2 significant assistance, 3 independent, 4 superior, 5 transformative. Major 9–11 independent domains with personal/base breadth, Substantial 4–8, Minor 0–3. Score is an editorial domain assessment, not DPS or relative content counts. Totemic native passive functions scored separately from actual pack policy.

| Domain | F&A | Totemic | Reliquary |
| --- | ---: | ---: | ---: |
| Combat | 3 | 2 | 4 |
| Protection | 4 | 4 | 4 |
| Survival | 4 | 3 | 4 |
| Gathering | 4 | 2 | 4 |
| Industry | 4 | 1 | 3 |
| Construction | 2 | 1 | 2 |
| Transportation | 2 | 3 | 4 |
| Logistics & storage | 2 | 0 | 4 |
| Life & agriculture | 3 | 4 | 5 |
| Death & recovery | 0 | 0 | 4 |
| Advancement | 5 | 3 | 2 |
| Total | 33 | 23 | 36 |
| Independent (>=3) | 7 | 5 | 9 |

## Forbidden & Arcanus — 33/55, 7 independent, Substantial

Combat3 Aureal Missile (5 personal Aureal), weapons/gavels, arrows and tagged Black Hole damage. Protection4 Draco/Tyr armor (native total defense30/37), modifiers and skull protections. Survival4 Eternal Stella removes damage and max-damage components, aquatic helmet conditional healing, fire resistance, tools; not repair by periodic mending. Gathering4 Demolishing plane mining, gavel eligible loot chance30%, Fiery smelt loot and specialized harvesting/capture. Industry4 Clibano two-lane combustion, residue recycling, four essence acquisition/transport and five-tier Forge with 22 native rituals. Clibano outputs one primary item, enhanced flames 1.5x/2.5x speed and typed residue chance, not guaranteed ore doubling. Construction2 Whirlwind/upwind and limited terrain transformations, no comprehensive builder. Transportation2 faster ladders, boats and short upwind lift, no sustained flight or gateways confirmed. Logistics2 typed Utrem Jar 10000, Quantum Injector essence transport, local magnetized pedestal pickup and sided machine I/O, no general item network. Life3 Arcane Bone Meal, Magical Farmland eligible crop loot doubling, Aurum/Edelwood trees. Death0 accessible player death save not confirmed: Soul Binding Crystal does implement a Soulbound item modifier (save and restore specific inventory slots), but the native tier4 ritual requires Divine Pact with no confirmed native survival acquisition in inspected data/loot/trades. No credit until actual route established; conditional death3/36 total/8 independent if obtainable, still Substantial. Advancement5 five Forge tiers, four upgrade rituals, two armor sets beyond netherite, modifiers and distinct essence economies; tier5 capacity not invented extra tier5 recipes.

Current TMM source also contains [tier4 Draconium Dust ritual](../../../kubejs/data/tmm/forbidden_arcanus/hephaestus_forge/ritual/draconium_dust.json): Elder Dragon Bone center, four Obsidiansteel, 2000 Aureal, 2000 Blood, 500 XP, 20 Souls -> four Draconium Dust. Pack-specific extension not needed for native score; do not interpret source as effective recipe or unapproved gate bypass. Eternal Stella tier3 requires diamond, three XPetrified Orbs, Stellarite, 82 Aureal, one Soul, 1000 Blood; no enhancer. Forge tier5 capacity 20000/1000/100000/7500 (A/S/B/XP). Personal Aureal default100, base regen0. Enhancer eligibility/debit mismatch remains documented.

## Totemic — 23/55, 5 independent, Substantial (native)

Combat2 War Dance, Baykok bow, strength effects; no broad direct damage spell arsenal. Protection4 native bat/blaze/cow/ocelot carvings, Sun Dance absorption, Medicine Bag; Survival3 passive buffs, water breathing, regeneration, curing. Gathering2 buffalo haste, animal products, not independent mining. Industry1 instrument progression, hide/wood conversion and flute infusion, no general production system. Construction1 totem carving and ritual site, not building magic. Transport3 speed/jump/climbing/slow fall, not true flight. Logistics0 Pan's Flute attracts entities but no storage/routing. Life4 Rite of Spring, Zaphkiel Waltz, Hymn of Maturity, Cedar, Buffalo/Eagle conversion. Death0 Tipi sleep and living healing not resurrection. Advancement3 instrument/material stages, music performance, Baykok, no permanent player progression tree. 14 native ceremonies, 12 functional passive carvings, six instrument families. Totemic native score includes passive carvings and portable Medicine Bag; TMM ritual-first policy intentionally restricts these, so don't promise native passive score as effective pack breadth. Illustrative ceremony-only overlay 19/55 (2 independent, Minor): Combat2 Protection2 Survival2 Gathering2 Industry1 Construction1 Transport2 Logistics0 Life4 Death0 Advancement3. This is a design-bound projection, not a measured config or changed policy; exact enabled carvings/ceremonies/gear and ritual behavior remain to be validated. Extended Totems ideas #749 are separate.

## Reliquary — 36/55, 9 independent, threshold Major by formal breadth

Combat4 Magicbane enchantment-level scaling, Mercy Cross, charged fantasy combat artifacts, staff control. Protection4 species charms, Witherless Rose, Kraken Shell, Salamander Eye, Twilight Cloak, Angelic Feather; conditional costs and durability matter. Survival4 customized potions, Chalices, XP and gold repair, hunger/health exchanges. Gathering4 active pedestal melee/fishing/shear/milk and Fortune Coin collection, Harvest Rod; Destruction Catalyst destroys without drops, not mining. Industry3 independent seeded Alkahestry resource production, Apothecary alchemy, Altar of Light redstone-to-glowstone and local processing automation; no general refining ladder or free replication. Distinguish gathering raw drops from duplication/processing acquired seeds. Construction2 lighting, freezing, limited destruction/placement/water; no bulk builder. Transport4 Rending Gale charged flight and area support, Ender Staff, Angelic Feather; no free permanent flight. Logistics4 Void Tear 2 billion items of one type, portable Hero's Medallion XP, Infernal Chalice lava 500000mB, Fortune Coin item/XP collection into neighboring containers, pedestal local item/fluid handling. Not general digital storage or remote routing. Life5 Lilypad of Fertility, Harvest Rod automation, fertilizer/breeding potions and pedestal animal/shear/milk. Death4 Phoenix Down reverts to feather; Angelheart Vial consumable death prevention, not postmortem inventory restoration. Advancement2 independent artifact recipes, no formal school/innate stats or coherent tiers.

Threshold judgment: independent industry3 rests on actual seeded resource multiplication + alchemy + light production, not on hypothetical factories. If industry were scored2 under a stricter manufacturing definition, Reliquary becomes 35/55, 8 independent, Substantial. Its domain breadth qualifies it as Major under current formal rubric even though it is an artifact toolbox rather than a conventional magical school. Quest organization is a separate owner decision; no chapter change or new classification criterion proposed. This is not a claim Reliquary is as powerful as 54-point Botania.

Active TMM source: 22 native duplication records and drain disabled via neoforge:false, 19 custom seed definitions (conditional modded items), cap9999, charge redstone8, glowstone dust48. Example [raw iron](../../../kubejs/data/tmm/recipe/alkahestry/minecraft__raw_iron.json) seed -> six total raw iron for 54 charge, not six extra; no Nether Star, Ancient Debris, Draconium, Replica or fuel recursion. Do not teach unrestricted native default duplication. Actual EMC values, loaded recipes, capture gates, flight and enchantment policy separately owned. Native artifact score unaffected by this specific bounded economy; no pack approvals inferred.

## Handoff

Three scores verified against existing native dossiers and all live pins; explicit Totemic policy and F&A survival-acquisition limit; Reliquary sensitivity recorded. Separate evidence-only PR avoids shared index collision and retains prior 29-axis dossiers. No new JAR audit, runtime acceptance, balance/config/quest edits, build or chapter classification change. Next infrastructure batch Embers, Arcane Archives, Aura Cascade.
