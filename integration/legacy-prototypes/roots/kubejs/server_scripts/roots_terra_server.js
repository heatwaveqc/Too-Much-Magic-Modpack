// Too Much Magic — experimental Roots Classic Terra synchronization.
// Opt-in: /tag <player> add tmm_roots_integration_test
// Test +20 capacity: /tag <player> add tmm_roots_terra_capacity_1
// Temporary stage-based input; replace with actual Pufferfish skills later.

const TMMRootsAttachments = Java.loadClass('elucent.rootsclassic.attachment.RootsAttachments');

function tmmUpdateRootsTerra(player) {
  if (!player.stages.has('tmm_roots_integration_test')) return;
  const type = TMMRootsAttachments.MANA.get();
  const data = player.getData(type);
  const maxTerra = 40 + (player.stages.has('tmm_roots_terra_capacity_1') ? 20 : 0);

  // Recalculate absolute value; repeated logins or dimensions never stack it.
  if (data.getMaxMana() !== maxTerra) {
    data.setMaxMana(maxTerra);
    if (data.getMana() > maxTerra) data.setMana(maxTerra);
    player.setData(type, data);
  }

  // Roots' player attachment lacks persistence and built-in client syncing.
  // HUD reads a separate, client-local attachment.
  player.sendData('tmm:roots_terra', { max: maxTerra, current: data.getMana() });
}

PlayerEvents.loggedIn(event => tmmUpdateRootsTerra(event.player));
PlayerEvents.respawned(event => tmmUpdateRootsTerra(event.player));

// Also handles dimension changes, packet race at login, and stage purchases.
// Only opted-in test players get packets, at most 4 small packets per second.
PlayerEvents.tick(event => {
  if (event.player.tickCount % 5 === 0) tmmUpdateRootsTerra(event.player);
});
