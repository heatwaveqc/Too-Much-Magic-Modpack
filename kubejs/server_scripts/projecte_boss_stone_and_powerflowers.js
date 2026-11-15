// Too Much Magic — ProjectE progression through interchangeable magic-boss loot.
// Minecraft 1.21.1 / NeoForge / KubeJS 2101.x.
// This is a RECIPE gate only: traded/EMC-duplicated ingredients are allowed.
// See docs/PROJECTE-RECIPE-GATES.md for limits and required in-world QA.
ServerEvents.recipes(event => {
  // ProjectE PE1.1.0 ships two orientations, both with a diamond at center.
  // Remove BOTH before creating the two orientations with the trophy tag.
  event.remove({ id: 'projecte:philosophers_stone' })
  event.remove({ id: 'projecte:philosophers_stone_alt' })

  const stoneKey = {
    R: '#c:dusts/redstone',
    G: '#c:dusts/glowstone',
    T: '#tmm:philosophers_stone_boss_trophies'
  }
  // Keep the exact eight vanilla-equivalent ingredients in their original spots.
  event.shaped('projecte:philosophers_stone', [
    'RGR',
    'GTG',
    'RGR'
  ], stoneKey).id('tmm:projecte/philosophers_stone_boss_outer_red')
  event.shaped('projecte:philosophers_stone', [
    'GRG',
    'RTR',
    'GRG'
  ], stoneKey).id('tmm:projecte/philosophers_stone_boss_outer_glow')

  // Project Expansion PE 1.0.6: basic=MK1, dark=MK2, RED=MK3.
  // Any MK3+ flower crafted directly requires a Chaos Shard in the
  // bottom-center relay position. A direct high-tier recipe otherwise
  // bypasses the MK3 bridge entirely; replace all such direct recipes.
  const tiersFromMk3 = [
    'red', 'magenta', 'pink', 'purple', 'violet', 'blue', 'cyan',
    'green', 'lime', 'yellow', 'orange', 'white', 'fading', 'final'
  ]
  for (const tier of tiersFromMk3) {
    event.remove({ id: 'projectexpansion:power_flower/' + tier })
    event.shaped('projectexpansion:' + tier + '_power_flower', [
      'CLC',
      'RRR',
      'RSR'
    ], {
      C: 'projectexpansion:' + tier + '_compressed_collector',
      L: 'projectexpansion:' + tier + '_emc_link',
      R: 'projectexpansion:' + tier + '_relay',
      S: 'draconicevolution:chaos_shard'
    }).id('tmm:projectexpansion/power_flower/' + tier + '_chaos_direct')
  }

  // Red has a separate upgrade recipe from Dark. Require the shard on
  // this alternative too, or a player can craft MK3 without the shard.
  event.remove({ id: 'projectexpansion:power_flower/red_upgrade' })
  event.shaped('projectexpansion:red_power_flower', [
    'CLC',
    'RPR',
    'RSR'
  ], {
    C: 'projectexpansion:red_compressed_collector',
    L: 'projectexpansion:red_emc_link',
    R: 'projectexpansion:red_relay',
    P: 'projectexpansion:dark_power_flower',
    S: 'draconicevolution:chaos_shard'
  }).id('tmm:projectexpansion/power_flower/red_chaos_upgrade')

  // All other upgrades still require the previous tier's Power Flower.
  // Project Expansion's Matter Upgrader item and EMC duplication are NOT
  // controlled by recipe edits; see the separate bypass audit.
})
