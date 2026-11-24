// Experimental 1.21.1 KubeJS hooks: require clean server and client QA before merging.
// All special Chaos rabbits produce no mob loot. The boss is a melee-only duel:
// 100% immunity to non-player-melee damage, and a 90% dodge chance on melee blows.
// IMPORTANT: This damage hook does not itself reject non-damaging status effects.
const TMM_DamageTypes = Java.loadClass('net.minecraft.world.damagesource.DamageTypes')
const TMM_BOSS_HIT_CHANCE = 0.10

function tmmIsChaosRabbit(entity) {
  return entity != null && String(entity.type) === 'minecraft:rabbit' &&
    entity.getTags().contains('tmm_caerbannog_chaos')
}

function tmmIsChaosBoss(entity) {
  return tmmIsChaosRabbit(entity) && entity.getTags().contains('tmm_caerbannog_boss')
}

EntityEvents.drops('minecraft:rabbit', event => {
  if (tmmIsChaosRabbit(event.entity)) event.cancel()
})

// Run only for the final boss, leaving the 155 earlier horde rabbits vulnerable
// to the player's normal spells, area attacks, bows, and melee attacks.
// Vanilla player's fists, swords and other hand attacks use PLAYER_ATTACK.
// Modded melee damage types require an explicit registry audit before allowing them.
// GENERIC_KILL is an admin escape hatch so /kill remains available in disposable QA worlds.
EntityEvents.beforeHurt('minecraft:rabbit', event => {
  if (!tmmIsChaosBoss(event.entity)) return
  if (event.level.isClientSide()) return
  const source = event.source
  if (source['is'](TMM_DamageTypes.GENERIC_KILL)) return
  const isPlayerMelee = source['is'](TMM_DamageTypes.PLAYER_ATTACK)
  if (!isPlayerMelee || Math.random() >= TMM_BOSS_HIT_CHANCE) {
    event.cancel()
  }
})

// The boss's own frog-tongue execution chance requires a damaging physical bite,
// not a blocked/zero-damage attack or indirect effect. Visual tongue is future work.
// Test armor/shield/totem interactions and resurrection bridge compatibility.
EntityEvents.afterHurt(event => {
  if (event.level.isClientSide() || event.damage <= 0) return
  const attacker = event.source.actual
  if (tmmIsChaosBoss(attacker) &&
      event.source['is'](TMM_DamageTypes.MOB_ATTACK) &&
      Math.random() < 0.10) {
    event.entity.kill()
  }
})
