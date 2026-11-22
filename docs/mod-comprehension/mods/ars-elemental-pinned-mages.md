# Ars Elemental 0.7.10.4 — four wandering Mage encounters

Exact addon SHA1 `562f640916aa6f5c83b3b3ee2384ca828b870e01`, native Ars5.13.5 SHA1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; [receipt](ars-elemental-pinned-guide-urn.md). Exact EntityMageBase including inners/MageResolver, four school subclasses, MageProjCastingGoal/ProjCastingGoal/CastGoal/SelfCastGoal/HealSelfCastGoal, ModEntities/EntityAttributes/config and five-biome resources; native EntitySpellResolver/SpellResolver/CapabilityRegistry, Sauce ISchoolFocus inspected. javap confirms armor switch's actual item selection; CFR's apparent uninitialized local is reconstruction artifact. No spawn/combat/loot/client acceptance.

## Find and understand the encounter

`ars_elemental:air_mage`, `fire_mage`, `water_mage`, `earth_mage` are Monster-category entities, width0.7/height1.9, tracked range16chunks; they are not tamable helper charms or a new boss/tier ritual. [Biome audit](ars-elemental-pinned-biomes.md) records matching forest Mage entries and all four in Vexing Caves, each weight4/group1–3. ON_GROUND placement requires block below in dirt tag, **Y>45 and block light≤6**. Biome spawn entry alone does not make deep low-Y Vexing cave spawning pass this predicate, and block light is not the same as daytime sky light. No guaranteed biome population or private spawner route is established.

Worn Notebook Archwood/elemental gear/glyph pages and JEI explain encountered materials/spells; AEDocumentation does not register a dedicated four-Mage encounter walkthrough in the inspected body. A future optional expedition quest must state their hostility/focus/combat behavior directly; do not invent a hidden manual item or mandatory kill gate. Main survival equipment/glyph recipes remain available through their established workshops.

Base attributes:150maxHealth,1attackDamage, movement0.25, followRange48; base XP reward15, max spawn cluster3. Default `magesAggro=true` adds proactive player targeting unless Sauce getFociSchools(player) contains Mage school. That helper considers held focuses and first matching Curios focus, or ISchoolProvider schools directly. This is broader than book/config comment's “wear”: held matching focus can qualify. Mage isAlliedTo also recognizes a living entity's matching focus-school set. Different-school Mages are highest-priority target choice; retaliation/HurtBy target is next, player targeting next, other non-Mage Monsters lower priority. Same-school focus is not blanket damage immunity, proof hostile target is forgotten instantly, or permission to attack safely.

## Equipment and resolver

Spawn setup always selects **medium** matching Elemental armor, not random light/medium/heavy despite unused type array. Armor-slot switch selects actual hat/chest/legs/boots (javap verified), nonarmor slots empty, then offhand native Apprentice Spellbook and mainhand Enchanter's Sword with **Touch→Knockback** spell. Subclasses tint hand stacks yellow/red/cyan/green; that is appearance, not a learned player spellbook unlock. Registered EntityAttributes also includes all four in CASTER_ENTITIES. [Armor effects/capacity](ars-elemental-pinned-armor.md) remain their own conditional consumers.

Ranged selection uniformly chooses one entry from pSpells. **Every Mage retains base constructor Projectile→Harm→Amplify** before subclass additions; do not omit this common attack or describe each school's pool as exclusive. MageResolver extends native EntitySpellResolver: enoughMana=true, normal onCast grammar/cancelable event pathway still runs, no carried Source jar/ammunition cost. Context caster is Mage/LivingCaster, no player book admission implied. hasFocus recognizes any ISchoolFocus of matching school and **Shaper's Focus for every Mage**, then delegates others. Child resolver preserves school. Virtual focus branches do not grant every player-only passive/event or necessarily make every recipient effect usable.

### Exact added spell pools

Native part names abbreviated; all lists are ordered. Counts include the common Harm entry above: Air4, Fire5, Water6, Earth6.

| School | Subclass-added sequences |
| --- | --- |
|Air|Projectile→Launch→Duration Down→Delay→Wind Shear; Arc→Pierce×2→Lightning; Projectile→Spark→Amplify→Discharge.|
|Fire|Projectile→Ignite→Flare; Projectile→Ignite→Conflagrate; Arc→Split×2→Accelerate→Pierce→Firework→AoE→Amplify→Extend Time; Homing→Sensitive→Explosion→Amplify×2→Dampen.|
|Water|Homing→Sensitive→Bubble→Watery Grave→Extend Time→Duration Down; Projectile→Freeze→Cold Snap→Amplify; Projectile→Burst→Sensitive→Spike→Amplify; Projectile→Bubble→Delay→Cavitate; Projectile→Water Jet→Delay→Water Jet.|
|Earth|Projectile→Envenom→Poison Spores→Amplify; Projectile→Burst→Sensitive→Spike→Amplify; Projectile→Crush→Amplify×2; Projectile→Snare→Extend Time→Spike; Pantomime→Conjure Terrain→Amplify×2→Leap.|

