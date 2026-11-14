# Early magic unlocks — cross-mod utility inventory

**2026-11-14 research-only candidate.** Based on the currently pinned 140-mod NeoForge 1.21.1 TMM branch and external documentation. The objective is to surface **easy, useful unlocks in Tips & Tricks** while leaving each mod's detailed progression in its own chapter. **No quest content, rewards, item registries, recipes, or gates are modified by this research.**

## Inventory rules

- Organize player tips by **the problem to solve** (ore yield; fast processing; light; movement/flight; portable crafting; storage; item pickup; farming; navigation) instead of hiding them in the alphabetic 25-mod category.
- Include prerequisites, early/short-chain/later status, whether the method changes *ores*, *raw ore*, *ingots*, *mod-only catalysts*, or *drops*, and a link into the specific magic chapter. A two-powder output is not necessarily two smeltable ingots.
- **Every claim remains unverified in the exact client until JEI/recipe dumps and actual gameplay tests.** `strong` means a credible external upstream description, not a client acceptance test. `tentative` rows are research leads, never publish as concrete advice.
- Keep the pack's **no generic bag/logistics, no Create, no pre-Guardian ProjectE/Expansion and no bag nesting** constraints. Do not use shop availability or a quest reward to bypass a boss/material gate.
- The 25 primary Magic chapters are each represented below (including mods with no verified early convenience yet). Artifacts, Vampirism, Werewolves, other exploration chapters are separately inventoried later.

## Three crucial distinctions

1. **Mahou mortar:** external recipes produce **2 catalyst powders from an ingot/gem**, not two smeltable ingots from raw ore. TMM's planned *ore → 2 unified, smeltable dust* change belongs to the separate ore/dust compatibility project, not a claim about the current pack. Do not publish the 'ore doubling' tip until that change actually passes a recipe+smelt test.
2. **Lectern versus smelting:** Ars Nouveau **Storage Lectern** links inventories and permits crafting; its **Smelt glyph** is a distinct **Tier 2** mage spell. No current reliable source establishes 'smelting through a lectern' as an early unlock. Verify the user's remembered mechanic before presenting it as fact.
3. **EvilCraft Vengeance Pickaxe:** upstream Origins of Darkness describes enormous **Fortune**; ATM10's current reference reports **Fortune V plus Curse of Vengeance III**. It is a high-yield but cursed diamond-tier pickaxe, **not** a vein-miner and not automatic ore-processing. EvilCraft **broom** is a separately crafted blood-powered mobility item.

## Ore processing & smelting

