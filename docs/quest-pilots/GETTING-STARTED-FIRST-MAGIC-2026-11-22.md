# Getting Started — first-magic walkthrough and layout pass (2026-11-22)

**Authoritative work slice:** source-only quest content and spatial layout atop [Tips & Tricks starter hub #550](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/550). The reviewed five-category visual grammar is [#498](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/498). **No Minecraft/Prism build or gameplay QA authorized.**

## Owner intent

This is TMM's first impression: magic first, player choice, no fake prerequisite forcing every vanilla tool tier or armor style before a player can try a spell. Begin in Newbie, but do not graduate or enroll a specialist Path for someone else. Starter Tome, two ordinary steaks and sleeping bag come from the *separate* Tips & Tricks claim designed in #550; no extra starter grants are inserted here.

**20 quests preserved** with every quest, task, XP/item/command reward ID intact and **no changes to physical reward contents, costs or progression mechanics**. One existing FTB quest title becomes **Pick Your First Magic** (same ID `544D501000D00000`); its direct predecessor is now **Make a Crafting Table**, not **Iron Pickaxe**. This deliberately removes a false path into magic without changing any survival item requirements in-game. Other references to the old title (magic shortcut and exploration) are updated. Eight survival/research objective flags become optional; their original item criteria, XP/rewards and internal vanilla prerequisites remain intact.

### Visible reading order

```text
Welcome   Punch a Tree → Crafting Table → Pick Your First Magic
                                   │              │
                    Optional mining & storage     ├─ Magical Shortcut
                    Optional book & bookshelf     │           ↓
                                                  │      First Spell / Ritual
                                                  └─ Explore      ↓
                                                            Rest of TMM

 Controls (independent, above-left)       Newbie → Normie (independent, personal)
```

The scene is spatial, not a gameplay hard lock on any magic recipe. Roots `Welcome`, `Controls`, `Newbie graduation`, and `Punch a Tree` remain **independent** to avoid retroactive auto-task problems and protect personal graduation. The first real item task is the log tag; crafting table follows. The center-right mainline follows a concise magic introduction to the final orientation, while the optional classic-survival rail runs separately beneath it. An optional library/research rail and exploration link appear above.

A player who never makes a wooden, stone, or iron pickaxe can still progress through the orientation's **Crafting Table → Magic Choice → Shortcut → First Spell/Ritual → Finish**. This does not mean all chosen magic schools are early-game accessible at all tiers; their dedicated chapters retain actual mechanics and recipes.

## Art/geometry policy

- Left-to-right core, with the controls/graduation as distinct option cards, library/exploration above, survival below; no invented dependencies just to make artwork look connected.
- Large gear at Welcome, hexagon for the first crafting/magic choice and first spell, octagon for the final orientation and personal graduation. Native Minecraft/TMM item icons replace some generic icons.
- Three optional **vanilla Minecraft item** image placements serve as spatial section cues. They are not ATM10 art, and do not bake pixel text into the chapter.
- Type-A contract in `tests/test_getting_started_layout_a.py` asserts **zero proper straight-edge crossings**, **minimum 1.6 coordinate units from an unrelated node**, no node/image or connector/image strikes, all source/SNBT positions and native IDs/rewards unchanged. The no-crossing score is a source-coordinate fact only; the in-game renderer and player zoom must still be tested.

## Stable-source and ownership strategy

**Stacked branch**, base `feature/quest-tips-starter-hub-20261122` at `661125e0d5ac354cb807ff1802e588f7db556715`, **not** directly on `test/full-integration`, because #550 edits the same canonical quests, localization, UI generator and Packwiz index. The Getting Started PR can merge into #550 first, then the owner/integration writer reviews promotion to the live integration ref; or rebase the Getting Started slice carefully onto integration after #550 is merged. Avoid cherry-picking `index.toml`, `pack.toml`, `manifest/quest_content.json` or `en_us.snbt` wholesale over a concurrently updated branch.

- The old Welcome rewards are **Unwitnessed Amulet and Enigmatic Legacy cursed ring**, not the forbidden Ring of Seven Deadly Sins. The quest description warns the cursed ring is optional to equip; this PR does not confiscate or increase rewards.
- The graduation command stays `tmmnewbie admin graduate {p}` with permission level 2 and `team_reward: false`, preserving personal team semantics. The player text describes Newbie/Normie without asserting a specific unfinished Progression runtime configuration.
- No starter-kit function/advancement, Prefab house, Curios reward, Q Shop currency, food rules or Progression executable code is modified.
- Theorem: the only altered effective quest prerequisite path is that **first magic no longer depends on the iron-tool branch**. All original task/reward IDs and ordinary survival branch order remain fixed; older saved completion/claims are unaffected in source but must be verified in the actual FTB client.

## Ready work, blockers and QA

**Code-ready / source-ready**
- [x] Preserve 20 existing quest/task/reward IDs and exact rewards; improve all 20 explanations.
- [x] Change only the relevant educational dependencies; make eight vanilla/research lessons optional.
- [x] Source, live FTB SNBT, English localization, UI rebuild positions and derived position ledger updated.
- [x] Add graph/art/ID/reward geometry regression tests and Type-A-only PR CI.
- [ ] Obtain CI evidence at final PR head, including Packwiz hash chain, canonical quest validator and inherited Tips starter tests.

**Owner design blockers:** None for the approved scope; optional future reconsideration whether to relocate the curse-bearing Welcome ring to a branch is **not** implemented because changing a claim reward has save/reward implications.

**Type B/C pending** (requires owner-approved test build; **not run**):
- [ ] C: normal and reduced zoom, mainline obvious and branches not cramped; individual native shapes/icons and original vanilla image resources render.
- [ ] C: fresh player can claim Tips starter kit, collect log → crafting table → choose first magic without wooden/stone/iron pickaxe; verify FTB item-tag task and XP/item claims.
- [ ] C: complete any optional survival research fork without it appearing as the mandatory prerequisite for first magic; finishing book does not enroll or graduate a Path.
- [ ] B/C: per-player Newbie graduation remains available without crafting iron or completing all branch quests; one player's claim does not alter teammates.
- [ ] C: existing test-world quest completion and old reward claim records stay intact after the layout and one-title/one-prerequisite migration, including any collapsed/old client view.
- [ ] C: client-facing Welcome cursed ring tooltip and warnings are comprehensible; there is no automatic equipment change.

These source tests and GitHub CI do **not** substitute for actual Minecraft acceptance.
