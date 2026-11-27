# Ars Nouveau pinned Starbuncle item transport

Exact `ars_nouveau-1.21.1-5.13.5.jar`, SHA-1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; direct JSON/CFR0.152. Evidence: `Starbuncle`, `StarbuncleCharm`, `StarbyTransportBehavior`, `StarbyListBehavior`, all transport state classes, three ItemScroll subclasses and ItemScrollData. [Parent](ars-nouveau.md), [Dominion Wand](ars-nouveau-pinned-source.md). This covers ordinary item transport/berries plus potion-jar mode, road/bed/passenger controls and filter matching. Familiar role/effective pack acceptance remain separate. No in-pack transport rate or compatibility test.

## Befriend and assign a first route

Use native Notebook **Crafting → Starbuncle Charm**, Dominion Wand, named Allow/Deny/Mimic Scroll entries and relevant related pages. Drop one gold-nugget-tag item near a wild Starbuncle and let it pick it up, **or main-hand interact with it holding a nugget** while it is untamed and its held stack is empty. Merely standing with a nugget without interaction is not the action. pickUpItem and mobInteract each split one survival nugget; creative interaction copies one. While holding it, attemptTame increments; after>60 ticks emits1..2 Starbuncle Shards and discards original. Special adopter-data case forces one shard and stores that data; no universal two-shard promise.

Craft charm through the exact apparatus recipe below, then use on a block face to spawn the tamed worker in the adjacent block position. AbstractSummonCharm consumes one on successful survival use; spawn result unchecked. Do not assume empty safe space/path from successful interaction. This charm is distinct from a familiar-binding item.

Configure **worker first → destination inventory last** to add a store endpoint; **source inventory first → worker last** adds a take endpoint. Dominion Wand selects entity/block then dispatches callbacks. Capability must exist on the selected side (Normal uses null; Strict supplies side). Multiple endpoints are supported; no numeric link limit/range check in these callbacks, but loaded chunks, pathing and actual inventory acceptance still constrain work. Clear connections with the wand clear interaction; transport clears both source/destination lists and direction maps.

Start with two ordinary chests on accessible ground, one source item and a compatible destination. Observe actual source decrement, carried stack and destination increment. For loose-drop collection, only a destination is required. A worker without a valid accepting destination will not pick up arbitrary drops or extract items. No Source debit in the inspected ordinary transport state path; Source infrastructure is not a mandatory fuel for this first route.

## Actual work and failure order

