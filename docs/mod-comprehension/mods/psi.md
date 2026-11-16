# Psi — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | psi / `psi` |
| Category / parent or addons | Magic; spell-programming system. Installed addon: Psi: Tweaks and Additions (separate profile required). |
| MC / loader / pack target | Minecraft 1.21.1 / NeoForge / TMM `test/full-integration` |
| Pinned version / artifact / hash | `mods/psi.pw.toml`; `Psi-neoforge-1.21.1-110.jar`; Modrinth project `pOeA0exL`, version `j9TFdTKC`; SHA512 `2bbe6b40f230cd495a5342db61fb5a6c2a4d03fda0ee5fe0a21d8b7ccc8494d1d3e7fa5f54236d5f90cbe78a8bb75c27e3bb9cd9b85e4b6eed67aa976ffe857a` [pin] |
| Source snapshot | Official `VazkiiMods/Psi` tag `release-1.21.1-110`; its `gradle.properties` declares MC 1.21.1, mod ID `psi`, version 110. Tag commit SHA and artifact equivalence were not established; source is version-aligned, not proof of JAR provenance. |
| Guide/documentation version gap | Official overview describes a leveling system with in-game documentation/tutorials; official Codex page says it mirrors the in-game Encyclopaedia opened by the Psi master keybind. The web Codex could not be retrieved; exact 1.21.1 guide entries, unlock conditions and names remain unverified. |
| Profile status | researching |
| Evidence reached | Exact packwiz pin; official upstream release tag metadata; official project overview, FAQ and changelog; no direct JAR inspection or client test |
| Reviewed | 2026-11-16 |
| Quest ownership | No Psi quest chapter reconciled in this pass. No quest files changed. |

## Executive gameplay map

**What the player actually does:** Psi is a programming-focused magic system. The player writes an action sequence (a spell) from selectors, operators and actions using vector/math concepts, then runs that spell in the world. Its official description says the mod teaches concepts and spell pieces through a leveling system with in-game documentation/tutorials, tying progression rewards to the player's creation process. The official site and current pinned source metadata do not establish the exact survival unlock order, item costs, spell-piece tiers, or terminal progression. Treat specific CAD, bullet, tool, armor and material progression as research questions until checked against the pinned artifact and guide.

**Why it matters in TMM:** Psi offers player-authored conditional logic and programmable actions rather than a fixed spell list or altar recipe. This could cover utility or combat behaviors unavailable in other schools, but the enabled roster also contains Psi: Tweaks and Additions; inventory the addon's actual contribution before writing quests or promising capabilities.

**Quick start (research outline, not yet a verified walkthrough):**
1. In Controls, locate the Psi master keybind and open the Encyclopaedia/Codex; confirm the in-game label and first tutorial in a client.
2. Follow the earliest tutorial to obtain the first programming interface and casting instrument. Exact acquisition, station and material requirements are not established from the sources retrieved.
3. Program and validate a minimal spell using the guide's first example; identify how the guide awards progress and unlocks additional pieces.
4. Transfer the validated spell into the first castable item/device and test the actual target, cost and success feedback.
5. Continue through all guide levels, component upgrades, casting forms and equipment branches. The full sequence and endpoint still need source/JAR review.

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Player-authored action sequences; vector/math-based spell construction; tutorial/leveling progression; broad spell-piece vocabulary (exact pinned catalog not yet enumerated); shared/importable spell workflows (exact 1.21.1 support needs verification). | Exact early recipes and functions not established. The in-game tutorial system is the likely first teaching route, but its initial access trigger needs confirmation. Do not label a specific spell or tool an early unlock until verified. | Psi: Tweaks and Additions is installed and must be profiled independently; programmable actions may complement other spell schools (hypothesis only); spell cast/progression events may be useful quest detectors (API/event evidence not yet inspected). |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Encyclopaedia, tutorials and leveling | Official overview says levels include documentation/tutorials; official Codex page says it mirrors in-game guide accessed by master keybind [docs: current official site] | partial | Encyclopaedia/Codex; exact category and first access step unverified | not run | Retrieve exact guide content and advancement/level trigger chain from pinned artifact |
| Spell programming | Official overview confirms action sequences (“Spells”) and vector/math concepts [docs: current official site] | partial | Tutorial sections and programmer UI references not retrieved | not run | Inventory all selectors/operators/actions and piece unlocks, constraints and compile errors |
| Casting instruments and spell storage | Not established for exact 1.21.1 pin; older changelog mentions CADs/bullets/drives, but is not used as proof of pinned mechanics | unresearched | Exact guide entries and JEI categories unknown | not run | Inspect pinned item/recipe data and source at tag |
| Progression materials and crafting | Not established | unresearched | JEI expected to expose recipes but actual categories not checked | not run | Confirm first materials, acquisition sources, recipe types, tiers and renewable paths |
| Tool/armor and advanced use | Official historical changelog contains tool/armor and advanced bullet concepts for older versions only; not treated as current proof | unresearched | Exact current guide sections unknown | not run | Audit 1.21.1 registry and guide; verify every substantive branch |
| Psi: Tweaks and Additions | Pack roster confirms addon enabled; addon mechanics not part of base Psi evidence | partial | See [separate addon profile](psi-tweaks.md), to be created | not run | Inspect exact addon pin and supported content; crosswalk to Psi |

