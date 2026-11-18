# Ars Nouveau pinned brewing and status-effect consumers

Exact5.13.5 JAR SHA1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; CFR0.152. Evidence: ModPotions/LibPotions, StartupConfig, all twelve `common/potions` classes, EventHandler/ArsEvents, EntityMixin, AbstractEffect, EffectGlide, existing glyph/ritual/familiar/armor consumers. [Parent](ars-nouveau.md), [potion storage/delivery](ars-nouveau-pinned-potion-devices.md), [glyph/guide routes](ars-nouveau-pinned-guide-glyphs.md), [rituals](ars-nouveau-pinned-rituals.md), [armor/perks](ars-nouveau-pinned-armor-threads.md). Native effect formulas and registrations, not measured final damage, rate or pack compatibility. No client/world acceptance.

## Brew six families and their variants

Use ordinary Brewing Stand and JEI brewing/uses, or supported [Wixie potion automation](ars-nouveau-pinned-wixie.md). Java RegisterBrewingRecipesEvent adds Water+Abjuration Essence -> Awkward, an alternate to vanilla Nether Wart. All six base families take Awkward plus listed ingredient. Redstone upgrades base to long; Glowstone Dust upgrades base to strong. No simultaneous strong+long route is added by these native calls. Spell effects with no potion row below are not automatically independently brewable.

| Family and ingredient | Base ticks / amplifier | Long ticks / amplifier | Strong ticks / amplifier |
| --- | --- | --- | --- |
| mana_regen; Sourceberry Bush item | 3600 /0 | 9600 /0 | 3600 /1 |
| spell_damage; Magebloom | 3600 /0 | 9600 /0 | 3600 /1 |
| recovery; Mendosteen Pod | 3600 /0 | 9600 /0 | 3600 /1 |
| blasting; Bombegranate Pod | 200 /0 | 400 /0 | 140 /1 |
| freezing; Frostaya Pod | 1800 /0 | 3600 /0 | 1800 /1 |
| shielding; Bastion Pod | 3600 /0 | 9600 /0 | 3600 /1 |

Actual potion IDs use `<family>_potion`, `_potion_long`, `_potion_strong` in `ars_nouveau`; these are distinct from mob-effect IDs (family without suffix). All18 registered definitions are accounted. Additional Water+Wilden Wing -> vanilla Leaping, Horn -> Strength, Spike -> **Long Water Breathing**. These ingredient registrations aren't proof one ingredient bypasses brewing fuel/time or every effective recipe remains enabled. Pod cultivation/world acquisition is a separate remaining branch; do not invent a craftable first pod.

Notebook guide uses relevant plant/Sourceberry and potion-device pages; JEI brewing tells actual transforms. This is an optional consumables branch after those ingredients, not a new book-tier requirement. Produce an ordinary bottle and verify its displayed contents before filling an enhanced Flask, Crown/Cannon or Diffuser. A familiar effect or glyph-created status is an alternate application channel, not proof its potion can be crafted.

## Six brewed effects: actual behavior

| Mob effect | Consumer contract |
| --- | --- |
| mana_regen | Attribute MANA_REGEN_BONUS ADD_VALUE from StartupConfig.potionRegen, default10 per potion level; range0..intmax. Attribute enters mana regeneration formula, not universal ten mana per second. |
| spell_damage | Attribute SPELL_DAMAGE_BONUS ADD_VALUE1 (amplifier scaling belongs attribute API). Spell-damage consumer/modifier pipeline is distinct from ordinary sword attack damage. |
| recovery | LivingHealEvent: adds `1+amplifier` to **existing heal event**. Does not periodically create heals itself. If Hex present, handler first halves amount, then adds Recovery bonus. Health cap, event cancellation and other mods still affect actual healed amount. |
| blasting | BlastEffect scheduled when remaining duration==1: explode at entity X,Y+1,Z, radius `2+amplifier`, fire=false, ExplosionInteraction.NONE, source entity null. This disables block-interaction mode in this call; it is not a harmless cosmetic burst or blanket no-entity-damage claim. Early cure/refresh/death can change whether duration1 is reached. No separate expiry-independent detonator is shown. |
| freezing | Each eligible tick sets frozen ticks to min(requiredToFreeze+3, currentFrozen+3+amplifier). Not direct periodic damage in this body; vanilla freezing system and immunity govern damage. |
| shielding | LivingDamageEvent.Pre only for exact DamageTypes.MAGIC, GENERIC or MOB_ATTACK and amount>0.5: subtracts `1+amplifier`, floor0.5. It is **not** all damage nor all magic-tagged sources. Later handler steps can further alter amount. |

Shared entityHurt handler then subtracts WARDING for magic-tagged damage and proportional FEATHER reduction for FALL, final clamps>=0. These layers must not be collapsed into a shielding percentage. Diffuser refreshes full stored durations every300worldticks; long-lasting Blasting/other statuses can be refreshed before their expiration trigger. Delivery choice changes timing, not just target count. Observe expiry before teaching detonation as an automatic immediate result.

