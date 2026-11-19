# Progression: scoped player capability development target

**Owner direction: 2026-11-16; narrowed 2026-11-18. Status: DESIGN TARGET, NOT GAMEPLAY ACCEPTANCE.** This replaces the former full four-system ownership parity requirement. The previous matrix remains available in git history.

**Player-only ownership (owner decision, 2026-11-18):** Progression supports innate/automatic/form-granted abilities and player-owned skill tree unlocks/ranks only. Research, item/resource costs, rituals, timed prerequisites and permanent installation-themed unlocks are flavors of acquisition for the same player-owned nodes, not an independent augment inventory or ownership type. A learned ability may be toggled inactive without changing ownership; configured skill-tree respec remains distinct from detachable traits. No equipment-owned effects, item affixes, gem/perk sockets, transferable/collectible ability tokens, uninstall/extraction/refund systems for removable installed traits, or ability trading/PvP theft. Equipment/item conditions for player-owned effects remain allowed; do not alter other mods' native equipment systems. Reference mods are donors of useful player-effect algorithms and acquisition/graph mechanics only, not full ownership parity. This supersedes earlier broad parity targets.

## Reference capability matrix

| Reference | Retain for player progression | Excluded |
|---|---|---|
| Oritech cybernetics | Prerequisite graphs, research, one-time cost or timed unlocks, active/passive toggles, relevant mechanical effects | Separate installed augment inventory, uninstall, extraction or reinstallation |
| Everlasting Abilities | Ranked persistent player effects, caps, utility, status, mobility, bounded aura and reactive effects | Collectible ability totems, transferable powers, ownership trade, PvP ability loss |
| Astral Sorcery | Attunement choices, activity XP, connected branches, prerequisites, rank scaling, configured respec | Socketed perks, interchangeable gem modifiers |
| Apotheosis | Triggered/contextual modifiers, proc patterns, caps, native equipment conditions applied to player-owned skills | Item affixes, rarity rolls, item progression, sockets, facets, gems, gear metadata or reforge/salvage transactions |

## Engine requirements

- One configuration-driven Progression runtime. Disciplines author data only. Independent named XP systems, built-in MXP, currency-specific costs and levels, server-authoritative acquisition and exact refund receipts for normal skill respec.
- Two player ownership forms: innate/automatic grants (including milestones/form eligibility), and unlocked/ranked skill nodes. An installation ritual is merely a way to unlock the node.
- Preserve native provider authority; predicates and effects may depend on equipped items/tags, form, environment, damage/target, resource thresholds, and rank. Equipment never owns Progression stats; existing mods' independently owned item bonuses remain untouched.
- Reuse attributes, statuses, immunities, vision, movement, mitigation, healing, harvesting, scoped loot, area and reactive proc mechanics where useful. Explicit caps, stacking, cooldowns, safe optional adapters, bounded queries and server-authoritative sync.
- No legacy save migration before first stable release; preserve within-build ownership through login, death and restart. No client-side entitlement, duplicate grants, or unsafe default skills.

## Acceptance and next work

Demonstrate data-only innate grants and skill nodes, one-time prerequisite/cost/timed unlocks where needed, toggles without detaching ownership, named XP wallets and connected tree requirements, configured respec where allowed, and dynamic effect conditions. Inventory only in-scope reusable mechanics with tests or explicit outstanding gaps. Do not require matching every donor's unrelated gameplay system. Batch 7 scope is limited to acquisition gaps; Batch 8 canceled. No new gameplay assignment or live test approved by this document.

## Reference material for later per-version auditing

- [Oritech Cybernetic Augmentation Center / data-defined augments](https://moddedmc.wiki/en/project/oritech/latest/content/augment_application_block) and [Oritech 0.14 cybernetic-system release](https://modrinth.com/mod/oritech/version/Wqjd2rMY).
- [Everlasting Abilities mod feature description](https://www.curseforge.com/minecraft/mc-mods/everlastingabilities).
- [Astral Sorcery 1.21.1 repository and perk-tree definitions](https://github.com/HellFirePvP/AstralSorcery/tree/1.21.1) and [1.21.1 localized attunement/perk mechanics](https://github.com/HellFirePvP/AstralSorcery/blob/1.21.1/src/main/resources/assets/astralsorcery/lang/en_us.json).
- [Apotheosis 1.21+ changes, gem facets and affixes](https://github.com/Shadows-of-Fire/Apotheosis/blob/26.1/changelog.md).

Use these to scope a future exact-version capability inventory, not to assume current pack feature completeness or to copy code/assets.
