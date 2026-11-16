# Newbie Mode + Smart Keep Inventory — implementation handoff (2026-11-16)

**Status:** Source staged on `feature/newbie-mode-smartkeep-2026-11-16`; no owner-authorized full smoke/client test; NOT accepted runtime. Reviewable draft PR, not a stable/merged pack change.

## Owner decisions

- All *new* characters begin Newbie. They graduate to Regular using an explicit, optional **Introduction** checkmark quest and a **personal reward claim**. Ordinary players cannot reverse graduation; operator can set either mode.
- Newbie receives individual vanilla-like inventory **and XP** retention, convenience `/home`, `/sethome`, `/rtp`, `/back`, no global MXP and no native faction enrollment.
- Regular has normal death + Corail Tombstone recovery, no Newbie travel commands, access to native progression/factions/MXP and normal dimensions.
- Prefab 1.0.9 remains intended housing provider; no buildings are granted until exact item/structure IDs and rewards are selected.
- ProjectE and other advancement recipes are to be blocked for Newbies, but exact per-mod recipes/stations are a separate survey; no speculative recipe removals.
- Persistent per-player EMC tooltip unlock after first Philosopher's Stone is **separate** issue #237.
- Starter **powered glide** now implemented in the existing Monk `ClassAbilities.glide` tick loop: automatically kicks in while falling, sustained capped forward thrust (0.045), look-up lift (0.035+0.095 * upward look), horizontal cap 0.75, upward cap 0.24; keeps survival game mode and normal chest armor. Ends on landing, entering water/lava or graduation. **This is not native Elytra wing rendering or a double-jump start**. Test air movement/anti-cheat and client feel; final virtual-Elytra pose is not yet delivered.

## Source staged in this branch

1. `compat/progression/.../runtime/NewbieMode.java`: canonical persistent mode stored in existing `PLAYER_MODES` attachment under `tmm_progression:player_mode`, with `copyOnDeath` and a `tmm_newbie` vanilla entity-tag projection for Smart Keep Inventory's predicate. Character-mode migration checks existing playtime; zero-time players start Newbie and older saves default Regular. Operator-only `/tmmnewbie admin graduate <player>` and `/tmmnewbie admin newbie <player>`; player-visible `/tmmnewbie status`. Persistent mode also drives `canEarnMxp(player)` API for future dispatcher.
2. `ProgressionMod` registers this controller. `ProgressionApi.join` rejects new native Monk/Hunter path joins for Newbies. It does **not** intercept independent Dragon Survival/JAWR/Goety/Gilded joins yet.
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
- Native path joins are vetoed. Source-level `@Pseudo` mixins in `tmm_transformations` now deny **positive** JAWR VampireLeveling/WerewolfLeveling.setLevel(ServerPlayer,int) while tagged `tmm_newbie`, and prevent Dragon Survival `DragonStateHandler.setSpecies(Player,Holder,boolean)` with non-null species for a Newbie. Cures (level 0) are not blocked; existing forms are not forcibly cleared. Verify injected signatures against pinned JAWR 0.5.39 and DS 2.0.74 in the assembled runtime.
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
| Native Monk/Hunter initiation | Blocked for Newbie, enabled after graduation |
| JAWR new Vampire / Werewolf levels | No positive level writes for Newbies through `setLevel`; cures still work; Regular unchanged |
| Dragon Survival species menu / altar / command | No non-null species assignment to Newbies; Regular unchanged; existing dragon Newbies are not auto-cured |
| Newbie starter glide | Forward boost and modest climbing after jump/fall; no extra armor requirement, no Creative flight; ends on graduation |
| Regular Monk Momentum Glide | Original slow descent, only with Monk/Tyz conditions, unaffected |
| Tyz Safekept | Skill absent from UI and no independent inventory-copy event |
| MXP | Once linked, no activity awards in Newbie; awards after graduation |
| Mod pin and packwiz | Exact 1.21.1 binary and hashes; no 1.21.11/26.x mismatch |

**No full server smoke, Prism package, or live saved-world testing is authorized by this handoff.**
