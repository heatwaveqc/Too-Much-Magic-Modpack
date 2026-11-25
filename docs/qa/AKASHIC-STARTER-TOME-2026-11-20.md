## Current native grant suppression — 2026-11-24

Owner requested empty initial physical inventory and a populated Introduction quest Tome; actual Nov 22 T09 list [#504](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/504#issuecomment-7084162893) at c56fb18ffc3b9f12f27317e7fb37f15c32576a8c: empty Akashic Tome, two cooked beef (frozen function), loose Astral Tome, Worn Notebook, Feywild Lexicon, Prefab house. #567 already retired auto kit, leaving only silent non-item IS3 enrollment; the personal Tips & Tricks claim uses exact native populated stack with 12 entries after adding Feywild. No quest rewrite, no blanket inventory deletion.

- Feywild exact owner JAR SHA-1 e59b68dd85b92ebbdbde478ea4e52fdb8b84c9d0 matches Packwiz. javap FeywildConfig: COMMON_BUILDER.push("feywild"), define("spawn_with_lexicon", true); Feywild registers COMMON_SPEC with default filename, ModEventListener.onPlayerLogin returns before checking saved receipt or adding item when false. New config/feywild-common.toml sets only this key. Native recipe feywild:feywild_lexicon uses fey dust + book; login creates default stack; FeywildLexicon.use spawns sprite and opens native menu, so no generic Patchouli book identity data is required. Native selection/use/return and sprite interaction must be tested in game; no direct proof of morph-return.
- Astral exact prior source dossier and resource comparison: e5309e9cc82dfe96d0fc7b04aaa5c56692d47f55, GeneralConfig.java defines general.giveTomeOnJoin true, PlayerEventHandler.onLogin only adds the Tome inside that gate; direct source re-read. Prior exact JAR identity/resource proof in docs/mod-comprehension/mods/astral-sorcery.md; current local exact JAR unavailable (DNS), compiled parity not rechecked. New defaultconfigs/astralsorcery-server.toml sets only that key; preserves all other defaults and crafting.
- Ars existing new-world spawnBook=false. Prefab #418 merged to dev/progression at 50e28498549fbbc4320e2ed81156ec0eb356359b, native grant suppression must be promoted to pack before empty-start acceptance. Do not duplicate the Progression writer or disable earned housing optional shaft/chests.
- Existing world/serverconfig may override new-world defaultconfigs; test new player in fresh candidate, also audit actual deployed server config without rewriting valuable saves. All known observed sources addressed in source or explicit promotion dependency, not all current mods' first-login sources proven; broader roster audit remains #509. Exact 12-book claim, config keys and Packwiz paths are enforced by updated existing test_tips_starter_a.py; designated integration writer relocks all four runtime files, updates provenance and reruns quick CI. B/C empty inventory before any quest claim, no delayed or relog gifts, one personal Tome reward with functioning morph and return for all guides, legacy claim guard, no extra items in Curios/armor/offhand. No full build authorized.

---

# Akashic starter guidebook census — #306

## Current disposition — 2026-11-24

Historical donor #396 frozen at `7eab326c85659c1c7a6953055a74057056bce414`; delivery superseded by merged #567 and config successor #586. All eleven exact component payloads and the Ars new-world `spawnBook=false` are retained in #586. Preserve the source-only census verbatim. The donor validator's manifest/identity/uniqueness checks are ported into existing `tests/test_tips_starter_a.py` without restoring first-login item delivery or replacing `scripts/validate.py`. The old advancement is impossible and remains a legacy ownership marker, the claim marker prevents double grants, Tips & Tricks personal reward supplies the Tome, two ordinary steaks and white sleeping bag, legacy recipients get bag only; silent IS3 book access remains independent first-login behavior. The old donor's delivery, global validator edits and Packwiz hashes must not be replayed. Both runtime files require latest index/pack hashes and starter integration-source provenance reconciliation by the designated integration writer. No final all-roster census or native parsing/morphing QA claimed. Pending candidates in the manifest remain #306 obligations. Current delivery contract takes precedence over all historical language below.

---

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
