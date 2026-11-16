# Psi: Tweaks and Additions — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | psi-tweaks / psitweaks |
| Category / parent or addons | Magic addon for [Psi](psi.md); conditional Mekanism, Mekanism Generators, Curios and Patchouli integrations |
| MC / loader / pack target | Minecraft 1.21.1 / NeoForge / TMM test/full-integration |
| Pinned version / artifact / hash | mods/psitweaks.pw.toml; psitweaks-1.21.1-0.10.10-hotfix.jar; Modrinth project pwmvTzDk, version sazDcHZZ; SHA512 22800c1ec3a29c6b246dc0e56e0621b28f7a5ac17f13bdbdb88dfac9ff1b17b32860145fa3139fd063e0139f7847ff43ec8d1522b558cb8ae371cb0b8b7bf33e [pin]. CurseForge identifies this exact NeoForge artifact as beta. |
| Source snapshot | `moratan251/psitweaks@ebb3d3c28bb1f41790ccb7cd6bbcff31889d2363`; Gradle metadata at this immutable commit declares Minecraft 1.21.1, NeoForge 21.1.248 and `1.21.1-0.10.10-hotfix`, matching the pinned artifact name/version. Source-to-JAR byte equivalence is not established. |
| Guide/documentation version gap | Upstream README places addon material in Psi's Patchouli Encyclopaedia; v0.10.10 changelog confirms Ideaspace pages and recipe-name display. Exact guide tree/page order and its agreement with the pinned JAR remain uninspected. |
| Profile status | researching |
| Evidence reached | Exact pack pin; CurseForge file metadata; immutable matching-version source commit; source config, unlock handler and generated research-recipe provider reviewed; README/changelog through v0.10.10; TMM roster comparison; no exact JAR inspection or client test |
| Reviewed | 2026-11-16 |
| Quest ownership | No Psi addon quest chapter reconciled. No quest files changed. |

## Executive gameplay map

**What the player actually does:** Psi: Tweaks and Additions extends [Psi](psi.md) with more programming data types, spell pieces, CAD hardware, bullets, equipment and a research gate for selected advanced pieces. Its changelog describes a Program Research Station where new pieces are unlocked by spending items, power and time; the upstream project page says research documentation lives in Psi's Patchouli Encyclopaedia. It also introduces automation-facing spell actions and an Ideaspace Storage system that can move items, fluids, Mekanism chemicals and FE when those modes are available. Mekanism is not enabled in the current TMM manifest, so its machines, MekaSuit and industrial recipes must be treated as absent; the addon changelog says some simplified alternate material and research recipes remain without Mekanism. This addon therefore has a native spell/programming branch and a broader optional technology branch.

**Why it matters in TMM:** It changes Psi from primarily authored spells into a larger engineering and late-game progression system. It adds typed programming, unlock research, casting-support equipment and a virtual storage/transfer subsystem. The storage/transfer feature may conflict with the project contract against generic item logistics; this is recorded as a design issue for future implementation decisions, not changed here.

**Quick start (research outline; exact recipe order still needed):**
1. Install the required base Psi, Curios API and Patchouli versions. All three are in TMM.
2. Open Psi's Encyclopaedia Psionica and locate the addon entries; upstream says addon documentation is included there.
3. Use JEI and the guide to identify the first addon material, spell unlock and any non-Mekanism alternative recipes.
4. Progress through the addon research gate by supplying its actual required inputs and power/time; confirm the unlock rather than assuming ingredient possession equals research.
5. Build casting-support equipment such as a spell magazine or inline caster, then evaluate higher-tier cores/batteries and advanced spell pieces.
6. Treat Ideaspace Storage as a separate candidate branch pending pack policy review. Test its types and limits before any questbook presents it as allowed or recommended.

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Expanded typed spell programming (Item, Block, String, Matrix); research-gated advanced pieces; high-tier CAD assemblies/cores/batteries and spell bullets; casting support including spell magazine, inline caster and automatic caster; Ideaspace Storage transfers across supported resource types. | Exact pinned recipe graph is not inspected; no unlock is responsibly labeled early yet. Spell-piece bookmarking/programmer editing helps authoring but is not a survival progression reward. | Base Psi progression and Codex; optional Mekanism industrial recipes/generators/MekaSuit (not installed); spell-driven storage/automation (high policy conflict, not authorized as runtime). |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Spell types, CAD memory and lists | Changelog v0.10.0 adds Item, Block, String and Matrix values, expanded lists and plain-value memory | partial | Encyclopaedia entries; exact titles unknown | not run | Inventory all types, unlock gates and parameter semantics from pinned guide/source |
| Research unlocks | v0.8.0 changelog says Program Research Station spends items, power and time to unlock pieces; whether research is required is configurable in that release | partial | Research pages in Encyclopaedia; exact recipe and current config names unknown | not run | Confirm pinned machine name, costs, energy type, recipes, unlock graph and default |
| CAD assemblies, components and bullets | README/changelog describe new material families, higher-tier cores/batteries/bullets and spell magazines | partial | Item pages/recipes uninspected | not run | Trace material progression through pinned recipes |
| Casters, magazines and automation | Changelog describes Inline Caster, Secondary/Parallel Casters, Spell Magazine, Auto Caster; custom tick interval in v0.8.2 | partial | Encyclopaedia; exact operation and caps unknown | not run | Verify behavior on 1.21.1 artifact, detector hooks and limits |
| Ideaspace Storage | Changelog v0.10.8–0.10.10 describes item/fluid/chemical/FE modes and programmable deposit/withdraw and amount selectors | partial | Ideaspace Storage pages referenced in v0.10.10 changelog | not run | Highest-priority policy check; inspect ownership, channels, capacities, limits, recipes and modes |
| Conditional Mekanism branch | README lists Metallurgic Infuser, Program Researcher, Material Mutator, Sculk Eroder, Psionic Generator and MekaSuit modules; dependency-absent setup omits Mekanism-specific blocks | partial | JEI/guide unknown | not run | Not in TMM roster; document only as absent optional branch |
| Configuration and compatibility | Changelog mentions config UI, research requirement, Psi damage/cooldown options and data-driven spell unlocks | partial | In-game config UI; exact keys uninspected | not run | Inspect defaults and reload behavior from source/artifact |

