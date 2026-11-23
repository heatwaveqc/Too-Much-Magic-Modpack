# Chaos Bunny: persistent population, five-boss cap and one-time trophy

**Status: approved design, partial source work in draft PR #556. Full independent boss runtime NOT YET CODED. No Minecraft B/C QA run.**
Authority: issue #549, 2026-11-22 owner clarification. Supersedes sixth-timed-wave boss and portal trophy. The complete horde Gateway is 5, 10, 20, 40, 80 Killer Bunnies; original wave timers remain.

## Population semantics (maximum FIVE alive in the entire saved server)

| Trigger | Count before | Result |
|---|---:|---|
| Open Gateway with none | 0 | Start five horde waves. Do not create a boss yet |
| Successfully complete Gateway with none alive | 0 | Portal closes and releases exactly one independently saved Chaos Bunny |
| Open Gateway with existing bunnies | 1–4 | Teleport ALL existing bunnies to portal and immediately create ONE additional independent boss |
| Open Gateway at five | 5 | Teleport all five; do not make a sixth |
| Gateway fails after recalling/spawning | 1–5 | World bosses REMAIN; only Gateway-owned horde rabbits are subject to failure cleanup |
| Boss killed | 1–5 | Remove its UUID from persistent global registry after actual terminal death, freeing one slot |
| All players offline / chunk unloaded / restart | 1–5 | Each rabbit is saved, not despawned, but does not need to tick while its chunk is unloaded |
| Gateway completed after its preexisting rabbits were all killed during the horde | 0 | Spawn a new independent boss at completion |

The hard cap counts **all living bosses including unloaded chunks and all dimensions**, not five per player, Gateway or dimension. Treat every Gateway Opened event as one transaction: one new rabbit maximum, all existing ones summoned. A Gateway that fails still leaves those already summoned: using a pearl while a boss is loose actively makes the world more dangerous. Suppress duplicate opening callbacks and repeated completion events. If cross-dimensional teleport or an unloaded-entity lookup cannot be performed safely, fail closed rather than clone/remove a boss.

The boss is **not a Gateway wave entity**: no Gateway leash, timer, wave cleanup, nearby-player requirement, reward array or world-owned Gateway entity registry. Retain vanilla name/AI characteristics as appropriate but allow attacking ONLY real survival/adventure players, never pets, golems, villagers, NPCs, fake players, creative/spectator players or nonplayer entities even in retaliation. No permanent forced chunk tickets. Safe destination spacing avoids all five suffocating or stacking; preserve individual boss UUID, health, data and effects.

## One-time trophy via advancement

Actual existing TMM patterns: the Curio Bag is from a claimable FTB Intro quest, while the starter kit has a native one-shot advancement with a reward function and Wilden Chimera has a native player-killed-entity advancement. Reuse the last two patterns.

**Source already added in draft PR #556:**
- kubejs/data/tmm/advancement/chaos_bunny_slayer.json: native minecraft:player_killed_entity criterion, minecraft:rabbit AND NBT tag tmm_caerbannog_boss; permanent per-player advancement
- kubejs/data/tmm/function/rewards/chaos_bunny_trophy.mcfunction: exactly one give of kubejs:chaos_bunny_trophy to the advancement recipient
- Both newly Packwiz-indexed; scripts/verify_chaos_bunny_advancement.py ensures predicate/reward/Packwiz matching
- Credited killing player only; no automatic team or nearby participant award. Normal survival cannot revoke advancement. Ordinary horde rabbits are excluded by tag.

**Unimplemented companion change:** remove boss trophy from the Gateway completion reward array and preserve zero normal loot from all special rabbits. Once that is done, the only ordinary reward avenue is the advancement function, independent of mob drops and Looting.

## Implementation-ready slices

- [x] Per-player tagged rabbit-kill advancement plus once-only function and indexed source test (draft PR #556)
- [ ] Remove sixth boss wave / Gateway trophy, leave five horde waves; revise source verifier and Packwiz hashes
- [ ] Register server-only native Gateways 5.1.0 GateEvent.Opened and GateEvent.Completed callbacks, exact key tmm:chaos_bunny, each once per portal UUID; Gateway class is read via GatewayRegistry key
- [ ] Create independent RabbitType 99 boss, with apothic elite true randomized intrinsic combat buffs, boss tags, target-only-players AI and saved persistence; not a Gateway-owned mob
- [ ] Global SavedData or verified KubeJS server persistentData ledger including UUID, dimension and last known location; dedup, reconcile only confirmed deaths; guard world restart/unloaded entities and teleport races
- [ ] Summon all current bosses to new Gateway, including from unloaded chunks and other dimensions; brief bounded loading only while transferring, safe placement, no cloning or indefinite chunk tickets
- [ ] Preserve 5s post-successful-melee immunity trial, 2% normal protected tongue damage, aquatic boons and hostile magic inversions for boss only
- [ ] Reward and boss cap regression tests, tracked real game QA; NO assembly, full smoke or Prism client launch without separate authorization.

## B/C test queue -- not run

- [ ] First successful fifth horde wave creates one permanent boss; failed original Gateway creates none
- [ ] Reopen Gateway with one boss: second spawns immediately and old one teleports, including after old chunk unload or cross-dimensional relocation
- [ ] Repeated openings produce exactly five total; sixth only recalls five, no duplicate UUID or more than five through save/restart
- [ ] Fail a Gateway after opening with a boss: summoned rabbits remain, no Gateway cleanup; horde 155 drop nothing
- [ ] Kill one then reopen: frees exactly one slot, new rabbit arrives, previous living UUIDs retained
- [ ] Bosses actively target and attack ONLY eligible players, no pets/companions/NPCs or collateral; idle unloaded mobs do not burn TPS
- [ ] Credited first boss kill gives exactly one player advancement and trophy, not dropped loot; second kill none. Ordinary rabbit, boss killed by admin, and portal completion give none
- [ ] Test multiplayer kill credit (vanilla final killer only), death protection, crossdim load, 5 rabbits combat and five-wave TPS.

**Important:** draft PR #556 still contains the old sixth Gateway boss and completion trophy and must stay unmerged until these old paths are removed and independent spawn/registry runtime works.
