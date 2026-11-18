# Armed Trader's Vengence — implementation ledger (2026-11-18)

Owner-approved: NPC-only high-caliber Caliber Arms pistol for all villagers and wandering traders; PneumaticCraft minigun for village/naturally spawned iron golems, never player-built golems. Preserve blacklisting, forgiveness, spawn rules and existing hostile-target policy. No player weapons, loot, recipes, new external dependency, packwiz changes, or world launch in this workstream.

## Milestones
- [x] Record goals and approved GPLv3 module license; preserve other modules' MIT licensing and packaging validation.
- [ ] Pin and review donor source/assets/licenses individually; preserve original notices, source provenance, modifications, and corresponding source.
- [ ] Stage only relevant audited assets/code, not entire donor mods.
- [ ] Replace villager Magic Missile with NPC pistol; extend equivalent behavior to wandering traders.
- [ ] Distinguish village/natural and player-built golems; preserve normal melee for player-built golems.
- [ ] Integrate NPC-only minigun state, animation, rendering and audio.
- [ ] Review dependency declarations, module compile, tests, packaging checks.
- [ ] In separately authorized gameplay QA: verify targeting, forgiveness, natural vs built golem behavior, save/reload, client visuals/audio, no-player acquisition and normal trades.

Dependency: stacked on PR #341, exact SHA 8cca84604664c316ac9b86b4c17735f39e8d2f63, to avoid dropping its notice/packaging work. Do not merge into active integration or promote an artifact without review. Initial GPL stage does not yet change combat. PneumaticCraft source candidate: https://github.com/TeamPneumatic/pnc-repressurized/tree/7bcbcbc744a851d158d924cfa3ce6e88fb8ccb28; Caliber Arms: https://www.curseforge.com/minecraft/mc-mods/caliber-arms (MIT claim; exact JAR assets/source still to audit).
