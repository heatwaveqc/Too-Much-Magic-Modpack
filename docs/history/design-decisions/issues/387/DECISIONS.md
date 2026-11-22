# Issue #387 — Six-mod intake and Weeping Angels design decisions

- **Captured:** 2026-11-22.
- **Exact original body:** [snapshot-2026-11-22.md](snapshot-2026-11-22.md).
- **Original issue `updated_at`:** `2026-11-22T20:20:33Z`.
- **Live issue:** https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/387.
- **Current indexed-source inventory:** https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/431.

## Supersession and current scope

1. **Owner-approved six-mod next comprehensive unstable inclusion attempt (2026-11-20):** Mowzie's Mobs, Doctor Who: Weeping Angels, Aquamirae, Sons of Sins, Aura Cascade Reimagined, and Arcane Archives: Reborn. Approval is for an *attempt*, with official artifact verification/config/balance QA, NOT a claim of already included/tested, and not full-build authorization.
2. **Later installed-source evidence (2026-11-22, #431):** **Arcane Archives: Reborn 1.0.2 already indexed**. The five other approved six-mod candidates are not indexed. The Graveyard is a **separate seventh approved addition** (#386), also still absent. No duplicate Arcane Archives addition.
3. **Later owner selection in #403:** **Born in Chaos was selected** for the next comprehensive QA, superseding the original #387 phrase that it is 'research-only'. It is an *additional* selection, NOT a retroactive member of the original six, and PR #403 remains draft/unindexed. **Runes of Wizardry remains research-only** without a verified 1.21.1 port.
4. **Weeping Angels destinations:** Original brainstorming wanted positive weighted targeting, suppression of local displacement/fallback, and possible hard Newbie exception. The later explicit owner decision **supersedes these proposals**: keep **native local and interdimensional teleportation**, uniform native selection/fallback, no custom teleport code or weighting. Allowed six destination dimensions: Overworld, Aether, Bumblezone, Twilight Forest, Aurorian, Eternal Starlight. Native blacklist must cover every other **actual loaded** dimension, including JAWR Spirit World; draft #392 is partial and not merge-ready pending full registry census.
5. **Eternal Starlight escape:** an earlier warning that Angel arrivals are stranded was **superseded by source proof** of locally obtainable Orb of Prophecy/portal materials. Self-escape is source-feasible but not player/gameplay-verified; still require actual escape testing. Other return routes also require in-game QA.
6. **Weeping Angels traps and combat:** current owner-directed encounter spec is **structure-only dormant statue traps**, sculk sensors for chambers / hidden tripwire for corridors; no random biomes/Snow Angels, permanent theft disable. Physical damage compatibility and claimed-block safety remain implementation/QA work. Draft #392 contains updated `block_breaking=true` pilot conditional on FTB Chunks claim protection checks; the older 'block breaking entirely undecided' line in #387 is stale. Do not claim actual claim-safety PASS.

## Current live implementation ownership

- [#392](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/392) Weeping Angels source candidate, dimension blacklist, structures, damage and claimed-block safety.
- [#399](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/399) four mob/structure pins and mandatory libraries; [#425](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/425) Aura Cascade candidate.
- [#386](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/386) separate Graveyard approval; [#403](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/403) Born in Chaos and further selection metadata.
- [#431](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/431) source-index readiness/reconciliation.

Archived research/version lists and obsolete alternatives are historical evidence, **not current implementation instructions**. No archive document authorizes the next comprehensive build.
