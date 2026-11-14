# HexCast NEOFORGED — Pufferfish initial deep dive

**Research date:** 2026-11-13. **Classification: Compatible with work**, *not* native-attribute Confirmed and *not* a runtime integration. No code, script or configuration was installed by this audit.

## Exact target and sources

- B6 already pins `hexcast-neoforged-1.21.1-0.11.1-19+neoforge.1.21.1` on Minecraft 1.21.1 NeoForge; 125-mod pack has dedicated-server startup evidence, **not** HexCast in-world integration.
- Modrinth project `lZGsiJ1L` points to **a1exrom/hex_cast_NEOFORGED**, an **unofficial** public port. The port repo's `gradle.properties` declares the **exact same** `modVersion=0.11.1-19+neoforge.1.21.1`; use it instead of assuming original HexMod source applies unmodified.
- The published port advertises 150+ actions, hand-drawn patterns, staff/trinket casting, spell circles and the Patchouli Hex Book. PAUCAL, Patchouli, Kotlin for Forge and Caelus are declared required. Listed optional Create integrations **do not license installing Create**, which remains hard-excluded.

## Core architecture differs significantly from Psi

- **No native player media capacity/regen attribute identified in the port.** Unlike Psi's synchronized `psi:total_psi` and `psi:regen`, Hex Casting pays **media from inventory item stacks, rechargeable holder items, or circle impetuses**. Sources implement `ADMediaHolder` / `MediaHolderItem`, with priority-driven inventory scans.
- `MediaConstants`: **10,000 media per Amethyst Dust**, 50,000 per shard, 100,000 per charged crystal at source defaults; `ForgeHexConfig` makes item values and health conversion configurable *globally*. These are media denominations, not per-character capacities.
- `PlayerBasedCastEnv.extractMediaFromInventory` consumes player-inventory media, and if casting has unlocked overcasting, leftover media debt can become **actual health damage**. Do not erase this risk automatically via broad discounts. Staff casts, packaged casting items and circles use different `CastingEnvironment` subclasses.
- `CircleCastEnv` consumes media from the impetus block entity, not the player's item inventory. Personal skill bonuses should **not** automatically transfer to unattended circles, nor be tied to a stale online player.

## Verified integration seam, but not built-in Pufferfish/KubeJS event

- `CastingEnvironment.addCreateEventListener(Consumer<CastingEnvironment>)` is a public static registration point called from `CastingVM` constructor. Integration code can inspect the `getCaster()` and install environment components via `addExtension`.
- `CastingEnvironmentComponent.ExtractMedia.onExtractMedia(long cost)` sees the requested **remaining media debt before environment fallback**; it can pay from a different source or pass a *discounted remaining cost* to the native item/circle extraction. Returns must be >= 0; guard costs, integer rounding and overflow, and never let a skill supply unbounded free media.
- `CastingEnvironmentComponent.IsVecInRange` can modify the per-cast range test (normal player ambit is 32 blocks, with sentinel special cases). `PostExecution` observes cast result, but is not a general-purpose spell-power/damage scalar.
- No native HexCast player attribute set or first-party `PsiEvents`-like KubeJS plugin was established in this source examination. **Prototype a small NeoForge bridge mod first;** KubeJS Java interop is speculative until proven with the installed version. A server datapack/config is not sufficient for actor-specific media efficiency.
- `HexConfig.server().isActionAllowed` and `isActionAllowedInCircles` apply **serverwide deny lists**, useful for closing progression exploits, but **not** per-player Pufferfish skills.
- Great spell ability uses an **advancement** (`enlightenment`), and overcast availability uses a separate advancement (`y_u_no_cast_angy`). Do not bypass either gate or bypass world permissions when adding extension components.

## Suggested Pufferfish rewards (provisional)

| Reward | Proposed technical route | Caveat |
|---|---|---|
| Media efficiency (personal) | Environment creation hook + `ExtractMedia` component with bounded fixed-percentage reduction | Apply only to player-owned casts and document actual resource accounting; no vanilla player capacity stat |
| Casting ambit/range | `IsVecInRange` component | Must preserve world bounds, permission checks and sentinel behavior; no free cross-dimensional access |
| Universal magical damage | Existing Pufferfish magic damage where damage types actually inherit it; inspect individual effects | Hex actions such as motion, teleport, block breaking and status effects don't have a universal potency value |
| Staff/cypher/artifact skill craft quality | Per-item metadata + narrow item handler only after item lifecycle analysis | No reliable one-time imprint point proved yet |
| Great-hex research | Existing advancement or explicit server permissions | Avoid bypassing original enlightenment and pattern research |

**Don't invent per-player media capacity/recovery skills unless we're intentionally adding a new resource mechanic.** The Pufferfish tree can include Hex-oriented perks that reduce media cost without creating a second mana bar. Per recent user decision, **no shared mana pool** with Psi, Ars or Iron's.

## Specific hazards and test order

1. **Economy and overcasting:** amethyst media-cost reduction, regenerative sources, dupes from item transfer, effect of perks on health-as-media and media usage statistics; compare with Occultism/mining/renewability guarantees.
2. **Casting channels:** staff, packaged items and unattended/caster-associated circles; tests with no player, offline player, multiple players, repeated VM creation, death, dimension changes and respec.
3. **World/claim safety:** action deny lists, circle deny lists, teleport dimension denial, block breaking and creation, and actor-dependent world permissions. Do not weaken the hard economy gates (ProjectE, Replication, tech ores).
4. **Range:** 32-block normal ambit vs sentinel vs world checks. Enforce range checks, not private-player permissions bypass.
5. **Spell potency:** test specific Hex direct damage/effect paths before assuming the universal Pufferfish magic-damage trait scales them.
6. **Run-time readiness:** confirm 1.21.1 port client graphics/render, recipes and in-world casting; its source label is matched but no attached jar or in-game execution was inspected.

## Primary sources

- Exact unofficial port: https://github.com/a1exrom/hex_cast_NEOFORGED/blob/main/gradle.properties
- Casting environment and create callback: https://github.com/a1exrom/hex_cast_NEOFORGED/blob/main/Common/src/main/java/at/petrak/hexcasting/api/casting/eval/CastingEnvironment.java
- Extension component interfaces: https://github.com/a1exrom/hex_cast_NEOFORGED/blob/main/Common/src/main/java/at/petrak/hexcasting/api/casting/eval/CastingEnvironmentComponent.java
- Player media extraction / overcast: https://github.com/a1exrom/hex_cast_NEOFORGED/blob/main/Common/src/main/java/at/petrak/hexcasting/api/casting/eval/env/PlayerBasedCastEnv.java
- Circle media extraction: https://github.com/a1exrom/hex_cast_NEOFORGED/blob/main/Common/src/main/java/at/petrak/hexcasting/api/casting/eval/env/CircleCastEnv.java
- Media source interface: https://github.com/a1exrom/hex_cast_NEOFORGED/blob/main/Common/src/main/java/at/petrak/hexcasting/api/addldata/ADMediaHolder.java
- Media denomination: https://github.com/a1exrom/hex_cast_NEOFORGED/blob/main/Common/src/main/java/at/petrak/hexcasting/api/misc/MediaConstants.java
- Config / denylist: https://github.com/a1exrom/hex_cast_NEOFORGED/blob/main/Forge/src/main/java/at/petrak/hexcasting/forge/ForgeHexConfig.java
- Modrinth published port: https://modrinth.com/mod/lZGsiJ1L
