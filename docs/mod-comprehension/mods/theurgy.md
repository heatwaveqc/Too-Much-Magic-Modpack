# Theurgy — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `theurgy` / `theurgy` |
| Category / parent or addons | magic; standalone alchemy system; see [Occultism](occultism.md) when available |
| MC / loader / pack target | 1.21.1 / NeoForge / TMM `test/full-integration` |
| Pinned version / artifact / hash | 1.21.1 NeoForge 1.79.0; `theurgy-1.21.1-neoforge-1.79.0.jar`; SHA-512 `2e92aa0a3462d400feed398d4a675f9492dab3613ad698f24fcbcce34b565a079a7362fb637c4e268dbb71be5d11fc6630758f149ecccc8d6603f2e9d96f0569`; Modrinth version `EBO0aQOY` |
| Source snapshot | Upstream release ref `release/v1.21.1-1.79.0`; release notes date 2026-11-09. **Source metadata mismatch:** this ref's `gradle.properties` declares mod version 1.47.1 and NeoForge 21.1.13. Source/JAR binary equivalence is not established. |
| Guide/documentation version gap | Modonomicon is installed (1.123.0); upstream describes the Hermetica as the in-game guide. Online recipe/datagen docs are not pinned to 1.79.0. |
| Profile status | researching |
| Evidence reached | exact packwiz pin; documented upstream; source metadata spot-check; not tested in pack |
| Reviewed | 2026-11-16 |
| Quest ownership | No quest edits in this research pass |

## Executive gameplay map

**What the player actually does:** Theurgy is a classical-alchemy progression built around manipulating matter through Sulfur, Salt and Mercury, then using the resulting alchemical materials to reproduce or transform substances and make artifacts. The official overview identifies the Hermetica as the guidebook and says to craft it from a book and two sand. Upstream's packmaker documentation exposes five recipe families—calcination, distillation, accumulation, liquefaction and incubation—and separate Divination Rod recipes/configuration. Those schemas establish useful input/output categories, but do not by themselves establish the exact in-world setup, unlock order, costs or 1.79.0 recipe set.

**Why it matters in TMM:** It may offer a distinct route for material conversion and resource acquisition. Theurgy also declares an Occultism dependency in its upstream build metadata, so the installed Occultism profile should be checked for a meaningful player-facing link. Whether that dependency is required by the exact 1.79.0 artifact or creates any live interoperability remains unverified.