## Other thirteen registered status channels

| Effect ID | Source-backed behavior and application scope |
| --- | --- |
| shocked | Tick scans worn armor plus main/offhand for **existence of FE item capability**, counts up to6; if count>0 attempts lightningBolt damage `20*count*(amp+1)`. Does not extract FE or require positive stored energy in this body. Damage immunity frames/events mean attempted tick damage is not measured DPS. EntityHurt handler also adds `3+3*amp` for exact LIGHTNING_BOLT damage while victim has Shocked; not every hit. |
| summoning_sickness | Effect class supplies empty cure-set method. AbstractEffect.canSummon requires real player and absence of effect unless creative; summon glyphs apply it through normal helpers. It is a spell-summoning gate, not a claim all worker charms or other mods' summons are blocked. |
| hex | MANA_REGEN_BONUS multiplier -0.5; halves LivingHealEvent amount. Shared hurt handler additionally adds `0.5+0.33*amp` when victim also poisoned, withered, burning, shocked or frozen>=threshold. It doesn't directly create those qualifying states. |
| looting | DRYGMY attribute ADD_VALUE1; scoped enchant-loot mixin consumers audited in armor companion. Not automatically every loot table or actual Drygmy work bonus. |
| scrying | PublicEffect registration; client ore-highlight scanning and stored scryer metadata are in ritual companion. Separate from camera/Oculus system. A similarly named ScryingEffect class exists but registration constructs PublicEffect. |
| glide | PublicEffect; EffectGlide.canGlide checks status or Gliding perk. Application via glyph adds configured potion duration; gliding/elytra integration is in armor companion, not grant of permanent creative flight. |
| snared | Movement speed ADD_MULTIPLIED_TOTAL -1.0. Does not inherently forbid all external teleport/knockback movement by this attribute alone. |
| flight | NeoForge CREATIVE_FLIGHT ADD_VALUE1. Native Flight ritual refresh/region logic is in ritual companion; effect flag does not prove permanent survival flight or bypass of Gravity. |
| gravity | Each effect tick while airborne: adds Yvelocity-0.5 unless player has non-air block1or2below; nonplayers don't use that ground-proximity exemption. PlayerTick disables flying for airborne noncreative player; Pre damage handler doubles FALL damage. These are separate forces/events, not a dimension-only restriction. |
| immolate | Public class itself has no ticking logic; ArsEvents SpellDamageEvent.Pre adds `2*(amp+1)` when caster has status and spell damage source IS_FIRE. Not every burn or physical fire hit and not an automatic immunity. |
| bounce | Fall event distance>2 with status: crouching sets damageMultiplier0; otherwise zeroes fall damage, resets fallDistance and reverses/scales velocity on nonplayer or client-player path, queues BounceTimedEvent and cancels event. Player fly-into-wall damage is set0 and velocity becomes look*-2. Horizontal factor divides by `0.95-0.1*amp`; don't call arbitrarily amplified effect bounded/safe. |
| magic_find | Server worldTime%60, looks for living entities in block AABB inflated75 whose entity type is tagged MAGIC_FIND; applies Glowing1200ticks. Amplifier isn't used in this body. Doesn't find all structures/ores/rare loot. Effective entity tag contents still matter. |
| soaked | Class applyEffectTick clears fire but doesn't override shouldApplyEffectTickThisTick, so don't infer a periodic extinguish schedule from that body alone. Actual EntityMixin makes isInWaterOrRain true for Soaked or riding BubbleEntity and intercepts positive setRemainingFireTicks to clear/cancel. This is concrete wet/fire behavior, not real water fluid insertion. |

These rows enumerate all19 native registrations without inventing brew recipes for thirteen non-brewed channels. Casting/ritual/thread methods determine their specific duration/cost/target acquisition; use linked companions. Loader/mixin compatibility and interactions with another mod's status handlers remain unverified.

## Quest and boundary handoff

Optional first brewing quest: obtain Sourceberry Bush/Magebloom, brew Awkward through one confirmed route, produce chosen Mana Regeneration/Spell Damage bottle, consume/use through a documented device and observe actual attribute/effect. Then offer pod-derived alternatives as optional choices after pod acquisition is documented. JEI brewing plus specific device Notebook pages are teaching references. Item criterion proves potion possession, not brewing, damage reduction, healing amplification or delayed detonation. Reward XP and mundane bottles/supplies, not full potent-effect sets.

Unrun acceptance: all18base/long/strong potion contents/durations, source ingredient/fuel use, Recovery on a known heal event versus no heal, Shielding exact-source positive versus excluded damage, Blasting expiration versus cure/refresh, Freezing immunity and threshold. Additional channels: Shocked FE-capability-empty versus no-capability gear and actual tick damage; Hex multi-state condition, Gravity near-ground/flying/fall, Soaked positive fire ticks, Bounce amp edge and crouch, magic_find tag membership. Test effect delivery via enhanced Flask/Diffuser against these consumers rather than infer final values from tooltip. No in-world PASS or exploit authorization.
