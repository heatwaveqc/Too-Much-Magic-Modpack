# Ars Ocultas 2.6.2 — guide, recipes and sacrifice bridge

Exact `ars_ocultas-1.21.1-2.6.2.jar`,82,755 bytes; full SHA512 `4a673096a2d0b9a5a83aca2747a8a08211205ed40875b4c22061f774146d9d4fff9c872e2c1b6b59e5fef2d1bf3b3a08732f22743ade4e0a82ba08636dc701a5` recomputed/matched. All46classes including inner classes CFR0.152 decompiled externally at `ocultas-fresh`. Native Occultism1.227.0 artifact fullpin hash rechecked; selected GoldenSacrificialBowlBlockEntity/RitualRecipe and inner classes freshly decompiled at `ocultas-parent-fresh`. [Addon profile](ars-ocultas.md) remains researching for spirit worker/transfer routes and final walkthrough/pilot review. No runtime tests.

## Actual guide and feature registration

`DocumentationHandler` registers native Java Worn Notebook category **Ars Ocultas**, with Gem, Spirit Jars and Transmutation entries, recipes and localized pages. Craft parent Notebook (Book+lapis-gem-tag item), right-click; [native guide](ars-nouveau-pinned-guide-glyphs.md). This corrects old wording calling the active guide Patchouli. No standalone addon guide item and no dedicated Altar entry are registered by this method, so future altar instructions must be supplied locally with JEI and Occultism Dictionary references.

`IntegrationRegistrar.registerJarBehaviours` registers SpiritJarBehaviour for Foliot, Djinni, Afrit and Marid. registerGlyphs/registerSounds are empty; registerFamiliars is a helper without a call in the inspected init path. Dragon familiar class names alone do not establish a new acquired survival familiar. Do not invent an addon glyph/familiar tier route from unused classes.

## Four current recipe resources and one legacy file

Use parent [Apparatus/Imbuement walkthrough](ars-nouveau-pinned-crafting.md), with exact structure and activation from Notebook **Crafting** and JEI recipe IDs. All quantities below are unit ingredients unless repeated.

| Current `ars_ocultas:recipe ID` | Typed input and catalysts/pedestals | Output / Source |
| --- | --- | --- |
|spirit_attuned_gem|Apparatus Source Gem reagent; Conjuration Essence+Manipulation Essence+Demon's Dream Essence pedestals|1Occultism Spirit Attuned Gem;500Source,keepNbtfalse|
|altar|Apparatus Golden Sacrificial Bowl reagent;4Silver Ingots+Drygmy Shard+2Sourcestone+Mob Jar pedestals|1Sacrificial Altar;5000Source,keepNbtfalse|
|gold_transmute|Imbuement1Occultism Silver Ingot; reusable nearby Manipulation Essence+Magebloom+Spirit Attuned Gem catalysts|1Gold Ingot;2000Source|
|silver_transmute|Imbuement1Gold Ingot; reusable nearby Manipulation Essence+Demon's Dream Essence+Spirit Attuned Gem catalysts|1Occultism Silver Ingot;2000Source|

Apparatus consumes reagent/pedestal items. **Imbuement consumes primary ingot, not the nearby reusable catalyst stacks**; earlier profile's wording about consumed transmutation reagents was misleading. Both directions1→1, not a metal multiplication loop, but ongoing catalyst cost is different from consumable reagents. Passive Source/payment/remainders follow native chamber implementation; no throughput or effective balancing approval claimed.

Additional file is at **plural legacy** `data/ars_ocultas/recipes/craft_stabilizer_tier4.json`, not current singular recipe directory. It names Marid book activation, craft_marid pentacle, duration240, Tier3 stabilizer/Iesnium-block forge-tag/Eye of Ender/Spirit Attuned Crystal/End Rod/Shulker Shell/Dragon Breath and Tier4 output. This file's presence is **not proof a current1.21.1 recipe loads or an active addon upgrade route exists**. Receipt counts distinguish4current resources from1legacy file. Use [Occultism's native stabilizer route](occultism.md) and its actual current JEI/Dictionary recipe; do not gate a quest on this unproven legacy override or promise the forge tag resolves. Final source review should keep this limitation, rather than repeatedly asking the user to unlock a nonexistent recipe.

## Altar build and operation

