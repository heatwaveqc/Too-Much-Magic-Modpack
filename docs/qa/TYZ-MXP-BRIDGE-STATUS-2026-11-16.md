# Tyz first-wave / MXP bridge readiness — 2026-11-16

**Pufferfish's Skills retired from active packwiz** and Tyz's Skills 6.4.2 restored from previously inspected installed snapshot `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` with its exact Modrinth version `OUmvFAPs` and SHA-512. This is source integration, **not** a new installed Prism ZIP. Pufferfish's Attributes remains a separate dependency pending audit. Improvable Skills is unaffected.

Only the **four existing first-wave Core Skills** are staged (Reservoir, Wellspring, Thrift, Potency; five ranks each); keep Tyz native tabs, don't invent the deferred full nine-tree progression. Correct the stale 99-rank Thrift KubeJS factor (0.003) to 0.0594 per five-rank level and cap the rank lookup at 5; runtime effects need confirmation. Tyz native activity XP is disabled; its level pool grants 1 SP/1000 XP.

## MXP bridge — code versus packaging

**Basic XP delivery already has Java code.** The latest `feature/mxp-configurable-growth-20261115` defines `MxpService.award` and `TyzBridge.deliver`. The latter invokes source-verified Tyz 6.4.2 `TyzsSkillsAPI.xp().addXP(ServerPlayer,float,boolean,boolean)` reflectively; it checks observed XP/level changes and returns accepted credit. MXP's ordinary award divisor defaults to configurable `1.02^P` in server config. No extra Java adapter is intrinsically needed to make that call.

**Packaging and in-world QA are missing.** Current integration baseline has no compiled `tmm_mxp` JAR. Integrate/compile the newest first-party source, include it in the actual test runtime, check reflection and the `/tmmmxp test fixed 1000` → Tyz 1 SP pipeline, per-player persistence/relogin and event cancellation. Source unit tests and static API signatures are not runtime guarantees.

**Real remaining code scope** concerns full MXP coverage rather than the handoff itself: incomplete 42-source roster and source-specific ritual/craft attribution, dedup and shared-payer edge cases, global Tyz XP firewall, crash reconciliation between separate Tyz/MXP saves, and runtime fixes if Ars/Iron/Botania/Neo native mixins fail on the pinned mod versions. The five non-Core custom tree categories beyond design are not live.

No full NeoForge smoke, Prism publication, or valued-world migration without owner authorization.
