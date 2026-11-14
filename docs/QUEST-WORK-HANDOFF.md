# Work handoff — scalable quest-book production
**For:** ChatGPT Work operating against \`heatwaveqc/Too-Much-Magic-Modpack\` via the connected GitHub app/browser.  
**Read first:** root [AGENTS.md](../AGENTS.md), [Quest Authoring Standard v2](QUEST-AUTHORING-STANDARDS-V2.md), [Critical Audit](QUEST-CRITICAL-AUDIT-2026-11-14.md), [45-chapter architecture](QUEST-ARCHITECTURE.md), [shop economy](QUEST-SHOP-ECONOMY.md), and [build channels](BUILD-CHANNELS-AND-TESTING.md).  
**Authority:** owner's latest instructions > root AGENTS.md hard restrictions > current pinned integration lock and runnable source > approved quest architecture > v2 proposal for new content > stale B6/B8/B11 descriptions. **Do not assume proposed v2 reward-policy changes are approved** until owner accepts them.

## Objective

Turn the functioning but sparse **180-entry** FTB Quest book (138 authored + 42 temporary overview entries) into a coherent, magic-first **guide** across the 45 approved chapters. The goal is **accurate, enjoyable and testable quests**, not a numerical fill target or a new hard progression system. Keep stable save/claim IDs and the good existing starter choice, shop, resources, first-log kit, vampirism and Monk work.

**Do not start by writing hundreds of JSON/SNBT quests.** The current authoring tool cannot safely compile new chapters at scale. Fix that first.

## Repository and branch entrypoints

- **Runtime assembly branch:** \`test/full-integration\`. Its checked-out manifest may show an old 137-author/179-layout baseline; the active \`manifest/integration-sources.json\` imports the quest stream. The latest integrated QA build's quest content is **not** simply this branch's checked-out manifest.
- **Current quest runtime source of truth:** \`test/quest-standards-starter-choice-2026-11-14\`, including \`manifest/quest_content.json\` (138 authored), \`manifest/quest_content_resolved.json\`, \`manifest/quest_chapters.json\`, \`scripts/rebuild_quest_ui.py\`, \`scripts/validate_quests.py\`, and the generated \`config/ftbquests/quests/...\` artifacts (180).
- **Design/contract branch:** \`design/quest-standards-starter-choice-2026-11-14\` contains the v2 docs. This branch is explicitly classified as non-runtime in integration manifest; keep it distinct from actual gameplay changes.
- **Build/test contract:** \`docs/BUILD-CHANNELS-AND-TESTING.md\`. The user requires the next QA release to combine **all registered active working streams** with immutable build/evidence, without promoting the experimental content to the stable branch.
- **Current feedback:** \`docs/USER-TEST-LEDGER-2026-11-14.md\` on \`test/full-integration\`. Owner confirmed quests, starter choice, shop button, rewards, travel and QoL good; Monk natural sunset now also passed after the player corrected direction; vampires still being tested. Do not reopen accepted fixes without a regression.

**Branch hygiene:** open a new focused **Work** feature branch based on an authoritative quest content snapshot, record its parent SHA and integration manifest pin, and present a PR. Notify the integration manifest of this new active working branch, or classify it as draft/non-runtime while unready, so full integration does not fail on an unclassified branch. Do not force-push or edit stable branch.

## Phase 0 — reproducible baseline and design audit (required)

1. Fetch the exact current quest stream, GitHub integration sources and pinned packwiz manifests; inspect \`AGENTS.md\`.
2. Record: 5 groups, 45 chapters, 180 generated entries (138 authored + 42 placeholders), 37 placeholder-only chapters; 47 authored checkmarks, 125 fixed-item reward records, 12 economy sales, one six-way choice; keep a machine-readable before/after inventory.
3. Confirm the actual starter choice is one per player; don't inadvertently migrate its claim ID or repeat grant. Keep the 12 copper/emerald/diamond sale claims and \`hqs coins add {p} 1\` compatibility bridge.
4. Check all quest text describing planned Pufferfish and unfinished gates. **Flag** rather than quietly changing game mechanics.
5. Produce an implementation plan with fields, generator rewrite strategy, ID migration strategy, and a short pilot success rubric.

**Checkpoint 0 artifact:** \`docs/QUEST-WORK-BASELINE.md\` plus a proposed schema and a list of constraints. Continue to compiler work if source integrity is established; stop on missing authority or dangerous save-ID ambiguity.

## Phase 1 — make authoring safe (required before bulk content)

Implement or replace quest compilation so **one canonical authored source** produces: quest SNBT blocks, localized \`en_us.snbt\`, the chapter/group mappings, coordinates/layout and packwiz indexes/hashes.

- **Don't key stable IDs by titles.** Existing IDs must be preserved even when renaming title/description. New IDs must be unique/deterministic.
- Represent kind: \`reference | milestone | challenge | choice | exchange\`. Track real \`task_type\`, \`task_target\`, completion semantics, \`manual_reason\`, optional/dependency list **by stable ID**, reward kind, individual/team scope, repeat settings, verification evidence and status.
- Represent references as non-claimable in a way **actually supported by pinned FTB Quests**, or clearly isolate that feature for player UI testing. No giant paper/XP farm from 42 scaffolds.
- Do not write unrelated logic into KubeJS just to trick a checkmark into auto-completing. Prefer verified FTB native types. For crafting goals, investigate \`ItemTask.only_from_crafting\`; for use/visit goals, investigate \`InteractionTask\`, \`ObservationTask\`, \`DimensionTask\`, \`StructureTask\`, \`StatTask\`, \`AdvancementTask\`, \`KillTask\` in the pinned 1.21.1 version.
- Update validation to **derive counts** rather than asserting 180/138 and to accept new quests in former placeholder-only chapters. Validate formatting \`&\` escapes, IDs, quest graph cycles, duplicate grants, exact registry IDs, manual exceptions, reward bans, and authored/source generated consistency.
- Two-run reproducibility check: compiling twice with no changed source must produce identical SNBT, locale and index/hash outputs.
- Write fixture tests for one new quest, rename preserving ID, adding a quest to a formerly empty chapter, nested choice reward, manual/checkmark exception and team currency.
- Back up or freeze the old generated book for diff analysis. Preserve existing progressed quest IDs, tasks, rewards, team balances and starter grants. No player save migration without explicit owner approval.

**Checkpoint 1:** a PR containing compiler, validators, roundtrip tests and unchanged (or clearly documented equivalent) generated book. This is the first major quality gate. A library or JSON parse test does not replace a complete NeoForge server startup.

## Phase 2 — editorial cleanup (approval-sensitive)

Do a content-only review of **Getting Started, Tips & Tricks, Food & Fishing and five Progression chapters**:

- Shorten or make optional the traditional wooden → stone → iron tutorial. Move \`Try a First Spell or Ritual\` out of the iron → barrel → vanilla lectern chain so the early magic path actually opens after the starter choice. Preserve old quest IDs if moved.
- Convert misleading action titles: \`Catch a Cod\` is just \`minecraft:cod\` possession; \`Chart Your Next Journey\` just checks filled-map possession; \`Build a Research Lectern\` only checks vanilla lectern item. Use true detection where available, or rename truthfully.
- Correct Pufferfish-only prose to distinguish installed Improvable Skills, the unfinished Tyz/MXP implementation and the eventual retirement of Pufferfish. Never assert live Tyz bonuses if still source-only.
- Remove or consolidate needless paper/checkmark tips and stale author warnings from player descriptions. **Propose** rewardless reference pages and XP-only simple milestones rather than silently changing existing rewards/claim states.
- Evaluate all duplicate filler rewards; report exceptions and before/after player impact. Do not change the approved twelve economy contracts or ProjectE gates.
- Food & Fishing needs actionable guidance on the modded kitchens and farming/specialty trees rather than only vanilla input acquisition. The old 22 quests average only ~16 words.

**Checkpoint 2:** show the user a concise before/after diff **for approvals concerning existing rewards, task semantics or dependencies**. Source-only copy improvements can proceed as a candidate, but production changes to previously completed quest/reward IDs need signoff.

## Phase 3 — pilot two contrasting magic chapters

Create **Ars Nouveau** and **Occultism** pilot chapters, because their progression mechanics are materially different and both are already present in the locked roster. Do not invent a quota; each should cover actual working first equipment, a first useful spell/ritual, resource loop, an optional branch, a practical medium-term goal and a real distinctive feature. Avoid filler item-fetch chain and avoid hard dependence on the other school.

For every quest:
- Exact pinned mod/version, recipe/item/entity IDs or relevant event ID; use live registry/JEI/commands when available.
- A short useful description and a **task that proves what its title promises**.
- Actual prerequisite and any crosslinks; do not impose a blanket "complete vanilla iron" requirement.
- Purposeful reward and claim scope, never XP bottles or forbidden/boss-gated materials.
- Evidence link/commit and an explicit test stage: draft, source-verified, server-smoked, client-verified, multiplayer-verified.
- A note for any manual checkmark that couldn't be replaced by a genuine event.

**Checkpoint 3:** present both complete pilot quest trees, source evidence and live QA findings. Stop mass production if the compiler is still brittle or the pilot scoring falls below **10/12** per quest using the v2 rubric.

## Phase 4 — batch production of the remaining chapters

Once the pilot passes review, populate the remaining magic/specialist chapters in logical batches (e.g. 3–5 chapters per commit/PR segment). Suggested authoring order is **easy independent early schools first**, then complex rituals/transformation systems, exploration and bosses, and finally endgame. No compulsory count target. If a chapter merits five meaningful quests, author five; if it needs 20, justify 20.

For every batch, emit:
- Chapter coverage matrix: existing authored, newly authored, removed scaffold, true gates, evidence status and open problems.
- Quest data and generated SNBT/locale/index diff plus a list of preserved/migrated IDs.
- Reward-quality/duplicate report, manual checkmark exception list, text and task semantic lint report.
- Verified source evidence including exactly pinned versions (not copied ATM10 prose).
- Clean integrated server smoke with all active workstreams, plus a **client test plan** that names which actions require real user input.

Perform a real gameplay acceptance pass before declaring any chapter **client-verified**. Do **not** mark source-complete or dedicated-server-smoked as "fully tested". Record unknown IDs/events/config behavior as blockers; do not guess, and continue with independent verified content.

## Phase 5 — safe full integration, not automatic stable promotion

- Register only tested runtime quest compiler/content commits as the selected active quest stream in \`manifest/integration-sources.json\`. Preserve the current good Monk/vampire/materials/home/shop work; don't substitute a partial quest-only QA pack for the all-stream build.
- Use the latest all-active-streams pipeline to assemble, validate, boot a disposable dedicated NeoForge world and freeze a commit SHA.
- Publish a Prism QA instance or exact GitHub Actions artifact link when all automated checks pass, clearly labeled **experimental**. Include a client acceptance matrix and known issues.
- Do not promote to stable or migrate saved quest progress without an explicit owner acceptance step. Re-run first-log, starter kit, shop, choices and currency claims as regressions.

## Owner decisions / explicit stop conditions

**May proceed autonomously:** source research, content outlines, source-to-SNBT compiler, static validation, draft pilot chapters, reviewable commits, documentation and dedicated-server smoke.

**Stop and request a review before:** changing prior player-visible grants to no-reward or XP-only; changing shop prices/sale rates; changing existing quest/reward IDs; activating new late-game mechanics/recipe/loot gates; replacing established stable source; changing which mods are installed; or shipping a survival-ready pack.

**Must not do:** invent recipe IDs or make unsupported runtime claims, copy another modpack's quest text, reward XP bottles, enable forbidden items or mods, put a hand-wave checkmark on every mod, hand players boss rewards, or announce a successful client test from a server log.

## Definition of done

- **System done:** author can add a quest to any chapter and regenerate/validate without manual edits to duplicate SNBT/locale/counts, while preserving existing claims.
- **Chapter done:** clear starting path, actual mechanics, meaningful choices, accurate verification, purposeful rewards, evidence metadata and client acceptance; no placeholder checkmark left masquerading as a completed guide.
- **Book done:** all approved 45 chapters are either substantively populated or explicitly **reference-only by design**, all hard gates reflect actual code, and Work's evidence/QA report makes that status honest.
- **Not done merely because:** 180 becomes 500, FTB loads on a headless server, GitHub Actions finishes green, or any chapter contains a long list of "bring item" quests.

---

### Copy/paste launch instruction for ChatGPT Work

> Work in \`heatwaveqc/Too-Much-Magic-Modpack\`. First read root AGENTS.md and the versioned quest documents \`docs/QUEST-CRITICAL-AUDIT-2026-11-14.md\`, \`docs/QUEST-AUTHORING-STANDARDS-V2.md\`, and \`docs/QUEST-WORK-HANDOFF.md\` on branch \`design/quest-standards-starter-choice-2026-11-14\`. Treat \`test/quest-standards-starter-choice-2026-11-14\` as the current authored quest stream and \`test/full-integration\` as the assembly source (read integration-sources.json). Improve the quest pipeline so it supports safe new chapters with stable IDs, dynamic counts, single-source generation, lint and save compatibility. Then execute the editorial review and draft two full pilot magic chapters (Ars Nouveau and Occultism), sourcing all item IDs/mechanics from the pinned pack. Keep accepted starter choice, shop, coin contracts, kits and transformation work intact. Build and smoke-test in small committed batches, report discrepancies and decisions requiring my approval, and do not promote to stable or replace existing rewards without review. Once the pilot is reviewed, expand through the remaining 45-chapter plan using the v2 standard. Keep status truthful: static/source verified is not client-tested.