Recipient formulas/boundaries are in [Fire/static](ars-elemental-pinned-fire-static.md), [Venom/Spores](ars-elemental-pinned-venom-spores-rage.md), [Cavitate/Grave](ars-elemental-pinned-cavitation-grave.md), [Jet](ars-elemental-pinned-jet-geyser.md), [Terrain/Spike](ars-elemental-pinned-terrain-spikes.md), [Homing](ars-elemental-pinned-homing.md)/[Arc](ars-elemental-pinned-arc.md), and native casting. A code preset is not proof every augment/effect executes successfully against every target/protection system. Terrain-changing spell threats require safe disposable-world acceptance, not live-world research tests.

## Timing and self support

Base melee goal activates/continues only within distanceSquared16 (four blocks) plus native MeleeAttackGoal rules. Ranged goal configured speed1.1/radius48/delay10/cooldown≤0; MageProjCastingGoal waits visibility counter≥20, animates then fires at max(1,delay/2)=5 animated ticks. It predicts half-tick target motion/half-eye-height, sets yaw/pitch and invokes performRangedAttack. That routine sets castCooldown20 regardless of onCast result. Goal uses strafe/unstuck navigation; timer values are not guaranteed1shot/sec or a measured DPS.

All get HealSelfCastGoal: at health≤0.33×max and selfCastCooldown≤0, **Self→Heal→Amplify**. SelfCastGoal casts on its first eligible tick after start rather than waiting its nominal delayTicks10, then sets shared selfCastCooldown60 even if cast fails. It caches supplied spell; no spell item is consumed. Fire additional priority2 self-goal at health<80% and a harmful milk-curable effect: **Self→Cauterize→Heal**. Water additional priority2 at health<80% without mana_bubble: **Self→Bubble Shield**. Their custom canUse predicates omit cooldown, but tick checks shared cooldown before attempting; goal scheduling/LOOK contention remains a runtime boundary.

Water shield is especially conditional: exact EffectBubbleShield.onResolveEntity only applies when target has native ManaCap. Inspected native CapabilityRegistry registers ManaCap for **Player**, not Mage, and no Mage-specific provider is added in inspected addon registrations. Thus don't promise a functioning infinite-Mage shield merely from goal preset; another installed mod could supply a capability and change result. Native entity resolver's enoughMana bypass does not itself supply ManaCap or a mana balance.

Cooldowns cast/selfCast save/read; school/pools come from type construction. Armor/hand state uses inherited entity equipment save. No summoned lifetime, player owner or noSave flag is introduced for these registered Mage types.

## Drops and quest boundaries

EntityMageBase.shouldDropLoot returns **false**, and no Mage-specific packaged loot-table JSON is provided. This is evidence against inventing a normal ingredient/rare-focus loot table, not a claim that vanilla equipment-drop/XP or every external loot listener is disabled: equipment handling is a distinct path and requires acceptance. Declared15baseXP is not a guaranteed15collectedXP for every death. Do not teach killing Mages as an established source of greater focuses, crafted armor, free books, Mark of Mastery or mandatory progression materials.

Optional encounter lesson: identify school/biome, carry appropriate matching focus and observe proactive hostility difference without attacking first; learn spell threat/escape, then choose whether to fight. Link Notebook Archwood/focus/glyph pages and supply the missing encounter instructions in quest text. Detection should be manual observation or explicit item-proxy for preparedness; no nonexistent boss advancement/item-drop detector. Modest XP and safe expedition/building supplies, no repeatable rare armor/focus reward farm.

Unrun positive/boundary cases: Y45/46/block light6/7 and actual cave spawning; held versus first-worn matching focus, multiple foci/retaliation/different schools; actual medium armor/hand save; all21pool entries including four common Harm copies; ranged visibility/failure cooldown, self healing/shared cooldown/contention; player-only vs Mage capability for Bubble Shield; terrain/claim/spell grammar/cancellation; vanilla equipment/XP versus no normal loot table and external drop handlers. No gameplay/runtime edits. Registry/recipe reconciliation, global Sauce selection, pilot/full profile review remain before addon completion.
