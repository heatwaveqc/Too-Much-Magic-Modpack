# Ars Nouveau pinned routing and control blocks

Exact5.13.5 JAR SHA1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; CFR0.152. Evidence: SpellPrismBlock/VoidPrism/IPrismaticBlock, EntityProjectileSpell, RedstoneRelay/Tile, BrazierRelay/Tile, RitualBrazierTile, ArcanePlatform/ArcanePedestal, Documentation/recipes/locale. [Parent](ars-nouveau.md), [detectors](ars-nouveau-pinned-detectors.md), [Source relays](ars-nouveau-pinned-source.md), [rituals](ars-nouveau-pinned-rituals.md), [Apparatus geometry](ars-nouveau-pinned-crafting.md). No live routing/redstone/chunk/cycle acceptance. Redstone/Brazier relays are different systems from Source relays.

## Five acquisition routes and guide pages

Notebook **Crafting**: Spell Prism, Void Prism, Redstone Relay, Brazier Relay; Arcane Platform recipe included under Enchanting Apparatus alongside Arcane Pedestal. Native AUTOMATION category maps to Crafting. JEI exact tag alternatives:

| Item | Native recipe |
| --- | --- |
| redstone_relay | Shaped `gxg/gMg/gxg`: six Gold ingots, two Redstone dust, one Source block (all corresponding c:tags). |
| spell_prism | Shaped `gxg/xnx/gxg`: four Gold ingots, four Archwood Planks, one Quartz block tag. |
| void_prism | Shaped Spell Prism surrounded by eight Obsidian-tag items. |
| brazier_relay | Apparatus Ritual Brazier reagent + three Manipulation Essence; SourceCost0, keepNbt=false. |
| arcane_platform | Shapeless one Arcane Pedestal -> one Platform. Fresh output, not guaranteed preservation of displayed contents. |

These are optional controls after their material/infrastructure gates. Use one simple route before chained automation; owning a relay does not prove a signal or ritual operated.

## Projectile prisms

Place Spell Prism facing desired exit (placement opposite nearest looking direction). Fire an Ars **EntityProjectileSpell** into it. On eligible block hit, projectile checks IPrismaticBlock before ordinary impact handling; server posts SpellProjectileHitEvent and cancellation prevents redirect. No general arrow/fireball/other mod projectile compatibility inferred.

Spell Prism increments projectile.prismRedirect, then with resolver present repositions same projectile just outside exit face and shoots along facing, retaining speed length with minimum0.1. Horizontal exits have Yoffset-0.2; directional offset0.505. It updates observers. Third and subsequent redirect attempts can reward nearby Prismatic advancement within10; this is a threshold trigger, not a three-redirect lifetime cap. Null resolver removes projectile. No new spell composition, personal mana or Source debit inside prism handler; original launcher/caster already paid through its own path. Still no refund when routing loses projectile.

Void Prism discards EntityProjectileSpell and updates observers. It's a spell-projectile terminator, not a generic item/entity voider, Source sink or claims-protection shield. Event/collision/hit-list rules determine whether prism callback is reached. Test a straight path, turn and intended terminal before relying on advanced loops or AOE projectiles; no chain throughput/lifetime/cycle safety claim here.

## Wireless redstone relay

Place source and receiver within30, same level; relay placement FACING is opposite player horizontal direction. It samples local input from block **at pos.relative(FACING)**, querying signal in FACING. getSignal/getDirectSignal output currentPower except queried side==FACING.opposite returns0. Describe/test facing by actual state/signal query, not an ambiguous front/back label.

Use Dominion Wand **source relay first, receiver last**: first callback toggles source.powering target; last toggles receiver.poweredFrom source. Repeat disconnects. Self-link rejected; links require existing RedstoneRelayTile and distance<=30. No Source/personal mana/item operating debit in these methods. Local neighbor change recalculates; linked notifications reach only loaded target tiles. It doesn't auto-load or store cross-dimensional addresses.

`calculateNewPower` starts with localPower and iterates poweredFrom; each loaded relay whose output>**localPower** overwrites newPower/currentParent. It compares against localPower, **not current running maximum**. With several parents above local input, later qualifying parent can override a stronger earlier one. Parent notifications also track currentParent and may choose newly stronger source; don't promise a mathematically stable max-of-inputs network independent of update/order. Use single-parent teaching circuit; multi-parent/cyclic networks need explicit acceptance.

setNewPower updates POWER state, self/all adjacent neighbors and directly notifies powering list. Removal notifies loaded listeners to remove parent reference/recalculate; unloaded links and saved local/current/parent values can retain history. Link lists/power state persist; pending updateListeners isn't a full persistent event queue. No bounded cycle detector in inspected notification/recalculation chain. Test signal falling edge, disconnect and reload before using relay as shutoff or sensor counter.

## Brazier Relay: move ritual operating location

Use Dominion Wand **original Ritual Brazier first, Relay last**, as Notebook instructs. Original brazier stores relayPos; Relay stores brazierPos for inventory delegation. Actual callbacks accept distance<=16, while native guide prose says15. Teach within15 conservatively and test exact16 boundary separately. Multiple original braziers can point at one Relay, but it stores one brazierPos for its own delegated inventory path; don't infer simultaneous independent inventories from multiple visual connections.

RitualBrazierTile performs its original Source acquisition/backoff logic, then if relayPos loaded and valid calls ritual.tryTick(relayTile) instead of tryTick(original). Source takeSource remains range6 around **original brazier**. Relay light is renewed ticksToLightOff2 and decorative=false while original ticks ritual there. If relay missing/unloaded, inspected original body falls back to original location; invalid remote isn't proven to pause every ritual safely. Keep sensitive ritual area clear at both ends.

Relay.getInventoryManager follows stored loaded RitualBrazierTile recursively, with shared traversed list cycle/size<256 guard, returning delegated manager when its inventory nonempty; clears traversal and falls back to its inherited manager otherwise. This changes item-location semantics alongside remote ticking; not arbitrary generic inventory transport. Relay's own tick is light/particles, not a second independent normal ritual engine tick. Per-ritual effects/material targeting still need their linked companion's contract and effective pack acceptance.

## Arcane Platform

A directional low-profile **ArcanePedestal subclass**, inheriting pedestal block entity/item interaction and capability semantics; changes oriented shapes and display offset scalar to0.1. Parent places against clicked face and moves one selected inventory item onto empty pedestal through its normal interaction. Recipe is alternate physical presentation/support for existing Apparatus, not new autocrafting tier or extra processing Source. Apparatus search still uses audited block-entity/range rules; don't assume stacking platforms changes permitted radius or allows two item slots. Check mounted orientation/clearance in actual layout.

## Quest and acceptance handoff

Optional choices: Prism turn+Void termination; single-parent Relay sending lever signal; Brazier relocation with benign ritual and original Source supply; Platform in known Apparatus recipe. Notebook named pages and JEI are build references. Item tasks prove acquisition only; advancement Prismatic proves its redirect condition, not destination effect or protected operation. Use manual observation/real detectors for wiring/ritual success, with XP and modest building/redstone reward directions.

Unrun: prism cancel/null-resolver/three-turn/void observer behavior and actual mana delta; relay facing,30edge, competing parents/order, disconnect/removal/unloaded/falling-edge/cycle bounds; Brazier16edge, two original rituals, missing/unloaded relay fallback, item inventory and original Source consumption; mounted Platform recipe and item recovery. No source-level route establishes runtime claims safety, deadlock freedom or full quest detection.
