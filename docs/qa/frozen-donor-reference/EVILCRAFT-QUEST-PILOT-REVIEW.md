# EvilCraft pilot quest line — source-only

Based on pinned EvilCraft `1.21.1-neoforge-1.2.100`. Contains **9 authored draft milestones**, not live quests yet. Exact upstream source snapshot: [CyclopsMC/EvilCraft at 94eeb8e9](https://github.com/CyclopsMC/EvilCraft/tree/94eeb8e972eaa5ab3fb220c94ef9c745e081510d).

It opens directly with Dark Gems, using EvilCraft's real `evilcraft:first_age` advancement. The first reward is **2 Crushed Dark Gems**, ID `evilcraft:dark_gem_crushed`, a useful early processing derivative that also has a high fuel value. The separate book acquisition uses `evilcraft:evil_source` advancement. Other quests distinguish **item possession** from crafting or use rather than pretending to detect operation of the Blood Infuser/Tank.

Branching: Dark Gem → Darkened Apple → Origins book; independent Dark Gem → Blood Extractor → Infuser and Tank; Dark Gem → Burning GemStone; Infuser → Dark Power Gem / Vengeance Pickaxe. This is intentionally not one unbroken linear item-checklist. Real infusion use, spirit furnace, broom and advanced modules can be added after verifying their exact mechanisms and task events.

Rewards reflect the approved humor/usefulness rule. Examples: dark gem → crushed gem; Blood Extractor → bucket; unsafe Vengeance Pickaxe → milk bucket (**not** a cure claim). No paper, cookies, empty bowls, compasses or XP bottles.

**Open QA:** verify the pinned NeoForge JAR registry, particularly source branch compatibility, Dark Gem first-age advancement, manual acquisition mechanics, visible reward icons, and any cross-mod recipe/currency gating before compiler import. The source draft has no allocated FTB task/reward IDs and must NOT be used directly as finished SNBT. Claim state from existing 42 scaffolds must be preserved/migrated intentionally.

Next steps for the Work author: review the draft against the pinned game, expand real mechanics (blood gathering and a first useful infusion), import through the declarative quest compiler once ready, test client and multiplayer and replace the EvilCraft placeholder without adding a second paper payout.
