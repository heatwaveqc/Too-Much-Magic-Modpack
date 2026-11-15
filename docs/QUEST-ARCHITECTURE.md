# Quest book organization — five top-level categories

**Owner-approved architecture, 2026-11-13/14.** Planned only. Source roster is `manifest/mods.json` on `build/b8-qol-compass-farming-market`. The exact chapter proposal and exhaustive 148-entry status inventory are in [QUEST-CHAPTER-INVENTORY.md](QUEST-CHAPTER-INVENTORY.md).

## Categories

1. **Introduction** — `Getting Started`, `Tips & Tricks`, `Food & Fishing`. Consolidates intro, player reference, cooking/farming, seed market and fishing.
2. **Progression** — `Basic Progression`, `Advanced Progression`, `Endgame Roadmap`; additional `Skills & Advancement` and `A Sufficiently Advanced Magic` owner-approved because they are cross-mod mechanics, not schools of magic. Endgame Roadmap is an overview, **not** duplicated Endgame rewards.
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
- The full navigation scaffold is authored separately; this architecture document does not authorize new gameplay rewards or gates. Verify all authored FTB quests and rewards on a disposable client/server world.

## Mod chapter completeness: walkthroughs, not just milestones

The [binding walkthrough standard](QUEST-WALKTHROUGH-STANDARD.md) requires each substantive mod's chapter to teach the **full player-accessible progression**, from first guidebook access and initial materials through required tiers, crafting/multiblock/ritual setup, operation, automation, optional subbranches, addons and final milestones. A novice should need no external wiki: **quests + JEI + the in-game guide** must suffice. Quest text gives short actionable instructions and directs the player to the **specific guidebook section or JEI category** for complex builds; a generic link to the manual is not enough.

Each mod with an in-game book/UI manual must have an early quest explaining how to get or open it, or reward the guide if safe. Full coverage is established in the comprehension profile's **progression-to-quest matrix**, not inferred from quest counts. Mod addons normally remain in the parent chapter with their own clear instructional branches and independent research coverage. For branching or irreconcilable faction choices, explain alternatives and mark quests optional instead of enforcing impossible completion.

A working data validator and a passing server boot establish neither complete progression teaching nor client usability. Review earlier pilots, including the 49-quest Ars chapter on [PR #74](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/74), for specific guidebook onboarding, tier, setup/how-to and final-capstone coverage gaps. Keep all five groups and the existing reward/gate policies.

## Recommended design order

Finalize the five-category navigation and overall quests skeleton; design Introduction and its Food & Fishing chapter first; write Basic/Advanced/Endgame Roadmap crosslinks; then flesh out Magic and Exploration & Combat in mod clusters. Defer detailed Endgame rewards and bosses until gates are integrated and validated.

## Owner lock: excluded ultimate-crafting mods (2026-11-14)

- **Do not install Re:Avaritia or Extended Crafting** or reserve Endgame chapters for them. The owner previously placed both on hold; they were mistakenly pinned in B5/B8 and carried forward into an incorrect 46-chapter inventory.
- Intended top-tier play is Draconic Evolution, Chaos Guardian, ProjectE and Project Expansion's Gem Armor, plus difficult resource acquisition. Their signature equipment should not be made obsolete by Infinity-tier gear or a competing standalone ultimate-crafting stack.
- **Skills & Advancement** and **A Sufficiently Advanced Magic** are explicitly approved as additional Progression chapters.

## Authored structure (feature candidate, 2026-11-14)

The five FTB chapter groups and 45 chapter files, with no-reward optional overview entries, now exist on `feature/ftb-quests-45-chapter-scaffold`. This is navigation scaffolding, **not** the complete quest content or client-certified feature. The per-player first-log Trinkets Pouch prototype from PR #14 is integrated here without an additional FTB item reward. See [QUEST-SCAFFOLD.md](QUEST-SCAFFOLD.md) and the generated `manifest/quest_chapters.json` for exact IDs. Additional gating/content requires separately approved and tested steps.

## Progression implementation (2026-11-14, candidate)

**Historical 2026-11-14 snapshot:** the five Progression chapters contained 65 tasks, including BiggerReactors Rework. **2026-11-15 decision:** retire 19 BiggerReactors tasks; keep the Draconic generator, Energy Core, crystals and high-risk Reactor track in *A Sufficiently Advanced Magic*, crosslinking Botania's Mana Fluxfield. The five groups and 45 chapter IDs stay unchanged. Alex's Caves Continued and CodxLib are planned but not yet installed. See [POWER-TECH-QUEST-GUIDE.md](POWER-TECH-QUEST-GUIDE.md). Client/gameplay validation remains outstanding.
