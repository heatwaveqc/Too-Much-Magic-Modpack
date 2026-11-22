// Parchment is an ultra-rare *ordinary treasure result*, never an extra catch.
// We modify the already-loaded pool rather than replacing vanilla/modded entries.
// LootJS 3.7.0: https://docs.almostreliable.com/lootjs/loot-tables/modify-loot-table.html
// See #318 for the ~0.5% of treasure outcomes target (hard ceiling 1%).
const TMM_PARCHMENT_TREASURE_ID = 'improvableskills:parchment_fragment'
const TMM_PARCHMENT_TREASURE_SHARE = 0.005
const TMM_PARCHMENT_TREASURE_TABLE = 'minecraft:gameplay/fishing/treasure'
const TMM_PARCHMENT_MAX_LOOT_WEIGHT = 2147483647

LootJS.lootTables(event => {
  const table = event.getLootTable(TMM_PARCHMENT_TREASURE_TABLE)
  if (table == null) {
    console.warn('[TMM Parchment] Fishing treasure table is missing; leaving loot unchanged')
    return
  }

  // This pool is selected by the native fishing loot table, so all native
  // open-water, Luck of the Sea and mod-added fishing conditions still apply.
  const pool = table.firstPool()
  const originals = []
  let totalWeight = 0

  for (const entry of pool.getEntries()) {
    // Composite entries cannot be safely treated as a single weighted result.
    // Fail closed rather than unexpectedly overriding a mod's alternatives.
    if (!entry.isSimple()) {
      console.warn('[TMM Parchment] Composite fishing treasure entry: no changes applied')
      return
    }
    if (entry.isItem() && String(entry.getItem().id) === TMM_PARCHMENT_TREASURE_ID) {
      console.warn('[TMM Parchment] Fragment already in fishing treasure pool: skipping duplicate')
      return
    }

    const weight = Number(entry.getWeight())
    const quality = Number(entry.getQuality())
    if (!Number.isSafeInteger(weight) || weight < 0 ||
        !Number.isSafeInteger(quality) || totalWeight + weight > TMM_PARCHMENT_MAX_LOOT_WEIGHT) {
      console.warn('[TMM Parchment] Invalid fishing treasure weights: no changes applied')
      return
    }
    totalWeight += weight
    originals.push({entry, weight, quality})
  }

  if (totalWeight === 0) {
    console.warn('[TMM Parchment] Empty/disabled fishing treasure pool: no changes applied')
    return
  }

  // Vanilla 1.21.1 has six equal-weight treasure entries. With 34x each,
  // the fragment's one weight is 1 / (6 * 34 + 1) = 0.4878% of treasure.
  // Calculate the multiplier from the *loaded* pool, so extra mod entries
  // retain their relative shares while the parchment remains similarly rare.
  const multiplier = Math.max(1, Math.ceil((1 / TMM_PARCHMENT_TREASURE_SHARE - 1) / totalWeight))
  for (const original of originals) {
    if (original.weight * multiplier > TMM_PARCHMENT_MAX_LOOT_WEIGHT ||
        Math.abs(original.quality * multiplier) > TMM_PARCHMENT_MAX_LOOT_WEIGHT) {
      console.warn('[TMM Parchment] Fishing treasure weight overflow: no changes applied')
      return
    }
  }

  // Scale *every* existing entry's weight and quality together: no entry is
  // removed, and old relative weights are preserved, including modded loot.
  // Adding one new weight to the SAME pool displaces a regular treasure item;
  // it never produces a bonus item or an empty result from a failed chance.
  for (const original of originals) {
    original.entry.setWeight(original.weight * multiplier)
    original.entry.setQuality(original.quality * multiplier)
  }
  pool.addEntry(LootEntry.of(TMM_PARCHMENT_TREASURE_ID).withWeight(1))
  console.info('[TMM Parchment] Weighted treasure entry installed: ' +
    (100 / (totalWeight * multiplier + 1)).toFixed(3) + '% at zero Luck')
})
