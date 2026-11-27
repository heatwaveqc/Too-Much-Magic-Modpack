# Ars Conflux 0.1.56 — carrier conversion and reflection interface

Exact Conflux SHA1 `97f3c31c624df19209db648e4fe6dd2e67e08d80`: [profile](ars-conflux.md). Direct CFR0.152: BuiltinCarriers/CarrierProfile/CarrierRegistry, ProjectileForm/Dispatch, ProjectileConversionMixin, SpellValidators/SpellValidationMixin, ProjectileReflections/ReflectionRules/ReflectionStamp, HomingReflectionMixin/ReflectedCarrierHitMixin/ReflectedDamageCreditMixin, CastContexts.reflected and optional Zero shield wrapper. [Mana/budget recipient](ars-conflux-pinned-mana.md), [current Elemental Phalanx](ars-elemental-pinned-phalanx-worldgen.md). No conversion/reflector/owner/payment/gameplay acceptance.

## Registered profile is not an installed spell

Eleven built-in carrier profiles declare integration behavior. Native Projectile: drivenfalse/repeatedfalse/areafalse. Native Orbit:true/true/true. Elemental Arc and Homing:true/false/false; Phalanx:true/false/true. ArsOmega Missile:true/false/true; NEG Trail:true/true/true. Propagators for ArsOmega Projectile/Missile and Elemental Arc/Homing point at emitted carrier ID and are nonrepeated; Missile area-enabled. ArsOmega/NEG profile records do not install those absent mods/glyphs.

Base profile augments are Amplify/Dampen/Accelerate/Decelerate/Split; driven adds Pierce/Sensitive, area adds AoE/ExtendTime/DurationDown, propagator adds Extract. Expansion runs AugmentEquivalence. **This is profile declaration, not evidence every native glyph's original compatibleAugments is overwritten.** CarrierProfile separately snapshots original supported augments during setup. Current Elemental's native Phalanx excludes Pierce/Amplify; a consumer's profile/validator can differ, so quote exact applicable layer.

CarrierRegistry rejects duplicate IDs. Resolver source binding attaches a registered glyph ID through ResolverContexts; unknown binding throws. Lookup prefers attached source, otherwise castType; native Orbit entity is recognized specially. Projectile with leading augment can resolve matching propagator record by emitted ID. No extra inventory item, tier or recipe is created by attaching profile.

## Conversion dispatch and one-payload rule

ProjectileForm is an opt-in interface extending TriggerPaidGlyph, with replace(projectile,profile) and initialCharge; default firstCharge delegates initialCharge. **No implementing ProjectileForm class was found inside exact Conflux binary.** Actual replacement payload/content must come from a consumer; do not invent a Conflux survival projectile craft from this API.

Server ProjectileDispatch accepts native EntityProjectileSpell with resolver and ProjectileForm augment in first phrase. Zero forms returnsfalse; more than1returns true immediately; exactly1requires recognized carrier and calls form.replace under payload school scope. It does not itself transfer ingredients or impose one universal fee; booleantrue means conversion/handling claimed, not proof of successful spawned replacement.

Normal MethodProjectile.summonProjectiles addFreshEntity call is wrapped as `convert(entity) OR originalAdd(entity)` with short-circuit; true prevents original native spawn and returns success. Constructor registers LOWEST join listener canceling event if convert reports true, covering other EntityProjectileSpell spawns. More-than-one-form handling can consequently suppress original entity; it is not a guaranteed fallback to plain Projectile. No atomic rollback/refund promised if converter mutates state then returnsfalse or its replacement spawn fails.

StandardSpellValidator RETURN appends one-payload-per-phrase errors plus registered validators; never removes native errors. This normally catches multiple ProjectileForms before spawn, but malformed/admin/other-tool paths still require boundary tests. A valid grammar is separate from actual resource/ownership/spawn success. The [mana report](ars-conflux-pinned-mana.md) explains conditional VolleyBudget Split quote; this source declaration is not another automatic charge.

## Managed reflection eligibility

ProjectileReflections.reflect is an API for **managed sessions/trajectories/CarrierProxy**, not a generic enchantment granting reflection to every projectile. It resolves actual carrier/visual through ArsTrajectory or proxy and optional weakly tracked ExecutionSession. No session/motion/proxy →UNMANAGED. Client/removed projectile/null normal/cross-level reflector/inactive session →REJECTED.

