# Gilded — prerequisites for first actual game build

**Reviewed:** 2026-11-16. **Branch:** `design/piglin-gold-progression-exploratory-2026-11-15`. **Status:** **NOT READY FOR LIVE TEST**. This is a design/source readiness audit, not an implementation, merge, smoke test or player PASS.

**Policy:** The owner expects every feature declared "Ready for a live test" to be *attempted* in that test. Do not mark Gilded ready or silently defer the gold, Netherite, Piglin, food or initiation mechanics. Build or full server smoke only on explicit instruction; preserve current frozen/known-good QA artifacts.

## Critical implementation blockers

| ID | Work | Exact scope / acceptance | Current state |
| --- | --- | --- | --- |
| GILDED-01 | Trusted ritual enrollment | At successful actual consumption of golden apple / enchanted golden apple / golden carrot, check server player in Nether wearing head/chest/legs/feet golden armor, then `ProgressionApi.join(player, "tmm_progression:gilded")` exactly once. Reject bad conditions and ordinary player `/tmmprogress enroll` bypass. `enrollment: admin` is intentional compatibility flag, not the ritual. | Draft `gilded_initiation.json`; **no event adapter** |
| GILDED-02 | Food eligibility enforcement | For joined Gilded, deny unauthorized food *before hunger/saturation/side effects* apply; allow explicit Nether food types, golden apple/carrot/enchanted golden apple and **food with real gold in crafting ingredient graph** (including gold tags/food ingredients; avoid cycles). Do not deny nonfood consumables. Confirm placed-feast and modded serving paths. Recipe classifier must refresh on datapack reload. | Draft `gilded_food_policy.json`/tags; **no pre-consume handler or recipe classifier** |
| GILDED-03 | Dynamic gold diamond equivalence | While actually holding/wearing gold: correct diamond-tier harvest behavior, normal/higher gold mining speed, diamond-equivalent item combat and armor values/toughness per slot, no underlying item-component/enchantment changes. Revert instantly on unequip/drop, class removal, relog. | `tmm_progression:gold_diamond_equivalence` **missing** |
| GILDED-04 | Effective Fortune | Gold and Netherite mining tools receive **+3 additive real effective Fortune levels** at full skill unlock, with intermediate +1/+2 rank; respected by loot context **once** without post-hoc dupes or persisted item edits. Handle Silk Touch, overlevels/Apothic, mass mining, modded ores and protected blocks. | `tmm_progression:virtual_fortune` **missing** |
| GILDED-05 | Effective Unbreaking | Gold and Netherite damageable tools/armor receive virtual Unbreaking, additive to real enchantment, applied once per correct wear event across mining, attack and armor damage; no NBT changes. Provisional +3 final rank. | `tmm_progression:virtual_unbreaking` **missing** |
| GILDED-06 | Piglin + Brute friendship | No *initiation* of unprovoked targeting toward Gilded, including normal gold armor check, gold mining and guarded container opening/breaking; Piglin Brutes also initially peaceful. Preserve targeting/retaliation following attacks, other players' anger and existing memories. Verify 1.21.1 native AI call paths before hook design. | `tmm_progression:piglin_affinity` **missing** |
| GILDED-07 | Gold-ore XP server event | On **successful completed** player-originated break of gold ore, deepslate gold ore, Nether gold ore **without actual tool Silk Touch**, emit `tmm_progression:gilded_gold_ore_broken_without_silk` once per block. 10 XP per event prototype, independent of Fortune count; no canceled/bypassed break credits. Check purchased/placed ore supported. | `external` activity configured; **no emitter** |
| GILDED-08 | Repair target semantics | Reuse real `gear_durability` rank handler for gold and Netherite at identical rates, but **`slot: all` includes offhand**, conflicting with mainhand-active/armor-only design. Tighten handler/selector or add explicit two-slot mode; prevent unintended inventory/offhand repair and double tick. | Handler exists, **targeting needs correction** |

**Strict parser:** `PathJson` rejects all five unknown Gilded custom effect types. Register real handlers in core before moving the JSON into active resources. A no-op registration is not acceptable.

## Required pack/data integration