## Guidebook onboarding — required before the player's first complex step

- **Guide name and access:** Psi's Encyclopaedia Psionica (Patchouli). The base Psi profile records its master-keybind route. Upstream says PsiTweaks documentation is in the same guide.
- **Earliest attainable stage:** Unknown whether all addon pages appear immediately or are gated by research/material.
- **First instructional quest:** “Open Psi's Encyclopaedia” should point players to the Psi master keybind, then to the addon category and first research page. Require the first documented research operation, not only possession of a research item.
- **Reference map:** Ideaspace Storage guide pages are explicitly mentioned in v0.10.10 changelog; other exact headings are not established. JEI is strongly recommended upstream, not established as a hard dependency.
- **Where guide is insufficient:** Need exact page names, recipe visibility, initial state, research eligibility and operation costs from the pinned guide/JAR. Changelog is a feature record, not a complete walkthrough.
- **Other in-game help:** Upstream advertises a configuration screen and programmer QoL. v0.10.10 changelog says guide pages with several recipes now display the current recipe name.

## Systems and full mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and source |
| --- | --- | --- | --- | --- |
| Extended spell value types | Psi spell-programming UI and unlocked addon pieces | Typed values include Item, Block, String and Matrix; lists extend beyond Entity; CAD memory can store Number/String/Matrix and Psi-native Vector | Enables spells to inspect/transform item and block data and use richer list/matrix flows | Exact piece catalog and conversion rules incomplete. [source: changelog v0.10.0] |
| Research | Program Research Station named in v0.8.0 changelog; project page describes a research system | Item inputs, power and elapsed time; recipe costs and energy units unknown | Unlocks selected spell pieces; v0.8.0 notes powerful addon spells also required unlocking | Whether research is mandatory can be configured per v0.8.0; exact default/key and current machine names unknown. [source: changelog v0.8.0/v0.10.3] |
| CAD materials/assemblies | CAD Assembly component recipes | New material families and components; exact recipes unknown | Higher performance and more practical large/high-cost spells per project page | Do not infer linear tier stats; audit exact recipes and stats. [docs: project page; source: changelog v0.10.5/v0.10.7] |
| Batteries / cores | CAD assembly component slots | Quantum/Reactive CAD Batteries; Absorptive/Ultraclocked CAD Cores; costs unknown | New hardware profiles; capacity/regeneration/efficiency/potency unknown | Recipes changed in v0.10.6; pin is v0.10.10 hotfix. [source: changelog v0.10.5–0.10.7] |
| Research machine | Research branch, with conditional Mekanism content | Research recipes require items, power and time; v0.10.3 says Program Research Table consumption is per-tick | Unlocks spell pieces | Exact energy type, machine availability and recipes depend on 1.21.1 setup. [source: changelog v0.8.0/v0.10.3; docs: README] |
| Spell Magazine | Hold/use while carrying a CAD, per v0.9.3 changelog | Stores up to 12 spell bullets; right-click while holding a CAD swaps stored bullet with selected CAD bullet | Portable library and bullet swap | Exact recipe, selection mode and server state untested. [source: changelog v0.9.3] |
| Filter Card | Register sample item(s), then use Selector: Filter Card | Holds up to 9 sample items; can be in Curios slot, either hand or inventory | Reads registered sample items into a spell | Exact matching rules and selection ordering need verification. [source: changelog v0.10.10] |
| Inline Casters | Put a spell bullet in its internal socket via CAD Assembler or similar; right-click while holding a CAD | One internal socket; exact Psi cost/cooldown unknown | Casts selected spell bullet; treated as casting through carried CAD | Secondary and Parallel Casters have 5- and 11-slot variants per v0.9.3; verify current item. [source: changelog v0.9.3] |
| Auto Caster | Place/configure; v0.8.2 adds custom tick interval | Spell bullet and configured ticks; exact energy/cost/cadence not fully established | Repeated spell execution | Seven sockets in v0.8.2; test invalid spell, no target, chunk unload and restart. [source: changelog v0.8.2] |
| Ideaspace Storage | Storage interface and Psi spells; exact block/item route uninspected | Supports item, fluid, Mekanism chemical and FE modes per v0.10.10. Item filters support ID wildcard, Item, Item List or strict NBT matching | Deposit/withdraw and slot-specific transfer; selectors expose free capacity, type count and last transfer amount | Dimensions, ownership, capacity, throughput, persistence and dependency-absent modes need verification. This virtual storage/transfer may conflict with the project's no-generic-storage/logistics rule. [source: changelog v0.10.8–0.10.10] |
| Conditional Mekanism branch | Only when Mekanism is installed | Metallurgic Infuser recipes and Mekanism power/materials; exact recipes uninspected | Psi materials, Program Researcher, Material Mutator, Sculk Eroder, Psionic Generator and MekaSuit modules listed upstream | Not present in TMM without Mekanism. [docs: current upstream README; roster comparison] |
| Spellpiece/data configuration | In-game config and data-driven spell_unlocks referenced in v0.10.10 changelog; schema not retrieved | Datapack/config edits | Can change spell unlock rules; /psitweaks spellunlock behavior fixed in v0.10.10 | Invalid unlock settings abort load and preserve previous settings per changelog; no runtime edits in this task. [source: changelog v0.10.10] |

