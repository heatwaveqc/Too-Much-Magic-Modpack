# <Mod name> — mechanics and gameplay profile

> **Template:** copy to `../mods/<manifest-id>.md`; remove this line, replace placeholders and keep unknowns explicit. Do not label an empty copy source-complete.

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `<manifest-id>` / `<actual game namespace(s)>` |
| Category / parent or addons | <category; linked profiles> |
| MC / loader / pack target | 1.21.1 / NeoForge / TMM `<integration SHA or branch>` |
| Pinned version / artifact / hash | <exact packwiz file and resolved artifact or UNVERIFIED> |
| Profile status | researching |
| Evidence reached | documented upstream / inspected exact artifact / tested in pack / mixed |
| Reviewed | YYYY-MM-DD |
| Quest ownership | <existing chapter and link> |

## Executive gameplay map

**What the player actually does:** <5–10 sentences, not promotional copy. Describe the loop and purpose.>

**Why it matters in TMM:** <how it differs from overlapping magic schools; utility, distinctive progression and expected niche>

**Quick start:** <the cheapest viable entry sequence, including environment and prereqs>

## Systems and full mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and source |
| --- | --- | --- | --- | --- |
| <machine, spell category, ritual, faction, worldgen, etc.> | ... | ... | ... | ... |

Cover all meaningful subsystems; expand with subsections if there are distinct mechanics (casting, crafting, automation, summoning, combat, transformations, exploration, UI, bosses, equipment, progression, etc.). Distinguish player-toggled modes from passive effects; record exact quantities and timers **only when supported**. A mod with no player-facing content should say so and document exposed API surfaces instead.

## Material / capability input-output contracts

| Producer / system | Accepted inputs (type and IDs where confirmed) | Output / world effect | Consumed? | Native automation or scriptability | Evidence |
| --- | --- | --- | --- | --- | --- |
| ... | items, item tags, fluids, XP, essence, FE, mana, blood, entity, block state, biome, effect, command, etc. | items, fluids, enchantments, XP, entity spawn, attribute, effect, blocks, dimension, scoreboard, etc. | ... | built-in / recipe JSON / KubeJS / other / unknown | ... |

Explicitly answer, where applicable: **items; fluids; FE/mana/source/blood; XP; entities; mobs and AI; damage/effects; player or equipment state; block/world changes; automation; inputs to and outputs from rituals.** If a category is unsupported, support the negative finding with implementation evidence or say *not established*.

### Acquisition, renewability and output economics

- <sources, byproducts, sinks, recycling/renewability, grind constraints, passive production>
- <quantities and exact output forms: raw ore vs smeltable dust vs ingot; temporary vs persistent entity; resource drained per use>
- <items/tags subject to pack-specific substitutions>

## Progression map

| Milestone / tier | Entry requirements and acquisition | Unlocks and practical payoff | Can be skipped? / gate | Evidence |
| --- | --- | --- | --- | --- |
| Earliest | ... | ... | ... | ... |
| Midgame | ... | ... | ... | ... |
| Late/endgame | ... | ... | ... | ... |

Add boss and dimension dependencies, worldgen/biome needs, knowledge-book dependencies, required player actions and alternate routes. Clearly separate built-in mechanic gates, current TMM enforcement and proposed future gates.

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

| Suggested stage | Meaningful player action or demonstrable milestone | Possible detection task | Possible safe thematic reward | Caveats |
| --- | --- | --- | --- | --- |
| ... | ... | observed item, advancement, event, read/checkmark if necessary | ... | unique task, gates, reward not auto-completing other quests |

Consider *Tips & Tricks* for universally useful early discoveries, and the mod's chapter for deep progression. Do **not** add new quest tasks, loot or rewards as part of this research.

## Conflicts, limitations and version drift

- <tested conflict, observed behavior or unconfirmed hypothesis, each labeled>
- <version differences, upstream fork details, worldgen/mob/attribute interactions, gating hazards>
- <what the existing docs or UI currently says that the findings may contradict>

## Evidence ledger

| Claim / section | Source URL or exact inspected `file:path` + version/SHA | Kind (upstream / artifact / pack-test / community) | What it proves / remaining limits |
| --- | --- | --- | --- |
| P-001 | ... | ... | ... |

Cite the source **next to the important claim** as well as here. If a primary source is unavailable, state the attempted source and blocker.

## Disposable-world test plan

| Test | Preconditions / commands / steps | Expected observable result | Performed? / evidence |
| --- | --- | --- | --- |
| ... | ... | ... | not run / pass / fail (include world/build if run) |

No implicit test credit from parsing, compilation or dedicated server startup.

## Unknowns and next research

- [ ] <precise unanswered question; file/API/test to inspect next>

## Revision notes

- YYYY-MM-DD — <new evidence, corrected claim or version update>
