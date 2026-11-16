# Psi — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | psi / `psi` |
| Category / parent or addons | Magic; spell-programming system. Installed addon: [Psi: Tweaks and Additions](psi-tweaks.md) (separate profile). |
| MC / loader / pack target | Minecraft 1.21.1 / NeoForge / TMM `test/full-integration` |
| Pinned version / artifact / hash | `mods/psi.pw.toml`; `Psi-neoforge-1.21.1-110.jar`; Modrinth project `pOeA0exL`, version `j9TFdTKC`; SHA512 `2bbe6b40f230cd495a5342db61fb5a6c2a4d03fda0ee5fe0a21d8b7ccc8494d1d3e7fa5f54236d5f90cbe78a8bb75c27e3bb9cd9b85e4b6eed67aa976ffe857a` [pin] |
| Source snapshot | Official `VazkiiMods/Psi` tag `release-1.21.1-110`; its `gradle.properties` declares MC 1.21.1, NeoForge 21.1.207, mod ID `psi`, version 110. The tag's content files were inspected directly; peeled tag commit SHA and pinned-JAR equivalence remain unverified. |
| Guide/documentation version gap | The exact 1.21.1 tag contains an Encyclopaedia Psionica Patchouli book, with Basics, Components, Items and Legacy Entries categories. It labels the book a work in progress and places six tutorials under Legacy Entries. Old localization keys still describe a leveling menu; the current source's default 29 spell-piece groups are not enforced unlock gates, so do not treat that older text as current progression behavior. |
| Profile status | researching |
| Evidence reached | Exact packwiz pin; matching upstream tag metadata; exact-tag Patchouli page definitions/localized guide content, default spell-piece group registry, CAD recipe/data, cast/resource source; no direct JAR inspection or client test |
| Reviewed | 2026-11-16 |
| Quest ownership | No Psi quest chapter reconciled in this pass. No quest files changed. |

## Executive gameplay map

**What the player actually does:** Psi is a spell-programming system built around CADs, spell bullets, and a grid-based Programmer. The 1.21.1 guide gives a concrete opening: craft a CAD Assembler and Iron CAD Assembly, assemble the starter CAD, then use it on dropped Redstone Dust to create Psidust. The Encyclopaedia teaches authoring and casting through six tutorials, with a broader catalog of components, bullets, tools, armor and spell pieces. In the source tag, default spell-piece groups organize and announce learning events, but do not lock their pieces behind a leveling tree.

**Why it matters in TMM:** Psi gives players a programmable action system with explicit spell statistics, resource costs, and CAD constraints. The installed Psi: Tweaks and Additions addon has a separate profile; its actions, extra progression and optional Mekanism-dependent content should not be attributed to base Psi.

