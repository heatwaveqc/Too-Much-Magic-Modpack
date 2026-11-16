# HexCast NEOFORGED — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | hexcast-neoforged / hexcasting (actual namespace not verified in the pinned JAR; mod project is named HexCast NEOFORGED) |
| Category / parent or addons | magic; unofficial NeoForge port of Hex Casting, by a separate maintainer from the original Hex Casting team |
| MC / loader / pack target | 1.21.1 / NeoForge / TMM test/full-integration |
| Pinned version / artifact / hash | mods/hexcast-neoforged.pw.toml; hexcast-neoforged-1.21.1-0.11.1-19+neoforge.1.21.1.jar; Modrinth project lZGsiJ1L, version 3qkLQwpf; SHA512 1b57619c9298d4ee7f77db44d0d860bcc7e0f48939a9a57657b6ae7ed386fcc14e4412d5358145cd58e3a85bf0f30c46ccde954a86cea53c6293534e81d2a889 [pin] |
| Source snapshot | Port source: a1exrom/hex_cast_NEOFORGED, identified from project page. No immutable commit tied to 0.11.1-19 located yet. Original mechanics source/docs: FallingColors/HexMod Hex Book, but it explicitly does not cover this port binary. |
| Guide/documentation version gap | Port page calls this an unofficial, independently maintained 1.21.1 NeoForge port; its listing advertises in-game Patchouli documentation. Original online Hex Book describes the related 0.11 line but cannot be assumed identical. |
| Profile status | researching |
| Evidence reached | exact pack pin; official Modrinth project documentation and linked port repo README; original Hex Book read for concepts only; no artifact/source comparison or client test |
| Reviewed | 2026-11-16 |
| Quest ownership | Proposed Magic chapter: HexCast NEOFORGED. No quest files changed. |

## Executive gameplay map

**What the player actually does:** Hexcasting is programmable magic: the player uses a staff to open a hexagonal drawing grid, draws ordered patterns, and those patterns execute actions that inspect the world, manipulate typed values, or cause magical effects. The spell program manipulates a stack of values called iotas. A casting requires a valid sequence, appropriate inputs, range and sufficient Media. HexCast NEOFORGED is an unofficial NeoForge port of the original Hex Casting mod. Its project page advertises 150+ actions, a Patchouli book, circle casting, staffs, lenses, trinkets, brainsweeping and quenched allays. The original documentation describes amethyst-based Media, pigments, scrolls/cyphers and structured spell programming, but every recipe and exact behavior must be checked against the port's own 1.21.1 book and JAR.

**Why it matters in TMM:** This is the pack's explicit programmable spell system, distinct from Ars glyph composition and Iron's learned spellbooks. Its unique player skill is encoding a repeatable operation as a drawn or stored sequence. The advertised circle-casting route may support larger, staged magic, but its operation and cost are not yet mapped. No cross-system mana or FE bridge is assumed.

