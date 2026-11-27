# Ars Nouveau pinned spell records, loot Tomes and Annotated Codex

Exact5.13.5 JAR SHA1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`. Evidence: BlankParchmentItem, SpellParchment, ICasterTool, CasterTome/TomeCasterData (inner resolver included), CasterTomeData, CasterTomeRegistry, AnnotatedCodex/CodexData, ScribesTile XP helpers, DungeonLootTables/DungeonLootEnhancerModifier, ManaUtil/LivingCaster/ManaCap, Documentation and Config/ServerConfig; native recipes/global loot modifier/locale. CFR0.152. [Parent](ars-nouveau.md), [glyph learning](ars-nouveau-pinned-guide-glyphs.md), [casting/mana](ars-nouveau-pinned-casting.md), [camera parchment distinction](ars-nouveau-pinned-scrying.md). No client/UI/multiplayer/loot test.

## Spell Parchment records a composition

Notebook **Crafting -> Scribe's Table -> Scribing** describes tool inscription; JEI Blank Parchment recipe and uses. Paper surrounded by eight Magebloom Fiber makes one Blank Parchment. Put Blank Parchment on Scribes Table and scribe selected spell from held Spell Book/Spell Parchment by selecting that spell in the book, then **sneak-use the book on the table**, as native Scribing page instructs. BlankParchmentItem.onScribe creates SpellParchment, delegates ICasterTool.onScribe and replaces table stack only on success. ICasterTool copies name/flavor/particle timeline from held caster, validates spell, then saves composition. It accepts held Book or Parchment (Manipulation Essence is a separate hide-recipe operation). No glyph ingredient/XP learning recipe is invoked by this data-copy handler.

Spell Parchment is a reusable spell **record/source for inscription**, not a consumable glyph unlock or portable Tome. Inspected SpellParchment class has no direct `use` cast override; its `useOn` handles copying a particle timeline from ITimelined block entity instead. Do not teach right-click parchment as ordinary spell casting. Copying timeline makes one modified copy preserving its spell, shrinks held stack once except infinite-material, then adds result or drops when inventory full. IWololoable target color is copied, otherwise default particle color. Repeated copy is not proof of resource output or spell effect.

`wipe_spell_parchment` shapeless returns fresh Blank Parchment; `dye_spell_parchment` accepts dye-tag item plus SpellParchment through native dye recipe type. Reset loses composition; dye/timeline are cosmetic record channels, not new glyphs. Normal learned glyphs and casting tier remain separate from possessing a written recipe.

## Loot Caster Tomes: fixed spells and personal mana

CasterTome native survival inscription is **blocked**: onScribe returns player.isCreative AND ICasterTool action. Discover preconfigured Tomes through loot rather than promising a blank Tome editing progression. Fifty native `ars_nouveau:caster_tome` recipe-data records define spells. Their `matches` returns false: these are registry/data definitions, not fifty ordinary crafting recipes. Reload fills CasterTomeRegistry and the DungeonLootTables.CASTER_TOMES supplier list; missing/air tome_type falls back to registered Caster Tome item. SpellData, when present, takes precedence over legacy spell fields. Names/flavor may be localized at item creation.

Native global modifier `ars_nouveau:dungeon_loot` is added by `data/neoforge/loot_modifiers/global_loot_modifiers.json`. Its conditions cover vanilla chest tables: simple_dungeon, jungle_temple, abandoned_mineshaft, bastion_treasure, desert_pyramid, end_city_treasure, ruined_portal, pillager_outpost, nether_bridge, stronghold_corridor/crossing/library, woodland_mansion, underwater_ruin_big/small. Config `spawnTomes` defaults true. On each eligible modifier call, Tome loop has one rare roll at0.1, separate from rare-item loop; successful roll selects uniformly from loaded Tome suppliers. Those defaults do not guarantee a Tome per structure/chest or establish effective pack loot contents. Native Alakarkinos Token also sits in rare-item pool, an alternate acquisition candidate via these modifier tables; it doesn't replace the independently established wild Sherd exchange.

Use a configured Tome to request its spell on server through TomeCasterData. CasterTome implements mana discount = integer `spell.getCost()/2`; ManaUtil includes the casting item discount alongside worn/armor discounts. Native tooltip says half cost or user's entire bar, whichever smaller. Actual specialized enoughMana allows discounted resolve cost <= current mana **OR current mana equals max mana** (or creative), with mana capability required. Consequently a too-expensive spell may pass when completely full, but fail one point below full if cost still exceeds current. Final expenditure goes through ordinary resolver cost/event calculations and ManaCap.removeMana/setMana clamps negative results to zero. Do not turn the tooltip into unconditional free casts, a lesser-than-full threshold, fixed50% under all modifiers, or proof of learned glyphs/tier promotion. Composition limits for constructing personal spells and existing loot Tome recipes are distinct; native Tome records include spells longer than a basic ten-link composition.

## Annotated Codex shares learned glyph knowledge

Notebook **Spell Casting -> Annotated Codex** and JEI `annotated_codex` recipe: Blank Parchment + Leather, shapeless, one Codex. This is native base-mod `ars_nouveau:annotated_codex`, separate from addon Codex systems. It holds recorded UUID/playerName/list of glyph IDs; considered recorded only with UUID and nonempty playerName.

- First server use records user's entire known glyph list if enough **total XP points**. Cost is not simply ten points times count: native `codexCost` default10 (range0..intmax) is multiplied by count, passed to ScribesTile.getLevelsFromExp (integer conversion), then back through getExperienceForLevel. Actual debit uses giveExperiencePoints(-computedPoints). For one glyph at default,10 nominal points becomes level1 ->7 actual; for five,50 ->level4 ->40. Displayed levelCost is a reference, not removal of that many current experience levels.
- Recording preserves Codex stack, writes component, sends recorded message and debits computed XP. Insufficient total XP leaves unrecorded data unchanged. No ingredient glyphs are consumed and no Source required here.
- Same recorded UUID use computes known glyphs missing from stored list. Only that difference determines cost, then replaces saved list with current known set on success. Empty difference reports up-to-date, no debit. This is author update, not author consuming their own Codex to relearn it.
- Another player's use unlocks every stored glyph absent from their knowledge; no recipient XP charge or individual glyph item cost appears in this branch. Updates mana glyph bonus to at least known count and synchronizes. If any new glyph unlocked, shrinks one Codex unless infinite-material. If no new glyph, reports no-use and preserves Codex. It does not upgrade recipient Spell Book tier; knowledge and usable glyph tier are separate.

Transfer is therefore a real alternate glyph-acquisition branch with author XP and consumed recipient copy, not just recipe prose. Effective pack restrictions, addon glyph IDs removed between saves and multiplayer ownership changes need acceptance. Codex snapshot does not automatically stream future author research to prior recipients. Propose quests that explain consensual exchange; do not award a full-knowledge Codex as a shortcut around intended glyph learning.

## Fifty native fixed compositions

Resource base `data/ars_nouveau/recipe/tomes/`. Glyph names below omit the common `ars_nouveau:glyph_` prefix; repeated parts remain repeated and order matters. This records exact definitions, not safety recommendations to cast explosive/transport spells in a base.

| Recipe basename | Ordered glyph sequence |
| --- | --- |
| alex_tome | self -> burst -> sensitive -> dampen -> aoe -> aoe -> aoe -> aoe -> aoe -> phantom_block -> amplify -> pierce |
| arachne_tome | projectile -> split -> split -> snare -> extend_time -> extend_time |
| aurellia_tome | projectile -> linger -> sensitive -> fangs -> lightning -> amplify -> amplify -> extend_time -> extend_time -> extend_time |
| bailey_tome | projectile -> launch -> amplify -> amplify -> delay -> explosion -> amplify |
| beepsterr_tome | projectile -> orbit -> extend_time -> sensitive -> split -> grow -> aoe -> aoe |
| bugcolez_tome | self -> summon_vex -> extend_time -> extend_time -> extend_time -> extend_time -> extend_time -> extend_time -> extend_time -> extend_time |
| chems_tome | projectile -> linger -> sensitive -> rune -> launch -> launch |
| chosenarchitect_tome | projectile -> accelerate -> accelerate -> accelerate -> accelerate -> accelerate -> exchange -> amplify -> randomize -> pickup |
| cocoaeyebrows_tome | touch -> break -> dampen -> dampen -> aoe -> aoe -> pierce -> pierce -> pierce -> pierce |
| crowdrone_tome | self -> sense_magic -> extend_time -> light -> extend_time -> extend_time -> extend_time -> extend_time -> extend_time -> extend_time |
| darkfira_tome | projectile -> burst -> sensitive -> aoe -> aoe -> aoe -> conjure_water -> freeze -> lightning -> amplify |
| farfalla_tome | projectile -> ignite -> delay -> conjure_water -> freeze |
| fireball_tome | projectile -> ignite -> explosion -> amplify -> amplify -> aoe -> aoe |
| frantastic_tome | projectile -> summon_decoy -> orbit -> summon_decoy -> orbit -> linger -> orbit -> summon_decoy -> lightning |
| glow_tome | touch -> rune -> snare -> extend_time -> light |
| gootastic_tome | projectile -> launch -> amplify -> amplify -> delay -> pull -> amplify -> amplify |
| ivy_tome | touch -> animate_block -> extend_time -> extend_time -> extend_time -> extend_time -> extend_time -> extend_time -> extend_time -> name |
| kamiihoney_tome | projectile -> harm -> amplify -> amplify -> wither -> bubble -> extend_time -> extend_time |
| kirin_tome | self -> summon_decoy -> blink -> amplify |
| lootz_tome | projectile -> harm -> amplify -> launch -> amplify -> amplify -> firework -> extend_time -> explosion |
| lufia_tome | projectile -> sensitive -> pierce -> pierce -> pierce -> intangible -> aoe -> aoe -> aoe -> aoe |
| lyrellion_tome | projectile -> accelerate -> wall -> sensitive -> phantom_block -> pierce -> amplify |
| mystifi_tome | underfoot -> linger -> sensitive -> extend_time -> extend_time -> intangible -> pierce -> pierce -> extend_time -> extend_time |
| nikk_tome | projectile -> hex -> harm -> blink -> amplify -> pickup -> aoe |
| oaksa_tome | projectile -> burst -> sensitive -> aoe -> aoe -> aoe -> aoe -> aoe -> aoe -> aoe -> break -> extract -> pickup |
| othy_tome | self -> extend_time -> summon_decoy -> blink -> amplify -> amplify |
| pandoxyy_tome | self -> orbit -> wind_burst -> orbit -> split -> split -> orbit -> rotate -> sensitive -> exchange |
| plauged757_tome | self -> explosion -> amplify -> amplify -> aoe -> aoe -> aoe -> aoe -> aoe -> aoe |
| poseidon_tome | projectile -> sensitive -> light -> burst -> aoe -> aoe -> sensitive -> freeze -> break -> freeze |
| pranks_tome | projectile -> linger -> launch -> amplify -> delay -> duration_down -> firework -> amplify -> summon_vex -> wind_shear |
| renew_rune_tome | touch -> rune -> dispel -> heal -> amplify |
| riftderp_tome | projectile -> accelerate -> accelerate -> harm -> blink |
| sebuss_tome | projectile -> smelt -> aoe -> sensitive -> ignite |
| shadow_tome | touch -> intangible -> aoe -> aoe -> pierce -> pierce -> pierce -> pierce -> pierce -> extend_time |
| shoob_tome | self -> explosion -> aoe -> aoe -> summon_decoy -> blink -> amplify -> amplify -> invisibility -> extend_time |
| silvanus_tome | touch -> light -> extend_time -> extend_time -> extend_time |
| sloppybox_tome | touch -> rune -> smelt -> pierce -> pierce -> pierce -> pierce |
| spinoftw_tome | self -> conjure_water -> heal -> amplify -> amplify -> phantom_block -> aoe -> aoe -> aoe |
| takeoff_tome | self -> launch -> launch -> glide -> duration_down |
| toxin_tome | projectile -> hex -> harm -> extend_time |
| treeleafs_tome | self -> burst -> sensitive -> aoe -> aoe -> aoe -> aoe -> grow -> harvest -> pickup |
| uni_tome | self -> orbit -> gust -> amplify -> amplify -> amplify -> amplify -> amplify -> amplify -> amplify -> amplify |
| urticantodin_tome | self -> leap -> amplify -> amplify -> amplify -> bounce -> duration_down -> amplify |
| vault_tome | self -> launch -> delay -> leap -> slowfall |
| warp_impact_tome | projectile -> blink -> explosion -> aoe |
| worsecookie_tome | touch -> rune -> hex -> amplify -> amplify -> amplify -> amplify -> wither -> amplify -> amplify |
| xacris_2_tome | projectile -> wall -> sensitive -> aoe -> firework -> extend_time -> extend_time -> extend_time -> extend_time -> amplify |
| xacris_tome | underfoot -> phantom_block -> aoe -> aoe -> aoe -> pierce -> pierce -> pierce |
| yeet_tome | self -> orbit -> launch -> amplify -> amplify -> delay -> gust -> amplify -> amplify |
| yuukiukami_tome | projectile -> break -> amplify -> extract -> pickup |

## Quest and acceptance handoff

Optional knowledge stage: write a harmless chosen Parchment -> use it to inscribe a compatible tool; obtain/use a loot Tome; record/update Codex -> another consenting player learns one missing glyph. Scribe's Table/Scribing and Spell Casting/Annotated Codex are the guide references; loot route and Tome full-bar edge must be stated explicitly because a recipe viewer data entry is not a craft instruction. FTB item tasks detect only records/Tomes/Codex possession; inscription, casting, XP cost and knowledge transfer need manual observation or actual detectors. Reward XP and modest parchment supplies, not a prefilled knowledge library.

Unrun boundaries: failed versus valid inscription, timeline copying with full inventory, wipe and dye data retention; survival Tome re-inscription rejection, full mana versus one-below-max when spell cost exceeds bar, actual mana debit after successful/canceled casts; Codex XP threshold rounding, same-author update difference, other-player newly missing glyph vs none, recipient item consumption and unchanged book tier. Verify effective loot modifier tables/config and selected Tome IDs in a disposable world before asserting availability or any safe farm/transport effect. No operation acceptance or automatic quest detection is claimed.
