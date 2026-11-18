# Ars Nouveau pinned field tools, chalk runes and grates

Exact5.13.5 JAR SHA1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; CFR0.152. Evidence: DowsingRod, RunicChalk, RuneBlock/RuneTile/RuneCaster/TileCaster, ItemGrate, native six recipes/tags/Documentation. [Parent](ars-nouveau.md), [spell records](ars-nouveau-pinned-spell-records.md), [status consumers](ars-nouveau-pinned-potion-effects.md), [Source](ars-nouveau-pinned-source.md). No live placement/rune/redstone/fluid/claims acceptance. These close distinct functional item branches, not every decorative variant.

## Dowsing Rod: two different use paths

Notebook **Items -> Dowsing Rod**. Shaped ` x /a a`: one Gold-ingot-tag item and two Archwood Planks. Declared durability4. Ordinary use increments damage1and shrinks item at damage>=max unless infinite-material; server grants Magic Find1200ticks plus RitualScrying.grantScrying1200ticks with dowsing_rod block tag. Durability mutation isn't guarded server-only in use; exact client sync untested.

Native block tag contains `#ars_nouveau:golem/budding`, not every ore. Magic Find separately scans tagged entities every60ticks within box75 and applies Glowing1200; entity tag includes Ars workers, Wilden types/boss, gift Starbuncle and Alakarkinos. See effect companion. This is a practical worker/budding-block locator, not a generic structure finder, guaranteed encounter or claim-permission probe.

**Use on IPedestalMachine** instead: server calls lightPedestal and returns SUCCESS without the ordinary-use durability/effect branch in that method. Use to visualize supported machine pedestal layout; doesn't prove all recipes are built correctly or execute one. Don't conflate four ordinary locator uses with a four-use construction-only limit. Wrong block/fallback and control interaction need client verification.

## Runic Chalk and permanent rune path

Notebook **Spell Casting -> Runic Chalk**, Scribes Table/Scribing for Spell Parchment. Shapeless Manipulation Essence+Bone Meal+Magebloom Fiber, one Chalk, durability15.

Use Chalk on block face: target adjacent clicked-face position; if replaceable, sets RuneBlock placement, stores player's UUID, then hurtAndBreak1using MAINHAND slot. Nonreplaceable target still returns SUCCESS but no placement/wear branch. Offhand placement can differ in break-slot behavior; don't teach successful return as placement detector. Placed RuneTile defaults permanent (isTemporaryfalse), chargedtrue, no spell; initial placement doesn't contain useful effects.

Use nonempty **Touch-leading Spell Parchment** on Rune to set spell and owner UUID. Other forms rejected with Touch message. This is not the effects-only inscription rule for Enchanter tools. Parchment is copied as spell data without consumption here; merely holding Book doesn't match this useItemOn branch. Chalk on a temporary rune sets isTemporaryfalse; that conversion branch doesn't explicitly consume Chalk durability. Rune glyph-created temporary spell and physical Chalk rune are separate acquisition routes.

EntityInside queries eligible nonspectator entities intersecting Rune shape; leading IFilter effects can reject trigger using entityIn. It then chooses **first entity from queried list**, which need not be same entity used in filter check, and schedules block tick1. That tick calls RuneTile.castSpell. It requires charged, nonempty Touch spell, server; permanent disabled flag blocks. Wrapper/fake-player identity uses stored UUID, Sensitive can select online real player and source inventory; it isn't a universal owner-permission bypass.

After resolver.onCastOnEntity, permanent Rune **unconditionally** becomes uncharged and ticksUntilCharge40; doesn't inspect returned success. Temporary Rune destroyed instead. Exception reports error and destroys Rune. Recharge after timer calls range10SourceUtil for raw spell.getCost, marks charged on successful payment; failure waits60ticks before retry. RuneCaster delegates TileCaster; inspected generic expendMana only debits BasicSpellTurretTile branch, so rune-specific charge economy is this recharge path, not ordinary player mana. First default charged rune can dispatch before initial Source recharge. Actual effect/cancel failure and Source debt must be observed; no miss refund or automatic all-state rollback promised.

## Four grate variants: vertical transfer despite orientation

Notebook **Crafting -> Archwood Grate**, includes Gold/Sourcestone/Smooth Sourcestone variants. Each shaped ` g /gMg/ g `: four Iron Bars around center Archwood-log tag, Gold-ingot tag, Sourcestone or Smooth Sourcestone respectively. One result. Actual implementation class ItemGrate, six-facing shape; placement opposite nearest look direction.

Ordinary item and empty-hand use both forward interaction to **block directly below**, shifted hitY-1; bucket pickup also delegates below. Transfer always compares pos.above/pos.below regardless visible FACING. Sideways orientation isn't a horizontal fluid pipe.

Server neighbor change with source fluid above schedules tick10. Tick requires source, then: (1) if below LiquidBlockContainer accepts/places liquid, attempts above BucketPickup and removes residual source above; (2) if below replaceable, takes above BucketPickup first and only places legacy fluid below when returned item is BucketItem; (3) for not-full Cauldron, picks up above fluid into stack before invoking cauldron item-use with ANFakePlayer. Failed downstream branch is not a guaranteed transaction rollback; don't use rare fluids before testing accepted states. Flowing fluid alone doesn't pass source check. No Source/personal mana debit in these methods.

Collision prioritizes ITEM_GRATE_COLLIDE tag; otherwise ItemEntity/Projectile or ITEM_GRATE_PASSABLE tag gets empty collision shape. Other entities retain physical shape. Effective entity tags, projectile ray tracing and claims can change outcome. This is world block/liquid movement, not a generic storage/logistics mod or recipe-driven fluid converter.

## Quest and acceptance handoff

Optional field-tool choices: use Rod on known helper/budding block or pedestal machine; Chalk place -> Touch Parchment bind -> safe entity trigger -> observe Source recharge; grate above known replaceable/cauldron target. Reference named Notebook pages and JEI recipe, reward XP/modest construction supplies. Item tasks prove acquisition only; manual observation/real detectors needed for layout reveal, rune operation or transfer.

Unrun boundaries: Rod4wear versus machine-use path; Rune blocked target, forms, multiple entities/filter identity, cancel/exception/disabled/recharge40and60retry, offhand/Sensitive owner offline; grate facing, source vs flowing, waterlogging, failed cauldron/replacement, full receiver and fluid-loss recovery, item/projectile tag precedence. No client PASS or claims-safe operation inferred.
