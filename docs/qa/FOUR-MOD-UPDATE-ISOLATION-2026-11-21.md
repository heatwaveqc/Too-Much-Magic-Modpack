# Four-mod update isolation trial — 2026-11-21

**Status:** upstream release candidates verified; source-only trial, **NOT INSTALLED OR SMOKE TESTED**. Baseline `test/full-integration@2373d50732866e460dc9e9454f7187f9045e1cdc` kept untouched. This branch is an experiment, **not a promotion request**.

## Version-diff evidence

| Mod | Baseline pin | Proposed trial | Provider evidence | Release channel | Decision |
|---|---|---|---|---|---|
| Neo Vitae | 1.1.53 | 1.1.54 | CurseForge [1404763 / 9687147](https://www.curseforge.com/minecraft/mc-mods/neovitae/files/9687147) | release, 1.21.1 NeoForge | Trial. Machine idle CPU optimization, Athanor and ritual behavior changed. |
| EvilCraft | 1.2.100 | 1.2.102 | CurseForge [74610 / 9631379](https://www.curseforge.com/minecraft/mc-mods/evilcraft/files/9631379) | release, 1.21.1 NeoForge | Trial. Recipe, book and biome integration fixes; regression risk for blood, inventory and recipes. |
| Goety | 3.2.2 | 3.2.3 | CurseForge [586095 / 9668015](https://www.curseforge.com/minecraft/mc-mods/goety/files/9668015) | release, 1.21.1 NeoForge | Trial. Worldgen changes demand disposable-world checks. |
| Ars Elemental | 0.7.10.4 | 0.7.11.3 | CurseForge [561470 / 9716535](https://www.curseforge.com/minecraft/mc-mods/ars-elemental/files/9716535) | **beta**, 1.21.1 NeoForge | Trial only; **hold promotion** without beta exception. Release note says it fixes a dedicated-server crash. |

**Scope note:** These are the four independently found update candidates. Owner's mention of 'four mods' did not provide names; confirm the identities against the launcher/upstream roster before treating this as exhaustive.

## Trial acceptance and required evidence

1. Run the mandatory **read-only complete active-mod update survey** against the latest *packwiz index and pins* (not just this shortlist). Check all upstream providers and JAR/library ranges; record any held newer candidates. This document is not that survey.
2. Resolve exact selected provider binary/JAR metadata and SHA-512s. Check loader = NeoForge, MC = 1.21.1, dependencies and compat with current CurseForge/Modrinth pins. No fetched JARs, generated worlds, logs or caches belong in Git. For Ars Elemental use the exact 9716535 **beta** only if explicitly within the isolated test.
3. On an isolated trial checkout, update these four `mods/*.pw.toml` plus `manifest/mods.json`, `index.toml` hashes and `pack.toml` index digest as a coherent patch. Do not hand-edit hash guesses or switch platforms invisibly. Capture full proposed diff and rollback SHA.
4. Run Repository CI, Module CI, and read-only Integration Preflight. A green quick check is not a game boot. The canonical `.github/workflows/full-integration.yml` currently hardcodes checkout of `test/full-integration`: **do not dispatch it and claim this trial was tested**.
5. Before any full test, reconcile the mandatory live-test inclusion register and owner-authorized QA scope. To perform an isolated server smoke, arrange an owner-approved **manual**, default-false opt-in workflow that checks out the **exact candidate SHA**, assembles the pack and genuinely launches the dedicated server with the selected first-party JARs. Never add push/PR/schedule-triggered game boot and never mutate the shared integration branch as a workaround.
6. Capture the eight canonical outcomes separately (Repository CI; Module CI; Integration Preflight; Integration Assembly; Server Boot Smoke; Prism QA Package; Client Launch Smoke; Gameplay Acceptance), logs and JAR/hash provenance. For this trial: **all eight are NOT RUN on the updated four-mod candidate**.
7. Smoke-regressions: server reaches `Done`; no missing registries, mixin conflicts, mod loading errors or recipe parsing failures; Neo Vitae Ara Vitae/Tabula/Hellfire Forge/Athanor plus EV/spiritus integrations; EvilCraft blood storage, spirits, display stands and JEI; Goety mob/structure spawning and configured compat; Ars Elemental glyphs/foci, spell casting and Ars addons, including dedicated-server paths; local client, multiplayer, recipe/tag, and in-game progression checks remain separate.
8. Baseline confounder: earlier authorized full smoke **failed** on Progression `OwnedWearMixin` (run [44976454994](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44976454994)); fix merged through [#460](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/460), with only quick CI presently confirmed. New trial server failures must be compared against a genuine repaired-baseline boot, not blamed on the four updates by assumption.

## Review outcome

**No compatibility or safety verdict yet.** Current baseline pins remain authoritative, no source changes promoted. Next execution boundary: exact candidate JAR hash verification, controlled Packwiz relock, then an isolated owner-authorized live smoke. The existing QA command console #291 pins *current integration head*, so it cannot be used to smoke this fork without first changing the shared baseline.
