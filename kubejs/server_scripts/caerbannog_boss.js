// Experimental 1.21.1 KubeJS hooks; unverified until clean server + client combat test.
// Wave rabbits have a custom loot table as a first defense; cancel any extra mob drops here.
function tmmIsChaosRabbit(entity) {
  return entity != null && String(entity.type) === 'minecraft:rabbit' &&
    entity.getTags().contains('tmm_caerbannog_chaos')
}

EntityEvents.drops('minecraft:rabbit', event => {
  if (tmmIsChaosRabbit(event.entity)) event.cancel()
})

// After-damage hook: successful boss bites have a 10% instant-kill chance.
// Test against armor, totems, PvP rules, extra lives, and transformation resurrection bridges.
EntityEvents.afterHurt(event => {
  if (event.level.isClientSide()) return
  const attacker = event.source.actual
  if (tmmIsChaosRabbit(attacker) && attacker.getTags().contains('tmm_caerbannog_boss') && Math.random() < 0.10) {
    event.entity.kill()
  }
})
