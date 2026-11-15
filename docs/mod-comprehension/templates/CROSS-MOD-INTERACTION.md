# <A> × <B> — <specific useful interaction>

> **Template:** copy to `../interactions/<alphabetic-id-a>--<alphabetic-id-b>--<purpose>.md`. One narrow interaction per file. Never call a proposal implemented.

| Field | Value |
| --- | --- |
| Participant manifest IDs / versions | <A and B plus optional C; exact pinned release or unknown> |
| Source profiles | [A](../mods/<a>.md), [B](../mods/<b>.md) |
| Pack integration base | <branch + SHA> |
| Interaction status | candidate / source-supported / verified-in-pack / blocked / rejected / stale |
| Mechanism class | native in pinned mods / upstream addon only / in-pack override / data/config/KubeJS bridge / needs Java / unsupported |
| Recommended game stage | earliest / early / mid / late / endgame |
| Reviewed | YYYY-MM-DD |

## Player-facing payoff

<What specifically becomes easier/more interesting? Why choose this over the base routes? Does it help nearly every player, or a specialist?>

## End-to-end mechanism

`Producer A → exact output type (ID/tag/quantity, if confirmed) → bridge/interface → B's accepted input → result / rate / upkeep`

| Step | Mod and system | Actual type / registry hook / conditions | Consumed, transferred or transformed? | Evidence |
| --- | --- | --- | --- | --- |
| 1 | ... | ... | ... | ... |

Document **both sides of the typed contract** (item stack vs dropped entity, fluid vs special capability, entity vs experience gem), including accepted IDs/tags, quantity, consumed/remainder behavior, output location, cadence/trigger and range/capacity. Record a missing link rather than assuming the bridge exists. Describe recipe serializers, tags, energy/fluid units, event/player capability, entities and world-state consequences where relevant. Identify pack config/implementation files, if any.

## Existing support vs work required

- **Natively supported and present?** <yes/no/unverified; exact addon and version>
- **Pack implementation already exists?** <path / none found; does file actually make it into built pack?>
- **If not, smallest implementation:** <KubeJS recipe/tag, datapack, supported config, or Java bridge; concrete hook required>
- **What cannot currently be done?** <API limitations and blockers>

## Minimum practical setup

1. <unlock / collect>
2. <build / configure>
3. <run; explain upkeep and speed>
4. <measure result>

**Cost and power comparison:** <timing, resources, bypass risks, opportunity cost, existing alternatives, only sourced numbers>

## Balance, safety and compatibility

- Progression locks and item-gate leakage:
- Resource loops or duplication:
- Entity/world performance:
- Multiplayer, teams, dimensions and persistence:
- Exclusion risks (no Create, generic logistics, prohibited items, before-Chaos ProjectE, etc.):
- What version/config changes could invalidate this:

## Quest / Tips & Tricks opportunity

<Concrete **instructional how-to** (prerequisites, actions, success result and relevant guidebook section/JEI category); truthful FTB detection vs behavior; safe XP + thematic reward; owning chapter and whether optional. See [walkthrough standard](../../QUEST-WALKTHROUGH-STANDARD.md).>

## Verification plan

| Test ID | Positive or boundary case with disposable-world steps | Expected observable value/behavior; distinguish source formula from measured rate | Observed status |
| --- | --- | --- | --- |
| INT-01 positive | ... | ... | not run |
| INT-01 boundary (required for significant gates/compat/economy) | ... | ... | not run |

## Evidence and open questions

| Claim | Primary source URL / pinned artifact file + version | What verified / unknown |
| --- | --- | --- |
| ... | ... | ... |

- [ ] <specific missing registry/API/in-game observation>

## Recommendation

**Disposition:** document only / test native interaction / propose pack bridge / do not implement yet / reject.

**Why:** <evidence-backed short reason; any dependencies on an owner decision>.

## Revision notes

- YYYY-MM-DD — <new finding>
