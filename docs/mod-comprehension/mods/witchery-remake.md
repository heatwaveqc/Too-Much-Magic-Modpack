# Just Another Witchery Remake (JAWR) — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `witchery-remake` / `witchery` |
| Category / related content | magic; paired content addon: JAWR: Forbidden Magic (separate manifest ID and later profile) |
| MC / loader / pack target | 1.21.1 / NeoForge / `test/full-integration` current research base |
| Pinned artifact / hash | `witchery-0.5.39+mc1.21.1.jar`; Modrinth version `TWcJbsjg`; SHA-512 `a3d50e65bb494b4d9b709163a295ddcecf53c498314df473d6de55633a4a5ebe810b9a0b5672f97f41786c03af01ef3ac91f0262216e074887e75d6afc35141d` (`mods/just-another-witchery-remake.pw.toml`). Downloaded to temporary research storage and hash-verified; no JAR committed. |
| Source/documentation snapshot | JAWR is closed-source; exact pinned JAR data/assets and metadata inspected. Public issue/wiki repository [`mrsterner/witchery-issues`](https://github.com/mrsterner/witchery-issues) provides JSON schema notes and creator issue responses; it is not the mod source. |
| Guide/documentation version | Guidebook JSON and en_us localization extracted from the exact pinned 0.5.39 JAR. |
| Profile status | researching |
| Evidence reached | Exact JAR identity verified; native guidebook, recipe/ritual/loot/advancement/structure data and dependency metadata inspected. Full dedicated-server roster has previously co-loaded JAWR; no new client or world test in this pass. |
| Reviewed | 2026-11-15 |
| Quest ownership | Existing FTB chapter is one optional manual checkmark with 2 XP and paper; inspected but not edited. |

## Executive gameplay map

**What the player does:** JAWR remakes Witchery as a broad witchcraft and supernatural progression system. Players gather unusual plants and ingredients, build a nature-powered Altar, craft a Cauldron, Witch's Oven, Distillery, Spinning Wheel and other workstations, brew custom potions and infusions, draw chalk circles, perform staged rituals, and use a Brazier for incense and spirits. Separate native vampire and lycanthrope bloodlines progress through nine rank milestones each. Additional branches include Tarot, Symbology, Spirit World, familiars, poppets, covens, transformation cures and transportation. The exact 0.5.39 guidebook contains nine categories and 223 entries; its locked specialty categories use advancements to reveal pages progressively. [jar: SHA-512 above; `data/witchery/modonomicon/books/guidebook/**`]

**Why it matters in TMM:** JAWR is the intended native home for vampire and lycanthrope physiology after the separately installed TeamLapen Vampirism and Werewolves mods are retired. Those removals are design work in progress; their worldgen, raids, pets and factions are not being migrated. JAWR also has a direct Neo Vitae integration: its conditional guide page and compiled compatibility package add a Viteomantic Bloodstone Pendant that lets vampire blood-feeding use the wearer's Anima network. That matters to the pack's blood economy and the current vampire/skill migration plans. [docs: `docs/PROGRESSION.md`; jar: `integration/neovitae/NeoVitaeCompat.class`, `ritual/rite_of_bloodbinding` guide/recipe; docs: official 0.5.12–0.5.13 changelog]

**Quick start:** Break wild grass for Witchery seeds and grow Belladonna for the Guidebook recipe. Craft and open the Guidebook, then follow General → The First Steps. For the first Altar, grow Mandrake, use the Witch's Oven/JEI route to obtain Exhale of the Horned One, and make four Mutandis in the cauldron by adding Mandrake Root → Exhale → Egg. In pinned 0.5.39 the recipe displays 500 Altar Power but the cauldron crafting path does not check or debit it, so this first craft can bootstrap Rowan saplings and Rowan Wood for the Altar; this is a reported, bytecode-backed exact-version finding, not a TMM world test. Mutation targets/probabilities and the complete Jar/Oven input chain still need direct recipe/source review. Cauldron recipes use ordered ingredients, not arbitrary shapeless mixing. [jar: guidebook and cauldron recipe data] [issue: 0.5.39 #127]

### Player-value snapshot

| Five standout capabilities | Three early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Data-driven ritual magic; customizable brews and item infusions; two full supernatural bloodlines; Spirit World travel and dream mechanics; poppets/familiars/coven and Tarot toolkits | Oven distillation of useful ingredients; Altar-powered workings; cauldron brews for utility and mobility | Neo Vitae Anima-powered Bloodstone Pendant; Forbidden Magic's separate soul/lich branch; JAWR ritual item/entity/tag inputs as candidate links for pack progression |

### Subsystem coverage at a glance

| Subsystem / branch | Evidence | Progression completeness | Guide / JEI | Client validation | Remaining blocker |
| --- | --- | --- | --- | --- | --- |
| Core witchcraft ingredients and workstations | exact pinned JAR recipes + creator issue report | opening Mutandis → Rowan → Altar loop mapped; broader ingredient graph partial | General (32 entries), oven/altar/cauldron pages + JEI | not run | Audit Jar/Oven chain and mutation targets/probability; test workstation transactions |
| Altar power, chalk and rituals | exact JAR recipe/schema and issue report | structure and ritual model mapped; individual rites partial | Rituals (53), ritual recipe pages, JEI | not run | Verify altar supply, sequencing, radius and ritual outputs |
| Brewing, cauldron crafting and infusions | 23 brews, 10 crafts, 25 infusions in pinned JAR | systems mapped; ingredients and effects partial | Brewing (34), JEI | not run | Known power-cost bypass in cauldron craft path; see issue below |
| Oven / Distillery / Spinning Wheel / Brazier | exact guide categories/pages and recipe data | category inventory complete; full chains partial | General, Brewing, Brazier; JEI | not run | Measure yields, fuel/inputs and side outputs |
| Vampire bloodline 1–9 | exact guide entries and advancement data | narrative gates mapped; underlying Java grant conditions not source-inspected | Vampirism (20 entries), advancement-gated pages | not run | Exact rank conditions, resource caps, HUD and cures need client/source verification |
| Lycanthrope bloodline 1–9 | exact guide entries and advancement data | explicit milestone requirements in guide mapped | Lycanthropy (17 entries) | not run | Check one malformed/ambiguous page, transformation states, forms, food and cures |
| Spirit World / dreams / transportation | exact JAR guide data and recipes | branch list mapped; dimension entry and survival details partial | Spirit World (16 entries) | not run | Test entry/exit, death, return and bed/body behavior |
| Tarot / Symbology / Poppets / Familiar / Coven | exact JAR guide data | catalogs mapped; effects/risks partial | Tarot (24), Symbology (15), General/Ritual | not run | Determine costs, duration, ownership and multi-player edge cases |
| Neo Vitae bridge | exact JAWR JAR package/conditional guide and changelog; Neo Vitae profile | named contract and recipe located | Rite of Bloodbinding, gated by `neovitae` loaded | not run | Test exact Neo Vitae 1.1.53 with JAWR 0.5.39 |

## Guidebook onboarding and access

- **Book:** `witchery:guidebook` (Modonomicon). The exact shaped recipe is a book in the center, Belladonna Flower above, Ink Sac left, Feather right, coal-tag item below. This is craftable before obtaining an Altar, but requires those specific ingredients. [jar: `data/witchery/recipe/guidebook.json`]
- **First pages:** General → The First Steps is visible initially and tells players to break wild grass for seeds. The Altar page unlocks after reading the Witch's Oven entry. Cauldron unlocks after The First Steps. Specialized categories are gated by Witchery advancements: Brazier by the Brazier advancement; Brewing by the Cauldron advancement; Lycanthropy by the Wolf Altar advancement; Rituals by Chalk; Spirit World by its portal advancement; Symbology by Symbology; Tarot by Tarot; Vampirism by vampire bloodline 1.
- **Native progressive guides:** Bloodline pages `vamp_2` through `vamp_9` require the prior entry to have been read and the corresponding `witchery:vampire/N` advancement. Lycan pages `were_1` through `were_9` similarly require the prior page and matching advancement; the first also requires reading the Wolf Altar entry. The data advancements use `minecraft:impossible`, which indicates the mod grants these progression flags through its own code, not a normal vanilla trigger. The book is a progressive journal, not proof the player completed a step.
- **Guide coverage:** Nine categories contain 223 entries: General 32, Brewing 34, Rituals 53, Vampirism 20, Lycanthropy 17, Brazier 12, Spirit World 16, Symbology 15, and Tarot 24. The guide includes workstation and structure pages, recipes, images and specialty entries; JEI supplements recipe discovery.
- **Limits:** Several pages are narrative hints rather than exact checklists. The vampire bloodline is written as an account of events and does not state all programmatic trigger predicates. Some operational requirements and effects need confirmation through the actual GUI/world. The Guidebook entry's default `auto_add_read_conditions` is false; reading a page is tracked only where an entry condition explicitly requests it.
- **Later quest implication:** A future chapter should teach how to obtain/open the Guidebook, then separately demonstrate one completed operation per major system. A crafted Altar, Cauldron or ritual component does not prove that Altar Power was generated or that the recipe/rite succeeded. Do not silently make vampire or lycan pages mandatory for ordinary witchcraft.

## Core systems and input/output contracts

| System | Inputs and operation | Outputs / resource | Limits and evidence |
| --- | --- | --- | --- |
| Witchery flora / Mutandis | Search grass and grow seeds; use recipe-specific Mutandis for plant variation | Belladonna, Mandrake, Wolfsbane and other magic plants/ingredient sources | Exact drops, climate and rates need worldgen/loot audit. Mutandis power-cost issue below is particularly important because it is also a route to Witchery trees. |
| Witch's Oven | Burn supported items/logs for fumes and other outputs; collect recipe outputs | Fumes/essences and other items used in later crafts | Recipes define accepted inputs and outputs; it is not general-purpose smelting. Exact furnace/fuel/time and output probabilities need measurement. |
| Distillery | Process recipe-specific ingredients | Refined spirit/essence reagents | Outputs feed potions, infusions and rites; JEI plus Distillery pages. No generic item/fluid conversion. |
| Altar | Six blocks in a 3×1×2 arrangement per guide; surrounding nature increases Altar Power; consumers query it in range | Shared Witchery Altar Power for supported devices/rites | Natural-structure multiplier is not quantified in the guide. Power is spatially supplied, not a portable generic FE network. Altar augmentation data modifies output by configured block/state conditions. |
| Cauldron brewing | Water-filled, heated cauldron; add recipe ingredients in the recipe's defined order | Named brew/potion; effects depend on recipe and delivery form | 23 brewing recipes. Exact custom potion/cauldron inputs and draining are recipe-specific. |
| Cauldron crafting | Add colored/order-tracked recipe ingredients to a heated water cauldron | Ordered crafted outputs (e.g. 4 Mutandis) | Ten recipes in the pinned JAR declare `altarPower: 500`; an upstream report documents that the 0.5.39 crafting path displays but does not check/consume this cost. Exact issue below. |
| Cauldron infusion | Apply a recipe's infusion to an item with ingredients and Altar Power | Enchanted/infused item or Witchery effect | 25 recipes. This path differs from ordinary cauldron crafting; do not assume it shares the cost bug. Requires testing. |
| Ritual | Build JSON-defined chalk pattern, fulfill celestial/weather/coven/item/entity requirements, then run ordered outputs | Items, effects, entity changes/spawns, dimensions/blocks/commands | 57 ritual recipes. Requirements must precede outputs; outputs may run in parallel; Altar Power is consumed at output phase and may drain per second. Timeouts may fail requirements. Upstream public schema documentation and exact JAR recipe JSON support these fields. |
| Brazier / incense | Recipe-driven incense with a brazier and inputs; spirit summoning/passes | Area buffs, corpse effects or summoned spirits | 5 passive incense and 3 summoning recipes. Range, fuel and duration need client tests. |
| Blood pool | Vampire-specific resource filled through eligible blood feeding and modded sources | Resource for vampire actions/advancement; mod-specific HUD | Exact cap, drain rate, death/respawn, child feeding and direct mob values not fully mapped. Do not assume ordinary hunger or Neo Vitae EV are interchangeable with blood. |
| Werewolf form and progression | Trap and be infected by a werewolf at the Wolf Altar; complete pack advancement requirements | Human/wolf form, transformation-specific progression and features | Guide documents the first infection and nine milestones. Exact triggers, transitions, constraints and food behavior need world tests. |
| Spirit World / dream weavers | Recipe-defined brew/portal and dream items | Separate dimension/travel and sleep-state effects | Dimension data and pages exist; death, return, body persistence and multiplayer rules untested. |
| Tarot | Tarot-reading system and 22 Major Arcana guide entries | Card-associated player effects/fortune outcomes | Guide contains reading and named card pages; draw odds, duration, rerolls and stacking need audit. |
| Poppets / familiars / covens | Poppets use taglocks/materials; familiar rites use entity and ritual inputs; coven contracts recruit members | Protection, remote interaction, companion/effect or ritual power bonuses | Exact trigger, holder/owner, range, transfer and failure behavior varies per item/rite. Need boundary tests, especially multiplayer. |

### Ritual data contract in more detail

JAWR's creator-published JSON schema describes a `witchery:ritual` as a block pattern plus an ordered list of requirement/output/neutral events. Requirement events precede outputs; all requirements are evaluated before the first output. The output phase debits `altar_power` and then drains `altar_power_per_second`; output events may run in parallel. Recipe fields can also require night/day/lunar phase, weather, nearby coven count, named entity/sacrifice conditions, and ordered consumed items with timeouts. This is a flexible ritual engine with item, entity, world-state and command-style consequences. It is **not** an unconstrained arbitrary-item recipe or generic fluid/XP/FE port. [docs: `witchery-issues` README JSON Structures; exact JAR: `recipe/ritual/*.json`]

## Native vampire bloodline — guide mapped

The 0.5.39 Guidebook tells a nine-stage story. The entries unlock from vampire advancements, but their advancement predicates are `impossible` data triggers that the mod grants in code. Treat the tasks below as guide-stated progression intent until the exact release's runtime logic is tested.

| Bloodline | Guide-stated milestone | Notes / evidence |
| ---: | --- | --- |
| 1 | Become a vampire through a night-time rebirth rite, described as a demonic pact; the account mentions chicken blood collected with an Arthana and goblet, chalk, candles, skull, moon/night and offering blood to Lilith | First page is unlocked by `witchery:vampire/1`; the exact ingredient/layout page is absent from the narrative entry. The page shows an eight-candle image. |
| 2 | Fill the new thirst and take a first victim | Guide narrative, not an explicit numeric criterion. |
| 3 | Transfix villagers and drink at most half of each; account names five victims | Guide phrase signals feeding control; verify if five is the actual tracked count. |
| 4 | Continue feeding to grow stronger; the story mentions mastery over several nights and the world slowing | Exact required quantity/kill list is not encoded in advancement JSON. |
| 5 | Overcome sunlight vulnerability by collecting sunlight and deliberately suffering ten burns at night | Guide states the 10-burn demonstration; exact damage mitigation/counter needs test. |
| 6 | Withstand sun weakly, then kill twenty fire creatures (the story says blazes) to gain strength | Use guide title/translation and exact mob tags to resolve criteria. |
| 7 | Repeat the rebirth rite with Lilith and offer a red/blood-colored flower | Guide suggests Blood Poppy; item/event exactness requires recipe/code check. |
| 8 | Achieve flight and extend vampire mobility/domain | Guide narrative does not name the exact predicate, flight ability, or power cost. |
| 9 | Mesmerize and cage five villagers, feed without killing them, then give a wine glass of vampire blood to a new child | Separate Vampire Children entry documents child hunting and Blood Crucible return; test ownership and feeding. |

Additional pages cover Vampire Cage, Blood Crucible, Blood Poppy, Bloodstone/Sunstone/Batwing Pendants, Cane Sword, Sunlight Collector and Sun Grenade. These are branches/support tools, not all rank gates.

## Native lycanthrope progression — guide mapped

The first guide step is direct: trap a Werewolf in a Beartrap in front of the Wolf Altar and let it infect the player. Later pages specify the following nine milestones; the mod exposes those stage advancements through code-granted triggers.

| Bloodline | Guide-stated action | Major branch/output |
| ---: | --- | --- |
| 1 | In wolf form, kill 30 sheep; offer the resulting Raw Lambchops at the altar | Learn transformation, hunting and altar turn-in |
| 2 | Collect 10 Tongues of Dog from fallen wolves and offer them | Pack tribute / wolf combat |
| 3 | Build a Wicker Man and burn it to summon and defeat the Horned Huntsman | Named boss encounter; verify attack/loot/death attribution |
| 4 | Kill 10 hostile creatures with critical hits | Precision combat milestone |
| 5 | Travel to 16 locations and howl at each | Exploration/territory milestone; define unique-location persistence |
| 6 | In wolf form, tame six wild wolves with empty hands/presence | Pack growth; verify entity variant and taming rule |
| 7 | Hunt 30 Pigmen in the Nether | Dimension-specific combat gate; not a dimension-as-specialization category |
| 8 | Spill blood of a friend or innocent villager | Morally costly final transformation path; test source/credit, consent/AI eligibility and cure |
| 9 | Guide contains no `were_9` narrative stage in inspected localization; the entry itself exists and requires advancement `werewolf/9` | Missing/blank final-stage text is a documentation defect candidate. Need inspect runtime trigger and guidebook in client. |

Support pages include Wolf Forms, Moon Charm, Night Howl, Pack Summon, Spreading Lycanthropy and Horned Huntsman. The exact form change/ability costs and overlap with native vampire state are not established by the data-only guide.

## Other major content branches

| Branch | Guide/source inventory | What it adds / current knowledge |
| --- | --- | --- |
| Witchcraft basics | General: ovens, cauldron, altar, distillery, spinning wheel, Hobgoblins, Owls, taglocks, poppets, nature and materials | Core ingredient economy and utility machines; first-loop item graph needs full recipe dependency audit. |
| Brewing | 34 guide entries, 23 cauldron-brew recipes, 25 infusions, 9 distilling, 11 oven-cooking, 9 spinning recipes | Custom potion effects, item anointments, mobility, utility and specialized materials. |
| Rituals and curses | 53 pages and 57 ritual JSON recipes | Chalks, teleportation, biome/weather/day changes, curses/removal, rites, familiars, spirits, entities and item transformations. Require operation-specific instructions. |
| Brazier | 12 pages, including 5 passive incense and 3 spirit-summoning recipes | Buffing, corpse functions, summon Banshee/Poltergeist/Spectre. |
| Spirit World | 16 pages with Spirit Portal, Nightmare, Dream Weaver, sleep brews and dreamweaver variants | Separate-world/oneiric branch; high-risk body, death and return behavior needs testing. |
| Symbology | 15 named powers such as Kindle, Water Call, Light Eater, Earth Bore, Propulsion, Mend Flesh and Wrest Free | Player-usable spell symbols; exact activation/charges/cooldowns need client research. |
| Tarot | 24 entries: reading plus Major Arcana | Divination and card effects; odds and persistence need test. |
| Nature / crafting | Mutandis plant variation, altar augments, infusions, brew crafting, Hobgoblin trades, familiars and poppets | Data-defined recipes and structures. Source includes worldgen/loot; no equivalent to TeamLapen village raids/pets is part of this mod-removal target. |

## Pack dependencies and compatibility

### Required library closure

The exact JAWR 0.5.39 JAR declares Minecraft 1.21.1 and NeoForge `>=21.1.235`; it requires Kotlin for Forge `>=5.10.0`, Curios `>=9.3.1+1.21.1`, Modonomicon `>=1.21.1-1.120.1`, and Lithostitched `>=1.8.0`. TMM pins NeoForge 21.1.265, Kotlin for Forge 5.12.0, Curios 9.5.1, Modonomicon 1.123.0 and Lithostitched 1.8.1, so every declared minimum is met. This is dependency-metadata compatibility plus prior server co-load, not client behavior proof. [jar: `META-INF/neoforge.mods.toml`; pin: matching `mods/*.pw.toml`]

**License metadata discrepancy:** Modrinth project metadata lists LGPL-3.0-only, while the exact JAR's `META-INF/neoforge.mods.toml` says `ARR`. The pack downloads the mod from its host and does not bundle its JAR; this profile does not resolve which declaration controls downstream redistribution. Record as upstream metadata inconsistency, not a confirmed violation. [docs: Modrinth project page; jar: metadata]

### Source-supported Neo Vitae integration

- JAWR's exact JAR contains `dev/sterner/witchery/integration/neovitae/NeoVitaeCompat.class`.
- The conditional “Rite of Bloodbinding” Guidebook page appears only when `neovitae` is loaded. Its guide says the Rite empowers a Bloodstone Pendant so it taps the wearer's Anima. Inputs listed by the guide: normal Bloodstone Pendant, Drop of Luck, Weak Blood Shard, Blood Pearl, Necromantic Stone and Nether Star; the recipe is `witchery:ritual/rite_of_bloodbinding`.
- Official JAWR changelog says 0.5.10 fed from Anima when using the vampire drink ability with a blood orb; 0.5.11 changed the bridge so vampires feed from a Neo Vitae orb/altar rather than Anima directly; 0.5.12 added the empowered pendant that replenishes blood from Anima; 0.5.13 added support for the remove-curse ritual to clear vampirism/lycanthropy. In 0.5.39, the compiled compatibility package and gated recipe/page remain in the JAR.
- TMM pins Neo Vitae 1.1.53. The source/guide proves intended integration exists; only the matching in-world test can establish how vampire blood, orb/altar and Anima interact at this exact pair of versions.

See the [cross-mod vampire blood report](../interactions/neovitae--witchery-remake--vampire-blood.md) for the typed claims and verification matrix.

### Known integration/test issues

1. **Confirmed cauldron Altar Power bypass in the exact pinned version (progression/economy issue).** The JAR's ten `cauldron_crafting` recipes all declare `altarPower: 500`, but the public report for JAWR 0.5.39 inspected the binary implementation and reproduced Mutandis without an Altar; its code inspection says that the crafting path neither checks nor consumes the stored power. The other nine affected recipes listed in that report are Binding Chalk, Drop of Luck, Golden Chalk, Infernal Chalk, Mutandis Extremis, Mutating Sprig, Nether Wart, Otherwhere Chalk and Pentacle. Creator replied that it was fixed in development, not that the pinned 0.5.39 release was republished. This is a reported/bytecode-confirmed release issue, not a test performed by this pass. It may bypass intended progression and cost; Mutandis in particular may be needed to get Rowan saplings for the Altar, so do not blindly enforce the cost and create a circular opening gate. [JAR recipe files; official issue #127: https://github.com/mrsterner/witchery-issues/issues/127]
2. **Rite of Binding recipient bug is reported on a different game version.** Issue #130 reports a two-player case in MC 26.1.2: the familiar can bind to the first nearby player rather than the ritual initiator, consuming ingredients and 8,000 Altar Power. It is an upstream report and not evidence of a 1.21.1 failure; retain as multiplayer boundary test if reproducing on TMM. [https://github.com/mrsterner/witchery-issues/issues/130]
3. **NEOVITAE bridge has no pairwise client test.** Its presence is binary-confirmed and versions meet metadata, but a successful dedicated-server startup does not prove vampire blood refill, Anima or empowered-pendant operation. See Neo Vitae profile.
4. **Client-side report inventory should be version-filtered.** Other open issues describe a 0.5.39 JEI Altar Power text color problem on 26.1.2 and familiar model/ownership problems. Do not label them 1.21.1 defects without exact-version reproduction.
5. **License declarations differ** between project metadata and pinned JAR metadata as above.
6. **No demonstrated server startup incompatibility:** JAWR, Forbidden Magic, Neo Vitae, Curios, Modonomicon, Kotlin and Lithostitched are already in the B8 server roster; full-roster startup passed. Native mechanics and the client remain unverified.

## Complete player progression / walkthrough blueprint

This covers major mandatory onboarding and high-level branches; it does not make every recipe/ritual mandatory. Completion remains `researching` until the first ingredients, operation, each nine-stage species track, and key branch recipes are reconciled with recipe IDs and observable outcomes.

| Stage / branch | Player-facing action and reference | Observable success separate from item ownership | Coverage / open research |
| --- | --- | --- | --- |
| 0. Find first plants | Break wild grass for seeds; grow Belladonna for the Guidebook and Mandrake for the first Mutandis batch. Read General → The First Steps. | Exact seed and crop yields observed | Route/action is documented, but drop probabilities/biome distribution and client harvest remain unaudited |
| 1. Make Guidebook | Craft `witchery:guidebook` using book + Belladonna + Ink Sac + Feather + coal-tag item in the shown pattern. | Book opens; General category readable | Exact pattern source-confirmed; client UI not tested |
| 2. First Mutandis batch | Make a Jar and use the Witch's Oven/JEI route to obtain Exhale of the Horned One; fill/heat the cauldron, then add Mandrake Root → Exhale → Egg. The 0.5.39 recipe declares 500 Altar Power and outputs 4 Mutandis, but the crafting code path does not enforce or spend that power. | Four Mutandis produced; no Altar required for this declared-cost recipe in the reported release behavior | Ingredients, order and output are exact-JAR/report backed; full Jar/Oven chain, timing and TMM reproduction remain open |
| 3. Rowan bootstrap and first Altar | Use Mutandis on eligible plants until Rowan Sapling is obtained; grow and harvest Rowan Wood; craft the 3×1×2 Altar and Cauldron. Surround the Altar with valid nature inputs and confirm its stored power. The power-cost bypass is what avoids a first-Altar deadlock in pinned 0.5.39. | Rowan tree yields wood; Altar stores power from valid nature sources | Mutation table/attempt odds and nature range/formula need exact audit; Altar operation untested |
| 4. Brewing/crafting | Fill and heat cauldron; add ingredients in recipe order; perform one brew, one crafting recipe and one infusion as separate operations. | Correct output/effect and observed power cost | Note cauldron cost mismatch for ten recipes; exact path tests required |
| 5. Chalk/ritual entry | Make appropriate chalk and draw a guide/JEI pattern; satisfy time, weather, item/entity and coven conditions in order. | Ritual enters output phase, debits altar, performs requested effect | Need representative cheap rite, wait/timeout case, boundary tests |
| 6. Utility branches | Choose a useful route: poppet protection, familiar, Brazier, Tarot/Symbology, brew mobility or Spirit World. | Activate protection/effect or complete travel and return | Branches optional; no requirement to complete every page |
| 7. Native vampirism | Follow Guidebook Vampirism pages when the player chooses to become a vampire: transformation rite, initial feeding, controlled feeding, sun-related powers, rebirth/flower stage, flight, and child/cage support. | Each code-granted rank advancement fires; blood meter/resource changes match the action | Exact rank conditions, costs, blood cap, forms and cure conditions need code/client tests |
| 8. Native lycanthropy | Wolf Altar infection; follow the guide's sheep, dog tongues, Horned Huntsman, critical kills, territory, tame wolves, Nether hunt and sacrifice stages. | Correct advancement fires and form/altar state changes | `were_9` entry has no text in en_us; exact trigger and transformation tests required |
| 9. Cross-system decisions | If Neo Vitae installed, consider Anima-powered pendant; treat blood/vitae transfer as a separate system and read its conditional guide. Use Remove Curse for curing where criteria are met. | Empowered pendant and resource changes observed; cure removes the intended state only | Exact JAWR 0.5.39 × Neo Vitae 1.1.53 test pending |
| 10. Advanced branches | Explore Spirit World, dreamweavers, infusion variants, altars/augments, teleportation, poppets and covens using the book/JEI. | Each chosen branch performs its intended world/item/effect operation | Detailed recipes and multiplayer edges remain partial |

## Disposable-world test plan

| Test | Positive and boundary cases | Expected observable | Performed? |
| --- | --- | --- | --- |
| Guidebook / unlocks | Craft exact book; inspect with and without root advancement; trigger exact specialty unlocks | Correct recipe and readable/unlocked categories; no claims from book possession alone | no; exact JAR data only |
| First-source circularity | Fresh world: obtain Rowan by ordinary generation, Mutandis, and any other supported sources; build altar; test Mutandis with zero/500+ Altar Power | Identify a valid opening route and whether the pinned cauldron crafts for free despite declared cost | no; upstream issue report has binary analysis, not our playtest |
| Cauldron costs | Run all ten `cauldron_crafting` recipes with no altar, distant altar, local empty/full altar; log inputs/output/power | Determine whether each checks/consumes exactly its declared 500; compare with infusion/brewing paths | no |
| Altar power | Fixed altar and environmental plant layout; vary flowers/trees/distance, verify producers/consumers and power debit | Establish range/formula and valid operation; don't assume nearest-block UI is exact | no |
| Ritual engine | Valid layout/requirements; wrong chalk; missing ingredient; wrong order; timeout; enough vs insufficient altar power; one parallel output | Valid path succeeds; invalid setup visibly waits/fails without consuming unrelated outputs | no |
| Vampire milestones | One player completes each guide action; measure blood pool before/after and advancement flags; boundary cases for half-feed and sunlight | Bloodline progression and damage/fed amounts are reproducible and tracked once | no |
| Werewolf milestones | Infect through Beartrap/Wolf Altar; test sheep count/Raw Lambchops, Horned Huntsman, 16 unique locations, wolf taming, Nether Pigmen and final sacrifice | Each guide action grants exact next rank, without duplicated/incorrect entity credit | no |
| Curse removal | Bind Taglock to target; perform daytime Remove Curse; test with vampire only, lycan only, both and unrelated curse | Only intended oldest/supported curse removed, no unintended reset | no |
| Neo Vitae bridge | At exact pins, compare normal pendant vs empowered pendant; vampire consumes Neo Vitae orb/altar; test empty/full Anima and unavailable Neo Vitae case | Blood is filled by documented route and no resource is duplicated or cross-drained unexpectedly | no |
| Multiplayer binding | Two players at familiar rite; initiating player vs closer/first-query bystander; disconnect initiator | Familiar goes only to intended initiator; failed owner case fails safely | no; issue #130 is 26.1.2 only |
| World/Spirit World | Fresh chunks, Tectonic and structures; enter and return, die/disconnect/rejoin | Structures/portal and player body/return work; no worldgen or persistence regression | no |

## Evidence ledger

| Claim | Source / exact immutable evidence | What it establishes / limit |
| --- | --- | --- |
| Selected release | `mods/just-another-witchery-remake.pw.toml`; Modrinth version `TWcJbsjg`; SHA-512 recorded above | Exact pinned artifact identity; local file hash equals lock |
| Exact JAR data | `data/witchery/modonomicon/books/guidebook/**`, `assets/witchery/lang/en_us.json`, `data/witchery/recipe/**`, `data/witchery/advancement/**`, `data/witchery/loot_table/**`, `data/witchery/structure/**` in matching pinned JAR | Recipes, book pages, tags/data, advancements and worldgen resources; class-code behavior not inferred unless separately documented |
| Dependency declaration | `META-INF/neoforge.mods.toml` in pinned JAR | Min versions and runtime required mods; pack metadata meets each minimum |
| Ritually ordered JSON schema | https://github.com/mrsterner/witchery-issues repository README, “JSON Structures” | Creator-maintained format explanation; not an exact implementation source |
| Current release/version history | https://modrinth.com/mod/just-another-witchery-remake/version/0.5.39 and official `witchery-issues` issue repo | 0.5.39 additions/fixes and current creator responses; upstream issue reports not TMM runtime tests |
| Altar power bypass | https://github.com/mrsterner/witchery-issues/issues/127; exact pinned `recipe/cauldron_crafting/*.json` | Upstream binary analysis/reproduction plus data costs; creator says fixed in dev, pinned release not confirmed fixed |
| Multiplayer familiar assignment | https://github.com/mrsterner/witchery-issues/issues/130 | Upstream report reproduced in 26.1.2; not evidence for 1.21.1 |
| TMM dependency pins | `mods/kotlin-for-forge.pw.toml`, `curios.pw.toml`, `modonomicon.pw.toml`, `lithostitched.pw.toml`, `neovitae.pw.toml` | Pack versions satisfy the JAWR metadata minimums; does not prove UI or API pairwise behavior |
| TMM server co-load | `docs/COMPATIBILITY.md`, B8 dedicated server result | Full-roster startup passed; no player-world or client behavior claims |
| Existing quest | `config/ftbquests/quests/chapters/just_another_witchery_remake.snbt` | One optional checkmark; not a real progression walkthrough |
| Retirement design | `docs/PROGRESSION.md`, `docs/TEAM-LAPEN-RETIREMENT-TARGET-2026-11-15.md` | JAWR owns future native vampire/lycanthrope physiology; TeamLapen content retirement still pending code/runtime migrations |

## Opening resource loop — Mutandis to the first Altar

This is the currently supported starting route for the pinned release. It resolves the circular gate that the broad walkthrough had left open, while separating the exact recipe data from unmeasured world behavior.

1. Break wild grass for Witchery seeds. Grow Belladonna for the Guidebook; grow Mandrake for Mutandis. The guide's General → The First Steps page is the in-game entry point. Seed/drop rates are not yet measured.
2. Craft a Jar and use the Witch's Oven recipe shown by JEI/its General pages to obtain Exhale of the Horned One. The source audit has not yet enumerated the exact Jar recipe, accepted oven fuel, cook duration or side outputs, so consult the live in-pack recipe and treat those values as open in this profile.
3. Fill and heat the Cauldron. Add **Mandrake Root → Exhale of the Horned One → Egg** in order. The exact pinned 0.5.39 Mutandis recipe declares 500 Altar Power and produces four Mutandis. The public creator issue documents the exact recipe and a binary inspection: the regular cauldron-crafting completion path does not query or debit Altar Power, and the reporter reproduced the output without an Altar. The creator acknowledged the issue as a 1.21.1 report and said it was fixed in development, then noted no rush; no published fix is established for this pin. [exact JAR data; creator issue #127]
4. Apply Mutandis to eligible plants until a Rowan Sapling is produced, grow the tree and harvest Rowan Wood. The issue report identifies Mutandis as the means to obtain Rowan and the Altar recipe as requiring Rowan. Which plant targets are eligible, success chance, per-use consumption, blacklist and whether the tree can spawn naturally are still unverified.
5. Craft the Witchery Altar from Rowan Wood and follow General → Altar for the 3×1×2 layout. Use its nature-source guide and JEI to charge it. That turns later Altar-dependent systems into an actual progression branch.

**Boundary:** The declared 500-power display remains visible in JEI/guide data even though the reported 0.5.39 code path does not enforce it. Do not teach it as a working power cost, and do not infer all ten recipes have been individually reproduced: Mutandis was the in-world reproduction; the other nine are affected by the same inspected craft path. [creator issue #127]

**Newer release notes:** Modrinth lists 0.5.39 additions for a boss entity tag and Lilith's garlic cure/enchant interactions, alongside fixes for cauldron item drops and other handling. These are release-note claims; the exact in-game recipe/trigger details are not yet integrated into the source-mapped content inventory. [docs: Modrinth release 0.5.39]

## Unknowns and next research

- [ ] Audit all ten cauldron crafting recipes and all altar-consuming infusions/rituals; track power costs separately from code enforcement.
- [ ] Resolve ordinary Witchery starting materials, especially Rowan wood/Mutandis, and all alternative renewable sources before any cost is enforced.
- [ ] Map every recipe dependency from Oven → Distillery → Altar/Cauldron → Chalk/Ritual and major outputs with exact item IDs; current profile gives categories and key routes, not the entire recipe graph.
- [ ] Extract exact Vampire and Werewolf rank predicates from a matching source/bytecode inspection or game client; test resource caps, nourishment, cures, dual-state behavior, form-specific attributes and rejoin synchronization.
- [ ] Review every ritual JSON/Guidebook pairing for requirements, consumption, output and side effects; capture day/night, weather, coven and timeout gates.
- [ ] Verify Neo Vitae compat against exact TMM pin and evaluate JAWR: Forbidden Magic in its separate profile.
- [ ] Check Spirit World/travel and world generation alongside Tectonic in a disposable world.
- [ ] Inspect game-client guide rendering and determine why the `were_9` translation entry is blank.
- [ ] Compare exact JAR's `ARR` license field with the project's LGPL metadata before any redistribution plan.
- [ ] Reconcile the one-checkmark FTB chapter against this walkthrough when quest work is authorized; leave it unchanged in this knowledge-only pass.

## Revision notes

- 2026-11-15 — Started exact JAR profile for 0.5.39; confirmed 223 guide entries and category/advancement gates, mapped nine-step bloodline guides, inspected recipe and dependency data, and documented confirmed cauldron Altar Power bypass plus cross-version multiplayer report. No quest or runtime files changed.

- 2026-11-16 — Resolved the opening Rowan/first-Altar circularity at source/report level: exact Mutandis ingredient order/output and the 500 Altar Power enforcement bug are documented, with an actionable plant → oven → Mutandis → Rowan → Altar route. Updated 0.5.39 release-note deltas. Mutation odds, Jar/Oven chain details, remaining systems and in-pack tests remain open; no quests/runtime changes.
