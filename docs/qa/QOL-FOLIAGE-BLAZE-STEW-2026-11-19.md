# Three small vanilla QoL changes — 2026-11-19

Tracks [#384](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/384). Author: isolated QoL feature writer. Intended base: `test/full-integration`. **No integration writer ownership assumed.**

## Current source status

| Request | Implementation | Source readiness |
| --- | --- | --- |
| Attacks through grass when targeting a mob | Add **Swing Through Grass** mod, not the similarly named client-side `SwingThrough` | **PENDING PACKWIZ PIN** |
| Blaze rods survive lava/fire after dropping | `kubejs/startup_scripts/vanilla_item_qol.js`: `minecraft:blaze_rod` → `setFireResistant()` | **SOURCE READY; UNTESTED IN GAME** |
| Mushroom soup stacks | Same startup script: `minecraft:mushroom_stew` → `setMaxStackSize(16)` | **SOURCE READY; UNTESTED IN GAME** |

The 1.21.1 KubeJS item-modification API includes both methods. Vanilla Minecraft's post-24w19a stew-consumption logic is intended to decrement stacked stew and return an empty bowl; verify with our pinned NeoForge/KubeJS and food integrations. No additional KubeJS food-event handler is added without a reproduced problem.

### Foliage-targeting pin for the integration writer

- Project: **Swing Through Grass** by Nikrecs.
- Target: **Minecraft 1.21.1, NeoForge, v1.1.0**.
- Official CurseForge project ID: `1477279`; exact file ID: `9063127`; file: `swingthroughgrass-neoforge-1.21.1-1.1.0.jar`.
- Official file: https://www.curseforge.com/minecraft/mc-mods/swing-through-grass/files/9063127
- Existing dependency: Architectury API `13.0.11` already pinned to `mods/architectury-api.pw.toml`.
- Server-side/server-authoritative behavior, including in integrated singleplayer; modded non-colliding foliage should be bypassed, but solids should still block attacks.
- **Do not assume installed:** obtain checksum from exact official provider file, add a new Packwiz entry, update `index.toml`/pack hash, and reconcile `manifest/mods.json`/inclusion register as the *designated integration writer*. No unverified hash or invented pin in this branch.
- Check interactions with attack-targeting, Reach attribute, modded foliage, and AoE weapons.

## Verification

**A — Offline / quick checks**

- `node --check kubejs/startup_scripts/vanilla_item_qol.js`: PASS on authored source before commit.
- Compare event/methods with pinned KubeJS 1.21.1 API; check source is in indexed Packwiz distribution after integration. Confirm no other startup script overwrites the same item properties.
- New grass JAR: validate provider file ID, exact SHA, NeoForge 1.21.1 metadata, Architectury dependency, and inclusion in downloaded manifest. No full server boot has been run.

**B — Commands in a disposable creative or test world**

- Give the test player `minecraft:mushroom_stew` and `minecraft:blaze_rod` via creative inventory or vanilla `/give` with item counts. Confirm 16 stew combine into one stack; 17 split into 16+1.
- Eat from a stack of two, then one; each use should subtract exactly one stew and return exactly one bowl. Repeat with a full inventory and check the bowl is not lost or duplicated.
- Toss a blaze rod item entity into ordinary fire and then lava; both should remain retrievable and not burn. Confirm blaze powder conversion, furnace-fuel behavior, crafting and blaze drops unchanged.

**C — Gameplay acceptance (only after actual integrated build)**

- With the new grass JAR present, stand in survival with a zombie behind tall grass, ferns, flowers and a modded non-colliding plant. Attacks should reach the target and not break the plant.
- Repeat with a stone wall/fence or other colliding block; attack must *not* pass through. Repeat with ranged/spell attacks where relevant to catch unexpected behavior; do not assume they follow melee behavior.
- Verify mushroom stew works with Cooking for Blockheads, any existing meal/dragon-diet integrations, and shift-click/inventory movement.
- Record actual Minecraft build SHA and observations in the QA status ledger; **not** a gameplay PASS until observed.

## Handoff

Two KubeJS item modifications are source-ready for routine integration review. Grass pin remains a separate integration dependency, so this is **not an all-three-installed handoff**. Integrator owns packwiz/manifest indexing and next comprehensive QA inclusion. No full Integration Assembly, NeoForge server smoke or Prism package authorized by this feature change.