## Material / capability input-output contracts

| System | Input kind + exact ID/tag/event | Input qty/formula; consumed/remainder | Output kind, qty and location | Trigger/cadence; range/capacity | Automation/interface and evidence |
| --- | --- | --- | --- | --- | --- |
| Research unlock | Item + energy/power + time; exact recipe IDs and energy type unknown | Recipe-specific; not established | Spell-piece unlock on player progression state | Timed operation; exact duration unknown | Research station/table; quest event unknown |
| Spell programming | Typed values (Item, Block, String, Matrix; plus Number/Vector/list) | No item/resource consumption established; memory and runtime constraints unknown | Computed values and programmed action | On spell execution | Native Psi integration; source changelog |
| Ideaspace item transfer | Stored stacks and program-defined filters | Transfer amount is program-defined; exact limits/consumption unknown | Items deposited/withdrawn within virtual storage or destination | Per spell cast; range/capacity unknown | Psi spell pieces; custom automation system; policy concern |
| Ideaspace fluid/FE/chemical transfer | Fluid, FE and Mekanism chemical modes per changelog | Units and amounts unknown; chemical mode requires Mekanism | Type-specific transfer | Per spell cast; capacity/cadence unknown | Chemical mode not available without Mekanism; others need artifact review |
| Psionic Generator, conditional | Linked owner's remaining Psi per upstream README | Consumes Psi; older v0.9.4 changelog says 25 FE per Psi | FE | While running; capacity/link rules unknown | Conditional Mekanism branch; not in TMM. 25 FE/Psi is historical and not asserted for pin |
| Spell Magazine | Up to 12 spell bullet items | Swaps selected bullet into CAD; exact consumption/remainder unverified | Selected bullet changes on carried CAD | Player interaction; cap 12 per v0.9.3 | Manual interaction; no automation confirmed |
| Inline Caster | Bullet in internal socket | One socket; exact cost and reuse unknown | Spell effect on right-click | Player interaction | CAD Assembler or similar; automation unconfirmed |

## Acquisition, renewability and output economics

- Exact material sources and tier recipes are unknown. Changelog names Psimetal, Ivory Psimetal, Ebony Psimetal, Chaotic Psimetal, Flashmetal, Heavy Psimetal, Antinite, Psycheonic Metal, Hypostasis Gem and other components; do not infer recipes or worldgen.
- Version 0.10.3 adds nugget forms and certain blocks, plus integrations for Mystical Agriculture, Mystical Agradditions and Productive Bees. None is in the TMM manifest.
- With Mekanism absent, source says some alternate recipes are provided and Mekanism-dependent blocks are omitted; exact alternate material/research recipes need JEI or data inspection.
- Ideaspace Storage transfers rather than manufactures the listed item/fluid/FE/chemical types per changelog. Total capacity and automation behavior are unknown.
- Historical v0.9.4 notes a Psi-Link Generator at 25 FE per Psi consumed; current operation/version is unverified and the current README associates industrial features with Mekanism, which is absent.
- No renewable-loop or throughput claims without pinned recipe data and disposable-world measurements.

## Complete player progression / walkthrough blueprint

**Completion test:** not met. Feature families are known, but the mandatory first recipe and full research/material/unlock graph are not.

