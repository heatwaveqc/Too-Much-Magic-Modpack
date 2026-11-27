# Ars Conflux 0.1.56 — incoming geometry and declared school power

Exact Conflux SHA1 `97f3c31c624df19209db648e4fe6dd2e67e08d80`: [profile](ars-conflux.md). CFR0.152: TriggerGeometry, resolver/projectile geometry mixins, SchoolPlatform/Registry/Definition/Weights/GlyphSchools, SchoolData/ReloadListener/Commands, SchoolDamage/Rule, PowerAttributes and SpellPower. Exact JAR data inventory and current tracked config/defaultconfigs/kubejs inspected. [Carrier/reflection](ars-conflux-pinned-carriers.md), [mana](ars-conflux-pinned-mana.md). No direction/damage/reload/command/gameplay acceptance; no data/config changes.

## Direction is context, not a new physical input

TriggerGeometry stores normalized incoming vector under `ars_conflux_incoming` in SpellContext.tag. Null context/direction makes write no-op; nonfinite or lengthSquared<1e−10clears current and legacy `irons_nouveau_incoming`. canonical helper requires>1e−10, otherwiseZERO. Read prefers currently scoped vector, then Conflux tag, then legacy tag; absent returnsZERO. Stored tag read normalizes values but does not perform the same finite guard as write, so malformed externally written values need validation at their producer/consumer.

EntityProjectileSpell.resolver RETURN on server writes direction into returned resolver context: use deltaMovement if lengthSquared>1e−10, otherwise nextHitPosition−position, normalized. This snapshots projectile travel, not caster look or target normal. ResolutionGeometryMixin captures current scoped direction at native SpellResolver.onResolveEffect HEAD. Scoped helper normalizes, runs action and restores prior scope through ScopedContext. Shared/mutated child contexts and external consumers determine how that vector is used; capture does not automatically rotate every effect.

The library adds no consumed vector item, mana fee or range in these methods. Native claim checks, placement location, caster owner and actual spell effects remain separate. Zero/Hex/other optional direction adapters must be evaluated only for installed recipients; a legacy key is compatibility support, not proof that another mod is present. No permission to add missing target mods or change spell geometry during research.

## Schools are explicitly declared weights

Conflux school registry is distinct from native Ars spellSchools. SchoolRegistry API defaults plus datapack definitions are combined with datapack overriding sameID; associate links native school IDs for consumers, without automatically changing original glyph's native memberships. GlyphSchools gets data override then API default; **undeclared glyph returnsEMPTY**, not automatic native-school inference.

SchoolWeights accepts finite nonnegative numbers, drops zero, sorts IDs and normalizes positive shares (scales by max first to avoid overflow). Allzero→EMPTY. Weighted multiplier is sum(share×factor), not product; EMPTY→1. Negative/nonfinite values rejected. Example weightsFire2/Water1normalize2/3and1/3, not two simultaneous fullstrength schools. This is an authoring/API contract, not a new survival specialization/unlock.

No SchoolRegistry.register/associate or GlyphSchools.declare call sites were found inside exact Conflux binary. Startup `schools.power_attributes` defaults empty; only explicitly listed IDs register extra per-school percentage attributes on startup. The eight other family binaries had no direct Conflux class references in prior [consumer scan](ars-conflux-pinned-active-casts.md), but other pack binaries/reflection/data still require combined review. Do not promise all native Fire/Water gear gains new Conflux school bonuses.

## Reload resources and selection order

SchoolReloadListener reads JSON directory **`data/<namespace>/ars_conflux/`**:

| Subpath | Contract |
| --- | --- |
| `schools/<id>.json` | Definition ID namespace+remaining path; name(defaultID), translation_key(defaultempty), RGBcolor(defaultCCCCCC, integer or hex), ars_schools native ID list |
| `glyph_schools/<id>.json` | That glyph ID with `weights` object schoolID→nonnegative number |
| `school_damage/<id>.json` | Rule ID, requiredschool/multiplier, optionaltarget_entity/target_tag/priority(default0) |

