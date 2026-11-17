# Friend QA stability repairs — 2026-11-17

Based on integration head `938c401ac304245663137085e7a2234244d57bf3` and the installed snapshot `322cf3f9f0deb303fc6e6c29b340273ed3f481f6`. Source repairs only, no authorized assembly/smoke/publication.

- Restore FTB Library sidebar top-left and FTB Chunks minimap top-right. Confirm on a fresh client; Tyz button placement still needs exact pinned UI config/implementation inspection.
- Halve Ars quest coordinates in runtime chapter, pilot source, and layout ledger; all 49 IDs/tasks/rewards/dependencies preserved. Confirm zoom and line rendering in client.
- Correct both marble stonecutting ingredient codecs and generator (item objects); validate actual RecipeManager loading on next authorized boot.
- Baseline owns the tested Werewolves-free Almost Unified materials file; donor still provides other exact config files. No override exemption and no validation weakening. Confirm strict preflight before assembly.
- Pending: actual Akashic Tome populated component schema and native book list, bundle slot texture, FTB Quest translation runtime diagnosis, remove custom TreeChop HUD through Progression engine owner, invalid key -1 log root cause. Do not alter working quest rewards or vanilla key defaults to guess at fixes.

Source-side checks in connector: original index hash and every changed existing indexed blob checked with SHA-256; new packwiz hash chain computed, 49 unique compact coordinates, exact silver semantic diff. Repository CI and gameplay not yet run.
