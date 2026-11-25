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
| Otherworld Sight | Occultism native sight/ritual trial TBD | Intrinsic Otherworld Sight (Classic) |
| Iesnium Palm | Otherworld Sight + appropriate harvest entitlement, native requirement verified | Barehand native Iesnium harvesting (no generic bypass) |

Existing Treasure Hand folded into Silk/Fortune switch; Fortune purchase grants integer native levels, no free Fortune I duplicate. Owner-selected 20-block requirements supersede five right-click trials. Proposed tag choices: #minecraft:logs (20), minecraft:dirt (20), minecraft:cobweb (20), minecraft:short_grass or minecraft:tall_grass (20 plants; count double-height removal once). Literal tall_grass-only vs both grass variants remains to confirm. Count genuine no-drop breaks, no canceled actions. No tool-action entitlement until completion. Proposed tool trials count from enrollment in parallel, including overlap of first five logs with wooden speed challenge, so no redundant log breaking. No extra prerequisites such as Animal Friendship or wood tier unless owner decides. Hoe's tall-grass training is thematic, not a claim of native hoe-specific grass drops. Tool action mode remains proposed. Invisibility optional separate trial pending owner; no absorption. Vein mining, radial mining, Elixir of Life and additional advanced magic/flight remain deferred to Unlimited challenge inventory, rather than silently dropping active mechanics. Existing Elixir absorption conflicts with new no-intrinsic-absorption design if interpreted globally; resolve before changing existing effect. Catch Arrows, Water Walking and retained original abilities remain fixed challenge powers. Blink and Withering Stare removed from Classic.

## Work / verification

No executable edits. New generic engine capability survey: ordered challenge prerequisites, trial vs permanent entitlements, unique entity action tracking, safe fall trial, timed/look/movement detectors, challenge HUD/sync, separate harvest graph, native virtual tool-action execution and loot, true Fortune/Looting levels, sweeping/thorns, protection math and native attribute stacking, migration from current automatic/purchased skills. Verify which existing code already handles these before opening engine PRs. Offline source review only; no compile/QA/build authorized. Pending owner decisions: exact challenge grouping/predicates, purchase gates, tool modes and utility scope, general Protection, magic increments, exact Classic total, 10% criterion, Classic-only harvest policy, capstone and external effect stacking. No new ready-to-code slice claimed until engine audit and design acceptance. Earlier references to removed challenge gates, if any, are superseded by the revised Journey section. 
