# All starter guidebooks in Akashic Tome — issue #306

**Owner scope correction, November 20, 2026:** The earlier four-book pilot is superseded. **Target: all introductory/reference guidebooks available in the installed modpack, including Occultism and Roots Classic's Runic Tablet.** Do not give functional spellcasting spellbooks, progression unlocks or research artifacts just because their names include *book*.

## Source-stage roster

Eleven source-identified introductory manuals are currently included in `manifest/starter_guidebooks.json` and one populated starter Tome command. This is **not a final installed-JAR census**; further eligible manuals must still be added after verification.

| Guide | Registry item | Required special data |
| --- | --- | --- |
| Worn Notebook (ars_nouveau) | `ars_nouveau:worn_notebook` | Akashic selector only |
| Lexica Botania (botania) | `botania:lexicon` | Akashic selector only |
| Dictionary of Spirits (occultism) | `occultism:dictionary_of_spirits` | Akashic selector only |
| Origins of Darkness (evilcraft) | `evilcraft:origins_of_darkness` | Akashic selector only |
| Runic Tablet (rootsclassic) | `rootsclassic:runic_tablet` | Akashic selector only |
| Book of Shadows (hexerei) | `hexerei:book_of_shadows` | Akashic selector only |
| The Hermetica (theurgy) | `modonomicon:modonomicon` | `modonomicon:book_id=theurgy:the_hermetica` |
| The Black Book (goety) | `patchouli:guide_book` | `patchouli:book=goety:black_book` |
| Ars Ecclesia (eidolon_repraised) | `eidolon_repraised:codex` | Akashic selector only |
| Scriptura Vitae (neovitae) | `neovitae:guide_book` | Akashic selector only |
| Astral Tome (astralsorcery) | `astralsorcery:tome` | Akashic selector only |

**Runic Tablet:** The 1.21.1 Roots Classic registry and recipe identify `rootsclassic:runic_tablet`. Native Akashic `AttachementRecipe.isTarget` accepts names containing `tablet`. Its native GUI and morph-return still need actual client QA. [Roots source](https://github.com/Lothrazar/RootsClassic/blob/1.21.1/src/main/java/elucent/rootsclassic/registry/RootsRegistry.java).

**Theurgy:** On the 1.21.1 source branch the Hermetica recipe outputs `modonomicon:modonomicon` with `modonomicon:book_id=theurgy:the_hermetica`, *not* a standalone `theurgy:the_hermetica` item. [Recipe source](https://github.com/klikli-dev/theurgy/blob/version/1.21.1/src/generated/resources/data/theurgy/recipe/crafting/shapeless/the_hermetica.json). Goety's Black Book likewise needs the `patchouli:book=goety:black_book` component on `patchouli:guide_book`; do not omit specialized book identity. The Neo Vitae guide is its own `neovitae:guide_book` according to 1.21.1 source.

## Implementation contract

One `tmm:starter_kit` persistent first-tick advancement grants exactly one `akashictome:tome` containing the serialized `akashictome:tool_content` ItemStack list. Every contained book has a distinct `akashictome:defined_mod` selector plus any native book-ID components. The other two commands grant **two ordinary cooked beef** and the unchanged silent Improvable Skills book unlock. The Trinkets Pouch remains an independent first-log reward. No player inventory deletion/migration.

Source-only `scripts/validate_starter_tome.py` loads `manifest/starter_guidebooks.json`, verifies native component serialization, key required books, no duplicates, original advancement and rewards. Invoked by `scripts/validate.py`. Guidebook manifest is repository source metadata, **not a Packwiz runtime file**; only the changed starter reward function's digest belongs in `index.toml`.

Native Ars Nouveau first-login Worn Notebook gift is disabled for *new worlds* via `[general] spawnBook=false` in `defaultconfigs/ars_nouveau-server.toml`. Other native grants including the current pinned Goety release remain to be audited; Goety's older implementation has starter Black Book and Witch's Brew options defaulting false. Do not rewrite existing `world/serverconfig` or remove legitimately earned books.

## Coverage audit and next QA

Unresolved candidates are listed individually in `manifest/starter_guidebooks.json` under `pending_review` (Goety Witch's Brew, Draconic info tablet, Primal Magick's placed Grimoire, Forbidden & Arcanus, Mahou, Hexcasting, Iron's, and other installed schools). **The issue stays open until an exact installed-JAR registry survey establishes every eligible portable starter manual is covered or explicitly justified as excluded.**

On an *owner-authorized* fresh disposable Prism world and fresh player, verify every guide opens and returns to the Tome: especially Roots Runic Tablet, Theurgy Modonomicon Hermetica, Goety Patchouli Black Book, Neo Vitae and Astral tome. Preserve player-specific research restrictions. Verify exactly one populated Tome, two steaks, no loose native book gifts, relog/death/no duplication, unaffected earlier players and independent pouch claim. Record actual runtime evidence; static CI does not establish gameplay compatibility.

**Integration:** This PR and #394 both modify `index.toml`/`pack.toml`; the integration writer must rebase/recompute from the latest unified candidate. No full boot or Prism packaging was authorized by this source change.
