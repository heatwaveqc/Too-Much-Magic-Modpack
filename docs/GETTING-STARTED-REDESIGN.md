# Getting Started redesign — vanilla campaign with magical alternatives

**Status:** Design specification / authoring contract only, 2026-11-15. **Do not regenerate FTB quest data yet.** The owner wants to wait for complete, version-scoped magic-mod comprehension before authoring the full new chapter. This document records the intended structure and the research/implementation gates, not a claim that gameplay has been changed or client-tested.

**Existing references:** [Quest architecture](QUEST-ARCHITECTURE.md), [quest design rules](QUEST-DESIGN-RULES.md), [quest walkthrough standard](QUEST-WALKTHROUGH-STANDARD.md), [chapter inventory](QUEST-CHAPTER-INVENTORY.md), [existing quest UI/content state](QUEST-CLIENT-QA-FIX.md), [early magic utilities](EARLY-MAGIC-UTILITY-INVENTORY.md), and [mod comprehension index](mod-comprehension/INDEX.md).

## Concept and player promise

Getting Started is the pack's **introductory campaign** from obtaining the first log to four major vanilla encounters: **Wither, Ender Dragon, Elder Guardian, Warden**. It assumes players know the basics of vanilla Minecraft or are following the game's built-in tutorial. It should not spend a long mandatory sequence teaching every ordinary workbench recipe.

**Core loop:** identify the next concrete survival goal → explain why it matters and the minimum route to reach it → offer useful, independently optional magical approaches → let the player choose → return to the next survival goal.

Target **roughly half the chapter's authored quests as optional, named magical-mod discovery/recommendation nodes**. This is a target for substantive nodes, not a quota padded with meaningless checkmarks. Each recommendation should solve a *specific player problem* and link to a *particular mod chapter and starting step*. A problem may have 2–4 options, but each option must be clearly characterized; the player is not forced into any one school.

Do not turn Getting Started into the detailed progression walkthrough of every magic mod. The specialist mod's own chapter owns the how-to from guidebook through capstone (per the walkthrough standard). Getting Started supplies the compelling use case, a verified starting route/requirements, and a navigational link.

## Ownership and relationship to existing chapters

- **Getting Started** owns the initial vanilla-driven spine and the four vanilla encounter introductions/completions. It should remain useful even when a player ignores magic suggestions.
- **Tips & Tricks** owns persistent QoL reference material (JEI, Jade, controls, sorting, claims, bag safety, etc.). Getting Started may link to it, but should not duplicate every tip as a reward-earning objective.
- **Basic/Advanced Progression** own broader shared modded milestones and systems, not duplicate first-log/iron/Netherite/vanilla boss tasks after migration.
- **Magic** chapters own validated full system walkthroughs, recipes, progression, addons, and substantive rewards.
- **Exploration & Combat** chapters own dimension- and mod-specific bosses/encounters. They may cross-link vanilla encounters without giving duplicate rewards for the identical criterion.
- **Endgame Roadmap** owns modded endgame orientation, not another paid Ender Dragon/Wither objective.

The currently authored Introduction has three chapters, 45 overall. A dedicated **Choosing Your Species & Faction** chapter is not yet present. The intended design adds one Introduction chapter (provisional structure: **46 total, Introduction 4**); this requires explicit manifest/chapter ID and UI work in the future implementation pass. Alternatively, keep that material centralized in an equivalently clearly titled chapter only if the owner later changes this structural decision.

## Campaign graph (conceptual; no quest IDs or reward IDs yet)

The **spine** is sparse and readable. Optional magical nodes attach at relevant waypoints, not in one giant block at the beginning.

| Stage | Core checkpoints | Optional magical problems to surface |
| --- | --- | --- |
| 0. Arrival | Welcome, first log / pouch reward, early link to Choosing Your Species & Faction, link to Tips & Tricks | 'What kind of character do you want to be?' is orientation, not a mandatory transformation |
| 1. Wood → iron | Obtain log, crafting basics only as needed, stone and iron tools, iron pickaxe | Better mining, ore-yield increases, portable crafting, early spells, light, basic protection, easier processing |
| 2. Establishment | Safe base, food/storage readiness, access to first practical magic choice | Magical storage, farming help, travel, tool repair, useful utility spells |
| 3. Nether | Reach Nether, explore Fortress for blaze rods, learn ancient debris/Netherite upgrading | Fire protection, travel/escape, alternative mining, smelting and navigation |
| 4. Netherite | Acquire/upgrade to Netherite as a highlighted milestone (not a universal hard gate) | Equipment alternatives, enchantment support, mobility, durability/defense magic |
| 5. Combat preparation | Eyes of Ender/stronghold path, wither skulls/soul materials, ocean monument, Deep Dark/Ancient City | Combat spells, detection, flight, underwater mobility, area protection, recovery |
| 6. Four independent finales | Wither; Ender Dragon; Elder Guardian; Warden | Each branch has one or more relevant optional magic suggestions, plus species/faction caveats |

