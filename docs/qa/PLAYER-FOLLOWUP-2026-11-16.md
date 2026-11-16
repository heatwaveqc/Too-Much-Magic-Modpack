# Player follow-up — 2026-11-16

Tested installed snapshot: `2aacf34893bc06f8048a7fabae644b3b91a1d1d4`. Gameplay performed by user; observations not independently reproduced by automation.

## Confirmed command/gameplay results

- **Additional Hunter PASS:** player used the read-only mutation progress query and confirmed partial progress survives world reloads. Non-Hunter Mutagenic Brew applies Wither as expected; the tested negative-control initiation attempt failed. Exact missing enrollment prerequisite was not specified, so do not infer every negative prerequisite combination passed. Permanent mutation unlock and ongoing hunger-cost tests remain unperformed.
- **Bundle Curios slot PASS:** dedicated slot exists and accepts the bag. This resolves prior uncertainty about slot availability; direct starter grant and absent quest reward remain separate failures/incomplete integration.
- **Dragon acquisition configuration FAIL:** player confirms login species choice is still enabled in this build. Ritual-only acquisition requirement remains unmet; no claim that changing the configuration was shipped.
- `/tmmprogress list` lists Monk and Hunter. Datapack listing displayed enabled packs.
- Natural Monk initiation passed again. Owner requests skipping repeated initiation tests unless relevant code/dependencies change.
- Hunter potion enrollment passed; specialty command exists and appears functional; mutations initially empty.
- With command-supplied Bio-Mutagen (bypassing broken crafting recipe), brewing/drinking grants mutation progress without temporary Speed. Crafting remains failed; permanent unlock/hunger cost untested.
- Bio-Mutagen not visible in JEI. Investigate separately from failed crafting-recipe visibility.
- Mutagenic brew appears as an empty glass bottle. Owner requests source potion color with green mutagen markings or similar; graphics work deferred.
- No progression UI key exists. Source confirms only ability keys; graphical UI NOT BUILT, not a failed keybind. Prior assistant K-key advice was incorrect.
- Owner requests skipping previously accepted JAWR baseline when this pack's relevant JAWR behavior has not changed; focus only on actual touched integration code.

## Starter, progression and quest observations

- Akashic Tome remains empty.
- Two Enigmatic trinkets still automatically granted on arrival.
- Other automatically granted books identified by player: **Feywild Lexicon, Astral Sorcery guide, Tattered Tome**. These are the observed book names; do not substitute earlier assumptions about Ars/Primal books.
- Empty first inventory slot works (player-confirmed).
- Bag still directly granted, and is not present as a quest reward. Dedicated bundle slot existence and acceptance subsequently passed; reward routing remains separate unfinished work.
- **Corrected TreeChop regression:** player clarifies that the **chopping progress bar disappears after the first break**, unlike previous builds. The complaint concerns TreeChop progress display persistence, not missing immediate Monk XP. Reproduce on a fresh tree, observe the bar before/after the first break and subsequent chops, and compare the prior build's overlay behavior.
- **Separate TreeChop/Monk XP failure confirmed by player:** no Monk XP is awarded even when the tree is fully chopped. This is independent of the disappearing progress bar. Exact new-build XP before/after values and tree/log counts were not supplied; do not reuse the earlier build's four-XP/four-tree measurement as this build's result. Investigate completed-felling event attribution and eligible training-block credit separately from overlay rendering.
- Several quest entries again have invalid formatting; user perceives questbook reversion. Screenshot evidence below confirms the issue; exact affected quest IDs remain uncaptured. Compare rendered content/localization against the previously accepted source before attributing the reversion's cause. Source titles contain literal ampersands in Tips & Tricks, Food & Fishing, Skills & Advancement, Forbidden & Arcanus, and Exploration & Combat, matching the visible error positions.
- Ars questline is not built out in game. Build manifest explicitly stages Ars pilot source/review only, deferring runtime import to avoid overwriting Alcohol and restoring obsolete quests. **Ars NOT BUILT**, not evidence that an accepted runtime Ars pilot regressed.
- Bag/quest integration fixes also appear in the manifest's deferred streams. Their absence confirms incomplete scope, not a proven failed implementation of a shipped fix. They remain owner requirements for next integration.

