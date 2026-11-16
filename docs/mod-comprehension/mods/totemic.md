# Totemic — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | totemic / totemic |
| Category / parent or addons | magic; nature, totem poles and music |
| MC / loader / pack target | 1.21.1 / NeoForge / TMM test/full-integration |
| Pinned version / artifact / hash | mods/fenns_totemic.pw.toml; Totemic-neoforge-1.21.1-0.12.18.jar; Modrinth project GLffYJtD, version 6Vny8MoL; SHA512 4b3c4a6c926b8bf203b5593c558264b096f870ecfe74e6b6dbeef850825c9814a3b9a2d9f03c3a91680042dd684574f0259280210b9173750c921ff733e94896 [pin] |
| Source snapshot | Exact release tag `1.21.1-0.12.18` resolves to commit [`1a253b29815842aa4359d6118b8cd84cce3048a1`](https://github.com/TeamTotemic/Totemic/tree/1a253b29815842aa4359d6118b8cd84cce3048a1); tagged `gradle.properties` declares MC 1.21.1 and mod version 1.21.1-0.12.18. The GitHub connector returns 404 for source/resource files under that ref, so code-level source review remains limited to the 0.12.19 branch and is explicitly not pin-exact. Binary equivalence not tested. |
| Guide/documentation version gap | Exact Modrinth release page: 1.21.1-0.12.18 Release, NeoForge, client/server. Release notes contain Totempedia unlock and KubeJS changes. In-game Totempedia is recommended through Patchouli, but Patchouli is not a hard dependency of Totemic itself. |
| Profile status | researching |
| Evidence reached | exact pack pin; official 0.12.18 release notes; release tag and matching root Gradle metadata; branch-level 0.12.19 source inspected selectively but not applied as pin evidence; no pinned artifact inspection or client test |
| Reviewed | 2026-11-16 |
| Quest ownership | Proposed Magic chapter: Totemic. No quest files changed. |

## Executive gameplay map

**What the player actually does:** Totemic is built around totem poles, nature and music. Carved totem pieces provide passive effects nearby; a totem is also the focus of Ceremonies. The player prepares musical instruments, starts a Ceremony at a totem and performs music/dancing until its requirements are satisfied, then receives an effect that may influence players, entities, blocks or the environment. The Totempedia is the mod's guide and its Ceremony entries are progressively hidden until the player owns required instruments. A portable Medicine Bag can carry selected Totem Carvings, with a server config option that can blacklist carvings. The precise early acquisition sequence, pole construction and all Ceremony recipes/effects still need the pinned version's guide/data review.

**Why it matters in TMM:** Totemic adds a ritual system driven by player performance and instrument collection rather than a stored mana pool or standard altar ingredients. Each Ceremony has music and startup requirements; the player has to learn the particular setup. Do not treat a Ceremony as a generic right-click recipe or assume music output equals FE/mana.

**Quick start (partial):**
1. Obtain the Totempedia if Patchouli is present and open its Totems, Instruments and Ceremonies categories. Totemic calls Patchouli recommended; verify the pack contains the book and how it is acquired.
2. Gather the first Totemic materials and craft the Totem Whittling Knife, Totem Base, pole pieces and starter instruments using exact JEI/Totempedia recipes.
3. Build the documented totem structure, carve at least one Totem Carving, and observe its passive effect/range.
4. Select a Ceremony whose required instruments are owned; activate it at the pole, play the selector instruments, and perform the required dance until its music threshold is met.
5. Use the Totempedia's unlocked Ceremony pages to branch into growth, combat, weather, utility and other effects. Keep exact actions, timing and costs per Ceremony in the book.

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Carved totem passive effects; music-and-dance Ceremonies; instrument progression; portable Medicine Bag for selected carvings; programmable Ceremonies/carvings through built-in KubeJS/API | Early carving/pole crafting once the knife and base are made; Totempedia unlocks only relevant Ceremonies as instruments are obtained; some Ceremonies may support growth or player utility (exact recipes/costs still needed). | Candidate: growth Ceremonies may support farming; validate blocks and throughput. Candidate: KubeJS is installed and upstream release includes registry/event APIs; could support custom Ceremonies or carvings if authorized later. Candidate: connect Ceremony actions to quest detectors, but native operation events must be verified. |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Totempedia and discovery | official page recommends Patchouli; exact release notes describe progressive Ceremony entry locks | partial | Totempedia categories Totems/Instruments/Ceremonies, per release notes | not run | obtain item/access method and full page index |
| Totem pole / base / carving | official description confirms passive boons and Ceremony focus; source API references TotemCarving | partial | Totempedia Totems and Carvings | not run | exact build geometry, knife recipe, carving method/effect/range |
| Instruments | release changelog names Wind Chime and Jingle Dress entries | partial | Totempedia Instruments | not run | source-confirm all instruments, recipes, music values and selector logic |
| Ceremonies | official description and source Ceremony API; release has music and startup parameters | partial | Totempedia Ceremonies; entries unlock by instrument | not run | map prerequisites, threshold, startup window, dance, output, duration and limits |
| Music measurement / dance | only 0.12.19 branch source currently accessible to connector exposes musicNeeded/maxStartupTime and startup-state logic; exact 0.12.18 implementation not retrieved | partial | Ceremony HUD/book | not run | tagged Java/resource subpaths return 404; obtain source archive or pinned JAR before source-exact contracts |
| Medicine Bag | 0.12.18 adds medicineBagBlacklist server option | partial | Totempedia Medicine Bag page unknown | not run | recipe, slots, allowed carvings, equip/use state and effect stacking |
| KubeJS integration | official release notes expand experimental registry/event API | partial | KubeJS docs and source builder | not run | inspect exact builders/events and confirm loaded plugin |
| World manipulation and farming | overview claims Ceremonies manipulate the world; source branch has a Zaphkiel Waltz growable block tag | partial | exact Ceremony pages unknown | not run | effect range, block tags, time/environment and repeatability |
| Server config / balance | release notes add Medicine Bag blacklist; other configs not swept | partial | server config defaults need inspection | not run | inspect defaults and TMM overrides |

## Guidebook onboarding

- **Guide name and access:** Totempedia, powered by Patchouli. Official page says Patchouli is recommended to receive the in-game guide, not a hard dependency of Totemic. Exact book recipe/drop or automatic grant is unknown.
- **Earliest attainable stage:** before the first complicated totem/Ceremony operation; exact item access must be confirmed.
- **First instructional quest:** “Open the Totempedia.” Teach how to obtain/open it and that Ceremony pages are conditional. Explain that carrying the relevant instruments can unlock pages, per the 0.12.18 release notes.
- **Reference map:** book categories include Ceremonies, confirmed by release notes, plus Totems and instruments in the overview. Exact page titles and diagrams need inspection.
- **Where the guide is insufficient:** no comparison yet against the pinned guide. Quest text must carry any missing pole geometry, carving sequence, instrument order, startup time, music threshold, dancing instructions and failure cues.
- **Other in-game help:** 0.12.18 adds a visual Ceremony music bar and fixes overflow; confirm what the display means in client.

## Systems and full mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and source |
| --- | --- | --- | --- | --- |
| Totem pole | build Totem Base and pole blocks; exact recipe/geometry open | wood/material ingredients and Totem Whittling Knife for carving (verify) | passive carving effects and Ceremony anchor | range/stacking/height requirements uninspected |
| Totem Carvings | acquire/apply carving to pole; exact interaction order unknown | carving item or knife action; costs unresearched | passive effect nearby; possibly transferable to Medicine Bag | 0.12.18 refactors carving/effect classes; exact effects need audit |
| Music instruments | craft/obtain instruments; release confirms Wind Chime and Jingle Dress entries | instrument item, placement/use/playing inputs; exact recipe and score unknown | Ceremony music and Totempedia unlocks | Ceremony selectors and per-instrument music not mapped |
| Ceremonies | activate at totem; satisfy instrument selectors, music threshold and dance | source API fields musicNeeded and maxStartupTime; exact per-Ceremony values unknown | effects on player, entities, blocks or environment | source API confirms a bounded startup time and threshold; failure penalty needs source/test |
| Ceremony progress/HUD | engage selected ceremony and play | active performance and instrument events | visible music bar and state feedback | release fixes overflow; reset behavior not established |
| Medicine Bag | craft/configure/carry bag; details unknown | bag + carving items; server blacklist | portable set of effects from allowed carvings | release adds blacklist, showing a configurable selection boundary; slots and stacking unknown |
| KubeJS data and events | built-in integration for 1.20.1+, confirmed in 0.12.18 changes | scripts register instrument/carving/ceremony definitions and callbacks | custom registries/effect modifications | integration explicitly experimental; no script changes authorized or made |
| Growth/world effects | perform matching Ceremony | totem + music/instruments + eligible blocks/environment | Ceremony-specific world changes; source branch has a Zaphkiel Waltz growable tag | exact range, tags and block operation need pinned-source data |
| Mob/player effects | run applicable Ceremony or carving | totem, instrument/music and nearby target | buff/debuff/effect | selector, ownership, filters, range and duration not inventoried |
| FE/mana/fluid/XP | no shared energy channel established | unknown | unknown | don't infer energy from music |

## Material / capability input-output contracts

| System | Input kind + exact ID/tag/event | Input qty/formula; consumed/remainder | Output kind, qty and location | Trigger/cadence; range/capacity | Automation/interface and evidence |
| --- | --- | --- | --- | --- | --- |
| Totem carving | block/totem base plus carving event/item | exact stack and tool wear unknown | persistent carving block state | player interaction | manual block event; data/API needs source review |
| Ceremony start | totem state + Ceremony selection + instrument presence | source defines musicNeeded and maxStartupTime; units need inspection | transition from startup to active Ceremony | player initiation with bounded startup timer | source API fields; branch 0.12.19 alpha, not pin-exact |
| Music input | playing/placed instruments within Ceremony scan region | per-instrument output and period unknown | progress toward threshold, then Ceremony active | timed music/event accumulation; radius unknown | exact placement/automation not established |
| Dance | player movement/actions during active ceremony | sequence unknown | contribution or pass condition | player event while active | test required; item possession does not prove operation |
| Medicine Bag | bag slots + carving items | count, blacklist and costs unknown | portable carving effects | equip/use tick | 0.12.18 server config option controls blacklist |
| KubeJS Ceremony | registry builder + effect callback | music target/startup time/duration are script-configurable per release notes | custom Ceremony or modified effect duration | data-load/registry and ceremony effect ticks | exact script syntax needs docs |

### Acquisition, renewability and output economics

- Totemic progression is gated by instruments and performance. 0.12.18 intentionally hides Ceremonies until the required instruments are obtained, so the book acts as a progressive reference.
- Carvings may be passive effects and portable Medicine Bag effects. Audit stacking and allowed items before recommending many effects at once; the server blacklist exists to constrain some carvings.
- Ceremonies may manipulate entities or blocks; distinguish actual Ceremony operation from merely owning instruments or book entries. Exact affected blocks and radius are unknown.
- Music is a Ceremony threshold, not a generalized FE/mana resource. Do not assume instruments fuel another mod.
- Built-in KubeJS makes Totemic extensible, but 0.12.18 calls that integration experimental. This research pass does not create recipes, carvings or Ceremonies.

## Complete player progression / walkthrough blueprint

This is a structured draft, not a source-complete quest handoff. Exact recipes, pole patterns and Ceremony operations must be transcribed from the pinned Totempedia/source data.

| Path and step | Gate or choice? | Exact prerequisite / materials / world condition | Player actions: obtain, build, activate, use | Unlock / visible success / next step | In-game guide section or JEI reference | Proposed quest/how-to, detection and reward | Evidence / coverage |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 0. Open Totempedia | required teaching | Patchouli present; item route unknown | obtain/open book, review Totems and Instruments | learn core vocabulary | Totempedia | access quest; item proxy for opening; XP + safe starter material | access not confirmed |
| 1. Gather basics | required | native materials and recipes unknown | obtain Totem Base, knife and instrument materials | ability to build/carve | Totempedia/JEI pages unknown | teach acquisition and crafting; items proxy; XP + wood/tools | recipe inventory needed |
| 2. Build a Totem | required | exact blocks/shape/height/anchor unknown | place base and poles as documented | valid totem pole | Totempedia Totems diagram | build-and-verify; possession is not proof; XP + safe construction supplies | guide data not checked |
| 3. Carve first effect | core action | knife and available carving | apply carving; stand within documented radius and observe | passive boon | Totempedia Carvings | teach target and success cue; block state/manual detector; XP + carving material | effect/range unknown |
| 4. Learn instruments | progression gate | instrument recipes/collectibles | craft/find and test instruments | Ceremony entry unlocks when relevant instrument is in inventory | Totempedia Instruments; release notes describe inventory unlock | instrument item plus page link; XP + safe material | list and unlock map needed |
| 5. Start a Ceremony | required operation | totem, chosen Ceremony and selector instruments | begin before startup time expires; play instruments | HUD progress starts | Totempedia Ceremony page | teach start and timing; Ceremony event detector if exposed; XP + useful instrument supply | exact operation unknown |
| 6. Perform and complete | required | music threshold plus Ceremony-specific dance | play to threshold, perform actions, verify effect | effect executes | exact Ceremony entry | teach precise action; authentic operation task if available; XP + thematic item | thresholds/effects incomplete |
| 7. Ceremony branches | optional choices | instrument subsets | obtain selectors and perform chosen Ceremonies | growth/combat/weather/utility | unlocked Totempedia entries | one how-to per useful Ceremony, not each recipe; XP + safe item | full list required |
| 8. Medicine Bag | optional utility | recipe, permitted carvings and slot capacity unknown | configure bag, load carving, equip/use and observe | portable effect | Totempedia Medicine Bag and JEI | teach blacklist/effect removal; state detector; XP + bag materials | recipe/capacity unknown |
| 9. Advanced/capstone | optional; terminal objective unknown | relevant instruments and Ceremonies | complete selected advanced operation | no mandatory endpoint established | advanced Totempedia entry | optional capability survey; no forced all-Ceremony completion; XP + safe reward | terminal advancement not mapped |

### Dependency graph and alternate routes

**Totempedia → materials + Totem Base/knife → pole carving → passive boon → instruments → instrument-gated Ceremony entries → start ceremony → meet music threshold within startup → perform effect.**

Medicine Bag is a separate portable-effect branch. Ceremonies appear to be instrument-dependent choices rather than one linear tier path; exact gate graph must come from the 0.12.18 guide/advancement data. Avoid requiring every ceremony or instrument if native progression allows branch choices.

**Native gate vs TMM gate:** no TMM progression lock, recipe change or custom ceremony is included in this profile. Totempedia page locks are native UI progression, not quest gates.

## Internal synergies and low-hanging setups

- **Passive carving:** once a safe early carving is available, the pole may provide a nearby boon without playing a Ceremony. Confirm effect and range.
- **Music performance:** a small instrument set can unlock and perform at least one Ceremony; choose a forgiving option after reading its requirements. Exact instruments and thresholds need investigation.
- **Medicine Bag:** portable effects may help avoid carrying a full pole, but check slots, blacklist, carving eligibility and stacking rules.
- **Farm utility:** investigate growth Ceremonies and the Zaphkiel Waltz tag before claiming crop automation. Verify affected blocks, speed and resource costs.
- **KubeJS extensibility:** future scripts could define instruments, carvings and Ceremonies, and modify effects. Integration is experimental; exact APIs need testing and a separate implementation request.

## Configuration, APIs and current pack behavior

- Pack pin is mods/fenns_totemic.pw.toml. Exact Modrinth release confirms 1.21.1, NeoForge and client/server.
- Totemic does not require Patchouli per its project page; it recommends it for Totempedia. Current pack has a Patchouli pin, but book visibility/acquisition is untested.
- Exact 0.12.18 release adds server config medicineBagBlacklist and notes that inventory presence of relevant instruments unlocks previously locked Instrument/Ceremony entries.
- 0.12.18 explicitly calls its KubeJS integration experimental. Release notes expose instrument, carving and ceremony registries via StartupEvents.registry keys totemic:a_instrument, totemic:c_totem_carving and totemic:d_ceremony; TotemicEvents.modifyTotemCarvings; and TotemicEvents.ceremonyEffectTick for effect duration changes.
- Official current source branch has progressed to 0.12.19 alpha, so internals must be rechecked against the pinned 0.12.18 release.
- No Totemic KubeJS script, server config, custom item or quest was changed.

## Progression-to-quest coverage matrix

| Stage | Teaching objective and action | Guide/JEI reference | Authentic completion / FTB detector | Reward direction | Coverage |
| --- | --- | --- | --- | --- | --- |
| Guide | access/open Totempedia; explain instrument-gated pages | Totempedia intro | book item proxies open | XP + safe build materials | partial |
| Pole | place base and correct geometry | Totempedia Totems | block state/manual inspection | XP + safe wood/material | partial |
| Carving | apply carving and observe boon | Totempedia Carvings | item is proxy; effect/state needed | XP + practical supply | partial |
| Instruments | acquire/play starter instruments | Totempedia Instruments | inventory items show unlock, not play | XP + safe instrument part | partial |
| Ceremony startup | start selected ceremony before timer expires | ceremony page/HUD | Ceremony state/event needed | XP + instrument supply | partial |
| Ceremony completion | reach threshold, dance, observe effect | exact ceremony page | actual event/effect; possession insufficient | XP + thematic item | partial |
| Medicine Bag | load allowed carving and use portability | Totempedia/JEI | bag possession insufficient | XP + safe bag materials | partial |
| Advanced branch | teach one useful selected Ceremony | specific entry | actual Ceremony event/output | XP + non-bypass reward | partial |

**Reward policy:** give positive XP and meaningful fixed items. Reward instrument supplies or construction materials rather than granting the completed boon. Instrument acquisition proves possession, not music played or Ceremony completion. No quests were edited.

## Open questions and next actions

1. Inspect pinned JAR against SHA512 and extract metadata, recipes, Totempedia JSON, advancement graph, loot and config.
2. Exact 0.12.18 release tag/commit is now located and Gradle metadata matches the pin. [ ] Compare code/data and pinned JAR. Tagged Java/resource paths return 404 through the available GitHub file connector; do not reuse 0.12.19 mechanics as pin-exact.
3. Complete Totempedia and acquisition map; map each instrument to the Ceremony pages it unlocks.
4. Document Totem Base/pole construction, carving recipes, tool costs, stacking, ranges and passive effects.
5. For every Ceremony, record selector instruments, music threshold, startup window, dance, failure/reset cost, effect, radius, duration, conditions and valid targets.
6. Audit Medicine Bag recipe, slots, selection, blacklist, stacking, unequip and config.
7. Inspect installed pack's KubeJS integration. No scripts are included in this pass.
8. Determine whether FTB tasks can detect ceremony start/completion or only proxy by output/effect.

## Test plan — not run

No tests were run; this is knowledge Markdown only.

- **Positive:** build one documented pole and apply an early carving; record recipe, state, effect, radius and duration. **Boundary:** invalid placement/target and just outside effect range.
- **Positive:** start a Ceremony with its selector instruments and complete music/dance; record startup timer, threshold, sequence and effect. **Boundary:** omit selector, exceed startup window, or stop before threshold; record reset/resource costs.
- **Positive:** add relevant instrument to inventory and verify its Totempedia page unlocks. **Boundary:** remove after unlock and test visibility persistence.
- **Positive:** load allowed carving into Medicine Bag and equip/use. **Boundary:** attempt blacklisted carving with server config behavior noted; check item consumption.
- **Positive:** run growth Ceremony on valid block. **Boundary:** invalid/out-of-range block, no target, chunk unload.
- Verify KubeJS 0.12.18 scripts/events in a disposable world. Do not author pack scripts.

## Evidence ledger

- Pack pin mods/fenns_totemic.pw.toml: artifact, Modrinth project/version and SHA512 [pin].
- [Official Modrinth release page for 1.21.1-0.12.18](https://modrinth.com/mod/fenns_totemic/version/1.21.1-0.12.18): Release, NeoForge, client/server; exact Totempedia lock, Medicine Bag config and KubeJS notes.
- [Official Totemic project page](https://modrinth.com/mod/fenns_totemic): focus on totems/music/nature, passive boons, Ceremony description, optional Patchouli/Totempedia and built-in KubeJS.
- [Exact source release tag](https://github.com/TeamTotemic/Totemic/tree/1.21.1-0.12.18), commit [`1a253b29815842aa4359d6118b8cd84cce3048a1`](https://github.com/TeamTotemic/Totemic/commit/1a253b29815842aa4359d6118b8cd84cce3048a1): root `gradle.properties` confirms version 1.21.1-0.12.18 and MC 1.21.1.
- Release-tag source limitation: GitHub connector retrieves root files at the tag but returns 404 for Java/resource paths, including Ceremony and Totem block/entity code. The public `1.21` branch declares 0.12.19 alpha; its code remains version-adjacent context only and is not treated as exact-pin evidence.
- Pinned JAR inspection, exact-tag source file review, client/world test, Ceremony operation and TMM config/script review: not performed.