| Unlock | Approximate access | What actually saves work | Status / caveat |
| --- | --- | --- | --- |
| **Mahou Tsukai: Mortar and Pestle** ([source](https://mahoutsukai.stepsword.com/guides/getting-started/)) | early | One supported ingot/gem to two Mahou catalyst powders; inexpensive entry into useful powder processing. | strong; NOT presently a proven raw-ore doubling-to-ingots route. TMM raw-ore input + smeltable dust is a separate unimplemented compat decision. |
| **Mahou Tsukai: Hammer** ([source](https://mahoutsukai.stepsword.com/guides/getting-started/)) | early | Lower-cost alternate to Mortar and Pestle; yields one powder per supported catalyst. | strong; Less efficient than two-powder Mortar; not automatic smelting. |
| **Pastel: Anvil crushing gemstone shards** ([source](https://mcmodwiki.com/pastel/guides/gemstones-pigments-and-pigment-pedestal-progression)) | early | Early mechanical anvil crushing turns gemstone shards into colored powder for Pigment Pedestal work. | moderate; Not generic metal-ore 2x; Create crusher integration is irrelevant because Create is excluded. |
| **Occultism: Foliot Crusher** ([source](https://privatechunks.net/wiki/mod/occultism)) | short chain | After first ritual/pentacle setup, Foliot processes supported raw ores into two dusts; dust may then be smelted if configured. | strong; Requires Demon's Dream, Dictionary, bound Foliot book, chalk and four ritual material offerings; confirm silver access and dust smelt tags. |
| **EvilCraft: Vengeance Pickaxe** ([source](https://evilcraft.rubensworks.net/book/second_age/tools/vengeance_pickaxe.html)) | short chain | Very high Fortune (typically V) at craft time, with Vengeance curse/spirit-spawning danger. | strong; Needs diamonds, hardened blood shards and undead wood sticks; it is NOT intrinsically a vein miner. Fortune is not traditional 2x raw-ore-to-ingot processing. |
| **Enchanted: Witchcraft: Witch's Oven + Clay Jars** ([source](https://mcmodwiki.com/enchanted-witchcraft/guides/witchs-oven-fumes-and-distillery-guide)) | short chain | Burn saplings/foods to generate ash and collect fumes; useful concurrent cooking and witchcraft byproducts. | moderate; NOT general metal ore smelting (Witchery-derived ovens don't smelt ores); check exact fuel/cooking speed. |
| **Ars Nouveau: Smelt glyph** ([source](https://ars.guide/1.21.1/docs/spell_theory/glyphs/)) | later | Tier 2 field smelting of blocks or dropped items; AOE handles multiple, Dampen selects smoking. | strong; REQUIRES Mage-tier casting and glyph crafting; not the Storage Lectern. |
| **Ars Nouveau: Crush glyph** ([source](https://ars.guide/1.21.1/docs/spell_theory/glyphs/)) | later | Tier 2 converts stone to gravel, gravel to sand, flowers to dye; JEI determines other recipes. | strong; Do not label generic ore doubling without a recipe for each ore. |
| **Pastel: Cinderhearth** ([source](https://mcmodwiki.com/pastel/guides/cinderhearth-enchanter-and-spirit-instiller-structures)) | later | Processes pure resource clusters into doubled output in a specialized multiblock. | moderate; Requires Pure Resource clusters and multiblock; cannot promise two ingots per ordinary raw ore. |
| **Malum: Spirit Crucible + Alchemical Impetus** ([source](https://www.minecraft-guides.com/mod/malum/)) | later | Produces valuable reagents, including redstone/glowstone, and processes metals into extra-output nodes. | moderate; Needs altar, spirits, rocks and repairable impetus; not a first-day magic doubling route. |
| **Theurgy: Sulfur / Salt / Mercury apparatus** ([source](https://github.com/AllTheMods/ATM-11/blob/main/config/ftbquests/quests/lang/en_us/chapters/theurgy.json5)) | later | Allows resource conversions after establishing its furnace heat, Sal Ammoniac and alchemical cycle. | moderate; Not equivalent to free early doubling; don't suggest post-gate resource conversion shortcuts. |
| **Neo Vitae: Alchemy Table / cutting tools** ([source](https://wiki.9minecraft.net/java/mod/neo-vitae/neovitae-alchemytable/)) | unknown | Large set of alchemical recipes, including mod-specific processing of mineral or gem dust. | tentative; Potential low-tier doubling is important but exact conversion costs/tier need installed recipe audit; do not claim ordinary 2x ores yet. |

## Mining, gathering & resource finding

| Unlock | Approximate access | What actually saves work | Status / caveat |
| --- | --- | --- | --- |
| **Ars Nouveau: Break glyph + Novice Spell Book** ([source](https://ars.guide/1.21.1/docs/spell_theory/glyphs/)) | early | Once you have the novice book, the Tier 1 Break glyph can mine ordinary blocks using spell mana. | strong; Requires book and sufficient mana; harvest level/ore drops must be checked in pack. |
| **Ars Nouveau: Harvest / Cut glyphs** ([source](https://ars.guide/1.21.1/docs/spell_theory/glyphs/)) | early | Use entry-tier effects for crop harvesting and shearing-style utility. | strong; Check crop and harvest interaction per mod, not universal crop doubling. |
| **Occultism: Demon's Dream Seeds + Spiritfire** ([source](https://www.minecraft-guides.com/mod/occultism/)) | early | Early hidden-world materials and conversions after collecting seeds from grass. | strong; Not a bulk smelter or automatic 2x ingot processing. |
| **Occultism: Demon's Dream Third Eye / Otherworld Goggles** ([source](https://www.minecraft-guides.com/mod/occultism/)) | early | Reveal disguised Otherworld materials without blindly mining them. | strong; Goggles require more ritual progress; the earliest temporary effect may be simpler. |
| **Roots Classic: Bark Knife** ([source](https://wiki.9minecraft.net/java/mod/roots-classic/guides/how-to-craft-and-use-the-mortar-and-pestle/)) | early | Harvest different barks from ordinary trees; unlocks first spell-crafting components. | moderate; Do not confuse with a generic tree-felling/wood-doubling axe. |
| **Astral Sorcery: Resonating Wand** ([source](https://github.com/HellFirePvP/AstralSorcery/blob/1.21.1/src/main/resources/assets/astralsorcery/lang/en_us.json)) | short chain | In pinned 1.21.1 branch, reveals deeply buried Rock Crystal with sparkles; also starts starlight crafting. | strong; Requires focal-point crafting and night sky. This is rock-crystal finding, not a general metal-ore scanner. |
| **Astral Sorcery: Starlight focal points** ([source](https://github.com/HellFirePvP/AstralSorcery/blob/1.21.1/src/main/resources/assets/astralsorcery/lang/en_us.json)) | short chain | Early open-air starlight transformations; book specifically mentions iron ore changing at focal points. | strong; Conversion outcome and ratios must be looked up in the pinned recipe data; do not promise arbitrary ore doubling. |
| **Theurgy: Divination Rod / Rod of Amethyst** ([source](https://github.com/AllTheMods/ATM-11/blob/main/config/ftbquests/quests/lang/en_us/chapters/theurgy.json5)) | unknown | Specialized resource and amethyst geode locating, potentially a strong anti-grind alternative. | tentative; ATM11 text is not guaranteed TMM recipe or early unlock. Check pinned Theurgy divination ritual and item ID. |

## Mobility and flight

| Unlock | Approximate access | What actually saves work | Status / caveat |
| --- | --- | --- | --- |
| **Ars Nouveau: Leap / Launch glyphs** ([source](https://ars.guide/1.21.1/docs/spell_theory/glyphs/)) | early | Tier 1 aerial hops or forward leaps; earlier than true sustained flight. | strong; Landing hazards remain; no guaranteed safe fall. |
| **EvilCraft: Modular Blood Broom** ([source](https://evilcraft.rubensworks.net/)) | short chain | Assemble rod, brush and cap; blood-powered flight with custom upgrades. | strong; Requires blood, dark materials and parts; use official Origins of Darkness/JEI for fuel and exact controls. Some secondary pages incorrectly say no fuel. |
| **EvilCraft: Blood Pearl of Teleportation** ([source](https://privatechunks.net/wiki/mod/evilcraft)) | short chain | Reusable, blood-fed short-range pearl alternative. | moderate; Confirm recipe and usage; not a Waystone or universal dimension teleporter. |
| **Hexerei: Modular Hexerei Broom** ([source](https://modrinth.com/mod/hexerei)) | short chain | Distinct broom-flight path involving a broom body/brush and witchcraft materials. | strong; Use correct Hexerei craft sequence, not EvilCraft's blood-powered mechanic. |
| **Hexerei: Broom Whistle** ([source](https://wiki.9minecraft.net/java/mod/hexerei/guides/altars-brooms-and-couriers-setting-up-utility-blocks/)) | short chain | Retrieve or summon your Hexerei broom after exploring. | moderate; Requires a broom first; don't present the whistle as transport on its own. |
| **Reliquary Reincarnations: Rending Gale** ([source](https://wiki.9minecraft.net/java/mod/reliquary/guides/reliquary-magical-artifacts/)) | later | Fuelled aerial mobility / push-pull tool using feathers. | moderate; Acquisition more involved; not a reliable first-day craft. |

## Portable crafting, storage & pickup

| Unlock | Approximate access | What actually saves work | Status / caveat |
| --- | --- | --- | --- |
| **EvilCraft: Wooden Exalted Crafter / Exalted Crafter** ([source](https://evilcraft.rubensworks.net/book/first_age/items/exalted_crafter.html)) | early | Portable 3x3 crafting; wooden variant has 27 slots, standard variant can pair with Ender Chest; keybindable. | strong; Uses Crushed Dark Gems, gold/iron; Ender Chest variant later than wooden variant. |
| **Ars Nouveau: Storage Lectern + Bookwyrms** ([source](https://ars.guide/book/crafting/storage_lectern/)) | short chain | Central access and crafting from linked inventories; protects pack's magical-storage identity. | strong; Enchanting Apparatus, Bookwyrm(s), linked inventories and Dominion Wand; NOT a smelter. |
| **Occultism: Foliot Janitor / transport helpers** ([source](https://wiki.9minecraft.net/java/mod/occultism/guides/occultism-spirits-and-tiers/)) | short chain | Early spirit automation can move or collect dropped items, reducing repetitive pickup. | moderate; Requires appropriate separate ritual; avoid describing as free generic pipes. |
| **EvilCraft: Kineticator** ([source](https://privatechunks.net/wiki/mod/evilcraft)) | short chain | Pulls item drops and XP; useful for mines, farms and mob areas. | moderate; Check blood drain and item range in installed config. |
| **EvilCraft: Blood Chest** ([source](https://privatechunks.net/wiki/mod/evilcraft)) | short chain | Repairs items using Blood rather than repeatedly re-crafting tools. | moderate; Needs blood supply and infusion; check repair compatibility. |
| **Hexerei: Courier Depot + letter/package** ([source](https://wiki.9minecraft.net/java/mod/hexerei/guides/altars-brooms-and-couriers-setting-up-utility-blocks/)) | short chain | Physical, thematic delivery mechanism between working areas of a large base. | moderate; Could have distance/activation limitations; not equivalent to banned generic item logistics. |
| **Hexerei: Small Satchel** ([source](https://wiki.9minecraft.net/java/mod/hexerei/guides/altars-brooms-and-couriers-setting-up-utility-blocks/)) | short chain | Named magical storage option worth comparing against starter Trinkets Pouch. | tentative; Check exact recipe/slot count/nesting policy in installed build before advertising. |
| **Reliquary Reincarnations: Coin of Fortune** ([source](https://wiki.9minecraft.net/java/mod/reliquary/guides/reliquary-magical-artifacts/)) | short chain | Inventory magnet for nearby items/XP; compare against EvilCraft Kineticator and Botania ring. | moderate; Documentation conflict with another page calling it a mob-drop booster. Confirm real behavior in pinned 1.21.1 client. |
| **Botania Revived: Ring of Magnetization** ([source](https://botaniamod.net/lexicon.html)) | short chain | Pulls dropped items toward the wearer after basic Manasteel and lens crafting. | strong; Verify specific port recipe in pack; requires mana setup. |
| **Botania Revived: Hopperhock flower** ([source](https://botaniamod.net/lexicon.html)) | short chain | Collects nearby item entities into adjacent inventories. | moderate; Requires mana/functional flower setup and nearby storage; avoid blanket AFK item-generation claims. |
| **Goety: Focus Bag** ([source](https://goety.wiki.gg/wiki/Getting_Started)) | short chain | Stores several wand focuses to avoid carrying each one loose. | strong; Useful once the player has a wand and focuses, not before. |
| **Occultism: Dimensional Storage Actuator** ([source](https://www.minecraft-guides.com/mod/occultism/)) | later | Expandable magical remote storage as an intentional primary storage route. | moderate; NOT day-one: ritual and component chain; avoid misleading early-access placement. |

## Light, survival, farming and initial school utilities

| Unlock | Approximate access | What actually saves work | Status / caveat |
| --- | --- | --- | --- |
| **Ars Nouveau: Conjure Magelight glyph** ([source](https://ars.guide/1.21.1/docs/spell_theory/glyphs/)) | early | Tier 1: place persistent magical light on blocks or grant temporary night vision when self-cast. | strong; Avoid falsely claiming it is free or consumes no mana. |
| **Malum: Soulstone Ore** ([source](https://www.minecraft-guides.com/mod/malum/)) | early | Raw Soulstone smelts into two Processed Soulstone; easy early access to Malum's guide and first tools. | moderate; The 2:1 here is a MOD RESOURCE recipe, not general ore doubling. |
| **Enchanted: Witchcraft: Earmuffs for Mandrake** ([source](https://mcmodwiki.com/enchanted-witchcraft/1.21.1)) | early | Simple protective equipment so harvesting Mandrake doesn't punish the player. | moderate; Only relevant once growing Mandrake. |
| **Roots Classic: Mortar and Pestle** ([source](https://wiki.9minecraft.net/java/mod/roots-classic/guides/how-to-craft-and-use-the-mortar-and-pestle/)) | early | Grind collected plants/bark into Growth, Spell and Mutating Powders. | moderate; Different from Mahou's item-powder doubling. Doesn't automatically yield 2 metal ingots. |
| **Eidolon: Repraised: Ars Ecclesia + first altar** ([source](https://mcmodwiki.com/eidolonrepraised/guides/getting-started-guide)) | early | Very early low-cost guide/prayer entry but not a proven general mining or smelting shortcut. | moderate; Worth only a guide link until a functional utility item is confirmed. |
| **Reliquary Reincarnations: Sojourner's Staff** ([source](https://wiki.9minecraft.net/java/mod/reliquary/guides/reliquary-magical-artifacts/)) | short chain | Store torches and place them remotely, reducing repetitive torch placement. | moderate; Requires Molten Core or comparable mob drop; not necessarily day one. |
| **Reliquary Reincarnations: Lantern of Paranoia** ([source](https://wiki.9minecraft.net/java/mod/reliquary/guides/reliquary-magical-artifacts/)) | short chain | Automatically places light in dark locations using carried torches. | moderate; Requires fuel torches and crafting drops; check placement behavior. |
| **Botania Revived: Endoflame + Mana Spreader + Pool** ([source](https://www.curseforge.com/minecraft/mc-mods/botania-revived)) | short chain | Early charcoal/coal-fed mana generation for the first Botania tools and transformations. | strong; Magic mana, not FE. Require actual functional flowers and a mana receiver. |
| **Goety: Totem of Roots + Soul Energy** ([source](https://goety.wiki.gg/wiki/Getting_Started)) | short chain | An early soul collector fueled by killing mobs; entry into summon/utility focus crafts. | strong; First Ectoplasm is a loot/mob constraint. No prebuilt generic summoned minions granted. |
| **Iron's Spells 'n Spellbooks: Apprentice's Spell Book** ([source](https://wiki.9minecraft.net/java/mod/irons-spells-n-spellbooks/)) | short chain | Craft or loot an entry-level book to begin casting known scrolls. | moderate; Available spells are book/loot dependent; cannot promise a specific no-cost smelting/flying spell. |
| **Iron's Spells 'n Spellbooks: Inscription Table** ([source](https://wiki.9minecraft.net/java/mod/irons-spells-n-spellbooks/)) | short chain | Provides spellbook upgrades and capacity beyond the very first cast. | moderate; Check actual requirements; crafted scroll forge requires crying obsidian. |
| **Psi: Iron CAD + Mine spell bullets** ([source](https://mcmodwiki.com/psi/guides/spells-you-program-guide)) | short chain | Programmable mining or block operations possible from a fairly early hardware tier. | moderate; Requires learning CAD spell programming and valid spell snippets, not a zero-effort vein miner. |
| **Reliquary Reincarnations: Tome of Alkahestry** ([source](https://wiki.9minecraft.net/java/mod/reliquary/guides/reliquary-magical-artifacts/)) | later | Limited item duplication at charge cost, potentially extremely valuable. | tentative; BALANCE BLOCKER: inspect accepted items, costs and interaction with boss/ProjectE gating. Do not feature as an endorsed early shortcut. |
| **Botania Revived: Orechid** ([source](https://botaniamod.net/lexicon.html)) | later | Stone conversion into ores powered by substantial mana. | strong; NOT beginner access; must not bypass expensive ore progression or locked tech materials. |
| **Enchanted: Witchcraft: Protective Poppets and Poppet Shelf** ([source](https://mcmodwiki.com/enchanted-witchcraft/1.21.1)) | later | Witchcraft-specific protection from selected threats and storage outside inventory. | moderate; Recipes and supported protections vary; do not treat all totems or death prevention as compatible until handler audit. |

## Coverage: 25 magical traditions

| Magic chapter | Investigated candidates | Current first-pass result |
| --- | ---: | --- |
| Ars Nouveau | 7 | 5 credible first-pass tips |
| Astral Sorcery | 2 | 2 credible first-pass tips |
| Botania Revived | 4 | 3 credible first-pass tips |
| Eidolon: Repraised | 1 | 1 credible first-pass tips |
| Enchanted: Witchcraft | 3 | 2 credible first-pass tips |
| EvilCraft | 6 | 6 credible first-pass tips |
| Forbidden & Arcanus | 1 | Needs primary-version research / client check |
| Goety | 2 | 2 credible first-pass tips |
| Hex Casting | 1 | Needs primary-version research / client check |
| Hexerei | 4 | 3 credible first-pass tips |
| Iron's Spells 'n Spellbooks | 2 | 2 credible first-pass tips |
| Mahou Tsukai | 2 | 2 credible first-pass tips |
| Malum | 2 | 1 credible first-pass tips |
| Neo Vitae | 1 | Needs primary-version research / client check |
| Occultism | 5 | 4 credible first-pass tips |
| Pastel | 3 | 1 credible first-pass tips |
| Primal Magick | 1 | 1 credible first-pass tips |
| Psi | 1 | 1 credible first-pass tips |
| Roots Classic | 2 | 2 credible first-pass tips |
| Scriptor Magicae | 1 | Needs primary-version research / client check |
| Theurgy | 2 | Later-stage utilities; no proven easy unlock |
| Totemic | 1 | Needs primary-version research / client check |
| Just Another Witchery Remake | 1 | Needs primary-version research / client check |
| Embers Re-Ignited | 1 | Needs primary-version research / client check |
| Reliquary Reincarnations | 5 | 3 credible first-pass tips |

## Next verification pass (before writing FTB tips)

1. **In the newest Prism candidate, search JEI by exact names**: `mahoutsukai:mortar_and_pestle`, Foliot Crusher ritual, `evilcraft:vengeance_pickaxe`, Wooden Exalted Crafter, both broom systems, Ars Glyph Smelt, Storage Lectern, Botania Ring of Magnetization, Reliquary Sojourner's Staff, Pastel Cinderhearth, Malum Spirit Crucible and Theurgy divination tools. Capture actual recipes and earliest prerequisite path.
2. **Test material conversion with actual counts.** One raw iron, one iron ingot, one ore block and one raw iron block are not interchangeable. For each entry measure input count, output type/count, ability to smelt and XP/energy/fuel requirements. Do not confuse Fortune V, two catalyst powders, raw-ore 2x crushing, and later pure-material 2x smelting.
3. **Verify safety/cost**: EvilCraft broom Blood consumption, Vengeance curse and its failure risks, spell glyph tiers, Bookwyrm/Enchanting Apparatus cost, Hexerei brooms, malum spirit requirements, loot-gated Reliquary catalysts.
4. **Security/economy**: inspect Reliquary's Tome of Alkahestry allowed duplication table (especially boss and post-Guardian tech materials), Pastel Bottomless Bundle/Hexerei satchel bag nesting, and all cross-mod dust recipes in the independent unification work.
5. **Follow up on the six weakly researched schools** (Forbidden & Arcanus, Hex Casting, Scriptor Magicae, Totemic, JAWR, Embers) and Theurgy/Neo Vitae recipe specifics before promising their benefits. Read their pinned 1.21.1 source, not just ATM10 anecdotes.
6. **Tips & Tricks authoring**: make question-led sections (`Need ore yield?`, `Need portable smelting?`, `Want to fly?`, `Need a magnet?`, `Need shared crafting?`, `Want easy light?`) with at most 2–4 curated options each, clear prerequisites and independent manual checkmarks. Existing thematic specialist chapters keep their actual acquisition quests. Tip rewards remain small fixed items + XP; no duplicated objective completion.

## Sources and provenance

URLs for each item are carried in `manifest/early_unlock_inventory.json`. Most reliable: Ars Guide, Mahou Tsukai wiki, Origins of Darkness, Botania Lexicon, Astral Sorcery 1.21.1 source and Goety's own Wiki. Secondary pack indices and community wikis identify additional candidate conveniences but **do not prove the exact installed recipes**.

