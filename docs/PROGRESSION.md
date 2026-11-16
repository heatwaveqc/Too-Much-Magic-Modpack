> **Owner-locked Monk balance and cost (2026-11-15):** Once optimized ATM10 references and TMM non-Monk specialist skill trees are established, each **numeric attribute actively modified by Monk** must individually reach **80–90% of the final corresponding optimized TMM specialist attribute** (85% design midpoint; no category averaging). Fully purchasing all ranks in the **Monk Tyz tree** should cost roughly **10x as many shared Tyz skill points** as a fully purchased canonical specialist tree (initial 9–11x tolerance). **MXP growth factor remains under review**: current prototype is `1.2^P`, with owner considering `1.1^P` or linear `1.0^P`; evaluate `1.01–1.05` sensitivity as well. At `1.0^P`, 10x SP requires exactly 10x ordinary XP from equivalent fresh starts; >10x XP is conditional on growth >1. Simulate feasibility before implementing. The standalone Monk core's automatically granted techniques or native points are a **different economy** and must not be conflated with Tyz SP. This supersedes prior fixed-85%-only targets. See [Monk specialist parity and cost specification](balance/MONK-SPECIALIST-PARITY-AND-POINT-COST-2026-11-15.md), [ATM10 benchmark](balance/ATM10-SPECIALIST-BENCHMARK-2026-11-15.md), and [machine-readable metrics](../manifest/atm10_specialist_benchmark.json). **Research-only; live skill-tree JSON unchanged.**

> **Superseding technology decision (2026-11-15):** The active Progression chapter is **A Sufficiently Advanced Magic**, currently covering Draconic Evolution FE generation, storage, crystals and its high-risk Reactor, plus future explicitly approved technology mods. Bigger Reactors original/Rework and uranium/graphite/cyanite machinery are retired. Older B4 and technology descriptions below are historical and not active instructions.

# Locked design decisions — implementation deferred

> **New owner target (2026-11-15), design only:** Remove TeamLapen **Vampirism** and **Werewolves** after Hunter/Monk dependency migrations; **JAWR alone owns vampire and lycanthrope physiology**. Do **not** preserve dropped-mod worldgen, villages/structures, raids, pets, faction minions or lord systems. Only selected personal skills migrate to the existing Vampire/Werewolf Tyz categories. See [retirement target](TEAM-LAPEN-RETIREMENT-TARGET-2026-11-15.md) and [source-backed skill triage](TEAM-LAPEN-SKILL-TREE-TRIAGE-2026-11-15.md). Older dual-vampire and TeamLapen Abomination designs are superseded; no runtime files changed. 

> **Updated owner decision (2026-11-14):** **ProjectE Philosopher's Stone uses a *tradeable* boss-associated drop as its center ingredient**, with **three alternatives** (Wilden Tribute, Gaia Spirit, Neo Vitae Mine Key). **Chaos Shard is required for Red MK3+ Power Flower direct recipes and MK3 upgrade**. This replaces the old per-player boss-kill gate proposal; older all-post-Chaos rules below are legacy. EMC coverage across all items is separately planned. See [the current tiered proposal](PROJECTE-TIERED-GATING-PROPOSAL.md) and [the full checklist](FEATURE-CHECKLIST.md). The historic post-Chaos-only rules below describe the **previous design**, not an implemented lock. Do not rework live recipes before the boss IDs and negative bypass audit are verified.

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

## Full technology chapter — user decision (2026-11-14)

- **Exceptional Power: Reactors is the entire approved technology questline**, not just a BiggerReactors subsection. It includes BiggerReactors Rework's uranium FE generator, cyanite/Industrial Processor, its modular Battery, optional turbine/exchanger branch; Draconic Evolution's **Energy Core multiblock FE bank**, pylons and crystal network; and the late **Draconic Reactor Core, four reactor stabilizers, injector, Flux Gate regulation and containment planning**.
- Differentiate the Energy Core *storage bank* from Draconic Reactor *generator*, and both from BiggerReactors. Advanced operation checks are manual, safety-conscious, not fake telemetry automation. More construction instructions in [POWER-TECH-QUEST-GUIDE.md](POWER-TECH-QUEST-GUIDE.md).
- No extra FE transport/logistics mod. Do not unlock Chaos Shards, Draconic Reactors or ProjectE by checking a quest. Any required Chaos Guardian recipe gate is enforced independently. Uranium/draconium scarcity is still pending world-generation enforcement.
