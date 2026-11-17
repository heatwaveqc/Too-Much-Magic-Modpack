// Too Much Magic: owner-approved economic restriction (2026-11-17).
// Remove only the upward metal-to-gem Stone crafting conversions.
// Leave diamond -> gold, iron <-> gold, gem exchange and Stone crafting intact.
ServerEvents.recipes(event => {
  if (!Platform.isLoaded('projecte')) return;
  event.remove({ id: 'projecte:conversions/gold_ingot_to_diamond' });
  event.remove({ id: 'projecte:conversions/iron_ingot_to_ender_pearl' });
});
