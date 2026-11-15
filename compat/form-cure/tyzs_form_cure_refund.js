// TMM JAWR -> Tyz form-skill lifecycle. QA CANDIDATE; NOT automatically installed.
// Copy into kubejs/server_scripts only after smoke tests. No custom JAR required.
// Tyz: 6.4.2; JAWR: 0.5.39; KubeJS: 2101.7.2-build.381.
//
// Contract: actual JAWR affliction gates purchases. On CONFIRMED cure, remove
// purchased ranks ONLY in the cured form's Tyz category, refund 100% of SP
// demonstrably charged by Tyz purchases, and revoke its KubeJS stage.
// Core Skills and the other form's skills are never touched.
//
// PRE-EXISTING skills without this script's paid-rank ledger are treated as
// free/unverified grants (0 SP to refund). Migrate genuine legacy purchases
// explicitly before activation; never manufacture currency from untracked
// admin-given ranks. Confirm native JAWR status APIs in a disposable world.

;(() => {
  const $Tyz = Java.tryLoadClass('com.tyzsskills.api.TyzsSkillsAPI')
  if ($Tyz == null) {
    console.info('[TMM form cure] Tyz not installed: cure integration dormant.')
    return
  }

  const FORMS = [
    {
      id: 'vampire',
      category: 'tmm_vampire',
      stage: 'tmm_vampire_active',
      clazz: Java.tryLoadClass('dev.sterner.witchery.feature.affliction.vampire.VampirePlayerAttachment'),
      predicate: 'isVampire'
    },
    {
      id: 'werewolf',
      category: 'tmm_werewolf',
      stage: 'tmm_werewolf_active',
      clazz: Java.tryLoadClass('dev.sterner.witchery.feature.affliction.werewolf.WerewolfPlayerAttachment'),
      predicate: 'isWerewolf'
    }
  ]

  const LEDGER_PREFIX = 'tmm_tyz_paid_ranks_v1_'
  const seenAbsent = {}
  const pendingBuy = {}
  const loggedFailures = {}

  function key(player, skillId) {
    return String(player.getUUID()) + '::' + String(skillId)
  }

  function formForSkill(skill) {
    const category = String(skill.getCategory())
    for (const form of FORMS) if (category === form.category) return form
    return null
  }

  // Return true or false ONLY from the pinned, authoritative JAWR state.
  // Return null when unavailable: never treat a failed read as a cure.
  function nativeFormState(player, form) {
    if (form.clazz == null) return null
    try {
      const data = form.clazz.getData(player)
      if (data == null) return null
      const value = data[form.predicate]()
      return typeof value === 'boolean' ? value : null
    } catch (error) {
      if (!loggedFailures[form.id]) {
        loggedFailures[form.id] = true
        console.error('[TMM form cure] Failed to read JAWR ' + form.id + ' state: ' + error)
      }
      return null
    }
  }

  function allSkillsOf(form) {
    const matches = []
    const all = $Tyz.skills().getSkillList()
    for (let i = 0; i < all.size(); i++) {
      const skill = all.get(i)
      if (String(skill.getCategory()) === form.category) matches.push(skill)
    }
    return matches
  }

  function currentRank(player, skill) {
    return Number($Tyz.skills().getSkillLevel(player, String(skill.getID())))
  }

  function ledgerKey(skill) {
    return LEDGER_PREFIX + String(skill.getID())
  }

  // Each entry is the exact nonnegative SP paid for ONE rank. A zero
  // represents an administrative, free or historical unverified rank.
  function readLedgerRaw(player, skill) {
    const raw = String(player.persistentData.getString(ledgerKey(skill)))
    if (!raw) return []
    try {
      const parsed = JSON.parse(raw)
      if (!Array.isArray(parsed) || parsed.length > 99) return []
      if (!parsed.every(n => Number.isInteger(n) && n >= 0 && n <= 100000000)) return []
      return parsed
    } catch (_) {
      return []
    }
  }

  function saveLedger(player, skill, paid) {
    player.persistentData.putString(ledgerKey(skill), JSON.stringify(paid))
  }

  function rankLedger(player, skill, level) {
    const paid = readLedgerRaw(player, skill)
    paid.length = Math.min(paid.length, Math.max(0, level))
    while (paid.length < level) paid.push(0)
    return paid
  }

  function syncLedger(player, skill) {
    const lvl = currentRank(player, skill)
    const old = readLedgerRaw(player, skill)
    if (old.length !== lvl) saveLedger(player, skill, rankLedger(player, skill, lvl))
  }

  function sumPaid(values) {
    let result = 0
    for (const value of values) result += value
    return result
  }

  // Prerequisite and form gates are server-enforced, not category GUI hiding.
  TyzsSkillsEvents.skill_purchase_pre(event => {
    const skill = event.getSkill()
    const form = formForSkill(skill)
    if (form == null) return

    const player = event.getPlayer()
    if (nativeFormState(player, form) !== true) {
      event.cancel()
      player.tell('You must currently be a ' + form.id + ' to purchase this skill.')
      return
    }

    const id = String(skill.getID())
    syncLedger(player, skill)
    pendingBuy[key(player, id)] = {
      level: currentRank(player, skill),
      sp: Number($Tyz.sp().getSP(player))
    }
    player.stages.add(form.stage)
  })

  TyzsSkillsEvents.skill_purchase_post(event => {
    const skill = event.getSkill()
    if (formForSkill(skill) == null) return

    const player = event.getPlayer()
    const id = String(skill.getID())
    const k = key(player, id)
    const before = pendingBuy[k]
    delete pendingBuy[k]
    if (!before) {
      console.warn('[TMM form cure] Purchase without pre-snapshot for ' + id + ': no SP ledger credit.')
      return
    }

    const nowRank = currentRank(player, skill)
    if (nowRank <= before.level) return
    const paid = rankLedger(player, skill, before.level)
    let charged = Math.max(0, before.sp - Number($Tyz.sp().getSP(player)))
    for (let level = before.level + 1; level <= nowRank; level++) {
      const price = Math.max(0, Number(skill.getPrice(level)))
      const recorded = Math.min(price, charged)
      paid.push(recorded)
      charged -= recorded
    }
    if (charged > 0) {
      console.warn('[TMM form cure] SP debit exceeds rank price for ' + id + ': extra debit not tracked.')
    }
    saveLedger(player, skill, paid)
  })

  // A player's normal Tyz respec has already refunded SP through Tyz.
  // Drop the refunded ranks from our ledger to prevent double credit at cure.
  TyzsSkillsEvents.skill_refund_post(event => {
    const skill = event.getSkill()
    if (formForSkill(skill) == null) return
    const player = event.getPlayer()
    saveLedger(player, skill, rankLedger(player, skill, currentRank(player, skill)))
  })

  // Recover/clear only the selected form. Do not use Tyz's native bulk
  // refund: it defaults to 30% and may be disabled for normal respec.
  function clearCuredForm(player, form) {
    let refunded = 0
    let removed = 0
    for (const skill of allSkillsOf(form)) {
      const id = String(skill.getID())
      const level = currentRank(player, skill)
      if (level <= 0) {
        // A direct administrative reset without our event must not leave
        // stale paid-rank entries to credit on the next cure.
        saveLedger(player, skill, [])
        continue
      }

      const paid = rankLedger(player, skill, level)
      $Tyz.skills().setSkillLevel(player, id, 0)
      if (currentRank(player, skill) !== 0) {
        console.error('[TMM form cure] Rank removal failed for ' + id + ', retaining ledger.')
        continue
      }
      // Anti-duplication: clear ledger before applying the compensating
      // award. A process crash in this tiny interval needs admin recovery.
      saveLedger(player, skill, [])
      refunded += sumPaid(paid)
      removed += level
    }

    if (refunded > 0) {
      // Non-limited award: an involuntary cure is a refund, not MXP income.
      const spBefore = Number($Tyz.sp().getSP(player))
      $Tyz.sp().addSP(player, refunded, false)
      const spAfter = Number($Tyz.sp().getSP(player))
      if (spAfter - spBefore !== refunded) {
        console.error('[TMM form cure] ' + form.id + ' refund SP mismatch: requested ' +
          refunded + ' but received ' + (spAfter - spBefore) + '. Review SP change hooks.')
      }
    }

    if (removed > 0 || player.stages.has(form.stage)) {
      player.stages.remove(form.stage)
      if (removed > 0) player.tell(
        'Your ' + form.id + ' skills were reset. Refunded ' + refunded + ' skill points.'
      )
    }

    return { removed: removed, refunded: refunded }
  }

  function examinePlayer(player) {
    for (const form of FORMS) {
      const k = key(player, form.category)
      const present = nativeFormState(player, form)
      if (present === null) {
        // Unknown state is never a cure. PurchasePre also denies unknown.
        delete seenAbsent[k]
        continue
      }
      if (present === true) {
        delete seenAbsent[k]
        player.stages.add(form.stage)
        for (const skill of allSkillsOf(form)) syncLedger(player, skill)
        continue
      }

      // Two confirmed observations to avoid form-state transition flicker.
      seenAbsent[k] = (seenAbsent[k] || 0) + 1
      if (seenAbsent[k] < 2) continue
      clearCuredForm(player, form)
    }
  }

  PlayerEvents.loggedIn(event => examinePlayer(event.player))
  PlayerEvents.tick(event => {
    const player = event.player
    if (player.tickCount % 20 === 0) examinePlayer(player)
  })
  PlayerEvents.loggedOut(event => {
    const player = event.player
    for (const form of FORMS) delete seenAbsent[key(player, form.category)]
  })
})()