- [ ] Copy the reviewed inactive `docs/explorations/gilded/data/tmm_progression/...` JSON files into `compat/progression/src/main/resources/data/tmm_progression/...` **after handlers work** and ensure resource loader discovers `tmm_progression:gilded`. Move/loader-wire ritual and gold-food policy contracts; their standalone JSON schemas are not recognized by `PathJson`.
- [ ] Audit all declared `mynethersdelight:` optional food IDs against **actual 1.21.1 registered item and recipe IDs**, plus food properties, not mod display names. Verify Hoglin drops, and whether native porkchops remain Nether-obtainable.
- [ ] Pin **My Nether's Delight** 1.10.4.1 (1.21.1 NeoForge) and its official file hash in packwiz + `manifest/mods.json`; Farmer's Delight is already pinned. Validate dependencies and combined mod ID versions; currently My Nether's Delight is **not** in this branch's `manifest/mods.json`.
- [ ] Recipe-aware gold food handling must be tested against vanilla/modern uncraftable enchanted golden apples (explicit allowed tag), tags that substitute for actual gold, direct gold ingredients and nested ingredients (e.g., golden carrot). Some plated/served recipes may be non-crafting serializers; decide any explicit eligible additions after recipe audit.
- [ ] Add Gilded localization, useful player status/XP feedback and instructions for ritual/diet. A full graphical talent tree is desirable but **not essential for first functional QA** because the current draft's skills auto-unlock by rank and `/tmmprogress status` is available; don't call UX complete.
- [ ] Bring branch feature files forward onto *current* `test/full-integration` head with path-scoped reconciliation, not a blind branch replacement. Register source/commit, first-party JAR and explicit Gilded acceptance items in `manifest/integration-sources.json` and machine-readable `manifest/feature-checklist.json` when **authorizing inclusion**. Do not change old frozen candidate ZIP.

## Source QA before player test (no automatic smoke)

1. Compile real `compat/progression` Java 21 / NeoForge 1.21.1 and run its unit tests with Gilded parser, rank and handler-specific tests; sample paths Monk/Hunter must still parse and their tests pass.
2. Verify exact loaded tag IDs, recipe classifier on reload, native mod dependency hashes and no missing or unknown custom effects in registry.
3. Use *targeted* controlled test harness for registration, storage/restore and key eligibility logic. Do not mark in-world success from compile or server boot.
4. On explicit instruction, construct first-party JAR and frozen unstable Prism artifact with exact manifest SHA and mod list. After build, **all eight Gilded work packages must be in the same build**. If not, call the build partial, enumerate exclusions and do not claim feature ready.

## First live test matrix (disposable new world + two players)

- [ ] Ritual positive: three golden food types, Nether, four gold pieces, complete eat => enroll once. Negative: Overworld/End, wrong/missing armor, changing armor during eating, canceled eating, command bypass, creative/spectator.
- [ ] Food positive: mushroom stew, raw/cooked pork, other confirmed Nether foods, golden apple/carrot, gold crafting recipe (direct and nested), optional My Nether's Delight foods. Negative: non-gold Overworld food, fake "gold" name, non-food potion exceptions. Verify hunger/saturation and bowl/leftovers.
- [ ] Gold diamond stats: armor/toughness per piece, sword/axe damage, pick mining diamond-requiring blocks and drops, gold's fast speed, non-Gilded teammate with identical equipment.
- [ ] Fortune effective levels 0/1/2/3, physical Fortune III => effective Fortune VI, Silk Touch precedence, ores and modded ores, mass break/claims, no duplicate resource/XP roll.
- [ ] Virtual Unbreaking and matching fast repair on gold and Netherite armor/tools; mainhand vs offhand vs inventory, breaks, swaps, death, relog, loss of enrollment.
- [ ] Ordinary Piglins **and Brutes** no spontaneous aggression; gold/containers no new hostility; direct attack retaliation; nearby non-Gilded retains independent hostility; no memory clearing.
- [ ] Ore XP once per successful gold ore block with non-Silk tool; no XP with Silk Touch or from drop quantity, canceled/protected/machine breaks. Milestones and level unlocks persist across death/relog.
- [ ] Regression: Monk/Hunter progression, pack food diets (especially dragon), JAWR forms, Apothic enchanting, claim protections, Quest Shop ores/coins, dedicated client/server sync.

## Deferred until after first functional Gilded test

- Full bespoke Gilded skill GUI, quests and polished onboarding (short readable temporary text/status sufficient during QA).
- Final Unbreaking and repair-balance tuning beyond prototype values.
- More modded golden items / food categories after tested registered IDs.
- Stable release/candidate promotion, old-world migrations and broad performance soaks.

**Current conclusion:** The most time-consuming/high-risk work is virtual Fortune at the correct loot calculation stage and Piglin/Brute AI initiation interception. The ritual/diet handler and temporary gold stats are medium complexity. Repair targeting, tag import, mod pinning and test registration are smaller tasks, but all must actually be integrated before full Gilded feature QA.
