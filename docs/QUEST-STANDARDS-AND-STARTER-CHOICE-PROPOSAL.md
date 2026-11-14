# Too Much Magic — Quest Standards & First-Path Choice (PROPOSAL)

**Status: proposed for owner review, NOT approved for gameplay.** Prepared from the native 2026-11-14 B13 quest/vampire/QoL build. Do not interpret this document as authorization to change the existing book or overwrite the earlier approved [quest contract](QUEST-DESIGN-RULES.md).

## Current state and evidence

The implemented 5-category, 45-chapter quest book contains 179 quest objects: 137 authored quests plus 42 temporary per-chapter overview/checkmark placeholders. The existing `manifest/quest_content.json` contains 137 authored entries. Its descriptions have a median of about 29 words (mean 28.1; range 6–81), and only four exceed 50 words. Fifty authored objectives are checkmarks; 86 are item criteria and one is a first-log advancement. Of 137, **125** grant a fixed physical item and **12** are consumed-commodity coin sales. The 125 physical rewards cover only **48 different item IDs**: redstone appears 11 times, paper/glass/experience bottles 8 each, and golden carrots 7. The 42 overview placeholders separately grant paper + 2 XP each.

This exposes an incentive problem: forcing `XP + item` on an information page creates little stacks of material rather than useful discovery. The existing validator correctly blocks *a single payout* from fully satisfying another quest's item criterion; it does not warn about repeated incidental payout types across chapters.

The player has confirmed working Eternal Steak and sale-coin claims. The current build also corrects the 13 literal-ampersand problems in FTB Quests localizations. Do not change that working code while evaluating standards.

## Proposed writing standard

**Quest vs reference separation:**
- **Reference/index/tip**: optional and available without artificial prerequisites. An index can be rewardless or minimal XP-only; do not require a physical item. A purely informational note need not be a claimable task if it is only a link or reference.
- **Objective/milestone**: explicit measurable action plus useful reward; present the *purpose*, *how to verify*, and *next useful step*. Keep progress and narrative flexible among magic schools.
- **Safety/complex machinery**: actionable preconditions, dimensions, dangers, and fallback/mitigation. Do not falsely imply a quest lock enforces a game mechanic; recipes/world controls provide actual locks.
- **Choice/one-time initiation**: explicitly describe the alternatives and permanence of the **giveaway**, not permanence of the player's specialization.

**Text budgets (editorial targets, not strict hard limits):**
- Index/reference note: **20–55 words**, ideally 1–3 short lines/paragraphs.
- Ordinary objective: **35–75 words**; explain why, how, and what the result enables.
- Multi-step machinery, ritual, dangerous boss/gate: **70–140 words** over short paragraphs or a concise checklist.
- Title: short and specific; favor a verb and a named system. Do not stuff complete tutorials into title/subtitle. Put extensive documentation in a linked guide or dedicated quest.

Every description should pass a truth/consistency audit against the **pinned** mod versions. Never explain unsupported features as certain. Avoid unearned authority and spoilers. No literal unescaped `&` in FTB localization; the generator escapes the FTB formatter then serializes SNBT (display `\&`, encoded `\\&`). Avoid unsourced tooltips and invalid placeholders.

## Proposed reward principles

1. **Purpose before payout.** Meaningful active milestones get XP plus a useful, contextual, fixed item or a deliberate choice. Read-only content may use low XP or no tangible item, subject to owner approval as a change to the old contract.
2. **No reward-engine multiplicative bugs.** Track each quest ID, reward ID, per-player/per-team ownership, once-only vs repeatable behavior, saved state and reset semantics. Validate in multiplayer and after restart, including Claim All.
3. **No automatic quest completion by reward.** Retain the existing cross-quest item ID + count validator. Add scrutiny for choices, mixed stacks and starting-kit payouts. User/shop-initiated purchases are a separate permitted design decision.
4. **Variety and chapter coherence.** Flag an identical fixed item awarded twice within the same chapter, or over ~4 times in the complete authored book, for design review. This is a warning not a hard rejection: infrastructure chapters may reasonably grant redstone, glass or materials repeatedly. Require a short exception rationale in the reward ledger.
5. **Reward size commensurate with effort.** Small consumables are fine as incidental utility; distinctive gear and capability unlocks should be reserved for appropriate milestones. Do not give restricted/boss-gated materials, incompatible class states, or late-game crafting bypasses.
6. **No currency flooding.** Preserve the 12 intentional one-team-coin commodity sales, consumed input and controlled repeat tiers. Use FTB Quests' built-in command reward to invoke `/hqs coins add {p} 1` until Quest Shop 1.4.0's outdated `void claim` API is repaired. Do not duplicate that payout through both an item and command.
7. **Separate player and team ownership.** Individual initial equipment, first-log pouch and starter-path choice must be claimable per-player **once**. Resource-sale coin payouts are team-shared **once per quest**, intentionally. Changes to teams must not manufacture duplicates.
8. **Do not force reward for reward's sake.** Remove the 42 paper placeholders as their chapters mature. Keep the overview navigational, not a fake harvest loop.

