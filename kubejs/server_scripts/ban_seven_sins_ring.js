// Too Much Magic: banned Relics Seven Deadly Sins Ring.
// Paired with config/relics/relics/ring_of_the_seven_deadly_sins.yaml
// (empty lootData.entries, so Relics will not generate it in chests).
// Curios defaults to allowing a ring-tagged item in a ring slot.
// Removing the tag keeps future normal equips from bypassing the loot ban.
// This cannot retroactively purge already equipped rings or command-spawned items.
ServerEvents.tags('item', event => {
  if (!Platform.isLoaded('relics')) return;
  event.remove('curios:ring', 'relics:ring_of_the_seven_deadly_sins');
});
