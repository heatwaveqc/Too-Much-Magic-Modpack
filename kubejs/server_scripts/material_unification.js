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

  // Do NOT add extra furnace recipes here: the already-loaded, AU-unified
  // Primal Magick iron-grit furnace recipes and Neo Vitae gold-dust recipes
  // each accept the *full* c:dusts/iron or c:dusts/gold tag, including
  // Mahou powders and Occultism dusts. Adding tmm:* tag-wide furnace
  // recipes created four overlapping Polymorph groups, with no new inputs.

  // Remove the obvious ingot -> dust shortcuts from the two smeltable metals.
  // Audit all other crushing routes against the exact jar before release.
  event.remove({ id: 'occultism:crushing/iron_dust_from_ingot' })
  event.remove({ id: 'occultism:crushing/gold_dust_from_ingot' })
})
