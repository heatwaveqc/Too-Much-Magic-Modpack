# Too Much Magic — critical quest-book audit
**Date:** 2026-11-14  
**Status:** source-based review and recommendations, **not** a gameplay change or owner approval of revised rewards.  
**Canonical analyzed content:** \`test/quest-standards-starter-choice-2026-11-14\`, quest source at \`b0fbff2899f32dc42320a6112cf2ebe840b0de04\`. Integrated release source list: \`test/full-integration\`; consult its \`manifest/integration-sources.json\` to see which quest stream is actually included. FTB Quests 1.21.1 native task/choice classes checked upstream.

## What is working and should not be discarded

- The approved **five categories and 45 chapter inventory** are a reasonable way to navigate a large, non-linear magic pack. Magic systems have recognizable ownership; food and QoL are consolidated instead of getting dozens of thin chapters.
- The native six-way **Choose Your First Path** reward works in player testing; it is a non-exclusive gift, not a character-class lock.
- User-reported gameplay passes: quest book, reward claiming, coin shop sidebar, travel commands, and general QoL. The Monk natural-sunset initiation **passed on retry** after correcting the viewing direction (earlier failure was a test setup issue). Vampirism remains in progress.
- Commodity sale coin workaround and one-time per-player first-log pouch are intentionally different reward lifecycles. Preserve the working bridge and stable IDs.
- The original source contains useful niche insights (Scriptor casting lectern, EvilCraft pickaxe tradeoff, reactor safety). Preserve accurate researched facts while rewriting their presentation.

## Measured content baseline

These counts come from the **138 authored records** in \`manifest/quest_content.json\` on the B14 quest stream, not the older 137-record integration checkout:

| Metric | Observed |
| --- | ---: |
| Approved categories | 5 |
| Chapters | 45 |
| Chapters with authored content | 8 |
| Chapters containing only temporary overview scaffolds | **37** |
| Generated quest entries | **180** = 138 authored + **42** temporary chapter-overview quests |
| Authored manual-checkmark objectives | **47 of 138** (34.1%) |
| Authored item-possession objectives | 90 |
| Authored advancement objectives | 1 |
| Authored fixed-item rewards | 125 |
| Authored shared-coin sales | 12 |
| Native personal choice rewards | 1 |
| Distinct item IDs used by 125 fixed-item rewards | **48** |
| Authored descriptions with fewer than 35 words | **107 of 138** |
| Quests with no authored dependency | 70 of 138 |

A missing prerequisite is NOT automatically a flaw: reference tips and independent branches correctly have none. The issue is when sequences that **should** teach cause and effect are disjoint, while unrelated items become arbitrary mandatory predecessors.

### Authored chapter distribution

| Chapter | Quests | Manual checkmarks | Quality note |
| --- | ---: | ---: | --- |
| Getting Started | 19 | 3 | Technically navigable, but too vanilla-heavy; the *first spell* branch is artificially gated |
| Tips & Tricks | 20 | 20 | All read-and-check, even when a reference page would be better |
| Food & Fishing | 22 | 1 | Many six-to-twenty-word item-fetch quests; little instruction for deeper cooking |
| Basic Progression | 22 | 1 | Twelve sale contracts plus ten mostly independent vanilla item goals; weak cross-mod story |
| Advanced Progression | 10 | 3 | All ten are independent roots; very little actual advanced magical progression |
| Skills & Advancement | 7 | 7 | All manual, and some instructions still center the retired **Pufferfish plan** |
| Endgame Roadmap | 6 | 6 | Informational notes made into six XP-plus-item checkmarks |
| Exceptional Power: Reactors | 32 | 6 | Detailed machinery arc dwarfs the still-empty Magic chapters |

### Findings and precise examples

| Priority | Defect | Evidence / why it matters | Recommended correction |
| --- | --- | --- | --- |
| **P0** | **Authoring compiler cannot scale** | \`scripts/rebuild_quest_ui.py\` rewrites **existing** SNBT quest blocks and hardcodes positions for the eight populated chapters and totals of 180/138. \`scripts/validate_quests.py\` also hardcodes counts and expects **one** overview in unpopulated chapters. Adding a new magic quest only to the author manifest is insufficient. | First make a **declarative source-to-SNBT quest compiler** and a dynamic validator. Preserve all existing stable IDs and progress compatibility. This is a prerequisite to assigning 37 chapters to Work. |
| **P0** | **Player-facing text contains plans that are obsolete or not real features** | \`Tips & Tricks / Understand Character Growth\` and \`Skills & Advancement\` describe Pufferfish as the intended magic framework despite the Tyz/MXP decision. Endgame notes narrate proposed gates as if they were usable. | Label work-in-progress for **authors**; describe only installed, functioning player paths in live quests. Link intended future gates only when their real code exists. |
| **P1** | **First spell is arbitrarily gated** | \`Try a First Spell or Ritual\` depends on \`Build a Research Lectern\` → \`Prepare a Field Kit\` → \`Iron Pickaxe\` even though the quest claims any introductory magic school qualifies. Starter choice, which correctly branches after the crafting table, does not fix this dependency. | Make first magic **directly available after onboarding/starter choice**. Any school's unique real prerequisites belong in that school's line, not behind a vanilla lectern/barrel/iron chain. |
| **P1** | **Over-emphasis on standard Minecraft progression** | Wooden/stone/iron/diamond pickaxes, shielding, farming vanilla inputs, brewing, enchanting and lodestone fill four early chapters. Helpful support topics are overrepresented compared with zero authored spells/rituals in 25 Magic chapters. | Shrink main onboarding to essentials and optional survival sidequests; spend authoring effort on each magic system's **first real working capability**. |
| **P1** | **Manual reading disguised as progression** | All 20 Tips, all 7 Skills and all 6 Endgame Roadmap entries use checkmarks. There are 42 additional overview checkmarks paying paper and XP, including ones in already-populated chapters. | Use **non-claimable reference/navigation nodes** where possible; if a true goal cannot be detected, document why, preferably with a specific alternative event hook. Remove temporary paper payouts instead of layering rewards. |
| **P1** | **Objective wording misstates the detection** | \`Catch a Cod\` checks possession of \`minecraft:cod\` rather than a fishing event. \`Chart Your Next Journey\` checks possession of a filled map rather than map creation/use. \`Build a Research Lectern\` checks possession of a vanilla lectern; it does not verify construction or Scriptor functionality. | Rename possession quests to **Acquire/Bring**; use FTB's crafting-only, interaction, observation, dimension, structure, advancement, stat and kill tasks where appropriate and tested. Don't pretend a custom event was detected. |
| **P1** | **Rewards are often mechanically valid but meaningless** | Two bowls for a crafting table, two potatoes for an iron pickaxe, paper for reading JEI, cookies for a settings tip. Redstone appears as a fixed reward **11 times**; paper, glass, and golden carrots **8 times each**. | **Owner decision 2026-11-14:** retain enjoyable tangible quest rewards (useful cheap tools, weird food, related derivative items); a shared coin is the fallback. One intentional bowl for Farmer's Delight Cooking Pot. See [reward palette](QUEST-REWARD-PALETTE-V1.md). |
| **P1** | **Too little explanatory value** | The 22 Food & Fishing authored entries average about **16 words** and largely say “craft X; explore more recipes.” Most descriptions lack concrete next actions, how the mod works, or a caution about alternate requirements. | Set a **purpose/mechanic/verification/next-step** standard. Elaborate only where the mechanic is unfamiliar; do not pad simple achievements. |
| **P1** | **Disproportionate content allocation** | Exceptional Power: Reactors has 32 authored quests. Every Magic/Exploration/Endgame specialist chapter still has only a temporary overview. The result feels like a power-engineering pack with empty magic categories. | Prioritize **magic-first pilot chapters**, preserve good reactor research but redistribute authoring time. |
| **P1** | **Progression and navigation overlap** | Getting Started teaches vanilla infrastructure, Basic Progression repeats ordinary resources/equipment, Advanced Progression is ten unrelated roots, and Endgame Roadmap repeats warnings from future dedicated chapters. | Establish **one owner per mechanic**. Cross-links and concise guide pages explain how to reach other chapters without duplicating objectives or payouts. |
| **P1** | **Design/source docs contradict the runtime** | \`docs/QUEST-DESIGN-RULES.md\` still says every quest **must** have XP + fixed item and still mentions 42 paper placeholders as expected; the B14 user instruction bans XP-bottle rewards and limits manual checkmarks. The base integration checkout also contains 137 old authored records; the B14 stream compiles 138. | Publish one versioned **authoring contract** with an authority hierarchy. Update legacy docs with superseded status after approval. CI should build from a pinned integration lock. |
| **P2** | **Economy onboarding has a small cliff** | First copper sale pays **one** coin but the cheapest catalog purchase costs **two**. Not necessarily wrong economy, but it makes first receipt unsatisfying as a shop tutorial. | Consider a one-coin starter purchase, or explicitly teach two transactions. Do **not** silently change the approved 12 sale rates. |
| **P2** | **Mechanical statements are mixed with contributor caveats** | Live tooltips refer to "upstream source", "our pinned build needs an actual test", and "unimplemented compat", which are useful audit notes but immersion-breaking player directions. | Put **evidence/confidence** in author metadata. Player-facing text should explain verified mechanics and safety clearly, without promising untested functionality. |
| **P2** | **Reward/quest test coverage is uneven** | Static CI validates IDs, ampersands, counts and some payout behavior; it does not prove every mod item ID, crafting-only semantics, GUI behavior, quest descriptions or multiplayer claim scope. | For every authoring batch, record source evidence and run a **smoked** integrated build; conduct client and multiplayer spot-checks before a stable release. |

## Positive design principles to keep

1. **Magic-first, branching, optional exploration.** The player must never be forced through an unrelated school or vanilla tech chain before the actual path they chose.
2. **No false mechanical gates.** ProjectE/Project Expansion must remain behind real Chaos Guardian enforcement; quests alone cannot enforce recipes or permissions. Excluded endgame mods/materials stay excluded.
3. **A market to reduce bad luck, not skip bosses.** Preserve the 12 intentional team coin-sales and 29-item shop catalog pending economy QA.
4. **One native starter choice; many later paths.** Starter items may open a line but must not force permanent specialization.
5. **Only verifiable client features are written in present tense.** Keep the full project vision in developer notes until implemented.
6. **Do not optimize for raw quest count.** Each node must justify its existence by information, meaningful progress, an interesting challenge, or a conscious player choice.

## Owner decision after audit

**The author rejects my suggestion to make ordinary quest rewards disappear.** XP plus a small physical present should stay: helpful next-stage components, unusual edible food, useful cheap tools and occasional coins. No generic paper/cookie/bowl/compass filler; the Farmer's Delight Cooking Pot quest gets a deliberately funny single bowl. The user approved ordinary vanilla ore blocks as Quest Shop purchases. Writers can **research/draft mod lines in parallel now**, independently of the declarative compiler; the compiler is required only for reliable bulk **runtime integration**.

## Acceptance implications

A source file can be structurally valid and **still be a bad quest**. Static lint is necessary, not sufficient. Work should use \`QUEST-AUTHORING-STANDARDS-V2.md\` and \`QUEST-WORK-HANDOFF.md\`, pilot two contrasting magic chapters, and fix the authoring pipeline before mass populating 37 placeholder-only chapters.

The audit intentionally does **not** rewrite quest IDs, claim data, shop balances, skill-system implementations, or authored player rewards. The existing working QA pack remains untouched.
