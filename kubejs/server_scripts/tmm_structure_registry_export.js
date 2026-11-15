// Operator-only structure registry inspection for B18 Ars Additions locator build inputs.
// Usage: /tmm_structure_registry ; copy the JSON after TMM_STRUCTURE_REGISTRY_JSON=
// from logs/latest.log. This does NOT automatically add recipes or modify the world.
// Runtime acceptance: ensure Registry.of resolves data-pack STRUCTURE entries on this KubeJS version.
ServerEvents.basicCommand('tmm_structure_registry', event => {
  try {
    const registry = Registry.of('minecraft:worldgen/structure')
    const ids = []
    registry.getKeys().forEach(key => ids.push(String(key)))
    ids.sort()
    console.info('TMM_STRUCTURE_REGISTRY_JSON=' + JSON.stringify({ structures: ids }))
    event.cancel('Logged ' + ids.length + ' registered structures to latest.log; copy TMM_STRUCTURE_REGISTRY_JSON into a .json file for the Ars locator compiler.')
  } catch (error) {
    console.error('TMM structure registry export failed: ' + error)
    event.cancel('Structure registry export failed; inspect latest.log.')
  }
})
