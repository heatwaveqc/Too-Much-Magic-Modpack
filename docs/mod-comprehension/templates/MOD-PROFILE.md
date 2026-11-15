# <Mod name> — mechanics and gameplay profile

> **Template:** copy to `../mods/<manifest-id>.md`; remove this line, replace placeholders and keep unknowns explicit. Do not label an empty copy source-complete.

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `<manifest-id>` / `<actual game namespace(s)>` |
| Category / parent or addons | <category; linked profiles> |
| MC / loader / pack target | 1.21.1 / NeoForge / TMM `<integration SHA or branch>` |
| Pinned version / artifact / hash | <exact packwiz file + SHA1/SHA256 or UNVERIFIED> |
| Source snapshot | <immutable commit SHA/release tag; binary equivalence tested? yes/no> |
| Guide/documentation version gap | <matching / earlier-later; what still needs validation> |
| Profile status | researching |
| Evidence reached | documented upstream / inspected exact artifact / tested in pack / mixed |
| Reviewed | YYYY-MM-DD |
| Quest ownership | <existing chapter and link> |

## Executive gameplay map

**What the player actually does:** <5–10 sentences, not promotional copy. Describe the loop and purpose.>

**Why it matters in TMM:** <how it differs from overlapping magic schools; utility, distinctive progression and expected niche>

**Quick start:** <the cheapest viable entry sequence, including environment and prereqs>

### Player-value snapshot (a quick reference for quest designers)

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| <ranked brief features and use cases> | <access cost, setup and benefit; if fewer exist, say why> | <benefit and actual missing/known bridge; mark hypothesis> |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| <one row for every major system, addon separately> | [source: SHA] / [docs: version] / unverified | complete / partial / unresearched | <specific section or not established> | not run / tested | <precise gap> |

## Guidebook onboarding — required before the player's first complex step

- **Guide name and access:** <exact physical item recipe, first acquisition route or UI/keybind; give if safe as a quest reward>
- **Earliest attainable stage:** <environment, ingredients or progression gate>
- **First instructional quest:** <proposed quest name, steps to obtain/open/read, truthful task and thematic reward>
- **Reference map:** <guidebook section/page names for machines, multiblocks, rituals, structures, spells, upgrades, bosses, addons; verify pinned version>
- **Where guide is insufficient:** <missing or outdated instructions that quest text must supply>
- **Other in-game help:** <JEI recipes/uses/categories, tooltips, in-game quest/tutorial UI, discoveries>
- **If no guide exists:** <explicitly say so; document the self-contained quest instructions that replace it>

## Systems and full mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and source |
| --- | --- | --- | --- | --- |
| <machine, spell category, ritual, faction, worldgen, etc.> | ... | ... | ... | ... |

Cover all meaningful subsystems; expand with subsections if there are distinct mechanics (casting, crafting, automation, summoning, combat, transformations, exploration, UI, bosses, equipment, progression, etc.). Distinguish player-toggled modes from passive effects; record exact quantities and timers **only when supported**. A mod with no player-facing content should say so and document exposed API surfaces instead.

## Material / capability input-output contracts

| System | Input **kind** + exact ID/tag/event | Input qty/formula; consumed/remainder | Output **kind**, qty and location | Trigger/cadence; range/capacity | Automation/interface and evidence |
| --- | --- | --- | --- | --- | --- |
| <system> | item stack vs dropped entity; fluid vs Source vs player mana; FE, XP orb vs gem, entity/world state, etc. | <specific; not established if unknown> | <inventory, world drop, skill/effect, dimension; exact units> | event/timer/consumed item/fluid/entity state/world interaction | native / API / recipe JSON / KubeJS proven? [pin]/[source: SHA]/[pack-test: build/world] |

Explicitly answer, where applicable: **items; fluids; FE/mana/source/blood; XP; entities; mobs and AI; damage/effects; player or equipment state; block/world changes; automation; inputs to and outputs from rituals.** If a category is unsupported, support the negative finding with implementation evidence or say *not established*.

