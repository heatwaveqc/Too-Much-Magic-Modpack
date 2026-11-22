# Quest walkthrough standard — mod chapters

**Owner decision: 2026-11-15.** This supplements [QUEST-DESIGN-RULES.md](QUEST-DESIGN-RULES.md) and [QUEST-ARCHITECTURE.md](QUEST-ARCHITECTURE.md). It governs all newly authored mod progression quests and is the review standard for earlier pilot chapters. It does **not** claim old chapters already comply or authorize editing them without a separate implementation task.

## Definition of done: teach the whole mod

A mod chapter must be a **rewarded, navigable how-to**, not a checklist of objects to acquire. A player starting with no outside knowledge should be able to complete the mod's normal progression, including major optional branches, by using **the questbook + JEI + the mod's in-game guide/reference**. No external wiki/video is necessary for the intended path.

Cover every substantive player-facing system and all meaningful progression tiers: **entry → fundamental resource/setup → unlocks and builds → operation → upgrades → specialized branches → automation/advanced systems → major encounters/endgame/completion**. For addons, teach their real new mechanics inside the parent chapter, or clearly cross-link the relevant other chapter. Not every incidental recipe requires its own quest; *no necessary step or unlock should be left unexplained*.

If the mod is informational/QoL rather than a progression system, write a concise **how-to and useful-tricks guide** with clear examples, not a fictitious tier ladder. If a mod has irreducible RNG or branching specializations, explain how to pursue/recognize the options and which are optional, without forcing all mutually exclusive choices.

## Mandatory guidebook onboarding

1. For **every mod with a player-accessible in-game guide** (book, menu, quest log, spell lexicon, manual, UI, Patchouli/Modonomicon entry, etc.), add an **early explicit quest** that teaches how to obtain/craft/open it or **provides it as a safe reward**. If the guide opens through a keybind or UI rather than a physical item, explain the precise access path. If access is gated, explain the earliest feasible path and place the quest accordingly.
2. The quest should explain **why the guide matters** and how the player will refer to it. A generic "read the manual" checkmark without acquisition/access instructions is insufficient.
3. Every quest relying on **multiblock construction, altar geometry, ritual patterns, celestial/biome positioning, machine layout, special crafting UI, or other hard-to-remember procedure** must **point explicitly to the relevant in-game guide section/page or JEI recipe/use category**. Include the minimum actionable build/setup instructions in quest text; don't send a player hunting through the whole guide.
4. When the in-game guide is incomplete or wrong in the pinned version, **the quest supplies the missing instructions or flags a hard blocker**; an external wiki link can be a citation in research, never the sole way a player can progress.
5. Use existing guidebook availability/starting rewards where possible; do not duplicate starter gifts, add forbidden items, or turn acquisition rewards into automatic unrelated quest completions.

## Each mod quest teaches a real action

A short, actionable quest description should answer:

| Element | Player-facing obligation |
| --- | --- |
| **Purpose** | Why the player should do this and what it unlocks |
| **Prerequisites** | Required previous stage, boss/biome/dimension, resources and any optional alternative route |
| **Do this** | Clear ordered instructions: what to craft/find, where to place it, how to activate/use it, what to feed/supply, what to watch |
| **Reference** | Precisely which JEI category/recipe/use page or in-game guide section explains a nontrivial procedure |
| **Expected result** | What visibly changes, produces items, unlocks a capability or advances progression |
| **Completion criterion** | What FTB actually detects, distinguished from the mechanic being taught |
| **Reward** | Positive XP and an appropriate, useful thematic fixed reward under the existing rules, with documented exceptions only |

**Example schematic (not a verified Ars recipe):** "Build a Source-producing workshop. Obtain the specified producer and Source Jar (see their JEI recipes); place them near qualifying activity; use the *Sourcelinks* section of the Worn Notebook for ranges and requirements. Trigger the producer, observe Source increasing in the Jar, then supply an Ars machine. **Quest task: acquire the producer** [this does *not* verify Source generation]. **Reward:** XP plus useful workshop supplies." Actual descriptions must use version-confirmed components and truthful detectable tasks.

Avoid vague text such as "master alchemy", "explore the mod", "make an altar" or "craft the item" without the procedure/why. Avoid long copied guidebook passages: give a workable summary and a precise in-game reference.

## Build the chapter from a verified progression graph

Before authoring quest entries, research and maintain a **progression coverage matrix** (in the mod profile and handoff) for:

- How players obtain **their first instructions/guidebook**; first materials; first operational setup; activation and failure modes.
- Exact tier names and unlock conditions, progression currency/XP/research, gear/spells, crafting requirements, resource loops and materials needed for the next stage.
- Every major workbench, machine, altar or multiblock; spatial layout, entity and environmental requirements, input costs, output and success signal.
- Each major side system (farming, storage, automation, summoning, factions, exploration, combat, bosses, dimensions), and why/when it is useful.
- Major optional branches, mutually exclusive choices, alternate acquisition routes, and transitions to addon content or another mod.
- A meaningful **terminal objective** for the mod, or a documented open-ended endpoint and advanced capability checklist.

