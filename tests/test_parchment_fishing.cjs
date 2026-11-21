// Type A / offline contract checks only: mocks LootJS; not Minecraft loot QA.
const {test} = require('node:test')
const assert = require('node:assert/strict')
const fs = require('node:fs')
const path = require('node:path')
const vm = require('node:vm')

const source = fs.readFileSync(path.join(__dirname, '../kubejs/server_scripts/parchment_fishing.js'), 'utf8')
const FRAGMENT = 'improvableskills:parchment_fragment'

function item(id, weight = 1, quality = 0) {
  return {
    id, weight, quality,
    isSimple: () => true,
    isItem: () => true,
    getItem() {return {id: this.id}},
    getWeight() {return this.weight},
    getQuality() {return this.quality},
    setWeight(value) {this.weight = value},
    setQuality(value) {this.quality = value}
  }
}

function run(originals, {missing = false} = {}) {
  const pools = [{entries: originals, getEntries() {return this.entries},
    addEntry(value) {this.entries.push(value)}}]
  const logs = []
  let createdPool = 0
  const api = {
    LootJS: {lootTables(handler) {handler({getLootTable(key) {
      assert.equal(key, 'minecraft:gameplay/fishing/treasure')
      return missing ? null : {firstPool() {return pools[0]}, createPool() {createdPool++; throw Error('bonus pool')}}
    }})}},
    LootEntry: {of(id) {return Object.assign(item(id), {withWeight(value) {this.weight = value; return this}})}},
    console: {warn: msg => logs.push(msg), info: msg => logs.push(msg)}
  }
  vm.runInNewContext(source, api)
  return {pools, logs, createdPool}
}

test('vanilla 6-outcome treasure competes at 0.5% without bonus/empty rolls', () => {
  const original = ['name_tag', 'saddle', 'bow', 'fishing_rod', 'book', 'nautilus_shell']
    .map(id => item('minecraft:' + id))
  const result = run(original)
  assert.equal(result.createdPool, 0)
  assert.equal(result.pools.length, 1)
  assert.equal(original.length, 7)
  assert.equal(original[6].id, FRAGMENT)
  assert.equal(original[6].weight, 1)
  assert.deepEqual(original.slice(0, 6).map(i => i.weight), [34, 34, 34, 34, 34, 34])
  const share = original[6].weight / original.reduce((s, i) => s + i.weight, 0)
  assert.ok(share >= 0.004 && share <= 0.005, `unexpected ${share}`)
  assert.equal(share, 1 / 205)
})

test('mod-added treasure entries retain proportional weight and quality', () => {
  const entries = [item('minecraft:name_tag', 1, 0), item('mod:relic', 7, 2), item('mod:other', 3, -1)]
  run(entries)
  assert.equal(entries[3].id, FRAGMENT)
  assert.equal(entries[0].weight * 7, entries[1].weight)
  assert.equal(entries[0].weight * 3, entries[2].weight)
  assert.equal(entries[1].quality, entries[0].weight * 2)
  assert.equal(entries[2].quality, -entries[0].weight)
  assert.ok(1 / (1 + entries.slice(0, 3).reduce((s, i) => s + i.weight, 0)) <= 0.005)
})

test('a preexisting fragment never injects a duplicate or rescales loot', () => {
  const entries = [item('minecraft:name_tag'), item(FRAGMENT)]
  const result = run(entries)
  assert.equal(entries.length, 2)
  assert.equal(entries[0].weight, 1)
  assert.ok(result.logs.some(x => x.includes('skipping duplicate')))
})

test('unrecognized composite pool fails closed without mutating items', () => {
  const entries = [item('minecraft:name_tag'), {isSimple: () => false}]
  const result = run(entries)
  assert.equal(entries[0].weight, 1)
  assert.equal(entries.length, 2)
  assert.ok(result.logs.some(x => x.includes('Composite')))
})

test('bad/overflowing pools fail closed without partial mutation', () => {
  for (const entries of [[], [item('mod:bad', -1)], [item('mod:high_quality', 1, 2147483647)]]) {
    const before = entries.map(x => x.weight)
    run(entries)
    assert.equal(entries.length, before.length)
    assert.deepEqual(entries.map(x => x.weight), before)
  }
})

test('missing treasure table fails without trying to replace it', () => {
  assert.ok(run([], {missing: true}).logs.some(x => x.includes('missing')))
})