### Acquisition, renewability and output economics

- <sources, byproducts, sinks, recycling/renewability, grind constraints, passive production>
- <quantities and exact output forms: raw ore vs smeltable dust vs ingot; temporary vs persistent entity; resource drained per use>
- <items/tags subject to pack-specific substitutions>

## Complete player progression / walkthrough blueprint

**Completion test:** could a player get from nothing to the real final milestone using only the proposed quest steps, JEI and the in-game guide? Include **every essential intermediate step**, not a three-row early/mid/late summary. Treat addon unlocks and optional branches separately and explain which choices are mutually exclusive.

| Path and step | Gate or choice? | Exact prerequisite / materials / world condition | Player actions: obtain, build, activate, use | Unlock / visible success / next step | In-game guide section or JEI reference | Proposed quest/how-to, detection and reward | Evidence / coverage |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 0. Find/open guide | <required / optional> | <source or UI keybind> | <craft/open/access> | <guide access> | <guide name/page> | <guide onboarding quest, XP + safe reward> | ... |
| 1. Starting setup | <required / one valid option> | ... | ... | ... | ... | ... | ... |
| <every actual tier/unlock/subsystem, alternate path and capstone> | <required gate / selected branch / optional> | ... | ... | ... | ... | ... | ... |

For code-granted/hidden advancements, include the advancement parent chain and trigger type as source facts. Keep those separate from Guidebook narrative, recipe/ritual events, and the implementation predicate that actually grants progression. If a trigger is `minecraft:impossible`, the unlock action remains unknown until source or runtime evidence establishes it.

### Dependency graph and alternate routes

<Explain branching quest dependencies, costs of tier transitions, repeatable progression currency/research, biome/dimension/boss prerequisites, alternate recipes, side systems and irreversible faction choices. Specify gates enforced by the native mod versus currently implemented TMM code versus future proposals. Use text or a small Mermaid diagram if helpful.>

### Structures, rituals, automation and other how-to instructions

| Required setup | Placement/shape/activation and operating inputs | Guidebook page/JEI category to cite **by name** | Failure symptoms, throughput or maintenance | Quest where taught |
| --- | --- | --- | --- | --- |
| <all critical altars, multiblocks, other builds> | ... | ... | ... | ... |

### Advanced and terminal objectives

- **Normal completion:** <what counts as finishing the main progression, or the closest open-ended endpoint>
- **Optional completion:** <meaningful side branches and addons, combat/bosses/automation>
- **Remaining progression blockers:** <unknown prerequisites, guide holes and exact research/test action>
- **Quest coverage check:** <all required stages linked to how-to quest or precise guide reference; if not, label profile partial>

## Internal combinations and overlooked mechanics

| Internal combination | Full chain | Benefit | Limitation / exploit potential | Evidence |
| --- | --- | --- | --- | --- |
| ... | A → B → C | ... | ... | ... |

Include automatic loops, specialized crafting, movement, defense, farming, resource multiplication and meaningful alternate strategies when present.

## Low-hanging setups useful in ordinary survival

Aim for at least **three distinct, actually grounded** setups for a substantial mod; give fewer only with an explanation.

### 1. <Setup name> — <early / short chain / later>
- **Goal and payoff:** ...
- **Minimum entry and ingredients:** ... (rough effort, rare prerequisites and known exact items)
- **Build/use steps:** 1. ... 2. ... 3. ...
- **Sustainability and limits:** ... (one-time vs recurring upkeep, server friendliness)
- **Evidence / pack test:** ...

### 2. <Setup name>
<same fields>

### 3. <Setup name>
<same fields>

## API, integration and pack-config surfaces

