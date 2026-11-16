# Gilded — Piglin-aligned gold and Netherite progression (exploratory)

**Status:** EXPERIMENTAL DESIGN ONLY; no implementation, runtime activation, or test-build registration.
**Branch:** `design/piglin-gold-progression-exploratory-2026-11-15` (forked from `test/full-integration`).
**Date:** 2026-11-15.
**Locked name:** **Gilded** (owner-approved, 2026-11-15).


## Player concept and owner-locked equipment contract

**Gilded** is a player-specific Piglin-aligned discipline. Its bonuses follow the enrolled player, not permanent item enchantments or upgraded global material definitions. A non-Gilded player with the same item gets ordinary vanilla stats. Keep this path isolated from the active integrated pack until implementation and live tests are authorized.

Owner-locked requirements (2026-11-15):

1. **Gold tools and gold armor operate at effective DIAMOND level while used/worn by Gilded players**: diamond-equivalent tool harvest tier and combat damage; diamond-equivalent armor points and armor toughness per slot. Keep gold's naturally faster mining speed and high enchantability—upgrading it must not accidentally *nerf* existing gold advantages.
2. **Gold tools and Netherite tools gain extra effective Fortune** while operated by a Gilded player. Extra Fortune enhances compatible block loot only; it does not confer Looting or Silk Touch.
3. **Gold AND Netherite tools and armor gain extra effective Unbreaking** while used/worn by a Gilded player.
4. **Gold AND Netherite tools and armor regenerate durability at exactly the SAME configured rate**, rapidly, when worn or actively held. Damageable items only; no backpack, container or idle inventory regeneration.
5. **Ordinary Piglins are nonhostile toward a Gilded player even without gold armor** and **must not become angry at that player for mining protected gold blocks or opening/breaking the containers covered by vanilla Piglin anger rules**. Keep aggression when the player attacks a Piglin/Piglin Brute; Piglin Brutes remain hostile.
6. The **name Gilded is final**. Progression level curve, ability rank unlocks, Fortune/Unbreaking bonus magnitude and exact numerical repair tick interval remain **exploratory/configurable**, not owner-locked.

Interpret “effective diamond level” by comparing **per-item combat, harvest and armor capabilities** while worn/wielded; do **not** rewrite stored max durability or item tier metadata globally. Effective gold mining tier must correctly harvest blocks that require a diamond pickaxe. Gold repair and Unbreaking address its limited native durability. Original diamond and Netherite items retain native material properties; **Netherite gains only the named virtual Fortune, Unbreaking and repair benefits**.


## Candidate skill domains (rank distribution still open)

| Domain / draft skill | Owner requirement | Implementation/balance starting point |
| --- | --- | --- |
| **Piglin Kin** | Ordinary Piglins treat Gilded players as allies, including while Gilded opens/chops vanilla protected containers or mines protected gold. | Do not globally disable anger. Target each verified Gilded player as the instigator. Attacks remain actionable and Brutes remain hostile. |
| **Gilded Grip** | Golden tools/armor have effective diamond-tier harvest, combat and defense stats. | Correct diamond equivalence by equipment type/armor slot; gold's existing higher mining speed and enchantability should survive. No permanent NBT edits. |
| **Fortune's Favor** | Additional effective Fortune on **gold or Netherite mining tools**. | Additive virtual Fortune suggested, possibly +1 to +2 with a configurable cap; preserve actual enchantment and Silk Touch behavior. The bonus/cap is not approved yet. |
| **Lasting Gilt** | Extra effective Unbreaking on **gold or Netherite tools and armor**. | Suggested +1 to +3 virtual levels with a configurable cap; apply to mining, combat and armor wear only once. Magnitude remains unapproved. |
| **Golden Renewal** | **Identical rapid repair rate** for equipped gold and Netherite items. | Provisional test rate: 1 durability per 20 ticks ramping to 1 per 5 ticks. One tunable schedule shared by both materials, rather than per-material rates. Final rate unapproved. |

### Equipment rules

- Vanilla initial target: golden helmet/chestplate/leggings/boots, sword/pickaxe/axe/shovel/hoe; **Netherite helmet/chestplate/leggings/boots, sword/pickaxe/axe/shovel/hoe**. Modded items enter only via audited opt-in tags.
- Proposed tags (not yet registered): `#tmm_progression:gilded_gold_tools`, `#tmm_progression:gilded_gold_armor`, `#tmm_progression:gilded_netherite_tools`, `#tmm_progression:gilded_netherite_armor`; combined tools/armor selectors may be built from these.
- Armor bonuses/repair/unbreaking require the corresponding equipment slot. Tool bonuses/repair/unbreaking require the actual active mainhand tool at the moment of action; other hands and offhand-item support require explicit testing. No unattended entire-inventory healing.
- Gold's **effective** diamond mining harvest tier must coexist with vanilla gold's fast mining speed. A harvest-permission workaround alone is insufficient if the client/server still apply wrong mining rules or drop logic.
- Virtual attributes and enchantments follow the **player and the action**, not the stack's persisted enchantments, components or creative tooltips. Prevent bonuses leaking to other players, machines and equipment later removed from slots.
- Neither repaired material can exceed full durability or be revived after breaking.


