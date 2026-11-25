# Energy resources and progression tiers — authoritative Atlas data contracts

**Status:** v0.2 design of how to research and document a mod, not gameplay implementation. Complements [the complete axis field guide](AXIS-FIELD-GUIDE.md), [per-mod template](templates/MOD-CAPABILITIES.md) and existing [quest walkthrough standard](../QUEST-WALKTHROUGH-STANDARD.md).

## 1. Energy means every spendable resource system, not "FE"

The `energy` axis is a **typed resource-flow ledger**. FE/OP are possible types, not the definition. Survey all quantifiable or consumable operating resources: EV, Anima, mana, Ars Source and caster mana, Botania mana, Astral starlight and lumen, Media, soul charges, fuel-like magical fluids, item charges, blood paid into magic systems and any distinct resource described by the mod. A mod may have multiple channels with similar lore names but **nonconvertible mechanics**. Do not claim life/health or a recipe ingredient is a networked energy pool merely because it's consumed; record it as an input where appropriate.

### Every resource needs these fields

| Property | Required documentation |
| --- | --- |
| Identity | Native name, registry IDs/tags, native units, source version; distinct pools never collapsed into a generic numeric "energy" |
| Kind | Fluid, numeric network points, NBT/component charge, capability, machine buffer, player attachment, world-state/ambient field, expendable item, etc.; type separately from location |
| **Where stored** | **Blocks/block entities** (altars, tanks, batteries, networks), **on players** (mana bar, attachment, soul state, per-player network), **in items** (orbs, staffs, tablets, charges, portable cells). Mark **each scope separately** `present`, `unknown`, `no_capability_confirmed` after source review; include any dimension/chunk-wide or entity pool as extra |
| Producer | Named block/item/action/entity/ritual; how activated; per-event or per-tick yield, input cost, world/location/environment gates |
| Consumption | Named ability/station/recipe and its exact resource channel, per-use/idle/drain cost, eligibility and failed-attempt debit order |
| Capacity | Per storage type and tier, visible vs hidden buffers, item and player caps, upgrade modifiers and amount over cap (discard/overflow) |
| Transport | Native pipes, linking, range, draw/push direction, rate, filters, loaded chunks, inter-dimensional routing, access controls |
| Conversion | A → B through named conversion block/recipe/bridge, interface at both ends, input:output ratio, loss and whether it actually operates in TMM; absent proof means `unknown` |
| Persistence | Survival across item break, player logout/death, world reload, dimension transfer, chunk unload; network ownership and sharing |
| Visibility | Bar/tooltip/gauge/probe/book/GUI and precisely what a player can observe; how a quest or test can verify producer/consumer operation |
| Progression | Earliest producer and first real consumer; every new generator, storage and transport tier; upper-end sustainable capacity/output; gate for each |
| Evidence | Pin, inspected JAR/source/guide or in-pack positive and boundary test; version gap, configuration and unknowns |

### Neo Vitae: required multi-scope example (source-backed partial, not a completed audit)

The existing [Neo Vitae comprehension profile](../mod-comprehension/mods/neovitae.md) maps **Essentia Vitae** and **Anima** as distinct operating channels. It reports Ara Vitae/basin/buffers, tanks/pipes/routing networks, **Orb-held Essentia Vitae**, and player-linked Anima networks/attributes. The source-backed description includes both direct orb harvest and player-linked Blood Siphon/Shield interactions. **Do not interpret the player's Anima pool, an Orb's EV fluid and the altar reservoir as identical stores or interchangeable currencies.** For the Atlas, Neo Vitae is a key example of resources represented in block entities, player state and items **across its resource systems**. Confirm exactly which producer/consumer uses which channel before making a quest or transfer claim.

#### Neo Vitae resource worksheet (not final capability findings)

| Resource path | Storage scope | Known components from existing profile | What the Atlas still must determine |
| --- | --- | --- | --- |
| EV → altar use | Block entity/network | Ara Vitae, its internal buffer and basin; tanks, pipes, Routing Nodes | Effective tier capacity, fill/drain, overflow, automation and visible GUI for **each** carrier |
| EV → portable item | Item | Novicius Orb and higher Blood Orbs | Exact registry IDs, per-tier capacity, valid fill/extract events and loss/recovery rules |
| Anima → player-linked magic | Player attachment/network | Player-bound Anima network, Blood Siphon and Blood Shield attribute hooks | Actual player-facing UI, lifecycle, separate rates, and which player-state consumers spend points |
| Cross-channel flows | Unknown until sourced | Some objects participate in both systems | No invented conversion or blanket equation between Anima and EV; document explicit bridges if they exist |

The existing profile lists Ara Vitae tiers **0–5** and important upgrade resources. Those tiers belong both in the energy ladder (capacity/transport unlocks) and the global progression graph (recipes, materials and world gates). This partial example does **not** grant verification of exact runtime UI, end-to-end resource transfer or gameplay operations.

### Astral Sorcery: multi-resource separation

The [Astral Sorcery comprehension profile](../mod-comprehension/mods/astral-sorcery.md) describes **Liquid Starlight**, starlight infrastructure, **lumen-bound equipment**, and player attunement/perks. Do not combine Liquid Starlight, altar starlight, lumen charges and per-player perk state into a single resource merely because they share a theme; establish each resource's true storage and consumption mechanics. The user's intended survey is of **all** resource systems, not just FE compatibility. Confirm whether each resource has block/item/player storage rather than assuming parity.

### Resource flow must be explicit

For every pathway, write a short sentence with typed components, for example: `[named source] --[event, measured amount]--> [specific storage scope] --[pipe/link/inventory]--> [named consumer] --[cost]--> [observable result]`. Both sides need source and the middle needs verified API or direct transfer behavior. A network diagram is optional; a textual transfer record is mandatory.