## Guidebook onboarding — required before the player's first complex step

- **Guide name and access:** The official website calls the guide the Encyclopaedia Psionica and says it is accessed with the Psi master keybind. The exact key name/default and whether the menu is available before any item or advancement are unverified. The keybind should be located under Controls; a future chapter should give the exact menu path and recommend changing conflicts.
- **Earliest attainable stage:** Not established. The interface may be accessible from the start, but this needs a clean survival client check.
- **First instructional quest:** “Open the Encyclopaedia” should teach Controls → Psi master keybind → first tutorial, and require completion of the first verified tutorial or a truthful item/advancement proxy. Do not reward a guide item unless the exact version uses one.
- **Reference map:** Official site describes in-game documentation/tutorials and the web Codex as a verbatim mirror. Specific tutorial names, level numbers, piece groups and page mappings remain unknown.
- **Where guide is insufficient:** No exact 1.21.1 Codex content was retrievable in this pass. If the guide omits costs, unlock triggers or station recipes, quest text must fill those gaps after inspection.
- **Other in-game help:** Spell-programming UI and JEI may expose pieces/recipes; exact coverage is not established.

## Systems and full mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and source |
| --- | --- | --- | --- | --- |
| Tutorial / leveling | In-game Encyclopaedia and leveling system per official project overview; exact first trigger unknown | Exact XP, action, item, advancement or spell completion triggers not established | Unlocks tutorial concepts/pieces according to the official overview; exact output and tier count unknown | Do not infer the advancement graph from old-version walkthroughs. [docs: official site; tag release-1.21.1-110] |
| Spell authoring | Spell-programming UI; official project describes player-created action sequences and mathematical/vector concepts | Exact spell piece registry, parameters, cost formula, validation and compile failure behavior not inspected | A programmed sequence that can be executed in-world; precise persistence/copy flow unknown | Identify compile/runtime errors, complexity/potency limits, permission checks and costs from pinned source and artifact |
| Spell execution | Exact casting instrument and input action not confirmed | Energy/mana/Psi resource, durability, cooldown and consumption order not established | Intended output is the spell's programmed world action | Do not claim FE/mana conversion, automation or target rules without source evidence |
| Spell sharing / transport | Official Modrinth description says spells can be shared/copied using in-game sharing tools and points to the community compendium | Exact copy medium, supported version encoding, item costs and addon dependencies unverified | Sharing/import/export of spell definitions, not necessarily gameplay resources | Check compatibility warnings and missing piece behavior at pin |
| Psi: Tweaks and Additions | Separate installed addon | Not established in this base profile | Not attributed to base Psi | Audit exact addon source/pin separately |

