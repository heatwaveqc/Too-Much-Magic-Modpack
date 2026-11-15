// SOURCE-VERIFIED DEMONSTRATION ONLY. NOT installed under kubejs/server_scripts.
// Target Tyz's Skills 6.4.2 / KubeJS 1.21.1 on a disposable test world.
// Never claim a Minecraft advancement proves JAWR status; substitute exact
// pinned JAWR advancement IDs only after verifying their semantics.
//
// Tyz KubeJS docs show `getId()` in one sample, but the actual 1.21.1
// ISkill interface spells it `getID()` (capital "ID").
const EXAMPLE_CATEGORY_ADVANCEMENTS = {
  tmm_vampire: 'minecraft:story/enter_the_nether',
  tmm_werewolf: 'minecraft:story/enter_the_nether'
}

const EXAMPLE_TIER_ADVANCEMENTS = {
  // Replace test IDs with known real gate keys, e.g. a native boss advancement:
  tmm_magic_potency_1: 'minecraft:story/enter_the_nether'
}

const EXAMPLE_STAGE_GATES = {
  // "stages" are per-player KubeJS progression flags, not vanilla entity-type tags.
  // Stage must be set by an authoritative grant/validation procedure.
  // tmm_monk: 'tmm_monk_cultivation_unlocked'
}

const EXAMPLE_PLAYER_TAG_GATES = {
  // Vanilla /tag player tags work, but only if they are set and revoked
  // authoritatively; they are NOT automatically emitted by JAWR.
  // tmm_werewolf: 'tmm_lycan_current'
}

TyzsSkillsEvents.skill_purchase_pre(event => {
  const player = event.getPlayer()
  const skill = event.getSkill()
  const category = skill.getCategory()
  const id = skill.getID()

  // An example for tests: use an advancement to gate an entire category.
  const categoryAdvancement = EXAMPLE_CATEGORY_ADVANCEMENTS[category]
  if (categoryAdvancement && !player.isAdvancementDone(categoryAdvancement)) {
    event.cancel()
    player.tell('Category locked: required advancement not earned.')
    return
  }

  // Or gate individual node tiers by separate advancements.
  const nodeAdvancement = EXAMPLE_TIER_ADVANCEMENTS[id]
  if (nodeAdvancement && !player.isAdvancementDone(nodeAdvancement)) {
    event.cancel()
    player.tell('Skill locked: required advancement not earned.')
    return
  }

  // Optional stage and vanilla player-tag predicates.
  const stage = EXAMPLE_STAGE_GATES[category]
  if (stage && !player.stages.has(stage)) {
    event.cancel()
    return
  }
  const tag = EXAMPLE_PLAYER_TAG_GATES[category]
  if (tag && !player.getTags().contains(tag)) {
    event.cancel()
  }
})

// IMPORTANT LIMITS:
// - Purchases are gated server-side; tabs are not automatically hidden.
// - Direct Tyz addSkillLevel/setSkillLevel bypass PurchasePre.
// - Bulk purchases fire PurchasePre only once at the node boundary.
// - On cure, existing bought CUSTOM attribute modifiers remain until
//   explicitly handled; a gate is NOT dynamic effect suppression.
// - Refunds of prerequisite roots should be guarded with
//   TyzsSkillsEvents.skill_refund_pre before allowing live use.
// - A pure advancement unlock can be permanent; current JAWR identity
//   needs a separately verified, current-state signal.
// - Do not duplicate earned MXP or silently mutate SP when denying.
