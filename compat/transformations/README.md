# Vampire v2 architecture note

This branch supersedes the original symmetric Vampire Chassis v1
design. **Normal infection is Vampirism-only; Lilith's Blood adds JAWR,
which becomes canonical for blood/hunger/healing; either cure must remove
both states.** The test contract is [VAMPIRE-ASCENSION-V2.md](../../docs/VAMPIRE-ASCENSION-V2.md).
All v1 examples below remain historical and SHOULD NOT be interpreted as
current desired mechanics.

# Too Much Magic — transformation compatibility bridge (development)

**Status: experimental vampire chassis compiled; runtime-changing options all OFF by default. NOT integrated as survival-ready transformation enforcement.**

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
- Production-safe atomic rollback for acquisition/cure; verified unified physiology, bat forms, HUD/appearance, death arbitration, and integrated multiplayer.
- Actual client gameplay or real transformation testing. Exact pinned JAWR/Vampirism binary event signatures are verified, but their interception behavior is not yet exercised in-world.
- Native state can be modified **only with explicit server-side experimental flags** in disposable worlds. One-sided dual cures are blocked when enabled; an operator-only test cure is available, but is NOT production-safe.
- Not included in packwiz; do not ship as enforcing transformation compatibility.

See [transition policy](../../docs/TRANSFORMATION-TRANSITION-POLICY.md) and
[implementation gates](../../docs/TRANSFORMATION-BRIDGE-IMPLEMENTATION.md).

Never bypass uncertain observations, erase progression, silently install Dragon Survival, or merge this project into The Monk Mod.

## Vampire dual-state pilot (2026-11-14)

- Both Vampirism 1.10.13 and JAWR 0.5.39 binary APIs have been hash-verified.
- Registered read-only JAWR vampire observer (alongside Vampirism vampire/hunter observers).
- `/tmmtransform vampire` reports real native progression/blood meters and intended resource ownership.
- Source-specific mixins now translate blood transactions, update the JAWR shadow after native changes, conserve fractional JAWR gains, and prevent duplicate hunger and passive regeneration in dual vampires. Native JAWR healing/sun events are cancellable and intercepted only in dual state.
- See [the exact-binary deep audit](../../docs/VAMPIRE-FUSION-AUDIT-2026-11-14.md) for source-backed conflicts, intended unified state and mandatory gameplay gates.
- Both native progression tracks remain distinct. Automatic dual enrollment and an operator cure are opt-in **test-only** features, disabled by default.
- The custom JAR built and booted with 127 server-side B8 mods in [disposable-server smoke CI](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43613675210). This is a boot test, not dual-vampire gameplay verification.

## Enablement and safety (test worlds only)

Read [Vampire chassis v1 test plan](../../docs/VAMPIRE-CHASSIS-V1-TEST-PLAN.md) before changing flags. The world-server config defines `experimentalVampireChassis`, `experimentalAutomaticDualAcquisition`, `experimentalAllowUnverifiedOtherProviders` and `experimentalOperatorCure`, **all default false**. The last two are especially unsafe and should never be enabled in a valuable world. Existing permanent transformations must not be migrated automatically. Do not add the JAR to packwiz while native cure, level progression, HUD, independent player isolation and death cases remain untested.

## Archlich native resurrection arbitration (test-only)

For players with **both** Goety 3.2.2 and Forbidden Magic 0.5.37 lich flags,
`ArchlichResurrectionEvents` (NeoForge `LivingDeathEvent` HIGHEST priority)
attempts the original JAWR phylactery save first. An actual JAWR save
cancels the death event, so Goety does not charge its Arca. If the JAWR
phylactery is not charged, native Goety remains the fallback without
any TMM manual resurrection. Other liches are left alone.

This is **ON by default** for fresh disposable pre-alpha worlds: `experimentalArchlichPhylacteryFirst = true` in `tmm_archlich-server.toml`. Set false explicitly to opt out; existing world configs retain their values.
The exact native API is version gated. It does not implement the proposed
shared hunger value, cures, or immortality cross-provider lifecycle.
See [Archlich priority and QA](docs/ARCHLICH-RESURRECTION-PRIORITY.md).

## Dracolich 1.21.1 native-state pilot

The first Dracolich code slice adds three version-checked, read-only provider
probes (Dragon Survival 2.0.74, Goety 3.2.2, Forbidden Magic 0.5.37),
a pure form source policy, and `/tmmtransform dracolich` diagnostics.

Target: **Sea/Forest/Cave dragon + one native lich**, with the dragon
retaining species/growth/abilities and the one lich retaining its own
resurrection anchor, hunger and progression. Dragon plus both lich providers
remains CONFLICT; the bridge does not choose one silently.

**No automatic acquisition, native-on-death priority rewrites, hunger reset,
texture transformation, or dual-cure handling has shipped in this pilot.**
This is observability/testing scaffolding, not a claim of gameplay acceptance.
Dragon Survival has an independent death-triggered passive ability that may
heal and cancel death before lich handlers; test it before reordering events.
See [Dracolich pilot / six-world QA](docs/DRACOLICH-PILOT.md).
# Dracolich skeleton art pilot

The [original skeleton model kit](art/dracolich/README.md) supplies editable
static GEO/GLB files, a neutral atlas, an actual mesh preview and a fail-closed
local assembler for the installed 272-bone Dragon Survival rig. Native
GeckoLib geometry baking passed. The Cave/Center implementation below now adds
conditional rendering, cosmetic sync and an original skin/glow atlas; client
gameplay acceptance and the other species/body variants remain pending.

## Cave/Center Dracolich renderer and skin

The [Cave/Center pilot](docs/DRACOLICH-CAVE-CENTER-PILOT.md) implements automatic
server-derived cosmetic state sync, client-local rig assembly, conditional model/skin/glow,
native armor-UV compatibility, original casting glyphs and native skin restoration.
Only real Cave/Center dragons with exactly one lich provider qualify. Client gameplay
acceptance remains required; see the linked test matrix and actual textured preview.
