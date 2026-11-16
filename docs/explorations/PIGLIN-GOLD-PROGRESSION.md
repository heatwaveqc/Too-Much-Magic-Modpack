# Gilded — Piglin-aligned gold and Netherite progression (exploratory)

**Status:** EXPERIMENTAL DESIGN ONLY; no implementation, runtime activation, or test-build registration.
**Branch:** `design/piglin-gold-progression-exploratory-2026-11-15` (forked from `test/full-integration`).
**Date:** 2026-11-15.
**Locked name:** **Gilded** (owner-approved, 2026-11-15).


## Player concept and owner-locked equipment contract

**Gilded** is a player-specific Piglin-aligned discipline. Its bonuses follow the enrolled player, not permanent item enchantments or upgraded global material definitions. A non-Gilded player with the same item gets ordinary vanilla stats. Keep this path isolated from the active integrated pack until implementation and live tests are authorized.

Owner-locked requirements (2026-11-15):

1. **Gold tools and gold armor operate at effective DIAMOND level while used/worn by Gilded players**: diamond-equivalent tool harvest tier and combat damage; diamond-equivalent armor points and armor toughness per slot. Keep gold's naturally faster mining speed and high enchantability—upgrading it must not accidentally *nerf* existing gold advantages.
2. **Gold tools and Netherite tools gain +3 effective Fortune levels ADDITIVELY** while operated by a Gilded player (actual Fortune III behaves like Fortune VI). Extra Fortune enhances compatible block loot only; it does not confer Looting or Silk Touch. Do not introduce an arbitrary Fortune cap that discards existing real enchantment levels.
3. **Gold AND Netherite tools and armor gain extra effective Unbreaking** while used/worn by a Gilded player.
4. **Gold AND Netherite tools and armor regenerate durability at exactly the SAME configured rate**, rapidly, when worn or actively held. Damageable items only; no backpack, container or idle inventory regeneration.
5. **Ordinary Piglins AND Piglin Brutes are initially friendly toward a Gilded player even without gold armor** and **must not initiate anger at that player for mining protected gold blocks or opening/breaking the containers covered by vanilla Piglin anger rules**. Preserve normal aggression following direct attacks, without deleting or resetting existing anger memories. Existing hostility to other players is unaffected.
6. The **name Gilded and +3 additive effective Fortune are final**, as are both Piglin types' initial friendship and the ore-break-only XP source. Progression XP amount, level curve, ability-rank unlocks, Unbreaking bonus magnitude and exact numerical repair tick interval remain **exploratory/configurable**, not owner-locked.

Interpret “effective diamond level” by comparing **per-item combat, harvest and armor capabilities** while worn/wielded; do **not** rewrite stored max durability or item tier metadata globally. Effective gold mining tier must correctly harvest blocks that require a diamond pickaxe. Gold repair and Unbreaking address its limited native durability. Original diamond and Netherite items retain native material properties; **Netherite gains only the named virtual Fortune, Unbreaking and repair benefits**.


## Candidate skill domains (rank distribution still open)

| Domain / draft skill | Owner requirement | Implementation/balance starting point |
| --- | --- | --- |
| **Piglin Kin** | Ordinary Piglins **and Piglin Brutes** treat Gilded players as allies, including when Gilded opens/breaks protected containers or mines gold. | Prevent **new cause-specific hostility initiation** against the acting Gilded player, rather than clearing mob anger. Direct attacks still provoke retaliation; other players retain normal hostility. |
| **Gilded Grip** | Golden tools/armor have effective diamond-tier harvest, combat and defense stats. | Correct diamond equivalence by equipment type/armor slot; gold's existing higher mining speed and enchantability should survive. No permanent NBT edits. |
| **Fortune's Favor** | **+3 additive effective Fortune levels** on **gold or Netherite mining tools**, including already-enchanted tools. | Owner-locked: actual Fortune III yields effective VI; do not reduce real overlevel enchantments via a synthetic cap. Respect Silk Touch and valid actual block loot computation. |
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
2. **Enrollment is locked:** in the Nether, wearing a complete four-piece golden armor set, successfully eat a golden apple, enchanted golden apple, or golden carrot. The new event adapter must enroll at successful consumption completion; the self-serve manual command must not bypass the ritual.
3. How many virtual Unbreaking levels and at what ranks? Fortune is **locked at +3 additive levels**, with no new Gilded-specific cap; test natural enchantment overlevels including Apotheosis.
4. What is the repair speed progression, and should the same rate be granted immediately or unlocked at higher Gilded ranks? **Gold and Netherite always share each unlocked rate.**
5. Whether additional bartering bonuses or Piglin reputation mechanics should exist. **None are authorized by Piglin/Piglin-Brute friendship or anger-prevention alone.**

