# Too Much Magic — transformation compatibility bridge (scaffold)

**Status: source-only development scaffold. NOT integrated; NOT safe as a
transformation-enforcing mod.** The parent pack remains authoritative for installed
mods, approvals and gameplay decisions.

- Target: Minecraft 1.21.1, NeoForge 21.1.265, Java 21.
- Mod ID: `tmm_transformations`
- Build: `cd compat/transformations && gradle build` (Gradle 9.2.1, Java 21).
- Dev run: `gradle runClient` or `gradle runServer` (accept local EULA for server).
- Test: `gradle test`
- Diagnostic: `/tmmtransform status` in a world.

## Implemented

- Independent Gradle / ModDevGradle 2.0.148 project and NeoForge mod entrypoint.
- Versioned, copy-on-death **review metadata attachment**, not a competing biology source.
- Read-only probe registration interface with exception -> UNKNOWN behavior.
- Complete-snapshot classifier for agreed target forms, with unit tests.
- Read-only diagnostic command that explicitly reports missing integration.

## NOT implemented

No actual upstream probes, cancelable transition hooks, food/blood integration,
power progression, death arbitration, cross-mod transformations, monk gameplay,
or client HUD. **No transformation restrictions are enforced.**
Probe registry is deliberately empty until upstream APIs/persisted data are verified.
A successful CI build or client boot cannot promote this to `integrated`.

## Safety and implementation contract

1. Upstream provider data is authoritative; bridge metadata cannot assert
   physiological changes on its own.
2. Unknown observation must remain UNKNOWN. Do not infer ABSENT merely because
   a mod is not detected or a probe throws; mod IDs must be verified.
3. Never erase mod data, alter skill trees or reset player states to repair a conflict.
4. Require a proven pre-transition interception route before enabling
   transformations; diagnostics do not count as enforcement.
5. Run disposable-world tests for acquisition (both orders), logout,
   dimension travel, death, resurrection, cure and food/HUD before promotion.
6. Compile the bridge artifact separately. Do not commit generated JARs or
   add it to packwiz's installed lock until the owner approves an integration build.

See [implementation gates](../../docs/TRANSFORMATION-BRIDGE-IMPLEMENTATION.md).

## New Dragon Survival release selection

`manifest/mods.json` has newly approved Dragon Survival 1.21.1
NeoForge v2.0.74, CF file 9539454. It is **NOT** packwiz-pinned or
installed. This bridge will ultimately observe real Dragon Survival
progression and allow Dragon + exactly one lichdom (Dracolich), rejecting
an additional lichdom and other forms. No read-only Dragon Survival probe,
state stacking or veto is live yet. GeckoLib dependency is already
present in the pack, but combined boot and world testing remain required.
