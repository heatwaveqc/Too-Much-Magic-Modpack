# Trader's Vengence: golem lava culpability (#377 / #412)

## Immutable source and reconciliation

- Current pack base: `test/full-integration@2a2b2382e37f1555f6f0bde98053ac7eec61a8fd`.
- Frozen reviewed PR #412 donor: `309f6538c010fbfd4e4abb52a8be6ba8c90ff03a`.
- Existing canonical installed module selector remains `compat/traders-vengence/`, `traders_vengence`, in `manifest/first-party-runtime.json`.
- The donor PR's current head uses a separate whole-module rename to `compat/traders-vengeance`, `traders_vengeance`, changing dozens of paths and persisted identifiers. **Do not merge this rename via #412**: it risks restoring/duplicating a second NeoForge module and changing world-persistent keys. That rename requires its own review and migration if ever approved.
- Selected **only three donor implementation files**: `runtime/VendettaEvents.java`, `runtime/VengeanceRules.java`, and `runtime/VengeanceRulesTest.java`. For each, normalized only the donor's module Java package/ID/method references back to the canonical selected legacy namespace. **Verified that removing the donor's small #377 additions from each normalized source reproduces the baseline file byte-for-byte**; no other source lines, minigun assets, NPC weapon behavior, module metadata or licenses are altered.
- No source clone, extra compilation module, refactor, Packwiz change, quest changes, additional state or configured trader behavior.

## Behavior now present in source

On server-side `LivingDeathEvent`, and only on an actual `IronGolem` death where the final damage source is exactly Minecraft `DamageTypes.LAVA`, enumerate online `ServerLevel.players()` **once** and apply existing world-global, UUID-based `TraderBlacklist` vendetta to each player at inclusive *3D Euclidean distance* 10 blocks from the dying golem (squared distance ≤100). No requirement for player kill credit, ownership or provenance: includes player-built and villager-summoned golems, but does not give player-built golems a minigun.

This branch executes **before** the old player-kill early return; original player-killed Wandering Trader/Villager, actual golem damage, Priest and Hero-of-the-Village pardon hooks are preserved.

**Intentional exclusion:** lethal `ON_FIRE` afterburn is not `LAVA`, nor are campfire, magma, generic fire, fall, or other damage types. No last-contact tick scanner or reclassification has been added; if gameplay shows iron-farm deaths normally occur from afterburn, that is a separate narrow source/owner engineering check. The owner already requested exact death-source discrimination.

## Evidence and outstanding B/C

- Type A: current `compat/traders-vengence` NeoForge 1.21.1 Java 21 module `clean test build`, with exact radius regression: origin, exactly 10, (6,8,0), 10.0001 outside, (8,8,0) outside; all older module tests retained. CI metadata and branch lifecycle verification. Test only the selected module; no independent gameplay boot.
- Type B/C on next *separately authorized* build: direct-LAVA lethal vs afterburn and false-positive sources; zero, exact-ten and outside-ten radii; online multiple/offender/bystander players; other dimension; natural/player-created/villager-created golems; persistence and new offenses after pardon; unchanged native trader/villager offending and player-built golem unarmed status.
- No author-approved full modded assembly, dedicated-server smoke, Prism package, client launch or in-world test was run as part of this source change. A source/CI green result is **not** gameplay acceptance.

## Review notes

The branch and source-ownership handoff to PR #412 and the code cleanup issue #377 should cite this document and merge commit; PR #412 remains a historical/donor source and does not need to be replayed wholesale.
