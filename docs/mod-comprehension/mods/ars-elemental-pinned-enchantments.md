# Ars Elemental 0.7.10.4 — Spell Reflection, shield Reactive and Soulbound

Exact addon SHA1 `562f640916aa6f5c83b3b3ee2384ca828b870e01`, native Ars5.13.5 SHA1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; [receipt](ars-elemental-pinned-guide-urn.md). Exact ShieldEvents, Events drop/clone handlers, enchantment/recipe/tag/advancement JSON, AEDocumentation; native EnchantmentRecipe, EnchantersShield and [crafting/enchants](ars-nouveau-pinned-crafting.md), [Reactive](ars-nouveau-pinned-armor-threads.md). No projectile, shield, Warden, death/recovery or corpse-mod acceptance.

## Acquire the shield and sequential enchantments

Native Worn Notebook **Crafting → Enchanter's Shield**, related **Spell Reflection** entry when registered by native documentation, and JEI **Enchanting Apparatus/enchantment**. Addon appends shield text/relation to native shield entry. Shield itself is native, not a new addon item: vanilla Shield reagent + two gold-storage-block-tag items + two Source-storage-block-tag items, Source0, keepNbtOfReagent=true. Native shield base durability500 with native RepairingPerk inventory repair hook. This research does not rename a vanilla shield into the required class.

Apparatus enchantments apply to the item reagent on apparatus; native EnchantmentRecipe requires existing level exactly one below requested, compatibility and supported item (Books/Enchanted Books have native special allowance), then copies item and sets level. JEI display result does not mean a separate shield is produced for free. Pedestals and Source are consumed; levels are sequential:

| Enchantment level | Pedestals | Source |
| --- | --- | ---: |
|`ars_elemental:mirror_shield` I|native Spell Prism + Manipulation Essence + Abjuration Essence|2000|
|II|Air/Fire/Earth/Water Essence one each + addon Spell Mirror|5000|
|III|native Enchanter's Mirror + Totem of Undying + Source-storage-block tag|8000|
|`ars_elemental:soulbound` I|Anima Essence tag + Totem of Undying + Lapis Block + Source-storage-block tag|10000|

Reflection data supports `#c:tools/shield`, max3, offhand slots, weight5/anvilCost1. **Actual event requires EnchantersShield class**, so data-supported generic shields can receive enchantment without this addon reflection operation. Book's only-Enchanter language describes useful recipient, not the broader JSON supported-items set. [Spell Mirror block's separate dispatch mismatch](ars-elemental-pinned-prisms.md) does not remove this shield event handler; they are different mechanics.

Soulbound supported set is `#ars_elemental:soulbound_extra`, max1/any slot, weight5/anvilCost1. Packaged tag includes writable/written books, both addon bags, optional Curios equipment tags, armor/equippable/weapon/bow/crossbow/durability vanilla enchantable tags, `#c:tools`, optional Ars spellbooks, named native wand/mirror/fishing rod/eye and addon Horn. Optional spellbinder reference does not install that mod. Eligibility is broad and tag-driven, not proof every third-party equipment/drop system participates.

## Spell projectile reflection

ProjectileImpactEvent handler checks **native EntityProjectileSpell** (including Homing subclass), ray hit Player who isBlocking, **offhand EnchantersShield**, and level×0.25≥Math.random. Nominal I/II/III25/50/75% per qualifying impact. It does not explicitly check frontal arc, actual blocked damage, current mana balance or cooldown before reflection. Reverses projectile velocity and addsY0.2, then cancels impact. It does not set new owner or rebuild resolver/caster as defender; damage/permission/discount attribution remains original projectile resolver unless another listener changes it.

For ServerPlayer with ManaCap: trigger `ars_elemental:mirror_shield` advancement, debit **projectile resolver.getResolveCost /(2×level)** from defender mana (fractional amount possible), add shield cooldown20×level ticks. No admission/sufficient-mana test or recheck of actual extraction; no cooldown gate in this listener. Reflection itself occurs before this mana-cap branch, so missing cap is not a rejection in the outer reflection code. Existing cooldown may affect ordinary shield use, but is not per-impact deduplication here.

