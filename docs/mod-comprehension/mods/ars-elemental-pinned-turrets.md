# Ars Elemental 0.7.10.4 — five infused turrets and native form extensions

Exact addon SHA1 `562f640916aa6f5c83b3b3ee2384ca828b870e01`, native Ars5.13.5 SHA1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; [artifact](ars-elemental-pinned-guide-urn.md). ModItems/ModTiles, ArsNouveauRegistry static behavior registrations including inners, AEDocumentation and five exact recipes/lang; bundled Sauce0.0.61.100 FocusEnhancedSpellTurret/Tile including FocusTurretSpellResolver, TurretAccessor, ModRegistry; native Basic/Rotating turret block/tile and [native turret audit](ars-nouveau-pinned-turrets.md). Global Sauce selected version still pending. No scribing/automation/combat acceptance.

## Upgrade, program, aim and power

Worn Notebook **Crafting → Elemental Infused Turrets**, related **Basic Spell Turret** entry, native automation/Source references; JEI **Imbuement Chamber** shows all five. Consume one native **Enchanted Spell Turret** (`ars_nouveau:spell_turret`, not basic_spell_turret or rotating_spell_turret) and5000Source. Reusable pedestal catalysts are three matching Essences plus focus:

| Output `ars_elemental:` | Essences×3 | Focus catalyst |
| --- | --- | --- |
|`fire_turret`|Fire|`ars_elemental:fire_focus`|
|`water_turret`|Water|`ars_elemental:water_focus`|
|`air_turret`|Air|`ars_elemental:air_focus`|
|`earth_turret`|Earth|`ars_elemental:earth_focus`|
|`manipulation_turret`|Manipulation|`ars_nouveau:shapers_focus`|

Elemental four require greater focus access ([Mark/focus recipes](ars-elemental-pinned-foci.md)); focus catalyst is reusable, not installed as a separate inventory slot. Choose a school for intended spell, not a mandatory five-turret ladder.

Place and main-hand program using registered caster item's nonempty supported spell; native programming records player UUID for fake-player attribution, consumes no casting item. Supply Source within lookup10 or link extractable provider through native workflow. Dominion Wand stored-target first callback uses inherited RotatingTurretTile.aim to set needed pitch/yaw; native tick interpolates rotation. Basic redstone rising edge schedules shot4ticks later, then clear/re-rise for another; constant power is not automatic timer operation. [Flashjack scout](ars-elemental-pinned-flashjack.md) can control it because tile extends RotatingTurretTile; scout's direct shootSpell calls still pay Source.

Sauce FOCUS_TURRET block entity is initially built with empty block list; **addon ModTiles.addBlocksToTiles registers all five blocks through BlockEntityTypeAddBlocksEvent**. Do not report the initial empty list as proof these turrets cannot retain a tile. Block newBlockEntity sets its declared school; save/load persists school along with native spell/UUID/rotation/source-link fields. No fresh loader or reload PASS is claimed.

## Whole-spell cost and virtual focus

getManaCost inspects **any part** of stored spell with school.isPartOfSchool. If any matches: int(rawSpellCost×0.35); otherwise int(rawSpellCost×0.6). This replaces native cost, not an additional multiplication after Enchanted turret's integer raw/2. Example raw100 gives matching35, nonmatching60, native Enchanted50. Infusing is therefore not a universal improvement for nonmatching spells. Part identity/school classification, not tooltip color or majority of spell, selects branch; cost may round to0 for small totals.

Positive cost: linked provider's selected side capability if configured; invalid capability clears link/returns, insufficient linked provider returns without nearby fallback; simulate then extract (actual return unchecked). Otherwise SourceUtil.takeSourceMultipleWithParticles(position,world,10,cost), null returns. After payment: animation, dispense position, fake player from programmed UUID or shared default, then FocusTurretSpellResolver/TileCaster(TURRET), then registered cast-type dispatch. Unsupported/canceled/rejected effect or failed projectile spawn has no Source refund here. Generic personal mana spend is skipped for TURRET; cost is jar Source, not owner mana or focus durability.

Resolver hasFocus(Item/Stack): an ISchoolFocus matches if its school is **exactly the turret's school**, regardless of lesser/greater class; native Shaper's Focus matches Manipulation; other items delegate to superclass. Child getNewResolver returns same school-specific resolver. This permits school-dependent spell branches without equipping a player's focus on the machine. It does not automatically grant every worn-focus event/passive: player-only events, target conditions and item-class distinctions outside hasFocus remain their own gates. See [empowered native glyphs](ars-elemental-pinned-empowering.md) for recipient conditions. No extra explicit universal damage modifier is added by this turret class.

## Addon extends both ordinary and adjustable form maps

ArsNouveauRegistry static initializer adds **Homing Projectile and Arc** to native BasicSpellTurret.TURRET_BEHAVIOR_MAP and RotatingSpellTurret.ROT_TURRET_BEHAVIOR_MAP, alongside native Touch/Projectile. Thus native audit's two-form inventory describes base Ars only; installed Elemental adds these two forms even to native turrets, not just infused variants.

Homing creates native EntityHomingProjectileSpell, owner=fake player, computed cast stats/speed and basicIgnores, then adds entity. Basic uses block facing; rotating uses normalized tile shoot angle. Arc creates **ordinary EntityProjectileSpell with gravity=true**, not custom EntityGravityProjectile; owner/fake player, computed Arc speed, then adds. Basic Arc vector adds0.25 to facingY; adjustable Arc uses normalized shoot angle without that addition. Therefore not every custom player-cast Arc entity/lifetime behavior is inherited from the name. [Homing](ars-elemental-pinned-homing.md) and [Arc](ars-elemental-pinned-arc.md) distinguish native/custom recipients.

Infused tile calls adjustable map but supplies **block FACING** as direction argument; native rotating tile supplies nearest ordered rotation direction. Projectile/Homing/Arc adjustable behaviors read tile angle directly. Native adjustable Touch uses direction for adjacent-block living-entity query while aimVec is rotation-based; a changed aim may therefore disagree with block-facing entity query. Test actual Touch selection rather than promising identical aim semantics for every form. No additional arbitrary form becomes supported merely because a player book can cast it.

## Quest / unrun acceptance handoff

Teach native controlled turret/Source first, then optional matching-school infusion: JEI catalyst recipe → program known simple spell → aim safe target → pulse once → observe actual target change and Source debit. Inventory possession is proxy; operation acknowledgement must be manual unless real detector exists later. Modest XP, mounting/target-practice blocks and limited workshop supplies; no free Mark/greater focus gate bypass.

Unrun cases: correct Enchanted reagent/reusable focus; all five block/tile types survive reload; raw100 matching35/nonmatching60 versus native50; any-one-part school selection, rounding0; no linked fallback/failed capability; fake-player permissions; school hasFocus versus player-only passive; four supported forms/basic versus adjustable Arc; Touch facing/aim disagreement; child resolver school; Flashjack same payment/direct fire. No runtime fixes. Gear/worldgen/dependency/pilot/final review still prevent whole-addon source-complete status.
