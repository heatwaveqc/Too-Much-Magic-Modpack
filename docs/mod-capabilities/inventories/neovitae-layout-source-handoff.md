# Neo Vitae 1.1.53 — frozen-source structure layout discovery and remaining research

**Source identity:** [`breakinblocks/NeoVitae@2744123bb6e4f72c602178b23384be41956eef23`](https://github.com/breakinblocks/NeoVitae/tree/2744123bb6e4f72c602178b23384be41956eef23); matches declared MC 1.21.1 / mod 1.1.53 in the selected TMM pin. The recursive source tree exposes **6 generated altar-tier JSON files and 48 generated regular ritual-layout JSON files**. File paths and immutable links below were individually discovered; **this is a full filename/source pointer index, not a claim that all JSON contents, 3D placements or runtime behavior were audited**. Targeted source inspection confirms component arrays with relative `pos` coordinates and selector fields: altar `valid`, `upgrade`, optional flags; ritual `rune`.

## Six actual altar-tier layout files

- [`apprentice`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/altar_tier/apprentice.json)
- [`archmage`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/altar_tier/archmage.json)
- [`mage`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/altar_tier/mage.json)
- [`master`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/altar_tier/master.json)
- [`transcendent`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/altar_tier/transcendent.json)
- [`weak`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/altar_tier/weak.json)

Each altar file describes a component list with x/y/z positions relative to an anchor; `upgrade:true` and `optional:true` fields change how counts and recipe-tier requirements should be interpreted. **Do not use serialized component count as material cost**, since upper levels carry lower-tier positions and optional structural blocks. The Tier-1 Apprentice file, for example, lists Ara Vitae at (0,0,0) and eight rune offsets at y=-1. The full tier-5 structure contains optional pillar positions and tier-3 capstone selectors; use JSON and the renderer/validator for a real player placement diagram.

## 48 regular ritual-layout files

- [`armour_evolve`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/armour_evolve.json)
- [`butchering`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/butchering.json)
- [`celerity`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/celerity.json)
- [`condor`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/condor.json)
- [`containment`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/containment.json)
- [`crystallum_fractura`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/crystallum_fractura.json)
- [`deus_armour_evolve`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/deus_armour_evolve.json)
- [`deus_celerity`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/deus_celerity.json)
- [`deus_crystallum_fractura`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/deus_crystallum_fractura.json)
- [`deus_enchanted_vitae`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/deus_enchanted_vitae.json)
- [`deus_felling`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/deus_felling.json)
- [`deus_green_grove`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/deus_green_grove.json)
- [`deus_magnetism`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/deus_magnetism.json)
- [`deus_meteor`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/deus_meteor.json)
- [`deus_placer`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/deus_placer.json)
- [`deus_torment_nexus`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/deus_torment_nexus.json)
- [`deus_well_of_suffering`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/deus_well_of_suffering.json)
- [`downgrade`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/downgrade.json)
- [`enchanted_vitae`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/enchanted_vitae.json)
- [`expulsion`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/expulsion.json)
- [`feathered_knife`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/feathered_knife.json)
- [`felling`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/felling.json)
- [`forsaken_soul`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/forsaken_soul.json)
- [`full_stomach`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/full_stomach.json)
- [`green_grove`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/green_grove.json)
- [`grounding`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/grounding.json)
- [`harvest`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/harvest.json)
- [`hellforging`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/hellforging.json)
- [`lava`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/lava.json)
- [`magnetism`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/magnetism.json)
- [`meteor`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/meteor.json)
- [`penance`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/penance.json)
- [`phantom_bridge`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/phantom_bridge.json)
- [`placer`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/placer.json)
- [`pump`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/pump.json)
- [`regeneration`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/regeneration.json)
- [`shepherd`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/shepherd.json)
- [`simple_dungeon`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/simple_dungeon.json)
- [`speed`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/speed.json)
- [`sphere`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/sphere.json)
- [`standard_dungeon`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/standard_dungeon.json)
- [`suppression`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/suppression.json)
- [`torment_nexus`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/torment_nexus.json)
- [`upgrade_remove`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/upgrade_remove.json)
- [`water`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/water.json)
- [`well_of_suffering`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/well_of_suffering.json)
- [`yawning_void`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/yawning_void.json)
- [`zephyr`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/zephyr.json)

The **48 regular** layout files are not equivalent to **51** native registered rituals (three separate **imperfect** rituals exist). For example, [`zephyr.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/zephyr.json) explicitly contains Air rune positions at (±1,0,±1), (±2,0,0)/(0,0,±2) and (±3,0,0)/(0,0,±3), plus Water rune corners (±2,0,±2). [`simple_dungeon.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/ritual_layout/simple_dungeon.json) includes **raised y=1..4** vertical components, so assuming all rituals are flat is incorrect.

## The finite Neo Atlas research gap

1. **Geometry interpretation:** parse all 6+48 JSON component arrays into a tabulated player-placement crosswalk and check which positions/tags are mandatory versus optional. Link to actual `AltarMultiblockBuilder`, `RitualMultiblockBuilder` and validators. This is source research; native Minecraft gameplay regression is not required just because it uses blocks.
2. **Actual ritual effects:** crossjoin the 48 layouts with [46 generated ritual-stat records + two class-default dungeons](neovitae-resource-and-ritual-stats.md) and registered `Ritual*.java` classes. Record activation, cost timing, range, target eligibility, owner attribution, failure and top-end variants for the significant gameplay rites, including dungeon entry/return.
3. **Arrays and sigils:** [48 Array recipe JSONs](neovitae-array-recipes.md) and item/registration lists are complete for inputs, but effects still need an explicit Java-handler crosswalk for named **26 Array Effect items**, **18 registered Sigils**, meaningful equipment effects, and output/failure boundaries. This is not a demand to execute 48 client tests; source effect tracing is enough for Atlas closure.
4. **Critical quest dependency:** ensure the [40 proposed quest lessons](../mods/neovitae-quest-map.md) reference exact mandatory tier-transition and ritual geometry, actual effect targets and costs, and correct player/event/possession proof. Existing native `RitualEvent.Activated` includes the actor; machine craft events do **not**. The future FTB implementation must separately validate per-player credit.
5. **Not a source-research blocker:** effective TMM datapack overrides, binary/JAR parity, full-modpack launch, JEI UI screenshots, EMC/ProjectE and Mine Key survival gate safety, blood/diet/MXP integration and client positive/boundary QA. These are separate implementation and verification tracks, not an excuse to reopen finished source recipe catalogs.

**Status:** Neo Vitae has already completed its **472 exact generated recipe rows**, detailed tier and resource ledgers, 29-axis scope, and 40 acyclic proposed lesson IDs; its remaining Atlas work is **native layout/effect crosswalking, not more recipe harvesting**. No scripts, manifests, quests, gameplay configs or client/server tests were changed for this index.