**Quick start (conceptual only until port-specific book and recipes are checked):**
1. Obtain and open the port's Patchouli book; learn how the port teaches patterns and safety/mishap rules.
2. Craft the first casting implement according to its JEI/book recipe and open the grid by using it.
3. Practice a harmless information/stack operation with a short Hex, then cast a simple valid effect.
4. Learn the port's Media sources and cost display; avoid casting without sufficient reserve.
5. Progress into pigment, stored Hex items, lenses/trinkets, circle casting and advanced action families only when the book exposes those stages.

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Real-time programmable spell drawing; stack-based action composition; stored/reusable casting in cyphers or scroll-like items if retained by the port; circle casting; Curio lenses/trinkets and custom effects | A reusable staff-based way to test short spells; book-driven learning and debugging; a configurable/personalized spell rather than a fixed spell list. Exact material costs and earliest recipes are not established. | **Confirmed project-page compatibility claim:** Create and Farmer's Delight; both are installed in TMM, but exact compat needs inspection. **Candidate:** Ars Nouveau and Iron's may provide comparative spell-system teaching, but no direct resource link is established. **Candidate:** Curios enables optional wearable lenses if the exact API slot behavior is present. |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Guidebook and discovery | Port listing claims Patchouli book with full documentation | partial | book page names not yet retrieved | not run | verify acquisition/open action, section names and hidden unlocks |
| Pattern drawing / casting grid | Port listing and original Hex Book describe use-staff open grid and draw patterns | partial | “Hex Casting 101” in original docs; port section unknown | not run | verify controls, grid size, pattern stroke directions, client input |
| Actions, iotas, stack and mishaps | original online book, not port code | partial | original guide sections; port guide unknown | not run | test every action family, input/output types, mishap costs and stack limit |
| Media economy and casting cost | original Hex Book covers amethyst Media; port exact values unknown | partial | port Media section unknown | not run | inspect exact sources, worth, rounding, exhaustion and reload |
| Pigments and customization | original Hex Book says pigments change casting/sentinel visuals | partial | exact port section unknown | not run | verify recipes, item consumption, persistence and effects |
| Casting implements / Cyphers / stored Hexes | port advertises staffs, lenses and trinkets; original docs describe cyphers and storage | partial | port book and JEI names unknown | not run | inspect recipes, storage/trigger semantics, media reserve and durability |
| Circles / advanced rituals | advertised on port page | unresearched | exact structure or circle pages unknown | not run | source and guidebook required; map geometry, participants, costs and outputs |
| Curios lenses / trinkets | optional compatibility claim on port listing | partial | Curios slot documentation absent | not run | confirm dependency, item slots and effect rules |
| Special features / entities | brainsweeping and quenched allays advertised | unresearched | not mapped | not run | establish acquisition, entity changes, persistence and failure outcomes |
| Integrations, config and API | port listing claims Create/Farmer's Delight compatibility; linked repo says PAUCAL/Patchouli/Kotlin/Caelus, optional Curios/JEI | partial | n/a | not run | inspect exact source commit, recipes, required deps, configs and live pack overrides |

## Guidebook onboarding

- **Guide name and access:** The port's project page says its in-game Patchouli book provides full documentation. Physical item name, recipe/drop, first advancement and opening route are not yet confirmed. Do not substitute the original mod's book recipe.
- **Earliest attainable stage:** unknown until the port's exact book recipe/advancement data is inspected.
- **First instructional quest:** “Open the HexCast Notebook.” Teach how to acquire/open the correct book and navigate the first casting lesson. A task that detects possession is only a proxy for reading.
- **Reference map:** Original Hex Book includes “Hex Casting 101,” “A Primer On Vectors,” “Mishaps,” and sections by action families. The port may rename, omit or alter them; confirm before quest text uses these names.
- **Where the guide is insufficient:** the mod page claims full documentation, but no port-specific guide pages have been checked against the pinned artifact. We have not established first spell, casting controls, Media gathering, circle setup or port-specific progression gates.

## Systems and full mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and source |
| --- | --- | --- | --- | --- |
| Drawn Hex casting | use a staff/casting implement to open grid; draw ordered glyph patterns | client pointer/grid input, spell stack and Media | actions query world, manipulate values or cause effects | project claims live pattern drawing; exact control, grid size, invalid patterns and range from port are unverified |
| Iotas and stack programming | cast ordered actions | typed values (iotas); exact literal/introspection patterns and stack cap unknown for port | values are pushed/transformed/consumed by actions | original Hex Book teaches a typed stack model; use as a concept only pending port parity |
| Media | action costs charged during casts | project page does not identify exact port sources/units; original version uses amethyst-derived Media | spell energy reserve depleted by actions | exact conversions, item value, round-up, inventory source, external media source and failure cost unknown |
| Mishaps | invalid inputs, bad range, math or other failed actions per original documentation | potentially Media and/or held-item/state penalties | action failure, error indication and harmful effects may occur | original book reports distinct consequences; port may diverge. Must source/measure before quest warnings |
| Patterns/action library | unlock/use patterns in documentation | pattern order + required stack arguments | manipulation, sensing, entity/block/world effects | port advertises 150+ actions; exact list, version, categories and unlock mechanisms uninspected |
| Pigments | craft/apply pigment according to original Hex Book | pigment item and a pattern/application action; exact port recipe unknown | changes visual identity/effect color according to original description | may be cosmetic or alter other systems; consumption and persistence need port test |
| Stored Hexes | store a spell in an item such as a cypher or scroll, if port retains original path | valid spell, item, Media/capacity | reusable/copyable spellcasting item | original says ancient cyphers reveal stored program and deplete Media, but verify port IDs, recipe, storage overwrite and break rules |
| Lenses / Curios | equip lens/trinket (advertised) | item and Curios slot if required | may alter grid/casting or give custom effects; exact behavior unknown | project lists Curios optional for wearable lenses; no exact slot/item/stat established |
| Circle casting | port feature listing | arrangement, implements, participants and/or Media not yet established | advanced ritual-like spell execution | no assumption about shape, multiblock, targeting, persistence or automation |
| Brainsweeping | advertised feature | target, action and cost unknown | effects unknown | no mechanics claim beyond port description; investigate guide/source |
| Quenched Allays | advertised feature | entity state, process and item costs unknown | modified Allay behavior unknown | investigate exact method, permanence, ownership and outputs |
| Compat integrations | port page lists JEI/Curios/Create/Farmer's Delight | cross-mod recipes or capabilities unknown | unknown until inspected | project page claim is not proof in pack; no absent mods should be added |

