# Ars Elemental 0.7.10.4 — armor acquisition, attributes and damage response

Exact hash-pinned Elemental binary and bundled Sauce 0.0.61.100 recipient scope: [guide receipt](ars-elemental-pinned-guide-urn.md), [dependency receipt](ars-elemental-pinned-foci.md). Direct 48 packaged armor recipes, ElementalArmor/ArmorSet/three weight classes/AAMaterials/ArsNouveauRegistry; Sauce ElementalArmorRecipe, JEI category, IElementalArmor, ManaDiscountMixin, AttributeEventHandler and DamageEventHandler. Parent exact Ars 5.13.5 AnimatedMagicArmor/ArmorPerkHolder and [native armor walkthrough](ars-nouveau-pinned-armor-threads.md). No runtime acceptance. Addon remains researching; [three addon threads](ars-elemental-pinned-threads.md) now have a separate audit; other systems still require review.

## Build a chosen school and weight

Worn Notebook → **Armor** → school set shows all four pieces in each of three weights; **Mark of Mastery** supplies its recipe. The choices are branches, not sequential light→medium→heavy upgrades. Sauce registers `sauce:armor_upgrade` among native Apparatus recipe types and supplies a separate JEI Armor Upgrade category with Apparatus catalyst. `excludeJei:true` on the recipe excludes ordinary display, not all JEI access; its dedicated category shows tier-2 internal input and the Source cost.

1. Choose native **Sorcerer → light**, **Arcanist → medium**, or **Battlemage → heavy**. Craft that family's piece and perform **both** native armor upgrades: internal0→1 using two blaze-rod-tag ingredients/2,500 Source, then internal1→2 using two ender-pearl-tag ingredients plus Chorus Fruit/5,000 Source. See native armor page for base recipes, apparatus and thread installation.
2. Obtain Marks via Wilden Tribute → **five Marks**, with eight reusable essence catalysts/10,000 Source; [focus/material route](ars-elemental-pinned-foci.md). This puts the Elemental route after a boss-material unlock even though the pilot only asks for one hat.
3. On the Apparatus, use the matching **internal-tier-2** native piece as reagent. Four consumed pedestals: **one Mark of Mastery + one Netherite Ingot + two matching school Essences**. Supply **7,000 Source per piece**. All 48 recipes have this pattern and omit `tier`; serializer default3 requires `input ArmorPerkHolder.getTier()==3−1`. A visually identical newly crafted unupgraded native piece fails this extra gate.
4. Collect corresponding Elemental piece, equip in its armor slot and inspect the school/weight tooltip. Four pieces cost four Marks, four Netherite Ingots, eight school Essences and **28,000 Source for the conversion stage**, plus base crafting/native upgrades and Mark creation. This is recipe arithmetic, not a tested throughput claim.

Current IDs are `ars_elemental:<prefix>_<suffix>`. School prefixes: **fire, aqua, air, earth** (Water armor uses **aqua**, not water). Each output's recipe path equals its item path.

| Weight / native family | Head | Chest | Legs | Feet |
| --- | --- | --- | --- | --- |
|Light / sorcerer|`hood`|`tunic`|`pants`|`shoes`|
|Medium / arcanist|`hat`|`robes`|`leggings`|`boots`|
|Heavy / battlemage|`helmet`|`chestplate`|`leggings_heavy`|`boots_heavy`|

Example pilot item **`ars_elemental:fire_hat`** takes **`ars_nouveau:arcanist_hood`**, not a Sorcerer hood or Battlemage helmet.

## Retention and thread capacity

`ElementalArmorRecipe` extends native EnchantingApparatusRecipe with keep-components enabled. `assemble` invokes parent assembly, reapplies the catalyst's component patch when nonempty and sets damage to zero. It does **not** call `ArmorPerkHolder.setTier(3)`; recipe tier, tooltip's displayed fixed **4**, and stored holder tier are separate. Do not claim a new stored tier merely because this is a higher equipment family. Existing threads/color/enchantment component patches travel through this route; effective behavior and invalid components still require acceptance.

`ElementalArmor.getMinTier()` returns2. `ArsNouveauRegistry.makePerkList` repeats the same slot list four times, so these providers offer the same capacity at all four registered holder indexes. Parent ArmorPerkHolder sorts slots descending:

| Weight | Head / feet slots | Chest / legs slots |
| --- | --- | --- |
|Light|3,2,1|3,2,2|
|Medium|3,2,1|3,2,1|
|Heavy|2,2,1|3,2,1|

