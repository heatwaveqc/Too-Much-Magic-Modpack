# Ars Elemental 0.7.10.4 — focus-selected temporary mounts

Exact Elemental hash/artifact scope: [guide receipt](ars-elemental-pinned-guide-urn.md). Direct SummonEvents.summonedEvent, SummonDolphin/Strider/Camel/SkeleHorse and ModEntities. Parent exact Ars5.13.5 EffectSummonSteed, SummonHorse and ISummon freshly decompiled externally to `elemental-parent-fresh`; existing native AbstractEffect.summonLivingEntity inspected for event order. No client/riding tests. Undead combat summons, Homing replay and remaining addon glyphs are still unfinished.

## Learn, specialize and summon

1. Open Worn Notebook → **Spell Casting → Summon Steed** and the selected elemental/Necrotic Focus page. Learn native `ars_nouveau:glyph_summon_steed` using the Scribes Table: **four Leather**, recipe XP27, Tier1 glyph. [Native glyph acquisition](ars-nouveau-pinned-guide-glyphs.md); [focus recipes/equip](ars-elemental-pinned-foci.md).
2. Equip one school focus and remove other held/worn school providers for a predictable choice. Build a spell with an appropriate cast method and Summon Steed. Native effect mana cost defaults100 before casting-method and other modifiers; no additional addon mount Source fee appears in this replacement event.
3. Native spell first creates a saddled owned horse at the hit location. Default duration is **300 seconds +120 seconds×durationMultiplier**, converted to ticks; nonpositive result falls back to20 ticks. Number is `1+round(aoeMultiplier)` via the loop bound; invalid/nonpositive counts can yield no horses. Native canSummon gate applies and it applies **600 ticks Summoning Sickness** after the loop. The [Summoning thread's advertised reduction is broken](ars-elemental-pinned-threads.md).
4. The addon HIGH-priority SummonEvent handler checks server world and selected focus schools, then substitutes the mount below when the summon is a native SummonHorse and shooter is **ServerPlayer**. Collect the desired specialized mount by interacting, rather than expecting a separate charm recipe.

| Selected focus school | Replacement entity ID | Key implementation difference |
| --- | --- | --- |
|Water|`ars_elemental:summon_dolphin`|Controllable dolphin; refills controlling Player rider's air in customServerAiStep|
|Fire|`ars_elemental:summon_strider`|Saddled strider with direct rider movement controls|
|Earth|`ars_elemental:summon_camel`|Tamed/saddled Camel subclass; retains native camel movement rather than custom dolphin/strider controls|
|Necromancy|`ars_elemental:summon_skelehorse`|SummonHorse subclass implementing IUndeadSummon; underwater dismount disabled and slowdown0.96|
|Air / no eligible school|native horse|No Air replacement branch in this method|

The focus collection is a Set, and the loop stops after the first eligible replacement. It has no explicit priority table when multiple eligible schools are present. Do not prescribe a deterministic Water-before-Fire rule from source case order.

## Replacement and lifetime boundaries

All four constructors copy the old horse's **remaining ticks**, summoner UUID, integer **block position** and beneficial active effects. They do not copy the entire horse inventory, attributes or arbitrary NBT. Camel/SkeleHorse tame and insert a new saddle; Strider equips one; Dolphin needs none. Old horse is discarded, event.summon is replaced, new entity is added and vanilla SUMMONED_ENTITY criterion triggered. If new owner UUID is null, replacement exits early.

Native AbstractEffect adds the original horse to the world **before** posting SummonEvent. If the native Summoning Focus applies, it also resolves its child spell onto the original horse before the addon replacement. Only beneficial effects copied by these constructors necessarily survive; do not assume arbitrary child-spell changes transfer. Necromancy's StrengthII and SpeedII500-tick buffs are added to the original summon earlier in this addon listener, then copied by the replacement constructor.

Dolphin/Strider/Camel implement their own server tick: decrement ticksLeft each tick; at≤0 poof, discard and post `onSummonDeath(world,null,true)`. Actual death invokes parent die then `onSummonDeath(world,cause,false)`. SkeleHorse inherits these timer/death/dispel paths from native SummonHorse. None of these constructor/tick paths charges recurring Source or refreshes lifetime simply because the player keeps wearing a focus.

**All four entity registrations use `noSave()`** in ModEntities.addEntity. NBT helper methods exist for remaining time and owner, but their presence does not promise normal chunk/world-save persistence. Fire Strider, Earth Camel and SkeleHorse types also register `fireImmune`; Dolphin does not. This is an entity-type property, not a rider's immunity guarantee. Treat these as temporary travel tools and plan safe dismount before expiration/unload; exact unload behavior still needs explicit acceptance.

## Riding details the guide does not fully specify

**Dolphin:** server interaction with **empty main hand**, not sneaking, attempts startRiding and reports SUCCESS; method does not check interacting player's ownership. First living passenger controls it. It turns to rider yaw/half pitch, halves strafe and doubles forward input (nonpositive forward further×0.25). In water its controlled speed is movement-speed attribute+2.5, with pitch-dependent vertical input; water slowdown0.975. It retains BreathAir/TryFindWater/DolphinJump/FollowBoat goals. Rider air refill does not mean the Dolphin itself never needs air.

Its jump interface permits jumping in water or moistness>2300. handleStartJump ignores power<50; otherwise add directional vector with vertical−1.5 if rider pitch>20, else+1.5, then scale by `0.7×power/10`. onPlayerJump/handleStopJump are empty. Do not convert these source factors into measured blocks/second or assume every client jump-control sequence invokes the same path.

**Strider:** empty-main-hand/non-sneaking server interaction likewise mounts without an owner gate in this override; otherwise PASS. First living passenger controls yaw/half pitch, half strafe and quarter reverse input. Travel directly consumes rider movement, not a warped-fungus-on-a-stick check in this method. It rejects food, mating, breeding, love and offspring and suppresses equipment drops; zero base XP. **Camel:** rejects food/breeding/mating/item-taking, suppresses equipment drops, zero base XP and blocks custom inventory screen; ordinary inherited Camel mounting still applies, not a custom owner-only permission promise.

**SkeleHorse:** changes sounds, disables dismount-underwater, slowdown0.96 and inherits no breeding/food/equipment-drop/XP/custom-inventory behavior from native SummonHorse. It still extends **Horse**, not vanilla SkeletonHorse, and this class has no explicit underwater-breathing override. The Notebook's underwater-breathing claim therefore needs recipient/entity-tag verification or runtime acceptance; sounds and appearance alone are insufficient proof. Its IUndeadSummon marker also affects generic Necrotic consumers, which remain in the combat-summon audit.

## Acceptance and coverage

Future explicit acceptance: spell recipe/UI/actual config duration and mana; one school versus competing schools; original Summoning Focus child effects before replacement; riding by owner/another player; Dolphin pitch/jump/rider versus mount air; Strider controls/rider hazards; Camel inherited mounting; SkeleHorse actual breathing; noSave/unload/expiration/death/dispel. No ownership, protection, persistence or gameplay PASS is claimed. Pilot35–37 only detects focus/Urn/hat possession and does not teach these travel branches. The addon remains researching for its other systems.
