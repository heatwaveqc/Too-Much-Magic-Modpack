# Ars Controle — pinned control and automation walkthrough

| Field | Value |
| --- | --- |
| Manifest / mod ID | `ars-controle` / `ars_controle` |
| Artifact | `ars_controle-1.21.1-1.6.16.jar`,313828bytes |
| Full SHA512 | `458c5a878a1418ffce46d1dd109f4ae23bcbe10bd7153edcaff8d1ab49ab3032f37977488eee7cecdb550cd99672f560e9d851818d6775d5d7e222632285d1a6` |
| Status | **source-complete for pinned installed scope, with known native limitations below**; no client/world acceptance |
| Parent | [Ars Nouveau](ars-nouveau.md)5.13.5 |
| Reconciliation | Fetched integration `9310199d33587be7970d1bca6c15f864523e28fc`; Controle/Ars/CCpins unchanged; no newer research/pilot/Ars chapter diff versus prior fetched2f662b9e |
| Scope | Six functional devices, eightfilters and PreciseDelay; CC: Tweaked1.121.0 is pinned optional integration. No independent boss, research currency or mandatory device tier ladder. |

Controle extends native Ars configuration, Boolean/height/random/timing spell control, capability/redstone proxies, server-load sensing, moving ritual effects and portals/projectile routing. It is useful for a small remote configuration workspace, selected low-cost logic spells and an optional travel/control workshop. It doesn't supply a universal item/fluid/energy pump; capability forwarding and remote configuration are different operations. Source-complete means the route and known failures are documented, not all behaviors approved for balance or implemented as working quests.

## Guide access and native prerequisites

Craft Worn Notebook from Book+lapis-gem-tag item, right-click; see [native guide/glyph learning](ars-nouveau-pinned-guide-glyphs.md). Exact1.6.16 ACDocumentation registers **Crafting** entries Remote,Scryer's Linkage,Temporal Stability Sensor,Warp Scroll Holder,Warping Spell Prism; **Items** Portable Brazier Relay, crosslinked to native Wand/Brazier/Prism/scroll pages. Glyphs appear through native effective glyph registry/tier index. This is Java documentation registration, not an absent Patchouli resource inference. Rendering/search still needs client acceptance.

First build parent's [Scribe/processing/Apparatus](ars-nouveau-pinned-crafting.md), create a valid [native spell](ars-nouveau-pinned-casting.md), and learn native Dominion Wand/ritual/travel controls through named Notebook pages and JEI. Use exact recipe IDs below and minimum local steps rather than requiring an external wiki. SourceCost0recipes still consume reagent/pedestals; they aren't free resource unlocks. Portable has explicit NetherStar/Tribute material gate, so keep that optional branch after native encounters.

## Start-to-end branch walkthrough

