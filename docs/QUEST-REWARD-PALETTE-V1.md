# Too Much Magic — Reward Palette v1.0: useful loot, weird food, and jokes
**Status:** owner-directed standard, 2026-11-14; candidate assignments must still pass pinned-item registry and progression validation.

## Core reward philosophy

**The reward is a small present, not a bureaucratic receipt.** Every claimable quest should normally grant direct XP plus one **physical, tangible, deliberate** item/stack. It should be (a) a useful head start on the mechanic being learned, (b) a cheap but genuinely useful tool/supply, (c) an unexpectedly funny, edible, oddity, or (d) a single Quest Shop coin **only if nothing physically appropriate exists**. Favor meaningful progression rewards over novelty at higher tiers. Humor is part of the pack's personality; "randomly inconvenient" is not humor.

- **Forbid generic inventory junk:** blank paper, random cookies, empty bowls and gratuitous compasses; do not grant these by default simply to satisfy an old fixed-item rule. Paper, cookie or a compass can still be an **actual authored mechanic's ingredient** or a **required task**, but must not be generic filler rewards.
- **One explicit bowl exception:** the quest for **Farmer's Delight Cooking Pot** should reward exactly **one \`minecraft:bowl\`**. Its joke is that you made dinner equipment and still need a bowl to eat. Do not scatter bowls elsewhere.
- **No \`minecraft:experience_bottle\` reward**, ever, including nested choice tables. FTB direct XP reward types are allowed.
- **Not cruel:** do not give harmful effects, delete player items, troll the inventory with renamed unstackables, or make a non-eating player consume food. Edible weirdness should be a gift, not a mandatory recipe or challenge.
- **Preserve progression:** no boss-gated materials, scarce ritual keystones, forbidden mods/items, post-Chaos Guardian ProjectE unlocks, or stack amounts that skip an intended mod tier.
- **No same-quest loop or accidental auto-claim:** don't give exactly the item required for another unrelated milestone unless the shortcut is explicitly designed; validate amounts, item IDs and team/player scope.
- **Lightweight by default:** single item or modest stack. Vary rewards *by narrative and utility*, not to hit an item-uniqueness metric. If one item genuinely fits several related objectives, that is acceptable.
- **A team coin is a fallback, not the new default.** Coins are Quest Shop's shared team currency; authors must label their scope to avoid many individual claims minting an unintended team balance.

## Reward selection algorithm for writers

In order, ask: (1) What resource, consumable or tool would make the NEXT real step easier without doing it for the player? (2) What quirky modded food or funny small practical item expresses the quest's theme? (3) Can the reward teach the existence of a useful derivative normally discovered later? (4) If none, is a one-coin team payout appropriate and safe? Never default to \`minecraft:paper\`, \`minecraft:cookie\`, \`minecraft:bowl\` or \`minecraft:compass\`.

A reward is **strong** if a player might actually equip, eat, use or tell someone about it. Write a one-line author-only explanation of the choice.

## A. Verified, readily available vanilla fallback palette

Vanilla IDs are definite for MC 1.21.1; prices and final stack sizes must still pass balance and task-registry tests.

| Item ID | Example small grant | Best use / playful beat |
| --- | ---: | --- |
| \`minecraft:brush\` | 1 | Ruins, archaeology, exploring strange places; "please don't lick the ancient vase" |
| \`minecraft:shears\` | 1 | Herb gathering, wool, gardens; practically useful |
| \`minecraft:flint_and_steel\` | 1 | Nether, experimental fires; safety prose must not encourage griefing |
| \`minecraft:scaffolding\` | 8–16 | Towers, multiblocks, base layout; directly useful in magical industry |
| \`minecraft:ladder\` | 12–16 | Mining, towers, ritual room access; a cheap escape plan |
| \`minecraft:oak_boat\` | 1 | Travel, maps, expeditions; "the official escape route" |
| \`minecraft:bucket\` | 1 | Water, blood systems where valid, fuel/water; usable after finding iron |
| \`minecraft:glow_berries\` | 4–8 | Light plus a slightly peculiar edible reward |
| \`minecraft:pumpkin_pie\` | 1–3 | A good harvest, kitchen/baking achievements |
| \`minecraft:rabbit_stew\` | 1 | One comically overprepared meal, unlike an **empty bowl** |
| \`minecraft:chorus_fruit\` | 1–3 | Surreal travel/End milestones **only after End access** |
| \`minecraft:honey_bottle\` | 1–2 | Witchcraft, botany and apothecary themes |
| \`minecraft:glow_ink_sac\` | 1–2 | Research display, illuminated maps or signage |
| \`minecraft:phantom_membrane\` | 1–2 | Mid/late traversal, real useful repair item, not trivial starter clutter |
| \`minecraft:firework_rocket\` | 2–4 | Celebration, long-range travel **after Elytra access**; do not overuse |
| \`minecraft:sea_pickle\` | 2–4 | Fishing and underwater exploration, useful light source |
| \`minecraft:lead\` | 1 | Ranching, animal handling, witches/animal quests |
| \`minecraft:bone_meal\` | 4–8 | An immediate farming follow-up, not five repeated mystery gifts |
| \`minecraft:slime_ball\` | 2–4 | Snares, sticky tricks, compact early ritual ingredients where appropriate |

### Explicit examples

- **Make a Crafting Table:** an early *useful oddity* like a small glow-berry snack, not two bowls.
- **Maps, navigation or travel:** a boat or a stack of scaffolding/ladder, NOT a compass.
- **Read a useful controls tip:** a small practical tool or an interesting edible surprise, NOT paper.
- **Use the Farmer's Delight Cooking Pot:** exactly **one empty bowl** (deliberate exception).
- **Discover Dark Gems (EvilCraft):** award **2 \`evilcraft:dark_gem_crushed\`**, the mod's actual **Crushed Dark Gem** item, rather than giving back the same raw gem. Its source config registers \`dark_gem_crushed\`, and it burns for 16,000 ticks as a fuel; inventory compatibility/claim still needs in-game QA.
- **Build a food or fishing station:** a finished strange food or a funny derivative ingredient, not generic steak for every food goal.

## B. Modded edible novelty pool — attractive candidates, not yet accepted runtime grants

The pinned pack includes **Pam's HarvestCraft 2 Food Core, NeoForge 1.21.1 1.0.4**. The following IDs are documented in an external item index, but **must be checked against the exact pinned 1.21.1 JAR registry or in-game JEI before an actual quest rewards them**. Do not replace existing tested reward IDs merely because the name sounds funny.

| Candidate item ID | The joke / use |
| --- | --- |
| \`pamhc2foodcore:gummycreepersitem\` | Gummy Creepers for a monster-related first success |
| \`pamhc2foodcore:chocolatebaconitem\` | Chocolate Bacon: culinary transgression for witchcraft/alchemy |
| \`pamhc2foodcore:chorusdonutitem\` | Chorus Donut for post-End magical travel |
| \`pamhc2foodcore:epicbaconitem\` | Epic Bacon for genuinely heroic, food-themed milestones |
| \`pamhc2foodcore:fishsticksitem\` | Fish Sticks when the player begins fishing |
| \`pamhc2foodcore:smoresitem\` | S'mores for campfire, night study or magic-tower initiation |
| \`pamhc2foodcore:cottoncandyitem\` | Cotton Candy for dreamlike/fairy magic |
| \`pamhc2foodcore:pickledbeetsitem\` | Pickled Beets for botanical, witchcraft or preservation rewards |
| \`pamhc2foodcore:hotdogitem\` | Hot Dog after a dangerous expedition, when humor matters more than literalism |
| \`pamhc2foodcore:p8juiceitem\` | P8 Juice as an oddball alchemical drink |
| \`pamhc2foodcore:fishandchipsitem\` | Fish and Chips for later Aquaculture milestones |
| \`pamhc2foodcore:marshmellowsitem\` | Marshmallows, provided the pinned registry confirms its spelling |
| \`pamhc2foodcore:jellybeansitem\` | Jelly Beans for a humble research- or control-screen tip |
| \`pamhc2foodcore:beefjerkyitem\` | Jerky as an exploration travel ration |
| \`pamhc2foodcore:chickennuggetitem\` | Chicken Nuggets for a very serious medieval chicken industry |

Modded novelty should be rare enough to be memorable. **No boss fights rewarded with 64 gummy candies.** A milestone reward can be four crushed gems, a cheap useful machine ingredient, or a small physical tool, with comedy in the quest title/description instead of only the item.

## C. Thematically useful progression rewards

| Chapter/school | Actual goal | Reward design pattern |
| --- | --- | --- |
| EvilCraft | Acquire Dark Gems | **Crushed Dark Gem** (\`evilcraft:dark_gem_crushed\`), 1–2; early discovery of a useful derivative |
| Occultism | First ritual ingredient | A small *different*, verified material used in its next ordinary ritual, not the finished summon or miner |
| Ars Nouveau | Create first spellbook | A minor verified novice spell component, not a high-tier glyph or unlock |
| Iron's Spells | Obtain spellbook | A valid low-tier scroll/ink component, only when exact pinned ID and spell tier verified |
| Neo Vitae | Discover blood altar progression | Low-volume ordinary ritual material, not a free tier transition |
| Mahou Tsukai | First meaningful circle | Verified circle reagent or small resource, not unlimited mana/stats |
| Botania Revived | First flowers / mana | Useful early flowers, fertilizer or botanical component; keep scarce runes out of early rewards |
| Scriptor Magicae | First discovered grammar | Ink/writing ingredient or verified utility, not a prewritten world-dependent "known spell" |
| Cooking/Fishing | Kitchen, real fish, rods | Finished bizarre food, small edible mystery or targeted cooking utensil |
| Magic industry / reactors | Real power milestone | Scaffolding and control/monitoring materials in modest quantities, not endless redstone/glass |

These entries are **reward-design concepts**, NOT fabricated item IDs. Work must fill in real registry evidence before generating the live item reward.

## D. Specific QA blockers

1. Validate every chosen modded item ID against the pinned live registry, not simply an older wiki page.
2. Check reward icon and count actually render in FTB Quests, including Choice Reward and Claim All.
3. Confirm no unrelated item-task auto-completion, no accidental team duplication and no restricted-gate bypass.
4. Record reward **why**, a funny one-line optional flavor, and what mechanic it supports in author-only metadata.
5. Review reused rewards at chapter level; warnings, not quotas or automatic rejections.
6. Keep the 12 approved commodity-sale coin contracts and actual Quest Shop accounting intact.

## Source notes

- Verified pinned EvilCraft line: \`evilcraft-1.21.1-neoforge-1.2.100\`. \`CyclopsMC/EvilCraft\` branch \`master-1.21-lts\`, \`src/main/java/org/cyclops/evilcraft/item/ItemDarkGemCrushedConfig.java\`; registers \`dark_gem_crushed\` and its 16000-tick fuel value. Exact user-game registry still requires client confirmation.
- Pam's item index: https://blockmicroscope.com/mods/pamhc2foodcore (indexes a prior 1.20 version; candidate IDs only). Pinned 1.21.1 1.0.4: https://www.curseforge.com/minecraft/mc-mods/pams-harvestcraft-2-food-core/files/6919485
- No quoted/copied third-party pack quest prose used.
