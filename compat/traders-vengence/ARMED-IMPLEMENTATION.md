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

### Phase 2 checkpoint
- [x] PneumaticCraft reference source and adapted standalone model staged, exact original texture blob verified; see `donor-reference/pneumaticcraft/provenance.json`.
- [ ] Caliber Arms blocker: previous JAR visible in Library, raw-byte materialization denied. Need accessible JAR/source; no unverified assets imported.
- [ ] PneumaticCraft sounds: individual provenance/license mapping pending. No sound bytes imported.
- [ ] Register/adapt model layer for iron golems, synchronize animation and integrate NPC-only combat. No behavior changed yet.

### Phase 3 checkpoint — uncompiled source integration
- [x] Narrow vanilla Villager.spawnGolemIfNeeded redirect stamps only successfully returned MOB_SUMMONED iron golems. Stamp persists; no command/spawn egg/player-crafted/legacy-unknown golem promotion. Existing natural golems without recorded provenance remain unarmed by design. Village structure-generated guardians also not included unless the villager-spawn hook created them.
- [x] NPC-only minigun goal reuses existing target and native LOS, pardons and vanilla melee fallback; donor spin constants/logic adapted. Provisional 16-block range, 5 game damage, ten-tick damage interval. No ammunition, player equipment, damage-type override or collateral bullet entities. Tune/verify in game; audio pending.
- [x] Server-to-client angle/speed heartbeats and client-only shoulder render layer wired; model pose/aim unverified. No server-side client classes loaded by packet handler.
- [ ] Run module compile + quick checks; actual Mixin injection, spawn provenance, combat, renderer and multiplayer sync unverified. No boot or integration assembly.
- [ ] Pistol source/assets still inaccessible. Keep Magic Missile for villagers until an audited replacement is ready; wandering trader pistol target/visual/combat hookup remains pending.

### Phase 4 — owner-approved donor parity (supersedes provisional Phase 3 values)
Pinned upstream CommonConfig.java defines base_range=50 and base_damage=6; Minigun.java getRange uses base range plus upgrades, StandardGunAmmoItem inherits 1.0 range/damage multipliers. DroneAIAttackEntity attempts firing each tick (16-block AI engagement range); SentryTurretBlockEntity attempts every 8 ticks and has 16-block default targeting range. Owner wants the full 50-block base weapon range, using handheld/drone tick cadence and standard ammo, no upgrades/special ammo. Golem now attempts 6 HP once per tick after spin-up; normal vanilla damage cooldown applies (NOT 120 effective DPS). Dedicated local standard damage type uses donor scaling/exhaustion, no armor/cooldown bypass. Native FOLLOW_RANGE transient modifier for eligible golems only; world test verify first acquisition at 49 blocks, non-acquisition beyond 50, reload, pardon, LOS, effective damage and armor. Other golems untouched. Previous Phase 3 head b234c35 passed quick module and bootstrap CI; this new commit requires fresh checks. No game boot authorized.
