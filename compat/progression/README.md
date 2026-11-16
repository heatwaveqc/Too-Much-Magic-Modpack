# Standalone player-enhancement progression core — early implementation

**Status:** development slice; NOT yet approved for the integrated modpack. **Target:** Minecraft 1.21.1, NeoForge 21.1.265, Java 21. Namespace: `tmm_progression`.

> **Monk Tyz tree parity/cost policy (2026-11-15):** The pack's **Tyz** Monk tree (not this standalone module's **native** technique point ledger) must total approximately **10x the full-purchase SP of a standard non-Monk specialist tree** and confer **80–90% of the relevant final optimized TMM specialist attribute for every Monk-modified trait**; 85% is the preferred midpoint. Because the global MXP 1.2^P award normalization is on **lifetime MXP levels**, this is far beyond 10x nominal ordinary XP, subject to a playability/pacing simulation. See [design contract](../../docs/balance/MONK-SPECIALIST-PARITY-AND-POINT-COST-2026-11-15.md). The standalone powers may remain baseline/native and are not charged as Tyz ranks unless explicitly migrated; do not edit this pilot JSON based on Tyz-cost economics alone. **Specification only.**

## Goal and ownership

This is a **reusable engine** for data-defined player disciplines such as Monk and Hunter, not a new version of the Vampirism-based Monk pilot and not a Tyz skill wallet. A discipline should primarily be a datapack path plus **occasional reusable Java effect handlers** rather than handwritten progression code. No mandatory Monk, Hunter, Vampirism, Werewolves, JAWR, Curios or Tyz dependency. The pack-global MXP/Tyz wallet remains separate.

