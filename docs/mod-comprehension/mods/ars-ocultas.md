# Ars Ocultas — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `ars-ocultas` / `ars_ocultas` |
| Category / parent or addons | magic addon; bridges [Ars Nouveau](ars-nouveau.md) and [Occultism](occultism.md) |
| MC / loader / pack target | Minecraft 1.21.1 / NeoForge / TMM snapshot `7ec35309b67ac393effdfc5afbacc1c525aa1ecd` |
| Pinned version / artifact / hash | `mods/ars-ocultas.pw.toml`; `ars_ocultas-1.21.1-2.6.2.jar`; SHA-512 `4a673096a2d0b9a5a83aca2747a8a08211205ed40875b4c22061f774146d9d4fff9c872e2c1b6b59e5fef2d1bf3b3a08732f22743ade4e0a82ba08636dc701a5` |
| Source snapshot | `dphaldes/Ars-Ocultas`, commit [`14aa0f4a05f8e85aca03b9bbe084e2bff36b007b`](https://github.com/dphaldes/Ars-Ocultas/tree/14aa0f4a05f8e85aca03b9bbe084e2bff36b007b), declares 2.6.2 / MC 1.21.1; pinned JAR hash verified, source/JAR bytecode equivalence not established |
| Guide/documentation version gap | matching English guide entries are built into Ars Nouveau's Worn Notebook via source event; live rendering not tested |
| Profile status | researching |
| Evidence reached | exact pinned JAR SHA-512 verified; metadata, data and version-aligned source inspected; official ATM10 inventory lists the identical 2.6.2 artifact for Minecraft 1.21.1, overturning the inferred load blocker; TMM client behavior remains untested |
| Reviewed | 2026-11-15 |
| Quest ownership | optional branch in Ars pilot draft; current integration branch still has the Ars chapter stub; see reconciliation below |

## Executive gameplay map

**What the addon does:** Ars Ocultas adds Occultism spirits to Ars Nouveau's Mob Jar behaviors. Foliot, Djinni, Afrit and Marid spirits can be stored in a jar, keep their normal Occultism job, receive job-compatible items, and expose an item-handler capability. A spirit can accept nearby filtered item drops, player right-click inputs or automation; Janitor spirits can push carried items into an adjacent inventory. The addon adds a Sacrificial Altar that interfaces with an Occultism Golden Sacrificial Bowl using Ars Source, plus recipes for a Spirit Attuned Gem, bidirectional silver/gold conversion, an Occultism tier-4 storage stabilizer, and the altar itself. It adds documentation pages to the Worn Notebook rather than a separate book.

**Why it matters in TMM:** the spirit jar bridge combines Occultism labor with Ars containers and logistics, and the gem/transmutation recipes offer alternate material routes. The Sacrificial Altar is a major ritual-gating surface: source code indicates it signals Occultism that a sacrifice or item-use condition was fulfilled without consuming the jarred entity or matching altar item. Treat it as a potential gate bypass until a live test and a design decision establish acceptable behavior.

**Compatibility status:** the pinned JAR's declared Minecraft range is recorded as `[1.21,1.21.1)`, which appears to exclude 1.21.1 under NeoForge's documented Maven-range syntax. However, the official ATM10 repository identifies ATM10 as a 1.21.1 pack and its current crash-assistant mod inventory lists `ars_ocultas-1.21.1-2.6.2.jar` with Modrinth hash `de39ec2ee0503e3f938bb167565414d16314d26c`, matching TMM's exact pinned file. This contradicts the earlier conclusion that the metadata makes the artifact unusable on 1.21.1, so the dependency range is no longer treated as a blocker. The discrepancy and TMM's own client behavior still need direct verification; no TMM launch was performed. [TMM pin] [ATM10 README](https://github.com/AllTheMods/ATM-10/blob/main/README.md) [ATM10 mod inventory](https://github.com/AllTheMods/ATM-10/blob/main/config/crash_assistant/modlist.json) [NeoForge 1.21.1 Mod Files](https://docs.neoforged.net/docs/1.21.1/gettingstarted/modfiles/)

**Quick start:** read Ars Nouveau's Worn Notebook → Ars Ocultas. Bind an Occultism worker spirit using the parent progression, place it in an Ars Mob Jar, and supply only items accepted by that spirit's Occultism pickup filter. A compatible spirit jar uses a 7×7×7 block-space AABB around the jar for automatic item pickup; the job's own processing remains Occultism behavior. The Spirit Attuned Gem is an Enchanting Apparatus recipe. The Sacrificial Altar is a second apparatus recipe; if testing it, use a disposable world and a low-risk Occultism ritual. [pin] [jar: SHA512] [source: 14aa0f4]

### Player-value snapshot

| Five standout capabilities | Three early practical unlocks | Three integration leads |
| --- | --- | --- |
| 1. Store Occultism workers in Ars jars without their normal lifetime decay 2. Feed the stored worker by hand or through item capability 3. Auto-pickup filtered drops around the jar 4. Give Janitors a nearby inventory destination 5. Bridge Ars Source into Occultism sacrifice/item-use waits | 1. Spirit Attuned Gem as an alternate Enchanting Apparatus path 2. Jarred Crusher or Smelter, if its normal Occultism bind recipe is already unlocked 3. Reverse silver/gold transmutation at 2,000 Source per operation | 1. Crusher/Smelter/Crystallizer/Trader job results can insert into adjacent inventories from the jar position 2. Janitor moves held items to adjacent inventories 3. Sacrificial Altar can satisfy bowl conditions without visibly consuming the jar target or item in its inventory; this is a high-risk gate behavior, not a recommended integration until tested |

### Subsystem coverage

| Subsystem | Evidence | Progression completeness | Guide/JEI reference | Client validation | Next action |
| --- | --- | --- | --- | --- | --- |
| Spirit Attuned Gem | exact recipe and README | recipe complete; alternative/base cost comparison not audited | Worn Notebook → Ars Ocultas → Spirit Attuned Gem; JEI | not run | verify apparatus and Source consumption |
| Spirit jars | exact JAR resources + `SpiritJarBehaviour.java` | Foliot/Djinni/Afrit/Marid behaviors registered; job filter and nearby handoff mapped | Ars Ocultas → Spirit Jars; JEI for Ars Mob Jar and Occultism worker | not run | test exact radius, pickup cadence, job output, persistence and item capability |
| Sacrificial Altar | exact recipe + addon `AltarBlockEntity`; Occultism bowl source | key predicates and costs mapped; source reveals non-consuming flag signaling | Ars Ocultas → Sacrificial Altar / JEI; Occultism ritual guide | not run | test correct/incorrect sacrifice and item-use rituals, source costs, item/entity consumption |
| Silver/gold transmutation | exact imbuement recipes + source datagen | both directions complete; 1 ingot each way | Worn Notebook → Transmutation | not run | verify Source draw and net economy with exact source/gem costs |
| Storage stabilizer tier 4 | exact JAR ritual JSON | one Marid-binding ritual recipe; parent pentacle and tier gate not reverified here | JEI / Occultism Dictionary ritual recipe | not run | verify altar draw, recipe visibility and exact recipe/pentacle in client |
| Soul Gem ↔ Mob Jar integration | exact addon source | config toggles and intended entity-data transfer inspected; negative validation ordering may destroy data | no distinct Worn Notebook entry found | not run | test denied entity, item restoration, full/empty jar and all Soul Gem types |

## Guidebook onboarding

- **Guide and access:** Ars Nouveau's Worn Notebook (Patchouli documentation); the addon registers the `ars_ocultas` main category through Ars Nouveau documentation events. It adds entries for Spirit Attuned Gem, Spirit Jars, and Transmutation. Source does not show the addon crafting a standalone guide item.
- **Earliest stage:** once the player has the Worn Notebook and can craft Ars's Enchanting Apparatus or Imbuement Chamber; the worker branch additionally needs a bound Occultism spirit and Ars Mob Jar.
- **First teaching goal:** open the Ars Ocultas category, compare the Spirit Attuned Gem recipes in JEI, then make one worker jar and prove it processes one valid item. Teach the Altar as a risky optional bridge separately.
- **Guide limits:** the Spirit Jars page gives the 7×7 pickup description and Janitor handoff, but it does not teach a particular worker's full Occultism ritual or give measured throughput. The altar page is not a complete safety guide: it does not state the 5,000 Source debits or the source-derived non-consumption behavior.

## Systems and typed inputs/outputs

| System | Input kind and exact recipe/event | Cost / consumption | Output / cadence / interface | Limit and evidence |
| --- | --- | --- | --- | --- |
| Spirit Attuned Gem | Enchanting Apparatus reagent `ars_nouveau:source_gem`; pedestal items `ars_nouveau:conjuration_essence`, `ars_nouveau:manipulation_essence`, `occultism:demons_dream_essence` | Recipe declares 500 Source; normal Ars apparatus consumes recipe inputs when valid | 1 `occultism:spirit_attuned_gem`; crafted apparatus output | exact recipe JSON; alternative route and whether base recipe is cheaper depends the pinned Ars/Occultism recipes |
| Tier-4 Storage Stabilizer | Occultism ritual, `craft` type; activation `occultism:book_of_binding_bound_marid`; pentacle `occultism:craft_marid`; duration 240; inputs include tier-3 stabilizer, Iesnium block tag, Ender Eye, Spirit Attuned Crystal, End Rod, Shulker Shell, Dragon Breath | exact listed ritual ingredients; Occultism handles ritual payment and consumption | 1 `occultism:storage_stabilizer_tier4` | exact addon JAR data; parent recipe cost/crafting-world result not client-verified |
| Silver ↔ gold | Ars Imbuement Chamber; one Gold Ingot → one Occultism Silver Ingot, or one Silver Ingot → one Gold Ingot; each recipe uses Manipulation Essence, Spirit Attuned Gem and a direction-specific third reagent | 2,000 Source per ingot; recipe inputs consumed by Imbuement | one ingot of the other metal; Source-powered machine | Source and ingredients make this a conversion convenience, not free transmutation; verify actual Source use/recipe in client |
| Spirit in Mob Jar | `ars_nouveau:mob_jar` contains Occultism `SpiritEntity`; only four entity types are registered: Foliot, Djinni, Afrit, Marid | Binding is Occultism's own progression; capturing/placing with Soul Gem uses saved entity data; normal jarred-spirit decay is absent per guide | spirit retains Occultism job and inventory; jar has item capability; right-click items accepted by `OccultismAPI.getItemsToPickUp` | server tick: job updates every tick; auto-pickup scan every 40 game ticks and inserts the first eligible nearby ItemEntity. Code AABB inflated by 3 in X/Y/Z = 7×7×7 blocks. Exact processor throughput is job-specific and not measured. |
| Spirit job output forwarding | Occultism Crusher, Smelter, Crystallizer or Trader job event from a spirit located in a Mob Jar | output item stack passed through adjacent Ars inventory manager; insertion can partially succeed | remainder stays in the spirit job event output; code attempts insertion into inventories adjacent to jar | exact 2.6.2 source; the README makes a broader capability claim, but per-job GUI/overflow behavior needs testing |
| Janitor handoff | spirit's main-hand stack; adjacent inventory handlers around jar | no extra mod currency; stack inserted with remainder retained in spirit's hand | each server tick while jarred Janitor job is active | `SpiritJarBehaviour` transfers before `SpiritJob.update`; no adjacent inventory leaves item in hand |
| Sacrificial Altar recipe | Enchanting Apparatus reagent `occultism:golden_sacrificial_bowl`; pedestals: four Occultism Silver Ingots, two Source Stone, Drygmy Shard, Mob Jar | 5,000 Source to craft; one addon Altar block | one `ars_ocultas:altar`; block has 27 one-item GUI slots and an item capability | exact JAR recipe, source `altar.json`, menu and capability registration |
| Altar → Occultism bowl | altar sits directly below Golden Sacrificial Bowl; altar scans for valid living entity inside Mob Jar (Manhattan search centered 3 above altar, extents 10/6/10) or a matching item among 27 altar slots | each pending flag costs 5,000 Ars Source within 10 blocks. Code calls `notifySacrifice(null)` / `notifyItemUse(null)`. It does not kill/remove the jarred entity; item-use code has an explicit TODO for consuming or changing the item, and leaves it in its slot. | sets Occultism bowl's persisted `sacrificeProvided` or `itemUseProvided` flag; Occultism ritual then advances if its other requirements pass | cross-checked with Occultism release bowl: the flags are checked as wait conditions; normal sacrifice flag comes from a qualifying player-caused death nearby and normal item-use from a qualifying right-click. The altar's alternate call does not perform those actions. This is source-proven possibility, not live-tested behavior. |
| Soul Gem and Mob Jar | Player right-click on Mob Jar with Soul Gem; addon config defaults both capture and placement toggles true | empty gem on eligible jar stores entity NBT then removes jar entity; filled gem removes `ENTITY_DATA` before deny-list validation, then attempts to create/place entity | entity can move jar ↔ Soul Gem, including its saved NBT; fragile Soul Gem is consumed on successful placement | exact source. A denied filled gem may lose its stored data because validation occurs after NBT removal; a separate tag-check path sets failure but does not immediately return. Test before relying on recovery. |

### Operation order, failure costs and exploit boundaries

The most consequential addon behavior is at the Altar. Every 300 game ticks (15 seconds at nominal 20 TPS), the block entity checks the Golden Bowl directly above. If the current ritual still lacks its sacrifice flag, it finds a matching living entity held in an Ars Mob Jar within its search volume, spends 5,000 nearby Source, and sets the flag. No code path in that branch removes the jar entity or calls the normal player-caused death event. If the bowl still lacks item use, the altar checks its 27 slots for a matching item, spends another 5,000 Source, and sets the item-use flag; the code comment explicitly leaves item consumption/state change TODO, and the stack stays in place. These branches could let an entity and item act as reusable ritual catalysts. Whether a particular ritual completes still depends on its own structure, base/catalyst item, additional ingredients, duration and other validity checks. [source: 14aa0f4 `AltarBlockEntity.java`; source: 990303e `GoldenSacrificialBowlBlockEntity.java`, `Ritual.java`]

The second sensitive operation is filled Soul Gem placement into an empty Mob Jar. The handler removes the entity-data component before all blacklist checks finish. The test plan should include a disallowed entity and confirm whether the gem's captured entity data is lost; this is a code-order risk, not an observed player result. Do not use rare or progression-gated creatures for the first test.

## Complete player progression / walkthrough blueprint

Ars Ocultas has no mandatory standalone tier ladder. It is an optional addon branch whose prerequisites come from the two parent mods.

| Stage | Gate or choice | Action / result | Guide or JEI reference | Truthful quest criterion / limitation | Evidence |
| --- | --- | --- | --- | --- | --- |
| 0. Discover addon guide | optional | open Worn Notebook → Ars Ocultas | category entries Spirit Attuned Gem, Spirit Jars, Transmutation | reading is not exposed as a known FTB detector; use manual/open guide instruction | `DocumentationHandler.java`; matching English lang JSON |
| 1. Choose gem route | optional | compare base and addon recipes; craft gem through Enchanting Apparatus | Spirit Attuned Gem + JEI | possession proves a gem, not which route; teach it as an alternate recipe | exact recipe JSON |
| 2. Obtain a worker spirit | parent prerequisite | use Occultism Dictionary and recipe to bind the desired worker; keep its job/ownership state | Occultism Dictionary / matching ritual; Spirit Jars entry | summoning/ownership must be real; addon doesn't grant spirits | Occultism profile and source |
| 3. Jar and configure spirit | optional automation | use Ars Mob Jar and valid Soul Gem flow; set filter if needed; inspect GUI; supply one accepted item manually or via hopper | Spirit Jars; Occultism job guide | jar item task only proves hardware; test actual processed output separately | `SpiritJarBehaviour.java`, README |
| 4. Collect worker output | selected by job | place adjacent inventory for Janitor or supported job output; for Crusher ensure its normal output is collected; test hopper side | Spirit Jars; Occultism role entry | require correct output from actual job, not jar possession | source events; no client test |
| 5. Convert silver/gold | optional recipe branch | craft/use Spirit Attuned Gem; provide named ingredients and 2,000 Source in Imbuement Chamber | Transmutation; JEI | item output detects one conversion, no free XP/coin award; confirm consumed inputs | exact recipe and datagen source |
| 6. Craft Tier 4 stabilizer | advanced Occultism branch | complete Marid recipe/pentacle with listed ingredients and bound Marid book | JEI / Occultism Dictionary | block output proves recipe only; parent source tier and Source? requirements need validation | exact addon ritual JSON |
| 7. Use Sacrificial Altar | optional, high risk | put Altar below Golden Bowl; test a simple sacrificial and item-use ritual with disposable inputs | no complete safety directions in guide; inspect Occultism ritual and addon Altar | don't author a survival quest until behavior is tested; source suggests reusable target/item and 5k Source per step | addon and parent source; no pack test |
| 8. Transfer spirit with Soul Gem | optional utility | test empty/filled supported gem with empty/full jar and every relevant restriction | Soul Gem and jar tooltips | manual fallback; negative case may lose captured data after denied placement | `EventHandler.java`, config |

## Internal combinations and useful setups

| Combination | Full chain | Payoff | Cost or risk | Evidence |
| --- | --- | --- | --- | --- |
| Jarred Crusher | Occultism Crusher ritual → Mob Jar → feed eligible ore by nearby item, hand or item capability → collect its normal result or adjacent inventory insert | worker runs without decaying and can live in compact automation | no throughput measured; exact accepted ore filters are Occultism job rules; output may be dropped or forwarded according to job event | guide + addon source; not tested |
| Jarred Janitor | bind Janitor → put in jar → feed eligible dropped items → place output inventory next to jar | self-cleans and exports items automatically | only adjacent inventory handlers; no target-side selection documented; item transfer runs every tick | README + source |
| Silver/gold conversion | silver or gold ingot + matching third reagent + Manipulation Essence + Spirit Attuned Gem → Imbuement | convert surplus from one metal into the other | 2,000 Source plus consumed reagents; no net gain of precious metal | exact recipes |
| Sacrificial Altar shortcut | matched entity in Mob Jar and/or matching item in altar → Altar below Golden Bowl → 5,000 Source and a satisfied bowl flag | can automate a ritual condition without a player killing the sacrifice or right-clicking item | source says target entity/item remain. May bypass sacrifice or consumable gates; do not treat as acceptable/balanced until tested | both parent/addon source; no in-world verification |

## API, config and pack integration surfaces

| Surface | Exact hook | Capabilities / limits | TMM override or status |
| --- | --- | --- | --- |
| Ars Mob Jar behavior | `JarBehaviorRegistry.register` for Occultism Foliot, Djinni, Afrit, Marid | Spirit job GUI wrapper; jar tick uses OccultismAPI pickup filter; item handler capability from spirit | no TMM script override found in inspected paths |
| Spirit job output | Occultism `CrusherJobEvent`, `SmelterJobEvent`, `CrystallizerJobEvent`, `TraderJobEvent` | insert output remainder into adjacent inventory manager around jar | requires both exact mods; no pack client test |
| Sacrificial altar | Ars apparatus recipe; addon `AltarBlockEntity`; Occultism Golden Bowl public flags and recipe predicates | 27 one-item slots, block capability; converts Source into persisted bowl condition flags, with the non-consuming behaviors above | no TMM override; record as a potential progression/exploit blocker |
| Soul Gem capture/place | NeoForge `PlayerInteractEvent.RightClickBlock`; addon config `containment_jar.soul_gem.pickup/place`, both default true | transfers Mob Jar entity NBT into/out of Soul Gem; restriction order needs test | no TMM config override found; inspect default generated config before release |
| Recipes and documentation | `data/ars_ocultas/recipe/*.json`, `data/ars_ocultas/recipes/*.json`, Ars documentation registration | apparatus, imbuement and Occultism ritual serializers | five addon recipe JSONs in exact JAR: altar, Spirit Attuned Gem, gold transmute, silver transmute, tier-4 stabilizer |
| Dependencies | addon metadata requires Minecraft `[1.21,1.21.1)`, Ars Nouveau `[5.3,6)`, Occultism `[1.220.4,)`; source Gradle pins older compatible API versions | Ars `[5.3,6)` and Occultism `[1.220.4,)` include the TMM pins. The declared range appears to exclude 1.21.1 under NeoForge range syntax, but the official ATM10 inventory lists this exact artifact in its 1.21.1 pack; the runtime discrepancy is unresolved and is not considered a blocker. | exact lock identities in `mods/*.pw.toml`; compare pinned artifact identity to official ATM10 inventory; no TMM client launch performed |

## Existing chapter / pilot reconciliation

The current integration branch's Ars chapter contains the early generic orientation only; the 49-quest Ars pilot remains on `feature/ars-quest-pilot` and is not included in this branch's tree. Its canonical `manifest/ars_quest_pilot.json` entries 40–42 are `ocultas_gem`, `ocultas_jar`, and `ocultas_altar`; each criterion is item possession. Rewards are 40 XP + four purple candles, 55 XP + two hoppers, and 80 XP + four crying obsidian, respectively. Generated chapter tasks use the corresponding gem, Mob Jar and Altar IDs, with non-consuming item tasks. The gem quest explicitly admits it cannot distinguish the recipe route; jar and altar quests describe tests but credit only equipment possession. They do not claim successful automation or a ritual, which is a truthful detector but leaves operation teaching to the prose/manual follow-up.

## Quest and teaching opportunities (research only)

| Goal | Actionable teaching | Reference | Detector and risk |
| --- | --- | --- | --- |
| Choose gem route | inspect both JEI outputs, make the gem in apparatus and budget 500 Source | Spirit Attuned Gem | inventory objective is honest but route-agnostic |
| Prove a spirit worker | bind one worker, jar it, supply a valid item and observe a real processed output | Spirit Jars + Occultism job page | no task for item ownership alone to prove processing; test FTB output trigger first |
| Operate altar | test low-risk sacrifice and item-use rituals in a disposable world with valid and invalid entities/items, keeping counts and Source visible | Sacrificial Altar + Occultism recipe page | do not teach the reusable-target/item path as intended behavior until independently confirmed and balance decision made |
| Test Soul Gem transfer | place/capture an ordinary allowed worker; test denied tag with a duplicate entity in disposable world | Soul Gem and Mob Jar controls | item task doesn't prove NBT preserved; manual verification needed |
| Use transmutation | operate Imbuement Chamber both directions; record source and ingredient loss | Transmutation | produced ingot is a useful detector; confirm exact input costs in client |

## Evidence ledger

| Claim | Exact evidence | What it proves / limit |
| --- | --- | --- |
| Artifact identity | `mods/ars-ocultas.pw.toml`; downloaded Modrinth file `qLGJRyji`; SHA-512 above | exact pinned binary verified; profile does not commit the JAR |
| Version-aligned source | `dphaldes/Ars-Ocultas` commit `14aa0f4a05f8e85aca03b9bbe084e2bff36b007b`, `gradle.properties`, `README.md` | declares addon 2.6.2, MC 1.21.1 and dependency ranges; source is version-aligned, not proven binary build source |
| Spirit jar | `content/spirit_jar/SpiritJarBehaviour.java`, `init/IntegrationRegistrar.java`, `init/RegisterEvents.java` | exact pickup bounds/cadence, filters, janitor handoff and registered spirit types |
| Altar bug path | addon `content/altar/AltarBlockEntity.java`, `AltarMenu.java`, `init/RegisterEvents.java` | 300-tick checks, 5,000 Source per fulfilled flag, matching mob/item predicates, one-item slots, non-consumption; no game test |
| NeoForge range syntax | [NeoForge 1.21.1 mod-file documentation](https://docs.neoforged.net/docs/1.21.1/gettingstarted/modfiles/) | states dependency version ranges are Maven Version Ranges; `[1.21,1.21.1)` has an exclusive upper endpoint. This establishes the apparent metadata discrepancy, but not incompatibility in practice because the official ATM10 mod inventory lists the identical artifact on 1.21.1; TMM's own loader response remains untested. |
| Parent ritual flags | Occultism release source commit `990303ea95ba057791b7d407d6cac5151f66d799`, `GoldenSacrificialBowlBlockEntity.java`, `Ritual.java` | confirms sacrifice/item flags gate ritual ticking and normal flag event paths; source does not verify installed client behavior |
| Recipes/docs | exact JAR `data/ars_ocultas/recipe/`, `data/ars_ocultas/recipes/`, `assets/ars_ocultas/lang/en_us.json` | exact recipe and translation data; app interface/rendering not observed |
| Pilot scope | `feature/ars-quest-pilot:manifest/ars_quest_pilot.json` and generated `config/ftbquests/quests/chapters/ars_nouveau.snbt` | entries 40–42 and actual task/reward mappings; feature branch has not been merged into current integration HEAD |

## Disposable-world test plan

| Test | Positive/boundary setup | Expected observation | Performed |
| --- | --- | --- | --- |
| Spirit pickup | valid filtered item vs invalid item; one drop inside 3-block AABB and one just outside; observe 40-tick cadence | only valid in-range item enters spirit; record actual process and output | no |
| Spirit input capability | hand, hopper and Starbuncle insertion with matching and nonmatching items; full spirit inventory | correct filter acceptance, no loss, correct remainder | no |
| Job output forwarding | Crusher/Smelter/Crystallizer/Trader with no adjacent inventory, one adjacent inventory, and full output inventory | dropped/remainder output matches source event; no duplication/loss | no |
| Altar sacrifice bridge | valid living entity in Mob Jar vs same entity absent/wrong tag; bowl ritual requires sacrifice; sufficient vs insufficient Source | record if matching jar alone satisfies flag, whether any entity is consumed, and 5,000 Source debit | no |
| Altar item-use bridge | required matching item in altar slot vs missing/wrong item; sufficient vs insufficient Source | observe ritual start and whether item stack stays unchanged after altar flags bowl | no |
| Altar scheduling/structure | bowl above, bowl adjacent, altar removed during a ritual, test on 300-tick boundaries | only directly-above bowl is served; cadence and restart/rollback captured | no |
| Soul Gem transfer | allowed vs denied entity type; empty/full gem and jar states; fragile/ordinary Occultism gem | preserved entity/job/NBT; denied placement doesn't silently clear entity data | no |
| Silver/gold | execute both recipes with exactly enough Source and one less than cost | one output each with expected 2,000 Source; failed operation does not erase ingredients | no |

## Unknowns and next research

- [ ] Reconcile the apparent `[1.21,1.21.1)` metadata range with the official ATM10 1.21.1 inventory listing the exact same artifact; inspect effective metadata/loader behavior and perform a TMM client launch when available. This is an unresolved discrepancy, not a blocker.
- [ ] Run the altar boundary tests before authoring a quest or treating the bridge as normal ritual behavior; source strongly predicts reusable sacrifice/item catalysts.
- [ ] Verify the Soul Gem invalid-type ordering and whether failed placement loses NBT in the pinned runtime.
- [ ] Test spirit GUI, filter interaction, jar capture/placement, job persistence, all four tiers, output side behavior and 7×7×7 bounds.
- [ ] Audit the parent Occultism route and exact tier gate for the tier-4 stabilizer recipe in JEI/Dictionary.
- [ ] Reconcile the separate Ars pilot branch after it is merged; do not call quests implemented/current from a feature branch.

## Revision notes

- 2026-11-16 — Reconciled the profile snapshot and confirmed the required Minecraft metadata range excludes TMM's 1.21.1 target under NeoForge's documented Maven range rules. Marked Ars Ocultas `blocked`; no runtime launch was performed and no mod/pack files were changed. The existing bridge and quest research remains useful if an upstream artifact fixes its dependency metadata.
- 2026-11-15 — Replaced README-only summary with exact hash-verified 2.6.2 source/resource audit. Mapped jar event/capability behavior, all four registered spirit tiers, Source costs and both metal transmutations. Found source-derived Sacrificial Altar behavior that signals Occultism ritual flags without consuming the matching jarred entity/item; traced the parent flags and listed negative/positive tests. Added Soul Gem failure-order risk and reconciled the unmerged quest pilot. No quest or runtime files changed.

- 2026-11-16 — Corrected the blocked status after the user pointed out ATM10's 1.21.1 use. The official ATM10 repository identifies the pack as 1.21.1 and lists Ars Ocultas 2.6.2 with the same Modrinth hash as TMM's pin. Reclassified the profile as `researching`; the declared version-range discrepancy remains an open verification item, but no longer blocks investigation. No TMM launch or gameplay test was performed.