Homing adds defender to ignore list only if **level>3**, unreachable through packaged normal max3. For normal survival enchantments, retargeting the defender again is not prevented by this special branch; do not promise the reversal permanently redirects a Homing spell to its sender. No custom arrow/trident/all-projectile reflection is implemented by this EntityProjectileSpell condition.

## Sonic Boom and Reactive are separate triggers

Server LivingIncomingDamageEvent Sonic Boom path requires blocking offhand EnchantersShield, chance level×0.25, nonnull source position, native SONIC_BOOM damage type, existing ManaCap and horizontal source-to-player dot(view)<0 (front check). It cancels incoming event **first**. If source entity is LivingEntity, draws beam, requests fixed10 Sonic Boom damage attributed to player, applies knockback scaled by recipient knockback resistance, debits150mana, cooldown20×level and stops item use. Hurt result is not checked before debit/push. If source is not living, incoming remains canceled but helper returns before return-damage/payment/cooldown. It is not a universal beam reflector or refund system.

Separate server LivingShieldBlockEvent hook: offhand EnchantersShield with REACTIVE_CASTER component, chance Reactive enchantment level×0.25 and reactive spell.isValid → castSpell(world,player,OFF_HAND,null). This does not require Mirror enchantment, and it does not fire from mere shield possession. Obtain/inscribe native Reactive using [exact parent recipe/casting flow](ars-nouveau-pinned-armor-threads.md#reactive-enchantment-activation); ordinary personal-mana/resolve admission remains in ReactiveCasterData, not a free bonus spell. Other native shield/Reactive listeners may also run; no merged runtime event-count claim.

## Soulbound drop collection and return order

Addon highest-priority LivingDropsEvent handler for Player returns when enchantment missing, FakePlayer or keepInventory=true. For ItemEntities **actually in event drop collection**, select nonempty stacks with Soulbound level>0, remove them from drops, serialize complete ItemStacks into player persistent **PlayerPersisted/tagSoulbound** count/index records. This is drop-event interception, not an item death-prevention component or ability to search arbitrary corpse inventories. Competing grave/drop handlers and Curios emission order require acceptance; do not promise items that never enter this event collection are recovered.

Low-priority PlayerEvent.Clone death handler reads new player's PlayerPersisted data, parses saved stacks, then returns each: ArmorItem goes into matching armor slot if empty; ShieldItem into offhand if empty; everything else via NeoForge giveItemToPlayer. Original hotbar, main-inventory and Curios slot positions are not serialized/restored by this routine. Complete item components include bag contents when serialized, but end-to-end resurrection still requires testing. Record is removed after recovery loop, no persistent extra copy intended by this code. Recovery uses helpers whose actual inventory-full fallback/other mod behavior must be observed; no lossless global guarantee.

Same clone handler restores saved admin Hymn lock fields from PlayerPersisted, then removes them; Soulbound item protection is not permission to clear a casting lock. The addon does not implement an XP refund or Source charge for return. Multiple-death/clone/corpse priorities remain explicit unrun boundaries.

## Quest / unrun acceptance handoff

Teach optional native shield→ReflectionI→actual block, with II/III resource steps as upgrades; name Notebook/JEI and offhand Enchanter class requirement. Distinguish projectile event, Sonic Boom and Reactive proc. Reflection advancement is actual branch feedback when server/ManaCap path triggers, not proof of mana affordability or a returned damaging hit. Soulbound is optional Totem/10000Source protection: apply to owned test item and verify controlled later death, not require reckless death with valuable items. Possession is proxy; manual operation acknowledgement where no real detector exists. Modest XP/building supplies, no free Totems/advanced enchantments.

Unrun cases: rank0/1/2/3 chances; generic shield versus Enchanter/offhand/mainhand; frontal/rear projectile, arrow versus native spell; no mana/cooldown active; Homing re-acquisition/owner attribution; Sonic front/back/nonliving-source/canceled recipient hurt; Reactive component/chance/insufficient mana and event duplication; sequential enchant failure/preserved item data; Soulbound ordinary/armor/shield/bag/Curios, keepInventory/FakePlayer, corpse conflict/full inventory and repeated clone/death. No runtime changes. Other gear/worldgen/dependency/pilot review keep addon researching.
