# Too Much Magic — transformation compatibility bridge (development)

**Status: active development, read-only observers + advisory transition policy. NOT integrated as survival-ready transformation enforcement.**

- Target: Minecraft 1.21.1, NeoForge 21.1.265, Java 21.
- Mod ID: `tmm_transformations`.
- Build: `cd compat/transformations && gradle build` (Gradle 9.2.1, Java 21).
- Dev run: `gradle runClient` or `gradle runServer`.
- Diagnostics: `/tmmtransform status` lists observed provider states.

## Current functionality

- Standalone NeoForge mod, diagnostics and persistent review metadata (never an alternative source of truth).
- Read-only Vampirism 1.10.x observer for **vampire** and **hunter** faction status, using source-verified public API names through reflection.
- Missing, unloaded, or failing providers report UNKNOWN.
- Complete-snapshot form classifier and tested fail-closed transition preflight with explicit cures, hunter replacement and atomic Abomination intent.

## Not implemented or proven

- Other provider adapters (Werewolves, JAWR lycanthrope, Forbidden Magic lich, Goety lich, Dragon Survival). JAWR vampire itself has a read-only probe.
- Live cancellation hooks, unified blood/diet/flight/sunlight/HUD, rituals, cures, death handling, resurrection and integrated multiplayer.
- Actual client gameplay or real transformation testing. Exact pinned JAWR/Vampirism binary event signatures are verified, but their interception behavior is not yet exercised in-world.
- No provider state is modified. No known acquisition is blocked yet.
- Not included in packwiz; do not ship as enforcing transformation compatibility.

See [transition policy](../../docs/TRANSFORMATION-TRANSITION-POLICY.md) and
[implementation gates](../../docs/TRANSFORMATION-BRIDGE-IMPLEMENTATION.md).

Never bypass uncertain observations, erase progression, silently install Dragon Survival, or merge this project into The Monk Mod.

## Vampire dual-state pilot (2026-11-14)

- Both Vampirism 1.10.13 and JAWR 0.5.39 binary APIs have been hash-verified.
- Registered read-only JAWR vampire observer (alongside Vampirism vampire/hunter observers).
- `/tmmtransform vampire` reports real native progression/blood meters and intended resource ownership.
- Pure projected blood conversion, ownership contract and unit tests exist. **No blood synchronization or event cancellation is enabled.**
- See [the exact-binary deep audit](../../docs/VAMPIRE-FUSION-AUDIT-2026-11-14.md) for source-backed conflicts, intended unified state and mandatory gameplay gates.
- Separate vampire skill progression remains intact; nothing is auto-granted or cured.
- The custom JAR built and booted with 127 server-side B8 mods in [disposable-server smoke CI](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43613675210). This is a boot test, not dual-vampire gameplay verification.