**Quick start (source-mapped, client acceptance still open):**
1. Open the Encyclopaedia Psionica using the Psi master keybind; its introduction directs the player to a CAD Assembler and Iron CAD Assembly.
2. Craft the CAD Assembler (5 iron ingots + 1 piston) and Iron CAD Assembly (4 iron ingots), then assemble the basic CAD in the machine.
3. Drop Redstone Dust on the ground and use the bare CAD while aiming at the item to craft Psidust. The source action spends 100 Psi and applies a 60-tick regeneration cooldown.
4. Craft a Spell Programmer (5 iron ingots + 1 Psidust) and a basic Spell Bullet (1 iron ingot + 1 Psidust). Follow the guide's six Legacy Entries tutorials to write, compile, transfer, select and cast the example spells.
5. Use JEI for recipes: the guide explicitly presents recipes for the CAD Assembler and CAD components, but says it is not a complete recipe book. Source-to-JAR parity and live client labels remain untested.

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Authorable spells assembled from typed pieces; five CAD spell statistics and resource-efficiency tuning; multiple bullet execution forms; block/entity/movement/effect/smelting spell tricks; Psimetal tools and exosuit pieces that trigger spells from actions or sensors. | Bare CAD crafts Psidust from dropped Redstone Dust; the six tutorial entries teach the compile-and-cast loop; CAD components and bullets expand spell capability. | Psi: Tweaks and Additions is a separate installed addon; spell-group completion events could support scripted progression if a listener is confirmed; Psi spell actions may complement other schools (no resource or registry bridge claimed). |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Encyclopaedia and tutorials | Exact-tag Patchouli assets and localization; six legacy tutorial entries | Starter tutorial sequence mapped; later reference pages not fully reviewed | Encyclopaedia Psionica; Basics, Components, Items, Legacy Entries | Not run | Confirm the book/keybind display and controls label in the pack client |
| Starter CAD and Psidust | Exact-tag shaped recipes and ItemCAD action | First material/crafting loop mapped | Introduction and CAD Assembler entries; JEI recipes | Not run | Verify assembler GUI and dropped-item targeting in client |
| Spell Programmer and bullets | Exact-tag recipes and tutorial text | Compile/store/cast flow mapped; all component/bullet variants open | Tutorial 1–2, Programmer entry, Bullet entry | Not run | Audit all bullet forms and component tiers/costs |
| Default piece groups | 29 registered groups; group docs define unlock semantics | Group categories mapped; no default enforceable level tree | Four tutorial groups plus 25 subject groups | Not run | Confirm no pack datapack overrides; trace event listeners only if integration is proposed |
| CAD energy, spell stats and cast limits | Source for default attributes, cost calculation, overflow path; guide for stats and 32-block boundary | Baseline and general boundaries mapped | Tutorial 3–5 | Not run | Audit component stat formulas, battery storage and per-trick limits |
| Psi: Tweaks and Additions | Separate installed addon profile | Separate research in progress | Addon content is in the base Encyclopaedia per upstream docs | Not run | Continue addon profile independently |

## Guidebook onboarding — required before the player's first complex step

- **Guide name and access:** The exact-tag assets define the Patchouli book Encyclopaedia Psionica; its current master keybind is named Psi Master Keybind in localization. The introduction and CAD Assembler pages are present. Client display and the actual key assignment remain untested.
- **Earliest attainable stage:** The source guide's first route uses a CAD Assembler and Iron CAD Assembly made from iron plus a piston. No earlier mod-specific unlock is named in those pages; confirm the actual client start.
- **First instructional step:** Build the basic CAD, then use it on dropped Redstone Dust to obtain Psidust. The source action applies a 60-tick regen cooldown and deducts 100 Psi.
- **Reference map:** Basics contains the introduction, CAD Assembler and Programmer entries; Components and Items describe equipment; Legacy Entries holds six tutorials, from spell writing through an explosion example.
- **Where the guide is insufficient:** The book is marked work in progress and says JEI is needed for recipes. Component stat tiers, every bullet recipe, advanced acquisition routes and some trick limits remain outside this mapped starter slice.
- **Other in-game help:** The Programmer provides compile status, error details and five spell statistics. JEI should be used for exact recipe lookups; neither surface has been checked in a client.

