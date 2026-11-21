# Ars Elemental 0.7.10.4 — Charm, Life Link and Phantom Grasp

Exact artifact/hash [receipt](ars-elemental-pinned-guide-urn.md). Direct three glyphs, EnthrallEffect/LifeLinkEffect and recipes; bundled Sauce EntityCarryMEI/EnthrallUtil/GenericEventHandler/goal and alliance mixins. Dependency-version scope in [focus receipt](ars-elemental-pinned-foci.md). No taming, AI, healing or multiplayer tests. **38of39 glyph recipes audited**; Bubble Shield remains, plus other major addon routes.

## Learn the three optional branches

Worn Notebook **Charm / Life Link / Phantom Grasp**, learned through [Scribes Table](ars-nouveau-pinned-guide-glyphs.md). Recipe inputs consumed, default mana before other parts/modifiers:

| Recipe/output `ars_elemental:glyph_…` | Tier / XP / default mana | Inputs |
| --- | --- | --- |
|charm|2 /55 /30|`sauce:anima_essence`, Golden Carrot, Source Berry Pie, Cake|
|life_link|3 /160 /30|Lead, `sauce:anima_essence`, Sculk Sensor|
|phantom_grasp|2 /55 /50|two Phantom Membranes, `sauce:anima_essence`|

Choose legal cast method and target. Charm attempts temporary enemy allegiance or ordinary animal taming/love; Link shares selected healing/damage direction; Phantom heals inverted-healing entities but harms others. These are not interchangeable “life steal” spells or guaranteed permanent followers.

## Charm target gates and probability

Requires Player shooter, ServerLevel and target not in **`ars_elemental:charm_blacklist`** (packaged empty contribution, other datapacks may extend). Requires Mob maximum health **strictly less than configurable150**, except hard-coded developer UUID bypasses health limit. Hostile **Enemy**, or NeutralMob that isn't an Animal able to fall in love, uses hostile branch:

- `R=(int)(10+100×currentHealth/maxHealth)`.
- `B=10+5×ampMultiplier`; add50 if undead-tagged and shooter passes **worn Necrotic Focus** test.
- Succeed when `random.nextInt(0,R)+B >= R`.

For normal positive R, lower health raises chance; integer R and fractional boost matter, so this is not a flat percent independent of target health. Success applies **Enthralled** with owner-carrying EntityCarryMEI and heart particles. Native addEffect can still reject or interact with existing effect.

Other Animal branch: untamed TamableAnimal→roll R100/B25×(1+amp), then tame(player); untamed AbstractHorse→same roll then **setTamed(true)** (does not explicitly assign player owner here); untrusted Fox→same roll then add trusted UUID; otherwise animal.canFallInLove→R90/same boost then setInLove(player). Failed early animal branch does not fall through to every later option. Compatible native potion augments Amplify/Extend Time/Duration Down; configured base30seconds, extension8seconds/unit for temporary Enthralled. Taming/trust/love calls differ from that timed hostile status.

## Actual Enthralled recipient and cleanup flaw

EnthrallEffect.onEffectAdded requires EntityCarryMEI, writes persistent **`sauce:thrall` owner UUID**, marks Mob persistence-required through bundled EnthrallUtil. Effect duration1 removes that key. Empty fillEffectCures means no default cure set from this override. **Its subscribed MobEffectEvent.Remove callback has no filter for removed effect** and unconditionally removes `sauce:thrall` from event entity. Removal of another status can therefore clear allegiance early; no matching reset of persistence-required appears here.

ArsElemental constructor sets **Sauce.ENABLE_ENTHRALL=true** (also enables spell crit), overriding Sauce's production-default false. With this flag, server EntityJoinLevel for PathfinderMob using ground/flying navigation installs FollowOwnerGoal and ThrallTargetGoal. Follow looks up master in current ServerLevel, needs no combat target and follows via navigation at speed1.5; no cross-dimension teleport promise. Target redirection picks master's recent attacker/victim or thrall attacker subject to utility predicates; LivingEntity.canAttack mixin further restricts Player-owned thrall targets to master's recent hurt/hurt-by entities and excludes allies. Not every Mob/navigation receives the follow goal.