**Important:** Netherite is strongly recommended in the intended narrative order but not required by the quest graph for the later encounters. The four major encounters fan out as **independent optional challenge branches**; Warden in particular is a survival encounter and not a normal loot-progression boss. If the user intends a strict kill milestone for Warden, keep that a clearly labeled opt-in challenge rather than a gate to later content.

Vanilla progression information should be brief but actionable: include the item/structure/hunt needed, where to locate it, and the decisive next move, without re-explaining how to make planks to experienced players. Multistep/special mechanics can receive more detail.

## Magic recommendation node contract

Each optional recommendation should carry:

1. **Question/problem:** 'Need more usable metal from an ore?', 'Want unattended fuel-free smelting?', 'Want flight before Elytra?', 'Need better storage?', 'Want to breathe underwater?'.
2. **Named system and exact advantage:** one actual mod and what it truly does. **Do not equate Fortune, crushing raw ore to smeltable dust, duplicating ingots, and copying items.**
3. **Earliest realistic entry:** named starting object/guide/ritual, important prerequisite, approximate stage (early / short chain / later), and whether the current pack actually permits it.
4. **How to explore:** link to the exact specialist chapter and its first relevant how-to milestone. Put full instructions in the specialist chapter.
5. **Honest caveat:** power cost, risk, time, boss gate, version uncertainty, character restriction, or not-yet-tested interoperability.
6. **Independent optional completion and reward:** small thematic, fixed item plus positive XP under current quest policy; never duplicate the same objective rewarded by the specialist quest and never grant restricted gear.

Use the mod-comprehension profile and progression/quest handoff as the acceptance source. The earlier [early utility inventory](EARLY-MAGIC-UTILITY-INVENTORY.md) is a lead list, **not** proof of exact working installed behaviors.

**Illustrative candidates, pending installed-version verification:**

| Player problem | Candidate discovery link | Critical distinction / status |
| --- | --- | --- |
| More ingots from raw ore | Occultism Foliot Crusher | Requires ritual/pentacle setup; verify smeltable dust outputs in the installed build |
| Faster resource yields | EvilCraft Vengeance Pickaxe | Fortune-oriented, cursed/risky, not 'ore doubling' or vein mining |
| Fuel-free smelting | Scriptor Magicae Casting Lectern + Inflame | Source-supported mechanism; actual pinned-build chest targeting/output still needs a gameplay test |
| Basic magical mining/movement | Ars Nouveau | Tier 1 Break/Leap possibilities; full flight and higher-tier smelting must not be misrepresented as day-one |
| Flight without Elytra | Hexerei broom or EvilCraft blood broom | Two different systems, separate resources/fuel and controls; compare verified costs |
| Better storage/crafting | Ars Nouveau Storage Lectern or later Occultism storage | Ars storage lectern is **not** the Scriptor smelter |
| Find specialized minerals | Astral Sorcery Resonating Wand | Rock Crystal discovery is not generic metal-ore detection |
| Item pickup / area utility | Appropriate Botania/EvilCraft/other verified systems | Confirm port, power source, range, recipe and whether it is truly accessible at that stage |

Don't promise generic 'ore doubling', free permanent flight, or bag nesting on evidence from other modpacks. Never recommend ProjectE shortcuts before the approved endgame gate or grants that bypass protected boss loot.

## Species and faction awareness

### Early choice chapter

Place an explicit cross-link near the beginning: **'Choose Your Species & Faction'**. It should teach what choices exist, how/when to gain them, whether they can be reversed, eligibility and combination restrictions, and where their full skill/progression chapters live. It must be **available early, not required before the player can punch wood or mine iron**.

**Species/physiology and faction/profession are different axes.** Hunter-like professions, dragon body types and transformations must not be conflated. The active roster and compatibility rules are in flux (including review of whether standalone Vampirism/Werewolves are retained); do not hard-code an outdated list or falsely assert incompatibility.

### Required callout in relevant Getting Started quests

If a species/faction changes a relevant step, include a concise **Species & Faction Notes** section in that quest (in the description or linked context). Research all approved playable paths for:

- Tools, mining and harvest capabilities, equipment/armor limits and carrying restrictions.
- Food, blood/energy sustenance, daylight, water, fire, cold, environmental survival.
- Movement and flight (including any restrictions on Elytra), portals and dimensions.
- Creature attitude, hunter/faction hostilities, boss AI, alternate objectives and rewards.
- Death/revival/anti-death mechanics that affect detecting genuine encounter success.

The standard applies to both positive shortcuts and dangerous side effects. All negative consequences that would reasonably change the player's decision belong **before** the choice or encounter.

