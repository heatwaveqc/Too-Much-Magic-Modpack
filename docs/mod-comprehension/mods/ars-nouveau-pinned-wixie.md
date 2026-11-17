# Ars Nouveau pinned Wixie crafting and potion work

Exact `ars_nouveau-1.21.1-5.13.5.jar`, SHA-1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; direct JSON/CFR0.152. [Parent](ars-nouveau.md), [Source supply](ars-nouveau-pinned-source.md), [glyph acquisition](ars-nouveau-pinned-guide-glyphs.md). Evidence classes: `WixieCharm`, `AbstractSummonCharm`, `WixieCauldron`/`WixieCauldronTile`, `EntityWixie`, all three `goal/wixie` classes, and `api/recipe/{MultiRecipeWrapper,CraftingManager,PotionCraftingManager}`. No actual automation, potion or reload test.

## Obtain and house the worker

Read native Notebook **Crafting → Wixie Charm**, related potion/storage entries, and **Glyph Index → Dispel**. `recipe/dispel_entity/wixie_shard.json` matches vanilla Witch plus percent-health-equal-or-lower0.5; `loot_table/dispel_witch.json` yields one `ars_nouveau:wixie_shards`. Dispel's native description teaches the weakened-Witch route; EventHandler.dispelEvent dispatches matching recipes for a living entity on ServerLevel. Keep the Witch alive, weaken to at most half health, then cast Dispel and observe replacement/shard. A killed Witch or merely clearing its potion effects is not a verified shard interaction.

Learn Dispel through Scribe's Table: Abjuration Essence plus three Milk Buckets,27experience points, then use the completed glyph. Casting also requires an eligible spellbook/form and mana; owning glyph does not itself cast the effect. Recipe and learning controls are in the linked glyph companion.

Enchanting Apparatus recipe `wixie_charm`: shard reagent; Sapling-tag item, Emerald-gem-tag item, Crafting Table and Brewing Stand on pedestals; sourceCost0. This is apparatus crafting, not ordinary2×2 inventory craft. `clear_wixie_charm` shapeless recrafts an existing charm into a fresh one; don't promise preservation of its familiar data.

Use charm on an **empty CauldronBlock** to replace it with Wixie Cauldron; AbstractSummonCharm consumes one on successful survival use. Conversion animation calls create Wixie at tickCounter>=120 and marks converted. A later charm on a converted cauldron without a Wixie can spawn replacement; existing-Wixie branch rejects it. Entity addition is unchecked, so actual worker presence matters. Water/lava/powder-snow cauldron variants are not established by this exact instanceof gate.

## First item recipe, step by step

1. Place an Arcane Pedestal in the cauldron's surrounding3×3×3 block cube (one in each axis), put a sample desired output on it, and leave that pedestal unpowered. The sample is copied for selection, not consumed by this routine. Supply safe ordinary ingredients for a known shaped/shapeless recipe.
2. Place ingredient storage and bind it with Dominion Wand **storage first → cauldron last**. The cauldron last callback toggles that position in boundedInvs if it exposes an item-handler capability with null side. Merely placing an adjacent chest is insufficient: getInventoryCount returns empty when nothing is bound. Repeat binding removes the endpoint; clear wand use clears all bound inventories. This callback has no explicit numeric range check, but pathing/loaded endpoints still limit actual work; do not advertise unlimited logistics.
3. Put Source Jar nearby, within the native SourceUtil range6 search. When hasSource=false, every5tick server boundary attempts a50Source multi-container debit and sets filled/hasSource. This can happen **before a valid recipe or ingredient extraction**, so idle/prepared cauldrons can prepay one craft charge. Source does not debit50 per ingredient. Insufficiency suspends this branch.
4. Every20ticks when previous manager is complete/invalid, rotate across samples on nearby nonpowered pedestals. It chooses a supported recipe that current **bound inventory item counts** can satisfy; sample choices rotate by craftingIndex. No sample, no supported recipe, insufficient ingredients or missing potion storage returns without starting that choice. Hash-set recipe order is not a player-selected deterministic recipe variant.
5. Wixie seeks the next needed Item in linked storage. Near one chosen inventory it can extract needed units from all linked handlers, sending visual flying-item effects and crediting CraftingManager. Selection/accounting is by **Item identity**, not complete ItemStack components; special component-sensitive alternatives need acceptance. The actual extractItem result is not checked for a full unit before giveItem, so unusual handler behavior is another boundary case.
6. Once all inputs are credited, CompleteCraftingGoal approaches cauldron; after40nearby goal ticks calls attemptFinish. Ordinary output and crafting remaining items are spawned **loose above the cauldron**, not inserted back into the bound input chest. Arrange real collection through permitted Ars helpers or manual pickup. Native spawn calls are unchecked.
7. CompleteCraft sets hasSource=false, marks complete and clears fill effect; tile sets craftCooldown1, empties current output state. Next craft needs another50Source. Inventory travel/backoff (including60 after extraction and40 near completion) means this is not50Source per fixed real-time second or a guaranteed40tick total craft.

