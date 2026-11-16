# Piglin-aligned golden equipment progression — exploratory concept

**Status:** EXPERIMENTAL DESIGN ONLY; no implementation, runtime activation, or test-build registration.
**Branch:** `design/piglin-gold-progression-exploratory-2026-11-15` (forked from `test/full-integration`).
**Date:** 2026-11-15.
**Working name:** *Gilded* / *Piglin-aligned* discipline. Naming intentionally open.

## Player concept and intended experience

A new player progression/discipline makes **ordinary gold equipment a viable specialty**. When a trained, Piglin-aligned player uses gold gear, that gear behaves much better **for that player**, without upgrading or permanently enchanting the physical item. A player who has not earned the progression should experience normal gold equipment. Unlike a global gold-tool rebalance, these effects should be tied to the player, earned ranks, and wearing/using the equipment.

Owner's starting requests (requirements, **not** fixed values):

1. Piglin-aligned identity.
2. **Improved stats** for gold gear while the aligned player wears/wields it.
3. **Extra effective Fortune** when that player mines with gold tools.
4. **Extra effective Unbreaking** on that player's gold equipment.
5. **Very rapid automatic repair** of gold armor while worn and gold tools while equipped/used.

Do not assume a final skill-tree name, unlock ritual, XP economy, exclusivity relationship to Monk/Hunter, or numerical tuning without review.

## Proposed first skill domains (all provisional)

| Domain / draft skill | Target behavior | Provisional tuning for testing, not approved balance |
| --- | --- | --- |
| **Piglin Kin** | Recognized as gold-aligned; ordinary Piglins become nonhostile by default, while genuine retaliation remains possible. Ordinary vanilla worn-gold behavior already exists; the discipline should matter beyond it. | No special handling for Piglin Brutes unless explicitly approved; do not interfere with other mods' factions. |
| **Gilded Grip** | Improve gold-tool melee damage and/or mining speed only when wielded by an enrolled player. Gold armor receives conditional armor/armor-toughness or another combat stat benefit only while worn. | Start with small rank-scaled bonuses and test against iron/diamond/netherite; do not mutate global gold material definitions. Optional effective mining tier needs a separate decision. |
| **Fortune's Favor** | Gold pickaxes, axes, shovels, and hoes get bonus **effective Fortune** for applicable block drops; apply even if the item is not itself enchanted, and combine sensibly with its real enchantment. | Test +1 then +2 effective levels. Example policy: actual Fortune + progression bonus, configurable effective cap (candidate 5), respecting Silk Touch precedence. Exact algorithm must be validated against loot and enchantment mods. |
| **Lasting Gilt** | Gold tools and armor gain bonus **effective Unbreaking** only while used/worn. Physical enchantment NBT remains unchanged. | Test +1 through +3 effective levels; configurable cap. Coverage must include mining, attacking, and armor damage, not just one tool-use event. |
| **Golden Renewal** | Damaged gold armor self-repairs rapidly while worn; damaged gold tools self-repair while actively selected/used. | Test baseline 1 durability per 20 ticks, progressing toward 1 durability per 5 ticks, with editable rate and amount. The brief is **very quick**, so this is intentionally far faster than the Hunter pilot's 1/200 ticks. |

### Equipment targeting

- Initial target: **vanilla golden helmet, chestplate, leggings, boots, sword, pickaxe, axe, shovel, hoe**. Handle modded gold items only after verifying IDs and explicit opt-in tags.
- Use reusable item tags, provisionally `#tmm_progression:golden_tools`, `#tmm_progression:golden_armor`, and `#tmm_progression:golden_equipment`. These are proposed tags, **not verified existing registrations**.
- Armor is active while equipped in the four vanilla armor slots. Tool regeneration is active while the item is selected/held for use; define whether offhand tools qualify and how to avoid offhand parking as a free inventory-wide repair exploit.
- A gold item in a chest, main inventory, curio slot, or another player's hands gets no discipline effect by default.
- No repair beyond full durability; no item resurrection after a stack has already broken. Respect normal damage, break callbacks, enchantments, and item identity.

## Progression and faction questions to resolve

1. Is Piglin alignment a **standalone compatible discipline**, a mutually exclusive identity/class, or an additional ancestry/faction layer? **Do not add an exclusive group yet.**
2. What unlocks it: bartering milestone, wearing a full set of gold in the Nether, a crafted totem, or manual enrollment for the first prototype? Existing chassis supports manual enrollment for QA, but no Piglin-specific rite yet.
3. How should the relationship with Piglins work? Vanilla Piglins already tolerate a player wearing at least one gold armor piece, except for aggression causes. Should the discipline remove the gold-armor requirement, improve bartering, or both? Piglin Brutes and Hoglins should remain unaffected until separately approved.
4. Does **improved stats** mean enough to compete with iron/diamond/netherite on damage, armor and mining speed, or a different niche (high yields, survival through regeneration, weak against burst damage)? Gold's low durability is part of vanilla's tradeoff, so repairing it quickly has major balance consequences.
5. Does Fortune go above vanilla Fortune III when an actual enchanted tool is held? Testable prototype: additive effective level with configurable cap, but no final approved value.
6. Will Hunter, Monk and Piglin disciplines coexist? Keep the engine path independent by default, and avoid inventing restrictions.

