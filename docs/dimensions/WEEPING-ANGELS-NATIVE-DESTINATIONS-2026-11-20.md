# Weeping Angels: initial native dimension blacklist and return-route audit

**Decision date:** 2026-11-20. **Status: candidate config, NOT a verified exhaustive blacklist or runtime PASS.** Owner explicitly wants native Weeping Angels behavior (both local and cross-dimension teleportation), no custom teleport patch. Initial allowed destinations: Overworld, Aether, Bumblezone, Twilight Forest, Aurorian and Eternal Starlight **only**. Newbie dimension exceptions remain to be tested separately. See [#387](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/387) and [Dimension Access & Survival Registry PR #389](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/389).

## Config candidate

Source: [`config/weeping_angels-common.toml`](../../config/weeping_angels-common.toml). The upstream `mc/1.21` NeoForge config uses `[teleporting].banned_dimensions` and `interdimensional_teleporting`. The native selector randomly chooses among all loaded dimension IDs not in the blacklist, **can select the current dimension** and always excludes Nether separately. Local teleportation and fallback remain enabled. No weighting supported. [Source config](https://github.com/Jeryn99/Weeping-Angels/blob/mc/1.21/common/src/main/java/mc/craig/software/angels/WAConfiguration.java), [source selector](https://github.com/Jeryn99/Weeping-Angels/blob/mc/1.21/common/src/main/java/mc/craig/software/angels/util/Teleporter.java), [1.21 NeoForge config registrar](https://github.com/Jeryn99/Weeping-Angels/blob/mc/1.21/forge/src/main/java/mc/craig/software/angels/neoforge/WeepingAngelsForge.java).

### Allowlist TARGET, not an upstream feature
- `minecraft:overworld` — local teleportation when selected from Overworld
- `aether:the_aether` — native fall below void boundary returns to Overworld
- `the_bumblezone:the_bumblezone` — native top/bottom dimensional exit
- `twilightforest:twilight_forest` — portal construction and return via Overworld origin
- `theaurorian:the_aurorian` — self-built Aurorian portal
- `eternal_starlight:starlight` — Starlight Portal, potentially difficult to create after involuntary entry

### Confirmed excluded IDs already in candidate config
- `minecraft:the_nether`; `minecraft:the_end`
- `deeperdarker:otherside`
- `wardentools:abyssdim` (Dive Into the Abyss — source's MOD_ID is wardentools, not abyss)
- `voidscape:void`
- `pastel:deeper_down`
- `neovitae:dungeon`

**Hard blocker before treating this config as complete:** determine exact 1.21.1 installed registry ID(s) for JAWR Spirit World (and other possibly loaded JAWR levels); check every third-party mod for additional dimension JSON and runtime-registered levels, including speculative Feywild and alternate Pastel content. The source project is a blacklist, not an allowlist: any unlisted new dimension silently becomes eligible. **Do not merge/release the config as a strict six-destination policy until the loaded dimension census has proved that every excluded world is present in the list**, and the exact Weeping Angels 1.21.1 NeoForge mod has been pinned/tested. This is an implementation-ready partial config, not a guarantee.

## Getting home after an involuntary Angel teleport (no entry portal)

| Destination | Native return route | Evidence / remaining concern | Status |
| --- | --- | --- | --- |
| Overworld | Already in home dimension | Ordinary local teleport allowed; coordinate offset up to `teleport_range` around attacking Angel | Native |
| Aether | Fall out of floating world below Y=0 -> Overworld | [Official Aether wiki](https://aether.wiki.gg/wiki/The_Aether/The_Aether). Keep native `disable_falling_to_overworld` false. Falling can leave player high in Overworld, potentially lethal without parachute or safe landing. Does not require entry portal. | Documented; exact config/live QA pending |
| Bumblezone | Cross below Y=0 or above Y=255 -> return to saved origin, fallback Overworld | [Bumblezone wiki](https://github.com/TelepathicGrunt/Bumblezone/wiki). Test no recorded origin from Weeping Angel and exact 7.17.3 config. Mining out bottom with bare hands may be nontrivial; top works too. | Documented; forced-entry QA pending |
| Twilight Forest | Collect materials, construct water/plant/diamond portal pool in Twilight Forest -> Overworld | [1.21.1 TF Portal Block](https://github.com/TeamTwilight/twilightforest/blob/1.21.1/src/main/java/twilightforest/block/TFPortalBlock.java) explicitly selects the configured origin dimension on return. [Native config](https://github.com/TeamTwilight/twilightforest/blob/1.21.1/src/main/java/twilightforest/config/TFCommonConfig.java): `originDimension=minecraft:overworld`, `shouldReturnPortalBeUsable=true` by default; portals need crafting/activation. Native arrival creates a portal, external Angel arrival does not. Test diamond and bucket material acquisition from scratch. | Source-confirmed return pathway; survival QA pending |
| Aurorian unofficial port | Gather Aurorian cobble -> smelt Aurorian stone -> craft portal frame; activate with one Silentwood Stick in each hand -> Overworld | [Portal](https://github.com/PhantomDaze/TheAurorian/blob/1.21.1/src/main/java/shiroroku/theaurorian/Blocks/AurorianPortal.java) routes from Aurorian to Overworld. [Port guidebook](https://github.com/PhantomDaze/TheAurorian/blob/1.21.1/src/main/resources/assets/theaurorian/patchouli_books/the_aurorian_guide/en_us/entries/aurorianbasics/1makingtheportal.json) documents local recipes and sticks. [Teleporter](https://github.com/PhantomDaze/TheAurorian/blob/1.21.1/src/main/java/shiroroku/theaurorian/Portal/AurorianPortalTeleporter.java) auto-builds a destination portal only on portal travel. Angel arrival doesn't create it. Test basic resource loop, wood furnace, sky location. | 1.21.1 source-confirmed survival escape route, in-world QA pending |
| Eternal Starlight | Find/construct Chiseled Voidstone Starlight Portal and activate using Orb of Prophecy | [Starlight Portal](https://github.com/LeoMinecraftModding/eternal-starlight/blob/1.21.1-arch/common/src/main/java/cn/leolezury/eternalstarlight/common/block/ESPortalBlock.java) calls [ESTeleporter](https://github.com/LeoMinecraftModding/eternal-starlight/blob/1.21.1-arch/common/src/main/java/cn/leolezury/eternalstarlight/common/world/ESTeleporter.java) to generate a linked return portal *when native portal is used*. Angel teleport skips this. [Official wiki](https://github.com/LeoMinecraftModding/eternal-starlight-wiki/blob/main/world/dimension.md). Native Orb of Prophecy may be unavailable to a naked player already inside; verify independent acquisition. | High stranding risk / hold for test |

## Tests before claiming policy complete

1. Pin and inspect exact 1.21.1 NeoForge Weeping Angels file and effective generated config (filename, registered keys, and destination blacklist).
2. Enumerate all actual loaded `ResourceKey<Level>` IDs in the complete frozen test build, not just folder names or prospective roster. Compute **loaded minus six allowed** and compare against `banned_dimensions`; fail review if any ID missing. Include JAWR realms and unanticipated extra worlds; don't fabricate guessed IDs.
3. Confirm natural spawn blacklist is separate and remains untouched.
4. In a disposable world, test Angel attacks from Overworld for every selected realm; confirm *exactly six* eligible results and both local/interdimensional behavior at native default chance. Note that uniformly selected destinations plus failed safe-position attempts distort observed counts.
5. For each allowed dimension, force arrival without prior portal/record; then try returning **without cheats, existing portal, pre-provided items, /home or prior dimension origin state**, and record the exact biome/coordinates, source and result.
6. Repeat with Newbie and Regular; determine whether Newbie gating actually prevents Angel teleports, without weakening ordinary portal/command rules.
7. Preserve native failure fallbacks, configs and balance constraints. Any new custom code is a separately approved proposal only if real tests demonstrate necessity.

No world launch, native mod-JAR launch, integration assembly, or release build was performed during this source review.
