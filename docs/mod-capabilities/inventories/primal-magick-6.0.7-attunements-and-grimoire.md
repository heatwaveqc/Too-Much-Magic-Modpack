# Primal Magick 6.0.7 — attunement bonuses and Grimoire navigation contract

**Evidence:** immutable upstream [6.0.7 SHA `5977468bda968b994e54066236ec5719295ac9c3`](https://github.com/daedalus4096/PrimalMagick/tree/5977468bda968b994e54066236ec5719295ac9c3). Exact research, player events, attribute registration, and client UI classes were read; **no actual client screen, published JAR, assembled TMM datapack or gameplay QA was executed**. The goal is a **truthful player walkthrough** and per-player progression/claim-safe quest expectations.

## A. Real Attunement values and thresholds

[`AttunementType.java`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/attunements/AttunementType.java), [`AttunementThreshold.java`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/attunements/AttunementThreshold.java), [`AttunementManager.java`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/attunements/AttunementManager.java):

| Attunement component | Native semantics | Cap / decay |
| --- | --- | --- |
| **Permanent** | From research; not removed by temporary decay | No positive cap in the enum |
| **Induced** | Added/removed via Ritual or other specified sources; persists without natural time decay | At most **50** of each Source |
| **Temporary** | From spells/crafting and Mana activity, then decays | At most **50** of each Source; **−1 every 1,200 player ticks** (about 60 seconds at 20 TPS) |
| **Total** | Sum of Permanent + Induced + Temporary for each of nine Sources, with suppressed state respected for actual bonus activation | Minor **30**, Lesser **60**, Greater **90** |

The thresholds apply to **actual summed raw attunement**. `AttunementManager.meetsThreshold` first checks source suppression; a suppressed Source does not grant the passive even if its raw total is high. Player-specific `ATTUNEMENT_THRESHOLD` native advancement criterion includes values of Permanent, Induced and Temporary, so a future Progression/FTB adapter should **not** infer permanent research from temporary points. [`ManaManager.recordManaConsumptionSideEffects`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/wands/ManaManager.java) increments **Temporary** attunement by `floor(sqrt(floor(pre-discount Mana spent /100 centimana)))` per source consumption event. No TMM MXP credit is automatic.

## B. Exact registered and player-event perks

The [`InitAttunements.java`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/init/InitAttunements.java) **attribute registration** includes:

| Source | Threshold | Source-defined passive |
| --- | --- | --- |
| Earth | Lesser 60 | **+20% attack speed** (`ADD_MULTIPLIED_TOTAL 0.2`) |
| Sky | Lesser 60 | **+20% movement speed** (`ADD_MULTIPLIED_TOTAL 0.2`) |
| Sea | Lesser 60 | **+60% swim speed** (`ADD_MULTIPLIED_TOTAL 0.6`) |

In [`PlayerEvents.java`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/events/PlayerEvents.java) are additional native player ticks and actions:

| Source | Threshold | Source-backed player event behavior | Tick or environmental condition |
| --- | --- | --- | --- |
| Earth | Greater 90 | **+0.4 Step Height** transient attribute modifier | Removed while sneaking and re-added while not sneaking |
| Sea | Greater 90 | **Water Breathing** effect 610 ticks (30.5 seconds) | Buff tick reapplied every 5 ticks while threshold met |
| Moon | Greater 90 | **Night Vision** effect 610 ticks | Buff tick reapplied every 5 ticks while threshold met |
| Sky | Greater 90 | Player **double jump** with upward motion 0.75 plus Jump Boost modifier | Client keyboard code; in air, not in water; jump gating and reset need actual client verification |
| Sun | Lesser 60 | **Photosynthesis** restores 1 food point with 0.3 saturation modifier | Server check every **200 ticks**; daytime, skylight, high light |
| Sun | Greater 90 | May place temporary fading **Glow Field** block in a dim location | Every **20 ticks**, **10% random check**; not sneaking, target empty and block light below 11 |

These are *not* the complete effect of every Source: Blood, Infernal, Void, Hallowed may grant discounts or additional source-specific effects from other classes. In particular, the table does **not** assert that they have no bonuses just because `InitAttunements` registers only three Attributes. The source has native death-save and ward mechanics too, with separate item/cooldown gates. The source does not prove stacking with all TMM Paths.

**Important pacing:** temporary attunement decreases by **one point once every 1,200 server player ticks**, not one per in-game day or every second. FTB can teach a temporary Greater effect, but should not mark it as irrevocably unlocked.

## C. Actual Grimoire player journey and visible navigation

[`GrimoireItem.java`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/items/misc/GrimoireItem.java) `use` acts **on the server**, records `StatsPM.GRIMOIRE_READ`, sends [`OpenGrimoireScreenPacket`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/network/packets/misc/OpenGrimoireScreenPacket.java) to the player, and then the client opens [`GrimoireScreen.java`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/client/gui/GrimoireScreen.java). The normal item **`primalmagick:grimoire`** registers with `new GrimoireItem(false)`.

The Grimoire is a **research-controlled, stage-aware index**, not a complete static recipe compendium unlocked from the beginning:

1. **Main index:** [`DisciplineIndexPage.java`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/client/gui/grimoire/DisciplineIndexPage.java) lists only disciplines that pass the player's `unlockRequirement`, while [`OtherIndexPage.java`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/client/gui/grimoire/OtherIndexPage.java) provides *Other* tabs.
2. **Discipline pages:** display entries according to each entry's player state (new, updated, complete, in progress, available or upcoming) and reveal individual `StagePage` entries. Where an unknown stage has unmet requirements, the client creates a [`RequirementsPage`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/client/gui/grimoire/RequirementsPage.java) rather than silently granting missing Research.
3. **Recipe pages:** stage recipes plus unlocked research addenda resolve through the active `RecipeManager`, then [`RecipePageFactory.java`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/client/gui/grimoire/RecipePageFactory.java) maps them to Shaped/Shapeless Arcane, Ritual, Runecarving, Concocting, Dissolution, smelting and other supported UI pages. Therefore a source recipe builder alone **does not prove recipe visibility**.
4. **Other → Recipe Index:** available once `FIRST_STEPS` is in progress or complete, with a **search text box**; its index filters to player-known or [`ResearchManager.isRecipeVisible`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/research/ResearchManager.java) stage-eligible recipes. Search terms are client-side names and visible results; not every source object is necessarily searchable immediately.
5. **Other → Attunements:** visible only after `ATTUNEMENTS` is complete. **Other → Linguistics** only after `LINGUISTICS`; **Other → Rune Enchantments** only after `UNLOCK_RUNE_ENCHANTMENTS`; **Other → Scans** only when Scans' own unlock requirement is met. **Statistics** and **Tips** links are native other-index functionality.

**Recommended FTB first-play messaging:** teach **"Open your Grimoire → Basics → current First Steps page → Requirements"**, not "Find every recipe in JEI". Once the user sees the Research Table, teach **"Other → Recipe Index → search for your already-unlocked output"** and when they reach Source Attunement teach **"Other → Attunements"**. These are source-backed navigation controls, but exact in-client font, strings, translations, pixel positions and actual 6.0.7 UI functioning are still QA.

## D. Material creative-tier bypass hazard — not evidence of actual survival access

[`ItemsPM.java`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/items/ItemsPM.java) registers **`primalmagick:grimoire_creative`** using `new GrimoireItem(true)`. Critically, [`GrimoireItem.use`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/items/misc/GrimoireItem.java) invokes **`ResearchManager.forceGrantAll(player)`** **on right-click** for that creative variant. This isn't merely a cosmetic guidebook: **if obtainable through EMC learning, loot, shop, creative-class craft or cross-mod transfer**, it bypasses the entire native Primal research gate. No proof of actual obtainable recipe/EMC value was found in this static source reading; **do not declare a current exploit**. It belongs in pack-wide [creative-tier exclusion #596](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/596), together with `primalmagick:mana_singularity_creative` and infinite Mana `creative` Gem/Orb. Keep normal `primalmagick:grimoire` obtainable.

## E. What is still untested

The menu itself must be inspected with a real Primal Magick **6.0.7** client and a new non-creative player, including FIRST_STEPS visibility, Native Research/Grimoire stage progression, locked recipe search behavior, all seven main disciplines, the separate Scans unlock, and the no-creative-item pathways. Client-side Sky Greater double jump and all three Attunement pool changes remain in the B/C QA queue. No actual game/world launch was performed in this research-only pass.
