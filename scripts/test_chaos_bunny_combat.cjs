// Offline deterministic tests of exact current 1.21.1 KubeJS combat callbacks.
// No Java classes, Minecraft runtime, server boot, Prism or game world required.
'use strict'
const fs = require('node:fs')
const vm = require('node:vm')
const assert = require('node:assert/strict')
const source = fs.readFileSync('kubejs/server_scripts/caerbannog_boss.js','utf8')
const handlers = {drops:[], beforeHurt:[], afterHurt:[]}
const DamageTypes = {GENERIC_KILL: 'admin',PLAYER_ATTACK:'player',MOB_ATTACK:'mob',LAVA:'lava',IN_FIRE:'in_fire',ON_FIRE:'on_fire'}
const effects = {MOVEMENT_SPEED:'speed'}
let rolls=0.5
const sandbox = {
  Java:{loadClass(n) {
    if(n==='net.minecraft.world.damagesource.DamageTypes') return DamageTypes
    if(n==='net.minecraft.world.effect.MobEffects') return effects
    if(n==='net.minecraft.world.effect.MobEffectInstance') return class { constructor(...args){this.args=args} }
    throw Error('unexpected class '+n)
  }},
  EntityEvents:{
    drops:(...a)=>handlers.drops.push(a.at(-1)),
    beforeHurt:(...a)=>handlers.beforeHurt.push(a.at(-1)),
    afterHurt:(...a)=>handlers.afterHurt.push(a.at(-1)),
  },
  Math: Object.assign(Object.create(Math), {random:()=>rolls}),
}
vm.runInNewContext(source,sandbox,{filename:'caerbannog_boss.js'})
assert.equal(handlers.beforeHurt.length,1)
assert.equal(handlers.afterHurt.length,2)
function data(){
  const v=new Map()
  return {getLong:k=>v.get(k)||0,getBoolean:k=>Boolean(v.get(k)),
  putLong:(k,x)=>v.set(k,x),putBoolean:(k,x)=>v.set(k,Boolean(x))}
}
function rabbit(boss=true){
  const tags = new Set(['tmm_caerbannog_chaos', ...(boss?['tmm_caerbannog_boss']:[])])
  return {type:'minecraft:rabbit',getTags:()=>({contains:k=>tags.has(k)}),
    getPersistentData:data(),addEffect(e){this.effect=e},setRemainingFireTicks(n){this.fire=n}}
}
function event(entity,tick,type='player',dmg=20){
  const e={entity,damage:dmg,level:{isClientSide:()=>false,getGameTime:()=>tick},
    source:{actual:{type:type==='player'?'minecraft:player':'minecraft:rabbit'},'is':k=>k===type},
    canceled:false,cancel(){this.canceled=true}}
  return e
}
const boss=rabbit()
const before=handlers.beforeHurt[0],after=handlers.afterHurt[0]
// First valid hit is allowed, starts 100-tick window ONLY after final >0 damage.
const e0=event(boss,500)
before(e0);assert.equal(e0.canceled,false)
after(e0)
const e30=event(boss,530);before(e30);assert.equal(e30.canceled,true)
const e599=event(boss,599);before(e599);assert.equal(e599.canceled,true)
const e600=event(boss,600);before(e600);assert.equal(e600.canceled,false)
after(e600)
// A zero-damage hit cannot arm a fresh cooldown.
const fresh=rabbit()
before(event(fresh,20))
after(event(fresh,20,'player',0))
const still=event(fresh,21);before(still);assert.equal(still.canceled,false)
// Non-player damage always rejected, with /kill escape.
const ranged=event(boss,700,'arrow');before(ranged);assert.equal(ranged.canceled,true)
const admin=event(boss,701,'admin');before(admin);assert.equal(admin.canceled,false)
// Fire and lava are rejected but grant Speed V + visible fire.
const lava=event(boss,702,'lava');before(lava)
assert.equal(lava.canceled,true)
assert.equal(boss.effect.args[0],'speed')
assert.equal(boss.effect.args[1],100)
assert.equal(boss.effect.args[2],4)
assert.equal(boss.fire,100)
// Horde rabbit does not inherit final-boss immunity.
const horde=event(rabbit(false),703,'arrow');before(horde);assert.equal(horde.canceled,false)
// 2% proc is made ONLY after successful damaging normal mob attack.
const bossBite=rabbit()
const victim={type:'minecraft:player',getPersistentData:data(),isAlive:()=>true,
  getMaxHealth:()=>20,getAbsorptionAmount:()=>0,
  damageSources:()=>({mobAttack:x=>({attacker:x})}),
  attack(s,d){this.count=(this.count||0)+1;this.source=s;this.raw=d;
    // Re-enter post-damage event: guard must block duplicate proc.
    const nested=event(this,800,'mob',10);nested.source.actual=s.attacker;handlers.afterHurt[1](nested)
  }}
const tongue=handlers.afterHurt[1]
const hit=event(victim,800,'mob',10);hit.source.actual=bossBite
rolls=0.019
tongue(hit)
assert.equal(victim.count,1)
assert.equal(victim.raw,1024)
assert.equal(victim.source.attacker,bossBite)
assert.equal(victim.invulnerableTime,0)
assert.equal(victim.getPersistentData().getBoolean('tmm_caerbannog_tongue_in_progress'),false)
rolls=0.02
tongue(hit);assert.equal(victim.count,1,'exactly 2% threshold; 0.02 must not proc')
tongue({...hit,damage:0});assert.equal(victim.count,1,'no proc on blocked attacks')
console.log('PASS source-only: no dodge; 100-tick successful-hit shield; safe admin exit; lava Speed V; horde unaffected; 2% normal-damage tongue with reentry guard')