**Monk classic parity remains governed by** [standalone design PR #100](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/100). This pilot intentionally DOES NOT implement all 25 classic powers. All 25 must eventually be attainable by one Monk natively without Tyz points; post-dragon Momentum Gliding replaces original Creative Flight. The earlier `compat/monk` prototype is not integrated into this module.

**Hunter creature taxonomy** is researched independently under [PR #97](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/97), so the tags supplied here are explicitly *pilot vanilla fixtures*, not the authoritative pack-wide taxonomy.

## Config / datapack interface

Create `data/<your_namespace>/tmm_progression/paths/<path>.json`. See both sample files in `src/main/resources/data/tmm_progression/tmm_progression/paths/`: `monk.json` and `hunter.json`. JSON datapacks reload on `/reload`.

| Key | Meaning |
| --- | --- |
| `schema: 1` | Strict format version; invalid JSON/policy errors reject the path |
| `exclusive_group` | Optional mutually exclusive path set; external transformations still need their own arbitration |
| `rank_xp` | Cumulative XP thresholds starting with 0; one entry per rank |
| `points_per_rank` | Native optional technique points; **not Tyz MXP** |
| `activities` | `event`, `target` ID/#tag, `xp`, `cooldown_ticks`; XP is credited only on trusted server events |
| `milestones` | `id`, AND-joined `criteria` (event + target + count), optional milestone IDs in `after`, one-time `xp` and `points` |
| `skills` | `id`, `max_rank`, `required_rank`, `base_cost`, `step_cost`, optional required skills/milestones, and `effects` |
| `auto_ranks` | For mandatory milestone/rank-granted powers; rank thresholds at which effect ranks automatically increase **for free** |
| `effects` | Native effect type with optional `target`, `weapon`, `damage_tag`, `attribute`, `operation`, `per_rank`, `tier`, `empty_hand`, `unarmored`, `params` |

Event types currently supported: `kill`, `block_break`, `external`. First Sunset and other cross-mod achievements can be added by a trusted adapter calling `ProgressionApi.recordExternal(serverPlayer, "namespace:event")`. That API **does not join the path** and awards only a configured milestone; `ProgressionApi.join(player, pathId)` must be called by a verified enrollment ritual. The only current in-game path join is an OP command for QA.

### Built-in effect handlers

| Type | Gameplay |
| --- | --- |
| `attribute` | Stable, reversible attribute modifier; `per_rank` and `operation`; resource-location attribute |
| `melee_bonus` | Added melee damage matching target entity ID/tag and optional held weapon ID/tag; `empty_hand` is supported |
| `damage_multiplier` | Damage reduction by source type/tag, with rank-scaled fraction; capped at zero damage |
| `break_speed` | Multiplies speed on matching block ID/tag; allows unarmed mining condition |
| `harvest_tier` | Effective vanilla tier 0..3, limited by attained rank; can make bare hands harvest higher tiers |

Supported generic conditions: `empty_hand`, `unarmored`. Default values mean no restriction. Tag selectors start with `#` and are resolved from live server registries.

**New unusual power?** Register once in Java: `EffectHandlers.register("yourmod:blink", handler)`, with implementation of the `Handler` interface. The datapack can then refer to `{"type":"yourmod:blink","params":{"range":"16"}}` under any skill. Server hooks offered: `tick`, `incoming`, `harvest`, `breakSpeed`, `blockBreak`, `kill`. Registration must be performed by trusted Java before datapack reload; arbitrary scripts in JSON are not executed.

### Persistent player ledger

Saved per player: path enrollment, rank, cumulative XP, native technique points, purchased and automatically granted skill ranks, completed milestones, criterion counts. Native attachment is copy-on-death; transient attribute modifiers are reconciled, rather than permanently stamped. Invalid reload definitions do not deliberately delete stored progress. There is **no player GUI or client sync protocol yet**; command output is server-side.

### QA commands

```text
/tmmprogress list
/tmmprogress status tmm_progression:monk
/tmmprogress buy tmm_progression:hunter tmm_progression:undead_slayer
/tmmprogress respec tmm_progression:hunter
/tmmprogress admin join tmm_progression:monk        # OP-only
/tmmprogress admin event tmm_progression:monk_first_sunset  # OP-only
```

To run the module from this directory with Gradle: `gradle --no-daemon build` then `gradle runServer` or `gradle runClient`. Github Actions workflow `progression-core.yml` checks compilation/JUnit on pushes and PRs; no successful run is claimed yet.

## Incomplete / guarded behavior

1. **Not full Monk or Hunter:** Monk first-sunset initiation, 15+ classic mechanics beyond this pilot, extraordinary custom effects (armor lock, fist slot, Fortune/Silk, water walking, leap/blink, arrow catch, Momentum Glide, animal charm, Withering Stare, etc.), and Hunter specialized tracking and taxonomy integration are outstanding.
2. **Tag semantics:** Pilot `tmm_progression:` tags cover a handful of vanilla entities/tools only. No claims about hundreds of modded mobs; consult Hunter taxonomy research. Do not assume external mods' tags exist.
3. **XP legitimacy:** `BlockEvent.BreakEvent` is a pre-break event; canceled events are ignored but other systems may subsequently veto a break. TreeChop/mass-break and automation attribution need audited post-commit bridges. Kill ownership currently credits the immediate source player; summon ownership/spell credit needs adapters. Activity cooldowns are process-local.
4. **Equipment and compatibility:** Armor checks use four vanilla armor slots, not Curios. Mining tiers currently implement vanilla stone/iron/diamond checks and not every modded tool tier. Event interactions with other combat modifiers require dedicated integration testing.
5. **UI, migration and safety:** No custom HUD/skill tree GUI, client-sync, versioned progress migration, path-leave/cure/reset, configured respec price, anti-farm event dedupe, effect-handler hot reload/registration timing tests, or pack-manifest inclusion. No in-game QA has passed.
6. **Lifecycle:** Unregistering/removing a datapack path retains its saved ledger but cannot apply or respec effects until the definition returns. Effects have transient IDs; login/logout/reset consistency must pass tests.
7. **Security:** Never expose `join`, `recordExternal` or arbitrary XP awards to untrusted client packets. The integration adapter must validate true initiation/trial completion server-side.

## Acceptance gates before merging into test/full-integration

- [ ] GitHub CI gradle build and all JUnit tests pass
- [ ] Dedicated-server create/save/respawn/dimension/logout/rejoin test, including negative `/reload`
- [ ] Monk First Sunset native enrollment; per-player saved automatic baseline grants
- [ ] Hunter enrollment/transform exclusion; target-specific weapon damage and XP validation
- [ ] Damage/defense modifier coexistence with magic and modded equipment
- [ ] Harvest check and break speed with bare hands across vanilla and installed modded tiers
- [ ] Core effect handler contract for remaining classic powers, gameplay implementations in Monk/Hunter layer
- [ ] No unexpected XP from creative, spectators, canceled/non-player block breaks or kill credit
- [ ] Later migration and GUI plan; only then integrate JAR and dependent paths into pack manifests

This is **an implementation skeleton and functional pilot**, not release-ready Monk/Hunter or a verified build.
