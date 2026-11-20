# Ars Elemental 0.7.10.4 — Bee and Slime combat summons

Exact hash/artifact [receipt](ars-elemental-pinned-guide-urn.md). Direct EffectSummonBee/Slime, SummonBee/Slime including inner goals, ModEntities/CompatUtils and two recipes; native Ars5.13.5 AbstractEffect summon/payment/event recipients. [Focus](ars-elemental-pinned-foci.md), [summoning threads](ars-elemental-pinned-threads.md), [undead revival](ars-elemental-pinned-undead-summons.md) provide related boundaries. No runtime combat/AI tests.

## Learn and cast

Search Worn Notebook **Summon Bee / Summon Slime**. Learn via native [Scribes Table](ars-nouveau-pinned-guide-glyphs.md), use a **Tier2**-capable book. Both glyph recipes XP55, default mana100 before cast-method/augments/modifiers:

| Recipe/output `ars_elemental:` | Consumed recipe ingredients | Spell schools |
| --- | --- | --- |
|`glyph_summon_bee`|Earth Essence, Conjuration Essence, Magebloom, Honeycomb|Conjuration + Earth|
|`glyph_summon_slime`|Water Essence, Conjuration Essence, two Slime Balls|Conjuration + Water|

Choose a cast method to place the summons at a hit location, then the summon effect. Native canSummon requires **real Player**, no Summoning Sickness unless creative. Each effect makes **3+Split count** creatures; **Extend Time/Duration Down/Split** are compatible augments. Duration is fixed formula **`(int)(20×(60+60×durationMultiplier))` ticks**; unlike native Steed, no minimum20 fallback is present in these methods. Nonpositive lifetime is not promised to create a durable summon.

Each gets ticksLeft, owner reference and hit position, then native summonLivingEntity assigns UUID, adds entity, applies native Summoning Focus child-effect path if present and posts SummonEvent. After that, caller sets last-hurt target/aggressive state; Slime also resets AI goals and assigns selected variant. It does not wait until the creature deals damage before charging the original spell. No recurring Source fee is in these entity timers.

Unless Ars version contains **SSR**, apply Summoning Sickness for **`(int)(ticks×0.8)`** after spawning. At unmodified duration this is960 ticks versus1200-tick summon lifetime. Pinned Ars5.13.5 is not SSR; addon summon thread does not actually shorten this duration for normal slots1–3 due to the [audited integer division](ars-elemental-pinned-threads.md).

## Shared timer, ownership and persistence

Both entity types implement ISummon/IFollowingSummon. Server tick decrements ticksLeft, poofs/discards at≤0 and posts expired summon-death event; actual die posts nonexpired event. Saved data includes `left` and owner UUID; Slime adds variant and resets goals on load. Owner lookup resolves **Player UUID in current level**, not arbitrary LivingEntity globally. Lost/offline/different-dimension owner behavior therefore needs acceptance.

Both registrations pass noSave only when SSR compatibility flag is true; current pin does not set it. Do not copy the blanket noSave claim from temporary mounts onto these combat summons. Timer serialization is still not proof of durable in-world acceptance across unload/restart.

Neither is one of the three classes in Necrotic reRaiseSummon; they do **not** become Direwolf/Undead/Vhex merely by wearing Necrotic Focus. The generic summoned-event Necromancy Strength/Speed branch can affect them, and native ISummon damage consumers remain applicable.

## Bee: fighting, not honey automation

SummonBee supplies Bee attack, wander/float/look, follow-summoner flying, copy-owner-target and owner-threat targeting goals. It returns **false for hasHive, hasNectar, canBreed, isFood**, and **false for hasStung**. It has zero base XP. This is not a teaching route for renewable beehive honey/pollination from ordinary Bee behavior; custom goals and return values must be respected.

Team/alliance checks delegate to resolved owner. Flying navigation can pass doors, cannot open doors, can float, and requires nonair below stable destinations. Actual pathing/sting cadence still needs runtime observation; no measured damage per second or unbreakable owner protection claimed.

## Slime: size, targeting and elemental variants

Spell sets size to **`(int)(2+ampMultiplier)`**, despite Amplify not being listed as a compatible augment. Don't instruct players to append an illegal Amplify merely because the implementation reads amp stats; other equipment/modifier sources may contribute. setSize calls vanilla then clamps its local attribute calculation1..127: movement-speed base **`0.4+0.05×size`**, attack-damage base **`2×size`**. Jump delay10..19 ticks. These attributes are not a full collision damage/throughput measurement.

The spell selects variant by ordered checks: special hard-coded developer UUID→MK; otherwise **Fire→Water→Earth→Air→Shaper's Focus→default Summon**. These checks use the audited resolver/focus-school helpers. Multiple elemental providers use this order, unlike the steed Set iteration. Variant names are lowercase strings. Only FIRE has inspected server behavior here: fireImmune true and boosted lava jump vertical velocity **`0.22+0.05×size`**; other variant names alone do not prove elemental status damage.

Spawn resets goal/target selectors: ordinary Slime goals plus FollowSummoner and custom CopyOwnerTarget, then mobs threatening/credited to owner. Custom target goal prefers owner's last hurt mob, else last hurt-by mob, excluding owner/same-owner summon. Team/alliance delegates to resolved owner and recognizes same-owner ISummon.

`isDealsDamage` always true. push explicitly attempts damage on LivingEntity unless owned/allied/same-owner-summon exclusions pass; playerTouch skips native touch for owner UUID. hurt rejects damage from same-owner ISummon. These are specific guards, not blanket protection against all friendly damage sources.

## Splitting is a separate death/removal path

SummonSlime.remove checks server, size>1 and **isDeadOrDying**, not just a removal-reason enum. It makes **2..4** child SummonSlime of integer half size, inheriting remaining lifetime, variant, owner reference, beneficial effects and selected name/noAI/invulnerability/persistence fields. Children spawn around old position; it does not invoke a new glyph cast or recharge mana. Ordinary live timer expiry does not satisfy dead/dying, so no guaranteed split-on-expiry from this branch.

Child creation directly casts `getOwner()` to **Player**; nonplayer-owner paths are not proven safe. The original spell's real-player gate fits this assumption, but arbitrary external summon creation can differ. No minimum lifetime reset occurs in child constructor. Zero base XP and absence of packaged matching summon loot tables are not a full assembled-pack no-loot guarantee; no Slime Ball farm is promised.

Future explicit acceptance: spell gate/negative duration, Split count and original/child effects; owner lookup/follow/AI; hive/nectar/sting behavior; size from permitted stats, variant precedence/fire lava behavior, friendly collision/damage; death splitting versus expiry, inherited lifetimes and save/load. These two routes bring glyph audit to **18of39**, with21other glyphs/12tomes and remaining major addon systems still researching. No runtime/config/quest changes.
