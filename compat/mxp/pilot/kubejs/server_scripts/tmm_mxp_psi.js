// PROTOTYPE ONLY — excluded from the installed packwiz index.
// Psi 110 source: preSpellCast contains compiled *nominal* cost, and spellCast
// fires after data.deductPsi on an accepted cast. Uses the MXP Java mod.
// Do not install until the KubeJS class-filter and Java ABI smoke test passes.
const MxpService = Java.tryLoadClass('io.github.heatwaveqc.tmm.mxp.MxpService')
const $ServerPlayer = Java.loadClass('net.minecraft.server.level.ServerPlayer')
const $SpellStat = Java.loadClass('vazkii.psi.api.spell.EnumSpellStat')
const pending = new Map()
let serial = 0

function playerKey(player) { return String(player.getUUID()) }
function cadPsi(stack) {
  if (stack.isEmpty()) return 0
  // In the pinned 1.21.1 Psi source, CAD items implement ICAD.getStoredPsi.
  // -1 means an infinite/creative reservoir and MUST NOT earn MXP.
  return Number(stack.getItem().getStoredPsi(stack))
}

if (MxpService) {
  PsiEvents.preSpellCast(event => {
    const player = event.getPlayer()
    if (!(player instanceof $ServerPlayer)) return
    const ctx = event.getContext()
    if (!ctx || !ctx.cspell || !ctx.cspell.metadata) return
    const nominal = Number(ctx.cspell.metadata.getStat($SpellStat.COST))
    const quoted = Number(event.getCost())
    if (!(nominal > 0) || !(quoted > 0)) return
    const cadBefore = cadPsi(event.getCad())
    if (cadBefore < 0) return // infinite CAD: not a paid cast
    pending.set(playerKey(player), {
      ctx,
      nominal,
      quote: quoted,
      psiBefore: Number(event.getPlayerData().getAvailablePsi()),
      cadBefore,
      tick: Number(player.level().getGameTime())
    })
  })

  PsiEvents.spellCast(event => {
    const player = event.getPlayer()
    if (!(player instanceof $ServerPlayer)) return
    const key = playerKey(player)
    const old = pending.get(key)
    // Do not consume a prior canceled cast's quotation.
    if (!old || !old.ctx.equals(event.getContext())) return
    pending.delete(key)
    const spentPersonal = Math.max(0, old.psiBefore -
      Number(event.getPlayerData().getAvailablePsi()))
    const afterCad = cadPsi(event.getCad())
    if (afterCad < 0) return
    const spentCad = Math.max(0, old.cadBefore - afterCad)
    const actualDebit = spentPersonal + spentCad
    if (!(actualDebit > 0)) return
    const tx = 'psi:' + key + ':' + old.tick + ':' + (++serial)
    // Original compiled cost is intentionally BEFORE any CAD efficiency
    // and TMM Thrift. First-access Psi pool is 5000, per Psi 110 source.
    MxpService.resource(player, 'psi:cad_cast', tx, old.nominal, 5000, actualDebit)
  })

  ServerEvents.unloaded(() => pending.clear())
} else {
  console.warn('[TMM MXP] Psi pilot not installed: Java bridge unavailable')
}
