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
2. **Lectern versus smelting — CORRECTED:** Scriptor Magicae's **Casting Lectern + Inflame** DOES automate standard furnace-smelting recipes **without fuel, FE or player mana** once a suitable spellbook is installed. The output requires smeltable input; the *spell cost* limits how expensive/fast the automated spell can be, it is not a resource consumed. See exact pinned-version caveat below about chest targeting. This is **not** Ars Nouveau's **Storage Lectern** (linked inventory and crafting), and Ars's separate **Smelt glyph** is Tier 2.
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
| Botania Revived | 9 | 4 credible early/short-chain tips; 5 later utilities |
| Eidolon: Repraised | 1 | 1 credible first-pass tips |
| Enchanted: Witchcraft | 3 | 2 credible first-pass tips |
| EvilCraft | 6 | 6 credible first-pass tips |
| Forbidden & Arcanus | 1 | Mid/later tool smelting (forge gated) |
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
| Scriptor Magicae | 1 | 1 **source-confirmed fuel-free Inflame-smelting** tip; chest automation to test |
| Theurgy | 2 | Later-stage utilities; no proven easy unlock |
| Totemic | 1 | Needs primary-version research / client check |
| Just Another Witchery Remake | 1 | 1 credible first-pass tip (ATM port uncertainty) |
| Embers Re-Ignited | 1 | Needs primary-version research / client check |
| Reliquary Reincarnations | 5 | 3 credible first-pass tips |

## Next verification pass (before writing FTB tips)

1. **In the newest Prism candidate, search JEI by exact names**: `mahoutsukai:mortar_and_pestle`, Foliot Crusher ritual, `evilcraft:vengeance_pickaxe`, Wooden Exalted Crafter, both broom systems, Ars Glyph Smelt, Storage Lectern, Botania Ring of Magnetization, Reliquary Sojourner's Staff, Pastel Cinderhearth, Malum Spirit Crucible and Theurgy divination tools. Capture actual recipes and earliest prerequisite path.
2. **Test material conversion with actual counts.** One raw iron, one iron ingot, one ore block and one raw iron block are not interchangeable. For each entry measure input count, output type/count, ability to smelt and XP/energy/fuel requirements. Do not confuse Fortune V, two catalyst powders, raw-ore 2x crushing, and later pure-material 2x smelting.
3. **Verify safety/cost**: EvilCraft broom Blood consumption, Vengeance curse and its failure risks, spell glyph tiers, Bookwyrm/Enchanting Apparatus cost, Hexerei brooms, malum spirit requirements, loot-gated Reliquary catalysts.
4. **Security/economy**: inspect Reliquary's Tome of Alkahestry allowed duplication table (especially boss and post-Guardian tech materials), Pastel Bottomless Bundle/Hexerei satchel bag nesting, and all cross-mod dust recipes in the independent unification work.
5. **Follow up on the least-researched schools** (Hex Casting, Totemic, Embers Re-Ignited, Neo Vitae) plus Theurgy's recipe specifics; Forbidden & Arcanus, Scriptor Magicae and JAWR now have plausible mechanics, but still need pinned-port tests. Read their pinned 1.21.1 source, not just ATM10 anecdotes.
6. **Tips & Tricks authoring**: make question-led sections (`Need ore yield?`, `Need portable smelting?`, `Want to fly?`, `Need a magnet?`, `Need shared crafting?`, `Want easy light?`) with at most 2–4 curated options each, clear prerequisites and independent manual checkmarks. Existing thematic specialist chapters keep their actual acquisition quests. Tip rewards remain small fixed items + XP; no duplicated objective completion.

## Sources and provenance

URLs for each item are carried in `manifest/early_unlock_inventory.json`. Most reliable: Ars Guide, Mahou Tsukai wiki, Origins of Darkness, Botania Lexicon, Astral Sorcery 1.21.1 source and Goety's own Wiki. Secondary pack indices and community wikis identify additional candidate conveniences but **do not prove the exact installed recipes**.


## Supplemental verification: fast furnace smelting and alternate lectern (2026-11-14)

