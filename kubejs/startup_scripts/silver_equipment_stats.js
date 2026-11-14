// Too Much Magic: silver equipment uses the best *relevant* base stats.
// Changes to item defaults require a complete client/server restart.
// ItemEvents.modification is a KubeJS startup event, not a reloadable recipe.
//
// We preserve the original iron harvest restriction and attack/enchant values;
// only the existing tool tier's durability and mining speed are updated.
// ArmorMaterial enchantability is a separate, known KubeJS gap.
ItemEvents.modification(event => {
  for (const slot of ['sword', 'axe', 'pickaxe', 'shovel', 'hoe']) {
    const id = 'eidolon_repraised:silver_' + slot
    event.modify(id, item => {
      // KubeJS's 2101 ItemModificationKubeEvent#setTier mutates the
      // existing TieredItem and refreshes maxDamage + digger Tool component.
      // Unlike a bare data-component change, this makes the mining speed
      // reflect the revised native tier. Preserve iron harvest capability,
      // attack bonus 2 and enchantability 20 from Eidolon's silver tier.
      // Verify against the *pinned* 2101.7.2-build.381 at server launch.
      item.setTier(tier => {
        tier.setUses(250)
        tier.setSpeed(7.6)
      })
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
