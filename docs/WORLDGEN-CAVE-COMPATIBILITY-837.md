# Infinity Cave compatibility test matrix (#837)

First approved run only: C0 original Infinity Cave 0.7 + audit-only Chunk Pregenerator/Carbon, NeoForge 21.1.265, MC 1.21.1 Java 21, seed 424242, measured 16x16 chunks, four-chunk buffer, 900 seconds. Preserve exact server log and native metadata, no repair or retry. If loader fails, stop; #833 owns repair. If successful, require full saved chunks and report exact deep layer/biome counts and effective overworld generator. Surface elevation/biome-specific boundary instrumentation belongs to later cases, not implied by this initial scanner.

| Case | Infinity Cave | Tectonic | Alex's Caves | Purpose |
|---|---|---|---|---|
| C0 | original | no | no | Native load, depth, deep fluids |
| C1 | accepted | yes | no | Tectonic terrain + depth |
| C2 | accepted | no | yes | Alex's interaction |
| C3 | accepted | yes | yes | combined |
| B0 | no | yes | yes | baseline comparator |

C1–C3/B0 require separate authorization, exact pinned dependencies and source/config parity. Deliberately locate and sample Alex's rare biomes and adjacent boundaries with matching coordinates and seeds, do not infer absence from spawn sampling. Measure per-height fluids, air, bedrock, block state, biome quart distribution, surface elevation, registry and generator selection; compare below -64 to C0; inspect cross-biome edges and other dimensions. Diagnose before proposing a scoped patch; no production removal authorized unless Alex's proven causal and reviewed. No automatic retries. One-shot command after reviewed launcher is installed: `/tmm worldgen cave 990c97ce355aac191419354809ab013efddb10f0 <fresh 32-hex nonce>` on #746. Same owner-only receipt and concurrency protections, no boot on PR. First run authorized 2026-11-27 only.
