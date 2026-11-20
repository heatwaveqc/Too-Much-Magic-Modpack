# Mod capability dossier — `<manifest-id>`

> **Purpose:** The primary evidence-backed source for writing **full instructional quests** for this mod. This is a **detailed catalog of objects, actual behaviors and tiered unlocks**, not a one-word capability scorecard, tier ranking or a 29-row substitute for gameplay research. See [axis field guide](../AXIS-FIELD-GUIDE.md), [energy and progression rules](../ENERGY-AND-PROGRESSION.md) and [quest walkthrough standard](../../../QUEST-WALKTHROUGH-STANDARD.md). Delete placeholder examples after real inspection; retain `unknown` honestly.

**Status:** `unresearched` / `partial` / `source-backed-partial` / `quest-source-ready` / `verified-in-pack` / `stale`  
**Selection vs installation:** `pinned-active` / `approved-unpinned` / `deferred` / `excluded` / `unknown`; exact manifest commit and packwiz/source SHA  
**Manifest name / internal mod ID / version:** `<name>` / `<modid>` / `<version>`  
**Binary & provenance:** `<artifact, hash, loader>`; exact source tag/SHA and version gap  
**Official guide / JEI:** `<item ID or UI/keybind>`, exact chapter names; note any guide omissions  
**Parent/addons:** `<all installed addons, separate profile links, status>`  
**Existing deep research:** [mod comprehension profile](../../mod-comprehension/mods/<manifest-id>.md) (only if it exists)  
**Last evidence review / required update triggers:** <date, updated pins/configs>

## 1. Orientation: what can a player actually do?

Describe in paragraph form the distinct systems the player can operate, with named examples of blocks, items, powers and entities. This is not a ranked top-five list. Then record:
- **First practical player loop:** starting guide → materials → exact first working block/item → action → observable output
- **Mandatory advancement spine:** what gates access to later crafting, energy, world, gear and permanent powers
- **Major optional branches:** what they enable, where they split/rejoin, and their first prerequisites
- **Terminal/endgame systems:** name every significant top-end route and what the player can do at its end
- **Cross-mod touchpoints:** real already-present bridges separately from possible/proposed integrations

## 2. Start-to-first-operation guide

| Step | Exact item/block/action and how to obtain it | Instructions, reference & relevant capability | What proves it worked | Pack verification |
| --- | --- | --- | --- | --- |
| 1. Guide access | <exact recipe/default grant/UI> | <named Tome/JEI page> | <player can open the guide> | unknown |
| 2. Resources | <exact source/drop/location and tool> | <how to discover> | <actual material obtained> | unknown |
| 3. Build | <named blocks and geometry, acquisition> | <specific guide page/JEI category> | <recognized/functional structure> | unknown |
| 4. Operate | <exact trigger, typed input, cost> | <use instructions> | <real production/ability/event> | unknown |

## 3. Concrete object and action inventory — fill, don't summarize vaguely

**The inventory must name the meaningful objects and their IDs.** Small families can be listed completely; for huge recipe/spell catalogs, document the family rules and link a complete evidenced table while writing detailed entries for all major mechanics, first unlocks, tier transitions and terminal branches. Object identity and capability identity are different: one wand can support several spells; one ritual may require multiple blocks.

### Blocks, machines and multiblocks

| Exact name / registry ID | What it does and which capability IDs | Ingredients / native recipe | How assembled / operated / GUI, energy | Earliest tier & full upgrade path | Throughput/capacity/limits | Guide & evidence |
| --- | --- | --- | --- | --- | --- | --- |
| <block> | <effect> | <recipe> | <placement, structure, target> | <tier IDs> | <range/quantity> | <source ref> |

### Items, tools, armor, curios, storage and transport devices

| Exact name / registry ID | Held/worn/portable role | How obtained, consumed, charged, repaired | Earliest obtainable → strongest upgrade / alternative | Effects / distinct capability IDs | Slot/capacity/cost and restrictions | Guide & evidence |
| --- | --- | --- | --- | --- | --- | --- |
| <item> | <role> | <actual recipe or drop> | <tier chain> | <capability ids> | <specific limit> | <ref> |

### Spells, rituals, inscriptions, upgrades, player skills and abilities

| Name / actual ID | Trigger and delivery (independent of gear) | Required blocks/items/resources/entity/world state | Exact effects/outputs + limits | Earliest / intermediate / terminal access | Real usage guide | Capability ID and evidence |
| --- | --- | --- | --- | --- | --- | --- |
| <ability> | <how cast/activated> | <typed inputs> | <result/formula> | <tier chain> | <guide section> | <id/ref> |

