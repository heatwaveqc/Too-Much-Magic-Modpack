# Progression: broad capability-parity development target

**Owner direction: 2026-11-16. Status: DEVELOPMENT TARGET / DESIGN ONLY.**
This document describes what the shared `tmm_progression` engine should **eventually be capable of expressing**, not what it currently implements and not a scope assignment for the next QA build. **No new runtime feature, mod dependency, balance value, migration, or build requirement is authorized by this target.**

## Goal

The **single Progression engine** should ultimately have enough reusable, data-driven primitives to reproduce **all gameplay-relevant progression and enhancement behaviors** found in:

1. **Oritech cybernetic augmentations** — researched, prerequisite-gated, costed, permanently installed, optionally toggled player improvements.
2. **Everlasting Abilities** — persistent, leveled, transferable and configurable player abilities, including both passive and triggered/area effects.
3. **Astral Sorcery player attunement and perk points** — affiliation/attunement, activity-based experience, branching and connected perk graphs, point purchases, respecs, and socketed bonuses.
4. **Apotheosis gems and affixes** — equipment-dependent enhancement rolls, rarities, gem sockets, gem quality/progression, contextual effects, and conditional/on-event abilities.

**This is capability/expressiveness parity, not a mandate to clone those four mods, their recipes/machines, their content assets, exact UI, loot/worldgen, balance, or progression.** If an original mechanic has a machine, altar, item, kill/drop, or advancement prerequisite, Progression must be able to **model and validate the corresponding acquisition conditions and transaction** and accept authorized external integration events; it need not duplicate that world's machines or structures. The references are a feature inventory, not dependencies.

Existing mods and their native mechanics remain authoritative where installed. This engine must not replace, shadow, multiply, or double-apply their enhancements without a separately approved integration design.

## Architectural invariant

**All executable behavior belongs to `compat/progression`.** Monk, Hunter, Gilded and other disciplines remain **configuration-only** path packages: JSON/datapacks, Tyz trees and config, tags/data assets, localization and documentation. A required new proc, attribute operation, equipment condition, flight method, socket rule, or acquisition adapter must be implemented **once as a general Progression-owned handler/API**, then referenced by IDs and parameters in path data. No discipline-owned Java, KubeJS/JS or bespoke runtime scripts.

The desired abstraction is a typed, versioned **capability definition** describing *who owns an effect*, *how it is obtained*, *when it is active*, *what it affects*, *how it scales/stacks*, and *how it is removed or migrated*. Native Progression path ranks, Tyz/MXP rewards, persistent augments, collectible abilities, and item-bound affixes are **distinct ledgers and ownership modes**, even if they invoke the same effect primitives. Do not silently equate XP currencies.

## Reference capability matrix

| Reference system | Mechanics to be expressible by Progression | Engine primitives / behaviors needed |
| --- | --- | --- |
| **Oritech cybernetics** | Attribute/effect/custom augments; research trees and prerequisites; one-time item/energy/resource costs; unlock then install; installed-player persistence; toggleable abilities; nonrefundable removal; special effects such as ore vision, speed, size/reach, flight | Prerequisite/technology graphs, trusted installation transactions, cost providers, optional timed jobs and concurrent research state, permanent owned augments with active/inactive state, attributes/potions/custom handlers, hotkey/API toggles, uninstall/refund policy |
| **Everlasting Abilities** | Leveled permanent abilities that survive death; attribute, status, utility, flight/mobility, radius effects and reactive/death effects; ability bottles/totems, acquisition, transfer, level caps, cost/refund; optional PvP loss; per-ability datapack customization | Persistent player ability ledger, ranked effect scaling, area targets/teams, triggered procs, acquire/transfer/remove hooks with validation, caps/exclusivity, owner-defined death/PvP policy, explicit XP/currency ledgers, serializable item-backed ability tokens |
| **Astral Sorcery perk tree** | Attunement choice; action-specific XP and point awards; many connected branches with node prerequisites; choice/gates; rank/purchase/refund/respec/re-attunement; attribute and unusual perks; perk sockets with fitted gems | Declarative graph of nodes/edges and gates, separate progression wallet(s), trusted event-driven XP, unlock/eligibility conditions, purchase/respec/migration, stateful attunement/affiliation, player perk slots and socket effects, client UI backed by authoritative server data |
| **Apotheosis gems and affixes** | Rarity/quality-scaled randomized stat and special-effect affixes; restrictions by item class/slot; equipment sockets and socketable gems; differing bonuses by host item; gem facets/rarity upgrades and uniqueness; on-hit/on-hurt/conditional powers; loot/upgrade/reforge-like generation flows | Item-owned affix and socket metadata, deterministic/random seeded modifier rolls with ranges and weight tables, equipment-aware activation/revocation, slot/item-tag eligibility, facet/rarity progression, uniqueness/stacking limits, triggered event handlers and cooldowns, APIs for authorized loot/forge/salvage transactions |

**Source-version caveat:** These mechanics vary across releases. The goal is the combined **capability family** demonstrated by the references, not an assertion that any single pinned 1.21.1 build contains every newer feature. Track exact versions and verify parity from source during later implementation audits.

## Capability families required for eventual parity

