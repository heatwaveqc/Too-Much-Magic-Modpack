# Newbie Mode + Smart Keep Inventory — implementation handoff (2026-11-16)

**Status:** Source staged on `feature/newbie-mode-smartkeep-2026-11-16`; no owner-authorized full smoke/client test; NOT accepted runtime. Reviewable draft PR, not a stable/merged pack change.

## Owner decisions

- All *new* characters begin Newbie. They graduate to Regular using an explicit, optional **Introduction** checkmark quest and a **personal reward claim**. Ordinary players cannot reverse graduation; operator can set either mode.
- Newbie receives individual vanilla-like inventory **and XP** retention, convenience `/home`, `/sethome`, `/rtp`, `/back`, no global MXP and no native faction enrollment.
- Regular has normal death + Corail Tombstone recovery, no Newbie travel commands, access to native progression/factions/MXP and normal dimensions.
- Prefab 1.0.9 remains intended housing provider; no buildings are granted until exact item/structure IDs and rewards are selected.
- ProjectE and other advancement recipes are to be blocked for Newbies, but exact per-mod recipes/stations are a separate survey; no speculative recipe removals.
- Persistent per-player EMC tooltip unlock after first Philosopher's Stone is **separate** issue #237.
- **Actual vanilla Elytra flight** (without an equipped Elytra): following Iron's Angel Wings NeoForge IItemStackExtension pattern and Ars Nouveau's Glide checks, the Progression Mod provides a synchronized, invisible `tmm_progression:virtual_elytra` effect; a mixin grants `canElytraFly` and `elytraFlightTick` while the effect is active. Server applies it to Newbies automatically and to Regular Monks with unlocked+toggled Momentum Glide. Jump-to-glide uses vanilla input, physics, body posture, wall collisions and chest armor; no custom gliding physics or Creative flight. Existing `ClassAbilities.glide` is now ONLY a modest continuous boost during `isFallFlying()`: at level heading, forward impulse <=0.016/tick, lift <=0.026/tick, no added acceleration near steep pitch (|look.y| >=0.65), horizontal boost stops at 1.05 block/tick and lift is capped at 0.065 upward velocity. Glide Control ranks add up to 0.004 forward impulse; vanilla Elytra/free fireworks remain untouched. Permission is removed immediately on graduation/disabled Monk toggle and refreshed via synchronized effect; tuning requires a client test.

## Source staged in this branch

1. `compat/progression/.../runtime/NewbieMode.java`: canonical persistent mode stored in existing `PLAYER_MODES` attachment under `tmm_progression:player_mode`, with `copyOnDeath` and a `tmm_newbie` vanilla entity-tag projection for Smart Keep Inventory's predicate. Character-mode migration checks existing playtime; zero-time players start Newbie and older saves default Regular. Operator-only `/tmmnewbie admin graduate <player>` and `/tmmnewbie admin newbie <player>`; player-visible `/tmmnewbie status`. Persistent mode also drives `canEarnMxp(player)` API for future dispatcher.
2. `ProgressionMod` registers this controller, native Monk/Hunter progression and the virtual Elytra effect. `ProgressionApi.join` rejects Newbie initiation and exposes an *any-joined-path* membership check independent of path names. The Transformations module registers a join veto via the public API: a player with ANY nonhuman native state (including vampire, werewolf, dragon, or either lich) cannot join ANY current/future Progression path, including Gilded, Monk or Hunter. The reciprocal native species acquisition hook blocks a newly acquired form when **any Progression path is already joined**. Server state must be source-verified and absent; no automatic curing of migrated players.
3. `config/ftbquests/quests/chapters/intro.snbt`: “I'm Ready for Regular Mode”, checkmark task; command reward executes trusted `tmmnewbie admin graduate {p}` with `team_reward: false`. The player must manually claim the reward; no team-wide auto-claim. A teammate's shared task-completion state may expose the reward, so **test per-player claim isolation** and make the personal action explicit in UI. Source-of-truth quest manifests, locale, layout, validators and packwiz hashes updated. The command is operator-only to prevent plain-text self-graduation.
4. `kubejs/data/tmm/smart_keep_inventory/condition/newbie.json`: player NBT predicate `Tags:["tmm_newbie"]`, keep XP true.
5. `kubejs/data/smart_keep_inventory/smart_keep_inventory/condition/bosses.json`: suppress Smart Keep Inventory's **default** boss-death rule for Regular players (version 1.0.1 adds boss rule by default).
6. `compat/magic/tyzs-test-overlay/.../keepsake.json`: `active: false` override. Preview and Monk staging scripts validate/copy this safety override so Tyz Safekept cannot independently capture inventory.

