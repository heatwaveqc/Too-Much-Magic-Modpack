# Too Much Magic — quest design contract

Approved 2026-11-14. Inspired by ATM10's navigational and branching approach. Quest text, reward choices, IDs and layouts are original; no copied ATM10 quests, images, loot tables or proprietary assets.

1. **Every quest must grant positive experience AND a concrete item.** No random-only or no-reward quest. An optional overview/checkmark still counts as a quest, so all 42 unfinished chapters now give 2 XP and one paper for their provisional overview.
2. **Rewards must not auto-complete another quest.** The initial stricter rule forbids ANY item-reward ID from matching ANY authored item-task ID, including when the rewarded stack would be too small. The validator enforces this across the 46 authored quests. An exception for introductory quests or awarding more of the SAME requested resource is permissible only with a specific rationale and an explicit future allowlist.
3. **Completion criteria should be unique across quests.** No same item task ID or advancement ID in two authored quests; a deliberate shared goal needs explicit documentation and test coverage. Every informational checkmark is intentionally manual, with a distinct unique task ID, not a shared external completion predicate.
4. Branch freely across magic schools, exploration and food. Within a chapter, give sensible optional dependency lines without enforcing unrelated specialties or duplicating rewards.
5. ProjectE and Project Expansion remain post-Chaos Guardian, and no quest may give the excluded Ring of the Seven Deadly Sins, Re:Avaritia or Extended Crafting items. Quests explain real gates; they do not implement those gates.

## First authored batch

**Getting Started: 11 quests** covering the welcome page, first-log pouch, basic crafting, tools, stone, furnace, chest, iron and first book. The first-log FTB quest gives **10 XP and four torches** when claimed; the **Trinkets Pouch is separate, automatically delivered once per player by the persistent advancement**. No second pouch is claimable.

**Tips & Tricks: 15 optional, manual read-and-check quests** covering JEI, Jade, inventory sorting, crafting tweaks, controls, guidebooks, Curios/bag nesting, chunk claims, Lootr, Tombstone, biomes, Polymorph, skills and overlays. Each provides 5 XP and a small item.

**Food & Fishing: 20 quests** covering crop basics, fishing (including Aquaculture rods), Farmer's Delight prep and cooking, the Farming for Blockheads Market, and optional Pam's HarvestCraft cooking and starfruit. No food or fishing task reuses a criterion from another authored quest.

**The other 42 chapters** retain a single optional read/checkmark overview each, with 2 XP and one sheet of paper rather than the previous no-reward placeholders. Remove or replace these as actual content is written.

Sources for modded item ID selection: ATM10 1.21.1 quests contain Aquaculture rods, Farming for Blockheads Market, Pam's Fried Chicken and Starfruit IDs; Farmer's Delight references use `farmersdelight:cutting_board` and `farmersdelight:cooking_pot`. The installed pack's client registry/JEI must still be verified.

## Source and tests

- Authoring plan: `manifest/quest_content.json`; stable IDs: `manifest/quest_content_resolved.json`; chapter membership: `manifest/quest_chapters.json`.
- `scripts/validate_quests.py` must check this contract in addition to structure/ID consistency.
- Confirm every task, XP reward, item reward, dependency and tooltip in a disposable Prism world. Check each reward once, including per-player first-log grant and multiplayer team independence. Server startup is only a smoke, not an item/quest UI test.
