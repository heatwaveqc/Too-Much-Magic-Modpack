// Chaos Bunny first balance-test variant: five-second successful-hit immunity.
// The boss takes ONLY direct player melee damage, with one real hit per 100 ticks.
// All other damage is blocked, but environmental triggers still activate counters
// (effect conversion / teleport/flight are a separate, not-yet-live workstream).
// Owner 2026-11-22: *2%* frog-tongue execution on successful damaging bite,
// processed by ordinary damage/death protection instead of Entity#kill/remove.
const TMM_DamageTypes = Java.loadClass('net.minecraft.world.damagesource.DamageTypes')
const TMM_CHAOS_MELEE_WINDOW_TICKS = 100 // QA variant: 5 seconds; compare 200 for 10.
const TMM_CHAOS_TONGUE_CHANCE = 0.02
const TMM_CHAOS_HIT_TICK_KEY = 'tmm_caerbannog_last_valid_melee_tick'
const TMM_CHAOS_HIT_ARMED_KEY = 'tmm_caerbannog_melee_cooldown_armed'
const TMM_CHAOS_TONGUE_GUARD_KEY = 'tmm_caerbannog_tongue_in_progress'

function tmmIsChaosRabbit(entity) {
  return entity != null && String(entity.type) === 'minecraft:rabbit' &&
    entity.getTags().contains('tmm_caerbannog_chaos')
}

function tmmIsChaosBoss(entity) {
  return tmmIsChaosRabbit(entity) && entity.getTags().contains('tmm_caerbannog_boss')
}

function tmmChaosTime(level) {
  return Number(level.getGameTime())
}

function tmmChaosEligibleMelee(source) {
  // Actual vanilla source class; do not accidentally allow arrows or ranged
  // spells whose owner happens to be a player. Modded physical types need QA.
  return source['is'](TMM_DamageTypes.PLAYER_ATTACK) &&
    source.actual != null && String(source.actual.type) === 'minecraft:player'
}

EntityEvents.drops('minecraft:rabbit', event => {
  if (tmmIsChaosRabbit(event.entity)) event.cancel()
})

EntityEvents.beforeHurt('minecraft:rabbit', event => {
  if (!tmmIsChaosBoss(event.entity) || event.level.isClientSide()) return
  const source = event.source
  if (source['is'](TMM_DamageTypes.GENERIC_KILL)) return // explicit admin /kill escape
  if (!tmmChaosEligibleMelee(source)) {
    event.cancel()
    return
  }
  const data = event.entity.getPersistentData()
  const now = tmmChaosTime(event.level)
  if (data.getBoolean(TMM_CHAOS_HIT_ARMED_KEY) &&
      now - Number(data.getLong(TMM_CHAOS_HIT_TICK_KEY)) < TMM_CHAOS_MELEE_WINDOW_TICKS &&
      now >= Number(data.getLong(TMM_CHAOS_HIT_TICK_KEY))) {
    event.cancel()
  }
})

// The cooldown starts on FINAL positive damage, not a swing, whiff, shield
// block or zero-damage strike. No random dodge rolls in this test variant.
EntityEvents.afterHurt('minecraft:rabbit', event => {
  if (!tmmIsChaosBoss(event.entity) || event.level.isClientSide() || event.damage <= 0) return
  if (!tmmChaosEligibleMelee(event.source)) return
  const data = event.entity.getPersistentData()
  data.putLong(TMM_CHAOS_HIT_TICK_KEY, tmmChaosTime(event.level))
  data.putBoolean(TMM_CHAOS_HIT_ARMED_KEY, true)
})

// The tongue is rare and should NEVER directly remove entities. Vanilla frogs
// remove prey with RemovalReason.KILLED: that bypasses ordinary protection.
// Here a *second*, guarded normal mob-attack damage packet triggers standard
// totems and all downstream native TMM death/resurrection processing.
// This remains a SOURCE QA prototype: in-game invulnerability frames and
// TMM death-priority interactions must be verified before survival release.
EntityEvents.afterHurt(event => {
  if (event.level.isClientSide() || event.damage <= 0) return
  const attacker = event.source.actual
  if (!tmmIsChaosBoss(attacker) || !event.source['is'](TMM_DamageTypes.MOB_ATTACK)) return
  const victim = event.entity
  if (victim == null || !victim.isAlive()) return
  const data = victim.getPersistentData()
  if (data.getBoolean(TMM_CHAOS_TONGUE_GUARD_KEY)) return
  if (Math.random() >= TMM_CHAOS_TONGUE_CHANCE) return
  data.putBoolean(TMM_CHAOS_TONGUE_GUARD_KEY, true)
  try {
    // Damage is deliberately large but not absolute. Armor, other damage
    // reductions, totems and custom Progression protections remain effective.
    // The victim was just hurt; reset the vanilla i-frame gate so this new
    // packet actually enters the normal damage pipeline in the same tick.
    const damage = Math.max(1024, Number(victim.getMaxHealth()) +
      Number(victim.getAbsorptionAmount()) + 512)
    victim.invulnerableTime = 0
    const source = victim.damageSources().mobAttack(attacker)
    victim.attack(source, damage)
  } finally {
    data.putBoolean(TMM_CHAOS_TONGUE_GUARD_KEY, false)
  }
})
