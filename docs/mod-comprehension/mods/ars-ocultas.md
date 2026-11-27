# Ars Ocultas — pinned bridge walkthrough

| Field | Value |
| --- | --- |
| Manifest / mod ID | `ars-ocultas` / `ars_ocultas` |
| Artifact | `ars_ocultas-1.21.1-2.6.2.jar`, 82,755 bytes |
| Full SHA512 | `4a673096a2d0b9a5a83aca2747a8a08211205ed40875b4c22061f774146d9d4fff9c872e2c1b6b59e5fef2d1bf3b3a08732f22743ade4e0a82ba08636dc701a5` |
| Status | **source-complete for pinned installed scope, with known native limitations**; no client/world acceptance |
| Parents | [Ars Nouveau](ars-nouveau.md) 5.13.5 and [Occultism](occultism.md) 1.227.0 |
| Pin reconciliation | Fetched integration `ce771cae0dc9853076bd468677267f7f88c58ae4`; all three relevant pins unchanged, exact local hashes rechecked |
| Scope | Four spirit entity tiers sharing jar behavior, two material-conversion recipes, alternate Spirit Attuned Gem, Sacrificial Altar and Soul Gem transfer; no separately registered glyph/familiar tier ladder |

Ocultas combines native Occultism workers with Ars Containment Jars, exposes their item handlers, forwards selected job outputs to adjacent inventories and offers Source-powered ritual condition signaling. Its practical endpoint is a legitimate worker performing a known job in a jar, with optional material conversion, container transfer and Altar branches. It doesn't unlock a Marid simply from owning a jar, supply generic pipes, or certify a reusable-sacrifice shortcut as balanced.

## Guide and prerequisites

Craft Worn Notebook from Book plus lapis-gem-tag item and right-click. Native **Ars Ocultas** category registers **Gem**, **Spirit Jars** and **Transmutation** entries through Java documentation events; it is not a separate required Patchouli guide item. See [Ars guide/glyph onboarding](ars-nouveau-pinned-guide-glyphs.md). Obtain/open the Occultism Dictionary and follow its actual spirit tiers, matching bound book, pentacle and worker ritual as documented in the parent profile. The addon has no dedicated Altar page in this registration, so future quest text must supply local steps below and point to the named parent ritual and JEI recipe.

Build native [Imbuement Chamber/Apparatus](ars-nouveau-pinned-crafting.md), acquire a [Containment Jar and legal capture setup](ars-nouveau-pinned-mob-jars.md), and prepare the chosen native worker. Conjuration/Manipulation Essences, Demon’s Dream Essence and Source follow their parent acquisition routes. No outside wiki is a necessary player step; rendered guide navigation and current effective recipes still require later acceptance.

## Full player route and optional branches

