# <Topic> — research-to-design handoff

> Draft recommendations only. This file does **not** authorize quest, recipe, KubeJS or balance edits.

| Field | Value |
| --- | --- |
| Linked profiles | <links to researched mods> |
| Linked interaction reports | <links to evidence-backed combinations> |
| Pack base and relevant pinned versions | ... |
| Readiness | research-only / needs pack test / tested and ready for design review |
| Prepared | YYYY-MM-DD |

## Required completeness audit: first guide → final milestone

What problem should the questbook solve for the player? Which choices remain optional? Preserve the five-category chapter architecture; addons generally belong in their parent mod's chapter. See the binding [quest walkthrough standard](../../QUEST-WALKTHROUGH-STANDARD.md).

**Guidebook onboarding:** <quest that tells the player precisely how to acquire/craft/open the in-game guide, or safely rewards it; source of the guide, earliest feasible stage, specific initial section>. If no guide, explicitly say so and make the quest text self-sufficient.

**Minimum chapter path:** <the playable sequence from zero knowledge through every native tier, main workbench, resource loop, major structure, ritual, upgrade and capstone; mark optional/mutually exclusive branches>. Never skip an unlock because a guide happens to exist.

| Progression stage / branch | Required? | Specific guidebook/JEI reference and prerequisite | Planned how-to quest(s) | Covered? / evidence / blocker |
| --- | --- | --- | --- | --- |
| 0. Obtain/open guide | yes if guide exists | ... | ... | ... |
| First working system | yes | ... | ... | ... |
| <each further tier, addon and capstone> | ... | ... | ... | ... |

A mandatory row without an actual quest and sufficient named in-game reference is a **walkthrough gap**; mark this handoff partial rather than complete.

## Recommended player-facing teaching sequence

| Quest/branch in order | Why, prerequisites and **ordered player instructions** | Named in-game guide section or JEI category; expected result | **Actual behavior vs FTB detector** (verified ID or honest proxy/manual) | XP + thematic fixed item | Risk / evidence / client test |
| --- | --- | --- | --- | --- | --- |
| ... | ... | ... | ... | ... | ... |

Respect `docs/QUEST-DESIGN-RULES.md`: every quest positive XP, normally thematic item reward; unique criteria unless expressly approved; rewards must not auto-complete other authored quests. A quest is not a mechanical gate. Do not introduce banned/restricted items or premature access to boss/ProjectE materials.

**Acceptance criterion:** A new player can actually follow **this chapter's quests + JEI + the in-game guide**, starting from no prior knowledge, to the final native milestone without external websites or unexplained build steps. Test guide access, setup instructions, progression sequence, correct FTB task attribution and personal rewards in a disposable world. Research-ready is distinct from implemented and client-accepted.

## Compatibility changes worth proposing

| Proposal | Existing vs missing function | Smallest technical hook | File(s) likely affected | Tests and acceptance criteria | Balance/approval needed |
| --- | --- | --- | --- | --- | --- |
| ... | ... | ... | ... | ... | ... |

Prefer existing native functionality. Do not add a bridge just to duplicate a built-in option.

## Evidence strength / issues requiring a decision

- Confirmed in current pack:
- Supported by exact-version source only:
- Unverified claims and tests:
- Owner decision required:

## Proposed next action

<Specific separate implementation request or test plan; no implied approval.>
