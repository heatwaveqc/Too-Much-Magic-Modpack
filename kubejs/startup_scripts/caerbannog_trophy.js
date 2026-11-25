// Prototype: unique commemorative item; has no crafting use or inherent EMC value.
StartupEvents.registry('item', event => {
  event.create('chaos_bunny_trophy')
    .displayName('Chaos Bunny of Caerbannog Trophy')
    .maxStackSize(1)
    .glow(true)
})
