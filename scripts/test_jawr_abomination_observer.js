#!/usr/bin/env node
'use strict'
// Standalone mock tests; no claim of real JAWR client gameplay.
const assert = require('node:assert/strict')
const fs = require('node:fs')
const vm = require('node:vm')
const path = require('node:path')
const code = fs.readFileSync(path.join(__dirname, '..', 'compat', 'abomination', 'jawr_dual_affliction_observer.js'), 'utf8')

function setup(missing = '') {
  const logs = []
  const hooks = {}
  const provider = {
    'dev.sterner.witchery.feature.affliction.vampire.VampirePlayerAttachment': {
      getData: p => p.broken ? null : {
        isVampire: () => p.v > 0, getLevel: () => p.v
      }
    },
    'dev.sterner.witchery.feature.affliction.werewolf.WerewolfPlayerAttachment': {
      getData: p => p.broken ? null : {
        isWerewolf: () => p.w > 0, getLevel: () => p.w
      }
    },
    'dev.sterner.witchery.feature.transformation.TransformationPlayerAttachment': {
      getData: p => p.broken ? null : {
        getTransformationType: () => ({ name: () => p.form })
      }
    }
  }
  vm.runInNewContext(code, {
    Java: { tryLoadClass: name => missing === name ? null : provider[name] },
    PlayerEvents: {
      loggedIn: cb => hooks.login = cb,
      tick: cb => hooks.tick = cb,
      loggedOut: cb => hooks.logout = cb
    },
    console: {
      info: s => logs.push(['info', s]),
      warn: s => logs.push(['warn', s])
    }
  })
  return {
    logs,
    hooks,
    player: id => ({ id, v: 0, w: 0, form: 'NONE', tickCount: 0, getUUID: () => id }),
    login: p => hooks.login({ player: p }),
    observe: p => { p.tickCount += 20; hooks.tick({player:p}) },
    logout: p => hooks.logout({player:p})
  }
}

const tests = []
function test(title, work) { tests.push({title,work}) }
test('JAWR dual progression is observable in either acquisition order', () => {
  for (const order of ['vw','wv']) {
    const h = setup(), p = h.player(order)
    h.login(p)
    if (order === 'vw') { p.v = 2; h.observe(p); p.w = 3 }
    else { p.w = 3; h.observe(p); p.v = 2 }
    h.observe(p)
    const l = h.logs.filter(x => x[1].includes('DUAL_AFFLICTED_RESEARCH_ONLY'))
    assert.equal(l.length, 1)
    assert(l[0][1].includes('vampireLv=2'))
    assert(l[0][1].includes('werewolfLv=3'))
    assert(l[0][1].includes('not Abomination authorization'))
  }
})
test('single active shape changes do not erase either infection', () => {
  const h=setup(),p=h.player('shapes')
  p.v=6;p.w=6;h.login(p)
  for(const shape of ['BAT','WOLF','WEREWOLF','NONE']) {
    p.form=shape;h.observe(p)
  }
  const dual=h.logs.filter(x=>x[1].includes('DUAL_AFFLICTED_RESEARCH_ONLY'))
  assert.equal(dual.length,5)
  assert(dual.some(x=>x[1].includes('activeForm=BAT')))
  assert(dual.some(x=>x[1].includes('activeForm=WEREWOLF')))
  assert.equal(p.v,6);assert.equal(p.w,6)
})
test('same state is not spammed on every player tick', () => {
  const h=setup(),p=h.player('quiet');p.v=1;p.w=1
  h.login(p);for(let i=0;i<60;i++) h.observe(p)
  assert.equal(h.logs.filter(x=>x[1].includes('DUAL_AFFLICTED')).length,1)
})
test('cure of only one branch logs reclassification but never edits data', () => {
  const h=setup(),p=h.player('cure');p.v=4;p.w=5;h.login(p)
  p.v=0;h.observe(p)
  assert(h.logs.some(x=>x[1].includes('JAWR_WEREWOLF')))
  assert.equal(p.w,5);assert.equal(p.v,0)
})
test('multi-player state is tracked separately and cleaned up on logout', () => {
  const h=setup(),a=h.player('a'),b=h.player('b')
  a.v=1;a.w=1;b.v=2;b.w=1;h.login(a);h.login(b)
  assert.equal(h.logs.filter(x=>x[1].includes('DUAL_AFFLICTED')).length,2)
  h.logout(a);h.login(a)
  assert.equal(h.logs.filter(x=>x[1].includes('DUAL_AFFLICTED')).length,3)
})
test('missing JAWR class disables observer safely', () => {
  const h=setup('dev.sterner.witchery.feature.affliction.vampire.VampirePlayerAttachment')
  assert.equal(Object.keys(h.hooks).length,0)
  assert(h.logs.some(x=>x[1].includes('observer disabled')))
})
test('unknown attachment state is NOT classified as a hybrid or a cure', () => {
  const h=setup(),p=h.player('unknown');p.v=1;p.w=1
  h.login(p);p.broken=true;h.observe(p)
  assert(h.logs.some(x=>x[1].includes('state UNKNOWN')))
  assert.equal(h.logs.filter(x=>x[1].includes('NEITHER')).length,0)
})
let errors=0
for(const {title,work} of tests) {
  try { work(); console.log('PASS '+title) }
  catch(error) { console.error('FAIL '+title+'\n'+error.stack);errors++ }
}
console.log(tests.length+' tests; '+errors+' failures')
if(errors) process.exit(1)
