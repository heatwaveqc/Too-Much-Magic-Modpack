# TMM — frozen holiday development-cycle mod roster (audit snapshot, November 23, 2026)

## SECOND-PASS ROSTER RECHECK — 2026-11-23

The current Gravekeeper resurvey [PR #576](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/576) found **no new selected external mods**: exactly **249 enabled** + **12 disabled**, and 249 matching `mods/*.pw.toml` entries. Rechecked the same live manifest blob `23b153fc6676cf6d4b3143e645a3838ed63a56df` and Packwiz index blob `e19b9f5d455f4cd387b67a44bb2523cb63d142e3`. The new magic-mod pouch policy [#619](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/619) is **not** a new mod inclusion; Malum and Gaze were already pinned, and portable-bag nesting remains banned under #16. Newly authored Progression, food, Monk and Atlas PRs are first-party/research work, not external JAR additions. Original 249-item live roster and archived 242 snapshot remain below. No source pin edits or game QA took place.

---

## LIVE HOLIDAY ROSTER — refreshed November 23, 2026 (supersedes the frozen 242-row snapshot below)

**Live source cross-check:** `test/full-integration` manifest `23b153fc6676cf6d4b3143e645a3838ed63a56df`, index `e19b9f5d455f4cd387b67a44bb2523cb63d142e3`. Exactly **249 enabled** external mod candidates have **249 matching Packwiz `mods/*.pw.toml` entries** (no missing or orphaned pins), and **12 catalog records are disabled**. This is a **source pin/selection audit**, not confirmation of downloads at runtime, Minecraft launch, client rendering, multiplayer, or survival gameplay.

Since the preserved old 242-mod snapshot, **8 newly selected logical IDs** (`q-shop`, `crispy`, `dungeons-delight`, `enders-delight`, `runiclib`, `huaxia-delight`, `multiplayer-spawners`, `swing-through-grass`) replaced / added atop that catalog, while **1 old ID** (`quest-shop`) was removed. This gives 242 + 8 − 1 = 249. In particular, **Q Shop has replaced Quest Shop** via #569, the five new food-mod artifacts are indexed via #571, Multiplayer Spawners/BetterEnd Lootr tag support via #572, and **Swing Through Grass is NOW selected/indexed**. Do not regenerate their old "missing" installation tasks. The historical target envelope of 252 is NOT a current build count or promise.

**Still unselected:** Lootr Liaison and Twilight Lootr remain desired but disabled pending exact official publisher binary/dependency review (#542/#543). **Weeping Angels** remains an explicitly Christmas-alpha target, with dimension/trap/native QA work in #392; **Chaos Bunny #549** is a Christmas first-party feature, not another external JAR. #593 is an approved next-unstable **EMI/EMIsemble/Extra Mod Integrations trial**, not yet shown as installed in this index, with JEI/JER replacement decisions and #592 dust-hide hold respected. Do not silently add them to the holiday roster before integrator-approved exact pins.

**Scope governance:** #390 is authoritative; no arbitrary old or unapproved mods added. Preserve custom HD texture work as paused (#295); Cathobb is separately selected as a resource pack, not counted in external JARs. Current execution/QA owners remain #431, #578 Gravekeeper live crosswalk and #534 archaeological resurrection. No server boot, Prism assembly, B/C acceptance, Packwiz edit, mod activation or source merge was performed by this roster documentation refresh.

### Complete 249-mod live selected roster (manifest & index matched)

| Category | Logical ID | Manifest version | Packwiz file slug |
| --- | --- | --- | --- |
| adventure | `aether` | 1.21.1-1.5.10-neoforge | `aether` |
| adventure | `alexs-caves-continued` | 1.1.2+neoforge-1.21.1 | `alexs-caves-continued` |
| adventure | `aquamirae` | 7.2.13 | `aquamirae` |
| adventure | `born-in-chaos` | 1.7.6 | `born-in-chaos` |
| adventure | `cataclysm` | L_Ender's Cataclysm 1.21.1-3.33 | `l_enders-cataclysm` |
| adventure | `creature-feature` | 1.3.0 | `creature-feature` |
| adventure | `creeper-overhaul` | 4.0.6 | `creeper-overhaul` |
| adventure | `critters-and-companions` | 1.21.1-2.4.0 | `critters-and-companions` |
| adventure | `deeper-darker` | deeperdarker-neoforge-1.21.1-1.4.1 | `deeperdarker` |
| adventure | `dive-into-the-abyss` | 1.1.4 | `dive-into-the-abyss` |
| adventure | `dungeon-crawl` | mc1.21.1-2.3.17 | `dungeon-crawl` |
| adventure | `enderman-overhaul` | 2.0.3 | `enderman-overhaul` |
| adventure | `eternal-starlight` | 0.9.1+1.21.1+neoforge | `eternal-starlight` |
| adventure | `gateways-to-eternity` | GatewaysToEternity-1.21.1-5.1.0 | `gateways-to-eternity` |
| adventure | `ghost` | 1.3.4 | `ghost` |
| adventure | `golem-overhaul` | 1.1.1 | `golem-overhaul` |
| adventure | `lava-fishing` | lavafishing-1.21.1-1.2.13-all.jar | `lava-fishing` |
| adventure | `mowzies-mobs` | 1.8.2 | `mowzies-mobs` |
| adventure | `nether-depths-upgrade` | netherdepthsupgrade-3.3-1.21.1-NeoForge.jar | `nether-depths-upgrade` |
| adventure | `ribbits` | 4.1.6 | `ribbits` |
| adventure | `sons-of-sins` | 2.2.1d | `sons-of-sins` |
| adventure | `swem` | 1.21.1-1.6.14 | `swem` |
| adventure | `swem-addendum` | 1.21.1-1.0.1 | `swem-addendum` |
| adventure | `the-aurorian-unofficial-port` | 1.21.1-1.0.6 | `the-aurorian-unofficial-port` |
| adventure | `the-bumblezone` | 7.17.3+1.21.1-neoforge | `the-bumblezone` |
| adventure | `the-graveyard` | 4.0.1 | `the-graveyard` |
| adventure | `twilight-forest` | 4.8.3345 | `twilight-forest` |
| adventure | `twilight-forest-final-boss-remake` | 2.1.2 | `twilight-forest-final-boss-remake` |
| adventure | `voidscape` | 1.9.588 | `voidscape` |
| adventure | `winter-overhaul` | 4.0.2+1.21.1-neoforge | `winter-overhaul` |
| compatibility | `almostunified` | AlmostUnified-NeoForge-1.21.1-1.4.2 | `almostunified` |
| decoration | `rechiseled` | rechiseled-1.2.5-neoforge-mc1.21 | `rechiseled` |
| decoration | `scarlet-springs-equestrian-decor-ssed` | 1.21.1-1.2.1 | `scarlet-springs-equestrian-decor-ssed` |
| decoration_beach | `lets-do-beachparty` | letsdo-beachparty-neoforge-2.1.5.jar | `lets-do-beachparty` |
| decoration_furniture | `lets-do-furniture` | letsdo-furniture-neoforge-1.1.11.jar | `lets-do-furniture` |
| decoration_glass | `crystalix` | crystalix-3.0.3.jar | `crystalix` |
| dependency | `apothic-attributes` | ApothicAttributes-1.21.1-2.11.0 | `apothic-attributes` |
| dependency | `apothic-enchanting` | ApothicEnchanting-1.21.1-1.6.4 | `apothic-enchanting` |
| dependency | `apothic-spawners` | ApothicSpawners-1.21.1-1.4.0 | `apothic-spawners` |
| dependency | `architectury-api` | architectury-13.0.11-neoforge | `architectury-api` |
| dependency | `balm` | balm-neoforge-1.21.1-21.0.67 | `balm` |
| dependency | `bclib-new-dawn` | 21.0.26 | `bclib-new-dawn` |
| dependency | `better-advanced-tooltips` | better-advanced-tooltips-2101.1.0-build.5 | `better-advanced-tooltips` |
| dependency | `blueprint` | 8.1.0 | `blueprint` |
| dependency | `bookshelf` | bookshelf-neoforge-1.21.1-21.1.81 | `bookshelf` |
| dependency | `brandons-core` | BrandonsCore-1.21.1-3.2.1.309 | `brandons-core` |
| dependency | `caelus` | caelus-neoforge-7.0.1+1.21.1 | `caelus` |
| dependency | `cloth-config` | cloth-config-15.0.140-neoforge | `cloth-config` |
| dependency | `cobweb` | cobweb-neoforge-1.21-1.4.0 | `cobweb` |
| dependency | `codechicken-lib-1-8` | CodeChickenLib-1.21.1-4.6.1.529 | `codechicken-lib-1-8` |
| dependency | `codxlib` | 1.5.1+neoforge-1.21.1 | `codxlib` |
| dependency | `common-network` | common-networking-neoforge-1.0.21-1.21.1 | `common-network` |
| dependency | `cucumber` | Cucumber-1.21.1-8.0.16 | `cucumber` |
| dependency | `curios-api` | curios-neoforge-9.5.1+1.21.1 | `curios` |
| dependency | `cyclops-core` | cyclopscore-1.21.1-neoforge-1.30.1 | `cyclops-core` |
| dependency | `databank` | databank-1.3.2.1 | `databank` |
| dependency | `exclusions-lib` | ExclusionsLib-1.1.0-NEO | `exclusions-lib` |
| dependency | `forbidden-magic` | witchery_forbidden_magic-0.5.37+mc1.21.1 | `jawr-forbidden-magic` |
| dependency | `ftb-filter-system` | ftb-filter-system-neoforge-21.1.4 | `ftb-filter-system` |
| dependency | `ftb-library` | ftb-library-neoforge-2101.1.39 | `ftb-library-forge` |
| dependency | `ftb-teams` | ftb-teams-neoforge-2101.1.12 | `ftb-teams-forge` |
| dependency | `ftb-xmod-compat` | ftb-xmod-compat-neoforge-21.1.14 | `ftb-xmod-compat` |
| dependency | `fusion-connected-textures` | 1.3.16-neoforge-mc1.21.1 | `fusion-connected-textures` |
| dependency | `geckolib` | geckolib-neoforge-1.21.1-4.9.3 | `geckolib` |
| dependency | `hammerlib` | HammerLib-1.21-21.0.17 | `hammer-lib` |
| dependency | `irons-lib` | irons_lib-1.21.1-2.3.0 | `irons-lib` |
| dependency | `kambrik` | 8.0.0-beta.2 | `kambrik` |
| dependency | `konkrete` | konkrete_neoforge_1.9.9_MC_1.21 | `konkrete` |
| dependency | `kotlin-for-forge` | kotlinforforge-5.12.0-all | `kotlin-for-forge` |
| dependency | `ldlib2` | 2.2.46.a | `ldlib2` |
| dependency | `lionfish-api` | lionfishapi-3.1 | `lionfish-api` |
| dependency | `lithostitched` | lithostitched-1.8.1-neoforge-21.1 | `lithostitched` |
| dependency | `lodestone` | lodestone-1.21.1-1.8.2 | `lodestonelib` |
| dependency | `mezzconfig` | mezz_config-1.21.1-neoforge-0.7.1-standalone | `mezzconfig` |
| dependency | `modonomicon` | modonomicon-1.21.1-neoforge-1.123.0 | `modonomicon` |
| dependency | `modopedia` | modopedia-neoforge-1.21.1-1.1.11 | `modopedia` |
| dependency | `observerlib` | observerlib-1.10.3 | `observerlib` |
| dependency | `octolib` | 0.6.2 | `octolib` |
| dependency | `owo-lib` | 0.12.15.6-beta.1+1.21 | `owo-lib` |
| dependency | `patchouli` | Patchouli-1.21.1-93-NEOFORGE | `patchouli` |
| dependency | `paucal` | paucal-0.7.0+neoforge-1.21.1 | `paucal` |
| dependency | `placebo` | Placebo-1.21.1-9.9.4 | `placebo` |
| dependency | `playeranimator` | player-animation-lib-forge-2.0.4+1.21.1 | `playeranimator` |
| dependency | `prickle` | prickle-neoforge-1.21.1-21.1.11 | `prickle` |
| dependency | `puffish-attributes` | puffish_attributes-0.8.3-1.21-neoforge | `puffish-attributes` |
| dependency | `puzzles-lib` | 21.1.60 | `puzzles-lib` |
| dependency | `quest-giver` | quest_giver-1.21.1-1.5.3 | `quest-giver` |
| dependency | `rhino` | rhino-2101.2.7-build.85 | `rhino` |
| dependency | `runiclib` | 5.0.7 | `runiclib` |
| dependency | `searchables` | Searchables-neoforge-1.21.1-1.0.2 | `searchables` |
| dependency | `smartbrainlib` | SmartBrainLib-neoforge-1.21.1-1.16.11 | `smartbrainlib` |
| dependency | `stateobserver` | stateobserver-neoforge-1.21.1-1.6.4 | `stateobserver` |
| dependency | `supermartijn642-config-lib` | 1.1.8-neoforge-mc1.21 | `supermartijn642s-config-lib` |
| dependency | `supermartijn642-core-lib` | 1.1.20-neoforge-mc1.21 | `supermartijn642s-core-lib` |
| dependency | `tectonic` | tectonic-3.0.28-neoforge-21.1 | `tectonic` |
| dependency | `titanium` | titanium-1.21-4.0.52 | `titanium` |
| dependency | `unfocused` | unfocused-neoforge-0.3.12-1.21.1-sgd | `unfocused` |
| dependency | `valhelsia-core` | valhelsia_core-neoforge-1.21.1-1.1.4 | `valhelsia-core` |
| dependency | `worldweaver-new-dawn` | 21.0.25 | `worldweaver-new-dawn` |
| dependency | `wunderlib-new-dawn` | 21.0.10 | `wunderlib-new-dawn` |
| emc_progression | `project-expansion` | projectexpansion-1.21.1-1.0.6 | `project-expansion` |
| emc_progression | `projecte-energy-converters` | projecte-energy-0.1.4 | `projecte-energy-converters` |
| food | `aquaculture` | Aquaculture-1.21.1-2.7.22 | `aquaculture` |
| food | `aquaculture-delight` | 1.2.0 | `aquaculture-delight` |
| food | `cooking-for-blockheads` | 21.1.24+neoforge-1.21.1 | `cooking-for-blockheads` |
| food | `crispy` | 1.1 | `crispy` |
| food | `dungeons-delight` | 1.5.3 | `dungeons-delight` |
| food | `enders-delight` | 1.3.1 | `enders-delight` |
| food | `farmers-delight` | FarmersDelight-1.21.1-1.3.4 | `farmers-delight` |
| food | `farming-for-blockheads` | farmingforblockheads-neoforge-1.21.1-21.1.14 | `farming-for-blockheads` |
| food | `huaxia-delight` | 1.2.0 | `huaxia-delight` |
| food | `lets-do-brewery` | 2.1.13 | `lets-do-brewery` |
| food | `lets-do-farm-and-charm` | 1.1.29 | `lets-do-farm-and-charm` |
| food | `lets-do-herbalbrews` | 1.1.4 | `lets-do-herbalbrews` |
| food | `my-nethers-delight` | MyNethersDelight-1.21.1-1.10.4.1.jar | `my-nethers-delight` |
| food | `pams-harvestcraft-2-crops` | pamhc2crops-NEOFORGE-1.21.1-1.0.9 | `pams-harvestcraft-2-crops` |
| food | `pams-harvestcraft-2-food-core` | pamhc2foodcore-NEOFORGE-1.21.1-1.0.4 | `pams-harvestcraft-2-food-core` |
| food | `pams-harvestcraft-2-food-extended` | pamhc2foodextended-NEOFORGE-1.21.1-1.0.0 | `pams-harvestcraft-2-food-extended` |
| food | `pams-harvestcraft-2-trees` | pamhc2trees-NEOFORGE-1.21.1-1.0.9 | `pams-harvestcraft-2-trees` |
| food_beverage | `brewin-and-chewin` | 4.5.0+1.21.1-neoforge | `brewin-and-chewin` |
| food_beverage | `cocktails-delight` | Cocktails-Delight-1.21.1-NeoForge-1.1.12.jar | `cocktails-delight` |
| housing | `prefab-custom-addon` | 2.7.1 | `prefab-custom-addon` |
| infrastructure | `ftb-chunks` | ftb-chunks-neoforge-2101.1.24 | `ftb-chunks-forge` |
| infrastructure | `ftb-quests` | ftb-quests-neoforge-2101.1.38 | `ftb-quests-forge` |
| infrastructure | `kubejs` | kubejs-neoforge-2101.7.2-build.381 | `kubejs` |
| infrastructure | `theurgy-kubejs` | 1.21.1-neoforge-1.6.0 | `theurgy-kubejs` |
| late_ore | `replication` | Replication-1.21.1-1.2.7 | `replication` |
| library | `ars-conflux` | 0.1.56 | `ars-conflux` |
| library | `fragmentum-neoforge` | 5.4.0+1.21.1 | `fragmentum-neoforge` |
| library | `lets-do-foundation` | 1.1.0 | `lets-do-foundation` |
| library | `resourceful-config` | 3.0.11 | `resourceful-config` |
| library | `resourceful-lib` | 3.0.12 | `resourceful-lib` |
| library | `villagerstradingplus` | 1.1.1 | `villagerstradingplus` |
| library | `yacl` | 3.8.2+1.21.1-neoforge | `yacl` |
| magic | `ars-nouveau` | ars_nouveau-1.21.1-5.13.5 | `ars-nouveau` |
| magic | `astral-sorcery` | AstralSorcery-2.0.3.59 | `astral-sorcery` |
| magic | `aura-cascade-reimagined` | 0.2.1+1.21.1 | `aura-cascade-reimagined` |
| magic | `botania-revived` | Botania-Revived-1.21.1-2.0.2-NeoForge | `botania-revived` |
| magic | `eidolon-repraised` | eidolon_repraised-1.21.1-0.5.0.8 | `eidolon-repraised` |
| magic | `enchanted-witchcraft` | enchanted-neoforge-1.21.1-4.2.7 | `enchanted-witchcraft` |
| magic | `evilcraft` | evilcraft-1.21.1-neoforge-1.2.102 | `evilcraft` |
| magic | `forbidden-arcanus` | forbidden_arcanus-2.6.1 | `forbidden-arcanus` |
| magic | `goety` | goety-3.2.3 | `goety` |
| magic | `hexcast-neoforged` | hexcast-neoforged-1.21.1-0.11.1-19+neoforge.1.21.1 | `hexcast-neoforged` |
| magic | `hexerei` | hexerei-0.5.0.3 | `hexerei` |
| magic | `irons-spells` | irons_spellbooks-1.21.1-3.16.3 | `irons-spells-n-spellbooks` |
| magic | `mahou-tsukai` | mahoutsukai-1.21.1-v1.36.31 | `mahou-tsukai` |
| magic | `malum` | malum-1.21.1-1.8.2 | `malum` |
| magic | `neovitae` | neovitae-1.21.1-1.1.54 | `neovitae` |
| magic | `occultism` | occultism-1.21.1-neoforge-1.227.0 | `occultism` |
| magic | `pastel` | Pastel-1.1.5.5 | `pastel` |
| magic | `primal-magick` | primalmagick-neoforge-1.21.1-6.0.7 | `primal-magick` |
| magic | `psi` | Psi-neoforge-1.21.1-110 | `psi` |
| magic | `roots-classic` | RootsClassic-1.21.1-1.5.10 | `roots-classic` |
| magic | `scriptor-magicae` | scriptor-neoforge-2.0.14-1.21.1-sgd | `scriptor-magicae` |
| magic | `theurgy` | theurgy-1.21.1-neoforge-1.79.0 | `theurgy` |
| magic | `totemic` | Totemic-neoforge-1.21.1-0.12.18 | `fenns_totemic` |
| magic | `witchery-remake` | witchery-0.5.39+mc1.21.1 | `just-another-witchery-remake` |
| magic_addon | `aquamirae-nouveau` | 0.1.3 | `aquamirae-nouveau` |
| magic_addon | `arcane-archives-reborn` | 1.0.2 | `arcane-archives-reborn` |
| magic_addon | `ars-additions` | ars_additions-1.21.1-21.3.0 | `ars-additions` |
| magic_addon | `ars-controle` | ars_controle-1.21.1-1.6.16 | `ars-controle` |
| magic_addon | `ars-elemancy` | ars_elemancy-1.21.1-1.18.3 | `ars-elemancy` |
| magic_addon | `ars-elemental` | ars_elemental-1.21.1-0.7.11.3 | `ars-elemental` |
| magic_addon | `ars-n-spells` | ars_n_spells-3.3.5 | `ars-n-spells` |
| magic_addon | `ars-ocultas` | ars_ocultas-1.21.1-2.6.2 | `ars-ocultas` |
| magic_addon | `ars-unification` | ars_unification-1.2.23 | `ars-unification` |
| magic_addon | `extra-botany-revived` | ExtraBotany-1.21.1-1.0.4-NeoForge | `extra-botany-revived` |
| magic_addon | `gaze-malum` | gaze-1.1.7.1 | `gaze-a-malum-addon` |
| magic_addon | `goety-cataclysm` | goety_cataclysm-1.21.1-1.8.4 | `goety-cataclysm` |
| magic_addon | `occult-cooking` | 1.0.1 | `occult-cooking` |
| magic_addon | `psi-tweaks` | psitweaks-1.21.1-0.10.10-hotfix | `psitweaks` |
| magic_addon | `unicorn-academy-bonded-magic-unofficial` | 1.21.1-1.0.0 | `unicorn-academy-bonded-magic-unofficial` |
| magic_adventure | `feywild` | feywild-1.21.1-5.6.4 | `feywild` |
| magic_industry | `embers-re-ignited` | embers-reignited-1.21.1-1.5.9 | `embers-re-ignited` |
| magic_progression | `projecte` | ProjectE-1.21.1-PE1.1.0 | `projecte` |
| magic_utility | `reliquary-reincarnations` | reliquary-1.21.1-2.0.85.1599 | `reliquary-reincarnations` |
| performance | `embeddium` | embeddium-1.0.15+mc1.21.1 | `embeddium` |
| performance | `entity-culling` | entityculling-neoforge-1.12.1-mc1.21.1 | `entityculling` |
| performance | `ferritecore` | ferritecore-7.0.3-neoforge | `ferrite-core` |
| performance | `immediatelyfast` | ImmediatelyFast-NeoForge-1.6.15+1.21.1 | `immediatelyfast` |
| performance | `modernfix` | modernfix-neoforge-5.27.27+mc1.21.1 | `modernfix` |
| performance_resource_generation | `cobblegen-galore` | cobblegengalore-1.21.1-0.2.9.jar | `cobblegen-galore` |
| power | `apotheosis` | Apotheosis-1.21.1-8.9.0 | `apotheosis` |
| power | `artifacts` | artifacts-neoforge-13.2.5 | `artifacts` |
| power | `enigmatic-legacy-plus` | enigmaticlegacyplus-1.21.1-1.1.2 | `enigmaticlegacy-plus` |
| power | `relics` | 0.12.5 | `relics` |
| power | `reliquified-artifacts` | 1.0.9 | `reliquified-artifacts` |
| qol | `akashic-tome` | AkashicTome-1.8-30 | `akashic-tome` |
| qol | `appleskin` | appleskin-neoforge-mc1.21-3.0.9 | `appleskin` |
| qol | `attributefix` | attributefix-neoforge-1.21.1-21.1.3 | `attributefix` |
| qol | `better-advancements` | BetterAdvancements-NeoForge-1.21.1-0.4.3.21 | `better-advancements` |
| qol | `bountiful` | 8.0.0-beta.2 | `bountiful` |
| qol | `clumps` | 19.0.0.1 | `clumps` |
| qol | `colorful-hearts` | 10.5.9 | `colorful-hearts` |
| qol | `comforts` | comforts-neoforge-9.0.5+1.21.1 | `comforts` |
| qol | `controlling` | Controlling-neoforge-1.21.1-19.0.5 | `controlling` |
| qol | `corail-tombstone` | tombstone-neoforge-1.21.1-9.5.6 | `corail-tombstone` |
| qol | `crafting-tweaks` | craftingtweaks-neoforge-1.21.1-21.1.11 | `crafting-tweaks` |
| qol | `default-options` | defaultoptions-neoforge-1.21.1-21.1.8 | `default-options` |
| qol | `enchantment-descriptions` | enchdesc-neoforge-1.21.1-21.1.11 | `enchantment-descriptions` |
| qol | `extreme-sound-muffler` | 3.57-1.21.1 | `extreme-sound-muffler` |
| qol | `ftb-essentials` | 2101.1.10 | `ftb-essentials` |
| qol | `get-it-together-drops` | v1.4 | `get-it-together-drops` |
| qol | `harvest-with-ease` | harvest-with-ease-neoforge-1.21-9.4.0 | `harvest-with-ease` |
| qol | `inventory-tweaks-refoxed` | invtweaks-1.21.1-1.3.2 | `inventory-tweaks-refoxed` |
| qol | `jade` | Jade-1.21.1-NeoForge-15.10.6 | `jade` |
| qol | `jei` | jei-1.21.1-neoforge-19.64.0.455 | `jei` |
| qol | `just-enough-professions` | 4.0.5 | `just-enough-professions` |
| qol | `just-enough-resources-jer` | 1.6.0.57 | `just-enough-resources-jer` |
| qol | `just-zoom` | justzoom_neoforge_3.1.0_MC_1.21.1 | `just-zoom` |
| qol | `keybind-bundles` | keybindbundles-1.5.0 | `keybind-bundles` |
| qol | `keysets` | keyset-neoforge-1.3.0+mc1.21.1 | `keysets` |
| qol | `leaves-be-gone` | v21.1.1-1.21.1-NeoForge | `leaves-be-gone` |
| qol | `lootjs` | 1.21.1-3.7.0+neoforge | `lootjs` |
| qol | `lootr` | lootr-neoforge-1.21.1-1.11.38.127 | `lootr` |
| qol | `more-darkness` | 2.0.1+1.21.1+neoforge | `more-darkness` |
| qol | `more-overlays-updated` | moreoverlays-1.24.2-mc1.21.1-neoforge | `more-overlays-updated` |
| qol | `mouse-tweaks` | MouseTweaks-neoforge-mc1.21-2.26.1 | `mouse-tweaks` |
| qol | `natures-compass` | NaturesCompass-1.21.1-3.4.0-neoforge | `natures-compass` |
| qol | `oracle-index` | oracle_index-neoforge-1.4.1 | `oracle-index` |
| qol | `polymorph` | polymorph-neoforge-1.2.0+1.21.1 | `polymorph` |
| qol | `prefab` | 1.0.9 NeoForge 1.21.1 | `prefab` |
| qol | `smart-keep-inventory` | 1.21.1-1.0.1-neoforge | `smart-keep-inventory` |
| qol | `swing-through-grass` | 1.1.0 | `swing-through-grass` |
| qol | `trashslot` | 21.1.11+neoforge-1.21.1 | `trashslot` |
| qol | `treechop` | 0.19.3 | `treechop` |
| ritual_framework | `summoning-rituals` | summoningrituals-neoforge-1.21.1-3.14.2 | `summoningrituals` |
| server_utility | `simple-backups` | SimpleBackups-1.21-4.0.30 | `simple-backups` |
| server_utility | `spark` | spark-1.10.124-neoforge | `spark` |
| skills | `improvable-skills` | ImprovableSkills-1.21-21.0.2 | `improvable-skills` |
| supernatural | `dragon-survival` | 2.0.74 | `dragon-survival` |
| technology | `cc-tweaked` | cc-tweaked-1.21.1-forge-1.121.0 | `cc-tweaked` |
| technology | `draconic-evolution` | Draconic-Evolution-1.21.1-3.1.4.633 | `draconic-evolution` |
| utility | `multiplayer-spawners` | 2.0.2 | `multiplayer-spawners` |
| utility | `q-shop` | qshop-neoforge-1.21.1-1.8.5 | `q-shop` |
| worldgen | `betterend-new-dawn` | 21.0.39 | `betterend-new-dawn` |
| worldgen | `yungs-api` | 1.21.1-NeoForge-5.1.9 | `yungs-api` |
| worldgen | `yungs-better-desert-temples` | 1.21.1-NeoForge-4.1.5 | `yungs-better-desert-temples` |
| worldgen | `yungs-better-dungeons` | 1.21.1-NeoForge-5.1.0 | `yungs-better-dungeons` |
| worldgen | `yungs-better-end-island` | 1.21.1-NeoForge-3.1.2 | `yungs-better-end-island` |
| worldgen | `yungs-better-jungle-temples` | 1.21.1-NeoForge-3.1.0 | `yungs-better-jungle-temples` |
| worldgen | `yungs-better-mineshafts` | 1.21.1-NeoForge-5.1.1 | `yungs-better-mineshafts` |
| worldgen | `yungs-better-nether-fortresses` | 1.21.1-NeoForge-3.1.5 | `yungs-better-nether-fortresses` |
| worldgen | `yungs-better-ocean-monuments` | 1.21.1-NeoForge-4.1.1 | `yungs-better-ocean-monuments` |
| worldgen | `yungs-better-strongholds` | 1.21.1-NeoForge-5.1.3 | `yungs-better-strongholds` |
| worldgen | `yungs-better-witch-huts` | 1.21.1-NeoForge-4.1.1 | `yungs-better-witch-huts` |
| worldgen | `yungs-bridges` | 1.21.1-NeoForge-5.1.0 | `yungs-bridges` |
| worldgen | `yungs-extras` | 1.21.1-NeoForge-5.1.0 | `yungs-extras` |

### Currently disabled catalog items (12)

| Catalog ID | Desired? | Current state |
| --- | :---: | --- |
| `botania` | no | Disabled in manifest / not selected in Packwiz |
| `construction-gadgets` | no | Disabled in manifest / not selected in Packwiz |
| `deep-aether` | yes | Disabled in manifest / not selected in Packwiz |
| `formation-wand` | no | Disabled in manifest / not selected in Packwiz |
| `liroth-revamped` | yes | Disabled in manifest / not selected in Packwiz |
| `lootr-liaison` | yes | Disabled in manifest / not selected in Packwiz |
| `magical-satchel` | yes | Disabled in manifest / not selected in Packwiz |
| `nuit` | yes | Disabled in manifest / not selected in Packwiz |
| `puffish-skills` | no | Disabled in manifest / not selected in Packwiz |
| `starlight-structures` | yes | Disabled in manifest / not selected in Packwiz |
| `twilight-lootr` | yes | Disabled in manifest / not selected in Packwiz |
| `tyzs-skills` | no | Disabled in manifest / not selected in Packwiz |

**Old snapshot below is retained as historical evidence only.** Its repeated "not yet integrated" statements (especially Q Shop, five food additions, Multiplayer Spawners, Swing Through Grass) and 242/10/252 numeric totals must not be read as live tasks.

---

## ORIGINAL FROZEN 242-MOD AUDIT — provenance only

> **Scope authority:** [#390](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/390) and the owner's November 23 decision. This is a **roster/disposition lock for the rest of the Thanksgiving-to-Christmas cycle**, not a mass activation or gameplay/QA PASS. All additions below were already owner-approved. The separate [#534 resurrection register](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/534) records historical work and [#431](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/431) controls inclusion slices. Do not invent new mods from archived examples or research notes.

## Exact evidence at snapshot

- Branch `test/full-integration` at **`23b974a2b427e98fd2cf6d35ccf93fab06f96b12`**; target Minecraft **1.21.1**, NeoForge **21.1.265**, Java **21**. Full server/Prism/individual-mod gameplay verification is not established by this static source audit.
- Source `manifest/mods.json`, blob **`0457afe0293117a0bed878d57e3d3af927a16025`**: **252** catalog entries: **242 enabled** and **10 disabled**.
- Source `index.toml`, blob **`95f021d735c7897bc900a875c445ee1319838347`**: **242** entries under `mods/*.pw.toml`. **All 242 enabled manifest rows resolve to matching current indexed Packwiz slugs; no unmatched or orphaned mod pin entries.** Record by logical ID, not literal file slug (21 logical IDs have known different file slugs).
- **Approved target envelope:** 242 active third-party/mod artifacts + **9 additional selected next-comprehensive-QA JARs** + **one Christmas-targeted Weeping Angels JAR** = **up to 252 indexed external mod files** if all candidates safely load and none are replaced/retired. This is **not** a promised running mod count, assembled build count or QA PASS. The approved **BetterEnd Lootr conversion** is a *datapack/verified tags* item, not an eleventh mod JAR.
- Five currently declared first-party modules are tracked separately by `manifest/first-party-runtime.json`: `tyr_progression`, `tmm_transformations`, `traders_vengence`, `tmm_economy`, `tmm_friendship_garden`. The planned Transformations-to-Progression architectural consolidation changes runtime packaging later and **must not be counted as an external mod addition**.
- Cathobb Unified 64x is a separate **indexed client resource pack**, not a mod JAR. Its client rendering/enablement still requires B/C testing.

## A. Ten approved external additions beyond today's indexed 242

The following entries are **selected** for the holiday cycle. A technical failure requires a reproducible **BLOCKED** finding, not silent removal, and a build-time OWNER-DEFERRED entry needs an explicit owner instruction. Selection never authorizes bypassing dependency/provenance/CI checks.

| Window | Selected item | Approved version | Proposed mod slug | Authority | Outstanding gate |
| --- | --- | --- | --- | --- | --- |
| Preview attempt | Multiplayer Spawners | 2.0.2 | multiplayer-spawners | [#542](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/542) / [#543](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/543) | Publisher GitHub asset SHA published; verify actual binary, native deps, 10s torch/default policy |
| Preview attempt | Lootr Liaison | 1.3.0+1.21.1-neoforge | lootr-liaison | #542 / #543 | Official CF 1445934/9028796; exact byte-hash and dependencies pending |
| Preview attempt | Twilight Lootr | 0.0.0.1 | twilight-lootr | #542 / #543 | Official CF 1589409/8327180; exact byte-hash and dependencies pending |
| Preview attempt | Crispy! | 1.1 | crispy | [#444](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/444) / [#551](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/551) | Publisher binary audited; pin/loader and recipe QA pending |
| Preview attempt | Dungeon's Delight | 1.5.3 (publisher filename ends 1.5.2) | dungeons-delight | #444 / #551 | Publisher binary audited; verify published/native version and RunicLib dependency |
| Preview attempt | Ender's Delight | 1.3.1 | enders-delight | #444 / #551 | Publisher binary audited; native Farmer's Delight version declaration needs runtime resolution |
| Preview attempt | Huaxia Delight | 1.2.0 | huaxia-delight | #444 / #551 | Owner-uploaded exact JAR inspected, SHA1 8c4eb34973a6f2ca5d0aac29e1bdff065bfc6919; publisher download 403; provenance distinct |
| Preview attempt | RunicLib (dependency) | 5.0.7 | runiclib | #444 / #551 | Mandatory Dungeon's Delight library; publisher JAR audited |
| Preview attempt | Swing Through Grass | 1.1.0 | swing-through-grass | [#384](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/384) / [#385](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/385) | Official CF 1477279/9063127; must validate exact hash and import; blaze/stew scripts already indexed |
| Christmas alpha | Doctor Who: Weeping Angels | 1.0.5 candidate | weeping-angels | [#387](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/387) / [#392](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/392) | Explicitly deferred from Thanksgiving; exact binary native 1.21.1 compatibility, traps, claims, physical-only damage, 19-dimension blacklist required by Christmas |

## A1. Approved replacement rather than an additional mod

**Quest Shop → Q Shop is a true mod swap, not an eleventh additive JAR.** [#523](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/523) / [PR #531](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/531) select **Q Shop 1.8.5** to replace the indexed **Quest Shop 1.4.0**, migrating all **48** old offers to six personal-wallet tabs. This replacement is **approved but not yet integrated**, and the integration writer must ensure **exactly one shop mod** survives in the live index, with equivalent offers, valid currency migration and quests. Until the swap source is integrated the live 242-pin snapshot correctly still contains Quest Shop. A clean Q Shop swap changes the count by **zero** (one pin out, one in). If blocked in the preview, record the exact blocker and leave the old shop functional; do not silently install both.

**One extra approved next-build nonmod installation obligation:** [#542](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/542) BetterEnd Lootr compatibility. Choose **one** valid strategy: reviewed `kubejs/data/lootr/tags/block/convert/{chests,barrels}.json` from PR #543 **or** the fully verified compatible standalone BetterEnd Lootr ZIP datapack, never both. Preserve full personal Lootr chests, not Fair Share. The three selected multiplayer JARs need proper per-player spawner control: vanilla torch extinguish at 10 seconds, magic light intact, `enableGroupDisabling=false`.

**Christmas feature commitment separate from the mod count:** [Chaos Bunny #549](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/549) is **preview-deferred / Christmas alpha-targeted**, alongside Weeping Angels. This is existing TMM/KubeJS/Progression content, **not** a new third-party mod. Its incomplete five-boss persistent lifecycle must not be advertised as Thanksgiving-complete. Do not merge the draft #556 as a finished runtime before its five-boss logic is coherent.

## B. 242 already selected and Packwiz-indexed mods — fixed current baseline

Rows are copied from the exact source catalog and matched to indexed `mods/*.pw.toml` slugs. Treat `version` as the recorded source-candidate version, **not** proof that an actual client resolved and executed it. The source loader and Packwiz file hashes remain authoritative.

| Catalog category | Logical mod ID | Recorded version | Indexed Packwiz slug |
| --- | --- | --- | --- |
| adventure | `aether` | 1.21.1-1.5.10-neoforge | `aether` |
| adventure | `alexs-caves-continued` | 1.1.2+neoforge-1.21.1 | `alexs-caves-continued` |
| adventure | `aquamirae` | 7.2.13 | `aquamirae` |
| adventure | `born-in-chaos` | 1.7.6 | `born-in-chaos` |
| adventure | `cataclysm` | L_Ender's Cataclysm 1.21.1-3.33 | `l_enders-cataclysm` |
| adventure | `creature-feature` | 1.3.0 | `creature-feature` |
| adventure | `creeper-overhaul` | 4.0.6 | `creeper-overhaul` |
| adventure | `critters-and-companions` | 1.21.1-2.4.0 | `critters-and-companions` |
| adventure | `deeper-darker` | deeperdarker-neoforge-1.21.1-1.4.1 | `deeperdarker` |
| adventure | `dive-into-the-abyss` | 1.1.4 | `dive-into-the-abyss` |
| adventure | `dungeon-crawl` | mc1.21.1-2.3.17 | `dungeon-crawl` |
| adventure | `enderman-overhaul` | 2.0.3 | `enderman-overhaul` |
| adventure | `eternal-starlight` | 0.9.1+1.21.1+neoforge | `eternal-starlight` |
| adventure | `gateways-to-eternity` | GatewaysToEternity-1.21.1-5.1.0 | `gateways-to-eternity` |
| adventure | `ghost` | 1.3.4 | `ghost` |
| adventure | `golem-overhaul` | 1.1.1 | `golem-overhaul` |
| adventure | `lava-fishing` | lavafishing-1.21.1-1.2.13-all.jar | `lava-fishing` |
| adventure | `mowzies-mobs` | 1.8.2 | `mowzies-mobs` |
| adventure | `nether-depths-upgrade` | netherdepthsupgrade-3.3-1.21.1-NeoForge.jar | `nether-depths-upgrade` |
| adventure | `ribbits` | 4.1.6 | `ribbits` |
| adventure | `sons-of-sins` | 2.2.1d | `sons-of-sins` |
| adventure | `swem` | 1.21.1-1.6.14 | `swem` |
| adventure | `swem-addendum` | 1.21.1-1.0.1 | `swem-addendum` |
| adventure | `the-aurorian-unofficial-port` | 1.21.1-1.0.6 | `the-aurorian-unofficial-port` |
| adventure | `the-bumblezone` | 7.17.3+1.21.1-neoforge | `the-bumblezone` |
| adventure | `the-graveyard` | 4.0.1 | `the-graveyard` |
| adventure | `twilight-forest` | 4.8.3345 | `twilight-forest` |
| adventure | `twilight-forest-final-boss-remake` | 2.1.2 | `twilight-forest-final-boss-remake` |
| adventure | `voidscape` | 1.9.588 | `voidscape` |
| adventure | `winter-overhaul` | 4.0.2+1.21.1-neoforge | `winter-overhaul` |
| compatibility | `almostunified` | AlmostUnified-NeoForge-1.21.1-1.4.2 | `almostunified` |
| decoration | `rechiseled` | rechiseled-1.2.5-neoforge-mc1.21 | `rechiseled` |
| decoration | `scarlet-springs-equestrian-decor-ssed` | 1.21.1-1.2.1 | `scarlet-springs-equestrian-decor-ssed` |
| decoration_beach | `lets-do-beachparty` | letsdo-beachparty-neoforge-2.1.5.jar | `lets-do-beachparty` |
| decoration_furniture | `lets-do-furniture` | letsdo-furniture-neoforge-1.1.11.jar | `lets-do-furniture` |
| decoration_glass | `crystalix` | crystalix-3.0.3.jar | `crystalix` |
| dependency | `apothic-attributes` | ApothicAttributes-1.21.1-2.11.0 | `apothic-attributes` |
| dependency | `apothic-enchanting` | ApothicEnchanting-1.21.1-1.6.4 | `apothic-enchanting` |
| dependency | `apothic-spawners` | ApothicSpawners-1.21.1-1.4.0 | `apothic-spawners` |
| dependency | `architectury-api` | architectury-13.0.11-neoforge | `architectury-api` |
| dependency | `balm` | balm-neoforge-1.21.1-21.0.67 | `balm` |
| dependency | `bclib-new-dawn` | 21.0.26 | `bclib-new-dawn` |
| dependency | `better-advanced-tooltips` | better-advanced-tooltips-2101.1.0-build.5 | `better-advanced-tooltips` |
| dependency | `blueprint` | 8.1.0 | `blueprint` |
| dependency | `bookshelf` | bookshelf-neoforge-1.21.1-21.1.81 | `bookshelf` |
| dependency | `brandons-core` | BrandonsCore-1.21.1-3.2.1.309 | `brandons-core` |
| dependency | `caelus` | caelus-neoforge-7.0.1+1.21.1 | `caelus` |
| dependency | `cloth-config` | cloth-config-15.0.140-neoforge | `cloth-config` |
| dependency | `cobweb` | cobweb-neoforge-1.21-1.4.0 | `cobweb` |
| dependency | `codechicken-lib-1-8` | CodeChickenLib-1.21.1-4.6.1.529 | `codechicken-lib-1-8` |
| dependency | `codxlib` | 1.5.1+neoforge-1.21.1 | `codxlib` |
| dependency | `common-network` | common-networking-neoforge-1.0.21-1.21.1 | `common-network` |
| dependency | `cucumber` | Cucumber-1.21.1-8.0.16 | `cucumber` |
| dependency | `curios-api` | curios-neoforge-9.5.1+1.21.1 | `curios` |
| dependency | `cyclops-core` | cyclopscore-1.21.1-neoforge-1.30.1 | `cyclops-core` |
| dependency | `databank` | databank-1.3.2.1 | `databank` |
| dependency | `exclusions-lib` | ExclusionsLib-1.1.0-NEO | `exclusions-lib` |
| dependency | `forbidden-magic` | witchery_forbidden_magic-0.5.37+mc1.21.1 | `jawr-forbidden-magic` |
| dependency | `ftb-filter-system` | ftb-filter-system-neoforge-21.1.4 | `ftb-filter-system` |
| dependency | `ftb-library` | ftb-library-neoforge-2101.1.39 | `ftb-library-forge` |
| dependency | `ftb-teams` | ftb-teams-neoforge-2101.1.12 | `ftb-teams-forge` |
| dependency | `ftb-xmod-compat` | ftb-xmod-compat-neoforge-21.1.14 | `ftb-xmod-compat` |
| dependency | `fusion-connected-textures` | 1.3.16-neoforge-mc1.21.1 | `fusion-connected-textures` |
| dependency | `geckolib` | geckolib-neoforge-1.21.1-4.9.3 | `geckolib` |
| dependency | `hammerlib` | HammerLib-1.21-21.0.17 | `hammer-lib` |
| dependency | `irons-lib` | irons_lib-1.21.1-2.3.0 | `irons-lib` |
| dependency | `kambrik` | 8.0.0-beta.2 | `kambrik` |
| dependency | `konkrete` | konkrete_neoforge_1.9.9_MC_1.21 | `konkrete` |
| dependency | `kotlin-for-forge` | kotlinforforge-5.12.0-all | `kotlin-for-forge` |
| dependency | `ldlib2` | 2.2.46.a | `ldlib2` |
| dependency | `lionfish-api` | lionfishapi-3.1 | `lionfish-api` |
| dependency | `lithostitched` | lithostitched-1.8.1-neoforge-21.1 | `lithostitched` |
| dependency | `lodestone` | lodestone-1.21.1-1.8.2 | `lodestonelib` |
| dependency | `mezzconfig` | mezz_config-1.21.1-neoforge-0.7.1-standalone | `mezzconfig` |
| dependency | `modonomicon` | modonomicon-1.21.1-neoforge-1.123.0 | `modonomicon` |
| dependency | `modopedia` | modopedia-neoforge-1.21.1-1.1.11 | `modopedia` |
| dependency | `observerlib` | observerlib-1.10.3 | `observerlib` |
| dependency | `octolib` | 0.6.2 | `octolib` |
| dependency | `owo-lib` | 0.12.15.6-beta.1+1.21 | `owo-lib` |
| dependency | `patchouli` | Patchouli-1.21.1-93-NEOFORGE | `patchouli` |
| dependency | `paucal` | paucal-0.7.0+neoforge-1.21.1 | `paucal` |
| dependency | `placebo` | Placebo-1.21.1-9.9.4 | `placebo` |
| dependency | `playeranimator` | player-animation-lib-forge-2.0.4+1.21.1 | `playeranimator` |
| dependency | `prickle` | prickle-neoforge-1.21.1-21.1.11 | `prickle` |
| dependency | `puffish-attributes` | puffish_attributes-0.8.3-1.21-neoforge | `puffish-attributes` |
| dependency | `puzzles-lib` | 21.1.60 | `puzzles-lib` |
| dependency | `quest-giver` | quest_giver-1.21.1-1.5.3 | `quest-giver` |
| dependency | `rhino` | rhino-2101.2.7-build.85 | `rhino` |
| dependency | `searchables` | Searchables-neoforge-1.21.1-1.0.2 | `searchables` |
| dependency | `smartbrainlib` | SmartBrainLib-neoforge-1.21.1-1.16.11 | `smartbrainlib` |
| dependency | `stateobserver` | stateobserver-neoforge-1.21.1-1.6.4 | `stateobserver` |
| dependency | `supermartijn642-config-lib` | 1.1.8-neoforge-mc1.21 | `supermartijn642s-config-lib` |
| dependency | `supermartijn642-core-lib` | 1.1.20-neoforge-mc1.21 | `supermartijn642s-core-lib` |
| dependency | `tectonic` | tectonic-3.0.28-neoforge-21.1 | `tectonic` |
| dependency | `titanium` | titanium-1.21-4.0.52 | `titanium` |
| dependency | `unfocused` | unfocused-neoforge-0.3.12-1.21.1-sgd | `unfocused` |
| dependency | `valhelsia-core` | valhelsia_core-neoforge-1.21.1-1.1.4 | `valhelsia-core` |
| dependency | `worldweaver-new-dawn` | 21.0.25 | `worldweaver-new-dawn` |
| dependency | `wunderlib-new-dawn` | 21.0.10 | `wunderlib-new-dawn` |
| emc_progression | `project-expansion` | projectexpansion-1.21.1-1.0.6 | `project-expansion` |
| emc_progression | `projecte-energy-converters` | projecte-energy-0.1.4 | `projecte-energy-converters` |
| food | `aquaculture` | Aquaculture-1.21.1-2.7.22 | `aquaculture` |
| food | `aquaculture-delight` | 1.2.0 | `aquaculture-delight` |
| food | `cooking-for-blockheads` | 21.1.24+neoforge-1.21.1 | `cooking-for-blockheads` |
| food | `farmers-delight` | FarmersDelight-1.21.1-1.3.4 | `farmers-delight` |
| food | `farming-for-blockheads` | farmingforblockheads-neoforge-1.21.1-21.1.14 | `farming-for-blockheads` |
| food | `lets-do-brewery` | 2.1.13 | `lets-do-brewery` |
| food | `lets-do-farm-and-charm` | 1.1.29 | `lets-do-farm-and-charm` |
| food | `lets-do-herbalbrews` | 1.1.4 | `lets-do-herbalbrews` |
| food | `my-nethers-delight` | MyNethersDelight-1.21.1-1.10.4.1.jar | `my-nethers-delight` |
| food | `pams-harvestcraft-2-crops` | pamhc2crops-NEOFORGE-1.21.1-1.0.9 | `pams-harvestcraft-2-crops` |
| food | `pams-harvestcraft-2-food-core` | pamhc2foodcore-NEOFORGE-1.21.1-1.0.4 | `pams-harvestcraft-2-food-core` |
| food | `pams-harvestcraft-2-food-extended` | pamhc2foodextended-NEOFORGE-1.21.1-1.0.0 | `pams-harvestcraft-2-food-extended` |
| food | `pams-harvestcraft-2-trees` | pamhc2trees-NEOFORGE-1.21.1-1.0.9 | `pams-harvestcraft-2-trees` |
| food_beverage | `brewin-and-chewin` | 4.5.0+1.21.1-neoforge | `brewin-and-chewin` |
| food_beverage | `cocktails-delight` | Cocktails-Delight-1.21.1-NeoForge-1.1.12.jar | `cocktails-delight` |
| housing | `prefab-custom-addon` | 2.7.1 | `prefab-custom-addon` |
| infrastructure | `ftb-chunks` | ftb-chunks-neoforge-2101.1.24 | `ftb-chunks-forge` |
| infrastructure | `ftb-quests` | ftb-quests-neoforge-2101.1.38 | `ftb-quests-forge` |
| infrastructure | `kubejs` | kubejs-neoforge-2101.7.2-build.381 | `kubejs` |
| infrastructure | `theurgy-kubejs` | 1.21.1-neoforge-1.6.0 | `theurgy-kubejs` |
| late_ore | `replication` | Replication-1.21.1-1.2.7 | `replication` |
| library | `ars-conflux` | 0.1.56 | `ars-conflux` |
| library | `fragmentum-neoforge` | 5.4.0+1.21.1 | `fragmentum-neoforge` |
| library | `lets-do-foundation` | 1.1.0 | `lets-do-foundation` |
| library | `resourceful-config` | 3.0.11 | `resourceful-config` |
| library | `resourceful-lib` | 3.0.12 | `resourceful-lib` |
| library | `villagerstradingplus` | 1.1.1 | `villagerstradingplus` |
| library | `yacl` | 3.8.2+1.21.1-neoforge | `yacl` |
| magic | `ars-nouveau` | ars_nouveau-1.21.1-5.13.5 | `ars-nouveau` |
| magic | `astral-sorcery` | AstralSorcery-2.0.3.59 | `astral-sorcery` |
| magic | `aura-cascade-reimagined` | 0.2.1+1.21.1 | `aura-cascade-reimagined` |
| magic | `botania-revived` | Botania-Revived-1.21.1-2.0.2-NeoForge | `botania-revived` |
| magic | `eidolon-repraised` | eidolon_repraised-1.21.1-0.5.0.8 | `eidolon-repraised` |
| magic | `enchanted-witchcraft` | enchanted-neoforge-1.21.1-4.2.7 | `enchanted-witchcraft` |
| magic | `evilcraft` | evilcraft-1.21.1-neoforge-1.2.102 | `evilcraft` |
| magic | `forbidden-arcanus` | forbidden_arcanus-2.6.1 | `forbidden-arcanus` |
| magic | `goety` | goety-3.2.3 | `goety` |
| magic | `hexcast-neoforged` | hexcast-neoforged-1.21.1-0.11.1-19+neoforge.1.21.1 | `hexcast-neoforged` |
| magic | `hexerei` | hexerei-0.5.0.3 | `hexerei` |
| magic | `irons-spells` | irons_spellbooks-1.21.1-3.16.3 | `irons-spells-n-spellbooks` |
| magic | `mahou-tsukai` | mahoutsukai-1.21.1-v1.36.31 | `mahou-tsukai` |
| magic | `malum` | malum-1.21.1-1.8.2 | `malum` |
| magic | `neovitae` | neovitae-1.21.1-1.1.54 | `neovitae` |
| magic | `occultism` | occultism-1.21.1-neoforge-1.227.0 | `occultism` |
| magic | `pastel` | Pastel-1.1.5.5 | `pastel` |
| magic | `primal-magick` | primalmagick-neoforge-1.21.1-6.0.7 | `primal-magick` |
| magic | `psi` | Psi-neoforge-1.21.1-110 | `psi` |
| magic | `roots-classic` | RootsClassic-1.21.1-1.5.10 | `roots-classic` |
| magic | `scriptor-magicae` | scriptor-neoforge-2.0.14-1.21.1-sgd | `scriptor-magicae` |
| magic | `theurgy` | theurgy-1.21.1-neoforge-1.79.0 | `theurgy` |
| magic | `totemic` | Totemic-neoforge-1.21.1-0.12.18 | `fenns_totemic` |
| magic | `witchery-remake` | witchery-0.5.39+mc1.21.1 | `just-another-witchery-remake` |
| magic_addon | `aquamirae-nouveau` | 0.1.3 | `aquamirae-nouveau` |
| magic_addon | `arcane-archives-reborn` | 1.0.2 | `arcane-archives-reborn` |
| magic_addon | `ars-additions` | ars_additions-1.21.1-21.3.0 | `ars-additions` |
| magic_addon | `ars-controle` | ars_controle-1.21.1-1.6.16 | `ars-controle` |
| magic_addon | `ars-elemancy` | ars_elemancy-1.21.1-1.18.3 | `ars-elemancy` |
| magic_addon | `ars-elemental` | ars_elemental-1.21.1-0.7.11.3 | `ars-elemental` |
| magic_addon | `ars-n-spells` | ars_n_spells-3.3.5 | `ars-n-spells` |
| magic_addon | `ars-ocultas` | ars_ocultas-1.21.1-2.6.2 | `ars-ocultas` |
| magic_addon | `ars-unification` | ars_unification-1.2.23 | `ars-unification` |
| magic_addon | `extra-botany-revived` | ExtraBotany-1.21.1-1.0.4-NeoForge | `extra-botany-revived` |
| magic_addon | `gaze-malum` | gaze-1.1.7.1 | `gaze-a-malum-addon` |
| magic_addon | `goety-cataclysm` | goety_cataclysm-1.21.1-1.8.4 | `goety-cataclysm` |
| magic_addon | `occult-cooking` | 1.0.1 | `occult-cooking` |
| magic_addon | `psi-tweaks` | psitweaks-1.21.1-0.10.10-hotfix | `psitweaks` |
| magic_addon | `unicorn-academy-bonded-magic-unofficial` | 1.21.1-1.0.0 | `unicorn-academy-bonded-magic-unofficial` |
| magic_adventure | `feywild` | feywild-1.21.1-5.6.4 | `feywild` |
| magic_industry | `embers-re-ignited` | embers-reignited-1.21.1-1.5.9 | `embers-re-ignited` |
| magic_progression | `projecte` | ProjectE-1.21.1-PE1.1.0 | `projecte` |
| magic_utility | `reliquary-reincarnations` | reliquary-1.21.1-2.0.85.1599 | `reliquary-reincarnations` |
| performance | `embeddium` | embeddium-1.0.15+mc1.21.1 | `embeddium` |
| performance | `entity-culling` | entityculling-neoforge-1.12.1-mc1.21.1 | `entityculling` |
| performance | `ferritecore` | ferritecore-7.0.3-neoforge | `ferrite-core` |
| performance | `immediatelyfast` | ImmediatelyFast-NeoForge-1.6.15+1.21.1 | `immediatelyfast` |
| performance | `modernfix` | modernfix-neoforge-5.27.27+mc1.21.1 | `modernfix` |
| performance_resource_generation | `cobblegen-galore` | cobblegengalore-1.21.1-0.2.9.jar | `cobblegen-galore` |
| power | `apotheosis` | Apotheosis-1.21.1-8.9.0 | `apotheosis` |
| power | `artifacts` | artifacts-neoforge-13.2.5 | `artifacts` |
| power | `enigmatic-legacy-plus` | enigmaticlegacyplus-1.21.1-1.1.2 | `enigmaticlegacy-plus` |
| power | `relics` | 0.12.5 | `relics` |
| power | `reliquified-artifacts` | 1.0.9 | `reliquified-artifacts` |
| qol | `akashic-tome` | AkashicTome-1.8-30 | `akashic-tome` |
| qol | `appleskin` | appleskin-neoforge-mc1.21-3.0.9 | `appleskin` |
| qol | `attributefix` | attributefix-neoforge-1.21.1-21.1.3 | `attributefix` |
| qol | `better-advancements` | BetterAdvancements-NeoForge-1.21.1-0.4.3.21 | `better-advancements` |
| qol | `bountiful` | 8.0.0-beta.2 | `bountiful` |
| qol | `clumps` | 19.0.0.1 | `clumps` |
| qol | `colorful-hearts` | 10.5.9 | `colorful-hearts` |
| qol | `comforts` | comforts-neoforge-9.0.5+1.21.1 | `comforts` |
| qol | `controlling` | Controlling-neoforge-1.21.1-19.0.5 | `controlling` |
| qol | `corail-tombstone` | tombstone-neoforge-1.21.1-9.5.6 | `corail-tombstone` |
| qol | `crafting-tweaks` | craftingtweaks-neoforge-1.21.1-21.1.11 | `crafting-tweaks` |
| qol | `default-options` | defaultoptions-neoforge-1.21.1-21.1.8 | `default-options` |
| qol | `enchantment-descriptions` | enchdesc-neoforge-1.21.1-21.1.11 | `enchantment-descriptions` |
| qol | `extreme-sound-muffler` | 3.57-1.21.1 | `extreme-sound-muffler` |
| qol | `ftb-essentials` | 2101.1.10 | `ftb-essentials` |
| qol | `get-it-together-drops` | v1.4 | `get-it-together-drops` |
| qol | `harvest-with-ease` | harvest-with-ease-neoforge-1.21-9.4.0 | `harvest-with-ease` |
| qol | `inventory-tweaks-refoxed` | invtweaks-1.21.1-1.3.2 | `inventory-tweaks-refoxed` |
| qol | `jade` | Jade-1.21.1-NeoForge-15.10.6 | `jade` |
| qol | `jei` | jei-1.21.1-neoforge-19.64.0.455 | `jei` |
| qol | `just-enough-professions` | 4.0.5 | `just-enough-professions` |
| qol | `just-enough-resources-jer` | 1.6.0.57 | `just-enough-resources-jer` |
| qol | `just-zoom` | justzoom_neoforge_3.1.0_MC_1.21.1 | `just-zoom` |
| qol | `keybind-bundles` | keybindbundles-1.5.0 | `keybind-bundles` |
| qol | `keysets` | keyset-neoforge-1.3.0+mc1.21.1 | `keysets` |
| qol | `leaves-be-gone` | v21.1.1-1.21.1-NeoForge | `leaves-be-gone` |
| qol | `lootjs` | 1.21.1-3.7.0+neoforge | `lootjs` |
| qol | `lootr` | lootr-neoforge-1.21.1-1.11.38.127 | `lootr` |
| qol | `more-darkness` | 2.0.1+1.21.1+neoforge | `more-darkness` |
| qol | `more-overlays-updated` | moreoverlays-1.24.2-mc1.21.1-neoforge | `more-overlays-updated` |
| qol | `mouse-tweaks` | MouseTweaks-neoforge-mc1.21-2.26.1 | `mouse-tweaks` |
| qol | `natures-compass` | NaturesCompass-1.21.1-3.4.0-neoforge | `natures-compass` |
| qol | `oracle-index` | oracle_index-neoforge-1.4.1 | `oracle-index` |
| qol | `polymorph` | polymorph-neoforge-1.2.0+1.21.1 | `polymorph` |
| qol | `prefab` | 1.0.9 NeoForge 1.21.1 | `prefab` |
| qol | `smart-keep-inventory` | 1.21.1-1.0.1-neoforge | `smart-keep-inventory` |
| qol | `trashslot` | 21.1.11+neoforge-1.21.1 | `trashslot` |
| qol | `treechop` | 0.19.3 | `treechop` |
| ritual_framework | `summoning-rituals` | summoningrituals-neoforge-1.21.1-3.14.2 | `summoningrituals` |
| server_utility | `simple-backups` | SimpleBackups-1.21-4.0.30 | `simple-backups` |
| server_utility | `spark` | spark-1.10.124-neoforge | `spark` |
| skills | `improvable-skills` | ImprovableSkills-1.21-21.0.2 | `improvable-skills` |
| supernatural | `dragon-survival` | 2.0.74 | `dragon-survival` |
| technology | `cc-tweaked` | cc-tweaked-1.21.1-forge-1.121.0 | `cc-tweaked` |
| technology | `draconic-evolution` | Draconic-Evolution-1.21.1-3.1.4.633 | `draconic-evolution` |
| utility | `quest-shop` | questshop-1.4.0 | `quest-shop` |
| worldgen | `betterend-new-dawn` | 21.0.39 | `betterend-new-dawn` |
| worldgen | `yungs-api` | 1.21.1-NeoForge-5.1.9 | `yungs-api` |
| worldgen | `yungs-better-desert-temples` | 1.21.1-NeoForge-4.1.5 | `yungs-better-desert-temples` |
| worldgen | `yungs-better-dungeons` | 1.21.1-NeoForge-5.1.0 | `yungs-better-dungeons` |
| worldgen | `yungs-better-end-island` | 1.21.1-NeoForge-3.1.2 | `yungs-better-end-island` |
| worldgen | `yungs-better-jungle-temples` | 1.21.1-NeoForge-3.1.0 | `yungs-better-jungle-temples` |
| worldgen | `yungs-better-mineshafts` | 1.21.1-NeoForge-5.1.1 | `yungs-better-mineshafts` |
| worldgen | `yungs-better-nether-fortresses` | 1.21.1-NeoForge-3.1.5 | `yungs-better-nether-fortresses` |
| worldgen | `yungs-better-ocean-monuments` | 1.21.1-NeoForge-4.1.1 | `yungs-better-ocean-monuments` |
| worldgen | `yungs-better-strongholds` | 1.21.1-NeoForge-5.1.3 | `yungs-better-strongholds` |
| worldgen | `yungs-better-witch-huts` | 1.21.1-NeoForge-4.1.1 | `yungs-better-witch-huts` |
| worldgen | `yungs-bridges` | 1.21.1-NeoForge-5.1.0 | `yungs-bridges` |
| worldgen | `yungs-extras` | 1.21.1-NeoForge-5.1.0 | `yungs-extras` |

## C. Inactive catalog records — **not** hidden installation requirements

Exactly ten current catalog rows are deliberately **not** Packwiz active. Two rows (`magical-satchel`, `starlight-structures`) were superseded as unresolved identities rather than proof of a missing mod: the former names an Occultism-native satchel, while the latter has no confirmed provider. Deep Aether/Liroth/Nuit are **held for compatibility**, with known binary/dependency obstacles and no automatic Christmas promotion; preserve intent but do not expand the selected 252 without a new owner decision. Other disabled rows record old choices/retirements.

| Inactive logical ID | Desired in historical catalog? | Current disposition |
| --- | --- | --- |
| `botania` | no | Original Botania port not selected; Botania Revived is indexed |
| `construction-gadgets` | no | Rejected building gadget duplicate |
| `deep-aether` | yes | Technical hold; exact 1.21.1 JAR's declared Minecraft range excludes 1.21.1, and TerraBlender beta prerequisites unresolved |
| `formation-wand` | no | No standalone JAR; native Astral Formation/Conversion wands |
| `liroth-revamped` | yes | Technical/owner hold; Nuit beta and additional BetterEnd-family libraries need closure |
| `magical-satchel` | yes | Superseded catalog identity; native Occultism satchel, not a distinct JAR |
| `puffish-skills` | no | Retired historical skills layer; separate Pufferfish Attributes remains indexed |
| `starlight-structures` | yes | Unresolved historical project identity; do not guess/pin |
| `tyzs-skills` | no | Retired by unified Progression; do not restore standalone wallet |
| `nuit` | yes | Held dependency; Liroth not selected for current builds |

## D. Explicitly excluded mods / families recorded in the catalog

No inclusion from these may be inferred from old proposals, PRs, or code references. Reconsideration requires a new explicit owner approval/scope exception.

| Excluded ID | Current repository reason |
| --- | --- |
| `swem-ranch-hands` | Owner declined inclusion in the 2026-11-17 equestrian batch; do not add without new approval. |
| `neo-mo-creatures` | Owner declined inclusion in the 2026-11-17 equestrian batch; do not add without new approval. |
| `create` | hard design exclusion |
| `blood-magic-original` | hard design exclusion |
| `sophisticated-storage` | hard design exclusion |
| `generic-backpacks` | hard design exclusion |
| `generic-item-logistics` | hard design exclusion |
| `applied-energistics-2` | hard design exclusion |
| `refined-storage` | hard design exclusion |
| `ftb-ultimine` | hard design exclusion |
| `separate-fe-transfer` | hard design exclusion |
| `elementalcraft` | User explicitly rejected ElementalCraft; do not propose or install. |
| `natures-aura` | User explicitly rejected Nature's Aura; do not propose or install. |
| `forcecraft` | User explicitly rejected ForceCraft; do not propose or install. |
| `mystical-agriculture` | Pre-existing user design exclusion reaffirmed; resource crops undermine the intended magical resource progression. |
| `mystical-agradditions` | Excluded with Mystical Agriculture and its ecosystem/addons. |
| `witchcraft-and-curses` | Current pack decision: omit Witchcraft and Curses. Not selected in Tell Me More triage; minor overlapping ritual content and additional biome/trees. |
| `pagans-blessing` | Current pack decision: omit Pagan's Blessing. Not selected in Tell Me More triage; redundant small ritual system. |
| `runology` | Current pack decision: omit Runology. Not selected in Tell Me More triage; maturity/content concerns. Revisit only on explicit user change. |
| `apothecary` | Current pack decision: omit Apothecary. Not selected in Tell Me More triage. User does not want another potion/effect system interfering with the many existing alchemy mechanics. |
| `magitech-arcane-engineering` | Current pack decision: omit Magitech — Arcane Engineering. Not selected in Tell Me More triage; separate tech/resource/biome ecosystem conflicts with design. |
| `relics-rpg-series` | Current pack decision: omit Relics (RPG Series). Not selected in Tell Me More triage; avoid introducing RPG Series spellcasting/Spell Engine dependencies. Distinct from existing base Relics mod. |
| `jewelry-rpg-series` | Current pack decision: omit Jewelry (RPG Series). Not selected in Tell Me More triage; avoid pulling in RPG Series spellcasting/attribute dependencies. |
| `runic-curiosities` | Current pack decision: omit Runic Curiosities. Not selected in Tell Me More triage; immature and power-balance concerns. |
| `aspect-alchemy` | User explicitly removed after source-level review: limited added progression value relative to Neo Vitae, Ars Nouveau and Botania brewing; hard-coded aspects, custom integration work and known Jade 15.10.6 conflict. Do not install. |
| `extended-crafting` | Owner explicitly excluded following previous on-hold decision. Extra ultimate crafting technology competes with Draconic Evolution and ProjectE Gem Armor endgame. Do not install, grant as loot, or create quest progression. |
| `re-avaritia` | Owner explicitly excluded following previous on-hold decision. Infinity-tier armor/tools undermine Draconic Evolution and ProjectE Gem Armor as signature endgame. Do not install, grant as loot, or create quest progression. |
| `building-gadgets-2` | Owner explicitly rejected Building Gadgets during building audit 2026-11-15 |
| `bigger-reactors` | Owner removed from roster 2026-11-15. Multiple native magical FE generation paths plus Draconic Evolution make a nuclear technology progression redundant; no uranium conversion/ore gate needed. |
| `bigger-reactors-rework` | Owner removed from roster 2026-11-15. Multiple native magical FE generation paths plus Draconic Evolution make a nuclear technology progression redundant; no uranium conversion/ore gate needed. |
| `vampirism` | Owner-retired; JAWR remains physiology owner |
| `werewolves` | Owner-retired; JAWR remains physiology owner |
| `vampires-delight` | Owner-retired; JAWR remains physiology owner |
| `aquaculture-2-lava-fishing-addon` | Not selected alongside full Lava Fishing 1.2.13. Source from TeamMetallurgy/Aquaculture2-Lava-Fishing-Addon duplicates six aquaculture hook names registered by Lava Fishing; it adds no native loot. Reconsider only if full Lava Fishing is removed or source registration is reconciled. |
| `explorers-compass` | Owner declined; the existing native Ars Additions Locate Structure ritual is extended instead. |

## E. Deeper recovery scan, source owners, and why nothing else was added

- **Open review population:** GitHub API returned **99 open PRs** and **89 open issues** during the November 23 sweep. Many PRs are stacked feature/Progression design, source-only QA, or unmerged historical pin donors. None of their titles or checked selected-mod intake contracts authorizes an additional currently missing *independent* third-party JAR beyond section A. See [resurrection register #534](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/534) for detailed inherited-source classifications and outstanding work.
- Earlier **seven-mob roster #261** is already covered by existing pins: Creeper Overhaul, Enderman Overhaul, Golem Overhaul, Winter Overhaul, Ghost, Ribbits, Critters and Companions; **Torchmaster remains excluded**.
- **#95 decorative proposal** is not a bulk addition mandate. Already chosen Glass/Beachparty/Rechiseled/Furniture/Brewin' and Chewin'/Cocktails entries appear in the current indexed roster; other unselected alternatives do not become Christmas dependencies by age. Existing Architecture scope explicitly rejects Create; unrelated external tech-pack gun/Create conversations are not approvals for TMM.
- Older external content proposals **#398/#399/#403/#406/#425** were recovered into indexed source by **#524/#528** where approved and compatible. Do not re-pin those old heads. Occult Cooking, Creature Feature, Twilight finale, and Arcane Archives are already present. **The full TeamLapen/Vampire's Delight port is deferred**; narrowly approved first-party JAWR cuisine #450 is a separate feature, not a new mod.
- Quest/Progression, Q Shop swap, food-provenance handler, Capture, Trader's Vengeance, Chaos Bunny and Angel compatibility remain **code/integration or QA work**; they are not reason to open unsolicited independent mod intake. Q Shop **replaces** an existing shop mod rather than adding a second permanent parallel shop (if approved swap integrates, count external replacement as net-zero).
- **Scope lock:** For the rest of the holiday development cycle, new third-party content outside the 242 baseline + ten selected additions must be reviewed as a documented exception under #390 (necessary hard dependency; demonstrable simplification/replacement; blocking compatibility/security fix). No speculative new mod inclusion. Necessary dependency resolution does **not** permit silent optional addons.
- **Before the Christmas integration freeze**, revisit all ten pending entries (including Angels) and nonmod BetterEnd Lootr conversion, recording **INCLUDED / BLOCKED with exact binary/API evidence / OWNER-DEFERRED with a dated owner citation**. Keep historic approval even if a JAR cannot load; do not claim the cycle is successful merely from 252 rows.

## Validation boundary

**Type A only:** live issue/PR/manifest/Packwiz source verification. There was **no new Integration Assembly, Server Boot Smoke, Prism package, real Minecraft client, or gameplay QA** in this roster audit. Any present source pin only means selected for a candidate, not playable. The integration writer owns all actual shared-index promotion and associated release decisions.
