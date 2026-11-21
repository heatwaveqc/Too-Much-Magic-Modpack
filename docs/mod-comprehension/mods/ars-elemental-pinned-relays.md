# Ars Elemental 0.7.10.4 — four upgraded Source relays

Exact addon SHA1 `562f640916aa6f5c83b3b3ee2384ca828b870e01`; native Ars5.13.5 SHA1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`. [Receipt](ars-elemental-pinned-guide-urn.md). Evidence: four relay blocks/tiles including anonymous storage subclasses, ModRegistry capability registrations, ModTiles, AEDocumentation, four packaged Apparatus recipes/lang; native RelayTile/Collector/Deposit/Splitter/Warp, AbstractSourceMachine and SourceStorage. No transfer/loaded-chunk/permission acceptance. Native prerequisites/search/link contracts are in [Source audit](ars-nouveau-pinned-source.md).

## Upgrade an existing network

Open Worn Notebook **Source → Elemental Relays**, linked to native Source Relay, Collector, Deposit, Splitter and Warp pages; use **Crafting → Dominion Wand** for directional connections. JEI **Enchanting Apparatus** shows each upgrade. All consume corresponding native relay reagent plus **two matching elemental Essences + two `#c:gems/diamond`**, SourceCost0, result1, keepNbtOfReagent=false:

| Output ID / display | Reagent | Essence | Buffer / max receive / max extract |
| --- | --- | --- | --- |
|`ars_elemental:fire_relay` / Fiery Collector Relay|`ars_nouveau:relay_collector`|Fire×2|4000 /4000 /3500|
|`ars_elemental:earth_relay` / Deep Depositor Relay|`ars_nouveau:relay_deposit`|Earth×2|5000 /5000 /2500|
|`ars_elemental:water_relay` / Flow Splitter Relay|`ars_nouveau:relay_splitter`|Water×2|3500 /3500 /3500|
|`ars_elemental:air_relay` / Wind Warper Relay|`ars_nouveau:relay_warp`|Air×2|3000 /3000 /3000|

Numbers are SourceStorage constructor capacity/maxReceive/maxExtract, initial Source0; **per-call limits, not measured network Source/second**. More buffer does not create Source. Diamond/essence upgrade follows native relay acquisition (collector chest recipe, depositor Hoppers, splitter quartz/lapis, warp Pearls/Popped Chorus); neither all four upgrades nor every school is mandatory. Upgrade recipe does not preserve reagent NBT; configure new block deliberately rather than promising portable network state.

For same-world loaded endpoints, normal wand workflow: source Jar first → relay second to set take-from; relay first → destination second to set send-to. Relay-to-relay chain is assigned through send callback, because ordinary last callback refuses a RelayTile as source. Shift wand clear with no stale stored selection clears connections. Splitter/Warper add source/destination positions to sets instead of replacing a single endpoint; no chunk loader or dimension address is added. Ordinary max distance30/self rejection remains, except Warp's another-Warp distance bypass. Power disables ticking through inherited block behavior; redstone/off and connections persist via native tile data.

## Actual relay loops

**Fire:** inherits native Collector, whose tick first runs ordinary pull→send on20 world-tick boundaries, then scans SourceUtil range5 providers to collect until buffer full. Configured source/destination are excluded from local scan. Collected Source is after that pass's send, so may wait for next pass to leave. Local collection uses legacy provider.getSource()→tile transfer, which clamps sender rate/availability and receiver capacity; it is not a pooled global3500/s reservation. Multiple providers can contribute to4000 capacity. Modern external capability receive/extract uses storage limits above.

**Earth:** ordinary configured pull→send first, then on same20tick boundary distributes remaining Source to SourceUtil range5 acceptable providers, excluding configured endpoints and local RelayTile destinations. Legacy local transfer uses source tile maxExtract2500 per transfer/capacity, so multiple destinations can draw in a pass until5000 buffer empties. Particles after a local attempt do not prove delivery. It moves Source only, not item inventories or fluids.

**Water:** native Splitter loop every20ticks requires **nonempty destination set before pulling at all**, not just before sending. Count loaded eligible sources, divide transfer ceiling3500 by count (integer); zero skips; transfer each then divide current buffer by eligible-destination count and try each. Capability path uses three-argument transfer(sourceStorage,destinationStorage,share); legacy fallback uses tile overload. Order/full destinations/individual caps and integer truncation mean nominal equal shares do not guarantee equal received totals. Unloaded endpoints remain stored/skipped; stale loaded endpoints may be removed. No mandatory upfront Source fee, only quantities moved.

**Air:** inherits Splitter scheduling and lists, buffer/rate3000. Inherited Warp.closeEnough permits another RelayWarpTile beyond30; both ends still must be loaded, positions are same-world BlockPos. Addon's two-argument capability transfer override directly calls plain three-argument capability transfer with sender maxExtract, avoiding native Warp's lossy two-argument code. In ordinary Splitter processFromList/processToList, calls are already the three-argument capability overload.

### Why “lossless Warp” needs the typed path

Addon ModRegistry explicitly registers **all four** addon block entity types for native SOURCE_CAPABILITY, returning tile.getSourceStorage for every side. Therefore normal addon relay-to-relay endpoints expose **SourceStorage**, not RelayWarpTile, to capability transfers. The inherited three-argument ISourceCap path simulates sender extraction and receiver acceptance, then performs equal requested extraction/receipt. It has no distance-loss term; extra Source-consuming spell/machine operations are separate. This supports a code-level lossless **loaded addon capability network**, not cross-dimension/unloaded transfers or measured throughput.

Air does **not** override legacy `transferSource(ISourceTile,ISourceTile,int)`. If an external legacy caller reaches inherited Warp three-argument tile transfer with a Warp destination, native probability(distance−30)/100 can discard30% on its branch (at distance≥130 always), delivering max1/floor0.7×rate. Even upgraded Air retains that fallback body. Native ordinary Warp's Splitter capability path also bypasses that legacy loss; don't claim the upgrade uniquely fixes every native Warp connection. This narrows the blanket Notebook “don't lose Source over long distances” to the actual registered modern route; other mod callers require two-sided audit.

Modern transfer simulates then performs extraction/receipt without checking actual return values again; interposed capability handlers may change acceptance. Legacy transfer mutates sender then receiver without a transaction/refund. No extra universal transfer fee exists in these addon tiles, but exact matching amounts require acceptance with installed handlers and loaded states.

## Quest / unrun acceptance handoff

Teach one native producer/Jar→relay→consumer setup first; optional upgrades are role choices. Give exact Notebook/JEI references, link direction, ranges and need for loaded endpoints; show actual sender decrease/receiver increase. Relay item or upgrade craft is only possession proxy; manual operation acknowledgement unless real transfer detector later exists. Modest XP, ordinary network mounting blocks and limited Source workshop supplies; no free diamond/advanced relay reward that bypasses the selected upgrade cost.

Tests not run: each upgrade's recipe/capacity; sender→receiver deltas, one/many endpoints, receive/extract caps versus entire pass total; Collector after-send delay and configured endpoint exclusion; Depositor local relay exclusion; Splitter no destinations/no pull, zero shares/full receiver/order; redstone/reload/clear; Warp30/31/130+ with loaded/unloaded ends; registered capability versus deliberate legacy tile invocation; invalid endpoint and changed acceptance between simulation/execution. No runtime edits or universal lossless compatibility claim. Remaining turret/gear/worldgen/dependency/pilot review keep addon researching.