## Follow-up priorities

### Screenshot evidence supplied after initial observations

- `Screenshot 2026-11-16 080030.png`: first-person barehand view of a spruce tree. Overlay visibly says **Chopped Spruce Log**, **2/6 chops**, displays log icon/count **8**, and credits **HT's TreeChop**. This is a TreeChop/Jade block overlay, not visible Monk XP progress. Screenshot alone does not establish whether its chop count is incorrect or XP was awarded. Keep the user's tooltip complaint open without assigning it to the native progression UI; correlate with expected overlay behavior/chop events.
- Player clarification resolves the symptom: the missing element is the chopping **progress bar after the first break**, previously retained across breaks. The still-visible text/count in the screenshot does not invalidate that reported bar regression.
- `Screenshot 2026-11-16 080049.png`: FTB Quest Book chapter navigation visibly contains multiple red **Invalid formatting! You must escape whitespace after &** errors in Introduction, Progression, Magic and a group heading; one Introduction chapter renders **Unnamed**. This confirms a rendered formatting/localization failure, beyond the structural quest validator's coverage. Investigate literal ampersands in titles/localization and missing title mappings; preserve final display text with renderer-compatible escaping. The visible Ars Nouveau chapter label does not prove the deferred expanded Ars questline shipped.
- Screenshot files remain local; no image upload or raw filesystem paths committed.

Fix actual command/recipe blockers; integrate starter reward/bundle and quest registry/layout work deliberately; preserve Alcohol content when reconciling Ars pilot. Diagnose completed-tree XP credit independently from TreeChop progress-bar rendering. Screenshot examples are supplied and sufficient to start triage; no need to repeat stable baseline tests while these integration gaps remain.

## Remaining checklist audit

Rechecked B00–B10 and C01–C09 against installed-snapshot source. Earlier recommendation to stop the entire pass was too broad: several shipped integration cases remain practical.

- B04/B05: Hunter mutation-progress persistence, non-Hunter Wither penalty and the tested negative enrollment control subsequently passed. Killing for Hunter XP is shipped, but broken namespaced status limits immediate measurement; require observable evidence rather than assuming a kill PASS. Permanent unlocks/hunger costs remain deferred pending accelerated fixtures.
- C05: `tmm_transformations` still registers native probes, mob alliances, Archlich phylactery-first and Dracolich dragon-death-first event arbitration. These are changed cross-mod behaviors eligible for testing, distinct from unchanged native JAWR blood/hunger. Read-only `/tmmtransform status`, `archlich`, and `dracolich` commands exist. Actual charged-anchor fallback and no-double-spend tests need appropriate disposable setups. Dedicated skeletal visual assets remain deferred.
- Dragon ritual-only selection was checked and login choice remains enabled. New diet compatibility is not comprehensively accepted. Dedicated bundle slot acceptance passed despite starter grant/quest failure.
- C08 backup/restore and C09 CC/boss-route tests remain possible on separate disposable copies/setups, but are longer and not immediate short command checks. C06 worldgen is not established; deferred BetterEnd/YUNG are excluded. C07 multiplayer remains excluded by user scope.
- B01/B02 high-rank Monk fixtures blocked by parser/admin-XP; B06 MXP, B07 B20 traders, B09/B10 YUNG/BetterEnd and C02/C04 Thermalily/Gateways are not shipped. B08 teleport baseline and B03 initiation already accepted; do not repeat without relevant changes.

## Owner disposition: end this pass

Owner requests deferring the rest until test infrastructure is built and intended features are actually included. No further tests are requested for this build. Preserve confirmed passes and rerun them only when relevant code/dependencies change; do not mark deferred cases passed. No additional server smoke or build authorized. Next handoff should explicitly list shipped features and working commands before assigning player tests.