1. **Choose a useful worker.** In the Dictionary select a native Foliot worker first, then optionally Djinni/Afrit/Marid as their actual summoning gates become available. A representative pinned `occultism:ritual/summon_foliot_crusher` uses bound Foliot book, summon_foliot pentacle, iron/gold/copper/silver ingot-tag units, declared duration60 and job `occultism:crush_tier1`. Cleaner recipe uses bound Foliot book with Brush, chest-tag, Dispenser and Hopper on the same pentacle. Follow the complete parent ritual preview/activation rather than treating those ingredients as a shaped crafting grid. Obtain a real job-bearing spirit with ownership/state; the addon registers behavior for all four entity tiers but creates no new job itself.
2. **Obtain a compatible container.** Use native Containment Jar route and actual entity restrictions, or supported Soul Gem exchange below. Do not assume every valid sacrifice or boss is capturable. Jar capture cost/ritual limitations belong to the native Ars companion; the addon doesn't erase them.
3. **Configure and feed.** Secondary-use empty hand on spirit jar opens its native job/spirit menu. Set appropriate filter where that job supports it, then try one eligible input. Manual use checks job pickup ingredients; dropped items use job.canPickupItem and a40tick scan; exposed item capability is another channel. A GUI blacklist isn't proof all channels enforce the same predicate. Expected success is an actual processed output or demonstrated local collection, not a jar item in inventory.
4. **Collect output.** Place a supported adjacent inventory and observe insertion plus remainder. Crusher/Smelter/Crystallizer/Trader outputs pass through job events; addon replaces result with leftover after adjacent insertion, native job handles remainder. Cleaner exports its carried mainhand stack before job.update. Full destinations, the native special extractor path and failed spawns can affect delivery after input was already consumed. Stationary job.update is not full entity AI: no promise pathfinding-dependent jobs walk a work area while contained.
5. **Optional material alternative.** Make Spirit Attuned Gem in Apparatus: Source Gem reagent, Conjuration Essence + Manipulation Essence + Demon’s Dream Essence pedestal units,500Source. This is another route to native Occultism item, not proof it was crafted by magic if another recipe/trade/reward supplied it. Consult **Gem** and JEI `ars_ocultas:spirit_attuned_gem`.
6. **Optional transmutation.** In Imbuement Chamber convert one Silver Ingot→one Gold with nearby Manipulation Essence, Magebloom and Spirit Attuned Gem catalysts,2000Source; reverse uses one Gold→one Silver with Manipulation Essence, Demon’s Dream Essence and gem. **Only primary ingot is consumed; nearby catalysts are reusable**. See Transmutation entries and JEI `gold_transmute`/`silver_transmute`. This is not a recipe consuming all displayed catalysts each cycle, nor a1→many metal loop. Native chamber Source cadence/refund rules remain.
7. **Optional Soul Gem exchange.** With config enabled, use empty supported Soul Gem on filled jar to save entity NBT into gem and clear jar; use filled gem on empty jar to place it. Respect native gem-specific deny tags and jar tags. Pinned placement removes stored gem data before validation, and one blacklist branch doesn't return; denied/failed operations may lose data or proceed unexpectedly. Fragile placement clears mainhand even if event was another hand. Prefer controlled owned fixtures and check both containers; this is a known failure boundary, not a guaranteed safe transport instruction. No new personal mana or Source fee appears in this exchange handler.
8. **Optional Sacrificial Altar.** Apparatus consumes a Golden Sacrificial Bowl reagent plus4Silver Ingots, Drygmy Shard,2Sourcestone and a Mob Jar,5000Source. Obtain a second Golden Bowl for use: put it **directly above** Altar and construct/start a valid parent Occultism ritual. Put an eligible living sacrifice in a nearby jar and/or matching item in Altar's27slots. Every global300tick boundary, addon may request5000Source per unfulfilled condition and directly signal bowl flags. It doesn't consume matching entity/item. Other parent ritual gates still apply; do not present this as ordinary sacrifice or assume all progression skips are approved. Explicit local explanation is required because no Altar guide entry is registered.

There is no addon terminal boss or hidden required extra tier after these selected options. Higher spirit tiers and storage advancement remain native Occultism progression. Source completeness includes these known-broken/conditional routes and their usable alternatives; it doesn't promise every optional path behaves correctly in a live pack.

## Native Tier4 storage versus legacy addon file

The addon contains four current singular `data/ars_ocultas/recipe` resources and one **plural legacy** `data/ars_ocultas/recipes/craft_stabilizer_tier4.json`. Its old duration240/forge Iesnium tag/End materials are not evidence an active1.21.1 override loads. Do not make that unproven file a mandatory addon quest.

Exact parent1.227.0 current `data/occultism/recipe/ritual/craft_stabilizer_tier4.json` exists: bound Marid book activation, `occultism:craft_marid` pentacle, declared duration510, Tier3 stabilizer + `c:storage_blocks/iesnium` + Beacon +2Spirit Attuned Crystals +Marid Essence, one Tier4 stabilizer output. Dark variant parallels it with dark Tier3/4. Use Dictionary **Storage** and actual JEI/native recipe, [ritual build references](occultism-pinned-rituals.md) and [storage companion](occultism-pinned-storage-harvest.md). This closes a real native material/tier route without pretending the addon legacy resource is active or granting the tier for free. Duration is resource value, not measured wall-clock throughput.

