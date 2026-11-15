#!/usr/bin/env node
// Static KubeJS fixture for the BiggerReactors Rework graphite furnace override.
// This validates emitted recipe IDs and exact ingredients, not Minecraft UX.
'use strict'
const vm = require('node:vm')
const fs = require('node:fs')
const assert = require('node:assert/strict')
const file = 'kubejs/server_scripts/reactors_diamond_graphite.js'
const script = fs.readFileSync(file, 'utf8')
const removed = [], added = []
vm.runInNewContext(script, {
  ServerEvents: { recipes(fn) { fn({
    remove(filter) { removed.push(filter.id) },
    custom(data) { const out = { ...data }; added.push(out); return { id(id) { out.recipeId = id } } }
  }) }}
}, { filename: file })
assert.deepEqual(removed.sort(), [
  'biggerreactorsrework:blasting/graphite_ingot',
  'biggerreactorsrework:smelting/graphite_ingot'
])
assert.equal(added.length, 2)
for (const recipe of added) {
  assert.equal(recipe.ingredient.item, 'minecraft:diamond')
  assert.equal(recipe.result.id, 'biggerreactorsrework:graphite_ingot')
  assert.equal(recipe.experience, 0.1)
  assert.ok(!JSON.stringify(recipe).includes('minecraft:coal'))
  assert.ok(!JSON.stringify(recipe).includes('minecraft:charcoal'))
}
const byType = Object.fromEntries(added.map(r => [r.type, r]))
assert.equal(byType['minecraft:smelting'].cookingtime, 200)
assert.equal(byType['minecraft:blasting'].cookingtime, 100)
assert.equal(byType['minecraft:smelting'].recipeId, 'tmm:reactors/diamond_graphite_smelting')
assert.equal(byType['minecraft:blasting'].recipeId, 'tmm:reactors/diamond_graphite_blasting')
assert.ok(!removed.includes('biggerreactorsrework:smelting/graphite_dust'))
assert.ok(!removed.includes('biggerreactorsrework:blasting/graphite_dust'))
console.log('PASS: graphite smelting/blasting now diamond-only; dust processing recipes retained')
console.log('Note: furnace shift-click behavior still requires live client test.')
