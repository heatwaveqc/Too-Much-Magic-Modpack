# Ars Nouveau pinned Warp Scrolls and portals

Exact `ars_nouveau-1.21.1-5.13.5.jar`, SHA-1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; JSON/CFR0.152. Evidence: WarpScroll/StableWarpScroll/WarpScrollData, PortalBlock/Size/PortalTile, PortalFrameTester/FlatPortalAreaHelper, BuildPortalEvent, ServerConfig, native recipes/frame tag. [Parent](ars-nouveau.md), [Warping ritual distinction](ars-nouveau-pinned-rituals.md), [Source](ars-nouveau-pinned-source.md). No actual dimension, landing, passenger or portal/protection acceptance.

## Record a destination and use a consumable scroll

Native Notebook **Crafting → Warp Portal** (`warp_portal`), **Items/Equipment → Stable Warp Scroll**; native pages cross-link Dominion Wand and Source Jar. JEI scroll recipes and named apparatus recipe provide ingredients. Start with an ordinary accessible same-world destination and safe landing space; native coordinates are a destination, not an automated safe-site search or permission to bypass pack dimension gates.

1. Blank Parchment: Paper at center surrounded by eight Magebloom Fiber (ordinary shaped). Warp Scroll: Blank Parchment+four Lapis-gem-tag items+four Source-gem-tag items, shapeless, one scroll.
2. Main-hand Shift-use an **unrecorded** ordinary scroll while standing at chosen destination. Saves blockPosition, dimension location string and rotation. One-stack case returns a recorded replacement; multi-stack case inserts a recorded scroll into inventory then shrinks one only on successful insertion (survival). Full inventory failure preserves unrecorded stack in this branch. No Source/XP charge to record.
3. At another location, use recorded ordinary scroll main hand. It first checks data valid and dimension equality unless crossDim flag allows; then teleports player in **current world** to X/Z+0.5 at savedY, applies saved rotation, consumes one survival scroll. Direct method isn't DimensionTransition. A recorded scroll Shift-use enters this valid-data branch too; **Shift doesn't overwrite it**, it can teleport/spend it. Offhand use doesn't teleport.
4. `reset_warp_scroll` shapeless recrafts scroll into fresh unrecorded one. Keep coordinate backup if needed; do not call reset a reusable saved waypoint. Stored crossDim flag alone is not proof direct ordinary-scroll travel changes dimensions.

## Permanent framed portal route

Build a vertical ring of blocks in `ars_nouveau:an_decorative`; native tag has71 entries including **Sourcestone**. A straightforward teaching build is full4wide×5high outer ring (14Sourcestone blocks), leaving2wide×3high empty interior. Drop a recorded scroll **inside the interior**, with9000Source available within native SourceUtil range10 search. This is dropped ItemEntity action, not holding/useOn the frame.

Native vertical Size validator accepts interior width/height1..21 and decorative bottom/sides/top, with air/portal/fire/fluid-replaceable interior. The Nether-style downward search is bounded21 and stops atY0, so don't assume modern negative-Y builds work identically. Corners/odd small bounds and effective tags require acceptance; the full ring avoids relying on permissive edge cases. A horizontal rectangular ring is also supported through FlatPortalAreaHelper, interior X/Z>=1 and **<21**; do not carry vertical inclusive21limit over to it. Portal updates revalidate frame and can become air when invalid.

Dropped WarpScroll.onEntityItemUpdate checks valid data, current dimension permitted, available9000Source, **tries spawning portal blocks**, then actually debits9000 and finally shrinks one scroll after successful payment. Consequently preflight availability is not final transaction: concurrent drain can leave placed portal with failed final payment/no scroll debit in that attempt. Portal creation is before payment success in this chain. Observe actual frame/content/debit; animation/nearby advancement alone isn't an atomic completion receipt.

`enableWarpPortals` native server defaulttrue gates PortalBlock.trySpawnPortal. That helper tries vertical first then horizontal. Existing effective pack config must be checked before teaching access. Display name from custom-named dropped scroll can be saved on resulting portal. Source is a **creation** cost here; no per-traveler Source debit in inspected PortalTile warp/tick methods. Ordinary framed route still consumes one recorded scroll at successful creation.

Portal is directed to recorded coordinates, not automatically paired to a return portal. Build/record a separate return route and place landing points outside portal blocks. No cross-dimensional destination is established by a standard same-dimension-only scroll; use actual Stable route for that feature.

## Stable scroll and temporary built portal