## Material / capability input-output contracts

| System | Input **kind** + exact ID/tag/event | Input qty/formula; consumed/remainder | Output **kind**, qty and location | Trigger/cadence; range/capacity | Automation/interface and evidence |
| --- | --- | --- | --- | --- | --- |
| Tutorial progression | Player state / guide action / advancement candidate; exact event unknown | Not established | Guide visibility or progression unlock candidate; exact state transition unknown | Player action / level event, exact trigger unknown | No detector claim; inspect advancement and event code |
| Spell authoring | UI edits and spell-piece entries; exact registry IDs not verified | Not established | Spell definition in interface or item candidate | User interaction; exact persistence boundary unknown | API and KubeJS event support not established |
| Spell execution | Spell definition plus casting item/device candidate | Resource, item, durability and cooldown costs all not established | Programmed spell effects in world; exact effects vary by spell | Player/entity interaction; exact radius/cadence and server checks unknown | Do not presume automation; inspect each trick's server-side implementation |
| Sharing | Spell definition and sharing tool candidate | Exact stack/form and consumption not established | Serialized/copied spell data candidate | Manual UI action | Exact input/output medium unknown; [docs: Modrinth project description] |

## Acquisition, renewability and output economics

- Exact material sources, crafting chain, resource sinks, renewable loops and upgrade costs are unresearched.
- Psi's official project description frames the core output as flexible programmed action sequences, not a general-purpose FE or fluid generator. It does not establish any pack-specific resource converter.
- The Modrinth project description says the mod includes in-game spell sharing/copying and community spell examples; sharing spell data is distinct from producing a resource item.
- Do not add recipe, loot or market substitutions until all exact IDs and gates are checked from the pinned artifact.

## Complete player progression / walkthrough blueprint

**Completion test:** not met. This is a structured research map, not a quest-ready progression graph.

| Path and step | Gate or choice? | Exact prerequisite / materials / world condition | Player actions: obtain, build, activate, use | Unlock / visible success / next step | In-game guide section or JEI reference | Proposed quest/how-to, detection and reward | Evidence / coverage |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 0. Find/open guide | Required teaching step | Exact early requirement unknown | Open Controls, find Psi master keybind and open Encyclopaedia; verify key label/default | First instructional page visible | Encyclopaedia; exact page name unknown | Teach keybind path; detect first verified tutorial completion or use manual fallback; reward small spellcrafting supplies | Official Codex description only; partial |
| 1. Obtain first interface | Required | Recipe, materials and unlock gate unknown | Craft/obtain first programming UI and casting device according to exact JEI | Player can author and attempt a spell | Exact JEI category and guide tutorial unknown | Actionable recipe/build/use instructions pending item/recipe inspection | Unresearched |
| 2. Author first spell | Required | Tutorial prerequisites and piece availability unknown | Place verified starter pieces, configure parameters and connect action sequence; run compiler | Valid spell definition and visible compiler success | First verified tutorial page and UI labels unknown | Teach one minimal safe utility spell; item possession does not prove it compiled | Official concept only; partial |
| 3. Cast and diagnose | Required | First valid spell and verified casting instrument | Load/store the spell, select it and cast using exact 1.21.1 interaction | Observable effect and cost/failure feedback | Casting tutorial/JEI reference unknown | Test actual operation; quest detector must be source-confirmed, otherwise manual completion | Unresearched |
| 4. Learn/unlock pieces | Required progression branch | Level count, XP source, threshold and unlock ordering unknown | Follow each tutorial and earn each verified unlock | New spell pieces become available | Exact Codex level/category names unknown | Map each level to a how-to stage and safe reward; do not award locked pieces | Official overview confirms leveling/tutorial concept only |
| 5. Upgrade components and casting modes | Major branch candidate | Current CAD/component/item tiers unverified | Inventory and teach all actual component, bullet and equipment routes | Increased spell options/capacity or new casting forms if present | Exact guide and JEI pages unknown | Split branches only after registry/guide review; no unverified mechanics in quests | Older source material excluded as version proof |
| 6. Share, copy and advanced use | Optional / terminal candidate | Official description mentions share/copy; exact version tools and completion target unknown | Use verified in-game sharing tool; test import and missing piece handling | Repeatable spell transfer and reliable advanced library | Exact guide page and UI action unknown | Teach practical import/export and safety/version checks if confirmed | Current Modrinth description; partial |
| 7. Completion | Open-ended endpoint not established | No documented final tier/objective found | Define completion only after full level/tutorial and branch inventory | Exact terminal capability unknown | Unknown | Avoid claiming mastery or quest completion until progression graph is mapped | Blocked by source/artifact inspection |

