// Too Much Magic — targeted 1.21.1 furnace / compaction deduplication.
// Ground truth: actual Almost Unified before/after dump + Polymorph's loaded
// recipe manager, docs/MATERIAL-PROCESSING-AUDIT.md. Never remove by output.
// All original items remain registered for legacy saves and mod-specific uses.
ServerEvents.recipes(event => {
  // 9 copper nuggets -> 1 vanilla ingot. Embers already has a recipe accepting
  // the full #c:nuggets/copper tag, including Malum's nuggets.
  event.remove({ id: 'malum:copper_ingot_from_nugget' })

  // AU canonicalizes both mod outputs to Occultism. Eidolon's retained
  // 9x silver ingot/nugget/raw compaction recipes accept c: tags *after AU*.
  // These Werewolves duplicates differ only in recipe JSON metadata/labels.
  for (const id of [
    'werewolves:silver_block_from_silver_ingot',
    'werewolves:raw_silver_block_from_raw_silver_ingot',
    'werewolves:silver_ingot_from_nuggets'
  ]) {
    event.remove({ id: id })
  }

  // Embers' retained ore/raw silver furnace recipes now use c:ores/silver
  // and c:raw_materials/silver after AU, producing occultism:silver_ingot.
  // Werewolves' recipes are identical in ingredient, output, time and XP.
  for (const id of [
    'werewolves:silver_ingot_from_smelting_raw_silver',
    'werewolves:silver_ingot_from_smelting_deepslate_silver_ore',
    'werewolves:silver_ingot_from_blasting_raw_silver',
    'werewolves:silver_ingot_from_blasting_deepslate_silver_ore'
  ]) {
    event.remove({ id: id })
  }

  // Neo Vitae's dynamically generated silver/lead dust smelting+blasting
  // recipes accept the complete #c:dusts/silver and #c:dusts/lead tags after
  // AU, output the canonical ingots and grant 0.7 XP instead of 0.5 XP.
  for (const id of [
    'eidolon_repraised:smelt_silver_dust',
    'eidolon_repraised:blast_silver_dust',
    'eidolon_repraised:smelt_lead_dust',
    'eidolon_repraised:blast_lead_dust'
  ]) {
    event.remove({ id: id })
  }

  // The same Neo Vitae dust has two native implementations. Keep the current
  // config-generated one with 1.0 XP; remove the older 0.7-XP version.
  event.remove({ id: 'neovitae:smelting/hellforged_ingot_from_dust' })
  event.remove({ id: 'neovitae:blasting/hellforged_ingot_from_dust' })

  // Netherite dust's Occultism recipe accepts the full c:dusts tag at 0.7 XP,
  // while Neo Vitae's accepts its own dust at 2 XP. Keep *both* item inputs
  // and the 2.0 XP progression by explicitly defining one tag-based recipe.
  for (const id of [
    'occultism:smelting/netherite_scrap_from_dust',
    'occultism:blasting/netherite_scrap_from_dust',
    'neovitae:materials/smelting_netherite_scrap_dust',
    'neovitae:materials/blasting_netherite_scrap_dust'
  ]) {
    event.remove({ id: id })
  }
  event.custom({
    type: 'minecraft:smelting', category: 'misc',
    ingredient: { tag: 'c:dusts/netherite_scrap' },
    result: { id: 'minecraft:netherite_scrap', count: 1 },
    experience: 2.0, cookingtime: 200
  }).id('tmm:netherite_scrap_dust_smelting')
  event.custom({
    type: 'minecraft:blasting', category: 'misc',
    ingredient: { tag: 'c:dusts/netherite_scrap' },
    result: { id: 'minecraft:netherite_scrap', count: 1 },
    experience: 2.0, cookingtime: 100
  }).id('tmm:netherite_scrap_dust_blasting')

  // Both Enchanted and Pastel smelt *vanilla calcite* in an ordinary
  // furnace, but to different products: Enchanted quicklime vs Pastel's
  // decorative polished calcite. Keep Enchanted's quicklime smelting,
  // Pastel's existing blast-furnace polishing, and add a natural stonecutter
  // route for polished calcite. Polymorph is no longer required.
  event.remove({ id: 'pastel:smelting/calcite_to_polished_calcite' })
  event.stonecutting('pastel:polished_calcite', 'minecraft:calcite')
    .id('tmm:stonecutting/polished_calcite')

  // Botania's white iridescent log already belongs to the vanilla
  // #minecraft:logs_that_burn -> charcoal smelting route, same 0.15 XP.
  event.remove({ id: 'botania:botania_extras/smelting/charcoal_white_iridescent' })
  // Pink iridescent logs also already smelt via the same vanilla tag
  // into exactly 1 charcoal, 0.15 XP, 200 ticks.
  event.remove({ id: 'botania:botania_extras/smelting/charcoal_pink_iridescent' })

  console.info('[TMM processing] Deduplicated copper/silver compaction, silver/lead/hellforged/netherite dust and ore furnace recipes; preserved special dimension outputs')
})
