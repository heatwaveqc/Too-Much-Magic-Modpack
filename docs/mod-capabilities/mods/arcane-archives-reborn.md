# Arcane Archives: Reborn 1.0.2 — 29-axis capability Atlas (pinned JAR verified)

> **Status: SOURCE/BINARY-VERIFIED / QUEST-SOURCE-READY for native 1.0.2 content.** User-uploaded exact published NeoForge JAR matches TMM's Packwiz SHA-1; its entire **34-recipe** inventory, **5-category/40-entry Modonomicon Tome**, **24 advancements**, actual native registry and compiled key storage/transfer/ownership code were inspected. **This is third-party functional research, not playtesting, implementation or a full-modpack compatibility PASS.** No generic third-party gameplay test is required under the [Atlas owner standard](../README.md).

| Input | Evidence |
| --- | --- |
| Exact selected artifact | `ArcaneArchives-1.0.2+Neoforge-1.21.1.jar`, **3,192,671 bytes**, SHA-1 **`082f5f560981c04c487b9c1f5e5ec9976f8ec2db`**, SHA-256 `ad3ce7609f9de5d24387245a68a061e55726e88143b5cb9913a9f744f43bfed4` |
| Manifest ID vs native ID | Packwiz/manifest `arcane-archives-reborn`; actual native registry namespace `arcanearchives` |
| Actual compiled release | NeoForge 1.21.1, `arcanearchives` 1.0.2, MIT; NeoForge >=21.1.251, Modonomicon >=1.120.5; ZIP 993 entries, 130 compiled classes, CRC valid |
| Verified evidence ledger | [**Detailed binary recipe, class and Tome census**](../inventories/arcane-archives-reborn-1.0.2-pinned-jar-census.md) — 34 recipes, 40 guide entries, 24 advancements, 18 registered non-block items, 14 usable blocks plus one internal helper |
| Original selected pin | [`mods/arcane-archives-reborn.pw.toml`](../../../mods/arcane-archives-reborn.pw.toml) and earlier metadata provenance [PR #441](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/441) |
| Scope | **Native released mod** understood; TMM FTB, bag safety, blood-fluid interfaces, ProjectE/EMC and claims are separate integration responsibilities |

## 1. Player-facing identity and the first working setup

Arcane Archives: Reborn is a **magic-themed, block-based storage and logistics system** for items and fluids, with renewable Raw Radiant Quartz, advanced containers, stationary crafting, remote depositing, locating and filter tools, owner-defined shared networks and automation integration. It does **not** require introducing another FE/Mana currency or a portable generic backpack. It offers immediate multitype storage via Radiant Chests, bulk/specialized storage via Troves and Tanks, and remote **location and deposit**, not universal remote item withdrawal.

**Five standout capabilities:** (1) a **54-slot Radiant Chest** with 4× normal stack size per slot (typically 256), (2) a single-item **Trove with 32,768 base items**, upgradeable to **327,680**, (3) a **16-bucket Tank** upgradeable to **160 buckets** and remotely filled/drained using linked Amphora, (4) **loaded-network searchable Manifest** across dimensions with optional external chest monitoring, (5) a **fuel-free Brazier** routing item deliveries to loaded same-dimension storage, with priority filters and ownership-based sharing. The Gem Cutter and persistent crafting table make the infrastructure accessible via native recipes.

**Complete native onboarding (source backed, not invented quest locks):**

1. Acquire `arcanearchives:tome_arcana`: crafting table shapeless **Book + Gold Nugget**. The publisher/compiled mod also supports configurable Tome acquisition when breaking a Bookshelf or crafting a Resonator. Open the **Tome of Arcana** (custom Modonomicon book; *not a separately generated generic book*).
2. Craft a **Radiant Resonator** with the actual 3×3 pattern `GIG/WBW/W W` where G=Gold Ingot, I=Iron Bars, B=Water Bucket, W=Logs. Put it down with **clear space above** and keep the owner online/chunk loaded. At default **6,000 ticks / five nominal minutes** it produces a Raw Quartz Cluster; normal break gives the first Raw Radiant Quartz, Silk Touch keeps the cluster. Default **three placed Resonators/player even across unloaded chunks**. No natural ore mining is needed.
3. Use one Raw Quartz with **8 planks** to craft a Radiant Chest, or combine one Raw Quartz + `#c:chests/wooden` shapeless; alternatively **sneak-use Raw Quartz on an existing normal chest** to convert it while retaining contents. **54 slots**, each capped at the item's normal stack size × server Chest multiplier (default **4**, configurable 1–8).
4. Craft **Gem Cutter's Table** (`DGP/WCW/QWQ`, D=Diorite, G=Glass Pane, P=Paper, W=Logs, C=Crafting Table, Q=Raw Quartz). Place materials in **its own 18 slots**. Recipe ingredient order doesn't matter; player inventory **does not count**. Select the lit recipe, take output; shift-click output transfers craft to inventory. Craft **2 Radiant Dust from 1 Raw Quartz**, and **1 Shaped Quartz from 2 Raw Quartz**.
5. Choose branch: a **Scintillating Inlay** creates **Material Interfaces** (Trove construction), **Containment Fields** (Tank construction) and **Matrix Braces** (capacity upgrade); use Gem Cutting for specialized gear, Brazier, Amphora, Manifest and companion devices. The optional **Radiant Crafting Table** has **9 persistent grid slots and three recipe bookmarks**; it uses ordinary crafting recipes rather than Gem Cutting.
6. Build bulk **Troves** and fluid **Tanks**, then optionally install **Brace → Interface/Field → Shaped Quartz Block** as each existing storage tier permits. Gain **3×, 6×, 10×** total capacity, not a research-resource rank.
7. Find/load/deposit/share: craft a Manifest, attach Monitoring Crystals to ordinary external inventories, use the Brazier, configure with Manipulation Scepter, use Revelation Scepter for diagnostics, or share a network with signed letters.

The complete normal and Gem Cutting recipe ingredient census, their **real IDs** and this exact five-chapter native Tome's **40 page IDs** are in [the release inventory](../inventories/arcane-archives-reborn-1.0.2-pinned-jar-census.md). **Do not confuse actual item prerequisites, category arrows and incidental advancements**: the 24 bundled advancements are primarily inventory-possession checks; the five Tome chapters are documentation, not hard progression locks.

## 2. Native infrastructure and capability ceilings

### A. Source generation, material chains and crafting

| Operation | Starting requirements | Numeric/output and advanced extent | Proof |
| --- | --- | --- | --- |
| Grow first quartz | Radiant Resonator, clear above, owner online and chunk loaded | **1 cluster per configured 6,000 ticks**, max default 3 Resonators/player; server `resonatorTicks` min20, max integer; `resonatorLimit` 1–1024 | `ArchiveConfig`, `ArchiveBlockEntity.tick`, Tome `start/resonator` |
| Break/refine quartz | Harvest raw cluster, Gem Cutter 18-slot inventory | **1 Raw Quartz →2 Dust**, **2 Raw Quartz →1 Shaped Quartz**. Dust/Inlay/Interface/Field progressions consume real items, no energy register | Recipe `gemcutting/radiant_dust`, `gemcutting/shaped_quartz` |
| Persistent ordinary crafting | 1 Raw Quartz + Vanilla Crafting Table | **9 saved grid slots** and 3 recipe bookmarks, ordinary recipes and JEI transfer | `ArchiveBlockEntity.savedRecipes`, Tome `start/crafting_table` |
| Gem Cutter craft | Diorite, Glass Pane, Paper, Logs, Vanilla Crafting Table, Raw Quartz | **20 distinct Gem Cutting JSON recipes**, input **counts** and no fixed slot order. Craftability from **table storage only**, not player inventory | `GemRecipes`, `GemMenu`, `data/arcanearchives/recipe/gemcutting/` |
| Decorative lighting | Raw Quartz click + blocks, Gem Cutter lantern | Light-giving Slivers, Quartz Clusters, Quartz Storage Blocks and lantern (4 per special craft) | Native registered blocks and Tome `sharing/lighting` |

### B. Real storage vs fluid vs optional device upgrades

| Container | Physical storage owner | Native base and exact highest upgrade | Transfer/condition |
| --- | --- | --- | --- |
| **Radiant Chest** `arcanearchives:radiant_chest` | **Block**, item slots | **54 slots**, default **item stack max ×4** (64-stack items: **256 each**), configurable Chest multiplier 1–8; no Brace-tier Chest upgrade | Player UI, shift-move, comparator, sided inventory; breaking chest drops contents |
| **Radiant Trove** `arcanearchives:radiant_trove` | **Block, portable item form preserves NBT/components** | One `ItemStack.isSameItemSameComponents` type: **32,768 →98,304 →196,608 →327,680** items (levels 0–3) | Right-click deposit; double-right-click matching player items; left-click front/top extraction; Key keeps empty filter; comparator |
| **Radiant Tank** `arcanearchives:radiant_tank` | **Block fluid handler, portable Tank item preserves stored content** | One fluid: **16,000 →48,000 →96,000 →160,000 mB** (16/48/96/160 buckets) | Buckets/containers, compatible fluid pipes any side, Linked Amphora; comparator |
| **Matrix Brace** | Installed capacity tier 1 | **3×** base | Install/remove through Manipulation Scepter menu or direct right-click; uninstall only if contents fit |
| **Material Interface** (Trove) / **Containment Field** (Tank) | Installed capacity tier 2 and crafting ingredient | **6×** base | Only after Brace; direct ingredient in making Trove/Tank |
| **Shaped Quartz Block** | Installed capacity tier 3 | **10×** base | Requires prior two capacity tiers; removal must be reversed |
| **Radiant Key** | Optional installed upgrade, type-filter state on empty Trove | Keeps item assignment despite zero items | Enables Brazier restocking an assigned but empty Trove |
| **Devouring Charm** | Held item, optional installed overflow filter | **No additional capacity**; destroys specified excess once full; handheld disposal menu has **six** discard slots and a fluid-drain slot | Destruction is intentional and irreversible; never teach as a mandatory step |

**Clarification of previous description:** Published marketing saying “up to 1,024 stacks” is not the compiled capacity arithmetic. In the release, each base Trove holds a fixed **32,768 items** independent of conventional 64-stack display metaphors. This is precisely **512 normal 64-count stacks**, and the 10× endpoint is **327,680**; use the actual item counts in quest instructions.

### C. Storage discovery, routing and machine movement

- **Manifest `arcanearchives:manifest`:** search loaded owned/shared network storage **across dimensions**, display counts and locations, filter by name/item ID/`@mod`/`#tag`/dimension, sort, track, and highlight matching slots with compatible interfaces. It **does not teleport items or withdraw them from a distant chest**. A **two-block Manifest Lectern** offers the same UI from either half. Native Radiant storage is automatically tracked; a **Monitoring Crystal** sneak-right-clicked on a compatible non-Radiant inventory face enrolls its contents (double chests counted once). Unloaded containers are not accessible.
- **Brazier of Hoarding `arcanearchives:brazier_of_hoarding`:** route a dropped, right-clicked or hopper/pipe-supplied stack into **loaded, owned/shared storage in the same dimension**. Default **150-block horizontal radius**, configurable **0–300** via the Manipulation Scepter; height is not part of the radius. This is *not* a cross-dimensional pipe. It needs **no fuel**. Priorities from actual `ArchiveNetworks.routingWeight`: matching stock in a Gem Cutter or Chest configured existing-items-only; matching configured Troves (normal before overflow-voiding where relevant); otherwise compatible Chests. Empty unassigned Troves are skipped, and remainders remain at the input when they cannot fit. Built-in transactional insertion uses `Transaction.openRoot()/commit()`, so it reports actually accepted items rather than blindly claiming all were transferred.
- **Scepter of Revelation:** use on a reachable machine to inspect its actual contents/capacity/growth, slot status or facing in chat. **Scepter of Manipulation** manages upgrades, Braziers and Chest display labels (opposite-hand display item) including existing-items-only destination preference.
- **Scepter of Translocation:** sneak-use an owned compatible machine to select it, normal use on block face to relocate; maximum **64 blocks in the same dimension**, loaded at both ends, clear space for two-block devices, preserving machine contents, ownership and upgrades. Sneak-use in air cancels. Moving a Tank requires relinking Amphora to its changed coordinate.
- **Radiant Amphora:** sneak-use a Tank to link, then use on fluid source to collect directly into it, or toggle collect/place by sneak-left-clicking a block. **No standalone reservoir** and **no infinite water production** is implied. Linked Tank must be loaded and accessible. Dispensers and item-fluid handlers can use the link; default full/empty source placement is constrained by registered fluid behavior.

### D. Shared ownership and automation

- Devices preserve an **owner UUID**; member access depends on saved `ArchiveNetworks` membership, **not transfer of ownership**. Owner-controlled sharing lets other invited members access loaded devices. `ArchiveBlockEntity.canAccess` delegates to the saved network membership check.
- **Letter of Invitation:** crafted via Gem Cutter, signed by creator, a new player holds use for **3.2 s** to accept. Existing network leaders must dissolve their own network before joining.
- **Letter of Resignation:** the member holds their own signed letter for **3.2 s**. A leader leaving disbands their shared group; the owned devices remain owned.
- **Writ of Expulsion:** owner/leader names the writ after an existing member using an anvil, then holds use **3.2 s**; its target identity persists for offline removal.
- **Automation:** NeoForge item handling exposes appropriate Chests/Troves/crafting stations and item-capable Braziers; fluid capabilities handle Tanks from any side and linked Amphoras. Comparators show Chest/Trove/Tank fullness or Resonator progress. Both search and routing operate only on **loaded** sources/destinations. No arbitrary chunk-loading or cross-dimensional transfer is presumed.
- **JEI integration** includes specialized Gem Cutting and Resonator recipe/display categories; client settings handle Manifest tracking shortcut, overlay and JEI search synchronization.

## 3. Exact 29-axis capability navigation

**Present** means a specific native item/action above; **no_capability_confirmed** is a scoped negative conclusion from the 1.0.2 registered item/block lists, recipe data and major compiled handlers, not from thematic guesses. A broad mod-wide mathematical proof of nonexistence is not implied.

| Axis | Research status | Source-based capability or exact boundary |
| --- | --- | --- |
| `gear` | **present** | Handheld Revelation/Manipulation/Translocation Scepters; Manifest, linked Amphora, Key and Devouring Charm. |
| `movement` | **no_capability_confirmed** | Moves *blocks/storage devices* via Translocation, not player locomotion or teleport; no registered movement gear. |
| `ore_processing` | **no_capability_confirmed** | Raw Quartz and shaped/dust are magical crafting, not mined-ore crushing/multiplication. |
| `storage` | **present** | 54-slot multiplier Chest, single-type Trove, single-fluid Tank; portable data-bearing storage and upgrades. |
| `crafting` | **present** | Radiant Crafting Table; 18-slot Gem Cutter with 20 special recipes and 14 standard JSON recipes. |
| `logistics` | **present** | Brazier routing, Manifest tracking/location, Monitoring Crystal, Amphora fluid transfer, sided handlers. |
| `buffs` | **no_capability_confirmed** | Selected 1.0.2 registered item/block/spell inventories contain no player buff subsystem. |
| `external_compatibility` | **present** | NeoForge item/fluid capability bridges and JEI recipe/track integration; named TMM mods require separate intersection study. |
| `mob_farming` | **no_capability_confirmed** | No registered creature/loot-generation/capture mechanism in 1.0.2's native registry. |
| `ore_generation` | **no_capability_confirmed** | Resonator grows quartz above a placed block; not a natural ore or mining machine. |
| `permanent_power` | **no_capability_confirmed** | Archive upgrades are block-owned; no permanent player attributes, skills or mutable character state. |
| `general_generation` | **present** | Renewable Raw Quartz growth on a timed owner-bound Radiant Resonator. |
| `direct_damage` | **no_capability_confirmed** | No registered combat-damage ability; Devouring Charm destroys items, not entities. |
| `block_breaking` | **present (limited)** | Left-click quartz with raw quartz splits slivers; Scepters relocate whole devices and quartz collection uses ordinary breaking—not a terrain-mining spell. |
| `permanent_pets` | **no_capability_confirmed** | No pet/companion registry or summon behavior in surveyed native code and guide. |
| `temporary_pets` | **no_capability_confirmed** | No temporary summons in surveyed native code and guide. |
| `defense` | **no_capability_confirmed** | Device ownership/claim access is not armor/resistance; no defensive player effect. |
| `healing_recovery` | **no_capability_confirmed** | No native healing or death recovery registered. |
| `debuff_control` | **no_capability_confirmed** | No native hostile status manipulation registered. |
| `energy` | **no_capability_confirmed (currency)** | No Mana/FE/EV pool in functional machine data; Resonator uses time/owner online and Gem Cutting uses items. |
| `automation_control` | **present** | Brazier item routing modes, existing-item precedence, optional void filter, comparators and sided item/fluid automation. |
| `agriculture` | **no_capability_confirmed** | Quartz growing is device resource generation, not crop/soil/husbandry automation. |
| `detection_information` | **present** | Manifest search/filter/track, crystal-indexed inventories, Revelation status report, comparators. |
| `equipment_augmentation` | **present (storage items)** | Trove/Tank capacity upgrades and optional installed Keys/Charms; no combat-item augmentation. |
| `world_manipulation` | **present** | Scepter Translocation moves machines with retained data within 64 blocks; in-world chest quartz conversion. |
| `world_content` | **present (minor)** | Player-generated Raw Quartz Cluster and decorative lit slivers/lanterns/quartz blocks; no natural dimension, biome or ore required. |
| `death_respawn` | **no_capability_confirmed** | No grave, corpse, totem, respawn saving or death handling in surveyed registered mechanics. |
| `trade_economy` | **no_capability_confirmed** | No native trading merchant or sales economy; recipes produce ordinary resources/items. |
| `progression_tiers` | **present (equipment tiers)** | Formal **block capacity upgrades** 1×→3×→6×→10×; Tome five *teaching chapters* are not five hard research gates. |

## 4. Native tier graph vs instructional quest-source map

The Tome has **five navigation chapters**: `start` (Grow Quartz), `storage` (Build Storage), `networks` (Find and Route), `tools` (Use Your Tools), and `sharing` (Share and Automate). Each links actual `arcanearchives:<category>/<page>` paths in the [40-entry guide inventory](../inventories/arcane-archives-reborn-1.0.2-pinned-jar-census.md). Some pages draw prerequisite *arrows*; those are guide instructions, **not 40 compulsory native research locks**. The only explicit upgrade tiers in the compiled storage model are **0–3**, controlling capacity.

### Quest-source entries — proposals for downstream FTB authoring, no saved IDs

Every source ID below is stable within this **research document only**. `Native guide entry` is a real Modonomicon ID or pair of IDs. **No quests, XP or rewards have been implemented.** Future FTB triggers must distinguish item possession proxies from actual routed fluid/item operations; this is a *research truthfulness note*, not a request to playtest the released mod.

| Atlas source ID | Main/optional | Native instructional action | Tome entry `arcanearchives:` | Predicate caveat for future quest author |
| --- | --- | --- | --- | --- |
| `AA-01` | Core | Obtain the Tome (bookshelf drop, crafted Book+Gold Nugget, or Resonator gift) and open it | `start/first_steps; start/resonator` | Book possession only proxies guide reading |
| `AA-02` | Core | Craft/place Radiant Resonator with clear block above; generate a cluster | `start/resonator` | Owning the resonator ≠ producing quartz; track acquisition if no event |
| `AA-03` | Core | Harvest Raw Radiant Quartz, distinguish normal break versus Silk Touch | `start/resonator` | Raw Quartz acquisition is a proxy for source |
| `AA-04` | Core | Make Radiant Chest using eight planks+Raw Quartz, or a tagged wooden chest + Raw Quartz | `start/first_chest` | Craft detector vs storage-use distinction |
| `AA-05` | Core | Build Gem Cutter from actual vanilla recipe and fill its own 18 storage slots | `start/gemcutting` | Craft output ≠ station-use proof |
| `AA-06` | Core | Craft 2 Dust from 1 Raw Quartz and 1 Shaped from 2 Raw Quartz | `start/recipe_radiant_dust; start/recipe_shaped_quartz` | Output may be source-independent; identify GemCutting event if needed |
| `AA-07` | Optional | Use 9-slot persistent Radiant Crafting Table and recipe bookmarks | `start/crafting_table` | Item possession ≠ bookmark use |
| `AA-08` | Optional | Make Inlay → Material Interface, then craft four Radiant Troves and set one item filter | `storage/inlay; storage/interface; storage/troves` | Trove item task ≠ configured identity |
| `AA-09` | Optional | Install Matrix Brace then Interface on Trove then Shaped Quartz Block | `storage/brace; storage/upgrades` | Differ capacity state from upgrade possession |
| `AA-10` | Optional | Make Containment Field and four Tanks; insert a fluid into 16-bucket tank | `storage/containment; storage/tanks` | Tank possession ≠ filled tank |
| `AA-11` | Optional | Apply Brace → Field → Shaped Quartz Block on Tank; show 16/48/96/160 buckets | `storage/upgrades` | Capacity can be recognized from native block state if implementation added |
| `AA-12` | Optional | Make/open Manifest, search a name, @mod, #tag; locate stored item | `networks/manifest` | Having Manifest ≠ actual search/track |
| `AA-13` | Optional | Place 2-block Manifest Lectern and open from either half | `networks/lectern` | Placed Lectern is source-only use proxy |
| `AA-14` | Optional | Attach Monitoring Crystal by sneak-right-click to external chest | `networks/monitoring` | Crystal possession ≠ successfully indexed container |
| `AA-15` | Optional | Use Manifest tracking lines, stop tracking, inspect matching slots | `networks/tracking` | Track actual state where supported; no fabricated advancement |
| `AA-16` | Optional | Feed Brazier by dropped item/right-click/hopper and observe stocked destination | `networks/brazier` | Item at destination demonstrates routing better than Brazier ownership |
| `AA-17` | Optional | Choose matching Trove, existing-only Chest, Gem Cutter priorities and horizontal radius | `networks/routing; networks/range` | Config choice is not a new in-game gate |
| `AA-18` | Optional | Use Revelation Scepter to inspect status/capacity/growth of one device | `tools/revelation` | Scepter possession ≠ informational use |
| `AA-19` | Optional | Use Manipulation Scepter to configure upgrades and Chest labels | `tools/manipulation; tools/chest_labels` | Label and upgrade are separate operations |
| `AA-20` | Optional | Link Amphora to loaded Tank and collect/place source fluid | `tools/amphora` | Amphora possession ≠ successful fluid transfer |
| `AA-21` | Optional | Move a loaded owned device ≤64 blocks with Translocation Scepter | `tools/translocation` | Moving block preserves exact item/fluid ownership; test only cross-mod if real conflict |
| `AA-22` | Optional | Learn Devouring Charm's six disposal slots, filtered pickup and installed overflow; caution on irreversible loss | `tools/devouring` | Do not make destruction a mandatory quest task |
| `AA-23` | Optional | Install Radiant Key to preserve an empty Trove's item identity | `tools/radiant_key` | Key installation is separate from item possession |
| `AA-24` | Optional | Invite player using signed Letter of Invitation; discuss owner vs member access | `sharing/sharing; sharing/invitation` | Multi-player social feature optional; no mandatory two-player gameplay task |
| `AA-25` | Optional | Resign or expel with signed held-use letters/writs, explaining 3.2-second action | `sharing/resignation; sharing/expulsion` | Never require disruptive expulsion for main quest completion |
| `AA-26` | Optional | Connect hoppers/fluid pipes, read comparator fullness and tune settings | `sharing/automation; sharing/settings` | Actual transfer and comparator signals are distinct from owning a machine |
| `AA-27` | Optional | Use decorative quartz slivers, lanterns, two quartz storage blocks and troubleshooting reference | `sharing/lighting; sharing/checklist` | Purely cosmetic orientation, not progression lock |

**Branch structure:** AA-01–06 represent first functional production; AA-07 optional persistent crafting; AA-08–11 independent bulk/fluid storage, AA-12–17 finding/routing, AA-18–23 tools, AA-24–27 social/automation/lighting. The owner-approved **Arcane Archives → Occultism → ProjectE** path is a **teaching and feature-comparison order**, *not a newly imposed mechanical gate* and not an instruction to remove native storage features.

## 5. Compatibility boundary: study interfaces, do not test third-party features by default

The exact released mod **does** register type-correct item and fluid capabilities, item-based data-preserving portable storage, an owned network and transactional routing. None of this licenses assuming the mod automatically converts **EvilCraft canonical blood**, reads ProjectE learned EMC, bypasses FTB Chunks claims or obeys TMM's bag-nesting blacklist. These are **named downstream cross-mod edge questions**:

1. **Generic bag safety (#16):** ensure portable Troves/Tanks with stored contents are in the same universal nested-container tagging/blacklist policy as other modded containers; this is TMM's adapter responsibility, not a reason to disable AA.
2. **Oversized stacks and data-component identity:** AA Chest is 54× item-defined max stack × multiplier; Trove stores one exact item+components. Other mods' item handlers/movers/EMC may need to recognize those limits; inspect the *other mod's* adapter before claiming a loss or exploit.
3. **Canonical blood/fluid:** a Tank exposes NeoForge fluid handlers. TMM's EvilCraft/JAWR/Hexerei fluid unification is a separate cross-mod conversion choice and needs actual fluid type/tag checks, not a generic “test every Tank” requirement.
4. **FTB Chunks and remote ownership:** AA has native UUID-network access checks and currently uses loaded-block routing; whether a specific claimed-protection callback is bypassed requires studying the actual *intersection* with TMM-installed claim wrappers, not assuming either native mod is defective.
5. **EMC/ProjectE:** portable contents and custom item data deserve an EMC acquisition/blacklist/economy audit in the separate TMM integration stream. Neither a Gem Cutting recipe nor a portable Tank inherently proves EMC duplication.

## 6. Completion statement and precise residual boundary

**Atlas native released-mod capability research: SOURCE/BINARY-VERIFIED AND QUEST-SOURCE-READY.** The original first pass lacked the 1.0.2 JAR and had to leave tank volume, raw quartz acquisition, network range, capacity growth and recipe IDs unproven. Those exact uncertainties are now resolved through the uploaded release's 34 recipes, 40 Tome entries, compiled bytecode and pinned hash. Additional undocumented legacy localization entries were **explicitly excluded** unless they have actual registrations.

The mod's ordinary functionality is **assumed to work as released**; no general survival test, server smoke, Prism import, or quest-writing mandate follows from this Atlas pass. A future specific disputed behavior or concrete cross-mod interaction may warrant additional source inspection, but not a blanket “native mechanics QA” obligation. **This pass changes research files only.**
