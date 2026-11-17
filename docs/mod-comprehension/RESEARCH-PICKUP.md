# Research pickup — local JAR continuation

## Current workstream

- Branch: `research/mod-comprehension-local-jars-20261116`; base `cb8e2dcab59cc625500b3ca0329df1511823610b`.
- User requests completion of major magic-mod research Markdown and regular recovery commits. Research only; preserve QA edits in the separate `qa-source` checkout.
- Start with `AGENTS.md`, `MOD-COMPREHENSION.md`, `WORK-CHAT-INSTRUCTIONS.md`, `INDEX.md`, templates and the quest walkthrough standard. Keep partial profiles `researching`; artifact inspection is not gameplay testing.
- [Artifact audit](LOCAL-ARTIFACT-AUDIT-2026-11-16.md): 156/159 current baseline pins match local files, all 159 metafiles match the index. Missing baseline JARs are Default Options, FTB Filter System and Keysets; all selected Goety research artifacts match. The prior QA checkout had 161/161 matches; those numbers describe different snapshots.

## Active slice and exact next steps

1. Completed this slice: Goety Cataclysm Fabricator and parent training/altar bytecode audit, 61-recipe appendix, 11 conversion routes, all 47 Cataclysm recipe item/block IDs matched registration literals, exact scaffold IDs, and targeted boundary tests. Key corrections: charged ritual advances are 20 game ticks apart; nominal cost is rate × duration; obstructed Fabricator attempts spend a queue entry. Profile remains researching because focus effects, research/structure gates, servant care and full acquisition paths are incomplete.
2. Next: map all focus cast effects/costs, parent research unlock/Forge setup, servant care/death recovery and boss-drop acquisition. Recipe inventory and exact current scaffold reconciliation are done; do not repeat them. The appendix is factual data coverage, not proof of complete progression.
3. Update index and this note, commit each coherent finding, then push and open a reviewable research PR to `test/full-integration`. Never merge into the shared integration branch yourself.
4. Next priorities: base Goety remaining ritual/focus/structure paths; Ars/Occultism exact artifact gaps; Mahou Tsukai exact artifact mechanics. Read existing profiles before duplicating work.

## Inspection method and local-only scratch

The local artifact directory is documented in the audit. `javap.exe` is available in Prism's `java/java-runtime-delta/bin/`. CFR 0.152 from its official site was used only for selected local classes; transient output lives under the Windows user's `AppData/Local/Temp/tmm-magic-research-20261116`. Do not stage decompiled Java, classes, JARs, assets or caches. Recover every claim by inspecting exact hash-matched artifacts; scratch paths are convenience only.

No full smoke, assembly, client launch, gameplay or new quest/config/runtime implementation is authorized by this research task. Workflow gates must be inspected before pushing; only quick CI is appropriate.

## Validation and continuation receipt

- Recovery commits: `9425b041` (local access audit/pickup) and `8efd4af1` (addon recipe/operating audit); subsequent commit corrects base-Goety ritual totals and cleans historical blocker text.
- `python scripts/validate.py`: PASS after canonical LF text normalization in the isolated research checkout (159 active pins, 294 indexed files, 181 desired selections).
- `python scripts/validate_integration_sources.py`: read-only PASS at `8efd4af1`, after fetching the 13 missing frozen donor commits. No assembly or smoke ran. Rerun on the final clean research head.
- Windows Git initially materialized CRLF text outside explicit `eol=lf` patterns. Only isolated-worktree text bytes were normalized and the unchanged index refreshed; no runtime/config changes entered the research commits. The unrelated `qa-source` edits were preserved.
- Local Markdown check found one pre-existing template placeholder in INDEX (`mods/<manifest-id>.md`); exclude that deliberate placeholder from literal link validation. All 61 recipe keys occur exactly once in the appendix.
- Base Goety Forbidden Fragment/Piece acquisition was already expanded on the current integration base. Do not repeat that work or keep the older claim that no sourcing is mapped. Its runtime loot acceptance remains open.

## Published handoff

