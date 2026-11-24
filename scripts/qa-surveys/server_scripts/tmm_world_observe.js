// Temporary QA command, no grants/teleports/world mutations. Explicit OP gate.
function tmmObserveWorld(p, server) {
  let R = Java.loadClass('net.minecraft.core.registries.BuiltInRegistries')
  let level = p.serverLevel()
  let result = {schema: 1, status: 'OBSERVATION_ONLY', build: JsonIO.read('kubejs/config/tmm-survey-build.json'), players: [], commands: [], recipes: [], errors: []}
  try {
    let biome = level.getBiome(p.blockPosition())
    let row = {dimension: String(level.dimension), biome: String(biome.unwrapKey().get().location()),
      nearbyEntities: [], biomeSpawns: [], effects: [], inventory: []}
    result.players.push(row) // Preserve completed fields if a later adapter is unsupported.
    level.getAllEntities().forEach(entity => {
      if (entity === p || entity.distanceToSqr(p) > 4096) return
      row.nearbyEntities.push({id: String(entity.type),
        distance: Math.round(Math.sqrt(entity.distanceToSqr(p))),
        tags: String(entity.getTags())})
    })
    Java.loadClass('net.minecraft.world.entity.MobCategory').values().forEach(category => {
      try {
        biome.value().getMobSettings().getMobs(category).unwrap().forEach(spawn => row.biomeSpawns.push({
          category: String(category.getName()), id: String(R.ENTITY_TYPE.getKey(spawn.type)),
          min: spawn.minCount, max: spawn.maxCount, weight: spawn.getWeight().asInt()}))
      } catch (e) { result.errors.push('spawn category ' + category + ': ' + e) }
    })
    p.getActiveEffects().forEach(e => row.effects.push({id: String(R.MOB_EFFECT.getKey(e.getEffect().value())), duration: e.getDuration(), amplifier: e.getAmplifier()}))
    let inv = p.getInventory()
    for (let i = 0; i < inv.getContainerSize(); i++) {
      let stack = inv.getItem(i)
      if (!stack.isEmpty()) row.inventory.push({slot: i, id: String(R.ITEM.getKey(stack.getItem())), count: stack.getCount()})
    }
  } catch (e) { result.errors.push('world snapshot: ' + e) }
  server.getCommands().getDispatcher().getRoot().getChildren().forEach(c => result.commands.push(String(c.getName())))
  server.getRecipeManager().getRecipes().forEach(holder => {
    let row = {id: String(holder.id())}
    try {
      let recipe = holder.value()
      row.type = String(R.RECIPE_TYPE.getKey(recipe.getType()))
      let out = recipe['getResultItem(net.minecraft.core.HolderLookup$Provider)'](server.registryAccess())
      row.output = String(R.ITEM.getKey(out.getItem()))
      row.count = out.getCount()
    } catch (e) { row.error = String(e).slice(0, 300) }
    result.recipes.push(row)
  })
  JsonIO.write('kubejs/export/tmm-world-observe.json', result)
  return result
}
ServerEvents.basicCommand('tmm_world_observe', event => {
  if (!event.player || !event.player.hasPermissions(2)) { event.cancel('Operator permission required'); return }
  let result = tmmObserveWorld(event.player, event.server)
  event.cancel('TMM world observation saved; ' + result.errors.length + ' limitations')
})
