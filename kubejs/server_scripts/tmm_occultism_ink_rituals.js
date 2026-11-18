// TMM cross-school ink production: Occultism 1.227.0 + Iron's Spells 3.16.3.
// Owner-approved directions: renewable common ink, 4:2 subsequent upgrades.
// Native ink alchemy unchanged. Runtime acceptance pending.
// Use the same craft pentacles and durations as native storage stabilizers.
// RitualRecipe requires ritual_dummy; use the actual ink as a display stack.
// Verify JEI/book lookup; the only granted output is the result stack.
ServerEvents.recipes(event => {
  const inks = [
    { rarity: 'common', spirit: 'foliot', ticks: 60, quantity: 4,
      offerings: ['occultism:purified_ink', 'irons_spellbooks:arcane_essence', 'minecraft:copper_ingot'] },
    { rarity: 'uncommon', spirit: 'foliot', ticks: 60, quantity: 2, previous: 'common' },
    { rarity: 'rare', spirit: 'djinni', ticks: 150, quantity: 2, previous: 'uncommon' },
    { rarity: 'epic', spirit: 'afrit', ticks: 300, quantity: 2, previous: 'rare' },
    { rarity: 'legendary', spirit: 'marid', ticks: 510, quantity: 2, previous: 'epic' }
  ]
  inks.forEach(ink => {
    const output = 'irons_spellbooks:' + ink.rarity + '_ink'
    const offerings = ink.offerings || [
      'irons_spellbooks:' + ink.previous + '_ink',
      'irons_spellbooks:' + ink.previous + '_ink',
      'irons_spellbooks:' + ink.previous + '_ink',
      'irons_spellbooks:' + ink.previous + '_ink'
    ]
    event.custom({
      type: 'occultism:ritual',
      activation_item: { item: 'occultism:book_of_binding_bound_' + ink.spirit },
      duration: ink.ticks,
      ingredients: offerings.map(item => ({ item: item })),
      pentacle_id: 'occultism:craft_' + ink.spirit,
      result: { count: ink.quantity, id: output },
      ritual_dummy: { count: 1, id: output },
      ritual_type: 'occultism:craft'
    }).id('tmm:ritual/occultism_ink_' + ink.rarity)
  })
})