## Systems and full mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and source |
| --- | --- | --- | --- | --- |
| Encyclopaedia Psionica | Patchouli book opened with Psi's master keybind | No item cost established | Basics, Components, Items and Legacy Entries tutorial material | Book labels itself a work in progress; exact guide content is in the version-aligned tag, not proof of client display. |
| CAD Assembler and components | Craft the machine and CAD components; insert an assembly and/or spell bullets | CAD Assembler uses 5 iron ingots + piston; Iron CAD Assembly uses 4 iron ingots; full CAD needs Core + Socket for casting | Builds a CAD and loads nonempty bullets into its sockets | CAD stats gate casting. Source-to-binary and client acceptance not verified. |
| Psidust | Use the bare CAD while targeting dropped Redstone Dust | 100 Psi; 60-tick regeneration cooldown | Crafts Psidust from the targeted dropped item | Source checks that the held CAD is the player's equipped CAD, avoids duplicate CAD use, and emits the fake-level tutorial event after first successful Psidust craft. |
| Spell Programmer | Craft, then right-click to program spells | 5 iron ingots + 1 Psidust | Typed piece grid; compile status and spell-stat readout | Invalid connections/parameters fail compilation; piece registry and full compiler limits remain to audit. |
| Spell Bullet and CAD | Put compiled spell into a bullet, then load the bullet into a CAD via the Assembler | Basic bullet: iron ingot + Psidust; casting cost derives from spell cost, CAD Efficiency and bullet modifier | Radial-select and cast through CAD; projectile/loopcast/circle/grenade/charge/mine bullets change execution form | Cost overflow consumes CAD stored Psi before damaging player; exact battery and cooldown edge cases need further source/client review. |
| Default spell-piece groups | 29 data-driven groups in source | Main-piece execution posts a completion event | Groups classify tutorials and spell-piece families | Default groups do not enforce a level gate; external/custom advancements can gate groups if correctly configured. |
| Spells / runtime | Cast with a valid CAD and spell container | Positive final cost deducted; zero final cost skips deduction | Server executes spell; client predicts cast | Needs Psi available, a non-overflowed player, valid spell and sufficient CAD stats. Guide states 32-block trick range from focus with fizzle on violation. |

### Verified 1.21.1 guide and starter progression

The exact-tag book assets identify the manual as Encyclopaedia Psionica. Its landing page says the book is a work in progress and points readers to the Legacy Entries section for the old tutorials. The tag contains six tutorial entries:

| Guide entry | Source-backed teaching step |
| --- | --- |
| Tutorial 1: Writing A Spell | Use the Programmer grid, connect Selector: Caster to Trick: Debug, configure the parameter, check the compile status, then place the compiled spell into a bullet. |
| Tutorial 2: Casting A Spell | Load the bullet into the CAD using the CAD Assembler, select the bullet with the master-keybind radial menu, and cast the Debug spell. |
| Tutorial 3: Types & Numbers | Teach typed spell parameters, numeric constants/operators and connectors. |
| Tutorial 4: Move It | Build an Add Motion spell using the caster, look vector and numeric strength. |
| Tutorial 5: Limits | Read the Programmer's five spell statistics and learn the focal-point range/fizzle boundary. |
| Tutorial 6: EXPLOSION | Combine a raycast with the Explode trick and use the Error Suppressor for intentional failure cases. |

The documented starter recipes are:
- CAD Assembler: a shaped recipe with five iron ingots and one piston.
- Iron CAD Assembly: four iron ingots; insert it in the CAD Assembler to produce the basic CAD.
- Spell Programmer: five iron ingots and one Psidust.
- Basic Spell Bullet: one iron ingot and one Psidust.
- Psidust: drop Redstone Dust and use the basic CAD while targeting it. Source calls for 100 Psi and a 60-tick regeneration cooldown.

The starter CAD is sufficient for the Psidust action but not general spell casting. The manual says a casting CAD needs a Core and Socket as well; a Battery increases maximum Psi and a Colorizer is cosmetic. The complete component recipes and tier stats are still open for audit. The guide also warns to carry only one CAD; multiple carried CADs interfere with casting.

### Spell statistics, energy and cast boundaries

The Programmer exposes Complexity, Potency, Cost, Projection and Bandwidth. CAD checks the spell's required stat values against the CAD's component-derived stats before casting; Efficiency modifies the Psi cost, and a spell-bullet container may apply an additional cost multiplier. Source defaults for player attributes are 5,000 maximum Psi and 25 Psi regeneration per tick, both modifiable through attributes. A cast can be refused if the spell is invalid, the CAD stats are insufficient, the player has no available Psi, or the player is already overflowed. The basic cast path posts a pre-cast event before charging; a positive final cost is deducted, while zero cost does not invoke that deduction.

When a deduction exceeds available Psi, the player's CAD may consume stored Psi first; remaining overflow is converted to damage and marks the player overflowed, preventing further casts until the overflow state clears. This boundary should be handled carefully in any future balancing or quest examples. The 1.21.1 guide states that ordinary spell tricks cannot affect targets beyond 32 blocks from the spell's focal point (except the caster); an out-of-range spell fizzles and stops remaining tricks.