| Path and step | Gate or choice? | Exact prerequisite / materials / world condition | Player actions: obtain, build, activate, use | Unlock / visible success / next step | In-game guide section or JEI reference | Proposed quest/how-to, detection and reward | Evidence / coverage |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 0. Open addon guide | Required teaching step | Base Psi and Patchouli; whether addon pages are gated unknown | Use Psi master keybind to open Encyclopaedia Psionica and locate addon entries | Addon guidance visible | Encyclopaedia; category names unknown | Teach actual keybind and first addon page; tutorial or unlock event, manual fallback if needed | README/Modrinth; partial |
| 1. Identify early material route | Required | First material source and alternate recipe uninspected | Use guide/JEI to acquire first addon material and identify base-Psi integration | First crafting branch opens | Recipe/use pages and guide section unknown | Teach exact recipe/source; no high-tier material reward | Unresearched |
| 2. Learn typed spell values | Core feature branch | Unlock gates unknown; source lists Item/Block/String/Matrix | Reproduce verified tutorial spell using one new type | New value type works in programmer | Tutorial name unknown | Require successful operation, not piece possession | Changelog v0.10.0; partial |
| 3. Unlock research-gated pieces | Core progression gate | Research machine path; exact inputs, energy, time and first unlock unknown | Build and operate research machine, supply one verified recipe, wait and confirm unlock | New piece becomes available | Research guide/JEI page unknown | Detect unlock/advancement if exposed; ingredients alone are only proxy | Changelog v0.8.0; partial |
| 4. Advance CAD hardware/material tiers | Major optional progression | Multiple material families and parts; chain unknown | Craft verified tiers and assemble components | Supports larger/higher-cost spells | CAD guide pages/recipes unknown | Map real tiers; don't bypass with rewards | README/changelog; incomplete |
| 5. Store/swap and cast with supports | Convenience/combat branch | Magazine, Inline/Secondary/Parallel Casters; recipes unknown | Fill magazine, swap selected bullet, load caster and use exact activation | Improved loadout workflow | Equipment guide pages unknown | Detect actual cast if possible; item possession is weak proxy | v0.9.3 changelog; partial |
| 6. Configure repeated casting | Automation branch | Auto Caster, bullet, interval; exact setup unknown | Build, set interval, test harmless spell and invalid/no-target boundary | Repeated spell behavior | Guide/JEI page unknown | Teach shutdown/safety; distinguish cast from ownership | v0.8.2 changelog; partial |
| 7. Ideaspace Storage | Optional, policy unresolved | Interface and project-policy decision unknown | Do not present as recommended progression before rule review; if retained, test channels and filters | Programmable storage/transfer possible | Ideaspace Storage guide pages | No quest authored; policy conflict blocks quest/runtime promotion | v0.10.8–0.10.10; partial |
| 8. Mekanism branch | Optional, not installed | Mekanism, Generators, power and machine recipes | Not available in current pack | Conditional industrial materials, machines and modules | Integration pages unknown | Do not quest unavailable content | README; absent from TMM |
| 9. Terminal capability | Open-ended | Highest recipe, research list and completion signal unknown | Define only after guide/recipe scan | Terminal state unknown | Unknown | No mastery objective until graph is mapped | Incomplete |

## Internal combinations and overlooked mechanics

| Internal combination | Full chain | Benefit | Limitation / exploit potential | Evidence |
| --- | --- | --- | --- | --- |
| Research to spell pieces | Research item inputs + power + time → unlock selected pieces → author spells requiring them | Progression gate beyond ordinary piece access | Default requirement/unlock graph unknown; config/data can alter gates | Changelog v0.8.0/v0.10.10 |
| Magazine and multi-slot casters | Store up to 12 bullets → swap into CAD; Inline Caster can hold bullet; Secondary/Parallel variants have 5/11 sockets | Less hotbar burden and multiple loadouts | Effect of slot count on power/cost unknown | Changelog v0.9.3 |
| Typed lists and matrix operations | Read item/block/string values → transform/filter using list/matrix pieces → apply spell action | Enables complex block/item-data and automation programs | Block actions may enable griefing/duplication; permission and failure tests needed | Project page and changelog v0.10.0 |
| Ideaspace Storage | Selector/filter → deposit/withdraw/slot transfer → inspect free capacity, type count and last amount | Programmable material movement and storage | Could violate no-generic-logistics policy; ownership and transfer ceilings unknown | Changelog v0.10.8–0.10.10 |
| Psi conversion to FE | Linked player's Psi → conditional Psi-Link Generator → FE | Potential energy conversion | Historical ratio 25 FE/Psi; current behavior unverified, and Mekanism absent | Changelog v0.9.4 and current README |

## Low-hanging setups useful in ordinary survival

No setup can yet be called low-hanging because exact survival recipes and unlock gates are unknown.

### 1. Spell Magazine loadout — candidate
- **Goal and payoff:** Carry multiple spell bullets and swap them into a CAD.
- **Minimum entry and ingredients:** Magazine recipe, bullet route and CAD requirement not inspected.
- **Build/use steps:** Store up to 12 bullets; right-click while holding a CAD to swap the selected bullet, per v0.9.3 changelog.
- **Sustainability and limits:** Capacity 12; recipe cost and preservation need pin verification.
- **Evidence / pack test:** Changelog v0.9.3; no client test.

### 2. Inline Caster — candidate
- **Goal and payoff:** Cast a stored bullet with right-click while holding a CAD.
- **Minimum entry and ingredients:** Recipe and unlock requirements unknown.
- **Build/use steps:** Insert a spell bullet in its single internal socket via CAD Assembler or supported method; hold CAD and right-click.
- **Sustainability and limits:** Treated as casting through carried CAD; exact costs and cooldown unknown.
- **Evidence / pack test:** Changelog v0.9.3; no client test.

