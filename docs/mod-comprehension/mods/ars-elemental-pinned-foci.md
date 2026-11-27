# Ars Elemental 0.7.10.4 — acquiring and selecting foci

Exact Elemental artifact/hash and guide access: [guide/Urn receipt](ars-elemental-pinned-guide-urn.md). This slice audits recipes, focus class modifiers, mana event selection and greater-focus passive conditions. Glyph replacement handlers, summoned entity internals and armor still need their own complete operational audit; [profile](ars-elemental.md) remains researching. No runtime tests.

## Acquisition and upgrades

Open Worn Notebook → **Spell Casting** → the desired elemental Focus; each entry includes lesser recipe, empowering text and greater recipe. **Armor → Mark of Mastery** supplies the upgrade material recipe. [Native Imbuement/Apparatus setup](ars-nouveau-pinned-crafting.md) applies. Imbuement's nearby pedestal catalysts are **reusable**; Apparatus consumes reagent and pedestal ingredients.

| Exact recipe `ars_elemental:` | Input / nearby catalysts or consumed pedestals | Output and Source |
| --- | --- | --- |
|`lesser_fire_focus`, `lesser_water_focus`, `lesser_air_focus`, `lesser_earth_focus`|Imbuement: one Amethyst Shard input; three matching native elemental Essences and three Gold Ingots as reusable nearby catalysts|one corresponding `lesser_*_focus`; 5,000 Source|
|`imbuement_anima_essence`|Imbuement: one `c:gems/source` input; Wither Skeleton Skull, Bone Meal and Golden Apple reusable catalysts|one **`sauce:anima_essence`**; 3,000 Source|
|`mark_of_mastery`|Imbuement: **one Wilden Tribute input**; one each Earth/Fire/Water/Air/Abjuration/Conjuration/Manipulation Essence plus **`sauce:anima_essence`** as eight reusable catalysts|**five** `mark_of_mastery`; 10,000 Source|
|`fire_focus`, `water_focus`, `air_focus`, `earth_focus`|Apparatus: corresponding lesser focus reagent; **one** Mark of Mastery pedestal|one corresponding greater focus; 5,000 Source; `keepNbtOfReagent:true`|
|`necrotic_focus`|Apparatus: native Summoning Focus reagent; two Wither Roses, one Wither Skeleton Skull and one **`sauce:anima_essence` tag** ingredient on pedestals|one `necrotic_focus`; 0 Source; keepNbt false|

Mark of Mastery therefore reaches the native Wilden boss route, not just four early essences. See [native rituals/boss](ars-nouveau-pinned-rituals.md). The native Summoning Focus precursor recipe is Apparatus: `c:storage_blocks/source` reagent; Wilden Horn, Spike, Wing, **Tribute** and `c:ingots/gold` pedestals; zero Source. This is a separate boss-material expense from a Mark.

The addon aliases `ModItems.ANIMA_ESSENCE` to Sauce's registry holder. Embedded Sauce `ModRegistry` registers `sauce:anima_essence` and an old `ars_elemental:anima_essence` registry alias. Do not rewrite the current recipe IDs to the older namespace.

## Equip and verify the specialization

`ElementalCurio` implements Curios `ICurioItem`; its use-to-equip hook rejects a stack identical to the entity's current offhand stack. Use the Curios equipment UI for an explicit worn focus. Holding and wearing are not equivalent for every benefit.

`ElementalFocus.applyItemModifiers` checks each resolved spell part against **ELEMENTAL** and the focus's school: matching part gets configurable amplification (default **+1**), another elemental part gets default **−1**, non-elemental part is unchanged. This is a spell-stat modifier, not a universal damage multiplier. `GreaterElementalFocus` overrides it: matching school gets **twice** the configured boost (default **+2**); it applies no other-school malus. Thus greater upgrade removes the lesser tradeoff as well as improving matching stats. Air foci also return true from Curios' powdered-snow walking hook.

`Events.focusDiscount(SpellCostCalcEvent)` is server-side and player-only. It asks Sauce for the **first focus**; if **any part of the spell** belongs to that focus's school, it sets:

`currentCost = (int)(currentCost − focusDiscount × spell.getCost())`

Discount defaults: lesser **0.15**, greater **0.25**, Necrotic **0.25**. This subtracts a fraction of the spell's base cost from the current event cost, including mixed-school spells once any part matches; it is not a per-matching-glyph sum or multiplication of the already-discounted cost. Cast-to-int truncation applies. This handler has no local zero floor; downstream native payment behavior and other listeners still matter. Multiple equipped foci do not make this handler sum discounts.

The **embedded** Sauce `ISchoolFocus.getFirstFocus` searches both hands in enum order, then the first matching Curios slot. A hand-held focus can therefore win over a worn focus for this cost handler. Its separate `getFociSchools` unions both hands and only the first matching Curio; an entity implementing `ISchoolProvider` returns its own schools directly. These selection functions differ from the parent resolver's modifier enumeration. Compare one school at a time and remove competing held foci for a clear acceptance setup.

