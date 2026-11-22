# Issue #445 — Historical decisions and current authority

- **Captured:** 2026-11-22.
- **Exact original body:** [snapshot-2026-11-22.md](snapshot-2026-11-22.md).
- **Original issue `updated_at`:** `2026-11-21T20:36:31Z`.
- **Current issue:** https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/445.
- **Current approved three-item cuisine pilot:** https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/450.

## Decision history

1. Initial research examined a potential full Vampire's Delight port and a smaller Brewin' and Chewin' blood-wine bridge; research text explicitly withheld approval for the smaller implementation.
2. Owner decided the **full** Vampire's Delight port is **deferred**. TeamLapen Vampirism/Werewolves/Vampire's Delight remain out of the TMM runtime; possible future adaptation is not current-sprint work.
3. The separate three-item JAWR cuisine pilot was **subsequently approved** and moved to #450: Blood Wine, Blood Sausage and Blood Pie Slice. Historic #445 text describes Blood Wine as **2×**; this is **superseded** by the latest #450 owner decision: **5× the actual blood value** of its input bottle. Sausage and Pie are each **2×**. Do not restore an old 2× wine number or an old 'research only' veto when implementing #450.

## Current executable authority

- **#445:** deferred broad content port only. No code-ready work and no B/C test obligation until reapproved.
- **#450:** owns the current narrow cuisine design, code work and QA; maintain blood provenance, native JAWR credit, vessel returns and abuse protections.
- **#334:** dietary/recreational drinks compatibility.
- The old cask feasibility/source analysis remains useful as historical technical context but is **not a live requirement**, and pinned upstream sources may have changed.
