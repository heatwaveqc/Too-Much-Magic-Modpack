# Ars Nouveau - mechanics, full native route and research status

**Status: researching.** Native progression and major optional systems are now consolidated against thirty exact-binary companions. Eight-addon installed-scope review remains open after new Conflux pin; source completeness and actual gameplay acceptance are separate. This current profile replaces the contradictory historical survey (82glyphs/Patchouli absence/old caster limits/pilot-only-on-PR claims). Earlier evidence remains in Git history; use current companions for exact mechanics.

| Field | Current evidence |
| --- | --- |
| Manifest / mod ID | ars-nouveau / ars_nouveau |
| Artifact | ars_nouveau-1.21.1-5.13.5.jar; MC1.21.1/NeoForge |
| Exact pin | [packwiz](../../../mods/ars-nouveau.pw.toml); CurseForge401955/file9521184; SHA1cdc946c63e1eb676f0e29e4640335c0ce68d7de3 |
| Method / reviewed | Hash-matched local distribution resources, selected CFR0.152 decompilation with inner siblings and consequential javap checks;2026-11-18 |
| Native inventory | 709data recipe resources,85glyph recipes,24rituals; counts don't imply every optional recipe is mandatory |
| Current source baseline | Research basedcb8e2dcab59cc625500b3ca0329df1511823610b; latest fetched integration2f662b9e8b73cee47a1e23bf6d1f3510dd41d8ae; no Ars pin change |
| Quest chapter | Ars Nouveau; historical pilot covers base plus7addons; current source adds8th [Conflux](ars-conflux.md). [All49pilot-node source/ID/task/reward crosswalk](ars-nouveau-pilot-crosswalk.md). Files physically present in integration source; actual client/actions/claims untested |
| Earlier upstream source | baileyholl/Ars-Nouveau4d323fba9200c3293a4f4848cf1e0fe70beb7098 declares5.13.5; version alignment doesn't prove binary build identity and is superseded by direct artifact when differing |

## Player value and two separate economies

**Five strengths:** authored modular spells; Source-powered processing/enchanting; local creature labor/storage; ritual/world utility; optional gear, travel and remote controls. Personal casting spends **player mana**. Sourcelinks produce **Source** stored/routed for machines/rituals. XP-point glyph learning is a third cost. These are distinct typed resources; Source Jar isn't a personal mana refill container.

Three practical starting options: passive Imbuement first Source Gems; Novice Book/Scribe chosen utility glyph; ordinary crop garden with Agronomic and local Jar once materials allow. Advanced Apparatus, Drygmy habitat, portals and boss-linked gear aren't universal first-day unlocks. Three integration leads: Ars Ocultas/Occultism, Ars 'n Spells/Iron's, Ars Unification/Farmer's Delight plus Occultism. Both ends/addon exact routes remain to close; not client-confirmed bridges.

## Guidebook / walkthrough readiness

Craft **Worn Notebook** shapeless Book+Lapis-gem tag and use to open native Java documentation UI. It is not a Patchouli JSON dependency. Novice Book has alternate recipe consuming Notebook plus four iron tools; keep/recraft guide if choosing that route. Book+fourtools ordinary route avoids consuming guide.

Notebook main references: **Getting Started** (Spell Books, spellcrafting, Learning Glyphs, Source, Magical Crafting), **Glyph Index** by tier, **Source**, **Crafting** (machines/controls/helpers), **Ritual Index**, **Field Guide** (Archwood/Sourceberry/Magebloom), **Familiars**, **Items/Spell Casting/Armor**, **Enchanting**. Titles/build entries and exact recipes are mapped per companion below. JEI supplies effective ingredient/tag choices; quest text must supply actual actions and named page, not send player to external wiki.

Assign configured **Open Spell Book**, **Open Radial HUD** and familiar controls in Minecraft Options if unbound; don't require hardcoded C. Spellbook composition/casting and Worn Notebook documentation are different UIs. Inventory task can prove guide/book possession, never that a page was read or spell operated.