Right-use cauldron with a desired output item only queries recipes and sends `recipe_set`/`no_recipe` messages in the inspected block method. It does **not** assign the active sample/manager there. Teach pedestal sampling, not reliance on that message alone. Powered pedestal is excluded from sample rotation; powered cauldron sets isOff, checked by item/potion acquisition goals. CompleteCraftingGoal.canUse lacks isOff and tile payment/rotation lacks it too, so powering during a fully supplied craft is not a proven atomic pause/refund.

## What recipes can it perform?

MultiRecipeWrapper supports vanilla **ShapedRecipe and ShapelessRecipe** subclasses for ordinary output item. It scans recipe manager, caches by output Item, expands shaped options through ShapedHelper, and builds ingredient/output wrappers. It does not automatically support every modded machine/altar/smelting recipe just because JEI displays the same item. This base implementation is separate from the Ars Additions Wixie Apparatus and any addon adapter.

For an ordinary ingredient it iterates ingredient.getItems alternatives, picks a present Item, subtracts one count in a temporary map and records its sample stack. Component-aware matching and dynamic recipe assembly are not performed by this item-count layer. Static `getResultItem` is used for output. Treat special-dye/NBT/copied-content or dynamic craft recipes as unverified, not a general claim that all shaped recipes preserve data. Container remainders are calculated from needed sample stacks and emitted at completion, not immediately on extraction. Destroying or interrupting the station mid-craft is not proven to refund all inputs.

## Potion route and jar amounts

Place the **desired ordinary Potion ItemStack** as pedestal sample, with its correct potion components. MultiRecipeWrapper requires Items.POTION and ItemStack.matches against API brewing outputs; splash/lingering or arbitrary modded potion containers are not admitted by that branch automatically. Brewing recipes come from ArsNouveauAPI.getAllPotionRecipes, a separate registry surface from ordinary crafting.

Potion selection needs300units of matching nonwater input in a Potion Jar, plus a compatible output jar with room for300. Jar searches are centered **two below** the cauldron, per-axis extents X/Z4,Y3. Input uses PotionContents equality; output uses canAccept(contents,300). **Water base is implicit**: checks accept it without finding/removing a water jar, and PotionCraftingManager starts water already obtained. Other input is found by FindPotionGoal, worker approaches, removes300 and marks obtained. That debit has no second complete amount/type eligibility check in the goal at arrival, so competing consumers/races need testing.

Potion branch derives needed item alternatives from the brewing ingredient's getItems list, constructing PotionCraftingManager directly rather than ordinary chosen itemsNeeded; it can therefore differ from normal single-alternative accounting. Do not claim arbitrary tag alternatives work identically before testing. Completion deposits300 potion amount in a compatible jar; if none found then, manager sets needsPotionStorage and returns without completeCraft. Earlier ingredient/potion debits are not rolled back there. The enclosing attemptFinish subsequently clears sample/output fields regardless of that manager return, so inspect actual jar amount and resumed manager state rather than visual completion alone. Potion manager output is intentionally empty and overrides invalid-state checks.

Craft state/needed items/remainders, hasSource, craftingIndex and bounded inventory data have serialization methods, but exact bound-inventory write/read representation and interruption behavior have not been runtime validated. Relog/unload must be part of acceptance; do not claim reliable restart from serializer presence.

## Teaching and acceptance matrix

| Lesson | What the proposed quest can honestly detect | Safe reward / minimum check |
| --- | --- | --- |
| Weaken Witch and Dispel | Shard possession proxy; actual replacement requires observed event/manual fallback unless real trigger confirmed | XP, modest milk/ordinary supplies; half-health living positive vs full-health/dead negative |
| Craft charm and convert empty cauldron | Charm/station possession does not prove worker spawned | XP, ordinary building materials; empty vs filled cauldron, existing-worker retry, survival one-charm debit |
| Bind, select and craft | Sample/cauldron acquisition proxy; exact output criterion cannot prove this worker produced it | XP and safe ingredient setup; bound vs adjacent-only chest, unsupported recipe, sample kept, real output+remainder location |
| Brewing | Potion sample/jar proxy, manual300input/output count unless native operation detector established | XP and ordinary brewing supplies; implicit Water vs nonwater, full/mismatched output, missing linked ingredient |
| Reliable control | Manual boundary checklist; no invented FTB network/recipe predicate | Powered sample/cauldron midcraft, consumed-input interruption, handler refusal, reload/unload, two workers competing |

A working Wixie is optional magical automation after basic spell/apparatus/Source setup. It does not replace guide onboarding, personal glyph progression or addon-specific research. Parent remains researching.
