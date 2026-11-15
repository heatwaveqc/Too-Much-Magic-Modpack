// Offline contract only: no client/server worldgen simulation.
// npm-free Node test; recognizes actual KubeJS callback structure.
const fs = require('node:fs')
const vm = require('node:vm')
const assert = require('node:assert/strict')
const gates = JSON.parse(fs.readFileSync('manifest/ore-source-gates-draft.json', 'utf8'))
const disabled = JSON.parse(fs.readFileSync('kubejs/data/biggerreactorsrework/neoforge/biome_modifier/overworld_ores.json', 'utf8'))
assert.equal(disabled.type, 'neoforge:none')
const source = fs.readFileSync('kubejs/server_scripts/toxic_caves_reactor_uranium.js', 'utf8')
const recipes = []
vm.runInNewContext(source, {
  Item: { of: value => value },
  ServerEvents: { recipes(fn) {
    fn({ shapeless(result, ingredients) {
      const recipe = { result, ingredients }
      recipes.push(recipe)
      return { id(value) { recipe.id = value } }
    } })
  } }
}, {filename:'toxic_caves_reactor_uranium.js'})
assert.equal(recipes.length, 1)
assert.equal(recipes[0].result, 'biggerreactorsrework:uranium_ingot')
assert.equal(recipes[0].ingredients.length, 1)
assert.equal(recipes[0].ingredients[0], 'alexscaves:uranium')
assert.equal(recipes[0].id, 'tmm:reactors/toxic_caves_uranium_ingot')
assert.equal(gates.resources.length, 2)
assert.equal(gates.resources[0].natural_generation_allowed.length, 0)
const [allow] = gates.resources[1].natural_generation_allowed
assert.equal(allow.dimension, 'minecraft:overworld')
assert.equal(allow.biome, 'alexscaves:toxic_caves')
assert.equal(allow.feature, 'alexscaves:radrock_uranium_ore')
assert.equal(gates.status, 'draft_source_staged_not_integrated')
console.log('PASS: reactor uranium worldgen disabled by exact modifier override; native Toxic Caves allowlist and one-way conversion staged')
console.log('NOT LIVE VERIFIED: dimension exclusion, native cave generation, item recipes, JER ore graphs')
