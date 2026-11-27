# Local artifact access audit — 2026-11-16

Research base: `cb8e2dcab59cc625500b3ca0329df1511823610b` (`test/full-integration`). Read-only inspection; no launcher, server, assembly or gameplay test was run.

## Reproducible evidence

The local Prism instance is `Too-Much-Magic-Full-Integration-Prism-UNSTABLE (1)` under the Windows user's `AppData/Roaming/PrismLauncher/instances/`; JARs are in `minecraft/mods/`. This is a local discovery hint, not a portable prerequisite. Revalidate the path and hashes next session.

For each checked-in `mods/*.pw.toml`, parsed UTF-8 TOML, opened its exact `filename`, computed `download.hash-format` over its bytes, compared with `download.hash`, and opened its ZIP central directory. Separately reconciled the metafile's SHA-256 against its `index.toml` entry. This establishes accessible hash-matched artifacts and archive listings, not CRC verification of every resource or functional compatibility.

**Result:** 159 pins; 156 matching local artifacts; 159 exact index/metafile matches.

## Artifact exceptions

- `default-options.pw.toml`: artifact missing, index match True.
- `ftb-filter-system.pw.toml`: artifact missing, index match True.
- `keysets.pw.toml`: artifact missing, index match True.

## Previously blocked and current focus artifacts

| Packwiz metafile | Local file | Result | SHA-512 |
| --- | --- | --- | --- |
| `ars-ocultas.pw.toml` | `ars_ocultas-1.21.1-2.6.2.jar` | match; index True | `4a673096a2d0b9a5a83aca2747a8a08211205ed40875b4c22061f774146d9d4fff9c872e2c1b6b59e5fef2d1bf3b3a08732f22743ade4e0a82ba08636dc701a5` |
| `draconic-evolution.pw.toml` | `Draconic-Evolution-1.21.1-3.1.4.633.jar` | match; index True | `42ccf634a2a167db9cda533507909dd0524f2798` |
| `eidolon-repraised.pw.toml` | `eidolon_repraised-1.21.1-0.5.0.8.jar` | match; index True | `12a816c11a064a7050e74c5c15cf07fb8269b6d4` |
| `goety-cataclysm.pw.toml` | `goety_cataclysm-1.21.1-1.8.4.jar` | match; index True | `45e390fb44ea7aa6b5f81b95b57ca224489a527a66167de1bdcf9a53c71cb5a54f6d29b69fa30b3f342e853c30bee677cf2c3701d141edf76b0be8515b6b6b60` |
| `goety.pw.toml` | `goety-3.2.2.jar` | match; index True | `d68103012faad1b74cf96017aa22711d80033d04035e98e5452f4f70d6fbc30c6523b4d3f561fedba798e116055caf598432b472b13e8a11dca07d00468b8b46` |
| `mahou-tsukai.pw.toml` | `mahoutsukai-1.21.1-v1.36.31.jar` | match; index True | `0c4f3bc823d681ad9cb00895d348d2b414201c4d397ca3aa6427b45b8d2b369e0bae8c1f354a9541e4205dadb58c49203d3570e2433d772c3eef1227bc8bfa5a` |
| `project-expansion.pw.toml` | `projectexpansion-1.21.1-1.0.6.jar` | match; index True | `97e832da2ca03309f4ef875b42a9250bba3798453f271f82b441e7c0bf739e103ee9f4274839b5e4d9a154a44167cc0f8169bd97ed37b3d16e7e09d34cfe3c85` |
| `projecte.pw.toml` | `ProjectE-1.21.1-PE1.1.0.jar` | match; index True | `24fb5997727453a15e956522761a3f586e646c2a` |

## Selection and assembled-runtime boundaries

The manifest remains selection intent, pins/index establish the baseline installation, and `manifest/integration-sources.json` declares additional frozen donor streams. Goety and Goety Cataclysm are enabled manifest records with hash-matched pins; no donor stream names a Goety path. This is not a complete dynamic override audit: generic scripts/tags and first-party Java can still affect behavior. The local QA instance is an assembled snapshot, not proof that its configs equal this newer integration base. This pass uses its matching JAR bytes as artifact evidence only; effective settings need separately identified world/config evidence.

The current manifest also contains owner-approved, uninstalled dimension plans. They are not installed merely because selected. The coverage index's older roster count must not be used as the current runtime total. Previous profile text about missing/dangling artifacts is historical and must be replaced only when that profile is actually re-audited; this access audit does not promote profiles to source-complete.