1. **Remote configuration.** Apparatus recipe `ars_controle:remote` consumes Dominion Wand+4ender-pearl-tag+4PoppedChorusFruit. Hold mainhand, sneak-use on target block/livingentity to store it, then ordinary-use another compatible endpoint. Start Single mode and two nearby endpoints. **Ars Selection HUD** key is native radial(defaultV,key86; actual remap in Controls), ordinary radial chooses Single/Multiple, sneak+radial chooses LockedFirst/LockedLast. Endpoint-specific callback order differs for block/entity interactions; see Remote companion. Multiple uses first/secondinclusiveblockcorners, not a universal safe unlimited group. **Left-click empty air clears target**; direct client/server evidence below. Observe actual endpoint links, not just Remote targetname/outline.
2. **Read server load.** Sensor recipe consumes Clock reagent+EyeOfEnder+SourceGemBlock, Source0. Place comparator reading it; compare a quiet known server state and wire reversible output to an already-known optionalfarm if desired. Getter is roundedaverageexecutionms×15/50, updates scheduled10ticks, noexplicit15clamp. It doesn't itself stop a machine or measure player networklatency. Avoid intentionally loading a live world to demonstrate it.
3. **Optional logic spells.** Scribe eight Tier1filter recipes27XPpoints each, PreciseDelayTier2recipe55points; ingredients in glyph companion. Paypoints/materials, collect/rightclickglyph, save validated spell. Choose one height/randomfilter then simple harmless effect, observe pass/fail; add NOTorOR/XOR/XNOR with required immediate filter operands, no nestedadaptiveoperator grammar. Delay with nExtendTime uses1<<n ticks(0→1,1→2), zeroownbasecost and20defaultaugmentlimit; form/wholecastmana still native. Learn chosen options rather than require allnineglyphs as a fake tier ladder.
4. **Optional remote capability/redstone.** Linkage recipe consumes Scryer's Crystal+4pearl-tag+4PoppedChorusFruit,Source0. Dominion Wand selects targetfirst then Linkagelast. Attach only an already-supported native caller/reader and observe targethandler operation or redstone/comparator output. Blacklists exclude native StorageLectern/Linkage and selected capabilityclasses. It passes querycontext/sides; it doesn't store copiedinventory, generateFE or guarantee every mod API. CrossdimensionGlobalPosresolution exists, but loadedtarget/handler/claims constraints remain. Don't add excluded logisticsmods for this lesson.
5. **Optional controllable portals.** Craft WarpScrollHolder from8SourcestoneSlabs in3×3ring; record valid native WarpScroll, prepare decorativeArs portalframe, replace oneframeblock with holderfacing into frame, insert recordedscroll. Default1000Source request withinrange10at opening newemptyinterior, using nativeproviderlookup; scrollreusableinholder. Click occupiedholder to extract/close, closebeforechangingdestination because existingportalcellsaren'treinitialized. Comparator15meanshasScroll, notpaid/open. Actual orientation/vertex/interior constraints and failures in portalcompanion; native [travel acquisition](ars-nouveau-pinned-warp-portals.md).
6. **Optional projectile routing.** WarpingPrism Apparatus consumes SpellPrism+4pearl-tag+4PoppedChorusFruit,Source0. TargetwithWand(targetfirst,Prismlast), or emptyhandsneakPrism toselectself. Fire safe native EntityProjectileSpell intoit. Blocktarget routing quotes configuredSource(costdistanceformula/defaultsbelow), mayloadtargetchunkandmovebeforepayment, then resolve/continueeffect; entitytargetreturnsbeforeSourcecollection. Otherplayerslinkdefaultdeniedunlesscreative/configpermits. Targethitandquotedcostdon'tproveactualdebit/effect. Use ownfixture; no remoteattack/claimsapproval.
7. **Late moving ritual option.** PortableRelay consumes BrazierRelay+2pearl-tag+2PoppedChorusFruit+2ManipulationEssence+NetherStar+WildenTribute,Source0. PrepareoneallowedritualatloadedrealBrazier, useRelayonit, carrymaininventory andobserveeffectfollowingposition. Sourcefuel/pause/stop parity is **not guaranteed** by pinned directtick implementation; knowncache/toggle/migration/rangequerylimits below. Don't describe it as a reliable safe universalritualcarrier merely because crafted. Reward directions must not supply the bossmaterials for free.
8. **Installed CC option.** Adjacent computer can inspect registered `scroll_holder` target data; `warping_spell_prism` exposes mainthread setTarget(x,y,z),setTargetWithLevel(level,x,y,z),target(),sourceNeeded(). NoLuaentitysetter or holderbespokeopen/settargetcommand here. Use Wand/self/entitycontrol and nativeiteminterface separately. CCprogrammingis optional, not required to reach above survival endpoint.

Everydevice/glyphbranch is optional after relevant native material/knowledgegate. Completion is one useful observedcontrolsetup plus discoverable otherchoices; no hidden extra tier/boss added by Controle. The Portable recipe's Tribute/Star remains an actual resource gate even though other controls are earlier.

## Exact control correction and pilot reconciliation

`ArsControleClient` is client-only registered eventlistener. `onClickEmpty(PlayerInteractEvent.LeftClickEmpty)` sends PacketClearRemote for Remote; serverhandler checks sender's currentmainhandRemote and writes cleareddata preservinglock/selectionwhileclearingtarget/firstcorner. This closes the earlier unidentifiedreset path. Native IRadialProvider.forKey returns configuredOPEN_RADIAL_HUD; exact5.13.5keymapping selection_hud default86(V). No specialclearpacketcommand or guessedresetkey required. Actual input/remaps/latency still clientacceptance, not missing-source-path blocker.

