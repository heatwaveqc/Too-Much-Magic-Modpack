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

- All other provider adapters (Werewolves, JAWR vampire/lycan, Forbidden Magic lich, Goety lich, Dragon Survival).
- Live cancellation hooks, unified blood/diet/flight/sunlight/HUD, rituals, cures, death handling, resurrection and integrated multiplayer.
- Actual client gameplay, jar-level event verification or real transformation testing.
- No provider state is modified. No known acquisition is blocked yet.
- Not included in packwiz; do not ship as enforcing transformation compatibility.

See [transition policy](../../docs/TRANSFORMATION-TRANSITION-POLICY.md) and
[implementation gates](../../docs/TRANSFORMATION-BRIDGE-IMPLEMENTATION.md).

Never bypass uncertain observations, erase progression, silently install Dragon Survival, or merge this project into The Monk Mod.