### Default spell-piece groups and progression behavior

The 1.21.1 source registers 29 default groups, spanning four tutorials, numbers, vectors, entities, projectiles, block operations/conjuration/movement, tool casting, loopcasting, flow control, movement, elemental and positive/negative effects, exosuit casting, smelting, infusion, memory/list operations, detection and miscellaneous tricks. Groups are data-driven and may be customized by datapacks.

The default groups use the execute-main unlock behavior. Executing a group's main piece posts a PieceGroupAdvancementComplete event; Psi does not itself award the group advancement, and default pieces remain placeable/compilable/castable without that advancement. A custom group can use an advancement unlock to hide its pieces and reject compilation/import until the named advancement is complete. The client only receives advancements with a display, so a custom gate must point at a displayed advancement. This is a potential future integration surface, not evidence that TMM currently gates Psi or that FTB Quests listens for the event. [source: exact-tag docs/spell-pieces.md, PsiSpellPieceGroups.java, PlayerData.java]

## Material / capability input-output contracts

| System | Input kind + exact ID/tag/event | Input qty/formula; consumed/remainder | Output kind, qty and location | Trigger/cadence; range/capacity | Automation/interface and evidence |
| --- | --- | --- | --- | --- | --- |
| CAD Assembler | Iron ingots and piston; recipe output psi:cad_assembler | 5 iron ingots + 1 piston | One CAD Assembler | Shaped crafting recipe | Recipe JSON; exact-tag source |
| Iron CAD Assembly | Iron ingots; recipe output psi:cad_assembly_iron | 4 iron ingots | One Iron CAD Assembly | Shaped crafting recipe | Recipe JSON; exact-tag source |
| Starter CAD assembly | Iron Assembly inserted in CAD Assembler | One assembly | One basic CAD | GUI machine operation | Encyclopaedia intro; client operation not checked |
| Psidust creation | Equipped player CAD targets dropped Redstone Dust | Source charges 100 Psi and sets 60-tick cooldown; exact item-entity debit path not yet audited | Psidust from the CAD crafting interaction | Player right-click; aimed at dropped redstone | ItemCAD.use and source guide; no automation claim |
| Spell Programmer | Iron ingots + Psidust; recipe output psi:programmer | 5 iron ingots + 1 Psidust | One Spell Programmer | Shaped crafting recipe | Recipe JSON; exact-tag source |
| Basic Spell Bullet | Iron ingot + Psidust; recipe output psi:spell_bullet | 1 iron ingot + 1 Psidust | One empty Spell Bullet | Crafting recipe | Exact recipe should be confirmed in JEI; guide text |
| Cast cost | Spell's compiled Cost stat, CAD Efficiency, bullet/container modifier | Real cost = spell cost adjusted by CAD efficiency and, for container bullets, the bullet cost modifier; positive final cost is deducted | Spell execution plus Psi debit | Player cast; pre-cast event may alter cost and other cast parameters | PsiSpellCosts.java and ItemCAD.java; zero skips debit, insufficient balance can overflow |
| Psi overflow | Deduction exceeding available Psi; CAD may contain stored Psi | CAD stored Psi is consumed first; remaining overflow becomes damage, except where shatter path changes handling | Player damage and overflowed state | On positive cast debit | PlayerData.deductPsi; overflow blocks subsequent casts until cleared |
| Default group completion | Execute the group's designated main piece | No cost specified by the group registry | PieceGroupAdvancementComplete event; no advancement automatically awarded | First qualifying execution | PlayerData.java and docs/spell-pieces.md; event receiver in TMM not established |
| Advancement-gated custom group | Custom psi:spell_piece_group entry referencing a displayed advancement | Advancement completion is prerequisite | Group pieces appear and compile/import for that player | Per-player advancement state | Datapack behavior documented upstream; no TMM custom group identified |

## Acquisition, renewability and output economics

