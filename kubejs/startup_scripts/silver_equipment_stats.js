// Too Much Magic: silver equipment uses the best *relevant* base stats.
// Changes to item defaults require a complete client/server restart.
// ItemEvents.modification is a KubeJS startup event, not a reloadable recipe.
//
// We deliberately preserve the native tool tiers/harvest restrictions and do
// not attempt broken 1.21.1 item.tier or ArmorMaterial enchantability changes.
ItemEvents.modification(event => {
  for (const slot of ['sword', 'axe', 'pickaxe', 'shovel', 'hoe']) {
    const id = 'eidolon_repraised:silver_' + slot
    event.modify(id, item => {
      // From Werewolves: 250 durability (vs Eidolon's native 193).
      item.maxDamage = 250
      if (slot !== 'sword') {
        // From Embers: 7.6 mining speed (vs Eidolon's native 7.0).
        // 1.21.1 direct setter; does NOT modify the underlying mining tier.
        item.digSpeed = 7.6
      }
    })
  }

  // Werewolves' 15 points of full-set armor defense and the native class-
  // based silver retaliation already beat Eidolon's armor; retain those.
  // Match or exceed Eidolon's per-slot durability where valuable.
  for (const [slot, maxDamage] of [
    ['helmet', 240], ['chestplate', 320],
    ['leggings', 300], ['boots', 260]
  ]) {
    event.modify('werewolves:silver_' + slot, item => {
      item.maxDamage = maxDamage
    })
  }
  // BLOCKER: changing Werewolves ArmorMaterial enchantability 9 -> 20
  // cannot be done through the supported KubeJS 1.21.1 item modifications.
  // Requires a verified Java compat bridge or conscious acceptance of 9.
})
