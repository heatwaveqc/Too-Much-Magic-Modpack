# FTB Quests five-category, 45-chapter scaffold

**Status:** authored source candidate, **not client-integrated or quest behavior verified**. Branch `feature/ftb-quests-45-chapter-scaffold` based on PR #17's corrected 138-mod pack (without Re:Avaritia/Extended Crafting).

## Navigation structure

| Group | Count | Chapters |
| --- | ---: | --- |
| Introduction | 3 | Getting Started; Tips & Tricks; Food & Fishing |
| Progression | 5 | Basic Progression; Advanced Progression; Endgame Roadmap; Skills & Advancement; Exceptional Power: Reactors |
| Magic | 25 | Ars Nouveau; Astral Sorcery; Botania Revived; Eidolon: Repraised; Enchanted: Witchcraft; EvilCraft; Forbidden & Arcanus; Goety; Hex Casting; Hexerei; Iron's Spells 'n Spellbooks; Mahou Tsukai; Malum; Neo Vitae; Occultism; Pastel; Primal Magick; Psi; Roots Classic; Scriptor Magicae; Theurgy; Totemic; Just Another Witchery Remake; Embers Re-Ignited; Reliquary Reincarnations |
| Exploration & Combat | 9 | L_Ender's Cataclysm; Deeper and Darker; Gateways to Eternity; Feywild; Apotheosis; Artifacts; Enigmatic Legacy+; Vampirism; Werewolves |
| Endgame | 3 | Draconic Evolution; ProjectE; Replication |

### Authored files

- `config/ftbquests/quests/data.snbt` configures the FTB Quests book with independent discovery and no default team reward sharing.
- `config/ftbquests/quests/chapter_groups.snbt` holds five stable group IDs in the owner-approved order.
- `config/ftbquests/quests/lang/en_us/chapter_group.snbt` and `chapter.snbt` label groups and chapters in English (ATM10-style).
- `config/ftbquests/quests/chapters/*.snbt` contains 45 chapters with stable unique 16-digit uppercase hex IDs, vanilla icons that exist in 1.21.1 (except the independently source-verified Ars pouch quest icon), clear descriptions, and one optional checkmark per chapter; the three Introduction-category chapters now have distinct actual questlines. This ensures the chapters have navigable content without introducing invented progression tasks or equipment.
- `manifest/quest_chapters.json` documents exact category membership, mod ownership, IDs, slugs, display icons, and future editing points.
- `scripts/validate_quests.py` checks scaffolding structure and ID uniqueness using only Python standard library.
- The agreed **Punch a Tree** quest from [PR #14](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/14) is carried into Getting Started with the same persistent advancement, task and quest IDs. KubeJS datapack function grants `ars_elemental:curio_bag` once per player upon obtaining a `#minecraft:logs` item; it is an **integration candidate**, not a confirmed runtime pass.
- Ars Elemental bag blacklist `#ars_elemental:blacklist_bag_item` receives `#tmm:portable_bags`, also ported from PR #14. **Only Ars bag destinations** are protected by this config. **Full cross-mod nesting prohibition is a release blocker, issue #16**.

### Intentional omissions

- No substantive item, boss, ritual, or crafting tasks for specialist chapters.
- Every chapter overview grants XP and paper. Punch a Tree has claimable XP plus torches while its per-player advancement independently issues one pouch.
- No forced cross-chapter dependencies or gating via FTB Quests.
- No ProjectE access before Chaos Guardian; this remains an independently enforced gameplay design requirement not yet implemented.
- No unsourced mod item registry IDs, new mods, recipes, KubeJS scripting or hidden changes to magic skills.
- No invented quests for Re:Avaritia or Extended Crafting (both excluded).

### Runtime acceptance checks

1. `python3 scripts/validate.py`, `python3 scripts/validate_quests.py`, packwiz metadata read and **fresh 138-mod server smoke** must pass.
2. In a **disposable Prism world**, open FTB Quests and confirm five categories in the specified order, 45 unique chapters visible with expected English labels/icons, zero empty chapters, no duplicate or ghost chapters.
3. Inspect optional overview quests: checkmark works, grants XP plus paper, doesn't force another chapter; after relog labels/progress persist.
4. Test first log acquisition: pouch appears exactly once for each player even when in the same FTB Team, after relog, death and repeat pickups; FTB Quest task reports completion; no duplicate FTB reward.
5. Check all Ars bag-to-bag denials, shifts/number-key insertion, automation where possible, and ensure issue #16 remains open for Occultism and ProjectE.
6. Ensure no pre-Guardian ProjectE items can appear from quests, and the excluded Re:Avaritia and Extended Crafting remain absent from item/quest registries.
7. Back up existing saves before installing the changed B8-derived pack; 2 mods removed and quest/player progression metadata added. Old-world compatibility is not established.

**Do not report the scaffold as client-verified until these tests are actually run.**

## Introduction first-content batch (2026-11-14)

- **11 Getting Started + 15 Tips & Tricks + 20 Food & Fishing = 46 authored quests**, and 42 other provisional overview quests; **88 FTB Quest objects** total in 45 chapters.
- All 88 quests have positive XP and a tangible item reward. First log also automatically gives exactly one pouch per player, without an FTB pouch reward.
- Task item/advancement criteria are unique across 46 authored quests. Reward item IDs cannot complete any currently authored item task.
- This batch uses original text and safe, small item rewards, inspired only by ATM10's broad quest organization. See [QUEST-DESIGN-RULES.md](QUEST-DESIGN-RULES.md).

## B11 shop and kitchen content pass (2026-11-14)

- Same five categories and 45 chapters. New Quest Shop tip in Tips & Tricks; Cooking Table and Oven quests in Food & Fishing; 12 item-consuming sale quests in Basic Progression.
- 61 authored quests + 42 other provisional chapter overviews = **103 quest entries**. Sale coins replace thematic items because these quests represent transactions; all quests grant positive XP.
- Current economic catalog offers five categories and 29 buy entries. No generic sell-anything mechanism; ProjectE-based material printing is expressly permitted once ProjectE is unlocked.
- Both newly approved mods were subsequently **pinned to exact provider versions, SHA-verified, and combined dedicated-server-smoked** in [B11 Actions run 43610383608](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43610383608). **Client/GUI, kitchen and team-economy integration is still pending.**