| Operation | Exact action and boundary |
| --- | --- |
| Select destination | Iterates TO_LIST, loaded capability and simulated insertion acceptance. Picks highest SortPref; first HIGHEST returns immediately. Equal preference retains earlier endpoint. Filtering priority is not nearest-distance sorting or guaranteed balancing. |
| Select source | First FROM_LIST position whose sided handler can simulate extracting1 item with a valid destination. Requires loaded position. At arrival may extract up to min(destination estimated room, source item's max stack size), one held stack per worker/passenger. |
| Deposit | Travels to target, rechecks sort validity, uses ItemHandlerHelper.insertItemStacked and **keeps returned remainder** as held stack. Rejection causes5..24tick backoff. Unlike ignored Whirlisprig remainder, ordinary Starbuncle delivery does not discard remainder at this call. |
| Loose item discovery | Cached worker-centered box inflate8, recalculated after>=60ticks; only alive ItemEntity without pickup delay and with accepting store endpoint. Reachability path filter, shuffled candidate list; no path gives30..59tick backoff. Travel can stop after300ticks or>100stuck checks. |
| Loose pickup | Requires valid store and pickup enabled; transfers entity stack into available worker/passenger slot and discards ItemEntity. Nearby same-item/components stacks within inflate3 can merge until stack limit; actual destination acceptance can change after pickup. |
| Passenger carrying | getStarbuncleWithSpace checks self then indirect Starbuncle passengers with empty held stack. Take/deposit can process passengers; this is not arbitrary inventory capacity or a fixed items-per-second formula. Passenger setup controls remain a separate entity-interaction audit. |
| Shades | Starbuncle Shades cosmetic sets isPickupDisabled. Stops loose-item/berry selection; source→destination transport is evaluated before this gate and remains possible. Do not teach Shades as a total work stop. |
| Idle / bed priority | A configured powered valid sleepable bed is checked before deposit, otherwise idle can choose bed after transport attempts. Full stop/recovery semantics need bed-state follow-through. No ordinary chest redstone-disable check in these item route callbacks. |

Important exact-code distinction: getMaxTake looks up destination capability using **FROM_DIRECTION_MAP**, while actual destination eligibility/deposit uses TO_DIRECTION_MAP. Strict-side destination-only handlers may pass destination checks but fail room estimation or extraction planning. Test sided inventories rather than promising generic side-only machine automation. Position/side storage is keyed by BlockPos.hashCode; rebinding same position with a different side can append another list entry. Do not infer deduplicated balanced rates from endpoint count alone.

## Filters, item frames and berries

- An ordinary Item Frame attached to the destination containing a sample item accepts matching **Item identity** at HIGHEST; a different sample rejects. Attached ItemScroll applies its own preference; multiple attached frames can interact through iteration, so use one clear filter per first setup.
- Allow Scroll: listed stack→HIGHEST, otherwise INVALID. Deny Scroll: listed→INVALID, otherwise LOW. Mimic Scroll: HIGHEST if destination already contains same Item, otherwise INVALID. Mimic doesn't consume/move its template item.
- Giving an ItemScroll to worker copies scroll into behavior; its INVALID result vetoes a destination even when that chest frame accepts. It is a global restriction on this worker's route. ItemScroll main-hand use with an offhand sample invokes writeWithFeedback and updates data; ItemScrollData.contains compares Item identity, ignoring components/count. Reusing an already-listed sample removes that Item from the list; a new sample adds a copy. The sample is not shrunk by this writer. This is an item-list toggle, not a confirmed tag/component editor.
- Sourceberry harvest: searches per-axis10,3,10 around worker for SourceBerryBush AGE>1, requires empty held stack and a valid berry destination. Arrival drops1..2 berries plus1 if mature age3, resets age1, then attempts pickup nearby. This is Sourceberry-specific, not an automatic all-crop harvester. Shades disables it. Sourceberry items here are crop output, not Source energy.

Dispel of living tamed worker calls dropData: charm with its StarbuncleCharmData plus current held stack spawned at position, then discard worker. Native spawn results unchecked. Exact cosmetics/behavior recovery and passengers need acceptance; do not promise atomic refund of every associated object. Worker death/drop handling is separate from Dispel.

## Exact recipe references

Rows read directly from `data/ars_nouveau/recipe/<basename>.json`; use JEI's matching crafting/apparatus category. No extra infrastructure or generic logistics mod is required by this research.

| Recipe | Exact payload |
| --- | --- |
| `starbuncle_charm` | `{"type":"ars_nouveau:enchanting_apparatus","keepNbtOfReagent":true,"pedestalItems":[{"tag":"c:ingots/gold"},{"tag":"c:ingots/gold"},{"tag":"c:ingots/gold"},{"tag":"c:ingots/gold"}],"reagent":{"item":"ars_nouveau:starbuncle_shards"},"result":{"count":1,"id":"ars_nouveau:starbuncle_charm"},"sourceCost":0}` |
| `deny_scroll` | `{"type":"minecraft:crafting_shapeless","category":"misc","ingredients":[{"item":"ars_nouveau:blank_parchment"},{"tag":"c:cobblestones"}],"result":{"count":1,"id":"ars_nouveau:deny_scroll"}}` |
| `allow_scroll` | `{"type":"minecraft:crafting_shapeless","category":"misc","ingredients":[{"item":"ars_nouveau:blank_parchment"},{"tag":"c:chests"}],"result":{"count":1,"id":"ars_nouveau:allow_scroll"}}` |
| `mimic_scroll` | `{"type":"minecraft:crafting_shapeless","category":"misc","ingredients":[{"item":"ars_nouveau:allow_scroll"},{"item":"minecraft:chest"}],"result":{"count":1,"id":"ars_nouveau:mimic_scroll"}}` |
| `starbuncle_shades` | `{"type":"minecraft:crafting_shaped","category":"misc","key":{"x":{"item":"minecraft:tinted_glass"},"y":{"item":"ars_nouveau:source_gem"}},"pattern":["xyx"],"result":{"count":1,"id":"ars_nouveau:starbuncle_shades"}}` |
| `wixie_hat` | `{"type":"minecraft:crafting_shaped","category":"misc","key":{"x":{"item":"ars_nouveau:magebloom_fiber"},"y":{"tag":"c:ingots/gold"}},"pattern":["xxx","xyx","xxx"],"result":{"count":1,"id":"ars_nouveau:wixie_hat"}}` |

## Potion jars, roads, beds and passenger controls

Exact `WixieHat`, `StarbyPotionBehavior`, `PotionTakeGoal`, `PotionStoreGoal`, `GoToBedGoal`/`GoToBedState`, `Starbuncle.mobInteract`/`onWanded`, and ItemScrollData. These are native optional worker branches, not the Wixie cauldron's brewing recipe implementation.

**Wixie Hat mode:** interact with a Starbuncle using Wixie Hat; sets a **fresh** StarbyPotionBehavior before cosmetic fitting, then equips one split hat on wearable target. This changes the work mode rather than augmenting ordinary item routing. Configure jar source first→worker last and worker first→destination jar last. Endpoints must be PotionJarTile. First valid source has>=100units and a destination canAccept(same contents,100). At pickup, worker takes min(source amount, destination remaining room,300), saving potion contents and numeric amount on itself. Thus100 is eligibility threshold and300 maximum trip load; neither is a bottle count.

At delivery it computes diff=min(current room,held amount), calls jar.add(contents,diff), **clears heldPotion to EMPTY unconditionally**, then subtracts diff from numeric amount. If destination room shrinks below load between pickup and arrival, residual numeric amount can remain without its potion identity; it is not a guaranteed preserved retry. Positive plus competing-consumer/full/mismatched/reload tests are required before treating this as reliable potion conservation. Potion take refuses while bed powered; potion store has no identical powered-bed check. Saved behavior tag contains contents/amount, but mode replacement starts a new behavior and is not a promise to refund an in-flight load. No Source fuel debit in these inspected transport goals.

**Road preference:** main-hand interact with a BlockItem sets worker's pathBlock to that block, without shrinking it in this branch. isOnRoad accepts DirtPathBlock or selected custom block; path options use onPathCost0.1. This establishes path preference, not a measured speed multiplier. Dye interaction changes recognized color and consumes one dye in survival if actually changed; color is not a production tier.

**Passenger stack:** interact with a Starbuncle Charm on an existing worker; follows first-Starbuncle-passenger chain to top, rejects if top has any other passenger, then spawns another tamed worker, restores charm data and attempts startRiding, consuming charm in survival. Spawn/mount return unchecked; no numeric cap or safe unlimited chain claim. Each actual worker/passenger can hold an item stack, but route/path/capacity checks still apply.

**Bed:** worker first→sleepable-tag block last sets bedPos through first callback. Loaded valid sleepable block and POWERED property true gives powered-bed state. Transport decision prioritizes powered bed before depositing, which can suspend a carried stack. Normal idle chooses bed later. GoToBed state/goal path toward bed, expires after300ticks and reevaluates validity/arrival on10tick boundaries; it is not a teleporting storage or item refund. Potion take refuses new pickup while powered but can still have held potion/store work pending.

**Clear and remove accessories:** StarbyListBehavior.clearOrRemove returns true only when source/destination lists are already empty. onWanded captures that result, clears lists via behavior, also clears pathBlock/bedPos; if cosmetic exists and the pre-clear result was true, drops cosmetic and resets nontransport mode to a fresh transport behavior. Consequently a connected equipped worker may need one clear to remove routes and a second clear to remove accessory/revert mode. Clear is not a proven potion/item refund transaction. Keep hands/workload empty first.

## Acceptance and remaining parent work

Teach nugget interaction/pickup → shard/charm → spawned worker → destination/source wand order → physical item movement → optional filter, berry and potion collection. Charm/items are acquisition proxies; animation is not proof of final insertion. Proposed rewards are XP and modest ordinary container/route supplies, not protected loot or an extra independent generic transport system.

Positive: source chest→destination plus loose drop and mature Sourceberry; one compatible potion-jar trip; selected path block and bed; one real passenger. Boundaries: full/unloaded/filtered destination, pickup delay, obstructed path, Strict side mismatch, changed filters while carrying, Shades retaining chest transport, identical/equal-priority endpoints, powered bed while loaded, potion destination shrinks after pickup, fresh behavior change, two-step accessory clear, Dispel/passengers and reload/chunk unload. Count actual item/potion conservation separately from animation/advancement.

Native worker routes now have source coverage; familiar abilities, equipment/device branches, effective pack tags/handler compatibility and full parent/addon walkthrough remain open. Parent remains researching.