## Mod targets, not yet installed

- **Smart Keep Inventory 1.21.1-1.0.1 NeoForge**: Modrinth project `qeGAjqzW`, version `JqVOLkFB`; CurseForge project `1424832` file `7530253`. Mod description indicates server-side, no required third-party project. **Await official provider hash pin + packwiz metadata**; `manifest/mods.json` marks desired/disabled, not installed. Source: https://modrinth.com/mod/smart-keep-inventory/version/1.21.1-1.0.1-neoforge.
- **Corail Tombstone 9.5.6** remains installed/unchanged. The public Corail repository is issue/update-only, not exact 9.5.6 death-handler source. No claim of compatibility until observed in disposable 2-player testing. Source: https://modrinth.com/mod/corail-tombstone/version/neoforge-1.21.1-9.5.6.
- **Prefab 1.0.9 NeoForge**: desired only; verify existing Cloth Config >=15.0.140, resolve actual pins, choose starter-house templates later.

## Integration boundaries

- `/home`, `/sethome`, `/delhome`, `/listhomes`, `/rtp`, and `/back` denied for Regular non-operators via server CommandEvent. Other teleport commands (`/tpa`, `/warp`, `/spawn`, Corail's `/tbtp`) remain unmodified until travel policy is chosen.
- FTB Essentials is a separate existing feature branch `feature/qol-ftb-essentials-sethome-rtp`. Its installed binary/permissions must be in the actual next assembled runtime; this branch does not install it.
- Native path joins are vetoed in both directions. Source-level `@Pseudo` mixins in `tmm_transformations` route positive JAWR vampire/werewolf setLevel, non-null Dragon Survival setSpecies, Goety setLich, and Forbidden Magic Lichdom setLevel through `NativeSpeciesJoinGuard`. It applies Newbie and any-joined-Progression rules, then the existing `TransitionPolicy` to reject incompatible interspecies combinations, while preserving upgrades, cures and the provisional JAWR-only vampire+werewolf research exception. `tmm_transformations` registers an opt-in `ProgressionApi.join` veto for *all* human Progression paths. Cross-mod integration is by reflection to avoid linking the independent build projects; startup intentionally fails rather than allowing an incompatible ABI to silently bypass the guard. Verify all signatures and in-world cases against exact pinned binaries.
- `canEarnMxp` is a **contract** in TMM progression, not yet called by the separately developed MXP runtime. Hook it into the finalized dispatcher before claiming XP suppression.
- Smart Keep Inventory's condition and source mod must both be in the pack's actual registry/packwiz; KubeJS datapack source alone does nothing without Smart Keep Inventory installed.
- Vanilla `keepInventory` world gamerule **must remain false** for mixed-mode testing.
- Do not use Tyz's Safekept slot-copying behavior in the same test; first verify the override is active.

## Corail compatibility analysis

Smart Keep Inventory hooks `GameRules.getBoolean(RULE_KEEPINVENTORY)` in the dying-player context during `dropAllDeathLoot`, and wraps respawn `restoreFrom`; it persists death-condition state. Unlike Tyz Safekept it does not clone/clear inventory slots in a separate temporary map. It therefore **should** cooperate with grave mods that consult the effective vanilla gamerule, but Corail's closed death-handler implementation can still independently capture a grave. This is a credible **test target**, not a source-proven guarantee. Corail can also preserve extra inventories and experience, so duplication and XP conservation must both be verified.

## Acceptance matrix for later owner-authorized disposable QA

| Case | Expected |
| --- | --- |
| First new join | Only that player becomes Newbie, with `tmm_newbie` tag |
| Existing saved world | Existing characters retain Regular mode (test playtime migration) |
| Two FTB teammates | One personal reward claim graduates only the claimant |
| Newbie death in Overworld, Nether, End | Inventory + Curios + XP retained; no recoverable duplicate Corail grave |
| Regular death | Normal Corail grave and XP rules, **including boss deaths**, no duplication |
| Crash/relog/respawn after death | No loss of retained inventory; tag and mode persist |
| Regular `/home` etc. | Blocked without blocking Newbie commands; OP unaffected |
| Any Progression join (including Gilded) | Newbie denied; Regular denied if any actual nonhuman state is present; Regular human allowed |
| Progression member → any native supernatural form | Positive vampire, werewolf, dragon and both lichdom acquisitions denied, regardless of path ID; cures/upgrades not forcibly revoked |
| Native Monk/Hunter initiation | Blocked for Newbie, enabled after graduation only while human |
| JAWR new Vampire / Werewolf levels | No positive level writes for Newbies through `setLevel`; cures still work; Regular unchanged |
| Dragon Survival species menu / altar / command | No non-null species assignment to Newbies; Regular unchanged; existing dragon Newbies are not auto-cured |
| Newbie virtual Elytra | Vanilla Elytra posture/physics and second-jump start without Elytra item; small level-flight assist; steep upward pitch stalls; ends on graduation |
| Regular Monk Momentum Glide | True virtual Elytra and sustained gentle boost when skill is unlocked and player toggle on; other regulars unchanged |
| Tyz Safekept | Skill absent from UI and no independent inventory-copy event |
| MXP | Once linked, no activity awards in Newbie; awards after graduation |
| Mod pin and packwiz | Exact 1.21.1 binary and hashes; no 1.21.11/26.x mismatch |

**No full server smoke, Prism package, or live saved-world testing is authorized by this handoff.**

## Native Elytra source patterns verified

- Iron's 1.21 source: [Angel Wings mixin](https://github.com/iron431/irons-spells-n-spellbooks/blob/1.21/src/main/java/io/redspace/ironsspellbooks/mixin/IItemExtensionMixin.java) intercepts `IItemStackExtension.canElytraFly` and `elytraFlightTick` at RETURN while a synchronized effect is present.
- Ars Nouveau source: [ClientElytraMixin](https://github.com/baileyholl/Ars-Nouveau/blob/main/src/main/java/com/hollingsworth/arsnouveau/common/mixin/elytra/ClientElytraMixin.java), [ElytraPlayerMixin](https://github.com/baileyholl/Ars-Nouveau/blob/main/src/main/java/com/hollingsworth/arsnouveau/common/mixin/elytra/ElytraPlayerMixin.java) and [MixinLivingEntity](https://github.com/baileyholl/Ars-Nouveau/blob/main/src/main/java/com/hollingsworth/arsnouveau/common/mixin/elytra/MixinLivingEntity.java) check the Glide potion or tier-three gliding thread on client/starting/ongoing vanilla flight.
- All three addons may modify the same predicates, so test with Iron's Angel Wings, Ars Glide, Ars Gliding Thread and actual Elytra on the same client; none of these must be broken by the new permission effect.
- Client verifies visible fall-flying pose without equipped Elytra, double-jump activation, level-speed maintenance, slow altitude gain, inability to sustain steep upward flight, crash collision/fall damage, creative/spectator unaffected, death/respawn/graduation reversion and Dragon Survival movement conflicts.