| Pilot node / stable ID | Actual criterion and reward | Full teaching coverage / gap |
| --- | --- | --- |
|32control_remote /544DB13000200000|Remote×1possession;40XP+4RedstoneTorches|Teachguide/recipe/selection/modes/leftclickair/endpointstate. Item alone doesn'tverifyconfiguration. |
|33control_sensor /544DB13000210000|Sensor×1possession;50XP+4Repeaters|Teachcomparatorformula/wiring, no automaticlagcure or validated15clamp. |
|34control_brazier /544DB13000220000|PortableRelay×1possession;65XP+8FireworkRockets|Teachlateingredients/supportedritual/bind/move/load/fuel/stoplimitations. No operation/fuel detector. |
|Absent optionalbranches|No currentControlepilotcriteria|Glyphlogic/delay,Linkage,Holder,PrismandCCneed optionalhowtos/references and realdetectororhonestmanualproxy. |

[All49pilotcrosswalk](ars-nouveau-pilot-crosswalk.md) reconciles canonical/resolved/chapter IDs. Pilot sourcepresence is not complete implementedwalkthrough or clientteamclaimPASS. Future newlessons shouldgive modestXPandusefulcommonredstone/scroll/stonesupplies, preservebossgates, and explain whatFTBactuallychecks. This research doesn'tauthorquests.

## Subsystem coverage and known native limits

| Exact binary companion | Source coverage / material limits |
| --- | --- |
|[Guide/glyph/sensor](ars-controle-pinned-glyphs-sensor.md)|All15recipes,nativeguide,8filters+Delaygrammar/chance/Y/ticks; adaptivebinarysecondoperandaugmentindex/skipboundary,negativeYtruncation andsensornoclamp requiretest. |
|[Remote/Linkage](ars-controle-pinned-remote-linkage.md)|Mode/block/entitydispatch,area/BreakEvent,capability/context/cache/class+blockblacklists/redstoneoutput; no targetclaims guarantee. Reset nowclosedabove. |
|[PortableRelay](ars-controle-pinned-portable-relay.md)|Recipe/blacklist/association/directtick/movingcontext/rangequery; shareditemfieldcache,60tickmapping,UUIDnamechange/toggle,Source/pausebypass andnoncancellableinjectioncancel documented. |
|[Holder/Prism/CC](ars-controle-pinned-portals-prism.md)|Frame/inventory/openclose/costs/projectile/crossdim/tickets/CC; existingportalcharge/targetlimits,entitySourceomission,prepaymentmovement/ticketandfullinventoryoutput risks. |

Known limitations are part of source completion, not silently certified gameplay. In particular Portable suppliedSource guidance isn't evidence directitemtickchecks it; Prism sourceNeeded isn'tcollectionreceipt; SourceholderhasScroll isn'topenportal; Linkageforwardedhandlerisn'townedtransportnetwork. Final client acceptance can disagree with or expose more failures than decompiled source predicts. Reopen profile scope if pins/config/mappings/installedendpoints change.

## Evidence and validation

ExactJARfullSHA512rechecked;97classes/allinnerCFRoutsideGit,15recipes; native5.13.5SourceUtil/IRadialProvider/KeyBindings plus parentcompanions; addonclientresetpacket/nativeguide registrations. Earlier [version-aligned upstream14c5f477](https://github.com/Vonr/Ars-Controle/commit/14c5f4770a9ec265491fb9a7ae1a60a2123dfbc0) is secondaryreference, not reproducedbinarybuild. It targetedArs5.13.1whilepack5.13.5recipientmethodsauditeddirectly. Fetchedintegration9310199d33587be7970d1bca6c15f864523e28fc pinsunchanged; no runtime/research/pilotrewrites fromconcurrentbranchcopied.

Read-onlyUTF8/local-links/docs-only/whitespace andactual3pilotcriterion/rewardcrosscheck. Noassembly/server/client/gameplay or currenteffectiveconfigreceipt, runtime/config/quest/pin changes ormerge. Acceptanceplan: allpositive/boundarycasesincompanions, includinginputremap/clear,claims,loaded/unloaded/crossdimension,payments/refunds/outputs,formfilters/grammar,Sourcepause/portalframe/inventory/cancellation,CCcallsandpersonalteamclaims. Noheavyloadorfarmscreatedduringresearch.