Use native Alteration Table to install/recover threads; highest-valued slot is filled first. The separate [Spore/Shock/Summon thread audit](ars-elemental-pinned-threads.md) covers recipes, slot effects and the broken advertised sickness reduction. Light/heavy tooltip explicitly warns their models/textures are work in progress and currently resemble medium; a shared model does not prove the same item stats.

## Per-piece casting and material stats

ElementalArmor explicitly adds default **100 Max Mana**, **4 Mana Regen attribute value** (configurable `armorMaxMana`/`armorManaRegen`) and **0.15 matching-school discount attribute** per equipped piece, using native armor modifier IDs. Parent AnimatedMagicArmor also builds tier-dependent mana modifiers with those IDs before the subclass additions; do not add the parent values again without checking final modifier replacement/effective attributes.

Bundled `IElementalArmor.applyItemModifiers` adds **+0.5 amplification** per matching-school resolved part per piece. Its old `getDiscount(recipe)` helper describes 20% matching glyph costs, but this alone is not the active payment route. **ManaDiscountMixin** injects at native `ManaUtil.getPlayerDiscounts` return: for a real non-fake Player, add integer generic discount plus, for each glyph, `ceil(glyphCastingCost × min(sum of that glyph's school discount attributes,1))`. This is separate from the focus's whole-base-spell cost subtraction. Multi-school glyph attributes sum within that glyph, capped at1; no promise of zero-cost casting follows without downstream native cost calculation.

| Weight | Own-school defense / power added per piece | Other-school defense | Durability multiplier; armor points H/C/L/F |
| --- | --- | --- | --- |
|Light|+12.5 / +2|weakness −25|30; 2/6/5/2|
|Medium|+25 / +1|weakness −12.5|35; 3/8/6/3|
|Heavy|+50 / +0.5|resistance +25|50; 4/10/7/4|

Weakness map: Fire→Water, Water→Air, Air→Earth, Earth→Fire. Heavy's extra resistance map: Fire→Earth, Water→Fire, Air→Water, Earth→Air. These are **attribute values**, not direct percent reductions. Sauce's `SpellDamageEvent.Pre` defense handler finds matching damage-tag schools and multiplies spell damage by **`2^(−resistance/100)`** for each applicable nonzero attribute; separate spell-resistance modifier also applies. Its power handler adds school power to the spell builder's **damage modifier**, distinct from the per-piece amplification above.

## Tagged incoming damage and set response

ArsNouveauRegistry maps school absorption to Sauce Fire/Water/Air/Earth damage tags. In bundled `DamageEventHandler.elementalDamageReduction` (LOW priority), only pieces whose school's damage tag matches the incoming source count. Let light/medium/heavy matching counts be L/M/H; `B=floor(0.5L+M+1.5H)`. A matching set may mix weights; four pieces must match **the school**, not an identical weight item set, for school effects.

For Player targets, with damage not bypassing enchantments and at least one school absorption:

- Four Fire and fire-tag damage: clear fire; if focus schools include Fire, refresh Spell Damage II for200 ticks.
- Four Water and exactly drowning damage: refill air and add4 to B. Four Air and fall-tag damage: add4 to B.
- Four Earth, eye Y strictly below20, food strictly below4: set food to20. This is inside the matching-damage handler, not passive underground food generation.
- When invulnerableTime≤10: at least two light pieces refresh Spell Crit Up for200 ticks, amplifier `L/2−1`; at least two medium refresh Discount Mana similarly. With B≥2, add native Mana Regen for200 ticks. Also restore **original incoming amount×B mana** if H≥2 or B≥4. With at least three heavy pieces, overflow mana can heal `min(overflow×H/100,10)` health.

Finally, for any target with B>0, non-bypass damage and original amount>0.1, multiply the current event amount by **`1−B/10`**, floor result at0.1. This is an additional tagged incoming-damage path, separate from spell-school attribute resistance. It uses original incoming damage for mana, not measured damage actually prevented. Four-medium matching pieces give B4, four-heavy B6 before Water/Air extra bonuses. Do not label it unconditional immunity or require a uniform-weight set for the code's school test.

Remaining explicit runtime acceptance: tier-gated recipe and JEI/Notebook display; component/thread/enchantment retention and damage reset; actual combined mana attributes/discounts; school tag definitions/effective resistance; mixed weight versus mixed school; bypass/invulnerability/Y/food boundaries; overflow healing and hunger integration. Pilot37 detects hat possession only; it cannot establish these full-set effects. Global bundled Sauce selection is still unresolved, so dependency-side findings retain the exact embedded-version scope above.
