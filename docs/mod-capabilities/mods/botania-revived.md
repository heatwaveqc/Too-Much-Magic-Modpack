# Botania Revived — Mod Capability Atlas (source-backed first pass)

**Status:** `source-backed-partial`; a substantive quest-source handoff, **not** full 29-axis/per-flower research closure or loaded-pack QA.  
**Pack identity:** `botania-revived` / actual mod ID `botania`; Minecraft 1.21.1 NeoForge; `Botania-Revived-1.21.1-2.0.2-NeoForge.jar`; CurseForge file 9577651, SHA-1 `b333e5ac4fd55b0e8822238ced0b25b193db9628`.  
**Frozen source:** [Velron-Devs/Botania-Revived@5f803227](https://github.com/Velron-Devs/Botania-Revived/tree/5f803227d484598da1f1093a91d5d7cf5cf9600d), tag `curseforge-neoforge-1.21.1-2.0.2`. Pin currently confirmed in [Packwiz metadata](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/mods/botania-revived.pw.toml).  
**Existing detailed source work:** [exact hashed-JAR Mod Comprehension study](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/research/mod-comprehension-local-jars-20261116/docs/mod-comprehension/mods/botania-revived.md), which this Atlas *reuses*, not replaces.  
**Selected addon:** [Extra Botany Revived 1.0.4](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/mods/extra-botany-revived.pw.toml), actual mod ID `extrabotany`; addon-specific capabilities **not** assumed present or closed from base Botania alone.

## 1. Actual player systems and reliable first operation

The player gathers Mystical Flowers and petals; consults the **Lexica Botania** (`botania:lexicon`); operates a water-filled **Petal Apothecary** to create **Pure Daisy**; uses the Daisy to convert neighboring valid logs/stone into **Livingwood/Livingrock**; makes a **Wand of the Forest**, a generating flower (e.g. **Endoflame**), a **Mana Spreader**, and a **Mana Pool**. Binding flower → spreader → pool and observing a real incoming burst establishes a functional mana loop. Crafting the blocks alone does not prove the network works. [Pinned-JAR Mod Comprehension: core progression, guide data and recipes]

**First concrete tutorial:** craft `botania:lexicon` from a book plus an item in `minecraft:saplings`; read *Basics and Mechanics → Lexicon / Flowers / Petal Apothecary / Pure Daisy*; gather petals; fill Apothecary with water; add the exact recipe petals then the finishing seed; place Pure Daisy and valid adjacent blocks; wait for Livingwood/Livingrock transformation. Next choose Endoflame, supply accepted burnable fuel, bind with the Wand and visibly increase the pool's stored mana. Exact ingredient quantities for the flower/network blocks are JEI recipes, not invented here. **No TMM client success has been verified.**

The exact hashed JAR contains **291 Lexica entry files in 13 categories**, including Generating Flora (16), Functional Flora (31), Natural Apparatus (26), Alfhomancy (15), Mystical Items (43), Trinkets and Accessories (31), Challenges (21), modules and other topics. This is guide *coverage*, not 291 unlocks or 291 quests; availability can depend on module and advancement flags.

## 2. Progression graph: separate branches, not a single ranking

| Stage / branch | Precise player action and object | New capability, costs or boundary | Proof appropriate for instruction |
| --- | --- | --- | --- |
| 0 — Nature materials | Gather multiple Mystical Flower colors; craft and open `botania:lexicon`; prepare water-filled Petal Apothecary | Petals, dye-family material, first generating/functional flower recipes | Actual Lexica open/manual; flower/apothecary possession merely proxies acquisition |
| 1 — Living materials | Craft Pure Daisy with the Apothecary and operate valid conversions | Livingwood/Livingrock for mana infrastructure and later craft chains; processing is timed/world-local | Observe transformed output, not only Daisy possession |
| 2 — First live Mana loop | Endoflame or a chosen documented generator → Wand-bound Spreader → Pool | Flower generates **Botania Mana** from its species-specific fuel/event; Spreader carries bursts; Pool stores mana; transmission can lose mana over distance | Inspect increased pool value or mana receipt; possession is insufficient |
| 3 — Mana material processing | Use Mana Infusion and Runic Altar, following JEI inputs and mana network | Mana-dependent materials/runes; Runic Altar requires an actual operation, not only the crafted station | Produced rune or infused item plus a paid Mana transaction |
| 4A — Functional flora, mechanics and utility | Pick a real functional-flower entry and its applicable target or an apparatus/Corporea workflow | Mana-powered farming, routing, block/entity manipulation, logistics, detection and control, each with distinct conditions/ranges | Action-specific observable world/inventory outcome |
| 4B — Elven trade | Construct Alfheim Gateway with its required pylons, supply mana, throw supported exchange item(s) | Elven materials through a **trade operation**, not through a crafting table; **not** a traversable dimension claim | Exchange receipt and resulting item |
| 5 — Terrasteel | Build usable Terrestrial Agglomeration Plate and three Natura Pylons with necessary structure; process exact inputs with sufficient Mana | Terrasteel and high-level equipment; strong mana production/transport needed | Completed high-tier transformation, not Plate possession |
| 6 — Gaia Guardian | Build Beacon-based Gaia arena with four Gaia Pylons; use a Terrasteel Ingot on Beacon for normal fight, or Gaia Ingot for hard fight | Player-owned boss kill, Gaia materials, further gear; party-specific contribution and loot | Real encounter/kill advancement; possession-only drops cannot establish first kill |
| Optional — Port modules | Discover configured **Botania Extras, Exploration, Asgard, Creative Crafting** content if registered; inspect disabled/default flags | Port-specific additions may change recipes, bosses/rewards and late access; existence in source does not imply survival reachability | Confirm startup module config then exact operational branch |
| Optional — Cross-mod Mana / Source / FE | Botania Revived Ars Nouveau integration, and separately Mana Fluxfield `botania:mana_fluxfield` | Discrete conversion contracts; **Mana ≠ FE ≠ Ars Source ≠ player spell mana** | Actual debit/credit at both endpoints; integration-owned acceptance |
| Addon — Extra Botany Revived | Separate `extrabotany` flowers, gear/mounts, utilities and Gaia III/Ego/Herrscher bosses | Do not assign ordering/costs without exact addon source/JAR recipe and entity review | **Currently not quest-source-ready**; addon dossier to follow |

**Gaia arena/source-specific detail:** four Gaia Pylons at offsets (`±4,+1,±4`) relative to the Beacon, horizontal fight radius **12**, floor/clearance checked over roughly two layers below through five above. Right-click the central Beacon with **Terrasteel Ingot** (normal) or **Gaia Ingot** (hard), with a real player, valid arena and non-Peaceful difficulty. Invalid summon should not consume the ingot. Source base max HP **320**; with more than one counted player, health multiplier `1 + playerCount×0.25`; hard mode instead applies 15 armor in the examined path. Player-contributor UUIDs, not merely final kill, receive individualized loot-table invocations. Normal per-contributor Life Essence: 6, or 8 for true killer; hard: 10, or 16 for killer, plus high-value chance pools. This is **exact pinned source logic**, not a claim that the current TMM fight passed. See Mod Comprehension's Gaia transaction table for full detailed loot conditions.

## 3. Energy and resource scope ledger

| Resource | Actual scope and producers | Transfer / consumer and known mechanics | Limits / unresolved |
| --- | --- | --- | --- |
| **Botania Mana — generating-flower buffer** | Individual flower block entity; generated by *species-specific* burning, consumption, environmental, living-entity or other trigger | Emits Mana to nearby bound Spreader; a passive flower is not equivalent to a reusable fuel generator | Trigger/fuel/cooldown/rate/buffer must be enumerated flower-by-flower from 2.0.2 code |
| **Botania Mana — network burst** | Spreader emits discrete burst after wand binding/aim | Moves toward compatible pool/machine; lenses and spreader tiers alter behavior; distance/loss and one-in-flight constrain ordinary spreader | No global magical grid or universal player Mana implied |
| **Botania Mana — block store** | Pool and distinct mana receivers; filled via burst | Mana Infusion, Runic Altar/Terrestrial structures and functional flora use this resource in specific ways | Each pool and machine needs exact capacity/cost/provider record; no fabricated constant |
| **Botania Mana — item store** | Mana Tablet/Mirror and various mana-using tools/accessories | Carried/refilled and spent through native handler; item-local pool is not automatically a personal RPG Mana bar | Item capacity/charging modes/player-credit semantics need exact survey |
| **Ars Nouveau Source** | Separate Ars Source blocks/handlers | Native optional Botania Revived integration defines bridges; original Comprehension has code-backed interaction study | No general 1:1 or blanket spell-payment assumption |
| **FE** | `botania:mana_fluxfield` has **12,800 FE** internal buffer (not an external Mana Pool) | Incoming burst converts **1 Botania Mana → 10 FE**; attempts up to **1,600 FE/tick** into adjacent receivers; only accepted FE leaves buffer | Specific converter, not proof other Botania/Ars/Neo resources are FE; near-full/side interoperability untested |
| **Gaia contribution / rewards** | Per-fight player UUID contributor set; per-player loot calls | Player-owned hits register participation, per-contributor reward and true-killer bonus | Not Mana, currency or vanilla XP; no possession-only first-kill inference |

**Important known TMM balance:** owner's desired high Thermalily output and lava cooldown adjustments are *separate pack design/implementation targets*. Do **not** silently present them as current 2.0.2 native rates or verified effective configuration. Existing special XP/MXP rules remain Progression-owned, and gathering vanilla XP never inherently awards MXP.

## 4. 29-axis navigation — first-pass evidence, not capability closure

Every `present` line points at a *named* family explored above or in the linked hash-verified Comprehension record; an `unresolved` line requires further source work rather than implying absence.

| Axis | Initial source evidence / next missing exact operations |
| --- | --- |
| `gear` | **present:** Manasteel/Terrasteel gear, tools, mana-using baubles/trinkets, Gaia and optional relic rewards. Need item-by-item slots, native effects, costs and exact terminal equipment. |
| `movement` | **present:** equipment/flora movement effects and optional port mobility. Need named item, prerequisites, flight/teleport/mana costs and disabled cases. |
| `ore_processing` | **present:** Mana Infusion material upgrading; Orechid/Orechid Ignem specialized ore acquisition is separate. Need all recipes and paid amounts. |
| `storage` | **present:** Mana Pools, tablets, items and Corporea bounded inventory queries; not generic storage mod parity. Need per-container capacities. |
| `crafting` | **present:** Apothecary, Pure Daisy, Runic Altar, Mana Infusion, Terrasteel Plate, Elven exchange (as distinct trade). Need exhaustive recipe-by-operation mapping. |
| `logistics` | **present:** Wand bindings, Spreaders, Corporea sparks, item/device transport. Need slot filters, native throughput, boundaries. |
| `buffs` | **present:** functional flora, tools and baubles with temporary/passive player effects. Need named effects, duration/charge. |
| `external_compatibility` | **present:** Ars optional mana/Source bridge; Fluxfield to FE; Extra Botany; other integrations need exact pack consumers. |
| `mob_farming` | **partially evidenced:** functional flowers and port modules (including Loonium) may create loot/encounter loops. Exact species/filter/eligibility and costs unresolved. |
| `ore_generation` | **present:** Orechid and Orechid Ignem (JEI categories), conditioned material substitution. Need source input, target and cost table. |
| `permanent_power` | **unresolved:** relic binding/advancements are not automatically persistent player-skill trees. Clarify any true personal permanent state. |
| `general_generation` | **present:** Daisy conversions and mana-powered production; not unrestricted replication. Need item/recipe scope and rates. |
| `direct_damage` | **present:** damaging flora and gear; Gaia fight is a boss, not itself an attack granted to player. Need exact item/ability classes. |
| `block_breaking` | **present:** functional flora/gear can alter or harvest blocks; identify named blocks, range/cost/protection hooks. |
| `permanent_pets` | **unresolved:** do not infer persistent controlled summons from decorative or hostile mobs. |
| `temporary_pets` | **unresolved:** identify any actual short-lived allied summons and owners before claiming. |
| `defense` | **present:** shields/protection via flora/gear and possible trinkets; exact mitigation/stacks needed. |
| `healing_recovery` | **present:** documented functional flora and potion/brew utility families; identify exact healing/cleansing consumer behavior. |
| `debuff_control` | **present:** functional flora manipulation/damage and enemy effects; exact targets/range still to map. |
| `energy` | **present:** typed flower/burst/pool/item Mana, separate Ars Source and explicit Fluxfield FE; detailed ledger above. |
| `automation_control` | **present:** redstone, Corporea requests, functional flora and Natural Apparatus; no CC/AE2 equivalence inferred. |
| `agriculture` | **present:** growth, planting/harvesting and resource flora. Need species-specific throughput and valid blocks. |
| `detection_information` | **present:** Lexica and mana/target status, Corporea lookup; further operational locators unresolved. |
| `equipment_augmentation` | **present:** Mana Enchanting, gear and lenses; distinguish enchanting from crafting new equipment. |
| `world_manipulation` | **present:** Daisy conversions, growth/soil/placement/flora; define source-world protections and radius. |
| `world_content` | **present:** flowers, optional Exploration/Asgard module structures/world content; need exact enabled registry/loot. |
| `death_respawn` | **unresolved:** do not assign a totem/death feature without specific source path. |
| `trade_economy` | **present:** Alfheim item-to-item gateway exchange; not player coins/shop. |
| `progression_tiers` | **present:** early Daisy/Mana → runes/materials → elven trade/Terrasteel → Gaia, with optional lateral flora/modules/addon. No claim of a single enforced tier system. |

## 5. Detailed capability records — selected, source-anchored exemplars

| Capability ID | Player setup/action and input | Functional outcome / boundary; first-to-late significance | Honest quest detector |
| --- | --- | --- | --- |
| `botania::pure_daisy_conversion` | Pure Daisy next to supported logs/stone; allow timed block transform | Livingwood/Livingrock; essential early infrastructure, later recipes reuse it; a Daisy item is insufficient | Custom block-conversion observation, else output-item proxy |
| `botania::generating_flower_endoflame` | Endoflame with a verified burnable; bind to Spreader/Pool | Makes Mana from consumed fuel; compare later flower efficiency only after per-flower source survey | Native flow/receipt or manual checkpoint; flower possession alone weak |
| `botania::mana_spreader_network` | Bind a generator to Spreader, aim at Pool, activate source | Transports burst; distance/lens/target conditions can reduce or prevent delivery | Actual pool delta / manual confirmation, not Wand possession |
| `botania::runic_altar` | Provide a completed mana connection and exact rune recipe ingredients | Actual rune material craft, distinct from station craft | Product item proxy or actual recipe completion |
| `botania::mana_infusion` | Throw/source an eligible material with pool mana and correct catalyst setup if recipe requires | Consumes Mana for converted material | Product or event, not pool possession |
| `botania::alfheim_trade` | Built gateway, sufficient Mana, eligible exchange item | Returns elven materials; no world portal travel | Exchange event if available; item fallback |
| `botania::terrasteel` | Complete Terrestrial Plate + pylons multiblock; supply correct ingredients/mana | Endgame material conversion; plate alone not proof | Actual Terrasteel output/event |
| `botania::gaia_boss` | Full Beacon/Gaia Pylon arena, normal/hard token; real player combat | Boss advancement/reward, contributors per UUID and hard-mode branches | Player kill/advancement event; never possession-only first-kill proof |
| `botania::mana_fluxfield_fe` | Aim Mana burst at `botania:mana_fluxfield`; connect FE receiver | 10 FE/Mana, 12.8k FE storage, ≤1.6k FE/tick attempted output | Direct accepted FE reading/manual; item proxy cannot prove |
| `botania::corporea_query` | Registered Corporea devices/sparks and request target | Routed inventory request within network conditions, not arbitrary cross-mod remote storage | Successful request/item delivery after validating exact setup |
| `botania::ars_bridge` | Have Ars Nouveau plus enabled Botania bridge and appropriate consumer/container | Narrow bidirectional Source↔Mana contracts; no universal equivalence | Source/Mana before-after measurements and single debit; QA owner |

## 6. Source-level flower and addon expansion (2026-11-23)

The preceding 29-axis matrix remains a navigation aid. It is now backed by detailed, immutable-version operating inventories:

- [**14/14 registered base generating-flower algorithms**](../inventories/botania-revived-generating-flora-2.0.2.md): actual fuel/XP/food/entity/cellular triggers; Mana output; timing/cooldown; buffer/capacity; native failure and destruction conditions. Includes the existing **TMM Thermalily owner lock**: source-native 45 Mana/t with randomized cooldown versus selected 75 Mana/t + zero cooldown in [#147](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/147). The code's exact deployment/QA is not established by this document.
- [**10 high-value functional-flower families**](../inventories/botania-revived-functional-flora-2.0.2.md): Orechid, Orechid Ignem, Loonium, Hopperhock, Rannuncarpus, Agricarnation, Exoflame, Fallen Kanade, Marimorphosis, Jaded Amaranthus. The weighted-output ore/loot producers require *effective TMM datapack* review before declaring protected gates safe; this source survey is not a claim of live exploit or bypass.
- [**Extra Botany Revived 1.0.4 first concrete addon-source audit**](../inventories/extra-botany-1.0.4-storage-power-first-source.md): independent `extrabotany` registration, Fluided Mana and bidirectional adapter families, default-enabled FE→Mana Flux Manafield, several named flowers/equipment lines, Gaia III combat disarm option, and **Mini Handbag**. Its 24-slot portable container is **not blocked by a native slot filter** and must be reconciled with existing TMM no-bag-nesting issue [#16](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/16) if obtainable. That is an implementation/release integration risk, not an Atlas authoring stop.

**Remaining genuine Atlas gaps:** all *remaining* functional flowers, exact Petal Apothecary/Runic/other recipes and tiers for named flowers, Mana storage/container and equipment-level stats, port-module availability, addon flower/boss/gear/mount operation details, and the current FTB chapter crosswalk. We should not claim full family closure from the 14 generating + 10 functional source traces.

## 7. Quest-source authoring handoff and explicit gaps

See the companion [research-only lesson DAG](botania-revived-quest-map.md). It is intentionally *not* FTB SNBT/GUIDs, an approved reward shop, or a claim that the old Botania chapter has these detectors. Keep all instructional pages rooted in Lexica/JEI. The main sequence demonstrates separate **possession**, **real operation**, and **boss first-kill** expectations instead of rewarding the same item as proof of every function.

**Highest-value source next work:** (1) parse exact 2.0.2 generating and functional flower classes one by one, recording fuel/event, per-cycle Mana, cooldown, radius, output and automation filters; (2) enumerate 291 Lexica entry names vs registered operations/recipes and current Botania FTB nodes; (3) build the exact mana-capacity/cost table for each pool, tool, tablet, rune and later machine; (4) inspect optional module startup flags and survival-reachable content; (5) separate full **Extra Botany 1.0.4** source/binary dossier, including three boss prerequisite/reward graphs, plants, mana gear, Airships and Mini Handbag policy risk. Native untouched behavior requires source-backed research, **not** a new blanket live QA tour; future TMM code/config/quest changes require targeted acceptance.

**Evidence limits:** direct TMM Packwiz pin and prior immutable-release/source/hash-matched-JAR evidence were rechecked; this first Atlas pass repackages them into typed capability/progression/quest-source records. **No new JAR binary disassembly, current effective runtime module config, game/client tests, new FTB quests, scripts or modpack changes.**