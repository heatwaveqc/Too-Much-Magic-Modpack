# Locked design decisions — implementation deferred

These are design invariants, not claims about live game behavior.

1. **ProjectE and Project Expansion:** all usable systems post-Chaos Guardian (Draconic Evolution). Lock all acquisition routes and functionality, not just crafting-table recipes. Check loot, villagers, quests, advancements, item duplication/EMC, starter items, commands and integrations for leaks; admin commands may remain a test bypass.
2. **Replication:** its Deepslate Replica Ore must be available through the highest-tier Occultism miner (Eldritch Ancient Miner) instead of ordinary worldgen. Confirm the exact mod, recipe/item IDs and whether any generation path can leak before coding this.
3. **Eldritch Ancient Miner:** gate by advanced Cataclysm materials, provisionally from the Harbinger and Ignis. Reduce the previously assumed eight-Marid input to a balance-target range of **two to four**. Consider very late Botania materials as another pathway or extra requirement; compatibility and balancing still pending.
4. **Tech materials:** uranium and draconic dust belong in dangerous deep exploration or Occultism mining, not routine overworld ore farm. Final sources and generation changes pending.
5. **Ring of the Seven Deadly Sins:** no normal acquisition at all, including crafting, world loot, trades, quest rewards, mob drops or generated containers. Audit similarly overpowered one-shot gear.
6. **Renewability:** every item/resource eventually needs a reproducible route. Boss loot can remain challenging; alternative routes must not bypass the intended boss/technology gates.
7. **Quests:** FTB Quests will communicate the pathways after core mechanics stabilize, not substitute for locks.

Do not add placeholder broken KubeJS recipes using speculative registry names.