Each matrix row should map to (a) one or more quests or a clearly named teaching section, (b) prerequisites and next step, (c) guidebook/JEI reference, (d) authentic detection method or honest surrogate, (e) a safe XP/item reward direction, and (f) source/test status. A required progression row with no quest/reference is a **coverage gap**, not "complete".

Show the **choice boundary** in the matrix: distinguish mandatory tiers/capability gates from player-selected spell, recipe, equipment or automation branches. Teach how the player can identify and complete a valid option using JEI and the in-game guide; do not require every optional recipe as a chapter milestone. Link a representative choice quest to the broader discoverable option set when useful.

The chapter must remain browseable, with sensible dependency arrows and crosslinks. Use *optional* quest branches for side content; never create arbitrary mechanical gates by quest dependency when the game doesn't enforce them. The player should be able to look ahead, choose strategies and return later.

## Required visual walkthrough layer (owner decision 2026-11-22)

Use [Quest Presentation and Visual Authoring Standard v3](QUEST-PRESENTATION-STANDARD-V3.md) to convert the Atlas-supported progression graph into a legible, original TMM quest map. **Progression/how-to correctness remains primary:** the art explains tiers, decisions, machines and side systems but never replaces actionable steps or creates an artificial gate. Use meaningful **objective-specific icons on every authored quest**, not a repeated default book; select a left-to-right tree for a mod's tier sequence, center-out for truly independent topics, or a convergent layout for verified cross-mod alternatives. Give every substantial chapter a thematic header and relevant background-like **positioned images**, with section-level visual cues and appropriate node sizing/shapes. All textures must be source/pack verified, legally reusable and readable at normal and reduced zoom.

Atlas lesson labels are research records, not one-to-one mandatory FTB node counts or saved IDs. Existing quest IDs, task truth and rewards take precedence during the presentation migration; cosmetic regeneration must not break claims. The active generator must be extended to preserve source-owned visual metadata **before** editing generated FTB SNBT. A source-based Atlas coverage PASS does not imply a rendered UI PASS.

## Existing pilot reconciliation

When a chapter or pilot already exists, inspect its authoring manifest and generated runtime quest data. Map every quest to the researched progression matrix and compare its prose with the actual task criterion, reward, exact ID and evidence. Record omissions (including a guidebook access step) and overclaims (such as treating item possession as operation). A pilot with many entries is not coverage proof until every required path and every installed addon has been reconciled. Distinguish proposed, implemented, merged, server-smoke-tested and client-accepted states.

For addons, inventory the full primary-source feature surface, inspect the exact pack pins, then intersect supported bridges with the current enabled-mod roster. Teach installed target integrations; label absent targets as out of scope. Keep addon profiles linked from the parent profile so feature counts and version gaps are visible.

## Detection honesty and actual behavior

A crafted Source Jar isn't proof of producing Source; an altar block isn't proof of performing a ritual; owning a weapon isn't proof of using it. **Never write the quest as though FTB verified an operation when the task only checks an item or inventory state.** Prefer a native advancement/event/real predicate when available; validate exact pinned task capabilities first. If unavailable, teach the behavior, state the acquisition proxy honestly, and use a clearly labeled manual read/check fallback where appropriate. Do not create unapproved repeated criteria, double rewards or new advancement triggers by assumption.

Use a minimal **positive test** and, for consequential gate/compatibility interactions, a **negative/boundary test** to verify the action and task credit separately. Check a second player/team for shared progress versus personal rewards when relevant.

## Rewards and safety

This standard **adds instructional depth**, not extra economy exceptions. Continue following the existing positive XP, useful fixed-item default, unique completion criteria and no auto-completing reward chains. An already approved XP-only orientation is a documented exception, **not permission for generic no-item/no-reward quests**. No quest reward, shop entry or acquisition path may bypass a gated boss, protected material, ProjectE timing, ban, or cross-faction restriction. Quests explain gates; the game implements them.

## Walkthrough QA / acceptance

A chapter can be marked **walkthrough-ready (research)** only when its coverage matrix has no unacknowledged progression gaps; version-confirmed mechanics, guidebook access and necessary construction references are recorded. It is **implemented** only when real quest definitions and rewards exist in the assembled branch; it is **client-accepted** only when a fresh disposable-world run demonstrates that a player can progress from entry to endpoint using only **quests + JEI + the in-game guide**.

Playtest review should explicitly verify:
- First guide access is explained or rewarded; guide section links or names are accurate.
- A new player is never stranded without knowing where the next material or prerequisite comes from.
- Each structure, machine and ritual includes actionable setup, activation, success signals and guide/JEI references.
- Side branches are understandable, optional where appropriate, and addons are accounted for.
- Quest task credit and claimed behavior agree; unique criteria and per-player rewards work correctly.
- The whole chapter can be followed without external websites, videos or knowledge from the author.

Use [mod-comprehension research instructions](mod-comprehension/WORK-CHAT-INSTRUCTIONS.md) and [handoff template](mod-comprehension/templates/QUEST-COMPAT-HANDOFF.md) to supply evidence. Existing chapters—including [the separate Ars pilot proposal](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/74)—need a gap audit against this standard; their prior validation is not proof of complete walkthrough coverage.