- Exact material sources, crafting chain, resource sinks, renewable loops and upgrade costs are unresearched.
- Psi's official project description frames the core output as flexible programmed action sequences, not a general-purpose FE or fluid generator. It does not establish any pack-specific resource converter.
- The Modrinth project description says the mod includes in-game spell sharing/copying and community spell examples; sharing spell data is distinct from producing a resource item.
- Do not add recipe, loot or market substitutions until all exact IDs and gates are checked from the pinned artifact.

## Complete player progression / walkthrough blueprint

**Completion test:** not met. The exact-tag starter route and six tutorial subjects are mapped, but the complete component/equipment branches, every reference entry, and client acceptance remain open.

| Path and step | Gate or choice? | Exact prerequisite / materials / world condition | Player actions: obtain, build, activate, use | Unlock / visible success / next step | In-game guide section or JEI reference | Knowledge-base coverage |
| --- | --- | --- | --- | --- | --- | --- |
| 0. Open the manual | Teaching step | Psi's Patchouli book and master keybind | Open Encyclopaedia Psionica using Psi Master Keybind | Introduction and categorized entries visible | Basics → Introduction | Source mapped; client key assignment/display untested |
| 1. Build the starter machine and assembly | Required | 5 iron ingots + piston for CAD Assembler; 4 iron ingots for Iron CAD Assembly | Craft both; place/open assembler and insert the assembly | Basic CAD assembled | Basics → CAD Assembler; JEI recipe lookup | Source recipe and instructions mapped |
| 2. Make Psidust | Required | Basic CAD and dropped Redstone Dust | Aim at the item and use the CAD | Psidust output; 100 Psi deducted and 60-tick regen cooldown | Basics → Introduction | Source action mapped; exact item-entity consumption not audited |
| 3. Program and compile | Required | Spell Programmer, basic bullet, CAD; 5 iron + Psidust for Programmer, iron + Psidust for basic bullet | Write the Debug example; connect typed pieces, configure parameters, and check compile indicator | Valid spell can be stored in a bullet | Legacy Entries → Tutorial 1 | Guide steps mapped; exact item recipes require client/JEI confirmation |
| 4. Load, select and cast | Required | Compiled bullet and assembled CAD | Use CAD Assembler to load bullet; hold CAD and use master-keybind radial selection; cast | Debug output confirms execution; inspect cost and errors | Legacy Entries → Tutorial 2 | Source guide mapped; in-game casting not tested |
| 5. Learn the core language | Recommended learning sequence | Tutorials 3–6 | Explore types/numbers, Add Motion, spell limits, then Explode and Error Suppressor | Practical spell construction; default groups themselves do not enforce unlocks | Legacy Entries → Tutorials 3–6 | Topic order mapped; individual piece behavior remains incomplete |
| 6. Expand components and casting forms | Optional branches | Core, Socket, Battery, Colorizer and bullet variants | Follow component/item entries and JEI recipes; test each form | Higher CAD stats, capacity or alternate delivery behavior | Components and Items categories | Full recipe, stat and limit audit remains open |
| 7. Completion | Open-ended | No source-backed final level or mastery gate found in the reviewed slice | Define any pack completion only after the full branch audit | No terminal tier currently established | Current guide labels itself work in progress | Do not claim source-complete or client-verified status |

## Internal combinations and overlooked mechanics

| Internal combination | Full chain | Benefit | Limitation / exploit potential | Evidence |
| --- | --- | --- | --- | --- |
| Tutorial-driven spell development | Read a tutorial → construct and compile its example → cast through CAD | The guide supports learning by doing; default piece groups do not enforce a level tree | Tutorial milestones are not automatically granted as advancements by Psi | Exact-tag guide and docs/spell-pieces.md |
| User spell library | Share/copy support is described by the official project page | May reduce repeated authoring and support community examples | Exact 1.21.1 import/export item/UI and validation path remain unreviewed | Current project page; profile remains researching |

## Low-hanging setups useful in ordinary survival

The source now supports a verified starter route and two safe tutorial setups; their in-game operation has not been tested.

