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

## Endgame equipment hierarchy and excluded mods (owner decision, 2026-11-14)

- **Exclude Re:Avaritia and Extended Crafting** (originally on hold, inadvertently pinned). No Infinity-tier equipment or separate ultra-crafting mechanic should obsolete Draconic Evolution or ProjectE Gem Armor.
- Draconic Evolution and Chaos Guardian remain the critical late-game step; ProjectE and Project Expansion unlock **only after** the Guardian, as described above, and Gem Armor remains a meaningful late-game reward.
- The Endgame category has detailed chapters for Draconic Evolution, ProjectE/Expansion and Replication. Progression has an Endgame Roadmap overview, as well as owner-approved Skills & Advancement and Exceptional Power: Reactors chapters.

## Quest economy and resource selling — owner decision, 2026-11-14

- Add Quest Shop and Cooking for Blockheads (the latter lives in Food & Fishing). Strongly prefer thematically appropriate fixed-item quest rewards; currency only where a fixed item would be contrived, including deliberate selling contracts.
- Quest Shop team currency may be generated indefinitely using ProjectE post-Chaos Guardian. No arbitrary item sell-anything store. Twelve copper/emerald/diamond exchange quests, three introductory sales and one highly inefficient repeatable sale per material; details in [QUEST-SHOP-ECONOMY.md](QUEST-SHOP-ECONOMY.md).
- The shop can replace luck-dependent gathering (amethyst, Heart of the Sea, selected Artifacts) but never bypass signature boss/technology gates.
