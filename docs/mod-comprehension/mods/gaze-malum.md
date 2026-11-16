# Gaze (A Malum Addon) — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `gaze-malum` / exact runtime mod ID not independently inspected |
| Category / parent or addons | Magic addon for Malum |
| MC / loader / pack target | 1.21.1 / NeoForge / TMM `test/full-integration` |
| Pinned version / artifact / hash | `gaze-1.1.7.1.jar`; Modrinth project `NlvaJ5WE`, version `od4ltbRo`; SHA-512 `7e9ecdcc996f376e280bf46f5c0cfd328fb6f1228b1dac469390031458e7a81ad56acff03d9bb6b57b8209586798ab443be1e91fa8fe56297a9e7c9451db03e2` |
| Source snapshot | No public source link was present in the official project pages reviewed. CurseForge lists the project as All Rights Reserved; no JAR/source inspection performed. |
| Guide/documentation version gap | Official descriptions give a feature roster; no detailed guide progression or recipe documentation located. CurseForge release note for 1.1.7.1 says it remains for Malum 1.8. |
| Profile status | researching |
| Evidence reached | exact pack pin; official Modrinth and CurseForge project/file pages; no source or client test |
| Reviewed | 2026-11-16 |
| Quest ownership | No quest edits in this research pass |

## Executive gameplay map

**What the player actually does:** Gaze extends Malum's soul-themed progression. The official feature list identifies a new progression screen, two Geas, additional Rites, six weapons, eight runes, five Curios, a new pouch, and an Anima Bestiary (the latter's release note says a blank/jittering bestiary display was fixed). The descriptions do not explain how to unlock, craft, perform, equip or progress through any of these features. We therefore know the addon expands Malum's soul-use systems, but cannot yet present a trustworthy player walkthrough or exact material flow. [docs: official CurseForge page and 1.1.7.1 release note]

**Why it matters in TMM:** Gaze is a substantial continuation branch for the already-present [Malum profile](malum.md), not a separate magic economy. The 1.1.7.1 release note says it remains compatible with Malum 1.8; TMM pins Malum 1.8.2, so the declared version line aligns. This is not an in-pack compatibility result.

**Quick start:** Open Malum's in-game guide and check for Gaze entries/progression screen once installed. The exact Gaze guide name, opening action and first attainable content are unknown. Do not treat the feature list as a recipe path.

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| New progression screen; two Geas; additional Rites; six weapons and eight runes; five Curios plus a pouch | None can be identified from official sources without recipes or unlock information | Malum guide/progression screen; Malum Rites; Malum Curios/equipment |

### Subsystem coverage at a glance

| Subsystem / branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Progression screen | Official feature list | roster-only | Page title and actions unknown | not run | Need pinned UI/page inspection or upstream instructions |
| Geas (two) | Official feature list | unresearched | No entry names supplied | not run | Need exact Geas effects, prerequisites, costs and removal |
| Rites | Official feature list says a new set | unresearched | No Rite names or construction instructions supplied | not run | Need rite list, layout, inputs, duration, targets and outputs |
| Weapons (six) | Official feature list | roster-only | Exact names/recipes/effects absent from descriptions | not run | Need item list, stats, activations, costs and recipes |
| Runes (eight) | Official feature list; release note names Fafnir | partial name evidence only | JEI/category/pages unknown | not run | Need full rune roster, slot rules, effects and set interaction |
| Curios (five) | Official feature list | roster-only | Exact items and slot types unknown | not run | Need modifiers, activation, persistence and slot conflicts |
| Pouch | Official feature list | unresearched | Exact name/capacity/collection behavior unknown | not run | Need storage and pickup behavior; compare against pack storage policy |
| Anima Bestiary | Release note says its entries/display were fixed in 1.1.7.1 | partial | Exact opening route and contents unknown | not run | Need guide text and any mob/animation/collection interaction |
| Malum 1.8 compatibility | CurseForge 1.1.7.1 note says release remains for Malum 1.8 | declared version alignment | Not a player progression path | not run | Test with exact TMM pins before claiming functional compatibility |

## Guidebook onboarding

- **Guide name and access:** Not established. Gaze is described as an expansion for Malum; official text does not name its guidebook or explain whether content is added to Malum's Encyclopedia Arcana.
- **Earliest attainable stage:** Unknown.
- **First instructional quest:** Not authored. First identify the progression-screen access and the first Gaze entry in game.
- **Reference map:** No exact Geas, Rite, rune, weapon, Curio or Bestiary entry names are given in the official pages reviewed.
- **Guide limitation:** The feature list is not a how-to. It supplies no recipe, placement, costs, unlock ordering or operational inputs/outputs.

## Systems and mechanics inventory

