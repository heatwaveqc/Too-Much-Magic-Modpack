#!/usr/bin/env node
// Offline transaction-model test for the actual KubeJS Catalyst Exchange script.
// Mocks ItemEntities and event order; DOES NOT claim to boot Minecraft.
// Run: node scripts/test_mahou_catalyst_exchange.cjs

'use strict'
const assert = require('node:assert/strict')
const fs = require('node:fs')
const vm = require('node:vm')
const source = fs.readFileSync('kubejs/server_scripts/mahou_neo_vitae_catalyst_exchange.js', 'utf8')
const TARGET = 'mahoutsukai:mahoujin_catalyst_exchange'
const DUSTS = {
  iron: 'mahoutsukai:powdered_iron',
  gold: 'mahoutsukai:powdered_gold',
  diamond: 'mahoutsukai:powdered_diamond',
  emerald: 'mahoutsukai:powdered_emerald',
  quartz: 'mahoutsukai:powdered_quartz'
}
let uuidSerial = 0
class ItemStack {
  constructor(id, count, components = { custom: 'preserved' }) {
    this.item = { id }
    this.count = count
    this.components = { ...components }
  }
  getItem() { return this.item }
  getCount() { return this.count }
  setCount(n) { this.count = n }
  copy() { return new ItemStack(this.item.id, this.count, this.components) }
  shrink(n) { this.count = Math.max(0, this.count - n) }
  isEmpty() { return this.count <= 0 }
}
class ItemEntity {
  constructor(level, id, count, pos = {x:1,y:65,z:1}) {
    this.world = level
    this.stack = new ItemStack(id, count)
    this.pos = pos
    this.removed = false
    this.uuid = 'qa-test-' + (++uuidSerial)
  }
  level() { return this.world }
  getUUID() { return this.uuid }
  isRemoved() { return this.removed }
  getItem() { return this.stack }
  setItem(stack) { this.stack = stack }
  discard() { this.removed = true }
  blockPosition() { return {...this.pos, offset(dx,dy,dz) {return {x:this.x+dx,y:this.y+dy,z:this.z+dz}}} }
}
class Level {
  constructor(targetPresent=true) { this.targetPresent=targetPresent }
  isClientSide() { return false }
  hasChunkAt() { return true }
  equals(obj) { return this === obj }
  getBlockState(pos) {
    return {getBlock: () => ({id: this.targetPresent && pos.x===1 && pos.y===64 && pos.z===1 ? TARGET : 'minecraft:stone'})}
  }
}
const nativeHandlers = new Map(), kubeHandlers = new Map(), logs = []
const context = {
  Java: {loadClass(name) {
    if (name === 'net.minecraft.world.entity.item.ItemEntity') return ItemEntity
    if (name === 'net.minecraft.core.registries.BuiltInRegistries') {
      return {ITEM:{getKey:item=>item.id}, BLOCK:{getKey:block=>block.id}}
    }
    throw new Error('Unexpected Java class ' + name)
  }},
  Item: {of: (name,count) => new ItemStack(name,count)},
  Map: Map,
  String: String,
  Object: Object,
  console: {info: msg=>logs.push(msg), error: msg=>logs.push('ERROR: '+msg)},
  EntityEvents: {spawned(name,fn) {assert.equal(name,'minecraft:item');kubeHandlers.set(name,fn)}},
  NativeEvents: {onEvent(name,fn) {assert.ok(!nativeHandlers.has(name));nativeHandlers.set(name,fn)}}
}
vm.runInNewContext(source,context,{filename:'mahou_neo_vitae_catalyst_exchange.js'})

const entityTick = nativeHandlers.get('net.neoforged.neoforge.event.tick.EntityTickEvent$Pre')
const tickPre = nativeHandlers.get('net.neoforged.neoforge.event.tick.LevelTickEvent$Pre')
const tickPost = nativeHandlers.get('net.neoforged.neoforge.event.tick.LevelTickEvent$Post')
const spawn = kubeHandlers.get('minecraft:item')
assert.ok(entityTick && tickPre && tickPost && spawn)

function roundtrip(level, item, consume) {
  const originalId = item.getItem().getItem().id
  spawn({entity:item})
  tickPre({getLevel:()=>level})
  const inWorld = item.getItem()
  const shadowed = inWorld.getItem().id !== originalId
  let cancelled = false
  entityTick({getEntity:()=>item, setCanceled:v=>{cancelled=v}})
  assert.equal(cancelled,shadowed,'Temporary Mahou powder must have its item entity tick cancelled')
  if (shadowed) {
    inWorld.shrink(consume)
    if (inWorld.getCount()===0) item.discard()
  }
  tickPost({getLevel:()=>level})
  return shadowed
}

// Every mapped dust must be substituted and its remainder returned unchanged.
for (const [material,mahou] of Object.entries(DUSTS)) {
  const level = new Level()
  const item = new ItemEntity(level, 'neovitae:'+material+'_dust', 7)
  spawn({entity:item})
  tickPre({getLevel:()=>level})
  assert.equal(item.getItem().getItem().id,mahou,'Wrong native proxy for '+material)
  // Native Mahou's inspected bytecode spawns outputs and shrinks THIS stack.
  item.getItem().shrink(3)
  tickPost({getLevel:()=>level})
  assert.equal(item.getItem().getItem().id,'neovitae:'+material+'_dust')
  assert.equal(item.getItem().getCount(),4)
  assert.equal(item.getItem().components.custom,'preserved')
}

// No mana: zero native consumption, full original Neo dust restored.
{
  const level = new Level()
  const item = new ItemEntity(level,'neovitae:iron_dust',16)
  assert.equal(roundtrip(level,item,0),true)
  assert.equal(item.getItem().getItem().id,'neovitae:iron_dust')
  assert.equal(item.getItem().getCount(),16)
  assert.equal(item.isRemoved(),false)
}

// Full conversion: native discard is respected, no phantom source restored.
{
  const level = new Level()
  const item = new ItemEntity(level,'neovitae:quartz_dust',1)
  assert.equal(roundtrip(level,item,1),true)
  assert.equal(item.isRemoved(),true)
}

// Ineligible circle / native item / unrecognized dust is never swapped.
for (const [id,target] of [
  ['neovitae:iron_dust',false],
  ['mahoutsukai:powdered_iron',true],
  ['neovitae:copper_dust',true],
  ['minecraft:iron_ingot',true]
]) {
  const level = new Level(target), item = new ItemEntity(level,id,5)
  assert.equal(roundtrip(level,item,0),false)
  assert.equal(item.getItem().getItem().id,id)
  assert.equal(item.getItem().getCount(),5)
}

assert.ok(!logs.some(x=>x.startsWith('ERROR:')),'No error in tested paths')
console.log('PASS: all five mappings, native proxy lifecycle, zero/partial/full consumption, wrong circles, source components and exclusion cases')
console.log('NOT A MINECRAFT BOOT OR ACTUAL MAHOU IN-GAME TEST')
