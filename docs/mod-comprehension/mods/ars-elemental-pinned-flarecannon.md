# Ars Elemental 0.7.10.4 — Flarecannon guard and familiar

Exact addon SHA1 `562f640916aa6f5c83b3b3ee2384ca828b870e01`, native Ars5.13.5 SHA1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; [artifact/guide](ars-elemental-pinned-guide-urn.md). Inspected FirenandoCharm, FirenandoEntity including anonymous resolver, FireCannonGoal/ProjCastingGoal/CastGoal, FirenandoHolder/FirenandoFamiliar, ArsNouveauRegistry, ConfigHandler, recipe/advancement/lang and native EntitySpellResolver/familiar/Binding code. javap confirms the anonymous resolver calls native SpellContext constructor; CFR's extra outer argument is synthetic, not a missing API constructor. No gameplay/client/ownership acceptance.

## Acquire and place a wandering guard

Native Worn Notebook **Crafting → Flarecannon Charm** has intro, entity display and crafting pages. JEI **Enchanting Apparatus** recipe `ars_elemental:firenando_charm`: Magma Block reagent; pedestals two Fire Essences, one Netherite Scrap and two Nether Brick items; sourceCost0; keepNbtOfReagent=false; result one `ars_elemental:firenando_charm`. Netherite Scrap makes this an optional Nether-resource workshop unlock, not a free first-day pet.

Use charm on a block with a player present. It spawns Firenando at click coordinates, sets home to clicked BlockPos and owner to that player's UUID, and copies charm color/custom name. Inherited AbstractSummonCharm consumes one on SUCCESS unless infinite materials; no-player returns FAIL. Existing SummoningTile uses the same routine: no special station block is created or required. Entity addition's return is unchecked, so consumption is not proof of a successfully retained entity.

Use Dominion Wand connection workflow to supply a stored block position to the guard as first endpoint; onFinishedConnectionFirst updates home and sends home-set feedback when storedPos is non-null. This callback has no explicit owner check or distance clamp. Guard's GoBackHomeGoal uses12 and runs when target is null. Do not interpret home as a territory permission boundary, guaranteed chunk loading or an exact combat leash.

Guard has maxHealth80, armor20, movement0.2, followRange16; never despawns for distance and base XP0. It targets Mob instances implementing Enemy, declares non-Enemy entities allied, and otherwise delegates alliance to superclass. This is a class/predicate rule, not faction/claim/PvP compatibility validation.

## Attack and recovery

Its fixed spell is **Homing Projectile → Ignite → Sensitive → Conflagrate → Amplify → Knockback**. The entity constructs native EntitySpellResolver with itself as LivingCaster and overrides hasFocus to recognize only the greater Fire Focus item. This is a resolver capability, not an equipped/consumed focus requirement. It directly constructs/adds the homing projectile rather than calling ordinary player onCast. No ammo, jar Source or player-mana withdrawal is coded in this attack method; native entity resolver enoughMana=true. Actual impact/cancelable resolution and recipient effects remain those in [Homing](ars-elemental-pinned-homing.md) and [Fire glyph audit](ars-elemental-pinned-fire-static.md), not an unconditional damage promise.

Projectile speed0.8/inaccuracy0.8, cast stats set to Sensitive, native/basic ignore predicates plus non-Enemy and same-owner Firenando exclusions. The last predicate calls getOwner().equals, so ownerless entities created outside normal charm flow are an edge to test. Magma versus Soul appearance changes particle/texture choice; both report Fire school, no separate soul-damage formula is introduced here.

FireCannonGoal is configured movement0.8, attack radius55, delay20; the attackInterval constructor argument20 is not forwarded/used. Base casting goal requires active/cooldown≤0 and target. ProjCastingGoal tracks visibility, starts strafing after20 visible ticks within range, and approaches otherwise. FireCannonGoal waits seeTime≥40 to begin animation, fires at animatedTicks10 and finishes at20. performRangedAttack sets cooldown20. Therefore “one shot each second” is not established: reacquisition, visibility, animation and cooldown all matter; target selection's followRange16 also differs from the goal's55 radius.