| Subsystem | Known contents | Inputs + costs | Effects / outputs | Limits and evidence |
| --- | --- | --- | --- | --- |
| New progression screen | Officially listed | Entry/access conditions unknown | New player progression UI | No screen capture, guide or source inspection |
| Geas | Two new Geas | Requirements, offerings, soul resource and eligibility unknown | Exact player/entity/world effect unknown | Do not infer from the general term or other mods |
| Rites | New set of Malum Rites | Totem arrangement, pole order, ingredients, spirit costs and activation unknown | Effects and duration unknown | No named rites exposed in project copy |
| Weapons | Six additions | Crafting costs, damage, durability, cooldowns and ability activators unknown | Combat behavior unknown | One file lists no mechanics |
| Runes | Eight additions | Socket, recipe and spirit cost unknown | Effects unknown | Release note specifically names Fafnir, which grants a bonus when wearing a full set of Malignant armor; no bonus value or full matching set route established |
| Curios | Five additions | Crafting and Curios slot types unknown | Attribute or special effects unknown | Exact objects are not listed |
| Pouch | One new pouch | Capacity, storage rules and auto-collection unknown | Item holding or pickup behavior unknown | Needs review against TMM storage boundaries |
| Anima Bestiary | Bestiary item/UI or entry surface implied by release notes | Acquisition and collection conditions unknown | Mob/Anima information or tracking not established | Release note only confirms the Bestiary display was fixed |

### Material / capability input-output contracts

| System | Input kind + exact ID/tag/event | Input qty/formula; consumed/remainder | Output kind and location | Trigger/cadence/interface | Evidence |
| --- | --- | --- | --- | --- | --- |
| Geas | Not established | Not established | Not established | Not established | Official page only names two Geas |
| Rites | Not established | Not established | Not established | Not established | Official page only says a new set |
| Runes | Fafnir rune name reported by release notes; exact ID unknown | Source says its full Malignant armor condition grants a bonus; no parameters | Player/equipment effect, exact stat unknown | Armor set condition | CurseForge 1.1.7.1 notes |
| Pouch | Item stack / auto-pickup possibility unknown | Capacity/filters unknown | Inventory contents or collected item entities unknown | Unknown | Official page only names a pouch |
| Progression screen | Player state/advancement unknown | Unknown | UI state/unlock presentation unknown | Unknown | Official page only names screen |

### Acquisition, renewability and output economics

- No recipe inputs, mob drops, rite offerings, recurring resource costs or reward quantities are documented in the pages reviewed.
- Whether the Geas, Rites or weapons consume Malum spirits, materials, player health, durability or another resource is unknown; do not assume “soul expansion” means a specific cost.
- The pouch could affect item collection or storage. Its actual behavior must be checked before using it in quest rewards or progression balance.

## Complete player progression / walkthrough blueprint

**Completion test:** Not met. No start-to-end player path can be built from the available public overview.

| Path and step | Gate or choice? | Exact prerequisite / materials / world condition | Player actions | Unlock / visible success / next step | In-game guide or JEI reference | Walkthrough coverage |
| --- | --- | --- | --- | --- | --- | --- |
| 0. Find addon guide/screen | Initial access unknown | Unknown | Open relevant guide/progression screen | Unknown | Exact title and route unknown | Blocked by absent instructions |
| 1. Unlock first Geas | Unknown | Unknown | Unknown | One of two Geas | Unknown | Unresearched |
| 2. Learn new Rites | Unknown | Unknown | Construct/activate Rite only after exact directions are known | Additional Rite effect | Unknown | Unresearched |
| 3. Obtain weapons/runes/Curios | Optional gear branches unknown | Recipes and gates unknown | Craft/equip/use | Combat or passive bonuses unknown | JEI/guide page unknown | Unresearched |
| 4. Obtain/use pouch and Bestiary | Optional utility branch unknown | Acquisition unknown | Acquire, fill/open/use | Capacity, collection and Bestiary functions unknown | Unknown | Unresearched |
| 5. Terminal progression objective | Unknown | Unknown | Unknown | No endpoint defined | Unknown | Unresearched |

### Dependency graph and alternate routes

The addon is described as a Malum extension. Its 1.1.7.1 file notes “Still for Malum 1.8”; TMM pins Malum 1.8.2. There is no evidence in the sources reviewed for a hard progression gate on Malum's base content, an alternate entry route, or a separate currency. The addon page also warns that the project is actively developed and prone to major reworks; any future version change should trigger a profile refresh.

### Structures, rituals, automation and other how-to instructions

No Rite layout, activation method, resource input, duration, range or automation behavior is established. Malum's existing Rite Anchor/Locus behavior cannot be assumed to describe Gaze's new Rites without evidence.

### Advanced and terminal objectives

- **Normal completion:** Not established.
- **Optional completion:** New Geas, Rites, weapons, runes, Curios, pouch and Bestiary are known content groups.
- **Remaining progression blockers:** Exact guide access and entry map; all recipe/loot IDs; rite construction and costs; Geas effects; item stats; pouch behavior; any new mobs/structures.
- **Quest coverage check:** Incomplete; no quest files changed.

## Internal combinations and overlooked mechanics

| Internal combination | Full chain | Benefit | Limitation / exploit potential | Evidence |
| --- | --- | --- | --- | --- |
| Fafnir + Malignant armor | Equip Fafnir rune + full Malignant armor set | Official release note confirms a conditional bonus | Bonus value, complete armor set and slot rules unknown | CurseForge 1.1.7.1 release note |
| Gaze content + Malum progression | Malum base progression → Gaze screen/entries → Geas/Rites/gear | Extends Malum's soul-themed journey | Unlock graph and resource coupling unverified | Official addon description |

