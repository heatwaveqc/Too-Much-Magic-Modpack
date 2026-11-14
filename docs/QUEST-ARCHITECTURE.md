# Quest book organization — five top-level categories

**Owner-approved architecture, 2026-11-13/14.** Planned only. Source roster is `manifest/mods.json` on `build/b8-qol-compass-farming-market`. The exact chapter proposal and exhaustive 148-entry status inventory are in [QUEST-CHAPTER-INVENTORY.md](QUEST-CHAPTER-INVENTORY.md).

## Categories

1. **Introduction** — `Getting Started`, `Tips & Tricks`, `Food & Fishing`. Consolidates intro, player reference, cooking/farming, seed market and fishing.
2. **Progression** — `Basic Progression`, `Advanced Progression`, `Endgame Roadmap`; additional `Skills & Advancement` and `Exceptional Power: Reactors` owner-approved because they are cross-mod mechanics, not schools of magic. Endgame Roadmap is an overview, **not** duplicated Endgame rewards.
3. **Magic** — chapter for every substantial independent *primary* magic system. Integrate addon content into the parent chapter wherever possible; 25 candidates from B8, including magical industry and Reliquary.
4. **Exploration & Combat** — each substantial dimension, boss, encounter, special equipment or supernatural faction system; 9 candidates from B8.
5. **Endgame** — dedicated late-game mod systems and ultimate milestones; 3 approved chapters: Draconic Evolution, ProjectE + Expansion, Replication. Re:Avaritia and Extended Crafting are excluded because they displace the intended Draconic Evolution/Gem Armor peak. Real locks belong to game mechanics, not quest completion alone.

**Counts:** Introduction 3 + Progression 5 + Magic 25 + Exploration & Combat 9 + Endgame 3 = **45 proposed chapters**. These are design candidates, not all locked individually. No optional sixth category has been approved.

## General chapter design contract

- Accessible, browsable guidebook; only explicitly locked game mechanics should impose true progression gates.
- Major primary mods get their own chapters. Addons generally appear within the parent chapter, with clear crosslinks for cross-mod integrations. Avoid separate chapters for dependency libraries, rendering or generic QoL tools.
- Individual player starter grants must not be accidentally team-shared. First-log Trinkets Pouch is the agreed starter reward and is authored as a prototype in PR #14.
- Food & Fishing covers all farming, cooking, market seeds, Aquaculture and harvesting enhancements in one chapter, not seven.
- Tips & Tricks is a reference section with minimal prerequisites, including item sorter, JEI/Jade, book consolidation, Curios, deaths, maps and loot.
- Progression's **Endgame Roadmap** tells the story of gates and routes; Endgame contains the definitive detailed questlines and relevant rewards.
- Cross-mod dependency quests should be centralized in Progression, Advanced or Endgame and linked back to specialist chapters.
- Don't reward players with disallowed/restricted items, enable ProjectE/Expansion before Chaos Guardian, or bypass late-game miner/Cataclysm prerequisites.
- No bags inside other bags; this universal policy has a separate implementation blocker (#16).
- Only the pinned NeoForge 1.21.1 roster is in scope; 8 inactive roster entries do not receive standalone quests.
- No actual FTB chapter/quest expansion is made by this document. Verify all authored FTB quests and rewards on a disposable client/server world.

## Recommended design order

Finalize the five-category navigation and overall quests skeleton; design Introduction and its Food & Fishing chapter first; write Basic/Advanced/Endgame Roadmap crosslinks; then flesh out Magic and Exploration & Combat in mod clusters. Defer detailed Endgame rewards and bosses until gates are integrated and validated.

## Owner lock: excluded ultimate-crafting mods (2026-11-14)

- **Do not install Re:Avaritia or Extended Crafting** or reserve Endgame chapters for them. The owner previously placed both on hold; they were mistakenly pinned in B5/B8 and carried forward into an incorrect 46-chapter inventory.
- Intended top-tier play is Draconic Evolution, Chaos Guardian, ProjectE and Project Expansion's Gem Armor, plus difficult resource acquisition. Their signature equipment should not be made obsolete by Infinity-tier gear or a competing standalone ultimate-crafting stack.
- **Skills & Advancement** and **Exceptional Power: Reactors** are explicitly approved as additional Progression chapters.
