# Ars Nouveau pinned Bookwyrm and Storage Lectern

Exact `ars_nouveau-1.21.1-5.13.5.jar`, SHA-1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; JSON/CFR0.152. Evidence: `BookwyrmCharm`, `CraftingLecternBlock`/`CraftingLecternTile`, `LecternInvWrapper`, `EntityBookwyrm`, `TransferGoal`/`TransferTask`, `InventoryManager`, `Config` and `ServerConfig`. [Parent](ars-nouveau.md), [Awakening ritual](ars-nouveau-pinned-rituals.md), [Dominion Wand](ars-nouveau-pinned-source.md). No actual storage/crafting/client or reload test. Bookwyrm worker and Bookwyrm familiar are separate roles; familiar magnet/XP behavior remains its own audit.

## Build and connect the workshop

Read native Notebook **Crafting → Storage Lectern**, Bookwyrm and Dominion Wand related entries; JEI **Enchanting Apparatus → Storage Lectern**. Exact `recipe/storage_lectern.json`: ordinary Lectern reagent and four chest-tag pedestal inputs, result one `ars_nouveau:storage_lectern`, sourceCost0. Do not substitute a vanilla lectern for the crafted station.

Obtain Bookwyrm Charm through **Ritual Index → Awakening**, augmenting with Book and Quill/WritableBookItem before starting: one emitted charm per consumed book. This is a ritual output, not an ordinary direct book→charm grid craft. `clear_bookwyrm_charm` only recrafts an existing charm to a fresh one. Use charm on Storage Lectern; addBookwyrm spawns at stationY+1 and records UUID; success consumes one survival charm. The spawn boolean is unchecked. No explicit number-of-workers cap in addBookwyrm; inventory limit is separate.

With Dominion Wand, select storage first, lectern last. Endpoint must expose ItemHandler, not itself be CraftingLecternTile or in LECTERN_BLACKLIST, and be within configured link distance(default30). Existing same-position binding toggles removal. In survival the number of bound inventory **positions** must stay below live registered Bookwyrm count×`bookwyrmLimit` (default8 per worker, allowed1..Integer.MAX_VALUE). It does not count inventory slots or total stacks. No worker means no new ordinary survival binding. Creative bypass exists in this check; don't use that as survival evidence.

Main-hand use with an ordinary held item opens the resolved lectern menu; Dominion Wand/Bookwyrm Charm and offhand pass through to their own controls. Missing/cyclic/unloaded main lectern yields invalid-lectern feedback. Named containers supply tabs, with a separate All tab; actual stack aggregation and filters are not proof that every kind of modded container is compatible.

## Access, crafting and import behavior

| Capability | Exact method contract and practical consequence |
| --- | --- |
| Read inventory | updateItems rebuilds tabs from cached valid handlers; custom-named Nameable containers group by name. StoredItemStack quantities are long. getInvManager requires loaded valid handler, applies FilterSet.forPosition and slot cache, and restricts to selected tab when supplied. |
| Withdraw/deposit | pullStack immediately calls InventoryManager.extractItemFromAll; its boolean=true is **includeInvalidInvs**, not simulation. pushStack inserts through InventoryManager and returns remainder. Actual transaction occurs before any Bookwyrm flight animation. |
| Craft from network | Per-player transient3×3 crafting matrix and result containers; transferToGrid first clears grid back to storage/drop, then takes matching player-inventory items before pulling from network. Candidate network alternatives sorted by itemCounts; player side matching can be by Item identity. Special component-sensitive recipes require acceptance. |
| Repeat crafting | craft removes one per active input slot, handles recipe remaining items, and refills empty slots from matching player inventory first then storage. Remainders prefer grid, then storage, player inventory, drop. craftShift repeats while output/components and stack-limit condition hold; delivers to player, then storage, then drops leftovers and fires crafting hooks. No arbitrary station recipe/machine automation is implied. |
| Clear grid / remove station | clear pushes remaining grid contents back or drops unaccepted stacks. Block removal drops all per-player transient matrices. Do not promise offline/relog persistence of a transient grid from NBT inventory bindings alone. |
| Automatic import | Every20tick boundary when not powered/backed off, scans six adjacent directions, skipping AUTOPULL_DISABLED, invalid/bound inventories and Hopper directly below. Extracts up to that item's max stack size from first nonempty slot; pushes to main storage, tries original slot for remainder, drops remainder that cannot return, then stops. Only one candidate slot per attempt, not all slots per second. No candidate gives100..119tick backoff. |
| Power | neighbor signal sets powered; gates automatic nearby import. It does not gate ordinary push/pull/menu/crafting methods. Power is not a full storage lockdown or permission system. |

Inventory Manager withdrawal mutates storage immediately. Its transfer visuals are not a second independent item movement and should never be counted again as output. Source debit is absent from these inspected lectern/worker transaction paths; that does not establish compatibility with arbitrary external energy/fluid/storage APIs.

## Remote lecterns and worker animation

Select **secondary lectern first → main lectern last** with wand: first callback on secondary records mainLecternPos and clears its local handler list, within configured default30distance. getMainLectern follows loaded same-world positions with a visited list; cycle returns null. This chains menus to storage, not an infinite-range/dimensional inventory, chunk loader or duplication of underlying item stacks. Binding a regular storage endpoint to a secondary later clears its main link. onWanded clears mainLecternPos; it does not clear all local handler bindings in this method.

Transaction methods optionally enqueue TransferTask only when canCreateTasks is true (player within strict40 checked every60..64ticks while worker UUID list nonempty). Queue is EvictingQueue capacity10; overflow can discard old **visual tasks** after transaction already happened. EntityBookwyrm polls queue and TransferGoal flies source→destination, sets displayed held stack, animates chest opening, clears held state at arrival or>200goal ticks. It does not perform storage debit/insertion in that goal. Failed path, absent nearby player or evicted visual task therefore does not by itself prove the storage transaction failed. Actual items/counts are the acceptance signal.

getBookwyrmEntities resolves UUIDs in current ServerLevel and removes unresolved entries; linked-inventory admission uses this live list. Avoid inferring that a temporarily unavailable worker leaves permanent counted capacity. Dispel of living Bookwyrm emits charm carrying its persistent familiar data and removes entity; exact capacity reduction/current existing-binding behavior after removal or chunk reload needs testing. No ownership/security check is established by these connection methods.

Typed-side caution: binding initially queries clicked side, but addHandlerPos/initHandlerCache build capability cache with null side. A side-only machine passing initial check may still not become an operational cached handler. Test actual null-side capability and real item insertion/extraction, not only a connection message. Native filters/tags and third-party storage semantics must be reconciled before promising protected-item or remote-container behavior.

## Quest / acceptance handoff

Teach apparatus lectern → Awakening charm → real worker → inventory first/lectern last → store/retrieve → craft/refill → optional named tabs/import/secondary access. Acquisition tasks detect lectern/charm only. Item possession or flying Bookwyrm is not proof of a network transaction; use confirmed native criteria or explicit manual count check. Rewards: XP and ordinary container/build materials, with no protected-resource shortcuts.

Positive: one worker supports first inventory, deposit/withdraw/craft produces expected counts; test default8positions and ninth rejection, distance30/over30, no-worker negative, powered import vs menu, full destination with fallback/drop, excluded hopper/tag, unloaded/cyclic secondary, same/different named tabs, side-only handler, rapid>10visual tasks, worker Dispel and reload. Compare physical inventory totals and crafting remainders independently of UI/animation. Parent remains researching until other native systems and addons/pilot are reconciled.
