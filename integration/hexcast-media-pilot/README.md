# HexCast personal media-efficiency pilot (opt-in, not installed)

This is a **NeoForge 1.21.1 bridge prototype** against the installed HexCast NEOFORGED `0.11.1-19+neoforge.1.21.1` public API, not a finished Pufferfish skill integration. Nothing in `index.toml`, `mods/*.pw.toml`, or ordinary KubeJS loads this source. No mod binary/JAR is committed. A **Gradle 8.14 / NeoForge 21.1.265** add-on build is now included. CI compiled it against the SHA-512-verified pinned HexCast JAR, temporarily injected the resulting *own-source* mod JAR into a disposable full B6 dedicated-server boot and observed **PASS/clean shutdown**. It is still **not part of the normal modpack**, and the client-side Hex spell casting tests remain pending.

## Intended behavior

The bridge registers `CastingEnvironment.addCreateEventListener` during `FMLCommonSetupEvent` and adds `CastingEnvironmentComponent.ExtractMedia` only to *player-based* casting environments. It checks player entity tags at casting-environment creation:

- `tmm_hex_media_test` — **required opt-in**; without it, no change even with tier tags.
- `tmm_hex_media_t1` — 10% reduction in media requested, floor-rounded savings.
- `tmm_hex_media_t2` — 20% reduction, overrides T1.
- No tier — no change.

After packaging/installing the test bridge, use these commands in a disposable world:

```mcfunction
/tag @s add tmm_hex_media_test
/tag @s add tmm_hex_media_t1
/tag @s remove tmm_hex_media_t1
/tag @s add tmm_hex_media_t2
/tag @s remove tmm_hex_media_t2
/tag @s remove tmm_hex_media_test
```

The pilot introduces **no mana conversion**: Ars + Iron's share their own bar through Ars 'n Spells, Psi keeps its separate player Psi/CAD energy, and Hex media remains item/circle based. This pilot only discounts the media *cost requested*; it does not create additional mana, alter action deny lists, edit permissions, bypass enlightenment, or remove overcasting consequences. It excludes circles even when one reports a caster. The bounded, long-division-based math does not overflow near `Long.MAX_VALUE` or make 1-unit casts free. **Tier is snapshotted when the VM/casting environment is created**, not refreshed for every operation. Long-running casts must be tested for this effect before adoption.

## Contract test actually run

`bash integration/hexcast-media-pilot/check-contract.sh` compiles the production Java sources together with **small source-shaped stand-ins** for upstream HexCast and NeoForge public classes, then executes `PilotHarnessTest`. This checks opt-in behavior, T1/T2 precedence, circles excluded, missing caster, one extension per environment, overflow-safe math and tier snapshot semantics. **That harness alone is not a Minecraft test.** A separate pinned binary compilation and combined modded server boot subsequently **passed** in [run 43560378024](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43560378024): exact HexCast JAR SHA-512 verified, NeoForge add-on compiled, 118 normal B6 server JARs downloaded/hash-verified, test JAR injected, dedicated server reached `Done`, exited cleanly. The workflow artifacts include the **compiled bridge** and **server logs**. This is **not** proof that the discount works during actual player casting.

## In-game acceptance checklist — pending

1. **Server compilation and startup: PASS** in CI. Still boot a **graphical client** on a disposable world with the actual bridge JAR and inspect in-world resource behavior; only a headless server boot is verified.
2. With actual Hex Casting items, compare a fixed 10,000-media spell with no gate (10,000), gate alone (10,000), gate + T1 (9,000), gate + T2 (8,000); inspect **real media consumed and media-use statistics**.
3. Repeat for staff, trinket/packaged items, circles including circles with a tagged player, and no-caster casts. Circles must not receive personal discounts.
4. Test overcasting with insufficient item media; validate that health costs scale appropriately without bypassing other risks, limits or permissions. Include low/high costs, repeated patterns, multiple players and offline owners.
5. Test tag removal, skill refund analogue, respawn, dimension changes, login and long-running VMs. Replace temporary tags with authoritative Pufferfish skill data only after a real tree exists.

Upstream contracts: https://github.com/a1exrom/hex_cast_NEOFORGED/blob/main/Common/src/main/java/at/petrak/hexcasting/api/casting/eval/CastingEnvironment.java and https://github.com/a1exrom/hex_cast_NEOFORGED/blob/main/Common/src/main/java/at/petrak/hexcasting/api/casting/eval/CastingEnvironmentComponent.java
