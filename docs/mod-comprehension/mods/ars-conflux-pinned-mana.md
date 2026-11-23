# Ars Conflux 0.1.56 — discounts, dispatch billing and NPC mana

Exact Conflux SHA1 `97f3c31c624df19209db648e4fe6dd2e67e08d80`, native Ars5.13.5 SHA1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`: [profile](ars-conflux.md). Direct CFR0.152: ConfluxConfig; ring/native-discount/trigger/upfront/split/inheritance mixins; ArsManaDiscounts, VanillaTriggerBilling, CostSlices, NativePaymentCleanup, ResourceCharge/Transactions/Account/PoolKey/ArsManaAccount/ArsManaPools, MobMana/NativeManaPlatform/ManaAttributeLimits. Native ManaUtil/SpellResolver/ManaCap/ServerConfig, Sauce74.113ManaDiscountMixin, nSpells3.3.5ArsCastPayments and current Elemental EffectBubbleShield recipients inspected. javap verifies ArsManaAccount constructor where CFR malformed: LivingCaster entity is pool owner, otherwise wrapped source; free only creative Player. No mixin-order/capability-resolution/gameplay acceptance.

## Different switches, different defaults

`ars_conflux-server.toml` mana_discounts.percentage_discount_rings **true**, lesser0.10/greater0.20(each0..1); ars_trigger_billing.enabled **false**. Default ring arithmetic changes do not mean native per-effect billing enabled. No matching override string found in current integration c11a95fc tracked config/defaultconfigs/kubejs; actual generated world SERVER config/assembled donors remain unverified. Personal mana is distinct from jar Source, XP and Soul energy.

## Percentage rings and cost quotes

Only exact native `ring_of_lesser_discount` / `ring_of_greater_discount` qualify. Enabled mixin returns0from their ordinary flat getManaDiscount, leaving mana-boost/regen methods alone. Rate scan sums each equipped Curios stack, armor stack and explicit caster-tool stack, caps at1; no stack-count multiplier, arbitrary inventory scan, separate offhand scan or cross-channel deduplication in this body.

At ManaUtil.getPlayerDiscounts RETURN, Conflux takes residual=max(0,rawSpellCost−otherDiscount), rounds **ceil(residual×(1−sumRates))**, adds difference back to discount. Native SpellResolver still posts Pre requirement versus Post debit cost events/clamps. Isolated examples: lesser ring raw10→9and raw1→1; lesser+greater100→70; with otherDiscount30first, same30%rings residual70→49. These are isolated formulas, not final all-equipment runtime prices.

Sauce74.113also injects TAIL on this method, adding player-only flat/school attributes; Conflux injects RETURN. Both inspected bodies use default priorities. **Final transformed injection order is not established** here. A whole-school/armor/ring formula needs generated-mixin/runtime evidence. ArsManaAccount.quoteCost instead uses percentage scan with empty explicit tool and no full native other-discount pipeline; opt-in quote(raw) is not necessarily native getResolveCost. Setting percentage modefalse restores native flat ring method and removes added ring-percentage conversion.

Use Worn Notebook native ring/Curios item entries and JEI crafts; Conflux adds no ring recipe/tier. Future lesson compares a known spell's actual debit under effective settings, with item-acquisition proxy/manual observation, small positive XP and ordinary supplies. Do not reward repeated mana expenditure or promise a fixed price from item possession.

## Optional native dispatch billing and failure costs

Mode is captured in SpellContext tag and inherited contexts consult snapshot. Enabled native deferred mode requires **LivingCaster**, excludes internal quote contexts; source-powered non-LivingCaster devices do not use this mode. A deferred group is native `ars_nouveau` AbstractEffect, excluding TriggerPaidGlyph; each part groups its following augments. Cast methods/addon glyphs are not automatically deferred. Group weight is casting cost+augment costs, clamped0..intMax. Raw/discount allocations use cumulative floors: floor((before+weight)×total/sum)−floor(before×total/sum), with BigInteger overflow fallback. Nondeferred groups retain upfront share.

ArsUpfrontPaymentMixin captures mode on native onCast/onCastOnBlock/onCastOnEntity HEAD. Before method dispatch, server path with TriggerPaidGlyph or eligible native deferred effect calls expendMana for noncreative upfront quote, marks paid, begins VolleyBudget, suppresses later normal debit. Return hook finishes/resets. Native pre-dispatch admission/event checks remain; **after this debit a failing method has no generic refund in the mixin**. Return-only cleanup is not exception-finally assurance. Horn direct onResolveEffect is not one of these entry methods, though its eventual native effects can still meet dispatch wrapper eligibility.

At native resume→AbstractEffect.onResolve wrapper:

1. Disabled/non-native/nonserver path simply runs original action. Under deferred mode, null/MISS stops context before action/debit. Executed group must match currentIndex−1and exact effect instance; mismatch throws.
2. Clone group-only quote context/copy tags/proportional raw+discount; ResolverContexts.quote supports source extensions and throws if multiple factories claim it. Compute Pre **required**, Post **spent**, freeze both.
3. Account checks enoughMana(max(required,spent)) plus quote's protected enoughMana; nested larger requests use wrapped enoughMana. Creative account runs action free.
4. Thread-local reservation for original resource+pool owner precedes action. On normal return wrapper reports true, even if effect's target was inapplicable or its internal mutation was rejected, then **physical debit follows action** through quote.expendMana. This is a paid attempt, not confirmed-output billing.
5. Insufficient request stops branch before action. Exception releases logical reservation but does not undo earlier world changes/upfront debit. NativePaymentCleanup.finish runs finally for quote context.

ResourceTransactions validates nonnegative amounts, aggregates nested held+charge under resource-kind/owner key, guards int overflow; affordableCount binary-searches count, zero/free returns requested. Reservation is **not physical escrow/global lock/world rollback**; unrelated APIs can still spend underlying mana. ResourceCharge action returningfalse skips debit, while native deferred wrapper always returnstrue after normally returning effect.

NativePaymentCleanup reflectively invokes installed nSpells **ArsCastPayments.finish(SpellContext)**. Exact3.3.5has this method; it removes plans/reservations, cancels unfinished uncommitted attempt, does not refund committed plan. Missing reflective method no-op; invocation failure throws. This confirms endpoint, not every cross-cast/alternate-currency integration.

VolleyBudget server batch stack caches per-SpellStats identity. Split getBuffCount RETURN changes count only with a nonrepeated current carrier profile and ProjectileForm in augments, through initialCharge.affordableCount(requested+1)−1; otherwise requested count remains. Quote does not itself debit/spawn. Complete carrier/conversion/repeated path remains open.

## NPC mana changes Elemental capability assumptions

Native provider was Player-only. Conflux adds missing maximum/regen attributes to nonplayer living types and LOWEST-priority capability provider over nonplayer entity types, returningnull for nonliving/client entities. ManagedCap extends native ManaCap using native MANA_ATTACHMENT. Existing providers/first-nonnull resolution remain a combined boundary; this does not grant player capabilities to blocks.

First server access marks persistent ars_conflux_mana_active, refreshes max, fills only if attachment did **not** already exist. Profile priority: explicit persistent profile; highest priority registered policy(descending, tieID); otherwise native INIT_MAX_MANA(default100) / INIT_MANA_REGEN(default5). No MobMana.configure/registerPolicy call sites found inside exact Conflux binary; external callers remain possible.

Maximum base=profile.max+glyphBonus×native glyphMax(default15)+bookTier×tierMax(default50), then attribute/MaxManaCalcEvent/reserve. Regen base=profile.regen+glyphBonus×glyphRegen(default0.33)+bookTier×tierRegen(default1), then attribute/ManaRegenCalcEvent and finite/nonnegative clamp. Active alive server nonplayer owned ManagedCap refreshes/regenerates every20actor ticks staggered by entityID; not a measured farm output. Native attachment stores values; only explicit player sync sends native mana packet, no NPC UI established.

Startup ceiling widening defaults enabled/max1,000,000/regen100,000, only if prior RangedAttribute ceiling lower. **Ceiling is not granted mana.** ArsManaPools read/set/restore/trySpend uses native pool/reservation accounting; client/dead/nonfinite/negative restrictions vary per operation, not arbitrary resource conversion.

Current Elemental EffectBubbleShield requires living target with nonnull native mana capability. **Conflux can supply previously missing NPC/Mage recipient**, so historical native-only caveat cannot exclude WaterMage shields in whole current family. Actual pool,350mana-per-mitigation default, timing and other providers still govern usefulness; base100alone is not effective-shield proof. MageResolver's own enoughMana override is separate. No guaranteed shield/unlimited Mage mana/combat PASS.

## Walkthrough and unrun acceptance

Native Notebook Spell Book/Scribes/Curios references remain; no extra Conflux altar/guide/tier needed. Explain effective percentage rings and per-dispatch payment only if enabled. FTB acquisition detects ring/book, not cost event or actual spell operation; manual observable cast/debit plus positive XP/ordinary supplies is honest handoff.

Test rings10/1/100raw, other equipment, modefalse/true, equipped/tool scan, actual Sauce/Conflux injection order and Pre/Post differences. Deferred mode native/addon/mixed spell, MISS, cancellation, inapplicable normal return, insufficient second branch, nested reservations, exception cleanup, creative/player/Source device. NPC no/existing attachment, profile/20tickregen/dead/client/provider coexistence, actual WaterMage shield/350mitigation. Observe world outcome and debit separately. No runtime/config/quest edits, assembly or launches.

Conflux remains researching for full carriers/reflection, active-cast/cooldown, geometry/schools and installed optional bridges. This closes named mana recipient contracts only.
