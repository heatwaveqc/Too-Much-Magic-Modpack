// Too Much Magic — experimental client mirror for Roots Classic's Terra HUD.
// The server is authoritative: this listener never sends stats back to server.

const TMMRootsClientAttachments = Java.loadClass('elucent.rootsclassic.attachment.RootsAttachments');

NetworkEvents.dataReceived('tmm:roots_terra', event => {
  const player = event.player;
  if (player == null) return;
  const maxTerra = event.data.getFloat('max');
  const currentTerra = event.data.getFloat('current');
  const type = TMMRootsClientAttachments.MANA.get();
  const data = player.getData(type);

  // The existing Roots HUD reads this local attachment each frame.
  data.setMaxMana(maxTerra);
  data.setMana(currentTerra);
  player.setData(type, data);
});
