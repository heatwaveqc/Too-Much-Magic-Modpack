// Tiny vanilla-item quality-of-life tweaks for Too Much Magic (MC 1.21.1).
// Keep this in startup_scripts: default item components are frozen at startup.
ItemEvents.modification(event => {
  // Blaze rods come from a creature made of fire. A dropped rod should survive it.
  event.modify('minecraft:blaze_rod', item => {
    item.setFireResistant()
  })

  // Mushroom stew can travel in useful quantities; vanilla 1.21 handles bowl returns.
  event.modify('minecraft:mushroom_stew', item => {
    item.setMaxStackSize(16)
  })
})