### 3. Programmed item transfer — blocked pending policy review
- **Goal and payoff:** Transfer/filter items through Ideaspace Storage using spells.
- **Minimum entry and ingredients:** Exact block and recipe unknown; pack contract prohibits generic item logistics/storage.
- **Build/use steps:** Not recommended for TMM quests pending policy decision. Research-only: v0.10.10 describes deposit/withdraw modes and selectors for capacity/transfer amount.
- **Sustainability and limits:** Capacity, throughput, automation and exploit behavior unknown.
- **Evidence / pack test:** Changelog v0.10.8–0.10.10; no test.

## API, integration and pack-config surfaces

| Hook or customization surface | Namespace / exact file, API or serializer | Capabilities / constraints | Current TMM override? | Evidence |
| --- | --- | --- | --- | --- |
| Spell unlock data | Changelog names datapack setting spell_unlocks; invalid settings abort load and preserve prior settings | Changes progression gates; schema/path unknown | None identified | Changelog v0.10.10 |
| /psitweaks spellunlock | Subcommands not enumerated; v0.10.10 fixes reload behavior and grant/revoke counts | Admin progression surface; not a quest detector | None found | Changelog v0.10.10 |
| Research requirement config | Exact key/default unknown; historical note says requirement can be configured | Can change whether pieces require research | None identified | Changelog v0.8.0 |
| Psi damage/cooldown config | v0.10.1 introduced options for Psi consumption on damage and regen cooldown | Changes survival costs and regen; exact defaults unknown | None identified | Changelog v0.10.1 |
| In-game config screen | Changelog says one is implemented | Current fields/options need review | Not tested | Changelog v0.10.1 |
| Conditional machine recipes | Mekanism-specific blocks omitted without dependency; some alternate recipes provided | Conditional content gate | Mekanism absent | README/changelog and manifest |
| Curios equipment | Curios API required/present | Filter Card works in Curios slot, either hand or inventory, per v0.10.10; other slot behavior unknown | Curios pinned in TMM | Changelog v0.10.10 and manifest |

## Compatibility leads

| Other mod | Candidate link and why | Native / pack-built / would require bridge | Status / source |
| --- | --- | --- | --- |
| [Base Psi](psi.md) | Required parent; addon adds types, pieces, equipment and research in the same Encyclopaedia | Native addon | Installed; source walkthrough incomplete |
| Mekanism / Generators | Optional material production, research/mutation machines, Psi-linked generation and MekaSuit modules | Native conditional integration | Not in TMM; unavailable in current gameplay |
| Mystical Agriculture / Agradditions / Productive Bees | v0.10.3 adds material crafting support | Native addon integration | Not in TMM manifest |
| Ideaspace Storage | Spell pieces work across item, fluid, chemical and FE modes | Native addon; no bridge required per changelog, but data not verified | Potential conflict with no-generic-storage/logistics policy |
| Psionic Utilities | Programmer interaction fixes and piece integration appear in changelog | Native addon integration | Not established as installed; do not promise |
| FTB Quests | Research/command/operation might expose detectors | Unknown | Needs event/API inspection |

## Quest and teaching opportunities (research, not actual quests)

| Stage and teaching goal | Actionable in-game instructions | Named guidebook section or JEI recipe | Intended behavior vs truthful FTB detector / fallback | Reward direction | Coverage and gating risk |
| --- | --- | --- | --- | --- | --- |
| Open addon guide | Open Encyclopaedia with Psi master keybind; find addon category and first research tutorial | Encyclopaedia Psionica; page unknown | Tutorial or unlock event; manual fallback if guide read isn't detectable | Small spellcrafting supplies after item audit | Exact guide pages unknown |
| Unlock first addon piece | Find first recipe/source, operate research machine and verify unlock | Research section and JEI recipe unknown | Unlock event/advancement; item possession is not proof | Incremental materials | Mandatory path gap |
| Upgrade CAD / bullet | Follow tier recipes and assemble exact parts | CAD/Assembly JEI and guide unknown | Item acquisition is proxy; test stats/casting separately | Intermediate component | Avoid granting rare gates |
| Use magazine/caster | Store bullet, swap into CAD or load Inline Caster and activate | Equipment guide unknown | Actual cast event if possible; manual fallback | Low-tier bullet/materials | Operation/cost unknown |
| Automation / storage | Teach Auto Caster; Ideaspace only after policy resolution | Relevant guide pages unknown | Cast/transfer event, not machine ownership | Only after safety and balance review | Storage-policy conflict |
| Mekanism branch | Separate chapter only if mod intentionally added | Mekanism JEI and guide | Not currently eligible | None for current pack | Dependencies absent |

## Existing chapter / pilot reconciliation

| Walkthrough stage | Current quest ID/title and status | Intended action vs actual detector | Named in-game guide/JEI reference | Gap / next action |
| --- | --- | --- | --- | --- |
| All addon stages | No PsiTweaks chapter or pilot reviewed in this pass | Not reviewed | Not reviewed | Search canonical quest source before future handoff; quest files unchanged |

