# Ars Elemental 0.7.10.4 — six paired entity filters

Exact artifact/hash [receipt](ars-elemental-pinned-guide-urn.md). Direct ElementalAbstractFilter and six concrete classes, all12 packaged glyph recipes and three entity tags; exact native AbstractFilter/IFilter/AbstractSpellPart, bundled Sauce GlyphEffectUtil recipient. Bundled dependency version scope remains in [focus receipt](ars-elemental-pinned-foci.md). No runtime tests.

## Acquire and place the filter

Search Worn Notebook glyph names **Aerial, Aquatic, Fiery, Insect, Summon, Undead**, and corresponding **Not** versions. Learn via [native Scribes Table](ars-nouveau-pinned-guide-glyphs.md). All12 recipes: **XP27**, inherited default **Tier1**, mana **0**, no compatible augments from native AbstractFilter. Output/recipe IDs are `ars_elemental:glyph_<family>_filter` and `glyph_not_<family>_filter`.

Every positive recipe consumes **Allow Scroll + family ingredient**; every negative consumes **Deny Scroll + same ingredient**. Native Allow Scroll shapeless recipe is Blank Parchment+`c:chests`; Deny Scroll is Blank Parchment+`c:cobblestones`. These are consumed glyph-recipe inputs, not live item filters loaded with mobs.

| Family | Recipe family ingredient | Exact entity predicate P |
| --- | --- | --- |
|aerial|Phantom Membrane|LivingEntity whose type is in `ars_elemental:aerial`|
|aquatic|`minecraft:fishes` item tag ingredient|LivingEntity whose type is in **`minecraft:aquatic` entity tag**, not merely swimming/standing in water|
|fiery|Blaze Powder|LivingEntity whose type is in `ars_elemental:fiery` **or** `fireImmune()` true; not simply on fire or having Fire Resistance status|
|insect|Spider Eye|LivingEntity whose type is in `minecraft:arthropod` **or** `ars_elemental:insect`|
|summon|Bone|Entity implements native **ISummon**; not generic tamed pet, custom name or owner tag alone|
|undead|Rotten Flesh|LivingEntity whose type is in **`minecraft:undead`** entity tag; not simply a skeleton-looking model or inverted healing|

Place filter **after cast method and before effects it should gate**. Normal addon onResolve calls the contextual five-argument shouldAffect and cancels continuation on false. Positive permits P, negative permits **not P**. This disagrees with the concrete classes' generated descriptions saying positive stops “if” P / negative “unless” P; the actual XOR implementation is authoritative for these inspected callers. Do not copy the reversed description into new quest instructions.

## Concrete tag contents and target boundaries

Packaged aerial tag lists Phantom, Wither, Bat, Allay, Ender Dragon, Parrot, Ghast, Vex, Bee, native Wilden Stalker/Boss and addon Flashjack. It does not dynamically test whether a creature is airborne. Fiery tag explicitly lists Ender Dragon; other naturally fire-immune living entities can pass via the additional runtime predicate. Insect tag contains `#minecraft:arthropod`. These are addon contributions, not a complete assembled-pack tag dump; other datapacks can extend membership.

ElementalAbstractFilter.shouldResolveOnBlock returns **false**. Contextual shouldAffect computes **`inverted != super.shouldAffect(...)`**: positive filters reject block hits, **negative filters allow block hits**. Likewise a nonliving entity generally fails positive family predicate and passes negative, except Summon's interface check can accept nonliving implementers. A negative filter is not automatically “living creatures outside the family”; combine another explicit native filter if a narrower target set is required.

Normal sequential filters form an intersection: every reached gate must pass. An Aerial then Not Aerial gate leaves no contextual entity match; different families can overlap (Bee is an example in packaged Aerial and Arthropod classification). This is not a union/OR selector by merely adding more positive filters.

## Following filters in Homing and Gravity

Bundled Sauce GlyphEffectUtil.getFilters scans from supplied index, skips AbstractCastMethod, collects consecutive IFilter entries, and stops at the first other AbstractEffect. When installed Controle is detected, it invokes its adaptive-filter skip helper; [Controle's audited grammar](ars-controle-pinned-glyphs-sensor.md) applies. Do not assume a filter placed after an ordinary effect participates in the earlier seek selection.

Contextual checkIgnoreFilters calls every collected filter's **five-argument shouldAffect**, ANDs results, returns the inverse to mean “ignore.” [Homing](ars-elemental-pinned-homing.md) collects from index0; [Gravity field](ars-elemental-pinned-empowering.md) uses current index+1. A filter can therefore influence target acquisition/field attraction as well as later effect continuation. Other built-in ignores (owner, allies, players without Sensitive, etc.) still apply; a passing Aerial filter does not force Homing to target Flashjack, which has an explicit basic ignore.

### Legacy helper overload does not apply inversion

The addon overrides only **contextual five-argument shouldAffect**. Native IFilter's deprecated two-argument shouldAffect dispatches straight to shouldResolveOnEntity/Block; the addon's two-argument entity method calls its raw contextual predicate with null context, bypassing XOR. Bundled Sauce also has a deprecated-style **two-argument checkIgnoreFilters** and getFilterPredicate calling that helper. For those callers, the negative instance tests the **same raw P as positive**, and both reject blocks. Do not promise polarity equivalence across contextual versus legacy API consumers. Homing/Gravity audited above use the contextual helper; other device consumers require their own callsite trace.

## Acceptance and coverage

Future explicit acceptance: all12glyph acquisitions; allow/deny recipe tags; family membership in assembled pack; positive/negative entity/block behavior versus reversed descriptions; multiple sequential filters; placement before/after effects; Controle adaptive grammar; contextual versus legacy consumers. These findings close12 paired-filter recipe routes with native limitations explicit. Combined with [Arc](ars-elemental-pinned-arc.md) and Homing, **16of39** addon glyph recipe routes are source-audited; **23 other effect/summon glyphs**,12tomes and other major addon systems remain researching. No new quests/config/tag changes or gameplay acceptance.
