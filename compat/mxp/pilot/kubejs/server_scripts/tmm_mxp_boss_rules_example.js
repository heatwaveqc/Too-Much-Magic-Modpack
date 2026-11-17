// Test-only opt-in. No reward amounts are approved for vanilla bosses yet.
// This file is NOT included in the actual packwiz build.
const ENABLE_TEST_BOSS_RULES = false

if (ENABLE_TEST_BOSS_RULES) {
  const MXP = Java.tryLoadClass('io.github.heatwaveqc.tmm.mxp.MxpService')
  if (!MXP) throw new Error('TMM MXP bridge not accessible through KubeJS')
  ServerEvents.loaded(() => {
    MXP.registerBossRule('minecraft:wither', 500, false)
    MXP.registerBossRule('minecraft:ender_dragon', 500, false)
    // Intentionally NOT registering a guessed Chaos Guardian entity ID.
    // Once the real entity ID and death semantics are verified:
    // MXP.registerBossRule(EXACT_ENTITY_ID, 100, true)
  })
}
