# Challenge engine primitives — #721

Engine-only change; no current path definitions altered. All paths may opt in.

- Milestones accept boolean `strict_order` (default false). A strict milestone with `after` prerequisites collects only when they were complete at the start of the signal. One signal cannot satisfy consecutive strict steps. Legacy milestones keep pre-counting and completion semantics. Existing saved counters are not deleted; use fresh IDs or explicit migration when converting previously live milestones. Cyclic milestone prerequisite graphs now fail validation.
- Event `dimension_entered`: target is destination dimension registry ID (e.g. `minecraft:the_nether`); emitted on actual server dimension transition, not login or historical vanilla advancement. Creative/spectator/dead excluded, ordinary earning and path eligibility enforced. Count 1 for one-time entitlement; existing ledger handles idempotence. No client packets or fake advancement grants.
- Existing achievement-backed skills (`auto_achievements`) and `activation: {"mode":"toggle"}` are the foundation for reusable tool forms, not paid ranks. Tool forms must have toggleable effect gates; plan mutually exclusive active right-click tool forms (normal/off available), separate Fortune/Silk mode, no forced block modification. Actual native tool action handlers and persistent HUD remain subsequent engine slices. No Monk IDs introduced in engine code.
- Unit tests cover strict vs legacy vs parallel progress, no same-signal progress on next strict step, count caps, joined state, matching dimension, malformed flags and cyclic graph. Server event, persistence and multiplayer gameplay acceptance outstanding.

No full smoke, assembly, packaging or live gameplay run authorized. 

## Trial effect and exclusive toggle foundation

- On a single-rank skill backed by exactly one strict milestone in `auto_achievements`, set effect `params.trial_milestone` to that milestone ID. Only that particular effect is available while the milestone is active and incomplete; normal skill/effect eligibility, hand and armor restrictions remain authoritative. No owned rank, point cost, receipt or other effect is temporarily granted. The existing projected achievement rank enables the permanent skill after completion. Activation-bearing skill trial effects are rejected to avoid bypassing toggle authorization; use a separate deed effect if needed. Non-trial effects unchanged.
- `activation: {"mode":"toggle","exclusive_group":"example:tool_forms"}` opts into a path-scoped single-selection group. All group members must be free cooldown-free toggles. Turning one on atomically selects it instead of the previous member; turning the selected one off leaves normal mode. No simultaneous grouped effects or separate client-owned flags. Other toggles retain their old behavior. Server authorization, owned-rank and eligibility checks remain in SkillActivation. Separate groups permit Fortune/Silk and right-click tool forms to coexist. Exact native tool effects and UI selection still pending. Existing legacy modes not migrated until the coordinated path change.
- Tests exercise trial effect rank isolation, invalid trial configuration, completion-to-achievement projection model, grouped selection and invalid group configurations. Source only, exact-head Module CI pending. No live path change.

## Native tool forms and material trials — second implementation slice

