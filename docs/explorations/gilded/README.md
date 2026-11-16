# Gilded — inactive JSON prototype

**Status: draft, intentionally NOT in active datapack path.** This subtree is deliberately under `docs/explorations/gilded/` so the JSON cannot be accidentally loaded into a server or the next test build.

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

## Files and intent

- `data/tmm_progression/tmm_progression/paths/gilded.json`: follows the exact `PathJson` schema 1 layout for ranks, activities, milestones and auto-rank skills. `enrollment: admin` is an existing parser-safe flag to disable self-service manual enrollment; a new ritual adapter will call the server join API. No exclusive group is assigned.
- `data/tmm_progression/tags/item/gilded_nether_foods.json`: explicit Nether-attainable **food item whitelist**, rather than `mynethersdelight:*` or a generic edible tag. The tag is about **item type**, not proof of where it was obtained. Includes mushroom stew, porkchops and rotten flesh (zombified Piglins), a nested tag of vanilla golden foods, plus four **optional** My Nether's Delight foods; modded recipe/actual food component and Nether-only ingredient tests required. Additional fully Nether-obtainable dishes should be added after JEI/recipe audit.
- Gold equipment, repairable gold+Netherite, Fortune-capable gold+Netherite tools, ordinary Piglin+Brute and gold ore target tags are broken out into separate files for reusable selectors.

**Porkchop compatibility caveat:** My Nether's Delight reportedly replaces Hoglin porkchop drops with Hoglin Loin. Vanilla porkchops remain explicitly allowed because the owner requested them, but if the strict policy means *obtainable from the Nether even after installing the addon*, audit the exact modded Hoglin loot table and retain an eligible Nether porkchop acquisition route (small secondary drop, conversion recipe, or confirmed existing Nether route). Do not blindly restore full vanilla loot and erase the addon's new Hoglin drops.

## Required Java implementation before activation

The JSON structure is syntactically valid and follows the existing progression parser for `activities`, `milestones`, `skills`, `auto_ranks` and the real `gear_durability` handler. **However five custom handler names are intentionally NOT registered in the engine**: `tmm_progression:food_whitelist`, `tmm_progression:piglin_affinity`, `tmm_progression:gold_diamond_equivalence`, `tmm_progression:virtual_fortune`, `tmm_progression:virtual_unbreaking`. The parser **rejects this path if moved into active `src/main/resources/data/` today**. The handler parameters are *proposed contracts*, not a promise that Java already reads them. Do not stub the handler to no-op merely to pass the parser.

- `food_whitelist` must cancel/deny unauthorized **food use before the effects or hunger/saturation apply** (including instant food, long food-use interactions, modded bowls and placed feasts). Nonfood potions/medicines should be unaffected; must not disable all consumables. No automatic blacklist of another mod's valid Nether-only foods—explicitly add to tag after verifying ingredients.
- `piglin_affinity` must prevent unprovoked anger *initiation* from mining gold/opening/breaking protected storage, and neutralize ordinary targeting for the specific enrolled player by **Piglins and Brutes**. Do not wipe or reset existing anger/other-player targets; retaliation after player attack remains valid.
- `gold_diamond_equivalence` must reflect equivalent damage, harvest tier, armor points/toughness for **worn/wielded gold** without changing persistent item components or lowering gold's vanilla fast mining speed.
- `virtual_fortune` adds +1 per unlocked rank (three ranks, **+3 at rank 11**) to actual Fortune once at legitimate block loot generation, with Silk Touch precedence and no item NBT mutation. User-locked final bonus = +3. No artificial enchantment cap.
- `virtual_unbreaking` adds +1 effective level per unlocked rank (three ranks, **+3 at rank 12** as a provisional proposed final value) without changing stack enchantments; exact bonus magnitude remains open.
- `gear_durability` is the existing registered effect; both gold and Netherite share one tag and **the exact same** 1, 2 or 3 durability restored per five ticks at path levels 1, 10 and 18. These rates and unlock levels are prototype values, not owner-locked. Armor and active mainhand qualify; verify whether the existing `slot:all` behavior permits offhand parked equipment and narrow it if necessary.
- The `external` activity `tmm_progression:gilded_gold_ore_broken_without_silk` can earn XP **repeatedly** via `ProgressionApi.recordExternal`, but a new trusted block-break adapter must emit the signal **once per successful player-originated gold ore break when the actual tool does not have Silk Touch**. Use the exact verified ore tag. Do not award from raw drop count, pre-canceled breaks, recipes, ordinary gold blocks, general Nether activities or automation. No extra non-owner-approved restriction on placed/purchased ore is built into this prototype.

## Prototype tuning

- 20 ranks, same cumulative XP ladder as the current Hunter pilot (0 to 28,000), 10 class XP per eligible ore. Two milestone bonuses at 32 and 256 qualifying blocks.
- Class abilities are automatic at certain path ranks; there are no spendable points; the Nether/full-gold/eat-golden-food initiation is **owner-locked** and described in `gilded_initiation.json` but still requires Java.
- Food restriction, Piglin/Brute affinity, gold diamond equivalence and first-tier renewal unlock with enrollment. Fortune I+1 at ranks 3/7/11, Unbreaking +1 at ranks 4/8/12; this distribution is a draft, not a locked class progression decision.

## Mod decision, not an installation

The original Nether's Delight is a different, older add-on; for MC 1.21.1/NeoForge use **My Nether's Delight `1.10.4.1`** (official Modrinth `https://modrinth.com/mod/my-nethers-delight`, CurseForge `https://www.curseforge.com/minecraft/mc-mods/my-nethers-delight`). Requires `farmersdelight`, already in pack. It is **suggested for the future Gilded-enabled build**, NOT installed, pinned, or added to manifests by this work. Check exact file metadata/hashes and gameplay compatibility before inclusion.

## QA and isolation

Check path parser in a harness after handler registration, test diet before consumption, empty hunger, placed feast and modded consumables, verify Piglin+Brute/no armor, gold alarm suppression preserving retaliation, gold-diamond property switches, Fortune III +3 = VI, netherite bonus, no item persistence, equal gold/Netherite repair, ore XP no-Silk only, and multiplayer claim/actor origin. Do not claim in-game tests without dedicated-server and client evidence.

This prototype is **not active**, **not compiled**, and **not in the current candidate test build**.
