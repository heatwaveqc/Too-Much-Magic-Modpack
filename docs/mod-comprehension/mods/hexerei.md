# Hexerei — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | hexerei / hexerei |
| Category / parent or addons | magic; standalone |
| MC / loader / pack target | 1.21.1 / NeoForge / test/full-integration |
| Pinned version / artifact / hash | hexerei-0.5.0.3.jar; SHA-512 6ad9f14ad23e84e693c8a02fa210871f7f9f7abb34acb342d9c72aacaa822dc4d06d203d78803ab40dd5eb334344961586a3f223ae877c362f1788b7be1dad6a [pin] |
| Source snapshot | JoeFoxe/Hexerei commit [a91395c0a933eb3bf6fbeecd89f30a0ea98248b2](https://github.com/JoeFoxe/Hexerei/tree/a91395c0a933eb3bf6fbeecd89f30a0ea98248b2), latest inspected 1.21.1 source commit before the official 0.5.0.3 release; its Gradle metadata says 0.5.0.1, so pinned-binary equivalence is unverified [source] |
| Guide/documentation version gap | The source tree contains a substantial custom Book of Shadows but predates the pinned artifact by patch versions. The 0.5.0.3 release notes say Mixing Cauldron potion crafting was fixed and Curios compatibility re-added. |
| Profile status | researching |
| Evidence reached | Pack pin reconciled; official release metadata and near-release source inspected; no exact JAR re-hash or client/world test in this pass |
| Reviewed | 2026-11-16 |
| Quest ownership | No Hexerei quests edited; research only |

## Executive gameplay map

**What the player actually does:** Hexerei is a witchcraft, exploration, storage and familiar mod. The player crafts a Book of Shadows, gathers plants and tree types, then uses the Mixing Cauldron, Pestle and Mortar, Herb Drying Rack, Candle Dipper, and other workstations to turn ingredients into potions, blood recipes, broom parts, storage and utility items. Witch Hut worldgen and the mod's plant/tree ecology provide exploration targets. Brooms give player-controlled flight with durability and attachment upgrades. Tamed crows can follow, gather, harvest crops and perform a limited villager pickpocket loop; Herb Jars and Coffers provide destinations for familiar logistics. Candles provide light and can carry nearby potion effects. Most progression is recipe- and discovery-driven rather than a tier ladder [source: Book of Shadows and recipe serializers].

**Why it matters in TMM:** Its strongest practical value is flight, an in-game reference, bulk Herb Jars and crows that automate narrow farming/gathering tasks. The Mixing Cauldron is a recipe-driven alternative for selected potion/item recipes, not universal potion automation. Witchery Remake overlaps thematically, so quests should distinguish Hexerei's cauldron/broom/crow systems from JAWR's faction and transformation loops.

**Quick start:** Craft the Book of Shadows with a vanilla Book, one Sage Seed, one item tagged hexerei:tallow_meltable, and two Leather in the documented shaped pattern. Open it for the Intro and Items chapters. The book explains recipe/uses links and the cauldron GUI/JEI transfer. A Mixing Cauldron is crafted from a vanilla Cauldron, five Iron Ingots, and two Torches [source: recipe/book_of_shadows.json; recipe/mixing_cauldron.json; Book of Shadows pages].

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Rideable broom flight; Crow familiar task modes; 1,024-item single-type Herb Jars; custom Book of Shadows with JEI links; heated fluid/item cauldron recipes and potion mixing | Book of Shadows; Mixing Cauldron; Willow Broom (exact early materials still need a pinned recipe audit) | Crow harvesting + pack crops (test compatibility); cauldron fluid recipes + Neo Vitae/JAWR (candidate only); broom keychain + Curios (release says compatibility restored; verify in pack) |

### Subsystem coverage at a glance

| Subsystem | Evidence and coverage | In-game reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- |
| Book of Shadows / altar | Recipe, custom book JSON, chapter/page data and English text; strong UI coverage | Intro; Items: Book of Shadows, Altars; book hover keybinds open JEI | Not run | Confirm pinned 0.5.0.3 interaction |
| Mixing Cauldron and potions | Source block/entity, recipe serializers and fluid/potion code; full recipe graph incomplete | Items: Mixing Cauldron pages 1–7; JEI category | Not run | Release .3 specifically fixes potion crafts; inspect/test pinned JAR |
| Herbs and processing | Drying-rack and pestle/mortar recipes; partial | Items: Drying Rack, Pestle and Mortar; JEI | Not run | Audit plant worldgen and renewable inputs |
| Blood Sigil / Candle Dipper / candles | Source traces fixed 250 mB blood increments per contact and 2,000 mB cauldron capacity; guide recipe/candle effects remain partial | Items: Blood Sigil, Bottle of Blood, Candles, Candle Dipper | Not run | Exact pinned .3 behavior, entity boundaries, bottle thresholds, Dipper and candle effects remain to test |
| Brooms / attachments | Book entity pages and recipe names; partial | Entities: Broom; Items: broom attachments | Not run | Audit inputs/stats/durability/Curios |
| Crows / Crow Flute | Book pages detail commands; code inventory exists | Entities: Crow; Items: Crow Flute; Herb Jar/Coffer | Not run | Test crop eligibility, ranges, cooldowns, ownership |
| Witch Hut / plants / trees | Structure/worldgen classes and data; partial | Book entries and Dowsing Rod | Not run | Verify worldgen with Tectonic and pack dimensions |
| Storage, couriers, satchels, gear | Broad content present; full mechanics not reviewed | Book Items entries | Not run | Audit major utilities before source-complete |

## Guidebook onboarding

- **Guide name/access:** Craft the custom Book of Shadows. Pattern: " L " / "ABS" / " L "; L=Leather, A=any item in hexerei:tallow_meltable, B=vanilla Book, S=Sage Seed. Right-click the held book to open it [source: data/hexerei/recipe/book_of_shadows.json; HexereiBookItem.java].
- **Altar:** Craft three Mahogany Slabs across the top and four Mahogany Planks in the two lower side columns. Place the book on it to display/read/turn pages; crouch-right-click to retrieve it.
- **Reference map:** Chapters are Index, Intro, Items, Entities. Items pages include Book of Shadows, Altars, Mixing Cauldron (7 pages), Herb Jar, Drying Rack, Pestle and Mortar, Blood Sigil, Bottle of Blood, Candles, Candle Dipper, broom attachments and Crow Flute. Entities includes Crow and Broom. The book documents hover keybinds for JEI recipe/uses.
- **Guide limits:** It gives useful steps but does not replace JEI for every exact recipe. Some effects, costs, times and automation boundaries need testing. Book text mentions future/planned familiars and coven content; do not promise those as implemented.
- **Other help:** Cauldron GUI can show JEI recipes; its plus button transfers ingredients. The 0.5.0.3 release note says potion crafting was fixed; the inspected source snapshot declares .1 and cannot prove .3 behavior.

## Systems and full mechanics inventory

| Subsystem | How accessed / inputs | Effects and outputs | Limits, failure conditions and source |
| --- | --- | --- | --- |
| Book of Shadows | Shaped recipe: vanilla Book, Sage Seed, 2 Leather, one hexerei:tallow_meltable item | In-game index, Intro, Items and Entities chapters; JEI recipe/uses links | Cover and corners are dyeable; data-driven page content may mention future features |
| Mixing Cauldron | Craft; GUI, throw items from above; 8 ingredient slots, fluid capacity up to 2 buckets; recipe-specific heat/moon conditions; optional top-left attachment | JSON recipes output items and/or fluid; custom cauldron/fluid/potion recipe types | Some recipes need heat beneath: fire, lava, campfire or magma, per guide. Wrong fluid/ingredients/heat/moon blocks recipe. Guide says item/fluid pipes and bottle/bucket use are supported. |
| Potion mixing | Fill with water, add two potion ingredients per Book example; heat | Book says up to 2 buckets of potion can be made | Version .3 changelog fixes this system. Exact input/output and container behavior require pinned test. |
| Blood Sigil | Recipe: 500 mB Lava, 4 Redstone, 4 Polished Blackstone; attach to cauldron sigil slot | Source snapshot: non-item entity contacts the sigil collision shape; with a Blood Sigil installed and cooldown clear, applies 3 magic damage, then adds 250 mB Hexerei blood if tank is empty or already blood and its current amount is below capacity | Tank capacity is 2,000 mB, but the nonempty-tank path uses `grow(250)` without clamping; a non-multiple amount below capacity can overfill it. No living-entity/type or successful-damage check occurs before the fill branch; full/incompatible tank still reaches damage call. Source-only boundary, not exact-pin verified. [source: `a91395c`, `MixingCauldronTile`] |
| Pestle and Mortar | Right-click to add one item; sneak-right-click adds stack; retrieve result by right-click | Recipe outputs include Seed Mixture, Wax Blend, Mindful Trance Blend and powders | Once all recipe inputs are in, items can no longer be removed; exact locking and consumption boundary source needs pin confirmation. |
| Herb Drying Rack | Right-click up to 3 items per slot; 3 slots; item transport automation described | Dried herbs, leather and broom-related ingredients | Recipe-specific drying process. Crouch-right-click empty-handed removes non-crafting stored items; right-click retrieves completed craft. |
| Candles / Dipper | Place/dye candles; apply accepted potion infusion with Dipper and recipe-defined fluid | Up to four candles in one block; infused lit candles apply effects to nearby entities | Book says typical candle burn is 35 minutes; only selected potion effects qualify. Exact radius, effect duration, input costs need catalog/test. Candles near an altar float when lit. |
| Brooms | Craft variants/attachments through recipes; place broom like a boat, mount and use controls | Willow, Witch Hazel, Mahogany broom flight; attachments, satchels/seats/keychain/whistle | Book calls Willow slower/easier and Mahogany faster. Brush wears while flying; Mending/Unbreaking can offset it. Netherite Tip resists fire/lava damage; Waterproof Tip supports underwater riding. Exact stats/costs unverified. |
| Crow familiar | Tame wild crows with Seed Mixture directly or by throwing it nearby; command via crow GUI/flute | Follow, Sit, Wander, Help. Help includes Gathering, Harvesting, Pickpocketing | Multiple crows may be tamed. Crow Flute selects/perches up to 9. Gather targets enabled nearby Coffers/Herb Jars; matching items normally, any item if that container is the perch. Harvest leaves drops on ground. Pickpocket loot depends on villager job and cooldown. |
| Herb Jar | Craft via cauldron; GUI via non-front face, or sneak right-click front with empty hand | One item type, up to 1,024 items; dye/name controls; crow gathering destination | Crow interaction depends on jar button and crow mode; full automation/range rules untested. |
| World flora, trees, Witch Hut | Explore generated features and structure; grow saplings | Ingredients and wood recipes for witchcraft crafting | Book says Witch Hazel appears in birch forests; saplings can drop from leaves. Other plant routes, dimensions and structure frequencies not mapped. |
| Coffers, couriers, satchels, crystals, armor, rings | Craft/use from recipe and book entries | Storage, item handling, trinkets, equipment and building palette | Major subsystems still unreviewed. Avoid calling these generic network logistics without evidence. |

### Material / capability input-output contracts

| System | Input kind and amount | Output | Trigger/range/capacity | Interface and evidence |
| --- | --- | --- | --- | --- |
| Book | Item stacks: book + seed + leather×2 + tallow-meltable item | hexerei:book_of_shadows | Shaped craft | Source recipe and custom book item |
| Cauldron | Up to 8 item recipe inputs + NeoForge fluid stack | Item and/or fluid output, recipe-defined | Up to 8,000 mB per guide; valid recipe conditions | GUI, JEI transfer, item and fluid capability path described in book |
| Potion mixing | Potion fluid + recipe ingredient stacks | Potion fluid in cauldron; bottles/buckets via fill/empty recipes | Book says up to 2 buckets; exact release behavior pending | Source includes potion/fluid bridge; .3 release note says fixed |
| Blood gathering | Non-item entity overlapping the Blood Sigil shape; slot 9 contains Blood Sigil; source cooldown `isColliding <= 1` | Damage call: 3 magic; on empty/same blood tank with free room, +250 mB `hexerei:blood_fluid`; no other cost; fill amount is independent of target health | Contact callback, cooldown set to 6 and decremented by cauldron ticking; tank capacity 2,000 mB | Source checks fluid compatibility/capacity only after damage, increments nonempty blood fluid with `grow(250)` (not capacity-clamped), and does not test `hurt()` result; exact pinned behavior and entity/contact semantics require client tests |
| Crow harvesting | Tamed Crow in Harvesting mode, eligible crop/plant | Dropped item entity in world | Crow or assigned perch vicinity; radius/cooldown unknown | Native AI; gathering is a separate mode |
| Crow gathering | Ground item entity + enabled Herb Jar/Coffer + Gathering mode | Item inserted into storage | AI scan; exact search range unknown | Mod-specific; test ownership/protected chunks |
| Candle effect | Lit, infused candle + nearby entity | Potion effect | While lit; radius and refresh cadence unknown | Recipe/feature exists; measurements pending |

### Acquisition, renewability and output economics

- The Book is an early craft in principle, but Sage Seed survival acquisition must be identified before questing it as guaranteed.
- The pre-release source snapshot defines a 250 mB blood increment per accepted sigil contact, a 2,000 mB tank and 3 magic damage before the tank-capacity/fluid-compatibility check. The nonempty-tank path can overfill a partly filled tank whose amount is not a multiple of 250. It does not restrict the target to a living entity or check whether damage succeeded before adding blood; this makes non-living, immune and full-tank cases important boundaries, not confirmed farm behavior. The 0.5.0.3 pin is not binary-matched to this 0.5.0.1-declaring source. The Book's three-jumps-per-bottle statement remains a guide claim, not a measured rate.
- Herb Jar holds 1,024 of one item type per its Book page; it is storage, not a universal transport network.
- Crow Harvesting drops items in the world; Gathering moves them into enabled storage. Analyze these separately for throughput and server cost.
- Cauldron recipes may consume fluids/containers differently. Do not assume empty containers return or one Hexerei brew equals one vanilla brewing operation without testing.

## Complete player progression / walkthrough blueprint

Hexerei has no numeric tier ladder found in this pass. A chapter should teach the book and one useful production branch, then present flight, familiars, candles and storage as optional branches. The source/version mismatch keeps pin-specific potion behavior open.

| Step | Gate or choice | Prerequisites and action | Result / named reference | Teaching and evidence |
| --- | --- | --- | --- | --- |
| 0. Find instructions | Required onboarding | Craft book using Book, Sage Seed, Leather×2, tallow ingredient; right-click; optional altar | Intro; Items: Book of Shadows, Altars | Task proves possession, not reading. Sage Seed source pending. |
| 1. First workstation | Core craft branch | Craft Mixing Cauldron (5 Iron, vanilla Cauldron, 2 Torches); GUI/top input, fluid, heat | Items: Mixing Cauldron 1–7; JEI category | Teach inputs/heat/JEI transfer. Pin-specific behavior untested. |
| 2. Process herbs | Optional resource branch | Craft drying rack and/or pestle; gather plants; dry or load exact recipe | Drying Rack; Pestle and Mortar | Teach lock/removal boundary and recipe retrieval. |
| 3. Fluid recipes | Selected recipes require fluid/heat | Use exact recipe fluid/items and heat/moon requirements | JEI cauldron category; Blood Sigil examples | Explain inputs, heat and fluid. Source recipe examples; pin operation pending. |
| 4. Collect blood | Optional | Blood Sigil, cauldron, entity contacting the sigil collision shape; bottle/bucket fluid afterward | Blood Sigil; Bottle of Blood | Guide says 3 jumps; source snapshot attempts +250 mB and 3 magic damage per eligible contact, but target and capacity boundaries need exact-pin verification |
| 5. Fly | Optional exploration | Broom plus functional brush; place, mount and use controls | Entities: Broom; attachment pages | Teach controls/wear; item possession is not proof of flight. |
| 6. Tame familiar | Optional | Seed Mixture; direct feed or throw near wild crow; set command | Entities: Crow; Items: Crow Flute | Teach one command and perch; owner/range limits need test. |
| 7. Crop/gather loop | Optional, server-sensitive | Crow Harvesting → dropped item entities → separate Gathering crow → enabled/perched storage | Crow pages 8–10; Herb Jar/Coffer | Must describe two modes and limited destination; crop compatibility untested. |
| 8. Infuse candle | Optional utility | Use valid potion infusion/Dipper recipe; light near player | Candles, Candle Dipper; JEI | Radius/effects need verification; candle acquisition is not proof of area effect. |
| 9. Explore optional content | Optional | Discover Witch Hut/trees/plants, then use Dowsing Rod and recipes if verified | Book Items/Entities and JEI | Worldgen and rare materials remain open. |

### Dependency graph and alternate routes

The practical core is **Book → Mixing Cauldron → recipe branches**. Herb processing, blood gathering, candles, brooms and crows are separate routes sharing ingredients/workstations. Brooms aid exploration; crows support local crop/item work. No compulsory numeric tier or boss gate was found. Confirm whether structures or rare plants gate key broom recipes before calling any step mandatory.

### Structures, rituals, automation and other how-to instructions

| Setup | Operation and reference | Failure/maintenance | Quest stage |
| --- | --- | --- | --- |
| Book altar | 3 Mahogany Slabs top; 4 Planks below sides; place book; crouch-right-click retrieves | Turn pages while displayed on altar; decorative | Onboarding |
| Mixing Cauldron | Add items from top or GUI; load fluid; use recipe heat beneath; use JEI transfer | Wrong input/fluid/heat/moon blocks recipe; 2 bucket capacity | First workstation |
| Drying Rack | Right-click inputs into 3 slots, up to 3 per slot; automate with item transport; retrieve finished output | Recipe time; crouch-right-click empty-handed removes ordinary stored items | Herb processing |
| Crow station | Enable jar/coffer Crow button; crow Gather mode; set container as perch for broader item pickup | Harvesting leaves drops in world; range, chunk safety and throughput untested | Crop/gather branch |
| Candle Dipper | Apply recipe-defined effect to candle using accepted potion/fluid | Only selected potion effects; radius/duration require test | Candle branch |

## Advanced and terminal objectives

- **Normal completion:** Open-ended sandbox mod; no capstone/terminal advancement identified.
- **Optional completion:** Use each broom class; configure crows; build herb/cauldron line; infuse candle; discover Witch Hut and plants; explore storage/trinket branches.
- **Remaining progression blockers:** Source-to-pin mismatch; Sage Seed route; full recipe/worldgen audit; blood cost; broom materials/stats; crow eligibility/ranges/cooldowns; candle effects; TMM overrides and client UI.
- **Quest coverage check:** Partial. The Book of Shadows is a strong reference, but pin-specific recipes and tested costs for fluid and broom branches remain.

## Internal combinations and overlooked mechanics

| Combination | Benefit | Limitation / evidence |
| --- | --- | --- |
| Crow Harvesting → dropped crops → Gathering crow → enabled/perched Herb Jar | Local crop/ingredient pickup | Separate modes; range/cooldown/chunk rules need tests. Book: Crow pages 8–10. |
| Cauldron → broom | Recipe inputs/fluid/heat create broom/brush/attachments → flight | Some recipes use blood or Nether-tier items; recipe path incomplete. |
| Herb Jar + Gathering crow | Focused storage plus familiar collection | One item type; mode and perch determine pickup. |
| Candle + altar | Ambient light/effect; candles float around altar when lit | Potion support/radius and stacking untested. |
| Blood Sigil → bottle/recipe | Entity interaction supplies blood ingredient | Health cost/eligible target not verified. |

## Low-hanging setups useful in ordinary survival

### 1. Book and Mixing Cauldron — early recipe access
- **Goal:** Use the mod's own guide and access its main item/fluid crafting interface.
- **Entry:** Book + Sage Seed + Leather×2 + tallow-tag item; five Iron, vanilla Cauldron, two Torches.
- **Steps:** Craft/open Book; craft/place cauldron; load a recipe's fluid and items; check JEI for heat and transfer.
- **Limits:** Up to 2 buckets per Book; Sage Seed source must be confirmed.
- **Evidence:** Near-release source JSON/book, no pinned client test.

### 2. Herb drying — ingredient conversion
- **Goal:** Turn gathered herbs into recipe ingredients.
- **Entry:** Drying Rack and recipe-defined plant.
- **Steps:** Right-click input into one of 3 rack slots, wait, retrieve output; item transport supported according to the Book.
- **Limits:** Up to 3 of one item per slot; recipe times vary.
- **Evidence:** Book of Shadows Drying Rack pages; not tested.

### 3. Willow Broom — flight payoff (recipe audit pending)
- **Goal:** Gain mount-based flight for exploration.
- **Entry:** Broom and functional brush; exact survival materials need pin audit.
- **Steps:** Place broom like a boat, mount, steer and use ascend/descend/dismount keybinds.
- **Limits:** Brush wears during flight; Mending/Unbreaking can offset wear according to Book.
- **Evidence:** Book of Shadows Broom pages; exact recipe/stats remain open.

## API, integration and pack-config surfaces

| Surface | Capability and constraint | Current TMM override? | Evidence |
| --- | --- | --- | --- |
| Mixing Cauldron recipe serializers | Item/fluid outputs, heat/moon conditions; .3 release notes potion fix | No override found in selected scan; full pack search needed | Source serializers/resource JSON and official release page |
| Book/candle/drying/pestle recipes | Separate custom serializers; candle data/components hold infusion | Unknown | Source recipe classes/JEI categories |
| Fluid capability | NeoForge IFluidHandler on cauldron; guide documents pipes, bottles and buckets | Unknown | Source and Book |
| Crow/jar/coffer state | AI modes, container gathering buttons and saved perch positions | Unknown | Entity/container implementation |
| Configuration | HexConfig and client keybinds; Book notes adjustable font | Full option list not inventoried | Source tree |
| Curios | Release notes say compatibility re-added; keychain documented as a Misc slot trinket | Curios is installed; not function-tested | Official .3 release metadata/book |

## Compatibility leads

| Other mod | Candidate link | Status |
| --- | --- | --- |
| Curios API | Broom keychain and trinket slot | Official release says compatibility re-added; verify pack slot |
| Farmer's Delight / Pam's crops | Crow harvesting modded crops | Candidate only; eligible crop list not checked |
| Neo Vitae / JAWR | Hexerei emits its own `hexerei:blood_fluid`; other blood/EV networks use distinct types | No native fluid conversion established; recipe compatibility or a deliberate bridge would be required | Candidate only; verify item/fluid recipes and pairwise operation before claiming integration |
| JEI | Book hyperlinks and cauldron transfer | Integration classes exist; client UI untested |
| Tectonic / dimensions | Witch Hut and flora generation | No seed/world test |

## Quest and teaching opportunities (research, not actual quests)

No quest entries were edited.

| Stage | Instructions/reference | Truthful detector and reward direction | Risk |
| --- | --- | --- | --- |
| Book | Craft using exact recipe; right-click; optionally make altar. Intro and Items: Book/Altars | Item proves possession, not reading; small XP and common supplies | Sage Seed acquisition |
| First cauldron craft | Build, open GUI, inspect recipe, use valid fluid/heat; Items: Mixing Cauldron; JEI | Product is proxy; investigate actual crafting advancement; XP + common inputs | Pin behavior untested |
| Dry herbs | Add exact recipe, wait and retrieve; Drying Rack/Pestle pages | Output proxy; XP + herbs/seeds | Times vary |
| Blood | Install Blood Sigil, use valid entity interaction, bottle; Blood Sigil/Bottle pages | Bottle proxy; XP + empty bottles if safe | Target/health cost open |
| Broom flight | Craft broom/brush, deploy/mount/test controls; Entities: Broom | Item not proof of flight; XP + repair supply | Controls/stats need test |
| Crow work | Tame, configure Harvest/Gather, set perch/container; Crow pages | Item gathered is proxy; may need manual observation; XP + seed mixture | Server throughput/protection |
| Candle effect | Infuse valid potion, light nearby; Candle pages | Candle possession is not proof of effect; XP + wax | Radius/effect list open |

## Existing chapter / pilot reconciliation

| Walkthrough stage | Quest state | Gap / next action |
| --- | --- | --- |
| Hexerei chapter | No quest implementation changed or reconciled in this research pass | Search canonical authoring/generated data and advancement support before a separate quest task |

## Conflicts, limitations and version drift

- Manifest pin is Hexerei 0.5.0.3 for Minecraft 1.21.1 NeoForge, both sides. Official Modrinth release notes mention repaired Mixing Cauldron potion crafting and restored Curios compatibility.
- Inspected public 1.21.1 source is from March 16, 2025 and declares version 0.5.0.1; pinned .3 is dated April 2. Source is near-release evidence, not proof of .3 bytecode.
- Modrinth release metadata lists LGPL-3.0-only, while upstream Gradle metadata in the inspected source declares MIT. This is recorded as a metadata discrepancy, not a legal conclusion.
- Book text mentions future covens/familiars; do not treat them as implemented.
- Exact effect radii, health costs, ranges, cooldowns and output rates have not been measured.
- No quests/runtime files changed and no client/world tests were run.

## Evidence ledger

| Claim | Source | Evidence and limit |
| --- | --- | --- |
| Pack pin | mods/hexerei.pw.toml; manifest/mods.json | [pin] version 0.5.0.3, Modrinth ID 9KSwA9O9, hash above; no local JAR re-hash |
| Release compatibility | [Modrinth 0.5.0.3](https://modrinth.com/mod/hexerei/version/9KSwA9O9) | 1.21.1 NeoForge, client/server; potion and Curios changelog notes |
| Source history | [commit a91395c0a933eb3bf6fbeecd89f30a0ea98248b2](https://github.com/JoeFoxe/Hexerei/commit/a91395c0a933eb3bf6fbeecd89f30a0ea98248b2), gradle.properties | Declares 0.5.0.1; code/data only near-release evidence |
| Guide | data/hexerei/recipe/book_of_shadows.json; book JSON/pages/lang | Exact recipe, chapters and instructions; source patch gap |
| Cauldron | MixingCauldron.java, MixingCauldronTile.java, MixingCauldronRecipe.java, recipe JSON | Recipe schema and guide contract; .3 behavior unverified |
| Blood Sigil output | [source commit a91395c](https://github.com/JoeFoxe/Hexerei/blob/a91395c0a933eb3bf6fbeecd89f30a0ea98248b2/src/main/java/net/joefoxe/hexerei/tileentity/MixingCauldronTile.java) | Source-declared 2,000 mB tank, 250 mB increment, 3 magic damage, cooldown 6; fluid check follows damage call and source has no LivingEntity or hurt-result guard. Commit metadata declares 0.5.0.1; this does not prove behavior of pinned 0.5.0.3. |
| Familiar/flight/storage | CrowEntity.java, BroomEntity.java, HerbJar/Coffer classes and book pages | Instructions and implementation presence; limits need runtime test |
| License | Modrinth release page vs Gradle properties | Conflicting upstream metadata; no legal interpretation |

## Disposable-world test plan

| Test | Positive and boundary case | Expected result | Performed? |
| --- | --- | --- | --- |
| Book/JEI | Craft book/altar; open chapters; test hover recipe/uses keybinds | Navigation and JEI links work; possession separate from reading | Not run |
| Cauldron | Valid heated, unheated and wrong-fluid recipes | Only valid input/condition processes; record item/fluid debit and outputs | Not run |
| Potion fix | Exact 0.5.0.3; water + two Nether Wart; inspect fluid and output | Verify two-bucket capacity and actual consumption | Not run |
| Blood Sigil | Empty/partial/full cauldron; incompatible fluid; player, ordinary mob, immune/dead entity and non-living entity; cross contact cooldown; check with/without sigil | Record exact-pin 250 mB increment, 3-magic damage, whether damage success/type gates fluid, full/incompatible tank order, and bottle threshold/three-jump claim | Not run; a91395c source behavior only |
| Herb/pestle | One drying and one mortar recipe; interrupt before/after complete input | Verify time, lock boundary, remainders and output | Not run |
| Crow/farm | Harvesting then gathering; vanilla/modded crops; protected chunk | Measure range/cooldown/drops/storage ownership and server cost | Not run |
| Broom | Same route with each broom; test brush damage, fire/water tips, Curios | Record controls, speed, durability, attachments | Not run |
| Candle | Infuse each supported potion; measure area and multi-candle behavior | Confirm valid effects/radius/duration and input preservation | Not run |
| Worldgen | Fresh seeds with Tectonic; locate huts, trees, herbs | Verify biome/placement/frequency without altering existing worlds | Not run |

## Unknowns and next research

- [ ] Inspect exact 0.5.0.3 JAR or establish a matching source commit.
- [ ] Find Sage Seed and tallow ingredient survival routes; complete plant renewability map.
- [ ] Audit all cauldron recipe JSON, moon/heat conditions, fluid/potion inputs, costs and automation.
- [ ] Inspect Blood Sigil/Dipper for target eligibility, health debit and fluid quantity.
- [ ] Audit broom recipes/stats/attachments and Curios slot.
- [ ] Map Crow target/crop lists, range, cooldowns, pickpocket loot and chunk/team protections.
- [ ] Finish Witch Hut/tree/plant worldgen and Tectonic interaction.
- [ ] Search current TMM recipes/config/scripts/quests for overrides; inspect FTB advancement support.
- [ ] Resolve upstream license metadata discrepancy for the pinned artifact.
- [ ] Perform disposable client/world tests before verified-in-pack status.

## Revision notes

- 2026-11-16 — Added a source-backed partial profile for the pinned Hexerei selection. Documented the Book of Shadows workflow, cauldron/blood/candle/broom/crow contracts and source/release gaps. No quest/runtime files changed.