Entity.isAlliedTo mixin replaces results for enthralled LivingEntity: master or same-master thrall are allied; other original allies need not retain their prior result. It also returns **false for a LivingEntity with Sauce Rage**. This closes a recipient left open in [Rage audit](ars-elemental-pinned-venom-spores-rage.md): exact enabled mixin **does remove that entity's alliance test**, though ordinary target rules/AI/other mods still constrain actual attacks. Thrall target event listener and two target goals are separate callsites; no claim every hostile follows reliably or saves custom effect references correctly.

## Life Link direction and actual sharing

Living target must differ from shooter. Normal cast sets **owner=shooter, target=hit entity**; **Sensitive reverses** them. Apply same owner/target-carrying Life Link to both ends, duration base30seconds +8seconds×duration multiplier via native potion formula, amplifier0. Compatible Extend Time/Duration Down/Sensitive. If resolver.hasFocus(Necrotic Focus), uses **forceAddEffect**; otherwise normal addEffect. No initial ally/damage-permission check is in this application method.

LifeLinkEffect constructor registers listeners; no default cures from override:

- When **target end heals**, if owner alive, owner.heal(**half event amount**) and set target event amount to half. Owner-side nested heal ordinarily fails target-identity condition of this same pair, but multiple/replaced links and external listeners remain acceptance boundaries.
- When **owner end takes LivingDamageEvent.Pre**, except BYPASSES_INVULNERABILITY source, call target.hurt(**same source**,half new damage), then set owner's new damage to half. It **does not check target.hurt return**: rejected transferred damage still leaves owner half in this handler. This is redistribution, not a guarantee damage is conserved through immunity/cancellation.
- If recipient missing/dead, remove effect from event entity. No proactive per-tick distance check or both-end teardown here.

Native Cut with addon GlyphEvents enabled removes effects from both stored endpoints; [empowering audit](ars-elemental-pinned-empowering.md). EntityCarryMEI keeps **direct LivingEntity references**, updates/removes old owner's/target's effect when references change, and supplies no custom serialized-reference implementation in this inspected class. Don't promise links persist correctly through unload/relog/dimension transfer. Frozen/heal modifiers and other listeners can alter shared amounts.

## Phantom Grasp: inverted heal versus damage/exhaustion

Server LivingEntity target must be alive/not removed. Amount **`3+3×ampMultiplier`** by config. If `isInvertedHealAndHarm`, add native Randomize roll if present then **heal target** directly. That branch bypasses native damage/alliance predicate; it is healing a compatible target, not transferring health to caster.

Otherwise attempt **MAGIC** damage through native IDamageEffect, which excludes allies and inverted-healing targets. If successful **and target is Player**, request food exhaustion **`2.5×(1.5+ampMultiplier)`**. It does not set hunger or saturation to a fixed number and does not exhaust every Mob. Default mana50, Necromancy school; compatible Amplify/Dampen/Fortune/Randomize, own Amplify limit3. [Necrotic Focus](ars-elemental-pinned-foci.md) can add modifiers, but no independent owner-heal transaction is in this glyph.

## Acceptance and remaining coverage

Future explicit acceptance: actual blacklist/health boundary/rolls; animal ownership and temporary versus tame state; early thrall-key cleanup on unrelated effect removal, master AI/alliance/save behavior; Rage recipient; Life Link rejection/force application/Sensitive, damage rejection/heal modifiers/multiple links and Cut teardown; Phantom target class, magic immunity and exhaustion. No permission bypass, balance fix or gameplay PASS claimed. **Bubble Shield** is final unaudited glyph recipe;12tomes and major helper/device/gear/worldgen branches plus final walkthrough review keep addon researching.