### 1. Psidust starter loop
- **Goal and payoff:** Produce the first spell-programming material from vanilla Redstone Dust.
- **Minimum entry and ingredients:** CAD Assembler (5 iron ingots + piston), Iron CAD Assembly (4 iron ingots), dropped Redstone Dust.
- **Build/use steps:** Assemble the basic CAD; target the dropped redstone and use the CAD.
- **Sustainability and limits:** Each action deducts 100 Psi and sets a 60-tick regen cooldown; exact item drop consumption and practical throughput remain client/source follow-up.
- **Evidence / pack test:** Exact-tag recipe/action source and guide; not client-tested.

### 2. Debug tutorial spell
- **Goal and payoff:** Confirm that typed pieces compile, store and cast through the CAD.
- **Minimum entry and ingredients:** Spell Programmer, basic Spell Bullet, assembled CAD; programmer and bullet recipes include Psidust.
- **Build/use steps:** Follow Tutorials 1–2 to connect Selector: Caster to Trick: Debug, compile, store in bullet, load and cast.
- **Sustainability and limits:** Positive cast cost is adjusted by CAD Efficiency and bullet modifier; overflow may damage the player.
- **Evidence / pack test:** Exact-tag Patchouli content and cast source; not client-tested.

### 3. Mobility spell tutorial
- **Goal and payoff:** Learn a direct player-facing effect using Trick: Add Motion.
- **Minimum entry and ingredients:** A compiled CAD spell using the caster, look vector and numeric strength.
- **Build/use steps:** Follow Tutorial 4, compile and cast in a safe open area.
- **Sustainability and limits:** Strength is a spell parameter and affects spell statistics; repeated casting may cause unsafe movement or Psi overflow.
- **Evidence / pack test:** Exact-tag tutorial content; not client-tested.

## API, integration and pack-config surfaces

| Hook or customization surface | Namespace / exact file, API or serializer | Capabilities / constraints | Current TMM override? | Evidence |
| --- | --- | --- | --- | --- |
| Mod API | Psi API includes PieceGroupAdvancementComplete and PreSpellCastEvent; source event/data classes inspected | Events expose group completion and allow a pre-cast listener to alter/cancel casts; no listener behavior presumed | No TMM listener identified in this profile pass | Exact-tag source; pack script/event search remains open |
| Config | Config defaults have not been inventoried from the tag/JAR | Do not infer configurable costs, unlocks, or piece behavior | Unknown | JAR/config audit remains open |
| Spell sharing | Official project page describes sharing/copying; exact source handler and UI were not reviewed | Keep as a candidate until import/export, version mismatch and missing-addon behavior are source-mapped | None identified | Source and client follow-up |
| Recipes and tags | Exact-tag starter recipes inspected; broader recipe registry not inventoried | No general substitution/tag-compatibility claim | No Psi override identified in inspected pack paths | Full recipe audit pending |

## Compatibility leads

| Other mod | Candidate link and why | Native / pack-built / would require bridge | Status / source |
| --- | --- | --- | --- |
| Psi: Tweaks and Additions | Installed addon adds separate types, spell pieces and research systems; see the addon profile | Treat addon content as a separate branch from base Psi | Separate profile remains researching |
| Ars Nouveau, Iron's Spells and other spell systems | Could offer distinct or complementary spell capabilities; no shared resource, registry or bridge is established | Unknown; likely needs deliberate bridge for direct interchange | Hypothesis only; inspect both sides before proposing an interaction |
| FTB Quests | Base Psi posts a piece-group completion event; no TMM listener or FTB detector verified | A custom event listener would need implementation/review; item possession is not proof of a successful cast | Candidate only; quests untouched |

## Quest and teaching opportunities (research, not actual quests)

