# Too Much Magic — quest design contract

Approved 2026-11-14. Inspired by ATM10's navigational and branching approach. Quest text, reward choices, IDs and layouts are original; no copied ATM10 quests, images, loot tables or proprietary assets.

1. **Every quest must grant positive experience and a meaningful reward.** Strongly prefer a **thematic, fixed item**, not currency; coins may substitute **only when no good fixed item makes sense**, principally selling-contract quests. The 42 provisional overview checkmarks still use temporary 2 XP + one paper rewards, to be redesigned as those chapters mature. No random-only or no-reward quests.
2. **Item rewards must not auto-complete another quest.** The initial stricter rule forbids ANY fixed reward item ID from matching ANY authored item-task ID; validator checks this. Exception for introductory quests or awarding more of the SAME resource is permissible only if individually documented and tested. Shop purchases are deliberate paid choices, not automatic quest rewards; they may fulfill eligible ordinary resource tasks but must never bypass boss/technology gates.
3. **Completion criteria should be unique across quests unless intentionally reused.** Repeated items are allowed only in the expressly documented ascending-volume copper, emerald and diamond sale tiers (six specific item IDs, each reused twice), using consuming tasks and different submission counts. No other duplicates or advancements. Informational checkmarks are manual and use unique task IDs.
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

## B11 changes — Quest Shop and Cooking for Blockheads

- **Quest Shop** introduced to serve as an anti-grind and anti-bad-luck market, not an alternate boss progression route. Vanilla amethyst and Heart of the Sea plus six known Artifacts are initial candidates; per-team currency balance is upstream behavior and needs multiplayer acceptance checks.
- **Cooking for Blockheads** is folded into Food & Fishing, with unique Cooking Table and Oven tasks, rather than creating a new quest chapter. Existing Balm covers its library dependency.
- **Currency is not the default reward.** Only the 12 dedicated sale quests currently grant `questshop:coins` instead of a fixed item. All other authored quests continue to use useful physical items plus XP.
- **Copper, emerald, diamond sales** consume inventory and pay one team coin. First three per resource are one-time, fourth deliberately inefficient and repeatable (256 copper blocks; 128 emerald blocks; 64 diamond blocks). These are **design-approved shared item criteria**, not accidental overlap.
- Exact rules and shop prices are in [QUEST-SHOP-ECONOMY.md](QUEST-SHOP-ECONOMY.md).
