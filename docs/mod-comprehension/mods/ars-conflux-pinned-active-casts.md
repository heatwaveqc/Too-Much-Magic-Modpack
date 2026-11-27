# Ars Conflux 0.1.56 — preparation, dispatch and cooldown interfaces

Exact SHA1 `97f3c31c624df19209db648e4fe6dd2e67e08d80`: [profile](ars-conflux.md). CFR0.152: ActiveCasts including Pending/Attempt/Sequence, ActiveCastPolicy/Guard/Provider/Request, PersistentCooldowns, CastingTiming/ChantDurations/TimingAttributes; native caster/bow/crossbow/sword/scry hooks; AnsActiveCasts and plugin gate. [Mana](ars-conflux-pinned-mana.md), [carrier contracts](ars-conflux-pinned-carriers.md). No observed chanting/dispatch/death persistence or gameplay acceptance.

## What is actually supplied

This is an opt-in provider framework around existing native tools. **No ActiveCasts.register or PersistentCooldowns.register caller was found inside exact Conflux binary.** All eight other current family JARs were scanned for class bytes containing `dev/arsconflux`; none had direct references. Therefore these inspected family artifacts establish wrappers/interfaces, not a concrete extra chant duration or cooldown provider. Other pack binaries, reflection/dynamic registration and first-party consumers remain unscanned; this is not a whole-pack no-provider proof.

Conflux does register attributes `ars_conflux:cast_time_reduction` and `cooldown_reduction`, default0/range−10..1/syncable, attached to available entity attribute sets. No item/recipe in this report grants reduction. Native tools retain their original preparation/cooldowns unless actual applicable provider/hook changes them. Do not invent a new Conflux wand, altar or compulsory wait quest.

## Preparation and pending use

ActiveCastPolicy.DEFAULT enables chanting and cooldowns. Provider registry rejects duplicate IDs and sorts by ID; provider.supports(spell) controls participation. Cooldown rejection chooses greatest remainingTicks, displays provider message red for ServerPlayer. Quotes sharing key merge maximum duration and remaining, not sum across duplicates.

`perform` checks provider cooldown eligibility before queuing and again before execution. Chant participants are supporting providers with chantingEnabled. None →UNNEEDED/immediate original action. With contributions, clamp each duration≥0; server config chant_mode defaults **MAXIMUM**, optional SUM adds every occurrence with intMax saturation. Then apply cast-time reduction.

Timing scale: nonpositive duration→0; nonfinite reduction→0; clamp reduction−10..1;1removes delay,0keeps duration, otherwise ceil(ticks×(1−reduction)), minimum1/saturatedmax. Negative reductions lengthen, e.g.−0.5makes10ticks15. Cooldown uses same scaling with own attribute. This formula requires an actual duration from provider/caller; attribute alone doesn't create cooldown.

Pending records caster UUID, original ServerLevel, hand, **held ItemStack identity plus full copy snapshot**, start time, ticks, participants and guard. Dead caster/global ACTIVEsize≥4096/current cooldown rejection denies new queue. If a previous valid pending use exists, ordinary defer returnsQUEUED for it; it does not replace spell or reset timer. Sequence preparation instead denies while valid pending exists. Bad prior pending canceled before replacement.

Default guard requires same exact held object and ItemStack.matches snapshot, not merely same item ID. Pending valid also requires alive/notremoved/sameworld; ServerPlayer connected/nonspectator; custom guard true; every participant still chantingEnabled. Moving positions alone isn't checked; changing level/held object/components/count can cancel. No blanket “movement interrupts casting” claim.

Server tick copies pending list, cancels invalid; elapsed world gameTime≥duration cancels/removes pending+sync0then runs release callback. Otherwise progress sync every20elapsed ticks. Stop/unload cancels matching pending. Preparation framework itself does not reserve mana or charge ingredients; providers/progress callbacks can have own side effects. No generic refund/rollback is implied by cancellation.

## Acknowledged release, not just an input click

Execution installs one thread-local Attempt; nested same-caster call shares it. Resolver dispatch HEAD attaches policy/checks eligibility; successful boolean return acknowledges release and remembers resolver spell. Native Scry acknowledgement occurs after original expendMana call; Bow/Crossbow acknowledges only successful addFreshEntity for EntitySpellArrow with resolver. A spell that launches but later hits nothing can still be a released operation. False dispatch doesn't automatically reverse independent costs already paid elsewhere.