## Complete player progression / walkthrough blueprint

This is a practical teaching order, not an artificial dependency chain. Book Tier2uses vanilla-material upgrade; players needn't complete every optional machine/helper to unlock it. Native endpoint is usable chosen Tier3spell after Archmage; Ars remains open-ended afterward. Each row is a research quest handoff: direct XP plus useful fixed supplies, with honest detector scope. No quests are modified here.

| Stage | Concrete player actions / success | Teaching source | Honest task / reward direction |
| --- | --- | --- | --- |
| 0 Guide | Craft/open Notebook; locate relevant Getting Started page and JEI recipe. | [Guide/glyphs](ars-nouveau-pinned-guide-glyphs.md) | Notebook item only, reading manual; XP+ordinary early supply. Missing dedicated pilot node. |
| 1 First cast | Novice shapeless Book+Iron Shovel/Pickaxe/Axe/Sword. Hold, open spell editor, choose slot, Form->Effect/augments, name/save, cast at safe target and observe mana/effect. Native starter defaults Touch/Self/Projectile/Break/Harm configurable. | [Casting](ars-nouveau-pinned-casting.md), Getting Started/Spell Books | Novice inventory advancement isn't cast proof; XP+modest consumables. |
| 2 First learned glyph | Scribe's Table, use Book to select glyph; selection prepays XP **points**, supply displayed items, wait output, use completed Glyph to learn. Start with one useful choice, not85mandatory quests. | [85glyph recipes/learning](ars-nouveau-pinned-guide-glyphs.md), Crafting/Scribe's Table | Glyph output vs actual learned knowledge distinguished; XP+chosen ingredient direction. |
| 3 First magic material | Imbuement Chamber with recipe Lapis/Amethyst input; passive accumulation allows first Source Gem before generator. For Essences use Source Gem and recipe's3reusable pedestal catalysts in per-axis1cube. | [Crafting](ars-nouveau-pinned-crafting.md), Magical Crafting/Imbuement | Output possession only; XP+ordinary material, don't call Chamber acquisition processing success. |
| 4 Power/workshop | Craft Jar and chosen Sourcelink, trigger real input and observe fill. Agronomic natural crop/tree growth; Mycelial food; Volcanic fuel; Alchemical potion; Vitalic eligible events. Keep within exact consumer ranges; optionally bind Source Relay with Wand. | [Source](ars-nouveau-pinned-source.md), Source/category entries | Block tasks don't detect Sourceflow; XP+seeds/lever; bone meal isn't direct Agronomic income. |
| 5 Apparatus | Core directly below upright Apparatus, compatible axis/FACING. Pedestal inputs one per slot in per-axis3cube, reagent main hand on Apparatus; Source only for recipe requesting it; collect output. Extra nonempty pedestals reject exact count. | [192craft resources/geometry](ars-nouveau-pinned-crafting.md), Crafting/Apparatus; Dowsing layout tool | Acquisition vs real craft; XP+useful pedestals, no automatic success from reward. |
| 6 Renewable materials | Collect natural first Archwood pods/saplings/Sourceberries; replant supported pods. Apparatus anyseeds+4SourceGems ->Magebloom seed; farm age7flower->4Fiber. | [Plants](ars-nouveau-pinned-plants.md), Field Guide+JEI | Materials possession vs growth; XP+ordinary farming supply. Sapling growth doesn't generate first pod. |
| 7 Tier2 | Upgrade Novice with1Obsidian,3Diamonds,2Quartz Blocks,2Blaze Rods via exact tags/recipe. Use Apprentice/Mage Book to compose Tier2glyphs you've learned. | [Casting gates](ars-nouveau-pinned-casting.md) | Book advancement only; XP+practical low-tier material. Ten stored spells and ten base composition links are separate; don't teach old4/7/10ladder. |
| 8 Ritual operation | Craft/place Brazier, load chosen tablet, supply accepted augments before start, empty-main-hand activate; observe effect and declared Source/upkeep. Power can start/pause state; interruption isn't generic refund. | [All24rituals](ars-nouveau-pinned-rituals.md), Ritual Index/Performing Rituals+chosen entry | Tablet/Brazier item isn't operation; XP+arena/building supply. |
| 9 Wilden preparation | Unaugmented Summon Wilden chooses Hunter/Guardian/Stalker; obtain exact Horn/Spike/Wing (native loot mapping). In remote arena load all3as augments to select Chimera, activate and handle phase/vulnerability windows. | [Ritual/boss endpoint](ars-nouveau-pinned-rituals.md) | Kill criterion doesn't prove preparation/Source/material use; XP+food, no Tribute reward. |
| 10 Archmage endpoint | Complete encounter and collect actual Java-death-loot Tribute. Upgrade Apprentice with3Ender Pearls,2Emeralds,Totem,Nether Star,Tribute. Learn/select a chosen Tier3glyph, save/use spell and observe effect. | [Casting](ars-nouveau-pinned-casting.md), Spell Books/Glyph Index/JEI | Archmage inventory isn't Tier3operation; XP+safe thematic supply; no boss-gate item payout. |
| 11 Branch mastery | Choose useful helper/equipment/travel/storage/control route below; execute it, measure input/output/cost and demonstrate recovery boundary. | Native subsystem table | Optional choices need separate success evidence and meaningful XP/supply; not every recipe required. |

