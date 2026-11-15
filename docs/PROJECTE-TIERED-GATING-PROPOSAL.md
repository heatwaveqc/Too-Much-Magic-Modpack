# ProjectE — proposed two-stage unlock (2026-11-14)

**Status: DESIGN PROPOSAL after user requested moving ProjectE earlier than Chaos Guardian. NOT ACTIVE IN GAME.**
This proposal supersedes the old *all ProjectE post-Chaos Guardian* design **if accepted**. Existing ProjectE and Project Expansion JARs are already packwiz-enabled without the actual gate. A passing server boot is not a gate implementation. Do not advertise the pack as gated survival until all positive and negative acquisition tests pass.

## Motivation

Requiring Draconic Evolution's Chaos Guardian before the first Philosopher's Stone prevents players from experiencing ProjectE's iconic transmutation until very late. Conversely, handing out energy collectors and condensers immediately collapses much of the magical material economy and the intended Occultism/Neo Vitae progression. Split **initial transmutation** from **industrial/unbounded EMC production and endgame equipment**.

## Proposed playable tiers

| Milestone | What becomes available | What remains locked |
| --- | --- | --- |
| Early game | No ProjectE/Project Expansion survival acquisition or functional access | All ProjectE and Project Expansion |
| **First major magic boss: Gaia Guardian I OR Neo Vitae Foreman** | **Basic ProjectE:** Philosopher's Stone, Transmutation Table, covalence dust and conservative alchemical crafting/storage, with carefully capped manual EMC turnover | Energy Collectors, Anti-Matter Relays, Energy Condensers, Matter furnaces, automatic EMC farms, Gem Armor and other extreme gear, Project Expansion |
| **Chaos Guardian** | **Advanced ProjectE + Project Expansion**, including collector/relay/condenser automation, high-tier matter, powerful rings/equipment and expansion progression | Any explicitly excluded item such as Ring of Seven Deadly Sins; other technology/ore restrictions remain independent |

This is **either boss, not both**. It offers a nature-magic path (Botania Revived ritual, Gaia Guardian I) and a combat/dungeon path (Neo Vitae Foreman). Both are earlier milestones than the Chaos Guardian, but this needs in-world balance comparison. Gaia Guardian II can replace Gaia I if testing shows basic ProjectE comes too early. Do not infer that every Botania or Neo Vitae 1.21.1 port has the exact classic item or boss entity ID.

**Concrete boss context:** Gaia is Botania's altar-summoned guardian with Gaia Spirit rewards; the Foreman is a 600 HP Neo Vitae Mine Dungeon boss that drops Mine Dungeon Keys. A **loot item alone must not substitute for personal boss credit**, or players can trade, duplicate or loot it into an unlock.

## Mechanism — implementation contract

1. **Discover exact installed registry IDs before changing any recipes.** Query the actual pinned Botania Revived, Neo Vitae, ProjectE and Project Expansion builds/registries for Gaia entity and tier, Foreman entity, stone, table, all automatic EMC devices and expansion items. The sources are not permission to guess identifiers.
2. **Record first eligible victory server-side** per player (or explicitly defined team credit after multiplayer design review). Ensure Gaia I and Foreman grant the same starter access, and award credit only after the legitimate boss's death. Never grant access from holding a Gaia Spirit, Mine key, spawn egg, stolen trophy or quest checkbox.
3. **Enforce positive early gate** at every recipe type: ordinary crafting, alchemical/transmutation conversion, machine/altar crafting, possible ProjectE shift-crafting, other mod crafts and worldgen/loot/trades/quests. The starter unlock must be discoverable and available to a player who legitimately beat either boss.
4. **Enforce negative late gate**: no pre-Chaos Energy Collectors, Condensers, Relays, automated EMC farm, advanced matter gear, Project Expansion or unbounded EMC exploitation, even if the items arrive through creative/admin test placement, inventory grants, other players, loot or nonstandard crafting. Creative/admin bypass is separately labeled for testing only.
5. **Avoid item-by-item broken placeholders.** If ProjectE does not expose a comprehensive in-world use/EMC deny API, implement an appropriate first-party NeoForge runtime bridge instead of pretending recipe removal suffices.
6. **EMC values and economy:** deny/restrict EMC on boss-gated materials, upgrade components and self-reproducing loops; verify whether Quest Shop coin conversion after basic ProjectE undermines exploration. The current small-per-sale payout was designed with ProjectE available only post-Chaos, so assess prices again.
7. **No full ProjectE auto-knowledge.** Tome of Knowledge, cross-mod EMC knowledge, transmutation tablets and shortcut items must not bypass tier checks. Simulate fresh player, post-boss player, and Chaos-complete player.
8. **Update the actual Endgame/Progression/Quest chapters only after runtime gate mechanics pass.** Quest dependencies guide the user but never are the gate.

## QA matrix (initial and endgame must be tested independently)

- [ ] Fresh survival player, no boss: cannot craft, find, purchase, use or EMC-learn starter/advanced ProjectE.
- [ ] Gaia I legitimately killed: basic table/stone usable; high-tier EMC automation unavailable.
- [ ] Foreman legitimately killed (different player/world): same starter entitlement without Gaia, plus correct other dungeon consequences.
- [ ] Defeating either boss through a party: explicit personal/team-credit decision is consistently applied.
- [ ] A traded Gaia Spirit or Mine Dungeon Key does **not** confer boss credit or bypass gate.
- [ ] Boss rematch, multiple deaths, logout, dimension change and server restart do not duplicate or remove entitlements.
- [ ] Pre-Chaos tier-one player: no loot/quest/reward/trade/EMC/condenser/machine automation bypass.
- [ ] Post-Chaos: high-end ProjectE and Project Expansion work and normal boss-gated materials remain governed independently.
- [ ] Ring of Seven Deadly Sins remains entirely unavailable in survival; Re:Avaritia/Extended Crafting remain excluded.
- [ ] Farming common ore and selling to Quest Shop after tier one does not print effectively unlimited coins in a way that trivializes other milestones.
- [ ] Multiplayer split progression and cheats/admin toggles tested in a disposable server.

## Source audit required

- Existing design that previously locked *all* ProjectE: [docs/PROGRESSION.md](PROGRESSION.md), [docs/DESIGN.md](DESIGN.md), [docs/QUEST-DESIGN-RULES.md](QUEST-DESIGN-RULES.md).
- Original quest sale economy assumed post-Chaos ProjectE: [docs/QUEST-SHOP-ECONOMY.md](QUEST-SHOP-ECONOMY.md).
- Current integration and item roster: [manifest/mods.json](../manifest/mods.json), [manifest/feature-checklist.json](../manifest/feature-checklist.json).
- Implementation remains **planned** and must not silently modify known-good saves, quests or existing users' permissions. Until bridge and tests exist, this stays a prospective design.

### Key open choices

1. **Gaia Guardian I vs Gaia II**: recommended **I** for accessibility; raise to II if the early unlock trivializes middle tiers.
2. **Personal versus team first-kill credit:** recommend individual credit for owned skill/economy progression, but explicitly test coordinated boss fights so co-participants aren't unfairly excluded.
3. **Which basic ProjectE items are genuinely safe** with the pinned extensions, especially alchemical bag/EMC interactions: use a verified registry-item allowlist after a live balance audit.
