#!/usr/bin/env node
// Pure Node harness, no Minecraft runtime required. KubeJS/JAWR/Tyz fakes
// exercise the non-installed cure integration; real Jar QA remains mandatory.
'use strict'

const assert = require('node:assert/strict')
const fs = require('node:fs')
const vm = require('node:vm')
const path = require('node:path')

const source = fs.readFileSync(
  path.join(__dirname, '..', 'compat', 'form-cure', 'tyzs_form_cure_refund.js'), 'utf8'
)

function harness({ missingJawr = false, missingTyz = false } = {}) {
  const logs = []
  const h = {}
  const hooks = {}
  const v = {
    'dev.sterner.witchery.feature.affliction.vampire.VampirePlayerAttachment': {
      getData: p => p.vampire === null ? null : { isVampire: () => p.vampire }
    },
    'dev.sterner.witchery.feature.affliction.werewolf.WerewolfPlayerAttachment': {
      getData: p => p.werewolf === null ? null : { isWerewolf: () => p.werewolf }
    }
  }

  function skill(id, category, prices) {
    return {
      getID: () => id,
      getCategory: () => category,
      getPrice: lvl => prices[lvl - 1],
      prices
    }
  }
  const skills = [
    skill('tmm_vampire_health', 'tmm_vampire', [2, 4, 8]),
    skill('tmm_vampire_fangs', 'tmm_vampire', [3]),
    skill('tmm_werewolf_health', 'tmm_werewolf', [5, 10]),
    skill('tmm_werewolf_bloodlust', 'tmm_werewolf', [7]),
    skill('tmm_core_health', 'tmm_magic_core', [11])
  ]
  function player(id, options = {}) {
    const tags = new Set()
    const saved = new Map()
    return {
      id,
      tickCount: 0,
      sp: options.sp == null ? 100 : options.sp,
      vampire: options.vampire ?? false,
      werewolf: options.werewolf ?? false,
      ranks: {},
      getUUID: () => id,
      messages: [],
      tell(message) { this.messages.push(message) },
      persistentData: {
        getString: key => saved.get(key) || '',
        putString: (key, value) => saved.set(key, value),
        _raw: saved
      },
      stages: {
        has: value => tags.has(value),
        add: value => tags.add(value),
        remove: value => tags.delete(value),
        _raw: tags
      }
    }
  }
  const tyz = {
    skills: () => ({
      getSkillList: () => ({ size: () => skills.length, get: i => skills[i] }),
      getSkillLevel: (p, id) => p.ranks[id] || 0,
      setSkillLevel: (p, id, lvl) => { p.ranks[id] = lvl }
    }),
    sp: () => ({
      getSP: p => p.sp,
      addSP: (p, amount, applyLimits) => {
        assert.equal(applyLimits, false, 'cure refund must bypass SP caps')
        p.sp += amount
      }
    })
  }
  const events = {
    skill_purchase_pre: cb => { hooks.buyPre = cb },
    skill_purchase_post: cb => { hooks.buyPost = cb },
    skill_refund_post: cb => { hooks.refundPost = cb }
  }
  const playerEvents = {
    loggedIn: cb => { hooks.login = cb },
    loggedOut: cb => { hooks.logout = cb },
    tick: cb => { hooks.tick = cb }
  }
  vm.runInNewContext(source, {
    Java: {
      tryLoadClass(name) {
        if (name === 'com.tyzsskills.api.TyzsSkillsAPI') return missingTyz ? null : tyz
        return missingJawr ? null : v[name]
      }
    },
    TyzsSkillsEvents: events,
    PlayerEvents: playerEvents,
    console: {
      warn: (...x) => logs.push(['warn', ...x]),
      error: (...x) => logs.push(['error', ...x]),
      info: (...x) => logs.push(['info', ...x])
    }
  }, { filename: 'tyzs_form_cure_refund.js' })

  function buy(p, id, count = 1) {
    const s = skills.find(s => s.getID() === id)
    assert(s, id)
    let canceled = false
    const ev = {
      getPlayer: () => p,
      getSkill: () => s,
      cancel: () => { canceled = true }
    }
    hooks.buyPre(ev)
    if (canceled) return false
    const start = p.ranks[id] || 0
    let price = 0
    for (let level = start + 1; level <= start + count; level++) {
      price += s.getPrice(level)
    }
    if (price > p.sp) return false
    p.sp -= price
    p.ranks[id] = start + count
    hooks.buyPost(ev)
    return true
  }

  function normalRefund(p, id, count, rate = 0.3) {
    const s = skills.find(s => s.getID() === id)
    const start = p.ranks[id] || 0
    assert(count > 0 && count <= start)
    let refund = 0
    for (let level = start; level > start - count; level--) {
      refund += Math.round(s.getPrice(level) * rate)
    }
    p.sp += refund
    p.ranks[id] = start - count
    hooks.refundPost({ getPlayer: () => p, getSkill: () => s })
    return refund
  }

  function tick(p, count = 1) {
    for (let i = 0; i < count; i++) {
      p.tickCount += 20
      hooks.tick({ player: p })
    }
  }

  h.player = player
  h.buy = buy
  h.normalRefund = normalRefund
  h.tick = tick
  h.login = p => hooks.login({player:p})
  h.logout = p => hooks.logout({player:p})
  h.skills = skills
  h.hooks = hooks
  h.logs = logs
  return h
}

