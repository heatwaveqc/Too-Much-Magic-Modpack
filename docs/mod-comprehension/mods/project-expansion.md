# Project Expansion — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | project-expansion / projectexpansion |
| Category / parent or addons | EMC progression; expansion for ProjectE |
| MC / loader / pack target | Minecraft 1.21.1 / NeoForge / TMM test/full-integration at ProjectE merge c1d048920f30ddc2e19a746ab3147ed4c05a5a09 |
| Pinned version / artifact / hash | projectexpansion-1.21.1-1.0.6.jar; SHA-512 97e832da2ca03309f4ef875b42a9250bba3798453f271f82b441e7c0bf739e103ee9f4274839b5e4d9a154a44167cc0f8169bd97ed37b3d16e7e09d34cfe3c85 |
| Source snapshot | [release commit d103d719bacd30c6cd3d42ce7c625116749e84b3](https://github.com/DonovanDMC/ProjectExpansion/commit/d103d719bacd30c6cd3d42ce7c625116749e84b3); binary-to-source equivalence not checked |
| Guide/documentation version gap | External wiki exists, but checked pages describe earlier releases; exact 1.0.6 in-game guidebook not established |
| Profile status | researching |
| Evidence reached | exact pin metadata and version-aligned source; no local jar or client test |
| Reviewed | 2026-11-16 |
| Quest ownership | Intended separate Expansion lessons; no corresponding authored FTB chapter file found |

## Executive gameplay map

**What the player actually does:** Project Expansion adds sixteen sequential matter tiers and builds out ProjectE's Collector, Relay, EMC Link, Power Flower and matter-item families. The first three tiers reuse ProjectE's Dark Matter and Red Matter; twelve colored tiers follow, then Fading and Final. Players craft collectors and relays, combine them with an EMC Link to build a Power Flower, and use generated EMC to learn items or power ProjectE transmutation machinery. Other branches add portable transmutation, remote Alchemical Chest access, EMC storage Stars, knowledge transfer, fluid/item transfer, and higher-tier condenser support. Special armor and equipment also exist; those branches still need a complete item-by-item use and recipe pass.

**Why it matters in TMM:** This is a large EMC multiplier and duplication surface, not a separate spell school. Power Flowers turn light exposure and higher matter tier into persistent EMC income; learned EMC then makes known items reproducible. TMM's Chaos-Shard recipe milestone is therefore a recipe boundary only until the Matter Upgrader, ProjectE learning, EMC values, trades/loot and machine-use paths are audited.

**Quick start:** ProjectE access is the parent gate. Use JEI to inspect the Basic Collector, Relay, EMC Link, Compressed Collector and Power Flower recipes. A Basic Power Flower uses two Basic Compressed Collectors, one Basic EMC Link and six Basic Relays. Place it in light with an owner; it stores EMC while that owner is offline and credits the player's account while online. Exact live recipes/configs and the proposed pack gate are not runtime-validated.

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Sixteen-tier passive EMC production; higher-tier Collectors/Relays/EMC Links; large portable EMC Stars; remote Alchemical Chest books; higher-capacity transmutation/condenser tools | Basic Power Flower after ProjectE and its recipe materials; Basic Collector/Relay network; Basic Alchemical Book remote access if affordable | ProjectE EMC/knowledge (native); Draconic Evolution Chaos Shard gate (pack recipe proposal); Compact Sun boosts to adjacent Power Flowers (native) |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Matter tiers, blocks and tools | Matter.java, release SHA | Tier names/scaling mapped; exact recipes/items incomplete | JEI | not run | Audit all 16 tier recipe families, pick requirements and stats |
| Collectors, Relays, EMC Links, Power Flowers | BlockEntityPowerFlower.java, BlockEntityCollector.java | Core output contract mapped; item/fluid transfer partial | JEI | not run | Inspect interfaces, config defaults and operation |
| Matter Upgrader | ItemMatterUpgrader.java | Interaction and EMC cost path mapped | JEI | not run | Test recipe/learned-item requirements and gate bypass |
| Stars and EMC storage | ItemStar.java, Star.java | Tiers and capacity formula partial | JEI | not run | Audit recipes, filling and transfer use |
| Alchemical Books and tablet | ItemAlchemicalBook.java, ItemArcaneTransmutationTablet.java | Access mode/tier behavior partial | JEI; external wiki | not run | Map binding, range, dimensions and recipes |
| Knowledge sharing | ItemKnowledgeSharingBook.java | Owner-to-player concept mapped | JEI | not run | Audit transfer scope, exclusions and config |
| Compact Sun, advanced condenser, gear and tools | Version-aligned tree/recipe source partial | partial | JEI / wiki | not run | Item inventory and full subsystem walkthrough remain |
| TMM access gate and overrides | Pack manifests/design documents | Conflict/bypass review only | not applicable | not run | Recipe change is staged elsewhere; effective enforcement not established |

## Guidebook onboarding

- **Guide name and access:** A native in-game guidebook was not established in the source examined. Alchemical Books are functional remote chest-access items, not tutorials. The upstream project links an external wiki; quests should explain the first machine and reference the exact JEI recipes.
- **Earliest attainable stage:** The Basic machine chain depends on ProjectE access and its normal materials. TMM's intended ProjectE access recipe is documented in the [ProjectE profile](projecte.md), but its gate is not merged or client-verified.
- **First instructional quest:** Teach a Collector or Power Flower as an EMC production machine: recipe, placement/light, ownership, offline storage, and EMC display.
- **Reference map:** JEI recipes are the reliable in-game help found so far. Upstream wiki pages cover Power Flowers and Alchemical Books, but their testing notes refer to earlier versions; use as orientation only.
- **Where help is insufficient:** Full recipes, machine GUI controls, upgrades, exact EMC rates, owner behavior, and the gap between recipe gates and item learning need quest instructions or tested tooltips.
- **Other in-game help:** The mod adds an EMC display and learned-item tooltips through config; effective TMM config values were not inspected.

## Systems and mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and source |
| --- | --- | --- | --- | --- |
| Matter progression | Craft ProjectE/Expansion machines and tier materials | Item recipes, ProjectE EMC, tiered matter | Basic, Dark, Red, Magenta, Pink, Purple, Violet, Blue, Cyan, Green, Lime, Yellow, Orange, White, Fading, Final | Matter.java scales collector/relay base values by factor 6 per tier ordinal. Pack recipes and economics need complete mapping. |
| Collector | Place tiered Collector; use inventory and upgrade slots | EMC-valued fuel and optional chargeable items | Generates EMC; supports ProjectE charge/fuel operations; exposes item automation | Output depends on tier/config. Compaction, side automation and exact transfer math partial. |
| Relay | Place/configure tiered Relay beside valid EMC consumers | EMC and adjacent/linked ProjectE storage | Transfers or boosts EMC within ProjectE network | Tier caps and network rules need source mapping and runtime check. |
| EMC Link | Place/configure tiered link | EMC, items and fluids | Import/export paths to connected inventories/fluids and EMC storage | Filters, side behavior, ownership and rates not fully audited. |
| Power Flower | Craft from tier-matched EMC Link, two compressed Collectors, six Relays; or upgrade sequentially | Tier machines, light, owner | Generates EMC; online owner receives it, offline EMC accumulates in block storage | At default 20 tick cadence, source formula is (collectorOutputBase × 18 + relayBonusBase × 30) × powerflowerMultiplier, rounded to integer. Adjacent Compact Sun below applies another configured factor. |
| Flower upgrades | Previous-tier Flower plus same-tier EMC Link, two compressed Collectors and five Relays | Shaped recipe family | Advances the Flower one tier | A direct recipe exists for each tier as well as sequential upgrades. TMM's staged Shard recipe must cover both Red entry routes. |
| Matter Upgrader | Craft reusable item; right-click compatible placed machine | Must know target tier BlockItem and pay EMC value(target)-value(current) | Replaces placed block with next matter tier | Does not invoke crafting recipe. Flower and EMC Link enforce owner match and preserve state. This can bypass a recipe gate if target item can be learned and paid for. |
| Stars | Craft a size and charge via ProjectE EMC-holder capability | EMC insertion | Portable EMC storage in Magnum, Colossal or Gargantuan families | Six sizes: Ein, Zwei, Drei, Vier, Sphere, Omega. First capacity is 204,800,000 EMC; each next registered capacity multiplies by 4 (18 type/size values). Recipes and endpoints incomplete. |
| Alchemical Books | Use tiered portable access item; higher tiers can bind to a player | Recipe materials and owner data for bound mode | Remote access to Alchemical Chests; Advanced/Master/Arcane support binding, later tiers cross dimensions | Four tiers: Basic, Advanced, Master, Arcane. Range, binding action, chest interaction and limits need exact-version review. |
| Arcane Transmutation Tablet | Use portable item to open transmutation menu | ProjectE Transmutation Tablet, Magenta Matter, workstation/chest tags and Magnum Ein Star per source recipe | Portable ProjectE transmutation interface | 1.0.6 release notes report a prior server-side issue fixed; operation untested here. |
| Knowledge Sharing Book | Crouch-use to store owner; ordinary use by another player | Crafted item; owner supplies knowledge | Shares ProjectE knowledge between players | Owner cannot use it on themselves. Transfer scope, online requirements, consumption and config need full audit. |
| Compact Sun | Place beneath a Power Flower | Compact Sun | Multiplies Flower output | Exact factor and stacking/adjacency limits unresolved. |
| Advanced condenser/utilities | Recipe-defined EMC machines/items | EMC, matter blocks and ProjectE components | Further EMC production and item handling | Full recipe and behavior inventory incomplete. |

## Material / capability input-output contracts

| System | Input kind + exact ID/tag/event | Input qty/formula; consumed/remainder | Output kind/location | Trigger/cadence/capacity | Automation/interface and evidence |
| --- | --- | --- | --- | --- | --- |
| Power Flower | World light and placed projectexpansion:<tier>_power_flower; optional Compact Sun below | No fuel stack consumed | EMC credited to owner capability online; otherwise stored in Flower block | Default tickDelay 20 ticks; formula above | ProjectE EMC capability; delivery batched every 20 server ticks by PowerFlowerCollector; live light/rate check open |
| Collector | ProjectE fuel/chargeable item inserted in Collector | Parent ProjectE behavior; exact count/remainder not established | EMC or charged item | Configurable operation delay; source default 20 | NeoForge item handlers; side contract incomplete |
| Matter Upgrader | Held item plus clicked IHasMatter block; ProjectE knowledge of target block item | No recipe ingredient consumed at use; EMC subtracted by value delta; creative cost zero | Replaced placed block; Flower/link preserve owner, EMC and link state | One block per use | Item-use event; recipe-free state transition after knowledge and EMC checks |
| Star | EMC inserted via item capability | Up to capacity | Stored EMC component | Per-star capacity | ProjectE IItemEmcHolder |
| Book/tablet | Player-item interaction | No mana/fluid input found | Menu access or knowledge operation | Book tier/range and dimension behavior | Server menu/packets; runtime untested |
| Fluid/item bridge | EMC Link capabilities | Exact stack details incomplete | Filtered item/fluid import/export and EMC behavior | Tier rates unknown | NeoForge item/fluid handlers and ProjectE EMC storage |
| Ritual, mana, blood, XP, mobs, damage | No native system established in inspected sources | — | — | — | Treat as unsupported unless source or runtime evidence is added |

## Acquisition, renewability and output economics

- Power Flowers are renewable EMC sources; output scales geometrically with matter tier (×6 base factor per tier ordinal) before config multipliers.
- The owner receives production while online; otherwise EMC accumulates in the Flower. Ownership and loaded ticking matter.
- ProjectE learning allows known items to be recreated if the player has enough EMC. A boss-associated recipe ingredient is not proof of a personal boss kill after it becomes learnable.
- The reusable Matter Upgrader's source recipe uses basic Expansion machines and ProjectE Dark Matter. Its use pays EMC value difference rather than recipe ingredients.
- Final throughput, Compact Sun multiplication, tier EMC costs, item/fluid transfer and profitable feedback loops remain unmeasured.

## Complete player progression / walkthrough blueprint

| Path and step | Gate or choice? | Prerequisite / condition | Player actions | Unlock / next step | In-game guide or JEI | Proposed how-to and detector | Evidence / coverage |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 0. Reach ProjectE | Required pack prerequisite | Owner-approved policy proposes Stone recipe with one of three boss-associated item alternatives; runtime not verified | Acquire permitted center item, craft Stone in either orientation | ProjectE EMC/knowledge access | JEI; [ProjectE profile](projecte.md) | Teach recipe and first learn/buy cycle; item does not prove boss kill | Pack design only |
| 1. Enter Expansion machine line | Required for passive EMC | ProjectE progression/materials; exact recipes via JEI | Craft Basic Collector, Relay, EMC Link and compressed Collectors | Build Basic Flower | JEI | Teach recipes and function before reward | Source partial; no client test |
| 2. Build Basic Power Flower | One EMC production setup | Two Basic Compressed Collectors, Basic EMC Link, six Basic Relays | Craft, place in light, bind owner, observe EMC/storage | Renewable EMC income | JEI | Prefer EMC delta detector; block ownership is weak evidence | Recipe/source |
| 3. Build collector/relay network | Optional alternative | Collector, Relay, fuel and valid recipient | Insert fuel, configure, observe transfer/charge | Local generation and routing | JEI/GUIs | Teach actual accepted inputs and slot sides | Partial |
| 4. Advance matter tiers | Optional progression; Red MK3 has TMM Shard milestone | Current tier materials; staged design adds Chaos Shard to direct Red+ crafts and Red upgrade | Craft direct tier or upgrade sequentially; test Matter Upgrader and learned EMC routes | Higher machine values | JEI | Negative tests for direct crafts, upgrades, Upgrader, learning, loot/trades | Recipe policy staged; gate unverified |
| 5. Portable storage/access | Optional utility branch | Star and Book ingredients; recipe stage varies | Craft/charge Star; use Book on valid chest; bind higher tier if desired | Portable EMC and remote chest access | JEI; external wiki is older | Teach each tier's ownership/dimension rules; possession is not proof of access | Partial |
| 6. Advanced transmutation/item manipulation | Optional capstone | Arcane Tablet, Condenser MK3 and recipe-specific ingredients | Use tablet; configure target/output; prove operation | Higher-throughput EMC conversion | JEI/GUIs | Needs source-complete config, costs and server checks | Partial |
| 7. Special gear and high-tier utilities | Optional branches | Recipe-defined late materials; recipe/effect tree incomplete | Craft and use once effects/costs are mapped | Equipment/automation | JEI/tooltips | Add how-to only after exact inventory and effect review | Incomplete |

**Dependency graph:** ProjectE access → Basic EMC machines → renewable EMC/tier upgrades → higher throughput and portable/transmutation utilities. Sixteen levels form a tier sequence, but individual additions form optional branches. Native crafting recipes enforce material requirements; Matter Upgrader and ProjectE knowledge are distinct acquisition paths.

## Advanced and terminal objectives

- **Normal completion:** Build and operate at least one EMC production network and use one practical Expansion utility.
- **Optional completion:** Reach top Flower/Collector/Relay tier; complete Star, Alchemical Book and advanced transmutation branches; map special equipment.
- **Remaining blockers:** Exact JAR inspection, full 178-item recipe/effect inventory, all tier recipes, machine automation/rates, Compact Sun math, book range/binding, Knowledge Sharing transfer, EMC Link filters/fluids, end-tier crafting, and live gate-bypass testing.
- **Quest coverage:** No authored Expansion chapter file found. ProjectE currently has only an optional manual checkmark. No quests were edited.

## Internal combinations and overlooked mechanics

| Internal combination | Full chain | Benefit | Limitation / exploit potential | Evidence |
| --- | --- | --- | --- | --- |
| Flower + Compact Sun | Flower → Compact Sun below → configured multiplier | Multiplies EMC output without fuel | Factor/stacking unverified; high tiers already scale exponentially | BlockEntityPowerFlower.java |
| Matter Upgrader + knowledge | Learn target Flower → click placed Flower → pay EMC delta | Skips crafting recipe and carries stored EMC forward | Recipe gate may leak; Flower/Link require same owner | ItemMatterUpgrader.java |
| Flower + online owner | Owned Flower generates and batches EMC; offline owner leaves EMC stored | Personal EMC income and catch-up | Ownership and loaded behavior should be tested | PowerFlowerCollector.java, BlockEntityPowerFlower.java |
| EMC knowledge + boss-associated item | Learn eligible drop, reproduce with EMC | Turns one eligible ingredient into repeatable item | Ownership does not prove kill provenance; consistent with recipe-only policy if explicit | ProjectE profile and gate proposal |

## Low-hanging setups useful in ordinary survival

### 1. Basic Power Flower — early after ProjectE
- **Goal:** Generate EMC without continuously feeding fuel.
- **Minimum entry:** Two Basic Compressed Collectors, one Basic EMC Link, six Basic Relays; light and owner.
- **Steps:** Craft using JEI; place in light; establish ownership as item/UI requires; compare EMC or stored value after it ticks.
- **Sustainability/limits:** Passive while loaded; online EMC is batched to owner, offline EMC is stored in Flower. Rate not measured.
- **Evidence:** Version-aligned recipe and code; not tested in TMM.

### 2. Collector charging/processing — early/mid
- **Goal:** Convert accepted EMC fuel into EMC or charge supported items.
- **Minimum entry:** Basic Collector and EMC-value fuel/chargeable item.
- **Steps:** Use JEI and GUI; insert into correct slot; inspect EMC/charge; verify sided automation before piping.
- **Limits:** Depends on input value and config; rates/slot contracts need audit.
- **Evidence:** Collector implementation; no game test.

### 3. Portable EMC reserve in a Star — later
- **Goal:** Carry rechargeable EMC.
- **Minimum entry:** One Star and EMC source; exact tier recipe chain remains to map.
- **Steps:** Craft size, charge via ProjectE EMC-holder behavior, inspect bar and use with supported ProjectE operations.
- **Limits:** Finite storage; capacity progression source-supported, endpoints and recharge recipe chain need verification.
- **Evidence:** ItemStar.java; not tested.

## API, integration and pack-config surfaces

| Hook or surface | Namespace / exact source | Capabilities / constraints | Current TMM override? | Evidence |
| --- | --- | --- | --- | --- |
| ProjectE API | IKnowledgeProvider, IEMCProxy, IItemEmcHolder, EMC storage | EMC balance/knowledge, item EMC and storage; parent dependency | Native integration; wrapper untested | Version-aligned source |
| Machine capabilities | NeoForge IItemHandler/IFluidHandler, ProjectE IEmcStorage and Expansion BigInteger EMC storage | Collector/EMC Link automation; sides/rates incomplete | None found | BlockEntityCollector.java, BlockEntityEMCLink.java |
| Server/client config | Project Expansion Config specs | UI format/display/tooltips, operation delay and production multipliers | Effective pack config not inspected | Config.java |
| Recipes | Generated data/projectexpansion/recipe families | Direct crafts and sequential upgrades are distinct paths | Chaos Shard gate staged on feature branch; loaded recipes unproven | power_flower recipe sources and pack docs |
| Matter Upgrader | Item-use hook on IHasMatter | EMC difference and knowledge check; changes placed block state | No targeted guard found | ItemMatterUpgrader.java |
| Guide/wiki | GitHub wiki and README | External material; version mismatch | No in-game guide found in inspected sources | README and wiki metadata |

## Compatibility leads

| Other mod | Candidate link | Boundary / status |
| --- | --- | --- |
| ProjectE | EMC, knowledge, Collector/Relay/Transmutation | Native; see [ProjectE profile](projecte.md); not runtime-tested |
| Draconic Evolution | Chaos Shard as Red MK3+ threshold | Pack recipe proposal, not native integration; Upgrader/knowledge paths remain open |
| Compact Sun | Flower output multiplier | Native internal interaction; factor/stacking unverified |
| Quest Shop | EMC generation/item duplication could feed sale currency | Candidate economy risk; loop audit not run |

## Quest and teaching opportunities

| Stage | Actionable instruction | Reference | Truthful detector | Reward/risk |
| --- | --- | --- | --- | --- |
| First production | Craft Basic Flower, place in light, verify EMC change | JEI | EMC delta/manual confirmation; ownership alone is weak | Small safe utility; no gated ingredients |
| Machine routing | Configure Collector/Relay and observe accepted fuel/transfer | JEI + GUI | State/input/output check | Useful basic material |
| Tier upgrade and gate | Compare direct recipe, sequential upgrade and Matter Upgrader | JEI | Separate recipe and item-use/knowledge checks | Do not claim gate closed until tested |
| Portable access | Bind/use Alchemical Book and tablet | JEI; older external wiki | Demonstrate access operation, not possession | Avoid early access grants |
| EMC economy | Learn/recreate one item | ProjectE UI | Knowledge/output event; trophy is not kill proof | Avoid EMC/shop reward loop |

## Existing chapter / pilot reconciliation

| Stage | Current status | Detector/reference | Gap |
| --- | --- | --- | --- |
| ProjectE entry | One optional manual checkmark | Manual task | Does not prove unlock, learning or EMC operation |
| Expansion production/progression | No Expansion-specific FTB file found | None | Chapter absent; no quests edited |
| Late-tier Power Flowers | Recipe design/checklist staged | No runtime or FTB detector | Test direct recipes, upgrades, Upgrader, learned EMC, loot/trading and feedback |

## Conflicts, limitations and version drift

- Pack pins Project Expansion 1.0.6 metadata, but the JAR was unavailable; source commit matches release, not proven byte-for-byte.
- TMM's current recipe target is a tradeable boss-associated Philosopher's Stone ingredient and a Chaos Shard at Red MK3 direct/upgrade recipes. This is not a personal-kill or blanket post-Chaos gate. Existing project documents retain older prose and differing gate descriptions; runtime status is unresolved.
- Matter Upgrader skips crafting recipes for compatible placed blocks. Use requires target-item knowledge and EMC; it is a concrete bypass candidate, not proof of a live pack bypass.
- ProjectE can reproduce learned items, so a boss-associated ingredient proves item acquisition, not a personal kill. This follows the owner's recipe-only direction if explained.
- Release notes say 1.0.6 fixes a prior Arcane Tablet server-side issue and Knowledge Sharing client-code-first crash. Notes do not replace client/server testing.
- Do not infer Project Expansion EMC powers Draconic OP/FE or Neo Vitae EV.

## Evidence ledger

| Claim | Exact source | Evidence | Limit |
| --- | --- | --- | --- |
| PE-X-001 Pin/release | mods/project-expansion.pw.toml; [Modrinth file F5C1OobM](https://modrinth.com/mod/project-expansion/version/F5C1OobM) | [pin] | Artifact metadata/hash; binary unavailable |
| PE-X-002 Release source | [Project Expansion commit d103d719bacd30c6cd3d42ce7c625116749e84b3](https://github.com/DonovanDMC/ProjectExpansion/commit/d103d719bacd30c6cd3d42ce7c625116749e84b3) | [source] | Immutable matching snapshot; not binary equivalence |
| PE-X-003 Matter tiers/scaling | Matter.java; generation/constants.ts at PE-X-002 | [source] | Names and ×6 base progression; config values untested |
| PE-X-004 Flower recipe family | generators/recipe/power_flower.json, power_flower_upgrade.json, power_flower.ts | [source] | Direct + previous-tier recipe families; loaded pack override unproven |
| PE-X-005 EMC production | BlockEntityPowerFlower.java; PowerFlowerCollector.java | [source] | Owner delivery, offline storage, batching and formula; no measured rates |
| PE-X-006 Matter Upgrader | ItemMatterUpgrader.java; matter_upgrader_1.json | [source] | Knowledge, EMC delta and recipe-free transition; not a runtime bypass test |
| PE-X-007 Stars/books | ItemStar.java; Star.java; ItemAlchemicalBook.java; ItemArcaneTransmutationTablet.java | [source] | Tier/capability surfaces; full use/recipe map incomplete |
| PE-X-008 Pack gate | docs/PROJECTE-TIERED-GATING-PROPOSAL.md; manifest/integration-sources.json; manifest/progression-gates.json | [pack-doc] | Direction and conflicts/staged state; no enforcement proof |
| PE-X-009 Quest status | ProjectE profile; manifest/quest_chapters.json; current quest files | [pack-doc] | ProjectE placeholder; no Expansion how-to chapter found |
| PE-X-010 Release notes | [GitHub release 1.0.6](https://github.com/DonovanDMC/ProjectExpansion/releases/tag/1.21.1-1.0.6) | [release-note] | Release notes only; not TMM runtime acceptance |