**Locked Gilded XP activity:** Award configurable native Gilded progression XP **once per successfully broken and harvested gold ore block without Silk Touch**, regardless of Fortune level or drop quantity. Initial vanilla targets: `minecraft:gold_ore`, `minecraft:deepslate_gold_ore`, `minecraft:nether_gold_ore`. A verified modded gold-ore tag may be added after registry inspection. No XP merely for bartering, crafting, holding gold, passive Nether exploration, or opening containers. Use the authoritative successful block-harvest/drop event (not a speculative pre-break callback), and inspect the **actual breaking tool's Silk Touch** at action time. Do not add a user-unapproved restriction on whether the ore was player-placed; Silk Touch already prevents recycling an ore block through itself, and purchased ores can legitimately be mined for Gilded XP at their shop cost. No extra XP from the number of raw items generated by Fortune.


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
4. **Source-specific Piglin anger exemptions:** prevent anger **initiation** for opening/**breaking** protected storage and breaking protected gold blocks **only when the acting player is Gilded**. Vanilla relevant targets include gold ore variants, Nether gold ore, gold block, gilded blackstone and protected chest/barrel/shulker containers as applicable to the target MC version. Confirm exact 1.21.1 list and alarm/alert call paths against code; do not clear memory/hostility after the fact or alter mob state for unrelated players.
5. **Ordinary Piglin AND Piglin Brute affiliation** while armorless, implemented in the respective target/anger eligibility checks rather than deleting existing hostility. Attacks must provoke retaliation, and unrelated players remain eligible targets. Hoglins and zombified Piglins unchanged.
6. The equipment-event machinery should snapshot the **actual player + item** during block break/use/damage, preventing post-action hand swaps from altering Fortune and durability results.

Unknown effect types fail the current strict path JSON parser. **Do not activate a Gilded path referring to Fortune/Unbreaking until the corresponding validated handlers exist.**


## Exploit review and proposed controls

### Intended powers, not automatically exploits

- Diamond-performance gold with gold's faster mining and Netherite's very rapid free repair can outperform ordinary diamond equipment in sustained use. **This is deliberate specialization**, not a bug to nerf away without owner input.
- Gilded players freely mining gold/opening Bastion loot containers without angering ordinary Piglins is an **approved affinity perk**. Direct retaliation and other Nether threats remain; **Piglin Brutes are not independently hostile to unprovoking Gilded players**.
- Mending's usefulness decreases substantially for Gilded gold/Netherite; this is expected. Don't add an arbitrary repair consumption cost without owner approval.

### Actual duplication, bypass and implementation risks

| Severity | Risk | Proposed mitigation |
| --- | --- | --- |
| **Critical** | **Second Fortune drop roll** duplicates original block output, re-rolls protected/block-entity loot or compounds with Apotheosis/modded harvesting. | Participate once in authoritative server loot generation; no post-break append/replacement after vanilla has already rolled. Exact stack/tool provenance and original XP preserved. |
| **High** | **Overcapped Fortune + existing Ore Blocks shop and ProjectE EMC** creates profitable transformation → shop-currency loops or bypasses progression gates. | Test ore block purchase → mine → process → sell, ore/resource EMC conversions and enchant-cap combinations; preserve existing shop balance and all boss gates. Do not disable intended Fortune merely because farm outputs are renewable. |
| **High** | **Unbreaking or repair fired twice** (two paths, event duplicates, armor slot aggregation) causes infinite effective durability beyond the specified rate. | Calculate single effective Unbreaking level per wear event and aggregate a single capped per-slot repair tick; enforce common gold/Netherite speed. |
| **High** | **Piglin/Brute friendliness leaks to retaliation or unrelated players** via globally resetting anger or clearing AI memory after an event. | Suppress **cause-specific initiation** only for eligible Gilded actor UUID, including existing hostile target selection against an unprovoking Gilded player. Preserve retaliation and all other players' target state; no wholesale clearing. |
| **High** | **A temporary player skill becomes a permanent item upgrade** via enchant NBT/components or attribute leakage; transfer becomes a duplication/buff exploit. | No persistent item mutations; recompute per action and reversible per-slot attributes; verify item hand-off, death, logout, respec. |
| **Medium** | **Gold tier mismatch** allows harvesting diamond-tier blocks while drops fail or bypasses intended modded harvest requirements. | Run real break/drop tests for vanilla diamond-tier blocks, include modded harvest tags, avoid global tier writes. |
| **Medium** | **Offhand/inventory parking** can regenerate many items with idle switching, invalid slots, grave auto-equip or duplicate repair callbacks. | Equippable armor + selected mainhand only in first prototype; per-stack verified single repair each tick interval. |
| **Medium** | **Gilded XP credit bypasses the approved rule** by crediting Silk Touch, repeated drops, other block types or automated breaks. | Credit exactly once per successful player-originated gold ore break **without Silk Touch**, including eligible placed/purchased gold ore; reward the block event, not every dropped raw-gold item. No unrelated XP events. |
| **Medium** | **Other mods' armor/attribute systems stack as diamond-equivalent gold plus extra material armor** or wrongly treat gold as diamond. | Apply only the diamond-minus-gold delta at the actual slot, reevaluate equipment stats and test all Curios/attribute interactions. |

All proposed controls are design checks, not claims of implementation. This branch remains **outside** the next test build.



## Quest Shop Fortune arbitrage (owner query, 2026-11-15)

**Scope:** Only the three *repeatable* FTB Quests sales, no one-time sales, no ProjectE, no modded processing/ore duplication. Source: `manifest/quest_content.json` on the integration baseline for the actual repeatables; current registered ore-purchase stream is `feature/questshop-vanilla-ore-blocks:manifest/quest_shop_catalog.json`. Purchases are a `runtime-experimental` build target; figures are design economics, not in-world-tested shop behavior.

| Commodity | Repeatable sale, worth 1 coin | Shop purchase | Required average base-item yield **per purchased ore** for 1-coin break-even | Minimum effective Fortune F for break-even on **expected value** |
| --- | --- | --- | ---: | ---: |
| Copper | 256 copper blocks = 2,304 ingots | 2 copper ore / coin | 1,152 raw copper/ore | **658** |
| Diamonds | 64 diamond blocks = 576 diamonds | 1 diamond ore / 10 coins | 5,760 diamonds/ore | **11,519** |
| Emeralds | 128 emerald blocks = 1,152 emeralds | 1 emerald ore / 10 coins | 11,520 emeralds/ore | **23,039** |

Vanilla Java Edition Fortune ore-drop expected multiplier:
`M(F) = 1 + F(F+1) / (2(F+2))` for nonnegative F. Each diamond/emerald ore produces 1 base item; copper ore produces 2–5 raw copper averaging 3.5 base items. `F=6` yields `M(6)=3.625`, so expected yields are 3.625 diamond/emerald and 12.6875 raw copper per ore.

At Fortune VI, one repeatable sale requires (expected average, fractional purchase cycles rounded only for display): **90.8 coins** to buy enough copper ore, **1,589 coins** for diamond ore, or **3,178 coins** for emerald ore; each returns **1 coin**. These numbers do not count smelting fuel or time, which only make purchases less profitable.

No Fortune cap is needed to protect **these existing repeatable quest exchanges**. Their high input costs already make shop-purchase->mine->sell loops radically unprofitable at Gilded Fortune +3 or ordinary enchantment overlevels. The expected-value break-even Fortune levels above are mathematical extremes; extremely unusual luck or modded multiplier/loot overrides may differ. Ordinary mining-generated ores can still give renewable resources or rewards—this is specifically a *purchased ore vs repeatable coin sale* analysis.

Source references for verification: `docs/QUEST-SHOP-ECONOMY.md`, `manifest/quest_content.json`, ore-shop stream `manifest/quest_shop_catalog.json`, and Minecraft Fortune ore drops documentation at `https://minecraft.wiki/w/Fortune`.

## Locked food restriction and prototype path (2026-11-15)

- **Gilded may consume only food item types obtainable from Nether resources**, regardless of current dimension or item's actual source. This applies to ordinary hunger/food use, not potions or unrelated non-food consumables.
- Initial explicit examples: mushroom stew, raw/cooked porkchops from Hoglins, and rotten flesh from zombified Piglins. Add suitable **My Nether's Delight** (`mynethersdelight`) meat and Nether-ingredient meals, verifying actual recipes and live food properties. Do **not** approve all items in a mod namespace just because that mod has a Nether theme.
- For Minecraft 1.21.1 NeoForge, **My Nether's Delight 1.10.4.1** (not the older original *Nether's Delight*) is the recommended future addition *when Gilded ships*. Requires Farmer's Delight, already present. No packwiz or manifest changes authorized yet.
- JSON-only cannot enforce the consumption ban with current `tmm_progression`: `food_consumed` hooks fire too late. A trusted pre-consume handler is required to block hunger/food effects; the draft JSON intentionally resides in `docs/explorations/gilded/` and will be rejected if loaded before handlers are registered.
- Draft datapack schema, optional-food whitelist, equipment/entity tags and implementation checklist: [Gilded inactive prototype](gilded/README.md). The 16-category class ideation map is in [monster-aligned disciplines](MONSTER-ALIGNED-CLASS-IDEAS.md).

## Owner-locked golden initiation and food extension (2026-11-16)

**Required initiation:** In `minecraft:the_nether`, wear **all four ordinary golden armor pieces** in their actual head/chest/legs/feet slots and *successfully finish eating* one of `minecraft:golden_apple`, `minecraft:enchanted_golden_apple`, or `minecraft:golden_carrot`. This triggers Gilded enrollment once, on the server. A golden carrot requires the usual hunger preconditions; enchanted golden apples can be eaten when full. No Piglin barter, extra altar, XP precondition or outside-Nether initiation. If a mod adds an enchanted golden carrot, confirm its actual registered ID and add it to the rite tag (none in vanilla).

**Diet = Nether food OR any legitimate gold food.** The Gilded restriction allows (a) explicit Nether-attainable food types from the existing tag, **or** (b) explicit golden food types, **or** (c) any food whose **crafting** recipe has a gold nugget/ingot/block ingredient, including gold-equivalent ingredient tags and nested golden food ingredients in a recipe chain. This gold exception applies even when other recipe ingredients originate outside the Nether. Do not derive membership merely from the word “gold” in an item name or mod namespace. Enchanted golden apples qualify explicitly even though uncraftable in modern vanilla.

The inactive prototype now includes:
- `gilded_initiation.json`: ritual conditions and a proposed successful-eating adapter contract.
- `data/tmm_progression/tags/item/gilded_rite_foods.json`: vanilla golden apple, enchanted golden apple and golden carrot; verified modded enchanted variants may be added here.
- `data/tmm_progression/tags/item/gilded_gold_foods.json`: explicit golden-food whitelist that is incorporated by the Nether-food tag.
- `gilded_food_policy.json`: proposed data-driven server recipe classifier for additional gold-derived crafted foods.

**Technical safety:** The existing `PathJson` only understands enrollment markers `admin`, `manual` and `sunset`. To prevent non-operator players bypassing this rite through `/tmmprogress enroll`, the prototype uses **`enrollment: "admin"` as a compatibility flag**. Actual players enroll by a **new trusted adapter calling `ProgressionApi.join(player,"tmm_progression:gilded")` only after the successful-use conditions**. Operator `admin join` remains for QA. The new ritual and food-policy JSON files are **data contracts only** and are NOT yet read by the engine. Enroll on successful **finish**, not click/use-start; verify item actually consumed and prevent repeated enrollment feedback or granting XP on the rite.

**Food enforcement:** Existing `ProgressionEvents.consumedFood` fires at `LivingEntityUseItemEvent.Finish`, too late to reliably cancel food effects. Implement a separate pre-consumption validation/cancellation hook for **already-Gilded** characters; ensure the rite remains accessible before enrollment. Validate both direct item tag entries and recipe-derived gold foods on the dedicated server, updating cache on recipe datapack reload. The discovered recipe ingredient tags must exist in the actual pack registry or be treated as empty, not cause parser failure. An empty or self-referential recipe tree must not grant a food category. Preserve unrelated potions, healing and non-food consumables.

**Live QA:** consume each of all three vanilla rite items in Nether with full gold armor => joins once; any missing piece, wrong armor material, Overworld/End, failed/canceled eating, creative/spectator, and attempts to run `/tmmprogress enroll` => fail. Verify armor swapping mid-eat, second-hand item, multiplayer, relog and named/gold-looking non-gold foods. Food checks: golden apples/carrots, gold-nugget recipe outputs, recipes using golden carrots, enchanted golden apple, all previously approved Nether foods, non-gold Overworld foods denied. Modded enchanted carrot only if actually registered.

## Minimum acceptance tests for later implementation

1. **Comparison:** Gilded and non-Gilded players receive the same gold armor/tool and same Netherite armor/tool. Only Gilded receives the new effects. Item data and stats revert when handed to a non-Gilded player.
2. **Gold diamond equivalence:** compare armor/armor toughness, sword/axe damage, pickaxe harvest eligibility (including diamond and obsidian), vanilla golden tool's high mining speed and enchantability; check exact armor slot deltas and no global material change.
3. **Fortune:** gold and Netherite Fortune 0/III/overcapped gear, Silk Touch cases, representative vanilla and modded ores, rare block drops, mass mining and a multiplayer claim; count **one** loot roll and no duplicated XP.
4. **Unbreaking:** gold and Netherite armor/tool wear under mining, combat and armor damage; sampled expected durability loss with/without physical Unbreaking and virtual ranks. Confirm no persisted enchantment changes.
5. **Matched regeneration:** damage equal gold and Netherite gear, verify the **same** actual durability-per-tick repair rate and rank behavior on selected mainhand and worn armor, idle inventory/offhand behavior, item swap, break/removal, respec and logout.
6. **Piglin exemption:** Gilded wearing no gold can approach ordinary Piglins **and Piglin Brutes**, break every verified vanilla gold alarm trigger and open/break relevant containers including applicable chest vehicles **without starting anger**; non-Gilded retains vanilla rules. Attacks by Gilded still provoke retaliation; verify a second, non-Gilded player remains a valid independent aggression target and no existing anger was erased.
7. **Economy:** repeatable-only vanilla ore-shop analysis establishes that even effective Fortune VI cannot come close to coin arbitrage (see calculation below). Still QA non-vanilla ore multipliers and other future purchasing/selling routes separately. Check EMC when unlocked, and preserve banned-item restrictions.
8. **Lifecycle/performance:** data reload, reconnect, death, cross-dimension, two-player dedicated server, repeatedly swapped equipment, unrelated mod inventory, protected blocks; profile frequent per-tick repairs across simultaneous players.


## Implementation status and isolation

- [x] Dedicated isolated exploratory branch and proposal.
- [x] **Name locked: Gilded**.
- [x] Gold diamond-equivalent performance, Netherite extension, and **matched rapid regeneration** recorded as owner requirements.
- [x] Ordinary Piglin **and Piglin Brute** initial friendliness, no new gold/container anger initiation for Gilded, and retained attack retaliation recorded.
- [x] Chassis seam review and specific exploit controls documented.
- [x] Fortune bonus locked at **+3 effective levels additive to actual Fortune**, without a new arbitrary cap.
- [x] Gilded XP source locked to **successful non-Silk-Touch mining of gold ore**, once per ore block.
- [ ] Enrollment and skill rank costs, Unbreaking bonus values and repair-speed curve chosen.
- [ ] Native Piglin anger + selected-tool enchantment mechanisms validated against exact 1.21.1 source.
- [ ] Active validated Gilded datapack path and new effects.
- [ ] Diamond-equivalent equipment and two-material repair implementation.
- [ ] Dedicated-server, disposable-world and multiplayer tests.
- [ ] Explicit owner authorization to register in integrated test manifests/checklist.

**Non-goal for this branch now:** do not change `test/full-integration`, `main`, mod lists, existing Monk/Hunter JSON, integrated build manifests, QA status, quests or live configuration. Do not mark this proposal tested or build-ready.