- Review PR: [#265](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/265), targeting `test/full-integration`; not merged by this research chat.
- Research commit `544a40e7` passed the metadata validator, read-only Integration Preflight (zero baseline replacements), docs-only scope, local Markdown link check and 61-row recipe completeness check. No client/gameplay acceptance is implied.
- Base advanced concurrently to `45f2b6429130a521e4ddb5ca165bff6eb62adfcf`, changing only `manifest/integration-sources.json`; this branch does not alter that file. Review against the current shared baseline and rerun preflight before any eventual merge.
- Resume this branch/worktree for the next slice, then keep commits small and update this file with exact completed and remaining coverage. Do not promote either Goety profile to source-complete yet.

## Continuing after the first PR slice

- User explicitly requested continuing until context exhaustion, completion or a serious blocker. Keep this same PR/branch; do not stop after one small slice by default.
- Completed next slice: `mods/goety-ritual-gates.md` maps exact Forge setup; nine addon-used ritual/environment families; Grave Soil depletion callback; research capability/use behavior; Bygone/Buried/Warred/Terminus recipes/acquisition and prerequisites; blueprint reward-loss boundary. Linked from both parent/addon profiles and index.
- Next active work: all 28 registered addon focuses, default costs/cast/cooldown, meaningful staff modifiers and servant summon effects. Selected classes and `GCSpellConfig`, `GCItems` are decompiled only in local scratch. `EMPSpell` is present as a class but not among the 28 `GCItems` focus registrations; do not claim an obtainable EMP focus from class presence alone.
- Parent research evidence is exact JAR: `ResearchScroll`, `ExtraScroll`, `ResearchList`, `SEHelper`, `DarkAltarBlockEntity.activate`; ritual type classes delegate through `RitualRequirements`. `ModLootInject` is subscribed and points current singular `loot_table` resources at named vanilla tables. Old plural loot resources need separate reachability review.

- Further slice: added `interactions/goety--goety-cataclysm--staged-cast-payment.md` with exact parent start/use/finish/release dispatch and the three addon stage/cooldown handlers. Payment-before-effect cannot be assumed: these classes emit from useSpell and have cooldown-only release handlers. Not gameplay tested. Next finish the 28-focus operation/default table, then servant care/recovery and remaining native gates.

- Completed focus slice: `mods/goety-cataclysm-focuses.md` has all28 GCItems registrations, class/default cost/cast/cooldown/enchantments and producer behavior; temporary summon30–119 seconds×Duration, cap/recall/Summon Down, Ashen Breath20-execution charging and cap comment mismatch. Underlying hit/filter formulas remain partial.
- Extended staged-payment report to fourth path Abyssal Beam. Important distinction: `AbyssMark.tick` discards a mark if player stops casting, so immediate cancellation is guarded; mark nevertheless emits portals after configured80 lifespan while full cast is90. `AbyssBlastPortal` has independent life/emission without cast-state check. Test post-emission/pre-completion; do not claim every early cancel produces damage.
- Next: servant care/healing/death/recovery, full encounter/drop acquisition and remaining base ritual families. The full spell effect delegation audit is large; prioritize consequential HP damage and world/ally filtering before any source-complete label.

- Completed care/recovery slice: `mods/goety-cataclysm-servants.md`. Mapped heal families, last-stack post-shrink loss/reduced-heal boundary, revival spirits/cores, parent serialized entity payload/FlyingItem delivery, Prowler3×3×3 reconstruction, juvenile Remnant skull, Ender/NM head patterns and pre-spawn destructive clearing. Explicit gaps remain for unlisted inherited care and Draugr soulJar invocation. Supporting generated target ItemStack source hash recorded; no runtime fixes.
- Next highest-value closure: remaining six base ritual types (the gate appendix currently covers nine addon-used types); parent conversion `MobUtil.convertTo` target ownership/equipment order; initial construct recognizers. Then follow consequential delegated HP-damage and resource-acquisition gaps, or move to another major mod's exact JAR profile once a coherent Goety slice is saved.

- Completed next closure: gate appendix now covers all15 registered native ritual type predicates (not just nine), candle-count/extinguish, enchant-power integer truncation, Apostle Soul Fire depletion, and shared `MobUtil.convertTo` semantics checked with javap when CFR was unstructured. General target selection is by nearby Mob tag, not owner; tamed result assigned to caster. Gear copied/cleared before add; add boolean ignored, source discarded. Concrete target equipment hooks and actual pack protections remain open.
- Next work should avoid repeating these inventories. Prioritize source-reachable/current-namespace acquisition and delegated damage/ally/grief filtering; source-complete still requires those, complete base Goety recipe/brew/utility branches and guide coverage. Exact JAR access is not blocked.

- Smaller framework closure: `mods/summoning-rituals.md` directly audited exact SHA1-pinned3.14.2 binary. No bundled recipes;31,021 recipe JSON resources across163 then-local JARs had no namespace reference; selected frozen donor paths empty. Corrected false historical two-script roster, item consumption (completion, not start), item-handler extraction (denied), commands (server source), conditions checked at start, inventory-only save state. Exact informational quest IDs/rewards reconciled. Marked source-complete strictly for unused framework baseline; adding a real acquisition/ritual path requires reopening coverage. No test recipe or runtime changes.

- Mahou Tsukai next slice completed: `mods/mahou-tsukai-pinned-onboarding.md` and main profile updates. Exact SHA512 verified;33 crafting resources;53 internal recipes/53 guide matches;35 cloth-required;19 advancement declarations. Starter grid, four-Diamond combined tool vs cheap Hammer, unchanged crafting tool remainders, Dagger2 damage request/Bleeding300ticks, draw-packet placement/failed Bleeding opportunity and counted-unordered recipe equality traced. Main remains researching; mana/circuit/effect/special-weapon chains and18 custom emitters still need work.
- Mahou scratch classes under `decompiled/stepsword/mahoutsukai` in same local temp. Next efficient action: trace `PlayerManaManager`/IMahou/mana-circuit and core Mahoujin lifecycle, then individual high-value exchange/weapon progression. Do not redo the53-row registry or treat cloth-required=false as proof every spell is scroll-capable.

- Completed Mahou mana/circuit slice: `mods/mahou-tsukai-mana-circuits.md`. Payment branch order verified against javap; normal full payment personal/gems/circuits, insufficient cases depend on flags; personal-only growth including donation, range strict X/Z with no Y, owner/shared access and owner-unchecked discrete toggle, redstone inversion, NBT vs implicit components and raw builder defaults recorded. Regen field is deliberately not called a per-game-tick rate. Next trace regen caller cadence/leyline/Fae/Geas/hunger, gem lifecycle and Magitech interfaces, then spell-specific payment/effects and special-weapon acquisition. Most major-mod research remains unfinished; source access is available.

## Context checkpoint after Mahou mana slice

- Latest research content commit: `0065741a4d87e10d04aac7b35f942d62dbd64505`; pushed to the existing research branch/PR265.
- At that head: `python scripts/validate.py` PASS (159 active pins/294 indexed files/181 desired entries); `python scripts/validate_integration_sources.py` read-only PASS, zero baseline replacements; full branch whitespace check and docs-only/local-link checks PASS (excluding deliberate INDEX template placeholder). No runtime or gameplay tests. GitHub metadata checks were still running when inspected; server jobs skipped.
- Context checkpoint only, not completion of the overall research phase. Resume the same worktree and exact next steps above; do not repeat completed inventories or promote partial profiles. Summoning Rituals source-complete means unused-framework baseline only.

- Resumed slice: mana/circuit appendix now traces100-tick regeneration caller, full arithmetic/casts/default examples, sleep guard, coordinate ley score/dimension filter, Fae-spawn side effect/current loot source, exact circuit/gem recipes, consumed Attuner and gem charging from circuits. Magitech is ordinary circuit subclass with capacity/facing changes; no FE capability established. Next inspect spell-specific lifecycle/effect/payment and major weapon acquisition; Geas/William/Death Collection and Fae population/complete drops still partial. Latest fetched integration still differs only in source registry; pins unchanged.

- Completed Caliburn/Morgan acquisition slice: `mods/mahou-tsukai-caliburn-morgan.md`. Native lake11 paid growths/330 cost; >150 connected-water guard; enchanted Player-owned sword/5000 queued payment;83-tier Nether Star cap list, pre-payment offering discard/trigger; output projectile pickup/despawn; default Fun gate off; owned tamed wolf conversion/no extra drain; AbstractVillager growth400/repair30/adult0.2/baby1, dual-hands secret. Important open delegates: terrain tryChangeBlockState, FakeSkeleton/attribute simulation, specialTarget/SmiteEntity, MorganBall/Rage, packet/protection/cancellation. Next audit remaining exchange loops or these combat delegates; do not call whole Mahou source-complete.

- Completed exchange/life-drain tile slice: `mods/mahou-tsukai-exchange-life-drain.md`. Catalyst7 uniform types/50 perunit/ceil(N/2) original-batch loop verified javap, potential output reroll. Durability below-NORTH handler plus separate above callbacks, taxbrackets, cap counts iterations notmana, postdamagecheck/break gap. DrainLife5/20tick, targetcube10/immunities/name, damage2+fallback3.6 and reward regardless second damage boolean; reward health0.5 thenfood1 thenmana1;10-circle cap/payment-beforegate. Updated lake block-change helper with explicit immutable blocks/BreakEvent fakeplayer. Next high-value audit Contract/Geas lifecycle and DrainLife scroll, or other major weapon/artifact branches (Clarent/Emrys/Replica/William/Kodoku) before whole-profile source completeness.

- Completed Contract/Geas/boundary-scroll slice: `mods/mahou-tsukai-contract-geas.md`. Contract owncircles within10 cube/10 perparty/newpair/sequentialdebits/no rollback, overworldsaveddata mutualUUID and incomingplayerdamage break; Geasclothrecipe,30 paidbeforetoggle/list, species vsnamed vsitemstrings, onlyspeciescountsregen, pickup/attack hooks, ownerresolvedfireclear. DrainLife scroll useOn stationarydeployment with0 upfront and survivalconsumptionbeforequeued placement/no refund. Next research remaining majorartifacts Clarent/Emrys/Replica/William/Kodoku and spellfamilies; full selector/delegatedcombat/runtimeacceptance stillopen.

- Completed Clarent/Emrys/Replica acquisition-handler slice: `mods/mahou-tsukai-artifact-acquisition.md`. Clarent strengthenedmainSword+Dragonownedcloud2inflation/rightclick/replace; Emrys strengthenedoffhandexactStick/lightningevent/wholestackreplace; Replica exactvanillaShieldmain/blocking/strengthened/DEuses>90%cap/souls>=min100cap/Immunitybuff/armor>15, no directconversiondebit. SupportDE5uses/40, DeathEyes400/600ticks/lookdeathmob0.25+debt+revival, Immunity400/1200 alternatingdamageIDdouble/sametypecancel. HIGHEST Immunity can prevent normal Replica handler: route requires runtime event-order acceptance. Next artifacts William/Kodoku, remaining spellfamilies, exactadvancement declarations/emitter crosswalk and main-profile stalewiki corrections; fullcombat/teleport notdone.

- Main-profile reconciliation replaces outdated wiki-only rows for mana/gems/circuits/ley/Fae/economy/weapon routes with pinned evidence, corrects base1-per-game-tick misreading and powder-consumption timing, and closes historical “lake unknown” table row. Broader research remains incomplete.

- Completed William/Kodoku economy slice: `mods/mahou-tsukai-william-kodoku.md`. William strengthened signedbook→emptyLectern→dictionarycompletion→stylecost500×styleclamp, fame0,10 authorrecords, textauthorship/recreation; sequentialpage500ms<gap<120s distinctreader/altfilter, cloningmakesordinaryWrittenBooks notWilliam; carriedbest+worst, deployed100ticks sameownerinactivecircuitsallowed, negativecubeplayerdrain. Kodoku6ingredientrecipe inclwart/poisonpotato, unchargedmount, deathstateformula usescurrentHP, wormcapturehoststate, recoveritem, consumedfirepayout value×(8+ley) toactivecircuitsWITHOUTownercheck; excesslost. Next partials: William textsubscores/cacheeffectivecontents/theater, Kodoku badluck/loot/tool penalties; remaining53spellfamilies, guide/advancement emitter coverage and namedartifact combatdelegates.

## Resumed-session validation checkpoint

- Latest substantive head: `449a3fb7` (William/Kodoku); seven pushed commits since previous `e153ee53` checkpoint cover regeneration/storage, Caliburn/Morgan, exchange/Drain Life, Contract/Geas/scrolls, Clarent/Emrys/Replica, main-profile reconciliation and William/Kodoku.
- At449a3fb7: metadata/hash validator PASS (159 active mods/294 indexed files/181 desired selections); read-only Integration Preflight PASS with0 baseline replacements;19 changed files all under docs/mod-comprehension; local Markdown links PASS excluding deliberate INDEX placeholder; exactly61 unique addon recipe rows,28 registered focus rows and53 Mahou recipe rows; Mahou artifact SHA512 reverified; full branch whitespace PASS; clean worktree before this receipt.
- PR265 title/body updated for all research slices. GitHub metadata jobs were pending at inspection; server jobs were skipped. No merge, assembly, boot, quest/config/runtime edits or gameplay tests.
- Resume same branch/worktree. Next prioritize remaining Mahou spell-family lifecycle/payment/effects and guide/advancement emitter coverage; finish William scoring/cache/theater and Kodoku penalty delegates or move to another high-priority partial major mod once this Mahou evidence is reviewed. Appendices explicitly retain detailed open paths. Broader phase unfinished; no serious local-artifact blocker.

- Reset continuation moved beyond Mahou to whole-roster closure. Current baseline fetched: only AGENTS branch-retirement policy/docs and integration-source registry differ; research pins unchanged. Roots profile tracked blob was binary-corrupt (nibble-shifted prefix/damaged midtransition); recovered readable UTF8 and replaced table gap, SHA recorded in profile. Exact RootsClassic1.5.10 SHA1 matches. New pinned appendix inventories46 rituals/22components, all12effectfamilies/layouts/200tick lifecycle, lost recipeID on reload, sacrifice firstentity directhealth bug, healbudget ignores hurtboolean, current5tickTerra/15+15staff/13tick castguard. Next close22components/poweredstones/living+armor+CrystalStaff/rare mutations before source-complete; no gameplay acceptance.

- Roots second slice covers all22components with recipeinputs/basecost/effectboundaries,8poweredstones, CrystalStaff no ground/healthgate, armor potency vshealthtooltip mismatch, rare3mutations and matcher/dayTime bugs. File `roots-classic-pinned-spells-items.md`. Narrow sourcegaps to close: RunicFocus/ChargedFocus use acrossJAR, EngravedForceful hook; inspect remainingmiscfoodregistration only ifguide declaresprogression. Then review source-complete bar (runtimeclaimtests remain separate).

- Roots native source-complete closure: all-JAR field refs for Focus/ChargedFocus only registry/datagen/research; Forceful only registration/ritual/tooltip, no runtimeconsumer. ResearchManager46ritualreferences hasonebroken `ritual/engraved_blade` vsactual`ritual/crafting/engraved_crafting`; suppliedJEIprocedure. Main+2appendices upgraded source-complete with explicitbrokennativefeatures andclienttests separate. Next whole-roster priority: Occultism/NeoVitae/Ars and remainingdeep magic profiles; do not stay on minor Roots runtimeacceptance.

- Occultism pinned ritual slice complete: `occultism-pinned-rituals.md`, exact1.227.0 SHA512,158records/11types; defaultduration30 and bowl-specificcountertiming; gradualinputs/ordinaryinterruptionrefund/savedrecipeID; actualoutputnotspawn-egg, resurrectionownership, chicken-tamedfactorypassesfalse. Next240crusher/194miner/54crystallize14trade27fire data/condition audit, familiars/storage/Otherworldtools+chalk. Mainresearching.

- Occultism processing slice: all529records (240crusher194miner54crystallize27fire14trade), typedconditions/tier/time/multiplier/weightedresults; dataoutputscope rawDraconium present, no literalprotectedtrophies/Replica in194minerpayloads (recursiveTMMtags notclosed). PinnedWorker/Mineshaft insertionremaindersignored; redstone/Unusing/fortune/silk andtagBarrierplaceholder. Next SpiritFiredispatch, minersetupconfigs, familiars/Otherworld/storage closedfromexactJAR thenreviewmaincompletion. `occultism-pinned-processing.md`.

- Wholeprofile artifact receipt `COMPLETION-STATUS.md`:42linkedprofiles allhashmatched local,17416bundledrecipeJSONs;3sourcecomplete39researching (countsnotcompletion). Occultism familiar/toolappendix added17roles, owner/range/Curios capture/transferredringrelease/noaddrollback; blacksmith18/400shortcircuit/lastingot+9boundary; Fairy/Guardian revival/trades; SpiritFire whole-stack1:1discards original. ComplexfamAI/fusion/loot, Otherworldsharedhooks/storage exactclosure remain.

- Occultism storage/harvest appendix added: capacities/stabilizers exactJAR, remoteanyuse/noload/selectedCuriosfallback, OtherworldcollectBreakEvent, soul/headhooks; DragonGreed XPonly amp+1, Blacksmith whole-stone-stackdiscard forone2dura repairattempt. RemainingfamAI/visibility/storagefulltransactions stillresearching. Pass2 report `mahou-tsukai--roots-classic--emrys-lightning.md` traces twoexactJARs plusgeneratedtargetLightningBolt NeoForgeevent; candidatenativetestunrun.


### Totemic pinned finite inventory checkpoint

Exact 0.12.18 JAR decompiled to scratch `decompiled/pokefenn/totemic` (112 nonclient top-level classes). Added `mods/totemic-pinned-ceremonies-recipes.md`: all 14 ceremonies, six music types/caps/playing actions, startup/effect/reset/reload contract, all 31 recipe resources, Totempedia access and advancement OR locks. Main linked; stays researching. Next: native passive/carving/Medicine Bag and full Buffalo→Eagle→Baykok walkthrough, entities/support/KubeJS, reconcile obsolete source blockers. Staff survival cancel comes from TotemBaseBlock.attack, not just creative PlayerInteract hook. `performed_ceremony` fires BEFORE effect, not output evidence. No runtime changes/tests.


### Totemic native research closure

Main rewritten against exact 0.12.18 JAR; all mandatory cedar→Buffalo→Rattle→Eagle→Whistle→Baykok gates, 12 carvings, charged single-carving bag, all optional ceremonies, support blocks/renewable entities and KubeJS surface documented. INDEX/receipt now source-complete (four profiles; 38 remain researching). Important: bundled base/pole loot tables EMPTY, pick-block component item is not survival retrieval; corrected claim before commit. Medicine Bag is no GUI/slots, charges 400/20 to4800 near matching passive pole, drains80/80 open; Ocelot not portable. No runtime tests. Next continue remaining profiles in completion receipt; Occultism jobs/familiar complexity still open.


### Occultism worker method audit

Added occultism-pinned-worker-jobs.md from exact JAR; job/AI classes all extracted and CFR decompiled in scratch. Covers registered roster, smelter4recipe priority/no fuel/output remainder loss, cleaner pickup, transport sided discrepancy, manager saved orders/proxy search, crop-class harvest/free replant, lumberjack connected traversal/noarea bound/replant cost and time/weather lifecycle. ChangeWeatherJob constructs visual-only bolt but NEVER adds it to world: not Emrys route. Remaining parent gaps enumerated in appendix; not source-complete. Next inspect SpiritEntity/instruction tools, farmer/lumberjack task composition, weather config/defaults, dispatcher/menu transaction and complex familiar fusion, reconcile full guide walkthrough.


### Occultism calling-book controls

Appended exact owner/link/capture/mode/block controls and manager endpoint setup to worker appendix. `BookOfCallingItem.useOn` double addFreshEntity and unchecked payload clearing confirmed by javap; do not claim duplicate spawn or lossless relocation. Capture on already bound target occurs even when sneaking. Native worker noncalling interaction can tame untamed spirit; owner sneak opens job menu. Book death notification static map/delayed1200, not persistent ledger. Remaining tasks remain in worker appendix and parent.


### Occultism worker closure follow-through

Added farmer/lumberjack brain tasks, tagged first selection vs UNFILTERED incidental3-block item sweep, required deposit memories, geometry enum16/32/64, exact processing/time/weather defaults and leaf weather calls. Job method scope now broadly closed; remaining source queue focus dispatcher/storage transaction and familiar fusion/guide full progression, not redoing worker outputs. Native Clear sets zero durations; weather parent equality means zero cast-count config boundary.


### Occultism rare familiar branch audit

Familiars appendix now records Chimera born-with-Goat/detach not an invented fusion, growth/meat/size riding/attacks, Goat→Shub, support spawn and actual enemy helper. Helper compares distanceToSqr<10, only last hurtBy/hurt targets, no dedup. Headless6-part disabled rebuild consumes2Wheat+Hay+2rods+CarvedPumpkin and grants advancement but does NOT clear disabled or heal in this JAR; wholeclassreferenceschecked. Sharddeathserializationresetsdisabled separately. All familiar inner siblings extracted/decompiled to eliminate unavailable anonymous goal. Next remaining storage dispatcher/menu transactions and complete guide-stage reconciliation; parent stays researching.


### Occultism storage/menu transaction follow-through

Storage appendix now has insertion blacklist/remainder semantics, extraction first component variant vs availability sum, nonreserved machine order queue, BlockPos-only menu lock,8blocklocal vs remotevalidity, persisted grids and shift-craft/remainder/component boundaries. All nested storage classes extracted/decompiled. Remaining for sourcecompletion: ordinary recipe/guide-stage reconciliation, visibility/loot path, residual familiar helpers; do not redo finite inventories/worker methods. Runtime packet/concurrency/crafting acceptance separate.


### Occultism visibility/harvest and current checklist

Storage appendix now traces IOtherworldBlock tier max, goggles clientreveal vs serverloot (goggles not in harvesttier), natural Iesnium/Otherstone/logloot and rod analogue/config/staff lookup +horizontal bands. Parent current checklist reconciled closed inventories/worker/storage/rarebranches; historical notes retained. Parent remains researching: full ordinaryrecipe/chalk/materialguide-to-stagewalkthrough and residualfamiliarhelpers. Do not call runtime acceptance/sourceaccess a blocker.


### Occultism full resource and guide crosswalk

Added occultism-pinned-crafting-guide.md: all244 remaining ordinary/special recipe resources and230 guide entry parent/recipe-ID refs; ninecategories (not oldsix), exact Dictionary seedtag+book, earlybinding/chalk grids. Total931 data recipeJSONs=158+529+244. Excluded assets/emi/recipe/defaults viewerlist. Corrected all42receipt counts to data/ prefix; total17407, changed[('ars_nouveau-1.21.1-5.13.5.jar', '710', 709), ('enchanted-neoforge-1.21.1-4.2.7.jar', '118', 113), ('mahoutsukai-1.21.1-v1.36.31.jar', '34', 33), ('occultism-1.21.1-neoforge-1.227.0.jar', '932', 931), ('ProjectE-1.21.1-PE1.1.0.jar', '157', 156)]. Parent unknowns reconciled. Remaining full parentstagewalkthrough +dynamichelpers, no sourceaccessblocker.


### Occultism chalk/binding/essence teaching contract

Craftingguideappendix now records exact4tierdynamicbinding action (Dictionary+unboundBook, grid≥2×2), all15coloredchalk additives/SpiritFire, Void/Rainbow rites and unboundAfrit/Marid input/event vs actualentityoutput. Corrected main oldsixcategories/fruitbook/click-list-binding and ritual-duration ticks errors; processor tickcounts unchanged. Remaining sourceclosure needs actualunboundentityloot, Dictionaryremainder/itemregistration, full allstageguidewalkthrough and residualhelpers. Current parent heading explicitly reconciliationinprogress.


### Occultism early acquisition/essence closure

GuideBookItem returns copy as crafting remainder, binding Dictionary reusable/componentsretained. Foods exact Datura70%ThirdEye300+Hunger, DreamEssence1200+sideeffects, purifiedOtherworldEssence1200+Luck. UnboundAfrit/Marid exactloot uniformmin.7/.9 max1 are NOT dropchance: target NumberProvider.getInt rounds→1 both endpoints, SetItemCount uses getInt =>base1. Verified supporting target sources in qa-source existing generatedjar; not engine guessed floor. Need finalwalkthrough consolidation and remaining residualhelpers before parent sourcecomplete.


### Occultism Battlefield/capture audit

New occultism-pinned-devices-capture.md records soul/fragile/trinity/lamp capture and unchecked releasepayloadclear, Battlefield componentinputs/virtualhealthweapon/fuel/work/failure/roll math, outputremainderloss and two explicitbossloottables (WitherNetherStar; Dragon3Breath:1Egg). Important prior194minerabsence was SCOPED, not global. Ordinary/Fragile denyboss; Trinity separatetag absentnativeJSON, needeffectivetag beforeavailabilityclaim. Recipes/gates recorded; noapprovedfarm/firstbossbypass/runtimeclaim. OptionalWormhole/satchelcontrols remain (classes extracted/decompiled in scratch).


### Occultism optional device controls

Devicesappendix extended StableWormhole sneaklinkloadedproxy; EntityWormhole1compassslotdestinationpriority/Lodestone/HOME/RTP/Recovery/spiritUUID and fishingpull; native117slotsatchelvs36ritualsatchels; EnderSatchel online-linked/fallbackcurrent; T1/T2patternmaterialplacement and T2collect. Placement helper ignores underlyinguseOnresult, possiblepartial/noop not guaranteedatomicbuild. No newmod/runtime/approvalpolicy changed. Main finalstagewalkthrough/residualhelpers still open.


### Occultism build references and first worker

Craftingguideappendix now inventories all20multiblock footprints/layers/actualmatcherBOMs vsdisplayonlyfloor, foundationtagalternatives, tierbookdyegrids and concretefirstFoliotCrusher operation. All19nondebugbuildreferences linkedtoDictionarypentacles except storage_stabilizer_demo usesStorage. This closes patternmaterialgate gaps; avoidclaimallcolorsmandatory or bookcraft=essence. Remaining parentconsolidation still needed; no newruntimechange.


## Current continuation checkpoint after16415c05

- User request remains the full major-magic research phase, with regular commits. **Not finished.** Four/42 profiles are source-complete;38 remain researching. No serious source-access blocker. All exact artifacts local; no need to ask user to reattach JARs or repeat a go/no-go.
- This slice completed Totemic (14ceremonies/6instruments/31recipes/12carvings/fullroute) and substantially closed Occultism's finite inventories and method contracts. Occultism still researching pending parent walkthrough consolidation/residual audit. Do not claim all38 profiles complete from resource counts alone.
- Latest substantive head16415c05; all commits pushed to the same isolated research branch/PR265. PR description refreshed at766e7e6d; subsequent16415c05 adds20construction references. No merge/runtime changes.
- Last full local integrity/preflight receipt766e7e6d: UTF8 of research tree, changedlocal links,docs-onlyscope,diffwhitespace,repositorymetadata and read-only IntegrationPreflight all PASS;0baseline replacements. GitHub metadata checked earlier93b021de SUCCESS; server jobs SKIPPED. This checkpoint is only Markdown; no client/server/assembly/gameplay run.
- All42 artifact receipt recipe counts now require data/ prefix:17407 runtime JSON resources, viewerassets excluded. Occultism931=158ritual+529processing+244ordinary/special; Dictionary230entries/ninecategories;20multiblockrecords. Five earliercounts corrected (Ars,Enchanted,Mahou,Occultism,ProjectE), hashes unchanged.
- Next exact files: mods/occultism.md (still old broadpartialtables), occultism-pinned-crafting-guide.md (fullrecipes/guide/buildrefs, binding/food/essence), occultism-pinned-devices-capture.md (Battlefield,capture,wormholes,satchels), worker/familiars/storage appendices. Reconcile concrete mandatory tiers/optional endpoints to these references. Residual sourceaudit should identify actual major gaps, not manufacture a blocker from unrun gameplay.
- Important corrections: ritual durationJSON is counter/seconds tier1 default, not ticks; Dictionary seedtagrecipe and reusablecraftingremainder; binds withgridnotnameclick; goldchalk comesyellowimpure; unboundritual displaysessence but summonsenemy (nativeessencelootbase1 via engine ROUND semantics). Nativebook ninecats,notoldsource six.
- Highgatefinding: Battlefield explicit WitherNetherStar and Dragon3Breath:1Egg loottables. Earlier194miner no-literal-boss-output statement was scopedonlyminer. Ordinary/FragileSoulGemdeny #c:bosses; Trinityseparate denycheck,no native tagJSON; effectivepackcontributions/runtime acceptance unverified. Do not call approvedfarm/bypass. Report remains in device appendix.
- Preserve qa-source dirtycheckout and concurrentintegrationwork. Workdir research-magic,branch research/mod-comprehension-local-jars-20261116,basecb8e2dcab59cc625500b3ca0329df1511823610b. No subagents currently authorized. Scratch/classes/decompiled outsideGit at LocalAppData Temp tmm-magic-research-20261116. Regular commits continue.


### 2026-11-17 hourly continuation: remaining utility familiars

Hourly continuation automation continue-magic-mod-research is ACTIVE. No concurrent local changes. Closed Beaver snack/tree15set-and-abort, Cthulhu lapis1→1or1–4/strict60trade/light, Greedy finder-vs-magnet/filter/owner/advancement limits, Beholder Warden recognition/ray9damage/support-eat and helper range. Updated old family table placeholders. Next consolidate Occultism parent full progression and residual-system coverage from exact appendices; no artifact blocker or runtime change.


### 2026-11-17 Occultism native completion

Consolidated main into complete actionable tier/optional walkthrough and explicit guide/build/companion map; removed stale partial checklists. Added occultism-pinned-traders-partners.md after residual audit found Java-defined Wondering catalogue (115offers, vision-dependent randomizedpool) and DemonicPartner interactions not in recipeJSON. Added critical CursedHoney→Orange via PossessedBee kill, ResearchDust/Lime, GrayPaste/foundationdark, Witherite/Black, Cruelty/Brown routes; direct loot vs ritualdisplay separated. All major native subsystems now source-backed and no acknowledged mandatory teaching gap remains. INDEX/receipt now5source-complete/37researching. Runtime/client/effectiveconfiguration acceptance unrun. Continue core Ars+addons/NeoVitae/JAWR queue, not repeated Occultism inventories. Hourly automation remainsactive; no hardblocker.