Potential natural XP/milestones, **ideas only**: successfully barter with a Piglin (requires a trusted server event adapter), mine a gold ore block legitimately, craft/equip gold tools, survive Nether exploration while wearing gold, and discover bastions. Avoid granting XP from merely holding/repeatedly cycling an item or generating farmed gold with automation. These new triggers are **not all available** in the current native activity event list.

## Technical fit with the current `tmm_progression` chassis

The existing standalone progression module on `test/full-integration` already supports native skills/ranks and player-specific effects. Relevant reusable handlers:

- `gear_durability`: tag-selected periodic repair on equipment slots including all armor, mainhand and offhand, with `interval_ticks` and `per_rank`. This is a promising direct fit for **Golden Renewal**.
- `attribute`, `damage_bonus`, `break_speed`, `harvest_tier`: possible building blocks for temporary gold equipment improvements; verify that a selected-item condition is applied to **attribute** effects before using them for gold-only stats. Damage and mining selectors already exist but must be checked case by case.
- `faction_neutrality`: target-change interception with retaliation protection; may provide a starting point for Piglin alignment, but check vanilla gold-armor/Piglin anger behavior and modded aggression before claiming parity.
- `EffectHandlers.register` and equipment/block-break/block-drops hooks provide an extension seam for reusable new effects.

**Missing / requires code and live verification:**

1. An **effective Fortune** hook for gold tools that participates in the real loot/enchantment calculation exactly once, rather than post-hoc duplicate drops. Existing `treasure_hand` is an empty-hand Monk loot replacement, **not** an appropriate drop-in for gold tools. Verify Silk Touch conflicts, Apotheosis enchantment behavior, modded ores, loot tables, block entities and claim protections.
2. An **effective Unbreaking** hook for all relevant gold wear contexts without mutating a physical stack's enchantments or double-applying wear protection. Respect Minecraft 1.21.1 NeoForge damage semantics and source item identity.
3. Reusable **held/worn-item conditions** for transient attribute bonuses. Do not introduce permanent attributes into a gold item, and ensure modifiers clean up on swap, death, logout, reload, class leave and respec.
4. Piglin bartering/anger event hooks, if chosen for enrollment or XP.

The first functional prototype should be a **small datapack path plus reusable handlers** in the same progression module, not a second separate XP engine and not a Tyz MXP tree. A data-only path containing unknown effect types will be rejected by the strict parser, so **do not create an active path JSON referring to Fortune/Unbreaking before handlers exist**.

## Exploit and balance concerns

- Full-time very fast regeneration may largely negate gold's low durability. That is part of the requested fantasy but needs intentional rate caps and comparison to Mending, Unbreaking and modded auto-repair gear.
- Ensure effective Fortune applies to actual block loot **once**, not to existing item stack drops, chest loot, machine outputs or Silk Touch collection. Guard against duplication in vein mining, mass-breakers, spells, protected blocks and repeated event callbacks.
- Check gold ore/Nether gold ore outputs, enchantment overcap interactions, and any gold-to-shop-coin or EMC conversion loop. Preserve existing ProjectE/boss gates and banned loot items.
- Prevent inventory swapping immediately after an action from conferring the bonus on the wrong tool, and avoid permanently writing virtual Fortune/Unbreaking to NBT.
- Keep vanilla equipment and non-enrolled players unchanged, including ordinary Piglin friendliness from wearing gold.

## Minimum acceptance tests for a later implementation

1. Two players on a disposable server, one aligned and one not, use visually identical gold gear: only the aligned player receives effective bonus stats, drops, reduced wear and regeneration.
2. Gold pickaxe with Fortune 0/III and Silk Touch tested on stone/coal/diamond/gold/Nether gold and representative modded ore; no double drops, correct normal XP, correct fortune policy.
3. Gold sword/tool/armor with Unbreaking 0/III across mining, combat and incoming damage; compare average durability loss over many trials without changing stored enchantments.
4. Damage a full gold armor set and held gold tool; observe repair-rate progression with ticks; switch to inventory and another player, confirm appropriate suspension; no resurrection after break.
5. Test ordinary Piglins, aggravated Piglins, Piglin Brutes, dimension changes, armor swaps and Nether bartering. Only the explicitly approved faction policy should change.
6. Reload datapacks, relog, die/respawn, respec/leave if implemented, and run dedicated multiplayer; no stranded attribute modifiers or shared player state.
7. Exercise Fortune/repair with item automation, mass mining, Apotheosis and ProjectE/quest economy to check for repeatable profitable bypasses.

## Implementation status and isolation

- [x] Exploratory branch and design recorded.
- [x] Existing progression-chassis integration seams identified **from source**, not world-tested.
- [ ] Name, unlock rule, faction behavior and balance finalized.
- [ ] Active datapack path.
- [ ] New effective Fortune and Unbreaking handlers.
- [ ] Gold-only stats/selection QA.
- [ ] Dedicated-server and in-world tests.
- [ ] Registration in `manifest/integration-sources.json` and feature checklist **only after authorization**.

**Non-goal for this branch now:** do not change `test/full-integration`, `main`, mod lists, existing Monk/Hunter JSON, integrated build manifests, QA status, quests or live configuration. Do not mark this proposal tested or build-ready.