## Candidate: Choose Your First Path (native FTB Quests choice reward)

**Proposed placement:** shortly after `Make a Crafting Table` in **Getting Started**, not behind `Iron Pickaxe`. Choice opens an early activity; it is not a commitment or class lock. Every player can play every other magic school later.

**Proposed requirements:** one optional, manual checkmark quest (or an early lightweight verifiable requirement) with small ordinary XP and exactly one native `ftbquests:choice` reward. The quest is non-repeatable. The **choice** reward must be `team_reward: false`, not auto-claim, and excludes itself from Claim All. FTB Quests 2101.1.38 implements `SelectChoiceRewardScreen` backed by a RewardTable and `ClaimChoiceRewardMessage`; the handler verifies quest completion, valid choice index, and not-already-claimed state before marking the reward claimed. Use a reward table with explicit **one item grant** per option, not a random table. Verify item registry IDs in the **pinned** game JAR before authoring.

| Path | Proposed giveaway | Confidence / caveat |
| --- | --- | --- |
| Ars Nouveau spellcaster | Novice Spell Book (`ars_nouveau:novice_spell_book`) | Known item ID from Ars documentation/examples; inspect exact pinned registry |
| Iron's spellcaster | Copper Spell Book, displayed as Flimsy Journal (`irons_spellbooks:copper_spell_book`) | ID confirmed in Iron's 1.21 source; check if empty book by itself can cast and whether it needs a separate low-level scroll |
| Fisher/chef | Vanilla Fishing Rod (`minecraft:fishing_rod`) | Known item ID; optional Aquaculture equivalent only after registry and balance audit |
| Botania naturalist | Lexica Botania guidebook | Present in installed Botania Revived port; verify item ID and whether it is already auto-granted |
| Occultist | Dictionary of Spirits or equivalent first-step guide | Verify pinned Occultism version, item ID and whether it is already auto-granted |
| Explorer (optional sixth) | Basic compass or map, not Nature's Compass unless its crafting costs and quest cross-effects are reviewed | Safe fallback for players not ready to pick a school |

**Design constraint:** Rewards should save an initial nuisance and permit immediate exploration, not skip tier gates. If a spellbook without spells is mechanically useless, prefer a small equivalent kit or distinct verified entry rather than presenting it as usable magic. Native Choice selects *one RewardTable entry*; a multi-item kit would need a carefully verified nested reward / command implementation, not an unreviewed extra implicit grant.

**User-facing quest text draft (proposed only):**

> **Choose Your First Path**
>
> There isn't a required first school. Pick a starter item for whatever interests you: casting with Ars Nouveau or Iron's, learning Botania or Occultism, fishing, or exploring. This choice grants **one starter reward**, not an exclusive class. You can pursue every other path later.
>
> Complete this optional quest and select exactly one reward. Choose carefully: the starting gift is only awarded once per player.

## Proposed validation changes, BEFORE gameplay implementation

- Extend author schema to distinguish `reward.kind` = `item | coins | choice | xp_only | informational`, and explicit `claim_scope` = `player | team` and `repeat_policy`; keep existing IDs unchanged, assign new stable IDs to the choice quest and its options.
- Validator: warn outside editorial word budgets; reject raw ampersands, invalid quest IDs, unapproved restricted items, wrong reward scope or repeatability, and starter choice auto-claim/team claims.
- Validator: report common payout IDs and reward distribution by chapter as **warnings**, with documented exceptions. Cross-completion test remains a hard error.
- Registry QA: verify all choice item IDs exist in the pinned mods and compare recipes/actual utility, especially the book-without-spells case.
- In-game QA: two players sharing one FTB team select *different* paths and each receives one item; Claim All cannot pre-select the path; game restart/quit and quest GUI reopen do not duplicate the item; full inventory doesn't lose the chosen item; first-log pouch remains separate and once-per-player.
- No valuable-world testing until separately server-smoked, and no automatic conversion of existing quest-progress data without a save-backup migration plan.

## Recommended implementation order

1. Owner review of the **XP-only information page exception** and overall reward/editorial ranges.
2. Exact pinned JAR ID/recipe review of 4–6 starter options.
3. Build one *single* choice-reward quest in a separate experimental branch, without replacing the authored Getting Started chapters wholesale.
4. Validate reward table + per-player persistence in-game, then merge into the combined QA build after success.
5. Review repeated redstone, glass, paper, XP bottles and golden-carrot payouts chapter by chapter; change meaningfully, not mechanically to meet a uniqueness metric.

**Current QA baseline intentionally untouched.** The proposal is documentation only.
