# Ars Elemental 0.7.10.4 — Conjure Terrain and Spike

Exact artifact/hash [receipt](ars-elemental-pinned-guide-urn.md). Direct glyphs/recipes/four terrain tags, DripstoneSpikeEntity/IceSpikeEntity/EnchantedDripstoneEntity, ModEntities; bundled Sauce GlyphEffectUtil placement helper; exact native Ars5.13.5 EnchantedFallingBlock/IDamageEffect. Conjure Terrain's switch assignments independently checked with javap where CFR printed ambiguous local scopes. No block-placement/combat tests. **35of39 glyph recipes audited**, four others plus remaining addon systems open.

## Learn and use

Worn Notebook **Conjure Terrain / Spike**; learn with [Scribes Table](ars-nouveau-pinned-guide-glyphs.md). Consumed inputs:

| Recipe/output `ars_elemental:glyph_…` | Tier / XP / default mana | Recipe inputs |
| --- | --- | --- |
|conjure_terrain|inherited1 /27 /20|Earth Essence, Dirt|
|spike|2 /55 /30|Pointed Dripstone, Netherite Ingot, Earth Essence|

Conjure Terrain places real terrain using spell mana, no block-stack reagent in its placement helper; choose legal cast method, target a block face and use the combinations below. Spike makes a temporary contact hazard if its support search succeeds, otherwise a falling entity. These are different branches, not a single guaranteed placed block or permanent defensive multiblock.

## Terrain choices and lookahead effects

Conjuration+Earth schools; compatible AoE/Pierce/Amplify/Randomize, own limits Amplify2/Randomize1. Selection reads **count of Amplify augments**, not total amplification stat from gear:

| Amplify count | Base terrain | Following recognized effect |
| --- | --- | --- |
|0|Dirt; Randomize chooses dirt tag|Conjure Water→Mud; Crush→Sand or randomized sand-tag member; Smelt leaves base choice|
|1|Cobblestone; Randomize chooses cobble tag|Crush→Sandstone or50% Red Sandstone if randomized; Smelt→Stone or randomized stone-tag member|
|2|Cobbled Deepslate|Crush same amplified Sandstone branch; Smelt→Deepslate|

Default/outside1/2 base branch uses Dirt/tag, but normal own Amplify limit2 applies. Code advances context while searching next **AbstractEffect**, skips non-effects; recognized combination is consumed rather than then cast normally. For unrecognized next effect, rewinds one context index. It does not search past every unrelated effect to find a distant Crush/Smelt. Smelt at0 is still consumed despite no material upgrade. Place helper then calls native Shaper's Focus block-spell propagation for each successful placement; that is a separate child path, not an unconditional second cast for every player.

All four block tags are fetched with **orElseThrow before selection**, so missing tags are not safely handled solely by random-member fallbacks. Packaged contributions: dirt **Dirt/Coarse Dirt/Podzol/Grass Block/Gravel**; cobble **Cobblestone only**; stone **Diorite/Andesite/Granite/Tuff/Calcite/Blackstone** (not plain Stone in its random list); sand **Sand/Red Sand**. Other datapacks can alter assembled membership. No claim of a guaranteed rare resource outside these exact lists.

Bundled GlyphEffectUtil.placeBlocks computes native AoE/Pierce positions, offsets toward clicked face unless hit isInside, then requires **destroyRespectsClaim**, replaceable existing state and world.isUnobstructed for caster collision context. Successful setBlockAndUpdate uses chosen default state. A passed recipe/grammar does not establish every placement succeeds in claims or occupied space. No per-placement inventory debit is present in this helper.

## Spike: supported hazard versus falling fallback

EffectSpike computes damage **8+2.5×ampMultiplier**. For entity hit, examine getOnPos and two below (i0..2) for a nonair block. On success it calls summonSpike with **original getOnPos**, not the found lower block, then adjusts X/Z to target's precise position. For block hit, scan i−1..2 (one above hit through two below) and pass the found pos. This is an **isAir test**, not a full solid-floor/protection test. If none, falling fallback starts at entity getOnPos.above2 or block hit pos.

Supported branch chooses **IceSpikeEntity if Water focus helper passes**, else DripstoneSpikeEntity; creates entity then optional precise X/Z adjustment. Schools Earth+Water. Compatible Amplify/Dampen/AoE/Pierce/Extend Time/Randomize/Fortune, own Amplify limit2. No explicit claim check appears in these creation methods; actual damage permissions still use native event/hurt endpoints.

### Supported entity recipient

DripstoneSpike stores caster/stats/context/resolver and base damage, with **pierceScale=1+0.25×Pierce count**, **aoeScale=1+0.25×(AoE multiplier−1)**. Dimensions request native type width×aoeScale and height×(aoeScale+pierceScale−0.5). This means ordinary no-AoE stat0 yields aoeScale0.75, not a mandatory width1 interpretation of the description.

Lifetime starts **20 ticks + `(int)(ExtendTimeConfig×durationMultiplier)`**, with config default10. There is **no×20** in this entity's extension formula, despite the generic config label being seconds. Each tick, before decrement, server damage check runs when lifeTicks **<16+durationMultiplier×config** and lifeTicks divisible5; every LivingEntity in bounding box is passed to native attemptDamage with **STALAGMITE** source. Remove after decrement<0. This is a short repeated-contact window, not a tested seconds-per-ExtendTime timer or unlimited damage per tick. Normal native damage eligibility/event/invulnerability still applies.

IceSpike's only inspected damage override adds **Slowness100 ticks/amplifier1** if base damage returns true. It does not freeze every entity merely because its model is ice. All four spike entity registrations set **fireImmune/noSave**, so NBT helpers do not promise normal reload persistence; supported NBT also does not serialize full stats/context/resolver.

## Falling spike recipient and damage paths

Fallback EnchantedDripstoneEntity uses downward Pointed Dripstone block state, **dropItem=false**, retained resolver/context/stats and Water-focus `icy` type choice. Glyph calls setHurtsEntities with **8+2.5×amp** per-distance amount and default maximum**40**, then native Shaper's Focus entity propagation. Falling icy class has no inspected Slowness override analogous to the supported IceSpike; don't promise that effect carries over.

Native EnchantedFallingBlock **causeFallDamage** computes `i=ceil(fallDistance−1)`, skips negative i, damages alive/noncreative/nonspectator entities in bounding box by **min(floor(i×perDistance),maxFallDamage)** with falling-block/Fallable source. A separate **onHitEntity projectile-impact** path uses speed, baseDamage and state bonus with a falling-stalactite source supplied by addon override. The40 cap belongs to the former formula, not proof every possible collision path is capped40. Both inherited endpoints must be distinguished from supported spike's repeated IDamageEffect calls.

## Acceptance and remaining coverage

Future explicit acceptance: tag presence/data overrides; Amplify count versus stat bonuses; recognized-effect consumption/context continuation; claim/replaceability/collision; entity support-position discrepancy; actual dimensions/tick duration/contact invulnerability; Water variant versus falling ice; collision/landing damage and no item drops/noSave. Four glyphs remain: Bubble Shield, Charm, Life Link, Phantom Grasp; plus12tomes/helper/device/gear/worldgen and final walkthrough/pilot review. Addon stays researching; no runtime/recipe/quest changes.
