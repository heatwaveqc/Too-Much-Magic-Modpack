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

## B4 compatibility proviso
- BiggerReactors Rework 0.1.6 adds a separate battery and industrial processor along with reactor energy generation. Review the energy/storage features against our limited-tech policy before progression work; it was explicitly approved as the newer fork for testing.
- Botania Revived may expose optional Ars Nouveau integration; balance mana/source conversions and remove any forbidden storage acquisition path.
- Experimental Astral Sorcery and Draconic Evolution are boot-tested, **not yet gameplay-accepted**; Draconic Chaos Guardian remains the ProjectE gate.

## Introductory storage quest (owner decision, 2026-11-13)

- The first introductory quest is **Punch a Tree**: each player receives **one** Ars Elemental **Trinkets Pouch (27 slots)** as soon as they first obtain any log or stem (`#minecraft:logs`). No starting-inventory pouch grant; the trigger must be individual and once-only, not claim-farmable or shared-team limited.
- Implemented as an **integration candidate** in `config/ftbquests/quests/chapters/intro.snbt` with a persistent hidden per-player `tmm:first_log` advancement and player-context reward function. **NOT yet live/gameplay verified.**
- Other portable bags (Ars Spellcaster Bag, Occultism Satchel, all ProjectE Alchemical Bags) must be obtained normally by crafting/ritual/progression, subject to ProjectE's separate Chaos Guardian lock.
- **No portable bag may be inserted into another bag**, regardless of which mod owns either bag. A shared `#tmm:portable_bags` tag extends Ars Elemental's blacklist, but Occultism and ProjectE currently lack full cross-mod enforcement. Treat that gap as a **release blocker**, not an accomplished policy.
