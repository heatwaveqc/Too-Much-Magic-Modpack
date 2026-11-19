// Parchment is bonus treasure, not a replacement for the regular catch.
LootJS.lootTables(event => {
  event.getLootTable("minecraft:gameplay/fishing/treasure").createPool(pool => {
    pool.addEntry(LootEntry.of("improvableskills:parchment_fragment").randomChance(0.10))
  })
})
