# FTB Quests architecture — Too Much Magic

Status: **approved high-level chapter organization** from owner conversation; chapter catalog, quest content, rewards, skill costs and progression specifics not yet approved. This is a design document, not an implemented quest pack. Existing `Getting Started` quest chapter is a standalone prototype.

## Primary categories (ATM10-inspired)

1. **Introduction** — new-player orientation, first actions, starter grants, basic UI/skill guidance. Include the agreed once-per-player **Punch a Tree** reward (first log -> Ars Elemental Trinkets Pouch, 27 slots).
2. **Tips & Tricks** — permanently accessible, reference-first entries: UI, controls, special mod interactions, compatibility caveats, avoiding prohibited bag nesting, practical magical alternatives to conventional logistics. Keep prerequisites minimal; don't make players complete tips as gates.
3. **Basic Progression** — cross-mod early survival, collection, food, simple magic, base setup, mundane-to-magical resource acquisition and low-tier storage. Explain multiple routes, rather than requiring every magic school.
4. **Advanced Progression** — shared mid/late pathways and dependencies: advanced crafting, power, high-tier magical mining, resource renewal, infrastructure and uncommon materials. Crosslink to relevant specialty chapters, and avoid duplicating every task.
5. **Magic** — one chapter per substantive **primary magic mod/system**, with relevant addons grouped inside the parent (unless an addon proves to have sufficiently independent progression to justify its own chapter). Include both creation-oriented and combat-oriented magic where their core systems fit. Examples: Ars Nouveau plus its addons; Occultism; Iron's Spells; Botania; Astral Sorcery; Neo Vitae; Roots; Goety; Psi; Hex Casting; and other approved primary systems. Catalog each against the *live installed/approved manifest* before authoring; no assumed active mod.
6. **Exploration & Combat** — one chapter per substantive exploration/combat mod, including dungeon/world/dimension content, bosses, combat systems, gear and adventure objectives. Examples for categorization review: Cataclysm, Deeper and Darker, Apotheosis, artifacts systems, themed structure mods and Gateways to Eternity. Do not create chapters for dependencies/libraries/very small addons. A boss/mod appearing here may also support a higher-tier cross-mod gate; document the gate in Advanced/Endgame and link here.
7. **Endgame** — overarching final milestones and cross-mod integration: Cataclysm boss requirements where applicable, Draconic Evolution's Chaos Guardian, post-Guardian unlocking of ProjectE and Project Expansion, high-tier Occultism mining, Replication and renewable endgame resources. Treat these as *documentation of independently enforced locks*; never rely on a quest completion alone as a gate.

An **Optional Challenges** section was proposed by the assistant as a potential eighth top-level group (mastery, unusual builds and achievements). **It is not a locked eighth group**; equally viable to put such quests into their respective mod chapters. Revisit only when designing challenge rewards.

## Design principles

- **Nonlinear specialty chapters.** Allow players to pursue one or several magic schools and adventure systems without artificial prerequisite chains. Only impose dependencies arising from actual required mechanics/progression gates.
- **Major content mods get chapters; addons usually do not.** Keep sidebar navigation useful even in a 125+ mod pack. Distinguish installed active gameplay providers, addons, QoL, libraries and shared infrastructure before counting chapters.
- **Cross-mod chapters communicate** suggested milestones and alternative routes; mod chapters describe each subsystem's mechanics, progression and unique rewards. Prefer links/dependencies over duplicate quest payouts.
- **Discoverable guidance, not compulsory checklists.** Tips/Tricks is a reference; interesting unusual mechanics, discovery and exploration should earn rewards without requiring every single recipe.
- **Per-player vs team semantics must be deliberate.** Personal unlocks/starter grants are per-player. Co-op structures, boss encounters and shared achievements may benefit from team progress; resolve individually. Test claims and replays.
- **Safe rewards.** No forbidden Ring of the Seven Deadly Sins, no normal pre-Guardian ProjectE, no progression bypass through quest loot, no untested game-breaking artifacts.
- **Preserve source-backed item IDs and states.** Only author concrete quest tasks/rewards for items actually available in the pinned build and verified in registry/runtime.
- **Separate authored from verified.** FTB Quests parsing, GUI, item filters, team behavior, reward grants and world joins require client/server integration validation.

## Near-term authoring order

1. Finish **Introduction** and the book's organization/navigation.
2. Author **Tips & Tricks** and Basic Progression scaffold, linking major systems but not enforcing prereqs.
3. Inventory substantive gameplay mods and produce a chapter catalog (magic versus exploration/combat) with integration notes and addon grouping.
4. Develop Advanced Progression and Endgame around actual verified gates, then fill the specialist chapters in parallel.

This document does **not** authorize new mods, reward additions, gameplay gates or mass quest implementation by itself.