| Hook or customization surface | Namespace / exact file, API or serializer | Capabilities / constraints | Current TMM override? | Evidence |
| --- | --- | --- | --- | --- |
| Recipes/tags/loot/config/commands/events/data map/API | ... | ... | present / none found / unknown | ... |

Document addons that introduce cross-mod content; explicit IDs only when checked. Call out client/server separation, teams/ownership, async timers, player attribute persistence, entity ownership and compatibility risks. Do not mistake "technically imaginable via Java" for an existing configurable hook.

## Compatibility leads

| Other mod | Candidate link and why | Native / pack-built / would require bridge | Status / source |
| --- | --- | --- | --- |
| ... | ... | ... | candidate; link interaction profile when present |

## Quest and teaching opportunities (research, not actual quests)

Use the complete progression table above as the **canonical chapter outline**; this table prioritizes the instructional details for authoring. Each quest is a **how-to with XP and a thematic reward**, not an unexplained acquisition checkbox.

| Stage and teaching goal | Actionable in-game instructions (prerequisites → steps → result) | Named guidebook section or JEI recipe | Intended behavior vs truthful FTB detector / fallback | Useful XP + fixed reward direction | Coverage and gating risk |
| --- | --- | --- | --- | --- | --- |
| First guidebook access | <how to obtain/open; never just 'read the wiki'> | <guide name + page> | <item/advancement vs actual reading; manual fallback> | <safe, no gift loop> | ... |
| <every needed stage> | ... | ... | <validated ID/event if known; item proxy explicitly limited> | ... | ... |

Consider *Tips & Tricks* for universally useful early discoveries, and the mod's chapter for deep progression. Do **not** add new quest tasks, loot or rewards as part of this research.

## Existing chapter / pilot reconciliation

| Walkthrough stage | Current quest ID/title and status | Intended action vs actual detector | Named in-game guide/JEI reference | Gap / next action |
| --- | --- | --- | --- | --- |
| <each required stage> | <none / branch / merged / client-accepted> | <operation, item proxy or manual fallback> | <exact page/recipe/category> | <missing instruction, test or no gap> |

Inspect canonical authoring data and generated quest data, not only a PR summary. Separately compare every addon feature with the installed pack roster; a pilot count is not coverage proof.

## Conflicts, limitations and version drift

- <tested conflict, observed behavior or unconfirmed hypothesis, each labeled>
- <version differences, upstream fork details, worldgen/mob/attribute interactions, gating hazards>
- <what the existing docs or UI currently says that the findings may contradict>

## Evidence ledger

| Claim / section | Source URL or exact inspected `file:path` + immutable SHA | Evidence tag / version of source | Pack version gap / what it proves / remaining limits |
| --- | --- | --- | --- |
| P-001 | ... | [pin] / [jar: SHA512] / [source: SHA] / [docs: version] / [pack-test: build/world] / [inference] | ... |

Cite the source **next to the important claim** as well as here. If a primary source is unavailable, state the attempted source and blocker.

## Disposable-world test plan

| Test | Positive/boundary case and disposable-world setup | Expected observable result (actual mechanic AND quest credit separately) | Performed? / evidence |
| --- | --- | --- | --- |
| <important progression or integration test> | valid setup + invalid/blocked variation | ... | not run / pass / fail (include build/world) |

No implicit test credit from parsing, compilation or dedicated server startup. For high-risk resource loops, gated entity drops and compatibility, include a positive and a negative/boundary test with controlled measurements. Label source formula and measured throughput separately. For routine low-risk features, a positive test is sufficient.

**Status requirement:** `source-complete` means full mechanics + every mandatory progression tier/branch has a source-backed how-to and guide/JEI reference, with unknowns confined to nonblocking details. It does **not** mean gameplay-tested. If the walkthrough graph has required unknown steps, keep `researching` and describe completed subsystems.

## Unknowns and next research

- [ ] <precise unanswered question; file/API/test to inspect next>

## Revision notes

- YYYY-MM-DD — <new evidence, corrected claim or version update>
