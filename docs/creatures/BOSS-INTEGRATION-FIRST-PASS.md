# Boss integration first pass — 2026-11-19

Development audit only; no runtime changes or reward activation. Read the canonical `registry-v1.json`; the frozen `boss-capture-review-v1.csv` is evidence, not a second editable boss inventory. Current registry donor #353 `4e97a8d0dc15a2b988b23ea26d4a929f749c9ec9`, Progression source inspected #347 `f206079bdc040e9aab64e55f4bd2c846eba0a103`. The installed #348 census (fa869c43a685241d4d9b48e7d2b89d019c321856) records IDs/names/categories and exact JAR hashes, not resolved entity tag membership. Historical entity tags are from reconciliation-v0. Current native tag membership needs one shared resolved-tag census or exact pinned resource survey, with nested tags, replace/merge and conditions resolved; current source evidence does not establish installed state.

## Coverage
- 56 standard candidates: 28 historically `c:bosses` (Goety 4 overlap `goety:bosses`, Botania 1 overlaps `neoforge:bosses`); seven further IDs sourced from native Twilight Forest boss tag; four further Feywild IDs in existing pack `tmm_transformations:boss_exempt` (not native boss credit permission); 17 additional roster IDs. The Knight Phantom also has Twilight Forest native tag source evidence, but is a group encounter, not one of the 56.
- 20 miniboss candidates: ten historically `goety:mini_bosses`, eight Cataclysm, Iron's Archevoker and Elder Guardian as working explicit candidates. No automatic full-boss reward.
- Four special encounter candidates: two conditional variants, two groups. 26 status/availability holds remain out of automatic awards. Registry-projected 56 type exclusions protect against wrong generic payouts but are not a positive award roster.
- Do not interpret arbitrary names containing boss as eligibility. Historical `neovitae:ritual_boss_blacklist` includes Warden (special threat); `forbidden_arcanus:boss_catcher_blacklisted` includes player. Protection/loot/capture tags serve different semantics.
- Keep historical tags as evidence. Generate exact pack-owned positive tags for reviewed standard and miniboss sets (candidates remain nonactive until registration is approved) and maintain existing negative generic-death tag. Do not grant rewards solely because a foreign tag adds another type; compare resolved native tags to registry for new/conflicting IDs.

### Standard candidates by evidence
| Entity ID | Tag evidence |
| --- | --- |
| `alexscaves:luxtructosaurus` | explicit roster |
| `ars_nouveau:wilden_boss` | historic c:bosses |
| `botania:doppleganger` | historic c:bosses |
| `cataclysm:ancient_remnant` | historic c:bosses |
| `cataclysm:ender_guardian` | historic c:bosses |
| `cataclysm:ignis` | historic c:bosses |
| `cataclysm:maledictus` | historic c:bosses |
| `cataclysm:netherite_monstrosity` | historic c:bosses |
| `cataclysm:scylla` | historic c:bosses |
| `cataclysm:the_harbinger` | historic c:bosses |
| `cataclysm:the_leviathan` | historic c:bosses |
| `draconicevolution:draconic_guardian` | explicit roster |
| `eidolon_repraised:necromancer` | explicit roster |
| `eternal_starlight:lunar_monstrosity` | explicit roster |
| `eternal_starlight:permafrost` | explicit roster |
| `eternal_starlight:starlight_golem` | explicit roster |
| `eternal_starlight:the_gatekeeper` | explicit roster |
| `extrabotany:ego` | historic c:bosses |
| `extrabotany:gaia_3rd` | historic c:bosses |
| `extrabotany:gaia_legacy` | historic c:bosses |
| `extrabotany:void_herrscher` | historic c:bosses |
| `feywild:ashen_lord` | pack transformation boss_exempt |
| `feywild:mab` | pack transformation boss_exempt |
| `feywild:oberon` | pack transformation boss_exempt |
| `feywild:titania` | pack transformation boss_exempt |
| `goety:apostle` | historic c:bosses |
| `goety:ender_keeper` | historic c:bosses |
| `goety:hostile_redstone_monstrosity` | historic c:bosses |
| `goety:vizier` | historic c:bosses |
| `irons_spellbooks:dead_king` | historic c:bosses |
| `irons_spellbooks:fire_boss` | historic c:bosses |
| `minecraft:ender_dragon` | historic c:bosses |
| `minecraft:wither` | historic c:bosses |
| `occultism:afrit_wild` | explicit roster |
| `occultism:marid_unbound` | explicit roster |
| `primalmagick:inner_demon` | historic c:bosses |
| `theaurorian:dungeon_keeper` | explicit roster |
| `theaurorian:dungeon_spider` | explicit roster |
| `theaurorian:moon_queen` | explicit roster |
| `totemic:baykok` | historic c:bosses |
| `twilightforest:alpha_yeti` | native Twilight Forest source tag |
| `twilightforest:hydra` | native Twilight Forest source tag |
| `twilightforest:lich` | native Twilight Forest source tag |
| `twilightforest:minoshroom` | native Twilight Forest source tag |
| `twilightforest:naga` | native Twilight Forest source tag |
| `twilightforest:snow_queen` | native Twilight Forest source tag |
| `twilightforest:ur_ghast` | native Twilight Forest source tag |
| `voidscape:corrupted_pawn` | explicit roster |
| `wardentools:contagion_incarnation` | explicit roster |
| `witchery:baba_yaga` | historic c:bosses |
| `witchery:death` | historic c:bosses |
| `witchery:gulg` | explicit roster |
| `witchery:horned_huntsman` | historic c:bosses |
| `witchery:leonard` | historic c:bosses |
| `witchery:lilith` | explicit roster |
| `witchery:mog` | explicit roster |

