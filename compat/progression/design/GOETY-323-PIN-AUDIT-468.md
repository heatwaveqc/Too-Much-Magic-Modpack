# Goety 3.2.3 native hook audit (#468)

2026-11-22 source contract for migrating standalone Transformations to Progression.

## Why this change is necessary

The installed canonical Packwiz mod entry mods/goety.pw.toml downloads goety-3.2.3.jar (SHA-512 372e5975...e74). The transplanted Progression code still recognized 3.2.2 in its independent version checks, causing Goety to be classified UNKNOWN and its exact-version native acquisition mixin to be disabled. Archlich resurrection also failed closed at a 3.2.2 gate. Updating only the optional mixin would create inconsistent behavior; all known guards must advance together.

## Audited publisher source

Upstream maintained 1.21.1 source: Vivideru/Goety-3 commit 10e104de5c99ca37d5f6b0f2d93b48fc9a1532d2, 2026-11-17, message "3.2.3 update". gradle.properties sets mod_version=3.2.3. Verified declarations:

- com.Polarice3.Goety.utils.LichdomHelper.setLich(Player, boolean) returns void: source-compatible with the TMM GoetyDracolichAcquisitionMixin injection.
- Same helper declares isLich(Player) returning boolean: source-compatible with both Progression and internal-form probes.
- com.Polarice3.Goety.common.events.SoulEnergyEvents.onLivingDeathEvent(LivingDeathEvent) retains a default @SubscribeEvent (normal priority), including Arca soul/teleport rescue logic. Progression's experimental Archlich phylactery-first preflight subscribes at HIGHEST and only cancels after an upstream JAWR save. Goety fallback remains native.
- LichEvents.onLivingDeathEvent runs at HIGH, but its handler is unrelated Lich visuals/particles; this does not replace the SoulEnergyEvents Arca path.

This audit covers matching public GitHub source at the release commit, not independent SHA-matching of compiled class bytes. Keep the exact 3.2.3 version gate and perform pinned compiled-JAR ABI and B/C gameplay verification before treating the hook as live-tested.

## Coordinated changes

The same 3.2.3 pin now appears in:
- NativePhysiologyObservations
- GoetyLichProbe
- DracolichMixinPlugin
- DracolichDiagnostics
- ArchlichDiagnostics
- ArchlichResurrectionEvents
- ArchlichResurrectionConfig

No global version wildcard, no source JAR redistribution, no native Goety code copied, no modification to player-owned attachments or native costs. Other provider versions remain exactly pinned.

## B/C queue

Check both orders of native Goety lichdom + other approved form behavior, deny prohibited Dragon second-lich initiation, preserve native rank/cure flows, and observe Archlich phylactery-first/Arca fallback with exactly one native debit. Check absent-Goety and unverified-provider cases. Explicitly inspect the pinned JAR method descriptors and test the mixin application before packaging promotion; Type-A compile/JUnit alone is not evidence of these behaviors.