| Condition | Result / cost |
| --- | --- |
|Active regeneration|Server every20 world ticks, heal1 if below max.|
|Ordinary fatal damage|Sets inactive, clears attacking Mob target if present, sets health0.1 and returns rather than ordinary death. Inactive isNoAi=true and hurt rejects damage.|
|Inactive recovery|Server every100 world ticks heal1; once at max, ACTIVE=true. About80 healing passes from0.1 is a code estimate, not measured uptime.|
|Feed inactive helper|Any player, not just owner, may spend one Magma Cream **or Blaze Powder** to heal100 and reactivate. This branch is outside the owner-only appearance block; no infinite-material exemption in its shrink.|
|Owner kills|Default flarecannon_owner_kill=true: fatal damage whose source entity UUID equals owner drops a **fresh** charm and calls superclass death instead of dormancy. Bypasses-invulnerability fatal damage also goes to superclass death without that charm branch.|
|Dispel|Server, not removed: drops fresh charm and discards helper, without caster-owner check. Unlike creation fromCharmData, recovery does not attach color/name data.|
|Recolor|Owner server interaction spends one Magma Block for Magma or Soul Sand for Soul if different; same-color falls through. No explicit creative exemption.|

Owner, home, cooldown, active and color are saved/read. Default new guard is active/Magma. No special serialization guarantee is inferred for unchecked spawn or other mods' effects.

## Familiar: actual cost formula differs from its book

Notebook **Familiars → Flarecannon**, **Ritual Index → Binding**. Holder `ars_elemental:firenando_familiar` matches any FirenandoEntity, including inactive/no owner check. Follow [exact Binding/script/summon chain](ars-elemental-pinned-siren.md#bind-learn-and-summon-the-siren-familiar) with this holder ID: Binding consumes original guard, plain bound script does not preserve appearance/home, main-hand use learns in player data, spellbook familiar screen summons owned companion. It does not keep wandering-guard targeting/fixed cannon attacks. Native familiar reserve/lifetime/dismissal are covered in [parent audit](ars-nouveau-pinned-familiars.md).

Per onModifier callback, adds2 damage modifier to owner's Fire-school parts. Native FamiliarEvents has **two** subscribed SpellModifierEvent dispatchers; do not promise final+2 damage without accounting for both and the recipient damage formula.

Book says projectile spell cost reduced20%%; actual onCostCalc subtracts **50% of whole spell's base getCost()** from event.currentCost, casts result to int. Requires alive familiar, owner matches context unwrapped caster and **first part** is in registered list: Arc, Homing Projectile, native Projectile, Propagate Homing, Propagate Arc. A qualifying propagation glyph later in a different first-form spell is insufficient. This is not50% of already-discounted current cost, nor guaranteed final50% after all other modifiers. Native cost dispatcher is a separate single callback in FamiliarEvents; the two damage dispatchers do not by themselves duplicate cost reduction.

Owner can spend one Magma Cream on familiar to apply Fire Resistance1200ticks (60seconds at20TPS), amplifier0. Blaze Powder is accepted by inactive worker recovery, **not** this familiar buff method. Owner can recolor familiar with Magma Block/Soul Sand. These interactions shrink1 without explicit infinite-material exception in the subclass.

## Quest and unrun boundary handoff

| Learn/action/reference | Detection and reward direction | Acceptance to run later |
| --- | --- | --- |
|Craft Apparatus charm; Notebook Crafting/Flarecannon and JEI|`ars_nouveau:firenando_charm` inventory advancement (parent enchanting_apparatus) proves possession only. Modest XP and patrol construction blocks.|Craft exact scrap/essence recipe; summon with owner/home; blocked spawn/ownerless boundary.|
|Set home with wand and observe hostile guard attack|Manual operation acknowledgement; no claim charm task detects combat. XP and modest repair ingredients.|Enemy target versus non-Enemy; line of sight/visibility delay; moving home and owner permissions; same-owner projectile ignore.|
|Recover disabled guard, distinguish owner kill/Dispel|Manual observation; small XP/repair reward, no unlimited charm farming reward.|Inactive regeneration and each recovery food; nonowner repair versus recolor; owner-kill config true/false; bypass fatal source; recovered fresh charm loses appearance.|
|Bind, learn and summon familiar; named Familiars/Binding pages|Script possession proxy explicitly separated from actual familiar buff. XP and modest notebook/workshop supplies.|Guard sacrificed, duplicate knowledge preserves script; first-form projectile versus later propagation cost;20%book versus actual base-cost subtraction; Fire modifier double dispatch; Magma Cream versus Blaze Powder.|

No new runtime/quest/config edits or combat tests. Flashjack, Flashing Weald Walker and the remaining addon systems still prevent whole-addon source-complete status.
