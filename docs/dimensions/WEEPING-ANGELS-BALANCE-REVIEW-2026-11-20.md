# Weeping Angels — spawn, combat, and world-interaction balance review

**Review date:** 2026-11-20. **Status:** source-reviewed **proposal**, NOT an owner-approved balance configuration, installed mod, or gameplay result. This follows the owner-locked native six-destination teleport policy in [#387](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/387) and [PR #392](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/392). Do not change the approved destinations, local teleport/fallback behavior, 50% default teleport chance, or 400-block range during this balance review.

## Sources and exact scope

Read source from [Jeryn99/Weeping-Angels `mc/1.21`](https://github.com/Jeryn99/Weeping-Angels/tree/mc/1.21):
- [`WAConfiguration.java`](https://github.com/Jeryn99/Weeping-Angels/blob/mc/1.21/common/src/main/java/mc/craig/software/angels/WAConfiguration.java): native keys, types, defaults and independent spawn config.
- [NeoForge registrar](https://github.com/Jeryn99/Weeping-Angels/blob/mc/1.21/forge/src/main/java/mc/craig/software/angels/neoforge/WeepingAngelsForge.java): main default common config and explicitly named `weeping-angels-spawns.toml`, both COMMON.
- [`WeepingAngel.java`](https://github.com/Jeryn99/Weeping-Angels/blob/mc/1.21/common/src/main/java/mc/craig/software/angels/common/entity/angel/WeepingAngel.java), [`AbstractWeepingAngel.java`](https://github.com/Jeryn99/Weeping-Angels/blob/mc/1.21/common/src/main/java/mc/craig/software/angels/common/entity/angel/AbstractWeepingAngel.java), [`HurtHelper.java`](https://github.com/Jeryn99/Weeping-Angels/blob/mc/1.21/common/src/main/java/mc/craig/software/angels/util/HurtHelper.java): attacks, theft, damage filtering, spawn restrictions and attributes.
- [NeoForge spawn modifier](https://github.com/Jeryn99/Weeping-Angels/blob/mc/1.21/forge/src/main/java/mc/craig/software/angels/data/neoforge/biome/AddAngelSpawns.java), [default biome tag](https://github.com/Jeryn99/Weeping-Angels/blob/mc/1.21/forge/src/generated/resources/data/weeping_angels/tags/worldgen/biome/spawns/weeping_angels.json), [block behavior](https://github.com/Jeryn99/Weeping-Angels/blob/mc/1.21/forge/src/main/java/mc/craig/software/angels/common/entity/angel/neoforge/BlockReactionsImpl.java), and [default theft tag](https://github.com/Jeryn99/Weeping-Angels/blob/mc/1.21/forge/src/generated/resources/data/weeping_angels/tags/item/stealable_items.json).

**Version caveat:** reviewed repository source is `mc/1.21`, not a verified extraction of the exact 1.0.5 NeoForge JAR; confirm binary parity, config syntax/filenames, and actual loaded behavior after the integration writer pins the artifact. Do not claim 1.21.1 runtime PASS from source alone.

## Native mechanics, relevant risks

1. **Natural spawning:** `[spawn]` has `minCount=1`, `maxCount=4`, `spawn_weight=8`, `spawnType=MONSTER`, `max_nearby=6`, `nearby_radius=48`, and empty `spawn_dimension_blacklist`. The default spawn-biome tag includes **`#minecraft:is_nether`** as well as forests, taiga and jungle. The spawn dimension blacklist is completely separate from teleport destinations and applies only to natural/chunk-generation spawn checks.
2. **Snow Angels:** `[snow_angels].snow_angels=true` controls a snowy-worldgen feature and requires a new-chunk test; do not conflate it with the regular mob spawn cap.
3. **Threat:** source attributes are 20 health, 8 attack damage, 2 armor, 0.8 movement speed and full knockback resistance; the AI halts when observed. No native attack-damage/health scalar was found in the reviewed configuration. Actual difficulty depends on modded stats, player class and the encounter environment.
4. **Damage:** default `[damage].hurt_type=PICKAXE_AND_GENERATOR` rejects spells, most weapons and empty-hand Monk attacks. The implementation checks the attacking player's **main-hand item**, including for other player-caused damage sources, rather than granting generic spell/unarmed damage. `ANYTHING` is the native compatibility option, though it also allows swords/arrows/etc. A tag-only workaround for magic/unarmed damage is insufficient: the native `attack/weeping_angels` tag checks a held item.
5. **Theft:** default `[behaviour].angel_theft=true` allows the Angel to take an **entire matching stack** from player main inventory; the default `stealable_items` tag contains clock, torch, compass, recovery compass and `#minecraft:pickaxes`. The Angel carries the stolen stack with guaranteed equipment drop and becomes persistent while holding it. This can strand a player without the intended counterweapon; modded tag additions and high-value pickaxes are potential balance hazards.
6. **Block behavior:** default `[behaviour].block_breaking=true`, together with `mobGriefing=true`, enables scanning for block reactions roughly every 400 ticks while observed, up to 25 horizontal and 3 vertical blocks away. Depending on block type, it can destroy emitting blocks, toggle lights/levers or extinguish candles. This affects redstone, magical fixtures and player-built structures. `block_breaking=false` disables **all** these interactions, not just physical destruction. The native `weeping_angels:no_breaking` block tag offers fine-grained exemption when restoring interactions later.

## Recommended *first unstable QA* balance (owner approval still needed)

| Control | Native default | Proposed pilot | Rationale |
| --- | --- | --- | --- |
| `[spawn].spawn_weight` | 8 | **4** | Half the relative weight; rare but observable. Not an absolute encounter frequency. |
| `[spawn].minCount/maxCount` | 1 / 4 | **1 / 2** | Prevent early groups of four, retain pair encounters. |
| `[spawn].max_nearby/nearby_radius` | 6 / 48 | **2 / 64** | Avoid multiple Angels saturating one encounter area. |
| `[spawn].spawnType` | MONSTER | **MONSTER** | Keep normal hostile category. |
| `[snow_angels].snow_angels` | true | **true** | Keep the special scenic/ambush feature, evaluate on new chunks. |
| `[behaviour].stalk_range` | 65 | **65** | Preserve observation/stalking mechanics. |
| `[behaviour].block_breaking` | true | **false initially** | Avoid destructive interactions with magical builds during first integration QA. Revisit protected-tag approach later. |
| `[behaviour].angel_theft` | true | **false initially** | Avoid full-stack loss of survival tools while testing teleports/combat. Later consider true with a restricted mundane-items tag excluding pickaxes and magical equipment. |
| `[damage].hurt_type` | PICKAXE_AND_GENERATOR | **ANYTHING initially** | Native config-only way to allow Monk, spells, dragons and ordinary weapon damage; monitor whether 20 HP makes them too fragile. |
| `[teleporting]` | native defaults | **unchanged** | Owner-locked native 50% chance, 400-block range, cross-dimension and fallback; existing draft six-world policy. |

**Spawn-dimension choice for owner:** recommend **natural Overworld-only Angel spawns in first QA**, while preserving the approved six-world **abduction destination** pool. The mod's default spawn-biome tag can include Nether and modded biomes. To claim strict Overworld-only using native `spawn_dimension_blacklist`, first finish the *same exact runtime dimension census* required by PR #392. Include Nether, End, the five permitted Angel destinations other than Overworld, and all remaining loaded dimensions in the natural-spawn exclusion list. Do not invent JAWR or speculative realm IDs. The initial pilot can explicitly document a partial suppression of currently confirmed dimensions but must not claim strict Overworld-only until complete.

**Design tradeoff:** using `ANYTHING` removes the iconic pickaxe-only vulnerability. A later exact source-compatible integration can preserve special resistances while making magic/Monk attacks eligible, but **that requires new gameplay behavior/code**, not just the native config or a tag change. Do not build such a patch without a separate owner decision.

## Implementation and QA gates

- **Pending owner direction:** approve/revise spawn numbers, Overworld-only natural spawn intent, theft policy, block interaction policy and `ANYTHING` combat policy. Do not silently treat this proposal as a locked owner decision.
- **Integration writer:** on approved balance, stage the exact `config/weeping_angels-common.toml` values on a designated integration branch, plus `config/weeping-angels-spawns.toml` using native names; review Packwiz/index ownership, exact pinned 1.21.1 NeoForge JAR, source/binary metadata and all dependent config changes. Continue to hold incomplete six-destination blacklist until exhaustive census.
- **A — offline:** compare actual 1.0.5 JAR config/spec/biome tags, confirm NeoForge key paths and effective files, validate TOML, run read-only source/Packwiz preflight and scoped tests. No in-game PASS from this step.
- **B — in-game commands:** after registry IDs and exact commands are verified from the assembled JAR, summon an Angel in a disposable world and verify attacks with pickaxe, sword, spell and empty-hand Monk; test teleport outcomes with Newbie and Regular. Do not invent or publish unverified exact summon/permission commands.
- **C — in-game play:** observe natural spawn frequency/pairs across tagged Overworld biomes and Nether; check Snow Angel worldgen on new chunks, stalking freeze, theft/griefing behavior, actual item return, return routes after abduction and magical base safety. Record world seed, position, dimension, exact integration SHA, effective configs and results. Separately check capture/spawner restrictions, Hunter tagging and MXP/kill attribution with the unified registries.

**Not executed:** no installation, Integration Assembly, server boot, Prism package, client launch or gameplay acceptance. Existing [#387](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/387) and [PR #392](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/392) remain active and blocked on the exact dimension census and pinning.