ReflectionRules requires finite nontrivial speedSquared>1e−7and normalSquared>1e−12; reflected velocity **v−2(v·n)/(n·n)n**, so normal need not be unit length. Linear native carrier is eligible only when actual class inherits both tickNextPosition/getNextHitPosition from EntityProjectileSpell, and profile is not repeated. ReflectableCarrier delegates canReflect; native Homing mixin accepts only exact declaring-method pattern, rejecting specialized trajectory overrides. Do not assume Orbit/Phalanx/Arc are compatible merely because carrier profile exists.

Whole projectile group must contain subject, all members must map by identity to same carrier(or be subject), and share level. Heterogeneous flights reject rather than partially reflecting. Custom carrier rejection happens before group mutation. No Source/mana fee/cooldown within this reflection API; caller/active execution account can still charge separately.

## Owner, resolver and account changes

Successful reflection picks reflector as new owner, otherwise current projectile living owner. With carrier/resolver and living owner, builds reflected CastContext/child resolver. **Original caster, executor and resource account remain; damageOwner changes to reflector.** Reflection is not a transfer of original mana bill to defender.

Moves each member0.3along reflected direction, sets common reflected velocity/impulse, Projectile owner and native spell-damage context/orientation. Session.damageOwner changes; hidden carrier receives velocity/position adjustment, redirected flag, reflection-version stamp, owner and replacement resolver; custom reflected callback receives previous/new owner. No retry/rollback if an external callback fails after these mutations.

DamageSource.getEntity RETURN maps declared damage owner only when it equals original caster/executor under reflected scope; unrelated declared owner preserved. Additional MobDamageCredit remaps matching-UUID FakePlayer proxies for active nonplayer mana caster. This is attributable damage-source behavior, not proof every effect/summon/drop event honors a new owner or that all rewards move to reflector.

ReflectedCarrierHitMixin wraps native onHit, recording reflection version; attemptRemoval during that collision is canceled only if stamp changed during it. Nested prior value restored finally. It doesn't grant indefinite no-despawn, prevent unrelated removal or automatically stamp arbitrary native bounce.

HomingReflectionMixin resets current target. On first managed reflection saves original ignored predicates and original owner; subsequent ignore predicate rejects **Conflux allies of new owner**, or original predicates except for original owner's entity. This permits original shooter to become target while protecting reflector's alliance; original filters otherwise remain. Conflux alliance is native team/owner/provider, **not Sauce Book list**, as [content report](ars-conflux-pinned-content.md) establishes. Old predicate snapshots/closures can preserve original state; repeated bounces and target ownership need observation.

## Installed versus optional reflector boundary

Only direct reflect API caller found in this exact binary is **optional Ars Zero ArcaneShieldEntity wrapper**. UNMANAGED delegates Zero original, REFLECTEDreturnstrue, REJECTEDreturnsfalse; startup Zero switchfalse also delegates. Current tracked pack has no Ars Zero pin; this API path is not proof of an installed default reflector. Other consumer binaries may call public API and remain combined-scope follow-up. Do not install Ars Zero to demonstrate a research branch.

No inspected call connects Elemental Mirror Shield enchantment/SpellMirror prism directly to this API. Therefore previous Elemental native ownership/payment/overload findings remain qualified; Conflux HomingReflectionMixin by itself does not repair native mirror's wrong onHit overload or automatically reassign owner when Elemental's own callback flips velocity. Managed reflection and native reversal are distinct routes.

## Player value, teaching and unrun tests

This is mostly an interface contract: existing native/Elemental projectile work can participate when a compatible consumer supplies payload/session/reflector. Do not create fictitious new Conflux weapon, workbench, altar or mandatory reflection quest. Native Notebook glyph entries/Spell Book editor remain references; enabled Phalanx acquisition stays in [content report](ars-conflux-pinned-content.md). Proposed optional lesson only after identifying an actual installed consumer: compose at most1payload per phrase, cast with known budget, observe actual carrier replacement/owner and charge. Item possession is proxy; positive XP/ordinary supplies, no repeated reflection payout.

Acceptance: no/one/twoProjectileForms with normal validator and malformed bypass; convertertrue/false/denied spawn; native Projectile versus Homing/Orbit/special trajectory; original/resolved carrier IDs/propagators. Reflect managed/unmanaged, zero/NaNnormal, removed/client/cross-level, shared versus mixed group. Verify velocity/speed/displacement, caster/account retained while owner/damage attribution changes, original shooter reacquisition/new-owner allies, repeated reflection and collision-removal stamp. Observe personal mana/source/cooldown via actual caller, not API alone. No launches/tests performed.

Remaining Conflux research: actual installed external consumers, active casting/cooldown/geometry/schools and installed Controle/Additions/nSpells bridges. This source-backed interface report does not promote whole-profile completion.