## Internal combinations and overlooked mechanics

| Internal combination | Full chain | Benefit | Limitation / exploit potential | Evidence |
| --- | --- | --- | --- | --- |
| Tutorial-driven spell development | Follow guide concept → construct spell → validate → unlock/use further pieces | Learning is integrated with progressive spell construction rather than a detached manual | Exact unlock events and level rewards not verified | Official project overview; source tag metadata |
| User spell library | Create/copy/share spell definitions → test in a controlled environment → reuse verified spell | Reduces repeated authoring and supports collaborative spell knowledge | Imported spells may be version- or addon-dependent; exact safeguards unverified | Modrinth project description; exact in-game tools need review |

## Low-hanging setups useful in ordinary survival

Three grounded early setups cannot yet be named without guessing at pinned recipes, unlocks and outputs.

### 1. First verified tutorial spell — early candidate
- **Goal and payoff:** Demonstrate the core author-program-test loop.
- **Minimum entry and ingredients:** Exact tutorial and item prerequisites are not yet established.
- **Build/use steps:** Open the in-game Encyclopaedia, complete its first tutorial, and reproduce the example only after confirming exact 1.21.1 steps.
- **Sustainability and limits:** Unknown until the cast resource and costs are inspected.
- **Evidence / pack test:** Official overview confirms in-game tutorials; no client test run.

### 2. Utility spell — candidate after first tutorial
- **Goal and payoff:** Identify one safe, repeatable survival utility action.
- **Minimum entry and ingredients:** Piece unlocks and resource costs unknown.
- **Build/use steps:** Select a source-backed utility effect from the pinned guide, program it, validate, cast, then test its boundary case.
- **Sustainability and limits:** Unknown; test costs and grief protection.
- **Evidence / pack test:** Not established; research before recommending.

### 3. Shared spell workflow — candidate
- **Goal and payoff:** Reuse or exchange a verified spell definition.
- **Minimum entry and ingredients:** In-game share/copy tool and import procedure need verification.
- **Build/use steps:** Produce a simple spell, export/copy via the exact tool, import into a disposable test, confirm missing-addon/version feedback.
- **Sustainability and limits:** Spell data transfer is not a resource generator; compatibility constraints unknown.
- **Evidence / pack test:** Current Modrinth description; not tested.

## API, integration and pack-config surfaces

| Hook or customization surface | Namespace / exact file, API or serializer | Capabilities / constraints | Current TMM override? | Evidence |
| --- | --- | --- | --- | --- |
| Mod API | Official FAQ confirms an API exists; exact 1.21.1 packages/events not inventoried | Addon authors can extend Psi; exact public interfaces and compatibility guarantees unreviewed | None found in inspected pack files for this pass | [docs: official FAQ] |
| Config | No exact 1.21.1 config keys inspected | Do not infer configurable costs, unlocks, or piece behavior | Unknown | Needs source/JAR review |
| Spell sharing | Official project description documents in-game share/copy support | Exact serializer/version validation and exposed API are unknown | None identified | [docs: Modrinth project page] |
| Recipes and tags | Not inspected in exact jar | No substitution or tag-compat claim | No Psi override identified in the paths inspected | Research pending |

## Compatibility leads

