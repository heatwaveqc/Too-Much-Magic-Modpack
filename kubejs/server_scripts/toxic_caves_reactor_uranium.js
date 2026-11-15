// NEXT QA — stage only, not part of current test/full-integration run.
// Convert Alex's Toxic Caves native uranium into BiggerReactors Rework fuel.
// Existing Alex's Caves uses (including nine-shard combination and storage
// block) are preserved. No worldgen is introduced here.
// Source-verified registries (2026-11-14):
//   alexscaves:uranium
//   biggerreactorsrework:uranium_ingot
ServerEvents.recipes(event => {
  event.shapeless(
    Item.of('biggerreactorsrework:uranium_ingot'),
    ['alexscaves:uranium']
  ).id('tmm:reactors/toxic_caves_uranium_ingot')
})