## Remaining progression design decisions

1. Is Gilded compatible with Monk/Hunter or exclusive with other identity disciplines? **No exclusivity group selected.**
2. How does Gilded enrollment unlock: bartering with Piglins, earning trust in a Bastion, a Nether gold rite, or another action? A manual join is sufficient for the first controlled QA prototype, not a shipping unlock.
3. Exactly how many virtual Fortune and Unbreaking levels, and what are their effective caps (including Apotheosis overlevel enchantments)?
4. What is the repair speed progression, and should the same rate be granted immediately or unlocked at higher Gilded ranks? **Gold and Netherite always share each unlocked rate.**
5. Whether additional bartering bonuses or Piglin reputation mechanisms should exist. **None are authorized just by the ordinary-Piglin anger exemption.**

Potential XP/milestones (**exploratory**, not available in current source by default): successful barter, legitimately mined gold ore, crafting/using gold gear, surviving Nether exploration, and exploring Bastions. Repeatedly placing/mining player-placed gold or cycling held gear must not create trivial infinite progression XP. Credit only verified server actions.


## Technical fit with the existing `tmm_progression` chassis

The existing custom progression engine on the `test/full-integration` base offers per-player paths, rank XP, equipment slot tick handlers and an extension seam. **This remains a design branch: no player-mod source changed.**

Existing candidate handlers:
- `gear_durability` can repair eligible damageable items in equipped armor/mainhand slots by configured amount and interval. Reuse this logic for **the same rank-scaled gold/Netherite rate**, not two independent stacked repair handlers.
- `attribute`, `damage_bonus`, `break_speed`, `harvest_tier` can partly represent Gilded gold diamond-equivalence, but currently need selected-gear and per-armor-slot conditions. The existing `harvest_tier` alone was designed for barehand harvesting; **do not assume it automatically changes the selected gold tool's effective material tier**.
- `faction_neutrality` has target-suppression and retaliation mechanisms, but vanilla Piglin chest/gold triggers can write an anger/target memory **before** ordinary target selection. A robust implementation must intercept or filter the **specific Piglin alarm initiation** for the offending Gilded player, not only clear targets later.
- `EffectHandlers.register` offers a trusted extension seam for validated new effect types and event contexts.

