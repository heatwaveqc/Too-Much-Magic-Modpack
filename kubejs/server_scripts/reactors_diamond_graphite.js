// Too Much Magic — make coal/charcoal usable as furnace fuel again.
// BiggerReactors Rework 0.1.6 registers its coal/charcoal -> graphite ingot
// recipes as regular furnace AND blasting recipes. Those cause Minecraft to
// classify coal/charcoal as valid input ingredients when shift-clicking.
//
// Player design: one diamond -> one graphite ingot. Remove BOTH offending
// recipes, retain the original cooking time and XP, and leave the graphite
// dust -> graphite ingot smelting/blasting recipes untouched.
// Use the pinned fork's exact registry IDs (not the old biggerreactors namespace).

ServerEvents.recipes(event => {
  event.remove({ id: 'biggerreactorsrework:smelting/graphite_ingot' })
  event.remove({ id: 'biggerreactorsrework:blasting/graphite_ingot' })

  const input = { item: 'minecraft:diamond' }
  const result = { id: 'biggerreactorsrework:graphite_ingot' }

  event.custom({
    type: 'minecraft:smelting',
    ingredient: input,
    result: result,
    experience: 0.1,
    cookingtime: 200
  }).id('tmm:reactors/diamond_graphite_smelting')

  event.custom({
    type: 'minecraft:blasting',
    ingredient: input,
    result: result,
    experience: 0.1,
    cookingtime: 100
  }).id('tmm:reactors/diamond_graphite_blasting')
})
