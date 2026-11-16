# Reliquary Reincarnations — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `reliquary-reincarnations` / `reliquary` |
| Category / parent or addons | Magic utility; no selected Reliquary addon |
| MC / loader / pack target | 1.21.1 / NeoForge / TMM `test/full-integration` |
| Pinned version / artifact / hash | `reliquary-1.21.1-2.0.85.1599.jar`; Modrinth project `fQO83PId`, version `NjITe5sZ`; SHA-512 `75e4034ffff03d85ac4647fef6802e8921034de55baa650c32b7826f6eba4406b65fedcf4a8875d1ae8a3b850395a49fd0ad40587d60ffa1a1c62bb3e48c2e37` |
| Source snapshot | [`d0cde7135d641529b9494aca9f1b06a52a9da603`](https://github.com/P3pp3rF1y/Reliquary/commit/d0cde7135d641529b9494aca9f1b06a52a9da603); source `gradle.properties` declares 2.0.85 / MC 1.21.1 / NeoForge. The release line and source commit align; binary-to-source identity has not been proved. |
| License | All Rights Reserved per the official project pages; source is inspected and paraphrased, not redistributed. |
| Guide/documentation version gap | Official wiki pages are mostly dated 2016–2018. Use them as operating leads only; several default figures/recipes may differ from 2.0.85. The current source includes newer mechanics and recipe changes. |
| Profile status | researching |
| Evidence reached | Exact pack pin, official file page, current branch source at 2.0.85, source recipes/config/default mechanics, official project page and wiki. No client/world test. |
| Reviewed | 2026-11-16 |
| Quest ownership | No TMM quests edited. |

## Executive gameplay map

**What the player actually does:** Reliquary is a toolbox of standalone magical items, crafted from ordinary materials and special mob drops. It has a custom potion lab (Apothecary Mortar + Cauldron), a charge-and-duplicate system (Tome of Alkahestry), a versatile Hunter's Handgun, toggleable/chargeable tools, protective charms and emergency items, and functional Pedestals. It is not a linear tiered magic school: the player's route is to use JEI and the wiki to choose a useful item, gather its exact crafting ingredients, and then learn its operating mode and resource cost. [source: official project, pinned-source registries and recipes]

**Why it matters in TMM:** The strongest survival uses are practical: crop work, item/XP collection, fluid handling, safer teleportation, protection, custom potions, death prevention, combat shots and local pedestal automation. Reliquary also has economy-sensitive item duplication: the pinned-version source recipe doubles one item tagged as a Nether Star for 256 Tome charge. That is a concrete progression/balance lead for the pack's late-game gates, not a proposed change.

**Quick start:** Open the item in JEI, check its ingredients and the Reliquary recipe category, then use the item tooltip's mode/charge instructions. For the custom potion chain, start with an Apothecary Mortar and the wiki's “Potion System” page; for the Tome, use “Tome of Alkahestry”; for operational tools, use “Chargeable and Toggleable items.” The wiki is older than the pinned release, so verify current JEI recipes and tooltips.

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Crop helper tools and Fertile Lilypad; item/XP magnet; fluid containers; modular handgun ammunition; potion and chargeable utility systems | Harvest Rod field work (requires its recipe); Witherless Rose as a passive defense; Infernal Chalice for lava handling (recipe/access costs need JEI) | Curios for compatible items; JEI for custom recipe types; ProjectE/late-game gates for the Tome's duplication recipes |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide / JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Crafting and special mob-drop ingredients | Current source item registry and recipe conditions; wiki mob-drop list is old | Partial | JEI; wiki “Mob drops and crafting ingredients” | not run | Compare all 2.0.85 loot tables/recipes against wiki; audit current drop rates |
| Apothecary potion system | Source shows three one-item mortar slots and cauldron state; official wiki explains recipe method | Partial | JEI Potion category; wiki “Potion System” | not run | Reconstruct full current potion map/effect formulas and verify in pinned JEI |
| Tome of Alkahestry | Exact Tome recipe, charging recipe, charge limit and Nether Star duplication recipe inspected in source | Partial but economy-critical route mapped | JEI Alkahestry categories; wiki “Tome of Alkahestry” | not run | Audit all charging/crafting/drain recipes and pack's EMC/progression interactions |
| Hunter's Handgun | Source registers nine magazine/shot families; wiki describes effects | Partial | JEI Handgun/magazine recipes; wiki “Handgun” | not run | Map all current bullet/magazine recipes, loading/reload/cooldown and damage |
| Pedestals | Source API/block entity inspected; official project + wiki list supported operations | Partial | JEI Pedestal recipes; wiki “Blocks” | not run | Map exact input/output, redstone, timing and supported wrapper set |
| Toggleable/chargeable gear and charms | Source registries/config + old wiki roster | Roster and examples only | Tooltips; wiki “Chargeable and Toggleable items” / “Other Items” | not run | Reconcile every item, toggle, charge, hunger/XP cost and Curios slot |
| Altar of Light / Glowstone | Source block/entity/config inspected | Core loop mapped | JEI Altar recipe | not run | Verify recipe and exact operation in world |
| Config/API/compatibility | Current source config categories and item capabilities inspected | Partial | Config file and source | not run | Compare with actual TMM config and test interfaces |

## Guidebook onboarding

- **No in-game Reliquary guidebook has been identified** in the pinned source item/block registry or official project description. The upstream reference is the official Reliquary GitHub wiki, with JEI and item tooltips for current recipes and operation.
- **Early access:** Reliquary has no single entry gate shown by its documentation; individual recipes determine when each tool is reachable.
- **Reference map:** Wiki pages are organized as “Blocks,” “Chargeable and Toggleable items,” “Other Items,” “Mob drops and crafting ingredients,” “Tome of Alkahestry,” “Handgun,” “Potion System,” and “Mod compatibility.”
- **Important limitation:** most pages were last edited years before 2.0.85. In particular, do not use their default Alkahestry output counts as current; source recipe JSON is authoritative where inspected. Quest instructions should provide concise operating steps and name the relevant JEI category.

## Systems and mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and source |
| --- | --- | --- | --- | --- |
| Mob-drop ingredients | Loot tables and recipes | Reliquary adds drops such as Bone Rib, Withered Rib, Chelicerae, Creeper Gland, Slime Pearl, Bat Wing, Zombie Heart, Molten Core, Eye of the Storm, Frozen Core, Nebulous Heart, Squid Beak and others. | Inputs for a broad set of tools, charms and blocks | Exact 2.0.85 mob/drop mappings and chances have not all been checked. The old wiki may use legacy names or drop sources. |
| Apothecary Mortar | Place and use mortar/pestle; insert ingredients through block UI | Source block entity has three slots, one item per slot, rejects duplicate item types and accepts configured potion ingredients or potion essence. Official wiki describes combining two or three unique ingredients, then using the pestle five times. | Potion Essence item with the intersection of compatible passive effects | Exact current effect map, ingredient tiers and pestle failure/consumption semantics require more source review and client validation. The wiki's list is old. |
| Apothecary Cauldron | Fill with water, place over a valid heat source, add essence and ingredients | Official wiki: essence + Nether Wart; add Gunpowder before Nether Wart for splash; Glowstone/redstone augment potency/duration; Dragon's Breath is listed by source state but detailed current sequence remains to verify. Empty vials retrieve the brew. | Three potions per completed brew per wiki | Current source tracks potion contents, liquid level, cooking time and modifiers. Exact water handling, heat-source list, operation order, recipe timings and item consumption are not yet fully mapped. |
| Tome of Alkahestry | Craft Tome; right-click for its menu; Shift-right-click toggles automatic charging | Pinned source Tome recipe consumes a book plus eight Reliquary components/mob drops. Source default charge limit is 1,000 (configurable 0–9,999). Current source has charging recipes: redstone dust and glowstone dust each add 1 charge; item-inventory charging is checked every 10 game ticks. | A Tome recipe consumes charge and a base item to output the recipe's configured result count; the consumed charge is returned with the Tome | Current source recipe for tag `c:nether_stars`: 256 charge + 1 matching item → 2. Some old wiki charge/value tables are stale. The exact complete recipe roster and all alternate charge ingredients remain to audit. |
| Tome charging / draining | Crafting table special recipe or active Tome | Source has dedicated `alkahestry_charging`, `alkahestry_crafting` and `alkahestry_drain` serializers; redstone/glowstone charging recipes verified | Charge stored on the nonstackable Tome; a special drain recipe returns a configured output when charge is spent | Alkahestry can be disabled by config; no TMM override was identified in checked manifest/pin files. |
| Hunter's Handgun | Craft handgun, bullet, magazine; load and fire | Source registers Neutral, Exorcism, Blaze, Ender, Concussive, Buster, Seeker, Sand and Storm magazines/bullets. Exact current recipes and ammo counts not enumerated here. | Distinct projectile/impact behavior by shot family | Old wiki says exorcism favors undead, seeker/ender home, ender pierces, concussive explodes without terrain damage, buster damages terrain, sand blinds/affects creepers and storm interacts with rain/charged creepers. Treat these effects as documented-upstream leads pending 2.0.85 code check. |
| Pedestal / Passive Pedestal | Place a color variant; insert one recognized item; use redstone where required | One item in the main slot; some tools have item/fluid capabilities or wrapper integrations | Depending on item: pickup items/XP, collect fluid/milk, shear, attack, charge Hero's Medallion, push/pull, grant local flight or act as Harvest Rod farm | Wiki says active operations require redstone; current code implements item-action wrappers and fluid/item handlers. Exact redstone propagation, cooldowns, ranges and chunk behavior need source/config validation. Passive variants are display-only. |
| Harvest Rod | Hand tool or Pedestal action item | Bonemeal, seeds/plantables, hoe/harvest modes; exact current charge/storage costs unknown | Hoe, plant, bonemeal, harvest; wiki says area action while holding use and harvests nearby crops when player breaks one | Current config exposes player and pedestal range/cooldown settings. Do not claim unattended output transfer before checking pedestal inventory behavior. |
| Fertile Lilypad | Place adjacent to crops | No recurring input described in source/wiki reviewed | Speeds crop growth; adjacent pads stack/boost growth per old wiki | Exact range, stack formula and growth conditions need current source/client check. |
| Infernal/Emperor Chalices | Toggle mode and use as portable fluid tools | Lava or water world interaction; food refill is available for a hunger/health trade per old wiki | Portable fill/place and fluid-handler compatibility; useful in Reliquary cauldron and other supported mod blocks | Source registers fluid-handler capabilities for the Chalices. Exact tank capacity, fill/drain compatibility and hunger cost need current-source verification. |
| Toggleable utility gear | Toggle with right-click; shift-left-click selects some modes | Per-item consumables include gunpowder, ender pearls, snowballs, bonemeal, XP, glowstone, feathers, torches or hunger/health | Teleportation, magnet, frost pathing, crop work, XP storage, auto-torch, fire, flight/push/pull, item storage and mob avoidance | Wiki's exact operational descriptions are old. Current tooltips/config and source implementation govern. |
| Mob Charms / Charm Belt | Craft mob-type charm(s); equip/use belt if chosen | Mob charms protect against their corresponding mob family; durability/charges and belt behavior need source check | Defensive mob avoidance | Current source has charm enable/config and recipe conditions. Verify exact range, targeting, charge attrition and stack/belt behavior before promising area defense. |
| Angelheart Vial / Phoenix Down | Carry in inventory | Angelheart Vial is consumed when player would die; current source has configurable heal percentage and negative-effect removal; Phoenix Down is listed as another death/fall tool | Prevent one death event and apply configured heal; vial returns an empty potion vial when consumed in crafting | Source commit contains a recent fix so Angelheart Vials and Phoenix Down no longer prevent `/kill`; do not describe them as absolute invulnerability. Test death cause order and multiple-vial behavior. |
| Altar of Light | Place, activate with redstone items, leave open to sky | Config default: 3 redstone; default nominal cycle 20 minutes plus randomized time variance (configurable); daytime and sky access required | Places one Glowstone block directly above the altar when the cycle finishes | It pauses during night or without sky visibility. This is distinct from the Tome of Alkahestry, despite its registry name `reliquary:alkahestry_altar`. |
| Special utilities / combat items | Craft and use individual items | Recipes commonly depend on Reliquary mob drops and components | Includes Witherless Rose, Wither protection; Angelic Feather, fall protection at hunger cost; Interdiction Torch, mob repelling; Holy Hand Grenade; Cross of Mercy; Glowing Water; and other food/combat/utility items | The roster is broad and not fully inventoried. Do not assume every 2016 wiki effect still describes 2.0.85. |

### Typed inputs and outputs: verified examples

| System | Input kind + exact ID/tag | Input quantity / cost | Output and location | Trigger / interface | Evidence |
| --- | --- | --- | --- | --- | --- |
| Alkahestry Tome recipe | Crafting item stacks: eight Reliquary items + `minecraft:book` | One each; shapeless | `reliquary:alkahestry_tome` | Crafting table; gated by `reliquary:alkahestry_enabled` recipe condition | Source recipe JSON at `d0cde713` |
| Tome charging | `minecraft:redstone`, `minecraft:glowstone_dust` | 1 charge each per inspected recipe | Charge stored on Tome | Special crafting or active-inventory charge path | Source charging recipe JSON and Tome code |
| Nether Star Tome craft | Item matching `c:nether_stars` + Tome with at least 256 charge | Consumes one recipe input and 256 Tome charge | Two matching items | Special crafting recipe | Source `alkahestry/crafting/nether_star.json` and `AlkahestryCraftingRecipe.java` |
| Angelheart Vial death handler | Player inventory contains `reliquary:angelheart_vial` | Vial count decreases by 1 on handled death; configured heal percentage | Player health restored; negative vanilla effects optionally removed | Reliquary's player-death handler | Source `AngelheartVialItem.java`; commit message reports `/kill` fix |
| Potion lab | Ingredient item stacks → potion essence item → cauldron-held potion state → empty vial | Exact ingredients and quantities are data/config-defined | Three potion items according to upstream documentation | Mortar interaction, cauldron input/cooking, vial extraction | Official wiki + current source block entities; exact per-recipe forms incomplete |
| Altar of Light | Redstone item stacks | Default 3 redstone to activate; time configurable | 1 `minecraft:glowstone` block placed above altar | Right-click activation; server tick only in daytime with open sky | Current source config and block entity |

### Acquisition, renewability and output economics

- Reliquary recipes draw on drops from common and hostile mobs; mob drops are a recurring survival resource, but the current 2.0.85 drop rates are not yet fully enumerated.
- The Alkahestry Tome is a true item duplicator with charge costs. Current source says one redstone or glowstone dust = one charge; Nether Star tag items cost 256 and output 2. This is reproducible from the source recipe data, not client-tested.
- The Tome's default maximum charge is 1,000, so a full Tome can afford three Nether Star duplication crafts (768 charge) with 232 charge left if recipe inputs and charge sources match the current source data. Whether a given TMM item is in the tag is an in-pack registry fact, not assumed here.
- The Altar of Light is a slow, sky/daylight-dependent redstone sink that produces one Glowstone block; it is not a power generator and does not produce FE.
- No current-source evidence reviewed establishes FE output, blood integration, or a general-purpose item/fluid transport network.

## Complete player progression / walkthrough blueprint

**Completion test:** Not met. Reliquary is an open-ended utility collection, not a tiered mod; a useful how-to path is possible, but the whole item/recipe roster, custom potion effect map and advanced operation details remain incomplete.

| Path and step | Gate or choice? | Prerequisites / materials | Player actions | Result / next step | In-game reference | Coverage |
| --- | --- | --- | --- | --- | --- | --- |
| 0. Learn how to discover recipes | Mandatory orientation | JEI and item tooltips; official wiki pages | Search an item, inspect uses/recipes/category, read its mode/charge tooltip | Find ingredients and exact recipe for a selected utility | JEI; relevant Reliquary wiki page | Partial; no in-game book found |
| 1. Choose a utility branch | Player choice | Desired task: crop, travel, combat, defense, potion, storage or duplication | Pick one item family; check required Reliquary drops | A focused item goal instead of a fictional linear tier | JEI; “Other Items” / “Chargeable and Toggleable items” | Partial |
| 2. Acquire Reliquary ingredients | Shared resource route | Mob drops and component items specified by JEI recipe | Hunt the relevant mob/loot source; use Looting only if the current drop mechanic supports it | Craft one or more utility items | “Mob drops and crafting ingredients”; JEI | Partial |
| 3A. Build a potion lab (optional) | Optional branch | Apothecary Mortar, pestle, Apothecary Cauldron, water, heat, unique potion ingredients, Nether Wart, empty vials | Make essence in mortar; put essence into a filled/heated cauldron; finalize and extract the brew with vials | Custom Reliquary potion types | “Potion System”; JEI Potion category | Partial; current effect map/guidance needs verification |
| 3B. Build a Tome (optional) | Optional, high-impact | Book, Witch Hat, Molten Core, Eye of the Storm, Catalyzing Gland, Slime Pearl, Chelicerae, Wither Skeleton Skull, Nebulous Heart | Craft Tome; charge it; inspect the exact duplication recipe and charge in JEI; combine Tome with one target item | Duplicate configured items, with Tome charge consumed | “Tome of Alkahestry”; JEI Alkahestry categories | Tome recipe and Nether Star recipe verified in source |
| 3C. Operate a functional Pedestal (optional) | Optional | Pedestal, one recognized action item, redstone source for active operation | Place a tool such as Coin of Fortune, Harvest Rod or sword; apply redstone; monitor its storage/output and range | Local pickup, farm, combat or item-specific operation | “Blocks” → “Pedestal”; JEI | Partial; exact operations need client checks |
| 3D. Craft and operate a firearm (optional) | Optional | Handgun, bullet/magazine components and chosen shot family | Craft ammo; load magazine; select/use the ammo type; inspect reload and cooldown information | Specialized ranged combat effect | “Handgun”; JEI | Partial |
| 3E. Obtain passive defense and survival tools | Optional | Charm, Witherless Rose, Angelheart Vial, Phoenix Down or environmental tools as selected | Craft from current JEI ingredients; equip/carry/use according to tooltip | Mob-specific avoidance, Wither protection, death/fall/fire/water support | “Other Items”; JEI | Partial |
| 4. Explore item combinations / advanced setups | Optional | Selected gear + compatible Pedestal, Curios or supported fluid/item interface | Test one item operation at a time and verify range/resource use | Efficient local automation or specialist kit | JEI + item tooltip + relevant wiki | Not fully mapped |
| 5. Completion | No single terminal objective | Player-selected utility goals | Finish the desired item/automation branches | Reliquary has no verified universal completion milestone | Wiki index / JEI | Open-ended |

### Dependency graph and alternate routes

- There is no universal Reliquary tier sequence. Mob drops feed several crafting branches; players can target a field tool, potion setup, combat kit, defense item or Tome independently.
- The Tome is not an early resource source: its current recipe requires a collection of specialized mob drops, including rare/hostile targets, and its duplication power is bounded by charge.
- The Altar of Light is a different system from Tome crafting; it converts an activation cost of redstone into Glowstone after a long, daytime/open-sky cycle.
- Pedestal automation is item-specific, not a generic logistics system. A pedestal's operation depends on recognized action-item interfaces and redstone behavior.

## Internal combinations and overlooked mechanics

| Internal combination | Full chain | Benefit | Limitation / exploit potential | Evidence |
| --- | --- | --- | --- | --- |
| Field farming | Harvest Rod or Fertile Lilypad → crop growth/planting/harvest | Saves manual planting and harvest effort | Exact range, speed, collection and chunk behavior need current verification | Official wiki; source config has Harvest Rod ranges/cooldowns |
| Functional Pedestal | Redstone + recognized item (Coin, sword, bucket, shears, Harvest Rod, Rending Gale, Hero's Medallion) | Local automation or effects | Must distinguish redstone-enabled function from display-only pedestal; avoid assuming generic item transfer | Official project page and source API |
| Tome duplication | Charge recipe → Tome → configured Alkahestry crafting recipe | Converts redstone/glowstone charge into extra items | Nether Star source recipe doubles a tag-matched input at 256 charge; check progression tags/EMC interaction | Exact current source JSON |
| Charged creeper harvesting | Storm Handgun magazine → charged creeper → Eye of the Storm drop (documented upstream) | Targeted access to a specialized ingredient | Shot behavior, drop trigger and rate need current source/runtime check | Old official Handgun wiki; source item registration |
| Potion essence chaining | Shared ingredient effects → essence → later recipe using essence | Combines/intersects effect ingredients | Effect overlap and duration/potency loss prevent naive unlimited stacking according to wiki; current formula still needs inspection | Official wiki + source PotionMap |

## Low-hanging setups useful in ordinary survival

### 1. Compact crop kit — early utility branch
- **Goal and payoff:** Reduce repetitive crop planting, bonemealing or harvesting.
- **Minimum entry:** Harvest Rod or Fertile Lilypad and its current recipe inputs.
- **Build/use steps:** Use JEI to craft the selected tool; read its tooltip and “Chargeable and Toggleable items” page; select the correct tool mode; test on one small patch before expanding.
- **Sustainability and limits:** The wiki describes fertilizer/plantable consumption and area actions; exact costs, range and item output behavior are not verified for 2.0.85.
- **Evidence:** Current config exposes player/Pedestal Harvest Rod ranges; no pack test.

### 2. Pedestal collection or farm station — utility branch
- **Goal and payoff:** Let a supported Reliquary item perform one local operation.
- **Minimum entry:** A Pedestal, an action item such as Coin of Fortune or Harvest Rod, and redstone activation.
- **Build/use steps:** Place the Pedestal; insert one supported item; apply redstone; observe what it targets and where it places output; inspect the item slot/inventory rather than assuming a networked output.
- **Sustainability and limits:** The wiki says active action items require redstone, while passive pedestal variants display only. Exact redstone range and chunk operation are unknown.
- **Evidence:** Source Pedestal entity and API; no in-pack test.

### 3. Safe travel / emergency kit — individual-item branch
- **Goal and payoff:** Combine one travel aid and one defensive safety item.
- **Minimum entry:** Ender Staff/Infernal Chalice plus Angelheart Vial, Phoenix Down or Witherless Rose as desired.
- **Build/use steps:** Craft through JEI; inspect mode and consumable requirements; test teleport/fall/fire protections in a disposable world before trusting them in dangerous progression.
- **Sustainability and limits:** Angelheart Vials are consumed on a handled death and should not be treated as invulnerability; current source includes a fix preventing /kill from being blocked. Other costs and edge cases remain untested.
- **Evidence:** Exact Angelheart Vial source handler and current release commit message; no client test.

## API, integration and pack-config surfaces

| Hook or customization surface | Namespace / exact surface | Capabilities / constraints | Current TMM override? | Evidence |
| --- | --- | --- | --- | --- |
| Common and client configs | Source `reliquary.reference.Config` | Disable toggles for Tome, Handgun, potions, Pedestals, passive Pedestals, charm/spawn-egg recipes; potion map, Altar timing, item costs/ranges/cooldowns and HUD locations | No Reliquary config override found in reviewed manifest/pin files; generated runtime configs not inspected | Source commit `d0cde713` |
| Item capabilities | Source `ModItems.registerCapabilities` | Item handler for Void Tear, Harvest Rod, Ender Staff, Rending Gale; fluid handler for Hero's Medallion and Emperor's Chalice | None documented in TMM | Source `ModItems.java` |
| Pedestal API | `IPedestalActionItem`, `IPedestalRedstoneItem`, wrappers and item/fluid capabilities | Item-specific actions; Pedestal is not a general-purpose logistics network | None identified | Current source package |
| Recipe serializers / conditions | `reliquary:alkahestry_*`, `reliquary:potion_effects`, mob-charm/spawn-egg recipes and config conditions | JEI custom recipe categories exist; feature enablement may hide recipes/items | No pack override identified | Current source `ModItems.java`, generated recipes |
| Loot | Reliquary entity/block loot conditions and modifiers | Config switches for drops; exact pinned loot tables need full audit | Not identified | Current source loot condition code |
| Licensing | Official project / source repository | All Rights Reserved; do not bundle source copies or assets in this knowledge base | N/A | Official project metadata and repository README |

## Compatibility leads

| Other mod | Candidate link and why | Native / pack-built / would require bridge | Status |
| --- | --- | --- | --- |
| JEI | Discover normal and custom Alkahestry/potion recipes | Native integration | Source contains JEI categories; current client UI not tested |
| Curios | Hero's Medallion and other items implement/access Curios hooks | Native compatibility likely; exact TMM item slots need testing | Source has Curios item interfaces; no client test |
| ProjectE | Tome's current Nether Star recipe doubles a stack-tagged input | Existing separate progression branch; no new bridge required for recipe existence | Source-supported risk; verify item EMC, tag membership and gate timing in pack |
| Botania / other fluid systems | Emperor's Chalice can act as a portable water/lava handler; old wiki names cross-mod container uses | Native/API compatibility if target mod exposes supported fluid handling | Source capability exists; each target integration untested |
| TMM custom config | Optional feature disable and recipe control surfaces | Configuration/data change would require a separate implementation task | Not changed in this pass |

## Quest and teaching opportunities — research only

- Teach the chosen task first, then show the relevant JEI recipe and the item's exact mode/resource cost. Avoid a single “make Reliquary items” chapter objective.
- If teaching the Potion System, give the exact current 5.6.4 operation sequence and point to JEI's potion category; the old wiki is insufficient for an exact pinned-version tutorial.
- The Tome deserves a deliberate progression lesson: explain that its current Nether Star recipe returns two items for one tag-matched input and consumes 256 charge; quest rewards should not accidentally seed a duplicator before the intended gate.
- Pedestal tasks must teach redstone activation and output inspection, and must distinguish operation from passive display.
- No TMM quests, runtime configs, KubeJS, recipes or item gates were changed.

## Existing chapter / pilot reconciliation

| Walkthrough stage | Current quest ID/title and status | Intended action vs actual detector | Gap / next action |
| --- | --- | --- | --- |
| Reliquary chapter | No TMM chapter inspected or edited | Not applicable | Inspect canonical quest source only if separately requested |
| Item/tool progression | No current TMM task reviewed | Not applicable | Any eventual chapter should cover choice-based tool goals and specific operation, not a linear ladder |
| Tome / Potion / Pedestal branches | No current TMM task reviewed | Not applicable | Source-backed how-to details and truthful FTB detector need separate quest work |

## Conflicts, limitations and version drift

- **Old wiki:** Main Reliquary wiki pages date from 2016–2018. They are useful for the names and broad operation of items, but not authoritative for the current source defaults or complete item/recipe roster.
- **Alkahestry discrepancy:** Old wiki says a Nether Star yields one. Current 2.0.85 source JSON has `result_count: 2` for `c:nether_stars`, at 256 charge. Use current source for that recipe.
- **Source identity:** Branch source at commit `d0cde7135d641529b9494aca9f1b06a52a9da603` declares 2.0.85 and includes the November 2026 release fixes. CurseForge/Modrinth binary identity has not been verified.
- **No client validation:** No crafting, GUI, effect, redstone, item/fluid capability, loot/drop, crop, combat or death behavior was tested.
- **Pack configuration:** The checked-in manifest/pin does not describe the actual generated Reliquary common config. Feature-disable settings and any local overrides remain unverified.
- **Remaining coverage:** Full custom potion map and formula; current mob drop sources/rates; all handgun shot behaviors and reload math; all item recipes/Curios behavior; Alkahestry charging/drain/output rosters; Pedestal ranges, redstone and item/fluid contracts; world loot and any mod interactions.

## Evidence ledger

| Claim / section | Source URL or exact inspected file + immutable ref | Evidence tier |
| --- | --- | --- |
| TMM exact pin | `mods/reliquary-reincarnations.pw.toml` on TMM `test/full-integration` | Pack metadata |
| Exact 1.21.1 file and release details | [CurseForge file 9555024](https://www.curseforge.com/minecraft/mc-mods/reliquary-reincarnations/files/9555024); [Modrinth project](https://modrinth.com/project/reliquary-reincarnations) | Documented upstream |
| Source version, config, registries, recipes and latest release changes | [Source commit](https://github.com/P3pp3rF1y/Reliquary/commit/d0cde7135d641529b9494aca9f1b06a52a9da603); `gradle.properties`, `reference/Config.java`, `init/ModItems.java` | Inspected source snapshot |
| Potion walkthrough | [Official Potion System wiki](https://github.com/P3pp3rF1y/Reliquary/wiki/Potion-System) | Documented upstream; last edited 2018, not exact-version guidance |
| Tome system | [Official Tome wiki](https://github.com/P3pp3rF1y/Reliquary/wiki/Tome-of-Alkahestry); current source `recipe/alkahestry/crafting/nether_star.json`, `AlkahestryCraftingRecipe.java`, `AlkahestryTomeItem.java` | Wiki historical; recipe source inspected |
| Pedestal and functional block use | [Official Blocks wiki](https://github.com/P3pp3rF1y/Reliquary/wiki/Blocks); current source `block/tile/PedestalBlockEntity.java` | Mixed |
| Tools, modes and costs | [Official Chargeable and Toggleable items wiki](https://github.com/P3pp3rF1y/Reliquary/wiki/Chargeable-and-Toggleable-items); [Other Items wiki](https://github.com/P3pp3rF1y/Reliquary/wiki/Other-Items) | Documented upstream; old pages |
| Handgun shot roster | [Official Handgun wiki](https://github.com/P3pp3rF1y/Reliquary/wiki/Handgun) | Documented upstream; old page |
| Mob-drop ingredient list | [Official Mob drops wiki](https://github.com/P3pp3rF1y/Reliquary/wiki/Mob-drops-and-crafting-ingredients) | Documented upstream; old page |
| Runtime behavior | Not tested | Not established |

## Next research actions

1. Reconcile all current generated recipes and mob-loot data against the old wiki, prioritizing every ingredient in the Tome recipe.
2. Inventory the source config defaults and derived potion combinations; turn the current Wiki potion procedure into a pinned-version, fully teachable walkthrough.
3. Inspect all nine handgun shot classes and current magazine recipes; capture reload/cooldown formulas and any block/entity side effects.
4. Audit active and passive Pedestal behaviors, redstone source/range, fluid/item outputs, wrappers and chunk behavior.
5. Validate the Tome's current input-consumption, charge, duplication and drain behavior in a client; compare Nether Star tag and EMC gate state in TMM.
