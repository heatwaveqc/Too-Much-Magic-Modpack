# ReFoxed contextual middle-click restoration — issue #305

**Status:** SOURCE STAGED; client gameplay NOT RUN. Existing pinned `invtweaks-1.21.1-1.3.2.jar`, no pin or loader update.

## Cause and solution

Owner's 2026-11-17 unstable QA reported that middle-click no longer sorted chests. The 315-key Default Options file unbound the registered `key.invtweaks_sort_either.desc` control. The first-party Progression client first-run handler independently unbound all new mod mappings, so editing only Default Options would not survive a clean first launch.

This config PR binds **only** `key_key.invtweaks_sort_either.desc:key.mouse.middle:`. Other ReFoxed sort mappings and mod controls remain unbound. Vanilla `key.pickItem` is absent from Default Options and must remain mouse-middle in the world. The dated 315-key observed inventory is kept immutable as provenance. Generator and validator explicitly allow the one controlled exception. Packwiz file/index SHA256s are updated.

**Dependency:** Progression PR #393 against `dev/progression` makes the first-run client rule preserve the contextual GUI sort and set Reset default to mouse button 2 while preserving saved player assignments. Integrate the engine, rebuild its first-party JAR, and reconcile this pack config in `test/full-integration` before live acceptance. Changes must not be applied in isolation as a completed fix.

## Quick checks and deferred QA

Run `python3 scripts/validate_defaultoptions_keybindings.py` and `python3 scripts/validate.py`. When actual final registered-key exports are available, run `scripts/generate_defaultoptions_keybindings.py --check`; its approved default is intentionally output even when a reference export reports another old binding.

Later, on owner-authorized fresh disposable Prism QA, verify pinned ReFoxed really loads, middle-click sorts both chest and player inventory with item counts preserved, Mouse Tweaks dragging still works, vanilla world pick-block still works, Controls > Reset restores GUI sorting, restart and fresh import preserve behavior, and custom or intentionally unbound saved controls remain untouched. Use isolated fresh profile rather than inherited `options.txt`. Record evidence against #305, C01/C16. No server smoke, Prism build, or gameplay acceptance performed here.