Craft Altar by exact apparatus recipe above; it consumes a Golden Bowl, so obtain another bowl for the actual ritual. Place **Golden Sacrificial Bowl directly above Altar**; adjacency alone doesn't qualify. Follow the chosen native Occultism ritual's Dictionary/pentacle/circle/activation/other ingredients. This addon doesn't generate a valid recipe or skip every parent gate.

Right-click Altar opens its server menu. Altar holds27slots, Container max stack1; menu supplies27altar slots plus player inventory/hotbar and standard shift-move. ItemHandler capability registered from every queried side returns ItemStackHandler over same list, without a local per-slot1limit override; don't assume GUI/container max1 also constrains automation identically. Save/load serializes items; removal drops inventory. Full-output/drop/insertion persistence remain acceptance boundaries.

At gameTime divisible300, Altar checks block above for GoldenSacrificialBowlBlockEntity and its current RitualRecipe. This is global cadence (nominal15seconds at20TPS), not15seconds after a player click. AltarBlock/getTicker/BlockHelper don't add a side gate and AltarBlockEntity.tick has no explicit isClientSide guard; authority/client prediction must be verified, not called a purely server-only hook without qualification.

If sacrifice not fulfilled, it searches BlockPos.withinManhattan centered **AltarPos.above(3)** with extents10,6,10. Candidate must be MobJarTile containing LivingEntity accepted by current ritual.isValidSacrifice. On first valid candidate it checks native SourceUtil.hasSourceNearby at **Altar**,range10,5000; if true requests takeSourceMultipleWithParticles and calls bowl.notifySacrifice(null), then returns. It does **not kill, remove or clear the jar entity**. It doesn't inspect success return of the actual extraction after precheck; source availability change between check/debit is a failure boundary.

If no valid sacrifice caused a return, and item-use not fulfilled, it scans Altar slots in order for recipe.getItemToUse predicate. Matching stack plus5000Source precheck triggers extraction request and bowl.notifyItemUse(null), then return. It does **not shrink the matching stack**. An earlier valid sacrifice branch returns even with insufficient Source; item-use won't also complete in that same pass. When both waits apply, don't promise one5000payment satisfies both or both clear at once.

## Exact Occultism recipient meaning

Pinned1.227.0 bowl `sacrificeFulfilled` is !recipe.requiresSacrifice or sacrificeProvided; `itemUseFulfilled` similarly uses requiresItemUse/itemUseProvided. notifySacrifice and notifyItemUse simply set their flag, mark changed and server network dirty; passed null isn't dereferenced or used to consume/validate entity/item. Native bowl tick waits while either predicate unfulfilled. Thus the addon really can signal satisfied conditions from reusable matching objects; this is stronger source evidence than only README prose, but not approved progression/balance behavior.

Ordinary bowl living-death path requires player-caused death within squared distance64 and ritual.isValidSacrifice. Ordinary item-use path requires player event within squared distance256 and ritual.isValidItemUse. Altar bypasses those particular death/use events by directly setting flags; other parent recipe/pentacle/additional ingredient/duration rules remain. No ownership/player-attribution check appears in Altar's scan. Jarred sacrifice eligibility and jar capture permission are different predicates: a matching sacrifice isn't automatic authorization to capture any entity.

## Metadata, teaching and acceptance

Exact JAR metadata still declares Minecraft `[1.21,1.21.1)`, Ars `[5.3,6)`, Occultism `[1.220.4,)`. Keep declared range discrepancy visible; this continuation neither launches loader nor overrides the prior user-corrected decision to continue research. Earlier official-pack evidence belongs to old ledger and is not reasserted as a newly checked external current listing. No pin or metadata patch performed.

Future optional Altar lesson should explicitly name direct vertical placement, parent Dictionary recipe, reusable jar/item flag behavior, each5000Source request/cadence and distinction from normal sacrifice. No quest implementation or balance approval before deciding intended pack behavior. Gem/transmutation lessons can use modestXP/common study materials; avoid reward loops or gifting Tier4/Boss materials.

Notrun: client/loader range, native guide pages; recipe availability and legacy file; apparatus/chamber consumed-vs-reusable items; Altar bowlabove versus side, no recipe, valid/invalid jar LivingEntity, wrong/missing item, Source4999/5000/change-between-check-and-debit, both waits/300tick alignment; reusable object retention, automation slotlimit/reload/drop, claims/player credit. Remaining spirit/job/SoulGem routes keep profile researching.
