// Too Much Magic — KubeJS 2101.7.2 / NeoForge 1.21.1.
// Iron's Blood Vial is a plain Item and cannot be consumed natively.
// Only add ordinary vanilla food-use semantics and a glass-bottle remainder.
// The existing first-party JAWR bottle bridge handles native JAWR blood pool
// credit and Hexerei-style medicine ONCE on successful drink completion.
// Do not modify JAWR's own WineGlassItem food properties or special components;
// that item has a custom finishUsingItem and vampire progression logic.
// Hexerei Blood Bottle is already edible and has native medicine effects.
// Requires a full client/server restart; /reload does not apply item changes.
ItemEvents.modification(event => {
  event.modify('irons_spellbooks:blood_vial', item => {
    item.foodProperties = food => {
      food.nutrition(1)
      food.saturation(1)
      food.alwaysEdible()
      food.usingConvertsTo(Item.of('minecraft:glass_bottle'))
    }
  })
})