### Entities, world content and access routes

| Exact entity/structure/dimension ID | How encountered/spawned/created | Drops, services, rules, escape/return | Native / pack gates, earliest/latest | Capabilities & source |
| --- | --- | --- | --- | --- |
| <entity/world> | <access> | <purpose and limits> | <tier> | <ids and refs> |

## 4. Progression ladder and branch map

This section is **mandatory even when no formal tiers exist**. Do not turn optional research or spells into mandatory prerequisites. Mark `formal_native`, `formal_pack`, `analyst_stage` explicitly. `analyst_stage` is a teaching label, not a claim of game mechanics.

**Main dependency graph:** `<start guide> → <first working setup> → <named Tier 0/Tier I/...> → <terminal capability>`

**Optional/exclusive branches:** `<node> → <branch A / branch B, rejoin/never rejoin>`

| Tier/node ID & exact in-game name | Kind + saved scope (player / item / multiblock / world) | Requirements: block layout, items, recipes, energy, environment, boss | What a player does / visible proof of unlock | Newly available named objects, recipes, resource tiers and capability IDs | Next step, optional/exclusive routes | Guide, source and quest plan |
| --- | --- | --- | --- | --- | --- | --- |
| `<manifest-id>::tier/<id>` | <formal_native, player etc.> | <actual prerequisites> | <operation proves tier active> | <specific unlocked capabilities> | <links> | <guide page; intended quest> |

**Tier transition detail:** For every mandatory named tier, explain the physical before/after build, complete material/research acquisition, any unusual layouts, power/fill thresholds, whether old resources remain useful, failure states, and exact positive and boundary tests. The index row alone does not fulfill this requirement.

## 5. All energy/resource systems — not FE shorthand

Include **every** distinct mod resource: internal magical fluids, per-player mana/Anima-like meters, item charge, network buffers, souls, etc. A mod can have all **three** primary scopes simultaneously. Give **separate rows for each resource and scope**, even if the named pool appears in several places. Record other entity/dimension/world pools as needed. Never conflate a player's network with a nearby altar's liquid just because both are called "blood energy".

### Resource identities and storage by scope

| Resource ID, in-game name, units | Storage scope (block / player / item / world/entity) | Exact block/item/player attachment and ID | Tier/first acquisition | Native capacity/hidden buffer/UI | Persistence/owner | Evidence |
| --- | --- | --- | --- | --- | --- | --- |
| <resource> | **block** | <named reservoir/machine/network> | <tier> | <cap/readout> | <owner> | <ref> |
| <resource> | **player** | <player-state attribute/meter/network> | <tier> | <cap/readout> | <player> | <ref> |
| <resource> | **item** | <named rechargeable item/container> | <tier> | <cap/tooltip> | <owner> | <ref> |

**Unreviewed storage scope is `unknown`; do not leave blank or assert absent without source inventory.**

### Producers → transfer → consumers

| Flow ID & resource | First available generation method and inputs | Production amount/rate | Native transfer route, range and buffer | Named consumer/effect and amount charged | Overflow/loss/refund/failure order | Intermediate/top-tier improvements | Observability and evidence |
| --- | --- | --- | --- | --- | --- | --- | --- |
| `<mod>::energy/<slug>` | <producer ID/action> | <units> | <pipe/direct/player event> | <cost + result> | <rules> | <upgrade tree> | <UI/source> |

**Conversions:** Explicit table of typed input resource to typed output, conversion mechanism and compatibility source. 'No FE' should be asserted only after search. Do not assume foreign pool interoperability.

## 6. Axis coverage navigation (not the capability documentation)

A status row is only an index to detailed entries below. `unknown` = unresearched; `present` = linked source-backed capability; `no_capability_confirmed` = sufficiently exhaustive negative evidence; `not_applicable` = reason documented. Use multiple axes per real capability. No axis is scored or ranked.

