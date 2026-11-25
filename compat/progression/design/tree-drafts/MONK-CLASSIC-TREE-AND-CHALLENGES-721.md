# Classic Monk — draft v1 — #721 / #722

2026-11-25. Proposed design, not implementation or approved final allocation. Based on exact dev/progression 4a027c5f0e78af0cc08bb004e1bbb4ca3688a42a, original 21-stage inventory (compat/progression/design/monk-original-21-gate-inventory-v1.json), current 72-skill crosswalk and MONK-CLASSIC-VANILLA-BENCHMARK-721.md. Prior owner decisions control; new utility actions and proposed criteria below are for review. One point per purchased rank. Challenge entitlements cost zero, do not count as investment, do not increase cost. Initial wood speed is explicit one-off exception to qualitative rewards. Ordinary training XP unchanged; fixed challenge MXP disposition remains undecided.

## Purchased tree

| Family | Skill | Ranks | Additive rank increment | Endpoint |
|---|---|---:|---|---|
| Martial | Iron Palm | 5 | +3.8 HP | 20 HP raw from base 1 |
| Martial | Fist Tempo | 2 | +0.4 attack speed | 4.8 |
| Martial | Sweeping Palm | 3 | +25 pp sweep scaling | 75% |
| Martial | Forceful Palm | 2 | +1 knockback level | II |
| Martial | Reaping Palm | 3 | +1 native Looting level | III |
| Body | Natural Armor | 10 | +2 armor | 20 |
| Body | Natural Toughness | 6 | +2 toughness | 12 |
| Body | Vitality | 5 | +4 HP | 40 HP |
| Body | Protection | 8 reserved | TBD | General protection only; exact layers/cap pending |
| Body | Rooted Body | 4 | +0.1 knockback resistance | 0.4 |
| Body | Regeneration | 4 | +0.2 HP/s | 0.8 HP/s |
| Body | Hunger Discipline | 5 | -10 pp exhaustion | -50% |
| Body | Retribution | 3 | +1 Thorns-equivalent level | III |
| Movement | Swift Step | 8 | +0.01 walk speed, doubled intrinsic bonus on sprint | 0.18 walk /0.26 sprint before normal sprint modifier |
| Movement | Monk Leap | 2 | +0.1 jump impulse | 0.62 |
| Movement | Depth Strider | 3 | +1/3 water movement efficiency | 1 |
| Movement | Silent Step | 3 | +0.15 sneak ratio | 0.75 |
| Movement | Soul Step | 3 | +1 Soul Speed equivalent level | III |
| Earthly | Hand Mining | 9 | +47/9 effective tool speed units from wood trial baseline 2 | 49 |
| Earthly | Fortunate Hands | 3 | +1 native Fortune level | III |
| Magical | Inner Reservoir | 5 reserved | Provider-specific values TBD | Personal capacity |
| Magical | Inner Wellspring | 5 reserved | Provider-specific values TBD | Personal recovery |
| Magical | Inner Thrift | 5 reserved | Provider-specific values TBD | Cost efficiency |
| Magical | Inner Potency | 5 reserved | Provider-specific values TBD | Spell effectiveness |

Totals: 15 martial +45 body +19 movement +12 earthly +20 magic =111 provisional ranks, including 28 reserved for protection/magic. 9 unallocated against provisional 120 planning budget, not purchasable filler. Exact Classic budget is final sum, Unlimited =1000−Classic, transcendence final point. No absorption, specialized weapon damage, separate fire/fall or damage-type protection ranks. Magic registry evidence and Core overlap unresolved. Every rank meaningful; additive increments use reference baseline/endpoint, not compounding current value; exact 10% criterion remains owner decision. Mining speed must use native effective tool/block speed; no drop eligibility before relevant challenge. Classic has no paid reach extension. No claim that all listed handlers exist. Native loot and sweeping/thorns parity require audits.

Proposed purchase layout: all families available on initiation, initial Iron Palm/Armor/Hand Mining available without challenge deadlocks; no sunrise, sprint or undead-kill gates for quantitative training skills. Exact other skill prerequisites/order TBD; do not make the player fight Wither without ability to buy Classic defense. Phase gate at Classic mastery + Ender Dragon + required Classic challenges, with configurable Go No Further. No final allocation or stopping policy ratified.

## Journey — revised sequential non-block-breaking challenges (owner correction 2026-11-25)