The **energy axis and external compatibility are distinct**: if a Botania block exports FE (after verifying the exact installed block and its output behavior), its own internal mana economy belongs under energy, its FE conversion belongs under energy **and** the Botania→FE external bridge. A mod with no FE interface may nevertheless have a large, fully documented energy system.

## 2. Progression tiers: a first-class axis and mandatory spine

Progression tier is **not a ranking between mods**. It is the actual ordered unlock topology *inside* each mod and the process by which capabilities become available. Formal named tiers (Neo Vitae Ara Vitae **0–5**) and research/altar stages (Astral Sorcery Tome research, altar upgrades and attunement) must be fully recorded. A mod with no formal tiers may have *documented access stages*; label these `analyst_stage` rather than inventing native tier numbers.

**Every tier node must record:**

1. Exact ID and in-game name; `formal_native`, `formal_pack`, or `analyst_stage`; source and version.
2. Parent/requisite tiers, optional branches, alternative routes, exclusive choices, and whether this is an item/block upgrade, personal progression, knowledge unlock or world access gate.
3. First acquisition materials/recipes/structure/ritual/guide page/JEI category; actual costs including energy/resources, consumptions and time.
4. **Successful operation proof**: valid structure/UI tier, completed recipe, granted research/perk, opened portal, or other state more reliable than mere possession.
5. What it **unlocks**: names/IDs of newly legal blocks, items, recipes, spells, resources, transformations, world content, automation and existing capability records.
6. What it **limits**: hard cap on resources, throughput, spells, recipes, size, storage, range, player choices and whether lower tiers remain useful.
7. Next tier and the necessary path to get its prerequisites, especially cross-mod and dimension gates.
8. Native behavior versus TMM override/desired target; whether gate enforcement is real code versus only suggested by quests.
9. Scope/persistence: per player, per item, per multiblock, per world/team; upgrade/revert/respec and server restart semantics.
10. Quest treatment: essential mandatory node versus an optional branch, exact action to teach, reference, detectable event versus item proxy, positive XP/thematic fixed-item reward proposal and anti-bypass review.

### Required structure for a progression ladder

```text
[Guide/starting resources]
    → [First operational action]
    → [Formal Tier 0 / initial setup]
    → [Formal Tier 1 ... Tier N / research unlocks]
       ├─ [optional branch A]
       └─ [optional branch B]
    → [terminal capabilities and capstone]
```

This is an illustration, not a requirement that every mod have a linear ladder. A rank tree, constellation specialization and two different working altars may be separate directed paths joined by prerequisite dependencies. Nodes should point to **capability IDs**, so quest authors can find exact machine/item/spell records rather than a vague unlock named "better crafting".

### Neo Vitae mandatory audit path

Treat each Ara Vitae tier 0, 1, 2, 3, 4 and 5 as **separate transition records**. The existing profile documents 8, 24, 52, 100 and 172 rune counts for tiers 1–5, respective structures, orb progression, Tau Fruit/Bloodstone and Demon Realm/Hellforged later gates. Investigate exact source-backed recipes/material costs, what each tier truly unlocks and whether player research or item upgrades are independent of altar tier. Capture separate Spiritus, rituals, gear, daemon realm and EV-network branch points. A quest may cover representative optional recipes, but no indispensable transition can be skipped.

### Astral Sorcery mandatory audit path

Keep Tome discovery/research, star-focal-point creation, early crafting altars, subsequent altar/structure upgrades, Liquid Starlight infrastructure, constellation discovery/attunement and per-player perk branches as an interlocking graph. Find the **actual** names, prerequisite research and structures in the pinned source; do not assume a single 'tier' controls all of them. For every upgrade say what newly craftable blocks/items/capabilities become possible, and how to prove the structure/research really advanced.

## 3. Boundaries: bottom end, middle, top end

For **each populated axis**, state:
- **Bottom end:** first attainable real capability for a normal new player, with named item/block/spell, gating, recipe or acquisition and a demonstrable operation.
- **Intermediate:** all meaningful functional upgrades, new machines/spells/modes, branching alternatives, expanded capacity and material/world access.
- **Top end:** fullest reachable/native version of each significant capability (highest altar tier, final item, strongest sustained resource production, maximum storage type, full spell repertoire, endgame ritual), with mechanics/limits. This is **not** a 'power rank' and may consist of several incomparable endpoints.
- **Inaccessible or deferred:** content shipped by a mod but disabled by TMM, absent addon requirements or gated/unavailable due to pack settings; explicitly mark the difference between native and current pack.

## 4. Quest-generation source contract

The Atlas is intended as the **primary detailed research input** for FTB Quests, together with JEI and in-game guides. The other Mod Comprehension project remains an authoritative source of evidence and deeper source inspections, but its broad profile must not be a reason to skip detailed Atlas entry fields.

For **each mandatory tier transition and significant capability**, include: learner goal; exact prerequisites and acquisition; numbered player actions and block placement/activation; reference page/JEI category; expected visible result; actual available FTB task/predicate with proof-level (`operation`, `upgrade`, `inventory_proxy`, `manual_fallback`, `unknown`); XP and safe thematic fixed reward suggestion; branch prerequisite IDs; current live quest crosswalk when one exists. Do not equate item tasks with genuine operation, invent commands/detectors or force every optional recipe into a mandatory chain. Mark unsupported task triggers as integration research needed, not an instruction to write new runtime code. All quest implementation remains a separate authorized workstream under the pack's existing design constraints.

**Completion gate:** A profile may not be labeled `quest-source-ready` if the first guide path, a necessary altar/machine, a formal tier transition, a meaningful subtype, the resource production/storage/consumption path or a terminal system is undocumented. A well-supported partial profile is useful, but must be clearly marked partial.