| Stage and teaching goal | Actionable in-game instructions (prerequisites → steps → result) | Named guidebook section or JEI recipe | Intended behavior vs truthful FTB detector / fallback | Useful XP + fixed reward direction | Coverage and gating risk |
| --- | --- | --- | --- | --- | --- |
| First guide access | Controls → Psi Master Keybind → Encyclopaedia Psionica → Basics → Introduction | Patchouli book in source | No read-page detector confirmed; do not substitute a piece event for reading | No reward implied | Source page path mapped; client access untested |
| First spell interface | Craft CAD Assembler and Iron CAD Assembly; insert assembly to create basic CAD | Source-mapped recipes and Basics → CAD Assembler | Crafting tasks prove recipe completion, not successful CAD operation | Material reward only if pack design later calls for it | Client machine interaction untested |
| Author and cast | Follow Tutorials 1–2: compile Debug spell, transfer to bullet, load/select, cast and inspect output | Legacy Entries → Tutorials 1–2 | Psi has a cast event in source; FTB event bridging/listener not checked | No quest reward authored | Client/server detector behavior unverified |
| Unlock progression | Teach Tutorials 1–6 as guide chapters; treat the 29 default groups as categories/events, not locked tiers | Legacy Entries → Tutorials 1–6 | No default advancement tree is enforced by base Psi | Rewards would be separate pack design | Tutorial-to-event mapping not tested |
| Advanced branches and sharing | Map CAD component stats, bullet forms, equipment, and import/export before teaching | Components and Items guide categories | Operation and any detector must be verified separately | No reward authored | Full source/client audit remains |

## Existing chapter / pilot reconciliation

| Walkthrough stage | Current quest ID/title and status | Intended action vs actual detector | Named in-game guide/JEI reference | Gap / next action |
| --- | --- | --- | --- | --- |
| All stages | No Psi chapter or pilot inspected in this research pass | Not reviewed | Not reviewed | Search canonical quest authoring/generated data before any future quest handoff |

## Conflicts, limitations and version drift

- The pinned artifact is 1.21.1-110. The official `release-1.21.1-110` tag declares Minecraft 1.21.1, NeoForge 21.1.207, and mod version 110. The tag's contents were queried directly, but its peeled commit SHA and equivalence to the packwiz JAR hash were not established. [pin] [source: release tag metadata]
- Source Gradle metadata says Patchouli 1.21.1-92; the pack pins Patchouli 1.21.1-93. The upstream README and Modrinth page say Patchouli is required. This is a minor dependency-version skew, not a verified incompatibility. [source: release tag README and Gradle metadata] [pin]
- The upstream source metadata advertises NeoForge 21.1.207 or newer; the pack target is NeoForge 21.1.265. This is within the declared range, but compatibility was not tested. [source: release tag metadata] [pin]
- The official changelog's detailed public entries are mostly old Minecraft-version series. Those entries are not used as proof of 1.21.1 recipes or mechanics.
- The exact-tag Patchouli assets and English localization provide the in-game book structure and tutorial names. The public web Codex retrieval failed; this does not prevent exact-tag source review.
- Psi is licensed under the Psi License; licensing/distribution compatibility should be reviewed before copying or redistributing guide content. TMM is only linking and summarizing here.

## Evidence ledger