**Ender Dragon example (must be validated against the exact pinned Dragon Survival build and TMM settings):** Upstream Dragon Survival 1.21.1 documentation states that killing the Ender Dragon gives dragon players a Mark that disables Spin; the Primordial Anchor in the dragon-specific End setup is affected by the dragon being alive/dead. Therefore the Ender Dragon quest must not advertise killing her as consequence-free for playable dragons. Explain the distinction, cross-link to dragon progression, and design a fair opt-in/alternate dragon quest resolution **only after** validating the intended mechanics and owner policy. Sources: [official release notes](https://www.curseforge.com/minecraft/mc-mods/dragons-survival/files/6268513), [specific earlier 1.21.1 release notes](https://www.curseforge.com/minecraft/mc-mods/dragons-survival/files/6257102).

When the quest system cannot automatically check a physiology/faction predicate, **do not pretend that it can**. Use explicit text, conditional branches only when tested, and approved manual confirmation or separate objective criteria as necessary. Avoid mutually exclusive unfinishable quest paths.

## First-log bag/pouch reconciliation

**Supersede the old first-log automatic inventory insertion.** The latest owner preference is:

- The early **Punch a Tree** FTB quest gives the starter bag as a **claimable quest reward**, not a KubeJS/advancement auto-grant on log pickup.
- Add **one dedicated Bundle Curio slot** appropriate for that bag. Confirm actual equipped item registry and slot semantics with client tests; no fabricated IDs.
- Preserve the intended per-player one-time benefit and check multiplayer/team reward behavior. Avoid duplicated pouch rewards, automatic second-grants, nested bags and chapter reward chains that complete unrelated objectives.
- The existing manifest/quest descriptions and KubeJS scripts still reflect an older automatic pouch grant; they must be reconciled **during the future implementation pass**, not edited by this planning document.

## Detection, rewards and layout

- Use **real task/advancement/boss-kill evidence** where feasible. Item possession isn't proof a boss was fought or a magical process was operated.
- Where a suggested mod is optional, use an honest manual read/discovery confirmation; do not make the player craft a complicated modded machine merely to 'check out' the system.
- Every quest still earns positive XP and generally a meaningful thematic fixed item, with only documented exceptions. Boss rewards cannot bypass later or protected loot gates.
- Do not repeat criteria or use the same underlying boss advancement for rewarded quests in multiple chapters.
- Visually show a left-to-right or top-to-bottom **survival spine**; attach magical opportunity quests as side branches in a parallel lane. Present the four encounters as a terminal fan-out, not a single dependency chain.
- Keep the chapter browseable from the start and let players change specialties; quests communicate routes, they do not invent gameplay locks.
- FTB localization, UI positions and IDs must be generated and validated using the current manifest/builder/validator process; check fresh-client render, text, icons, reward claims and faction-specific messages.

## Hold point and future implementation gates

**Now:** agree on campaign architecture, ownership, early species/faction chapter proposal, item-reward policy, approximate quest ratio, and the 4-boss graph. Keep old quest content unchanged until specifically authorized for implementation.

**Before writing final new quest nodes:**
1. Finish and audit the full magic-mod comprehension profiles and version-specific progression-to-quest matrices, including genuine early utilities, prerequisites and links.
2. Lock the actual species/faction roster and integration behavior; verify the Dragon Survival End interaction in the installed version.
3. Confirm the first-log claimable bag item, the one Bundle Curio, FTB team/per-player reward semantics, and removal of legacy auto-grant.
4. Audit conflict/duplication with Basic/Advanced Progression, the Endgame Roadmap and combat chapters, and plan quest ID/progression migrations.
5. Make a quest coverage matrix mapping each vanilla milestone and each magic discovery recommendation to its prerequisites, exact specialist chapter, real criterion, XP/item reward, character caveat, and evidence status.

**Future implementation / acceptance:** Generate source manifests, localization and FTB files on a feature branch; run validators; do a fresh disposable-world walkthrough for multiple character types; confirm vanilla spine, choice links, no involuntary specialty gates, all four boss encounter branches, reward non-duplication and the intended roughly 50/50 rhythm. A green server boot or syntactically valid quest file is not equivalent to working quest UI/gameplay.

## Open design judgments (not blockers for this spec)

- For dragon players, should a peaceful/alternative Ender Dragon encounter satisfy an equivalent quest, or should the kill remain an expressly optional costly choice? Research first.
- How precisely should Warden 'completion' be defined (survive a summoned Warden, reach/escape the Ancient City, or actually kill a Warden)? Treat kill as optional challenge by default.
- Whether to place the species/faction chapter in Introduction as a fourth chapter (**recommended**) or restructure existing Intro chapters without increasing the chapter count.
- The exact counts, rewards, criteria, and mod pointers are intentionally deferred until the comprehension and client-verification work is complete.
