# Alcohol is Magic — quest progression and source ledger

**Authoring status:** 19-quest standalone Introduction chapter, researched against the installed 1.21.1 NeoForge B13 pins. Runtime/client acceptance pending. Produced 2026-11-15.

## Why this is a separate chapter

The user explicitly chose *Alcohol is Magic* as a standalone chapter rather than folding it into Food & Fishing. The parent chapter covers farms, crops, kitchens and fish; the new one teaches **Brewin' and Chewin'** (fermentation), **Cocktails Delight** (keg spirits + crafting mixes), and **[Let's Do] Beachparty** (tropical Palm Bar drinks + fridge + lounge). It creates NO new magic-system gate and no new mod requirement. The native vanilla Brewing Stand remains in Advanced Progression.

**First in-game reference:** Brewin' and Chewin' has no guidebook. The Advancements screen (L by default) contains a Beer-icon tab. Use JEI on the Keg to find its craft, Fermenting and Keg Pouring recipes. For Beachparty, use JEI Palm Bar Mixing and Mini Fridge Freezing. Cocktails Delight uses a mix of Fermenting/Keg Pouring and shapeless crafting recipes. All how-to steps are spelled out in quest text, so the player doesn't require an external website.

## Source-backed progression coverage matrix

| Stage / quest | Why/how it works | FTB completion criterion | Branch | Evidence and uncertainty |
| --- | --- | --- | --- | --- |
| Welcome to the Magic of Alcohol | Orient to three beverage systems | Manual acknowledge | Main | B13 installed |
| Read the Brewer's In-Game Reference | Open Brewin advancement page and inspect JEI categories | Manual, not proof of use | Main | [Upstream guide](https://github-wiki-see.page/m/Monad-Modding/BrewinAndChewin/wiki/Getting-Started) |
| Build a Keg | Honeycomb + iron + plank 3×3 recipe | Keg inventory acquisition | Main | [Keg recipe](https://github.com/Monad-Modding/BrewinAndChewin/blob/1.21.1/common/src/generated/resources/data/brewinandchewin/recipe/keg.json) |
| Make Your Serving Tankards | 5 planks + 2 iron nuggets → 4 Tankards | Tankard inventory acquisition | Main | [Tankard recipe](https://github.com/Monad-Modding/BrewinAndChewin/blob/1.21.1/common/src/generated/resources/data/brewinandchewin/recipe/tankard.json) |
| Prime the Keg and Read Its Temperature | 4 inputs, container/fluid slot, temperature, progress | Manual positive action | Main | Upstream guide; in-pack JEI and temperature UI need verification |
| First Brew: Beer | 1000 mB water + dough tag + wheat seeds + brown mushroom | Manual after successful pour | Main | [Beer Fermenting](https://github.com/Monad-Modding/BrewinAndChewin/blob/1.21.1/common/src/generated/resources/data/brewinandchewin/recipe/fermenting/beer_from_water.json); *dough tag* must be filled in assembled pack |
| A Honey Mead Batch | 1000 mB honey, wheat, wheat seeds, sweet berries | Manual | Optional | [Mead Fermenting](https://github.com/Monad-Modding/BrewinAndChewin/blob/1.21.1/common/src/generated/resources/data/brewinandchewin/recipe/fermenting/mead_from_honey.json) |
| A Keg Makes Food, Too | 250 mB milk + sugar + cocoa beans for fudge; cheese branch | Manual | Optional | [Fudge recipe](https://github.com/Monad-Modding/BrewinAndChewin/blob/1.21.1/common/src/generated/resources/data/brewinandchewin/recipe/fermenting/cocoa_fudge_from_milk.json) |
| Transfer a Full Keg | Move a filled 1000 mB Keg safely | Manual | Optional | Upstream guide, must test pickup preservation |
| Cocktails Delight: Learn the Two Workflows | Differ between fermented spirit and shapeless mixed drink | Manual | Optional | [Cocktails Delight overview](https://modrinth.com/mod/cocktails-delight) |
| Ferment White Rum | 1000 mB water + 3 sugar cane + sugar, temp 4, 8400 ticks | Rum inventory acquisition | Optional | [Rum fermentation](https://github.com/DraconicArcher/CocktailsDelight/blob/main/src/main/resources/data/cocktailsdelight/recipe/fermenting/rum_from_water.json); temperature environment needs test |
| Mix a Drink and Check Its Side Effects | Look up rum uses and craft a mixed drink; review Tipsy/XP buffs | Manual | Optional | Upstream cocktail item source; no unverified XP-stacking claims |
| The Piña Colada Ingredient Puzzle | Recipe only enabled when **both** pineapple and coconut food tags nonempty | Manual tag/JEI inspection, no impossible craft requirement | Optional | [Conditional Piña Colada recipe](https://github.com/DraconicArcher/CocktailsDelight/blob/main/src/main/resources/data/cocktailsdelight/recipe/pina_colada.json); runtime tags unresolved |
| Find Coconuts and Palm Wood | Coastal palms, coconuts, open coconut, palm planks | Manual | Tropical | [Beachparty official docs](https://lets-do.ch/wiki/beachparty/) |
| Construct a Palm Bar | 7 Palm Planks → 3 bars | Palm Bar inventory acquisition | Tropical | [Palm Bar recipe](https://github.com/Let-s-Do-Collection/Beachparty/blob/main/common/src/main/resources/data/beachparty/recipe/palm_bar.json) |
| Build a Mini Fridge for Ice | 5 iron + redstone; water bottle → ice in 1200 ticks | Mini Fridge inventory acquisition | Tropical | [Mini Fridge](https://github.com/Let-s-Do-Collection/Beachparty/blob/main/common/src/main/resources/data/beachparty/recipe/mini_fridge.json); [Ice](https://github.com/Let-s-Do-Collection/Beachparty/blob/main/common/src/main/resources/data/beachparty/recipe/mini_fridge_freezing/ice.json) |
| Serve a Coconut Cocktail | Palm Bar Mixing: open coconut + ice + honey bottle | Coconut Cocktail inventory acquisition | Tropical | [Palm Bar recipe](https://github.com/Let-s-Do-Collection/Beachparty/blob/main/common/src/main/resources/data/beachparty/recipe/palm_bar_mixing/coconut_cocktail.json) |
| Place, Sip, and Compare Effects | Set drink as block and interact; compare sip/hand use | Manual | Optional tropical | [Official cocktails](https://lets-do.ch/wiki/beachparty/cocktails/) |
| Open the Beach Bungalow | Put the station and drinks into a usable coastal bar | Manual | Optional capstone | In-pack test pending; no furniture detection asserted |

## Genuine cross-mod uncertainty and safeguards

1. **Piña Colada is not guaranteed craftable:** Recipe requires `c:foods/pineapple_item` and `c:foods/coconut` tags. The pinned Pam's trees and Beachparty coconut items must actually participate in those tags at runtime; absent either, the recipe is conditionally removed. Do not silently grant an item or fabricate KubeJS fixes. Raise a separate compatibility issue after client registry inspection if necessary.
2. **Fermentation can stall:** The Keg relies on a 1000 mB fluid, correct four inputs, a serving container and recipe temperature. Test that a specific pinned recipe actually makes progress. Merely owning the Keg cannot prove it.
3. **No random reward economy:** Each quest gives positive XP and a fixed small vanilla item; task item IDs unique in the whole book and never appear as rewards. No coins, no automated character-skill XP bypass, no forced drink consumption.
4. **Effect audit:** Cocktail status effects can overlap with class progression or skill experience and should be tested against Tyz/MXP before claiming player-safe balance. No new gameplay gates were implemented.
5. **Client/server acceptance:** Open FTB Quests on a disposable world, find Intro → Alcohol is Magic, verify title/subtitle and all 19 descriptions, test non-overlapping layout, item acquisition detection and manual completions, one-claim rewards, team/single-player independence, brewing temperature and fluid operations, Palm Bar mixer and fridge, Cocount Cocktail item ID, optional Piña recipe visibility. A static validator or server 'Done' alone is not a tested walkthrough.

**Source status:** Current upstream recipe paths were inspected, but identical semantics in the exact downloaded JAR and correct FTB UI rendering have not yet been confirmed in a graphical client. The chapter must not be called client-accepted until the above QA passes.