Required new mechanics / code:
1. **Player-and-tool-context effective Fortune** on gold and Netherite tools, applied once to normal applicable block loot, respecting actual enchantments, Silk Touch precedence, claim restrictions, high-level Apotheosis and modded loot. The Monk `treasure_hand` loot replacement is not an appropriate reusable Fortune implementation.
2. **Player-and-item-context virtual Unbreaking**, covering both eligible materials across tool mining/attacks and armor wear without rewriting item enchantments or applying protection twice.
3. **Effective gold diamond equivalence** for vanilla armor slots, damage and harvest/mining checks, with reconciliation when switching equipment and logging in/out. Verify existing fast gold mining speed is preserved; do not globally register gold as diamond.
4. **Source-specific Piglin anger exemptions:** suppress opening/**breaking** protected storage and breaking protected gold blocks **only when the acting player is Gilded**. Vanilla relevant targets include gold ore variants, Nether gold ore, raw gold block, gold block, gilded blackstone, chest/trapped chest/ender chest/barrel/shulker box and chest vehicles as applicable to the target MC version. Confirm exact 1.21.1 rules against code; do not broadly exempt all storage or alter mob state across players.
5. **Full ordinary-Piglin affiliation** while armorless, without preventing true attack retaliation or changing Piglin Brutes, Hoglins or zombified Piglins.
6. The equipment-event machinery should snapshot the **actual player + item** during block break/use/damage, preventing post-action hand swaps from altering Fortune and durability results.

Unknown effect types fail the current strict path JSON parser. **Do not activate a Gilded path referring to Fortune/Unbreaking until the corresponding validated handlers exist.**


## Exploit review and proposed controls

### Intended powers, not automatically exploits

- Diamond-performance gold with gold's faster mining and Netherite's very rapid free repair can outperform ordinary diamond equipment in sustained use. **This is deliberate specialization**, not a bug to nerf away without owner input.
- Gilded players freely mining gold/opening Bastion loot containers without angering ordinary Piglins is an **approved affinity perk**. Brutes and direct retaliation provide remaining danger.
- Mending's usefulness decreases substantially for Gilded gold/Netherite; this is expected. Don't add an arbitrary repair consumption cost without owner approval.

### Actual duplication, bypass and implementation risks

| Severity | Risk | Proposed mitigation |
| --- | --- | --- |
| **Critical** | **Second Fortune drop roll** duplicates original block output, re-rolls protected/block-entity loot or compounds with Apotheosis/modded harvesting. | Participate once in authoritative server loot generation; no post-break append/replacement after vanilla has already rolled. Exact stack/tool provenance and original XP preserved. |
| **High** | **Overcapped Fortune + existing Ore Blocks shop and ProjectE EMC** creates profitable transformation → shop-currency loops or bypasses progression gates. | Test ore block purchase → mine → process → sell, ore/resource EMC conversions and enchant-cap combinations; preserve existing shop balance and all boss gates. Do not disable intended Fortune merely because farm outputs are renewable. |
| **High** | **Unbreaking or repair fired twice** (two paths, event duplicates, armor slot aggregation) causes infinite effective durability beyond the specified rate. | Calculate single effective Unbreaking level per wear event and aggregate a single capped per-slot repair tick; enforce common gold/Netherite speed. |
| **High** | **Piglin anger immunity leaks to attacking Piglins, Piglin Brutes or unrelated players** via global anger cancellation or memory resets. | Exempt **specific anger causes + current player UUID**; preserve damage/retaliation and other targets; no wholesale clearing of mob anger. |
| **High** | **A temporary player skill becomes a permanent item upgrade** via enchant NBT/components or attribute leakage; transfer becomes a duplication/buff exploit. | No persistent item mutations; recompute per action and reversible per-slot attributes; verify item hand-off, death, logout, respec. |
| **Medium** | **Gold tier mismatch** allows harvesting diamond-tier blocks while drops fail or bypasses intended modded harvest requirements. | Run real break/drop tests for vanilla diamond-tier blocks, include modded harvest tags, avoid global tier writes. |
| **Medium** | **Offhand/inventory parking** can regenerate many items with idle switching, invalid slots, grave auto-equip or duplicate repair callbacks. | Equippable armor + selected mainhand only in first prototype; per-stack verified single repair each tick interval. |
| **Medium** | **Infinite Gilded XP** from placing/mining gold, machine-broken ore, automation or repetitive bartering farm without personal activity. | Trusted actual player action, placed-block/automation protections where relevant, cooldowns/unique milestones; don't reward item cycling. |
| **Medium** | **Other mods' armor/attribute systems stack as diamond-equivalent gold plus extra material armor** or wrongly treat gold as diamond. | Apply only the diamond-minus-gold delta at the actual slot, reevaluate equipment stats and test all Curios/attribute interactions. |

All proposed controls are design checks, not claims of implementation. This branch remains **outside** the next test build.


## Minimum acceptance tests for later implementation

1. **Comparison:** Gilded and non-Gilded players receive the same gold armor/tool and same Netherite armor/tool. Only Gilded receives the new effects. Item data and stats revert when handed to a non-Gilded player.
2. **Gold diamond equivalence:** compare armor/armor toughness, sword/axe damage, pickaxe harvest eligibility (including diamond and obsidian), vanilla golden tool's high mining speed and enchantability; check exact armor slot deltas and no global material change.
3. **Fortune:** gold and Netherite Fortune 0/III/overcapped gear, Silk Touch cases, representative vanilla and modded ores, rare block drops, mass mining and a multiplayer claim; count **one** loot roll and no duplicated XP.
4. **Unbreaking:** gold and Netherite armor/tool wear under mining, combat and armor damage; sampled expected durability loss with/without physical Unbreaking and virtual ranks. Confirm no persisted enchantment changes.
5. **Matched regeneration:** damage equal gold and Netherite gear, verify the **same** actual durability-per-tick repair rate and rank behavior on selected mainhand and worn armor, idle inventory/offhand behavior, item swap, break/removal, respec and logout.
6. **Piglin exemption:** Gilded wearing no gold can approach ordinary Piglins, break every vanilla gold trigger and open/break relevant containers including chest vehicles **without starting anger**; non-Gilded retains vanilla rules. Attacks by Gilded still provoke retaliation; Piglin Brutes still initiate hostility. Verify a second, non-Gilded player remains a valid independent aggression target.
7. **Economy:** run ore shop buy→Fortune mine→smelt/process→sell cycles, EMC conversions, Apotheosis Fortune beyond vanilla, and no bypass of gated materials/banned items.
8. **Lifecycle/performance:** data reload, reconnect, death, cross-dimension, two-player dedicated server, repeatedly swapped equipment, unrelated mod inventory, protected blocks; profile frequent per-tick repairs across simultaneous players.


## Implementation status and isolation

- [x] Dedicated isolated exploratory branch and proposal.
- [x] **Name locked: Gilded**.
- [x] Gold diamond-equivalent performance, Netherite extension, and **matched rapid regeneration** recorded as owner requirements.
- [x] Ordinary Piglin anger exemption for gold mining and container access recorded, with retained attack retaliation and Brute hostility.
- [x] Chassis seam review and specific exploit controls documented.
- [ ] Enrollment and skill rank costs, Fortune/Unbreaking values and repair-speed curve chosen.
- [ ] Native Piglin anger + selected-tool enchantment mechanisms validated against exact 1.21.1 source.
- [ ] Active validated Gilded datapack path and new effects.
- [ ] Diamond-equivalent equipment and two-material repair implementation.
- [ ] Dedicated-server, disposable-world and multiplayer tests.
- [ ] Explicit owner authorization to register in integrated test manifests/checklist.

**Non-goal for this branch now:** do not change `test/full-integration`, `main`, mod lists, existing Monk/Hunter JSON, integrated build manifests, QA status, quests or live configuration. Do not mark this proposal tested or build-ready.
