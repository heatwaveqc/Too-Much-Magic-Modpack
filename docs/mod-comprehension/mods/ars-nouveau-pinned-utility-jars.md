# Ars Nouveau pinned Void Jar and Jar of Light

Exact5.13.5 JAR SHA1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; CFR0.152. Evidence: VoidJar/VoidJarData, JarOfLight/LightJarData, ItemScrollData, EventHandler ItemEntityPickupEvent.Pre, ManaCap, Documentation/recipes/en_us. [Parent](ars-nouveau.md), [mana](ars-nouveau-pinned-casting.md), [Apparatus](ars-nouveau-pinned-crafting.md). [Mob Jar capture and24native special behaviors](ars-nouveau-pinned-mob-jars.md) are a separate audited system. No client/lighting/pickup/claim/economy acceptance.

## Two optional utility unlocks

Notebook **Items -> Void Jar / Jar of Light**, JEI corresponding Apparatus recipes. Both center Glass Bottle, output1, SourceCost0, keepNbtOfReagent=false. Void Jar pedestal inputs: Lava Bucket, empty Bucket, Allow Scroll and Ender-pearl-tag item. Jar of Light: four Glowstone, two Redstone Lamps and two Glass-block-tag items. Existing Apparatus recipe/remainder rules apply; no invented bucket return guarantee from item name alone.

Void Jar turns selected **picked-up ItemStacks** into personal mana, irreversibly removing stack. Jar of Light creates a moving world light block. Neither is Source Jar, potion storage, Mob Jar or a general item logistics provider.

## Void Jar configuration and pickup order

1. Hold Void Jar main hand and example item offhand. Ordinary use toggles that item in its stored ItemScrollData list, copying example without consuming it. Alternatively put Jar on Scribes Table and use item to write list. Empty example does nothing. Shift-use main-hand Jar toggles active; default inactive.
2. Matching uses ItemStack.isSameItem, **ignoring components and count**. Selecting a mundane example can match enchanted/named/different-component stacks of same item. This is an item allowlist for destruction, not component-sensitive sorting. Inspect list before activation; no trash restoration branch exists.
3. On ItemEntityPickupEvent.Pre at LOWEST priority, EventHandler calls tryVoiding. ANFakePlayer rejected. It scans player.inventory.items (ordinary main inventory list, **not just hotbar** as native guide says), returns immediately for the **first VoidJar found**, even if inactive/nonmatching. Only if none in main list does it try offhand Jar. Later Jars do not combine their lists or serve as fallbacks in this method.
4. If active and matches, preConsume adds `5*stackCount` mana when mana capability exists, then sets incoming stack count0. Missing mana capability still allows destruction. ManaCap clamps current to max, so excess yield can be wasted; Jar has no overflow buffer. No Source generation, XP or output items.
5. This is pickup-time event mutation, not periodic inventory cleanup. Carrying existing stock does not consume it by inventoryTick. Entire incoming matching stack is zeroed, not only space that could fit in inventory. Event method doesn't itself distinguish naturally dropped versus deliberately dropped items or check final downstream pickup success; effective event order/permissions matter. No loop/renewability approval follows from formula.

Record feedback and actual list state separately: ItemScrollData.Mutable.remove first removes all same-item entries with removeIf, then loops remaining list and normally returns false; contents can therefore change despite false result passed back from onScribe. Tooltip/record inspection is stronger than relying on successful inscription boolean. Ordinary use always saves resulting data after writeWithFeedback.

## Jar of Light world state

Use item to summon/dismiss. On server, if saved position present, removes matching LightBlock there and resets pos empty/enabledfalse. Otherwise attempts placing registered LightBlock at player block position **only if air**, then saves position/enabledtrue even if placement failed. No personal mana/Source/material debit in these methods. Actual brightness, visuals and other lighting integrations need acceptance; no portable full-bright guarantee from name.

Server inventoryTick runs for carried item (not only selected hotbar) when enabled and saved pos present. If saved block is no longer LightBlock, clears stored pos but keeps enabledtrue. If old saved position distance from carrier>7, removes old LightBlock and tries one block behind carrier direction; on failure tries that point+2Y. It records fallback location regardless of second placement success. This is stepped relocation, not light continuously centered every tick. If initial/fallback failure leaves enabledtrue with empty position, tick returns until use establishes position again.

LightJarData stores **BlockPos only**, no dimension. Dimension travel can inspect/remove matching LightBlock at same coordinates in current world rather than safely retiring the old world's light. Block removal also recognizes any LightBlock, without an owner identity in this check; multiple jars/users can interfere. No item-drop/death/offline cleanup is established by inventoryTick/use alone. Test these before teaching persistent public-world lighting. Native placeLight requires air and directly setBlockAndUpdate; effective claims policy isn't proven by this class.

## Walkthrough and acceptance handoff

Optional post-Apparatus choices: craft Jar -> inspect record/settings -> activate -> observe actual pickup mana/item delta, or toggle Light Jar -> walk beyond7 -> observe relocation/dismiss. Notebook Items pages and JEI recipes are references. Item criteria prove acquisition only; mana conversion, destruction and light block movement need manual observation or dedicated detector. Reward XP and modest construction supplies; no random rare-item examples for destructive allowlist lesson.

Unrun boundaries: inactive/matching/nonmatching pickup; same ID with different components; first inactive Jar versus later active/offhand Jar; full mana, missing capability and full inventory; deliberately dropped stacks and actual event ordering. Light: clear/blocked summon point, both relocation locations blocked, externally removed light, another user's LightBlock, same coordinates across dimensions, item drop/death/reload and server cleanup. Count item loss, mana cap and actual world block state; no in-world PASS, Source compatibility or approved resource bypass.
