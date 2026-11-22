# Saved dimension registry and installed-build evidence — November 22

## Live confirmation — 14:43:16 America/Phoenix

The owner subsequently opened the same test world and ran `/reload`, then `/tmm_dimension_registry`. The actual `server.levelKeys()` export completed at `2026-11-22T21:43:16.855382100Z` with **19 sorted unique dimension IDs, zero capture errors, and an exact match to all 19 saved IDs**. Both integration commit and BUILD-MANIFEST hash match the saved/build audit. No fallback path was needed.

- **[live-dimension-registry.json](live-dimension-registry.json)** — actual same-build server level keys, capture time and build identity.
- **[live-versus-saved.json](live-versus-saved.json)** — no live-only or saved-only IDs; source export file hash retained.

The existing `dimension-ids.txt`, CSV and six-allow/thirteen-complement review set are therefore also confirmed against the **current live server pool**. `botania:skybox` is absent from both live and saved sets. No extra `witchery:spirit_world` key appeared in the live pool; future/lazy realms still need a fresh census if later created.

The investigator did not use screen capture or move/teleport the player for this confirmation; the owner entered the commands. Temporary instance probe/enable flag were removed afterward, with the exported result retained locally. No Angel config, dimension setting or gameplay policy was changed. Registry capture is **B PASS for dimension export only**, not an Angel teleport/filter/return-route gameplay PASS. The closed-client limitation below describes the initial offline export and is now superseded for the live confirmation only.

**19 dimension IDs exported from the current test save.** Installed experimental source: `c56fb18ffc3b9f12f27317e7fb37f15c32576a8c`, baseline `74165da57a97228559562e016e1f7b796f60e652`. Minecraft was closed when this request was investigated, so this is **persisted WorldGenSettings dimension-stem registry evidence plus installed resource/config evidence**, not a freshly executed `server.levelKeys()` dump. No client/world was started or save edited.

## Use these files

- **[dimension-ids.txt](dimension-ids.txt)** — 19 sorted, copyable namespaced dimension IDs. These are the keys to use when reviewing Angel destinations.
- **[dimensions.csv](dimensions.csv)** — the same IDs with dimension-type ID, generator type and installed provider JAR. A dimension type such as `pastel:deeper_down_type` is not the destination ID `pastel:deeper_down`.
- **[dimension-registry.json](dimension-registry.json)** — saved IDs, matching installed resources, provider SHA-256 hashes, build/save hashes and limitations. Does not contain world seed, player data, world path or coordinates.
- **[angel-policy-review.json](angel-policy-review.json)** — the six previously owner-selected allowed destinations observed in this save and the other 13 saved IDs as a native-blacklist review complement. This is a derived review input, **not an applied config or a new policy decision**.

The initial six-destination policy is recorded in [the canonical access/survival registry](../../ACCESS-SURVIVAL-REGISTRY.md), with implementation/return-route work in #392. All six are present: Overworld, Aether, Bumblezone, Twilight Forest, Aurorian and Eternal Starlight. The exported complement retains Nether even though native Angel logic may already exclude it; it is an exhaustive saved-set complement, not proof of the mod's final effective candidate selection.

## Important distinctions for the Angel list

- There are **16 saved modded dimensions plus 3 vanilla dimensions**. All 16 modded IDs have matching installed dimension resources. The JAR resource catalog has one additional entry, **`botania:skybox`**, absent from the save. Its resource has a `botania:module_enabled` condition for `skybox`, and the installed `botania-common.toml` has `modules.skybox = false`. Do not promote a conditional JAR resource into the loaded-destination list merely because its JSON exists.
- JAWR/Witchery's saved IDs are **`witchery:dream_world`, `witchery:mirror_world`, `witchery:nightmare_world`**. No separate `witchery:spirit_world` key appears in this saved registry or installed dimension-resource catalog. No guessed Spirit World ID is supplied. Whether the previously discussed Spirit World is one of these mechanics or an additional lazy/runtime realm remains an explicit follow-through question for the JAWR workstream.
- `neovitae:dungeon`, `irons_spellbooks:pocket_dimension` and `mahoutsukai:reality_marble` are actual saved keys, not merely inferred names. They are retained even though their normal entry/state initialization differs from generic teleportation.
- A registry key does not establish that the player visited that dimension, that chunks were generated there, or that a naked Angel arrival has a safe landing/return route. Existing allow/deny and return-route decisions still apply.
- Native blacklist configuration is not fail-closed against newly added or later-created dimensions. Rerun the census for each changed build and, if necessary, after lazy realms are initialized. The list is not a universal registry for all future worlds/builds.

## Reproduction and next live confirmation

The reproducible read-only exporter is `scripts/export_saved_dimensions.py` (uses the NBT reader from QA report #504). Run from repository root:

```text
python scripts/export_saved_dimensions.py --minecraft <instance-minecraft> --world <test-save> --output <report-directory>
```

It reads only `Data.WorldGenSettings.dimensions`, build identity, installed dimension JSON resources, provider hashes and the relevant Botania configuration; it excludes seed/player payloads. Saved-registry export, matching of all 16 modded saved keys, zero resource-JSON parse errors, the six-plus-thirteen split, and Python syntax checks completed successfully.

`scripts/qa-surveys/server_scripts/tmm_dimension_registry.js` is a minimal **not-yet-run** live exporter. It reads `server.levelKeys()` (or loaded-level fallback), validates namespaced IDs and writes `kubejs/export/tmm-dimension-registry.json`. It is opt-in via local `kubejs/config/tmm-dimension-survey.json` containing `enabled: true` and a reduced `build` identity, and requires an OP for its command/login trigger. For a future explicitly authorized session, stage it before joining an existing disposable world or run its `/tmm_dimension_registry` command after loading it. It has passed Node syntax checking and parsing with installed Rhino 2101.2.7-build.85; actual native bridge execution remains **NOT RUN**. No temporary probe/config was left installed after finding Minecraft closed. No screen was captured for this request; only running-app availability was checked and the computer-use session ended/reset.

## Work-slice handoff

**Implemented/source-ready:** saved dimension export, installed provider/resource reconciliation, copyable ID/CSV/JSON artifacts and policy review complement; native destination config is unchanged.

**Engineering follow-through:** compare a future actual loaded-level dump to these 19 keys before calling the Angel candidate census live-complete; resolve any lazy JAWR realm mapping from actual runtime/source evidence. Stage the reviewed native blacklist through #392's owner/integration flow. No arbitrary destination expansion or guessed identifiers.

**Design blockers:** none for exporting these facts. This report does not supersede the prior six-destination decision; any new allowed destination or changed policy belongs to the owner.

**B/C queue:** live level-key capture **B PASS for export on 2026-11-22**, superseding the original NOT RUN status; actual Angel candidate filtering and return-route tests **NOT RUN**. No teleportation, spawn, entry/return behavior, world creation or mod functionality was tested here.
