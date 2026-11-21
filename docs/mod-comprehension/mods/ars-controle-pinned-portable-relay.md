# Ars Controle1.6.16 — Portable Brazier Relay lifecycle

Direct pinned binary continuation of [guide/glyph/sensor](ars-controle-pinned-glyphs-sensor.md) and [Remote/Linkage](ars-controle-pinned-remote-linkage.md). Same313828byte/fullSHA512verified artifact and97classCFR `controle-fresh`; native Ars5.13.5 caller evidence. [Addon](ars-controle.md) remains researching for ScrollHolder/WarpingPrism and final control/pilot review. No gameplay acceptance or runtime changes.

## Acquisition and intended operation

Native Notebook **Items → Portable Brazier Relay**, crosslinked to Ritual Brazier/Brazier Relay; JEI recipe consumes BrazierRelay reagent plus2ender-pearl-tag,2PoppedChorusFruit,2ManipulationEssence,NetherStar,WildenTribute, SourceCost0/keepNbtfalse. This is a late material gate. See [native crafting](ars-nouveau-pinned-crafting.md) and [Tribute route](ars-nouveau-pinned-rituals.md).

Prepare one safe supported ritual at a real loaded Brazier. Serverplayer use of PortableRelay on Brazier records sourceGlobalPos/newrandomUUID/ritualname; Brazier stores matching RELAY_UUID and associatedplayerUUID. Carrying the item drives operation. Client outline requires held main/offhand matching stack and same dimension as bound position; outline isn't proof active server association. Native page says keep original Brazier loaded and supplied with Source; actual gate differences below must be tested rather than assumed.

`canRelay` rejects ConjureBiomeRitual, StructureRitual, FeaturePlacementRitual subclasses; requires registered RitualTablet and rejects `ars_controle:ritual_blacklist`. Exact tag contains Burrowing, Awakening, Sunrise, Moonfall and Challenge tablets. Effective tags can add exclusions. Every other third-party ritual is not thereby guaranteed safe while moving. Empty Brazier isn't demonstrated graceful failure: canRelay assumes nonnull ritual before getRegistryName.

## Binding, state and cache

`getBrazier(entity,stack)` requires storedposition+UUID and resolves server dimension. Unloaded block returnsnull without clearing. Invaliddimension/wrongtile/missingritual or nonnullentity UUIDmismatch clears itemcomponent; nowblacklisted clears item plus tile associations. Calls with entitynull skip UUIDcomparison.

`useOn` clears previous resolved Brazier attachments **before** checking clicked Brazier's existing UUID for same-target toggle. SameBrazier secondclick may rebind rather than clear; no guaranteed reset gesture asserted. Retargeting may clear prior association before new setup. No BreakEvent/owner check appears here unlike Remote; stored association is not a claims policy.

Server inventoryTick every20gameticks resolves original Brazier, returns if missing/done, updates association and cachedRitual. Ritual-name change builds new data with **new random UUID**, without matching tileUUID update in that branch; later resolution can invalidate it. `cachedRitual` is an itemclass field, not perstack/player data. Failed refresh doesn't reset that field; other ticks may see an earlier running ritual. Multiple items/players, retarget/remove/unload and namechange need explicit tests.

`getRelayedRituals` WeakHashMap ritual→player refreshes only after LAST_UPDATE+60serverticks. Scans players' maininventoryitems, resolves getBrazier(null,stack) (thus skips UUIDcheck), and overwrites duplicate ritual mapping. This path doesn't scan Curios/armor/offhand. Map can lag item movement/removal. ServerStopped clearCache clears map/timestamp, not cachedRitual field.

## Actual tick and Source boundary

With cached running ritual, inventoryTick directly invokes protected ritual.tick via AbstractRitualInvoker. `RitualBrazierTileMixin` suppresses original native tryTick whenever tile has RELAY_UUID. Sourcecollection remains in original Brazier tick outside that interception, but direct itemtick does **not** check original isOff, needsSourceNow or successful Source payment. Native tryTick checks started/notdone; isRunning supplies that condition, not Source/pause parity. Therefore native page's supply instruction isn't proof depleted Source/redstone prevents all relayed effects. Do not approve economy or exploit from this source finding.

No independent ritual copy/chunkloader is created. Original context tile remains real Brazier. `AbstractRitualMixin` changes getWorld/getPos to mapped player's level/blockposition on server. Effect may follow across dimensions while source stays loaded; ritual implementations using tile fields rather than those getters can differ.

`RitualBrazierTileMixin.getInventoryManager` returns PlayerCaster inventory only if association resolves player **in source ServerLevel**, otherwise original manager. Crossdimension effectposition does not establish crossdimension associated inventory. Ingredient/output/remainder contracts still depend on each native ritual.

`RitualEventQueueMixin` appends exactclass matching relayed RangeRituals; append loop has no requested-level filter. Single getRitual checks supplied predicate and suppresses native relayed result. getRituals exclusion injection calls cir.cancel without cancellabletrue declaration, a transformed-path failure boundary to test. No blanket claim all range events work in every dimension. None of these findings authorize a runtime patch during research.

## Teaching and unrun acceptance

Pilot `control_brazier` item possession doesn't prove binding, effectfollow, fuel or stop. Future optional how-to should name native page, late recipe and one allowed ritual, observe original/effect locations and actual resources, then test removal/pause. SmallXP/common supplies; no boss-material reward bypass.

Notrun: allowed/blocked/empty Brazier; firstbind/sametarget/retarget/ritualnamechange; Source depleted/redstone pause; one versus multipleitems/players, inventory/offhand/Curios; cachedtick/map refresh/unload/reload/relog; crossdimension effect versus inventory; rangequery cancellation/level filtering; claims/recipientcredit and UI sync. Known code limitations are explicit; they do not block other authorized source research.
