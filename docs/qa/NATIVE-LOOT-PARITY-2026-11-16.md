# Monk virtual Fortune / Hunter favored Looting — implementation and QA

Source: `feature/native-monk-fortune-hunter-looting-2026-11-16`
Base: `test/full-integration`; first-party mod: `tmm_progression`.

## Before this patch
- Monk `HarvestAbilities.blockDrops()` used a virtual enchanted Netherite pickaxe to call `Block.getDrops()` a second time for *both* Fortune and Silk modes, then replaced original drops. This could bypass mod-custom loot pipelines and duplicate any downstream execution with its own side effects.
- Hunter favored-prey selection already doubled eligible kill XP; it did **not** affect loot. No baseline favored Looting was applied.

## After this patch
- `MonkFortuneMixin` targets Minecraft `ApplyBonusCount.run`'s original `calculateNewCount` argument. On the server, if the loot context identifies the real empty-handed player and an eligible block, it adds **Fortune I** from native Treasure Hand and **up to Fortune III extra** from existing Tyz `fortunate_hands` ranks (original total I–IV). No tool item is created, drops are not cleared, and the original loot function is executed once.
- Only the existing **Silk Touch** mode still uses the synthetic tool and block loot override. Silk mode gives zero virtual Fortune; it should receive a separate native-context implementation before removing the last synthetic-tool branch.
- `HunterLootingMixin` adds **effective Looting I** at `EnchantedCountIncreaseFunction.run`, and `HunterRareLootMixin` does the same for `LootItemRandomChanceWithEnchantedBonusCondition.test`. It checks the LOOTING enchantment key; real loot victim and damage source must identify the actual player killer; Hunter must be joined, target must be eligible, and the purchased favored enemy creature tag must match.
- Projectile kills credited to the player's damage source qualify. Players, friendly tames, excluded/non-prey categories, unrelated creatures, simulated drops, and non-player kills do not.
- Existing actual Fortune/Looting levels are additive. No entity tick handlers or extra item entities are introduced.

## Required gameplay QA (NO server/client test yet)
1. Bare Monk fist, rank 12 Treasure Hand, Fortune mode: normal ore and Fortune-sensitive plants should roll once with Fortune I; Tyz Fortunate Hands upgrades from I to IV. No synthetic pickaxe should enter inventory or affect drops. Test ore requiring pickaxe against original hand-harvest requirements, modded loot tables, empty offhand and survival/creative.
2. Monk switches to Silk: native virtual Fortune becomes zero; existing Silk re-roll continues, one physical drop set; switch back to Fortune and confirm no stale Silk. Test glass/Glass Touch, non-pickaxe tools and block entities (remain protected).
3. Hunters: favored zombie with and without actual Looting weapons, with arrows, vs nonfavored and friendly/tamed entities; verify additive Looting I where loot table uses it and rare drops such as heads/boss-specific loot are not manufactured. Test no double XP/extra drop entities.
4. Class selection and progression respec remove bonuses; multiple players with different paths receive only their own entitlements.
5. Integrate Gilded experimental `GildedFortuneMixin`, `GildedLootingMixin`, `GildedRareLootMixin` with the same targets: compile/launch together, verify additive transformation orders without duplicated loot, and retain Gilded's gold-gear restrictions. This branch does not import Gilded's separate work.
6. Confirm server and client mixin metadata loads all three new injectors in the actual assembled JAR, not just in source.

## Validation gates
Lightweight standalone Gradle compilation and JUnit CI may execute on push. A compiled source is not evidence of successful runtime injection. No full server smoke, Prism package or in-world gameplay test was run or authorized. No packwiz runtime source lock was altered.
