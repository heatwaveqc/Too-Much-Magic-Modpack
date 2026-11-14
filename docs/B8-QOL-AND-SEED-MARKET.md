# B8 QoL addition decision (2026-11-13 local)

## User-selected additions
The user approved **Nature's Compass and the other QoL mods** recommended in the previous ATM10 shortlist, excluding **Waystones**. This selects ten functional QoL additions and the seed-market mod asked about, with three supporting libraries:

| Canonical ID | Feature | CurseForge project | ATM10 current matched 1.21.1 jar |
|---|---|---:|---|
| akashic-tome | combine mod documentation books | 250577 | AkashicTome-1.8-30.jar |
| crafting-tweaks | rotate, distribute and clear crafting grid | 233071 | craftingtweaks-neoforge-1.21.1-21.1.11.jar |
| simple-backups | timed backups; number and disk space cap | 583228 | SimpleBackups-1.21-4.0.30.jar |
| keybind-bundles | radial selection of many magic keybinds | 1172594 | keybindbundles-1.5.0.jar |
| enchantment-descriptions | enchantment tooltips | 250419 | enchdesc-neoforge-1.21.1-21.1.11.jar |
| better-advancements | advancements user interface | 272515 | BetterAdvancements-NeoForge-1.21.1-0.4.3.21.jar |
| oracle-index | in-game ModdedMC.wiki viewer | 1206582 | oracle_index-neoforge-1.4.1.jar |
| more-overlays-updated | light/spawn, chunk, JEI chest search | 391382 | moreoverlays-1.24.2-mc1.21.1-neoforge.jar |
| spark | profiler for server/client | 361579 | spark-1.10.124-neoforge.jar |
| natures-compass | find distant biomes (explicitly approved) | 252848 | NaturesCompass-1.21.1-3.4.0-neoforge.jar |
| farming-for-blockheads | **seed and sapling Market**, separate from Farmer's Delight; sprinklers and husbandry | 261924 | farmingforblockheads-neoforge-1.21.1-21.1.14.jar |
| balm | required library (Crafting Tweaks, Farming for Blockheads) | 531761 | balm-neoforge-1.21.1-21.0.67.jar |
| bookshelf | required library (Enchantment Descriptions) | 228525 | bookshelf-neoforge-1.21.1-21.1.81.jar |
| prickle | required configuration library | 1023259 | prickle-neoforge-1.21.1-21.1.11.jar |

Source: official ATM10 `config/crash_assistant/modlist.json` from https://github.com/AllTheMods/ATM-10, plus mod-author CurseForge project pages. This uses the **ATM10 release snapshot** except **Oracle Index 1.4.1 (NeoForge MC 1.21.1 stable, CurseForge file 9506889)**, verified from its official project listing: https://www.curseforge.com/minecraft/mc-mods/oracle-index/files/9506889 . Newer upstream files are never accepted silently.

## Intentional exclusions / pending decisions
- **Waystones** explicitly declined. No Waystones mod or teleportation stone recipe.
- **Explorer's Compass** was not approved. This adds **Nature's Compass (biomes)** only, not structure location.
- Also do not add: FTB Essentials (free /home), generic logistics, AE2, Refined Storage, Create, Sophisticated modules, FTB Ultimine, unrelated optional ATM10 candidates. These remain excluded or pending.
- **Almost Unified** is a separate recipe/ore unification review. Never switch it on before uranium, draconic dust and replication ore gating audit. Extra UI/HUD/animation suggestions stay pending.

## Market gameplay caution
**The seed Market is from Farming for Blockheads, NOT Farmer's Delight.** Compatibility with Pam's Crops and Farmer's Delight is attractive but must be validated in a disposable world. The Market can expose modded seeds and saplings, potentially including Ars Nouveau Magebloom/Archwood and other rare magic ingredients. For this initial *boot test* run, record every purchasable item in the actual Market UI, including optional presets and dynamic tags. **Before a survival-balanced release**, install a documented whitelist/blacklist config or replace Market trades to allow mundane agriculture without bypassing magical progression. DO NOT invent registry item IDs or assume unsupported market presets. Without validated market restriction, this remains an experimental client-test build.

## Acceptance
- Exact Minecraft 1.21.1 NeoForge provider artifact IDs, checksums, and dependency closure for all 13.
- No duplicate mods, no Waystones/Explorer's Compass.
- Preserve prior known-good 126-mod B7 baseline.
- Dedicated-server boot plus fresh Prism GUI/world client check. Client-only GUI features can't be proven by a headless server.
- Backup retention config and a trial restoration on a disposable world before relying on Simple Backups for important saves.

## Follow-up: Prickle and beta status
- **Prickle** was automatically installed as a required configuration-library dependency of this exact QoL selection. Explicitly register its stable MC 1.21.1 NeoForge build (CurseForge project 1023259, file 6961457); it provides no progression-changing features.
- **Simple Backups 1.21-4.0.30 is beta-tagged** on CurseForge (file 7965880). Treat it as an experimental test in this pack, not a production-safety guarantee. Test backup retention, disk usage, successful restore before relying on it for valuable saved worlds. https://www.curseforge.com/minecraft/mc-mods/simple-backups/files/7965880
