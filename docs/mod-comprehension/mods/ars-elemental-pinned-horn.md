# Ars Elemental 0.7.10.4 — Enchanter's Horn

**Version update:** Current0.7.11.3 sound-tuning delta and preserved cast-body comparison: [impact review](ars-elemental-pinned-07113-impact.md). Newly installed [Conflux](ars-conflux.md) combined behavior remains under research.

Exact addon SHA1 `562f640916aa6f5c83b3b3ee2384ca828b870e01`; native Ars5.13.5 and embedded Sauce0.0.61.100 [receipt](ars-elemental-pinned-guide-urn.md). SpellHorn, provider registration, AEDocumentation and recipe/lang; native ICasterTool/SpellResolver/SpellContext/LivingCaster/SpellStats, Sauce GlyphEffectUtil filter path and addon OrderEffect reviewed. No casting/cooldown/mana/client acceptance; selected global Sauce remains open.

## Craft and inscribe an effect fragment

Read Worn Notebook **Spell Casting → Enchanter's Horn**, plus native **Scribes Table** instructions; JEI **Enchanting Apparatus**. Wilden Horn reagent, pedestals Air Essence + three Gold Ingots + four Source Gem-tag items, SourceCost0, result one `ars_elemental:spell_horn`, keepNbtOfReagent=false. Optional casting tool after native glyph/workshop access, not a new book tier or compulsory unlock.

Horn has native SPELL_CASTER component/provider. On Scribes Table copy a spell **without any AbstractCastMethod** (effect/augment/filter fragment), from supported Spellbook/Spell Parchment through inherited ICasterTool.onScribe. This subclass rejects a fragment containing Self/Touch/Projectile/other method, then prepends MethodSelf to accepted recipe. It does not simply copy a complete Self→Heal book spell. Methodless composition/storage is the intended native crafting/UI path; actual UI acceptance must be tested. Tooltip presents stored spell/hidden state under native options. Packaged dye recipe exists, but SpellHorn does **not** implement IDyeable in its declared interfaces; exact appearance/render use of BASE_COLOR is not established by that recipe alone.

## Hold/release operation and range

Use starts item use; release with elapsed **j≥10ticks** and player entity executes routine. Under10 does not cast/add cooldown in this body. Nominal maximum200ticks: onUseTick auto-releases when elapsed>200 (strict), with declared long-use duration72000. Range parameter **0.5+j/10** inflates AABB of player's BlockPos, not spherical radius/player exact center. At10ticks inflate1.5; at150 inflate15.5; at200 inflate20.5. Real visible affected count depends on targets/filters and effects, not elapsed duration alone.

Server obtains caster, constructs **one** SpellResolver/SpellContext/LivingCaster from stored spell and player, then queries entities with Sauce filter predicate; default selects LivingEntity, including the player. No ally/line-of-sight filter is added here. It loops targets through onResolveEffect(EntityHitResult) and calls **expendMana once after the loop**. It does not debit per target. No jar Source, ammunition or item durability debit in this body.

After server work, long hold check grants native Ars **Spell Damage** effect for800ticks/amplifier0 when **j+50≥200**, i.e. **150ticks**, not only at maximum despite book wording. Sound/game-event fires, then cooldown200ticks (10seconds at20TPS). Sound volume parameter is range×16; this is not extra effect range. Buff/sound/cooldown code is outside explicit ServerLevel branch, so actual side synchronization remains a client test. Default empty caster is non-null; valid spell/effect success is not a precondition of this outer buff/cooldown branch. A null caster on server returns before it.

Horn also implements ISpellModifierItem: each modifier callback adds duration modifier1 with no school/tool identity gate. Native SpellStats collection includes equipment/hand items and worn Curios; holding Horn can therefore modify duration for another tool's stat build, not just Horn casts. It is an additive duration modifier consumed by each effect's formula, not guaranteed doubling of every spell duration.

## Admission, filtering and shared-context boundaries

This release routine directly calls native **onResolveEffect**, not onCast/canCast/postEvent. Native onResolveEffect invokes resolveAllEffects→resetCastCounter→resume; resume still posts **SpellResolveEvent.Pre**, per-effect events and recipient capability callbacks. It does **not** perform ordinary up-front enoughMana/grammar/SpellCastEvent admission on this route. Final expendMana computes whole spell cost/discounts/Post cost event and asks LivingCaster to remove mana, after effects; this is not a proof the player could afford it first. Payment can occur after canceled/rejected resolves and is requested once even if no eligible targets. Empty stored spell has no effect iteration; no invented normal glyph-learning gate is supplied by this item.

OrderEffect/Hymn of Order's inspected blocking listener cancels **SpellCastEvent**, while this Horn route does not post that event. Its native max-mana punishment still exists, and other resolve listeners can cancel effects. Record a targeted admission/lock boundary test; do not claim universal protection bypass or ship a runtime fix from research.

Sauce getFilterPredicate gets filters before first effect (cast methods skipped); when none, uses default LivingEntity predicate. With filters, **replaces** that default with legacy checkIgnoreFilters rather than ANDing it. This uses two-argument IFilter.shouldAffect, so [paired Elemental filter inversion discrepancy](ars-elemental-pinned-filters.md) applies: negative filters can behave like positive in this path. Other installed filter implementations may alter eligible entity types; not a guaranteed living-only query when a custom filter is present.

One resolver/context is reused for targets. Native resetCastCounter resets index0 and canceled=false, but not delayed flag or all context attachments. A simple immediate effect can restart at each target; Delay/propagation/attachment behavior must not be described as independent fresh casts for every target. Effect-specific owner/payment/claim/status restrictions remain intact where their recipients enforce them; not every entity in the box necessarily receives the whole sequence.

## Quest / unrun acceptance handoff

Teach optional group support tool: exact craft → methodless simple beneficial fragment → Scribes Table Horn → hold10/release near own consenting target → observe actual status and player mana → test longer hold/buff/cooldown. Name Horn Notebook, native Scribes Table and JEI Apparatus. Possession is craft proxy; manual operation acknowledgement unless true cast/effect detector is later implemented. Modest XP and ordinary workshop supplies; no automatic high-tier glyph/rare drop grant.

Unrun cases: methodless fragment accepted/complete spell refused; hold9/10/149/150/200/auto>200; cuboid boundary/obstruction/self/ally; low/no mana versus post-effect debit, canceled resolution with debit, empty spell and no targets; Hymn SpellCast-only guard versus resolve listener; default/positive/negative filters and custom nonliving predicate; repeated immediate effect versus Delay/context reuse; Horn held while another tool casts; dye result component versus visible render. No runtime changes or acceptance claims. Shield/enchantments/other gear/worldgen/dependency/pilot review remain.