- `tool_form` effect requires `empty_hand:true`, an explicit block target (ID/tag/*), and `params.form` in axe, shovel, hoe, shears. Shears may additionally specify `params.entity_target` exact ID/tag/* (default none). Requires free exclusive toggle activation; passive/trial bypass rejected. Server uses context-only vanilla tool stack with native ItemStack.useOn (NeoForge UseItemOnBlockEvent and placement rollback), never equips it. Shears uses native IShearable interaction before animal friendship, and normal Block.getDrops loot context for eligible shears blocks inside actual native player break. No second loot roll. Respect event cancellation/use denial, loaded position, border, mayInteract, survival build permission; claim integration and actual runtime behavior require B/C. Native source branch 1.21.1 confirms tool-modification events and shearing API; exact pin 21.1.265 module compile and mixin runtime pending. Pumpkin carving uses a block-side item-use method rather than ShearsItem.useOn; not yet implemented. Shears is passed to the original loot context on non-tool-required scoped blocks as well (e.g. leaves/vines), and on correct-for-drops required blocks only. Actual drop parity requires runtime verification; no blanket drops. Fortune on shears-context blocks is suppressed by existing empty-tool gates; legacy Silk mode takes precedence; future Silk toggle integration needs review.
- `native_harvest` effect, `empty_hand:true`, explicit block selector, `params.tool` exact registered representative item ID. Uses the actual item TOOL component and isCorrectToolForDrops, never equips it or invokes its special powers. Unknown/non-tool representative fails closed. Existing harvest_tier unchanged. New strict `material_harvest_trial` criterion target is exact tool ID, counts a committed native empty-hand block removal only if original block requires a correct tool, candidate can harvest it, and no active native capability can harvest it. Eligibility snapshot before removal; existing strict prerequisites and joined path enforce sequence; no tool needed to receive progress, no premature loot. Native material signals distinct from ordinary break and MXP. Configured representative candidates are a first step toward dynamic graph; registry enumeration, equivalence collapse, generated milestones/prerequisites, nonstandard tools, actual modded harvesting parity and migration not yet implemented. Existing vanilla numerical harvest path untouched. Native secondary/bulk mining may need an explicit verified producer adapter for material trials; not claiming it works automatically.
- Tests: NativeToolSchemaTest verifies toggle requirement, allowed forms, exact material tool and strict criterion. Pending exact-head compilation and native runtime verification; no live path definitions changed.

### Discovery graph planner

`NativeHarvest.discover(itemSelector,blockSelector)` is a bounded read-only server registry survey API (explicit item ID/tag and block ID/tag). It evaluates TOOL-backed representative stacks against default states of tool-required blocks, collapses equivalent native capability sets, computes strict-subset Hasse edges, and preserves incomparable material branches. Comparison budget capped; never run per tick. Pure HarvestCapabilityGraph tests cover equality, branching, transitive edge removal, empty sets and deterministic canonical selection. No automatic mutation of path definitions yet: the path compiler must call discovery after registry tags are bound (ordinary resource listener apply may precede tag binding), establish stable entitlement identity across mod/tag changes, generate strict milestone/skill definitions and validate overrides for special materials. Current exact-ID configured trials are usable before that integration. Default-state survey does not prove state-dependent/mod-specific native behavior. No automatic registry discovery invocation or installed tool survey claimed yet.

## Automatic harvest generation — third implementation slice

Opt-in raw Path declaration (example only; no bundled path changed):
```json
"harvest_generation": [
  {
    "id": "example:pickaxe_materials",
    "item_selector": "#example:representative_pickaxes",
    "block_selector": "#minecraft:mineable/pickaxe",
    "count": 5,
    "after": ["example:wood_speed"],
    "prefer_tools": ["minecraft:wooden_pickaxe", "minecraft:stone_pickaxe", "minecraft:iron_pickaxe", "minecraft:diamond_pickaxe"],
    "exclude_tools": [],
    "training_blocks": {"minecraft:wooden_pickaxe": ["minecraft:stone"]},
    "lane": "Harvesting",
    "unarmored": true
  }
]
```

- HarvestGeneration is a pure compiler consuming native fingerprints; PathRegistry defers installation of opt-in path catalog until logical-server TagsUpdatedEvent (SERVER_DATA_LOAD). NeoForge 1.21.1 ReloadableServerResources patch posts this event after updateRegistryTags and Blocks.rebuildCache. Ordinary catalogs without generation preserve immediate install path. Generated JSON is validated through normal PathJson, ChannelConfig and eligibility/presentation/resource validation before installation. Retain last valid catalog on validation failure; tag-event ordering, manager lifetime, failed reload behavior and integrated/dedicated lifecycle require runtime acceptance. No automatically invoked full server build.
- A generator scans explicit tool/block families, chooses preferred representatives or deterministic lexical IDs, excludes configured representatives, collapses equal default-state tool-required block sets, and generates one strict milestone + one single-rank achievement skill per novel group. The count defaults to five, after gates all nodes, all direct predecessor branches must be complete, no fixed XP or paid cost is issued. Tool family scope is retained on effect and exact qualifying block set stored separately in registry. Optional exact-ID training_blocks override must be a nonempty subset of the graph delta; use it for five stone rather than any wooden-tool block. Invalid/unmatched override fails closed; update it explicitly when removing a tool. Tool IDs cannot be shared across generators of one path or collide with manual material criteria. Extra special-material gates can be authored separately with manual milestones/effects/exclusions; per-material arbitrary overrides not implemented.
- Redundant union nodes with no new block beyond all predecessors are omitted and their actual dependencies forwarded. Training uses delta relative to the node's prerequisites rather than all currently active unrelated permissions; this intentionally permits credit on blocks covered by another independent/incomparable branch so the trial cannot softlock. Existing explicit material criteria retain their global native-harvest eligibility rule. Trial count snapshots before committed native break; no speed bonus or tool item; runtime state predicate checked again for each candidate. Current graph checks default states only, not specialized item/block-state loot behavior.
- Stable receipt UUID uses Path ID, generator ID and exact representative tool ID. Actual completion records receipts for each then-equivalent tool, in per-path milestones and economy. A later changed representative projects a completed canonical node from existing aliases without persisting new receipts; a later split does not falsely award a newly distinct tool merely because it was briefly equivalent. Removed/excluded tools and redundant omitted nodes retain historical receipts but have no current node/effect; when a tool is available as a distinct node again, receipt can reactivate. If previously-earned tool becomes redundant through new predecessor union, its old receipt is preserved but current permissions may need those predecessor challenges; this edge remains migration review. In-progress counters use canonical ID and may need retraining if representative changes. No automatic migration of old numeric harvest tier or preexisting saves; no reset of paid ranks. Keep generator IDs stable. No claims of actual installed survey or permanent save acceptance.
- Added HarvestReceiptTest and HarvestGenerationTest, plus graph preferred ordering and delta planner. Actual live generated tree, tag reload/startup, installed representative tag contents, unarmored eligibility, native special loot and save/relog require B/C tests after an approved configuration and candidate. No bundled Monk config changed. Prior checkpoint 7d16c09 and alias foundation a2aca1e passed exact-head quick CI; generator checkpoint f12cdc2 still running at documentation time.

## Persistent personal challenge HUD

Opt-in path configuration:
```json
"challenge_hud": [
  {"id":"journey","title":"Monk's Journey","title_key":"example.hud.journey"},
  {"id":"harvest","title":"Harvesting"}
]
```
A milestone opts into one lane:
```json
"hud": {
  "group":"journey",
  "objectives":[{"text":"Remain underwater for 14 seconds","key":"example.hud.underwater"}]
}
```
- The objective list must match criterion order/count; optional client translation keys use literal fallback when missing. This is presentation only, not a new criterion or award. PathRegistry validates all groups and objectives against the fully expanded path before publishing. Existing paths without opt-in have no HUD. One first eligible incomplete objective per lane in definition order; simultaneous independent lanes, prerequisites and projected material aliases respected. A count >1 displays current/total; single-action trials show text only. Completed or blocked lanes disappear. Generated families can opt in with `hud_group:"harvest"`; objective fallback derives from actual training delta, including up to two example block IDs. Generated English fallback is not automatically translated; data-authored objectives support localization. Keep descriptions short.
- The logical server reads existing authoritative Track/counter state, filters inactive/unjoined paths, sends a one-way bounded snapshot (max four rows total, sorted path ID and declared lane order), on change within five player ticks and one-second heartbeat while visible. No client action can award progression; no additional saved state. Login/respawn/dimension resync, logout and server-stop cache cleanup. Client owner identity and 40-tick expiry prevent stale cross-world rows; F1, open screen, death/creative/spectator suppress rendering. Lower-right layout above hotbar, bounded wrap/height; very small scaled windows may hide/truncate rows. No in-game overlay positioning control or user preference toggle beyond vanilla hide-GUI; test other HUD mods for collisions. Current trial counters need not represent continuous elapsed time; only authoritative existing criterion counts are shown.
- Tests: ChallengeHudSpecTest (strict sequence, parallel lanes, progress, completion, dormant path, invalid metadata), HarvestGenerationTest (generated lane with actual delta and equivalent receipt). Existing generator code/receipt checkpoints and documentation checkpoint 2f0cc4b quick checks passed; current HUD CI pending at this checkpoint. A/B/C: verify compiled payload and parser; after authorized build, actual server/client sync across join/leave, out-of-order trial, completion, dimension, respawn, relog, /reload, generated materials and multi-player isolation; inspect bottom-right positioning and scale with installed overlays. No bundled config enabled yet, no full build/smoke/gameplay acceptance.


## Reusable height-fall challenge (Leap of Faith)

Opt-in strict single-deed milestone example (not installed in the bundled Monk path):

```json
{
  "id": "example:leap_of_faith",
  "strict_order": true,
  "after": ["example:creeper_kiss"],
  "criteria": [{"event":"height_fall_trial","target":"example:leap_of_faith"}],
  "fall_trial": {
    "dimension":"minecraft:overworld",
    "start_y":319,
    "end_y":-59,
    "distance":379,
    "advancement":"minecraft:adventure/fall_from_world_height",
    "advancement_criterion":"fall_from_world_height"
  },
  "hud":{"group":"journey","objectives":[{"text":"Fall from the top of the world to its depths."}]}
}
```

- Optional `fall_trial` metadata must match a unique strict milestone with exactly one height_fall_trial criterion, target equal to its milestone ID and count 1. Dimension, start/end heights and minimum displacement configurable; advancement and criterion optional paired fields. No hardcoded Path identifier. HeightFallTrialSpec validates numeric bounds and matching metadata before registry publication.
- HeightFallTrialMixin wraps the 1.21.1 ServerPlayer.checkFallDamage(double,boolean,BlockState,BlockPos) method and reads native startingToFallPosition. Mappings evidence: https://mappings.dev/1.21.1/net/minecraft/server/level/ServerPlayer.html . Exact vanilla advancement JSON: https://github.com/misode/mcmeta/blob/1.21.1-data/data/minecraft/advancement/adventure/fall_from_world_height.json . Vanilla minimum start Y319, max finish Y-59 and min vertical displacement 379. This implementation additionally requires grounded collision with nearby solid support, actual accumulated native fall distance including final downward movement (1.5-block tolerance for sampling/rounding), and an active eligible living survival/adventure actor, not riding/flying, creative, spectator, Newbie or fake player. No protection from void damage. No hay-bale requirement.
- Only the scoped synchronous native landing call can cancel ordinary environmental DamageTypes.FALL through LivingIncomingDamageEvent; finally clears scope on exit. No general immunity timer or movement/position modification. After native call succeeds and player remains alive and at the landing, record verified height_fall_trial against the prequalified Path. Grant configured advancement criterion idempotently after confirmed milestone completion if not already granted by vanilla. Existing advancement receipts do not fulfill this challenge. Missing configured advancement/criterion fails closed before granting temporary landing protection (log warning). Other damage types unchanged.
- Permanent immunity is the separate achievement-backed native damage_multiplier effect (damage_tag minecraft:fall, per_rank 1) in the future Classic config; no effect is granted in this PR merely by declaring a fall trial. Config may add normal eligibility constraints; actual immunity must be verified in live gameplay. No arbitrary heal, teleport, or other change to health/spawn. Native start/accumulated distance/collision is an additional conservative check, not a claim of fully verified native advancement equivalence; exact landing event order, reset on teleports/gliding, movement packet handling and compatibility with other fall hooks need runtime tests.
- A: HeightFallTrialSpecTest verifies bounds, continuous distance, matching dimension, invalid metadata and mismatched criterion. Module compile and mixin ABI check at exact head pending. B/C after authorized candidate with actual Classic configuration: safe active qualifying dry fall with no landing aid; both fresh and already-earned advancement cases; prior trial unavailable, after completion, ordinary short fall, landing too high, wrong dimension, creative/fake/flight/teleport and void cases; ensure other damage untouched, no stale scoped protection, no duplicate receipts, persistent immunity after relog, protection revoked when leaving Path. Test the boundary with unarmored Monk and verify actual start sample and accumulated distance using diagnostics before asking a player to risk a lethal fall. Water landing is not supported by this stricter grounded trial. Existing legacy fall reduction remains unchanged. Prior HUD checkpoint fbd1282 passed all applicable quick checks. No full assembly, server smoke or gameplay QA performed.


## Spiritual trial detectors and reusable deed evidence

Engine-only milestone wiring for the nine remaining Classic Journey trials (not activated in a shipped path):

| Trial | Criterion | Evidence |
|---|---|---|
| Deep Breath | observed_trial, target own milestone ID, count 1 | observed_trial.kind underwater, dimension, ticks 280; consecutive submerged server ticks; restores air after actual completion |
| Animal Friendship | animal_petted_unique, target entity ID/tag/*, count 20 | actual existing successful affection handler plus persistent unique UUID receipt; no credit for repeated same animal |
| Arrow Catching/Step Assist | arrow_dodged, target own milestone ID, count 5 | observed_trial.kind arrow_dodge, shooter minecraft:skeleton, radius 2.5; initial target and approaching trajectory, close sample then receding, unique actual arrow UUID; actual damaging arrow hit resets active criterion and UUID receipts |
| Moon Boat | observed_trial, own ID, 1 | observed_trial.kind moon_boat, ticks 200, look_cosine .92, clear sky, night and actual boat; celestial direction derived from native day angle |
| Fire Walk | observed_trial, own ID, 1 | observed_trial.kind fire_walk, distance 10, consecutive grounded horizontal movement in fire-tagged blocks; bounded step size; invalid position resets; no interim blanket protection |
| Creeper Kiss | external tyr_progression:creeper_kissed | existing successful interaction; hidden trial effect on active prerequisite |
| Bedrock Rest | external tyr_progression:bedrock_rest_completed | existing actual sleep/wake session, no spawn reset; hidden trial effect |
| Beyond Sight | external via blindness_ordeal kill_event | existing voluntary blind mode and actual blindness, now requires direct player_attack damage, empty hand, hostile Monster; trial effect until milestone complete; configurable completion path and ID |
| Conquer Death | strict_fist_victory, target minecraft:wither, 1 | existing full-health same-owner proof within configured training window; new path-scoped generic signal; legacy Wither observer retained for verified Monk path only |

`observed_trial` metadata is required for observed_trial/arrow_dodged criteria. Strict single criterion, self-target, no arbitrary client event; kind/dimension/ticks/distance/look_cosine/radius/shooter validated, defaults Overworld, radius 2.5, shooter skeleton, look cosine .92. Timed observations reset on gaps, invalid conditions, eligibility loss, dimension/death/logout and changed registry catalog. Arrow detection is conservative sampled trajectory evidence, not exact original projectile logic; misses with unusually fast or near-origin arrows may be rejected. Need real gameplay to tune radius and aiming, fire collision and moon direction. Fire requires genuine consecutive grounded steps in fire blocks, not just being on fire or lava, which is an implementation choice relative to historical broader fire walk. No mandatory drowning or intermediate fire immunity. Distinct events persist bounded per-criterion subject receipts, 256 maximum; existing legacy animal_petted criteria still fire normally. Reset is restricted to incomplete active arrow trial and actual damaging shooter hit, not a canceled attack. Old data paths opt out of observed detectors. Strict-fist evidence now checks any eligible configured training path, observers receive the specific verified path and the legacy Monk callback only honors its own proof. Existing training XP routing unchanged; generic strict victory requires configured training XP and its strict window.

Single-rank water_breathing can opt into params.full_breathing=true rather than requiring two auto ranks. Purification supports interval_ticks (default 10) and comma-separated exact exclude_effects, preserves active blindness ordeal; actual protected status registry list remains config author responsibility. Blindness kill uses exact player_attack. Existing native fire_immunity handler covers fire-tagged damage; permanent reward, enhanced night vision and glide, and step-height attribute still need achievement-backed config. Existing hidden trial effect/activation plumbing is reused; no live Monk tree change. Exact HUD text for continuous timer/distance shows the deed only, not transient fractional progress; arrow and animal counts are authoritative persistent counts. A: ObservedTrialSpecTest covers configuration, unique subjects, no precount and reset. B/C: actual continuous submersion, distinct pets across relog, skeleton near misses/real hit reset, moon direction and 200 ticks, fire walk on ordinary fire with normal damage, creeper and bedrock hidden access, blindness preserved and five valid kills, Wither strict proof; verify completion order and no receipt before eligible, other paths unchanged. Prior Leap of Faith head 0e3b8bc passed applicable quick CI; current checks pending. No full build/gameplay QA.
