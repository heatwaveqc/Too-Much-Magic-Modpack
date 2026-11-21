# Ars Elemental 0.7.10.4 — three current elevators

Exact SHA1 `562f640916aa6f5c83b3b3ee2384ca828b870e01`; [artifact/guide receipt](ars-elemental-pinned-guide-urn.md). Exact UpstreamBlock/Tile, Air/Water/Magma subclasses, ModItems/ModTiles, ConfigHandler, AEDocumentation, three packaged Apparatus recipes and language inspected. Native Ars5.13.5 SourceUtil.takeSourceMultiple recipient reviewed in [Siren audit](ars-elemental-pinned-siren.md). No movement, damage, multiplayer or chunk tests; defaults are not assembled overrides.

## Craft and build a vertical route

Worn Notebook **Crafting → Elemental Upstream Blocks** registers an intro and all three crafting pages; JEI **Enchanting Apparatus** gives these recipes. Each outputs1, SourceCost0, keepNbtOfReagent=false; listed ingredients are consumed by craft.

| Output ID / name | Reagent | Pedestals | Stage/burden |
| --- | --- | --- | --- |
|`ars_elemental:water_upstream` / Bubble Current Elevator|Soul Sand|Air Essence + Water Essence + four Prismarine Shards|Apparatus/Nether Soul Sand plus prismarine; optional aquatic travel.|
|`ars_elemental:magma_upstream` / Magmatic Current Elevator|Soul Sand|Air Essence + Fire Essence + four Magma Blocks|Apparatus/Nether materials; optional lava traversal.|
|`ars_elemental:air_upstream` / Slipstream Current Elevator|Gold Block|four Air Essences + Wilden Wing + two Shulker Shells|Shulker-gated material burden; not an early craft even though recipe Source0.|

Place selected block at shaft base and enter its vertical column in the matching medium. No activation item is used. Stack **same block type vertically below the operating top block** to multiply reach; a Water/Magma/Air mixture is not one combined stack. Supply native Source within lookup range10 for Air. Use redstone power to disable: neighborChanged saves hasNeighborSignal into tile.disabled; disabled persists in NBT. Remove signal to resume; no logic here generates a redstone elevator destination or teleports between paired blocks.

Top operation skips when blockState directly above is exactly equal to this blockState. Power starts1, increments for each contiguous same block **type** below; only operating top tile applies that stack's work under normal identical states. The scan AABB is from top block center to center46×power blocks above, inflated1.5 on every axis. Thus46 per stack is the raw height term, not a separately checked floor count. It queries LivingEntity, not arbitrary dropped items/vehicles, and has no block obstruction, owner or claim predicate in this body. Build an open safe route and test real headroom rather than promising the effect stops at a solid ceiling.

## Actual effects and Source cadence

| Elevator | Target / interval | Applied operation |
| --- | --- | --- |
|Air|Non-spectator, not in water or lava; every20 world ticks|Reset fall distance; if crouching Slow Falling, otherwise Levitation; duration50ticks, amplifier1, hidden particles/icon; hurtMarked + visual. It does not explicitly remove the opposite effect when switching crouch state, so overlap/immunity interactions need acceptance.|
|Water|Non-spectator, in water, not crouching; every2 world ticks|Water Breathing100ticks; set Y velocity=max(0.4,oldY+0.1), preserveX/Z, reset fall distance, particles, hurtMarked. This is direct entity velocity, not creation of vanilla Bubble Column blocks or a water-source-block requirement.|
|Magma|Non-spectator, in lava, not crouching; every2 world ticks|Fire Resistance100ticks; set Y velocity0.4 (not oldY+0.1), preserveX/Z, reset fall distance, particles, hurtMarked. Crouching skips both lift and new protection; prior Fire Resistance eventually expires. Do not teach permanently safe lava descent.|

Default costs per eligible pass: `airElevatorCost=10`, `waterElevatorCost=0`, `lavaElevatorCost=0`, each multiplied by stack power. Defaults configurable0..1000. Payment is once for the **whole nonempty target list**, not per entity. Air at20TPS with constant eligible occupants is code-derived10×power Source/second, unmeasured; empty list causes no debit. Water/Magma default0 bypass Source lookup, not a claim every pack config leaves them free.

For positive cost the tile calls native takeSourceMultiple(position,level,10,cost), then requires non-null, nonempty returned provider list and all providers isValid. Native helper can combine providers and restores recorded withdrawals if aggregate insufficient. **The post-extraction provider-list validity checks occur after payment**; no refund branch exists here for that later failure. After admission it applies effects to all queried entities without per-target addEffect success checks or rebate. An effect rejected by immunity/event can still consume the pass's Source. Range10 means native provider lookup semantics, not proof every location in a ten-block sphere or unloaded provider qualifies.

## Quest and acceptance handoff

Teach optional travel after native Apparatus/Source onboarding; choose Water/Magma before Shulker-gated Air if appropriate. Name Notebook **Elemental Upstream Blocks** and JEI recipe; give base-block, same-type stack, matching-medium, crouch and redstone instructions. Detecting elevator item is explicitly a craft proxy; manual operation acknowledgement should require observed ascent and controlled descent. Modest XP and shaft/landing construction blocks are useful rewards; do not reward free Shulker Shells to shortcut Air's recipe gate.

Unrun positive/boundary cases: one block versus two same-type blocks versus mixed stack; actual AABB edges/headroom; one versus many occupants same debit; empty column no debit; Air insufficient/invalid providers and effect rejection after debit; Water flowing versus source water; spectator/crouching exclusion; Magma crouch protection expiry and outside-column danger; Air Slow Falling/Levitation overlap; redstone pause/reload. No item/boat elevator, safe-lava guarantee or gameplay acceptance inferred. Whole addon still researching for remaining devices/gear/worldgen/dependency/pilot review.
