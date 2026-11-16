# Gilded — inactive JSON prototype

**Status: draft, intentionally NOT in active datapack path.** This subtree is deliberately under `docs/explorations/gilded/` so the JSON cannot be accidentally loaded into a server or the next test build.

## Files and intent

- `data/tmm_progression/tmm_progression/paths/gilded.json`: follows the exact `PathJson` schema 1 layout for ranks, activities, milestones and auto-rank skills. `enrollment: manual` is QA-only pending an owner-approved rite. No exclusive group is assigned.
- `data/tmm_progression/tags/item/gilded_nether_foods.json`: explicit Nether-attainable **food item whitelist**, rather than `mynethersdelight:*` or a generic edible tag. The tag is about **item type**, not proof of where it was obtained. Includes mushroom stew, porkchops and rotten flesh (zombified Piglins) plus four **optional** My Nether's Delight foods; modded recipe/actual food component and Nether-only ingredient tests required. Additional fully Nether-obtainable dishes should be added after JEI/recipe audit.
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
- Class abilities are automatic at certain path ranks; there are no spendable points or invented enrollment gate in this prototype.
- Food restriction, Piglin/Brute affinity, gold diamond equivalence and first-tier renewal unlock with enrollment. Fortune I+1 at ranks 3/7/11, Unbreaking +1 at ranks 4/8/12; this distribution is a draft, not a locked class progression decision.

## Mod decision, not an installation

The original Nether's Delight is a different, older add-on; for MC 1.21.1/NeoForge use **My Nether's Delight `1.10.4.1`** (official Modrinth `https://modrinth.com/mod/my-nethers-delight`, CurseForge `https://www.curseforge.com/minecraft/mc-mods/my-nethers-delight`). Requires `farmersdelight`, already in pack. It is **suggested for the future Gilded-enabled build**, NOT installed, pinned, or added to manifests by this work. Check exact file metadata/hashes and gameplay compatibility before inclusion.

## QA and isolation

Check path parser in a harness after handler registration, test diet before consumption, empty hunger, placed feast and modded consumables, verify Piglin+Brute/no armor, gold alarm suppression preserving retaliation, gold-diamond property switches, Fortune III +3 = VI, netherite bonus, no item persistence, equal gold/Netherite repair, ore XP no-Silk only, and multiplayer claim/actor origin. Do not claim in-game tests without dedicated-server and client evidence.

This prototype is **not active**, **not compiled**, and **not in the current candidate test build**.
