# Ars Elemental 0.7.10.4 — Enchanter and six school bangles

Exact addon SHA1 `562f640916aa6f5c83b3b3ee2384ca828b870e01`; native Ars5.13.5 and embedded Sauce0.0.61.100 [receipt](ars-elemental-pinned-guide-urn.md). Inspected seven bangle classes, ElementalCurio, DamageEvents, registration/documentation, seven recipes/bracelet tag/lang; Sauce ISchoolBangle, AttributeEventHandler/DamageEventHandler, CompatUtils; native SpellStats.Builder and ring recipe. No equipment/attribute/combat acceptance; global Sauce version selection remains open.

## Acquire and equip a specialization

Worn Notebook **Items → Enchanter's Bangle** includes base recipe and all six school subpages; JEI **Enchanting Apparatus** gives exact ingredients. Native Ring of Potential craft: eight iron-nugget-tag items around Source Gem. Base upgrade consumes Ring of Potential reagent + Source-storage-block-tag item + **two Gold Blocks + End Crystal**, Source0, output one `ars_elemental:base_bangle`. This End Crystal resource burden makes it optional advanced equipment, not a trivial starting accessory. End Crystal crafting materials do not imply an Ender Dragon kill gate in this recipe.

Six Apparatus upgrades consume one base_bangle, Source0, output1, keepNbtOfReagent=false. Pedestals:

| Output `ars_elemental:` | Consumed pedestal ingredients | Curios attributes, all ADD_VALUE |
| --- | --- | --- |
|`fire_bangle`|Fire Essence×3 + Fire Charge|Fire power+2; movement+0.035 only when sampled biome baseTemperature>1.8.|
|`water_bangle`|Water Essence×3 + Powder Snow Bucket|Water power+2; NeoForge swim speed+0.5; movement+0.035 when wearer in water/rain.|
|`earth_bangle`|Earth Essence×3 + Cobweb|Earth power+2; knockback resistance+0.3.|
|`air_bangle`|Air Essence×3 + Piston|Air power+2; movement+0.06; attack knockback+1.2.|
|`anima_bangle`|`#sauce:anima_essence`×2 + Ghast Tear + Wither Rose|Necromancy power+2; maxHealth+4 (two hearts of capacity, not immediate healing).|
|`summon_bangle`|Conjuration Essence×2 + Bone + Wilden Horn|Summon power+2.|

All seven appear in packaged `curios:bracelet` item tag. Addon's packaged Curios player definition includes bracelet/bundle; bracelet size2 is supplied by `curios/slots/bracelet.json`. Final merged pack slot count remains an acceptance question. ElementalCurio permits equip-from-use except when stack is the entity's actual offhand stack reference. Worn effects are not supplied merely by carrying in ordinary inventory. Conditional movement bonuses are computed in getAttributeModifiers; exact refresh timing on biome/weather transition is not established by that getter alone. Attribute amounts are flat additions, not labeled percentage multipliers.

## Spell damage: random base versus school attribute

BaseBangle implements ISpellModifierItem: **each applyItemModifiers call** rolls random nextBoolean; success adds damage modifier2 without school filtering. Native SpellStats.Builder gathers all Curios worn items plus entity equipment/hand slots, loops each modifier stack count and invokes the callback when building stats for a spell part. Consequently this is not one guaranteed coin flip per whole cast, and holding the item can participate in native modifier-item collection even without its Curios passives. No personal mana, Source or durability debit in the bangle callback.

School bangles extend ElementalCurio directly, not BaseBangle, so do not inherit that random roll. Their ISchoolBangle deprecated modifier method adds2 only when no registered power attribute exists for that school. These six **do have** Sauce power mappings; normal damage modifier is instead produced by AttributeEventHandler reading caster's effective school-power attribute. It processes each spell part's declared schools and sub-schools (with special Elemental handling), not every part indiscriminately. +2 attribute is not a universal final+2 hit: recipient formulas/school overlap/other listeners determine final damage.

Summon power also feeds both addon SummonEvents and bundled Sauce damage listeners for eligible ISummon owner-player damage, potentially adding the same effective value twice. See [exact duplicate consumers](ars-elemental-pinned-threads.md); Summoner Bangle+2 can therefore yield nominal+4 through those two paths, separately from spell-part modifiers, subject to actual event/source/owner conditions. This is a known code interaction, not measured combat damage or a guaranteed farm multiplier.

## Player-attributed incoming damage specials

Addon DamageEvents.banglesSpecials handles LivingIncomingDamageEvent when DamageSource.getEntity is a **Player** and target differs from player. It skips a target implementing ISummon whose ownerAlt is that exact player. It gathers **worn Curios school bangles** through Sauce getBangles, returning a set of schools; duplicate same-school bangles do not repeat this special loop. It does not require a melee/direct hit, empty hand, matching spell school or successful final health loss. Player-attributed projectile/spell sources can qualify; later cancellation/immunity may leave already-applied side effects. No explicit ally/permission predicate beyond own-summon skip in this handler.

| School special | Exact side effect |
| --- | --- |
|Fire|setRemainingFireTicks(100), overwriting rather than adding100.|
|Water|setTicksFrozen(old+100), no explicit cap here. Not a separately added spell Freezing effect.|
|Earth|Native Snare60ticks.|
|Anima/Necromancy|Coin flip: Wither60ticks; otherwise target.heal1 **and** player.heal1. Healing target is real, not lifesteal, and does not cancel the incoming hit.|
|Conjuration|Find owned ISummon LivingEntities in box around player roughly±30X/Z,±10Y using block centers; Monster/NeutralMob recipients set target to struck entity; all matched receive SpeedII80ticks. Other summon classes may only receive speed. No extra damage addition occurs in this particular branch.|
|Air|No switch special in this handler; passives/attributes above remain.|

Defensive branch: a Player target wearing Earth-school bangle cancels damage types Cactus, Sweet Berry Bush or native Source Berry Bush. Not universal thorn/plant immunity or general Earth damage reduction. Book prose's “enemies hit” is broader-looking than actual unconditional player-attribution trigger; teach the code boundary rather than an ally-safety guarantee.

## Quest / unrun acceptance handoff

Teach optional base acquisition → choose one school and JEI upgrade → equip correct Curios slot → observe attribute/appropriate effect. Name Enchanter's Bangle Notebook subpages, explain costly End Crystal/two Gold Blocks and no mandatory collection of all schools. Possession/equip task is proxy for combat behavior; manual operation acknowledgement unless detector is later implemented. Modest XP and ordinary workshop/building supplies; avoid granting free End Crystals/advanced bangles to bypass the branch.

Unrun positive/boundaries: base per-part random rolls and held/equipped collection; upgraded no base roll; actual school attribute damage, duplicate IDs/slot counts and same-school specials dedupe; hot-biome>1.8 equality/weather refresh; Water moving/rain/swimming; own summon skip versus allies/player-attributed projectile; Fire overwrite/frozen increment/Anima heal; summon owner class/range and duplicate power listeners; Earth three exact damage types versus other thorns; immunity/cancellation before or after handler. No gameplay or configuration changes. Bags/other gear/worldgen/dependency/pilot review remain before addon completion.