1. **Acquisition and progress:** trusted enrollment/research/attunement/install/transfer actions; attribute/advancement/recipe/item/energy/XP resource costs; one-shot, repeatable and timed prerequisites; connected prerequisite graphs and optional mutually exclusive choices; configurable caps and level/rank progression.
2. **Effect ownership and lifetime:** player-native persistent effects, learned/unlocked-but-not-equipped effects, toggleable implants, removable/refundable abilities, temporary effects, and equipment-bound affixes with ownership on an ItemStack; predictable behavior on death, logout, dimension change, item transfer, equip/unequip, dispel and respec.
3. **Conditions and targeting:** player/path/rank/attunement, target ID/tag/faction, damage and projectile source, active equipment slot or item tag, wielded/empty hand, environmental and biome/dimension predicates, health/resource thresholds, proximity/radius, allies/teams and configurable exceptions.
4. **Effects:** additive/multiplicative/capped/reversible attributes, status effects and immunities, vision and detection, movement/flight/step/jump/size/reach, damage/mitigation/reflection/knockback, healing/hunger/absorption, mining/harvest/loot odds, crafting/resource interactions, team/area auras and custom event-driven capabilities. **An unimplemented or truly non-generic mechanic must create an engine extension, never a class-specific code fork.**
5. **Triggers and cadence:** on purchase/install/activate, on-hit/on-kill/on-hurt/on-block-break/on-item-use/on-potion-use/on-death/on-respawn, area entry/exit, and time-based effects where actually needed; target filtering, proc chance/priority, cooldown, charges, duration, safe scheduling and once-only accounting.
6. **Stacking and scaling:** ranks, rarity/quality, rolled ranges, context scaling, uniqueness per item/player, hard caps, exclusive groups, additive vs multiplicative stacking, precedence among native/gear/modded buffs, conflict detection, exact reversible modifiers and explicit refund/removal rules.
7. **Sockets and randomized modifiers:** gem-like modifiers attached to player perk slots **or** gear sockets; bounded socket counts; placement restrictions; equipment-dependent output; seeded or auditable random roll pools; rarity and gem cutting/facet/upgrade representations; non-duplication across load/save and item copying.
8. **Content/UI/tooling:** versioned, reload-validated JSON schemas, stable IDs, datapack override precedence, client descriptions/tooltips, inspectable prerequisites/points/active effects/installed augments/socketed gems, server-authoritative purchase and socket transactions, optional external addon APIs, diagnostics and save migrations.

## Engineering and integration constraints

- **Data-only disciplines.** A Monk, Hunter, Gilded or future config may only select registered primitive handlers and parameters. All new reusable gameplay code lives under the Progression engine and goes through a `feature/progression-*` PR targeting `dev/progression`.
- **Server authority and safety.** Clients request operations; the server checks source, ownership, costs, eligibility, counts, cooldowns, inventory state and valid target. Never trust client-provided point totals or arbitrary effect IDs.
- **No pervasive polling.** Prefer gameplay events, cached state, attribute modifiers and scheduled work for persistent effects. No per-tick scans of every dropped item, inventory slot or nearby entity. Auras must use bounded cadence/range/target budgets, not spawn uncontrolled entities or repeatedly reapply duplicates.
- **Compatibility by design.** Modded attributes/effects/equipment slots are referenced by registry IDs and tags; optional bridges should load safely when third-party mods are absent. Keep Tyz/MXP and native path XP separate unless the owner explicitly maps them.
- **Save/migration discipline.** Unique stable IDs, deterministic read/write, rollback on failed transactions, idempotent grants, reconciliation of equipment changes, and regressions for death, PvP, world reload, /reload, multiplayer and path respec are eventual requirements.
- **Balance is data.** Stat magnitudes, slot counts, acquisition costs, rarity weights, XP curve and exclusivity are intentionally unspecified. This target grants **no** default access to flight, high luck, infinite gems, boss drops or otherwise gated powers.

## Completion criteria — eventual, not current

Progression reaches this **development target** only after a later verified reference-mechanics inventory maps every relevant gameplay behavior from each of the four reference systems to (a) a tested generic engine primitive, or (b) a deliberately supported extension point, with representative data-only fixtures and automated tests. An agent must be able to author a new discipline or compatible item/augment definition through schema/config **without editing runtime code** for any mechanism already in the coverage inventory.

Representative future acceptance scenarios (not tasks authorized now):
- A persistent researched augment is installed after prerequisite and resource checks, toggled without losing ownership, then removed according to configured refund rules.
- A collectible, ranked ability survives death and logout, applies a bounded aura to valid non-allied targets, and can be transferred or lost under an explicit PvP policy.
- A constellation-like path awards event-specific points, enforces a branching node graph, supports a socketed perk, and refunds/respecs exactly once.
- An item rolls eligible affixes by rarity, takes a quality-scaled gem with context-dependent bonuses, activates only while equipped, and removes modifiers on unequip without duplication.

**Status tracking:** `TARGET ONLY — NOT IMPLEMENTED OR VERIFIED` for the four-system parity program. Existing implemented Progression features may overlap individual primitives; do not upgrade this status based on code compilation, independent mod availability, or an untested schematic. This target is not marked `READY FOR LIVE TEST`, is not added to the mandatory next-build inclusion register, and must not trigger a server smoke or Prism QA package.

## Reference material for later per-version auditing

- [Oritech Cybernetic Augmentation Center / data-defined augments](https://moddedmc.wiki/en/project/oritech/latest/content/augment_application_block) and [Oritech 0.14 cybernetic-system release](https://modrinth.com/mod/oritech/version/Wqjd2rMY).
- [Everlasting Abilities mod feature description](https://www.curseforge.com/minecraft/mc-mods/everlastingabilities).
- [Astral Sorcery 1.21.1 repository and perk-tree definitions](https://github.com/HellFirePvP/AstralSorcery/tree/1.21.1) and [1.21.1 localized attunement/perk mechanics](https://github.com/HellFirePvP/AstralSorcery/blob/1.21.1/src/main/resources/assets/astralsorcery/lang/en_us.json).
- [Apotheosis 1.21+ changes, gem facets and affixes](https://github.com/Shadows-of-Fire/Apotheosis/blob/26.1/changelog.md).

Use these to scope a future exact-version capability inventory, not to assume current pack feature completeness or to copy code/assets.
