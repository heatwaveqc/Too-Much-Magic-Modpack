# Aquamirae & Nouveau / Ars Conflux exact pin evidence

**Status:** source-ready inactive candidates for issue [#404](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/404); not installed, assembled, launched, or gameplay accepted. The isolated branch starts from `test/full-integration` commit `077a34d8706dba51b10ad65dde05a7d34cfb5c96`.

The owner approved these exact additions in #404. At pickup, the issue and documentation-only [PR #406](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/406) had no substantive activity for about twenty hours. PR #406 explicitly leaves artifact hashing and Packwiz pinning undone. Existing draft [#399](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/399) owns only the separate Aquamirae 7.2.13 and Fragmentum 5.4.0 paths. No Ars Conflux branch, duplicate candidate files, indexed code, frozen manifest selection, or later owner decision supplied these two pins.

## Official artifact evidence

| Field | Aquamirae & Nouveau | Ars Conflux |
| --- | --- | --- |
| CurseForge identity | project `1767657`, file `9470029` | project `1737308`, file `9505585` |
| Exact filename | `aquamirae-nouveau-0.1.3-mc1.21.1-neoforge.jar` | `ars-conflux-0.1.56-mc1.21.1-neoforge.jar` |
| Size | 110,796 bytes | 552,542 bytes |
| SHA-1 | `932c37b798a04d13ca231d141d86590153e99fa3` | `97f3c31c624df19209db648e4fe6dd2e67e08d80` |
| SHA-256 | `69bd58464e02eab917b4c2a284c1d750bda528a5116f9a7ddcaae60980b478a7` | `f95248e5fe2258ebc011bffdf6b350857d0d327aac2fec458ed48ca709d00158` |
| Mod ID / version | `aquamirae_nouveau` / `0.1.3` | `ars_conflux` / `0.1.56` |
| License metadata | MIT | MIT code; LGPL-3.0 optional-addon translations |

Hashes were computed from the exact official CDN bytes for the two selected CurseForge files. Both JARs remained outside the repository and are not committed.

## Exact native dependency closure

Both artifacts declare Minecraft `[1.21.1]`, NeoForge `[21.1.250,21.2)`, and both sides. The current integration source uses Minecraft 1.21.1 and NeoForge 21.1.265.

Ars Conflux requires Ars Nouveau `[5.13.1,6)`; the current active pin is 5.13.5. Its `ars_elemental` and `hex_ars_link` relations are optional. Its JAR contains four Conflux glyph recipes and preserves separate notices for optional-addon translation resources.

Aquamirae & Nouveau requires:

- Ars Nouveau `[5.13.1,6)`;
- Ars Conflux `[0.1.47,0.2)`;
- Aquamirae `[7.2.11,7.3)`.

The selected Conflux 0.1.56 and existing Ars Nouveau 5.13.5 satisfy those ranges. Draft #399's Aquamirae 7.2.13 candidate satisfies the remaining direct range and carries its own Fragmentum dependency candidate. The addon JAR contains twelve glyph recipes, including native Fury and Resilience glyphs. GeckoLib 4.9.3 is already active for the wider Aquamirae dependency chain.

## Exact branch changes

- Add `mods/aquamirae-nouveau.pw.toml` and `mods/ars-conflux.pw.toml` with independently computed SHA-1 values and immutable provider IDs.
- Keep both candidates deliberately absent from `index.toml`; this feature branch does not activate a runtime mod or claim shared Packwiz/manifest ownership.
- Add `scripts/verify_aquamirae_nouveau_candidates.py` to guard exact pins, inactivity, no committed JARs, loader/Minecraft target, active Ars Nouveau compatibility, and optional presence of draft #399's Aquamirae pin.
- Add this source provenance and integration handoff.

## Checks and remaining limits

Run the candidate verifier, Python bytecode compilation, repository validation, and `git diff --check`. These are source/static checks only.

The integration writer must independently reconcile #399, #406, these two candidates, `manifest/mods.json`, `manifest/next-qa-target.json`, `index.toml`, `pack.toml`, integration sources, and the live-inclusion register. The exact Aquamirae 7.2.13 difficulty/spawner config remains a separate JAR audit; the Progression-owned Sea Dragon/Monk behavior and scoped player-combat question are not implemented here. No Integration Assembly, server/client launch, full smoke, Prism package, spell execution, combat test, or gameplay acceptance was performed.
