# Current Ars family — Sauce 0.0.74.113 and Book of Friends

## Artifact and scope

Current Elemental0.7.11.3 packages `META-INF/jarjar/sauce-1.21.1-0.0.74.113.jar`, coordinate `com.alexthw.sauce:sauce-1.21.1`, supported range `[0.0.74,)`, nested SHA256 `e46494d1530f395bd6347f854ed2e381a339feadc7ae418b9469718b1d7ca652`. Exact containing artifact/hash: [current pin receipt](ars-elemental-pinned-utility-rituals.md). New JAR fully CFR0.152 decompiled externally and ZIP entries compared to61.100. No loader or gameplay acceptance.

Elemancy1.18.3 still supplies29.73/[29,), other existing family pins unchanged at integration `17a564d85b5ee3f0f4f830b68397e54a824a768e`; new Conflux0.1.56 has no jarjar metadata. Intersection for the two known Sauce providers is now **[74,)**, so74.113 is sole eligible candidate supplied by these inspected nine family artifacts. The [old61.100 receipt](ars-family-pinned-sauce.md) is historical. No whole-pack provider/loader selected-version claim; inspect active assembled dependency graph when authorized. Do not add a standalone library or edit pins to force selection during research.

## What did and did not change

Class ZIP delta is confined to Sauce bootstrap/registry/lang/network, ArcanePlinth, new FriendBookItem/AlliesSavedData and client ally UI, plus BetterFilters. Old school-focus/bangle attribute implementation, caster-tome resolver, focus-enhanced turret, prism lens, summon-power hooks, GlyphEffectUtil and PosCarryMEI class bodies are **byte-identical** between61.100and74.113. This preserves those inspected recipient bodies, not a proof that registration/runtime class selection and new mixins have no impact. New BetterFilters damage hook below is a real behavior addition. Arcane Plinth use/debit recipient is reviewed below; no automated throughput is promoted from this inventory.

## Book of Friends acquisition and UI

`data/sauce/recipe/book_of_friends.json`: shapeless1Book +1`ars_nouveau:starbuncle_shards` →1`sauce:book_of_friends`. Recipe-discovery advancement grants recipe when possessing Worn Notebook or recipe already unlocked; that is recipe-book discovery, not an extra physical crafting currency. No Source/mana fee in recipe or item use.

FriendBookItem.use on server Player sends PacketOpenAllyScreen; client opens GuiAlliesScreen. It is **an ally settings UI**, not an alternate magic guide or spell-tier requirement. JEI Crafting supplies recipe; no dedicated Notebook walkthrough entry was established in this source slice, so future quest text must give direct UI instructions.

UI maintains sets of **player UUIDs** and **entity-type ResourceLocations**. Player entry accepts UUID directly or looks up profile by name; unknown name marks field red. Entity entry parses/lowercases registry ID and rejects an ID absent from client entity registry. Adding an entity type applies to every matching nonplayer entity, not one pet UUID, owner or tag. Player path compares exact UUID, so adding minecraft:player as entity type does not replace individual-player entries in isAlly. List entries support removal and resync.

After edit, client caches local list and sends PacketSetAllies with own player UUID. Item stack does not store friends: server SavedData stores map owner UUID→AllyData. This is **directional outgoing protection**, not a reciprocal team/faction alliance, entity taming, control permission or universal PvP disable.

## Actual damage recipient and dimension boundary

Required `sauce.mixins.json` includes BetterFilters on native **IDamageEffect**. Existing contextual filter injection remains. New TAIL injection into canDamage acts only if original return wastrue; when shooter is OwnableEntity, substitutes its owner. If resulting shooter is Player on ServerLevel, returnsfalse when AlliesSavedData.isAlly(current shooter level, player UUID, target). It only narrows original permission, not authorize a formerly rejected hit. Unowned pets, nonplayer shooters and paths that do not call this interface are outside that guard. ISummon ownership is not separately resolved by this particular hook.

