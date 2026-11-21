# Ars Controle1.6.16 — native guide, glyph acquisition, logic and load sensor

Exact `ars_controle-1.21.1-1.6.16.jar`,313828bytes; fullSHA512 `458c5a878a1418ffce46d1dd109f4ae23bcbe10bd7153edcaff8d1ab49ab3032f37977488eee7cecdb550cd99672f560e9d851818d6775d5d7e222632285d1a6` recomputed/matched packwiz. All97class files including inner classes CFR0.152 decompiled outside Git at `Temp/tmm-magic-research-20261116/controle-fresh`. Read all15recipe resources. [Parent](ars-controle.md) remains researching for five remaining device routes/controls/cost/failure and full pilot crosswalk; this is substantive glyph/sensor closure, not inventory-based completion. No runtime tests.

## Native guide access is present

`registry/ACDocumentation.addPages` subscribes to ReloadDocumentationEvent.AddEntries and registers **native Java documentation entries**, correcting earlier profile's unverified no-addon-page caveat. Craft parent Worn Notebook (Book+lapis-gem-tag item), right-click; see [parent guide audit](ars-nouveau-pinned-guide-glyphs.md). Under **Crafting** look for Warping Spell Prism, Scryer's Linkage, Temporal Stability Sensor, Warp Scroll Holder and Remote. Under **Items** look for Portable Brazier Relay, related to Ritual Brazier/Brazier Relay and ritual search tag. Crosslinks edit native Spell Prism and Dominion Wand pages. Recipes are attached to those entries; glyph pages use native effective glyph registry/tier and JEI glyph recipes.

Rendering/clientsearch remains untested, but actual native registration is source evidence. Supply concrete operation instructions in future quest text and point to these named pages rather than external README alone. No extra addon book item/currency exists in this source path.

## Exact acquisition routes

Use parent [Scribe/XP/glyph learning](ars-nouveau-pinned-guide-glyphs.md): select glyph, pay **XP points** and unit recipe ingredients, wait120ticks after ingredients complete, collect and right-click glyph. Mana costs below refer to glyph contribution, not total form/equipment/config spell cost. Book tier must accept glyph and disabled/perglyph overrides still apply.

| Glyph ID `ars_controle:` | XP points | Unit ingredients | Binary default tier/mana |
| --- | ---: | --- | --- |
| glyph_filter_not |27|Ars Allow Scroll+Redstone Torch|Tier1/0 |
| glyph_filter_or |27|Comparator+Redstone Dust|Tier1/0 |
| glyph_filter_xor |27|Comparator+Redstone Torch|Tier1/0 |
| glyph_filter_xnor |27|Comparator+Redstone Dust+Redstone Torch|Tier1/0 |
| glyph_filter_above |27|Allow Scroll+Feather|Tier1/0 |
| glyph_filter_level |27|Allow Scroll+Short Grass|Tier1/0 |
| glyph_filter_below |27|Allow Scroll+Cobbled Deepslate|Tier1/0 |
| glyph_filter_random |27|Allow Scroll+native Randomize Glyph|Tier1/0 |
| glyph_precise_delay |55|Manipulation Essence+Clock+Comparator|Tier2/0 |

The four filter classes inherit native AbstractFilter default mana0and AbstractSpellPart tierONE; Precise Delay explicitly tierTWO. Learn with an appropriate Apprentice book for Delay, parent tier recipe/layout in [casting companion](ars-nouveau-pinned-casting.md). Native Randomize Glyph is consumed as an ingredient of Random filter, not automatically learned for free; learn/obtain it through its parent glyph recipe if needed.

All six device acquisitions, for subsequent operation audits, are confirmed data rather than guessed README recipes. Five Apparatus recipes set keepNbtOfReagentfalse and SourceCost0; zero Source does not imply reusable pedestal inputs or zero personal casting cost:

| Device | Consumed reagent / ordinary grid | Unit pedestal inputs |
| --- | --- | --- |
| Remote |Dominion Wand|4ender-pearl-tag+4Popped Chorus Fruit |
| Scryer's Linkage |Scryer's Crystal|4ender-pearl-tag+4Popped Chorus Fruit |
| Temporal Stability Sensor |Clock|Eye of Ender+Source Gem Block |
| Warping Spell Prism |Spell Prism|4ender-pearl-tag+4Popped Chorus Fruit |
| Portable Brazier Relay |Brazier Relay|2ender-pearl-tag+2Popped Chorus Fruit+2Manipulation Essence+Nether Star+Wilden Tribute |
| Warp Scroll Holder |3×3grid with centerempty,8Sourcestone Slabs|NoApparatus |

Apparatus build/activation: [native processing](ars-nouveau-pinned-crafting.md). Portable recipe proves a late boss/NetherStar material gate even though SourceCost0; no reward or alternative route is approved that bypasses it. Item acquisition does not prove bound/configured operation.

## Adaptive filter grammar and behavior

