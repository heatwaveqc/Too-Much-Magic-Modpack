# Server-authoritative client dragon diet preview (issue #444)

This Progression-only source slice follows the existing recipe evaluator and does **not** require the food census. No Packwiz/config/legacy Dragon Survival map edits, no new food IDs, no world boot.

## Runtime contract
- Server resource reload atomically publishes policy, source roots and recipe routes. That generation is the invalidation key for diet preview.
- For a single, native-verified Cave/Forest/Sea dragon profile, the server projects the exact same `FoodRecipeEvaluator` nutrition used at consumption. It considers only registered default-stack `DataComponents.FOOD` items with approved data evidence. Server-known magical-tag foods carry a distinct **permission-only** marker; the client reuses their original food properties. Unknown or dynamic-only foods keep their native client result.
- A sorted, bounded table of `item ID -> {nutrition, saturation modifier}` or magical permission is sent only to that player. Entries are compact text records chunked below 4096 bytes per packet, with a unique transaction ID and piece count. No recipes, player identifiers, inventory, item NBT or food provenance is transmitted. This is a derived view, not a second client policy engine.
- Client assembles a complete, validated transfer before publishing; a new transfer clears stale preview, malformed or interrupted transfers produce no override. Data belongs to the current local player instance, never a world-global allowance.
- On species change, data reload, respawn or dimension change the server resends; logout drops the server cache. Non-dragons receive an empty snapshot. Beverage permission lease remains independent.
- Client food hook uses the per-item projected value to construct a **temporary** FoodProperties return preserving the actual stack's always-eat flag, duration, use container and effects; it never writes `DataComponents.FOOD`. The server's existing `DragonDietHook` remains the sole consumption authority.

## Limitations and required verification
- Data-driven source-only code; no same-build census required to compile or exercise synthetic source tests. The number of recognized foods is the separate provenance coverage problem.
- No preview for item stacks with FOOD only on an exceptional non-default stack, for unsurveyed food IDs, for custom native use handlers, or for foods whose native logic uses a separate client use gate. No assumption that returning FoodProperties alone fixes all upstream client interactions.
- Source-level tests cover chunking, nutrition, magical permissions, zero nutrition, duplicate/invalid rows. Validate mixed sushi, raw/cooked/charred, native denial of new foods, effects/containers, per-player species transitions, relog/dimension changes, /reload, packet bounds and the Dragon Survival 2.0.74 mixin in an owner-authorized full client test before claiming B/C PASS.
