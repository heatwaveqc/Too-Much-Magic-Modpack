// Read-only JAWR 0.5.39 dual-affliction observer for a DISPOSABLE QA instance.
// NOT installed under kubejs/server_scripts. No transformations, stages, Tyz
// levels, ability unlocks, or native attachments are modified.
// Purpose: detect both acquisition orders and logged changes to JAWR's
// two independent affliction levels and its one active transformation slot.
;(() => {
  const V = Java.tryLoadClass('dev.sterner.witchery.feature.affliction.vampire.VampirePlayerAttachment')
  const W = Java.tryLoadClass('dev.sterner.witchery.feature.affliction.werewolf.WerewolfPlayerAttachment')
  const T = Java.tryLoadClass('dev.sterner.witchery.feature.transformation.TransformationPlayerAttachment')
  if (V == null || W == null || T == null) {
    console.warn('[TMM JAWR hybrid observer] Required pinned JAWR classes missing; observer disabled.')
    return
  }

  const last = {}
  function snapshot(player) {
    try {
      const v = V.getData(player)
      const w = W.getData(player)
      const t = T.getData(player)
      if (v == null || w == null || t == null) return null
      const vampire = v.isVampire()
      const werewolf = w.isWerewolf()
      const vampireLevel = Number(v.getLevel())
      const werewolfLevel = Number(w.getLevel())
      const form = String(t.getTransformationType().name())
      if (typeof vampire !== 'boolean' || typeof werewolf !== 'boolean' ||
          !Number.isInteger(vampireLevel) || !Number.isInteger(werewolfLevel)) return null
      return { vampire, werewolf, vampireLevel, werewolfLevel, form }
    } catch (ex) {
      console.warn('[TMM JAWR hybrid observer] Native state unreadable: ' + ex)
      return null
    }
  }

  function report(player, force) {
    const uuid = String(player.getUUID())
    const state = snapshot(player)
    if (state === null) {
      if (last[uuid] !== 'UNKNOWN') {
        console.warn('[TMM JAWR hybrid observer] ' + uuid + ' state UNKNOWN; cannot infer hybrid or cure.')
        last[uuid] = 'UNKNOWN'
      }
      return
    }
    const signature = JSON.stringify(state)
    if (!force && last[uuid] === signature) return
    last[uuid] = signature
    const classification = state.vampire && state.werewolf
      ? 'DUAL_AFFLICTED_RESEARCH_ONLY'
      : state.vampire ? 'JAWR_VAMPIRE' : state.werewolf ? 'JAWR_WEREWOLF' : 'NEITHER'
    console.info('[TMM JAWR hybrid observer] ' + uuid + ' ' + classification +
      ' vampireLv=' + state.vampireLevel +
      ' werewolfLv=' + state.werewolfLevel +
      ' activeForm=' + state.form +
      ' (READ ONLY; not Abomination authorization)')
  }

  PlayerEvents.loggedIn(event => report(event.player, true))
  PlayerEvents.tick(event => {
    if (event.player.tickCount % 20 === 0) report(event.player, false)
  })
  PlayerEvents.loggedOut(event => { delete last[String(event.player.getUUID())] })
})()
