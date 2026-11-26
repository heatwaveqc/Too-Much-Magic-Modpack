#!/usr/bin/env node
'use strict'
// Mock contract tests; Rhino/LootJS runtime acceptance still requires real chest loot.
const assert = require('node:assert/strict')
const fs = require('node:fs')
const vm = require('node:vm')
const path = require('node:path')
const source = fs.readFileSync(path.join(__dirname, '..', 'kubejs/server_scripts/zz_reactive_loot_books.js'), 'utf8')
const STORED = 'stored', CASTER = 'caster', BOOK = {}, OTHER = {}
const location = s => ({ equals: other => other.id === s, id: s })
const holder = (id, min = 1, max = 3) => ({
  unwrapKey: () => ({ isPresent: () => true, get: () => ({ location: () => location(id) }) }),
  value: () => ({ getMinLevel: () => min, getMaxLevel: () => max }),
  is: () => { throw Error('ambiguous Holder.is') }
})
const reactive = holder('ars_nouveau:reactive'), sharp = holder('minecraft:sharpness'), good = holder('minecraft:unbreaking')
function iterator(items) {
  let i = 0
  return { hasNext: () => i < items.length, next: () => items[i++], set: v => { items[i-1] = v } }
}
const javaList = items => ({ iterator: () => iterator(items) })
function stored(levels) {
  return { levels, keySet: () => javaList([...levels.keys()]), getLevel: h => levels.get(h) || 0 }
}
function stack(levels, { configured = false, item = BOOK, broken = false } = {}) {
  return {
    item, configured, extra: 'keep', stored: stored(new Map(levels)),
    getItem: () => { if (broken) throw Error('native failure'); return { equals: other => item === other } },
    is: () => { throw Error('ambiguous ItemStack.is') },
    has: component => component === CASTER && configured,
    get(component) { return component === STORED ? this.stored : null },
    copy() { const copy = stack(this.stored.levels, { configured, item }); copy.extra = this.extra; return copy },
    set(component, value) { assert.equal(component, STORED); this.stored = value }
  }
}
function setup({ pool = [reactive, sharp, good], mods = true, caster = CASTER, compatible = true } = {}) {
  let callback
  const errors = [], warnings = []
  const registry = { getTagOrEmpty: () => javaList(pool) }
  const classes = {
    'net.minecraft.core.component.DataComponents': { STORED_ENCHANTMENTS: STORED },
    'net.minecraft.core.registries.Registries': { ENCHANTMENT: 'enchantments' },
    'net.minecraft.core.registries.BuiltInRegistries': { DATA_COMPONENT_TYPE: { get: () => caster } },
    'net.minecraft.resources.ResourceLocation': { parse: location },
    'net.minecraft.tags.TagKey': { create: () => 'pool' },
    'net.minecraft.world.item.enchantment.Enchantment': { areCompatible: () => compatible },
    'net.minecraft.world.item.enchantment.ItemEnchantments$Mutable': class {
      constructor(original) { this.levels = new Map(original.levels) }
      set(h, l) { if (l === 0) this.levels.delete(h); else this.levels.set(h, l) }
      toImmutable() { return stored(this.levels) }
    },
    'net.minecraft.world.item.Items': { ENCHANTED_BOOK: BOOK }
  }
  const modifier = { name: () => modifier, customAction: fn => { callback = fn } }
  vm.runInNewContext(source, {
    Java: { loadClass: name => { assert(name in classes); return classes[name] } },
    Platform: { isLoaded: () => mods },
    LootJS: { modifiers: fn => fn({ addTableModifier: type => { assert.equal(type, 'CHEST'); return modifier } }) },
    LootType: { CHEST: 'CHEST' },
    console: { warn: msg => warnings.push(msg), error: msg => errors.push(msg) }
  })
  const ctx = { getLevel: () => ({ registryAccess: () => ({ registryOrThrow: () => registry }) }),
    getRandom: () => ({ nextInt: () => 0 }) }
  return { errors, warnings, callback, roll: items => { callback(ctx, javaList(items)); return items } }
}
{
  const h = setup(), plain = stack([], {item: OTHER}), original = stack([[reactive, 2], [sharp, 3]])
  const [a,b] = h.roll([plain, original])
  assert.equal(a, plain); assert.notEqual(b, original); assert.equal(b.stored.getLevel(reactive), 0)
  assert.equal(b.stored.getLevel(sharp), 3); assert.equal(b.stored.getLevel(good), 1)
  assert.equal(b.extra, original.extra); assert.equal(original.stored.getLevel(reactive), 2); assert.equal(h.errors.length, 0)
}
{
  const h = setup(), configured = stack([[reactive, 1]], {configured:true}), ordinary = stack([[sharp, 2]])
  assert.deepEqual(h.roll([configured, ordinary]), [configured, ordinary])
}
{
  const h = setup({pool:[reactive]}), only = stack([[reactive, 1]])
  assert.equal(h.roll([only])[0], only); assert.equal(h.roll([only])[0], only); assert.equal(h.warnings.length, 1)
  const mixed = h.roll([stack([[reactive,1],[sharp,3]])])[0]
  assert.equal(mixed.stored.getLevel(reactive), 0); assert.equal(mixed.stored.getLevel(sharp), 3)
}
{
  const h = setup({caster:null}), only = stack([[reactive, 1]])
  assert.equal(h.roll([only])[0], only)
  assert.equal(setup({mods:false}).callback, undefined)
}
{
  const h = setup(), broken = stack([[reactive,1]], {broken:true}), following = stack([[reactive,1]])
  assert.deepEqual(h.roll([broken,following]), [broken,following]); assert.equal(h.errors.length,1)
  assert.equal(h.roll([following])[0],following); assert.equal(h.errors.length,1)
}
console.log('PASS 5 Reactive loot mock fixture groups (not native Rhino QA)')