Apparatus `stable_warp_scroll`: Warp Scroll reagent, four Blaze Powder and two Ender-pearl-tag items, SourceCost0, keepNbtOfReagent=true. Record before upgrading if desired; inventoryTick ensures stable component crossDim=true even when copied source flag wasfalse. Stable Shift-use unrecorded main hand records; Shift-use recorded reports already recorded. Its direct use method doesn't perform the ordinary scroll's personal teleport.

Two distinct stable actions:

- **Use recorded stable scroll on a block:** queues BuildPortalEvent, using player clockwise direction to lay a vertical portal plane above clicked block. Neither this useOn body nor BuildPortalEvent has an item/Source debit. It builds a **temporary** frame; don't confuse it with9000Source dropped-scroll creation. Native config gate above is in trySpawnPortal, not repeated inside this direct queued builder; effective intended disable behavior needs pack acceptance across both entry paths.
- **Drop recorded stable scroll:** delegates ordinary onEntityItemUpdate; creation checks/payments apply and **shrinks one stable scroll on success**. Stable isn't infinitely reusable in every interaction just because direct useOn doesn't consume it.

Builder frame has outer5wide×5high footprint in its plane above click, with3wide×3high interior (constructor bottom row spans-2..2, sides3high, top row). It reuses decorative frame blocks, or places TemporaryBlock mimicking Sourcestone with tickDuration1200 and current gameTime. Existing decorative TemporaryTile can have its mimic/duration refreshed. It then places all empty/replaceable interior portal blocks and sets scroll destination on created portal tiles. Blocked nonreplaceable location aborts, destroys this event's recorded placed blocks without drops; this is not a general restore-old-world snapshot. Does not consume a material inventory to build the temporary frame in these methods. Actual collapse/expiry depends on temporary block and neighbor-update behavior, not a guaranteed permanent portal or independently measured60seconds wall time.

`reset_stable_warp_scroll` shapeless returns fresh stable scroll, clearing saved destination. Re-record/inspect actual destination, don't accidentally drop a reusable tool into a fueled frame. This is native operation research, not a proposal to enable absent dimensions or bypass protected access.

## Travel, failure and passenger boundaries

PortalBlock.entityInside requires entity.canUsePortal(false). Players are queued in PortalTile Set and processed on tile tick; nonplayers call warp directly, then fallDistance0. Follow-projectile particles are excluded from player-queue processing. This is entity travel, not only a player UI.

PortalTile resolves saved dimension string to actual ServerLevel. Same-dimension path adds PORTAL region ticket, sends position update, calls teleportTo and rotates; if target block itself is PortalBlock, it returns entity unchanged. With passengers it sends passenger/vehicle updates; doesn't prove every passenger independently arrives safely. Cross-dimension path calls changeDimension(DimensionTransition(target world, destination, zero velocity, saved rotation)). Missing destination level returns original entity from helper; a nonnull helper return can therefore mean **no move**, so visual/success feedback isn't proof destination loaded or travel happened.

No safe-landing collision check, fee, faction permission or dimension-unlock check in these tile methods beyond referenced engine/hooks; effective TMM protection/dimension policy must be verified separately. Portals don't award legitimate access to a gated dimension from a hardcoded coordinate string. Saved warp/dimension/rotation/display/horizontal state has NBT fields; queue clears each tick. Relog/unload, unavailable dimensions and rebuilds need acceptance.

## Quest and acceptance map

Teach exact scroll recipe → recorded same-world coordinate → one personal consumable trip → frame/9000Source/drop activation → actual travel → intentional return route → optional Stable temporary/cross-dimension branch. Cite Warp Portal/Stable Scroll notebook pages and exact recipes. Item/scroll possession is acquisition proxy; portal advancement near4blocks signals creation feedback, not a trip or safe return. Use XP and ordinary frame/fiber supplies as rewards; don't hand out marked gated destinations or advanced travel tools as gate shortcuts.

Positive/boundary: main/offhand, unrecorded Shift vs recorded Shift, full inventory multi-stack, reset, ordinary wrong dimension; vertical2×3interior vs invalid frame/extra blocked cell/Y<0, horizontal20vs21boundary;9000available vs insufficient/concurrent drain, display names; Stable useOn vs dropped-consumed and blocked temporary cleanup, config checks per path; actual travel vs targetPortal/no destination level, passengers/entities, one-way vs return and chunk reload. Count Source, scrolls, placed temporary blocks and destination coordinates independently. Parent researching.
