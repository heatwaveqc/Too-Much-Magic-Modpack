// TMM cross-school ink production: Occultism 1.227.0 + Iron's Spells 3.16.3.
// Proposed balance; native ink alchemy unchanged. Runtime acceptance pending.
// RitualRecipe requires ritual_dummy; use the corresponding actual ink as a
// display-only stack, avoiding additional registry items. Verify JEI and book
// lookup before integration; the actual grant is only the result stack.
ServerEvents.recipes(event => {
  const inks = [
    { rarity: 'common', spirit: 'foliot', ticks: 60, quantity: 4, offering: 'minecraft:copper_ingot' },
    { rarity: 'uncommon', spirit: 'foliot', ticks: 60, quantity: 2, offering: 'minecraft:lapis_lazuli' },
    { rarity: 'rare', spirit: 'djinni', ticks: 150, quantity: 2, offering: 'minecraft:amethyst_shard' },
    { rarity: 'epic', spirit: 'afrit', ticks: 300, quantity: 1, offering: 'minecraft:diamond' },
    { rarity: 'legendary', spirit: 'marid', ticks: 510, quantity: 1, offering: 'occultism:iesnium_ingot' }
  ]
  inks.forEach(ink => {
    const output = 'irons_spellbooks:' + ink.rarity + '_ink'
    event.custom({
      type: 'occultism:ritual',
      activation_item: { item: 'occultism:book_of_binding_bound_' + ink.spirit },
      duration: ink.ticks,
      ingredients: [
        { item: 'occultism:purified_ink' },
        { item: 'irons_spellbooks:arcane_essence' },
        { item: ink.offering }
      ],
      pentacle_id: 'occultism:craft_' + ink.spirit,
      result: { count: ink.quantity, id: output },
      ritual_dummy: { count: 1, id: output },
      ritual_type: 'occultism:craft'
    }).id('tmm:ritual/occultism_ink_' + ink.rarity)
  })
})
