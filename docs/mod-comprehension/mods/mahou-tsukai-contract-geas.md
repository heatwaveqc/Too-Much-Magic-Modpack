# Mahou Tsukai — pinned Contract, Geas and boundary scrolls

Companion to [main profile](mahou-tsukai.md), [mana rules](mahou-tsukai-mana-circuits.md) and [exchange/Drain Life audit](mahou-tsukai-exchange-life-drain.md). Exact artifact `mahoutsukai-1.21.1-v1.36.31.jar`, SHA-512 `0c4f3bc823d681ad9cb00895d348d2b414201c4d397ca3aa6427b45b8d2b369e0bae8c1f354a9541e4205dadb58c49203d3570e2433d772c3eef1227bc8bfa5a`; [pin](../../../mods/mahou-tsukai.pw.toml), inspected2026-11-16. **Partial research, no multiplayer/client acceptance.** Numeric defaults are native defaults, not asserted effective world overrides.

## Contract: mutual recognition, not a blanket damage shield

1. Each participating player makes their own **Contract** circle: two Powdered Emerald and one Powdered Ender, unordered counted recipe. No cloth requirement in the registrar. Compendium **Contract** receives configured cost10 in its native page.
2. Put circles within the native radius10 coordinate cube and each player stand inside their own circle's one-block scan volume. The tile polls every4 game ticks by default. It looks for Player entities, and each other circle's first scanned player must be alive and match that circle's caster UUID. Its own callback also requires the selected player match its caster UUID. Crowd the volume and entity ordering can prevent the intended pair being selected.
3. Both players need10 available mana per new pair. The code first checks both balances, then drains caster and target sequentially through ordinary personal/gem/circuit payment, and finally adds the relationship. Existing contracts skip payment. There is no transaction rollback if the first debit succeeds but the second debit/add fails; preliminary checks do not reserve shared circuit mana.
4. Successful addition stores **both UUID directions** and sends both players a message. Multi-participant nearby circles can form several new pairings, each with cost10 per party; do not describe a group-wide one-time10-mana fee.

`Utils.getWorldMahou` resolves the server overworld's saved data even when invoked from another level. `MahouSavedData` adds/removes both UUID directions, marks data dirty, and delegates NBT to `WorldMahouStorage`, which writes/reads player-contract pairs. This is world-save player recognition, not a temporary local-circle field or FTB-team membership. Persistence after restart/cross-dimension behavior still needs actual tests.

The shared immunity helper used by Drain Life and other callers protects contracted players and their tamed animals (plus caster/own pets, ArmorStand, relevant Mental Displacement owner, creative/spectator and Geas rules described in the economy appendix). **Only callers that invoke that helper obtain this protection.** No claim is made that all magic, projectiles, vanilla attacks, explosions or third-party damage respect Contract.

`ServerHandler` invokes `breakContract` from its ordinary `LivingIncomingDamageEvent` handler. If victim is a Player and damage-source entity is another contracted Player, the helper removes the relationship and messages both. It does not cancel the damage itself. The handler sits before that method's later `!event.isCanceled` check; interactions with higher-priority cancellation and the event bus's canceled-delivery rules need testing. Do not promise a contract survives every canceled hit or blocks friendly fire universally. Generic/magic damage with last-hurt credit but no Player source entity is a separate attribution boundary.

## Geas: scroll creation and exact vow identity

Recipe is one Powdered Gold, one Emerald, one Iron **with Spell Cloth required**. Place cloth before drawing; use native Compendium **Geas** and **Geas (cont.)**. `GeasMahoujinTileEntity` is a ScrollGiver returning the Geas scroll. Its SingleUse block's empty-hand action removes the cloth-backed circle; `onRemove` creates one scroll through the native drop helper, carrying Fae identity or the resolved caster's UUID/name. Collect the dropped scroll; do not assume it is inserted into inventory. Owner-offline/caster-resolution, generic block removal and drop protections need acceptance.

The inherited `SpellScroll` starts held use and executes on release, with no minimum charge duration in this class. On the server it verifies a scroll component and binding (empty/same caster/Fae/creative), **drains native30 mana first**, then calls Geas effect. A new vow returns true and consumes one scroll in survival. Removing an existing vow returns false and leaves the scroll, but the prior30-mana payment has already occurred. A no-target call prints the current list and returns false: no scroll consumed, but still pays30. This is not free list inspection through this casting route.

Selection uses cursor targeting with global look range100 default and a predicate allowing other LivingEntities/ItemEntities, excluding configured entity blacklist (ArmorStand default). Full ray/near-candidate geometry remains open; do not promise through-wall range100 targeting.

| Target/action | Saved identity and consequence |
| --- | --- |
| Unnamed non-player living entity | Exact entity registry name, producing a species-wide vow. |
| Custom-named non-player, not sneaking | Custom name + separator + registry name; only matching name/type qualifies. Renaming can change matching. |
| Same custom-named entity while sneaking/discrete | Registry name only, hence species-wide rather than named-target vow. |
| Player | Display name + separator + `minecraft:player`; not a UUID identity. Display-name changes and formatting are boundaries to test. |
| Dropped item | `Item:` plus the item's localized type name from `item.getItem().getName(stack)`, not arbitrary stack hover-name or registry ID. Language/item-name collisions and custom items need testing. |
| Cast at an identity already saved | Toggle that vow off; return false, so no scroll consumption, but paid mana remains spent. |