Reload handles invalid individual resources with warning and omits them from new snapshot; it does not preserve that resource's previous valid rule automatically. Rules grouped by school, priority descending then ruleID lexical; first matching rule supplies factor. EntityID and entityTag, when both supplied, are **AND** constraints. Finite nonnegative multiplier required. No matching rule→1.

Exact Conflux JAR has only four recipe JSONs under its own data namespace and no packaged school definitions/glyph weights/damage rules. No matching tracked Conflux configuration/school data found in current integration c11a95fc config/defaultconfigs/kubejs bounded path search; generated/effective datapacks and unrelated first-party binaries remain separate. This is not permission to author rules in research or claim an arbitrary vulnerability table already installed.

## Damage power and optional balance rules

General attribute `ars_conflux:confluence_spell_power` is always registered, default0/range−1024..1024/syncable; per-school attributes use `ars_conflux:school_power/<namespace>/<path>` only for startup-listed schools. They format percent values; no recipe/gear in this report grants a value. **Multiplier=max(0,1+generalBonus+sum(weightedSchoolBonus))**. Zero/negative net suppresses qualified damage. This is separate from native glyph school flat power or Elemental focus effects.

Server `schools.damage_rules` defaultsfalse; it only controls multiplication by configured target-vulnerability rule factors. General/per-school SpellPower layer can apply independently if attributes/profile exist. With defaults0and no data, arithmetic factor1; configfalse is not a universal disable switch for every Conflux damage hook.

SchoolPlatform listens at LOWEST to native SpellDamageEvent.Pre. Skip canceled/client/missing target/nonfinite or nonpositive damage and events SpellPower flags as already handled. Prefer current school scope, otherwise damage source direct-entity binding/current fallback; apply caster's SpellPower, then optional target-rule factor, clamp floatMax. This preserves damage type/armor event flow; no automatic bypass-invulnerability.

SpellPower scopes carry original caster/executor/damageOwner/schools, can bind nonliving spawned entities and restore scope during their tick. Native damage wrapper resolves current versus direct-entity profile, keeps original hurt when no eligible profile, and uses handled/ARS_EVENT flags to avoid intended double application. Scoped finally restores previous state; weak payload maps clear by unload/stop. These guards do not prove compatibility with all unrelated event handlers or every delayed entity path. Actual duplicate/ordering/damage amount requires two-sided runtime test.

## Diagnostics and teaching

Operator permission2commands **`/conflux schools list`** and **`/conflux schools glyph <id>`** report declared definitions/weights; undeclared output is informative, not missing glyph registry or player research failure. Commands do not teach a survival guide, grant a glyph or enable rule data. No server/admin commands were run during this research.

Ordinary player reference remains native Worn Notebook/Spell Book glyph descriptions and enabled provider's own guide; no extra mandatory schoolbook, altar or crafting stage established. A future quest can explain an installed declared school/geometry benefit only after actual provider/data and item route are identified. Inventory proves acquisition only; manual observed effect/damage/direction plus positive XP/ordinary supplies is honest. Do not reward admin rule editing or create new balance tables here.

Unrun tests: zero/nonfinite/scoped/legacy direction, stopped projectile fallback, nested resolves and child context retention; registered versus undeclared glyphs, weight2:1normalization/zero/invalid resource warning/reload removal, equal-priority first match and entity+tagAND. Compare defaultfactor1/attributebonus/rulesfalse versustrue, overlapping native events and delayed spawned nonliving effects, reflection damage-owner/account distinction and unload cleanup. Observe final health after armor/other handlers separately from event arithmetic. No assembly/server/client/gameplay tests performed.

Conflux remains researching for actual installed consumers and Controle/Additions/nSpells two-ended bridge/default-scope consolidation; ability/adapter-specific paths are not closed by this general school/direction interface report.
