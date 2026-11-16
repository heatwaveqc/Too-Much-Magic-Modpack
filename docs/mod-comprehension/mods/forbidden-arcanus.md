# Forbidden & Arcanus — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `forbidden-arcanus` / `forbidden_arcanus` |
| Category / parent or addons | magic; standalone profile |
| MC / loader / pack target | 1.21.1 / NeoForge / `test/full-integration` |
| Pinned version / artifact / hash | `forbidden_arcanus-2.6.1.jar`; SHA-512 `012a1d5c779a0173a5c76ea79f429486753f040dfe59739bc45fd0817182d705a80110032f024757f0ac51a9bd10c546a11a730899aa9adbf7251a5040472724` [pin] |
| Source snapshot | [`f62af610d550e0d033f6c5cd166e40062638c44b`](https://github.com/stal111/Forbidden-Arcanus/tree/f62af610d550e0d033f6c5cd166e40062638c44b), the upstream commit that declares 2.6.1; pinned JAR equivalence not verified [source] |
| Guide/documentation version gap | The source snapshot matches the pinned version declaration; no player-facing manual with progression instructions was found in the snapshot. Upstream wiki material located during research targets older releases and is not treated as 2.6.1 authority. |
| Profile status | researching |
| Evidence reached | Exact pack pin reconciled; source/data inspected at immutable upstream commit; no client or world test |
| Reviewed | 2026-11-16 |
| Quest ownership | No FTB Quests chapter found in the inspected knowledge-base scope; research only |

## Executive gameplay map

**What the player actually does:** Forbidden & Arcanus is a collection of exploration materials, magical equipment, trinkets, and item-processing systems organized around two major machines. The Clibano is a fuel-fired multiblock furnace that accepts recipes with progressively stronger fire requirements; Soul and enchanted fire both alter its recipes and byproducts. The Hephaestus Forge consumes four kinds of stored essence and performs datapack-defined rituals using a central input, pedestal ingredients, a tier requirement, and a matching magic circle. Forge progression runs from tier 1 through tier 5, with upgrades requiring increasingly valuable materials and essence. Additional branches include Lost Souls, equipment modifiers/enhancers, and late-game items such as the Eternal Stella, Boss Catcher, and Tyr/Draco Arcanus gear. The upstream README describes these loops but does not teach their complete construction or acquisition path [source: README.md; source: ritual and recipe data].

**Why it matters in TMM:** The Clibano is a candidate for magical ore processing and item conversion; the Hephaestus Forge is a late progression/crafting system with typed essence costs. Its role overlaps with other pack processing and ritual systems, so quest design should compare concrete recipes and resource costs before presenting it as an early alternative.

**Quick start:** Exact first access is not established. A Soul Extractor can be crafted from a Utrem Jar, Nether Bricks, a Quartz Block, and Quartz, then used on vanilla Soul Sand to harvest a Soul and convert that block to Soulless Sand. A Clibano Core recipe exists (Darkstone ring around a Blast Furnace), but the complete multiblock layout and the survival acquisition path for every required block remain to be verified [source: `recipe/soul_extractor.json`; `recipe/clibano_core.json`; `SoulExtractorItem.java`].

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Tiered Hephaestus Forge rituals; Clibano ore/item processing; soul-powered smelting; Eternal Stella item repair/indestructibility use case; late-game Tyr and Draco Arcanus equipment | None confirmed as genuinely early yet. The extractor recipe is low-complexity on paper, but it requires a modded Utrem Jar and the survival acquisition chain has not been mapped. | Clibano recipes × pack ore tags (candidate; tag compatibility must be checked); Forge ingredients × other magic-mod resources (candidate only); Forge-made equipment × Apotheosis affixes/enchantments (candidate only; no compatibility patch verified) |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Forbiddenomicon and research desk | Source: `DeskBlock.java`, `ResearchDeskBlock.java`, `ModKnowledge.java`, research JSON | Partial; only three knowledge nodes exist | Research screen is opened by right-clicking the Research Desk; nodes do not give build instructions | Not run | Locate a survival acquisition route; source data has no recipe/loot-table/advancement file for either item, and the Forbiddenomicon block reports disabled feature state |
| Soul Extractor and Souls | Source: `SoulExtractorItem.java`, recipe JSON | Partial; extractor action is known, source of the recipe's Utrem Jar not yet mapped | JEI recipe/use pages not client-checked | Not run | Confirm recipe display, tool durability and pack recipe availability |
| Clibano | Source: block entity, logic, recipe and residue data | Partial; recipe/fire rules known, exact multiblock placement and full recipe I/O audit remain | Clibano GUI; no named guide page found | Not run | Establish multiblock shape, full input/output recipe catalog, and automation |
| Hephaestus Forge | Source: ritual registry/data, Forge block entity and RitualManager | Partial; tier costs and ritual schemas supported by source | Forge UI offers ritual status; no player-facing how-to pages found | Not run | Map forge/pedestal/circle geometry and complete item/essence acquisition chain |
| Aureal, blood, souls, experience essences | Source: essence manager/storage/provider code | Partial; storage and some gain rules identified | No in-game reference page found | Not run | Trace collection, item storage, caps by tier, entity eligibility and exact player economy |
| Lost Souls, worldgen, gear, trinkets, enhancers | Source inventory confirms these subsystems exist | Unresearched beyond selected hooks | No complete guide mapping established | Not run | Audit every major branch before source-complete status |

## Guidebook onboarding — required before the player's first complex step

- **Guide name and access:** The mod has a custom Research Desk screen and a Forbiddenomicon block/item. Right-click a Research Desk without an item to open its menu. Use a Forbiddenomicon on a plain Desk to convert it into a Research Desk; this consumes one Forbiddenomicon. Shift-right-clicking the Research Desk changes it back to a Desk and returns a Forbiddenomicon. [source: `DeskBlock.java`, `ResearchDeskBlock.java`]
- **Earliest attainable stage:** Not established. The inspected source snapshot contains no recipe, loot table, or advancement file named for either Forbiddenomicon or Research Desk. The Forbiddenomicon block's `isEnabled` method returns false; whether another pack/world feature setting changes its practical availability is untested.
- **First instructional quest:** If the guide UI proves obtainable in the pack, an early quest should teach how to obtain and open it. Do not invent a free reward as an existing mechanic. If it is inaccessible or informationally empty, the quest chapter itself must supply all build and operation instructions.
- **Reference map:** The research registry contains only Welcome, Forbiddenomicon, and Desk. These are unlock/display nodes, not a progression guide. No Patchouli/Modonomicon manual pages were found in the pinned source snapshot.
- **Where guide is insufficient:** It does not explain material acquisition, Clibano construction/operation, forge geometry, any forge ritual, essence collection, or tier unlocks.
- **Other in-game help:** JEI may expose recipes/categories, but it was not opened in a client. The Clibano and Hephaestus Forge have dedicated menus.

## Systems and full mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and source |
| --- | --- | --- | --- | --- |
| Soul Extractor | Craft, then use on Soul Sand for 35 ticks while not sneaking | Soul Extractor durability: 1 durability point per successful extraction; target must still be vanilla `minecraft:soul_sand` and player must have permission to interact | Replaces the targeted block with `forbidden_arcanus:soulless_sand`; spawns one `forbidden_arcanus:soul` item entity above it | Rechecks target at completion. If target is no longer valid, returns without extraction. Uses a POV ray at completion; exact durability slot behavior needs client check. [source: `SoulExtractorItem.java`] |
| Clibano fire modes | Supply fuel and recipe inputs; optionally place an item tagged for soul/enchanted fire in the soul slot | Fuel item; one tagged fire starter item is consumed when a recipe can smelt and a burn is active or fuel is present | Fire, Soul Fire, or Enchanted Fire mode; cooking-speed multipliers are 1.0×, 1.5×, 2.5× respectively | Soul/enchanted mode lasts 2,700 ticks (135 seconds at 20 TPS) by default; an enhancer can multiply the Soul duration. Stronger recipes require matching fire tier. [source: `ClibanoFireType.java`, `ClibanoMainBlockEntity.java`] |
| Clibano default smelting | Two input slots, fuel, two result slots | Each slot processes its own matching non-double recipe; item inputs shrink by one on completion | Recipe-defined result, possibly XP; recipe-specific residue may be added under Soul/Enchanted Fire | Recipe progress resets when the relevant recipe cannot proceed. Output capacity and required fire tier gate smelting. |
| Clibano double recipe | Both input slots must match one recipe marked double | Both slots consumed as one operation | Recipe-defined result into an available output slot | Both input slots must be occupied and pass recipe matching; no claim of generic “double every ingot” is supported. |
| Clibano residues | Chance attached to a recipe, only when fire mode is not ordinary Fire | Recipe completion plus chance roll; one residue unit on success | Residue stored internally; residue types may combine into a recipe-defined result when enough accumulate and outputs have room | Total residue storage cap is 64. Exact types, chance values, combination requirements, and recipe association must be cataloged from the full registry/data. |
| Hephaestus Forge ritual | Place main ingredient in forge and ingredient stacks on supported pedestals; select/activate ritual through the Forge menu | Ritual JSON declares 1–8 ingredient entries, exact main ingredient, essences, minimum/exact tier, enhancer requirements, magic-circle type, and duration | Item creation or main-item transmutation; some rituals upgrade forge tier or execute a level effect | Costs are removed when ritual starts. Source snapshot has failure chance hardcoded to 0.0, but the ritual code retains lightning/failure paths. If interrupted after starting, essence recovery is not established; exact item-loss behavior needs a controlled test. Ritual failure logic returns the main ingredient but pedestal ingredients are cleared by reset. |
| Forge essences | Feed essence through supported essence item/block/effect paths | Four typed stores: Aureal, Souls, Blood, Experience. Rituals declare integer costs | Stored by the forge and spent by ritual; modifiers may alter required essence in the eligibility calculation | Values are capped by tier-specific essence definitions; full caps and feeding channels are not yet documented. Forge checks `hasMoreThan` requirements, not a measured throughput. |
| Forge tier upgrades | Perform the `upgrade_tier` ritual with its exact ingredients and matching circle | Tier-specific main input, pedestal inputs and essence costs below | Forge tier increases to 2–5 | Source has ritual data for tiers 2–5. Recipe operation needs forge tier, placement, essence delivery, and ritual activation. |
| Eternal Stella | Tier 3 `create_item` ritual | Diamond main input; 3 XPetrified Orbs + 1 Stellarite Piece; 82 Aureal, 1 Soul, 1,000 Blood | 1 Eternal Stella | Source data confirms ingredients and result only; intended item use and whether it consumes/repairs a target need inspection. |
| Boss Catcher | Tier 4 `create_item` ritual | Quantum Catcher main input; Nether Star + 3 Stellarite Pieces; 500 Aureal, 10 Souls, 7,500 Blood, 1,200 Experience | 1 Boss Catcher | Capture behavior, valid boss types, persistence, and release/output mechanics not yet inspected. |
| Lost Souls | Spawned through world behavior and special weapon/enchantment death hooks | On qualifying target death with a weapon, chance comes from enchantment helper; 4% chance to upgrade to Enchanted Lost Soul after a qualifying spawn roll | Lost Soul / Corrupt Lost Soul / Enchanted Lost Soul entities | Which entities qualify is tag-driven. Souls do not spawn from all kills by default. Full native ambient/world spawn conditions not mapped. |
| Enchantments, equipment, trinkets, plants, structures, worldgen and creatures | Registry/source inventory | Not fully traced | Many player-facing items/blocks and world features are present | Full audit remains required; do not infer their acquisition or effects from names. |

### Forge tier ritual costs confirmed from pinned-version source data

Costs below are the values in each ritual JSON. The RitualManager's start path reduces essence storage by the ritual's declared costs and the ritual schema validates the forge tier/ingredients. Whether modifiers are applied consistently to the deducted amount is an unresolved source-level concern: eligibility computes a modified requirement, while the visible start method reduces the unmodified declared value.

| Target tier | Main ingredient | Pedestal ingredients | Essence cost | Circle / tier gate |
| ---: | --- | --- | --- | --- |
| 2 | 1 Edelwood Planks | 4 Arcane Crystals + 4 Spawner Scraps | 500 Aureal, 6,000 Blood, 10 Souls | Upgrade Tier circle; exact tier match |
| 3 | 1 Chiseled Polished Darkstone | 4 Arcane Crystals + 4 Deorum Ingots | 1,000 Aureal, 9,000 Blood, 50 Souls | Forge tier 2; Upgrade Tier circle; exact tier match |
| 4 | 1 Chiseled Polished Darkstone | 4 Stellarite Pieces + 4 Runes | 2,000 Aureal, 12,000 Blood, 100 Souls | Forge tier 3; Upgrade Tier circle; exact tier match |
| 5 | 1 Stellarite Block | 4 Sculk Catalysts + 2 Dark Nether Stars + 2 Dragon Scales | 5,000 Aureal, 20,000 Blood, 500 Souls | Forge tier 4; Upgrade Final Tier circle; exact tier match |


### Built-in ritual catalog — version-aligned 2.6.1 source data

The pinned-version-aligned generated-resource cache lists **22** ritual JSON files. These are the declared recipes, not a claim that their inputs are obtainable in TMM or that the pinned binary has been proven identical to source. Every entry uses the create_item magic circle except the four tier upgrades, which use the named upgrade circle. “No explicit tier” means the JSON omits a forge-tier field; it is not a tested accessibility result. Pedestal inputs are listed after the main ingredient.

| Result type | Ritual → output | Main ingredient | Pedestal inputs | Essence costs | Additional JSON gate |
| --- | --- | --- | --- | --- | --- |
| Upgrade tier | upgrade_tier_2 → tier 2 | 1 Edelwood Planks | 4 Arcane Crystals; 4 Spawner Scraps | 500 Aureal; 6,000 Blood; 10 Souls | No explicit tier |
| Upgrade tier | upgrade_tier_3 → tier 3 | 1 Chiseled Polished Darkstone | 4 Arcane Crystals; 4 Deorum Ingots | 1,000 Aureal; 9,000 Blood; 50 Souls | Tier 2 |
| Upgrade tier | upgrade_tier_4 → tier 4 | 1 Chiseled Polished Darkstone | 4 Stellarite Pieces; 4 Runes | 2,000 Aureal; 12,000 Blood; 100 Souls | Tier 3 |
| Upgrade tier | upgrade_tier_5 → tier 5 | 1 Stellarite Block | 4 Sculk Catalysts; 2 Dark Nether Stars; 2 Dragon Scales | 5,000 Aureal; 20,000 Blood; 500 Souls | Tier 4; Upgrade Final Tier circle |
| Transmute input | draco_arcanus_boots → Draco Arcanus Boots | 1 Netherite Boots | 2 Dragon Scales; 2 Obsidiansteel Ingots | 700 Aureal; 405 Blood; 370 Experience; 5 Souls | Tier 2 |
| Transmute input | draco_arcanus_chestplate → Draco Arcanus Chestplate | 1 Netherite Chestplate | 4 Dragon Scales; 3 Obsidiansteel Ingots | 1,300 Aureal; 625 Blood; 760 Experience; 9 Souls | Tier 2 |
| Transmute input | draco_arcanus_helmet → Draco Arcanus Helmet | 1 Netherite Helmet | 2 Dragon Scales; 3 Obsidiansteel Ingots | 820 Aureal; 475 Blood; 420 Experience; 6 Souls | Tier 2 |
| Transmute input | draco_arcanus_leggings → Draco Arcanus Leggings | 1 Netherite Leggings | 3 Dragon Scales; 3 Obsidiansteel Ingots | 1,000 Aureal; 565 Blood; 540 Experience; 7 Souls | Tier 2 |
| Transmute input | tyr_boots → Tyr Boots | 1 Draco Arcanus Boots | 3 Aquatic Dragon Scales; 2 Golden Dragon Scales | 4,460 Aureal; 5,620 Blood; 2,170 Experience; 40 Souls | Tier 4 |
| Transmute input | tyr_chestplate → Tyr Chestplate | 1 Draco Arcanus Chestplate | 4 Aquatic Dragon Scales; 4 Golden Dragon Scales | 6,300 Aureal; 8,080 Blood; 3,420 Experience; 60 Souls | Tier 4 |
| Transmute input | tyr_helmet → Tyr Helmet | 1 Draco Arcanus Helmet | 4 Aquatic Dragon Scales; 1 Golden Dragon Scale | 4,820 Aureal; 5,930 Blood; 2,450 Experience; 40 Souls | Tier 4 |
| Transmute input | tyr_leggings → Tyr Leggings | 1 Draco Arcanus Leggings | 4 Aquatic Dragon Scales; 3 Golden Dragon Scales | 5,080 Aureal; 7,620 Blood; 3,010 Experience; 50 Souls | Tier 4 |
| Create item | boss_catcher → Boss Catcher | 1 Quantum Catcher | 1 Nether Star; 3 Stellarite Pieces | 500 Aureal; 7,500 Blood; 1,200 Experience; 10 Souls | Tier 4 |
| Create item | eternal_stella → Eternal Stella | 1 Diamond | 3 Xpetrified Orbs; 1 Stellarite Piece | 82 Aureal; 1,000 Blood; 1 Soul | Tier 3 |
| Create item | ferrognetic_mixture → Ferrognetic Mixture | 1 Lodestone | 2 Clay Balls; 2 Wax; 2 Slime Balls; 1 Iron Ingot | 100 Aureal; 1,250 Blood; 2 Souls | No explicit tier |
| Create item | quantum_catcher → Quantum Catcher | 1 Quantum Core | 4 Spawner Scraps | 200 Aureal; 1,200 Blood; 155 Experience; 5 Souls | No explicit tier |
| Create item | quantum_injector → Quantum Injector | 1 Mundabitur Dust | 4 Quantum Cores | 5,000 Aureal; 3,000 Blood; 1,060 Experience; 50 Souls | Tier 3 |
| Create item | sea_prism → Sea Prism | 1 Heart of the Sea | 2 Prismarine Shards; 2 Turtle Scutes; 2 Lapis Lazuli | 1,000 Aureal; 2,000 Blood; 8 Souls | Tier 3; Elementarium enhancer |
| Create item | smelter_prism → Smelter Prism | 1 Arcane Crystal Block | 2 Coal; 4 Blaze Powder | 200 Aureal; 1,250 Blood; 4 Souls | Elementarium enhancer; no explicit tier |
| Create item | soul_binding_crystal → Soul Binding Crystal | 1 Quartz | 1 Soul; 1 Corrupt Soul; 1 Enchanted Soul; 2 Amethyst Shards; 3 Ender Pearl Fragments | 1,500 Aureal; 375 Blood; 220 Experience; 20 Souls | Tier 4; Divine Pact enhancer |
| Create item | terrastomp_prism → Terrastomp Prism | 1 Diamond Block | 2 Flint; 2 Dripstone Blocks; 2 Pointed Dripstone | 300 Aureal; 1,500 Blood; 9 Souls | Tier 2; Elementarium enhancer |
| Create item | whirlwind_prism → Whirlwind Prism | 1 White Wool | 1 Bat Wing; 2 Feathers; 3 Phantom Membranes | 1,000 Aureal; 2,250 Blood; 3 Souls | Tier 3; Elementarium enhancer |

The catalog corrects a potential reading of the Forge as uniformly tier-gated: tier requirements are explicit per JSON and absent from several rituals. Three registered result types are present (create_item, transmute_input, upgrade_tier); the generated JEI plugin places the first two into its smithing category and tier upgrades into a separate upgrading category. The catalog does not establish recipe availability after TMM scripts/configs, real item-acquisition routes, or successful in-world execution. [source: generated-resource cache and ritual JSONs; ForbiddenArcanusJEIPlugin.java]

## Material / capability input-output contracts

| System | Input kind + exact ID/tag/event | Input qty/formula; consumed/remainder | Output kind, qty and location | Trigger/cadence; range/capacity | Automation/interface and evidence |
| --- | --- | --- | --- | --- | --- |
| Soul extraction | World block `minecraft:soul_sand` + held item `forbidden_arcanus:soul_extractor` | Hold-use for 35 ticks; tool loses 1 durability; target block replaced | Item entity `forbidden_arcanus:soul` at target +1 Y | Player world interaction, valid target and permissions | Native item action; automation not established. [source] |
| Clibano ordinary recipes | Item stack in one input slot + fuel item | Recipe-defined; one input consumed per completion; fuel duration is vanilla fuel behavior | Recipe result to GUI output slot | Tick-driven while fuel burns; 2 input slots, 2 output slots | Menu/container item handler exists. Exact sided automation contract not checked. |
| Clibano Soul/Enchanted Fire | Item in soul slot matching the respective item tag | One starter item consumed; 2,700-tick base Soul duration; enhancer may modify it | Fire type state; modifies recipe availability/speed/residue | Timer; one active mode at a time | Tags: `forbidden_arcanus:clibano/creates_soul_fire`, `.../creates_enchanted_fire`. [source] |
| Clibano residue roll | Completed recipe with `residue_chance` data | Probability and residue type are recipe data; max combined storage 64 | Internal residue count; possible combined item in result slot | Per successful recipe completion | Datapack recipe type supports it; values not fully inventoried. |
| Forge ritual | Main ingredient stack + pedestal item stacks + forge essence storage + tier/circle | Input requirements are datapack-defined; ritual duration defaults to 500 ticks when omitted. Essence is deducted on start. | Ritual result item and optional level effect; item result is returned to caller/output path | Ritual ticks; pedestal/forge geometry and valid-circle search still to document | Datapack registry `forbidden_arcanus:ritual`; 22 built-in ritual JSON entries in the pinned source tree. |
| Forge blood from combat | Living entities within an AABB inflated 5 blocks around forge; health observed between ticks | Source code adds integer health lost × 20 to Blood for cached nearby entities | Forge Blood essence, bounded by current tier maximum | Forge tick; checks all living entities in the 5-block inflated box | Source-level behavior in `EssenceManager.tick`; who is eligible and tick timing need validation. Could create a gated-drop/farm concern; do not propose a farm before tests. |
| Aureal player resource | Entity essence capability and Aureal cost component | Specific wearable/tool costs not enumerated | Player Aureal changes; Forge Aureal storage exists separately | Entity/provider-driven; exact regeneration and transfer path not yet mapped | Capability and data components exist. |
| Ritual progression | Ritual JSON inputs, tier predicate, enhancer requirements and magic-circle key | Exact inputs listed in ritual data; source checks against cached pedestal ingredients | Create-item, transmute-input, tier-upgrade result, plus some ritual level effects | Datapack-defined duration and essence costs | Data-driven registry; operation is not proven by JEI display alone. |

### Acquisition, renewability and output economics

- The Soul Extractor turns one Soul Sand block into one Soul item and one Soulless Sand block; the action is not a renewable block conversion unless Soulless Sand can be converted back by another confirmed recipe.
- Tier progression costs rise sharply in Souls, Blood, and Aureal; no source-backed production rate or attainable cap comparison has yet been completed.
- Forge blood storage can increase from nearby observed health loss, source-derived at 20 Blood per integer health point lost; source code shows a 5-block inflated AABB. Do not call this a practical farm until entity selection, cadence, caps, and pack-side safety have been tested.
- Clibano products are recipe-data outputs, not a universal multiplier. Soul/enchanted fire gates special recipes and can roll residues; a claim that any ore doubles is unsupported.
- Each tier upgrade recipe consumes its listed main and pedestal items through the ritual process. The exact point of ingredient removal and interruption edge cases need a disposable-world test.

## Complete player progression / walkthrough blueprint

This is a verified partial route, not yet a complete start-to-finish progression graph. The research UI does not provide the missing how-to instructions.

| Path and step | Gate or choice? | Exact prerequisite / materials / world condition | Player actions: obtain, build, activate, use | Unlock / visible success / next step | In-game guide section or JEI reference | Proposed quest/how-to, detection and reward | Evidence / coverage |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 0. Access instructions | Necessary for a self-guided chapter, but acquisition is unresolved | Forbiddenomicon and Research Desk have no recipe/loot/advancement path found in inspected data; block feature state may disable it | Verify whether the desk exists in the pack and whether its menu can be opened | Only three knowledge nodes: Welcome, Forbiddenomicon, Desk | Research screen; no detailed pages | Do not create an acquisition quest until client/source path is established; if unavailable, place concise instructions in chapter text | Partial; source/data scan |
| 1. Harvest Souls | Optional Clibano input system; likely required for Soul Fire recipes | Craft Soul Extractor: Utrem Jar + Nether Bricks + Quartz Block + Quartz; locate Soul Sand | Hold use on Soul Sand for 35 ticks | Soul item drops; target becomes Soulless Sand | JEI Soul Extractor recipe, not yet client-checked | Teach ordered action; FTB can detect extractor/Soul item, not prove block conversion | Source-supported operation; recipe prerequisites incomplete |
| 2. Build/operate Clibano | Major processing branch | Craft Clibano Core from a ring of Darkstone around Blast Furnace; remaining frame pieces/layout unverified | Build multiblock, add fuel/recipe inputs, use GUI output; optionally add tagged fire starter | Process ordinary recipes; Soul/Enchanted Fire unlock stronger recipe tiers and residue chance | Clibano screen; exact recipe category name/client UI unverified | Must supply build diagram and teach one recipe; item acquisition is not enough to verify operation | Partial |
| 3. Establish Forge and essences | Required for ritual branch | Exact forge tier 1 recipe/placement, pedestal count/range, circle matching, and essence delivery not yet fully mapped | Build tier 1 forge; set central input/pedestals; supply essence and activate valid ritual | Ritual indicator/circle effect, progress, result | Hephaestus Forge screen; no manual page found | Teach exact geometry and one starter ritual only after checking source pattern and live GUI | Partial |
| 4. Upgrade to Tier 2 | Gate | Edelwood Planks main item; 4 Arcane Crystals, 4 Spawner Scraps; 500 Aureal, 6,000 Blood, 10 Souls | Stage exact inputs and Upgrade Tier circle; activate ritual | Tier 2 forge | Ritual data; screen/page name not yet validated | Quest teaches inputs, essence sourcing and activation. Ritual-completed advancement trigger exists; inspect whether exposed for FTB | Costs source-backed; feasibility and shape partial |
| 5. Upgrade to Tier 3 | Gate | Tier 2; Chiseled Polished Darkstone; 4 Arcane Crystals, 4 Deorum Ingots; 1,000 Aureal, 9,000 Blood, 50 Souls | Perform Tier 3 upgrade ritual | Tier 3 forge; enables at least Eternal Stella ritual | Ritual data | Same teaching requirements; test exact cost deduction and craft result | Partial |
| 6. Upgrade to Tier 4 | Gate | Tier 3; Chiseled Polished Darkstone; 4 Stellarite Pieces, 4 Runes; 2,000 Aureal, 12,000 Blood, 100 Souls | Perform Tier 4 upgrade ritual | Tier 4 forge; Boss Catcher ritual is available | Ritual data | Teach material progression and circle use | Partial |
| 7. Upgrade to Tier 5 | Capstone tier | Tier 4; Stellarite Block; 4 Sculk Catalysts, 2 Dark Nether Stars, 2 Dragon Scales; 5,000 Aureal, 20,000 Blood, 500 Souls | Perform Upgrade Final Tier ritual | Tier 5 forge | Ritual data | Explain sources for all late materials; do not make tier 5 a completion proxy until tested | Costs source-backed; acquisition route unresearched |
| 8. Optional forge recipes | Branch | Tier requirements and ritual-specific items/essence; e.g. tier 3 Eternal Stella and tier 4 Boss Catcher | Stage main and pedestal inputs, circle, and essence; activate and observe result | Eternal Stella, Boss Catcher, equipment, prisms, catcher/injector items and other recipe outputs | Ritual data; exact menu display unverified | Separate lessons by useful branch and action; rewards must not bypass tier gate | Inventory of built-in ritual IDs found; full recipe audit still due |
| 9. Equipment/enhancer/other branches | Optional and potentially major | Exact sources, recipes, and effects not fully reviewed | Craft/use modifiers, trinkets, gear, plants and worldgen items | Combat, utility, or Forge/Clibano enhancements | JEI/tooltips; page references not established | Profile all high-impact systems before chapter design | Unresearched |

### Dependency graph and alternate routes

The source-backed forge tier sequence is linear: tier 1 → 2 → 3 → 4 → 5. Optional ritual recipes branch at their minimum forge tier. Clibano smelting is a separate branch, but it may provide ingredients used by Forge rituals. The current evidence does not support a complete material graph, first-access route, or source-independent guide.

### Structures, rituals, automation and other how-to instructions

| Required setup | Placement/shape/activation and operating inputs | Guidebook page/JEI category to cite | Failure symptoms, throughput or maintenance | Quest where taught |
| --- | --- | --- | --- | --- |
| Clibano | Core recipe exists; the complete multiblock shape and assembly validation still require source inspection. It has input, fuel, Soul, and output slots. | Clibano UI and Clibano Combustion JEI category (category label based on source integration; verify in pack) | Wrong fire tier, no valid recipe, unavailable fuel/output space, or spent Soul timer prevents operation | Pending geometry audit |
| Hephaestus Forge | Forge block entity has 9 slots; main input is slot 4; essence stores map to slots 5–8. Ritual ingredients use pedestal entities in cached range. Exact pedestal/circle pattern not yet confirmed. | Hephaestus Forge UI; exact in-game reference absent | Invalid tier, ingredients, enhancer, circle or essence prevents ritual indicator; after ritual starts, essence is spent. Failure path clears pedestal items. | Pending structure/interaction audit |
| Research Desk | Use Forbiddenomicon item on Desk to transform it; right-click without item opens menu; shift-right-click converts back and returns the book | Research screen; only three knowledge nodes | No acquisition route or detailed instruction tree found | Pending availability test |

## Advanced and terminal objectives

- **Normal completion:** No canonical ending or completion objective found. Tier 5 forge is a strong progression endpoint, not proof that all content is mastered.
- **Optional completion:** Use the Clibano's higher fire recipes, complete useful forge rituals, and audit major equipment/trinket/enhancer/worldgen branches.
- **Remaining progression blockers:** Obtainable path for Forbiddenomicon/Research Desk; first tier Forge recipe and structure; pedestal/circle geometry; essence caps/source loops; full ritual list and material provenance; Clibano recipe/residue catalog; major non-machine branches.
- **Quest coverage check:** Partial. Essential steps and source-backed tier costs have begun to map, but the required how-to references and first-access path remain unresolved.

## Internal combinations and overlooked mechanics

| Internal combination | Full chain | Benefit | Limitation / exploit potential | Evidence |
| --- | --- | --- | --- | --- |
| Soul extraction → Clibano | Soul Sand → Soul Extractor → Soul item → Soul Fire input | Enables Soul Fire mode for recipes that require it and may produce recipe residues | Consumes Soul Sand conversion target and extractor durability; duration is finite | Source code + recipe |
| Enhanced Clibano | Slot item tagged as Enchanted Fire starter → 2.5× cooking-speed fire mode | Faster processing and access to stronger recipes | Starter item is consumed; exact tagged item supply and recipe data need audit | Source tag and enum |
| Forge-tier progression → advanced crafting | Tier upgrades → higher-tier ritual recipes | Unlocks high-end gear, tool/equipment outcomes and utility items | Large typed essence costs; blood cap/farm and other resource pipelines need balancing review | Ritual JSON |
| Forge nearby combat damage → Blood storage | Living entities lose health in 5-block inflated box → manager adds integer health loss × 20 Blood | Passive conversion can support Blood costs in a Forge economy | Entity selection, repeated damage accounting, healing interaction, cadence and exploit resistance need measured tests | `EssenceManager.java` |

## Low-hanging setups useful in ordinary survival

No setup is promoted as an early-game recommendation yet. The extractor requires a modded Utrem Jar and has an unresolved starting recipe chain. The Forge's initial access/geometry and the Clibano layout are not yet fully established. Once those gates are mapped, add three sourced builds with explicit stage, ingredient burden, payoff, and limits.

## API, integration and pack-config surfaces

| Hook or customization surface | Namespace / exact file, API or serializer | Capabilities / constraints | Current TMM override? | Evidence |
| --- | --- | --- | --- | --- |
| Clibano recipes | Recipe type `forbidden_arcanus:clibano_combustion`; data provider `ClibanoRecipeProvider` | Recipe inputs/outputs, required fire, cooking time, optional residue chance and double-input flag are data-driven | No override found in current indexed files; full repo search still needed | Pinned source |
| Forge rituals | Registry `forbidden_arcanus:ritual`; JSON schema includes inputs, main ingredient, result, requirements, magic circle, duration | Supports create-item, transmute-input and upgrade-tier result types; requirements include typed essences, tier predicate, enhancers | No override found in current indexed files; full repo search still needed | Pinned source |
| Tags | `forbidden_arcanus:clibano/creates_soul_fire`, `forbidden_arcanus:clibano/creates_enchanted_fire`, `forbidden_arcanus:hephaestus_forges` | Datapack tags select starter items and accepted forge items | No override found in current indexed files; full repo search still needed | Pinned source |
| Config | Common options: Boom Arrow block damage/radius; Mundabitur Dust creeper charge; Stella Arcanum explosion/block damage/radius; Edelwood ladder speed | Config defaults are source-backed; no global ritual/essence tuning option found in inspected config classes | No override found in current indexed files; full repo search still needed | `ItemConfig.java`, `BlockConfig.java` |
| Capability/data components | Entity essence provider, Essence Value/Storage/Aureal Cost data components | Enables item/entity essence storage and Aureal-cost checks; exact transfer channels still to inspect | Unknown | `EssenceHelper.java` |

## Compatibility leads

| Other mod | Candidate link and why | Native / pack-built / would require bridge | Status / source |
| --- | --- | --- | --- |
| Apotheosis | Compare affixes/enchantments on Forge-made equipment; validate before presenting it as synergy | Candidate only | No compatibility hook confirmed |
| Ars Nouveau | Compare Clibano recipes with existing ore processing and Ars utility spells | Candidate only | No shared API confirmed |
| Occultism | Compare ore/material paths and late-game ritual resource access | Candidate only | No shared API confirmed |
| ATM10 pack recipe layers | Recipe/tag changes may expand or replace the base Clibano material contract | Requires current pack recipe audit | Not yet checked |

## Quest and teaching opportunities (research, not actual quests)

The progression table is the canonical partial outline. No quests were edited or authored.

| Stage and teaching goal | Actionable in-game instructions | Named guidebook section or JEI recipe | Intended behavior vs truthful detector | XP/reward direction | Coverage and gating risk |
| --- | --- | --- | --- | --- | --- |
| Find the research UI | Verify whether Research Desk and Forbiddenomicon are available; if so, craft/use book on Desk and right-click | Research screen; no guide pages | Item tasks cannot prove reading or research actions | Small XP; avoid free progression item until acquisition confirmed | Hard access gap |
| Extract Souls | Craft the extractor, aim at Soul Sand, hold use for 35 ticks, collect Soul, observe Soulless Sand | JEI Soul Extractor recipe/uses | Soul acquisition is a proxy; does not prove conversion | Modest XP and a reusable tool supply if safe | Utrem Jar chain unknown |
| Operate Clibano | Assemble verified multiblock; fuel it; put recipe item in input; observe result; repeat with a valid fire starter | Clibano Combustion category | Recipe output item is a proxy; actual operation trigger needs advancement scan | XP + ordinary processing materials, not rare gated inputs | Layout still unknown |
| Forge tier upgrades | Teach exact ingredients, essence sourcing and circle activation for each transition | Forge ritual data/UI; no named tutorial found | Source includes a ritual-completed trigger; determine if FTB can consume it, otherwise item/tier proxy plus manual observation | XP plus non-gating supplies | Essence source and structure instructions incomplete |
| Teach optional rituals/equipment | One meaningful branch per quest after full input/output audit | Forge ritual data, JEI when confirmed | Use ritual advancement/event if available; crafted item alone is not operation proof | XP + modest supporting supplies | Full ritual audit pending |

## Existing chapter / pilot reconciliation

| Walkthrough stage | Current quest ID/title and status | Intended action vs actual detector | Named in-game guide/JEI reference | Gap / next action |
| --- | --- | --- | --- | --- |
| All stages | No Forbidden & Arcanus quest chapter was found in the profile/index pass; no generated quest file was changed | No detector inventory performed | Research screen and JEI references unverified in client | Before quest handoff, search canonical authoring data/generated quests and verify advancement integration |

## Conflicts, limitations and version drift

- Upstream Modrinth identifies 2.6.1 for Minecraft 1.21.1 NeoForge and requires Valhelsia Core; the pack manifest records `dependencies_verified: false`. The source metadata declares Valhelsia Core 1.1.5. Do not treat that as confirmation of the pack's installed dependency version.
- The upstream repository has later releases; this profile is deliberately scoped to the pack's 2.6.1 pin. Do not import later behavior.
- Source snapshot is tied to the commit that declares version 2.6.1, not a verified build of the pack's pinned JAR.
- Source's RitualManager contains a hardcoded zero ritual failure chance despite retaining lightning/failure code. Treat successful rituals as source-expected; validate runtime.
- Tier ritual eligibility applies essence modifiers while the start path visibly reduces the ritual's unmodified essence definition. This may be a source bug or an incomplete modifier path; test with a relevant enhancer before documenting modified costs as actual.
- The current research tree has only three informational nodes and no detailed progression content. It should not be mistaken for a complete guide.
- No quest or runtime changes were made, and no tests were run.

## Evidence ledger

| Claim / section | Source URL or exact inspected file + immutable SHA | Evidence tag / version | What it proves / remaining limits |
| --- | --- | --- | --- |
| Pack pin | `mods/forbidden-arcanus.pw.toml`; `manifest/mods.json` on `test/full-integration` | [pin] 2.6.1, Modrinth version `fNjxgZPH`, SHA-512 above | Identifies selected artifact, not local JAR inspection |
| Source version | [upstream commit](https://github.com/stal111/Forbidden-Arcanus/commit/f62af610d550e0d033f6c5cd166e40062638c44b), `gradle.properties`, `neoforge/src/main/resources/META-INF/neoforge.mods.toml` | [source: `f62af610...`] | Declared source version/dependencies; no binary equivalence proof |
| Soul extraction | `common/item/SoulExtractorItem.java`, `data/forbidden_arcanus/recipe/soul_extractor.json` | [source: `f62af610...`] | Valid target, hold duration, block replacement, output entity and recipe |
| Clibano | `common/block/entity/clibano/ClibanoMainBlockEntity.java`, `ClibanoFireType.java`, recipe JSON | [source: `f62af610...`] | Fire tiers, timer, double recipe and residue processing code; full recipe list not yet audited |
| Forge rituals | `common/block/entity/forge/ritual/Ritual.java`, `RitualManager.java`, generated ritual JSON | [source: `f62af610...`] | Schema, costs, start/failure paths, tier-upgrade ingredients |
| Research UI | `common/block/DeskBlock.java`, `ResearchDeskBlock.java`, `data/research/ModKnowledge.java`, generated knowledge JSON | [source: `f62af610...`] | Desk transforms and opens UI; only three knowledge nodes; acquisition absent in scanned data |
| Essence collection | `common/block/entity/forge/essence/EssenceManager.java`, `common/essence/EssenceHelper.java` | [source: `f62af610...`] | Blood gain hook and existence of capability helpers; complete ecology not mapped |
| Config | `core/config/ItemConfig.java`, `BlockConfig.java` | [source: `f62af610...`] | Listed defaults and constraints only |

## Disposable-world test plan

| Test | Positive/boundary case and disposable-world setup | Expected observable result (actual mechanic and quest credit separately) | Performed? / evidence |
| --- | --- | --- | --- |
| Exact version/client UI | Install pinned pack on clean disposable world; inspect JEI, research desk, feature flags | Confirm item registration, recipe availability, research access, visible tabs | Not run |
| Soul extraction | Extract from Soul Sand with valid tool; then try invalid block and permission-denied target | Valid use costs one durability, emits one Soul and converts target; invalid target changes neither | Not run |
| Clibano fire | Run same recipe in ordinary, Soul, and Enchanted Fire modes; inspect timer, speed, residue, fuel/output gating | Match source recipe fire gates, 1.0×/1.5×/2.5× speed and 2,700-tick base duration | Not run |
| Clibano double recipe | Place valid pair vs mismatched inputs; compare consumed stacks and output | Both consumed together only for double recipe; rejected recipe consumes neither | Not run |
| Forge costs/failure | One each of tier-upgrade ritual with exact resources; interrupt after start; induce any supported invalidation | Observe cost deduction timing, item return/clear behavior, indicator, output and advancement event | Not run |
| Blood essence | Controlled nearby damage/healing with entities just inside/outside five-block inflated bounds | Measure source formula separately from observed tick/cap behavior; examine repeated-damage accounting | Not run |
| Ritual modifier | Use Forge enhancer that modifies required essence and compare eligibility with actual deduction | Establish whether the source-level modified-check/unmodified-deduction discrepancy affects play | Not run |
| Tier 5/capstone branch | Disposable late-game inventory, perform tier 5 and one tier-gated ritual | Confirm final tier, item consumption, circle behavior and item result | Not run |

## Unknowns and next research

- [ ] Determine whether the pinned JAR can be inspected locally and verify binary-to-source equivalence.
- [ ] Find the intended survival route for Forbiddenomicon and Research Desk; explain the disabled feature state.
- [ ] Map tier-1 forge recipe, structure shape, pedestal count/range, circle geometry, menu controls and output handling.
- [x] Audit all 22 built-in ritual JSONs at the version-aligned source snapshot: result types, exact declared inputs/essence costs, tier/enhancer fields, and outputs are cataloged below. Source item routes and pack-effective overrides remain open.
- [ ] Map Aureal/Souls/Blood/Experience collection and each tier's maximum essence capacity; test essence transfer sources.
- [ ] Finish Clibano geometry, recipe catalog, residue registry, valid fuel/fire inputs and sided automation.
- [ ] Audit the remaining material, worldgen, equipment, trinket, enchantment, plant and creature branches.
- [ ] Search current TMM recipe/config/script layers for ritual overrides; quest integration remains out of scope for this knowledge-only pass.
- [ ] Run disposable client tests before any profile can become verified-in-pack.

## Revision notes

- 2026-11-16 — Created source-backed partial profile for the pinned 2.6.1 selection. Recorded the research UI gap, extractor contract, Clibano fire mechanics, tier-upgrade costs and ritual source-level risks. No quest/runtime files changed.
- 2026-11-16 — Audited the full 22-entry built-in ritual catalog from the 2.6.1-aligned generated-resource cache and JSONs; corrected the earlier 21-entry count and recorded each result, declared input, essence cost, and explicit tier/enhancer gate. Source-to-JAR parity, pack overrides, item routes, and in-world execution remain open. No quest/runtime files changed.