Addition strips formatting from saved strings; matching uses current names/registry. Vows live in the player's Mahou HashSet; `MahouStorage` serializes the set and loads it back. `Mahou.copyMahou` copies it in the player-clone path. This establishes intended persistence, not proof of every death/relog outcome or immediate client sync.

### What a vow actually restricts and rewards

- `AttackEntityEvent` checks a matching living target and cancels direct attacks. Highest-priority `LivingIncomingDamageEvent` checks vows when the damage-source entity is a Player. Indirect sources without Player attribution can differ; blanket inability to hurt the species through any mechanism is not proven.
- `ItemEntityPickupEvent.Pre` sets pickup false for a matching item vow and completes native Mandatory Fun `GEAS_PICKUP_CANCEL`. It is a pickup restriction, not a demonstrated ban on crafting, inventories, trades or external insertion.
- The common spell immunity helper checks Geas when it can resolve the caster as an online ServerPlayer, protecting matching living targets from those specific callers. This is not a universal server permission system.
- `getUniqueGeasCount` counts only strings without the separator and not beginning `Item:`: **species-wide non-item vows**. Named entities, players and item vows contribute zero to this count. Native regeneration adds3 per counted species per100-game-tick regeneration opportunity, capped by configured10000000. Adding many renamed members of one species does not yield that bonus per name.
- Burning/destroying a Geas scroll with ON_FIRE, IN_FIRE or LAVA damage invokes reset only when its ItemEntity owner resolves to a ServerPlayer; it sets that player's Geas set null. The handler does not compare scroll binding to item owner. Test owner resolution/fire protection/persistence before prescribing a recovery procedure. This is a reset path, not a claim every lava-dropped scroll always clears vows.

## Drain Life boundary scroll: placement differs from generic casting

`DrainLifeBoundarySpellScroll` selects the same Drain Life boundary block. Its parent overrides ordinary useAction to do nothing; the researched scroll is deployed with **useOn**, not the generic release-cast path. It has initial placement cost0 in that parent; the resulting boundary still pays its own periodic costs.

On the server `boundaryScrollUse` permits matched/unbound/Fae/creative caster and requires air adjacent to the clicked face. It queues `drawBoundary`, then consumes one survival scroll immediately (creative consumption governed by config). The queued action checks air again, places the block, reconstructs its catalyst recipe, copies caster UUID, sets cloth=false and attempts to toggle the boundary for the player. If the position becomes occupied before the queued action, no block is placed and this path has no scroll refund. Block set/add results and claim hooks need client/server acceptance.

Result is a stationary, initially toggled boundary, **not a held aura following the player**. Its ordinary operation/immunity/range/cost is covered in the economy appendix. Do not infer generic scroll payment/consumption rules for boundary deployment. No Spell Cloth is carried into the placed tile by this method, so automatic recovery into an equivalent cloth scroll is not established.

## Walkthrough and acceptance coverage

| Optional lesson | Action and in-game reference | Detection / reward / boundary test (unrun) |
| --- | --- | --- |
| Mutual Contract | Both learn native Contract page, create own circles within coordinate range, stand separately, fund10 each and observe confirmation. | Manual confirmation until a real contract detector exists; item/circle possession does not prove relationship. XP+food. Test one player short, two players sharing a marginal battery, existing relationship and first-player scan ordering. |
| Contract protection | Use a researched helper-calling boundary and compare protected player/pet to an unrelated target. | Observe actual exclusion; vanilla hit should be tested separately for contract break. Test restart/dimension and third-party claim hooks. |
| Species vow | Make cloth-backed Geas scroll, target known expendable species, release and record30 debit/scroll consumption. | Manual vow-list/action evidence; item possession is only craft proxy. XP+Lapis/food. Test named vs sneaking species form and true default regen count. |
| Vow removal/recovery | Toggle same identity with another scroll; explain paid removal and retained scroll. Discuss owner-resolved fire reset only after testing. | Compare balance/scroll before and after; no-target list call should still cost30. Test denied pickup vs inventory insertion separately. |
| Portable boundary placement | Make correct boundary scroll, useOn safe air, observe stationary tile and periodic costs. | Item task does not prove placement/activation. Test wrong binding, occupied adjacent block and queued placement race; no promise of refund. |

Exact evidence: `tile/exchange/ContractMahoujinTileEntity`, `effects/secret/GeasEffect`, `item/spells/{SpellScroll,secret/GeasSpellScroll,boundary/{BoundarySpellScroll,DrainLifeBoundarySpellScroll}}`, `tile/secret/GeasMahoujinTileEntity`, `block/spells/SingleUseMahoujinBlockTileEntity`, `dataattachments/{mahou/{Mahou,MahouStorage},worldsave/{MahouSavedData,WorldMahouStorage}}`, `util/Utils`, `handlers/ServerHandler`, native `GuidebookInstance` pages/registrar and `MTConfig$Server`. Remaining gaps: full saved-data runtime lifecycle, full cursor selector, online/offline protection and other spell delegates, external integrations and gameplay acceptance. Main Mahou profile stays researching.