| Other mod | Candidate link and why | Native / pack-built / would require bridge | Status / source |
| --- | --- | --- | --- |
| Psi: Tweaks and Additions | Enabled addon may supply additional spell pieces or changes; exact content is unreviewed | Native addon relationship; individual features unknown | Candidate. Maintain separate [addon profile](psi-tweaks.md). |
| Ars Nouveau, Iron's Spells and other spell systems | Could offer distinct or complementary spell capabilities; no shared resource, registry or bridge is established | Unknown; likely needs deliberate bridge for direct interchange | Hypothesis only; inspect both sides before proposing an interaction |
| FTB Quests | Spell/tutorial milestones might expose advancement or operation detectors | Unknown; item possession is not proof of successful casting | Needs advancement/event/API inspection |

## Quest and teaching opportunities (research, not actual quests)

| Stage and teaching goal | Actionable in-game instructions (prerequisites → steps → result) | Named guidebook section or JEI recipe | Intended behavior vs truthful FTB detector / fallback | Useful XP + fixed reward direction | Coverage and gating risk |
| --- | --- | --- | --- | --- | --- |
| First guide access | Controls → locate Psi master keybind → open Encyclopaedia → identify the first tutorial | Encyclopaedia; exact page unknown | Reading/access task not known to be detectable; use first verified tutorial advancement or manual fallback | Small themed materials only after exact safe items are known | Exact access and trigger need client/source verification |
| First spell interface | Craft/obtain and open exact first interface; follow its recipe | JEI recipe category unknown | Item task is only an interface proxy; operation should be tested separately | Programming materials after costs are confirmed | Mandatory path currently unknown |
| Author and cast | Assemble a minimal verified spell, compile, store, select and cast; inspect visible success and cost | Exact tutorial title unknown | Require actual cast event if exposed; otherwise truthful item proxy plus manual evidence | XP + low-value crafting support | Do not reward spell bullets/items as if they prove a successful effect |
| Unlock progression | Complete each tutorial's explicit action and verify the next unlocked piece | Exact level/tutorial names unknown | Source-confirmed advancement only; manual fallback for UI-only progress | Progressive fixed rewards that do not bypass recipe gates | Full chain required before quest authoring |
| Advanced branches and sharing | Teach component upgrades, casting forms and safe import/export after exact inventory | Exact guide pages unknown | Test operation and separately test detector | Branch-appropriate reward direction pending item audit | Optional vs required branch not yet known |

## Existing chapter / pilot reconciliation

| Walkthrough stage | Current quest ID/title and status | Intended action vs actual detector | Named in-game guide/JEI reference | Gap / next action |
| --- | --- | --- | --- | --- |
| All stages | No Psi chapter or pilot inspected in this research pass | Not reviewed | Not reviewed | Search canonical quest authoring/generated data before any future quest handoff |

## Conflicts, limitations and version drift

- The pinned artifact is 1.21.1-110. The upstream release tag `release-1.21.1-110` declares mod version 110 in Gradle metadata, but the exact tag commit SHA was not retrieved and the artifact was not hash-inspected. [pin] [source: release tag metadata]
- Source Gradle metadata says Patchouli 1.21.1-92; the pack pins Patchouli 1.21.1-93. The upstream README and Modrinth page say Patchouli is required. This is a minor dependency-version skew, not a verified incompatibility. [source: release tag README and Gradle metadata] [pin]
- The upstream source metadata advertises NeoForge 21.1.207 or newer; the pack target is NeoForge 21.1.265. This is within the declared range, but compatibility was not tested. [source: release tag metadata] [pin]
- The official changelog's detailed public entries are mostly old Minecraft-version series. Those entries are not used as proof of 1.21.1 recipes or mechanics.
- The Codex page retrieval returned a server error. This is an information-access gap, not evidence that the in-game guide is missing.
- Psi is licensed under the Psi License; licensing/distribution compatibility should be reviewed before copying or redistributing guide content. TMM is only linking and summarizing here.

## Evidence ledger