## Material / capability input-output contracts

| System | Input kind + exact ID/tag/event | Input qty/formula; consumed/remainder | Output kind, qty and location | Trigger/cadence; range/capacity | Automation/interface and evidence |
| --- | --- | --- | --- | --- | --- |
| Drawn casting | staff/grid input + ordered pattern actions + current iota stack | action-level Media; exact per-action cost and item consumption unknown for port | spell effect, stack value, block/entity/world interaction, or mishap | player cast event; exact world range and player-reference exceptions unknown | requires client grid and server effect; no pack test |
| Media source | original Hex book associates amethyst forms with Media | original book gives approximate values for older release; do not apply to port without comparison | internal spell reserve consumed by actions | action execution; per-action timing | no standardized FE/fluid/Source/blood interface established |
| Stored spell | spell pattern + stored casting item | capacity, Media charge and wear unknown | triggered spell casting | use/right-click/trigger unknown | automation not established; verify whether item trigger can be redstone/automation driven |
| Circle casting | spell/action sequence + circle blocks/participants unknown | inputs/costs unknown | advanced spell/ritual outcome unknown | event and range unknown | no automation claim |
| Pigment | pigment item + cast/apply operation | item consumption/return unknown | cosmetic/state change unknown in port | player event | original docs only; exact port unavailable |
| FE, mana, Source, blood, XP | no verified shared-resource bridge | unknown | unknown | unknown | do not infer from Media or color effects |

### Acquisition, renewability and output economics

- Casting uses a distinct Media economy. Do not equate it to FE, Ars Source, Malum spirits, blood or vanilla XP.
- The original Hex Book explains an important action-order concern: an action may spend Media as it executes rather than only after the entire Hex succeeds. It also describes mishap consequences. These are not yet proven for the NeoForge port; preserve as test questions rather than player facts.
- Media source items may be consumed in whole-item increments even where an action needs less than their value in the original release. Exact port rounding/consumption could materially affect spell cost and must be checked.
- Stored spells and lenses are potential quality-of-life / automation features, but no trigger or recharge path is established for this pinned version.
- Circles and altered entities may create persistent effects; exact costs, owner state, cleanup and exploit boundaries need source inspection.

## Complete player progression / walkthrough blueprint

This is a mechanics outline, not a quest-authoring handoff. Because the pinned unofficial port has not been inspected at the artifact level, the original Hex Casting book is not treated as proof of port progression or recipe details.

