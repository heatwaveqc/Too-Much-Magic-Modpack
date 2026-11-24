# Entity offering enrollment — Sexy Demon initiation (#415)
Owner approved 2026-11-24: give Lilith a diamond ring from Iron's Jewelry to initiate the single Sexy Demon Path. No separate sex-specific Path; no vampire conversion or cure as a side effect. Scope: shared engine hook only; the future Path is data-only. 
Exact JAWR 0.5.39 uploaded binary SHA512 matches Packwiz: LilithEntity.mobInteract(Player, InteractionHand) handles server main-hand Poppy at vampire level6, Garlic cure, then generic enchantable item acceptance. WitcheryEntityTypes registers lilith. Intercept server EntityInteract before native mobInteract; do not edit upstream JAR. Iron's Jewelry 1.21.1 source https://github.com/iron431/irons-jewelry/tree/1.21.1 has generic ring, JewelryData.get(ItemStack), isValid(), parts() map of holders; diamond material registry key irons_jewelry:diamond. The active pack manifest/index currently contains no Iron's Jewelry pin: no provider added or version assumed. Verify selected release's API/registry before activating Path; optional adapter fails closed when absent or incompatible. Diamond material in any genuine ring part qualifies; exact gemstone subset, band composition or pattern restriction not requested. No display name or lore matching.
Future Path JSON fragment (NOT a full Path or shipped live definition):
```json
{
  "enrollment": "entity_offering",
  "entity_offering": {
    "entity": "witchery:lilith",
    "item": "irons_jewelry:ring",
    "jewelry_material": "irons_jewelry:diamond",
    "completion_event": "tyr_progression:sexy_demon_initiation"
  }
}
```
Use normal ProgressionApi.join for eligibility, Newbie restriction and compatibility vetoes. Only consume one ring after successful join. Already joined, ambiguous match, creative/spectator, or rejected join: no consumption; reserve matching offering from native enchanting. Other items/targets and invalid material do not match; preserve ordinary behavior. No forced affliction mutation, grant of XP or invented abilities. No live path installed here. Link to remaining Path design PR #495; reconcile PathRegistry changes with #513 and other active engine writers before merge. 
A: parser tests cover exact entity/item/material and invalid declarations; module compile/CI required. B/C after approved inclusion: valid diamond ring, ruby/generic/malformed/renamed ring, other entity, garlic/poppy/enchantable item, main/offhand, repeated clicks, already joined, incompatible path, creative/spectator, missing provider, rejoin; verify ring spent exactly once on successful enrollment, no loss on rejection, no vampire rank/cure or other native interactions changed. No full build/boot/package authorized.