const tests = []
function test(name, fn) { tests.push({ name, fn }) }

test('purchase gate checks native JAWR state rather than a spoofed stage', () => {
  const h = harness()
  const p = h.player('one')
  p.stages.add('tmm_vampire_active')
  assert.equal(h.buy(p, 'tmm_vampire_health'), false)
  assert.equal(p.sp, 100)
  p.vampire = true
  assert.equal(h.buy(p, 'tmm_vampire_health'), true)
  assert.equal(p.ranks.tmm_vampire_health, 1)
  assert(p.stages.has('tmm_vampire_active'))
})

test('rank-priced vampire refund is full, isolated and idempotent', () => {
  const h = harness()
  const p = h.player('two', { vampire: true, werewolf: true, sp: 250 })
  h.login(p)
  assert(h.buy(p, 'tmm_vampire_health', 3))
  assert(h.buy(p, 'tmm_vampire_fangs'))
  assert(h.buy(p, 'tmm_werewolf_health'))
  p.ranks.tmm_core_health = 1
  assert.equal(p.sp, 250 - 14 - 3 - 5)
  p.vampire = false
  h.tick(p, 1)
  assert.equal(p.ranks.tmm_vampire_health, 3, 'first false is not enough')
  h.tick(p, 1)
  assert.equal(p.ranks.tmm_vampire_health, 0)
  assert.equal(p.ranks.tmm_vampire_fangs, 0)
  assert.equal(p.ranks.tmm_werewolf_health, 1)
  assert.equal(p.ranks.tmm_core_health, 1)
  assert.equal(p.sp, 250 - 5)
  assert(!p.stages.has('tmm_vampire_active'))
  assert(p.stages.has('tmm_werewolf_active'))
  h.tick(p, 3)
  assert.equal(p.sp, 245, 'repeat cure observations must never duplicate SP')
})

test('normal partial and bulk refunds are not paid twice', () => {
  const h = harness()
  const p = h.player('three', { werewolf: true })
  assert(h.buy(p, 'tmm_werewolf_health', 2))
  assert.equal(p.sp, 85)
  const nativePartial = h.normalRefund(p, 'tmm_werewolf_health', 1)
  assert.equal(nativePartial, 3)
  assert.equal(p.sp, 88)
  p.werewolf = false
  h.tick(p, 2)
  assert.equal(p.sp, 93, '5 SP of still-invested rank is returned')
  assert.equal(p.ranks.tmm_werewolf_health, 0)
})

test('untracked free grants do not generate skill points on cure', () => {
  const h = harness()
  const p = h.player('four', { vampire: true })
  p.ranks.tmm_vampire_health = 3
  h.login(p)
  p.vampire = false
  h.tick(p, 2)
  assert.equal(p.ranks.tmm_vampire_health, 0)
  assert.equal(p.sp, 100)
})

test('one false flicker followed by true is not treated as a cure', () => {
  const h = harness()
  const p = h.player('five', { vampire: true })
  assert(h.buy(p, 'tmm_vampire_health'))
  p.vampire = false
  h.tick(p, 1)
  p.vampire = true
  h.tick(p, 1)
  p.vampire = false
  h.tick(p, 1)
  assert.equal(p.ranks.tmm_vampire_health, 1)
  p.vampire = true
  h.tick(p, 1)
  assert.equal(p.sp, 98)
})

test('unknown class or unreadable native state cannot trigger a refund', () => {
  const h = harness()
  const p = h.player('six', { vampire: true })
  assert(h.buy(p, 'tmm_vampire_health'))
  p.vampire = null
  h.tick(p, 5)
  assert.equal(p.ranks.tmm_vampire_health, 1)
  assert.equal(p.sp, 98)
  const missing = harness({missingJawr: true})
  const q = missing.player('six-b', {vampire:true})
  assert.equal(missing.buy(q, 'tmm_vampire_health'), false)
  assert.equal(q.sp, 100)
})

test('re-infection allows new purchases using returned wallet', () => {
  const h = harness()
  const p = h.player('seven', { vampire: true })
  assert(h.buy(p, 'tmm_vampire_health', 2))
  p.vampire = false
  h.tick(p, 2)
  assert.equal(p.sp, 100)
  p.vampire = true
  h.tick(p)
  assert(h.buy(p, 'tmm_vampire_health'))
  assert.equal(p.sp, 98)
})

test('two different players remain isolated', () => {
  const h = harness()
  const a = h.player('eight-a', { vampire: true })
  const b = h.player('eight-b', { vampire: true })
  assert(h.buy(a, 'tmm_vampire_health', 2))
  assert(h.buy(b, 'tmm_vampire_health', 1))
  a.vampire = false
  h.tick(a, 2)
  assert.equal(a.sp, 100)
  assert.equal(b.sp, 98)
  assert.equal(b.ranks.tmm_vampire_health, 1)
})

test('noninstalled Tyz causes no event registration or game mutation', () => {
  const h = harness({ missingTyz: true })
  assert.equal(Object.keys(h.hooks).length, 0)
  assert(h.logs.some(x => x.join(' ').includes('dormant')))
})

let failures = 0
for (const { name, fn } of tests) {
  try {
    fn()
    console.log('PASS ' + name)
  } catch (error) {
    failures++
    console.error('FAIL ' + name + '\n' + error.stack)
  }
}
console.log(tests.length + ' cases; ' + failures + ' failed')
if (failures) process.exit(1)
