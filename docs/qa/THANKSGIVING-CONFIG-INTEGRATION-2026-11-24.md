# Thanksgiving configuration and quest integration crosswalk

Baseline `e744f166a6a82781ef7017bea93e524bfd940404`. Designated integration writer combines independent approved donor paths for combined hash/CI review; no duplicate feature implementation. Exact crosswalk:

## #699 — 090dfa282296e7c44e3bc3fe02755e9e4be40d43

- `config/ftbquests/quests/chapters/intro.snbt`
- `config/ftbquests/quests/lang/en_us.snbt`
- `manifest/quest_content.json`
- `manifest/quest_content_resolved.json`
- `scripts/rebuild_quest_ui.py`
- `scripts/validate_newbie_integration.py`
- `scripts/validate_quests.py`
- `config/ftbquests/quests/reward_tables/starter_homes.snbt`
- `manifest/starter_home_choices.json`

## #394 — 8e95950272151ee839be659059f6347267169d60

- `config/defaultoptions/keybindings.txt`
- `scripts/generate_defaultoptions_keybindings.py`
- `scripts/validate_defaultoptions_keybindings.py`
- `docs/qa/REFOXED-MIDDLE-CLICK-2026-11-20.md`

## #338 — 1f885eb81f12019cccee6f086b31ca5a340512b5

- `kubejs/assets/tmm/lang/en_us.json`
- `.github/workflows/occultism-batch1-source.yml`
- `docs/qa/OCCULTISM-BATCH1-338-2026-11-24.md`
- `kubejs/data/occultism/modonomicon/books/dictionary_of_spirits/entries/crafting_rituals/craft_ancient_miner.json`
- `kubejs/data/occultism/modonomicon/books/dictionary_of_spirits/entries/crafting_rituals/tmm_perpetual_life.json`
- `kubejs/data/occultism/modonomicon/books/dictionary_of_spirits/entries/getting_started/books_of_binding.json`
- `kubejs/data/occultism/modonomicon/books/dictionary_of_spirits/entries/getting_started/spirit_fire.json`
- `kubejs/data/occultism/recipe/crafting/book_of_binding_afrit.json`
- `kubejs/data/occultism/recipe/crafting/book_of_binding_afrit_from_empty.json`
- `kubejs/data/occultism/recipe/crafting/book_of_binding_djinni.json`
- `kubejs/data/occultism/recipe/crafting/book_of_binding_djinni_from_empty.json`
- `kubejs/data/occultism/recipe/crafting/book_of_binding_foliot.json`
- `kubejs/data/occultism/recipe/crafting/book_of_binding_foliot_from_empty.json`
- `kubejs/data/occultism/recipe/crafting/book_of_binding_marid.json`
- `kubejs/data/occultism/recipe/crafting/book_of_binding_marid_from_empty.json`
- `kubejs/data/occultism/recipe/ritual/misc_miner_ancient_eldritch.json`
- `kubejs/data/tmm/recipe/ritual/perpetual_life.json`
- `kubejs/data/tmm/recipe/spirit_fire/apotheosis_gem_chipped.json`
- `kubejs/data/tmm/recipe/spirit_fire/apotheosis_gem_cracked.json`
- `kubejs/data/tmm/recipe/spirit_fire/apotheosis_gem_flawed.json`
- `kubejs/data/tmm/recipe/spirit_fire/apotheosis_gem_flawless.json`
- `kubejs/data/tmm/recipe/spirit_fire/apotheosis_gem_normal.json`
- `kubejs/data/tmm/recipe/spirit_fire/apotheosis_gem_perfect.json`
- `tests/test_occultism_batch1_contract.py`

## #339 — 7c91aa7f066ae57daa468fce97272edcefdd5ff7

- `docs/MAGICAL-INDUSTRY-BATCH3-HANDOFF.md`
- `kubejs/data/botania/recipe/petal_apothecary/orechid.json`
- `kubejs/data/draconicevolution/neoforge/biome_modifier/end_comet.json`
- `kubejs/data/draconicevolution/neoforge/biome_modifier/end_draconium_ore.json`
- `kubejs/data/draconicevolution/neoforge/biome_modifier/nether_draconium_ore.json`
- `kubejs/data/draconicevolution/neoforge/biome_modifier/overworld_draconium_ore.json`
- `kubejs/data/neovitae/recipe/meteor/diamond.json`
- `kubejs/data/neovitae/recipe/meteor/iron.json`
- `kubejs/data/neovitae/recipe/meteor/nether.json`
- `kubejs/data/neovitae/recipe/meteor/nether_star.json`
- `kubejs/data/neovitae/recipe/meteor/stone.json`
- `kubejs/data/occultism/recipe/miner/ores/draconium_ore.json`
- `kubejs/data/occultism/recipe/miner/ores/uranium_ore.json`
- `kubejs/data/tmm/forbidden_arcanus/hephaestus_forge/ritual/draconium_dust.json`
- `kubejs/data/tmm/recipe/altar/draconium_dust.json`
- `kubejs/data/tmm/recipe/miner/draconium_ore_eldritch.json`
- `kubejs/data/tmm/recipe/orechid/deepslate_draconium_ore.json`
- `kubejs/data/tmm/recipe/orechid/deepslate_replica_ore.json`
- `kubejs/data/tmm/recipe/orechid/draconium_ore.json`

## #343 — 38ee634578c15a3acf3c17289005fc0a632bfea5

- `compat/economy/src/main/java/io/github/heatwaveqc/tmm/economy/spawn/LostSoulNaturalSpawns.java`
- `compat/economy/src/main/java/io/github/heatwaveqc/tmm/economy/spawn/LostSoulSpawnPolicy.java`
- `compat/economy/src/test/java/io/github/heatwaveqc/tmm/economy/spawn/LostSoulSpawnPolicyTest.java`
- `config/twilightforest-common.toml`
- `docs/LOOT-FOOD-COMPAT-BATCH5-HANDOFF.md`
- `kubejs/data/betterend/recipe/sulphur_gunpowder.json`
- `kubejs/data/c/tags/item/dusts/sulfur.json`
- `kubejs/data/eidolon_repraised/recipe/sulfur.json`
- `kubejs/data/forbidden_arcanus/neoforge/biome_modifier/add_lost_soul_overworld.json`

## #345 — fded440534367e724a574d3d817fb3099d39b00b

- `defaultconfigs/enigmaticlegacyplus-server.toml`
- `docs/GENERAL-CONFIG-QUEST-LABELS-BATCH6-HANDOFF.md`

Shared index rows selected only for corresponding runtime paths; baseline SHA256 checked against pack.toml, combined CI must verify imported hashes. #345 only approved config and historical handoff; no older quest labels or layout replay. Native provider review limitations retained from donor PRs, no game test or full-build authorization.
