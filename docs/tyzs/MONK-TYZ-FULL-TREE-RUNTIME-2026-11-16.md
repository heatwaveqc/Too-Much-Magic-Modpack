# Monk's full Tyz tree — runtime implementation for next integration

**2026-11-16 · Draft [PR #229](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/229) · Minecraft 1.21.1 / NeoForge / Tyz 6.4.2**

**Scope:** the complete owner-approved **17 first-pass Monk-specific Tyz skills + Empty-Hand Paragon**. The four prior **Core Magic** skills remain installed and unchanged. All Tyz skills use the **one existing Tyz MXP/skill-point wallet**; native Monk cultivation rank, owned classic abilities and native technique points are distinct. Standalone Monk continues to work with Tyz absent. This **does not** install the separate 40 proposed rarity-specific Apotheosis affix conversions: their underlying rarity/effect/stacking APIs need registry verification first. They are **not safe no-op purchases**.

## Live skill data: 18 Tyz IMMUTABLE entries

Tyz 6.4.2 custom JSON `type:CUSTOM` only supports **unconditional player attribute modifications**, which cannot express Monk-only, barehand-only, native-milestone-gated effects. We therefore use its supported `IMMUTABLE` entries for persistent purchased rank/value sets and handle **all actual effects** in the new optional `TyzMonkBridge` in `tmm_progression`. Every node has `active:true`, a positive cost, progression stage and nonempty values, no unconditional modifiers, and a named handler. The server intercepts Tyz `SkillActionEvent.PurchasePre` and `RefundPre`, even for bulk purchase/refund, and queries server-owned purchased levels with the Tyz public API. Admin/direct-granted ranks remain **dormant** until genuinely eligible rather than inventing native Monk initiation.

### Four lanes, three stages each

| Lane | Entry — 25 ranks | Advanced — 10 ranks | Mastery — 5 ranks |
| --- | --- | --- | --- |
| Martial Body | **Empty-Hand Force:** +0.35 real unarmed damage per rank, at most +8.75 | **Flowing Counter:** bounded server-side retaliation to direct hostile melee, 40-tick cooldown | **Empty-Hand Precision:** up to 10% chance for a 1.5× real fist strike |
| Conditioning | **Natural Poise:** up to +10 additional armor when otherwise unarmored | **Hunger Discipline:** reduces remaining native exhaustion by up to 40% (still consumes food) | **Fire & Blast Tempering:** additional fire/explosion mitigation up to 12.5%, never immunity |
| Footwork | **Body Speed:** up to +0.075 absolute movement-speed attribute, unarmored | **Balanced Landing:** up to 30% additional fall mitigation and +0.25 knockback resistance | **Glide Control:** extra native momentum glide handling, only after legitimate Dragon milestone |
| Hands as Tools | **Barehand Efficiency:** up to 2.5× final hand mining speed on top of native | **Fortunate Hands:** at most +3 above native Fortune I, never applies to Silk mode | **Looting Palm:** up to 20% chance for one extra ordinary mob drop, direct empty-fist kills only, no boss loot |

Each advanced node requires one rank of its own entry node; each mastery node requires one rank of its advanced node. **Server-side stage gates** add Monk native rank 10 for advanced, rank 17 for mastery, and rank 20 + Dragon credit for Glide Control (the source design's old rank-15 glide license is superseded by our actual native Momentum Glide rank-20 requirement). All are skill-point purchases and cannot substitute for classic Monk's **free native abilities**.

### Additional mastery and cultivation

| Skill | Ranks / price | Implemented outcome |
| --- | --- | --- |
| **Monastic Mastery Learning** | 25 × 1 SP | Up to +50% **native Monk activity XP only**, never Tyz MXP or Tyz SP |
| **Focused Advancement** | 5 × 1 SP, nonrefundable | One **permanent additional native Monk technique point per purchased rank**, bounded five total per character. Each is tracked by a permanent unique marker; refund is blocked so XP/SP laundering is not possible. Available only at sufficiently advanced native cultivation |
| **Inner Fortune** | 50 × 1 SP | Up to +10 true Minecraft Luck attribute, gearless; doesn't pretend to be Fortune/Looting |
| **Inner Reservoir** | 25 × 1 SP | Up to +125 personal Ars/Iron's mana and +1,250 Psi reserve where those attributes actually exist; equipment-conditioned |
| **Worldly Cultivation** | 25 × 1 SP | At most 25% chance for one extra **ordinary mature-crop** item on an actually completed barehand harvest; no automation or arbitrary loot rerolls |
| **Empty-Hand Paragon** | 1 × 5 SP | +5 empty-fist attack damage and +3 armor only when native rank20 + Dragon achievement + fully barehanded, unarmored and **Curios empty** |

**Capstone eligibility:** must purchase one real Mastery lane and **one Advanced purchase in a *different* lane**; only server-side gate can enforce this OR/different-lane condition. Core Tyz level requirements/prerequisites alone cannot. All active powers must pass live gear checks every tick/event, while invalid investments remain purchased but dormant rather than refunded on temporary disqualification/cure.

**Balance and acceptance caution:** These are functional **pilot numbers**, not final 85% gear-normalized cross-class balance or a demonstrated zero-gear solo Chaos Guardian win. Full 85%-of-legal-geared-specialist Luck/mana/harvest calibration requires the corresponding complete legal gear and Curios ceiling audit in the separate design branch. Never present these provisional maxima as validated 85% parity. No source skill adds innate Spirit Projectile, free Creative Flight, no-CD teleport, unconditional damage immunity, or bonus Tyz MXP.

## Files and startup

- `compat/magic/tyzs-test-overlay/config/tyzs_skills/custom/skills/tmm_monk_*.json`: 18 runtime Tyz skills (6.4.2 `IMMUTABLE` value sets), plus unchanged `tmm_magic_*.json` Core Magic skills.
- `compat/magic/tyzs-test-overlay/config/tyzs_skills/custom/data/categories.json`: preserve **four vanilla Tyz categories** and `tmm_magic_core`, add `tmm_monk`.
- `compat/magic/stage_preview.py --check`: static schema + account-of-22 skill validator; `--install --pack-root /path/to/disposable/packwiz/source` performs staging when authorized.
- `scripts/activate_tyzs.py` / `scripts/verify_tyz_migration.py`: now expect the combined 4 Core + 18 Monk overlay. **Do not invoke the old 4-only copy from PR #33**, which would overwrite/remove the Monk tab.
- `compat/progression/src/main/java/io/github/heatwaveqc/tmm/progression/runtime/TyzMonkBridge.java`: native Monk hooks and conditional effects, no Tyz hard mod dependency. Its future changes must be rebuilt into the **first-party JAR** for the overlay to have real effects.
- `manifest/integration-sources.json`: both the Tyz overlay and standalone Monk JAR stream select this source branch for the next integration build.

## Actual test boundaries

**Automatically check:** `python3 compat/magic/stage_preview.py --check`, `python3 -m unittest discover -s tests -p 'test_monk_tyz_tree.py'`, `python3 -m py_compile scripts/activate_tyzs.py scripts/verify_tyz_migration.py`, and `cd compat/progression && gradle --no-daemon build`. The quick CI workflow deliberately runs **no full pack smoke**, no Prism ZIP and no server or client test.

**Client tests needed in disposable world:**

1. Open existing Tyz UI (`/skills` or existing bound hotkey). Verify **Monk** tab appears alongside the four defaults and Core Skills; count 18 nodes. No duplicate XP/SP wallet or missing prerequisite labels.
2. Join Monk natively, grant a controlled amount of shared Tyz SP by operator-supported Tyz commands, and buy rank 1 Entry; verify SP decreases once, native Monk rank unchanged. Login/reload/refund and confirm no repeated SP costs or disconnected node attributes.
3. Attempt purchase while not a Monk, while wearing normal armor, with an invalid source license or before native rank10/17/20; server must reject, including Tyz bulk-buy. Direct Tyz admin-granted ranks must remain dormant outside Monk, not activate during invalid form.
4. Buy same-lane Entry → Advanced → Mastery; verify prerequisites enforce ordering. Try capstone with only a single lane; reject. Add one **different** advanced lane, reach Monk20 and credited Dragon, empty both hands/armor/Curios; accept at 5 SP.
5. Test actual fist damage, armor, movement, fire damage, fall mitigation, retaliation ICD, precision procs, and gliding while legitimate, then immediately equip an illegal sword/armor and ensure conditional effects stop. Remove gear and verify they restore, with no stale/duplicated attribute modifiers across death/relogin.
6. Test Fortune-mode Treasure Hand with three additional Tyz Fortune levels versus Silk Touch mode. No loss of source glass drop, no duplication of block entities/boss keys; tool tier and claim rule respected.
7. Test plain-mob fist Looting and mature-crop bonuses in a controlled sample; boss/quest drops must not duplicate.
8. Test Monastic Mastery Learning on actual legitimate native activity XP without changing Tyz MXP/SP. Test Focused Advancement once per rank (max five native points permanently), including after login/admin grant and attempted refund.
9. With empty Curios, test Capstone attributes. Put on any Curio while already purchased: Paragon must suspend immediately, and become active again when removed. With Curios mod absent, ordinary Monk skills still work; absence of Tyz entirely must not crash native Monk progression.
10. After Tyz `/skills reload`, check ID stability, purchase persistence, and ranks. Compare actual live benefits to calibrated gear-normalized reference—do not claim final 85% parity without that dataset.

**No claims of in-game PASS yet.** Source/staging/JUnit/Gradle success, if reached, only qualifies this for the next owner-authorized integrated live test. Existing user directive forbids auto full smoke, background Prism publication and automatic progress promotion.