## Conflicts, limitations and version drift

- The exact pack pin is a 1.21.1 NeoForge beta file dated 2026-10-20. The v0.10.10 hotfix notes address item/data loss on break/fire and a crash when shift-hovering machine items with contents. Test those protections before treating machine persistence as safe. [pin] [docs: exact CurseForge file page]
- Upstream says Mekanism is strongly recommended, but its changelog says the addon can run without Mekanism with simplified recipes and omitted dependency-specific blocks. TMM does not enable Mekanism; this profile distinguishes that optional branch.
- Upstream also lists Create Aeronautics/Sable, Tinkers' Construct (1.20.1), Mystical Agriculture, Mystical Agradditions and Productive Bees as optional integrations. They are not active in the current TMM roster or not established as installed.
- Ideaspace Storage handles programmable storage and transfer modes including items and FE. This appears to overlap the no-generic-storage/logistics contract. Record as a policy issue before quest/runtime promotion; no pack change made.
- Research unlock settings are configurable/data-driven, so actual guide progression may differ if future datapacks change those rules.
- Source README/changelog is aligned to the 1.21.1 project, but exact binary equivalence to the hotfix pin is unknown.

## Evidence ledger

| Claim / section | Source URL or exact inspected file + immutable SHA | Evidence tag / version | Pack version gap / what it proves / remaining limits |
| --- | --- | --- | --- |
| Pack artifact identity | mods/psitweaks.pw.toml and manifest/mods.json on test/full-integration | [pin] Modrinth project pwmvTzDk, version sazDcHZZ; SHA512 above | Exact file identity; not artifact inspection |
| Exact beta file | [CurseForge file 9266947](https://www.curseforge.com/minecraft/mc-mods/psitweaks/files/9266947) | [docs: 1.21.1 NeoForge beta, 2026-10-20] | Confirms exact filename/loader/channel/hotfix notes; not binary inspection |
| Project features/dependencies | [Upstream README](https://github.com/moratan251/psitweaks/blob/master/README.md) | [docs: 1.21.1 branch] | Confirms feature families, guidebook and conditional Mekanism support; source commit not fixed |
| Changelog/piece names | [Upstream CHANGELOG](https://github.com/moratan251/psitweaks/blob/master/CHANGELOG.md) | [source: versions through v0.10.10] | Confirms feature introduction and recent fixes; source tag SHA/JAR equivalence unresolved |
| Pack dependency comparison | manifest/mods.json on test/full-integration | [pin] | Curios and Patchouli present; Mekanism absent |
| License | [Modrinth project page](https://modrinth.com/mod/psitweaks/) | [docs: project metadata] | Project says MIT; no code/assets copied into profile |

## Disposable-world test plan

| Test | Positive/boundary case and setup | Expected observable result and quest credit | Performed? |
| --- | --- | --- | --- |
| Dependency-absent startup | Psi, Curios, Patchouli present; Mekanism absent | Confirm game loads, omitted machines and remaining alternate recipes; server/client separate | Not run |
| Research unlock | Start with locked piece; run valid recipe with inputs/energy; repeat missing-input or invalid-data case | Record consumption, energy, timer, unlock signal; bad settings must not silently remove gates | Not run |
| Ideaspace Storage | Empty/full stores; valid/invalid filters; item deposit/withdraw; test fluid/FE; compare absent-Mekanism chemical mode | Measure units, limits, ownership, persistence, transfer and policy; quest credit separately | Not run |
| Automation/casting | Auto Caster with harmless spell at chosen interval; then empty bullet/invalid target/chunk unload | Confirm cadence, costs, failure and server effect; record quest detector separately | Not run |
| Hotfix preservation | Fill a Sculk Eroder, Program Research Table, Material Mutator, Psi-Link Generator or Transcendent Energy Cube; break and separately destroy by fire/lava | Verify contents/energy/upgrades retained and no server crash | Not run |
| Filter Card | Register sample items in Curios, hand and inventory; test match/nonmatch | Confirm Selector: Filter Card values and slot precedence | Not run |

**Status requirement:** keep researching. Guide pages, full recipe/material tree, research prerequisites and storage/automation behavior remain incomplete. No tests run.

## Unknowns and next research

- [ ] Inspect exact 0.10.10 hotfix artifact metadata, data recipes, advancements, configs and guide JSON; do not commit artifacts or extracted copyrighted assets.
- [ ] Identify immutable source commit for the hotfix and compare its 1.21.1 files to source branch.
- [ ] Map full non-Mekanism recipe/research graph, first material/machine, costs, timer and unlock order.
- [ ] Map CAD stats, material/bullet tiers and actual terminal progression.
- [ ] Enumerate Ideaspace recipes, ownership, persistence, capacity, channels, fluid/FE units, automation and filters; reconcile with storage/logistics prohibition.
- [ ] Verify spell-piece unlocks and current data/config schema for spell_unlocks.
- [ ] Test hotfix fixes on disposable client and dedicated server when requested.
- [ ] Inspect quest authoring/generated files for existing PsiTweaks pilots before quest handoff.

## Exact source audit — hotfix source commit

The following source findings are tied to [commit `ebb3d3c`](https://github.com/moratan251/psitweaks/tree/ebb3d3c28bb1f41790ccb7cd6bbcff31889d2363). The commit's Gradle metadata matches the pinned version label and NeoForge line, but that does not prove the Modrinth JAR was built from this commit.

### Research gate and its current TMM reachability

- The common config's `spells.unlocks.requireSpellUnlocks` defaults to **true**. When enabled, the Psi piece-knowledge event cancels learning for entries in the addon unlock snapshot until the player's unlock tag is present. [source: SHA]
- The default unlock snapshot has **19 command IDs**: `cocytus`, `time_accelerate`, `flight`, `phonon_maser`, `meteor_line`, `dry_meteor`, `supreme_infusion`, `molecular_divider`, `radiation_injection`, `radiation_filter`, `cure_radiation`, `guillotine`, `active_air_mine`, `die_flex`, `jump_flex`, `switch_flex`, `material_mutation`, `mass_block_break`, and `idea_storage`. The final ID unlocks the Ideaspace piece group; the four related deposit/withdraw/amount/connector definitions share its unlock item/tag. [source: SHA]
- The configured unlock item is handled on right-click, and the source exposes permission-level-2 commands at `/psitweaks spellunlock <id> grant|revoke|status <player>` and `/psitweaks spellunlock all grant|revoke|status <player>`. This is admin surface, not normal survival progression. [source: SHA]
- The Program Researcher and its block/container registration are in Mekanism-specific classes. TMM does **not** install Mekanism. The generated default research recipe set includes direct Mekanism item IDs in 10 of its 19 recipes. Upstream README says alternate recipes exist for certain materials and research programs, but this audit has not established which alternative outputs are exposed without Mekanism or how they reach players. Thus the default Program Researcher route is not a proven TMM route; the missing alternate-recipe/guide/JAR audit remains a progression blocker. [source: SHA] [pin]
- Reloaded definitions come from datapack JSON under `data/*/spell_unlocks/*.json`; malformed/unreadable entries reject the reload as a whole and preserve the prior active snapshot. If the directory has no definitions, the handler keeps the current definitions. The server syncs the active definitions to clients. [source: SHA]

### Default Program Research recipe catalog (source data)

These are source-provider defaults, not a claim that the machine or every recipe is available in TMM. Recipe input counts are consumed except the Philosopher's Stone catalyst in Material Mutation. Time is source ticks (20 ticks/second). FE/t and total FE are calculated from the provider's `energy_per_tick` and `time`; they are source-data arithmetic, not measured draw. [source: SHA]

| Program output | Exact source inputs | FE/t × ticks (source) | Time |
| --- | --- | ---: | ---: |
| Cocytus | Blank Program ×1; Mekanism Antimatter Pellet ×8; Blue Ice ×64; Sculk Shrieker ×24; Heart of the Sea ×4; Psycheonic Metal Ingot ×64 | 1,500,000 × 108,000 = 162,000,000,000 FE | 5,400 s |
| Time Accelerate | Blank Program ×1; Clock ×8; Redstone Block ×8; Chaotic Psimetal ×8; Powered Rail ×32 | 10,000 × 1,200 = 12,000,000 FE | 60 s |
| Flight | Blank Program ×1; Advanced Spell Bullet Loop ×16; Sublimated Spell Bullet Loop ×1; Feather ×32; Chaotic Factor ×8 | 10,000 × 600 = 6,000,000 FE | 30 s |
| Phonon Maser | Blank Program ×1; Mekanism Laser ×1; Amethyst Shard ×12; Flashmetal ×8; Note Block ×48 | 40,000 × 2,400 = 96,000,000 FE | 120 s |
| Meteor Line | Blank Program ×1; Nether Star ×24; Mekanism Antimatter Pellet ×16; Alloy Hypostasis ×48; Psycheonic Metal Ingot ×128 | 1,500,000 × 288,000 = 432,000,000,000 FE | 14,400 s |
| Dry Meteor | Blank Program ×1; Packed Ice ×16; Psidust ×32; Bone ×16 | 15,000 × 1,200 = 18,000,000 FE | 60 s |
| Supreme Infusion | Blank Program ×1; Netherite Ingot ×1; Flashmetal ×8; Psion Alloy ×24; Amethyst Block ×16 | 40,000 × 2,400 = 96,000,000 FE | 120 s |
| Molecular Divider | Blank Program ×1; Psigem ×32; Quartz ×48; Heavy Psimetal ×8; Echo Control Circuit ×3 | 80,000 × 6,000 = 480,000,000 FE | 300 s |
| Radiation Injection | Blank Program ×1; Mekanism Uranium Ingot ×16; Mekanism Lead Ingot ×8; Chaotic Psimetal ×8 | 60,000 × 3,600 = 216,000,000 FE | 180 s |
| Radiation Filter | Blank Program ×1; Mekanism Lead Ingot ×16; Mekanism Fluorite Dust ×16; Chaotic Psimetal ×8 | 60,000 × 3,600 = 216,000,000 FE | 180 s |
| Cure Radiation | Blank Program ×1; Golden Apple ×8; Mekanism Lead Ingot ×16; Chaotic Psimetal ×8 | 60,000 × 3,600 = 216,000,000 FE | 180 s |
| Guillotine | Blank Program ×1; Wither Skeleton Skull ×1; Anvil ×3; Rotten Flesh ×24; Bone ×16; Psimetal Sword ×1 | 50,000 × 3,000 = 150,000,000 FE | 150 s |
| Active Air Mine | Blank Program ×1; TNT ×8; Psidust ×16; Psigem ×2 | 4,000 × 400 = 1,600,000 FE | 20 s |
| Die Flex | Blank Program ×1; Psionic Control Circuit ×16; Chaotic Psimetal ×16; Chaotic Factor ×16 | 6,000 × 600 = 3,600,000 FE | 30 s |
| Jump Flex | Die Flex Program ×1; Echo Control Circuit ×16; Flashmetal ×24; Mekanism Polonium Pellet ×4 | 100,000 × 54,000 = 5,400,000,000 FE | 2,700 s |
| Switch Flex | Jump Flex Program ×1; Hypostasis Control Circuit ×16; Heavy Psimetal ×32; Psitweaks Americium Pellet ×8 | 500,000 × 90,000 = 45,000,000,000 FE | 4,500 s |
| Material Mutation | Blank Program ×1; Philosopher's Stone ×1 (catalyst, retained); Sculk Catalyst ×1; Chaotic Factor ×16; Antinite Ingot ×32; Psionic Echo ×16 | 250,000 × 24,000 = 6,000,000,000 FE | 1,200 s |
| Mass Block Break | Blank Program ×1; Psigem ×16; Chaotic Psimetal ×16; TNT ×16 | 10,000 × 1,200 = 12,000,000 FE | 60 s |
| Ideaspace | Blank Program ×1; Quantized Plate ×16; Hypostasis Control Circuit ×8; Mekanism Quantum Entangloporter ×2; Dimensional Crystal ×2; Psycheonic Metal Ingot ×2 | 750,000 × 36,000 = 27,000,000,000 FE | 1,800 s |

The table is the generated default `program_research/*` set at the source snapshot. The provider emits JSON recipe inputs and outputs; it does not itself establish guide order, eligibility state, runtime consumption, or the alternative no-Mekanism route.

### Ideaspace source limits and persistence

- Common config defaults per player: **256** distinct item types; item quantity per type is **16,384 × that item's max stack size** (so stack-size-64 item: 1,048,576); **64** fluid types at **1,048,576 mB** per type; **64** Mekanism chemical types at **33,554,432 B** per type; and **Long.MAX_VALUE FE**. Config bounds allow increasing type counts up to 100,000 and setting per-type/energy amounts within positive long range. [source: SHA]
- The persistent store is keyed by player UUID in Overworld dimension saved data, with a per-player storage record. It therefore persists across logout and is not a per-dimension inventory; this is an inspected source behavior, not a runtime test. [source: SHA]
- The transfer tricks use server-side player context, finite positive quantities, loaded-target and interaction/protection checks; block/slot transfer paths also enforce spell radius and avoid the caster's own connector / CAD-sensitive slots. Item, fluid, chemical and FE mode availability depends on the relevant integration. Existing profile policy flag remains: this programmable storage/transfer system conflicts with the project's generic storage/logistics boundary and must not be taught as recommended TMM progression absent a policy decision. [source: SHA] [inference]

### Version and audit corrections

- The hotfix source config defaults `disableDamagePsiDeduction=true` and `disableRegenCooldown=true`, so the earlier base-Psi source behavior of Psi loss on damage / a regen delay is overridden in the addon setup unless a config changes it. This is config source evidence, not proof of effective server config. [source: SHA]
- Config default `requireSpellUnlocks=true` is stronger evidence than the older changelog's generic statement that unlock requirements can be configured. The default gate is on at this source snapshot. [source: SHA]
- The project README and release metadata identify required Psi, Curios API and Patchouli; Mekanism and Mekanism Generators are strongly recommended, not required. Their optionality does not make the Mekanism-only Program Researcher available without Mekanism. [docs: v0.10.10 source README]
- Added claims above are source-inspected at the immutable commit, not observed in TMM. Exact pinned JAR parity, guide access/page tree, alternative no-Mekanism program routes, item recipes/material sources, and client/server acceptance remain open.

## Revision notes

- 2026-11-16 — Added a source-backed partial profile for the enabled beta addon. Recorded no-Mekanism TMM mode, incomplete research/Ideaspace Storage and policy concern. No quest, runtime or config files changed.
- 2026-11-16 — Reconciled the profile to immutable source commit `ebb3d3c28bb1f41790ccb7cd6bbcff31889d2363`: added the 19 default unlock IDs, exact research recipe catalog, default lock/config behavior and Ideaspace storage limits/persistence. The no-Mekanism alternative research route and exact JAR/guide match remain unresolved; no quest/runtime changes.