`FilterUnary.NOT` consumes the following filter operand and negates its IFilter predicate. `FilterBinary` consumes next two filter operands: OR short-circuits after truefirst; XOR compares inequality, XNOR equality. Both advance SpellContext.currentIndex over operands so these aren't additional unconditional sequential gates. Unary allows operand augments via getAugments. Binary skips first operand's augments to locate second, but returns skip+1and reads second Random operand's augments from the first operand index in this binary. Therefore don't assume independent second-operand augmentation works identically; test before advanced examples.

`UnaryFilterValidator` demands a following AbstractFilter and rejects nested IAdaptiveFilter; `BinaryFilterValidator` demands two with modifier handling between, also rejects adaptive nesting. A missing/invalid runtime operand yieldsfalse; predicate exceptions report an error position/log and returnfalse. This is not an arbitrary Booleanexpression parser; no claim nested OR(NOT(...),...)is valid. Use a simple form→operator→operand(s)→effect arrangement, save/validate before casting, then demonstrate target that should pass and one that should fail. Native continuation/effect output remains the observable result, not owning glyph items.

Both adaptive operators store resolver and set Y/Random operand context before direct predicate calls. Native classes are shared glyph instances; this explains setup code but isn't a claim thread-safe/concurrent callbacks or complete cross-mod compatibility. Binary second-operand indexing/augmentation is a precise acceptance boundary, not permission to patch runtime during research.

## Height and random options

`FilterYLevel` captures `(int)(casterPositionY −1)` before base filter dispatch. Block predicate compares **integer block Y** to it; entity predicate compares `(int)entity.position().y`. Above/Below require Integer.compare1/−1and Level0. Java integer cast truncates toward0, including belowzero; these are not continuous eye-height, floor-for-negative or center-of-hitbox comparisons. Test negativeY/entityboundaries rather than teaching an exact physical plane from the label.

`FilterRandom.calculateChance`: amp0→.5;positivea→`1−.5/2^a`; negativea→`.5/2^(−a)`. Predicate accepts randomnextDouble≤chance. Amplify/Dampen allowed with local cost override0; direct filter resolution computes chance from SpellStats amp multiplier. Examples a1=.75,a−1=.25; random outcomes do not guarantee a particular batch count. Use repeated measured trials, not a quest that requires one luckypreselectedresult. Whole spell form/cost and book limits remain native.

## Precise Delay

`EffectPreciseDelay.sendPacket` returns if no remaining spell links. Otherwise duration=`1 << ExtendTimeBuffCount`; schedules DelayedSpellEvent with duration, or duration+1when prior delayed event exists in context, marks context delayed and queues server event, then sends nearby effect packet. Default augment limit ExtendTime20; local ExtendTimecostoverride0. Thus n0queues1tick, n1=2, n20=1048576ticks nominal, **not seconds**. Actual elapsed walltime depends on server ticks/lifecycle. Config overrides/large shifts and alreadydelayed chaining require tests; no unbounded-duration guarantee.

Teach optional safe effect after delay, compare noaugment versusoneExtendTime at known tick count, then consider practical timer control. It doesn't add its own mana contribution, but no claim fullcastfree or persistenceacrossunload/crash.

## Temporal Stability Sensor

`TemporalStabilitySensorBlock` supplies comparator analogue output. Server getter returns `(int)round(averageTickTimeNanos ×15 /50 /nanoPerMillisecond)`; client0. This is average tick **execution duration**, not TPS, playerlatency or a worldlagkiller. At50mscomputed15;25ms≈8. There is **no explicit0..15clamp in this getter**, so overloaded>50mscancomputeabove15before downstream comparator behavior. Do not promise calibrated0..15output or fixed thresholdwithouttesting.

Placement schedules blocktick10ticks; eachtickupdates comparator output neighbors then schedules another10at EXTREMELY_HIGH priority. A comparator and reversible shutoff are separate blocks/wiring; sensor itself doesn't stop farms. No Source/personal mana/fuel debit in this sensor class. Recipe Clock+EyeOfEnder+SourceGemBlock viaApparatusabove. Pilot sensor item task proves possession only; don't generate heavy lag merely to validate it during research.

## Quest directions and acceptance

Native guide→Scribe→one simple filter success/failure→optionalBoolean/Random/Delay branch is actionable; choose useful options rather than require all eightfilters. XPpluscommon redstone/recipe supplies are safe directions; no highvalueuniquegate reward. Sensor lesson: place comparator/read quiet output, wire reversible stop to an already-known optionalmachine and observe state; itemtask remains proxy. Glyphoperation needs supported detector/manualobservation separatefrompossession. Existingpilot covers Remote/Sensor/Portable acquisitions only; allglyphs and advanceddevices need teaching coverage after device audits.

Notrun: nativeNotebookpage/render; valid/invalid/incomplete/adaptivenested grammar, bothoperandaugments/Randomorder, Ynegativeboundaries, RNGdistribution, Delaychaining/eventpersistence; sensor comparatorabove15downstreamclamp/quietload/scheduledreload/redstonecontrol. No assembly/server/client/gameplay or runtime/config/quest edits. Remaining devicecontrols/typedcapability/range/Source/ownership/failure paths are next, so addon stays researching.