| Path and step | Gate or choice? | Exact prerequisite / materials / world condition | Player actions: obtain, build, activate, use | Unlock / visible success / next step | In-game guide section or JEI reference | Proposed quest/how-to, detection and reward | Evidence / coverage |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 0. Obtain/open port guide | required teaching | item/advancement/recipe unknown | acquire and open Patchouli book | access to port-specific docs | exact section names not mapped | explicit book onboarding; item detection proxies opening; XP + safe starter material | port page claims guidebook; acquisition blocker |
| 1. First casting implement | required | staff recipe and Media source unknown | craft implement, use it to open grid | casting interface | first casting page unknown | teach controls and close without cast; detect staff item only as proxy; XP + harmless pigment/resource | port-specific recipe uninspected |
| 2. Draw first safe Hex | required | short valid pattern and required iotas | draw action sequence; observe stack/effect | valid cast and action feedback | port pattern introduction | teach exact stroke order and safe target; detector for real cast needed; XP + modest Media supply | original docs only |
| 3. Understand stack and arguments | required learning milestone | iota types and action inputs | inspect inputs/outputs; compose a basic query/manipulation | reliable simple programs | port guide sections for actions/types | teach one harmless composition; completion detector likely manual proxy; XP + guide-related supplies | port book missing |
| 4. Media management | required for sustained use | exact media source and reserve | acquire/store Media; cast with cost visible | repeatable casts | port media section and JEI | record actual source, quantity and result; XP + safe Media item | exact economics missing |
| 5. Mishaps and safe boundaries | required safety/how-to | cast a known invalid/out-of-range hex only if low risk | explain signal and safe recovery; avoid dangerous test on survival world | fail safely and read error | port mishap pages | manual demonstration or disposable world; XP + non-bypass utility | exact failure costs missing |
| 6. Pigments and personalization | optional branch | pigment recipes and application trigger unknown | craft/apply chosen pigment | visual customization or other effect | pigment section/JEI | optional branch; item proxy; XP + safe cosmetic materials | original docs only |
| 7. Store/reuse Hex | optional productivity branch | item, recipe and capacity unknown | create stored spell and execute it; inspect Media/wear | reusable casting capability | cypher/scroll pages and JEI | task must demonstrate execution, not item possession; XP + storage item/material | port feature parity unverified |
| 8. Lens/trinket branch | optional | exact equipment and Curios requirements unknown | equip and compare casting UI/effect | additional casting affordance | Curios item pages/JEI | teach slot and benefit; item detector as proxy; XP + safe component | listing claim only |
| 9. Circle casting | advanced gate/branch | geometry, blocks and inputs unknown | build according to book, perform supported sequence, observe result | larger/advanced casting | circle casting guide and JEI | must include explicit pattern/geometry, real operation detector, XP + safe construction supplies | mandatory setup blocker |
| 10. Brainsweeping / quenched Allay | optional special content | target and acquisition unknown | perform operation and document resulting entity | special entity/system branch | port book pages unknown | optional quests after exact risks, state and reversibility known | feature blurb only |
| 11. Advanced action families / capstone | open-ended until pinned book analyzed | action progression, spell unlocks and final objective unknown | teach a chosen useful program; define actual success | no terminal milestone currently established | exact action categories and final page unknown | only add capstone after exact unlock tree is mapped; XP + useful non-gated reward | incomplete |

### Dependency graph and alternate routes

**Patchouli onboarding → casting implement → draw patterns → typed stack/actions + Media → safe repeatable Hexes → optional stored spells/lenses → advanced circle casting and special features.**

This is a framework inferred from the port's public description, not an established native unlock chain. Keep optional branches optional; the Advancement/guidebook predicate graph must be inspected before chapter dependencies are written. No TMM gate or recipe change has been made.

## Internal synergies and low-hanging setups

- A short read-only or local-query pattern is the safest first lesson: it teaches the grid and stack without granting a powerful world effect. Choose the exact action only after verifying port docs.
- A stored spell, if present in the port, could reduce repeated drawing and act as a player-created utility tool. Do not call it automation until a redstone/block or entity trigger is confirmed.
- Curios lenses/trinkets and pattern grid changes may support a caster build, but exact attributes/effects need item data.
- Create and Farmer's Delight are named on the port page as compatible. Audit each actual recipe/interaction and avoid suggesting those mods as a prerequisite.
- The port says it was built for a private server and is independently maintained; record version pin and report all conclusions as specific to HexCast NEOFORGED, not original Hex Casting generally.

## Configuration, APIs and current pack behavior

- Pack pin is mods/hexcast-neoforged.pw.toml. Modrinth project page states NeoForge 1.21.1 and client/server.
- Port README lists required PAUCAL, Patchouli, Kotlin for Forge and Caelus Elytra API; Curios and JEI are optional there. The exact pack pin/manifest for each dependency must be reconciled before a dependency-complete claim.
- The port page advertises 150+ actions, circle casting, staff/lens/trinket support, brainsweeping, quenched Allays, and Create/Farmer's Delight compatibility. These are feature claims, not a mechanics audit.
- Original Hex Casting online documentation is useful to identify concepts but cannot substitute for this unofficial port's source, recipes, guidebook and runtime.
- Exact source ref, config defaults, action registry, spell recipe serializers, API, advancements, spell-circle rules and pack overrides are not yet inspected. No compatibility or balance assertion is made.

## Progression-to-quest coverage matrix