| Claim / section | Source URL or exact inspected `file:path` + immutable SHA | Evidence tag / version of source | Pack version gap / what it proves / remaining limits |
| --- | --- | --- | --- |
| Pack pin | `mods/psi.pw.toml` and `manifest/mods.json` in `test/full-integration` | [pin] version ID `j9TFdTKC`, SHA512 recorded above | Exact artifact identity, not artifact inspection |
| Release metadata | [Upstream `gradle.properties`](https://github.com/VazkiiMods/Psi/blob/release-1.21.1-110/gradle.properties) | [source: tag `release-1.21.1-110`] | Declares MC 1.21.1, NeoForge baseline and mod version 110; tag SHA/JAR equivalence not established |
| Project gameplay overview | [Official Psi site](https://psi.vazkii.net/) | [docs: current official overview] | Describes spell programming and historical leveling/tutorial framing; exact-tag 1.21.1 group docs/code qualify that default groups do not enforce unlocks |
| In-game guide and starter flow | Exact tag `Xplat/src/main/resources/assets/psi/patchouli_books/encyclopaedia_psionica/en_us/entries/` plus `Xplat/src/main/resources/assets/psi/lang/en_us.json` | [source: `release-1.21.1-110`] | Defines book categories, the six tutorial entries and their localized text; client access/rendering still untested |
| Dependency and sharing description | [Official Modrinth project page](https://modrinth.com/project/pOeA0exL) | [docs: current project description] | States Patchouli required; describes spell sharing/copying; not a pinned artifact audit |
| Piece progression and cast APIs | Exact-tag `docs/spell-pieces.md`, `PsiSpellPieceGroups.java`, `PlayerData.java`, `ItemCAD.java`, `PsiSpellCosts.java` | [source: `release-1.21.1-110`] | Confirms default group event semantics, source cost path, attributes and overflow handling; no JAR parity or TMM listener claim |

## Disposable-world test plan

| Test | Positive/boundary case and disposable-world setup | Expected observable result (actual mechanic AND quest credit separately) | Performed? / evidence |
| --- | --- | --- | --- |
| Guide access and starter crafting | Fresh survival world; invoke Psi Master Keybind, open the Encyclopaedia, craft the Assembler/assembly and make Psidust | Confirm client access, recipes, CAD assembly, Psidust output, cost/cooldown and item-entity debit | Not run |
| Spell compile and cast | Follow Tutorials 1–2 with a valid Debug spell; separately try an invalid compile and a cast with inadequate CAD stats | Confirm compiler errors, bullet transfer, cast output, actual cost and failure boundaries; no quest credit assumption | Not run |
| Server-side behavior | Use a valid spell and an invalid/out-of-range spell on a dedicated server | Confirm authoritative results, resource debit, 32-block failure and whether event hooks fire | Not run |
| Addon boundary | Compare base Psi and enabled Psi: Tweaks and Additions in disposable installs; test one addon-only component and import behavior without addon | Confirm which content belongs to base vs addon, compatibility errors and whether spell fails safely | Not run |
| Sharing/import | Export a simple spell, import into same version, then try an unavailable-piece/version case | Confirm actual share format, validation/warning and whether imported data grants progression (not assumed) | Not run |

**Status requirement:** keep `researching`. The starter loop, six tutorial topics, default group behavior, baseline Psi attributes and general cast constraints are source-mapped, but the pinned JAR/source equivalence, complete component and trick catalogs, TMM overrides, progression event listeners, and client acceptance remain open. No gameplay tests were run.

## Unknowns and next research

- [x] Retrieve the exact-tag Patchouli entry definitions and localization keys; identify Basics, Components, Items, Legacy Entries and all six tutorials. Complete page-by-page guide review remains open.
- [ ] Inspect the pinned JAR transiently to validate tag parity, config defaults, registries, complete recipes/data and advancements.
- [ ] Resolve the release tag to its peeled commit SHA and compare the exact pinned JAR to source/resources.
- [x] Map the starter interface, CAD/Programmer/bullet recipes, Psidust action, baseline Psi attributes, spell-stat checks and the guide's 32-block cast boundary. Full component tiers, piece costs and individual trick limits remain open.
- [x] Inventory the enabled Psi: Tweaks and Additions pin and profile it separately; see the linked addon profile.
- [ ] Search the full TMM datapack/scripts for Psi group overrides or event listeners if progression integration is later proposed; quest work remains out of scope.
- [ ] Perform clean client and dedicated-server disposable-world tests only when the user requests implementation verification.

## Revision notes

- 2026-11-16 — Created an explicitly partial profile from the exact pack pin, official release metadata and project/docs sources. No quest, runtime or configuration files changed.
- 2026-11-16 — Reconciled the 1.21.1 guide and progression model against the exact release tag: mapped its six tutorial entries and starter recipes, default 5,000/25 Psi attributes, CAD/stat/resource casting rules, 32-block fizzle boundary and 29 default piece groups. Corrected the stale leveling-tree implication: the default groups are not enforced unlock gates. Tag-to-JAR equivalence, complete component/trick coverage and client acceptance remain open. No quest/runtime edits.
