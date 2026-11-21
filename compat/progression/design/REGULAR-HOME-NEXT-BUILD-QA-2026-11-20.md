# Regular-stage Starter Home — next-build test handoff

**Branch:** `feature/progression-regular-home-power-20261120`; **PR #418** targeting `dev/progression`.
**Scope:** a first playable *server-authoritative* pilot, not a verified Minecraft client or server runtime PASS. The earlier `REGULAR-STAGE-STARTER-HOME-ABILITY-2026-11-20.md` is the owner design contract; this document records the pilot implementation and its intentional restrictions.

## How the power works

The existing *I'm Ready for Regular Mode* FTB quest still grants 5 XP and issues only `tmmnewbie admin graduate {p}` as an **individual** command. `NewbieMode` stores `tmm_progression:player_mode=regular` as before. Progression's built-in `RegularHomePower` registers `/tmmhome`; the power is available to any Regular regardless of chosen character path and does not use a physical voucher, FTB choice reward or extra path.

`ProgressionApi.mode` persists the new `tmm_progression:regular_starter_home` key inside the existing copy-on-death `PLAYER_MODES` attachment. Absent means available, `reserved:<id>` means manual inspection required after an interrupted build, and `spent:<id>` means permanently used. Neither a repeated graduation, death/relog nor class change clears the field. One shared grant covers all **19** theme choices: one stock Prefab Generic *or* 18 allowlisted custom NBT structures. There are no selectable FTB quest rewards or custom house items.

## Player QA commands (after actual NBT and Prefab installation)

- `/tmmhome` or `/tmmhome status` — see available/reserved/spent state.
- `/tmmhome list` — names of the 18 original custom structures; Generic is separate.
- Aim at the **top face** of a solid block within 8 blocks. `/tmmhome inspect <house_id>` checks the desired NBT, site size, clear volume, chunk availability, world border, supported footprint and chunk claims. It reports a **textual** size and destination, not a hologram.
- `/tmmhome place <house_id>` — directly load the **approved vanilla-format NBT** from `prefab-extension/server-cache/<house_id>.nbt` using Minecraft's `StructureTemplate`, atomically reserve the player entitlement, place once on the server, then mark spent after the synchronous native template call reports success. This does **not** call Prefab Addon's separately asynchronous construction API, material-payment UI, uploaded blueprints or cloud storage; it is an intentionally narrow optional-provider-free pilot.
- `/tmmhome generic` — when stock Prefab is installed and the player has inventory space, issue **exactly one** original `prefab:item_house`. Prefab then supplies its native GUI of 15 style variants and consumes its own item on placement. For this initial pilot **the entitlement is spent when the item is given**, since there is no reliable stock Prefab completion callback yet. Do not claim completed-placement retry/restore behavior for Generic; test item loss/dropped/trade behavior separately.

## Required next-build integration

1. The one integration writer selects and merges the coherent Progression core source from this PR through `dev/progression` and the immutable full-integration donor manifest. The mod must actually be compiled and installed; source-only PR presence is not runtime activation.
2. The housing writer reconciles **Inn board PR #381** with **18-house PR #416** (which static-QA generated all 18 files), then the integration writer stages their exact verified outputs at **`prefab-extension/server-cache/<id>.nbt`** in the actual game/server directory. No generated NBT, source donor JAR or built-in house is silently embedded in the Progression mod. Preserve source licenses and attribution.
3. Integrate the exact Prefab **1.0.9** native house and dependencies; Prefab Custom Addon 2.7.1 + LDLib2 source pins in PR #403 are recommended for their native browser and comparison QA, but **not a runtime hard dependency of this pilot's custom placement command**. Disable Prefab's `newPlayersGetStartingItem` automatic first-join item and set `startingItem=Nothing`; prevent Generic's default starter chest contents/mineshaft and gratuitous 20 coal per furnace per the approved Generic audit.
4. Keep the existing personal quest and packwiz shared index unchanged unless the integration writer identifies a specific source hash/update need. Never add `prefab:item_house` as a quest item reward or add a second player-stage system.
5. Run normal Repository CI, Module CI and read-only Integration Preflight before a reviewed promotion. A separate explicit owner authorization is required for full pack assembly, NeoForge server boot, Prism QA/client smoke and gameplay acceptance. Request/log precise release hashes and test artifacts.

## Current safe placement boundaries

The command fails closed unless the player is Regular and the state is available; exact 18-house allowlist, overworld-only for the initial trial; actual source files must be non-symlink regular NBT files no larger than 8 MiB compressed; NBT decode budget 64 MiB; dimension 96x128x96 and volume maximum 250,000 blocks. The entire template bounding box must be inside world/build boundaries and already loaded, air or replaceable, with no living entities. Four supporting base corners must be solid. Vanilla spawn/use checks and optional **FTB Chunks EDIT_BLOCK** check on every intersected chunk apply; if the FTB protection API is present but unusable, deny placement. A missing NBT, occupied plot, claim denial or unready file **does not consume the ability**.

**Source-level pilot limitations that must be explicitly tested:**
- No rendered preview, rotate/mirror chooser, terrain leveling or multi-biome foundation matching; custom house origin follows the top face of the block under the crosshair, oriented as stored in NBT. Large/underground and coastal designs may fail the strict flat-clearance policy; do not treat this as a broken generator without investigating.
- Template placement itself is synchronous. An exception after partial mutation leaves `reserved:<id>` until a human verifies the site; no unsafe automatic refund or terrain rollback. A crash during save/placement may require manual reconciliation; the pilot is **not crash-atomic**.
- Generic is currently spent on **blueprint issuance**, not confirmed placement. Unlike custom synchronous structures, a lost Generic item is **not automatically refunded**. No FTB/native Prefab callback or client GUI modification is installed.
- Checks for external claims use the optional reflection-based FTB Chunks public protection API; exact installed 1.21.1 release behavior needs gameplay QA. Native Prefab may have separate internal build restrictions; test with that exact version.
- No extra custom content/recipe/marketplace upload is eligible for the one-free-home power; later paid crafting remains governed by its own mods. Vanilla-style placement may differ from Prefab Custom Addon's material/palette/animation previews; check required modded block IDs and block-entity behavior.
- The active asset roster is a frozen source snapshot for QA. Before stable release, reconcile the final artifact hashes and allowed variants explicitly, and avoid treating any pre-stable placeholder as canonical.
 
## Minimum live acceptance

1. Newbie cannot use commands; graduation quest can be claimed once per player; each FTB teammate independently enters Regular with one unused entitlement.
2. Inspect + place one small custom NBT on a flat, supported unclaimed plot, then attempt a second home, logout/relog, die, relog again, change class, and repeat graduation; all extra free placements must be refused.
3. Inspect bad ID, missing file, loaded/unloaded chunk, wall-occupied space, protected claim, border, dimension, over-height and living-entity interference; every failed **preflight** preserves the one-time benefit. Valid flat plot works once.
4. Test Generic separately with Prefab's 15 styles and item consumption. A Regular player may choose Generic **or** custom, never both. Inventory-full Generic request fails without entitlement debit; once Generic item is issued, the entitlement is spent.
5. Inspect two player sessions to ensure no team sharing, rerouting of item rewards, duplicate issue via concurrent commands or client-supplied NBT file names. Confirm copyOnDeath and server save persistence.

**No server/client live tests are claimed by this document.**
