# Armed Trader's Vengeance — implementation ledger (2026-11-18)

Owner-approved: NPC-only high-caliber Caliber Arms pistol for all villagers and wandering traders; PneumaticCraft minigun for village/naturally spawned iron golems, never player-built golems. Preserve blacklisting, forgiveness, spawn rules and existing hostile-target policy. No player weapons, loot, recipes, new external dependency, packwiz changes, or world launch in this workstream.

## Milestones
- [x] Record goals and approved GPLv3 module license; preserve other modules' MIT licensing and packaging validation.
- [ ] Pin and review donor source/assets/licenses individually; preserve original notices, source provenance, modifications, and corresponding source.
- [ ] Stage only relevant audited assets/code, not entire donor mods.
- [x] Replace villager Magic Missile with NPC pistol; extend equivalent behavior to wandering traders (source; QA pending).
- [x] Distinguish confirmed villager-summoned and player-built golems; preserve normal melee for player-built golems (source; QA pending).
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

### Phase 5 — exact Caliber Arms JAR received and audited
- [x] Uploaded Forge 1.21.1 JAR 1.0.0 SHA256 88068f10a62af5af35b2e8256137cb5160ea5eb2cc8f42644a0fa42722931f90; exact embedded MIT LICENSE.txt and CC0 CREDITS.md imported under META-INF/traders_vengeance/caliberarms, plus manifest of SHA256 hashes.
- [x] Selected M1911A1 (.45 ACP), original model (namespace only changed), original texture, 2 near firing sounds and 8 pistol reload sounds (identical donor bytes), local sounds.json entries. Sixteen extracted files verified by matching local Git blob SHA-1 to GitHub-created blobs. No Forge classes, donor JAR, player recipes, other firearms, or item registration imported.
- [x] Javap inspection of exact artifact GunType M1911: damage 7, maximum 420 RPM, 7-round magazine + chamber, empty reload 42 ticks, effective range 35 blocks (110 max travel); bytecode V.bulletDamage and resources confirm standard projectile-tagged bullet damage, scaling never. No public source repo located; implement small independent NeoForge NPC adapter, not falsely claim a source-code port.
- [ ] Wire pistol combat for villagers and wandering traders; render NPC-only cosmetic layer; test. Previous minigun parity head 7b298a9 passed quick and bootstrap CI. No game launch authorized.

### Phase 6 — pistol source integration
- [x] Added independent NeoForge NPC-only pistol cycle, sound registration and stand-alone client render layer for ordinary villagers and wandering traders (including baby pose approximation). Removed former Magic Missile bridge/mixin. Original Iron's dependency remains for existing Priest vendetta hook and packaging pin.
- [x] Existing target policy, pardon, line-of-sight, native armor/hurt cooldown preserved; no player-obtainable item or ammo, no Forge dependency, no collateral bullets, no headshot or damage falloff simulation. Base 7 HP, 35-block effective range, fractional 420-RPM attempt cadence, initial 7+1 capacity, empty 42-tick reload to seven rounds. Virtual unlimited reserve, current magazine is per-goal in-memory state (may reset on chunk reload). Reload sound event offsets approximated; only near/dry direct gunfire and reload audio, no distant simulation. Cosmetic model attachment/recoil is provisional; normal trading/Brain arbitration needs gameplay acceptance.
- [x] Added offline unit fixtures for cycle and all 15 audited asset/license hashes, plus packaged provenance.
- [ ] Recheck quick CI at final head. No game boot, packaging for distribution, or merge. Minigun audio licensing remains pending.

### Phase 7 — packaging gate
Actual Trader JAR verification now rejects missing/duplicated/mismatched audited Caliber Arms resources, including original MIT/CC0 notices, on build, paths, smoke and direct packaging. The source manifest requires the exact donor SHA and a 15-file allowlist; synthetic valid fixtures and missing-license/modified-OGG negatives exercise the gate. NPC installation is scoped to Villager/WanderingTrader subtypes, not arbitrary unrelated AbstractVillager merchants.

### Phase 8 — minigun audio audit and import
- [x] Exact upstream minigun.ogg and minigun_stop.ogg copied byte-for-byte (Git blob SHA verified); upstream project GPLv3 and both separately credited free sound sources preserved. Individual Freesound sample mapping remains unconfirmed. hud_init.ogg spin-up alias excluded due to embedded third-party commercial-library credit and no verified redistribution permission; use vanilla replacement. No donor dependency.
- [x] Combined local sounds.json adds two events; Caliber Arms audit hashes adjusted only for combined registry and provenance. Original pistol definitions/recordings untouched.
- [ ] Client looping/stop lifecycle and vanilla spin-up event integration; fresh CI and separately authorized gameplay QA. Previous full pistol head e8f3b83 passed both quick checks.

### Phase 9 — minigun audio source integration
- [x] Server spin-up on zero-to-active transition using built-in piston sound (lowered pitch), and authoritative firing flag in protocol v2. Immediate firing transitions plus ten-tick heartbeat; no client authority. Client one positional looping instance per tracked golem, stop recording once when firing ends; canceled on entity removal/world disconnect; stale snapshots fail closed. Donor default drone volume 1.0. No additional projectiles or damage changes.
- [ ] Fresh compile, unit/packaging CI and separately authorized in-world audio/lifecycle checks. Original spin-up file intentionally omitted; see Phase 8 audit.

### Phase 10 — minigun audio packaging gate
The runtime validator requires the two exact audited OGG Git blob identities and exact packaged bytes; excludes the uncleared HUD recording. Synthetic packaging tests cover missing, modified, and uncleared audio. No live game boot or distribution assembly performed.

### Phase 11 — owner-approved CC0 replacement
- [x] User supplied audio from the credited Pixabay mirror of SamsterBirdies' CC0 original minigun.ogg; verified original license and source credits. Trimmed 0.055–1.245 s, mono Vorbis, preserved natural speed; verified Git blob hash on upload. Replaced vanilla piston cue with new registered spin-up event, subtitle, pinned packaging hash, missing-audio regression, combined sound manifest and test hashes. No change to game spin timing or original PneumaticCraft firing/wind-down. See THIRD_PARTY_NOTICES.md for provenance. Prior full audio head 8205cbf passed quick and bootstrap checks; new head requires fresh checks. Gameplay testing remains unauthorized.