Previous Nov 23 decisions: sunrise OR sunset initiation, generous window; remove separate sunrise and sprint trials, replace void trial with strict Wither glide. Owner now restores underwater trial (ten primary Journey challenges after initiation). Current pinned monk.json actually has sunset-only timed enrollment (12000–13000, 200 ticks), so reconcile the prior sunrise-or-sunset decision separately, not a second challenge. VerifiedSprintingXp observes genuine on-foot horizontal sprint distance server-side and TrainingRewards uses configured base 1 MXP/block. Keep repeatable training, no sprint or movement skill gates. Hunger Discipline, Swift Step, Monk Leap and martial ranks are purchases, no sunrise, sprint, or undead-kill challenge gates. Classic Hunger Discipline reduces exhaustion; its advanced counterpart periodically grants small amounts of saturation instead of further hunger-loss reduction, cadence and amount pending. No Blink or Withering Stare unlocks in Classic; no respective Enderman or Wither Skeleton challenge. Advanced disposition not decided.

| Order | Qualifying deed | Reward |
|---:|---|---|
| 0 | Existing sun meditation initiation | Enrollment, Reserved Fist and Armor Lock |
| 1 | Remain continuously submerged 280 ticks (14 seconds; proposed deterministic original duration, no actual drowning required) | Full Water Breathing |
| 2 | Pet 20 distinct eligible animals barehanded | Animal Friendship, hidden trial access |
| 3 | Dodge 5 skeleton arrows | Arrow Catching and Step Assist |
| 4 | Moon meditation from boat, 200 ticks | Water Walking |
| 5 | Walk 10 blocks through fire under controlled conditions | Full fire/lava immunity |
| 6 | Pacify a creeper barehanded | Creeper Kiss, hidden trial access |
| 7 | Caves & Cliffs full-height fall, see below | Full fall damage immunity |
| 8 | Sleep and wake on adjacent exposed bedrock at night | Bedrock Rest, hidden trial access |
| 9 | Activate trial blindness, kill 5 hostiles barehanded | Night Vision |
| 10 | Strict verified barehand Wither victory | Purification and Momentum Glide |
| Finale | Ender Dragon + Classic purchased mastery + required challenge completion | Classic mastery/Go No Further / Unlimited entry |

Owner restores the underwater trial as first Journey challenge. Exactly ten primary challenges after enrollment; underwater duration and deterministic conditions are proposed pending review. Exact tool/magic branch predicates remain proposals. Preserve ordered original relative sequence for retained original deeds and current objective HUD. No duplicate award or purchase credit. Ten primary Journey challenges are the current design; independent harvest, tool and magic challenges do not count toward ten.

### Caves & Cliffs fall trial

Owner replaces 40-block hay-bale fall with vanilla Caves & Cliffs, no hay landing requirement. Exact 1.21.1 advancement JSON inspected: https://github.com/misode/mcmeta/blob/1.21.1-data/data/minecraft/advancement/adventure/fall_from_world_height.json ; advancement minecraft:adventure/fall_from_world_height, criterion fall_from_world_height, trigger minecraft:fall_from_height, start Y>=319, end Y<=-59, vertical displacement >=379. Vanilla triggers on landing. Use server-verified native-equivalent continuous fall (Overworld default height; exact dimension and datapack-height policy TBD). When trial is current and full fall conditions satisfied, protect from qualifying landing damage BEFORE damage resolution; preserve native fall start/landing tracking so vanilla criterion fires; if hook ordering prevents that, grant exact criterion only after trusted verified qualifying landing. Grant durable fall immunity and advancement together, even if advancement was earned before Monk (fresh qualifying trial should not depend on advancement-earned event firing again). Never give general temporary fall immunity or cancel arbitrary lethal damage, void damage, unrelated falls, teleport/flight spoof, or award for simply having advancement. Verify native trigger/event order in 1.21.1 engine and test both previously earned and first-time cases. No runtime implementation or QA yet.

### Bedrock rest source finding

BedrockRest.java at baseline explicitly calls startSleeping rather than startSleepInBed; no spawn reset. Two exposed adjacent bedrock blocks, safe area, night/thunder, bed-valid dimension, true sleep vote, resets insomnia and triggers slept-in-bed after successful rest. Preserve existing respawn point. Verified in source only.

## Harvest cultivation — independent semi-dynamic material graph

| Trial | Award |
|---|---|
| 5 logs barehanded | Wooden-tool-equivalent speed 2 on relevant blocks; no harvest eligibility |
| 5 stone barehanded | Wooden harvest entitlement |
| 5 blocks requiring stone (iron/copper ore) | Stone eligibility |
| 5 blocks requiring iron (diamond ore) | Iron eligibility |
| 5 blocks requiring diamond (obsidian) | Diamond eligibility |
| 5 blocks uniquely newly eligible with material X, predecessor unlocked | Native material X eligibility; equivalent tiers collapse, incomparable branches supported, overrides for special tools |

Count genuine no-drop removals; never award speed with subsequent material tiers or grant eligibility early. Vanilla harvesting completion is the Classic baseline; modded tier challenges may continue independently subject to native/pack gates and configurable Classic-only behavior (owner decision).