| Claim / section | Source URL or exact inspected `file:path` + immutable SHA | Evidence tag / version of source | Pack version gap / what it proves / remaining limits |
| --- | --- | --- | --- |
| Pack pin | `mods/psi.pw.toml` and `manifest/mods.json` in `test/full-integration` | [pin] version ID `j9TFdTKC`, SHA512 recorded above | Exact artifact identity, not artifact inspection |
| Release metadata | [Upstream `gradle.properties`](https://github.com/VazkiiMods/Psi/blob/release-1.21.1-110/gradle.properties) | [source: tag `release-1.21.1-110`] | Declares MC 1.21.1, NeoForge baseline and mod version 110; tag SHA/JAR equivalence not established |
| Project gameplay overview | [Official Psi site](https://psi.vazkii.net/) | [docs: current official overview] | Confirms spell/action sequence, math/vector emphasis, tutorial/leveling concept; not complete recipe/progression guide |
| In-game guide access | [Official Codex page](https://psi.vazkii.net/codex.php) | [docs: current official site] | Search result states web Codex mirrors in-game Encyclopaedia opened by master keybind; page could not be fetched to confirm exact text |
| Dependency and sharing description | [Official Modrinth project page](https://modrinth.com/project/pOeA0exL) | [docs: current project description] | States Patchouli required; describes spell sharing/copying; not a pinned artifact audit |
| API availability | [Official FAQ](https://psi.vazkii.net/faq.php) | [docs: current official FAQ] | Confirms API exists; exact package/event surfaces not established |

## Disposable-world test plan

| Test | Positive/boundary case and disposable-world setup | Expected observable result (actual mechanic AND quest credit separately) | Performed? / evidence |
| --- | --- | --- | --- |
| Guide access and first progression | Fresh survival world; open Controls, invoke Psi master keybind before/after first Psi item; complete first tutorial and inspect next unlock | Determine initial access gate, exact page names and level/advancement changes; separately record whether FTB can detect the actual tutorial action | Not run |
| Spell compile and cast | Compile and cast one guide-provided starter spell, then repeat with one intentionally invalid or unavailable piece | Valid spell produces documented effect and cost; invalid spell reports error without unverified item/resource loss; quest credit recorded independently | Not run |
| Server-side behavior | Use the same valid spell on a dedicated server with a normal target and an invalid/out-of-range target | Server-authoritative result, resource consumption, range and failure behavior; compare quest event/detector separately | Not run |
| Addon boundary | Compare base Psi and enabled Psi: Tweaks and Additions in disposable installs; test one addon-only component and import behavior without addon | Confirm which content belongs to base vs addon, compatibility errors and whether spell fails safely | Not run |
| Sharing/import | Export a simple spell, import into same version, then try an unavailable-piece/version case | Confirm actual share format, validation/warning and whether imported data grants progression (not assumed) | Not run |

**Status requirement:** keep `researching`. The progression graph, first recipe chain, guide page inventory, unlock triggers and casting resource/cost behavior are still mandatory unknowns. No gameplay tests were run.

## Unknowns and next research

- [ ] Retrieve exact 1.21.1 Codex/tutorial content and enumerate all sections, prerequisites and progression levels.
- [ ] Inspect the pinned JAR transiently: metadata, registries, recipes, advancement tree, config defaults, loot/data and guide JSON; do not commit the JAR or extracted copyrighted assets.
- [ ] Resolve the upstream release tag to an immutable commit SHA and compare its resource/metadata behavior to the pinned file where possible.
- [ ] Map first materials, spell interface, casting instruments, all item/component tiers and resource/durability/cooldown costs.
- [ ] Inventory the exact enabled Psi: Tweaks and Additions pin and profile it separately.
- [ ] Search quest authoring and generated files for any existing Psi pilot before a quest handoff.
- [ ] Perform clean client and dedicated-server disposable-world tests only when the user requests implementation verification.

## Revision notes

- 2026-11-16 — Created a source-aligned, explicitly partial profile from the exact pack pin, official release metadata, project overview, FAQ and available docs. No quest, runtime or configuration files changed.
