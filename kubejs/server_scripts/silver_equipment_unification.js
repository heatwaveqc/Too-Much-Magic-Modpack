// Too Much Magic — one normal crafting result per silver equipment slot.
// Keep other mods' registered items, loot and special crafting routes intact.
// See docs/SILVER-EQUIPMENT-REVIEW.md for the source-backed mechanics.
//
// Werewolves checks tools/silver TAG on attack (Silver effect), and checks
// type/silver/ISilverItem on the werewolf wielding or wearing silver.
// Native Werewolves armor is the canonical armor because attacker retaliation
// checks `instanceof ISilverItem`, not a tag.
ServerEvents.tags('item', event => {
  for (const slot of ['sword', 'pickaxe', 'axe', 'shovel', 'hoe']) {
    // Eidolon's sword already burns undead; this tag adds the native
    // Werewolves Silver effect WITHOUT adding a duplicate damage handler.
    event.add('werewolves:tools/silver', 'eidolon_repraised:silver_' + slot)
    // Retain silver's danger and efficacy on legacy Embers gear from loot,
    // creative inventories and old world saves.
    event.add('werewolves:tools/silver', 'embers:silver_' + slot)
  }
  for (const slot of ['helmet', 'chestplate', 'leggings', 'boots']) {
    // Legacy Eidolon armor counts as silver to wearers. The Werewolves
    // retaliation against attacking werewolves still requires ISilverItem,
    // so canonical craftable armor remains native Werewolves equipment.
    event.add('werewolves:armor/silver', 'eidolon_repraised:silver_' + slot)
  }
})

ServerEvents.recipes(event => {
  // Keep native Eidolon recipes for every silver tool (including the
  // 5-second undead-igniting sword). Embers and Werewolves registered
  // tools remain valid legacy items; remove only their ordinary recipes.
  for (const slot of ['sword', 'pickaxe', 'axe', 'shovel', 'hoe']) {
    event.remove({ id: 'embers:silver_' + slot })
    event.remove({ id: 'werewolves:silver_' + slot })
  }

  // Keep Werewolves native armor (ISilverItem + wearing/retaliation rules).
  // Eidolon armor items remain registered, but not directly craftable.
  for (const slot of ['helmet', 'chestplate', 'leggings', 'boots']) {
    event.remove({ id: 'eidolon_repraised:silver_' + slot })
  }
  console.info('[TMM silver] Native Eidolon tools + Werewolves armor selected; 14 duplicate recipe IDs removed')
})
