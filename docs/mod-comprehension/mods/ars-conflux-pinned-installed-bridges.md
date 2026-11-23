# Ars Conflux 0.1.56 — installed Controle/Additions/nSpells recipients

Exact Conflux SHA1 `97f3c31c624df19209db648e4fe6dd2e67e08d80`: [profile](ars-conflux.md). Two-ended CFR0.152 against hash-pinned Controle1.6.16, Additions21.3.0and nSpells3.3.5; identities in [completion receipt](../COMPLETION-STATUS.md). Conflux optional mixins/RandomOperands/RetaliationTargets/ArsTrajectory/EntityTransfers/AnsMixinPlugin, native FilterBinary/Random/WarpingSpellPrismBlock/MethodRetaliate and javap CrossCastingHandler signatures inspected. [Current Elemental bridge](ars-elemental-pinned-phalanx-worldgen.md), [Additions learning bridge](ars-conflux-pinned-content.md). No injection/transfer/RNG/retaliation/gameplay acceptance.

## Controle binary Random operands

OptionalControleFilterMixin targets exact `dev.qther.ars_controle.spell.filter.FilterBinary.getFilters` RETURN and wraps result with RandomOperands.snapshot. Null result staysnull. Determine first operand at resolver currentIndex and second at first+first-augment-count+1. If operand class name is exactly FilterRandom, rebuild its own SpellStats from **that operand index's augments**, child context/resolver and caster equipment, reflectively call public static calculateChance(double), return new IFilter Snapshot. Nonrandom operand retains native object. Reflection API failure throws IllegalStateException rather than defaulting to50%.

Native getFilters uses FilterRandom.INSTANCE singleton. It sets first chance then sets second chance; its second case erroneously obtains spell.getAugments(firstIndex,caster). Two Random operands can therefore share/overwrite chance and use wrong second augments. Current Conflux returned pair replaces Random operands with **independent immutable chance values**, using correct second index. It does not remove earlier native singleton mutation or patch standalone Random resolution.

Chance formula remains native: A>0→1−0.5/2^A; A<0→0.5/2^(−A); zero→0.5. Snapshot stores probability **not the boolean roll**. Each shouldResolveOnBlock/Entity call rolls fresh ThreadLocalRandom<=chance; binary short-circuit/operator evaluation still controls how many operands evaluated. Do not teach one frozen true/false per spell or a guaranteed compound outcome. Height-filter mutable state and other binary operands remain native.

Player route stays existing Controle Filter glyph learning/Spell Book boolean grammar; no new recipe/cost/unlock introduced here. Use native Notebook Controle binary/random glyph entries and JEI Scribes recipes. Explain independent per-operand probabilities when relevant. Item task only proves glyph possession, not RNG correctness; small positive XP/ordinary supplies/manual cast observation. Test Random+Amplify versus Random+Dampen in same binary pair, reversed ordering, short-circuit operator, sequential players, no Random and standalone Random. Record output distribution rather than treating one cast as probability proof.

## Controle managed Warping Spell Prism

Native changeSpellLevel creates a new same-type projectile in destination, restoreFrom/copies resolver+redirect/age/Pierce/Sensitive/velocity, addDuringTeleport then removes old CHANGED_DIMENSION. Current OptionalControlePrismMixin intercepts HEAD: if **ArsTrajectory.redirect** recognizes managed carrier, return original carrier and skip native clone branch. Unmanaged trajectory returnsfalse and native path remains.

Managed redirect looks up weak MANAGED entry; absent returnsfalse. Present sets pendingDestination, immediately rewrites carrier Level through EntityWorldAccess and returnstrue. It does **not** immediately prove visible flight transfer succeeded. Next trajectory tick/impact transfer clears pending, puts carrier at supplied position and relocates visual or entire owning-session projectile group. Failure closes trajectory/discards visual+carrier; no automatic Source refund in these methods. EntityTransfers.projectile changes dimension/removal callback/revives/same object then destination.addFreshEntity; failed add discards. No arbitrary chunk-loading guarantee.

This hook changes carrier identity/transfer implementation only. Native prism destination/link checks, Source request/payment order, redirect counter/collision/event paths remain in [Controle profile](ars-controle.md). Source cost is not replaced with personal mana or waived by managed redirect; native position/Source actions outside helper still apply. No generic interdimensional item transport item/network is added.

Test actual compatible managed consumer with shared session/visual, no managed consumer(nativeclone), same/cross-dimension, destination denial/unload/removed visual, Source shortage before/after native relocation steps, owner/resolver identity. Neither managed-library registration nor prism possession proves transfer. Future optional quest uses named native Warping Spell Prism guide plus honest manual operation check; no repeated teleport payout.

## Additions Retaliate: current damage source, not stale kill credit

Native Additions MethodRetaliate.cast calls caster.getKillCredit, rejectsnull/self, resolves remaining spell at EntityHitResult of that living entity, returnsSUCCESS; Tier3/base mana25/no compatible augments. Its guide says last attacker within5seconds, but no literal5second check occurs in this method; native getKillCredit internals/lifecycle govern fallback.

