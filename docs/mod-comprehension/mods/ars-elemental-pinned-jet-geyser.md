# Ars Elemental 0.7.10.4 — delayed Water Jet and directional Geyser

Exact artifact/hash [receipt](ars-elemental-pinned-guide-urn.md). Direct EffectWaterJet/EntityWaterJet, EffectGeyser/EntityGeyser/EntityLavaGeyser, ModEntities and recipe/damage tags; native IDamageEffect recipient in earlier companions. No world/combat/visual tests. **33of39 glyph recipes audited**, six other glyphs and remaining major addon systems open.

## Acquire and choose a setup

Search Worn Notebook **Water Jet / Create Geyser**, learn with [Scribes Table](ars-nouveau-pinned-guide-glyphs.md). Recipe inputs consumed; mana defaults before other spell parts/modifiers:

| Recipe/output `ars_elemental:glyph_…` | Tier / XP / default mana | Inputs |
| --- | --- | --- |
|water_jet|3 /160 /80|Water Essence, Manipulation Essence, Prismarine Shard, Wilden Spike, Breeze Rod|
|geyser|2 /55 /40|Fire Essence, Water Essence, Magma Block, Wind Charge|

Water Jet creates delayed aimed damage markers around a hit location; Split adds jets, Randomize distributes them. Geyser creates a temporary directional push/Soaked column on an anchor block; Sensitive permits nonupward orientation, Fire focus switches to burning variant. Neither requires a fluid tank, placed Source-consuming block entity or renewable water supply in these source methods. Their cost is ordinary spell casting, not an invented per-tick Source debit.

## Water Jet: initial acquisition versus delayed firing

Water-school glyph uses hit entity's eye position or ordinary hit location as impact. Query **living/alive/not-shooter** in **block-position AABB inflated10** and sort by squared distance to impact. No explicit ally, following-filter or line-of-sight exclusion here. If none, server splash and return. Compatible Split/Randomize/Amplify; Split's cost override **40**. Config base damage **4+1×ampMultiplier**.

Create **1+Split count** origins evenly around radius**1.5** circle at impactY+1. Without Randomize every jet records **nearest target's entity ID**; with randomized stats use **i modulo target count**, a deterministic round-robin after sorting—not random selection of independent targets. Construct one EntityWaterJet marker carrying lists, damage, stats, context and resolver. The glyph body has no explicit client early-return; actual native resolver dispatch remains a side-acceptance boundary.

Marker tickTimer starts0; server **when tickTimer≥25** fires and discards, then increments timer. It resolves each saved entity ID in current level, requires alive LivingEntity and **squared distance to that jet origin≤144** (12blocks), else splash. It does **not retarget** another entity if original died/moved out, or require target remain within the original10-box. Then native attemptDamage applies current eligibility/events to saved target; beam particles are spawned **without checking damage result**. Visual beam therefore does not prove successful hurt.

No block ray trace/nearest-obstruction check occurs in fire; target eye position supplies visual endpoint at firing time. IDamageEffect still rejects dead/allied targets and respects its event/hurt outcomes. An allied nearest target can occupy recorded selection and later fail damage; the selector does not automatically choose next hostile. This source path is not a blanket claim/protection/PvP PASS.

Packaged `minecraft:bypasses_armor` includes optional `ars_elemental:water_jet`; also magic/Water damage tags. That supports the armor-bypass guide claim for these data contributions, not bypassing every damage immunity or resistance. Nonplayer source builder substitutes native fake player. No spell remainder is resolved along every intermediate beam particle—these are delayed damage requests, not a moving native spell projectile.

Marker type registers **fireImmune and noSave**. Complex spawn data sends origin list for client particles, not full server target/context state. NBT helper saves only Timer, not context/targets/origins; noSave means ordinary reload persistence is not promised by having that helper. No recurring Source or mana removal occurs in marker tick.

## Geyser: anchor and orientation

Water+Fire-school effect server-only. Height/length **`max(0,4+2×ampMultiplier)`**. Start from hit block position. For block hit, take clicked face; if notUP shift anchor one block along it, then **unless Sensitive set directionUP**. Search backward opposite final direction through air, bounded by roughly twice height using source attempts counter. Entity hit uses entity.getOnPos; generic hit uses block above. If final anchor is still air, **no entity is created**. This is a nonair anchor requirement, not proof every support face is solid or allowed by a claim.

Base duration config **5seconds**. Ordinary water geyser adds **`(int)(1second×durationMultiplier×20)`**; **Fire-focus lava branch uses base only**, so Extend Time/Duration Down do not extend that branch through this expression. Compatible Sensitive/AoE/Amplify/Dampen/Extend Time/Duration Down. Focus check follows [audited focus helpers](ars-elemental-pinned-foci.md), not only greater Fire Focus.

Geyser entity centers horizontally on block, stores length, AoE-derived value **1+aoeMultiplier**, facing and duration. Server applies pushes each tick, increments age, discards at age≥duration. No owner/filter/alliance selection is stored, no explicit block-obstruction clipping within the push path. Temporary movement stream is not a physical placed fluid column.

## Push and status recipients

UP dimension request is **width `(1+AoE)×1.5`**, height=calculated length; query alive Entity in bounding box. LivingEntity gets **Soaked40ticks/amplifier0**. Below top, set vertical velocity **0.4**, retain horizontal components, mark impulse/update and reset fallDistance0. Native lingering/wall spell entities also move directly just above top before velocity update. It affects caster/allies/loose entities meeting the query, not just enemies.

Other directions query bounding box expanded toward direction×length, apply LivingEntity status, then set selected directional velocity component **±0.4**, retain others and reset fall distance. The query/dimension handling is a box, not exact cylindrical particle shape. Directional placement/client synchronization and wall behavior need runtime acceptance; the graphics do not establish collision geometry.

**Lava variant overrides applyEffect** to add **20 fire ticks every included server tick** instead of applying Soaked. It does not invoke base Soaked in this override. Inherits pushes and expiry; no direct periodic damage method in that override, although resulting fire can damage through engine rules. Both geyser types register fireImmune/noSave; NBT fields exist but are not proof of normal save persistence. No explicit Source draw or repeated spell payment is in these entity methods.

## Acceptance and remaining coverage

Future explicit acceptance: recipes/config/payment, no targets/allied closest/moving/dead targets, Round-robin Split, delay/12-distance boundary, beam versus damage/armor tags, side dispatch/noSave; Geyser anchor/face/Sensitive/backtracking, actual extent/collisions, Fire lifetime not extended, caster/allies/status/fire and lingering-spell movement. Six glyphs remain: Bubble Shield, Charm, Conjure Terrain, Life Link, Phantom Grasp, Spike; plus12tomes/helper/device/gear/worldgen and final walkthrough/pilot review. Addon remains researching; no runtime/config/quest changes.