MethodHomingProjectile additionally consults same ally data in its own targeting predicate; see [current Homing impact](ars-elemental-pinned-07113-impact.md). Damage blocking and target avoidance are different mechanisms. Neither proves immunity to status effects, direct hurt calls, ritual lightning, explosions or another mod's damage path.

**Storage mismatch to test:** PacketSetAllies.onServerReceived writes `minecraftServer.overworld()` under packet-supplied owner; canDamage/Homing read shooter's current ServerLevel. AlliesSavedData.get uses that level's data storage, with no global-overworld normalization. Thus editing in Nether/End still writes Overworld but these recipient reads can see a separate empty dimension list. Login sync reads login level; onSave sends lists only from Overworld to its current players. No immediate server response/readback after edit is in setter. Do not promise cross-dimension or cross-world consistency.

PacketSetAllies handler does not compare supplied owner to sender UUID or validate transmitted type entries; ordinary GUI sends self and valid IDs, but server-authoritative ownership validation is not established. Record as a source boundary for compatibility/acceptance; no packet tampering test, exploit workflow or runtime fix performed. Shared local cache and save/login synchronization require multiplayer observation before promising reliable server settings.

SavedData serializes owner/player UUIDs and entity IDs under ArsAllies to `ars_allies`; setAllies marks dirty. Direct source persistence is present, actual reload/network behavior is untested. No cooldown/durability debit in Book use; contents live outside item so losing/recrafting book is not source-proven list deletion.

## Walkthrough and unrun acceptance

Optional safe-casting lesson after normal native guide/Starbuncle material access: craft Book of Friends, use, add player's UUID/name or a deliberate entity registry type, remove mistakes in list, then verify outgoing damage and Homing targeting in controlled play. State that this protects only participating paths and has unresolved dimension behavior. Acquisition task proves Book possession only; manually verify actual exclusion. Positive XP +ordinary supplies; avoid rewarding a hazardous PvP or arbitrary hostile-type whitelist as mandatory progression.

Test same-dimension configured Player/nonplayer type versus unlisted target, reciprocal player direction, ordinary Player versus owned/unowned shooter, original canDamage=false preservation, Homing with and without Sensitive, status/direct/routine damage contrast, dimension transfer/login/save/reload/list removal. Check personal/team task credit separately. Do not call Book settings a universal safe-zone or claim a runtime failure solely from source suspicion.

## Arcane Plinth: stored stacks and one-unit apparatus inputs

Recipe `sauce:arcane_plinth`: shaped SSS/SPS/SSS, S=native Sourcestone, P=native Arcane Pedestal→1Plinth. Recipe existed in61.100, but that archive lacks the current ArcanePlinth block class; current registry now instantiates this subclass of ArcanePedestal, with ArcanePlinthTile. This is a native workshop pedestal, not a general storage/network dependency or permission to install logistics.

Current main-hand use on server copies **whole held stack** into tile and empties hand; occupied prior stack is spawned at player's position. Empty hand retrieves by spawned ItemEntity and clears slot. No automatic merge with old stack and spawn returns unchecked: test full inventory/denied spawns. Offhand passes to default block interaction. Tile's canPlaceItem returns true, but native inherited SingleItemTile.getMaxStackSize still1; direct setStack doesn't clamp, so successful manual bulk storage does not prove every inventory capability accepts bulk.

Existing unchanged ApparatusPedestalPatch redirects clearItems setStack: for ArcanePlinthTile with current count>1 remove1and spawn crafting remainder if nonempty; otherwise setStack(remainder) normally. Native preserves-tag branch remains outside redirected call. Thus one compatible plinth stack represents **one ingredient candidate/one consumed unit per apparatus craft**, not all recipe slots at once. Extra nonempty pedestals still affect matching. Output spawn/consumption and insertion API remain acceptance boundaries; no automatic recraft/reagent feed is provided by this block.

Use JEI Crafting and native Notebook Enchanting Apparatus layout, one matching ingredient kind on each plinth. A known 3-item stack should become2after one valid ordinary recipe; a1-item stack clears/remainders. Compare native pedestal/Plinth, preserve-tag item, returned-remainder item, replacement/retrieval and capability insertion. No runtime tests.