On normally returning outer action, if release acknowledged and cooldown policy enabled, call supporting providers.released and keyed afterRelease callbacks. Release actions are putIfAbsent(key), avoiding duplicate key actions in same attempt. If provider.released throws, scheduled release actions are still run then error propagated. If original action itself throws, CURRENT restores finally, but normal-return commit section isn't reached. Prior world/payment effects are not undone.

Sequence API aggregates phase spells for admission/chant once, checks caster alive/sameworld again, and executes admitted phases under inherited policy. First acknowledged release commits provider cooldowns for full planned sequence through ActiveUseCommit; after-release actions still run. Closing sequence prevents future open work, not a refund of earlier phases. Actual optional Zero/Hex sequences and provider-specific costs need their own installed-recipient proof; absent targets do not become gameplay branches.

## Existing native entry points

| Entry | Scope of wrapper |
| --- | --- |
| AbstractCaster / ScryCasterData castSpell | ActiveCasts.perform, CASTER_TOOL, explicit denied versus queued item result |
| SpellBow / SpellCrossbow releaseUsing | Derive held hand by stack identity and registry caster; valid tool enters preparation, otherwise original routine |
| SpellCrossbow performShooting | ActiveCasts.execute without a second defer; acknowledges spawned arrow |
| Enchanter's Sword hurtEnemy resolver.onCastOnEntity | Wrap **spell** with ActiveCasts.execute/WEAPON, not the whole physical hit; no automatic chant on melee |
| Native resolver onCast/onCastOnBlock/onCastOnEntity | Policy admission and success acknowledgement; direct onResolveEffect isn't one of these entry points |

Consequently native reactive/passive resolution isn't automatically a new active chant. Elemental Horn direct-resolve branch isn't silently granted new admission by these three resolver methods; other effect/billing listeners still apply. Do not generalize wrapper coverage to every item API.

## Persistent deadline API

PersistentCooldowns.register requires unique namespace and dataKey; duplicate/blank rejected. No own namespace registration caller found in this JAR. Opt-in stores write actor persistent CompoundTag deadlines using **server Overworld gameTime**, allowing same clock across dimensions. remaining=max(0,deadline−now); extend only server/positive durations, keeps max(existing,new deadline), saturates longMax. extend scales once; extendRaw bypasses scale. commit derives adjusted duration/key and schedules after acknowledged release if inside matching attempt, otherwise extends immediately; disabled policy skips commit.

Clone hook at LOWEST copies registered stores. Nondeath clone always merges maximum deadlines; death default preserve_on_deathtrue, false removes store on replacement. This does not claim vanilla item cooldown manager or native Iron/Goety cooldown storage is rewritten, nor verify offline/server-reset/reload semantics. Real provider-selected storage and actual lifecycle acceptance remain necessary.

## nSpells adapter boundary

AnsMixinPlugin admits OptionalAns hooks only when installed CrossCastingHandler.castArsSpell has expected target-hint/origin signature. AnsActiveCasts uses reflective carrier/proxy entry points and reentrancy guard. Server carrier route snapshots carrier, then at release rejects changed carrier, sleeping/spectator/player menu other than inventory or changed held tool. Proxy initiation checks native can-cast/current-casting and rechecks same carried book/snapshot before native initiate; release uses ActiveCasts.execute. Weapon versus caster-tool source derives Sword/DiggerItem.

This traces local guarding intent; **matching current3.3.5signatures, full reflection API availability, native resource/cooldown ledger and actual installed provider registration remain combined-audit work**. Returning queued/refused is not proof native initiation happened. No arbitrary carrier swap or doubled-billing compatibility promise.

## Player teaching and unrun tests

When actual provider identified, use existing native Spell Book/Scribes/tool pages and that provider's guide; explain holding same tool through visible preparation, genuine release acknowledgement and effective cooldown. Inventory tasks prove tool acquisition only. Optional manual observable cast/deadline lesson with small positive XP/ordinary supplies; no repeated cancellation/payment reward.

Test no-provider immediate native route versus known provider, MAXIMUM/SUM contributions and reduction boundaries; same pending repeated click versus changed spell/tool/components, moving/sameworld versus dimension transfer, disconnect/spectator/death/unload, zero duration/4096boundary. Compare false/true resolver result, denied arrow spawn, scry debit acknowledgement, melee physical damage versus rejected spell. Verify exactly-once keyed cooldown action, original exception/provider exception, sequence first-release commit, deadline same clock across dimensions/deathclone true/false/reload. Keep world mutation, resource debit and cooldown credit separate. No assembly/launch/tests performed.

Conflux remains researching for geometry/schools and actual installed external consumers/Controle/Additions/nSpells bridges; this report documents generic active-cast contracts without claiming a default extra casting delay.