| Axis | Status | Exact capability IDs / subsection references / negative evidence |
| --- | --- | --- |
| `gear` | unknown | — |
| `movement` | unknown | — |
| `ore_processing` | unknown | — |
| `storage` | unknown | — |
| `crafting` | unknown | — |
| `logistics` | unknown | — |
| `buffs` | unknown | — |
| `external_compatibility` | unknown | — |
| `mob_farming` | unknown | — |
| `ore_generation` | unknown | — |
| `permanent_power` | unknown | — |
| `general_generation` | unknown | — |
| `direct_damage` | unknown | — |
| `block_breaking` | unknown | — |
| `permanent_pets` | unknown | — |
| `temporary_pets` | unknown | — |
| `defense` | unknown | — |
| `healing_recovery` | unknown | — |
| `debuff_control` | unknown | — |
| `energy` | unknown | — |
| `automation_control` | unknown | — |
| `agriculture` | unknown | — |
| `detection_information` | unknown | — |
| `equipment_augmentation` | unknown | — |
| `world_manipulation` | unknown | — |
| `world_content` | unknown | — |
| `death_respawn` | unknown | — |
| `trade_economy` | unknown | — |
| `progression_tiers` | unknown | — |

## 7. Detailed capabilities (repeat the entire block for *each* substantive effect)

### `<manifest-id>::<capability-slug>` — <descriptive effect name>

**What it actually enables:** <what player/automation can accomplish, rather than 'movement'/'gear' label>  
**Axis tags:** <one or more of 29 exact IDs>  
**Native or pack behavior:** <native; installed addon; pack override; proposed, source-only/unverified>  
**Concrete things involved:** <all relevant block/items with IDs, spell/ritual/entity/attribute ID, GUI and item carrier where applicable>  
**Acquisition:** <how to find/craft/build each required object, exact preconditions and recipe/guide page>  
**Starting/bottom-end capability:** <earliest legal gate and **real operation**, its weakest/basic form and limitations>  
**Intermediate forms:** <all meaningful item/spell/rune/altar/storage upgrades, alternate modes and their costs; link tiers>  
**Top-end capability:** <terminal reachable version(s), what it does, throughput/range/capacity/scaling and hard caps; do not compare with other mods>  
**Operation walkthrough:** <numbered actual player actions, placement, casting/activation, setup, expected result, failure signals>  
**Typed I/O:** <items/fluids/player or item charge/other input → consumed or refunded amount → output, target or world-state change>  
**Resource link:** <resource/flow IDs in §5, or explicitly none>  
**Tier links:** <required tier IDs and which capability changes at each transition>  
**Duration, cooldown, area, safety:** <numbers and config if backed; owner, claims, dimensions, multiplayer and anti-exploit constraints>  
**External compatibility:** <documented bridge ID, both typed ends, version and whether installed; proposed bridge is unverified>  
**Guide/JEI source and technical evidence:** <named chapter, JEI category, precise source paths/hashes or pinned-JAR evidence>  
**Quest goal and teaching steps:** <why useful; exact recipe/find/build/use instructions; success signal; optional vs mandatory>  
**FTB detection honesty:** <actual advancement/event if verified, real operation handler, item proxy, manual or unknown; what the task DOES/DOES NOT prove>  
**Proposed safe quest reward:** <positive XP plus useful fixed thematic item, avoiding gates/exploit; proposal only>  
**Acceptance:** <positive in-world test; negative/boundary case for impactful mechanic; actual tested version or NOT RUN>  
**Open questions:** <concrete missing registry IDs/rates/behavior and next evidence source>

## 8. Quest authoring matrix — main spine plus choices

Create one row per teachable mandatory step and major optional branch. Every required progression transition and major capability must be reachable from a row; incidental recipes may be linked to JEI rather than each getting a quest. This is the **primary quest-input handoff**, not the already-written quest chapter.

| Quest source ID | Required/optional | Prerequisite tier and capability IDs | Player-facing purpose and exact actions (items, blocks, activation) | Guidebook page / JEI category | Actual observable success | Honest FTB criterion (real operation / upgrade / possession proxy / manual / unknown) | XP + safe thematic fixed item suggestion | Existing quest ID / source gap |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| `<mod>::quest/<slug>` | <mandatory/optional> | <ids> | <numbered operations or link §7> | <page/category> | <observable effect> | <proof level and limit> | <proposal> | <existing/candidate> |

## 9. Evidence, completeness and next research

- **Source ledger:** list exact pin/hash, immutable source SHA/path, generated guide and JEI data, relevant config patches, any installed-addon verification and genuine pack-test world/build.
- **Capability coverage gaps:** named missing objects, tier transitions, spell families, resource scopes or interactions; explain how to verify each.
- **Quest completeness check:** if initial guide, first functional loop, required tier transition, significant optional branch, energy storage path, final capability or honest quest task is missing, status remains `partial`.
- **Live checks:** distinguish static evidence, compilation, pack assembly, client launch and gameplay acceptance. Do not mark anything verified from server boot alone.
- **Ownership:** research docs only; no unauthorized quest, packwiz, manifest, compatibility code or balance changes.
