# Astral Sorcery — next comprehensive live QA (2026-11-19)

Owner-approved policy: Overworld full sky, Eternal Starlight, Aurorian, Twilight Forest and BetterEnd/End constellation overlay; Nether, Bumblezone, Otherside, Dive Into The Abyss, Voidscape keep native sky without overlay. Preserve Overworld as primary natural resource/structure acquisition. No changes to worldgen, dimension type, native celestial time or general perk behavior in this source slice.

Source: pinned AstralSorcery-2.0.3.59.jar (SHA1 66f36655eae7cb404c9c16da6f33db31dff6ed9a); upstream 1.21.1 RenderingConfig, BaseConfiguration, AstralSkyRenderer, LevelSkyHandler. Rendering options are client-only, not a gameplay allowlist. Full render only in NORMAL sky type, overlay ignored in full-sky list, eclipses not shown by overlay. Actual starlight mechanics may differ due to fixed time, native sky type, celestial event cycle and sky exposure or modded renderer. The policy "yes/no" for infrastructure is an acceptance target, not implemented server dimension gating. Do not synthesize a whitelist or globally change skylight/fixed time; if tests fail report exact native seam to patch. Retain all existing worldgen placement settings and verify natural acquisition; only change if policy requires it, after source review.

## Inclusion
- Apply config/astralsorcery-client.toml from this scoped source; regenerate packwiz hash chain on final canonical integration checkout, reconciling concurrent pack metadata rather than copying these old lockfiles. Read effective config after fresh profile and update/restart; confirm exact resource IDs using live registry.
- Include Progression #347 cross-dimension Mahou bridge at least commit 8e47e22322a8228233d540099d651ade71ccee05 (or reviewed successor). Verify actual assembled JAR and optional mixin application, not just source tests. No separate MXP or Hunter JAR introduced by this work.
- Use disposable new/test worlds only and owner-authorized next build. No full smoke/packaging/gameplay authorization from this note. Source PASS is not gameplay PASS.

## Codex live checklist — all NOT RUN
| ID | Test | Acceptance |
|---|---|---|
| AS-D01 | Fresh and updated client config; capture actual installed JAR hashes and dimension registry; record dimension type fixed_time, natural, has_skylight, has_ceiling, effects and live time. | Exactly five approved IDs resolve; full only OW; overlay exactly four; existing custom sky remains; no TOML correction beyond unrelated native defaults. |
| AS-D02 | In Overworld, Eternal Starlight, Aurorian, Twilight Forest, BetterEnd visit open sky and covered locations at multiple times, log/take screenshots. | Native sky preserved in four overlay dimensions; recognizable constellations; no crash, eclipses not overlaid; More Darkness/native custom sky compatibility. |
| AS-D03 | In each approved realm observe starlight collection/altar craft, constellation paper/telescope, attunement altar, sustained light network and ritual outcome using equivalent structures; test fixed-time and relog behavior. | Meaningfully usable native infrastructure, document unavailable constellation/time, sky check or generation issue; do not treat visible overlay as working gameplay. |
| AS-D04 | Nether, Bumblezone, Otherside, Abyss, Voidscape: observe native sky, attempt equivalent collection/altar/attunement. | No added sky rendering; report any server magic still functional against owner intent, distinguish imported items/perks/gateways (do not globally disable personal AS benefits). |
| AS-D05 | Natural new-chunk AS structure/ore/loot discovery with actual registry placement checks in overworld and other dimensions. | No newly introduced alternate generation; record preexisting exceptions for review. |
| AS-D06 | Two players with active Discidia and/or Hunter; actual Mahou boundary normal & resistant damage, nonlethal and lethal. Cross-dimension remote loaded farm, travel between primary and fallback, logout/rejoin, different owners, native direct/projectile controls. | Native AS incoming amount/eligibility/modifiers unchanged; Hunter eligible death once; online owner only, no duplicate XP or melee/loot proc, no Torment Nexus credit. |
| AS-D07 | Capture XP/health before-after, live mixin warnings, ownership, dimension and config snapshots; repeat after restart. | Explicit PASS/FAIL/BLOCKED/NOT RUN by dimension/test; document exact blocker for fixed-time, custom render, server mechanics or config. |

No actual runtime test or acceptance performed by this PR.
