# Ars Elemental 0.7.10.4 — Siren acquisition and fishing shrine

Exact artifact SHA1 `562f640916aa6f5c83b3b3ee2384ca828b870e01`; [guide/artifact receipt](ars-elemental-pinned-guide-urn.md). Research date2026-11-21. Inspected exact addon MermaidEntity, SirenCharm, MermaidRock/MermaidTile, MermaidChannelGoal, MermaidHolder/MermaidFamiliar, ConfigHandler, AEDocumentation and packaged data; native Ars5.13.5 AbstractSummonCharm, SummoningTile, BlockUtil and SourceUtil. Static source evidence only. This closes the shrine operation route; native Binding recipient/capture details remain a specific familiar follow-up, not a whole-addon completion claim.

## Build the optional fishing workshop

1. Open the native Worn Notebook, Crafting → Siren Charm. AEDocumentation registers its introduction, Siren entity display, second explanatory page and crafting pages. JEI shows the Enchanting Apparatus recipe. No separate addon book is needed.
2. Find wild `ars_elemental:siren_entity`. Packaged biome modifier adds weight3, groups1–3, to `#ars_elemental:siren_spawn`: `#minecraft:produces_corals_from_bonemeal` plus optional `#ars_nouveau:archwood_biome`. Spawn placement is IN_WATER; the predicate checks Y between sea level−15 and sea level+15, plus either water below/water block above, or dirt-tag/sand/sandstone below and brightness>8. Biome eligibility and placement checks do not guarantee a spawn.
3. Main-hand interact with one Sea Pickle. Server consumes it once, starts the jump and marks the wild Siren as processing. After the jump's animation counter exceeds30, it drops **1–2 Siren Tokens** (`ars_elemental:siren_shards`), rewards nearby players within10 through native POOF_MOB and discards itself. This does **not** set that wild entity tamed or retain it as a pet. It is a wild-entity conversion, not repeated token harvesting from one Siren.
4. Apparatus: one Siren Token reagent, pedestals **one Prismarine Shard + three `#minecraft:fishes` + three `#c:gems/source`**, result one `ars_elemental:siren_charm`, sourceCost0, keepNbtOfReagent=false. These are consumed craft ingredients, not aquarium decorations. Requires the native Apparatus setup; [native progression](ars-nouveau.md).
5. Use the charm on **plain `minecraft:prismarine`**. It replaces that block with default `ars_elemental:mermaid_rock`, unconverted and not waterlogged. Server use consumes one charm unless the player has infinite materials; unrelated blocks PASS. After120 conversion ticks the shrine changes appearance and spawns a tamed Siren centered one block above with this shrine as home. No extra activation item is checked. Conversion runs in super.tick before the shrine's redstone-off early return.
6. Build a pond/aquarium with aquatic mobs and diverse plants; put an item-handler inventory immediately adjacent and a Source provider within the native range7 lookup. Keep inventory space available. Wait for score refresh and output; see actual timers below. A charm used on an existing MermaidTile instead adds another tamed Siren at the click location with that home and consumes the charm; it does not require a second Prismarine block or check a one-Siren limit in this method.

The block supports waterlogging on ordinary placement, but charm conversion explicitly sets the default state; do not promise submerged conversion preserves waterlogging. It reports true for the conduit-frame hook. Breaking the shrine's packaged loot table returns Prismarine (survives-explosion condition), not a portable progress-preserving shrine.

## Progress, payment and output order

| State/operation | Exact default and boundary |
| --- | --- |
|Channel|Tamed Siren, cooldown≤0, valid home MermaidTile, shrine not off. Select random nearby aquatic mob, or itself when none exists. Goal channels100 ticks then calls giveProgress and sets cooldown100. Movement/AI can change elapsed time; no measured throughput claim.|
|Progress|giveProgress increments only below configured max30. Channel does not check needsMana, so progress can fill while payment is pending.|
|Passive progress|On world gameTime%1000==0, while not needsMana: if progress≥30, generate; otherwise if a nearby aquatic mob exists, add one progress and visual. No nearby mob means no passive increment. Reaching30 on that pass waits until another eligible generation pass.|
|Generation|Server fishing rolls → attempted adjacent inventory insertion → progress=0 → needsMana=true. Initial new-tile needsMana defaults false: no explicit initial1000Source charge. Output is scheduled on1000-tick boundaries, not immediately on the last channel.|
|Payment|Every80 world ticks, while needsMana, native takeSourceMultiple(position,level,7,1000). Non-null clears needsMana. Native helper aggregates providers and restores its recorded withdrawals if insufficient; no payment-success means no further generation. Progress is retained during this wait.|
|Redstone|Neighbor update sets isOff from hasNeighborSignal. Off stops aquarium/payment/output logic and prevents new channel use; conversion still precedes that return.|
|Persistence|Tile saves progress, bonus, needsMana plus inherited converted/off. Siren saves home/tamed/color/cooldown. No player-owner UUID is part of this helper's declared fields; home is a block position.|

All timers are ticks/world-time boundaries, not guaranteed wall-clock rates. Defaults exposed by ConfigHandler are mermaidManaCost1000, mermaidMaxProgress30, mermaidBaseItems1, mermaidScoreBonus2, mermaidTreasureBonus0.002 and mermaidQuantityCap5. These are packaged defaults, not proof of assembled config values.