**Quick start:** Craft the Hermetica from a book and two sand, then use its in-game guidance as the authoritative next step. No first machine or process setup is documented here as exact-pin verified.

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Matter replication/transmutation (official project description; exact recipes unverified); alchemical Sulfur; Salt and Mercury; alchemical processing recipe families; Divination Rod block finding | Hermetica access is a cheap first step; other early uses remain unverified | Occultism dependency/link (unverified gameplay); vanilla and modded item replication candidates (recipe coverage unknown); TMM material-gate review (no balance/runtime changes proposed) |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Hermetica onboarding | [official project page](https://modrinth.com/mod/theurgy) | partial | Hermetica; exact page names not established | not run | Inspect the pinned guide in-game or exact data |
| Sulfur / Salt / Mercury and spagyrics | [official project page](https://modrinth.com/mod/theurgy); [upstream docs index](https://theurgy.docs.klikli-dev.com/) | partial | exact recipe pages not established | not run | Map recipes, costs, sources and unlocks at 1.79.0 |
| Five alchemical recipe schemas | [calcination](https://theurgy.docs.klikli-dev.com/docs/recipes/calcination/), [distillation](https://theurgy.docs.klikli-dev.com/docs/recipes/distillation/), [accumulation](https://theurgy.docs.klikli-dev.com/docs/recipes/accumulation/), [liquefaction](https://theurgy.docs.klikli-dev.com/docs/recipes/liquefaction/), [incubation](https://theurgy.docs.klikli-dev.com/docs/recipes/incubation/) | partial; schema only | packmaker documentation, not player-page names | not run | Confirm actual recipes, machines and guide instructions |
| Divination Rods | [upstream rod docs](https://theurgy.docs.klikli-dev.com/docs/items/divination-rods/), [rod recipes](https://theurgy.docs.klikli-dev.com/docs/recipes/divination-rods/) | partial | exact guide page names not established | not run | Verify 1.79.0 recipe set/defaults, tiers and operation |
| Artifacts and remaining systems | official project description only | unresearched | not established | not run | Inventory all content and terminal progression from pin |

## Guidebook onboarding

- **Guide name and access:** The Hermetica is the in-game guide. The official project page says to craft it shapelessly from one book and two sand. Upstream source spot-check at immutable commit [b3d6b2ca](https://github.com/klikli-dev/theurgy/blob/b3d6b2ca6885c5b09e922ea5b4b7c0475972a53d/src/main/java/com/klikli_dev/theurgy/datagen/recipe/ShapelessRecipeProvider.java) shows a book plus two items in the `Tags.Items.SANDS` ingredient tag.
- **Earliest attainable stage:** The guide recipe uses book and sand; exact recipe availability and TMM item substitutions have not been checked in the running pack.
- **First instructional quest:** Not authored. A future walkthrough should first teach the player to craft/open the Hermetica and follow its first entry, after its exact opening instructions have been inspected.
- **Reference map:** Not established. Online packmaker documentation is not a substitute for mapping the guidebook's player-facing entries.
- **Where guide is insufficient:** Unknown until inspected in the pinned client. Do not fill the gap with assumptions from recipes or promotional text.
- **Other in-game help:** JEI recipe categories and exact Theurgy guide entry names remain unverified.

## Systems and mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and evidence |
| --- | --- | --- | --- | --- |
| Hermetica | Shapeless recipe | Book + two items matching sand tag | Opens Theurgy's Modonomicon guide | Exact pack recipe/test not inspected; source recipe provider commit above |
| Spagyrics | Identified by the official project page | Sulfur, Salt and Mercury are named as the alchemical substances | Official description says they are used to manipulate matter; exact operation not established | Do not infer which substances are catalysts, consumed inputs or outputs |
| Calcination schema | Packmaker recipe data | Item or item-tag ingredient; recipe field `calcination_time` in ticks | Result item | Schema only; exact 1.79.0 recipes, block setup and advancement gates unknown |
| Distillation schema | Packmaker recipe data | Item or item-tag ingredient with count; time | Result item | Schema only; exact 1.79.0 recipes, block setup and advancement gates unknown |
| Accumulation schema | Packmaker recipe data | Evaporant fluid amount and optional solid solute | Result fluid amount over time | Schema only; fluid input/output and in-world structure unverified |
| Liquefaction schema | Packmaker recipe data | Item and solvent fluid amount | Alchemical Sulfur item with source-ID data; generic sulfur may represent unsupported item families in the documented schema | Schema only; recipe availability and source-ID behavior not checked against pin |
| Incubation schema | Packmaker recipe data | Sulfur, Salt and Mercury ingredients | Item or item-tag result after time | Schema only; exact quantities, consumption, structure and eligibility unverified |
| Divination Rod | Rod docs describe manual or pre-attuned recipes, followed by scanning | Exact recipes and tier progression not checked | Finds/visualizes blocks; configuration can constrain eligible targets and scan settings | Docs state scans consume durability, while visualization of the last result does not. Pin behavior/defaults unverified. |
| Magical artifacts | Official description | Not established | Project advertises artifact creation | Contents, acquisition and progression unresearched |

The recipe-schema fields above come from the upstream [Theurgy packmaker documentation](https://theurgy.docs.klikli-dev.com/). These pages describe data authoring; they do not prove that each schema has a player-usable machine with the same name, nor that every family is present in the pack's exact version.

## Material / capability input-output contracts

| System | Input kind + exact ID/tag/event | Input qty/formula; consumed/remainder | Output kind, qty and location | Trigger/cadence; range/capacity | Automation/interface and evidence |
| --- | --- | --- | --- | --- | --- |
| Hermetica crafting | item stack; vanilla book + `Tags.Items.SANDS` | one book, two sand-tag items; recipe-consumption details not runtime-tested | one Hermetica item | crafting recipe | source recipe provider at commit b3d6b2c; exact pack JEI not checked |
| Calcination | item/tag | ingredient count and consumption not established from player perspective | item result | recipe time in ticks per schema | recipe JSON/data; player machine and automation not established |
| Distillation | item/tag plus count | schema identifies ingredient count; consumption/remainder unverified | item result | recipe time in ticks per schema | recipe JSON/data; player machine and automation not established |
| Accumulation | fluid amount plus optional solid item | exact amounts/consumption unknown | fluid amount | recipe time in ticks per schema | recipe JSON/data; transfer capability not inspected |
| Liquefaction | item plus solvent fluid amount | exact quantities/consumption unknown | Sulfur item carrying source-ID NBT per schema | recipe-defined | recipe JSON/data; resulting NBT requires pin inspection |
| Incubation | Sulfur, Salt, Mercury ingredients | exact quantities/consumption unknown | item/tag result | recipe-defined time | recipe JSON/data; machine/automation not inspected |
| Divination Rod scan | held item/tool + target configuration | Docs say a scan spends durability; exact durability cost unknown | visual indication of last found block; no material output established | configured range/duration in docs; exact pin defaults unknown | item behavior/config; pack test needed |

### Acquisition, renewability and output economics

- Exact sources, renewability, byproducts, conversion ratios, item families supported and resource sinks are not yet established.
- The recipe documentation distinguishes fluids, items and result items. It does not establish in-world consumption order, failure costs or throughput in 1.79.0.
- Verify whether the pack's Ore/ingot material gates can be bypassed by Theurgy replication before any progression design relies on them.

## Complete player progression / walkthrough blueprint

**Completion test:** Not met. This partial profile is insufficient to author a start-to-end rewarded walkthrough without consulting undocumented steps.

| Path and step | Gate or choice? | Exact prerequisite / materials / world condition | Player actions | Unlock / visible success / next step | In-game guide or JEI reference | Walkthrough coverage / evidence |
| --- | --- | --- | --- | --- | --- | --- |
| 0. Find/open guide | Entry | Book + two sand-tag items per upstream recipe | Craft and open Hermetica | Guide becomes available | Hermetica; inner page names unknown | Partially source-backed |
| 1. First alchemical setup | Unknown | Unknown | Unknown | Unknown | Unknown | Unresearched |
| 2. Sulfur / Salt / Mercury acquisition and use | Unknown | Officially named materials; actual sources and recipes unknown | Unknown | Unknown | Unknown | Unresearched |
| 3. Process families and unlock order | Unknown | Five authoring schemas documented; actual tier gates unknown | Unknown | Unknown | Unknown | Unresearched |
| 4. Divination Rod progression | Unknown | Manual/pre-attuned recipes appear in upstream docs | Unknown | Target location assistance described | Exact pages and pin recipes unknown | Partial schema only |
| 5. Artifacts, optional branches and terminal goals | Unknown | Unknown | Unknown | Unknown | Unknown | Unresearched |

### Dependency graph and alternate routes

The upstream build metadata at the release ref lists Modonomicon and Occultism as dependencies, alongside JEI and other libraries. This is not evidence that Theurgy has a functional Occultism gameplay bridge. Inspect the pinned `mods/theurgy.pw.toml` and `mods/modonomicon.pw.toml` for pack pins; map other dependencies before describing compatibility.

### Structures, rituals and automation

No structure shape, machine placement, redstone behavior, transfer side, throughput, automation contract or ritual sequence is established from the documentation reviewed so far. The recipe schemas alone are not enough to construct a quest walkthrough or automation recommendation.

### Advanced and terminal objectives

- **Normal completion:** Not established.
- **Optional completion:** Artifact creation is advertised, but available artifacts and unlock path are not yet inventoried.
- **Remaining progression blockers:** Inspect the Hermetica pages and JEI in a TMM client; inspect exact pinned 1.79.0 recipes, advancements, tags and configs; reconcile source version metadata; map every required unlock and machine.
- **Quest coverage check:** Incomplete; no quest files changed.

## Internal combinations and overlooked mechanics

| Internal combination | Full chain | Benefit | Limitation / exploit potential | Evidence |
| --- | --- | --- | --- | --- |
| Hermetica + in-game guidance | Craft guide → read instructions → identify recipes | Self-contained progression route may be possible | Actual contents/page map uninspected | Official project page; guide runtime untested |
| Liquefaction + Sulfur source data | Item + solvent → typed Sulfur | May allow downstream alchemical transformation | Source-ID representation does not prove arbitrary item replication or automatic compatibility | Upstream packmaker schema; version gap |
| Divination scan + last-result visualization | Scan → visualize prior result | Docs distinguish durability-consuming search from free visualization | Exact costs and item state in pinned release unverified | Upstream rod documentation |

## Low-hanging setups useful in ordinary survival

Only the guidebook recipe is currently supported as an early action. Three substantive survival setups cannot yet be recommended without inventing steps.

### 1. Get the Hermetica — first step

- **Goal and payoff:** Access Theurgy's in-game guide.
- **Minimum entry and ingredients:** One book and two items accepted by the sand item tag.
- **Build/use steps:** Craft the Hermetica; open it and follow its first entry.
- **Sustainability and limits:** One-time guide access; exact recipe test and guide contents not checked.
- **Evidence / pack test:** Upstream recipe-provider source; no TMM client test.

## API, integration and pack-config surfaces

| Hook or customization surface | Namespace / exact file, API or serializer | Capabilities / constraints | Current TMM override? | Evidence |
| --- | --- | --- | --- | --- |
| Recipe datagen | Theurgy docs schemas for calcination, distillation, accumulation, liquefaction, incubation and Divination Rods | Packmaker-facing recipe authoring surfaces; exact registered serializers and 1.79.0 support not inspected | Unknown; no override established | [upstream docs](https://theurgy.docs.klikli-dev.com/) |
| Divination Rod NBT/configuration | Upstream docs describe target allow/deny tags, tier, range, duration and max damage values | Scan durability and eligibility are configurable in the documented format; pin defaults and active config unknown | None identified | [rod docs](https://theurgy.docs.klikli-dev.com/docs/items/divination-rods/) |
| Hermetica recipe | Upstream source uses a shapeless recipe, book + sand tag | TMM substitutions/recipe overrides not inspected | None identified | [source commit](https://github.com/klikli-dev/theurgy/blob/b3d6b2ca6885c5b09e922ea5b4b7c0475972a53d/src/main/java/com/klikli_dev/theurgy/datagen/recipe/ShapelessRecipeProvider.java) |

## Compatibility leads

| Other mod | Candidate link and why | Native / pack-built / would require bridge | Status / source |
| --- | --- | --- | --- |
| Occultism | Listed in Theurgy's upstream build dependencies; TMM installs both | Dependency only is established by upstream metadata; gameplay link unknown | Candidate; do not claim interaction until profiles or source establish it |
| JEI | Listed in upstream build metadata and present in the pack | Likely recipe viewer support, exact categories not checked | Unverified |
| Modonomicon | Theurgy's guidebook framework; exact library pin is present in pack | Guide UI/content dependency | Documented; client untested |

## Quest and teaching opportunities

- Teach guidebook acquisition first, once the actual pinned Hermetica opening instructions have been checked.
- Teach each process only after identifying its in-world block or machine, exact recipe examples and the guide/JEI location.
- Do not create a quest claiming Theurgy can replicate a specific gated material until the exact 1.79.0 recipe registry is checked.

## Targeted verification and open questions

1. Inspect the Hermetica in a 1.21.1 TMM client and record all page/entry names, page order, hidden entries and references to JEI.
2. Inspect the exact pinned JAR's `META-INF/neoforge.mods.toml`, data recipes, advancements, tags, configs and item/block registrations; source tag metadata currently disagrees with declared mod version.
3. Build a complete first-material-to-terminal progression map with actual guide/JEI citations and recipe costs.
4. For every process, identify the real in-world input mechanism, consumed state, output location, timing, automation sides and interruption/failure cost.
5. Inspect Divination Rod tiers, recipes, defaults, target eligibility and durability use against the pinned artifact.
6. Identify all artifacts and any combat, exploration or boss milestones.
7. Check actual Occultism/Theurgy integration and whether any Theurgy recipe can bypass TMM resource gates.

## Evidence ledger

- **[pin]** TMM `mods/theurgy.pw.toml` on `test/full-integration`: exact Modrinth version `EBO0aQOY`, version name 1.21.1 NeoForge 1.79.0, SHA-512 recorded above.
- **[pin]** TMM `mods/modonomicon.pw.toml`: Modonomicon 1.21.1 NeoForge 1.123.0, CurseForge file 9384356, SHA-1 `3971a5ade2595bfcb8a93f4baa2e6e834691e3cf`.
- **[docs: release page]** [CurseForge file 9530580](https://www.curseforge.com/minecraft/mc-mods/theurgy/files/9530580): 1.21.1 NeoForge 1.79.0, release date 2026-11-09. Release notes list commits `b04c107`, `a1f4a64`, `860946f` and `df94639`.
- **[source: release ref, mismatch]** [upstream `gradle.properties`](https://github.com/klikli-dev/theurgy/blob/release/v1.21.1-1.79.0/gradle.properties) declares MC 1.21.1 but mod 1.47.1 and NeoForge 21.1.13. The [release changelog](https://github.com/klikli-dev/theurgy/blob/release/v1.21.1-1.79.0/CHANGELOG.md) labels the ref 1.79.0. Do not treat the source snapshot as proven binary provenance.
- **[source: b3d6b2c]** [Hermetica recipe provider](https://github.com/klikli-dev/theurgy/blob/b3d6b2ca6885c5b09e922ea5b4b7c0475972a53d/src/main/java/com/klikli_dev/theurgy/datagen/recipe/ShapelessRecipeProvider.java) uses one book and two sand-tag ingredients. Exact relation of this commit to the pinned binary is not proven.
- **[docs: version not pinned]** [Official Modrinth project page](https://modrinth.com/mod/theurgy) describes the alchemy concept, three substances and Hermetica opening recipe.
- **[docs: version not pinned]** [Theurgy documentation](https://theurgy.docs.klikli-dev.com/) has player-facing and packmaker material. The recipe-schema and rod details above are reported only as upstream documentation, not exact-pin mechanics.
- **[pack-test]** No client or world test run; no runtime claims.

**Method note:** Keep release metadata, source declarations, packmaker schema and player-tested behavior as separate evidence classes. For Theurgy, the source version mismatch is a reason to avoid upgrading status based on apparently matching release labels alone.
