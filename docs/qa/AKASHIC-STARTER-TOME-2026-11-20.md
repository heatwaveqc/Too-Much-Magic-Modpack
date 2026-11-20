# Populated starter Akashic Tome — source first pass (#306)

**2026-11-20 / source staged:** no assembled NeoForge, Prism or gameplay validation. Built from the exact pinned Akashic Tome 1.8-30 data-component source and confirmed guidebook registry/resource IDs. Deliberately conservative until a complete installed-JAR guidebook registry is available.

## Native representation

In [Akashic Tome 1.8-30](https://github.com/VazkiiMods/AkashicTome/tree/release-1.21.1-1.8-30), `Registries.TOOL_CONTENT` registers `akashictome:tool_content` as a `ToolContentComponent` backed by `ItemStack.CODEC.listOf()`. The native crafting attachment adds `akashictome:defined_mod` to each stored item. `MorphingHandler.getStackFromMod` currently tests that component before lookup, so **both data components matter**. Merely giving an Akashic Tome or stacking plain nested item IDs is insufficient.

The once-per-player `tmm:starter_kit` tick advancement is unchanged; the reward function now gives one prepopulated Tome, two ordinary cooked beef and the existing silent Improvable Skills book unlock. Four source-confirmed entries are staged:

| Book | Item ID | Evidence |
| --- | --- | --- |
| Worn Notebook | `ars_nouveau:worn_notebook` | [Ars Nouveau item/recipe](https://github.com/baileyholl/Ars-Nouveau/blob/main/src/generated/resources/data/ars_nouveau/recipe/worn_notebook.json) |
| Lexica Botania | `botania:lexicon` | [Botania Revived recipe](https://github.com/Velron-Devs/Botania-Revived/blob/Neo/1.21.1/src/generated/resources/data/botania/recipe/lexicon.json) |
| Dictionary of Spirits | `occultism:dictionary_of_spirits` | [Occultism book recipe](https://github.com/klikli-dev/occultism/blob/version/26.1.2/src/generated/resources/data/occultism/recipe/crafting/dictionary_of_spirits.json) |
| Origins of Darkness | `evilcraft:origins_of_darkness` | [EvilCraft item resource](https://github.com/CyclopsMC/EvilCraft/blob/master-26-lts/src/main/resources/assets/evilcraft/items/origins_of_darkness.json) |

## Native duplicate guidebook prevention

[Ars Nouveau's player-login event](https://github.com/baileyholl/Ars-Nouveau/blob/main/src/main/java/com/hollingsworth/arsnouveau/common/event/EventHandler.java) grants `WORN_NOTEBOOK` when `Config.SPAWN_BOOK` is true, independently of the TMM advancement. [Ars server config](https://github.com/baileyholl/Ars-Nouveau/blob/main/src/main/java/com/hollingsworth/arsnouveau/setup/config/Config.java) defines `[general] spawnBook = true` by default. The pack now ships `defaultconfigs/ars_nouveau-server.toml` with `spawnBook = false`; this only affects **new worlds**. Existing `world/serverconfig/ars_nouveau-server.toml` remains player-owned; no automatic retrofit or inventory deletion. Other mods' native first-join grants are still **unverified**, so no exhaustive no-loose-books claim is made.

## Before marking issue #306 complete

1. On the exact compiled/pinned client, validate data-component parsing by giving the starter function, open the Tome GUI and select/return each guidebook. Check crashes, missing registry IDs and whether transformed items retain their component.
2. Inventory all **installed** guidebook IDs from the actual JARs (Theurgy, Primal Magick, Hexerei, Goety, Draconic Evolution, Astral Sorcery, etc.). Include more only with confirmed IDs/eligibility and proof they don't bypass legitimate progression gates. Source list above is an initial core, not certification of every desired guide.
3. Inspect other mod-native new-player grants and disable only verified duplicate guidebook injections through native config, preserving later player-earned books.
4. New disposable world/new player: exactly one Tome, four core books inside, two ordinary steaks, no duplicate loose books; inspect actual inventory. Relog, die/recover, return Tome morph, first-log pouch quest; confirm neither grant repeats. Existing worlds/players and their inventories must remain untouched. Review serverconfig on existing saves only with explicit migration approval.
5. Run `python3 scripts/validate.py` (calls `validate_starter_tome.py`) to verify the starter contract, component JSON shape, Ars config and all Packwiz hashes. This is **Repository CI**, not a game boot or gameplay PASS.

**Integration dependency:** this PR modifies `index.toml` and `pack.toml` on `test/full-integration`; integration writer must reconcile those hashes with any concurrent pending PR (notably #394) instead of replacing another change wholesale.