**Output-loss boundary:** native BlockUtil.insertItemAdjacent iterates adjacent IItemHandlers and returns the uninserted remainder. MermaidTile ignores that return and neither drops nor stores it. A missing/full inventory can therefore lose generated loot; progress still resets and the next Source payment is requested. This is not safe overflow-to-ground automation.

## Aquarium score and loot selection

Score refresh is every2000 world ticks. Inclusive block scan spans ±16X/Z and ±10Y (33×33×21 before build-height exclusions). A HashSet counts each distinct **BlockState**, not every block: air=0; exact default Water state=1; `#ars_elemental:aquarium_bonus`, KelpPlantBlock or CoralBlock=2; otherwise0. Packaged bonus tag includes corals, seagrass, tall seagrass, kelp/kelp plant and turtle egg. Distinct properties can yield distinct states; a room full of the same state does not keep adding points.

Important implementation discrepancy: the local variable water increments for **score0 or1**, including air and unrelated zero-point blocks. The `water>50` gate is therefore not a count of fifty actual water blocks. Preserve a usable aquarium for mobs; do not teach that code as a verified minimum-water structure.

For zero-point scanned blocks, a native Mob Jar's contained aquatic LivingEntity adds its entity type to the jar set. If water>50, each distinct jar type adds3. Then distinct additional live aquatic types add5 each, querying an AABB of the shrine block inflated20 in all axes, excluding MermaidEntity. Jar types suppress duplicate live-type points; multiple mobs of the same type do not repeatedly score. The query does not require the living mob itself currently be underwater.

Synthetic Fishing Rod gets Luck of the Sea level floor(bonus/25) when bonus≥25. Loot context supplies ORIGIN and TOOL, without explicit THIS_ENTITY or context luck. Thus this audit does not claim the synthetic enchantment necessarily improves every fishing table condition or that ordinary player/open-water context is reproduced.

Attempts = baseItems + min(floor(bonus/25),scoreBonus), normally1–3. Each attempt chooses:

- While the special-treasure flag is still true, bonus>50 and random<max(0.1,bonus×0.002): direct vanilla FISHING_TREASURE table; flag becomes false. This chance has a minimum10%, no explicit maximum clamp.
- Otherwise, if flag remains true and bonus≤25, a50% check selects FISHING_JUNK; failing that uses FISHING. Other cases use FISHING.

Each returned ItemStack is inserted and increments the counter, even if insertion fails. The quantity cap5 is checked **after the whole selected table's returned list**, between attempts: it is not a hard cap on stack sizes, total item units or every individual returned stack. Direct treasure happens at most once per cycle through the flag; ordinary FISHING rolls may independently return treasure. Actual loaded vanilla/modded loot-table contents remain a later assembled-data acceptance concern.

## Recovery and familiar boundary

Dispel on a server-side tamed helper produces one charm carrying color/custom name and discards the helper; an untamed Siren is not converted by this method. The returned charm carries no shrine progress/home; shrine tile state is separate. Tamed helpers accept one kelp or matching coral item to change to a different variant, consuming it; same-color interactions fall through. Wild helpers may despawn far away; tamed ones return false for removeWhenFarAway. Ten health, no fall damage, zero base XP; no combat/resource-farm claim inferred.

MermaidHolder registers `ars_elemental:siren_familiar` and accepts MermaidEntity instances. Notebook familiar prose names Ritual of Binding. Actual familiar adds +2 damage modifier for its owner's Water-school parts. Every60 server ticks, **the familiar being in water** (not an owner-water check) grants both familiar and owner Dolphin's Grace II for600 ticks. It can renew while the owner is dry; effect expiry is not instant on leaving water. Familiar does not run MermaidChannelGoal or generate shrine loot. Native Binding eligibility/ownership, source cost, entity consumption and capture data must still be reconciled with the parent ritual before treating familiar acquisition as closed.

## Quest handoff and unrun acceptance

| Teach | Truthful detection/reward direction | Positive and boundary acceptance |
| --- | --- | --- |
|Wild Siren → token → charm; Notebook Crafting/Siren Charm + JEI Apparatus|Token/charm possession is a proxy; `ars_nouveau:siren_charm` advancement is inventory_changed, parent poof_mob. Suggest modest XP and pond building materials, not a free replacement rare gate.|One pickle converts one wild Siren into1–2tokens; second interaction during processing does not consume another.|
|Convert Prismarine and observe helper; same Notebook page|Manual operation acknowledgement, or explicitly labeled charm item proxy; XP and small aquarium decorations.|One charm creates shrine/helper after120ticks; unrelated block preserves charm.|
|Supply inventory, aquarium, Source and collect output|Possession of fishing loot does not prove shrine operation. Manual acknowledgement unless a real operation detector is separately implemented; XP and useful Source workshop supplies.|Observe progress reset/output then1000Source repayment; full/no inventory loses remainder yet still resets/charges next cycle.|
|Improve diversity/maintain workshop|Manual score observation; modest XP and decorations, no guaranteed treasure reward loop.|Repeated identical block/mob state versus distinct states/types; wait2000ticks; redstone-off; insufficient Source preserves pending state; reload retains tile progress.|

Additional boundaries: compare empty air volume versus real water for the misnamed score gate; jar/live same-type score overlap; multiple helpers; channel target far away/unreachable. Goal's not-approached branch is gated by gameTime%20, with the else branch able to start channeling before physical arrival; do not claim strict two-block reach as a proven protection. Binding/familiar ritual acceptance remains pending source closure. No tests above were run, and no runtime/config/quest changes are authorized by this research.
