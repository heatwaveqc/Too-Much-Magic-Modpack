# Ars Elemental 0.7.10.4 — Advanced Prism, lenses and Mirror boundary

Exact addon SHA1 `562f640916aa6f5c83b3b3ee2384ca828b870e01`; [receipt](ars-elemental-pinned-guide-urn.md). Addon prism block/tile/seven lens classes/SpellMirror, registry/config/documentation, packaged recipes/loot/lang; embedded Sauce0.0.61.100 SpellPrismLens; native Ars5.13.5 SpellPrismBlock/EntityProjectileSpell/ICasterTool/SpellResolver/SourceUtil/StandardSpellValidator inspected. Global selected Sauce version still requires all-addon reconciliation. javap confirms Mirror method descriptors and native Pierce default40. No actual projectile routing/scribing/Source tests.

## Craft, aim and install

Read Worn Notebook **Crafting → Advanced Spell Prism**, its three text/crafting groups, related native **Spell Prism** entry, and **Spell Mirror** entry. JEI crafting/Imbuement/Apparatus pages are the recipe reference. Advanced Prism grid `QSQ / SPS / QSQ`: center native Spell Prism, corners four Quartz items, cardinal four `#c:gems/source`, output1. Place it, select destination block with Dominion Wand and complete the prism endpoint to aim. Tile's first-connection callback delegates native IWandable first, then uses stored position to calculate pitch/yaw, save and show beam/message. No custom distance/ownership/dimension guard appears in aim itself; do not promise remote chunk access.

Right-click Advanced Prism with any SpellPrismLens item: server splits1 from hand even without a creative exemption, drops prior lens through clear callback, stores new lens and updates. Use wand clear/shift interaction described by Notebook to remove lens; tile emits ItemEntity and clears reference. Lens/rotation save in tile NBT. Packaged block loot drops only the prism item with explosion condition, no copy-components/installed-lens entry; remove lens before dismantling rather than assuming block loot preserves it. Tilt/rotation is tile state, no runtime UI guarantee inferred.

Five Imbuement recipes consume **one `#c:gems/quartz`** and2000Source, with reusable pedestal catalysts **Manipulation Essence + matching glyph item**:

| Lens output `ars_elemental:` | Glyph catalyst |
| --- | --- |
|`acceleration_prism_lens`|`ars_nouveau:glyph_accelerate`|
|`deceleration_prism_lens`|`ars_nouveau:glyph_decelerate`|
|`arc_prism_lens`|`ars_elemental:glyph_arc_projectile`|
|`homing_prism_lens`|`ars_elemental:glyph_homing_projectile`|
|`piercing_prism_lens`|`ars_nouveau:glyph_pierce`|

Chaining Apparatus recipe consumes quartz-tag reagent + Manipulation Essence + Source Gem Block + Blank Parchment, Source0, result1 `ars_elemental:chaining_prism_lens`, keepNbtOfReagent=false. Rainbow lens `ars_elemental:rainbow_prism_lens` is registered unconditionally despite deprecated class, but has no packaged lens crafting recipe in this set; do not invent a normal survival acquisition or require it for progression.

## Incoming projectile and lens operation

Native EntityProjectileSpell's prismatic-block hit event can cancel redirect. Normal dispatch is IPrismaticBlock.onHit(Level,BlockState,BlockPos,projectile). Advanced Prism's matching override runs server-side: move projectile to block center+0.5×shoot-angle, increment prismRedirect, feedback advancement at≥3 within10, discard if resolver missing; if installed lens canConvert use it, otherwise use default lens; update observers. Failed eligibility does **not** eat the projectile; it gets ordinary aimed redirection. Advancement is routing feedback, not proof of a particular lens operation.

Embedded Sauce default lens computes acceleration count at spell index0 minus0.5×deceleration count; speed=max(0.1,0.55+0.1×min(8,acceleration)), shoot along angle/no inaccuracy. Thus no-lens Advanced Prism recalculates speed rather than preserving incoming magnitude. Native ordinary prism preserves current speed magnitude with minimum0.1.

