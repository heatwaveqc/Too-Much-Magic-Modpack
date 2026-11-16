// Preserve the native iron-tier mining limit and Eidolon enchantability.
// Only improve the five Eidolon tools' durability/speed to the chosen composite.
ItemEvents.modification(event => {
  for (const slot of ['sword', 'axe', 'pickaxe', 'shovel', 'hoe']) {
    event.modify('eidolon_repraised:silver_' + slot, item => {
      item.setTier(tier => {
        tier.setUses(250)
        tier.setSpeed(7.6)
      })
    })
  }
})
