# Tips & Tricks — starter hub and informational chapter (2026-11-22)

**Scope:** Source candidate for TMM Minecraft 1.21.1 NeoForge. Current physical `config/ftbquests/quests/chapters/tips_tricks.snbt` stays in the existing Introduction group pending the separate [five-category migration PR #498](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/498). The chapter's **authoring archetype** is Informational, no compulsory mainline.

## Owner direction and inherited contracts

**Latest explicit owner direction (2026-11-22):** Tips & Tricks is the single **claim point** for steak, Akashic Tome and Comforts sleeping bag. This supersedes the physical-first-login Tome/steak distribution portion of #509 and the current instructions on draft #396 / issue #306, **but not the goal of a populated Tome**, the approved ordinary head start, or the separate native Prefab starter house. This branch changes the *distribution point*, not the contents/provenance of the guidebook.

- **Claim Your Starter Supplies:** one personal FTB manual-checkmark quest; the reward runs `execute as {p} run function tmm:rewards/starter_kit` with permission level 2 and `team_reward: false`. The function gives **one Akashic Tome, two ordinary cooked beef, one white Comforts sleeping bag**. It never grants Eternal Steak, a combat item, a specialist Path, a boss drop, EMC or free late-game travel.
- **Legacy and duplication safety:** change the existing `tmm:starter_kit` first-tick advancement trigger to `minecraft:impossible`, retaining the historic criterion name `first_tick` as an existing-world marker. A fresh, separate `tmm:starter_first_login` advancement grants **only** the Improvable Skills guide unlock silently. A new `tmm:starter_kit_claimed` impossible advancement records one-time supplies. Grant commands check both markers, so legacy auto-supply recipients get only a sleeping bag and ordinary new players receive exactly the new kit once. Existing test saves can still receive the Information quest, but not duplicate old Tome or steaks.
- **Prefab and Curios ownership:** native Prefab first-join house / permitted shaft stay untouched, as do the existing separate Intro Curios reward and other starter item/boss restrictions. The welcome card now points to this claim.
- **Populated Tome:** [draft #396](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/396) separately authors the 11-guide component-aware Tome and will need its packwiz/index and `starter_kit.mcfunction` changes **reconciled** into this new quest claim path. On this branch the Tome is **still plain**, as it is on the current base. Do not claim the 11-guide integration or its C tests passed.

**Important FTB test boundary:** personal `team_reward:false` command claim and functions' advancement selectors need verified multiplayer/new-player client QA; this PR is **source-only**. The legacy-marker strategy is designed to prevent replay on existing saves, not proof of in-game persistence.

## 34-quest five-zone arrangement

Retain all 20 existing quest/task/reward IDs and all their original rewards, with refreshed descriptions and objective-specific icons; add **14 new personal information quests** with 5 XP only, except the one starter claim (10 XP plus starter function). No new information-only item trinkets clutter inventory.

| Cluster | Guide coverage |
| --- | --- |
| Starter center | Existing Tips index and one-time Claim Your Starter Supplies |
| Books & Information | Akashic Tome, JEI, Jade, AppleSkin, JER/JEP, enchantment tooltips, Better Advancements |
| Controls & UI | Controlling, KeyBind Bundles, KeySets, Just Zoom/More Overlays, Extreme Sound Muffler, Default Options |
| Inventory & Crafting | ReFoxed sorting, Mouse Tweaks, Crafting Tweaks, Polymorph, Curios and TrashSlot |
| Comfort & Survival | Comforts hammocks/sleeping bags, FTB Chunks, Lootr, Tombstone/Smart Keep Inventory, Nature's Compass, Harvest with Ease, TreeChop/Leaves Be Gone, Prefab |
| Useful Magical Tricks | Scriptor smelting, Occultism/Mahou ore processing, EvilCraft/Hexerei brooms, Vengeance Pickaxe, TMM Progression and shop orientation |

The five topical zones **surround** the starter center; separate columns/rows with negative space. Existing static chapter has a sparse five-element arrangement of normal vanilla item textures, not unlicensed ATM art. All nodes are optional with **zero manufactured prerequisite arrows**. Guide content avoids inventing exact modded keybind names or claiming staged changes already work in installed builds.

**Shop handoff:** [PR #531](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/531) migrates old Quest Shop team balances and `/hqs shop` to Q Shop personal wallets and `/qshop open tmm`. The Info card describes both commands conditionally, without changing that PR's actual shop or FTB sales; integration writer will remove the legacy command branch after the new Q Shop is known installed.

## QA/test queue

### Source (Type A)
- [x] Updated 34 authored and resolved quest identities, all old persistent IDs retained; 273 total quest objects / 235 authored.
- [x] Physically staged all 34 FTB quest nodes, English entries, derived layout and Packwiz file hash chain including new datapack functions/advancements.
- [x] Added a targeted Type-A unittest and pull-request workflow to check personal kit grant, no duplicate tick inventory grants, shared IDs and mod pins.
- [ ] Verify all checks green at this PR's exact head: `scripts/validate_quests.py`, `scripts/validate.py`, and `tests/test_tips_starter_a.py` plus broader Verify bootstrap.
- [ ] Merge conflict reconcile with #396 and #531; do not copy old startup/quest index snapshots.

### Pending source/enforcement detail
- [ ] Source-verify the **effective 1.21.1 loaded Comforts sleeping bag registry** before client acceptance; the 9.0.5 released data/docs identify `comforts:sleeping_bag_white`, but this tool session did not byte-inspect the pinned JAR. Disable this reward rather than accept an invalid item ID if the installed registry differs.
- [ ] Merge/populate the starter Tome from #396 once its full pin and native book components are accepted.

### B/C — **not run**, separately authorized in-game testing
- [ ] **C fresh profile:** has normal Prefab offer/shafts (subject to native rules), no auto Tome or steaks; sees the Tips claim immediately; claims Tome + exactly two cooked beef and a white sleeping bag, no XP/item shortcuts; receives existing Curios reward through its original mechanism.
- [ ] **C book:** eligible physical guide entries actually present after #396, correct selection/return, no duplicate loose manuals, no premature knowledge unlock.
- [ ] **B/C multiplayer:** two independent players claim once each, including after changing FTB teams/relogging; commands run as correct player; second claim and manual function replay give nothing.
- [ ] **C existing saved test world:** original `tmm:starter_kit` completed first_tick marker still suppresses duplicate Tome and steaks; optional Tips claim supplies sleeping bag only.
- [ ] **C gameplay/readability:** five cluster regions, icons and vanilla decoration images render at normal and reduced zoom, no concealed lines/icons, any custom mod controls are described without wrong defaults.

Do not call the current source a tested release; **no new assembly, server smoke, Prism/client or world mutation is authorized or performed**.
