# Ars Elemental 0.7.10.4 — focus-empowered native glyphs

Exact binary/hash: [guide receipt](ars-elemental-pinned-guide-urn.md); [focus acquisition/equip/discount](ars-elemental-pinned-foci.md). Direct GlyphEvents, CompatUtils, Cut/Grow/Flare/Evaporate mixins, HeadCutRecipe, EntityMagnetSpell, MagicFireEffect/FrozenEffect and relevant DamageEvents handlers, plus five packaged head-cut recipes. No runtime tests. This closes the native-glyph modification slice; addon glyphs and summon replacements remain separate unfinished routes.

## Access and controlled use

Worn Notebook → **Spell Casting → school Focus**, then the related native glyph pages. Java `AEDocumentation.postInit` appends addon text to Ignite, Gravity, Freeze, Launch, Grow and Cut. Learn the native glyphs using [Scribes Table](ars-nouveau-pinned-guide-glyphs.md), then equip a single chosen focus and construct the combinations below in the spellbook editor. These are modifications of learned native glyphs, not six extra addon unlock recipes.

`GlyphEvents.empowerGlyphs` is controlled by binary-default-true `glyph_empower`. Entity branch requires a LivingEntity hit in ServerLevel. School checks are **not greater-focus-only**: `CompatUtils.*Check` accepts resolver.hasFocus(matching lesser focus) **or** selected focus schools from the unwrapped caster. Held/worn focus selection and school-providing casters therefore matter; [focus companion](ars-elemental-pinned-foci.md) scopes bundled Sauce selection. Some separate mixins/handlers do not check this config, so disabling it is not proof every change below disappears.

| Focus / spell action | Actual pre-resolve behavior |
| --- | --- |
|Fire + Ignite on living target|Add Magic Fire for200 ticks; normal Ignite resolution is not canceled by this branch. Effect source passed is the target itself.|
|Water + Freeze on another living target|Mark powder-snow state; add `(int)(60×ampMultiplier)` frozen ticks. A normal Skeleton starts freeze conversion. When fully frozen and canFreeze, force Frozen for60 ticks with hidden particles/icon flags.|
|Water + Conjure Water followed by Freeze on block|Use GlyphEffectUtil.placeBlocks to place **Ice** and cancel Conjure Water resolution. This is the checked following-effect condition, not merely having Freeze elsewhere in a different spell.|
|Air + Launch with nonzero duration multiplier|Apply Levitation for `50×(1+(int)durationMultiplier)` ticks, amplifier `(int)ampMultiplier/2`; trigger addon Levitate advancement for real ServerPlayer shooter. This adds a status; branch does not cancel ordinary Launch.|
|Earth + Grow against undead with Player shooter|Attempt magic damage `3+2×ampMultiplier`. If dead/dying, independent20% roll can place addon ground blossom above eligible underfoot block with air above; see below.|
|Earth + Gravity augmented Sensitive|Spawn magnetic field, cancel ordinary Gravity. Entity hits track the living target; block hits anchor the field at hit location.|

## Burn and freezing are event-driven statuses

MagicFireEffect itself has no periodic damage method. `FlareMixin.canDamage` broadens native Flare eligibility to targets with Magic Fire. `DamageEvents.changeDamageType` substitutes addon MAGIC_FIRE damage when a fire-tagged **spell** hits a Fire Resistance target that also has Magic Fire; copies luck if original source is SpellDamageSource. This is not unconditional removal of all fire immunity.

For magic-tagged incoming damage to a Magic Fire target, the post-reduction hook adds back **half the amount recorded as armor reduction**. Earth-tagged incoming damage against Magic Fire is multiplied by **0.85**. These are scoped event formulas, not a universal +50% magic multiplier.

FrozenEffect is a marker class. DamageEvents cancels healing while Frozen; fire-school tagged incoming damage multiplies current amount by **1.5** and removes Frozen. A separate player/mage target branch gives Water-focused freezing-tag damage ×1.25 when percent frozen>0.75. Do not equate the marker with an unconditional AI/movement shutdown: this class has no such implementation. Actual freezing/conversion/immunity and combined listener order remain acceptance work.

## Earth: undead Grow, blossoms and magnetic fields