Conflux wraps LivingEntity.hurt in nested thread-local RetaliationTargets frame(victim,DamageSource,parent), restores previous finally. During a matching victim frame, OptionalAdditionsRetaliateMixin replaces getKillCredit with **current DamageSource.getEntity if living and not victim**, otherwise null. **No stale-kill-credit fallback when currently taking damage but no eligible attacker**. Outside a frame it delegates original getKillCredit. Thus environmental/nonliving-source/self damage can cause Retaliate failure instead of targeting an earlier attacker. Nested frames search matching victim. DamageSource.getEntity may also reflect Conflux damage-owner rules; direct projectile entity is not necessarily attacker.

No additional mana/Source fee/cooldown in this hook. Ordinary resolver admission/debit versus failure comes from native/current Conflux route; direct reactive paths remain separately scoped. No guarantee that every damage callback actually invokes a Retaliate spell or bypasses grammar/claims.

Teach existing Additions Retaliate glyph/native Notebook/JEI acquisition, current-attack targeting for a supported reactive use, plus ordinary manual fallback separately. Item possession isn't action proof. Test two attackers/stalecredit/currentenvironmental hit/selfsource/projectile-with-living-owner/nesteddamage and canceled hurt. Observe target and mana independently. No unsafe mandatory player-damage quest or repeatable retaliation reward.

## nSpells3.3.5optional adapter is intentionally gated out

AnsMixinPlugin checks CrossCastingHandler has castArsSpell descriptor containing **CastTargetHint or InteractionOrigin**. Exact installed3.3.5javap has only signatures `(Player,ItemStack,CrossModSpell)` and sameplusUUID. Neither expected target/origin descriptor exists. Therefore plugin's supported=false and **OptionalAnsCarrierMixin/OptionalAnsNativeCastMixin/OptionalAnsProxyReleaseMixin are not selected** under this exact class surface. Local AnsActiveCasts code described in [active-cast report](ars-conflux-pinned-active-casts.md) must not be claimed as current installed extra chant/cooldown admission.

This guarded optional skip is a known source compatibility boundary, not a loader hard blocker or permission to upgrade nSpells. Generic native resolver/mana wrappers still exist; NativePaymentCleanup separately finds installed ArsCastPayments.finish and is not guarded by OptionalAns plugin name. Basic nSpells source profile remains [version-scoped](ars-n-spells.md); this audit does not claim its complete runtime compatibility with all Conflux generic hooks.

Test authorized loaded mixin selection and original3.3.5cross-cast behavior, comparing an explicitly supported future target only after separate pin authorization. Do not change versions or force-enable adapters during research. No unrun API signature match promoted to PASS.

## Ability tags and absent consumers

AbilityReloadListener reads `data/<namespace>/conflux/glyph_abilities/<glyph-path>.json` with `tags` array of namespaced resource-ID strings; map key is glyph resource ID. Invalid resource warns/omits; reload replaces snapshot. GlyphAbilities API declarations merge defaults with data override, duplicate conflicting default rejects; undeclared→empty. No GlyphAbilities.declare/has consumer found inside exact binary beyond its own API forwarding; packaged Conflux has only4recipeJSON/no ability data. Tracked integration Java grep found no direct Conflux provider APIs; this is bounded source evidence, not all pack binaries/reflection proof.

This classification hook neither grants new spells nor manufactures items. Do not require players to craft an ability tag, edit datapacks or use operator commands as normal progression. External consumers/actual loaded data must establish player behavior first.

## Scope and acceptance handoff

These bridges keep original native guide/JEI/build routes. Current Phalanx recipe/registration and Codex random-learning boundaries already have two-sided companions; optional Zero/NEG/Omega/Hex/Adams pins absent in current tracked mod paths and are not invented content. Default ring/NPC-capability/generic native changes remain [mana report](ars-conflux-pinned-mana.md).

Conflux still needs consolidated default-scope/interface/progression matrix and final pilot review; runtime injection/order/effective config remains unrun acceptance. This report closes the named installed-recipient source gaps, not every hypothetical consumer mod.

## Remaining generic execution/budget interfaces

ExecutionSession has PREPARED→ACTIVE→COMPLETED/CANCELLED lifecycle. Billing configured only before start; start verifies permission/owner, then either payment.trigger(launch) or uncharged launch for repeat-billed mode. Repeat-billed activate wraps each action in payment trigger. Failed start finishes SPAWN_FAILED; errors finish ERROR and preserve cleanup exceptions. Tick ends on owner/executor absence or leaving origin world, maxTicks, periodic20tick permission loss, driver completion. Impact requires active+permitted and carries driver incoming vector into continuation. finish closes driver/continuation and untracks projectiles; it is not a generic inventory/mana rollback. Registry owns active sessions, prevents conflicting claims/reentrant starts, supports explicit replacing after handoff and terminates on unload/stop. No concrete new consumer spell is supplied by this lifecycle alone.

SpellBudgets aggregates native quote plus known TriggerPaidGlyph charges by resource-pool key; incomplete estimates are not affordable/possible. Capacity and current affordability differ; estimation does not reserve/debit. Generic API consumers must supply known charge/driver/permission/scope. These contracts supplement the [mana](ars-conflux-pinned-mana.md)/[carrier](ars-conflux-pinned-carriers.md) reports; no universal cross-resource exchange or free failed effect promise.