This additional source pass identified several high-value overlooked conveniences; these should be considered before writing the question-led Tips & Tricks pages. **These are upstream capabilities, not live tests of the installed Botania Revived port.**

| Problem solved | Candidate | Access | Verified mechanism / open question |
| --- | --- | --- | --- |
| Smelt without coal or waiting | **Botania Revived: Exoflame** ([original Botania Lexicon](https://botaniamod.net/lexicon.html)) | **Later (Runic Altar, Fire and Summer Runes)** | Mana powers and speeds nearby **ordinary Furnaces**; useful once Botania is established, NOT an immediate beginner shortcut. Verify recipe and mechanics in Revived port |
| Accelerate furnace manually | **Botania Revived: Manatide Bellows** ([Lexicon](https://botaniamod.net/lexicon.html)) | Short magical setup | Point toward active Furnace, right-click to increase speed and fuel efficiency; requires player attention |
| Smelt a block in-place | **Botania Revived: Rod of the Molten Core** ([Lexicon](https://botaniamod.net/lexicon.html)) | Later | Turns **placed sand into glass** and **cobblestone into stone**; not proof of ore-dust smelting |
| Locate hidden ores | **Botania Revived: Rod of the Plentiful Mantle** ([Lexicon](https://botaniamod.net/lexicon.html)) | Later | Highlights nearby underground ore blocks with Mana; a resource finder, not an ore generator |
| Bulk carry blocks | **Botania Revived: Black Hole Talisman** ([Lexicon](https://botaniamod.net/lexicon.html)) | Later | Stores one block type; confirm no container-nesting violations in TMM |
| Smelt mined block drops | **Forbidden & Arcanus: Smelter Prism** ([ATM10 related quest data](https://github.com/AllTheMods/ATM10-Aeronautics/blob/main/config/ftbquests/quests/lang/en_us/chapters/forbidden__arcanus.snbt)) | **After tier 1 Hephaestus Forge**, not immediate | Smelts mined drops on equipped tools (raw ore→ingot, logs→charcoal, sand→glass). Forge is a substantial 9×9 ritual structure; verify Relic input and TMM craft |
| **Smelt automatically without fuel** | **Scriptor Magicae: Casting Lectern + Inflame** ([actual Inflame source](https://github.com/ssblur/scriptor/blob/main/common/src/main/kotlin/com/ssblur/scriptor/word/action/InflameAction.kt), [lectern source](https://github.com/ssblur/scriptor/blob/main/common/src/main/kotlin/com/ssblur/scriptor/blockentity/CastingLecternBlockEntity.kt)) | **Short chain: early ingredients, world-specific spell discovery** | **Proven in source:** Inflame uses normal furnace recipes, consumes input, returns output; lectern periodically casts with **no fuel, FE or player mana**. **Pending:** pinned 2.0.14 in-world input/chest behavior, world spell vocabulary, full costs. |
| Harvest witchcraft byproducts | **Just Another Witchery Remake: Witch's Oven** ([ATM11 pack guide](https://forgebase.johnhalls.com/atm11/mods/just-another-witchery-remake)) | Short witchcraft setup | Can create ash and bottle fumes from specific items; not a replacement for an ordinary ore furnace. Independent from Enchanted: Witchcraft's oven |

**Editorial priority — corrected:** The "Need fast smelting?" top-level tip should **lead with Scriptor Casting Lectern + Inflame** as a potential early unattended zero-fuel smelter, subject to an actual chest/input test; also compare vanilla Blast Furnace, Botania Bellows, Ars Smelt glyph Tier 2 and Forbidden Smelter Prism later. Exoflame belongs to established Botania, not first-day shortcuts. Ars **Storage Lectern** belongs under crafting/storage, **not smelting**.

**Critical release policy:** Source-only and ATM examples are research leads. Confirm actual pinned 1.21.1 registry IDs, recipes, access cost and transformation effects using JEI and disposable Prism gameplay. In particular, don't mistake the Botania original Lexicon for proof of the Revived port, and don't assume another pack's Hephaestus ritual recipe is ours.

**Recipe-tier correction (2026-11-14):** An original Botania Exoflame recipe requires a **Rune of Fire and Rune of Summer** ([recipe listing](https://wiki.9minecraft.net/java/mod/botania/exoflame/)). That requires a Runic Altar and, often, Nether ingredients, so it is a *later goal* rather than a first-day processing tip. The Manatide Bellows is a more plausible earlier furnace helper, subject to the actual Revived recipe. The updated structured inventory reflects this change.

## Verified code audit: Scriptor Magicae Casting Lectern smelting (2026-11-14)

**Correction to prior research: this actually exists and is very valuable.** Our pinned version is `scriptor-neoforge-2.0.14-1.21.1-sgd.jar` (CurseForge file **8899737**). Inspecting the upstream main branch shows these exact mechanics:

- **Item:** `scriptor:casting_lectern`; upstream recipe is **3 wooden slabs, 1 bookshelf and 1 purple wool**. Source: [casting_lectern.json](https://github.com/ssblur/scriptor/blob/main/common/src/main/resources/data/scriptor/recipe/stations/casting_lectern.json). It is made from ordinary starter materials, not an advanced ritual. A suitable **written spellbook** is needed, which can be a bigger access barrier because Scriptor's magical grammar and words are randomized per world; the spellbook binder itself needs leather, purple wool, gold ingot and string.
- **Spell:** the `InflameAction` detects `RecipeType.SMELTING` for a target item stack, shrinks the stack by the recipe ingredient count and deposits the output. There is **no coal, furnace, FE or player mana deduction** in this code. It is processing with input consumption, not ore multiplication. [InflameAction.kt](https://github.com/ssblur/scriptor/blob/main/common/src/main/kotlin/com/ssblur/scriptor/word/action/InflameAction.kt).
- **Automated cadence:** when the lectern is **unpowered** (no redstone input) and holds a parsable written spellbook, it casts each time its cooldown reaches zero. Default max accepted **total spell cost: 25**; after a successful cast, cooldown is **max(10, total spell cost × 10)** ticks (100% default multiplier). A total-cost-3 spell is about **1.5 seconds per cast**, without paying 3 resource units. The spell cost is a complexity/rate restriction, not a spent mana pool. [CastingLecternBlockEntity.kt](https://github.com/ssblur/scriptor/blob/main/common/src/main/kotlin/com/ssblur/scriptor/blockentity/CastingLecternBlockEntity.kt) and [ScriptorConfig.kt](https://github.com/ssblur/scriptor/blob/main/common/src/main/kotlin/com/ssblur/scriptor/config/ScriptorConfig.kt).
- **Important caveat:** the official guide says the lectern uses adjacent chests as a default inventory and that Casting Crystals can redirect the focus. However, upstream main's [LecternTargetable.kt](https://github.com/ssblur/scriptor/blob/main/common/src/main/kotlin/com/ssblur/scriptor/helpers/targetable/LecternTargetable.kt) seems to return the **null backing field** after checking for an adjacent container instead of the container entity. This is a credible code bug / port mismatch, not definitive evidence that the **pinned** 2.0.14 JAR is broken. **Client-test chest access and output handling before promoting 'unattended chest smelting' from code-supported candidate to verified gameplay**. A coordinate-focused crystal may provide a different targeting path.
- The Scriptor Casting Lectern is **distinct** from Ars Nouveau's **Storage Lectern**, which gives inventory crafting and does not itself smelt. Neither should be confused with Ars's Tier-2 Smelt spell.

**Priority Prism check:** In a throwaway world: craft a Casting Lectern, learn or supply a valid low-cost Inflame spell (matching the world's language), place a chest with 8 raw iron beside/below/behind it, verify the lectern can target the chest and convert it into 8 iron ingots without fuel over successive casts. Record input/output counts, book preservation, cast interval, redstone disable behavior and any crash. Retest using a Coordinate-Focused Crystal if the chest doesn't work by default; **do not assume a crystal is free or early**.
