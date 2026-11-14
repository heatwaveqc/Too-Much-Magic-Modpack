// TMM Core Magic pilot, MC 1.21.1 NeoForge.
// Requires Tyz's Skills 6.4.2, KubeJS 2101, Ars Nouveau 5.13.5,
// Iron's Spells 3.16.3 and Psi 110. Intentionally NOT part of installed pack yet.
//
// The 99-rank Thrift node supplies Goety and Neo Vitae through native Tyz
// attribute modifiers. These three additional casting hooks target otherwise
// unexposed resource costs. Their API paths were checked in upstream source.
// MXP awards must read the pre-Thrift nominal cost, not this discounted debit.
//
// IMPORTANT TEST GATE: Ars 'n Spells cross-casting and payer-switching can
// exercise multiple handlers. Test exactly-once discount and debit in a
// disposable world before enabling the pilot in a published pack.

const TMM_MAGIC_THRIFT_ID = 'tmm_magic_thrift_1'
const TMM_MAGIC_THRIFT_PER_RANK = 0.003 // 0.3% of nominal price per rank
const TMM_ServerPlayer = Java.loadClass('net.minecraft.server.level.ServerPlayer')

function tmmThriftRank(player) {
  if (!(player instanceof TMM_ServerPlayer)) return 0
  const rank = TyzsSkills.skills().getSkillLevel(player, TMM_MAGIC_THRIFT_ID)
  return Math.min(99, Math.max(0, rank))
}

function tmmDiscount(nominal, rank) {
  // For zero-cost or invalid casts, do not create a payment.
  if (!Number.isFinite(nominal) || nominal <= 0 || rank <= 0) return nominal
  // Never reduce a positive paid cast to zero. Keeping the actual debit > 0
  // is also required by the universal MXP transaction acceptance rule.
  return Math.max(1, Math.ceil(nominal * (1 - TMM_MAGIC_THRIFT_PER_RANK * rank)))
}

// Ars Nouveau posts SpellCostCalcEvent.Post in SpellResolver.getExpendedCost
// just before charging the accepted spell. getResolveCost uses Pre, not Post.
NativeEvents.onEvent('com.hollingsworth.arsnouveau.api.event.SpellCostCalcEvent$Post', event => {
  const player = event.context.getUnwrappedCaster()
  const rank = tmmThriftRank(player)
  if (rank <= 0) return
  event.currentCost = tmmDiscount(event.currentCost, rank)
})

// Iron's SpellOnCastEvent is posted immediately before the committed mana
// write by AbstractSpell.castSpell. Nonmana scroll/mob/command casts should
// never receive a free-cast award or pretend to consume mana.
NativeEvents.onEvent('io.redspace.ironsspellbooks.api.events.SpellOnCastEvent', event => {
  const player = event.getPlayer()
  const rank = tmmThriftRank(player)
  if (rank <= 0 || !event.getCastSource().consumesMana()) return
  event.setManaCost(tmmDiscount(event.getManaCost(), rank))
})

// Psi's own NeoForge KubeJS integration handles pre-cast cost decisions.
// Its PreSpellCastEvent.getCost is AFTER CAD efficiency and may include CAD
// reservoir discounts. The core skill gives an additional reduction to the
// remaining positive price. Exclude canceled/failure casts in the MXP ledger.
PsiEvents.preSpellCast(event => {
  const rank = tmmThriftRank(event.getPlayer())
  if (rank <= 0) return
  event.setCost(tmmDiscount(event.getCost(), rank))
})