## Tool utility and magic — proposed parallel challenge branches

These are independent of the ordered original journey. Owner selects four block-breaking tool trials, each 20 qualifying barehand block removals, rather than right-click actions. Award the corresponding tool action only on completion; no hidden tool unlock needed for break-based trials. Other utility/magic predicates remain proposals. Use explicit selectable action mode rather than overriding normal right-click behavior; prevent accidental block transformation, conflict with Animal Friendship/Bedrock Rest, use native action and loot context, claim permission, correct tool family and relevant material eligibility. No durability item or special modded item powers; no double output or simultaneous Fortune/Silk. Block eligibility not bypassed by tool action; material/tool rules still govern drops. Mode UI/keybind details and exact predicates need owner review.

| Ability | Proposed trial and prerequisite | Entitlement |
|---|---|---|
| Glass Touch | Break 5 glass blocks after wooden harvest | Glass/pane harvest only, no universal silk yet |
| Silk Touch | Successfully harvest 5 glass blocks with Glass Touch | Toggle native Silk Touch I vs Fortune; no paid Silk ranks |
| Shearing Hands | Break 20 cobwebs barehanded | Native shears entity/block actions, leaves/vines/cobweb correct drops and pumpkin carving; no breeding while shearing |
| Earth Shaping | Break 20 dirt blocks barehanded | Native shovel path and campfire extinguishing |
| Cultivating Hands | Break 20 tall grass plants barehanded (propose count short_grass and tall_grass; one double-height plant counts once) | Native hoe tilling |
| Wood Shaping | Break 20 logs barehanded | Native axe stripping; scraping/dewaxing optional separate vanilla action review |
| Aqua Affinity | After full breathing, break 5 eligible underwater blocks | Remove submerged mining penalty (no airborne penalty change) |
| Otherworld Sight | First completed consumption of Demon's Dream Fruit (`occultism:datura`) while enrolled as Monk | Intrinsic Otherworld Sight (Classic), tier-two texture reveal / tier-one native harvest, no shader or potion refresh |
| Iesnium Palm | First actual entry into `minecraft:the_nether` while enrolled as Monk, independent of fruit milestone | Barehand native Iesnium harvesting entitlement; existing sight and correct-for-drops conditions still apply to actual ore harvest unless owner approves relaxing them |

Existing Treasure Hand folded into Silk/Fortune switch; Fortune purchase grants integer native levels, no free Fortune I duplicate. Owner-selected 20-block requirements supersede five right-click trials. Proposed tag choices: #minecraft:logs (20), minecraft:dirt (20), minecraft:cobweb (20), minecraft:short_grass or minecraft:tall_grass (20 plants; count double-height removal once). Literal tall_grass-only vs both grass variants remains to confirm. Count genuine no-drop breaks, no canceled actions. No tool-action entitlement until completion. Proposed tool trials count from enrollment in parallel, including overlap of first five logs with wooden speed challenge, so no redundant log breaking. No extra prerequisites such as Animal Friendship or wood tier unless owner decides. Hoe's tall-grass training is thematic, not a claim of native hoe-specific grass drops. Tool action mode remains proposed. Invisibility optional separate trial pending owner; no absorption. Vein mining, radial mining, Elixir of Life and additional advanced magic/flight remain deferred to Unlimited challenge inventory, rather than silently dropping active mechanics. Existing Elixir absorption conflicts with new no-intrinsic-absorption design if interpreted globally; resolve before changing existing effect. Catch Arrows, Water Walking and retained original abilities remain fixed challenge powers. Blink and Withering Stare removed from Classic.

## Implementation readiness — owner-directed Occultism unlocks, 2026-11-25

Design now sufficient to begin bounded Classic engine implementation independent of Unlimited balance. Existing #611 native adapter is present at dev/progression 4a027c5f0e78af0cc08bb004e1bbb4ca3688a42a, verified earlier against pinned Occultism 1.227.0 binary. Do not duplicate adapter or change Lich/non-Monk behavior. Source confirms:

- `ChannelEngine.view` projects achievement-backed skills automatically from saved achievement/milestone IDs, outside purchased ranks; `EffectRuntime.active` gates them on active eligible path. Reuse for free entitlements; no zero-price paid skill. Existing `requires_milestone` effect parameter can gate effect use; hidden trial entitlement mechanism still needs exact design/implementation.
- `ProgressionEvents.consumedFood` calls `ProgressionApi.recordConsumedFood` on food-use finish, which records `food_consumed` by item selector for joined paths. Occultism upstream 1.21.1 registry `OccultismItems.DATURA` registers edible `occultism:datura` ([source](https://github.com/klikli-dev/occultism/blob/version/1.21.1/src/main/java/com/klikli_dev/occultism/registry/OccultismItems.java)); confirm exact installed registry item during acceptance. Fruit must actually be consumed, not just obtained. Repeat does not re-award. Retain ordinary fruit effect on that first consumption, permanent sight itself is shader-free. Do not alter non-Monk or Lich.
- Native advancement `minecraft:story/enter_the_nether` ([1.21.1 definition](https://github.com/misode/mcmeta/blob/1.21.1-data/data/minecraft/advancement/story/enter_the_nether.json)) triggers on change to `minecraft:the_nether`, but advancement-event-only is insufficient when the player already has it. Existing server PlayerChangedDimensionEvent callback clears/reconciles runtime state, emits no dimension milestone. Add reusable verified destination-dimension signal to that callback, respecting active Path and eligibility, not repeat reward. Use dimension-entry signal for Iesnium independent of fruit. Proposed first qualifying action while enrolled, not retroactive pre-Monk fruit/Nether history; confirm if owner wants historical credit. Current `OtherworldHarvest` enforces sight tier 2, empty hands, `HarvestAbilities.canHarvestBarehand` and separate tier-2 ore entitlement; keep eligibility and native loot, no bypass. If player enters Nether before eating fruit, entitlement is saved but ore remains hidden until sight; if harvest tier insufficient, no drops. Unlock may be early without bypassing actual ore prerequisites. Scope of dropping tier requirement remains an owner decision if intended.
- Strict sequential counters need a generic opt-in: current `ProgressionApi.recordFiltered` collects all matching counters regardless of `after`, then checks prerequisites only on completion; `MilestoneFeedback.next` explicitly says this. Existing paths rely on old pre-count semantics, so default unchanged; opt-in strict milestones must ignore out-of-order actions, activate only after prior milestone already complete at signal start, keep parallel harvest/tool/fruit/dimension counters independent. Existing milestone feedback is transient actionbar, not persistent bottom-right HUD. Use existing title/progress metadata and authoritative sync, add HUD in separate engine slice after audit.

### Independently actionable slices

| ID | State | Scope / acceptance |
|---|---|---|
| E1 | ready-to-code | Generic opt-in strict ordered milestone collection, no pre-counting or same-event cascading; preserve legacy semantics and parallel milestones; unit tests for before/after/same signal/reload. Progression-owned engine PR from dev/progression, no path edits. |
| E2 | ready-to-code | Generic verified server dimension-entry criterion; destination exact registry ID, no client trust, no spectator/creative reward, repeated entries idempotent via milestone ledger; tests including prior native advancement and nonmatching dimension. Progression-owned engine PR or coherent E1 engine workstream if same writer and review boundary. |
| C1 | ready-to-code (data scope, depends on existing achievement projection; Nether criterion depends E2) | Convert Monk Occultism sight/palm from paid skills to one-time achievement-backed entitlements; completed fruit and Nether entry independently, no rank/point charges. Keep live current tree untouched until coordinated replacement/migration; stage in isolated Classic config PR after E2, fruit can be tested independently. Native exact JAR gameplay QA later. |
| E3 | needs-research | Hidden trial access and durable HUD; audit achievement projection and sync, trial eligibility, no spillover into later challenge. |
| E4 | needs-research | Full-height fall verifier and native advancement hook, native tool actions/harvest graph, remaining trial detectors, true Fortune/Looting and sweeping. |
| D2 | needs-decision | General Protection and basic magic provider numerical endpoints, exact Classic allocation, remaining utility selectors, phase/stop policy and existing-save migration. Does not block E1/E2. |

This is authorization to scope approved behavior, not a claim of runtime completion; do not launch a Minecraft full build without explicit per-run authorization. No code changes in this research PR. Preserve active QA fixes and exact source ownership. All existing paid-rank migration and legacy fixed trial MXP policy must be resolved before replacing live Monk path JSON.

## Work / verification

No executable edits. New generic engine capability survey: ordered challenge prerequisites, trial vs permanent entitlements, unique entity action tracking, safe fall trial, timed/look/movement detectors, challenge HUD/sync, separate harvest graph, native virtual tool-action execution and loot, true Fortune/Looting levels, sweeping/thorns, protection math and native attribute stacking, migration from current automatic/purchased skills. Verify which existing code already handles these before opening engine PRs. Offline source review only; no compile/QA/build authorized. Pending owner decisions: exact challenge grouping/predicates, purchase gates, tool modes and utility scope, general Protection, magic increments, exact Classic total, 10% criterion, Classic-only harvest policy, capstone and external effect stacking. The bounded E1/E2 and C1 slices above are now scoped; all other engine/config work remains subject to its actual dependencies and owner review. Earlier references to removed challenge gates, if any, are superseded by the revised Journey section. 
