# Quick QA fixes and starter rewards — 2026-11-16

**Source branch:** `fix/quick-qa-and-starter-rewards-2026-11-16` at base `ea7d6c9270cb66f6f89aec99b4ce3f34f9a3da47`.
Scope excludes broad quest rendering, obsolete quest item IDs, Monk issues and the deferred Ars pilot, which have separate owners. No full server smoke, client test, or build publication is authorized by this change.

## Changes

- Bio-Mutagen shapeless 3×3 recipe now uses 1.21.1 ingredient objects for **4 moss blocks + 4 rotten flesh + 1 spider eye**; JUnit contract rejects raw-string ingredients.
- New-world Dragon Survival server config turns off both login and inventory species pickers, keeps the transformation altar enabled, and does not prohibit human players.
- Pinned Enigmatic Legacy+ source has two join gifts: `enigmaticlegacyplus:unwitnessed_amulet` and `enigmaticlegacyplus:cursed_ring`. New-world config disables native starter gear; both exact items become claimable item rewards in the optional Welcome checkmark. XP retained; original filler paper replaced.
- Punch a Tree now uses the FTB Filter System native item-tag task for `minecraft:logs`; the hidden advancement and its old reward function are removed. Claiming the quest gives the **Ars Elemental Curio Bag** and XP, with no torches. FTB Filter System 21.1.4 is pinned for NeoForge 1.21.1 and bridges through the existing FTB XMod Compat. The task accepts all logs and Nether stems. The dedicated bundle Curios slot already passed player QA and is unchanged.
- `feature/traders-vengence` registered as a deferred/source-only draft in integration-sources; this is not runtime inclusion and not an override of the mandatory attempt-to-include-ready-features policy.

## QA and migration

Run `python scripts/verify_quick_repairs.py`, `python scripts/validate.py`, `python scripts/validate_quests.py`, and `gradle --no-daemon test` in `compat/progression` as quick-only checks. Static checks do **not** establish that RecipeManager loaded the ingredient JSON or that FTB Quests gives actual items.

Test a **new disposable world/new player**: no auto Enigmatic gifts or pouch, no dragon login/inventory choice; Welcome claim delivers the amulet/ring; log pickup completes Punch a Tree and claimed reward gives one bag and XP, with no torches; check relog/per-player behavior and Bio-Mutagen crafting/JEI. Avoid assuming an old world's inventory/gifts can be migrated safely: `defaultconfigs` only applies on new world creation, old `world/serverconfig/*.toml` must be updated separately. Players already gifted items might get duplicates if they claim the new quest. Do not auto-strip items, revoke advancements, or edit valued worlds.