## What to build
1. Generic data-driven reward registration and native terminal death settlement. Shared MxpService already provides FIRST_BOSS_MXP=1000, persisted first completions, transaction dedupe and repeat divisor. Generic MxpMod death hook is present but server rule map is empty by default; old KubeJS example is disabled and uses legacy namespace. Use registry-generated reviewed type/encounter policy and reviewed repeat configuration, reload/start/stop lifecycle, optional mod IDs; no new XP engine. Current handler is final-blow native ServerPlayer only and has no explicit cancellation check or event priority. Audit canceled/rescued deaths, fake player/creative/spectator and duplicate processing, especially revival/phase transitions; defer or use provider-native terminal receipt where required. Never count damage or bossbar alone as victory.
2. Special receipts: Foreman (ordinary Doloris excluded from boss MXP), augmented Null Servant (separate variants/keys), Knight Phantom group and Cosmic Crystal custom HP/group. Two more source-sensitive standard candidates: vanilla Ender Dragon and Draconic Evolution Chaos Guardian, previously flagged in compat/mxp/README.md. Inspect exact pinned completion lifecycle and event/player attribution before enabling; not proof custom mixins necessarily needed. Gaia/other staged bosses also sample terminality and clone/summon paths; regular actual final death should share generic handler. Cosmic Crystal Hunter completion reward remains separate.
3. Reward policy: locked first valid boss victory 1000 effective MXP (per player/per encounter key); ordinary repeats divided using existing growth factor (now 1.02). Exact base repeat award not approved in inspected source; Chaos Guardian fixed repeat 100 remains explicitly proposed, not live. Miniboss MXP policy and multiplayer/final-blow vs participant eligibility not settled in inspected source. Avoid activating full boss credit, repeat values, or participation tracking without owner decisions. Recommend ordinary repeat base 1000 divided if owner agrees; separate miniboss policy, optionally no special MXP. Preserve ordinary Hunter XP independent.
4. One shared native QA: exact resolved boss/miniboss tags, source/class/spawn and native Capturing constraints, ownership, terminality, group encounter transaction, recipient, reload/relog, canceled/rescue death, multiple copies and repeated victories; retain capture-specific Foreman protection, no global capture overrides. No duplicate 1,763-type research. Need verify newly added type tags/candidate sources when installed set changes.

References: `compat/mxp/README.md`, `compat/mxp/SOURCE-HOOKS-2026-11-14.md`, `compat/progression/src/main/java/io/github/heatwaveqc/tmm/progression/mxp/MxpMod.java` and `MxpService.java`, `compat/transformations/src/main/resources/data/tmm_transformations/tags/entity_type/boss_exempt.json`, frozen registry and #348 survey. No merge, build, dispatch, or newly activated rules.

## Boss reward implementation decision — 2026-11-19

Owner approved standard first 1,000 fixed effective MXP, standard repeat base 1,000 divided, miniboss first 250 fixed and repeat base 250 divided, genuine summoned encounter eligible, and Chaos Guardian repeat 100 fixed (held until verified terminal receipt). Apotheosis actual Invader/Elite receives a small ordinary divided bonus, no first-victory completion key; working interpretation: Elite 10, Invader common/uncommon 10, rare 20, epic 30, mythic 40, based on native stored rarity, never gear appearance or inflated HP. Native source family 8.9.0 at Shadows-of-Fire/Apotheosis 84e27e7031c7e2ab2f2e8328daf491c88203fcbc (Invader.BOSS_KEY/RARITY_KEY, Elite.MINIBOSS_KEY); installed JAR hash 23a8b4a21d0d8744f097c77c8a96fcba14c4b95f3b1bb542412f40dab272872e, binary/live confirmation pending. Regular pack bosses/minibosses take precedence, no stacking with elite bonus. Multiplayer working interpretation of owner suggestion: native last-player timer is 100 ticks, single player only; track separate committed-damage timestamps for eligible real online players, per victim, default 100 ticks, plus valid terminal blow; no damage share split, no group membership requirement, no generic pet attribution. Excludes canceled/rescued or fake-player events; no special encounter completion until exact native evidence. 

Registry revision 2: vanilla Ender Dragon and Draconic Guardian require terminal receipt, joining existing special instance rules. Positive candidate projections `bosses.json` (56) and `minibosses.json` (20); negative generic-death tag now 58 (including both terminal holds), leaving 54 ordinary boss candidates after intersection. In total 21 tags. Preserve first-victory key by entity ID for generic boss tiers; distinct miniboss key; special authored encounter keys not yet assigned. Source-only, not runtime installed. Projection workflow donor must be updated after #347 commit.

## Ender Dragon exception — owner approved, revision 3

Vanilla Ender Dragon no longer needs a native terminal-death adapter. The exact completed advancements `minecraft:end/kill_dragon` and `minecraft:end/respawn_dragon` each grant 1,000 fixed MXP once per player (second is a resummoning ritual). No repeat dragon-kill MXP, retain ordinary death exclusion and Hunter eligibility. Runtime #347 `a408c51bb4a647d844b2b620534997439dbfaa86` uses native AdvancementEarnEvent, existing canonical `boss:minecraft:ender_dragon` and distinct `advancement:minecraft:end/respawn_dragon` persisted first keys with zero repeat. The two instance rules above remain native-behavior-pending until live QA; no historical advancement backfill. Administrative grant/revoke is native event behavior; persisted ledger prevents a second payment. Five remaining native encounter adapters: Neo Foreman, augmented Null Servant, Knight Phantom group, Cosmic Crystal and Chaos Guardian. All 21 projected tags and counts unchanged. No new runtime activation or generic dragon payout.