| Stage | Teaching objective and action | Guide/JEI reference | Authentic completion / FTB detector | Reward direction | Coverage |
| --- | --- | --- | --- | --- | --- |
| Guide | get/open Patchouli book; use its index | exact book section missing | book item proxies open | XP + safe starting materials | partial |
| Implement | make staff; open and exit grid | JEI and first-cast page unknown | staff possession is proxy | XP + safe media source | partial |
| First cast | draw a known simple pattern and observe result | port-specific stroke page missing | actual cast event needed; item task insufficient | XP + low-power spell utility | partial |
| Stack learning | compose typed input/output sequence | action pages unknown | manual state/output test may be needed | XP + practical materials | partial |
| Media | source and spend Media; record reserve | Media entry/JEI unknown | before/after resource state required | XP + modest replenishment | partial |
| Mishaps | understand error feedback and failure cost | mishap pages unknown | manual/disposable-world test | XP + no high-value gear | partial |
| Stored spell | create and successfully trigger stored Hex | stored-item page unknown | trigger/action detector required | XP + storage/casting item supplies | partial |
| Circle | build and operate circle casting | exact pattern/geometry unknown | operation trigger unknown | XP + structural ingredients | partial |
| Special branches | learn Curio, brainsweeping or allay path if desired | exact book refs missing | item/entity state or manual fallback | XP + safe branch-specific reward | partial |

**Reward policy:** award positive XP and useful fixed items. Do not reward a powerful spell item without teaching how to obtain its media and how mishaps work. Possession is a proxy unless the task verifies the actual cast, stored-spell trigger or circle operation. No quests have been edited.

## Open questions and next actions

1. Inspect the exact pinned JAR transiently and compare SHA512; verify modid, metadata, recipes, Patchouli entries, advancements, actions, loot, config and Curios slots.
2. Locate the immutable port source commit for release 0.11.1-19 and compare against the actual binary. Original HexMod source is not a substitute.
3. Map book acquisition/opening, first implement and actual start-to-end unlock progression.
4. Inventory all pattern/action families and arguments, stack semantics, media cost formula, range, mishap cost/order, target immunity and side effects.
5. Map all stored casting items, media sources and reserves, pigments, lenses/trinkets, circle casting and special entity branches.
6. Confirm dependency pins and inspect actual Create/Farmer's Delight compat recipes.
7. Determine which FTB task predicates can verify casts, spell triggers and circle operations. Item possession alone is inadequate.
8. Only after artifact mapping, design positive and boundary client/world tests against this exact pack.

## Test plan — not run

No tests were run; this is knowledge Markdown only.

- **Positive:** cast one harmless pattern that pushes a known value and another that consumes it; record grid controls, stack result and Media change. **Boundary:** wrong iota type and incomplete pattern; record the exact mishap, media loss and held-item/state penalties.
- **Positive:** cast one safe world interaction at a target inside the stated range. **Boundary:** repeat just outside range and against an immune target; record effects, item loss, spell interruption and any cooldown.
- **Positive:** use a stored spell and record charge/wear/recharge. **Boundary:** trigger when empty and with invalid contents.
- **Positive:** construct and perform one documented circle cast. **Boundary:** omit one component/participant or use malformed pattern; record all resource loss and state cleanup.
- **Positive:** use one lens/Curio and one pigment; record slot, effect and persistence. **Boundary:** remove/swap during casting or test without Curios if optional.
- Verify client guidebook, input mapping, JEI and server-side cast synchronization on an exact disposable test pack.

## Evidence ledger

- Pack pin mods/hexcast-neoforged.pw.toml: exact artifact, Modrinth project/version and SHA512 [pin].
- [HexCast NEOFORGED Modrinth page](https://modrinth.com/mod/hexcast-neoforged): identifies an unofficial independently maintained NeoForge 1.21.1 port, supported client/server; claims book, 150+ actions, circles, staffs/lenses/trinkets, special features and compatibility. Lists required and optional dependencies.
- [Port source repository](https://github.com/a1exrom/hex_cast_NEOFORGED): README confirms unofficial port, maintainer, dependency list and MIT project license; exact release commit not identified.
- [Original Hex Casting online Hex Book](https://hexcasting.hexxy.media/v/latest/main/en_us/): describes drawing grid, action stack, Media, range, mishaps, pigments and stored casting concepts. It is not evidence that the port has identical behavior.
- Pack runtime integration, pinned JAR contents, action outputs, source equivalence and client behavior: not verified.