GrowMixin makes native Grow implement IDamageEffect, allowing damage only to a living, positive-health, non-allied undead target when Earth check passes. GlyphEvents uses the attemptDamage endpoint, then tests death. Its20% blossom roll additionally requires underfoot **Moss Block, dirt-tag or leaves-tag**, and air above. Output is **`ars_elemental:spore_blossom_up`**, not the vanilla hanging Spore Blossom used by the Spore Thread recipe. This path calls setBlockAndUpdate directly; no extra claim check appears in this placement branch. Do not claim all protection mods necessarily intercept it.

`EntityMagnetSpell.createMagnet` stores owner, resolver, following filters, AoE and duration stats. Field query is a block-position AABB inflated by **`3+aoeMultiplier`**. Every fifth server entity-age tick, eligible entities at vector distance≥1 get velocity **`(oldVelocity + normalizedVectorTowardField)×0.5`**, then hurtMarked=true. Ignores owner, familiars, allied entities, lingering spells, tracked target and applicable following filters. The field follows its tracked living target's position; expiration accessor returns **`70+(int)(durationMultiplier×200)`** ticks; effective parent tick expiration remains an acceptance boundary. It supplies attraction, not damage or storage. The setter tracks only LivingEntity, and block hit uses no tracked entity.

## Air Cut and native head drops

CutMixin wraps native EffectCut.attemptDamage: Air check substitutes addon **CUT damage source**, preserving the original damage amount and passing the caster or native fake-player helper. It does not itself guarantee damage or a head. `DamageEvents.vorpalCut(SpellDamageEvent.Post)` needs a LivingEntity target with health≤0 and that CUT source. For a Player it builds a profile-bearing Player Head with threshold20. Otherwise it takes the **first matching** HeadCutRecipe entity ID. Recipes are lookup data (`matches=false`, crafting assembly empty), not crafting-grid recipes.

| Packaged `head_cut/` recipe suffix | Entity | Output | Threshold |
| --- | --- | --- | ---: |
|behead_creeper|minecraft:creeper|minecraft:creeper_head|10|
|behead_dragon|minecraft:ender_dragon|minecraft:dragon_head|20|
|behead_skeleton|minecraft:skeleton|minecraft:skeleton_skull|10|
|behead_wither_skeleton|minecraft:wither_skeleton|minecraft:wither_skeleton_skull|5|
|behead_zombie|minecraft:zombie|minecraft:zombie_head|10|

Roll uses `nextInt(100) <= threshold`, so normal thresholds5/10/20 mean **6%/11%/21% per attempt**, not the nominal5/10/20%. Luck is `min(3, SpellDamageSource.getLuckLevel())`; loop starts at−1 and stops before luck, allowing normally **1+luck attempts**, stopping after the first successful drop. For nonnegative luck L, success probability is `1−(1−p)^(1+min(3,L))`; no guaranteed head or multiple successful heads from one handler. It directly casts CUT source to SpellDamageSource; custom non-spell CUT sources are not proven safe. Native boss-part event delivery and actual Ender Dragon drops still need runtime acceptance.

## Other modified combinations and boundaries

With Fire and a following Evaporate effect, player Ignite over its computed AoE/Pierce ice/snow positions sets blocks to **Air** and cancels normal Ignite. Without that conjunction, this branch invokes the block's `playerDestroy(...,ItemStack.EMPTY)` and cancels Ignite; that callback alone is not proof the block is removed. No explicit claim check is in this loop. Separate EvaporateMixin changes **Mud→Clay** when native `destroyRespectsClaim` passes, cancels that evaporate call and requires no focus test in its body.

Entity Evaporate removes native Soaked without a school condition inside the enabled GlyphEvents handler. Entity Cut also removes both ends of addon Life Link if its effect instance carries owner/target references, without requiring Air. These are auxiliary modifications, not proof the whole Life Link glyph route is audited.

Future explicit acceptance: configured empowering toggle versus separate mixins; lesser/greater/held/resolver focus checks; burn/fire resistance/armor event order; Freeze amp and marker/heal/fire interactions; claim/placement permissions; target/allies/following filters and field expiration; Launch duration; head-drop statistical/kill-source behavior. Pilot35–37 provides item possession milestones only. No new quest, balance change or protection override is authorized by this report.