## Low-hanging setups useful in ordinary survival

No early setup can be recommended yet. The official descriptions lack the recipe and access information needed to call any Gaze item an early unlock.

## API, integration and pack-config surfaces

| Surface | Known capability / constraint | Current TMM override? | Evidence |
| --- | --- | --- | --- |
| Modrinth release metadata | 1.21.1 NeoForge, both client/server; exact version ID `od4ltbRo` | Pack pin exists | Modrinth 1.1.7.1 page |
| Malum compatibility | Release note says still for Malum 1.8 | TMM pins Malum 1.8.2 | Compatible version line declared, not tested |
| Public source | Not linked by the official pages reviewed; CurseForge identifies All Rights Reserved | No code, recipes or assets copied | Modrinth and CurseForge project pages |
| Config/API/data pack | Not established | Unknown | No public source or detailed documentation found |

## Compatibility leads

| Other mod/system | Candidate link and why | Native / pack-built / bridge | Status |
| --- | --- | --- | --- |
| Malum | Parent mod and soul/rite ecosystem | Native addon integration | Version line aligns with Malum 1.8.2; client behavior untested |
| Curios | Five additional curios are reported | Likely uses base Curios path, exact slot rules unknown | No specific compatibility statement; inspect in client |
| TMM portable storage rules | New pouch might add portable inventory or auto-collection | Native addon feature | Review actual capacity/behavior before reward use |
| Iron's Spells | Official project says compatibility was planned, not included in listed current features | Would require a later addon release or integration | Not present in 1.1.7.1 feature list |

## Quest and teaching opportunities

- Treat the progression screen and guide access as the first research target before authoring any Gaze quest.
- Teach each Rite only after documenting its full layout, activation, resource cost, duration and success/failure signal.
- Present Fafnir's Malignant armor condition accurately once the bonus and set are verified.
- Keep the pouch out of rewards until its inventory/collection function is tested against pack rules.
- Do not teach planned Iron's compatibility as a current 1.1.7.1 feature.

## Targeted verification and open questions

1. Inspect the pinned JAR metadata, registry namespaces, language entries, recipes, loot, tags, advancements, Patchouli data and config.
2. Record the progression screen's actual entry requirements and all unlock states.
3. Enumerate the two Geas and each new Rite; document exact input objects, placement, resource consumption, timer, target and output/effect.
4. Inventory six weapons, eight runes and five Curios with recipe IDs, stats, costs, set conditions and slot rules.
5. Determine the pouch's capacity and whether it auto-collects, filters or stores Malum-specific resources.
6. Verify Anima Bestiary access and content.
7. Test the exact Gaze 1.1.7.1 + Malum 1.8.2 pair in a disposable TMM client if a client is available.

**Research blocker:** Public sources currently establish only the feature roster and one conditional rune effect. The official pages provide no detailed how-to, and CurseForge labels Gaze All Rights Reserved with no public source link. Do not reverse-engineer or redistribute restricted code/assets. The profile remains partial until the owner/developer provides sufficient documentation or a legitimate inspectable artifact.

## Evidence ledger

- **[pin]** TMM `mods/gaze-a-malum-addon.pw.toml`: project `NlvaJ5WE`, version `od4ltbRo`, file `gaze-1.1.7.1.jar`, SHA-512 recorded above.
- **[pin]** TMM `mods/malum.pw.toml` and [Malum profile](malum.md): Malum 1.8.2.
- **[docs: current]** [Modrinth Gaze project page](https://modrinth.com/mod/gaze-a-malum-addon/): identifies Gaze as a Malum addon and warns it is actively developed and prone to major reworks.
- **[release metadata]** [Modrinth 1.1.7.1](https://modrinth.com/mod/gaze-a-malum-addon/version/od4ltbRo): Release, 1.21.1 NeoForge, client and server; no changelog supplied.
- **[docs: project feature list]** [CurseForge project page](https://www.curseforge.com/minecraft/mc-mods/gaze-a-malum-addon): new progression screen, two Geas, new Rites, six weapons, eight runes, five Curios, a pouch; also notes planned Iron's compatibility separately.
- **[release metadata]** [CurseForge file 7261638](https://www.curseforge.com/minecraft/mc-mods/gaze-a-malum-addon/files/7261638): 1.1.7.1 release for 1.21.1 NeoForge; note says it remains for Malum 1.8, names Fafnir rune's full Malignant-armor bonus and Bestiary fixes.
- **[license/access]** Modrinth labels this release ARR and CurseForge lists All Rights Reserved; no source link was found on the reviewed official pages.
- **[pack-test]** No client/world test or code inspection. No quests/runtime changed.

**Method note:** When addon source is unavailable and developer docs only list features, record the confirmed surface, exact integration pins, and the specific questions that block a walkthrough. Do not convert feature names into presumed mechanics.