## Greater focus passives

`GreaterElementalFocus.curioTick` only runs its effects for a **server-side Player wearing it**, with `EnableRegenBonus` true (binary default). Every 20 player ticks it visits its own school and subschools:

| School | Required condition | Effect refresh (ticks; zero-based amplifier) |
| --- | --- | --- |
|Fire|On fire, in lava **or** has addon Magic Fire|native Spell Damage: 200, amplifier 1|
|Water|In water, rain or bubble; swimming selects stronger branch|Mana Regen: 120, amplifier 0; swimming instead Mana Regen 120/1 plus Dolphin's Grace 200/1|
|Air|Has native Shocked; otherwise Y strictly greater than 200|Shocked: Mana Regen and Spell Damage both 60/1; high-altitude branch Mana Regen 120/0|
|Earth|Y strictly less than 0|Mana Regen 120/0|

The Earth greater focus additionally returns a **+0.2 additive Knockback Resistance** attribute modifier. Air's Shocked branch is stronger than the guide's broad Mana Regen I wording. `DamageEvents.handleHealing` separately multiplies player healing by **1.5** when focus-school selection contains Earth, and cancels healing for Frozen entities. That school check does not restrict itself to greater foci. These effects do not imply immunity to the hazardous condition used to trigger them.

## Necrotic focus boundaries

`NecroticFocus.applyItemModifiers` adds **damage modifier +1 for every part**. For Necromancy-school parts it adds duration modifier **+2** and amplification **+2** specifically for Heal, addon Phantom and native Summon Undead. Its school discount uses the same first-focus event above.

`hasFocus` is a different test: server-side Player and **any worn Curios stack** whose item is NecroticFocus. Holding it alone does not satisfy this test. `SummonEvents.reRaiseSummon` uses it when a non-`IUndeadSummon` dies: native SummonWolf → SummonDirewolf, EntityAllyVex → AllyVhexEntity, SummonSkeleton → SummonUndead; inherit old summon data and spawn. Already-undead summons are excluded, so this is not an indefinitely repeated resurrection branch. The new entity classes' lifetime/AI and inheritance details remain for the summon audit.

`NecroticFocus.castSpell` also requires the worn-focus test and exactly the **Homing Projectile cast method**. For owned `IUndeadSummon` mobs in the player's block-position box inflated by 30, it aims at the mob target, else the player's last hurt mob; if neither is alive it passes the player's view vector to LookControl. It clones the event context, substitutes the summon as wrapped caster and calls `EntitySpellResolver.onCast(ItemStack.EMPTY, world)`. Do not promise free casts, successful target hits or inherited payment from this caller alone; resolver/payment/entity endpoints remain to audit.

The subscribed `lifeSteal(LivingDeathEvent)` heals a resolved player owner **2 health** when the damage-source entity implements `IUndeadSummon` and has an owner UUID in a server level. **This method does not check whether the owner still wears a Necrotic Focus.** It creates a follow-projectile visual, not an additional heal-on-arrival transaction.

## Source scope and next acceptance

Direct classes: ElementalFocus, GreaterElementalFocus, NecroticFocus, ElementalCurio, Events.focusDiscount, DamageEvents.handleHealing, SummonEvents.reRaiseSummon; packaged recipes and English focus pages. Elemental's `META-INF/jarjar/metadata.json` embeds **Sauce 0.0.61.100**, minimum range `[0.0.61,)`. Extracted embedded JAR: 248,060 bytes, SHA256 `58ce7a36fc6fb7631b35765d58227b2982ece031559fa4122f1391a17811b251`, CFR externally `elemental-sauce-fresh`; inspected ISchoolFocus/registry recipient ends above. This proves the bundled dependency implementation, **not which version NeoForge selects across every installed addon's bundled dependencies**. That final dependency-selection reconciliation remains open.

Future explicit acceptance: actual config values; lesser matching/other/non-elemental glyph stats; mixed-spell mana formula and held-versus-worn precedence; exact Y200/Y0 boundaries; greater passive refresh, wet/swimming and Shocked branches; Necrotic holding versus wearing, eligible first death versus undead death, owner unequip before kill and Homing replay payments. Pilot 35 only detects Lesser Fire Focus possession, not specialization operation; greater and Necrotic branches have no corresponding pilot tutorial. Glyph empowering, head-cut loot, steed substitutions and summon internals remain open rather than being inferred from translated descriptions.

Follow-up: [undead recipient/payment audit](ars-elemental-pinned-undead-summons.md) now closes the inherited lifetime and Homing replay endpoints left open above, including Vhex shadow-counter limitation. Addon Homing glyph acquisition remains separate.
