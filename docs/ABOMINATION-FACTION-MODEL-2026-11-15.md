# Abomination architecture — theoretical model locked (2026-11-15)

**Status: selected theoretical target; not a claim that the native 1.21.1 hybrid faction or four-tree runtime works.**

## Identity and progression

- **Abomination = Vampire + Werewolf.** Neither lichdom is a prerequisite. Lich stacking remains prohibited pending a separate explicit decision.
- The intended TeamLapen integration is **one custom playable faction**, `tmm_transformations:abomination` (displayed as *Abomination*). The `Vampirism` framework's faction handler currently stores exactly one active faction.
- Retain and independently earn four underlying progressions: **Vampirism vampire**, **Werewolves: Become a Beast werewolf**, **JAWR vampire**, and **JAWR lycanthrope**. Acquiring a base vampire/werewolf identity must never auto-grant JAWR levels, advanced powers, quests, or unearned skills. JAWR altar-and-trapped-werewolf is an advanced lycanthropy milestone; JAWR Lilith is the vampire milestone.
- A future **combined Abomination skill interface** exposes two independently paced TeamLapen branches plus an Abomination-specific branch and JAWR progression. Do not overwrite native skill histories or simply cast vampire/waswolf skills to a new player generic type; their code expects `IVampirePlayer` and `IWerewolfPlayer` respectively.
- **Explicit hybridization only.** Uncontrolled bite/infection cannot make a hybrid. The transition bridge must use preflight, state snapshots, persistence validation and rollback before switching factions.
- Reuse JAWR native dual-affliction player data and its single-form enum as the first runtime candidate. Its dual-affliction code is structurally permissive, not live-proven. The later faction must delegate native actions using correct original typed player attachments and preserve separate earned/unlocked skill histories.
- Use **one** physiology owner for hunger/blood, protection, cooldowns, rendering and death arbitration. Vampirism is the proposed canonical blood source, not a production fact. JAWR cure currently clears all active JAWR afflictions; it must not silently erase the Abomination.
- Wolf and vampire forms must be mutually exclusive on one body. Preserve both original weakness sets, without multiplying duplicate survival effects.

## Two-ability experimental proof of delegation

The **first runnable slice is intentionally smaller than a real new faction**. Until the 1.21 faction and skill serialization adapters exist, the test runs **only** via operator commands. It probes JAWR's two actual native attachments, then delegates to **native HissingAction** (Vampirism) and **native HowlingAction** (Werewolves) on each mod's own typed player attachment. The test deliberately skips original skill entitlement to isolate action-engine compatibility; it does not unlock any tree, change a faction, persist synthetic levels or grant progression. Each delegate still gets its native `canUse` check and its own bounded test cooldown. The command must be disabled by default, requires operator permission, refuses unverified JAWR hybrid observations and missing mods, and reports native entitlement separately.

This is an **adapter proof, not the custom faction implementation**. A successful test shows one player carrying JAWR vampire + wolf states can use both native action implementations under controlled conditions. It does **not** show four-tree native UIs, faction equivalence, normal hotkey dispatch, real unlock prerequisites, or survival reconciliation. Do not represent it as such.

## Next-stage implementation gates

1. Confirm two JAWR afflictions can coexist in either acquisition order, survive save/relog and preserve progress and forms.
2. Implement a proper `IPlayableFaction<AbominationPlayer>` using the **embedded 1.21 Vampirism** faction API, not 2.x standalone FactionApi. Register on the correct lifecycle, with independent persisted progression data and safe migration.
3. Build and wire custom skill tree registry nodes; verify skill screen, dynamic reload, synchronization, independent costs and progress. Make original action dispatch respect the single new faction without raw casts or automatic grants.
4. Promote the two native action adapters from operator-only testing into earned skill nodes, with full original permission, cooldown, action event and client packet processing. Test one Vampirism and one Werewolves action before expanding.
5. Integrate visual, diet, weakness, other species exclusions, permanent cures, death arbitration, and multiplayer, then enable by configuration in disposable worlds.

See `docs/JAWR-VAMPIRE-LYCANTHROPE-BINARY-AUDIT-2026-11-15.md`.
