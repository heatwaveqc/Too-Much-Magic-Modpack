// Temporary operator-only capture. Does not grant items or change gameplay data.
ServerEvents.basicCommand('tmm_survey_capture', event => {
  let R = Java.loadClass('net.minecraft.core.registries.BuiltInRegistries')
  let P = Java.loadClass('moze_intel.projecte.api.proxy.IEMCProxy')
  let M = Java.loadClass('moze_intel.projecte.emc.EMCMappingHandler')
  let I = Java.loadClass('moze_intel.projecte.api.ItemInfo')
  let result = {schema: 1, items: [], entities: [], structures: [], recipes: [], mappedEntries: [], errors: [], emcMapSize: M.getEmcMapSize(), emcLoadIndex: M.getLoadIndex()}
  R.ITEM.entrySet().forEach(entry => {
    let item = entry.getValue()
    let row = {id: String(entry.getKey().location())}
    try {
      let stack = item.getDefaultInstance()
      row.name = String(stack.getHoverName().getString())
      row.emc = String(P.INSTANCE['getValue(net.minecraft.world.item.ItemStack)'](stack))
      row.hasStoredDefaultEntry = M.hasEmcValue(I.fromStack(stack))
      row.tags = []
      item.builtInRegistryHolder().tags().forEach(t => row.tags.push(String(t.location())))
    } catch (e) { row.error = String(e) }
    result.items.push(row)
  })
  R.ENTITY_TYPE.entrySet().forEach(entry => {
    let entity = entry.getValue()
    let row = {id: String(entry.getKey().location())}
    try { row.name = String(entity.getDescription().getString()); row.category = String(entity.getCategory().getName()) } catch(e) { row.error = String(e) }
    result.entities.push(row)
  })
  try {
    let K = Java.loadClass('net.minecraft.core.registries.Registries')
    event.server.registryAccess().registryOrThrow(K.STRUCTURE).keySet().forEach(k => result.structures.push(String(k)))
  } catch(e) {result.errors.push('structures: ' + e)}
  try { M.getMappedItems().forEach(info => result.mappedEntries.push({key: String(info), emc: String(M.getStoredEmcValue(info))})) } catch(e) {result.errors.push('mappedEntries: ' + e)}
  try {
    event.server.getRecipeManager().getRecipes().forEach(holder => {
      let row = {id: String(holder.id())}
      let recipe = holder.value()
      try {
        row.type = String(R.RECIPE_TYPE.getKey(recipe.getType()))
        let output = recipe['getResultItem(net.minecraft.core.HolderLookup$Provider)'](event.server.registryAccess())
        row.output = {id: String(R.ITEM.getKey(output.getItem())), count: output.getCount()}
        row.ingredients = []
        recipe.getIngredients().forEach(ingredient => {
          let choices = []
          let stacks = ingredient.getStackArray()
          let n = Math.min(stacks.length, 128)
          for (let i = 0; i < n; i++) choices.push({id: String(R.ITEM.getKey(stacks[i].getItem())), count: stacks[i].getCount()})
          row.ingredients.push({choices: choices, totalAlternatives: stacks.length, truncated: stacks.length > n})
        })
      } catch(e) {row.error = String(e)}
      result.recipes.push(row)
    })
  } catch(e) {result.errors.push('recipes: ' + e)}
  JsonIO.write('kubejs/export/tmm-runtime-survey.json', result)
  console.info('TMM_SURVEY_CAPTURE items=' + result.items.length + ' entities=' + result.entities.length + ' structures=' + result.structures.length + ' emcMapSize=' + result.emcMapSize)
  event.cancel('Survey written to kubejs/export/tmm-runtime-survey.json')
})