| Lens | Exact additional behavior / cost |
| --- | --- |
|Acceleration/Deceleration|Always eligible; inserts corresponding augment at resolver.spell index1, then base shoot recalculates speed. No Source payment/cap here. It mutates resolver.spell, not explicitly spellContext's spell in these methods. Other runtime stats/context consistency remain a test boundary.|
|Arc|Eligible when incoming isNoGravity; base shoot then setGravity(true) on same projectile. It does not replace entity with addon EntityGravityProjectile or add Arc form to recipe; “Arc conversion” here means gravity-enabled projectile, not every custom Arc behavior.|
|Homing|Only incoming not already EntityHomingProjectileSpell. Constructs native Homing with same resolver, fresh basicIgnores (not prior custom ignore list), copies pierceLeft/prismRedirect/age/position, shoots/adds then discards old projectile. No charge in method. Other state such as prior homing target/hit history is not explicitly copied; failed addition still discards original. [Targeting/lifetime recipient](ars-elemental-pinned-homing.md).|
|Piercing|Requires pierceLeft<configured pierceLensLimit10 and native hasSourceNearby(range6,AugmentPierce casting cost). Base shoot, ++pierceLeft, then takeSourceMultiple(range6,cost), return ignored. Native default glyph cost40 (config may override); payment occurs **after** effect, no rollback on extraction failure. Limit counts current pierceLeft, not a separate lifetime lens-use counter.|
|Rainbow|Only not already rainbow: modifies serialized particle-color type to Rainbow, then base shoot. No acquisition recipe established; no gameplay bonus inferred.|

Native hasSourceNearby aggregates simulated provider removals; actual helper is a later call, so eligibility is not an atomic reservation. No lens durability/consumption happens on each redirected shot in these methods.

## Chaining: appended fragment, strict size and full combined payment

Place lens on Scribes Table; use Spellbook or Spell Parchment through inherited ICasterTool.onScribe. Registered SPELL_CASTER provider reads component. Native scribe copies supplied nonempty spell, name/flavor/particles; this subclass does not strip its cast method. To append a fragment, supply an effect/augment fragment rather than blindly copying a complete Projectile→effect spell. Combined crafting validator permits at most one AbstractCastMethod and checks augment/occurrence/combination policies; it does not require a first Method for the fragment itself. A complete second cast-method recipe fails the combined maximum-one-method check. Lens tooltips show added glyphs; Manipulation Essence hides display through inherited scribe path rather than crafting a new ability.

canConvert appends all stored glyphs to incoming resolver.spell, then requires combined recipe size **strictly less than chainLensLimit15** (14 accepted by this count,15 rejected), no crafting-validation errors and Source simulation within10 for **combined raw spell.getCost**, not appended-cost delta. When accepted it first redirects at base speed, appends all glyphs, updates both resolver.spellContext.withSpell and resolver.spell, computes native getResolveCost (whole context base less caster discounts, Pre event, clamp0), and attempts range10 withdrawal with particles. Return ignored: mutation precedes actual extraction, and simulated raw-cost gate can differ from discounted/event-adjusted debit. No added up-front player-book mana payment in this method, but original cast was separately paid; no guarantee against later impact costs/target rejection. Test real fragment scribing and paid operation before questing it as proven working automation.

## Spell Mirror: pinned overload does not implement reflection hook

Apparatus consumes one `#c:storage_blocks/source` reagent, pedestals two `#c:logs/archwood`, two quartz-tag gems and two Gold Ingots, Source0, outputs **two** `ars_elemental:spell_mirror`. It needs sturdy support behind FACING; loss of support returns Air. It has thin face-aligned shape.

Its custom source body reflects normal velocity component×−0.9, but declares **onHit(ServerLevel,BlockPos,EntityProjectileSpell)**. Native IPrismaticBlock dispatch requires **onHit(Level,BlockState,BlockPos,EntityProjectileSpell)**, inherited from SpellPrismBlock. javap confirms there is no matching Mirror override/bridge. Therefore ordinary native projectile dispatch reaches inherited directional prism routing, not the custom reflection overload: facing-direction re-shoot/reposition with retained speed magnitude. Do not promise angle-of-incidence reflection from prose or the unused overload. Other explicit callers could invoke it, but none is established here. This is a research finding, not authorization to patch runtime.

## Quest / unrun acceptance handoff

Teach basic aimed redirection first, then optional speed/gravity/Homing/Pierce/Chaining lens choices, not every recipe as a mandatory ladder. Name Advanced Prism Notebook pages/JEI categories and Scribes Table for fragments; spell/glyph prerequisites remain native progression. Item acquisition is proxy; manual acknowledgement for routed shot/lens change/cost unless operation detector later implemented. Modest XP and prism-building blocks or limited workshop supplies; no guaranteed free high-tier glyph reward.

Positive/boundary tests: aim steep vertical/diagonal; replacement/extraction/reload/dismantle lens retention; cancellation/missing resolver; accelerated resolver.spell versus context; gravity lens versus true Arc entity; Homing custom-ignore/state loss/age; Piercing9→10/limit fallback/payment race; chaining14 versus15, full-method rejection/effect fragment, simulated raw versus actual discounted cost and lost reservation; Mirror ordinary facing route versus custom reflection expectation. No tests run. Remaining relay/turret/gear/worldgen/dependency/pilot routes keep addon researching.
