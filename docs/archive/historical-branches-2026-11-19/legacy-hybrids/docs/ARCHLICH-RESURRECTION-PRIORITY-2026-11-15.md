# Archlich resurrection: phylactery first, Arca second

**Decision locked by owner on 2026-11-15. Theoretical model only — not yet implemented or tested in the pinned Minecraft 1.21.1 mods.**

## Eligibility

- **Archlich** means the character has independently earned both the **JAWR: Forbidden Magic lich** and **Goety/Goetied lich** paths. Both native progression histories, powers, and resurrection resources must remain available.
- This priority applies **only** to an **Archlich**. A single-path lich uses its one native anchor; a Dracolich uses **exactly one** lich path and therefore has no two-anchor fallback. Dragon plus both lichdoms (Archlich + dragon) remains forbidden.
- The same player's two resurrection backends must never each independently cancel one lethal event and both charge resources or teleport the character.

## Locked attempt order

1. For a fatal event which would invoke lich resurrection, **check the JAWR phylactery first**.
2. If the phylactery can complete the rescue, **use it**. Finalize exactly one save; **do not activate, charge, discharge, or teleport via the Goety Arca**.
3. If the phylactery is **unavailable, depleted, unbound, ineligible for that death, or otherwise unable to complete a rescue**, attempt the **Goety Arca** as the fallback, provided its own eligibility rules permit it.
4. If neither can rescue, allow the event to proceed to **true death** and its normal inventory/grave handling. Corail Tombstone owns recoverable items/XP on true death only; no grave or true-death penalty on a successfully prevented death.
5. If the first rescue produces a **partially committed or ambiguous state** (e.g. consumed an item but no confirmed rescue), do **not** blindly run the other handler as a second independent death saver. Log a diagnostic/administrative hold until exact-version event behavior allows a safe, transactional retry or fallback. The priority is locked; edge-case rollback details are not yet proven.

In short: **Phylactery → Arca → true death**, with one successful rescue per lethal event, no double charges, and no accidental duplicate teleport.

## What is deliberately not decided

- Whether either anchor is consumed, depleted, teleports, or triggers before vanilla death in each pinned upstream version; must inspect real code before interception.
- How to handle partial costs/irreversible side effects if phylactery has started but cannot finish. Do not invent a rollback ability in an upstream provider.
- Player override/toggle is **not part of the approved model**. Fixed priority is the design target, not an optional preference UI.
- Exact event priority for other *non-lich* death prevention (vanilla totem, Occultism Guardian, Ars armor totem perk, Mahou soul protection, Vampirism DBNO, etc.) remains a separate cross-mod arbitration question.

## Implementation and test gates

- Audit actual pinned JAWR: Forbidden Magic and Goetied code for phylactery/Arca eligibility, resurrection events, persistence, resource expenditure and event cancellation; classify each potential failure state.
- Observe both successful one-anchor cases and both failure cases on fresh disposable worlds, then intercept only with verified cancellable hooks or narrow version-pinned mixins. Never run an unknown death handler twice to discover whether it works.
- Test **phylactery available + Arca available** (phylactery wins, Arca unchanged); **phylactery unavailable + Arca available** (Arca rescues); **both unavailable** (true death); **phylactery available + Arca unavailable**; damage-type exclusions, dimension transitions, logout, respawn, death-save overlaps and multiplayer.
- Record both anchor values and native lich levels before/after each attempt; assert no duplicate resources, abilities, revival, teleport, grave or inventory/XP outcomes.

See `docs/TRANSFORMATION-TRANSITION-POLICY.md` and `docs/TRANSFORMATION-BRIDGE-IMPLEMENTATION.md`.
