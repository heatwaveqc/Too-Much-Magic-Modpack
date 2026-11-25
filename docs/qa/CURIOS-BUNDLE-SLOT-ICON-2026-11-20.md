# Custom Curios bundle slot icon — source fix, 2026-11-20

Tracking [QA Issue #309](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/309). The owner saw a broken bundle-slot texture in the 2026-11-17 unstable Prism test (installed SHA `322cf3f9f0deb303fc6e6c29b340273ed3f481f6`).

## Source evidence / cause

- Frozen slot registration used `"icon": "curios:slot/empty_curios_slot"` — **incorrect plural `curios`**.
- Current `test/full-integration` slot registration removed the invalid icon property, but no verified replacement was selected.
- The pinned mod is `curios-neoforge-9.5.1+1.21.1.jar` (Modrinth `yohfFbgD`). Curios' upstream `1.21.1` branch contains `common/src/main/resources/assets/curios/textures/slot/empty_curio_slot.png` (singular) and references `"icon": "curios:slot/empty_curio_slot"` in `common/src/main/resources/data/curios/curios/slots/curio.json`. This is an existing **Curios generic placeholder**, not a newly invented texture or a claimed bundle outline.
- Correct the existing `kubejs/data/tmm/curios/slots/bundle.json` to the source-verified `curios:slot/empty_curio_slot`. Preserve `size=1`, `operation=SET`, `order=50`, `validators=["curios:tag"]`, player slot assignment and Ars Elemental Curio/Caster bag tags.

## Preventing regression

`scripts/validate_curios_bundle_slot.py` enforces the exact slot contract and pinned Curios version. `tests/test_curios_bundle_slot.py` rejects the old plural ID, missing icon, changed size/order/validator, dropped bag tags, a missing player assignment or pin drift. `scripts/validate.py` calls it during lightweight metadata validation; it performs no downloads or game launch.

## Acceptance still required

This is **source-only**; the upstream source branch is supporting evidence, not a byte-level inspection of the pinned downloaded JAR or a running GUI.

1. In the next authorized clean Prism client, inspect the bundle slot empty and occupied with both Ars bag types. Confirm the Curios icon renders (no checkerboard or purple missing texture) and the slot retains the intended order.
2. Check actual equip, unequip, quick-move and Curios screen reopen / reconnect; preserved validator and tags do not by themselves establish runtime acceptance.
3. Optionally inspect the exact pinned Curios JAR for `assets/curios/textures/slot/empty_curio_slot.png` and its atlas stitching behavior; if absent, prefer a verified alternative rather than a guessed path.

Recompute Packwiz indexed slot file hash and root `pack.toml` hash before integration. The integration writer must reconcile concurrent Packwiz edits by #394/#396/#401 rather than copying this PR's root files wholesale. No independent build or full smoke authorized.
