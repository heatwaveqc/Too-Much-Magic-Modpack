# Classic harvesting survey — pinned binary evidence, 2026-11-25

## Scope and result

Downloaded and examined 21 likely harvesting-related JARs from the owner's TMM Drive folder. Computed each file's configured SHA-1 or SHA-512 and verified all 21 against the active test/full-integration Packwiz pins. Inspected the shipped item/block tag JSON and selected bytecode with javap. Found 82 concrete entries in minecraft:pickaxes (expanding Pastel's nested tag), from 18 inspected mods. This is a **targeted binary survey, not an exhaustive loaded registry census**; remaining mods, tag merging, default TOOL components, KubeJS, block-state properties, block entities, mod-specific harvest overrides and actual loot remain runtime questions. None of the following counts imply a distinct capability tier simply because there is another named tool.

| Exact binary | Declared pickaxe-tag entries |
|---|---:|
| BetterEnd-21.0.39.jar | 3 |
| Botania-Revived-1.21.1-2.0.2-NeoForge.jar | 4 |
| Draconic-Evolution-1.21.1-3.1.4.633.jar | 5 |
| L_Ender's Cataclysm 1.21.1-3.33.jar | 4 |
| Pastel-1.1.5.5.jar | 9 |
| ProjectE-1.21.1-PE1.1.0.jar | 2 |
| Voidscape-1.21.1-1.9.588.jar | 5 |
| aether-1.21.1-1.5.10-neoforge.jar | 5 |
| aquamirae-neoforge-1.21.1-7.2.13.jar | 0 |
| eidolon_repraised-1.21.1-0.5.0.8.jar | 2 |
| embers-reignited-1.21.1-1.5.9.jar | 4 |
| enigmaticlegacyplus-1.21.1-1.1.2.jar | 0 |
| eternalstarlight-0.9.1+1.21.1+neoforge.jar | 14 |
| evilcraft-1.21.1-neoforge-1.2.102.jar | 2 |
| forbidden_arcanus-2.6.1.jar | 2 |
| malum-1.21.1-1.8.2.jar | 2 |
| occultism-1.21.1-neoforge-1.227.0.jar | 2 |
| primalmagick-neoforge-1.21.1-6.0.7.jar | 4 |
| projecte-energy-0.1.4.jar | 0 |
| theaurorian-1.21.1-1.0.6.jar | 7 |
| twilightforest-1.21.1-4.8.3345-universal.jar | 6 |

## Material tier findings

* **Pastel:** data/c/tags/block/needs_netherite_tool.json references #pastel:malachite_ores (3 ore IDs), #pastel:dragonbone_blocks (16 IDs) and pastel:rock_crystal. All five lower vanilla incorrect-for-tool tags, including diamond, append #c:needs_netherite_tool. This is a concrete source-level distinction between diamond and netherite. The configured Classic tag includes netherite but the mastery eligibility names only the four originally expected vanilla receipts. If the live graph distinguishes netherite, it can currently unlock as an extra generated deed without being required for Unlimited. Recommend requiring the fifth vanilla tier if distinct and reachable; no balance or mastery change without owner approval and live graph evidence. Netherite trial may need a specific naturally obtainable training block; verify dimension access and eligible five breaks.
* **ProjectE:** data/minecraft/tags/block/incorrect_for_{wooden,gold,stone,iron,diamond,netherite}_tool.json each excludes #projecte:needs_dark_matter_tool (dark_matter_block, dm_furnace, dm_pedestal) and #projecte:needs_red_matter_tool (red_matter_block, rm_furnace). ProjectE's incorrect_for_dark_matter_tool denies the red group, red's own deny tag is empty. PEPickaxe extends PickaxeItem and uses IMatterType; do not inherit charge, special mining mode, speed or AoE from a harvest-only unlock. Block entity restrictions matter: use actual solid eligible training blocks, not furnace/pedestal.
* **Voidscape:** separate native tool tiers and nonempty needs_voidic_tool (titanite_ore), needs_titanite_tool (flesh_ore), needs_ichor_tool (strange_ore, cracked_astralrock); own incorrect-for-tier tags and named pickaxe/warhammer entries. These are not proof that vanilla tools are denied (no matching vanilla incorrect-for-tool additions in the inspected JAR); confirm actual registry and contextual harvest before defining any independent tier.
* **Draconic Evolution:** Wyvern/Awakened/Chaotic needs tags are empty in shipped data; netherite excludes #draconicevolution:needs_wyvern_tool. Infused obsidian and chaos crystal/parts are in needs_diamond_tool, but native special restrictions/guardian gates may apply. No separate harvest tier inferred from equipment name or speed.
* **Occultism:** infused and Iesnium pickaxes tagged. Silver ore variants in needs_iron_tool. Otherworld visibility and natural Iesnium have separate contextual rules, already implemented via explicit Iesnium Palm and Otherworld Sight; generic fingerprint must not replace that adapter.
* **Twilight Forest:** Ironwood denies vanilla iron-ineligible blocks; Knightmetal and Steeleaf use diamond deny; Fiery uses netherite deny; Giant uses stone deny. These are direct nested-tag aliases, not proof of distinct extra harvesting capabilities; specialized tool actions excluded from the passive tier survey.
* **Embers:** four needs_*_tool tags (lead, silver, dawnstone, clockwork) empty in the inspected JAR; ordinary blocks in vanilla iron/diamond tags. Do not infer four extra harvest tiers.
* **BetterEnd:** Aeternium incorrect-for-tool tag empty, with Thallasium/Terminite/Aeternium pickaxe entries. Aether, Eternal Starlight, Primal Magick and Forbidden & Arcanus contribute vanilla needs-tool tags. Other tools can share capabilities despite separate recipe and speed tiers.
* **Other surveyed candidates:** Botania, Malum, EvilCraft, Eidolon, Aurorian and Cataclysm tagged tools, not assumed distinct. The full fingerprint survey must decide equivalence and identify real novel block sets.

## Runtime census plan

Engine #731 adds operator-only /tyrprogress admin harvestcensus. Default selectors are #minecraft:pickaxes and #minecraft:mineable/pickaxe. After authorized build/boot and successful datapack reload, export world/reports/tyr-harvest-registry-v1.json. Preserve the first report before running the narrow variant:

`/tyrprogress admin harvestcensus "#tyr_progression:classic_pickaxe_representatives" "#minecraft:mineable/pickaxe"`

This calls the exact existing NativeHarvest.fingerprints and HarvestCapabilityGraph planner, exports all sets, equivalents, deltas, missing default TOOL components and actual installed material trial scopes; no recurring scans or registry mutations. For Classic, verify the wooden/stone/iron/diamond override blocks are in their actual deltas, all mandatory receipt IDs exist or are recorded aliases, and check netherite difference. For modded, compare all fingerprint groups to Classic union and record only true new capability sets. Check actual default-state requiresCorrectToolForDrops, hardness, reachable natural blocks, not block entities; contextual/claim/loot checks remain manual. Keep Classic open paid purchase order as owner approved; defer decisions on modded material tiers until evidence and owner approval.

Status: targeted static binary evidence complete; installed native census blocked by unavailable running candidate. No full modded boot, assembly, release or migration performed. Prior Aqua/graph quick CI 9/9 green; diagnostic source checks pending.