## Quest coverage and detection honesty

| Pilot / stable ID | Actual criterion and rewards | Teaching gap |
| --- | --- | --- |
|40 ocultas_gem /544DB13000280000|Spirit Attuned Gem×1;40XP+4Purple Candles|Possession permits other acquisition; teach exact alternate Apparatus/Source route and native use.|
|41 ocultas_jar /544DB13000290000|Mob Jar×1;55XP+2Hoppers|Empty hardware can satisfy it. Doesn't prove contained spirit/job/tier, configured filter or processing.|
|42 ocultas_altar /544DB130002A0000|Altar×1;80XP+4Crying Obsidian|Doesn't prove bowl placement, Source request, fulfilled flags or retained matching items.|
|Optional remaining branches|No dedicated existing criterion|Worker operation, transfers, transmutation and native Tier4 storage need actual-operation detector or clearly labeled output/manual proxy.|

[All49pilot crosswalk](ars-nouveau-pilot-crosswalk.md) reconciles source files. Native guide, JEI and above steps support future rewarded authoring; useful fixed common ingredients/XP suit optional lessons, not gifts of higher-tier spirits/unique sacrifice objects or repeatable conversion payouts. Existing pilot is source-present; PR closure/source presence doesn't mean client/team claims accepted. No quest changes here.

## Subsystem source coverage

| Companion | Completed source path |
| --- | --- |
|[Guide/recipes/Altar](ars-ocultas-pinned-altar-recipes.md)|Native registration, four current recipes/legacy distinction, consumed/reusable ingredients, exact vertical/cadence/Source/flags, native bowl recipient and container/metadata boundaries.|
|[Spirits/outputs/gems](ars-ocultas-pinned-spirit-transfer.md)|Four entity registrations, three input channels, native age/job distinction, all four result event consumers/Cleaner export, GUI/filter and serialized gem transfer failure order.|

Known limits: jar direct job.update bypasses normal aiStep age advancement but not every possible job timer; dropped scan only tries first eligible candidate; capability/manual/drop predicates differ; GUI filter packet doesn't enforce menu/distance/owner in that handler; output isn't universally atomic. Altar reusable sacrifice/item flags and unchecked extraction after precheck need balance/permission decisions later. Known failures are not unacknowledged source gaps.

## Identity, compatibility and validation limits

Exact JAR metadata still declares MC`[1.21,1.21.1)`, Ars`[5.3,6)` and Occultism`[1.220.4,)`. The MC range discrepancy remains unresolved in this task; previous user correction authorized continued research based on identical artifact used by another1.21.1pack. This is **not** a newly verified loader pass, blocker requiring a repeat user decision, metadata fix or permission to change pins. Earlier external upstream pack claims are historical evidence, not checked again here. Native current behaviors are documented even while effective loader compatibility remains acceptance work.

Direct addon46classes/allinners and selected parent1.227.0 bowl/recipe/API/spirit/fivejobs/inner classes inspected from hash-verified artifacts, outsideGit. Prior version-aligned [upstream14aa0f4](https://github.com/dphaldes/Ars-Ocultas/tree/14aa0f4a05f8e85aca03b9bbe084e2bff36b007b) is secondary; no reproduced binary build. Fetchedintegrationce771cae0dc9853076bd468677267f7f88c58ae4 pins unchanged; each relevant local artifact hash recomputed.

UTF8/local-link/docs-only/status-sync/whitespace and actual3pilot criterion/reward checks performed. No assembly, server/client/gameplay, effective-config receipt, runtime/config/quest/pin change or merge. Later disposable-world acceptance: loader/guide, all chosen native worker tiers and input/output boundaries, capture/release/reload/age/state, both gem directions/denied/fragile/offhand, Altar conditions/Source cadence/GUI limits/flags, transmutation catalysts/net resources, native-vs-legacy recipe availability, multiplayer ownership/claims and quest credit. Reopen source scope if pins/recipes/jobs/config or enabled endpoints change.
