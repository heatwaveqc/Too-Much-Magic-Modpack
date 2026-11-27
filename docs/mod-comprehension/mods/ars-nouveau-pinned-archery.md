# Ars Nouveau pinned spell archery

Exact5.13.5 JAR SHA1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`. Direct SpellBow, SpellCrossbow, SpellArrow, FormSpellArrow, EntitySpellArrow, ItemsRegistry, Documentation and recipe/locale evidence; CFR0.152, javap confirms crossbow payment-before-useAmmo, unconditional isSpell=true on loaded stacks and charged-projectile clearing. [Parent](ars-nouveau.md), [inscription](ars-nouveau-pinned-spell-records.md), [personal casting](ars-nouveau-pinned-casting.md), [Imbuement/Apparatus](ars-nouveau-pinned-crafting.md). No actual weapon/enchantment/projectile/protection acceptance.

## Build, inscribe and choose ammunition

Notebook **Spell Casting -> Enchanter's Bow / Enchanter's Crossbow** (registered IDs `spell_bow`, `spell_crossbow`), including all three augment-arrow recipes. JEI effective ingredients/uses, **Crafting -> Scribe's Table -> Scribing** for inscription.

- Apparatus Bow or Crossbow reagent plus one Gold-block-tag, one Source-block-tag and one Manipulation Essence; one matching spell weapon, SourceCost0, keepNbtOfReagent=true.
- Put weapon on Scribes Table. Select an **effects/augments-only** spell in held Book/Parchment, then sneak-use it on table. Both reject any supplied AbstractCastMethod and prepend Projectile internally. A full Projectile spell is the wrong inscription source. Start with a harmless visible effect; don't use an explosive area spell for first base test.
- Ordinary arrows remain useful. Native shaped `spike_to_arrow` uses Wilden Spike above Stick above Feather for32vanilla Arrows. Imbuement accepts one `minecraft:arrows` center input and produces one augment arrow at source100; reusable pedestal catalysts are Source Gem, Air Essence, plus Diamond-gem-tag (Amplify), Wilden Horn (Split), or Wilden Spike (Pierce). Chamber operation/catalyst semantics and low-cost pull boundary are in crafting companion.
- Registered Amplify Arrow appends **two Amplify** parts to spell. Split/Pierce are FormSpellArrow, insert **two matching augments at index1** after form. Their native item modification changes spell recipe, not player's permanently learned glyphs. Availability via dungeon loot is an additional candidate in spell-record companion.

Both weapons implement discount equal to Projectile.getCastingCost, rather than percentage off entire spell. Actual expended cost still runs personal modifier/events. Native item constructors/registration don't declare max damage and custom firing bodies have no explicit weapon hurtAndBreak call; do not assert ordinary bow/crossbow durability economics solely from parent class names or the upgraded vanilla reagent.

## Bow operation and prepaid launch

Use draws if it finds ammo or has valid affordable spell (or infinite-material fallback); release applies arrow-loose event and requires draw power>=0.1. Ammo search checks held projectiles then inventory. It filters SpellArrow ammo out when **base stored spell** can't cast, but ordinary arrows can still be chosen. No-ammo valid affordable spell branch builds an EntitySpellArrow with base physical damage0; Power enchantment later adds damage in addArrow, so “zero physical damage” is only its initial base value.

On server release it computes ammo-use quantity, creates selected arrow object, then builds resolver for modified weapon spell. With augment-arrow item it rechecks canCast, debits mana before adding entity, and marks didCastSpell. With ordinary ammo, if resolver canCast it replaces arrow with spell arrow and debits; otherwise ordinary arrow can fly without spell. Special arrow modifiers live on initial arrow's resolver, while **payment resolver uses weapon spell**, not necessarily augmented recipe. Don't promise arrow-supplied augments add corresponding personal mana charge here.

Split count at cast index0 adds arrows: normally from weapon spell, or from initial EntitySpellArrow's resolver when present. Extra arrows are built from weapon's base stored spell, so do not assume all ammunition augments copied into every split. These fan out alternating10degree increments. One launch mana debit is outside extra-arrow loop; ammo shrinks once by processed use amount after adding arrows unless infinite. No per-hit debit or automatic miss refund in inspected launch/impact paths. Infinity/creative/processAmmoUse and pickup controls are separate branches; don't extrapolate generic infinite recoverable arrows from a visible fan.

## Crossbow: loading and firing are different transactions

Charging complete and not already charged calls custom tryLoadProjectiles. It first builds modified spell context and computes **expended** cost; requires enough personal mana or creative **before ammo validation/loading**, unlike the Bow's ordinary-arrow fallback. Then obtains projectile item and tests resolver.canCast (or nonplayer branch). Successful check expends mana and adds spell Split to requested projectile count; Multishot>0 initially requests3, otherwise1. Split-arrow ammo adds its two extra count separately.

It calls inherited useAmmo for each requested projectile (additional projectiles use the k>0 flag). Only nonempty checked stacks become ChargedProjectiles. Native method can debit mana and ultimately store no usable stack; no refund branch exists here. It returns true even when stacks empty, so end-loading sound is not proof a charged shot was created. Do not promise no-ammo casting works like Bow.

When nonempty load succeeds, it sets CUSTOM_DATA to a **new** CompoundTag with isSpell=true, not merged old custom data. This flag is not conditioned on consumedMana: resolver rejection after initial enough-mana check is not a reliable guarantee of an ordinary nonspell charge. Data overwriting and failed validation need explicit acceptance, not inferred atomicity.

On firing, arrow-loose cancellation returns before clearing charges. Otherwise performShooting replaces CHARGED_PROJECTILES with EMPTY **before** iterating/spawning. It reads isSpell and shootOne constructs projectiles from **current stored spell**, not a frozen load-time spell snapshot. No new mana debit/check in this firing body. Changing inscription between load and fire can therefore separate paid spell from dispatched spell; effective synchronization and restrictions need test, not exploit approval.

isSpell branch replaces even initially constructed projectile with a new base-spell EntitySpellArrow; thus special ammo modifiers/firework construction are not proof their original entity/effects survive this branch. It adds Piercing enchantment to `pierceLeft`, while EntitySpellArrow's inspected collision/removal uses **getPierceLevel/setPierceLevel**, with no other pierceLeft read in that class. Do not advertise guaranteed vanilla Piercing parity from an increment to a separate field. Additional projectile pickup restrictions and charged ammo behavior require actual tests.

## Impact, ownership and verification boundaries

SpellArrow.createArrow only constructs its modified spell resolver for a **Player with mana capability and a main-hand ICasterTool**. Otherwise returns ordinary arrow path (no mana capability gives vanilla Arrow). This uses main-hand held tool rather than reliably the supplied bowStack; offhand shooting/item switching must be checked independently. Bow/Crossbow-specific wrapping can replace that initial arrow as described above.

EntitySpellArrow resolves spell effects directly on block/entity impact through onResolveEffect, not another complete personal onCast/prepayment cycle. Entity impact resolves spell before its arrow hit callback. Block impact excludes immediate repeat at same lastPosHit, invokes block projectile-hit callback, resolves particles and then removal decrements actual pierce level. Native tick skips noPhysics entities and respects player canHarmPlayer plus ProjectileImpact hook; these guards are not proof every claims/faction/protected-block rule works for all effect handlers. Count actual state changes, not arrow visuals.

## Quest and acceptance handoff

Optional stage after Apparatus: craft one spell weapon -> effects-only inscription -> counted normal-arrow effect -> compare no-mana boundary -> optional Imbuement arrow. Crossbow lesson must explicitly say payment happens during loading and fire uses current stored composition. Item criteria detect acquisition only; loaded state, mana debit and impact effects need manual observation or actual detectors. Reward XP and modest ordinary arrows, not rare arrow modifiers or a free advanced weapon as an operation proxy.

Unrun tests: Bow ordinary ammo/no ammo/no mana, minimum draw, Power on no-ammo branch, three augment arrows/split clones and actual ammo/mana deltas. Crossbow sufficient mana but missing/invalid ammo, resolver rejection, Multishot/Split count, load then change spell, canceled fire versus cleared charges, custom-data retention and Piercing behavior. Both: offhand/item switching, unload/reload while charged/in flight, miss/protected target, physical damage versus spell effect, pickup/durability and actual enchantment rules. No native bug finding is a validated economy or authorized bypass.
