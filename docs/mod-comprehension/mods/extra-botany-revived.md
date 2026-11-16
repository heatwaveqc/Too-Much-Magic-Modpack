# Extra Botany Revived — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `extra-botany-revived` / `extrabotany` |
| Category / parent or addons | Botania addon; unofficial NeoForge port of Extra Botany / Extra Botany: Reburn, built for Botania Revived |
| MC / loader / pack target | 1.21.1 / NeoForge / TMM `test/full-integration` |
| Pinned version / artifact / hash | 1.0.4; `ExtraBotany-1.21.1-1.0.4-NeoForge.jar`; CurseForge project 1764698, file 9599226; SHA-1 `246e372964413d09c5f2fa0c93b95a4c224d1949` |
| Source snapshot | [Commit `b58857d3658479f6e8684d3b45f6d3138a294548`](https://github.com/Velron-Devs/Extra-Botany-Revived/tree/b58857d3658479f6e8684d3b45f6d3138a294548), version bump 1.0.4; Gradle metadata declares MC 1.21.1, NeoForge 21.1.240+, mod 1.0.4 and Botania Revived 2.0.2+. Binary equivalence not tested. |
| Guide/documentation version gap | CurseForge says a Patchouli book and entries added to Botania's Lexicon; exact pinned JAR entries/recipes not extracted. |
| Profile status | researching |
| Evidence reached | exact pack pin; exact source release commit and dependencies; official CurseForge project/file metadata; no client test |
| Reviewed | 2026-11-16 |
| Quest ownership | No quest edits in this research pass |

## Executive gameplay map

**What the player actually does:** Extra Botany Revived extends Botania Revived with additional generating and functional flowers, relics, weapons, armor, curios, mounts, utility devices and boss encounters. Its roster includes Gaia III, Ego and the Herrscher of the Void; the CurseForge description also names Excalibur, Failnaught, Photon Shotgun, First Fractal, Pandora's Box, Judah Oath, several hammer tiers, Starry Idol and other armor sets, three Airships, a Mana Charger, Mana Buffer, Enchanter, Mini Handbag, cocktails and potion effects. The release profile says the addon adds a Patchouli book and entries to Botania's Lexicon. This establishes a large addon surface, but not the acquisition routes, recipes, mana costs, boss gates or exact victory rewards. [docs: CF project page; source: 1.0.4 changelog]

**Why it matters in TMM:** This is not an independent magic progression: its declared base is Botania Revived, pinned at 2.0.2, and the addon source requires Botania Revived 2.0.2+. Treat it as additional Botania branches, and document those branches separately from the base Botania loop. The Mini Handbag and extra combat/boss equipment have potential storage and power-balance implications that should be checked against TMM's existing decisions before quest reward use.

**Quick start:** Start with the Botania Revived Lexica and the addon's own Patchouli book. The addon has no confirmed standalone entry recipe or first unlock in the reviewed sources; do not assume it needs only the player's first Botania flower. The exact guide/opening sequence remains to be inspected in the pinned client.

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Additional mana-generating flowers; new functional flowers; new relics/weapons; three boss encounters; mounts and utility devices | No early unlock is yet source-mapped; guide/JEI inspection is required | Botania Lexicon entries; Curios equipment slots; Botania mana network and boss progression |

### Subsystem coverage at a glance

| Subsystem / branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Patchouli book and Botania Lexicon integration | [official project page](https://www.curseforge.com/minecraft/mc-mods/extra-botany-revived); source README | partial | Addon guide and Botania Lexicon; entry names unknown | not run | Inspect guide pages/conditions and opening recipe |
| Generating and functional flowers | official project feature list | roster-level only | Lexicon/JEI expected; exact pages unverified | not run | Inventory flowers, accepted inputs, mana rates and outputs |
| Weapons, relics, hammers, armor, Curios | official project feature list; 1.0.4 changelog | roster-level only | Exact entries/JEI categories unknown | not run | Enumerate recipes, passive/active effects and costs |
| Bosses and rewards | official project names three boss encounters | unresearched progression | Exact summon recipe, arena and guide pages unknown | not run | Map gates, fight requirements, phases, drops and repeatability |
| Airships / mounts | official project identifies three elemental Airships | unresearched | Exact guide/JEI path unknown | not run | Inspect assembly, fuel, controls and travel boundaries |
| Utility systems | official project identifies Mini Handbag, Mana Charger/Buffer, Enchanter, pedestals, cocktails and effects | unresearched | Patchouli/JEI; exact pages unknown | not run | Determine I/O, slot behavior, mana costs and potion effects |
| 1.0.4 changes | [release file 9599226](https://www.curseforge.com/minecraft/mc-mods/extra-botany-revived/files/9599226) | release maintenance only | not applicable | not run | Changelog reports transparency/texture fixes only |

## Guidebook onboarding — required before the player's first complex step

- **Guide name and access:** The project lists both a Patchouli book and additions to the Botania Lexicon. Exact book item, recipe and whether the addon uses the existing `botania:lexicon` have not been verified from the pinned JAR.
- **Earliest attainable stage:** Unknown. The Botania Revived base is present; the exact addon entry conditions and required parent progression are not yet mapped.
- **First instructional quest:** Not authored. A walkthrough should direct the player to the correct addon guide after source/runtime confirms how it is opened.
- **Reference map:** Guide entry names, conditional unlocks and JEI recipe categories are unverified.
- **Guide limitation:** The project page is an overview, not a step-by-step quest path. It does not document boss summons, flower inputs or equipment recipes.
- **Required pairing:** Source metadata requires Botania Revived `2.0.2+`; TMM pins Botania Revived `2.0.2`, so the declared lower bound is met. The exact mod pairing has not been client-tested.

## Systems and mechanics inventory

| Subsystem | Known contents | Inputs + costs | Effects / outputs | Limits and evidence |
| --- | --- | --- | --- | --- |
| Generating Flora | New generating flowers, including floating variants | Unknown flower-specific burnables/events and mana rates | Mana into Botania's mana network | Feature class only; per-flower recipes and triggers not yet audited |
| Functional Flora | New functional flowers | Unknown mana/valid target behavior | Automation or world/player effects, exact behavior unknown | Feature class only |
| Relics and weapons | Named examples include Excalibur, Failnaught, Photon Shotgun, First Fractal, Pandora's Box, Judah Oath; multiple hammer tiers | Recipes, mana/material costs, cooldowns and activation requirements unknown | Combat actions, passive effects or special abilities unknown | Do not assign stats/effects from other versions or original-mod docs |
| Boss encounters | Gaia III, Ego, Herrscher of the Void | Summoning items, dimensions, prerequisites and player scaling unknown | Boss entities and rewards unknown | Boss names verified; fight mechanics not established |
| Armor and baubles | Starry Idol, Goblin Slayer, Pleiades Combat Maid and other sets; rings/amulets/goggles and mana-oriented accessories | Crafting paths/Curios slot rules unknown | Combat, mobility and resource modifiers unknown | Check attribute caps and Curios conflicts in the actual pin |
| Mounts | Manasteel, Elementium and Terrasteel Airships; Motor and UFO | Assembly, fuel, mana and controls unknown | Rideable mobility | Feature roster only |
| Mana/utility devices | Mana Charger, Mana Buffer, Enchanter, pedestals, cocktails | Exact input kinds/quantities unknown | May charge, store, enchant or alter effects; source behavior not yet checked | Names are not enough to establish their contracts |
| Mini Handbag | Project description says 24 slots and four armor slots | Acquisition/capacity/filtering/Curios rules unknown | Portable inventory/storage, exact slot meaning unverified | Policy review needed against TMM storage constraints |
| Patchouli/Lexicon extension | Addon guide and new entries in Botania's guide | Entry conditions unknown | In-game documentation for addon content | Exact guide pages not extracted |

## Material / capability input-output contracts

| System | Input kind + exact ID/tag/event | Input qty/formula; consumed/remainder | Output kind and location | Trigger/cadence; interface | Evidence and confidence |
| --- | --- | --- | --- | --- | --- |
| Addon flower → mana | flower-specific item, event or world state; exact inputs unknown | unknown | Botania mana into compatible mana network, assumed from feature category but not confirmed per item | flower-specific | Project says it adds generating flowers; exact contracts unresearched |
| Mana Charger | Mana input source unknown | unknown | Charged item/resource unknown | machine/item operation unknown | Named by project page only |
| Mana Buffer | Mana input/output and capacity unknown | unknown | Stored mana or device-buffer effect unverified | unknown | Named by project page only |
| Mini Handbag | item stacks | 24 inventory slots are reported by project; armor slots ambiguous | Portable inventory item | item/Curios interaction unknown | Project description; exact pinned item not tested |
| Boss encounters | player/world/summon item unknown | unknown | Boss entity; reward tables unknown | summon/encounter unknown | Boss names only |
| Potions/cocktails | fluid/item ingredients unknown | unknown | Potion/effect and container results unknown | brewing/use unknown | Project feature list |

### Acquisition, renewability and output economics

- Recipe sources, loot routes, renewability and drop quantities are not yet established.
- Boss rewards and repeat conditions need particular attention before using the addon as a progression or farming route.
- The reported Mini Handbag adds portable storage; its slot semantics and TMM compatibility must be verified rather than inferred from its name.
- Flower rates and mana costs should be compared to the base Botania Revived mana economy only after exact 1.0.4 data is examined.

## Complete player progression / walkthrough blueprint

**Completion test:** Not met. The official overview identifies the addon content, but does not provide enough detail to author a start-to-end walkthrough.

| Path and step | Gate or choice? | Exact prerequisite / materials / world condition | Player actions | Unlock / visible success / next step | In-game guide or JEI reference | Walkthrough coverage |
| --- | --- | --- | --- | --- | --- | --- |
| 0. Open addon guide | First step, access unknown | Patchouli and Botania Revived are declared dependencies | Find/craft/open addon Patchouli guide and inspect Lexicon | Guide entries visible | Exact title/item/recipe unknown | Partial |
| 1. Addon flowers | Branch unlock unknown | Botania base progression and recipe inputs unknown | Craft/use generating or functional flower | Mana or effect/output unknown | Entry names unknown | Unresearched |
| 2. Utility items and equipment | Optional branches | Recipes, Curios slot and mana requirements unknown | Craft/charge/equip/use items | Exact effect and progression gate unknown | JEI/Lexicon entries not inspected | Unresearched |
| 3. Mounts | Optional branch | Airship/Motor/UFO recipes and control requirements unknown | Assemble, mount and operate | Mobility | Exact guide entry unknown | Unresearched |
| 4. Gaia III, Ego, Herrscher of the Void | Boss branch/possible capstone | Summon recipes, arena and gear gates unknown | Trigger each encounter and collect rewards | Boss defeated and reward output unknown | Exact guide and JEI entries unknown | Unresearched |
| 5. Remaining relics and effects | Completion branch unknown | Complete item list and gates not audited | Craft and use each meaningful family | Terminal objective unknown | Lexicon entry map unknown | Unresearched |

### Dependency graph and alternate routes

Extra Botany Revived is layered onto Botania Revived rather than replacing its progression. The source's minimum dependency is Botania Revived `2.0.2`, which matches the TMM pin; Patchouli and Curios are required. JEI is listed as optional upstream and is installed in TMM. The exact meaning of any Botania page unlock, boss gate, item dependency or module compatibility must be established from the pinned addon JAR.

### Structures, rituals and automation

No boss arena geometry, mount construction, flower placement pattern, mana buffer capacity, or device automation behavior is established here. Do not derive those details from the original 1.12.2 addon without confirming the NeoForge port.

### Advanced and terminal objectives

- **Normal completion:** Not established.
- **Optional completion:** Three named bosses, all flower/utility families, equipment and mounts are candidate branches, but no actual terminal target is documented.
- **Remaining progression blockers:** Guide entry names and unlock order; exact recipes and typed I/O; boss spawn conditions, attacks and drops; Mini Handbag slot behavior; actual mana rates and Curios interactions.
- **Quest coverage check:** Incomplete; no quest files changed.

## Internal combinations and overlooked mechanics

| Internal combination | Full chain | Benefit | Limitation / exploit potential | Evidence |
| --- | --- | --- | --- | --- |
| Addon flower + Botania Revived mana network | addon generating flower → mana spreader/pool or another valid mana target | Extends base mana production options | Exact flower triggers/rates and valid receivers unknown | Project says additional generating flowers; requires pin inspection |
| Mana utility + equipment | Mana Charger/Buffer → item, relic or Curios equipment | May add an item-charging/equipment-support loop | No source-backed resource contract yet | Feature roster only |
| Boss rewards + tiered gear | summon boss → defeat → collect reward → use in advanced recipes | Possible endgame branch | Must verify repeatability, reward count and boss access before progression design | Boss names only |
| Mini Handbag + travel | portable storage + Airship/mount | Convenience | Could duplicate restricted generic portable storage; slot count/contents need policy check | Project listing only |

## Low-hanging setups useful in ordinary survival

No addon setup can yet be recommended as early-game: the release page does not provide recipes, acquisition routes or per-system costs. The Botania Revived base profile covers its core first-week loop. Avoid inventing a cheap Extra Botany unlock before inspecting the Lexicon/JEI.

## API, integration and pack-config surfaces

| Hook or customization surface | Exact surface | Capability / constraint | Current TMM override? | Evidence |
| --- | --- | --- | --- | --- |
| Mod identity | `extrabotany` | Addon namespace registered separately from Botania | no override identified | upstream README/Gradle metadata |
| Parent dependency | Botania Revived `[2.0.2,)` | Hard dependency declared by port source | TMM pins 2.0.2 | upstream Gradle metadata + TMM pin |
| Guidebook | Patchouli book and Lexicon entry providers | Addon content documented in both guide surfaces | no override identified | official CF project page |
| Optional viewer/script integration | JEI, EMI, KubeJS listed as optional | Possible recipe-viewer/script surfaces; actual addon serializers and recipes unknown | JEI installed; no addon recipe override found | upstream project description |
| Curios | Required dependency | Baubles/armor accessories depend on Curios; slot requirements unknown | Curios installed | upstream project/metadata |
| GameTest source | Upstream changelog says 234 GameTests live in a separate `gametest` source set, not the release jar | Tests cover recipes, weapons/relics, armor, baubles, flowers, bosses, mounts, handbag, effects and loot | no action | 1.0.0 changelog; tests not executed here |

## Compatibility leads

| Other mod/system | Candidate link and why | Native / pack-built / bridge | Status |
| --- | --- | --- | --- |
| Botania Revived | Required parent; Lexicon and mana systems are extended | Native | Source-declared; TMM pins exact minimum 2.0.2; client untested |
| Patchouli | Required guide framework | Native dependency | Installed; addon guide not rendered in TMM |
| Curios | Required for baubles/equipment | Native dependency | Installed; exact slot conflicts and item modifiers untested |
| TMM storage policy | Mini Handbag adds 24 slots plus four armor slots per listing | Native content | Needs owner-policy review before quest reward or storage claims |
| TMM power progression | Boss rewards, mana tools, relics and hammer tiers can be high-impact | Native addon progression | Audit recipes/rewards against existing caps and boss order |

## Quest and teaching opportunities

- Add one addon onboarding step once the guidebook item and Lexicon entry are verified.
- Teach flowers by their exact input type and trigger, including mana-per-cycle and valid spreader/pool setup.
- Keep each boss encounter and reward on its own documented branch; an item task alone does not prove the encounter.
- Treat Mini Handbag as a balance/policy review item before offering it as a quest reward.
- Do not mix Extra Botany's boss progression into base Botania's Gaia Guardian route until exact gates establish their order.

## Targeted verification and open questions

1. Inspect pinned JAR metadata, registries, recipes, Patchouli book and Lexicon data; compare them to commit `b58857d`.
2. Build a feature inventory from all registered flowers, items, blocks, entities, effects and recipes.
3. For every generating flower, capture exact inputs, world/event trigger, mana amount/cadence, spreader compatibility and byproducts.
4. For each functional flower, map target rules, mana costs, range and effect.
5. Extract exact weapon/relic/armor Curios behavior, attribute modifiers, cooldowns, costs and recipe gates.
6. Map Gaia III, Ego and Herrscher of the Void summon conditions, arena requirements, phases, drops and repeat rules.
7. Verify Mini Handbag capacity, armor-slot implementation, contents, Curios compatibility and restriction implications.
8. Inspect the Lexicon/Patchouli visibility conditions and reconcile every addon progression stage to an in-game reference.
9. Run no tests in this research pass; client/world verification remains separate.

## Evidence ledger

- **[pin]** `mods/extra-botany-revived.pw.toml`: CurseForge project 1764698, file 9599226, 1.21.1 NeoForge 1.0.4, SHA-1 recorded above.
- **[pin]** Parent `mods/botania-revived.pw.toml`: Botania Revived 2.0.2, CurseForge file 9577651, SHA-1 `b333e5ac4fd55b0e8822238ced0b25b193db9628`.
- **[release metadata]** [CurseForge 1.0.4 file](https://www.curseforge.com/minecraft/mc-mods/extra-botany-revived/files/9599226): Release, MC 1.21.1, NeoForge, uploaded 2026-11-13; release note is limited to rendering fixes.
- **[source: 1.0.4]** [Upstream changelog](https://github.com/Velron-Devs/Extra-Botany-Revived/blob/b58857d3658479f6e8684d3b45f6d3138a294548/CHANGELOG.md) and [Gradle metadata](https://github.com/Velron-Devs/Extra-Botany-Revived/blob/b58857d3658479f6e8684d3b45f6d3138a294548/gradle.properties) identify version, dependency floor and port history. Binary equivalence not tested.
- **[docs: current project page]** [CurseForge description](https://www.curseforge.com/minecraft/mc-mods/extra-botany-revived) lists content families, bosses, utility items, dependencies, and guide integration. It is a feature overview, not a progression walkthrough.
- **[pack-test]** No client/world test run. No quest/runtime changes.

**Method note:** For rapidly released unofficial ports, the exact file page, source version declaration, changelog and dependency floor are useful anchors, but they do not replace extracting the pinned guide and recipes. Here the feature roster is known; player progression is not.
