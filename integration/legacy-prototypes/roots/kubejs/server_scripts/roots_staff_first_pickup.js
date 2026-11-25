// Too Much Magic — experimental Roots Classic crystal staff imprinting.
// Opt in: /tag <player> add tmm_roots_integration_test
// Skill-stage IDs are temporary test inputs, NOT the final Pufferfish tree.

const TMMRootsComponents = Java.loadClass('elucent.rootsclassic.registry.RootsComponents');
const TMMSpellData = Java.loadClass('elucent.rootsclassic.datacomponent.SpellData');
const TMMSpellDataList = Java.loadClass('elucent.rootsclassic.datacomponent.SpellDataList');
const TMMNonNullList = Java.loadClass('net.minecraft.core.NonNullList');
const TMMDataComponents = Java.loadClass('net.minecraft.core.component.DataComponents');
const TMMCustomData = Java.loadClass('net.minecraft.world.item.component.CustomData');
const TMMCompoundTag = Java.loadClass('net.minecraft.nbt.CompoundTag');

ItemEvents.canPickUp('rootsclassic:crystal_staff', event => {
  const player = event.player;
  if (!player.stages.has('tmm_roots_integration_test')) return;

  // Pre-pickup event exposes the actual entity stack before inventory transfer.
  const stack = event.item;
  const existing = stack.get(TMMDataComponents.CUSTOM_DATA);
  const tag = existing == null ? new TMMCompoundTag() : existing.copyTag();
  if (tag.getBoolean('tmm_roots_imprinted')) return;

  const spells = stack.get(TMMRootsComponents.SPELLS.get());
  if (spells == null) return;
  const original = spells.spellList();
  const improved = TMMNonNullList.create();
  const potencyBonus = player.stages.has('tmm_roots_staff_potency_1') ? 1 : 0;
  const efficiencyBonus = player.stages.has('tmm_roots_staff_efficiency_1') ? 1 : 0;
  const sizeBonus = player.stages.has('tmm_roots_staff_size_1') ? 1 : 0;
  let populatedSpells = 0;

  for (let i = 0; i < original.size(); i++) {
    const spell = original.get(i);
    if (String(spell.effect()).length === 0) {
      improved.add(spell);
    } else {
      populatedSpells++;
      improved.add(new TMMSpellData(
        spell.potency() + potencyBonus,
        spell.efficiency() + efficiencyBonus,
        spell.size() + sizeBonus,
        spell.effect()
      ));
    }
  }

  // Don't mark blank staffs as imprinted.
  if (populatedSpells === 0) return;
  stack.set(TMMRootsComponents.SPELLS.get(), new TMMSpellDataList(improved));
  tag.putBoolean('tmm_roots_imprinted', true);
  TMMCustomData.set(TMMDataComponents.CUSTOM_DATA, stack, tag);
});
