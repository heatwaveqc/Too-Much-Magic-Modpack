// Too Much Magic: conventional materials only. Gate special ores independently.
// GAMEPLAY TEST CANDIDATE; see docs/MATERIAL-UNIFICATION.md.
ServerEvents.tags('item', event => {
  // Mahou powders stay canonical so item-specific magical uses still work.
  for (const material of ['iron', 'gold', 'diamond', 'emerald', 'quartz']) {
    event.add('c:dusts/' + material, 'mahoutsukai:powdered_' + material)
  }
})

ServerEvents.recipes(event => {
  // Preserve native Mahou recipes/tool remainders; change only ingot -> raw metal.
  for (const [material, ingot] of [['iron', 'minecraft:iron_ingot'], ['gold', 'minecraft:gold_ingot']]) {
    for (const suffix of ['', '_2']) {
      event.replaceInput(
        { id: 'mahoutsukai:powdered_' + material + suffix },
        ingot,
        Ingredient.of('#c:raw_materials/' + material)
      )
    }
  }

  // Gem dust may be reconstituted by other mods; prevent gem -> 2 dust -> 2 gems.
  // 1:1 hammer conversions stay intact; the 2x mortar now requires a silk-touch ore.
  for (const [material, gem, oreTag] of [
    ['diamond', 'minecraft:diamond', '#c:ores/diamond'],
    ['emerald', 'minecraft:emerald', '#c:ores/emerald'],
    ['quartz', 'minecraft:quartz', '#c:ores/quartz']
  ]) {
    event.replaceInput(
      { id: 'mahoutsukai:powdered_' + material + '_2' },
      gem,
      Ingredient.of(oreTag)
    )
  }

  // Existing Neo Vitae / Occultism dusts also use these tags.
  event.smelting('minecraft:iron_ingot', '#c:dusts/iron').id('tmm:iron_dust_smelting')
  event.blasting('minecraft:iron_ingot', '#c:dusts/iron').id('tmm:iron_dust_blasting')
  event.smelting('minecraft:gold_ingot', '#c:dusts/gold').id('tmm:gold_dust_smelting')
  event.blasting('minecraft:gold_ingot', '#c:dusts/gold').id('tmm:gold_dust_blasting')

  // Remove the obvious ingot -> dust shortcuts from the two smeltable metals.
  // Audit all other crushing routes against the exact jar before release.
  event.remove({ id: 'occultism:crushing/iron_dust_from_ingot' })
  event.remove({ id: 'occultism:crushing/gold_dust_from_ingot' })
})