### Dependency graph and alternate routes

`Notebook -> reference`; `Novice + XP/material glyph learning -> personal spells -> Apprentice -> Archmage` is casting spine. `Chamber -> Source Gems/Essences -> Apparatus -> helpers/gear/materials` is workshop spine. `Brazier + Summon Wilden + three exact parts -> Chimera -> Tribute` supplies one Archmage gate, while vanilla Totem/Nether Star/other materials remain separate. Loot, Codex knowledge transfer and native alternate recipes can supply some items/knowledge; they don't automatically prove the taught operation or waive effective progression policy.

## Systems and full mechanics inventory

Thirty exact pinned companions contain native recipes, typed quantities, activation/control/build references, failure order, recovery and positive/boundary acceptance. They are the detailed how-to for each optional branch, not merely an item inventory.

| Subsystem | Exact pinned companion / current state |
| --- | --- |
| Guide and glyph acquisition | [Native documentation UI, five configurable starter defaults, all85 glyph recipes and player learning](ars-nouveau-pinned-guide-glyphs.md); guide is not dependent on Patchouli JSON. |
| Rituals | [All24 tablets/effects, augments, source order, event boundaries and placement](ars-nouveau-pinned-rituals.md); earlier remaining20-effects note superseded. |
| Source | [All five producers, five relays, capacity/search/transaction formulas and acquisition](ars-nouveau-pinned-source.md); formulas separate from measured rate. |
| Crafting worker | [Wixie cauldron item/potion workflow, mandatory binding and real sample selection](ars-nouveau-pinned-wixie.md). |
| Grove worker | [Whirlisprig habitat/progress, paid plant-drop generation and ignored overflow](ars-nouveau-pinned-whirlisprig.md); consumes Source, does not produce it. |
| Storage worker | [Bookwyrm/Storage Lectern, default8positions per worker/default30link, crafting and visual-only queue](ars-nouveau-pinned-bookwyrm-storage.md). |
| Transport worker | [Starbuncle item/berry/potion modes, filters, bed/road/passenger controls and partial-load boundaries](ars-nouveau-pinned-starbuncle.md). |
| Loot worker | [Drygmy exact acquisition/Henge/channel/loot/XP and Source timing](ars-nouveau-pinned-drygmy.md); effective protected/boss eligibility separate. |
| Familiars | [Six registered roles, Binding/script/player unlock/summon and ability methods](ars-nouveau-pinned-familiars.md); Jabberwog classes not core-registered. |
| Processing/enchanting | [Imbuement passive/accelerated operation, Apparatus geometry/consumption/failure order,96 sequential enchantment ranks and all192 audited recipe resources](ars-nouveau-pinned-crafting.md). |
| Casting/tier route | [Novice/Apprentice/Archmage recipes, ten stored slots vs ten composition links, validation layers, mana/reserve formulas and debit order](ars-nouveau-pinned-casting.md). |
| Armor/threads/Reactive | [Twelve armor layouts, two upgrade gates, nineteen thread recipes/effects, table controls/state boundaries and Reactive activation](ars-nouveau-pinned-armor-threads.md). |
| Amethyst worker | [Awakening/charm/home setup, conversion/growth/harvest/shard pickup and delivery](ars-nouveau-pinned-amethyst-golem.md). |
| Turret automation | [Four variants, programming/form gates, Source predebit, linked-provider and timer/rotation controls](ars-nouveau-pinned-turrets.md). |
| Travel | [Warp/Stable scroll recording, framed9000Source creation vs temporary builder, dimension/passenger/failure boundaries](ars-nouveau-pinned-warp-portals.md). |
| Archaeology worker | [Alakarkinos Sherd/Token/Charm route, hat/state machine, six weighted loot tables and post-job Source/output-loss boundaries](ars-nouveau-pinned-alakarkinos.md). |
| Remote viewing/casting | [Crystal/Scroll/Oculus acquisition, camera loading/dimension boundaries and Enchanter's Eye form/payment dispatch](ars-nouveau-pinned-scrying.md). |
| Pocket dimensions | [Planarium naming/acquisition, shared address, entry/spawn/return, generated geometry and projection refresh](ars-nouveau-pinned-planarium.md). |
| Spell records/knowledge transfer | [Parchment inscription, fifty loot Tome compositions/full-bar casting and Codex author XP/recipient unlock](ars-nouveau-pinned-spell-records.md). |
| Enchanter equipment | [Five exact tool recipes, effects-only inscription, Sword/Shield/Mirror/Gauntlet/Rod triggers and failure/repair boundaries](ars-nouveau-pinned-enchanter-tools.md). |
| Spell archery | [Bow/Crossbow acquisition, effects-only inscription, three augment arrows, prepaid launch/load and impact/charge boundaries](ars-nouveau-pinned-archery.md). |
| Curios/foci | [Nine equipped mana/movement/focus effects, twelve acquisition recipes, summon event and changed-target propagation](ars-nouveau-pinned-curios-foci.md). |
| Potion storage/delivery | [Jar/three Flasks/Crown/two Cannons/Diffuser, charge/payment and transfer boundaries; [all19effect consumers/18brewed variants](ars-nouveau-pinned-potion-effects.md)](ars-nouveau-pinned-potion-devices.md). |
| Inventory/spell detection | [Display Case strict thresholds/filters/stale state and Spell Sensor cast-vs-resolve pulse/occlusion](ars-nouveau-pinned-detectors.md). |
| Routing/control | [Projectile prisms, wireless Redstone Relay, Brazier Relay location/inventory and Arcane Platform](ars-nouveau-pinned-routing-controls.md). |
| Utility jars | [Void Jar pickup filter/mana/destruction and Jar of Light relocation/state boundaries](ars-nouveau-pinned-utility-jars.md); [Mob Jar capture/release and all24native special registrations](ars-nouveau-pinned-mob-jars.md). |
| Plant/material acquisition | [Archwood/sapling/first-pod route, Sourceberry worldgen/harvest, Magebloom seed-to-fiber and actual food effects](ars-nouveau-pinned-plants.md). |
| Field tools/runes/grates | [Dowsing locator vs layout use, physical Chalk rune programming/recharge and four grate transfer variants](ars-nouveau-pinned-field-tools.md). |

### Optional branches: practical goals and quest evidence

| Branch | Actionable endpoint / reference | Detection and reward direction |
| --- | --- | --- |
| Five main labor roles | Acquire appropriate charm, deploy/bind/home, supply exact Source/input and observe Starbuncle item transfer, Wixie supported craft, Whirlisprig plant output, Drygmy eligible loot or Bookwyrm storage. Matching worker companions give geometry/filter/remainder rules. | Charm inventory is a proxy; operation manual/real detector. XP+ordinary habitat supplies; no protected boss-farm assumption. |
| Other workers | Amethyst Golem home/convert/grow/harvest; Alakarkinos Sherd->Token->Charm->home/Sand/Gravel->loot. | Count actual block/output/Source changes, not animation. XP+ordinary stone/storage materials. |
| Familiars | Own eligible creature ->Binding script ->use script to learn ->familiar UI summon/toggle; six registered roles, knowledge persists separately from entity. | Custom familiar trigger separate from charm inventory; XP+small useful consumable. |
| Knowledge/equipment | Write Parchment; loot fixed Tome; author XP Codex->recipient missing glyph; choose armor/thread upgrade or effects-only Enchanter tool inscription. | Record/item acquisition doesn't prove effect. XP+blank supplies; don't give full-knowledge Codex/boss gates. |
| Potions/curios | Brew chosen family, Jar/Flask charges, Crown/Cannon or Diffuser operation; equip chosen curio/focus and observe native modifier/target change. | Inventory/tooltip vs applied effect/charge delta distinct. XP+bottles/farming supplies. |
| Travel/building | Safe recorded Warp/Stable route, permanent/temporary portal, separately named Planarium with recoverable entry/exit, controlled Flight ritual. | No item proxy proves dimension travel/landing; XP+ordinary building material; no dimension-policy bypass. |
| Remote/casting/control | Inscribe turret/Eye, source or mana as applicable, load Crystal/camera/pedestal addresses; detector signal and prism/relay route. | Pulse may prove event stage, not success/owner. XP+redstone/building supplies. |
| Utility | Configure Void Jar safely, carry/toggle Light Jar, Chalk Touch Parchment/recharge, grate known fluid transfer. | Irreversible loss/payment order must be taught; XP+mundane supply rather than rare destructive example. |

## Input-output economics and internal combinations

Personal mana, Source, XP, items, dropped entities, block state and potion units must remain explicit. Examples: Source Gem Chamber catalysts reusable while Apparatus pedestals consumed;100potion units=bottle/Flask charge and10=tipped arrow; Void Jar item destruction yields personal mana5peritem, not Source. Worker/charm/familiar/ISummon are different entity channels. First-cycle/post-job recharge and ignored insertion remainders are documented where present, not hidden behind a generic “requires power” statement.

Useful native combinations: Agronomic plus a real growing garden ->Jar->processing; Mycelial consumes surplus food, requiring actual sink/rate review; Bookwyrm interfaces with bounded inventories; Starbuncle moves products to supported consumers; Containment can present eligible entities to Drygmy with independent blacklist rules; worker-produced plants/Amethyst can feed crafting. None establishes protected loot eligibility, stable throughput or claims behavior without effective-pack tests.

## Addon scope and compatibility leads

The original seven are separately pinned profiles; current integration17a564d8 adds eighth [Conflux](ars-conflux.md), whose execution overlays and optional glyphs require combined review. Unification, Ars n Spells, Controle and Ocultas are source-complete for pinned installed scopes; three addon full-route audits remain. Existing pilot20addon nodes mostly select acquisition milestones and do not establish operation acceptance.

| Addon | Remaining full-route focus |
| --- | --- |
| [Ars Additions](ars-additions.md) | Advanced Wand, Warp Index/Ender Source, Wixie Apparatus, glyphs/rituals/structures/Codex and actual controls, not just3pilot items. |
| [Ars Controle](ars-controle.md) | **Source-complete** six devices/nine control glyphs, nativeguide/fullwalkthrough and actual pilot criteria; known native limitations and runtime acceptance remain. |
| [Ars Elemental](ars-elemental.md) | Exact binary guide/Urn companion now covers wand linking, fluid capability, payment and false Botania fallback. Four schools/foci/armor and other helper/device/biome routes remain researching beyond three fire-oriented pilot items. |
| [Ars Elemancy](ars-elemancy.md) | Combined Essences, armor and Elemancer shard route with actual spell mechanics/gates. |
| [Ars Ocultas](ars-ocultas.md) | **Source-complete** Occultism workers/jars/gems/Altar/material routes and current nativeTier4 versus legacy file; known failure/metadata limits and runtime acceptance remain. |
| [Ars 'n Spells](ars-n-spells.md) | **Source-complete** Loom/bind/cast, generic carriers, mana/payment/regen/utility and school/cooldown paths with exact Iron's endpoint; runtime acceptance remains. |
| [Ars Unification](ars-unification.md) | **Source-complete** installed Farmer's Delight Cut and Occultism Crush routes, acquisition, batch/priority/failure rules and pilot gaps; runtime acceptance remains. |

Existing [Botania mana bridge candidate](../interactions/botania-revived--ars-nouveau--mana-bridge.md) remains candidate; matching word “mana” doesn't establish native conversion. No absent optional targets, forbidden Create/storage/logistics or boss/ProjectE gates are silently enabled by research.

## Quest pilot, source state and open work

[Full49node crosswalk](ars-nouveau-pilot-crosswalk.md) verifies canonical/resolved/generated source IDs, criteria, rewards and current integration comparison. Pilot is source-present, PR74closed/unmerged; neither fact proves player acceptance. One orientation,31itemtasks,17advancementtasks;11native advancement conditions are inventory_changed. Dedicated Notebook acquisition/opening missing, many operation lessons use equipment proxies, and major optional routes outgrow original pilot. Exact advancement presence isn't proof of correct attribution/action credit.

Current research remains incomplete until current eight-addon routes and final combined stage/pilot reconciliation close. Native ordinary choices already have detailed route/build contracts; any discovered mandatory path gap gets targeted source check. Do not promote this parent because it has thirty companion documents or because a registry probe loaded49quests. Current project receipt:9/43source-complete,33researching and1stale after new Conflux scope.

## Evidence ledger and verification

Primary evidence is hash-pinned JAR: data recipe/loot/tag/worldgen JSON, native Documentation registry/en_us, exact classes listed in each companion, selected javap checks for consequential branches. No JAR, decompiled code dump or assets committed. Scratch path isn't durable evidence identity; filename+pin hash+internal path is. Earlier Prism instance was removed; recovered identical file to external scratch and verified SHA1. Source access isn't blocked.

Read-only research validation: Markdown UTF8/local links/docs-only scope/whitespace plus targeted resource tables and exact hash. `python scripts/validate_ars_quest_pilot.py` PASS49quests/7addons on research checkout, editorial repeated-payout report; no --jars whole-modset pass this run. Earlier metadata ad981968 and clean canonical-LF Integration Preflight eede4e69 are historical receipts, not new runtime testing. No assembly/server/client/gameplay run, configuration/quest/pin change or merge.

## Disposable-world test plan

Later authorized acceptance should follow each companion's positive/boundary cases: first cast/glyph/XP, processing/pedestal/Source consumption, book upgrade/Chimera actual Tribute, worker/familiar deployment and output, storage full/remainders, potion/equipment modifiers, transport/return identity, detector attribution/cancel and claims, reload/ownership. Record actual inventories/resources/world states and real operation credit/team personal claims. Native source formulas aren't measured rates or compatibility guarantees.
