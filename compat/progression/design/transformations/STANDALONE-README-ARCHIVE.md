# JAWR-native transformation compatibility bridge — next-pack QA

Mod ID: `tmm_transformations`. Target: Minecraft 1.21.1, NeoForge 21.1.265, Java 21.
This branch removes all TeamLapen Vampirism/Werewolves dual-state code, client blood UI mixins,
experimental dual cures, and obsolete faction probes. **JAWR owns vampire and lycanthrope
physiology, progression, nutrition and cure.** No external blood meter is created.

## Current source-level implementation (not in-world certified)

- Five native, fail-closed state probes: JAWR vampire, JAWR werewolf, Goety lich,
  Forbidden Magic lich and Dragon Survival dragon.
- /tmmtransform status, vampire, werewolf, archlich, dracolich diagnostics (read-only).
- Archlich death priority: charged Forbidden Magic phylactery first, native Goety Arca fallback.
  Controlled by `tmm_archlich-server.toml`; pre-alpha default true. This may change event
  ordering and **must be tested on disposable worlds**.
- Dracolich first-pass Dragon Survival on-death ability before lich resurrection,
  with original native failure fallback, plus prevention of acquiring two lichdoms as a dragon.
- Server-side mob alliances: unprovoked undead normally ignore JAWR vampires/verified liches,
  with per-attacker retaliation, boss exemptions, explicit dragon/faction tags.

## Not yet verified

Mixins/third-party binary signatures and client loading, real death priority, JAWR attachment
probes, FTB death/restore, other-mod hostile targeting goals, mob tag coverage, PvP, rollback,
multiplayer and saves. Invalid cross-provider form combinations are read-only diagnostics
except the narrowly scoped Dracolich dual-lich acquisition guard; no automatic cures or Abomination
ritual. Monk and Hunter are independent `tmm_progression` disciplines, not species flags.

## Development

`cd compat/transformations && gradle --no-daemon build` checks unit tests. Full integration
runs a disposable dedicated-server smoke with both first-party JARs, and packages a separate
Prism QA client. Neither is a replacement for a client/world gameplay test.

## Cave/Center Dracolich visual pilot (next client QA)

The original generated geometry, rig validator, armor/glow renderer and exact pinned-version Dragon Survival client mixins are staged in this first-party module. See [the scoped pilot and graphical acceptance plan](docs/DRACOLICH-CAVE-CENTER-PILOT.md). Its client rendering and cure/reversion remain **NOT RUN** in the combined pack; source compile is not graphical acceptance.
